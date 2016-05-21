package com.example.teachcooking.entity;

import java.util.ArrayList;

public class Province {
	private String province;// 省份
	private ArrayList<String> citys;// 城市

	public Province(String provice, ArrayList<String> city) {
		this.province = provice;
		this.citys = city;
	}

	public String getProvice() {
		return province;
	}

	public void setProvice(String provice) {
		this.province = provice;
	}

	public ArrayList<String> getCity() {
		return citys;
	}

	public void setCity(ArrayList<String> city) {
		this.citys = city;
	}
}
