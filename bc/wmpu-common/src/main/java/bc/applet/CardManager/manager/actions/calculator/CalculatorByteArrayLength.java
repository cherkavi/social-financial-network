package bc.applet.CardManager.manager.actions.calculator;

import bc.applet.CardManager.manager.actions.Action;

public class CalculatorByteArrayLength extends ActionStepCalculator{

	/** */
	private static final long serialVersionUID = 1L;
	private String field_parameter_source;
	private String field_parameter_destination;
	
	/** взять параметр-источник (byte[]) и положить в приемник (Integer) кол-во байт в массиве 
	 * @param parameter_source имя параметра-источника
	 * @param parameter_destination имя параметра-приемника
	 * @param count кол-во символов, которые нужно брать 
	 * */
	public CalculatorByteArrayLength(String parameter_source,String parameter_destination){
		this.field_parameter_source=parameter_source;
		this.field_parameter_destination=parameter_destination;
	}
	
	@Override
	public boolean calculate(Action action) {
		boolean return_value=false;
		try{
			action.setParameter(field_parameter_destination, new Integer(((byte[])action.getParameter(field_parameter_source)).length));
			return_value=true;
		}catch(Exception ex){
			return_value=false;
		}
		return return_value;
	}
	
}
