package bc.objects;

import java.util.HashMap;
import java.util.Map;

public class bcDocumentFileObject extends bcObject {
	private Map<String,Comparable<String>> fieldHm = new HashMap<String, Comparable<String>>();
	
	private String idDocFile;
	
	public bcDocumentFileObject(String pIdDocFile) {
		this.idDocFile = pIdDocFile;
		getFeature();
	}
	
	private void getFeature() {
		String featureSelect = " SELECT * FROM " + getGeneralDBScheme() + ".vc_doc_file_all WHERE id_doc_file= ?";
		fieldHm = getFeatures2(featureSelect, this.idDocFile, false);
	}
	
	public String getValue(String pColumnName) {
		return getFeautureResult(fieldHm, pColumnName);
	}
}
