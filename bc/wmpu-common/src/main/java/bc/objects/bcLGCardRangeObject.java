package bc.objects;

import java.util.HashMap;
import java.util.Map;

import bc.service.bcFeautureParam;

public class bcLGCardRangeObject extends bcObject {
    
	private Map<String,Comparable<String>> fieldHm = new HashMap<String, Comparable<String>>();
	
	private String idRecord;
	private String idLine;
	
	public bcLGCardRangeObject(String pIdRecord, String pIdLine) {
		this.idRecord = pIdRecord;
		this.idLine = pIdLine;
		getFeature();
	}
	
	private void getFeature() {
		String mySQL = " SELECT * FROM " + getGeneralDBScheme() + ".VC_LG_CARD_RNG_PRIV_ALL WHERE id_lg_record = ? AND id_lg_card_range = ?";
		
		bcFeautureParam[] array = new bcFeautureParam[2];
		array[0] = new bcFeautureParam("int", this.idRecord);
		array[1] = new bcFeautureParam("int", this.idLine);

		fieldHm = getFeatures2(mySQL, array);
	}
	
	public String getValue(String pColumnName) {
		return getFeautureResult(fieldHm, pColumnName);
	}
}
