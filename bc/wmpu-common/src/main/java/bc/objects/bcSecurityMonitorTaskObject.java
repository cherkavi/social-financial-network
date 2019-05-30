package bc.objects;

import java.util.HashMap;
import java.util.Map;


public class bcSecurityMonitorTaskObject extends bcObject {
    
	private Map<String,Comparable<String>> fieldHm = new HashMap<String, Comparable<String>>();
	
	private String idMonitorTask;
	
	public bcSecurityMonitorTaskObject(String pIdMonitorTask) {
		this.idMonitorTask = pIdMonitorTask;
		getFeature();
	}
	
	private void getFeature() {
		String featureSelect = " SELECT * FROM " + getGeneralDBScheme() + ".VC_SYS_SEC_MONITOR_TASK_ALL WHERE id_sec_monitor_task = ?";
		fieldHm = getFeatures2(featureSelect, this.idMonitorTask, false);
	}
	
	public String getValue(String pColumnName) {
		return getFeautureResult(fieldHm, pColumnName);
	}

}
