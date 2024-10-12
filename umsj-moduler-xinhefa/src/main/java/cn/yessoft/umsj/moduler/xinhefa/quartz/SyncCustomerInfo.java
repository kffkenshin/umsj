package cn.yessoft.umsj.moduler.xinhefa.quartz;

import static cn.yessoft.umsj.common.exception.util.ServiceExceptionUtil.exception;
import static cn.yessoft.umsj.moduler.base.enums.ErrorCodeConstants.O_NOT_EXISTS;

import cn.hutool.db.Db;
import cn.hutool.db.Entity;
import cn.yessoft.umsj.common.utils.BaseUtils;
import cn.yessoft.umsj.common.utils.json.JsonUtils;
import cn.yessoft.umsj.moduler.base.entity.BaseDictDetailDO;
import cn.yessoft.umsj.moduler.base.service.IBaseDictDetailService;
import cn.yessoft.umsj.moduler.xinhefa.entity.XhfCustomerDO;
import cn.yessoft.umsj.moduler.xinhefa.enums.XHFSyncJobDictConfig;
import cn.yessoft.umsj.moduler.xinhefa.service.IXhfCustomerService;
import cn.yessoft.umsj.quartz.core.handler.JobHandler;
import com.google.common.collect.Maps;
import jakarta.annotation.Resource;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import org.apache.commons.compress.utils.Lists;
import org.quartz.DisallowConcurrentExecution;
import org.springframework.stereotype.Component;

@Component
@DisallowConcurrentExecution
public class SyncCustomerInfo implements JobHandler {

  @Resource private IXhfCustomerService xhfCustomerService;

  @Resource private IBaseDictDetailService baseDictDetailService;

  @Override
  public String execute(String param) throws Exception {
    StringBuilder errMsg = new StringBuilder();
    BaseDictDetailDO lastRunTime =
        baseDictDetailService.getByDictIdAndDetailKey(
            XHFSyncJobDictConfig.CUSTOMER_INFO.getDictId(),
            XHFSyncJobDictConfig.CUSTOMER_INFO.getDetailKey());
    if (lastRunTime == null) {
      throw exception(O_NOT_EXISTS, "客户同步定时配置");
    }
    String now = BaseUtils.getCurrentTimeStr();
    // 上次执行的时间
    Map<String, String> lastRun = Maps.newHashMap();
    lastRun.put("lastRun", lastRunTime.getDetailValue());
    List<Entity> result = Db.use().query(SQL, lastRun);
    errMsg.append(handleResult(result));
    // 记录本次时间
    if (!result.isEmpty()) {
      lastRunTime.setDetailValue(now);
      baseDictDetailService.updateById(lastRunTime);
    }
    return (errMsg.isEmpty())
        ? ""
        : ("\n ======错误信息:====== \n" + errMsg) + JsonUtils.toJsonString(result);
  }

  private String handleResult(List<Entity> result) {
    List<XhfCustomerDO> needInsert = Lists.newArrayList();
    if (BaseUtils.isNotEmpty(result)) {
      for (Entity i : result) {
        XhfCustomerDO r = xhfCustomerService.getByNo(i.getStr("客户编号"));
        if (r != null) {
          String em = fillData(r, i);
          xhfCustomerService.updateById(r);
        } else {
          r = new XhfCustomerDO();
          String em = fillData(r, i);
          xhfCustomerService.save(r);
        }
      }
    }
    return "";
  }

  private String fillData(XhfCustomerDO item, Entity i) {
    item.setCusGroup(i.getStr("客户分类名称"));
    item.setNameShort(i.getStr("客户简称"));
    item.setNo(i.getStr("客户编号"));
    item.setNameCn(i.getStr("客户名称"));
    return "";
  }

  private static String SQL =
      "SELECT pmaaent 企业编号,pmabsite 营运据点,pmaa001 客户编号,pmaastus 资料状态,pmaal003 客户名称,pmaal004 客户简称,"
          + "       pmaa090 客户分类编码,oocql004 客户分类名称,pmaa093 集团资料生命周期,pmab114 据点资料生命周期,"
          + "       pmaamoddt 最后修改日期"
          + " FROM dsdata.pmaa_t"
          + "  LEFT JOIN dsdata.pmab_t ON pmabent = pmaaent AND pmab001 = pmaa001"
          + "  LEFT JOIN dsdata.pmaal_t ON pmaalent = pmaaent AND pmaal001 = pmaa001 AND pmaal002 = 'zh_CN'"
          + "  LEFT JOIN dsdata.oocql_t ON oocqlent = pmaaent AND oocql001 = '281' AND oocql002 = pmaa090 AND oocql003 = 'zh_CN'"
          + " WHERE pmaaent = 100"
          + "  AND pmabsite = 'PHXHF'"
          + "  AND pmaa002 IN (2,3)"
          + "  AND pmaastus = 'Y'"
          + "  AND( pmaamoddt >= TO_DATE(:lastRun, 'YYYY-MM-DD HH24:MI:SS')"
          + "    OR PMAACRTDT >= TO_DATE(:lastRun, 'YYYY-MM-DD HH24:MI:SS')"
          + "    OR pmabmoddt >= TO_DATE(:lastRun, 'YYYY-MM-DD HH24:MI:SS')"
          + "    OR pmabcrtdt >= TO_DATE(:lastRun, 'YYYY-MM-DD HH24:MI:SS')"
          + "  )";

  public static void main(String[] args) throws SQLException {
    List<Entity> result = Db.use().query(SQL);
    System.out.println(JsonUtils.toJsonString(result));
  }
}
