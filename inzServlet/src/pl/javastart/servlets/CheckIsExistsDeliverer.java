package pl.javastart.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CheckIsExistsDeliverer extends HttpServlet {
	private static final long serialVersionUID = 1L;
	String id;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		try {
			id = req.getParameter("ID");
			System.out.println("dopost" + id);
		} catch (Exception ex) {
			System.out.println("Problem in message reading");
		}
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		PrintWriter out = resp.getWriter();
		boolean is = selectFromDB(id);
		if (is)
			out.println("true");
		else
			out.println("false");
		System.out.println("doget " + id);
	}

	boolean selectFromDB(String id) {
		String strSelect = "";
		int delivererId = -1;
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}
		try (Connection conn = DriverManager.getConnection(
				"jdbc:mysql://localhost:3306/deli", "root", "sun5flower");
				Statement stmt = conn.createStatement();) {
			strSelect = "select * from deli.deliverer where id=" + id;
			// System.out.println("SQL query is: " + strSelect);

			ResultSet rset = stmt.executeQuery(strSelect);
			// System.out.println("the records selected are:");

			while (rset.next()) {
				delivererId = rset.getInt("id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if (delivererId > 0)
			return true;
		return false;
	}
}
