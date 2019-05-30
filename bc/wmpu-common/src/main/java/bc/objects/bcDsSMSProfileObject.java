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

public class bcDsSMSProfileObject extends bcObject {
	private final static Logger LOGGER=Logger.getLogger(bcDsSMSProfileObject.class);
	
	private Map<String,Comparable<String>> fieldHm = new HashMap<String, Comparable<String>>();
	
	private String idSMSProfile;
	
	public bcDsSMSProfileObject(String pIdSMSProfile){
		this.idSMSProfile = pIdSMSProfile;
		getFeature();
	}
	
	private void getFeature() {
		String featureSelect = " SELECT * FROM " + getGeneralDBScheme() + ".VC_DS_SMS_PROFILE_CLUB_ALL WHERE id_sms_profile = ?";
		fieldHm = getFeatures2(featureSelect, this.idSMSProfile, false);
	}
	
	public String getValue(String pColumnName) {
		return getFeautureResult(fieldHm, pColumnName);
	}

    public String getMessagesHTML(String pOperationType, String pFindString, String pMessageType, String pMessageState, String pDispatchKind, String pIsArchive, String p_beg, String p_end) {
    	StringBuilder html = new StringBuilder();
    	Connection con = null;
    	PreparedStatement st = null;
        
    	ArrayList<bcFeautureParam> pParam = initParamArray();

    	String mySQL = 
    		" SELECT id_sms_message, name_sms_message_type, name_dispatch_kind, recepient, text_message, " +
			"        event_date_frmt, name_sms_state, send_count, is_archive_tsl, " +
			"		 id_nat_prs, id_contact_prs, id_tr_person, id_user " +
			"   FROM (SELECT ROWNUM rn, id_sms_message, name_sms_message_type, name_dispatch_kind, recepient, text_message, " +
			"                event_date_frmt, name_sms_state, send_count, is_archive_tsl, " +
			"				 id_nat_prs, id_contact_prs, id_tr_person, id_user " +
			"           FROM (SELECT id_sms_message, " +
    		"  				         DECODE(cd_sms_message_type, " +
    		"                               'SEND', '<font color=\"black\">'||name_sms_message_type||'</font>', " +
    		"                               'RECEIVE', '<font color=\"blue\">'||name_sms_message_type||'</font>', " +
    		"                               'CALL_IN', '<b><font color=\"red\">'||name_sms_message_type||'</font></b>', " +
    		"                               'CALL_OUT', '<b><font color=\"darkgreen\">'||name_sms_message_type||'</font></b>', " +
    		"                              name_sms_message_type" +
    		"						 ) name_sms_message_type, " +
			"                        DECODE(cd_dispatch_kind, " +
			"                      			'CLIENT', '<b><font color=\"green\">'||name_dispatch_kind||'</font></b>', " +
			"                      			'PARTNER', '<b><font color=\"blue\">'||name_dispatch_kind||'</font></b>', " +
			"                               'SYSTEM', '<font color=\"red\"><b>'||name_dispatch_kind||'</b></font>', " +
			"                               'TRAINING', '<font color=\"darkgray\">'||name_dispatch_kind||'</font>', " +
			"                              	'LG_PROMOTER', '<font color=\"black\">'||name_dispatch_kind||'</font>', " +
			"                               'UNKNOWN', '', " +
			"                              name_dispatch_kind" +
    		"						 ) name_dispatch_kind, " +
			"						 recepient, text_message, " +
			"                        event_date_frmt, " +
			"                        DECODE(cd_sms_state, " +
			"                               'PREPARED', '<b><font color=\"black\">'||name_sms_state||'</font></b>', " +
			"                               'NEW', '<font color=\"black\">'||name_sms_state||'</font>', " +
			"                               'IN_PROCESS', '<font color=\"gray\">'||name_sms_state||'</font>', " +
			"                               'DELIVERED', '<font color=\"darkgreen\">'||name_sms_state||'</font>', " +
			"                               'ERROR', '<b><font color=\"red\">'||name_sms_state||'</font></b>', " +
			"                               'REPORT_NOT_RECIEVE', '<b><font color=\"red\">'||name_sms_state||'</font></b>', " +
			"                               'RECEIVED', '<font color=\"blue\">'||name_sms_state||'</font>', " +
			"                               'WAIT_FOR_CONFIRM', '<font color=\"darkyellow\">'||name_sms_state||'</font>', " +
			"                              name_sms_state) name_sms_state, " +
			"                        send_count, " +
			"  				         DECODE(is_archive, " +
			"                               'Y', '<b><font color=\"red\">'||is_archive_tsl||'</font></b>', " +
			"                              '<b><font color=\"red\">'||is_archive_tsl||'</font></b>'" +
			"						 ) is_archive_tsl, id_nat_prs, id_contact_prs, id_tr_person, id_user " +
			"                   FROM " + getGeneralDBScheme()+".vc_ds_sms_all " +
			"                  WHERE id_sms_profile = ? ";
    	pParam.add(new bcFeautureParam("int", this.idSMSProfile));
    	
	    if (!isEmpty(pMessageType)) {
	    	mySQL = mySQL + " AND cd_sms_message_type = ? ";
	    	pParam.add(new bcFeautureParam("string", pMessageType));
	    }
	    if (!isEmpty(pMessageState)) {
	    	mySQL = mySQL + " AND cd_sms_state = ? ";
	    	pParam.add(new bcFeautureParam("string", pMessageState));
	    }
	    if (!isEmpty(pDispatchKind)) {
	    	mySQL = mySQL + " AND cd_dispatch_kind = ? ";
	    	pParam.add(new bcFeautureParam("string", pDispatchKind));
	    }
	    if (!isEmpty(pIsArchive)) {
	    	mySQL = mySQL + " AND is_archive = ? ";
	    	pParam.add(new bcFeautureParam("string", pIsArchive));
	    }
    	if (!isEmpty(pFindString)) {
    		mySQL = mySQL + " AND (UPPER(id_sms_message) LIKE UPPER('%'||?||'%') OR " +
    				"              UPPER(full_name_nat_prs) LIKE UPPER('%'||?||'%') OR " +
					"              UPPER(recepient) LIKE UPPER('%'||?||'%') OR " +
					"              UPPER(text_message) LIKE UPPER('%'||?||'%') OR " +
    				"              UPPER(event_date_frmt) LIKE UPPER('%'||?||'%'))";
    		for (int i=0; i<5; i++) {
    		    pParam.add(new bcFeautureParam("string", pFindString));
    		}
    	}
    	pParam.add(new bcFeautureParam("int", p_end));
    	pParam.add(new bcFeautureParam("int", p_beg));
	    mySQL = mySQL + 
    		" 				   ORDER BY id_sms_message DESC) " +
    		"          WHERE ROWNUM < ? " +
    		"		 ) " +
    		"  WHERE rn >= ?";
	    
	    boolean hasSMSPermission = false;
	    boolean hasNatPrsPermission = false;
	    boolean hasContactPrsPermission = false;
	    boolean hasTrainingPersonPermission = false;
	    boolean hasSystemUserPermission = false;
        boolean hasEditPermission = false;
        
	    try{
	    	if (isEditMenuPermited("DISPATCH_MESSAGES_SMS")>=0) {
	    		hasSMSPermission = true;
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
	    	if (isEditPermited("DISPATCH_SETTINGS_SMS_PROFILES_MESSAGES")>0) {
	    		hasEditPermission = true;
        	}
	    	
	    	LOGGER.debug(prepareSQLToLog(mySQL, pParam));
	    	con = Connector.getConnection(getSessionId());
	    	st = con.prepareStatement(mySQL);
	    	st = prepareParam(st, pParam);
	    	ResultSet rset = st.executeQuery();
            ResultSetMetaData mtd = rset.getMetaData();            
            int colCount = mtd.getColumnCount();
            
            if (hasEditPermission) {
            	html.append("<form method=\"Post\" name=\"updateForm\" id=\"updateForm\" action=\"../crm/dispatch/settings/sms_profileupdate.jsp\" onSubmit=\"return CheckSelect(this);\">\n");
            	html.append("<input type=\"hidden\" name=\"type\" id=\"type\" value=\"message\">\n");
            	html.append("<input type=\"hidden\" name=\"id\" id=\"id\" value=\""+this.idSMSProfile+"\">\n");
            	if ("change_profile".equalsIgnoreCase(pOperationType)) {
           	 		html.append("<input type=\"hidden\" name=\"action\" id=\"action\" value=\"change_profile\">\n");
           	 		html.append("<input type=\"hidden\" name=\"process\" id=\"process\" value=\"no\">\n");
            	} else {
            		html.append("<input type=\"hidden\" name=\"action\" id=\"action\" value=\"execute\">\n");
           	 		html.append("<input type=\"hidden\" name=\"process\" id=\"process\" value=\"yes\">\n");
            	}
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
	           	 String changeProfileSelected = "";
           	 
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
		       	 } else if ("change_profile".equalsIgnoreCase(pOperationType)) {
		       		 changeProfileSelected = " SELECTED ";
		       	 }
	           	 html.append("<th>");
	           	 html.append("<select id=\"operation_type\" name=\"operation_type\" onchange=\"checkOperation()\" class=\"inputfield\">");
	           	 html.append("<option value=\"send\" " + sendSelected + ">" + messageXML.getfieldTransl("h_operation_send", false) + "</option>");
	           	 html.append("<option value=\"delete\" " + deleteSelected + ">" + messageXML.getfieldTransl("h_operation_delete", false) + "</option>");
	           	 html.append("<option value=\"prepare\" " + prepareSelected + ">" + messageXML.getfieldTransl("h_operation_prepare", false) + "</option>");
	           	 html.append("<option value=\"to_archive\" " + toArchiveSelected + ">" + messageXML.getfieldTransl("h_operation_to_archive", false) + "</option>");
	           	 html.append("<option value=\"from_archive\" " + fromArchiveSelected + ">" + messageXML.getfieldTransl("h_operation_from_archive", false) + "</option>");
	           	 html.append("<option value=\"delivered\" " + deliveredSelected + ">" + messageXML.getfieldTransl("h_operation_delivered", false) + "</option>");
	           	 html.append("<option value=\"error\" " + errorSelected + ">" + messageXML.getfieldTransl("h_operation_error", false) + "</option>");
	           	 html.append("<option value=\"change_profile\" " + changeProfileSelected + ">" + messageXML.getfieldTransl("h_operation_change_profile", false) + "</option>");
	           	 html.append("</select><br>");
	        	 html.append(getSubmitButtonAjax3("../crm/dispatch/settings/sms_profileupdate.jsp", "submit", "CheckSelect(document.getElementById('updateForm'))") + "<br>");
	           	 html.append("<input name=\"mainCheck\" id=\"mainCheck\" type=\"checkbox\" onclick=\"CheckAll(this,'chb')\"></th>\n");
           }
	        for (int i=1; i <= colCount-4; i++) {
	        	html.append(getBottomFrameTableTH(smsXML, mtd.getColumnName(i)));
            }
	        html.append("</tr></thead><tbody>");
	        
	        while (rset.next()) {
	        	html.append("<tr>\n");
	           	 if (hasEditPermission) {
	           		 html.append("<td align=\"center\"><input type=\"checkbox\" name=\"chb"+rset.getString("ID_SMS_MESSAGE")+"\" value=\"\" id=\"chb"+rset.getString("ID_SMS_MESSAGE")+"\" onclick=\"return CheckCB(this);\"></td>\n");
	           	 } 
		        for (int i=1; i <= colCount-4; i++) {
	        		if ("ID_SMS_MESSAGE".equalsIgnoreCase(mtd.getColumnName(i)) && hasSMSPermission) {
	        			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/dispatch/messages/smsspecs.jsp?id="+rset.getString("ID_SMS_MESSAGE"), "", ""));
	        		} else if ("RECEPIENT".equalsIgnoreCase(mtd.getColumnName(i))) { 
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
	  } // getRelationShipsHTML
    
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
			"                               'LG_PROMOTER', '<font color=\"black\">'||name_dispatch_kind||'</font>', " +
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
			"                  WHERE id_sms_profile = ? ";
    	pParam.add(new bcFeautureParam("int", this.idSMSProfile));
    	
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
}
