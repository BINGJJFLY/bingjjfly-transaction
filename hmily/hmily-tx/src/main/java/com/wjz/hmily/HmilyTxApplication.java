package com.wjz.hmily;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;

@EnableDubbo
@SpringBootApplication
public class HmilyTxApplication {

	public static void main(String[] args) {
		SpringApplication.run(HmilyTxApplication.class, args);
	}
}
