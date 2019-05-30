package bc.payment.sberbank.service;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.simpleframework.xml.convert.Converter;
import org.simpleframework.xml.stream.InputNode;
import org.simpleframework.xml.stream.OutputNode;

public class SberbankDateConverter implements Converter<Date>{
	private final static String DATE_FORMAT="yyyy-MM-dd'T'HH:mm:ss";
	
	@Override
	public Date read(InputNode node) throws Exception {
		String textRepresentation=node.getValue();
		return new SimpleDateFormat(DATE_FORMAT).parse(textRepresentation);
	}

	@Override
	public void write(OutputNode node, Date value) throws Exception {
		node.setValue(new SimpleDateFormat(DATE_FORMAT).format(value));
	}


}
