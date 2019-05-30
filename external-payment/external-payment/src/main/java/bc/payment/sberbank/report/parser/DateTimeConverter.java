package bc.payment.sberbank.report.parser;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.simpleframework.xml.convert.Converter;
import org.simpleframework.xml.stream.InputNode;
import org.simpleframework.xml.stream.OutputNode;


public class DateTimeConverter implements Converter<Date>{
	// 2011-05-13 12:00:00
	public final static String DATETIME_FORMAT="yyyy-MM-dd HH:mm:ss";

	@Override
	public Date read(InputNode node) throws Exception {
		String textRepresentation=node.getValue();
		return new SimpleDateFormat(DateTimeConverter.DATETIME_FORMAT).parse(textRepresentation);
	}

	@Override
	public void write(OutputNode node, Date value) throws Exception {
		node.setValue(new SimpleDateFormat(DateTimeConverter.DATETIME_FORMAT).format(value));
	}

}
