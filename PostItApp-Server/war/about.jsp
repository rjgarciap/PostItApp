<%@ page contentType="text/html;charset=UTF-8" language="java"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<%@page isELIgnored="false"%>
<!DOCTYPE html>


<html>
	<head>
		<title>Moderation Workspace</title>
		<link rel="stylesheet" type="text/css" href="css/main.css" />
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
	<img alt="" src="/images/foto.png">
	
	</div>
	
	<p> PostItApp - ISST 2014-2015 </p>
	
	<p> Grupo 18 </p>
	<p> Componentes del equipo </p>
	<p> Product Owner: Jorge Ramírez </p>
	<p>	Scrum Master: Ricardo García</p>
	<p>Team Developer Members: 
		Cristina Luna, Moisés Torres, Rubén Oliva</p>
	
	<a href="mailto:app.postit@gmail.com">Contacta con Nosotros.</a>


		
	</body>
</html>