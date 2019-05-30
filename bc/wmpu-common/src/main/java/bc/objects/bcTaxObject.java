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

public class bcTaxObject extends bcObject {
	private final static Logger LOGGER=Logger.getLogger(bcTaxObject.class);
	private Map<String,Comparable<String>> fieldHm = new HashMap<String, Comparable<String>>();
	
	private String cdTax;
	
	public bcTaxObject(String pCdTax) {
		this.cdTax = pCdTax;
		getFeature();
	}
	
	private void getFeature() {
		String featureSelect = " SELECT * FROM " + getGeneralDBScheme() + ".VC_TAX_ALL WHERE cd_tax = ?";
		fieldHm = getFeatures2(featureSelect, this.cdTax, true);
	}
	
	public String getValue(String pColumnName) {
		return getFeautureResult(fieldHm, pColumnName);
	}

    public String getTaxValuesHTML(String pFindString, String p_beg, String p_end) {
    	ArrayList<bcFeautureParam> pParam = initParamArray();

    	String mySQL = 
        	" SELECT rn, begin_action_date_frmt, end_action_date_frmt, value_tax, name_tax_value_type, " +
        	"        name_currency, id_tax_value " +
            "   FROM (SELECT ROWNUM rn, value_tax, name_tax_value_type, " +
        	"                name_currency, begin_action_date_frmt, end_action_date_frmt, id_tax_value " +
            "           FROM (SELECT value_tax, name_tax_value_type, " +
        	"                        name_currency, begin_action_date_frmt, end_action_date_frmt, id_tax_value " +
        	"		            FROM " + getGeneralDBScheme() + ".vc_tax_values_club_all a " +
        	"                  WHERE a.cd_tax = ? ";
    	pParam.add(new bcFeautureParam("string", this.cdTax));
    	
	    if (!isEmpty(pFindString)) {
	    	mySQL = mySQL + 
					" AND (TO_CHAR(value_tax) LIKE UPPER('%'||?||'%') OR " +
					"	   UPPER(name_tax_value_type) LIKE UPPER('%'||?||'%') OR " +
					"	   UPPER(begin_action_date_frmt) LIKE UPPER('%'||?||'%') OR " +
					"	   UPPER(end_action_date_frmt) LIKE UPPER('%'||?||'%')) ";
	    	for (int i=0; i<4; i++) {
	    		pParam.add(new bcFeautureParam("string", pFindString));
	    	}
	    }
	    pParam.add(new bcFeautureParam("int", p_end));
	    pParam.add(new bcFeautureParam("int", p_beg));
	    mySQL = mySQL +
        	"		           ORDER BY begin_action_date) " +
        	"           WHERE ROWNUM < ? " + 
            " ) WHERE rn >= ?";
    	
        StringBuilder html = new StringBuilder();
        PreparedStatement st = null;
        Connection con = null;
        
        boolean hasEditPermission = false;
        
        try{
        	if (isEditPermited("FINANCE_TAX_INFO")>0) {
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
            	html.append(getBottomFrameTableTH(taxXML, mtd.getColumnName(i)));
            }
            if (hasEditPermission) {
            	html.append("<th>&nbsp;</th><th>&nbsp;</th>\n");
            }
            html.append("</tr></thead><tbody>\n");
            while (rset.next())
            {
                html.append("<tr>\n");
                for (int i=1; i<=colCount-1; i++) {
          	  		html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", "", ""));
                }
                if (hasEditPermission) {
                  	String myHyperLink = "../crm/finance/taxupdate.jsp?id=" + this.cdTax + "&id_value=" + rset.getString("ID_TAX_VALUE") + "&type=value";
                  	String myDeleteLink = "../crm/finance/taxupdate.jsp?id=" + this.cdTax + "&id_value=" + rset.getString("ID_TAX_VALUE") + "&type=value&action=remove&process=yes";
                	html.append(getDeleteButtonHTML(myDeleteLink, taxXML.getfieldTransl("l_remove_tax_value", false)));
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
    }

}
