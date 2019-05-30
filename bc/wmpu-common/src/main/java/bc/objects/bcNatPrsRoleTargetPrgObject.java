package bc.objects;

import java.util.HashMap;
import java.util.Map;

import bc.service.bcFeautureParam;

public class bcNatPrsRoleTargetPrgObject extends bc.objects.bcObject {
    
	private Map<String,Comparable<String>> fieldHm = new HashMap<String, Comparable<String>>();
	
	private String idNatPrsRoleTargetPrg;
	
	public bcNatPrsRoleTargetPrgObject(String pIdNatPrsRoleTargetPrg) {
		this.idNatPrsRoleTargetPrg = pIdNatPrsRoleTargetPrg;
		getFeature();
	}
	
	private void getFeature() {
		String featureSelect =
			"SELECT * FROM " + getGeneralDBScheme() + ".vc_nat_prs_role_tp_club_all " + " WHERE id_nat_prs_role_target_prg = ? ";
			
		bcFeautureParam[] array = new bcFeautureParam[1];
		array[0] = new bcFeautureParam("int", this.idNatPrsRoleTargetPrg);
		fieldHm = getFeatures2(featureSelect, array);
	}
	
	public String getValue(String pColumnName) {
		return getFeautureResult(fieldHm, pColumnName);
	}

}
