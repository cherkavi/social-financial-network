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

public class bcPostingLineObject extends bcObject {
	private final static Logger LOGGER=Logger.getLogger(bcPostingLineObject.class);
	private Map<String,Comparable<String>> fieldHm = new HashMap<String, Comparable<String>>();
	
	private String idPostingLine;
	
	public bcPostingLineObject() {}
	
	public bcPostingLineObject(String pIdPostingLine) {
		this.idPostingLine = pIdPostingLine;
		this.getFeature();
	}
	
	public void getFeature() {
		String featureSelect = " SELECT * FROM " + getGeneralDBScheme() + ".VC_ACC_POSTING_LINES_CLUB_ALL WHERE id_posting_line = ?";
		fieldHm = getFeatures2(featureSelect, this.idPostingLine, false);
	}
	
	public String getValue(String pColumnName) {
		return getFeautureResult(fieldHm, pColumnName);
	}
	
    public String getPostingTransHTML(String pFindString, String p_beg, String p_end) {
        StringBuilder html = new StringBuilder();
        Connection con = null;
        PreparedStatement st = null;

        ArrayList<bcFeautureParam> pParam = initParamArray();

        String mySQL = 	
        	" SELECT rn, id_posting_detail, id_posting_line, operation_date, name_currency," +
        	"        oper_number id_bk_operation_scheme_line, debet_id_bk_account, credit_id_bk_account, " +
        	"		 entered_amount, assignment_posting, id_clearing_line, " +
        	"        debet_id_bk_account2, debet_cd_bk_account, debet_name_bk_account, " +
        	"        credit_id_bk_account2, credit_cd_bk_account, credit_name_bk_account, " +
        	"        id_bk_operation_scheme_line id_bk_operation_scheme_line2 " +
			"   FROM (SELECT ROWNUM rn,id_posting_detail, id_posting_line, operation_date_frmt operation_date, " +
        	"                name_currency, oper_number, id_bk_operation_scheme_line, " +
        	"                debet_cd_bk_account||' '||debet_name_bk_account debet_id_bk_account,  " +
        	"        		 credit_cd_bk_account||' '||credit_name_bk_account credit_id_bk_account, " +
        	"		 		 entered_amount_frmt entered_amount, assignment_posting, " +
        	"        		 id_clearing_line, " +
        	"        		 debet_id_bk_account debet_id_bk_account2, " +
        	"                debet_cd_bk_account_frmt debet_cd_bk_account, debet_name_bk_account, " +
        	"        		 credit_id_bk_account credit_id_bk_account2, " +
        	"                credit_cd_bk_account_frmt credit_cd_bk_account, credit_name_bk_account " +
        	"           FROM (SELECT * " +
        	"                   FROM " + getGeneralDBScheme() + ".vc_acc_posting_detail_club_all" +
        	"                  WHERE id_posting_line = ? ";
        pParam.add(new bcFeautureParam("int", this.idPostingLine));
        
	    if (!isEmpty(pFindString)) {
	    	mySQL = mySQL + 
					" AND (TO_CHAR(id_posting_detail) LIKE UPPER('%'||?||'%') OR " +
					"	   UPPER(operation_date_frmt) LIKE UPPER('%'||?||'%') OR " +
					"	   UPPER(oper_number) LIKE UPPER('%'||?||'%') OR " +
					"	   UPPER(debet_cd_bk_account||' '||debet_name_bk_account ) LIKE UPPER('%'||?||'%') OR " +
					"	   UPPER(credit_cd_bk_account||' '||credit_name_bk_account) LIKE UPPER('%'||?||'%') OR " +
					"	   UPPER(entered_amount_frmt) LIKE UPPER('%'||?||'%') OR " +
					"	   UPPER(assignment_posting) LIKE UPPER('%'||?||'%')) ";
	    	for (int i=0; i<7; i++) {
	    	    pParam.add(new bcFeautureParam("string", pFindString));
	    	}
	    }
	    pParam.add(new bcFeautureParam("int", p_end));
	    pParam.add(new bcFeautureParam("int", p_beg));
	    mySQL = mySQL +
        	"          ) WHERE ROWNUM < ?) WHERE rn >= ?";
        
        boolean hasPostingPermission = false;
        boolean hasAccDocPermission = false;
        boolean hasBKAccountPermission = false;
        boolean hasSchemePermission = false;
        try{
        	if (isEditMenuPermited("FINANCE_POSTINGS")>=0) {
        		hasPostingPermission = true;
        	}
        	if (isEditMenuPermited("FINANCE_ACCOUNTING_DOC")>=0) {
        		hasAccDocPermission = true;
        	}
        	if (isEditMenuPermited("FINANCE_BK_ACCOUNTS")>=0) {
        		hasBKAccountPermission = true;
        	}
        	if (isEditMenuPermited("FINANCE_POSTING_SCHEME")>=0) {
        		hasSchemePermission = true;
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
            for (int i=1; i <= colCount-8; i++) {
                html.append(getBottomFrameTableTH(postingXML, mtd.getColumnName(i)));
            }
            html.append("</tr></thead><tbody>");
            while (rset.next())
            {
                html.append("<tr>\n");
                for (int i=1; i <= colCount-8; i++) {
                	if ("ID_POSTING_DETAIL".equalsIgnoreCase(mtd.getColumnName(i))) {
                		if (hasPostingPermission && (!(rset.getString(i) == null || "".equalsIgnoreCase(rset.getString(i))))) {
                			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/finance/postingspecs.jsp?id="+rset.getString(i), "", ""));
                		} else {
                			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", "", ""));
                		}
                	} else if ("ID_POSTING_LINE".equalsIgnoreCase(mtd.getColumnName(i))) {
                		if (hasAccDocPermission && (!(rset.getString(i) == null || "".equalsIgnoreCase(rset.getString(i))))) {
                			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/finance/accounting_docspecs.jsp?type=posting&id_posting="+rset.getString(i), "", ""));
                		} else {
                			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", "", ""));
                		}
                	} else if ("ID_BK_OPERATION_SCHEME_LINE".equalsIgnoreCase(mtd.getColumnName(i))) {
                		if (hasSchemePermission && (!(rset.getString(i) == null || "".equalsIgnoreCase(rset.getString(i))))) {
                			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/finance/posting_scheme_linespecs.jsp?id="+rset.getString("ID_BK_OPERATION_SCHEME_LINE2"), "", ""));
                		} else {
                			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", "", ""));
                		}
                	} else if ("DEBET_ID_BK_ACCOUNT".equalsIgnoreCase(mtd.getColumnName(i))) {
                		if (hasBKAccountPermission && (!(rset.getString(i) == null || "".equalsIgnoreCase(rset.getString(i))))) {
                			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString("DEBET_CD_BK_ACCOUNT"), "../crm/finance/bk_accountspecs.jsp?id="+rset.getString("DEBET_ID_BK_ACCOUNT2"), "", ""));
                		} else {
                			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", "", ""));
                		}
                	} else if ("CREDIT_ID_BK_ACCOUNT".equalsIgnoreCase(mtd.getColumnName(i))) {
                		if (hasBKAccountPermission && (!(rset.getString(i) == null || "".equalsIgnoreCase(rset.getString(i))))) {
                			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString("CREDIT_CD_BK_ACCOUNT"), "../crm/finance/bk_accountspecs.jsp?id="+rset.getString("CREDIT_ID_BK_ACCOUNT2"), "", ""));
                		} else {
                			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", "", ""));
                		}
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
    } //getPostingTransHTML

    public String getPostingFilesHTML(String id_file, String pReportFormat) {
        StringBuilder html = new StringBuilder();
        Connection con = null;
        PreparedStatement st = null;
        ArrayList<bcFeautureParam> pParam = initParamArray();

        String mySQL = 
        	" SELECT id_file, name_file, path_file, desc_file, creation_date_frmt creation_date, " +
        	"	     name_user, record_count "+
        	"   FROM " + getGeneralDBScheme() + ".vc_exchange_files_all "+ 
        	"  WHERE id_file = ? ";
        pParam.add(new bcFeautureParam("int", id_file));
        try{
        	String myReportId = getReportId("SR02");
        	
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
            	html.append(getBottomFrameTableTH(exp_fileXML, mtd.getColumnName(i)));
            }
            html.append("</tr></thead><tbody>");
            while (rset.next())
            {
                html.append("<tr>");
                for (int i=1; i <= colCount; i++) {
                	html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", "", ""));
                }
                if (!isEmpty(myReportId)) {
         	   		String myHyperLink = "../reports/Reporter?REPORT_ID=" + myReportId + "&REPORT_FORMAT="+pReportFormat+"&ID_FILE="+getValue2(rset.getString("ID_FILE"));
         	   		html.append(getReportButtonHTML(myHyperLink, "_blank"));
                } else {
                	html.append("<td>&nbsp;</td>\n");
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
    } //getPostingFilesHTML

}
