package bc.objects;

import java.net.URLEncoder;
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

public class bcClearingObject extends bcObject {
	private final static Logger LOGGER=Logger.getLogger(bcClearingObject.class);
	
	private Map<String,Comparable<String>> fieldHm = new HashMap<String, Comparable<String>>();
	
	private String idClearing;
	
	public bcClearingObject() {	}
	
	public bcClearingObject(String pIdClearint) {
		this.idClearing = pIdClearint;
		this.getFeature();
	}
	
	private void getFeature() {
		String featureSelect = " SELECT * FROM " + getGeneralDBScheme() + ".VC_CLEARING_CLUB_ALL WHERE id_clearing = ?";
		fieldHm = getFeatures2(featureSelect, this.idClearing, false);
	}
	
	public String getValue(String pColumnName) {
		return getFeautureResult(fieldHm, pColumnName);
	}
	
    public String getClearingLinesHTML(String pFind, String pIdOwner, String pPostingTab, String p_beg, String p_end) {
        StringBuilder html = new StringBuilder();
        PreparedStatement st = null;
        Connection con = null;
        int rowCnt = 0;
        
        boolean hasAccountPermission = false;
        boolean hasPartnerPermission = false;
        boolean hasClientPermission = false;
        
        ArrayList<bcFeautureParam> pParam = initParamArray();
        
        String mySQL = 
        	" SELECT rn, number_doc_clearing, receiver_mfo_bank, receiver_name_bank_alt, receiver_number_bank_account, " +
        	"		 receiver_sname_owner_ba, payer_mfo_bank, " +
        	" 		 payer_name_bank_alt, payer_number_bank_account, payer_sname_owner_ba, " +
        	"        name_currency, entered_amount, payment_function, need_clearing_line_export, state_clearing_line_export_tsl, " +
        	"		 reconcile_state, id_clearing_line, receiver_id_bank_account, receiver_id_bank, " +
        	"        payer_id_bank_account, payer_id_bank, receiver_type_owner_ba, payer_type_owner_ba, " +
        	"        receiver_id_owner_ba, payer_id_owner_ba " +
        	"   FROM (SELECT ROWNUM rn, number_doc_clearing, " +
        	"        		 receiver_sname_owner_ba, receiver_number_bank_account, " +
        	"		 		 receiver_sname_bank receiver_name_bank_alt, receiver_mfo_bank, payer_sname_owner_ba, " +
        	" 		 		 payer_number_bank_account, payer_sname_bank payer_name_bank_alt, " +
        	"        		 payer_mfo_bank, name_currency, entered_amount_frmt entered_amount, payment_function,  " +
        	"		 		 need_clearing_line_export, state_clearing_line_export_tsl, reconcile_state_tsl reconcile_state, " +
        	"		 		 id_clearing_line, receiver_id_bank_account, receiver_id_bank, " +
        	"        		 payer_id_bank_account, payer_id_bank, " +
        	"        		 receiver_type_owner_ba, payer_type_owner_ba, " +
        	"        		 receiver_id_owner_ba, payer_id_owner_ba " +
        	"   		FROM (SELECT * " +
        	"                   FROM " + getGeneralDBScheme() + ".VC_CLEARING_LINES_CLUB_ALL " +
        	"  		           WHERE id_clearing = ? ";
        pParam.add(new bcFeautureParam("int", this.idClearing));
        
	    if (!isEmpty(pFind)) {
	    	mySQL = mySQL + 
				" AND (UPPER(number_doc_clearing) LIKE UPPER('%'||?||'%') OR " +
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
	    	for (int i=0; i<11; i++) {
	    	    pParam.add(new bcFeautureParam("string", pFind));
	    	}
	    }
        if (!isEmpty(pIdOwner)) {
        	mySQL = mySQL + " AND (receiver_id_owner_ba = ? OR payer_id_owner_ba = ?) ";
        	pParam.add(new bcFeautureParam("int", pIdOwner));
        	pParam.add(new bcFeautureParam("int", pIdOwner));
        }
        pParam.add(new bcFeautureParam("int", p_end));
        pParam.add(new bcFeautureParam("int", p_beg));
        mySQL =	mySQL + "  ORDER BY number_line) WHERE ROWNUM < ?) WHERE rn >= ?";
        
        boolean hasPermission = false;
        
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
        	
        	if (isEditPermited("FINANCE_CLEARING_LINES")>0) {
        		hasPermission = true;
        	}
        	
        	con = Connector.getConnection(getSessionId());
        	st = con.prepareStatement(mySQL);
        	st = prepareParam(st, pParam);
        	ResultSet rset = st.executeQuery();

            ResultSetMetaData mtd = rset.getMetaData();
            int colCount = mtd.getColumnCount();

            if (hasPermission) {
                html.append("<form action=\"../crm/finance/clearingupdate.jsp\" name=\"updateForm\" id=\"updateForm\" accept-charset=\"UTF-8\" method=\"POST\">");
                html.append("<input type=\"hidden\" name=\"action\" value=\"edit\">");
                html.append("<input type=\"hidden\" name=\"process\" value=\"yes\">");
                html.append("<input type=\"hidden\" name=\"type\" value=\"set_state\">");
                html.append("<input type=\"hidden\" name=\"id_clearing\" value=\""+ this.idClearing +"\">");
            }
            html.append(getBottomFrameTable());
            html.append("<tr>");
            html.append("<th rowspan=\"2\">"+ commonXML.getfieldTransl("RN", false)+"</th>\n");
            html.append("<th rowspan=\"2\">"+ clearingXML.getfieldTransl("NUMBER_DOC_CLEARING", false)+"</th>\n");
            html.append("<th colspan=\"4\">"+ clearingXML.getfieldTransl("RECEIVER_PARTICIPANT", false)+"</th>\n");
            html.append("<th colspan=\"4\">"+ clearingXML.getfieldTransl("PAYER_PARTICIPANT", false)+"</th>\n");
            html.append("<th rowspan=\"2\">"+ clearingXML.getfieldTransl("NAME_CURRENCY", false)+"</th>\n");
            html.append("<th rowspan=\"2\">"+ clearingXML.getfieldTransl("ENTERED_AMOUNT", false)+"</th>\n");
            html.append("<th rowspan=\"2\">"+ clearingXML.getfieldTransl("PAYMENT_FUNCTION", false)+"</th>\n");
            html.append("<th rowspan=\"2\">"+ clearingXML.getfieldTransl("NEED_CLEARING_LINE_EXPORT", false)+
            		"<br><input id=\"mainCheck\" name=\"mainCheck\" type=\"checkbox\" onclick=\"CheckAll(this,'chb_id')\"></th>\n");

            html.append("<th rowspan=\"2\">"+ clearingXML.getfieldTransl("STATE_CLEARING_LINE_EXPORT", false)+"</th>\n");
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
            if (hasPermission) {
            	html.append("<tr>");
            	html.append("<td colspan=\"17\" align=\"center\">");
            	html.append(getSubmitButtonAjax("../crm/finance/clearingupdate.jsp"));
            	html.append("</td></tr>\n");
            }
            while (rset.next())
            {
                html.append("<tr>");
                rowCnt = rowCnt + 1;
                
                String id = "id_" + rset.getString("ID_CLEARING_LINE");

                String tprvCheck="prv_"+id;
                String tCheck="chb_"+id;
                
                for (int i=1; i <= colCount-9; i++) {
                	
                	if ("NUMBER_DOC_CLEARING".equalsIgnoreCase(mtd.getColumnName(i))) {
                		if (!isEmpty(pPostingTab)) {
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
                	} else if ("NEED_CLEARING_LINE_EXPORT".equalsIgnoreCase(mtd.getColumnName(i))) {
                        if (hasPermission) {
                        	if ("Y".equalsIgnoreCase(rset.getString("NEED_CLEARING_LINE_EXPORT"))) {
                        		html.append("<td align=\"center\"><INPUT  type=\"checkbox\" value = \"Y\" name="+tCheck+" id="+tCheck+" CHECKED onclick=\"return CheckCB(this);\"></td>\n");
                        		html.append("<INPUT type=hidden  value=\"Y\" name="+tprvCheck+">");
                        		html.append("</td>\n");
                        	} else {
                        		html.append("<td align=\"center\"><INPUT  type=\"checkbox\" value = \"Y\" name="+tCheck+" id="+tCheck+" onclick=\"return CheckCB(this);\"></td>\n");
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
            
            if (hasPermission) {
                html.append("<input type=\"hidden\" name=\"row_count\" value=\""+rowCnt+"\">");
                html.append("");
                html.append("<tr>");
                html.append("<td colspan=\"17\" align=\"center\">");
                html.append(getSubmitButtonAjax("../crm/finance/clearingupdate.jsp"));
                html.append("</td></tr></form>");
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

    public String getClearingPostingsHTML(String pIdClearingLine, String pFind, String p_beg, String p_end) {
        StringBuilder html = new StringBuilder();
        PreparedStatement st = null;
        Connection con = null;
        ArrayList<bcFeautureParam> pParam = initParamArray();

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
        	"                WHERE 1=1";
        if (isEmpty(pIdClearingLine)) {
        	mySQL = mySQL + " AND id_clearing_line in (" +
        			" SELECT b.id_clearing_line " +
        			"   FROM " + getGeneralDBScheme() + ".VC_CLEARING_LINES_CLUB_ALL b" +
        			"  WHERE b.id_clearing = ?)";
        	pParam.add(new bcFeautureParam("int", this.idClearing));
        } else {
        	mySQL = mySQL + " AND id_clearing_line = ? ";
        	pParam.add(new bcFeautureParam("int", pIdClearingLine));
        }
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
        mySQL = mySQL + "  ) WHERE ROWNUM < ?) WHERE rn >= ?";
        
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
                			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/finance/accounting_docspecs.jsp?type=posting&id_posting="+rset.getString(i), "", ""));
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

    public String getClearingFilesHTML(String idFile, String pFind, String p_beg, String p_end) {
        StringBuilder html = new StringBuilder();
        PreparedStatement st = null;
        Connection con = null;
        ArrayList<bcFeautureParam> pParam = initParamArray();

        String mySQL = 
        	" SELECT id_file, name_file, data_format, record_count, creation_date, name_user, path_file "+
            "   FROM (SELECT ROWNUM rn, id_file, name_file, data_format, " +
            "                creation_date_time_frmt creation_date, name_user, record_count, path_file "+
            "           FROM (SELECT * " +
            "                   FROM " + getGeneralDBScheme() + ".vc_exchange_files_all "+ 
            "                  WHERE id_entry = ? " +
            "                    AND type_entry IN ('CLEARING', 'CLEARING_REPORT') ";
        pParam.add(new bcFeautureParam("int", this.idClearing));
        
	    if (!isEmpty(pFind)) {
	    	mySQL = mySQL + 
				" AND (TO_CHAR(id_file) LIKE UPPER('%'||?||'%') OR " +
				"	   UPPER(name_file) LIKE UPPER('%'||?||'%') OR " +
				"	   UPPER(data_format) LIKE UPPER('%'||?||'%') OR " +
				"	   UPPER(record_count) LIKE UPPER('%'||?||'%') OR " +
				"	   UPPER(creation_date_time_frmt) LIKE UPPER('%'||?||'%') OR " +
				"	   UPPER(name_user) LIKE UPPER('%'||?||'%') OR " +
				"	   UPPER(path_file) LIKE UPPER('%'||?||'%')) ";
	    	for (int i=0; i<7; i++) {
	    	    pParam.add(new bcFeautureParam("string", pFind));
	    	}
	    }
        if (!isEmpty(idFile)) {
        	mySQL = mySQL + " AND id_file = ? ";
        	pParam.add(new bcFeautureParam("int", idFile));
        }

        pParam.add(new bcFeautureParam("int", p_end));
        pParam.add(new bcFeautureParam("int", p_beg));
        mySQL = mySQL +
        	"                  ORDER BY id_file desc " +
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
            html.append("<th>&nbsp;</th>\n");
            for (int i=1; i <= colCount-1; i++) {
            	html.append(getBottomFrameTableTH(exp_fileXML, mtd.getColumnName(i)));
            }
            html.append("</tr></thead><tbody>");
            while (rset.next())
            {
                html.append("<tr>");
                if (isEmpty(rset.getString("RECORD_COUNT")) || "0".equalsIgnoreCase(rset.getString("RECORD_COUNT"))) {
                	html.append("<td align=\"center\">&nbsp;</td>");
                } else {
	                if (isEmpty(idFile)) {
		                html.append("<td align=\"center\">");
						html.append(getHyperLinkFirst()+"../crm/finance/clearingspecs.jsp?id="+idClearing+"&id_file="+rset.getString("ID_FILE")+"&detail_type=FILE" + getHyperLinkMiddle());
						html.append("<img vspace=\"0\" hspace=\"0\" src=\"../images/oper/rows/postings.png\" align=\"top\" style=\"border: 0px;\" title=\""+ clearingXML.getfieldTransl("t_file_content", false) +"\">");
						html.append(getHyperLinkEnd() + "</td>\n");
	                } else {
		                html.append("<td align=\"center\">");
						html.append(getHyperLinkFirst()+"../crm/finance/clearingspecs.jsp?id="+idClearing+"&detail_type=FILE" + getHyperLinkMiddle());
						html.append("<img vspace=\"0\" hspace=\"0\" src=\"../images/oper/rows/postings.png\" align=\"top\" style=\"border: 0px;\" title=\""+ clearingXML.getfieldTransl("t_file_content", false) +"\">");
						html.append(getHyperLinkEnd() + "</td>\n");
	                }
                }
				
               	for (int i=1; i <= colCount-1; i++) {
               		if ("NAME_FILE".equalsIgnoreCase(mtd.getColumnName(i))) {
               			html.append("<td><a href=\"../FileSender?FILENAME=" + 
               					URLEncoder.encode(rset.getString("path_file") + "/" + rset.getString("name_file"),"UTF-8") + "\" title=\"" + 
               					buttonXML.getfieldTransl("open", false) + 
               					"\" target=_blank>" + rset.getString("name_file") + "</a></td>\n");
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
    } //getClearingFilesHTML

    public String getClearingFilesDetailHTML(String idFile, String pFind, String p_beg, String p_end) {
        StringBuilder html = new StringBuilder();
        PreparedStatement st = null;
        Connection con = null;
        ArrayList<bcFeautureParam> pParam = initParamArray();

        String mySQL = 
        	" SELECT line_number, field_value1, field_value2, field_value3, "+
            "        field_value4, field_value5, field_value6, field_value7, "+
            "        field_value8, field_value9, field_value10, field_value11, "+
            "        field_value12, field_value13, field_value14, "+
            "        field_value15, field_value16 "+
            "   FROM (SELECT ROWNUM rn, line_number, field_value1, field_value2, field_value3, "+
            "                field_value4, field_value5, field_value6, field_value7, "+
            "                field_value8, field_value9, field_value10, field_value11, "+
            "                field_value12, field_value13, field_value14, "+
            "                field_value15, field_value16 "+
            "           FROM (SELECT * " +
            "                   FROM " + getGeneralDBScheme() + ".vc_exchange_file_lines "+ 
            "                  WHERE id_file = ? ";
        pParam.add(new bcFeautureParam("int", idFile));
        
	    if (!isEmpty(pFind)) {
	    	mySQL = mySQL + 
				" AND (UPPER(field_value1) LIKE UPPER('%'||?||'%') OR " +
				"	   UPPER(field_value2) LIKE UPPER('%'||?||'%') OR " +
				"	   UPPER(field_value3) LIKE UPPER('%'||?||'%') OR " +
				"	   UPPER(field_value4) LIKE UPPER('%'||?||'%') OR " +
				"	   UPPER(field_value5) LIKE UPPER('%'||?||'%') OR " +
				"	   UPPER(field_value6) LIKE UPPER('%'||?||'%') OR " +
				"	   UPPER(field_value7) LIKE UPPER('%'||?||'%') OR " +
				"	   UPPER(field_value8) LIKE UPPER('%'||?||'%') OR " +
				"	   UPPER(field_value9) LIKE UPPER('%'||?||'%') OR " +
				"	   UPPER(field_value10) LIKE UPPER('%'||?||'%') OR " +
				"	   UPPER(field_value11) LIKE UPPER('%'||?||'%') OR " +
				"	   UPPER(field_value12) LIKE UPPER('%'||?||'%') OR " +
				"	   UPPER(field_value13) LIKE UPPER('%'||?||'%') OR " +
				"	   UPPER(field_value14) LIKE UPPER('%'||?||'%') OR " +
				"	   UPPER(field_value15) LIKE UPPER('%'||?||'%') OR " +
				"	   UPPER(field_value16) LIKE UPPER('%'||?||'%')) ";
	    	for (int i=0; i<16; i++) {
	    	    pParam.add(new bcFeautureParam("string", pFind));
	    	}
	    }
	    pParam.add(new bcFeautureParam("int", p_end));
	    pParam.add(new bcFeautureParam("int", p_beg));
	    mySQL = mySQL +
            "                  ORDER BY line_number) " +
            "		   WHERE ROWNUM < ? " + 
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
            html.append("<th rowspan=\"2\">"+ clearingXML.getfieldTransl("NUMBER_LINE", false)+"</th>\n");
            html.append("<th rowspan=\"2\">"+ clearingXML.getfieldTransl("DATE_CLEARING", false)+"</th>\n");
            html.append("<th rowspan=\"2\">"+ clearingXML.getfieldTransl("NUMBER_CLEARING", false)+"</th>\n");
            html.append("<th rowspan=\"2\">"+ clearingXML.getfieldTransl("ENTERED_AMOUNT", false)+"</th>\n");
            html.append("<th colspan=\"6\">"+ clearingXML.getfieldTransl("RECEIVER_PARTICIPANT", false)+"</th>\n");
            html.append("<th colspan=\"6\">"+ clearingXML.getfieldTransl("PAYER_PARTICIPANT", false)+"</th>\n");
            html.append("<th rowspan=\"2\">"+ clearingXML.getfieldTransl("PAYMENT_FUNCTION", false)+"</th>\n");
            html.append("</tr>");
            html.append("<tr>");
            html.append("<th>"+ clearingXML.getfieldTransl("RECEIVER_SNAME_OWNER_BA", false)+"</th>\n");
            html.append("<th>"+ clearingXML.getfieldTransl("RECEIVER_INN_NUMBER", false)+"</th>\n");
            html.append("<th>"+ clearingXML.getfieldTransl("RECEIVER_NUMBER_BANK_ACCOUNT", false)+"</th>\n");
            html.append("<th>"+ clearingXML.getfieldTransl("RECEIVER_NAME_BANK_ALT", false)+"</th>\n");
            html.append("<th>"+ clearingXML.getfieldTransl("RECEIVER_MFO_BANK", false)+"</th>\n");
            html.append("<th>"+ clearingXML.getfieldTransl("RECEIVER_CODE_COUNTRY", false)+"</th>\n");

            html.append("<th>"+ clearingXML.getfieldTransl("PAYER_SNAME_OWNER_BA", false)+"</th>\n");
            html.append("<th>"+ clearingXML.getfieldTransl("PAYER_INN_NUMBER", false)+"</th>\n");
            html.append("<th>"+ clearingXML.getfieldTransl("PAYER_NUMBER_BANK_ACCOUNT", false)+"</th>\n");
            html.append("<th>"+ clearingXML.getfieldTransl("PAYER_NAME_BANK_ALT", false)+"</th>\n");
            html.append("<th>"+ clearingXML.getfieldTransl("PAYER_MFO_BANK", false)+"</th>\n");
            html.append("<th>"+ clearingXML.getfieldTransl("PAYER_CODE_COUNTRY", false)+"</th>\n");
            html.append("</tr></thead><tbody>");
            while (rset.next())
            {
                html.append("<tr>");
                for (int i=1; i <= colCount; i++) {
                    html.append("<td>"+ getValue3(rset.getString(i)) +"</td>\n");
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
    } //getClearingFilesHTML
	
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
        	"  		           WHERE id_clearing = ? ";
        pParam.add(new bcFeautureParam("int", this.idClearing));
        
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
	    mySQL = mySQL + ") WHERE ROWNUM < ?) WHERE rn >= ?";
        
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
