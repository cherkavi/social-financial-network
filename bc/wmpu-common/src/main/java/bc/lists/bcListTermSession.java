package bc.lists;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import bc.connection.Connector;
import bc.service.bcFeautureParam;

public class bcListTermSession extends bc.objects.bcObject {
	private final static Logger LOGGER=Logger.getLogger(bcListTermSession.class);
	
	public bcListTermSession() {
		
	}
	
	public String getTermSessionList(String pWhereCause, ArrayList<bcFeautureParam> pWhereValue, String pFind, String pDataType, String pDataState, String p_beg, String p_end){
		StringBuilder html = new StringBuilder();
        Connection con = null;
        PreparedStatement st = null;
        boolean hasTerminalPermission = false;
        boolean hasDealerPermission = false;
        boolean hasTermSesPermission = false;
        
        ArrayList<bcFeautureParam> pParam = initParamArray();

        String mySQL = 
    		" SELECT a.rn, a.id_term_ses, a.date_beg_frmt, a.date_end_frmt, a.sname_service_place, a.id_term, /*a.id_sam,*/ " +
            "        CASE WHEN a.need_card_req = 0 THEN b.name_term_ses_creq_state " +
    		"             ELSE DECODE(NVL(a.need_card_req, 0), " +
    		"                         0, '<font color=\"black\">'||b.name_term_ses_creq_state||'</font>', " +
    		"                         -1, '<font color=\"blue\">'||b.name_term_ses_creq_state||'</font>', " +
    		"                         -9, '<b><font color=\"red\">'||b.name_term_ses_creq_state||'</font></b>', " +
    		"                         1, '<font color=\"green\">'||b.name_term_ses_creq_state||'</font>', " +
    		"                         9, '<font color=\"darkgreen\">'||b.name_term_ses_creq_state||'</font>', " +
    		"                         '<font>'||b.name_term_ses_creq_state||'</font>' " +
    		"                  )||" +
    		"                  ' ('||RTRIM(DECODE(a.action_card_req," +
    		"                              0, '<span style=\"color:green;\" title=\"" + term_sesXML.getfieldTransl("h_card_req_action_0", false) + "\">', " +
    		"                              1, '<span style=\"color:blue;\" title=\"" + term_sesXML.getfieldTransl("h_card_req_action_1", false) + "\">', " +
    		"                              2, '<span style=\"color:red;font-weight:bold;\" title=\"" + term_sesXML.getfieldTransl("h_card_req_action_2", false) + "\">', " +
    		"                              3, '<span style=\"color:green;font-weight:bold;\" title=\"" + term_sesXML.getfieldTransl("h_card_req_action_3", false) + "\">', " +
    		"                              4, '<span style=\"color:red;font-weight:bold;\" title=\"" + term_sesXML.getfieldTransl("h_card_req_action_4", false) + "\">', " +
    		"                              5, '<span style=\"color:brown;font-weight:bold;\" title=\"" + term_sesXML.getfieldTransl("h_card_req_action_5", false) + "\">', " +
    		"                              6, '<span style=\"color:red;font-weight:bold;\" title=\"" + term_sesXML.getfieldTransl("h_card_req_action_6", false) + "\">', " +
    		"                              7, '<span style=\"color:red;font-weight:bold;\" title=\"" + term_sesXML.getfieldTransl("h_card_req_action_7", false) + "\">', " +
    		"                              8, '<span style=\"color:red;font-weight:bold;\" title=\"" + term_sesXML.getfieldTransl("h_card_req_action_8", false) + "\">', " +
    		"                              9, '<span style=\"color:red;font-weight:bold;\" title=\"" + term_sesXML.getfieldTransl("h_card_req_action_9", false) + "\">', " +
    		"                              15, '<span style=\"color:red;font-weight:bold;\" title=\"" + term_sesXML.getfieldTransl("h_card_req_action_15", false) + "\">', " +
    		"		                       '' " +
    		"                       )" +
    		"                   ||DECODE(action_card_req,NULL,'</span>',action_card_req||'</span>, ')||" +
    		"                   CASE WHEN NVL(need_online_pay,0) = 0 THEN '' " +
    		"                        ELSE '<span '||need_online_pay_state||' title=\""+term_sesXML.getfieldTransl("h_is_online_pay", false)+"\">"+term_sesXML.getfieldTransl("h_is_online_pay_short", false)+"</span> <span '||need_online_pay_style||' title=\"'||need_online_pay_state||'\">'||need_online_pay||'</span>, ' "+
    		"                   END||" +
    		"                   CASE WHEN NVL(need_club_pay,0) = 0 THEN '' " +
    		"                        ELSE '<span '||need_club_pay_state||' title=\""+term_sesXML.getfieldTransl("h_is_club_pay", false)+"\">"+term_sesXML.getfieldTransl("h_is_club_pay_short", false)+"</span> <span '||need_club_pay_style||' title=\"'||need_club_pay_state||'\">'||need_club_pay||'</span>, ' "+
    		"                   END||" +
    		"                   CASE WHEN NVL(need_online_storno,0) = 0 THEN '' " +
    		"                        ELSE '<span '||need_online_storno_state||' title=\""+term_sesXML.getfieldTransl("h_is_online_storno", false)+"\">"+term_sesXML.getfieldTransl("h_is_online_storno_short", false)+"</span> <span '||need_online_storno_style||' title=\"'||need_online_storno_state||'\">'||need_online_storno||'</span>, ' "+
    		"                   END||" +
    		"                   CASE WHEN NVL(need_adv_pay,0) = 0 THEN '' " +
    		"                        ELSE '<span '||need_adv_pay_state||' title=\""+term_sesXML.getfieldTransl("h_is_adv_pay", false)+"\">"+term_sesXML.getfieldTransl("h_is_adv_pay_short", false)+"</span> <span '||need_adv_pay_style||' title=\"'||need_adv_pay_state||'\">'||need_adv_pay||'</span>, ' "+
    		"                   END,', ')||')</span>'" +
    		"        END need_card_req_tsl," +
    		"        DECODE(NVL(a.need_col_data, 0), " +
    		"               0, c.name_term_ses_data_state, " +
    		"               -1, '<font color=\"blue\">'||c.name_term_ses_data_state||'</font>', " +
    		"               -9, '<b><font color=\"red\">'||c.name_term_ses_data_state||'</font></b>', " +
    		"               1, '<font color=\"green\">'||c.name_term_ses_data_state||'</font>', " +
    		"               c.name_term_ses_data_state " +
    		"        ) need_col_data_tsl, " +
    		"        DECODE(NVL(a.need_tpar_data, 0), " +
    		"               0, d.name_term_ses_param_state, " +
    		"               -1, '<font color=\"blue\">'||d.name_term_ses_param_state||'</font>', " +
    		"               -9, '<b><font color=\"red\">'||d.name_term_ses_param_state||'</font></b>', " +
    		"               1, '<font color=\"green\">'||d.name_term_ses_param_state||'</font>', " +
    		"               9, '<font color=\"darkgreen\">'||d.name_term_ses_param_state||'</font>', " +
    		"               d.name_term_ses_param_state" +
    		"        ) need_tpar_data_tsl, " +
    		"        DECODE(NVL(a.need_term_mon, 0), " +
    		"                0, e.name_term_ses_mon_state, " +
    		"                -1, '<font color=\"blue\">'||e.name_term_ses_mon_state||'</font>', " +
    		"                -9, '<b><font color=\"red\">'||e.name_term_ses_mon_state||'</font></b>', " +
    		"                1, '<font color=\"green\">'||e.name_term_ses_mon_state||'</font>', " +
            "                9, '<font color=\"darkgreen\">'||e.name_term_ses_mon_state||'</font>', " +
    		"               e.name_term_ses_mon_state" +
    		"        ) need_term_mon_tsl /*, " +
    		"        a.desc_term_ses*/, a.id_service_place " + 
    		"   FROM (SELECT rn, id_term_ses, id_term, id_service_place, sname_service_place, /*id_sam,*/  " +
    		"  			     date_beg_frmt, date_end_frmt, desc_term_ses,  " +
    		"  			     need_card_req, action_card_req, need_col_data, need_tpar_data, " +
    		"                need_term_mon, need_online_pay, " +
    		"                DECODE(NVL(need_online_pay, 0), " +
    		"                       -1, '" + term_sesXML.getfieldTransl("h_need_card_req_state_1neg", false) + "', " +
    		"                       -9, '" + term_sesXML.getfieldTransl("h_need_card_req_state_9neg", false) + "', " +
    		"                       1, '" + term_sesXML.getfieldTransl("h_need_card_req_state_1", false) + "', " +
            "                       9, '" + term_sesXML.getfieldTransl("h_need_card_req_state_9", false) + "', " +
    		"                       ''" +
    		"                ) need_online_pay_state," +
    		"                DECODE(NVL(need_online_pay, 0), " +
    		"                       -1, 'style=\"color:blue;\"', " +
    		"                       -9, 'style=\"color:red;font-weight:bold;\"', " +
    		"                       1, 'style=\"color:green;\"', " +
            "                       9, 'style=\"color:darkgreen;\"', " +
    		"                       ''" +
    		"                ) need_online_pay_style," +
    		"  			     need_online_storno, " +
    		"                DECODE(NVL(need_online_storno, 0), " +
    		"                       -1, '" + term_sesXML.getfieldTransl("h_need_card_req_state_1neg", false) + "', " +
    		"                       -9, '" + term_sesXML.getfieldTransl("h_need_card_req_state_9neg", false) + "', " +
    		"                       1, '" + term_sesXML.getfieldTransl("h_need_card_req_state_1", false) + "', " +
            "                       9, '" + term_sesXML.getfieldTransl("h_need_card_req_state_9", false) + "', " +
    		"                       ''" +
    		"                ) need_online_storno_state," +
    		"                DECODE(NVL(need_online_storno, 0), " +
    		"                       -1, 'style=\"color:blue;\"', " +
    		"                       -9, 'style=\"color:red;font-weight:bold;\"', " +
    		"                       1, 'style=\"color:green;\"', " +
            "                       9, 'style=\"color:darkgreen;\"', " +
    		"                       ''" +
    		"                ) need_online_storno_style," +
    		"                need_club_pay, " +
    		"                DECODE(NVL(need_club_pay, 0), " +
    		"                       -1, '" + term_sesXML.getfieldTransl("h_need_card_req_state_1neg", false) + "', " +
    		"                       -9, '" + term_sesXML.getfieldTransl("h_need_card_req_state_9neg", false) + "', " +
    		"                       1, '" + term_sesXML.getfieldTransl("h_need_card_req_state_1", false) + "', " +
            "                       9, '" + term_sesXML.getfieldTransl("h_need_card_req_state_9", false) + "', " +
    		"                       ''" +
    		"                ) need_club_pay_state," +
    		"                DECODE(NVL(need_club_pay, 0), " +
    		"                       -1, 'style=\"color:blue;\"', " +
    		"                       -9, 'style=\"color:red;font-weight:bold;\"', " +
    		"                       1, 'style=\"color:green;\"', " +
            "                       9, 'style=\"color:darkgreen;\"', " +
    		"                       ''" +
    		"                ) need_club_pay_style," +
    		"                need_adv_pay, " +
    		"                DECODE(NVL(need_adv_pay, 0), " +
    		"                       -1, '" + term_sesXML.getfieldTransl("h_need_card_req_state_1neg", false) + "', " +
    		"                       -9, '" + term_sesXML.getfieldTransl("h_need_card_req_state_9neg", false) + "', " +
    		"                       1, '" + term_sesXML.getfieldTransl("h_need_card_req_state_1", false) + "', " +
            "                       9, '" + term_sesXML.getfieldTransl("h_need_card_req_state_9", false) + "', " +
    		"                       ''" +
    		"                ) need_adv_pay_state," +
    		"                DECODE(NVL(need_adv_pay, 0), " +
    		"                       -1, 'style=\"color:blue;\"', " +
    		"                       -9, 'style=\"color:red;font-weight:bold;\"', " +
    		"                       1, 'style=\"color:green;\"', " +
            "                       9, 'style=\"color:darkgreen;\"', " +
    		"                       ''" +
    		"                ) need_adv_pay_style," +
    		"                name_last_input_telgr, name_last_output_telgr " +
    		"   FROM (SELECT ROWNUM rn, id_term_ses, id_term, id_service_place, sname_service_place, /*id_sam,*/  " +
    		"  			     date_beg_frmt, date_end_frmt, desc_term_ses,  " +
    		"  			     need_card_req, action_card_req, " +
    		"                need_col_data, need_tpar_data, need_term_mon, " +
    		"                need_online_pay, need_online_storno, need_club_pay, need_adv_pay, " +
    		"                name_last_input_telgr, name_last_output_telgr " +
    		"  			FROM (SELECT id_term_ses, id_term, id_service_place, sname_service_place, /*id_sam,*/ nt_sam_begin,  " +
    		"  						 TO_CHAR(date_beg,'dd.mm.rrrr hh24:mi:ss') date_beg_frmt,  " +
    		"  						 TO_CHAR(date_end,'dd.mm.rrrr hh24:mi:ss') date_end_frmt, desc_term_ses,  " +
    		"  						 need_card_req, action_card_req, need_col_data, need_tpar_data, " +
    		"                        need_term_mon, need_online_pay, need_online_storno, need_club_pay, need_adv_pay, " +
    		"                        name_last_input_telgr,  name_last_output_telgr " +
    		"                   FROM " + getGeneralDBScheme()+".vc_term_session_priv_all " +
    		(isEmpty(pWhereCause)?" WHERE 1=1 ":pWhereCause) ;
       	if (pWhereValue.size() > 0) {
       		for(int counter=0; counter<=pWhereValue.size()-1;counter++){
       			pParam.add(pWhereValue.get(counter));
       		}
       	}
    
	    if (!isEmpty(pFind)) {
			mySQL = mySQL + " AND (TO_CHAR(id_term_ses) LIKE UPPER('%'||?||'%') OR " +
				"TO_CHAR(id_term) LIKE UPPER('%'||?||'%') OR " + 
				"TO_CHAR(date_beg,'dd.mm.rrrr hh24:mi:ss') LIKE UPPER('%'||?||'%') OR " + 
				"TO_CHAR(date_end,'dd.mm.rrrr hh24:mi:ss') LIKE UPPER('%'||?||'%') OR " + 
				"UPPER(desc_term_ses) LIKE UPPER('%'||?||'%')) ";
			for (int i=0; i<5; i++) {
				pParam.add(new bcFeautureParam("string", pFind));
	    	}
		}
	    
	    if (!isEmpty(pDataType)) {
	    	if ("TERM_CARD_REQ".equalsIgnoreCase(pDataType)) {
	    		if (isEmpty(pDataState)) {
	    			mySQL = mySQL + " AND NVL(need_card_req, 0) <> 0 ";
	    		} else {
	    			mySQL = mySQL + " AND NVL(need_card_req, 0) = ? ";
	    			pParam.add(new bcFeautureParam("int", pDataState));
	    		}
	    	} else if ("TERM_CARD_CHECK".equalsIgnoreCase(pDataType)) {
	    		mySQL = mySQL + " AND NVL(need_club_pay, 0) = 0 AND NVL(need_online_pay, 0) = 0 AND NVL(need_online_storno, 0) = 0 AND NVL(need_adv_pay, 0) = 0 ";
	    		if (isEmpty(pDataState)) {
	    			mySQL = mySQL + " AND NVL(need_card_req, 0) <> 0 ";
	    		} else {
	    			mySQL = mySQL + " AND NVL(need_card_req, 0) = ? ";
	    			pParam.add(new bcFeautureParam("int", pDataState));
	    		}
	    	} else if ("TERM_CLUB_PAY".equalsIgnoreCase(pDataType)) {
	    		if (isEmpty(pDataState)) {
	    			mySQL = mySQL + " AND NVL(need_club_pay, 0) <> 0 ";
	    		} else {
	    			mySQL = mySQL + " AND NVL(need_club_pay, 0) = ? ";
	    			pParam.add(new bcFeautureParam("int", pDataState));
	    		}
	    	} else if ("TERM_ONLINE_PAY".equalsIgnoreCase(pDataType)) {
	    		if (isEmpty(pDataState)) {
	    			mySQL = mySQL + " AND NVL(need_online_pay, 0) <> 0 ";
	    		} else {
	    			mySQL = mySQL + " AND NVL(need_online_pay, 0) = ? ";
	    			pParam.add(new bcFeautureParam("int", pDataState));
	    		}
	    	} else if ("TERM_ADV_PAY".equalsIgnoreCase(pDataType)) {
	    		if (isEmpty(pDataState)) {
	    			mySQL = mySQL + " AND NVL(need_adv_pay, 0) <> 0 ";
	    		} else {
	    			mySQL = mySQL + " AND NVL(need_adv_pay, 0) = ? ";
	    			pParam.add(new bcFeautureParam("int", pDataState));
	    		}
	    	} else if ("TERM_ONLINE_STORNO".equalsIgnoreCase(pDataType)) {
	    		if (isEmpty(pDataState)) {
	    			mySQL = mySQL + " AND NVL(need_online_storno, 0) <> 0 ";
	    		} else {
	    			mySQL = mySQL + " AND NVL(need_online_storno, 0) = ? ";
	    			pParam.add(new bcFeautureParam("int", pDataState));
	    		}
	    	} else if ("TERM_COL_DATA".equalsIgnoreCase(pDataType)) {
	    		if (isEmpty(pDataState)) {
	    			mySQL = mySQL + " AND NVL(need_col_data, 0) <> 0 ";
	    		} else {
	    			mySQL = mySQL + " AND NVL(need_col_data, 0) = ? ";
	    			pParam.add(new bcFeautureParam("int", pDataState));
	    		}
	    	} else if ("APP_TPAR_DATA".equalsIgnoreCase(pDataType)) {
	    		if (isEmpty(pDataState)) {
	    			mySQL = mySQL + " AND NVL(need_tpar_data, 0) <> 0 ";
	    		} else {
	    			mySQL = mySQL + " AND NVL(need_tpar_data, 0) = ? ";
	    			pParam.add(new bcFeautureParam("int", pDataState));
	    		}
	    	} else if ("TERM_MON_REP".equalsIgnoreCase(pDataType)) {
	    		if (isEmpty(pDataState)) {
	    			mySQL = mySQL + " AND NVL(need_term_mon, 0) <> 0 ";
	    		} else {
	    			mySQL = mySQL + " AND NVL(need_term_mon, 0) = ? ";
	    			pParam.add(new bcFeautureParam("int", pDataState));
	    		}
	    	}
	    }
	    pParam.add(new bcFeautureParam("int", p_end));
		pParam.add(new bcFeautureParam("int", p_beg));
		mySQL = mySQL + 
    		"  				   ORDER BY id_term_ses desc ) " +
    		"		   WHERE ROWNUM < ?)" +
    		" WHERE rn >= ?) a," +
    		getGeneralDBScheme()+".vc_term_ses_creq_state_all b, " +
    		getGeneralDBScheme()+".vc_term_ses_data_state_all c, " +
    		getGeneralDBScheme()+".vc_term_ses_param_state_all d, " +
    		getGeneralDBScheme()+".vc_term_ses_mon_state_all e " +
            " WHERE a.need_card_req = b.cd_term_ses_creq_state " +
            "   AND a.need_col_data = c.cd_term_ses_data_state " +
    		"   AND a.need_tpar_data = d.cd_term_ses_param_state " +
    		"   AND a.need_term_mon = e.cd_term_ses_mon_state " +
    		"   ORDER BY rn";
      
		try{
			if (isEditMenuPermited("CLIENTS_TERMSES")>=0) {
      	  		hasTermSesPermission = true;
      	  	}
      	  	if (isEditMenuPermited("CLIENTS_TERMINALS")>=0) {
               	hasTerminalPermission = true;
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

                html.append(getBottomFrameTable());
                html.append("<tr>");
                for (int i=1; i <= colCount-1; i++) {
                    html.append(getBottomFrameTableTH(term_sesXML, mtd.getColumnName(i)));
                }
                html.append("</tr></thead><tbody>\n");
                while (rset.next())
                {
                	html.append("<tr>");
              	  	for (int i=1; i <= colCount-1; i++) {
	              	  	if (hasTermSesPermission && "ID_TERM_SES".equalsIgnoreCase(mtd.getColumnName(i))) {
		      	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/clients/termsespecs.jsp?id="+rset.getString("ID_TERM_SES"), "", ""));
		      	  		} else if (hasDealerPermission && "SNAME_SERVICE_PLACE".equalsIgnoreCase(mtd.getColumnName(i))) {
             	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/clients/yurpersonspecs.jsp?id="+rset.getString("ID_SERVICE_PLACE"), "", ""));
             	  		} else if (hasTerminalPermission && "ID_TERM".equalsIgnoreCase(mtd.getColumnName(i))) {
              	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/clients/terminalspecs.jsp?id="+rset.getString("ID_TERM"), "", ""));
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
