package pl.javastart.servlets;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SearchParcel {

	private boolean isParcelExists = false;

	private int parcelId;
	private int parcelFromUserId;
	private String parcelAddresseeName;
	private String parcelAddresseeStreet;
	private String parcelAddresseeCity;
	private String parcelAddresseeCityCode;
	private String parcelSendTime;
	private String parcelTimePos;
	private String parcelDeliveryTime;
	private int parcelDeliverer;

	private int delivererId;
	private double delivererLatitude;
	private double delivererLongitude;
	private String delivererTimePos;

	private int registeredUserId;
	private String registeredUserNameUser;
	private String registeredUserStreetUser;
	private String registeredUserCityUser;
	private String registeredUserCityCodeUser;

	private String centreNameCentre;

	public SearchParcel(int _nrParcel) {
		selectFromDB(_nrParcel);
	}

	void selectFromDB(int nrParcel) {
		String strSelect = "";
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		// odnalezienie przesylki o id=nrParcel
		try {
			Connection connection = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/deli", "root", "sun5flower");
			Statement statement = connection.createStatement();
			strSelect = "select * from deli.parcel where id=" + nrParcel;
			ResultSet resultSet = statement.executeQuery(strSelect);
			while (resultSet.next()) {
				isParcelExists = true;
				parcelId = resultSet.getInt("id");
				parcelFromUserId = resultSet.getInt("fromUserId");
				parcelAddresseeName = resultSet.getString("addresseeName");
				parcelAddresseeStreet = resultSet.getString("addresseeStreet");
				parcelAddresseeCity = resultSet.getString("addresseeCity");
				parcelAddresseeCityCode = resultSet
						.getString("addresseeCityCode");
				parcelSendTime = resultSet.getString("sendTime");
				parcelTimePos = resultSet.getString("timePos");
				parcelDeliveryTime = resultSet.getString("deliveryTime");
				parcelDeliverer = resultSet.getInt("deliverer");
			}

			// jesli przesylka istnieje
			if (isParcelExists == true) {
				// odnalezienie dostawcy ktory ma w swoim posiadaniu przesylke
				// nrParcel
				strSelect = "select * from deli.deliverer where id="
						+ parcelDeliverer;
				resultSet = statement.executeQuery(strSelect);
				while (resultSet.next()) {
					delivererId = resultSet.getInt("id");
					delivererLatitude = resultSet.getDouble("latitude");
					delivererLongitude = resultSet.getDouble("longitude");
					delivererTimePos = resultSet.getString("timePos");
				}
				// odnalezienie nadawcy przesylki nrParcel
				strSelect = "select * from deli.registeredUser where id="
						+ parcelFromUserId;
				resultSet = statement.executeQuery(strSelect);
				while (resultSet.next()) {
					registeredUserId = resultSet.getInt("id");
					registeredUserNameUser = resultSet.getString("nameUser");
					registeredUserStreetUser = resultSet
							.getString("streetUser");
					registeredUserCityUser = resultSet.getString("cityUser");
					registeredUserCityCodeUser = resultSet
							.getString("cityCodeUser");
				}
			}
			connection.close();
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public void selectBase(int _nrBase) {
		String strSelect = "";
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		// odnalezienie przesylki o nrParcel
		try {
			Connection connection = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/deli", "root", "sun5flower");
			Statement statement = connection.createStatement();
			strSelect = "select * from deli.centre where id=" + _nrBase;
			ResultSet resultSet = statement.executeQuery(strSelect);
			while (resultSet.next()) {
				centreNameCentre = resultSet.getString("nameCentre");
			}
			connection.close();
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// //
	// getters & setters
	// //
	public int getParcelId() {
		return parcelId;
	}

	public void setParcelId(int parcelId) {
		this.parcelId = parcelId;
	}

	public int getParcelFromUserId() {
		return parcelFromUserId;
	}

	public void setParcelFromUserId(int parcelFromUserId) {
		this.parcelFromUserId = parcelFromUserId;
	}

	public String getParcelAddresseeName() {
		return parcelAddresseeName;
	}

	public void setParcelAddresseeName(String parcelAddresseeName) {
		this.parcelAddresseeName = parcelAddresseeName;
	}

	public String getParcelAddresseeStreet() {
		return parcelAddresseeStreet;
	}

	public void setParcelAddresseeStreet(String parcelAddresseeStreet) {
		this.parcelAddresseeStreet = parcelAddresseeStreet;
	}

	public String getParcelAddresseeCity() {
		return parcelAddresseeCity;
	}

	public void setParcelAddresseeCity(String parcelAddresseeCity) {
		this.parcelAddresseeCity = parcelAddresseeCity;
	}

	public String getParcelAddresseeCityCode() {
		return parcelAddresseeCityCode;
	}

	public void setParcelAddresseeCityCode(String parcelAddresseeCityCode) {
		this.parcelAddresseeCityCode = parcelAddresseeCityCode;
	}

	public String getParcelSendTime() {
		return parcelSendTime;
	}

	public void setParcelSendTime(String parcelSendTime) {
		this.parcelSendTime = parcelSendTime;
	}

	public String getParcelDeliveryTime() {
		return parcelDeliveryTime;
	}

	public void setParcelDeliveryTime(String parcelDeliveryTime) {
		this.parcelDeliveryTime = parcelDeliveryTime;
	}

	public int getParcelDeliverer() {
		return parcelDeliverer;
	}

	public void setParcelDeliverer(int parcelDeliverer) {
		this.parcelDeliverer = parcelDeliverer;
	}

	public int getDelivererId() {
		return delivererId;
	}

	public void setDelivererId(int delivererId) {
		this.delivererId = delivererId;
	}

	public double getDelivererLatitude() {
		return delivererLatitude;
	}

	public void setDelivererLatitude(double delivererLatitude) {
		this.delivererLatitude = delivererLatitude;
	}

	public double getDelivererLongitude() {
		return delivererLongitude;
	}

	public void setDelivererLongitude(double delivererLongitude) {
		this.delivererLongitude = delivererLongitude;
	}

	public String getDelivererTimePos() {
		return delivererTimePos;
	}

	public void setDelivererTimePos(String delivererTimePos) {
		this.delivererTimePos = delivererTimePos;
	}

	public int getRegisteredUserId() {
		return registeredUserId;
	}

	public void setRegisteredUserId(int registeredUserId) {
		this.registeredUserId = registeredUserId;
	}

	public String getRegisteredUserNameUser() {
		return registeredUserNameUser;
	}

	public void setRegisteredUserNameUser(String registeredUserNameUser) {
		this.registeredUserNameUser = registeredUserNameUser;
	}

	public String getRegisteredUserStreetUser() {
		return registeredUserStreetUser;
	}

	public void setRegisteredUserStreetUser(String registeredUserStreetUser) {
		this.registeredUserStreetUser = registeredUserStreetUser;
	}

	public String getRegisteredUserCityUser() {
		return registeredUserCityUser;
	}

	public void setRegisteredUserCityUser(String registeredUserCityUser) {
		this.registeredUserCityUser = registeredUserCityUser;
	}

	public String getRegisteredUserCityCodeUser() {
		return registeredUserCityCodeUser;
	}

	public void setRegisteredUserCityCodeUser(String registeredUserCityCodeUser) {
		this.registeredUserCityCodeUser = registeredUserCityCodeUser;
	}

	public boolean getIsParcelExists() {
		return isParcelExists;
	}

	public void setParcelExists(boolean isParcelExists) {
		this.isParcelExists = isParcelExists;
	}

	public String getCentreNameCentre() {
		return centreNameCentre;
	}

	public void setCentreNameCentre(String centreNameCentre) {
		this.centreNameCentre = centreNameCentre;
	}

	public String getParcelTimePos() {
		return parcelTimePos;
	}

	public void setParcelTimePos(String parcelTimePos) {
		this.parcelTimePos = parcelTimePos;
	}

}
