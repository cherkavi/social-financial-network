package bc.objects;

import java.util.HashMap;
import java.util.Map;

import bc.service.bcFeautureParam;


public class bcTerminalParamObject extends bcObject {
	private Map<String,Comparable<String>> fieldHm = new HashMap<String, Comparable<String>>();
	
	private String idTerm;
	private String idParamHistory;
	
	public bcTerminalParamObject(String pIdTerm, String pIdParamHistory) {
		this.idTerm = pIdTerm;
		this.idParamHistory = pIdParamHistory;
		getFeature();
	}
	
	public void getFeature() {
		if (isEmpty(this.idParamHistory)) {
			fieldHm.clear();
			return;
		}
		
		if (!isEmpty(this.idTerm)) {
			String featureSelect = " SELECT * FROM " + getGeneralDBScheme() + ".VC_TERM_PARAM_H_ALL WHERE id_term = ? AND id_param_history = ?";
			
			bcFeautureParam[] array = new bcFeautureParam[2];
			array[0] = new bcFeautureParam("int", this.idTerm);
			array[1] = new bcFeautureParam("int", this.idParamHistory);
			fieldHm = getFeatures2(featureSelect, array);
		} else {
			String featureSelect = " SELECT * FROM " + getGeneralDBScheme() + ".VC_TERM_PARAM_H_ALL WHERE id_term = ?";
			
			bcFeautureParam[] array = new bcFeautureParam[1];
			array[0] = new bcFeautureParam("int", this.idTerm);
			fieldHm = getFeatures2(featureSelect, array);
			
		}
	}
	
	public String getValue(String pColumnName) {
		return getFeautureResult(fieldHm, pColumnName);
	}
	
}
