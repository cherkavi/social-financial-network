package bc.objects;

import java.util.HashMap;
import java.util.Map;


public class bcQuestionnaireFormFieldObject extends bcObject {
	private Map<String,Comparable<String>> fieldHm = new HashMap<String, Comparable<String>>();
	
    private String idFormField;
    
    public bcQuestionnaireFormFieldObject(String pIdFormField) {
    	this.idFormField = pIdFormField;
		this.getFeature();
    }
	
	public void getFeature() {
		String featureSelect = " SELECT * FROM " + getGeneralDBScheme() + ".VC_QUEST_FORM_FIELD_ALL WHERE id_quest_form_field = ?";
		fieldHm = getFeatures2(featureSelect, this.idFormField, false);
	}
	
	public String getValue(String pColumnName) {
		return getFeautureResult(fieldHm, pColumnName);
	}

}
