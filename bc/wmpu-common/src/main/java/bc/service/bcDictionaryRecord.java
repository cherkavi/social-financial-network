package bc.service;

import java.util.Comparator;

public class bcDictionaryRecord {
	
	private String code;
	private String value;
	private String shortValue;
	private String existFlag;
	private String optionStyle;

	public bcDictionaryRecord(String pCode, String pValue) {
		this.code = pCode;
		this.value = pValue;
		this.shortValue = pValue;
		this.existFlag = "Y";
		this.optionStyle = "";
	}
	
	public bcDictionaryRecord(String pCode, String pValue, String pExistFlag) {
		this.code = pCode;
		this.value = pValue;
		this.shortValue = pValue;
		this.existFlag = pExistFlag;
		this.optionStyle = "";
	}

	public bcDictionaryRecord(String pCode, String pValue, String pExistFlag, String pOptionStyle) {
		this.code = pCode;
		this.value = pValue;
		this.shortValue = pValue;
		this.existFlag = pExistFlag;
		this.optionStyle = pOptionStyle;
	}

	public bcDictionaryRecord(String pCode, String pValue, String pShortValue, String pExistFlag, String pOptionStyle) {
		this.code = pCode;
		this.value = pValue;
		this.shortValue = pShortValue;
		this.existFlag = pExistFlag;
		this.optionStyle = pOptionStyle;
	}

	public String getCode() {
		return this.code;
	}
	
	public String getValue() {
		return this.value;
	}
	
	public String getShortValue() {
		return this.shortValue;
	}
	
	public String getExistFlag() {
		return this.existFlag;
	}
	
	public String getOptionStyle() {
		return this.optionStyle;
	}

    public static class CompareCode implements Comparator<bcDictionaryRecord> {
        @Override
        public int compare(bcDictionaryRecord arg0, bcDictionaryRecord arg1) {
            return arg0.code.compareToIgnoreCase(arg1.code);
        }
    }

    public static class CompareValue implements Comparator<bcDictionaryRecord> {
        @Override
        public int compare(bcDictionaryRecord arg0, bcDictionaryRecord arg1) {
            return arg0.value.compareToIgnoreCase(arg1.value);
        }
    }
	
}
