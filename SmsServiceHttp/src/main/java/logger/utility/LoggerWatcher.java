package logger.utility;

import java.util.concurrent.TimeUnit;

/** класс, который реинициализирует логгер, согласно условиям */
class LoggerWatcher extends Thread{
	private int delay;
	private boolean flagRun;
	private LoggerActivator loggerActivator;
	private ILoggerCondition loggerCondition;
	/** класс, который реинициализирует логгер, согласно условиям
	 * @param loggerActivator - логгер активатор, который нужно переинициализировать
	 * @param loggerCondition - условие, которое следует проверять на возвращение положительного значения   
	 * @param delay - время в милисекундах указывающее на опрос условия 
	 * */
	public LoggerWatcher(LoggerActivator loggerActivator, 
						 ILoggerCondition loggerCondition,
						 int delay){
		this.loggerActivator=loggerActivator;
		this.loggerCondition=loggerCondition;
		this.delay=delay;
		this.flagRun=true;
	}
	
	public void stopThread(){
		this.flagRun=false;
		this.interrupt();
	}
	
	public void run(){
		while(this.flagRun){
			if(this.loggerCondition.condition()){
				this.loggerActivator.refreshSettings();
			}
			try{
				TimeUnit.MILLISECONDS.sleep(this.delay);
			}catch(Exception ex){};
		}
	}
}

