package cn.yessoft.umsj.moduler.base.controller.vo.role;

import cn.yessoft.umsj.moduler.base.enums.RoleTpyeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoleMemberRespVO {
    private Long id;

    /**
     * 成员类型
     */
    private String typeStr;

    private Integer type;

    /**
     * 成员名称
     */
    private String memberName;

    public String getTypeStr() {
        return RoleTpyeEnum.valueOf(type).getName();
    }

}
