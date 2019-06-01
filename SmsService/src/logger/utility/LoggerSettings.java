package logger.utility;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 * объект, который содержит все настройки для логов 
 */
class LoggerSettings{
	private Logger logger;
	private Level level;
	private String pattern;
	private String timeStamp;
	private int maxBackupIndex;
	private String maxFileSize;
	private String fileName;
	/**
	 * объект, который содержит все настройки для логов 
	 */
	public LoggerSettings(){
	}

	public void setTimeStamp(String timeStamp) {
		this.timeStamp=timeStamp;
	}

	public void setMaxBackupIndex(int maxIndex) {
		this.maxBackupIndex=maxIndex;
	}

	public void setMaxFileSize(String maxFileSize) {
		this.maxFileSize=maxFileSize;
	}

	public void setFileName(String logFileName) {
		this.fileName=logFileName;
	}

	public String getFileName() {
		return this.fileName;
	}
	
	public Logger getLogger() {
		return logger;
	}
	public void setLogger(Logger logger) {
		this.logger = logger;
	}
	public Level getLevel() {
		return level;
	}
	public void setLevel(Level level) {
		this.level = level;
	}

	public String getPattern() {
		return pattern;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

	public String getTimeStamp() {
		return timeStamp;
	}

	public int getMaxBackupIndex() {
		return maxBackupIndex;
	}

	public String getMaxFileSize() {
		return maxFileSize;
	}
}
