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


public class bcFNOperSchemeObject extends bcObject {
	private final static Logger LOGGER=Logger.getLogger(bcFNOperSchemeObject.class);
	private Map<String,Comparable<String>> fieldHm = new HashMap<String, Comparable<String>>();
	
	private String idOperScheme;
	
	public bcFNOperSchemeObject(String pIdOperScheme) {
		this.idOperScheme = pIdOperScheme;
		getFeature();
	}
	
	private void getFeature() {
		String featureSelect = " SELECT * FROM " + getGeneralDBScheme() + ".VC_FN_OPER_SCHEME_CLUB_ALL WHERE id_fn_oper_scheme = ?";
		fieldHm = getFeatures2(featureSelect, this.idOperScheme, false);
	}
	
	public String getValue(String pColumnName) {
		return getFeautureResult(fieldHm, pColumnName);
	}
	
    public String getLinesHTML(String pCdOperationType, String pFind, String p_beg, String p_end) {
        StringBuilder html = new StringBuilder();
        Connection con = null;
        PreparedStatement st = null;
	    boolean hasEditPermission = false;
    	
	    ArrayList<bcFeautureParam> pParam = initParamArray();

	    String mySQL = 
        	" SELECT rn, order_number, full_name_fn_oper_type, desc_fn_oper_scheme_line, id_fn_oper_scheme_line  " +
	      	"   FROM (SELECT ROWNUM rn, order_number, " +
	      	"                DECODE(cd_fn_oper_kind, " +
        	"                       'FINANCE_OPERATION', '<font color=\"blue\">'||full_name_fn_oper_type||'</font>', " +
        	"                       'BON_CARD_OPERATION', '<font color=\"green\"><b>'||full_name_fn_oper_type||'</b></font>', " +
        	"                       'TERMINAL_OPERATION', '<font color=\"black\"><b>'||full_name_fn_oper_type||'</b></font>', " +
        	"                       full_name_fn_oper_type) " +
	  		"                full_name_fn_oper_type, " +
	      	"                desc_fn_oper_scheme_line, id_fn_oper_scheme_line " +
	      	" 			FROM (SELECT * " +
	      	"                   FROM " + getGeneralDBScheme() + ".vc_fn_oper_scheme_lines_all " +
	      	"                  WHERE id_fn_oper_scheme = ? ";
	    pParam.add(new bcFeautureParam("int", this.idOperScheme));
	    
    	if (!isEmpty(pCdOperationType)) {
    		mySQL = mySQL + " AND cd_name_fn_oper_type = ? ";
    		pParam.add(new bcFeautureParam("string", pCdOperationType));
    	}
        if (!isEmpty(pFind)) {
    		mySQL = mySQL + 
    			" AND (TO_CHAR(id_fn_oper_scheme_line) LIKE UPPER('%'||?||'%') OR " +
    			"      UPPER(desc_fn_oper_scheme_line) LIKE UPPER('%'||?||'%')) ";
    		for (int i=0; i<5; i++) {
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
        	if (isEditPermited("FINANCE_OPER_SCHEME_OPER")>0) {
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
               html.append(getBottomFrameTableTH(oper_schemeXML, mtd.getColumnName(i)));
            }
            if (hasEditPermission) {
                html.append("<th>&nbsp;</th><th>&nbsp;</th>\n");
            }
	        html.append("</tr></thead><tbody>");
            
            while (rset.next()) {
            	html.append("<tr>\n");
            	for (int i=1; i <= colCount-1; i++) {
            		html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", "", ""));
          	  	}
                if (hasEditPermission) {
	            	String myHyperLink = "../crm/finance/oper_scheme_linespecs.jsp?type=general&id_scheme="+this.idOperScheme+"&id="+rset.getString("ID_FN_OPER_SCHEME_LINE");
	            	String myDeleteLink = "../crm/finance/oper_scheme_lineupdate.jsp?type=general&id_scheme="+this.idOperScheme+"&id="+rset.getString("ID_FN_OPER_SCHEME_LINE")+"&action=remove&process=yes";
	                html.append(getDeleteButtonHTML(myDeleteLink, oper_schemeXML.getfieldTransl("h_delete_line", false), rset.getString("ID_FN_OPER_SCHEME_LINE")));
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
