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
import cn.yessoft.umsj.moduler.xinhefa.enums.XHFSyncJobDictConfig;
import cn.yessoft.umsj.moduler.xinhefa.service.IXhfItemService;
import cn.yessoft.umsj.moduler.xinhefa.service.IXhfProductProcessRouteService;
import cn.yessoft.umsj.quartz.core.handler.JobHandler;
import com.google.common.collect.Maps;
import jakarta.annotation.Resource;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.Lists;
import org.quartz.DisallowConcurrentExecution;
import org.springframework.stereotype.Component;

@Component
@DisallowConcurrentExecution
@Slf4j
public class SyncItemInfo implements JobHandler {

  @Resource private IBaseDictDetailService baseDictDetailService;

  @Resource private IXhfItemService xhfItemService;

  @Resource private IXhfProductProcessRouteService xhfProductProcessRouteService;

  @Override
  public String execute(String param) throws Exception {
    BaseDictDetailDO lastRunTime =
        baseDictDetailService.getByDictIdAndDetailKey(
            XHFSyncJobDictConfig.ITEM_INFO.getDictId(),
            XHFSyncJobDictConfig.ITEM_INFO.getDetailKey());
    if (lastRunTime == null) {
      throw exception(O_NOT_EXISTS, "物料同步定时配置");
    }
    String now = BaseUtils.getCurrentTimeStr();
    // 上次执行的时间
    Map<String, String> lastrun = Maps.newHashMap();
    lastrun.put("lastrun", lastRunTime.getDetailValue());
    List<Entity> result = Lists.newArrayList();
    if ("INIT".equals(lastRunTime.getDetailValue())) {
      List<Entity> count = Db.use().query(INIT_SQL_COUNT);
      Long counts = count.get(0).getLong("counts");
      for (Long e = 50L; e * 2000 <= counts; e++) {
        result = Db.use().query(INIT_SQL, 2000 * e);
        handleResult(result);
      }
    } else if ("INIT_TYPE2".equals(lastRunTime.getDetailValue())) { // 初始化产品类型2
      updateType2();
    } else if ("INIT_CHANNEL".equals(lastRunTime.getDetailValue())) { // 初始化通道数
      updateChannel();
    } else {
      result = Db.use().query(SQL, lastrun);
      handleResult(result);
    }
    // 记录本次时间
    if (!result.isEmpty()) {
      lastRunTime.setDetailValue(now);
      baseDictDetailService.updateById(lastRunTime);
    }
    return JsonUtils.toJsonString(result);
  }

  // 初始化通道数
  private void updateChannel() {
    List<XhfItemDO> items = xhfItemService.getEmptyChannelItems();
    int count = 0;
    for (XhfItemDO item : items) {
      Short channelsCount = getChannelsCount(item);
      item.setChannelsCount(channelsCount);
      xhfItemService.updateById(item);
      log.info(count++ + "//" + items.size());
    }
  }

  private Short getChannelsCount(XhfItemDO item) {
    try {
      List<Entity> result = Db.use().query(SyncProcuctBom.SQL, item.getItemNo());
      if (!result.isEmpty()) {
        return result.get(0).getShort("通道数");
      }
    } catch (SQLException e) {
    }
    return null;
  }

  // 初始化产品类型2
  private void updateType2() {
    List<XhfItemDO> items = xhfItemService.getEmptyType2Items();
    int count = 0;
    for (XhfItemDO item : items) {
      String itemType2 = getItemType2(item);
      item.setItemType2(itemType2);
      xhfItemService.updateById(item);
      log.info(count++ + "//" + items.size());
    }
  }

  private String getItemType2(XhfItemDO item) {
    if ("小底封卷料".equals(item.getItemType1())
        || "点断卷料".equals(item.getItemType1())
        || "小底封袋".equals(item.getItemType1())) {
      if (xhfProductProcessRouteService.getByItemIdAndProcessNo(item.getId(), "TFTB") != null) {
        return switch (item.getItemType1()) {
          case "小底封卷料" -> "贴标小连卷";
          case "点断卷料" -> "贴标点断";
          case "小底封袋" -> "贴标小底封";
          default -> item.getItemType1();
        };
      }
    }
    return item.getItemType1();
  }

  private void handleResult(List<Entity> result) {
    List<XhfItemDO> results = Lists.newArrayList();
    if (BaseUtils.isNotEmpty(result)) {
      result.forEach(
          i -> {
            XhfItemDO r = xhfItemService.getItemByNo(i.getStr("料件编号"));
            if (r != null) {
              fillData(r, i);
            } else {
              r = new XhfItemDO();
              fillData(r, i);
            }
            results.add(r);
          });
      xhfItemService.insertOrUpdateBatch(results);
    }
  }

  private void fillData(XhfItemDO item, Entity i) {
    item.setItemNo(i.getStr("料件编号"));
    item.setItemName(i.getStr("品名"));
    item.setItemSpec(i.getStr("规格"));
    item.setSaleUnit(i.getStr("基础单位"));
    item.setLife(i.getStr("生命周期"));
    item.setCategoryName(i.getStr("分类名称"));
    item.setTickness(i.getShort("厚度"));
    item.setLength(i.getShort("长度"));
    item.setWidth(i.getShort("展开宽度"));
    item.setDensity(i.getBigDecimal("密度"));
    item.setColorCount(BaseUtils.isEmpty(i.getShort("色数")) ? i.getShort("集团色数") : i.getShort("色数"));
    item.setGWeight(i.getBigDecimal("克重"));
    item.setItemType1(i.getStr("产品类型"));
    item.setFhTaoTongLength(i.getStr("复合套筒"));
    item.setRyTaoTongLength(i.getBigDecimal("柔印套筒"));
    item.setRollerRound(i.getBigDecimal("版周"));
    item.setRollerLength(i.getShort("版长"));
    item.setCategoryName(i.getStr("色系"));
    if (item.getItemNo().startsWith("2")) {
      item.setItemType2("外销薄膜");
    }
  }

  // todo  dsdata 需要改成可以配置的 用formatter{0}处理,目前为了main好测试 先不做
  private static final String SQL =
      "SELECT imafent 企业编号,imafsite 营运据点,imaa001 料件编号,imaal003 品名,imaal004 规格,"
          + "       imaa006 基础单位,imaf016 生命周期,imaa009 分类编码,rtaxl003 分类名称,imaa019 长度,imaa020 展开宽度,imaa021 厚度,imaaud013 密度,"
          + "       ROUND(imaa019/1000 * imaa020/1000 * imaa021/1000 * imaaud013 * 1000,6) 克重,imaa017 集团色数，"
          + "       imaa127 色系编号,(SELECT oocql004 FROM dsdata.oocql_t WHERE oocqlent = imafent AND oocql001 = 2003 AND oocql002 = imaa127 AND oocql003 = 'zh_CN') 色系,"
          + "       imaa128 产品类型编码,(SELECT oocql004 FROM dsdata.oocql_t WHERE oocqlent = imafent AND oocql001 = 2004 AND oocql002 = imaa128 AND oocql003 = 'zh_CN') 产品类型,"
          + "       (SELECT imaiuc010 FROM DSDATA.imaiuc_t WHERE imaiucent = imafent AND imaiucsite = imafsite AND imaiuc001 = imaf001 AND imaiucstus = 'Y' AND ROWNUM = 1) 色数,"
          + "       (SELECT imaiuc015 FROM DSDATA.imaiuc_t WHERE imaiucent = imafent AND imaiucsite = imafsite AND imaiuc001 = imaf001 AND imaiucstus = 'Y' AND ROWNUM = 1) 版长,"
          + "       (SELECT imaiuc016 FROM DSDATA.imaiuc_t WHERE imaiucent = imafent AND imaiucsite = imafsite AND imaiuc001 = imaf001 AND imaiucstus = 'Y' AND ROWNUM = 1) 版周,"
          + "       (SELECT imaiuc017 FROM DSDATA.imaiuc_t WHERE imaiucent = imafent AND imaiucsite = imafsite AND imaiuc001 = imaf001 AND imaiucstus = 'Y' AND ROWNUM = 1) 柔印套筒,"
          + "       (SELECT imaiuc034 FROM DSDATA.imaiuc_t WHERE imaiucent = imafent AND imaiucsite = imafsite AND imaiuc001 = imaf001 AND imaiucstus = 'Y' AND ROWNUM = 1) 据点版辊数据,"
          + "    (SELECT IMAIUCCRTDT FROM DSDATA.imaiuc_t WHERE imaiucent = imafent AND imaiucsite = imafsite AND imaiuc001 = imaf001 AND imaiucstus = 'Y' AND ROWNUM = 1) 版辊数据创建时间,"
          + "    (SELECT IMAIUCMODDT FROM DSDATA.imaiuc_t WHERE imaiucent = imafent AND imaiucsite = imafsite AND imaiuc001 = imaf001 AND imaiucstus = 'Y' AND ROWNUM = 1) 版辊数据修改时间,"
          + "       (SELECT imaguc074 FROM DSDATA.imaguc_t WHERE imagucent = imafent AND imaguc001 = imaf001 AND ROWNUM = 1) 复合套筒,"
          + "    imaf128/100 销售超交率,"
          + "       imafmoddt 最近修改日期,IMAFCRTDT 创建日期"
          + "  FROM DSDATA.imaf_t"
          + "  LEFT JOIN DSDATA.imaa_t ON imaaent =imafent AND imaa001 = imaf001"
          + "  LEFT JOIN DSDATA.imaal_t ON imaalent = imaaent AND imaal001 = imaa001 AND imaal002 = 'zh_CN'"
          + "  LEFT JOIN DSDATA.rtaxl_t ON rtaxlent = imaaent AND rtaxl001 = imaa009 AND rtaxl002 = 'zh_CN'"
          + "  LEFT JOIN DSDATA.oocql_t ON oocqlent = imafent AND oocql001 = '210' AND oocql002 = imaf016 AND oocql003 = 'zh_CN'"
          + " WHERE imafent = 100"
          + "   AND imafsite = 'PHXHF'"
          + " AND imaa001 ='1101011305'"
          + " AND( imafmoddt >= TO_DATE(:lastrun, 'YYYY-MM-DD HH24:MI:SS') "
          + "      OR  IMAFCRTDT >= TO_DATE(:lastrun, 'YYYY-MM-DD HH24:MI:SS')    "
          + "      OR (SELECT IMAIUCCRTDT FROM DSDATA.imaiuc_t WHERE imaiucent = imafent AND imaiucsite = imafsite AND imaiuc001 = imaf001 AND imaiucstus = 'Y' AND ROWNUM = 1) >= TO_DATE(:lastrun, 'YYYY-MM-DD HH24:MI:SS') "
          + "      OR (SELECT IMAIUCMODDT FROM DSDATA.imaiuc_t WHERE imaiucent = imafent AND imaiucsite = imafsite AND imaiuc001 = imaf001 AND imaiucstus = 'Y' AND ROWNUM = 1) >= TO_DATE(:lastrun, 'YYYY-MM-DD HH24:MI:SS') "
          + "      OR IMAACRTDT >= TO_DATE(:lastrun, 'YYYY-MM-DD HH24:MI:SS') "
          + "      OR IMAAMODDT >= TO_DATE(:lastrun, 'YYYY-MM-DD HH24:MI:SS') "
          + "    )";

  private static final String INIT_SQL =
      "SELECT imafent 企业编号,imafsite 营运据点,imaa001 料件编号,imaal003 品名,imaal004 规格,"
          + "       imaa006 基础单位,imaf016 生命周期,imaa009 分类编码,rtaxl003 分类名称,imaa019 长度,imaa020 展开宽度,imaa021 厚度,imaaud013 密度,"
          + "       ROUND(imaa019/1000 * imaa020/1000 * imaa021/1000 * imaaud013 * 1000,6) 克重,imaa017 集团色数，"
          + "       imaa127 色系编号,(SELECT oocql004 FROM dsdata.oocql_t WHERE oocqlent = imafent AND oocql001 = 2003 AND oocql002 = imaa127 AND oocql003 = 'zh_CN') 色系,"
          + "       imaa128 产品类型编码,(SELECT oocql004 FROM dsdata.oocql_t WHERE oocqlent = imafent AND oocql001 = 2004 AND oocql002 = imaa128 AND oocql003 = 'zh_CN') 产品类型,"
          + "       (SELECT imaiuc010 FROM DSDATA.imaiuc_t WHERE imaiucent = imafent AND imaiucsite = imafsite AND imaiuc001 = imaf001 AND imaiucstus = 'Y' AND ROWNUM = 1) 色数,"
          + "       (SELECT imaiuc015 FROM DSDATA.imaiuc_t WHERE imaiucent = imafent AND imaiucsite = imafsite AND imaiuc001 = imaf001 AND imaiucstus = 'Y' AND ROWNUM = 1) 版长,"
          + "       (SELECT imaiuc016 FROM DSDATA.imaiuc_t WHERE imaiucent = imafent AND imaiucsite = imafsite AND imaiuc001 = imaf001 AND imaiucstus = 'Y' AND ROWNUM = 1) 版周,"
          + "       (SELECT imaiuc017 FROM DSDATA.imaiuc_t WHERE imaiucent = imafent AND imaiucsite = imafsite AND imaiuc001 = imaf001 AND imaiucstus = 'Y' AND ROWNUM = 1) 柔印套筒,"
          + "       (SELECT imaiuc034 FROM DSDATA.imaiuc_t WHERE imaiucent = imafent AND imaiucsite = imafsite AND imaiuc001 = imaf001 AND imaiucstus = 'Y' AND ROWNUM = 1) 据点版辊数据,"
          + "    (SELECT IMAIUCCRTDT FROM DSDATA.imaiuc_t WHERE imaiucent = imafent AND imaiucsite = imafsite AND imaiuc001 = imaf001 AND imaiucstus = 'Y' AND ROWNUM = 1) 版辊数据创建时间,"
          + "    (SELECT IMAIUCMODDT FROM DSDATA.imaiuc_t WHERE imaiucent = imafent AND imaiucsite = imafsite AND imaiuc001 = imaf001 AND imaiucstus = 'Y' AND ROWNUM = 1) 版辊数据修改时间,"
          + "       (SELECT imaguc074 FROM DSDATA.imaguc_t WHERE imagucent = imafent AND imaguc001 = imaf001 AND ROWNUM = 1) 复合套筒,"
          + "    imaf128/100 销售超交率,"
          + "       imafmoddt 最近修改日期,IMAFCRTDT 创建日期"
          + "  FROM DSDATA.imaf_t"
          + "  LEFT JOIN DSDATA.imaa_t ON imaaent =imafent AND imaa001 = imaf001"
          + "  LEFT JOIN DSDATA.imaal_t ON imaalent = imaaent AND imaal001 = imaa001 AND imaal002 = 'zh_CN'"
          + "  LEFT JOIN DSDATA.rtaxl_t ON rtaxlent = imaaent AND rtaxl001 = imaa009 AND rtaxl002 = 'zh_CN'"
          + "  LEFT JOIN DSDATA.oocql_t ON oocqlent = imafent AND oocql001 = '210' AND oocql002 = imaf016 AND oocql003 = 'zh_CN'"
          + " WHERE imafent = 100"
          + "   AND imafsite = 'PHXHF'"
          + " OFFSET ? ROWS FETCH NEXT 2000 ROWS ONLY";

  private static final String INIT_SQL_COUNT =
      "SELECT count(1) as counts"
          + "  FROM DSDATA.imaf_t"
          + "  LEFT JOIN DSDATA.imaa_t ON imaaent =imafent AND imaa001 = imaf001"
          + "  LEFT JOIN DSDATA.imaal_t ON imaalent = imaaent AND imaal001 = imaa001 AND imaal002 = 'zh_CN'"
          + "  LEFT JOIN DSDATA.rtaxl_t ON rtaxlent = imaaent AND rtaxl001 = imaa009 AND rtaxl002 = 'zh_CN'"
          + "  LEFT JOIN DSDATA.oocql_t ON oocqlent = imafent AND oocql001 = '210' AND oocql002 = imaf016 AND oocql003 = 'zh_CN'"
          + " WHERE imafent = 100"
          + "   AND imafsite = 'PHXHF' ";

  // 测试用
  public static void main(String[] args) throws SQLException {
    String now = BaseUtils.getCurrentTimeStr();
    String nowwww = "asfs%".formatted("sdfsdf");
    System.out.println(nowwww);
    //    Map<String, String> lastrun = Maps.newHashMap();
    //    lastrun.put("lastrun", "2020-09-24 14:26:40");
    //    List<Entity> result = Db.use().query(SQL, lastrun);
    //    System.out.println(JsonUtils.toJsonString(result));
  }
}
