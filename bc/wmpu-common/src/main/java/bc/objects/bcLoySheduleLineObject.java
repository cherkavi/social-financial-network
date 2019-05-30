package bc.objects;

import java.util.HashMap;
import java.util.Map;

import bc.service.bcFeautureParam;


public class bcLoySheduleLineObject extends bcObject {
	private Map<String,Comparable<String>> fieldHm = new HashMap<String, Comparable<String>>();
	
	private String idSheduleLine;
	
	public bcLoySheduleLineObject(String pIdSheduleLine) {
		this.idSheduleLine = pIdSheduleLine;
		getFeature();
	}
	
	private void getFeature() {
		String featureSelect = " SELECT * FROM " + getGeneralDBScheme() + ".VC_LS_SHEDULE_ALL WHERE id_shedule_line = ?";

		bcFeautureParam[] array = new bcFeautureParam[1];
		array[0] = new bcFeautureParam("int", this.idSheduleLine);

		fieldHm = getFeatures2(featureSelect, array);
	}
	
	public String getValue(String pColumnName) {
		return getFeautureResult(fieldHm, pColumnName);
	}

}
