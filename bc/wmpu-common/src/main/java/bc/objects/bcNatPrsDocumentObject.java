package bc.objects;

import java.util.HashMap;
import java.util.Map;

public class bcNatPrsDocumentObject extends bcObject {
	private Map<String,Comparable<String>> fieldHm = new HashMap<String, Comparable<String>>();
	
	private String idNatPrsDoc;
	
	public bcNatPrsDocumentObject(String pIdNatPrsDoc) {
		this.idNatPrsDoc = pIdNatPrsDoc;
		getFeature();
	}
	
	private void getFeature() {
		String featureSelect = " SELECT * FROM " + getGeneralDBScheme() + ".VC_NAT_PRS_DOC_ALL WHERE id_nat_prs_doc = ?";
		fieldHm = getFeatures2(featureSelect, this.idNatPrsDoc, false);
	}
	
	public String getValue(String pColumnName) {
		return getFeautureResult(fieldHm, pColumnName);
	}    

}
