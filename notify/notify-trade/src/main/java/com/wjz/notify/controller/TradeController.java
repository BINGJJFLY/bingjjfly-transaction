package com.wjz.notify.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wjz.notify.service.TradeService;

@RestController
public class TradeController {

	@Autowired
	private TradeService tradeService;

	@GetMapping("/insert")
	public String insert() {
		String gtxId = UUID.randomUUID().toString();
		tradeService.insert("iss002", gtxId);
		return gtxId;
	}
}
