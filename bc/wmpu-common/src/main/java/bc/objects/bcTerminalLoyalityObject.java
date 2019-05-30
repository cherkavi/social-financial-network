package bc.objects;

import java.util.HashMap;
import java.util.Map;

import bc.service.bcFeautureParam;


public class bcTerminalLoyalityObject extends bcObject {
	private Map<String,Comparable<String>> fieldHm = new HashMap<String, Comparable<String>>();
	
	private String idTerm;
	private String idLoyalityHistory;
	//private String idServicePlace;
	
	public bcTerminalLoyalityObject(String pIdTerm, /*String pIdServicePlace,*/ String pIdLoyalityHistory) {
		this.idTerm = pIdTerm;
		//this.idServicePlace = pIdServicePlace;
		this.idLoyalityHistory = pIdLoyalityHistory;
		getFeature();
	}
	
	public void getFeature() {
		if (isEmpty(this.idLoyalityHistory)) {
			fieldHm.clear();
			return;
		}
		
		String featureSelect = " SELECT * FROM " + getGeneralDBScheme() + ".VC_TERM_LOYALITY_H_CLUB_ALL WHERE id_term = ? AND id_loyality_history = ?";

		bcFeautureParam[] array = new bcFeautureParam[2];
		array[0] = new bcFeautureParam("int", this.idTerm);
		array[1] = new bcFeautureParam("int", this.idLoyalityHistory);
		fieldHm = getFeatures2(featureSelect, array);
	}
	
	public bcTerminalLoyalityObject(String pIdLoyalityHistory) {
		this.idLoyalityHistory = pIdLoyalityHistory;
		getFeatureForHistoryId();
	}
	
	public void getFeatureForHistoryId() {
		String featureSelect = " SELECT * FROM " + getGeneralDBScheme() + ".VC_TERM_LOYALITY_H_CLUB_ALL WHERE id_loyality_history = ?";
		
		fieldHm = getFeatures2(featureSelect, this.idLoyalityHistory, false);
	}
	
	public String getValue(String pColumnName) {
		return getFeautureResult(fieldHm, pColumnName);
	}
	
}
