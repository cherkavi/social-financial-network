package bc.objects;

import java.util.HashMap;
import java.util.Map;



public class bcTerminalUserObject extends bcObject {
	private Map<String,Comparable<String>> fieldHm = new HashMap<String, Comparable<String>>();
	
	private String idTermUser;
	
	public bcTerminalUserObject(String pIdTermUser) {
		this.idTermUser = pIdTermUser;
		getFeature();
	}
	
	public void getFeature() {
		String featureSelect = " SELECT * FROM " + getGeneralDBScheme() + ".VC_TERM_USER_ALL WHERE id_term_user = ?";
		fieldHm = getFeatures2(featureSelect, this.idTermUser, false);
	}
	
	public String getValue(String pColumnName) {
		return getFeautureResult(fieldHm, pColumnName);
	}
	
}
