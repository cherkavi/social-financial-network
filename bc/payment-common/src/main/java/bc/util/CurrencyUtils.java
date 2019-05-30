package bc.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class CurrencyUtils {
	
	/**
	 * convert from BigDecimal value to user representation with 2 digits after comma
	 * @param value
	 * @return
	 */
	public static String amountToString(BigDecimal value){
		BigDecimal bd = new BigDecimal(value.toString()).setScale(2, BigDecimal.ROUND_HALF_DOWN);
		DecimalFormat df = new DecimalFormat();
		df.setMaximumFractionDigits(2);
		df.setMinimumFractionDigits(2);
		df.setGroupingUsed(false);
		return df.format(bd).replace(',', '.'); // for some platforms		
	}
	
}
