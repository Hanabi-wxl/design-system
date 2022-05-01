package edu.dlu.bysj.security.config;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.Filter;

import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authc.pam.AuthenticationStrategy;
import org.apache.shiro.authc.pam.FirstSuccessfulStrategy;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import edu.dlu.bysj.security.filter.JwtFilter;
import edu.dlu.bysj.security.model.mathcer.JwtCredentialsMatcher;
import edu.dlu.bysj.security.model.realm.JwtRealm;

/**
 * @author XiangXinGang
 * @date 2021/10/10 22:24
 */
@Configuration
public class ShiroConfig {

    /**
     * 交由 Spring 来自动地管理 Shiro-Bean 的生命周期
     */
    @Bean
    public static LifecycleBeanPostProcessor getLifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    /**
     * 为 Spring-Bean 开启对 Shiro 注解的支持
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor =
            new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }

    @Bean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator app = new DefaultAdvisorAutoProxyCreator();
        app.setProxyTargetClass(true);
        return app;
    }

    /**
     * 不向 Spring容器中注册 JwtFilter Bean，防止 Spring 将 JwtFilter 注册为全局过滤器 全局过滤器会对所有请求进行拦截，而本例中只需要拦截除 /common/login/student 和
     * /common/login/student 外的请求 另一种简单做法是：直接去掉 jwtFilter()上的 @Bean 注解
     */
    @Bean
    public FilterRegistrationBean<Filter> registration(JwtFilter filter) {
        FilterRegistrationBean<Filter> registration = new FilterRegistrationBean<>(filter);
        registration.setEnabled(false);
        return registration;
    }

    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager manager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(manager);

        /*前后端分离项目不需要从定向*/
        // shiroFilterFactoryBean.setLoginUrl("/common/login/student");
        // 添加jwt 专用过滤器 拦截除了登录之外的所有请求;

        Map<String, Filter> filterMap = new LinkedHashMap<>();
        filterMap.put("jwtFilter", jwtFilter());
        shiroFilterFactoryBean.setFilters(filterMap);

        // 添加拦截路径
        LinkedHashMap<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
        // 关于登录部分可以匿名访问
        // TODO 设置放行路径
        // 测试下载放行所有的路径
//        filterChainDefinitionMap.put("/**", "anon");

        filterChainDefinitionMap.put("/common/login/**", "anon");

        // 放行Swagger2页面，需要放行这些
        filterChainDefinitionMap.put("/swagger-ui.html", "anon");
        filterChainDefinitionMap.put("/swagger/**", "anon");
        filterChainDefinitionMap.put("/webjars/**", "anon");
        filterChainDefinitionMap.put("/swagger-resources/**", "anon");
        filterChainDefinitionMap.put("/v2/**", "anon");
        filterChainDefinitionMap.put("/static/**", "anon");
        // 拦截 需登录才能访问
         filterChainDefinitionMap.put("/**", "jwtFilter,authc");

        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;
    }

    @Bean
    public SecurityManager securityManager(JwtRealm jwtRealm) {
        DefaultWebSecurityManager manager = new DefaultWebSecurityManager();
        // 多realm
        List<Realm> realms = new ArrayList<>(16);
        realms.add(jwtRealm);
        manager.setRealms(realms);

        DefaultSessionStorageEvaluator evaluator = new DefaultSessionStorageEvaluator();
        evaluator.setSessionStorageEnabled(false);
        DefaultSubjectDAO subjectDAO = new DefaultSubjectDAO();
        subjectDAO.setSessionStorageEvaluator(evaluator);
        manager.setSubjectDAO(subjectDAO);
        return manager;
    }

    /**
     * jwt realm
     */
    @Bean
    public JwtRealm jwtRealm(EhCacheManager ehCacheManager) {

        JwtRealm jwtRealm = new JwtRealm();
        // 设置加密算法,不进行散列
        CredentialsMatcher matcher = new JwtCredentialsMatcher();
        jwtRealm.setCredentialsMatcher(matcher);
        // 开启缓存
        jwtRealm.setCacheManager(ehCacheManager);
        jwtRealm.setCachingEnabled(true);
        jwtRealm.setAuthenticationCachingEnabled(true);
        jwtRealm.setAuthenticationCacheName("jwtAuthenticationCacheName");

        jwtRealm.setAuthorizationCachingEnabled(true);
        jwtRealm.setAuthorizationCacheName("jwtAuthorizationCacheName");

        return jwtRealm;
    }


    /**
     * 多realm 认证策略
     */
    @Bean
    public ModularRealmAuthenticator authenticator() {
        ModularRealmAuthenticator authenticator = new ModularRealmAuthenticator();
        // 设置多Realm 认证策略,有一个成功就返回;
        AuthenticationStrategy strategy = new FirstSuccessfulStrategy();
        authenticator.setAuthenticationStrategy(strategy);
        return authenticator;
    }

    @Bean
    public JwtFilter jwtFilter() {
        return new JwtFilter();
    }

    @Bean
    public EhCacheManager ehCacheManager() {
        return new EhCacheManager();
    }
}
