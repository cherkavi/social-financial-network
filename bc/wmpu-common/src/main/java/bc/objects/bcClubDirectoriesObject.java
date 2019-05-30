package bc.objects;

import java.util.HashMap;
import java.util.Map;

public class bcClubDirectoriesObject extends bcObject {
	
	private Map<String,Comparable<String>> fieldHm = new HashMap<String, Comparable<String>>();
	
	private String idClub;
	
	public bcClubDirectoriesObject(String pIdClub) {
		this.idClub = pIdClub;
		getFeature();
	}
	
	private void getFeature() {
		String featureSelect = " SELECT * FROM " + getGeneralDBScheme() + ".v_club_dir_all WHERE id_club = ?";
		fieldHm = getFeatures2(featureSelect, this.idClub, false);
	}
	
	public String getValue(String pColumnName) {
		return getFeautureResult(fieldHm, pColumnName);
	}
	
}
