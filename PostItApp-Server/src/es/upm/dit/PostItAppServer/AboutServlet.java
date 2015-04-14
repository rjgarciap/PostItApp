package es.upm.dit.PostItAppServer;
import java.io.IOException;


import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class AboutServlet extends HttpServlet{
/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
	
		
		RequestDispatcher view = req.getRequestDispatcher("about.jsp");
		view.forward(req, resp);
			
		
	}
}