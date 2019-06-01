package bonclub.office_private.web_service.common_objects;

import java.io.Serializable;

/** фильтр для колонок таблицы */
public class Filter implements Serializable{
	private final static long serialVersionUID=1L;
	/** условие, которое следует добавить к SQL запросу   */
	private String sqlString;
	
	/** фильтр для колонок таблицы */
	public Filter(){
	}
	
	/** фильтр для колонок таблицы */
	public Filter(String sqlString){
		this.sqlString=sqlString;
	}
	
	/** установить строку для запроса  */
	public void setSqlString(String value){
		this.sqlString=value;
	}
	
	/** получить строку для запроса  */
	public String getSqlString(){
		return this.sqlString;
	}
	
}
