package com.wjz.rocketmq.domain;

import java.io.Serializable;

public class Trade implements Serializable {

	private static final long serialVersionUID = -7771707551960307114L;
	
	private Integer id;
	private String name;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		return "Finance [id=" + id + ", name=" + name + "]";
	}
}
