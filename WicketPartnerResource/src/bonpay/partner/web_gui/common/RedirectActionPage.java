package bonpay.partner.web_gui.common;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import org.apache.wicket.markup.html.WebPage;

/** ������, ������� �������� � ���� ����������� ���������� �� �������� ���������� ���������� ������� c ���������� ����������� 
 */
public class RedirectActionPage implements Serializable{
	private final static long serialVersionUID=1L;
	/** �����, �� ��������� �������� ����� �������� WebPage*/
	private Class<?> classPage=null;
	/** ������, ������� ������ �������� ����� �������� WebPage*/
	private Object objectPage=null;
	/** ��� ������, �� ������ ������� ����� �������� WebPage */
	private String methodName=null;
	/** ������ ���������� ���� ��� ������������ ���� ��� ������� */
	private Class<?>[] parameters=null;
	/** ��������� ���������� ���� ��� ������������ ���� ��� ������� */
	private Object[] values=null;
	/** ��������, ������� ������� �������� ���������� */
	private WebPage webPage=null;
	
	private boolean isClassPage=false;
	private boolean isWebPage=false;
	private boolean isClassPageNeedCreate=false;
	private boolean isGetPageFromObject=false;
	private boolean isGetPageAfterCallMethod=false;
	
	/** 
	 * <b> ClassPage </b>
	 * ������� ������ �� ������� ���������� ���������� ��� ������������� �������� ������ ������� 
	 * @param classPage - ������, �������� ���������� ���������� ���������� 
	 * */
	public RedirectActionPage(Class<?> classPage){
		this.classPage=classPage;
		this.isClassPage=true;
	}

	/** 
	 * <b> WebPage </b>
	 * ������� ������ �� ������� ���������� ���������� ��� ������������� �������� ������ ������� 
	 * @param classPage - ������, �������� ���������� ���������� ���������� 
	 * */
	public RedirectActionPage(WebPage page){
		this.webPage=page;
		this.isWebPage=true;
	}
	
	/** 
	 * <b> ClassPageNeedCreate</b>
	 * ������� ������ � ���������� ����������� ��� �������� ���������� 
	 * @param classPage - �����, ������ �������� ����� �������
	 * @param parameters - ������ ����������, ������� ����� �������� 
	 * @param values - ��������, ������� ����� �������� � �����������
	 * */
	public RedirectActionPage(Class<?> classPage, 
							  Class<?>[] parameters, 
							  Object[] values){
		this.classPage=classPage;
		this.parameters=parameters;
		this.values=values;
		this.isClassPageNeedCreate=true;
	}
	
	/** 
	 * <b>GetPageFromObject</b>
	 * �������� ������ WebPage �� �������, �� ��������� ������ ������ ����� ������� 
	 * @param objectWithPage - ������, ������� �������� ������ �� �������� � ������ �� ����� ������ ������
	 * @param methodName - ��� ������, ������� ����� ������� ��� ��������� WebPage
	 * @param parameters - ������ ����������, ������� ����� �������� 
	 * @param values - ��������, ������� ����� �������� � �����
	 * */
	public RedirectActionPage(Object objectWithPage, 
							  String methodName, 
							  Class<?>[] parameters, 
							  Object[] values){
		this.objectPage=objectWithPage;
		this.parameters=parameters;
		this.values=values;
		this.isGetPageFromObject=true;
	}
	
	/** 
	 * <b> GetPageAfterCallMethod </b>
	 * <li>�������� ���������� ��������, ����� ���� ������ � ������� ��� ������</li>
	 * <li>�������� ���������� ������ �������, ������� ��� ������ ���������� �������� ����������</li>
	 * @param webPage ��������, ������� ����� �������� ����������
	 * @param object ������, � �������� ����� ������ �����
	 * @param methodName ��� ������, ������� ����� ������ � �������
	 * @param parameters - (nullable) ������ ����������, ������� ������ ���� � ������� ( ���������)
	 * @param values - (nullable) �������, ������� ������ ���� �������� � �������� ����������
	 */
	public RedirectActionPage(WebPage webPage, 
							  Object object, 
							  String methodName, 
							  Class<?>[] parameters,
							  Object[] values){
		this.webPage=webPage;
		this.objectPage=object;
		this.methodName=methodName;
		this.parameters=parameters;
		this.values=values;
		this.isGetPageAfterCallMethod=true;
	}
	
	/** ������� � ������� ����� �� ���(������) ����� 
	 * @param object - ������, � �������� ���������� �����
	 * @param methodName - ��� ������, ������� ����������
	 * @param argumentClasses - (nullable)������ ��������, �� ��������� ������� ����� �������� �����
	 * @param argumentObjects - (nullable)�������, ������� ����� �������� � �������� ����������
	 * */
	private Object callMethodOfObject(Object object, 
		  							  String methodName,
									  Class<?>[] argumentClasses,
									  Object[] argumentObjects){
		Object returnValue=null;
		try{
			if(argumentClasses==null){
				argumentClasses=new Class<?>[]{};
			}
			if(argumentObjects==null){
				argumentObjects=new Object[]{};
			}
			Class<?> objectClass=object.getClass();
			Method method=objectClass.getMethod(methodName,argumentClasses);
			returnValue=method.invoke(object,argumentObjects);
		}catch(Exception ex){
			System.err.println("callMethodOfObject Exception:"+ex.getMessage());
		}
		return returnValue;
	}
	
	/** �������� ���������� ������� ������� �� ��������� ����� ������� ������� 
	 * @param component - ������� ����� ������������ �����������
	 * @param methodName - (nullable)��� ������, �������� ����� �������� ���������
	 * @param executeClass - (nullable)������ �� �������, � ������� ��������� ������� 
	 * @param executeObject - (nullable)�������������� ��������� ��� ������
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
	 * 		<td>GetPageAfterCallMethod</td> <td>null</td> <td>value</td> <td> �������� ����� � ������� � ������������ �����������, ����� ������� ���������� ��������</td>
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
					      String methodName,
						  Class<?>[] executeClass, 
						  Object[] executeObject){
		boolean returnValue=false;
		if(isClassPage==true){
			debug("action: �������� ���������� ������ (��� ����������)"); 
			component.setResponsePage(classPage);
		}
		if(isWebPage==true){
			debug("action: �������� ���������� ������� ������ WebPage");
			if(methodName==null){
				component.setResponsePage(this.webPage);
			}
			if(methodName!=null){
				debug("action:                         � ��������������� ������� ������ ");
				this.callMethodOfObject(this.webPage, methodName, executeClass, executeObject);
				component.setResponsePage(this.webPage);
			}
		}
		if(isClassPageNeedCreate==true){
			debug("action: ������� ������ � �����������");
			try{
				if(methodName==null){
					if((executeClass==null)||(executeClass.length==0)){
						Constructor<?> constructor=this.classPage.getConstructor(parameters);
						WebPage page=(WebPage)constructor.newInstance(this.values);
						component.setResponsePage(page);
					}else{
						debug("action:                    ���������� ���������� � ������������ ");
						Constructor<?> constructor=this.classPage.getConstructor(this.joinClassArray(parameters, executeClass));
						WebPage page=(WebPage)constructor.newInstance(this.joinObjectArray(values, executeObject));
						component.setResponsePage(page);
					}
				}
				if(methodName!=null){
					Constructor<?> constructor=this.classPage.getConstructor(parameters);
					WebPage page=(WebPage)constructor.newInstance(this.values);
					if((executeClass==null)||(executeClass.length==0)){
						debug("action:                    ����� ������ ����� �������� �������� ");
						this.callMethodOfObject(page, methodName, null, null);
					}else{
						debug("action:                    ����� ������ � ����������� ����� �������� �������� ");
						this.callMethodOfObject(page, methodName, executeClass, executeObject);
					}
					component.setResponsePage(page);
				}
			}catch(Exception ex){
				System.err.println("ActionPage Constructor not found for:"+this.classPage);
				returnValue=false;
			}
		}
		if(isGetPageFromObject==true){
			debug("action: �������� ���������� �� ��������� �������(WebPage), ����������� �� ������ �� ����� ������");
			if(methodName==null){
				if((executeClass==null)||(executeClass.length==0)){
					Object webPage=callMethodOfObject(this.objectPage,this.methodName,this.parameters,values);
					if((webPage!=null)&&(webPage instanceof WebPage)){
						debug("action: �������� ���������� ���������� �������� ������������ ");
						component.setResponsePage((WebPage)webPage);
						returnValue=true;
					}else{
						debug("action: �������� ���������� ����������� ������ ��������� ");
						returnValue=true;
					}
				}else{
					debug("action:                                      ���������� � ������ ��������� �������� ��������� ���������� ");
					Object webPage=callMethodOfObject(this.objectPage,this.methodName,this.joinClassArray(this.parameters, executeClass),this.joinObjectArray(this.values,executeObject));
					if((webPage!=null)&&(webPage instanceof WebPage)){
						debug("action: �������� ���������� ���������� �������� ������������ ");
						component.setResponsePage((WebPage)webPage);
						returnValue=true;
					}else{
						debug("action: �������� ���������� ����������� ������ ��������� ");
						returnValue=true;
					}
				}
			}
			if(methodName!=null){
				Object webPage=callMethodOfObject(this.objectPage,this.methodName,this.parameters,values);
				if((executeClass==null)||(executeClass.length==0)){
					debug("action:                                      ����� ������ � �������� ����� ��������� �� ���������� ");
					callMethodOfObject(webPage,methodName,null,null);
				}else{
					debug("action:                                      ����� ������ � �������� � ����������� ����� ��������� �� ���������� ");
					callMethodOfObject(webPage,methodName,executeClass,executeObject);
				}
				component.setResponsePage((WebPage)webPage);
				returnValue=true;
			}
		}
		if(isGetPageAfterCallMethod==true){
			debug("action: �������� ���������� �� ��������� WebPage, � ��������������� ������� ������");
			if(methodName==null){
				if((executeClass==null)||(executeClass.length==0)){
					callMethodOfObject(this.objectPage,this.methodName,this.parameters,this.values);
					component.setResponsePage(this.webPage);
					returnValue=true;
				}else{
					debug("action:                                             ���������� ���������� � ����� ������ ");
					callMethodOfObject(this.objectPage,this.methodName,this.joinClassArray(parameters, executeClass),this.joinObjectArray(values, executeObject));
					component.setResponsePage(this.webPage);
					returnValue=true;
				}
			}
			if(methodName!=null){
				if((executeClass==null)||(executeClass.length==0)){
					debug("action:                                   ����� ������ � �������� ����� ��������� ����������");
					callMethodOfObject(this.objectPage,this.methodName,this.parameters,this.values);
					callMethodOfObject(this.webPage,methodName,null,null);
					component.setResponsePage(this.webPage);
					returnValue=true;
				}else{
					debug("action:                                   ����� ������ c ����������� � �������� ����� ��������� ����������");
					callMethodOfObject(this.objectPage,this.methodName,this.parameters,this.values);
					callMethodOfObject(this.webPage,methodName,executeClass,executeObject);
					component.setResponsePage(this.webPage);
					returnValue=true;
				}
			}
			returnValue=true;
		}
		return returnValue;
	}
	
	/** ������� �������� Class[] */
	private Class<?>[] joinClassArray(Class<?>[] array1, Class<?>[] array2){
		int length=0;
		if(array1!=null){
			length+=array1.length;
		}
		if(array2!=null){
			length+=array2.length;
		}
		Class<?>[] returnValue=new Class<?>[length];
		int array1Length=0;
		if(array1!=null){
			for(int counter=0;counter<array1.length;counter++){
				returnValue[counter]=array1[counter];
			}
			array1Length=array1.length;
		}
		if(array2!=null){
			for(int counter=0;counter<array2.length;counter++){
				returnValue[counter+array1Length]=array2[counter];
			}
		}
		return returnValue;
	}

	/** ������� �������� Object[] */
	private Object[] joinObjectArray(Object[] array1, Object[] array2){
		int length=0;
		if(array1!=null){
			length+=array1.length;
		}
		if(array2!=null){
			length+=array2.length;
		}
		Object[] returnValue=new Object[length];
		int array1Length=0;
		if(array1!=null){
			for(int counter=0;counter<array1.length;counter++){
				returnValue[counter]=array1[counter];
			}
			array1Length=array1.length;
		}
		if(array2!=null){
			for(int counter=0;counter<array2.length;counter++){
				returnValue[counter+array1Length]=array2[counter];
			}
		}
		return returnValue;
	}
	
	
	private void debug(Object information){
		System.out.print("ActionPage");
		System.out.print("DEBUG ");
		System.out.println(information);
	}
}
