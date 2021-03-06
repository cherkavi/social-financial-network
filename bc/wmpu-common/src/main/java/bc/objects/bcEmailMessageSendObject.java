package bc.objects;

import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import bc.connection.Connector;

public class bcEmailMessageSendObject extends bcObject {
	private final static Logger LOGGER=Logger.getLogger(bcEmailMessageSendObject.class);
	
	private Map<String,Comparable<String>> fieldHm = new HashMap<String, Comparable<String>>();
	
	private String idMessageSend;
	
	public bcEmailMessageSendObject(String pIdMessageSend) {
		this.idMessageSend = pIdMessageSend;
		getFeature();
	}
	
	private void getFeature() {
		String mySQL = "SELECT * FROM " + getGeneralDBScheme() + ".VC_DS_MESSAGES_SEND_ALL WHERE id_ds_message_send = ?";
        fieldHm = getFeatures2(mySQL, this.idMessageSend, false);
	}
	
	public String getValue(String pColumnName) {
		return getFeautureResult(fieldHm, pColumnName);
	}

    public String getAttachedFilesHTML() {
        StringBuilder html = new StringBuilder();
        Connection con = null;
        PreparedStatement st = null;
        String mySQL = 
        	" SELECT id_ds_message_file, file_name, stored_file_name, id_ds_message, id_ds_message_send " +
            "   FROM " + getGeneralDBScheme() + ".vc_ds_messages_files_all" +
            "  WHERE id_ds_message_send = ? " +
            "  ORDER BY file_name";
        
        try{
        	LOGGER.debug(mySQL + 
            		", 1={" + this.idMessageSend + ",int}");
        	con = Connector.getConnection(getSessionId());
            st = con.prepareStatement(mySQL);
            st.setInt(1, Integer.parseInt(this.idMessageSend));
            ResultSet rset = st.executeQuery();
            
            while (rset.next())
            {
          	  	html.append(rset.getString("file_name") + 
   	  				"&nbsp;&nbsp;<a href=\"../FileSender?FILENAME=" + URLEncoder.encode(rset.getString("stored_file_name"),"UTF-8") + "\" " +
					"  title=\"" + buttonXML.getfieldTransl("open", false) + "\" target=_blank>" +
					"<img vspace=\"0\" hspace=\"0\" src=\"../images/oper/small/open.gif\" align=\"top\" style=\"border: 0px;\"> " +
					"</a>");
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
