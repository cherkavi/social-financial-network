package bc.objects;

import java.util.HashMap;
import java.util.Map;

public class bcWarningObject extends bcObject {
	private Map<String,Comparable<String>> fieldHm = new HashMap<String, Comparable<String>>();
	
	private String idWarning;
	
	public bcWarningObject(String pIdWarning) {
		this.idWarning = pIdWarning;
		getFeature();
	}
	
	private void getFeature() {
		String featureSelect = " SELECT * FROM " + getGeneralDBScheme() + ".VC_WARNINGS_ALL WHERE id_warning = ?";
		fieldHm = getFeatures2(featureSelect, this.idWarning, false);
	}
	
	public String getValue(String pColumnName) {
		return getFeautureResult(fieldHm, pColumnName);
	}

}
