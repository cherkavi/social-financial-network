package bonclub.office_private.web_service.common_objects;

import java.io.Serializable;
import java.math.BigDecimal;

public class Purse  implements Serializable{
	private static final long serialVersionUID = -4640877578871695788L;
	private BigDecimal id_club_card_purse;
	private BigDecimal number_club_card_purse;
	private String cd_club_card_purse_type;
	private String name_club_card_purse_type;
	private BigDecimal cd_currency;
	private String name_currency;
	private String sname_currency;
	private BigDecimal value_club_card_purse;
	
	
	public BigDecimal getId_club_card_purse() {
		return id_club_card_purse;
	}
	public void setId_club_card_purse(BigDecimal idClubCardPurse) {
		id_club_card_purse = idClubCardPurse;
	}
	public BigDecimal getNumber_club_card_purse() {
		return number_club_card_purse;
	}
	public void setNumber_club_card_purse(BigDecimal numberClubCardPurse) {
		number_club_card_purse = numberClubCardPurse;
	}
	public String getCd_club_card_purse_type() {
		return cd_club_card_purse_type;
	}
	public void setCd_club_card_purse_type(String cdClubCardPurseType) {
		cd_club_card_purse_type = cdClubCardPurseType;
	}
	public String getName_club_card_purse_type() {
		return name_club_card_purse_type;
	}
	public void setName_club_card_purse_type(String nameClubCardPurseType) {
		name_club_card_purse_type = nameClubCardPurseType;
	}
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
