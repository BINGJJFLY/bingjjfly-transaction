package com.wjz.hmily;

import org.dromara.hmily.common.config.HmilyDbConfig;
import org.dromara.hmily.core.bootstrap.HmilyTransactionBootstrap;
import org.dromara.hmily.core.service.HmilyInitService;
import org.dromara.hmily.spring.boot.starter.parent.configuration.HmilyAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Primary;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;

@EnableDubbo
@SpringBootApplication(exclude=HmilyAutoConfiguration.class)
@EnableAspectJAutoProxy
@EnableTransactionManagement
@ComponentScan({"com.wjz", "org.dromara.hmily"})
public class HmilyFinanceApplication {

	public static void main(String[] args) {
		SpringApplication.run(HmilyFinanceApplication.class, args);
	}
	
	@Bean
	@Primary
    public HmilyTransactionBootstrap hmilyTransactionBootstrap(HmilyInitService hmilyInitService) {
        final HmilyTransactionBootstrap hmilyTransactionBootstrap = new HmilyTransactionBootstrap(hmilyInitService);
        hmilyTransactionBootstrap.setRetryMax(30);
        hmilyTransactionBootstrap.setRecoverDelayTime(128);
        hmilyTransactionBootstrap.setRepositorySupport("db");
        hmilyTransactionBootstrap.setScheduledDelay(128);
        hmilyTransactionBootstrap.setScheduledThreadMax(10);
        hmilyTransactionBootstrap.setSerializer("kryo");
        hmilyTransactionBootstrap.setStarted(true);
        HmilyDbConfig config = new HmilyDbConfig();
        config.setDriverClassName("com.mysql.jdbc.Driver");
        config.setUrl("jdbc:mysql://192.168.21.134:3306/hmily");
        config.setUsername("root");
        config.setPassword("123456a");
        hmilyTransactionBootstrap.setHmilyDbConfig(config);
        hmilyTransactionBootstrap.setAsyncThreads(200);
        return hmilyTransactionBootstrap;
    }
}
