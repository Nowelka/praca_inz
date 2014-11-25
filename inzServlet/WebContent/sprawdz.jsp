<%@ page language="java" import="pl.javastart.servlets.*"
	contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<!--
	Linear by TEMPLATED
    templated.co @templatedco
    Released for free under the Creative Commons Attribution 3.0 license (templated.co/license)
-->
<html>
<head>
<title>Sprawdz przesylke</title>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<meta name="description" content="" />
<meta name="keywords" content="" />
<link
	href='http://fonts.googleapis.com/css?family=Roboto:400,100,300,700,500,900'
	rel='stylesheet' type='text/css'>
<script
	src="http://ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js"></script>
<script src="js/skel.min.js"></script>
<script src="js/skel-panels.min.js"></script>
<script src="js/init.js"></script>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/skel-noscript.css"
	type="text/css" />
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/style.css" type="text/css" />
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/style-desktop.css"
	type="text/css" />
</head>
<style>
#map-canvas {
	width: 100%;
	height: 400px;
}
#map-canvas-direction {
	width: 100%;
	height: 400px;
}
</style>
<body>
	<!-- Header -->
	<div id="header">
		<div id="nav-wrapper">
			<!-- Nav -->
			<nav id="nav">
				<ul>
					<div align="right">
						<a href="logowanie"><font color="#FFFFFF" size="3em">Zaloguj</font></a>
					</div>
					<li><a href="index">Homepage</a></li>
					<li class="active"><a href="zamowienie">Zlozenie
							zamowienia</a></li>
					<li><a href="sprawdz">Sprawdz przesylke</a></li>
					<li><a href="info">O firmie</a></li>
				</ul>
			</nav>
		</div>
		<div class="container">

			<!-- Logo -->
			<div id="logo">
				<h1>
					<a href="#">Firma transportowa</a>
				</h1>
				<span class="tag">Twój czas</span>
			</div>
		</div>
	</div>
	<!-- Header -->

	<!-- Main -->

	<div id="main">
		<div class="container">
			<div class="row">

				<!-- Sidebar -->
				<div id="sidebar" class="4u">
					<section>
						<header>
							<h2>Wpisz nr przesylki</h2>
							<form id="formularz" method="post" action="">
								<input type="text" name="nr" /> <input type="submit"
									value="sprawdź" />
							</form>
							${msg}
						</header>

						<%
							if (request.getAttribute("b") != null
									&& request.getAttribute("b").equals(true)) {
						%>
						<table width="100%">

							<tr>
								<td>
									<%
										out.print("Numer przesylki");
									%>
								</td>
								<td>${id}</td>
							</tr>

							<tr>
								<td>
									<%
										out.print("Nadawca");
									%>
								</td>
								<td>${regUserName}<br /> ${regUserAddr}
								</td>
							</tr>

							<tr>
								<td>
									<%
										out.print("Czas nadania");
									%>
								</td>
								<td>${timeSend}</td>
							</tr>

							<tr>
								<td>
									<%
										out.print("Odbiorca");
									%>
								</td>
								<td>${userName}<br /> ${userAddr}
								</td>
							</tr>

							<tr>
								<td>
									<%
										out.print("Planowany czas");
									%><br /> <%
 	out.print("dostarczenia");
 %>
								</td>
								<td>${timeDelivery}</td>
							</tr>
							<tr>
								<td>
									<%
										out.print("Ostatnie zarejestrowane");
									%><br /> <%
 	out.print("położenie");
 %>
								</td>
								<td>${lastTime}</td>
							</tr>
						</table>

					</section>

				</div>

				<!-- Content -->
				<div id="content" class="8u skel-cell-important">
					<div id="map-canvas">
						<!-- Polaczenie z googleapis, pobranie mapy, wyznaczenie markera i wyswietlenie mapy -->
						<script
							src="https://maps.googleapis.com/maps/api/js?v=3.exp&sensor=false"></script>

						<script>
							var lat = "${lat}";
							var lon = "${lon}";
							function initialize() {
								var mapOptions = {
									zoom : 12,
									center : new google.maps.LatLng(lat, lon),
									mapTypeControl : true,
									mapTypeControlOptions : {
										style : google.maps.MapTypeControlStyle.HORIZONTAL_BAR,
										position : google.maps.ControlPosition.BOTTOM_CENTER
									},
									panControl : true,
									panControlOptions : {
										position : google.maps.ControlPosition.TOP_RIGHT
									},
									zoomControl : true,
									zoomControlOptions : {
										style : google.maps.ZoomControlStyle.LARGE,
										position : google.maps.ControlPosition.LEFT_CENTER
									},
									scaleControl : true,
									streetViewControl : true,
									streetViewControlOptions : {
										position : google.maps.ControlPosition.LEFT_TOP
									}
								}
								var map = new google.maps.Map(document
										.getElementById('map-canvas'),
										mapOptions);
								var marker = new google.maps.Marker(
										{
											position : new google.maps.LatLng(
													lat, lon),
											map : map,
											title : "Kurier"
										});
							}
							google.maps.event.addDomListener(window, 'load',
									initialize);
						</script>
						
					</div>
					
					<div id="map-canvas-direction">
						<script src="https://maps.googleapis.com/maps/api/js?v=3.exp"></script>
						<script>
							var directionsDisplay;
							var directionsService = new google.maps.DirectionsService();
							var map;

							function initialize() {
								directionsDisplay = new google.maps.DirectionsRenderer();
								var latlon = new google.maps.LatLng("${lat}",
										"${lon}");
								var mapOptions = {
									zoom : 7,
									center : latlon
								};
								map = new google.maps.Map(document
										.getElementById('map-canvas-direction'),
										mapOptions);
								directionsDisplay.setMap(map);
								var marker = new google.maps.Marker(
										{
											position : new google.maps.LatLng(
													lat, lon),
											map : map,
											title : "Kurier"
										});
							}

							function calcRoute() {
								var start = "${regUserAddr}";
								var end = "${userAddr}";
								var request = {
									origin : start,
									destination : end,
									travelMode : google.maps.TravelMode.DRIVING
								};
								directionsService
										.route(
												request,
												function(response, status) {
													if (status == google.maps.DirectionsStatus.OK) {
														directionsDisplay
																.setDirections(response);
													}
												});
							}

							google.maps.event.addDomListener(window, 'load',
									initialize);
							google.maps.event.addDomListener(window, 'load',
									calcRoute);
						</script>

					</div>
					<%
							}
						%>
				</div>


			</div>
		</div>
	</div>
	<!-- /Main -->

	<!-- Tweet -->
	<div id="tweet">
		<div class="container">
			<section>
				<blockquote>&ldquo;cycat cycat&rdquo;</blockquote>
			</section>
		</div>
	</div>
	<!-- /Tweet -->

	<!-- Footer -->
	<div id="footer">
		<div class="container">
			<section>
				<header>
					<h2>Kontakt</h2>
					<span class="byline">telefon telefon</span> <span class="byline">email
						email</span>
				</header>
			</section>
		</div>
	</div>
	<!-- /Footer -->

	<!-- Copyright -->
	<div id="copyright">
		<div class="container">
			Design: <a href="http://templated.co">TEMPLATED</a> Images: <a
				href="http://unsplash.com">Unsplash</a> (<a
				href="http://unsplash.com/cc0">CC0</a>)
		</div>
	</div>


</body>
</html>