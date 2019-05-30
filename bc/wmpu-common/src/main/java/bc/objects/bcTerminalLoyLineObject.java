package bc.objects;

import java.util.HashMap;
import java.util.Map;

import bc.service.bcFeautureParam;


public class bcTerminalLoyLineObject extends bcObject {
	private Map<String,Comparable<String>> fieldHm = new HashMap<String, Comparable<String>>();
	
	private String idTerminal;
	private String idLine;
	
	public bcTerminalLoyLineObject(String pIdTerm, String pIdLoyalityLine) {
		this.idLine = pIdLoyalityLine;
		this.idTerminal = pIdTerm;
		getFeature();
	}
	
	private void getFeature() {
		String mySQL = " SELECT * FROM " + getGeneralDBScheme() + ".VC_TERM_LOYALITY_LINES_ALL WHERE id_term = ? AND id_line = ?";
		
		bcFeautureParam[] array = new bcFeautureParam[2];
		array[0] = new bcFeautureParam("int", this.idTerminal);
		array[1] = new bcFeautureParam("int", this.idLine);

		fieldHm = getFeatures2(mySQL, array);
	}
	
	public String getValue(String pColumnName) {
		return getFeautureResult(fieldHm, pColumnName);
	}

}
