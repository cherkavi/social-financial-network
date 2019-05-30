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
import bc.lists.bcListCardOperation;
import bc.service.bcFeautureParam;


public class bcRemittanceObject extends bcObject {
	private final static Logger LOGGER=Logger.getLogger(bcRemittanceObject.class);
	private Map<String,Comparable<String>> fieldHm = new HashMap<String, Comparable<String>>();
	
	private String idRemittance;
	
	public bcRemittanceObject(String pIdRemittance) {
		this.idRemittance = pIdRemittance;
		getFeature();
	}
	
	private void getFeature() {
		String featureSelect = " SELECT * FROM " + getGeneralDBScheme() + ".VC_REMITTANCE_CLUB_ALL WHERE id_remittance = ?";
		fieldHm = getFeatures2(featureSelect, this.idRemittance, false);
	}
	
	public String getValue(String pColumnName) {
		return getFeautureResult(fieldHm, pColumnName);
	}
    
    public String getClubCardsTasksHTML(String pFindString, String pOperType, String pOperState, String p_beg, String p_end) {
    	bcListCardOperation list = new bcListCardOperation();
    	
    	String pWhereCause = "   WHERE id_remittance = ? ";
    	
    	ArrayList<bcFeautureParam> pWhereValue = new ArrayList<bcFeautureParam>();
    	pWhereValue.add(new bcFeautureParam("int", this.idRemittance));

    	String lDeleteLink = "";
    	String lEditLink = "";
    	
    	return list.getCardOperationsHTML(pWhereCause, pWhereValue, pFindString, pOperType, pOperState, lEditLink, lDeleteLink, "div_data_detail", p_beg, p_end);
    	
    }

    public String getPostingsHTML(String idBkOperationType, String p_beg, String p_end) {
        StringBuilder html = new StringBuilder();
        Connection con = null;
        PreparedStatement st = null;
        ArrayList<bcFeautureParam> pParam = initParamArray();

        String mySQL = 	
        	" SELECT rn, id_posting_detail, operation_date_frmt operation_date," +
        	"        oper_number id_bk_operation_scheme_line, " +
        	"        debet_cd_bk_account||' '||debet_name_bk_account debet_id_bk_account,  " +
        	"        credit_cd_bk_account||' '||credit_name_bk_account credit_id_bk_account, " +
        	"		 name_currency, entered_amount_frmt entered_amount, assignment_posting, " +
        	"        id_clearing_line, " +
        	"        debet_id_bk_account debet_id_bk_account2, debet_cd_bk_account, debet_name_bk_account, " +
        	"        credit_id_bk_account credit_id_bk_account2, credit_cd_bk_account, credit_name_bk_account, " +
        	"        id_bk_operation_scheme_line id_bk_operation_scheme_line2, id_posting_line " +
        	"  FROM (SELECT ROWNUM rn, id_posting_detail, id_posting_line, operation_date_frmt, name_currency," +
        	"               id_bk_operation_scheme_line, oper_number, " +
        	"		        entered_amount_frmt, assignment_posting, " +
        	"               id_clearing_line, " +
        	"               debet_id_bk_account, debet_cd_bk_account_frmt debet_cd_bk_account, debet_name_bk_account, " +
        	"               credit_id_bk_account, credit_cd_bk_account_frmt credit_cd_bk_account, credit_name_bk_account " +
        	"          FROM (SELECT * " +
        	"                  FROM "+getGeneralDBScheme() + ".vc_acc_posting_detail_club_all " +
        	"                  WHERE id_remittance = ? ";
        pParam.add(new bcFeautureParam("int", this.idRemittance));
        
        if (!isEmpty(idBkOperationType)) {
        	mySQL = mySQL + " AND id_bk_operation_scheme_line IN (" +
        			" SELECT id_bk_operation_scheme " +
        			"   FROM "+getGeneralDBScheme() + ".vc_oper_scheme_lines_club_all " +
        			"  WHERE cd_bk_operation_type = ?) ";
        	pParam.add(new bcFeautureParam("string", idBkOperationType));
        }
        pParam.add(new bcFeautureParam("int", p_end));
        pParam.add(new bcFeautureParam("int", p_beg));
        mySQL = mySQL + 
        	"         ORDER BY oper_number) WHERE ROWNUM < ? " + 
        	" ) WHERE rn >= ?";
        boolean hasPostingPermission = false;
        //boolean hasAccDocPermission = false;
        boolean hasBKAccountPermission = false;
        boolean hasSchemePermission = false;
        
        boolean hasEditPermission = false;
        try{
        	if (isEditPermited("FINANCE_REMITTANCE_POSTINGS")>=0) {
        		hasEditPermission = true;
        	}
        	if (isEditMenuPermited("FINANCE_POSTINGS")>=0) {
        		hasPostingPermission = true;
        	}
        	//if (isEditMenuPermited("FINANCE_ACCOUNTING_DOC")>=0) {
        	//	hasAccDocPermission = true;
        	//}
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
            for (int i=1; i <= colCount-9; i++) {
                html.append(getBottomFrameTableTH(postingXML, mtd.getColumnName(i)));
            }
            if (hasEditPermission) {
            	html.append("<th>&nbsp;</th><th>&nbsp;</th>\n");
	        }
            html.append("</tr></thead><tbody>");
            while (rset.next())
            {
                html.append("<tr>\n");
                for (int i=1; i <= colCount-9; i++) {
                	if ("ID_POSTING_DETAIL".equalsIgnoreCase(mtd.getColumnName(i))) {
                		if (hasPostingPermission && (!(rset.getString(i) == null || "".equalsIgnoreCase(rset.getString(i))))) {
                			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/finance/postingspecs.jsp?id="+rset.getString(i), "", ""));
                		} else {
                			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", "", ""));
                		}
                	/*} else if ("ID_POSTING_LINE".equalsIgnoreCase(mtd.getColumnName(i))) {
                		if (hasAccDocPermission && (!(rset.getString(i) == null || "".equalsIgnoreCase(rset.getString(i))))) {
                			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/finance/accounting_docspecs.jsp?id="+rset.getString(i), "", ""));
                		} else {
                			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", "", ""));
                		}
                	*/
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
	            if (hasEditPermission && 
		            (rset.getString("ID_POSTING_LINE") == null || "".equalsIgnoreCase(rset.getString("ID_POSTING_LINE"))) && 
		            (rset.getString("ID_CLEARING_LINE") == null || "".equalsIgnoreCase(rset.getString("ID_CLEARING_LINE")))) {
		            	
		            	String myHyperLink = "../crm/finance/remittanceupdate.jsp?type=posting&id="+this.idRemittance+"&id_posting_detail="+rset.getString("ID_POSTING_DETAIL");
		            	String myDeleteLink = "../crm/finance/remittanceupdate.jsp?type=posting&id="+this.idRemittance+"&id_posting_detail="+rset.getString("ID_POSTING_DETAIL")+"&action=remove&process=yes";
		                html.append(getDeleteButtonHTML(myDeleteLink, remittanceXML.getfieldTransl("LAB_DELETE_ONE_POSTING", false), rset.getString("DEBET_CD_BK_ACCOUNT")+ " - " + rset.getString("CREDIT_CD_BK_ACCOUNT")));
		            	html.append(getEditButtonHTML(myHyperLink));
	            } else {
	            	html.append("<td align=center>&nbsp;</td><td align=center>&nbsp;</td>\n");
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
    } //getCardPostingsHTML
}
