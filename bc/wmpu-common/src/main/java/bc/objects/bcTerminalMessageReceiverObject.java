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

public class bcTerminalMessageReceiverObject extends bcObject {
	private final static Logger LOGGER=Logger.getLogger(bcTerminalMessageReceiverObject.class);
	private Map<String,Comparable<String>> fieldHm = new HashMap<String, Comparable<String>>();
	
	private String idMessageReceiver;
	
	public bcTerminalMessageReceiverObject(String pIdMessageReceiver) {
		this.idMessageReceiver = pIdMessageReceiver;
		getFeature();
	}
	
	private void getFeature() {
		String mySQL = "SELECT * FROM " + getGeneralDBScheme() + ".VC_TERM_MESSAGE_RECEIVER_ALL WHERE id_term_message_receiver = ?";
        fieldHm = getFeatures2(mySQL, this.idMessageReceiver, false);
	}
	
	public String getValue(String pColumnName) {
		return getFeautureResult(fieldHm, pColumnName);
	}



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
            "                  WHERE id_term_message_receiver = ? ";
        pParam.add(new bcFeautureParam("int", this.idMessageReceiver));
        
    	if (!isEmpty(pFindString)) {
    		mySQL = mySQL + 
   				" AND (TO_CHAR(id_term) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(text_term_message) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(send_date_frmt) LIKE UPPER('%'||?||'%') OR " +
   				"      TO_CHAR(id_telgr) LIKE UPPER('%'||?||'%'))";
    		for (int i=0; i<5; i++) {
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
        //boolean hasServicePlacePermission = false;
        
        try{
        	if (isEditMenuPermited("CLIENTS_TERMSES")>=0) {
        		hasTermSesPermission = true;
        	}
        	//if (isEditMenuPermited("CLIENTS_SERVICE_PLACE")>=0) {
        	//	hasServicePlacePermission = true;
        	//}
        	
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
          	  	for (int i=1; i <= colCount-2; i++) {
          	  		if ("ID_TELGR".equalsIgnoreCase(mtd.getColumnName(i)) && hasTermSesPermission) {
             	  		html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/clients/telegramspecs.jsp?id="+rset.getString("ID_TELGR"), "", ""));
          	  		//} else if ("NAME_SERVICE_PLACE".equalsIgnoreCase(mtd.getColumnName(i)) && hasServicePlacePermission) {
          	  		//	html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/clients/service_placespecs.jsp?id="+rset.getString("ID_SERVICE_PLACE"), "", ""));
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
    	
    	String pWhereCause = "   WHERE id_term_message_receiver = ? ";
    	
    	ArrayList<bcFeautureParam> pWhereValue = new ArrayList<bcFeautureParam>();
    	pWhereValue.add(new bcFeautureParam("int", this.idMessageReceiver));

    	String lDeleteLink = "";
    	String lEditLink = "";
    	
    	return list.getTermMessageCardOperationsHTML(pWhereCause, pWhereValue, pFindString, pOperType, pOperState, lEditLink, lDeleteLink, "div_data_detail", p_beg, p_end);
    	
    }

}
