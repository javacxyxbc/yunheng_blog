package com.moxi.hyblog.sms;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
//@EnableEurekaServer
@EnableDiscoveryClient
@EnableRabbit
@EnableFeignClients("com.moxi.hyblog.sms.feign")
@ComponentScan(basePackages = {
        "com.moxi.hyblog.utils",
        "com.moxi.hyblog.sms.feign",
        "com.moxi.hyblog.sms.listener",
        "com.moxi.hyblog.sms.config",
        "com.moxi.hyblog.sms.util",
})
public class SmsApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmsApplication.class, args);
    }
}
