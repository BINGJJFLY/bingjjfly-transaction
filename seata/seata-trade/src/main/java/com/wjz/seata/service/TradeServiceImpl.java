package com.wjz.seata.service;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.wjz.seata.domain.Trade;
import com.wjz.seata.mapper.TradeMapper;

@Service
public class TradeServiceImpl implements TradeService {
	
	@Autowired
	private TradeMapper mapper;

	@Override
	public void insert(String name) {
		Trade trade = new Trade();
		trade.setName(name);
		mapper.insert(trade);
//		int i = 1 / 0;
	}
}
