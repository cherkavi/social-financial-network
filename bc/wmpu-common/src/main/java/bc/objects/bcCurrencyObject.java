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


public class bcCurrencyObject extends bcObject {
	private final static Logger LOGGER=Logger.getLogger(bcCurrencyObject.class);
	private Map<String,Comparable<String>> fieldHm = new HashMap<String, Comparable<String>>();
	
	private String cdCurrency;
	
	public bcCurrencyObject() {
	}
	
	public bcCurrencyObject(String pCdCurrency) {
		this.cdCurrency = pCdCurrency;
		getFeature();
	}
	
	private void getFeature() {
		String featureSelect = " SELECT * FROM " + getGeneralDBScheme() + ".VC_CURRENCY_ALL WHERE cd_currency = ?";
		fieldHm = getFeatures2(featureSelect, this.cdCurrency, false);
	}
	
	public void getFeature(String pCdCurrency) {
		String featureSelect = " SELECT * FROM " + getGeneralDBScheme() + ".VC_CURRENCY_ALL WHERE cd_currency = ?";
		fieldHm = getFeatures2(featureSelect, pCdCurrency, false);
	}
	
	public String getValue(String pColumnName) {
		return getFeautureResult(fieldHm, pColumnName);
	}

    public String getCurrencyExangeRatesHTML(String pFindString, String p_beg, String p_end) {
        StringBuilder html = new StringBuilder();
        PreparedStatement st = null;
        Connection con = null;
        boolean hasEditPermission = false;

        ArrayList<bcFeautureParam> pParam = initParamArray();

        String mySQL = 
        	" SELECT to_currency, to_currency_name, conversion_date_frmt, " +
            "        conversion_type, conversion_rate, src_data_tsl, id_daily_rate " +
            "   FROM (SELECT ROWNUM rn, to_currency, to_currency_name, conversion_date_frmt, " +
            "                conversion_type, conversion_rate_frmt conversion_rate, " +
            "                src_data_tsl, id_daily_rate " +
            "           FROM (SELECT * " +
            "                   FROM " + getGeneralDBScheme() + ".vc_daily_rates_all "+
            "                  WHERE TRIM(from_currency) like '%'||?||'%'";
        pParam.add(new bcFeautureParam("int", this.cdCurrency));

        if (!isEmpty(pFindString)) {
    		mySQL = mySQL + 
   				" AND (TO_CHAR(to_currency) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(to_currency_name) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(conversion_date_frmt) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(conversion_type) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(conversion_rate) LIKE UPPER('%'||?||'%'))";
    		for (int i=0; i<5; i++) {
    		    pParam.add(new bcFeautureParam("string", pFindString));
    		}
    	}
        pParam.add(new bcFeautureParam("int", p_end));
        pParam.add(new bcFeautureParam("int", p_beg));
        mySQL = mySQL +
            "                  ORDER BY to_currency, conversion_date) " +
            "          WHERE ROWNUM < ? " + 
            " ) WHERE rn >= ?";
        try{
        	if (isEditPermited("SETUP_CURRENCY_EXCHANGE") >0) {
        		hasEditPermission = true;
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
            	html.append(getBottomFrameTableTH(daily_rateXML, mtd.getColumnName(i)));
            }
            if (hasEditPermission) {
            	html.append("<th>&nbsp;</th><th>&nbsp;</th>\n");
            }
            html.append("</tr></thead><tbody>\n");
            while (rset.next())
            {
            	html.append("<tr>");
          	  	for (int i=1; i <= colCount-1; i++) {
          	  		html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", "", ""));
         	  	}
          	  	if (hasEditPermission) {
            	   String myHyperLink = "../crm/setup/currencyupdate.jsp?type=rate&id="+this.cdCurrency+"&id_daily_rate="+rset.getString("ID_DAILY_RATE");
            	   String myDeleteLink = "../crm/setup/currencyupdate.jsp?type=rate&id="+this.cdCurrency+"&id_daily_rate="+rset.getString("ID_DAILY_RATE")+"&action=remove&process=yes";
                   html.append(getDeleteButtonHTML(myDeleteLink, daily_rateXML.getfieldTransl("h_rate_remove", false), rset.getString("TO_CURRENCY")));
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
    } // getCurrencyExangeRatesHTML

}
