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


public class bcFNInvoiceObject extends bcObject {
	private final static Logger LOGGER=Logger.getLogger(bcFNInvoiceObject.class);
	private Map<String,Comparable<String>> fieldHm = new HashMap<String, Comparable<String>>();
	
	private String idInvoice;
	
	public bcFNInvoiceObject(String pIdInvoice) {
		this.idInvoice = pIdInvoice;
		getFeature();
	}
	
	private void getFeature() {
		String featureSelect = " SELECT * FROM " + getGeneralDBScheme() + ".VC_FN_INVOICE_ALL WHERE id_invoice = ?";
		fieldHm = getFeatures2(featureSelect, this.idInvoice, false);
	}
	
	public String getValue(String pColumnName) {
		return getFeautureResult(fieldHm, pColumnName);
	}
	
    public String getContentHTML(String pFind, String p_beg, String p_end) {
        StringBuilder html = new StringBuilder();
        Connection con = null;
        PreparedStatement st = null;
	    boolean hasEditPermission = false;
    	
	    ArrayList<bcFeautureParam> pParam = initParamArray();

	    String mySQL = 
        	" SELECT rn, name_nomenkl, count_nomenkl, price_nomenkl_frmt, " +
        	"		 '<font style=\"color:green;font-weight:bold;\">'||total_amount_frmt||'</font>' total_amount_frmt, " +
        	"		 '<font style=\"color:blue;font-weight:bold;\">'||tax_percent_frmt||'</font>' tax_percent_frmt, " +
        	"        '<font style=\"color:red;font-weight:bold;\">'||tax_amount_frmt||'</font>' tax_amount_frmt, " +
        	"        id_invoice_line, can_change_data " +
	      	"   FROM (SELECT ROWNUM rn, name_nomenkl, count_nomenkl, price_nomenkl_frmt, total_amount_frmt, tax_percent_frmt, " +
	      	"                tax_amount_frmt, id_invoice_line, can_change_data" +
	      	" 			FROM (SELECT * " +
	      	"                   FROM " + getGeneralDBScheme() + ".vc_fn_invoice_line_all " +
	      	"                  WHERE id_invoice = ? ";
	    pParam.add(new bcFeautureParam("int", this.idInvoice));
	    
    	if (!isEmpty(pFind)) {
    		mySQL = mySQL + 
    			" AND (TO_CHAR(name_nomenkl) LIKE UPPER('%'||?||'%') OR " +
    			"      UPPER(price_nomenkl_frmt) LIKE UPPER('%'||?||'%') OR " +
    			"      UPPER(total_amount_frmt) LIKE UPPER('%'||?||'%') OR " +
    			"      UPPER(tax_amount_frmt) LIKE UPPER('%'||?||'%')) ";
    		for (int i=0; i<4; i++) {
    		    pParam.add(new bcFeautureParam("string", pFind));
    		}
    	}
        pParam.add(new bcFeautureParam("int", p_end));
        pParam.add(new bcFeautureParam("int", p_beg));
        mySQL = mySQL + 
	      	"         ORDER BY order_number" +
	      	"   ) WHERE ROWNUM < ? " + 
	      	" ) WHERE rn >= ?";
        
        try{
        	if (isEditPermited("CRM_FINANCE_INVOICE_INFO")>0) {
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

            for (int i=1; i <= colCount-2; i++) {
            	if ("PRICE_NOMENKL_FRMT".equalsIgnoreCase(mtd.getColumnName(i)) ||
            			"TOTAL_AMOUNT_FRMT".equalsIgnoreCase(mtd.getColumnName(i)) ||
            			"TAX_AMOUNT_FRMT".equalsIgnoreCase(mtd.getColumnName(i))) {
            		html.append(getBottomFrameTableTH3(clearingXML, mtd.getColumnName(i), "", ", " + this.getValue("SNAME_CURRENCY")));
            	} else {
            		html.append(getBottomFrameTableTH(clearingXML, mtd.getColumnName(i)));
            	}
            }
            if (hasEditPermission) {
                html.append("<th>&nbsp;</th><th>&nbsp;</th>\n");
            }
	        html.append("</tr></thead><tbody>");
            
            while (rset.next()) {
            	html.append("<tr>\n");
            	for (int i=1; i <= colCount-2; i++) {
            		html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", "", ""));
          	  	}
                if (hasEditPermission && "Y".equalsIgnoreCase(rset.getString("CAN_CHANGE_DATA"))) {
	            	String myHyperLink = "../crm/finance/invoiceupdate.jsp?type=line&id="+this.idInvoice+"&id_invoice_line="+rset.getString("ID_INVOICE_LINE");
	            	String myDeleteLink = "../crm/finance/invoiceupdate.jsp?type=line&id="+this.idInvoice+"&id_invoice_line="+rset.getString("ID_INVOICE_LINE")+"&action=remove&process=yes";
	                html.append(getDeleteButtonHTML(myDeleteLink, clearingXML.getfieldTransl("h_delete_invoice_line", false), rset.getString("NAME_NOMENKL")));
	            	html.append(getEditButtonWitDivHTML(myHyperLink, "div_data_detail"));
                } else {
                	html.append("<td>&nbsp;</td><td>&nbsp;</td>\n");
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
	
    public String getTransactionsHTML(String pFindString, String pTypeTrans, String pPayType, String pStateTrans, String p_beg, String p_end) {
    	bcListTransaction list = new bcListTransaction();
    	
    	String pWhereCause = ", v_invoice_trans_all t WHERE a.id_trans = t.id_trans AND t.id_invoice = ? ";
    	
    	ArrayList<bcFeautureParam> pWhereValue = new ArrayList<bcFeautureParam>();
    	pWhereValue.add(new bcFeautureParam("int", this.idInvoice));
    	
    	return list.getTransactionList(pWhereCause, pWhereValue, pFindString, pTypeTrans, pPayType, pStateTrans, p_beg, p_end);
    }

}
