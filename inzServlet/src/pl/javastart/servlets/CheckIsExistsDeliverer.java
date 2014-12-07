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
	String activ;
	String logout;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		try {
			id = req.getParameter("ID");
			activ = req.getParameter("activ");
			logout = req.getParameter("logout");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		PrintWriter out = resp.getWriter();
		String tmp = selectFromDB(id);
		out.println(tmp);
	}

	String selectFromDB(String id) {
		String sqlInsert = "";
		String stringSelect = "";
		int delivererId = -1;
		boolean delivererActiv = false;
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		try {
			Connection connection = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/deli", "root", "sun5flower");

			Statement statement = connection.createStatement();
			stringSelect = "select * from deli.deliverer where id=" + id;
			ResultSet resultSet = statement.executeQuery(stringSelect);
			while (resultSet.next()) {
				delivererId = resultSet.getInt("id");
				delivererActiv = resultSet.getBoolean("activ");
			}

			if (delivererId > 0) {
				if (delivererActiv == false) {
					sqlInsert = " update deli.deliverer set activ=1 where id="
							+ id;
					statement.executeUpdate(sqlInsert);
					connection.close();
					statement.close();
					return "logIn";
				} else if (delivererActiv == true) {

					if (logout.equals("0")) {
						sqlInsert = " update deli.deliverer set activ=0 where id="
								+ id;
						statement.executeUpdate(sqlInsert);
						connection.close();
						statement.close();
						return "logOut";
					} else if (logout.equals("1")) {
						connection.close();
						statement.close();
						return "busy";
					}
				}
			} else
				return "false";
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "";
	}
}
