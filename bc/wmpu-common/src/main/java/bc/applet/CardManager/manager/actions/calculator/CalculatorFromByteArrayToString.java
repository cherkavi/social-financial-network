package bc.applet.CardManager.manager.actions.calculator;

import org.apache.log4j.Logger;

import bc.applet.CardManager.manager.actions.Action;

public class CalculatorFromByteArrayToString extends ActionStepCalculator{
	private final static Logger LOGGER=Logger.getLogger(CalculatorFromByteArrayToString.class);
	
	/** unique serial number */
	private static final long serialVersionUID = 1L;
	/** имя источника */
	private String field_name_source;
	/** имя приемника */
	private String field_name_destination;

	private final static String hexChars[] = { "0", "1", "2", "3", "4", "5",
		"6", "7", "8", "9", "A", "B", "C", "D", "E", "F" };
	
	private static int unsignedInt(int a) {
		if (a < 0) {
			return a + 256;
		}
		return a;
	}

	private static int min(int a, int b) {
		if (a < b) {
			return a;
		}
		return b;
	}

	/** получить в виде HEX текста */
	private static String hexDump(byte[] data) {
		return hexDump(data, 0, data.length);
	}
	/** получить в виде HEX текста */
	private static String hexDump(byte[] data, int offset, int length) {
		String result = "";
		String part = "";
		for (int i = 0; i < min(data.length, length); i++) {
			part = ""
					+ hexChars[(byte) (unsignedInt(data[offset + i]) / 16)]
					+ hexChars[(byte) (unsignedInt(data[offset + i]) % 16)];
			result = result + part;
		}
		return result;
	}
	
	/** взять значение из источника и положить данное значение в приемник, преобразовав из byte[] в HexString
	 * @param parameter_name_source - имя источника ( по имени нужно брать значение из промежуточного хранилища)
	 * @param parameter_name_destination - имя приемника ( нужно положить по данному имени полученное значение
	 * */
	public CalculatorFromByteArrayToString(String parameter_name_source, String parameter_name_destination){
		this.field_name_source=parameter_name_source;
		this.field_name_destination=parameter_name_destination;
	}

	@Override
	public boolean calculate(Action action) {
		try{
			action.setParameter(field_name_destination, 
								hexDump((byte[])action.getParameter(field_name_source)));
			return true;
		}catch(Exception ex){
			LOGGER.error(" Source:"+field_name_source+"  Destination: "+field_name_destination+" Exception :"+ex.getMessage());
			return false;
		}
	}
}
