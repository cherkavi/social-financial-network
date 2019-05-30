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


public class bcBKAccountObject extends bcObject {
	private final static Logger LOGGER=Logger.getLogger(bcBKAccountObject.class);
	private Map<String,Comparable<String>> fieldHm = new HashMap<String, Comparable<String>>();
	
	private String idBKAccount;
	
	public bcBKAccountObject(String pIdBKAccount) {
		this.idBKAccount = pIdBKAccount;
		getFeature();
	}
	
	private void getFeature() {
		String featureSelect = " SELECT * FROM " + getGeneralDBScheme() + ".VC_BK_ACCOUNTS_CLUB_ALL WHERE id_bk_account = ?";
		fieldHm = getFeatures2(featureSelect, this.idBKAccount, false);
	}
	
	public String getValue(String pColumnName) {
		return getFeautureResult(fieldHm, pColumnName);
	}

    public String getRestsHTML(String pBeginPeriod, String pEndPeriod, String pFind, String p_beg, String p_end) {
    	StringBuilder html = new StringBuilder();
        Connection con = null;
        PreparedStatement st = null; 
        ArrayList<bcFeautureParam> pParam = initParamArray();
        String mySQL = 
        		"SELECT rn, accounting_date_frmt, name_currency, begin_balance_frmt,  " +
	            "       debet_amount_frmt, credit_amount_frmt, end_balance_frmt " +
	            "  FROM (SELECT ROWNUM rn, accounting_date_frmt, name_currency, begin_balance_frmt,  " +
	            "               debet_amount_frmt, credit_amount_frmt, end_balance_frmt " +
	            "          FROM (SELECT accounting_date_frmt, name_currency, " +
	            "                       CASE WHEN begin_balance > 0 THEN '<font color=\"green\"><b>'||begin_balance_frmt||'</b></font>' " +
	            "                            WHEN begin_balance < 0 THEN '<font color=\"blue\"><b>'||begin_balance_frmt||'</b></font>' " +
	    		"                            ELSE begin_balance_frmt " +
	    		"                       END begin_balance_frmt, " +
	            "                       CASE WHEN debet_amount > 0 THEN '<font color=\"green\"><b>'||debet_amount_frmt||'</b></font>' " +
	            "                            WHEN debet_amount < 0 THEN '<font color=\"blue\"><b>'||debet_amount_frmt||'</b></font>' " +
	    		"                            ELSE debet_amount_frmt " +
	    		"                       END debet_amount_frmt, " +
	    		"                       CASE WHEN credit_amount > 0 THEN '<font color=\"green\"><b>'||credit_amount_frmt||'</b></font>' " +
	            "                            WHEN credit_amount < 0 THEN '<font color=\"blue\"><b>'||credit_amount_frmt||'</b></font>' " +
	    		"                            ELSE credit_amount_frmt " +
	    		"                       END credit_amount_frmt, " +
	    		"                       CASE WHEN end_balance > 0 THEN '<font color=\"green\"><b>'||end_balance_frmt||'</b></font>' " +
	            "                            WHEN end_balance < 0 THEN '<font color=\"blue\"><b>'||end_balance_frmt||'</b></font>' " +
	    		"                            ELSE end_balance_frmt " +
	    		"                       END end_balance_frmt " +
	            "                  FROM " + getGeneralDBScheme() + ".v_bk_account_rests_club_all " +
	            "                 WHERE id_bk_account = ? ";
        pParam.add(new bcFeautureParam("string", this.idBKAccount));
        
        
        if (!isEmpty(pBeginPeriod)) {
        	mySQL = mySQL + " and accounting_date >= TO_DATE(?,?) ";
        	pParam.add(new bcFeautureParam("string", pBeginPeriod));
        	pParam.add(new bcFeautureParam("string", this.getDateFormat()));
        }
        if (!isEmpty(pEndPeriod)) {
        	mySQL = mySQL + " and accounting_date <= TO_DATE(?,?) ";
        	pParam.add(new bcFeautureParam("string", pEndPeriod));
        	pParam.add(new bcFeautureParam("string", this.getDateFormat()));
        }
        if (!isEmpty(pFind)) {
        	mySQL = mySQL + 
				" AND (UPPER(accounting_date_frmt) LIKE UPPER('%'||?||'%') OR " +
				"      UPPER(begin_balance_frmt) LIKE UPPER('%'||?||'%') OR " +
				"      UPPER(debet_amount_frmt) LIKE UPPER('%'||?||'%') OR " +
				"      UPPER(credit_amount_frmt) LIKE UPPER('%'||?||'%') OR " +
				"      UPPER(end_balance_frmt) LIKE UPPER('%'||?||'%')) ";
        	for (int i=0; i<5; i++) {
        	    pParam.add(new bcFeautureParam("string", pFind));
        	}
		}
        pParam.add(new bcFeautureParam("int", p_end));
        pParam.add(new bcFeautureParam("int", p_beg));
        mySQL = mySQL + " ORDER BY TRUNC(accounting_date) DESC" +
        	"    ) WHERE ROWNUM < ?" + 
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
            html.append("<th rowspan=\"2\">"+ bk_accountXML.getfieldTransl("ACCOUNTING_DATE", false)+"</th>\n");
            html.append("<th rowspan=\"2\">"+ bk_accountXML.getfieldTransl("NAME_CURRENCY", false)+"</th>\n");
            html.append("<th rowspan=\"2\">"+ bk_accountXML.getfieldTransl("BEGIN_BALANCE", false)+"</th>\n");
            html.append("<th colspan=\"2\">"+ bk_accountXML.getfieldTransl("RESTS", false)+"</th>\n");
            html.append("<th rowspan=\"2\">"+ bk_accountXML.getfieldTransl("END_BALANCE", false)+"</th>\n");
            html.append("</tr>");
            html.append("<tr>");
            html.append("<th>"+ bk_accountXML.getfieldTransl("DEBET_AMOUNT", false)+"</th>\n");
            html.append("<th>"+ bk_accountXML.getfieldTransl("CREDIT_AMOUNT", false)+"</th>\n");
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
                if (st!=null) {
					st.close();
				}
            } catch (SQLException w) {w.toString();}
            try {
                if (con!=null) {
					con.close();
				}
            } catch (SQLException w) {w.toString();}
            Connector.closeConnection(con);
        } // finally
        return html.toString();
       }

    public String getBankAccountsHTML(String pFind, String p_beg, String p_end) {
    	StringBuilder html = new StringBuilder();
        Connection con = null;
        PreparedStatement st = null; 
        
        boolean hasBankAccountPermission = false;
        
        ArrayList<bcFeautureParam> pParam = initParamArray();
        
        String mySQL = 
	       	"SELECT rn, id_bank_account, number_bank_account, name_bank_account_type " +
	        "  FROM (SELECT ROWNUM rn, id_bank_account, number_bank_account, name_bank_account_type " +
	        "          FROM (SELECT id_bank_account, number_bank_account, name_bank_account_type " +
	        "                  FROM " + getGeneralDBScheme() + ".vc_bk_account_bank_account_all " +
	        "                 WHERE id_bk_account = ? ";
        pParam.add(new bcFeautureParam("string", this.idBKAccount));
        
	    if (!isEmpty(pFind)) {
	    	mySQL = mySQL + 
				" AND (TO_CHAR(id_bank_account) LIKE UPPER('%'||?||'%') OR " +
				"	   UPPER(number_bank_account) LIKE UPPER('%'||?||'%')) ";
	    	for (int i=0; i<2; i++) {
	    	    pParam.add(new bcFeautureParam("string", pFind));
	    	}
	    }
	    pParam.add(new bcFeautureParam("int", p_end));
	    pParam.add(new bcFeautureParam("int", p_beg));
	    mySQL = mySQL +
	        "                 ORDER BY number_bank_account " +
        	"    ) WHERE ROWNUM < ? " + 
        	" ) WHERE rn >= ?";
       
        try{
        	if (isEditMenuPermited("CLIENTS_BANK_ACCOUNTS")>=0) {
        		hasBankAccountPermission = true;
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
                html.append(getBottomFrameTableTH(accountXML, mtd.getColumnName(i)));
             }
            html.append("<tr>");
            html.append("</tr></thead><tbody>\n");
            
            while (rset.next())
                {
        		html.append("<tr>");
            	for (int i=1; i <= colCount; i++) {
            		if (hasBankAccountPermission && 
            				("ID_BANK_ACCOUNT".equalsIgnoreCase(mtd.getColumnName(i)) ||
            						"NUMBER_BANK_ACCOUNT".equalsIgnoreCase(mtd.getColumnName(i)))) {
	                	html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/clients/accountspecs.jsp?id="+rset.getString("ID_BANK_ACCOUNT"), "", ""));
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
                if (st!=null) {
					st.close();
				}
            } catch (SQLException w) {w.toString();}
            try {
                if (con!=null) {
					con.close();
				}
            } catch (SQLException w) {w.toString();}
            Connector.closeConnection(con);
        } // finally
        return html.toString();
       }
	
}
