package bc.utils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;


/**
 * utility for execute query GET/POST and read response
 */
@SuppressWarnings("deprecation")
public class RestUtils {
	private final static Logger LOGGER=Logger.getLogger(RestUtils.class);

	public static String getStringPostRequest(String url, String parameterName, String value) {
		CloseableHttpClient httpClient = new DefaultHttpClient();
		try {
			HttpPost httpPostRequest = new HttpPost(url);
			List<NameValuePair> parameters=new ArrayList<NameValuePair>();
			parameters.add(new BasicNameValuePair(parameterName, value));
			httpPostRequest.setEntity(new UrlEncodedFormEntity(parameters));
			HttpResponse httpResponse = httpClient.execute(httpPostRequest);
			if(isPositive(httpResponse)){
				return EntityUtils.toString(httpResponse.getEntity());
			}else{
				return null;
			}
		} catch (IOException e) {
			LOGGER.warn("can't execute POST request:"+url);
			return null;
		} finally {
			closeQuietly(httpClient);
		}
	}
	
	public static String getString(String url) {
		CloseableHttpClient httpClient = new DefaultHttpClient();
		try {
			HttpGet httpGetRequest = new HttpGet(url);
			HttpResponse httpResponse = httpClient.execute(httpGetRequest);
			if(isPositive(httpResponse)){
				return EntityUtils.toString(httpResponse.getEntity(), Charset.forName("UTF-8"));
				// return EntityUtils.toString(httpResponse.getEntity());
			}else{
				return null;
			}
		} catch (IOException e) {
			LOGGER.warn("can't execute GET request:"+url);
			return null;
		} finally {
			closeQuietly(httpClient);
		}
	}
	
	private static boolean isPositive(HttpResponse response){
		return response.getStatusLine().getStatusCode()>=200 && response.getStatusLine().getStatusCode()<300;
	}
	
	private static void closeQuietly(CloseableHttpClient httpClient){
		if(httpClient!=null){
			try {
				httpClient.close();
			} catch (IOException e) {
			}
		}
	}

}
