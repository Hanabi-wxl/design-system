package edu.dlu.bysj.base.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.autoconfigure.ConfigurationCustomizer;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author XiangXinGang
 * @date 2021/10/10 17:03
 */
@Configuration
@MapperScan(
        basePackages = {
                "edu.dlu.bysj.common.mapper",
                "edu.dlu.bysj.security.mapper",
                "edu.dlu.bysj.system.mapper",
                "edu.dlu.bysj.log.mapper",
                "edu.dlu.bysj.paper.mapper",
                "edu.dlu.bysj.defense.mapper",
                "edu.dlu.bysj.grade.mapper",
                "edu.dlu.bysj.notification.mapper",
                "edu.dlu.bysj.document.mapper"
        })
public class MapperConfig {
    /**
     * 注入分页插件
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }

    @Bean
    public ConfigurationCustomizer configurationCustomizer() {
        return configuration -> configuration.setUseDeprecatedExecutor(false);
    }
}
