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

public class bcClubCardOperationObject extends bcObject {
	private final static Logger LOGGER=Logger.getLogger(bcClubCardOperationObject.class);
	
	private Map<String,Comparable<String>> fieldHm = new HashMap<String, Comparable<String>>();
	
	private String idOperation;
	
	public bcClubCardOperationObject(String pIdOperation){
		this.idOperation = pIdOperation;
		getFeature();
	}
	
	private void getFeature() {
		String featureSelect = " SELECT * FROM " + getGeneralDBScheme() + ".VC_CARD_OPER_CLUB_ALL WHERE id_card_operation = ?";
		fieldHm = getFeatures2(featureSelect, this.idOperation, false);
	}
	
	public String getValue(String pColumnName) {
		return getFeautureResult(fieldHm, pColumnName);
	}

	
    public String getActionsHTML(String pFind, String pOperationType, String pOperationState, String p_beg, String p_end) {
        StringBuilder html = new StringBuilder();
        PreparedStatement st = null;
        Connection con = null;
        String fieldList = "";

        ArrayList<bcFeautureParam> pParam = initParamArray();
        
        if ("SEND_MESSAGE".equalsIgnoreCase(pOperationType)) {
        	fieldList = "";
        } else if ("ADD_BON".equalsIgnoreCase(pOperationType)) {
        	fieldList = ", bal_acc_frmt, bal_cur_frmt, date_acc_frmt ";
        } else if ("WRITE_OFF_BON".equalsIgnoreCase(pOperationType)) {
        	fieldList = ", bal_acc_frmt, bal_cur_frmt, date_acc_frmt ";
        } else if ("CHANGE_PARAM".equalsIgnoreCase(pOperationType)) {
        	fieldList = ", name_card_status, name_bon_category, name_disc_category, " + 
        		"bal_acc_frmt, bal_cur_frmt, bal_bon_per_frmt, bal_disc_per_frmt, " +
        		"date_acc_frmt, date_mov_frmt, id_card_status, id_bon_category, id_disc_category ";
        } else if ("SET_CATEGORIES_ON_PERIOD".equalsIgnoreCase(pOperationType)) {
        	fieldList = ", name_card_status, name_bon_category, name_disc_category, " +
        		"id_card_status, id_bon_category, id_disc_category ";
        } else if ("BLOCK_CARD".equalsIgnoreCase(pOperationType)) {
        	fieldList = "";
        } else if ("ADD_GOODS_TO_PURSE".equalsIgnoreCase(pOperationType)) {
        	fieldList = ", id_card_purse, value_card_purse_act_frmt ";
        } else if ("WRITE_OFF_GOODS_FROM_PURSE".equalsIgnoreCase(pOperationType)) {
        	fieldList = ", id_card_purse, value_card_purse_act_frmt ";
        }
        String mySQL = 
        	" SELECT id_card_action, state_action_tsl, id_term, send_date_frmt, " +
            "		 acknowledgement_date_frmt, id_term_card_req_telgr, card_nt_icc " + fieldList +
            "   FROM (SELECT ROWNUM rn, id_card_action, " +
            "                DECODE(state_action, " +
			"                       'C', '<font color=\"blue\"><b>'||state_action_tsl||'</b></font>', " +
			"                       'E', '<font color=\"red\"><b>'||state_action_tsl||'</b></font>', " +
			"                       'N', '<font color=\"black\"><b>'||state_action_tsl||'</b></font>', " +
			"                       'S', '<font color=\"green\"><b>'||state_action_tsl||'</b></font>', " +
			"                       state_action_tsl" +
			"                ) state_action_tsl, " +
		  	"                id_term, send_date_frmt, " +
            "				 acknowledgement_date_frmt, id_term_card_req_telgr, card_nt_icc " + fieldList +
            "           FROM (SELECT id_card_action, state_action_tsl, state_action, id_term, send_date_frmt, " +
            "					     acknowledgement_date_frmt, id_term_card_req_telgr, card_nt_icc " + fieldList +
            "                   FROM " + getGeneralDBScheme() + ".vc_card_oper_action_all "+
            "                  WHERE id_card_operation = ? ";
        pParam.add(new bcFeautureParam("int", this.idOperation));

        if (!(pFind == null || "".equalsIgnoreCase(pFind))) {
        	mySQL = mySQL + 
	        	" AND (TO_CHAR(id_card_action) LIKE UPPER('%'||?||'%') OR " +
				"      TO_CHAR(id_term) LIKE UPPER('%'||?||'%') OR " +
				"      UPPER(send_date_frmt) LIKE UPPER('%'||?||'%') OR " +
				"      UPPER(acknowledgement_date_frmt) LIKE UPPER('%'||?||'%') OR " +
				"      TO_CHAR(id_term_card_req_telgr) LIKE UPPER('%'||?||'%') OR " +
				"      TO_CHAR(card_nt_icc) LIKE UPPER('%'||?||'%'))";
        	for (int i=0; i<6; i++) {
        	    pParam.add(new bcFeautureParam("string", pFind));
        	}
        }
        if (!isEmpty(pOperationState)) {
        	mySQL = mySQL + " AND state_action = ? ";
        	pParam.add(new bcFeautureParam("string", pOperationState));
        }
        pParam.add(new bcFeautureParam("int", p_end));
        pParam.add(new bcFeautureParam("int", p_beg));
  		mySQL = mySQL + 
            "         ) WHERE ROWNUM < ?" + 
            " ) WHERE rn >= ?";
        
        boolean hasTerminalPermission = false;
        boolean hasTelegramPermission = false;
        boolean hasCardStatusPermission = false;
        
        try{
        	if (isEditMenuPermited("CLIENTS_TERMINALS")>=0) {
        		hasTerminalPermission = true;
        	}
        	if (isEditMenuPermited("CLIENTS_TERMSES")>=0) {
        		hasTelegramPermission = true;
        	}
        	if (isEditMenuPermited("CARDS_CARDSETTING")>=0) {
        		hasCardStatusPermission = true;
        	}
        	
        	LOGGER.debug(prepareSQLToLog(mySQL, pParam));
        	con = Connector.getConnection(getSessionId());
        	st = con.prepareStatement(mySQL);
        	st = prepareParam(st, pParam);
        	ResultSet rset = st.executeQuery();
            ResultSetMetaData mtd = rset.getMetaData();

            int colCount = mtd.getColumnCount();
            if ("CHANGE_PARAM".equalsIgnoreCase(pOperationType)|| 
            		"SET_CATEGORIES_ON_PERIOD".equalsIgnoreCase(pOperationType)) {
            	colCount = colCount - 3;
            }
            html.append(getBottomFrameTable());
            html.append("<tr>");

            for (int i=1; i <= colCount; i++) {
            	html.append(getBottomFrameTableTH(card_taskXML, mtd.getColumnName(i)));
            }
            html.append("</tr></thead><tbody>");
            while (rset.next())
            {
                html.append("<tr>");
                for (int i=1; i<=colCount; i++) {
                	if (hasTerminalPermission && "ID_TERM".equalsIgnoreCase(mtd.getColumnName(i))) {
          	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/clients/terminalspecs.jsp?id="+rset.getString("ID_TERM"), "", ""));
         	  		} else if (hasTelegramPermission && "ID_TERM_CARD_REQ_TELGR".equalsIgnoreCase(mtd.getColumnName(i))) {
          	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/clients/telegramspecs.jsp?id="+rset.getString("ID_TERM_CARD_REQ_TELGR"), "", ""));
         	  		} else if (hasCardStatusPermission && "NAME_CARD_STATUS".equalsIgnoreCase(mtd.getColumnName(i))) {
          	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/cards/cardsetting.jsp?id_status="+rset.getString("ID_CARD_STATUS"), "", ""));
         	  		} else if (hasCardStatusPermission && "NAME_BON_CATEGORY".equalsIgnoreCase(mtd.getColumnName(i)) &&
         	  				!isEmpty(rset.getString("ID_BON_CATEGORY"))) {
          	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/cards/cardsettingspecs.jsp?id_category="+rset.getString("ID_BON_CATEGORY"), "", ""));
         	  		} else if (hasCardStatusPermission && "NAME_DISC_CATEGORY".equalsIgnoreCase(mtd.getColumnName(i)) &&
         	  				!isEmpty(rset.getString("ID_DISC_CATEGORY"))) {
          	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/cards/cardsettingspecs.jsp?id_category="+rset.getString("ID_DISC_CATEGORY"), "", ""));
         	  		} else if ("ID_CARD_PURSE".equalsIgnoreCase(mtd.getColumnName(i))) {
          	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/cards/clubcardpursespecs.jsp?id="+rset.getString("ID_CARD_PURSE"), "", ""));
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
        PreparedStatement st = null;
        Connection con = null;

        ArrayList<bcFeautureParam> pParam = initParamArray();
        
        String mySQL = 	
        	" SELECT rn, id_posting_detail, operation_date_frmt operation_date," +
        	"        oper_number id_bk_operation_scheme_line, " +
        	"        debet_cd_bk_account debet_id_bk_account,  " +
        	"        credit_cd_bk_account credit_id_bk_account, " +
        	"		 name_currency, entered_amount_frmt entered_amount, assignment_posting, " +
        	"        id_clearing_line, " +
        	"        debet_id_bk_account debet_id_bk_account2, debet_cd_bk_account, " +
        	"        credit_id_bk_account credit_id_bk_account2, credit_cd_bk_account, " +
        	"        id_bk_operation_scheme_line id_bk_operation_scheme_line2, id_posting_line " +
        	"  FROM (SELECT ROWNUM rn, id_posting_detail, id_posting_line, operation_date_frmt, name_currency," +
        	"               id_bk_operation_scheme_line, oper_number, " +
        	"		        entered_amount_frmt, assignment_posting, " +
        	"               id_clearing_line, " +
        	"               debet_id_bk_account, debet_cd_bk_account_frmt debet_cd_bk_account, " +
        	"               credit_id_bk_account, credit_cd_bk_account_frmt credit_cd_bk_account " +
        	"          FROM (SELECT * " +
        	"                  FROM "+getGeneralDBScheme() + ".vc_acc_posting_detail_club_all " +
        	"                  WHERE id_card_operation = ? ";
        pParam.add(new bcFeautureParam("int", this.idOperation));
        
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
        	"         ) WHERE ROWNUM < ? " + 
        	" ) WHERE rn >= ?";
        boolean hasPostingPermission = false;
        //boolean hasAccDocPermission = false;
        boolean hasBKAccountPermission = false;
        boolean hasSchemePermission = false;
        
        boolean hasEditPermission = false;
        try{
        	if (isEditPermited("CARDS_CARD_TASKS_POSTINGS")>=0) {
        		hasEditPermission = true;
        	}
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
            for (int i=1; i <= colCount-7; i++) {
            	html.append(getBottomFrameTableTH(postingXML, mtd.getColumnName(i)));
            }
            if (hasEditPermission) {
            	html.append("<th>&nbsp;</th><th>&nbsp;</th>\n");
	        }
            html.append("</tr></thead><tbody>\n");
            while (rset.next())
            {
                html.append("<tr>\n");
                for (int i=1; i <= colCount-7; i++) {
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
		            	
		           	   String myHyperLink = "../crm/cards/card_tasksupdate.jsp?type=posting&id="+this.idOperation+"&id_posting_detail="+rset.getString("ID_POSTING_DETAIL");
		           	   String myDeleteLink = "../crm/cards/card_tasksupdate.jsp?type=posting&id="+this.idOperation+"&id_posting_detail="+rset.getString("ID_POSTING_DETAIL")+"&action=remove&process=yes";
		               html.append(getDeleteButtonHTML(myDeleteLink, card_taskXML.getfieldTransl("LAB_DELETE_ONE_POSTING", false), rset.getString("DEBET_CD_BK_ACCOUNT")+ " - " + rset.getString("CREDIT_CD_BK_ACCOUNT")));
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
    } //getCardPostingsHTML
    
}
