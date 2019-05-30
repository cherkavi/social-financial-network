package bc.objects;

import java.util.HashMap;
import java.util.Map;

public class bcDocumentBankAccountObject extends bcObject {
	private Map<String,Comparable<String>> fieldHm = new HashMap<String, Comparable<String>>();
	
	private String idDocBankAccount;
	
	public bcDocumentBankAccountObject(String pIdDocBankAccount) {
		this.idDocBankAccount = pIdDocBankAccount;
		getFeature();
	}
	
	private void getFeature() {
		String featureSelect = " SELECT * FROM " + getGeneralDBScheme() + ".vc_doc_bank_account_all WHERE id_doc_bank_account= ?";
		fieldHm = getFeatures2(featureSelect, this.idDocBankAccount, false);
	}
	
	public String getValue(String pColumnName) {
		return getFeautureResult(fieldHm, pColumnName);
	}
}
