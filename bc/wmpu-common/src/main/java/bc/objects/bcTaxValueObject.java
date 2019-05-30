package bc.objects;

import java.util.HashMap;
import java.util.Map;

public class bcTaxValueObject extends bcObject {
	private Map<String,Comparable<String>> fieldHm = new HashMap<String, Comparable<String>>();
	
	private String idTaxValue;
	
	public bcTaxValueObject(String pIdTaxValue) {
		this.idTaxValue = pIdTaxValue;
		getFeature();
	}
	
	private void getFeature() {
		String featureSelect = " SELECT * FROM " + getGeneralDBScheme() + ".VC_TAX_VALUES_CLUB_ALL WHERE id_tax_value = ?";
		fieldHm = getFeatures2(featureSelect, this.idTaxValue, false);
	}
	
	public String getValue(String pColumnName) {
		return getFeautureResult(fieldHm, pColumnName);
	}}
