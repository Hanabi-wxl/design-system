package edu.dlu.bysj.security.filter;


import cn.hutool.json.JSONUtil;
import edu.dlu.bysj.base.result.CommonResult;
import edu.dlu.bysj.base.result.ResultCodeEnum;
import edu.dlu.bysj.base.util.JwtUtil;
import edu.dlu.bysj.security.model.token.JwtToken;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 自定义认证过滤器，用来拦截 header 中携带jwt token 的请求
 *
 * @author XiangXinGang
 * @date 2021/10/11 16:09
 */
@Slf4j
public class JwtFilter extends BasicHttpAuthenticationFilter {

    /**
     * 前置处理
     */
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = WebUtils.toHttp(request);
        HttpServletResponse httpServletResponse = WebUtils.toHttp(response);
        // 跨域时会首先发送一个option请求，这里我们给option请求直接返回正常状态
        if (httpServletRequest.getMethod().equals(RequestMethod.OPTIONS.name())) {
            httpServletResponse.setStatus(HttpStatus.OK.value());
            return false;
        }
        return super.preHandle(request, response);
    }

    /**
     * 后置处理
     */
    @Override
    protected void postHandle(ServletRequest request, ServletResponse response) {
        //添加跨域支持
        this.fillCorsHeader(WebUtils.toHttp(request), WebUtils.toHttp(response));
    }

    /**
     * 过滤器拦截请求的入口方法
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        //内部使用重写的 isLoginAttempt判断;
        this.fillCorsHeader(WebUtils.toHttp(request), WebUtils.toHttp(response));
        if (this.isLoginRequest(request, response)) {
            return false;
        }
        boolean allowed = false;
        try {
            //检测Header 里面JWT TOKEN 内容是否正确
            allowed = executeLogin(request, response);
        } catch (Exception e) {
            log.error("not fund any token");
        }
        return allowed || super.isPermissive(mappedValue);
    }

    /**
     * 身份检查 JWT TOKEN是否合法
     */
    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String token = httpServletRequest.getHeader("JWT");
        JwtToken jwtToken = new JwtToken(token);
        // 提交给realm进行登入，如果错误他会抛出异常并被捕获
        try {
            getSubject(request, response).login(jwtToken);
            // 如果没有抛出异常则代表登入成功，返回true
            return onLoginSuccess(jwtToken, SecurityUtils.getSubject(),request,response);
        } catch (AuthenticationException e) {
            return this.onLoginFailure(jwtToken, e, request, response);
        }
    }

    /**
     * shiro 利用jwt token 登录成功，会进入该方法
     */
    @Override
    protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request, ServletResponse response) {
        HttpServletResponse httpServletResponse = WebUtils.toHttp(response);
        String newToken = null;
        // 如果验证为jwt token 就使用旧token 去跟新新token 的过期时间;
        if (token instanceof JwtToken) {
            newToken = JwtUtil.refreshToken(token.getCredentials().toString());
        }
        //将新token 放入到Header中;
        if (newToken != null) {
            httpServletResponse.setHeader(JwtUtil.AUTH_HEADER, newToken);
        }
        return true;
    }

    /**
     * shiro 利用 jwt token 登录失败，会进入该方法
     */

    @Override
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException exception,
                                     ServletRequest request, ServletResponse response) {
        // 直接返回false 交给onAccessDenied()
        return false;
    }

    /**
     * 检测Header 中包含的Jwt字段
     */
    @Override
    protected boolean isLoginAttempt(ServletRequest request, ServletResponse response) {
        HttpServletRequest httpServletRequest = WebUtils.toHttp(request);
        return httpServletRequest.getHeader(JwtUtil.AUTH_HEADER) == null;
    }

    /**
     * 从Header 中获取jwt token
     */
    @Override
    protected AuthenticationToken createToken(ServletRequest servletRequest, ServletResponse servletResponse) {
        HttpServletRequest httpServletRequest = WebUtils.toHttp(servletRequest);
        String authorization = httpServletRequest.getHeader(JwtUtil.AUTH_HEADER);
        return new JwtToken(authorization);
    }


    /**
     * isAccessAllowed() 方法返回false ，会进入该方法表示拒绝访问
     * 返回 true 则允许访问
     * 返回false 则禁止访问，会进入 onAccessDenied()
     */
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws IOException {
        HttpServletResponse httpServletResponse = WebUtils.toHttp(response);
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setContentType("application/json;charset=UTF-8");
        //设置json 格式的返回值
        String result = JSONUtil.toJsonStr(CommonResult.failed(ResultCodeEnum.FORBIDDEN));
        response.getWriter().write(result);
        //添加跨域支持;
        this.fillCorsHeader(WebUtils.toHttp(request), httpServletResponse);

        //扔出异常判断是否登录情况;
        return false;
    }

    /**
     * 添加跨域支持
     */
    protected void fillCorsHeader(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        httpServletResponse.setHeader("Access-control-Allow-Origin", httpServletRequest.getHeader("Origin"));
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,HEAD,DELETE,PUT,PATCH");
        httpServletResponse.setHeader("Access-Control-Allow-Headers", httpServletRequest.getHeader("Access-Control-Request-Headers"));
        httpServletResponse.setHeader("Access-Control-Expose-Headers","JWT");
    }

}
