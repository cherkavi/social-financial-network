package webpos;

import java.util.HashMap;
import java.util.Map;

import bc.service.bcFeautureParam;

public class wpTargetPrgObject extends wpObject {
    
	private Map<String,Comparable<String>> fieldHm = new HashMap<String, Comparable<String>>();
	
	private String idTargetPrg;
	
	public wpTargetPrgObject(String pIdTargetPrg) {
		this.idTargetPrg = pIdTargetPrg;
		getFeature();
	}
	
	private void getFeature() {
		String featureSelect =
			"SELECT * " +
			"  FROM " + getGeneralDBScheme() + ".vc_target_prg_club_all " +
			"  WHERE id_target_prg = ? ";
			
		bcFeautureParam[] array = new bcFeautureParam[1];
		array[0] = new bcFeautureParam("int", this.idTargetPrg);
		
		fieldHm = getFeatures2(featureSelect, array);
	}
	
	public String getValue(String pColumnName) {
		return getFeautureResult(fieldHm, pColumnName);
	}

}
