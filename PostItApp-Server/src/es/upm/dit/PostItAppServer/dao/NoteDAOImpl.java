package es.upm.dit.PostItAppServer.dao;

import java.util.List;

import es.upm.dit.PostItAppServer.model.Note;
import es.upm.dit.PostItAppServer.dao.EMFService;

import javax.persistence.EntityManager;
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
		//HABRA QUE HACER UNA CONSULTA MAS RARA
		Query q = em.createQuery("select n from Notes n");
		List<Note> notes = q.getResultList();
		
		return notes;
	}

	@Override
	public void add(String title, String text, Double lat, Double lon) {
		synchronized(this){
			EntityManager em = EMFService.get().createEntityManager();
			Note note = new Note(title, text, lat, lon);
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
		//Query q = em.createQuery("select n from Notes n where n.id =:id");
		//Note note = (Note) q.getSingleResult();
		//return note;

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
		// TODO Auto-generated method stub
		
		EntityManager em = EMFService.get().createEntityManager();
		Query q = em.createQuery("select n from Note n where n.lat >= :lat-2 AND lat <= :lat+2 AND lon >= :lon-2 AND lon <= :lon+2");
		List<Note> notes= q.getResultList();
		return notes;
	}

}
