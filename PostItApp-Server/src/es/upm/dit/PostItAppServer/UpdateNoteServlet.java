package es.upm.dit.PostItAppServer;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import es.upm.dit.PostItAppServer.dao.NoteDAO;
import es.upm.dit.PostItAppServer.dao.NoteDAOImpl;
import es.upm.dit.PostItAppServer.model.ColorNote;
import es.upm.dit.PostItAppServer.model.Note;

public class UpdateNoteServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		
		// Recupero de la petición HTTP el id de la nota que quiere borrar el usuario
		//String id = req.getParameter("id");
		String idNote = checkNull(req.getParameter("id"));
		String title = checkNull(req.getParameter("title")); // Checks if the parameters are or not empty.
		String text = checkNull(req.getParameter ("content"));
		String colorNote  = checkNull(req.getParameter("colorNote"));
		String imageId  = req.getParameter("imageId");
		ColorNote color = ColorNote.valueOf(colorNote);
		long id = Long.parseLong(idNote);
		//MIRAR
		NoteDAO dao = NoteDAOImpl.getInstance();
		dao.editNote(id, title, text, color, imageId);
		//Note note = dao.getById(Long.parseLong(idNote));
		
		//note.setText(title);
		//note.setText(text);
		//note.setColorNote(ColorNote.valueOf(colorNote));
		
	}
	
	private String checkNull (String s){ 
		if (s == null){ 
			return "This field should not be empty";
		}
		return s;
	}

}
