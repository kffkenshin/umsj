package cn.yessoft.umsj.moduler.xinhefa.entity;

import cn.yessoft.umsj.mybatis.core.entity.BaseDO;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

/**
 * 产品类型表
 *
 * @author ethan
 * @since 2024-09-19
 */
@Getter
@Setter
@TableName("xhf_item")
public class XhfItemDO extends BaseDO {

  private static final long serialVersionUID = 1L;

  /** ID */
  @TableId(value = "id", type = IdType.AUTO)
  private Long id;

  /** 厂内料号 */
  private String itemNo;

  /** 名称 */
  private String itemName;

  /** 产品类型 */
  private String itemType1;

  /** 产品类型2 */
  private String itemType2;

  /** 物料规格 */
  private String itemSpec;

  /** 销售单位 */
  private String saleUnit;

  /** 生命周期 */
  private String life;

  /** 产品生产品名 */
  private String productName;

  /** 克重 */
  private BigDecimal gWeight;

  /** 分类名称 */
  private String categoryName;

  /** 制袋区分 */
  private String zdDiff;

  /** 卷长KM */
  private BigDecimal rollLengthKm;

  /** 厚度 */
  private Short tickness;

  /** 喷码 */
  private String penMa;

  /** 复合套筒长度 */
  private String fhTaoTongLength;

  /** 密度 */
  private BigDecimal density;

  /** 层数 */
  private String layersCount;

  /** 展开宽度 */
  private Short width;

  /** 材料名称 */
  private String materialName;

  /** 材质 */
  private String texture;

  /** 柔印套筒 */
  private BigDecimal ryTaoTongLength;

  /** 油墨体系 */
  private String ymtx;

  /** 烫金 */
  private String tj;

  /** 版周 */
  private BigDecimal rollerRound;

  /** 版长 */
  private Short rollerLength;

  /** 色数 */
  private Short colorCount;

  /** 色系 */
  private String colorClass;

  /** 通道数 */
  private Short channelsCount;

  /** 长度 */
  private Short length;

  /** 工艺路线备注 */
  private String processRemark;

  private String firstMachine;

  private String secondMachine;

  private String thirdMachine;
}
