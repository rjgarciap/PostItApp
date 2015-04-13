package es.upm.dit.PostItAppServer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.*;

//import com.cloudinary.Cloudinary;

import es.upm.dit.PostItAppServer.dao.ReportsDAOImpl;
import es.upm.dit.PostItAppServer.dao.ReportsDAO;
import es.upm.dit.PostItAppServer.model.Reports;

public class ReportsServlet extends HttpServlet{
	
private static final long serialVersionUID = 1L;
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
		

		
		ReportsDAO dao = ReportsDAOImpl.getInstance();
	
		
	
		
		List<Reports> reports = new ArrayList<Reports>();
		reports = dao.listReports();
		System.out.println(""+reports.size());
		
		
		req.getSession().setAttribute("reports", new ArrayList<Reports>(reports));
		

	
		//Cloudinary cloudinary;
		RequestDispatcher view = req.getRequestDispatcher("showreported.jsp");
		view.forward(req, resp);
			
		
	}

	
}
