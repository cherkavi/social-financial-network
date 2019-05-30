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
import bc.lists.bcListCardOperation;
import bc.service.bcFeautureParam;

public class bcTransactionObject extends bcObject {
	private final static Logger LOGGER=Logger.getLogger(bcTransactionObject.class);
	private Map<String,Comparable<String>> fieldHm = new HashMap<String, Comparable<String>>();
	
	private String idTrans;
	
	public bcTransactionObject(String pIdTrans) {
		this.idTrans = pIdTrans;
		getFeature();
	}
	
	private void getFeature() {
		String featureSelect = " SELECT * FROM " + getGeneralDBScheme() + ".VC_TRANS_OK_CLUB_ALL WHERE id_trans = ?";
		fieldHm = getFeatures2(featureSelect, this.idTrans, false);
	}
	
	public String getValue(String pColumnName) {
		return getFeautureResult(fieldHm, pColumnName);
	}
	
    public String getNomenklatureHTML() {
        StringBuilder html = new StringBuilder();
        Connection con = null;
        PreparedStatement st = null;

        ArrayList<bcFeautureParam> pParam = initParamArray();

        String mySQL = 	
        	" SELECT name_jur_prs_nomenkl, value_nomenkl_frmt, sname_nomenkl_unit, cheque_number "+
        	"  FROM " + getGeneralDBScheme() + ".vc_rec_payment_ext_nomenkl_all c " +
        	" WHERE id_trans = ? " + 
        	" ORDER BY name_jur_prs_nomenkl, value_nomenkl_frmt";
        pParam.add(new bcFeautureParam("int", this.idTrans));
        int cnt = 0;
        try{
        	
        	LOGGER.debug(prepareSQLToLog(mySQL, pParam));
        	con = Connector.getConnection(getSessionId());
        	st = con.prepareStatement(mySQL);
        	st = prepareParam(st, pParam);
        	ResultSet rset = st.executeQuery();

            while (rset.next()) {
            	cnt = cnt + 1;
            	if (cnt > 1) {
            		html.append("<br>");
            	}
            	html.append("<font color=\"green\"><b>" + rset.getString("NAME_JUR_PRS_NOMENKL") + "</b></font>;&nbsp;" + 
            				"<font color=\"red\"><b>" + rset.getString("VALUE_NOMENKL_FRMT") + "&nbsp;" + rset.getString("SNAME_NOMENKL_UNIT") + "</b></font>;&nbsp;" + 
            				"<font color=\"blue\"><b>" + rset.getString("CHEQUE_NUMBER") + "</b></font>");
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
        return html.toString();
    } //getTransactionsTelegramsHTML
	
    public String getTransactionsTelegramsHTML() {
        StringBuilder html = new StringBuilder();
        Connection con = null;
        PreparedStatement st = null;

        ArrayList<bcFeautureParam> pParam = initParamArray();

        String mySQL = 	" SELECT id_term_ses, id_telgr, date_telgr_frmt date_telgr, cd_telgr_type, tel_identifier, " +
        				"       tel_length, nt_msg_b, tel_version, vk_enc, id_sam, "+
        				"       nt_sam, id_term, name_telgr_state "+
        				"   FROM " + getGeneralDBScheme() + ".vc_telgr_club_all c WHERE id_telgr IN " +
        				" (SELECT id_telgr FROM " + getGeneralDBScheme() + ".vc_trans_ok_club_all WHERE id_trans = ?)";
        pParam.add(new bcFeautureParam("int", this.idTrans));
        boolean hasTermSesPermission = false;
        boolean hasTermPermission = false;
        boolean hasSAMPermission = false;
        try{
        	if (isEditMenuPermited("CLIENTS_TERMSES")>=0) {
        		hasTermSesPermission = true;
        	}
        	if (isEditMenuPermited("clients_terminals")>=0) {
        		hasTermPermission = true;
        	}
        	if (isEditMenuPermited("CLIENTS_SAM")>=0) {
        		hasSAMPermission = true;
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
            for (int i=1; i <= colCount; i++) {
            	html.append(getBottomFrameTableTH(telegramXML, mtd.getColumnName(i)));
            }
            html.append("</tr></thead><tbody>");
            while (rset.next())
            {
            	html.append("<tr>");
          	  	for (int i=1; i <= colCount; i++) {
          	  		if (hasTermSesPermission && "ID_TERM_SES".equalsIgnoreCase(mtd.getColumnName(i))) {
          	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/clients/termsespecs.jsp?id="+rset.getString(i), "", ""));
          	  		} else if (hasSAMPermission && "ID_SAM".equalsIgnoreCase(mtd.getColumnName(i))) {
          	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/clients/samspecs.jsp?id="+rset.getString(i), "", ""));
          	  		} else if (hasTermPermission && "ID_TERM".equalsIgnoreCase(mtd.getColumnName(i))) {
          	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/clients/terminalspecs.jsp?id="+rset.getString(i), "", ""));
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
    } //getTransactionsTelegramsHTML

    public String getTransactionsPostingsHTML(String pFind, String idBkOperationType, String p_beg, String p_end) {
        StringBuilder html = new StringBuilder();
        Connection con = null;
        PreparedStatement st = null;

        ArrayList<bcFeautureParam> pParam = initParamArray();

        String mySQL = 	
        	" SELECT rn, id_posting_detail, operation_date_frmt operation_date," +
        	"        oper_number id_bk_operation_scheme_line, " +
        	"        debet_cd_bk_account||' '||debet_name_bk_account debet_id_bk_account,  " +
        	"        credit_cd_bk_account||' '||credit_name_bk_account credit_id_bk_account, " +
        	"		 name_currency, entered_amount_frmt entered_amount, assignment_posting, " +
        	"        rejection_state, id_clearing_line, " +
        	"        debet_id_bk_account debet_id_bk_account2, debet_cd_bk_account, debet_name_bk_account, " +
        	"        credit_id_bk_account credit_id_bk_account2, credit_cd_bk_account, credit_name_bk_account, " +
        	"        id_bk_operation_scheme_line id_bk_operation_scheme_line2, id_posting_line " +
        	"  FROM (SELECT ROWNUM rn, id_posting_detail, id_posting_line, operation_date_frmt, name_currency," +
        	"               id_bk_operation_scheme_line, oper_number, " +
        	"		        entered_amount_frmt, assignment_posting, " +
        	"               rejection_state_tsl rejection_state, id_clearing_line, " +
        	"               debet_id_bk_account, debet_cd_bk_account_frmt debet_cd_bk_account, debet_name_bk_account, " +
        	"               credit_id_bk_account, credit_cd_bk_account_frmt credit_cd_bk_account, credit_name_bk_account " +
        	"          FROM (SELECT * " +
        	"                  FROM "+getGeneralDBScheme() + ".vc_acc_posting_detail_club_all " +
        	"                 WHERE id_trans = ? ";
        pParam.add(new bcFeautureParam("int", this.idTrans));
        
        if (!isEmpty(idBkOperationType)) {
           	mySQL = mySQL + " AND id_bk_operation_scheme_line IN (" +
           		" SELECT id_bk_operation_scheme_line " +
           		"   FROM "+getGeneralDBScheme() + ".vc_oper_scheme_lines_club_all " +
           		"  WHERE cd_bk_operation_type = ?) ";
           	pParam.add(new bcFeautureParam("string", idBkOperationType));
        }
       	if (!isEmpty(pFind)) {
       		mySQL = mySQL + 
   				" AND (TO_CHAR(id_posting_detail) LIKE UPPER('%'||?||'%') OR " +
   				"  UPPER(debet_cd_bk_account) LIKE UPPER('%'||?||'%') OR " +
        		"  UPPER(credit_cd_bk_account) LIKE UPPER('%'||?||'%') OR " +
        		"  UPPER(oper_number) LIKE UPPER('%'||?||'%') OR " +
         		"  UPPER(assignment_posting) LIKE UPPER('%'||?||'%') OR " +
           		"  UPPER(operation_date_frmt) LIKE UPPER('%'||?||'%') OR " +
           		"  UPPER(entered_amount_frmt) LIKE UPPER('%'||?||'%'))";
       		for (int i=0; i<7; i++) {
       		    pParam.add(new bcFeautureParam("string", pFind));
       		}
      	}
       	pParam.add(new bcFeautureParam("int", p_end));
       	pParam.add(new bcFeautureParam("int", p_beg));
        mySQL = mySQL + 
        	"                ORDER BY oper_number" +
        	"        ) WHERE ROWNUM < ? " + 
        	" ) WHERE rn >= ?";
        boolean hasPostingPermission = false;
        //boolean hasAccDocPermission = false;
        boolean hasBKAccountPermission = false;
        boolean hasSchemePermission = false;
        
        boolean hasEditPermission = false;
        try{
        	if (isEditPermited("CARDS_TRANSACTIONS_POSTINS")>=0) {
        		hasEditPermission = true;
        	}
        	/*
        	if (isEditMenuPermited("FINANCE_ACCOUNTING_DOC")>=0) {
        		hasAccDocPermission = true;
        	}
        	*/
        	if (isEditMenuPermited("FINANCE_POSTINGS")>=0) {
        		hasPostingPermission = true;
        	}
        	if (isEditMenuPermited("FINANCE_BK_ACCOUNTS")>=0) {
        		hasBKAccountPermission = true;
        	}
        	if (isEditMenuPermited("FINANCE_POSTING_SCHEME")>=0) {
        		hasSchemePermission = true;
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
            for (int i=1; i <= colCount-9; i++) {
            	html.append(getBottomFrameTableTH(postingXML, mtd.getColumnName(i)));
            }
            if (hasEditPermission) {
            	html.append("<th>&nbsp;</th><th>&nbsp;</th>\n");
	        }
            html.append("</tr></thead><tbody>\n");
            while (rset.next())
            {
                html.append("<tr>\n");
                for (int i=1; i <= colCount-9; i++) {
                	if ("ID_POSTING_DETAIL".equalsIgnoreCase(mtd.getColumnName(i))) {
                		if (hasPostingPermission && (!(rset.getString(i) == null || "".equalsIgnoreCase(rset.getString(i))))) {
                			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/finance/postingspecs.jsp?id="+rset.getString(i), "", ""));
                		} else {
                			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", "", ""));
                		}
                	/*} else if ("ID_POSTING_LINE".equalsIgnoreCase(mtd.getColumnName(i))) {
                		if (hasAccDocPermission && (!(rset.getString(i) == null || "".equalsIgnoreCase(rset.getString(i))))) {
                			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/finance/accounting_docspecs.jsp?id="+rset.getString(i), "", ""));
                		} else {
                			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", "", ""));
                		}
                	*/
                	} else if ("ID_BK_OPERATION_SCHEME_LINE".equalsIgnoreCase(mtd.getColumnName(i))) {
                		if (hasSchemePermission && (!(rset.getString(i) == null || "".equalsIgnoreCase(rset.getString(i))))) {
                			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/finance/posting_scheme_linespecs.jsp?id="+rset.getString("ID_BK_OPERATION_SCHEME_LINE2"), "", ""));
                		} else {
                			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", "", ""));
                		}
                	} else if ("DEBET_ID_BK_ACCOUNT".equalsIgnoreCase(mtd.getColumnName(i))) {
                		if (hasBKAccountPermission && (!(rset.getString(i) == null || "".equalsIgnoreCase(rset.getString(i))))) {
                			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString("DEBET_CD_BK_ACCOUNT"), "../crm/finance/bk_accountspecs.jsp?id="+rset.getString("DEBET_ID_BK_ACCOUNT2"), "", ""));
                		} else {
                			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", "", ""));
                		}
                	} else if ("CREDIT_ID_BK_ACCOUNT".equalsIgnoreCase(mtd.getColumnName(i))) {
                		if (hasBKAccountPermission && (!(rset.getString(i) == null || "".equalsIgnoreCase(rset.getString(i))))) {
                			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString("CREDIT_CD_BK_ACCOUNT"), "../crm/finance/bk_accountspecs.jsp?id="+rset.getString("CREDIT_ID_BK_ACCOUNT2"), "", ""));
                		} else {
                			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", "", ""));
                		}
                	} else {
                		html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", "", ""));
                	}
                }
	            if (hasEditPermission && 
	            	(rset.getString("ID_POSTING_LINE") == null || "".equalsIgnoreCase(rset.getString("ID_POSTING_LINE"))) && 
	            	(rset.getString("ID_CLEARING_LINE") == null || "".equalsIgnoreCase(rset.getString("ID_CLEARING_LINE")))) {
	            	
	            	   String myHyperLink = "../crm/cards/transactionsupdate.jsp?type=posting&id="+this.idTrans+"&id_posting_detail="+rset.getString("ID_POSTING_DETAIL");
	            	   String myDeleteLink = "../crm/cards/transactionsupdate.jsp?type=posting&id="+this.idTrans+"&id_posting_detail="+rset.getString("ID_POSTING_DETAIL")+"&action=remove&process=yes";
	                   html.append(getDeleteButtonHTML(myDeleteLink, transactionXML.getfieldTransl("LAB_DELETE_ONE_POSTING", false), rset.getString("DEBET_CD_BK_ACCOUNT") + " - " + rset.getString("CREDIT_CD_BK_ACCOUNT")));
	            	   html.append(getEditButtonHTML(myHyperLink));
	            } else {
	            	html.append("<td align=center>&nbsp;</td><td align=center>&nbsp;</td>\n");
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
    } //getTransactionsPostingsHTML

    public String getClubRelationshipsHTML() {
        
        StringBuilder html = new StringBuilder();
        Connection con = null;
        PreparedStatement st = null; 
        
    	bcClubCardObject card = new bcClubCardObject(this.getValue("CARD_SERIAL_NUMBER"), this.getValue("ID_ISSUER"), this.getValue("ID_PAYMENT_SYSTEM"));
        
    	ArrayList<bcFeautureParam> pParam = initParamArray();

    	String mySQL =
        	" SELECT rn, id_club_rel, date_club_rel, name_club_rel_type,  " +
      	  	"        sname_party1_full, sname_party2_full " +
      	  	"   FROM (SELECT ROWNUM rn, id_club_rel, name_club_rel_type, date_club_rel_frmt date_club_rel, " +
      	  	"                desc_club_rel, sname_party1 sname_party1_full, " +
      	  	"                sname_party2 sname_party2_full " + 
          	"   		  FROM (SELECT * " +
          	"                     FROM " + getGeneralDBScheme()+".vc_club_rel_club_all " +
          	"                    WHERE cd_club_rel_type IN('SOCIETY-SHAREHOLDER', 'SOCIETY-OTHER') ";
        
        if (!isEmpty(this.getValue("ID_TERMINAL_MANUFACTURER"))) {
        	mySQL = mySQL + 
        		" OR (cd_club_rel_type = 'SOCIETY-TERMINAL_MANUFACTURER' AND " +
        		"     id_party1 = ? AND " +
        		"     id_party2 = ?)";
        	pParam.add(new bcFeautureParam("int", this.getValue("ID_CLUB")));
        	pParam.add(new bcFeautureParam("int", this.getValue("ID_TERMINAL_MANUFACTURER")));
        }
        if (!isEmpty(this.getValue("ID_DEALER"))) {
        	mySQL = mySQL + 
        		" OR (cd_club_rel_type = 'SOCIETY-DEALER' AND " +
        		"     id_party1 = ? AND " +
        		"     id_party2 = ?)";
        	pParam.add(new bcFeautureParam("int", this.getValue("ID_CLUB")));
        	pParam.add(new bcFeautureParam("int", this.getValue("ID_DEALER")));
        	
        	if (!isEmpty(this.getValue("ID_FINANCE_ACQUIRER"))) {
            	mySQL = mySQL + 
            		" OR (cd_club_rel_type = 'DEALER-FINANCE_ACQUIRER' AND " +
            		"     id_party1 = ? AND " +
            		"     id_party2 = ?)";
            	pParam.add(new bcFeautureParam("int", this.getValue("ID_DEALER")));
            	pParam.add(new bcFeautureParam("int", this.getValue("ID_FINANCE_ACQUIRER")));
            }
        	if (!isEmpty(this.getValue("ID_TECHNICAL_ACQUIRER"))) {
            	mySQL = mySQL + 
            		" OR (cd_club_rel_type = 'DEALER-TECHNICAL_ACQUIRER' AND " +
            		"     id_party1 = ? AND " +
            		"     id_party2 = ?)";
            	pParam.add(new bcFeautureParam("int", this.getValue("ID_DEALER")));
            	pParam.add(new bcFeautureParam("int", this.getValue("ID_TECHNICAL_ACQUIRER")));
            }
        }
        if (!isEmpty(this.getValue("ID_FINANCE_ACQUIRER"))) {
        	mySQL = mySQL + 
        		" OR (cd_club_rel_type = 'SOCIETY-FINANCE_ACQUIRER' AND " +
        		"     id_party1 = ? AND " +
        		"     id_party2 = ?)";
        	pParam.add(new bcFeautureParam("int", this.getValue("ID_CLUB")));
        	pParam.add(new bcFeautureParam("int", this.getValue("ID_FINANCE_ACQUIRER")));
        }
    	if (!isEmpty(this.getValue("ID_TECHNICAL_ACQUIRER"))) {
        	mySQL = mySQL + 
        		" OR (cd_club_rel_type = 'SOCIETY-TECHNICAL_ACQUIRER' AND " +
        		"     id_party1 = ? AND " +
        		"     id_party2 = ?)";
        	pParam.add(new bcFeautureParam("int", this.getValue("ID_CLUB")));
        	pParam.add(new bcFeautureParam("int", this.getValue("ID_TECHNICAL_ACQUIRER")));
        }
    	if (!isEmpty(this.getValue("ID_ISSUER"))) {
        	mySQL = mySQL + 
        		" OR (cd_club_rel_type = 'SOCIETY-ISSUER' AND " +
        		"     id_party1 = ? AND " +
        		"     id_party2 = ?)";
        	pParam.add(new bcFeautureParam("int", this.getValue("ID_CLUB")));
        	pParam.add(new bcFeautureParam("int", this.getValue("ID_ISSUER")));
        }
    	if (!isEmpty(this.getValue("ID_TERM_OWNER"))) {
        	mySQL = mySQL + 
        		" OR (cd_club_rel_type = 'SOCIETY-PARTNER' AND " +
        		"     id_party1 = ? AND " +
        		"     id_party2 = ?)";
        	pParam.add(new bcFeautureParam("int", this.getValue("ID_CLUB")));
        	pParam.add(new bcFeautureParam("int", this.getValue("ID_TERM_OWNER")));
        }
    	if (!isEmpty(card.getValue("ID_JUR_PRS_WHO_CARD_SOLD"))) {
        	mySQL = mySQL + 
        		" OR (cd_club_rel_type = 'SOCIETY-CARD_SELLER' AND " +
        		"     id_party1 = ? AND " +
        		"     id_party2 = ?)";
        	pParam.add(new bcFeautureParam("int", this.getValue("ID_CLUB")));
        	pParam.add(new bcFeautureParam("int", this.getValue("ID_JUR_PRS_WHO_CARD_SOLD")));
        }

        mySQL = mySQL + " ORDER BY name_club_rel_type, sname_party1, sname_party2))";
        
        boolean hasRelationshipPermission = false;
       
        try{
        	if (isEditMenuPermited("CLUB_RELATIONSHIP")>=0) {
        		hasRelationshipPermission = true;
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
            for (int i=1; i <= colCount; i++) {
               String colName = mtd.getColumnName(i);
               html.append(getBottomFrameTableTH(relationshipXML, colName));
            }
            html.append("</tr></thead><tbody>\n");
            
            while (rset.next())
                {
                    html.append("<tr>");
              	  	for (int i=1; i <= colCount; i++) {
              	  		if (hasRelationshipPermission && "ID_CLUB_REL".equalsIgnoreCase(mtd.getColumnName(i))) {
              	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/club/relationshipspecs.jsp?id="+rset.getString("ID_CLUB_REL"), "", ""));
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
       }

    public String getFundOperationsHTML(String pFind, String p_beg, String p_end) {
        StringBuilder html = new StringBuilder();
        PreparedStatement st = null;
        Connection con = null;

        ArrayList<bcFeautureParam> pParam = initParamArray();
        
        String mySQL = 
        	" SELECT rn, name_club_fund, name_club_fund_payment_kind, name_club_fund_oper_type, date_fund_oper_frmt, "+
        	"        amount_fund_oper_frmt, desc_fund_oper, id_club_fund " +
        	"   FROM (SELECT ROWNUM rn, name_club_fund || ' ('||sname_club_fund||')' name_club_fund, name_club_fund_payment_kind, " +
        	"				 DECODE(cd_club_fund_oper_type, " +
  			"                      'PAYMENT', '<font color=\"green\">'||name_club_fund_oper_type||'</font>', " +
  			"                      'WRITE_OFF', '<font color=\"blue\">'||name_club_fund_oper_type||'</font>', " +
  			"                      name_club_fund_oper_type" +
			"                ) name_club_fund_oper_type, " +
        	"				 date_fund_oper_frmt,  " +
            "                DECODE(cd_club_fund_oper_type, " +
  			"                      'PAYMENT', '<font color=\"green\">'||amount_fund_oper_frmt||' '||sname_currency ||'</font>', " +
  			"                      'WRITE_OFF', '<font color=\"red\">'||amount_fund_oper_frmt||' '||sname_currency ||'</font>', " +
  			"                      name_club_fund_oper_type" +
			"                ) amount_fund_oper_frmt, desc_fund_oper, id_club_fund " +
        	" 		    FROM (SELECT * " +
        	"                   FROM " + getGeneralDBScheme() + ".vc_fund_oper_all "+
        	" 		           WHERE id_trans = ? ";
    	pParam.add(new bcFeautureParam("int", this.idTrans));

    	
        if (!isEmpty(pFind)) {
    		mySQL = mySQL + 
    				" AND (TO_CHAR(name_club_fund) LIKE UPPER('%'||?||'%') OR " +
    				"  UPPER(date_fund_oper_frmt) LIKE UPPER('%'||?||'%') OR " +
    				"  UPPER(amount_fund_oper_frmt) LIKE UPPER('%'||?||'%')OR " +
    				"  UPPER(desc_fund_oper) LIKE UPPER('%'||?||'%'))";
    		for (int i=0; i<4; i++) {
    		    pParam.add(new bcFeautureParam("string", pFind));
    		}
    	}
    	pParam.add(new bcFeautureParam("int", p_end));
    	pParam.add(new bcFeautureParam("int", p_beg));
        mySQL = mySQL + 
        	"                  ORDER BY id_fund_oper, date_fund_oper DESC, creation_date DESC) " +
        	"          WHERE ROWNUM < ?) " +
        	"  WHERE rn >= ?";
        
        boolean hasClubFundPermission = false;
        
        try{

        	if (isEditMenuPermited("CLUB_FUND")>=0) {
        		hasClubFundPermission = true;
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
                html.append(getBottomFrameTableTH(clubfundXML, mtd.getColumnName(i)));
            }
            html.append("</tr></thead><tbody>");
            
            while (rset.next())
                {
                    html.append("<tr>");
              	  	for (int i=1; i <= colCount-1; i++) {
              	  		if ("NAME_CLUB_FUND".equalsIgnoreCase(mtd.getColumnName(i)) && hasClubFundPermission) {
              	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/club/fundspecs.jsp?id="+rset.getString("ID_CLUB_FUND"), "", ""));
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
    } // getCardRequestsHTML
    
    public String getClubCardsTasksHTML(String pFindString, String pOperType, String pOperState, String p_beg, String p_end) {
    	bcListCardOperation list = new bcListCardOperation();
    	
    	String pWhereCause = "   WHERE id_trans = ? ";
    	
    	ArrayList<bcFeautureParam> pWhereValue = new ArrayList<bcFeautureParam>();
    	pWhereValue.add(new bcFeautureParam("int", this.idTrans));

    	String lDeleteLink = "";
    	String lEditLink = "";
    	
    	return list.getCardOperBasedOnActionsHTML(pWhereCause, pWhereValue, pFindString, pOperType, pOperState, lEditLink, lDeleteLink, "div_data_detail", p_beg, p_end);
    	
    }

}
