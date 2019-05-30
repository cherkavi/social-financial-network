package bc.objects;

import java.util.HashMap;
import java.util.Map;


public class bcClubRelationshipBKOperationShemeObject extends bcObject {
	private Map<String,Comparable<String>> fieldHm = new HashMap<String, Comparable<String>>();
	
	private String idClubRelBKOperScheme;

	public bcClubRelationshipBKOperationShemeObject(String pIdClubRelBKOper) {
		this.idClubRelBKOperScheme = pIdClubRelBKOper;
		getFeature();
	}
	
	private void getFeature() {
		String featureSelect = " SELECT * FROM " + getGeneralDBScheme() + ".VC_CLUB_REL_BK_OPER_S_CLUB_ALL WHERE id_club_rel_oper_scheme = ?";
		fieldHm = getFeatures2(featureSelect, this.idClubRelBKOperScheme, false);
	}
	
	public String getValue(String pColumnName) {
		return getFeautureResult(fieldHm, pColumnName);
	}
}
