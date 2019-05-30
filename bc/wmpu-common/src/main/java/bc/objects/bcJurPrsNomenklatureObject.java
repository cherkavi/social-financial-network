package bc.objects;

import java.util.HashMap;
import java.util.Map;

public class bcJurPrsNomenklatureObject extends bcObject {
	private Map<String,Comparable<String>> fieldHm = new HashMap<String, Comparable<String>>();
	
	private String idNomenklature;
	
	public bcJurPrsNomenklatureObject(String pIdNomenklature) {
		this.idNomenklature = pIdNomenklature;
		getFeature();
	}
	
	private void getFeature() {
		String featureSelect = " SELECT * FROM " + getGeneralDBScheme() + ".VC_JUR_PRS_NOMENKL_CLUB_ALL WHERE id_jur_prs_nomenkl = ?";
		fieldHm = getFeatures2(featureSelect, this.idNomenklature, false);
	}
	
	public String getValue(String pColumnName) {
		return getFeautureResult(fieldHm, pColumnName);
	}
}
