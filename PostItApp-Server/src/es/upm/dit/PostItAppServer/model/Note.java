package es.upm.dit.PostItAppServer.model;


import java.io.Serializable;

import javax.persistence.Entity; 
import javax.persistence.GeneratedValue; 
import javax.persistence.GenerationType;
import javax.persistence.Id;


@Entity
public class Note implements Serializable {
	
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String title;
	private String text;
	private Double lat;
	private Double lon;
	private ColorNote colorNote;
	private String userId;
	private String imageId;
	private String ttl;
	private String friendsList;
	private int views;
	
	public Note(String title, String text, Double lat, Double lon, ColorNote colorNote, String userId,
			String imageId, String ttl,String friendsList ,int views){
		
		this.title = title; 
		this.text = text;
		this.lat = lat;
		this.lon = lon;
		this.colorNote = colorNote;
		this.userId = userId;
		this.imageId = imageId;
		this.friendsList = friendsList;
		this.ttl = ttl;
		this.views = views;
		
	}
	
	public Long getId(){
		return id;
	}
	public String getTitle(){
		return title;
	}
	public String getText(){
		return text;		
	}
	public Double getLon(){
		return lon;
	}
	public Double getLat(){
		return lat;
	}
	public ColorNote getColorNote(){
		return colorNote;
	}
	public String getFriendsList(){
		return friendsList;
	}
	public void setTitle(String title){
		this.title = title;
	}
	public void setText(String text){
		this.text= text;
	}
	public void setLat(Double lat){
		this.lat = lat;
	}
	public void setLon(Double lon){
		this.lon = lon;
	}
	public void setColorNote(ColorNote colorNote){
		this.colorNote = colorNote;
	}
	public String getUserId(){
		return userId;		
	}
	public void setUserId(String userId){
		this.userId= userId;
	}
	public String getImageId(){
		return imageId;		
	}
	public void setImageId(String imageId){
		this.imageId= imageId;
	}
	public String getTTL(){
		return ttl;
	}
	public void setTTL(String ttl){
		this.ttl = ttl;
	}
	public int getViews(){
		return views;
	}
	public void setViews(int views){
		this.views = views;
	}
	public void setFriendsList(String friendsList){
		this.friendsList = friendsList;
	}
}
