package com.wjz.seata.service;

import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;

import io.seata.spring.annotation.GlobalTransactional;

@Service
public class TxServiceImpl implements TxService {
	
	@Reference
	private FinanceService financeService;
	@Reference
	private TradeService tradeService;

	@Override
	@GlobalTransactional(timeoutMills = 300000, name = "tx-gts-seata")
	public void insert(String name) {
		financeService.insert(name);
		tradeService.insert(name);
	}
}
