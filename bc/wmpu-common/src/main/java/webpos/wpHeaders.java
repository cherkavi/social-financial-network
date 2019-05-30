package webpos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import bc.objects.*;
import bc.service.*;
import bc.connection.*;

public class wpHeaders { 
	protected static Connection con = null;
	private wpObject object = new wpObject();
	
	private final static Logger LOGGER=Logger.getLogger(wpHeaders.class);
	
    //private String language;
	private String idClub;
	
	public wpHeaders(String pDateFormat){
		object.setDateFormat(pDateFormat);		
	}
	
	//public void setLanguage(String pLanguage) {
	//	this.language = pLanguage.toUpperCase();
	//}
	
	public void setIdClub(String pIdClub){
		idClub = pIdClub;
	}
    
    private String returnRowCount = "0";
    
    public String getLastResultSetRowCount() {
    	return returnRowCount;
    }
    
    private void setLastResultSetRowCount(String pRowCount) {
    	returnRowCount = pRowCount;
    	//LOGGER.debug("returnRowCount=" + returnRowCount);
    }
	
	public String getIdClubFieldString(String commaPosition) {
		String returnValue = "";
		if (this.idClub == null || "".equalsIgnoreCase(this.idClub)) {
			returnValue = "id_club";
			if ("FIRST".equalsIgnoreCase(commaPosition)) {
				returnValue = ", " + returnValue;
			} else if ("LAST".equalsIgnoreCase(commaPosition)) {
				returnValue =  returnValue + ", ";
			}
		}
		return returnValue;
	}

	public void setSessionId(String pSessionId) {
		object.setSessionId(pSessionId);
	}
	
	public String getHeaderTable(){
		return "<table width=100% class=\"tablesorter\" id=\"id_table\"\n>";
	}
	
	public String getPrintTable(){
		return "<table class=\"tablebottom\"\n>";
	}
	
	private String deleteHyperLink = "";
	private String deleteConfirmMessage = "";
	private String deleteEntryId = "";
	private String deleteEntryName = "";
	
	public void setDeleteHyperLink(String pHyperLink, String pMessage, String pEntryId, String pEntryName) {
		deleteHyperLink = pHyperLink;
		deleteConfirmMessage = pMessage;
		deleteEntryId = pEntryId;
		deleteEntryName = pEntryName;
	}
	
	public void setDeleteHyperLink(String pHyperLink, String pMessage, String pEntryId) {
		deleteHyperLink = pHyperLink;
		deleteConfirmMessage = pMessage;
		deleteEntryId = pEntryId;
		deleteEntryName = pEntryId;
	}
	
	public String getHeaderHTML(String pSQL, int pLessColumn, String pCompareType, int pColumn, String pValue, String pHyperLink, bcXML myXML, String pPrint) {
		ArrayList<bcFeautureParam> pParam = object.initParamArray();

		pParam.add(new bcFeautureParam("none", ""));
		
		return this.getHeaderExtendHTML(pSQL, pParam, pLessColumn, pCompareType, pColumn, pValue, pHyperLink, myXML, pPrint, false, "", "", "");
	}
	
	public String getHeaderParamHTML(String pSQL, ArrayList<bcFeautureParam> pParam, int pLessColumn, String pCompareType, int pColumn, String pValue, String pHyperLink, bcXML myXML, String pPrint) {
		return this.getHeaderExtendHTML(pSQL, pParam, pLessColumn, pCompareType, pColumn, pValue, pHyperLink, myXML, pPrint, false, "", "", "");
	}
	
	public String getHeaderExtendHTML(String pSQL, ArrayList<bcFeautureParam> pParam, int pLessColumn, String pCompareType, int pColumn, String pValue, String pDetailHyperLink, bcXML myXML, String pPrint, boolean pExtend, String pSortHyperLink, String pOrderField, String pOrderType) {
        StringBuilder html = new StringBuilder();
        
        PreparedStatement st = null;
        
        String myHyperLink = "";

        String myFont = "<font>";
        String myFontEnd = "</font>";
        String myBgColor = object.noneBackGroundStyle;
        
        boolean isSetRowCount = true;

        try{
        	setLastResultSetRowCount("0");
        	if ("Y".equalsIgnoreCase(pPrint)) {
        		deleteHyperLink = "";
        	}
        	
        	LOGGER.debug(pSQL);
        	con = Connector.getConnection(object.getSessionId());
        	
            st = con.prepareStatement(pSQL);
            st = object.prepareParam(st, pParam);
            
            ResultSet rset = st.executeQuery();
            ResultSetMetaData mtd = rset.getMetaData();
            
            int colCount = mtd.getColumnCount();
            if ("Y".equalsIgnoreCase(pPrint)) {
            	html.append(getPrintTable());
            } else {
            	html.append(getHeaderTable());
            }
            html.append("<thead><tr>\n");
            
            int lessColumn = 0;

            for (int i=1; i <= colCount-pLessColumn; i++) {
            	String colName = mtd.getColumnName(i);
            	if (!pExtend) {
	            	if ("RN".equalsIgnoreCase(colName) ||
	            		"CREATION_DATE".equalsIgnoreCase(colName) ||
	            		"CREATION_DATE_FRMT".equalsIgnoreCase(colName) ||
	            		"LAST_UPDATE_DATE".equalsIgnoreCase(colName) ||
	            		"LAST_UPDATE_DATE_FRMT".equalsIgnoreCase(colName)) {
	            		html.append("<th>"+ object.commonXML.getfieldTransl(colName, false)+"</th>\n");
	            	} else if ("CLUB_NAME".equalsIgnoreCase(colName)) {
	            		html.append("<th>"+ object.clubXML.getfieldTransl("club", false)+"</th>\n");
	            	} else if ("ID_CLUB".equalsIgnoreCase(colName)) {
	            		html.append("<th>"+ object.clubXML.getfieldTransl("id_club", false)+"</th>\n");
	            	} else {
	            		html.append("<th>"+ myXML.getfieldTransl(colName, false)+"</th>\n");
	            	}
            	} else {
            		if ("RN".equalsIgnoreCase(colName)) {
            			lessColumn = 1;
            			html.append("<th>"+ object.commonXML.getfieldTransl(colName, false)+"</th>\n");
    	           	} else {
    	           		html.append(object.getBottomFrameTableExtendedTH(
  	        			  object.club_actionXML,
  	        			  i - lessColumn,
  	        			  mtd.getColumnName(i), 
  	        			  pOrderField,
  	        			  pOrderType,
  	        			  pSortHyperLink));
    	           	}
            	}
            }
            for (int i=1; i <= colCount; i++) {
            	if ("ROW_COUNT".equalsIgnoreCase(mtd.getColumnName(i))) {
            		isSetRowCount = false;
            	}
            }
            if (!(deleteHyperLink==null || "".equalsIgnoreCase(deleteHyperLink))) {
            	html.append("<th>&nbsp;</th>\n");
            }
            html.append("</tr></thead><tbody>\n");
            
            while (rset.next())
            {
            	if (!isSetRowCount) {
            		setLastResultSetRowCount(rset.getString("ROW_COUNT"));
            		isSetRowCount = true;
            	}
            	if ("RN".equalsIgnoreCase(mtd.getColumnName(1))) {
            		html.append("<tr id=\"elem_"+rset.getString(2)+"\" onmouseout=\"coloredOut(this)\" onmouseover=\"coloredOver(this,1)\">");
            	} else {
            		html.append("<tr id=\"elem_"+rset.getString(1)+"\" onmouseout=\"coloredOut(this)\" onmouseover=\"coloredOver(this,1)\">");
            	}
            	
            	
            	String deleteId = "";
            	String deleteName = "";
            	
            	myHyperLink = pDetailHyperLink;
            	for (int i=1; i <= colCount; i++) {
            		if (rset.getString(i)==null) {
            			myHyperLink = myHyperLink.replace(":FIELD"+i+":", "");
            		} else {
            			myHyperLink = myHyperLink.replace(":FIELD"+i+":", rset.getString(i));
            		}
            	}
            	
            	if ("N".equalsIgnoreCase(pCompareType)) {
            		if (pValue.equalsIgnoreCase(rset.getString(pColumn))) {
            			myFont = "<font color=\"red\">";
            			myFontEnd = "</font>";
            			myBgColor = object.selectedBackGroundStyle;
            		} else {
            			myFont = "<font color=\"black\">";
            			myFontEnd = "</font>";
            			myBgColor = object.noneBackGroundStyle;
            		}
            	} else if ("P".equalsIgnoreCase(pCompareType)) {
            		if (pValue.equalsIgnoreCase(rset.getString(pColumn))) {
            			myFont = "<font color=\"black\">";
            			myFontEnd = "</font>";
            			myBgColor = object.noneBackGroundStyle;
            		} else {
            			myFont = "<font color=\"red\">";
            			myFontEnd = "</font>";
            			myBgColor = object.selectedBackGroundStyle;
            		}
            	} else {
            		myFont = "<font color=\"black\">";
        			myFontEnd = "</font>";
        			myBgColor = object.noneBackGroundStyle;
            	}
            	
            	for (int i=1; i <= colCount; i++) {
            		if (deleteEntryId.equalsIgnoreCase(mtd.getColumnName(i))) {
            			deleteId = object.getHTMLValue(rset.getString(i));
            		}
            		if (deleteEntryName.equalsIgnoreCase(mtd.getColumnName(i))) {
            			deleteName = object.getHTMLValue(rset.getString(i));
            		}
            	}
            	//String columnName = "";
            	for (int i=1; i <= colCount-pLessColumn; i++) {
            		html.append(object.formatTDData(mtd.getColumnName(i), mtd.getColumnType(i), myBgColor));
            		/*
            		switch (mtd.getColumnType(i)) {
						case Types.DATE:
						case Types.TIMESTAMP:
						case Types.NUMERIC:
						case Types.INTEGER:
						case Types.DECIMAL:
							html.append("<td align=\"center\" "+myBgColor+">");
							break;
						case Types.VARCHAR:
							columnName = mtd.getColumnName(i);
							if (columnName.contains("FRMT") || 
								columnName.contains("PERCENT") || 
								columnName.contains("TSL") || 
								columnName.contains("DATE") || 
								columnName.contains("NUMBER") || 
								columnName.contains("HEX")) {
								html.append("<td align=\"center\" "+myBgColor+">");
							} else {
								html.append("<td "+myBgColor+">");
							}
							break;
						default:
							html.append("<td "+myBgColor+">");
					} // switch ()
					*/
					//if ("RN".equalsIgnoreCase(mtd.getColumnName(i))) {
					//	html.append(getValue2(rset.getString(i)) + "</td>\n");
					//} else {
						if ("Y".equalsIgnoreCase(pPrint)) {
							html.append(getValue2(rset.getString(i)) + "</td>\n");
						} else {
							String pTempValue = "";
							if ("ID_CLUB".equalsIgnoreCase(mtd.getColumnName(i))) {
								pTempValue = "<font color=\"blue\"><b>" + getValue2(rset.getString(i)) + "</b></font>";
							} else {
								pTempValue = getValue2(rset.getString(i));
							}
							html.append(object.getHyperLinkHeaderFirst() + myHyperLink +  object.getHyperLinkMiddle() + myFont + pTempValue + myFontEnd + object.getHyperLinkEnd() + "</td>\n");
						}
					//}
            	}
            	if (!(deleteHyperLink==null || "".equalsIgnoreCase(deleteHyperLink))) {
                	html.append("<td align=\"center\"><div class=\"div_button\" onclick=\"var msg='" + deleteConfirmMessage + " \\\'" + deleteName + "\\\'?';var res=window.confirm(msg);if (res) {ajaxpage('"+deleteHyperLink + deleteId + "','div_main');}\">" + "<img vspace=\"0\" hspace=\"0\"" +
            				" src=\"../images/row_del_v.GIF\" width=\"16\" height=\"16\" align=\"top\" style=\"border: 0px;\"" + 
            				" title=\"" + object.buttonXML.getfieldTransl("button_delete", false)+"\">" + object.getHyperLinkEnd()+"</td>\n");
                }
                html.append("</tr>\n");
            }
            html.append("</tbody></table>\n");
            setDeleteHyperLink("","",""); 
        } // try
        catch (SQLException e) {LOGGER.error(html, e);}
        catch (Exception el) {LOGGER.error(html, el);}
        finally {
            try {
                if (st!=null) st.close();
            } catch (SQLException w) {w.toString();}
            try {
                if (con!=null) con.close();
            } catch (SQLException w) {w.toString();}
            Connector.closeConnection(con);
        } // finally
        
        deleteHyperLink = "";
		deleteConfirmMessage = "";
		deleteEntryId = "";
		deleteEntryName = "";
        LOGGER.debug("END");
        return html.toString();
    }
    
    public String getValue2(String pValue){ // TEMPORARY FOR DEMO!!
    	String result;
    	if (pValue == null || "null".equalsIgnoreCase(pValue)) {
    		result = "&nbsp;";
    	} else {
    		result = pValue;
    		if (result.length()>=5) {
	    		if (!("<span".equalsIgnoreCase(result.substring(0,5)) ||
	    				"<font".equalsIgnoreCase(result.substring(0,5)) ||
	    				"<b>".equalsIgnoreCase(result.substring(0,3)))) {
	    			result = result.replace("&", "&amp;");
	    			result = result.replace("<", "&lt;");
	    			result = result.replace(">", "&gt;");
	    			result = result.replace("~", "&tilde;");
	    		}
    		}
    	}
    	return result;
    } // end of getValue2
    
    public String getValue3(String pValue){
    	String result;
    	if (pValue == null || "null".equalsIgnoreCase(pValue)) {
    		result = "";
    	} else {
    		result = pValue;    		
    	}
    	return result;
    } // end of getValue3
    

    
    public String getTransCollectionHeadHTML(String pIdTerm, String pFind, String pState, String p_beg, String p_end, String pPrint) {
    	object.readDateFormat();
    	ArrayList<bcFeautureParam> pParam = object.initParamArray();
        
    	String mySQL = 
        	"SELECT rn, id_trans_collection, state_trans_collection_tsl, " +
        	"       send_date_trans_collect_frmt, rec_all_count, rec_payment_all_count, " +
        	"       rec_mov_bon_count, rec_chk_card_count, rec_inval_card_count, " +
        	"       rec_storno_bon_count, min_trans_sys_date_frmt, max_trans_sys_date_frmt" +
        	"  FROM (SELECT ROWNUM rn, id_trans_collection, state_trans_collection_tsl, " +
        	"               send_date_trans_collect_frmt, rec_all_count, rec_payment_all_count, " +
        	"               rec_mov_bon_count, rec_chk_card_count, rec_inval_card_count, " +
        	"               rec_storno_bon_count, min_trans_sys_date_frmt, max_trans_sys_date_frmt" +
        	"		   FROM (SELECT id_trans_collection, " +
        	"                       DECODE(state_trans_collection, " +
			"                  		       'NEW', '<b><font color=\"red\">'||state_trans_collection_tsl||'</font></b>', " +
			"                  		       'SEND', '<font color=\"green\">'||state_trans_collection_tsl||'</font>', " +
			"                  		       state_trans_collection_tsl" +
			"                       ) state_trans_collection_tsl, " +
        	"                       send_date_trans_collect_frmt, " +
        	"                       CASE WHEN rec_all_count > 0 " +
        	"                            THEN '<b><font color=\"red\">'||TO_CHAR(rec_all_count)||'</font></b>'" +
        	"                            ELSE TO_CHAR(rec_all_count)" +
        	"                       END rec_all_count, " +
        	"                       CASE WHEN rec_payment_all_count > 0 " +
        	"                            THEN '<b><font color=\"red\">'||TO_CHAR(rec_payment_all_count)||'</font></b>'" +
        	"                            ELSE TO_CHAR(rec_payment_all_count)" +
        	"                       END rec_payment_all_count, " +
        	"                       CASE WHEN rec_mov_bon_count > 0 " +
        	"                            THEN '<b><font color=\"red\">'||TO_CHAR(rec_mov_bon_count)||'</font></b>'" +
        	"                            ELSE TO_CHAR(rec_mov_bon_count)" +
        	"                       END rec_mov_bon_count, " +
        	"                       CASE WHEN rec_chk_card_count > 0 " +
        	"                            THEN '<b><font color=\"red\">'||TO_CHAR(rec_chk_card_count)||'</font></b>'" +
        	"                            ELSE TO_CHAR(rec_chk_card_count)" +
        	"                       END rec_chk_card_count, " +
        	"                       CASE WHEN rec_inval_card_count > 0 " +
        	"                            THEN '<b><font color=\"red\">'||TO_CHAR(rec_inval_card_count)||'</font></b>'" +
        	"                            ELSE TO_CHAR(rec_inval_card_count)" +
        	"                       END rec_inval_card_count, " +
        	"                       CASE WHEN rec_storno_bon_count > 0 " +
        	"                            THEN '<b><font color=\"red\">'||TO_CHAR(rec_storno_bon_count)||'</font></b>'" +
        	"                            ELSE TO_CHAR(rec_storno_bon_count)" +
        	"                       END rec_storno_bon_count, " +
        	"                       min_trans_sys_date_frmt, max_trans_sys_date_frmt" +
            "                   FROM " + object.getGeneralDBScheme() + ".vc_trans_collection_all "+
        	"                  WHERE id_term = ? ";
    	pParam.add(new bcFeautureParam("int", pIdTerm));
    	
        if (!(pFind==null || "".equalsIgnoreCase(pFind))) {
    		mySQL = mySQL + " AND (TO_CHAR(id_trans_collection) LIKE UPPER('%'||?||'%') OR " +
    			"UPPER(desc_trans_collection) LIKE UPPER('%'||?||'%') OR " + 
    			"TO_CHAR(send_date_trans_collect_frmt) LIKE UPPER('%'||?||'%')) ";
    		for (int i=0; i<3; i++) {
    			pParam.add(new bcFeautureParam("string", pFind));
        	}
    	}
       	if (!(pState==null || "".equalsIgnoreCase(pState))) {
        	mySQL = mySQL + " AND state_trans_collection = ? ";    
           	pParam.add(new bcFeautureParam("string", "1"));   
       	}
        pParam.add(new bcFeautureParam("int", p_end));
		pParam.add(new bcFeautureParam("int", p_beg));
    	mySQL = mySQL + 
        		"  				   ORDER BY id_trans_collection DESC) " +
        		"		   WHERE ROWNUM < ?)" +
        		" WHERE rn >= ?";
    	
    	

        return getHeaderParamHTML(
        		mySQL,
        		pParam,
                0,
                "D",
            	0,
            	"",
            	"/term/all_transspecs.jsp?id=:FIELD2:", 
            	object.transactionXML,
            	pPrint);
    } //getTermSessionsHeadHTML()
    
    public String getOneTermSessionsForIDHeadHTML(String pIdSession1, String pIdSession2, String pIdSession3, String p_beg, String p_end, String pPrint) {
    	object.readDateFormat();
    	ArrayList<bcFeautureParam> pParam = object.initParamArray();
        String mySQL = 
        	"SELECT rn, id_term_ses, date_term_ses_frmt, id_term, desc_term_ses, " +
        	"       is_monitoring_tsl, is_card_online_tsl, is_online_pay_tsl, is_col_data_tsl, is_param_tsl" +
        	"  FROM (SELECT ROWNUM rn, id_term_ses, date_term_ses_frmt, id_term, desc_term_ses, " +
        	"               is_monitoring_tsl, is_card_online_tsl, is_online_pay_tsl, is_col_data_tsl, is_param_tsl" +
        	"		   FROM (SELECT id_term_ses, date_term_ses_frmt, id_term, desc_term_ses, " +
        	"                       DECODE(is_monitoring, " +
            "          					   '1', '<font color=\"green\"><b>'||is_monitoring_tsl||'</b></font>', " +
            "          					   is_monitoring_tsl" +
            "  		 		 	    ) is_monitoring_tsl, " +
        	"                       DECODE(is_card_online, " +
            "          					   '1', '<font color=\"green\"><b>'||is_card_online_tsl||'</b></font>', " +
            "          					   is_card_online_tsl" +
            "  		 		 	    ) is_card_online_tsl, " +
        	"                       DECODE(is_online_pay, " +
            "          					   '1', '<font color=\"green\"><b>'||is_online_pay_tsl||'</b></font>', " +
            "          					   is_online_pay_tsl" +
            "  		 		 	    ) is_online_pay_tsl, " +
        	"                       DECODE(is_col_data, " +
            "          					   '1', '<font color=\"green\"><b>'||is_col_data_tsl||'</b></font>', " +
            "          					   is_col_data_tsl" +
            "  		 		 	    ) is_col_data_tsl, " +
        	"                       DECODE(is_param, " +
            "          					   '1', '<font color=\"green\"><b>'||is_param_tsl||'</b></font>', " +
            "          					   is_param_tsl" +
            "  		 		 	    ) is_param_tsl " +
            "                   FROM " + object.getGeneralDBScheme() + ".vc_term_ses_all "+
        	"                  WHERE id_term_ses = ? " ;
        pParam.add(new bcFeautureParam("int", pIdSession1));
        if (!(pIdSession2==null || "".equalsIgnoreCase(pIdSession2) || "null".equalsIgnoreCase(pIdSession2))) {
            mySQL = mySQL +
            	"                     OR id_term_ses = ? ";
            pParam.add(new bcFeautureParam("int", pIdSession2));
        }
        if (!(pIdSession3==null || "".equalsIgnoreCase(pIdSession3) || "null".equalsIgnoreCase(pIdSession3))) {
            mySQL = mySQL +
            	"                     OR id_term_ses = ? ";
            pParam.add(new bcFeautureParam("int", pIdSession3));
        }
        mySQL = mySQL + 
        	"  				   ORDER BY id_term_ses desc) " +
        	"		   WHERE ROWNUM < ?)" +
        	" WHERE rn >= ?";
        pParam.add(new bcFeautureParam("int", p_end));
		pParam.add(new bcFeautureParam("int", p_beg));
    	

        return getHeaderParamHTML(
        		mySQL,
        		pParam,
                0,
                "D",
            	0,
            	"",
            	"/term/term_sespecs.jsp?id=:FIELD2:", 
            	object.term_sesXML,
            	pPrint);
    } //getTermSessionsHeadHTML()
    


    public String getTransactionsForInterfaceOperHeadHTML(String pIdInterfaceOper, String p_beg, String p_end, String pPrint) {

    	ArrayList<bcFeautureParam> pParam = object.initParamArray();

    	StringBuilder html = new StringBuilder();
        String mySQL =  
                " SELECT rn, id_trans, type_trans_txt, state_trans, sys_date_frmt, " +
        		"        id_term, cd_card1, cd_currency, nt_icc, nt_ext, action, " +
   				"        opr_sum_frmt, sum_pay_cash_frmt, sum_pay_card_frmt, sum_pay_bon_frmt,  " +
   				"		 sum_bon_frmt, sum_disc_frmt " +
   				"   FROM (SELECT ROWNUM rn, id_trans, " +
   				"                DECODE(type_trans, " +
   	    		"                       1,  '<font color=\"black\"><b>'||type_trans_txt||'</b></font>', " +
   	    		"                       2,  '<font color=\"blue\">'||type_trans_txt||'</font>', " +
   	    		"                       3,  '<font color=\"green\">'||type_trans_txt||'</font>', " +
   	    		"                       4,  '<font color=\"red\"><b>'||type_trans_txt||'</b></font>', " +
   	    		"                       5,  '<font color=\"red\">'||type_trans_txt||'</font>', " +
   	    		"                       6,  '<font color=\"black\"><b>'||type_trans_txt||'</b></font>', " +
   	    		"                       7,  '<font color=\"black\"><b>'||type_trans_txt||'</b></font>', " +
   	    		"                       type_trans_txt) type_trans_txt, " +
   	    		"                state_trans, sys_date_frmt, " +
   				"                id_term, cd_card1, cd_currency, nt_icc, nt_ext, action,  " +
	    		"		         opr_sum_frmt, sum_pay_cash_frmt, sum_pay_card_frmt, sum_pay_bon_frmt, " +
	    		"                sum_bon_frmt, sum_disc_frmt " +
	    		"           FROM (SELECT *" +
	    		"                   FROM " + object.getGeneralDBScheme()+".vc_trans_all " +
	    		"                  WHERE id_interface_oper_temp = ? " +  
	    		"				   ORDER BY sys_date desc, card_serial_number, nt_icc) WHERE ROWNUM < ? " + 
	    		"        ) WHERE rn >= ?";

        pParam.add(new bcFeautureParam("int", pIdInterfaceOper));
        pParam.add(new bcFeautureParam("int", p_end));
        pParam.add(new bcFeautureParam("int", p_beg));
        
        html.append(getHeaderParamHTML(
       			mySQL,
        		pParam,
                1,
                "D",
                0,
              	"",
               	"/online/transspecs.jsp?id=:FIELD2:", 
               	object.transactionXML,
               	pPrint));

        return html.toString();
    } //getTransactionsHeadHTML
    
    public String getPaymentTransactionsForCardHeadHTML(String pCardSerialNumber, String pIdIssuer, String pIdPaymentSystem, String pFind, String pIdCardStatus, String p_beg, String p_end, String pPrint) {

    	ArrayList<bcFeautureParam> pParam = object.initParamArray();

    	StringBuilder html = new StringBuilder();
        String mySQL =  
                " SELECT rn, sys_date_frmt, " +
        		"        id_term, cd_card1, cd_currency, nt_icc, " +
   				"        opr_sum_frmt, sum_pay_cash_frmt, sum_pay_card_frmt, sum_pay_bon_frmt,  " +
   				"		 sum_bon_frmt, sum_disc_frmt, id_trans " +
   				"   FROM (SELECT ROWNUM rn, id_trans, " +
   				"                DECODE(type_trans, " +
   	    		"                       1,  '<font color=\"black\"><b>'||type_trans_txt||'</b></font>', " +
   	    		"                       2,  '<font color=\"blue\">'||type_trans_txt||'</font>', " +
   	    		"                       3,  '<font color=\"green\">'||type_trans_txt||'</font>', " +
   	    		"                       4,  '<font color=\"red\"><b>'||type_trans_txt||'</b></font>', " +
   	    		"                       5,  '<font color=\"red\">'||type_trans_txt||'</font>', " +
   	    		"                       6,  '<font color=\"black\"><b>'||type_trans_txt||'</b></font>', " +
   	    		"                       7,  '<font color=\"black\"><b>'||type_trans_txt||'</b></font>', " +
   	    		"                       type_trans_txt) type_trans_txt, " +
   	    		"                DECODE(state_trans, " +
	    		"                       -1,  '<font color=\"blue\"><b>'||state_trans_tsl||'</b></font>', " +
	    		"                       0,  '<font color=\"black\"><b>'||state_trans_tsl||'</b></font>', " +
	    		"                       1,  '<font color=\"green\"><b>'||state_trans_tsl||'</b></font>', " +
	    		"                       state_trans_tsl" +
	    		"                ) state_trans_tsl, " +
   	    		"                sys_date_frmt, " +
   				"                id_term, cd_card1, cd_currency, nt_icc, nt_ext, " +
   				"                CASE WHEN type_trans <= 0 " +
   				"                     THEN DECODE(action,  " +
   				"                                '00', action, " +
	    		"                                '<font color=\"red\"><b><blink>'||action||'<blink></b></font>'" +
	    		"                          ) " +
	    		"                     ELSE action " +
	    		"                END action, " +
   	    		"		         opr_sum_frmt, sum_pay_cash_frmt, sum_pay_card_frmt, sum_pay_bon_frmt, " +
	    		"                sum_bon_frmt, sum_disc_frmt " +
	    		"           FROM (SELECT *" +
	    		"                   FROM " + object.getGeneralDBScheme()+".vc_trans_all " +
	    		"          WHERE type_trans IN (1,6,7)";
        if (!(pCardSerialNumber==null || "".equalsIgnoreCase(pCardSerialNumber))) {
        	mySQL = mySQL +
	    		"                    AND card_serial_number = ? " +
	    		"                    AND id_issuer = ? " +
	    		"                    AND id_payment_system = ? ";
        	pParam.add(new bcFeautureParam("string", pCardSerialNumber));
        	pParam.add(new bcFeautureParam("int", pIdIssuer));
        	pParam.add(new bcFeautureParam("int", pIdPaymentSystem));
        }
        if (!(pFind==null || "".equalsIgnoreCase(pFind))) {
    		mySQL = mySQL + " AND (TO_CHAR(id_term) LIKE UPPER('%'||?||'%') OR " +
    			"UPPER(sys_date_frmt) LIKE UPPER('%'||?||'%') OR " + 
    			"UPPER(cd_card1) LIKE UPPER('%'||?||'%') OR " + 
    			"UPPER(nt_icc) LIKE UPPER('%'||?||'%') OR " + 
    			"UPPER(opr_sum_frmt) LIKE UPPER('%'||?||'%') OR " + 
    			"UPPER(sum_pay_cash_frmt) LIKE UPPER('%'||?||'%') OR " + 
    			"UPPER(sum_pay_card_frmt) LIKE UPPER('%'||?||'%') OR " + 
    			"UPPER(sum_pay_bon_frmt) LIKE UPPER('%'||?||'%') OR " + 
    			"UPPER(sum_bon_frmt) LIKE UPPER('%'||?||'%') OR " + 
    			"UPPER(sum_disc_frmt) LIKE UPPER('%'||?||'%')) ";
    		for (int i=0; i<10; i++) {
    			pParam.add(new bcFeautureParam("string", pFind));
        	}
    	}
        if (!(pIdCardStatus==null || "".equalsIgnoreCase(pIdCardStatus))) {
        	mySQL = mySQL +
	    		"                    AND id_card_status = ? ";
        	pParam.add(new bcFeautureParam("int", pIdCardStatus));
        }
        mySQL = mySQL +
	    		"				   ORDER BY sys_date desc, card_serial_number, nt_icc) WHERE ROWNUM < ? " + 
	    		"        ) WHERE rn >= ?";

        pParam.add(new bcFeautureParam("int", p_end));
        pParam.add(new bcFeautureParam("int", p_beg));
        
        html.append(getHeaderParamHTML(
       			mySQL,
        		pParam,
                1,
                "D",
                0,
              	"",
               	"/online/transspecs.jsp?id=:FIELD13:", 
               	object.transactionXML,
               	pPrint));

        return html.toString();
    } //getTransactionsHeadHTML
    
    public String getPaymentTransactionsForTerminalHeadHTML(String pIdTerm, String pFind, String pIdCardStatus, String p_beg, String p_end, String pPrint) {

    	ArrayList<bcFeautureParam> pParam = object.initParamArray();

    	StringBuilder html = new StringBuilder();
        String mySQL =  
                " SELECT rn, sys_date_frmt, " +
        		"        /*id_term,*/ cd_card1, cd_currency, nt_icc, " +
   				"        opr_sum_frmt, sum_pay_cash_frmt, sum_pay_card_frmt, sum_pay_bon_frmt,  " +
   				"		 sum_bon_frmt, sum_disc_frmt, id_trans " +
   				"   FROM (SELECT ROWNUM rn, id_trans, " +
   				"                DECODE(type_trans, " +
   	    		"                       1,  '<font color=\"black\"><b>'||type_trans_txt||'</b></font>', " +
   	    		"                       2,  '<font color=\"blue\">'||type_trans_txt||'</font>', " +
   	    		"                       3,  '<font color=\"green\">'||type_trans_txt||'</font>', " +
   	    		"                       4,  '<font color=\"red\"><b>'||type_trans_txt||'</b></font>', " +
   	    		"                       5,  '<font color=\"red\">'||type_trans_txt||'</font>', " +
   	    		"                       6,  '<font color=\"black\"><b>'||type_trans_txt||'</b></font>', " +
   	    		"                       7,  '<font color=\"black\"><b>'||type_trans_txt||'</b></font>', " +
   	    		"                       type_trans_txt) type_trans_txt, " +
   	    		"                DECODE(state_trans, " +
	    		"                       -1,  '<font color=\"blue\"><b>'||state_trans_tsl||'</b></font>', " +
	    		"                       0,  '<font color=\"black\"><b>'||state_trans_tsl||'</b></font>', " +
	    		"                       1,  '<font color=\"green\"><b>'||state_trans_tsl||'</b></font>', " +
	    		"                       state_trans_tsl" +
	    		"                ) state_trans_tsl, " +
   	    		"                sys_date_frmt, " +
   				"                id_term, cd_card1, cd_currency, nt_icc, nt_ext, " +
   				"                CASE WHEN type_trans <= 0 " +
   				"                     THEN DECODE(action,  " +
   				"                                '00', action, " +
	    		"                                '<font color=\"red\"><b><blink>'||action||'<blink></b></font>'" +
	    		"                          ) " +
	    		"                     ELSE action " +
	    		"                END action, " +
   	    		"		         opr_sum_frmt, sum_pay_cash_frmt, sum_pay_card_frmt, sum_pay_bon_frmt, " +
	    		"                sum_bon_frmt, sum_disc_frmt " +
	    		"           FROM (SELECT *" +
	    		"                   FROM " + object.getGeneralDBScheme()+".vc_trans_all " +
	    		"          WHERE type_trans IN (1,6,7)";
        if (!(pIdTerm==null || "".equalsIgnoreCase(pIdTerm))) {
        	mySQL = mySQL +
	    		"                    AND id_term = ? ";
        	pParam.add(new bcFeautureParam("int", pIdTerm));
        }
        if (!(pFind==null || "".equalsIgnoreCase(pFind))) {
    		mySQL = mySQL + " AND (TO_CHAR(id_term) LIKE UPPER('%'||?||'%') OR " +
    			"UPPER(sys_date_frmt) LIKE UPPER('%'||?||'%') OR " + 
    			"UPPER(cd_card1) LIKE UPPER('%'||?||'%') OR " + 
    			"UPPER(nt_icc) LIKE UPPER('%'||?||'%') OR " + 
    			"UPPER(opr_sum_frmt) LIKE UPPER('%'||?||'%') OR " + 
    			"UPPER(sum_pay_cash_frmt) LIKE UPPER('%'||?||'%') OR " + 
    			"UPPER(sum_pay_card_frmt) LIKE UPPER('%'||?||'%') OR " + 
    			"UPPER(sum_pay_bon_frmt) LIKE UPPER('%'||?||'%') OR " + 
    			"UPPER(sum_bon_frmt) LIKE UPPER('%'||?||'%') OR " + 
    			"UPPER(sum_disc_frmt) LIKE UPPER('%'||?||'%')) ";
    		for (int i=0; i<10; i++) {
    			pParam.add(new bcFeautureParam("string", pFind));
        	}
    	}
        if (!(pIdCardStatus==null || "".equalsIgnoreCase(pIdCardStatus))) {
        	mySQL = mySQL +
	    		"                    AND id_card_status = ? ";
        	pParam.add(new bcFeautureParam("int", pIdCardStatus));
        }
        mySQL = mySQL +
	    		"				   ORDER BY sys_date desc, card_serial_number, nt_icc) WHERE ROWNUM < ? " + 
	    		"        ) WHERE rn >= ?";

        pParam.add(new bcFeautureParam("int", p_end));
        pParam.add(new bcFeautureParam("int", p_beg));
        
        html.append(getHeaderParamHTML(
       			mySQL,
        		pParam,
                1,
                "D",
                0,
              	"",
               	"/online/transspecs.jsp?id=:FIELD12:", 
               	object.transactionXML,
               	pPrint));

        return html.toString();
    } //getTransactionsHeadHTML

/*
    public String getWebClientCardListHeadHTML(String pCardSerialNumber, String pIdIssuer, String pIdPaymentSystem) {
    	StringBuilder html = new StringBuilder();
    	ArrayList<bcFeautureParam> pParam = object.initParamArray();
        
        PreparedStatement st = null;

        try{
        	String mySQL =  
                    " SELECT * " +
    	    		"   FROM " + object.getGeneralDBScheme()+".vw$card_all " +
    	    		"  ORDER BY ";
            if (!(pIdTerm==null || "".equalsIgnoreCase(pIdTerm))) {
            	mySQL = mySQL +
    	    		"                    AND id_term = ? ";
            	pParam.add(new bcFeautureParam("int", pIdTerm));
            }
            if (!(pFind==null || "".equalsIgnoreCase(pFind))) {
        		mySQL = mySQL + " AND (TO_CHAR(id_term) LIKE UPPER('%'||?||'%') OR " +
        			"UPPER(sys_date_frmt) LIKE UPPER('%'||?||'%') OR " + 
        			"UPPER(cd_card1) LIKE UPPER('%'||?||'%') OR " + 
        			"UPPER(nt_icc) LIKE UPPER('%'||?||'%') OR " + 
        			"UPPER(opr_sum_frmt) LIKE UPPER('%'||?||'%') OR " + 
        			"UPPER(sum_pay_cash_frmt) LIKE UPPER('%'||?||'%') OR " + 
        			"UPPER(sum_pay_card_frmt) LIKE UPPER('%'||?||'%') OR " + 
        			"UPPER(sum_pay_bon_frmt) LIKE UPPER('%'||?||'%') OR " + 
        			"UPPER(sum_bon_frmt) LIKE UPPER('%'||?||'%') OR " + 
        			"UPPER(sum_disc_frmt) LIKE UPPER('%'||?||'%')) ";
        		for (int i=0; i<10; i++) {
        			pParam.add(new bcFeautureParam("string", pFind));
            	}
        	}
            if (!(pIdCardStatus==null || "".equalsIgnoreCase(pIdCardStatus))) {
            	mySQL = mySQL +
    	    		"                    AND id_card_status = ? ";
            	pParam.add(new bcFeautureParam("int", pIdCardStatus));
            }
            mySQL = mySQL +
    	    		"				   ORDER BY sys_date desc, card_serial_number, nt_icc) WHERE ROWNUM < ? " + 
    	    		"        ) WHERE rn >= ?";

            pParam.add(new bcFeautureParam("int", p_end));
            pParam.add(new bcFeautureParam("int", p_beg));
        	if ("Y".equalsIgnoreCase(pPrint)) {
        		deleteHyperLink = "";
        	}
        	
        	LOGGER.debug(prepareSQLToLog(mySQL, pParam));
        	con = Connector.getConnection(object.getSessionId());
        	
            st = con.prepareStatement(mySQL);
            st = object.prepareParam(st, pParam);
            
            ResultSet rset = st.executeQuery();
            ResultSetMetaData mtd = rset.getMetaData();
            
            int colCount = mtd.getColumnCount();
            if ("Y".equalsIgnoreCase(pPrint)) {
            	html.append(getPrintTable());
            } else {
            	html.append(getHeaderTable());
            }
            html.append("<thead><tr>\n");
            
            int lessColumn = 0;

            for (int i=1; i <= colCount-pLessColumn; i++) {
            	String colName = mtd.getColumnName(i);
            	if (!pExtend) {
	            	if ("RN".equalsIgnoreCase(colName) ||
	            		"CREATION_DATE".equalsIgnoreCase(colName) ||
	            		"CREATION_DATE_FRMT".equalsIgnoreCase(colName) ||
	            		"LAST_UPDATE_DATE".equalsIgnoreCase(colName) ||
	            		"LAST_UPDATE_DATE_FRMT".equalsIgnoreCase(colName)) {
	            		html.append("<th>"+ object.commonXML.getfieldTransl(this.language, colName, false)+"</th>\n");
	            	} else if ("CLUB_NAME".equalsIgnoreCase(colName)) {
	            		html.append("<th>"+ object.clubXML.getfieldTransl(this.language, "club", false)+"</th>\n");
	            	} else if ("ID_CLUB".equalsIgnoreCase(colName)) {
	            		html.append("<th>"+ object.clubXML.getfieldTransl(this.language, "id_club", false)+"</th>\n");
	            	} else {
	            		html.append("<th>"+ myXML.getfieldTransl(this.language, colName, false)+"</th>\n");
	            	}
            	} else {
            		if ("RN".equalsIgnoreCase(colName)) {
            			lessColumn = 1;
            			html.append("<th>"+ object.commonXML.getfieldTransl(this.language, colName, false)+"</th>\n");
    	           	} else {
    	           		html.append(object.getBottomFrameTableExtendedTH(
  	        			  object.club_actionXML,
  	        			  i - lessColumn,
  	        			  mtd.getColumnName(i), 
  	        			  pOrderField,
  	        			  pOrderType,
  	        			  pSortHyperLink,
  	        			  this.language));
    	           	}
            	}
            }
            for (int i=1; i <= colCount; i++) {
            	if ("ROW_COUNT".equalsIgnoreCase(mtd.getColumnName(i))) {
            		isSetRowCount = false;
            	}
            }
            if (!(deleteHyperLink==null || "".equalsIgnoreCase(deleteHyperLink))) {
            	html.append("<th>&nbsp;</th>\n");
            }
            html.append("</tr></thead><tbody>\n");
            
            while (rset.next())
            {
            	if (!isSetRowCount) {
            		setLastResultSetRowCount(rset.getString("ROW_COUNT"));
            		isSetRowCount = true;
            	}
            	if ("RN".equalsIgnoreCase(mtd.getColumnName(1))) {
            		html.append("<tr id=\"elem_"+rset.getString(2)+"\" onmouseout=\"coloredOut(this)\" onmouseover=\"coloredOver(this,1)\">");
            	} else {
            		html.append("<tr id=\"elem_"+rset.getString(1)+"\" onmouseout=\"coloredOut(this)\" onmouseover=\"coloredOver(this,1)\">");
            	}
            	
            	
            	String deleteId = "";
            	String deleteName = "";
            	
            	myHyperLink = pDetailHyperLink;
            	for (int i=1; i <= colCount; i++) {
            		if (rset.getString(i)==null) {
            			myHyperLink = myHyperLink.replace(":FIELD"+i+":", "");
            		} else {
            			myHyperLink = myHyperLink.replace(":FIELD"+i+":", rset.getString(i));
            		}
            	}
            	
            	if ("N".equalsIgnoreCase(pCompareType)) {
            		if (pValue.equalsIgnoreCase(rset.getString(pColumn))) {
            			myFont = "<font color=\"red\">";
            			myFontEnd = "</font>";
            			myBgColor = object.selectedBackGroundStyle;
            		} else {
            			myFont = "<font color=\"black\">";
            			myFontEnd = "</font>";
            			myBgColor = object.noneBackGroundStyle;
            		}
            	} else if ("P".equalsIgnoreCase(pCompareType)) {
            		if (pValue.equalsIgnoreCase(rset.getString(pColumn))) {
            			myFont = "<font color=\"black\">";
            			myFontEnd = "</font>";
            			myBgColor = object.noneBackGroundStyle;
            		} else {
            			myFont = "<font color=\"red\">";
            			myFontEnd = "</font>";
            			myBgColor = object.selectedBackGroundStyle;
            		}
            	} else {
            		myFont = "<font color=\"black\">";
        			myFontEnd = "</font>";
        			myBgColor = object.noneBackGroundStyle;
            	}
            	
            	for (int i=1; i <= colCount; i++) {
            		if (deleteEntryId.equalsIgnoreCase(mtd.getColumnName(i))) {
            			deleteId = object.getHTMLValue(rset.getString(i));
            		}
            		if (deleteEntryName.equalsIgnoreCase(mtd.getColumnName(i))) {
            			deleteName = object.getHTMLValue(rset.getString(i));
            		}
            	}
            	//String columnName = "";
            	for (int i=1; i <= colCount-pLessColumn; i++) {
            		html.append(object.formatTDData(mtd.getColumnName(i), mtd.getColumnType(i), myBgColor));
            		/*
            		switch (mtd.getColumnType(i)) {
						case Types.DATE:
						case Types.TIMESTAMP:
						case Types.NUMERIC:
						case Types.INTEGER:
						case Types.DECIMAL:
							html.append("<td align=\"center\" "+myBgColor+">");
							break;
						case Types.VARCHAR:
							columnName = mtd.getColumnName(i);
							if (columnName.contains("FRMT") || 
								columnName.contains("PERCENT") || 
								columnName.contains("TSL") || 
								columnName.contains("DATE") || 
								columnName.contains("NUMBER") || 
								columnName.contains("HEX")) {
								html.append("<td align=\"center\" "+myBgColor+">");
							} else {
								html.append("<td "+myBgColor+">");
							}
							break;
						default:
							html.append("<td "+myBgColor+">");
					} // switch ()
					*/
					//if ("RN".equalsIgnoreCase(mtd.getColumnName(i))) {
					//	html.append(getValue2(rset.getString(i)) + "</td>\n");
					//} else {
/*
						if ("Y".equalsIgnoreCase(pPrint)) {
							html.append(getValue2(rset.getString(i)) + "</td>\n");
						} else {
							String pTempValue = "";
							if ("ID_CLUB".equalsIgnoreCase(mtd.getColumnName(i))) {
								pTempValue = "<font color=\"blue\"><b>" + getValue2(rset.getString(i)) + "</b></font>";
							} else {
								pTempValue = getValue2(rset.getString(i));
							}
							html.append(object.getHyperLinkHeaderFirst() + object.getContextPath() + myHyperLink +  object.getHyperLinkMiddle() + myFont + pTempValue + myFontEnd + object.getHyperLinkEnd() + "</td>\n");
						}
					//}
            	}
            	if (!(deleteHyperLink==null || "".equalsIgnoreCase(deleteHyperLink))) {
                	html.append("<td align=\"center\"><div class=\"div_button\" onclick=\"var msg='" + deleteConfirmMessage + " \\\'" + deleteName + "\\\'?';var res=window.confirm(msg);if (res) {ajaxpage('"+object.getContextPath() + deleteHyperLink + deleteId + "','div_main');}\">" + "<img vspace=\"0\" hspace=\"0\"" +
            				" src=\"" + object.getContextPath() + "/images/row_del_v.GIF\" width=\"16\" height=\"16\" align=\"top\" style=\"border: 0px;\"" + 
            				" title=\"" + object.buttonXML.getfieldTransl(this.language,"button_delete", false)+"\">" + object.getHyperLinkEnd()+"</td>\n");
                }
                html.append("</tr>\n");
            }
            html.append("</tbody></table>\n");
            setDeleteHyperLink("","",""); 
        } // try
        catch (SQLException e) {LOGGER.error(html, e);}
        catch (Exception el) {LOGGER.error(html, el);}
        finally {
            try {
                if (st!=null) st.close();
            } catch (SQLException w) {w.toString();}
            try {
                if (con!=null) con.close();
            } catch (SQLException w) {w.toString();}
            Connector.closeConnection(con);
        } // finally
        
        deleteHyperLink = "";
		deleteConfirmMessage = "";
		deleteEntryId = "";
		deleteEntryName = "";
        LOGGER.debug("END");
        return html.toString();
    }*/
}
