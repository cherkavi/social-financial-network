package logger.utility;
import java.io.FileInputStream;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import logger.utility.condition.DateChanger;
import logger.utility.condition.TimerCondition;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.RollingFileAppender;


/** класс, который активирует логи, согласно файлу активации */
public class LoggerActivator {
	private String defaultTimeStamp="yyyy_MM_dd_HH_mm_ss";
	/** ключ для получения текстового вида лога уровня */
	String propertiesLevel="logger.level";
	/** ключ для получения строки шаблона */
	String propertiesPattern="logger.pattern";
	/** ключ для получения пути к файлу */
	String propertiesFile="logger.file";
	/** ключ для получения пути к файлу */
	String propertiesFileMaxSize="logger.file.max_size";
	/** установить максимальное */
	String propertiesFileMaxIndex="logger.file.max_index";
	/** штамп времени */
	String propertiesFileTimeStamp="logger.file.time_stamp";
	
	private LoggerSettings settings=new LoggerSettings();

	/** класс, который активирует логи, согласно файлу активации 
	 * @param logPackage - пакет, по которому нжуно создать лог (null - RootLog)
	 * @param pathToFile - путь к файлу, куда следует складывать логи 
	 * @param delay - время задержки перед созданием следующего блока логов (5000)
	 */
	public LoggerActivator(String logPackage, 
			   			   String pathToFile,
			   			   final int delay){
		readSettingsFromFile(logPackage, pathToFile);
		this.refreshSettings();
		new LoggerWatcher(this,new TimerCondition(delay),1000).start();
	}

	/** класс, который активирует логи, согласно файлу активации 
	 * @param logPackage - пакет, по которому нжуно создать лог (null - RootLog)
	 * @param pathToFile - путь к файлу, куда следует складывать логи 
	 * @param delay - время задержки перед созданием следующего блока логов (5000)
	 */
	public LoggerActivator(String logPackage, 
			   			   String pathToFile){
		readSettingsFromFile(logPackage, pathToFile);
		this.refreshSettings();
		new LoggerWatcher(this,new DateChanger(),60000).start();
	}
	/** 
	 * объект, который установит логи для указанного пакета согласно данным, которые находятся в файле
	 * @param logPackage - пакет, по которому устанавливается логгирование (null - RootLogger )
	 * @param pathToFile - properties файл, который содержит всю необходимую информацию по логам 
	 * */
	public void readSettingsFromFile(String logPackage, String pathToFile){
		// 
		Properties properties=new Properties();
		try{
			this.settings=new LoggerSettings();
			properties.load(new FileInputStream(pathToFile));
			// получить логгер, который нужно сконфигурировать
			if((logPackage==null)||(logPackage.trim().equals(""))){
				this.settings.setLogger(Logger.getRootLogger());
			}else{
				this.settings.setLogger(Logger.getLogger(logPackage));
			}
			// получить уровень лога
			this.settings.setLevel(this.getLevel(properties.getProperty(propertiesLevel)));
			
			// получить шаблон лога 
			String pattern=PatternLayout.DEFAULT_CONVERSION_PATTERN;
			if(properties.getProperty(this.propertiesPattern)!=null){
				pattern=properties.getProperty(this.propertiesPattern);
			}
			this.settings.setPattern(pattern);
			
			// получить имя файла
			String logFileName=properties.getProperty(propertiesFile);
			if(   (logFileName==null)
					||(logFileName.trim().equalsIgnoreCase("empty"))
					||(logFileName.trim().equalsIgnoreCase("null"))
					||(logFileName.trim().equalsIgnoreCase(""))){
					logFileName=null;
				
			};
			this.settings.setFileName(logFileName);
			
			// получить максимальную длинну файла, если она задана  
			try{
				this.settings.setMaxFileSize("");
				this.settings.setMaxFileSize(properties.getProperty(this.propertiesFileMaxSize));
			}catch(Exception ex){};
			// получить максимальное кол-во файлов
			try{
				this.settings.setMaxBackupIndex(0);
				Integer maxFileSize=Integer.parseInt(properties.getProperty(this.propertiesFileMaxIndex));
				if(maxFileSize>0){
					this.settings.setMaxBackupIndex(maxFileSize);
				}
			}catch(Exception ex){};
			// получить штамп времени для вывода данных 
			String timeStamp=properties.getProperty(propertiesFileTimeStamp);
			if((timeStamp==null)||(timeStamp.trim().equals(""))){
				timeStamp="yyyy_MM_dd";
			}
			this.settings.setTimeStamp(timeStamp);
			 
			// если файл не найден
			// logger.addAppender(new ConsoleAppender(new PatternLayout(logPattern)));

			
			/*try{
				//DailyRollingFileAppender fileAppender=new DailyRollingFileAppender(new PatternLayout(logPattern), logFileName, timeStamp);
				RollingFileAppender fileAppender=new RollingFileAppender(new PatternLayout(logPattern), 
																		logFileName);
				logger.addAppender(fileAppender);
			}catch(Exception ex){
				logger.addAppender(new ConsoleAppender(new PatternLayout(logPattern)));
				System.err.println("LoggerActivator logger settings Exception: "+ex.getMessage()+"  file: "+logFileName + " logger put to Console");
			}*/
		}catch(Exception ex){
			System.err.println("не удалось загрузить файл - установки по умолчанию [ERROR, Console, %m%n]");
			this.settings=new LoggerSettings();
			this.settings.setFileName(null);
			this.settings.setLevel(Level.ERROR);
			if((logPackage==null)||(logPackage.trim().equals(""))){
				this.settings.setLogger(Logger.getRootLogger());
			}else{
				this.settings.setLogger(Logger.getLogger(logPackage));
			}
			this.settings.setPattern(PatternLayout.DEFAULT_CONVERSION_PATTERN);
		};
	}
	
	public void refreshSettings(){
		LogManager.resetConfiguration();
		
		//this.settings.getLogger().setAdditivity(false);
		this.settings.getLogger().setLevel(this.settings.getLevel());
		if(this.settings.getFileName()!=null){
			// this.settings.getLogger().removeAllAppenders(); because resetConfiguration();
			String fileName=this.generateFileName(this.settings.getFileName(), this.settings.getTimeStamp());
			try{
				RollingFileAppender fileAppender=new RollingFileAppender(new PatternLayout(this.settings.getPattern()), 
																						   fileName
																		 );
				if(this.settings.getMaxBackupIndex()>0){
					fileAppender.setMaxBackupIndex(this.settings.getMaxBackupIndex());
				}else{
					fileAppender.setMaxBackupIndex(-2);
				}
				if(this.settings.getMaxFileSize()!=null){
					fileAppender.setMaxFileSize(this.settings.getMaxFileSize());
				}
				this.settings.getLogger().addAppender(fileAppender);
			}catch(Exception ex){
				System.err.println("Не удалось инициализировать лог ");
				this.settings.getLogger().addAppender(new ConsoleAppender(new PatternLayout(this.settings.getPattern())));
			}
		}else{
			// this.settings.getLogger().removeAllAppenders(); because resetConfiguration();
			this.settings.getLogger().addAppender(new ConsoleAppender(new PatternLayout(this.settings.getPattern())));
		}
	}
	
	/** сгенерировать новый файл  */
	private String generateFileName(String fileName, String timeStamp){
		// получить имя файла и его расширение
		int dotPosition=fileName.lastIndexOf(".");
		if(dotPosition>0){
			String name=fileName.substring(0,dotPosition);
			String extension="";
			if(dotPosition<(fileName.length()-1)){
				extension=fileName.substring(dotPosition+1);
			}
			try{
				SimpleDateFormat sdf=new SimpleDateFormat(timeStamp);
				return name+sdf.format(new Date())+"."+extension;
			}catch(Exception ex){
				System.err.println(" Error create file Pattern ");
				ex.printStackTrace(System.err);
				SimpleDateFormat sdf=new SimpleDateFormat(this.defaultTimeStamp);
				return name+sdf.format(new Date())+"."+extension;
			}
		}else{
			try{
				SimpleDateFormat sdf=new SimpleDateFormat(timeStamp);
				return fileName+sdf.format(new Date());
			}catch(Exception ex){
				System.err.println(" Error create file Pattern ");
				ex.printStackTrace(System.err);
				SimpleDateFormat sdf=new SimpleDateFormat(this.defaultTimeStamp);
				return fileName+sdf.format(new Date());
			}
		}
	}
	
	/** получить уровень лога, который будет установлен */
	private Level getLevel(String levelString){
		Level level=Level.DEBUG;
		if(levelString==null){
			level=Level.DEBUG;
		}else if(levelString.equalsIgnoreCase("debug")){
			level=Level.DEBUG;
		}else if(levelString.equalsIgnoreCase("info")){
			level=Level.INFO;			
		}else if(levelString.equalsIgnoreCase("warn")){
			level=Level.WARN;			
		}else if(levelString.equalsIgnoreCase("error")){
			level=Level.ERROR;			
		}else if(levelString.equalsIgnoreCase("fatal")){
			level=Level.FATAL;			
		}else if(levelString.equalsIgnoreCase("all")){
			level=Level.ALL;			
		}else if(levelString.equalsIgnoreCase("off")){
			level=Level.OFF;
		}
		return level;
	}
}


