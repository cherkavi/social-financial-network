package bc.objects;

import java.util.HashMap;
import java.util.Map;

public class bcTerminalSAMObject extends bcObject {
	private Map<String,Comparable<String>> fieldHm = new HashMap<String, Comparable<String>>();
	
	private String idTermSAM;
	
	public bcTerminalSAMObject(String pIdTermSAM) {
		this.idTermSAM = pIdTermSAM;
		getFeature();
	}
	
	private void getFeature() {
		String mySQL = "SELECT * FROM " + getGeneralDBScheme() + ".vc_term_sam_all WHERE id_term_sam = ?";
        fieldHm = getFeatures2(mySQL, this.idTermSAM, false);
	}
	
	public String getValue(String pColumnName) {
		return getFeautureResult(fieldHm, pColumnName);
	}

}
