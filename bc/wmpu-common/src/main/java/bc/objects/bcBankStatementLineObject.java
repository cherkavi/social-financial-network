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
import bc.service.bcFeautureParam;


public class bcBankStatementLineObject extends bcObject {
	private final static Logger LOGGER=Logger.getLogger(bcBankStatementLineObject.class);
	private Map<String,Comparable<String>> fieldHm = new HashMap<String, Comparable<String>>();
	
	private String idBankStatementLine;
	
	public bcBankStatementLineObject(String pIdBankStatementLine) {
		this.idBankStatementLine = pIdBankStatementLine;
		getFeature();
	}
	
	private void getFeature() {
		String featureSelect = " SELECT * FROM " + getGeneralDBScheme() + ".VC_BS_LINES_CLUB_ALL WHERE id_bank_statement_line = ?";
		fieldHm = getFeatures2(featureSelect, this.idBankStatementLine, false);
	}
	
	public String getValue(String pColumnName) {
		return getFeautureResult(fieldHm, pColumnName);
	}

    public String getPostingsHTML(String pFind, String pBeg, String pEnd) {
        StringBuilder html = new StringBuilder();
        Connection con = null;
        PreparedStatement st = null;
        ArrayList<bcFeautureParam> pParam = initParamArray();
        String mySQL = 	
        	" SELECT rn, id_posting_detail, operation_date_frmt operation_date," +
        	"        name_currency, oper_number id_bk_operation_scheme_line, " +
        	"        debet_cd_bk_account||' '||debet_name_bk_account debet_id_bk_account,  " +
        	"        credit_cd_bk_account||' '||credit_name_bk_account credit_id_bk_account, " +
        	"		 entered_amount_frmt entered_amount, assignment_posting, " +
        	"        id_clearing_line, " +
        	"        debet_id_bk_account debet_id_bk_account2, debet_cd_bk_account, debet_name_bk_account, " +
        	"        credit_id_bk_account credit_id_bk_account2, credit_cd_bk_account, credit_name_bk_account, " +
        	"        id_bk_operation_scheme_line id_bk_operation_scheme_line2, id_posting_line " +
        	"  FROM (SELECT ROWNUM rn, id_posting_detail, id_posting_line, operation_date_frmt, name_currency," +
        	"               id_bk_operation_scheme_line, oper_number, " +
        	"		        entered_amount_frmt, assignment_posting, " +
        	"               id_clearing_line, " +
        	"               debet_id_bk_account, debet_cd_bk_account_frmt debet_cd_bk_account, debet_name_bk_account, " +
        	"               credit_id_bk_account, credit_cd_bk_account_frmt credit_cd_bk_account, credit_name_bk_account " +
        	"          FROM (SELECT * " +
        	"                  FROM "+getGeneralDBScheme() + ".vc_acc_posting_detail_club_all " +
        	"                 WHERE id_bank_statement_line = ? ";
        pParam.add(new bcFeautureParam("int", this.idBankStatementLine));
        
	    if (!isEmpty(pFind)) {
	    	mySQL = mySQL + 
				" AND (TO_CHAR(id_posting_detail) LIKE UPPER('%'||?||'%') OR " +
				"	   UPPER(operation_date_frmt) LIKE UPPER('%'||?||'%') OR " +
				"	   UPPER(oper_number) LIKE UPPER('%'||?||'%') OR " +
				"	   UPPER(debet_cd_bk_account_frmt||' '||debet_name_bk_account) LIKE UPPER('%'||?||'%') OR " +
				"	   UPPER(credit_cd_bk_account_frmt||' '||credit_name_bk_account) LIKE UPPER('%'||?||'%') OR " +
				"	   UPPER(entered_amount_frmt) LIKE UPPER('%'||?||'%') OR " +
				"	   UPPER(assignment_posting) LIKE UPPER('%'||?||'%')) ";
	    	for (int i=0; i<7; i++) {
	    		pParam.add(new bcFeautureParam("string", pFind));
	    	}
	    }
	    pParam.add(new bcFeautureParam("int", pEnd));
	    pParam.add(new bcFeautureParam("int", pBeg));
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
            html.append("</tr></thead><tbody>");
            while (rset.next())
            {
                html.append("<tr>\n");
                for (int i=1; i <= colCount-9; i++) {
                	if ("ID_POSTING_DETAIL".equalsIgnoreCase(mtd.getColumnName(i))) {
                		if (hasPostingPermission && (!isEmpty(rset.getString(i)))) {
                			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/finance/postingspecs.jsp?id="+rset.getString(i), "", ""));
                		} else {
                			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", "", ""));
                		}
                	/*} else if ("ID_POSTING_LINE".equalsIgnoreCase(mtd.getColumnName(i))) {
                		if (hasAccDocPermission && (!isEmpty(rset.getString(i)))) {
                			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/finance/accounting_docspecs.jsp?id="+rset.getString(i), "", ""));
                		} else {
                			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", "", ""));
                		}
                	*/
                	} else if ("ID_BK_OPERATION_SCHEME_LINE".equalsIgnoreCase(mtd.getColumnName(i))) {
                		if (hasSchemePermission && (!isEmpty(rset.getString(i)))) {
                			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/finance/posting_scheme_linespecs.jsp?id="+rset.getString("ID_BK_OPERATION_SCHEME_LINE2"), "", ""));
                		} else {
                			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", "", ""));
                		}
                	} else if ("DEBET_ID_BK_ACCOUNT".equalsIgnoreCase(mtd.getColumnName(i))) {
                		if (hasBKAccountPermission && (!isEmpty(rset.getString(i)))) {
                			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString("DEBET_CD_BK_ACCOUNT"), "../crm/finance/bk_accountspecs.jsp?id="+rset.getString("DEBET_ID_BK_ACCOUNT2"), "", ""));
                		} else {
                			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", "", ""));
                		}
                	} else if ("CREDIT_ID_BK_ACCOUNT".equalsIgnoreCase(mtd.getColumnName(i))) {
                		if (hasBKAccountPermission && (!isEmpty(rset.getString(i)))) {
                			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString("CREDIT_CD_BK_ACCOUNT"), "../crm/finance/bk_accountspecs.jsp?id="+rset.getString("CREDIT_ID_BK_ACCOUNT2"), "", ""));
                		} else {
                			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", "", ""));
                		}
                	} else {
                		html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", "", ""));
                	}
                }
	            if (hasEditPermission && 
	            	(isEmpty(rset.getString("ID_POSTING_LINE"))) && 
	            	(isEmpty(rset.getString("ID_CLEARING_LINE")))) {
	            	
	            	   String myHyperLink = "../crm/finance/bankstatement_lineupdate.jsp?type=posting&id="+this.idBankStatementLine+"&id_posting_detail="+rset.getString("ID_POSTING_DETAIL");
	            	   String myDeleteLink = "../crm/finance/bankstatement_lineupdate.jsp?type=posting&id="+this.idBankStatementLine+"&id_posting_detail="+rset.getString("ID_POSTING_DETAIL")+"&action=remove&process=yes";
	                   html.append(getDeleteButtonHTML(myDeleteLink, transactionXML.getfieldTransl("LAB_DELETE_ONE_POSTING", false), rset.getString("DEBET_CD_BK_ACCOUNT") + " - " + rset.getString("CREDIT_CD_BK_ACCOUNT")));
	            	   html.append(getEditButtonHTML(myHyperLink));
	            } else {
	            	html.append(getBottomFrameTableTD("", "", "", "", ""));
	            	html.append(getBottomFrameTableTD("", "", "", "", ""));
	            }
                html.append("</tr>\n");
            }
            html.append("</tbody></table>\n");
        } // try
        catch (SQLException e) {LOGGER.error(html, e); html.append(showCRMException(e));}
        catch (Exception el) {LOGGER.error(html, el); html.append(showCRMException(el));}
        finally {
            try {
                if (st!=null) {
					st.close();
				}
            } catch (SQLException w) {w.toString();}
            try {
                if (con!=null) {
					con.close();
				}
            } catch (SQLException w) {w.toString();}
            Connector.closeConnection(con);
        } // finally
        return html.toString();
    } //getTransactionsPostingsHTML
	
    public String getClearingReconcileLinesHTML(String pFind, String p_beg, String p_end) {
        StringBuilder html = new StringBuilder();
        PreparedStatement st = null;
        Connection con = null;
        
        boolean hasAccountPermission = false;
        boolean hasPartnerPermission = false;
        boolean hasClientPermission = false;
        boolean hasClearingPermission = false;
        
        ArrayList<bcFeautureParam> pParam = initParamArray();
        
        String mySQL = 
        	" SELECT rn, reconciled_amount_frmt, reconcile_type_tsl, " +
        	"        number_doc_clearing, receiver_mfo_bank, receiver_name_bank_alt, receiver_number_bank_account, " +
        	"		 receiver_sname_owner_ba, payer_mfo_bank, " +
        	" 		 payer_name_bank_alt, payer_number_bank_account, payer_sname_owner_ba, " +
        	"        name_currency, entered_amount, payment_function, " +
        	"		 reconcile_state, id_clearing_line, receiver_id_bank_account, receiver_id_bank, " +
        	"        payer_id_bank_account, payer_id_bank, receiver_type_owner_ba, payer_type_owner_ba, " +
        	"        receiver_id_owner_ba, payer_id_owner_ba " +
        	"   FROM (SELECT ROWNUM rn, reconciled_amount_frmt, reconcile_type_tsl, number_doc_clearing, " +
        	"        		 receiver_sname_owner_ba, receiver_number_bank_account, " +
        	"		 		 receiver_sname_bank receiver_name_bank_alt, receiver_mfo_bank, payer_sname_owner_ba, " +
        	" 		 		 payer_number_bank_account, payer_sname_bank payer_name_bank_alt, " +
        	"        		 payer_mfo_bank, name_currency, entered_amount_frmt entered_amount, payment_function,  " +
        	"		 		 reconcile_state_tsl reconcile_state, " +
        	"		 		 id_clearing_line, receiver_id_bank_account, receiver_id_bank, " +
        	"        		 payer_id_bank_account, payer_id_bank, " +
        	"        		 receiver_type_owner_ba, payer_type_owner_ba, " +
        	"        		 receiver_id_owner_ba, payer_id_owner_ba " +
        	"   		FROM (SELECT * " +
        	"                   FROM " + getGeneralDBScheme() + ".vc_bs_reconcile_all " +
        	"  		           WHERE id_bank_statement_line = ? ";
        pParam.add(new bcFeautureParam("int", this.idBankStatementLine));
        
	    if (!isEmpty(pFind)) {
	    	mySQL = mySQL + 
				" AND (UPPER(reconciled_amount_frmt) LIKE UPPER('%'||?||'%') OR " +
				"	   UPPER(number_doc_clearing) LIKE UPPER('%'||?||'%') OR " +
				"	   UPPER(receiver_sname_owner_ba) LIKE UPPER('%'||?||'%') OR " +
				"	   UPPER(receiver_number_bank_account) LIKE UPPER('%'||?||'%') OR " +
				"	   UPPER(receiver_sname_bank) LIKE UPPER('%'||?||'%') OR " +
				"	   UPPER(receiver_mfo_bank) LIKE UPPER('%'||?||'%') OR " +
				"	   UPPER(payer_sname_owner_ba) LIKE UPPER('%'||?||'%') OR " +
				"	   UPPER(payer_number_bank_account) LIKE UPPER('%'||?||'%') OR " +
				"	   UPPER(payer_sname_bank) LIKE UPPER('%'||?||'%') OR " +
				"	   UPPER(payer_mfo_bank) LIKE UPPER('%'||?||'%') OR " +
				"	   UPPER(entered_amount_frmt) LIKE UPPER('%'||?||'%') OR " +
				"	   UPPER(payment_function) LIKE UPPER('%'||?||'%')) ";
	    	for (int i=0; i<12; i++) {
	    	    pParam.add(new bcFeautureParam("string", pFind));
	    	}
	    }

	    pParam.add(new bcFeautureParam("int", p_end));
	    pParam.add(new bcFeautureParam("int", p_beg));
	    mySQL = mySQL +	") WHERE ROWNUM < ?) WHERE rn >= ";
        
        try {
        	if (isEditMenuPermited("CLIENTS_BANK_ACCOUNTS")>=0) {
        		hasAccountPermission = true;
        	}
        	if (isEditMenuPermited("CLIENTS_YURPERSONS")>=0) {
        		hasPartnerPermission = true;
        	}
        	if (isEditMenuPermited("CLIENTS_NATPERSONS")>=0) {
        		hasClientPermission = true;
        	}
        	if (isEditMenuPermited("FINANCE_CLEARING")>=0) {
        		hasClearingPermission = true;
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
            html.append("<th rowspan=\"2\">"+ commonXML.getfieldTransl("RN", false)+"</th>\n");
            html.append("<th rowspan=\"2\">"+ clearingXML.getfieldTransl("RECONCILED_AMOUNT_FRMT", false)+"</th>\n");
            html.append("<th rowspan=\"2\">"+ bank_statementXML.getfieldTransl("RECONCILE_TYPE", false)+"</th>\n");
            html.append("<th rowspan=\"2\">"+ clearingXML.getfieldTransl("NUMBER_DOC_CLEARING", false)+"</th>\n");
            html.append("<th colspan=\"4\">"+ clearingXML.getfieldTransl("RECEIVER_PARTICIPANT", false)+"</th>\n");
            html.append("<th colspan=\"4\">"+ clearingXML.getfieldTransl("PAYER_PARTICIPANT", false)+"</th>\n");
            html.append("<th rowspan=\"2\">"+ clearingXML.getfieldTransl("NAME_CURRENCY", false)+"</th>\n");
            html.append("<th rowspan=\"2\">"+ clearingXML.getfieldTransl("ENTERED_AMOUNT", false)+"</th>\n");
            html.append("<th rowspan=\"2\">"+ clearingXML.getfieldTransl("PAYMENT_FUNCTION", false)+"</th>\n");
            html.append("<th rowspan=\"2\">"+ clearingXML.getfieldTransl("RECONCILE_STATE", false)+"</th>\n");
            html.append("</tr>");
            html.append("<tr>");
            html.append("<th>"+ clearingXML.getfieldTransl("RECEIVER_MFO_BANK", false)+"</th>\n");
            html.append("<th>"+ clearingXML.getfieldTransl("RECEIVER_NAME_BANK_ALT", false)+"</th>\n");
            html.append("<th>"+ clearingXML.getfieldTransl("RECEIVER_NUMBER_BANK_ACCOUNT", false)+"</th>\n");
            html.append("<th>"+ clearingXML.getfieldTransl("RECEIVER_SNAME_OWNER_BA", false)+"</th>\n");
            html.append("<th>"+ clearingXML.getfieldTransl("PAYER_MFO_BANK", false)+"</th>\n");
            html.append("<th>"+ clearingXML.getfieldTransl("PAYER_NAME_BANK_ALT", false)+"</th>\n");
            html.append("<th>"+ clearingXML.getfieldTransl("PAYER_NUMBER_BANK_ACCOUNT", false)+"</th>\n");
            html.append("<th>"+ clearingXML.getfieldTransl("PAYER_SNAME_OWNER_BA", false)+"</th>\n");
            html.append("</tr></thead><tbody>");
            html.append("<tr>");
            html.append("<td colspan=\"17\" align=\"center\">");
            html.append(getSubmitButtonAjax("../crm/finance/clearingupdate.jsp"));
            html.append("</td></tr>\n");
            while (rset.next())
            {
                html.append("<tr>");
                
                for (int i=1; i <= colCount-9; i++) {
                	
                	if ("NUMBER_DOC_CLEARING".equalsIgnoreCase(mtd.getColumnName(i))) {
                		if (hasClearingPermission) {
                			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/finance/clearing_linespecs.jsp?id="+ rset.getString("ID_CLEARING_LINE"), "", ""));
                		} else {
                			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", "", ""));
                		}
                	} else if ("RECEIVER_NAME_BANK_ALT".equalsIgnoreCase(mtd.getColumnName(i))) {
                		if (hasPartnerPermission) {
                			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/clients/yurpersonspecs.jsp?id="+rset.getString("RECEIVER_ID_BANK"), "", ""));
                		} else {
                			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", "", ""));
                		}
                	} else if ("RECEIVER_NUMBER_BANK_ACCOUNT".equalsIgnoreCase(mtd.getColumnName(i))) {
                		if (hasAccountPermission) {
                			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/clients/accountspecs.jsp?id="+rset.getString("RECEIVER_ID_BANK_ACCOUNT"), "", ""));
                		} else {
                			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", "", ""));
                		}
                	} else if ("RECEIVER_SNAME_OWNER_BA".equalsIgnoreCase(mtd.getColumnName(i))) {
                		if (!isEmpty(rset.getString("RECEIVER_TYPE_OWNER_BA"))) {
                			if ("JUR_PRS".equalsIgnoreCase(rset.getString("RECEIVER_TYPE_OWNER_BA")) && hasPartnerPermission) {
                    			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/clients/yurpersonspecs.jsp?id="+rset.getString("RECEIVER_ID_OWNER_BA"), "", ""));
                			} else if ("NAT_PRS".equalsIgnoreCase(rset.getString("RECEIVER_TYPE_OWNER_BA")) && hasClientPermission) {
                    			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/clients/natpersonspecs.jsp?id="+rset.getString("RECEIVER_ID_OWNER_BA"), "", ""));
                			}
                		} else {
                			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", "", ""));
                		}
                	} else if ("PAYER_NAME_BANK_ALT".equalsIgnoreCase(mtd.getColumnName(i))) {
                		if (hasPartnerPermission) {
                			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/clients/yurpersonspecs.jsp?id="+rset.getString("PAYER_ID_BANK"), "", ""));
                		} else {
                			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", "", ""));
                		}
                	} else if ("PAYER_NUMBER_BANK_ACCOUNT".equalsIgnoreCase(mtd.getColumnName(i))) {
                		if (hasAccountPermission) {
                			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/clients/accountspecs.jsp?id="+rset.getString("PAYER_ID_BANK_ACCOUNT"), "", ""));
                		} else {
                			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", "", ""));
                		}
                	} else if ("PAYER_SNAME_OWNER_BA".equalsIgnoreCase(mtd.getColumnName(i))) {
                		if (!isEmpty(rset.getString("PAYER_TYPE_OWNER_BA"))) {
                			if ("JUR_PRS".equalsIgnoreCase(rset.getString("PAYER_TYPE_OWNER_BA")) && hasPartnerPermission) {
                    			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/clients/yurpersonspecs.jsp?id="+rset.getString("PAYER_ID_OWNER_BA"), "", ""));
                			} else if ("NAT_PRS".equalsIgnoreCase(rset.getString("PAYER_TYPE_OWNER_BA")) && hasClientPermission) {
                    			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/clients/natpersonspecs.jsp?id="+rset.getString("PAYER_ID_OWNER_BA"), "", ""));
                			}
                		} else {
                			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", "", ""));
                		}
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
                if (st!=null) {
					st.close();
				}
            } catch (SQLException w) {w.toString();}
            try {
                if (con!=null) {
					con.close();
				}
            } catch (SQLException w) {w.toString();}
            Connector.closeConnection(con);
        } // finally
    	return html.toString();
    } //getClearingAllHTML

	  public String getFileLinesHTML(String pFind, String p_beg, String p_end) {
	      StringBuilder html = new StringBuilder();
	      PreparedStatement st = null;
	      Connection con = null;
	      ArrayList<bcFeautureParam> pParam = initParamArray();
	      String mySQL = 
	    	  " SELECT line_number, line_text " +
	          "   FROM (SELECT ROWNUM rn, line_number, line_text " +
	          "   		  FROM (SELECT line_number, line_text " +
	          "                   FROM " + getGeneralDBScheme() + ".vc_bs_file_lines " +
	          "  		         WHERE id_bank_statement_line = ? ";
	      pParam.add(new bcFeautureParam("int", this.idBankStatementLine));
	      
	      if (!isEmpty(pFind)) {
	    	  mySQL = mySQL + " AND (UPPER(line_text) LIKE UPPER('%'||?||'%')) ";
	    	  pParam.add(new bcFeautureParam("string", pFind));
	      }
	      pParam.add(new bcFeautureParam("int", p_end));
	      pParam.add(new bcFeautureParam("int", p_beg));
	      mySQL = mySQL +
	          "                  ORDER BY line_number" +
	          "                ) WHERE ROWNUM < ? " +  
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
	          for (int i=1; i <= colCount; i++) {
	        	  html.append(getBottomFrameTableTH(bank_statementXML, mtd.getColumnName(i)));
	          }
	          html.append("</tr></thead><tbody>");
	          while (rset.next())
	          {
	              html.append("<tr>");
	              for (int i=1; i <= colCount; i++) {
	            	  html.append(getBottomFrameTableTDBase("", mtd.getColumnName(i), mtd.getColumnType(i), rset.getString(i), "", "", ""));
	              }
	              html.append("</tr>\n");
	          }
	          html.append("</tbody></table>\n");

	      } // try
	      catch (SQLException e) {LOGGER.error(html, e); html.append(showCRMException(e));}
	        catch (Exception el) {LOGGER.error(html, el); html.append(showCRMException(el));}
	      finally {
	            try {
	                if (st!=null) {
						st.close();
					}
	            } catch (SQLException w) {w.toString();}
	            try {
	                if (con!=null) {
						con.close();
					}
	            } catch (SQLException w) {w.toString();}
	            Connector.closeConnection(con);
	        } // finally
	      return html.toString();
	  } //getBankStatementLinesIntHTML

}
