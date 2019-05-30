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

public class bcLGPromoterWorkHistoryObject extends bcObject {
	private final static Logger LOGGER=Logger.getLogger(bcLGPromoterWorkHistoryObject.class);
	
	private Map<String,Comparable<String>> fieldHm = new HashMap<String, Comparable<String>>();
	
	private String idLGPromoterPayment;
	
	public bcLGPromoterWorkHistoryObject(String pIdLGPromoterPayment) {
		this.idLGPromoterPayment = pIdLGPromoterPayment;
		getFeature();
	}
	
	private void getFeature() {
		String mySQL = " SELECT * FROM " + getGeneralDBScheme() + ".VC_LG_PROMOTER_PAYMENT_ALL WHERE id_lg_promoter_payment = ?";
		fieldHm = getFeatures2(mySQL, this.idLGPromoterPayment, false);
	}
	
	public String getValue(String pColumnName) {
		return getFeautureResult(fieldHm, pColumnName);
	}
	
    public String getPaymentLinesHTML(String pFindString, String pLineState, String p_beg, String p_end) {
        StringBuilder html = new StringBuilder();
        Connection con = null;
        PreparedStatement st = null;

        ArrayList<bcFeautureParam> pParam = initParamArray();

        String mySQL = 
        	"SELECT rn, name_lg_promoter, date_begin_sale_frmt, date_end_sale_frmt, " +
        	"       sales_cards_count, amount_all_payment_frmt, amount_penalty_frmt, " +
			"       amount_currency_payment_frmt, amount_bon_payment_frmt, id_lg_promoter_pay_line, id_lg_promoter " +
	  		"  FROM (SELECT ROWNUM rn,  " +
	  		"               name_lg_promoter, date_begin_sale_frmt, date_end_sale_frmt, sales_cards_count, " +
	  		"               amount_all_payment_frmt||' '||sname_currency amount_all_payment_frmt," +
	  		"               amount_currency_payment_frmt||' '||sname_currency amount_currency_payment_frmt," +
	  		"               amount_penalty_frmt||' '||sname_currency amount_penalty_frmt," +
	  		"               amount_bon_payment_frmt," +
	  		"               id_lg_promoter_pay_line, id_lg_promoter " +
            "           FROM (SELECT * " +
            "                   FROM " + getGeneralDBScheme() + ".vc_lg_promoter_pay_line_all "+
            "                  WHERE id_lg_promoter_payment = ? ";
        pParam.add(new bcFeautureParam("int", this.idLGPromoterPayment));
        
        if (!isEmpty(pFindString)) {
    		mySQL = mySQL + 
   				" AND (UPPER(name_lg_promoter) LIKE UPPER('%" + pFindString + "%') OR " +
   				"      UPPER(date_begin_sale_frmt) LIKE UPPER('%" + pFindString + "%') OR " +
   				"      UPPER(date_end_sale_frmt) LIKE UPPER('%" + pFindString + "%'))";
    		for (int i=0; i<3; i++) {
    		    pParam.add(new bcFeautureParam("string", pFindString));
    		}
    	}
        pParam.add(new bcFeautureParam("int", p_end));
        pParam.add(new bcFeautureParam("int", p_beg));
        mySQL = mySQL + 
            "                  ORDER BY name_lg_promoter, date_begin_sale DESC) " +
            "          WHERE ROWNUM < ? " + 
            " ) WHERE rn >= ?";
        
        boolean hasPromoterPermission = false;
        
        try{
        	if (isEditMenuPermited("LOGISTIC_CLIENTS_PROMOTERS")>=0) {
        		hasPromoterPermission = true;
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
               html.append(getBottomFrameTableTH(logisticXML, mtd.getColumnName(i)));
            }
            html.append("<th>&nbsp;</th>\n");
            html.append("</tr></thead><tbody>");
            while (rset.next())
            {
            	html.append("<tr>");
          	  	for (int i=1; i <= colCount-2; i++) {
          	  		if ("NAME_LG_PROMOTER".equalsIgnoreCase(mtd.getColumnName(i)) && hasPromoterPermission) {
          	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/logistic/clients/promoterspecs.jsp?id=" + rset.getString("ID_LG_PROMOTER"), "", ""));
          	  		} else {
          	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", "", ""));
          	  		}
          	  	}
          	  	String myHyperLink = "../crm/logistic/clients/paymentlineupdate.jsp?id_payment="+this.idLGPromoterPayment+"&id="+rset.getString("ID_LG_PROMOTER_PAY_LINE")+"&type=plan";
          	  	html.append(getEditButtonHTML(myHyperLink));
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
