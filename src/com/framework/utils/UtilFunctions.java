package com.framework.utils;

import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Scanner;
import java.util.TimeZone;

/**
 * 
 * @author Shevtcov
 */
public class UtilFunctions {

    public static void main(String[] args) throws UnknownHostException {
	// System.out.println(checkInternetConnection());
	getIP("nikolay-pc");
    }

    public static String getIP(String hostName) throws UnknownHostException {
	InetAddress ipaddress = InetAddress.getByName(hostName);
	return ipaddress.getHostAddress();
    }

    /**
     * 
     * @param begin
     * @param end
     * @param format
     * @return
     */
    public static String TimeBetween(Date begin, Date end, String format) {
	Locale local = new Locale("ru", "RU");
	SimpleDateFormat sdf = new SimpleDateFormat(format, local);
	sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
	Date total = new Date(end.getTime() - begin.getTime());
	return sdf.format(total);
    }

    public static String TimeBetween(Date begin, Date end) {
	return TimeBetween(begin, end, "HH:mm:ss.SSS");
    }

    public static boolean checkInternetConnection() {
	Boolean result = false;
	HttpURLConnection con = null;
	try {
	    // HttpURLConnection.setFollowRedirects(false);
	    // HttpURLConnection.setInstanceFollowRedirects(false)
	    con = (HttpURLConnection) new URL("http://www.google.com").openConnection();
	    con.setRequestMethod("HEAD");
	    result = (con.getResponseCode() == HttpURLConnection.HTTP_OK);
	} catch (Exception e) {
	    System.out.println("no internet connection detected");
	} finally {
	    if (con != null) {
		try {
		    con.disconnect();
		} catch (Exception e) {
		    System.out.println("no internet connection detected");
		}
	    }
	}
	return result;
    }

    public static void sleep(long secs) {
	try {
	    System.out.println("sleep " + secs + " secs");
	    Thread.sleep(secs * 1000);
	} catch (InterruptedException e) {
	    System.out.println("sleep of " + secs + " was interupted");
	}
    }

    public static boolean AskYesNoQuestion(String question) {
	Scanner in = new Scanner(System.in);
	while (true) {
	    System.out.print(question + "[yes,no]");
	    String answer = in.nextLine();
	    if (answer.equalsIgnoreCase("yes") || answer.equalsIgnoreCase("no")) {
		in.close();
		return (answer.equalsIgnoreCase("yes"));
	    } else {
		System.out.println("please choose [yes,no]");
		in.close();
	    }
	}
    }

    /***
     * Сравнивает строки example и str. Сравнивает посимвольно, если символы не
     * совпадают, то в строке str берётся следующий(например str =
     * "01.02.2014 12:30", example = "010220141230")
     * 
     * @param example
     * @param str
     * @return
     */
    public static boolean StringsAreTheSame(String example, String str) {
	if (str.length() < example.length()) {
	    return false;
	}

	for (int i = 0, j = 0; i < example.length() && j < str.length(); i++, j++) {
	    if (example.charAt(i) != str.charAt(j)) {
		i--;
	    } else if (i == example.length() - 1) {
		return true;
	    }
	}
	return false;
    }
}
