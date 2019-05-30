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
import bc.lists.bcListBankAccount;
import bc.lists.bcListCardPackage;
import bc.lists.bcListDocument;
import bc.lists.bcListLoyalityScheme;
import bc.lists.bcListReferralScheme;
import bc.lists.bcListSystemRole;
import bc.lists.bcListTargetProgram;
import bc.lists.bcListTerminal;
import bc.lists.bcListTransaction;
import bc.service.bcFeautureParam;


public class bcJurPrsObject extends bcObject {
	private final static Logger LOGGER=Logger.getLogger(bcJurPrsObject.class);
	
	private Map<String,Comparable<String>> fieldHm = new HashMap<String, Comparable<String>>();
	
	private String idJurPrs;
	
	public bcJurPrsObject(String pIdJurPrs) {
		this.idJurPrs = pIdJurPrs;
		getFeature();
	}

	private void getFeature() {
		String featureSelect = " SELECT * FROM " + getGeneralDBScheme() + ".VC_JUR_PRS_PRIV_ALL WHERE id_jur_prs = ?";
		fieldHm = getFeatures2(featureSelect, this.idJurPrs, false);
	}
	
	public String getValue(String pColumnName) {
		return getFeautureResult(fieldHm, pColumnName);
	}

    public String getJurPersonServicePlacesHTML(String pFindString, String pJurPrsForm, String p_beg, String p_end) {
	      StringBuilder html = new StringBuilder();
	      Connection con = null;
	      PreparedStatement st = null;
	      
	      ArrayList<bcFeautureParam> pParam = initParamArray();

	      String mySQL = 
	    	  " SELECT rn, /*name_jur_prs_kind,*/ " +
	    	  "        name_jur_prs_form, name_jur_prs, " +
	    	  "        fact_adr_full, club_date_beg,  name_club_member_status, id_jur_prs, id_club, name_jur_prs_initial " +
	    	  "	FROM (SELECT ROWNUM rn, name_jur_prs_form, id_jur_prs, name_jur_prs_kind, " +
	    	  "                CASE WHEN cd_jur_prs_status = 'PARTNER' " +
	    	  "                         THEN '<b><font color=\"blue\" title=\"" + jurpersonXML.getfieldTransl("title_partner", false) + "\">'||name_jur_prs||'</font></b>'" +
	    	  "                         ELSE '<font color=\"green\" title=\"" + jurpersonXML.getfieldTransl("title_service_place", false) + "\">'||name_jur_prs||'</font>'" +
	    	  "                END  name_jur_prs, " +
	    	  "                fact_adr_full, club_date_beg, " +
	    	  "				   DECODE(cd_club_member_status, " +
	    	  "                    		'CLOSED', '<font color=\"red\"><b>'||name_club_member_status||'</b></font>', " +
	    	  "                    		'OUT_OF_CLUB', '<font color=\"gray\">'||name_club_member_status||'</font>', " +
	    	  "                    		'IN_PROCESS', '<font color=\"blue\">'||name_club_member_status||'</font>', " +
	    	  "                    		'PARTICIPATE', '<font color=\"green\">'||name_club_member_status||'</font>', " +
	    	  "                    		name_club_member_status" +
	    	  "                ) name_club_member_status," +
	    	  "				   id_club, name_jur_prs name_jur_prs_initial" +
	    	  "	        FROM (SELECT id_jur_prs, name_jur_prs_form, cd_jur_prs_status, name_jur_prs_kind, id_jur_prs_parent, " +
	    	  "                      name_jur_prs, fact_adr_full, date_beg_frmt club_date_beg, " +
	    	  "                      cd_club_member_status, name_club_member_status, id_club " +
	    	  "                   FROM " + getGeneralDBScheme()+".vc_jur_prs_priv_all a" +
              "                  WHERE id_jur_prs_parent = ? ";
	      pParam.add(new bcFeautureParam("int", this.idJurPrs));
	      
	      if (!isEmpty(pFindString)) {
	    		mySQL = mySQL + 
	   				" AND (TO_CHAR(id_jur_prs) LIKE UPPER('%'||?||'%') OR " +
	   				"      UPPER(name_jur_prs) LIKE UPPER('%'||?||'%') OR " +
	   				"      UPPER(sname_jur_prs) LIKE UPPER('%'||?||'%') OR " +
	   				"      UPPER(date_beg_frmt) LIKE UPPER('%'||?||'%') OR " +
	   				"      UPPER(fact_adr_full) LIKE UPPER('%'||?||'%'))";
	    		for (int i=0; i<5; i++) {
	    		    pParam.add(new bcFeautureParam("string", pFindString));
	    		}
	      }
	      if (!isEmpty(pJurPrsForm)) {
	    		mySQL = mySQL + " AND id_jur_prs_form = ? ";
	    		pParam.add(new bcFeautureParam("int", pJurPrsForm));
	      }
	      pParam.add(new bcFeautureParam("int", p_end));
	      pParam.add(new bcFeautureParam("int", p_beg));
	      mySQL = mySQL +
	      	  " ORDER BY name_jur_prs)" +
              "          WHERE ROWNUM < ? " + 
              " ) WHERE rn >= ?";
	      
	      boolean hasEditPermission = false;
	      try{
	    	  if (isEditPermited("CLIENTS_YURPERSONS_SERVICEPLACE")>0) {
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
	          for (int i=1; i <= colCount-3; i++) {
	        	  html.append(getBottomFrameTableTH(jurpersonXML, mtd.getColumnName(i)));
	          }
	          if (hasEditPermission) {
	              html.append("<th>&nbsp;</th><th>&nbsp;</th>\n");
	          }
	          html.append("</tr></thead><tbody>\n");
	          while (rset.next())
	          {
	        	  html.append("<tr>");
	          	  for (int i=1; i <= colCount-3; i++) {
	          		if ("NAME_JUR_PRS".equalsIgnoreCase(mtd.getColumnName(i))) {
          	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/clients/yurpersonspecs.jsp?id="+rset.getString("ID_JUR_PRS"), "", ""));
          	  		} else {
          	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", "", ""));
          	  		}
	          	  }
	              if (hasEditPermission) {
	            	  String myHyperLink = "../crm/clients/yurpersonserviceplaceupdate.jsp?type=service_place&id_jur_prs="+this.idJurPrs+"&id_service_place="+rset.getString("ID_JUR_PRS");
	            	  String myDeleteLink = "../crm/clients/yurpersonserviceplaceupdate.jsp?type=service_place&id_jur_prs="+this.idJurPrs+"&id_club="+rset.getString("ID_CLUB")+"&id_service_place="+rset.getString("ID_JUR_PRS")+"&action=remove&process=yes";
		              html.append(getDeleteButtonHTML(myDeleteLink, jurpersonXML.getfieldTransl("LAB_DELETE_SERVICE_PLACE", false), rset.getString("NAME_JUR_PRS_INITIAL")));
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
	  } // getJurPersonServicePlacesHTML

    public String getBankAccountsHTML(String pFindString, String pBankAccountType, String p_beg, String p_end) {
    	bcListBankAccount list = new bcListBankAccount();
    	
    	String pWhereCause = " WHERE id_jur_prs = ? ";
    	
    	ArrayList<bcFeautureParam> pWhereValue = new ArrayList<bcFeautureParam>();
    	pWhereValue.add(new bcFeautureParam("int", this.idJurPrs));
    	String myDeleteLink = "";
    	String myEditLink = "";
    	String myCopyLink = "";
    	if (isEditPermited("CLIENTS_YURPERSONS_BANKACCNT")>0) {
    		myDeleteLink = "../crm/clients/accountsupdate.jsp?back_type=PARTNER&type=general&id="+this.idJurPrs+"&action=remove&process=yes";
    	    myEditLink = "../crm/clients/accountsupdate.jsp?back_type=PARTNER&type=general&id="+this.idJurPrs;
    	}
    	
    	return list.getCardPackagesHTML(pWhereCause, pWhereValue, "OWNER", pFindString, pBankAccountType, myEditLink, myCopyLink, myDeleteLink, p_beg, p_end);
    	
    }

	    public String getJurPersonBankAccountsHTML(String pFindString, String pType, String p_beg, String p_end) {
	        StringBuilder html = new StringBuilder();
	        Connection con = null;
	        PreparedStatement st = null;
	        boolean hasBankAccountPermission = false;
	        boolean hasCurrencyPermission = false;
	        boolean hasEditPermission = false;

	        ArrayList<bcFeautureParam> pParam = initParamArray();

	        String mySQL = 
	        	" SELECT rn, number_bank_account, name_bank, name_currency, " +
	            "        name_bank_account_type, id_bank_account, id_bank, cd_currency "+
	            "   FROM (SELECT ROWNUM rn, id_bank_account, number_bank_account, " +
	            "                DECODE(cd_bank_account_type, " +
	            "         	 	        'SETTLEMENT_ACCOUNT', '<font color=\"green\"><b>'||name_bank_account_type||'</b></font>', " +
	            "          	 	        'CORRESPONDENT_ACCOUNT', '<font color=\"blue\"><b>'||name_bank_account_type||'</b></font>', " +
	            "          		        name_bank_account_type" +
	            "  		         ) name_bank_account_type, sname_bank name_bank,  " +
	            "                cd_currency, name_currency, " +
	            "                id_bank "+
	            "           FROM (SELECT * " +
                "                   FROM " + getGeneralDBScheme() + ".vc_bank_account_priv_all "+
                "                  WHERE id_jur_prs = ? ";
	        pParam.add(new bcFeautureParam("int", this.idJurPrs));
	        
		    if (!isEmpty(pFindString)) {
		    		mySQL = mySQL + 
		   				" AND (TO_CHAR(id_bank_account) LIKE UPPER('%'||?||'%') OR " +
		   				"      UPPER(number_bank_account) LIKE UPPER('%'||?||'%') OR " +
		   				"      UPPER(desc_bank_account) LIKE UPPER('%'||?||'%') OR " +
		   				"      UPPER(sname_bank) LIKE UPPER('%'||?||'%'))";
		    		for (int i=0; i<4; i++) {
		    		    pParam.add(new bcFeautureParam("string", pFindString));
		    		}
		    }
		    if (!isEmpty(pType)) {
	        	mySQL = mySQL + " AND cd_bank_account_type = ? ";
	        	pParam.add(new bcFeautureParam("string", pType));
	        }
	        
		    pParam.add(new bcFeautureParam("int", p_end));
		    pParam.add(new bcFeautureParam("int", p_beg));
	        mySQL = mySQL +
                "                  ORDER BY number_bank_account)" +
                "          WHERE ROWNUM < ?" + 
                " ) WHERE rn >= ?";
	        try{
	        	if (isEditMenuPermited("CLIENTS_BANK_ACCOUNTS")>=0) {
	            	hasBankAccountPermission = true;
	            }
	        	if (isEditMenuPermited("SETUP_CURRENCY")>=0) {
	            	hasCurrencyPermission = true;
	            }
	        	if (isEditPermited("CLIENTS_YURPERSONS_BANKACCNT")>0) {
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
	            for (int i=1; i <= colCount-3; i++) {
	                html.append(getBottomFrameTableTH(accountXML, mtd.getColumnName(i)));
	            }
	            if (hasEditPermission) {
	                html.append("<th>&nbsp;</th><th>&nbsp;</th>\n");
	            }
	            html.append("</tr></thead><tbody>\n");
	            while (rset.next())
	            {
	            	html.append("<tr>");
	          	  	for (int i=1; i <= colCount-3; i++) {
	          	  		if ("NAME_BANK".equalsIgnoreCase(mtd.getColumnName(i))) {
	          	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/clients/yurpersonspecs.jsp?id="+rset.getString("ID_BANK"), "", ""));
	          	  		} else if (hasBankAccountPermission && 
	          	  				("NUMBER_BANK_ACCOUNT".equalsIgnoreCase(mtd.getColumnName(i)))) {
	          	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/clients/accountspecs.jsp?id="+rset.getString("ID_BANK_ACCOUNT"), "", ""));
		          	  	} else if (hasCurrencyPermission && 
	          	  				("NAME_CURRENCY".equalsIgnoreCase(mtd.getColumnName(i)))) {
	          	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/setup/currencyspecs.jsp?id="+rset.getString("CD_CURRENCY"), "", ""));
	          	  		} else {
	          	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", "", ""));
	          	  		}
	          	  	}
	                if (hasEditPermission) {
		            	String myHyperLink = "../crm/clients/yurpersonupdate.jsp?type=account&id="+this.idJurPrs+"&id_bank_account="+rset.getString("ID_BANK_ACCOUNT");
		            	String myDeleteLink = "../crm/clients/yurpersonupdate.jsp?type=account&id="+this.idJurPrs+"&id_bank_account="+rset.getString("ID_BANK_ACCOUNT")+"&action=remove&process=yes";
		                html.append(getDeleteButtonHTML(myDeleteLink, accountXML.getfieldTransl("h_delete_bank_account", false), rset.getString("NUMBER_BANK_ACCOUNT")));
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
	    } // getJurPersonAccountsHTML
	    
	    public String getTerminalsHTML(String pFindString, String pTermType, String pTermStatus, String pTermRole, String p_beg, String p_end) {
	    	bcListTerminal list = new bcListTerminal();
	    	
	    	String pWhereCause = " ," +
	            "                        (SELECT id_jur_prs " +
	            "                           FROM " + getGeneralDBScheme() + ".v_jur_prs_name_all" +
	            "                          START WITH id_jur_prs = ? " +
	            "                          CONNECT BY PRIOR id_jur_prs = id_jur_prs_parent) b ";
        	if ("JUR_PRS_PLACE".equalsIgnoreCase(pTermRole)) {
        		pWhereCause = pWhereCause + " WHERE s.id_service_place = b.id_jur_prs ";
        	} else if ("FINANCE_ACQUIRER".equalsIgnoreCase(pTermRole)) {
        		pWhereCause = pWhereCause + " WHERE s.id_finance_acquirer = b.id_jur_prs ";
        	} else if ("MANUFACTURER".equalsIgnoreCase(pTermRole)) {
        		pWhereCause = pWhereCause + " WHERE s.id_jur_prs_manufacturer = b.id_jur_prs ";
        	} else if ("TECHNICAL_ACQUIRER".equalsIgnoreCase(pTermRole)) {
        		pWhereCause = pWhereCause + " WHERE s.id_technical_acquirer = b.id_jur_prs ";
        	} else if ("OWNER".equalsIgnoreCase(pTermRole)) {
        		pWhereCause = pWhereCause + " WHERE s.id_term_owner = b.id_jur_prs ";
        	} else {
        		// Запрещаем доступ
        		pWhereCause = pWhereCause + " WHERE 1=0";
        	}
	    	
	    	ArrayList<bcFeautureParam> pWhereValue = new ArrayList<bcFeautureParam>();
	    	pWhereValue.add(new bcFeautureParam("int", this.idJurPrs));
	    	
	    	String myEditLink = "";
	    	if (isEditPermited("CLIENTS_YURPERSONS_TERM")>0) {
	    		myEditLink = "../crm/clients/terminalupdate.jsp?back_type=PARTNER&type=term&id="+this.idJurPrs;
	    	}
	    	
	    	return list.getTerminalsHTML(pWhereCause, pWhereValue, pFindString, pTermType, pTermStatus, "", myEditLink, "", p_beg, p_end);
	    }

	    public String getJurPersonAccPostingHTML(String p_beg, String p_end) {
	        StringBuilder html = new StringBuilder();
	        Connection con = null;
	        PreparedStatement st = null;
	        String mySQL = 
	        	" SELECT id_posting_detail, id_posting_line, operation_date, name_currency, debet_cd_bk_account, " +
	        	"        credit_cd_bk_account, entered_amount, state_posting_tsl " +
        		"   FROM (SELECT ROWNUM rn, id_posting_detail, id_posting_line, operation_date, name_currency, debet_cd_bk_account, " +
	        	"                credit_cd_bk_account, entered_amount, state_posting_tsl " +
        		"           FROM (SELECT a.id_posting_detail, a.id_posting_line, a.operation_date_frmt operation_date, " +
        		" 		                 a.name_currency, a.debet_cd_bk_account_frmt debet_cd_bk_account, " +
        		"                        a.credit_cd_bk_account_frmt credit_cd_bk_account, " +
        		"                        a.entered_amount_frmt entered_amount, a.state_posting_tsl " +
        		"                   FROM " + getGeneralDBScheme() + ".vc_acc_posting_detail_club_all a, " +
        		"	  			         " + getGeneralDBScheme() + ".vc_bk_accounts_member_all da, " +
        		"                        " + getGeneralDBScheme() + ".vc_bk_accounts_member_all ca " +
        		"                  WHERE a.debet_id_bk_account = da.id_bk_account " +
        		"                    AND a.credit_id_bk_account = ca.id_bk_account " +
        		"                    AND (da.id_member = ? OR ca.id_member = ?) " +
        		"                  ORDER BY a.operation_date, a.id_posting_detail) " +
        		"           WHERE ROWNUM < ? " +
        		" ) WHERE rn >= ?";
	        try{
	        	
	        	LOGGER.debug(mySQL + 
	            		", 1={'" + this.idJurPrs + "',string}" + 
	            		", 2={'" + this.idJurPrs + "',string}" + 
	            		", 3={" + p_end + ",int}" + 
	            		", 4={" + p_beg + ",int}");
	        	con = Connector.getConnection(getSessionId());
	        	st = con.prepareStatement(mySQL);
	        	st.setString(1, this.idJurPrs);
	        	st.setString(2, this.idJurPrs);
	        	st.setInt(3, Integer.parseInt(p_end));
	        	st.setInt(4, Integer.parseInt(p_beg));
	        	ResultSet rset = st.executeQuery();
	            ResultSetMetaData mtd = rset.getMetaData();
	            int colCount = mtd.getColumnCount();

	            html.append(getBottomFrameTable());
	            html.append("<tr>");
	            for (int i=1; i <= colCount; i++) {
	                html.append(getBottomFrameTableTH(postingXML, mtd.getColumnName(i)));
	            }
	            html.append("</tr></thead><tbody>\n");
	            while (rset.next())
	            {
	            	html.append("<tr>");
	          	  	for (int i=1; i <= colCount; i++) {
	          	  		html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", "", ""));
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

	    public String getJurPersonComissionHTML(String pFindString, String pComissionType, String p_beg, String p_end) {
	    	ArrayList<bcFeautureParam> pParam = initParamArray();

	    	String mySQL = 
	        	"SELECT rn, name_comission_type, " +
	        	"       sname_jur_prs_payer jur_prs_payer, sname_jur_prs_receiver jur_prs_receiver, " + 
	  	  		"       begin_action_date_frmt, end_action_date_frmt, " +
	  	  		"       fixed_value_frmt, percent_value_frmt, exist_flag_tsl, " +
	  	  		"       id_comission, id_doc, id_comission_type, exist_flag, " +
	  	  		"       payer_has_error, payer_has_error_type_tsl, receiver_has_error, receiver_has_error_type_tsl," +
	  	  		"       id_jur_prs_payer, id_jur_prs_receiver, is_special_comission " +
	  	  		"  FROM (SELECT ROWNUM rn, a.name_comission_type, " +
	  	  		"               a.sname_jur_prs_payer, a.sname_jur_prs_receiver, " + 
	  	  		"               a.name_payment_system, a.begin_action_date_frmt, a.end_action_date_frmt, " +
	  	  		"               a.fixed_value_frmt, a.percent_value_frmt, a.description, a.exist_flag_tsl, " +
	  	  		"               a.id_comission, a.id_doc, a.id_comission_type, a.exist_flag, " +
	  	  		"               a.payer_has_error, a.payer_has_error_type_tsl, " +
	  	  		"               a.receiver_has_error, a.receiver_has_error_type_tsl," +
	  	  		"               a.id_jur_prs_payer, a.id_jur_prs_receiver, " +
	  	  		"               a.is_special_comission " +
	  			"  		   FROM (SELECT * " +
	  			"                  FROM " + getGeneralDBScheme() + ".vc_jur_prs_comission_priv_all " +
      	  		"       	      WHERE (id_jur_prs_payer = ? " + 
      	  		"                    OR id_jur_prs_receiver = ? ) ";
	    	pParam.add(new bcFeautureParam("int", this.idJurPrs));
	    	pParam.add(new bcFeautureParam("int", this.idJurPrs));
	    	
      	    if (!isEmpty(pComissionType)) {
      	       	mySQL = mySQL + " AND id_comission_type = ? ";
    	    	pParam.add(new bcFeautureParam("int", pComissionType));
      	    }
        	if (!isEmpty(pFindString)) {
        		mySQL = mySQL + 
       				" AND (UPPER(sname_jur_prs_payer) LIKE UPPER('%'||?||'%') OR " +
       				"      UPPER(sname_jur_prs_receiver) LIKE UPPER('%'||?||'%') OR " +
       				"      UPPER(begin_action_date_frmt) LIKE UPPER('%'||?||'%') OR " +
       				"      UPPER(end_action_date_frmt) LIKE UPPER('%'||?||'%') OR " +
       				"      UPPER(fixed_value_frmt) LIKE UPPER('%'||?||'%') OR " +
       				"      UPPER(percent_value_frmt) LIKE UPPER('%'||?||'%') OR " +
       				"      UPPER(description) LIKE UPPER('%'||?||'%'))";
        		for (int i=0; i<7; i++) {
        		    pParam.add(new bcFeautureParam("string", pFindString));
        		}
        	}
        	pParam.add(new bcFeautureParam("int", p_end));
        	pParam.add(new bcFeautureParam("int", p_beg));
      	    mySQL = mySQL + 
      		  	"                 ORDER BY name_comission_type, sname_jur_prs_payer, sname_jur_prs_receiver) a " +
	      	  	"        WHERE ROWNUM < ? " + 
	      	  	" ) WHERE rn >= ?";
	        StringBuilder html = new StringBuilder();
	        Connection con = null;        
	        PreparedStatement st = null;
	        int myCnt = 0;
	        String myFont = "";
	        String myFontEnd = "";

	        boolean hasEditPermission = false;
	        //boolean hasRelationshipPermission = false;
	        boolean hasComisTypePermission = false;
	        
	        String payerHyperLink = "";
	        String receiverHyperLink = "";
	  
	        try{
	        	if (isEditPermited("CLIENTS_YURPERSONS_COMISSION")>0) {
	        		hasEditPermission = true;
	        	}
	          	//if (isEditMenuPermited("CLUB_RELATIONSHIP")>=0) {
	        	//	hasRelationshipPermission = true;
	        	//}
	        	if (isEditMenuPermited("CLUB_COMISTYPE")>=0) {
	        	    hasComisTypePermission = true;
	        	}
	        	  
	        	LOGGER.debug(prepareSQLToLog(mySQL, pParam));
	        	con = Connector.getConnection(getSessionId());
	            st = con.prepareStatement(mySQL);
	            st = prepareParam(st, pParam);
	            ResultSet rset = st.executeQuery();
	            ResultSetMetaData mtd = rset.getMetaData();
	                 
	            int colCount = mtd.getColumnCount();
	            
	            if (hasEditPermission) { 
	            	html.append("<form action=\"../crm/clients/yurpersonscomissionupdate.jsp\" name=\"updateForm\" id=\"updateForm\" accept-charset=\"UTF-8\" method=\"POST\">\n");
	            	html.append("<input type=\"hidden\" name=\"type\" value=\"comission\">\n");
	            	html.append("<input type=\"hidden\" name=\"action\" value=\"editall\">\n");
	            	html.append("<input type=\"hidden\" name=\"process\" value=\"yes\">\n");
	            	html.append("<input type=\"hidden\" name=\"id\" value=\""+this.idJurPrs+"\">\n");
	            }
	            html.append(getBottomFrameTable());
	            html.append("<tr>");
	            for (int i=1; i <= colCount-11; i++) {
	            	html.append(getBottomFrameTableTH(comissionXML, mtd.getColumnName(i)));
	            }
	            if (hasEditPermission) {
	            	html.append("<th>&nbsp;</th><th>&nbsp;</th>\n");
	            }
	            html.append("</tr></thead><tbody>\n");
	         
	         while (rset.next())
	         {
	        	 myCnt = myCnt + 1;
	        	 if ("N".equalsIgnoreCase(rset.getString("EXIST_FLAG"))) {
	        		 myFont = "<b><font color=\"red\">";
	        		 myFontEnd = "</font></b>";
	        	 } else if ("Y".equalsIgnoreCase(rset.getString("IS_SPECIAL_COMISSION"))) {
	        		 myFont = "<b><font color=\"green\">";
	        		 myFontEnd = "</font></b>";
	        	 } else {
	        		 myFont = "";
	        		 myFontEnd = "";
	        	 }
	        	 
	        	 if (!isEmpty(rset.getString("ID_JUR_PRS_PAYER"))) {
	        		 payerHyperLink = getHyperLinkFirst()+"../crm/clients/yurpersonspecs.jsp?id="+rset.getString("ID_JUR_PRS_PAYER")+getHyperLinkMiddle();
	        	 } else {
	        		 payerHyperLink = "";
	        	 }
	        	 if (this.idJurPrs.equalsIgnoreCase(rset.getString("ID_JUR_PRS_PAYER"))) {
	        		 payerHyperLink = "";
	        	 }
	        	 
	        	 if (!isEmpty(rset.getString("ID_JUR_PRS_RECEIVER"))) {
	        		 receiverHyperLink = getHyperLinkFirst()+"../crm/clients/yurpersonspecs.jsp?id="+rset.getString("ID_JUR_PRS_RECEIVER")+getHyperLinkMiddle();
	        	 } else {
	        		 receiverHyperLink = "";
	        	 }
	        	 if (this.idJurPrs.equalsIgnoreCase(rset.getString("ID_JUR_PRS_RECEIVER"))) {
	        		 receiverHyperLink = "";
	        	 }
	        	 
	        	 html.append("<tr><td align=\"center\">" + myFont + getValue2(rset.getString("RN")) +  myFontEnd + "</td>\n");
	        	 if (hasComisTypePermission) {
	        		 html.append("<td>" + myFont + getHyperLinkFirst()+"../crm/club/comistypespecs.jsp?id="+rset.getString("ID_COMISSION_TYPE") + getHyperLinkMiddle() + getValue2(rset.getString("NAME_COMISSION_TYPE")) + getHyperLinkEnd() + myFontEnd + "</td>");
	    		 } else {
	    			 html.append("<td>" + myFont + getValue2(rset.getString("NAME_COMISSION_TYPE")) +  myFontEnd + "</td>");
	    		 }
	        	 //if (hasRelationshipPermission) {
	             //	html.append("<td>" + myFont + getHyperLinkFirst()+"../crm/club/relationshipspecs.jsp?id="+rset.getString("ID_CLUB_REL") + getHyperLinkMiddle() + getValue2(rset.getString("FULL_NAME_CLUB_REL")) + getHyperLinkEnd() + myFontEnd + "</td>");
	             //} else {
	             //	html.append("<td>" + myFont + getValue2(rset.getString("FULL_NAME_CLUB_REL")) +  myFontEnd + "</td>");
	             //}
	            	html.append("<td>" + myFont + payerHyperLink + getValue2(rset.getString("JUR_PRS_PAYER")) + getHyperLinkEnd() + myFontEnd);
	        	 if (!"N".equalsIgnoreCase(rset.getString("PAYER_HAS_ERROR"))) {
	        		 html.append("&nbsp;<img vspace=\"0\" hspace=\"0\" src=\"../images/oper/rows/warning.png\" align=\"top\" style=\"border: 0px;\" title=\""+rset.getString("PAYER_HAS_ERROR_TYPE_TSL")+"\">");
	        	 }
	        	 html.append("</td><td>" + myFont + receiverHyperLink + getValue2(rset.getString("JUR_PRS_RECEIVER")) + getHyperLinkEnd() + myFontEnd);
	        	 if (!"N".equalsIgnoreCase(rset.getString("RECEIVER_HAS_ERROR"))) {
	        		 html.append("&nbsp;<img vspace=\"0\" hspace=\"0\" src=\"../images/oper/rows/warning.png\" align=\"top\" style=\"border: 0px;\" title=\""+rset.getString("RECEIVER_HAS_ERROR_TYPE_TSL")+"\">");
	        	 }
	             html.append("</td><td align=\"center\">" + myFont + getValue2(rset.getString("BEGIN_ACTION_DATE_FRMT")) +  myFontEnd + 
	            		   "</td><td align=\"center\">" + myFont + getValue2(rset.getString("END_ACTION_DATE_FRMT")) +  myFontEnd + 
	            		   "</td>\n");
	             if (hasEditPermission) {
	            	 html.append("<input type=\"hidden\" name=\"nameparam"+myCnt+"\" value=\""+rset.getString("ID_COMISSION")+"\">");
	            	 html.append("<td><input type=\"text\" name=\"fixedvalue"+myCnt+"\" size=\"8\" value=\""+getHTMLValue(rset.getString("FIXED_VALUE_FRMT"))+"\" class=\"inputfield\"></td>\n");
	            	 html.append("<td><input type=\"text\" name=\"percentvalue"+myCnt+"\" size=\"8\" value=\""+getHTMLValue(rset.getString("PERCENT_VALUE_FRMT"))+"\" class=\"inputfield\"></td>\n");
	             } else {
	            	 html.append("<td>" + myFont + getValue2(rset.getString("FIXED_VALUE_FRMT")) +  myFontEnd + "</td>\n");
	            	 html.append("<td>" + myFont + getValue2(rset.getString("PERCENT_VALUE_FRMT")) +  myFontEnd + "</td>\n");
	             }
	             html.append("<td align=\"center\">" + myFont + getValue2(rset.getString("EXIST_FLAG_TSL")) +  myFontEnd + 
	          		   "</td>\n");
	             if (hasEditPermission) {
	            	 String myHyperLink = "../crm/clients/yurpersonscomissionupdate.jsp?type=comission&id=" + this.idJurPrs + "&code="+rset.getString("ID_COMISSION");
	            	 String myDeleteLink = "../crm/clients/yurpersonscomissionupdate.jsp?type=comission&id=" + this.idJurPrs + "&code="+rset.getString("ID_COMISSION")+"&action=remove&process=yes";
		             html.append(getDeleteButtonHTML(myDeleteLink, comissionXML.getfieldTransl("h_delete_bank_comission", false), rset.getString("NAME_COMISSION_TYPE")));
		             html.append(getEditButtonWitDivHTML(myHyperLink, "div_data_detail"));
	             }
	             html.append("</tr>\n");
	         }
	         html.append("<input type=\"hidden\" name=\"paramcount\" value=\""+myCnt+"\">");
	         if (hasEditPermission && (myCnt>0)) {
	        	 if (myCnt>0) {
	        		 html.append("<tr>");
	        		 html.append("<td colspan=\"14\" align=\"center\">");
	        		 html.append(getSubmitButtonAjax("../crm/clients/yurpersonscomissionupdate.jsp"));
	        		 html.append("</td></tr>\n");
	        	 }
	        	 html.append("</form></table>\n");
	         }
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
         Connector.closeConnection(con);
	     return html.toString();
	  } // class getJurPersonComissionHTML

	public String getJurPersonBKAccountsHTML(String pFindString, String pExist, String id_member, String participant_list, String p_beg, String p_end) {
	        StringBuilder html = new StringBuilder();
	        Connection con = null;
	        PreparedStatement st = null;

	        ArrayList<bcFeautureParam> pParam = initParamArray();

	        String mySQL = 
	        	" SELECT rn, cd_bk_account, name_bk_account, " +
                "        internal_name_bk_account, exist_flag_tsl, exist_flag, id_bk_account " +
                "   FROM (SELECT ROWNUM rn, id_bk_account, cd_bk_account, name_bk_account, " +
                "                internal_name_bk_account, exist_flag, exist_flag_tsl " +
                "           FROM (SELECT * " +
                "                   FROM " + getGeneralDBScheme() + ".vc_bk_accounts_member_priv_all "+
                "                  WHERE UPPER(cd_participant) in (" + participant_list + ")" +
                "                    AND id_member = ? ";
	        pParam.add(new bcFeautureParam("string", id_member));
	        
	    	if (!isEmpty(pFindString)) {
	    		mySQL = mySQL + 
	   				" AND (TO_CHAR(id_bk_account) LIKE UPPER('%'||?||'%') OR " +
	   				"      UPPER(cd_bk_account) LIKE UPPER('%'||?||'%') OR " +
	   				"      UPPER(name_bk_account) LIKE UPPER('%'||?||'%') OR " +
	   				"      UPPER(internal_name_bk_account) LIKE UPPER('%'||?||'%'))";
	    		for (int i=0; i<4; i++) {
	    		    pParam.add(new bcFeautureParam("string", pFindString));
	    		}
	    	}
	    	if (!isEmpty(pExist)) {
	    		mySQL = mySQL + " AND exist_flag = ? ";
    		    pParam.add(new bcFeautureParam("string", pExist));
	    	}
	    	pParam.add(new bcFeautureParam("int", p_end));
	    	pParam.add(new bcFeautureParam("int", p_beg));
	        mySQL = mySQL +
                "                  ORDER BY cd_bk_account" +
                "       ) WHERE ROWNUM < ? " + 
                " ) WHERE rn >= ? ";
	        boolean hasBKAccountPermission = false; 
	        
	        String myFont = "";

	        String myBgColor = noneBackGroundStyle;
	        try{
	        	if (isEditMenuPermited("FINANCE_BK_ACCOUNTS")>=0) {
	        		hasBKAccountPermission = true;
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
	            for (int i=1; i <= colCount-3; i++) {
	                html.append(getBottomFrameTableTH(bk_accountXML, mtd.getColumnName(i)));
	            }
	            html.append("</tr></thead><tbody>\n");
	            while (rset.next())
	            {
	          	  	if ("Y".equalsIgnoreCase(rset.getString("EXIST_FLAG"))) {
	                		myFont = "";
	                		myBgColor = noneBackGroundStyle;
	                } else {
	                		myFont = "<font color=\"red\">";
	                		myBgColor = selectedBackGroundStyle;
	                }

	          	  	html.append("<tr>");
	          	  	for (int i=1; i <= colCount-3; i++) {
	          	  		if (hasBKAccountPermission && 
	          	  				("CD_BK_ACCOUNT".equalsIgnoreCase(mtd.getColumnName(i)))) {
	          	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/finance/bk_accountspecs.jsp?id="+rset.getString("ID_BK_ACCOUNT"), myFont, myBgColor));
	         	  		} else {
	          	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", myFont, myBgColor));
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
	} // getJurPersonBKAccountsHTML
	
    public String getRelationShipsHTML(String pFindString, String p_beg, String p_end) {
    	StringBuilder html = new StringBuilder();
    	Connection con = null;
    	PreparedStatement st = null;
	    boolean hasRelationShipPermission = false;
	    boolean hasEditPermission = false;
	    boolean hasJurPrsPermission = false;

	    ArrayList<bcFeautureParam> pParam = initParamArray();

	    String mySQL = 
	    	" SELECT rn, id_club_rel, date_club_rel, name_club_rel_type,  " +
      	  	"        sname_party1_full, sname_party2_full, id_party1, id_party2 " +
      	  	"   FROM (SELECT ROWNUM rn, id_club_rel, name_club_rel_type, date_club_rel_frmt date_club_rel, " +
      	  	"                desc_club_rel, sname_party1 sname_party1_full, " +
      	  	"                sname_party2 sname_party2_full, id_party1, id_party2 " +
           	"           FROM (SELECT * " +
           	"                   FROM " + getGeneralDBScheme() + ".vc_club_rel_club_all "+
           	"                  WHERE (id_party1 = ? " + 
           	"                    OR id_party2 = ? ) ";
	    pParam.add(new bcFeautureParam("int", this.idJurPrs));
	    pParam.add(new bcFeautureParam("int", this.idJurPrs));
	    
    	if (!isEmpty(pFindString)) {
    		mySQL = mySQL + 
   				" AND (TO_CHAR(id_club_rel) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(name_club_rel_type) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(date_club_rel_frmt) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(desc_club_rel) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(sname_party1) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(sname_party2) LIKE UPPER('%'||?||'%'))";
    		for (int i=0; i<6; i++) {
    		    pParam.add(new bcFeautureParam("string", pFindString));
    		}
    	}
    	pParam.add(new bcFeautureParam("int", p_end));
    	pParam.add(new bcFeautureParam("int", p_beg));
	    mySQL = mySQL +
          	"         ) WHERE ROWNUM < ? " + 
          	" ) WHERE rn >= ?";
	    try{
        	if (isEditPermited("CLIENTS_YURPERSONS_RELATIONSHIPS")>0) {
        		hasEditPermission = true;
        	}
	    	if (isEditMenuPermited("CLUB_RELATIONSHIP")>=0) {
	    		hasRelationShipPermission = true;
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
	        	html.append(getBottomFrameTableTH(relationshipXML, mtd.getColumnName(i)));
            }
            if (hasEditPermission) {
                html.append("<th>&nbsp;</th><th>&nbsp;</th>\n");
            }

            html.append("</tr></thead><tbody>\n");
	        
	        while (rset.next()) {
	        	html.append("<tr>\n");
		        for (int i=1; i <= colCount-2; i++) {
	        		if (hasRelationShipPermission && "ID_CLUB_REL".equalsIgnoreCase(mtd.getColumnName(i))) {
	        			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/club/relationshipspecs.jsp?id="+rset.getString(i), "", ""));
         	  		} else if (hasJurPrsPermission && "SNAME_PARTY1_FULL".equalsIgnoreCase(mtd.getColumnName(i)) &&
         	  				!isEmpty(rset.getString("ID_PARTY1")) &&
	      	  				!(this.idJurPrs.equalsIgnoreCase(rset.getString("ID_PARTY1")))) {
	        			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/clients/yurpersonspecs.jsp?id="+rset.getString("ID_PARTY1"), "", ""));
         	  		} else if (hasJurPrsPermission && "SNAME_PARTY2_FULL".equalsIgnoreCase(mtd.getColumnName(i)) &&
         	  				!isEmpty(rset.getString("ID_PARTY2")) &&
	      	  				!(this.idJurPrs.equalsIgnoreCase(rset.getString("ID_PARTY2")))) {
	        			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/clients/yurpersonspecs.jsp?id="+rset.getString("ID_PARTY2"), "", ""));
         	  		} else {
          	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", "", ""));
	        		}
	        	}
                if (hasEditPermission) {
	            	String myHyperLink = "../crm/clients/yurpersonupdate.jsp?type=relationship&id="+this.idJurPrs+"&id_club_rel="+rset.getString("ID_CLUB_REL");
	            	String myDeleteLink = "../crm/clients/yurpersonupdate.jsp?type=relationship&id="+this.idJurPrs+"&id_club_rel="+rset.getString("ID_CLUB_REL")+"&action=remove&process=yes";
	                html.append(getDeleteButtonHTML(myDeleteLink, relationshipXML.getfieldTransl("h_delete_relationship", false), rset.getString("ID_CLUB_REL")));
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
	  } // getRelationShipsHTML
    
    public String getRelationShipsNeededCount() {

    	ArrayList<bcFeautureParam> pParam = initParamArray();
    	pParam.add(new bcFeautureParam("int", this.idJurPrs));
    	pParam.add(new bcFeautureParam("int", this.idJurPrs));

    	return getOneValueByParamId(
    			" SELECT COUNT(*) " +
    			"   FROM " + getGeneralDBScheme()+".vc_club_rel_need_club_all " +
    			"  WHERE id_party1 = ? " + 
	           	"     OR id_party2 = ? ", pParam);
    } // getCardStateName
	
    public String getRelationShipsNeededHTML(String pFindString, String p_beg, String p_end) {
    	StringBuilder html = new StringBuilder();
    	Connection con = null;
    	PreparedStatement st = null;
	    boolean hasRelationShipPermission = false;
	    boolean hasJurPrsPermission = false;

	    ArrayList<bcFeautureParam> pParam = initParamArray();

	    String mySQL =  
		   	" SELECT rn, name_club_rel_type, sname_party1 sname_party1_full, sname_party2 sname_party2_full, id_club_rel, id_party1, id_party2 " +
	      	"   FROM (SELECT ROWNUM rn, sname_party1, sname_party2, name_club_rel_type, id_club_rel, id_party1, id_party2 " +
	      	"   		  FROM (SELECT * " +
	       	"                   FROM " + getGeneralDBScheme()+".vc_club_rel_need_club_all " +
	       	"                  WHERE (id_party1 = ? " + 
	       	"                    OR id_party2 = ? )";
	    pParam.add(new bcFeautureParam("int", this.idJurPrs));
	    pParam.add(new bcFeautureParam("int", this.idJurPrs));
	    
    	if (!isEmpty(pFindString)) {
    		mySQL = mySQL + 
   				" AND (UPPER(name_club_rel_type) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(sname_party1) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(sname_party2) LIKE UPPER('%'||?||'%'))";
    		for (int i=0; i<3; i++) {
    		    pParam.add(new bcFeautureParam("string", pFindString));
    		}
    	}
    	pParam.add(new bcFeautureParam("int", p_end));
    	pParam.add(new bcFeautureParam("int", p_beg));
	    mySQL = mySQL +
	      	"         ) WHERE ROWNUM < ? " +
	       	" ) WHERE rn >= ?";
	    try{
	    	if (isEditMenuPermited("CLUB_RELATIONSHIP")>=0) {
	    		hasRelationShipPermission = true;
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
	        for (int i=1; i <= colCount-3; i++) {
	        	html.append(getBottomFrameTableTH(relationshipXML, mtd.getColumnName(i)));
            }
            html.append("</tr></thead><tbody>\n");
	        
	        while (rset.next()) {
	        	html.append("<tr>\n");
		        for (int i=1; i <= colCount-3; i++) {
	        		if (hasRelationShipPermission && "NAME_CLUB_REL_TYPE".equalsIgnoreCase(mtd.getColumnName(i))) {
	        			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/clients/yurpersonupdate.jsp?type=relationship&action=addneeded&process=no&id="+this.idJurPrs+"&id_club_rel="+rset.getString("id_club_rel"), "", ""));
          	  		} else if ("SNAME_PARTY1_FULL".equalsIgnoreCase(mtd.getColumnName(i)) && hasJurPrsPermission &&
          	  				!isEmpty(rset.getString("ID_PARTY1")) &&
	      	  				!(this.idJurPrs.equalsIgnoreCase(rset.getString("ID_PARTY1")))) {
	          			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/clients/yurpersonspecs.jsp?id=" + rset.getString("ID_PARTY1"), "", ""));
          	  		} else if ("SNAME_PARTY2_FULL".equalsIgnoreCase(mtd.getColumnName(i)) && hasJurPrsPermission &&
          	  				!isEmpty(rset.getString("ID_PARTY2")) &&
	      	  				!(this.idJurPrs.equalsIgnoreCase(rset.getString("ID_PARTY2")))) {
	          			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/clients/yurpersonspecs.jsp?id=" + rset.getString("ID_PARTY2"), "", ""));
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
	  } // getRelationShipsHTML
    
    public String getDeviceTypesHTML(String pFindString, String pTermType, String p_beg, String p_end) {
        StringBuilder html = new StringBuilder();
        Connection con = null;
        PreparedStatement st = null;

        ArrayList<bcFeautureParam> pParam = initParamArray();

        String mySQL = 
        	" SELECT rn, name_term_type, name_device_type, desc_device_type, " +
        	"        term_code_page, work_with_certificate_tsl, id_device_type " +
        	"   FROM (SELECT ROWNUM rn, id_device_type, name_device_type, desc_device_type, " +
        	"                name_term_type, " +
        	"				 DECODE(work_with_certificate, " +
	        "         	 	        'Y', '<font color=\"green\"><b>'||work_with_certificate_tsl||'</b></font>', " +
	        "          	 	        '<font color=\"red\"><b>'||work_with_certificate_tsl||'</b></font>'" +
	        "  		         ) work_with_certificate_tsl, term_code_page " +
        	"           FROM (SELECT * " +
        	"                   FROM " + getGeneralDBScheme() + ".vc_term_device_type_all "+
            "                  WHERE id_jur_prs_manufacturer = ? ";
        pParam.add(new bcFeautureParam("int", this.idJurPrs));
        
    	if (!isEmpty(pFindString)) {
    		mySQL = mySQL + 
   				" AND (TO_CHAR(id_device_type) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(name_device_type) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(desc_device_type) LIKE UPPER('%'||?||'%'))";
    		for (int i=0; i<3; i++) {
    		    pParam.add(new bcFeautureParam("string", pFindString));
    		}
    	}
    	if (!isEmpty(pTermType)) {
    		mySQL = mySQL + " AND cd_term_type = ? ";
    		pParam.add(new bcFeautureParam("string", pTermType));
    	}
    	pParam.add(new bcFeautureParam("int", p_end));
    	pParam.add(new bcFeautureParam("int", p_beg));
        mySQL = mySQL +
            "                  ORDER BY name_device_type " +
            "        ) WHERE ROWNUM < ? " +
            " ) WHERE rn >= ?";
        boolean hasEditPerm = false;
        boolean hasDevicePermission = false;
        try{
        	if (isEditPermited("CLIENTS_YURPERSONS_TERM_DEVICE_TYPES")>0) {
        		hasEditPerm = true;
        	}

        	if (isEditMenuPermited("CLUB_TERM_DEVICE_TYPE")>=0) {
        		hasDevicePermission = true;
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
               html.append(getBottomFrameTableTH(terminalXML, mtd.getColumnName(i)));
            }
            if (hasEditPerm) {
            	html.append("<th>&nbsp;</th><th>&nbsp;</th>\n");
            }
            html.append("</tr></thead><tbody>");
            	
            while (rset.next())
            {
            	html.append("<tr>");
          	  	for (int i=1; i <= colCount-1; i++) {
          	  		if ("NAME_DEVICE_TYPE".equalsIgnoreCase(mtd.getColumnName(i)) && hasDevicePermission) {
          	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/club/term_device_typespecs.jsp?id="+rset.getString("ID_DEVICE_TYPE"), "", ""));
          	  		} else {
          	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", "", ""));
          	  		}
          	  	}
                if (hasEditPerm) {
             	   String myHyperLink = "../crm/clients/yurpersonupdate.jsp?id=" + this.idJurPrs + "&id_device_type=" + rset.getString("ID_DEVICE_TYPE") + "&type=device";
             	   String myDeleteLink = "../crm/clients/yurpersonupdate.jsp?id=" + this.idJurPrs + "&id_device_type=" + rset.getString("ID_DEVICE_TYPE") + "&type=device&action=remove&process=yes";
                   html.append(getDeleteButtonHTML(myDeleteLink, terminalXML.getfieldTransl("h_delete_devict_type", false), rset.getString("NAME_DEVICE_TYPE")));
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
    } // getDeviceTypesHTML

    public String getUserJurPrsPrivilegesHTML(String pFindString, String pSelected, String p_beg, String p_end) {
    	ArrayList<bcFeautureParam> pParam = initParamArray();

    	String mySQL = 
        	" SELECT rn, name_user, name_user_status, " +
        	"        fio_nat_prs, sname_service_place_work, fact_adr_full, phone_mobile, sname_club club, " +
        	"        has_permission, id_club, id_nat_prs, id_service_place_work, id_user, has_permission_tsl " +
        	"   FROM (SELECT ROWNUM rn, sname_club, id_user, name_user, " +
        	"                id_nat_prs, fio_nat_prs, id_service_place_work, sname_service_place_work," +
        	"                fact_adr_full, phone_mobile, name_user_status, has_permission, id_club, has_permission_tsl " +
        	"           FROM (SELECT a.sname_club, a.id_user, a.name_user, a.name_user_status, " +
        	"					     a.id_nat_prs, a.fio_nat_prs, a.id_service_place_work, a.sname_service_place_work, " +
        	"                        a.adr_service_place_work fact_adr_full, a.phone_mobile_user phone_mobile, a.has_permission, a.id_club, a.has_permission_tsl " +
        	"                   FROM " + getGeneralDBScheme() + ".vc_user_jur_prs_priv_all a " +
        	"                  WHERE a.id_jur_prs = ? ";
    	pParam.add(new bcFeautureParam("int", this.idJurPrs));
    	
    	if (!isEmpty(pSelected)) {
    		if ("Y".equalsIgnoreCase(pSelected)) {
        		mySQL = mySQL + " AND has_permission = 'Y' ";
    		} else if ("N".equalsIgnoreCase(pSelected)) {
    			mySQL = mySQL + " AND has_permission = 'N' ";
    		}
    	}
    	if (!isEmpty(pFindString)) {
    		mySQL = mySQL + " AND (UPPER(name_user) LIKE UPPER('%'||?||'%'))";
    		pParam.add(new bcFeautureParam("string", pFindString));
    	}
    	pParam.add(new bcFeautureParam("int", p_end));
    	pParam.add(new bcFeautureParam("int", p_beg));
    	mySQL = mySQL +
        	"                  ORDER BY DECODE(a.has_permission, 'Y',1,2), a.name_user)" + 
        	"          WHERE ROWNUM < ? " + 
        	" ) WHERE rn >= ?";
        StringBuilder html = new StringBuilder();
        Connection con = null;
        PreparedStatement st = null;
        String myFont = "";
        String myFontEnd = "";
        String myBGColor = "";
          
        boolean hasPermission = false;
        boolean hasClubPermission = false;
        boolean hasUserPermission = false;
        boolean hasJurPrsPermission = false;
        boolean hasNatPrsPermission = false;
          
        try{
        	 if (isEditPermited("CLIENTS_YURPERSONS_ACCESS")>0) {
        		 hasPermission = true;
        	 }
        	 if (isEditMenuPermited("CLUB_CLUB")>=0) {
        		 hasClubPermission = true;
        	 }
        	 if (isEditMenuPermited("SECURITY_USERS")>=0) {
        		 hasUserPermission = true;
        	 }
        	 if (isEditMenuPermited("CLIENTS_YURPERSONS")>=0) {
        		 hasJurPrsPermission = true;
        	 }
        	 if (isEditMenuPermited("CLIENTS_NATPERSONS")>=0) {
        		 hasNatPrsPermission = true;
        	 }
        	 
        	 LOGGER.debug(prepareSQLToLog(mySQL, pParam));
        	 con = Connector.getConnection(getSessionId());
             st = con.prepareStatement(mySQL);
             st = prepareParam(st, pParam);
             ResultSet rset = st.executeQuery();
             ResultSetMetaData mtd = rset.getMetaData();
                
             int colCount = mtd.getColumnCount();
             if (hasPermission) {
            	 html.append("<form method=\"post\" name=\"updateForm\" id=\"updateForm\" action=\"../crm/clients/yurpersonupdate.jsp\">\n");
            	 html.append("<input type=\"hidden\" name=\"type\" value=\"access\">\n");
            	 html.append("<input type=\"hidden\" name=\"action\" value=\"set\">\n");
            	 html.append("<input type=\"hidden\" name=\"process\" value=\"yes\">\n");
            	 html.append("<input type=\"hidden\" name=\"id\" value=\"" + this.idJurPrs + "\">\n");
             }
             html.append(getBottomFrameTable());
             html.append("<tr>");
             for (int i=1; i <= colCount-5; i++) {
            	 String colName = mtd.getColumnName(i);                            
                 if ("HAS_PERMISSION".equalsIgnoreCase(colName)) {
                     if (hasPermission) {
                       	 html.append("<th>"+ jurpersonXML.getfieldTransl(colName, false)+
                       		"<br><input id=\"mainCheck\" name=\"mainCheck\" type=\"checkbox\" onclick=\"CheckAll(this,'chb_id')\"></th>\n");
                     } else {
                    	 html.append(getBottomFrameTableTH(userXML, colName));
                     } 
                 } else {
                  	if ("TITLE_PARTNER".equalsIgnoreCase(mtd.getColumnName(i)) ||
                			"PHONE_MOBILE".equalsIgnoreCase(mtd.getColumnName(i)) ||
                			"FACT_ADR_FULL".equalsIgnoreCase(mtd.getColumnName(i))) {
                		html.append(getBottomFrameTableTH(jurpersonXML, mtd.getColumnName(i)));
                	} else {
                		html.append(getBottomFrameTableTH(userXML, mtd.getColumnName(i)));
                	}
                 }
             }
             html.append("</tr></thead><tbody>\n");
             if (hasPermission) {
             	html.append("<tr>");
             	html.append("<td colspan=\"9\" align=\"center\">");
             	html.append(getSubmitButtonAjax("../crm/clients/yurpersonupdate.jsp"));
             	html.append("</td>");
             	html.append("</tr>");
             }

             
             int rowCount = 0;
             while (rset.next()) {
             	rowCount++;
                String jurPrsID="id_"+rset.getString("ID_CLUB")+"_"+rset.getString("ID_USER");
                String tprvCheck="prv_"+jurPrsID;
                String tCheck="chb_"+jurPrsID;
                
                if(rset.getString("HAS_PERMISSION").equalsIgnoreCase("Y")){
                	myFont = "<font color=\"blue\">";
                	myFontEnd = "</font>";
                	myBGColor = selectedBackGroundStyle;
                } else {
                	myFont = "";
                	myFontEnd = "";
                	myBGColor = "";
                }
                html.append("<tr>\n");
                for (int i=1; i <= colCount-6; i++) {
	      	  		if ("NAME_USER".equalsIgnoreCase(mtd.getColumnName(i)) && hasUserPermission) {
	      	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/club/clubspecs.jsp?id="+rset.getString("ID_CLUB"), myFont, myBGColor));
	      	  		} else if ("CLUB".equalsIgnoreCase(mtd.getColumnName(i)) && hasClubPermission) {
	      	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/security/userspecs.jsp?id="+rset.getString("ID_USER"), myFont, myBGColor));
	      	  		} else if ("SNAME_SERVICE_PLACE_WORK".equalsIgnoreCase(mtd.getColumnName(i)) && hasJurPrsPermission) {
	      	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/clients/yurpersonspecs.jsp?id="+rset.getString("ID_SERVICE_PLACE_WORK"), myFont, myBGColor));
	      	  		} else if ("FIO_NAT_PRS".equalsIgnoreCase(mtd.getColumnName(i)) && hasNatPrsPermission) {
	      	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/clients/natpersonspecs.jsp?id="+rset.getString("ID_NAT_PRS"), myFont, myBGColor));
	      	  		} else {
	      	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", myFont, myBGColor));
	      	  		}
                }
                 
                /*html.append("<tr>\n" +
                    	"<td " + myBGColor + " align=\"center\">" + myFont + rset.getString(1) + myFontEnd + "</td>\n");
                if (hasClubPermission) {
      	  			html.append(getBottomFrameTableTD("CLUB", rset.getString("CLUB"), "../crm/club/clubspecs.jsp?id="+rset.getString("ID_CLUB"), myFont, myBGColor));
     	  		} else {
     	  			html.append("<td " + myBGColor + ">" + myFont + rset.getString(2) + myFontEnd + "</td>\n");
     	  		}
                if (hasUserPermission) {
      	  			html.append(getBottomFrameTableTD("NAME_USER", rset.getString("NAME_USER"), "../crm/security/userspecs.jsp?id="+rset.getString("ID_USER"), myFont, myBGColor));
     	  		} else {
     	  			html.append("<td " + myBGColor + ">" + myFont + rset.getString("NAME_USER") + myFontEnd + "</td>\n");
     	  		}
                html.append("<td " + myBGColor + ">" + myFont + rset.getString(4) + myFontEnd + "</td>\n");
                */
                if (hasPermission) {
	                if(rset.getString("HAS_PERMISSION").equalsIgnoreCase("Y")){
	                  	html.append("<td " + myBGColor + " align=\"center\"><INPUT  type=\"checkbox\" style=\"height: inherit !important;\" value = \"Y\" name="+tCheck+" id="+tCheck+" checked onclick=\"return CheckCB(this);\"></td>\n");
	                  	html.append("<INPUT type=hidden  value=\"Y\" name="+tprvCheck+">");
	                } else { 
	                    html.append("<td " + myBGColor + " align=\"center\"><INPUT  type=\"checkbox\" style=\"height: inherit !important;\" value = \"Y\" name="+tCheck+" id="+tCheck+" onclick=\"return CheckCB(this);\"></td>\n");
	                }
                } else {
                	html.append("<td " + myBGColor + ">" + myFont + rset.getString("HAS_PERMISSION_TSL") + myFontEnd + "</td>\n");
                }
                        
                html.append("<INPUT type=hidden  value="+rset.getString("ID_USER")+" name="+jurPrsID+">");
                html.append("</tr> ");        
            }

            if (hasPermission) {
            	html.append("<tr>");
            	html.append("<td colspan=\"9\" align=\"center\">");
            	html.append(getSubmitButtonAjax("../crm/clients/yurpersonupdate.jsp"));
            	html.append("</td>");
            	html.append("</tr>");
            }
            html.append("<input type=hidden value="+rowCount+" name=rowCount>");
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
    } //getUserJurPrsPrivilegesHTML

    public String getJurPersonNomenklaturesHTML(String pFindString, String p_beg, String p_end) {
        StringBuilder html = new StringBuilder();
        Connection con = null;
        PreparedStatement st = null;

        ArrayList<bcFeautureParam> pParam = initParamArray();

        String mySQL = 
        	" SELECT rn, cd_jur_prs_nomenkl, name_jur_prs_nomenkl, " +
        	"        name_nomenkl_unit, begin_action_date_frmt, end_action_date_frmt, id_jur_prs_nomenkl " +
        	"   FROM (SELECT ROWNUM rn, id_jur_prs_nomenkl, cd_jur_prs_nomenkl, name_jur_prs_nomenkl, " +
        	"                name_nomenkl_unit, begin_action_date_frmt, end_action_date_frmt " +
        	"           FROM (SELECT id_jur_prs_nomenkl, cd_jur_prs_nomenkl, name_jur_prs_nomenkl, " +
        	"                        name_nomenkl_unit, begin_action_date_frmt, end_action_date_frmt " +
        	"                   FROM " + getGeneralDBScheme() + ".vc_jur_prs_nomenkl_club_all "+
            "                  WHERE id_jur_prs = ? ";
        pParam.add(new bcFeautureParam("int", this.idJurPrs));
        
    	if (!isEmpty(pFindString)) {
    		mySQL = mySQL + 
   				" AND (TO_CHAR(id_jur_prs_nomenkl) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(cd_jur_prs_nomenkl) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(name_jur_prs_nomenkl) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(name_nomenkl_unit) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(begin_action_date_frmt) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(end_action_date_frmt) LIKE UPPER('%'||?||'%'))";
    		for (int i=0; i<6; i++) {
    		    pParam.add(new bcFeautureParam("string", pFindString));
    		}
    	}
    	pParam.add(new bcFeautureParam("int", p_end));
    	pParam.add(new bcFeautureParam("int", p_beg));
        mySQL = mySQL +
            "                  ORDER BY cd_jur_prs_nomenkl) " + 
            "          WHERE ROWNUM < ? " + 
            "  ) WHERE rn >= ?";
        boolean hasPermission = false;
        try{
        	if (isEditPermited("CLIENTS_YURPERSONS_NOMENKLATURE")>0) {
        		hasPermission = true;
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
                html.append(getBottomFrameTableTH(jurpersonXML, mtd.getColumnName(i)));
            }
            if (hasPermission) {
            	html.append("<th>&nbsp;</th><th>&nbsp;</th>\n");
            }
            html.append("</tr></thead><tbody>\n");
            while (rset.next())
            {
          	  	html.append("<tr>");
          	  	for (int i=1; i <= colCount-1; i++) {
          	  		html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", "", ""));
          	  	}
                if (hasPermission) {
	            	String myHyperLink = "../crm/clients/yurpersonupdate.jsp?type=nomenkl&id="+this.idJurPrs+"&id_jur_prs_nomenkl="+rset.getString("ID_JUR_PRS_NOMENKL");
	            	String myDeleteLink = "../crm/clients/yurpersonupdate.jsp?type=nomenkl&id="+this.idJurPrs+"&id_jur_prs_nomenkl="+rset.getString("ID_JUR_PRS_NOMENKL")+"&action=remove&process=yes";
	                html.append(getDeleteButtonHTML(myDeleteLink, jurpersonXML.getfieldTransl("h_delete_nomenkl", false), rset.getString("NAME_JUR_PRS_NOMENKL")));
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
    } //getJurPersonWorkersHTML

    public String getLogisticHTML(String pFindString, String p_beg, String p_end) {
        StringBuilder html = new StringBuilder();

        Connection con = null;
        PreparedStatement st = null;

        ArrayList<bcFeautureParam> pParam = initParamArray();

        String mySQL = 
        	"SELECT rn, name_lg_type, action_date_frmt, operation_desc, " +
        	"       sname_jur_prs_receiver,  sname_jur_prs_sender, object_count, cd_lg_type, " +
        	"       id_jur_prs_receiver, id_jur_prs_sender, id_lg_record " + 
        	"  FROM (SELECT ROWNUM rn, id_lg_record, name_lg_type, operation_desc, object_count, " + 
        	"               sname_jur_prs_receiver, sname_jur_prs_sender, action_date_frmt, cd_lg_type, " +
        	"               id_jur_prs_receiver, id_jur_prs_sender " +
        	"		   FROM (SELECT * " +
        	"                  FROM " + getGeneralDBScheme() + ".vc_lg_club_all " +
        	"                 WHERE (id_jur_prs_receiver = ? " + 
        	"                    OR id_jur_prs_sender = ?) ";
        pParam.add(new bcFeautureParam("int", this.idJurPrs));
        pParam.add(new bcFeautureParam("int", this.idJurPrs));
        
    	if (!isEmpty(pFindString)) {
    		mySQL = mySQL + 
   				" AND (TO_CHAR(id_lg_record) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(name_lg_type) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(action_date_frmt) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(operation_desc) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(sname_jur_prs_receiver) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(sname_jur_prs_sender) LIKE UPPER('%'||?||'%'))";
    		for (int i=0; i<6; i++) {
    		    pParam.add(new bcFeautureParam("string", pFindString));
    		}
    	}
    	pParam.add(new bcFeautureParam("int", p_end));
    	pParam.add(new bcFeautureParam("int", p_beg));
        mySQL = mySQL +
            "               ORDER BY action_date DESC) " +
            "         WHERE ROWNUM < ?)" + 
            "  WHERE rn >= ?";
        
        boolean hasBonCardLogisticPermission = false;
        boolean hasQuestionnaireLogisticPermission = false;
        boolean hasTerminalLogisticPermission = false;
        boolean hasSAMLogisticPermission = false;
        boolean hasGiftLogisticPermission = false;
        boolean hasOtherLogisticPermission = false;
        
        try{
        	if (isEditMenuPermited("LOGISTIC_PARTNERS_CARDS")>=0) {
        		hasBonCardLogisticPermission = true;
        	}
        	if (isEditMenuPermited("LOGISTIC_PARTNERS_QUEST")>=0) {
        		hasQuestionnaireLogisticPermission = true;
        	}
        	if (isEditMenuPermited("LOGISTIC_PARTNERS_TERMINALS")>=0) {
        		hasTerminalLogisticPermission = true;
        	}
        	if (isEditMenuPermited("LOGISTIC_PARTNERS_SAMS")>=0) {
        		hasSAMLogisticPermission = true;
        	}
        	if (isEditMenuPermited("CLUB_EVENT_WAREHOUSE")>=0) {
        		hasGiftLogisticPermission = true;
        	}
        	if (isEditMenuPermited("LOGISTIC_PARTNERS_OTHERS")>=0) {
        		hasOtherLogisticPermission = true;
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
            for (int i=1; i <= colCount-4; i++) {
                html.append(getBottomFrameTableTH(logisticXML, mtd.getColumnName(i)));
            }
            html.append("</tr></thead><tbody>\n");
            while (rset.next())
            {
          	  	html.append("<tr>");
          	  	
          	  	for (int i=1; i <= colCount-4; i++) {
	          	  	if ("NAME_LG_TYPE".equalsIgnoreCase(mtd.getColumnName(i)) || 
	          	  		"ACTION_DATE_FRMT".equalsIgnoreCase(mtd.getColumnName(i))) {
	                    if ("BON_CARD".equalsIgnoreCase(rset.getString("CD_LG_TYPE")) && hasBonCardLogisticPermission) {
	                    	html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/logistic/partners/cardspecs.jsp?id="+rset.getString("ID_LG_RECORD"), "", ""));
	                    } else if ("QUESTIONNAIRE".equalsIgnoreCase(rset.getString("CD_LG_TYPE")) && hasQuestionnaireLogisticPermission) {
	                    	html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/logistic/partners/questspecs.jsp?id="+rset.getString("ID_LG_RECORD"), "", ""));
	                    } else if ("TERMINAL".equalsIgnoreCase(rset.getString("CD_LG_TYPE")) && hasTerminalLogisticPermission) {
	                    	html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/logistic/partners/terminalspecs.jsp?id="+rset.getString("ID_LG_RECORD"), "", ""));
	                    } else if ("SAM".equalsIgnoreCase(rset.getString("CD_LG_TYPE")) && hasSAMLogisticPermission) {
	                    	html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/logistic/partners/samspecs.jsp?id="+rset.getString("ID_LG_RECORD"), "", ""));
	                    } else if ("GIFT".equalsIgnoreCase(rset.getString("CD_LG_TYPE")) && hasGiftLogisticPermission) {
	                    	html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/club_event/warehousespecs.jsp?id="+rset.getString("ID_LG_RECORD"), "", ""));
	                    } else if ("OTHER".equalsIgnoreCase(rset.getString("CD_LG_TYPE")) && hasOtherLogisticPermission) {
	                    	html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/logistic/partners/otherspecs.jsp?id="+rset.getString("ID_LG_RECORD"), "", ""));
	                    } else {
	                    	html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", "", ""));
	                    }
	      	  		} else if ("SNAME_JUR_PRS_RECEIVER".equalsIgnoreCase(mtd.getColumnName(i)) &&
	      	  				!isEmpty(rset.getString("ID_JUR_PRS_RECEIVER")) &&
	      	  				!(this.idJurPrs.equalsIgnoreCase(rset.getString("ID_JUR_PRS_RECEIVER")))) {
	                    html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/clients/yurpersonspecs.jsp?id="+rset.getString("ID_JUR_PRS_RECEIVER"), "", ""));
	      	  		} else if ("SNAME_JUR_PRS_SENDER".equalsIgnoreCase(mtd.getColumnName(i)) &&
	      	  				!isEmpty(rset.getString("ID_JUR_PRS_SENDER")) &&
	      	  				!(this.idJurPrs.equalsIgnoreCase(rset.getString("ID_JUR_PRS_SENDER")))) {
	      	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/clients/yurpersonspecs.jsp?id="+rset.getString("ID_JUR_PRS_SENDER"), "", ""));
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
    } //getJurPersonWorkersHTML
	
    public String getDocumentsListHTML(String pFindString, String pType, String pState, String pClubRelType, String p_beg, String p_end, String pTabName, String pEditHiperLink) {
    	bcListDocument list = new bcListDocument();
    	
    	String pWhereCause =  
    		"                  WHERE (id_jur_prs_party1 = ? " + 
   			"                     OR id_jur_prs_party2 = ? " + 
   			"                     OR id_jur_prs_party3 = ?) ";
    	
    	ArrayList<bcFeautureParam> pWhereValue = new ArrayList<bcFeautureParam>();
    	pWhereValue.add(new bcFeautureParam("int", this.idJurPrs));
    	pWhereValue.add(new bcFeautureParam("int", this.idJurPrs));
    	pWhereValue.add(new bcFeautureParam("int", this.idJurPrs));
    	String myDeleteLink = "";
    	String myEditLink = "";
    	String myCopyLink = "";
    	if (isEditPermited("CLIENTS_YURPERSONS_DOCUMENTS")>0) {
    		myDeleteLink = "../crm/club/documentupdate.jsp?back_type=PARTNER&type=general&id_jur_prs="+this.idJurPrs+"&action=remove&process=yes";
    	    myEditLink = "../crm/club/documentupdate.jsp?back_type=PARTNER&type=general&id_jur_prs="+this.idJurPrs;
    	}
    	
    	return list.getDocumentsHTML(pWhereCause, pWhereValue, pFindString, pType, pClubRelType, pState, myEditLink, myCopyLink, myDeleteLink, p_beg, p_end);
    	
    }
	
    public String getContactListHTML(String pFindString, String pPost, String p_beg, String p_end, String pTabName, String pEditHiperLink) {
        StringBuilder html = new StringBuilder();
        PreparedStatement st = null;
        Connection con = null;

        ArrayList<bcFeautureParam> pParam = initParamArray();
        
        String sNameJurPrsField = "";
        String idJurPrsField = "";
        int jurPrsFieldCount = 0;
        if (!"SERVICE_PLACE".equalsIgnoreCase(this.getValue("CD_JUR_PRS_STATUS"))) {
        	sNameJurPrsField = " sname_service_place_work, ";
        	idJurPrsField = " id_service_place_work, ";
        	jurPrsFieldCount = 1;
        }

        String mySQL = 
        	" SELECT rn, " + sNameJurPrsField + " name_contact_prs, name_post, cd_card1, phone_mobile, phone_work, email_work, " +
        	"        id_nat_prs_role, " + idJurPrsField + " card_serial_number, card_id_issuer, card_id_payment_system  "+
            "   FROM (SELECT ROWNUM rn, " + sNameJurPrsField + " name_contact_prs, name_post, phone_mobile, phone_work, email_work, cd_card1, " +
        	"                id_nat_prs_role, id_nat_prs, " + idJurPrsField + " card_serial_number, card_id_issuer, card_id_payment_system "+
            "           FROM (SELECT a.* " +
            "                   FROM " + getGeneralDBScheme() + ".VC_CONTACT_PRS_PRIV_ALL a," +
            "                        (SELECT id_jur_prs " +
            "                           FROM " + getGeneralDBScheme() + ".v_jur_prs_name_all" +
            "                          START WITH id_jur_prs = ?" +
            "                          CONNECT BY PRIOR id_jur_prs = id_jur_prs_parent) b " + 
            "                  WHERE a.id_service_place_work = b.id_jur_prs ";
        pParam.add(new bcFeautureParam("int", this.idJurPrs));
        
    	if (!isEmpty(pFindString)) {
    		mySQL = mySQL + 
   				" AND (UPPER(name_contact_prs) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(phone_mobile) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(phone_work) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(email_work) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(cd_card1) LIKE UPPER('%'||?||'%'))";
    		for (int i=0; i<5; i++) {
    		    pParam.add(new bcFeautureParam("string", pFindString));
    		}
    	}
    	if (!isEmpty(pPost)) {
    		mySQL = mySQL + " AND cd_post = ? ";
    		pParam.add(new bcFeautureParam("string", pPost));
    	}
    	pParam.add(new bcFeautureParam("int", p_end));
    	pParam.add(new bcFeautureParam("int", p_beg));
        mySQL = mySQL +
            "         ORDER BY name_contact_prs) " +
            "      WHERE ROWNUM < ? " + 
            " ) WHERE rn >= ?";
        
        boolean hasContactPermission = false;
        boolean hasCardPermission = false;
        boolean hasEditPermission = false;
        try{
        	if (isEditPermited(pTabName)>0) {
        		hasEditPermission = true;
        	}
        	if (isEditMenuPermited("CLIENTS_CONTACT_PRS")>=0) {
        		hasContactPermission = true;
        	}
        	if (isEditMenuPermited("CARDS_CLUBCARDS")>=0) {
        		hasCardPermission = true;
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
            for (int i=1; i <= colCount-(5+jurPrsFieldCount); i++) {
            	if ("SNAME_SERVICE_PLACE_WORK".equalsIgnoreCase(mtd.getColumnName(i)) ||
            			"NAME_CONTACT_PRS".equalsIgnoreCase(mtd.getColumnName(i)) ||
            			"NAME_POST".equalsIgnoreCase(mtd.getColumnName(i)) ||
            			"PHONE_MOBILE".equalsIgnoreCase(mtd.getColumnName(i)) ||
            			"PHONE_WORK".equalsIgnoreCase(mtd.getColumnName(i)) ||
            			"EMAIL_WORK".equalsIgnoreCase(mtd.getColumnName(i))) {
            		html.append(getBottomFrameTableTH(contactXML, mtd.getColumnName(i)));
            	} else {
            		html.append(getBottomFrameTableTH(userXML, mtd.getColumnName(i)));
            	}
            }
            if (hasEditPermission) {
            	html.append("<th>&nbsp;</th><th>&nbsp;</th>\n");
            }
            html.append("</tr></thead><tbody>\n");
            while (rset.next())
            {
            	html.append("<tr>");
          	  	for (int i=1; i <= colCount-(5+jurPrsFieldCount); i++) {
          	  		if (("SNAME_SERVICE_PLACE_WORK".equalsIgnoreCase(mtd.getColumnName(i))) && hasContactPermission) {
          	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/clients/yurpersonspecs.jsp?id=" + rset.getString("ID_SERVICE_PLACE_WORK"), "", ""));
          	  		} else if ("NAME_CONTACT_PRS".equalsIgnoreCase(mtd.getColumnName(i))) {
          	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/clients/contact_prsspecs.jsp?id=" + rset.getString("ID_NAT_PRS_ROLE"), "", ""));
          	  		} else if (hasCardPermission && "CD_CARD1".equalsIgnoreCase(mtd.getColumnName(i))) {
          	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/cards/clubcardspecs.jsp?id="+rset.getString("CARD_SERIAL_NUMBER")+"&iss="+rset.getString("CARD_ID_ISSUER")+"&paysys="+rset.getString("CARD_ID_PAYMENT_SYSTEM"), "", ""));
          	  		} else {
          	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", "", ""));
          	  		}
          	  	}
                if (hasEditPermission) {
                  	String myHyperLink = pEditHiperLink + "&id_contact_prs=" + rset.getString("ID_NAT_PRS_ROLE") + "&type=general";
                  	String myDeleteLink = pEditHiperLink + "&id_contact_prs=" + rset.getString("ID_NAT_PRS_ROLE") + "&type=general&action=remove&process=yes";
                	html.append(getDeleteButtonHTML(myDeleteLink, contactXML.getfieldTransl("l_remove_contact", false), rset.getString("NAME_CONTACT_PRS")));
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
    } // getContactListHTML 

    public String getCardPackagesHTML(String pFindString, String pCardStatus, String p_beg, String p_end) {
    	bcListCardPackage list = new bcListCardPackage();
    	
    	String pWhereCause = " ," +
            "                        (SELECT id_jur_prs id_jur_prs_all " +
            "                           FROM " + getGeneralDBScheme() + ".v_jur_prs_name_all" +
            "                          START WITH id_jur_prs = ?" +
            "                          CONNECT BY PRIOR id_jur_prs = id_jur_prs_parent) b " + 
            "                  WHERE a.id_jur_prs = b.id_jur_prs_all ";
    	
    	ArrayList<bcFeautureParam> pWhereValue = new ArrayList<bcFeautureParam>();
    	pWhereValue.add(new bcFeautureParam("int", this.idJurPrs));
    	String myDeleteLink = "";
    	String myEditLink = "";
    	String myCopyLink = "";
    	if (isEditPermited("CLIENTS_YURPERSONS_CARD_PACKAGE")>0) {
    		myDeleteLink = "../crm/club/card_packageupdate.jsp?back_type=PARTNER&type=general&id="+this.idJurPrs+"&action=remove&process=yes";
    	    myEditLink = "../crm/club/card_packageupdate.jsp?back_type=PARTNER&type=general&id="+this.idJurPrs;
    	    myCopyLink = "../crm/club/card_packageupdate.jsp?back_type=PARTNER&type=general&id="+this.idJurPrs;
    	}
    	
    	return list.getCardPackagesHTML(pWhereCause, pWhereValue, "", pFindString, pCardStatus, myEditLink, myCopyLink, myDeleteLink, p_beg, p_end);
    	
    }

    public String getReferralSchemeHTML(String pFindString, String pSchemeType, String pCalcType, String p_beg, String p_end) {
    	bcListReferralScheme list = new bcListReferralScheme();
    	
    	String pWhereCause = " WHERE (id_jur_prs = ? OR id_referral_scheme IN " +
            "                         (SELECT id_referral_scheme " +
            "                            FROM " + getGeneralDBScheme()+".vc_target_prg_club_all " +
            "                           WHERE id_jur_prs = ?" +
            "                         )" +
            "                        ) ";
    	
    	ArrayList<bcFeautureParam> pWhereValue = new ArrayList<bcFeautureParam>();
    	pWhereValue.add(new bcFeautureParam("int", this.idJurPrs));
    	pWhereValue.add(new bcFeautureParam("int", this.idJurPrs));
    	String myDeleteLink = "";
    	String myEditLink = "";
    	String myCopyLink = "";
    	if (isEditPermited("CLIENTS_YURPERSONS_REFERRAL_SCHEME")>0) {
    		myDeleteLink = "../crm/club/referral_schemeupdate.jsp?back_type=PARTNER&type=general&id="+this.idJurPrs+"&action=remove&process=yes";
    	    myEditLink = "../crm/club/referral_schemeupdate.jsp?back_type=PARTNER&type=general&id="+this.idJurPrs;
    	    myCopyLink = "../crm/club/referral_schemeupdate.jsp?back_type=PARTNER&type=general&id="+this.idJurPrs;
    	}
    	
    	return list.getReferralSchemesHTML(pWhereCause, pWhereValue, pFindString, pSchemeType, pCalcType, myEditLink, myCopyLink, myDeleteLink, p_beg, p_end);
    	
    }

    public String getLoyalitySchemeHTML(String pFindString, String pCdKindLoyality, String p_beg, String p_end) {
    	bcListLoyalityScheme list = new bcListLoyalityScheme();
    	
    	String pWhereCause = " WHERE id_jur_prs = ? ";
    	
    	ArrayList<bcFeautureParam> pWhereValue = new ArrayList<bcFeautureParam>();
    	pWhereValue.add(new bcFeautureParam("int", this.idJurPrs));
    	String myDeleteLink = "";
    	String myEditLink = "";
    	String myCopyLink = "";
    	if (isEditPermited("CLIENTS_YURPERSONS_REFERRAL_SCHEME")>0) {
    		myDeleteLink = "../crm/clients/loyupdate.jsp?back_type=PARTNER&type=general&id="+this.idJurPrs+"&action=remove&process=yes";
    	    myEditLink = "../crm/clients/loyupdate.jsp?back_type=PARTNER&type=general&id="+this.idJurPrs;
    	    myCopyLink = "../crm/clients/loyupdate.jsp?back_type=PARTNER&type=general&id="+this.idJurPrs;
    	}
    	
    	return list.getLoyalitySchemesHTML(pWhereCause, pWhereValue, "PARTNER", pFindString, pCdKindLoyality, myEditLink, myCopyLink, myDeleteLink, p_beg, p_end);
    	
    }

    public String getTargetProgramsHTML(String pFindString, String pPayPeriod, String p_beg, String p_end) {
    	bcListTargetProgram list = new bcListTargetProgram();
    	
    	String pWhereCause = " WHERE id_jur_prs = ? ";
    	
    	ArrayList<bcFeautureParam> pWhereValue = new ArrayList<bcFeautureParam>();
    	pWhereValue.add(new bcFeautureParam("int", this.idJurPrs));
    	String myDeleteLink = "";
    	String myEditLink = "";
    	String myCopyLink = "";
    	if (isEditPermited("CLIENTS_YURPERSONS_TARGET_PROGRAMS")>0) {
    		myDeleteLink = "../crm/club/target_programupdate.jsp?back_type=PARTNER&type=general&id="+this.idJurPrs+"&action=remove&process=yes";
    	    myEditLink = "../crm/club/target_programupdate.jsp?back_type=PARTNER&type=general&id="+this.idJurPrs;
    	    myCopyLink = "../crm/club/target_programupdate.jsp?back_type=PARTNER&type=general&id="+this.idJurPrs;
    	}
    	
    	return list.getTargetProgramsHTML(pWhereCause, pWhereValue, "PARTNER", pFindString, pPayPeriod, myEditLink, myCopyLink, myDeleteLink, p_beg, p_end);
    	
    }

    public String getSystemRolesHTML(String pFindString, String pCdModuleType, String p_beg, String p_end) {
    	bcListSystemRole list = new bcListSystemRole();
    	
    	String pWhereCause = " WHERE id_jur_prs = ? ";
    	
    	ArrayList<bcFeautureParam> pWhereValue = new ArrayList<bcFeautureParam>();
    	pWhereValue.add(new bcFeautureParam("int", this.idJurPrs));
    	String myDeleteLink = "";
    	String myEditLink = "";
    	String myCopyLink = "";
    	if (isEditPermited("CLIENTS_YURPERSONS_ACCESS_ROLES")>0) {
    		myDeleteLink = "../crm/security/rolesupdate.jsp?back_type=PARTNER&type=general&id="+this.idJurPrs+"&action=remove&process=yes";
    	    myEditLink = "../crm/security/rolesupdate.jsp?back_type=PARTNER&type=general&id="+this.idJurPrs;
    	    myCopyLink = "../crm/security/rolesupdate.jsp?back_type=PARTNER&type=general&id="+this.idJurPrs;
    	}
    	
    	return list.getSystemRolesHTML(pWhereCause, pWhereValue, "PARTNER", pFindString, pCdModuleType, myEditLink, myCopyLink, myDeleteLink, p_beg, p_end);
    	
    }

    public String getTransactionsHTML(String pFindString, String pTypeTrans, String pPayType, String pStateTrans, String p_beg, String p_end) {
    	bcListTransaction list = new bcListTransaction();
    	
    	String pWhereCause = " WHERE (id_dealer = ? OR id_service_place = ?) ";
    	
    	ArrayList<bcFeautureParam> pWhereValue = new ArrayList<bcFeautureParam>();
    	pWhereValue.add(new bcFeautureParam("int", this.idJurPrs));
    	pWhereValue.add(new bcFeautureParam("int", this.idJurPrs));
    	
    	return list.getTransactionList(pWhereCause, pWhereValue, pFindString, pTypeTrans, pPayType, pStateTrans, p_beg, p_end);
    	
    }
	    
}
