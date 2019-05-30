package webpos;

import java.util.HashMap;
import java.util.Map;


public class wpNatPrsObject extends wpObject {
    
	private Map<String,Comparable<String>> fieldHm = new HashMap<String, Comparable<String>>();
	
	private String idNatPrs;
	
	public wpNatPrsObject(String pIdNatPrs) {
		this.idNatPrs = pIdNatPrs;
		getFeature();
	}
	
	private void getFeature() {
		String featureSelect = " SELECT * FROM " + getGeneralDBScheme() + ".vp$nat_prs_all WHERE id_nat_prs = ?";
		fieldHm = getFeatures2(featureSelect, this.idNatPrs, false);
	}
	
	public String getValue(String pColumnName) {
		return getFeautureResult(fieldHm, pColumnName);
	}

}
