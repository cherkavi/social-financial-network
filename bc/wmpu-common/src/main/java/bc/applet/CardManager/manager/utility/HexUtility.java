package bc.applet.CardManager.manager.utility;

import java.math.BigInteger;


/** данный класс отвечает за преобразования (byte[]-String String-byte[])*/
public class HexUtility {

	public final static String hexChars[] = { "0", "1", "2", "3", "4", "5",
		"6", "7", "8", "9", "A", "B", "C", "D", "E", "F" };
	
	public static int unsignedInt(int a) {
		if (a < 0) {
			return a + 256;
		}
		return a;
	}

	public static int min(int a, int b) {
		if (a < b) {
			return a;
		}
		return b;
	}

	public static byte stringCharToBCDByte(String data, int pos) {
		return (byte) (Integer.parseInt(data.substring(pos, pos + 2), 16));
	}
	
	
	/** получить в виде HEX текста */
	public static String hexDump(BigInteger big) {
		return hexDump(big.toByteArray());
	}
	/** получить в виде HEX текста */
	public static String hexDump(byte[] data) {
		return hexDump(data, 0, data.length);
	}
	/** получить в виде HEX текста */
	public static String hexDump(byte[] data, int length) {
		return hexDump(data, 0, length);
	}
	/** получить в виде HEX текста */
	public static String hexDump(byte[] data, int offset, int length) {
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

}
