package xml_ini;

/**
 * settings error 
 */
public class SettingsException extends Exception{

	private static final long	serialVersionUID	= 1L;

	public SettingsException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public SettingsException(String message, Throwable cause) {
		super(message, cause);
	}

	public SettingsException(String message) {
		super(message);
	}

	public SettingsException(Throwable cause) {
		super(cause);
	}
	
	

}
