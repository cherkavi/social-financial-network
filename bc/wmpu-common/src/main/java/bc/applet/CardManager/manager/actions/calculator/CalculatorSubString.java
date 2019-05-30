package bc.applet.CardManager.manager.actions.calculator;

import org.apache.log4j.Logger;

import bc.applet.CardManager.manager.actions.Action;

public class CalculatorSubString extends ActionStepCalculator{
	private final static Logger LOGGER=Logger.getLogger(CalculatorSubString.class);
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private boolean field_flag_one_parameter=true;
	private int field_begin_index=0;
	private int field_end_index=0;
	private String field_name_source;
	private String field_name_destination;
	
	/** получить значение источника (String) из Промежуточного хранилища и сохранить(String) в получателя в Промежуточном хранилище 
	 * @param parameter_name_source имя параметра-источника в промежуточном хранилище данных
	 * @param parameter_name_destination имя параметра приемника в промежуточном хранилище данных
	 * @param begin_index индекс начала
	 * @param end_index индекс окончания 
	 */
	public CalculatorSubString(String parameter_name_source, 
							   String parameter_name_destination, 
							   int begin_index, 
							   int end_index){
		this.field_name_source=parameter_name_source;
		this.field_name_destination=parameter_name_destination;
		this.field_begin_index=begin_index;
		this.field_end_index=end_index;
		field_flag_one_parameter=false;
	}

	/** получить значение источника (String) из Промежуточного хранилища и сохранить(String) в получателя в Промежуточном хранилище 
	 * @param parameter_name_source имя параметра-источника в промежуточном хранилище данных
	 * @param parameter_name_destination имя параметра приемника в промежуточном хранилище данных
	 * @param begin_index индекс начала
	 * @param end_index индекс окончания 
	 */
	public CalculatorSubString(String parameter_name_source, 
							   String parameter_name_destination, 
							   int begin_index){
		this.field_name_source=parameter_name_source;
		this.field_name_destination=parameter_name_destination;
		this.field_begin_index=begin_index;
		field_flag_one_parameter=true;
	}
	
	@Override
	public boolean calculate(Action action) {
		try{
			if(this.field_flag_one_parameter){
				action.setParameter(field_name_destination, action.getParameterString(field_name_source).substring(this.field_begin_index));
			}else{
				action.setParameter(field_name_destination,action.getParameterString(field_name_source).substring(this.field_begin_index, this.field_end_index));
			}
		}catch(Exception ex){
			LOGGER.error("Exception: "+ex.getMessage());
			return false;
		}
		return true;
	}
}
