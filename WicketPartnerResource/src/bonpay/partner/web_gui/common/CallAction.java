package bonpay.partner.web_gui.common;

import java.lang.reflect.Method;
import java.io.Serializable;
import org.apache.wicket.ajax.AjaxRequestTarget;


/** класс, который содержит необходимую информацию по передаче управления объекту */
public class CallAction implements Serializable{
	private final static long serialVersionUID=1L;
	private Object object;
	private String methodName;
	private Class<?>[] executeClass;
	private Object[] executeObject;
	
	/** объект, который вызывает у переданного в качестве параметра объекта метод
	 * @param object - объект, которому нужно передать управление
	 * @param methodName - метод, который нужно вызвать ( должен быть параметр AjaxRequestTarget)
	 * */
	public CallAction(Object object, String methodName){
		this.object=object;
		this.methodName=methodName;
	}
	
	/** объект, который вызывает у переданного в качестве параметра объекта метод
	 * @param object - объект, которому нужно передать управление
	 * @param methodName - метод, который нужно вызвать ( должен быть параметр AjaxRequestTarget)
	 * */
	public CallAction(Object object, String methodName,Class<?>[] executeClass, Object[] executeObject){
		this.object=object;
		this.methodName=methodName;
		this.executeClass=executeClass;
		this.executeObject=executeObject;
	}
	
	
	
	/** вызвать метод для Ajax ссылки*/
	public void execute(AjaxRequestTarget target){
		try{
			Class<?> objectClass=object.getClass();
			if(this.executeClass!=null){
				Method method=objectClass.getMethod(this.methodName, this.joinClassArray(new Class<?>[]{target.getClass()},this.executeClass));
				method.invoke(object,this.joinObjectArray(new Object[]{target}, this.executeObject));
			}else{
				Method method=objectClass.getMethod(this.methodName, target.getClass());
				method.invoke(object,target);
			}
		}catch(Exception ex){
			System.err.println("CallAction exception: "+ex.getMessage());
		}
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
	
}
