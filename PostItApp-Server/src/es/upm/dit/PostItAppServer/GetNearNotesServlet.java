package es.upm.dit.PostItAppServer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import es.upm.dit.PostItAppServer.dao.NoteDAO;
import es.upm.dit.PostItAppServer.dao.NoteDAOImpl;
import es.upm.dit.PostItAppServer.model.Note;

public class GetNearNotesServlet extends HttpServlet {

private static final long serialVersionUID = 1L;
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {

		
		// Recupero de la petición HTTP el id de la nota que quiere ver el usuario
		Double lat = Double.parseDouble(req.getParameter("lat"));
		Double lon = Double.parseDouble(req.getParameter("long"));
		String userId = req.getParameter("userId");
		
		Double lonSup = lon+1;
		Double lonInf = lon-1;
		
		NoteDAO dao = NoteDAOImpl.getInstance();
		
		List <Note> notes = dao.getNearNotes(lat, lon);
		List <Note> filterNotes = new ArrayList<Note>();
		
		for(int i=0;i<notes.size();i++){
			String[] friendsList = notes.get(i).getFriendsList().split("/");
			if(notes.get(i).getLon()<=lonSup &&notes.get(i).getLon()>=lonInf ){
				if((friendsList[0] == "" && friendsList.length == 1)|| notes.get(i).getUserId().equals(userId) ){
					filterNotes.add(notes.get(i));
				}else{
					for(String a : friendsList){
						if(a.equals(userId)){
							filterNotes.add(notes.get(i));
						}
					}
				}
			}
		}

		String jsonNotes = new Gson().toJson(filterNotes);
		
		resp.setContentType("application/json");
		
		resp.getWriter().println(jsonNotes);

	}	
}
