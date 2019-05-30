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

public class bcAutoreconcilationObject extends bcObject {
	private final static Logger LOGGER=Logger.getLogger(bcAutoreconcilationObject.class);
	
	private Map<String,Comparable<String>> fieldHm = new HashMap<String, Comparable<String>>();
	
	private String idAutoreconcil;
	
	public bcAutoreconcilationObject() {	}
	
	public bcAutoreconcilationObject(String pIdAutoreconcil) {
		this.idAutoreconcil = pIdAutoreconcil;
		this.getFeature();
	}
	
	private void getFeature() {
		String featureSelect = " SELECT * FROM " + getGeneralDBScheme() + ".V_AUTORECONCILATION WHERE id_autoreconcilation = ?";
		fieldHm = getFeatures2(featureSelect, this.idAutoreconcil, false);
	}
	
	public String getValue(String pColumnName) {
		return getFeautureResult(fieldHm, pColumnName);
	}
	
    public String getAutoreconcilationLinesHTML(String pFindString, String p_beg, String p_end) {
    	StringBuilder html = new StringBuilder();
        PreparedStatement st = null;
        Connection con = null;
        
        ArrayList<bcFeautureParam> pParam = initParamArray();
        
        String mySQL = 
        	" SELECT number_doc_clearing, date_clearing, " +
            "        number_bank_statement, date_bank_statement, bank_statement_line_number," +
            "        reconciled_amount, id_clearing_line, id_bank_statement "+
            "   FROM (SELECT ROWNUM rn, number_doc_clearing, date_clearing_frmt date_clearing, " +
             "               number_bank_statement, date_bank_statement_frmt date_bank_statement, bank_statement_line_number," +
            "                reconciled_amount_frmt reconciled_amount, " +
            "                id_clearing_line, id_bank_statement "+
            "           FROM (SELECT * " +
            "                   FROM " + getGeneralDBScheme() + ".vc_reconcile_club_all "+
            "                  WHERE id_autoreconcilation = ? ";
        pParam.add(new bcFeautureParam("int", this.idAutoreconcil));
	    if (!isEmpty(pFindString)) {
	    	mySQL = mySQL + 
					" AND (UPPER(number_doc_clearing) LIKE UPPER('%'||?||'%') OR " +
					"	   UPPER(date_clearing_frmt) LIKE UPPER('%'||?||'%') OR " +
					"	   UPPER(number_bank_statement) LIKE UPPER('%'||?||'%') OR " +
					"	   UPPER(date_bank_statement_frmt) LIKE UPPER('%'||?||'%') OR " +
					"	   UPPER(bank_statement_line_number) LIKE UPPER('%'||?||'%') OR " +
					"	   UPPER(reconciled_amount_frmt) LIKE UPPER('%'||?||'%')) ";
	    	for (int i=0; i<6; i++) {
	    		pParam.add(new bcFeautureParam("string", pFindString));
	    	}
	    }
	    pParam.add(new bcFeautureParam("int", p_end));
	    pParam.add(new bcFeautureParam("int", p_beg));
	    mySQL = mySQL +
            "                  ORDER BY begin_period_frmt, end_period_frmt, clearing_line_number) " +
            "          WHERE ROWNUM < ? " + 
            " ) WHERE rn >= ? ";
        
        boolean hasClearingPermission = false;
        boolean hasBSPermission = false;
        
        try{
        	if (isEditMenuPermited("FINANCE_CLEARING")>=0) {
        		hasClearingPermission = true;
        	}
        	if (isEditMenuPermited("FINANCE_BANKSTATEMENT")>=0) {
        		hasBSPermission = true;
        	}
        	
        	LOGGER.debug(prepareSQLToLog(mySQL, pParam));
        	con = Connector.getConnection(getSessionId());
        	st = con.prepareStatement(mySQL);
        	st = prepareParam(st, pParam);
            ResultSet rset = st.executeQuery();
            ResultSetMetaData mtd = rset.getMetaData();
            int colCount = mtd.getColumnCount();
            
            html.append(getBottomFrameTable());
        	html.append("<tr>\n");
            html.append("<th colspan=\"3\">"+ kvitovkaXML.getfieldTransl("t_clearing", false)+"</th>\n");
            html.append("<th colspan=\"3\">"+ kvitovkaXML.getfieldTransl("t_bank_statement", false)+"</th>\n");
            html.append("<th rowspan=\"2\">"+ kvitovkaXML.getfieldTransl("RECONCILED_AMOUNT", false)+"</th>\n");
        	html.append("</tr>\n");
        	html.append("<tr>\n");
            html.append("<th>"+ clearingXML.getfieldTransl("NUMBER_DOC_CLEARING", false)+"</th>\n");
            html.append("<th>"+ clearingXML.getfieldTransl("DATE_CLEARING", false)+"</th>\n");
            html.append("<th>"+ kvitovkaXML.getfieldTransl("CLEARING_LINE_NUMBER", false)+"</th>\n");
            html.append("<th>"+ kvitovkaXML.getfieldTransl("NUMBER_BANK_STATEMENT", false)+"</th>\n");
            html.append("<th>"+ kvitovkaXML.getfieldTransl("DATE_BANK_STATEMENT", false)+"</th>\n");
            html.append("<th>"+ kvitovkaXML.getfieldTransl("BANK_STATEMENT_LINE_NUMBER", false)+"</th>\n");
        	html.append("</tr></thead><tbody>");
            while (rset.next())
            {
            	html.append("<tr>\n");
                for (int i=1; i<=colCount-2; i++) {
                	String myData = "";
                	if ("NUMBER_DOC_CLEARING".equalsIgnoreCase(mtd.getColumnName(i)) && 
                			hasClearingPermission && 
                			(!isEmpty(rset.getString(i)))) {
                		myData = "../crm/finance/clearing_linespecs.jsp?id="+rset.getString("ID_CLEARING_LINE"); 
                    } else if ("NUMBER_BANK_STATEMENT".equalsIgnoreCase(mtd.getColumnName(i)) && 
                    		hasBSPermission && 
                			(!isEmpty(rset.getString(i)))) {
                		myData = "../crm/finance/bankstatementspecs.jsp?id="+rset.getString("ID_BANK_STATEMENT"); 
                    } else {
                    	myData = "";
                    }
                	html.append(getBottomFrameTableTDBase("", mtd.getColumnName(i), mtd.getColumnType(i), rset.getString(i), myData, "", ""));
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
    } //getClearingAllHTML

}
