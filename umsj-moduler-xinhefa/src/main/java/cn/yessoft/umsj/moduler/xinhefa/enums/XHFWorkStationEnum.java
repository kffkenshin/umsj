package cn.yessoft.umsj.moduler.xinhefa.enums;

import cn.hutool.core.util.ArrayUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * APS工作站
 */
@Getter
@AllArgsConstructor
public enum XHFWorkStationEnum {

    YS(100, "印刷"),
    PM(200, "喷码"),
    FH(300, "复合"),
    JM(400, "检码"),
    TJ(500, "烫金"),
    KM(600, "刻码"),
    ZDX(850, "制袋线"),
    ZDJ(900, "制袋机"),
    CM(2000, "吹膜"),
    ;

    private final Integer code;

    private final String name;

    public static XHFWorkStationEnum valueOf(Integer value) {
        return ArrayUtil.firstMatch(e -> e.getCode().equals(value), XHFWorkStationEnum.values());
    }
}
