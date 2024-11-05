package cn.yessoft.umsj.moduler.xinhefa.quartz;

import cn.yessoft.umsj.moduler.xinhefa.entity.*;
import cn.yessoft.umsj.moduler.xinhefa.service.*;
import cn.yessoft.umsj.quartz.core.handler.JobHandler;
import jakarta.annotation.Resource;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.quartz.DisallowConcurrentExecution;
import org.springframework.stereotype.Component;

@Component
@DisallowConcurrentExecution
@Slf4j
public class SuperScheduler implements JobHandler {

  @Resource private IXhfManufactureOrderHeaderService xhfMoHeaderService;

  @Resource private IXhfManufactureOrderDetailService xhfMoDetailService;

  private Map<String, Long> proessesMap;

  @Override
  public String execute(String param) throws Exception {
    StringBuilder resultMsg = new StringBuilder();
    // 选机、发送T100工单
    String initResult = xhfMoHeaderService.initMo();
    // 排产
    String schResult = xhfMoDetailService.executeScheduler();
    return resultMsg.append(initResult).append(schResult).toString();
  }
}
