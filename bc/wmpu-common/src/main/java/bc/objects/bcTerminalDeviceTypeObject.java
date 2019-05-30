package bc.objects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import bc.lists.bcListTerminal;
import bc.service.bcFeautureParam;


public class bcTerminalDeviceTypeObject extends bcObject {
	private Map<String,Comparable<String>> fieldHm = new HashMap<String, Comparable<String>>();
	
	private String idDeviceType;
	
	public bcTerminalDeviceTypeObject(String pIdDeviceType) {
		this.idDeviceType = pIdDeviceType;
		getFeature();
	}
	
	private void getFeature() {
		String featureSelect = " SELECT * FROM " + getGeneralDBScheme() + ".VC_TERM_DEVICE_TYPE_ALL WHERE id_device_type = ?";
		fieldHm = getFeatures2(featureSelect, this.idDeviceType, false);
	}
	
	public String getValue(String pColumnName) {
		return getFeautureResult(fieldHm, pColumnName);
	}

	public String getTerminalsHTML(String pFindString, String pTermType, String pTermStatus, String p_beg, String p_end) {
		bcListTerminal list = new bcListTerminal();
	      
	    String pWhereCause = " WHERE id_device_type = ? ";
	    	
	    ArrayList<bcFeautureParam> pWhereValue = new ArrayList<bcFeautureParam>();
	    pWhereValue.add(new bcFeautureParam("int", this.idDeviceType));
	      
	    return list.getTerminalsHTML(pWhereCause, pWhereValue, pFindString, pTermType, pTermStatus, "", "", "", p_beg, p_end);
	}	
}
