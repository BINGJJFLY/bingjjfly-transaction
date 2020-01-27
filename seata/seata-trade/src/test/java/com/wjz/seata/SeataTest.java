package com.wjz.seata;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.alibaba.dubbo.config.annotation.Reference;
import com.wjz.seata.service.TradeService;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class SeataTest {

	@Reference
	private TradeService tradeService;

	@Test
	public void insert() {
		tradeService.insert("iss002");
	}
}
