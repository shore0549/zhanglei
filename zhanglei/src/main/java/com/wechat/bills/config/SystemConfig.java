package com.wechat.bills.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;

import java.io.InputStream;


@Configuration
@ComponentScan("com.wechat.bills.config")
@PropertySource("classpath:env-config/configs/myConfig.properties")
public class SystemConfig {
    @Value("${downloadPath}")
    public String downloadPath;


    @Value("classpath:env-config/configs/test.txt")
    private Resource testFile;

    @Value("http://www.baidu.com")
    private Resource testUrl;


    @Autowired
    private Environment environment;


    public void outSource() {
        System.out.println(downloadPath);
        try {
            InputStream inputStream = testFile.getInputStream();
            InputStream inputStream1 = testUrl.getInputStream();
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println(environment.getProperty("file.fileName"));
    }


}
