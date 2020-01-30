package com.wjz.hmily.mapper;

import com.wjz.hmily.domain.Trade;

public interface TradeMapper {

	void insert(Trade trade);
	
	void delete(Trade trade);
	
	void insertTryLog(String gtxId);

	void insertConfirmLog(String gtxId);

	void insertCancelLog(String gtxId);
	
	int isTried(String gtxId);

	int isConfirmed(String gtxId);

	int isCanceled(String gtxId);
}
