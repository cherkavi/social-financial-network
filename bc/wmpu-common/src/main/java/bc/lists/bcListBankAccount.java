package bc.lists;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import bc.connection.Connector;
import bc.service.bcFeautureParam;

public class bcListBankAccount extends bc.objects.bcObject {
	private final static Logger LOGGER=Logger.getLogger(bcListBankAccount.class);
	
	public bcListBankAccount() {
		
	}

	public String getCardPackagesHTML(String pWhereCause, ArrayList<bcFeautureParam> pWhereValue, String pNotVisibleData, String pFindString, String pBankAccountType, String pEditLink, String pCopyLink, String pDeleteLink, String p_beg, String p_end) {
		return getBankAccountsHTMLBase(false, pWhereCause, pWhereValue, pNotVisibleData, pFindString, pBankAccountType, pEditLink, pCopyLink, pDeleteLink, p_beg, p_end);
	}
	
	public String getCardPackagesHTMLOnlySelect(String pFindString, String pBankAccountType, String pGoToLink, String p_beg, String p_end) {
		String pWhereCause = "";
    	
    	ArrayList<bcFeautureParam> pWhereValue = new ArrayList<bcFeautureParam>();
    	
    	return getBankAccountsHTMLBase(true, pWhereCause, pWhereValue, "", pFindString, pBankAccountType, pGoToLink, "", "", p_beg, p_end);
	}
	
	public String getBankAccountsHTMLBase(boolean pOnlySelect, String pWhereCause, ArrayList<bcFeautureParam> pWhereValue, String pNotVisibleData, String pFindString, String pBankAccountType, String pEditLink, String pCopyLink, String pDeleteLink, String p_beg, String p_end) {
		StringBuilder html = new StringBuilder();
        Connection con = null;
        PreparedStatement st = null;
        ArrayList<bcFeautureParam> pParam = initParamArray();

        boolean hasPartnerPermission = false;
        boolean hasNatPrsPermission = false;
        boolean hasBankAccountPermission = false;
        
        String mySQL = 
        	"SELECT rn, " + ("OWNER".equalsIgnoreCase(pNotVisibleData)?"":"sname_owner_bank_account, ")+ 
        	"       number_bank_account, name_bank_alt, name_currency, " +
			"		name_bank_account_type, id_bank_account, " +
			"       id_bank, id_owner_bank_account, type_owner_bank_account " +
			"  FROM (SELECT ROWNUM rn, id_bank_account, number_bank_account, name_currency, " +
    		"		        sname_bank name_bank_alt, " +
    		"               DECODE(cd_bank_account_type, " +
            "         	 	        'SETTLEMENT_ACCOUNT', '<font color=\"green\"><b>'||name_bank_account_type||'</b></font>', " +
            "          	 	        'CORRESPONDENT_ACCOUNT', '<font color=\"blue\"><b>'||name_bank_account_type||'</b></font>', " +
            "          		        name_bank_account_type" +
            "  		        ) name_bank_account_type, " +
    		"               sname_owner_bank_account, id_bank, id_owner_bank_account, type_owner_bank_account " +
    		"          FROM (SELECT * " +
    		"                  FROM " + getGeneralDBScheme()+".vc_bank_account_priv_all " +
        	(isEmpty(pWhereCause)?" WHERE 1=1 ":pWhereCause);
        
	   	if (pWhereValue.size() > 0) {
	   		for(int counter=0; counter<=pWhereValue.size()-1;counter++){
	   			pParam.add(pWhereValue.get(counter));
	   		}
	   	}
	        
	   	if (!isEmpty(pFindString)) {
    		mySQL = mySQL + " AND (UPPER(id_bank_account) LIKE UPPER('%'||?||'%') OR " +
    			"UPPER(number_bank_account) LIKE UPPER('%'||?||'%') OR " +
    			"UPPER(sname_bank) LIKE UPPER('%'||?||'%') OR " +
    			"UPPER(desc_bank_account) LIKE UPPER('%'||?||'%') OR " + 
    			"UPPER(name_owner_bank_account) LIKE UPPER('%'||?||'%')) ";
    		for (int i=0; i<5; i++) {
    		    pParam.add(new bcFeautureParam("string", pFindString));
    		}
    	}
	   	if (!isEmpty(pBankAccountType)) {
    		mySQL = mySQL + " AND cd_bank_account_type = ? ";
    		pParam.add(new bcFeautureParam("string", pBankAccountType));
    	}

        pParam.add(new bcFeautureParam("int", p_end));
        pParam.add(new bcFeautureParam("int", p_beg));
        mySQL = mySQL +
            "   ORDER BY sname_owner_bank_account, number_bank_account)  WHERE ROWNUM < ? " + 
            " ) WHERE rn >= ?";
        try{
        	if (isEditMenuPermited("CLIENTS_YURPERSONS")>=0) {
        		hasPartnerPermission = true;
            }
        	if (isEditMenuPermited("CLIENTS_NATPERSONS")>=0) {
        		hasNatPrsPermission = true;
            }
        	if (isEditMenuPermited("CLIENTS_BANK_ACCOUNTS")>=0) {
        		hasBankAccountPermission = true;
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
            for (int i=1; i <= colCount-4; i++) {
            	html.append(getBottomFrameTableTH(accountXML, mtd.getColumnName(i)));
            }
            if (!isEmpty(pDeleteLink) && !pOnlySelect) {
                html.append("<th>&nbsp;</th>\n");
            }
            if (!isEmpty(pCopyLink) && !pOnlySelect) {
                html.append("<th>&nbsp;</th>\n");
            }
            if (!isEmpty(pEditLink) && !pOnlySelect) {
               html.append("<th>&nbsp;</th>\n");
            }
            if (pOnlySelect) {
                html.append("<th>&nbsp;</th>\n");
            }
            html.append("</tr></thead><tbody>");
            
            while (rset.next())
            {
            	html.append("<tr>");
          	  	for (int i=1; i <= colCount-4; i++) {
          	  		if ("NUMBER_BANK_ACCOUNT".equalsIgnoreCase(mtd.getColumnName(i)) &&
          	  			hasBankAccountPermission) {
          	  			if (!pOnlySelect) {
          	  				html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/clients/accountspecs.jsp?id="+rset.getString("ID_BANK_ACCOUNT"), "", ""));
          	  			} else {
          	  				html.append(getBottomFrameTableTDWithDiv(mtd.getColumnName(i), rset.getString(i), pEditLink+"&id_bank_account="+rset.getString("ID_BANK_ACCOUNT"), "", "", "div_data_detail"));
          	  			}
          	  		} else if ("NAME_BANK_ALT".equalsIgnoreCase(mtd.getColumnName(i)) &&
          	  			hasPartnerPermission && !pOnlySelect) {
          	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/clients/yurpersonspecs.jsp?id="+rset.getString("ID_BANK"), "", ""));
          	  		} else if ("SNAME_OWNER_BANK_ACCOUNT".equalsIgnoreCase(mtd.getColumnName(i)) && !pOnlySelect) {
          	  			if ("JUR_PRS".equalsIgnoreCase( rset.getString("TYPE_OWNER_BANK_ACCOUNT")) &&
          	  					hasPartnerPermission) {
          	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/clients/yurpersonspecs.jsp?id="+rset.getString("ID_OWNER_BANK_ACCOUNT"), "", ""));
          	  			} else if ("NAT_PRS".equalsIgnoreCase( rset.getString("TYPE_OWNER_BANK_ACCOUNT")) &&
          	  					hasNatPrsPermission) {
          	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/clients/natpersonspecs.jsp?id="+rset.getString("ID_OWNER_BANK_ACCOUNT"), "", ""));
          	  			}
          	  		} else {
          	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", "", ""));
          	  		}
          	  	}
          	  	if (!isEmpty(pDeleteLink) && !pOnlySelect) {
            	   String myDeleteLink = pDeleteLink+"&id_bank_account="+rset.getString("ID_BANK_ACCOUNT");
                   html.append(getDeleteButtonHTML(myDeleteLink, accountXML.getfieldTransl("h_delete_bank_account", false), rset.getString("NUMBER_BANK_ACCOUNT")));
          	  	}
          	  	if (!isEmpty(pCopyLink) && !pOnlySelect) {
             	   String myCopyLink = pEditLink+"&id_bank_account="+rset.getString("ID_BANK_ACCOUNT");
             	   html.append(getCopyButtonStyleHTML(myCopyLink, "", "div_data_detail"));
                }
          	  	if (!isEmpty(pEditLink) && !pOnlySelect) {
            	   String myEditLink = pEditLink+"&id_bank_account="+rset.getString("ID_BANK_ACCOUNT");
            	   html.append(getEditButtonWitDivHTML(myEditLink, "div_data_detail"));
                }
          	  	if (pOnlySelect) {
             	   String mySelectLink = pEditLink+"&id_bank_account="+rset.getString("ID_BANK_ACCOUNT");
             	   html.append(getSelectButtonStyleHTML(mySelectLink, "", "div_data_detail"));
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
