package bc.payment.income.utility;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

import org.apache.camel.Exchange;
import org.apache.commons.io.IOUtils;

import bc.payment.income.exception.InputParameterException;

public class Body2String {

	public static String convertBody(Exchange exchange) throws InputParameterException{
		// HttpMessage
		Object body=exchange.getIn().getBody();
		if(body==null || !(body instanceof InputStream) ){
			return null;
		}
		StringWriter writer=new StringWriter();
		try {
			IOUtils.copy((InputStream)body, writer);
		} catch (IOException e) {
			throw new InputParameterException("read data exception: "+e.getMessage());
			
		}
		return writer.toString();
	}
	
}
