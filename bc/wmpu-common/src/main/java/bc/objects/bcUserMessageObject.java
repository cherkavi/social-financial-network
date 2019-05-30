package bc.objects;

import java.util.HashMap;
import java.util.Map;

public class bcUserMessageObject extends bcObject {
	private Map<String,Comparable<String>> fieldHm = new HashMap<String, Comparable<String>>();
	
	private String idUserMessage;
	
	public bcUserMessageObject(String pIdUserMessage) {
		this.idUserMessage = pIdUserMessage;
		getFeature();
	}
	
	public bcUserMessageObject() { }
	
	private void getFeature() {
		String featureSelect = " SELECT * FROM " + getGeneralDBScheme() + ".vc_user_message_all WHERE id_user_message = ?";
		fieldHm = getFeatures2(featureSelect, this.idUserMessage, false);
	}
	
	public String getValue(String pColumnName) {
		return getFeautureResult(fieldHm, pColumnName);
	}

}
