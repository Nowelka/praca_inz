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
			System.out.println("dopost " + id + "  " + activ + "  " + logout);
		} catch (Exception ex) {
			System.out.println("Problem in message reading");
		}
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		PrintWriter out = resp.getWriter();
		String tmp = selectFromDB(id);
		out.println(tmp);
		System.out.println("doget " + id + "  " + tmp);
	}

	String selectFromDB(String id) {
		String sqlInsert = "";
		String strSelect = "";
		int delivererId = -1;
		boolean delivererActiv = false;
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}
		Connection conn;
		try {
			conn = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/deli", "root", "sun5flower");

			Statement stmt = conn.createStatement();
			strSelect = "select * from deli.deliverer where id=" + id;
			ResultSet rset = stmt.executeQuery(strSelect);
			System.out.println(rset.toString() + "  " + strSelect);
			while (rset.next()) {

				delivererId = rset.getInt("id");
				delivererActiv = rset.getBoolean("activ");
				System.out.println(delivererId + "  " + delivererActiv);
			}

			if (delivererId > 0) {
				if (delivererActiv == false) {
					sqlInsert = " update deli.deliverer set activ=" + activ
							+ " where id=" + id;
					System.out.println("CHECK commend " + sqlInsert);
					stmt.executeUpdate(sqlInsert);
					conn.close();
					stmt.close();
					return "logIn";
				} else if (delivererActiv == true) {

					if (logout.equals("0")) {
						sqlInsert = " update deli.deliverer set activ=0 where id="
								+ id;
						System.out.println("CHECK commend " + sqlInsert);
						stmt.executeUpdate(sqlInsert);
						conn.close();
						stmt.close();
						return "logOut";
					} else if (logout.equals("1")) {
						conn.close();
						stmt.close();
						return "busy";
					}
				}
			} else
				return "false";
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";

	}
}
