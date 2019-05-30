package bc.objects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import bc.lists.bcListCardOperation;
import bc.service.bcFeautureParam;


public class bcNatPrsGiftObject extends bcObject {
	//private final static Logger LOGGER=Logger.getLogger(bcNatPrsGiftObject.class);
	private Map<String,Comparable<String>> fieldHm = new HashMap<String, Comparable<String>>();
	
	private String idNatPrsGift;
	
	public bcNatPrsGiftObject(String pIdNatPrsGift) {
		this.idNatPrsGift = pIdNatPrsGift;
		getFeature();
	}
	
	private void getFeature() {
		String featureSelect = " SELECT * FROM " + getGeneralDBScheme() + ".VC_NAT_PRS_GIFTS_CLUB_ALL WHERE id_nat_prs_gift = ?";
		fieldHm = getFeatures2(featureSelect, this.idNatPrsGift, false);
	}
	
	public String getValue(String pColumnName) {
		return getFeautureResult(fieldHm, pColumnName);
	}    
    
    public String getClubCardsTasksHTML(String pFindString, String pOperType, String pOperState, String p_beg, String p_end) {
    	bcListCardOperation list = new bcListCardOperation();
    	
    	String pWhereCause = " WHERE id_nat_prs_gift = ? ";
    	
    	ArrayList<bcFeautureParam> pWhereValue = new ArrayList<bcFeautureParam>();
    	pWhereValue.add(new bcFeautureParam("int", this.idNatPrsGift));

    	String lDeleteLink = "";
    	String lEditLink = "";
    	if (isEditPermited("CLUB_EVENT_DELIVERY_CARD_TASKS") >0) {
 	 		lDeleteLink = "../crm/club_event/deliveryupdate.jsp?id="+this.idNatPrsGift+"&type=tasks";
 	 		lEditLink = "../crm/club_event/deliveryupdate.jsp?id="+this.idNatPrsGift+"&type=tasks";
 	 	}
    	
    	return list.getCardOperationsHTML(pWhereCause, pWhereValue, pFindString, pOperType, pOperState, lEditLink, lDeleteLink, "div_data_detail", p_beg, p_end);
    }

}
