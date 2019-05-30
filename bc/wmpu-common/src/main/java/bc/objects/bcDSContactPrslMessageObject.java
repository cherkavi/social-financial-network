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


public class bcDSContactPrslMessageObject extends bcObject {
	private final static Logger LOGGER=Logger.getLogger(bcDSContactPrslMessageObject.class);
	private Map<String,Comparable<String>> fieldHm = new HashMap<String, Comparable<String>>();
	
	private String idMessage;
	
	public bcDSContactPrslMessageObject(String pIdMessage) {
		this.idMessage = pIdMessage;
		getFeature();
	}
	
	public void getFeature() {
		String featureSelect = " SELECT * FROM " + getGeneralDBScheme() + ".VC_CONTACT_PRS_MES_CLUB_ALL WHERE id_message = ?";
		fieldHm = getFeatures2(featureSelect, this.idMessage, false);
	}
	
	public String getValue(String pColumnName) {
		return getFeautureResult(fieldHm, pColumnName);
	}

    public String getMessagesSendInfoHTML(String p_beg, String p_end) {
        StringBuilder html = new StringBuilder();
        Connection con = null;
        PreparedStatement st = null;
        String mySQL = "";

        ArrayList<bcFeautureParam> pParam = initParamArray();

        if ("EMAIL".equalsIgnoreCase(this.getValue("TYPE_MESSAGE"))) {
	        mySQL = 
	        	" SELECT rn, id_message_send, send_date, email, state_send, error_message " +
	            "   FROM (SELECT ROWNUM rn, id_message_send, send_date, email, state_send, error_message " +
	            "           FROM (SELECT id_message_send, send_date_frmt send_date, mail, " +
	            "               	 	 DECODE(state_send, " +
	  		  	"                      			'N', '<font color=\"black\">'||state_send_tsl||'</font>', " +
				"                      			'S', '<font color=\"green\">'||state_send_tsl||'</font>', " +
				"                      			'C', '<font color=\"blue\">'||state_send_tsl||'</font>', " +
				"                      			'E', '<b><font color=\"red\">'||state_send_tsl||'</font></b>', " +
				"                      			state_send_tsl) state_send, " +
				" 					     text_message error_message " +
	            "                   FROM " + getGeneralDBScheme() + ".vc_contact_prs_mes_send_all" +
	            "                  WHERE id_message = ? " + 
	            "                  ORDER BY send_date desc" +
	            "        ) WHERE ROWNUM < ? " + 
	            " ) WHERE rn >= ?";

	        pParam.add(new bcFeautureParam("int", this.idMessage));
	        pParam.add(new bcFeautureParam("int", p_end));
	        pParam.add(new bcFeautureParam("int", p_beg));
	        
        } else if ("OFFICE".equalsIgnoreCase(this.getValue("TYPE_MESSAGE"))) {
        	mySQL = 
	        	" SELECT rn, id_message_send, send_date, state_send, error_message " +
	            "   FROM (SELECT ROWNUM rn, id_message_send, send_date, state_send, error_message " +
	            "           FROM (SELECT id_message_send, send_date_frmt send_date, " +
	            "               	 	 DECODE(state_send, " +
	  		  	"                      			'N', '<font color=\"black\">'||state_send_tsl||'</font>', " +
				"                      			'S', '<font color=\"green\">'||state_send_tsl||'</font>', " +
				"                      			'C', '<font color=\"blue\">'||state_send_tsl||'</font>', " +
				"                      			'E', '<b><font color=\"red\">'||state_send_tsl||'</font></b>', " +
				"                      			state_send_tsl) state_send, " +
				" 					     text_message error_message " +
	            "                   FROM " + getGeneralDBScheme() + ".vc_contact_prs_mes_send_all" +
	            "                  WHERE id_message = ? " + 
	            "                  ORDER BY send_date desc" +
	            "        ) WHERE ROWNUM < ? " + 
	            " ) WHERE rn >= ?";

	        pParam.add(new bcFeautureParam("int", this.idMessage));
	        pParam.add(new bcFeautureParam("int", p_end));
	        pParam.add(new bcFeautureParam("int", p_beg));
        }
        try{
        	LOGGER.debug(prepareSQLToLog(mySQL, pParam));
        	con = Connector.getConnection(getSessionId());
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
            html.append("</tr></thead><tbody>");
            
            while (rset.next())
            {
            	html.append("<tr>");
          	  	for (int i=1; i <= colCount; i++) {
          	  		html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", "", ""));
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

    public String getAttachedFilesHTML(boolean hasEditPermission) {
        StringBuilder html = new StringBuilder();
        Connection con = null;
        PreparedStatement st = null;
        String mySQL = 
        	" SELECT id_message_file, file_name, stored_file_name " +
            "   FROM " + getGeneralDBScheme() + ".vc_contact_prs_mes_files_all" +
            "  WHERE id_message = ? " + 
            "  ORDER BY file_name";
        
        try{
        	LOGGER.debug(mySQL + 
	    			", 1={" + this.idMessage + ",int}");
        	con = Connector.getConnection(getSessionId());
        	st = con.prepareStatement(mySQL);
        	st.setInt(1, Integer.parseInt(this.idMessage));
        	ResultSet rset = st.executeQuery();
            
            while (rset.next())
            {
          	  	html.append(rset.getString("file_name") + 
   	  				"&nbsp;&nbsp;<a href=\"../FileSender?FILENAME=" + URLEncoder.encode(rset.getString("stored_file_name"),"UTF-8") + "\" " +
					"  title=\"" + buttonXML.getfieldTransl("open", false) + "\" target=_blank>" +
					"<img vspace=\"0\" hspace=\"0\" src=\"../images/oper/small/open.gif\" align=\"top\" style=\"border: 0px;\"> " +
					"</a>");
      	  		if (hasEditPermission) {
      	  			if ("EMAIL".equalsIgnoreCase(this.getValue("TYPE_MESSAGE"))) {
	       	  			html.append("&nbsp;<a href=\"#\" onclick=\"if (window.confirm('" + documentXML.getfieldTransl("l_remove_file", false) + " \\\'" + rset.getString("file_name") + "\\\'?')) {ajaxpage('../crm/dispatch/messages/email_messagesupdate.jsp?id=" + this.idMessage + "&type=general&process=yes&action=remove_src&id_file=" + rset.getString("id_message_file") + "', 'div_main')}\" " +
	  						" title=\"" + buttonXML.getfieldTransl("button_delete", false) + "\"> " + 
	  						"<img vspace=\"0\" hspace=\"0\" src=\"../images/oper/small/delete.gif\" align=\"top\" style=\"border: 0px;\">" +
	      	  				"</a>");
      	  			} else if ("OFFICE".equalsIgnoreCase(this.getValue("TYPE_MESSAGE"))) {
      	  				html.append("&nbsp;<a href=\"#\" onclick=\"if (window.confirm('" + documentXML.getfieldTransl("l_remove_file", false) + " \\\'" + rset.getString("file_name") + "\\\'?')) {ajaxpage('../crm/dispatch/messages/office_messagesupdate.jsp?id=" + this.idMessage + "&type=general&process=yes&action=remove_src&id_file=" + rset.getString("id_message_file") + "', 'div_main')}\" " +
	  						" title=\"" + buttonXML.getfieldTransl("button_delete", false) + "\"> " + 
	  						"<img vspace=\"0\" hspace=\"0\" src=\"../images/oper/small/delete.gif\" align=\"top\" style=\"border: 0px;\">" +
	      	  				"</a>");
      	  			}
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
	
}
