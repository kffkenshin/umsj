package cn.yessoft.umsj.moduler.xinhefa.enums;

import cn.yessoft.umsj.common.exception.ErrorCode;

/**
 * System 错误码枚举类
 *
 * <p>system APS系统，使用 2-000-000-000 段
 */
public interface XHFErrorCodeConstants {
  // 数据同步 2_001_000_000
  ErrorCode SO_ITEM_NOT_MATCH = new ErrorCode(2_001_000_001, "物料和原订单项次{}不符");
  // 生产
  ErrorCode SO_IS_EMPTY = new ErrorCode(2_001_001_001, "没有选择发货批次");
  ErrorCode MO_MACHINE_IS_EMPTY = new ErrorCode(2_001_002_001, "{}选机为空");
  ErrorCode NO_MACHINE_SPEED = new ErrorCode(2_001_002_002, "机速为空");
}
