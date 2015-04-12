<%@ page contentType="text/html;charset=UTF-8" language="java"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<%@page isELIgnored="false"%>
<!DOCTYPE html>


<html>
	<head>
		<title>Moderator LOGIN</title>
		<link rel="stylesheet" type="text/css" href="css/login.css" />
		<link href='http://fonts.googleapis.com/css?family=Oleo+Script' rel='stylesheet' type='text/css'>
		<link href='http://fonts.googleapis.com/css?family=Indie+Flower' rel='stylesheet' type='text/css'>
		<meta charset="utf-8">
	
	
	</head>
	
	
	<body>
		<div class = "contenedor">
			<div class="contenedor2">
				<header>
				<img id="logo" src="/images/logopeque.png">
				<h1>PostItApp</h1>
				</header>
			
			
				<section class="cuerpo">
				
					<aside class ="lateral">
					<!-- <img id = "mapa" src="/images/imagenmapa.png"> -->
					
					<img id="movimiento"src="/images/postit.png">
					
					</aside>

					<article>
						<div class = "background">
							<div class ="login" >
								<p>Por favor, introduzca sus credenciales para acceder.</p>
									
									
								
								<form action="/login" method="post"> 
								
								<p>Nombre de usuario:</p>
								<input type="text" id="name" name="name" placeholder="Introduzca su nick" />
								<input type="hidden" name="opcion" id="opcion" value="Campo vacío, introduzca su nombre/nick">
								<br>
								<p>Contraseña:</p>
								<input type="password" id="password" name="password" placeholder="Contraseña" />
								<br>
								<button type="submit">Enviar</button>
								
								<c:if test="${alerta == true }">
									<p id="alerta">*Valor de usuario o contraseña incorrecto</p>
									
									</c:if>
							  	
							  
								 </form>
							 </div>
						</div>
					</article>
					
				</section>
			</div>
		</div>

	
	
	
	
	<footer>
	<div class="footercontainer">
	<p> © 2015 PostItApp.com</p>

	</div>
	</footer>
	
	
		
	</body>
</html>