package com.wjz.hmily.service;

import org.dromara.hmily.annotation.Hmily;
import org.dromara.hmily.core.concurrent.threadlocal.HmilyTransactionContextLocal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.wjz.hmily.domain.Finance;
import com.wjz.hmily.mapper.FinanceMapper;

/**
 * 
 * <p>
 * Try {
 * </p>
 * <p>
 * 幂等校验
 * </p>
 * <p>
 * 悬挂校验
 * </p>
 * <p>
 * 插入融资订单
 * </p>
 * <p>
 * 插入try日志
 * </p>
 * <p>
 * 插入交易订单
 * </p>
 * <p>
 * }
 * </p>
 * 
 * <p>
 * Cancel {
 * </p>
 * <p>
 * 幂等校验
 * </p>
 * <p>
 * 空回滚校验
 * </p>
 * <p>
 * 删除融资订单
 * </p>
 * <p>
 * 插入cancel日志
 * </p>
 * <p>
 * }
 * </p>
 * 
 * @author iss002
 *
 */
@Service
public class FinanceServiceImpl implements FinanceService {

	private static final Logger logger = LoggerFactory.getLogger(FinanceServiceImpl.class);

	@Autowired
	private FinanceMapper mapper;
	@Reference
	private TradeService tradeService;

	/**
	 * 插入融资订单同时插入交易订单<br>
	 * Hmily-TCC的try方法<br>
	 * Try幂等校验<br>
	 * Try悬挂校验<br>
	 * 插入融资订单<br>
	 * 插入try日志<br>
	 * 插入交易订单<br>
	 */
	@Override
	@Transactional
	@Hmily(confirmMethod = "commit", cancelMethod = "rollback")
	public void insert(String name) {
		// 获取全局事务id
		String gtxId = HmilyTransactionContextLocal.getInstance().get().getTransId();
		// ==> Try幂等校验：检查Try是否执行过即查询try_log表是否存在数据，已执行try则不能执行
		if (mapper.isTried(gtxId) > 0) {
			logger.info("finance insert try 已经执行过了，gtxId[{}]", gtxId);
			return;
		}
		// ==>
		// Try悬挂校验：检查Cancel、Confirm是否执行过即查询cancel_log、confirm_log表是否存在数据，已执行cancel、confirm则不能执行
		if (mapper.isCanceled(gtxId) > 0 || mapper.isConfirmed(gtxId) > 0) {
			logger.info("finance insert cancel or confirm 已经执行过了，gtxId[{}]", gtxId);
			return;
		}
		// ==> 插入融资订单：假设需要校验融资订单是否已经存在，存在则不插入
		try {
			Finance finance = new Finance();
			finance.setName(name);
			mapper.insert(finance);
			// int i = 1 / 0;
		} catch (Exception e) {
			throw new RuntimeException("finance insert 时异常，gtxId[" + gtxId + "]", e);
		}
		// ==> 插入try日志
		mapper.insertTryLog(gtxId);
		// ==> 插入交易订单
		try {
			tradeService.insert(name);
		} catch (Exception e) {
			throw new RuntimeException("finance insert 调用 trade insert 时异常，gtxId[" + gtxId + "]", e);
		}
	}

	/**
	 * Hmily-TCC的confirm方法
	 */
	public void commit(String name) {

	}

	/**
	 * Hmily-TCC的cancel方法<br>
	 * 幂等校验<br>
	 * 空回滚校验<br>
	 * 删除融资订单<br>
	 */
	@Transactional
	public void rollback(String name) {
		// 获取全局事务id
		String gtxId = HmilyTransactionContextLocal.getInstance().get().getTransId();
		// ==> Cancel幂等校验：检查Cancel是否执行过即查询cancel_log表是否存在数据，已执行cancel则不能执行
		if (mapper.isCanceled(gtxId) > 0) {
			logger.info("finance insert cancel 已经执行过了，gtxId[{}]", gtxId);
			return;
		}
		// ==> 空回滚校验：检查Try是否已经执行过即查询try_log表是否存在数据，未执行try则不能执行cancel
		if (mapper.isTried(gtxId) <= 0) {
			logger.info("finance insert try 未执行 cancel 不能执行，gtxId[{}]", gtxId);
			return;
		}
		// ==> 删除融资订单
		Finance finance = new Finance();
		finance.setName(name);
		mapper.delete(finance);
		// ==> 插入cancel日志
		mapper.insertCancelLog(gtxId);
	}
}
