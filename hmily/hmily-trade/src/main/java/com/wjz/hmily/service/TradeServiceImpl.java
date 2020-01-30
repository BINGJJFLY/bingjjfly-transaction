package com.wjz.hmily.service;

import org.dromara.hmily.annotation.Hmily;
import org.dromara.hmily.core.concurrent.threadlocal.HmilyTransactionContextLocal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Service;
import com.wjz.hmily.domain.Trade;
import com.wjz.hmily.mapper.TradeMapper;

/**
 * 
 * <p>
 * Confirm {
 * </p>
 * <p>
 * 幂等校验
 * </p>
 * <p>
 * 插入交易订单
 * </p>
 * <p>
 * 插入confirm日志
 * </p>
 * <p>
 * }
 * </p>
 * 
 * @author iss002
 *
 */
@Service
public class TradeServiceImpl implements TradeService {

	private static final Logger logger = LoggerFactory.getLogger(TradeServiceImpl.class);

	@Autowired
	private TradeMapper mapper;

	@Override
	@Hmily(confirmMethod = "commit", cancelMethod = "rollback")
	public void insert(String name) {
		// 获取全局事务id
		String gtxId = HmilyTransactionContextLocal.getInstance().get().getTransId();
		System.out.println(gtxId);
	}

	/**
	 * Hmily-TCC的confirm方法<br>
	 * 幂等校验<br>
	 * 插入交易订单<br>
	 * 插入confirm日志
	 * 
	 * @param name
	 */
	@Transactional
	public void commit(String name) {
		// 获取全局事务id
		String gtxId = HmilyTransactionContextLocal.getInstance().get().getTransId();
		// ==> Confirm幂等校验：检查Confirm是否执行过即查询confirm_log表是否存在数据，已执行confirm则不能执行
		if (mapper.isConfirmed(gtxId) > 0) {
			logger.info("trade insert confirm 已经执行过了，gtxId[{}]", gtxId);
			return;
		}
		// ==> 插入交易订单
		Trade trade = new Trade();
		trade.setName(name);
		mapper.insert(trade);
		// int i = 1 / 0;
		// ==> 插入confirm日志
		mapper.insertConfirmLog(gtxId);
	}

	public void rollback(String name) {

	}
}
