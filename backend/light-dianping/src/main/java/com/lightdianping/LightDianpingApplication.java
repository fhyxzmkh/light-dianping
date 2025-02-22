package com.lightdianping;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy(exposeProxy = true)
public class LightDianpingApplication {

    public static void main(String[] args) {
        SpringApplication.run(LightDianpingApplication.class, args);
    }

}
