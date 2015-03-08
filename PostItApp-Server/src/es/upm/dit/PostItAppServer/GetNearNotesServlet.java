package es.upm.dit.PostItAppServer;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import es.upm.dit.PostItAppServer.dao.NoteDAO;
import es.upm.dit.PostItAppServer.dao.NoteDAOImpl;
import es.upm.dit.PostItAppServer.model.Note;

public class GetNearNotesServlet extends HttpServlet {

private static final long serialVersionUID = 1L;
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		
		HttpSession session = req.getSession();
		
		// Recupero de la petición HTTP el id de la nota que quiere ver el usuario
		Double lat = Double.parseDouble(req.getParameter("lat"));
		Double lon = Double.parseDouble(req.getParameter("long"));
		
		NoteDAO dao = NoteDAOImpl.getInstance();
		
		List <Note> notes = dao.getNearNotes(lat, lon);
		
		session.setAttribute("notesList", notes);
	
	}	
}
