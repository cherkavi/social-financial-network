package bc.applet.CardManager.manager.actions.calculator;

import org.apache.log4j.Logger;

import bc.applet.CardManager.manager.actions.Action;

public class CalculatorTailOfByteArray extends ActionStepCalculator{
	private final static Logger LOGGER=Logger.getLogger(CalculatorTailOfByteArray.class);
	
	/** */
	private static final long serialVersionUID = 1L;

	private String field_name_source;
	private String field_name_destination;
	private int field_count;
	/** взять из хранилища промежуточных переменных (по имени источника) значение типа byte[] <br> 
	 * получить указанное кол-во начальных байт и положить в хранилище промежуточных переменных ( по имени приемника)
	 * @param name_source имя переменной-источника
	 * @param name_destination имя переменной приемника
	 * @param count кол-во элементов в итоговом массиве
	 */
	public CalculatorTailOfByteArray(String param_source, String param_destination, int count){
		this.field_name_source=param_source;
		this.field_name_destination=param_destination;
		this.field_count=count;
	}
	
	private byte[] headBytes(byte[] data, int len) {
		int real_length=data.length-(len);
		if(real_length>0){
			real_length=len;
		}else{
			real_length=(data.length);
		}
		byte[] res = new byte[real_length];
		for (int i = data.length-real_length-1; i < data.length; i++)
			res[i] = data[i];
		return res;
	}
	
	@Override
	public boolean calculate(Action action) {
		try{
			action.setParameter(field_name_destination, headBytes((byte[])action.getParameter(field_name_source),this.field_count));
		}catch(Exception ex){
			LOGGER.error("Exception:"+ex.getMessage());
			return false;
		}
		return true;
	}

}
