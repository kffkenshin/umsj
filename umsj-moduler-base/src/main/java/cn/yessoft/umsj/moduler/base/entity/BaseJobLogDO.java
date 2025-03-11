package cn.yessoft.umsj.moduler.base.entity;

import cn.yessoft.umsj.mybatis.core.entity.BaseDO;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import lombok.*;

/** 定时任务的执行日志 */
@TableName("base_job_log")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BaseJobLogDO extends BaseDO {

  /** 日志编号 */
  @TableId(type = IdType.AUTO)
  private Long id;

  /**
   * 任务编号
   *
   * <p>
   */
  private Long jobId;

  /**
   * 处理器的名字
   *
   * <p>
   */
  private String handlerName;

  /**
   * 处理器的参数
   *
   * <p>
   */
  private String handlerParam;

  /**
   * 第几次执行
   *
   * <p>用于区分是不是重试执行。如果是重试执行，则 index 大于 1
   */
  private Integer executeIndex;

  /** 开始执行时间 */
  private LocalDateTime beginTime;

  /** 结束执行时间 */
  private LocalDateTime endTime;

  /** 执行时长，单位：毫秒 */
  private Integer duration;

  /**
   * 状态
   *
   * <p>
   */
  private Integer status;

  /**
   * 结果数据
   *
   * <p>
   */
  private String result;
}
