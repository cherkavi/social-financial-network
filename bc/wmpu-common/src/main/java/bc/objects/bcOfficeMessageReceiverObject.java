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

public class bcOfficeMessageReceiverObject extends bcObject {
	private final static Logger LOGGER=Logger.getLogger(bcOfficeMessageReceiverObject.class);
	private Map<String,Comparable<String>> fieldHm = new HashMap<String, Comparable<String>>();
	
	private String idMessageReceiver;
	
	public bcOfficeMessageReceiverObject(String pIdMessageReceiver) {
		this.idMessageReceiver = pIdMessageReceiver;
		getFeature();
	}
	
	private void getFeature() {
		String mySQL = "SELECT * FROM " + getGeneralDBScheme() + ".VC_DS_MESSAGES_RECEIVER_ALL WHERE id_ds_message_receiver = ?";
        fieldHm = getFeatures2(mySQL, this.idMessageReceiver, false);
	}
	
	public String getValue(String pColumnName) {
		return getFeautureResult(fieldHm, pColumnName);
	}

	public String getMessageSendHTML(String pFindString, String pState, String p_beg, String p_end) {
		ArrayList<bcFeautureParam> pParam = initParamArray();

		String mySQL = 
	    		" SELECT rn, name_ds_receiver_dispatch_kind, email, title_ds_message, name_ds_message_send_state, send_date_frmt, " +
	    		"		 cd_ds_message_send_state, id_ds_message_send, " +
				"		 id_nat_prs_receiver, id_contact_prs_receiver, " +
				"		 id_tr_person_receiver, id_user_receiver, cd_ds_receiver_dispatch_kind " +
	            "   FROM (SELECT ROWNUM rn, name_ds_receiver_dispatch_kind, name_ds_message_send_state, send_date_frmt, email, " +
	            "				 title_ds_message, cd_ds_message_send_state, id_ds_message_send, " +
				"				 id_nat_prs_receiver, id_contact_prs_receiver, " +
				"				 id_tr_person_receiver, id_user_receiver, cd_ds_receiver_dispatch_kind " +
	            "           FROM (SELECT DECODE(cd_ds_message_send_state, " +
	  		  	"                      			'N', '<font color=\"black\">'||name_ds_message_send_state||'</font>', " +
				"                      			'S', '<font color=\"green\">'||name_ds_message_send_state||'</font>', " +
				"                      			'C', '<font color=\"blue\">'||name_ds_message_send_state||'</font>', " +
				"                      			'E', '<b><font color=\"red\">'||name_ds_message_send_state||'</font></b>', " +
				"                      			name_ds_message_send_state" +
				"						 ) name_ds_message_send_state, " +
				"						 DECODE(cd_ds_receiver_dispatch_kind, " +
  		  		"                      				 'CLIENT', '<b><font color=\"green\">'||name_ds_receiver_dispatch_kind||'</font></b>', " +
  		  		"                      				 'PARTNER', '<b><font color=\"blue\">'||name_ds_receiver_dispatch_kind||'</font></b>', " +
  		  		"                               	 'SYSTEM', '<font color=\"red\"><b>'||name_ds_receiver_dispatch_kind||'</b></font>', " +
  		  		"                               	 'TRAINING', '<font color=\"darkgray\">'||name_ds_receiver_dispatch_kind||'</font>', " +
  		  		"                             		 'LG_PROMOTER', '<font color=\"black\">'||name_ds_receiver_dispatch_kind||'</font>', " +
  		  		"                               	 'UNKNOWN', '', " +
  		  		"                      				 name_ds_receiver_dispatch_kind" +
  		  		"						 ) name_ds_receiver_dispatch_kind, cd_ds_receiver_dispatch_kind, " +
  		  		"						 cd_ds_message_send_state, " +
				"						 send_date_frmt, email, title_ds_message, id_ds_message_send, " +
				"						 id_nat_prs_receiver, id_contact_prs_receiver, " +
				"						 id_tr_person_receiver, id_user_receiver " +
	   		 	"                   FROM " + getGeneralDBScheme() + ".vc_ds_messages_send_all " +
	   		 	"                  WHERE id_ds_message_receiver = ? ";
		pParam.add(new bcFeautureParam("int", this.idMessageReceiver));
		
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
        
        boolean hasNatPrsPermission = false;
        boolean hasContactPrsPermission = false;
        boolean hasTrainingPermission = false;
        boolean hasSystemUserPermission = false;
        
        try{
        	if (isEditMenuPermited("CLIENTS_NATPERSONS")>=0) {
        		hasNatPrsPermission = true;
        	}
        	if (isEditMenuPermited("CLIENTS_CONTACT_PRS")>=0) {
        		hasContactPrsPermission = true;
        	}
        	if (isEditMenuPermited("TRAINING_TRAININGS")>=0) {
        		hasTrainingPermission = true;
        	}
        	if (isEditMenuPermited("SECURITY_USERS")>=0) {
        		hasSystemUserPermission = true;
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
            for (int i=1; i <= colCount-7; i++) {
            	html.append(getBottomFrameTableTH(messageXML, mtd.getColumnName(i)));
            }
            html.append("<th>&nbsp;</th>\n");
            html.append("</tr></thead><tbody>\n");
            while (rset.next())
            {
                html.append("<tr>\n");
                if ("E".equalsIgnoreCase(rset.getString("CD_DS_MESSAGE_SEND_STATE"))) {
                	myFont = "<font color=\"red\">";
                } else {
                	myFont = "";
                }
                for (int i=1; i<=colCount-7; i++) {
                	if ("EMAIL".equalsIgnoreCase(mtd.getColumnName(i))) {
           				if (hasNatPrsPermission &&
     	        			!isEmpty(rset.getString("ID_NAT_PRS_RECEIVER"))) {
       	        			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/clients/natpersonspecs.jsp?id="+rset.getString("ID_NAT_PRS_RECEIVER"), myFont, ""));
       	        		} else if (hasContactPrsPermission &&
       	        			!isEmpty(rset.getString("ID_CONTACT_PRS_RECEIVER"))) {
       	        			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/clients/contact_prsspecs.jsp?id="+rset.getString("ID_CONTACT_PRS_RECEIVER"), myFont, ""));
       	        		} else if (hasTrainingPermission &&
       	        			!isEmpty(rset.getString("ID_TR_PERSON_RECEIVER"))) {
       	        			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/training/participant_personspecs.jsp?id="+rset.getString("ID_TR_PERSON_RECEIVER"), myFont, ""));
       	        		} else if (hasSystemUserPermission &&
       	        			!isEmpty(rset.getString("ID_USER_RECEIVER"))) {
       	        			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/security/userspecs.jsp?id="+rset.getString("ID_USER_RECEIVER"), myFont, ""));
       	        		} else {
       	        			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", myFont, ""));
       	        		}
           			} else {
           				html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", myFont, ""));
           			}
                }
                String myHyperLink = "../crm/dispatch/messages/email_messagereceiverupdate.jsp?type=send&id="+this.idMessageReceiver+"&id_send="+rset.getString("ID_DS_MESSAGE_SEND");
              	html.append(getEditButtonHTML(myHyperLink));
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

}
