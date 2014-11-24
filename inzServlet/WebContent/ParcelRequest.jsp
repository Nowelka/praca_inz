<%@ page language="java" import="pl.javastart.servlets.*"
	contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Przesyłka</title>
</head>

<style>
/* ponizej ustawienie rozmieszczenia na ekranie elementow */
#map-canvas {
	width: 50%;
	height: 400px;
	position: absolute;
	right: 0px;
	top: 80px;
}
#kody {
	width: 100%;
	position: absolute;
	left: 0px;
	top: 80px;
}
</style>

<body>
<!-- wyswietlanie napisau i formularza -->
<%out.print("Podaj numer przesyłki"); %>
	<form id="formularz" method="post" action="">
		<input type="text" name="nr" /> <input type="submit" value="sprawdź" />
	</form>
<!-- sprawdzanie czy istnieje przesylka; wyswietlanie informacji o przesylce -->
<div id="kody">
	<%

		String id = request.getParameter("nr");

		if (id != null && (Sth.isInteger(id)) == true) {
			SearchParcel searchParcel = new SearchParcel(
					Integer.parseInt(id));
			if (searchParcel.getIsParcelExists() == true) {
				request.setAttribute("lat",
						searchParcel.getDelivererLatitude());
				request.setAttribute("lon",
						searchParcel.getDelivererLongitude());
				request.removeAttribute("nr");
				%>
				<table width="50%" border="1" >
			
					<tr>
						<td>
							<%
								out.print("Numer przesylki");
							%>
						</td>
						<td>
							<%
								out.print(id);
							%>
						</td>
					</tr>
			
					<tr>
						<td>
							<%
								out.print("Nadawca");
							%>
						</td>
						<td>
							<%
								out.print(searchParcel.getRegisteredUserNameUser());
							%><br />
							<%
								out.print(searchParcel.getRegisteredUserStreetUser() + " "
												+ searchParcel.getRegisteredUserCityUser() + " "
												+ searchParcel.getRegisteredUserCityCodeUser());
							%>
						</td>
					</tr>
			
					<tr>
						<td>
							<%
								out.print("Czas nadania");
							%>
						</td>
						<td>
							<%
								out.print(searchParcel.getParcelSendTime());
							%>
						</td>
					</tr>
			
					<tr>
						<td>
							<%
								out.print("Odbiorca");
							%>
						</td>
						<td>
							<%
								out.print(searchParcel.getParcelAddresseeName());
							%><br />
							<%
								out.print(searchParcel.getParcelAddresseeStreet() + " "
												+ searchParcel.getParcelAddresseeCity() + " "
												+ searchParcel.getParcelAddresseeCityCode());
							%>
						</td>
					</tr>
			
					<tr>
						<td>
							<%
								out.print("Planowany czas");
							%><br />
							<%
								out.print("dostarczenia");
							%>
						</td>
						<td>
							<%
								out.print(searchParcel.getParcelDeliveryTime());
							%>
						</td>
					</tr>
					<%if(searchParcel.getDelivererId() > 999000)  {
						searchParcel.selectBase(searchParcel.getDelivererId());
						out.print("Przesylka w bazie "+ searchParcel.getCentreNameCentre());
					}%>
			
				</table>
				<%
			} else
				out.print("Nie mamy takiej przesylki w bazie");
		}
		if (id == null || (Sth.isInteger(id)) == false)
			out.print("Prosze podac poprawny numer przesylki");
	%>
	</div>
	<div id="map-canvas">
<!-- Polaczenie z googleapis, pobranie mapy, wyznaczenie markera i wyswietlenie mapy -->
	<script
		src="https://maps.googleapis.com/maps/api/js?v=3.exp&sensor=false"></script>

	<script>
		var lat = ${lat};
		var lon = ${lon};
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
			var map = new google.maps.Map(
					document.getElementById('map-canvas'), mapOptions);
			var marker = new google.maps.Marker({
				position : new google.maps.LatLng(lat, lon),
				map : map,
				title : "Przesylka"
			});
		}

		google.maps.event.addDomListener(window, 'load', initialize);
	</script>
	</div>
</body>
</html>