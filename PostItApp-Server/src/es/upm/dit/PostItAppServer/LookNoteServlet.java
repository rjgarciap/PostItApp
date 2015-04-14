package es.upm.dit.PostItAppServer;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import es.upm.dit.PostItAppServer.dao.NoteDAO;
import es.upm.dit.PostItAppServer.dao.NoteDAOImpl;
import es.upm.dit.PostItAppServer.model.Note;

public class LookNoteServlet extends HttpServlet{

	private static final long serialVersionUID = 1L;
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException{
		
		String id = req.getParameter("id");
		
		NoteDAO dao = NoteDAOImpl.getInstance();
		Note note = dao.getById(Long.parseLong(id));
		
		req.getSession().setAttribute("note", note);
		

	
		
		RequestDispatcher view = req.getRequestDispatcher("looknote.jsp");
		view.forward(req, resp);
		
	
	}
}
