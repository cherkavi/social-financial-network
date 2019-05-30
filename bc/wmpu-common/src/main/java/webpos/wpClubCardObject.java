package webpos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import bc.objects.bcTerminalLoyLineHistoryObject;
import bc.service.bcFeautureParam;

public class wpClubCardObject extends wpObject {
	//private final static Logger LOGGER=Logger.getLogger(wpClubCardObject.class);
	
	private String cardSerialNumber;
	private String idIssuer;
	private String idPaymentSystem;
	private String cdCard1;
	private boolean needCalcTasks;
	
	private Map<String,Comparable<String>> fieldHm = new HashMap<String, Comparable<String>>();
	
	public wpClubCardObject(String pCdCard1){
		this.cdCard1 = pCdCard1;
		this.needCalcTasks = false;
		getFeature();
	}
	
	public wpClubCardObject(String pCardSerialNumber, String pIdIssuer, String pIdPaymentSystem){
		this.cardSerialNumber = pCardSerialNumber;
		this.idIssuer = pIdIssuer;
		this.idPaymentSystem = pIdPaymentSystem;
		this.needCalcTasks = false;
		getFeature();
	}
	
	public wpClubCardObject(String pCardSerialNumber, String pIdIssuer, String pIdPaymentSystem, boolean pNeedCalcTasks){
		this.cardSerialNumber = pCardSerialNumber;
		this.idIssuer = pIdIssuer;
		this.idPaymentSystem = pIdPaymentSystem;
		this.needCalcTasks = pNeedCalcTasks;
		getFeature();
	}
	
	private void getFeature() {
		String featureSelect = "";
		if (!isEmpty(this.cdCard1)) {
			featureSelect = " SELECT * FROM " + getGeneralDBScheme() + ".vp$card_all WHERE cd_card1 = ?";
			
			bcFeautureParam[] array = new bcFeautureParam[1];
			array[0] = new bcFeautureParam("string", this.cdCard1);
			
			fieldHm = getFeatures2(featureSelect, array);
			
			this.cardSerialNumber = this.getValue("CARD_SERIAL_NUMBER");
			this.idIssuer = this.getValue("ID_ISSUER");
			this.idPaymentSystem = this.getValue("ID_PAYMENT_SYSTEM");
			
		} else {
			if (!(this.needCalcTasks)) {
				featureSelect = " SELECT * FROM " + getGeneralDBScheme() + ".vp$card_all WHERE card_serial_number = ? AND id_issuer = ? AND id_payment_system = ?";
				
				bcFeautureParam[] array = new bcFeautureParam[3];
				array[0] = new bcFeautureParam("string", this.cardSerialNumber);
				array[1] = new bcFeautureParam("int", this.idIssuer);
				array[2] = new bcFeautureParam("int", this.idPaymentSystem);
				
				fieldHm = getFeatures2(featureSelect, array);
			} else {
				featureSelect = 
					"SELECT c.*, TO_CHAR(c.bal_feauture/100,'fm9999999999990d099') bal_feauture_frmt, " +
					" c.bal_acc + c.bal_cur - c.bal_feauture bal_calc, " +
					" TO_CHAR((c.bal_acc + c.bal_cur - c.bal_feauture)/100,'fm9999999999990d099') bal_calc_frmt " +
					" FROM (" + 
					" SELECT a.*, " + getGeneralDBScheme() + ".fnc_calc_bon_card_task_rests(a.card_serial_number, a.id_issuer, a.id_payment_system) bal_feauture " + 
					" FROM " + getGeneralDBScheme() + ".vp$card_all a " +
					" WHERE a.card_serial_number = ?" +
			        "   AND a.id_issuer = ?" +
			        "   AND a.id_payment_system = ?) c";
				
				bcFeautureParam[] array = new bcFeautureParam[3];
				array[0] = new bcFeautureParam("string", this.cardSerialNumber);
				array[1] = new bcFeautureParam("int", this.idIssuer);
				array[2] = new bcFeautureParam("int", this.idPaymentSystem);
				
				fieldHm = getFeatures2(featureSelect, array);
			}
		}
	}
	
	public String getValue(String pColumnName) {
		return getFeautureResult(fieldHm, pColumnName);
	}
    
    public String getUserTargetProgramImagesHTML(String pFind, String p_beg, String p_end) {
    	String mySQL = "";        
        ArrayList<bcFeautureParam> pParam = initParamArray();        

    	mySQL = 
    		" SELECT rn, id_target_prg, name_target_prg, sname_target_prg, desc_target_prg, id_club, " +
            "		 sname_club, name_nat_prs_initiator, min_pay_amount, min_pay_amount_frmt, " +
            "		 name_nat_prs_administrator, name_jur_prs, " +
            "		 adr_jur_prs, date_beg_frmt, date_end_frmt, id_target_prg_parent, child_count, image_target_prg, row_count " +
            " FROM (SELECT ROWNUM rn, id_target_prg, name_target_prg, sname_target_prg, desc_target_prg, id_club, " +
            "			   sname_club, name_nat_prs_initiator, min_pay_amount, min_pay_amount_frmt, " +
            "			   name_nat_prs_administrator, name_jur_prs, " +
            "			   adr_jur_prs, date_beg_frmt, date_end_frmt, id_target_prg_parent, child_count, image_target_prg, row_count " +
            " 		  FROM (SELECT id_target_prg, name_target_prg, sname_target_prg, desc_target_prg, id_club, " +
            "					   sname_club, name_nat_prs_initiator, min_pay_amount, min_pay_amount_frmt||' '||sname_currency min_pay_amount_frmt, " +
            "					   name_nat_prs_administrator, name_jur_prs, " +
            "					   adr_jur_prs, date_beg_frmt, date_end_frmt, id_target_prg_parent, child_count, image_target_prg," +
            "					   count(*) over () as row_count " +
            "           	  FROM " + getGeneralDBScheme() + ".vc_nat_prs_role_tp_club_all " +
            "                WHERE id_nat_prs = ? ";
    	pParam.add(new bcFeautureParam("int", this.getValue("ID_NAT_PRS")));
    	
        if (!isEmpty(pFind)) {
        	mySQL = mySQL + " AND (UPPER(name_target_prg) LIKE UPPER('%'||?||'%')) ";
        	for (int i=0; i<1; i++) {
        	    pParam.add(new bcFeautureParam("string", pFind));
        	}
       	}
        pParam.add(new bcFeautureParam("int", p_end));
        pParam.add(new bcFeautureParam("int", p_beg));
        mySQL = mySQL +
            "                ORDER BY name_target_prg) " +
            "        WHERE ROWNUM < ?) " +
            " WHERE rn >= ?";
        
        return geTargetProgramImagesSelecOneHTML(this.getValue("ID_NAT_PRS"), "USERS", mySQL, pParam, "updateForm5", "div_main");
    } // getCardTransHTML
    
    public String getWEBClientCardParamHTML(wpTerminalObject term) {
    	StringBuilder html = new StringBuilder();

    	wpNatPrsRoleObject role = null;
    	boolean roleExist = false;
    	boolean cardQuestioed = false;
    	if (!(this.getValue("ID_NAT_PRS_ROLE_CURRENT") == null || "".equalsIgnoreCase(this.getValue("ID_NAT_PRS_ROLE_CURRENT")))) {
    		role = new wpNatPrsRoleObject(this.getValue("ID_NAT_PRS_ROLE_CURRENT"));
    		roleExist = true;
    		if (!("GIVEN".equalsIgnoreCase(role.getValue("CD_NAT_PRS_ROLE_STATE")))) {
        		cardQuestioed = true;
        	}
    	}
    	
    	bcTerminalLoyLineHistoryObject loy = new bcTerminalLoyLineHistoryObject(term.getValue("ID_LOYALITY_HISTORY", "LOYALITY"), this.getValue("ID_CARD_STATUS"), this.getValue("ID_BON_CATEGORY"));
        
        if (cardQuestioed) {
            html.append("<tr><td>" + webposXML.getfieldTransl("client_fullname", false) + "</td><td>" + getValue3(role.getValue("FIO_NAT_PRS")) + "</td></tr>\n");
        }
       	html.append("<tr><td width=\"60%\">" + webposXML.getfieldTransl("cheque_cd_card1", false) + "</td><td>" + getValue3(this.getValue("CD_CARD1_HIDE")) + "</td></tr>\n");
        html.append("<tr><td>" + webposXML.getfieldTransl("cheque_card_type", false) + "</td><td>" + getValue3(this.getValue("NAME_CARD_TYPE")) + "</td></tr>\n");
        html.append("<tr><td>" + webposXML.getfieldTransl("cheque_card_state", false) + "</td><td>" + getValue3(this.getValue("NAME_CARD_STATE")) + "</td></tr>\n");
        String lCardStatus = "";
        if (roleExist) {
	        if ("Y".equalsIgnoreCase(role.getValue("IS_SHAREHOLDER"))) {
	        	lCardStatus = lCardStatus + webposXML.getfieldTransl("cheque_is_shareholder", false);
	        }
	        if ("Y".equalsIgnoreCase(role.getValue("IS_ORGANIZER"))) {
	        	lCardStatus = lCardStatus + ("".equalsIgnoreCase(lCardStatus)?"":" / ") + webposXML.getfieldTransl("cheque_is_organizer", false);
	        }
	        if ("Y".equalsIgnoreCase(role.getValue("IS_INVESTOR"))) {
	        	lCardStatus = lCardStatus + ("".equalsIgnoreCase(lCardStatus)?"":" / ") + webposXML.getfieldTransl("cheque_is_investor", false);
	        }
        }
        lCardStatus = lCardStatus.toLowerCase();
        if (!isEmpty(lCardStatus)) {
        	lCardStatus = lCardStatus.substring(0, 1).toUpperCase()+lCardStatus.substring(1);
            html.append("<tr><td>" + webposXML.getfieldTransl("cheque_card_status", false) + "</td><td>" + getValue3(lCardStatus) + "</td></tr>\n");
        }
        //html.append("<tr><td>" + webposXML.getfieldTransl("cheque_organization", false) + "</td><td>" + getValue3(this.getValue("NAME_CARD_STATUS")) + "</td></tr>\n");
        html.append("<tr><td>" + webposXML.getfieldTransl("cheque_share_account_amount", false) + "</td><td>" + getValue3(this.getValue("CONS_VALUE_CARD_PURSE_FRMT")) + " " + getValue3(this.getValue("SNAME_CURRENCY")) + "</td></tr>\n");
        html.append("<tr><td>" + webposXML.getfieldTransl("cheque_bal_acc", false) + "</td><td>" + getValue3(this.getValue("BAL_ACC_FRMT")) + "</td></tr>\n");
        html.append("<tr><td>" + webposXML.getfieldTransl("cheque_bal_cur", false) + "</td><td>" + getValue3(this.getValue("BAL_CUR_FRMT")) + "</td></tr>\n");
        html.append("<tr><td>" + webposXML.getfieldTransl("cheque_nt_icc", false) + "</td><td>" + getValue3(this.getValue("NT_ICC")) + "</td></tr>\n");
        if (cardQuestioed) {
            html.append("<tr><td>" + webposXML.getfieldTransl("club_date_beg", false) + "</td><td>" + getValue3(role.getValue("DATE_CARD_SALE_DF")) + "</td></tr>\n");
        }
        if (roleExist) {
            html.append("<tr><td>" + webposXML.getfieldTransl("membership_month_sum", false) + "</td><td>" + getValue3(role.getValue("MEMBERSHIP_MONTH_SUM_FRMT")) + " " + getValue3(this.getValue("SNAME_CURRENCY")) + "</td></tr>\n");
            html.append("<tr><td>" + webposXML.getfieldTransl("membership_last_date", false) + "</td><td>" + getValue3(role.getValue("MEMBERSHIP_LAST_DATE_DF")) + "</td></tr>\n");
        }
        html.append("<tr><td>" + webposXML.getfieldTransl("cheque_expiry_date", false) + "</td><td>" + getValue3(this.getValue("EXPIRY_DATE_FRMT")) + "</td></tr>\n");
        html.append("<tr><td colspan=\"2\">" + webposXML.getfieldTransl("cheque_bonus_transfer_term", false) + ":</td></tr>\n");
        html.append("<tr><td>" + webposXML.getfieldTransl("cheque_bonus_transfer_term_money", false) + "</td><td>" + getValue3(loy.getValue("BONUS_TRANSFER_TERM_MONEY")) + "</td></tr>\n");
        html.append("<tr><td>" + webposXML.getfieldTransl("cheque_bonus_transfer_term_points", false) + "</td><td>" + /*getValue3(term.getValue("BONUS_TRANSFER_TERM", "LOYALITY"))*/ getValue3(loy.getValue("BONUS_TRANSFER_TERM_POINT")) + "</td></tr>\n");

        /*//html.append("<tr><td>" + clubcardXML.getfieldTransl("date_beg_frmt", false) + "</td><td>" + getValue3(this.getValue("DATE_BEG_FRMT")) + "</td></tr>\n");
        html.append("<tr><td>" + clubcardXML.getfieldTransl("name_bon_category", false) + "</td><td>" + getValue3(this.getValue("NAME_BON_CATEGORY")) + "</td></tr>\n");
        html.append("<tr><td>" + clubcardXML.getfieldTransl("club_bon", false) + "</td><td>" + getValue3(this.getValue("CLUB_BON_PERCENT")) + "</td></tr>\n");
        html.append("<tr><td>" + cardsettingXML.getfieldTransl("bonus_calc_term", false) + "</td><td>" + getValue3(this.getValue("BONUS_CALC_TERM")) + "</td></tr>\n");
        html.append("<tr><td>" + clubcardXML.getfieldTransl("name_disc_category", false) + "</td><td>" + getValue3(this.getValue("NAME_DISC_CATEGORY")) + "</td></tr>\n");
        html.append("<tr><td>" + clubcardXML.getfieldTransl("club_disc", false) + "</td><td>" + getValue3(this.getValue("CLUB_DISC_PERCENT")) + "</td></tr>\n");
        //html.append("<tr><td>" + clubcardXML.getfieldTransl("bal_exist_frmt", false) + "</td><td>" + getValue3(this.getValue("BAL_EXIST_FRMT")) + "</td></tr>\n");
        html.append("<tr><td>" + clubcardXML.getfieldTransl("date_acc_frmt", false) + "</td><td>" + getValue3(this.getValue("DATE_ACC_FRMT")) + "</td></tr>\n");
        html.append("<tr><td>" + clubcardXML.getfieldTransl("date_mov_frmt", false) + "</td><td>" + getValue3(this.getValue("DATE_MOV_FRMT")) + "</td></tr>\n");
        html.append("<tr><td>" + clubcardXML.getfieldTransl("date_calc_frmt", false) + "</td><td>" + getValue3(this.getValue("DATE_CALC_FRMT")) + "</td></tr>\n");
        html.append("<tr><td>" + clubcardXML.getfieldTransl("date_last_trans_frmt", false) + "</td><td>" + getValue3(this.getValue("DATE_LAST_TRANS_FRMT")) + "</td></tr>\n");
        //html.append("<tr><td>" + clubcardXML.getfieldTransl("inv_value_card_purse_frmt", false) + "</td><td>" + getValue3(this.getValue("INV_VALUE_CARD_PURSE_FRMT")) + "</td></tr>\n");
        */
        return html.toString();
    } // getCardTransHTML
	
	public String getCardParamAllButtons() {

		
		wpNatPrsRoleObject role = null;
    	if (!(this.getValue("ID_NAT_PRS_ROLE_CURRENT") == null || "".equalsIgnoreCase(this.getValue("ID_NAT_PRS_ROLE_CURRENT")))) {
    		role = new wpNatPrsRoleObject(this.getValue("ID_NAT_PRS_ROLE_CURRENT"));
    	}
    	
		StringBuilder result = new StringBuilder();
		/*result.append("<script language=\"javascript\">\n");
		result.append("function validateSendCheque(){\n");
		result.append("send_email = document.getElementById('send_EMAIL');\n");
		result.append("if (send_email.checked) {\n");
		result.append("document.getElementById('div_result').innerHTML = '';\n");
		result.append("return true;\n");
		result.append("} else {\n");
		result.append("alert('" + webposXML.getfieldTransl("send_cheque_select_options_error", false) + "');\n");
		result.append("return false;\n");
		result.append("}\n");
		result.append("}\n");
		result.append("</script>\n");*/

		result.append("<div style=\"display: inline-block;\">");
		result.append("<a class=\"button button_small\" style=\"height: 22px !important; width: 120px !important; display: block; text-decoration: none; padding-top:5px\" href=\"#openModal2\">" + webposXML.getfieldTransl("title_cheque_actions", false) + "</a>\n");
		result.append("<div class=\"modalDialog\" id=\"openModal2\">\n");;
		result.append("<div>\n");
		result.append("<a class=\"close\" title=\"" + buttonXML.getfieldTransl("close", false) + "\" href=\"#close\">X</a>\n");
		//result.append("<form name=\"updateForm21\" id=\"updateForm21\" accept-charset=\"UTF-8\" method=\"POST\">\n");
		//result.append("<input type=\"hidden\" name=\"cd_card1\" value=\""+this.getValue("CD_CARD1")+"\">\n");
		result.append("<table class=\"tabledialog\"><thead></thead><tbody>\n");
		
		result.append("<tr><td align=\"left\" style=\"padding-top: 5px; \"><font style=\"font-weight: bold; color: blue; align:center; font-size: 13px;\">" + webposXML.getfieldTransl("title_print_card_param", false) + "</font></td></tr>\n");
		result.append("<tr><td align=\"left\" style=\"padding-bottom: 5px;\"><input type=\"button\" class=\"button button_small\" onclick=\"JavaScript: var cWin=window.open('service/check_cardprint.jsp?cd_card1=" + this.getValue("CD_CARD1") + "&print=Y','blank','height=600,width=420,top=50,left=150,toolbar=no,menubar=no,location=no,scrollbars=yes,status=no'); cWin.focus(); cWin.printDiv('printableArea');\" value=\"" + buttonXML.getfieldTransl("button_print", false) +"\" /></td></tr>\n");
		boolean canSendEmail = false;
		
		result.append("<tr><td align=\"left\" style=\"padding-top: 5px; border-top: 1px dashed gray;\"><font style=\"font-weight: bold; color: blue; align:center; font-size: 13px;\">" + webposXML.getfieldTransl("title_send_card_param_email", false) + "</font></td></tr>\n");
		if (!isEmpty(role.getValue("EMAIL"))) {
			canSendEmail = true;
			
			result.append("<tr><td align=\"left\" style=\"text-transform:none;\">" + webposXML.getfieldTransl("send_card_param_email_hint", false) + " <b style=\"color:green;\">" + role.getValue("EMAIL_HIDE") + "</b></td></tr>\n");
		} else {
			result.append("<tr><td align=\"left\" style=\"text-transform:none;\"><font style=\"color: red;\"><input type=\"checkbox\" style=\"visibility: hidden\" value=\"N\" id=\"send_EMAIL\" name=\"send_EMAIL\">" + webposXML.getfieldTransl("send_cheque_email_error", false) + "</font></td></tr>\n");
		}
		if (canSendEmail) {
			result.append("<tr><td align=\"left\"><div id=\"div_result\"><button type=\"button\" class=\"button button_small\" onclick=\"ajaxpage('service/check_cardsend.jsp?cd_card1="+this.getValue("CD_CARD1")+"','div_result'); this.disabled=false; this.className = 'button';\" class=\"button\" type=\"button\">" + buttonXML.getfieldTransl("send", false) + "</button></div></td></tr>\n");
		}
		result.append("</tbody></table>\n");
		//result.append("</form>\n");
		result.append("</div>\n");
		result.append("</div>\n");
		result.append("</div>\n");
		return result.toString();
	}
    
}
