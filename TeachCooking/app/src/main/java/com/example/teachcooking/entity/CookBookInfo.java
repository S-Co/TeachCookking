package com.example.teachcooking.entity;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobRelation;

public class CookBookInfo extends BmobObject {
	private BmobRelation likes;//多对多关系,用于存储喜欢该帖子的所有用户
	// 发布者
	private UsuerInfo author;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// 菜谱名
	private String cookBookName;
	// 菜谱简介
	private String cookBookIntroduce;
	// 菜谱用料
	private String cookBookMaterial;
	// 做法
	private ArrayList<String> steps;
	// 菜谱图以及步骤图
	private ArrayList<BmobFile> step_pics;
	// 小贴士
	private String tip;
	private String adrress;

	public String getCookBookName() {
		return cookBookName;
	}

	public void setCookBookName(String cookBookName) {
		this.cookBookName = cookBookName;
	}

	public String getCookBookIntroduce() {
		return cookBookIntroduce;
	}

	public void setCookBookIntroduce(String cookBookIntroduce) {
		this.cookBookIntroduce = cookBookIntroduce;
	}

	public String getCookBookMaterial() {
		return cookBookMaterial;
	}

	public void setCookBookMaterial(String cookBookMaterial) {
		this.cookBookMaterial = cookBookMaterial;
	}

	public void setSteps(ArrayList<String> steps) {
		this.steps = steps;
	}

	public String getTip() {
		return tip;
	}

	public void setTip(String tip) {
		this.tip = tip;
	}

	public List<String> getSteps() {
		return steps;
	}

	public ArrayList<BmobFile> getStep_pics() {
		return step_pics;
	}

	public void setStep_pics(ArrayList<BmobFile> step_pics) {
		this.step_pics = step_pics;
	}

	public UsuerInfo getAuthor() {
		return author;
	}

	public void setAuthor(UsuerInfo author) {
		this.author = author;
	}

	public String getAdrress() {
		return adrress;
	}

	public void setAdrress(String adrress) {
		this.adrress = adrress;
	}

	public BmobRelation getLikes() {
		return likes;
	}

	public void setLikes(BmobRelation likes) {
		this.likes = likes;
	}

}
