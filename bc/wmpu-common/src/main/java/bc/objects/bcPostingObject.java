package bc.objects;

import java.net.URLEncoder;
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

public class bcPostingObject extends bcObject {
	private final static Logger LOGGER=Logger.getLogger(bcPostingObject.class);
	private Map<String,Comparable<String>> fieldHm = new HashMap<String, Comparable<String>>();
	
	private String idPosting;
	
	public bcPostingObject() {}
	
	public bcPostingObject(String pIdPosting) {
		this.idPosting = pIdPosting;
	}
	
	public void getFeature() {
		String featureSelect = " SELECT * FROM " + getGeneralDBScheme() + ".VC_ACC_POSTING_HEADER_CLUB_ALL WHERE id_posting = ?";
		fieldHm = getFeatures2(featureSelect, this.idPosting, false);
	}
	
	public String getValue(String pColumnName) {
		return getFeautureResult(fieldHm, pColumnName);
	}
	
    public String getPostingLinesHTML(String pIdLine, String pFindString, String p_beg, String p_end) {
        StringBuilder html = new StringBuilder();
        Connection con = null;
        PreparedStatement st = null;

        ArrayList<bcFeautureParam> pParam = initParamArray();

        String mySQL = 	
        	" SELECT rn, id_posting_line, operation_date, name_currency," +
			"        debet_id_bk_account, credit_id_bk_account, " +
			"		 entered_amount,  " +
			"        state_posting_line_tsl, da_id, ca_id, " +
			"        debet_cd_bk_account, debet_name_bk_account, " +
			"        credit_cd_bk_account, credit_name_bk_account " +
			"   FROM (SELECT ROWNUM rn, id_posting_line, operation_date_frmt operation_date, name_currency," +
        	"                debet_cd_bk_account_frmt debet_cd_bk_account, debet_name_bk_account, " +
        	"                debet_cd_bk_account_frmt||' '||debet_name_bk_account debet_id_bk_account,  " +
        	"                credit_cd_bk_account_frmt credit_cd_bk_account, credit_name_bk_account, " +
        	"                credit_cd_bk_account_frmt||' '||credit_name_bk_account credit_id_bk_account, " +
        	"		         entered_amount_frmt entered_amount, " +
        	"                state_posting_line_tsl, debet_id_bk_account da_id, credit_id_bk_account ca_id " +
        	"           FROM (SELECT * " +
        	"                   FROM " + getGeneralDBScheme() + ".vc_acc_posting_lines_club_all " +
        	"                  WHERE id_posting = ?";
        pParam.add(new bcFeautureParam("int", this.idPosting));

	    if (!isEmpty(pFindString)) {
	    	mySQL = mySQL + 
					" AND (TO_CHAR(id_posting_line) LIKE UPPER('%'||?||'%') OR " +
					"	   UPPER(operation_date_frmt) LIKE UPPER('%'||?||'%') OR " +
					"	   UPPER(debet_cd_bk_account_frmt||' '||debet_name_bk_account) LIKE UPPER('%'||?||'%') OR " +
					"	   UPPER(credit_cd_bk_account_frmt||' '||credit_name_bk_account) LIKE UPPER('%'||?||'%') OR " +
					"	   UPPER(entered_amount_frmt) LIKE UPPER('%'||?||'%')) ";
	    	for (int i=0; i<5; i++) {
	    		pParam.add(new bcFeautureParam("string", pFindString));
	    	}
	    }
        if (!isEmpty(pIdLine)) {
        	mySQL = mySQL + " AND id_posting_line = ? ";
        	pParam.add(new bcFeautureParam("int", pIdLine));
        }
        pParam.add(new bcFeautureParam("int", p_end));
        pParam.add(new bcFeautureParam("int", p_beg));
        mySQL = mySQL + ") WHERE ROWNUM < ?) WHERE rn >= ?";
        
        boolean hasBKPermission = false;
        try{
        	if (isEditMenuPermited("FINANCE_POSTINGS")>=0) {
        		hasBKPermission = true;
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
            html.append("<th>&nbsp;</th>");
            for (int i=1; i <= colCount-6; i++) {
            	html.append(getBottomFrameTableTH(postingXML, mtd.getColumnName(i)));
            }
            html.append("</tr></thead><tbody>");
            while (rset.next())
            {
                html.append("<tr>\n");
            	if (isEmpty(pIdLine)) {
	               	html.append("<td align=\"center\">");
					html.append(getHyperLinkFirst()+"../crm/finance/accounting_docspecs.jsp?type=doc&id_doc="+this.idPosting+"&id_posting="+rset.getString("ID_POSTING_LINE")+"&detail_page=1" +getHyperLinkMiddle());
					html.append("<img vspace=\"0\" hspace=\"0\" src=\"../images/oper/rows/postings.png\" width=\"16\" height=\"16\" align=\"top\" style=\"border: 0px;\" title=\""+ reportXML.getfieldTransl( "t_content", false) +"\">");
					html.append(getHyperLinkEnd() + "</td>\n");
               	} else {
	               	html.append("<td align=\"center\">");
					html.append(getHyperLinkFirst()+"../crm/finance/accounting_docspecs.jsp?type=doc&id_doc="+this.idPosting+getHyperLinkMiddle());
					html.append("<img vspace=\"0\" hspace=\"0\" src=\"../images/oper/rows/postings.png\" width=\"16\" height=\"16\" align=\"top\" style=\"border: 0px;\" title=\""+ reportXML.getfieldTransl( "t_content", false) +"\">");
					html.append(getHyperLinkEnd() + "</td>\n");
               	}
                for (int i=1; i <= colCount-6; i++) {
                	if ("ID_POSTING_LINE".equalsIgnoreCase(mtd.getColumnName(i))) {
                		html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/finance/accounting_docspecs.jsp?type=posting&id_posting="+rset.getString(i), "", ""));
                	} else if ("DEBET_ID_BK_ACCOUNT".equalsIgnoreCase(mtd.getColumnName(i))) {
                		if (hasBKPermission) {
                			html.append(getBottomFrameTableTD("DEBET_NAME_BK_ACCOUNT", rset.getString("DEBET_NAME_BK_ACCOUNT"), "../crm/finance/bk_accountspecs.jsp?id="+rset.getString("DA_ID"), "", ""));
                		} else {
                			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", "", ""));
                		}
                	} else if ("CREDIT_ID_BK_ACCOUNT".equalsIgnoreCase(mtd.getColumnName(i))) {
                		if (hasBKPermission) {
                			html.append(getBottomFrameTableTD("CREDIT_NAME_BK_ACCOUNT", rset.getString("CREDIT_NAME_BK_ACCOUNT"), "../crm/finance/bk_accountspecs.jsp?id="+rset.getString("CA_ID"), "", ""));
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
    } //getPostingLinesHTML

    public String getPostingFilesHTML(String idFile, String pFindString, String p_beg, String p_end) {
    	StringBuilder html = new StringBuilder();
    	PreparedStatement st = null;
        Connection con = null;

        ArrayList<bcFeautureParam> pParam = initParamArray();

        String mySQL = 
        	" SELECT id_file, name_file, data_format, record_count, creation_date, name_user, path_file "+
            "   FROM (SELECT ROWNUM rn, id_file, name_file, data_format, " +
            "                creation_date_time_frmt creation_date, name_user, record_count, path_file "+
            "           FROM (SELECT * " +
            "                   FROM " + getGeneralDBScheme() + ".vc_exchange_files_all "+ 
            "                  WHERE id_entry = ? " + 
            "                    AND type_entry IN ('POSTING') ";
        pParam.add(new bcFeautureParam("int", this.idPosting));
        
	    if (!isEmpty(pFindString)) {
	    	mySQL = mySQL + 
					" AND (TO_CHAR(id_file) LIKE UPPER('%'||?||'%') OR " +
					"	   UPPER(name_file) LIKE UPPER('%'||?||'%') OR " +
					"	   UPPER(data_format) LIKE UPPER('%'||?||'%') OR " +
					"	   UPPER(creation_date_time_frmt) LIKE UPPER('%'||?||'%') OR " +
					"	   UPPER(path_file) LIKE UPPER('%'||?||'%') OR " +
					"	   UPPER(name_user) LIKE UPPER('%'||?||'%')) ";
	    	for (int i=0; i<6; i++) {
	    	    pParam.add(new bcFeautureParam("string", pFindString));
	    	}
	    }
        if (!isEmpty(idFile)) {
        	mySQL = mySQL + " AND id_file = ? ";
        	pParam.add(new bcFeautureParam("int", idFile));
        }
        pParam.add(new bcFeautureParam("int", p_end));
        pParam.add(new bcFeautureParam("int", p_beg));
        mySQL = mySQL +
        	"                  ORDER BY id_file desc " +
            "                ) WHERE ROWNUM < ? " + 
            " ) WHERE rn >= ?";
        try{
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
            	html.append(getBottomFrameTableTH(exp_fileXML, mtd.getColumnName(i)));
            }
            html.append("</tr></thead><tbody>");
            while (rset.next())
            {
                html.append("<tr>");
				
               	for (int i=1; i <= colCount-1; i++) {
               		if ("NAME_FILE".equalsIgnoreCase(mtd.getColumnName(i))) {
               			html.append("<td><a href=\"../FileSender?FILENAME=" + 
               					URLEncoder.encode(rset.getString("path_file") + "/" + rset.getString("name_file"),"UTF-8") + "\" title=\"" + 
               					buttonXML.getfieldTransl("open", false) + 
               					"\" target=_blank>" + rset.getString("name_file") + "</a></td>\n");
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
    } //getPostingFilesHTML

}
