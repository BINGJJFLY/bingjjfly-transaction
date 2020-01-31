package com.wjz.notify.mapper;

import com.wjz.notify.domain.Trade;

public interface TradeMapper {

	void insert(Trade trade);
	
	int isInserted(String gtxId);
	
	void insertLog(String gtxId);

	Trade selectByName(String name);
}
