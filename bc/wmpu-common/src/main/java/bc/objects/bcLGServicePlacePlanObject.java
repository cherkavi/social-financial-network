package bc.objects;

import java.util.HashMap;
import java.util.Map;

public class bcLGServicePlacePlanObject extends bcObject {
    
	private Map<String,Comparable<String>> fieldHm = new HashMap<String, Comparable<String>>();
	
	private String idServicePlacePlan;
	
	public bcLGServicePlacePlanObject(String pIdServicePlacePlan) {
		this.idServicePlacePlan = pIdServicePlacePlan;
		getFeature();
	}
	
	private void getFeature() {
		String mySQL = " SELECT * FROM " + getGeneralDBScheme() + ".VC_LG_SERVICE_PLACE_PLAN_ALL WHERE id_lg_service_place_plan = ?";
		fieldHm = getFeatures2(mySQL, this.idServicePlacePlan, false);
	}
	
	public String getValue(String pColumnName) {
		return getFeautureResult(fieldHm, pColumnName);
	}
}
