package cn.yessoft.umsj.moduler.xinhefa.enums;

import cn.hutool.core.util.ArrayUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

/** APS工作站 */
@Getter
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum XHFManufactureRuleEnum {
  K1(
      "客户",
      "ALL",
      "K",
      "客户名称",
      "K1",
      "客户名称",
      "读取客户简称",
      "包含\"客户名称“\n" + "名称包含金佰利时需要同时满足包含\"中国”或”北京“或”南京“或”天津“"),
  C11("产品分类", "ALL", "C1", "贴标区分", "C11", "贴标", "读取工艺路线", "包含“烫封贴标“"),
  C12("产品分类", "ALL", "C2", "贴标区分", "C12", "不贴标", "读取工艺路线", "不包含“烫封贴标“"),
  FQ1("产品分类", "分切卷料", "FQ", "分切品类", "FQ1", "手帕", "读取T100产品分类名称", "包含“手帕”"),
  FQ2("产品分类", "分切卷料", "FQ", "分切品类", "FQ2", "软抽", "读取T100产品分类名称", "包含“软抽”"),
  FQ3("产品分类", "分切卷料", "FQ", "分切品类", "FQ3", "中封", "读取T100产品分类名称", "包含“中封”"),
  FQ4("产品分类", "分切卷料", "FQ", "分切品类", "FQ4", "橡胶", "读取T100产品分类名称", "包含“橡胶”"),
  FQ5("产品分类", "分切卷料", "FQ", "分切品类", "FQ5", "手提条", "读取材料品名", "包含“手提条”"),
  FQ6("产品分类", "分切卷料", "FQ", "分切品类", "FQ6", "加强片", "读取材料品名", "包含“加强片”"),
  FQ7("产品分类", "分切卷料", "FQ", "分切品类", "FQ7", "其他", "分切卷料剔除以上分类", "除以上其他"),
  DDF1("产品分类", "大底封袋", "DDF", "袋子类型", "DDF1", "尿裤尿片类", "读取产品分类名称", "包含“尿裤”"),
  DDF2("产品分类", "大底封袋", "DDF", "袋子类型", "DDF2", "其他", "大底封袋剔除以上分类", "-"),
  GK1("产品分类", "小侧封袋", "GK", "挂孔区分", "GK1", "小挂孔", "产品规格", "产品规格“（）“内第一个“+“前后两个数字之差的绝对值为20"),
  GK2("产品分类", "小侧封袋", "GK", "挂孔区分", "GK2", "大挂孔", "产品规格", "产品规格“（）“内第一个“+“前后两个数字之差的绝对值为40"),
  G11("工艺", "ALL", "G1", "层数区分", "G11", "复合", "读取工艺路线", "包含“无溶剂复合“"),
  G12("工艺", "ALL", "G1", "层数区分", "G12", "单层", "读取工艺路线", "不包含“无溶剂复合“"),
  G21("工艺", "ALL", "G2", "烫金工艺", "G21", "烫金(正面/背面打电晕)", "读取工艺路线（备注）", "包含“烫金（正面/背面打电晕）“"),
  G22("工艺", "ALL", "G2", "烫金工艺", "G22", "烫金", "读取工艺路线（备注）", "包含“烫金”但要剔除“烫金（正面/背面打电晕）“"),
  G23("工艺", "ALL", "G2", "烫金工艺", "G23", "打电晕", "读取工艺路线（备注）", "包含“打电晕”但要剔除“烫金（正面/背面打电晕）“"),
  G24("工艺", "ALL", "G2", "烫金工艺", "G24", "烫金2", "读取工艺路线（备注）", "包含“烫金2”但要剔除“烫金（正面/背面打电晕）“"),
  G31("工艺", "点断卷料", "G3", "打孔工艺", "G31", "打孔", "读取工艺路线（备注）", "包含“打*孔“"),
  G32("工艺", "点断卷料", "G3", "打孔工艺", "G32", "不打孔", "读取工艺路线（备注）", "不包含“打*孔“"),
  G41("工艺", "小侧封袋", "G4", "穿绳工艺", "G41", "穿绳", "读取工艺路线", "包含“手工穿绳“"),
  G42("工艺", "小侧封袋", "G4", "穿绳工艺", "G42", "不穿绳", "读取工艺路线", "不包含“手工穿绳“"),
  G51("工艺", "大侧封袋", "G5", "标签工艺", "G51", "贴标签", "读取BOM内材料料号开头", "BOM内材料有料号开头是“4040”"),
  G52("工艺", "大侧封袋", "G5", "标签工艺", "G52", "不贴标签", "读取BOM内材料料号开头", "BOM内材料无料号开头是“4040”"),
  G61("工艺", "ALL", "G6", "印刷工艺", "G61", "印刷", "读取工艺路线", "工艺路线包含“凹印“或“柔印“"),
  G62("工艺", "ALL", "G6", "印刷工艺", "G62", "不印刷", "读取工艺路线", "工艺路线不包含“凹印“或“柔印“"),
  G71("工艺", "ALL", "G7", "喷码工艺", "G71", "喷码", "读取工艺路线", "工艺路线包含“喷码“"),
  G72("工艺", "ALL", "G7", "喷码工艺", "G72", "打撕裂线", "读取工艺路线", "工艺路线包含“打撕裂线“"),
  G81("工艺", "ALL", "G8", "手提工艺", "G81", "有手提", "读取BOM材料品名", "BOM内材料品名有包含“手提条“材料"),
  G82("工艺", "ALL", "G8", "手提工艺", "G82", "无手提", "读取BOM材料品名", "BOM内材料品名无包含“手提条“材料"),
  G91("工艺", "ALL", "G9", "刻码工艺", "G91 ", "激光刻码", "读取工艺路线", "包含“激光刻码“"),
  G92("工艺", "ALL", "G9", "刻码工艺", "G92", "无激光刻码", "读取工艺路线", "不包含“激光刻码“"),
  G101("工艺", "ALL", "G10", "检码工艺", "G101", "双检", "读取工艺路线", "包含“机器双检“"),
  G102("工艺", "ALL", "G10", "检码工艺", "G102", "检码", "读取工艺路线", "包含“机器检码“"),
  B01("薄膜", "ALL", "B0", "印刷膜材质", "B01", "PPE", "读取BOM作业编码YS或RY对应材料品名", "包含“PPE“"),
  B02("薄膜", "ALL", "B0", "印刷膜材质", "B02", "PE", "读取BOM作业编码YS或RY对应材料品名", "包含“PE“不包含“PPE“"),
  B03("薄膜", "ALL", "B0", "印刷膜材质", "B03", "PP", "读取BOM作业编码YS或RY对应材料品名", "包含“PP“不包含“PPE“"),
  B04("薄膜", "ALL", "B0", "印刷膜材质", "B04", "其它材质", "读取BOM作业编码YS或RY对应材料品名", "不包含“PE“或“PP“或“PPE“"),
  B11("薄膜", "ALL", "B1", "复合膜材质", "B11", "金砂膜", "读取BOM作业编码FH对应材料品名", "包含“金砂膜“"),
  B12("薄膜", "ALL", "B1", "复合膜材质", "B12", "拉丝膜", "读取BOM作业编码FH对应材料品名", "包含“拉丝膜“"),
  B13("薄膜", "ALL", "B1", "复合膜材质", "B13", "PE", "读取BOM作业编码FH对应材料品名", "包含“PE“不包含“金砂膜“&”拉丝膜“&“磨砂膜”"),
  B14("薄膜", "ALL", "B1", "复合膜材质", "B14", "PP", "读取BOM作业编码FH对应材料品名", "包含“PP“不包含“PPE“"),
  B15("薄膜", "ALL", "B1", "复合膜材质", "B15", "其它材质", "读取BOM作业编码FH对应材料品名", "除以上其他"),
  B21("薄膜", "ALL", "B2", "薄膜颜色", "B21", "透明", "读取材料品名", "材料品名第五位字符为“0“"),
  B22("薄膜", "ALL", "B2", "薄膜颜色", "B22", "乳白", "读取材料品名", "材料品名第五位字符为“1“"),
  B23("薄膜", "ALL", "B2", "薄膜颜色", "B23", "消光", "读取材料品名", "材料品名第五位字符为“2“"),
  B24("薄膜", "ALL", "B2", "薄膜颜色", "B24", "消光乳白", "读取材料品名", "材料品名第五位字符为“3“"),
  B25("薄膜", "ALL", "B2", "薄膜颜色", "B25", "其它颜色", "读取材料品名", "除以上其他"),
  B31("薄膜", "ALL", "B3", "薄膜厚度", "B31", "印刷膜厚度", "读取BOM作业编码YS或RY对应材料规格", "取材料规格”*“前数字"),
  B32("薄膜", "ALL", "B3", "薄膜厚度", "B32", "复合膜厚度", "读取BOM作业编码FH对应材料规格", "取材料规格”*“前数字"),
  B41(
      "薄膜",
      "ALL",
      "B4",
      "薄膜宽度",
      "B41",
      "印刷膜宽度",
      "读取BOM作业编码YS或RY对应材料规格",
      "取材料规格”*“后数字且不包含“（）“及“（）“内内容"),
  B42(
      "薄膜",
      "ALL",
      "B4",
      "薄膜宽度",
      "B42",
      "复合膜宽度",
      "读取BOM作业编码FH对应材料规格",
      "取材料规格”*“后数字且不包含“（）“及“（）“内内容"),
  B43(
      "薄膜",
      "ALL",
      "B4",
      "薄膜宽度",
      "B43",
      "薄膜宽度",
      "读取BOM第一个主要材料对应材料规格",
      "取材料规格”*“后数字且不包含“（）“及“（）“内内容"),
  D1("袋子规格", "ALL", "D1", "袋子长度", "D1", "袋子长度区分", "物料基础资料", "物料基础资料内“产品长度“数据"),
  D2("袋子规格", "ALL", "D2", "袋子宽度", "D2", "袋子宽度区分", "产品规格", "产品规格“（）“内“+“前数字"),
  D3("袋子规格", "ALL", "D3", "袋子厚度", "D3", "袋子厚度区分", "物料基础资料", "物料基础资料内“产品厚度“数据"),
  D41("袋子规格", "ALL", "D4", "袋边特征", "D41", "齐边", "产品规格", "产品规格“（）“内第一个“+“前后数字相等"),
  D42("袋子规格", "ALL", "D4", "袋边特征", "D42", "大小边", "产品规格", "产品规格“（）“内第一个“+“前后数字不相等"),
  D5("袋子规格", "ALL", "D5", "白袋区分", "D5", "白袋(不印刷)", "读取工艺路线", "不包含“柔印“且不包含“凹印“"),
  D6("袋子规格", "ALL", "D6", "正面宽度", "D6", "袋子正面宽度区分", "产品规格", "产品规格“（）“内第一个“+“前数字"),
  Z11("制袋方法", "ALL", "Z1", "刀具区分", "Z11", "平刀", "待确认", "待给出"),
  Z12("制袋方法", "ALL", "Z1", "刀具区分", "Z12", "齿刀", "待确认", "待给出"),
  Z21("制袋方法", "大底封卷料", "Z2", "刀数区分", "Z21", "单刀", "读取工艺路线括号内备注信息", "“()“内包含”单刀“字段"),
  Z22("制袋方法", "大底封卷料", "Z2", "刀数区分", "Z22", "双刀", "读取工艺路线括号内备注信息", "“()“内不包含”单刀“字段或无括号备注字段"),
  T1("通道数", "ALL", "T", "通道数", "T1", "单通道", "物料基础资料", "物料基础资料内“通道数“数据"),
  T2("通道数", "ALL", "T", "通道数", "T2", "双通道", "物料基础资料", "物料基础资料内“通道数“数据"),
  T3("通道数", "ALL", "T", "通道数", "T3", "三通道", "物料基础资料", "物料基础资料内“通道数“数据"),
  L1("特殊料号", "ALL", "L", "特殊料号", "L1", "特殊料号", "产品料号", "特殊料号清单"),
  Q1("入库需求数量", "ALL", "Q", "入库需求数量", "Q1", "入库需求数量", "取该笔工单入库需求数量", "取该笔工单入库需求数量");

  private final String classes;

  private final String itemType;

  private final String code;

  private final String option;

  private final String optionCode;

  private final String optionName;

  private final String optionMethod;

  private final String optionRule;

  public static XHFManufactureRuleEnum valueOfOptionCode(String optionCode) {
    return ArrayUtil.firstMatch(
        e -> e.getOptionCode().equals(optionCode), XHFManufactureRuleEnum.values());
  }
}
