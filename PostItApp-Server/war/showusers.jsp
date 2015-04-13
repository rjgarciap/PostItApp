<%@ page contentType="text/html;charset=UTF-8" language="java"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<%@page isELIgnored="false"%>
<!DOCTYPE html>
<html>
<head>
	
		<title>UsersList</title>
		

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
	<table>
		<thead>
			<tr>
				<th>Nombre de Usuario</th>
				<%-- PODEMOS AÑADIR POR EJEMPLO FECHA DE ULTIMA CREACIÓN DE NOTA O NUM DE NOTAS.. --%>
				
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${users}" var="user">
				<tr class="alt">
					<td><c:out value="${user}" /></td>
					
					
				</tr>
			</c:forEach>
			</tbody>
		</table>
	</div>
</div>
</body>
</html>