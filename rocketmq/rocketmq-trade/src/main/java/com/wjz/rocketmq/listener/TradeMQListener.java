package com.wjz.rocketmq.listener;

import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.wjz.rocketmq.service.TradeService;

@Component
@RocketMQMessageListener(consumerGroup = "trade-consumer-group", topic = "finance-destination")
public class TradeMQListener implements RocketMQListener<String> {

	private static final Logger logger = LoggerFactory.getLogger(TradeMQListener.class);

	@Autowired
	private TradeService tradeService;

	/**
	 * 监听消息
	 */
	@Override
	@Transactional
	public void onMessage(String message) {
		logger.info("消费者监听消息，{}", message);
		JSONObject parse = (JSONObject) JSONObject.parse(message);
		tradeService.insert((String) parse.get("name"), (String) parse.get("gtxId"));
	}
}
