package com.example.teachcooking.entity;

import java.io.Serializable;
import java.util.List;
/**
 * �������ǩ�б�
 * @author Li
 *
 */
public class ResponseClassifys implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int resultcode;
	private String reason;
	private List<Classify> result;
	
	public int getResultcode() {
		return resultcode;
	}

	public void setResultcode(int resultcode) {
		this.resultcode = resultcode;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public List<Classify> getResult() {
		return result;
	}

	public void setResult(List<Classify> result) {
		this.result = result;
	}

	/**
	 * ����
	 * @author Li
	 *
	 */
	public class Classify implements Serializable{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private String parentId;
		private String name;
		private List<Cuisine> list;
		public String getParentId() {
			return parentId;
		}
		public void setParentId(String parentId) {
			this.parentId = parentId;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public List<Cuisine> getList() {
			return list;
		}
		public void setList(List<Cuisine> list) {
			this.list = list;
		}
	}
	
	/**
	 * ��ϵ
	 * @author Li
	 *
	 */
	public class Cuisine implements Serializable{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private String id;
		private String name;
		private String parentId;
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getParentId() {
			return parentId;
		}
		public void setParentId(String parentId) {
			this.parentId = parentId;
		}
		
	}
}
