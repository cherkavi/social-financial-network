package logger.utility.condition;

import java.util.Calendar;

import logger.utility.ILoggerCondition;

/** класс, который выставляет флаг изменения условия в случае изменения даты (младший разряд - изменение дня в году)*/
public class DateChanger implements ILoggerCondition{
	/** текущий день, по которому установлен лог */
	private int day;
	
	/** класс, который выставляет флаг изменения условия в случае изменения даты (младший разряд - изменение дня в году)*/
	public DateChanger(){
		this.day=this.getCurrentDay();
	}
	
	private int getCurrentDay(){
		 Calendar calendar=Calendar.getInstance();
		 return calendar.get(Calendar.DAY_OF_YEAR);
	}
	
	@Override
	public boolean condition() {
		int currentDay=this.getCurrentDay();
		if(day!=currentDay){
			this.day=currentDay;
			return true;
		}else{
			return false;
		}
	}

}
