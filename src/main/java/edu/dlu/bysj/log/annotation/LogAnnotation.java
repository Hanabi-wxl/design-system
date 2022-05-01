package edu.dlu.bysj.log.annotation;

import edu.dlu.bysj.log.enums.LogType;

import java.lang.annotation.*;

/**
 * @author XiangXinGang
 * @date 2021/10/22 21:27
 */
// 定义一个方法级注解
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LogAnnotation {
  
  /**
   * 日志操作内容
   *
   * @return
   */
  String content() default "";

  /**
   * 日志操作类型 (默认为操作日志，其他类型需要自己使用LogType中的值);
   *
   * @return
   */
  LogType type() default LogType.OPERATION_TYPE;
}
