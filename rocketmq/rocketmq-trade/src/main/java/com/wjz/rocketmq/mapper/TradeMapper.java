package com.wjz.rocketmq.mapper;

import com.wjz.rocketmq.domain.Trade;

public interface TradeMapper {

	void insert(Trade trade);
	
	int isInserted(String gtxId);
	
	void insertLog(String gtxId);
}
