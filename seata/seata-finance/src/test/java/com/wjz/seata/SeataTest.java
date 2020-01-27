package com.wjz.seata;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.alibaba.dubbo.config.annotation.Reference;
import com.wjz.seata.service.FinanceService;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class SeataTest {

	@Reference
	private FinanceService financeService;

	@Test
	public void insert() {
		financeService.insert("iss002");
	}
}
