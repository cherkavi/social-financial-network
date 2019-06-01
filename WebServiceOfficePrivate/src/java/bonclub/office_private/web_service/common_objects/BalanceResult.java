package bonclub.office_private.web_service.common_objects;

import java.io.Serializable;
import java.math.BigDecimal;

public class BalanceResult implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private BigDecimal cd_currency; 
	private String name_currency;
	private String sname_currency; 
	private BigDecimal value_club_card_purse;
	public BigDecimal getCd_currency() {
		return cd_currency;
	}
	public void setCd_currency(BigDecimal cdCurrency) {
		cd_currency = cdCurrency;
	}
	public String getName_currency() {
		return name_currency;
	}
	public void setName_currency(String nameCurrency) {
		name_currency = nameCurrency;
	}
	public String getSname_currency() {
		return sname_currency;
	}
	public void setSname_currency(String snameCurrency) {
		sname_currency = snameCurrency;
	}
	public BigDecimal getValue_club_card_purse() {
		return value_club_card_purse;
	}
	public void setValue_club_card_purse(BigDecimal valueClubCardPurse) {
		value_club_card_purse = valueClubCardPurse;
	}
	
	
}
