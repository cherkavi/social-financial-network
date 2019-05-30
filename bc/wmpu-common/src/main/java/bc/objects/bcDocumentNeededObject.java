package bc.objects;

import java.util.HashMap;
import java.util.Map;

public class bcDocumentNeededObject extends bcObject {
	private Map<String,Comparable<String>> fieldHm = new HashMap<String, Comparable<String>>();
	
	private String idDoc;
	
	public bcDocumentNeededObject(String pIdDoc) {
		this.idDoc = pIdDoc;
		getFeature();
	}
	
	private void getFeature() {
		String featureSelect = " SELECT * FROM " + getGeneralDBScheme() + ".VC_DOC_CLUB_REL_NEED_CLUB_ALL WHERE id_doc = ?";
		fieldHm = getFeatures2(featureSelect, this.idDoc, false);
	}
	
	public String getValue(String pColumnName) {
		return getFeautureResult(fieldHm, pColumnName);
	}
}
