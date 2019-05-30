package bc.applet.CardManager.manager.actions.calculator;

import java.io.Serializable;

import bc.applet.CardManager.manager.actions.Action;



public abstract class ActionStepCalculator implements Serializable{
	/** unique serial number for Serializable */
	private static final long serialVersionUID = 1L;

	public abstract boolean calculate(Action action);
}
