package es.upm.dit.PostItAppServer;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import es.upm.dit.PostItAppServer.dao.ReportsDAO;
import es.upm.dit.PostItAppServer.dao.ReportsDAOImpl;


public class ReportsServlet extends HttpServlet {

private static final long serialVersionUID = 1L; //Serializes objects through HTTP.
	
	public void doPost (HttpServletRequest req, HttpServletResponse resp) throws IOException{ // Uploads the content of a Post-It
		
		System.out.println("Reporting new note");
				
		Long noteId = Long.parseLong(req.getParameter("noteId")); // Checks if the parameters are or not empty.
		String userNoteId = checkNull(req.getParameter ("userId"));
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd/k:m:s");
		Date timestampDate = new Date();
		String timestamp = formatter.format(timestampDate) ;
		
		
		ReportsDAO dao = ReportsDAOImpl.getInstance();
		dao.add(noteId, timestamp, userNoteId);
		
	}
	
	//Yo borraria esto de check null y mejor obligaria a rellenar todos los campos del form
	private String checkNull (String s){ 
		if (s == null){ 
			return "This field should not be empty";
		}
		return s;
	}
}