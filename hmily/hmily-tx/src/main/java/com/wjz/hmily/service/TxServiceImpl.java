package com.wjz.hmily.service;

import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;

@Service
public class TxServiceImpl implements TxService {
	
	@Reference
	private FinanceService financeService;

	@Override
	public void insert(String name) {
		financeService.insert(name);
	}
}
