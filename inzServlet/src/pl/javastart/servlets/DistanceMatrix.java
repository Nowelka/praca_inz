package pl.javastart.servlets;

import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class DistanceMatrix {
	float lat = 0, lng = 0;
	public double odlegloscPomiedzyDwomaPunktami = 0.0;
	public int czasPomiedzyDwomaPunktami = 0;
	public boolean statusPomiedzyDwomaPuntmi = false;

	public DistanceMatrix() {
	}

	public Double wyliczOdlegloscDistanceMatrix(String ulNadania,
			String miastoNadania, String ulOdbioru, String miastoOdbioru) {
		System.out.println(ulNadania + " " + miastoNadania + " <> " + ulOdbioru
				+ " " + miastoOdbioru);
		String urlPath = "";
		try {
			urlPath = "http://maps.googleapis.com/maps/api/distancematrix/xml?origins="
					+ URLEncoder.encode(ulNadania, "UTF-8")
					+ "+"
					+ URLEncoder.encode(miastoNadania, "UTF-8")
					+ "&destinations="
					+ URLEncoder.encode(ulOdbioru, "UTF-8")
					+ "+"
					+ URLEncoder.encode(miastoOdbioru, "UTF-8")
					+ "&mode=driving&language=pl-PL&sensor=false";
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		Document document = null;
		try {
			document = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder().parse(new URL(urlPath).openStream());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		String str = toString(document);
		System.out.println(str);
		String tmp = str.substring(str.indexOf("status>") + "status>".length(),
				str.indexOf("</status"));

		if (tmp.equals("OK")) {
			statusPomiedzyDwomaPuntmi = true;
			odlegloscPomiedzyDwomaPunktami = Double.parseDouble(str.substring(
					str.indexOf("distance>") + 21, str.lastIndexOf("</val")));
			czasPomiedzyDwomaPunktami = Integer.parseInt(str.substring(
					str.indexOf("duration>") + 21, str.indexOf("</val")));
		} else
			statusPomiedzyDwomaPuntmi = false;
		return 0.0;
	}

	public String wyliczCzasDostarczenia(Date sendTime) {
		int days = 1;

		// zamiana inta na date
		String datee = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
				.format(new Date(czasPomiedzyDwomaPunktami * 1000L));
		//
		// pobranie godziny z daty
		System.out.println();
		Calendar calendar = GregorianCalendar.getInstance();
		calendar.setTime(sendTime);
		System.out.println(sendTime + "   "
				+ calendar.get(Calendar.HOUR_OF_DAY));
		//
		// ustawienie dni dostarczania
		// godzina nadania po 18
		if (calendar.get(Calendar.HOUR_OF_DAY) > 18) {
			calendar.set(Calendar.HOUR_OF_DAY, 11);
			days++;
		}
		// godzina nadania jest pomiedzy 0 a 12
		if (calendar.get(Calendar.HOUR_OF_DAY) > 0
				&& calendar.get(Calendar.HOUR_OF_DAY) < 12) {
			calendar.set(Calendar.HOUR_OF_DAY, 15);
		}
		// czas pomiedzy dwoma puntami mniejszy niż 1,5h
		if (czasPomiedzyDwomaPunktami < 1000) {
			days--;
		}
		// czas pomiedzy punktami wiecej niz 6,5h
		if (czasPomiedzyDwomaPunktami > 20000) {
			days++;
		}

		System.out.println(datee + "  dnie " + days);
		System.out.println(odlegloscPomiedzyDwomaPunktami + "  "
				+ czasPomiedzyDwomaPunktami);

		calendar.add(Calendar.DAY_OF_MONTH, days);
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(calendar
				.getTime());
	}

	public static String toString(Document doc) {
		try {
			StringWriter sw = new StringWriter();
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer transformer = tf.newTransformer();
			transformer
					.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
			transformer.setOutputProperty(OutputKeys.METHOD, "xml");
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

			transformer.transform(new DOMSource(doc), new StreamResult(sw));
			return sw.toString();
		} catch (Exception ex) {
			throw new RuntimeException("Error converting to String", ex);
		}
	}
}
