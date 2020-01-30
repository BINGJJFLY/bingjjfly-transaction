package com.wjz.hmily.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wjz.hmily.service.TxService;

@RestController
public class TxController {
	
	@Autowired
	private TxService txService;

	@RequestMapping("/index")
	public String index() {
		txService.insert("iss002");
		return "success.";
	}
}
