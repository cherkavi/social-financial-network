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

public class bcLGPromoterPenaltyObject extends bcObject {
	private final static Logger LOGGER=Logger.getLogger(bcLGPromoterPenaltyObject.class);
	
	private Map<String,Comparable<String>> fieldHm = new HashMap<String, Comparable<String>>();
	
	private String idLGPromoterPenalty;
	
	public bcLGPromoterPenaltyObject(String pIdLGPromoterPenalty) {
		this.idLGPromoterPenalty = pIdLGPromoterPenalty;
		getFeature();
	}
	
	private void getFeature() {
		String mySQL = " SELECT * FROM " + getGeneralDBScheme() + ".VC_LG_PROMOTER_PENALTY_ALL WHERE id_lg_promoter_penalty = ?";
		fieldHm = getFeatures2(mySQL, this.idLGPromoterPenalty, false);
	}
	
	public String getValue(String pColumnName) {
		return getFeautureResult(fieldHm, pColumnName);
	}
	
    public String getPenaltyWriteOffHTML(String pFindString, String p_beg, String p_end) {
        StringBuilder html = new StringBuilder();
        Connection con = null;
        PreparedStatement st = null;

        ArrayList<bcFeautureParam> pParam = initParamArray();

        String mySQL = 
        	"SELECT rn, name_lg_promoter_pay_kind, date_lg_promoter_payment_frmt, " +
        	"       value_lg_penalty_wr_off_frmt, id_lg_promoter_penalty_wr_off, id_lg_promoter_payment " +
	  		"  FROM (SELECT ROWNUM rn,  " +
	  		"               name_lg_promoter_pay_kind, date_lg_promoter_payment_frmt, " +
	  		"               value_lg_penalty_wr_off_frmt||' '||sname_currency value_lg_penalty_wr_off_frmt," +
	  		"               id_lg_promoter_penalty_wr_off, id_lg_promoter_payment " +
            "           FROM (SELECT * " +
            "                   FROM " + getGeneralDBScheme() + ".vc_lg_promoter_penalty_wo_all "+
            "                  WHERE id_lg_promoter_penalty = ? ";
        pParam.add(new bcFeautureParam("int", this.idLGPromoterPenalty));

        if (!isEmpty(pFindString)) {
    		mySQL = mySQL + 
   				" AND (UPPER(date_lg_promoter_payment_frmt) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(value_lg_penalty_wr_off_frmt) LIKE UPPER('%'||?||'%'))";
    		for (int i=0; i<2; i++) {
    		    pParam.add(new bcFeautureParam("string", pFindString));
    		}
    	}
        pParam.add(new bcFeautureParam("int", p_end));
        pParam.add(new bcFeautureParam("int", p_beg));
        mySQL = mySQL + 
            "                  ORDER BY date_lg_promoter_payment_frmt DESC) " +
            "          WHERE ROWNUM < ? " + 
            " ) WHERE rn >= ?";
        
        boolean hasPromoterPaymentPermission = false;
        
        try{
        	if (isEditMenuPermited("LOGISTIC_CLIENTS_PROMOTERS")>=0) {
        		hasPromoterPaymentPermission = true;
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
            html.append("</tr></thead><tbody>");
            while (rset.next())
            {
            	html.append("<tr>");
          	  	for (int i=1; i <= colCount-2; i++) {
          	  		if ("DATE_LG_PROMOTER_PAYMENT_FRMT".equalsIgnoreCase(mtd.getColumnName(i)) && hasPromoterPaymentPermission) {
          	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/logistic/clients/paymentspecs.jsp?id=" + rset.getString("ID_LG_PROMOTER_PAYMENT"), "", ""));
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
