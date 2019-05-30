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

public class bcOfficeMessageObject extends bcObject {
	private final static Logger LOGGER=Logger.getLogger(bcOfficeMessageObject.class);
	private Map<String,Comparable<String>> fieldHm = new HashMap<String, Comparable<String>>();
	
	private String idMessage;
	
	public bcOfficeMessageObject(String pIdMessage) {
		this.idMessage = pIdMessage;
		getFeature();
	}
	
	private void getFeature() {
		String mySQL = "SELECT * FROM " + getGeneralDBScheme() + ".VC_DS_MESSAGES_CLUB_ALL WHERE id_ds_message = ?";
        fieldHm = getFeatures2(mySQL, this.idMessage, false);
	}
	
	public String getValue(String pColumnName) {
		return getFeautureResult(fieldHm, pColumnName);
	}

	public String getMessageSendHTML(String pFindString, String pState, String p_beg, String p_end) {
		ArrayList<bcFeautureParam> pParam = initParamArray();

		String mySQL = 
	    		" SELECT rn, name_ds_message_send_state, send_date_frmt, email, " +
	    		"		 title_ds_message, cd_ds_message_send_state, id_ds_message_send " +
	            "   FROM (SELECT ROWNUM rn, name_ds_message_send_state, send_date_frmt, email, " +
	            "				 title_ds_message, cd_ds_message_send_state, id_ds_message_send " +
	            "           FROM (SELECT DECODE(cd_ds_message_send_state, " +
	  		  	"                      			'N', '<font color=\"black\">'||name_ds_message_send_state||'</font>', " +
				"                      			'S', '<font color=\"green\">'||name_ds_message_send_state||'</font>', " +
				"                      			'C', '<font color=\"blue\">'||name_ds_message_send_state||'</font>', " +
				"                      			'E', '<b><font color=\"red\">'||name_ds_message_send_state||'</font></b>', " +
				"                      			name_ds_message_send_state" +
				"						 ) name_ds_message_send_state, cd_ds_message_send_state, " +
				"						 send_date_frmt, email, title_ds_message, id_ds_message_send " +
	   		 	"                   FROM " + getGeneralDBScheme() + ".vc_ds_messages_send_all " +
	   		 	"                  WHERE id_ds_message = ? ";
		pParam.add(new bcFeautureParam("int", this.idMessage));
		
    	if (!isEmpty(pFindString)) {
    		mySQL = mySQL + 
   				" AND (UPPER(send_date_frmt) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(email) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(title_ds_message) LIKE UPPER('%'||?||'%'))";
    		for (int i=0; i<3; i++) {
    		    pParam.add(new bcFeautureParam("string", pFindString));
    		}
    	}
        if (!isEmpty(pState)) {
        	mySQL = mySQL + " AND cd_ds_message_send_state = ? ";
        	pParam.add(new bcFeautureParam("string", pState));
        }
        pParam.add(new bcFeautureParam("int", p_end));
        pParam.add(new bcFeautureParam("int", p_beg));
    	mySQL = mySQL +
	   		 	"                  ORDER BY send_date DESC) "+
	   		 	"         WHERE ROWNUM < ? " + 
	   		 	" ) WHERE rn >= ?";
   		
        StringBuilder html = new StringBuilder();
        Connection con = null;
        PreparedStatement st = null;
        String myFont = "";
        try{
        	LOGGER.debug(prepareSQLToLog(mySQL, pParam));
        	con = Connector.getConnection(getSessionId());;
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
            html.append("</tr></thead><tbody>\n");
            while (rset.next())
            {
                html.append("<tr>\n");
                if ("E".equalsIgnoreCase(rset.getString("CD_DS_MESSAGE_SEND_STATE"))) {
                	myFont = "<font color=\"red\">";
                } else {
                	myFont = "";
                }
                for (int i=1; i<=colCount-2; i++) {
                	html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", myFont, ""));
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
    } // getMessageSendHTML

    public String getAttachedFilesHTML(boolean hasEditPermission) {
        StringBuilder html = new StringBuilder();
        Connection con = null;
        PreparedStatement st = null;
        ArrayList<bcFeautureParam> pParam = initParamArray();

        String mySQL = 
        	" SELECT id_ds_message_file, file_name, stored_file_name " +
            "   FROM " + getGeneralDBScheme() + ".vc_ds_messages_files_all" +
            "  WHERE id_ds_message = ? " +
            "  ORDER BY file_name";
        pParam.add(new bcFeautureParam("int", this.idMessage));
        
        try{
        	LOGGER.debug(prepareSQLToLog(mySQL, pParam));
        	con = Connector.getConnection(getSessionId());
        	st = con.prepareStatement(mySQL);
        	st = prepareParam(st, pParam);
        	ResultSet rset = st.executeQuery();
            
            while (rset.next())
            {
          	  	html.append(rset.getString("file_name") + 
   	  				"&nbsp;&nbsp;<a href=\"../FileSender?FILENAME=" + URLEncoder.encode(rset.getString("stored_file_name"),"UTF-8") + "\" " +
					"  title=\"" + buttonXML.getfieldTransl("open", false) + "\" target=_blank>" +
					"<img vspace=\"0\" hspace=\"0\" src=\"../images/oper/small/open.gif\" align=\"top\" style=\"border: 0px;\"> " +
					"</a>");
      	  		if (hasEditPermission) {
       	  			html.append("&nbsp;<a href=\"#\" onclick=\"if (window.confirm('" + documentXML.getfieldTransl("l_remove_file", false) + " \\\'" + rset.getString("file_name") + "\\\'?')) {ajaxpage('../crm/dispatch/messages/email_messagesupdate.jsp?id=" + this.idMessage + "&type=general&process=yes&action=remove_src&id_file=" + rset.getString("ID_DS_MESSAGE_FILE") + "', 'div_main')}\" " +
  						" title=\"" + buttonXML.getfieldTransl("button_delete", false) + "\"> " + 
  						"<img vspace=\"0\" hspace=\"0\" src=\"../images/oper/small/delete.gif\" align=\"top\" style=\"border: 0px;\">" +
      	  				"</a>");
       	  		}
       	  		html.append("<br>");
            }
        }
         // try
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

    public String getMessagesReceiverHTML(String pFindString, String pDispatchKind, String pState, String pIsArchive, String p_beg, String p_end) {
        StringBuilder html = new StringBuilder();
        Connection con = null;
        PreparedStatement st = null;

        ArrayList<bcFeautureParam> pParam = initParamArray();

        String mySQL = 
        	" SELECT name_ds_receiver_dispatch_kind, receiver_email, sendings_quantity, last_send_date_frmt,  " +
            "		 name_ds_message_state, is_archive_tsl, is_selected, id_ds_message_receiver " +
            "   FROM (SELECT ROWNUM rn, name_ds_receiver_dispatch_kind, receiver_email, name_ds_message_state,  " +
            "			 	 sendings_quantity, last_send_date_frmt, is_archive_tsl, is_selected, id_ds_message_receiver " +
            "           FROM (SELECT DECODE(cd_ds_receiver_dispatch_kind, " +
			"                               'CLIENT', '<font color=\"green\">'||name_ds_receiver_dispatch_kind||'</font>', " +
			"                               'PARTNER', '<font color=\"blue\">'||name_ds_receiver_dispatch_kind||'</font>', " +
			"                               'SYSTEM', '<font color=\"red\"><b>'||name_ds_receiver_dispatch_kind||'</b></font>', " +
			"                               'TRAINING', '<font color=\"darkgray\">'||name_ds_receiver_dispatch_kind||'</font>', " +
			"                             	'LG_PROMOTER', '<font color=\"black\">'||name_ds_receiver_dispatch_kind||'</font>', " +
		  	"                               'UNKNOWN', '', " +
			"                              name_ds_receiver_dispatch_kind" +
			"                        ) name_ds_receiver_dispatch_kind, receiver_email,  " +
            "						 CASE WHEN NVL(sendings_quantity, 0) > 0 " +
            "                             THEN '<b><font color=\"black\">'||sendings_quantity||'</font></b>' " +
            "                             ELSE TO_CHAR(sendings_quantity)" +
            "                        END sendings_quantity, " +
            "						 last_send_date_frmt,  " +
            "						 DECODE(cd_ds_message_state, " +
  		  	"                      	  		'PREPARED', '<b><font color=\"black\">'||name_ds_message_state||'</font></b>', " +
			"                      			'NEW', '<font color=\"black\">'||name_ds_message_state||'</font>', " +
			"                      			'EXECUTED', '<font color=\"green\">'||name_ds_message_state||'</font>', " +
			"                      			'EXECUTED_PARTIALLY', '<font color=\"darkgreen\">'||name_ds_message_state||'</font>', " +
			"                      			'ERROR', '<b><font color=\"red\">'||name_ds_message_state||'</font></b>', " +
			"                      			'CARRIED_OUT_PARTIALLY', '<b><font color=\"darkgray\">'||name_ds_message_state||'</font></b>', " +
			"                      			'CARRIED_OUT', '<font color=\"gray\">'||name_ds_message_state||'</font>', " +
			"                      			'CANCELED', '<font color=\"blue\">'||name_ds_message_state||'</font>', " +
			"                      			name_ds_message_state" +
			"                  		 ) name_ds_message_state, " +
            "						 DECODE(is_archive, " +
			"                           	'Y', '<b><font color=\"red\">'||is_archive_tsl||'</font></b>', " +
			"                           	'N', '<b><font color=\"blue\">'||is_archive_tsl||'</font></b>', " +
			"                           	is_archive_tsl" +
			"                  		 ) is_archive_tsl, is_selected, id_ds_message_receiver " +
            "                   FROM " + getGeneralDBScheme() + ".vc_ds_messages_receiver_all a " +
            "                          WHERE 1=1 ";
    	if (!isEmpty(pFindString)) {
    		mySQL = mySQL + 
   				" AND (UPPER(receiver_email) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(last_send_date_frmt) LIKE UPPER('%'||?||'%'))";
    		for (int i=0; i<2; i++) {
    		    pParam.add(new bcFeautureParam("string", pFindString));
    		}
    	}
        if (!isEmpty(pDispatchKind)) {
        	mySQL = mySQL + " AND cd_ds_receiver_dispatch_kind = ? ";
        	pParam.add(new bcFeautureParam("string", pDispatchKind));
        }
        if (!isEmpty(pState)) {
        	mySQL = mySQL + " AND cd_ds_message_state = ? ";
        	pParam.add(new bcFeautureParam("string", pState));
        }
        if (!isEmpty(pIsArchive)) {
        	mySQL = mySQL + " AND is_archive = ? ";
        	pParam.add(new bcFeautureParam("string", pIsArchive));
        }
        pParam.add(new bcFeautureParam("int", p_end));
        pParam.add(new bcFeautureParam("int", p_beg));
        mySQL = mySQL +
            "                  ORDER BY last_send_date DESC" +
            "        )  WHERE ROWNUM < ? " + 
            " ) WHERE rn >= ?";
        
        boolean hasEditPermission = false;
        
        try{
       	 	if (isEditPermited("DISPATCH_MESSAGES_EMAIL_RECEIVERS")>0) {
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
            for (int i=1; i <= colCount-2; i++) {
                html.append(getBottomFrameTableTH(messageXML, mtd.getColumnName(i)));
            }
            if (hasEditPermission) {
                html.append("<th>&nbsp;</th>\n");
            }
            html.append("</tr></thead><tbody>");
            
            while (rset.next()) {
            	html.append("<tr>");
          	  	for (int i=1; i <= colCount-2; i++) {
          	  		html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", "", ""));
          	  	}
                if (hasEditPermission) {
             	   String myHyperLink = "../crm/dispatch/messages/email_messagesupdate.jsp?type=receiver&id="+this.idMessage+"&id_ds_message_receiver="+rset.getString("ID_DS_MESSAGE_RECEIVER");
             	   html.append(getEditButtonHTML(myHyperLink));
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

    public String getAnswerMessagesHTML(String pFind, String pDispatchKind, String pMessageOperType, String pStateRecord, String pIsArchive, String p_beg, String p_end) {

    	ArrayList<bcFeautureParam> pParam = initParamArray();

    	String mySQL = 
        	"SELECT id_ds_message id_message, name_ds_message_oper_type, name_ds_sender_dispatch_kind, " +
        	"		email, title_ds_message title_message, " +
        	"		sendings_quantity, error_sendings_quantity, event_date_frmt, name_ds_message_state, " +
        	"		is_archive_tsl, cd_ds_message_oper_type " +
  		  	"  FROM (SELECT ROWNUM rn, id_ds_message, " +
  		  	"				DECODE(cd_ds_message_oper_type, " +
  		  	"                      'SEND', '<b><font color=\"green\">'||name_ds_message_oper_type||'</font></b>', " +
			"                      'RECEIVE', '<font color=\"blue\">'||name_ds_message_oper_type||'</font>', " +
			"                      name_ds_message_oper_type" +
			"               ) name_ds_message_oper_type, " +
  		  	"				DECODE(cd_ds_message_oper_type, " +
  		  	"                      'RECEIVE',DECODE(cd_ds_sender_dispatch_kind, " +
  		  	"                      				 'CLIENT', '<b><font color=\"green\">'||name_ds_sender_dispatch_kind||'</font></b>', " +
			"                      				 'PARTNER', '<b><font color=\"blue\">'||name_ds_sender_dispatch_kind||'</font></b>', " +
			"                               	 'SYSTEM', '<font color=\"red\"><b>'||name_ds_sender_dispatch_kind||'</b></font>', " +
			"                               	 'TRAINING', '<font color=\"darkgray\">'||name_ds_sender_dispatch_kind||'</font>', " +
			"                             		 'LG_PROMOTER', '<font color=\"black\">'||name_ds_sender_dispatch_kind||'</font>', " +
		  	"                               	 'UNKNOWN', '', " +
			"                      				 name_ds_sender_dispatch_kind" +
			"							  )," +
			"					   ''" +
			"               ) name_ds_sender_dispatch_kind, " +
  		  	"				DECODE(cd_ds_message_oper_type, " +
  		  	"                      'RECEIVE',email," +
  		  	"					   '<font color=\"red\"><b>'||TO_CHAR(NVL(receivers_count,0))||' '||" +
  		  	"							CASE WHEN NVL(receivers_count,0) = 0 THEN '" + messageXML.getfieldTransl("h_less_5_receivers", false) + "' " +
  		  	"							     WHEN NVL(receivers_count,0) = 1 THEN '" + messageXML.getfieldTransl("h_one_receiver", false) + "' " +
		  	"							     WHEN NVL(receivers_count,0) BETWEEN 2 AND 4 THEN '" + messageXML.getfieldTransl("h_2_4_receivers", false) + "' " +
  		  	"							     WHEN NVL(receivers_count,0) >= 5 THEN '" + messageXML.getfieldTransl("h_less_5_receivers", false) + "' " +
		  	"                           END||'</b></font>'" +
  		  	"				) email," +
  		  	"				title_ds_message, " +
  		  	"				DECODE(cd_ds_message_oper_type, " +
  		  	"                      'SEND',sendings_quantity," +
  		  	"					   ''" +
  		  	"				) sendings_quantity, " +
  		    "               DECODE(cd_ds_message_oper_type, " +
  		  	"                      'SEND',error_sendings_quantity," +
  		  	"					   ''" +
  		  	"				) error_sendings_quantity, " +
  		    "				event_date_frmt,  " +
  		  	"               DECODE(cd_ds_message_state, " +
  		  	"                      'PREPARED', '<b><font color=\"black\">'||name_ds_message_state||'</font></b>', " +
			"                      'NEW', '<font color=\"black\">'||name_ds_message_state||'</font>', " +
			"                      'EXECUTED', '<font color=\"green\">'||name_ds_message_state||'</font>', " +
			"                      'EXECUTED_PARTIALLY', '<font color=\"darkgreen\">'||name_ds_message_state||'</font>', " +
			"                      'ERROR', '<b><font color=\"red\">'||name_ds_message_state||'</font></b>', " +
			"                      'CARRIED_OUT_PARTIALLY', '<b><font color=\"darkgray\">'||name_ds_message_state||'</font></b>', " +
			"                      'CARRIED_OUT', '<font color=\"gray\">'||name_ds_message_state||'</font>', " +
			"                      'CANCELED', '<font color=\"blue\">'||name_ds_message_state||'</font>', " +
			"                      name_ds_message_state" +
			"               ) name_ds_message_state, " +
			"  				DECODE(is_archive, " +
			"                      'Y', '<b><font color=\"red\">'||is_archive_tsl||'</font></b>', " + 
			"                      'N', '<b><font color=\"blue\">'||is_archive_tsl||'</font></b>', " +
			"                      is_archive_tsl" +
			"               ) is_archive_tsl," +
			"				cd_ds_message_oper_type " +
  		  	"        FROM (SELECT * " +
  		  	"                FROM " + getGeneralDBScheme()+".vc_ds_messages_club_all " +
  		  	"               WHERE id_ds_message_parent = ? ";
    	pParam.add(new bcFeautureParam("int", this.idMessage));
    	
        if (!isEmpty(pFind)) {
    		mySQL = mySQL + 
    			" AND (UPPER(TO_CHAR(id_ds_message)) LIKE UPPER('%'||?||'%') OR " +
    			"	     UPPER(email) LIKE UPPER('%'||?||'%') OR " +
    			"	     UPPER(title_ds_message) LIKE UPPER('%'||?||'%') OR " +
    			"	     UPPER(event_date_frmt) LIKE UPPER('%'||?||'%')) ";
    		for (int i=0; i<4; i++) {
    		    pParam.add(new bcFeautureParam("string", pFind));
    		}
    	}
        if (!isEmpty(pDispatchKind)) {
	    	mySQL = mySQL + " AND cd_ds_sender_dispatch_kind = ? ";
	    	pParam.add(new bcFeautureParam("string", pDispatchKind));
	    }
        if (!isEmpty(pMessageOperType)) {
	    	mySQL = mySQL + " AND cd_ds_message_oper_type = ? ";
	    	pParam.add(new bcFeautureParam("string", pMessageOperType));
	    }
        if (!isEmpty(pIsArchive)) {
	    	mySQL = mySQL + " AND is_archive = ? ";
	    	pParam.add(new bcFeautureParam("string", pIsArchive));
	    }
	    if (!isEmpty(pStateRecord)) {
    		mySQL = mySQL + " AND cd_ds_message_state = ? ";
    		pParam.add(new bcFeautureParam("string", pStateRecord));
        }
	    pParam.add(new bcFeautureParam("int", p_end));
	    pParam.add(new bcFeautureParam("int", p_beg));
        mySQL = mySQL + 
  		  	"               ORDER BY cd_ds_message_type DESC) " +
  		  	"       WHERE ROWNUM < ? " + 
  		  	"        )"+
  		  	" WHERE rn >= ?";
        
        StringBuilder html = new StringBuilder();
        PreparedStatement st = null;
        Connection con = null;
        
        try{
            LOGGER.debug(prepareSQLToLog(mySQL, pParam));
            con = Connector.getConnection(getSessionId());
            st = con.prepareStatement(mySQL);
            st = prepareParam(st, pParam);
            ResultSet rset = st.executeQuery();
            ResultSetMetaData mtd = rset.getMetaData();
            int colCount = mtd.getColumnCount();
            
            html.append(getBottomFrameTable());
            html.append("<thead><tr>\n");
            for (int i=1; i <= colCount-1; i++) {
            	html.append(getBottomFrameTableTH(messageXML, mtd.getColumnName(i)));
            }
            html.append("<tr><tbody>\n");
            
            while (rset.next()) {
            	html.append("<tr id=\"elem_"+rset.getString("ID_MESSAGE")+"\" onmouseout=\"coloredOut(this)\" onmouseover=\"coloredOver(this,1)\">");
	           	for (int i=1; i <= colCount-1; i++) {
	           		if ("ID_MESSAGE".equalsIgnoreCase(mtd.getColumnName(i))) {
          	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/dispatch/messages/email_messagesspecs.jsp?id="+rset.getString("ID_MESSAGE"), "", ""));
         	  		} else {
                		html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", "", ""));
         	  		}
	           	}
            }
            html.append("</tbody></table>\n");
            html.append("</form\n");
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
        LOGGER.debug("END");
        return html.toString();
    } // getNatPrsEmailMessagesHeaderHTML

}
