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


public class bcFNBKSchemeLineObject extends bcObject {
	private final static Logger LOGGER=Logger.getLogger(bcFNBKSchemeLineObject.class);
	
	private Map<String,Comparable<String>> fieldHm = new HashMap<String, Comparable<String>>();
	
	private String idBKAccountSchemeLine;
	
	public bcFNBKSchemeLineObject(String pIdBKAccountSchemeLine) {
		this.idBKAccountSchemeLine = pIdBKAccountSchemeLine;
		getFeature();
	}
	
	private void getFeature() {
		String featureSelect = " SELECT * FROM " + getGeneralDBScheme() + ".VC_BK_ACCOUNT_SH_LN_CLUB_ALL WHERE id_bk_account_scheme_line = ?";
		fieldHm = getFeatures2(featureSelect, this.idBKAccountSchemeLine, false);
	}
	
	public String getValue(String pColumnName) {
		return getFeautureResult(fieldHm, pColumnName);
	}
  
	public String getOperationSchemesHTML(String pFindString, String pIdOperScheme, String p_beg, String p_end) {
        StringBuilder html = new StringBuilder();
        Connection con = null;
        PreparedStatement st = null;
        
        ArrayList<bcFeautureParam> pParam = initParamArray();

        String mySQL = 
        	" SELECT rn, desc_bk_operation_scheme fn_posting_scheme, oper_number, " +
        	"		 name_club_rel_type, name_bk_operation_type, name_bk_phase, " +
	      	"        debet_cd_bk_account_sh_line debet_cd_bk_account, " +
	      	"        credit_cd_bk_account_sh_line credit_cd_bk_account, " +
	      	"        oper_content, " +
	      	"        DECODE(exist_flag, " +
	        "               'Y', '<font color=\"green\"><b>'||exist_flag_tsl||'<b></font>', " +
	        "               '<font color=\"red\"><b>'||exist_flag_tsl||'<b></font>' " +
	        "        ) exist_flag_tsl, " +
	      	"		 exist_flag, id_bk_operation_scheme_line, id_bk_operation_scheme," +
	      	"	     debet_id_bk_account_sh_line, credit_id_bk_account_sh_line " +
	      	"   FROM (SELECT ROWNUM rn, id_bk_operation_scheme_line, name_club_rel_type, name_bk_operation_type, " +
	      	"                name_bk_phase, oper_number, " +
	      	"                debet_cd_bk_account_sh_line, credit_cd_bk_account_sh_line, oper_content, " +
	      	"                exist_flag_tsl, exist_flag, id_bk_operation_scheme, desc_bk_operation_scheme," +
	      	"				 debet_id_bk_account_sh_line, credit_id_bk_account_sh_line " +
	      	" 			FROM (SELECT *" +
	      	"                   FROM " + getGeneralDBScheme() + ".vc_oper_scheme_lines_club_all " +
            "                  WHERE (debet_id_bk_account_sh_line = ? " + 
            "                     OR credit_id_bk_account_sh_line = ?) " ;

        pParam.add(new bcFeautureParam("int", this.idBKAccountSchemeLine));
        pParam.add(new bcFeautureParam("int", this.idBKAccountSchemeLine));
        
        if (!isEmpty(pFindString)) {
    		mySQL = mySQL + 
    			" AND (TO_CHAR(id_bk_operation_scheme_line) LIKE UPPER('%'||?||'%') OR " +
    			"      UPPER(name_club_rel_type) LIKE UPPER('%'||?||'%') OR " +
    			"      UPPER(oper_number) LIKE UPPER('%'||?||'%') OR " +
    			"      UPPER(debet_cd_bk_account_sh_line) LIKE UPPER('%'||?||'%') OR " +
    			"      UPPER(credit_cd_bk_account_sh_line) LIKE UPPER('%'||?||'%') OR " +
    			"      UPPER(oper_content) LIKE UPPER('%'||?||'%')) ";
    		for (int i=0; i<6; i++) {
    		    pParam.add(new bcFeautureParam("string", pFindString));
    		}
    	}
        if (!isEmpty(pIdOperScheme)) {
          	mySQL = mySQL + " AND id_bk_operation_scheme = ? ";
          	pParam.add(new bcFeautureParam("int", pIdOperScheme));
        }
        pParam.add(new bcFeautureParam("int", p_end));
        pParam.add(new bcFeautureParam("int", p_beg));
        mySQL = mySQL+ 
            "         ) WHERE ROWNUM < ? " + 
            " ) WHERE rn >= ?";
        
        String myFont = "";
        String myBGColor = "";
        
        boolean hasOperSchemePermission = false;
        
        try{
        	if (isEditMenuPermited("FINANCE_POSTING_SCHEME")>=0) {
        		hasOperSchemePermission = true;
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
            for (int i=1; i <= colCount-5; i++) {
            	html.append(getBottomFrameTableTH(posting_schemeXML, mtd.getColumnName(i)));
            }
            html.append("</tr></thead><tbody>\n");
            while (rset.next()) {
            	
                if ("Y".equalsIgnoreCase(rset.getString("EXIST_FLAG"))){
                	myFont = "";
                	myBGColor = noneBackGroundStyle;
                } else {
                	myFont = "<font color=\"red\">";
                	myBGColor = selectedBackGroundStyle;
                }
                
                html.append("<tr>");
                for (int i=1; i<=colCount-5; i++) {
                	if (hasOperSchemePermission && "OPER_NUMBER".equalsIgnoreCase(mtd.getColumnName(i))) {
          	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/finance/posting_scheme_linespecs.jsp?id="+rset.getString("ID_BK_OPERATION_SCHEME_LINE"), myFont, myBGColor));
                	} else if (hasOperSchemePermission && "FN_POSTING_SCHEME".equalsIgnoreCase(mtd.getColumnName(i))) {
          			  	html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/finance/posting_schemespecs.jsp?id=" + rset.getString("ID_BK_OPERATION_SCHEME"), myFont, myBGColor));
                	} else if ("DEBET_CD_BK_ACCOUNT".equalsIgnoreCase(mtd.getColumnName(i)) &&
                			!(this.idBKAccountSchemeLine.equalsIgnoreCase(rset.getString("DEBET_ID_BK_ACCOUNT_SH_LINE")))) {
            			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/finance/bk_scheme_linespecs.jsp?id=" + rset.getString("DEBET_ID_BK_ACCOUNT_SH_LINE"), myFont, myBGColor));
                	} else if ("CREDIT_CD_BK_ACCOUNT".equalsIgnoreCase(mtd.getColumnName(i)) &&
                			!(this.idBKAccountSchemeLine.equalsIgnoreCase(rset.getString("CREDIT_ID_BK_ACCOUNT_SH_LINE")))) {
            			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/finance/bk_scheme_linespecs.jsp?id=" + rset.getString("CREDIT_ID_BK_ACCOUNT_SH_LINE"), myFont, myBGColor));
            		} else {
          	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", myFont, myBGColor));
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
    } // getOperationSchemesHTML
  
	public String getBKAccountsHTML(String pFindString, String p_beg, String p_end) {
        StringBuilder html = new StringBuilder();
        Connection con = null;
        PreparedStatement st = null;

        ArrayList<bcFeautureParam> pParam = initParamArray();

        String mySQL = 
        	" SELECT rn, id_bk_account, cd_bk_account, name_bk_account, cd_bk_account_parent, " +
	  		"        is_group_tsl, exist_flag_tsl, exist_flag " +
  		    "  FROM (SELECT ROWNUM rn, id_bk_account, cd_bk_account, name_bk_account, " +
  		    "               cd_bk_account_parent, " +
  		    "				DECODE(is_group, " +
			"          			    'Y', '<b><font color=\"blue\">'||is_group_tsl||'</font></b>', " +
			"              		    '<b><font color=\"green\">'||is_group_tsl||'</font></b>' " +
			"          		 ) is_group_tsl, " +
  		    "	      		 DECODE(exist_flag, " +
			"          			    'Y', '<b><font color=\"green\">'||exist_flag_tsl||'</font></b>', " +
			"              		    '<b><font color=\"red\">'||exist_flag_tsl||'</font></b>' " +
			"          		 ) exist_flag_tsl, exist_flag " +
  		    "          FROM (SELECT * " +
	  		"                  FROM " + getGeneralDBScheme()+".vc_bk_accounts_club_all " +
	  		"                 WHERE id_bk_account_scheme_line = ? ";
        pParam.add(new bcFeautureParam("int", this.idBKAccountSchemeLine));
        
        if (!isEmpty(pFindString)) {
    		mySQL = mySQL + 
    			" AND (TO_CHAR(id_bk_account) LIKE UPPER('%'||?||'%') OR " +
    			"      UPPER(cd_bk_account) LIKE UPPER('%'||?||'%') OR " +
    			"      UPPER(name_bk_account) LIKE UPPER('%'||?||'%') OR " +
    			"      UPPER(cd_bk_account_parent) LIKE UPPER('%'||?||'%')) ";
    		for (int i=0; i<4; i++) {
    		    pParam.add(new bcFeautureParam("string", pFindString));
    		}
    	}
        pParam.add(new bcFeautureParam("int", p_end));
        pParam.add(new bcFeautureParam("int", p_beg));
        mySQL = mySQL +
            "         ) WHERE ROWNUM < ? " + 
            " ) WHERE rn >= ?";
        
        String myFont = "";
        String myBGColor = "";
        
        boolean hasBKAccountPermission = false;
        
        try{
        	if (isEditMenuPermited("FINANCE_BK_ACCOUNTS")>=0) {
        		hasBKAccountPermission = true;
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
            	html.append(getBottomFrameTableTH(bk_accountXML, mtd.getColumnName(i)));
            }
            html.append("</tr></thead><tbody>\n");
            while (rset.next()) {
            	
                if ("Y".equalsIgnoreCase(rset.getString("EXIST_FLAG"))){
                	myFont = "";
                	myBGColor = noneBackGroundStyle;
                } else {
                	myFont = "<font color=\"red\">";
                	myBGColor = selectedBackGroundStyle;
                }
                
                html.append("<tr>");
                for (int i=1; i<=colCount-1; i++) {
                	if (hasBKAccountPermission && "CD_BK_ACCOUNT".equalsIgnoreCase(mtd.getColumnName(i))) {
          	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/finance/bk_accountspecs.jsp?id="+rset.getString("ID_BK_ACCOUNT"), myFont, myBGColor));
         	  		} else {
          	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", myFont, myBGColor));
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
    } // getBKAccountsHTML
  
}
