package com.wjz.notify.listener;

import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.wjz.notify.service.FinanceService;

@Component
@RocketMQMessageListener(consumerGroup = "finance-consumer-group", topic = "trade-destination")
public class FinanceMQListener implements RocketMQListener<String> {

	private static final Logger logger = LoggerFactory.getLogger(FinanceMQListener.class);

	@Autowired
	private FinanceService financeService;
	
	/**
	 * 监听消息，插入融资订单
	 */
	@Override
	public void onMessage(String message) {
		logger.info("消费者监听消息，{}", message);
		JSONObject parse = (JSONObject) JSONObject.parse(message);
		financeService.insert((String) parse.get("name"), (String) parse.get("gtxId"));
	}
}
