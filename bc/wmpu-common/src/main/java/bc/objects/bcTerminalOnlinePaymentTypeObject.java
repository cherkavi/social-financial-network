package bc.objects;

import java.util.HashMap;
import java.util.Map;

public class bcTerminalOnlinePaymentTypeObject extends bcObject {
	
	private String idIdTerminalOperation;
	
	private Map<String,Comparable<String>> fieldHm = new HashMap<String, Comparable<String>>();
	
	public bcTerminalOnlinePaymentTypeObject(String pIdTerminalOperation){
		this.idIdTerminalOperation = pIdTerminalOperation;
		getFeature();
	}
	
	private void getFeature() {
		String mySQL = " SELECT * FROM " + getGeneralDBScheme() + ".vc_term_online_pay_type_cl_all WHERE id_term_online_pay_type = ?";
		fieldHm = getFeatures2(mySQL, this.idIdTerminalOperation, false);
	}
	
	public String getValue(String pColumnName) {
		return getFeautureResult(fieldHm, pColumnName);
	}
    
}
