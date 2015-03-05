package es.upm.dit.PostItAppServer;

import java.io.IOException;
import javax.servlet.http.*;
import es.upm.dit.PostItAppServer.dao.NoteDAOImpl;
import es.upm.dit.PostItAppServer.dao.NoteDAO;
import es.upm.dit.PostItAppServer.model.Note;

public class GetNoteServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		
		// Recupero de la petición HTTP el id de la nota que quiere ver el usuario
		String id = req.getParameter("id");
		
		NoteDAO dao = NoteDAOImpl.getInstance();
		
		Note note = dao.getById(Long.parseLong(id));
		
		resp.setContentType("application/json");
		
		// OJO Habrá que añadir la geolocalización
		resp.getWriter().println("{\"title\": \"" + note.getTitle() + "\", \"text\": \"" + note.getText() + "\"}");
		
	}	
}