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
			
			//resp.sendRedirect("/");
			//req.getSession().setAttribute("name", name);
			
			//req.setAttribute(name, "hola");
			System.out.println("Enviamos peticion");
			
			RequestDispatcher view = req.getRequestDispatcher("main.jsp");
			try {
				view.forward(req, resp);
			} catch (ServletException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}	else{
			
			if(name.equals("")){
				
				
			}
			
			
			System.out.println("Entramos en else");
			resp.sendRedirect("/");
		}
		
		
	}

	private String checkNull(String s) {
		if (s== null){
			return "";
		}
		return s;
	}

	
	
	

}
