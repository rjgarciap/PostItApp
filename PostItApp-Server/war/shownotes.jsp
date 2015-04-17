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
   <li class='active has-sub'><a href='/main'><span>Management</span></a>
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
	<table>
		<thead>
			<tr>
				
				<th>Id de Usuario</th>
				<th>Titulo de la Nota</th>
				<th>Contenido de la Nota</th>
				<th>Fecha de expiración de la Nota</th>
				<th>Localización</th>
				<th>Ver nota</th>
			</tr>
		</thead>
			
		<tbody>
			
			<c:forEach items="${notes}" var="note">
			
				<tr class = "alt">
					
					<td><c:out value="${note.userId}" /></td>
					<td><c:out value="${note.title}"/></td>
					<td><c:out value="${note.text}" /></td>
					<td><c:out value="${note.TTL}" /></td>
					
					
					<td><c:out value="${note.lat}"/><c:out value=","/><c:out value="${note.lon}"/></td>
					<td> <a href="<c:url value="/LookNote?id=${note.id}"/>">Ver nota</a></td>
				</tr>
			</c:forEach>
			
		</tbody>
			
	</table>
</div>

</div>
</body>


</html>