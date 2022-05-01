package edu.dlu.bysj.base.result;

/**
 * @author XiangXinGang
 * @date 2021/10/7 15:59
 * 通用response状态码
 */
public enum ResultCodeEnum implements IErrorCode {
    SUCCESS(200,"操作成功"),
    NOTFOUND(404,"资源未找到"),
    UNAUTHORIZED(401,"没有相关权限"),
    VALIDATE_FAILED(405, "参数检验失败"),
    FORBIDDEN(403,"暂未登录或token验证失败"),
    FAILED(500, "操作失败"),
    SEND_MESSAGE_ERROR(700,"消息发送失败"),
    LOGIN_ERROR(100,"登录失败");


    private Integer code;
    private String message;

    private ResultCodeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return  message;
    }
}
