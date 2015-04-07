<%@ page contentType="text/html;charset=UTF-8" language="java"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<%@page isELIgnored="false"%>
<!DOCTYPE html>
<html>

<%-- HAY QUE PONER UN IF (NOTE IS REPORTED) THEN.. --%>
<table>
			<tr>
				<th>Nombre de Usuario</th>
				<th>Titulo de la Nota</th>
				<th>Contenido de la Nota</th>
				<th>Localización</th>
				
			</tr>
	
			<c:forEach items="${notes}" var="note">
				<tr>
					<td><c:out value="${note.userName}" /></td>
					<td><c:out value="${note.title}"/></td>
					<td><c:out value="${note.content}" /></td>
					<td><c:out value="${note.location}" /></td>
					
				</tr>
			</c:forEach>
		</table>

</html>