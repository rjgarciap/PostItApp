package es.upm.dit.PostItAppServer;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import es.upm.dit.PostItAppServer.dao.NoteDAO;
import es.upm.dit.PostItAppServer.dao.NoteDAOImpl;
import es.upm.dit.PostItAppServer.model.Note;

public class GetUserNotesServlet extends HttpServlet{

	private static final long serialVersionUID = 1L;
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		
		String userId = req.getParameter("userId");
		
		NoteDAO dao = NoteDAOImpl.getInstance();
		List <Note> notes = dao.getUserNotes(userId);
		
		String jsonNotes = new Gson().toJson(notes);
		
		resp.setContentType("application/json");
		
		resp.getWriter().println(jsonNotes);

		
	}
	
}
