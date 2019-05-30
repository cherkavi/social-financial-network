package bc.objects;

import java.util.HashMap;
import java.util.Map;

import bc.service.bcFeautureParam;

public class bcClubActionWinnerObject extends bcObject {
    
	private Map<String,Comparable<String>> fieldHm = new HashMap<String, Comparable<String>>();
	
	private String idAction;
	private String idGift;
	private String idWinner;
	
	public bcClubActionWinnerObject(String pIdClubAction, String pIdWinner, String pIdGift) {
		this.idAction = pIdClubAction;
		this.idWinner = pIdWinner;
		this.idGift = pIdGift;
		getFeature();
	}
	
	private void getFeature() {
		String mySQL = 
			" SELECT * FROM " + getGeneralDBScheme() + ".VC_NAT_PRS_GIFTS_CLUB_ALL WHERE id_club_event = ? AND id_nat_prs = ? AND id_gift = ?";
		
		bcFeautureParam[] array = new bcFeautureParam[2];
		array[0] = new bcFeautureParam("int", this.idAction);
		array[1] = new bcFeautureParam("int", this.idWinner);
		array[1] = new bcFeautureParam("int", this.idGift);

		fieldHm = getFeatures2(mySQL, array);
	}
	
	public String getValue(String pColumnName) {
		return getFeautureResult(fieldHm, pColumnName);
	}
}
