package utility.model;

import java.lang.reflect.Method;

/** объект, содержащий необходимое знечение для объекта */
public abstract class Model<T> implements IModel<T>{
	private Object model;
	private String propertyName;
	
	/** метод для получения данных */
	private Method methodGet;
	
	/** метод для установки данных */
	private Method methodSet;
	
	
	/** объект, содержащий необходимое значение для объекта  
	 * @param model - объект, из которого нужно получать значения 
	 * @param propertyName - имя свойства, по которому нужно получать значение 
	 * (будет сформировано getPropertyName и setPropertyName)
	 */
	public Model(Object model, String propertyName){
		this.model=model;
		this.propertyName=propertyName;
		this.methodGet=this.getMethod(this.model, this.getProperty(this.propertyName));
		this.methodSet=this.setMethod(this.model, this.setProperty(this.propertyName));
	}
	
	/** получить метод GET  */
	private Method getMethod(Object object, String methodName){
		try{
			return object.getClass().getMethod(methodName);
		}catch(Exception ex){
			System.err.println("Get Method Exception: "+ex.getMessage());
			return null;
		}
	}
	
	/** получить метод SET  */
	private Method setMethod(Object object, String methodName){
		try{
			return object.getClass().getMethod(methodName, this.getGenericClass());
		}catch(Exception ex){
			System.err.println("Get Method Exception: "+ex.getMessage());
			return null;
		}
	}
	
	/** получить класс сгенерируемого объекта ( исправление невозможности вызова класса для параметризированного значения ) */
	public abstract Class<T> getGenericClass();
	
	/** получить getProperty на основании property*/
	private String getProperty(String propertyName){
		if((this.propertyName!=null)&&(this.propertyName.length()>0)){
			String value=propertyName.trim();
			String firstLetter=(new String(new char[]{value.charAt(0)})).toUpperCase();
			value=firstLetter+value.substring(1);
			return "get"+value;
		}else{
			return null;
		}
	}
	
	/** получить setProperty на основании property*/
	private String setProperty(String propertyName){
		if((this.propertyName!=null)&&(this.propertyName.length()>0)){
			String value=propertyName.trim();
			String firstLetter=(new String(new char[]{value.charAt(0)})).toUpperCase();
			value=firstLetter+value.substring(1);
			return "set"+value;
		}else{
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public T getModelValue() {
		try{
			return (T)this.methodGet.invoke(this.model);
		}catch(Exception ex){
			System.err.println("getModelValue:"+ex.getMessage());
			return null;
		}
	}

	@Override
	public void setModelValue(T value) {
		try{
			this.methodSet.invoke(this.model, value);
		}catch(Exception ex){
			System.err.println("setModelValue:"+ex.getMessage());
		}
	}


	/** тестирование данной модели  */
	public static void main(String[] args){
		Example example=new Example();
		Model<String> model=new Model<String>(example, "stringValue"){
			@Override
			public Class<String> getGenericClass() {
				return String.class;
			}
		};
		model.setModelValue("this is model value");
		System.out.println("ModelValue:"+model.getModelValue());
	}
	
}

class Example{
	public Example()
	{
	}
	
	private String stringValue;
	
	/** получить значение */
	public String getStringValue(){
		return this.stringValue;
	}
	
	/** установить значение */
	public void setStringValue(String value){
		this.stringValue=value;
	}
}