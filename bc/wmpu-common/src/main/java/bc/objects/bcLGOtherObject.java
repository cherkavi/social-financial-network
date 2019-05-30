package bc.objects;

import java.util.HashMap;
import java.util.Map;


public class bcLGOtherObject extends bcObject {
    
	private Map<String,Comparable<String>> fieldHm = new HashMap<String, Comparable<String>>();
	
	private String idLine;
	
	public bcLGOtherObject(String pIdLine) {
		this.idLine = pIdLine;
		getFeature();
	}
	
	private void getFeature() {
		String mySQL = " SELECT * FROM " + getGeneralDBScheme() + ".VC_LG_OTHERS_ALL WHERE id_lg_other = ?";
		fieldHm = getFeatures2(mySQL, this.idLine, false);
	}
	
	public String getValue(String pColumnName) {
		return getFeautureResult(fieldHm, pColumnName);
	}
}
