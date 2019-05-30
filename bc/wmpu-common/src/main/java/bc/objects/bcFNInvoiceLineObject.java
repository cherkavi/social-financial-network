package bc.objects;

import java.util.HashMap;
import java.util.Map;

public class bcFNInvoiceLineObject extends bcObject {
	private Map<String,Comparable<String>> fieldHm = new HashMap<String, Comparable<String>>();
	
	private String idInvoice;
	
	public bcFNInvoiceLineObject(String pIdInvoice) {
		this.idInvoice = pIdInvoice;
		getFeature();
	}
	
	private void getFeature() {
		String featureSelect = " SELECT * FROM " + getGeneralDBScheme() + ".VC_FN_INVOICE_LINE_ALL WHERE id_invoice_line = ?";
		fieldHm = getFeatures2(featureSelect, this.idInvoice, false);
	}
	
	public String getValue(String pColumnName) {
		return getFeautureResult(fieldHm, pColumnName);
	}

}
