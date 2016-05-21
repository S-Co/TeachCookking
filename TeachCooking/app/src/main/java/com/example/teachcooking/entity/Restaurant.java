package com.example.teachcooking.entity;

import java.io.Serializable;

/**
 * 餐厅类
 * */
public class Restaurant implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name; // 店名
	private String navigation; // 地址位置
	private String area; // 所属区域
	private String address; // 详细地址
	private String phone; // 电话
	private float latitude; // 坐标经度
	private float longitude; // 坐标纬度
	private float stars; // 评星
	private String avg_price;// 人均消费
	private String photos; // 图片URL
	private String tags; // 标签
	private String city; // 城市
	private String recommended_dishes; // 推荐菜品
	private String product_rating; //产品评分
	private String environment_rating; // 环境评分
	private String service_rating; //服务评分
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNavigation() {
		return navigation;
	}

	public void setNavigation(String navigation) {
		this.navigation = navigation;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAvg_price() {
		return avg_price;
	}

	public void setAvg_price(String avg_price) {
		this.avg_price = avg_price;
	}

	public String getPhotos() {
		return photos;
	}

	public void setPhotos(String photos) {
		this.photos = photos;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public String getRecommended_dishes() {
		return recommended_dishes;
	}

	public void setRecommended_dishes(String recommended_dishes) {
		this.recommended_dishes = recommended_dishes;
	}

	public float getLongitude() {
		return longitude;
	}

	public void setLongitude(float longitude) {
		this.longitude = longitude;
	}

	public float getLatitude() {
		return latitude;
	}

	public void setLatitude(float latitude) {
		this.latitude = latitude;
	}

	public float getStars() {
		return stars;
	}

	public void setStars(float stars) {
		this.stars = stars;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getProduct_rating() {
		return product_rating;
	}

	public void setProduct_rating(String product_rating) {
		this.product_rating = product_rating;
	}

	public String getEnvironment_rating() {
		return environment_rating;
	}

	public void setEnvironment_rating(String environment_rating) {
		this.environment_rating = environment_rating;
	}

	public String getService_rating() {
		return service_rating;
	}

	public void setService_rating(String service_rating) {
		this.service_rating = service_rating;
	}

}
