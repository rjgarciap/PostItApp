<%@ page contentType="text/html;charset=UTF-8" language="java"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<%@page isELIgnored="false"%>
<!DOCTYPE html>
<html>

<table>
			<tr>
				<th>Nombre de Usuario</th>
				<%-- PODEMOS AÑADIR POR EJEMPLO FECHA DE ULTIMA CREACIÓN DE NOTA O NUM DE NOTAS.. --%>
				
			</tr>
	
			<c:forEach items="${notes}" var="note">
				<tr>
					<td><c:out value="${note.userName}" /></td>
					
					
				</tr>
			</c:forEach>
		</table>

</html>