package com.wjz.rocketmq;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;

/**
 * RocketMQ启动方式：
 * 	启动nameserver：D:\rocketmq-all-4.5.0-bin-release\bin\mqnamesrv.cmd
 * 	启动broker：D:\rocketmq-all-4.5.0-bin-release\bin\mqbroker.cmd -n 127.0.0.1:9876 autoCreateTopicEnable=true
 * 
 * @author iss002
 *
 */
@EnableDubbo
@SpringBootApplication
public class RocketmqTxApplication {

	public static void main(String[] args) {
		SpringApplication.run(RocketmqTxApplication.class, args);
	}
}
