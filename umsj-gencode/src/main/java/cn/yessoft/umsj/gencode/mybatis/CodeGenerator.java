package cn.yessoft.umsj.gencode.mybatis;

import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;

public class CodeGenerator {
    /**
     * 数据源配置
     */
    private static final DataSourceConfig DATA_SOURCE_CONFIG = new DataSourceConfig
            .Builder("jdbc:mysql://localhost:3306/umsj?serverTimezone=Asia/Shanghai", "root", "root")
            .schema("umsj")
            .build();

    /**
     * 策略配置
     */
    protected static StrategyConfig.Builder strategyConfig() {
        return new StrategyConfig.Builder();
    }

    /**
     * 全局配置
     */
    protected static GlobalConfig.Builder globalConfig() {
        return new GlobalConfig.Builder();
    }

    /**
     * 包配置
     */
    protected static PackageConfig.Builder packageConfig() {
        return new PackageConfig.Builder();
    }

    public static void main(String[] args) {
        String[] ignoreColumns = {"creator", "create_time", "update_time", "updater"};
        AutoGenerator generator = new AutoGenerator(DATA_SOURCE_CONFIG);
        generator.strategy(strategyConfig().addInclude("xhf_product_bom")
                .controllerBuilder().enableRestStyle()
                .entityBuilder().superClass("cn.yessoft.umsj.mybatis.core.entity.BaseDO").enableLombok().addIgnoreColumns(ignoreColumns).formatFileName("%sDO")
                .mapperBuilder().superClass("cn.yessoft.umsj.mybatis.core.mapper.YesBaseMapper")
                .build());
        generator.global(globalConfig().author("ethan").outputDir("D:/gencode").build());
        generator.packageInfo(packageConfig().parent("cn.yessoft.umsj.moduler.xinhefa").build());
        generator.execute();
    }
}
