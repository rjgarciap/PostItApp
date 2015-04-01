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
	
	<button type="submit" id="ShowNotes">Mostrar todas las notas</button>
	<button type="submit" id="ReportedNotes">Mostrar notas reportadas</button>
	<button type="submit" id="ShowUsers">Mostrar todos los usuarios</button>
	
	
	

<script type="text/javascript">
    document.getElementById("ShowNotes").onclick = function () {
        location.href = "RUTA PARA PODER VER LAS NOTAS";
    };
</script>

<script type="text/javascript">
    document.getElementById("ReportedNotes").onclick = function () {
        location.href = "RUTA PARA PODER VER LAS NOTAS REPORTADAS";
    };
</script>

<script type="text/javascript">
    document.getElementById("ShowUsers").onclick = function () {
        location.href = "RUTA PARA PODER VER LOS USUARIOS";
    };
</script>
		
	</body>
</html>