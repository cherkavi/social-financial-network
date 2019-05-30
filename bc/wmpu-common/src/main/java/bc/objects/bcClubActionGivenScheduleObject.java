package bc.objects;

import java.util.HashMap;
import java.util.Map;



public class bcClubActionGivenScheduleObject extends bcObject {
    private Map<String,Comparable<String>> fieldHm = new HashMap<String, Comparable<String>>();
	
	private String idSchedule;
	
	public bcClubActionGivenScheduleObject(String pIdSchedule) {
		this.idSchedule = pIdSchedule;
		getFeature();
	}
	
	private void getFeature() {
		String featureSelect = " SELECT * FROM " + getGeneralDBScheme() + ".VC_CLUB_EVENT_GIVEN_SH_CL_ALL WHERE id_club_event_given_schedule = ?";
		fieldHm = getFeatures2(featureSelect, this.idSchedule, false);
	}
	
	public String getValue(String pColumnName) {
		return getFeautureResult(fieldHm, pColumnName);
	}
}
