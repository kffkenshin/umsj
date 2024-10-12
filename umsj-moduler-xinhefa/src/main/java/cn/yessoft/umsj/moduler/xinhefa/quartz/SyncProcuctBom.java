package cn.yessoft.umsj.moduler.xinhefa.quartz;

import static cn.yessoft.umsj.common.exception.util.ServiceExceptionUtil.exception;
import static cn.yessoft.umsj.moduler.base.enums.ErrorCodeConstants.O_NOT_EXISTS;

import cn.hutool.db.Db;
import cn.hutool.db.Entity;
import cn.yessoft.umsj.common.utils.BaseUtils;
import cn.yessoft.umsj.common.utils.json.JsonUtils;
import cn.yessoft.umsj.moduler.base.entity.BaseDictDetailDO;
import cn.yessoft.umsj.moduler.base.service.IBaseDictDetailService;
import cn.yessoft.umsj.moduler.xinhefa.entity.XhfItemDO;
import cn.yessoft.umsj.moduler.xinhefa.entity.XhfProductBomDO;
import cn.yessoft.umsj.moduler.xinhefa.entity.XhfProductProcessDO;
import cn.yessoft.umsj.moduler.xinhefa.entity.XhfProductProcessRouteDO;
import cn.yessoft.umsj.moduler.xinhefa.enums.XHFSyncJobDictConfig;
import cn.yessoft.umsj.moduler.xinhefa.service.IXhfItemService;
import cn.yessoft.umsj.moduler.xinhefa.service.IXhfProductBomService;
import cn.yessoft.umsj.moduler.xinhefa.service.IXhfProductProcessRouteService;
import cn.yessoft.umsj.moduler.xinhefa.service.IXhfProductProcessService;
import cn.yessoft.umsj.quartz.core.handler.JobHandler;
import com.google.common.collect.Maps;
import jakarta.annotation.Resource;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.Lists;
import org.quartz.DisallowConcurrentExecution;
import org.springframework.stereotype.Component;

@Component
@DisallowConcurrentExecution
@Slf4j
public class SyncProcuctBom implements JobHandler {

  @Resource private IBaseDictDetailService baseDictDetailService;

  @Resource private IXhfItemService xhfItemService;

  @Resource private IXhfProductProcessService xhfProductProcessService;

  @Resource private IXhfProductProcessRouteService xhfProductProcessRouteService;

  @Resource private IXhfProductBomService xhfProductBomService;

  private static final Integer EACH_QTY = 200;
  private Map<String, Long> proessesMap;

  @Override
  public String execute(String param) throws Exception {
    List<XhfProductProcessDO> processes = xhfProductProcessService.list();
    proessesMap = new HashMap<String, Long>();
    processes.forEach(
        i -> {
          proessesMap.put(i.getProcessNo(), i.getId());
        });
    BaseDictDetailDO lastRunTime =
        baseDictDetailService.getByDictIdAndDetailKey(
            XHFSyncJobDictConfig.PRODUCT_BOM.getDictId(),
            XHFSyncJobDictConfig.PRODUCT_BOM.getDetailKey());
    if (lastRunTime == null) {
      throw exception(O_NOT_EXISTS, "BOM同步定时配置");
    }
    String now = BaseUtils.getCurrentTimeStr();
    // 上次执行的时间
    Map<String, String> lastrun = Maps.newHashMap();
    lastrun.put("lastrun", lastRunTime.getDetailValue());
    List<Entity> result = Lists.newArrayList();
    if ("INIT".equals(lastRunTime.getDetailValue())) {
      List<Entity> count = Db.use().query(INIT_SQL_COUNT);
      int counts = count.size();
      for (int e = 0; e * EACH_QTY <= counts; e++) {
        result = Db.use().query(INIT_SQL, EACH_QTY * e);
        handleResult(result);
      }
    } else {
      result = Db.use().query(GETNO_SQL, lastrun);
      handleResult(result);
    }
    // 记录本次时间
    if (!result.isEmpty()) {
      lastRunTime.setDetailValue(now);
      baseDictDetailService.updateById(lastRunTime);
    }
    return JsonUtils.toJsonString(result);
  }

  private void handleResult(List<Entity> result) throws SQLException {
    List<XhfProductBomDO> results = Lists.newArrayList();
    int count = 1;
    if (BaseUtils.isNotEmpty(result)) {
      for (Entity i : result) {
        log.info("===" + count++ + "////" + result.size());
        String itemNo = i.getStr("主件编号");
        XhfItemDO pitem = xhfItemService.getItemByNo(itemNo);
        if (pitem == null) {
          log.error("找不到料号" + itemNo);
          continue;
        }
        // 先删除旧的
        xhfProductBomService.deleteByItemId(pitem.getId());
        List<XhfProductBomDO> insert = create(pitem);
        if (!insert.isEmpty()) results.addAll(insert);
      }
      if (!result.isEmpty()) xhfProductBomService.insertBatch(results);
    }
  }

  private List<XhfProductBomDO> create(XhfItemDO item) throws SQLException {
    List<XhfProductBomDO> results = Lists.newArrayList();
    List<Entity> boms = Db.use().query(SQL, item.getItemNo());
    if (BaseUtils.isEmpty(boms)) {
      return results;
    }
    // 更新通道数
    item.setChannelsCount(boms.get(0).getShort("通道数"));
    xhfItemService.updateById(item);
    boms.forEach(
        bom -> {
          XhfProductBomDO bomDo = new XhfProductBomDO();
          bomDo.setItemId(item.getId());
          bomDo.setBaseNumber(bom.getBigDecimal("主件底数"));
          XhfItemDO mitem = xhfItemService.getItemByNo(bom.getStr("材料料号"));
          if (mitem != null) {
            bomDo.setMaterialId(mitem.getId());
          }
          XhfProductProcessRouteDO route =
              xhfProductProcessRouteService.getByItemIdAndProcessNo(
                  item.getId(), bom.getStr("作业编号"));
          if (route != null) {
            bomDo.setProcessRouteId(route.getId());
          }
          Integer channulNumber = bom.getInt("通道数");
          if (BaseUtils.objtoint(item.getChannelsCount()) < channulNumber) {
            item.setChannelsCount(channulNumber.shortValue());
          }
          bomDo.setSeq(bom.getInt("材料项次"));
          results.add(bomDo);
        });
    xhfItemService.updateById(item);
    return results;
  }

  // todo  dsdata 需要改成可以配置的 用formatter{0}处理,目前为了main好测试 先不做
  public static final String SQL =
      "SELECT  bmbaent 企业编号,bmbasite 营运据点,bmaa001 主件编号,"
          + "        imaal_t1.imaal003 主件产品名称,"
          + "        imaal_t1.imaal004 产品规格,"
          + "        imaa_t1.imaa009 产品分类编码,"
          + "        rtaxl_t1.rtaxl003 产品分类名称,"
          + "        bmaa004 生产单位,"
          + "        DECODE(imaf016,'1','1:设计','2','2:试样','3','3:试产',"
          + "                          '4','4:量产','5','5:停产','6','6:失效',"
          + "                          imaf016) 生命周期,"
          + "        bmba009 材料项次,"
          + "        bmba007 作业编号,"
          + "        bmba008 作业序,"
          + "        bmba003 材料料号,"
          + "        imaal_t2.imaal003 材料名称,"
          + "        imaal_t2.imaal004 材料规格,"
          + "        imaa_t2.imaa009 材料分类编码,"
          + "        rtaxl_t2.rtaxl003 材料分类名称,"
          + "        bmba011 组成用量,"
          + "        bmba012 主件底数,"
          + "        bmba010 材料单位,"
          + "        DECODE(bmba013,'1','1:主要材料','2','2:次要材料',"
          + "                       '3','3:间接材料','4','4:参考材料',"
          + "                       bmba013) bmba013, "
          + "        imaa_t1.imaa019 产品长度,"
          + "        imaa_t1.imaa020 产品宽度,"
          + "        imaa_t1.imaa021 产品厚度,"
          + "        imaa_t1.imaaud013 产品密度,"
          + "        imaa_t1.imaa017 产品颜色数量,"
          + "        CASE WHEN imaa_t1.imaa020 <> 0 AND (imaa_t2.imaa009 LIKE '2%' AND imaa_t2.imaa009 NOT LIKE '20204%')"
          + "             THEN ROUND(imaa_t2.imaa020 / imaa_t1.imaa020)"
          + "             ELSE 0"
          + "         END AS 通道数,"
          + "         pmaal004 客户简称,"
          + "         bmba026 ECN变更单号,"
          + "         bmfacnfdt ECN变更单审核日期"
          + "  FROM  DSDATA.bmaa_t          "
          + "  LEFT JOIN DSDATA.bmba_t ON bmbaent = bmaaent AND bmbasite = bmaasite AND bmba001 =bmaa001"
          + "  LEFT JOIN DSDATA.imaa_t imaa_t1 ON imaa_t1.imaaent = bmbaent AND imaa_t1.imaa001 = bmaa001"
          + "  LEFT JOIN DSDATA.imaal_t imaal_t1 ON imaal_t1.imaalent = bmaaent AND imaal_t1.imaal001 = bmaa001 AND imaal_t1.imaal002 = 'zh_CN'"
          + "  LEFT JOIN DSDATA.rtaxl_t rtaxl_t1 ON rtaxl_t1.rtaxlent = imaa_t1.imaaent AND rtaxl_t1.rtaxl001 = imaa_t1.imaa009 AND rtaxl_t1.rtaxl002 = 'zh_CN'  "
          + "  LEFT JOIN DSDATA.imaf_t ON imafent = bmaaent AND imafsite = bmaasite AND imaf001 = bmaa001"
          + "  LEFT JOIN DSDATA.imaa_t imaa_t2 ON imaa_t2.imaaent = bmbaent AND imaa_t2.imaa001 = bmba003"
          + "  LEFT JOIN DSDATA.imaal_t imaal_t2 ON imaal_t2.imaalent = bmbaent AND imaal_t2.imaal001 = bmba003 AND imaal_t2.imaal002 = 'zh_CN'"
          + "  LEFT JOIN DSDATA.rtaxl_t rtaxl_t2 ON rtaxl_t2.rtaxlent = imaa_t2.imaaent AND rtaxl_t2.rtaxl001 = imaa_t2.imaa009 AND rtaxl_t2.rtaxl002 = 'zh_CN'  "
          + "  LEFT JOIN DSDATA.bmce_t ON bmceent = bmbaent AND bmcesite = bmbasite AND bmce001 = bmba001"
          + "                  AND bmce002 = bmba002 AND bmce003 = bmba003 AND bmce004 = bmba004"
          + "                   AND bmce007 = bmba007 AND bmce008 = bmba008 AND bmce005 = bmba005"
          + "  LEFT JOIN DSDATA.imaguc_t ON imagucent = bmaaent AND imaguc001 = bmaa001 AND imagucstus = 'Y'"
          + "  LEFT JOIN DSDATA.pmaal_t ON pmaalent = imagucent AND pmaal001 = imaguc007 AND pmaal002 = 'zh_CN'"
          + "  LEFT JOIN DSDATA.bmfa_t ON bmfaent = bmbaent AND bmfasite = bmbasite AND bmfadocno = bmba026"
          + " WHERE bmaaent = 100 AND bmaasite = 'PHXHF' AND bmaastus = 'Y'"
          + "   AND imaa_t1.imaa009 LIKE '1%' "
          + "   AND bmba006 IS NULL"
          + "   AND imafstus = 'Y' AND imaf016 = '4'"
          + "   AND bmba019 <> 2 "
          + " and bmaa001 = ? ";

  private static final String GETNO_SQL =
      "SELECT  bmaa001 主件编号 "
          + "  FROM  DSDATA.bmaa_t          "
          + "  LEFT JOIN DSDATA.bmba_t ON bmbaent = bmaaent AND bmbasite = bmaasite AND bmba001 =bmaa001"
          + "  LEFT JOIN DSDATA.imaa_t imaa_t1 ON imaa_t1.imaaent = bmbaent AND imaa_t1.imaa001 = bmaa001"
          + "  LEFT JOIN DSDATA.imaal_t imaal_t1 ON imaal_t1.imaalent = bmaaent AND imaal_t1.imaal001 = bmaa001 AND imaal_t1.imaal002 = 'zh_CN'"
          + "  LEFT JOIN DSDATA.rtaxl_t rtaxl_t1 ON rtaxl_t1.rtaxlent = imaa_t1.imaaent AND rtaxl_t1.rtaxl001 = imaa_t1.imaa009 AND rtaxl_t1.rtaxl002 = 'zh_CN'  "
          + "  LEFT JOIN DSDATA.imaf_t ON imafent = bmaaent AND imafsite = bmaasite AND imaf001 = bmaa001"
          + "  LEFT JOIN DSDATA.imaa_t imaa_t2 ON imaa_t2.imaaent = bmbaent AND imaa_t2.imaa001 = bmba003"
          + "  LEFT JOIN DSDATA.imaal_t imaal_t2 ON imaal_t2.imaalent = bmbaent AND imaal_t2.imaal001 = bmba003 AND imaal_t2.imaal002 = 'zh_CN'"
          + "  LEFT JOIN DSDATA.rtaxl_t rtaxl_t2 ON rtaxl_t2.rtaxlent = imaa_t2.imaaent AND rtaxl_t2.rtaxl001 = imaa_t2.imaa009 AND rtaxl_t2.rtaxl002 = 'zh_CN'  "
          + "  LEFT JOIN DSDATA.bmce_t ON bmceent = bmbaent AND bmcesite = bmbasite AND bmce001 = bmba001"
          + "                  AND bmce002 = bmba002 AND bmce003 = bmba003 AND bmce004 = bmba004"
          + "                   AND bmce007 = bmba007 AND bmce008 = bmba008 AND bmce005 = bmba005"
          + "  LEFT JOIN DSDATA.imaguc_t ON imagucent = bmaaent AND imaguc001 = bmaa001 AND imagucstus = 'Y'"
          + "  LEFT JOIN DSDATA.pmaal_t ON pmaalent = imagucent AND pmaal001 = imaguc007 AND pmaal002 = 'zh_CN'"
          + "  LEFT JOIN DSDATA.bmfa_t ON bmfaent = bmbaent AND bmfasite = bmbasite AND bmfadocno = bmba026"
          + " WHERE bmaaent = 100 AND bmaasite = 'PHXHF' AND bmaastus = 'Y'"
          + "   AND imaa_t1.imaa009 LIKE '1%' "
          + "   AND bmba006 IS NULL"
          + "   AND imafstus = 'Y' AND imaf016 = '4'"
          + "   AND bmba019 <> 2 "
          + " and( bmfacnfdt >= TO_DATE(:lastrun, 'YYYY-MM-DD HH24:MI:SS')"
          + "      OR  BMAACRTDT >= TO_DATE(:lastrun, 'YYYY-MM-DD HH24:MI:SS')    "
          + "      OR  BMAAMODDT >= TO_DATE(:lastrun, 'YYYY-MM-DD HH24:MI:SS')    "
          + "      OR  IMAFCRTDT >= TO_DATE(:lastrun, 'YYYY-MM-DD HH24:MI:SS')    "
          + "      OR  IMAFMODDT >= TO_DATE(:lastrun, 'YYYY-MM-DD HH24:MI:SS')    "
          + "      OR  IMAGUCCRTDT >= TO_DATE(:lastrun, 'YYYY-MM-DD HH24:MI:SS')    "
          + "      OR  IMAGUCMODDT >= TO_DATE(:lastrun, 'YYYY-MM-DD HH24:MI:SS')    "
          + ")"
          + "    group by bmaa001";

  private static final String INIT_SQL =
      "SELECT  bmaa001 主件编号 "
          + "  FROM  DSDATA.bmaa_t          "
          + "  LEFT JOIN DSDATA.bmba_t ON bmbaent = bmaaent AND bmbasite = bmaasite AND bmba001 =bmaa001"
          + "  LEFT JOIN DSDATA.imaa_t imaa_t1 ON imaa_t1.imaaent = bmbaent AND imaa_t1.imaa001 = bmaa001"
          + "  LEFT JOIN DSDATA.imaal_t imaal_t1 ON imaal_t1.imaalent = bmaaent AND imaal_t1.imaal001 = bmaa001 AND imaal_t1.imaal002 = 'zh_CN'"
          + "  LEFT JOIN DSDATA.rtaxl_t rtaxl_t1 ON rtaxl_t1.rtaxlent = imaa_t1.imaaent AND rtaxl_t1.rtaxl001 = imaa_t1.imaa009 AND rtaxl_t1.rtaxl002 = 'zh_CN'  "
          + "  LEFT JOIN DSDATA.imaf_t ON imafent = bmaaent AND imafsite = bmaasite AND imaf001 = bmaa001"
          + "  LEFT JOIN DSDATA.imaa_t imaa_t2 ON imaa_t2.imaaent = bmbaent AND imaa_t2.imaa001 = bmba003"
          + "  LEFT JOIN DSDATA.imaal_t imaal_t2 ON imaal_t2.imaalent = bmbaent AND imaal_t2.imaal001 = bmba003 AND imaal_t2.imaal002 = 'zh_CN'"
          + "  LEFT JOIN DSDATA.rtaxl_t rtaxl_t2 ON rtaxl_t2.rtaxlent = imaa_t2.imaaent AND rtaxl_t2.rtaxl001 = imaa_t2.imaa009 AND rtaxl_t2.rtaxl002 = 'zh_CN'  "
          + "  LEFT JOIN DSDATA.bmce_t ON bmceent = bmbaent AND bmcesite = bmbasite AND bmce001 = bmba001"
          + "                  AND bmce002 = bmba002 AND bmce003 = bmba003 AND bmce004 = bmba004"
          + "                   AND bmce007 = bmba007 AND bmce008 = bmba008 AND bmce005 = bmba005"
          + "  LEFT JOIN DSDATA.imaguc_t ON imagucent = bmaaent AND imaguc001 = bmaa001 AND imagucstus = 'Y'"
          + "  LEFT JOIN DSDATA.pmaal_t ON pmaalent = imagucent AND pmaal001 = imaguc007 AND pmaal002 = 'zh_CN'"
          + "  LEFT JOIN DSDATA.bmfa_t ON bmfaent = bmbaent AND bmfasite = bmbasite AND bmfadocno = bmba026"
          + " WHERE bmaaent = 100 AND bmaasite = 'PHXHF' AND bmaastus = 'Y'"
          + "   AND imaa_t1.imaa009 LIKE '1%' "
          + "   AND bmba006 IS NULL"
          + "   AND imafstus = 'Y' AND imaf016 = '4'"
          + "   AND bmba019 <> 2 "
          + "    group by bmaa001"
          + " OFFSET ? ROWS FETCH NEXT %d ROWS ONLY".formatted(EACH_QTY);

  private static final String INIT_SQL_COUNT =
      "SELECT count(1) counts "
          + "  FROM  DSDATA.bmaa_t          "
          + "  LEFT JOIN DSDATA.bmba_t ON bmbaent = bmaaent AND bmbasite = bmaasite AND bmba001 =bmaa001"
          + "  LEFT JOIN DSDATA.imaa_t imaa_t1 ON imaa_t1.imaaent = bmbaent AND imaa_t1.imaa001 = bmaa001"
          + "  LEFT JOIN DSDATA.imaal_t imaal_t1 ON imaal_t1.imaalent = bmaaent AND imaal_t1.imaal001 = bmaa001 AND imaal_t1.imaal002 = 'zh_CN'"
          + "  LEFT JOIN DSDATA.rtaxl_t rtaxl_t1 ON rtaxl_t1.rtaxlent = imaa_t1.imaaent AND rtaxl_t1.rtaxl001 = imaa_t1.imaa009 AND rtaxl_t1.rtaxl002 = 'zh_CN'  "
          + "  LEFT JOIN DSDATA.imaf_t ON imafent = bmaaent AND imafsite = bmaasite AND imaf001 = bmaa001"
          + "  LEFT JOIN DSDATA.imaa_t imaa_t2 ON imaa_t2.imaaent = bmbaent AND imaa_t2.imaa001 = bmba003"
          + "  LEFT JOIN DSDATA.imaal_t imaal_t2 ON imaal_t2.imaalent = bmbaent AND imaal_t2.imaal001 = bmba003 AND imaal_t2.imaal002 = 'zh_CN'"
          + "  LEFT JOIN DSDATA.rtaxl_t rtaxl_t2 ON rtaxl_t2.rtaxlent = imaa_t2.imaaent AND rtaxl_t2.rtaxl001 = imaa_t2.imaa009 AND rtaxl_t2.rtaxl002 = 'zh_CN'  "
          + "  LEFT JOIN DSDATA.bmce_t ON bmceent = bmbaent AND bmcesite = bmbasite AND bmce001 = bmba001"
          + "                  AND bmce002 = bmba002 AND bmce003 = bmba003 AND bmce004 = bmba004"
          + "                   AND bmce007 = bmba007 AND bmce008 = bmba008 AND bmce005 = bmba005"
          + "  LEFT JOIN DSDATA.imaguc_t ON imagucent = bmaaent AND imaguc001 = bmaa001 AND imagucstus = 'Y'"
          + "  LEFT JOIN DSDATA.pmaal_t ON pmaalent = imagucent AND pmaal001 = imaguc007 AND pmaal002 = 'zh_CN'"
          + "  LEFT JOIN DSDATA.bmfa_t ON bmfaent = bmbaent AND bmfasite = bmbasite AND bmfadocno = bmba026"
          + " WHERE bmaaent = 100 AND bmaasite = 'PHXHF' AND bmaastus = 'Y'"
          + "   AND imaa_t1.imaa009 LIKE '1%' "
          + "   AND bmba006 IS NULL"
          + "   AND imafstus = 'Y' AND imaf016 = '4'"
          + "   AND bmba019 <> 2 "
          + "    group by bmaa001";

  // 测试用
  public static void main(String[] args) throws SQLException {
    String now = BaseUtils.getCurrentTimeStr();
    Map<String, String> lastrun = Maps.newHashMap();
    lastrun.put("lastrun", "2020-09-24 14:26:40");
    List<Entity> routes = Db.use().query(SQL, "1202222828");
    System.out.println(JsonUtils.toJsonString(routes));
  }
}
