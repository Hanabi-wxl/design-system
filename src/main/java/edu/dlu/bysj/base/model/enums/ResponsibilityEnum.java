package edu.dlu.bysj.base.model.enums;

/**
 * @author XiangXinGang
 * @date 2021/11/7 20:44
 */
public enum ResponsibilityEnum {
    GROUP_LEADER("组长", "0"),
    DEPUTY_HEAD("副组长", "1"),
    SECRETARY("秘书", "2"),
    GROUP_NUMBER("组员", "3"),
    RESPONDENT("答辩人", "4");

    ResponsibilityEnum(String roleName, String responsibilityCode) {
        this.roleName = roleName;
        this.responsibilityCode = responsibilityCode;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getResponsibilityCode() {
        return responsibilityCode;
    }

    public void setResponsibilityCode(String responsibilityCode) {
        this.responsibilityCode = responsibilityCode;
    }

    /**
     * 角色名称
     */
    private String roleName;
    /**
     * 答辩职责id
     */
    private String responsibilityCode;


    public static String getResponsibilityMessage(String code) {
        ResponsibilityEnum[] values = ResponsibilityEnum.values();
        for (ResponsibilityEnum element : values) {
            if (element.getResponsibilityCode().equals(code)) {
                return element.getRoleName();
            }
        }
        return null;
    }
}
