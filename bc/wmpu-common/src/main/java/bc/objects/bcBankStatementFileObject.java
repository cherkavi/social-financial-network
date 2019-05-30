package bc.objects;

import java.util.HashMap;
import java.util.Map;


public class bcBankStatementFileObject extends bcObject {
	private Map<String,Comparable<String>> fieldHm = new HashMap<String, Comparable<String>>();
	
	private String idBankStatementLine;
	
	public bcBankStatementFileObject(String pIdBankStatementLine) {
		this.idBankStatementLine = pIdBankStatementLine;
		getFeature();
	}
	
	private void getFeature() {
		String featureSelect = " SELECT * FROM " + getGeneralDBScheme() + ".VC_BS_FILES_CLUB_ALL WHERE id_bank_statement_file = ?";
		fieldHm = getFeatures2(featureSelect, this.idBankStatementLine, false);
	}
	
	public String getValue(String pColumnName) {
		return getFeautureResult(fieldHm, pColumnName);
	}

}
