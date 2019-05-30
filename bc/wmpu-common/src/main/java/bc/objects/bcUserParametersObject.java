package bc.objects;

import java.util.HashMap;
import java.util.Map;

import bc.service.bcFeautureParam;


public class bcUserParametersObject extends bcObject {
	private Map<String,Comparable<String>> fieldHm = new HashMap<String, Comparable<String>>();
	
	public bcUserParametersObject() {}
	
	public bcUserParametersObject(boolean needFeauture) {
		if (needFeauture) {
			getCurntUserParamFeature();
		}
	}
	
	public void getCurntUserParamFeature() {
		String featureCurntUserSelect = " SELECT * FROM V_USER_PARAM_LN";
		bcFeautureParam[] array = new bcFeautureParam[1];
		array[0] = new bcFeautureParam("none", "");
		fieldHm = getFeatures2(featureCurntUserSelect, array);
	}
	
	public String getValue(String pColumnName) {
		if (fieldHm.isEmpty()) {
			getCurntUserParamFeature();
		}
		return getFeautureResult(fieldHm, pColumnName);
	}

}
