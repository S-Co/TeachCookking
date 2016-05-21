package com.example.teachcooking.entity;

import java.io.Serializable;
import java.util.List;

/**
 * ĳ���˵Ĳ���
 * @author Li
 *
 */
public class CookBook implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	private String title;
	private String tags;
	private String imtro;
	private String ingredients;
	private String burden;
	private List<String> albums;
	private List<CookStep> steps;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getTags() {
		return tags;
	}
	public void setTags(String tags) {
		this.tags = tags;
	}
	public String getImtro() {
		return imtro;
	}
	public void setImtro(String imtro) {
		this.imtro = imtro;
	}
	public String getIngredients() {
		return ingredients;
	}
	public void setIngredients(String ingredients) {
		this.ingredients = ingredients;
	}
	public String getBurden() {
		return burden;
	}
	public void setBurden(String burden) {
		this.burden = burden;
	}
	public List<String> getAlbums() {
		return albums;
	}
	public void setAlbums(List<String> albums) {
		this.albums = albums;
	}
	public List<CookStep> getSteps() {
		return steps;
	}
	public void setSteps(List<CookStep> steps) {
		this.steps = steps;
	}
	
}
