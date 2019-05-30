package bc.util;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;



/**
 * utility for execute query GET/POST and read response
 */
public class RestUtils {
	private final static Logger LOGGER=Logger.getLogger(RestUtils.class);
	private final static int CONNECT_TIMEOUT=2000;
	private final static int REQUEST_TIMEOUT=2000;
	
	public static String getString(String url) {
		CloseableHttpClient httpClient=buildClient();
		
		try {
			HttpGet httpGetRequest = new HttpGet(url);
			HttpResponse httpResponse = httpClient.execute(httpGetRequest);
			if(isPositive(httpResponse)){
				return EntityUtils.toString(httpResponse.getEntity());
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
	
	private static CloseableHttpClient buildClient(){
		HttpClientBuilder clientBuilder = HttpClientBuilder.create();
		RequestConfig.Builder requestBuilder = RequestConfig.custom();
		requestBuilder = requestBuilder.setConnectTimeout(CONNECT_TIMEOUT);
		requestBuilder = requestBuilder.setConnectionRequestTimeout(REQUEST_TIMEOUT);
		clientBuilder.setDefaultRequestConfig(requestBuilder.build());
		CloseableHttpClient httpClient = clientBuilder.build();
		return httpClient;
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
