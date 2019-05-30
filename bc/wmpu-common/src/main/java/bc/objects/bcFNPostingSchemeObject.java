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


public class bcFNPostingSchemeObject extends bcObject {
	private final static Logger LOGGER=Logger.getLogger(bcFNPostingSchemeObject.class);
	private Map<String,Comparable<String>> fieldHm = new HashMap<String, Comparable<String>>();
	
	private String idPostingScheme;
	
	public bcFNPostingSchemeObject(String pIdPostingScheme) {
		this.idPostingScheme = pIdPostingScheme;
		getFeature();
	}
	
	private void getFeature() {
		String featureSelect = " SELECT * FROM " + getGeneralDBScheme() + ".VC_FN_POSTING_SCHEME_CLUB_ALL WHERE id_fn_posting_scheme = ?";
		fieldHm = getFeatures2(featureSelect, this.idPostingScheme, false);
	}
	
	public String getValue(String pColumnName) {
		return getFeautureResult(fieldHm, pColumnName);
	}
	
    public String getLinesHTML(String pCdOperationType, String pCdClubRelType, String pFind, String p_beg, String p_end) {
        StringBuilder html = new StringBuilder();
        Connection con = null;
        PreparedStatement st = null;
	    boolean hasBKAccountSchemePermission = false;
	    boolean hasEditPermission = false;
    	
	    ArrayList<bcFeautureParam> pParam = initParamArray();

	    String mySQL = 
        	" SELECT rn, id_bk_operation_scheme_line, oper_number, name_club_rel_type, name_bk_operation_type, " +
    		"        DECODE(cd_bk_phase, " +
    		"               'AFTER_MONEY_TRANSFER', '<font color=\"green\">'||name_bk_phase||'</font>', " +
    		"               name_bk_phase) name_bk_phase, " +
    		"        debet_cd_bk_account_sh_line debet_cd_bk_account, " +
	      	"        credit_cd_bk_account_sh_line credit_cd_bk_account, " +
	      	"        oper_content, " +
	      	"        DECODE(exist_flag, " +
		    "               'Y', '<font color=\"green\"><b>'||exist_flag_tsl||'<b></font>', " +
		    "               '<font color=\"red\"><b>'||exist_flag_tsl||'<b></font>' " +
		    "        ) exist_flag_tsl, exist_flag, " +
	      	"        debet_id_bk_account_sh_line, credit_id_bk_account_sh_line  " +
	      	"   FROM (SELECT ROWNUM rn, id_bk_operation_scheme_line, name_club_rel_type, name_bk_operation_type, " +
	      	"                cd_bk_phase, name_bk_phase, oper_number, debet_cd_bk_account_sh_line, " +
	      	"                credit_cd_bk_account_sh_line, oper_content, exist_flag_tsl, exist_flag, " +
	      	"                debet_id_bk_account_sh_line, credit_id_bk_account_sh_line " +
	      	" 			FROM (SELECT * " +
	      	"                   FROM " + getGeneralDBScheme() + ".vc_oper_scheme_lines_club_all " +
	      	"                  WHERE id_bk_operation_scheme = ? ";
	    pParam.add(new bcFeautureParam("int", this.idPostingScheme));
	    
    	if (!(isEmpty(pCdOperationType) || "-1".equalsIgnoreCase(pCdOperationType))) {
    		mySQL = mySQL + " AND cd_bk_operation_type = ? ";
    		pParam.add(new bcFeautureParam("string", pCdOperationType));
    	}
    	if (!(isEmpty(pCdClubRelType) || "-1".equalsIgnoreCase(pCdClubRelType))) {
    		mySQL = mySQL + " AND cd_club_rel_type = ? ";
    		pParam.add(new bcFeautureParam("string", pCdClubRelType));
    	}
        if (!isEmpty(pFind)) {
    		mySQL = mySQL + " AND (UPPER(name_bk_phase) LIKE UPPER('%'||?||'%') OR " +
    			"UPPER(oper_number) LIKE UPPER('%'||?||'%') OR " +
    			"UPPER(debet_cd_bk_account_sh_line) LIKE UPPER('%'||?||'%') OR " +
    			"UPPER(credit_cd_bk_account_sh_line) LIKE UPPER('%'||?||'%') OR " +
    			"UPPER(oper_content) LIKE UPPER('%'||?||'%')) ";
    		for (int i=0; i<4; i++) {
    		    pParam.add(new bcFeautureParam("string", pFind));
    		}
    	}
        pParam.add(new bcFeautureParam("int", p_end));
        pParam.add(new bcFeautureParam("int", p_beg));
        mySQL = mySQL + 
	      	"         ORDER BY oper_number" +
	      	"   ) WHERE ROWNUM < ? " + 
	      	" ) WHERE rn >= ?";
        
        try{
        	if (isEditPermited("FINANCE_POSTING_SCHEME_LINES")>0) {
        		hasEditPermission = true;
        	}
	    	if (isEditMenuPermited("FINANCE_BK_SCHEME")>=0) {
	    		hasBKAccountSchemePermission = true;
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

            for (int i=1; i <= colCount-3; i++) {
               html.append(getBottomFrameTableTH(posting_schemeXML, mtd.getColumnName(i)));
            }
            if (hasEditPermission) {
                html.append("<th>&nbsp;</th><th>&nbsp;</th>\n");
            }
	        html.append("</tr></thead><tbody>");
            
            while (rset.next()) {
            	html.append("<tr>\n");
            	for (int i=1; i <= colCount-3; i++) {
            		if ("ID_BK_OPERATION_SCHEME_LINE".equalsIgnoreCase(mtd.getColumnName(i)) ||
            				"OPER_NUMBER".equalsIgnoreCase(mtd.getColumnName(i))) {
          	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/finance/posting_scheme_linespecs.jsp?type=general&id_scheme="+this.idPostingScheme+"&id="+rset.getString("ID_BK_OPERATION_SCHEME_LINE"), "", ""));
         	  		} else if (hasBKAccountSchemePermission && "DEBET_CD_BK_ACCOUNT".equalsIgnoreCase(mtd.getColumnName(i))) {
          	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/finance/bk_scheme_linespecs.jsp?id="+rset.getString("DEBET_ID_BK_ACCOUNT_SH_LINE"), "", ""));
         	  		} else if (hasBKAccountSchemePermission && "CREDIT_CD_BK_ACCOUNT".equalsIgnoreCase(mtd.getColumnName(i))) {
          	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/finance/bk_scheme_linespecs.jsp?id="+rset.getString("CREDIT_ID_BK_ACCOUNT_SH_LINE"), "", ""));
         	  		} else {
          	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", "", ""));
          	  		}
            	}
                if (hasEditPermission) {
	            	String myHyperLink = "../crm/finance/posting_scheme_linespecs.jsp?type=general&id_scheme="+this.idPostingScheme+"&id="+rset.getString("ID_BK_OPERATION_SCHEME_LINE");
	            	String myDeleteLink = "../crm/finance/posting_scheme_lineupdate.jsp?type=general&id_scheme="+this.idPostingScheme+"&id="+rset.getString("ID_BK_OPERATION_SCHEME_LINE")+"&action=remove&process=yes";
	                html.append(getDeleteButtonHTML(myDeleteLink, oper_schemeXML.getfieldTransl("h_delete_line", false), rset.getString("OPER_NUMBER")));
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
