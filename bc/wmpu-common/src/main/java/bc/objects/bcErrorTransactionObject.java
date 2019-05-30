package bc.objects;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import bc.connection.Connector;


public class bcErrorTransactionObject extends bcObject {
	private final static Logger LOGGER=Logger.getLogger(bcErrorTransactionObject.class);
	private Map<String,Comparable<String>> fieldHm = new HashMap<String, Comparable<String>>();
	
	private String idTrans;
	
	public bcErrorTransactionObject(String pIdTrans) {
		this.idTrans = pIdTrans;
		getFeature();
	}
	
	private void getFeature() {
		String featureSelect = " SELECT * FROM " + getGeneralDBScheme() + ".VC_TRANS_ALL_ERROR_ALL WHERE id_trans = ?";
		fieldHm = getFeatures2(featureSelect, this.idTrans, false);
	}
	
	public String getValue(String pColumnName) {
		return getFeautureResult(fieldHm, pColumnName);
	}
	
    public String getTransactionsTelegramsHTML() {
        StringBuilder html = new StringBuilder();
        Connection con = null;
        PreparedStatement st = null;
        String mySQL = 	" SELECT id_term_ses, id_telgr, date_telgr_frmt date_telgr, cd_telgr_type, tel_identifier, " +
        				"       tel_length, nt_msg_b, tel_version, vk_enc, id_sam, "+
        				"       nt_sam, id_term, name_telgr_state "+
        				"   FROM " + getGeneralDBScheme() + ".vc_telgr_club_all c WHERE id_telgr IN " +
        				" (SELECT id_telgr FROM " + getGeneralDBScheme() + ".vc_trans_all_error_all " +
        				"   WHERE id_trans = ?)";
        boolean hasTermSesPermission = false;
        boolean hasTermPermission = false;
        boolean hasSAMPermission = false;
        try{
        	if (isEditMenuPermited("CLIENTS_TERMSES")>=0) {
        		hasTermSesPermission = true;
        	}
        	if (isEditMenuPermited("CLIENTS_TERMINALS")>=0) {
        		hasTermPermission = true;
        	}
        	if (isEditMenuPermited("CLIENTS_SAM")>=0) {
        		hasSAMPermission = true;
        	}
        	
        	LOGGER.debug(mySQL + 
            		", 1={" + this.idTrans + ",int}");
        	con = Connector.getConnection(getSessionId());
        	st = con.prepareStatement(mySQL);
        	st.setInt(1, Integer.parseInt(this.idTrans));
        	ResultSet rset = st.executeQuery();
            ResultSetMetaData mtd = rset.getMetaData();
            int colCount = mtd.getColumnCount();

            html.append(getBottomFrameTable());
            html.append("<tr>");
            for (int i=1; i <= colCount; i++) {
            	html.append(getBottomFrameTableTH(telegramXML, mtd.getColumnName(i)));
            }
            html.append("</tr></thead><tbody>");
            while (rset.next())
            {
            	html.append("<tr>");
          	  	for (int i=1; i <= colCount; i++) {
          	  		if (hasTermSesPermission && "ID_TERM_SES".equalsIgnoreCase(mtd.getColumnName(i))) {
          	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/clients/termsespecs.jsp?id="+rset.getString(i), "", ""));
          	  		} else if (hasSAMPermission && "ID_SAM".equalsIgnoreCase(mtd.getColumnName(i))) {
          	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/clients/samspecs.jsp?id="+rset.getString(i), "", ""));
          	  		} else if (hasTermPermission && "ID_TERM".equalsIgnoreCase(mtd.getColumnName(i))) {
          	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/clients/terminalspecs.jsp?id="+rset.getString(i), "", ""));
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
    } //getTransactionsTelegramsHTML

    /*public String getTransSysMonitorHTML(String pFindString, String row_type, String p_beg, String p_end) {
        StringBuilder html = new StringBuilder();
        Connection con = null;        
        PreparedStatement st = null; 
        
        ArrayList<bcFeautureParam> pParam = initParamArray();

        String myFont = "";
        String myBgColor = noneBackGroundStyle;
        
        String mySQL =
        	"SELECT rn, id_monitor, id_telgr, --id_trans," +
            "       creation_date_frmt, exc_code, --check_phase," + 
            "       check_result_tsl," +
            "       name_param1||': '||value_param1 value_param1, name_param2||': '||value_param2 value_param2, " +
            "       --user_log_id, outher_sid, " + 
            "       check_result, exc_desc" +
            "  FROM (SELECT ROWNUM rn, a.id_monitor, a.creation_date_frmt, a.exc_code, a.exc_desc, " +
            "	            a.check_phase, a.check_result, a.check_result_tsl," +
            "               a.name_param1, a.value_param1, a.name_param2, a.value_param2, " +
            "               a.id_telgr, a.id_trans, a.user_log_id, a.outher_sid" +
            "  		   FROM (SELECT * " +
            "                  FROM " + getGeneralDBScheme() + ".vc_sys_monitor_all " +
            " 		          WHERE id_trans = ? ";
        pParam.add(new bcFeautureParam("int", this.idTrans));
        
    	if (!isEmpty(pFindString)) {
    		mySQL = mySQL + 
   				" AND (UPPER(exc_code) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(creation_date_frmt) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(name_param1) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(value_param1) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(name_param2) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(value_param2) LIKE UPPER('%'||?||'%'))";
    		for (int i=0; i<5; i++) {
    		    pParam.add(new bcFeautureParam("string", pFindString));
    		}
    	}
        if (!isEmpty(row_type)) {
        	mySQL = mySQL + " AND NVL(check_result,-9) = ? ";
        	pParam.add(new bcFeautureParam("int", row_type));
        }

        pParam.add(new bcFeautureParam("int", p_end));
        pParam.add(new bcFeautureParam("int", p_beg));
        mySQL = mySQL + 
        	"                ORDER BY id_monitor) a " +
        	"         WHERE ROWNUM < ? " + 
        	" ) WHERE rn >= ?";
        
        boolean hasTelegramPermission = false;
       
        try{
            if (isEditMenuPermited("CLIENTS_TERMSES")>=0) {
            	hasTelegramPermission = true;
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
              	html.append(getBottomFrameTableTH(sysmonitorXML, mtd.getColumnName(i)));
            }
            html.append("</tr></thead><tbody>\n");
            
            while (rset.next())
                {
                    html.append("<tr>");
                    if (rset.getString("CHECK_RESULT").equalsIgnoreCase("0")) {
                    	myFont = "";
                    	myBgColor = noneBackGroundStyle;
                    } else {
                    	myFont = "<b><font color=\"red\">";
                    	myBgColor = selectedBackGroundStyle;
                    }
                    for (int i=1; i <= colCount-2; i++) {
	              	  	if ("ID_TELGR".equalsIgnoreCase(mtd.getColumnName(i))  && hasTelegramPermission && 
	              	  			!isEmpty(rset.getString(i))) {
	          	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/clients/telegramspecs.jsp?id="+rset.getString(i), "", ""));
	          	  		} else {
	          	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", myFont, myBgColor));
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
       }*/

}
