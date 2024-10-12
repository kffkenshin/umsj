package cn.yessoft.umsj.common.utils;

import cn.hutool.core.date.DateUtil;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

public class BaseUtils {
  /**
   * 字符串返回
   *
   * @param obj
   * @return
   */
  public static String toString(Object obj) {
    return toString(obj, "");
  }

  /**
   * 字符串返回
   *
   * @param obj
   * @param str 默认值
   * @return
   */
  public static String toString(Object obj, String str) {

    if (obj == null || obj.equals("null") || obj.toString().isEmpty()) {
      return str;
    } else {
      return obj.toString().trim();
    }
  }

  /**
   * @param obj
   * @return @Title: Hql like String @Description:
   * @author chen xin
   */
  public static String toLikeString(Object obj) {
    return "%" + toString(obj) + "%";
  }

  /**
   * Gets the substring after the first occurrence of a separator. The separator is not returned.
   *
   * <p>A {@code null} string input will return {@code null}. An empty ("") string input will return
   * the empty string. A {@code null} separator will return the empty string if the input string is
   * not {@code null}.
   *
   * <p>If nothing is found, the empty string is returned.
   *
   * <pre>
   * BaseUtil.substringAfter(null, *)      = null
   * BaseUtil.substringAfter("", *)        = ""
   * BaseUtil.substringAfter(*, null)      = ""
   * BaseUtil.substringAfter("abc", "a")   = "bc"
   * BaseUtil.substringAfter("abcba", "b") = "cba"
   * BaseUtil.substringAfter("abc", "c")   = ""
   * BaseUtil.substringAfter("abc", "d")   = "abc"
   * BaseUtil.substringAfter("abc", "")    = "abc"
   * </pre>
   *
   * @param str the String to get a substring from, may be null
   * @param separator the String to search for, may be null
   * @return the substring after the first occurrence of the separator, {@code null} if null String
   *     input
   * @since 2.0
   */
  public static String substringAfter(final String str, final String separator) {
    if (isEmpty(str)) {
      return str;
    }
    if (separator == null) {
      return "";
    }
    final int pos = str.indexOf(separator);
    if (pos < 0) {
      return str;
    }
    return str.substring(pos + separator.length());
  }

  /**
   * 判断对象是否Empty(null或元素为0)<br>
   * 实用于对如下对象做判断:String Collection及其子类 Map及其子类
   *
   * @param pObj 待检查对象
   * @return boolean 返回的布尔值
   */
  @SuppressWarnings("rawtypes")
  public static boolean isEmpty(Object pObj) {

    if (pObj == null) return true;
    if (pObj.equals("")) return true;
    if (pObj == "") return true;
    if (pObj instanceof String) {
      return ((String) pObj).length() < 1;
    } else if (pObj instanceof Collection) {
      return ((Collection) pObj).size() < 1;
    } else if (pObj instanceof Map) {
      return ((Map) pObj).size() < 1;
    } else if (pObj.getClass().isArray()) {
      return Array.getLength(pObj) < 1;
    }
    return false;
  }

  public static boolean isNotEmpty(Object pObj) {
    return !isEmpty(pObj);
  }

  public static boolean isNumeric(String str) {
    if (str == null) return true;
    return str.matches("^[0-9]*$");
  }

  public static boolean isDouble(String str) {
    if (str == null) return true;
    return str.matches("^[-+]?(\\d+(\\.\\d*)?|\\.\\d+)$");
  }

  public static boolean isDouble(Object obj) {
    if (obj == null) return true;
    if (obj instanceof Double) {
      return true;
    }
    return isDouble(obj.toString());
  }

  public static boolean isNumeric(Object obj) {
    if (obj == null) return true;
    return isNumeric(obj.toString());
  }

  public static Integer objtoint(Object obj) {
    return objtoint(obj, 0);
  }

  public static Integer objtoint(Object obj, Integer iter) {
    if (isNumeric(obj)) {
      return isEmpty(obj) ? iter : Integer.parseInt(obj.toString());
    } else {
      return iter;
    }
  }

  public static Long objtolong(Object obj) {
    return objtolong(obj, 0L);
  }

  public static Long objtolong(Object obj, Long iter) {
    if (isNumeric(obj)) {
      return isEmpty(obj) ? iter : Long.parseLong(obj.toString());
    } else {
      return iter;
    }
  }

  public static Double objToDouble(Object obj) {
    return objToDouble(obj, (double) 0L);
  }

  public static Double objToDoubleHasNull(Object obj) {
    if (isDouble(obj)) {
      return isEmpty(obj) ? null : Double.parseDouble(obj.toString());
    } else {
      return null;
    }
  }

  public static Double objToDouble(Object obj, Double num) {
    if (isDouble(obj)) {
      return isEmpty(obj) ? num : Double.parseDouble(obj.toString());
    } else {
      return num;
    }
  }

  public static Boolean objToBoolean(Object obj) {
    return objToBoolean(obj, false);
  }

  public static Boolean objToBoolean(Object obj, Boolean bool) {
    String value = toString(obj).trim();
    if (value.isEmpty()) {
      return isEmpty(bool) ? false : bool;
    }
    if ("是".equals(value)) {
      return true;
    }
    if ("否".equals(value)) {
      return false;
    }
    return Boolean.parseBoolean(value);
  }

  /**
   * 判断字符串是否是整数
   *
   * @param str
   * @return
   */
  public static boolean isInteger(String str) {
    if (str == null) return true;
    return str.matches("^[-\\+]?\\d+$");
  }

  /**
   * 判断字符串是否是浮点数
   *
   * @param str
   * @return
   */
  public static boolean isFloat(String str) {
    if (str == null) return true;
    return str.matches("^-?([1-9]\\d*\\.\\d*|0\\.\\d*[1-9]\\d*|0?\\.0+|0)$") || isInteger(str);
  }

  /**
   * 字符串转为日期 注意格式是 yyyy-MM-dd
   *
   * @param str
   * @return Date
   */
  public static Date strToDate(String str) {
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    Date date = null;
    try {
      date = formatter.parse(str);
    } catch (ParseException e) {
      e.printStackTrace();
    }
    return date;
  }

  /**
   * 日期转为字符串 注意格式是 yyyy-MM-dd
   *
   * @return str
   */
  public static String dateToStr(Date date) {
    if (BaseUtils.isEmpty(date)) return null;
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    return formatter.format(date);
  }

  // 四舍五入，并且保存n位小数
  public static Double getNumber(Double d, Integer n) {
    BigDecimal b = new BigDecimal(d);
    double f = b.setScale(n, BigDecimal.ROUND_HALF_UP).doubleValue();
    return f;
  }

  /**
   * double 加法 v1+v2
   *
   * @param v1
   * @param v2
   * @return
   */
  public static Double doubleAdd(Double v1, Double v2) {
    if (v1 == null) {
      v1 = 0d;
    }
    if (v2 == null) {
      v2 = 0d;
    }
    BigDecimal b1 = new BigDecimal(v1.toString());
    BigDecimal b2 = new BigDecimal(v2.toString());
    Double f = b1.add(b2).doubleValue();
    return getNumber(f, 8);
  }

  /**
   * double 减法 v1-v2
   *
   * @param v1
   * @param v2
   * @return
   */
  public static Double doubleSub(Double v1, Double v2) {
    if (v1 == null) {
      v1 = 0d;
    }
    if (v2 == null) {
      v2 = 0d;
    }
    BigDecimal b1 = new BigDecimal(v1.toString());
    BigDecimal b2 = new BigDecimal(v2.toString());
    Double f = b1.subtract(b2).doubleValue();
    return getNumber(f, 8);
  }

  /**
   * double 乘法 v1*v2
   *
   * @param v1
   * @param v2
   * @return
   */
  public static Double doubleMul(Double v1, Double v2) {
    if (isEmpty(v1) || isEmpty(v2)) {
      return 0d;
    }
    BigDecimal b1 = new BigDecimal(v1.toString());
    BigDecimal b2 = new BigDecimal(v2.toString());
    Double f = b1.multiply(b2).doubleValue();
    return getNumber(f, 8);
  }

  /**
   * double 除法 v1/v2 默认保留小数点后六位
   *
   * @param v1
   * @param v2
   * @return if v2==0D return 0D
   */
  public static Double doubleDivde(Double v1, Double v2) {
    return doubleDivde(v1, v2, 8);
  }

  /**
   * double 除法 v1/v2
   *
   * @param v1
   * @param v2
   * @param scale 除不尽时候精确的小数点位数
   * @return
   */
  public static Double doubleDivde(Double v1, Double v2, int scale) {
    if (scale < 0) {
      scale = 0;
    }
    if (v2.compareTo(0D) == 0) {
      return 0D;
    }
    BigDecimal b1 = new BigDecimal(v1.toString());
    BigDecimal b2 = new BigDecimal(v2.toString());
    return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
  }

  /**
   * double 小数位精确
   *
   * @param v1 需要四舍五入的double数字
   * @param scale 保留小数点后几位
   * @return
   */
  public static Double doubleRound(Double v1, int scale) {
    if (scale < 0) {
      scale = 0;
    }
    BigDecimal b1 = new BigDecimal(v1.toString());
    BigDecimal b2 = new BigDecimal("1");
    return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
  }

  /**
   * Description:类型强转
   *
   * @param obj
   * @return
   * @author cx 2017年4月27日上午11:01:01
   */
  @SuppressWarnings("unchecked")
  public static <T> T convertObject(Object obj) {
    return (T) obj;
  }

  public static void main(String[] args) {
    // Double a = 1.0021d, b = 9.01d;
    System.out.println();
    // System.out.println(a - b);
    // System.out.println(a / b);
  }

  // 判断ip地址是否符合配置 ipSection
  public static boolean ipIsValid(String ipSection, String ip) {
    if (ipSection == null) throw new NullPointerException("IP段不能为空！");
    if (ip == null) throw new NullPointerException("IP不能为空！");
    ipSection = ipSection.trim();
    ip = ip.trim();
    final String REGX_IP =
        "^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\."
            + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
            + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
            + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$";
    final String REGX_IPB =
        "^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\."
            + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
            + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
            + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)"
            + "\\-"
            + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\."
            + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
            + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
            + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)"
            + "$";
    if (!ipSection.matches(REGX_IPB) || !ip.matches(REGX_IP)) return false;
    int idx = ipSection.indexOf('-');
    String[] sips = ipSection.substring(0, idx).split("\\.");
    String[] sipe = ipSection.substring(idx + 1).split("\\.");
    String[] sipt = ip.split("\\.");
    long ips = 0L, ipe = 0L, ipt = 0L;
    for (int i = 0; i < 4; ++i) {
      ips = ips << 8 | Integer.parseInt(sips[i]);
      ipe = ipe << 8 | Integer.parseInt(sipe[i]);
      ipt = ipt << 8 | Integer.parseInt(sipt[i]);
    }
    if (ips > ipe) {
      long t = ips;
      ips = ipe;
      ipe = t;
    }
    return ips <= ipt && ipt <= ipe;
  }

  public static String getCurrentTimeStr() {
    return DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss");
  }

  public static LocalDateTime toLocalDateTime(Date date) {
    Instant instant = date.toInstant(); // 将Date转换为Instant
    ZoneId zoneId = ZoneId.systemDefault(); // 获取系统默认时区
    return LocalDateTime.ofInstant(instant, zoneId); // 将Instant转换为LocalDateTime
  }
}
