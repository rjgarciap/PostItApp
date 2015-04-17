package es.upm.dit.PostItAppServer;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import es.upm.dit.PostItAppServer.dao.NoteDAO;
import es.upm.dit.PostItAppServer.dao.NoteDAOImpl;
import es.upm.dit.PostItAppServer.model.Note;


public class DeleteExpiredNotesCronServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		
		NoteDAO dao = NoteDAOImpl.getInstance();
		List <Note> notes = dao.listNotes();
		
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date today = new Date();
		
		for (Note note : notes) {
			String expirationDateString = note.getTTL(); 
			if(!expirationDateString.equals("")){
				
				try {
					Date expirationDate = formatter.parse(expirationDateString);
					
					if(expirationDate.before(today))
						dao.remove(note.getId());
					
				} catch (ParseException e) {
					e.printStackTrace();
				}		
					
			}
		}
		
	}
	
}