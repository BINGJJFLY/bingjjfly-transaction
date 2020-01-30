package com.wjz.hmily;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.wjz.hmily.domain.Finance;
import com.wjz.hmily.mapper.FinanceMapper;
import com.wjz.hmily.service.FinanceService;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class LogTest {

	@Autowired
	private FinanceMapper financeMapper;
	@Autowired
	private FinanceService financeService;
	
	@Test
	public void tx() {
		financeService.insert("test");
	}
	
	@Test
	public void insert() {
		Finance finance = new Finance();
		finance.setName("test");
		financeMapper.insert(finance);
	}
	
	@Test
	public void delete() {
		Finance finance = new Finance();
		finance.setName("test");
		financeMapper.delete(finance);
	}

	@Test
	public void insertTryLog() {
		financeMapper.insertTryLog("test");
	}

	@Test
	public void insertConfirmLog() {
		financeMapper.insertConfirmLog("test");
	}

	@Test
	public void insertCancelLog() {
		financeMapper.insertCancelLog("test");
	}

	@Test
	public void isTried() {
		int i = financeMapper.isTried("test");
		Assert.assertEquals(1, i);
	}

	@Test
	public void isConfirmed() {
		int i = financeMapper.isConfirmed("test");
		Assert.assertEquals(1, i);
	}

	@Test
	public void isCanceled() {
		int i = financeMapper.isCanceled("test");
		Assert.assertEquals(1, i);
	}

}
