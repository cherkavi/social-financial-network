package bc.applet.CardManager.manager.actions.converter;

import java.io.Serializable;

/** класс, который является базовым для всех Преобразований */
public abstract class ActionStepConverter implements Serializable{
	/** unique serial number for Serializable */
	private static final long serialVersionUID = 1L;

	/**  тип переменной для сравнения - byte[] */
	public final static int TYPE_BYTEARRAY=0;
	/**  тип переменной для сравнения - String */
	public final static int TYPE_STRING=1;
	/**  тип переменной для сравнения - Integer(int) */
	public final static int TYPE_INT=2;
	
	/** передаем оригинал объекта, и возвращаем обработанный объект*/
	public abstract Object convert(Object object_for_convert);
}
