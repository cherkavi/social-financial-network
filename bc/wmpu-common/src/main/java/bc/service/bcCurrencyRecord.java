package bc.service;

public class bcCurrencyRecord {
	
	private String cd_currency;
	private String scd_currency;
	private String name_currency;
	private String sname_currency;
	private String exist_flag;
	private String optionStyle;

	public bcCurrencyRecord(String pCode, String pStringCode, String pName, String pShortName, String pExist) {
		this.cd_currency = pCode;
		this.scd_currency = pStringCode;
		this.name_currency = pName;
		this.sname_currency = pShortName;
		this.exist_flag = pExist;
		this.optionStyle = "";
	}

	public String getCode() {
		return this.cd_currency;
	}
	
	public String getStringCode() {
		return this.scd_currency;
	}
	
	public String getName() {
		return this.name_currency;
	}
	
	public String getShortName() {
		return this.sname_currency;
	}
	
	public String getExistFlag() {
		return this.exist_flag;
	}
	
	public String getOptionStyle() {
		return this.optionStyle;
	}
	
}
