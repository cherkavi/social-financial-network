package bonpay.partner.web_gui.common;

import java.io.Serializable;
import java.util.HashMap;

/** ������, ������� ������������� ����������� ��������� ������� �� ��������, 
 * � �������� �� ��������� �������� */
public class RedirectAction implements Serializable{
	private final static long serialVersionUID=1L;
	private HashMap<String, RedirectActionPage> map=new HashMap<String,RedirectActionPage>();
	/** �����, ������� �������� � ���� ����������� ���������� �� �������� ���������� ��������� ��������� 
	 * @param returnValues (not null)- ��������, ��� ������� �������������� �������� ����������   
	 * @param pages (not null)- ��������, �� ������� ������� ���������� ���������� 
	 * */
	public RedirectAction(String[] returnValues,RedirectActionPage[] pages){
		for(int counter=0;counter<returnValues.length;counter++){
			map.put(returnValues[counter], pages[counter]);
		}
	}
	
	/** ��������� ���������� �� ���������� ���������� �� �������� 
	 * @param component - ���������, �������� ����� ������������ ����� �������� ���������� (component.setResponsePage)
	 * @param returnValue - ��������� �������� ��� ������������� �������� ���������� 
	 * @param methodName - (nullable)��� ������, �������� ����� �������� ����������
	 * @param executeClass -  (nullable)(executeClass.length==executeObject.length) ������ ���������� 
	 * @param executeObject - (nullable)(executeClass.length==executeObject.length) ���������
	 * <table border=1 style="background:gray">
	 * 	<tr>
	 * 		<th>�����</th> <th>��� ������</th> <th>���������</th> <th> �������� </th>
	 * 	</tr>
	 * 	<tr>
	 * 		<td>ClassPage</td> <td>null</td>   <td>any</td>         <td> ��������� �/��� ����� �� ������������</td>
	 * 	</tr>
	 * 	<tr>
	 * 		<td>ClassPage</td> <td>value</td> <td>any</td>          <td> ��������� �/��� ����� �� ������������</td>
	 * 	</tr>
	 * 	<tr>
	 * 		<td>WebPage</td> <td>null</td> <td>null</td>  <td> �������� ���������� ��������</td>
	 * 	</tr>
	 * 	<tr>
	 * 		<td>WebPage</td> <td>null</td> <td>value</td> <td> ��������� �/��� ����� �� ������������</td>
	 * 	</tr>
	 * 	<tr>
	 * 		<td>WebPage</td> <td>value</td> <td>null</td> <td> ����� ������ � �������� ����� ��������� ����������</td>
	 * 	</tr>
	 * 	<tr>
	 * 		<td>WebPage</td> <td>value</td> <td>value</td> <td> ����� ������ c ����������� � �������� ����� ��������� ����������</td>
	 * 	</tr>
	 * 	<tr>
	 * 		<td>ClassPageNeedCreate</td> <td>null</td> <td>null</td>  <td> �������� ���������� ���������� ������� �� ������</td>
	 * 	</tr>
	 * 	<tr>
	 * 		<td>ClassPageNeedCreate</td> <td>null</td> <td>value</td> <td> ���������� � ������������ �������� ��������� ���������� </td>
	 * 	</tr>
	 * 	<tr>
	 * 		<td>ClassPageNeedCreate</td> <td>value</td> <td>null</td> <td> �������� ��������, ����� ����� ������ � ���� ��������</td>
	 * 	</tr>
	 * 	<tr>
	 * 		<td>ClassPageNeedCreate</td> <td>value</td> <td>value</td> <td> �������� ��������, ����� ����� ������ c ����������� � ���� ��������</td>
	 * 	</tr>
	 * 	<tr>
	 * 		<td>GetPageFromObject</td> <td>null</td> <td>null</td>  <td> �������� ���������� ���������� ������</td>
	 * 	</tr>
	 * 	<tr>
	 * 		<td>GetPageFromObject</td> <td>null</td> <td>value</td> <td> ���������� � ������ ��������� �������� ��������� ���������� </td>
	 * 	</tr>
	 * 	<tr>
	 * 		<td>GetPageFromObject</td> <td>value</td> <td>null</td> <td> ��������� �������� �� �������, ����� ����� ������ � ���� ��������</td>
	 * 	</tr>
	 * 	<tr>
	 * 		<td>GetPageFromObject</td> <td>value</td> <td>value</td> <td> ��������� �������� �� �������, ����� ����� ������ � ���� �������� c �����������</td>
	 * 	</tr>
	 * 	<tr>
	 * 		<td>GetPageAfterCallMethod</td> <td>null</td> <td>null</td>  <td> �������� ����� � �������, ����� �������� ���������� ��������</td>
	 * 	</tr>
	 * 	<tr>
	 * 		<td>GetPageAfterCallMethod</td> <td>null</td> <td>value</td> <td> �������� ����� � ������� � ������������ �����������, ����� ���e���� ���������� ��������</td>
	 * 	</tr>
	 * 	<tr>
	 * 		<td>GetPageAfterCallMethod</td> <td>value</td> <td>null</td> <td> �������� ����� � �������, ����� � �������� ��� �� �������� �����</td>
	 * 	</tr>
	 * 	<tr>
	 * 		<td>GetPageAfterCallMethod</td> <td>value</td> <td>value</td> <td> �������� ����� � �������, ����� � �������� ��� �� �������� ����� c �����������</td>
	 * 	</tr>
	 * </table> 
	 * */
	public boolean action(org.apache.wicket.Component component,
						  String returnValue,
						  String methodName,
						  Class<?>[] executeClass,
						  Object[] executeObject){
		boolean value=false;
		if(map.containsKey(returnValue)){
			value=map.get(returnValue).action(component,methodName, executeClass, executeObject);
		}else{
			System.err.println("no ActionPage for returnValue:"+returnValue);
			value=false;
		}
		return value;
	}
}
