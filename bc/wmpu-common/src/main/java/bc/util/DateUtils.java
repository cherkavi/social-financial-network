package bc.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

public class DateUtils {
	private DateUtils(){
	}
	
	public final static String DATE_FORMAT_DEFAULT="DD.MM.yyyy";
	
	public static Date parseDate(String date){
		return parseDate(date, DATE_FORMAT_DEFAULT); 
	}

	/**
	 * @param dateString - date representation into String format
	 * @param format - format of Date @see {@link SimpleDateFormat} 
	 * @return
	 * <ul>
	 * 	<li><b>null</b> - wrong format (can't be parsed) or empty </li>
	 * </ul>
	 */
	public static Date parseDate(String dateString, String format){
		String value=StringUtils.trimToNull(dateString);
		if(value==null){
			return null;
		}
		SimpleDateFormat sdf=new SimpleDateFormat(format);
		try {
			return sdf.parse(value);
		} catch (ParseException e) {
			return null;
		}
	}
}
