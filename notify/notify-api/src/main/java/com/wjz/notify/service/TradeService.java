package com.wjz.notify.service;

public interface TradeService {
	
	void insert(String name, String gtxId);
	
	int isInserted(String gtxId);
}
