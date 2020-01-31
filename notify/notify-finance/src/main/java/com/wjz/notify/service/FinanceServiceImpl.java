package com.wjz.notify.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.wjz.notify.domain.Finance;
import com.wjz.notify.mapper.FinanceMapper;

@Service
public class FinanceServiceImpl implements FinanceService {

	@Autowired
	private FinanceMapper mapper;
	@Reference
	private TradeService tradeService;

	/**
	 * 实现幂等性
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
		// ==> 插入业务日志
		mapper.insertLog(gtxId);
	}

	/**
	 * 消息重复通知无效时，调用该方法进行人为通知
	 */
	@Override
	public void tradeIsInserted(String gtxId) {
		if (tradeService.isInserted(gtxId) > 0) {
			insert("iss002", gtxId);
		}
	}
}
