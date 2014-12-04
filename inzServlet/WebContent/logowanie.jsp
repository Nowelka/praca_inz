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
<title>Zamowienie</title>
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
<body>

	<!-- Header -->
	<div id="header">
		<div id="nav-wrapper">
			<!-- Nav -->
			<nav id="nav">
				<ul>
					<div align="right">
						<a href="logowanie"><font color="#FFFFFF" size="3em" align="right">Zaloguj</font></a>
					</div>
					<li><a href="index">Home</a></li>
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
							<h2></h2>
						</header>
					</section>
				</div>
				<!-- Content -->
				<div id="content" class="8u skel-cell-important">
					<section>
						<header>
							<h2>Logowanie</h2>
						</header>
						imie i nazwisko
						<form id="formularz" method="post" action="">
							<input type="text" name="name" />
						</form>
						ulica
						<form id="formularz" method="post" action="">
							<input type="text" name="street" />
						</form>
						miasto
						<form id="formularz" method="post" action="">
							<input type="text" name="city" />
						</form>
						kod pocztowy
						<form id="formularz" method="post" action="">
							<input type="text" name="cityCode" /> <input type="submit"
								value="dodaj" />
						</form>



					</section>
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