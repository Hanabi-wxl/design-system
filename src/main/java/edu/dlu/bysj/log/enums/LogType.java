package edu.dlu.bysj.log.enums;

/**
 * @author XiangXinGang
 * @date 2021/10/22 21:43 定义日志的操作类型
 */
public enum LogType {
  OPERATION_TYPE(0, "操作日志"),
  ERROR_TYPE(1, "错误日志"),
  LOG_IN_TYPE(2, "登录日志");

  private Integer code;
  private String message;

  LogType(Integer code, String message) {
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
}
