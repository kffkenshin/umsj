package cn.yessoft.umsj.moduler.xinhefa.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/** APS工作站 */
@Getter
@AllArgsConstructor
public enum XHFSyncJobDictConfig {
  ITEM_INFO(100, "ITEM_INFO"), // 同步物料信息
  CUSTOMER_ITEM_INFO(100, "CUSTOMER_ITEM_INFO"), // 同步客户物料信息
  CUSTOMER_INFO(100, "CUSTOMER_INFO"), // 同步客户信息
  PROCESS_ROUTE(100, "PROCESS_ROUTE"), // 同步工艺路线
  PRODUCT_BOM(100, "PRODUCT_BOM"), // 同步BOM
  SALE_ORDER(100, "SALE_ORDER"), // 同步销售订单
  ;

  private final Integer dictId;

  private final String detailKey;
}
