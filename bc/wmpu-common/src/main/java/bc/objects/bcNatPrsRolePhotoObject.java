package bc.objects;

import java.util.HashMap;
import java.util.Map;


public class bcNatPrsRolePhotoObject extends bcObject {
	private Map<String,Comparable<String>> fieldHm = new HashMap<String, Comparable<String>>();
	
	private String idNatPrs;
	
	public bcNatPrsRolePhotoObject(String pIdNatPrs) {
		this.idNatPrs = pIdNatPrs;
		getFeature();
	}
	
	private void getFeature() {
		String featureSelect = " SELECT * FROM " + getGeneralDBScheme() + ".v_nat_prs_role_photo_all WHERE id_nat_prs = ?";
		fieldHm = getFeatures2(featureSelect, this.idNatPrs, false);
	}
	
	public String getValue(String pColumnName) {
		return getFeautureResult(fieldHm, pColumnName);
	}

}
