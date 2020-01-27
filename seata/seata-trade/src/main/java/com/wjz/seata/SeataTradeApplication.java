package com.wjz.seata;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;

@EnableDubbo
@SpringBootApplication
public class SeataTradeApplication {

	public static void main(String[] args) {
		SpringApplication.run(SeataTradeApplication.class, args);
	}
}
