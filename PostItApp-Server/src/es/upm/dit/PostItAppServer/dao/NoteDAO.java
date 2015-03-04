package es.upm.dit.PostItAppServer.dao;

import java.util.List;

import es.upm.dit.PostItAppServer.model.Note;


public interface NoteDAO {
	
	public List<Note> listNotes(); //Lista notas
	public void add(String title, String text, Double lat, Double lon);//Añade nota
	public void remove(long id); //Borra nota con ese id
	public Note getById(String id);
	

}
