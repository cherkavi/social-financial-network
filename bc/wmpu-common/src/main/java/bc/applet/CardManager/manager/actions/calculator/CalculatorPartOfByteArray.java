package bc.applet.CardManager.manager.actions.calculator;

import org.apache.log4j.Logger;

import bc.applet.CardManager.manager.actions.Action;

public class CalculatorPartOfByteArray extends ActionStepCalculator{
	private final static Logger LOGGER=Logger.getLogger(CalculatorPartOfByteArray.class);
	
	/** */
	private static final long serialVersionUID = 1L;
	/** индекс начала*/
	private int field_index_begin;
	/** кол-во байт, которые нужно получить */
	private int field_count;
	private String field_name_source;
	private String field_name_destination;
	/** взять из хранилища промежуточных переменных (по имени источника) значение типа byte[] 
	 * вырезать из него указанную часть и положить в хранилище промежуточных переменных ( по имени приемника)
	 * @param name_source имя переменной-источника
	 * @param name_destination имя переменной приемника
	 * @param index_begin индекс начала
	 * @param count кол-во элементов в итоговом массиве
	 */
	public CalculatorPartOfByteArray(String name_source, String name_destination, int index_begin, int count){
		this.field_name_source=name_source;
		this.field_name_destination=name_destination;
		this.field_index_begin=index_begin;
		this.field_count=count;
	}
	
	private byte[] selectBytes(byte[] data, int pos, int len) {
		int real_length=data.length-(len+pos+1);
		if(real_length>0){
			real_length=len;
		}else{
			real_length=(data.length-pos-1);
		}
		byte[] res = new byte[real_length];
		for (int i = 0; i < real_length; i++)
			res[i] = data[pos + i];
		return res;
	}
	
	@Override
	public boolean calculate(Action action) {
		try{
			action.putParameter(field_name_destination, selectBytes((byte[])action.getParameter(field_name_source),this.field_index_begin,this.field_count));
		}catch(Exception ex){
			LOGGER.error(" Source:"+this.field_name_source+" Destination:"+this.field_name_destination+" Exception: ");
			return false;
		}
		return true;
	}
}
