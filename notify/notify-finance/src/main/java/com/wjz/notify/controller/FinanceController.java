package com.wjz.notify.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wjz.notify.service.FinanceService;

@RestController
public class FinanceController {

	@Autowired
	private FinanceService financeService;
	
	@GetMapping("/tradeIsInserted")
	public String tradeIsInserted(String gtxId) {
		financeService.tradeIsInserted(gtxId);
		return "success.";
	}
}
