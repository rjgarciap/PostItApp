package es.upm.dit.PostItAppServer;

import java.io.IOException;

import javax.servlet.http.*;

import es.upm.dit.PostItAppServer.dao.NoteDAOImpl;
import es.upm.dit.PostItAppServer.dao.NoteDAO;
import es.upm.dit.PostItAppServer.model.ColorNote;

public class PostNoteServlet extends HttpServlet{

	private static final long serialVersionUID = 1L; //Serializes objects through HTTP.
	
	public void doPost (HttpServletRequest req, HttpServletResponse resp) throws IOException{ // Uploads the content of a Post-It
		
		System.out.println("Creating new note");
				
		String title = checkNull(req.getParameter("title")); // Checks if the parameters are or not empty.
		String text = checkNull(req.getParameter ("content"));
		String lat = checkNull(req.getParameter("lat"));
		String lon  = checkNull(req.getParameter("long"));
		String colorNote  = checkNull(req.getParameter("colorNote"));
		String userId  = checkNull(req.getParameter("userId"));
		String imageId  = req.getParameter("imageId");
		String friendsList = req.getParameter("friendsList");
		String ttl = req.getParameter("ttl");
		
		NoteDAO dao = NoteDAOImpl.getInstance();
		dao.add(title,text,Double.parseDouble(lat),Double.parseDouble(lon), ColorNote.valueOf(colorNote),
				userId, imageId, ttl, friendsList ,0);
		
		//Habría que enviar un código de 200 ok o algo que haga que la app sepa que ha ido bien o no
	
		//resp.sendRedirect("/"); // Loads the index page.
		
	}
	
	//Yo borraria esto de check null y mejor obligaria a rellenar todos los campos del form
	private String checkNull (String s){ 
		if (s == null){ 
			return "This field should not be empty";
		}
		return s;
	}
	
}
