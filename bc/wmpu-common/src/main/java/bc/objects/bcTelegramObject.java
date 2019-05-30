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
import bc.lists.bcListTransaction;
import bc.service.bcFeautureParam;

public class bcTelegramObject extends bcObject {
	private final static Logger LOGGER=Logger.getLogger(bcTelegramObject.class);
	private Map<String,Comparable<String>> fieldHm = new HashMap<String, Comparable<String>>();
	
	private String idTelegram;
	
	public bcTelegramObject(String pIdTelegram) {
		this.idTelegram = pIdTelegram;
	}

	public void getFeature() {
		String featureSelect = " SELECT * FROM " + getGeneralDBScheme() + ".VC_TELGR_CLUB_ALL WHERE id_telgr = ?";
		fieldHm = getFeatures2(featureSelect, this.idTelegram, false);
	}
	
	public String getValue(String pColumnName) {
		return getFeautureResult(fieldHm, pColumnName);
	}
	
    public String getTelegramParamHTML() {
        StringBuilder html = new StringBuilder();
        Connection con = null;
        PreparedStatement st = null;

        ArrayList<bcFeautureParam> pParam = initParamArray();

        String mySQL = 
        	" SELECT cd_telgr_type, id_telgr, outher_sid, nt_msg_b, tel_version, id_term, id_sam, " +
            " 		 nt_sam, nc, ni, date_telgr_frmt date_telgr, " +
            "        DECODE(way_telgr, " +
    		"               'RECEIVED', '<font color=\"blue\"><b>" + telegramXML.getfieldTransl("way_telgr_received", false)+ "</b></font>', " +
    		"               'SENT', '<font color=\"green\"><b>" + telegramXML.getfieldTransl("way_telgr_sent", false)+ "</b></font>', " +
    		"               ''" +
    		"        ) way_telgr_tsl, " +
    		"        name_telgr_state " +
            "   FROM " + getGeneralDBScheme() + ".vc_telgr_club_all " +
            "  WHERE id_telgr = ? ";
        pParam.add(new bcFeautureParam("int", this.idTelegram));
        
        boolean hasTermPermission = false;
        boolean hasSAMPermission = false;
        try{

            if (isEditMenuPermited("CLIENTS_TERMINALS")>=0) {
            	hasTermPermission = true;
            }
            if (isEditMenuPermited("CLIENTS_SAM")>=0) {
            	hasSAMPermission = true;
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
            //html.append("<th>&nbsp;</th><th>&nbsp;</th>");
            for (int i=1; i <= colCount; i++) {
            	if ("CD_TELGR_TYPE".equalsIgnoreCase(mtd.getColumnName(i))) {
            		html.append("<th colspan=\"3\">"+ telegramXML.getfieldTransl( mtd.getColumnName(i), false)+"</th>\n");
            	} else {
            		html.append(getBottomFrameTableTH(telegramXML, mtd.getColumnName(i)));
            	}
            }
            html.append("</tr></thead><tbody>");
            while (rset.next())
            {
                html.append("<tr>");
          	  	for (int i=1; i <= colCount; i++) {
          	  		if ("CD_TELGR_TYPE".equalsIgnoreCase(mtd.getColumnName(i))) {
          	  			html.append("<td>");
          	  			html.append(getHyperLinkFirst()+"../crm/clients/telegramspecs.jsp?id="+this.idTelegram+"&src_type=FORMATTED"+getHyperLinkMiddle()+
          	  				"<img vspace=\"0\" hspace=\"0\"" +
            				" src=\"../images/oper/rows/telgr_format.png\" align=\"top\" style=\"border: 0px;\"" + 
            				" title=\"" + buttonXML.getfieldTransl("button_telgr_format", false)+"\">");
          	  			html.append("</td>");
          	  			html.append("<td>\n");
          	  			html.append(getHyperLinkFirst()+"../crm/clients/telegramspecs.jsp?id="+this.idTelegram+"&src_type=SOURCE"+getHyperLinkMiddle()+
          	  				"<img vspace=\"0\" hspace=\"0\"" +
            				" src=\"../images/oper/rows/telgr_source.png\" align=\"top\" style=\"border: 0px;\"" + 
            				" title=\"" + buttonXML.getfieldTransl("button_telgr_source", false)+"\">"+
            				getHyperLinkEnd());
          	  			html.append("</td>");
          	  			html.append("<td>\n");
          	  			html.append(getHyperLinkFirst()+"../crm/clients/telegramspecs.jsp?id="+this.idTelegram+getHyperLinkMiddle()+getValue2(rset.getString("CD_TELGR_TYPE"))+getHyperLinkEnd());
          	  			html.append("</td>\n");
          	  		} else if (hasTermPermission && "ID_TERM".equalsIgnoreCase(mtd.getColumnName(i)) && 
          	  				!isEmpty(rset.getString(i))) {
          	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/clients/terminalspecs.jsp?id="+rset.getString(i), "", ""));
	          	  	} else if (hasSAMPermission && "ID_SAM".equalsIgnoreCase(mtd.getColumnName(i)) && 
	      	  				!isEmpty(rset.getString(i))) {
	      	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/clients/samspecs.jsp?id="+rset.getString(i), "", ""));
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
    } //getSessionTelegramsHTML
	
    public String getTelegramSourceHTML(String pSrcType, String pIdTermSes) {
        StringBuilder html = new StringBuilder();
        Connection con = null;
        String mySQL = "";
        
        ArrayList<bcFeautureParam> pParam = initParamArray();

        PreparedStatement st = null;
        String hasSAMPermisson = "N";
        String hasTermPermisson = "N";
        String hasCardPermisson = "N";
        try{
        	if (isEditMenuPermited("CLIENTS_SAM")>=0) {
        		hasSAMPermisson = "Y";
        	}
        	if (isEditMenuPermited("CLIENTS_TERMINALS")>=0) {
        		hasTermPermisson = "Y";
        	}
        	if (isEditMenuPermited("CARDS_CLUBCARDS")>=0) {
        		hasCardPermisson = "Y";
        	}
        	if ("FORMATTED".equalsIgnoreCase(pSrcType)) {
            	mySQL = 
            		" SELECT " + getGeneralDBScheme() + ".FNC_FORMAT_SRC_TELGR3(a.cd_telgr_type,?,?,?,a.src_telgr) src_msg " +
                    "   FROM " + getGeneralDBScheme() + ".vc_telgr_src2_all a " +
                    "  WHERE a.id_telgr = ? ";
            	pParam.add(new bcFeautureParam("string", hasTermPermisson));
            	pParam.add(new bcFeautureParam("string", hasSAMPermisson));
            	pParam.add(new bcFeautureParam("string", hasCardPermisson));
            	pParam.add(new bcFeautureParam("int", this.idTelegram));
            } else if ("SOURCE".equalsIgnoreCase(pSrcType)) {
            	mySQL = 
            		" SELECT a.src_telgr src_msg " +
                    "   FROM " + getGeneralDBScheme() + ".vc_telgr_src_all a " +
                    "  WHERE a.id_telgr = ? ";
            	pParam.add(new bcFeautureParam("int", this.idTelegram));
            } else {
            	mySQL = 
            		" SELECT a.src_telgr_input src_msg " +
                    "   FROM " + getGeneralDBScheme() + ".vc_telgr_src_all a " +
                    "  WHERE a.id_telgr = ? ";
            	pParam.add(new bcFeautureParam("int", this.idTelegram));
            }
        	
        	LOGGER.debug(prepareSQLToLog(mySQL, pParam));
        	con = Connector.getConnection(getSessionId());
        	st = con.prepareStatement(mySQL);
        	st = prepareParam(st, pParam);
        	ResultSet rset = st.executeQuery();
            
            ResultSetMetaData mtd = rset.getMetaData();
            int colCount = mtd.getColumnCount();

            if ("INPUT".equalsIgnoreCase(pSrcType)) {
            	while (rset.next()) {
            		html.append(rset.getString("SRC_MSG"));
            	}
            } else {
	            html.append(getBottomFrameTable());
	            html.append("<tr>");
	            for (int i=1; i <= colCount; i++) {
	                String colName = mtd.getColumnName(i);
	                html.append("<th>");
	                html.append(this.getValue("ID_TELGR") + "&nbsp;-&nbsp;" + this.getValue("CD_TELGR_TYPE") + ":&nbsp;");
	                html.append(telegramXML.getfieldTransl( colName, false));
	                html.append("&nbsp;&nbsp;<select onchange=\"ajaxpage('../crm/clients/termsespecs.jsp?id=" + pIdTermSes + "&id_telgr=" +this.idTelegram + "&src_type='+getElementById('src_type').value, 'div_main')\" name=\"src_type\" id=\"src_type\" class=\"inputfield\">\n");
	                if ("FORMATTED".equalsIgnoreCase(pSrcType)) {
	                	html.append("<option value=\"FORMATTED\" selected>"+buttonXML.getfieldTransl("button_telgr_format", false)+"</option>\n");
	                } else {
	                	html.append("<option value=\"FORMATTED\">"+buttonXML.getfieldTransl("button_telgr_format", false)+"</option>\n");
	                }
	                if ("SOURCE".equalsIgnoreCase(pSrcType)) {
	                	html.append("<option value=\"SOURCE\" selected>"+buttonXML.getfieldTransl("button_telgr_source", false)+"</option>\n");
	                } else {
	                	html.append("<option value=\"SOURCE\">"+buttonXML.getfieldTransl("button_telgr_source", false)+"</option>\n");
	                }
	                html.append("</select>\n");
	                html.append("</th>\n");
	            }
	            html.append("</tr></thead><tbody>");
	            while (rset.next())
	            {
	                html.append("<tr>");
	                html.append("<td>"+getValue2(rset.getString("SRC_MSG")).replaceAll("NEW_LINE", "\n")+"</td>\n");
	                html.append("</tr>\n");
	            }
	            html.append("</tbody></table>\n");
            }
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
    } //getTelegramSourceHTML
    
    public String getTransactionsHTML(String pFindString, String pTypeTrans, String pPayType, String pStateTrans, String p_beg, String p_end) {
    	bcListTransaction list = new bcListTransaction();
    	
    	String pWhereCause = " WHERE id_telgr = ? ";
    	
    	ArrayList<bcFeautureParam> pWhereValue = new ArrayList<bcFeautureParam>();
    	pWhereValue.add(new bcFeautureParam("int", this.idTelegram));
    	
    	return list.getTransactionList(pWhereCause, pWhereValue, pFindString, pTypeTrans, pPayType, pStateTrans, p_beg, p_end);
    	
    }

    /*public String getTransactionsHTML(String pFindString, String pTransType, String p_beg, String p_end) {
        StringBuilder html = new StringBuilder();
        Connection con = null;
        PreparedStatement st = null;
        boolean hasTransPermission = false;
        boolean hasTermPermission = false;
        boolean hasSAMPermission = false;
        ArrayList<bcFeautureParam> pParam = initParamArray();

        String mySQL  = 	
	        	" SELECT rn, id_trans, type_trans_txt, state_trans_tsl, sys_date, " +
	    		"        name_service_place, id_telgr, id_term, id_sam, nt_sam, " +
	    		"        ver_trans, card_serial_number, nt_icc, nt_ext, action,  " +
		    	"		 opr_sum_frmt, sum_pay_cash_frmt, sum_pay_card_frmt, sum_pay_bon_frmt, " +
		    	"        sum_bon_frmt, sum_disc_frmt, " +
		    	"        id_issuer, id_payment_system, way_trans, id_service_place, src_trans, parse_state_trans " +
	    		"   FROM (SELECT rn, id_trans, type_trans_txt, state_trans_tsl, sys_date, " +
	    		"        name_service_place, id_telgr, id_term, id_sam, nt_sam, " +
	    		"        ver_trans, card_serial_number, nt_icc, nt_ext, action,  " +
		    	"		 opr_sum_frmt, sum_pay_cash_frmt, sum_pay_card_frmt, sum_pay_bon_frmt, " +
		    	"        sum_bon_frmt, sum_disc_frmt, " +
		    	"        id_issuer, id_payment_system, way_trans, id_service_place, src_trans, parse_state_trans " +
	    		"   FROM (SELECT ROWNUM rn, state_trans_tsl, id_telgr, id_trans, id_term, id_sam, nt_sam, " +
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
	    		"                id_service_place, name_service_place, src_trans, parse_state_trans " +
	    		"           FROM (SELECT DECODE(state_trans, " +
	    		"                               -9,  '<font color=\"gray\">'||state_trans_tsl||'</font>', " +
	    		"                               0,  state_trans_tsl, " +
	    		"                               1,  '<font color=\"green\"><b>'||state_trans_tsl||'</b></font>', " +
	    		"                               5,  '<font color=\"gray\"><b>'||state_trans_tsl||'</b></font>', " +
	    		"                               8,  '<font color=\"red\"><b>'||state_trans_tsl||'</b></font>', " +
	    		"                               9,  '<font color=\"red\"><b>'||state_trans_tsl||'</b></font>', " +
	    		"                               state_trans_tsl) " +
	    		"                        state_trans_tsl, id_telgr, id_trans, id_term, " +
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
        		"				   WHERE id_telgr = ? " + 
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
	    		"                        state_trans_tsl, id_telgr, id_trans, id_term, id_sam, nt_sam, " + 
        		"						 type_trans, type_trans_txt, ver_trans, card_nr card_serial_number, nt_icc, " +
        		"						 nt_ext, sys_date||' '||sys_time sys_date, action,  " +
		    	"		 				 opr_sum opr_sum_frmt, sum_pay_cash sum_pay_cash_frmt, " +
		    	"						 sum_pay_card sum_pay_card_frmt, sum_pay_bon sum_pay_bon_frmt, " +
		    	"        				 sum_bon sum_bon_frmt, sum_disc sum_disc_frmt, " +
		    	"						 id_issuer, id_payment_system, 'P' way_trans," +
        		"                        NULL id_service_place, NULL name_service_place," +
        		"                        NULL src_trans, 'E' parse_state_trans " +
        		"				    FROM " + getGeneralDBScheme() + ".vc_trans_error_all " +
        		"				   WHERE id_telgr = ? " + 
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
	    		"                        state_trans_tsl, " +
        		"                        id_telgr, id_trans, id_term, id_sam, nt_sam, " + 
        		"						 type_trans, type_trans_txt, ver_trans, card_serial_number, TO_CHAR(nt_icc) nt_icc,  " +
        		"						 TO_CHAR(nt_ext) nt_ext, sys_date_full_frmt sys_date, action,  " +
		    	"		 				 opr_sum_frmt, sum_pay_cash_frmt, sum_pay_card_frmt, sum_pay_bon_frmt, " +
		    	"        				 sum_bon_frmt, sum_disc_frmt, " +
		    	"						 id_issuer, id_payment_system, 'F' way_trans," +
        		"                        id_service_place, name_service_place," +
        		"                        NULL src_trans, 'N' parse_state_trans " +
        		"					FROM " + getGeneralDBScheme() + ".vc_trans_ok_club_all " +
        		"				   WHERE id_telgr = ? " + 
        		"				  ) WHERE 1=1 ";
        pParam.add(new bcFeautureParam("int", this.idTelegram));
        pParam.add(new bcFeautureParam("int", this.idTelegram));
        pParam.add(new bcFeautureParam("int", this.idTelegram));
        
        if (!isEmpty(pFindString)) {
    		mySQL = mySQL + 
   				" AND (TO_CHAR(id_trans) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(sys_date) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(name_service_place) LIKE UPPER('%'||?||'%') OR " +
   				"      TO_CHAR(id_telgr) LIKE UPPER('%'||?||'%') OR " +
   				"      TO_CHAR(id_term) LIKE UPPER('%'||?||'%') OR " +
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
        		"   ORDER BY id_trans) WHERE ROWNUM < ?) WHERE rn>= ?";
        try{ 
            if (isEditMenuPermited("CARDS_TRANSACTIONS")>=0) {
            	hasTransPermission = true;
            }
            if (isEditMenuPermited("CLIENTS_TERMINALS")>=0) {
            	hasTermPermission = true;
            }
            if (isEditMenuPermited("CLIENTS_SAM")>=0) {
            	hasSAMPermission = true;
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
	          	  		} else if (hasTermPermission && "ID_TERM".equalsIgnoreCase(mtd.getColumnName(i)) && 
	          	  				!isEmpty(rset.getString(i))) {
	          	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/clients/terminalspecs.jsp?id="+rset.getString(i), "", ""));
	          	  		} else if (hasSAMPermission && "ID_SAM".equalsIgnoreCase(mtd.getColumnName(i)) && 
	          	  				!isEmpty(rset.getString(i))) {
	          	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/clients/samspecs.jsp?id="+rset.getString(i), "", ""));
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

    public String getPostingsHTML(String pFind, String idBkOperationType, String p_beg, String p_end) {
        StringBuilder html = new StringBuilder();
        Connection con = null;
        PreparedStatement st = null;
        ArrayList<bcFeautureParam> pParam = initParamArray();

        String mySQL = 	
        	" SELECT rn, id_trans, id_posting_detail, id_posting_line, operation_date, name_currency," +
        	"        oper_number id_bk_operation_scheme_line, debet_id_bk_account, credit_id_bk_account, " +
        	"		 entered_amount, assignment_posting, id_clearing_line, " +
        	"        debet_id_bk_account2, debet_cd_bk_account, debet_name_bk_account, " +
        	"        credit_id_bk_account2, credit_cd_bk_account, credit_name_bk_account, " +
        	"        id_bk_operation_scheme_line id_bk_operation_scheme_line2 " +
			"   FROM (SELECT ROWNUM rn, id_trans, id_posting_detail, id_posting_line, operation_date_frmt operation_date, " +
        	"                name_currency, oper_number, id_bk_operation_scheme_line, " +
        	"                debet_cd_bk_account||' '||debet_name_bk_account debet_id_bk_account,  " +
        	"        		 credit_cd_bk_account||' '||credit_name_bk_account credit_id_bk_account, " +
        	"		 		 entered_amount_frmt entered_amount, assignment_posting, " +
        	"        		 id_clearing_line, " +
        	"        		 debet_id_bk_account debet_id_bk_account2, " +
        	"                debet_cd_bk_account_frmt debet_cd_bk_account, debet_name_bk_account, " +
        	"        		 credit_id_bk_account credit_id_bk_account2, " +
        	"                credit_cd_bk_account_frmt credit_cd_bk_account, credit_name_bk_account " +
        	"           FROM (SELECT * " +
        	"                   FROM " + getGeneralDBScheme() + ".vc_acc_posting_detail_club_all " +
        	"                  WHERE id_trans IN (SELECT id_trans " +
        	"                                       FROM " + getGeneralDBScheme() + ".vc_trans_ok_club_all " +
        	"		                               WHERE id_telgr = ? ";
        pParam.add(new bcFeautureParam("int", this.idTelegram));
        

        if (!isEmpty(idBkOperationType)) {
        	mySQL = mySQL + " AND id_bk_operation_scheme_line IN (" +
        			" SELECT id_bk_operation_scheme_line " +
        			"   FROM "+getGeneralDBScheme() + ".vc_oper_scheme_lines_club_all " +
        			"  WHERE cd_bk_operation_type = ?) ";
        	pParam.add(new bcFeautureParam("string", idBkOperationType));
        }
    	if (!isEmpty(pFind)) {
    		mySQL = mySQL + 
    				" AND (TO_CHAR(id_posting_detail) LIKE UPPER('%'||?||'%') OR " +
    				"  UPPER(debet_cd_bk_account) LIKE UPPER('%'||?||'%') OR " +
    				"  UPPER(credit_cd_bk_account) LIKE UPPER('%'||?||'%') OR " +
    				"  UPPER(oper_number) LIKE UPPER('%'||?||'%') OR " +
    				"  UPPER(assignment_posting) LIKE UPPER('%'||?||'%') OR " +
    				"  UPPER(operation_date_frmt) LIKE UPPER('%'||?||'%') OR " +
    				"  UPPER(entered_amount_frmt) LIKE UPPER('%'||?||'%'))";
    		for (int i=0; i<7; i++) {
    		    pParam.add(new bcFeautureParam("string", pFind));
    		}
    	}
    	pParam.add(new bcFeautureParam("int", p_end));
    	pParam.add(new bcFeautureParam("int", p_beg));
    	mySQL = mySQL +
        	"                                    )" +
        	"                  ORDER BY id_trans, debet_cd_bk_account, credit_cd_bk_account " +
        	"                ) " +
        	"         WHERE ROWNUM < ? " + 
            "  ) WHERE rn >= ?";
        
        boolean hasTransactionPermission = false;
        boolean hasPostingPermission = false;
        boolean hasAccDocPermission = false;
        boolean hasBKAccountPermission = false;
        boolean hasSchemePermission = false;
        
        try{
        	if (isEditMenuPermited("CARDS_TRANSACTIONS")>=0) {
        		hasTransactionPermission = true;
        	}
        	if (isEditMenuPermited("FINANCE_POSTINGS")>=0) {
        		hasPostingPermission = true;
        	}
        	if (isEditMenuPermited("FINANCE_ACCOUNTING_DOC")>=0) {
        		hasAccDocPermission = true;
        	}
        	if (isEditMenuPermited("FINANCE_BK_ACCOUNTS")>=0) {
        		hasBKAccountPermission = true;
        	}
        	if (isEditMenuPermited("FINANCE_POSTING_SCHEME")>=0) {
        		hasSchemePermission = true;
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
            for (int i=1; i <= colCount-8; i++) {
                html.append(getBottomFrameTableTH(postingXML, mtd.getColumnName(i)));
            }
            html.append("</tr></thead><tbody>");
            while (rset.next())
            {
                html.append("<tr>\n");
                for (int i=1; i <= colCount-8; i++) {
                	if ("ID_TRANS".equalsIgnoreCase(mtd.getColumnName(i))) {
                		if (hasTransactionPermission && (!(rset.getString(i) == null || "".equalsIgnoreCase(rset.getString(i))))) {
                			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/clients/transactionspecs.jsp?id="+rset.getString(i), "", ""));
                		} else {
                			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", "", ""));
                		}
                	} else if ("ID_POSTING_DETAIL".equalsIgnoreCase(mtd.getColumnName(i))) {
                		if (hasPostingPermission && (!(rset.getString(i) == null || "".equalsIgnoreCase(rset.getString(i))))) {
                			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/finance/postingspecs.jsp?id="+rset.getString(i), "", ""));
                		} else {
                			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", "", ""));
                		}
                	} else if ("ID_POSTING_LINE".equalsIgnoreCase(mtd.getColumnName(i))) {
                		if (hasAccDocPermission && (!(rset.getString(i) == null || "".equalsIgnoreCase(rset.getString(i))))) {
                			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/finance/accounting_docspecs.jsp?id="+rset.getString(i), "", ""));
                		} else {
                			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", "", ""));
                		}
                	} else if ("ID_BK_OPERATION_SCHEME_LINE".equalsIgnoreCase(mtd.getColumnName(i))) {
                		if (hasSchemePermission && (!(rset.getString(i) == null || "".equalsIgnoreCase(rset.getString(i))))) {
                			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/finance/posting_scheme_linespecs.jsp?id="+rset.getString("ID_BK_OPERATION_SCHEME_LINE2"), "", ""));
                		} else {
                			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", "", ""));
                		}
                	} else if ("DEBET_ID_BK_ACCOUNT".equalsIgnoreCase(mtd.getColumnName(i))) {
                		if (hasBKAccountPermission && (!(rset.getString(i) == null || "".equalsIgnoreCase(rset.getString(i))))) {
                			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString("DEBET_CD_BK_ACCOUNT"), "../crm/finance/postingspecs.jsp?id="+rset.getString("DEBET_ID_BK_ACCOUNT2"), "", ""));
                		} else {
                			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", "", ""));
                		}
                	} else if ("CREDIT_ID_BK_ACCOUNT".equalsIgnoreCase(mtd.getColumnName(i))) {
                		if (hasBKAccountPermission && (!(rset.getString(i) == null || "".equalsIgnoreCase(rset.getString(i))))) {
                			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString("CREDIT_CD_BK_ACCOUNT"), "../crm/finance/postingspecs.jsp?id="+rset.getString("CREDIT_ID_BK_ACCOUNT2"), "", ""));
                		} else {
                			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", "", ""));
                		}
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
    } //getTermSesPostingsHTML
}
