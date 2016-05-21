package com.example.teachcooking.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ResponseClass implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// 请求码
	private String resultcode;
	// 查询结果
	private String reason;
	// 结果集
	private List<Restaurant> result = new ArrayList<Restaurant>();


	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public List<Restaurant> getResult() {
		return result;
	}

	public void setResult(List<Restaurant> result) {
		this.result = result;
	}

	public String getResultcode() {
		return resultcode;
	}

	public void setResultcode(String resultcode) {
		this.resultcode = resultcode;
	}
}
