<%@ page contentType="text/html;charset=UTF-8" language="java"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>


<%@page isELIgnored="false"%>
<!DOCTYPE html>
<html>
<head>

<title>NotesList</title>


<link rel="stylesheet" type="text/css" href="css/lookNote.css" />
<link href='http://fonts.googleapis.com/css?family=Oleo+Script'
	rel='stylesheet' type='text/css'>
<link href='http://fonts.googleapis.com/css?family=Indie+Flower'
	rel='stylesheet' type='text/css'>
<meta charset="utf-8">
</head>

<body>
	<div id='cssmenu'>
		<ul>
			<li><a href='main.jsp'><span>Home</span></a></li>
			<li class='active has-sub'><a href='main.jsp'><span>Management</span></a>
				<ul>
					<li class='has-sub'><a href='/showNotes'><span>Show
								Notes</span></a></li>
					<li class='has-sub'><a href='/reportNotes'><span>Show
								Reported Notes</span></a></li>
					<li class='has-sub'><a href='/showUsers'><span>Show
								Users</span></a></li>
				</ul></li>

			<li class='last'><a href='/about'><span>About</span></a></li>
		</ul>
	</div>

	<div id="fondo">


		<c:choose>


			<c:when test="${note.colorNote == \"BLUE\"}">
				<c:set var="caja" value="cajablue" />
			</c:when>
			<c:when test="${note.colorNote == \"RED\"}">
				<c:set var="caja" value="cajared" />
			</c:when>
			<c:when test="${note.colorNote == \"GREEN\"}">
				<c:set var="caja" value="cajagreen" />
			</c:when>
			<c:otherwise>
				<c:set var="caja" value="cajayellow" />
			</c:otherwise>

		</c:choose>

		<div class=<c:out value="${caja}"/>>

			<form action="/deletereport?id=${note.id}" method="post"
				accept-charset="utf-8">




				<dl>
					<dt>
						<label for="expiracion">Fecha de expiración: </label>
						<dd>
						<c:choose>
						<c:when test="${note.TTL != null}">
							<c:out value="${note.TTL }" />
						</c:when>
						<c:otherwise>
							
								<c:out value="No se ha definido fecha de expiración" />
							
						</c:otherwise>
					</c:choose>
					</dd>
					</dt>



					<dt>
						<label for="title">Título:</label>
					</dt>

					<dd>
						<c:out value="${note.title}" />
					</dd>

					<br>
					<dt>
						<label for="text">Contenido:</label>
					</dt>
					<dd>
						<c:out value="${note.text}" />
					</dd>
					<br>
					<!-- <dt><label for="text">Color:</label></dt> -->
					<!--<dd><c:out value="${note.colorNote}" /></dd>-->

					<!--<dt><label for="image">Imagen</label></dt>-->
					<br>
					<c:choose>
						<c:when test="${note.imageId != \"\"}">
							<center>
								<img
									src=<c:url value="http://res.cloudinary.com/postitapp/image/upload/${note.imageId}"/>>
							</center>
						</c:when>
						<c:otherwise>
							<dd>
								<c:out value="(No hay imagen)" />
							</dd>
						</c:otherwise>
					</c:choose>



					<!--<dt><label for="localization">Localización:</label></dt>-->
					<!--<dd><c:out value="${note.lat}"/><c:out value=","/><c:out value="${note.lon}"/></dd>-->

					<br>

					<center>
						<dt>
							<input class="button" type="submit" value="ELIMINAR NOTA" />
						</dt>
					</center>
					</br>
				</dl>

			</form>


		</div>

	</div>
</body>


</html>