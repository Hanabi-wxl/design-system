package edu.dlu.bysj.base.handler;

import edu.dlu.bysj.base.exception.GlobalException;
import edu.dlu.bysj.base.result.CommonResult;
import edu.dlu.bysj.base.result.ResultCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.UnauthenticatedException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 全局异常统一处理
 *
 * @author XiangXinGang
 * @date 2021/10/13 10:41
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 对全局自定义异常进行拦截
     *
     * @param exception GlobalException
     * @return 通用返回结果
     */
    @ExceptionHandler(GlobalException.class)
    @ResponseBody
    public CommonResult<Object> customizeHandler(GlobalException exception) {
        exception.printStackTrace();
        //根据code 和 message 抛出异常
        return CommonResult.failed(exception.getCode(), exception.getMessage());
    }


    /**
     * 拦截授权异常
     *
     * @param exception
     * @return
     */
    @ExceptionHandler(AuthorizationException.class)
    @ResponseBody
    public CommonResult<Object> authorizationHandler(AuthorizationException exception) {
        exception.printStackTrace();
        return CommonResult.failed(ResultCodeEnum.UNAUTHORIZED);
    }

    /**
     * 拦截没有权限异常
     *
     * @param exception
     * @return
     */
    @ExceptionHandler(UnauthenticatedException.class)
    @ResponseBody
    public CommonResult<Object> unauthenticatedExceptionHandler(UnauthenticatedException exception) {
        exception.printStackTrace();
        return CommonResult.failed(ResultCodeEnum.UNAUTHORIZED);
    }

    /**
     * BindException 拦截参数校验异常
     */
    @ExceptionHandler(BindException.class)
    @ResponseBody
    public CommonResult<Map<String, String>> bindExceptionHandler(BindException exception) {
        log.info("参数发生错误");
        //将错误包装成collection;
        Map<String, String> collect = exception.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
        exception.printStackTrace();
        /*参数校验未通过提示参数校验值*/
        return CommonResult.validated(ResultCodeEnum.VALIDATE_FAILED, collect);
    }

    /**
     * ConstraintViolationException 拦截异常参数的校验
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseBody
    public CommonResult<Map<Path, String>> constraintViolationHandler(ConstraintViolationException exception) {
        log.info("参数校验发生错误");
        /*从异常中获取信息*/
        Map<Path, String> collect = exception.getConstraintViolations().stream()
                .collect(Collectors.toMap(ConstraintViolation::getPropertyPath, ConstraintViolation::getMessage));
        return CommonResult.validated(ResultCodeEnum.VALIDATE_FAILED, collect);
    }


}
