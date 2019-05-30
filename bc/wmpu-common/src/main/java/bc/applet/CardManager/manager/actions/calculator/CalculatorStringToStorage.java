package bc.applet.CardManager.manager.actions.calculator;

import bc.applet.CardManager.manager.actions.Action;

public class CalculatorStringToStorage extends ActionStepCalculator{

	/** */
	private static final long serialVersionUID = 1L;
	private String field_parameter_source;
	private String field_value;
	
	/** положить в хранилище статическое значение в виде String 
	 * @param parameter_source имя параметра-источника
	 * @param value значение, которое нужно положить в хранилище
	 * */
	public CalculatorStringToStorage(String parameter_source,String value){
		this.field_parameter_source=parameter_source;
		this.field_value=value;
	}
	
	@Override
	public boolean calculate(Action action) {
		boolean return_value=false;
		try{
			action.setParameter(field_parameter_source, field_value);
			return_value=true;
		}catch(Exception ex){
			return_value=false;
		}
		return return_value;
	}
	
}
