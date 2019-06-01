package gui;
import gui.Frames.Main.ConsoleMain;


import gui.Frames.Main.FrameMain;
import gui.Utility.CommonObject;

import java.awt.GridLayout;
import java.util.Calendar;

import javax.swing.*;

import logger.utility.LoggerActivator;

public class EnterPoint extends JFrame{
	private static final long serialVersionUID=1L;
	/** рабочий стол для отображения всех событий*/
	private JDesktopPane field_desktop;
	
	public static void main(String[] args){
		CommonObject commonObject=null;
		if(args.length>0){
			commonObject=getCommonObject(args[0]);
		}else{
			commonObject=getCommonObject("settings.xml");
		}

		Calendar notDeliveredMessages=Calendar.getInstance();
		notDeliveredMessages.set(Calendar.DAY_OF_MONTH, 2);
		notDeliveredMessages.set(Calendar.HOUR_OF_DAY, 0);
		notDeliveredMessages.set(Calendar.MINUTE, 0);
		notDeliveredMessages.set(Calendar.SECOND, 0);

		Calendar repeatNotDelivered=Calendar.getInstance();
		repeatNotDelivered.set(Calendar.DAY_OF_MONTH, 0);
		repeatNotDelivered.set(Calendar.HOUR_OF_DAY, 2);
		repeatNotDelivered.set(Calendar.MINUTE, 0);
		repeatNotDelivered.set(Calendar.SECOND, 0);
		
		//Level level=CommonObject.convertStringLevel(commonObject.getLoggerLevel());
		//Logger.getRootLogger().setLevel(level);
		/*Logger.getLogger("org").setLevel(level);
		Logger.getLogger("gui").setLevel(level);
		Logger.getLogger("engine").setLevel(level);*/
		
		new LoggerActivator(null, "logger.properties");
		//new LoggerActivator("org.smslib", "logger.properties");
		//new LoggerActivator("modem", "logger.properties");
		
		/*new LoggerActivator("org", "logger.properties");
		new LoggerActivator("gui", "logger.properties");
		new LoggerActivator("engine", "logger.properties");
		
		
		
		new LoggerActivator("org.smslib", "logger.properties");
		Logger.getLogger("org.hibernate").setLevel(Level.WARN);
		new LoggerActivator("org.hibernate", "logger.properties");
		Logger.getLogger("org.hibernate").setLevel(Level.WARN);*/
		
		/*String pattern=commonObject.getLogPattern();
		if(pattern.equals("")){
			pattern=PatternLayout.TTCC_CONVERSION_PATTERN;
		}
		//Logger.getRootLogger().addAppender(new ConsoleAppender(new PatternLayout(pattern)));
		
		if((commonObject.getLogFile()==null)||(commonObject.getLogFile().trim().equals(""))){
			Logger.getRootLogger().addAppender(new ConsoleAppender(new PatternLayout(pattern)));
		}else{
			try{
				//Logger.getRootLogger().removeAllAppenders();
				Logger.getLogger("org").addAppender(new FileAppender(new PatternLayout(pattern),commonObject.getLogFile()));
				Logger.getLogger("gui").addAppender(new FileAppender(new PatternLayout(pattern),commonObject.getLogFile()));
				Logger.getLogger("engine").addAppender(new FileAppender(new PatternLayout(pattern),commonObject.getLogFile()));
				//Logger.getRootLogger().addAppender(new FileAppender(new PatternLayout(pattern),commonObject.getLogFile()));
			}catch(Exception ex){
				Logger.getRootLogger().addAppender(new ConsoleAppender(new PatternLayout(pattern)));
				System.err.println("Set logger Exception:"+ex.getMessage());
				System.exit(0);
			}
		}
		*/
		Boolean isConsole=Boolean.parseBoolean(commonObject.isConosle().trim());
		if(isConsole==true){
			new ConsoleMain(commonObject, notDeliveredMessages, repeatNotDelivered);
		}else{
			new EnterPoint(commonObject,notDeliveredMessages, repeatNotDelivered);
		}
		
	}
	
	public EnterPoint(CommonObject commonObject, 
					  Calendar notDeliveredMessages, 
					  Calendar repeatNotDelivered){
		super("Viewer");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(900,650);
		this.getContentPane().add(this.initComponent());
		try{
			//this.setExtendedState(MAXIMIZED_BOTH);
		}catch(Exception ex){};
		this.setVisible(true);
		
		new FrameMain(this.field_desktop,
					  commonObject,
					  notDeliveredMessages,
					  repeatNotDelivered
					  );
	}
	
	/** получить общий объект для всех графических окон */
	private static CommonObject getCommonObject(String pathToXml){
		CommonObject common_object=null;
		try{
			// INFO XML настройки для графических окон 
			common_object=new CommonObject(pathToXml);
		}catch(Exception ex){
			System.err.println("getCommonObject connection Exception:"+ex.getMessage());
		}
		return common_object;
	}
	
	private JPanel initComponent(){
		JPanel panel_main=new JPanel(new GridLayout(1,1));
		this.field_desktop=new JDesktopPane();
		panel_main.add(field_desktop);
		return panel_main;
	}
	
}
