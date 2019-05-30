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

public class bcEmailProfileObject extends bcObject {
	private final static Logger LOGGER=Logger.getLogger(bcEmailProfileObject.class);
	private Map<String,Comparable<String>> fieldHm = new HashMap<String, Comparable<String>>();
	
	private String idEmailProfile;
	
	public bcEmailProfileObject(String pIdEmailProfile){
		this.idEmailProfile = pIdEmailProfile;
		getFeature();
	}
	
	private void getFeature() {
		String featureSelect = " SELECT * FROM " + getGeneralDBScheme() + ".VC_DS_EMAIL_PROFILE_CLUB_ALL WHERE id_email_profile = ?";
		fieldHm = getFeatures2(featureSelect, this.idEmailProfile, false);
	}
	
	public String getValue(String pColumnName) {
		return getFeautureResult(fieldHm, pColumnName);
	}
    
    public String getPatternsHTML(String pFind, String pType, String pStatus, String p_beg, String p_end) {
    	ArrayList<bcFeautureParam> pParam = initParamArray();

    	String mySQL = 
    		" SELECT rn, id_ds_pattern, name_ds_pattern, name_pattern_type, name_dispatch_kind, " +
			"        name_pattern_status, begin_action_date, end_action_date " +
			"   FROM (SELECT ROWNUM rn, id_ds_pattern, name_ds_pattern, name_pattern_type, name_dispatch_kind, " +
			"                name_pattern_status, begin_action_date, end_action_date " +
			"           FROM (SELECT a.id_ds_pattern, a.name_ds_pattern, a.name_pattern_type, " +
			"						 DECODE(cd_dispatch_kind, " +
			"                      			'CLIENT', '<b><font color=\"green\">'||name_dispatch_kind||'</font></b>', " +
			"                      			'PARTNER', '<b><font color=\"blue\">'||name_dispatch_kind||'</font></b>', " +
			"                               'SYSTEM', '<font color=\"red\"><b>'||name_dispatch_kind||'</b></font>', " +
			"                               'TRAINING', '<font color=\"darkgray\">'||name_dispatch_kind||'</font>', " +
			"                             	'LG_PROMOTER', '<font color=\"black\">'||name_dispatch_kind||'</font>', " +
		  	"                               'UNKNOWN', '', " +
			"                              name_dispatch_kind" +
    		"						 ) name_dispatch_kind," +
			"                 DECODE(a.cd_pattern_status, " +
        	"                        'ACTIVE', '<font color=\"black\">'||name_pattern_status||'</font>', " +
        	"                        'SUSPENDED', '<font color=\"brown\"><b>'||name_pattern_status||'</b></font>', " +
        	"                        'CANCEL', '<font color=\"red\"><b>'||name_pattern_status||'</b></font>', " +
        	"                        'FINISH', '<font color=\"green\"><b>'||name_pattern_status||'</b></font>', " +
        	"                        a.name_pattern_status) name_pattern_status, " +
	  		" 						 a.begin_action_date_frmt begin_action_date," +
			"                        a.end_action_date_frmt end_action_date " +
			"                   FROM " + getGeneralDBScheme()+".vc_ds_pattern_club_all a " +
			"                  WHERE id_email_profile = ?";
    	pParam.add(new bcFeautureParam("int", this.idEmailProfile));
    	
    	if (!isEmpty(pFind)) {
    		mySQL = mySQL + 
    			" AND (UPPER(a.id_ds_pattern) LIKE UPPER('%'||?||'%') OR " +
    			"      UPPER(a.begin_action_date_frmt) LIKE UPPER('%'||?||'%') OR " +
    			"      UPPER(a.end_action_date_frmt) LIKE UPPER('%'||?||'%') OR " +
    			"      UPPER(a.name_ds_pattern) LIKE UPPER('%'||?||'%'))";
    		for (int i=0; i<4; i++) {
    		    pParam.add(new bcFeautureParam("string", pFind));
    		}
    	}
    	if (!isEmpty(pType)) {
    		mySQL = mySQL + " AND cd_pattern_type = ? ";
    		pParam.add(new bcFeautureParam("string", pType));
    	}
    	if (!isEmpty(pStatus)) {
    		mySQL = mySQL + " AND cd_pattern_status = ? ";
    		pParam.add(new bcFeautureParam("string", pStatus));
    	}
    	pParam.add(new bcFeautureParam("int", p_end));
    	pParam.add(new bcFeautureParam("int", p_beg));
    	mySQL = mySQL + 
    		" 				   ORDER BY a.name_ds_pattern) " +
    		"          WHERE ROWNUM < ? " +
    		"		 ) " +
    		"  WHERE rn >= ?";
    	StringBuilder html = new StringBuilder();
        Connection con = null;
        PreparedStatement st = null;
        boolean hasPattenrPermission = false;
        try{
        	if (isEditMenuPermited("DISPATCH_MESSAGES_PATTERN")>=0) {
        		hasPattenrPermission = true;
        	}
        	
        	LOGGER.debug(prepareSQLToLog(mySQL, pParam));
        	con = Connector.getConnection(getSessionId());;
            st = con.prepareStatement(mySQL);
            st = prepareParam(st, pParam);
            ResultSet rset = st.executeQuery();
            ResultSetMetaData mtd = rset.getMetaData();
            int colCount = mtd.getColumnCount();
            
            html.append(getBottomFrameTable());
            html.append("<tr>");
            for (int i=1; i <= colCount; i++) {
            	html.append(getBottomFrameTableTH(messageXML, mtd.getColumnName(i)));
            }
            html.append("</tr></thead><tbody>\n");
            while (rset.next())
            {
                html.append("<tr>\n");
                for (int i=1; i<=colCount; i++) {
                	if (("ID_DS_PATTERN".equalsIgnoreCase(mtd.getColumnName(i)) ||
                			"NAME_DS_PATTERN".equalsIgnoreCase(mtd.getColumnName(i))) &&
                			hasPattenrPermission) {
                		html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/dispatch/messages/patternspecs.jsp?id="+rset.getString("ID_DS_PATTERN"), "", ""));
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

	public String getMessagesHTML(String pOperationType, String pFind, String pDispatchKind, String pMessageOperType, String pStateRecord, String pIsArchive, String p_beg, String p_end) {
		ArrayList<bcFeautureParam> pParam = initParamArray();

		String mySQL = 
    		"SELECT id_ds_message id_message, name_ds_message_oper_type, name_ds_sender_dispatch_kind, " +
        	"		email, title_ds_message title_message, " +
        	"		sendings_quantity, error_sendings_quantity, event_date_frmt, name_ds_message_state, " +
        	"		is_archive_tsl, cd_ds_message_oper_type, id_nat_prs, id_contact_prs, id_tr_person, id_user " +
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
  		  	"                      'SEND',TO_CHAR(NVL(sendings_quantity,0))," +
  		  	"					   ''" +
  		  	"				) sendings_quantity, " +
  		    "               DECODE(cd_ds_message_oper_type, " +
  		  	"                      'SEND',TO_CHAR(NVL(error_sendings_quantity,0))," +
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
			"				cd_ds_message_oper_type, id_nat_prs_sender id_nat_prs, id_contact_prs_sender id_contact_prs, " +
			"				id_tr_person_sender id_tr_person, id_user_sender id_user " +
  		  	"        FROM (SELECT * " +
  		  	"                FROM " + getGeneralDBScheme()+".vc_ds_messages_club_all " +
    		"                 WHERE id_email_profile = ? " + 
    		"					AND cd_ds_message_type = 'EMAIL' ";
    	pParam.add(new bcFeautureParam("int", this.idEmailProfile));
    	
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
    		"                 ORDER BY begin_action_date desc, id_ds_message) "+
    		"         WHERE ROWNUM < ? " + 
    		" ) WHERE rn >= ?";
   		
        StringBuilder html = new StringBuilder();
        Connection con = null;
        PreparedStatement st = null;
        boolean hasEmailPermission = false;
	    boolean hasNatPrsPermission = false;
	    boolean hasContactPrsPermission = false;
	    boolean hasTrainingPersonPermission = false;
	    boolean hasSystemUserPermission = false;
        
        boolean hasEditPermission = false;
        try{
        	if (isEditMenuPermited("DISPATCH_MESSAGES_EMAIL")>=0) {
        		hasEmailPermission = true;
        	}
	    	if (isEditMenuPermited("CLIENTS_NATPERSONS")>=0) {
	    		hasNatPrsPermission = true;
        	}
	    	if (isEditMenuPermited("CLIENTS_CONTACT_PRS")>=0) {
	    		hasContactPrsPermission = true;
        	}
	    	if (isEditMenuPermited("TRAINING_PERCONS")>=0) {
	    		hasTrainingPersonPermission = true;
        	}
	    	if (isEditMenuPermited("SECURITY_USERS")>=0) {
	    		hasSystemUserPermission = true;
        	}
        	
       	 	if (isEditPermited("DISPATCH_SETTINGS_EMAIL_PROFILE_MESSAGES")>0) {
       	 		hasEditPermission = true;
        	}
        	
        	LOGGER.debug(prepareSQLToLog(mySQL, pParam));
        	con = Connector.getConnection(getSessionId());;
            st = con.prepareStatement(mySQL);
            st = prepareParam(st, pParam);
            ResultSet rset = st.executeQuery();
            ResultSetMetaData mtd = rset.getMetaData();
            int colCount = mtd.getColumnCount();
            
            if (hasEditPermission) {
            	html.append("<form method=\"Post\" name=\"updateForm\" id=\"updateForm\" action=\"../crm/dispatch/email_profileupdate.jsp\" onSubmit=\"return CheckSelect(this);\">\n");
            	html.append("<input type=\"hidden\" name=\"type\" value=\"general\">\n");
           	 	html.append("<input type=\"hidden\" name=\"action\" value=\"execute\">\n");
           	 	html.append("<input type=\"hidden\" name=\"process\" value=\"yes\">\n");
           	 	html.append("<input type=\"hidden\" name=\"id\" value=\"" + this.idEmailProfile + "\">\n");
            }
            
            html.append(getBottomFrameTable());
            html.append("<tr>");
            if (hasEditPermission) {
            	 String sendSelected = "";
	           	 String deleteSelected = "";
	           	 String prepareSelected = "";
	           	 //String cancelSelected = "";
	           	 String toArchiveSelected = "";
	           	 String fromArchiveSelected = "";
	           	 String deliveredSelected = "";
	           	 String errorSelected = "";
         	 
	           	 if ("send".equalsIgnoreCase(pOperationType)) {
	           		sendSelected = " SELECTED ";
		       	 } else if ("delete".equalsIgnoreCase(pOperationType)) {
		       		 deleteSelected = " SELECTED ";
		       	 } else if ("prepare".equalsIgnoreCase(pOperationType)) {
		       		 prepareSelected = " SELECTED ";
		       	 } else if ("to_archive".equalsIgnoreCase(pOperationType)) {
		       		 toArchiveSelected = " SELECTED ";
		       	 } else if ("from_archive".equalsIgnoreCase(pOperationType)) {
		       		 fromArchiveSelected = " SELECTED ";
		       	 } else if ("delivered".equalsIgnoreCase(pOperationType)) {
		       		 deliveredSelected = " SELECTED ";
		       	 } else if ("error".equalsIgnoreCase(pOperationType)) {
		       		 errorSelected = " SELECTED ";
		       	 }
	           	 html.append("<th>");
	           	 html.append("<select id=\"operation_type\" name=\"operation_type\" class=\"inputfield\">");
	           	 html.append("<option value=\"send\" " + sendSelected + ">" + messageXML.getfieldTransl("h_operation_send", false) + "</option>");
	           	 html.append("<option value=\"delete\" " + deleteSelected + ">" + messageXML.getfieldTransl("h_operation_delete", false) + "</option>");
	           	 html.append("<option value=\"prepare\" " + prepareSelected + ">" + messageXML.getfieldTransl("h_operation_prepare", false) + "</option>");
	           	 html.append("<option value=\"to_archive\" " + toArchiveSelected + ">" + messageXML.getfieldTransl("h_operation_to_archive", false) + "</option>");
	           	 html.append("<option value=\"from_archive\" " + fromArchiveSelected + ">" + messageXML.getfieldTransl("h_operation_from_archive", false) + "</option>");
	           	 html.append("<option value=\"delivered\" " + deliveredSelected + ">" + messageXML.getfieldTransl("h_operation_delivered", false) + "</option>");
	           	 html.append("<option value=\"error\" " + errorSelected + ">" + messageXML.getfieldTransl("h_operation_error", false) + "</option>");
	           	 html.append("</select><br>");
	        	 html.append(getSubmitButtonAjax3("../crm/dispatch/email_profileupdate.jsp", "submit", "CheckSelect(document.getElementById('updateForm'))") + "<br>");
	           	 html.append("<input name=\"mainCheck\" id=\"mainCheck\" type=\"checkbox\" onclick=\"CheckAll(this,'chb')\"></th>\n");
           }
            for (int i=1; i <= colCount-5; i++) {
            	html.append(getBottomFrameTableTH(messageXML, mtd.getColumnName(i)));
            }
            html.append("</tr></thead><tbody>\n");
            while (rset.next())
            {
                html.append("<tr>\n");
                if (hasEditPermission) {
              		 html.append("<td align=\"center\"><input type=\"checkbox\" name=\"chb"+rset.getString("ID_MESSAGE")+"\" value=\"\" id=\"chb"+rset.getString("ID_MESSAGE")+"\" onclick=\"return CheckCB(this);\"></td>\n");
              	}
                for (int i=1; i<=colCount-5; i++) {
                	if (("ID_MESSAGE".equalsIgnoreCase(mtd.getColumnName(i)) || 
                			"TITLE_MESSAGE".equalsIgnoreCase(mtd.getColumnName(i))) &&
                			hasEmailPermission) {
                		html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/dispatch/messages/email_messagesspecs.jsp?id="+rset.getString("ID_MESSAGE"), "", ""));
           			} else if ("EMAIL".equalsIgnoreCase(mtd.getColumnName(i))) {
           				if (hasNatPrsPermission &&
   	        				!isEmpty(rset.getString("ID_NAT_PRS"))) {
   	        				html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/clients/natpersonspecs.jsp?id="+rset.getString("ID_NAT_PRS"), "", ""));
   	        			} else if (hasContactPrsPermission &&
   	        				!isEmpty(rset.getString("ID_CONTACT_PRS"))) {
   	        				html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/clients/contact_prsspecs.jsp?id="+rset.getString("ID_CONTACT_PRS"), "", ""));
   	        			} else if (hasTrainingPersonPermission &&
   	        				!isEmpty(rset.getString("ID_TR_PERSON"))) {
   	        				html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/training/participant_personspecs.jsp?id="+rset.getString("ID_TR_PERSON"), "", ""));
   	        			} else if (hasSystemUserPermission &&
   	        				!isEmpty(rset.getString("ID_USER"))) {
   	        				html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/security/userspecs.jsp?id="+rset.getString("ID_USER"), "", ""));
   	        			} else {
   	        				html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", "", ""));
   	        			}
           			} else {
           				html.append(getBottomFrameTableTDBase("", mtd.getColumnName(i), mtd.getColumnType(i), rset.getString(i), "", "", ""));
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
    } // getMessagesHTML
    
}
