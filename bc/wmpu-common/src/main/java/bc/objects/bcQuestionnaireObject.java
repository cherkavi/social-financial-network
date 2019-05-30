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


public class bcQuestionnaireObject extends bcObject {
	private final static Logger LOGGER=Logger.getLogger(bcQuestionnaireObject.class);
	private Map<String,Comparable<String>> fieldHm = new HashMap<String, Comparable<String>>();
	
    private String idQuest;
    
    public bcQuestionnaireObject(String pIdQuest) {
    	this.idQuest = pIdQuest;
    }
	
	public void getFeature() {
		String featureSelect = " SELECT * FROM " + getGeneralDBScheme() + ".VC_QUEST_INT_CLUB_ALL WHERE id_quest_int = ?";
		fieldHm = getFeatures2(featureSelect, this.idQuest, false);
	}
	
	public String getValue(String pColumnName) {
		return getFeautureResult(fieldHm, pColumnName);
	}

	
    public String getMessagesHTML(String pFindString, String pType, /*String pState,*/ String p_beg, String p_end) {
        StringBuilder html = new StringBuilder();
        PreparedStatement st = null;
        Connection con = null;
        
        ArrayList<bcFeautureParam> pParam = initParamArray();

        String mySQL = 
        	" SELECT rn, id_ds_message, name_ds_message_type, name_ds_message_owner, " +
        	"        name_ds_recepient, begin_action_date_frmt, end_action_date_frmt, repetitions_count, " +
        	"		 name_ds_message_state, sendings_quantity, error_sendings_quantity, " +
        	"        basis_for_operation, cd_ds_message_type, id_nat_prs " +
        	"   FROM (SELECT ROWNUM rn, id_ds_message, name_ds_message_type, name_ds_message_owner, " +
        	"        		 name_ds_recepient, begin_action_date_frmt, end_action_date_frmt, repetitions_count, " +
        	"		 		 name_ds_message_state, sendings_quantity, error_sendings_quantity, " +
        	"        		 basis_for_operation, cd_ds_message_type, id_nat_prs " +
        	"   		FROM (SELECT m.id_ds_message, m.name_ds_message_type, m.name_ds_message_owner, " +
        	"        				 m.name_ds_recepient, m.begin_action_date_frmt, m.end_action_date_frmt, m.repetitions_count, " +
        	"		 				 m.name_ds_message_state, m.sendings_quantity, m.error_sendings_quantity, " +
        	"        				 m.basis_for_operation, m.cd_ds_message_type, m.id_nat_prs " +
        	"   				FROM (SELECT a.id_ds_message, a.cd_ds_message_type, a.name_ds_message_type, a.name_ds_message_owner, " +
        	"                                a.name_ds_recepient, a.begin_action_date_frmt, " +
        	"			     				 a.end_action_date_frmt, 1 repetitions_count, " +
        	"                                a.cd_ds_message_state, a.name_ds_message_state, " +
        	"                				 a.sendings_quantity, a.error_sendings_quantity, " +
        	"				 				 a.basis_for_operation, a.id_nat_prs " +
        	"							FROM " + getGeneralDBScheme() + ".vc_nat_prs_messages_all a " +
            "          				   WHERE id_quest_int = ? " + 
        	"		   				   UNION ALL " +
        	"		  				  SELECT a.id_sms_message id_ds_message, 'SMS' cd_ds_message_type, 'SMS' name_ds_message_type, full_name_recepient name_ds_message_owner, " +
        	"                                a.recepient name_ds_recepient, a.begin_action_date_frmt begin_action_date_frmt, " +
        	"				 				 NULL end_action_date_frmt, send_count repetitions_count, " +
        	"				 				 cd_sms_state cd_ds_message_state, name_sms_state name_ds_message_state, send_count sendings_quantity, " +
        	"                				 NULL error_sendings_quantity, " +
        	"				 				 NULL basis_for_operation, id_nat_prs " +
        	"							FROM " + getGeneralDBScheme() + ".vc_ds_sms_all a " +
            "          				   WHERE id_quest_int = ? " + 
        	"        				 ) m " +
        	"  			      WHERE 1=1";
        pParam.add(new bcFeautureParam("int", this.idQuest));
        pParam.add(new bcFeautureParam("int", this.idQuest));
        
    	if (!isEmpty(pFindString)) {
    		mySQL = mySQL + 
   				" AND (TO_CHAR(id_ds_message) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(name_ds_message_owner) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(name_ds_recepient) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(begin_action_date_frmt) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(end_action_date_frmt) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(basis_for_operation) LIKE UPPER('%'||?||'%'))";
    		for (int i=0; i<6; i++) {
    		    pParam.add(new bcFeautureParam("string", pFindString));
    		}
    	}
        if (!isEmpty(pType)) {
        	mySQL = mySQL + " AND cd_ds_message_type = ? ";
        	pParam.add(new bcFeautureParam("string", pType));
        }
        pParam.add(new bcFeautureParam("int", p_end));
        pParam.add(new bcFeautureParam("int", p_beg));
        mySQL = mySQL + 
        	"                )" +
        	"        WHERE ROWNUM < ? " + 
	      	" ) WHERE rn >= ?";
        
        boolean hasNatPrsPermission = false;
        boolean hasEmailPermission = false;
        boolean hasOfficePermission = false;
        boolean hasSMSPermission = false;
        
        try{
        	if (isEditMenuPermited("CLIENTS_NATPERSONS")>=0) {
        		hasNatPrsPermission = true;
        	}
        	if (isEditMenuPermited("DISPATCH_MESSAGES_EMAIL")>=0) {
        		hasEmailPermission = true;
        	}
        	if (isEditMenuPermited("DISPATCH_MESSAGES_OFFICE")>=0) {
        		hasOfficePermission = true;
        	}
        	if (isEditMenuPermited("DISPATCH_MESSAGES_SMS")>=0) {
        		hasSMSPermission = true;
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
            	html.append(getBottomFrameTableTH(messageXML, mtd.getColumnName(i)));
            }
            html.append("</tr></thead><tbody>");
            while (rset.next())
            {
                html.append("<tr>");
                for (int i=1; i<=colCount-2; i++) {
                	if ("ID_DS_MESSAGE".equalsIgnoreCase(mtd.getColumnName(i))) {
                		if ("SMS".equalsIgnoreCase(rset.getString("CD_DS_MESSAGE_TYPE")) && hasSMSPermission) {
                			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/dispatch/messages/smsspecs.jsp?id="+rset.getString("ID_DS_MESSAGE"), "", ""));
                		} else if ("EMAIL".equalsIgnoreCase(rset.getString("CD_DS_MESSAGE_TYPE")) && hasEmailPermission) {
                			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/dispatch/messages/email_messagesspecs.jsp?id="+rset.getString("ID_DS_MESSAGE"), "", ""));
                		} else if ("OFFICE".equalsIgnoreCase(rset.getString("CD_DS_MESSAGE_TYPE")) && hasOfficePermission) {
                			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/dispatch/messages/office_messagesspecs.jsp?id="+rset.getString("ID_DS_MESSAGE"), "", ""));
                		} else {
                			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", "", ""));
                		}
                	} else if ("NAME_DS_MESSAGE_OWNER".equalsIgnoreCase(mtd.getColumnName(i)) && hasNatPrsPermission &&
                			!isEmpty(rset.getString("NAME_DS_MESSAGE_OWNER"))) {
                		html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/clients/natpersonspecs.jsp?id="+rset.getString("ID_NAT_PRS"), "", ""));
                	} else if ("NAME_DS_RECEPIENT".equalsIgnoreCase(mtd.getColumnName(i)) && hasNatPrsPermission &&
                			!isEmpty(rset.getString("NAME_DS_RECEPIENT"))) {
                		html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/clients/natpersonspecs.jsp?id="+rset.getString("ID_NAT_PRS"), "", ""));
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

    public String getPostingsHTML(String pFind, String idBkOperationType, String p_beg, String p_end) {
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
        	"                 WHERE id_quest_int = ? ";
        pParam.add(new bcFeautureParam("int", this.idQuest));
        
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
        	if (isEditPermited("CARDS_QUESTIONNAIRE_IMPORT_POSTINGS")>=0) {
        		hasEditPermission = true;
        	}
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
		            	
		            	   String myHyperLink = "../crm/cards/questionnaire_importupdate.jsp?type=posting&id="+this.idQuest+"&id_posting_detail="+rset.getString("ID_POSTING_DETAIL");
		            	   String myDeleteLink = "../crm/cards/questionnaire_importupdate.jsp?type=posting&id="+this.idQuest+"&id_posting_detail="+rset.getString("ID_POSTING_DETAIL")+"&action=remove&process=yes";
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
    }  
    
    public String getClubCardsTasksHTML(String pFindString, String pOperType, String pOperState, String p_beg, String p_end) {
    	bcListCardOperation list = new bcListCardOperation();
    	
    	String pWhereCause = "   WHERE id_quest_int = ? ";
    	
    	ArrayList<bcFeautureParam> pWhereValue = new ArrayList<bcFeautureParam>();
    	pWhereValue.add(new bcFeautureParam("int", this.idQuest));

    	String lDeleteLink = "";
    	String lEditLink = "";
    	
    	return list.getCardOperationsHTML(pWhereCause, pWhereValue, pFindString, pOperType, pOperState, lEditLink, lDeleteLink, "div_data_detail", p_beg, p_end);
    	
    }

}
