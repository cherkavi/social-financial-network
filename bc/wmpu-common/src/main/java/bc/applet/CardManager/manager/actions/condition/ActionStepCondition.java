package bc.applet.CardManager.manager.actions.condition;

import java.io.Serializable;

import bc.applet.CardManager.manager.actions.Action;


/** 
 * Класс, который отвечает за обработку условий в пакете ActionStep
 * <b>Основная задача:</b> - сравнение различных, заранее заданных объектов или объектов из хранилища, с объектами из хранилища
 * @author cherkashinv
 */
public abstract class ActionStepCondition implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**  тип переменной для сравнения - byte[] */
	public final static int TYPE_BYTEARRAY=0;
	/**  тип переменной для сравнения - String */
	public final static int TYPE_STRING=1;
	/**  тип переменной для сравнения - Integer(int) */
	public final static int TYPE_INT=2;
	
	/** 
	 * @param action - текущий Action на основании которого выводится решение
	 */
	public abstract boolean isCondition(Action action);
}









