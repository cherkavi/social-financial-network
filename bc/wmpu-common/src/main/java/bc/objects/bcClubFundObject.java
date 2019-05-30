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
import bc.service.bcFeautureParam;

public class bcClubFundObject extends bcObject {
	private final static Logger LOGGER=Logger.getLogger(bcClubFundObject.class);
	
	private String idClubFund;
	
	private Map<String,Comparable<String>> fieldHm = new HashMap<String, Comparable<String>>();
	
	public bcClubFundObject(String pIdClubFund){
		this.idClubFund = pIdClubFund;
		getFeature();
	}
	
	private void getFeature() {
		String mySQL = " SELECT * FROM " + getGeneralDBScheme() + ".vc_club_fund_club_all WHERE id_club_fund = ?";
		fieldHm = getFeatures2(mySQL, this.idClubFund, false);
	}
	
	public String getValue(String pColumnName) {
		return getFeautureResult(fieldHm, pColumnName);
	}


    public String getRestsHTML(String pBeginPeriod, String pEndPeriod, String pFind, String pPaymentKind, String p_beg, String p_end) {
        StringBuilder html = new StringBuilder();
        Connection con = null;
        PreparedStatement st = null; 
        
        String paymentKindName = "";
        if (!isEmpty(pPaymentKind)) {
        	paymentKindName = " '" + getClubFundPaymentKindName(pPaymentKind) + "' name_club_fund_payment_kind, ";
        }

        ArrayList<bcFeautureParam> pParam = initParamArray();
        
        String mySQL = 
	        	"SELECT rn, "+paymentKindName+" date_from_frmt, /*sname_currency,*/ " +
	            "       value_fund_balance_beg_amnt, value_fund_income_amnt, " +
	            "       value_fund_expense_amnt, value_fund_turnover_amnt, value_fund_balance_end_amnt " +
	            "  FROM (SELECT ROWNUM rn, date_from_frmt, sname_currency, " +
	            "               value_fund_balance_beg_amnt, value_fund_income_amnt, " +
	            "               value_fund_expense_amnt, value_fund_turnover_amnt, value_fund_balance_end_amnt" +
	            "          FROM (SELECT a.date_from_frmt, a.sname_currency, " +
	            "                       CASE WHEN a.value_fund_balance_beg > 0 THEN '<font color=\"green\"><b>'||a.value_fund_balance_beg_frmt||'</b></font>' " +
	            "                            WHEN a.value_fund_balance_beg < 0 THEN '<font color=\"blue\"><b>'||a.value_fund_balance_beg_frmt||'</b></font>' " +
	    		"                            ELSE a.value_fund_balance_beg_frmt " +
	    		"                       END||' '||a.sname_currency value_fund_balance_beg_amnt, " +
	    		"                       CASE WHEN NVL(a.value_fund_income,0) <> 0 THEN '<font color=\"green\"><b>'||a.value_fund_income_frmt||'</b></font>' " +
	            "                            ELSE a.value_fund_income_frmt " +
	            "                       END||' '||a.sname_currency value_fund_income_amnt, " +
	            "                       CASE WHEN NVL(a.value_fund_expense,0) <> 0 THEN '<font color=\"blue\"><b>'||a.value_fund_expense_frmt||'</b></font>' " +
	            "                            ELSE a.value_fund_expense_frmt " +
	            "                       END||' '||a.sname_currency value_fund_expense_amnt, " +
	            "                       CASE WHEN a.value_fund_turnover > 0 THEN '<font color=\"green\"><b>'||a.value_fund_turnover_frmt||'</b></font>' " +
	            "                            WHEN a.value_fund_turnover < 0 THEN '<font color=\"blue\"><b>'||a.value_fund_turnover_frmt||'</b></font>' " +
	    		"                            ELSE a.value_fund_turnover_frmt " +
	    		"                       END||' '||a.sname_currency value_fund_turnover_amnt, " +
	    		"                       CASE WHEN a.value_fund_balance_end > 0 THEN '<font color=\"green\"><b>'||a.value_fund_balance_end_frmt||'</b></font>' " +
	            "                            WHEN a.value_fund_balance_end < 0 THEN '<font color=\"blue\"><b>'||a.value_fund_balance_end_frmt||'</b></font>' " +
	    		"                            ELSE a.value_fund_balance_end_frmt " +
	    		"                       END||' '||a.sname_currency value_fund_balance_end_amnt " +
	            "                  FROM " + getGeneralDBScheme() + ".vc_fund_rest_all a ";
	    mySQL = mySQL + 
	            "                 WHERE id_club_fund = ? ";
        pParam.add(new bcFeautureParam("int", this.idClubFund));
        
        
        if (!isEmpty(pPaymentKind)) {
        	mySQL = mySQL + " AND cd_club_fund_payment_kind = ? ";
        	pParam.add(new bcFeautureParam("string", pPaymentKind));
        } else {
        	mySQL = mySQL + " AND cd_club_fund_payment_kind = 'TOTAL' ";
        }
        
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
				"      UPPER(value_fund_balance_beg_frmt) LIKE UPPER('%'||?||'%') OR " +
				"      UPPER(value_fund_turnover_frmt) LIKE UPPER('%'||?||'%') OR " +
				"      UPPER(value_fund_balance_end_frmt) LIKE UPPER('%'||?||'%')) ";
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
            if (!isEmpty(pPaymentKind)) {
                html.append("<th rowspan=\"2\">"+ clubfundXML.getfieldTransl("NAME_CLUB_FUND_PAYMENT_KIND", false)+"</th>\n");
            }
            html.append("<th rowspan=\"2\">"+ clubfundXML.getfieldTransl("DATE_TURNOVER", false)+"</th>\n");
            //html.append("<th rowspan=\"2\">"+ clubcardXML.getfieldTransl("SNAME_CURRENCY", false)+"</th>\n");
            html.append("<th rowspan=\"2\">"+ clubfundXML.getfieldTransl("BEGIN_BALANCE", false)+"</th>\n");
            html.append("<th colspan=\"3\">"+ clubfundXML.getfieldTransl("FUND_BALANCE", false)+"</th>\n");
            html.append("<th rowspan=\"2\">"+ clubfundXML.getfieldTransl("END_BALANCE", false)+"</th>\n");
            html.append("</tr>");
            html.append("<tr>");
            html.append("<th>"+ clubfundXML.getfieldTransl("FUND_INCOME", false)+"</th>\n");
            html.append("<th>"+ clubfundXML.getfieldTransl("FUND_EXPENSE", false)+"</th>\n");
            html.append("<th>"+ clubfundXML.getfieldTransl("FUND_TURNOVER", false)+"</th>\n");
            html.append("</tr></thead><tbody>");
            html.append("<tr>");
            html.append("</tr></thead><tbody>\n");
            
            while (rset.next())
                {
                    html.append("<tr>");
              	  	for (int i=1; i <= colCount; i++) {
              	  		html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", "", ""));
              	  		/*if ("VALUE_FUND_BALANCE_BEG_AMNT".equalsIgnoreCase(mtd.getColumnName(i)) ||
              	  			"VALUE_FUND_TURNOVER_AMNT".equalsIgnoreCase(mtd.getColumnName(i)) ||
              	  			"VALUE_FUND_BALANCE_END_AMNT".equalsIgnoreCase(mtd.getColumnName(i))) {
              	  			html.append(getBottomFrameTableTDAlign(mtd.getColumnName(i), rset.getString(i), "", "", "", "right"));
              	  		} else {
              	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", "", ""));
              	  		}*/
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

    public String getTransferHTML(String pFind, String pPaymentKind, String pOperationType, String p_beg, String p_end) {
        StringBuilder html = new StringBuilder();
        PreparedStatement st = null;
        Connection con = null;

        ArrayList<bcFeautureParam> pParam = initParamArray();
        
        String mySQL = 
        	" SELECT rn, id_trans, name_club_fund_payment_kind, name_club_fund_oper_type, date_fund_oper_frmt, "+
        	"        amount_fund_oper_frmt amount_fund_oper_amnt, desc_fund_oper " +
        	"   FROM (SELECT ROWNUM rn, id_trans, name_club_fund_payment_kind, " +
        	"				 DECODE(cd_club_fund_oper_type, " +
  			"                      'PAYMENT', '<font color=\"green\">'||name_club_fund_oper_type||'</font>', " +
  			"                      'WRITE_OFF', '<font color=\"blue\">'||name_club_fund_oper_type||'</font>', " +
  			"                      name_club_fund_oper_type" +
			"                ) name_club_fund_oper_type, " +
        	"				 date_fund_oper_frmt,  " +
            "                DECODE(cd_club_fund_oper_type, " +
  			"                      'PAYMENT', '<font color=\"green\">'||amount_fund_oper_frmt||' '||sname_currency ||'</font>', " +
  			"                      'WRITE_OFF', '<font color=\"blue\">'||amount_fund_oper_frmt||' '||sname_currency ||'</font>', " +
  			"                      name_club_fund_oper_type" +
			"                ) amount_fund_oper_frmt, desc_fund_oper " +
        	" 		    FROM (SELECT * " +
        	"                   FROM " + getGeneralDBScheme() + ".vc_fund_oper_all "+
        	" 		           WHERE id_club_fund = ? ";
    	pParam.add(new bcFeautureParam("int", this.idClubFund));

    	if (!isEmpty(pPaymentKind)) {
    		mySQL = mySQL + " AND cd_club_fund_payment_kind = ? ";
    		pParam.add(new bcFeautureParam("string", pPaymentKind));
    	}
    	if (!isEmpty(pOperationType)) {
    		mySQL = mySQL + " AND cd_club_fund_oper_type = ? ";
    		pParam.add(new bcFeautureParam("string", pOperationType));
    	}
    	
        if (!isEmpty(pFind)) {
    		mySQL = mySQL + 
    				" AND (TO_CHAR(date_fund_oper_frmt) LIKE UPPER('%'||?||'%') OR " +
    				"  UPPER(amount_fund_oper_frmt) LIKE UPPER('%'||?||'%') OR " +
    				"  UPPER(desc_fund_oper) LIKE UPPER('%'||?||'%'))";
    		for (int i=0; i<3; i++) {
    		    pParam.add(new bcFeautureParam("string", pFind));
    		}
    	}
    	pParam.add(new bcFeautureParam("int", p_end));
    	pParam.add(new bcFeautureParam("int", p_beg));
        mySQL = mySQL + 
        	"                  ORDER BY date_fund_oper DESC, creation_date DESC) " +
        	"          WHERE ROWNUM < ?) " +
        	"  WHERE rn >= ?";
        
        boolean hasTransactionPermission = false;
        
        try{

        	if (isEditMenuPermited("CARDS_TRANSACTIONS")>=0) {
        		hasTransactionPermission = true;
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
                html.append(getBottomFrameTableTH(clubfundXML, mtd.getColumnName(i)));
            }
            html.append("</tr></thead><tbody>");
            
            while (rset.next())
                {
                    html.append("<tr>");
              	  	for (int i=1; i <= colCount; i++) {
	              	  	if (hasTransactionPermission && "ID_TRANS".equalsIgnoreCase(mtd.getColumnName(i)) &&
	              	  			!isEmpty(rset.getString("ID_TRANS"))) {
	              	  		html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/cards/transactionspecs.jsp?id=" + rset.getString("ID_TRANS"), "", ""));
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
    } // getCardRequestsHTML
    
}
