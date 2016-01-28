package ws;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import play.Play;

public class ConnectionService {

	public static String openConnection(String json, String urlConnection) throws IOException {
		URL url = new URL(urlConnection);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("POST");
		conn.setInstanceFollowRedirects(false);
		conn.setRequestProperty("Content-Type", "application/json;charset=utf-8");
		conn.setRequestProperty("Cache-Control", "no-store,max-age=0,no-cache");
		conn.setRequestProperty("Pragma", "no-cache");
		conn.setDoInput(true);
		conn.setUseCaches(false);
		conn.setRequestProperty("Accept", "application/json");
		int length = json.getBytes().length;
		if (length > 0) {
			conn.setDoOutput(true);
			conn.setRequestProperty("Content-Length", "" + Integer.toString(length));
			DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
			wr.writeBytes(json);
			wr.flush();
			wr.close();
		}
		if (conn.getResponseCode() != 200) {
			throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
		}
		BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
		String output;
		while ((output = br.readLine()) != null) {
			return output;
		}
		conn.disconnect();
		return output;
	}
}
