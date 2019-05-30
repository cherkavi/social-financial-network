package bc.payment.sberbank.report.parser;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.simpleframework.xml.convert.Converter;
import org.simpleframework.xml.stream.InputNode;
import org.simpleframework.xml.stream.OutputNode;


public class DateConverter implements Converter<Date>{
	// 2011-05-13 12:00:00
	public final static String DATE_FORMAT="yyyy-MM-dd";

	@Override
	public Date read(InputNode node) throws Exception {
		String textRepresentation=node.getValue();
		return new SimpleDateFormat(DateConverter.DATE_FORMAT).parse(textRepresentation);
	}

	@Override
	public void write(OutputNode node, Date value) throws Exception {
		node.setValue(new SimpleDateFormat(DateConverter.DATE_FORMAT).format(value));
	}

}
