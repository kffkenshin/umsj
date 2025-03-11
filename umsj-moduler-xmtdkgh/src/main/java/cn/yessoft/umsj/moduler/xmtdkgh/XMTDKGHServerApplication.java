package cn.yessoft.umsj.moduler.xmtdkgh;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/** 项目的启动类 */
@SpringBootApplication
@ComponentScan("cn.yessoft.umsj")
@MapperScan("cn.yessoft.umsj.moduler")
@EnableTransactionManagement
public class XMTDKGHServerApplication {
  public static void main(String[] args) {
    SpringApplication.run(XMTDKGHServerApplication.class, args);
  }
}
