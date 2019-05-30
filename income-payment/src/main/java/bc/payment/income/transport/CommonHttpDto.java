package bc.payment.income.transport;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.camel.Exchange;
import org.apache.camel.component.http.HttpMessage;
import org.springframework.util.StringUtils;

import bc.payment.income.exception.InputParameterException;
import bc.payment.income.utility.Body2String;

public class CommonHttpDto {
	Map<String, String[]> parameters;
	Map<String, String> headers;
	String body;
	private HttpMessage originalMessage;
	private String method;

	public CommonHttpDto(){
	}
	
	public Map<String, String[]> getParameters() {
		return parameters;
	}

	public Map<String, String> getHeaders() {
		return headers;
	}
	public String getBody() {
		return body;
	}
	
	public HttpMessage getOriginalMessage() {
		return originalMessage;
	}

	public String getMethod() {
		return method;
	}
	
	
	private final static String METHOD_GET="GET";
		
	public boolean isGetMethod(){
		return METHOD_GET.equals(this.method);
	}
	
	private final static String METHOD_POST="POST";
	
	public boolean isPostMethod(){
		return METHOD_POST.equals(this.method);
	}

	public static CommonHttpDto read(Exchange exchange) throws InputParameterException{
		if(!(exchange.getIn() instanceof HttpMessage)){
			// return empty object
			return new CommonHttpDto();
		}
		HttpMessage message=(HttpMessage)exchange.getIn();
		CommonHttpDto returnValue=new CommonHttpDto();
		
		returnValue.body=Body2String.convertBody(exchange);
		returnValue.parameters=new HashMap<String, String[]>(message.getRequest().getParameterMap());
		returnValue.headers=readHeaders(message.getRequest());
		returnValue.originalMessage=message;
		returnValue.method=StringUtils.trimWhitespace(message.getRequest().getMethod()).toUpperCase();
		
		return returnValue;
	}

	private static Map<String, String> readHeaders(HttpServletRequest request) {
		Map<String, String> returnValue=new HashMap<String, String>();
		Enumeration<String> headersEnumeration=request.getHeaderNames();
		while(headersEnumeration.hasMoreElements()){
			String nextHeader=headersEnumeration.nextElement();
			returnValue.put(nextHeader, request.getHeader(nextHeader));
		}
		return returnValue;
	}
	
}
