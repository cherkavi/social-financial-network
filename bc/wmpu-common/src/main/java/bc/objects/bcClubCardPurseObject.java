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

public class bcClubCardPurseObject extends bcObject {
	private final static Logger LOGGER=Logger.getLogger(bcClubCardPurseObject.class);
	private Map<String,Comparable<String>> fieldHm = new HashMap<String, Comparable<String>>();
	
	private String idClubCardPurse;
	
	public bcClubCardPurseObject(String pIdClubCardPurse){
		this.idClubCardPurse = pIdClubCardPurse;
		getFeature();
	}
	
	public bcClubCardPurseObject(String pCardSerialNumber, String pIdIssuer, String pIdPaymentSystem, String pPurseType){

		StringBuilder html = new StringBuilder();
		PreparedStatement st = null;
        Connection con = null;
        String mySQL = 
        	" SELECT * " +
        	"   FROM " + getGeneralDBScheme() + ".VC_CARD_PURSE_ALL " + 
        	"  WHERE card_serial_number = ? " +
        	"    AND id_issuer = ? " + 
        	"    AND id_payment_system = ? " + 
        	"    AND cd_card_purse_type = ? ";
        
		try {			
        	con = Connector.getConnection(getSessionId());
        	st = con.prepareStatement(mySQL);
        	st.setString(1, pCardSerialNumber);
        	st.setInt(2, Integer.parseInt(pIdIssuer));
        	st.setInt(3, Integer.parseInt(pIdPaymentSystem));
        	st.setString(4, pPurseType);
        	ResultSet rset_profile = st.executeQuery();
        	while (rset_profile.next()) {
        		if (!("".equalsIgnoreCase(rset_profile.getString(1).trim()))) {
        			this.idClubCardPurse = rset_profile.getString(1);
        		}
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

		getFeature();
	}
	
	private void getFeature() {
		String featureSelect = " SELECT * FROM " + getGeneralDBScheme() + ".VC_CARD_PURSE_ALL WHERE id_card_purse = ?";
		fieldHm = getFeatures2(featureSelect, this.idClubCardPurse, false);
	}
	
	public String getValue(String pColumnName) {
		return getFeautureResult(fieldHm, pColumnName);
	}   
    
    public String getClubCardsTasksHTML(String pFindString, String pOperType, String pOperState, String p_beg, String p_end) {
    	bcListCardOperation list = new bcListCardOperation();
    	
    	String pWhereCause = "   WHERE id_card_purse = ? ";
    	
    	ArrayList<bcFeautureParam> pWhereValue = new ArrayList<bcFeautureParam>();
    	pWhereValue.add(new bcFeautureParam("int", this.idClubCardPurse));

    	String lDeleteLink = "";
    	String lEditLink = "";
 	 	if (isEditPermited("CARDS_CLUBCARDS_TASKS") >0) {
 	 		lDeleteLink = "../crm/cards/clubcardpurseupdate.jsp?id="+this.idClubCardPurse+"&type=tasks";
 	 		lEditLink = "../crm/cards/clubcardpurseupdate.jsp?id="+this.idClubCardPurse+"&type=tasks";
 	 	}
    	
    	return list.getCardOperationsHTML(pWhereCause, pWhereValue, pFindString, pOperType, pOperState, lEditLink, lDeleteLink, "div_data_detail", p_beg, p_end);
    	
    }
	
    public String getFinanceOperationsHTML(String pState, String pFind, String p_beg, String p_end) {
        StringBuilder html = new StringBuilder();
        Connection con = null;
        PreparedStatement st = null;

        ArrayList<bcFeautureParam> pParam = initParamArray();

        String mySQL = 
        	"SELECT rn, id_fn_card_oper, date_fn_card_oper_frmt, payment_order_fn_card_oper, " +
        	"       name_fn_oper_type, name_fn_card_oper_state, value_fn_card_oper_frmt " +
        	"  FROM (SELECT ROWNUM rn, id_fn_card_oper, date_fn_card_oper_frmt, payment_order_fn_card_oper, " +
        	"               name_fn_oper_type, name_fn_card_oper_state, value_fn_card_oper_frmt " +
        	"		   FROM (SELECT id_fn_card_oper, date_fn_card_oper_frmt, payment_order_fn_card_oper, " +
        	"                       name_fn_oper_type, " +
        	"                       DECODE(cd_fn_card_oper_state, " +
    		"                              'ACCEPT', '<font color=\"blue\"><b>'||name_fn_card_oper_state||'</b></font>', " +
    		"                              'CONFIRM', '<font color=\"green\">'||name_fn_card_oper_state||'</font>', " +
    		"                              'REJECT', '<font color=\"red\">'||name_fn_card_oper_state||'</font>', " +
    		"                              'ERROR', '<font color=\"red\"><b>'||name_fn_card_oper_state||'</b></font>', " +
    		"                              name_fn_card_oper_state" +
    		"                       ) name_fn_card_oper_state, " +
        	"                       value_fn_card_oper_frmt " +
            "                   FROM " + getGeneralDBScheme() + ".vc_fn_card_oper_club_all "+
            "                  WHERE id_card_purse = ? ";
        pParam.add(new bcFeautureParam("int", this.idClubCardPurse));
        
        if (!isEmpty(pState)) {
        	mySQL = mySQL + " AND cd_fn_card_oper_state = ? ";
        	pParam.add(new bcFeautureParam("string", pState));
        }
        if (!isEmpty(pFind)) {
        	mySQL = mySQL + 
        		" AND (TO_CHAR(id_fn_card_oper) LIKE UPPER('%'||?||'%') OR " +
        		"      UPPER(date_fn_card_oper_frmt) LIKE UPPER('%'||?||'%') OR " +
        		"      UPPER(payment_order_fn_card_oper) LIKE UPPER('%'||?||'%') OR " +
        		"      UPPER(name_fn_oper_type) LIKE UPPER('%'||?||'%') OR " +
        		"      UPPER(value_fn_card_oper_frmt) LIKE UPPER('%'||?||'%')) ";
        	for (int i=0; i<5; i++) {
        	    pParam.add(new bcFeautureParam("string", pFind));
        	}
        }
        pParam.add(new bcFeautureParam("int", p_end));
        pParam.add(new bcFeautureParam("int", p_beg));
	    mySQL = mySQL + 
	        "                 ORDER BY date_fn_card_oper DESC, id_fn_card_oper) " +
            "          WHERE ROWNUM < ? " + 
            " ) WHERE rn >= ?";
        
        //boolean hasEditPermission = false;
        
        try{
        	/*if (isEditPermited("CARDS_CLUBCARDS_PURSES")>0) {
        		hasEditPermission = true;
        	}*/
        	
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
                html.append(getBottomFrameTableTH(clubcardXML, mtd.getColumnName(i)));
            }
            /*if (hasEditPermission) {
            	html.append("<th>&nbsp;</th>\n");
            }*/
            html.append("</tr></thead><tbody>");
            while (rset.next())
            {
            	html.append("<tr>");
          	  	for (int i=1; i <= colCount; i++) {
          	  		html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", "", ""));
          	  	}
	            /*if (hasEditPermission) {
	            	String myHyperLink = "../crm/cards/clubcardpursespecs.jsp?id="+rset.getString("ID_CARD_PURSE");
	            	html.append(getEditButtonHTML(myHyperLink, getLanguage()));
	            } else {
	            	html.append("<td align=center>&nbsp;</td>\n");
	            }*/
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


    public String getRestsHTML(String pBeginPeriod, String pEndPeriod, String pFind, String p_beg, String p_end) {
        StringBuilder html = new StringBuilder();
        Connection con = null;
        PreparedStatement st = null; 

        ArrayList<bcFeautureParam> pParam = initParamArray();
        
        String mySQL = 
	        	"SELECT rn, date_from_frmt, sname_currency, " +
	            "       value_purse_balance_beg_frmt, value_purse_turnover_frmt, value_purse_balance_end_frmt " +
	            "  FROM (SELECT ROWNUM rn, date_from_frmt, sname_currency, " +
	            "               value_purse_balance_beg_frmt, value_purse_turnover_frmt, value_purse_balance_end_frmt" +
	            "          FROM (SELECT date_from_frmt, sname_currency, " +
	            "                       value_purse_balance_beg_frmt, " +
	            "                       CASE WHEN value_purse_turnover > 0 THEN '<font color=\"green\"><b>'||value_purse_turnover_frmt||'</b></font>' " +
	            "                            WHEN value_purse_turnover < 0 THEN '<font color=\"blue\"><b>'||value_purse_turnover_frmt||'</b></font>' " +
	    		"                            ELSE value_purse_turnover_frmt " +
	    		"                       END value_purse_turnover_frmt, " +
	            "						value_purse_balance_end_frmt " +
	            "                  FROM " + getGeneralDBScheme() + ".vc_card_purse_rests_all " +
	            "                 WHERE id_card_purse = ? ";
        pParam.add(new bcFeautureParam("int", this.idClubCardPurse));
        
        if (!isEmpty(pBeginPeriod)) {
        	mySQL = mySQL + " and date_from >= TO_DATE(?,?) ";
        	pParam.add(new bcFeautureParam("string", pBeginPeriod));
        	pParam.add(new bcFeautureParam("string", this.getDateFormat()));
        }
        if (!isEmpty(pEndPeriod)) {
        	mySQL = mySQL + " and date_from <= TO_DATE(?,?) ";
        	pParam.add(new bcFeautureParam("string", pEndPeriod));
        	pParam.add(new bcFeautureParam("string", this.getDateFormat()));
        }
        if (!isEmpty(pFind)) {
        	mySQL = mySQL + 
				" AND (UPPER(date_from_frmt) LIKE UPPER('%'||?||'%') OR " +
				"      UPPER(value_purse_balance_beg_frmt) LIKE UPPER('%'||?||'%') OR " +
				"      UPPER(value_purse_turnover_frmt) LIKE UPPER('%'||?||'%') OR " +
				"      UPPER(value_purse_balance_end_frmt) LIKE UPPER('%'||?||'%')) ";
        	for (int i=0; i<4; i++) {
        	    pParam.add(new bcFeautureParam("string", pFind));
        	}
		}
        pParam.add(new bcFeautureParam("int", p_end));
        pParam.add(new bcFeautureParam("int", p_beg));
        mySQL = mySQL + " ORDER BY TRUNC(date_from) DESC " +
        	"    ) WHERE ROWNUM < ? " + 
        	" ) WHERE rn >= ?";
       
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
            html.append(getBottomFrameTable());
            html.append("<tr>");
            html.append("<th rowspan=\"2\">"+ commonXML.getfieldTransl("RN", false)+"</th>\n");
            html.append("<th rowspan=\"2\">"+ clubcardXML.getfieldTransl("DATE_TURNOVER", false)+"</th>\n");
            html.append("<th rowspan=\"2\">"+ clubcardXML.getfieldTransl("SNAME_CURRENCY", false)+"</th>\n");
            html.append("<th colspan=\"3\">"+ clubcardXML.getfieldTransl("PURSE_BALANCE", false)+"</th>\n");
            html.append("</tr>");
            html.append("<tr>");
            html.append("<th>"+ clubcardXML.getfieldTransl("BEGIN_BALANCE", false)+"</th>\n");
            html.append("<th>"+ clubcardXML.getfieldTransl("BON_TURNOVER", false)+"</th>\n");
            html.append("<th>"+ clubcardXML.getfieldTransl("END_BALANCE", false)+"</th>\n");
            html.append("</tr></thead><tbody>");
            html.append("<tr>");
            html.append("</tr></thead><tbody>\n");
            
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
       }
    
}
