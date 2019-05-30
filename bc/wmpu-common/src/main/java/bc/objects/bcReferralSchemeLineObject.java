package bc.objects;

import java.util.HashMap;
import java.util.Map;

import bc.service.bcFeautureParam;

public class bcReferralSchemeLineObject extends bcObject {
	
	private String idReferralSchemeLine;
	
	private Map<String,Comparable<String>> fieldHm = new HashMap<String, Comparable<String>>();
	
	public bcReferralSchemeLineObject(String pIdReferralSchemeLine){
		this.idReferralSchemeLine = pIdReferralSchemeLine;
		getFeature();
	}
	
	private void getFeature() {
		String featureSelect = " SELECT * FROM " + getGeneralDBScheme() + ".vc_referral_scheme_line_all WHERE id_referral_scheme_line = ?";
				
		bcFeautureParam[] array = new bcFeautureParam[1];
		array[0] = new bcFeautureParam("int", this.idReferralSchemeLine);
		
		fieldHm = getFeatures2(featureSelect, array);
	}
	
	public String getValue(String pColumnName) {
		return getFeautureResult(fieldHm, pColumnName);
	}
}
