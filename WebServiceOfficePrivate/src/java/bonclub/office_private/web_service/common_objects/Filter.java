package bonclub.office_private.web_service.common_objects;

import java.io.Serializable;

/** ������ ��� ������� ������� */
public class Filter implements Serializable{
	private final static long serialVersionUID=1L;
	/** �������, ������� ������� �������� � SQL �������   */
	private String sqlString;
	
	/** ������ ��� ������� ������� */
	public Filter(){
	}
	
	/** ������ ��� ������� ������� */
	public Filter(String sqlString){
		this.sqlString=sqlString;
	}
	
	/** ���������� ������ ��� �������  */
	public void setSqlString(String value){
		this.sqlString=value;
	}
	
	/** �������� ������ ��� �������  */
	public String getSqlString(){
		return this.sqlString;
	}
	
}
