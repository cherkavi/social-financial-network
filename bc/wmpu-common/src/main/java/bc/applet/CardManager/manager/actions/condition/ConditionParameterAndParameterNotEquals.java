package bc.applet.CardManager.manager.actions.condition;

import java.util.Arrays;

import bc.applet.CardManager.manager.actions.Action;


/** проверка на Not Equals двух значений из Промежуточного хранилища */
public class ConditionParameterAndParameterNotEquals extends ActionStepCondition{
	/**
	 */
	private static final long serialVersionUID = 1L;
	/** имя параметра из хранилища */
	private String field_parameter_name_source;
	private String field_parameter_name_destination;
	private int field_flag_parameter_type=0;
	
	
	/** 
	 * Проверка на Not Equals двух переменных из Промежуточного хранилища
	 * @param parameter_name_source
	 * @param parameter_name_destination
	 * @param type - тип для сравнения: <br>
	 *  <b>byte[]</b><br>
	 *  <b>String</b><br>
	 *  <b>Integer(int)</b><br>
	 * */
	public ConditionParameterAndParameterNotEquals(String parameter_name_source,String parameter_name_destination, int type){
		this.field_parameter_name_source=parameter_name_source;
		this.field_parameter_name_destination=parameter_name_destination;
		this.field_flag_parameter_type=type;
	}
	@Override
	public boolean isCondition(Action action) {
		boolean return_value=false;
		switch(field_flag_parameter_type){
			case TYPE_BYTEARRAY: {
				try{
					return_value=!(Arrays.equals(action.getParameterByte(this.field_parameter_name_source),action.getParameterByte(this.field_parameter_name_destination)));
				}catch(Exception ex){}
			};break;
			case TYPE_STRING: {
				try{
					return_value=!(action.getParameterString(this.field_parameter_name_source).equalsIgnoreCase(action.getParameterString(this.field_parameter_name_destination)));
				}catch(Exception ex){}
			};break;
			case TYPE_INT: {
				try{
					return_value=(action.getParameterInt(this.field_parameter_name_source)!=action.getParameterInt(this.field_parameter_name_destination));
				}catch(Exception ex){}
			};break;
			default: return_value=false;
		}
		return return_value;
	}
 
}
