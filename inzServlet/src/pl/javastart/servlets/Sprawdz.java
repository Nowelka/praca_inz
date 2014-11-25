package pl.javastart.servlets;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Sprawdz extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		RequestDispatcher view = req.getRequestDispatcher("/sprawdz.jsp");
		System.out.println("get");
		view.forward(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String id = req.getParameter("nr");
		boolean isParcelExists = false;
		System.out.println("post");
		RequestDispatcher view = req.getRequestDispatcher("/sprawdz.jsp");

		if (id != null && (Sth.isInteger(id)) == true) {
			SearchParcel searchParcel = new SearchParcel(Integer.parseInt(id));
			if (searchParcel.getIsParcelExists() == true) {

				isParcelExists = true;
				req.setAttribute("id", id);
				req.setAttribute("lat", searchParcel.getDelivererLatitude());
				req.setAttribute("lon", searchParcel.getDelivererLongitude());
				req.setAttribute("regUserName",
						searchParcel.getRegisteredUserNameUser());
				req.setAttribute(
						"regUserAddr",
						searchParcel.getRegisteredUserStreetUser() + " "
								+ searchParcel.getRegisteredUserCityUser()
								+ " "
								+ searchParcel.getRegisteredUserCityCodeUser());
				req.setAttribute("timeSend", searchParcel.getParcelSendTime()
						.substring(0, 16));
				req.setAttribute("userName",
						searchParcel.getParcelAddresseeName());
				req.setAttribute("userAddr",
						searchParcel.getParcelAddresseeStreet() + " "
								+ searchParcel.getParcelAddresseeCity() + " "
								+ searchParcel.getParcelAddresseeCityCode());
				req.setAttribute("timeDelivery", searchParcel
						.getParcelSendTime().substring(0, 16));
				req.setAttribute("lastTime", searchParcel.getDelivererTimePos()
						.substring(0, 16));

				if (searchParcel.getDelivererId() > 999000) {
					searchParcel.selectBase(searchParcel.getDelivererId());
					req.setAttribute(
							"msg",
							"Przesylka w bazie "
									+ searchParcel.getCentreNameCentre());
				}
			} else
				req.setAttribute("msg", "Nie mamy takiej przesylki w bazie");
		}
		if (id == null || (Sth.isInteger(id)) == false)
			req.setAttribute("msg", "Prosze podac poprawny numer przesylki");

		req.setAttribute("b", isParcelExists);
		req.removeAttribute("nr");
		view.forward(req, resp);
	};
}
