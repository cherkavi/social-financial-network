package bc.objects;

import java.util.HashMap;
import java.util.Map;

public class bcNatPrsJOfficeRequestObject extends bcObject {
	private Map<String,Comparable<String>> fieldHm = new HashMap<String, Comparable<String>>();
	
	private String idJOfficeRequest;
	
	public bcNatPrsJOfficeRequestObject(String pIdJOfficeRequest) {
		this.idJOfficeRequest = pIdJOfficeRequest;
		getFeature();
	}
	
	private void getFeature() {
		String featureSelect = " SELECT * FROM " + getGeneralDBScheme() + ".VC_NAT_PRS_OFFICE_ALL WHERE id_nat_prs_office = ?";
		fieldHm = getFeatures2(featureSelect, this.idJOfficeRequest, false);
	}
	
	public String getValue(String pColumnName) {
		return getFeautureResult(fieldHm, pColumnName);
	}
}
