package com.wjz.rocketmq.service;

/**
 * 插入融资订单<br>
 * 发送消息
 * 
 * @author iss002
 *
 */
public interface FinanceService {
	
	void insert(String name, String gtxId);
	
	void sendMsg(String name, String gtxId);
}
