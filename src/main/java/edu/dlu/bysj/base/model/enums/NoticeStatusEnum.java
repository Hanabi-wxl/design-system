package edu.dlu.bysj.base.model.enums;

/**
 * @author XiangXinGang
 * @date 2021/11/8 21:07
 */
public enum NoticeStatusEnum {
    TOP_STATUS(0, "置顶"),
    ORDINARY_STATUS(1, "普通"),
    HIDE_STATUS(2, "隐藏");


    NoticeStatusEnum(Integer statusCode, String statusMessage) {
        this.statusCode = statusCode;
        this.statusMessage = statusMessage;
    }

    private Integer statusCode;
    private String statusMessage;

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public static String noticeStatus(Integer code) {
        NoticeStatusEnum[] values = NoticeStatusEnum.values();
        for (NoticeStatusEnum element : values) {
            if (element.getStatusCode().equals(code)) {
                return element.getStatusMessage();
            }
        }
        return null;
    }
}
