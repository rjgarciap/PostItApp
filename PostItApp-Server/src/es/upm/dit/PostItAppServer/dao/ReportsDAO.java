package es.upm.dit.PostItAppServer.dao;
import java.util.List;



import es.upm.dit.PostItAppServer.model.ColorNote;
import es.upm.dit.PostItAppServer.model.Reports;


public interface ReportsDAO {
	public List<Reports> listReports(); //Lista notas
	public void remove(long id); //Borra nota con ese id
	public Reports getById(long id); //Busca el reporte con el ID
	public void add(String noteId, String timestamp, String userId); //Add nota
	
}
