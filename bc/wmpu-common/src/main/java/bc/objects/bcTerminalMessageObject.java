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


public class bcTerminalMessageObject extends bcObject {
	private final static Logger LOGGER=Logger.getLogger(bcTerminalMessageObject.class);
	private Map<String,Comparable<String>> fieldHm = new HashMap<String, Comparable<String>>();
	
	private String idMessage;
	
	public bcTerminalMessageObject(String pIdMessage) {
		this.idMessage = pIdMessage;
		getFeature();
	}
	
	public void getFeature() {
		String featureSelect = " SELECT * FROM " + getGeneralDBScheme() + ".VC_TERM_MESSAGES_CLUB_ALL WHERE id_term_message = ?";
		fieldHm = getFeatures2(featureSelect, this.idMessage, false);
	}
	
	public String getValue(String pColumnName) {
		return getFeautureResult(fieldHm, pColumnName);
	}

    public String getTermMessagesReceiverHTML(String pIsSelected, String pFindString, String pState, String pIsArchive, String p_beg, String p_end) {
        StringBuilder html = new StringBuilder();
        Connection con = null;
        PreparedStatement st = null;

        ArrayList<bcFeautureParam> pParam = initParamArray();

        String mySQL = 
        	" SELECT is_selected, rn, id_term, repetitions_count, sendings_quantity, last_send_date_frmt, " +
            "		 name_term_message_state, is_archive_tsl, id_term_message_receiver, " +
            "        id_term_message, sendings_quantity_src " +
            "   FROM (SELECT ROWNUM rn, id_term, repetitions_count, sendings_quantity, last_send_date_frmt, " +
            "			 	 name_term_message_state, is_archive_tsl, is_selected, " +
            "                id_term_message_receiver, id_term_message, sendings_quantity_src " +
            "           FROM (SELECT id_term, repetitions_count, " +
            "						 CASE WHEN NVL(sendings_quantity, 0) > 0 " +
            "                             THEN '<b><font color=\"blue\">'||sendings_quantity||'</font></b>' " +
            "                             ELSE TO_CHAR(sendings_quantity)" +
            "                        END sendings_quantity, sendings_quantity sendings_quantity_src, " +
            "						 last_send_date_frmt, " +
            "						 DECODE(cd_term_message_state, " +
  		  	"                      	  		'PREPARED', '<b><font color=\"black\">'||name_term_message_state||'</font></b>', " +
			"                      			'NEW', '<font color=\"black\">'||name_term_message_state||'</font>', " +
			"                      			'EXECUTED', '<font color=\"green\">'||name_term_message_state||'</font>', " +
			"                      			'EXECUTED_PARTIALLY', '<font color=\"darkgreen\">'||name_term_message_state||'</font>', " +
			"                      			'ERROR', '<b><font color=\"red\">'||name_term_message_state||'</font></b>', " +
			"                      			'CARRIED_OUT_PARTIALLY', '<b><font color=\"darkgray\">'||name_term_message_state||'</font></b>', " +
			"                      			'CARRIED_OUT', '<font color=\"gray\">'||name_term_message_state||'</font>', " +
			"                      			'CANCELED', '<font color=\"blue\">'||name_term_message_state||'</font>', " +
			"                      			name_term_message_state" +
			"                  		 ) name_term_message_state, " +
            "						 DECODE(is_archive, " +
			"                           	'Y', '<b><font color=\"red\">'||is_archive_tsl||'</font></b>', " +
			"                           	'N', '<b><font color=\"blue\">'||is_archive_tsl||'</font></b>', " +
			"                           	is_archive_tsl" +
			"                  		 ) is_archive_tsl, is_selected, id_term_message_receiver, id_term_message " +
            "                   FROM (SELECT a.id_term, m.repetitions_count, m.sendings_quantity, m.last_send_date_frmt," +
            "                                m.cd_term_message_state, m.name_term_message_state, " +
            "                                m.is_archive, m.is_archive_tsl, m.id_term_message_receiver, m.id_term_message, " +
            "                                CASE WHEN m.id_term_message_receiver IS NULL" +
            "                                     THEN 'D'" +
            "                                     ELSE m.is_selected" +
            "                                END is_selected " +
            "                           FROM " + getGeneralDBScheme() + ".vc_term_short_club_all a " +
            "                           LEFT JOIN  (SELECT id_term, id_term_message_receiver, id_term_message, " +
            "											   repetitions_count, sendings_quantity, last_send_date_frmt, " +
            "											   cd_term_message_state, name_term_message_state, is_archive, " +
            "											   is_archive_tsl, is_selected " +
            "                                         FROM " + getGeneralDBScheme() + ".vc_term_message_receiver_all " +
            "                                        WHERE id_term_message = ? " + 
            "                                      ) m " +
            "                            ON (a.id_term = m.id_term))" +
            "                          WHERE 1=1 ";
        pParam.add(new bcFeautureParam("int", this.idMessage));
        
    	if (!isEmpty(pFindString)) {
    		mySQL = mySQL + 
   				" AND (TO_CHAR(id_term) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(last_send_date_frmt) LIKE UPPER('%'||?||'%'))";
    		for (int i=0; i<2; i++) {
    		    pParam.add(new bcFeautureParam("string", pFindString));
    		}
    	}
        if (!isEmpty(pState)) {
        	mySQL = mySQL + " AND cd_term_message_state = ? ";
        	pParam.add(new bcFeautureParam("string", pState));
        }
        if (!isEmpty(pIsArchive)) {
        	mySQL = mySQL + " AND is_archive = ? ";
        	pParam.add(new bcFeautureParam("string", pIsArchive));
        }
        if (!isEmpty(pIsSelected)) {
        	if ("Y".equalsIgnoreCase(pIsSelected)) {
        		mySQL = mySQL + " AND is_selected = ? ";
        		pParam.add(new bcFeautureParam("string", pIsSelected));
        	} else {
        		mySQL = mySQL + " AND is_selected <> ? ";
        		pParam.add(new bcFeautureParam("string", pIsSelected));
        	}
        }
        pParam.add(new bcFeautureParam("int", p_end));
        pParam.add(new bcFeautureParam("int", p_beg));
        mySQL = mySQL +
            "                  ORDER BY DECODE(is_selected, 'Y', 1, 'N', 2, 3), id_term" +
            "        )  WHERE ROWNUM < ? " + 
            " ) WHERE rn >= ?";
        
        boolean hasTerminalPermission = false;
        boolean hasEditPermission = false;
        
        String myFont = "";
        String myBGColor = "";
        
        try{
       	 	if (isEditPermited("DISPATCH_MESSAGES_TERM_RECEIVERS")>0) {
       	 		hasEditPermission = true;
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
            if (hasEditPermission) {
	           	 html.append("<form method=\"post\" name=\"updateForm\" id=\"updateForm\" action=\"../crm/dispatch/messages/term_messagesupdate.jsp\">\n");
	           	 html.append("<input type=\"hidden\" name=\"type\" value=\"receiver\">\n");
	           	 html.append("<input type=\"hidden\" name=\"action\" value=\"set\">\n");
	           	 html.append("<input type=\"hidden\" name=\"process\" value=\"yes\">\n");
	           	 html.append("<input type=\"hidden\" name=\"id\" value=\"" + this.idMessage + "\">\n");
            }
            
            html.append(getBottomFrameTable());
            html.append("<tr>");
            for (int i=1; i <= colCount-3; i++) {
           	 	String colName = mtd.getColumnName(i);
                if ("is_selected".equalsIgnoreCase(colName)) {
                    if (hasEditPermission) {
                      	html.append("<th>"+ //buttonXML.getfieldTransl( "button_select_all", false)+"<br>"+
                      		"<input id=\"mainCheck\" name=\"mainCheck\" type=\"checkbox\" onclick=\"CheckAll(this,'chb_id')\" title=\"" + buttonXML.getfieldTransl( "button_select_all", false) + "\"></th>\n");
                    } else {
                   	 	html.append(getBottomFrameTableTH(messageXML, colName));
                    } 
                } else {
               	 	html.append(getBottomFrameTableTH(messageXML, mtd.getColumnName(i)));
                }
            }
            if (hasEditPermission) {
                html.append("<th>&nbsp;</th>\n");
            }
            html.append("</tr></thead><tbody>");
            if (hasEditPermission) {
             	html.append("<tr>");
             	html.append("<td colspan=\"9\" align=\"center\">");
             	html.append(getSubmitButtonAjax2("../crm/dispatch/messages/term_messagesupdate.jsp", "button_data_selector"));
             	html.append("</td>");
             	html.append("</tr>");
            }
            
            int rowCount = 0;
            while (rset.next()) {
             	rowCount++;
                String idReceiver="id_receiver_"+rset.getString("ID_TERM_MESSAGE_RECEIVER");
                String idTerm="id_term_"+rset.getString("ID_TERM");
                String tprvCheck="prv_"+idReceiver;
                String tCheck="chb_"+idReceiver;
                String tTermCheck="chb_"+idTerm;
                
                if("Y".equalsIgnoreCase(rset.getString("IS_SELECTED")) ||
                		"N".equalsIgnoreCase(rset.getString("IS_SELECTED"))){
                	myFont = "<b>";
                	myBGColor = selectedBackGroundStyle;
                } else {
                	myFont = "";
                	myBGColor = "";
                }
            	html.append("<tr>");
            	if (hasEditPermission) {
	                if ("Y".equalsIgnoreCase(rset.getString("IS_SELECTED"))){
	                	html.append("<td " + myBGColor + " align=\"center\"><INPUT  type=\"checkbox\" value = \"Y\" name="+tCheck+" id="+tCheck+" checked onclick=\"return CheckCB(this);\"></td>\n");
                		html.append("<INPUT type=hidden  value=\"Y\" name="+tprvCheck+">");
	                	/*if ("0".equalsIgnoreCase(rset.getString("sendings_quantity_src"))){
	                		html.append("<td " + myBGColor + " align=\"center\"><INPUT  type=\"checkbox\" value = \"Y\" name="+tCheck+" id="+tCheck+" checked onclick=\"return CheckCB(this);\"></td>\n");
	                		html.append("<INPUT type=hidden  value=\"Y\" name="+tprvCheck+">");
	                	} else {
	                		html.append("<td " + myBGColor + " align=\"center\"><INPUT  type=\"checkbox\" value = \"Y\" name=\"selected_"+ rset.getString("ID_TERM")+ "\" id=\"selected_"+ rset.getString("ID_TERM")+ "\" checked disabled></td>\n");
	                	}*/
	                } else if ("N".equalsIgnoreCase(rset.getString("IS_SELECTED"))){
   	                	html.append("<td " + myBGColor + " align=\"center\"><INPUT  type=\"checkbox\" value = \"Y\" name="+tCheck+" id="+tCheck+" onclick=\"return CheckCB(this);\"></td>\n");
                   	} else { 
	                    html.append("<td align=\"center\"><INPUT  type=\"checkbox\" value = \"Y\" name="+tTermCheck+" id="+tTermCheck+" onclick=\"return CheckCB(this);\"></td>\n");
	                }
            	} else {
            		if ("Y".equalsIgnoreCase(rset.getString("IS_SELECTED"))){
	                  	html.append("<td " + myBGColor + " align=\"center\"><INPUT  type=\"checkbox\" value = \"Y\" name="+tCheck+" id="+tCheck+" checked disabled></td>\n");
	                  	html.append("<INPUT type=hidden  value=\"Y\" name="+tprvCheck+">");
	                } else { 
	                    html.append("<td align=\"center\"><INPUT  type=\"checkbox\" value = \"Y\" name="+tCheck+" id="+tCheck+" disabled></td>\n");
	                }
            	}
          	  	for (int i=2; i <= colCount-3; i++) {
          	  		if ("ID_TERM".equalsIgnoreCase(mtd.getColumnName(i)) && hasTerminalPermission) {
             	  		html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/clients/terminalspecs.jsp?id="+rset.getString("ID_TERM"), myFont, myBGColor));
      	  			} else {
              	  		html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", myFont, myBGColor));
          	  		}
          	  	}
                if (hasEditPermission) {
                	if ("Y".equalsIgnoreCase(rset.getString("IS_SELECTED")) ||
                			"N".equalsIgnoreCase(rset.getString("IS_SELECTED"))) {
                		String myHyperLink = "../crm/dispatch/messages/term_message_receiverspecs.jsp?id="+rset.getString("ID_TERM_MESSAGE_RECEIVER");
                		html.append(getEditButtonHTML(myHyperLink));
                	} else {
                        html.append("<td>&nbsp;</td>\n");
                	}
                }
          	  	html.append("</tr>\n");
            }
            if (hasEditPermission) {
             	html.append("<tr>");
             	html.append("<td colspan=\"9\" align=\"center\">");
             	html.append(getSubmitButtonAjax2("../crm/dispatch/messages/term_messagesupdate.jsp", "button_data_selector"));
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
    } // getTermMessagesActionsHTML

    public String getTermMessagesSendHTML(String pFindString, String p_beg, String p_end) {
        StringBuilder html = new StringBuilder();
        Connection con = null;
        PreparedStatement st = null;
        ArrayList<bcFeautureParam> pParam = initParamArray();

        String mySQL = 
        	" SELECT rn, id_term, text_term_message text_message, send_date_frmt, id_telgr, " +
        	"        id_term_message_send " +
            "   FROM (SELECT ROWNUM rn, id_term, text_term_message, send_date_frmt, " +
            "                id_telgr, id_term_message_send " +
            "           FROM (SELECT id_term, text_term_message, send_date_frmt, " +
            "                        id_telgr, id_term_message_send " +
            "                   FROM " + getGeneralDBScheme() + ".vc_term_message_send_all " +
            "                  WHERE id_term_message = ? ";
        pParam.add(new bcFeautureParam("int", this.idMessage));
        
    	if (!isEmpty(pFindString)) {
    		mySQL = mySQL + 
   				" AND (TO_CHAR(id_term) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(text_term_message) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(send_date_frmt) LIKE UPPER('%'||?||'%') OR " +
   				"      TO_CHAR(id_telgr) LIKE UPPER('%'||?||'%'))";
    		for (int i=0; i<4; i++) {
    		    pParam.add(new bcFeautureParam("string", pFindString));
    		}
    	}
    	pParam.add(new bcFeautureParam("int", p_end));
    	pParam.add(new bcFeautureParam("int", p_beg));
    	mySQL = mySQL +
            "                  ORDER BY send_date desc" +
            "        ) WHERE ROWNUM < ? " + 
            " ) WHERE rn >= ?";
        
        boolean hasTermSesPermission = false;
        boolean hasTerminalPermission = false;
        
        try{
        	if (isEditMenuPermited("CLIENTS_TERMSES")>=0) {
        		hasTermSesPermission = true;
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
            for (int i=1; i <= colCount-1; i++) {
            	html.append(getBottomFrameTableTH(messageXML, mtd.getColumnName(i)));
            }
            html.append("</tr></thead><tbody>");
            
            while (rset.next())
            {
            	html.append("<tr>");
          	  	for (int i=1; i <= colCount-1; i++) {
          	  		if ("ID_TELGR".equalsIgnoreCase(mtd.getColumnName(i)) && hasTermSesPermission) {
             	  		html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/clients/telegramspecs.jsp?id="+rset.getString("ID_TELGR"), "", ""));
          	  		} else if ("ID_TERM".equalsIgnoreCase(mtd.getColumnName(i)) && hasTerminalPermission) {
          	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/clients/terminalspecs.jsp?id="+rset.getString("ID_TERM"), "", ""));
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
    } // getTermMessagesActionsHTML
    
    public String getClubCardsTasksHTML(String pFindString, String pOperType, String pOperState, String p_beg, String p_end) {
    	bcListCardOperation list = new bcListCardOperation();
    	
    	String pWhereCause = "   WHERE id_term_message = ? ";
    	
    	ArrayList<bcFeautureParam> pWhereValue = new ArrayList<bcFeautureParam>();
    	pWhereValue.add(new bcFeautureParam("int", this.idMessage));

    	String lDeleteLink = "";
    	String lEditLink = "";
    	
    	return list.getTermMessageCardOperationsHTML(pWhereCause, pWhereValue, pFindString, pOperType, pOperState, lEditLink, lDeleteLink, "div_data_detail", p_beg, p_end);
    	
    }
	
}
