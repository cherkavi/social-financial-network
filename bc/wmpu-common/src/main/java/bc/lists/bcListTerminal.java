package bc.lists;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import bc.connection.Connector;
import bc.service.bcFeautureParam;


public class bcListTerminal extends bc.objects.bcObject {
	private final static Logger LOGGER=Logger.getLogger(bcListTerminal.class);
	
	public bcListTerminal() {
	}

	public String getTerminalsHTML(String pWhereCause, ArrayList<bcFeautureParam> pWhereValue, String pFindString, String pTermType, String pTermStatus, String pGoToLink, String pEditLink, String pDivName, String p_beg, String p_end) {
		StringBuilder html = new StringBuilder();
        Connection con = null;
        PreparedStatement st = null;
        boolean hasTerminalPermission = false;
        boolean hasDeviceTypePermission = false;
        boolean hasDealerPermission = false;
        
        ArrayList<bcFeautureParam> pParam = initParamArray();

        String mySQL = 
	        	" SELECT rn, id_term, term_serial_number, sname_service_place name_service_place, cash_desk_number, date_last_ses, name_term_type, name_term_status, " +
	        	"        date_location_frmt, date_extract_frmt, id_device_type, id_service_place " +
	        	"   FROM (SELECT ROWNUM rn, sname_service_place, id_term, term_serial_number, cash_desk_number, date_last_ses, name_term_type, name_term_status, " +
	        	"                date_location_frmt, date_extract_frmt, id_device_type, id_service_place " +
	        	"           FROM (SELECT s.sname_service_place, s.id_term, s.term_serial_number, s.cash_desk_number, " +
	        	"                        CASE WHEN TRUNC(s.date_last_ses) < TRUNC(SYSDATE)-1" +
	        	"                               THEN '<font color=\"red\"><b>'||s.date_last_ses_frmt||'</b></font>'" +
	        	"                             WHEN TRUNC(s.date_last_ses) = TRUNC(SYSDATE)-1" +
	        	"                               THEN '<font color=\"brown\"><b>'||s.date_last_ses_frmt||'</b></font>'" +
	        	"                             ELSE '<font color=\"green\">'||s.date_last_ses_frmt||'</font>' " +
	        	"                        END date_last_ses, " +
	        	"                        DECODE(cd_term_type, " +
	        	"                  				'PHYSICAL', '<b><font color=\"green\">'||name_term_type||'</font></b>', " +
	        	"                  				'WEBPOS', '<font color=\"blue\">'||name_term_type||'</font>', " +
	        	"                  				name_term_type" +
	        	"                		 ) name_term_type, " +
	        	"                        DECODE(s.cd_term_status, " +
	        	"                       		'ACTIVE', '<b><font color=\"green\">'||s.name_term_status||'</font></b>', " +
				"                       		'SETTING', '<font color=\"red\">'||s.name_term_status||'</font>', " +
				"                       		'EXCLUDED', '<b><font color=\"red\">'||s.name_term_status||'</font></b>', " +
				"                       		'BLOCKED', '<b><font color=\"red\">'||s.name_term_status||'</font></b>', " +
	        	"                               s.name_term_status" +
	        	"                        ) name_term_status,  " +
		        "                        s.date_location_frmt, s.date_extract_frmt, s.id_device_type, s.id_service_place " +
	        	"                   FROM " + getGeneralDBScheme() + ".vc_term_priv_all s " +
		(isEmpty(pWhereCause)?" WHERE 1=1 ":pWhereCause) ;
        
	   	if (pWhereValue.size() > 0) {
	   		for(int counter=0; counter<=pWhereValue.size()-1;counter++){
	   			pParam.add(pWhereValue.get(counter));
	   		}
	   	}
	        
	    if (!isEmpty(pFindString)) {
	    	mySQL = mySQL + 
	   			" AND (TO_CHAR(id_term) LIKE UPPER('%'||?||'%') OR " +
	   			"      UPPER(term_serial_number) LIKE UPPER('%'||?||'%'))";
	    	for (int i=0; i<2; i++) {
	    	    pParam.add(new bcFeautureParam("string", pFindString));
	    	}
	    }
	    if (!isEmpty(pTermStatus)) {
	    	mySQL = mySQL + " AND cd_term_status = ? ";
	    	pParam.add(new bcFeautureParam("string", pTermStatus));
	    }
	    pParam.add(new bcFeautureParam("int", p_end));
	    pParam.add(new bcFeautureParam("int", p_beg));
	       mySQL = mySQL + 
               "                  ORDER BY s.id_term" +
               "                ) " +
               "           WHERE ROWNUM < ? " + 
               " ) WHERE rn >= ?";
	       
	    String lLink = "";
        try{
        	if (isEditMenuPermited("CLIENTS_TERMINALS")>=0) {
            	hasTerminalPermission = true;
            }
        	if (isEditMenuPermited("CLUB_TERM_DEVICE_TYPE")>=0) {
        		hasDeviceTypePermission = true;
        	}
            if (isEditMenuPermited("CLIENTS_YURPERSONS")>=0) {
            	hasDealerPermission = true;
            }
        	
        	LOGGER.debug(prepareSQLToLog(mySQL, pParam));
        	con = Connector.getConnection(getSessionId());
        	st = con.prepareStatement(mySQL);
        	st = prepareParam(st, pParam);
        	ResultSet rset = st.executeQuery();
            ResultSetMetaData mtd = rset.getMetaData();
            int colCount = mtd.getColumnCount();

            html.append(isEmpty(pGoToLink)?getBottomFrameTable():getTableSorterTable());
            html.append("<tr>");
            for (int i=1; i <= colCount-2; i++) {
                html.append(getBottomFrameTableTH(terminalXML, mtd.getColumnName(i)));
            }
            if (!isEmpty(pEditLink)) {
            	html.append("<th>&nbsp;</th>\n");
            }
            html.append("</tr></thead><tbody>\n");
            while (rset.next())
            {
            	html.append("<tr>");
          	  	for (int i=1; i <= colCount-2; i++) {
          	  		if (hasTerminalPermission && ("ID_TERM".equalsIgnoreCase(mtd.getColumnName(i)) || "TERM_SERIAL_NUMBER".equalsIgnoreCase(mtd.getColumnName(i)))) {
          	  			lLink = isEmpty(pGoToLink)?"../crm/clients/terminalspecs.jsp?id="+rset.getString("ID_TERM"):(pGoToLink+"&id_term="+rset.getString("ID_TERM"));
	          	  	} else if ("NAME_SERVICE_PLACE".equalsIgnoreCase(mtd.getColumnName(i)) &&
	          	  				hasDealerPermission &&
	          	  				!isEmpty(rset.getString("ID_SERVICE_PLACE"))) {
	          	  		lLink = isEmpty(pGoToLink)?"../crm/clients/yurpersonspecs.jsp?id="+rset.getString("ID_SERVICE_PLACE"):(pGoToLink+"&id_term="+rset.getString("ID_TERM"));
         	  		} else if (hasDeviceTypePermission && "NAME_DEVICE_TYPE".equalsIgnoreCase(mtd.getColumnName(i)) &&
         	  				!isEmpty(rset.getString("ID_DEVICE_TYPE"))) {
         	  			lLink = isEmpty(pGoToLink)?"../crm/club/term_device_typespecs.jsp?id="+rset.getString("ID_DEVICE_TYPE"):(pGoToLink+"&id_term="+rset.getString("ID_TERM"));
         	  		} else {
         	  			lLink = isEmpty(pGoToLink)?"":(pGoToLink+"&id_term="+rset.getString("ID_TERM"));
          	  		}
          	  		//System.out.println("lLink="+lLink);
          	  		html.append(getBottomFrameTableTDBase("", mtd.getColumnName(i), Types.OTHER, rset.getString(i), lLink, isEmpty(pDivName)?"div_main":pDivName, "", "", ""));
          	  		//html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), lLink, "", ""));
          	  	}

          	  	if (!isEmpty(pEditLink)) {
            	   String myEditLink = pEditLink+"&id_term="+rset.getString("ID_TERM");
            	   html.append(getEditButtonWitDivHTML(myEditLink, "div_data_detail"));
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
