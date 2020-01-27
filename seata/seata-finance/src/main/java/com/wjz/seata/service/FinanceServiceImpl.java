package com.wjz.seata.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Service;
import com.wjz.seata.domain.Finance;
import com.wjz.seata.mapper.FinanceMapper;

@Service
public class FinanceServiceImpl implements FinanceService {

	@Autowired
	private FinanceMapper mapper;

	@Override
	@Transactional
	public void insert(String name) {
		Finance finance = new Finance();
		finance.setName(name);
		mapper.insert(finance);
//		int i = 1 / 0;
	}
}
