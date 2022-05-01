package edu.dlu.bysj.base.config;

import com.google.common.base.Predicates;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

/**
 * @author XiangXinGang
 * @date 2021/10/6 16:37
 */
@EnableSwagger2
@Configuration
public class SwaggerConfig {
    @Bean
    public Docket defenseConfig() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("defense")
                .apiInfo(defenseInfo())
                .select()
                .paths(Predicates.and(PathSelectors.regex("/defenseManagement/.*")))
                .build()
                .globalOperationParameters(setHeaderJwt());

    }

    @Bean
    public Docket gradeConfig() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("grade")
                .apiInfo(gradeInfo())
                .select()
                .paths(Predicates.and(PathSelectors.regex("/gradeManage/.*")))
                .build()
                .globalOperationParameters(setHeaderJwt());
    }

    @Bean
    public Docket notificationConfig() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("notification")
                .apiInfo(notificationInfo())
                .select()
                .paths(Predicates.and(PathSelectors.regex("/notificationManagement/.*")))
                .build()
                .globalOperationParameters(setHeaderJwt());
    }

    @Bean
    public Docket paperConfig() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("paper")
                .apiInfo(paperInfo())
                .select()
                .paths(Predicates.and(PathSelectors.regex("/paperManagement/.*")))
                .build()
                .globalOperationParameters(setHeaderJwt());
    }

    @Bean
    public Docket systemConfig() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("system")
                .apiInfo(systemInfo())
                .select()
                .paths(Predicates.and(PathSelectors.regex("/systemManagement/.*")))
                .build()
                .globalOperationParameters(setHeaderJwt());
    }

    @Bean
    public Docket commonConfig() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("common")
                .apiInfo(commonInfo())
                .select()
                .paths(Predicates.and(PathSelectors.regex("/common/.*")))
                .build()
                .globalOperationParameters(setHeaderJwt());
    }

    private ApiInfo defenseInfo() {
        return new ApiInfoBuilder()
                .title("答辩模块接口")
                .description("答辩相关接口的文档描述")
                .version("1.0")
                .build();
    }

    private ApiInfo gradeInfo() {
        return new ApiInfoBuilder()
                .title("成绩模块接口")
                .description("成绩相关接口的文档描述")
                .version("1.0")
                .build();
    }

    private ApiInfo notificationInfo() {
        return new ApiInfoBuilder()
                .title("通知模块接口")
                .description("通知相关接口的api文档描述")
                .version("1.0")
                .build();
    }

    private ApiInfo paperInfo() {
        return new ApiInfoBuilder()
                .title("论文模块接口")
                .description("论文相关的接口api文档描述")
                .version("1.0")
                .build();
    }

    private ApiInfo systemInfo() {
        return new ApiInfoBuilder()
                .title("系统模块接口")
                .description("系统相关的接口api文档描述")
                .version("1.0")
                .build();
    }

    private ApiInfo commonInfo() {
        return new ApiInfoBuilder()
                .title("通用模块接口")
                .description("通用模块相关接口api文档描述")
                .version("1.0")
                .build();
    }

    private List<Parameter> setHeaderJwt() {
        ParameterBuilder builder = new ParameterBuilder();
        List<Parameter> parameters = new ArrayList<>();
        builder.name("JWT")
                .description("用户认证JWT")
                .modelRef(new ModelRef("string"))
                .parameterType("header")
                .required(false)
                .build();
        parameters.add(builder.build());
        return parameters;
    }
}

