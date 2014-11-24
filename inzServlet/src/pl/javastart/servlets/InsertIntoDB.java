package pl.javastart.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class InsertIntoDB extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private String id;
	private String longitude;
	private String latitude;
	private String date;
	private String activ;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		try {
			id = req.getParameter("ID");
			longitude = req.getParameter("longitude");
			latitude = req.getParameter("latitude");
			date = req.getParameter("timestamp");
			activ = req.getParameter("activ");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		insertIntoJDBC();
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.setContentType("text/html;charset=UTF-8");

		PrintWriter out = resp.getWriter();
		out.write(id + " " + longitude + " " + latitude + " " + date + " "
				+ activ);

	}

	public boolean insertIntoJDBC() {
		Connection conn = null;
		Statement stmt = null;
		String sqlInsert = "";
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}
		try {
			conn = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/deli", "root", "sun5flower");
			stmt = conn.createStatement();
			sqlInsert = "insert into deli.deliverer (id,latitude,longitude,timePos, activ) values"
					+ "("
					+ id
					+ ","
					+ latitude
					+ ","
					+ longitude
					+ ",'"
					+ date
					+ "',"
					+ activ
					+ ") on duplicate key update latitude=values(latitude),"
					+ " longitude=values(longitude), timePos=values(timePos), activ=values(activ)";
			stmt.executeUpdate(sqlInsert);
			conn.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean insertOrderIntoJDBC(String sName, String sStreet,
			String sCity, String sCityCode, String aName, String aStreet,
			String aCity, String aCityCode) {
		Connection conn = null;
		Statement stmt = null;
		String sqlInsert = "";
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}
		try {
			conn = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/deli", "root", "sun5flower");
			stmt = conn.createStatement();
			sqlInsert = "insert into deli.registeredUser (nameUser,streetUser,cityUser,cityCodeUser) values"
					+ "("
					+ sName
					+ ","
					+ sStreet
					+ ","
					+ sCity
					+ ","
					+ sCityCode
					+ ") on duplicate key update nameUser=values(nameUser),"
					+ " streetUser=values(streetUser), cityUser=values(cityUser), cityCodeUser=values(cityCodeUser);";
			stmt.executeUpdate(sqlInsert);
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date = new Date(System.currentTimeMillis());
			sqlInsert = "insert into deli.parcel (id,fromUserId,addresseeName,addresseeStreet,addresseeCity,addresseeCityCode, sendTime, deliverer, base) values"
					+ "(null,last_insert_id(),"
					+ aName
					+ ","
					+ aStreet
					+ ","
					+ aCity
					+ ","
					+ aCityCode
					+ ",'"
					+ df.format(date)
					+ "',0,0);";
			stmt.executeUpdate(sqlInsert);
			conn.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
}