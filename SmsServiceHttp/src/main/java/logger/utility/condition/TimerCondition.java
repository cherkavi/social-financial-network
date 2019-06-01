package logger.utility.condition;

import java.util.Date;

import logger.utility.ILoggerCondition;

/** класс, который отсчитывает заданные временные интервалы */
public class TimerCondition implements ILoggerCondition{
	private Date currentDate=new Date();
	private int delay;
	
	/** класс, который отсчитывает заданные временные интервалы 
	 * @param delay - задержка в мс после которой нужно выставлять флаг изменения условия 
	 * */
	public TimerCondition(int delay){
		this.delay=delay;
	}
	
	@Override
	public boolean condition() {
		boolean returnValue=false;
		Date tempDate=new Date();
		if((currentDate.getTime()+delay)<(tempDate.getTime())){
			this.currentDate=tempDate;
			returnValue=true;
		}
		return returnValue;
	}

}
