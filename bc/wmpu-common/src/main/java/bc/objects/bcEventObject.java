package bc.objects;

import java.util.HashMap;
import java.util.Map;


public class bcEventObject extends bcObject {
	private Map<String,Comparable<String>> fieldHm = new HashMap<String, Comparable<String>>();
	
	private String idEvent;
	
	public bcEventObject(String pIdEvent) {
		this.idEvent = pIdEvent;
		getFeature();
	}
	
	private void getFeature() {
		String featureSelect = " SELECT * FROM " + getGeneralDBScheme() + ".VC_SYS_EVENT WHERE id_event = ?";
		fieldHm = getFeatures2(featureSelect, this.idEvent, false);
	}
	
	public String getValue(String pColumnName) {
		return getFeautureResult(fieldHm, pColumnName);
	}

}
