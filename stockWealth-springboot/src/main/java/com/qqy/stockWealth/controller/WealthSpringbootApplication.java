package com.qqy.stockWealth.controller;

import com.qqy.stockWealth.service.shareHold.IShareHoldService;
import com.qqy.stockWealth.service.shareHold.impl.ShareHoldServiceImpl;
import com.qqy.stockWealth.util.SpringUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jms.activemq.ActiveMQAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@ComponentScan("com.qqy.stockWealth")
@PropertySource(value = {"classpath:datasource.properties"})


public class WealthSpringbootApplication {

    public static void main(String[] args) throws Exception{
        SpringApplication.run(WealthSpringbootApplication.class,args);

        ApplicationContext context = SpringUtil.getApplicationContext();
        IShareHoldService shareHoldService = context.getBean(ShareHoldServiceImpl.class);
        shareHoldService.runStock();
    }

}
