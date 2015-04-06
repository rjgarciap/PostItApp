package es.upm.dit.PostItAppServer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

import es.upm.dit.PostItAppServer.dao.NoteDAO;
import es.upm.dit.PostItAppServer.dao.NoteDAOImpl;
import es.upm.dit.PostItAppServer.model.*;

public class ShowNotesModeratorServlet extends HttpServlet {
	
private static final long serialVersionUID = 1L;
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
		

		
		NoteDAO dao = NoteDAOImpl.getInstance();
	
		
	
		
		List<Note> notes = new ArrayList<Note>();
		notes = dao.listNotes();
		System.out.println(""+notes.size());
		
		
		req.getSession().setAttribute("notes", new ArrayList<Note>(notes));
		

	
		
		RequestDispatcher view = req.getRequestDispatcher("shownotes.jsp");
		view.forward(req, resp);
			
		
	}


}
