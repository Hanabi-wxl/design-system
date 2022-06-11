package edu.dlu.bysj.base.exception;

import edu.dlu.bysj.base.result.CommonResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
 
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
 
/**
 * 全局异常处理 无法处理filter抛出的异常
 */
@RestControllerAdvice
public class OnlineGlobalException {
 
    private static Logger logger = LoggerFactory.getLogger(OnlineGlobalException.class);
 
    /**
     * 方法参数校验异常 Validate
     * @param request
     * @param ex
     * @return
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseBody
    public CommonResult<Object> handleValidationException(HttpServletRequest request, ConstraintViolationException ex) {
        logger.error("异常:" + request.getRequestURI(), ex);
        String collect = ex.getConstraintViolations().stream().filter(Objects::nonNull)
                .map(cv -> cv == null ? "null" : cv.getPropertyPath() + ": " + cv.getMessage())
                .collect(Collectors.joining(", "));
        CommonResult<Object> restResultWrapper = new CommonResult<>();
        logger.info("请求参数异常",collect);
        restResultWrapper.setCode(HttpStatus.BAD_REQUEST.value());
        restResultWrapper.setMessage(ex.getMessage());
        return restResultWrapper;
    }
 
    /**
     * Bean 校验异常 Validate
     * @param request
     * @param exception
     * @return
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class) //400
    @ResponseBody
    public CommonResult<Object> methodArgumentValidationHandler(HttpServletRequest request, MethodArgumentNotValidException exception){
        logger.info("异常:" + request.getRequestURI(), exception);
        logger.info("请求参数错误！{}",getExceptionDetail(exception),"参数数据："+showParams(request));
        CommonResult<Object> restResultWrapper = new CommonResult<>();
        restResultWrapper.setCode(HttpStatus.BAD_REQUEST.value());
        if (exception.getBindingResult() != null && !CollectionUtils.isEmpty(exception.getBindingResult().getAllErrors())) {
            restResultWrapper.setMessage(exception.getBindingResult().getAllErrors().get(0).getDefaultMessage());
        } else {
            restResultWrapper.setMessage(exception.getMessage());
        }
        return restResultWrapper;
    }
 
    /**
     * 绑定异常
     * @param request
     * @param pe
     * @return
     */
    @ExceptionHandler(BindException.class)
    @ResponseBody
    public CommonResult<Object> bindException(HttpServletRequest request, BindException pe) {
        logger.error("异常:" + request.getRequestURI(), pe);
        CommonResult<Object> restResultWrapper = new CommonResult<>();
        Map map=new HashMap();
        if(pe.getBindingResult()!=null){
            List<ObjectError> allErrors = pe.getBindingResult().getAllErrors();
            allErrors.stream().filter(Objects::nonNull).forEach(objectError -> {
                map.put("请求路径："+request.getRequestURI()+"--请求参数："+(((FieldError) ((FieldError) allErrors.get(0))).getField().toString()),objectError.getDefaultMessage());
            });
        }
        restResultWrapper.setCode(HttpStatus.BAD_REQUEST.value());
        restResultWrapper.setMessage("请求参数绑定失败");
        restResultWrapper.setData(map.toString());
        return restResultWrapper;
    }
 
 
    /**
     * 访问接口参数不全
     * @param request
     * @param pe
     * @return
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseBody
    public CommonResult<Object> missingServletRequestParameterException(HttpServletRequest request, MissingServletRequestParameterException pe) {
        logger.error("异常:" + request.getRequestURI(), pe);
        CommonResult<Object> restResultWrapper = new CommonResult<>();
        restResultWrapper.setCode(HttpStatus.BAD_REQUEST.value());
        restResultWrapper.setMessage("该请求路径："+request.getRequestURI()+"下的请求参数不全："+pe.getMessage());
        return restResultWrapper;
    }
 
    /**
     * HttpRequestMethodNotSupportedException
     * @param request
     * @param pe
     * @return
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseBody
    public CommonResult<Object> httpRequestMethodNotSupportedException(HttpServletRequest request, HttpRequestMethodNotSupportedException pe) {
        logger.error("异常:" + request.getRequestURI(), pe);
        CommonResult<Object> restResultWrapper = new CommonResult<>();
        restResultWrapper.setCode(HttpStatus.BAD_REQUEST.value());
        restResultWrapper.setMessage("请求方式不正确");
        return restResultWrapper;
    }
 
 
    /**
     * 其他异常
     * @param request
     * @param pe
     * @return
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public CommonResult<Object> otherException(HttpServletRequest request, Exception pe) {
        logger.error("异常:" + request.getRequestURI(), pe);
        CommonResult<Object> commonResult = new CommonResult<>();
        commonResult.setCode(HttpStatus.BAD_REQUEST.value());
        commonResult.setMessage(getExceptionDetail(pe));
        return commonResult;
    }
 
    /**
     * 异常详情
     * @param e
     * @return
     */
    private String getExceptionDetail(Exception e) {
        StringBuilder stringBuffer = new StringBuilder(e.toString() + "\n");
        StackTraceElement[] messages = e.getStackTrace();
        Arrays.stream(messages).filter(Objects::nonNull).forEach(stackTraceElement -> {
            stringBuffer.append(stackTraceElement.toString() + "\n");
        });
        return stringBuffer.toString();
    }
 
    /**
     * 请求参数
     * @param request
     * @return
     */
    public  String showParams(HttpServletRequest request) {
        Map<String,Object> map = new HashMap<String,Object>();
        StringBuilder stringBuilder=new StringBuilder();
        Enumeration paramNames = request.getParameterNames();
        stringBuilder.append("----------------参数开始-------------------");
        stringBuilder.append(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        if(Objects.nonNull(paramNames)){
            while (paramNames.hasMoreElements()) {
                String paramName = (String) paramNames.nextElement();
                String[] paramValues = request.getParameterValues(paramName);
                if (paramValues.length >0) {
                    String paramValue = paramValues[0];
                    if (paramValue.length() != 0) {
                        stringBuilder.append("参数名:").append(paramName).append("参数值:").append(paramValue);
                    }
                }
            }
        }
        stringBuilder.append("----------------参数结束-------------------");
        return stringBuilder.toString();
    }
 
 
}