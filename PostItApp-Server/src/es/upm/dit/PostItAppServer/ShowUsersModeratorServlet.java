package es.upm.dit.PostItAppServer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import es.upm.dit.PostItAppServer.dao.NoteDAO;
import es.upm.dit.PostItAppServer.dao.NoteDAOImpl;
import es.upm.dit.PostItAppServer.model.Note;

public class ShowUsersModeratorServlet extends HttpServlet{
	
public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
		

		
		NoteDAO dao = NoteDAOImpl.getInstance();
	
		
	
		
		List<String> users = new ArrayList<String>();
		users = dao.getUsers();
		System.out.println(""+users.size());
		
		
		req.getSession().setAttribute("users", new ArrayList<String>(users));
		

	
		
		RequestDispatcher view = req.getRequestDispatcher("showusers.jsp");
		view.forward(req, resp);
			
		
	}


}



