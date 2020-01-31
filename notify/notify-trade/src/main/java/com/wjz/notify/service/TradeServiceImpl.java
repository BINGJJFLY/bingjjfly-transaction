package com.wjz.notify.service;

import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSONObject;
import com.wjz.notify.domain.Trade;
import com.wjz.notify.mapper.TradeMapper;

@Service
public class TradeServiceImpl implements TradeService {
	
	private static final Logger logger = LoggerFactory.getLogger(TradeServiceImpl.class);

	@Autowired
	private TradeMapper mapper;
	@Autowired
	private RocketMQTemplate rocketMQTemplate;

	/**
	 * 插入交易订单<br>
	 * 发送普通消息
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
		// ==> 插入业务日志
		mapper.insertLog(gtxId);
		// ==> 发送普通消息
		String message;
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("name", name);
		jsonObject.put("gtxId", gtxId);
		rocketMQTemplate.convertAndSend("trade-destination", message = jsonObject.toJSONString());
		logger.info("发送普通消息，{}", message);
	}

	/**
	 * 接收通知方会调用此方法来查询插入交易订单结果
	 */
	@Override
	public int isInserted(String gtxId) {
		return mapper.isInserted(gtxId);
	}
}
