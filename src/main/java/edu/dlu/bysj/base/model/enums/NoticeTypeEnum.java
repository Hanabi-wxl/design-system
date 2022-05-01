package edu.dlu.bysj.base.model.enums;

/**
 * @author XiangXinGang
 * @date 2021/11/8 21:03
 */
public enum NoticeTypeEnum {
    WHOLE_SCHOOL("全校通知", 0),
    WHOLE_COLLEGE("全院通知", 1),
    WHOLE_MAJOR("全专业通知", 2);

    NoticeTypeEnum(String typeMessage, Integer typeCode) {
        this.typeMessage = typeMessage;
        this.typeCode = typeCode;
    }

    private String typeMessage;
    private Integer typeCode;

    public String getTypeMessage() {
        return typeMessage;
    }

    public void setTypeMessage(String typeMessage) {
        this.typeMessage = typeMessage;
    }

    public Integer getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(Integer typeCode) {
        this.typeCode = typeCode;
    }

    public static String noticeMessage(Integer code) {
        NoticeTypeEnum[] values = NoticeTypeEnum.values();
        for (NoticeTypeEnum element : values) {
            if (element.getTypeCode().equals(code)) {
                return element.getTypeMessage();
            }
        }
        return null;
    }
}
