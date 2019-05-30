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


public class bcBankAccountObject extends bcObject {
	private final static Logger LOGGER=Logger.getLogger(bcBankAccountObject.class);
	
	private Map<String,Comparable<String>> fieldHm = new HashMap<String, Comparable<String>>();
	
	private String idBankAcccount;
	
	public bcBankAccountObject(String pIdBankAccount) {
		this.idBankAcccount = pIdBankAccount;
		getFeature();
	}
	
	private void getFeature() {
		String featureSelect = " SELECT * FROM " + getGeneralDBScheme() + ".VC_BANK_ACCOUNT_PRIV_ALL WHERE id_bank_account = ?";
		fieldHm = getFeatures2(featureSelect, this.idBankAcccount, false);
	}
	
	public String getValue(String pColumnName) {
		return getFeautureResult(fieldHm, pColumnName);
	}
	
    public String getBKAccountsHTML(String pFind, String p_beg, String p_end) {
    	StringBuilder html = new StringBuilder();
        PreparedStatement st = null;
        Connection con = null;
        
        ArrayList<bcFeautureParam> pParam = initParamArray();
        
        boolean hasBKAccountPermission = false;
        String mySQL = 
        	" SELECT rn, cd_bk_account, name_bk_account, " +
            "        internal_name_bk_account, exist_flag_tsl exist_flag, id_bk_account, exist_flag exist_flag2" +
            "   FROM (SELECT ROWNUM rn, id_bk_account, cd_bk_account, name_bk_account, " +
            "                internal_name_bk_account, exist_flag_tsl, exist_flag " +
            "           FROM (SELECT id_bk_account, cd_bk_account, name_bk_account, " +
            "                        internal_name_bk_account, exist_flag, exist_flag_tsl " +
            "                   FROM " + getGeneralDBScheme() + ".vc_bk_account_bank_account_all "+
            "                  WHERE id_bank_account = ? ";
        pParam.add(new bcFeautureParam("int", this.idBankAcccount));
    	if (!isEmpty(pFind)) {
    		mySQL = mySQL + 
   				" AND (TO_CHAR(id_bk_account) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(cd_bk_account) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(name_bk_account) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(internal_name_bk_account) LIKE UPPER('%'||?||'%'))";
    		for (int i=0; i<4; i++) {
    		    pParam.add(new bcFeautureParam("string", pFind));
    		}
    	}

    	pParam.add(new bcFeautureParam("int", p_end));
    	pParam.add(new bcFeautureParam("int", p_beg));
        mySQL = mySQL +
            "                  ORDER BY cd_bk_account" +
            "                ) " +
            "          WHERE ROWNUM < ?" +
            "         )" +
            "   WHERE rn >= ?";
        
        String myFont = "";

        String myBgColor = noneBackGroundStyle;
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
            for (int i=1; i <= colCount-2; i++) {
               html.append(getBottomFrameTableTH(bk_accountXML, mtd.getColumnName(i)));
            }
            html.append("</tr></thead><tbody>");
            
            while (rset.next())
            {
          	  	if ("Y".equalsIgnoreCase(rset.getString("EXIST_FLAG2"))) {
                		myFont = "";
                		myBgColor = noneBackGroundStyle;
                } else {
                		myFont = "<font color=red>";
                		myBgColor = selectedBackGroundStyle;
                }
          	  	
          	  	html.append("<tr>");
          	  	for (int i=1; i <= colCount-2; i++) {
          	  		if (hasBKAccountPermission && 
          	  				("CD_BK_ACCOUNT".equalsIgnoreCase(mtd.getColumnName(i)))) {
          	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/finance/bk_accountspecs.jsp?id=" + rset.getString("ID_BK_ACCOUNT"), myFont, myBgColor));
         	  		} else {
          	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", myFont, myBgColor));
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
    } // getBKAccountsHTML

    public String getDocumentsHTML(String pFind, String pDocType, String pClubRelType, String pDocBankAccountType, String p_beg, String p_end) {
    	StringBuilder html = new StringBuilder();
    	PreparedStatement st = null;
	    Connection con = null;

	    ArrayList<bcFeautureParam> pParam = initParamArray();
	    String mySQL = 
	    	" SELECT rn, number_doc, date_doc_frmt, name_doc, " +
           	"        parties_doc, name_doc_type, name_club_rel_type club_rel_type, " +
           	"        name_doc_bank_account_type, id_doc_bank_account, id_doc " +
      	  	"   FROM (SELECT ROWNUM rn, number_doc, date_doc_frmt, name_doc, " +
           	"                parties_doc, name_doc_type, name_club_rel_type, " +
           	"                DECODE(cd_doc_bank_account_type, " +
            "         	 	        'SETTLEMENT_ACCOUNT', '<font color=\"green\"><b>'||name_doc_bank_account_type||'</b></font>', " +
            "          	 	        'CORRESPONDENT_ACCOUNT', '<font color=\"blue\"><b>'||name_doc_bank_account_type||'</b></font>', " +
            "          		        name_doc_bank_account_type" +
            "  		         ) name_doc_bank_account_type, id_doc_bank_account, id_doc " +
           	"           FROM (SELECT number_doc, date_doc_frmt, name_doc, " +
           	"                        parties_doc, name_doc_type, name_club_rel_type, cd_doc_bank_account_type, " +
           	"                        name_doc_bank_account_type, id_doc_bank_account, id_doc " +
           	"                   FROM " + getGeneralDBScheme() + ".vc_doc_bank_account_all "+
           	"                  WHERE id_bank_account = ?";
	    pParam.add(new bcFeautureParam("int", this.idBankAcccount));
    	if (!isEmpty(pFind)) {
    		mySQL = mySQL + 
   				" AND (TO_CHAR(number_doc) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(date_doc_frmt) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(name_doc) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(parties_doc) LIKE UPPER('%'||?||'%'))";
        	for (int i=0; i<4; i++) {
        	    pParam.add(new bcFeautureParam("string", pFind));
        	}
    	}
    	if (!isEmpty(pDocType)) {
    		mySQL = mySQL + " AND cd_doc_type = ?";
        	pParam.add(new bcFeautureParam("string", pDocType));
    	}
    	if (!isEmpty(pClubRelType)) {
    		mySQL = mySQL + " AND cd_club_rel_type = ?";
        	pParam.add(new bcFeautureParam("string", pClubRelType));
    	}
    	if (!isEmpty(pDocBankAccountType)) {
    		mySQL = mySQL + " AND cd_doc_bank_account_type = ?";
        	pParam.add(new bcFeautureParam("string", pDocBankAccountType));
    	}
    	pParam.add(new bcFeautureParam("int", p_end));
    	pParam.add(new bcFeautureParam("int", p_beg));
	    mySQL = mySQL +
          	"         ) WHERE ROWNUM < ?" + 
          	" ) WHERE rn >= ?";
	    
	    boolean hasDocumentPermission = false;
	    try{
	    	if (isEditMenuPermited("CLUB_DOCUMENTS")>=0) {
	    		hasDocumentPermission = true;
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
               html.append(getBottomFrameTableTH(documentXML, mtd.getColumnName(i)));
            }
            html.append("</tr></thead><tbody>");

	        while (rset.next()) {
          	  	html.append("<tr>");
          	  	for (int i=1; i <= colCount-2; i++) {
          	  		if (hasDocumentPermission && 
          	  				("NUMBER_DOC".equalsIgnoreCase(mtd.getColumnName(i)))) {
          	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/club/documentspecs.jsp?id="+rset.getString("ID_DOC"), "", ""));
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
	  } // getRelationShipsHTML


    public String getRestsHTML(String pBeginPeriod, String pEndPeriod, String pFind, String p_beg, String p_end) {
        StringBuilder html = new StringBuilder();
        Connection con = null;
        PreparedStatement st = null; 

        ArrayList<bcFeautureParam> pParam = initParamArray();

        String mySQL = 
	        	"SELECT rn, accounting_date_frmt, name_currency, " +
	            "       need_begin_balance_frmt, need_receiv_amount_frmt, need_pay_amount_frmt, " +
	            "       need_end_balance_frmt, confirm_received_amount_frmt, confirm_payed_amount_frmt " +
	            "  FROM (SELECT ROWNUM rn, accounting_date_frmt, name_currency, " +
	            "               need_begin_balance_frmt, need_receiv_amount_frmt, need_pay_amount_frmt, " +
	            "               need_end_balance_frmt, confirm_received_amount_frmt, confirm_payed_amount_frmt" +
	            "          FROM (SELECT accounting_date_frmt, name_currency, " +
	            "                       CASE WHEN need_begin_balance > 0 THEN '<font color=\"green\"><b>'||need_begin_balance_frmt||'</b></font>' " +
	            "                            WHEN need_begin_balance < 0 THEN '<font color=\"blue\"><b>'||need_begin_balance_frmt||'</b></font>' " +
	    		"                            ELSE need_begin_balance_frmt " +
	    		"                       END need_begin_balance_frmt, " +
	            "                       CASE WHEN need_receiv_amount > 0 THEN '<font color=\"green\"><b>'||need_receiv_amount_frmt||'</b></font>' " +
	            "                            WHEN need_receiv_amount < 0 THEN '<font color=\"blue\"><b>'||need_receiv_amount_frmt||'</b></font>' " +
	    		"                            ELSE need_receiv_amount_frmt " +
	    		"                       END need_receiv_amount_frmt, " +
	    		"                       CASE WHEN need_pay_amount > 0 THEN '<font color=\"green\"><b>'||need_pay_amount_frmt||'</b></font>' " +
	            "                            WHEN need_pay_amount < 0 THEN '<font color=\"blue\"><b>'||need_pay_amount_frmt||'</b></font>' " +
	    		"                            ELSE need_pay_amount_frmt " +
	    		"                       END need_pay_amount_frmt, " +
	    		"                       CASE WHEN need_end_balance > 0 THEN '<font color=\"green\"><b>'||need_end_balance_frmt||'</b></font>' " +
	            "                            WHEN need_end_balance < 0 THEN '<font color=\"blue\"><b>'||need_end_balance_frmt||'</b></font>' " +
	    		"                            ELSE need_begin_balance_frmt " +
	    		"                       END need_end_balance_frmt, " +
	            "                       CASE WHEN confirm_received_amount > 0 THEN '<font color=\"green\"><b>'||confirm_received_amount_frmt||'</b></font>' " +
	            "                            WHEN confirm_received_amount < 0 THEN '<font color=\"blue\"><b>'||confirm_received_amount_frmt||'</b></font>' " +
	    		"                            ELSE confirm_received_amount_frmt " +
	    		"                       END confirm_received_amount_frmt, " +
	            "                       CASE WHEN confirm_payed_amount > 0 THEN '<font color=\"green\"><b>'||confirm_payed_amount_frmt||'</b></font>' " +
	            "                            WHEN confirm_payed_amount < 0 THEN '<font color=\"blue\"><b>'||confirm_payed_amount_frmt||'</b></font>' " +
	    		"                            ELSE confirm_payed_amount_frmt " +
	    		"                       END confirm_payed_amount_frmt " +
	            "                  FROM " + getGeneralDBScheme() + ".vc_bank_account_rests_club_all " +
	            "                 WHERE id_bank_account = ? ";
        pParam.add(new bcFeautureParam("int", this.idBankAcccount));
        if (!isEmpty(pBeginPeriod)) {
        	mySQL = mySQL + " and accounting_date >= TO_DATE(?,?) ";
        	pParam.add(new bcFeautureParam("string", pBeginPeriod));
        	pParam.add(new bcFeautureParam("string", this.getDateFormat()));
        }
        if (!isEmpty(pEndPeriod)) {
        	mySQL = mySQL + " and accounting_date <= TO_DATE(?,?) ";
        	pParam.add(new bcFeautureParam("string", pEndPeriod));
        	pParam.add(new bcFeautureParam("string", this.getDateFormat()));
        }
        if (!isEmpty(pFind)) {
        	mySQL = mySQL + 
				" AND (UPPER(accounting_date_frmt) LIKE UPPER('%'||?||'%') OR " +
				"      UPPER(need_begin_balance_frmt) LIKE UPPER('%'||?||'%') OR " +
				"      UPPER(need_receiv_amount_frmt) LIKE UPPER('%'||?||'%') OR " +
				"      UPPER(need_pay_amount_frmt) LIKE UPPER('%'||?||'%') OR " +
				"      UPPER(need_end_balance_frmt) LIKE UPPER('%'||?||'%') OR " +
				"      UPPER(confirm_received_amount_frmt) LIKE UPPER('%'||?||'%') OR " +
				"      UPPER(confirm_payed_amount_frmt) LIKE UPPER('%'||?||'%')) ";
        	for (int i=0; i<5; i++) {
        	    pParam.add(new bcFeautureParam("string", pFind));
        	}
		}
        pParam.add(new bcFeautureParam("int", p_end));
        pParam.add(new bcFeautureParam("int", p_beg));
        mySQL = mySQL + " ORDER BY TRUNC(accounting_date) DESC " +
        	"    ) WHERE ROWNUM < ? " + 
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
            html.append(getBottomFrameTable());
            html.append("<tr>");
            html.append("<th rowspan=\"2\">"+ commonXML.getfieldTransl("RN", false)+"</th>\n");
            html.append("<th rowspan=\"2\">"+ accountXML.getfieldTransl("ACCOUNTING_DATE", false)+"</th>\n");
            html.append("<th rowspan=\"2\">"+ accountXML.getfieldTransl("NAME_CURRENCY", false)+"</th>\n");
            html.append("<th colspan=\"4\">"+ accountXML.getfieldTransl("NEED_BALANCE", false)+"</th>\n");
            html.append("<th colspan=\"2\">"+ accountXML.getfieldTransl("CONFIRM_RESTS", false)+"</th>\n");
            html.append("</tr>");
            html.append("<tr>");
            html.append("<th>"+ accountXML.getfieldTransl("NEED_BEGIN_BALANCE", false)+"</th>\n");
            html.append("<th>"+ accountXML.getfieldTransl("NEED_RECEIV_AMOUNT", false)+"</th>\n");
            html.append("<th>"+ accountXML.getfieldTransl("NEED_PAY_AMOUNT", false)+"</th>\n");
            html.append("<th>"+ accountXML.getfieldTransl("NEED_END_BALANCE", false)+"</th>\n");
            html.append("<th>"+ accountXML.getfieldTransl("CONFIRM_RECEIVED_AMOUNT", false)+"</th>\n");
            html.append("<th>"+ accountXML.getfieldTransl("CONFIRM_PAYED_AMOUNT", false)+"</th>\n");
            html.append("</tr></thead><tbody>");
            html.append("<tr>");
            html.append("</tr></thead><tbody>\n");
            
            while (rset.next())
                {
                    html.append("<tr>");
              	  	for (int i=1; i <= colCount; i++) {
              	  		html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", "", ""));
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
       }
}
