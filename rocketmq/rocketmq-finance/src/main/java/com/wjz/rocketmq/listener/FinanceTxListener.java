package com.wjz.rocketmq.listener;

import org.apache.rocketmq.spring.annotation.RocketMQTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.wjz.rocketmq.mapper.FinanceMapper;
import com.wjz.rocketmq.service.FinanceService;

@Component
@RocketMQTransactionListener(txProducerGroup = "finance-poducer-group")
public class FinanceTxListener implements RocketMQLocalTransactionListener {

	private static final Logger logger = LoggerFactory.getLogger(FinanceTxListener.class);

	@Autowired
	private FinanceService financeService;
	@Autowired
	private FinanceMapper financeMapper;

	/**
	 * 消息发送到RocketMQ成功后执行本地事务方法
	 */
	@Override
	@Transactional
	public RocketMQLocalTransactionState executeLocalTransaction(Message msg, Object arg) {
		logger.info("消息发送到RocketMQ成功后执行本地事务方法，消息{}", msg);
		try {
			// 解析消息
			JSONObject parse = (JSONObject) JSONObject.parse(new String((byte[]) msg.getPayload()));
			// 执行本地事务
			financeService.insert((String) parse.get("name"), (String) parse.get("gtxId"));
			return RocketMQLocalTransactionState.COMMIT;
		} catch (Exception e) {
			return RocketMQLocalTransactionState.ROLLBACK;
		}
	}

	/**
	 * 事务回查检查本地事务是否执行成功
	 */
	@Override
	public RocketMQLocalTransactionState checkLocalTransaction(Message msg) {
		logger.info("事务回查检查本地事务是否执行成功，消息{}", msg);
		JSONObject parse = (JSONObject) JSONObject.parse(new String((byte[]) msg.getPayload()));
		if (financeMapper.isInserted((String) parse.get("gtxId")) > 0) {
			return RocketMQLocalTransactionState.COMMIT;
		}
		return RocketMQLocalTransactionState.UNKNOWN;
	}
}
