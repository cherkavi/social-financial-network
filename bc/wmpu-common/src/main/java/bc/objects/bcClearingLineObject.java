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

public class bcClearingLineObject extends bcObject {
	private final static Logger LOGGER=Logger.getLogger(bcClearingLineObject.class);
	
	private Map<String,Comparable<String>> fieldHm = new HashMap<String, Comparable<String>>();
	
	private String idClearingLine;
	
	public bcClearingLineObject() {	}
	
	public bcClearingLineObject(String pIdClearingLine) {
		this.idClearingLine = pIdClearingLine;
		this.getFeature();
	}
	
	private void getFeature() {
		String featureSelect = " SELECT * FROM " + getGeneralDBScheme() + ".VC_CLEARING_LINES_CLUB_ALL WHERE id_clearing_line = ?";
		fieldHm = getFeatures2(featureSelect, this.idClearingLine, false);
	}
	
	public String getValue(String pColumnName) {
		return getFeautureResult(fieldHm, pColumnName);
	}

    public String getPostingsHTML(String pFind, String p_beg, String p_end) {
    	ArrayList<bcFeautureParam> pParam = initParamArray();

    	StringBuilder html = new StringBuilder();
    	PreparedStatement st = null;
        Connection con = null;
        String mySQL = 
        	" SELECT rn, id_posting_detail, operation_date_frmt operation_date," +
        	"        oper_number id_bk_operation_scheme_line, " +
        	"        debet_cd_bk_account||' '||debet_name_bk_account debet_id_bk_account,  " +
        	"        credit_cd_bk_account||' '||credit_name_bk_account credit_id_bk_account, " +
        	"		 name_currency, entered_amount_frmt entered_amount, assignment_posting, " +
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
        	" 				   WHERE id_clearing_line = ? ";
        pParam.add(new bcFeautureParam("int", this.idClearingLine));
        
	    if (!isEmpty(pFind)) {
	    	mySQL = mySQL + 
				" AND (TO_CHAR(id_posting_detail) LIKE UPPER('%'||?||'%') OR " +
				"	   UPPER(id_posting_line) LIKE UPPER('%'||?||'%') OR " +
				"	   UPPER(operation_date_frmt) LIKE UPPER('%'||?||'%') OR " +
				"	   UPPER(oper_number) LIKE UPPER('%'||?||'%') OR " +
				"	   UPPER(entered_amount_frmt) LIKE UPPER('%'||?||'%') OR " +
				"	   UPPER(assignment_posting) LIKE UPPER('%'||?||'%')) ";
	    	for (int i=0; i<6; i++) {
	    	    pParam.add(new bcFeautureParam("string", pFind));
	    	}
	    }
	    pParam.add(new bcFeautureParam("int", p_end));
	    pParam.add(new bcFeautureParam("int", p_beg));
	    mySQL = mySQL +
        	"                ) WHERE ROWNUM < ? " + 
        	" ) WHERE rn >= ?";
        
        boolean hasPostingPermission = false;
        //boolean hasAccDocPermission = false;
        boolean hasBKAccountPermission = false;
        boolean hasSchemePermission = false;
        
        try{
        	if (isEditMenuPermited("FINANCE_POSTINGS")>=0) {
        		hasPostingPermission = true;
        	}
        	//if (isEditMenuPermited("FINANCE_ACCOUNTING_DOC")>=0) {
        	//	hasAccDocPermission = true;
        	//}
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
        	html.append("</tr></thead><tbody>");
            while (rset.next())
            {
                html.append("<tr>");
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
                			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString("DEBET_CD_BK_ACCOUNT"), "../crm/finance/postingspecs.jsp?id="+rset.getString("DEBET_ID_BK_ACCOUNT2"), "", ""));
                		} else {
                			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", "", ""));
                		}
                	} else if ("CREDIT_ID_BK_ACCOUNT".equalsIgnoreCase(mtd.getColumnName(i))) {
                		if (hasBKAccountPermission && (!(rset.getString(i) == null || "".equalsIgnoreCase(rset.getString(i))))) {
                			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString("CREDIT_CD_BK_ACCOUNT"), "../crm/finance/postingspecs.jsp?id="+rset.getString("CREDIT_ID_BK_ACCOUNT2"), "", ""));
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
                if (st!=null) st.close();
            } catch (SQLException w) {w.toString();}
            try {
                if (con!=null) con.close();
            } catch (SQLException w) {w.toString();}
            Connector.closeConnection(con);
        } // finally
        return html.toString();
    } //getClearingPostingsHTML
	
    public String getBankStatementReconcileLinesHTML(String pFind, String p_beg, String p_end) {
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
        	"        line_number, number_bank_statement, date_bank_statement_frmt, " +
      	  	"        mfo_bank_client, name_bank_branch_client, " +
      	  	"        number_bank_account_client, name_jur_prs_client," +
      	  	"        mfo_bank_correspondent, name_bank_branch_correspondent, " +
      	  	"        num_bank_account_correspondent, name_jur_prs_correspondent, " +
	        "        debet_amount, credit_amount, " +
	        "        payment_assignment, reconcile_state, id_bank_statement_line," +
	        "        client_id_owner_bank_account, client_type_owner_bank_account," +
	        "        client_id_bank_account, client_id_bank," +
	        "        corr_id_owner_bank_account, corr_type_owner_bank_account," +
	        "        corr_id_bank_account, corr_id_bank " +
	        "   FROM (SELECT ROWNUM rn, reconciled_amount_frmt, reconcile_type_tsl, line_number, " +
	        "                id_bank_statement, number_bank_statement, date_bank_statement_frmt, " +
            "                client_sname_owner_bank_accnt name_jur_prs_client, client_number_bank_account number_bank_account_client, " +
            "                client_sname_bank name_bank_branch_client, client_mfo_bank mfo_bank_client, " +
            "                corr_sname_owner_bank_account name_jur_prs_correspondent, " +
	        "        		 corr_number_bank_account num_bank_account_correspondent, " +
	        "        		 corr_sname_bank name_bank_branch_correspondent, corr_mfo_bank mfo_bank_correspondent, " +
	        "        		 debet_amount_frmt debet_amount, credit_amount_frmt credit_amount, " +
	        "        		 assignment payment_assignment, " +
	        "  			     DECODE(reconcile_state, " +
			"                       'UNRECONCILE', '<b><font color=\"red\">'||reconcile_state_tsl||'</font></b>', " +
			"                       reconcile_state_tsl) reconcile_state, " +
			"                id_bank_statement_line," +
	        "                client_id_owner_bank_account, client_type_owner_bank_account," +
	        "                client_id_bank_account, client_id_bank," +
	        "                corr_id_owner_bank_account, corr_type_owner_bank_account," +
	        "                corr_id_bank_account, corr_id_bank " +
	        "   		FROM (SELECT * " +
        	"                   FROM " + getGeneralDBScheme() + ".vc_clearing_reconcile_all " +
        	"  		           WHERE id_clearing_line = ? ";
        pParam.add(new bcFeautureParam("int", this.idClearingLine));
        
	    if (!isEmpty(pFind)) {
	    	mySQL = mySQL + 
				" AND (UPPER(reconciled_amount_frmt) LIKE UPPER('%'||?||'%') OR " +
				"	   UPPER(number_bank_statement) LIKE UPPER('%'||?||'%') OR " +
				"	   UPPER(date_bank_statement_frmt) LIKE UPPER('%'||?||'%') OR " +
				"	   UPPER(client_sname_owner_bank_accnt) LIKE UPPER('%'||?||'%') OR " +
				"	   UPPER(client_number_bank_account) LIKE UPPER('%'||?||'%') OR " +
				"	   UPPER(client_sname_bank) LIKE UPPER('%'||?||'%') OR " +
				"	   UPPER(client_mfo_bank) LIKE UPPER('%'||?||'%') OR " +
				"	   UPPER(corr_sname_owner_bank_account) LIKE UPPER('%'||?||'%') OR " +
				"	   UPPER(corr_number_bank_account) LIKE UPPER('%'||?||'%') OR " +
				"	   UPPER(corr_sname_bank) LIKE UPPER('%'||?||'%') OR " +
				"	   UPPER(corr_mfo_bank) LIKE UPPER('%'||?||'%') OR " +
				"	   UPPER(debet_amount_frmt) LIKE UPPER('%'||?||'%') OR " +
				"	   UPPER(credit_amount_frmt) LIKE UPPER('%'||?||'%') OR " +
				"	   UPPER(assignment) LIKE UPPER('%'||?||'%')) ";
	    	for (int i=0; i<14; i++) {
	    	    pParam.add(new bcFeautureParam("string", pFind));
	    	}
	    }
	    pParam.add(new bcFeautureParam("int", p_end));
	    pParam.add(new bcFeautureParam("int", p_beg));
	    mySQL = mySQL + ") WHERE ROWNUM < ?) WHERE rn >= ? ";
        
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
            html.append("<th rowspan=\"2\">"+ bank_statementXML.getfieldTransl("LINE_NUMBER", false)+"</th>\n");
            html.append("<th rowspan=\"2\">"+ bank_statementXML.getfieldTransl("NUMBER_BANK_STATEMENT", false)+"</th>\n");
            html.append("<th rowspan=\"2\">"+ bank_statementXML.getfieldTransl("DATE_BANK_STATEMENT_FRMT", false)+"</th>\n");
            html.append("<th colspan=\"4\">"+ bank_statementXML.getfieldTransl("H_CLIENT", false)+"</th>\n");
            html.append("<th colspan=\"4\">"+ bank_statementXML.getfieldTransl("H_CORRESPONDENT", false)+"</th>\n");
            html.append("<th rowspan=\"2\">"+ bank_statementXML.getfieldTransl("DEBET_AMOUNT", false)+"</th>\n");
            html.append("<th rowspan=\"2\">"+ bank_statementXML.getfieldTransl("CREDIT_AMOUNT", false)+"</th>\n");
            html.append("<th rowspan=\"2\">"+ bank_statementXML.getfieldTransl("PAYMENT_ASSIGNMENT", false)+"</th>\n");
            html.append("<th rowspan=\"2\">"+ bank_statementXML.getfieldTransl("RECONCILE_STATE", false)+"</th>\n");
            html.append("</tr>\n");
            html.append("<tr>");
            html.append("<th>"+ bank_statementXML.getfieldTransl("MFO_BANK", false)+"</th>\n");
            html.append("<th>"+ bank_statementXML.getfieldTransl("NAME_BANK", false)+"</th>\n");
            html.append("<th>"+ bank_statementXML.getfieldTransl("BANK_ACCOUNT", false)+"</th>\n");
            html.append("<th>"+ bank_statementXML.getfieldTransl("NAME_JUR_PRS", false)+"</th>\n");
            html.append("<th>"+ bank_statementXML.getfieldTransl("MFO_BANK", false)+"</th>\n");
            html.append("<th>"+ bank_statementXML.getfieldTransl("NAME_BANK", false)+"</th>\n");
            html.append("<th>"+ bank_statementXML.getfieldTransl("BANK_ACCOUNT", false)+"</th>\n");
            html.append("<th>"+ bank_statementXML.getfieldTransl("NAME_JUR_PRS", false)+"</th>\n");
            html.append("</tr></thead><tbody>");
            html.append("<tr>");
            while (rset.next())
            {
                html.append("<tr>");
                
                for (int i=1; i <= colCount-9; i++) {
                	
                	if ("LINE_NUMBER".equalsIgnoreCase(mtd.getColumnName(i))) {
                		if (hasClearingPermission) {
                			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/finance/bankstatement_linespecs.jsp?id="+ rset.getString("ID_BANK_STATEMENT_LINE"), "", ""));
                		} else {
                			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", "", ""));
                		}
                	} else if ("NAME_BANK_BRANCH_CLIENT".equalsIgnoreCase(mtd.getColumnName(i))) {
                		if (hasPartnerPermission) {
                			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/clients/yurpersonspecs.jsp?id="+rset.getString("CLIENT_ID_BANK"), "", ""));
                		} else {
                			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", "", ""));
                		}
                	} else if ("NUMBER_BANK_ACCOUNT_CLIENT".equalsIgnoreCase(mtd.getColumnName(i))) {
                		if (hasAccountPermission) {
                			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/clients/accountspecs.jsp?id="+rset.getString("CLIENT_ID_BANK_ACCOUNT"), "", ""));
                		} else {
                			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", "", ""));
                		}
                	} else if ("NAME_JUR_PRS_CLIENT".equalsIgnoreCase(mtd.getColumnName(i))) {
                		if (!isEmpty(rset.getString("CLIENT_TYPE_OWNER_BANK_ACCOUNT"))) {
                			if ("JUR_PRS".equalsIgnoreCase(rset.getString("CLIENT_TYPE_OWNER_BANK_ACCOUNT")) && hasPartnerPermission) {
                    			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/clients/yurpersonspecs.jsp?id="+rset.getString("CLIENT_ID_OWNER_BANK_ACCOUNT"), "", ""));
                			} else if ("NAT_PRS".equalsIgnoreCase(rset.getString("CLIENT_TYPE_OWNER_BANK_ACCOUNT")) && hasClientPermission) {
                    			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/clients/natpersonspecs.jsp?id="+rset.getString("CLIENT_ID_OWNER_BANK_ACCOUNT"), "", ""));
                			}
                		} else {
                			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", "", ""));
                		}
                	} else if ("NAME_BANK_BRANCH_CORRESPONDENT".equalsIgnoreCase(mtd.getColumnName(i))) {
                		if (hasPartnerPermission) {
                			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/clients/yurpersonspecs.jsp?id="+rset.getString("CORR_ID_BANK"), "", ""));
                		} else {
                			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", "", ""));
                		}
                	} else if ("NUM_BANK_ACCOUNT_CORRESPONDENT".equalsIgnoreCase(mtd.getColumnName(i))) {
                		if (hasAccountPermission) {
                			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/clients/accountspecs.jsp?id="+rset.getString("CORR_ID_BANK_ACCOUNT"), "", ""));
                		} else {
                			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", "", ""));
                		}
                	} else if ("NAME_JUR_PRS_CORRESPONDENT".equalsIgnoreCase(mtd.getColumnName(i))) {
                		if (!isEmpty(rset.getString("CORR_TYPE_OWNER_BANK_ACCOUNT"))) {
                			if ("JUR_PRS".equalsIgnoreCase(rset.getString("CORR_TYPE_OWNER_BANK_ACCOUNT")) && hasPartnerPermission) {
                    			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/clients/yurpersonspecs.jsp?id="+rset.getString("CORR_ID_OWNER_BANK_ACCOUNT"), "", ""));
                			} else if ("NAT_PRS".equalsIgnoreCase(rset.getString("CORR_TYPE_OWNER_BANK_ACCOUNT")) && hasClientPermission) {
                    			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/clients/natpersonspecs.jsp?id="+rset.getString("CORR_ID_OWNER_BANK_ACCOUNT"), "", ""));
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
                if (st!=null) st.close();
            } catch (SQLException w) {w.toString();}
            try {
                if (con!=null) con.close();
            } catch (SQLException w) {w.toString();}
            Connector.closeConnection(con);
        } // finally
    	return html.toString();
    } //getClearingAllHTML

}
