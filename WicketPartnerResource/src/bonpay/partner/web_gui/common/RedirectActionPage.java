package bonpay.partner.web_gui.common;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import org.apache.wicket.markup.html.WebPage;

/** объект, который содержит в себе необходимую информацию по передаче управления следующему объекту c указанными параметрами 
 */
public class RedirectActionPage implements Serializable{
	private final static long serialVersionUID=1L;
	/** класс, на основании которого нужно получить WebPage*/
	private Class<?> classPage=null;
	/** объект, вызывая методы которого можно получить WebPage*/
	private Object objectPage=null;
	/** имя метода, на вызвав который можно получить WebPage */
	private String methodName=null;
	/** классы параметров либо для конструктора либо для объекта */
	private Class<?>[] parameters=null;
	/** аргументы параметров либо для конструктора либо для объекта */
	private Object[] values=null;
	/** страница, которой следует передать управление */
	private WebPage webPage=null;
	
	private boolean isClassPage=false;
	private boolean isWebPage=false;
	private boolean isClassPageNeedCreate=false;
	private boolean isGetPageFromObject=false;
	private boolean isGetPageAfterCallMethod=false;
	
	/** 
	 * <b> ClassPage </b>
	 * создать объект на который передается управление без необходимости создания самого объекта 
	 * @param classPage - объект, которому необходимо передавать управление 
	 * */
	public RedirectActionPage(Class<?> classPage){
		this.classPage=classPage;
		this.isClassPage=true;
	}

	/** 
	 * <b> WebPage </b>
	 * создать объект на который передается управление без необходимости создания самого объекта 
	 * @param classPage - объект, которому необходимо передавать управление 
	 * */
	public RedirectActionPage(WebPage page){
		this.webPage=page;
		this.isWebPage=true;
	}
	
	/** 
	 * <b> ClassPageNeedCreate</b>
	 * создать объект с указанными параметрами для передачи управления 
	 * @param classPage - класс, объект которого нужно создать
	 * @param parameters - классы параметров, которые нужно получить 
	 * @param values - значения, которые нужно передать в конструктор
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
	 * получить объект WebPage из объекта, на основании вызова метода этого объекта 
	 * @param objectWithPage - объект, который содержит ссылку на страницу и выдаст ее после вызова метода
	 * @param methodName - имя метода, который нужно вызвать для получения WebPage
	 * @param parameters - классы параметров, которые нужно получить 
	 * @param values - значения, которые нужно передать в метод
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
	 * <li>передать управление странице, перед этим вызвав у объекта имя метода</li>
	 * <li>передать управление методу объекта, который сам должен продолжить передачу управления</li>
	 * @param webPage страница, которой будет передано управление
	 * @param object объект, у которого будет вызван метод
	 * @param methodName имя метода, который будет вызван у объекта
	 * @param parameters - (nullable) классы параметров, которые должны быть в объекте ( Сигнатура)
	 * @param values - (nullable) объекты, которые должны быть переданы в качестве аргументов
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
	
	/** вызвать у объекта метод по его(метода) имени 
	 * @param object - объект, у которого вызывается метод
	 * @param methodName - имя метода, который вызывается
	 * @param argumentClasses - (nullable)классы объектов, на основании которых нужно получить метод
	 * @param argumentObjects - (nullable)объекты, которые будут переданы в качестве аргументов
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
	
	/** передача управления другому ресурсу на основании полей данного объекта 
	 * @param component - который будет осуществлять перемещение
	 * @param methodName - (nullable)имя метода, которому нужно передать параметры
	 * @param executeClass - (nullable)массив из классов, к которым относятся объекты 
	 * @param executeObject - (nullable)дополнительные параметры для вызова
	 * <table border=1 style="background:gray">
	 * 	<tr>
	 * 		<th>Ветка</th> <th>Имя метода</th> <th>Параметры</th> <th> Описание </th>
	 * 	</tr>
	 * 	<tr>
	 * 		<td>ClassPage</td> <td>null</td>   <td>any</td>         <td> параметры и/или метод не используются</td>
	 * 	</tr>
	 * 	<tr>
	 * 		<td>ClassPage</td> <td>value</td> <td>any</td>          <td> параметры и/или метод не используются</td>
	 * 	</tr>
	 * 	<tr>
	 * 		<td>WebPage</td> <td>null</td> <td>null</td>  <td> передача управления странице</td>
	 * 	</tr>
	 * 	<tr>
	 * 		<td>WebPage</td> <td>null</td> <td>value</td> <td> параметры и/или метод не используются</td>
	 * 	</tr>
	 * 	<tr>
	 * 		<td>WebPage</td> <td>value</td> <td>null</td> <td> вызов метода у страницы перед передачей управления</td>
	 * 	</tr>
	 * 	<tr>
	 * 		<td>WebPage</td> <td>value</td> <td>value</td> <td> вызов метода c параметрами у страницы перед передачей управления</td>
	 * 	</tr>
	 * 	<tr>
	 * 		<td>ClassPageNeedCreate</td> <td>null</td> <td>null</td>  <td> передача управления созданному объекту из класса</td>
	 * 	</tr>
	 * 	<tr>
	 * 		<td>ClassPageNeedCreate</td> <td>null</td> <td>value</td> <td> добавление к конструктору страницы указанных параметров </td>
	 * 	</tr>
	 * 	<tr>
	 * 		<td>ClassPageNeedCreate</td> <td>value</td> <td>null</td> <td> создание страницы, затем вызов метода у этой страницы</td>
	 * 	</tr>
	 * 	<tr>
	 * 		<td>ClassPageNeedCreate</td> <td>value</td> <td>value</td> <td> создание страницы, затем вызов метода c параметрами у этой страницы</td>
	 * 	</tr>
	 * 	<tr>
	 * 		<td>GetPageFromObject</td> <td>null</td> <td>null</td>  <td> передача управления созданному классу</td>
	 * 	</tr>
	 * 	<tr>
	 * 		<td>GetPageFromObject</td> <td>null</td> <td>value</td> <td> добавление к методу получения страницы указанных параметров </td>
	 * 	</tr>
	 * 	<tr>
	 * 		<td>GetPageFromObject</td> <td>value</td> <td>null</td> <td> получение страницы из объекта, затем вызов метода у этой страницы</td>
	 * 	</tr>
	 * 	<tr>
	 * 		<td>GetPageFromObject</td> <td>value</td> <td>value</td> <td> получение страницы из объекта, затем вызов метода у этой страницы c параметрами</td>
	 * 	</tr>
	 * 	<tr>
	 * 		<td>GetPageAfterCallMethod</td> <td>null</td> <td>null</td>  <td> вызываем метод у объекта, затем передаем управление странице</td>
	 * 	</tr>
	 * 	<tr>
	 * 		<td>GetPageAfterCallMethod</td> <td>null</td> <td>value</td> <td> вызываем метод у объекта с добавленными параметрами, затем пердаем управление странице</td>
	 * 	</tr>
	 * 	<tr>
	 * 		<td>GetPageAfterCallMethod</td> <td>value</td> <td>null</td> <td> вызываем метод у объекта, затем у страницы так же вызываем метод</td>
	 * 	</tr>
	 * 	<tr>
	 * 		<td>GetPageAfterCallMethod</td> <td>value</td> <td>value</td> <td> вызываем метод у объекта, затем у страницы так же вызываем метод c параметрами</td>
	 * 	</tr>
	 * </table> 
	 * */
	public boolean action(org.apache.wicket.Component component,
					      String methodName,
						  Class<?>[] executeClass, 
						  Object[] executeObject){
		boolean returnValue=false;
		if(isClassPage==true){
			debug("action: передать управление классу (без параметров)"); 
			component.setResponsePage(classPage);
		}
		if(isWebPage==true){
			debug("action: передача управления объекту класса WebPage");
			if(methodName==null){
				component.setResponsePage(this.webPage);
			}
			if(methodName!=null){
				debug("action:                         с предварительным вызовом метода ");
				this.callMethodOfObject(this.webPage, methodName, executeClass, executeObject);
				component.setResponsePage(this.webPage);
			}
		}
		if(isClassPageNeedCreate==true){
			debug("action: создать объект с параметрами");
			try{
				if(methodName==null){
					if((executeClass==null)||(executeClass.length==0)){
						Constructor<?> constructor=this.classPage.getConstructor(parameters);
						WebPage page=(WebPage)constructor.newInstance(this.values);
						component.setResponsePage(page);
					}else{
						debug("action:                    добавление параметров к конструктору ");
						Constructor<?> constructor=this.classPage.getConstructor(this.joinClassArray(parameters, executeClass));
						WebPage page=(WebPage)constructor.newInstance(this.joinObjectArray(values, executeObject));
						component.setResponsePage(page);
					}
				}
				if(methodName!=null){
					Constructor<?> constructor=this.classPage.getConstructor(parameters);
					WebPage page=(WebPage)constructor.newInstance(this.values);
					if((executeClass==null)||(executeClass.length==0)){
						debug("action:                    вызов метода после создания страницы ");
						this.callMethodOfObject(page, methodName, null, null);
					}else{
						debug("action:                    вызов метода с параметрами после создания страницы ");
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
			debug("action: передача управления на основании объекта(WebPage), полученного из метода по имени метода");
			if(methodName==null){
				if((executeClass==null)||(executeClass.length==0)){
					Object webPage=callMethodOfObject(this.objectPage,this.methodName,this.parameters,values);
					if((webPage!=null)&&(webPage instanceof WebPage)){
						debug("action: передача управления полученной странице осуществлена ");
						component.setResponsePage((WebPage)webPage);
						returnValue=true;
					}else{
						debug("action: передача управления вызывающему метода закончена ");
						returnValue=true;
					}
				}else{
					debug("action:                                      добавление к методу получения страницы указанных параметров ");
					Object webPage=callMethodOfObject(this.objectPage,this.methodName,this.joinClassArray(this.parameters, executeClass),this.joinObjectArray(this.values,executeObject));
					if((webPage!=null)&&(webPage instanceof WebPage)){
						debug("action: передача управления полученной странице осуществлена ");
						component.setResponsePage((WebPage)webPage);
						returnValue=true;
					}else{
						debug("action: передача управления вызывающему метода закончена ");
						returnValue=true;
					}
				}
			}
			if(methodName!=null){
				Object webPage=callMethodOfObject(this.objectPage,this.methodName,this.parameters,values);
				if((executeClass==null)||(executeClass.length==0)){
					debug("action:                                      вызов метода у страницы перед передачей ей управления ");
					callMethodOfObject(webPage,methodName,null,null);
				}else{
					debug("action:                                      вызов метода у страницы с параметрами перед передачей ей управления ");
					callMethodOfObject(webPage,methodName,executeClass,executeObject);
				}
				component.setResponsePage((WebPage)webPage);
				returnValue=true;
			}
		}
		if(isGetPageAfterCallMethod==true){
			debug("action: передача управления на основании WebPage, с предварительным вызовом метода");
			if(methodName==null){
				if((executeClass==null)||(executeClass.length==0)){
					callMethodOfObject(this.objectPage,this.methodName,this.parameters,this.values);
					component.setResponsePage(this.webPage);
					returnValue=true;
				}else{
					debug("action:                                             добавление параметров в вызов метода ");
					callMethodOfObject(this.objectPage,this.methodName,this.joinClassArray(parameters, executeClass),this.joinObjectArray(values, executeObject));
					component.setResponsePage(this.webPage);
					returnValue=true;
				}
			}
			if(methodName!=null){
				if((executeClass==null)||(executeClass.length==0)){
					debug("action:                                   вызов метода у страницы перед передачей управления");
					callMethodOfObject(this.objectPage,this.methodName,this.parameters,this.values);
					callMethodOfObject(this.webPage,methodName,null,null);
					component.setResponsePage(this.webPage);
					returnValue=true;
				}else{
					debug("action:                                   вызов метода c параметрами у страницы перед передачей управления");
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
	
	/** слияние объектов Class[] */
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

	/** слияние объектов Object[] */
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
