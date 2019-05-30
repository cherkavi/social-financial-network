package bc.objects;

import java.util.HashMap;
import java.util.Map;

import bc.service.bcFeautureParam;


public class bcContactPrsDataTerminalObject extends bcObject {
	private Map<String,Comparable<String>> fieldHm = new HashMap<String, Comparable<String>>();
	
	private String idTerm;
	private String idContactPrs;
	
	public bcContactPrsDataTerminalObject(String pIdTerm, String pIdContactPrs) {
		this.idTerm = pIdTerm;
		this.idContactPrs = pIdContactPrs;
		getFeature();
	}
	
	private void getFeature() {
		String featureSelect = " SELECT * FROM " + getGeneralDBScheme() + ".VC_CONTACT_PRS_DT_TERM_ALL WHERE id_contact_prs = ? AND id_term = ?";
		
		bcFeautureParam[] array = new bcFeautureParam[2];
		array[0] = new bcFeautureParam("int", this.idContactPrs);
		array[1] = new bcFeautureParam("int", this.idTerm);

		fieldHm = getFeatures2(featureSelect, array);
	}
	
	public String getValue(String pColumnName) {
		return getFeautureResult(fieldHm, pColumnName);
	}}
