package bc.objects;

import java.util.HashMap;
import java.util.Map;


public class bcComissionObject extends bcObject {
	private Map<String,Comparable<String>> fieldHm = new HashMap<String, Comparable<String>>();
	
	private String id_comission;
	
	public bcComissionObject(String pIdComission) {
		this.id_comission = pIdComission;
		this.getFeature();
	}
	
	public void getFeature() {
		String mySQL = " SELECT * FROM " + getGeneralDBScheme() + ".VC_JUR_PRS_COMISSION_PRIV_ALL WHERE id_comission = ?";
		fieldHm = getFeatures2(mySQL, this.id_comission, false);
	}
	
	public String getValue(String pColumnName) {
		return getFeautureResult(fieldHm, pColumnName);
	}

}
