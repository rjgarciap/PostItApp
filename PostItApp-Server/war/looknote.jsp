<%@ page contentType="text/html;charset=UTF-8" language="java"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<%@page isELIgnored="false"%>
<!DOCTYPE html>
<html>
<head>
	
		<title>NotesList</title>
		

		<link rel="stylesheet" type="text/css" href="css/shownotes.css" />
		
		<meta charset="utf-8">
	</head>

<body>
<div id='cssmenu'>
<ul>
   <li><a href='main.jsp'><span>Home</span></a></li>
   <li class='active has-sub'><a href='main.jsp'><span>Management</span></a>
      <ul>
         <li class='has-sub'><a href='/showNotes'><span>Show Notes</span></a>
            
         </li>
         <li class='has-sub'><a href='/reportNotes'><span>Show Reported Notes</span></a>
         </li>
         <li class='has-sub'><a href='/showUsers'><span>Show Users</span></a>
         </li>
      </ul>
   </li>

   <li class='last'><a href='/about'><span>About</span></a></li>
</ul>
</div>

<div id="fondo">
	
	
	

<div class="datagrid">
		<form action="/deletereport?id=${note.id}" method="post" accept-charset="utf-8">
					
						<table>
						<tbody>
						
							<tr class = "alt">
								<td><label for="title">Título</label></td>
								<td><c:out value="${note.title}" /></td>
							</tr>
							<tr class = "alt">
								<td><label for="text">Contenido</label></td>
								<td><c:out value="${note.text}" /></td>
							</tr>
							<tr class = "alt">
							
							<td><label for="image">Imagen</label></td>
					
								<c:choose>
									<c:when test="${note.imageId != \"\"}">
								
								<td><img width="100" height="100" src=<c:url value="http://res.cloudinary.com/postitapp/image/upload/${note.imageId}"/>></td>
								</c:when>
								<c:otherwise>
								<td><c:out value="No imagen" />
								</c:otherwise>
								</c:choose>
							</tr>
							
							<tr class = "alt">
								<td><label for="localization">Localización</label></td>
								<td><c:out value="${note.lat}"/><c:out value=","/><c:out value="${note.lon}"/></td>
							</tr>
							
							
							<tr class = "alt">
								<td><input type="submit" value="Borrar"/></td>
							</tr>
						</tbody>
						</table>
						
					</form>
</div>

</div>
</body>


</html>