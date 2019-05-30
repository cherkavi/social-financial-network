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
import bc.lists.bcListTerminalUser;
import bc.service.bcFeautureParam;


public class bcContactsObject extends bcObject {
	private final static Logger LOGGER=Logger.getLogger(bcContactsObject.class);
	private Map<String,Comparable<String>> fieldHm = new HashMap<String, Comparable<String>>();
	
	private String idContactPrs;
	
	public bcContactsObject(String pIdContactPrs) {
		this.idContactPrs = pIdContactPrs;
		getFeature();
	}
	
	private void getFeature() {
		String featureSelect = " SELECT * FROM " + getGeneralDBScheme() + ".VC_CONTACT_PRS_PRIV_ALL WHERE id_nat_prs_role = ?";
		fieldHm = getFeatures2(featureSelect, this.idContactPrs, false);
	}
	
	public String getValue(String pColumnName) {
		return getFeautureResult(fieldHm, pColumnName);
	}

    public String getSystemUsersHTML(String pFindString, String pUserStatus, String p_beg, String p_end) {
	      StringBuilder html = new StringBuilder();
	      Connection con = null;
	      PreparedStatement st = null;

	      ArrayList<bcFeautureParam> pParam = initParamArray();

	      String mySQL = 
	    	  " SELECT rn, name_user, desc_user, name_user_status, id_user " +
              "   FROM (SELECT ROWNUM rn, name_user, desc_user," +
              "  		       DECODE(cd_user_status, " +
              "         	 	        'OPENED', '<font color=\"green\"><b>'||name_user_status||'</b></font>', " +
              "          	 	        'CLOSED', '<font color=\"gray\"><b>'||name_user_status||'</b></font>', " +
              "          		        'BLOCKED', '<font color=\"red\"><b>'||name_user_status||'</b></font>', " +
              "          		        'DELETED', '<font color=\"red\"><b>'||name_user_status||'</b></font>', " +
              "          		        name_user_status" +
              "  		       ) name_user_status, " +
              "  		       id_user " +
              "           FROM (SELECT name_user, desc_user, cd_user_status, name_user_status,  " +
              "                        id_user " +
              "                   FROM " + getGeneralDBScheme() + ".vc_users_all "+
              "                  WHERE id_nat_prs_role = ? ";
	      pParam.add(new bcFeautureParam("int", this.idContactPrs));
	      
	      if (!isEmpty(pFindString)) {
	    		mySQL = mySQL + 
	   				" AND (TO_CHAR(name_user) LIKE UPPER('%'||?||'%') OR " +
	   				"      UPPER(id_term) LIKE UPPER('%'||?||'%'))";
	    		for (int i=0; i<2; i++) {
	    		    pParam.add(new bcFeautureParam("string", pFindString));
	    		}
	      }
	      if (!isEmpty(pUserStatus)) {
	    		mySQL = mySQL + " AND cd_user_status = ? ";
	    		pParam.add(new bcFeautureParam("string", pUserStatus));
	      }
	      pParam.add(new bcFeautureParam("int", p_end));
	      pParam.add(new bcFeautureParam("int", p_beg));
	      mySQL = mySQL +
              "                  ORDER BY name_user)" +
              "          WHERE ROWNUM < ? " + 
              " ) WHERE rn >= ?";
	      boolean hasEditPermission = false;
	      boolean hasUserPermission = false;
	      try{
	    	  if (isEditPermited("CLIENTS_CONTACT_PRS_SYSTEM_USER")>0) {
	    		  hasEditPermission = true;
	    	  }
	    	  if (isEditMenuPermited("SECURITY_USERS")>=0) {
	    		  hasUserPermission = true;
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
	               html.append(getBottomFrameTableTH(contactXML, mtd.getColumnName(i)));
	          }
	          if (hasEditPermission) {
	            html.append("<th>&nbsp;</th><th>&nbsp;</th>\n");
	          }
	          html.append("</tr></thead><tbody>\n");
	          while (rset.next())
	          {
	        	  html.append("<tr>");
	          	  for (int i=1; i <= colCount-1; i++) {
	          		  if ("NAME_USER".equalsIgnoreCase(mtd.getColumnName(i)) && hasUserPermission) {
	          			  html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/security/userspecs.jsp?id=" + rset.getString("ID_USER"), "", ""));
	          		  } else {
	          			  html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", "", ""));
	          		  }
	          	  }
	            if (hasEditPermission) {
	            	String myHyperLink = "../crm/clients/contact_prsupdate.jsp?id_contact_prs="+this.idContactPrs+"&id_system_user="+rset.getString("ID_USER")+"&type=system_user";
	            	String myDeleteLink = "../crm/clients/contact_prsupdate.jsp?id_contact_prs="+this.idContactPrs+"&id_system_user="+rset.getString("ID_USER")+"&type=system_user&action=remove&process=yes";
	                html.append(getDeleteButtonHTML(myDeleteLink, userXML.getfieldTransl("LAB_DELETE_USER", false), rset.getString("NAME_USER")));
	            	html.append(getEditButtonWitDivHTML(myHyperLink, "div_data_detail"));
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
	  } // getJurPersonServicePlacesHTML

    public String getTerminalUsersHTML(String pFindString, String pTermUserAccessType, String pTermUserStatus, String p_beg, String p_end) {
    	bcListTerminalUser list = new bcListTerminalUser();
    	
    	String pWhereCause = " WHERE id_nat_prs_role = ? ";
    	
    	ArrayList<bcFeautureParam> pWhereValue = new ArrayList<bcFeautureParam>();
    	pWhereValue.add(new bcFeautureParam("int", this.idContactPrs));
    	String myDeleteLink = "";
    	String myEditLink = "";
    	if (isEditPermited("CLIENTS_CONTACT_PRS_TERMINAL_USER")>0) {
    		myDeleteLink = "../crm/clients/terminaluserupdate.jsp?back_type=CONTACT_PRS&type=user&id="+this.idContactPrs+"&id_contact_prs="+this.idContactPrs+"&action=remove&process=yes";
    	    myEditLink = "../crm/clients/terminaluserupdate.jsp?back_type=CONTACT_PRS&type=user&id="+this.idContactPrs+"&id_contact_prs="+this.idContactPrs;
    	}
    	
    	return list.getTerminalUsersHTML(pWhereCause, pWhereValue, "CONTACT_PRS", pFindString, pTermUserAccessType, pTermUserStatus, myEditLink, myDeleteLink, p_beg, p_end);
	
    }

    public String getMessagesHTML(String pFindString, String pTypeMessage, String p_beg, String p_end) {
    	StringBuilder html = new StringBuilder();
    	Connection con = null;
    	PreparedStatement st = null;
	    
	    boolean hasEmailMessagePermission = false;
	    boolean hasOfficeMessagePermission = false;
	    

	    ArrayList<bcFeautureParam> pParam = initParamArray();

	    String mySQL = 
	    	"SELECT rn, type_message, title_message, " + 
    		"		sendings_quantity, error_sendings_quantity, event_date_frmt, " +
    		"       state_record_tsl, is_archive_tsl, type_message2, " + 
    		"		id_message  " +
    		"  FROM (SELECT ROWNUM rn, id_message, type_message, title_message, " + 
    		"				sendings_quantity, error_sendings_quantity, event_date_frmt, " +
    		"               state_record_tsl, is_archive_tsl, type_message2, " + 
    		"				id_nat_prs " +
    		"          FROM (SELECT id_ds_message id_message, name_ds_message_type type_message, " +
    		"                       title_ds_message title_message, " + 
    		"						sendings_quantity, error_sendings_quantity, event_date_frmt, " +
    		"               		DECODE(cd_ds_message_state, " +
  		  	"                      			'PREPARED', '<b><font color=\"black\">'||name_ds_message_state||'</font></b>', " +
			"                      			'NEW', '<font color=\"black\">'||name_ds_message_state||'</font>', " +
			"                      			'EXECUTED', '<font color=\"green\">'||name_ds_message_state||'</font>', " +
			"                      			'EXECUTED_PARTIALLY', '<font color=\"darkgreen\">'||name_ds_message_state||'</font>', " +
			"                      			'ERROR', '<b><font color=\"red\">'||name_ds_message_state||'</font></b>', " +
			"                      			'CARRIED_OUT_PARTIALLY', '<b><font color=\"darkgray\">'||name_ds_message_state||'</font></b>', " +
			"                      			'CARRIED_OUT', '<font color=\"gray\">'||name_ds_message_state||'</font>', " +
			"                      			'CANCELED', '<font color=\"blue\">'||name_ds_message_state||'</font>', " +
			"                      			name_ds_message_state) state_record_tsl, " +
			"  				       	DECODE(is_archive, " +
			"                             	'Y', '<b><font color=\"red\">'||is_archive_tsl||'</font></b>', " +
			"                             	is_archive_tsl) is_archive_tsl, " +
  		  	"                       cd_ds_message_type type_message2, id_nat_prs " +
    		"                  FROM " + getGeneralDBScheme() + ".vc_nat_prs_messages_all " +
			"                  WHERE id_nat_prs = ? ";
	    pParam.add(new bcFeautureParam("int", this.getValue("ID_NAT_PRS")));
	    
    	if (!isEmpty(pFindString)) {
    		mySQL = mySQL + 
   				" AND (TO_CHAR(id_ds_message) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(title_ds_message) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(event_date_frmt) LIKE UPPER('%'||?||'%'))";
    		for (int i=0; i<3; i++) {
    		    pParam.add(new bcFeautureParam("string", pFindString));
    		}
    	}
    	if (!isEmpty(pTypeMessage)) {
    		mySQL = mySQL + " AND cd_ds_message_type = ? ";
    		pParam.add(new bcFeautureParam("string", pTypeMessage));
    	}
    	pParam.add(new bcFeautureParam("int", p_end));
    	pParam.add(new bcFeautureParam("int", p_beg));
	    mySQL = mySQL +
				" 				   ORDER BY begin_action_date desc) " +
	    		"          WHERE ROWNUM < ? " +
	    		"		 ) " +
	    		"  WHERE rn >= ? ";

	    try{
	    	if (isEditMenuPermited("DISPATCH_MESSAGES_EMAIL")>=0) {
	    		hasEmailMessagePermission = true;
        	}
	    	if (isEditMenuPermited("DISPATCH_MESSAGES_OFFICE")>=0) {
	    		hasOfficeMessagePermission = true;
        	}
	    	
	    	LOGGER.debug(prepareSQLToLog(mySQL, pParam));
	    	con = Connector.getConnection(getSessionId());
	    	st = con.prepareStatement(mySQL);
	    	st = prepareParam(st, pParam);
	    	ResultSet rset = st.executeQuery();
            ResultSetMetaData mtd = rset.getMetaData();
            
            int colCount = mtd.getColumnCount();

            html.append(getBottomFrameTable());
	        html.append("<tr>\n");
	        for (int i=1; i <= colCount-2; i++) {
	        	html.append(getBottomFrameTableTH(messageXML, mtd.getColumnName(i)));
            }
	        html.append("</tr></thead><tbody>");
	        
	        while (rset.next()) {
	        	html.append("<tr>\n");
		        for (int i=1; i <= colCount-2; i++) {
	        		if (("ID_MESSAGE".equalsIgnoreCase(mtd.getColumnName(i)) || "TITLE_MESSAGE".equalsIgnoreCase(mtd.getColumnName(i))) &&
	        				"EMAIL".equalsIgnoreCase(rset.getString("TYPE_MESSAGE2")) &&
	        				hasEmailMessagePermission) {
	        			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/dispatch/messages/email_messagesspecs.jsp?id="+rset.getString(i), "", ""));
	        		} else if (("ID_MESSAGE".equalsIgnoreCase(mtd.getColumnName(i)) || "TITLE_MESSAGE".equalsIgnoreCase(mtd.getColumnName(i))) && 
	        				"OFFICE".equalsIgnoreCase(rset.getString("TYPE_MESSAGE2")) &&
	        				hasOfficeMessagePermission) {
	        			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/dispatch/messages/office_messagesspecs.jsp?id="+rset.getString(i), "", ""));
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
	  } // getMessagesHTML

}
