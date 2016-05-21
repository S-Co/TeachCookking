package com.example.teachcooking.entity;

import cn.bmob.v3.BmobObject;

public class BasketInfo extends BmobObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// 用户
	private UsuerInfo user;
	private CookBookInfo cookBookInfo;
	private UsuerInfo author;
	
	public CookBookInfo getCookBookInfo() {
		return cookBookInfo;
	}

	public void setCookBookInfo(CookBookInfo cookBookInfo) {
		this.cookBookInfo = cookBookInfo;
	}

	public UsuerInfo getUser() {
		return user;
	}

	public void setUser(UsuerInfo user) {
		this.user = user;
	}

	public UsuerInfo getAuthor() {
		return author;
	}

	public void setAuthor(UsuerInfo author) {
		this.author = author;
	}

}
