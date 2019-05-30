package bc.objects;

import java.util.HashMap;
import java.util.Map;


public class bcCallCenterInquirerLineObject extends bcObject {
	private Map<String,Comparable<String>> fieldHm = new HashMap<String, Comparable<String>>();
	
	private String idInquirerLine;
	
	public bcCallCenterInquirerLineObject(String pIdInquirerLine) {
		this.idInquirerLine = pIdInquirerLine;
		getFeature();
	}
	
	private void getFeature() {
		String featureSelect = " SELECT * FROM " + getGeneralDBScheme() + ".VC_CC_INQUIRER_LINE_ALL WHERE id_cc_inquirer_line = ?";
		fieldHm = getFeatures2(featureSelect, this.idInquirerLine, false);
	}
	
	public String getValue(String pColumnName) {
		return getFeautureResult(fieldHm, pColumnName);
	}
}
