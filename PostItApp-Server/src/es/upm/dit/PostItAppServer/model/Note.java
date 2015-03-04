package es.upm.dit.PostItAppServer.model;


import java.io.Serializable;

import javax.persistence.Entity; 
import javax.persistence.GeneratedValue; 
import javax.persistence.GenerationType;
import javax.persistence.Id;



public class Note implements Serializable {
	
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	//Cambio a público porque si no lo de Moises da error
	public String title;
	public String text;
	private Double lat;
	private Double lon;
	
	
	public Note(String title, String text, Double lat, Double lon){
		
		this.title = title; 
		this.text = text;
		this.lat = lat;
		this.lon = lon;
		
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
	public void setTitle(String title){
		this.title = title;
	}
	public void setLat(Double lat){
		this.lat = lat;
	}
	public void setLon(Double lon){
		this.lon = lon;
	}


}
