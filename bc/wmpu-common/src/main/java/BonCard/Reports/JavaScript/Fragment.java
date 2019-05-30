package BonCard.Reports.JavaScript;

/** данный класс является общим для JavaScript и для Server Side <br>
 * он является единицей общения между клиентом и сервером
 * */
public class Fragment {
	/** Имя запрошенной функции */
	private String name;
	/** Параметры квитанции/запроса-ответа */
	private String[] parameters;
	/** Двухмерный массив из элементов, для получения параметров на сервер*/
	private String[][] values;
	/** Строка для хранения ИД сессии*/
	private String sessionId;
	
	public Fragment(){
		this.name="error";
		this.parameters=new String[]{"empty","empty"};
		this.values=new String[][]{};
	}
	
	/** Getter's and Setter's*/
	/** установить Values*/
	public void setValues(String[][] value){
		this.values=value;
	}
	/** получить Values*/
	public String[][] getValues(){
		return this.values;
	}
	
	/** устновить название функции */
	public void setName(String value){
		this.name=value;
	}
	/** получить название функции */
	public String getName(){
		return this.name;
	}
	/** установить значения параметров*/
	public void setParameters(String[] value){
		this.parameters=value;
	}
	/** получить значения параметров*/
	public String[] getParameters(){
		return this.parameters;
	}
	
	public void setSessionId(String value){
		this.sessionId=value;
	}
	
	public String getSessioniId(){
		return this.sessionId;
	}
}
