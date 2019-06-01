package bonclub.office_private.web_service.common_objects;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class PurseHistory implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private BigDecimal id_oper; 
	private String source_oper; 
	private String type_oper; 
	private String type_oper_tsl;
    private String basis_for_oper; 
    private String state_oper; 
    private String state_oper_tsl;
    private BigDecimal cd_currency; 
    private String name_currency;
    private String sname_currency; 
    private Date oper_date; 
    private BigDecimal oper_amount;
	public BigDecimal getId_oper() {
		return id_oper;
	}
	public void setId_oper(BigDecimal idOper) {
		id_oper = idOper;
	}
	public String getSource_oper() {
		return source_oper;
	}
	public void setSource_oper(String sourceOper) {
		source_oper = sourceOper;
	}
	public String getType_oper() {
		return type_oper;
	}
	public void setType_oper(String typeOper) {
		type_oper = typeOper;
	}
	public String getType_oper_tsl() {
		return type_oper_tsl;
	}
	public void setType_oper_tsl(String typeOperTsl) {
		type_oper_tsl = typeOperTsl;
	}
	public String getBasis_for_oper() {
		return basis_for_oper;
	}
	public void setBasis_for_oper(String basisForOper) {
		basis_for_oper = basisForOper;
	}
	public String getState_oper() {
		return state_oper;
	}
	public void setState_oper(String stateOper) {
		state_oper = stateOper;
	}
	public String getState_oper_tsl() {
		return state_oper_tsl;
	}
	public void setState_oper_tsl(String stateOperTsl) {
		state_oper_tsl = stateOperTsl;
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
	public Date getOper_date() {
		return oper_date;
	}
	public void setOper_date(Date operDate) {
		oper_date = operDate;
	}
	public BigDecimal getOper_amount() {
		return oper_amount;
	}
	public void setOper_amount(BigDecimal operAmount) {
		oper_amount = operAmount;
	}


}
