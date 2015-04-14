package es.upm.dit.PostItAppServer;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import es.upm.dit.PostItAppServer.dao.NoteDAO;
import es.upm.dit.PostItAppServer.dao.NoteDAOImpl;
import es.upm.dit.PostItAppServer.dao.ReportsDAO;
import es.upm.dit.PostItAppServer.dao.ReportsDAOImpl;

public class DeleteReportsServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException{
		String id = req.getParameter("id");
		NoteDAO dao = NoteDAOImpl.getInstance();
		dao.remove(Long.parseLong(id));
		
		ReportsDAO daorep = ReportsDAOImpl.getInstance();
		daorep.remove(Long.parseLong(id));
		
		resp.sendRedirect("/showNotes");
	
	}
}
