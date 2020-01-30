package com.wjz.hmily.mapper;

import com.wjz.hmily.domain.Finance;

public interface FinanceMapper {

	void insert(Finance finance);
	
	void delete(Finance finance);

	void insertTryLog(String gtxId);

	void insertConfirmLog(String gtxId);

	void insertCancelLog(String gtxId);
	
	int isTried(String gtxId);

	int isConfirmed(String gtxId);

	int isCanceled(String gtxId);
}
