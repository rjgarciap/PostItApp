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
	
	public Note(String title, String text, Double lat, Double lon, ColorNote colorNote, String userId){
		
		this.title = title; 
		this.text = text;
		this.lat = lat;
		this.lon = lon;
		this.colorNote = colorNote;
		this.userId = userId;
		
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
}
