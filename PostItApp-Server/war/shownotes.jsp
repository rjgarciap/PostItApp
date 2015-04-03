<%@ page contentType="text/html;charset=UTF-8" language="java"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<%@page isELIgnored="false"%>
<!DOCTYPE html>
<html>

<body>
<table>
			<tr>
				<th>Nombre de Usuario</th>
				<th>Titulo de la Nota</th>
				<th>Contenido de la Nota</th>
				<th>Localización</th>
				
			</tr>
	
			<c:forEach items="${notes}" var="note">
				<tr>
					<td><c:out value="${note.UserId}" /></td>
					<td><c:out value="${note.title}"/></td>
					<td><c:out value="${note.text}" /></td>
					<td><c:out value="${note.lat},"+"${note.lon}" /></td>
				
				</tr>
			</c:forEach>
		</table>
</body>


</html>