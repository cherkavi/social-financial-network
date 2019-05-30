package bc.objects;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import bc.connection.Connector;
import bc.service.bcFeautureParam;
import bc.objects.bcXML;


public class bcEntriesHistoryObject extends bcObject {
	private final static Logger LOGGER=Logger.getLogger(bcEntriesHistoryObject.class);
	
	private String idEntriesHistory;
	private String typeEntriesHistory;
	
	public bcEntriesHistoryObject(String pTypeEntriesHistory, String pIdEntriesHistory) {
		this.typeEntriesHistory = pTypeEntriesHistory;
		this.idEntriesHistory = pIdEntriesHistory;
	}
	
    public String getHistoryHTML(String pFindString, String p_beg, String p_end) {
    	ArrayList<bcFeautureParam> pParam = initParamArray();

    	StringBuilder html = new StringBuilder();
    	PreparedStatement st = null;
        Connection con = null;
        String mySQL = 
        	" SELECT cd_entry_type, date_modification_frmt||' - '||name_entry_type date_modification, time_modification_frmt, operation_type, " +
        	"        field_name, old_value, new_value, id_user_who_modified, is_base_value" +
            "   FROM (SELECT ROWNUM rn, cd_entry_type, name_entry_type, date_modification_frmt, time_modification_frmt, operation_type, " +
            "                field_name, old_value, new_value, id_user_who_modified, is_base_value " +
            "           FROM (SELECT cd_entry_type, name_entry_type, date_modification_frmt, time_modification_frmt, operation_type, " +
            "                        field_name, old_value, new_value, id_user_who_modified, is_base_value " +
            "                   FROM " + getGeneralDBScheme() + ".vc_sys_entries_h "+
            "                  WHERE cd_entry_type IN (" + this.typeEntriesHistory  + ") " +
            "                    AND id_entry = ? ";
        pParam.add(new bcFeautureParam("int", this.idEntriesHistory));
        
        if (!isEmpty(pFindString)) {
    		mySQL = mySQL + 
   				" AND (UPPER(date_modification_frmt) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(time_modification_frmt) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(old_value) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(new_value) LIKE UPPER('%'||?||'%'))";
    		for (int i=0; i<4; i++) {
    		    pParam.add(new bcFeautureParam("string", pFindString));
    		}
    	}
        pParam.add(new bcFeautureParam("int", p_end));
        pParam.add(new bcFeautureParam("int", p_beg));
        mySQL = mySQL + 
            "                  ORDER BY date_modification DESC, time_modification DESC, field_name" +
            "                ) " +
            "          WHERE ROWNUM < ? " + 
            "         )" +
            "   WHERE rn >= ?";
        
        String myFontDate = "";
        String myBgColorDate = selectedBackGroundStyle;
        
        String myFontDetail = "";
        String myBgColorDetail = noneBackGroundStyle;
        
        String currentDate = "";
        
        bcXML fieldXML = null;
        try{
        	LOGGER.debug(prepareSQLToLog(mySQL, pParam));
        	con = Connector.getConnection(getSessionId());
        	st = con.prepareStatement(mySQL);
        	st = prepareParam(st, pParam);
        	ResultSet rset = st.executeQuery();
            
            html.append(getBottomFrameTable());
            html.append("<tr>");
            html.append("<th colspan=\"6\">"+ commonXML.getfieldTransl("date_modification_frmt", false)+"</th>\n");
            html.append("</tr>");
            html.append("<tr>");
            html.append(getBottomFrameTableTH(commonXML, "TIME_MODIFICATION"));
            html.append(getBottomFrameTableTH(commonXML, "OPERATION_TYPE"));
            html.append(getBottomFrameTableTH(commonXML, "ID_USER_WHO_MODIFIED"));
            html.append(getBottomFrameTableTH(commonXML, "FIELD_NAME"));
            html.append(getBottomFrameTableTH(commonXML, "OLD_VALUE"));
            html.append(getBottomFrameTableTH(commonXML, "NEW_VALUE"));
            html.append("</tr></thead><tbody>");
            
            while (rset.next())
            {
            	fieldXML = getTranslateXML(rset.getString("CD_ENTRY_TYPE"));
            	html.append("<tr>");
          	  	if (!(rset.getString("DATE_MODIFICATION").equalsIgnoreCase(currentDate))) {
          	  		html.append(getBottomFrameTableTDBase("6","DATE_MODIFICATION", Types.OTHER, rset.getString("DATE_MODIFICATION"), "", myFontDate, myBgColorDate));
          	  		html.append("</tr>");
          	  		html.append("<tr>");
          	  		currentDate = rset.getString("DATE_MODIFICATION");
          	  	}
          	  	if ("Y".equalsIgnoreCase(rset.getString("IS_BASE_VALUE"))) {
          	  		myFontDetail = "<font color = \"black\"><b>";
          	  	} else {
          	  		myFontDetail = "";
          	  	}
          	  	String pFieldTranslate = fieldXML.getfieldTransl(rset.getString("FIELD_NAME"), false);
          	  	if (pFieldTranslate == null || "".equalsIgnoreCase(pFieldTranslate) || " ".equalsIgnoreCase(pFieldTranslate)) {
          	  		pFieldTranslate = rset.getString("FIELD_NAME");
          	  	} else {
          	  		pFieldTranslate = "<span title=\"" + rset.getString("FIELD_NAME") + "\">" + pFieldTranslate + "</span>";
          	  	}
          	    html.append(getBottomFrameTableTD("TIME_MODIFICATION_FRMT", rset.getString("TIME_MODIFICATION_FRMT"), "", myFontDetail, myBgColorDetail));
          	    String operType = commonXML.getfieldTransl("operation_update", false);
          	    if ("I".equalsIgnoreCase(rset.getString("OPERATION_TYPE"))) {
          	    	operType = "<b><font color=\"blue\">" + commonXML.getfieldTransl("operation_insert", false) + "</font></b>";
          	    } else if ("U".equalsIgnoreCase(rset.getString("OPERATION_TYPE"))) {
          	    	operType = "<b><font color=\"green\">" + commonXML.getfieldTransl("operation_update", false) + "</font></b>";
          	    } else if ("D".equalsIgnoreCase(rset.getString("OPERATION_TYPE"))) {
          	    	operType = "<b><font color=\"red\">" + commonXML.getfieldTransl("operation_delete", false) + "</font></b>";
          	    }
      	        html.append(getBottomFrameTableTD("OPERATION_TYPE", operType, "", myFontDetail, myBgColorDetail));
      	        html.append(getBottomFrameTableTD("ID_USER_WHO_MODIFIED", rset.getString("ID_USER_WHO_MODIFIED"), this.getAjaxHyperlink("ID_USER",rset.getString("ID_USER_WHO_MODIFIED")), "", ""));
      	      	html.append(getBottomFrameTableTD("FIELD_NAME", pFieldTranslate, "", myFontDetail, myBgColorDetail));
        	    html.append(getBottomFrameTableTD("OLD_VALUE", rset.getString("OLD_VALUE"), this.getAjaxHyperlink(rset.getString("FIELD_NAME"),rset.getString("OLD_VALUE")), "", ""));
          	    html.append(getBottomFrameTableTD("NEW_VALUE", rset.getString("NEW_VALUE"), this.getAjaxHyperlink(rset.getString("FIELD_NAME"),rset.getString("NEW_VALUE")), "", ""));
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
    } // getBKAccountsHTML
    
    private String getAjaxHyperlink(String pFieldName, String pValue) {
    	String return_value = "";
    	
    	if (isEmpty(pValue)) {
    		return return_value;
    	}
    	
    	if ("ID_SAM".equalsIgnoreCase(pFieldName) && isEditMenuPermited("CLIENTS_SAM")>=0) {
    		return_value = "../crm/clients/samspecs.jsp?id="+pValue;
    	}
    	if ("ID_USER".equalsIgnoreCase(pFieldName) && isEditMenuPermited("SECURITY_USERS")>=0) {
    		return_value = "../crm/security/userspecs.jsp?id="+pValue;
    	}
    	return return_value;
    }
    
    private bcXML getTranslateXML(String pOperationType) {
    	bcXML return_value = commonXML;
    	if ("TERM".equalsIgnoreCase(pOperationType)) {
    		return_value = terminalXML;
    	} else if ("TERM_SAM".equalsIgnoreCase(pOperationType)) {
    		return_value = samXML;
    	}
    	return return_value;
    }
}
