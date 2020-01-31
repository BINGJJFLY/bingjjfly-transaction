package com.wjz.notify.mapper;

import com.wjz.notify.domain.Finance;

public interface FinanceMapper {

	void insert(Finance finance);
	
	int isInserted(String gtxId);
	
	void insertLog(String gtxId);
}
