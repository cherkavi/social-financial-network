package bc.payment.citypay.service;

import java.io.StringWriter;

import org.apache.log4j.Logger;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.convert.AnnotationStrategy;
import org.simpleframework.xml.core.Persister;

public class MarshallerCityPay {
	private final static String XML_HEADER="<?xml version=\"1.0\" encoding=\"UTF-8\"?>";	
	private final static Logger LOGGER=Logger.getLogger(MarshallerCityPay.class);

	/**
	 * <documentation> It is fully thread safe and can be shared by multiple threads without concerns. </documentation> 
	 */
	private static Serializer serializer= new Persister(new AnnotationStrategy());
	// serializer= new Persister(new RegistryStrategy(new Registry().bind(Date.class, CityPayDateConverter.class)));
	
	public static String toXmlString(Object value) {
		StringWriter writer=new StringWriter();
		writer.write(XML_HEADER);
		try {
			serializer.write(value, writer);
			return writer.toString();
		} catch (Exception e) {
			LOGGER.error("can't convert object to String", e);
			return null;
		}
	}

}
