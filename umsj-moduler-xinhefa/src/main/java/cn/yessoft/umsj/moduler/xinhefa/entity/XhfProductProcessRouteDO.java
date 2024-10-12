package cn.yessoft.umsj.moduler.xinhefa.entity;

import cn.yessoft.umsj.mybatis.core.entity.BaseDO;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

/**
 * 工艺路线表
 *
 * @author ethan
 * @since 2024-09-21
 */
@Getter
@Setter
@TableName("xhf_product_process_route")
public class XhfProductProcessRouteDO extends BaseDO {

  private static final long serialVersionUID = 1L;

  /** ID */
  @TableId(value = "id", type = IdType.AUTO)
  private Long id;

  /** 物料ID */
  private Long itemId;

  /** 顺序 */
  private Byte seq;

  /** 工序ID */
  private Long processId;

  /** 是否报工 */
  private String needReport;
}
