package com.wjz.rocketmq.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;

@Service
public class TxServiceImpl implements TxService {
	
	@Reference
	private FinanceService financeService;

	@Override
	public void insert(String name) {
		financeService.sendMsg(name, UUID.randomUUID().toString());
	}
}
