package bc.applet.CardManager.manager.actions.condition;

import bc.applet.CardManager.manager.actions.Action;

/** возвращает TRUE, если параметр не существует */
public class ConditionParameterNotExists extends ActionStepCondition{
	private static final long serialVersionUID = 1L;
	private String field_parameter_name=null;
	
	/** Возвращает TRUE, если параметр с заданным именем не существует 
	 * в Промежуточном хранилище */
	public ConditionParameterNotExists(String parameter_name){
		this.field_parameter_name=parameter_name;
	}

	@Override
	public boolean isCondition(Action action) {
		return !(action.isParameterExists(field_parameter_name));
	}
}
