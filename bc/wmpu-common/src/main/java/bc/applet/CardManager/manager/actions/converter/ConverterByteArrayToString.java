package bc.applet.CardManager.manager.actions.converter;



public class ConverterByteArrayToString extends ActionStepConverter{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
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

	/** Объект, который получает в качестве параметра byte[] и возвращает String */
	public ConverterByteArrayToString(){
		
	}

	@Override
	public Object convert(Object object_for_convert) {
		String return_value=null;
		try{
			return_value=hexDump((byte[])object_for_convert);
		}catch(Exception ex){
			
		}
		return return_value;
	}
	
}
