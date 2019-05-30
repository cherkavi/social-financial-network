package webpos;

import java.util.HashMap;
import java.util.Map;

public class wpTelegramObject extends wpObject {
    
	private Map<String,Comparable<String>> fieldHm = new HashMap<String, Comparable<String>>();
	
	private String idTelgr;
	
	public wpTelegramObject(String pIdTelgr) {
		this.idTelgr = pIdTelgr;
		getFeature();
	}
	
	private void getFeature() {
		String featureSelect = " SELECT * FROM " + getGeneralDBScheme() + ".vp$telgr_all WHERE id_telgr = ?";
		fieldHm = getFeatures2(featureSelect, this.idTelgr, false);
	}
	
	public String getValue(String pColumnName) {
		return getFeautureResult(fieldHm, pColumnName);
	}

}
