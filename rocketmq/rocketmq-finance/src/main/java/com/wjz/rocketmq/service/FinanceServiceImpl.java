package com.wjz.rocketmq.service;

import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSONObject;
import com.wjz.rocketmq.domain.Finance;
import com.wjz.rocketmq.mapper.FinanceMapper;

@Service
public class FinanceServiceImpl implements FinanceService {

	@Autowired
	private FinanceMapper mapper;
	@Autowired
	private RocketMQTemplate rocketMQTemplate;

	/**
	 * 事务参与方方法实现幂等性
	 */
	@Override
	@Transactional
	public void insert(String name, String gtxId) {
		// ==> 检查幂等性
		if (mapper.isInserted(gtxId) > 0) {
			return;
		}
		// ==> 插入融资订单
		Finance finance = new Finance();
		finance.setName(name);
		mapper.insert(finance);
//		int i = 1 / 0;
		// ==> 插入业务日志
		mapper.insertLog(gtxId);
	}

	/**
	 * RocketMQ发送消息
	 */
	@Override
	public void sendMsg(String name, String gtxId) {
		// 事务生产者组
		String txProducerGroup = "finance-poducer-group";
		// 目的地-主题名称
		String destination = "finance-destination";
		// 消息
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("name", name);
		jsonObject.put("gtxId", gtxId);
		Message<String> message = MessageBuilder.withPayload(jsonObject.toJSONString()).build();
		// 参数
		Object arg = null;
		rocketMQTemplate.sendMessageInTransaction(txProducerGroup, destination, message, arg);
	}
}
