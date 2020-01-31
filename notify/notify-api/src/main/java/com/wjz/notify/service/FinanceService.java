package com.wjz.notify.service;

/**
 * 插入融资订单<br>
 * 主动查询交易订单是否插入
 * 
 * @author iss002
 *
 */
public interface FinanceService {
	
	void insert(String name, String gtxId);
	
	void tradeIsInserted(String gtxId);
}
