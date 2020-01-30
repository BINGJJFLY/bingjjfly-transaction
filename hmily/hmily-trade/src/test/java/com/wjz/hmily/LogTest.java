package com.wjz.hmily;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.wjz.hmily.domain.Trade;
import com.wjz.hmily.mapper.TradeMapper;
import com.wjz.hmily.service.TradeService;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class LogTest extends AbstractJUnit4SpringContextTests {

	@Autowired
	private TradeMapper tradeMapper;
	@Autowired
    protected ApplicationContext ctx;
	@Autowired
	private TradeService tradeService;
	
	@Test
	public void beans() {
		String[] beanDefinitionNames = ctx.getBeanDefinitionNames();
		for (String beanName : beanDefinitionNames) {
			if ("tradeServiceImpl".equals(beanName)) {
				Object bean = ctx.getBean(beanName);
				System.out.println("bean["+beanName+"],bean["+bean+"]");
			}
			if ("hmilyTransactionAspectService".equals(beanName)) {
				System.out.println();
			}
		}
	}
	
	@Test
	public void t() {
		tradeService.insert("test");
	}
	
	@Test
	public void insert() {
		Trade trade = new Trade();
		trade.setName("test");
		tradeMapper.insert(trade);
	}
	
	@Test
	public void delete() {
		Trade trade = new Trade();
		trade.setName("test");
		tradeMapper.delete(trade);
	}

	@Test
	public void insertTryLog() {
		tradeMapper.insertTryLog("test");
	}

	@Test
	public void insertConfirmLog() {
		tradeMapper.insertConfirmLog("test");
	}

	@Test
	public void insertCancelLog() {
		tradeMapper.insertCancelLog("test");
	}

	@Test
	public void isTried() {
		int i = tradeMapper.isTried("test");
		Assert.assertEquals(1, i);
	}

	@Test
	public void isConfirmed() {
		int i = tradeMapper.isConfirmed("test");
		Assert.assertEquals(1, i);
	}

	@Test
	public void isCanceled() {
		int i = tradeMapper.isCanceled("test");
		Assert.assertEquals(1, i);
	}

}
