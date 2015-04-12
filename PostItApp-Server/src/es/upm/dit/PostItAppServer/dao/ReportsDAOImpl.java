package es.upm.dit.PostItAppServer.dao;

import java.util.ArrayList;
import java.util.List;

import es.upm.dit.PostItAppServer.model.Note;
import es.upm.dit.PostItAppServer.model.Reports;
import es.upm.dit.PostItAppServer.dao.EMFService;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
public class ReportsDAOImpl implements ReportsDAO {
	private static ReportsDAOImpl instance;
	private ReportsDAOImpl(){
		
	}
	public static ReportsDAOImpl getInstance(){
		if(instance ==null){
			instance = new ReportsDAOImpl();
		}
		return instance;
	}
	
	public List<Reports> listReports() {
		EntityManager em = EMFService.get().createEntityManager();
		//HABRA QUE HACER UNA CONSULTA MAS RARA
		Query q = em.createQuery("select n from Reports n");
		List<Reports> reports = q.getResultList();
		
		return reports;
	}
	@Override
	public void remove(long id) {
		EntityManager em = EMFService.get().createEntityManager();
		try{
			Reports report = em.find(Reports.class, id);
			em.remove(report);
		} finally {
			em.close();
		}
	}
	@Override
	public Reports getById(long id) {
		EntityManager em = EMFService.get().createEntityManager();
		
		 Reports report = null;
		 try{
		  	 report = em.find(Reports.class, id);
		 } finally {
			 em.close();
		 }
		  
		 return report;		
	}
	@Override
	public void add(Long noteId, String timestamp, String userId) {
		synchronized(this){
			EntityManager em = EMFService.get().createEntityManager();
			Reports report = new Reports(noteId, timestamp, userId);
			em.persist(report);
			em.close();
		}
	}
		
	}
	

