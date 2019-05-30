package bc.objects;

import java.util.HashMap;
import java.util.Map;

import bc.service.bcFeautureParam;


public class bcTerminalLoyLineHistoryObject extends bcObject {
	private Map<String,Comparable<String>> fieldHm = new HashMap<String, Comparable<String>>();
	
	private String idLoyalityHistoryLine;
	
	public bcTerminalLoyLineHistoryObject(String pIdLoyalityHistoryLine) {
		this.idLoyalityHistoryLine = pIdLoyalityHistoryLine;
		String mySQL = " SELECT * FROM " + getGeneralDBScheme() + ".VC_TERM_LOYALITY_LINES_H_ALL WHERE id_loyality_history_line = ?";
		fieldHm = getFeatures2(mySQL, this.idLoyalityHistoryLine, false);
	}
	
	public bcTerminalLoyLineHistoryObject(String pIdLoyalityHistory, String pIdCardStatus, String pIdCardCategory) {
		bcFeautureParam[] array = new bcFeautureParam[3];
		array[0] = new bcFeautureParam("int", pIdLoyalityHistory);
		array[1] = new bcFeautureParam("int", pIdCardStatus);
		array[2] = new bcFeautureParam("int", pIdCardCategory);
		
		String mySQL = 
			" SELECT * " +
			"   FROM " + getGeneralDBScheme() + ".VC_TERM_LOYALITY_LINES_H_ALL " +
			"  WHERE id_loyality_history = ? " +
			"    AND id_card_status = ? " +
			"    AND id_category = ? ";
		fieldHm = getFeatures2(mySQL, array);
	}
	
	public String getValue(String pColumnName) {
		return getFeautureResult(fieldHm, pColumnName);
	}

}
