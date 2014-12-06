package pl.javastart.servlets;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Zamowienie extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public String nrPracel = "";

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		RequestDispatcher view = req.getRequestDispatcher("/zamowienie.jsp");
		view.forward(req, resp);
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		RequestDispatcher view = req.getRequestDispatcher("/zamowienie.jsp");

		InsertIntoDB insert = new InsertIntoDB();
		nrPracel = insert.insertOrderIntoJDBC(req.getParameter("sName"),
				req.getParameter("sStreet"), req.getParameter("sCity"),
				req.getParameter("sCityCode"), req.getParameter("aName"),
				req.getParameter("aStreet"), req.getParameter("aCity"),
				req.getParameter("aCityCode"));

		req.setAttribute("nrPracel", nrPracel);
		view.forward(req, resp);

	};

}
