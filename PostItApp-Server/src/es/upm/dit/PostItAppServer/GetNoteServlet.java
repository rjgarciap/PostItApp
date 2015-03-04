package es.upm.dit.PostItAppServer;

import java.io.IOException;
import javax.servlet.http.*;

public class GetNoteServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		
		// Recupero de la petición HTTP el id de la nota que quiere ver el usuario
		String id = req.getParameter("id");
		
		// OJO a los nombres aquí
		NoteDAO dao = NoteDAOImpl.getInstance();
		
		// OJO que este método no venía en lo de los TODOs
		Note note = dao.getById(id);
		
		resp.setContentType("application/json");
		
		// OJO Habrá que añadir la geolocalización
		resp.getWriter().println("{\"title\": \"" + note.title + "\", \"text\": \"" + note.text + "\"}");
		
	}	
}