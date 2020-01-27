package com.wjz.seata.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.seata.spring.annotation.GlobalTransactionScanner;

@Configuration
public class SeataAutoConfig {

	@Bean
    public GlobalTransactionScanner globalTransactionScanner(){
        return new GlobalTransactionScanner("tx-gts-seata", "my_test_tx_group");
    }
}
