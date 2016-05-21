package com.example.teachcooking.entity;

import java.io.Serializable;

/**
 * ���˲���
 * @author Li
 *
 */
public class CookStep implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String img;
	private String step;
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}
	public String getStep() {
		return step;
	}
	public void setStep(String step) {
		this.step = step;
	}
	public CookStep(String img, String step) {
		super();
		this.img = img;
		this.step = step;
	}
	public CookStep() {
		super();
		// TODO Auto-generated constructor stub
	}
	
}
