package cn.yessoft.umsj.moduler.base.controller.vo.role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoleMemberCreateVO {

    /**
     * 成员类型
     */
    private Integer type;

    private String roleId;

    private String memberId;

    private String memberName;

}
