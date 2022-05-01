package edu.dlu.bysj.base.result;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author XiangXinGang
 * @date 2021/10/7 16:15
 */
@Data
public class CommonResult<T> {

    @ApiModelProperty(value = "返回码")
    private Integer code;
    @ApiModelProperty(value = "返回消息")
    private String message;
    @ApiModelProperty(value = "返回数据")
    private T data;

    public CommonResult() {

    }

    public CommonResult(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    /**
     * 返回成功结果获取的数据
     *
     * @param data 获取的数据
     * @param <T>
     * @return
     */
    public static <T> CommonResult<T> success(T data) {
        return new CommonResult<T>(ResultCodeEnum.SUCCESS.getCode(), ResultCodeEnum.SUCCESS.getMessage(), data);
    }

    /**
     * 成功返回结果
     *
     * @param data    获取的数据
     * @param message 提示信息
     * @param <T>
     * @return
     */
    public static <T> CommonResult<T> success(T data, String message) {
        return new CommonResult<T>(ResultCodeEnum.SUCCESS.getCode(), message, data);
    }

    /**
     * 参数错误校验统一返回结果
     *
     * @param errorCode 错误码
     * @param date      数据
     * @param <T>       泛型
     * @return
     */
    public static <T> CommonResult<T> validated(IErrorCode errorCode, T date) {
        return new CommonResult<T>(errorCode.getCode(), errorCode.getMessage(), date);
    }

    /**
     * 返回失败的结果
     *
     * @param errorCode 错误码
     * @param <T>
     * @return
     */
    public static <T> CommonResult<T> failed(IErrorCode errorCode) {
        return new CommonResult<T>(errorCode.getCode(), errorCode.getMessage(), null);
    }

    /**
     * 返回失败结果
     *
     * @param errorCode 错误码
     * @param message   错误信息
     * @param <T>
     * @return
     */
    public static <T> CommonResult<T> failed(IErrorCode errorCode, String message) {
        return new CommonResult<T>(errorCode.getCode(), message, null);
    }

    /**
     * 返回错误结果
     *
     * @param message 提示信息
     * @param <T>
     * @return
     */
    public static <T> CommonResult<T> failed(String message) {
        return new CommonResult<T>(ResultCodeEnum.FAILED.getCode(), message, null);
    }

    public static <T> CommonResult<T> failed(Integer code, String message) {
        return new CommonResult<T>(code, message, null);
    }

    /**
     * 返回失败结果
     *
     * @param <T>
     * @return
     */
    public static <T> CommonResult<T> failed() {
        return failed(ResultCodeEnum.FAILED);
    }

    public static <T> CommonResult<T> failed(T date) {
        return new CommonResult<T>(ResultCodeEnum.FAILED.getCode(), ResultCodeEnum.FAILED.getMessage(), date);
    }


    /**
     * 未授权返回结果
     *
     * @param data 获取的数据
     * @param <T>
     * @return
     */
    public static <T> CommonResult<T> forbidden(T data) {
        return new CommonResult<T>(ResultCodeEnum.FORBIDDEN.getCode(), ResultCodeEnum.FORBIDDEN.getMessage(), data);
    }

    /**
     * 未登录返回结果
     *
     * @param data
     * @param <T>
     * @return
     */
    public static <T> CommonResult<T> unauthorized(T data) {
        return new CommonResult<>(ResultCodeEnum.UNAUTHORIZED.getCode(), ResultCodeEnum.UNAUTHORIZED.getMessage(), data);
    }

    /**
     * 参数验证失败返回结果
     *
     * @param <T>
     * @return
     */
    public static <T> CommonResult<T> validateFailed() {
        return failed(ResultCodeEnum.VALIDATE_FAILED);
    }

    /**
     * 参数验证失败返回信息
     *
     * @param message
     * @param <T>
     * @return
     */
    public static <T> CommonResult<T> validateFailed(String message) {
        return new CommonResult<T>(ResultCodeEnum.VALIDATE_FAILED.getCode(), message, null);
    }

}
