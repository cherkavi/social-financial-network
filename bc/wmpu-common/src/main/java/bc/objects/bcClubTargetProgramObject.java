package bc.objects;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import bc.connection.Connector;
import bc.lists.bcListReferralScheme;
import bc.lists.bcListTransaction;
import bc.service.bcFeautureParam;

public class bcClubTargetProgramObject extends bcObject {
	private final static Logger LOGGER=Logger.getLogger(bcClubTargetProgramObject.class);
	
	private String idTargetProgram;
	
	private Map<String,Comparable<String>> fieldHm = new HashMap<String, Comparable<String>>();
	
	public bcClubTargetProgramObject(String pIdTargetProgram){
		this.idTargetProgram = pIdTargetProgram;
		getFeature();
	}
	
	private void getFeature() {
		String mySQL = " SELECT * FROM " + getGeneralDBScheme() + ".vc_target_prg_club_all WHERE id_target_prg = ?";
		fieldHm = getFeatures2(mySQL, this.idTargetProgram, false);
	}
	
	public String getValue(String pColumnName) {
		return getFeautureResult(fieldHm, pColumnName);
	}


    public String getRestsHTML(String pBeginPeriod, String pEndPeriod, String pFind, String p_beg, String p_end) {
        StringBuilder html = new StringBuilder();
        Connection con = null;
        PreparedStatement st = null; 

        ArrayList<bcFeautureParam> pParam = initParamArray();
        
        String mySQL = 
	        	"SELECT rn, date_from_frmt, /*sname_currency,*/ " +
	            "       value_target_prg_bal_beg_amnt, value_target_prg_income_amnt, " +
	            "       value_target_prg_expense_amnt, value_target_prg_turnover_amnt, value_target_prg_bal_end_amnt " +
	            "  FROM (SELECT ROWNUM rn, date_from_frmt, sname_currency, " +
	            "               value_target_prg_bal_beg_amnt, value_target_prg_income_amnt, " +
	            "               value_target_prg_expense_amnt, value_target_prg_turnover_amnt, value_target_prg_bal_end_amnt" +
	            "          FROM (SELECT a.date_from_frmt, a.sname_currency, " +
	            "                       CASE WHEN a.value_target_prg_balance_beg > 0 THEN '<font color=\"green\"><b>'||a.value_target_prg_bal_beg_frmt||'</b></font>' " +
	            "                            WHEN a.value_target_prg_balance_beg < 0 THEN '<font color=\"blue\"><b>'||a.value_target_prg_bal_beg_frmt||'</b></font>' " +
	    		"                            ELSE a.value_target_prg_bal_beg_frmt " +
	    		"                       END||' '||a.sname_currency value_target_prg_bal_beg_amnt, " +
	    		"                       CASE WHEN NVL(a.value_target_prg_income,0) <> 0 THEN '<font color=\"green\"><b>'||a.value_target_prg_income_frmt||'</b></font>' " +
	            "                            ELSE a.value_target_prg_income_frmt " +
	            "                       END||' '||a.sname_currency value_target_prg_income_amnt, " +
	            "                       CASE WHEN NVL(a.value_target_prg_expense,0) <> 0 THEN '<font color=\"blue\"><b>'||a.value_target_prg_expense_frmt||'</b></font>' " +
	            "                            ELSE a.value_target_prg_expense_frmt " +
	            "                       END||' '||a.sname_currency value_target_prg_expense_amnt, " +
	            "                       CASE WHEN a.value_target_prg_turnover > 0 THEN '<font color=\"green\"><b>'||a.value_target_prg_turnover_frmt||'</b></font>' " +
	            "                            WHEN a.value_target_prg_turnover < 0 THEN '<font color=\"blue\"><b>'||a.value_target_prg_turnover_frmt||'</b></font>' " +
	    		"                            ELSE a.value_target_prg_turnover_frmt " +
	    		"                       END||' '||a.sname_currency value_target_prg_turnover_amnt, " +
	    		"                       CASE WHEN a.value_target_prg_balance_end > 0 THEN '<font color=\"green\"><b>'||a.value_target_prg_bal_end_frmt||'</b></font>' " +
	            "                            WHEN a.value_target_prg_balance_end < 0 THEN '<font color=\"blue\"><b>'||a.value_target_prg_bal_end_frmt||'</b></font>' " +
	    		"                            ELSE a.value_target_prg_bal_end_frmt " +
	    		"                       END||' '||a.sname_currency value_target_prg_bal_end_amnt " +
	            "                  FROM " + getGeneralDBScheme() + ".vc_target_prg_rest_all a ";
	    mySQL = mySQL + 
	            "                 WHERE id_target_prg = ? ";
        pParam.add(new bcFeautureParam("int", this.idTargetProgram));
        
        
        if (!isEmpty(pBeginPeriod)) {
        	mySQL = mySQL + " and date_from >= TO_DATE(?,?) ";
        	pParam.add(new bcFeautureParam("string", pBeginPeriod));
        	pParam.add(new bcFeautureParam("string", this.getDateFormat()));
        }
        if (!isEmpty(pEndPeriod)) {
        	mySQL = mySQL + " and date_from <= TO_DATE(?,?) ";
        	pParam.add(new bcFeautureParam("string", pEndPeriod));
        	pParam.add(new bcFeautureParam("string", this.getDateFormat()));
        }
        if (!isEmpty(pFind)) {
        	mySQL = mySQL + 
				" AND (UPPER(date_from_frmt) LIKE UPPER('%'||?||'%') OR " +
				"      UPPER(value_target_prg_bal_beg_frmt) LIKE UPPER('%'||?||'%') OR " +
				"      UPPER(value_target_prg_turnover_frmt) LIKE UPPER('%'||?||'%') OR " +
				"      UPPER(value_target_prg_bal_end_frmt) LIKE UPPER('%'||?||'%')) ";
        	for (int i=0; i<4; i++) {
        	    pParam.add(new bcFeautureParam("string", pFind));
        	}
		}
        pParam.add(new bcFeautureParam("int", p_end));
        pParam.add(new bcFeautureParam("int", p_beg));
        mySQL = mySQL + " ORDER BY TRUNC(date_from) DESC " +
        	"    ) WHERE ROWNUM < ? " + 
        	" ) WHERE rn >= ?";
       
        try{
        	
        	LOGGER.debug(prepareSQLToLog(mySQL, pParam));
        	con = Connector.getConnection(getSessionId());
            st = con.prepareStatement(mySQL);
            st = prepareParam(st, pParam);
            ResultSet rset = st.executeQuery();
            ResultSetMetaData mtd = rset.getMetaData();            
            int colCount = mtd.getColumnCount();
            
            html.append(getBottomFrameTable());
            html.append("<tr>");
            html.append(getBottomFrameTable());
            html.append("<tr>");
            html.append("<th rowspan=\"2\">"+ commonXML.getfieldTransl("RN", false)+"</th>\n");
            html.append("<th rowspan=\"2\">"+ clubfundXML.getfieldTransl("DATE_TURNOVER", false)+"</th>\n");
            //html.append("<th rowspan=\"2\">"+ clubcardXML.getfieldTransl("SNAME_CURRENCY", false)+"</th>\n");
            html.append("<th rowspan=\"2\">"+ clubfundXML.getfieldTransl("BEGIN_BALANCE", false)+"</th>\n");
            html.append("<th colspan=\"3\">"+ clubfundXML.getfieldTransl("FUND_BALANCE", false)+"</th>\n");
            html.append("<th rowspan=\"2\">"+ clubfundXML.getfieldTransl("END_BALANCE", false)+"</th>\n");
            html.append("</tr>");
            html.append("<tr>");
            html.append("<th>"+ clubfundXML.getfieldTransl("FUND_INCOME", false)+"</th>\n");
            html.append("<th>"+ clubfundXML.getfieldTransl("FUND_EXPENSE", false)+"</th>\n");
            html.append("<th>"+ clubfundXML.getfieldTransl("FUND_TURNOVER", false)+"</th>\n");
            html.append("</tr></thead><tbody>");
            html.append("<tr>");
            html.append("</tr></thead><tbody>\n");
            
            while (rset.next())
                {
                    html.append("<tr>");
              	  	for (int i=1; i <= colCount; i++) {
              	  		html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", "", ""));
              	  		/*if ("VALUE_FUND_BALANCE_BEG_AMNT".equalsIgnoreCase(mtd.getColumnName(i)) ||
              	  			"VALUE_FUND_TURNOVER_AMNT".equalsIgnoreCase(mtd.getColumnName(i)) ||
              	  			"VALUE_FUND_BALANCE_END_AMNT".equalsIgnoreCase(mtd.getColumnName(i))) {
              	  			html.append(getBottomFrameTableTDAlign(mtd.getColumnName(i), rset.getString(i), "", "", "", "right"));
              	  		} else {
              	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", "", ""));
              	  		}*/
              	  	}
              	  	html.append("</tr>\n");
                }
            html.append("</tbody></table>\n");
        } // try
        catch (SQLException e) {LOGGER.error(html, e); html.append(showCRMException(e));}
       	catch (Exception el) {LOGGER.error(html, el); html.append(showCRMException(el));}
        finally {
            try {
                if (st!=null) st.close();
            } catch (SQLException w) {w.toString();}
            try {
                if (con!=null) con.close();
            } catch (SQLException w) {w.toString();}
            Connector.closeConnection(con);
        } // finally
        return html.toString();
    }

    public String getReferralSchemeHTML(String pFindString, String pSchemeType, String pCalcType, String p_beg, String p_end) {
    	bcListReferralScheme list = new bcListReferralScheme();
    	
    	String pWhereCause = " WHERE id_target_prg = ? ";
    	
    	ArrayList<bcFeautureParam> pWhereValue = new ArrayList<bcFeautureParam>();
    	pWhereValue.add(new bcFeautureParam("int", this.idTargetProgram));
    	String myDeleteLink = "";
    	String myEditLink = "";
    	String myCopyLink = "";
    	if (isEditPermited("CLUB_TARGET_PROGRAM_REFERRAL_SCHEME")>0) {
    		myDeleteLink = "../crm/club/referral_schemeupdate.jsp?back_type=TARGET_PROGRAM&type=general&id="+this.idTargetProgram+"&action=remove&process=yes";
    	    myEditLink = "../crm/club/referral_schemeupdate.jsp?back_type=TARGET_PROGRAM&type=general&id="+this.idTargetProgram;
    	    myCopyLink = "../crm/club/referral_schemeupdate.jsp?back_type=TARGET_PROGRAM&type=general&id="+this.idTargetProgram;
    	}
    	
    	return list.getReferralSchemesHTML(pWhereCause, pWhereValue, pFindString, pSchemeType, pCalcType, myEditLink, myCopyLink, myDeleteLink, p_beg, p_end);
    	
    }
	
    public String getReferralSchemeHTML(String pFind, String p_beg, String p_end) {
        StringBuilder html = new StringBuilder();
        Connection con = null;
        PreparedStatement st = null;
        ArrayList<bcFeautureParam> pParam = initParamArray();

        String mySQL = 
        	" SELECT rn, " +
        	"        name_referral_scheme, desc_referral_scheme, " +
        	"        DECODE(cd_referral_scheme_calc_type," +
        	"               'POINT_SUM', '<font color=\"blue\"><b>'||name_referral_scheme_calc_type||'<b></font>', " +
            "               'CLUB_SUM', '<font color=\"green\"><b>'||name_referral_scheme_calc_type||'<b></font>', " +
            "               name_referral_scheme_calc_type" +
        	"        ) name_referral_scheme_calc_type, " +
        	"		 accounting_level_count, accounting_percent_all_frmt, " +
        	"        date_beg_frmt, date_end_frmt, id_referral_scheme " +
        	"   FROM (SELECT ROWNUM rn, name_referral_scheme, desc_referral_scheme, cd_referral_scheme_type, name_referral_scheme_type, " +
        	"		         cd_referral_scheme_calc_type, name_referral_scheme_calc_type, " +
        	"                accounting_level_count, accounting_percent_all_frmt, " +
        	"                date_beg_frmt, date_end_frmt, id_referral_scheme " +
        	"   	    FROM (SELECT * " +
        	"                   FROM " + getGeneralDBScheme()+".vc_referral_scheme_club_all " +
            "                  WHERE id_target_prg = ? ";
    	pParam.add(new bcFeautureParam("int", this.idTargetProgram));
    	
    	if (!isEmpty(pFind)) {
    		mySQL = mySQL + 
    			" AND (TO_CHAR(name_referral_scheme) LIKE UPPER('%'||?||'%') OR " +
    			"      UPPER(desc_referral_scheme) LIKE UPPER('%'||?||'%') OR " +
    			"      UPPER(date_beg_frmt) LIKE UPPER('%'||?||'%') OR " +
    			"      UPPER(date_end_frmt) LIKE UPPER('%'||?||'%'))";
    		for (int i=0; i<4; i++) {
    		    pParam.add(new bcFeautureParam("string", pFind));
    		}
    	}
    	pParam.add(new bcFeautureParam("int", p_end));
    	pParam.add(new bcFeautureParam("int", p_beg));
        mySQL = mySQL + 
	        "                 ORDER BY date_beg, name_referral_scheme DESC) " +
            "          WHERE ROWNUM < ? " + 
            " ) WHERE rn >= ?";
        
        boolean hasReferralSchemePermission = false;
        boolean hasEditPermission = false;
        
        try{
        	if (isEditMenuPermited("CRM_CLUB_REFERRAL_SCHEME")>=0) {
        		hasReferralSchemePermission = true;
            }
        	if (isEditPermited("CLUB_TARGET_PROGRAM_REFERRAL_SCHEME")>0) {
        		hasEditPermission = true;
            }
        	
        	LOGGER.debug(prepareSQLToLog(mySQL, pParam));
        	con = Connector.getConnection(getSessionId());
        	st = con.prepareStatement(mySQL);
        	st = prepareParam(st, pParam);
        	ResultSet rset = st.executeQuery();
            ResultSetMetaData mtd = rset.getMetaData();
            int colCount = mtd.getColumnCount();
            
            html.append(getBottomFrameTable());
            html.append("<tr>");
            for (int i=1; i <= colCount-1; i++) {
                html.append(getBottomFrameTableTH(clubXML, mtd.getColumnName(i)));
            }
            if (hasEditPermission) {
                html.append("<th>&nbsp;</th>\n");
            }
            html.append("</tr></thead><tbody>");
            while (rset.next())
            {
            	html.append("<tr>");
          	  	for (int i=1; i <= colCount-1; i++) {
          	  		if ("NAME_REFERRAL_SCHEME".equalsIgnoreCase(mtd.getColumnName(i)) &&
          	  				hasReferralSchemePermission) {
          	  			html.append(getBottomFrameTableTD("NAME_REFERRAL_SCHEME", rset.getString("NAME_REFERRAL_SCHEME"), "../crm/club/referral_schemespecs.jsp?id="+rset.getString("ID_REFERRAL_SCHEME"), "", ""));
          	  		} else {
          	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", "", ""));
          	  		}
          	  	}
                if (hasEditPermission) {
                	String myDeleteLink = "../crm/club/referral_schemeupdate.jsp?back_type=TARGET_PROGRAM&type=general&id_target_prg="+this.idTargetProgram+"&id_referral_scheme="+rset.getString("ID_REFERRAL_SCHEME")+"&action=remove&process=yes";
                	html.append(getDeleteButtonHTML(myDeleteLink, clubfundXML.getfieldTransl("h_delete_referral_scheme", false), rset.getString("NAME_REFERRAL_SCHEME")));
                }
          	  	html.append("</tr>\n");
            }
            html.append("</tbody></table>\n");
        } // try
        catch (SQLException e) {LOGGER.error(html, e); html.append(showCRMException(e));}
       	catch (Exception el) {LOGGER.error(html, el); html.append(showCRMException(el));}
        finally {
            try {
                if (st!=null) st.close();
            } catch (SQLException w) {w.toString();}
            try {
                if (con!=null) con.close();
            } catch (SQLException w) {w.toString();}
            Connector.closeConnection(con);
        } // finally
        return html.toString();
    } //getAssignTermHistoryHTML
	
    public String getParticipantsHTML(String pFind, String p_beg, String p_end) {
        StringBuilder html = new StringBuilder();
        Connection con = null;
        PreparedStatement st = null;
        ArrayList<bcFeautureParam> pParam = initParamArray();

        String mySQL = 
        	"SELECT rn, fio_nat_prs, cd_card1, pay_entrance_fee_frmt, membership_fee_sum_frmt, membership_last_date_frmt, " +
        	"       cd_subscribe_state cd_subscribe_state_tsl, " +
        	"       is_administrator_confirm is_administrator_confirm_tsl, id_nat_prs, id_nat_prs_role_target_prg," +
        	"       card_serial_number, card_id_issuer, card_id_payment_system " +
        	"  FROM (SELECT ROWNUM rn, fio_nat_prs, cd_card1, cd_subscribe_state, is_administrator_confirm, " +
        	"               pay_entrance_fee_frmt, membership_fee_sum_frmt, membership_last_date_frmt, " +
        	"				id_nat_prs, id_nat_prs_role_target_prg," +
        	"               card_serial_number, card_id_issuer, card_id_payment_system " +
        	"		   FROM (SELECT a.fio_nat_prs, a.cd_card1, " +
        	"                		DECODE(a.cd_subscribe_state, " +
    		"                       	   'Y', '<font color=\"green\"><b>"+clubfundXML.getfieldTransl("cd_subscribe_state_yes", false)+"</b></font>', " +
    		"                              'N', '<font color=\"red\"><b>"+clubfundXML.getfieldTransl("cd_subscribe_state_no", false)+"</b></font>', " +
    		"                              'NC', '<font color=\"red\"><b>"+clubfundXML.getfieldTransl("cd_subscribe_state_not_confirm", false)+"</b></font>', " +
    		"                              'NU', '<font color=\"gray\"><b>"+clubfundXML.getfieldTransl("cd_subscribe_state_not_used", false)+"</b></font>', " +
    		"                              '"+clubfundXML.getfieldTransl("cd_subscribe_state_unknown", false)+"'" +
    		"                       ) cd_subscribe_state," +
    		"                       DECODE(a.is_administrator_confirm, " +
    		"                       	   'Y', '<font color=\"green\"><b>"+commonXML.getfieldTransl("yes", false)+"</b></font>', " +
    		"                              'N', '<font color=\"red\"><b>"+commonXML.getfieldTransl("no", false)+"</b></font>', " +
    		"                              ''" +
    		"                       ) is_administrator_confirm," +
        	"						CASE WHEN pay_entrance_fee IS NULL " +
            "							 THEN '' " +
            "							 ELSE a.pay_entrance_fee_frmt||' '||a.sname_currency " +
            "						END pay_entrance_fee_frmt, " +
            "                       CASE WHEN membership_fee_sum IS NULL " +
            "							 THEN '' " +
            "							 ELSE a.membership_fee_sum_frmt||' '||a.sname_currency " +
            "						END membership_fee_sum_frmt, membership_last_date_frmt, " +
        	" 						a.id_nat_prs, a.id_nat_prs_role_target_prg," +
        	"                       a.card_serial_number, a.card_id_issuer, a.card_id_payment_system " +
            "                   FROM " + getGeneralDBScheme() + ".vc_nat_prs_role_tp_club_all a"+
            "                  WHERE a.id_target_prg = ? ";
    	pParam.add(new bcFeautureParam("int", this.idTargetProgram));
    	
    	if (!isEmpty(pFind)) {
    		mySQL = mySQL + 
    			" AND (TO_CHAR(fio_nat_prs) LIKE UPPER('%'||?||'%'))";
    		for (int i=0; i<1; i++) {
    		    pParam.add(new bcFeautureParam("string", pFind));
    		}
    	}
    	pParam.add(new bcFeautureParam("int", p_end));
    	pParam.add(new bcFeautureParam("int", p_beg));
        mySQL = mySQL + 
	        "                 /*ORDER BY fio_nat_prs DESC*/) " +
            "          WHERE ROWNUM < ? " + 
            " ) WHERE rn >= ?";
        
        boolean hasNatPrsPermission = false;
        boolean hasCardPermission = false;
        boolean hasEditPermission = false;
        
        try{
        	if (isEditMenuPermited("CLIENTS_NATPERSONS")>=0) {
        		hasNatPrsPermission = true;
            }
        	if (isEditMenuPermited("CARDS_CLUBCARDS")>=0) {
        		hasCardPermission = true;
            }
        	if (isEditPermited("CLUB_TARGER_PROGRAM_PARTICIPANT")>0) {
        		hasEditPermission = true;
            }
        	
        	LOGGER.debug(prepareSQLToLog(mySQL, pParam));
        	con = Connector.getConnection(getSessionId());
        	st = con.prepareStatement(mySQL);
        	st = prepareParam(st, pParam);
        	ResultSet rset = st.executeQuery();
            ResultSetMetaData mtd = rset.getMetaData();
            int colCount = mtd.getColumnCount();
            
            html.append(getBottomFrameTable());
            html.append("<tr>");
            for (int i=1; i <= colCount-5; i++) {
                html.append(getBottomFrameTableTH(clubfundXML, mtd.getColumnName(i)));
            }
            if (hasEditPermission) {
                html.append("<th>&nbsp;</th><th>&nbsp;</th>\n");
            }
            html.append("</tr></thead><tbody>");
            while (rset.next())
            {
            	html.append("<tr>");
          	  	for (int i=1; i <= colCount-5; i++) {
          	  		if ("FIO_NAT_PRS".equalsIgnoreCase(mtd.getColumnName(i)) &&
          	  			hasNatPrsPermission &&
          	  				!isEmpty(rset.getString("ID_NAT_PRS"))) {
          	  			html.append(getBottomFrameTableTD("FIO_NAT_PRS", rset.getString("FIO_NAT_PRS"), "../crm/clients/natpersons.jsp?id="+rset.getString("ID_NAT_PRS"), "", ""));
          	  		} else if (hasCardPermission && "CD_CARD1".equalsIgnoreCase(mtd.getColumnName(i)) &&
          	  				!isEmpty(rset.getString("CD_CARD1"))) {
          	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/cards/clubcardspecs.jsp?id="+rset.getString("CARD_SERIAL_NUMBER")+"&iss="+rset.getString("CARD_ID_ISSUER")+"&paysys="+rset.getString("CARD_ID_PAYMENT_SYSTEM"), "", ""));
         	  		} else {
          	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", "", ""));
          	  		}
          	  	}
                if (hasEditPermission) {
                	String myEditLink = "../crm/club/target_programupdate.jsp?type=participant&id_target_prg="+this.idTargetProgram+"&id_nat_prs_role_target_prg="+rset.getString("ID_NAT_PRS_ROLE_TARGET_PRG");
                	String myDeleteLink = "../crm/club/target_programupdate.jsp?type=participant&id_target_prg="+this.idTargetProgram+"&id_nat_prs_role_target_prg="+rset.getString("ID_NAT_PRS_ROLE_TARGET_PRG")+"&action=remove&process=yes";
                	html.append(getDeleteButtonHTML(myDeleteLink, clubfundXML.getfieldTransl("h_delete_participant", false), rset.getString("FIO_NAT_PRS")));
                	html.append(getEditButtonWitDivHTML(myEditLink, "div_data_detail"));
                }
          	  	html.append("</tr>\n");
            }
            html.append("</tbody></table>\n");
        } // try
        catch (SQLException e) {LOGGER.error(html, e); html.append(showCRMException(e));}
       	catch (Exception el) {LOGGER.error(html, el); html.append(showCRMException(el));}
        finally {
            try {
                if (st!=null) st.close();
            } catch (SQLException w) {w.toString();}
            try {
                if (con!=null) con.close();
            } catch (SQLException w) {w.toString();}
            Connector.closeConnection(con);
        } // finally
        return html.toString();
    } //getAssignTermHistoryHTML
	
    public String getSubprogramsHTML(String pFind, String pPayPeriod, String p_beg, String p_end) {
        StringBuilder html = new StringBuilder();
        Connection con = null;
        PreparedStatement st = null;
        ArrayList<bcFeautureParam> pParam = initParamArray();

        String mySQL = 
        	" SELECT rn, " +
        	"        name_target_prg, " +
        	"        name_target_prg_pay_period target_prg_pay_period, " +
        	"        pay_amount_full_frmt pay_amount_short_frmt, desc_target_prg, " +
        	"        /*sname_club,*/ date_beg_frmt target_program_date_beg, date_end_frmt target_program_date_end, " +
        	"        id_target_prg " +
        	"   FROM (SELECT ROWNUM rn, id_target_prg, " +
        	"                '<span>'||LPAD(' ', ((level_target_prg-1)*4*6), '&nbsp;')||name_target_prg||'</span>' name_target_prg, desc_target_prg, " +
        	"                name_target_prg_pay_period, " +
        	"                DECODE(cd_target_prg_pay_period, NULL, '', pay_amount_full_frmt) pay_amount_full_frmt, sname_club, date_beg_frmt, date_end_frmt, " +
        	"                level_target_prg " +
        	"   		  FROM (SELECT order_rn, id_target_prg, " +
        	"                          CASE WHEN id_target_prg_parent IS NULL " +
        	"                               THEN '<b><font color=\"green\">'||name_target_prg||'</font></b>'" +
        	"                               ELSE name_target_prg" +
        	"                          END name_target_prg, desc_target_prg, " +
        	"                          cd_target_prg_pay_period, " +
        	"                          CASE WHEN NVL(child_count,0) > 0 THEN '' ELSE name_target_prg_pay_period END name_target_prg_pay_period, pay_amount_full_frmt, " +
        	"                          sname_club, date_beg_frmt, date_end_frmt, " +
        	"                          level_target_prg" +
        	"                    FROM (SELECT ROWNUM order_rn, id_target_prg, name_target_prg, desc_target_prg, " +
        	"                                 DECODE(cd_target_prg_pay_period, " +
            "               						 'IRREGULAR', '<font color=\"red\"><b>'||name_target_prg_pay_period||'</b></font>', " +
            "               						 'HOUR', '<font color=\"green\"><b>'||name_target_prg_pay_period||'</b></font>', " +
            "               						 'DAY', '<font color=\"green\"><b>'||name_target_prg_pay_period||'</b></font>', " +
            "               						 'WEEK', '<font color=\"green\"><b>'||name_target_prg_pay_period||'</b></font>', " +
            "               						 'MONTH', '<font color=\"green\"><b>'||name_target_prg_pay_period||'</b></font>', " +
            "               						 'STUDY_COUNT', '<font color=\"blue\"><b>'||name_target_prg_pay_period||'</b></font>', " +
            "               						 name_target_prg_pay_period" +
            "        		 		 		  ) name_target_prg_pay_period, pay_amount_full_frmt, " +
        	"                                 cd_target_prg_pay_period, sname_club, date_beg_frmt, " +
        	"                                 date_end_frmt, id_club, id_target_prg_parent, " +
        	"                                 id_jur_prs, LEVEL level_target_prg, child_count " +
        	"                            FROM " + getGeneralDBScheme()+".vc_target_prg_club_all " +
        	"                           START WITH id_target_prg_parent IS NULL " +
        	"                         CONNECT BY PRIOR id_target_prg = id_target_prg_parent " +
        	"                           ORDER siblings BY id_club, name_target_prg, id_target_prg) " + 
            "                  WHERE id_target_prg_parent = ? ";
    	pParam.add(new bcFeautureParam("int", this.idTargetProgram));
    	
    	if (!isEmpty(pFind)) {
    		mySQL = mySQL + 
    			" AND (UPPER(name_target_prg) LIKE UPPER('%'||?||'%') OR " +
				"	   UPPER(desc_target_prg) LIKE UPPER('%'||?||'%')) ";
    		for (int i=0; i<2; i++) {
    		    pParam.add(new bcFeautureParam("string", pFind));
    		}
    	}
    	if (!isEmpty(pPayPeriod)) {
    		mySQL = mySQL + " AND cd_target_prg_pay_period = ? ";
    		pParam.add(new bcFeautureParam("string", pPayPeriod));
    	}
    	pParam.add(new bcFeautureParam("int", p_end));
    	pParam.add(new bcFeautureParam("int", p_beg));
        mySQL = mySQL + 
	        "                 ORDER BY order_rn) " +
            "          WHERE ROWNUM < ? " + 
            " ) WHERE rn >= ?";

        try{
        	LOGGER.debug(prepareSQLToLog(mySQL, pParam));
        	con = Connector.getConnection(getSessionId());
        	st = con.prepareStatement(mySQL);
        	st = prepareParam(st, pParam);
        	ResultSet rset = st.executeQuery();
            ResultSetMetaData mtd = rset.getMetaData();
            int colCount = mtd.getColumnCount();
            
            html.append(getBottomFrameTable());
            html.append("<tr>");
            for (int i=1; i <= colCount-1; i++) {
                html.append(getBottomFrameTableTH(clubfundXML, mtd.getColumnName(i)));
            }
            html.append("</tr></thead><tbody>");
            while (rset.next())
            {
            	html.append("<tr>");
            	for (int i=1; i <= colCount-1; i++) {
            		if ("NAME_TARGET_PRG".equalsIgnoreCase(mtd.getColumnName(i)) &&
          	  			!isEmpty(rset.getString("NAME_TARGET_PRG"))) {
          	  			html.append(getBottomFrameTableTD("NAME_TARGET_PRG", rset.getString("NAME_TARGET_PRG"), "../crm/club/target_programspecs.jsp?id="+rset.getString("ID_TARGET_PRG")+"&tab=1", "", ""));
          	  		} else {
          	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", "", ""));
          	  		}
            	}
          	  	html.append("</tr>\n");
            }
            html.append("</tbody></table>\n");
        } // try
        catch (SQLException e) {LOGGER.error(html, e); html.append(showCRMException(e));}
       	catch (Exception el) {LOGGER.error(html, el); html.append(showCRMException(el));}
        finally {
            try {
                if (st!=null) st.close();
            } catch (SQLException w) {w.toString();}
            try {
                if (con!=null) con.close();
            } catch (SQLException w) {w.toString();}
            Connector.closeConnection(con);
        } // finally
        return html.toString();
    } //getAssignTermHistoryHTML
	
    public String getTransactionsHTML(String pFindString, String pTypeTrans, String pPayType, String pStateTrans, String p_beg, String p_end) {
    	bcListTransaction list = new bcListTransaction();
    	
    	String pWhereCause = " WHERE id_target_prg = ? ";
    	
    	ArrayList<bcFeautureParam> pWhereValue = new ArrayList<bcFeautureParam>();
    	pWhereValue.add(new bcFeautureParam("int", this.idTargetProgram));
    	
    	return list.getTransactionList(pWhereCause, pWhereValue, pFindString, pTypeTrans, pPayType, pStateTrans, p_beg, p_end);
    }
	
    public String getServicePlaces(String pFind, String p_beg, String p_end) {
        StringBuilder html = new StringBuilder();
        Connection con = null;
        PreparedStatement st = null;
        ArrayList<bcFeautureParam> pParam = initParamArray();

        String mySQL = 
        	" SELECT rn, sname_service_place, adr_full adr_jur_prs, " +
        	"	     date_beg_frmt payment_date_beg_frmt, " +
        	"        date_end_frmt payment_date_end_frmt, id_service_place, id_target_prg_place " +
        	"   FROM (SELECT ROWNUM rn, sname_service_place, adr_full, " +
        	"		         date_beg_frmt, date_end_frmt, id_service_place, id_target_prg_place " +
        	"   	    FROM (SELECT * " +
        	"                   FROM " + getGeneralDBScheme()+".vc_target_prg_place_all " +
            "                  WHERE id_target_prg = ? ";
    	pParam.add(new bcFeautureParam("int", this.idTargetProgram));
    	
    	if (!isEmpty(pFind)) {
    		mySQL = mySQL + 
    			" AND (TO_CHAR(sname_service_place) LIKE UPPER('%'||?||'%') OR " +
    			"      UPPER(adr_full) LIKE UPPER('%'||?||'%') OR " +
    			"      UPPER(date_beg_frmt) LIKE UPPER('%'||?||'%') OR " +
    			"      UPPER(date_end_frmt) LIKE UPPER('%'||?||'%'))";
    		for (int i=0; i<4; i++) {
    		    pParam.add(new bcFeautureParam("string", pFind));
    		}
    	}
    	pParam.add(new bcFeautureParam("int", p_end));
    	pParam.add(new bcFeautureParam("int", p_beg));
        mySQL = mySQL + 
	        "                 ORDER BY sname_service_place) " +
            "          WHERE ROWNUM < ? " + 
            " ) WHERE rn >= ?";
        
        boolean hasJurPrsPermission = false;
        boolean hasEditPermission = false;
        
        try{
        	if (isEditPermited("CLUB_TARGET_PROGRAM_SERVICE_PLACES")>0) {
        		hasEditPermission = true;
        	}
        	if (isEditMenuPermited("CLIENTS_YURPERSONS")>=0) {
        		hasJurPrsPermission = true;
            }
        	
        	LOGGER.debug(prepareSQLToLog(mySQL, pParam));
        	con = Connector.getConnection(getSessionId());
        	st = con.prepareStatement(mySQL);
        	st = prepareParam(st, pParam);
        	ResultSet rset = st.executeQuery();
            ResultSetMetaData mtd = rset.getMetaData();
            int colCount = mtd.getColumnCount();
            
            html.append(getBottomFrameTable());
            html.append("<tr>");
            for (int i=1; i <= colCount-2; i++) {
                html.append(getBottomFrameTableTH(clubfundXML, mtd.getColumnName(i)));
            }
            if (hasEditPermission) {
            	html.append("<th>&nbsp;</th><th>&nbsp;</th>\n"); 
            }
            html.append("</tr></thead><tbody>");
            while (rset.next())
            {
            	html.append("<tr>");
          	  	for (int i=1; i <= colCount-2; i++) {
          	  		if ("SNAME_SERVICE_PLACE".equalsIgnoreCase(mtd.getColumnName(i)) &&
          	  				hasJurPrsPermission) {
          	  			html.append(getBottomFrameTableTD("SNAME_SERVICE_PLACE", rset.getString("SNAME_SERVICE_PLACE"), "../crm/clients/yurpersonspecs.jsp?id="+rset.getString("ID_SERVICE_PLACE"), "", ""));
          	  		} else {
          	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", "", ""));
          	  		}
          	  	}
	            if (hasEditPermission) {
	            	String myHyperLink = "../crm/club/target_programupdate.jsp?id_target_prg="+this.idTargetProgram+"&id_target_prg_place="+rset.getString("ID_TARGET_PRG_PLACE")+"&type=service_place";
	            	html.append(getDeleteButtonBaseHTML(myHyperLink, "remove", "yes", "delete.png", clubfundXML.getfieldTransl("h_delete_service_place", false) + " \"" + rset.getString("SNAME_SERVICE_PLACE").replace("'", "\"") + "\"", "div_data_detail"));
		           	html.append(getEditButtonWitDivHTML(myHyperLink, "div_data_detail"));
	            }
          	  	html.append("</tr>\n");
            }
            html.append("</tbody></table>\n");
        } // try
        catch (SQLException e) {LOGGER.error(html, e); html.append(showCRMException(e));}
       	catch (Exception el) {LOGGER.error(html, el); html.append(showCRMException(el));}
        finally {
            try {
                if (st!=null) st.close();
            } catch (SQLException w) {w.toString();}
            try {
                if (con!=null) con.close();
            } catch (SQLException w) {w.toString();}
            Connector.closeConnection(con);
        } // finally
        return html.toString();
    } //getAssignTermHistoryHTML
    
}
