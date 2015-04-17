package es.upm.dit.PostItAppServer.dao;

import java.util.ArrayList;
import java.util.List;

import es.upm.dit.PostItAppServer.model.ColorNote;
import es.upm.dit.PostItAppServer.model.Note;
import es.upm.dit.PostItAppServer.dao.EMFService;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;


public class NoteDAOImpl implements NoteDAO {
	
	private static NoteDAOImpl instance;
	
	private NoteDAOImpl(){}
	
	public static NoteDAOImpl getInstance(){
		if(instance ==null){
			instance = new NoteDAOImpl();
		}
		return instance;
	}

	@Override
	public List<Note> listNotes() {
		EntityManager em = EMFService.get().createEntityManager();
	
		Query q = em.createQuery("select n from Note n");
	
		List<Note> notes= q.getResultList();
		if(notes == null){
			notes = new ArrayList<Note>();
		}
		
		return notes;
	}

	@Override
	public void add(String title, String text, Double lat, Double lon, ColorNote colorNote, String userId, 
			String imageId, String ttl, int views) {
		synchronized(this){
			EntityManager em = EMFService.get().createEntityManager();
			Note note = new Note(title, text, lat, lon, colorNote, userId, imageId, ttl, views);
			em.persist(note);
			em.close();
		}
	}

	@Override
	public void remove(long id) {
		
		EntityManager em = EMFService.get().createEntityManager();
		try{
			Note note = em.find(Note.class, id);
			em.remove(note);
		} finally {
			em.close();
		}
		

	}

	@Override
	public Note getById(long id) {
		
		EntityManager em = EMFService.get().createEntityManager();

		 Note note = null;
		 try{
		  	 note = em.find(Note.class, id);
		 } finally {
			 em.close();
		 }
		  
		 return note;		
	}

	@Override
	public List<Note> getNearNotes(Double lat, Double lon) {
		
		Double latSup = lat+1;
		Double latInf = lat-1;
	
		EntityManager em = EMFService.get().createEntityManager();
		Query q = em.createQuery("select n from Note n where n.lat >= :latInf AND n.lat <= :latSup");
		q.setParameter("latSup", latSup);
		q.setParameter("latInf", latInf);
		List<Note> notes= q.getResultList();
		if(notes == null){
			notes = new ArrayList<Note>();
		}
		
		return notes;
	}
	
	@Override
	public List<Note> getUserNotes(String userId) {

		EntityManager em = EMFService.get().createEntityManager();
		Query q = em.createQuery("select n from Note n where n.userId = :userId");
		q.setParameter("userId", userId);
		List<Note> notes= q.getResultList();
		if(notes == null){
			notes = new ArrayList<Note>();
		}
		
		return notes;
	}
	
	@Override
	public void editNote(long id, String title, String text,  ColorNote colorNote, String imageId, String ttl){

		EntityManager em = EMFService.get().createEntityManager();

		EntityTransaction tx = em.getTransaction();
		try {
		        tx.begin();
		        Note note = em.find(Note.class, id);
		        note.setTitle(title);
		        note.setText(text);
		        note.setColorNote(colorNote);
		        note.setImageId(imageId);
		        note.setTTL(ttl);
		        em.persist(note);
		        tx.commit();
		} finally {
		        if (tx.isActive()) {
		                tx.rollback();
		        }
		}
		
	}
	
	
	
	public List<String> getUsers(){
		
		EntityManager em = EMFService.get().createEntityManager();
		
		Query q = em.createQuery("select distinct n.userId from Note n");
	
		List<String> users= q.getResultList();
		if(users == null){
			users = new ArrayList<String>();
		}
		
		return users;
		
		
	}
	
	public Note getNoteAddView(long id){
		
		EntityManager em = EMFService.get().createEntityManager();

		EntityTransaction tx = em.getTransaction();
		try {
		        tx.begin();
		        Note note = em.find(Note.class, id);
		        note.setViews(note.getViews()+1);
		        em.persist(note);
		        tx.commit();
		        return note;
		} finally {
		        if (tx.isActive()) {
		                tx.rollback();
		        }
		}	
		
	}
	
}
