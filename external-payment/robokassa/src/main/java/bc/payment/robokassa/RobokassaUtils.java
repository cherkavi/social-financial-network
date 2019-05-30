package bc.payment.robokassa;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;


/**
 * utility methods for Robokassa
 */
public class RobokassaUtils {
	/**
	 * user id into WCM system
	 */
	public static final String WCM_USER_ID="wcm_user_id";
	/**
	 *  HTTP Request parameter to/from Robokassa <br />
	 *  Идентификатор магазина в ROBOKASSA <br />
	 *  {@link https://www.robokassa.ru/ru/Doc/Ru/Interface.aspx}
	 */
	public static final String ROBOPARAM_MERCHANT_LOGIN="MerchantLogin";
	
	/**
	 *  HTTP Request parameter to/from Robokassa <br />
	 *  Номер счета в магазине. <br />
	 *  {@link https://www.robokassa.ru/ru/Doc/Ru/Interface.aspx}
	 */
	public static final String ROBOPARAM_INV_ID="InvId";
	
	/**
	 *  HTTP Request parameter to/from Robokassa <br />
	 *  Требуемая к получению сумма <br />
	 *  {@link https://www.robokassa.ru/ru/Doc/Ru/Interface.aspx}
	 */
	public static final String ROBOPARAM_OUT_SUM="OutSum";
	
	/**
	 *  HTTP Request parameter to/from Robokassa <br />
	 *  Контрольная сумма MD5 <br />
	 *  {@link https://www.robokassa.ru/ru/Doc/Ru/Interface.aspx}
	 */
	public static final String ROBOPARAM_SIGN_VALUE="SignatureValue";
	
	/**
	 * ship item - code of goods
	 */
	public static final String ROBOPARAM_SHIP_ITEM="shp_Item";
	
	private static final int MAX_LENGTH=100;
	private static final String ACCESSIBLE_PATTERN="[^A-zА-я0-9 ,.]*"; 
	
	/**
	 * fix Description for Robokassa, according rules of External Service 
	 * @param rawValue
	 * @return
	 */
	public static String correctDescription(String rawValue){
		String clearValue=rawValue.replaceAll(ACCESSIBLE_PATTERN, "");
		if(clearValue.length()>MAX_LENGTH){
			return clearValue.substring(0,MAX_LENGTH);
		}
		return clearValue;
	}

	private final static String CRC_DELIMITER=":";
	
	/**
	 * calculate CRC
	 * @param values
	 * @return
	 */
	public static String crc(String ... values) {
		if(values==null || values.length==0){
			return StringUtils.EMPTY;
		}
		StringBuilder returnValue=new StringBuilder();
		for(String eachValue:values){
			if(returnValue.length()>0){
				returnValue.append(CRC_DELIMITER);
			}
			returnValue.append(eachValue);
		}
		return DigestUtils.md5Hex(returnValue.toString());
	}
	
	/**
	 * calculate CRC
	 * @param values
	 * @return
	 */
	public static String crcWithShipItem(String shipItem, String ... values) {
		if(values==null || values.length==0){
			return StringUtils.EMPTY;
		}
		StringBuilder returnValue=new StringBuilder();
		for(String eachValue:values){
			returnValue.append(eachValue);
			returnValue.append(CRC_DELIMITER);
		}
		returnValue.append(ROBOPARAM_SHIP_ITEM+"=");
		returnValue.append(shipItem);
		return DigestUtils.md5Hex(returnValue.toString());
	}

	/**
	 * calculate CRC and compare with expected
	 * @param expectedCrc
	 * @param values
	 * @return
	 */
	public static boolean isCrcValid(String expectedCrc, String ... values){
		String calculatedCrc=crc(values);
		return calculatedCrc.equalsIgnoreCase(expectedCrc);
	}
	
	/**
	 * when Robokassa send response as request to our system with next GET/POST parameters:
	 * @param InvId - number of order
	 * @param OutSum - amount of payment
	 * @param SignatureValue - signature 
	 * @param robokassaPassword2 password #2 of Robokassa
	 * @return
	 * <ul>
	 * 	<li><b>true</b> - request is valid </li>
	 * 	<li><b>false</b> - bad request </li>
	 * </ul>
	 */
	public static boolean isCrcResultFromRobokassaValid(String InvId, String OutSum, String SignatureValue, String robokassaPassword2){
		String calculateCrc=crc(OutSum, InvId, robokassaPassword2);
		return calculateCrc.equalsIgnoreCase(SignatureValue);
	}
	
}
