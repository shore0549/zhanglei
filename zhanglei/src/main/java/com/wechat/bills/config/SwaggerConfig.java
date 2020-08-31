
package com.wechat.bills.config;
//swagger2的配置文件，在项目的启动类的同级文件建立

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

/**
 *  * @ClassName Swagger2
 *  * @Description TODO(这里用一句话描述这个类的作用)
 *  * @Author elephone
 *  * @Date 2019年03月16日 10:20
 *  * @Version 1.0.0
 *  
 **/
@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket createRestApi() {
        ParameterBuilder aParameterBuilder = new ParameterBuilder();
        aParameterBuilder.name("Referer").description("Origin").modelRef(new ModelRef("string")).parameterType("header").required(false).build();
        List<Parameter> aParameters = new ArrayList<Parameter>();
        aParameters.add(aParameterBuilder.build());
        System.out.println("----------------SwaggerConfig被加载-----------------");
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                //.useDefaultResponseMessages(false)
                //.globalOperationParameters(aParameters)
                .select()
                //为当前包路径
                .apis(RequestHandlerSelectors.basePackage("com.wechat.bills.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {

        System.out.println("----------------SwaggerConfig标题加载------------");
        return new ApiInfoBuilder()
                //页面标题
                .title("张雷获取处理平台--基于RESTful风格API接口文档")
                //描述
                .description("张雷获取后台[游戏平台接口]")
                //创建人
                .contact(new Contact("Elephone", "", ""))
                //版本号
                .version("1.0")
                .build();
    }
}
