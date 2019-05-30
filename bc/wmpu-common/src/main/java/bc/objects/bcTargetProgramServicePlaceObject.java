package bc.objects;

import java.util.HashMap;
import java.util.Map;

public class bcTargetProgramServicePlaceObject extends bcObject {
	
	private Map<String,Comparable<String>> fieldHm = new HashMap<String, Comparable<String>>();
	
	private String idTargetProgramPlace;
	
	public bcTargetProgramServicePlaceObject(String pIdTargetProgramPlace) {
		this.idTargetProgramPlace = pIdTargetProgramPlace;
		getFeature();
	}
	
	private void getFeature() {
		String featureSelect = " SELECT * FROM " + getGeneralDBScheme() + ".vc_target_prg_place_all WHERE id_target_prg_place = ?";
		fieldHm = getFeatures2(featureSelect, this.idTargetProgramPlace, false);
	}
	
	public String getValue(String pColumnName) {
		return getFeautureResult(fieldHm, pColumnName);
	}
}
