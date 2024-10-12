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
import cn.yessoft.umsj.moduler.xinhefa.entity.XhfProductProcessDO;
import cn.yessoft.umsj.moduler.xinhefa.entity.XhfProductProcessRouteDO;
import cn.yessoft.umsj.moduler.xinhefa.enums.XHFSyncJobDictConfig;
import cn.yessoft.umsj.moduler.xinhefa.service.IXhfItemService;
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
public class SyncProcessRoute implements JobHandler {

  @Resource private IBaseDictDetailService baseDictDetailService;

  @Resource private IXhfItemService xhfItemService;

  @Resource private IXhfProductProcessService xhfProductProcessService;

  @Resource private IXhfProductProcessRouteService xhfProductProcessRouteService;

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
            XHFSyncJobDictConfig.PROCESS_ROUTE.getDictId(),
            XHFSyncJobDictConfig.PROCESS_ROUTE.getDetailKey());
    if (lastRunTime == null) {
      throw exception(O_NOT_EXISTS, "工艺路线同步定时配置");
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
    List<XhfProductProcessRouteDO> results = Lists.newArrayList();
    int count = 1;
    if (BaseUtils.isNotEmpty(result)) {
      for (Entity i : result) {
        log.info("===" + count++ + "////" + result.size());
        String itemNo = i.getStr("料件编号");
        XhfItemDO pitem = xhfItemService.getItemByNo(itemNo);
        if (pitem == null) {
          log.error("找不到料号" + itemNo);
          continue;
        }

        // 先删除旧的
        xhfProductProcessRouteService.deleteByItemId(pitem.getId());
        List<XhfProductProcessRouteDO> insert = create(pitem);
        if (!insert.isEmpty()) results.addAll(insert);
      }
      if (!result.isEmpty()) xhfProductProcessRouteService.insertBatch(results);
    }
  }

  private List<XhfProductProcessRouteDO> create(XhfItemDO item) throws SQLException {
    List<XhfProductProcessRouteDO> results = Lists.newArrayList();
    List<Entity> routes = Db.use().query(SQL, item.getItemNo());
    if (BaseUtils.isEmpty(routes)) {
      return results;
    }
    // 更新工艺路线说明 、产品类型2
    item.setProcessRemark(routes.get(0).getStr("料件工艺说明"));
    item.setItemType2(getItemType2(item.getItemType1(), routes));
    xhfItemService.updateById(item);
    routes.forEach(
        route -> {
          XhfProductProcessRouteDO routeDO = new XhfProductProcessRouteDO();
          routeDO.setItemId(item.getId());
          routeDO.setSeq(route.getByte("项次"));
          routeDO.setProcessId(proessesMap.get(route.getStr("作业编号")));
          routeDO.setNeedReport(route.getStr("报工站"));
          results.add(routeDO);
        });
    return results;
  }

  private String getItemType2(String itemType1, List<Entity> routes) {
    if ("小底封卷料".equals(itemType1) || "点断卷料".equals(itemType1) || "小底封袋".equals(itemType1)) {
      if (routes.stream().anyMatch(i -> (i.getStr("工艺编号").equals("TFTB")))) {
        return switch (itemType1) {
          case "小底封卷料" -> "贴标小连卷";
          case "点断卷料" -> "贴标点断";
          case "小底封袋" -> "贴标小底封";
          default -> itemType1;
        };
      }
    }
    return itemType1;
  }

  // todo  dsdata 需要改成可以配置的 用formatter{0}处理,目前为了main好测试 先不做
  private static final String SQL =
      "SELECT ecbaent 企业编号,ecbastus 单据状态,ecba001 料件编号,imaal003 品名,imaal004 规格,"
          + "ecba002 工艺编号,ecba003 料件工艺说明,imaa128 产线编号,oocql004 产线说明,imaa006 基础单位,imaa009 产品分类编码,rtaxl003 产品分类名称,"
          + "ecbb003 项次,ecbb004 作业编号,ecbb005 作业序,ecbb008 上站作业,ecbb009 上站作业序,"
          + "ecbb010 下站作业编号,ecbb011 下站作业序,ecbb017 报工站,ecbamoddt 最近修改日期"
          + " FROM DSDATA.ecba_t"
          + " LEFT JOIN DSDATA.ecbb_t ON ecbbent = ecbaent AND ecbbsite = ecbasite AND ecbb001 = ecba001 AND ecbb002 = ecba002"
          + " LEFT JOIN DSDATA.imaa_t ON imaaent = ecbaent AND imaa001 = ecba001"
          + " LEFT JOIN DSDATA.imaal_t ON imaalent = ecbaent AND imaal001 = ecba001 AND imaal002 = 'zh_CN'"
          + " LEFT JOIN DSDATA.rtaxl_t ON rtaxlent = imaaent AND rtaxl001 = imaa009 AND rtaxl002 = 'zh_CN'"
          + " LEFT JOIN DSDATA.oocql_t ON oocqlent = imaaent AND oocql001 = '2004' AND oocql002 = imaa128 AND oocql003 = 'zh_CN'"
          + " WHERE ecbaent  = 100"
          + " AND ecbasite = 'PHXHF'"
          + " AND ecbastus = 'Y'"
          + " AND ecba002 = '1'"
          + " and ecba001 = ?";

  private static final String GETNO_SQL =
      "SELECT ecba001 料件编号,ecba003 料件工艺说明"
          + " FROM DSDATA.ecba_t"
          + " LEFT JOIN DSDATA.ecbb_t ON ecbbent = ecbaent AND ecbbsite = ecbasite AND ecbb001 = ecba001 AND ecbb002 = ecba002"
          + " LEFT JOIN DSDATA.imaa_t ON imaaent = ecbaent AND imaa001 = ecba001"
          + " LEFT JOIN DSDATA.imaal_t ON imaalent = ecbaent AND imaal001 = ecba001 AND imaal002 = 'zh_CN'"
          + " LEFT JOIN DSDATA.rtaxl_t ON rtaxlent = imaaent AND rtaxl001 = imaa009 AND rtaxl002 = 'zh_CN'"
          + " LEFT JOIN DSDATA.oocql_t ON oocqlent = imaaent AND oocql001 = '2004' AND oocql002 = imaa128 AND oocql003 = 'zh_CN'"
          + " WHERE ecbaent  = 100"
          + " AND ecbasite = 'PHXHF'"
          + " AND ecbastus = 'Y'"
          + " AND ecba002 = '1'"
          + " and( ecbamoddt >= TO_DATE(:lastrun, 'YYYY-MM-DD HH24:MI:SS')"
          + "      OR  ECBACRTDT >= TO_DATE(:lastrun, 'YYYY-MM-DD HH24:MI:SS')    "
          + "      OR  IMAAMODDT >= TO_DATE(:lastrun, 'YYYY-MM-DD HH24:MI:SS')    "
          + "      OR  IMAACRTDT >= TO_DATE(:lastrun, 'YYYY-MM-DD HH24:MI:SS')    "
          + ")"
          + "    group by ecba001,ecba003";

  private static final String INIT_SQL =
      "SELECT ecba001 料件编号"
          + " FROM DSDATA.ecba_t"
          + " LEFT JOIN DSDATA.ecbb_t ON ecbbent = ecbaent AND ecbbsite = ecbasite AND ecbb001 = ecba001 AND ecbb002 = ecba002"
          + " LEFT JOIN DSDATA.imaa_t ON imaaent = ecbaent AND imaa001 = ecba001"
          + " LEFT JOIN DSDATA.imaal_t ON imaalent = ecbaent AND imaal001 = ecba001 AND imaal002 = 'zh_CN'"
          + " LEFT JOIN DSDATA.rtaxl_t ON rtaxlent = imaaent AND rtaxl001 = imaa009 AND rtaxl002 = 'zh_CN'"
          + " LEFT JOIN DSDATA.oocql_t ON oocqlent = imaaent AND oocql001 = '2004' AND oocql002 = imaa128 AND oocql003 = 'zh_CN'"
          + " WHERE ecbaent  = 100"
          + " AND ecbasite = 'PHXHF'"
          + " AND ecbastus = 'Y'"
          + " AND ecba002 = '1' "
          + " group by ecba001"
          + " OFFSET ? ROWS FETCH NEXT %d ROWS ONLY".formatted(EACH_QTY);

  private static final String INIT_SQL_COUNT =
      "SELECT count(*) as counts"
          + " FROM DSDATA.ecba_t"
          + " LEFT JOIN DSDATA.ecbb_t ON ecbbent = ecbaent AND ecbbsite = ecbasite AND ecbb001 = ecba001 AND ecbb002 = ecba002"
          + " LEFT JOIN DSDATA.imaa_t ON imaaent = ecbaent AND imaa001 = ecba001"
          + " LEFT JOIN DSDATA.imaal_t ON imaalent = ecbaent AND imaal001 = ecba001 AND imaal002 = 'zh_CN'"
          + " LEFT JOIN DSDATA.rtaxl_t ON rtaxlent = imaaent AND rtaxl001 = imaa009 AND rtaxl002 = 'zh_CN'"
          + " LEFT JOIN DSDATA.oocql_t ON oocqlent = imaaent AND oocql001 = '2004' AND oocql002 = imaa128 AND oocql003 = 'zh_CN'"
          + " WHERE ecbaent  = 100"
          + " AND ecbasite = 'PHXHF'"
          + " AND ecbastus = 'Y'"
          + " AND ecba002 = '1'"
          + " group by ecba001";

  // 测试用
  public static void main(String[] args) throws SQLException {
    String now = BaseUtils.getCurrentTimeStr();
    Map<String, String> lastrun = Maps.newHashMap();
    lastrun.put("lastrun", "2020-09-24 14:26:40");
    List<Entity> routes = Db.use().query(SQL, "1202222828");
    System.out.println(JsonUtils.toJsonString(routes));
  }
}
