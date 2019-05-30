package bc.objects;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;


public class bcClubShortObject extends bcObject {
	private final static Logger LOGGER=Logger.getLogger(bcClubShortObject.class);
	private Map<String,Comparable<String>> fieldHm = new HashMap<String, Comparable<String>>();
	
	private String idClub;
	
	public bcClubShortObject(String pIdClub) {
		this.idClub = pIdClub;
		getFeature();
	}
	
	private void getFeature() {
		if (!isEmpty(this.idClub)) {
			String featureSelect = " SELECT * FROM " + getGeneralDBScheme() + ".VC_CLUB_NAMES WHERE id_club = ?";
			fieldHm = getFeatures2(featureSelect, this.idClub, false);
		} else {
			LOGGER.error("idClub IS EMPTY");
		}
	}
	
	public String getValue(String pColumnName) {
		if (isEmpty(this.idClub)) {
			LOGGER.error("idClub IS EMPTY, pColumnName set EMPTY");
			return "";
		} else {
			return getFeautureResult(fieldHm, pColumnName);
		}
	}

}
