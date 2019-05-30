package bc.objects;

import java.util.HashMap;
import java.util.Map;

public class bcLoyLineObject extends bcObject {
	private Map<String,Comparable<String>> fieldHm = new HashMap<String, Comparable<String>>();
	
	private String idLine;
	
	public bcLoyLineObject(String pIdLoyalityLine) {
		this.idLine = pIdLoyalityLine;
		getFeature();
	}
	
	private void getFeature() {
		String featureSelect = " SELECT * FROM " + getGeneralDBScheme() + ".VC_LOYALITY_SCHEME_LINES_ALL WHERE id_line = ?";
		fieldHm = getFeatures2(featureSelect, this.idLine, false);
	}
	
	public String getValue(String pColumnName) {
		return getFeautureResult(fieldHm, pColumnName);
	}

}
