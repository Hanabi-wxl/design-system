package edu.dlu.bysj.base.exception;

import edu.dlu.bysj.base.result.ResultCodeEnum;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.net.BindException;

/**
 * 全局异常定义
 * @author XiangXinGang
 * @date 2021/10/13 10:30
 */
public class GlobalException extends RuntimeException{
    private final Integer code;

    public Integer getCode() {
        return code;
    }

    /**
     * 手动填充状态码和错误消息
     * @param code  状态码
     * @param message 错误消息
     */
    public GlobalException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    /**
     * 使用枚举类填充;
     * @param resultCodeEnum  返回值状态码枚举类
     */
    public GlobalException(ResultCodeEnum resultCodeEnum) {
        super(resultCodeEnum.getMessage());
        this.code = resultCodeEnum.getCode();
    }

    @Override
    public String toString() {
        return "GlobalException{" +
                "code=" + code +
                "message =" +this.getMessage()+
                '}';
    }
}
