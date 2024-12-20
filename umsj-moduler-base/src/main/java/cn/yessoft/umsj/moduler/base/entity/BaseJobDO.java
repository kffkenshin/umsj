package cn.yessoft.umsj.moduler.base.entity;

import cn.yessoft.umsj.mybatis.core.entity.BaseDO;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * 定时任务 DO
 */
@TableName("base_job")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BaseJobDO extends BaseDO {

    /**
     * 任务编号
     */
    @TableId
    private Long id;
    /**
     * 任务名称
     */
    private String name;
    /**
     * 任务状态
     */
    private Integer status;
    /**
     * 处理器的名字
     */
    private String handlerName;
    /**
     * 处理器的参数
     */
    private String handlerParam;
    /**
     * CRON 表达式
     */
    private String cronExpression;

    // ========== 重试相关字段 ==========
    /**
     * 重试次数
     * 如果不重试，则设置为 0
     */
    private Integer retryCount;
    /**
     * 重试间隔，单位：毫秒
     * 如果没有间隔，则设置为 0
     */
    private Integer retryInterval;

    // ========== 监控相关字段 ==========
    /**
     * 监控超时时间，单位：毫秒
     * 为空时，表示不监控
     * <p>
     * 注意，这里的超时的目的，不是进行任务的取消，而是告警任务的执行时间过长
     */
    private Integer monitorTimeout;

}
