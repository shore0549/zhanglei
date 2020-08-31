package com.wechat.bills;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportResource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;


@SpringBootApplication(scanBasePackages = {"com.wechat.bills", "com.wechat.bills"})
//@ImportResource(locations={"classpath:application-tast.xml"})
//@MapperScan("com.young.mapper")//必须加这个，不加报错，如果不加，也可以在每个mapper上添加@Mapper注释
//@EnableScheduling
public class BillsApplication {

    public static void main(String[] args) {
        SpringApplication.run(BillsApplication.class, args);
    }


    //解决跨域问题
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurerAdapter() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedOrigins("*");
            }
        };
    }

}
