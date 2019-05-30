package bc.payment.sberbank.service;

import java.io.StringWriter;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.Logger;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.convert.AnnotationStrategy;
import org.simpleframework.xml.core.Persister;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import bc.payment.exception.MarshallingException;

@Component
public class MarshallerSberbank {
	private final static String XML_HEADER="<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
	private final static Logger LOGGER=Logger.getLogger(MarshallerSberbank.class);

	/**
	 * <documentation> It is fully thread safe and can be shared by multiple threads without concerns. </documentation>
	 */
	private static Serializer serializer = new Persister(new AnnotationStrategy());
	// serializer= new Persister(new RegistryStrategy(new Registry().bind(Date.class, SberbankDateConverter.class)));


	public static String toXmlString(Object value) {
		StringWriter writer=new StringWriter();
		writer.write(MarshallerSberbank.XML_HEADER);
		try {
			MarshallerSberbank.serializer.write(value, writer);
			return writer.toString();
		} catch (Exception e) {
			MarshallerSberbank.LOGGER.error("can't convert object to String", e);
			return null;
		}
	}

	public static <T> T getFromXmlString(String xmlRepresentation, Class<T> clazz) throws MarshallingException{
		try {
			return MarshallerSberbank.serializer.read(clazz, xmlRepresentation);
		} catch (Exception e) {
			MarshallerSberbank.LOGGER.error("attempt to read data from xml:"+xmlRepresentation, e);
			throw new MarshallingException("attempt to read data from xml:"+xmlRepresentation, e);
		}
	}

	public static String getXmlHeader(){
		return MarshallerSberbank.XML_HEADER;
	}

	public static String toXmlStringWithoutHeader(Object value) {
		StringWriter writer=new StringWriter();
		try {
			MarshallerSberbank.serializer.write(value, writer);
			return writer.toString();
		} catch (Exception e) {
			MarshallerSberbank.LOGGER.error("can't convert object to String", e);
			return null;
		}
	}

	private static String PASSWORD="password";

	/** set value from external source - context.xml of the application */
	@Value("${external-payment.sberbank.password}")
	public void setPassword(String password){
		MarshallerSberbank.PASSWORD=password;
	}

	public String getPassword(){
		return MarshallerSberbank.PASSWORD;
	}

	public static String calculateMd5(String params) {
		// TODO - check MD5 logic - ask Sberbank for this
		return DigestUtils.md5Hex(params+MarshallerSberbank.PASSWORD);
	}

}
