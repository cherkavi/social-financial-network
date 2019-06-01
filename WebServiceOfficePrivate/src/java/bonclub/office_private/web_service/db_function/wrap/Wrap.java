package bonclub.office_private.web_service.db_function.wrap;

/** ������� ����� ��� ���� ������� ��� �������� �������� � ������� */
public class Wrap {
	
	/** ���������, ���������� �� ������� ��������� �������� "0" - ��� ������������� (�� ��������������) � ������������� ��������� ��������� 
	 * @param value - ��������, ������� ������� ������� 
	 * @return
	 * <li><b> true</b>  ������� ���������� ��� ������ </li>
	 * <li><b>false</b> ���� ������ ��� ��������� </li>
	 */
	public boolean checkReturnValueForZero(String value){
		if(value!=null){
			return value.trim().equalsIgnoreCase("0");
		}else{
			return false;
		}
	}

	/** ���������, ���������� �� ������� ��������� �������� "0" - ��� ������������� (�� ��������������) � ������������� ��������� ��������� 
	 * <li><b> true</b>  ������� ���������� ��� ������ </li>
	 * <li><b>false</b> ���� ������ ��� ��������� </li>
	 * */
	public boolean checkReturnValueForZero(){
		if(this.getReturnValue()!=null){
			return this.getReturnValue().trim().equalsIgnoreCase("0");
		}else{
			return false;
		}
	}
	
	
	/** �������������� ���������, ������ ��(��-��������������), � ������� ������������ ������ � ���� ������ */
	protected String resultMessage;
	/** ������������ �������� ��������, ������� ����� ��������� �� "0" �������� {@link #checkReturnValueForZero} (��-��������������) */
	protected String returnValue;

	/**
	 * @return �������������� ���������, ������ ��(��-��������������), � ������� ������������ ������ � ���� ������ 
	 */
	public String getResultMessage() {
		return resultMessage;
	}
	/**
	 * @param resultMessage �������������� ���������, ������ ��(��-��������������), � ������� ������������ ������ � ���� ������ 
	 */
	public void setResultMessage(String resultMessage) {
		this.resultMessage = resultMessage;
	}
	/**
	 * @return the returnValue ������������ �������� ��������, ������� ����� ��������� �� "0" �������� {@link #checkReturnValueForZero} (��-��������������)
	 */
	public String getReturnValue() {
		return returnValue;
	}
	/**
	 * @param returnValue ������������ �������� ��������, ������� ����� ��������� �� "0" �������� {@link #checkReturnValueForZero} (��-��������������)
	 */
	public void setReturnValue(String returnValue) {
		this.returnValue = returnValue;
	}
	
	
}
