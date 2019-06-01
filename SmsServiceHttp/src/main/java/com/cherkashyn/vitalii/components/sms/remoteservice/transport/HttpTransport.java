package com.cherkashyn.vitalii.components.sms.remoteservice.transport;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;

import org.apache.commons.io.IOUtils;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import com.cherkashyn.vitalii.components.sms.remoteservice.client.Constants;

public class HttpTransport {
	
	final CloseableHttpClient client;
	
	public HttpTransport(){
		client = HttpClients.createMinimal();
	}
	
	@Override
	protected void finalize() throws Throwable {
		this.client.close();
	}
	
	/**
	 * send data to remote host 
	 * @param url - http resource
	 * @param xmlRequest - xml representation of object
	 * @return
	 */
	public String sendData(String url, String xmlRequest) throws TransportException{
		// prepare POST request
		HttpEntityEnclosingRequestBase request = new HttpPost(url);
		try {
			request.setEntity(new StringEntity(xmlRequest));
		} catch (UnsupportedEncodingException e) {
			throw new TransportException("can't add entity as POST body", e);
		}
		// request
		CloseableHttpResponse response=null;
		try {
			response = client.execute(request);
		} catch (ClientProtocolException e) {
			throw new TransportException(e);
		} catch (IOException e) {
			throw new TransportException(e);
		}
		// parse response
		try {
			// check response for OK
			if(isResponseOk(response)){
				return getStringFromResponse(response);
			}else{
				throw new TransportException("can't execute request to remote server:"+response.getStatusLine().getStatusCode());
			}
		} finally {
		    try {
		    	if(response!=null){
		    		response.close();
		    	}
			} catch (IOException e) {
			}
		}		
	}

	private String getStringFromResponse(CloseableHttpResponse response) throws TransportException{
		InputStream input=null;
		try {
			input=response.getEntity().getContent();
			StringWriter writer=new StringWriter();
			IOUtils.copy(input, writer);
			return writer.toString();
		} catch (IOException e) {
			throw new TransportException("can't read data from response", e);
		} finally{
			IOUtils.closeQuietly(input);
		}
	}

	private boolean isResponseOk(CloseableHttpResponse response) {
		int statusCode=response.getStatusLine().getStatusCode();
		return statusCode>=200 && statusCode<300; 
	}

	/**
	 * send data to remote host
	 * @param xmlRequest
	 * @return remote service response
	 */
	public String sendData(String xmlRequest) throws TransportException{
		return sendData(Constants.URL, xmlRequest);
	}
	
}
