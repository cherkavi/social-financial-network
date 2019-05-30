package bc.objects;

import java.util.HashMap;
import java.util.Map;

public class bcReglamentObject extends bcObject {
	private Map<String,Comparable<String>> fieldHm = new HashMap<String, Comparable<String>>();
	
	private String idJob;
	
	public bcReglamentObject(String pIdJob) {
		this.idJob = pIdJob;
		getFeature();
	}
	
	private void getFeature() {
		String featureSelect = " SELECT * FROM " + getGeneralDBScheme() + ".VC_SYS_JOBS_ALL WHERE id_job = ?";
		fieldHm = getFeatures2(featureSelect, this.idJob, false);
	}
	
	public String getValue(String pColumnName) {
		return getFeautureResult(fieldHm, pColumnName);
	}

}
