package edu.dlu.bysj.base.model.enums;

/**
 * @author XiangXinGang
 * @date 2021/10/17 21:24
 */
public enum ManagerEnum {
    MAJOR_MANGER(3,"majorManager"),
    COLLEGE_MANAGER(4,"collegeManager"),
    SCHOOL_MANAGER(5,"schoolManger");

    ManagerEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    private Integer code;
    private String message;

    public static String mangerRoleName(Integer code) {
        ManagerEnum[] values = ManagerEnum.values();
        for (ManagerEnum element: values) {
            if (code .equals(element.getCode())) {
                return element.getMessage();
            }
        }
       return "";
    }

}
