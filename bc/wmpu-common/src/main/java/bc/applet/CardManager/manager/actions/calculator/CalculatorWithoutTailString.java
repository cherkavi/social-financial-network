package bc.applet.CardManager.manager.actions.calculator;

import bc.applet.CardManager.manager.actions.Action;

public class CalculatorWithoutTailString extends ActionStepCalculator{

	/** */
	private static final long serialVersionUID = 1L;
	private String field_parameter_source;
	private String field_parameter_destination;
	private int field_count;
	
	/** взять параметр-источник (String) вырезать из него конечную часть (хвост) с указанным кол-вом символов и оставшуюся часть положить в приемник 
	 * @param parameter_source имя параметра-источника
	 * @param parameter_destination имя параметра-приемника
	 * @param count кол-во символов, которые нужно брать 
	 * */
	public CalculatorWithoutTailString(String parameter_source,String parameter_destination, int count){
		this.field_parameter_source=parameter_source;
		this.field_parameter_destination=parameter_destination;
		this.field_count=count;
	}
	
	@Override
	public boolean calculate(Action action) {
		boolean return_value=false;
		try{
			action.setParameter(field_parameter_destination, ((String)action.getParameter(field_parameter_source)).substring(0, ((String)action.getParameter(field_parameter_source)).length()-this.field_count));
			return_value=true;
		}catch(Exception ex){
			return_value=false;
		}
		return return_value;
	}
	
}
