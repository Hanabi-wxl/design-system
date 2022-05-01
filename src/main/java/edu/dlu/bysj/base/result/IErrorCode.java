package edu.dlu.bysj.base.result;

/**
 * 常用api 返回对象接口
 * @author XiangXinGang
 * @date 2021/10/7 16:38
 */
public interface IErrorCode {
    /**
     * 返回码
     * @return 错误码
     */
    Integer getCode();

    /**
     * 返回信息
     * @return 错误消息
     */
    String getMessage();
}
