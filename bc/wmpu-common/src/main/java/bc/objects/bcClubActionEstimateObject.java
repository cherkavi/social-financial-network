package bc.objects;

import java.util.HashMap;
import java.util.Map;



public class bcClubActionEstimateObject extends bcObject {
    private Map<String,Comparable<String>> fieldHm = new HashMap<String, Comparable<String>>();
	
	private String idEstimate;
	
	public bcClubActionEstimateObject(String pIdEstimateCriterion) {
		this.idEstimate = pIdEstimateCriterion;
		getFeature();
	}
	
	private void getFeature() {
		String mySQL = 
			" SELECT * FROM " + getGeneralDBScheme() + ".VC_CLUB_EVENT_ESTIMATE_ALL WHERE id_club_event_estimate = ?";
		fieldHm = getFeatures2(mySQL, this.idEstimate, false);
	}
	
	public String getValue(String pColumnName) {
		return getFeautureResult(fieldHm, pColumnName);
	}
}
