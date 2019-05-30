package bc.applet.CardManager.manager.actions.condition;

import java.util.Arrays;

import bc.applet.CardManager.manager.actions.Action;


/** проверка на NotEquals указанного значения и значения из хранилища */
public class ConditionParameterNotEquals extends ActionStepCondition{
	/**
	 */
	private static final long serialVersionUID = 1L;
	/** параметр, который нужно сравнить*/
	private Object field_parameter;
	/** имя параметра из хранилища */
	private String field_parameter_name;
	private int field_flag_parameter_type=0;
	
	private final static int TYPE_BYTEARRAY=0;
	private final static int TYPE_STRING=1;
	private final static int TYPE_INT=2;
	
	/** Сравнить параметр на неравенство в Промежуточном хранилище и int значение
	 * @param parameter_name имя значения из промежуточного хранилища 
	 * @param parameter сравниваемое значение
	 */
	public ConditionParameterNotEquals(String parameter_name,int parameter){
		this.field_parameter_name=parameter_name;
		this.field_parameter=new Integer(parameter);
		this.field_flag_parameter_type=TYPE_INT;
	}

	/** Сравнить параметр на неравенство в Промежуточном хранилище и String значение
	 * @param parameter_name имя значения из промежуточного хранилища 
	 * @param parameter сравниваемое значение
	 */
	public ConditionParameterNotEquals(String parameter_name, String parameter){
		this.field_parameter_name=parameter_name;
		this.field_parameter=parameter;
		this.field_flag_parameter_type=TYPE_STRING;
	}
	
	/** Сравнить параметр на неравенство в Промежуточном хранилище и byte[] значение
	 * @param parameter_name имя значения из промежуточного хранилища 
	 * @param parameter сравниваемое значение
	 */
	public ConditionParameterNotEquals(String parameter_name,byte[] parameter){
		this.field_parameter_name=parameter_name;
		this.field_parameter=parameter;
		this.field_flag_parameter_type=TYPE_BYTEARRAY;
	}
	@Override
	public boolean isCondition(Action action) {
		boolean return_value=false;
		switch(field_flag_parameter_type){
			case TYPE_BYTEARRAY: {
				try{
					return_value=!Arrays.equals(((byte[])this.field_parameter),action.getParameterByte(this.field_parameter_name));
				}catch(Exception ex){}
			};break;
			case TYPE_STRING: {
				try{
					return_value=!((String)this.field_parameter).equalsIgnoreCase(action.getParameterString(this.field_parameter_name));
				}catch(Exception ex){}
			};break;
			case TYPE_INT: {
				try{
					return_value=((Integer)this.field_parameter).intValue()!=action.getParameterInt(this.field_parameter_name);
				}catch(Exception ex){}
			};break;
			default: return_value=false;
		}
		return return_value;
	}
 
}
