package edu.dlu.bysj.base.util;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * 获取request的工具类;
 *
 * @author XiangXinGang
 * @date 2021/10/23 21:33
 */
public class HttpContextUtil {
  public static HttpServletRequest getHttpServletRequest() {
    return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
  }
}
