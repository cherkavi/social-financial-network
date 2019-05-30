package bc.objects;

import java.util.HashMap;
import java.util.Map;


public class bcClubActionGiftObject extends bcObject {
    
	private Map<String,Comparable<String>> fieldHm = new HashMap<String, Comparable<String>>();
	
	private String idClubActionGift;
	
	public bcClubActionGiftObject(String pIdClubActionGift) {
		this.idClubActionGift = pIdClubActionGift;
		getFeature();
	}
	
	private void getFeature() {
		String mySQL = 
			" SELECT * FROM " + getGeneralDBScheme() + ".VC_CLUB_EVENT_GIFTS_ALL WHERE id_club_event_gift = ?";
		fieldHm = getFeatures2(mySQL, this.idClubActionGift, false);
	}
	
	public String getValue(String pColumnName) {
		return getFeautureResult(fieldHm, pColumnName);
	}
}
