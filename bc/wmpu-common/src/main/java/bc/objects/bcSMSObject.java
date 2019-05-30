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

public class bcSMSObject extends bcObject {
	private final static Logger LOGGER=Logger.getLogger(bcSMSObject.class);
	private Map<String,Comparable<String>> fieldHm = new HashMap<String, Comparable<String>>();
	
	private String idSMS;
	
	public bcSMSObject(String pIdSMS){
		this.idSMS = pIdSMS;
		getFeature();
	}
	
	private void getFeature() {
		String featureSelect = " SELECT * FROM " + getGeneralDBScheme() + ".VC_DS_SMS_ALL WHERE id_sms_message = ?";
		fieldHm = getFeatures2(featureSelect, this.idSMS, false);
	}
	
	public String getValue(String pColumnName) {
		return getFeautureResult(fieldHm, pColumnName);
	}
    
    public String getActionsHTML(String pIdSendStatus, String pIdDeliveryStatus, String p_beg, String p_end) {
    	StringBuilder html = new StringBuilder();
    	Connection con = null;
    	PreparedStatement st = null;

    	ArrayList<bcFeautureParam> pParam = initParamArray();

    	String mySQL = 
	    	" SELECT rn, id_sms_action, name_sms_profile, recepient, text_message, " +
			"        name_send_status, send_status_date, name_deliv_status, " +
			"        deliv_status_date, refno, error_message, id_sms_profile " +
			"   FROM (SELECT ROWNUM rn, id_sms_action, name_sms_profile, recepient, text_message, " +
			"                name_send_status, send_status_date, name_deliv_status, " +
			"                deliv_status_date, refno, error_message, id_sms_profile " +
			"           FROM (SELECT id_sms_action, name_sms_profile, recepient, text_message, " +
			"  				         DECODE(id_send_status, " +
    		"                               11, '<font color=\"blue\">'||name_send_status||'</font>', " +
    		"                               12, '<font color=\"red\">'||name_send_status||'</font>', " +
    		"                               13, '<b><font color=\"red\">'||name_send_status||'</font></b>', " +
    		"                               14, '<font color=\"green\">'||name_send_status||'</font>', " +
    		"                              name_send_status) name_send_status, " +
			"                        send_status_date_frmt send_status_date, " +
			"  				         DECODE(id_deliv_status, " +
    		"                               10, '<font color=\"black\">'||name_deliv_status||'</font>', " +
    		"                               11, '<font color=\"green\">'||name_deliv_status||'</font>', " +
    		"                               12, '<b><font color=\"red\">'||name_deliv_status||'</font></b>', " +
    		"                               13, '<b><font color=\"red\">'||name_deliv_status||'</font></b>', " +
    		"                               14, '<b><font color=\"red\">'||name_deliv_status||'</font></b>', " +
    		"                              name_deliv_status) name_deliv_status, " +
			"                        deliv_status_date_frmt deliv_status_date, refno, " +
			"                        error_message, id_sms_profile " +
    		"                   FROM " + getGeneralDBScheme()+".vc_ds_sms_action_all " +
    		"                  WHERE id_sms_message = ? "; 
    	pParam.add(new bcFeautureParam("int", this.idSMS));
    	
	    if (!isEmpty(pIdSendStatus)) {
    		mySQL = mySQL + " AND id_send_status = ? ";
    		pParam.add(new bcFeautureParam("int", pIdSendStatus));
    	}
			
	    if (!isEmpty(pIdDeliveryStatus)) {
	    	mySQL = mySQL + " AND id_deliv_status = ? ";
	    	pParam.add(new bcFeautureParam("int", pIdDeliveryStatus));
	    }
	    pParam.add(new bcFeautureParam("int", p_end));
	    pParam.add(new bcFeautureParam("int", p_beg));
    	mySQL = mySQL + 
    		" 				   ORDER BY id_sms_action DESC) " +
    		"          WHERE ROWNUM < ? " +
    		"		 ) " +
    		"  WHERE rn >= ?";
    	
        boolean hasProfilePermission = false;
	    try{
        	if (isEditMenuPermited("DISPATCH_SETTINGS_SMS_PROFILE")>=0) {
        		hasProfilePermission = true;
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
	        	html.append(getBottomFrameTableTH(smsXML, mtd.getColumnName(i)));
            }
	        html.append("</tr></thead><tbody>");
	        
	        while (rset.next()) {
	        	html.append("<tr>\n");
		        for (int i=1; i <= colCount-1; i++) {
		        	if ("NAME_SMS_PROFILE".equalsIgnoreCase(mtd.getColumnName(i)) &&
		        			hasProfilePermission) {
		        		html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/dispatch/settings/sms_profilespecs.jsp?id="+rset.getString("ID_SMS_PROFILE"), "", ""));
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
    
    public String getRelatedSMSHTML(String p_beg, String p_end) {
    	StringBuilder html = new StringBuilder();
    	Connection con = null;
    	PreparedStatement st = null;
	    boolean hasSMSPermission = false;
	    boolean hasNatPrsPermission = false;

	    ArrayList<bcFeautureParam> pParam = initParamArray();

	    String mySQL = 
	    	" SELECT rn, id_sms_message, name_sms_message_type, recepient, text_message, " +
			"        event_date_frmt, name_sms_state, send_count, is_archive_tsl, " +
			"        id_nat_prs " +
			"   FROM (SELECT ROWNUM rn, id_sms_message, name_sms_message_type, recepient, text_message, " +
			"                event_date_frmt, name_sms_state, send_count, is_archive_tsl, " +
			"                id_nat_prs " +
			"           FROM (SELECT id_sms_message, " +
    		"  				         DECODE(cd_sms_message_type, " +
    		"                               'SEND', '<font color=\"black\">'||name_sms_message_type||'</font>', " +
    		"                               'RECEIVE', '<font color=\"blue\">'||name_sms_message_type||'</font>', " +
    		"                               'CALL_IN', '<b><font color=\"red\">'||name_sms_message_type||'</font></b>', " +
    		"                               'CALL_OUT', '<b><font color=\"darkgreen\">'||name_sms_message_type||'</font></b>', " +
    		"                              name_sms_message_type) name_sms_message_type, " +
			"                        recepient, text_message, " +
			"                        event_date_frmt, name_sms_state, send_count, " +
			"  				         DECODE(is_archive, " +
			"                               'Y', '<b><font color=\"red\">'||is_archive_tsl||'</font></b>', " +
			"                              is_archive_tsl) is_archive_tsl, " +
			"                        id_nat_prs " +
			"                   FROM " + getGeneralDBScheme()+".vc_ds_sms_all " +
    		"                  WHERE id_sms_message_parent = ? " +
	    	" 				   ORDER BY id_sms_message DESC) " +
    		"          WHERE ROWNUM < ? " +
    		"		 ) " +
    		"  WHERE rn >= ?";
	    pParam.add(new bcFeautureParam("int", this.idSMS));
	    pParam.add(new bcFeautureParam("int", p_end));
	    pParam.add(new bcFeautureParam("int", p_beg));
	    try{
	    	if (isEditMenuPermited("DISPATCH_MESSAGES_SMS")>=0) {
	    		hasSMSPermission = true;
        	}
	    	if (isEditMenuPermited("CLIENTS_NATPERSONS")>=0) {
	    		hasNatPrsPermission = true;
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
	        	html.append(getBottomFrameTableTH(smsXML, mtd.getColumnName(i)));
            }
	        html.append("</tr></thead><tbody>");
	        
	        while (rset.next()) {
	        	html.append("<tr>\n");
		        for (int i=1; i <= colCount-1; i++) {
	        		if ("ID_SMS_MESSAGE".equalsIgnoreCase(mtd.getColumnName(i)) && hasSMSPermission) {
	        			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/dispatch/messages/smsspecs.jsp?id="+rset.getString("ID_SMS_MESSAGE"), "", ""));
	        		} else if ("RECEPIENT".equalsIgnoreCase(mtd.getColumnName(i)) && 
	        				hasNatPrsPermission &&
	        				!isEmpty(rset.getString("ID_NAT_PRS"))) {
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
    
    public String getGiftsRequestsHTML(String p_beg, String p_end) {
    	StringBuilder html = new StringBuilder();
    	Connection con = null;
    	PreparedStatement st = null;

    	ArrayList<bcFeautureParam> pParam = initParamArray();

    	String mySQL = 
	    	"SELECT rn, id_nat_prs_gift_request, name_nat_prs_gift_request_type, nm_nat_prs_gift_request_state, " +
        	"       date_accept_frmt, name_nat_prs, phone_contact, name_club_event, " +
        	"       text_request, id_nat_prs, id_club_event " +
        	"  FROM (SELECT ROWNUM rn, id_nat_prs_gift_request, full_name name_nat_prs, phone_contact, " +
        	"               DECODE(cd_nat_prs_gift_request_type," +
        	"                      'SMS', '<b><font color=\"green\">'||name_nat_prs_gift_request_type||'</font></b>'," +
        	"                      'EMAIL', '<b><font color=\"darkgray\">'||name_nat_prs_gift_request_type||'</font></b>'," +
        	"                      'PHONE_CALL', '<b><font color=\"blue\">'||name_nat_prs_gift_request_type||'</font></b>'," +
        	"                      'CALL_CENTER', '<b><font color=\"brown\">'||name_nat_prs_gift_request_type||'</font></b>'," +
        	"                      'OFFICE', '<b><font color=\"brown\">'||name_nat_prs_gift_request_type||'</font></b>'," +
        	"                      name_nat_prs_gift_request_type" +
        	"               ) name_nat_prs_gift_request_type, " +
        	"               DECODE(cd_nat_prs_gift_request_state," +
        	"                      'ACCEPT', '<b><font color=\"blue\">'||nm_nat_prs_gift_request_state||'</font></b>'," +
        	"                      'REJECTED', '<b><font color=\"red\">'||nm_nat_prs_gift_request_state||'</font></b>'," +
        	"                      'PROCESSED', '<b><font color=\"green\">'||nm_nat_prs_gift_request_state||'</font></b>'," +
        	"                      nm_nat_prs_gift_request_state" +
        	"               ) nm_nat_prs_gift_request_state, " +
        	"               name_club_event, " +
        	"               text_request, date_accept_frmt, id_nat_prs, id_club_event " +
        	"		   FROM (SELECT * " +
        	"                  FROM " + getGeneralDBScheme() + ".vc_nat_prs_gift_request_cl_all" +
        	"                 WHERE id_accept_sms_message = ? " + 
            "               ORDER BY date_accept DESC) " +
            "         WHERE ROWNUM < ?)" + 
            "  WHERE rn >= ?";
	    pParam.add(new bcFeautureParam("int", this.idSMS));
	    pParam.add(new bcFeautureParam("int", p_end));
	    pParam.add(new bcFeautureParam("int", p_beg));

	    boolean hasEditPermission = false;
	    boolean hasGiftRequestPermission = false;
	    boolean hasNatPrsPermission = false;
	    boolean hasClubActionPermission = false;
	    
	    try{
	    	if (isEditPermited("DISPATCH_MESSAGES_SMS_GIFTS_REQUEST")>0) {
	    		hasEditPermission = true;
        	}
        	
	    	if (isEditMenuPermited("CLUB_EVENT_REQUEST")>=0) {
	    		hasGiftRequestPermission = true;
        	}
	    	if (isEditMenuPermited("CLUB_EVENT_ACTIONS")>=0) {
	    		hasClubActionPermission = true;
        	}
	    	if (isEditMenuPermited("CLIENTS_NATPERSONS")>=0) {
	    		hasNatPrsPermission = true;
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
	        	html.append(getBottomFrameTableTH(club_actionXML, mtd.getColumnName(i)));
            }
	        if (hasEditPermission) {
	        	html.append("<th>&nbsp;</th>");
	        }
	        html.append("</tr></thead><tbody>");
	        
	        while (rset.next()) {
	        	html.append("<tr>\n");
		        for (int i=1; i <= colCount-2; i++) {
	        		if ("ID_NAT_PRS_GIFT_REQUEST".equalsIgnoreCase(mtd.getColumnName(i)) && hasGiftRequestPermission) {
	        			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/club_event/requestspecs.jsp?id="+rset.getString("ID_NAT_PRS_GIFT_REQUEST"), "", ""));
	        		} else if ("NAME_CLUB_EVENT".equalsIgnoreCase(mtd.getColumnName(i)) && hasClubActionPermission) {
	        			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/club_event/clubactionspecs.jsp?id="+rset.getString("ID_CLUB_EVENT"), "", ""));
	        		} else if ("NAME_NAT_PRS".equalsIgnoreCase(mtd.getColumnName(i)) && 
	        				hasNatPrsPermission &&
	        				!isEmpty(rset.getString("ID_NAT_PRS"))) {
	        			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/clients/natpersonspecs.jsp?id="+rset.getString("ID_NAT_PRS"), "", ""));
	        		} else {
	        			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", "", ""));
	        		}
	        	}
                if (hasEditPermission) {
                  	String myDeleteLink = "../crm/dispatch/messages/smsupdate.jsp?id=" + this.idSMS + "&id_nat_prs_gift_request=" + rset.getString("ID_NAT_PRS_GIFT_REQUEST") + "&type=genera&action=remove_request&process=yes";
                	html.append(getDeleteButtonHTML(myDeleteLink, club_actionXML.getfieldTransl("h_delete_nat_prs_gift_request", false)));
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
