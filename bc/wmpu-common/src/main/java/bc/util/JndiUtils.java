package bc.util;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.commons.lang3.StringUtils;

/**
 * read information from jndi <br />
 * will work inside servlet container with JNDI 
 */
public class JndiUtils {

	private static Context environmentContext;
	
	static{
		try {
			environmentContext = (Context) new InitialContext().lookup("java:/comp/env");
		} catch (NamingException e) {
		}
	}

	private static final String JNDI_MARKER_BEGIN="${";
	private static final String JNDI_MARKER_END="}";
	
	public static boolean isJndiValue(String value) {
		return StringUtils.startsWith(value, JNDI_MARKER_BEGIN) && StringUtils.endsWith(value, JNDI_MARKER_END);
	}

	public static String readJndi(String value){
		String jndiValue=clearJndiMarker(value);
		return returnValueFromJndi(jndiValue);
	}

	private static String clearJndiMarker(String value) {
		return StringUtils.removeEnd(StringUtils.removeStart(value, JNDI_MARKER_BEGIN), JNDI_MARKER_END);
	}

	/**
	 * read value from JNDI context
	 * @param jndiParameterPath
	 * @return
	 */
	private static String returnValueFromJndi(String jndiParameterPath) {
		synchronized(environmentContext){
			try {
				return (String)environmentContext.lookup(jndiParameterPath);
			} catch (NamingException e) {
				return null;
			}
		}
	}

}
