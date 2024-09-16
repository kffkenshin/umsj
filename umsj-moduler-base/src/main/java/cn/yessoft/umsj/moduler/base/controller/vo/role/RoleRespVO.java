package cn.yessoft.umsj.moduler.base.controller.vo.role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoleRespVO {
    private Long id;
    /**
     * 编号
     */
    private String roleNo;

    /**
     * 名称
     */
    private String roleName;

    /**
     * 是否系统保留
     */
    private Boolean retain;

    private Boolean disabled;

    public Boolean getDisabled() {
        return retain;
    }
}
