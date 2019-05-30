package bc.objects;

import java.util.HashMap;
import java.util.Map;


public class bcCallCenterActivityObject extends bcObject {
	private Map<String,Comparable<String>> fieldHm = new HashMap<String, Comparable<String>>();
	
	private String idActivity;
	
	public bcCallCenterActivityObject(String pIdActivity) {
		this.idActivity = pIdActivity;
		getFeature();
	}
	
	private void getFeature() {
		String featureSelect = " SELECT * FROM " + getGeneralDBScheme() + ".VC_CC_QUESTION_ACTIVITIES_ALL WHERE id_cc_activity = ?";
		fieldHm = getFeatures2(featureSelect, this.idActivity, false);
	}
	
	public String getValue(String pColumnName) {
		return getFeautureResult(fieldHm, pColumnName);
	}
}
