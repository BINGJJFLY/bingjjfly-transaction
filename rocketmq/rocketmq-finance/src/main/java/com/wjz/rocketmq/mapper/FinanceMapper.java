package com.wjz.rocketmq.mapper;

import com.wjz.rocketmq.domain.Finance;

public interface FinanceMapper {

	void insert(Finance finance);
	
	int isInserted(String gtxId);
	
	void insertLog(String gtxId);
}
