package webpos;

import java.util.HashMap;
import java.util.Map;

import bc.service.bcFeautureParam;

public class wpNatPrsTargetPrgObject extends wpObject {
    
	private Map<String,Comparable<String>> fieldHm = new HashMap<String, Comparable<String>>();
	
	private String idTargetPrg;
	private String idNatPrs;
	
	public wpNatPrsTargetPrgObject(String pIdTargetPrg, String pIdNatPrs) {
		this.idTargetPrg = pIdTargetPrg;
		this.idNatPrs = pIdNatPrs;
		getFeature();
	}
	
	private void getFeature() {
		String featureSelect =
			"SELECT a.id_target_prg, a.name_target_prg, a.desc_target_prg, a.id_club, " +
			"       a.sname_club, a.id_target_prg_parent, a.id_nat_prs_initiator, " +
			"       a.name_nat_prs_initiator, a.id_nat_prs_administrator, " +
			"       a.name_nat_prs_administrator, a.id_jur_prs, " +
			"       a.name_jur_prs, a.adr_jur_prs, a.date_beg, " +
			"       a.date_beg_frmt, a.date_end, a.date_end_frmt, a.creation_date, " +
			"       a.created_by, a.last_update_date, a.last_update_by, " +
			"       a.child_count, a.cd_currency, a.name_currency, a.sname_currency, " +
			"       a.scd_currency, a.cd_target_prg_pay_period, " +
			"       a.name_target_prg_pay_period, a.name_target_prg_pay_per_full, " +
			"       a.pay_amount, a.pay_amount_frmt, a.pay_amount_full_frmt, " +
			"       a.min_pay_amount, a.min_pay_amount_frmt, a.pay_count, a.pay_description, a.need_subscribe, " +
			"       a.need_administrator_confirm, a.entrance_fee, " +
			"       a.entrance_fee_frmt, " +
			"       b.id_nat_prs_role_target_prg, b.id_nat_prs, b.cd_target_prg_use_type, " +
			"       b.is_administrator_confirm, b.pay_entrance_fee, " +
			"       b.pay_entrance_fee_frmt, b.can_subscribe " +
			"  FROM (SELECT * " +
			"          FROM " + getGeneralDBScheme() + ".vc_target_prg_club_all " +
			"         WHERE id_target_prg = ? " +
			"       ) a " +
			"       LEFT JOIN " + 
			"       (SELECT * " +
			"          FROM " + getGeneralDBScheme() + ".vc_nat_prs_role_tp_sh_all " +
			"         WHERE id_target_prg = ? " +
			"           AND id_nat_prs = ?) b " +
			"    ON (a.id_target_prg = b.id_target_prg)";
			
		bcFeautureParam[] array = new bcFeautureParam[3];
		array[0] = new bcFeautureParam("int", this.idTargetPrg);
		array[1] = new bcFeautureParam("int", this.idTargetPrg);
		array[2] = new bcFeautureParam("int", isEmpty(this.idNatPrs)?"-1":this.idNatPrs);
		
		fieldHm = getFeatures2(featureSelect, array);
	}
	
	public String getValue(String pColumnName) {
		return getFeautureResult(fieldHm, pColumnName);
	}

}
