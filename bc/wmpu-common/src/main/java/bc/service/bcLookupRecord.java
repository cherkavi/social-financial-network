package bc.service;

import java.util.Comparator;

public class bcLookupRecord {
	
	private String lookup_type;
	private String lookup_code;
	private String lookup_meaning;
	private String lookup_description;
	private int lookup_number_value;
	private String existFlag;
	private String optionStyle;

	public bcLookupRecord(String pType, String pCode, String pMeaning, String pDescription, int pNumberValue) {
		this.lookup_type = pType;
		this.lookup_code = pCode;
		this.lookup_meaning = pMeaning;
		this.lookup_description = pDescription;
		this.lookup_number_value = pNumberValue;
		this.existFlag = "Y";
		this.optionStyle = "";
	}
	
	public bcLookupRecord(String pType, String pCode, String pMeaning, String pDescription, int pNumberValue, String pExistFlag) {
		this.lookup_type = pType;
		this.lookup_code = pCode;
		this.lookup_meaning = pMeaning;
		this.lookup_description = pDescription;
		this.lookup_number_value = pNumberValue;
		this.existFlag = pExistFlag;
		this.optionStyle = "";
	}

	public bcLookupRecord(String pType, String pCode, String pMeaning, String pDescription, int pNumberValue, String pExistFlag, String pOptionStyle) {
		this.lookup_type = pType;
		this.lookup_code = pCode;
		this.lookup_meaning = pMeaning;
		this.lookup_description = pDescription;
		this.lookup_number_value = pNumberValue;
		this.existFlag = pExistFlag;
		this.optionStyle = pOptionStyle;
	}

	public String getType() {
		return this.lookup_type;
	}

	public String getCode() {
		return this.lookup_code;
	}
	
	public String getMeaning() {
		return this.lookup_meaning;
	}

	public String getDescription() {
		return this.lookup_description;
	}

	public int getNumberValue() {
		return this.lookup_number_value;
	}
	
	public String getExistFlag() {
		return this.existFlag;
	}
	
	public String getOptionStyle() {
		return this.optionStyle;
	}

    public static class CompareCode implements Comparator<bcLookupRecord> {
        @Override
        public int compare(bcLookupRecord arg0, bcLookupRecord arg1) {
            return arg0.lookup_code.compareToIgnoreCase(arg1.lookup_code);
        }
    }

    public static class CompareMeaning implements Comparator<bcLookupRecord> {
        @Override
        public int compare(bcLookupRecord arg0, bcLookupRecord arg1) {
            return arg0.lookup_meaning.compareToIgnoreCase(arg1.lookup_meaning);
        }
    }

    public static class CompareDescription implements Comparator<bcLookupRecord> {
        @Override
        public int compare(bcLookupRecord arg0, bcLookupRecord arg1) {
            return arg0.lookup_description.compareToIgnoreCase(arg1.lookup_description);
        }
    }
    
    public static class CompareNumber implements Comparator<bcLookupRecord> {
        @Override
        public int compare(bcLookupRecord arg0, bcLookupRecord arg1) {
            return arg0.lookup_number_value - arg1.lookup_number_value;
        }
    }
	
}
