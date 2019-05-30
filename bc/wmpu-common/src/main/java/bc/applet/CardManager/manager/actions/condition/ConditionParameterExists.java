package bc.applet.CardManager.manager.actions.condition;

import bc.applet.CardManager.manager.actions.Action;

/** проверяет, существует ли параметр в промежуточном хранилище с указанным именем */
public class ConditionParameterExists extends ActionStepCondition{
	private static final long serialVersionUID = 1L;
	
	private String field_parameter_name=null;
	
	/** Возвращает TRUE если существует параметр с заданным именем в Промежуточном хранилище 
	 * @param parameter_name 
	 */
	public ConditionParameterExists(String parameter_name){
		this.field_parameter_name=parameter_name;
	}

	@Override
	public boolean isCondition(Action action) {
		return action.isParameterExists(field_parameter_name);
	}
}
