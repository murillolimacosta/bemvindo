package util;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import play.mvc.Controller;

public class Utils extends Controller {
	public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm";

	public static String formatToMoney(double price) {
		NumberFormat formatter = new DecimalFormat("R$ #0.00");
		return formatter.format(price);
	}

	public static String formatDate(Date postedAt) {
		String format = "dd/MM/yyyy HH'h'mm";
		SimpleDateFormat formatas = new SimpleDateFormat(format);
		String formattedDate = formatas.format(postedAt);
		return formattedDate;
	}

	public static String formatDateSimple(Date postedAt) {
		String format = "dd/MM/yyyy";
		SimpleDateFormat formatas = new SimpleDateFormat(format);
		String formattedDate = formatas.format(postedAt);
		return formattedDate;
	}

	public static boolean validateEmail(String email) {
		validation.email(email);
		if (!validation.hasErrors()) {
			return true;
		}
		return false;
	}

	public static String replaceSpace(String name) {
		return name.replace(" ", "-");
	}

	public static boolean isNullOrEmpty(String text) {
		if (text == null || text.equals(" ") || text.equals("")) {
			return true;
		}
		return false;
	}

	public static boolean isNullOrZero(Long text) {
		if (text == null || text == 0) {
			return true;
		}
		return false;
	}

	public static String replaceBoolean(boolean value) {
		if (value == true)
			return "Sim";
		else
			return "Não";

	}

	public static String timeHourNow() {
		return new SimpleDateFormat("yyyyMMddHHmm").format(new Date());
	}

	public static String splitSpacesAndLimitSubstring(String str, int limit) {
		String f[] = str.split(" ");
		String finalStr = "";
		for (String string : f) {
			if (!Utils.isNullOrEmpty(string)) {
				finalStr = finalStr + string;
			}
		}
		return finalStr.substring(0, limit);
	}

	public static String split(String regex, String str) {
		String f[] = str.split(regex);
		String finalStr = "";
		for (String string : f) {
			if (!Utils.isNullOrEmpty(string)) {
				finalStr = finalStr + string;
			}
		}
		return finalStr.trim();
	}

	public static boolean validateCPFOrCNPJ(String text) {
		if (Utils.isNullOrEmpty(text)) {
			return false;
		}
		String str = text.trim();
		str = str.replace(".", "");
		str = str.replace("-", "");
		str = str.replace("/", "");
		if (str.length() == 11) {
			if (CPFCNPJ.isValidCPF(str))
				return true;
		} else if (str.length() == 14) {
			if (CPFCNPJ.isValidCNPJ(str))
				return true;
		}
		return false;
	}

	public static String substringText(String text, int limitMinimum, int limitMaximum) {
		String string = null;
		if (!isNullOrEmpty(text) && text.length() > limitMaximum) {
			string = text.substring(limitMinimum, limitMaximum - 3);
			return string + "...";
		}
		return text;
	}

	public static Date parseDate(String date, String format) throws ParseException {
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		return formatter.parse(date);
	}

	public static String getCurrentDateTime() {
		DateFormat dateFormat = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
		Calendar cal = Calendar.getInstance();
		return dateFormat.format(cal.getTime());
	}

	public static void main(String[] args) {
		for (int i = 0; i < 1000; i++) {
			System.out.println(randomKey());
		}
	}

	public static String randomKey() {
		return UUID.randomUUID().toString();
	}
	
}
