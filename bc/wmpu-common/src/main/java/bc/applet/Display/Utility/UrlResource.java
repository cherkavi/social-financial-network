package bc.applet.Display.Utility;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.commons.io.IOUtils;

public class UrlResource {

	/**
	 * читает URL и возвращает данные в виде строки
	 * 
	 * @param url_path
	 *            полный путь к URL данные из которого нужно получить
	 * @param key
	 *            - имена параметров
	 * @param value
	 *            - значения параметров
	 */
	public static String get_http_text(String url_path, String[] key,
			String[] value) throws Exception {
		StringBuilder return_value = new StringBuilder();
		URL url = new URL(url_path);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setDoInput(true);
		connection.setDoOutput(true);
		connection.setRequestMethod("GET");

		// создание переменных запроса
		StringBuilder data = new StringBuilder();
		for (int counter = 0; counter < key.length; counter++) {
			if (counter == 0) {
				data.append(key[counter] + "="
						+ value[counter].replaceAll(" ", "%20"));
			} else {
				data.append("&" + key[counter] + "="
						+ value[counter].replaceAll(" ", "%20"));
			}
		}

		Writer output=null;
		InputStreamReader isr = null;
		try{
			output = new OutputStreamWriter(
					connection.getOutputStream());
			output.write(data.toString());
			output.flush();
			connection.connect();
			
			isr = new InputStreamReader(connection.getInputStream());
			BufferedReader br = new BufferedReader(isr);
			String current_string;
			while ((current_string = br.readLine()) != null) {
				return_value.append(current_string);
			}
		}finally{
			IOUtils.closeQuietly(output);
			IOUtils.closeQuietly(isr);
		}
		return return_value.toString();
	}

}
