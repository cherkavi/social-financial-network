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


public class bcFNBKSchemeObject extends bcObject {
	private final static Logger LOGGER=Logger.getLogger(bcFNBKSchemeObject.class);
	private Map<String,Comparable<String>> fieldHm = new HashMap<String, Comparable<String>>();
	
	private String idBKAccountScheme;
	
	public bcFNBKSchemeObject(String pIdBKAccountScheme) {
		this.idBKAccountScheme = pIdBKAccountScheme;
		getFeature();
	}
	
	private void getFeature() {
		String featureSelect = " SELECT * FROM " + getGeneralDBScheme() + ".VC_BK_ACCOUNT_SCHEME_CLUB_ALL WHERE id_bk_account_scheme = ?";
		fieldHm = getFeatures2(featureSelect, this.idBKAccountScheme, false);
	}
	
	public String getValue(String pColumnName) {
		return getFeautureResult(fieldHm, pColumnName);
	}
	
    public String getLinesHTML(String pFind, String pGroup, String pExist, String p_beg, String p_end) {
        StringBuilder html = new StringBuilder();
        Connection con = null;
        PreparedStatement st = null;
	    boolean hasEditPermission = false;
    	
	    ArrayList<bcFeautureParam> pParam = initParamArray();

	    String mySQL = 
        	"SELECT rn, id_bk_account_scheme_line, cd_bk_account_scheme_line, name_bk_account_scheme_line, " +
        	"       int_nm_bk_account_scheme_line, is_group_tsl, cd_bk_account_scheme_ln_parent," +
        	"       exist_flag_tsl " +
  		  	"  FROM (SELECT ROWNUM rn, id_bk_account_scheme_line, cd_bk_account_scheme_line, name_bk_account_scheme_line, " +
  		  	"               int_nm_bk_account_scheme_line, cd_bk_account_scheme_ln_parent, " +
  		    "               DECODE(exist_flag, " +
	        "                      'Y', '<font color=\"green\"><b>'||exist_flag_tsl||'<b></font>', " +
	        "                      '<font color=\"red\"><b>'||exist_flag_tsl||'<b></font>' " +
	        "               ) exist_flag_tsl, " +
  		  	"				DECODE(is_group, " +
	        "                      'Y', '<font color=\"blue\"><b>'||is_group_tsl||'<b></font>', " +
	        "                      '<font color=\"green\"><b>'||is_group_tsl||'<b></font>' " +
	        "               ) is_group_tsl " +
  		  	"         FROM (SELECT * " +
  		  	"                 FROM " + getGeneralDBScheme()+".vc_bk_account_sh_ln_club_all " +
	      	"                  WHERE id_bk_account_scheme = ? ";
	    pParam.add(new bcFeautureParam("int", this.idBKAccountScheme));
	    
        if (!isEmpty(pFind)) {
    		mySQL = mySQL + 
    			" AND (UPPER(TO_CHAR(id_bk_account_scheme_line)) LIKE UPPER('%'||?||'%') OR " +
    			"      UPPER(cd_bk_account_scheme_line) LIKE UPPER('%'||?||'%') OR " +
    			"      UPPER(name_bk_account_scheme_line) LIKE UPPER('%'||?||'%') OR " +
    			"      UPPER(int_nm_bk_account_scheme_line) LIKE UPPER('%'||?||'%') OR " +
    			"      UPPER(cd_bk_account_scheme_ln_parent) LIKE UPPER('%'||?||'%')) ";
    		for (int i=0; i<5; i++) {
    		    pParam.add(new bcFeautureParam("string", pFind));
    		}
    	}
        if (!isEmpty(pGroup)) {
    		mySQL = mySQL + " AND is_group = ? ";
    		pParam.add(new bcFeautureParam("string", pGroup));
    	}
        if (!isEmpty(pExist)) {
    		mySQL = mySQL + " AND exist_flag = ? ";
    		pParam.add(new bcFeautureParam("string", pExist));
    	}
        pParam.add(new bcFeautureParam("int", p_end));
        pParam.add(new bcFeautureParam("int", p_beg));
        mySQL = mySQL + 
	      	"         ORDER BY cd_bk_account_scheme_line" +
	      	"   ) WHERE ROWNUM < ? " + 
	      	" ) WHERE rn >= ?";
        
        try{
        	if (isEditPermited("FINANCE_BK_SCHEME_LINES")>0) {
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

            for (int i=1; i <= colCount; i++) {
               html.append(getBottomFrameTableTH(bk_schemeXML, mtd.getColumnName(i)));
            }
            if (hasEditPermission) {
                html.append("<th>&nbsp;</th><th>&nbsp;</th>\n");
            }
	        html.append("</tr></thead><tbody>");
            
            while (rset.next()) {
            	html.append("<tr>\n");
            	for (int i=1; i <= colCount; i++) {
            		if ("ID_BK_ACCOUNT_SCHEME_LINE".equalsIgnoreCase(mtd.getColumnName(i)) ||
            				"CD_BK_ACCOUNT_SCHEME_LINE".equalsIgnoreCase(mtd.getColumnName(i))) {
            			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/finance/bk_scheme_linespecs.jsp?type=general&id_scheme="+this.idBKAccountScheme+"&id="+rset.getString("ID_BK_ACCOUNT_SCHEME_LINE"), "", ""));
	          		} else {
            			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", "", ""));
	          		}
          	  	}
                if (hasEditPermission) {
	            	String myHyperLink = "../crm/finance/bk_scheme_linespecs.jsp?type=general&id_scheme="+this.idBKAccountScheme+"&id="+rset.getString("ID_BK_ACCOUNT_SCHEME_LINE");
	            	String myDeleteLink = "../crm/finance/bk_scheme_lineupdate.jsp?type=participant&id_scheme="+this.idBKAccountScheme+"&id="+rset.getString("ID_BK_ACCOUNT_SCHEME_LINE")+"&action=remove&process=yes";
	                html.append(getDeleteButtonHTML(myDeleteLink, posting_schemeXML.getfieldTransl("h_delete_line", false), rset.getString("CD_BK_ACCOUNT_SCHEME_LINE")));
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
