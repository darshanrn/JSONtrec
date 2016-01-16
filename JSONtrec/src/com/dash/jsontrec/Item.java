package com.dash.jsontrec;

public class Item {
	String title;
	String subtitle;
	String imageURL;
	
	public Item(String title, String description, String imageURL){
		this.title = title;
		this.subtitle = description;
		this.imageURL = imageURL;
	}
	
	public String getTitle(){
		return this.title;
	}
	
	public String getSubtitle(){
		return this.subtitle;
	}
	
	public String getImageURL(){
		return this.imageURL;
	}
}
