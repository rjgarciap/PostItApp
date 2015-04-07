package es.upm.dit.PostItAppServer;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;






public class LoginModeratorServlet extends HttpServlet {
	
	
	
private static final long serialVersionUID = 1L;
	
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		System.out.println("Recuperando datos del moderador");
		
		String admin = "admin";
		String name = checkNull(req.getParameter("name"));
		String password= checkNull(req.getParameter("password"));
		String menserror = req.getParameter("opcion");
		System.out.println("Nombre:"+name+"Contra:"+password);
		
		if (name.equals("admin")&&password.equals("admin") ){
			System.out.println("Entramos en if");
			
			
			
			
			System.out.println("Enviamos peticion");
			req.getSession().setAttribute("alerta", false);	
			RequestDispatcher view = req.getRequestDispatcher("main.jsp");
			try {
				view.forward(req, resp);
			} catch (ServletException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}	else{
			
			if(name.equals("")){
				req.getSession().setAttribute("alerta", false);	
				
			}else{
				
				req.getSession().setAttribute("alerta", true);	
			}
			
			
			System.out.println("Entramos en else");
			
			RequestDispatcher view = req.getRequestDispatcher("login.jsp");
			try {
				view.forward(req, resp);
			} catch (ServletException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		
	}

	private String checkNull(String s) {
		if (s== null){
			return "";
		}
		return s;
	}

	
	
	

}
