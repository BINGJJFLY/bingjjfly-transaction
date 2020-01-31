package com.wjz.rocketmq.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Service;
import com.wjz.rocketmq.domain.Trade;
import com.wjz.rocketmq.mapper.TradeMapper;

@Service
public class TradeServiceImpl implements TradeService {

	@Autowired
	private TradeMapper mapper;

	/**
	 * 方法实现幂等性防止消息被重复消费
	 */
	@Override
	@Transactional
	public void insert(String name, String gtxId) {
		// ==> 检查幂等性
		if (mapper.isInserted(gtxId) > 0) {
			return;
		}
		// ==> 插入交易订单
		Trade trade = new Trade();
		trade.setName(name);
		mapper.insert(trade);
//		int i = 1 / 0;
		// ==> 插入业务日志
		mapper.insertLog(gtxId);
	}
}
