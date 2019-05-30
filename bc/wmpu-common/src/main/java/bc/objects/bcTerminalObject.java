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
import bc.lists.bcListTermSession;
import bc.lists.bcListTerminalSAM;
import bc.lists.bcListTerminalUser;
import bc.lists.bcListTransaction;
import bc.service.bcFeautureParam;


public class bcTerminalObject extends bcObject {
	private final static Logger LOGGER=Logger.getLogger(bcTerminalObject.class);
	private Map<String,Comparable<String>> fieldHm = new HashMap<String, Comparable<String>>();
	
	
	private String idTerminal;

	ArrayList<bcFeautureParam> pRelationshipFilterParam = initParamArray();

	public bcTerminalObject(String pIdTerm) {
		this.idTerminal = pIdTerm;
	}
	
	public void getFeature() {
		String featureSelect = " SELECT * FROM " + getGeneralDBScheme() + ".VC_TERM_ALL WHERE id_term = ?";
		fieldHm = getFeatures2(featureSelect, this.idTerminal, false);
	}
	
	public String getValue(String pColumnName) {
		return getFeautureResult(fieldHm, pColumnName);
	}
	
    public String getTermCertificateOptions(String id_cert, String id_cert_exclude, boolean isNull) {
    	ArrayList<bcFeautureParam> pParam = initParamArray();
    	
    	String mySQL = 
    		" SELECT id_term_certificate, TO_CHAR(id_term_certificate)||': '||begin_action_date_frmt||' - '||end_action_date_frmt name_certificate " +
     		"   FROM " + getGeneralDBScheme()+".vc_term_certificates_all "+
     		"  WHERE TRUNC(SYSDATE) BETWEEN TRUNC(begin_action_date) AND TRUNC(end_action_date) " +
     		"    AND id_term = ? ";
    	pParam.add(new bcFeautureParam("int", this.idTerminal));
    	
    	if (!isEmpty(id_cert_exclude)) {
    		mySQL = mySQL + " AND id_term_certificate <> ? ";
    		pParam.add(new bcFeautureParam("int", id_cert_exclude));
    	}
    	return getSelectBodyFromParamQuery(mySQL, pParam, id_cert, isNull);
    }

    public String getTermSAMHTML(String pFindString, String pSAMStatus, String p_beg, String p_end) {
    	bcListTerminalSAM list = new bcListTerminalSAM();
    	
    	String pWhereCause = " WHERE id_term = ? ";
    	
    	ArrayList<bcFeautureParam> pWhereValue = new ArrayList<bcFeautureParam>();
    	pWhereValue.add(new bcFeautureParam("int", this.idTerminal));
    	
    	String myDeleteLink = "../crm/clients/terminalupdate.jsp?type=sam&id_term="+this.idTerminal+"&action=remove&process=yes";
	    String myEditLink = "../crm/clients/terminalupdate.jsp?type=sam&id="+this.idTerminal;
    	
    	return list.getTerminalSAMHTML(pWhereCause, pWhereValue, pFindString, pSAMStatus, myEditLink, myDeleteLink, p_beg, p_end);
    	
    }
    
    public String getTermSessionsHTML(String pFindString, String pDataType, String pDataState, String p_beg, String p_end) {
    	bcListTermSession list = new bcListTermSession();
    	
    	String pWhereCause = " WHERE id_term = ? ";
    	
    	ArrayList<bcFeautureParam> pWhereValue = new ArrayList<bcFeautureParam>();
    	pWhereValue.add(new bcFeautureParam("int", this.idTerminal));
    	
    	return list.getTermSessionList(pWhereCause, pWhereValue, pFindString, pDataType, pDataState, p_beg, p_end);
    }

    public String getTermSessionsOldHTML(String pFindString, String p_beg, String p_end) {
        StringBuilder html = new StringBuilder();
        Connection con = null;
        PreparedStatement st = null;

        ArrayList<bcFeautureParam> pParam = initParamArray();

        String mySQL = 
        	" SELECT a.rn, a.id_term_ses, a.date_beg_frmt, a.date_end_frmt, /*a.id_term,*/ a.name_service_place, /*a.id_sam,*/ " +
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
    		"        a.desc_term_ses*/, id_service_place " + 
    		"   FROM (SELECT rn, id_term_ses, id_term, name_service_place, /*id_sam,*/  " +
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
    		"                name_last_input_telgr, name_last_output_telgr, id_service_place " +
    		"   FROM (SELECT ROWNUM rn, id_term_ses, id_term, name_service_place, /*id_sam,*/  " +
    		"  			     date_beg_frmt, date_end_frmt, desc_term_ses,  " +
    		"  			     need_card_req, action_card_req, " +
    		"                need_col_data, need_tpar_data, need_term_mon, " +
    		"                need_online_pay, need_online_storno, need_club_pay, need_adv_pay, " +
    		"                name_last_input_telgr, name_last_output_telgr, id_service_place " +
    		"  			FROM (SELECT id_term_ses, id_term, name_service_place, /*id_sam,*/ nt_sam_begin,  " +
    		"  						 TO_CHAR(date_beg,'dd.mm.rrrr hh24:mi:ss') date_beg_frmt,  " +
    		"  						 TO_CHAR(date_end,'dd.mm.rrrr hh24:mi:ss') date_end_frmt, desc_term_ses,  " +
    		"  						 need_card_req, action_card_req, need_col_data, need_tpar_data, " +
    		"                        need_term_mon, need_online_pay, need_online_storno, need_club_pay, need_adv_pay, " +
    		"                        name_last_input_telgr,  name_last_output_telgr, id_service_place " +
    		"                   FROM " + getGeneralDBScheme()+".vc_term_session_all " +
    		"                  WHERE id_term = ? ";
        pParam.add(new bcFeautureParam("int", this.idTerminal));
        
	    if (!isEmpty(pFindString)) {
			mySQL = mySQL + " AND (TO_CHAR(id_term_ses) LIKE UPPER('%'||?||'%') OR " +
				"TO_CHAR(id_term) LIKE UPPER('%'||?||'%') OR " + 
				"TO_CHAR(date_beg,'dd.mm.rrrr hh24:mi:ss') LIKE UPPER('%'||?||'%') OR " + 
				"TO_CHAR(date_end,'dd.mm.rrrr hh24:mi:ss') LIKE UPPER('%'||?||'%') OR " + 
				"UPPER(desc_term_ses) LIKE UPPER('%'||?||'%')) ";
			for (int i=0; i<5; i++) {
				pParam.add(new bcFeautureParam("string", pFindString));
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
        
        boolean hasTermSesPermission = false;
        boolean hasServicePlacePermission = false;
        //boolean hasSAMPermission = false;
        try{ 
      	  	if (isEditMenuPermited("CLIENTS_TERMSES")>=0) {
      	  		hasTermSesPermission = true;
      	  	}
      	  	if (isEditMenuPermited("CLIENTS_SERVICE_PLACE")>=0) {
      	  		hasServicePlacePermission = true;
    	  	}
      	  	//if (isEditMenuPermited("CLIENTS_SAM")>=0) {
      	  	//	hasSAMPermission = true;
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
            	html.append(getBottomFrameTableTH(term_sesXML, mtd.getColumnName(i)));
            }
            html.append("</tr></thead><tbody>");
            
            while (rset.next())
            {
            	html.append("<tr>");
          	  	for (int i=1; i <= colCount-1; i++) {
	          	  	if (hasTermSesPermission && "ID_TERM_SES".equalsIgnoreCase(mtd.getColumnName(i))) {
	      	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/clients/termsespecs.jsp?id="+rset.getString("ID_TERM_SES"), "", ""));
	      	  		} else if (hasServicePlacePermission && "NAME_SERVICE_PLACE".equalsIgnoreCase(mtd.getColumnName(i))) {
	      	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/clients/service_placespecs.jsp?id="+rset.getString("ID_SERVICE_PLACE"), "", ""));
	      	  		//} else if (hasSAMPermission && "ID_SAM".equalsIgnoreCase(mtd.getColumnName(i))) {
	      	  		//	html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/clients/samspecs.jsp?id="+rset.getString("ID_SAM"), "", ""));
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
    } // getTermSessionsHTML

    public String getTermLoyalityLinesHTML(String pFindString, String pIdLoyalityScheme, String pIdCardStatus, String pIdCategory, String pCdKindLoyality, String p_beg, String p_end) {
    	ArrayList<bcFeautureParam> pParam = initParamArray();

    	String mySQL = 
			" SELECT id_line, date_beg_frmt, date_end_frmt, " +
			"        name_card_status, name_category, cd_kind_loyality, " +
			"        opr_sum_frmt, bon_percent_value_percent, bon_fixed_value_frmt, " +
			"        max_bon_st_frmt, bonus_transfer_term, disc_percent_value_percent, disc_fixed_value_frmt, " +
			"        max_disc_st_frmt, bonus_calc_term, nullperiod_flag_tsl, active_tsl, id_loyality_scheme " +
			"   FROM (SELECT ROWNUM rn, id_line, date_beg_frmt, date_end_frmt, " +
    		"                id_loyality_scheme, name_card_status, name_category, cd_kind_loyality, " +
    		"                opr_sum_frmt, bon_percent_value_percent, bon_fixed_value_frmt, " +
    		"                max_bon_st_frmt, bonus_transfer_term, disc_percent_value_percent, disc_fixed_value_frmt, " +
    		"                max_disc_st_frmt, bonus_calc_term, " +
    		"        		 DECODE(nullperiod_flag, 0, '<b><font color=\"red\">'||nullperiod_flag_tsl||'</font><b>', nullperiod_flag_tsl) nullperiod_flag_tsl, " +
        	"        		 DECODE(active, 0, '<b><font color=\"red\">'||active_tsl||'</font><b>', active_tsl) active_tsl, opr_sum " +
        	"                date_beg " +
    		"           FROM (SELECT * " +
    		"                   FROM " + getGeneralDBScheme() + ".vc_term_loyality_lines_h_all " +
    		"                  WHERE id_term = ? ";
    	pParam.add(new bcFeautureParam("int", this.idTerminal));
    	
        if (!isEmpty(pFindString)) {
    		mySQL = mySQL + 
   				" AND (TO_CHAR(id_line) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(date_beg_frmt) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(date_end_frmt) LIKE UPPER('%'||?||'%'))";
    		for (int i=0; i<3; i++) {
    		    pParam.add(new bcFeautureParam("string", pFindString));
    		}
    	}
       	if (!isEmpty(pIdCategory)) {
       		mySQL = mySQL + " AND id_category = ? ";
       		pParam.add(new bcFeautureParam("int", pIdCategory));
       	}
       	if (!isEmpty(pIdCardStatus)) {
       		mySQL = mySQL + " AND id_card_status = ? ";
       		pParam.add(new bcFeautureParam("int", pIdCardStatus));
       	}
       	if (!isEmpty(pCdKindLoyality)) {
       		mySQL = mySQL + " AND cd_kind_loyality = ? ";
       		pParam.add(new bcFeautureParam("string", pCdKindLoyality));
       	}

   		if (isEmpty(pIdLoyalityScheme)) {
       		mySQL = mySQL + " AND 1 = 0 ";
   		} else {
   			mySQL = mySQL + " AND id_loyality_history = ? ";
   			pParam.add(new bcFeautureParam("int", pIdLoyalityScheme));
   		}
   		pParam.add(new bcFeautureParam("int", p_end));
   		pParam.add(new bcFeautureParam("int", p_beg));
       	mySQL = mySQL +
			"                  ORDER BY date_beg, id_loyality_scheme, cd_kind_loyality, " +
			"                           name_card_status, name_category, opr_sum_frmt )" + 
    		"           WHERE ROWNUM < ? " + 
    		" ) WHERE rn >= ?";
    	
		StringBuilder html = new StringBuilder();
		Connection con = null;
		PreparedStatement st = null;
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
            for (int i=1; i <= colCount-1; i++) {
            	html.append(getBottomFrameTableTH(loylineXML, mtd.getColumnName(i)));
            }
            html.append("</tr></thead><tbody>");
            
            while (rset.next())
            {
            	html.append("<tr>");
          	  	for (int i=1; i <= colCount-1; i++) {
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
    } // getTermServCardHTML

    public String getTermMessagesHTML(String pFindString, String pIsArchive, String p_beg, String p_end) {
        StringBuilder html = new StringBuilder();
        Connection con = null;
        PreparedStatement st = null;
        boolean hasEditPermission = false;
        boolean hasMessagePermission = false;

        ArrayList<bcFeautureParam> pParam = initParamArray();

        String mySQL = 
        	" SELECT rn, text_message, begin_action_date_frmt begin_send_date,  " +
            "        end_action_date_frmt end_send_date, sendings_quantity, event_date_frmt last_send_date, " +
            "        to_send_tsl, id_message, id_term_message_receiver " +
            "   FROM (SELECT ROWNUM rn, id_message, text_message, begin_action_date_frmt, " +
            "                end_action_date_frmt, sendings_quantity, event_date_frmt, " +
            "                to_send_tsl, id_term_message_receiver " +
            "           FROM (SELECT r.id_term_message id_message, r.text_term_message text_message, r.begin_action_date_frmt," +
            "                        r.end_action_date_frmt, r.sendings_quantity, r.last_send_date_frmt event_date_frmt, " +
			"               	     DECODE(r.to_send, " +
			"                               'Y', '<b><font color=\"green\">'||r.to_send_tsl||'</font></b>', " +
			"                               'N', '<b><font color=\"red\">'||r.to_send_tsl||'</font></b>', " +
			"                               r.to_send_tsl) " +
			"                        to_send_tsl, r.id_term_message_receiver " +
            "                   FROM " + getGeneralDBScheme() + ".vc_term_message_receiver_all r " +
            "                  WHERE r.id_term = ? ";
        pParam.add(new bcFeautureParam("int", this.idTerminal));
        
        if (!isEmpty(pFindString)) {
    		mySQL = mySQL + 
   				" AND (TO_CHAR(id_message) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(name_service_place) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(text_message) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(begin_action_date_frmt) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(end_action_date_frmt) LIKE UPPER('%'||?||'%'))";
    		for (int i=0; i<5; i++) {
    		    pParam.add(new bcFeautureParam("string", pFindString));
    		}
    	}
        if (!isEmpty(pIsArchive)) {
        	mySQL = mySQL + " AND to_send = ? ";
        	pParam.add(new bcFeautureParam("string", pIsArchive));
        }
        pParam.add(new bcFeautureParam("int", p_end));
        pParam.add(new bcFeautureParam("int", p_beg));
        mySQL = mySQL +
            "                  ORDER BY begin_action_date desc, text_message" +
            "        ) WHERE ROWNUM < ? " + 
            " ) WHERE rn >= ?";
        try{
        	if (isEditPermited("CLIENTS_TERMINALS_MESSAGES")>0) {
        		hasEditPermission = true;
        	}
        	if (isEditMenuPermited("DISPATCH_MESSAGES_TERM")>=0) {
        		hasMessagePermission = true;
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
            	html.append(getBottomFrameTableTH(messageXML, mtd.getColumnName(i)));
            }
            if (hasEditPermission) {
               html.append("<th>&nbsp;</th>\n");
               //html.append("<th>&nbsp;</th>\n");
            }
            html.append("</tr></thead><tbody>");
            
            while (rset.next())
            {
            	html.append("<tr>");
          	  	for (int i=1; i <= colCount-2; i++) {
          	  		if (hasMessagePermission && "TEXT_MESSAGE".equalsIgnoreCase(mtd.getColumnName(i))) {
          	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/dispatch/messages/term_messagesspecs.jsp?id="+rset.getString("ID_MESSAGE"), "", ""));
          	  		} else {
          	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", "", ""));
          	  		}
          	  	}
                if (hasEditPermission) {
            	   String myHyperLink = "../crm/clients/terminalupdate.jsp?type=message&id_term="+this.idTerminal+"&id_term_message_receiver="+rset.getString("ID_TERM_MESSAGE_RECEIVER");
            	   //String myDeleteLink = "../crm/clients/terminalupdate.jsp?type=message&id_term="+this.idTerminal+"&id_term_message_receiver="+rset.getString("ID_TERM_MESSAGE_RECEIVER")+"&action=remove&process=yes";
            	   //html.append(getDeleteButtonHTML(myDeleteLink, messageXML.getfieldTransl("h_message_delete", false), rset.getString("ID_MESSAGE")));
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
    } // getTermMessagesHTML

    /*public String getTermPositionsHTML(String pFindString, String p_beg, String p_end) {
        StringBuilder html = new StringBuilder();
        Connection con = null;
        PreparedStatement st = null;
        boolean hasEditPermission = false;

        ArrayList<bcFeautureParam> pParam = initParamArray();

        String mySQL = 
        	" SELECT sname_jur_prs jur_prs_name, name_service_place, adr_full, cash_desk_number, " +
            "        cd_service_place_short, date_location_frmt, date_extract_frmt, is_base_service_place_tsl, id_service_place, id_jur_prs " +
            "   FROM (SELECT ROWNUM rn, sname_jur_prs, name_service_place, adr_full, cash_desk_number, " +
            "                cd_service_place_short, date_location_frmt, date_extract_frmt, is_base_service_place_tsl, id_service_place, id_jur_prs " +
            "           FROM (SELECT sname_jur_prs, name_service_place, adr_full, cash_desk_number, " +
            "                        cd_service_place cd_service_place_short, date_location_frmt, " +
            "                        date_extract_frmt, " +
            "                        DECODE(is_base_service_place," +
            "                               'N', '<font color=\"red\"><b>'||is_base_service_place_tsl||'</b></font>'," +
            "                               is_base_service_place_tsl" +
            "                        ) is_base_service_place_tsl, " +
            "                        id_service_place, id_jur_prs " +
            "                   FROM " + getGeneralDBScheme() + ".vc_term_service_places_all" +
            "                  WHERE id_term = ? ";
        pParam.add(new bcFeautureParam("int", this.idTerminal));
        
        if (!isEmpty(pFindString)) {
    		mySQL = mySQL + 
   				" AND (UPPER(sname_jur_prs) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(name_service_place) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(adr_full) LIKE UPPER('%'||?||'%'))";
    		for (int i=0; i<3; i++) {
    		    pParam.add(new bcFeautureParam("string", pFindString));
    		}
    	}
        pParam.add(new bcFeautureParam("int", p_end));
        pParam.add(new bcFeautureParam("int", p_beg));
        mySQL = mySQL +
            "                  ORDER BY sname_jur_prs desc, name_service_place" +
            "        ) WHERE ROWNUM < ? " + 
            " ) WHERE rn >= ?";
        
        boolean hasJurPrsPermission = false;
        boolean hasServicePlacePermission = false;
        
        try{
        	if (isEditPermited("CLIENTS_TERMINALS_POSITION")>0) {
        		hasEditPermission = true;
        	}
        	if (isEditMenuPermited("CLIENTS_YURPERSONS")>=0) {
        		hasJurPrsPermission = true;
            }
        	if (isEditMenuPermited("CLIENTS_SERVICE_PLACE")>=0) {
        		hasServicePlacePermission = true;
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
            	html.append(getBottomFrameTableTH(terminalXML, mtd.getColumnName(i)));
            }
            if (hasEditPermission) {
               html.append("<th>&nbsp;</th><th>&nbsp;</th>\n");
            }
            html.append("</tr></thead><tbody>");
            
            while (rset.next())
            {
            	html.append("<tr>");
          	  	for (int i=1; i <= colCount-2; i++) {
          	  		if (hasJurPrsPermission && "JUR_PRS_NAME".equalsIgnoreCase(mtd.getColumnName(i))) {
          	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/clients/yurpersonspecs.jsp?id="+rset.getString("ID_JUR_PRS"), "", ""));
         	  		} else if (hasServicePlacePermission && "NAME_SERVICE_PLACE".equalsIgnoreCase(mtd.getColumnName(i))) {
          	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/clients/service_placespecs.jsp?id="+rset.getString("ID_SERVICE_PLACE"), "", ""));
         	  		} else {
          	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", "", ""));
         	  		}
          	  	}
                if (hasEditPermission) {
            	   String myHyperLink = "../crm/clients/terminalupdate.jsp?type=position&id_term="+this.idTerminal+"&id_service_place="+rset.getString("ID_SERVICE_PLACE");
            	   String myDeleteLink = "../crm/clients/terminalupdate.jsp?type=position&id_term="+this.idTerminal+"&id_service_place="+rset.getString("ID_SERVICE_PLACE")+"&action=remove&process=yes";
                   html.append(getDeleteButtonHTML(myDeleteLink, terminalXML.getfieldTransl("h_position_delete", false), rset.getString("NAME_SERVICE_PLACE")));
            	   html.append(getEditButtonHTML(myHyperLink));
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
    } // getTermPositionsHTML*/

    public String getComissionListHTML(String pFindString, String cdClubRelType, String p_beg, String p_end) {
    	ArrayList<bcFeautureParam> pParam = initParamArray();

    	String mySQL = 
        		"SELECT rn, name_comission_type, full_name_club_rel, " +
        		"       sname_jur_prs_payer, sname_jur_prs_receiver, " + 
	  			"       begin_action_date_frmt, end_action_date_frmt, " +
	  			"       fixed_value_frmt, percent_value_frmt, " +
	  			"       id_comission, id_doc, id_comission_type, exist_flag, " +
	  			"       payer_has_error, payer_has_error_type_tsl, " +
	  			"       receiver_has_error, receiver_has_error_type_tsl, " +
	  			"       is_special_comission, id_club_rel, " +
	  			"       id_jur_prs_payer, cd_jur_prs_prim_type_payer, " +
	  			"       id_jur_prs_receiver, cd_jur_prs_prim_type_receiver " +
	  			"  FROM (SELECT ROWNUM rn, a.name_comission_type, a.full_name_club_rel, " +
	  			"               a.sname_jur_prs_payer, a.sname_jur_prs_receiver, " + 
	  			"               a.name_payment_system, a.begin_action_date_frmt, a.end_action_date_frmt, " +
	  			"               a.fixed_value_frmt, a.percent_value_frmt, a.description,  " +
	  			"               a.id_comission, a.id_doc, a.id_comission_type, a.exist_flag, " +
	  			"               a.payer_has_error, a.payer_has_error_type_tsl, " +
	  			"               a.receiver_has_error, a.receiver_has_error_type_tsl," +
	  			"               a.is_special_comission, a.name_club_rel_type, a.id_club_rel, " +
	  			"               a.id_jur_prs_payer, a.cd_jur_prs_prim_type_payer, " +
	  			"               a.id_jur_prs_receiver, a.cd_jur_prs_prim_type_receiver " +
	  			"  		   FROM (SELECT * " +
	  			"                  FROM " + getGeneralDBScheme() + ".vc_term_comission_club_all " +
	  			" 	  	          WHERE id_term = ? " + 
	  			"                   AND exist_flag = ? ";
    	pParam.add(new bcFeautureParam("int", this.idTerminal));
    	pParam.add(new bcFeautureParam("string", "Y"));
    	
        if (!isEmpty(pFindString)) {
    		mySQL = mySQL + 
   				" AND (UPPER(name_comission_type) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(full_name_club_rel) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(sname_jur_prs_payer) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(sname_jur_prs_receiver) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(begin_action_date_frmt) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(end_action_date_frmt) LIKE UPPER('%'||?||'%'))";
    		for (int i=0; i<6; i++) {
    		    pParam.add(new bcFeautureParam("string", pFindString));
    		}
    	}
        if (!isEmpty(cdClubRelType)) {
        	mySQL = mySQL + " AND cd_club_rel_type = ? ";
        	pParam.add(new bcFeautureParam("string", cdClubRelType));
        }
        pParam.add(new bcFeautureParam("int", p_end));
        pParam.add(new bcFeautureParam("int", p_beg));
        mySQL = mySQL +
	  			"                 ORDER BY sname_jur_prs_payer, sname_jur_prs_receiver, name_comission_type_full" +
	  			"               ) a " +
	  			"         WHERE ROWNUM < ? " + 
	  			" ) WHERE rn >= ?";
        StringBuilder html = new StringBuilder();
        Connection con = null;
        PreparedStatement st = null; 
        String myFont = "";
        String myFontEnd = "";

        boolean hasRelationshipPermission = false;
        boolean hasComisTypePermission = false;
        boolean hasPartnerPermission = false;
           
        String payerHyperLink = "";
        String receiverHyperLink = "";
        
        try{
      	  	if (isEditMenuPermited("CLUB_RELATIONSHIP")>=0) {
        		hasRelationshipPermission = true;
        	}
      	  	if (isEditMenuPermited("CLUB_COMISTYPE")>=0) {
      	  		hasComisTypePermission = true;
      	  	}
      	  	if (isEditMenuPermited("CLIENTS_YURPERSONS")>=0) {
      	  		hasPartnerPermission = true;
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
            html.append("<th>&nbsp;</th>\n");
            for (int i=2; i <= colCount-14; i++) {
            	html.append(getBottomFrameTableTH(comissionXML, mtd.getColumnName(i)));
            }
            html.append("</tr></thead><tbody>\n");
         
         while (rset.next())
         {
        	 if ("Y".equalsIgnoreCase(rset.getString("IS_SPECIAL_COMISSION"))) {
        		 myFont = "<b><font color=\"green\">";
        		 myFontEnd = "</font></b>";
        	 } else {
        		 myFont = "";
        		 myFontEnd = "";
        	 }
        	 
        	 if (!isEmpty(rset.getString("CD_JUR_PRS_PRIM_TYPE_PAYER")) && hasPartnerPermission) {
          		payerHyperLink = getHyperLinkFirst()+"../crm/clients/yurpersonspecs.jsp?id="+rset.getString("ID_JUR_PRS_PAYER") + getHyperLinkMiddle();
          	 } else {
          		payerHyperLink = "";
          	 }
          	
          	 if (!isEmpty(rset.getString("CD_JUR_PRS_PRIM_TYPE_RECEIVER")) && hasPartnerPermission) {
          		receiverHyperLink = getHyperLinkFirst()+"../crm/clients/yurpersonspecs.jsp?id="+rset.getString("ID_JUR_PRS_RECEIVER") + getHyperLinkMiddle();
          	 } else {
          		receiverHyperLink = "";
          	 }
        	 
        	 html.append("<tr><td>" + myFont + getValue2(rset.getString("RN")) + myFontEnd + "</td>\n");
        	 if (hasComisTypePermission) {
        		 html.append("<td>" + myFont + getHyperLinkFirst()+"../crm/club/comistypespecs.jsp?id="+rset.getString("ID_COMISSION_TYPE") + getHyperLinkMiddle() + getValue2(rset.getString("NAME_COMISSION_TYPE")) + getHyperLinkEnd() + myFontEnd);
    		 } else {
    			 html.append("<td>" + myFont + getValue2(rset.getString("NAME_COMISSION_TYPE")) +  myFontEnd);
    		 }
        	 if (hasRelationshipPermission) {
         		html.append("<td>" + myFont + getHyperLinkFirst()+"../crm/club/relationshipspecs.jsp?id="+rset.getString("ID_CLUB_REL") + getHyperLinkMiddle() + getValue2(rset.getString("FULL_NAME_CLUB_REL")) + getHyperLinkEnd() + myFontEnd);
         	 } else {
         		html.append("<td>" + myFont + getValue2(rset.getString("FULL_NAME_CLUB_REL")) +  myFontEnd);
         	 }
        	 html.append("<td>" + myFont + payerHyperLink + getValue2(rset.getString("SNAME_JUR_PRS_PAYER")) + getHyperLinkEnd() + myFontEnd);
        	 if (!"N".equalsIgnoreCase(rset.getString("PAYER_HAS_ERROR"))) {
        		 html.append("&nbsp;<img vspace=\"0\" hspace=\"0\" src=\"p_context_path, /images/oper/rows/warning.png\" align=\"top\" style=\"border: 0px;\" title=\""+rset.getString("PAYER_HAS_ERROR_TYPE_TSL")+"\">");
        	 }
        	 html.append("<td>" + myFont + receiverHyperLink + getValue2(rset.getString("SNAME_JUR_PRS_RECEIVER")) + getHyperLinkEnd() + myFontEnd);
        	 if (!"N".equalsIgnoreCase(rset.getString("RECEIVER_HAS_ERROR"))) {
        		 html.append("&nbsp;<img vspace=\"0\" hspace=\"0\" src=\"p_context_path, /images/oper/rows/warning.png\" align=\"top\" style=\"border: 0px;\" title=\""+rset.getString("RECEIVER_HAS_ERROR_TYPE_TSL")+"\">");
        	 }
             html.append("</td>\n<td>" + myFont + getValue2(rset.getString("BEGIN_ACTION_DATE_FRMT")) + myFontEnd +
            		   "</td>\n<td>" + myFont + getValue2(rset.getString("END_ACTION_DATE_FRMT")) + myFontEnd +
            		   "</td>\n<td>" + myFont + getValue2(rset.getString("FIXED_VALUE_FRMT")) + myFontEnd +
            		   "</td>\n<td>" + myFont + getValue2(rset.getString("PERCENT_VALUE_FRMT")) + myFontEnd +
            		   "</td>\n");
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
    
    private String getRelationshipFilter() {
    	String lTermOwner = this.getValue("ID_TERM_OWNER");
	    String lFinAcquirer = this.getValue("ID_FINANCE_ACQUIRER");
	    String lTechAcquirer = this.getValue("ID_TECHNICAL_ACQUIRER");
	    String lDeviceType = this.getValue("ID_DEVICE_TYPE");
	    
	    pRelationshipFilterParam.clear();
	    String myFiltr = "";
	    myFiltr = myFiltr + 
	    	" WHERE (cd_club_rel_type = 'OPERATOR-DEALER' AND " +
	    	"	(id_party2) IN " +
	    	"	  (SELECT id_jur_prs " +
	    	"        FROM " + getGeneralDBScheme() + ".vc_term_service_places_all " +
	    	"       WHERE id_term = ? ) " +
	    	"   ) ";
	    pRelationshipFilterParam.add(new bcFeautureParam("int", this.idTerminal));
	    
	    if (!isEmpty(lTermOwner)) {
	    	myFiltr = myFiltr + " OR (cd_club_rel_type = 'OPERATOR-PARTNER' AND id_party2 = ?) ";
		    pRelationshipFilterParam.add(new bcFeautureParam("int", lTermOwner));
	    }
	    if (!isEmpty(lFinAcquirer)) {
	    	myFiltr = myFiltr + " OR (cd_club_rel_type = 'OPERATOR-FINANCE_ACQUIRER' AND id_party2 = ?) ";
	    	myFiltr = myFiltr + " OR (cd_club_rel_type = 'DEALER-FINANCE_ACQUIRER' AND " +
	    		"			   (id_party1, id_party2) IN " +
	    		"				  (SELECT id_jur_prs, ? id_party2 " +
	    		"                   FROM " + getGeneralDBScheme() + ".vc_term_service_places_all " +
	    		"                   WHERE id_term = ?) " +
	    		"              ) ";
		    pRelationshipFilterParam.add(new bcFeautureParam("int", lFinAcquirer));
		    pRelationshipFilterParam.add(new bcFeautureParam("int", lFinAcquirer));
		    pRelationshipFilterParam.add(new bcFeautureParam("int", this.idTerminal));
	    }
	    if (!isEmpty(lTechAcquirer)) {
	    	myFiltr = myFiltr + " OR (cd_club_rel_type = 'OPERATOR-TECHNICAL_ACQUIRER' AND id_party2 = ?) ";
	    	myFiltr = myFiltr + " OR (cd_club_rel_type = 'DEALER-TECHNICAL_ACQUIRER' AND " +
				"			   (id_party1, id_party2) IN " +
				"				  (SELECT id_jur_prs, ? id_party2 " +
				"                    FROM " + getGeneralDBScheme() + ".vc_term_service_places_all " +
				"                   WHERE id_term = ?) " +
			    "              ) ";
		    pRelationshipFilterParam.add(new bcFeautureParam("int", lTechAcquirer));
		    pRelationshipFilterParam.add(new bcFeautureParam("int", lTechAcquirer));
		    pRelationshipFilterParam.add(new bcFeautureParam("int", this.idTerminal));
	    }
	    if (!isEmpty(lDeviceType)) {
		    myFiltr = myFiltr + 
	    		" OR (cd_club_rel_type = 'OPERATOR-TERMINAL_MANUFACTURER' AND " +
	    		"	(id_party2) IN " +
	    		"	  (SELECT id_jur_prs_manufacturer " +
	    		"        FROM " + getGeneralDBScheme() + ".vc_term_device_type_all " +
	    		"       WHERE id_device_type = ?) " +
	    		"   ) ";
		    pRelationshipFilterParam.add(new bcFeautureParam("int", lDeviceType));
	    }
	    return myFiltr;
    }

    public String getRelationShipsHTML(String pFindString, String p_beg, String p_end) {
    	StringBuilder html = new StringBuilder();
    	Connection con = null;
    	PreparedStatement st = null;
	    boolean hasRelationshipPermission = false;
	    boolean hasEditPermission = false;
	    
	    String mySQL = 
	    	" SELECT rn, id_club_rel, date_club_rel, name_club_rel_type,  " +
      	  	"        sname_party1_full, sname_party2_full " +
      	  	"   FROM (SELECT ROWNUM rn, id_club_rel, name_club_rel_type, date_club_rel_frmt date_club_rel, " +
      	  	"                desc_club_rel, sname_party1 sname_party1_full, " +
      	  	"                sname_party2 sname_party2_full " +
           	"           FROM (SELECT * " +
           	"                   FROM (SELECT * " +
           	"                          FROM " + getGeneralDBScheme() + ".vc_club_rel_club_all "+
           	getRelationshipFilter() + 
          	"              ) WHERE 1=1";
        if (!isEmpty(pFindString)) {
    		mySQL = mySQL + 
   				" AND (TO_CHAR(id_club_rel) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(date_club_rel_frmt) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(name_club_rel_type) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(sname_party1) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(sname_party2) LIKE UPPER('%'||?||'%'))";
    		for (int i=0; i<5; i++) {
    		    pRelationshipFilterParam.add(new bcFeautureParam("string", pFindString));
    		}
    	}
        pRelationshipFilterParam.add(new bcFeautureParam("int", p_end));
        pRelationshipFilterParam.add(new bcFeautureParam("int", p_beg));
        mySQL = mySQL +
          	"    ) WHERE ROWNUM < ? " + 
          	" ) WHERE rn >= ?";
	    try{
        	if (isEditPermited("CLIENTS_TERMINALS_RELATIONSHIPS")>0) {
        		hasEditPermission = true;
        	}
	    	if (isEditMenuPermited("CLUB_RELATIONSHIP")>=0) {
	    		hasRelationshipPermission = true;
	    	}
	    	  
	    	LOGGER.debug(prepareSQLToLog(mySQL, pRelationshipFilterParam));
	    	con = Connector.getConnection(getSessionId());
	    	st = con.prepareStatement(mySQL);
	    	st = prepareParam(st, pRelationshipFilterParam);
	    	ResultSet rset = st.executeQuery();
            ResultSetMetaData mtd = rset.getMetaData();
            
            int colCount = mtd.getColumnCount();

            html.append(getBottomFrameTable());
            html.append("<tr>");
	        for (int i=1; i <= colCount; i++) {
	        	html.append(getBottomFrameTableTH(relationshipXML, mtd.getColumnName(i)));
            }
            if (hasEditPermission) {
                html.append("<th>&nbsp;</th><th>&nbsp;</th>\n");
            }
	        html.append("</tr></thead><tbody>");
	        
	        while (rset.next()) {
	        	html.append("<tr>");
          	  	for (int i=1; i <= colCount; i++) {
          	  		if (hasRelationshipPermission && "ID_CLUB_REL".equalsIgnoreCase(mtd.getColumnName(i))) {
          	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/club/relationshipspecs.jsp?id="+rset.getString(i), "", ""));
         	  		} else {
          	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", "", ""));
          	  		}
          	  	}
                if (hasEditPermission) {
	            	String myHyperLink = "../crm/clients/terminalupdate.jsp?type=relationship&id_term="+this.idTerminal+"&id_club_rel="+rset.getString("ID_CLUB_REL");
	            	String myDeleteLink = "../crm/clients/terminalupdate.jsp?type=relationship&id_term="+this.idTerminal+"&id_club_rel="+rset.getString("ID_CLUB_REL")+"&action=remove&process=yes";
	                html.append(getDeleteButtonHTML(myDeleteLink, relationshipXML.getfieldTransl("h_delete_relationship", false), rset.getString("ID_CLUB_REL")));
	            	html.append(getEditButtonHTML(myHyperLink));
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
	  } // getRelationShipsHTML
    
    public String getRelationShipsNeededCount() {
    	String mySQL = " SELECT COUNT(*) FROM " + getGeneralDBScheme()+".vc_club_rel_need_club_all " + getRelationshipFilter();
	    return getOneValueByParamId(mySQL, pRelationshipFilterParam);
    } // getCardStateName
    
    public String getRelationShipsNeededHTML(String pFindString, String p_beg, String p_end) {
    	StringBuilder html = new StringBuilder();
    	Connection con = null;
    	PreparedStatement st = null;
	    boolean hasAccessPermission = false;
	    String mySQL =  
		    	" SELECT rn, name_club_rel_type, sname_party1 sname_party1_full, sname_party2 sname_party2_full, id_club_rel " +
	      	  	"   FROM (SELECT ROWNUM rn, sname_party1, sname_party2, name_club_rel_type, id_club_rel " +
	          	"   		  FROM (SELECT * " +
	          	"                   FROM (SELECT * " +
	          	"                           FROM " + getGeneralDBScheme()+".vc_club_rel_need_club_all " +
	          	getRelationshipFilter () + 
	          	"         ) WHERE 1=1";
        if (!isEmpty(pFindString)) {
    		mySQL = mySQL + 
   				" AND (TO_CHAR(sname_party1) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(sname_party2) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(name_club_rel_type) LIKE UPPER('%'||?||'%'))";
    		for (int i=0; i<3; i++) {
    		    pRelationshipFilterParam.add(new bcFeautureParam("string", pFindString));
    		}
    	}
        pRelationshipFilterParam.add(new bcFeautureParam("int", p_end));
        pRelationshipFilterParam.add(new bcFeautureParam("int", p_beg));
        mySQL = mySQL +
	          	") WHERE ROWNUM < ? " + 
	          	" ) WHERE rn >= ?";
	    try{
	    	if (isEditMenuPermited("CLUB_RELATIONSHIP")>=0) {
	    		hasAccessPermission = true;
	    	}
	    	  
	    	LOGGER.debug(prepareSQLToLog(mySQL, pRelationshipFilterParam));
	    	con = Connector.getConnection(getSessionId());
	    	st = con.prepareStatement(mySQL);
	    	st = prepareParam(st, pRelationshipFilterParam);
	    	ResultSet rset = st.executeQuery();
            ResultSetMetaData mtd = rset.getMetaData();
            
            int colCount = mtd.getColumnCount();

            html.append(getBottomFrameTable());
            html.append("<tr>");
	        for (int i=1; i <= colCount-1; i++) {
	        	html.append(getBottomFrameTableTH(relationshipXML, mtd.getColumnName(i)));
            }
            html.append("</tr></thead><tbody>\n");
	        
	        while (rset.next()) {
	        	html.append("<tr>\n");
		        for (int i=1; i <= colCount-1; i++) {
	        		if (hasAccessPermission) {
	        			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/clients/terminalupdate.jsp?type=relationship&action=addneeded&process=no&id_term="+this.idTerminal+"&id_club_rel="+rset.getString("id_club_rel"), "", ""));
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
	  } // getRelationShipsHTML
	
    public String getLogisticHTML(String pFindString, String p_beg, String p_end) {
        StringBuilder html = new StringBuilder();
        Connection con = null;
        PreparedStatement st = null;

        ArrayList<bcFeautureParam> pParam = initParamArray();

        String mySQL = 
        	"SELECT rn, action_date_frmt, operation_desc, terminals_count, " +
        	"		sname_jur_prs_receiver, sname_jur_prs_sender, id_lg_record " +
        	"  FROM (SELECT ROWNUM rn, id_lg_record, operation_desc, terminals_count, " +
        	"               sname_jur_prs_receiver, sname_jur_prs_sender, action_date_frmt " +
        	"		   FROM (SELECT * " +
            "                   FROM " + getGeneralDBScheme() + ".vc_lg_terminal_priv_all "+
            "                  WHERE id_lg_record IN " +
            "                       (SELECT id_lg_record " +
            "                          FROM " + getGeneralDBScheme() + ".vc_lg_term_all " +
            "                         WHERE id_term = ? ";
        pParam.add(new bcFeautureParam("int", this.idTerminal));
        
        if (!isEmpty(pFindString)) {
    		mySQL = mySQL + 
   				" AND (TO_CHAR(id_lg_record) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(action_date_frmt) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(operation_desc) LIKE UPPER('%'||?||'%') OR " +
   				"      TO_CHAR(sname_jur_prs_receiver) LIKE UPPER('%'||?||'%') OR " +
   				"      TO_CHAR(sname_jur_prs_sender) LIKE UPPER('%'||?||'%'))";
    		for (int i=0; i<5; i++) {
    		    pParam.add(new bcFeautureParam("string", pFindString));
    		}
    	}
        pParam.add(new bcFeautureParam("int", p_end));
        pParam.add(new bcFeautureParam("int", p_beg));
        mySQL = mySQL +
            "                  ) ORDER BY action_date) " +
            "          WHERE ROWNUM < ? " + 
            " ) WHERE rn >= ?";
        
        boolean hasLogisticPermission = false;
        
        try{
        	if (isEditMenuPermited("LOGISTIC_PARTNERS_TERMINALS")>=0) {
        		hasLogisticPermission = true;
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
                html.append(getBottomFrameTableTH(logisticXML, mtd.getColumnName(i)));
            }
            html.append("</tr></thead><tbody>");
            while (rset.next())
            {
            	html.append("<tr>");
          	  	for (int i=1; i <= colCount-1; i++) {
          	  		if (hasLogisticPermission && 
          	  				("ACTION_DATE_FRMT".equalsIgnoreCase(mtd.getColumnName(i)) ||
          	  					"OPERATION_DESC".equalsIgnoreCase(mtd.getColumnName(i)))) {
          	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/logistic/partners/terminalspecs.jsp?id="+rset.getString(i), "", ""));
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
    } //getAssignTermHistoryHTML
	
    public String getMonitoringHTML(String pFindString, String p_beg, String p_end) {
        StringBuilder html = new StringBuilder();
        Connection con = null;
        PreparedStatement st = null;

        ArrayList<bcFeautureParam> pParam = initParamArray();

        String mySQL = 
        	"SELECT rn, id_term_ses, sys_date, sys_time, m_ni, m_0601, m_0602, m_0603, m_060d, " +
        	"		m_0e3a, m_0e6c, m_0e6e, m_0ea1, m_02ad, m_004c, m_004f, m_0057, m_0050 " +
        	"  FROM (SELECT ROWNUM rn, id_term_ses, sys_date, sys_time, " +
        	"               DECODE(m_ni,0,TO_CHAR(m_ni),'<span style=\"color:green;font-weight:bold;\">'||TO_CHAR(m_ni)||'</span>') m_ni, " +
        	"               DECODE(m_0601,0,TO_CHAR(m_0601),'<span style=\"color:green;font-weight:bold;\">'||TO_CHAR(m_0601)||'</span>') m_0601, " +
        	"               DECODE(m_0602,0,TO_CHAR(m_0602),'<span style=\"color:green;font-weight:bold;\">'||TO_CHAR(m_0602)||'</span>') m_0602, " +
        	"               DECODE(m_0603,0,TO_CHAR(m_0603),'<span style=\"color:green;font-weight:bold;\">'||TO_CHAR(m_0603)||'</span>') m_0603, " +
        	"               DECODE(m_060d,0,TO_CHAR(m_060d),'<span style=\"color:green;font-weight:bold;\">'||TO_CHAR(m_060d)||'</span>') m_060d, " +
        	"               DECODE(m_0e3a,0,TO_CHAR(m_0e3a),'<span style=\"color:green;font-weight:bold;\">'||TO_CHAR(m_0e3a)||'</span>') m_0e3a, " +
        	"               DECODE(m_0e6c,0,TO_CHAR(m_0e6c),'<span style=\"color:green;font-weight:bold;\">'||TO_CHAR(m_0e6c)||'</span>') m_0e6c, " +
        	"               DECODE(m_0e6e,0,TO_CHAR(m_0e6e),'<span style=\"color:green;font-weight:bold;\">'||TO_CHAR(m_0e6e)||'</span>') m_0e6e, " +
        	"               DECODE(m_0ea1,0,TO_CHAR(m_0ea1),'<span style=\"color:green;font-weight:bold;\">'||TO_CHAR(m_0ea1)||'</span>') m_0ea1, " +
        	"               DECODE(m_02ad,0,TO_CHAR(m_02ad),'<span style=\"color:green;font-weight:bold;\">'||TO_CHAR(m_02ad)||'</span>') m_02ad, " +
        	"               DECODE(m_004c,0,TO_CHAR(m_004c),'<span style=\"color:green;font-weight:bold;\">'||TO_CHAR(m_004c)||'</span>') m_004c, " +
        	"               DECODE(m_004f,0,TO_CHAR(m_004f),'<span style=\"color:green;font-weight:bold;\">'||TO_CHAR(m_004f)||'</span>') m_004f, " +
        	"               DECODE(m_0057,0,TO_CHAR(m_0057),'<span style=\"color:green;font-weight:bold;\">'||TO_CHAR(m_0057)||'</span>') m_0057, " +
        	"               DECODE(m_0050,0,TO_CHAR(m_0050),'<span style=\"color:green;font-weight:bold;\">'||TO_CHAR(m_0050)||'</span>') m_0050 " +
        	"		   FROM (SELECT * " +
            "                   FROM " + getGeneralDBScheme() + ".v_term_mon_rep "+
            "                  WHERE id_term = ? ";
        pParam.add(new bcFeautureParam("int", this.idTerminal));
        
        if (!isEmpty(pFindString)) {
    		mySQL = mySQL + 
   				" AND (UPPER(sys_date) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(sys_time) LIKE UPPER('%'||?||'%'))";
    		for (int i=0; i<2; i++) {
    		    pParam.add(new bcFeautureParam("string", pFindString));
    		}
    	}
        pParam.add(new bcFeautureParam("int", p_end));
        pParam.add(new bcFeautureParam("int", p_beg));
        mySQL = mySQL +
            "                  ORDER BY sys_date DESC, sys_time DESC) " +
            "          WHERE ROWNUM < ? " + 
            " ) WHERE rn >= ?";
        
        boolean hasTermSesPermission = false;
        
        try{ 
      	  	if (isEditMenuPermited("CLIENTS_TERMSES")>=0) {
      	  		hasTermSesPermission = true;
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
            for (int i=1; i <= colCount; i++) {
                html.append(getBottomFrameTableTH(terminalXML, mtd.getColumnName(i)));
            }
            html.append("</tr></thead><tbody>");
            while (rset.next())
            {
            	html.append("<tr>");
          	  	for (int i=1; i <= colCount; i++) {
          	  		if (hasTermSesPermission && "ID_TERM_SES".equalsIgnoreCase(mtd.getColumnName(i))) {
          	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/clients/termsespecs.jsp?id="+rset.getString(i), "", ""));
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
    } //getAssignTermHistoryHTML
	
    public String getTerminalHistoryHTML(String pFindString, String p_beg, String p_end) {
    	bcEntriesHistoryObject history = new bcEntriesHistoryObject("'TERM','TERM_SAM'", this.idTerminal);
        return history.getHistoryHTML(pFindString, p_beg, p_end);
    }

    public String getLoadedCertificatesHTML(String pFindString, String p_beg, String p_end) {
        StringBuilder html = new StringBuilder();
        Connection con = null;
        PreparedStatement st = null;
        ArrayList<bcFeautureParam> pParam = initParamArray();

        String mySQL = 
        	" SELECT id_term, id_term_certificate, text_certificate, begin_action_date_frmt, " +
        	"        end_action_date_frmt, file_name, stored_file_name, stored_full_file_name " +
            "   FROM (SELECT ROWNUM rn, id_term, id_term_certificate, text_certificate, begin_action_date_frmt, " +
            "                end_action_date_frmt, file_name, stored_file_name, stored_full_file_name " +
            " 	 	    FROM (SELECT * " +
            "                   FROM " + getGeneralDBScheme() + ".vc_term_certificates_priv_all "+
            " 	 	           WHERE id_term = ? ";
        pParam.add(new bcFeautureParam("int", this.idTerminal));
        
        if (!isEmpty(pFindString)) {
    		mySQL = mySQL + 
   				" AND (TO_CHAR(id_term_certificate) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(text_certificate) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(begin_action_date_frmt) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(end_action_date_frmt) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(file_name) LIKE UPPER('%'||?||'%'))";
    		for (int i=0; i<5; i++) {
    		    pParam.add(new bcFeautureParam("string", pFindString));
    		}
    	}
        pParam.add(new bcFeautureParam("int", p_end));
        pParam.add(new bcFeautureParam("int", p_beg));
        mySQL = mySQL +
            "                  ORDER BY begin_action_date DESC" +
            "                ) " +
            "          WHERE ROWNUM < ? " + 
        	"        ) " +
        	"  WHERE rn >= ?";
        //
        boolean hasTerminalCertificatePermission = false;
        
        try{
        	if (isEditMenuPermited("CLIENTS_CERTIFICATE") >0) {
        		hasTerminalCertificatePermission = true;
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
            html.append("</tr>\n");
            html.append("<tr>");
            for (int i=1; i <= colCount-2; i++) {
            	html.append(getBottomFrameTableTH(terminalCertificateXML, mtd.getColumnName(i)));
            }
            html.append("</tr></thead><tbody>");

            while (rset.next())
            {
                html.append("<tr>\n");
                for (int i=1; i <= colCount-2; i++) {
          	  		if (hasTerminalCertificatePermission && "ID_TERM".equalsIgnoreCase(mtd.getColumnName(i))) {
          	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/clients/certificatespecs.jsp?id_cert=&id_profile=T&id_term="+rset.getString(i), "", ""));
          	  		} else if ("id_term_certificate".equalsIgnoreCase(mtd.getColumnName(i))) {
          	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/clients/certificatespecs.jsp?id_term=" + rset.getString("id_term") + "&id_profile=C&id_cert="+rset.getString(i), "", ""));
          	  		} else {
         	  			if ("file_name".equalsIgnoreCase(mtd.getColumnName(i))) {
         	          	  	if (!isEmpty(rset.getString("STORED_FULL_FILE_NAME"))) {
         	          	  		html.append(getBottomFrameTableTD(mtd.getColumnName(i), "<a href=\"../FileSender?FILENAME=" + URLEncoder.encode(rset.getString("STORED_FULL_FILE_NAME"),"UTF-8") + "\" title=\"" + buttonXML.getfieldTransl("open", false) + "\" target=_blank>" + rset.getString("FILE_NAME") + "</a>", "", "", ""));
         	          	  	} else {
             	  				html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", "", ""));
         	          	  	}
         	  			} else {
         	  				html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", "", ""));
         	  			}
          	  		}
          	  	}
                html.append("</tr>\n");
            }
            html.append("</tbody></table>\n");
        	html.append("</form>\n");
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
    } // getValidityCertificatesHTML
    
    public String getTransactionsHTML(String pFindString, String pTypeTrans, String pPayType, String pStateTrans, String p_beg, String p_end) {
    	bcListTransaction list = new bcListTransaction();
    	
    	String pWhereCause = " WHERE id_term = ? ";
    	
    	ArrayList<bcFeautureParam> pWhereValue = new ArrayList<bcFeautureParam>();
    	pWhereValue.add(new bcFeautureParam("int", this.idTerminal));
    	
    	return list.getTransactionList(pWhereCause, pWhereValue, pFindString, pTypeTrans, pPayType, pStateTrans, p_beg, p_end);
    	
    }

/*    public String getTransactionsHTML(String pFindString, String pTransType, String p_beg, String p_end) {
        StringBuilder html = new StringBuilder();
        Connection con = null;
        PreparedStatement st = null;
        boolean hasTransPermission = false;
        boolean hasCardPermission = false;
        boolean hasSAMPermission = false;
        boolean hasServicePlacePermission = false;
        boolean hasTelegramPermission = false;
        ArrayList<bcFeautureParam> pParam = initParamArray();

        String mySQL  = 	
	        	" SELECT rn, id_trans, type_trans_txt, state_trans_tsl, sys_date, " +
	    		"        name_service_place, id_term_ses, id_telgr, id_sam, nt_sam, " +
	    		"        ver_trans, card_serial_number, nt_icc, nt_ext, action,  " +
		    	"		 opr_sum_frmt, sum_pay_cash_frmt, sum_pay_card_frmt, sum_pay_bon_frmt, " +
		    	"        sum_bon_frmt, sum_disc_frmt, " +
		    	"        id_issuer, id_payment_system, way_trans, id_service_place, src_trans, parse_state_trans " +
	    		"   FROM (SELECT rn, id_trans, type_trans_txt, state_trans_tsl, sys_date, " +
        		"        name_service_place, id_term_ses, id_telgr, id_sam, nt_sam, " +
        		"        ver_trans, card_serial_number, nt_icc, nt_ext, action,  " +
		    	"		 opr_sum_frmt, sum_pay_cash_frmt, sum_pay_card_frmt, sum_pay_bon_frmt, " +
		    	"        sum_bon_frmt, sum_disc_frmt, " +
		    	"        id_issuer, id_payment_system, way_trans, id_service_place, src_trans, parse_state_trans " +
        		"   FROM (SELECT ROWNUM rn, state_trans_tsl, id_term_ses, id_telgr, id_trans, id_sam, nt_sam, " +
        		"                DECODE(type_trans, " +
	    		"                       1,  '<font color=\"black\"><b>'||type_trans_txt||'</b></font>', " +
	    		"                       2,  '<font color=\"blue\">'||type_trans_txt||'</font>', " +
	    		"                       3,  '<font color=\"green\">'||type_trans_txt||'</font>', " +
	    		"                       4,  '<font color=\"red\"><b>'||type_trans_txt||'</b></font>', " +
	    		"                       5,  '<font color=\"red\">'||type_trans_txt||'</font>', " +
	    		"                       6,  '<font color=\"black\"><b>'||type_trans_txt||'</b></font>', " +
	    		"                       7,  '<font color=\"black\"><b>'||type_trans_txt||'</b></font>', " +
	    		"                       type_trans_txt) type_trans_txt, " +
	    		"                ver_trans, card_serial_number, nt_icc, nt_ext, " +
        		"                sys_date, action,  " +
		    	"       		 opr_sum_frmt, sum_pay_cash_frmt, sum_pay_card_frmt, sum_pay_bon_frmt, " +
		    	"        		 sum_bon_frmt, sum_disc_frmt, " +
		    	" 			     id_issuer, id_payment_system, way_trans," +
        		"                id_service_place, name_service_place, src_trans, parse_state_trans, type_trans " +
        		"           FROM (SELECT DECODE(state_trans, " +
	    		"                               -9,  '<font color=\"gray\">'||state_trans_tsl||'</font>', " +
	    		"                               0,  state_trans_tsl, " +
	    		"                               1,  '<font color=\"green\"><b>'||state_trans_tsl||'</b></font>', " +
	    		"                               5,  '<font color=\"gray\"><b>'||state_trans_tsl||'</b></font>', " +
	    		"                               8,  '<font color=\"red\"><b>'||state_trans_tsl||'</b></font>', " +
	    		"                               9,  '<font color=\"red\"><b>'||state_trans_tsl||'</b></font>', " +
	    		"                               state_trans_tsl) " +
	    		"                        state_trans_tsl, id_term_ses, id_telgr, id_trans, " +
        		"						 NULL id_sam, NULL nt_sam, type_trans, type_trans_txt, ver_trans, " +
        		"						 NULL card_serial_number, NULL nt_icc, NULL nt_ext, NULL sys_date, NULL action,  " +
		    	"		 			     NULL opr_sum_frmt, NULL sum_pay_cash_frmt, NULL sum_pay_card_frmt, NULL sum_pay_bon_frmt, " +
		    	"        				 NULL sum_bon_frmt, NULL sum_disc_frmt, " +
        		"                        NULL id_issuer, NULL id_payment_system, 'P' way_trans," +
        		"                        NULL id_service_place, NULL name_service_place," +
        		"                		 CASE WHEN LENGTH(src_trans) > 100 " +
        		"                     		  THEN SUBSTR(src_trans, 1, 100)||'...'" +
        		"                     		  ELSE src_trans" +
        		"					     END src_trans, " +
        		"				         'P' parse_state_trans " +
        		"					FROM " + getGeneralDBScheme() + ".vc_trans_not_parsed_all " +
        		"				   WHERE id_term = ? " + 
        		"				  UNION  " +
        		"				  SELECT DECODE(state_trans, " +
	    		"                               -9,  '<font color=\"gray\">'||state_trans_tsl||'</font>', " +
	    		"                               -7,  '<font color=\"blue\">'||state_trans_tsl||'</font>', " +
	    		"                               -6,  '<font color=\"blue\">'||state_trans_tsl||'</font>', " +
	    		"                               -4,  '<font color=\"green\">'||state_trans_tsl||'</font>', " +
	    		"                               -2,  '<font color=\"ligthred\">'||state_trans_tsl||'</font>', " +
	    		"                               -1,  '<font color=\"darkbrown\">'||state_trans_tsl||'</font>', " +
	    		"                               0,  state_trans_tsl, " +
	    		"                               1,  '<font color=\"red\"><b>'||state_trans_tsl||'</b></font>', " +
	    		"                               5,  '<font color=\"red\"><b>'||state_trans_tsl||'</b></font>', " +
	    		"                               9,  '<font color=\"red\"><b>'||state_trans_tsl||'</b></font>', " +
	    		"                               state_trans_tsl) " +
	    		"                        state_trans_tsl, id_term_ses, id_telgr, id_trans, id_sam, nt_sam, " + 
        		"						 type_trans, type_trans_txt, ver_trans, card_nr card_serial_number, nt_icc, " +
        		"						 nt_ext, sys_date||' '||sys_time sys_date, action,  " +
		    	"		 				 opr_sum opr_sum_frmt, sum_pay_cash sum_pay_cash_frmt, " +
		    	"						 sum_pay_card sum_pay_card_frmt, sum_pay_bon sum_pay_bon_frmt, " +
		    	"        				 sum_bon sum_bon_frmt, sum_disc sum_disc_frmt, " +
		    	"						 id_issuer, id_payment_system, 'P' way_trans," +
        		"                        term_id_service_place id_service_place, term_name_service_place name_service_place," +
        		"                        NULL src_trans, 'E' parse_state_trans " +
        		"				    FROM " + getGeneralDBScheme() + ".vc_trans_error_all " +
        		"				   WHERE id_term = ? " + 
        		"				   UNION  " +
        		"				  SELECT DECODE(state_trans, " +
	    		"                               -9,  '<font color=\"gray\">'||state_trans_tsl||'</font>', " +
	    		"                               -7,  '<font color=\"blue\">'||state_trans_tsl||'</font>', " +
	    		"                               -6,  '<font color=\"blue\">'||state_trans_tsl||'</font>', " +
	    		"                               -4,  '<font color=\"green\">'||state_trans_tsl||'</font>', " +
	    		"                               -2,  '<font color=\"ligthred\">'||state_trans_tsl||'</font>', " +
	    		"                               -1,  '<font color=\"darkbrown\">'||state_trans_tsl||'</font>', " +
	    		"                               0,  state_trans_tsl, " +
	    		"                               1,  '<font color=\"red\"><b>'||state_trans_tsl||'</b></font>', " +
	    		"                               5,  '<font color=\"red\"><b>'||state_trans_tsl||'</b></font>', " +
	    		"                               9,  '<font color=\"red\"><b>'||state_trans_tsl||'</b></font>', " +
	    		"                               state_trans_tsl) " +
	    		"                        state_trans_tsl, " +
        		"                        id_term_ses, id_telgr, id_trans, id_sam, nt_sam, " + 
        		"						 type_trans, type_trans_txt, ver_trans, card_serial_number, TO_CHAR(nt_icc) nt_icc,  " +
        		"						 TO_CHAR(nt_ext) nt_ext, sys_date_full_frmt sys_date, action,  " +
		    	"		 				 opr_sum_frmt, sum_pay_cash_frmt, sum_pay_card_frmt, sum_pay_bon_frmt, " +
		    	"        				 sum_bon_frmt, sum_disc_frmt, " +
		    	"						 id_issuer, id_payment_system, 'F' way_trans," +
        		"                        id_service_place, name_service_place," +
        		"                        NULL src_trans, 'N' parse_state_trans " +
        		"					FROM " + getGeneralDBScheme() + ".vc_trans_ok_club_all " +
        		"				   WHERE id_term = ? " +  
        		"				  ) WHERE 1=1";
        pParam.add(new bcFeautureParam("int", this.idTerminal));
        pParam.add(new bcFeautureParam("int", this.idTerminal));
        pParam.add(new bcFeautureParam("int", this.idTerminal));
        
        if (!isEmpty(pFindString)) {
    		mySQL = mySQL + 
   				" AND (TO_CHAR(id_trans) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(sys_date) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(name_service_place) LIKE UPPER('%'||?||'%') OR " +
   				"      TO_CHAR(id_term_ses) LIKE UPPER('%'||?||'%') OR " +
   				"      TO_CHAR(id_telgr) LIKE UPPER('%'||?||'%') OR " +
   				"      TO_CHAR(id_sam) LIKE UPPER('%'||?||'%'))";
    		for (int i=0; i<6; i++) {
    		    pParam.add(new bcFeautureParam("string", pFindString));
    		}
    	}
    	if (!isEmpty(pTransType)) {
    		mySQL = mySQL + " AND type_trans = ? ";
    		pParam.add(new bcFeautureParam("int", pTransType));
    	}
    	pParam.add(new bcFeautureParam("int", p_end));
    	pParam.add(new bcFeautureParam("int", p_beg));
    	mySQL = mySQL +
        		"    ORDER BY id_trans) WHERE ROWNUM < ?) WHERE rn >= ?";
        try{ 
            if (isEditMenuPermited("CARDS_TRANSACTIONS")>=0) {
            	hasTransPermission = true;
            }
            if (isEditMenuPermited("CLIENTS_SAM")>=0) {
            	hasSAMPermission = true;
            }
            if (isEditMenuPermited("CARDS_CLUBCARDS")>=0) {
            	hasCardPermission = true;
            }
            if (isEditMenuPermited("CLIENTS_SERVICE_PLACE")>=0) {
            	hasServicePlacePermission = true;
            }
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
            for (int i=1; i <= colCount-6; i++) {
            	html.append(getBottomFrameTableTH(transactionXML, mtd.getColumnName(i)));
            }
            html.append("</tr></thead><tbody>");
            
            while (rset.next())
            {
            	html.append("<tr>");
      	  		if ("P".equalsIgnoreCase(rset.getString("PARSE_STATE_TRANS"))) {
          	  		html.append(getBottomFrameTableTD("RN", rset.getString("RN"), "", "", ""));
       	  			if (hasTransPermission) {
       	  				html.append(getBottomFrameTableTD("ID_TRANS", rset.getString("ID_TRANS"), "../crm/cards/trans_correctionspecs.jsp?id="+rset.getString("ID_TRANS"), "", ""));
       	  			} else {
       	  				html.append(getBottomFrameTableTD("ID_TRANS", rset.getString("ID_TRANS"), "", "", ""));
       	  			}
          	  		html.append(getBottomFrameTableTD("TYPE_TRANS_TXT", rset.getString("TYPE_TRANS_TXT"), "", "", ""));
          	  		html.append(getBottomFrameTableTD("STATE_TRANS_TSL", rset.getString("STATE_TRANS_TSL"), "", "", ""));
          	  		html.append(getBottomFrameTableTDBase("17", "SRC_TRANS", Types.OTHER, rset.getString("SRC_TRANS"), "", "", ""));
          	  	} else {
	            	for (int i=1; i <= colCount-6; i++) {
	          	  		if ("ID_TRANS".equalsIgnoreCase(mtd.getColumnName(i))) {
	          	  			if (hasTransPermission && "P".equalsIgnoreCase(rset.getString("WAY_TRANS"))) {
	          	  				html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/cards/trans_correctionspecs.jsp?id="+rset.getString(i), "", ""));
	          	  			} else if (hasTransPermission && "F".equalsIgnoreCase(rset.getString("WAY_TRANS"))) {
	          	  				html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/cards/transactionspecs.jsp?id="+rset.getString(i), "", ""));
	          	  			} else {
	          	  				html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", "", ""));
	          	  			}
	          	  		} else if (hasTelegramPermission && "ID_TERM_SES".equalsIgnoreCase(mtd.getColumnName(i)) && 
	          	  				!isEmpty(rset.getString(i))) {
	          	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/clients/termsespecs.jsp?id="+rset.getString(i), "", ""));
	          	  		} else if (hasCardPermission && "CARD_SERIAL_NUMBER".equalsIgnoreCase(mtd.getColumnName(i)) && 
	      	  					!isEmpty(rset.getString("ID_ISSUER"))) {
	      	  				html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/cards/clubcardspecs.jsp?id="+rset.getString("CARD_SERIAL_NUMBER")+"&iss="+rset.getString("ID_ISSUER")+"&paysys="+rset.getString("ID_PAYMENT_SYSTEM"), "", ""));
	          	  		} else if (hasSAMPermission && "ID_SAM".equalsIgnoreCase(mtd.getColumnName(i)) && 
	          	  				!isEmpty(rset.getString(i))) {
	          	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/clients/samspecs.jsp?id="+rset.getString(i), "", ""));
		          	  	} else if (hasServicePlacePermission && "NAME_SERVICE_PLACE".equalsIgnoreCase(mtd.getColumnName(i)) && 
		      	  				!isEmpty(rset.getString(i))) {
		      	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/clients/service_placespecs.jsp?id="+rset.getString("ID_SERVICE_PLACE"), "", ""));
		          	  	} else if (hasTelegramPermission && "ID_TELGR".equalsIgnoreCase(mtd.getColumnName(i)) && 
		      	  				!isEmpty(rset.getString(i))) {
		      	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/clients/telegramspecs.jsp?id="+rset.getString("ID_TELGR"), "", ""));
		      	  		} else {
	          	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", "", ""));
	          	  		}
	            	}
          	  	}
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
    } //getTermSessionsTransactionsHTML*/

    public String getTerminalUsersHTML(String pFindString, String pTermUserAccessType, String pTermUserStatus, String p_beg, String p_end) {
    	bcListTerminalUser list = new bcListTerminalUser();
    	
    	String pWhereCause = " WHERE id_term = ? ";
    	
    	ArrayList<bcFeautureParam> pWhereValue = new ArrayList<bcFeautureParam>();
    	pWhereValue.add(new bcFeautureParam("int", this.idTerminal));
    	String myDeleteLink = "";
    	String myEditLink = "";
    	if (isEditPermited("CLIENTS_TERMINALS_USERS")>0) {
    		myDeleteLink = "../crm/clients/terminaluserupdate.jsp?back_type=TERMINAL&type=user&id="+this.idTerminal+"&id_term="+this.idTerminal+"&action=remove&process=yes";
    	    myEditLink = "../crm/clients/terminaluserupdate.jsp?back_type=TERMINAL&type=user&id="+this.idTerminal+"&id_term="+this.idTerminal;
    	}
    	
    	return list.getTerminalUsersHTML(pWhereCause, pWhereValue, "TERMINAL", pFindString, pTermUserAccessType, pTermUserStatus, myEditLink, myDeleteLink, p_beg, p_end);
	
    }

    public String getOnlinePaymentTypesHTML(String pFind, String p_beg, String p_end) {
    	StringBuilder html = new StringBuilder();
	      Connection con = null;
	      PreparedStatement st = null;
	      boolean hasEditPermission = false;
	      boolean hasOPTypesPermission = false;

	      ArrayList<bcFeautureParam> pParam = initParamArray();

	      String mySQL = 
	    	  " SELECT rn, name_club_online_pay_type, term_card_req_club_pay_id, " +
	    	  "        DECODE(need_calc_pin, " +
    		  "               'Y', '<font color=\"red\"><b>'||need_calc_pin_tsl||'</b></font>', " +
    		  "               need_calc_pin_tsl" +
    		  "        ) need_calc_pin_tsl, " + 
			  "        DECODE(exist_term_online_pay_type, " +
			  "               'Y', '<b><font color=\"green\">'||exist_term_online_pay_type_tsl||'</font></b>', " +
			  "               '<b><font color=\"red\">'||exist_term_online_pay_type_tsl||'</font></b>'" +
			  "        ) exist_term_online_pay_type_tsl," +
			  "        id_club_online_pay_type, id_term_online_pay_type " +
			  "   FROM (SELECT ROWNUM rn, name_club_online_pay_type, term_card_req_club_pay_id, " +
			  "                need_calc_pin, need_calc_pin_tsl, exist_term_online_pay_type," +
			  "                exist_term_online_pay_type_tsl, id_club_online_pay_type, id_term_online_pay_type " +
			  "           FROM (SELECT name_club_online_pay_type, term_card_req_club_pay_id, " +
			  "                        need_calc_pin, need_calc_pin_tsl, exist_term_online_pay_type, " +
			  "                        exist_term_online_pay_type_tsl, id_club_online_pay_type, id_term_online_pay_type " +
			  "                   FROM " + getGeneralDBScheme() + ".vc_term_online_pay_type_cl_all "+
			  "                  WHERE id_term = ? ";
	      pParam.add(new bcFeautureParam("int", this.idTerminal));
	      
	      if (!isEmpty(pFind)) {
	    		mySQL = mySQL + 
	   				" AND (TO_CHAR(id_term) LIKE UPPER('%'||?||'%') OR " +
	   				"      UPPER(term_card_req_club_pay_id) LIKE UPPER('%'||?||'%'))";
	    		for (int i=0; i<2; i++) {
	    		    pParam.add(new bcFeautureParam("string", pFind));
	    		}
	      }
	      pParam.add(new bcFeautureParam("int", p_end));
	      pParam.add(new bcFeautureParam("int", p_beg));
	      mySQL = mySQL +
            "                  ORDER BY id_term)" +
            "          WHERE ROWNUM < ? " + 
            " ) WHERE rn >= ?";
	      try{
	    	  if (isEditPermited("CLIENTS_TERMINALS_OP_TYPES")>0) {
	    		  hasEditPermission = true;
	    	  }
	    	  if (isEditMenuPermited("CLUB_ONLINE_PAY_TYPE")>=0) {
	    		  hasOPTypesPermission = true;
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
	               html.append(getBottomFrameTableTH(terminalXML, mtd.getColumnName(i)));
	          }
	          if (hasEditPermission) {
	            html.append("<th>&nbsp;</th><th>&nbsp;</th>\n");
	          }
	          html.append("</tr></thead><tbody>\n");
	          while (rset.next())
	          {
	        	  html.append("<tr>");
	          	  for (int i=1; i <= colCount-2; i++) {
	          		  if ("NAME_CLUB_ONLINE_PAY_TYPE".equalsIgnoreCase(mtd.getColumnName(i)) && hasOPTypesPermission) {
	          			  html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/club/online_pay_typespecs.jsp?id=" + rset.getString("ID_CLUB_ONLINE_PAY_TYPE"), "", ""));
	          		  } else {
	          			  html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", "", ""));
	          		  }
	          	  }
	            if (hasEditPermission) {
	            	String myHyperLink = "../crm/clients/terminalupdate.jsp?id_term="+this.idTerminal+"&id_term_online_pay_type="+rset.getString("ID_TERM_ONLINE_PAY_TYPE") + "&type=online_type";
	            	String myDeleteLink = "../crm/clients/terminalupdate.jsp?id_term="+this.idTerminal+"&id_term_online_pay_type="+rset.getString("ID_TERM_ONLINE_PAY_TYPE")+"&type=online_type&action=remove&process=yes";
	                html.append(getDeleteButtonHTML(myDeleteLink, terminalXML.getfieldTransl("h_delete_term_online_pay_type", false), rset.getString("NAME_CLUB_ONLINE_PAY_TYPE")));
	            	html.append(getEditButtonHTML(myHyperLink));
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
    } // getCardRequestsHTML

    public String getTermLoyalityHistoryHTML(String pFindString, String p_beg, String p_end) {
    	ArrayList<bcFeautureParam> pParam = initParamArray();

    	String mySQL = 
    		" SELECT rn, date_beg_frmt, date_end_frmt, name_loyality_scheme, " +
            "		 cd_kind_loyality, id_loyality_history " +
	  		"   FROM (SELECT ROWNUM rn, name_loyality_scheme, " +
            "		         cd_kind_loyality, date_beg_frmt, date_end_frmt, id_loyality_history " +
  			"           FROM (SELECT name_loyality_scheme, " + 
  			"						 cd_kind_loyality||' - '||name_kind_loyality cd_kind_loyality, " +
  			"						 date_beg_frmt, date_end_frmt, id_loyality_history" +
  			"                   FROM " + getGeneralDBScheme() + ".vc_term_loyality_h_club_all " +
    		"                  WHERE id_term = ? ";
    	pParam.add(new bcFeautureParam("int", this.idTerminal));
    	
        if (!isEmpty(pFindString)) {
    		mySQL = mySQL + 
   				" AND (TO_CHAR(id_line) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(date_beg_frmt) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(date_end_frmt) LIKE UPPER('%'||?||'%'))";
    		for (int i=0; i<3; i++) {
    		    pParam.add(new bcFeautureParam("string", pFindString));
    		}
    	}
   		pParam.add(new bcFeautureParam("int", p_end));
   		pParam.add(new bcFeautureParam("int", p_beg));
       	mySQL = mySQL +
			"               ORDER BY date_beg desc)" + 
    		"           WHERE ROWNUM < ? " + 
    		" ) WHERE rn >= ?";
    	
		StringBuilder html = new StringBuilder();
		Connection con = null;
		PreparedStatement st = null;
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
            for (int i=1; i <= colCount-1; i++) {
            	html.append(getBottomFrameTableTH(loyXML, mtd.getColumnName(i)));
            }
            html.append("</tr></thead><tbody>");
            
            while (rset.next())
            {
            	html.append("<tr>");
          	  	for (int i=1; i <= colCount-1; i++) {
          	  		html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/clients/terminalspecs.jsp?id=" + this.idTerminal + "&hist=y&id_loy_history="+rset.getString("ID_LOYALITY_HISTORY"), "", ""));
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
    } // getTermServCardHTML
    
}
