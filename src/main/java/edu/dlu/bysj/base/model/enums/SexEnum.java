package edu.dlu.bysj.base.model.enums;

/**
 * @author XiangXinGang
 * @date 2021/10/17 10:21
 */
public enum SexEnum {
    MAN("男","男"),
    WOMAN("女","女");
    private String code;
    private String message;

    SexEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    public static String sexDescription(String code) {
        SexEnum[] values = SexEnum.values();
        for (SexEnum element : values) {
            if (element.getCode().equals(code)) {
                return  element.getMessage();
            }
        }
        return "";
    }
}
