package xml_ini;

import java.io.IOException;
import java.text.MessageFormat;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

/**
 * all settings for program  
 * @author technik
 *
 */
public class Settings {

	private final static String DELIMITER="/";

	private static String makePath(String ... elements){
		if(elements==null || elements.length==0){
			return DELIMITER;
		}
		StringBuilder returnValue=new StringBuilder();
		for(int index=0;index<elements.length;index++){
			if(index!=(elements.length-1)){
				returnValue.append( DELIMITER);
			}
			returnValue.append(elements[index]);
		}
		return returnValue.toString();
	}
	
	private final static String PATH_ROOT="settings";
	private final static String PATH_ITERATION_DELAY=makePath(PATH_ROOT, "iteration_delay");
	private final static String PATH_JDBC=makePath(PATH_ROOT, "jdbc", "url");
	private final static String PATH_JDBC_LOGIN=makePath(PATH_ROOT, "jdbc", "login");
	private final static String PATH_JDBC_PASSWORD=makePath(PATH_ROOT, "jdbc", "password");
	private static final String	PATH_PROFILE_ID	= makePath(PATH_ROOT, "profile_id");
	private static final String	PATH_MESSAGE_COUNT_FOR_SEND	= makePath(PATH_ROOT, "send_count");
	private int pathIteration;
	private String	 jdbcUrl;
	private String	 jdbcUrlLogin;
	private String	 jdbcUrlPassword;
	/** Database.Sms.ProfileId*/
	private int	profileId;
	/** Database.Sms.Messages for send, how many need to send from one iteration */
	private int	messagesCountForSend;
	
	
	
	public static Settings load(String pathToSettings) throws SettingsException {
		Loader loader=null;
		try {
			loader=new Loader(pathToSettings);
		} catch (ParserConfigurationException | SAXException | IOException e) {
			throw new SettingsException(MessageFormat.format("can''t load data from file: {0}, check this path for exists or validate the file", pathToSettings), e );
		}
		// load data 
		Settings returnValue=new Settings();
		returnValue.pathIteration=readInteger(loader, pathToSettings, PATH_ITERATION_DELAY);
		returnValue.jdbcUrl=readString(loader, pathToSettings, PATH_JDBC);
		returnValue.jdbcUrlLogin=readString(loader, pathToSettings, PATH_JDBC_LOGIN);
		returnValue.jdbcUrlPassword=readString(loader, pathToSettings, PATH_JDBC_PASSWORD);
		returnValue.profileId=readInteger(loader, pathToSettings, PATH_PROFILE_ID);
		returnValue.messagesCountForSend=readInteger(loader, pathToSettings, PATH_MESSAGE_COUNT_FOR_SEND);
		// TODO 
		return returnValue;
	}
	
	
	
	private static String readString(Loader loader, String pathToSettings,
			String path) throws SettingsException {
		String value=loader.getString(path);
		if(value==null){
			throw new SettingsException(MessageFormat.format(" file {0} don''t contains value: {1} ", pathToSettings, path));
		}
		return value;
	}

	private static int readInteger(Loader loader, String pathToSettings,
			String path) throws SettingsException {
		int value=loader.getInteger(path, Integer.MIN_VALUE);
		if(value==Integer.MIN_VALUE){
			throw new SettingsException(MessageFormat.format(" file {0} don''t contains value: {1} ", pathToSettings, path));
		}
		return value;
	}



	/**
	 * @return - seconds for delay before next work iteration (read data for send, update status ... ) 
	 */
	public long getWorkiterationDelay() {
		return this.pathIteration;
	}



	public int getPathIteration() {
		return pathIteration;
	}



	public String getJdbcUrl() {
		return jdbcUrl;
	}



	public String getJdbcUrlLogin() {
		return jdbcUrlLogin;
	}



	public String getJdbcUrlPassword() {
		return jdbcUrlPassword;
	}



	public int getProfileId() {
		return this.profileId;
	}



	public Long getMessagesCountForSend() {
		return new Long(this.messagesCountForSend);
	}

	
	
}
