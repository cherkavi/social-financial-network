package bc.lists;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import bc.connection.Connector;
import bc.service.bcFeautureParam;

public class bcListCardOperation extends bc.objects.bcObject {
	private final static Logger LOGGER=Logger.getLogger(bcListCardOperation.class);
	
	public bcListCardOperation() {
		
	}

	public String getCardOperationsHTML(String pWhereCause, ArrayList<bcFeautureParam> pWhereValue, String pFindString, String pOperType, String pOperState, String pEditLink, String pDeleteLink, String pDivName, String p_beg, String p_end){
		return getCardOperationsBaseHTML("vc_card_oper_club_all", pWhereCause, pWhereValue, pFindString, pOperType, pOperState, pEditLink, pDeleteLink, pDivName, p_beg, p_end);
	}
	
	public String getTermMessageCardOperationsHTML(String pWhereCause, ArrayList<bcFeautureParam> pWhereValue, String pFindString, String pOperType, String pOperState, String pEditLink, String pDeleteLink, String pDivName, String p_beg, String p_end){
		return getCardOperationsBaseHTML("vc_term_message_card_oper_all", pWhereCause, pWhereValue, pFindString, pOperType, pOperState, pEditLink, pDeleteLink, pDivName, p_beg, p_end);
	}
	
	public String getCardOperationsBaseHTML(String pViewName, String pWhereCause, ArrayList<bcFeautureParam> pWhereValue, String pFindString, String pOperType, String pOperState, String pEditLink, String pDeleteLink, String pDivName, String p_beg, String p_end){
		StringBuilder html = new StringBuilder();
        Connection con = null;
        PreparedStatement st = null;
        boolean hasTaskPermission = false;
        boolean hasCardPermission = false;
        boolean hasEditPermission = isEmpty(pEditLink)?false:true;
        boolean hasDeletePermission = isEmpty(pDeleteLink)?false:true;
        
        ArrayList<bcFeautureParam> pParam = initParamArray();

        String mySQL = 
        	"SELECT rn, cd_card1, name_card_operation_type, " +
            "       /*begin_action_date_frmt, end_action_date_frmt,*/ " +
            "   	basis_for_operation, name_card_oper_state, creation_date_frmt," +
            "       card_serial_number, card_id_issuer, card_id_payment_system, id_card_operation " +
	  		"  FROM (SELECT ROWNUM rn, id_card_operation, " +
	  		"               '<span title=\"'||opr_param||'\">'||" +
  			"               DECODE(cd_card_operation_type, " +
  			"                      'CHANGE_PARAM', '<font color=\"brown\">'||name_card_operation_type||'</font>', " +
  			"                      'SEND_MESSAGE', '<font color=\"black\">'||name_card_operation_type||'</font>', " +
  			"                      'BLOCK_CARD', '<font color=\"red\"><b>'||name_card_operation_type||'</b></font>', " +
  			"                      'ADD_BON', '<font color=\"green\"><b>'||name_card_operation_type||'</b></font>', " +
  			"                      'WRITE_OFF_BON', '<font color=\"blue\"><b>'||name_card_operation_type||'</b></font>', " +
  			"                      'ADD_GOODS_TO_PURSE', '<font color=\"green\">'||name_card_operation_type||'</font>', " +
  			"                      'WRITE_OFF_GOODS_FROM_PURSE', '<font color=\"blue\">'||name_card_operation_type||'</font>', " +
  			"                      'MOVE_BON', '<font color=\"#FF0000;\">'||name_card_operation_type||'</font>', " +
  			"                      'SET_CATEGORIES_ON_PERIOD', '<font color=\"brown\">'||name_card_operation_type||'</font>', " +
			"                      name_card_operation_type)||" +
  			"               '</span>' name_card_operation_type, " +
            "     		    cd_card1, begin_action_date_frmt, end_action_date_frmt, " +
            "     			basis_for_operation, " +
            "               DECODE(cd_card_oper_state, " +
  			"                      'EXECUTED', '<font color=\"green\"><b>'||name_card_oper_state||'</b></font>', " +
  			"                      'EXECUTED_PARTIALLY', '<font color=\"green\">'||name_card_oper_state||'</font>', " +
  			"                      'CARRIED_OUT', '<font color=\"brown\"><b>'||name_card_oper_state||'</b></font>', " +
  			"                      'CARRIED_OUT_PARTIALLY', '<font color=\"brown\">'||name_card_oper_state||'</font>', " +
  			"                      'NEW', '<b>'||name_card_oper_state||'</b>', " +
  			"                      'CANCELED', '<font color=\"gray\">'||name_card_oper_state||'</font>', " +
  			"                      'ERROR', '<font color=\"red\"><b>'||name_card_oper_state||'</b></font>', " +
  			"                      'PREPARED', '<font color=\"blue\">'||name_card_oper_state||'</font>', " +
  			"                      name_card_oper_state) " +
  			"				name_card_oper_state, " +
  			"               creation_date_frmt," +
  			"               card_serial_number, card_id_issuer, card_id_payment_system " +
	  		"          FROM (SELECT id_card_operation, cd_card_operation_type, name_card_operation_type, " +
            "     				    cd_card1, begin_action_date_frmt, end_action_date_frmt, " +
            "     					basis_for_operation, name_card_oper_state, creation_date_frmt, " +
            "                       cd_card_oper_state, opr_param, " +
            "                       card_serial_number, card_id_issuer, card_id_payment_system " +
            "				   FROM " + getGeneralDBScheme() + ". " + pViewName + 
	            (isEmpty(pWhereCause)?" WHERE 1=1 ":pWhereCause) ;
        	if (pWhereValue.size() > 0) {
        		for(int counter=0; counter<=pWhereValue.size()-1;counter++){
        			pParam.add(pWhereValue.get(counter));
        		}
        	}
        	if (!isEmpty(pFindString)) {
        		mySQL = mySQL + 
       				" AND (TO_CHAR(id_card_operation) LIKE UPPER('%'||?||'%') OR " +
       				"      UPPER(begin_action_date_frmt) LIKE UPPER('%'||?||'%') OR " +
       				"      UPPER(end_action_date_frmt) LIKE UPPER('%'||?||'%') OR " +
       				"      UPPER(basis_for_operation) LIKE UPPER('%'||?||'%') OR " +
       				"      UPPER(creation_date_frmt) LIKE UPPER('%'||?||'%'))";
        		for (int i=0; i<5; i++) {
        		    pParam.add(new bcFeautureParam("string", pFindString));
        		}
        	}
            if (!isEmpty(pOperType)) {
            	mySQL = mySQL + " AND cd_card_operation_type = ? ";
            	pParam.add(new bcFeautureParam("string", pOperType));
            }
            if (!isEmpty(pOperState)) {
            	mySQL = mySQL + " AND cd_card_oper_state = ? ";
            	pParam.add(new bcFeautureParam("string", pOperState));
            }
            mySQL = mySQL + 
    			"				   ORDER BY begin_action_date DESC, id_card_operation" +
    			"  ) WHERE ROWNUM < ? " + 
    			"  ) WHERE rn >= ?";
        	pParam.add(new bcFeautureParam("int", p_end));
        	pParam.add(new bcFeautureParam("int", p_beg));
            try{
            	if (isEditMenuPermited("CARDS_CARD_TASKS")>=0) {
            		hasTaskPermission = true;
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
                for (int i=1; i <= colCount-4; i++) {
                	html.append(getBottomFrameTableTH(card_taskXML, mtd.getColumnName(i)));
                }
                if (hasDeletePermission) {
                	html.append("<th>&nbsp;</th>\n");
                }
                if (hasEditPermission) {
                	html.append("<th>&nbsp;</th>\n");
                }
                html.append("</tr></thead><tbody>\n");
                while (rset.next())
                {
                	html.append("<tr>");
                    for (int i=1; i<=colCount-4; i++) {
                    	if (hasTaskPermission && "NAME_CARD_OPERATION_TYPE".equalsIgnoreCase(mtd.getColumnName(i))) {
                   			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/cards/card_taskspecs.jsp?id="+rset.getString("ID_CARD_OPERATION"), "", ""));
                    	} else if (hasCardPermission && "CD_CARD1".equalsIgnoreCase(mtd.getColumnName(i))) {
              	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/cards/clubcardspecs.jsp?id="+rset.getString("CARD_SERIAL_NUMBER")+"&iss="+rset.getString("CARD_ID_ISSUER")+"&paysys="+rset.getString("CARD_ID_PAYMENT_SYSTEM"), "", ""));
             	  		} else {
                    		html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", "", ""));
                    	}

                    }
    	            if (hasDeletePermission) {
    	            	html.append(getDeleteButtonBaseHTML(pDeleteLink+"&id_card_operation="+rset.getString("ID_CARD_OPERATION"), "remove", "yes", "delete.png", buttonXML.getfieldTransl("button_delete", false), (isEmpty(pDivName)?"div_main":pDivName)));
     	            }
    	            if (hasEditPermission) {
    		           	html.append(getEditButtonWitDivHTML(pEditLink+"&id_card_operation="+rset.getString("ID_CARD_OPERATION"), (isEmpty(pDivName)?"div_main":pDivName)));
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
	
	public String getCardOperBasedOnActionsHTML(String pWhereCause, ArrayList<bcFeautureParam> pWhereValue, String pFindString, String pOperType, String pOperState, String pEditLink, String pDeleteLink, String pDivName, String p_beg, String p_end){
		StringBuilder html = new StringBuilder();
        Connection con = null;
        PreparedStatement st = null;
        boolean hasTaskPermission = false;
        boolean hasCardPermission = false;
        boolean hasTerminalPermission = false;
        boolean hasEditPermission = isEmpty(pEditLink)?false:true;
        boolean hasDeletePermission = isEmpty(pDeleteLink)?false:true;
        
        ArrayList<bcFeautureParam> pParam = initParamArray();

        String mySQL = 
        	"SELECT rn, id_term, send_date_frmt date_card_oper_online, cd_card1, name_card_operation_type,  " +
            "       /*begin_action_date_frmt, end_action_date_frmt,*/ " +
            "   	basis_for_operation, name_card_oper_state, " +
            "       card_serial_number, card_id_issuer, card_id_payment_system, id_card_operation " +
	  		"  FROM (SELECT ROWNUM rn, id_card_operation, id_term, send_date_frmt, cd_card1, " +
	  		"               DECODE(cd_card_operation_type, " +
  			"                      'CHANGE_PARAM', '<font color=\"brown\">'||name_card_operation_type||'</font>', " +
  			"                      'SEND_MESSAGE', '<font color=\"black\">'||name_card_operation_type||'</font>', " +
  			"                      'BLOCK_CARD', '<font color=\"red\"><b>'||name_card_operation_type||'</b></font>', " +
  			"                      'ADD_BON', '<font color=\"green\"><b>'||name_card_operation_type||'</b></font>', " +
  			"                      'WRITE_OFF_BON', '<font color=\"blue\"><b>'||name_card_operation_type||'</b></font>', " +
  			"                      'ADD_GOODS_TO_PURSE', '<font color=\"green\">'||name_card_operation_type||'</font>', " +
  			"                      'WRITE_OFF_GOODS_FROM_PURSE', '<font color=\"blue\">'||name_card_operation_type||'</font>', " +
  			"                      'MOVE_BON', '<font color=\"#FF0000;\">'||name_card_operation_type||'</font>', " +
  			"                      'SET_CATEGORIES_ON_PERIOD', '<font color=\"brown\">'||name_card_operation_type||'</font>', " +
			"                      name_card_operation_type) " +
			"			    name_card_operation_type, " +
            "     		    begin_action_date_frmt, end_action_date_frmt, " +
            "     			basis_for_operation, " +
            "               DECODE(cd_card_oper_state, " +
  			"                      'EXECUTED', '<font color=\"green\"><b>'||name_card_oper_state||'</b></font>', " +
  			"                      'EXECUTED_PARTIALLY', '<font color=\"green\">'||name_card_oper_state||'</font>', " +
  			"                      'CARRIED_OUT', '<font color=\"brown\"><b>'||name_card_oper_state||'</b></font>', " +
  			"                      'CARRIED_OUT_PARTIALLY', '<font color=\"brown\">'||name_card_oper_state||'</font>', " +
  			"                      'NEW', '<b>'||name_card_oper_state||'</b>', " +
  			"                      'CANCELED', '<font color=\"gray\">'||name_card_oper_state||'</font>', " +
  			"                      'ERROR', '<font color=\"red\"><b>'||name_card_oper_state||'</b></font>', " +
  			"                      'PREPARED', '<font color=\"blue\">'||name_card_oper_state||'</font>', " +
  			"                      name_card_oper_state) " +
  			"				name_card_oper_state," +
  			"               card_serial_number, card_id_issuer, card_id_payment_system " +
	  		"          FROM (SELECT * " +
            "				   FROM " + getGeneralDBScheme() + ".vc_card_oper_action_all a " +
	            (isEmpty(pWhereCause)?" WHERE 1=1 ":pWhereCause) ;
        	if (pWhereValue.size() > 0) {
        		for(int counter=0; counter<=pWhereValue.size()-1;counter++){
        			pParam.add(pWhereValue.get(counter));
        		}
        	}
        	if (!isEmpty(pFindString)) {
        		mySQL = mySQL + 
       				" AND (TO_CHAR(id_card_operation) LIKE UPPER('%'||?||'%') OR " +
       				"      UPPER(begin_action_date_frmt) LIKE UPPER('%'||?||'%') OR " +
       				"      UPPER(end_action_date_frmt) LIKE UPPER('%'||?||'%') OR " +
       				"      UPPER(basis_for_operation) LIKE UPPER('%'||?||'%')OR " +
       				"      UPPER(send_date_frmt) LIKE UPPER('%'||?||'%'))";
        		for (int i=0; i<5; i++) {
        		    pParam.add(new bcFeautureParam("string", pFindString));
        		}
        	}
            if (!isEmpty(pOperType)) {
            	mySQL = mySQL + " AND cd_card_operation_type = ? ";
            	pParam.add(new bcFeautureParam("string", pOperType));
            }
            if (!isEmpty(pOperState)) {
            	mySQL = mySQL + " AND cd_card_oper_state = ? ";
            	pParam.add(new bcFeautureParam("string", pOperState));
            }
            mySQL = mySQL + 
    			"				   ORDER BY begin_action_date DESC, priority DESC, id_card_operation" +
    			"  ) WHERE ROWNUM < ? " + 
    			"  ) WHERE rn >= ?";
        	pParam.add(new bcFeautureParam("int", p_end));
        	pParam.add(new bcFeautureParam("int", p_beg));
            try{
            	if (isEditMenuPermited("CARDS_CARD_TASKS")>=0) {
            		hasTaskPermission = true;
            	}
            	if (isEditMenuPermited("CARDS_CLUBCARDS")>=0) {
            		hasCardPermission = true;
            	}
            	if (isEditMenuPermited("CLIENTS_TERMINALS")>=0) {
            		hasTerminalPermission = true;
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
                	html.append(getBottomFrameTableTH(card_taskXML, mtd.getColumnName(i)));
                }
                if (hasDeletePermission) {
                	html.append("<th>&nbsp;</th>\n");
                }
                if (hasEditPermission) {
                	html.append("<th>&nbsp;</th>\n");
                }
                html.append("</tr></thead><tbody>\n");
                while (rset.next())
                {
                	html.append("<tr>");
                    for (int i=1; i<=colCount-4; i++) {
                    	if (hasTaskPermission && "NAME_CARD_OPERATION_TYPE".equalsIgnoreCase(mtd.getColumnName(i))) {
                   			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/cards/card_taskspecs.jsp?id="+rset.getString("ID_CARD_OPERATION"), "", ""));
                    	} else if (hasTerminalPermission && "ID_TERM".equalsIgnoreCase(mtd.getColumnName(i))) {
                   			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/clients/terminalspecs.jsp?id="+rset.getString("ID_TERM"), "", ""));
                    	} else if (hasCardPermission && "CD_CARD1".equalsIgnoreCase(mtd.getColumnName(i))) {
              	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/cards/clubcardspecs.jsp?id="+rset.getString("CARD_SERIAL_NUMBER")+"&iss="+rset.getString("CARD_ID_ISSUER")+"&paysys="+rset.getString("CARD_ID_PAYMENT_SYSTEM"), "", ""));
             	  		} else {
                    		html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", "", ""));
                    	}

                    }
    	            if (hasDeletePermission) {
    	            	html.append(getDeleteButtonBaseHTML(pDeleteLink+"&id_task="+rset.getString("ID_CARD_OPERATION"), "remove_task", "yes", "delete.png", buttonXML.getfieldTransl("button_delete", false), (isEmpty(pDivName)?"div_main":pDivName)));
     	            }
    	            if (hasEditPermission) {
    		           	html.append(getEditButtonWitDivHTML(pEditLink+"&id_task="+rset.getString("ID_CARD_OPERATION"), (isEmpty(pDivName)?"div_main":pDivName)));
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
	
    
}
