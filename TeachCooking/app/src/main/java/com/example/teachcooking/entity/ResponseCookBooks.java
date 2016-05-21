package com.example.teachcooking.entity;

import java.io.Serializable;
import java.util.List;

public class ResponseCookBooks implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String resultcode;
	private String reason;
	private CookBooks result;
	private int error_code;
	public String getResultcode() {
		return resultcode;
	}
	public void setResultcode(String resultcode) {
		this.resultcode = resultcode;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public CookBooks getResult() {
		return result;
	}
	public void setResult(CookBooks result) {
		this.result = result;
	}
	public int getError_code() {
		return error_code;
	}
	public void setError_code(int error_code) {
		this.error_code = error_code;
	}

	public class CookBooks implements Serializable{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private List<CookBook> data;
		private String totalNum;
		private String pn;
		private String rn;
		public List<CookBook> getData() {
			return data;
		}
		public void setData(List<CookBook> data) {
			this.data = data;
		}
		public String getTotalNum() {
			return totalNum;
		}
		public void setTotalNum(String totalNum) {
			this.totalNum = totalNum;
		}
		public String getPn() {
			return pn;
		}
		public void setPn(String pn) {
			this.pn = pn;
		}
		public String getRn() {
			return rn;
		}
		public void setRn(String rn) {
			this.rn = rn;
		}
		
	}
}
