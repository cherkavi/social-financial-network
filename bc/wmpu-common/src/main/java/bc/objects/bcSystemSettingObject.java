package bc.objects;

import java.util.HashMap;
import java.util.Map;

public class bcSystemSettingObject extends bcObject {
	private Map<String,Comparable<String>> fieldHm = new HashMap<String, Comparable<String>>();
	
	private String cdParam;
	
	public bcSystemSettingObject(String pCdParam) {
		this.cdParam = pCdParam;
		getFeature();
	}
	
	private void getFeature() {
		String featureSelect = " SELECT * FROM " + getGeneralDBScheme() + ".VC_SYSTEM_PARAM WHERE UPPER(cd_param) = ?";
		fieldHm = getFeatures2(featureSelect, this.cdParam, true);
	}
	
	public String getValue(String pColumnName) {
		return getFeautureResult(fieldHm, pColumnName);
	}

}
