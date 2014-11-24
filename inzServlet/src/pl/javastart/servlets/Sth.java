package pl.javastart.servlets;


public class Sth {

	public static boolean isInteger(String _id) {
		try {
			Integer.parseInt(_id);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}

}
