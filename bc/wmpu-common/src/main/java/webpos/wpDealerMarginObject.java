package webpos;

import java.util.HashMap;
import java.util.Map;

import bc.service.bcFeautureParam;

public class wpDealerMarginObject extends wpObject {
    
	private Map<String,Comparable<String>> fieldHm = new HashMap<String, Comparable<String>>();

	private String idClub;
	private String idDealer;
	private String cdTransType;
	
	public wpDealerMarginObject(String pIdClub, String pIdDealer, String pCdTransType) {
		this.idClub = pIdClub;
		this.idDealer = pIdDealer;
		this.cdTransType = pCdTransType;
		getFeature();
	}
	
	private void getFeature() {
		bcFeautureParam[] array = new bcFeautureParam[3];
		array[0] = new bcFeautureParam("string", this.cdTransType);
		array[1] = new bcFeautureParam("int", this.idClub);
		array[2] = new bcFeautureParam("int", this.idDealer);
		
		String featureSelect = 
			" SELECT * " +
			"   FROM (SELECT * " +
			"           FROM " + getGeneralDBScheme() + ".vc_jur_prs_oper_margin_all " +
			"          WHERE (TRUNC(SYSDATE) BETWEEN TRUNC(begin_action_date) AND NVL(TRUNC(end_action_date),TRUNC(SYSDATE))) " +
			"            AND fcd_trans_type = ? " +
			"            AND id_club = ? " +
			"            AND id_jur_prs = ? " +
			"          ORDER BY begin_action_date DESC)" +
			"  WHERE rownum < 2 ";
		fieldHm = getFeatures2(featureSelect, array);
	}
	
	public String getValue(String pColumnName) {
		return getFeautureResult(fieldHm, pColumnName);
	}

}
