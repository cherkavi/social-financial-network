package bc.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * utility class for currency 
 */
public class ConvertUtils {
	private final static int DIGITS_AFTER_COMMA=2;
	private final static BigDecimal HUNDRED=new BigDecimal("100");
	
	private ConvertUtils(){
	}
	
	public static BigDecimal divideByHundred(BigDecimal value){
		if(value==null){
			return BigDecimal.ZERO.setScale(DIGITS_AFTER_COMMA, RoundingMode.CEILING);
		}
		return value.divide(HUNDRED).setScale(DIGITS_AFTER_COMMA, RoundingMode.CEILING);
//		double realValue=((double)value.longValue())/100;
//		return new BigDecimal(realValue).setScale(DIGITS_AFTER_COMMA, RoundingMode.CEILING);
	}
	
	public static int getCents(BigDecimal amount){
		if(amount==null){
			return 0;
		}
		return amount.multiply(new BigDecimal(100)).intValue();
	}
	
	
}
