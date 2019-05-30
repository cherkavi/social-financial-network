package bc.applet.CardManager.manager.actions.condition;

import bc.applet.CardManager.manager.actions.Action;

public class ConditionStatic extends ActionStepCondition{
	/** serialize number */
	private static final long serialVersionUID = 1L;
	private boolean field_return_value=false;
	/** постоянный параметр, который возвращается в условии*/
	public ConditionStatic(boolean return_value){
		this.field_return_value=return_value;
	}
	
	@Override
	public boolean isCondition(Action action) {
		return field_return_value;
	}
	

}
