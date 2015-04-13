<%@ page contentType="text/html;charset=UTF-8" language="java"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<%@page isELIgnored="false"%>
<!DOCTYPE html>
<html>
<!DOCTYPE html>
<html>
<head>
	
		<title>ReportsList</title>
		

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
            <ul>
               <li><a href='#'><span>hola</span></a></li>
               <li class='last'><a href='#'><span>Sub Product</span></a></li>
            </ul>
         </li>
         <li class='has-sub'><a href='/showReports'><span>Show Reported Notes</span></a>
         </li>
         <li class='has-sub'><a href='/showUsers'><span>Show Users</span></a>
         </li>
      </ul>
   </li>
   <li><a href='#'><span>About</span></a></li>
   <li class='last'><a href='#'><span>Help</span></a></li>
</ul>
</div>

<div id="fondo">
	

<div class="datagrid">	
<%-- HAY QUE PONER UN IF (NOTE IS REPORTED) THEN.. --%>
<table>
	<thead>
			<tr>
				<th>Fecha del reporte</th>
				<th>Id de la nota reportada</th>
				<th>Id del usuario que la subió</th>
				<th>Ver nota</th>
				
			</tr>
	</thead>
		<tbody>
			<c:forEach items="${reports}" var="report">
				<tr>
					<td><c:out value="Hola" /></td>
					<td><c:out value="${report.noteId}"/></td>
					<td><c:out value="${report.userId}" /></td>
					<td> <a href="<c:url value="/LookNote?id=${report.noteId}"/>">Ver nota</a></td>
					
				</tr>
			</c:forEach>
		</tbody>
</table>
</div>

</div>
</body>


</html>