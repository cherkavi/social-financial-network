package bc.objects;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import bc.connection.Connector;
import bc.service.bcFeautureParam;


public class bcBankStatementHeaderObject extends bcObject {
	private final static Logger LOGGER=Logger.getLogger(bcBankStatementHeaderObject.class);
	private Map<String,Comparable<String>> fieldHm = new HashMap<String, Comparable<String>>();
	
	private String importState;
	private String idBankStatement;
	
	public bcBankStatementHeaderObject() {
	}
	
	public bcBankStatementHeaderObject(String pImportState, String pIdBankStatement) {
		this.importState = pImportState;
		this.idBankStatement = pIdBankStatement;
		getFeature();
	}
	
	private void getFeature() {
		String featureSelect = "";
		if ("IMPORTED".equalsIgnoreCase(this.importState)) {
			featureSelect = " SELECT * FROM " + getGeneralDBScheme() + ".VC_BS_HEADERS_CLUB_ALL WHERE id_bank_statement = ?";
		} else {
			featureSelect = " SELECT * FROM " + getGeneralDBScheme() + ".VC_BS_HEADERS_INT_CLUB_ALL WHERE id_bank_statement = ?";
		}
		fieldHm = getFeatures2(featureSelect, this.idBankStatement, false);
	}
	
	public String getValue(String pColumnName) {
		return getFeautureResult(fieldHm, pColumnName);
	}

	public String getBankStatementLinesHTML(String pFind, String p_beg, String p_end) {
		StringBuilder html = new StringBuilder();
		PreparedStatement st = null;
	    Connection con = null;
	    ArrayList<bcFeautureParam> pParam = initParamArray();
	    int rowCnt = 0;
	      
	    String mySQL = 
	    	" SELECT line_number, mfo_bank_correspondent, name_bank_branch_correspondent, " +
	        "        num_bank_account_correspondent, name_jur_prs_correspondent, " +
	        "        cd_operation, date_operation, "+ 
	        "        debet_amount, credit_amount, number_document, date_document, " +
	        "        payment_assignment, need_bs_line_import, import_state_bs_line_tsl, reconcile_state, " +
	        "        reconcile_state2, id_bank_statement_line, import_state_bs_line, need_bs_line_import_tsl, " +
			"        corr_id_owner_bank_account, corr_type_owner_bank_account, " +
			"        corr_id_bank_account, corr_id_bank " +
	        "   FROM (SELECT ROWNUM rn, line_number, mfo_bank_correspondent, name_bank_branch_correspondent, " +
	        "        		   num_bank_account_correspondent, name_jur_prs_correspondent, " +
	        "        		   cd_operation, date_operation, "+ 
	        "        		   debet_amount, credit_amount, number_document, date_document, " +
	        "        		   payment_assignment, reconcile_state, import_state_bs_line_tsl, " +
	        "                  need_bs_line_import, reconcile_state2, id_bank_statement_line, " +
	        "                  import_state_bs_line, need_bs_line_import_tsl, " +
			"                  corr_id_owner_bank_account, corr_type_owner_bank_account, " +
			"                  corr_id_bank_account, corr_id_bank " +
	        "   		 FROM (SELECT id_bank_statement_line, line_number, " +
	        "                         corr_sname_owner_bank_account name_jur_prs_correspondent, " +
	        "        		   		  corr_number_bank_account num_bank_account_correspondent, " +
	        "        		   		  corr_sname_bank name_bank_branch_correspondent, " +
	        "                         TO_CHAR(corr_mfo_bank) mfo_bank_correspondent, " +
	        "        		   		  operation_code cd_operation, operation_date_frmt date_operation, "+ 
	        "        		   		  debet_amount_frmt debet_amount, credit_amount_frmt credit_amount, " +
	        "                         doc_number number_document, doc_date_frmt date_document, " +
	        "        		   		  assignment payment_assignment, " +
	        "  			   		  	  DECODE(reconcile_state, " +
			"                       		 'RECONCILE', '<b><font color=\"green\">'||reconcile_state_tsl||'</font></b>', " +
			"                       		 'UNRECONCILE', '<b><font color=\"red\">'||reconcile_state_tsl||'</font></b>', " +
			"                       		 reconcile_state_tsl" +
			"                         ) reconcile_state, " +
			"                         need_bs_line_import, need_bs_line_import_tsl, " +
	        "  			   		  	  DECODE(import_state_bs_line, " +
			"                		   		 'IMPORTED', '<b><font color=\"green\">'||import_state_bs_line_tsl||'</font></b>', " +
			"                       		 'ERROR', '<b><font color=\"red\">'||import_state_bs_line_tsl||'</font></b>', " +
			"                       		 'NOT_IMPORTED', '<b>'||import_state_bs_line_tsl||'</b>', " +
			"                       		 '<b><font color=\"gray\">'||import_state_bs_line_tsl||'</font></b>'" +
			"                      	  ) import_state_bs_line_tsl, " +
			"                		  reconcile_state reconcile_state2, " +
			"                         import_state_bs_line, " +
			"                         corr_id_owner_bank_account, corr_type_owner_bank_account, " +
			"                         corr_id_bank_account, corr_id_bank " +
	        "   		  		 FROM " + getGeneralDBScheme() + ".vc_bs_lines_club_all " +
	        " 	            	WHERE id_bank_statement = ? ";
	    pParam.add(new bcFeautureParam("string", this.idBankStatement));
	    if (!isEmpty(pFind)) {
	    	mySQL = mySQL + 
					" AND (TO_CHAR(id_bank_statement_line) LIKE UPPER('%'||?||'%') OR " +
					"	   UPPER(corr_sname_owner_bank_account) LIKE UPPER('%'||?||'%') OR " +
					"	   UPPER(corr_number_bank_account) LIKE UPPER('%'||?||'%') OR " +
					"	   UPPER(corr_mfo_bank) LIKE UPPER('%'||?||'%') OR " +
					"	   UPPER(operation_code) LIKE UPPER('%'||?||'%') OR " +
					"	   UPPER(operation_date_frmt) LIKE UPPER('%'||?||'%') OR " +
					"	   UPPER(debet_amount_frmt) LIKE UPPER('%'||?||'%') OR " +
					"	   UPPER(credit_amount_frmt) LIKE UPPER('%'||?||'%') OR " +
					"	   UPPER(doc_number) LIKE UPPER('%'||?||'%') OR " +
					"	   UPPER(doc_date_frmt) LIKE UPPER('%'||?||'%') OR " +
					"	   UPPER(assignment) LIKE UPPER('%'||?||'%')) ";
	    	for (int i=0; i<11; i++) {
	    		pParam.add(new bcFeautureParam("string", pFind));
	    	}
	    }
	    pParam.add(new bcFeautureParam("int", p_end));
	    pParam.add(new bcFeautureParam("int", p_beg));
	    mySQL = mySQL +
	        "                   ORDER BY line_number " +
	        "                )" +
	        "          WHERE ROWNUM < ? " + 
	        "  ) WHERE rn >= ?";
	    
	    boolean hasLineEditPerm = false;
	    
	    boolean hasAccountPermission = false;
        boolean hasPartnerPermission = false;
        boolean hasClientPermission = false;
        
	    try{
        	if (isEditMenuPermited("CLIENTS_BANK_ACCOUNTS")>=0) {
        		hasAccountPermission = true;
        	}
        	if (isEditMenuPermited("CLIENTS_YURPERSONS")>=0) {
        		hasPartnerPermission = true;
        	}
        	if (isEditMenuPermited("CLIENTS_NATPERSONS")>=0) {
        		hasClientPermission = true;
        	}
        	
	    	if (isEditPermited("FINANCE_BANKSTATEMENT_LINES")>0) {
	    		hasLineEditPerm = true;
	    	}
	    	  
	    	LOGGER.debug(prepareSQLToLog(mySQL, pParam));
	    	con = Connector.getConnection(getSessionId());
	    	st = con.prepareStatement(mySQL);
	    	st = prepareParam(st, pParam);
	    	ResultSet rset = st.executeQuery();

	        ResultSetMetaData mtd = rset.getMetaData();
	        int colCount = mtd.getColumnCount();

	        if (hasLineEditPerm) {
		        html.append("<form action=\"../crm/finance/bankstatementupdate.jsp\" name=\"updateForm\" id=\"updateForm\" accept-charset=\"UTF-8\" method=\"POST\">");
                html.append("<input type=\"hidden\" name=\"action\" value=\"set_need_import\">");
                html.append("<input type=\"hidden\" name=\"process\" value=\"yes\">");
                html.append("<input type=\"hidden\" name=\"type\" value=\"line\">");
                html.append("<input type=\"hidden\" name=\"id\" value=\""+ this.idBankStatement +"\">");
	        }
	            
	        html.append(getBottomFrameTable());
	        html.append("<tr>");
	        boolean isEx = false;
	        for (int i=1; i <= colCount-8; i++) {
	            String colName = mtd.getColumnName(i);
	            if (i!=2 && i!=3 && i!=4 && i!=5) {
	            	if ("NEED_BS_LINE_IMPORT".equalsIgnoreCase(colName)) {
	            		if (hasLineEditPerm) {
	            			html.append("<th rowspan=\"2\">"+ bank_statementXML.getfieldTransl("NEED_BS_LINE_IMPORT", false)+
		            			"<br><input id=\"mainCheck\" name=\"mainCheck\" type=\"checkbox\" onclick=\"CheckAll(this,'chb_id')\"></th>\n");
	            		} else {
	            			html.append("<th rowspan=\"2\">"+ bank_statementXML.getfieldTransl(colName, false)+"</th>\n");
	            		}
	            	} else {  
	            		html.append("<th rowspan=\"2\">"+ bank_statementXML.getfieldTransl(colName, false)+"</th>\n");
	            	}
	            } else {
	            	if (!isEx) {
	            		html.append("<th colspan=\"4\">"+ bank_statementXML.getfieldTransl("CORRESPONDENT", false)+"</th>\n");
	            		isEx = true;
	            	}
	            }
	        }
	        if (hasLineEditPerm) {
	        	html.append("<th rowspan=\"2\">&nbsp;</th><th rowspan=\"2\">&nbsp;</th>\n");
	        }
	        html.append("</tr><tr>\n");
	        for (int i=1; i <= colCount-8; i++) {
	            String colName = mtd.getColumnName(i);
	            if (i==2 || i==3 || i==4 || i==5) {
	            	html.append("<th>"+ bank_statementXML.getfieldTransl(colName, false)+"</th>\n");
	            }
	        }
	          
	        html.append("</tr></thead><tbody>\n");

	        if (hasLineEditPerm) {
		        html.append("<tr>");
		        html.append("<td colspan=\"17\" align=\"center\">");
		        html.append(getSubmitButtonAjax("../crm/finance/bankstatementupdate.jsp"));
		        html.append("</td></tr>\n");
	        }
	        while (rset.next()) {
	            html.append("<tr>");

	            rowCnt = rowCnt + 1;
	                
	            String id = "id_" + rset.getString("ID_BANK_STATEMENT_LINE");

	            String tprvCheck="prv_"+id;
	            String tCheck="chb_"+id;
	                
	            for (int i=1; i <= colCount-8; i++) {
	            	if ("LINE_NUMBER".equalsIgnoreCase(mtd.getColumnName(i))) {
	            		  html.append(getBottomFrameTableTDBase("", mtd.getColumnName(i), mtd.getColumnType(i), rset.getString(i), "../crm/finance/bankstatement_linespecs.jsp?id="+rset.getString("ID_BANK_STATEMENT_LINE"), "", ""));
	            	} else if ("NEED_BS_LINE_IMPORT".equalsIgnoreCase(mtd.getColumnName(i))) {
	            		if (!"IMPORTED".equalsIgnoreCase(rset.getString("IMPORT_STATE_BS_LINE"))) {
		            		if (hasLineEditPerm) {
		                        if ("Y".equalsIgnoreCase(rset.getString("NEED_BS_LINE_IMPORT"))) {
		                        	html.append("<td align=\"center\"><INPUT  type=\"checkbox\" value = \"Y\" name="+tCheck+" id="+tCheck+" CHECKED onclick=\"return CheckCB(this);\"></td>\n");
		                        	html.append("<INPUT type=hidden  value=\"Y\" name="+tprvCheck+">");
		                        	html.append("</td>\n");
		                        } else {
		                        	html.append("<td align=\"center\"><INPUT  type=\"checkbox\" value = \"Y\" name="+tCheck+" id="+tCheck+" onclick=\"return CheckCB(this);\"></td>\n");
		                        }
		                    } else {
		                        html.append(getBottomFrameTableTDBase("", "NEED_BS_LINE_IMPORT_TSL", mtd.getColumnType(i), rset.getString("NEED_BS_LINE_IMPORT_TSL"), "", "", ""));
		                    }
	            		} else {
	            			html.append(getBottomFrameTableTDBase("", "NEED_BS_LINE_IMPORT_TSL", mtd.getColumnType(i), rset.getString("NEED_BS_LINE_IMPORT_TSL"), "", "", ""));
	            		}

                	} else if ("NAME_BANK_BRANCH_CORRESPONDENT".equalsIgnoreCase(mtd.getColumnName(i))) {
                		if (hasPartnerPermission  && 
                				!isEmpty(rset.getString("CORR_ID_BANK"))) {
                			html.append(getBottomFrameTableTDBase("", mtd.getColumnName(i), mtd.getColumnType(i), rset.getString(i), "../crm/clients/yurpersonspecs.jsp?id="+rset.getString("CORR_ID_BANK"), "", ""));
                		} else {
                			html.append(getBottomFrameTableTDBase("", mtd.getColumnName(i), mtd.getColumnType(i), rset.getString(i), "", "", ""));
                		}
                	} else if ("NUM_BANK_ACCOUNT_CORRESPONDENT".equalsIgnoreCase(mtd.getColumnName(i))) {
                		if (hasAccountPermission && 
                				!isEmpty(rset.getString("CORR_ID_BANK_ACCOUNT"))) {
                			html.append(getBottomFrameTableTDBase("", mtd.getColumnName(i), mtd.getColumnType(i), rset.getString(i), "../crm/clients/accountspecs.jsp?id="+rset.getString("CORR_ID_BANK_ACCOUNT"), "", ""));
                		} else {
                			html.append(getBottomFrameTableTDBase("", mtd.getColumnName(i), mtd.getColumnType(i), rset.getString(i), "", "", ""));
                		}
                	} else if ("NAME_JUR_PRS_CORRESPONDENT".equalsIgnoreCase(mtd.getColumnName(i))) {
                		if (!isEmpty(rset.getString("CORR_TYPE_OWNER_BANK_ACCOUNT"))) {
                			if ("JUR_PRS".equalsIgnoreCase(rset.getString("CORR_TYPE_OWNER_BANK_ACCOUNT")) && 
                					hasPartnerPermission &&
                					!isEmpty(rset.getString("CORR_ID_OWNER_BANK_ACCOUNT"))) {
                				html.append(getBottomFrameTableTDBase("", mtd.getColumnName(i), mtd.getColumnType(i), rset.getString(i), "../crm/clients/yurpersonspecs.jsp?id="+rset.getString("CORR_ID_OWNER_BANK_ACCOUNT"), "", ""));
                			} else if ("NAT_PRS".equalsIgnoreCase(rset.getString("CORR_TYPE_OWNER_BANK_ACCOUNT")) && 
                					hasClientPermission 
                					&& !isEmpty(rset.getString("CORR_ID_OWNER_BANK_ACCOUNT"))) {
                				html.append(getBottomFrameTableTDBase("", mtd.getColumnName(i), mtd.getColumnType(i), rset.getString(i), "../crm/clients/natpersonspecs.jsp?id="+rset.getString("CORR_ID_OWNER_BANK_ACCOUNT"), "", ""));
                			}
                		} else {
                			html.append(getBottomFrameTableTDBase("", mtd.getColumnName(i), mtd.getColumnType(i), rset.getString(i), "", "", ""));
                		}
	            	} else {
	            		html.append(getBottomFrameTableTDBase("", mtd.getColumnName(i), mtd.getColumnType(i), rset.getString(i), "", "", ""));
	            	}
	            }
            	if (hasLineEditPerm) {
            		String myDeleteLink = "../crm/finance/bankstatementupdate.jsp?id="+this.idBankStatement+"&line="+rset.getString("ID_BANK_STATEMENT_LINE")+"&type=general&action=remove_line&process=yes";
            		html.append(getDeleteButtonHTML(myDeleteLink,  bank_statementXML.getfieldTransl("h_delete_line", false), rset.getString("LINE_NUMBER")));
            	    html.append(getEditButtonHTML("../crm/finance/bankstatement_linespecs.jsp?id="+rset.getString("ID_BANK_STATEMENT_LINE")));
            		
            	}
	            html.append("</tr>\n");
	        }
	        if (hasLineEditPerm) {
		        html.append("<tr>");
		        html.append("<td colspan=\"17\" align=\"center\">");
		        html.append(getSubmitButtonAjax("../crm/finance/bankstatementupdate.jsp"));
		        html.append("</td></tr>\n");
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
	} //getBankStatementLinesHTML
	
    public String getBankStatementsForKvitovkaHTML(String debet_accnt, String credit_accnt, String idLine, String cDate, String cNumber, String cEnteredAmount, String pClearingAmount, String pagesHTML, String p_beg, String p_end) {
        StringBuilder html = new StringBuilder();
        PreparedStatement st = null;
        Connection con = null;
        boolean hasEditPermission = false;
        
        ArrayList<bcFeautureParam> pParam = initParamArray();
        
        try{
        	if (isEditPermited("FINANCE_KVITOVKA_EXEC")>0) {
        		hasEditPermission = true;
        	}
        	
        	String mySQL = 
        		" SELECT id_bank_statement_line, line_number, number_bank_statement, " +
        		"		 date_bank_statement, name_jur_prs_client, number_bank_account_client, " +
        		"        name_bank_branch_client, name_jur_prs_correspondent, num_bank_account_correspondent, " +
        		"        name_bank_branch_correspondent, cd_operation, date_operation, "+ 
        		"        debet_amount_frmt debet_amount, credit_amount_frmt credit_amount, number_document, date_document, " +
        		"        payment_assignment, reconcile_state, " +
        		"        TO_CHAR(ROUND(NVL(ABS (NVL(debet_amount,0) - NVL(credit_amount,0)), 0)/100,2),'fm9999999999990d0099') full_sum " +
        		"   FROM (SELECT ROWNUM rn, id_bank_statement_line, line_number, number_bank_statement, " +
        		"		   		 date_bank_statement_frmt date_bank_statement, " +
        		"        		 client_sname_owner_ba name_jur_prs_client, " +
        		"        		 client_number_bank_account number_bank_account_client, " +
        		"        		 client_sname_bank name_bank_branch_client, " +
        		"        		 corr_sname_owner_ba name_jur_prs_correspondent, " +
        		"        		 corr_number_bank_account num_bank_account_correspondent, " +
        		"        		 corr_sname_bank name_bank_branch_correspondent, " +
        		"        		 operation_code cd_operation, operation_date_frmt date_operation, "+ 
        		"        		 debet_amount_frmt, debet_amount, credit_amount_frmt, credit_amount, doc_number number_document, doc_date_frmt date_document, " +
        		"        		 assignment payment_assignment, reconcile_state_tsl reconcile_state " +
        		"   		FROM (SELECT * " +
        		"           		FROM " + getGeneralDBScheme() + ".vc_bs_lines_club_all " +
        		"                  WHERE reconcile_state IN ('UNRECONCILE','RECONCILE_PART') ";
            if (!("".equalsIgnoreCase(cDate))) {
            	mySQL = mySQL + " AND doc_date = TO_DATE(?,?)";
            	pParam.add(new bcFeautureParam("string", cDate));
            	pParam.add(new bcFeautureParam("string", this.getDateFormat()));
            }
            if (!("".equalsIgnoreCase(debet_accnt))) {
            	mySQL = mySQL + " AND client_id_bank_account = ? ";
            	pParam.add(new bcFeautureParam("int", debet_accnt));
            }
            if (!("".equalsIgnoreCase(credit_accnt))) {
            	mySQL = mySQL + " AND corr_id_bank_account = ? ";
            	pParam.add(new bcFeautureParam("int", credit_accnt));
            }
            if (!("".equalsIgnoreCase(cNumber))) {
            	mySQL = mySQL + " AND doc_number = ? ";
            	pParam.add(new bcFeautureParam("string", cNumber));
            }
            if (!("".equalsIgnoreCase(cEnteredAmount))) {
            	mySQL = mySQL + " AND (debet_amount_frmt = ? OR credit_amount_frmt = ?) ";
            	pParam.add(new bcFeautureParam("string", cEnteredAmount));
            	pParam.add(new bcFeautureParam("string", cEnteredAmount));
            }

            pParam.add(new bcFeautureParam("int", p_end));
            pParam.add(new bcFeautureParam("int", p_beg));
            mySQL = " ) WHERE ROWNUM < ?) WHERE rn >= ?";

            LOGGER.debug(prepareSQLToLog(mySQL, pParam));
        	con = Connector.getConnection(getSessionId());
        	st = con.prepareStatement(mySQL);
        	st = prepareParam(st, pParam);
        	ResultSet rset = st.executeQuery();

            ResultSetMetaData mtd = rset.getMetaData();
            int colCount = mtd.getColumnCount();

            if (hasEditPermission) {
            	html.append("<script language=\"JavaScript\">\n");
            	html.append("function set_reconcile_sum(pSum, pClearingAmount) {\n");
            	html.append("  if (pSum>pClearingAmount) {");
                html.append("    document.getElementById('reconciled_sum').value=pClearingAmount;\n");
                html.append("  } else {\n");
                html.append("    document.getElementById('reconciled_sum').value=pSum;\n");
                html.append("  }\n");
                html.append("}\n");
                html.append("</script>\n");
                html.append("<form action=\"../crm/finance/kvitovkaupdate.jsp\" accept-charset=\"UTF-8\" method=\"POST\">\n");
                html.append("<input type=\"hidden\" name=\"id_clearing_line\" value=\""+idLine+"\">\n");
                html.append("<input type=\"hidden\" name=\"action\" value=\"reconcile\">\n");
                html.append("<input type=\"hidden\" name=\"process\" value=\"yes\">\n");
                html.append("<center><b>" +bank_statementXML.getfieldTransl("general", false) + "</b></center>\n");
                html.append(getSubmitButtonAjax2("../crm/clients/yurpersonscomissionupdate.jsp", "verify"));
                html.append("&nbsp;<input type=\"text\" id=\"reconciled_sum\" name=\"reconciled_sum\" size=\"10\" value=\"\" class=\"inputfield\">&nbsp;\n");
            } else {
            	html.append("<center><b>" +bank_statementXML.getfieldTransl("general", false) + "</b></center><br>\n");
            }
            html.append("&nbsp;&nbsp;&nbsp;" + pagesHTML);
            html.append(getBottomFrameTable());
            html.append("<tr>");
            if (hasEditPermission) {
             html.append("<th>&nbsp;</th>\n");
            }
            
            for (int i=1; i <= colCount-1; i++) {
            	html.append(getBottomFrameTableTH(bank_statementXML, mtd.getColumnName(i)));
            }
            html.append("</tr></thead><tbody>");
            while (rset.next())
            {
                html.append("<tr>\n");
                for (int i=1; i <= colCount-1; i++) {

                    html.append("<td>\n");
                    if (hasEditPermission) {
                     if (i==1) {
                      html.append("<input type=\"radio\" name=\"bank_statement\" value=\""+rset.getString(1)+"\" class=\"inputfield\" onclick=\"set_reconcile_sum("+rset.getString("FULL_SUM").replaceAll(",", ".")+","+pClearingAmount.replaceAll(",", ".")+")\">"+
                    		  "</td><td>\n");
                     }
                    }
                    html.append(getValue3(rset.getString(i)) +"</td>\n");
                }
                html.append("</tr>\n");
            }
            html.append("</tbody></table>\n");
            if (hasEditPermission) {
             //html.append("<button type=\"submit\" class=\"button\">"+ buttonXML.getfieldTransl(getLanguage(),"verify")+"</button>&nbsp;<input type=\"text\" id=\"reconciled_sum\" name=\"reconciled_sum\" size=\"10\" value=\"\" class=\"inputfield\">&nbsp;");
             html.append("</form>");
            }
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
    } //getBankStatementsHTML

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
        	"                 WHERE id_bank_statement_line IN "+  
        	"   		  			(SELECT id_bank_statement_line " +
	        "                   	   FROM " + getGeneralDBScheme() + ".vc_bs_lines_club_all " +
	        " 	             		  WHERE id_bank_statement = ? ";
        pParam.add(new bcFeautureParam("int", this.idBankStatement));
        
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
	        "                )" +
        	"        ) WHERE ROWNUM < ?" + 
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
	            	
	            	   String myHyperLink = "../crm/finance/bankstatementupdate.jsp?type=posting&id="+this.idBankStatement+"&id_posting_detail="+rset.getString("ID_POSTING_DETAIL");
	            	   String myDeleteLink = "../crm/finance/bankstatementupdate.jsp?type=posting&id="+this.idBankStatement+"&id_posting_detail="+rset.getString("ID_POSTING_DETAIL")+"&action=remove&process=yes";
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
        	"  		           WHERE id_bank_statement = ? ";
        pParam.add(new bcFeautureParam("string", this.idBankStatement));
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
        mySQL = mySQL + " ) WHERE ROWNUM>="+p_beg+") WHERE rn<"+p_end;
        
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

	  public String getNotImportedLinesIntHTML(String p_beg, String p_end) {
	      StringBuilder html = new StringBuilder();
	      PreparedStatement st = null;
	      Connection con = null;
	      String mySQL = 
	    	  " SELECT line_number, mfo_bank_correspondent,  "+
	          "        name_bank_branch_correspondent, num_bank_account_correspondent, " +
	          "        inn_number_jur_prs_corr, name_jur_prs_correspondent, cd_operation, date_operation, "+ 
	          "        debet_amount, credit_amount, number_document, date_document, " +
	          "        payment_assignment, record_status_flag_tsl, id_bank_statement_line, record_status_flag " +
	          "   FROM (SELECT ROWNUM rn, line_number, corr_name name_jur_prs_correspondent, " +
	          "        		   corr_inn_number inn_number_jur_prs_corr,  "+
	          "        		   corr_bank_account_number num_bank_account_correspondent, " +
	          "        		   corr_bank_name name_bank_branch_correspondent, " +
	          "        		   corr_bank_mfo mfo_bank_correspondent, " +
	          "        		   operation_code cd_operation, operation_date_frmt date_operation, "+ 
	          "        		   debet_amount_frmt debet_amount, credit_amount_frmt credit_amount, " +
	          "        		   doc_number number_document, doc_date_frmt date_document, " +
	          "        		   assignment payment_assignment, record_status_flag_tsl, " +
	          "                id_bank_statement_line, record_status_flag " +
	          "   		  FROM (SELECT * " +
	          "                   FROM " + getGeneralDBScheme() + ".vc_bs_lines_int_club_all " +
	          "  		         WHERE id_bank_statement = ? " +
	          "                  ORDER BY line_number" +
	          "                ) WHERE ROWNUM < ? " + 
	          " ) WHERE rn >= ?";
	      
	      boolean hasLineEditPerm = false;
	      
	      String myFont = "";
	      String myBgColor = noneBackGroundStyle;
	      
	      try{
	    	  if (isEditPermited("FINANCE_BSIMPORT_LINES")>0) {
	    		  hasLineEditPerm = true;
	    	  }
	    	  
	    	  LOGGER.debug(mySQL + 
		    			", 1={" + this.idBankStatement + ",int}" + 
		    			", 2={" + p_end + ",int}" + 
		    			", 3={" + p_beg + ",int}");
	    	  con = Connector.getConnection(getSessionId());
	    	  st = con.prepareStatement(mySQL);
	    	  st.setInt(1, Integer.parseInt(this.idBankStatement));
	    	  st.setInt(2, Integer.parseInt(p_end));
	    	  st.setInt(3, Integer.parseInt(p_beg));
	    	  ResultSet rset = st.executeQuery();

	          ResultSetMetaData mtd = rset.getMetaData();
	          int colCount = mtd.getColumnCount();

	          html.append(getBottomFrameTable());
	          html.append("<tr>");
	          boolean isEx = false;
	          for (int i=1; i <= colCount-2; i++) {
	              String colName = mtd.getColumnName(i);
	              if (i!=2 && i!=3 && i!=4 && i!=5 && i!=6) {
	            	  html.append("<th rowspan=\"2\">"+ bank_statementXML.getfieldTransl(colName, false)+"</th>\n");
	              } else {
	            	  if (!isEx) {
	            		  html.append("<th colspan=\"5\">"+ bank_statementXML.getfieldTransl("CORRESPONDENT", false)+"</th>\n");
	            		  isEx = true;
	            	  }
	              }
	          }
	          if (hasLineEditPerm) {
	        	  html.append("<th rowspan=\"2\">&nbsp;</th><th rowspan=\"2\">&nbsp;</th>\n");
	          }
	          html.append("</tr><tr>\n");
	          for (int i=1; i <= colCount-2; i++) {
	              String colName = mtd.getColumnName(i);
	              if (i==2 || i==3 || i==4 || i==5 || i==6) {
	            	  html.append("<th>"+ bank_statementXML.getfieldTransl(colName, false)+"</th>\n");
	              }
	          }
	          
	          html.append("</tr></thead><tbody>\n");
	          while (rset.next())
	          {
	              html.append("<tr>");
          	  if ("E".equalsIgnoreCase(rset.getString("RECORD_STATUS_FLAG"))) {
          		  myFont = "<font color=\"red\">";
            	  myBgColor = selectedBackGroundStyle;
          	  } else {
          		  myFont = "";
          		  myBgColor = noneBackGroundStyle;
          	  }
	              for (int i=1; i <= colCount-2; i++) {
	                  html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", myFont, myBgColor));
	              }
          	  if (hasLineEditPerm) {
          		  String myHyperLink = "../crm/finance/bsimportupdate.jsp?id="+this.idBankStatement+"&line="+rset.getString("ID_BANK_STATEMENT_LINE")+"&type=line";
          		  String myDeleteLink = "../crm/finance/bsimportupdate.jsp?id="+this.idBankStatement+"&line="+rset.getString("ID_BANK_STATEMENT_LINE")+"&type=line&action=remove&process=yes";
          		  html.append(getDeleteButtonStyle2HTML(myDeleteLink, myBgColor, bank_statementXML.getfieldTransl("h_delete_line", false), rset.getString("LINE_NUMBER")));
          		  html.append(getEditButtonStyleHTML(myHyperLink, myBgColor));
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

	  public String runBankStatementImport (String bs_number, String imp_type) {
	      StringBuilder html = new StringBuilder();
	      int result = -1;
	      CallableStatement cs = null;
	      Connection con = null;
	      String reportId = "";
	      try
	      {
	    	  con = Connector.getConnection(getSessionId());

	    	  if (imp_type=="2") {
	    		  cs = con.prepareCall("{? = call PACK_UI_BANK_STATEMENT_IMP.run_bank_statement_import(?,?,NULL,'N',?,?)}");
	    		  cs.registerOutParameter(1, Types.NUMERIC);
	    		  cs.setString(2,bs_number);
	    		  cs.setString(3,imp_type);
	    		  cs.registerOutParameter(4, Types.NUMERIC);
	    		  cs.registerOutParameter(5, Types.VARCHAR);
	    	  } else {
	    		  cs = con.prepareCall("{? = call PACK_UI_BANK_STATEMENT_IMP.run_bank_statement_import(NULL,?,NULL,'N',?,?)}");
	    		  cs.registerOutParameter(1, Types.NUMERIC);
	    		  cs.setString(2,imp_type);
	    		  cs.registerOutParameter(3, Types.NUMERIC);
	    		  cs.registerOutParameter(4, Types.VARCHAR);
	    	  }
	    	  cs.execute();
	    	  result = cs.getInt(1);
	    	  reportId = new Integer(result).toString();
	    	  cs.close();
	      }
	      catch (SQLException e) {LOGGER.error(html, e); html.append(showCRMException(e));}
	        catch (Exception el) {LOGGER.error(html, el); html.append(showCRMException(el));}
		  finally {
	            try {
	                if (cs!=null) {
						cs.close();
					}
	            } catch (SQLException w) {w.toString();}
	            try {
	                if (con!=null) {
						con.close();
					}
	            } catch (SQLException w) {w.toString();}
	            Connector.closeConnection(con);
	        } // finally
	      return reportId;
	  } // runBankStatementImport

	  public String getBankStatementFileLinesHTML(String pFind, String p_beg, String p_end) {
	      StringBuilder html = new StringBuilder();
	      PreparedStatement st = null;
	      Connection con = null;
	      
	      ArrayList<bcFeautureParam> pParam = initParamArray();
	      
	      String mySQL = 
	    	  " SELECT line_number, line_text " +
	          "   FROM (SELECT ROWNUM rn, line_number, line_text " +
	          "   		  FROM (SELECT line_number, line_text " +
	          "                   FROM " + getGeneralDBScheme() + ".vc_bs_file_lines " +
	          "  		         WHERE id_bank_statement_file = ? ";
	      pParam.add(new bcFeautureParam("int", this.getValue("ID_BANK_STATEMENT_FILE")));
	      
	      if (!isEmpty(pFind)) {
	    	  mySQL = mySQL + " AND (UPPER(line_text) LIKE UPPER('%'||?||'%')) ";
	    	  pParam.add(new bcFeautureParam("string", pFind));
	      }
	      pParam.add(new bcFeautureParam("int", p_end));
	      pParam.add(new bcFeautureParam("int", p_beg));
	      mySQL = mySQL +
	          "                  ORDER BY line_number" +
	          "                ) WHERE ROWNUM < ?" + 
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
