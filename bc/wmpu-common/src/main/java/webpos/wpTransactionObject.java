package webpos;

import java.util.HashMap;
import java.util.Map;

public class wpTransactionObject extends wpObject {
    
	private Map<String,Comparable<String>> fieldHm = new HashMap<String, Comparable<String>>();
	
	private String idTrans;
	
	public wpTransactionObject(String pIdTrans) {
		this.idTrans = pIdTrans;
		getFeature();
	}
	
	private void getFeature() {
		String featureSelect = " SELECT * FROM " + getGeneralDBScheme() + ".vp$trans_all WHERE id_trans = ?";
		fieldHm = getFeatures2(featureSelect, this.idTrans, false);
	}
	
	public String getValue(String pColumnName) {
		return getFeautureResult(fieldHm, pColumnName);
	}

}
