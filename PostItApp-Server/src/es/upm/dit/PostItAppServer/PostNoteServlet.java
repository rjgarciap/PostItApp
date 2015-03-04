package es.upm.dit.PostItAppServer;

import java.io.IOException;


import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;


// IMPORTAR LOS PAQUETES DAO.

public class PostNoteServlet extends HttpServlet{

	private static final long serialVersionUID = 1L; //Serializes objects through HTTP.
	
	public void doPost (HttpServletRequest req, HttpServletResponse resp) throws IOException{ // Uploads the content of a Post-It
		System.out.println("Creating new Post-It ");
				
		String title = checkNull(req.getParameter("title")); // Checks if the parameters are or not empty.
		String text = checkNull (req.getParameter ("text"));
		String lat = checkNull (req.getParameter("lat"));
		String lon  = checkNull (req.getParameter("lon"));
		
		//A�ADIR LA GEOLOCALIZACION
		
		
		
		// INTRODUCIR LOS METODOS DEL DAO, Y EL REDIRECT...
		
		resp.sendRedirect("/"); // Loads the index page.
		
	}
	private String checkNull (String s){ 
		if (s == null){ 
			return "This field should not be empty";
		}
		return s;
	}
	
}
