package dit.upm.es.postitapp;


import java.io.Serializable;


public class Note implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private Long id;
	private String title;
	private String text;
	private Double lat;
	private Double lon;
	private ColorNote colorNote;
	
	
	public Note(String title, String text, Double lat, Double lon, ColorNote colorNote){
		
		this.title = title; 
		this.text = text;
		this.lat = lat;
		this.lon = lon;
		this.colorNote = colorNote;
		
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


}
