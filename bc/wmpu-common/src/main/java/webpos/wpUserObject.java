package webpos;

import java.util.HashMap;
import java.util.Map;

public class wpUserObject extends wpObject {
	private Map<String,Comparable<String>> fieldHm = new HashMap<String, Comparable<String>>();
	
	private String idUser;
	
	public wpUserObject(String pIdUser) {
		this.idUser = pIdUser;
		getFeature();
	}

	private void getFeature() {
		String featureSelect = " SELECT * FROM " + getGeneralDBScheme() + ".vp$user_all WHERE id_term_user = ?";
		fieldHm = getFeatures2(featureSelect, this.idUser, false);
	}
	
	public String getValue(String pColumnName) {
		return getFeautureResult(fieldHm, pColumnName);
	}
}
