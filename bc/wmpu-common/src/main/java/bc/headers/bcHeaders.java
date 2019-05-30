package bc.headers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import bc.bcBase;
import bc.connection.Connector;
import bc.objects.bcBankStatementLineObject;
import bc.objects.bcObject;
import bc.objects.bcXML;
import bc.service.bcFeautureParam;

public class bcHeaders extends bcObject  { 
	protected static Connection con = null;
	
	private final static Logger LOGGER=Logger.getLogger(bcHeaders.class);
	
    private String idClub;
	
	public bcHeaders(String pDateFormat){
		setSessionId(bcBase.SESSION_PARAMETERS.SESSION_ID.getValue() );
		setLanguage(bcBase.SESSION_PARAMETERS.LANG.getValue());
		setGeneralDBScheme(bcBase.SESSION_PARAMETERS.GENERAL_DB_SCHEME.getValue());
		setDateFormat(pDateFormat);		
		LOGGER.debug("bcHeaders('" + pDateFormat + "') constructor");
	}
	
	public void setPamam() {
		setSessionId(bcBase.SESSION_PARAMETERS.SESSION_ID.getValue() );
		setLanguage(bcBase.SESSION_PARAMETERS.LANG.getValue());
		setGeneralDBScheme(bcBase.SESSION_PARAMETERS.GENERAL_DB_SCHEME.getValue());
		setDateFormat(bcBase.SESSION_PARAMETERS.DATE_FORMAT.getValue());
	}
	
	public void setIdClub(String pIdClub){
		idClub = pIdClub;
	}
	
	public String getIdClubFieldString(String commaPosition) {
		String returnValue = "";
		if (isEmpty(this.idClub)) {
			returnValue = "id_club";
			if ("FIRST".equalsIgnoreCase(commaPosition)) {
				returnValue = ", " + returnValue;
			} else if ("LAST".equalsIgnoreCase(commaPosition)) {
				returnValue =  returnValue + ", ";
			}
		}
		return returnValue;
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
	
	private String changeHyperLink = "";
	private String changeConfirmMessage = "";
	private String changeEntryId = "";
	private String changeEntryName = "";
	
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
	
	public void setChangeHyperLink(String pHyperLink, String pMessage, String pEntryId, String pEntryName) {
		changeHyperLink = pHyperLink;
		changeConfirmMessage = pMessage;
		changeEntryId = pEntryId;
		changeEntryName = pEntryName;
	}
	
	public void setChangeHyperLink(String pHyperLink, String pMessage, String pEntryId) {
		changeHyperLink = pHyperLink;
		changeConfirmMessage = pMessage;
		changeEntryId = pEntryId;
		changeEntryName = pEntryId;
	}
	
	public String getHeaderHTML(String pSQL, int pLessColumn, String pCompareType, int pColumn, String pValue, String pHyperLink, bcXML myXML, String pPrint) {
		ArrayList<bcFeautureParam> pParam = initParamArray();

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
        String myBgColor = noneBackGroundStyle;
        
        int isSetRowCount = 0;
        int rowCnt = 0;

        try{
        	setLastResultSetRowCount("0");
        	if ("Y".equalsIgnoreCase(pPrint)) {
        		deleteHyperLink = "";
        		changeHyperLink = "";
        	}
        	
        	LOGGER.debug(prepareSQLToLog(pSQL, pParam));
        	con = Connector.getConnection(getSessionId());
        	
            st = con.prepareStatement(pSQL);
            st = prepareParam(st, pParam);
            
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

            if (!isEmpty(changeHyperLink)) {
            	html.append("<th>&nbsp;</th>\n");
            }
            for (int i=1; i <= colCount-pLessColumn; i++) {
            	String colName = mtd.getColumnName(i);
            	if (!pExtend) {
	            	if ("RN".equalsIgnoreCase(colName) ||
	            		"CREATION_DATE".equalsIgnoreCase(colName) ||
	            		"CREATION_DATE_FRMT".equalsIgnoreCase(colName) ||
	            		"LAST_UPDATE_DATE".equalsIgnoreCase(colName) ||
	            		"LAST_UPDATE_DATE_FRMT".equalsIgnoreCase(colName)) {
	            		html.append("<th>"+ commonXML.getfieldTransl(colName, false)+"</th>\n");
	            	} else if ("CLUB_MEMBER_STATUS".equalsIgnoreCase(colName)) {
	            		html.append("<th>"+ clubXML.getfieldTransl("club_member_status", false)+"</th>\n");
	            	} else if ("CLUB_MEMBER_TYPE".equalsIgnoreCase(colName)) {
	            		html.append("<th>"+ clubXML.getfieldTransl("club_member_type", false)+"</th>\n");
	            	} else if ("CLUB_DATE_BEG".equalsIgnoreCase(colName)|| "CLUB_DATE_BEG_FRMT".equalsIgnoreCase(colName)) {
	            		html.append("<th>"+ clubXML.getfieldTransl("club_date_beg", false)+"</th>\n");
	            	} else if ("CLUB_DATE_END".equalsIgnoreCase(colName)|| "CLUB_DATE_END_FRMT".equalsIgnoreCase(colName)) {
	            		html.append("<th>"+ clubXML.getfieldTransl("club_date_end", false)+"</th>\n");
	            	} else if ("ID_CLUB".equalsIgnoreCase(colName)) {
	            		html.append("<th>"+ clubXML.getfieldTransl("id_club", false)+"</th>\n");
	            	} else if ("CLUB_NAME".equalsIgnoreCase(colName)) {
	            		html.append("<th>"+ clubXML.getfieldTransl("club", false)+"</th>\n");
	            	//} else if ("NAME_CLUB".equalsIgnoreCase(colName)) {
	            	//	html.append("<th>"+ clubXML.getfieldTransl("club", false)+"</th>\n");
	            	//} else if ("SNAME_CLUB".equalsIgnoreCase(colName)) {
	            	//	html.append("<th>"+ clubXML.getfieldTransl("club", false)+"</th>\n");
	            	} else {
	            		html.append("<th>"+ myXML.getfieldTransl(colName, false)+"</th>\n");
	            	}
            	} else {
            		if ("RN".equalsIgnoreCase(colName)) {
            			lessColumn = 1;
            			html.append("<th>"+ commonXML.getfieldTransl(colName, false)+"</th>\n");
    	           	} else {
    	           		html.append(getBottomFrameTableExtendedTH(
  	        			  myXML,
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
            		isSetRowCount = 1;
            	}
            }
            if (!isEmpty(deleteHyperLink)) {
            	html.append("<th>&nbsp;</th>\n");
            }
            html.append("</tr></thead><tbody>\n");
            
            while (rset.next())
            {
            	rowCnt = rowCnt + 1;
            	if (isSetRowCount==1) {
            		setLastResultSetRowCount(rset.getString("ROW_COUNT"));
            		isSetRowCount = 2;
            	}
            	html.append("<tr id=\"elem_"+rowCnt+"\" onmouseout=\"coloredOut(this)\" onmouseover=\"coloredOver(this,1)\">");
            	/*if ("RN".equalsIgnoreCase(mtd.getColumnName(1))) {
            		html.append("<tr id=\"elem_"+rset.getString(2)+"\" onmouseout=\"coloredOut(this)\" onmouseover=\"coloredOver(this,1)\">");
            	} else {
            		html.append("<tr id=\"elem_"+rset.getString(1)+"\" onmouseout=\"coloredOut(this)\" onmouseover=\"coloredOver(this,1)\">");
            	}*/
            	
            	
            	String deleteId = "";
            	String deleteName = "";
            	String changeId = "";
            	String changeName = "";
            	
            	myHyperLink = pDetailHyperLink;
            	for (int i=1; i <= colCount; i++) {
            		if ("DETAIL_HYPERLINK".equalsIgnoreCase(mtd.getColumnName(i))) {
            			myHyperLink = rset.getString(i);
            		} else {
	            		if (rset.getString(i)==null) {
	            			myHyperLink = myHyperLink.replace(":FIELD"+i+":", "");
	            		} else {
	            			myHyperLink = myHyperLink.replace(":FIELD"+i+":", rset.getString(i));
	            		}
            		}
            	}
            	
            	if ("N".equalsIgnoreCase(pCompareType)) {
            		if (pValue.equalsIgnoreCase(rset.getString(pColumn))) {
            			myFont = "<font color=\"red\">";
            			myFontEnd = "</font>";
            			myBgColor = selectedBackGroundStyle;
            		} else {
            			myFont = "<font color=\"black\">";
            			myFontEnd = "</font>";
            			myBgColor = noneBackGroundStyle;
            		}
            	} else if ("P".equalsIgnoreCase(pCompareType)) {
            		if (pValue.equalsIgnoreCase(rset.getString(pColumn))) {
            			myFont = "<font color=\"black\">";
            			myFontEnd = "</font>";
            			myBgColor = noneBackGroundStyle;
            		} else {
            			myFont = "<font color=\"red\">";
            			myFontEnd = "</font>";
            			myBgColor = selectedBackGroundStyle;
            		}
            	} else {
            		myFont = "<font color=\"black\">";
        			myFontEnd = "</font>";
        			myBgColor = noneBackGroundStyle;
            	}
            	
            	for (int i=1; i <= colCount; i++) {
            		if (deleteEntryId.equalsIgnoreCase(mtd.getColumnName(i))) {
            			deleteId = getHTMLValue(rset.getString(i));
            		}
            		if (deleteEntryName.equalsIgnoreCase(mtd.getColumnName(i))) {
            			deleteName = getHTMLValue(rset.getString(i));
            		}
            	}
            	
            	for (int i=1; i <= colCount; i++) {
            		if (changeEntryId.equalsIgnoreCase(mtd.getColumnName(i))) {
            			changeId = getHTMLValue(rset.getString(i));
            		}
            		if (changeEntryName.equalsIgnoreCase(mtd.getColumnName(i))) {
            			changeName = getHTMLValue(rset.getString(i));
            		}
            	}
            	//String columnName = "";
            	if (!isEmpty(changeHyperLink)) {
                	html.append("<td align=\"center\"><div class=\"div_button\" onclick=\"var msg='" + changeConfirmMessage + " \\\'" + changeName + "\\\'?';var res=window.confirm(msg);if (res) {ajaxpage('"+changeHyperLink + changeId + "','div_main');}\">" + "<img vspace=\"0\" hspace=\"0\"" +
            				" src=\"../images/oper/change_button.png\" height=\"16\" align=\"top\" style=\"border: 0px;\"" + 
            				" title=\"" + buttonXML.getfieldTransl("set_current", false)+"\">" + getHyperLinkEnd()+"</td>\n");
                }
            	for (int i=1; i <= colCount-pLessColumn; i++) {
            		html.append(formatTDData(mtd.getColumnName(i), mtd.getColumnType(i), myBgColor));
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
            		//LOGGER.debug(mtd.getColumnName(i)+"("+i+")="+rset.getString(i));
						if ("Y".equalsIgnoreCase(pPrint)) {
							html.append(getValue2(rset.getString(i)) + "</td>\n");
						} else {
							String pTempValue = "";
							if ("ID_CLUB".equalsIgnoreCase(mtd.getColumnName(i))) {
								pTempValue = "<font color=\"blue\"><b>" + getValue2(rset.getString(i)) + "</b></font>";
							} else {
								pTempValue = getValue2(rset.getString(i));
							}
							//LOGGER.debug(mtd.getColumnName(i)+"("+i+")="+pTempValue);
							html.append(getHyperLinkHeaderFirst() + myHyperLink +  getHyperLinkMiddle() + myFont + pTempValue + myFontEnd + getHyperLinkEnd() + "</td>\n");
						}
					//}
            	}
            	if (!isEmpty(deleteHyperLink)) {
                	html.append("<td align=\"center\"><div class=\"div_button\" onclick=\"var msg='" + deleteConfirmMessage + " \\\'" + deleteName + "\\\'?';var res=window.confirm(msg);if (res) {ajaxpage('"+deleteHyperLink + deleteId + "','div_main');}\">" + "<img vspace=\"0\" hspace=\"0\"" +
            				" src=\"../images/oper/rows/delete.png\" width=\"16\" height=\"16\" align=\"top\" style=\"border: 0px;\"" + 
            				" title=\"" + buttonXML.getfieldTransl("button_delete", false)+"\">" + getHyperLinkEnd()+"</td>\n");
                }
                html.append("</tr>\n");
            }
            html.append("</tbody></table>\n");
            if (isSetRowCount==0) {
            	setLastResultSetRowCount(rowCnt + "");
            }
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
        
        deleteHyperLink = "";
		deleteConfirmMessage = "";
		deleteEntryId = "";
		deleteEntryName = "";
		changeHyperLink = "";
		changeConfirmMessage = "";
		changeEntryId = "";
		changeEntryName = "";
        //LOGGER.debug("END");
        //LOGGER.debug(html.toString());
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
    
	public String getClubCardHeadHTML(String pFind, String id_profile, String id_issuer, String p_beg, String p_end, String pPrint) {
        StringBuilder html = new StringBuilder();
        
        PreparedStatement st = null;
        
        ArrayList<bcFeautureParam> pParam = initParamArray();
        
        String myHyperLink = "";

        String myFont = "<font>";
        String myFontEnd = "</font>";
        String myBgColor = noneBackGroundStyle;
        
        PreparedStatement st_profile = null;
        String myFiltr = "";
        String mySQL_profile = "SELECT filtr FROM " + getGeneralDBScheme()+".v_menu_profile_filtr_all WHERE id_menu_element = ? ";
        String mySQL =  "";
        
        try{
        	setLastResultSetRowCount("0");
        	
            if (!isEmpty(id_profile)) {
            	try {
	            	con = Connector.getConnection(getSessionId());
	            	st_profile = con.prepareStatement(mySQL_profile);
	            	st_profile.setInt(1, Integer.parseInt(id_profile));
	            	ResultSet rset_profile = st_profile.executeQuery();
	            	while (rset_profile.next()) {
	            		if (!("".equalsIgnoreCase(rset_profile.getString(1).trim()))) {
	            			myFiltr = myFiltr + " AND "+rset_profile.getString(1);
	            		}
	            	}
            	} // try
            	catch (SQLException e) {LOGGER.error(html, e); html.append(showCRMException(e));}
               	catch (Exception el) {LOGGER.error(html, el); html.append(showCRMException(el));}
                finally {
                    try {
                        if (st_profile!=null) {
							st_profile.close();
						}
                    } catch (SQLException w) {w.toString();}
                    try {
                        if (con!=null) {
							con.close();
						}
                    } catch (SQLException w) {w.toString();}
                    Connector.closeConnection(con);
                } // finally
            }
            
            mySQL = 
            	"SELECT rn, cd_card1, name_card_status, /*name_card_type,*/ " +
            	"		name_card_state, full_name name_nat_prs, /*date_open_frmt,*/ date_card_sale_frmt, nt_icc, " +
            	"		card_serial_number, id_issuer, id_payment_system, id_card_state " +
            	"  FROM (SELECT ROWNUM rn, cd_card1, full_name, " +
            	"				name_card_type, name_card_status,  " +
            	"				name_card_state, date_open_frmt, date_card_sale_frmt, nt_icc, id_issuer, " +
            	"				id_payment_system, card_serial_number, id_card_state " +
            	"		  FROM (SELECT a.* " +
            	"                 FROM " + getGeneralDBScheme()+".vc_card_find_all a " +
            	"                WHERE 1=1 ";
            if (!isEmpty(myFiltr)) {
            	mySQL = mySQL + myFiltr;
            }
            if (!isEmpty(id_issuer)) {
            	mySQL = mySQL + " AND id_issuer = ? ";
            	pParam.add(new bcFeautureParam("int", id_issuer));
            }
            if (!isEmpty(pFind)) {
            	mySQL = mySQL +
            		" AND (cd_card1 LIKE '%'||?||'%' " +
            		"    OR card_serial_number LIKE UPPER('%'||?||'%')" +
            		"    OR UPPER(full_name) LIKE UPPER('%'||?||'%'))";
            	for (int i=0; i<3; i++) {
            	    pParam.add(new bcFeautureParam("string", pFind));
            	}
            }
            pParam.add(new bcFeautureParam("int", p_end));
            pParam.add(new bcFeautureParam("int", p_beg));
            mySQL = mySQL + ") WHERE ROWNUM < ?)  WHERE rn >= ?";

        	
        	LOGGER.debug(prepareSQLToLog(mySQL, pParam));
        	con = Connector.getConnection(getSessionId());
        	
            st = con.prepareStatement(mySQL);
            st = prepareParam(st, pParam);
            ResultSet rset = st.executeQuery();
            ResultSetMetaData mtd = rset.getMetaData();
            
            int colCount = mtd.getColumnCount();
            if ("Y".equalsIgnoreCase(pPrint)) {
            	html.append(getPrintTable());
            } else {
            	html.append(getHeaderTable());
            }

            html.append("<thead><tr>\n");

            for (int i=1; i <= colCount-4; i++) {
            	html.append(getBottomFrameTableTH(clubcardXML, mtd.getColumnName(i)));
            }
            html.append("</tr></thead><tbody>\n");
            
            while (rset.next())
            {
            	html.append("<tr id=\"elem_"+rset.getString("CARD_SERIAL_NUMBER")+"\" onmouseout=\"coloredOut(this)\" onmouseover=\"coloredOver(this,1)\">");
            	
            	myHyperLink = getHyperLinkHeaderFirst()+"../crm/cards/clubcardspecs.jsp?id=:FIELD8:"+
            		"&iss=:FIELD9:&paysys=:FIELD10:" + getHyperLinkMiddle();
            	for (int i=1; i <= colCount; i++) {
            		if (rset.getString(i)==null) {
            			myHyperLink = myHyperLink.replace(":FIELD"+i+":", "");
            		} else {
            			myHyperLink = myHyperLink.replace(":FIELD"+i+":", rset.getString(i));
            		}
            	}
            	
            	String columnName = "";
            	for (int i=1; i <= colCount-4; i++) {
                	if ("0".equalsIgnoreCase(rset.getString("ID_CARD_STATE"))) {
                		myFont = "<font color=\"black\">";
            			myFontEnd = "</font>";
            			myBgColor = noneBackGroundStyle;
                	} else if ("1".equalsIgnoreCase(rset.getString("ID_CARD_STATE"))) {
                		myFont = "<font color=\"black\">";
            			myFontEnd = "</font>";
            			myBgColor = noneBackGroundStyle;
                	} else if ("2".equalsIgnoreCase(rset.getString("ID_CARD_STATE"))) {
                		myFont = "<font color=\"red\">";
            			myFontEnd = "</font>";
            			myBgColor = selectedBackGroundStyle;
                	} else if ("3".equalsIgnoreCase(rset.getString("ID_CARD_STATE"))) {
                		myFont = "<font color=\"red\">";
            			myFontEnd = "</font>";
            			myBgColor = selectedBackGroundStyle;
                	} else if ("4".equalsIgnoreCase(rset.getString("ID_CARD_STATE"))) {
                		myFont = "<font color=\"red\">";
            			myFontEnd = "</font>";
            			myBgColor = selectedBackGroundStyle;
                	} else if ("5".equalsIgnoreCase(rset.getString("ID_CARD_STATE"))) {
                		myFont = "<font color=\"gray\">";
            			myFontEnd = "</font>";
            			myBgColor = noneBackGroundStyle;
                	} else if ("6".equalsIgnoreCase(rset.getString("ID_CARD_STATE"))) {
                		myFont = "<font color=\"gray\">";
            			myFontEnd = "</font>";
            			myBgColor = noneBackGroundStyle;
                	} else if ("7".equalsIgnoreCase(rset.getString("ID_CARD_STATE"))) {
                		myFont = "<font color=\"green\">";
            			myFontEnd = "</font>";
            			myBgColor = noneBackGroundStyle;
                	} else if ("8".equalsIgnoreCase(rset.getString("ID_CARD_STATE"))) {
                		myFont = "<font color=\"red\">";
            			myFontEnd = "</font>";
            			myBgColor = selectedBackGroundStyle;
                	} else {
               			myFont = "<font color=\"black\">";
               			myFontEnd = "</font>";
               			myBgColor = noneBackGroundStyle;
                	}

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
					if ("RN".equalsIgnoreCase(mtd.getColumnName(i))) {
						html.append(getValue2(rset.getString(i)) + "</td>\n");
					} else {
						html.append(myHyperLink + myFont + getValue2(rset.getString(i)) + myFontEnd + getHyperLinkEnd() + "</td>\n");
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
    } 
    
	public String getAdminConnectionsHeadHTML(String pFindString, String p_beg, String p_end, String pPrint) {
        StringBuilder html = new StringBuilder();
        
        boolean hasEditPermission = false;
        try{
        	if (isEditMenuPermited("SECURITY_ADMIN")>0) {
       	 		hasEditPermission = true;
        	}
            
        	if ("Y".equalsIgnoreCase(pPrint)) {
            	html.append(getPrintTable());
            } else {
            	html.append(getHeaderTable());
            }
            html.append("<thead><tr>\n");
            html.append("<th>"+ commonXML.getfieldTransl("RN", false)+"</th>\n");
       		html.append("<th>"+ userXML.getfieldTransl("NAME_USER", false)+"</th>\n");
       		html.append("<th>"+ userXML.getfieldTransl("DB_CONNECTION_COUNT", false)+"</th>\n");
       		if (!("Y".equalsIgnoreCase(pPrint)) && hasEditPermission) {
       			html.append("<th>&nbsp;</th>\n");
       		}
            html.append("</tr></thead><tbody>\n");
            
            Map<String, String> mp = Connector.getConnectionMap();
            int counter = 0;
            int lBeg = Integer.parseInt(p_beg);
            int lEnd = Integer.parseInt(p_end);
            for (String maps:mp.keySet()) {
            	if (isEmpty(pFindString) ||
            			(!isEmpty(pFindString) && maps.toLowerCase().contains(pFindString.toLowerCase()))) {
	            	counter  = counter + 1;
	            	if (counter >= lBeg && counter < lEnd) {
	            		html.append("<tr id=\"elem_"+counter+"\" onmouseout=\"coloredOut(this)\" onmouseover=\"coloredOver(this,1)\">");
	            		html.append("<td align=\"center\">" + counter + "</td>\n");
	            		html.append("<td align=\"center\">" + maps + "</td>\n");
	            		html.append("<td align=\"center\">" + mp.get(maps) + "</td>\n");
	            		if (!("Y".equalsIgnoreCase(pPrint)) && hasEditPermission) {
	                    	html.append("<td align=\"center\"><div class=\"div_button\" onclick=\"var msg='" + buttonXML.getfieldTransl("delete", false) + " \\\'" + maps + "\\\'?';var res=window.confirm(msg);if (res) {ajaxpage('"+"../crm/security/adminupdate.jsp?type=general&process=yes&action=remove&id=" + maps + "','div_main');}\">" + "<img vspace=\"0\" hspace=\"0\"" +
	                				" src=\"../images/oper/rows/delete.png\" width=\"16\" height=\"16\" align=\"top\" style=\"border: 0px;\"" + 
	                				" title=\"" + buttonXML.getfieldTransl("button_delete", false)+"\">" + getHyperLinkEnd()+"</td>\n");
	               		}
	                    html.append("</tr>\n");
	            	}
            	}
            }
            html.append("</tbody></table>\n");
        } // try
        catch (Exception el) {LOGGER.error(html, el); html.append(showCRMException(el));}
        finally {
            try {
                if (con!=null) {
					con.close();
				}
            } catch (SQLException e) {LOGGER.error(html, e); html.append(showCRMException(e));}

        } // finally
        return html.toString();
    }

	public String getCurrenciesHTML(String pFind, String pIsUsed, boolean isAll, String pPrint) {
		String mySQL = 
			" SELECT cd_currency, scd_currency, name_currency, sname_currency, " +
            "		 cd_currency_in_telgr_hex, is_used_tsl, count(*) over() as row_count " +
            "   FROM " + getGeneralDBScheme() + ".vc_currency_all c " +
            "  WHERE 1=1 ";
		
		ArrayList<bcFeautureParam> pParam = initParamArray();
        if (!isEmpty(pFind)) {
    		mySQL = mySQL + 
    			" AND (UPPER(TO_CHAR(cd_currency)) LIKE UPPER('%'||?||'%') OR " +
    			"	   UPPER(scd_currency) LIKE UPPER('%'||?||'%') OR " +
    			"	   UPPER(name_currency) LIKE UPPER('%'||?||'%') OR " +
    			"	   UPPER(sname_currency) LIKE UPPER('%'||?||'%') OR " +
    			"	   UPPER(cd_currency_in_telgr_hex) LIKE UPPER('%'||?||'%')) ";
    		for (int i=0; i<5; i++) {
    			pParam.add(new bcFeautureParam("string", pFind));
        	}
    	}
        if (!isEmpty(pIsUsed)) {
    		mySQL = mySQL + 
    			" AND is_used = ? ";
    		pParam.add(new bcFeautureParam("int", pIsUsed));
    	}
        mySQL = mySQL + 
            "  ORDER BY is_used desc, cd_currency ";
		
    	return getHeaderParamHTML(
    			mySQL,
    			pParam,
                1,
                "D",
            	0,
            	"",
    			"../crm/setup/currencyspecs.jsp?id=:FIELD1:", 
    			currencyXML,
    			pPrint);
    } // getCurrenciesHTML

    public String getCardStatusHeadHTML(String pFind, String pIdStatus, String p_beg, String p_end, String pPrint) { 
		
		ArrayList<bcFeautureParam> pParam = initParamArray();
        
    	String mySQL = 
    		" SELECT rn, name_card_status, telgr_name_card_status, " +
    		"            day_next_online, categories_count, id_club, id_card_status" + 
			"   FROM (SELECT ROWNUM rn, id_club, id_card_status, name_card_status, telgr_name_card_status, " +
			"                day_next_online, " +
			"                CASE WHEN categories_count > 0 " +
			"                     THEN '<font color=\"red\"><b>'||TO_CHAR(categories_count)||'</b></font>' " +
			"                     ELSE TO_CHAR(categories_count)" +
			"                END categories_count " + 
			"           FROM (SELECT id_club, id_card_status, name_card_status, telgr_name_card_status, " +
			"                        day_next_online, categories_count" + 
			"                   FROM " + getGeneralDBScheme()+".vc_card_status_club_all a " +
			"                  WHERE 1=1 ";
    	if (!("".equalsIgnoreCase(pIdStatus))) {
    		mySQL = mySQL + " AND id_card_status = ? ";
    		pParam.add(new bcFeautureParam("int", pIdStatus));
    	}
    	if (!isEmpty(pFind)) {
    		mySQL = mySQL + 
    			" AND (UPPER(TO_CHAR(id_card_status)) LIKE UPPER('%'||?||'%') OR " +
    			"	   UPPER(name_card_status) LIKE UPPER('%'||?||'%') OR " +
    			"	   UPPER(telgr_name_card_status) LIKE UPPER('%'||?||'%')) ";
    		for (int i=0; i<3; i++) {
    			pParam.add(new bcFeautureParam("string", pFind));
        	}
    	}

		pParam.add(new bcFeautureParam("int", p_end));
		pParam.add(new bcFeautureParam("int", p_beg));
		
        mySQL = mySQL + 
    		" 				   ORDER BY id_club, name_card_status) " +
    		"          WHERE ROWNUM < ?" +
    		"		 ) " +
    		"  WHERE rn >= ? ";
    	
    	return getHeaderParamHTML(
    			mySQL,
    			pParam,
                2,
                "D",
            	0,
            	"",
                "../crm/cards/cardsettingspecs.jsp?id_club=:FIELD6:&id=:FIELD7:", 
    			cardsettingXML,
    			pPrint);
    } //getCardCategoriesHeadHTML()

    public String getCardOnlinePayTypeHeadHTML(String pFind, String pExistFlag, String p_beg, String p_end, String pPrint) { 
		
		ArrayList<bcFeautureParam> pParam = initParamArray();
        
    	String mySQL = 
    		" SELECT rn, name_club_online_pay_type,  " +
    		"        desc_club_online_pay_type, term_card_req_club_pay_id_def, " +
    		"        DECODE(need_calc_pin, " +
    		"               'Y', '<font color=\"red\"><b>'||need_calc_pin_tsl||'</b></font>', " +
    		"               need_calc_pin_tsl" +
    		"        ) need_calc_pin_tsl, " + 
			"        DECODE(exist_club_online_pay_type, " +
    		"               'N', '<font color=\"red\"><b>'||exist_club_online_pay_type_tsl||'</b></font>', " +
    		"               exist_club_online_pay_type_tsl " +
    		"        ) exist_club_online_pay_type_tsl, sname_club, id_club, id_club_online_pay_type " + 
			"   FROM (SELECT ROWNUM rn, id_club_online_pay_type, name_club_online_pay_type,  " +
    		"                desc_club_online_pay_type, term_card_req_club_pay_id_def, " +
    		"                need_calc_pin, need_calc_pin_tsl, exist_club_online_pay_type, " +
    		"                exist_club_online_pay_type_tsl, sname_club, id_club " + 
			"           FROM (SELECT id_club_online_pay_type, name_club_online_pay_type,  " +
    		"                        desc_club_online_pay_type, term_card_req_club_pay_id_def, " +
    		"                        need_calc_pin, need_calc_pin_tsl, exist_club_online_pay_type, " +
    		"                        exist_club_online_pay_type_tsl, sname_club, id_club " + 
			"                   FROM " + getGeneralDBScheme()+".vc_club_online_pay_type_cl_all a " +
			"                  WHERE 1=1 ";
    	if (!isEmpty(pExistFlag)) {
    		mySQL = mySQL + " AND exist_club_online_pay_type = ? ";
    		pParam.add(new bcFeautureParam("string", pExistFlag));
    	}
    	if (!isEmpty(pFind)) {
    		mySQL = mySQL + 
    			" AND (UPPER(name_club_online_pay_type) LIKE UPPER('%'||?||'%') OR " +
    			"	   UPPER(desc_club_online_pay_type) LIKE UPPER('%'||?||'%')) ";
    		for (int i=0; i<2; i++) {
    			pParam.add(new bcFeautureParam("string", pFind));
        	}
    	}

		pParam.add(new bcFeautureParam("int", p_end));
		pParam.add(new bcFeautureParam("int", p_beg));
		
        mySQL = mySQL + 
    		" 				   ORDER BY name_club_online_pay_type) " +
    		"          WHERE ROWNUM < ?" +
    		"		 ) " +
    		"  WHERE rn >= ? ";
    	
    	return getHeaderParamHTML(
    			mySQL,
    			pParam,
                2,
                "D",
            	0,
            	"",
                "../crm/club/online_pay_typespecs.jsp?id=:FIELD9:&id_club=:FIELD8:", 
    			clubXML,
    			pPrint);
    } //getCardCategoriesHeadHTML()

    public String getCardCategoriesHeadHTML(String filtrValue, String p_beg, String p_end, String pPrint) {
		
		ArrayList<bcFeautureParam> pParam = initParamArray();
        
    	String mySQL = 
    		" SELECT rn, name_card_status, name_category, max_bon_st_frmt, bonus_transfer_term, " +
			"        max_disc_st_frmt, bonus_calc_term, id_category " +
			"   FROM (SELECT ROWNUM rn, name_card_status, name_category, " +
			"                max_bon_st_frmt, bonus_transfer_term, " +
			"                max_disc_st_frmt, bonus_calc_term, id_card_status, id_category " +
			"           FROM (SELECT a.name_card_status, a.name_category, " +
			"                        a.max_bon_st_frmt, a.bonus_transfer_term, " +
			"                        a.max_disc_st_frmt, a.bonus_calc_term, a.id_card_status, a.id_category " +
			"                   FROM " + getGeneralDBScheme()+".vc_card_category_club_all a ";
    	if (!("".equalsIgnoreCase(filtrValue))) {
    		mySQL = mySQL + " WHERE a.id_card_status = ? ";
    		pParam.add(new bcFeautureParam("int", filtrValue));
    	}
		
    	mySQL = mySQL + 
    		" 				   ORDER BY a.name_card_status, a.name_category) " +
    		"          WHERE ROWNUM < ? " +
    		"		 ) " +
    		"  WHERE rn >= ? ";
    	
    	pParam.add(new bcFeautureParam("int", p_end));
		pParam.add(new bcFeautureParam("int", p_beg));
    	
    	return getHeaderParamHTML(
    			mySQL,
    			pParam,
                1,
                "D",
            	0,
            	"",
                "../crm/cards/cardsettingspecs.jsp?id_category=:FIELD8:"+"&filtr="+filtrValue+"", 
    			cardsettingXML,
    			pPrint);
    } //getCardCategoriesHeadHTML()
    
    public String getSMSProfileHeadHTML(String cdProfileState, String pFind, String p_beg, String p_end, String pPrint) {
    	
    	ArrayList<bcFeautureParam> pParam = initParamArray();
    	
    	String mySQL = 
    		" SELECT rn, name_sms_profile, desc_sms_profile, name_profile_state, " + 
			"		 name_sms_device_type, device_serial_number, mobile_operator, " +
			"        phone_mobile, id_sms_profile " +
			"   FROM (SELECT ROWNUM rn, id_sms_profile, name_sms_profile, desc_sms_profile, name_profile_state, " + 
			"				 name_sms_device_type, device_serial_number, mobile_operator, phone_mobile " +
			"           FROM (SELECT id_sms_profile, name_sms_profile, desc_sms_profile, " +
			"        		 		 DECODE(cd_profile_state, " +
            "               				'INACTIVE', '<font color=\"red\"><b>'||name_profile_state||'</b></font>', " +
            "               				'ACTIVE', '<font color=\"green\"><b>'||name_profile_state||'</b></font>', " +
            "               				name_profile_state" +
            "        		 		 ) name_profile_state, " + 
			"						 name_sms_device_type, device_serial_number, mobile_operator, phone_mobile " +
			"                   FROM " + getGeneralDBScheme()+".vc_ds_sms_profile_club_all a " +
			"                  WHERE 1=1 ";
    	if (!isEmpty(cdProfileState)) {
    		mySQL = mySQL + " AND cd_profile_state = ? ";
    		pParam.add(new bcFeautureParam("string", cdProfileState));
    	}
    	if (!isEmpty(pFind)) {
    		mySQL = mySQL + 
    			" AND (UPPER(TO_CHAR(id_sms_profile)) LIKE UPPER('%'||?||'%') OR " +
    			"	   UPPER(name_sms_profile) LIKE UPPER('%'||?||'%') OR " +
    			"	   UPPER(desc_sms_profile) LIKE UPPER('%'||?||'%') OR " +
    			"	   UPPER(device_serial_number) LIKE UPPER('%'||?||'%') OR " +
    			"	   UPPER(name_sms_device_type) LIKE UPPER('%'||?||'%') OR " +
    			"	   UPPER(mobile_operator) LIKE UPPER('%'||?||'%') OR " +
    			"	   UPPER(phone_mobile) LIKE UPPER('%'||?||'%')) ";
    		for (int i=0; i<7; i++) {
    		    pParam.add(new bcFeautureParam("string", pFind));
    		}
    	}
        pParam.add(new bcFeautureParam("int", p_end));
        pParam.add(new bcFeautureParam("int", p_beg));
        mySQL = mySQL + 
    		" 				   ORDER BY a.name_sms_profile) " +
    		"          WHERE ROWNUM < ? " + 
    		"		 ) " +
    		"  WHERE rn >= ? ";    	
    	return getHeaderParamHTML(
    			mySQL,
    			pParam,
                1,
                "D",
            	0,
            	"",
                "../crm/dispatch/settings/sms_profilespecs.jsp?id=:FIELD9:", 
    			smsXML,
    			pPrint);
    }
    
    public String getClientProfileHeadHTML(String pProfileType, String pFind, String p_beg, String p_end, String pPrint) {

    	ArrayList<bcFeautureParam> pParam = initParamArray();
    	
    	String mySQL = 
    		" SELECT rn, name_ds_cl_profile name_profile, " +
    		"        desc_ds_cl_profile desc_profile, name_cl_profile_type name_profile_type, " +
    		"        id_ds_cl_profile id_profile " +
			"   FROM (SELECT ROWNUM rn, id_ds_cl_profile, name_ds_cl_profile, desc_ds_cl_profile, name_cl_profile_type " +
			"           FROM (SELECT a.id_ds_cl_profile, a.name_ds_cl_profile, a.desc_ds_cl_profile, a.name_cl_profile_type " +
			"                   FROM " + getGeneralDBScheme()+".vc_ds_cl_profile_club_all a " +
			"                  WHERE 1=1 ";
    	if (!isEmpty(pProfileType)) {
    		mySQL = mySQL + " AND cd_cl_profile_type = ? ";
    		pParam.add(new bcFeautureParam("string", pProfileType));
    	}
        if (!isEmpty(pFind)) {
    		mySQL = mySQL + 
    			" AND (UPPER(TO_CHAR(id_ds_cl_profile)) LIKE UPPER('%'||?||'%') OR " +
    			"	   UPPER(name_ds_cl_profile) LIKE UPPER('%'||?||'%') OR " +
    			"	   UPPER(desc_ds_cl_profile) LIKE UPPER('%'||?||'%')) ";
    		for (int i=0; i<3; i++) {
    		    pParam.add(new bcFeautureParam("string", pFind));
    		}
    	}    	
        
        pParam.add(new bcFeautureParam("int", p_end));
        pParam.add(new bcFeautureParam("int", p_beg));
        
        mySQL = mySQL + 
    		" 				   ORDER BY a.name_ds_cl_profile) " +
    		"          WHERE ROWNUM < ? " +
    		"		 ) " +
    		"  WHERE rn >= ? ";
    	
    	return getHeaderParamHTML(
    			mySQL,
    			pParam,
                1,
                "D",
            	0,
            	"",
                "../crm/dispatch/settings/client_profilespecs.jsp?id=:FIELD5:", 
    			messageXML,
    			pPrint);
    }
    
    public String getPartnerProfileHeadHTML(String pFind, String p_beg, String p_end, String pPrint) {
    	
    	ArrayList<bcFeautureParam> pParam = initParamArray();
    	
    	String mySQL = 
    		" SELECT rn, name_ds_pt_profile name_profile, desc_ds_pt_profile desc_profile, id_ds_pt_profile id_profile " +
			"   FROM (SELECT ROWNUM rn, id_ds_pt_profile, name_ds_pt_profile, desc_ds_pt_profile " +
			"           FROM (SELECT a.id_ds_pt_profile, a.name_ds_pt_profile, a.desc_ds_pt_profile " +
			"                   FROM " + getGeneralDBScheme()+".vc_ds_pt_profile_club_all a ";
        if (!isEmpty(pFind)) {
    		mySQL = mySQL + 
    			" AND (UPPER(TO_CHAR(a.id_ds_pt_profile)) LIKE UPPER('%'||?||'%') OR " +
    			"	   UPPER(a.name_ds_pt_profile) LIKE UPPER('%'||?||'%') OR " +
    			"	   UPPER(a.desc_ds_pt_profile) LIKE UPPER('%'||?||'%')) ";
    		for (int i=0; i<3; i++) {
    		    pParam.add(new bcFeautureParam("string", pFind));
    		}
    	}
    	
        pParam.add(new bcFeautureParam("int", p_end));
        pParam.add(new bcFeautureParam("int", p_beg));
        
        mySQL = mySQL + 
    		" 				   ORDER BY a.name_ds_pt_profile) " +
    		"          WHERE ROWNUM < ? " +
    		"		 ) " +
    		"  WHERE rn >= ? ";
    	
    	return getHeaderParamHTML(
    			mySQL,
    			pParam,
                1,
                "D",
            	0,
            	"",
                "../crm/dispatch/settings/partner_profilespecs.jsp?id=:FIELD4:", 
    			messageXML,
    			pPrint);
    }
    
    public String getQuestionnairesHTML(String pOperationType, String pFind, String state_quest, String p_beg, String p_end, String pPrint) {
        StringBuilder html = new StringBuilder();
        PreparedStatement st = null; 

        ArrayList<bcFeautureParam> pParam = initParamArray();
        
        String mySQL = 
           	" SELECT rn, id_quest_int, date_card_sale_frmt, state_quest_tsl, date_import_frmt, cd_card, full_name_nat_prs, " +
           	"        discount_card_number, discount_card_percent, " +
           	"		 state_quest, is_discount_card_changed " +
           	"   FROM (SELECT ROWNUM rn, id_quest_int, date_card_sale_frmt," +
           	"                cd_card, surname||' '||name||' '||patronymic full_name_nat_prs, " +
           	"                discount_card_number, discount_card_percent, " +
           	"                creation_date_frmt quest_add_date, date_import_frmt, " +
           	"        		 DECODE(state_quest, " +
            "               		'NEW', '<b>'||state_quest_tsl||'</b>', " +
            "               		'ERROR', '<font color=\"red\"><b>'||state_quest_tsl||'</b></font>', " +
            "               		'IMPORTED', '<font color=\"green\">'||state_quest_tsl||'</font>', " +
            "               		state_quest_tsl" +
            "        		 ) state_quest_tsl, " +
            "                state_quest, NVL(is_discount_card_changed, 'N') is_discount_card_changed " +
            "           FROM (SELECT * " +
            "                   FROM " + getGeneralDBScheme()+".vc_quest_int_club_all " +
            "                  WHERE 1=1 ";
        if (!isEmpty(pFind)) {
           	mySQL = mySQL + " AND (TO_CHAR(id_quest_int) LIKE UPPER('%'||?||'%') " +
           					"    OR UPPER(discount_card_number) LIKE UPPER('%'||?||'%') " +
           					"    OR UPPER(cd_card) LIKE UPPER('%'||?||'%') " +
           					"    OR UPPER(surname) LIKE UPPER('%'||?||'%') " +
           					"    OR UPPER(name) LIKE UPPER('%'||?||'%') " +
           					"    OR UPPER(patronymic) LIKE UPPER('%'||?||'%')) ";
           	for (int i=0; i<6; i++) {
           	    pParam.add(new bcFeautureParam("string", pFind));
           	}
        }
        if (!isEmpty(state_quest)) {
        	if ("NOT_IMPORTED".equalsIgnoreCase(state_quest)) {
               	mySQL = mySQL + " AND state_quest IN ('NEW', 'ERROR') ";
        	} else {
               	mySQL = mySQL + " AND state_quest = ? ";
               	pParam.add(new bcFeautureParam("string", state_quest));
        	}
        }

        pParam.add(new bcFeautureParam("int", p_end));
        pParam.add(new bcFeautureParam("int", p_beg));
        
        mySQL = mySQL + " ORDER BY id_quest_int DESC) WHERE ROWNUM < ? ) WHERE rn >= ? ";
        
        boolean hasEditPermission = false;
        
        try{
       	 	if (isEditMenuPermited("CARDS_QUESTIONNAIRE_IMPORT")>0) {
       	 		hasEditPermission = true;
        	}
       	 	if ("Y".equalsIgnoreCase(pPrint)) {
       	 		hasEditPermission = false;
       	 	}
            LOGGER.debug(prepareSQLToLog(mySQL, pParam));
       	 	con = Connector.getConnection(getSessionId());
            st = con.prepareStatement(mySQL);
            st = prepareParam(st, pParam);
            ResultSet rset = st.executeQuery();
            ResultSetMetaData mtd = rset.getMetaData();
            
            int colCount = mtd.getColumnCount();
            if (hasEditPermission) {
            	html.append("<form method=\"Post\" name=\"updateForm\" id=\"updateForm\" action=\"../crm/cards/questionnaire_importupdate.jsp\" onSubmit=\"return CheckSelect(this);\">\n");
            	html.append("<input type=\"hidden\" name=\"type\" value=\"general\">\n");
           	 	html.append("<input type=\"hidden\" name=\"action\" value=\"set_change\">\n");
           	 	html.append("<input type=\"hidden\" name=\"process\" value=\"yes\">\n");
            }
            
            if ("Y".equalsIgnoreCase(pPrint)) {
            	html.append(getPrintTable());
            } else {
            	html.append(getHeaderTable());
            }
            html.append("<thead><tr>\n");
            for (int i=1; i <= colCount-2; i++) {
            	html.append(getBottomFrameTableTH(questionnaireXML, mtd.getColumnName(i)));
            }
            if (hasEditPermission) {
           	 	html.append("<th>");
	           	html.append(getSubmitButtonAjax3("../crm/cards/questionnaire_importupdate.jsp", "button_interchange", "CheckSelect(document.getElementById('updateForm'))") + "<br>");
	           	html.append("<input name=\"mainCheck\" id=\"mainCheck\" type=\"checkbox\" onclick=\"CheckAll(this,'chb')\"></th>\n");
            } else {
            	html.append("<th>&nbsp;</th>");
            }
      
           html.append("<tr><tbody>\n");

           int rowCount = 0;
            while (rset.next())
            {
             	rowCount++;
                String idQuest="id_"+rset.getString("ID_QUEST_INT");
                String tprvCheck="prv_"+idQuest;
                String tCheck="chb_"+idQuest;
                
            	html.append("<tr id=\"elem_"+idQuest+"\" onmouseout=\"coloredOut(this)\" onmouseover=\"coloredOver(this,1)\">");
           	 	for (int i=1; i <= colCount-2; i++) {
           	 		if ("Y".equalsIgnoreCase(pPrint)) {
           	 			html.append("<td>" + getValue2(rset.getString(i)) + "</td>\n");
           	 		} else {
           	 			html.append(formatTDData(mtd.getColumnName(i), mtd.getColumnType(i), "")+getHyperLinkHeaderFirst() + "../crm/cards/questionnaire_importspecs.jsp?id="+rset.getString("ID_QUEST_INT")+getHyperLinkMiddle()+getValue2(rset.getString(i))+getHyperLinkEnd()+"</td>\n");
           	 		}
           	 	}
           	 	if (hasEditPermission) {
           	 		if ("Y".equalsIgnoreCase(rset.getString("IS_DISCOUNT_CARD_CHANGED"))) {
           	 			html.append("<td align=\"center\"><input type=\"checkbox\" name=\""+tCheck+"\" value=\"Y\" id=\""+tCheck+"\" checked onclick=\"return CheckCB(this);\" style=\"background-color: #BBBBBB;\"></td>\n");
           	 			html.append("<INPUT type=hidden  value=\"Y\" name="+tprvCheck+">");
                    } else {
           	 			html.append("<td align=\"center\"><input type=\"checkbox\" name=\""+tCheck+"\" value=\"Y\" id=\""+tCheck+"\" onclick=\"return CheckCB(this);\"></td>\n");
           	 		}
           	 	} else {
	           	 	if ("Y".equalsIgnoreCase(rset.getString("IS_DISCOUNT_CARD_CHANGED"))) {
	       	 			html.append("<td align=\"center\"><input type=\"checkbox\" name=\""+tCheck+"\"  value=\"\" id=\""+tCheck+"\" disabled readonly checked style=\"background-color: #BBBBBB;\"></td>\n");
	       	 		} else {
	       	 			html.append("<td align=\"center\"><input type=\"checkbox\" name=\""+tCheck+"\" value=\"\" id=\""+tCheck+"\" disabled readonly></td>\n");
	       	 		}
           	 	}
            }
            html.append("<input type=hidden value="+rowCount+" name=rowCount>");
            html.append("</tbody></table>\n");
            html.append("</form\n");
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
    } // getQuestionnairesHTML

    public String getQuestionnairePackHeadHTML(String pFiltr, String pState, String pImportState, String p_beg, String p_end, String pPrint) {

    	ArrayList<bcFeautureParam> pParam = initParamArray();
    	
        String mySQL = 
        	" SELECT 0 rn, -1 id_quest_pack, NULL date_reception_pack, null sname_jur_pr_who_has_sold_card," +
        	"        a.meaning name_serv_plce_where_card_sold, NULL state_pack, " +
        	"        NULL import_state_pack, NULL expected_quest_quantity, " +
        	"        NULL real_quest_quantity, NULL imported_quest_quantity, 'OPENED' state_pack2 " +
        	"   FROM " + getGeneralDBScheme()+".vc_lookup_values a " +
        	"  WHERE a.name_lookup_type = 'NAT_PRS_INT_PACK_NONE' " +
        	"  UNION ALL " +
        	" SELECT rn, id_quest_pack, date_reception_pack, sname_jur_pr_who_has_sold_card, " +
        	"        name_serv_plce_where_card_sold, " +
        	"        DECODE(state_pack2, 'CLOSED', '<font color=\"green\">'||state_pack||'</font>', state_pack) state_pack, " +
        	"        DECODE(all_quest_not_imported, 'Y', '<font color=\"red\"><b>'||import_state_pack||'</b></font>', import_state_pack) import_state_pack, " +
        	"        DECODE(all_quest_not_imported, 'Y', '<font color=\"red\"><b>'||expected_quest_quantity||'</b></font>', expected_quest_quantity) expected_quest_quantity, " +
        	"        DECODE(all_quest_not_imported, 'Y', '<font color=\"red\"><b>'||real_quest_quantity||'</b></font>', real_quest_quantity) real_quest_quantity, " +
        	"        DECODE(all_quest_not_imported, 'Y', '<font color=\"red\"><b>'||imported_quest_quantity||'</b></font>', imported_quest_quantity) imported_quest_quantity, " +
        	"        state_pack2 " +
			"   FROM (SELECT ROWNUM rn, id_quest_pack, date_reception_pack_frmt date_reception_pack, " +
			"                sname_jur_pr_who_has_sold_card, " +
			"                name_serv_plce_where_card_sold, state_pack_tsl state_pack, import_state_pack_tsl import_state_pack," +
			"                expected_quest_quantity, real_quest_quantity, imported_quest_quantity, state_pack state_pack2," +
			"                CASE WHEN NVL(expected_quest_quantity,0) <> NVL(real_quest_quantity,0) THEN 'Y' " +
			"                     WHEN NVL(expected_quest_quantity,0) <> NVL(imported_quest_quantity,0) THEN 'Y' " +
			"                     WHEN NVL(real_quest_quantity,0) <> NVL(imported_quest_quantity,0) THEN 'Y' " +
			"                     ELSE 'N'" +
			"                END all_quest_not_imported " +
			"   		FROM (SELECT * " +
			"                   FROM " + getGeneralDBScheme()+".vc_quest_pack_club_all " +
			"                  WHERE 1=1 ";
        if (!isEmpty(pState)) {
        	mySQL = mySQL +	" AND state_pack = ? ";
        	pParam.add(new bcFeautureParam("string", pState));
        }
        if (!isEmpty(pImportState)) {
        	mySQL = mySQL +	" AND import_state_pack = ? ";
        	pParam.add(new bcFeautureParam("string", pImportState));
        }
        if (!isEmpty(pFiltr)) {
           	mySQL = mySQL + " AND (TO_CHAR(id_quest_pack) LIKE UPPER('%'||?||'%') " +
           					"    OR UPPER(date_reception_pack_frmt) LIKE UPPER('%'||?||'%') " +
           					"    OR UPPER(sname_jur_pr_who_has_sold_card) LIKE UPPER('%'||?||'%') " +
           					"    OR UPPER(name_serv_plce_where_card_sold) LIKE UPPER('%'||?||'%')) ";
           	
           	for (int i=0; i<4; i++) {
           	    pParam.add(new bcFeautureParam("string", pFiltr));
           	}
        }

        pParam.add(new bcFeautureParam("int", p_end));
        pParam.add(new bcFeautureParam("int", p_beg));
        
        mySQL = mySQL +
        	"				  ORDER BY id_quest_pack DESC) " +
        	"          WHERE ROWNUM < ? ) WHERE rn>= ? ";
        
        return getHeaderParamHTML(
        		mySQL,
        		pParam,
                1,
                "D",
            	0,
            	"",
            	"../crm/cards/questionnaire_packspecs.jsp?id=:FIELD2:", 
            	questionnaireXML,
            	pPrint);
    }

    public String getQuestionnaireFormHeadHTML(String pFindString, String pState, String p_beg, String p_end, String pPrint) {

    	ArrayList<bcFeautureParam> pParam = initParamArray();
    	
    	String mySQL = 
        	" SELECT rn, sname_quest_form, desc_quest_form, name_quest_form_state, " +
			"        date_beg_quest_form_frmt, date_end_quest_form_frmt, id_quest_form " +
			"   FROM (SELECT ROWNUM rn, id_quest_form, sname_quest_form, desc_quest_form, " +
			"                DECODE(cd_quest_form_state, " +
			"						'CONFIRMED', '<font color=\"green\"><b>'||name_quest_form_state||'</b></font>', " +
			"						'IN_WORK', '<font color=\"blue\"><b>'||name_quest_form_state||'</b></font>', " +
			"						'CANCELED', '<font color=\"red\"><b>'||name_quest_form_state||'</b></font>', " +
			"						name_quest_form_state)  " +
        	"				 name_quest_form_state, " +
			"                date_beg_quest_form_frmt, date_end_quest_form_frmt " +
			"   		FROM (SELECT id_quest_form, sname_quest_form, desc_quest_form, cd_quest_form_state, name_quest_form_state, " +
			"                        date_beg_quest_form_frmt, date_end_quest_form_frmt " +
			"                   FROM " + getGeneralDBScheme()+".vc_quest_form_club_all " +
			"                  WHERE 1=1 ";
        if (!isEmpty(pState)) {
        	mySQL = mySQL +	" AND cd_quest_form_state = ? ";
        	pParam.add(new bcFeautureParam("string", pState));
        }
        if (!isEmpty(pFindString)) {
           	mySQL = mySQL + 
           		" AND (TO_CHAR(id_quest_form) LIKE UPPER('%'||?||'%') " +
           		"  OR UPPER(sname_quest_form) LIKE UPPER('%'||?||'%') " +
           		"  OR UPPER(name_quest_form) LIKE UPPER('%'||?||'%') " +
           		"  OR UPPER(desc_quest_form) LIKE UPPER('%'||?||'%') " +
           		"  OR UPPER(date_beg_quest_form_frmt) LIKE UPPER('%'||?||'%') " +
           		"  OR UPPER(date_end_quest_form_frmt) LIKE UPPER('%'||?||'%')) ";
           	for (int i=0; i<6; i++) {
           	    pParam.add(new bcFeautureParam("string", pFindString));
           	}
        }

        pParam.add(new bcFeautureParam("int", p_end));
        pParam.add(new bcFeautureParam("int", p_beg));
        
        mySQL = mySQL +
        	"				  ORDER BY name_quest_form) " +
        	"          WHERE ROWNUM < ? ) WHERE rn>= ? ";
        
        return getHeaderParamHTML(
        		mySQL,
        		pParam,
                1,
                "D",
            	0,
            	"",
            	"../crm/cards/questionnaire_formspecs.jsp?id=:FIELD7:", 
            	questionnaireXML,
            	pPrint);
    }

    public String getQuestionnaireImportSettingsHeadHTML(String pFind, String p_beg, String p_end, String pPrint) {
    	
    	ArrayList<bcFeautureParam> pParam = initParamArray();
    	
        String mySQL = 
        	" SELECT rn, id_import_setting, name_import_setting, begin_action_date, end_action_date " +
			"   FROM (SELECT ROWNUM rn, id_import_setting, name_import_setting, " +
			"                begin_action_date_frmt begin_action_date, " +
			"                end_action_date_frmt end_action_date " +
			"   		FROM (SELECT * " +
			"                   FROM " + getGeneralDBScheme()+".vc_quest_import_set_club_all ";
        if (!isEmpty(pFind)) {
    		mySQL = mySQL + 
    			" WHERE (UPPER(TO_CHAR(id_import_setting)) LIKE UPPER('%'||?||'%') OR " +
    			"	     UPPER(name_import_setting) LIKE UPPER('%'||?||'%') OR " +
    			"	     UPPER(begin_action_date_frmt) LIKE UPPER('%'||?||'%') OR " +
    			"	     UPPER(end_action_date_frmt) LIKE UPPER('%'||?||'%')) ";
    		for (int i=0; i<4; i++) {
    		    pParam.add(new bcFeautureParam("string", pFind));
    		}
    	}

        pParam.add(new bcFeautureParam("int", p_end));
        pParam.add(new bcFeautureParam("int", p_beg));
        
        mySQL = mySQL +
        	"				  ORDER BY begin_action_date DESC) " +
        	"          WHERE ROWNUM < ? ) WHERE rn >= ? ";
        
        return getHeaderParamHTML(
        		mySQL,
        		pParam,
                0,
                "D",
            	0,
            	"",
            	"../crm/cards/quest_settingspecs.jsp?id=:FIELD2:", 
            	questionnaireXML,
            	pPrint);
    } 

    public String getSAMHeadHTML(String pCdCardType, String pFind, String p_beg, String p_end, String pPrint) {
    	
    	ArrayList<bcFeautureParam> pParam = initParamArray();
    	
        String mySQL = 
        	" SELECT rn, id_sam, id_sam_hex, sam_serial_number, name_sam_type,  " +
			"        expiry_date_frmt, name_user " +
			"   FROM (SELECT ROWNUM rn, id_sam, id_sam_hex, sam_serial_number, " +
			"                DECODE(cd_sam_type, " +
            "          				'M', '<font color=\"green\"><b>'||name_sam_type||'</b></font>', " +
            "          				'V', '<font color=\"blue\"><b>'||name_sam_type||'</b></font>', " +
            "          				name_sam_type" +
            "  		 		 ) name_sam_type, " +
			"				 expiry_date_frmt, name_user " +
			"   		FROM (SELECT * " +
			"                   FROM " + getGeneralDBScheme()+".vc_sam_club_all " +
			"				   WHERE 1=1 ";
        if (!isEmpty(pCdCardType)) {
        	mySQL = mySQL + " AND cd_sam_type = ? ";
        	pParam.add(new bcFeautureParam("string", pCdCardType));
        }
        if (!isEmpty(pFind)) {
    		mySQL = mySQL + 
    		    " AND (TO_CHAR(id_sam) LIKE UPPER('%'||?||'%') OR " +
    			"      UPPER(id_sam_hex) LIKE UPPER('%'||?||'%') OR " +
    			"      UPPER(sam_serial_number) LIKE UPPER('%'||?||'%') OR " +
    			"      UPPER(expiry_date_frmt) LIKE UPPER('%'||?||'%') OR " +
    			"      UPPER(name_user) LIKE UPPER('%'||?||'%')) ";
    		for (int i=0; i<5; i++) {
    		    pParam.add(new bcFeautureParam("string", pFind));
    		}
    	}

        pParam.add(new bcFeautureParam("int", p_end));
        pParam.add(new bcFeautureParam("int", p_beg));
        
        mySQL = mySQL + "ORDER BY id_sam) WHERE ROWNUM < ? ) WHERE rn >= ?";
        
        return getHeaderParamHTML(
        		mySQL,
        		pParam,
                0,
                "D",
            	0,
            	"",
            	"../crm/clients/samspecs.jsp?id=:FIELD2:", 
            	samXML,
            	pPrint);
    } 

    public String getContactPrsHeadHTML(String pCdPost, String pFind, String p_beg, String p_end, String pPrint) {
    	
    	ArrayList<bcFeautureParam> pParam = initParamArray();
    	
        String mySQL = 
        	" SELECT rn, name_contact_prs, sname_service_place_work contact_prs_place, name_post, cd_card1, phone_mobile, phone_work, email_work, " +
        	"        id_nat_prs_role, row_count "+
            "   FROM (SELECT ROWNUM rn, name_contact_prs, sname_service_place_work, name_post, phone_mobile, phone_work, email_work, cd_card1, " +
        	"  		         id_nat_prs_role, row_count " +
			"   		FROM (SELECT name_contact_prs, sname_service_place_work, name_post, phone_mobile, phone_work, email_work, cd_card1, " +
			"                        id_nat_prs_role, count(*) over() as row_count " +
			"                   FROM " + getGeneralDBScheme()+".vc_contact_prs_priv_all " +
			"				   WHERE 1=1 ";
        if (!isEmpty(pCdPost)) {
        	mySQL = mySQL + " AND cd_post = ? ";
        	pParam.add(new bcFeautureParam("string", pCdPost));
        }
        if (!isEmpty(pFind)) {
    		mySQL = mySQL + " AND (UPPER(TO_CHAR(id_nat_prs_role)) LIKE UPPER('%'||?||'%') OR " +
    			"UPPER(name_contact_prs) LIKE UPPER('%'||?||'%') OR " +
    			"UPPER(sname_service_place_work) LIKE UPPER('%'||?||'%') OR " +
    			"UPPER(phone_mobile) LIKE UPPER('%'||?||'%') OR " +
    			"UPPER(phone_work) LIKE UPPER('%'||?||'%') OR " +
    			"UPPER(email_work) LIKE UPPER('%'||?||'%')) ";
    		for (int i=0; i<6; i++) {
    		    pParam.add(new bcFeautureParam("string", pFind));
    		}
    	}
        
        pParam.add(new bcFeautureParam("int", p_end));
        pParam.add(new bcFeautureParam("int", p_beg));
        
        mySQL = mySQL + "ORDER BY name_contact_prs) a WHERE ROWNUM < ? ) WHERE rn >= ?";
        
        return getHeaderParamHTML(
        		mySQL,
        		pParam,
                2,
                "D",
            	0,
            	"",
            	"../crm/clients/contact_prsspecs.jsp?id=:FIELD9:", 
            	contactXML,
            	pPrint);
    } 
    
    /*public String getPrivateOfficePartnersUsersHeadHTML(String pCdPost, String pFind, String p_beg, String p_end, String pPrint) {
    	
    	ArrayList<bcFeautureParam> pParam = initParamArray();
    	
        String mySQL = 
        	" SELECT rn, username_contact_prs, name_contact_prs, name_role, name_joffice_state, " +
			"        need_login_by_card_tsl, date_last_connect_frmt, row_count, id_joffice_user " +
			"   FROM (SELECT ROWNUM rn, id_joffice_user, username_contact_prs, name_joffice_state, name_role, " +
			"                name_contact_prs, sname_jur_prs, name_service_place, name_post, " +
			"                need_login_by_card_tsl, date_last_connect_frmt, row_count " +
			"   		FROM (SELECT id_joffice_user, username_contact_prs, " +
			"                        DECODE(cd_joffice_state, " +
            "               				'OPENED', '<b><font color=\"green\">'||name_joffice_state||'</font></b>', " +
            "               				'<b><font color=\"red\">'||name_joffice_state||'</font></b>'" +
            "        		 		 ) name_joffice_state, " +
			"                        name_role, " +
			"                        name_contact_prs, sname_jur_prs, name_service_place, name_post, " +
			"                        DECODE(need_login_by_card, " +
            "               				'Y', '<b><font color=\"green\">'||need_login_by_card_tsl||'</font></b>', " +
            "               				'<b><font color=\"red\">'||need_login_by_card_tsl||'</font></b>'" +
            "        		 		 ) need_login_by_card_tsl, " +
			"						 date_last_connect_frmt, " +
			"                        count(*) over() as row_count " +
			"                   FROM " + getGeneralDBScheme()+".vc_contact_prs_jof_club_all " +
			"				   WHERE 1=1 ";
        if (!isEmpty(pCdPost)) {
        	mySQL = mySQL + " AND cd_post = ? ";
        	pParam.add(new bcFeautureParam("string", pCdPost));
        }
        if (!isEmpty(pFind)) {
    		mySQL = mySQL + " AND (UPPER(TO_CHAR(id_joffice_user)) LIKE UPPER('%'||?||'%') OR " +
    			"UPPER(username_contact_prs) LIKE UPPER('%'||?||'%') OR " +
    			"UPPER(name_contact_prs) LIKE UPPER('%'||?||'%')) ";
    		for (int i=0; i<3; i++) {
    		    pParam.add(new bcFeautureParam("string", pFind));
    		}
    	}
        
        pParam.add(new bcFeautureParam("int", p_end));
        pParam.add(new bcFeautureParam("int", p_beg));
        
        mySQL = mySQL + "ORDER BY username_contact_prs) a WHERE ROWNUM < ? ) WHERE rn >= ? ";
        
        return getHeaderParamHTML(
        		mySQL,
        		pParam,
                2,
                "D",
            	0,
            	"",
            	"../crm/private_office/partners/userspecs.jsp?id=:FIELD9:", 
            	contactXML,
            	pPrint);
    } */
    /*
    public String getPrivateOfficePartnersActionsHeadHTML(String pActionType, String pActionState, String pFind, String p_beg, String p_end, String pPrint) {

    	ArrayList<bcFeautureParam> pParam = initParamArray();

    	String mySQL = 
    		"SELECT rn, sname_office_private, name_contact_prs, username_contact_prs, date_op_action_frmt, " +
    		"       name_op_action_type, name_report, name_op_action_state, id_op_action " +
    		"  FROM (SELECT ROWNUM rn, id_op_action, sname_office_private, name_contact_prs, username_contact_prs, date_op_action_frmt, name_op_action_type, " + 
    		"				name_report, name_op_action_state " +
    		"          FROM (SELECT id_op_action, sname_office_private, name_contact_prs, username_contact_prs, date_op_action_frmt, " +
    		"        		 		DECODE(cd_op_action_type, " +
            "               			   'LOGIN', '<font color=\"blue\"><b>'||name_op_action_type||'</b></font>', " +
            "               			   'REPORT_EXECUTION', '<font color=\"green\"><b>'||name_op_action_type||'</b></font>', " +
            "               			   'UPDATE_CLIENT_PARAM', '<font color=\"red\"><b>'||name_op_action_type||'</b></font>', " +
            "               			   'UPDATE_CONTACT_PRS_PARAM', '<font color=\"red\"><b>'||name_op_action_type||'</b></font>', " +
            "               			   name_op_action_type" +
            "        		 		) name_op_action_type, " +
    		"						cd_report||'. '||name_report name_report, " +
    		"        		 		DECODE(cd_op_action_state, " +
            "               			   'NEW', '<font color=\"blue\"><b>'||name_op_action_state||'</b></font>', " +
            "               			   'IN_PROCESS', '<font color=\"gray\"><b>'||name_op_action_state||'</b></font>', " +
            "               			   'CREATED', '<font color=\"green\"><b>'||name_op_action_state||'</b></font>', " +
            "               			   'ERROR', '<font color=\"red\"><b>'||name_op_action_state||'</b></font>', " +
            "               			   name_op_action_type" +
            "        		 		) name_op_action_state " +
    		"                  FROM " + getGeneralDBScheme() + ".vc_op_action_partner_club_all ";
    	if (!isEmpty(pFind)) {
    		mySQL = mySQL + 
    			" AND (UPPER(TO_CHAR(id_op_action)) LIKE UPPER('%'||?||'%') OR " +
    			"	     UPPER(username_contact_prs) LIKE UPPER('%'||?||'%') OR " +
    			"	     UPPER(name_contact_prs) LIKE UPPER('%'||?||'%') OR " +
    			"	     UPPER(date_op_action_frmt) LIKE UPPER('%'||?||'%') OR " +
    			"	     UPPER(cd_report||'. '||name_report) LIKE UPPER('%'||?||'%') OR " +
    			"	     UPPER(error_op_action) LIKE UPPER('%'||?||'%')) ";
    		for (int i=0; i<6; i++) {
    		    pParam.add(new bcFeautureParam("string", pFind));
    		}
    	}
    	if (!isEmpty(pActionState)) {
    		mySQL = mySQL + " AND cd_op_action_state = ? ";
    		pParam.add(new bcFeautureParam("string", pActionState));
    	}
    	if (!isEmpty(pActionType)) {
    		mySQL = mySQL + " AND cd_op_action_type = ? ";
    		pParam.add(new bcFeautureParam("string", pActionType));
    	}

    	pParam.add(new bcFeautureParam("int", p_end));
    	pParam.add(new bcFeautureParam("int", p_beg));
    	
        mySQL = mySQL + 
    		"                 ORDER BY date_op_action DESC) "+
    		"         WHERE ROWNUM < ? " +  
    		" ) WHERE rn >= ? ";
        
        return getHeaderParamHTML(
        		mySQL,
        		pParam,
                1,
                "D",
            	0,
            	"",
            	"../crm/private_office/partners/actionspecs.jsp?id=:FIELD9:", 
            	contactXML,
            	pPrint);
    } 
    
    public String getPrivateOfficeClientsActionsHeadHTML(String pActionType, String pActionState, String pFind, String p_beg, String p_end, String pPrint) {

    	ArrayList<bcFeautureParam> pParam = initParamArray();

    	String mySQL = 
    		"SELECT rn, sname_office_private, cd_card2, cd_card1, date_op_action_frmt, name_op_action_type, " + 
    		"		name_report, name_op_action_state, id_op_action " +
    		"  FROM (SELECT ROWNUM rn, id_op_action, sname_office_private, cd_card2, cd_card1, date_op_action_frmt, name_op_action_type, " + 
    		"				name_report, name_op_action_state " +
    		"          FROM (SELECT id_op_action, sname_office_private, cd_card2, cd_card1, date_op_action_frmt, " +
    		"        		 		DECODE(cd_op_action_type, " +
            "               			   'LOGIN', '<font color=\"blue\"><b>'||name_op_action_type||'</b></font>', " +
            "               			   'REPORT_EXECUTION', '<font color=\"green\"><b>'||name_op_action_type||'</b></font>', " +
            "               			   'UPDATE_CLIENT_PARAM', '<font color=\"red\"><b>'||name_op_action_type||'</b></font>', " +
            "               			   'UPDATE_CONTACT_PRS_PARAM', '<font color=\"red\"><b>'||name_op_action_type||'</b></font>', " +
            "               			   name_op_action_type" +
            "        		 		) name_op_action_type, " +
    		"						cd_report||'. '||name_report name_report, " +
    		"        		 		DECODE(cd_op_action_state, " +
            "               			   'NEW', '<font color=\"blue\"><b>'||name_op_action_state||'</b></font>', " +
            "               			   'IN_PROCESS', '<font color=\"gray\"><b>'||name_op_action_state||'</b></font>', " +
            "               			   'CREATED', '<font color=\"green\"><b>'||name_op_action_state||'</b></font>', " +
            "               			   'ERROR', '<font color=\"red\"><b>'||name_op_action_state||'</b></font>', " +
            "               			   name_op_action_type" +
            "        		 		) name_op_action_state " +
    		"                  FROM " + getGeneralDBScheme() + ".vc_op_action_client_club_all ";
    	if (!isEmpty(pFind)) {
    		mySQL = mySQL + 
    			" AND (UPPER(TO_CHAR(id_op_action)) LIKE UPPER('%'||?||'%') OR " +
    			"	     UPPER(cd_card1) LIKE UPPER('%'||?||'%') OR " +
    			"	     UPPER(cd_card2) LIKE UPPER('%'||?||'%') OR " +
    			"	     UPPER(date_op_action_frmt) LIKE UPPER('%'||?||'%') OR " +
    			"	     UPPER(cd_report||'. '||name_report) LIKE UPPER('%'||?||'%') OR " +
    			"	     UPPER(error_op_action) LIKE UPPER('%'||?||'%')) ";
    		for (int i=0; i<6; i++) {
    		    pParam.add(new bcFeautureParam("string", pFind));
    		}
    	}
    	if (!isEmpty(pActionState)) {
    		mySQL = mySQL + " AND cd_op_action_state = ? ";
    		pParam.add(new bcFeautureParam("string", pActionState));
    	}
    	if (!isEmpty(pActionType)) {
    		mySQL = mySQL + " AND cd_op_action_type = ? ";
    		pParam.add(new bcFeautureParam("string", pActionType));
    	}
    	pParam.add(new bcFeautureParam("int", p_end));
    	pParam.add(new bcFeautureParam("int", p_beg));
    	
        mySQL = mySQL + 
    		"                 ORDER BY date_op_action DESC) "+
    		"         WHERE ROWNUM < ? " + 
    		" ) WHERE rn >= ? ";
        
        return getHeaderParamHTML(
        		mySQL,
        		pParam,
                1,
                "D",
            	0,
            	"",
            	"../crm/private_of/crm/clients/nts/actionspecs.jsp?id=:FIELD9:", 
            	contactXML,
            	pPrint);
    } */
    /*
    public String getPrivateOfficeClientsRequestsHeadHTML(String pCdState, String pFind, String p_beg, String p_end, String pPrint) {
        String mySQL = 
        	" SELECT rn, id_nat_prs_office, sname_office_private, login_nat_prs, surname, name, patronymic, " +
			"        name_office_demand_state, creation_date_frmt " +
			"   FROM (SELECT ROWNUM rn, id_nat_prs_office, sname_office_private, login_nat_prs, surname, name, patronymic, " +
			"                name_office_demand_state, creation_date_frmt " +
			"   		FROM (SELECT id_nat_prs_office, sname_office_private, login_nat_prs, surname, name, patronymic, " +
			"                        name_office_demand_state, creation_date_frmt " +
			"                   FROM " + getGeneralDBScheme()+".vc_nat_prs_office_all " +
			"				   WHERE 1=1 ";
        if (!isEmpty(pCdState)) {
        	mySQL = mySQL + 
        		" AND cd_office_demand_state = '" + pCdState + "' ";
        }
        if (!isEmpty(pFind)) {
    		mySQL = mySQL + " AND (UPPER(TO_CHAR(id_nat_prs_office)) LIKE UPPER('%'||?||'%') OR " +
    			"UPPER(login_nat_prs) LIKE UPPER('%'||?||'%') OR " +
    			"UPPER(surname) LIKE UPPER('%'||?||'%') OR " +
    			"UPPER(name) LIKE UPPER('%'||?||'%') OR " +
    			"UPPER(patronymic) LIKE UPPER('%'||?||'%')) ";
    	}
        mySQL = mySQL + "ORDER BY creation_date DESC) a WHERE ROWNUM <" + p_end + " ) WHERE rn>= " + p_beg;
        
        return getHeaderParamHTML(
        		mySQL,
        		pParam,
                0,
                "D",
            	0,
            	"",
            	"../crm/private_of/crm/clients/nts/demandspecs.jsp?id=:FIELD2:", 
            	natprsXML,
            	pPrint);
    } 
	*/
    /*
    public String getPrivateOfficePlacesHeadHTML(String pCdType, String pCdState, String pFind, String p_beg, String p_end, String pPrint) {
    	
    	ArrayList<bcFeautureParam> pParam = initParamArray();

    	String mySQL = 
        	" SELECT rn, name_office_private_type, code_office_private, " +
			"        sname_office_private, placement_office_private, name_office_private_state, id_office_private " +
			"   FROM (SELECT ROWNUM rn, id_office_private, name_office_private_type, code_office_private, " +
			"                sname_office_private, placement_office_private, name_office_private_state " +
			"   		FROM (SELECT id_office_private, " +
			"                        DECODE(cd_office_private_type, " +
			"					         	'CLIENT', '<font color=\"green\"><b>'||name_office_private_type||'</b></font>', " +
			"						        'PARTNER', '<font color=\"blue\"><b>'||name_office_private_type||'</b></font>', " +
			"						        name_office_private_type" +
			"                        ) name_office_private_type, " +
			"                        code_office_private, " +
			"                        sname_office_private, placement_office_private, " +
			"                        DECODE(cd_office_private_state, " +
			"					         	'SETTING', '<font color=\"blue\"><b>'||name_office_private_state||'</b></font>', " +
			"						        'WORK', '<font color=\"green\"><b>'||name_office_private_state||'</b></font>', " +
			"						        'DISCONNECT', '<font color=\"red\"><b>'||name_office_private_state||'</b></font>', " +
			"						        name_office_private_state" +
			"                        ) name_office_private_state " +
			"                   FROM " + getGeneralDBScheme()+".vc_office_private_all " +
			"				   WHERE 1=1 ";
        if (!isEmpty(pCdState)) {
        	mySQL = mySQL + " AND cd_office_private_state = ? ";
        	pParam.add(new bcFeautureParam("string", pCdState));
        }
        if (!isEmpty(pCdType)) {
        	mySQL = mySQL + " AND cd_office_private_type = ? ";
        	pParam.add(new bcFeautureParam("string", pCdType));
        }
        if (!isEmpty(pFind)) {
    		mySQL = mySQL + 
    			" AND (UPPER(TO_CHAR(id_office_private)) LIKE UPPER('%'||?||'%') OR " +
    			"UPPER(code_office_private) LIKE UPPER('%'||?||'%') OR " +
    			"UPPER(sname_office_private) LIKE UPPER('%'||?||'%') OR " +
    			"UPPER(placement_office_private) LIKE UPPER('%'||?||'%')) ";
    		for (int i=0; i<4; i++) {
    		    pParam.add(new bcFeautureParam("string", pFind));
    		}
    	}
        pParam.add(new bcFeautureParam("int", p_end));
        pParam.add(new bcFeautureParam("int", p_beg));
        mySQL = mySQL + "ORDER BY sname_office_private DESC) a WHERE ROWNUM < ?) WHERE rn >= ? ";
        
        return getHeaderParamHTML(
        		mySQL,
        		pParam,
                1,
                "D",
            	0,
            	"",
            	"../crm/private_office/placespecs.jsp?id=:FIELD7:", 
            	object.private_officeXML,
            	pPrint);
    } 
    */
    public String getRemittanceHeadHTML(String pRemittanceType, String pRemittanceState, String pFind, String p_beg, String p_end, String pPrint) {

    	ArrayList<bcFeautureParam> pParam = initParamArray();

    	String mySQL = 
        	" SELECT rn, name_remittance_type, date_remittance_frmt, from_participant,  " +
			"        to_participant, bal_full_frmt, name_remittance_state, id_remittance " +
			"   FROM (SELECT ROWNUM rn, id_remittance, " +
			"  			   	 DECODE(cd_remittance_type, " +
			"                  		'REMITTANCE', '<b><font color=\"black\">'||name_remittance_type||'</font></b>', " +
			"                  		'REMITTANCE_FROM_LOST_CARD', '<b><font color=\"blue\">'||name_remittance_type||'</font></b>', " +
			"                  		'REMITTANCE_FROM_BROKEN_CARD', '<b><font color=\"blue\">'||name_remittance_type||'</font></b>', " +
			"                  		'REMITTANCE_FROM_FACTORY_BROKEN_CARD', '<b><font color=\"blue\">'||name_remittance_type||'</font></b>', " +
			"                  		'CONTRACT_CANCELLATION_GET_CASH_BON', '<b><font color=\"red\">'||name_remittance_type||'</font></b>', " +
			"                  		'CONTRACT_CANCELLATION_GET_NON_CASH_BON', '<b><font color=\"red\">'||name_remittance_type||'</font></b>', " +
			"                  		'WRITE_OFF_BON_CASH', '<b><font color=\"brown\">'||name_remittance_type||'</font></b>', " +
			"                  		'WRITE_OFF_BON_NON_CASH', '<b><font color=\"brown\">'||name_remittance_type||'</font></b>', " +
			"                  		'ADD_BON_CASH', '<b><font color=\"green\">'||name_remittance_type||'</font></b>', " +
			"                  		'ADD_BON_FROM_BANK_ACCOUNT', '<b><font color=\"green\">'||name_remittance_type||'</font></b>', " +
			"                       name_remittance_type" +
			"                ) name_remittance_type, " +
			"				 date_remittance_frmt, " +
			"			 	 from_participant, to_participant, " +
            "                bal_full_frmt, name_remittance_state " +
			"   		FROM (SELECT * " +
			"                   FROM " + getGeneralDBScheme()+".vc_remittance_club_all " +
			"                  WHERE 1=1 ";
        if (!isEmpty(pRemittanceType)) {
    		mySQL = mySQL + " AND cd_remittance_type = ? ";
    		pParam.add(new bcFeautureParam("string", pRemittanceType));
    	}
        if (!isEmpty(pRemittanceState)) {
    		mySQL = mySQL + " AND cd_remittance_state = ? ";
    		pParam.add(new bcFeautureParam("string", pRemittanceState));
    	}
        if (!isEmpty(pFind)) {
    		mySQL = mySQL + 
    			" AND (UPPER(date_remittance) LIKE UPPER('%'||?||'%') OR " +
    			"      UPPER(from_participant) LIKE UPPER('%'||?||'%') OR " +
    			"      UPPER(to_participant) LIKE UPPER('%'||?||'%')) ";
    		for (int i=0; i<3; i++) {
    		    pParam.add(new bcFeautureParam("string", pFind));
    		}
    	}
        pParam.add(new bcFeautureParam("int", p_end));
        pParam.add(new bcFeautureParam("int", p_beg));
        mySQL = mySQL + "ORDER BY date_remittance DESC) WHERE ROWNUM < ?) WHERE rn >= ? ";
        
        return getHeaderParamHTML(
        		mySQL,
        		pParam,
                1,
                "D",
            	0,
            	"",
            	"../crm/finance/remittancespecs.jsp?id=:FIELD8:", 
            	remittanceXML,
            	pPrint);
    } //getRemittanceHeadHTML

    public String getTaxHeadHTML(String pFind, String p_beg, String p_end, String pPrint) {

    	ArrayList<bcFeautureParam> pParam = initParamArray();
    	
    	String mySQL = 
        	" SELECT rn, cd_tax, name_tax, value_tax, name_tax_value_type, name_currency," +
			"        begin_action_date_frmt, end_action_date_frmt " +
			"   FROM (SELECT ROWNUM rn, cd_tax, name_tax, value_tax, name_tax_value_type, name_currency," +
			"                begin_action_date_frmt, end_action_date_frmt " +
			"   		FROM (SELECT cd_tax, name_tax, value_tax, name_tax_value_type, name_currency," +
			"                        begin_action_date_frmt, end_action_date_frmt " +
			"                   FROM " + getGeneralDBScheme()+".vc_tax_current_club_all ";
        if (!isEmpty(pFind)) {
    		mySQL = mySQL + 
    			" WHERE (UPPER(cd_tax) LIKE UPPER('%'||?||'%') OR " +
    			"        UPPER(name_tax) LIKE UPPER('%'||?||'%') OR " +
    			"        UPPER(value_tax) LIKE UPPER('%'||?||'%') OR " +
    			"        UPPER(name_tax_value_type) LIKE UPPER('%'||?||'%') OR " +
    			"        UPPER(name_currency) LIKE UPPER('%'||?||'%') OR " +
    			"        UPPER(begin_action_date_frmt) LIKE UPPER('%'||?||'%') OR " +
    			"        UPPER(end_action_date_frmt) LIKE UPPER('%'||?||'%')) ";
    		for (int i=0; i<7; i++) {
    		    pParam.add(new bcFeautureParam("string", pFind));
    		}
    	}
        pParam.add(new bcFeautureParam("int", p_end));
        pParam.add(new bcFeautureParam("int", p_beg));
        mySQL = mySQL + "ORDER BY name_tax DESC) WHERE ROWNUM < ?) WHERE rn >= ?";
        


        StringBuilder html = new StringBuilder();
        PreparedStatement st = null; 
        
        try{
            
        	LOGGER.debug(prepareSQLToLog(mySQL, pParam));
            con = Connector.getConnection(getSessionId());
            
            st = con.prepareStatement(mySQL);
            st = prepareParam(st, pParam);
            ResultSet rset = st.executeQuery();
            ResultSetMetaData mtd = rset.getMetaData();
            
            int colCount = mtd.getColumnCount();
            if ("Y".equalsIgnoreCase(pPrint)) {
            	html.append(getPrintTable());
            } else {
            	html.append(getHeaderTable());
            }
            html.append("<thead>\n");
            
            html.append("<tr>\n");
            html.append("<th rowspan=\"2\">"+ commonXML.getfieldTransl("RN", false)+"</th>\n");
            html.append("<th rowspan=\"2\">"+ taxXML.getfieldTransl("CD_TAX", false)+"</th>\n");
            html.append("<th rowspan=\"2\">"+ taxXML.getfieldTransl("NAME_TAX", false)+"</th>\n");
            html.append("<th colspan=\"5\">"+ taxXML.getfieldTransl("H_CURRENT_VALUE", false)+"</th>\n");
            
            if (!isEmpty(deleteHyperLink)) {
            	html.append("<th rowspan=\"2\">&nbsp;</th>\n");
            }
            
            html.append("</tr>\n");
            html.append("<tr>");
            html.append("<th>"+ taxXML.getfieldTransl("VALUE_TAX", false)+"</th>\n");
            html.append("<th>"+ taxXML.getfieldTransl("NAME_TAX_VALUE_TYPE", false)+"</th>\n");
            html.append("<th>"+ taxXML.getfieldTransl("NAME_CURRENCY", false)+"</th>\n");
            html.append("<th>"+ taxXML.getfieldTransl("BEGIN_ACTION_DATE_FRMT", false)+"</th>\n");
            html.append("<th>"+ taxXML.getfieldTransl("END_ACTION_DATE_FRMT", false)+"</th>\n");
            html.append("</tr>");
            html.append("</thead><tbody>\n");
            
            while (rset.next())
            {
            	String deleteId = "";
            	String deleteName = "";
            	
            	for (int i=1; i <= colCount; i++) {
            		if (deleteEntryId.equalsIgnoreCase(mtd.getColumnName(i))) {
            			deleteId = getHTMLValue(rset.getString(i));
            		}
            		if (deleteEntryName.equalsIgnoreCase(mtd.getColumnName(i))) {
            			deleteName = getHTMLValue(rset.getString(i));
            		}
            	}
            	
                html.append("<tr id=\"elem_"+rset.getString("CD_TAX")+"\" onmouseout=\"coloredOut(this)\" onmouseover=\"coloredOver(this,1)\">");
                
                String myHyperLink = "../crm/finance/taxspecs.jsp?id=" + rset.getString("CD_TAX");
                html.append("<td align=\"center\">"+getHyperLinkFirst()+myHyperLink+getHyperLinkMiddle()+ "<font color=\"black\">" + getValue2(rset.getString("RN")) + "</font>" + getHyperLinkEnd() + "</td>\n");
                for (int i=2; i <= colCount; i++) {
                	html.append("<td align=\"center\">"+getHyperLinkFirst()+myHyperLink+getHyperLinkMiddle()+ "<font color=\"black\">" + getValue2(rset.getString(i)) + "</font>" + getHyperLinkEnd() + "</td>\n");
                }

            	if (!isEmpty(deleteHyperLink)) {
                	html.append("<td align=\"center\"><div class=\"div_button\" onclick=\"var msg='" + deleteConfirmMessage + " \\\'" + deleteName + "\\\'?';var res=window.confirm(msg);if (res) {ajaxpage('"+ deleteHyperLink + deleteId + "','div_main');}\">" + "<img vspace=\"0\" hspace=\"0\"" +
            				" src=\"../images/oper/rows/delete.png\" width=\"16\" height=\"16\" align=\"top\" style=\"border: 0px;\"" + 
            				" title=\"" + buttonXML.getfieldTransl("button_delete", false)+"\">" + getHyperLinkEnd()+"</td>\n");
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
    } //getRemittanceHeadHTML

    public String getPurchasesHeadHTML(String pFind, String p_beg, String p_end, String pPrint) {

    	ArrayList<bcFeautureParam> pParam = initParamArray();

        String mySQL = 
        	"  SELECT rn, id_trans, card_serial_number, nt_icc, nt_ext, sys_date, opr_sum_frmt, " +
    		" 		  sum_pay_cash_frmt, sum_pay_card_frmt, sum_pay_bon_frmt, " +
    		"         club_sum_frmt, sum_bon_frmt, sum_disc_frmt, state_trans_tsl, is_storned "+
    		"    FROM (SELECT ROWNUM rn, id_trans, card_serial_number, nt_icc, nt_ext, sys_date_time_frmt sys_date, " +
    		"                 cd_currency, opr_sum_frmt, " +
            "			      sum_pay_cash_frmt, sum_pay_card_frmt, sum_pay_bon_frmt, club_sum_frmt, " +
            "                 sum_bon_frmt, sum_disc_frmt, state_trans_tsl, is_storned "+
            "            FROM (SELECT * " +
            "                    FROM " + getGeneralDBScheme()+".vc_purchases_club_all ";
            
        if (!isEmpty(pFind)) {
    		mySQL = mySQL + 
    			" WHERE (UPPER(TO_CHAR(id_trans)) LIKE UPPER('%'||?||'%') OR " +
    			"UPPER(card_serial_number) LIKE UPPER('%'||?||'%')) ";
    		for (int i=0; i<2; i++) {
    		    pParam.add(new bcFeautureParam("string", pFind));
    		}
    	}
        pParam.add(new bcFeautureParam("int", p_end));
        pParam.add(new bcFeautureParam("int", p_beg));
        mySQL = mySQL + ") WHERE ROWNUM < ?) WHERE rn >= ?";
            
        return getHeaderParamHTML(
            		mySQL,
            		pParam,
                    1,
                    "N",
                	15,
                	"1",
                	"../crm/cards/stornospecs.jsp?id=:FIELD2:", 
                	transactionXML,
                	pPrint);
    } // getPurchasesHeadHTML

    public String getLoyHeadHTML(String pCdKindLoyality, String pFind, String p_beg, String p_end, String pPrint) {

    	ArrayList<bcFeautureParam> pParam = initParamArray();

    	String mySQL =
        	" SELECT rn, name_partner, name_loyality_scheme title_loyality_scheme, " +
            "		 /*desc_loyality_scheme,*/ cd_kind_loyality||' - '||name_kind_loyality name_kind_loyality, " +
            "		 date_beg_frmt, date_end_frmt, id_loyality_scheme " +
            "   FROM (SELECT ROWNUM rn, id_loyality_scheme, name_loyality_scheme, " +
            "				 desc_loyality_scheme, cd_kind_loyality, name_kind_loyality, sname_jur_prs name_partner, " +
            "				 date_beg_frmt, date_end_frmt " +
            "           FROM (SELECT * " +
            "                   FROM " + getGeneralDBScheme()+".vc_loyality_scheme_club_all " +
            "                  WHERE 1=1 ";
        if (!isEmpty(pCdKindLoyality)) {
        	mySQL = mySQL + " AND cd_kind_loyality = ? ";
        	pParam.add(new bcFeautureParam("string", pCdKindLoyality));
        }
        if (!isEmpty(pFind)) {
    		mySQL = mySQL + 
    			" AND (UPPER(sname_jur_prs) LIKE UPPER('%'||?||'%') OR " +
    			"      UPPER(name_loyality_scheme) LIKE UPPER('%'||?||'%') OR " +
    			"      UPPER(desc_loyality_scheme) LIKE UPPER('%'||?||'%') OR " +
    			"      UPPER(date_beg_frmt) LIKE UPPER('%'||?||'%') OR " + 
    			"      UPPER(date_end_frmt) LIKE UPPER('%'||?||'%')) ";
    		for (int i=0; i<5; i++) {
    		    pParam.add(new bcFeautureParam("string", pFind));
    		}
    	}
    	pParam.add(new bcFeautureParam("int", p_end));
    	pParam.add(new bcFeautureParam("int", p_beg));
        mySQL = mySQL + " ORDER BY name_loyality_scheme) WHERE ROWNUM < ?) WHERE rn >= ?";
            
        return getHeaderParamHTML(
            		mySQL,
            		pParam,
                    1,
                    "D",
                	0,
                	"",
                	"../crm/clients/loyspecs.jsp?id=:FIELD7:", 
                	loyXML,
                	pPrint);
    } //getBankStatementHeadHTML

    
    public String getBankStatementHeadHTML(String pImportState, String pFind, String p_beg, String p_end, String pPrint) {

    	ArrayList<bcFeautureParam> pParam = initParamArray();

    	String mySQL = 
        	" SELECT rn, id_bank_statement, number_bank_statement, date_bank_statement_frmt, " +
            "        mfo_bank, sname_bank, number_bank_account, sname_owner_bank_account, " +
            "        name_currency, debet_total_frmt, credit_total_frmt, " +
            "        import_state_bs_header_tsl, date_bank_statement, import_state_bs_header " +
            "   FROM (SELECT ROWNUM rn, id_bank_statement, number_bank_statement, date_bank_statement_frmt, " +
            "                sname_owner_bank_account, number_bank_account, " +
            "                sname_bank, mfo_bank, name_currency, debet_total_frmt, credit_total_frmt, " +
            "                import_state_bs_header_tsl, date_bank_statement, import_state_bs_header " +
            "           FROM (SELECT id_bank_statement, number_bank_statement, date_bank_statement_frmt, " +
        	"                        sname_owner_bank_account, number_bank_account, " +
        	"                        sname_bank, mfo_bank, name_currency, debet_total_frmt, credit_total_frmt, " +
        	"  			   			 DECODE(import_state_bs_header, " +
			"                       		'IMPORTED', '<b><font color=\"green\">'||import_state_bs_header_tsl||'</font></b>', " +
			"                       		'ERROR', '<b><font color=\"red\">'||import_state_bs_header_tsl||'</font></b>', " +
			"                       		'NOT_IMPORTED', '<b>'||import_state_bs_header_tsl||'</b>', " +
			"                       		'<b><font color=\"gray\">'||import_state_bs_header_tsl||'</font></b>'" +
			"                              ) import_state_bs_header_tsl, " +
	        "                        date_bank_statement, " +
        	"                        import_state_bs_header " +
        	"                   FROM " + getGeneralDBScheme()+".vc_bs_headers_club_all " +
        	"                  WHERE 1=1 ";
        if (!isEmpty(pImportState)) {
    		mySQL = mySQL + " AND import_state_bs_header = ? ";
    		pParam.add(new bcFeautureParam("string", pImportState));
    	}
    	if (!isEmpty(pFind)) {
    		mySQL = mySQL + 
    			"   AND (TO_CHAR(id_bank_statement) LIKE UPPER('%'||?||'%') OR " +
    			"		 UPPER(number_bank_statement) LIKE UPPER('%'||?||'%') OR " +
    			"		 UPPER(date_bank_statement_frmt) LIKE UPPER('%'||?||'%') OR " +
				"		 UPPER(sname_owner_bank_account) LIKE UPPER('%'||?||'%') OR " +
    			"		 UPPER(number_bank_account) LIKE UPPER('%'||?||'%') OR " + 
    			"		 UPPER(sname_bank) LIKE UPPER('%'||?||'%') OR " + 
    			"		 UPPER(mfo_bank) LIKE UPPER('%'||?||'%') OR " + 
    			"		 UPPER(debet_total_frmt) LIKE UPPER('%'||?||'%') OR " + 
    			"		 UPPER(credit_total_frmt) LIKE UPPER('%'||?||'%')) ";
    		for (int i=0; i<9; i++) {
    		    pParam.add(new bcFeautureParam("string", pFind));
    		}
    	}
    	pParam.add(new bcFeautureParam("int", p_end));
    	pParam.add(new bcFeautureParam("int", p_beg));
    	mySQL = mySQL +
            "                  ORDER BY date_bank_statement DESC, id_bank_statement)" +
            "          WHERE ROWNUM < ? " + 
            "  ) WHERE rn >= ? ";
        
        StringBuilder html = new StringBuilder();
        PreparedStatement st = null;
        
    	try {

            
        	LOGGER.debug(prepareSQLToLog(mySQL, pParam));
            con = Connector.getConnection(getSessionId());
            
            st = con.prepareStatement(mySQL);
            st = prepareParam(st, pParam);
            ResultSet rset = st.executeQuery();
            ResultSetMetaData mtd = rset.getMetaData();
            
            int colCount = mtd.getColumnCount();
            if ("Y".equalsIgnoreCase(pPrint)) {
            	html.append(getPrintTable());
            } else {
            	html.append(getHeaderTable());
            }
            html.append("<thead>\n");
            
            html.append("<tr>\n");
            html.append("<th rowspan=\"2\">"+ commonXML.getfieldTransl("RN", false)+"</th>\n");
            html.append("<th rowspan=\"2\">"+ bank_statementXML.getfieldTransl("ID_BANK_STATEMENT", false)+"</th>\n");
            html.append("<th rowspan=\"2\">"+ bank_statementXML.getfieldTransl("NUMBER_BANK_STATEMENT", false)+"</th>\n");
            html.append("<th rowspan=\"2\">"+ bank_statementXML.getfieldTransl("DATE_BANK_STATEMENT_FRMT", false)+"</th>\n");
            html.append("<th colspan=\"4\">"+ bank_statementXML.getfieldTransl("H_CLIENT", false)+"</th>\n");
            html.append("<th rowspan=\"2\">"+ bank_statementXML.getfieldTransl("NAME_CURRENCY_CLIENT", false)+"</th>\n");
            html.append("<th rowspan=\"2\">"+ bank_statementXML.getfieldTransl("DEBET_TOTAL_FRMT", false)+"</th>\n");
            html.append("<th rowspan=\"2\">"+ bank_statementXML.getfieldTransl("CREDIT_TOTAL_FRMT", false)+"</th>\n");
            html.append("<th rowspan=\"2\">"+ bank_statementXML.getfieldTransl("IMPORT_STATE_BS_HEADER", false)+"</th>\n");
            html.append("</tr>\n");
            html.append("<tr>");
            html.append("<th>"+ bank_statementXML.getfieldTransl("MFO_BANK", false)+"</th>\n");
            html.append("<th>"+ bank_statementXML.getfieldTransl("NAME_BANK", false)+"</th>\n");
            html.append("<th>"+ bank_statementXML.getfieldTransl("BANK_ACCOUNT", false)+"</th>\n");
            html.append("<th>"+ bank_statementXML.getfieldTransl("NAME_JUR_PRS", false)+"</th>\n");
            html.append("</tr>");
            html.append("</thead><tbody>\n");
            
            while (rset.next())
            {
                html.append("<tr>");
                String myHyperLink = "";
                myHyperLink = getHyperLinkFirst()+"../crm/finance/bankstatementspecs.jsp?id="+getValue2(rset.getString("ID_BANK_STATEMENT"))+getHyperLinkMiddle();
                for (int i=1; i <= colCount-2; i++) {
                	html.append(formatTDData(mtd.getColumnName(i), mtd.getColumnType(i), "")+ myHyperLink+ "<font color=\"black\">" + getValue2(rset.getString(i)) + "</font>" + getHyperLinkEnd() + "</td>\n");
                }
                html.append("</tr>\n");
            }
            html.append("</tbody></table></form>\n");
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
	} //getBankStatementHeadHTML

    
    public String getBankStatementLinesHTML(String pImportState, String pReconcileState, String pFind, String p_beg, String p_end, String pPrint) {

    	ArrayList<bcFeautureParam> pParam = initParamArray();

    	String mySQL =
        	  " SELECT rn, date_bank_statement_frmt, line_number,  " +
        	  "        mfo_bank_client, name_bank_branch_client, number_bank_account_client, name_jur_prs_client," +
        	  "        mfo_bank_correspondent, name_bank_branch_correspondent, num_bank_account_correspondent, name_jur_prs_correspondent, " +
	          "        debet_amount, credit_amount, " +
	          "        payment_assignment, import_state_bs_line_tsl, reconcile_state_tsl, id_bank_statement_line " +
	          "   FROM (SELECT ROWNUM rn, id_bank_statement_line, line_number, " +
	          "                id_bank_statement, number_bank_statement, date_bank_statement_frmt, " +
              "                client_sname_owner_bank_accnt name_jur_prs_client, client_number_bank_account number_bank_account_client, " +
              "                client_sname_bank name_bank_branch_client, client_mfo_bank mfo_bank_client, " +
              "                corr_sname_owner_bank_account name_jur_prs_correspondent, " +
	          "        		   corr_number_bank_account num_bank_account_correspondent, " +
	          "        		   corr_sname_bank name_bank_branch_correspondent, corr_mfo_bank mfo_bank_correspondent, " +
	          "        		   debet_amount_frmt debet_amount, credit_amount_frmt credit_amount, " +
	          "        		   assignment payment_assignment, " +
	          "  			   DECODE(reconcile_state, " +
			  "                       'RECONCILE', '<b><font color=\"green\">'||reconcile_state_tsl||'</font></b>', " +
			  "                       'UNRECONCILE', '<b><font color=\"red\">'||reconcile_state_tsl||'</font></b>', " +
			  "                       reconcile_state_tsl) reconcile_state_tsl, " +
	          "  			   DECODE(import_state_bs_line, " +
			  "                		  'IMPORTED', '<b><font color=\"green\">'||import_state_bs_line_tsl||'</font></b>', " +
			  "                       'ERROR', '<b><font color=\"red\">'||import_state_bs_line_tsl||'</font></b>', " +
			  "                       'NOT_IMPORTED', '<b>'||import_state_bs_line_tsl||'</b>', " +
			  "                       '<b><font color=\"gray\">'||import_state_bs_line_tsl||'</font></b>'" +
			  "                      ) import_state_bs_line_tsl " +
	          "   		  FROM (SELECT * " +
	          "                   FROM " + getGeneralDBScheme() + ".vc_bs_lines_club_all " +
	          "                  WHERE 1=1 ";
        if (!isEmpty(pImportState)) {
    		mySQL = mySQL + " AND import_state_bs_line = ? ";
    		pParam.add(new bcFeautureParam("string", pImportState));
    	}
        if (!isEmpty(pReconcileState)) {
    		mySQL = mySQL + " AND reconcile_state = ? ";
    		pParam.add(new bcFeautureParam("string", pReconcileState));
    	}
    	if (!isEmpty(pFind)) {
    		mySQL = mySQL + " AND (UPPER(line_number) LIKE UPPER('%'||?||'%') OR " +
    			"UPPER(number_bank_statement) LIKE UPPER('%'||?||'%') OR " +
    			"UPPER(date_bank_statement_frmt) LIKE UPPER('%'||?||'%') OR " +
    			"UPPER(client_sname_owner_bank_accnt) LIKE UPPER('%'||?||'%') OR " + 
    			"UPPER(client_number_bank_account) LIKE UPPER('%'||?||'%') OR " + 
    			"UPPER(client_sname_bank) LIKE UPPER('%'||?||'%') OR " + 
    			"UPPER(client_mfo_bank) LIKE UPPER('%'||?||'%') OR " + 
    			"UPPER(corr_sname_owner_bank_account) LIKE UPPER('%'||?||'%') OR " + 
    			"UPPER(corr_number_bank_account) LIKE UPPER('%'||?||'%') OR " + 
    			"UPPER(corr_sname_bank) LIKE UPPER('%'||?||'%') OR " + 
    			"UPPER(corr_mfo_bank) LIKE UPPER('%'||?||'%') OR " + 
    			"UPPER(debet_amount_frmt) LIKE UPPER('%'||?||'%') OR " + 
    			"UPPER(credit_amount_frmt) LIKE UPPER('%'||?||'%') OR " + 
    			"UPPER(assignment) LIKE UPPER('%'||?||'%')) ";
    		for (int i=0; i<14; i++) {
    		    pParam.add(new bcFeautureParam("string", pFind));
    		}
    	}
    	pParam.add(new bcFeautureParam("int", p_end));
    	pParam.add(new bcFeautureParam("int", p_beg));
    	mySQL = mySQL +
            "                  )" +
            "          WHERE ROWNUM < ? " + 
            "  ) WHERE rn >= ?";
        
        StringBuilder html = new StringBuilder();
        PreparedStatement st = null;
        
    	try {

            
        	LOGGER.debug(prepareSQLToLog(mySQL, pParam));
            con = Connector.getConnection(getSessionId());
            
            st = con.prepareStatement(mySQL);
            st = prepareParam(st, pParam);
            ResultSet rset = st.executeQuery();
            ResultSetMetaData mtd = rset.getMetaData();
            
            int colCount = mtd.getColumnCount();
            if ("Y".equalsIgnoreCase(pPrint)) {
            	html.append(getPrintTable());
            } else {
            	html.append(getHeaderTable());
            }
            html.append("<thead>\n");
            
            html.append("<tr>\n");
            html.append("<th rowspan=\"2\">"+ commonXML.getfieldTransl("RN", false)+"</th>\n");
            html.append("<th rowspan=\"2\">"+ bank_statementXML.getfieldTransl("DATE_BANK_STATEMENT_FRMT", false)+"</th>\n");
            html.append("<th rowspan=\"2\">"+ bank_statementXML.getfieldTransl("LINE_NUMBER", false)+"</th>\n");
            html.append("<th colspan=\"4\">"+ bank_statementXML.getfieldTransl("H_CLIENT", false)+"</th>\n");
            html.append("<th colspan=\"4\">"+ bank_statementXML.getfieldTransl("H_CORRESPONDENT", false)+"</th>\n");
            html.append("<th rowspan=\"2\">"+ bank_statementXML.getfieldTransl("DEBET_AMOUNT", false)+"</th>\n");
            html.append("<th rowspan=\"2\">"+ bank_statementXML.getfieldTransl("CREDIT_AMOUNT", false)+"</th>\n");
            html.append("<th rowspan=\"2\">"+ bank_statementXML.getfieldTransl("PAYMENT_ASSIGNMENT", false)+"</th>\n");
            html.append("<th rowspan=\"2\">"+ bank_statementXML.getfieldTransl("H_IMPORT_STATE", false)+"</th>\n");
            html.append("<th rowspan=\"2\">"+ bank_statementXML.getfieldTransl("RECONCILE_STATE", false)+"</th>\n");
            html.append("</tr>\n");
            html.append("<tr>");
            html.append("<th>"+ bank_statementXML.getfieldTransl("MFO_BANK", false)+"</th>\n");
            html.append("<th>"+ bank_statementXML.getfieldTransl("NAME_BANK", false)+"</th>\n");
            html.append("<th>"+ bank_statementXML.getfieldTransl("BANK_ACCOUNT", false)+"</th>\n");
            html.append("<th>"+ bank_statementXML.getfieldTransl("NAME_JUR_PRS", false)+"</th>\n");
            html.append("<th>"+ bank_statementXML.getfieldTransl("MFO_BANK", false)+"</th>\n");
            html.append("<th>"+ bank_statementXML.getfieldTransl("NAME_BANK", false)+"</th>\n");
            html.append("<th>"+ bank_statementXML.getfieldTransl("BANK_ACCOUNT", false)+"</th>\n");
            html.append("<th>"+ bank_statementXML.getfieldTransl("NAME_JUR_PRS", false)+"</th>\n");
            html.append("</tr>");
            html.append("</thead><tbody>\n");
            
            while (rset.next())
            {
                html.append("<tr>");
                for (int i=1; i <= colCount-1; i++) {
                	String myHyperLink = getHyperLinkFirst()+"../crm/finance/bankstatement_linespecs.jsp?id="+getValue2(rset.getString("ID_BANK_STATEMENT_LINE"))+getHyperLinkMiddle();
                	html.append(formatTDData(mtd.getColumnName(i), mtd.getColumnType(i), "")+ myHyperLink+ "<font color=\"black\">" + getValue2(rset.getString(i)) + "</font>" + getHyperLinkEnd() + "</td>\n");
                }
                html.append("</tr>\n");
            }
            html.append("</tbody></table></form>\n");
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
	} //getBankStatementHeadHTML
    
    public String getBankStatementIntHeadHTML(String pFind, String p_beg, String p_end, String pPrint) {

    	ArrayList<bcFeautureParam> pParam = initParamArray();

    	String mySQL =
        	" SELECT rn, id_bank_statement, number_bank_statement, date_bank_statement_frmt, mfo_bank_client, " +
            "        name_bank_branch_client, number_bank_account_client, name_jur_prs_client, " +
            "        name_currency_client, debet_total_frmt, credit_total_frmt, record_status_flag_tsl, record_status_flag " +
            "   FROM (SELECT ROWNUM rn, id_bank_statement, bank_statement_number number_bank_statement, bank_statement_date_frmt date_bank_statement_frmt, " +
            "                client_name name_jur_prs_client, " +
            "                client_bank_account_number number_bank_account_client, " +
            "                client_bank_name name_bank_branch_client, client_bank_mfo mfo_bank_client," +
            "                currency_code name_currency_client, " +
            "                debet_total_frmt, credit_total_frmt, record_status_flag_tsl, record_status_flag " +
            "           FROM (SELECT * " +
            "                   FROM " + getGeneralDBScheme()+".vc_bs_headers_int_club_all";

    	if (!isEmpty(pFind)) {
    		mySQL = mySQL + " WHERE (TO_CHAR(id_bank_statement) LIKE UPPER('%'||?||'%') OR " +
    			"UPPER(bank_statement_number) LIKE UPPER('%'||?||'%') OR " +
    			"UPPER(bank_statement_date_frmt) LIKE UPPER('%'||?||'%') OR " +
    			"UPPER(client_name) LIKE UPPER('%'||?||'%') OR " +
    			"UPPER(client_bank_account_number) LIKE UPPER('%'||?||'%') OR " + 
    			"UPPER(client_bank_name) LIKE UPPER('%'||?||'%') OR " + 
    			"UPPER(client_bank_mfo) LIKE UPPER('%'||?||'%') OR " + 
    			"UPPER(debet_total_frmt) LIKE UPPER('%'||?||'%') OR " + 
    			"UPPER(credit_total_frmt) LIKE UPPER('%'||?||'%')) ";
    		for (int i=0; i<9; i++) {
    		    pParam.add(new bcFeautureParam("string", pFind));
    		}
    	}
    	pParam.add(new bcFeautureParam("int", p_end));
    	pParam.add(new bcFeautureParam("int", p_beg));
    	mySQL = mySQL +
            "                  ORDER BY bank_statement_date DESC, id_bank_statement) " +
            "          WHERE ROWNUM < ? " + 
            "  ) WHERE rn >= ?";
            
        return getHeaderParamHTML(
            		mySQL,
            		pParam,
                    1,
                    "N",
                	13,
                	"E",
                	"../crm/finance/bsimportspecs.jsp?id=:FIELD2:", 
                	bank_statementXML,
                	pPrint);
    } //getBankStatementIntHeadHTML

    public String getKvitovkaHeadHTML(String pIdBankStatementLine, String pLineState, String pFind, String p_beg, String p_end, String pPrint) {

    	ArrayList<bcFeautureParam> pParam = initParamArray();

    	String mySQL =
        	" SELECT rn, reconcile_state_tsl, name_currency, debet_amount_frmt, credit_amount_frmt, " +
        	"        reconciled_amount_frmt, number_bank_statement||'.'||line_number number_bank_statement, date_bank_statement_frmt, " + 
        	"        client_mfo_bank, client_sname_bank, client_number_bank_account, client_name_owner_bank_account, " +
        	"        corr_mfo_bank, corr_sname_bank, corr_number_bank_account, corr_name_owner_bank_account,  " +
        	"        assignment, doc_number, doc_date_frmt, reconcile_state, id_bank_statement_line " +
            "  FROM (SELECT ROWNUM rn, number_bank_statement, line_number, date_bank_statement_frmt, " + 
        	"               client_mfo_bank, client_sname_bank, client_number_bank_account, client_name_owner_bank_account, " +
        	"               corr_mfo_bank, corr_sname_bank, corr_number_bank_account, corr_name_owner_bank_account,  " +
        	"               doc_number, doc_date_frmt, name_currency, debet_amount_frmt, credit_amount_frmt,  " +
        	"               reconciled_amount_frmt, assignment, " +
        	"               DECODE(reconcile_state, " +
        	"                      'UNRECONCILE', '<font color=\"red\"><b>'||reconcile_state_tsl||'</b></font>', " +
        	"                      'RECONCILE_PART', '<font color=\"green\">'||reconcile_state_tsl||'</font>', " +
        	"                      reconcile_state_tsl" +
        	"                     ) reconcile_state_tsl, " +
        	"               reconcile_state, id_bank_statement_line " +
            " 		   FROM (SELECT *" +
            "                  FROM " + getGeneralDBScheme()+".vc_bs_lines_club_all " ;
        if (!isEmpty(pIdBankStatementLine)) {
        	mySQL = mySQL + " WHERE id_bank_statement_line = ? ";
        	pParam.add(new bcFeautureParam("int", pIdBankStatementLine));
        } else {
        	mySQL = mySQL + " WHERE 1=1 ";
        	if (!isEmpty(pLineState)) {
	        	mySQL = mySQL + " AND reconcile_state = ? ";
	        	pParam.add(new bcFeautureParam("string", pLineState));
	        } 
	    	if (!isEmpty(pFind)) {
	    		mySQL = mySQL + " AND (UPPER(doc_number) LIKE UPPER('%'||?||'%') OR " +
	    			"UPPER(doc_date_frmt) LIKE UPPER('%'||?||'%') OR " + 
	    			"UPPER(number_bank_statement) LIKE UPPER('%'||?||'%') OR " + 
	    			"UPPER(date_bank_statement_frmt) LIKE UPPER('%'||?||'%') OR " + 
	    			"UPPER(client_mfo_bank) LIKE UPPER('%'||?||'%') OR " + 
	    			"UPPER(client_sname_bank) LIKE UPPER('%'||?||'%') OR " + 
	    			"UPPER(client_number_bank_account) LIKE UPPER('%'||?||'%') OR " + 
	    			"UPPER(client_name_owner_bank_account) LIKE UPPER('%'||?||'%') OR " + 
	    			"UPPER(corr_mfo_bank) LIKE UPPER('%'||?||'%') OR " + 
	    			"UPPER(corr_sname_bank) LIKE UPPER('%'||?||'%') OR " + 
	    			"UPPER(corr_number_bank_account) LIKE UPPER('%'||?||'%') OR " + 
	    			"UPPER(corr_name_owner_bank_account) LIKE UPPER('%'||?||'%') OR " + 
	    			"UPPER(debet_amount_frmt) LIKE UPPER('%'||?||'%') OR " + 
	    			"UPPER(credit_amount_frmt) LIKE UPPER('%'||?||'%') OR " + 
	    			"UPPER(reconciled_amount_frmt) LIKE UPPER('%'||?||'%') OR " + 
	    			"UPPER(assignment) LIKE UPPER('%'||?||'%')) ";
	    		for (int i=0; i<16; i++) {
	    		    pParam.add(new bcFeautureParam("string", pFind));
	    		}
	    	}
        }
        pParam.add(new bcFeautureParam("int", p_end));
        pParam.add(new bcFeautureParam("int", p_beg));
        mySQL = mySQL +
        	" 		  )  " +
            "        WHERE ROWNUM < ? " + 
            " ) WHERE rn >= ?";

        StringBuilder html = new StringBuilder();
        PreparedStatement st = null; 
        
        try{
            
        	LOGGER.debug(prepareSQLToLog(mySQL, pParam));
            con = Connector.getConnection(getSessionId());
            
            st = con.prepareStatement(mySQL);
            st = prepareParam(st, pParam);
            ResultSet rset = st.executeQuery();
            ResultSetMetaData mtd = rset.getMetaData();
            
            int colCount = mtd.getColumnCount();
            if ("Y".equalsIgnoreCase(pPrint)) {
            	html.append(getPrintTable());
            } else {
            	html.append(getHeaderTable());
            }
            html.append("<thead>\n");
            
            html.append("<tr>\n");
            html.append("<th rowspan=\"2\">"+ commonXML.getfieldTransl("RN", false)+"</th>\n");
            html.append("<th rowspan=\"2\">&nbsp;</th>\n");
            html.append("<th rowspan=\"2\">"+ bank_statementXML.getfieldTransl("RECONCILE_STATE", false)+"</th>\n");
            html.append("<th rowspan=\"2\">"+ bank_statementXML.getfieldTransl("NAME_CURRENCY_CLIENT", false)+"</th>\n");
            html.append("<th rowspan=\"2\">"+ bank_statementXML.getfieldTransl("DEBET_AMOUNT_FRMT", false)+"</th>\n");
            html.append("<th rowspan=\"2\">"+ bank_statementXML.getfieldTransl("CREDIT_AMOUNT_FRMT", false)+"</th>\n");
            html.append("<th rowspan=\"2\">"+ bank_statementXML.getfieldTransl("RECONCILED_AMOUNT_FRMT", false)+"</th>\n");
            html.append("<th rowspan=\"2\">"+ bank_statementXML.getfieldTransl("NUMBER_BANK_STATEMENT", false)+"</th>\n");
            html.append("<th rowspan=\"2\">"+ bank_statementXML.getfieldTransl("DATE_BANK_STATEMENT_FRMT", false)+"</th>\n");
            html.append("<th colspan=\"4\">"+ bank_statementXML.getfieldTransl("H_CLIENT", false)+"</th>\n");
            html.append("<th colspan=\"4\">"+ bank_statementXML.getfieldTransl("H_CORRESPONDENT", false)+"</th>\n");
            html.append("<th rowspan=\"2\">"+ bank_statementXML.getfieldTransl("PAYMENT_ASSIGNMENT", false)+"</th>\n");
            html.append("<th rowspan=\"2\">"+ bank_statementXML.getfieldTransl("NUMBER_DOCUMENT", false)+"</th>\n");
            html.append("<th rowspan=\"2\">"+ bank_statementXML.getfieldTransl("DATE_DOCUMENT", false)+"</th>\n");
            html.append("</tr>\n");
            html.append("<tr>");
            html.append("<th>"+ bank_statementXML.getfieldTransl("MFO_BANK", false)+"</th>\n");
            html.append("<th>"+ bank_statementXML.getfieldTransl("NAME_BANK_BRANCH", false)+"</th>\n");
            html.append("<th>"+ bank_statementXML.getfieldTransl("NUMBER_BANK_ACCOUNT", false)+"</th>\n");
            html.append("<th>"+ bank_statementXML.getfieldTransl("NAME_JUR_PRS", false)+"</th>\n");
            html.append("<th>"+ bank_statementXML.getfieldTransl("MFO_BANK", false)+"</th>\n");
            html.append("<th>"+ bank_statementXML.getfieldTransl("NAME_BANK_BRANCH", false)+"</th>\n");
            html.append("<th>"+ bank_statementXML.getfieldTransl("NUMBER_BANK_ACCOUNT", false)+"</th>\n");
            html.append("<th>"+ bank_statementXML.getfieldTransl("NAME_JUR_PRS", false)+"</th>\n");
            html.append("</tr>");
            html.append("</thead><tbody>\n");
            
            while (rset.next())
            {
                html.append("<tr id=\"elem_"+rset.getString("ID_BANK_STATEMENT_LINE")+"\" onmouseout=\"coloredOut(this)\" onmouseover=\"coloredOver(this,1)\">");
                String myHyperLink = "";
                if (isEmpty(pIdBankStatementLine)) {
                	myHyperLink = "../crm/finance/kvitovka.jsp?id_bank_statement_line="+rset.getString("ID_BANK_STATEMENT_LINE");
                } else {
                	myHyperLink = "../crm/finance/kvitovka.jsp";
                }
                html.append("<td align=\"center\">"+getHyperLinkFirst()+myHyperLink+getHyperLinkMiddle()+ "<font color=\"black\">" + getValue2(rset.getString("RN")) + "</font>" + getHyperLinkEnd() + "</td>\n");
                if (!"UNRECONCILE".equalsIgnoreCase(rset.getString("RECONCILE_STATE"))) {
	            	String myDeleteLink = "../crm/finance/kvitovkaupdate.jsp?id_bank_statement_line="+rset.getString("ID_BANK_STATEMENT_LINE");
	            	html.append(getDeleteButtonHTML(myDeleteLink, "unreconcile", "yes", "oper/rows/delete.png", clearingXML.getfieldTransl("h_cancel_reconcile", false)));
                } else {
                    html.append("<td>&nbsp;</td>\n");
                }
                for (int i=2; i <= colCount-2; i++) {
                	if ("RECONCILE_STATE_TSL".equalsIgnoreCase(mtd.getColumnName(i))) {
                		if (!"UNRECONCILE".equalsIgnoreCase(rset.getString("RECONCILE_STATE"))) {
                			html.append("<td align=\"center\">"+getHyperLinkFirst()+myHyperLink+getHyperLinkMiddle()+ "<font color=\"red\">" + getValue2(rset.getString(i)) + "</font>" + getHyperLinkEnd() + "</td>\n");
                		} else {
                			html.append("<td align=\"center\">"+getHyperLinkFirst()+myHyperLink+getHyperLinkMiddle()+ "<font color=\"black\">" + getValue2(rset.getString(i)) + "</font>" + getHyperLinkEnd() + "</td>\n");
                		}
                	} else {
                		html.append("<td align=\"center\">"+getHyperLinkFirst()+myHyperLink+getHyperLinkMiddle()+ "<font color=\"black\">" + getValue2(rset.getString(i)) + "</font>" + getHyperLinkEnd() + "</td>\n");
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
    } //getKvitovkaHeadHTML

    public String getKvitovkaBankStatementHTML(String pIdBankStatementLine, String pLineState, String pFind, String p_beg, String p_end, String pPrint) {
    	boolean needFind = false;
    	
    	if (!(isEmpty(pLineState) && isEmpty(pFind))) {
    		needFind = true;
    	}
    	
    	bcBankStatementLineObject line = null;
    	
    	if (!isEmpty(pIdBankStatementLine)) {
    		if (needFind==false) {
    			line = new bcBankStatementLineObject(pIdBankStatementLine);
    		}
        }

    	ArrayList<bcFeautureParam> pParam = initParamArray();

    	String mySQL =
        	" SELECT rn, reconcile_state_tsl, name_currency, entered_amount_frmt, reconciled_amount_frmt," +
        	"        number_doc_clearing, date_clearing_frmt, " +
            " 	     receiver_mfo_bank, receiver_name_bank_alt,  " +
            " 	     receiver_number_bank_account, receiver_sname_owner_ba, " +
            " 	     payer_mfo_bank, payer_name_bank_alt,  " +
            " 	     payer_number_bank_account, payer_sname_owner_ba, " +
            " 	     payment_function, reconcile_state, id_clearing_line " +
            "  FROM (SELECT ROWNUM rn, id_clearing_line, number_doc_clearing, date_clearing_frmt, " +
            " 		        receiver_mfo_bank, receiver_sname_bank receiver_name_bank_alt,  " +
            " 		        receiver_number_bank_account, receiver_sname_owner_ba, " +
            " 		        payer_mfo_bank, payer_sname_bank payer_name_bank_alt,  " +
            " 		        payer_number_bank_account, payer_sname_owner_ba, " +
            " 		        name_currency, entered_amount_frmt, reconciled_amount_frmt, " +
            " 		        payment_function, reconcile_state_tsl, reconcile_state " +
            " 		   FROM (SELECT *" +
            "                  FROM " + getGeneralDBScheme()+".vc_clearing_lines_club_all " +
            "                 WHERE 1=1 ";
        if (needFind==false) {
        	if (!isEmpty(pIdBankStatementLine)) {
	        	mySQL = mySQL + 
	        		" AND (receiver_id_bank_account = ? OR payer_id_bank_account = ? OR ";
	        	pParam.add(new bcFeautureParam("int", line.getValue("CLIENT_ID_BANK_ACCOUNT")));
	        	pParam.add(new bcFeautureParam("int", line.getValue("CORR_ID_BANK_ACCOUNT")));
	        	if (!isEmpty(line.getValue("DEBET_AMOUNT"))) {
		        	mySQL = mySQL + " entered_amount = ? OR ";
		        	pParam.add(new bcFeautureParam("int", line.getValue("DEBET_AMOUNT")));
	        	}
	        	if (!isEmpty(line.getValue("CREDIT_AMOUNT"))) {
		        	mySQL = mySQL + " entered_amount = ? OR ";
		        	pParam.add(new bcFeautureParam("int", line.getValue("CREDIT_AMOUNT")));
	        	}
	        	mySQL = mySQL + "      payment_function = ?) " ;
	        	pParam.add(new bcFeautureParam("string", line.getValue("ASSIGNMENT")));
        	}
        } else {
        	if (!isEmpty(pLineState)) {
	        	mySQL = mySQL + " AND reconcile_state = ? ";
	        	pParam.add(new bcFeautureParam("string", pLineState));
	        } 
	    	if (!isEmpty(pFind)) {
	    		mySQL = mySQL + " AND (UPPER(number_doc_clearing) LIKE UPPER('%'||?||'%') OR " +
	    			"UPPER(date_clearing_frmt) LIKE UPPER('%'||?||'%') OR " + 
	    			"UPPER(receiver_sname_owner_ba) LIKE UPPER('%'||?||'%') OR " + 
	    			"UPPER(receiver_number_bank_account) LIKE UPPER('%'||?||'%') OR " + 
	    			"UPPER(receiver_sname_bank) LIKE UPPER('%'||?||'%') OR " + 
	    			"UPPER(payer_sname_owner_ba) LIKE UPPER('%'||?||'%') OR " + 
	    			"UPPER(payer_number_bank_account) LIKE UPPER('%'||?||'%') OR " + 
	    			"UPPER(payer_sname_bank) LIKE UPPER('%'||?||'%') OR " + 
	    			"UPPER(entered_amount_frmt) LIKE UPPER('%'||?||'%') OR " + 
	    			"UPPER(unreconciled_amount_frmt) LIKE UPPER('%'||?||'%') OR " + 
	    			"UPPER(payment_function) LIKE UPPER('%'||?||'%')) ";
	    		for (int i=0; i<11; i++) {
	    		    pParam.add(new bcFeautureParam("string", pFind));
	    		}
	    	}
        }
        pParam.add(new bcFeautureParam("int", p_end));
        pParam.add(new bcFeautureParam("int", p_beg));
        mySQL = mySQL +
        	" 		  ORDER BY date_clearing DESC)  " +
            "        WHERE ROWNUM < ? " + 
            " ) WHERE rn >= ?";

        StringBuilder html = new StringBuilder();
        PreparedStatement st = null; 
        
        boolean hasEditPermission = false;
        
        try{
       	 	if (isEditMenuPermited("FINANCE_KVITOVKA")>0) {
       	 		hasEditPermission = true;
        	}
            
        	LOGGER.debug(prepareSQLToLog(mySQL, pParam));
            con = Connector.getConnection(getSessionId());
            
            st = con.prepareStatement(mySQL);
            st = prepareParam(st, pParam);
            ResultSet rset = st.executeQuery();
            ResultSetMetaData mtd = rset.getMetaData();
            
            int colCount = mtd.getColumnCount();
            if (hasEditPermission && "N".equalsIgnoreCase(pPrint)) {
            	html.append("<form method=\"Post\" name=\"updateForm\" id=\"updateForm\" action=\"../crm/finance/kvitovkaupdate.jsp\" onSubmit=\"return CheckSelect(this);\">\n");
           	 	html.append("<input type=\"hidden\" name=\"action\" value=\"reconcile\">\n");
           	 	html.append("<input type=\"hidden\" name=\"process\" value=\"yes\">\n");
           	 	html.append("<input type=\"hidden\" name=\"id_bank_statement_line\" value=\""+pIdBankStatementLine+"\">\n");
            }
            if ("Y".equalsIgnoreCase(pPrint)) {
            	html.append(getPrintTable());
            } else {
            	html.append(getHeaderTable());
            }
            html.append("<thead>\n");
            
            html.append("<tr>\n");
            html.append("<th rowspan=\"2\">"+ commonXML.getfieldTransl("RN", false)+"</th>\n");
            html.append("<th rowspan=\"2\">&nbsp;</th>\n");
            html.append("<th rowspan=\"2\">"+ clearingXML.getfieldTransl("RECONCILE_STATE", false)+"</th>\n");
            html.append("<th rowspan=\"2\">"+ clearingXML.getfieldTransl("NAME_CURRENCY", false)+"</th>\n");
            html.append("<th rowspan=\"2\">"+ clearingXML.getfieldTransl("ENTERED_AMOUNT", false)+"</th>\n");
            html.append("<th rowspan=\"2\">"+ clearingXML.getfieldTransl("RECONCILED_AMOUNT", false)+"</th>\n");
            html.append("<th rowspan=\"2\">"+ clearingXML.getfieldTransl("NUMBER_DOC_CLEARING", false)+"</th>\n");
            html.append("<th rowspan=\"2\">"+ clearingXML.getfieldTransl("DATE_CLEARING", false)+"</th>\n");
            html.append("<th colspan=\"4\">"+ clearingXML.getfieldTransl("RECEIVER_PARTICIPANT", false)+"</th>\n");
            html.append("<th colspan=\"4\">"+ clearingXML.getfieldTransl("PAYER_PARTICIPANT", false)+"</th>\n");
            html.append("<th rowspan=\"2\">"+ clearingXML.getfieldTransl("PAYMENT_FUNCTION", false)+"</th>\n");
            html.append("</tr>\n");
            html.append("<tr>");
            html.append("<th>"+ clearingXML.getfieldTransl("RECEIVER_MFO_BANK", false)+"</th>\n");
            html.append("<th>"+ clearingXML.getfieldTransl("RECEIVER_NAME_BANK_ALT", false)+"</th>\n");
            html.append("<th>"+ clearingXML.getfieldTransl("RECEIVER_NUMBER_BANK_ACCOUNT", false)+"</th>\n");
            html.append("<th>"+ clearingXML.getfieldTransl("RECEIVER_SNAME_OWNER_BA", false)+"</th>\n");
            html.append("<th>"+ clearingXML.getfieldTransl("PAYER_MFO_BANK", false)+"</th>\n");
            html.append("<th>"+ clearingXML.getfieldTransl("PAYER_NAME_BANK_ALT", false)+"</th>\n");
            html.append("<th>"+ clearingXML.getfieldTransl("PAYER_NUMBER_BANK_ACCOUNT", false)+"</th>\n");
            html.append("<th>"+ clearingXML.getfieldTransl("PAYER_SNAME_OWNER_BA", false)+"</th>\n");
            html.append("</tr>");
            html.append("</thead><tbody>\n");
            
            if (hasEditPermission && "N".equalsIgnoreCase(pPrint)) {
           	 	html.append("<tr><td colspan=\"21\">");
           	 	html.append(getSubmitButtonAjax3("../crm/finance/kvitovkaupdate.jsp", "button_reconcile", ""));
            	html.append("</td></tr>\n");
            } 
            while (rset.next())
            {
                html.append("<tr id=\"elem_"+rset.getString("ID_CLEARING_LINE")+"\" onmouseout=\"coloredOut(this)\" onmouseover=\"coloredOver(this,1)\">");
                if (hasEditPermission && "N".equalsIgnoreCase(pPrint)) {
	                html.append("<td align=\"center\">");
	                if (!"RECONCILE".equalsIgnoreCase(rset.getString("RECONCILE_STATE"))) {
	                	html.append("<input type=\"checkbox\" class=\"inputfield\" value=\""+getValue2(rset.getString("ID_CLEARING_LINE"))+"\" name=\"id_"+rset.getString("ID_CLEARING_LINE")+"\" id=\"id_"+rset.getString("ID_CLEARING_LINE")+"\" onchange=\"checkEditAmountPermission('"+rset.getString("ID_CLEARING_LINE")+"')\">");
	                } else {
	                	html.append("&nbsp;");
	                }
	                html.append("</td>\n");
                }
                for (int i=1; i <= colCount-2; i++) {
                	String pValue = "";
                	if ("RECONCILED_AMOUNT_FRMT".equalsIgnoreCase(mtd.getColumnName(i))) {
                		pValue = "<input type=\"text\" size=\"8\" readonly class=\"inputfield-ro\" value=\""+getValue2(rset.getString("RECONCILED_AMOUNT_FRMT"))+"\" name=\"sum_"+rset.getString("ID_CLEARING_LINE")+"\" id=\"sum_"+rset.getString("ID_CLEARING_LINE")+"\">";
                	} else {
                		pValue = getValue2(rset.getString(i));
                	}
                	if ("RECONCILE_STATE_TSL".equalsIgnoreCase(mtd.getColumnName(i))) {
                		if (!"UNRECONCILE".equalsIgnoreCase(rset.getString("RECONCILE_STATE"))) {
                			html.append("<td align=\"center\"><font color=\"red\">" + pValue + "</font></td>\n");
                			//html.append("<td align=\"center\">"+getHyperLinkFirst()+myHyperLink+getHyperLinkMiddle()+ "<font color=\"red\">" + pValue + "</font>" + getHyperLinkEnd() + "</td>\n");
                		} else {
                			html.append("<td align=\"center\"><font color=\"black\">" + pValue + "</font></td>\n");
                			//html.append("<td align=\"center\">"+getHyperLinkFirst()+myHyperLink+getHyperLinkMiddle()+ "<font color=\"black\">" + pValue + "</font>" + getHyperLinkEnd() + "</td>\n");
                		}
                	} else {
                		html.append("<td align=\"center\"><font color=\"black\">" + pValue + "</font></td>\n");
                		//html.append("<td align=\"center\">"+getHyperLinkFirst()+myHyperLink+getHyperLinkMiddle()+ "<font color=\"black\">" + pValue + "</font>" + getHyperLinkEnd() + "</td>\n");
                	}
                }
                html.append("</tr>\n");
            }
            html.append("</tbody></table></form>\n");
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
    } //getKvitovkaHeadHTML
    
    public String getAllErrorTransHTML(String pTypeTrans, String pStateTrans, String pFind, String p_beg, String p_end, String pPrint) {
        StringBuilder html = new StringBuilder();
        PreparedStatement st = null;
        String myHyperLink = "";

        ArrayList<bcFeautureParam> pParam = initParamArray();

        String mySQL = 
        	" SELECT rn, id_trans, type_trans_txt, state_trans_tsl, sys_date, " +
        	"        sys_time, sname_dealer, id_term, " +
        	"        card_nr, nt_icc, " +
        	"		 opr_sum, sum_bon, id_telgr, err_type, src_trans " +
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
    		"                sys_date, sys_time, sname_dealer, id_term, card_nr, " +
        	"                nt_icc, nt_ext, action,  " +
        	"		         opr_sum, sum_bon, " +
        	"                DECODE(state_trans, " +
    		"                       -9,  '<font color=\"gray\">'||state_trans_tsl||'</font>', " +
    		"                       -7,  '<font color=\"blue\">'||state_trans_tsl||'</font>', " +
    		"                       -6,  '<font color=\"blue\">'||state_trans_tsl||'</font>', " +
    		"                       -4,  '<font color=\"green\">'||state_trans_tsl||'</font>', " +
    		"                       -2,  '<font color=\"ligthred\">'||state_trans_tsl||'</font>', " +
    		"                       -1,  '<font color=\"darkbrown\">'||state_trans_tsl||'</font>', " +
    		"                       0,  state_trans_tsl, " +
    		"                       1,  '<font color=\"red\"><b>'||state_trans_tsl||'</b></font>', " +
    		"                       5,  '<font color=\"red\"><b>'||state_trans_tsl||'</b></font>', " +
    		"                       8,  '<font color=\"red\"><b>'||state_trans_tsl||'</b></font>', " +
    		"                       9,  '<font color=\"red\"><b>'||state_trans_tsl||'</b></font>', " +
    		"                       state_trans_tsl) state_trans_tsl, " +
    		"                id_telgr, err_type, " +
    		"                CASE WHEN LENGTH(src_trans) > 100 " +
    		"                     THEN SUBSTR(src_trans, 1, 100)||'...'" +
    		"                     ELSE src_trans" +
    		"                END src_trans " +
        	"           FROM (SELECT * " +
        	"                   FROM " + getGeneralDBScheme()+".vc_trans_all_error_all " +
        	"                  WHERE 1=1 ";
        if (!isEmpty(pStateTrans)) {
        	mySQL = mySQL + " AND state_trans = ? ";
        	pParam.add(new bcFeautureParam("int", pStateTrans));
        }
        if (!isEmpty(pTypeTrans)) {
       	 	mySQL = mySQL + " AND type_trans = ? ";
       	 	pParam.add(new bcFeautureParam("int", pTypeTrans));
        } else {
        	mySQL = mySQL + " AND type_trans <> -4 ";
        }
        if (!isEmpty(pFind)) {
    		mySQL = mySQL + 
    			" AND (UPPER(TO_CHAR(id_term)) LIKE UPPER('%'||?||'%') OR " +
    			"	   UPPER(sys_date) LIKE UPPER('%'||?||'%') OR " +
    			"	   UPPER(sys_time) LIKE UPPER('%'||?||'%') OR " +
    			"	   UPPER(card_nr) LIKE UPPER('%'||?||'%')) ";
    		for (int i=0; i<4; i++) {
    		    pParam.add(new bcFeautureParam("string", pFind));
    		}
    	}
        pParam.add(new bcFeautureParam("int", p_end));
        pParam.add(new bcFeautureParam("int", p_beg));
        mySQL = mySQL +
        	"          		   ORDER BY id_trans DESC) " +
        	"          WHERE ROWNUM < ? " + 
        	" ) WHERE rn >= ?";
        try{
            con = Connector.getConnection(getSessionId());
            st = con.prepareStatement(mySQL);
            st = prepareParam(st, pParam);
            LOGGER.debug(prepareSQLToLog(mySQL, pParam));

        	ResultSet rset = st.executeQuery();
            ResultSetMetaData mtd = rset.getMetaData();
            int colCount = mtd.getColumnCount();

            if ("Y".equalsIgnoreCase(pPrint)) {
            	html.append(getPrintTable());
            } else {
            	html.append(getHeaderTable());
            }
            html.append("<thead><tr>\n");
            for (int i=1; i <= colCount-3; i++) {
            	html.append(getBottomFrameTableTH(transactionXML, mtd.getColumnName(i)));
            }
            html.append("</tr></thead><tbody>\n");

            while (rset.next())
            {
            	myHyperLink = getHyperLinkHeaderFirst()+"../crm/cards/trans_correctionspecs.jsp?id="+rset.getString("ID_TRANS")+
                "&idtelgr="+rset.getString("ID_TELGR")+"&err_type="+rset.getString("ERR_TYPE")+getHyperLinkMiddle();

            	html.append("<tr id=\"elem_"+rset.getString("ID_TRANS")+"\" onmouseout=\"coloredOut(this)\" onmouseover=\"coloredOver(this,1)\">");                
            	if ("C".equalsIgnoreCase(rset.getString("ERR_TYPE"))) {
                
            		html.append(
                        "<td align=center>" + myHyperLink + getValue2(rset.getString(1)) + getHyperLinkEnd()+ "</td>\n" +
                        "<td align=center>" + myHyperLink + getValue2(rset.getString(2)) + getHyperLinkEnd()+ "</td>\n" +
                        "<td>" + myHyperLink + getValue2(rset.getString(3)) + getHyperLinkEnd()+ "</td>\n" +
                        "<td>" + myHyperLink + getValue2(rset.getString(4)) + getHyperLinkEnd()+ "</td>\n" +
                        "<td align=center>" + myHyperLink + getValue2(rset.getString(5)) + getHyperLinkEnd()+ "</td>\n" +
                        "<td align=center>" + myHyperLink + getValue2(rset.getString(6)) + getHyperLinkEnd()+ "</td>\n" +
                        "<td align=center>" + myHyperLink + getValue2(rset.getString(7)) + getHyperLinkEnd()+ "</td>\n" +
                        "<td align=center>" + myHyperLink + getValue2(rset.getString(8)) + getHyperLinkEnd()+ "</td>\n" +
                        "<td align=center>" + myHyperLink + getValue2(rset.getString(9)) + getHyperLinkEnd()+ "</td>\n" +
                        "<td align=center>" + myHyperLink + getValue2(rset.getString(10)) + getHyperLinkEnd()+ "</td>\n" +
                        "<td align=center>" + myHyperLink + getValue2(rset.getString(11)) + getHyperLinkEnd()+ "</td>\n" +
                        "<td align=center>" + myHyperLink + getValue2(rset.getString(12)) + getHyperLinkEnd()+ "</td>\n");
                } else if ("P".equalsIgnoreCase(rset.getString("ERR_TYPE"))) {
                	html.append(
                            "<td align=center>" + myHyperLink + getValue2(rset.getString(1)) + getHyperLinkEnd()+ "</td>\n" +
                            "<td align=center>" + myHyperLink + getValue2(rset.getString(2)) + getHyperLinkEnd()+ "</td>\n" +
                            "<td>" + myHyperLink + getValue2(rset.getString(3)) + getHyperLinkEnd()+ "</td>\n" +
                            "<td>" + myHyperLink + getValue2(rset.getString(4)) +getHyperLinkEnd()+ "</td>\n" +
                            "<td colspan=\"9\">" + myHyperLink + getValue2(rset.getString(15)) + getHyperLinkEnd()+ "</td>\n");
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
    } // getAllErrorTransHTML

    public String getTransactionsHeadHTML(String pTypeTrans, String pPayType, String pStateTrans, String id_profile, String pFind, String p_beg, String p_end, String pPrint) {

    	ArrayList<bcFeautureParam> pParam = initParamArray();

    	StringBuilder html = new StringBuilder();
        PreparedStatement st_profile = null;
        String myFiltr = "";
        String mySQL_profile = "SELECT filtr FROM " + getGeneralDBScheme()+".v_menu_profile_filtr_all WHERE id_menu_element = ? ";
        String mySQL =  "";
        
        LOGGER.debug(mySQL_profile);
            
        if (!isEmpty(id_profile)) {
            try{
            	con = Connector.getConnection(getSessionId());
            	st_profile = con.prepareStatement(mySQL_profile);
            	st_profile.setInt(1, Integer.parseInt(id_profile));
            	ResultSet rset_profile = st_profile.executeQuery();
            	while (rset_profile.next()) {
            		if (!("".equalsIgnoreCase(rset_profile.getString(1).trim()))) {
            			myFiltr = myFiltr + " AND "+rset_profile.getString(1)+" ";
            		}
            	}
            } // try
            catch (SQLException e) {LOGGER.error(html, e); html.append(showCRMException(e));}
           	catch (Exception el) {LOGGER.error(html, el); html.append(showCRMException(el));}
            finally {
                try {
                    if (st_profile!=null) {
						st_profile.close();
					}
                } catch (SQLException w) {w.toString();}
                try {
                    if (con!=null) {
						con.close();
					}
                } catch (SQLException w) {w.toString();}
                Connector.closeConnection(con);
            } // finally
        }
        if (!isEmpty(pTypeTrans)) {
        	myFiltr = myFiltr + " AND type_trans = ? ";
        	pParam.add(new bcFeautureParam("int", pTypeTrans));
        }
        if (!isEmpty(pPayType)) {
        	myFiltr = myFiltr + " AND pay_type = ? ";
        	pParam.add(new bcFeautureParam("string", pPayType));
        }
        if (!isEmpty(pStateTrans)) {
        	myFiltr = myFiltr + " AND state_trans = ? ";
        	pParam.add(new bcFeautureParam("int", pStateTrans));
        }
        if (!isEmpty(pFind)) {
           	myFiltr = myFiltr + " AND (UPPER(id_trans) LIKE UPPER('%'||?||'%') OR " +
           		"UPPER(TO_CHAR(id_term)) LIKE UPPER('%'||?||'%') OR " +
           		"UPPER(sys_date_frmt) LIKE UPPER('%'||?||'%') OR " +
       			"UPPER(card_serial_number) LIKE UPPER('%'||?||'%')) ";
           	for (int i=0; i<4; i++) {
           	    pParam.add(new bcFeautureParam("string", pFind));
           	}
       	}
            
        mySQL = " SELECT rn, name_service_place, id_term, cashier_name, " +
        		"        nc_term, type_trans_txt, sys_date_frmt date_trans_frmt, " +
        		"        cd_card1, /*nt_ext, action,*/  " +
   				"		 CASE WHEN NVL(opr_sum,0) > 0 " +
   				"             THEN '<font color=\"blue\">'||opr_sum_frmt||'</font>'||' '||sname_currency " +
   				"             ELSE '' " +
   				"        END opr_sum_frmt, /*sum_pay_cash_frmt, sum_pay_card_frmt, sum_pay_bon_frmt,*/ " +
   				"        CASE WHEN NVL(sum_bon,0) > 0 " +
   				"             THEN '<font color=\"blue\">'||sum_bon_frmt||'</font>' " +
   				"             ELSE '' " +
   				"        END sum_bon_frmt, " +
   				"        /*CASE WHEN NVL(sum_disc,0) > 0 " +
   				"             THEN '<font color=\"blue\">'||sum_disc_frmt||'</font>'||' '||sname_currency " +
   				"             ELSE '' " +
   				"        END sum_disc_frmt,*/ state_trans_tsl, id_trans, id_telgr " +
   				"   FROM (SELECT ROWNUM rn, id_trans, " +
   				"			   DECODE(fcd_trans_type, " +
	            "                       'REC_PAYMENT',  '<font style = \"color: black\"><b>'||name_trans_type_full||'</b></font>', " +
	            "                       'REC_MOV_BON',  '<font style = \"color: blue\"><b>'||name_trans_type_full||'</b></font>', " +
	            "                       'REC_CHK_CARD',  '<font style = \"color: green\"><b>'||name_trans_type_full||'</b></font>', " +
	            "                       'REC_INVAL_CARD',  '<font style = \"color: red\"><b>'||name_trans_type_full||'</b></font>', " +
	            "                       'REC_STORNO_BON',  '<font style = \"color: red\"><b>'||name_trans_type_full||'</b></font>', " +
	            "                       'REC_PAYMENT_IM',  '<font style = \"color: black\"><b>'||name_trans_type_full||'</b></font>', " +
	            "                       'REC_PAYMENT_EXT',  '<font style = \"color: black\"><b>'||name_trans_type_full||'</b></font>', " +
	    		"                       'REC_ACTIVATION',  '<font style = \"color: blue\"><b>'||name_trans_type_full||'</b></font>', " +
	    		"                       'REC_QUESTIONING',  '<font style = \"color: blue\"><b>'||name_trans_type_full||'</b></font>', " +
	    		"                       'REC_PUT_CARD',  '<font style = \"color: #191970\"><b>'||name_trans_type_full||'</b></font>', " +
	            "                       'REC_COUPON',  '<font style = \"color: #8B8989\"><b>'||name_trans_type_full||'</b></font>', " +
	    		"                       'REC_MEMBERSHIP_FEE',  '<font style = \"color: #8B3A3A\"><b>'||name_trans_type_full||'</b></font>', " +
	    		"                       'REC_POINT_FEE',  '<font style = \"color: #C8860B\"><b>'||name_trans_type_full||'</b></font>', " +
	    		"                       'REC_MTF',  '<font style = \"color: #B8860B\"><b>'||name_trans_type_full||'</b></font>', " +
	    		"                       'REC_SHARE_FEE',  '<font style = \"color: green\"><b>'||name_trans_type_full||'</b></font>', " +
	    		"                       'REC_TRANSFER_GET_POINT',  '<font style = \"color: #BA55D3\"><b>'||name_trans_type_full||'</b></font>', " +
	    		"                       'REC_TRANSFER_PUT_POINT',  '<font style = \"color: #BA55D3\"><b>'||name_trans_type_full||'</b></font>', " +
	    		//"                       'REC_CANCEL',  '<font style = \"color: red\"><b>'||name_trans_type_full||'</b></font>', " +
	    		//"                       'REC_RETURN',  '<font style = \"color: red\"><b>'||name_trans_type_full||'</b></font>', " +
	    		"                       name_trans_type_full" +
	    		"                ) type_trans_txt, " +
	            "                sys_date_frmt, " +
   				"                name_dealer, name_service_place, id_term, cd_card1, " +
   				"				 DECODE(fcd_trans_type, " +
	            "                       'REC_PAYMENT', sname_currency, " +
	            "                       'REC_MOV_BON', '', " +
	            "                       'REC_CHK_CARD', '', " +
	            "                       'REC_INVAL_CARD', '', " +
	            "                       'REC_STORNO_BON', sname_currency, " +
	            "                       'REC_PAYMENT_IM', sname_currency, " +
	            "                       'REC_PAYMENT_EXT', sname_currency, " +
	    		"                       'REC_ACTIVATION', '', " +
	    		"                       'REC_QUESTIONING', '', " +
	    		"                       'REC_PUT_CARD', sname_currency, " +
	            "                       'REC_COUPON', '', " +
	    		"                       'REC_MEMBERSHIP_FEE', sname_currency, " +
	    		"                       'REC_MTF', sname_currency, " +
	            "                       'REC_POINT_FEE', '', " +
	    		"                       'REC_SHARE_FEE', sname_currency, " +
	    		"                       'REC_TRANSFER_GET_POINT', '', " +
	    		"                       'REC_TRANSFER_PUT_POINT', '', " +
	    		"                       'REC_PAYMENT_INVOICE', sname_currency, " +
	    		"                       'REC_SHARE_FEE_CHANGE', sname_currency, " +
	    		"                       'REC_TRANSFORM_FROM_SHARE', sname_currency, " +
	    		//"                       'REC_CANCEL', sname_currency, " +
	    		//"                       'REC_RETURN', sname_currency, " +
	    		"                       sname_currency" +
	    		"                ) sname_currency, nc_term, nt_ext, action,  " +
	    		"		         opr_sum, opr_sum_frmt, sum_pay_cash_frmt, sum_pay_card_frmt, sum_pay_bon_frmt, " +
	    		"                sum_bon, sum_bon_frmt, sum_disc, sum_disc_frmt, " +
	    		"                DECODE(state_trans, " +
	    		"                       -99,  '<font color=\"gray\">'||state_trans_tsl||'</font>', " +
	    		"                       -55,  '<font color=\"red\">'||state_trans_tsl||'</font>', " +
	    		"                       -54,  '<font color=\"blue\">'||state_trans_tsl||'</font>', " +
	    		"                       -13,  '<font color=\"red\"><b>'||state_trans_tsl||'</b></font>', " +
	    		"                       -12,  '<font color=\"red\"><b>'||state_trans_tsl||'</b></font>', " +
	    		"                       -11,  '<font color=\"red\"><b>'||state_trans_tsl||'</b></font>', " +
	    		"                       -9,  '<font color=\"gray\">'||state_trans_tsl||'</font>', " +
	    		"                       -7,  '<font color=\"blue\">'||state_trans_tsl||'</font>', " +
	    		"                       -6,  '<font color=\"blue\">'||state_trans_tsl||'</font>', " +
	    		"                       -4,  '<font color=\"green\">'||state_trans_tsl||'</font>', " +
	    		"                       -2,  '<font color=\"ligthred\">'||state_trans_tsl||'</font>', " +
	    		"                       -1,  '<font color=\"darkbrown\">'||state_trans_tsl||'</font>', " +
	    		"                       0,  '<font color=\"black\"><b>'||state_trans_tsl||'</b></font>', " +
	    		"                       1,  '<font color=\"red\"><b>'||state_trans_tsl||'</b></font>', " +
	    		"                       5,  '<font color=\"red\"><b>'||state_trans_tsl||'</b></font>', " +
	    		"                       9,  '<font color=\"red\"><b>'||state_trans_tsl||'</b></font>', " +
	    		"                       state_trans_tsl) state_trans_tsl, cashier_name, " +
	    		"                id_telgr " +
	    		"           FROM (SELECT *" +
	    		"                   FROM " + getGeneralDBScheme()+".vc_trans_club_all " +
	    		"                  WHERE 1=1 " + myFiltr + 
	    		"				   ORDER BY sys_date desc, card_serial_number, nt_icc) WHERE ROWNUM < ? " + 
	    		"        ) WHERE rn >= ?";

        pParam.add(new bcFeautureParam("int", p_end));
        pParam.add(new bcFeautureParam("int", p_beg));
        
        html.append(getHeaderParamHTML(
       			mySQL,
        		pParam,
                2,
                "D",
                0,
              	"",
               	"../crm/cards/transactionspecs.jsp?id=:FIELD12:", 
               	transactionXML,
               	pPrint));

        return html.toString();
    } //getTransactionsHeadHTML
    
    public String getNatpersonsHeadHTML(String pFind, String p_beg, String p_end, String pPrint) {

    	ArrayList<bcFeautureParam> pParam = initParamArray();

    	String mySQL = 
        	" SELECT rn, full_name, fact_adr_full, date_of_birth_frmt, phone_contact, email, id_nat_prs " +
    		"   FROM (SELECT ROWNUM rn, id_nat_prs, full_name, fact_adr_full, date_of_birth_frmt, phone_contact, email " +
    		"  		    FROM (SELECT id_nat_prs, full_name, fact_adr_full, date_of_birth_frmt, phone_contact, email" +
    		"                   FROM " + getGeneralDBScheme()+".vc_nat_prs_priv_all ";
    	if (!isEmpty(pFind)) {
    		mySQL = mySQL + 
    			" WHERE (TO_CHAR(id_nat_prs) LIKE UPPER('%'||?||'%') OR " +
    			"        UPPER(full_name) LIKE UPPER('%'||?||'%') OR " +
				"        UPPER(fact_adr_full) LIKE UPPER('%'||?||'%') OR " +
				"        UPPER(fact_adr_full) LIKE UPPER('%'||?||'%') OR " +
    			"        UPPER(date_of_birth_frmt) LIKE UPPER('%'||?||'%') OR " +
    			"        UPPER(phone_contact) LIKE UPPER('%'||?||'%') OR " + 
    			"        UPPER(email) LIKE UPPER('%'||?||'%')) ";
    		for (int i=0; i<7; i++) {
    		    pParam.add(new bcFeautureParam("string", pFind));
    		}
    	}
    	pParam.add(new bcFeautureParam("int", p_end));
    	pParam.add(new bcFeautureParam("int", p_beg));
    	mySQL = mySQL + ") WHERE ROWNUM < ?" +  
    		")  WHERE rn >= ?";

        return getHeaderParamHTML(
        		mySQL,
        		pParam,
                1,
                "D",
            	0,
            	"",
            	"../crm/clients/natpersonspecs.jsp?id=:FIELD7:", 
            	natprsXML,
            	pPrint);
    }
    
    public String getShareholdersHeadHTML(String pFind, String p_beg, String p_end, String pPrint) {

    	ArrayList<bcFeautureParam> pParam = initParamArray();
    	
    	String mySQL = 
        	" SELECT rn, name_shareholder, fact_adr_full, phone_mobile, email, id_nat_prs " +
    		"   FROM (SELECT ROWNUM rn, name_shareholder, fact_adr_full, phone_mobile, email, id_nat_prs " +
    		"  		    FROM (SELECT full_name name_shareholder, fact_adr_full, phone_mobile, email, id_nat_prs " +
    		"                   FROM " + getGeneralDBScheme()+".vc_nat_prs_club_all " +
    		"                  WHERE 1=1 ";
    	if (!isEmpty(pFind)) {
    		mySQL = mySQL + 
    			" AND (UPPER(full_name) LIKE UPPER('%'||?||'%') OR " +
    			"      UPPER(fact_adr_full) LIKE UPPER('%'||?||'%') OR " +
				"      UPPER(phone_mobile) LIKE UPPER('%'||?||'%') OR " +
				"      UPPER(email) LIKE UPPER('%'||?||'%')) ";
    		for (int i=0; i<4; i++) {
    		    pParam.add(new bcFeautureParam("string", pFind));
    		}
    	}
    	pParam.add(new bcFeautureParam("int", p_end));
    	pParam.add(new bcFeautureParam("int", p_beg));
    	mySQL = mySQL + ") WHERE ROWNUM < ?" +  
    		")  WHERE rn >= ?";

        return getHeaderParamHTML(
        		mySQL,
        		pParam,
                1,
                "D",
            	0,
            	"",
            	"../crm/clients/natpersonspecs.jsp?id=:FIELD6:", 
            	natprsXML,
            	pPrint);
    }
    
    public String getShareholdersHeadHTMLOld(String pType, String pFind, String pClubMemberStatus, String p_beg, String p_end, String pPrint) {

    	ArrayList<bcFeautureParam> pParam = initParamArray();
    	
    	String pNatPrs = commonXML.getfieldTransl("h_nat_prs_one", false);
    	String pJurPrs = commonXML.getfieldTransl("h_jur_prs_one", false);

        String mySQL = 
        	" SELECT rn, type_shareholder, name_shareholder, adr_full, sname_club club_name, " +
        	"		 name_club_member_status club_member_status, date_beg_frmt club_date_beg_frmt, /*date_end_frmt,*/ " +
        	"		 phone_mobile, detail_hyperlink " +
    		"   FROM (SELECT ROWNUM rn, type_shareholder, name_shareholder, adr_full, sname_club, " +
    		"				 name_club_member_status, date_beg_frmt, date_end_frmt, " +
    		"				 phone_mobile, detail_hyperlink " +
    		"  		    FROM (SELECT DECODE(type_shareholder," +
    		"						 		'NAT_PRS', '<font color=\"blue\"><b>" + pNatPrs + "</b></font>', " + 
    		"						 		'JUR_PRS', '<font color=\"green\"><b>" + pJurPrs + "</b></font>', " + 
    		"						 		type_shareholder" +
    		"                        ) type_shareholder, " +
    		"						 name_shareholder, adr_full, sname_club, " +
    		"						 DECODE(cd_club_member_status, " +
       		"                       		'CLOSED', '<font color=\"red\"><b>'||name_club_member_status||'</b></font>', " +
       		"                       		'OUT_OF_CLUB', '<font color=\"gray\">'||name_club_member_status||'</font>', " +
       		"                       		'IN_PROCESS', '<font color=\"blue\">'||name_club_member_status||'</font>', " +
       		"                       		'PARTICIPATE', '<font color=\"green\">'||name_club_member_status||'</font>', " +
       		"                       		name_club_member_status" +
       		"                		) name_club_member_status, " +
       		"						date_beg_frmt, date_end_frmt, phone_mobile, " +
       		"						DECODE(kind_shareholder," +
    		"						 		'NAT_PRS', '../crm/clients/natpersonspecs.jsp?id='||TO_CHAR(id_shareholder), " + 
    		"						 		'JUR_PRS', '../crm/clients/yurpersonspecs.jsp?id='||TO_CHAR(id_shareholder), " + 
    		"						 		''" +
    		"                        ) detail_hyperlink " +
    		"                   FROM " + getGeneralDBScheme()+".vc_nat_prs_shareholder_all " +
    		"                  WHERE 1=1 ";
    	if (!(isEmpty(pType) || "ALL".equalsIgnoreCase(pType))) {
    		mySQL = mySQL + " AND type_shareholder = ? ";
    		pParam.add(new bcFeautureParam("string", pType));
    	}
    	if (!isEmpty(pClubMemberStatus)) {
    		mySQL = mySQL + " AND cd_club_member_status = ? ";
    		pParam.add(new bcFeautureParam("string", pClubMemberStatus));
    	}
    	if (!isEmpty(pFind)) {
    		mySQL = mySQL + 
    			" AND (TO_CHAR(name_shareholder) LIKE UPPER('%'||?||'%') OR " +
    			"      UPPER(adr_full) LIKE UPPER('%'||?||'%') OR " +
				"      UPPER(date_beg_frmt) LIKE UPPER('%'||?||'%') OR " +
				"      UPPER(date_end_frmt) LIKE UPPER('%'||?||'%') OR " +
    			"      UPPER(phone_mobile) LIKE UPPER('%'||?||'%')) ";
    		for (int i=0; i<5; i++) {
    		    pParam.add(new bcFeautureParam("string", pFind));
    		}
    	}
    	pParam.add(new bcFeautureParam("int", p_end));
    	pParam.add(new bcFeautureParam("int", p_beg));
    	mySQL = mySQL + ") WHERE ROWNUM < ?" +  
    		")  WHERE rn >= ?";

        return getHeaderParamHTML(
        		mySQL,
        		pParam,
                2,
                "D",
            	0,
            	"",
            	""/*"../crm/clients/natpersonspecs.jsp?id=:FIELD9:"*/, 
            	natprsXML,
            	pPrint);
    }
    
    public String getTermSessionsHeadHTML(String pFind, String pDataType, String pDataState, String p_beg, String p_end, String pPrint) {
    	readDateFormat();
        String mySQL = 
        		" SELECT a.rn, a.id_term_ses, a.date_beg_frmt, a.date_end_frmt, a.id_term, a.sname_service_place, /*a.id_sam,*/ " +
                "        CASE WHEN a.need_card_req = 0 THEN b.name_term_ses_creq_state " +
        		"             ELSE DECODE(NVL(a.need_card_req, 0), " +
        		"                         0, '<font color=\"black\">'||b.name_term_ses_creq_state||'</font>', " +
        		"                         -1, '<font color=\"blue\">'||b.name_term_ses_creq_state||'</font>', " +
        		"                         -9, '<b><font color=\"red\">'||b.name_term_ses_creq_state||'</font></b>', " +
        		"                         1, '<font color=\"green\">'||b.name_term_ses_creq_state||'</font>', " +
        		"                         9, '<font color=\"darkgreen\">'||b.name_term_ses_creq_state||'</font>', " +
        		"                         '<font>'||b.name_term_ses_creq_state||'</font>' " +
        		"                  )||" +
        		"                  ' ('||RTRIM(DECODE(a.action_card_req," +
        		"                              0, '<span style=\"color:green;\" title=\"" + term_sesXML.getfieldTransl("h_card_req_action_0", false) + "\">', " +
        		"                              1, '<span style=\"color:blue;\" title=\"" + term_sesXML.getfieldTransl("h_card_req_action_1", false) + "\">', " +
        		"                              2, '<span style=\"color:red;font-weight:bold;\" title=\"" + term_sesXML.getfieldTransl("h_card_req_action_2", false) + "\">', " +
        		"                              3, '<span style=\"color:green;font-weight:bold;\" title=\"" + term_sesXML.getfieldTransl("h_card_req_action_3", false) + "\">', " +
        		"                              4, '<span style=\"color:red;font-weight:bold;\" title=\"" + term_sesXML.getfieldTransl("h_card_req_action_4", false) + "\">', " +
        		"                              5, '<span style=\"color:brown;font-weight:bold;\" title=\"" + term_sesXML.getfieldTransl("h_card_req_action_5", false) + "\">', " +
        		"                              6, '<span style=\"color:red;font-weight:bold;\" title=\"" + term_sesXML.getfieldTransl("h_card_req_action_6", false) + "\">', " +
        		"                              7, '<span style=\"color:red;font-weight:bold;\" title=\"" + term_sesXML.getfieldTransl("h_card_req_action_7", false) + "\">', " +
        		"                              8, '<span style=\"color:red;font-weight:bold;\" title=\"" + term_sesXML.getfieldTransl("h_card_req_action_8", false) + "\">', " +
        		"                              9, '<span style=\"color:red;font-weight:bold;\" title=\"" + term_sesXML.getfieldTransl("h_card_req_action_9", false) + "\">', " +
        		"                              15, '<span style=\"color:red;font-weight:bold;\" title=\"" + term_sesXML.getfieldTransl("h_card_req_action_15", false) + "\">', " +
        		"		                       '' " +
        		"                       )" +
        		"                   ||DECODE(action_card_req,NULL,'</span>',action_card_req||'</span>, ')||" +
        		"                   CASE WHEN NVL(need_online_pay,0) = 0 THEN '' " +
        		"                        ELSE '<span '||need_online_pay_state||' title=\""+term_sesXML.getfieldTransl("h_is_online_pay", false)+"\">"+term_sesXML.getfieldTransl("h_is_online_pay_short", false)+"</span> <span '||need_online_pay_style||' title=\"'||need_online_pay_state||'\">'||need_online_pay||'</span>, ' "+
        		"                   END||" +
        		"                   CASE WHEN NVL(need_club_pay,0) = 0 THEN '' " +
        		"                        ELSE '<span '||need_club_pay_state||' title=\""+term_sesXML.getfieldTransl("h_is_club_pay", false)+"\">"+term_sesXML.getfieldTransl("h_is_club_pay_short", false)+"</span> <span '||need_club_pay_style||' title=\"'||need_club_pay_state||'\">'||need_club_pay||'</span>, ' "+
        		"                   END||" +
        		"                   CASE WHEN NVL(need_online_storno,0) = 0 THEN '' " +
        		"                        ELSE '<span '||need_online_storno_state||' title=\""+term_sesXML.getfieldTransl("h_is_online_storno", false)+"\">"+term_sesXML.getfieldTransl("h_is_online_storno_short", false)+"</span> <span '||need_online_storno_style||' title=\"'||need_online_storno_state||'\">'||need_online_storno||'</span>, ' "+
        		"                   END||" +
        		"                   CASE WHEN NVL(need_adv_pay,0) = 0 THEN '' " +
        		"                        ELSE '<span '||need_adv_pay_state||' title=\""+term_sesXML.getfieldTransl("h_is_adv_pay", false)+"\">"+term_sesXML.getfieldTransl("h_is_adv_pay_short", false)+"</span> <span '||need_adv_pay_style||' title=\"'||need_adv_pay_state||'\">'||need_adv_pay||'</span>, ' "+
        		"                   END,', ')||')</span>'" +
        		"        END need_card_req_tsl," +
        		"        DECODE(NVL(a.need_col_data, 0), " +
        		"               0, c.name_term_ses_data_state, " +
        		"               -1, '<font color=\"blue\">'||c.name_term_ses_data_state||'</font>', " +
        		"               -9, '<b><font color=\"red\">'||c.name_term_ses_data_state||'</font></b>', " +
        		"               1, '<font color=\"green\">'||c.name_term_ses_data_state||'</font>', " +
        		"               c.name_term_ses_data_state " +
        		"        ) need_col_data_tsl, " +
        		"        DECODE(NVL(a.need_tpar_data, 0), " +
        		"               0, d.name_term_ses_param_state, " +
        		"               -1, '<font color=\"blue\">'||d.name_term_ses_param_state||'</font>', " +
        		"               -9, '<b><font color=\"red\">'||d.name_term_ses_param_state||'</font></b>', " +
        		"               1, '<font color=\"green\">'||d.name_term_ses_param_state||'</font>', " +
        		"               9, '<font color=\"darkgreen\">'||d.name_term_ses_param_state||'</font>', " +
        		"               d.name_term_ses_param_state" +
        		"        ) need_tpar_data_tsl, " +
        		"        DECODE(NVL(a.need_term_mon, 0), " +
        		"                0, e.name_term_ses_mon_state, " +
        		"                -1, '<font color=\"blue\">'||e.name_term_ses_mon_state||'</font>', " +
        		"                -9, '<b><font color=\"red\">'||e.name_term_ses_mon_state||'</font></b>', " +
        		"                1, '<font color=\"green\">'||e.name_term_ses_mon_state||'</font>', " +
                "                9, '<font color=\"darkgreen\">'||e.name_term_ses_mon_state||'</font>', " +
        		"               e.name_term_ses_mon_state" +
        		"        ) need_term_mon_tsl /*, " +
        		"        a.desc_term_ses*/ " + 
        		"   FROM (SELECT rn, id_term_ses, id_term, sname_service_place, /*id_sam,*/  " +
        		"  			     date_beg_frmt, date_end_frmt, desc_term_ses,  " +
        		"  			     need_card_req, action_card_req, need_col_data, need_tpar_data, " +
        		"                need_term_mon, need_online_pay, " +
        		"                DECODE(NVL(need_online_pay, 0), " +
        		"                       -1, '" + term_sesXML.getfieldTransl("h_need_card_req_state_1neg", false) + "', " +
        		"                       -9, '" + term_sesXML.getfieldTransl("h_need_card_req_state_9neg", false) + "', " +
        		"                       1, '" + term_sesXML.getfieldTransl("h_need_card_req_state_1", false) + "', " +
                "                       9, '" + term_sesXML.getfieldTransl("h_need_card_req_state_9", false) + "', " +
        		"                       ''" +
        		"                ) need_online_pay_state," +
        		"                DECODE(NVL(need_online_pay, 0), " +
        		"                       -1, 'style=\"color:blue;\"', " +
        		"                       -9, 'style=\"color:red;font-weight:bold;\"', " +
        		"                       1, 'style=\"color:green;\"', " +
                "                       9, 'style=\"color:darkgreen;\"', " +
        		"                       ''" +
        		"                ) need_online_pay_style," +
        		"  			     need_online_storno, " +
        		"                DECODE(NVL(need_online_storno, 0), " +
        		"                       -1, '" + term_sesXML.getfieldTransl("h_need_card_req_state_1neg", false) + "', " +
        		"                       -9, '" + term_sesXML.getfieldTransl("h_need_card_req_state_9neg", false) + "', " +
        		"                       1, '" + term_sesXML.getfieldTransl("h_need_card_req_state_1", false) + "', " +
                "                       9, '" + term_sesXML.getfieldTransl("h_need_card_req_state_9", false) + "', " +
        		"                       ''" +
        		"                ) need_online_storno_state," +
        		"                DECODE(NVL(need_online_storno, 0), " +
        		"                       -1, 'style=\"color:blue;\"', " +
        		"                       -9, 'style=\"color:red;font-weight:bold;\"', " +
        		"                       1, 'style=\"color:green;\"', " +
                "                       9, 'style=\"color:darkgreen;\"', " +
        		"                       ''" +
        		"                ) need_online_storno_style," +
        		"                need_club_pay, " +
        		"                DECODE(NVL(need_club_pay, 0), " +
        		"                       -1, '" + term_sesXML.getfieldTransl("h_need_card_req_state_1neg", false) + "', " +
        		"                       -9, '" + term_sesXML.getfieldTransl("h_need_card_req_state_9neg", false) + "', " +
        		"                       1, '" + term_sesXML.getfieldTransl("h_need_card_req_state_1", false) + "', " +
                "                       9, '" + term_sesXML.getfieldTransl("h_need_card_req_state_9", false) + "', " +
        		"                       ''" +
        		"                ) need_club_pay_state," +
        		"                DECODE(NVL(need_club_pay, 0), " +
        		"                       -1, 'style=\"color:blue;\"', " +
        		"                       -9, 'style=\"color:red;font-weight:bold;\"', " +
        		"                       1, 'style=\"color:green;\"', " +
                "                       9, 'style=\"color:darkgreen;\"', " +
        		"                       ''" +
        		"                ) need_club_pay_style," +
        		"                need_adv_pay, " +
        		"                DECODE(NVL(need_adv_pay, 0), " +
        		"                       -1, '" + term_sesXML.getfieldTransl("h_need_card_req_state_1neg", false) + "', " +
        		"                       -9, '" + term_sesXML.getfieldTransl("h_need_card_req_state_9neg", false) + "', " +
        		"                       1, '" + term_sesXML.getfieldTransl("h_need_card_req_state_1", false) + "', " +
                "                       9, '" + term_sesXML.getfieldTransl("h_need_card_req_state_9", false) + "', " +
        		"                       ''" +
        		"                ) need_adv_pay_state," +
        		"                DECODE(NVL(need_adv_pay, 0), " +
        		"                       -1, 'style=\"color:blue;\"', " +
        		"                       -9, 'style=\"color:red;font-weight:bold;\"', " +
        		"                       1, 'style=\"color:green;\"', " +
                "                       9, 'style=\"color:darkgreen;\"', " +
        		"                       ''" +
        		"                ) need_adv_pay_style," +
        		"                name_last_input_telgr, name_last_output_telgr " +
        		"   FROM (SELECT ROWNUM rn, id_term_ses, id_term, sname_service_place, /*id_sam,*/  " +
        		"  			     date_beg_frmt, date_end_frmt, desc_term_ses,  " +
        		"  			     need_card_req, action_card_req, " +
        		"                need_col_data, need_tpar_data, need_term_mon, " +
        		"                need_online_pay, need_online_storno, need_club_pay, need_adv_pay, " +
        		"                name_last_input_telgr, name_last_output_telgr " +
        		"  			FROM (SELECT id_term_ses, id_term, sname_service_place, /*id_sam,*/ nt_sam_begin,  " +
        		"  						 TO_CHAR(date_beg,'dd.mm.rrrr hh24:mi:ss') date_beg_frmt,  " +
        		"  						 TO_CHAR(date_end,'dd.mm.rrrr hh24:mi:ss') date_end_frmt, desc_term_ses,  " +
        		"  						 need_card_req, action_card_req, need_col_data, need_tpar_data, " +
        		"                        need_term_mon, need_online_pay, need_online_storno, need_club_pay, need_adv_pay, " +
        		"                        name_last_input_telgr,  name_last_output_telgr " +
        		"                   FROM " + getGeneralDBScheme()+".vc_term_session_priv_all " +
        		"                  WHERE 1=1 ";
        
        ArrayList<bcFeautureParam> pParam = initParamArray();
        if (!isEmpty(pFind)) {
    		mySQL = mySQL + " AND (TO_CHAR(id_term_ses) LIKE UPPER('%'||?||'%') OR " +
    			"TO_CHAR(id_term) LIKE UPPER('%'||?||'%') OR " + 
    			"TO_CHAR(date_beg,'dd.mm.rrrr hh24:mi:ss') LIKE UPPER('%'||?||'%') OR " + 
    			"TO_CHAR(date_end,'dd.mm.rrrr hh24:mi:ss') LIKE UPPER('%'||?||'%') OR " + 
    			"UPPER(desc_term_ses) LIKE UPPER('%'||?||'%')) ";
    		for (int i=0; i<5; i++) {
    			pParam.add(new bcFeautureParam("string", pFind));
        	}
    	}
        
        if (!isEmpty(pDataType)) {
        	if ("TERM_CARD_REQ".equalsIgnoreCase(pDataType)) {
        		if (isEmpty(pDataState)) {
        			mySQL = mySQL + " AND NVL(need_card_req, 0) <> 0 ";
        		} else {
        			mySQL = mySQL + " AND NVL(need_card_req, 0) = ? ";
        			pParam.add(new bcFeautureParam("int", pDataState));
        		}
        	} else if ("TERM_CARD_CHECK".equalsIgnoreCase(pDataType)) {
        		mySQL = mySQL + " AND NVL(need_club_pay, 0) = 0 AND NVL(need_online_pay, 0) = 0 AND NVL(need_online_storno, 0) = 0 AND NVL(need_adv_pay, 0) = 0 ";
        		if (isEmpty(pDataState)) {
        			mySQL = mySQL + " AND NVL(need_card_req, 0) <> 0 ";
        		} else {
        			mySQL = mySQL + " AND NVL(need_card_req, 0) = ? ";
        			pParam.add(new bcFeautureParam("int", pDataState));
        		}
        	} else if ("TERM_CLUB_PAY".equalsIgnoreCase(pDataType)) {
        		if (isEmpty(pDataState)) {
        			mySQL = mySQL + " AND NVL(need_club_pay, 0) <> 0 ";
        		} else {
        			mySQL = mySQL + " AND NVL(need_club_pay, 0) = ? ";
        			pParam.add(new bcFeautureParam("int", pDataState));
        		}
        	} else if ("TERM_ONLINE_PAY".equalsIgnoreCase(pDataType)) {
        		if (isEmpty(pDataState)) {
        			mySQL = mySQL + " AND NVL(need_online_pay, 0) <> 0 ";
        		} else {
        			mySQL = mySQL + " AND NVL(need_online_pay, 0) = ? ";
        			pParam.add(new bcFeautureParam("int", pDataState));
        		}
        	} else if ("TERM_ADV_PAY".equalsIgnoreCase(pDataType)) {
        		if (isEmpty(pDataState)) {
        			mySQL = mySQL + " AND NVL(need_adv_pay, 0) <> 0 ";
        		} else {
        			mySQL = mySQL + " AND NVL(need_adv_pay, 0) = ? ";
        			pParam.add(new bcFeautureParam("int", pDataState));
        		}
        	} else if ("TERM_ONLINE_STORNO".equalsIgnoreCase(pDataType)) {
        		if (isEmpty(pDataState)) {
        			mySQL = mySQL + " AND NVL(need_online_storno, 0) <> 0 ";
        		} else {
        			mySQL = mySQL + " AND NVL(need_online_storno, 0) = ? ";
        			pParam.add(new bcFeautureParam("int", pDataState));
        		}
        	} else if ("TERM_COL_DATA".equalsIgnoreCase(pDataType)) {
        		if (isEmpty(pDataState)) {
        			mySQL = mySQL + " AND NVL(need_col_data, 0) <> 0 ";
        		} else {
        			mySQL = mySQL + " AND NVL(need_col_data, 0) = ? ";
        			pParam.add(new bcFeautureParam("int", pDataState));
        		}
        	} else if ("APP_TPAR_DATA".equalsIgnoreCase(pDataType)) {
        		if (isEmpty(pDataState)) {
        			mySQL = mySQL + " AND NVL(need_tpar_data, 0) <> 0 ";
        		} else {
        			mySQL = mySQL + " AND NVL(need_tpar_data, 0) = ? ";
        			pParam.add(new bcFeautureParam("int", pDataState));
        		}
        	} else if ("TERM_MON_REP".equalsIgnoreCase(pDataType)) {
        		if (isEmpty(pDataState)) {
        			mySQL = mySQL + " AND NVL(need_term_mon, 0) <> 0 ";
        		} else {
        			mySQL = mySQL + " AND NVL(need_term_mon, 0) = ? ";
        			pParam.add(new bcFeautureParam("int", pDataState));
        		}
        	}
        }
        pParam.add(new bcFeautureParam("int", p_end));
		pParam.add(new bcFeautureParam("int", p_beg));
    	mySQL = mySQL + 
        		"  				   ORDER BY id_term_ses desc ) " +
        		"		   WHERE ROWNUM < ?)" +
        		" WHERE rn >= ?) a," +
        		getGeneralDBScheme()+".vc_term_ses_creq_state_all b, " +
        		getGeneralDBScheme()+".vc_term_ses_data_state_all c, " +
        		getGeneralDBScheme()+".vc_term_ses_param_state_all d, " +
        		getGeneralDBScheme()+".vc_term_ses_mon_state_all e " +
                " WHERE a.need_card_req = b.cd_term_ses_creq_state " +
                "   AND a.need_col_data = c.cd_term_ses_data_state " +
        		"   AND a.need_tpar_data = d.cd_term_ses_param_state " +
        		"   AND a.need_term_mon = e.cd_term_ses_mon_state " +
        		"   ORDER BY rn";
    	
    	

        return getHeaderParamHTML(
        		mySQL,
        		pParam,
                0,
                "D",
            	0,
            	"",
            	"../crm/clients/termsespecs.jsp?id=:FIELD2:", 
            	term_sesXML,
            	pPrint);
    } //getTermSessionsHeadHTML()
    
    public String getTelegramsHeadHTML(String pFind, String cdTelgr, String telgrState, String p_beg, String p_end, String pPrint) {

    	ArrayList<bcFeautureParam> pParam = initParamArray();

    	String mySQL = 
        	" SELECT rn, id_telgr, id_term_ses, name_service_place, cd_telgr_type, date_telgr, nt_msg_b, tel_version, id_term, id_sam, " +
            " 		 nc, ni, way_telgr_tsl, name_telgr_state " +
            "   FROM (SELECT ROWNUM rn, id_telgr, id_term_ses, name_service_place, cd_telgr_type, nt_msg_b, tel_version, vk_enc, id_term, id_sam, " +
            " 		         nc, ni, date_telgr_frmt date_telgr, " +
            "                DECODE(way_telgr, " +
    		"                       'RECEIVED', '<font color=\"blue\"><b>" + telegramXML.getfieldTransl("way_telgr_received", false)+ "</b></font>', " +
    		"                       'SENT', '<font color=\"green\"><b>" + telegramXML.getfieldTransl("way_telgr_sent", false)+ "</b></font>', " +
    		"                       ''" +
    		"                ) way_telgr_tsl, " +
    		"                DECODE(cd_telgr_state, " +
    		"                       'EXECUTED', '<font color=\"green\"><b>'||name_telgr_state||'</b></font>', " +
    		"                       'IN_PROCESS', '<font color=\"blue\"><b>'||name_telgr_state||'</b></font>', " +
    		"                       'ERROR', '<font color=\"red\"><b>'||name_telgr_state||'</b></font>', " +
    		"                       'UNKNOWN', '<font color=\"red\"><b>'||name_telgr_state||'</b></font>', " +
    		"                       'WARNING', '<font color=\"red\"><b>'||name_telgr_state||'</b></font>', " +
    		"                       'FATAL_ERROR', '<font color=\"red\"><b>'||name_telgr_state||'</b></font>', " +
    		"                       'RETURNED', '<font color=\"brown\"><b>'||name_telgr_state||'</b></font>', " +
    		"                       'RETURNED_PARTIALLY', '<font color=\"brown\"><b>'||name_telgr_state||'</b></font>', " +
    		"                       'CANCEL', '<font color=\"brown\"><b>'||name_telgr_state||'</b></font>', " +
    		"                       '<font color=\"red\"><b>'||name_telgr_state||'</b></font>' " +
    		"                ) name_telgr_state " +
            "           FROM (SELECT * " +
            "                   FROM " + getGeneralDBScheme() + ".vc_telgr_club_all " +
        		"                  WHERE 1=1 ";
        if (!isEmpty(cdTelgr)) {
        	mySQL = mySQL + " AND cd_telgr_type = ? ";
        	pParam.add(new bcFeautureParam("string", cdTelgr));
        }
        if (!isEmpty(telgrState)) {
        	mySQL = mySQL + " AND cd_telgr_state= ? ";
        	pParam.add(new bcFeautureParam("string", telgrState));
        }
    	if (!isEmpty(pFind)) {
    		mySQL = mySQL + " AND (UPPER(cd_telgr_type) LIKE UPPER('%'||?||'%') OR " +
    			"UPPER(id_telgr) LIKE UPPER('%'||?||'%') OR " + 
    			"UPPER(outher_sid) LIKE UPPER('%'||?||'%') OR " + 
    			"UPPER(id_term) LIKE UPPER('%'||?||'%') OR " + 
    			"UPPER(id_sam) LIKE UPPER('%'||?||'%') OR " + 
    			"UPPER(date_telgr_frmt) LIKE UPPER('%'||?||'%')) ";
    		for (int i=0; i<6; i++) {
    		    pParam.add(new bcFeautureParam("string", pFind));
    		}
    	}
    	pParam.add(new bcFeautureParam("int", p_end));
    	pParam.add(new bcFeautureParam("int", p_beg));
        mySQL = mySQL + 
        		"  				   ORDER BY id_telgr desc ) " +
        		"		   WHERE ROWNUM < ?) " +
        		"  WHERE rn >= ?";

        return getHeaderParamHTML(
        		mySQL,
        		pParam,
                0,
                "D",
            	0,
            	"",
            	"../crm/clients/telegramspecs.jsp?id=:FIELD2:", 
            	telegramXML,
            	pPrint);
    } //getTermSessionsHeadHTML()
    
    public String getJurpersonsHeadHTML(String pJurPrsStatus, String pClubMemberStatus, String pClubMemberType, String pFind, String p_beg, String p_end, String pPrint) {

    	ArrayList<bcFeautureParam> pParam = initParamArray();

    	String mySQL = 
    		" SELECT rn, /*name_jur_prs_kind,*/ ";
    	if ("SERVICE_PLACE".equalsIgnoreCase(pJurPrsStatus)) {
    		mySQL = mySQL +
				"        '<b><font color=\"blue\">'||sname_jur_prs_parent||'</font></b>' title_partner, name_jur_prs title_service_place, ";
    	} else {
    		mySQL = mySQL +
    			"        '<span>'||LPAD(' ', ((level_jur_prs-1)*4*6), '&nbsp;')||name_jur_prs||'</span>' " + ("ALL".equalsIgnoreCase(pJurPrsStatus)?"name_jur_prs":"title_partner") + ", ";
    	}
    	mySQL = mySQL +
    		"        fact_adr_full, sname_club title_club, club_date_beg, " +
    		"        name_club_member_status club_member_status, " +
    		"        id_jur_prs, name_jur_prs_initial " +
        	"	FROM (SELECT ROWNUM rn, id_jur_prs, sname_jur_prs_parent, name_jur_prs_kind, cd_jur_prs_status, " +
        	"                CASE WHEN cd_jur_prs_status = 'PARTNER' " +
        	"                     THEN '<b><font color=\"blue\" title=\"" + jurpersonXML.getfieldTransl("title_partner", false) + "\">'||name_jur_prs||'</font></b>'" +
        	"                     ELSE '<font color=\"green\" title=\"" + jurpersonXML.getfieldTransl("title_service_place", false) + "\">'||name_jur_prs||'</font>'" +
        	"                END name_jur_prs, " +
        	"                fact_adr_full, sname_club, club_date_beg, " +
        	"                DECODE(cd_club_member_status, " +
       		"                     	'CLOSED', '<font color=\"red\"><b>'||name_club_member_status||'</b></font>', " +
       		"                     	'OUT_OF_CLUB', '<font color=\"gray\">'||name_club_member_status||'</font>', " +
       		"                     	'IN_PROCESS', '<font color=\"blue\">'||name_club_member_status||'</font>', " +
       		"                     	'PARTICIPATE', '<font color=\"green\">'||name_club_member_status||'</font>', " +
       		"                     	name_club_member_status" +
       		"                ) name_club_member_status," +
       		"				 level_jur_prs, name_jur_prs name_jur_prs_initial" +
        	"	        FROM (SELECT id_jur_prs, sname_jur_prs_parent, name_jur_prs_kind, " +
        	"                        id_jur_prs_parent, cd_jur_prs_status, name_jur_prs, fact_adr_full, sname_club, date_beg_frmt club_date_beg, " +
        	"                        cd_club_member_status, name_club_member_status, level level_jur_prs " +
        	"                   FROM " + getGeneralDBScheme()+".vc_jur_prs_priv_all a" +
        	"                  WHERE 1 = 1 ";
    	if (!isEmpty(pClubMemberType)) {
        	if ("DEALER".equalsIgnoreCase(pClubMemberType)) {
            	mySQL = mySQL + " AND NVL(is_dealer, 'N') = 'Y' ";
        	} else if ("BANK".equalsIgnoreCase(pClubMemberType)) {
        		mySQL = mySQL + " AND NVL(is_bank, 'N') = 'Y' ";
        	} else if ("OPERATOR".equalsIgnoreCase(pClubMemberType)) {
        		mySQL = mySQL + " AND NVL(is_operator, 'N') = 'Y' ";
        	} else if ("PARTNER".equalsIgnoreCase(pClubMemberType)) {
        		mySQL = mySQL + " AND NVL(is_partner, 'N') = 'Y' ";
        	} else if ("TERMINAL_MANUFACTURER".equalsIgnoreCase(pClubMemberType)) {
        		mySQL = mySQL + " AND NVL(is_terminal_manufacturer, 'N') = 'Y' ";
        	} else if ("ISSUER".equalsIgnoreCase(pClubMemberType)) {
        		mySQL = mySQL + " AND NVL(is_issuer, 'N') = 'Y' ";
        	} else if ("FINANCE_ACQUIRER".equalsIgnoreCase(pClubMemberType)) {
        		mySQL = mySQL + " AND NVL(is_finance_acquirer, 'N') = 'Y' ";
        	} else if ("TECHNICAL_ACQUIRER".equalsIgnoreCase(pClubMemberType)) {
        		mySQL = mySQL + " AND NVL(is_technical_acquirer, 'N') = 'Y' ";
        	} else if ("AGENT".equalsIgnoreCase(pClubMemberType)) {
        		mySQL = mySQL + " AND NVL(is_agent, 'N') = 'Y' ";
        	} else if ("SHAREHOLDER".equalsIgnoreCase(pClubMemberType)) {
        		mySQL = mySQL + " AND NVL(is_shareholder, 'N') = 'Y' ";
        	} else if ("REGISTRATOR".equalsIgnoreCase(pClubMemberType)) {
        		mySQL = mySQL + " AND NVL(is_registrator, 'N') = 'Y' ";
        	} else if ("COORDINATOR".equalsIgnoreCase(pClubMemberType)) {
        		mySQL = mySQL + " AND NVL(is_coordinator, 'N') = 'Y' ";
        	} else if ("CURATOR".equalsIgnoreCase(pClubMemberType)) {
        		mySQL = mySQL + " AND NVL(is_curator, 'N') = 'Y' ";
        	} else if ("SERVICE_PLACE".equalsIgnoreCase(pClubMemberType)) {
        		mySQL = mySQL + " AND cd_jur_prs_status = 'SERVICE_PLACE' ";
        	}
        }
    	if (!isEmpty(pFind)) {
    		mySQL = mySQL + " AND (UPPER(id_jur_prs) LIKE UPPER('%'||?||'%') OR " +
    			"UPPER(name_jur_prs_kind) LIKE UPPER('%'||?||'%') OR " +
    			"UPPER(sname_jur_prs) LIKE UPPER('%'||?||'%') OR " + 
    			"UPPER(fact_adr_full) LIKE UPPER('%'||?||'%') OR " + 
    			"UPPER(date_beg_frmt) LIKE UPPER('%'||?||'%')) ";
    		for (int i=0; i<5; i++) {
    		    pParam.add(new bcFeautureParam("string", pFind));
    		}
    	}
    	if (!isEmpty(pClubMemberStatus)) {
    		mySQL = mySQL + " AND cd_club_member_status = ? ";
    		pParam.add(new bcFeautureParam("string", pClubMemberStatus));
    	}
    	if (!isEmpty(pJurPrsStatus) && !"ALL".equalsIgnoreCase(pJurPrsStatus)) {
    		mySQL = mySQL + " AND cd_jur_prs_status = ? ";
    		pParam.add(new bcFeautureParam("string", pJurPrsStatus));
    	}
    	pParam.add(new bcFeautureParam("int", p_end));
    	pParam.add(new bcFeautureParam("int", p_beg));
    	mySQL = mySQL + 
    		" START WITH id_jur_prs_parent IS NULL " +
    		" CONNECT BY PRIOR id_jur_prs = id_jur_prs_parent " +
    		" ORDER siblings BY name_jur_prs) WHERE ROWNUM < ?) WHERE rn >= ? ";
    	
    	String linkField = ":FIELD7:";
    	if ("SERVICE_PLACE".equalsIgnoreCase(pJurPrsStatus)) {
    		linkField = ":FIELD8:";
    	}
    	return getHeaderParamHTML(
        		mySQL,
        		pParam,
                2,
                "D",
            	0,
            	"",
            	"../crm/clients/yurpersonspecs.jsp?id=" + linkField + "&adr=full", 
            	jurpersonXML,
            	pPrint);
    }
    
    public String getAutoreconcilationHeadHTML(String pFind, String p_beg, String p_end, String pPrint) {

    	ArrayList<bcFeautureParam> pParam = initParamArray();

    	String mySQL = 
    		" SELECT rn, id_autoreconcilation, date_autoreconcilation, " +
    		"        reconciled_lines_count " +
        	"	FROM (SELECT ROWNUM rn, id_autoreconcilation, date_autoreconcil_full_frmt date_autoreconcilation, " +
        	"                reconciled_lines_count " +
        	"	        FROM (SELECT *" +
        	"                   FROM " + getGeneralDBScheme()+".v_autoreconcilation ";

    	if (!isEmpty(pFind)) {
    		mySQL = mySQL + " WHERE (UPPER(date_autoreconcil_full_frmt) LIKE UPPER('%'||?||'%') OR " +
    			"UPPER(TO_CHAR(reconciled_lines_count)) LIKE UPPER('%'||?||'%')) ";
    		for (int i=0; i<2; i++) {
    		    pParam.add(new bcFeautureParam("string", pFind));
    		}
    	}
    	pParam.add(new bcFeautureParam("int", p_end));
    	pParam.add(new bcFeautureParam("int", p_beg));
    	mySQL = mySQL +
    	    "                  ORDER BY date_autoreconcilation DESC) " +
    	    "          WHERE ROWNUM < ?) " +
    	    "  WHERE rn >= ? ";
    	
    	return getHeaderParamHTML(
        		mySQL,
        		pParam,
                0,
                "D",
            	0,
            	"",
            	"../crm/finance/autoreconcilspecs.jsp?id=:FIELD2:", 
            	kvitovkaXML,
            	pPrint);
    }

    public String getCallCenterCallGroupHeadHTML(String pCallGroupState, String pFind, String p_beg, String p_end, String pPrint) {

    	ArrayList<bcFeautureParam> pParam = initParamArray();

    	String mySQL = 
    		" SELECT rn, name_cc_call_group, desc_cc_call_group, " +
        	"        name_cc_call_group_state, date_cc_call_group_frmt, clients_count, name_cc_inquirer, id_cc_call_group " +
        	"	FROM (SELECT ROWNUM rn, id_cc_call_group, name_cc_call_group, desc_cc_call_group, " +
        	"                DECODE(cd_cc_call_group_state, " +
			"                  		'NEW', '<b><font color=\"black\">'||name_cc_call_group_state||'</font></b>', " +
			"                  		'IN_PROCESS', '<b><font color=\"blue\">'||name_cc_call_group_state||'</font></b>', " +
			"                  		'EXECUTED', '<b><font color=\"green\">'||name_cc_call_group_state||'</font></b>', " +
			"                  		'CANCEL', '<b><font color=\"red\">'||name_cc_call_group_state||'</font></b>', " +
			"                  		name_cc_call_group_state" +
			"                ) name_cc_call_group_state," +
        	"                date_cc_call_group_frmt, clients_count, name_cc_inquirer " +
        	"	        FROM (SELECT * " +
        	"                   FROM " + getGeneralDBScheme()+".vc_cc_call_group_club_all " +
        	"                  WHERE 1=1 ";
    	if (!isEmpty(pFind)) {
    		mySQL = mySQL + 
    			" AND (UPPER(name_cc_call_group) LIKE UPPER('%'||?||'%') " +
    			"    OR UPPER(desc_cc_call_group) LIKE UPPER('%'||?||'%') " +
    			"    OR UPPER(name_cc_inquirer) LIKE UPPER('%'||?||'%')) ";
    		for (int i=0; i<3; i++) {
    		    pParam.add(new bcFeautureParam("string", pFind));
    		}
    	}
        if (!isEmpty(pCallGroupState)) {
        	mySQL = mySQL + " AND cd_cc_call_group_state = ? ";
        	pParam.add(new bcFeautureParam("string", pCallGroupState));
        }

        pParam.add(new bcFeautureParam("int", p_end));
        pParam.add(new bcFeautureParam("int", p_beg));
    	mySQL = mySQL +
        	"                 ORDER BY date_cc_call_group DESC, name_cc_call_group) " +
        	"          WHERE ROWNUM < ? " + 
        	"        ) " +
        	"  WHERE rn >= ? ";
    	return getHeaderParamHTML(
        		mySQL,
        		pParam,
                1,
                "D",
            	0,
            	"",
            	"../crm/call_center/call_groupspecs.jsp?id=:FIELD8:", 
            	call_centerXML,
            	pPrint);
    }

    public String getCallCenterInquirerHeadHTML(String pInquirerState, String pFind, String p_beg, String p_end, String pPrint) {

    	ArrayList<bcFeautureParam> pParam = initParamArray();

    	String mySQL = 
    		" SELECT rn, name_cc_inquirer, desc_cc_inquirer, " +
        	"        name_cc_inquirer_state, date_cc_inquirer_frmt, id_cc_inquirer " +
        	"	FROM (SELECT ROWNUM rn, id_cc_inquirer, name_cc_inquirer, desc_cc_inquirer, " +
        	"                DECODE(cd_cc_inquirer_state, " +
			"                  		'CONSTUCTION', '<b><font color=\"blue\">'||name_cc_inquirer_state||'</font></b>', " +
			"                  		'APPROVE', '<b><font color=\"green\">'||name_cc_inquirer_state||'</font></b>', " +
			"                  		'CANCEL', '<b><font color=\"red\">'||name_cc_inquirer_state||'</font></b>', " +
			"                  		name_cc_inquirer_state" +
			"                ) name_cc_inquirer_state," +
        	"                date_cc_inquirer_frmt " +
        	"	        FROM (SELECT * " +
        	"                   FROM " + getGeneralDBScheme()+".vc_cc_inquirer_club_all " +
        	"                  WHERE 1=1 ";
    	if (!isEmpty(pFind)) {
    		mySQL = mySQL + 
    			" AND (UPPER(name_cc_inquirer) LIKE UPPER('%'||?||'%') " +
    			"    OR UPPER(desc_cc_inquirer) LIKE UPPER('%'||?||'%')) ";
    		for (int i=0; i<2; i++) {
    		    pParam.add(new bcFeautureParam("string", pFind));
    		}
    	}
        if (!isEmpty(pInquirerState)) {
        	mySQL = mySQL + " AND cd_cc_inquirer_state = ? ";
        	pParam.add(new bcFeautureParam("string", pInquirerState));
        }
        pParam.add(new bcFeautureParam("int", p_end));
        pParam.add(new bcFeautureParam("int", p_beg));
    	mySQL = mySQL +
        	"                 ORDER BY date_cc_inquirer DESC, name_cc_inquirer) " +
        	"          WHERE ROWNUM < ? " + 
        	"        ) " +
        	"  WHERE rn >= ?";
    	return getHeaderParamHTML(
        		mySQL,
        		pParam,
                1,
                "D",
            	0,
            	"",
            	"../crm/call_center/inquirerspecs.jsp?id=:FIELD6:", 
            	call_centerXML,
            	pPrint);
    }

    public String getCallCenterFAQHeadHTML(String pIdCategory, String pFind, String p_beg, String p_end, String pPrint) {
    	
    	ArrayList<bcFeautureParam> pParam = initParamArray();

    	String idClubField = this.getIdClubFieldString("FIRST");
    	
    	String mySQL = 
    		" SELECT rn, id_cc_faq, cd_cc_faq, title_cc_faq, name_cc_faq_category, exist_flag_tsl " + idClubField + 
        	"	FROM (SELECT ROWNUM rn, id_cc_faq, cd_cc_faq, title_cc_faq, name_cc_faq_category, " +
        	"  			     DECODE(exist_flag, " +
			"                  		'Y', '<b><font color=\"green\">'||exist_flag_tsl||'</font></b>', " +
			"                   	'<b><font color=\"red\">'||exist_flag_tsl||'</font></b>' " +
			"                ) exist_flag_tsl " +
			"				 " + idClubField + 
        	"	        FROM (SELECT id_cc_faq, cd_cc_faq, title_cc_faq, name_cc_faq_category," +
        	"                        exist_flag, exist_flag_tsl " + idClubField + 
        	"                   FROM " + getGeneralDBScheme()+".vc_cc_faq_club_all " +
        	"                  WHERE 1 = 1 ";
    	if (!isEmpty(pIdCategory)) {
    		mySQL = mySQL + " AND id_cc_faq_category = ? ";
    		pParam.add(new bcFeautureParam("int", pIdCategory));
    	}
    	if (!isEmpty(pFind)) {
    		mySQL = mySQL + " AND (UPPER(cd_cc_faq) LIKE UPPER('%'||?||'%') " +
				"    OR UPPER(title_cc_faq) LIKE UPPER('%'||?||'%') " +
				"    OR UPPER(question_cc_faq) LIKE UPPER('%'||?||'%') " +
				"    OR UPPER(answer_cc_faq) LIKE UPPER('%'||?||'%')) ";
    		for (int i=0; i<4; i++) {
    		    pParam.add(new bcFeautureParam("string", pFind));
    		}
    	}
    	pParam.add(new bcFeautureParam("int", p_end));
    	pParam.add(new bcFeautureParam("int", p_beg));
    	mySQL = mySQL +
        	"                 ORDER BY name_cc_faq_category, title_cc_faq) " +
        	"          WHERE ROWNUM < ?" + 
        	"        ) " +
        	"  WHERE rn >= ?";
    	return getHeaderParamHTML(
        		mySQL,
        		pParam,
                0,
                "D",
            	0,
            	"",
            	"../crm/call_center/faqspecs.jsp?id=:FIELD2:", 
            	call_centerXML,
            	pPrint);
    }

    public String getCallCenterSettingsHeadHTML(String pCDSettingState, String pFind, String p_beg, String p_end, String pPrint) {
    	
    	ArrayList<bcFeautureParam> pParam = initParamArray();

    	String mySQL = 
    		" SELECT rn, name_cc_setting, desc_cc_setting, name_cc_setting_state, send_email_tsl, id_cc_setting " + 
        	"	FROM (SELECT ROWNUM rn, id_cc_setting, name_cc_setting, desc_cc_setting, name_cc_setting_state, send_email_tsl " +  
        	"	        FROM (SELECT id_cc_setting, name_cc_setting, desc_cc_setting, " +
        	"         		         DECODE(cd_cc_setting_state, " +
            "               	 	        'ENABLE', '<font color=\"green\"><b>'||name_cc_setting_state||'</b></font>', " +
            "               		        'DISABLE', '<font color=\"red\"><b>'||name_cc_setting_state||'</b></font>', " +
            "               		        name_cc_setting_state" +
            "        		         ) name_cc_setting_state, " +
        	"         		         DECODE(send_email, " +
            "               	 	        'Y', '<font color=\"green\"><b>'||send_email_tsl||'</b></font>', " +
            "               		        'N', '<font color=\"red\"><b>'||send_email_tsl||'</b></font>', " +
            "               		        send_email_tsl" +
            "        		         ) send_email_tsl " +  
        	"                   FROM " + getGeneralDBScheme()+".vc_cc_setting_all " +
        	"                  WHERE 1 = 1 ";
    	if (!isEmpty(pCDSettingState)) {
    		mySQL = mySQL + " AND cd_cc_setting_state = ? ";
    		pParam.add(new bcFeautureParam("string", pCDSettingState));
    	}
    	if (!isEmpty(pFind)) {
    		mySQL = mySQL + " AND (UPPER(id_cc_setting) LIKE UPPER('%'||?||'%') " +
				"    OR UPPER(name_cc_setting) LIKE UPPER('%'||?||'%') " +
				"    OR UPPER(desc_cc_setting) LIKE UPPER('%'||?||'%')) ";
    		for (int i=0; i<3; i++) {
    		    pParam.add(new bcFeautureParam("string", pFind));
    		}
    	}
    	pParam.add(new bcFeautureParam("int", p_end));
    	pParam.add(new bcFeautureParam("int", p_beg));
    	mySQL = mySQL +
        	"                 ORDER BY DECODE(id_cc_setting, 0, 1, 2), name_cc_setting) " +
        	"          WHERE ROWNUM < ?" + 
        	"        ) " +
        	"  WHERE rn >= ?";
    	return getHeaderParamHTML(
        		mySQL,
        		pParam,
                1,
                "D",
            	0,
            	"",
            	"../crm/call_center/settingspecs.jsp?id=:FIELD6:", 
            	call_centerXML,
            	pPrint);
    }

    public String getCallCenterQuestionsHeadHTML(String pFind, String pContactType, String pStatus, String p_beg, String p_end, String pPrint) {

    	ArrayList<bcFeautureParam> pParam = initParamArray();

    	String mySQL = 
    		" SELECT a.rn, " +
    		"        DECODE(a.cd_cc_contact_type, " +
			"          		'PHONE', '<b><font color=\"blue\">'||c.name_cc_contact_type||'</font></b>', " +
			"          		'CALL_GROUP', '<b><font color=\"green\">'||c.name_cc_contact_type||'</font></b>', " +
			"          		'EMAIL', '<b><font color=\"green\">'||c.name_cc_contact_type||'</font></b>', " +
			"          		'SITE', '<b><font color=\"gray\">'||c.name_cc_contact_type||'</font></b>', " +
			"          		'PERSONAL_CABINET', '<b><font color=\"red\">'||c.name_cc_contact_type||'</font></b>', " +
			"          		c.name_cc_contact_type" +
			"        ) cc_contact_type, " +
        	"        a.cc_question_status, " +
    		"        a.cd_card1, a.fio_nat_prs, a.phone_mobile, " +
        	"        a.title, a.due_date_frmt, a.id_cc_question " +
        	"	FROM (SELECT rn, id_cc_question, cd_cc_contact_type, cd_card1, fio_nat_prs, phone_mobile, " +
        	"        title, due_date_frmt, cc_question_status " +
        	"	FROM (SELECT ROWNUM rn, id_cc_question, cd_cc_contact_type, cd_card1, fio_nat_prs, phone_mobile," +
        	"                title, due_date_frmt, " +
        	"                DECODE(cd_cc_question_status, " +
			"                  		'BEGUN', '<b><font color=\"black\">'||name_cc_question_status||'</font></b>', " +
			"                  		'IN_PROCESS', '<b><font color=\"blue\">'||name_cc_question_status||'</font></b>', " +
			"                  		'FINISHED', '<b><font color=\"green\">'||name_cc_question_status||'</font></b>', " +
			"                  		'CANCEL', '<b><font color=\"red\">'||name_cc_question_status||'</font></b>', " +
			"                  		'POSTPONED', '<b><font color=\"red\">'||name_cc_question_status||'</font></b>', " +
			"                  		name_cc_question_status" +
			"                ) cc_question_status" +
        	"	        FROM (SELECT * " +
        	"                   FROM " + getGeneralDBScheme()+".vc_cc_questions2_all " +
        	"                  WHERE 1=1";
    	if (!isEmpty(pFind)) {
    		mySQL = mySQL + 
    			" AND (UPPER(title) LIKE UPPER('%'||?||'%') " +
				"  OR UPPER(cd_card1) LIKE UPPER('%'||?||'%') " +
				"  OR UPPER(fio_nat_prs) LIKE UPPER('%'||?||'%') " +
				"  OR UPPER(phone_mobile) LIKE UPPER('%'||?||'%') " +
				"  OR UPPER(due_date_frmt) LIKE UPPER('%'||?||'%')) ";
    		for (int i=0; i<5; i++) {
    		    pParam.add(new bcFeautureParam("string", pFind));
    		}
    	}
    	if (!isEmpty(pContactType)) {
    		mySQL = mySQL + " AND cd_cc_contact_type = ? ";
    		pParam.add(new bcFeautureParam("string", pContactType));
    	}
    	if (!isEmpty(pStatus)) {
    		mySQL = mySQL + " AND cd_cc_question_status = ? ";
    		pParam.add(new bcFeautureParam("string", pStatus));
    	}
    	pParam.add(new bcFeautureParam("int", p_end));
    	pParam.add(new bcFeautureParam("int", p_beg));
    	mySQL = mySQL +
        	"                 ORDER BY due_date DESC, fio_nat_prs) " +
        	"          WHERE ROWNUM < ?" + 
        	"        ) " +
        	"  WHERE rn >= ?) a, " + getGeneralDBScheme()+".vc_cc_contact_type_all c" +
        	" WHERE a.cd_cc_contact_type = c.cd_cc_contact_type";
    	return getHeaderParamHTML(
        		mySQL,
        		pParam,
                1,
                "D",
            	0,
            	"",
            	"../crm/call_center/questionspecs.jsp?id=:FIELD9:", 
            	call_centerXML,
            	pPrint);
    }

    public String getCallCenterUsersHeadHTML(String pIdUserStatus, String pFind, String p_beg, String p_end, String pPrint) {

    	ArrayList<bcFeautureParam> pParam = initParamArray();

    	String mySQL = 
    		" SELECT rn, name_user, desc_user, name_module_type, name_cc_user_status, id_user " +
        	"	FROM (SELECT ROWNUM rn, id_user, name_user, desc_user, name_module_type, name_cc_user_status " +
        	"	        FROM (SELECT id_user, name_user, desc_user, " +
        	"         		         DECODE(cd_module_type, " +
            "               	 	        'WEBCLIENT', '<font color=\"blue\"><b>'||name_module_type||'</b></font>', " +
            "               	 	        'WEBPOS', '<font color=\"blue\"><b>'||name_module_type||'</b></font>', " +
            "               		        'CRM', '<font color=\"red\"><b>'||name_module_type||'</b></font>', " +
            "               		        'SYSTEM', '<font color=\"red\"><b>'||name_module_type||'</b></font>', " +
            "               		        'PRIVATE_OFFICE', '<font color=\"green\"><b>'||name_module_type||'</b></font>', " +
            "               		        name_module_type" +
            "        		         ) name_module_type, " +
        	"         		         DECODE(cd_cc_user_status, " +
            "               	 	        'ADMINISTRATOR', '<font color=\"red\"><b>'||name_cc_user_status||'</b></font>', " +
            "               	 	        'USER', '<font color=\"green\"><b>'||name_cc_user_status||'</b></font>', " +
            "               		        'MANAGER', '<font color=\"blue\"><b>'||name_cc_user_status||'</b></font>', " +
            "               		        name_cc_user_status" +
            "        		         ) name_cc_user_status " +
        	"                   FROM " + getGeneralDBScheme()+".vc_cc_users_all " +
        	"                  WHERE 1=1 ";
    	if (!isEmpty(pIdUserStatus)) {
    		mySQL = mySQL + " AND cd_cc_user_status = ? ";
    		pParam.add(new bcFeautureParam("string", pIdUserStatus));
    	}
        if (!isEmpty(pFind)) {
    		mySQL = mySQL + 
    			" AND (UPPER(TO_CHAR(id_user)) LIKE UPPER('%'||?||'%') OR " +
    			"	     UPPER(name_user) LIKE UPPER('%'||?||'%') OR " +
    			"	     UPPER(desc_user) LIKE UPPER('%'||?||'%') OR " +
    			"	     UPPER(name_module_type) LIKE UPPER('%'||?||'%')) ";
    		for (int i=0; i<4; i++) {
    		    pParam.add(new bcFeautureParam("string", pFind));
    		}
    	}
        pParam.add(new bcFeautureParam("int", p_end));
        pParam.add(new bcFeautureParam("int", p_beg));
    	mySQL = mySQL +
        	"                 ORDER BY name_user) " +
        	"          WHERE ROWNUM < ?" + 
        	"        ) " +
        	"  WHERE rn >= ?";
    	return getHeaderParamHTML(
        		mySQL,
        		pParam,
                1,
                "D",
            	0,
            	"",
            	"../crm/call_center/administrationspecs.jsp?id=:FIELD6:", 
            	call_centerXML,
            	pPrint);
    }

    public String getTermDeviceTypeHeadHTML(String pTermType, String pFind, String p_beg, String p_end, String pPrint) {

    	ArrayList<bcFeautureParam> pParam = initParamArray();

    	String mySQL = 
    		" SELECT rn, name_device_type, desc_device_type, " +
        	"        name_term_type, terminal_manufacturer, work_with_certificate_tsl, /*exist_flag_tsl,*/ id_device_type " +
        	"   FROM (SELECT ROWNUM rn, id_device_type, name_device_type, desc_device_type, " +
        	"                name_term_type, sname_jur_prs terminal_manufacturer, " +
        	"	     		 DECODE(exist_flag, " +
			"          				'Y', '<b><font color=\"green\">'||exist_flag_tsl||'</font></b>', " +
			"              			'<b><font color=\"red\">'||exist_flag_tsl||'</font></b>' " +
			"          		 ) exist_flag_tsl, " +
        	"				 DECODE(work_with_certificate, " +
			"          				'Y', '<b><font color=\"green\">'||work_with_certificate_tsl||'</font></b>', " +
			"              			'<b><font color=\"red\">'||work_with_certificate_tsl||'</font></b>' " +
			"          		 ) work_with_certificate_tsl " +
        	"           FROM (SELECT * " +
        	"                   FROM " + getGeneralDBScheme() + ".vc_term_device_type_all "+
        	"                  WHERE 1=1 ";

    	if (!isEmpty(pTermType)) {
        	mySQL = mySQL + " AND cd_term_type = ? ";
        	pParam.add(new bcFeautureParam("string", pTermType));
        }
    	if (!isEmpty(pFind)) {
    		mySQL = mySQL + " AND (UPPER(id_device_type) LIKE UPPER('%'||?||'%') OR " +
    			"UPPER(name_device_type) LIKE UPPER('%'||?||'%') OR " +
    			"UPPER(desc_device_type) LIKE UPPER('%'||?||'%') OR " +
    			"UPPER(terminal_manufacturer) LIKE UPPER('%'||?||'%')) ";
    		for (int i=0; i<4; i++) {
    		    pParam.add(new bcFeautureParam("string", pFind));
    		}
    	}
    	pParam.add(new bcFeautureParam("int", p_end));
    	pParam.add(new bcFeautureParam("int", p_beg));
    	mySQL = mySQL +
        	"                  ORDER BY sname_jur_prs) " +
       	    "          WHERE ROWNUM < ?) " + 
       	    "  WHERE rn >= ?";
    	
    	return getHeaderParamHTML(
        		mySQL,
        		pParam,
                1,
                "D",
            	0,
            	"",
            	"../crm/club/term_device_typespecs.jsp?id=:FIELD7:", 
            	terminalXML,
            	pPrint);
    }

    public String getDocumentsHeadHTML(String pDocType, String pDocState, String pClubRelType, String pFind, String p_beg, String p_end, String pPrint) {

    	ArrayList<bcFeautureParam> pParam = initParamArray();
    	
    	String mySQL = 
        	" SELECT rn, number_doc, date_doc_frmt, name_doc, /*full_doc,*/ " +
        	"        parties_doc, name_doc_type, club_rel_type, name_doc_state, id_doc "+
            "   FROM (SELECT ROWNUM rn, id_doc, name_doc_type, " +
            "                DECODE(cd_doc_state, " +
            "               		'SIGNED', '<font color=\"green\"><b>'||name_doc_state||'</b></font>', " +
            "               		'WORKING_OUT', '<font color=\"blue\"><b>'||name_doc_state||'</b></font>', " +
            "               		'FINISCHED', '<font color=\"red\">'||name_doc_state||'</font>', " +
            "               		name_doc_state" +
            "        		 ) name_doc_state, " +
            "				 number_doc, date_doc_frmt, name_doc, /*full_doc,*/ parties_doc," +
            "                name_club_rel_type club_rel_type "+
        	"			FROM (SELECT *" +
        	"                   FROM " + getGeneralDBScheme()+".vc_doc_priv_all " +
        	"				   WHERE 1=1 ";
    	if (!isEmpty(pDocType)) {
    		mySQL = mySQL + " AND cd_doc_type = ? ";
    		pParam.add(new bcFeautureParam("string", pDocType));
    	}
    	if (!isEmpty(pDocState)) {
    		mySQL = mySQL + " AND cd_doc_state = ? ";
    		pParam.add(new bcFeautureParam("string", pDocState));
    	}
    	if (!isEmpty(pClubRelType)) {
    		mySQL = mySQL + " AND cd_club_rel_type = ?";
        	pParam.add(new bcFeautureParam("string", pClubRelType));
    	}
    	if (!isEmpty(pFind)) {
    		mySQL = mySQL + 
    			" AND (TO_CHAR(id_doc) like UPPER('%'||?||'%') OR " +
    			"      UPPER(full_doc) like UPPER('%'||?||'%') OR " +
    			"      UPPER(sname_jur_prs_party1) like UPPER('%'||?||'%') OR " +
    			"      UPPER(sname_jur_prs_party2) like UPPER('%'||?||'%') OR " +
    			"      UPPER(sname_jur_prs_party3) like UPPER('%'||?||'%') " +
    			") ";
    		for (int i=0; i<5; i++) {
    		    pParam.add(new bcFeautureParam("string", pFind));
    		}
    	}
    	pParam.add(new bcFeautureParam("int", p_end));
    	pParam.add(new bcFeautureParam("int", p_beg));
    	mySQL = mySQL +
        	"                  ORDER BY number_doc )" + 
        	"          WHERE ROWNUM < ?) " + 
        	"  WHERE rn >= ?";
    	return getHeaderParamHTML(
        		mySQL,
        		pParam,
                1,
                "D",
            	0,
            	"",
            	"../crm/club/documentspecs.jsp?id=:FIELD9:", 
            	documentXML,
            	pPrint);
    }

    public String getDocumentsNeedHeadHTML(String pDocType, String pFind, String p_beg, String p_end, String pPrint) {

    	ArrayList<bcFeautureParam> pParam = initParamArray();

    	String mySQL = 
        	" SELECT rn, name_doc_type, id_club_rel, name_club_rel_type, date_club_rel, " +
        	"        sname_party1_full, sname_party2_full, id_doc "+
            "   FROM (SELECT ROWNUM rn, id_club_rel, name_club_rel_type, date_club_rel_frmt date_club_rel, " +
            "                sname_party1 sname_party1_full, sname_party2 sname_party2_full, name_doc_type, id_doc "+
        	"			FROM (SELECT *" +
        	"                   FROM " + getGeneralDBScheme()+".vc_doc_club_rel_need_club_all " +
        	"				   WHERE 1=1 ";
    	if (!isEmpty(pDocType)) {
    		mySQL = mySQL + " AND cd_doc_type = ? ";
    		pParam.add(new bcFeautureParam("string", pDocType));
    	}
    	if (!isEmpty(pFind)) {
    		mySQL = mySQL + 
    			" AND (UPPER(id_club_rel) like UPPER('%'||?||'%') OR " +
    			"  UPPER(name_club_rel_type) like UPPER('%'||?||'%') OR " +
    			"  UPPER(date_club_rel_frmt) like UPPER('%'||?||'%') OR " +
    			"  UPPER(sname_party1) like UPPER('%'||?||'%') OR " +
    			"  UPPER(sname_party2) like UPPER('%'||?||'%') " +
    			") ";
    		for (int i=0; i<5; i++) {
    		    pParam.add(new bcFeautureParam("string", pFind));
    		}
    	}
    	pParam.add(new bcFeautureParam("int", p_end));
    	pParam.add(new bcFeautureParam("int", p_beg));
    	mySQL = mySQL +
        	"                  ORDER BY name_doc_type, name_club_rel_type, sname_party1, sname_party2 )" + 
        	"          WHERE ROWNUM < ?) " + 
        	"  WHERE rn >= ?";
    	return getHeaderParamHTML(
        		mySQL,
        		pParam,
                1,
                "D",
            	0,
            	"",
            	"../crm/club/documentupdate.jsp?type=general&action=addneeded&process=no&id_doc_needed=:FIELD8:", 
            	relationshipXML,
            	pPrint);
    }

    public String getClubComissionTypeHTML(String pClubRelType, String pFind, String p_beg, String p_end, String pPrint) {

    	ArrayList<bcFeautureParam> pParam = initParamArray();

    	String mySQL = 
        	" SELECT rn, name_comission_type, name_club_rel_type, " +
        	"        name_participant_payer jur_prs_payer, " +
        	"        name_participant_receiver jur_prs_receiver, " +
        	"        name_payment_system, fixed_value_frmt fixed_value_def_frmt, " +
        	"        percent_value_frmt percent_value_def_frmt, exist_flag_tsl, " +
        	"        cd_comission_type, id_comission_type " +
            "   FROM (SELECT ROWNUM rn, id_comission_type, cd_comission_type, name_comission_type,  " +
            "                name_club_rel_type, name_participant_payer, " +
            "                name_participant_receiver, name_payment_system, " +
            "                fixed_value_frmt, percent_value_frmt, " +
            "	     		 DECODE(exist_flag, " +
			"          				'Y', '<b><font color=\"green\">'||exist_flag_tsl||'</font></b>', " +
			"              			'<b><font color=\"red\">'||exist_flag_tsl||'</font></b>' " +
			"          		 ) exist_flag_tsl "+
        	"			FROM (SELECT *" +
        	"                   FROM " + getGeneralDBScheme()+".vc_comission_type_club_all " +
        	"                  WHERE 1=1";
    	if (!isEmpty(pClubRelType)) {
    		mySQL = mySQL + " AND cd_club_rel_type = ? ";
    		pParam.add(new bcFeautureParam("string", pClubRelType));
    	}
    	if (!isEmpty(pFind)) {
    		mySQL = mySQL + 
    			" AND (UPPER(cd_comission_type) like UPPER('%'||?||'%') OR " +
    			"  UPPER(name_comission_type) like UPPER('%'||?||'%') OR " +
    			"  UPPER(name_club_rel_type) like UPPER('%'||?||'%') OR " +
    			"  UPPER(name_participant_payer) like UPPER('%'||?||'%') OR " +
    			"  UPPER(name_payment_system) like UPPER('%'||?||'%') OR " +
    			"  UPPER(fixed_value_frmt) like UPPER('%'||?||'%') OR " +
    			"  UPPER(percent_value_frmt) like UPPER('%'||?||'%') " +
    			") ";
    		for (int i=0; i<7; i++) {
    		    pParam.add(new bcFeautureParam("string", pFind));
    		}
    	}
    	pParam.add(new bcFeautureParam("int", p_end));
    	pParam.add(new bcFeautureParam("int", p_beg));
    	mySQL = mySQL + 
    		"                  ORDER BY name_comission_type )" + 
        	"          WHERE ROWNUM < ?) " + 
        	"  WHERE rn >= ?";
    	return getHeaderParamHTML(
        		mySQL,
        		pParam,
                2,
                "D",
            	0,
            	"",
            	"../crm/club/comistypespecs.jsp?id=:FIELD11:", 
            	comissionXML,
            	pPrint);
    }
    
    public String getServicePlacesHeadHTML(String id_type, String pClubStatus, String pFind, String p_beg, String p_end, String pPrint) {

    	ArrayList<bcFeautureParam> pParam = initParamArray();

    	String mySQL = 
    		" SELECT rn, name_service_place_type, " +
            "        sname_jur_prs, name_service_place, adr_full, club_date_beg_frmt, name_club_member_status club_member_status, id_service_place " +
        	"	FROM (SELECT ROWNUM rn, id_service_place, " +
        	"                DECODE(id_service_place_type, " +
       		"                       0, '<font color=\"red\"><b>'||name_service_place_type||'</b></font>', " +
       		"                       name_service_place_type" +
       		"                ) name_service_place_type, " +
        	"                sname_jur_prs," +
            "                name_service_place, adr_full, date_beg_frmt club_date_beg_frmt," +
            "                DECODE(cd_club_member_status, " +
       		"                   	'CLOSED', '<font color=\"red\"><b>'||name_club_member_status||'</b></font>', " +
       		"                       'OUT_OF_CLUB', '<font color=\"gray\">'||name_club_member_status||'</font>', " +
       		"                       'IN_PROCESS', '<font color=\"blue\">'||name_club_member_status||'</font>', " +
       		"                       'PARTICIPATE', '<font color=\"green\">'||name_club_member_status||'</font>', " +
       		"                       name_club_member_status" +
       		"                ) name_club_member_status" +
        	"	        FROM (SELECT * " +
        	"                   FROM " + getGeneralDBScheme()+".vc_jur_prs_service_plc_prv_all" +
        	"                  WHERE 1=1 ";
    	if (!isEmpty(id_type)) {
    		mySQL = mySQL + " AND id_service_place_type = ? ";
    		pParam.add(new bcFeautureParam("int", id_type));
    	}
    	if (!isEmpty(pClubStatus)) {
    		mySQL = mySQL + " AND cd_club_member_status = ? ";
    		pParam.add(new bcFeautureParam("string", pClubStatus));
    	}
    	if (!isEmpty(pFind)) {
    		mySQL = mySQL + 
    			" AND (UPPER(adr_full) like UPPER('%'||?||'%') OR " +
    			"  UPPER(name_service_place_type) like UPPER('%'||?||'%') OR " +
    			"  UPPER(sname_jur_prs) like UPPER('%'||?||'%') OR " +
    			"  UPPER(name_service_place) like UPPER('%'||?||'%') " +
    			") ";
    		for (int i=0; i<4; i++) {
    		    pParam.add(new bcFeautureParam("string", pFind));
    		}
    	}
    	pParam.add(new bcFeautureParam("int", p_end));
    	pParam.add(new bcFeautureParam("int", p_beg));
    	mySQL = mySQL + " ORDER BY sname_jur_prs, name_service_place) WHERE ROWNUM < ?) WHERE rn >= ?";
    	
    	return getHeaderParamHTML(
        		mySQL,
        		pParam,
                1,
                "D",
            	0,
            	"",
            	"../crm/clients/service_placespecs.jsp?id=:FIELD8:", 
            	jurpersonXML,
            	pPrint);
    }
    
    public String getTerminalsHeadHTML(String cd_type, String cd_status, String pFind, String p_beg, String p_end, String pPrint) {

    	ArrayList<bcFeautureParam> pParam = initParamArray();

    	String mySQL = 
    		" SELECT rn, id_term, /*id_term_nbu, term_serial_number,*/ /*id_term_hex,*/ name_term_type, " +
    		"        name_term_status, date_last_ses, term_place, adr_place, description " +
        	"	FROM (SELECT ROWNUM rn, id_term, id_term_hex, term_serial_number, id_term_nbu, " +
        	"				 DECODE(cd_term_type, " +
			"                  		'PHYSICAL', '<b><font color=\"green\">'||name_term_type||'</font></b>', " +
			"                  		'WEBPOS', '<font color=\"blue\">'||name_term_type||'</font>', " +
			"                  		name_term_type" +
			"                ) name_term_type, " +
        	"                DECODE(cd_term_status, " +
			"                  		'ACTIVE', '<b><font color=\"green\">'||name_term_status||'</font></b>', " +
			"                  		'SETTING', '<b><font color=\"blue\">'||name_term_status||'</font></b>', " +
			"                 		'EXCLUDED', '<b><font color=\"gray\">'||name_term_status||'</font></b>', " +
			"                  		'BLOCKED', '<b><font color=\"red\">'||name_term_status||'</font></b>', " +
			"                  		name_term_status" +
			"                ) name_term_status, " +
            "                sname_service_place term_place, adr_service_place adr_place, cd_term_status, description, " +
            "                CASE WHEN TRUNC(date_last_ses) < TRUNC(SYSDATE)-1" +
        	"                       THEN '<font color=\"red\"><b>'||date_last_ses_frmt||'</b></font>'" +
        	"                     WHEN TRUNC(date_last_ses) = TRUNC(SYSDATE)-1" +
        	"                       THEN '<font color=\"green\"><b>'||date_last_ses_frmt||'</b></font>'" +
        	"                     ELSE '<font color=\"blue\">'||date_last_ses_frmt||'</font>' " +
        	"                END date_last_ses " +
        	"	        FROM (SELECT * " +
        	"                   FROM " + getGeneralDBScheme()+".vc_term_priv_all" +
        	"                  WHERE 1=1 ";
    	if (!isEmpty(cd_type)) {
    		mySQL = mySQL + " AND cd_term_type = ? ";
    		pParam.add(new bcFeautureParam("string", cd_type));
    	}
    	if (!isEmpty(cd_status)) {
    		mySQL = mySQL + " AND cd_term_status = ? ";
    		pParam.add(new bcFeautureParam("string", cd_status));
    	}
    	if (!isEmpty(pFind)) {
    		mySQL = mySQL + 
    			" AND (TO_CHAR(UPPER(id_term)) like UPPER('%'||?||'%') OR " +
    			"  UPPER(id_term_hex) like UPPER('%'||?||'%') OR " +
    			//"  UPPER(id_term_nbu) like UPPER('%'||?||'%') OR " +
    			"  UPPER(date_last_ses_frmt) like UPPER('%'||?||'%') OR " +
    			"  UPPER(name_term_owner) like UPPER('%'||?||'%') OR " +
    			"  UPPER(description) like UPPER('%'||?||'%') " +
    			") ";
    		for (int i=0; i<5; i++) {
    		    pParam.add(new bcFeautureParam("string", pFind));
    		}
    	}
    	pParam.add(new bcFeautureParam("int", p_end));
    	pParam.add(new bcFeautureParam("int", p_beg));
    	mySQL = mySQL + ") WHERE ROWNUM < ?) WHERE rn >= ?";
    	
    	return getHeaderParamHTML(
        		mySQL,
        		pParam,
                0,
                "D",
            	0,
            	"",
            	"../crm/clients/terminalspecs.jsp?id=:FIELD2:&id_loyality_history=", 
            	terminalXML,
            	pPrint);
    }

    public String getTerminalCertificateHeadHTML(String pType, String p_profile, String pFind, String p_beg, String p_end, String pPrint) {
        StringBuilder html = new StringBuilder();
    	
    	String mySQL_profile = "SELECT filtr FROM " + getGeneralDBScheme()+".v_menu_profile_filtr_all WHERE id_menu_element = ? ";
    	
    	String mySQL = "";
    	
    	PreparedStatement st_profile = null;
        String myFiltr = "";
        
        ArrayList<bcFeautureParam> pParam = initParamArray();

        if ("I".equalsIgnoreCase(pType)) {
    		mySQL = 
    			" SELECT rn, id_term, id_term_hex, /*id_term_nbu,*/ id_term_certificate, " +
    			"        text_certificate, begin_action_date_frmt, end_action_date_frmt," +
    			"        state_certificate_tsl " +
    			"	FROM (SELECT ROWNUM rn, id_term_certificate, id_term, id_term_hex,  id_term_nbu, " +
    			"                text_certificate, begin_action_date_frmt, " +
    			"                end_action_date_frmt, " +
    			"  			     DECODE(state_certificate, " +
    			"                  		'E', '<b><font color=\"red\">'||state_certificate_tsl||'</font></b>', " +
    			"                  		'I', '<b><font color=\"green\">'||state_certificate_tsl||'</font></b>', " +
    			"                  		'N', '<b><font color=\"blue\">'||state_certificate_tsl||'</font></b>', " +
    			"                   	state_certificate_tsl " +
    			"                ) state_certificate_tsl " +
    			"	        FROM (SELECT * " +
    			"                   FROM " + getGeneralDBScheme()+".vc_term_certificates_int_p_all " +
    			"                  WHERE 1=1 ";
            if (!isEmpty(pFind)) {
        		mySQL = mySQL + 
        			" AND (UPPER(TO_CHAR(id_term_certificate)) LIKE UPPER('%'||?||'%') OR " +
        			"	     UPPER(id_term) LIKE UPPER('%'||?||'%') OR " +
        			"	     UPPER(text_certificate) LIKE UPPER('%'||?||'%') OR " +
        			"	     UPPER(begin_action_date_frmt) LIKE UPPER('%'||?||'%') OR " +
        			"	     UPPER(end_action_date_frmt) LIKE UPPER('%'||?||'%')) ";
        		for (int i=0; i<5; i++) {
        		    pParam.add(new bcFeautureParam("string", pFind));
        		}
        	}
            pParam.add(new bcFeautureParam("int", p_end));
            pParam.add(new bcFeautureParam("int", p_beg));
            mySQL = mySQL +
    			"        )  WHERE ROWNUM < ?) WHERE rn >= ?";

            return getHeaderParamHTML(
            		mySQL,
            		pParam,
                    0,
                    "D",
                	0,
                	"",
                	"../crm/clients/certificatespecs.jsp?id_term=:FIELD2:&id_cert=:FIELD4:&id_profile=" + pType, 
                	terminalCertificateXML,
                	pPrint);
        } else if ("T".equalsIgnoreCase(pType)) {
        	mySQL = 
        		" SELECT rn, id_term, id_term_hex, /*id_term_nbu,*/ name_device_type, work_with_certificate_tsl, " +
        		"        id_term_certificate, begin_action_date_frmt, end_action_date_frmt, is_certificate_current_tsl, " +
        		"        is_certificate_received_tsl, date_certificate_received_frmt, has_error " +
        		"	FROM (SELECT ROWNUM rn, id_term, id_term_hex, id_term_nbu, name_device_type, work_with_certificate_tsl, " +
        		"                id_term_certificate, begin_action_date_frmt, end_action_date_frmt, " +
        		"  			     DECODE(is_certificate_current, " +
    			"                  		'Y', '<b><font color=\"green\">'||is_certificate_current_tsl||'</font></b>', " +
    			"                   	'<b><font color=\"red\">'||is_certificate_current_tsl||'</font></b>' " +
    			"                ) is_certificate_current_tsl, " +
        		"  			     DECODE(is_certificate_received, " +
    			"                  		'Y', '<b><font color=\"green\">'||is_certificate_received_tsl||'</font></b>', " +
    			"                   	'<b><font color=\"red\">'||is_certificate_received_tsl||'</font></b>' " +
    			"                ) is_certificate_received_tsl, " +
                "				 date_certificate_received_frmt, has_error " +
        		"	        FROM (SELECT * " +
        		"                   FROM " + getGeneralDBScheme()+".vc_term_certificates_cur_p_all " +
        		"                  WHERE 1=1 ";
    	
        	if (!isEmpty(p_profile)) {
        		try {
        			LOGGER.debug("(profiles)" + mySQL_profile + 
        	    			", 1={'" + p_profile + "',int}");
        			con = Connector.getConnection(getSessionId());
        			st_profile = con.prepareStatement(mySQL_profile);
        			st_profile.setInt(1, Integer.parseInt(p_profile));
        			ResultSet rset_profile = st_profile.executeQuery(mySQL_profile);
        			while (rset_profile.next()) {
        				if (!("".equalsIgnoreCase(rset_profile.getString(1).trim()))) {
        					myFiltr = myFiltr + " AND "+rset_profile.getString(1);
        				} else {
        				}
        			}
        		} // try
        		catch (SQLException e) {LOGGER.error(html, e); html.append(showCRMException(e));}
               	catch (Exception el) {LOGGER.error(html, el); html.append(showCRMException(el));}
        		finally {
                    try {
                        if (st_profile!=null) {
							st_profile.close();
						}
                    } catch (SQLException w) {w.toString();}
                    try {
                        if (con!=null) {
							con.close();
						}
                    } catch (SQLException w) {w.toString();}
                    Connector.closeConnection(con);
                } // finally
        	}

            if (!isEmpty(pFind)) {
        		mySQL = mySQL + 
        			" AND (UPPER(TO_CHAR(id_term)) LIKE UPPER('%'||?||'%')) ";
        		for (int i=0; i<1; i++) {
        		    pParam.add(new bcFeautureParam("string", pFind));
        		}
        	}

            pParam.add(new bcFeautureParam("int", p_end));
            pParam.add(new bcFeautureParam("int", p_beg));
        	if ("".equalsIgnoreCase(myFiltr)) {
        		mySQL = mySQL +  ") WHERE ROWNUM < ?) WHERE rn >= ?";
        	} else {
        		mySQL = mySQL +  myFiltr + ") WHERE ROWNUM < ?) WHERE rn >= ?";
        	}
			Connector.closeConnection(con);

            return getHeaderParamHTML(
            		mySQL,
            		pParam,
                    1,
                    "N",
                	12,
                	"1",
                	"../crm/clients/certificatespecs.jsp?id_term=:FIELD2:&id_cert=:FIELD6:&id_profile=" + pType, 
                	terminalCertificateXML,
                	pPrint);
        } else if ("C".equalsIgnoreCase(pType)) {
    		mySQL = 
    			" SELECT rn, id_term, id_term_hex, /*id_term_nbu,*/ id_term_certificate, text_certificate, begin_action_date_frmt, end_action_date_frmt,  "+
                "   	 is_certificate_received_tsl, date_certificate_received_frmt, file_name " +
                "   FROM (SELECT ROWNUM rn, id_term, id_term_hex, id_term_nbu, id_term_certificate, text_certificate, begin_action_date_frmt, " +
                "                end_action_date_frmt, " +
                "  			     DECODE(is_certificate_received, " +
    			"                  		'Y', '<b><font color=\"green\">'||is_certificate_received_tsl||'</font></b>', " +
    			"                   	'<b><font color=\"red\">'||is_certificate_received_tsl||'</font></b>' " +
    			"                ) is_certificate_received_tsl, " +
                "				 date_certificate_received_frmt," +
                "                file_name " +
				"	        FROM (SELECT * " +
				"                   FROM " + getGeneralDBScheme()+".vc_term_certificates_priv_all " +
				"                  WHERE 1=1 ";
	        if (!isEmpty(pFind)) {
	    		mySQL = mySQL + 
	    			" AND (UPPER(TO_CHAR(id_term_certificate)) LIKE UPPER('%'||?||'%') OR " +
	    			"	     UPPER(id_term) LIKE UPPER('%'||?||'%') OR " +
	    			"	     UPPER(id_term_hex) LIKE UPPER('%'||?||'%') OR " +
	    			//"	     UPPER(id_term_nbu) LIKE UPPER('%'||?||'%') OR " +
	    			"	     UPPER(text_certificate) LIKE UPPER('%'||?||'%') OR " +
	    			"	     UPPER(begin_action_date_frmt) LIKE UPPER('%'||?||'%') OR " +
	    			"	     UPPER(end_action_date_frmt) LIKE UPPER('%'||?||'%')) ";
	    		for (int i=0; i<6; i++) {
	    		    pParam.add(new bcFeautureParam("string", pFind));
	    		}
	    	}
	        pParam.add(new bcFeautureParam("int", p_end));
	        pParam.add(new bcFeautureParam("int", p_beg));
	        mySQL = mySQL +
				"        )  WHERE ROWNUM < ?) WHERE rn >= ?";
	
	        return getHeaderParamHTML(
	        		mySQL,
	        		pParam,
	                0,
	                "D",
	            	0,
	            	"",
	            	"../crm/clients/certificatespecs.jsp?id_term=:FIELD2:&id_cert=:FIELD4:&id_profile=" + pType, 
	            	terminalCertificateXML,
	            	pPrint);
        } else {
        	return html.toString();
        }
    }

    public String getWarningsHeadHTML(String pUser, String pType, String pStatus, String pFind, String p_beg, String p_end, String pPrint) {

    	ArrayList<bcFeautureParam> pParam = initParamArray();

    	String mySQL = 
    		" SELECT rn, name_menu_element, type_warning_tsl, status_warning_tsl, " +
    		"        desc_warning, created_by_name found_by, creation_date_frmt, row_count, id_warning " +
    		"   FROM (SELECT ROWNUM rn, id_warning, " +  
    		"				 name_menu_element||DECODE(id_tabsheet, NULL, NULL, '/'||name_tabsheet) name_menu_element," + 
    		"  				 DECODE(type_warning, " +
    		"                       'ERROR', '<b><font color=\"red\">'||type_warning_tsl||'</font></b>', " +
    		"                       'CRITICAL_ERROR', '<font color=\"red\">'||type_warning_tsl||'</font>', " +
    		"                       'REMARK', '<b><font color=\"blue\">'||type_warning_tsl||'</font></b>', " +
    		"                       'SPECIFICATION', '<font color=\"gray\">'||type_warning_tsl||'</font>', " +
    		"                       'COMPLETION', '<font color=\"green\">'||type_warning_tsl||'</font>', " +
    		"                       type_warning_tsl)  " +
    		"				 type_warning_tsl," + 
    		"  				 DECODE(status_warning, " +
    		"                       'N', '<font color=\"black\">'||status_warning_tsl||'</font>', " +
    		"                       'P', '<font color=\"gray\">'||status_warning_tsl||'</font>', " +
    		"                       'A', '<font color=\"gray\">'||status_warning_tsl||'</font>', " +
    		"                       'C', '<font color=\"green\">'||status_warning_tsl||'</font>', " +
    		"                       'D', '<font color=\"red\"><b>'||status_warning_tsl||'</b></font>', " +
    		"                       'S', '<font color=\"blue\">'||status_warning_tsl||'</font>', " +
    		"                       status_warning_tsl)  " +
    		"				 status_warning_tsl, " +
    		"                desc_warning, created_by_name, creation_date_frmt, row_count " +
    		"           FROM (SELECT a.*, count(*) over () as row_count " +
    		"                   FROM " + getGeneralDBScheme()+".vc_warnings_all a " +
    		"                  WHERE 1=1 ";
    	if (!("".equalsIgnoreCase(pUser) || "ALL".equalsIgnoreCase(pUser))) {
    		mySQL = mySQL + " AND created_by = ? ";
    		pParam.add(new bcFeautureParam("int", pUser));
    	}
    	if (!("".equalsIgnoreCase(pType))) {
    		mySQL = mySQL + " AND type_warning = ? ";
    		pParam.add(new bcFeautureParam("string", pType));
    	}
    	if (!("".equalsIgnoreCase(pStatus))) {
    		if ("NC".equalsIgnoreCase(pStatus)) {
    			mySQL = mySQL + " AND NOT (status_warning IN ('C','D'))";
    		} else {
    			mySQL = mySQL + " AND status_warning = ? ";
    			pParam.add(new bcFeautureParam("string", pStatus));
    		}
    	}
    	if (!isEmpty(pFind)) {
    		mySQL = mySQL + 
    			" AND (UPPER(TO_CHAR(id_warning)) LIKE UPPER('%'||?||'%') OR " +
    			"	     UPPER(name_menu_element) LIKE UPPER('%'||?||'%') OR " +
    			"	     UPPER(name_tabsheet) LIKE UPPER('%'||?||'%') OR " +
    			"	     UPPER(desc_warning) LIKE UPPER('%'||?||'%') OR " +
    			"	     UPPER(creation_date_frmt) LIKE UPPER('%'||?||'%')) ";
    		for (int i=0; i<5; i++) {
    		    pParam.add(new bcFeautureParam("string", pFind));
    		}
    	}
    	pParam.add(new bcFeautureParam("int", p_end));
    	pParam.add(new bcFeautureParam("int", p_beg));
    	mySQL = mySQL + " ORDER BY id_warning DESC) WHERE ROWNUM < ?) WHERE rn >= ? ";
    	
    	return getHeaderParamHTML(
        		mySQL,
        		pParam,
                2,
                "D",
            	0,
            	"",
            	"../admin/warningspecs.jsp?id=:FIELD9:", 
            	warningXML,
            	pPrint);
    } //getWarningsHeadHTML()

    public String getBKAccountsHTML(String id_scheme, String pIsGroup, String pExistFlag, String pFind, String p_beg, String p_end, String pPrint) {

    	ArrayList<bcFeautureParam> pParam = initParamArray();

    	String mySQL = 
        	" SELECT rn, cd_bk_account, name_bk_account, cd_bk_account_parent, " +
	  		"        is_group_tsl, exist_flag_tsl, id_bk_account " +
  		    "   FROM (SELECT ROWNUM rn, id_bk_account, cd_bk_account, name_bk_account, " +
  		    "                cd_bk_account_parent, " +
  		    "	      		 DECODE(is_group, " +
			"          			    'Y', '<b><font color=\"blue\">'||is_group_tsl||'</font></b>', " +
			"              		    '<b><font color=\"green\">'||is_group_tsl||'</font></b>' " +
			"          		 ) is_group_tsl, " +
  		    "	      		 DECODE(exist_flag, " +
			"          			    'Y', '<b><font color=\"green\">'||exist_flag_tsl||'</font></b>', " +
			"              		    '<b><font color=\"red\">'||exist_flag_tsl||'</font></b>' " +
			"          		 ) exist_flag_tsl " +
	  		"          FROM (SELECT * " +
	  		"                  FROM " + getGeneralDBScheme()+".vc_bk_accounts_club_all " +
	  		"                 WHERE 1=1 ";
        if (!(id_scheme==null)) {
        	if ("EMPTY".equalsIgnoreCase(id_scheme)) {
        		mySQL = mySQL + " AND id_bk_account_scheme_line IS NULL";
        	} else if (!("".equalsIgnoreCase(id_scheme))) {
        		mySQL = mySQL + " AND id_bk_account_scheme_line IN (" +
        				" SELECT id_bk_account_scheme_line " +
        				"   FROM " + getGeneralDBScheme()+".vc_bk_account_sh_ln_club_all" +
        				"  WHERE id_bk_account_scheme = ?)";
        		pParam.add(new bcFeautureParam("int", id_scheme));
        	}
        }
        if (!isEmpty(pFind)) {
        	mySQL = mySQL + " AND (UPPER(cd_bk_account) LIKE '%'||?||'%' OR " +
        			" UPPER(name_bk_account) LIKE '%'||?||'%')";
        	for (int i=0; i<2; i++) {
        	    pParam.add(new bcFeautureParam("string", pFind));
        	}
        }
        if (!isEmpty(pIsGroup)) {
    		mySQL = mySQL + " AND is_group = ? ";
    		pParam.add(new bcFeautureParam("string", pIsGroup));
    	}
        if (!isEmpty(pExistFlag)) {
    		mySQL = mySQL + " AND exist_flag = ? ";
    		pParam.add(new bcFeautureParam("string", pExistFlag));
    	}

        pParam.add(new bcFeautureParam("int", p_end));
        pParam.add(new bcFeautureParam("int", p_beg));
        mySQL = mySQL + ") WHERE ROWNUM < ?) WHERE rn >= ?";
    	
        return getHeaderParamHTML(
        		mySQL,
        		pParam,
                1,
                "D",
            	0,
            	"",
            	"../crm/finance/bk_accountspecs.jsp?id=:FIELD7:", 
            	bk_accountXML, 
            	pPrint);
    } // getBKAccountsHTML

    public String getInvoicesHTML(String pFindString, String pCdInvoiceState, String pPriority, String p_beg, String p_end, String pPrint) {

    	ArrayList<bcFeautureParam> pParam = initParamArray();

    	String mySQL = 
        	" SELECT rn, number_invoice, date_invoice_frmt, sname_jur_prs_receiver, " +
	  		"        sname_jur_prs_payer, invoice_doc, name_fn_priority, " +
	  		"        name_fn_invoice_state, total_sum_frmt, paid_sum_frmt, id_invoice " +
  		    "   FROM (SELECT ROWNUM rn, number_invoice, date_invoice_frmt, sname_jur_prs_payer, " +
			"                sname_jur_prs_receiver, invoice_doc, " +
			"                DECODE(cd_fn_priority, " +
			"          			    'CRITICAL', '<b><font color=\"red\">'||name_fn_priority||'</font></b>', " +
			"          			    'HIGH', '<font color=\"red\">'||name_fn_priority||'</font>', " +
			"          			    'MEDIUM', '<font color=\"blue\">'||name_fn_priority||'</font>', " +
			"          			    'LOW', '<font color=\"green\">'||name_fn_priority||'</font>', " +
			"              		    name_fn_priority " +
			"          		 ) name_fn_priority,  " +
			"                DECODE(cd_fn_invoice_state, " +
			"          			    'PAID', '<b><font color=\"green\">'||name_fn_invoice_state||'</font></b>', " +
			"          			    'ISNT_PAID', '<font color=\"blue\">'||name_fn_invoice_state||'</font>', " +
			"          			    'CANCELLED', '<font color=\"red\">'||name_fn_invoice_state||'</font>', " +
			"              		    name_fn_priority " +
			"          		 ) name_fn_invoice_state, " +
			"                total_sum_frmt||' '||name_currency total_sum_frmt, " +
			"                paid_sum_frmt||' '||name_currency paid_sum_frmt, id_invoice " +
	  		"          FROM (SELECT * " +
	  		"                  FROM " + getGeneralDBScheme()+".vc_fn_invoice_club_all " +
	  		"                 WHERE 1=1 ";
        if (!isEmpty(pFindString)) {
        	mySQL = mySQL + 
        		" AND (UPPER(number_invoice) LIKE '%'||?||'%' OR " +
        		"      UPPER(date_invoice_frmt) LIKE '%'||?||'%') OR " +
        		"      UPPER(sname_jur_prs_receiver) LIKE '%'||?||'%') OR " +
        		"      UPPER(sname_jur_prs_payer) LIKE '%'||?||'%') OR " +
        		"      UPPER(invoice_doc) LIKE '%'||?||'%')";
        	for (int i=0; i<5; i++) {
        	    pParam.add(new bcFeautureParam("string", pFindString));
        	}
        }
        if (!isEmpty(pCdInvoiceState)) {
    		mySQL = mySQL + " AND cd_fn_invoice_state = ? ";
    		pParam.add(new bcFeautureParam("string", pCdInvoiceState));
    	}
        if (!isEmpty(pPriority)) {
    		mySQL = mySQL + " AND cd_fn_priority = ? ";
    		pParam.add(new bcFeautureParam("string", pPriority));
    	}

        pParam.add(new bcFeautureParam("int", p_end));
        pParam.add(new bcFeautureParam("int", p_beg));
        mySQL = mySQL + " ORDER BY date_invoice DESC, number_invoice, id_invoice DESC) " +
        		" WHERE ROWNUM < ?) " +
        		" WHERE rn >= ?";
    	
        return getHeaderParamHTML(
        		mySQL,
        		pParam,
                1,
                "D",
            	0,
            	"",
            	"../crm/finance/invoicespecs.jsp?id=:FIELD11:", 
            	clearingXML, 
            	pPrint);
    } // getBKAccountsHTML

    public String getClearingHeadHTML(String pFind, String p_beg, String p_end, String pPrint) {

    	ArrayList<bcFeautureParam> pParam = initParamArray();

    	String mySQL =
        	" SELECT rn, date_clearing, number_clearing, begin_period_frmt, end_period_frmt, " +
        	"  		 line_count, reconciled_line_count, posting_count, state_clearing, club_name, id_clearing " +
        	"   FROM (SELECT ROWNUM rn, id_clearing, date_clearing_frmt date_clearing, begin_period_frmt, end_period_frmt, number_clearing, " +
        	"  		         line_count, reconciled_line_count, posting_count, " +
        	"                DECODE(state_clearing, " +
        	"                       'CREATED', state_clearing_tsl, " +
        	"                       'CREATED_WITH_ERROR', '<font color=\"red\"><b>'||state_clearing_tsl||'</b></font>', " +
        	"                       'EXPORTED', '<font color=\"ligthbrown\">'||state_clearing_tsl||'</font>', " +
        	"                       'EXPORTED_WITH_ERROR', '<font color=\"red\"><b>'||state_clearing_tsl||'</b></font>', " +
        	"                       'RECONCILED', '<font color=\"darkgreen\">'||state_clearing_tsl||'</font>', " +
        	"                       'RECONCILED_PARTIALLY', '<font color=\"linghgreen\">'||state_clearing_tsl||'</font>', " +
        	"                       'ERROR', '<font color=\"red\"><b>'||state_clearing_tsl||'</b></font>', " +
        	"                       state_clearing_tsl) state_clearing, sname_club club_name " +
        	"           FROM (SELECT * " +
        	"                   FROM " + getGeneralDBScheme()+".VC_CLEARING_CLUB_ALL ";

        if (!isEmpty(pFind)) {
        	mySQL = mySQL + " WHERE (TO_CHAR(id_clearing) LIKE UPPER('%'||?||'%') OR " +
				"UPPER(date_clearing_frmt) LIKE UPPER('%'||?||'%') OR " +
				"UPPER(begin_period_frmt) LIKE UPPER('%'||?||'%') OR " +
				"UPPER(end_period_frmt) LIKE UPPER('%'||?||'%') OR " +
				"UPPER(number_clearing) LIKE UPPER('%'||?||'%') OR " +
				"UPPER(line_count) LIKE UPPER('%'||?||'%') OR " +
				"UPPER(reconciled_line_count) LIKE UPPER('%'||?||'%') OR " +
				"UPPER(posting_count) LIKE UPPER('%'||?||'%')) ";
        	for (int i=0; i<8; i++) {
        	    pParam.add(new bcFeautureParam("string", pFind));
        	}
    	}
        pParam.add(new bcFeautureParam("int", p_end));
        pParam.add(new bcFeautureParam("int", p_beg));
        mySQL = mySQL + 
        	"                  ORDER BY id_clearing DESC)" +
        	"          WHERE ROWNUM < ?) " +
        	"  WHERE rn >= ?";

        int lessColumn = 0;
        if (!isEmpty(this.idClub)) {
        	lessColumn = 1;
        }
        return getHeaderParamHTML(
        		mySQL,
        		pParam,
        		lessColumn,
                "",
            	1,
            	"",
            	"../crm/finance/clearingspecs.jsp?id=:FIELD11:", 
            	clearingXML,
            	pPrint);
    } //getClearingHeadHTML

    public String getClearingLinesHeadHTML(String pFind, String p_beg, String p_end, String pPrint) {

    	ArrayList<bcFeautureParam> pParam = initParamArray();
    	
    	String mySQL =
        	" SELECT rn, date_clearing_frmt, number_doc_clearing, " +
            " 	     receiver_mfo_bank, receiver_name_bank_alt,  " +
            " 	     receiver_number_bank_account, receiver_sname_owner_ba, " +
            " 	     payer_mfo_bank, payer_name_bank_alt,  " +
            " 	     payer_number_bank_account, payer_sname_owner_ba, " +
            "        name_currency, entered_amount_frmt, " +
            " 	     payment_function, id_clearing_line " +
            "  FROM (SELECT ROWNUM rn, id_clearing_line, number_doc_clearing, date_clearing_frmt, " +
            " 		        receiver_mfo_bank, receiver_sname_bank receiver_name_bank_alt,  " +
            " 		        receiver_number_bank_account, receiver_sname_owner_ba, " +
            " 		        payer_mfo_bank, payer_sname_bank payer_name_bank_alt,  " +
            " 		        payer_number_bank_account, payer_sname_owner_ba, " +
            " 		        name_currency, entered_amount_frmt, " +
            " 		        payment_function " +
            " 		   FROM (SELECT *" +
            "                  FROM " + getGeneralDBScheme()+".vc_clearing_lines_club_all " ;
        if (!isEmpty(pFind)) {
	    	mySQL = mySQL + " WHERE (UPPER(date_clearing_frmt) LIKE UPPER('%'||?||'%') OR " +
	    			"UPPER(number_doc_clearing) LIKE UPPER('%'||?||'%') OR " + 
					"UPPER(receiver_sname_owner_ba) LIKE UPPER('%'||?||'%') OR " + 
	    			"UPPER(receiver_number_bank_account) LIKE UPPER('%'||?||'%') OR " + 
	    			"UPPER(receiver_sname_bank) LIKE UPPER('%'||?||'%') OR " + 
	    			"UPPER(payer_sname_owner_ba) LIKE UPPER('%'||?||'%') OR " + 
	    			"UPPER(payer_number_bank_account) LIKE UPPER('%'||?||'%') OR " + 
	    			"UPPER(payer_sname_bank) LIKE UPPER('%'||?||'%') OR " + 
	    			"UPPER(entered_amount_frmt) LIKE UPPER('%'||?||'%') OR " + 
	    			"UPPER(unreconciled_amount_frmt) LIKE UPPER('%'||?||'%') OR " + 
	    			"UPPER(payment_function) LIKE UPPER('%'||?||'%')) ";
	    	for (int i=0; i<11; i++) {
	    	    pParam.add(new bcFeautureParam("string", pFind));
	    	}
	    }
        pParam.add(new bcFeautureParam("int", p_end));
        pParam.add(new bcFeautureParam("int", p_beg));
        mySQL = mySQL +
        	" 		  ORDER BY date_clearing DESC, number_doc_clearing)  " +
            "        WHERE ROWNUM < ?" + 
            " ) WHERE rn >= ?";

        StringBuilder html = new StringBuilder();
        PreparedStatement st = null; 
        
        try{
            
        	LOGGER.debug(prepareSQLToLog(mySQL, pParam));
            con = Connector.getConnection(getSessionId());
            
            st = con.prepareStatement(mySQL);
            st = prepareParam(st, pParam);
            ResultSet rset = st.executeQuery();
            ResultSetMetaData mtd = rset.getMetaData();
            
            int colCount = mtd.getColumnCount();
            if ("Y".equalsIgnoreCase(pPrint)) {
            	html.append(getPrintTable());
            } else {
            	html.append(getHeaderTable());
            }
            html.append("<thead>\n");
            
            html.append("<tr>\n");
            html.append("<th rowspan=\"2\">"+ commonXML.getfieldTransl("RN", false)+"</th>\n");
            html.append("<th rowspan=\"2\">"+ clearingXML.getfieldTransl("DATE_CLEARING", false)+"</th>\n");
            html.append("<th rowspan=\"2\">"+ clearingXML.getfieldTransl("NUMBER_DOC_CLEARING", false)+"</th>\n");
            html.append("<th colspan=\"4\">"+ clearingXML.getfieldTransl("RECEIVER_PARTICIPANT", false)+"</th>\n");
            html.append("<th colspan=\"4\">"+ clearingXML.getfieldTransl("PAYER_PARTICIPANT", false)+"</th>\n");
            html.append("<th rowspan=\"2\">"+ clearingXML.getfieldTransl("NAME_CURRENCY", false)+"</th>\n");
            html.append("<th rowspan=\"2\">"+ clearingXML.getfieldTransl("ENTERED_AMOUNT", false)+"</th>\n");
            html.append("<th rowspan=\"2\">"+ clearingXML.getfieldTransl("PAYMENT_FUNCTION", false)+"</th>\n");
            html.append("</tr>\n");
            html.append("<tr>");
            html.append("<th>"+ clearingXML.getfieldTransl("RECEIVER_MFO_BANK", false)+"</th>\n");
            html.append("<th>"+ clearingXML.getfieldTransl("RECEIVER_NAME_BANK_ALT", false)+"</th>\n");
            html.append("<th>"+ clearingXML.getfieldTransl("RECEIVER_NUMBER_BANK_ACCOUNT", false)+"</th>\n");
            html.append("<th>"+ clearingXML.getfieldTransl("RECEIVER_SNAME_OWNER_BA", false)+"</th>\n");
            html.append("<th>"+ clearingXML.getfieldTransl("PAYER_MFO_BANK", false)+"</th>\n");
            html.append("<th>"+ clearingXML.getfieldTransl("PAYER_NAME_BANK_ALT", false)+"</th>\n");
            html.append("<th>"+ clearingXML.getfieldTransl("PAYER_NUMBER_BANK_ACCOUNT", false)+"</th>\n");
            html.append("<th>"+ clearingXML.getfieldTransl("PAYER_SNAME_OWNER_BA", false)+"</th>\n");
            html.append("</tr>");
            html.append("</thead><tbody>\n");
            
            while (rset.next())
            {
                html.append("<tr id=\"elem_"+rset.getString("ID_CLEARING_LINE")+"\" onmouseout=\"coloredOut(this)\" onmouseover=\"coloredOver(this,1)\">");

                String myHyperLink = "../crm/finance/clearing_linespecs.jsp?id=" + rset.getString("ID_CLEARING_LINE");
                html.append("<td align=\"center\">"+getHyperLinkFirst()+myHyperLink+getHyperLinkMiddle()+ "<font color=\"black\">" + getValue2(rset.getString("RN")) + "</font>" + getHyperLinkEnd() + "</td>\n");
                for (int i=2; i <= colCount-1; i++) {
                	html.append("<td align=\"center\">"+getHyperLinkFirst()+myHyperLink+getHyperLinkMiddle()+ "<font color=\"black\">" + getValue2(rset.getString(i)) + "</font>" + getHyperLinkEnd() + "</td>\n");
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
    } //getClearingHeadHTML

    public String getPaymentOrdersHeadHTML(String pFind, String p_beg, String p_end, String pPrint) {

    	ArrayList<bcFeautureParam> pParam = initParamArray();

    	String mySQL =
        	" SELECT rn, number_line, date_payment_order_frmt, " +
            " 	     receiver_mfo_bank, receiver_name_bank_alt,  " +
            " 	     receiver_number_bank_account, receiver_sname_owner_ba, " +
            " 	     payer_mfo_bank, payer_name_bank_alt,  " +
            " 	     payer_number_bank_account, payer_sname_owner_ba, " +
            "        name_currency, entered_amount_frmt, " +
            " 	     payment_function, id_payment_order " +
            "  FROM (SELECT ROWNUM rn, id_payment_order, number_line, date_payment_order_frmt, " +
            " 		        receiver_mfo_bank, receiver_sname_bank receiver_name_bank_alt,  " +
            " 		        receiver_number_bank_account, receiver_sname_owner_ba, " +
            " 		        payer_mfo_bank, payer_sname_bank payer_name_bank_alt,  " +
            " 		        payer_number_bank_account, payer_sname_owner_ba, " +
            " 		        name_currency, entered_amount_frmt,  " +
            " 		        payment_function " +
            " 		   FROM (SELECT *" +
            "                  FROM " + getGeneralDBScheme()+".vc_payment_order_club_all " ;
        if (!isEmpty(pFind)) {
	    	mySQL = mySQL + " WHERE (UPPER(id_payment_order) LIKE UPPER('%'||?||'%') OR " +
	    			"UPPER(number_line) LIKE UPPER('%'||?||'%') OR " + 
	    			"UPPER(date_payment_order_frmt) LIKE UPPER('%'||?||'%') OR " + 
					"UPPER(receiver_sname_owner_ba) LIKE UPPER('%'||?||'%') OR " + 
	    			"UPPER(receiver_number_bank_account) LIKE UPPER('%'||?||'%') OR " + 
	    			"UPPER(receiver_sname_bank) LIKE UPPER('%'||?||'%') OR " + 
	    			"UPPER(payer_sname_owner_ba) LIKE UPPER('%'||?||'%') OR " + 
	    			"UPPER(payer_number_bank_account) LIKE UPPER('%'||?||'%') OR " + 
	    			"UPPER(payer_sname_bank) LIKE UPPER('%'||?||'%') OR " + 
	    			"UPPER(entered_amount_frmt) LIKE UPPER('%'||?||'%') OR " + 
	    			"UPPER(payment_function) LIKE UPPER('%'||?||'%')) ";
	    	for (int i=0; i<11; i++) {
	    	    pParam.add(new bcFeautureParam("string", pFind));
	    	}
	    }
        pParam.add(new bcFeautureParam("int", p_end));
        pParam.add(new bcFeautureParam("int", p_beg));
        mySQL = mySQL +
        	" 		  ORDER BY number_line DESC, date_payment_order)  " +
            "        WHERE ROWNUM < ?" + 
            " ) WHERE rn >= ?";

        StringBuilder html = new StringBuilder();
        PreparedStatement st = null; 
        
        try{
            
        	LOGGER.debug(prepareSQLToLog(mySQL, pParam));
            con = Connector.getConnection(getSessionId());
            
            st = con.prepareStatement(mySQL);
            st = prepareParam(st, pParam);
            ResultSet rset = st.executeQuery();
            ResultSetMetaData mtd = rset.getMetaData();
            
            int colCount = mtd.getColumnCount();
            if ("Y".equalsIgnoreCase(pPrint)) {
            	html.append(getPrintTable());
            } else {
            	html.append(getHeaderTable());
            }
            html.append("<thead>\n");
            
            html.append("<tr>\n");
            html.append("<th rowspan=\"2\">"+ commonXML.getfieldTransl("RN", false)+"</th>\n");
            html.append("<th rowspan=\"2\">"+ clearingXML.getfieldTransl("NUMBER_LINE_PAYMENT_ORDER", false)+"</th>\n");
            html.append("<th rowspan=\"2\">"+ clearingXML.getfieldTransl("DATE_PAYMENT_ORDER", false)+"</th>\n");
            html.append("<th colspan=\"4\">"+ clearingXML.getfieldTransl("RECEIVER_PARTICIPANT", false)+"</th>\n");
            html.append("<th colspan=\"4\">"+ clearingXML.getfieldTransl("PAYER_PARTICIPANT", false)+"</th>\n");
            html.append("<th rowspan=\"2\">"+ clearingXML.getfieldTransl("NAME_CURRENCY", false)+"</th>\n");
            html.append("<th rowspan=\"2\">"+ clearingXML.getfieldTransl("ENTERED_AMOUNT", false)+"</th>\n");
            html.append("<th rowspan=\"2\">"+ clearingXML.getfieldTransl("PAYMENT_FUNCTION", false)+"</th>\n");
            
            if (!isEmpty(deleteHyperLink)) {
            	html.append("<th rowspan=\"2\">&nbsp;</th>\n");
            }
            
            html.append("</tr>\n");
            html.append("<tr>");
            html.append("<th>"+ clearingXML.getfieldTransl("RECEIVER_MFO_BANK", false)+"</th>\n");
            html.append("<th>"+ clearingXML.getfieldTransl("RECEIVER_NAME_BANK_ALT", false)+"</th>\n");
            html.append("<th>"+ clearingXML.getfieldTransl("RECEIVER_NUMBER_BANK_ACCOUNT", false)+"</th>\n");
            html.append("<th>"+ clearingXML.getfieldTransl("RECEIVER_SNAME_OWNER_BA", false)+"</th>\n");
            html.append("<th>"+ clearingXML.getfieldTransl("PAYER_MFO_BANK", false)+"</th>\n");
            html.append("<th>"+ clearingXML.getfieldTransl("PAYER_NAME_BANK_ALT", false)+"</th>\n");
            html.append("<th>"+ clearingXML.getfieldTransl("PAYER_NUMBER_BANK_ACCOUNT", false)+"</th>\n");
            html.append("<th>"+ clearingXML.getfieldTransl("PAYER_SNAME_OWNER_BA", false)+"</th>\n");
            html.append("</tr>");
            html.append("</thead><tbody>\n");
            
            while (rset.next())
            {
            	String deleteId = "";
            	String deleteName = "";
            	
            	for (int i=1; i <= colCount; i++) {
            		if (deleteEntryId.equalsIgnoreCase(mtd.getColumnName(i))) {
            			deleteId = getHTMLValue(rset.getString(i));
            		}
            		if (deleteEntryName.equalsIgnoreCase(mtd.getColumnName(i))) {
            			deleteName = getHTMLValue(rset.getString(i));
            		}
            	}
            	
                html.append("<tr id=\"elem_"+rset.getString("ID_PAYMENT_ORDER")+"\" onmouseout=\"coloredOut(this)\" onmouseover=\"coloredOver(this,1)\">");
                
                String myHyperLink = "../crm/finance/payment_orderspecs.jsp?id=" + rset.getString("ID_PAYMENT_ORDER");
                html.append("<td align=\"center\">"+getHyperLinkFirst()+myHyperLink+getHyperLinkMiddle()+ "<font color=\"black\">" + getValue2(rset.getString("RN")) + "</font>" + getHyperLinkEnd() + "</td>\n");
                for (int i=2; i <= colCount-1; i++) {
                	html.append("<td align=\"center\">"+getHyperLinkFirst()+myHyperLink+getHyperLinkMiddle()+ "<font color=\"black\">" + getValue2(rset.getString(i)) + "</font>" + getHyperLinkEnd() + "</td>\n");
                }

            	if (!isEmpty(deleteHyperLink)) {
                	html.append("<td align=\"center\"><div class=\"div_button\" onclick=\"var msg='" + deleteConfirmMessage + " \\\'" + deleteName + "\\\'?';var res=window.confirm(msg);if (res) {ajaxpage('"+deleteHyperLink + deleteId + "','div_main');}\">" + "<img vspace=\"0\" hspace=\"0\"" +
            				" src=\"../images/oper/rows/delete.png\" width=\"16\" height=\"16\" align=\"top\" style=\"border: 0px;\"" + 
            				" title=\"" + buttonXML.getfieldTransl("button_delete", false)+"\">" + getHyperLinkEnd()+"</td>\n");
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
    } //getClearingHeadHTML
    
    public String getCardPackagesHeadHTML(String pFind, String p_beg, String p_end, String pPrint) {

    	ArrayList<bcFeautureParam> pParam = initParamArray();

    	String mySQL =
    		"SELECT rn, sname_jur_prs sname_partner, name_card_status card_status, name_jur_prs_card_pack, total_amount_card_pack_frmt, entrance_fee_frmt, " +
	  		"		membership_fee_frmt, share_fee_frmt, dealer_margin_frmt, agent_margin_frmt, " +
	  		"       /*action_date_beg_frmt, action_date_end_frmt,*/ id_jur_prs_card_pack, id_card_status, id_club " +
	  		"  FROM (SELECT ROWNUM rn, sname_jur_prs, name_jur_prs_card_pack, name_card_status," +
	  		"				total_amount_jp_card_pack_frmt||' '||sname_currency total_amount_card_pack_frmt , " +
	  		"				entrance_fee_frmt||' '||sname_currency entrance_fee_frmt, " +
	  		"				membership_fee_frmt||' '||sname_currency||' ('||membership_fee_month_count||'"+webposXML.getfieldTransl("cheque_target_prg_poriod_month", false)+")' membership_fee_frmt, " +
	  		"				share_fee_frmt||' '||sname_currency share_fee_frmt, " +
	  		"				dealer_margin_frmt||' '||sname_currency dealer_margin_frmt, " +
	  		"				agent_margin_frmt||' '||sname_currency agent_margin_frmt, " +
	  		"				action_date_beg_frmt, action_date_end_frmt, id_jur_prs_card_pack, id_card_status, id_club " +
	  		"          FROM (SELECT sname_jur_prs, name_jur_prs_card_pack, name_card_status, sname_currency, entrance_fee_frmt, " +
	  		"						membership_fee_frmt, share_fee_frmt, membership_fee_month_count, " +
	  		"						dealer_margin_frmt, agent_margin_frmt, total_amount_jp_card_pack_frmt," +
	  		"                       action_date_beg_frmt, action_date_end_frmt, id_jur_prs_card_pack, " +
	  		"                       id_card_status, id_club " +
            "				   FROM " + getGeneralDBScheme() + ".vc_club_jp_card_pack_club_all ";
    	if (!isEmpty(pFind)) {
    		mySQL = mySQL + 
    			" WHERE (UPPER(sname_jur_prs) LIKE UPPER('%'||?||'%') OR " +
    			"		UPPER(name_card_status) LIKE UPPER('%'||?||'%') OR " +
    			"		UPPER(name_jur_prs_card_pack) LIKE UPPER('%'||?||'%') OR " +
    			"		UPPER(total_amount_card_pack_frmt) LIKE UPPER('%'||?||'%')) ";
    		for (int i=0; i<4; i++) {
    		    pParam.add(new bcFeautureParam("string", pFind));
    		}
    	}
    	pParam.add(new bcFeautureParam("int", p_end));
    	pParam.add(new bcFeautureParam("int", p_beg));
        mySQL = mySQL + 
        	  "                  ORDER BY sname_jur_prs, name_card_status, name_jur_prs_card_pack, action_date_beg_frmt) " +
          	  "          WHERE ROWNUM < ?) " +
          	  "  WHERE rn >= ?";

        return getHeaderParamHTML(
        		mySQL,
        		pParam,
                3,
                "",
            	0,
            	"",
            	"../crm/club/card_packagespecs.jsp?id=:FIELD11:", 
            	clubcardXML,
            	pPrint);
    } //getClubsHeadHTML
    
    public String getReferralSchemeHeaderHTML(String pFind, String pType, String pCalcType, String p_beg, String p_end, String pPrint) {

    	ArrayList<bcFeautureParam> pParam = initParamArray();
    	
    	String mySQL =
        	" SELECT rn, " +
        	"        /*DECODE(cd_referral_scheme_type," +
        	"               'TARGET_PROGRAM', '<font color=\"blue\"><b>'||name_referral_scheme_type||'<b></font>', " +
            "               'PAYMENT', '<font color=\"green\"><b>'||name_referral_scheme_type||'<b></font>', " +
            "               name_referral_scheme_type" +
        	"        ) name_referral_scheme_type,*/ " +
        	"        DECODE(cd_referral_scheme_type," +
        	"               'TARGET_PROGRAM', '<font color=\"blue\"><b>'||name_target_prg||'<b></font>', " +
            "               'PAYMENT', '<font color=\"green\"><b>'||sname_jur_prs||'<b></font>', " +
            "               ''" +
        	"        ) jur_prs_and_target_prg, name_referral_scheme, desc_referral_scheme, " +
        	"        DECODE(cd_referral_scheme_calc_type," +
        	"               'POINT_SUM', '<font color=\"blue\"><b>'||name_referral_scheme_calc_type||'<b></font>', " +
            "               'CLUB_SUM', '<font color=\"green\"><b>'||name_referral_scheme_calc_type||'<b></font>', " +
            "               name_referral_scheme_calc_type" +
        	"        ) name_referral_scheme_calc_type, " +
        	"		 accounting_level_count, accounting_percent_all_frmt, " +
        	"        /*club_name, date_beg_frmt, date_end_frmt,*/ id_referral_scheme " +
        	"   FROM (SELECT ROWNUM rn, sname_jur_prs, name_target_prg, name_referral_scheme, desc_referral_scheme, cd_referral_scheme_type, name_referral_scheme_type, " +
        	"		         cd_referral_scheme_calc_type, name_referral_scheme_calc_type, " +
        	"                accounting_level_count, accounting_percent_all_frmt, " +
        	"                sname_club club_name, date_beg_frmt, date_end_frmt, id_referral_scheme " +
        	"   	    FROM (SELECT * " +
        	"                   FROM " + getGeneralDBScheme()+".vc_referral_scheme_club_all " +
        	"          WHERE 1=1 ";
    	if (!isEmpty(pFind)) {
    		mySQL = mySQL + 
    			" AND (UPPER(sname_jur_prs) LIKE UPPER('%'||?||'%') OR " +
    			"	   UPPER(name_referral_scheme) LIKE UPPER('%'||?||'%') OR " +
    			"	   UPPER(desc_referral_scheme) LIKE UPPER('%'||?||'%')) ";
    		for (int i=0; i<3; i++) {
    		    pParam.add(new bcFeautureParam("string", pFind));
    		}
    	}
        if (!isEmpty(pType)) {
    		mySQL = mySQL + " AND cd_referral_scheme_type = ? ";
    		pParam.add(new bcFeautureParam("string", pType));
    	}
        if (!isEmpty(pCalcType)) {
    		mySQL = mySQL + " AND cd_referral_scheme_calc_type = ? ";
    		pParam.add(new bcFeautureParam("string", pCalcType));
    	}
    	pParam.add(new bcFeautureParam("int", p_end));
    	pParam.add(new bcFeautureParam("int", p_beg));
        mySQL = mySQL + 
        	  "       ORDER BY name_referral_scheme DESC) " +
          	  "          WHERE ROWNUM < ?) " +
          	  "  WHERE rn >= ?";

        return getHeaderParamHTML(
        		mySQL,
        		pParam,
                1,
                "",
            	0,
            	"",
            	"../crm/club/referral_schemespecs.jsp?id=:FIELD8:", 
            	clubXML,
            	pPrint);
    } //getClubsHeadHTML
    
    public String getClubsHeadHTML(String pFind, String p_beg, String p_end, String pPrint) {

    	ArrayList<bcFeautureParam> pParam = initParamArray();

    	String mySQL =
        	" SELECT rn, cd_club, name_club, name_operator, id_club " +
        	  "   FROM (SELECT ROWNUM rn, id_club, cd_club, name_club, sname_jur_prs name_operator " +
        	  "   		  FROM (SELECT * " +
        	  "                   FROM " + getGeneralDBScheme()+".vc_club_info ";
    	if (!isEmpty(pFind)) {
    		mySQL = mySQL + " WHERE (TO_CHAR(id_club) LIKE UPPER('%'||?||'%') OR " +
    			"UPPER(id_club) LIKE UPPER('%'||?||'%') OR " +
    			"UPPER(name_club) LIKE UPPER('%'||?||'%') OR " + 
    			"UPPER(sname_jur_prs) LIKE UPPER('%'||?||'%')) ";
    		for (int i=0; i<4; i++) {
    		    pParam.add(new bcFeautureParam("string", pFind));
    		}
    	}
    	pParam.add(new bcFeautureParam("int", p_end));
    	pParam.add(new bcFeautureParam("int", p_beg));
        mySQL = mySQL + 
        	  "                  ORDER BY sname_club) " +
          	  "          WHERE ROWNUM < ?) " +
          	  "  WHERE rn >= ?";

        return getHeaderParamHTML(
        		mySQL,
        		pParam,
                1,
                "",
            	0,
            	"",
            	"../crm/club/clubspecs.jsp?id=:FIELD5:", 
            	clubXML,
            	pPrint);
    } //getClubsHeadHTML
    
    public String getClubsFundHeadHTML(String pFind, String p_beg, String p_end, String pPrint) {

    	ArrayList<bcFeautureParam> pParam = initParamArray();

    	String mySQL =
        	" SELECT rn, sname_jur_prs, '<span>'||LPAD(' ', ((level_club_fund-1)*4*6), '&nbsp;')||name_club_fund||'</span>' name_club_fund, " +
        	"        sname_club_fund, desc_club_fund, " +
        	"        amount_rest_frmt amount_rest_amnt, date_calc_rest_frmt, id_club_fund " +
        	"   FROM (SELECT ROWNUM rn, id_club_fund, name_club_fund, sname_club_fund, desc_club_fund, " +
        	"                sname_jur_prs, date_calc_rest_frmt, amount_rest_frmt, level_club_fund " +
        	"   		  FROM (SELECT order_rn, id_club_fund, name_club_fund, sname_club_fund, desc_club_fund, " +
        	"                          sname_jur_prs, date_calc_rest_frmt, amount_rest_frmt, level_club_fund" +
        	"                    FROM (SELECT ROWNUM order_rn, id_club_fund, name_club_fund, sname_club_fund, desc_club_fund, " +
        	"                                 sname_jur_prs, date_calc_rest_frmt, " +
        	"                                 amount_rest_frmt, id_club, LEVEL level_club_fund " +
        	"                            FROM " + getGeneralDBScheme()+".vc_club_fund_club_all " +
        	"                           START WITH id_club_fund_parent IS NULL " +
        	"                         CONNECT BY PRIOR id_club_fund = id_club_fund_parent " +
        	"                           ORDER siblings BY id_club, sname_jur_prs, name_club_fund, id_club_fund) ";
    	if (!isEmpty(pFind)) {
    		mySQL = mySQL + " WHERE (TO_CHAR(id_club_fund) LIKE UPPER('%'||?||'%') OR " +
    			"UPPER(name_club_fund) LIKE UPPER('%'||?||'%') OR " +
    			"UPPER(sname_club_fund) LIKE UPPER('%'||?||'%') OR " + 
    			"UPPER(desc_club_fund) LIKE UPPER('%'||?||'%') OR " + 
    			"UPPER(sname_jur_prs) LIKE UPPER('%'||?||'%')) ";
    		for (int i=0; i<5; i++) {
    		    pParam.add(new bcFeautureParam("string", pFind));
    		}
    	}
    	pParam.add(new bcFeautureParam("int", p_end));
    	pParam.add(new bcFeautureParam("int", p_beg));
        mySQL = mySQL + 
        	  "                  ORDER BY order_rn) " +
          	  "          WHERE ROWNUM < ?) " +
          	  "  WHERE rn >= ?";

        return getHeaderParamHTML(
        		mySQL,
        		pParam,
                1,
                "",
            	0,
            	"",
            	"../crm/club/fundspecs.jsp?id=:FIELD8:", 
            	clubfundXML,
            	pPrint);
    } //getClubsHeadHTML
    
    public String getClubsTargetPrgHeadHTML(String pFind, String pPayPeriod, String p_beg, String p_end, String pPrint) {

    	ArrayList<bcFeautureParam> pParam = initParamArray();

    	String mySQL =
    		" SELECT rn, sname_jur_prs, name_target_prg, " +
        	"        name_target_prg_pay_period target_prg_pay_period, " +
        	"        pay_amount_full_frmt pay_amount_short_frmt, desc_target_prg, " +
        	"        /*sname_club,*/ date_beg_frmt target_program_date_beg, " +
        	"        date_end_frmt target_program_date_end, " +
        	"        id_target_prg " +
        	"   FROM (SELECT ROWNUM rn, sname_jur_prs, id_target_prg, " +
        	"                '<span>'||LPAD(' ', ((level_target_prg-1)*4*6), '&nbsp;')||name_target_prg||'</span>' name_target_prg, desc_target_prg, " +
        	"                name_target_prg_pay_period, " +
        	"                DECODE(cd_target_prg_pay_period, NULL, '', pay_amount_full_frmt) pay_amount_full_frmt, sname_club, date_beg_frmt, date_end_frmt, " +
        	"                level_target_prg " +
        	"   		  FROM (SELECT order_rn, sname_jur_prs, id_target_prg, " +
        	"                          CASE WHEN id_target_prg_parent IS NULL " +
        	"                               THEN '<b><font color=\"green\">'||name_target_prg||'</font></b>'" +
        	"                               ELSE name_target_prg" +
        	"                          END name_target_prg, desc_target_prg, " +
        	"                          cd_target_prg_pay_period, " +
        	"                          CASE WHEN NVL(child_count,0) > 0 THEN '' ELSE name_target_prg_pay_period END name_target_prg_pay_period, pay_amount_full_frmt, " +
        	"                          sname_club, date_beg_frmt, date_end_frmt, " +
        	"                          level_target_prg" +
        	"                    FROM (SELECT ROWNUM order_rn, sname_jur_prs, id_target_prg, name_target_prg, desc_target_prg, " +
        	"                                 DECODE(cd_target_prg_pay_period, " +
            "               						 'IRREGULAR', '<font color=\"red\"><b>'||name_target_prg_pay_period||'</b></font>', " +
            "               						 'HOUR', '<font color=\"green\"><b>'||name_target_prg_pay_period||'</b></font>', " +
            "               						 'DAY', '<font color=\"green\"><b>'||name_target_prg_pay_period||'</b></font>', " +
            "               						 'WEEK', '<font color=\"green\"><b>'||name_target_prg_pay_period||'</b></font>', " +
            "               						 'MONTH', '<font color=\"green\"><b>'||name_target_prg_pay_period||'</b></font>', " +
            "               						 'STUDY_COUNT', '<font color=\"blue\"><b>'||name_target_prg_pay_period||'</b></font>', " +
            "               						 name_target_prg_pay_period" +
            "        		 		 		  ) name_target_prg_pay_period, pay_amount_full_frmt, " +
        	"                                 cd_target_prg_pay_period, sname_club, date_beg_frmt, " +
        	"                                 date_end_frmt, id_club, id_target_prg_parent, " +
        	"                                 id_jur_prs, LEVEL level_target_prg, child_count " +
        	"                            FROM " + getGeneralDBScheme()+".vc_target_prg_club_all " +
        	"                           START WITH id_target_prg_parent IS NULL " +
        	"                         CONNECT BY PRIOR id_target_prg = id_target_prg_parent " +
        	"                           ORDER siblings BY id_club, name_target_prg, id_target_prg) " +
        	"                      WHERE 1=1 ";
    	if (!isEmpty(pFind)) {
    		mySQL = mySQL + 
    			" AND (UPPER(sname_jur_prs) LIKE UPPER('%'||?||'%') OR " +
    			"      UPPER(name_target_prg) LIKE UPPER('%'||?||'%') OR " +
    			"      UPPER(pay_amount_full_frmt) LIKE UPPER('%'||?||'%') OR " +
    			"      UPPER(date_beg_frmt) LIKE UPPER('%'||?||'%') OR " +
    			"      UPPER(date_end_frmt) LIKE UPPER('%'||?||'%')) ";
    		for (int i=0; i<5; i++) {
    		    pParam.add(new bcFeautureParam("string", pFind));
    		}
    	}
    	if (!isEmpty(pPayPeriod)) {
    		mySQL = mySQL + " AND cd_target_prg_pay_period = ? ";
    		pParam.add(new bcFeautureParam("string", pPayPeriod));
    	}
    	pParam.add(new bcFeautureParam("int", p_end));
    	pParam.add(new bcFeautureParam("int", p_beg));
        mySQL = mySQL + 
        	  "                  ORDER BY order_rn) " +
          	  "          WHERE ROWNUM < ?) " +
          	  "  WHERE rn >= ?";

        return getHeaderParamHTML(
        		mySQL,
        		pParam,
                1,
                "",
            	0,
            	"",
            	"../crm/club/target_programspecs.jsp?id=:FIELD9:", 
            	clubfundXML,
            	pPrint);
    } //getClubsHeadHTML
    
    public String getClubActionsHeadHTML(String pActionType, String pActionState, String pFind, String p_beg, String p_end, String pPrint) {

    	ArrayList<bcFeautureParam> pParam = initParamArray();

    	String mySQL =
          	  " SELECT rn, name_club_event_type, name_club_event, desc_action_club, " +
          	  "        name_club_event_state, date_beg_frmt, date_end_frmt, id_club_event " +
          	  "   FROM (SELECT ROWNUM rn, id_club_event, " +
          	  "                DECODE(cd_club_event_type, " +
              "                       'REGULAR', '<font color=\"green\"><b>'||name_club_event_type||'<b></font>', " +
              "                       'ONE_TIME', '<font color=\"blue\"><b>'||name_club_event_type||'<b></font>', " +
              "                       name_club_event_type " +
              "                ) name_club_event_type, " +
              "                DECODE(cd_club_event_state, " +
              "                       'OPERATING', '<font color=\"green\"><b>'||name_club_event_state||'<b></font>', " +
              "                       'HAS_ENDED', '<font color=\"red\"><b>'||name_club_event_state||'<b></font>', " +
              "                       'SUSPENDED', '<font color=\"gray\"><b>'||name_club_event_state||'<b></font>', " +
              "                       name_club_event_state " +
              "                ) name_club_event_state, " +
          	  "                name_club_event, desc_action_club, " +
          	  "                date_beg_frmt, date_end_frmt " +
          	  "   		  FROM (SELECT * " +
          	  "                   FROM " + getGeneralDBScheme()+".vc_club_event_club_all " +
          	  "                  WHERE 1=1";
        if (!isEmpty(pFind)) {
    		mySQL = mySQL + 
    			" AND (UPPER(TO_CHAR(id_club_event)) LIKE UPPER('%'||?||'%') OR " +
    			"	   UPPER(name_club_event) LIKE UPPER('%'||?||'%') OR " +
    			"	   UPPER(desc_action_club) LIKE UPPER('%'||?||'%') OR " +
    			"	   UPPER(date_beg_frmt) LIKE UPPER('%'||?||'%') OR " +
    			"	   UPPER(date_end_frmt) LIKE UPPER('%'||?||'%')) ";
    		for (int i=0; i<5; i++) {
    		    pParam.add(new bcFeautureParam("string", pFind));
    		}
    	}
    	if (!isEmpty(pActionType)) {
    		mySQL = mySQL + " AND cd_club_event_type = ? ";
    		pParam.add(new bcFeautureParam("string", pActionType));
    	}
    	if (!isEmpty(pActionState)) {
    		mySQL = mySQL + " AND cd_club_event_state = ? ";
    		pParam.add(new bcFeautureParam("string", pActionState));
    	}
    	pParam.add(new bcFeautureParam("int", p_end));
    	pParam.add(new bcFeautureParam("int", p_beg));
        mySQL = mySQL + 
          	  "                  ORDER BY date_beg DESC) " +
          	  "          WHERE ROWNUM < ?) " +
          	  "  WHERE rn >= ?";

        return getHeaderParamHTML(
        		mySQL,
        		pParam,
                1,
                "",
            	0,
            	"",
            	"../crm/club_event/clubeventspecs.jsp?id=:FIELD8:", 
            	club_actionXML,
            	pPrint);
    } //getClubActionsHeadHTML
    
    public String getClubActionGiftsDeliveryHeadHTML(String pGiftState, String pFind, String p_beg, String p_end, String pPrint) {

    	ArrayList<bcFeautureParam> pParam = initParamArray();

    	String mySQL =
        	" SELECT rn, name_nat_prs, name_nat_prs_gift_state, " +
            "        name_gift, cost_gift_frmt, name_club_event, " +
            "        date_reserve_frmt, date_given_frmt, id_nat_prs_gift " +
            "   FROM (SELECT ROWNUM rn, id_nat_prs_gift, name_nat_prs, name_nat_prs_gift_state, " +
            "                name_gift, cost_gift_frmt, name_club_event, date_reserve_frmt, " +
            "                date_given_frmt " +
            "           FROM (SELECT id_nat_prs_gift, full_name name_nat_prs, " +
            "                        DECODE(cd_nat_prs_gift_state, " +
            "                               'RESERVED', '<font color=\"green\"><b>'||name_nat_prs_gift_state||'<b></font>', " +
            "                               'GIVEN', '<font color=\"blue\"><b>'||name_nat_prs_gift_state||'<b></font>', " +
            "                               '<font color=\"red\"><b>'||name_nat_prs_gift_state||'<b></font>' " +
            "                        ) name_nat_prs_gift_state, " +
            "                        name_nat_prs_gift name_gift, " +
            "                        cost_lg_gift_frmt || ' ' || sname_lg_currency cost_gift_frmt, " +
            "                        name_club_event, date_reserve_frmt, date_given_frmt " +
          	"   	  	        FROM " + getGeneralDBScheme()+".vc_nat_prs_gifts_club_all " +
          	"                  WHERE 1=1";
        if (!isEmpty(pFind)) {
    		mySQL = mySQL + 
    			" AND (UPPER(TO_CHAR(full_name)) LIKE UPPER('%'||?||'%') OR " +
    			"	   UPPER(phone_contact) LIKE UPPER('%'||?||'%') OR " +
    			"	   UPPER(name_club_event) LIKE UPPER('%'||?||'%') OR " +
    			"	   UPPER(name_gift) LIKE UPPER('%'||?||'%') OR " +
    			"	   UPPER(desc_lg_gift) LIKE UPPER('%'||?||'%')) ";
    		for (int i=0; i<5; i++) {
    		    pParam.add(new bcFeautureParam("string", pFind));
    		}
    	}
    	if (!isEmpty(pGiftState)) {
    		mySQL = mySQL + " AND cd_nat_prs_gift_state = ? ";
    		pParam.add(new bcFeautureParam("string", pGiftState));
    	}
    	pParam.add(new bcFeautureParam("int", p_end));
    	pParam.add(new bcFeautureParam("int", p_beg));
        mySQL = mySQL + 
          	  "                  ORDER BY date_reserve DESC) " +
          	  "          WHERE ROWNUM < ?) " +
          	  "  WHERE rn >= ?";

        return getHeaderParamHTML(
        		mySQL,
        		pParam,
                1,
                "",
            	0,
            	"",
            	"../crm/club_event/deliveryspecs.jsp?id=:FIELD9:", 
            	club_actionXML,
            	pPrint);
    } //getClubActionsHeadHTML
    
    public String getClubActionsGiftsHeadHTML(String pGiftType, String pFind, String p_beg, String p_end, String pOrderField, String pOrderType, String pPrint) {

    	ArrayList<bcFeautureParam> pParam = initParamArray();

    	String mySQL =
          	  " SELECT rn, cd_gift, name_gift, name_gift_type, id_gift " +
          	  "   FROM (SELECT ROWNUM rn, id_gift, cd_gift, name_gift, name_gift_type " +
          	  "   		  FROM (SELECT * " +
          	  "                   FROM " + getGeneralDBScheme()+".vc_gifts_club_all " +
          	  "                  WHERE 1=1 ";
        if (!isEmpty(pFind)) {
    		mySQL = mySQL + 
    			" AND (UPPER(TO_CHAR(id_gift)) LIKE UPPER('%'||?||'%') OR " +
    			"	   UPPER(cd_gift) LIKE UPPER('%'||?||'%') OR " +
    			"	   UPPER(name_gift) LIKE UPPER('%'||?||'%')) ";
    		for (int i=0; i<3; i++) {
    		    pParam.add(new bcFeautureParam("string", pFind));
    		}
    	}
    	if (!isEmpty(pGiftType)) {
    		mySQL = mySQL + " AND cd_gift_type = ? ";
    		pParam.add(new bcFeautureParam("string", pGiftType));
    	}
	    if (!isEmpty(pOrderField)) {
	    	mySQL = mySQL +
	          	" ORDER BY " + pOrderField;
		    if (!isEmpty(pOrderType)) {
		    	  mySQL = mySQL + " " + pOrderType;
		    }
	    } else {
	    	mySQL = mySQL +	" ORDER BY 4";
	    }
	    pParam.add(new bcFeautureParam("int", p_end));
	    pParam.add(new bcFeautureParam("int", p_beg));
        mySQL = mySQL + 
          	  "                  ) " +
          	  "          WHERE ROWNUM < ?) " +
          	  "  WHERE rn >= ?";
		
        return getHeaderExtendHTML(
        		mySQL,
        		pParam,
                1,
                "D",
            	0,
            	"",
            	"../crm/club_event/giftspecs.jsp?id=:FIELD5:", 
            	club_actionXML,
            	pPrint,
            	true,
            	"../crm/club_event/gifts.jsp?id=", 
            	pOrderField,
            	pOrderType);
    } //getClubActionsHeadHTML
    

    public String getOperSchemeHeadHTML(String pExecType, String pState, String pFind, String p_beg, String p_end, String pPrint) {

    	ArrayList<bcFeautureParam> pParam = initParamArray();

    	String mySQL = 
    		" SELECT rn, name_fn_oper_scheme, desc_fn_oper_scheme, " +
    		"        name_fn_oper_exec_type, name_fn_oper_state, id_fn_oper_scheme " +
	      	"   FROM (SELECT ROWNUM rn, id_fn_oper_scheme, name_fn_oper_scheme, desc_fn_oper_scheme, " +
	      	"                DECODE(cd_fn_oper_exec_type, " +
            "               		'IMPORT_QUESTIONNAIRE', '<font color=\"blue\"><b>'||name_fn_oper_exec_type||'</b></font>', " +
            "               		'MANUAL_BY_OPERATOR', '<font color=\"green\"><b>'||name_fn_oper_exec_type||'</b></font>', " +
            "               		'ON_TERMINAL', '<font color=\"black\"><b>'||name_fn_oper_exec_type||'</b></font>', " +
            "               		name_fn_oper_state" +
            "                ) name_fn_oper_exec_type," +
	      	"                DECODE(cd_fn_oper_state, " +
            "               		'IN_DEVELOPMENT', '<font color=\"blue\"><b>'||name_fn_oper_state||'</b></font>', " +
            "               		'CONFIRMED', '<font color=\"green\"><b>'||name_fn_oper_state||'</b></font>', " +
            "               		'ERROR', '<font color=\"red\"><b>'||name_fn_oper_state||'</b></font>', " +
            "               		name_fn_oper_state" +
            "        		 ) name_fn_oper_state " +
	      	" 			FROM (SELECT * " +
	      	"                   FROM " + getGeneralDBScheme() + ".vc_fn_oper_scheme_club_all " +
	      	"                  WHERE 1 = 1 ";
        if (!isEmpty(pFind)) {
    		mySQL = mySQL + 
    			" AND (TO_CHAR(id_fn_oper_scheme) LIKE UPPER('%'||?||'%') OR " +
    			"      UPPER(name_fn_oper_scheme) LIKE UPPER('%'||?||'%') OR " +
    			"      UPPER(desc_fn_oper_scheme) LIKE UPPER('%'||?||'%')) ";
    		for (int i=0; i<3; i++) {
    		    pParam.add(new bcFeautureParam("string", pFind));
    		}
    	}
    	if (!isEmpty(pExecType)) {
    		mySQL = mySQL + " AND cd_fn_oper_exec_type = ? ";
    		pParam.add(new bcFeautureParam("string", pExecType));
    	}
    	if (!isEmpty(pState)) {
    		mySQL = mySQL + " AND cd_fn_oper_state = ? ";
    		pParam.add(new bcFeautureParam("string", pState));
    	}
    	pParam.add(new bcFeautureParam("int", p_end));
    	pParam.add(new bcFeautureParam("int", p_beg));
    	mySQL = mySQL + "  ORDER BY name_fn_oper_scheme" +
    		"        ) WHERE ROWNUM < ?" + 
    		" ) WHERE rn >= ?";

        return getHeaderParamHTML(
        		mySQL,
        		pParam,
                1,
                "D",
            	0,
            	"CANCELED",
            	"../crm/finance/oper_schemespecs.jsp?id=:FIELD6:", 
            	oper_schemeXML, pPrint);
    }

    public String getPostingSchemeHeadHTML(String pState, String pFind, String p_beg, String p_end, String pPrint) {

    	ArrayList<bcFeautureParam> pParam = initParamArray();

    	String mySQL = 
    		" SELECT rn, desc_fn_posting_scheme, name_fn_posting_scheme_state, " +
	      	"        begin_action_date, end_action_date, desc_bk_account_scheme, id_fn_posting_scheme " +
	      	"   FROM (SELECT ROWNUM rn, id_fn_posting_scheme, desc_fn_posting_scheme, " +
	      	"                DECODE(cd_fn_posting_scheme_state, " +
            "               		'IN_WORKING', '<font color=\"blue\"><b>'||name_fn_posting_scheme_state||'</b></font>', " +
            "               		'APPROVED', '<font color=\"green\"><b>'||name_fn_posting_scheme_state||'</b></font>', " +
            "               		'CANCELED', '<font color=\"red\"><b>'||name_fn_posting_scheme_state||'</b></font>', " +
            "               		name_fn_posting_scheme_state" +
            "                ) name_fn_posting_scheme_state, " +
	      	"                begin_action_date_frmt begin_action_date, end_action_date_frmt end_action_date," +
	      	"                desc_bk_account_scheme " +
	      	" 			FROM (SELECT * " +
	      	"                   FROM " + getGeneralDBScheme() + ".vc_fn_posting_scheme_club_all " +
	      	"                  WHERE 1 = 1 ";
        if (!isEmpty(pFind)) {
    		mySQL = mySQL + " AND (TO_CHAR(id_fn_posting_scheme) LIKE UPPER('%'||?||'%') OR " +
    			"UPPER(desc_fn_posting_scheme) LIKE UPPER('%'||?||'%') OR " +
    			"UPPER(begin_action_date_frmt) LIKE UPPER('%'||?||'%') OR " +
    			"UPPER(end_action_date_frmt) LIKE UPPER('%'||?||'%')) ";
    		for (int i=0; i<4; i++) {
    		    pParam.add(new bcFeautureParam("string", pFind));
    		}
    	}
    	if (!isEmpty(pState)) {
    		mySQL = mySQL + " AND cd_fn_posting_scheme_state = ? ";
    		pParam.add(new bcFeautureParam("string", pFind));
    	}
    	pParam.add(new bcFeautureParam("int", p_end));
    	pParam.add(new bcFeautureParam("int", p_beg));
    	mySQL = mySQL + "  ORDER BY begin_action_date DESC" +
    		"        ) WHERE ROWNUM < ?" + 
    		" ) WHERE rn >= ?";

        return getHeaderParamHTML(
        		mySQL,
        		pParam,
                1,
                "D",
            	0,
            	"",
            	"../crm/finance/posting_schemespecs.jsp?id=:FIELD7:&opercode=-1", 
            	posting_schemeXML, pPrint);
    }

    public String getOperSchemeLinesHeadHTML(String pCdOperationType, String pCdClubRelType, String pFind, String p_beg, String p_end, String pPrint) {

    	ArrayList<bcFeautureParam> pParam = initParamArray();

    	String mySQL = 
    		" SELECT rn, name_club_rel_type, name_bk_operation_type, " +
    		"        DECODE(cd_bk_phase, " +
    		"               'AFTER_MONEY_TRANSFER', '<font color=\"green\">'||name_bk_phase||'</font>', " +
    		"               name_bk_phase) name_bk_phase, " +
    		"        oper_number, " +
	      	"        debet_cd_bk_account_sh_line debet_cd_bk_account, " +
	      	"        credit_cd_bk_account_sh_line credit_cd_bk_account, " +
	      	"        oper_content, " +
	      	"		 exist_flag_tsl, id_bk_operation_scheme_line " +
	      	"   FROM (SELECT ROWNUM rn, id_bk_operation_scheme_line, name_club_rel_type, name_bk_operation_type, " +
	      	"                cd_bk_phase, name_bk_phase, oper_number, debet_cd_bk_account_sh_line, " +
	      	"                credit_cd_bk_account_sh_line, oper_content, " +
	      	"                DECODE(exist_flag, " +
            "               		'Y', '<font color=\"green\"><b>'||exist_flag_tsl||'</b></font>', " +
            "               		'<font color=\"red\"><b>'||exist_flag_tsl||'</b></font>'" +
            "                ) exist_flag_tsl " +
	      	" 			FROM (SELECT * " +
	      	"                   FROM " + getGeneralDBScheme() + ".vc_oper_scheme_lines_club_all " +
	      	"                  WHERE 1 = 1 ";
    	if (!(isEmpty(pCdOperationType) || "-1".equalsIgnoreCase(pCdOperationType))) {
    		mySQL = mySQL + " AND cd_bk_operation_type = ? ";
    		pParam.add(new bcFeautureParam("string", pFind));
    	}
    	if (!(isEmpty(pCdClubRelType) || "-1".equalsIgnoreCase(pCdClubRelType))) {
    		mySQL = mySQL + " AND cd_club_rel_type = ? ";
    		pParam.add(new bcFeautureParam("string", pFind));
    	}
        if (!isEmpty(pFind)) {
    		mySQL = mySQL + " AND (UPPER(name_bk_phase) LIKE UPPER('%'||?||'%') OR " +
    			"UPPER(oper_number) LIKE UPPER('%'||?||'%') OR " +
    			"UPPER(debet_cd_bk_account_sh_line) LIKE UPPER('%'||?||'%') OR " +
    			"UPPER(credit_cd_bk_account_sh_line) LIKE UPPER('%'||?||'%') OR " +
    			"UPPER(oper_content) LIKE UPPER('%'||?||'%')) ";
    		for (int i=0; i<5; i++) {
    		    pParam.add(new bcFeautureParam("string", pFind));
    		}
    	}

        pParam.add(new bcFeautureParam("int", p_end));
        pParam.add(new bcFeautureParam("int", p_beg));
    	mySQL = mySQL + "  ORDER BY oper_number, name_bk_phase" +
    		"        ) WHERE ROWNUM < ?" + 
    		" ) WHERE rn >= ?";

        return getHeaderParamHTML(
        		mySQL,
        		pParam,
                1,
                "D",
            	0,
            	"",
            	"../crm/finance/posting_schemespecs.jsp?id=:FIELD10:&opercode=-1", 
            	posting_schemeXML, pPrint);
    }
	
    private ArrayList<String> idScheme = new ArrayList<String>();
    private Map <String, String> operType = new HashMap<String, String>();
    private Map <String, String> debetCode = new HashMap<String, String>();
    private Map <String, String> creditCode = new HashMap<String, String>();
    private Map <String, String> operNumber = new HashMap<String, String>();
    private Map <String, String> operContent = new HashMap<String, String>();
    private Map <String, String> namePhase = new HashMap<String, String>();
    private Map <String, String> idRelatedScheme = new HashMap<String, String>();
    private Map <String, String> levelScheme = new HashMap<String, String>();
    private Map <String, String> hasChild = new HashMap<String, String>();

    public String getOperSchemeLinesTreeHeadHTML(String pCdOperationType, String pPrint) {
    	
    	ArrayList<bcFeautureParam> pParam = initParamArray();

    	String mySQL = 
    		" SELECT id_bk_operation_scheme_line, name_bk_operation_type, debet_cd_bk_account_sh_line," +
    		"        credit_cd_bk_account_sh_line, oper_number, oper_content, name_bk_phase, " +
    		"		 id_related_bk_oper_scheme_line, level_operation_scheme " +
    		"   FROM (SELECT id_bk_operation_scheme_line, name_bk_operation_type, debet_cd_bk_account_sh_line," +
    		"                credit_cd_bk_account_sh_line, oper_number, oper_content, name_bk_phase, " +
    		"				 id_related_bk_oper_scheme_line, LEVEL level_operation_scheme " +
	      	"           FROM (SELECT * " +
	      	"                   FROM " + getGeneralDBScheme() + ".vc_oper_scheme_lines_club_all ";
    	if (!(isEmpty(pCdOperationType) || "-1".equalsIgnoreCase(pCdOperationType))) {
    		mySQL = mySQL + " WHERE cd_bk_operation_type = ? ";
    		pParam.add(new bcFeautureParam("string", pCdOperationType));
    	}
    	mySQL = mySQL + 
	      	"         ) START WITH id_related_bk_oper_scheme_line IS NULL " +
	      	"        CONNECT BY PRIOR id_bk_operation_scheme_line = id_related_bk_oper_scheme_line)" +
	      	"  ORDER BY oper_number";

    	StringBuilder result = new StringBuilder();
    	PreparedStatement st = null;
        
        try{
        	idScheme.clear();
        	operType.clear();
        	debetCode.clear();
        	creditCode.clear();
        	operNumber.clear();
        	operContent.clear();
        	namePhase.clear();
        	idRelatedScheme.clear();
        	levelScheme.clear();
        	hasChild.clear();
        	
        	LOGGER.debug(prepareSQLToLog(mySQL, pParam));
            con = Connector.getConnection(getSessionId());
            st = con.prepareStatement(mySQL);
            st = prepareParam(st, pParam);
            ResultSet rs = st.executeQuery();
            
            while (rs.next()){
            	String id_scheme = getValue3(rs.getString("id_bk_operation_scheme_line"));
            	String name_bk_operation_type = getValue3(rs.getString("name_bk_operation_type"));
            	String debet_cd_bk_account_sh_line = getValue3(rs.getString("debet_cd_bk_account_sh_line"));
            	String credit_cd_bk_account_sh_line = getValue3(rs.getString("credit_cd_bk_account_sh_line"));
            	String oper_number = getValue3(rs.getString("oper_number"));
            	String oper_content = getValue3(rs.getString("oper_content"));
            	String name_bk_phase = getValue3(rs.getString("name_bk_phase"));
            	String id_related_scheme = getValue3(rs.getString("id_related_bk_oper_scheme_line"));
            	String level_scheme = getValue3(rs.getString("level_operation_scheme"));
            	
            	idScheme.add(id_scheme);
            	operType.put(id_scheme, name_bk_operation_type);
            	debetCode.put(id_scheme, debet_cd_bk_account_sh_line);
            	creditCode.put(id_scheme, credit_cd_bk_account_sh_line);
            	operNumber.put(id_scheme, oper_number);
            	operNumber.put(id_scheme, oper_number);
            	operNumber.put(id_scheme, oper_number);
            	operContent.put(id_scheme, oper_content);
            	namePhase.put(id_scheme, name_bk_phase);
            	idRelatedScheme.put(id_scheme, id_related_scheme);
            	levelScheme.put(id_scheme, level_scheme);
            }
            rs.close();
            st.close();
            
            for (int i=0; i <= idScheme.size()-1; i++) {
            	String idSchemeElement = idScheme.get(i);
            	if (idRelatedScheme.containsValue(idSchemeElement)) {
            		hasChild.put(idSchemeElement, "Y");
            	} else {
            		hasChild.put(idSchemeElement, "N");
            	}
            }
        } catch (SQLException e){
             result.append("<br><b>bcHeaders.getOperSchemeTreeHeadHTML() SQLException:<b><br>"+e.toString());
             LOGGER.error("SQLException: " + e.toString());
            }
           catch (Exception e){
               result.append("<br><b>bcHeaders.getOperSchemeTreeHeadHTML() Exception:<b><br>"+e.toString());
               LOGGER.error("Exception: " + e.toString());
           }
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

           result.append(makeOperSchemeTree("", ""));
        return result.toString();
    }

    private String openedParentId = "";
	private String papkaName = "";
    
    private String makeOperSchemeTree(String parentId, String currentId) {
    	StringBuilder result = new StringBuilder();
        StringBuilder resultFull = new StringBuilder();
        String idSchemeElement = "";
        
        for (int i=0; i <= idScheme.size()-1; i++) {
        	idSchemeElement = idScheme.get(i);
        	String titleStr = operType.get(idSchemeElement) + ": " + operContent.get(idSchemeElement);
        	String caption = "&nbsp;&nbsp;"+operNumber.get(idSchemeElement) + " (" + 
        		debetCode.get(idSchemeElement) + " - " + 
        		creditCode.get(idSchemeElement) + ", " + 
        		namePhase.get(idSchemeElement) + ")";
           	if (parentId.equalsIgnoreCase(idRelatedScheme.get(idSchemeElement))) {
           		if ("Y".equalsIgnoreCase(hasChild.get(idSchemeElement))) {

           	        StringBuilder tmp = new StringBuilder();
           	       	tmp.append(makeOperSchemeTree(idSchemeElement, currentId));
            		if (idSchemeElement.equalsIgnoreCase(openedParentId)) {
            			openedParentId = parentId;
            			papkaName = "../images/menu/papkaop.gif";
            		}
                	result.append("<li>"+
                    		"<b><a href=\"javascript:chhidElem(child_" + idSchemeElement + ", top_" + idSchemeElement + ")\" title=\""+titleStr+"\">"+
                    		"<img src=\""+papkaName+"\" name=\"top_" + idSchemeElement + "\" width=\"16\" height=\"16\" border=\"0\"></a>"+
                    		"<span class=\"div_menu_element\" onclick=\"ajaxpage('../crm/finance/posting_schemespecs.jsp?id=" + idSchemeElement + "', 'div_main')\" title=\""+titleStr+"\">" +
                    		caption+"</span></b>\n");
                	result.append(tmp.toString());
                	//result.append("</li>");
            		if (isEmpty(parentId)) {
           	       		openedParentId = "";
                    }
                	
           		} else {
           			result.append("<LI id=\""+ idSchemeElement +"\">");
               		result.append("<div class=\"div_menu_element\" onclick=\"ajaxpage('../crm/finance/posting_schemespecs.jsp?id=" + idSchemeElement + "', 'div_main')\" title=\""+titleStr+"\">");
               		result.append(caption+ "</div>");
          		}
           	}
        }
		if (isEmpty(parentId)) {
           	resultFull.append("<ul id=\"sheme_menu\" class=\"treeview\">\n");
        } else {
          	resultFull.append("<ul id=\"child_" + parentId + "\" rel=\"open\" class=\"extelem\">\n");
          	openedParentId = parentId;
        }
		resultFull.append(result.toString());
		resultFull.append("</ul>");
		
        return resultFull.toString();
    }
    public String getPostingsHeadHTML(String pIdReport, String pIsGrouped, String pIsClearing, String pFind, String p_beg, String p_end, String pPrint) {
        
    	ArrayList<bcFeautureParam> pParam = initParamArray();

    	String mySQL = 
    			" SELECT rn, operation_date_frmt, name_currency, " +
    			"        oper_number id_bk_operation_scheme_line, debet_cd_bk_account, credit_cd_bk_account, " +
    			"        entered_amount_frmt, assignment_posting, is_grouped_tsl, is_clearing_tsl, id_posting_detail "+
    			"   FROM (SELECT ROWNUM rn, id_posting_detail, operation_date_frmt, name_currency, " +
    			"                oper_number, debet_cd_bk_account_frmt debet_cd_bk_account, " +
    			"                credit_cd_bk_account_frmt credit_cd_bk_account,  " +
    			"                entered_amount_frmt, assignment_posting, " +
    			"                DECODE(is_grouped, " +
                "               		'Y', '<font color=\"green\"><b>'||is_grouped_tsl||'</b></font>', " +
                "               		'<font color=\"red\"><b>'||is_grouped_tsl||'</b></font>'" +
                "                ) is_grouped_tsl, " +
    	      	"                DECODE(is_clearing, " +
                "               		'Y', '<font color=\"green\"><b>'||is_clearing_tsl||'</b></font>', " +
                "               		'<font color=\"red\"><b>'||is_clearing_tsl||'</b></font>'" +
                "                ) is_clearing_tsl "+
    			"           FROM (SELECT * " +
    			"                   FROM " + getGeneralDBScheme()+".vc_acc_posting_detail_club_all " +
    			"                  WHERE 1=1 ";
   		if (!isEmpty(pIdReport)) {
   			mySQL = mySQL + " AND id_report = ? ";
   			pParam.add(new bcFeautureParam("int", pIdReport));
        }
   		if (!isEmpty(pIsGrouped)) {
   			mySQL = mySQL + " AND NVL(run_postings_export, 'N') = 'Y' AND NVL(is_grouped, '0') = ? ";
   			pParam.add(new bcFeautureParam("string", pIsGrouped));
        }
   		if (!isEmpty(pIsClearing)) {
   			mySQL = mySQL + " AND NVL(using_in_clearing, 'N') = 'Y' AND NVL(is_clearing, '0') = ? ";
   			pParam.add(new bcFeautureParam("string", pIsClearing));
        }
    	if (!isEmpty(pFind)) {
    		mySQL = mySQL + 
    				" AND (UPPER(debet_cd_bk_account) LIKE UPPER('%'||?||'%') OR " +
    				"  UPPER(credit_cd_bk_account) LIKE UPPER('%'||?||'%') OR " +
    				"  UPPER(oper_number) LIKE UPPER('%'||?||'%') OR " +
    				"  UPPER(assignment_posting) LIKE UPPER('%'||?||'%') OR " +
    				"  UPPER(operation_date_frmt) LIKE UPPER('%'||?||'%') OR " +
    				"  UPPER(entered_amount_frmt) LIKE UPPER('%'||?||'%'))";
    		for (int i=0; i<6; i++) {
    		    pParam.add(new bcFeautureParam("string", pFind));
    		}
    	}

    	pParam.add(new bcFeautureParam("int", p_end));
    	pParam.add(new bcFeautureParam("int", p_beg));
    	mySQL = mySQL + " ORDER BY id_posting_detail) WHERE ROWNUM < ?) WHERE rn >= ?";

        return getHeaderParamHTML(
            		mySQL,
            		pParam,
                    1,
                    "D",
                	0,
                	"",
                	"../crm/finance/postingspecs.jsp?id=:FIELD11:", 
                	postingXML,
                	pPrint);

    } //getPostingsHeadHTML


    public String getAccountingDocHeadHTML(String pFind, String p_beg, String p_end, String pPrint) {

    	ArrayList<bcFeautureParam> pParam = initParamArray();

    	String mySQL = 
    		" SELECT rn, number_posting, date_posting_frmt, state_posting_tsl, " +
    		"        posting_count, line_count, line_count line_count2, id_posting"+
    		"   FROM (SELECT ROWNUM rn, id_posting, number_posting, date_posting_frmt, " +
    		"                state_posting_tsl, posting_count, line_count"+
    		"           FROM (SELECT * " +
    		"                   FROM " + getGeneralDBScheme()+".vc_acc_posting_header_club_all " +
    		"                  WHERE 1=1 ";
    		
   		if (!isEmpty(pFind)) {
   			mySQL = mySQL + 
   				" AND (UPPER(number_posting) LIKE UPPER('%'||?||'%') OR " +
   				"  UPPER(date_posting_frmt) LIKE UPPER('%'||?||'%')) ";
   			for (int i=0; i<5; i++) {
   			    pParam.add(new bcFeautureParam("string", pFind));
   			}
   		}

   		pParam.add(new bcFeautureParam("int", p_end));
   		pParam.add(new bcFeautureParam("int", p_beg));
   		mySQL = mySQL + " ORDER BY number_posting DESC) WHERE ROWNUM < ?) WHERE rn >= ?";
    		
        return getHeaderParamHTML(
        		mySQL,
        		pParam,
                2,
                "D",
            	0,
            	"",
            	"../crm/finance/accounting_docspecs.jsp?type=doc&id_doc=:FIELD8:", 
            	postingXML,
            	pPrint);

    } //getPostingsHeadHTML


    public String getAccountingDocPostingHeadHTML(String pIdDoc, String pFind, String p_beg, String p_end, String pPrint) {

    	ArrayList<bcFeautureParam> pParam = initParamArray();

    	String mySQL = 
    		" SELECT rn, operation_date_frmt, debet_cd_bk_account, credit_cd_bk_account, name_currency, " +
    		"        entered_amount_frmt, id_posting_line "+
    		"   FROM (SELECT ROWNUM rn, operation_date_frmt, debet_cd_bk_account_frmt debet_cd_bk_account, " +
    		"                credit_cd_bk_account_frmt credit_cd_bk_account, name_currency, " +
    		"                entered_amount_frmt, id_posting_line "+
    		"           FROM (SELECT * " +
    		"                   FROM " + getGeneralDBScheme()+".vc_acc_posting_lines_club_all " +
    		"                  WHERE 1=1 ";
    		
   		if (!isEmpty(pIdDoc)) {
   			mySQL = mySQL + " AND id_posting = ? ";
   			pParam.add(new bcFeautureParam("int", pIdDoc));
   		}
   		if (!isEmpty(pFind)) {
   			mySQL = mySQL + 
   				" AND (UPPER(debet_cd_bk_account) LIKE UPPER('%'||?||'%') OR " +
				"  UPPER(credit_cd_bk_account) LIKE UPPER('%'||?||'%') OR " +
				"  UPPER(operation_date_frmt) LIKE UPPER('%'||?||'%') OR " +
				"  UPPER(entered_amount_frmt) LIKE UPPER('%'||?||'%'))";
   			for (int i=0; i<4; i++) {
   			    pParam.add(new bcFeautureParam("string", pFind));
   			}
   		}

   		pParam.add(new bcFeautureParam("int", p_end));
   		pParam.add(new bcFeautureParam("int", p_beg));
   		mySQL = mySQL + " ORDER BY number_posting DESC) WHERE ROWNUM < ?) WHERE rn >= ?";

        return getHeaderParamHTML(
        		mySQL,
        		pParam,
                1,
                "D",
            	0,
            	"",
            	"../crm/finance/accounting_docspecs.jsp?type=posting&id_posting=:FIELD7:", 
            	postingXML,
            	pPrint);

    } //getPostingsHeadHTML

    public String getShedulesHTML(String pTermType, String pFind, String p_beg, String p_end, String pPrint) {

    	ArrayList<bcFeautureParam> pParam = initParamArray();

    	String mySQL = 
        	" SELECT rn, name_shedule, desc_shedule, name_loyality_scheme_default, " +
        	" 	     date_beg_frmt, date_end_frmt, id_shedule " +
        	"   FROM (SELECT ROWNUM rn, id_shedule, name_shedule, desc_shedule, name_loyality_scheme_default, " +
        	" 	             date_beg_frmt, date_end_frmt " +
        	"	        FROM (SELECT * " +
        	"                   FROM " + getGeneralDBScheme()+".v_ls_shedule_name_club_all " +
        	"                  WHERE 1=1 ";

        if (!isEmpty(pTermType)) {
        	mySQL = mySQL + " AND cd_term_type = ? ";
        	pParam.add(new bcFeautureParam("string", pTermType));
        }
        if (!isEmpty(pFind)) {
    		mySQL = mySQL + " AND (UPPER(name_shedule) LIKE UPPER('%'||?||'%') OR " +
    			"UPPER(desc_shedule) LIKE UPPER('%'||?||'%') OR " +
    			"UPPER(date_beg_frmt) LIKE UPPER('%'||?||'%') OR " +
    			"UPPER(date_end_frmt) LIKE UPPER('%'||?||'%')) ";
    		for (int i=0; i<4; i++) {
    		    pParam.add(new bcFeautureParam("string", pFind));
    		}
    	}
        pParam.add(new bcFeautureParam("int", p_end));
        pParam.add(new bcFeautureParam("int", p_beg));
        mySQL = mySQL +
        	"                  ORDER BY id_shedule) " +
        	"          WHERE ROWNUM < ?" + 
        	"  ) WHERE rn >= ?";

        return getHeaderParamHTML(
        		mySQL,
        		pParam,
                1,
                "",
            	0,
            	"",
            	"../crm/clients/shedulespecs.jsp?id=:FIELD7:", 
            	sheduleXML,
            	pPrint);
    } // getShedulesHTML

    public String getDictHeadHTML(String pFind, String p_group, String p_beg, String p_end, String pPrint) {

    	ArrayList<bcFeautureParam> pParam = initParamArray();

    	String mySQL = 
    		" SELECT ROWNUM rn, dictionary_group_tsl dictionary_group_full, " +
    		"        table_name_tsl table_name_full, table_name, view_name, " +
    		"        has_translate_tsl, updated_tsl, has_translate, updated " +
        	"   FROM (SELECT * " +
        	"           FROM " + getGeneralDBScheme()+".vc_dictionary_all " +
        	"          WHERE 1=1 ";
    	if (!isEmpty(p_group)) {
    		mySQL = mySQL + " AND dictionary_group = ? ";
    		pParam.add(new bcFeautureParam("string", p_group));
        }
        if (!isEmpty(pFind)) {
    		mySQL = mySQL + 
    			" AND (UPPER(dictionary_group_tsl) LIKE UPPER('%'||?||'%') OR " +
    			"	     UPPER(table_name_tsl) LIKE UPPER('%'||?||'%') OR " +
    			"	     UPPER(table_name) LIKE UPPER('%'||?||'%') OR " +
    			"	     UPPER(view_name) LIKE UPPER('%'||?||'%')) ";
    		for (int i=0; i<4; i++) {
    		    pParam.add(new bcFeautureParam("string", pFind));
    		}
    	}
    	mySQL = mySQL + ") WHERE ROWNUM < ? ";
    	mySQL = 
    		" SELECT rn, dictionary_group_full, table_name_full, view_name, " +
    		"        has_translate_tsl, updated_tsl, has_translate, updated, table_name " +
    		"   FROM ( " +
    		mySQL +
    		" ) WHERE rn >= ? ";
    	pParam.add(new bcFeautureParam("int", p_end));
    	pParam.add(new bcFeautureParam("int", p_beg));

        return getHeaderParamHTML(
        		mySQL,
        		pParam,
                3,
                "N",
            	8,
            	"N",
            	"../crm/setup/dictionaryspecs.jsp?view_name=:FIELD4:&page=1", 
            	dictionaryXML,
            	pPrint);
    } // getDictHeadHTML

    public String getPostingSettingsHTML(String pFind, String p_beg, String p_end, String pPrint) {

    	ArrayList<bcFeautureParam> pParam = initParamArray();

    	String mySQL = 
        	"SELECT rn, desc_bk_account_scheme, " +
        	"       state_bk_account_scheme_tsl, begin_action_date, end_action_date, id_bk_account_scheme " +
  		  	"  FROM (SELECT ROWNUM rn, id_bk_account_scheme, desc_bk_account_scheme, " +
  		  	"               DECODE(state_bk_account_scheme, " +
  		  	"               		'IN_WORKING', '<font color=\"blue\"><b>'||state_bk_account_scheme_tsl||'</b></font>', " +
  		  	"               		'APPROVED', '<font color=\"green\"><b>'||state_bk_account_scheme_tsl||'</b></font>', " +
  		  	"               		'CANCELED', '<font color=\"red\"><b>'||state_bk_account_scheme_tsl||'</b></font>', " +
  		  	"               		state_bk_account_scheme_tsl" +
  		  	"               ) state_bk_account_scheme_tsl, " +
  		  	"               begin_action_date_frmt begin_action_date, " +
  		  	"               end_action_date_frmt end_action_date " +
  		  	"         FROM (SELECT * " +
  		  	"                 FROM " + getGeneralDBScheme()+".vc_bk_account_scheme_club_all ";

        if (!isEmpty(pFind)) {
    		mySQL = mySQL + " WHERE (UPPER(id_bk_account_scheme) LIKE UPPER('%'||?||'%') OR " +
    			"UPPER(desc_bk_account_scheme) LIKE UPPER('%'||?||'%') OR " +
    			"UPPER(begin_action_date) LIKE UPPER('%'||?||'%') OR " +
    			"UPPER(end_action_date) LIKE UPPER('%'||?||'%')) ";
    		for (int i=0; i<4; i++) {
    		    pParam.add(new bcFeautureParam("string", pFind));
    		}
    	}
        pParam.add(new bcFeautureParam("int", p_end));
        pParam.add(new bcFeautureParam("int", p_beg));
        mySQL = mySQL +
  		  	"                ORDER BY begin_action_date DESC)" +
  		  	"         WHERE ROWNUM < ? " + 
  		  	"       )"+
			" WHERE rn >= ? ";

        return getHeaderParamHTML(
        		mySQL,
        		pParam,
                1,
                "D",
            	0,
            	"",
            	"../crm/finance/bk_schemespecs.jsp?id=:FIELD6:", 
            	bk_schemeXML,
            	pPrint);
    } // getPostingSettingsHTML

    public String getReglamentsHeadHTML(String pBroken, String pFind, String p_beg, String p_end, String pPrint) {

    	ArrayList<bcFeautureParam> pParam = initParamArray();

    	String mySQL = 
        	" SELECT rn, id_job, name_job, desc_job, next_date, interval, broken_tsl, " +
	 		" 		 what, interval2, num_job"  +
	 		"  FROM (SELECT ROWNUM rn, id_job, name_job, desc_job, next_date, interval, " +
	 		"                DECODE(broken, " +
            "               		'W', '<font color=\"green\"><b>'||broken_tsl||'</b></font>', " +
            "               		'<font color=\"red\"><b>'||broken_tsl||'</b></font>'" +
            "                ) broken_tsl, " +
	 		" 	             what, interval interval2, num_job"  +
	 		"          FROM (SELECT * " +
	 		"                  FROM " + getGeneralDBScheme()+".vc_sys_jobs_all " +
	 		"                 WHERE 1=1 ";
        if (!isEmpty(pBroken)) {
        	mySQL = mySQL + " AND broken = ? ";
        	pParam.add(new bcFeautureParam("string", pBroken));
        }
        if (!isEmpty(pFind)) {
    		mySQL = mySQL + 
    			" AND (UPPER(TO_CHAR(id_job)) LIKE UPPER('%'||?||'%') OR " +
    			"	     UPPER(name_job) LIKE UPPER('%'||?||'%') OR " +
    			"	     UPPER(desc_job) LIKE UPPER('%'||?||'%') OR " +
    			"	     UPPER(next_date) LIKE UPPER('%'||?||'%') OR " +
    			"	     UPPER(interval) LIKE UPPER('%'||?||'%') OR " +
    			"	     UPPER(what) LIKE UPPER('%'||?||'%')) ";
    		for (int i=0; i<6; i++) {
    		    pParam.add(new bcFeautureParam("string", pFind));
    		}
    	}
        pParam.add(new bcFeautureParam("int", p_end));
        pParam.add(new bcFeautureParam("int", p_beg));
        mySQL = mySQL +
	 		 "                 ORDER BY name_job )" +
	 		 "         WHERE ROWNUM < ? " + 
	 		 ") WHERE rn >= ? ";

        return getHeaderParamHTML(
        		mySQL,
        		pParam,
                0,
                "D",
            	0,
            	"",
            	"../crm/security/reglamentspecs.jsp?id=:FIELD2:", 
            	reglamentXML,
            	pPrint);
    } //getReglamentsHeadHTML

    public String getRolesHTML(String id_member, String pFind, String p_beg, String p_end, String pPrint) {

    	ArrayList<bcFeautureParam> pParam = initParamArray();

    	String mySQL =
        	" SELECT rn, partner, name_role, desc_role, name_module_type, id_role " +
			"   FROM (SELECT ROWNUM rn, id_role, name_role, desc_role, sname_jur_prs partner, " +
			"  		         DECODE(cd_module_type, " +
			"               	 	'WEBCLIENT', '<font color=\"blue\"><b>'||name_module_type||'</b></font>', " +
            "               	 	'WEBPOS', '<font color=\"blue\"><b>'||name_module_type||'</b></font>', " +
            "               		'CRM', '<font color=\"red\"><b>'||name_module_type||'</b></font>', " +
            "               		'SYSTEM', '<font color=\"red\"><b>'||name_module_type||'</b></font>', " +
            "               		'PRIVATE_OFFICE', '<font color=\"green\"><b>'||name_module_type||'</b></font>', " +
            "          		        name_module_type" +
            "  		         ) name_module_type " +
    		"           FROM (SELECT * " +
    		"                   FROM " + getGeneralDBScheme()+".vc_role_all " +
    		"                  WHERE 1=1 ";
        if (!isEmpty(id_member)) {
        	mySQL = mySQL + " AND cd_module_type = ? ";
        	pParam.add(new bcFeautureParam("string", id_member));
        }
        if (!isEmpty(pFind)) {
    		mySQL = mySQL + 
    			" AND (UPPER(TO_CHAR(id_role)) LIKE UPPER('%'||?||'%') OR " +
    			"	     UPPER(name_role) LIKE UPPER('%'||?||'%') OR " +
    			"	     UPPER(desc_role) LIKE UPPER('%'||?||'%')) ";
    		for (int i=0; i<3; i++) {
    		    pParam.add(new bcFeautureParam("string", pFind));
    		}
    	}
        pParam.add(new bcFeautureParam("int", p_end));
        pParam.add(new bcFeautureParam("int", p_beg));
        mySQL = mySQL + " ORDER BY sname_jur_prs, name_role) WHERE ROWNUM < ?) WHERE rn >= ?";

        return getHeaderParamHTML(
        		mySQL,
        		pParam,
                1,
                "",
            	0,
            	"",
            	"../crm/security/rolespecs.jsp?id=:FIELD6:", 
            	roleXML,
            	pPrint);
    } // getRolesHTML()

    public String getSettingsHTML(String pFind, String p_beg, String p_end, String pPrint) {

    	ArrayList<bcFeautureParam> pParam = initParamArray();

    	String mySQL = 
        	" SELECT rn, cd_param, name_param, value_param_tsl, date_param_frmt, " +
        	"        is_used, default_value, name_lookup_type "+
        	"  FROM (SELECT ROWNUM rn, cd_param, name_param, value_param_tsl, date_param_frmt, " +
        	"               is_used, default_value, name_lookup_type "+
        	"          FROM (SELECT * " +
        	"                  FROM " + getGeneralDBScheme()+".vc_system_param ";
        if (!isEmpty(pFind)) {
    		mySQL = mySQL + 
    			" WHERE (UPPER(TO_CHAR(cd_param)) LIKE UPPER('%'||?||'%') OR " +
    			"	     UPPER(name_param) LIKE UPPER('%'||?||'%') OR " +
    			"	     UPPER(value_param_tsl) LIKE UPPER('%'||?||'%') OR " +
    			"	     UPPER(date_param_frmt) LIKE UPPER('%'||?||'%') OR " +
    			"	     UPPER(name_lookup_type) LIKE UPPER('%'||?||'%')) ";
    		for (int i=0; i<5; i++) {
    		    pParam.add(new bcFeautureParam("string", pFind));
    		}
    	}
        pParam.add(new bcFeautureParam("int", p_end));
        pParam.add(new bcFeautureParam("int", p_beg));
        mySQL = mySQL + 
        	"                 ORDER BY name_param)" +
        	"         WHERE ROWNUM < ? " + 
        	" ) WHERE rn >= ?";

        return getHeaderParamHTML(
        		mySQL,
        		pParam,
                0,
                "",
            	0,
            	"",
            	"../crm/setup/settingspecs.jsp?id=:FIELD2:",
            	settingXML,
            	pPrint);
    } // getSettingsHTML()

    public String getUsersHTML(String cdUserStatus, String pFind, String p_beg, String p_end, String pPrint) {

    	ArrayList<bcFeautureParam> pParam = initParamArray();

    	String mySQL =
        	" SELECT rn, name_user, fio_nat_prs, sname_service_place_work, phone_mobile, " +
        	"		 email_work, name_user_status, id_user " +
	  		"   FROM (SELECT ROWNUM rn, name_user, fio_nat_prs, sname_service_place_work, phone_mobile, " +
	  		"  		         email_work, " +
	  		"  		         DECODE(cd_user_status, " +
            "         	 	        'OPENED', '<font color=\"green\"><b>'||name_user_status||'</b></font>', " +
            "          	 	        'CLOSED', '<font color=\"gray\"><b>'||name_user_status||'</b></font>', " +
            "          		        'BLOCKED', '<font color=\"red\"><b>'||name_user_status||'</b></font>', " +
            "          		        'DELETED', '<font color=\"red\"><b>'||name_user_status||'</b></font>', " +
            "          		        name_user_status" +
            "  		         ) name_user_status, " +
            "                id_user  " +
  			"           FROM (SELECT * " +
  			"                   FROM " + getGeneralDBScheme()+".vc_users_all " +
  			"                  WHERE 1=1 ";
        if (!isEmpty(cdUserStatus)) {
        	mySQL = mySQL + " AND cd_user_status = ? ";
        	pParam.add(new bcFeautureParam("string", cdUserStatus));
        }
        if (!isEmpty(pFind)) {
    		mySQL = mySQL + 
    			" AND (UPPER(TO_CHAR(id_user)) LIKE UPPER('%'||?||'%') OR " +
    			"	     UPPER(name_user) LIKE UPPER('%'||?||'%') OR " +
    			"	     UPPER(phone_mobile) LIKE UPPER('%'||?||'%') OR " +
    			"	     UPPER(email_work) LIKE UPPER('%'||?||'%') OR " +
    			"	     UPPER(fio_nat_prs) LIKE UPPER('%'||?||'%')) ";
    		for (int i=0; i<5; i++) {
    		    pParam.add(new bcFeautureParam("string", pFind));
    		}
    	}
        pParam.add(new bcFeautureParam("int", p_end));
        pParam.add(new bcFeautureParam("int", p_beg));
        mySQL = mySQL +
  			"                  ORDER BY UPPER(name_user)) " +
  			"          WHERE ROWNUM < ? " + 
  			"        ) " +
  			"  WHERE rn >= ?";

        return getHeaderParamHTML(
        		mySQL,
        		pParam,
                1,
                "D",
            	0,
            	"",
            	"../crm/security/userspecs.jsp?id=:FIELD8:&user=:FIELD2:", 
            	userXML,
            	pPrint);
    } // getUsersHTML()

    public String getAccountsHTML(String id_type, String pFind, String p_beg, String p_end, String pPrint) {

    	ArrayList<bcFeautureParam> pParam = initParamArray();

    	String mySQL =
        	"SELECT rn, number_bank_account, name_bank_alt, name_currency, " +
			"		name_bank_account_type,  name_owner_bank_account, id_bank_account " +
			"  FROM (SELECT ROWNUM rn, id_bank_account, number_bank_account, name_currency, " +
    		"		        sname_bank name_bank_alt, " +
    		"               DECODE(cd_bank_account_type, " +
            "         	 	        'SETTLEMENT_ACCOUNT', '<font color=\"green\"><b>'||name_bank_account_type||'</b></font>', " +
            "          	 	        'CORRESPONDENT_ACCOUNT', '<font color=\"blue\"><b>'||name_bank_account_type||'</b></font>', " +
            "          		        name_bank_account_type" +
            "  		        ) name_bank_account_type, " +
    		"               name_owner_bank_account " +
    		"          FROM (SELECT * " +
    		"                  FROM " + getGeneralDBScheme()+".vc_bank_account_priv_all " +
    		"				  WHERE 1=1 ";
    
        if (!isEmpty(id_type)) {
        	mySQL = mySQL + " AND cd_bank_account_type = ? ";
        	pParam.add(new bcFeautureParam("string", id_type));
        }
        if (!isEmpty(pFind)) {
    		mySQL = mySQL + " AND (UPPER(id_bank_account) LIKE UPPER('%'||?||'%') OR " +
    			"UPPER(number_bank_account) LIKE UPPER('%'||?||'%') OR " +
    			"UPPER(sname_bank) LIKE UPPER('%'||?||'%') OR " +
    			"UPPER(desc_bank_account) LIKE UPPER('%'||?||'%') OR " + 
    			"UPPER(name_owner_bank_account) LIKE UPPER('%'||?||'%')) ";
    		for (int i=0; i<5; i++) {
    		    pParam.add(new bcFeautureParam("string", pFind));
    		}
    	}
        pParam.add(new bcFeautureParam("int", p_end));
        pParam.add(new bcFeautureParam("int", p_beg));
        mySQL = mySQL + " ORDER BY number_bank_account) WHERE ROWNUM < ?) WHERE rn >= ?";

        return getHeaderParamHTML(
        		mySQL,
        		pParam,
                1,
                "D",
            	0,
            	"",
            	"../crm/clients/accountspecs.jsp?id=:FIELD7:", 
            	accountXML,
            	pPrint);
    } // getAccountsHTML()
    
    public String getReportsListHTML(String pFind, String pModuleType, String pReportKind, String p_beg, String p_end, String pPrint) {

    	ArrayList<bcFeautureParam> pParam = initParamArray();

    	String mySQL = 
        	" SELECT /*rn, */ id_report, /*cd_report,*/ name_report, desc_report, exec_file, name_module_type, name_report_kind, " +
        	"        cd_report_state, id_menu_element " +
     		"   FROM (SELECT ROWNUM rn, id_report, cd_report, name_report, desc_report, exec_file, cd_report_state," +
     		"                name_module_type, name_report_kind, id_menu_element " +
     		"           FROM (SELECT * " +
     		"                   FROM " + getGeneralDBScheme()+".vc_user_reports_all a " +
     		"                  WHERE id_menu_element = 730 ";
    	if (!isEmpty(pModuleType)) {
    		mySQL = mySQL + " AND cd_module_type = ? ";
        	pParam.add(new bcFeautureParam("string", pModuleType));
    	}
    	if (!isEmpty(pReportKind)) {
    		mySQL = mySQL + " AND cd_report_kind = ? ";
        	pParam.add(new bcFeautureParam("string", pReportKind));
    	}

        if (!isEmpty(pFind)) {
    		mySQL = mySQL + 
    			" AND (UPPER(TO_CHAR(id_report)) LIKE UPPER('%'||?||'%') OR " +
    			"	     UPPER(cd_report) LIKE UPPER('%'||?||'%') OR " +
    			"	     UPPER(name_report) LIKE UPPER('%'||?||'%') OR " +
    			"	     UPPER(desc_report) LIKE UPPER('%'||?||'%') OR " +
    			"	     UPPER(exec_file) LIKE UPPER('%'||?||'%')) ";
    		for (int i=0; i<5; i++) {
    		    pParam.add(new bcFeautureParam("string", pFind));
    		}
    	}
        pParam.add(new bcFeautureParam("int", p_end));
        pParam.add(new bcFeautureParam("int", p_beg));
        mySQL = mySQL + 
     		"                  ORDER BY name_report)" +
     		"          WHERE ROWNUM < ? " + 
     		" ) WHERE rn >= ?";

        StringBuilder html = new StringBuilder();
        PreparedStatement st = null; 
        
        boolean hasSelectedMenuAccessPermission = false;
        
        try{
       	 	if (isEditMenuPermited("REPORTS_SELECTED")>=0) {
       	 	hasSelectedMenuAccessPermission = true;
        	}
            LOGGER.debug(prepareSQLToLog(mySQL, pParam));
       	 	con = Connector.getConnection(getSessionId());
            st = con.prepareStatement(mySQL);
            st = prepareParam(st, pParam);
            ResultSet rset = st.executeQuery();
            ResultSetMetaData mtd = rset.getMetaData();
            
            int colCount = mtd.getColumnCount();
            if (hasSelectedMenuAccessPermission && "N".equalsIgnoreCase(pPrint)) {
            	html.append("<form method=\"Post\" name=\"updateForm\" id=\"updateForm\" action=\"../crm/reports/rep_update.jsp\" onSubmit=\"return CheckSelect(this);\">\n");
            	html.append("<input type=\"hidden\" name=\"type\" value=\"to_selected\">\n");
           	 	html.append("<input type=\"hidden\" name=\"action\" value=\"set\">\n");
           	 	html.append("<input type=\"hidden\" name=\"process\" value=\"yes\">\n");
           	 	/*html.append("<input type=\"hidden\" name=\"id_menu_element\" value=\""+id_menu_element+"\">\n");*/
            }
            
            if ("Y".equalsIgnoreCase(pPrint)) {
            	html.append(getPrintTable());
            } else {
            	html.append(getHeaderTable());
            }
            html.append("<thead><tr>\n");
            if (hasSelectedMenuAccessPermission && "N".equalsIgnoreCase(pPrint)) {
           	 	html.append("<th>");
           	 	html.append(getSubmitButtonAjax3("../crm/reports/rep_update.jsp", "button_to_selected", ""));
            	html.append("<br><input name=\"mainCheck\" id=\"mainCheck\" type=\"checkbox\" onclick=\"CheckAll(this,'chb')\"></th>\n");
            } 
       
            for (int i=1; i <= colCount-2; i++) {
               	html.append(getBottomFrameTableTH(reportXML, mtd.getColumnName(i)));
            }
            html.append("</tr></thead>\n");
            
            html.append("<tbody>\n");
            
            while (rset.next()) {
	           	 html.append("<tr id=\"event_"+rset.getString("ID_REPORT")+"\" onmouseout=\"coloredOut(this)\" onmouseover=\"coloredOver(this,1)\">");
	           	 if (hasSelectedMenuAccessPermission && "N".equalsIgnoreCase(pPrint)) {
	           		 html.append("<td align=\"center\"><input type=\"checkbox\" name=\"chb"+rset.getString("ID_REPORT")+"\" value=\"\" id=\"chb"+rset.getString("ID_REPORT")+"\" onclick=\"return CheckCB(this);\"></td>\n");
	           	 } 
	           	for (int i=1; i <= colCount-2; i++) {
	           		if (!("WORKS".equalsIgnoreCase(rset.getString("CD_REPORT_STATE")))) {
	           			html.append("<td style=\"color:gray;\">"+getHyperLinkHeaderFirst()+"../crm/reports/rep_specs2.jsp?id="+rset.getString("ID_REPORT")+"&type=all" + getHyperLinkMiddle() + getValue2(rset.getString(i)) + getHyperLinkEnd() + "</td>\n");
	           		} else {
	           			html.append("<td>"+getHyperLinkHeaderFirst()+"../crm/reports/rep_specs2.jsp?id="+rset.getString("ID_REPORT")+"&type=all" + getHyperLinkMiddle() + getValue2(rset.getString(i)) + getHyperLinkEnd() + "</td>\n");
	           		}
	           	}
	           	html.append("</tr>");
            }
            html.append("</tbody></table>\n");
            html.append("</form\n");
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
        
    } // getReportsListHTML
    
    
    public String getReportsSelectedHTML(String pFind, String pModuleType, String pReportKind, String p_beg, String p_end, String pPrint) {

    	ArrayList<bcFeautureParam> pParam = initParamArray();

    	String mySQL = 
    		" SELECT /*rn, */ id_report, /*cd_report,*/ name_report, desc_report, exec_file, name_module_type, name_report_kind, " +
        	"        cd_report_state, id_menu_element " +
     		"   FROM (SELECT ROWNUM rn, id_report, cd_report, name_report, desc_report, exec_file, cd_report_state," +
     		"                name_module_type, name_report_kind, id_menu_element " +
     		"           FROM (SELECT * " +
     		"                   FROM " + getGeneralDBScheme()+".vc_user_sel_reports_all a " +
     		"                  WHERE 1=1 ";
        if (!isEmpty(pModuleType)) {
        	mySQL = mySQL + " AND cd_module_type = ? ";
           	pParam.add(new bcFeautureParam("string", pModuleType));
        }
        if (!isEmpty(pReportKind)) {
        	mySQL = mySQL + " AND cd_report_kind = ? ";
           	pParam.add(new bcFeautureParam("string", pReportKind));
        }

        if (!isEmpty(pFind)) {
    		mySQL = mySQL + 
    			" AND (UPPER(TO_CHAR(id_report)) LIKE UPPER('%'||?||'%') OR " +
    			"	     UPPER(cd_report) LIKE UPPER('%'||?||'%') OR " +
    			"	     UPPER(name_report) LIKE UPPER('%'||?||'%') OR " +
    			"	     UPPER(desc_report) LIKE UPPER('%'||?||'%') OR " +
    			"	     UPPER(exec_file) LIKE UPPER('%'||?||'%')) ";
    		for (int i=0; i<5; i++) {
    		    pParam.add(new bcFeautureParam("string", pFind));
    		}
    	}
        pParam.add(new bcFeautureParam("int", p_end));
        pParam.add(new bcFeautureParam("int", p_beg));
        mySQL = mySQL + 
     		"                  ORDER BY name_report)" +
     		"          WHERE ROWNUM < ? " + 
     		" ) WHERE rn >= ?";

        StringBuilder html = new StringBuilder();
        PreparedStatement st = null; 
        
        boolean hasSelectedMenuAccessPermission = false;
        
        try{
       	 	if (isEditMenuPermited("REPORTS_SELECTED")>0) {
       	 		hasSelectedMenuAccessPermission = true;
        	}
            LOGGER.debug(prepareSQLToLog(mySQL, pParam));
       	 	con = Connector.getConnection(getSessionId());
            st = con.prepareStatement(mySQL);
            st = prepareParam(st, pParam);
            ResultSet rset = st.executeQuery();
            ResultSetMetaData mtd = rset.getMetaData();
            
            int colCount = mtd.getColumnCount();
            if (hasSelectedMenuAccessPermission && "N".equalsIgnoreCase(pPrint)) {
            	html.append("<form method=\"Post\" name=\"updateForm\" id=\"updateForm\" action=\"../crm/reports/rep_update.jsp\" onSubmit=\"return CheckSelect(this);\">\n");
            	html.append("<input type=\"hidden\" name=\"type\" value=\"to_selected\">\n");
           	 	html.append("<input type=\"hidden\" name=\"action\" value=\"delete\">\n");
           	 	html.append("<input type=\"hidden\" name=\"process\" value=\"yes\">\n");
            }
            
            if ("Y".equalsIgnoreCase(pPrint)) {
            	html.append(getPrintTable());
            } else {
            	html.append(getHeaderTable());
            }
            html.append("<thead><tr>\n");
            if (hasSelectedMenuAccessPermission && "N".equalsIgnoreCase(pPrint)) {
           	 	html.append("<th>");
           	 	html.append(getSubmitButtonAjax3("../crm/reports/rep_update.jsp", "button_from_selected", ""));
            	html.append("<br><input name=\"mainCheck\" id=\"mainCheck\" type=\"checkbox\" onclick=\"CheckAll(this,'chb')\"></th>\n");
            } 
       
            for (int i=1; i <= colCount-2; i++) {
               	html.append(getBottomFrameTableTH(reportXML, mtd.getColumnName(i)));
            }
            html.append("</tr></thead>\n");
            
            html.append("<tbody>\n");
            
            while (rset.next()) {
	           	 html.append("<tr id=\"event_"+rset.getString("ID_REPORT")+"\" onmouseout=\"coloredOut(this)\" onmouseover=\"coloredOver(this,1)\">");
	           	 if (hasSelectedMenuAccessPermission && "N".equalsIgnoreCase(pPrint)) {
	           		 html.append("<td align=\"center\"><input type=\"checkbox\" name=\"chb"+rset.getString("ID_REPORT")+"\" value=\"\" id=\"chb"+rset.getString("ID_REPORT")+"\" onclick=\"return CheckCB(this);\"></td>\n");
	           	 } 
		           	for (int i=1; i <= colCount-2; i++) {
		           		if (!("WORKS".equalsIgnoreCase(rset.getString("CD_REPORT_STATE")))) {
		           			html.append("<td style=\"color:gray;\">"+getHyperLinkHeaderFirst()+"../crm/reports/rep_specs2.jsp?id="+rset.getString("ID_REPORT")+"&type=selected"+getHyperLinkMiddle() + getValue2(rset.getString(i)) + getHyperLinkEnd() + "</td>\n");
		           		} else {
		           			html.append("<td>"+getHyperLinkHeaderFirst()+"../crm/reports/rep_specs2.jsp?id="+rset.getString("ID_REPORT")+"&type=selected" + getHyperLinkMiddle() + getValue2(rset.getString(i)) + getHyperLinkEnd() + "</td>\n");
		           		}
		           	}
	           	html.append("</tr>");
            }
            html.append("</tbody></table>\n");
            html.append("</form\n");
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
        
    } // getReportsListHTML
    
    private String returnRowCount = "0";
    
    public String getLastResultSetRowCount() {
    	return returnRowCount;
    }
    
    private void setLastResultSetRowCount(String pRowCount) {
    	returnRowCount = pRowCount;
    }
    
    private String formatSysEventParam(String pValue) {
    	return pValue;
    }

    public String getEventsHTML(String pFind, String p_id_type, String p_beg, String p_end, String pPrint) {
        StringBuilder html = new StringBuilder();
        PreparedStatement st = null; 

        ArrayList<bcFeautureParam> pParam = initParamArray();

        String mySQL = 
        	" SELECT rn, creation_date, desc_event_type, desc_event, " +
        	"        param_event, cd_event_type, id_event, row_count " +
        	"  FROM (SELECT ROWNUM rn, id_event, creation_date_frmt creation_date, " +
        	"               desc_event_type, " +
        	"				desc_event, param_event, " +
        	"				cd_event_type, " +
        	"				row_count " +
        	"          FROM (SELECT a.*, count(*) over () as row_count " +
        	"                  FROM " + getGeneralDBScheme()+".VC_SYS_EVENT a " +
        	"                 WHERE 1=1 ";
        
        if (!isEmpty(p_id_type)) {
       	 	mySQL = mySQL + " AND cd_event_type = ? ";
       	 	pParam.add(new bcFeautureParam("string", p_id_type));
        }
        if (!isEmpty(pFind)) {
    		mySQL = mySQL + 
    			" AND (UPPER(id_event) LIKE UPPER('%'||?||'%') OR " +
    			"	     UPPER(creation_date_frmt) LIKE UPPER('%'||?||'%') OR " +
    			"	     UPPER(desc_event) LIKE UPPER('%'||?||'%') OR " +
    			"	     UPPER(param_event) LIKE UPPER('%'||?||'%')) ";
    		for (int i=0; i<4; i++) {
    		    pParam.add(new bcFeautureParam("string", pFind));
    		}
    	}

        pParam.add(new bcFeautureParam("int", p_end));
        pParam.add(new bcFeautureParam("int", p_beg));
        mySQL = mySQL + " ORDER BY id_event DESC) WHERE ROWNUM < ? ) WHERE rn >= ? ";
        
        boolean hasEditPermission = false;
        
        String myFont = "";
        String myFontEnd = "";
        
        boolean isSetRowCount = false;
        
        try{
        	setLastResultSetRowCount("0");
       	 	if (isEditMenuPermited("SETUP_EVENTS")>0) {
       	 		hasEditPermission = true;
        	}
            LOGGER.debug(prepareSQLToLog(mySQL, pParam));
       	 	con = Connector.getConnection(getSessionId());
            st = con.prepareStatement(mySQL);
            st = prepareParam(st, pParam);
            ResultSet rset = st.executeQuery();
            ResultSetMetaData mtd = rset.getMetaData();
            
            int colCount = mtd.getColumnCount();
            if (hasEditPermission && "N".equalsIgnoreCase(pPrint)) {
            	html.append("<form method=\"Post\" name=\"updateForm\" id=\"updateForm\" action=\"../crm/setup/eventsupdate.jsp\" onSubmit=\"return CheckSelect(this);\">\n");
           	 	html.append("<input type=\"hidden\" name=\"action\" value=\"delete\">\n");
           	 	html.append("<input type=\"hidden\" name=\"process\" value=\"yes\">\n");
            }
            
            if ("Y".equalsIgnoreCase(pPrint)) {
            	html.append(getPrintTable());
            } else {
            	html.append(getHeaderTable());
            }
            html.append("<thead><tr id=\"table_head\">\n");
            if (hasEditPermission && "N".equalsIgnoreCase(pPrint)) {
           	 	html.append("<th>");
           	 	html.append(getSubmitButtonAjax3("../crm/setup/eventsupdate.jsp", "button_delete", "CheckSelect(document.getElementById('updateForm'))"));
            	html.append("<br><input name=\"mainCheck\" id=\"mainCheck\" type=\"checkbox\" onclick=\"CheckAll(this,'chb')\"></th>\n");
            } 
       
            for (int i=1; i <= colCount-3; i++) {
               	html.append(getBottomFrameTableTH(eventXML, mtd.getColumnName(i)));
            }
            html.append("</tr></thead>\n");

            //html.append("</div>\n");
            //html.append("<div id=\"div_data\">\n");
            //html.append("<div id=\"div_data_detail\">\n");
            html.append("<tbody>\n");
            
            while (rset.next())
            {
            	if (!isSetRowCount) {
            		setLastResultSetRowCount(rset.getString("ROW_COUNT"));
            		isSetRowCount = true;
            	}
           	 html.append("<tr id=\"event_"+rset.getString("ID_EVENT")+"\" onmouseout=\"coloredOut(this)\" onmouseover=\"coloredOver(this,1)\">");
           	 if (hasEditPermission && "N".equalsIgnoreCase(pPrint)) {
           		 html.append("<td align=\"center\"><input type=\"checkbox\" name=\"chb"+rset.getString("ID_EVENT")+"\" value=\"\" id=\"chb"+rset.getString("ID_EVENT")+"\" onclick=\"return CheckCB(this);\"></td>\n");
           	 } 
             if ("C".equalsIgnoreCase(rset.getString("CD_EVENT_TYPE")) ||
                		"E".equalsIgnoreCase(rset.getString("CD_EVENT_TYPE")) ||
                		"S".equalsIgnoreCase(rset.getString("CD_EVENT_TYPE"))) {
                	myFont = "<b><font color=\"red\">";
                	myFontEnd = "</font></b>";
                } else if ("M".equalsIgnoreCase(rset.getString("CD_EVENT_TYPE"))) {
                	myFont = "<font color=\"green\">";
                	myFontEnd = "</font>";
                } else if ("W".equalsIgnoreCase(rset.getString("CD_EVENT_TYPE"))) {
                	myFont = "<b><font color=\"blue\">";
                	myFontEnd = "</font></b>";
                } else {
                	myFont = "";
                	myFontEnd = "";
                }
                html.append(
                		//"<td>"+getHyperLinkHeaderFirst()+"../crm/setup/eventspecs.jsp?id="+rset.getString("ID_EVENT")+getHyperLinkMiddle() + myFont + getValue2(rset.getString(1)) + myFontEnd + getHyperLinkEnd() + "</td>\n" +
                		"<td>"+getHyperLinkHeaderFirst()+"../crm/setup/eventspecs.jsp?id="+rset.getString("ID_EVENT")+getHyperLinkMiddle() + myFont + getValue2(rset.getString(1)) + myFontEnd + getHyperLinkEnd() + "</td>\n" +
                		"<td>"+getHyperLinkHeaderFirst()+"../crm/setup/eventspecs.jsp?id="+rset.getString("ID_EVENT")+getHyperLinkMiddle() + myFont + getValue2(rset.getString(2)) + myFontEnd + getHyperLinkEnd() + "</td>\n" + 
                		"<td>"+getHyperLinkHeaderFirst()+"../crm/setup/eventspecs.jsp?id="+rset.getString("ID_EVENT")+getHyperLinkMiddle() + myFont + getValue2(rset.getString(3)) + myFontEnd + getHyperLinkEnd() + "</td>\n" + 
                		"<td>"+getHyperLinkHeaderFirst()+"../crm/setup/eventspecs.jsp?id="+rset.getString("ID_EVENT")+getHyperLinkMiddle() + myFont + getValue2(rset.getString(4)) + myFontEnd + getHyperLinkEnd() + "</td>\n" + 
                		"<td>" + myFont + formatSysEventParam(rset.getString(5)) + myFontEnd + "</td>\n"
                );
            }
            html.append("</tbody></table>\n");
            html.append("</form\n");
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
       } // getEventsHTML

    public String getPartnerPatternsHeaderHTML(String pPath, String pFind, String pCdPatternType, String p_beg, String p_end, String pPrint) {

    	ArrayList<bcFeautureParam> pParam = initParamArray();

    	String mySQL = 
        	" SELECT rn, name_pattern, " +
			"        name_pattern_status, name_pattern_type, name_dispatch_type, " +
			"        partner_profile, begin_action_date, end_action_date, can_send_partner_tsl, id_pattern " +
			"   FROM (SELECT ROWNUM rn, id_pt_pattern id_pattern, " +
			"                name_pt_pattern name_pattern, " +
			"                DECODE(cd_pattern_status, " +
        	"                       'ACTIVE', '<font color=\"black\">'||name_pattern_status||'</font>', " +
        	"                       'SUSPENDED', '<font color=\"brown\"><b>'||name_pattern_status||'</b></font>', " +
        	"                       'CANCEL', '<font color=\"red\"><b>'||name_pattern_status||'</b></font>', " +
        	"                       'FINISH', '<font color=\"green\"><b>'||name_pattern_status||'</b></font>', " +
        	"                       name_pattern_status) name_pattern_status, " +
	  		"                name_pattern_type, name_dispatch_type, " +
			"                name_ds_pt_profile partner_profile, begin_action_date_frmt begin_action_date, " +
			"                end_action_date_frmt end_action_date,  " + 
			"                DECODE(can_send_email, " +
			"                       'N', '<font color=\"black\">'||can_send_email_tsl||'</font>', " +
			"                       'Y', '<font color=\"red\"><b>'||can_send_email_tsl||'</b></font>', " +
			"                       can_send_email_tsl)||'/'||" +
			"                DECODE(can_send_office, " +
			"                       'N', '<font color=\"black\">'||can_send_office_tsl||'</font>', " +
			"                       'Y', '<font color=\"red\"><b>'||can_send_office_tsl||'</b></font>', " +
			"                       can_send_office_tsl)||'/'||" +
			"                DECODE(can_send_term, " +
			"                       'N', '<font color=\"black\">'||can_send_term_tsl||'</font>', " +
			"                       'Y', '<font color=\"red\"><b>'||can_send_term_tsl||'</b></font>', " +
			"                       can_send_term_tsl) can_send_partner_tsl " +
		  	"          FROM (SELECT * " +
		  	"                  FROM " + getGeneralDBScheme()+".vc_ds_pt_pattern_club_all " +
		  	"                 WHERE 1=1 ";
        if (!isEmpty(pCdPatternType)) {
    		mySQL = mySQL + " AND cd_pattern_type = ? ";
    		pParam.add(new bcFeautureParam("string", pCdPatternType));
    	}
        if (!isEmpty(pPath)) {
        	if ("EMAIL".equalsIgnoreCase(pPath)) {
        		mySQL = mySQL + " AND can_send_email = 'Y' ";
        	} else if ("OFFICE".equalsIgnoreCase(pPath)) {
        		mySQL = mySQL + " AND can_send_office = 'Y' ";
        	} else if ("TERM".equalsIgnoreCase(pPath)) {
        		mySQL = mySQL + " AND can_send_term = 'Y' ";
        	}
    	}
        if (!("".equalsIgnoreCase(pFind))) {
    		mySQL = mySQL + 
	    		" AND (UPPER(TO_CHAR(id_pt_pattern)) LIKE UPPER('%'||?||'%') OR " +
				"	   UPPER(name_pt_pattern) LIKE UPPER('%'||?||'%') OR " +
				"	   UPPER(basis_for_operation) LIKE UPPER('%'||?||'%') OR " +
				"	   UPPER(begin_action_date_frmt) LIKE UPPER('%'||?||'%') OR " +
				"	   UPPER(end_action_date_frmt) LIKE UPPER('%'||?||'%')) ";
    		for (int i=0; i<5; i++) {
    		    pParam.add(new bcFeautureParam("string", pFind));
    		}
    	}
        pParam.add(new bcFeautureParam("int", p_end));
        pParam.add(new bcFeautureParam("int", p_beg));
        mySQL = mySQL +
		  	"                ORDER BY begin_action_date DESC) " +
		  	"         WHERE ROWNUM < ?" +  
		  	"        )"+
		  	" WHERE rn >= ?";

    	return getHeaderParamHTML(
        		mySQL,
        		pParam,
                1,
                "D",
            	0,
            	"",
            	"../crm/dispatch/patterns/partner_patternspecs.jsp?id=:FIELD10:", 
            	messageXML,
            	pPrint);
    } // getMessagesHeaderHTML

    public String getTermMessagesHeaderHTML(String pOperationType, String pFind, String pType, String pState, String pIsArchive, String p_beg, String p_end, String pPrint) {
    	
    	ArrayList<bcFeautureParam> pParam = initParamArray();

    	String mySQL = 
        	" SELECT id_term_message id_message, name_term_message_type, " +
        	"        receivers_count, text_term_message text_message, " +
    		"        begin_action_date_frmt, end_action_date_frmt, repetitions_count, " +
			"        sendings_quantity, last_send_date_frmt, name_term_message_state, is_archive_tsl " +
			"   FROM (SELECT ROWNUM rn, id_term_message, name_term_message_type, " +
			"                receivers_count, text_term_message, " +
    		"        		 begin_action_date_frmt, end_action_date_frmt, repetitions_count, " +
			"        		 sendings_quantity, last_send_date_frmt, name_term_message_state, is_archive_tsl " +
			"           FROM (SELECT a.id_term_message, " +
			"						 DECODE(cd_term_message_type, " +
			"                           	'CARD_REQUEST', '<b><font color=\"blue\">'||name_term_message_type||'</font></b>', " +
			"                           	'TERMINAL_PARAMETERS', '<b><font color=\"green\">'||name_term_message_type||'</font></b>', " +
			"                           	name_term_message_type" +
			"                  		 ) name_term_message_type, " +
			"						 CASE WHEN NVL(receivers_count, 0) = 0 " +
            "                             THEN '<b><font color=\"red\"><blink>'||receivers_count||'</blink></font></b>' " +
            "                             ELSE TO_CHAR(receivers_count)" +
            "                        END receivers_count, a.text_term_message, " +
			"                        a.begin_action_date_frmt, " +
			"                        a.end_action_date_frmt, " +
			"                        a.repetitions_count, " +
			"						 CASE WHEN NVL(sendings_quantity, 0) > 0 " +
            "                             THEN '<b><font color=\"red\">'||sendings_quantity||'</font></b>' " +
            "                             ELSE TO_CHAR(sendings_quantity)" +
            "                        END sendings_quantity, a.last_send_date_frmt, " +
			"               	     DECODE(cd_term_message_state, " +
  		  	"                      	  		'PREPARED', '<b><font color=\"black\">'||name_term_message_state||'</font></b>', " +
			"                      			'NEW', '<font color=\"black\">'||name_term_message_state||'</font>', " +
			"                      			'EXECUTED', '<font color=\"green\">'||name_term_message_state||'</font>', " +
			"                      			'EXECUTED_PARTIALLY', '<font color=\"darkgreen\">'||name_term_message_state||'</font>', " +
			"                      			'ERROR', '<b><font color=\"red\">'||name_term_message_state||'</font></b>', " +
			"                      			'CARRIED_OUT_PARTIALLY', '<b><font color=\"darkgray\">'||name_term_message_state||'</font></b>', " +
			"                      			'CARRIED_OUT', '<font color=\"gray\">'||name_term_message_state||'</font>', " +
			"                      			'CANCELED', '<font color=\"blue\">'||name_term_message_state||'</font>', " +
			"                      			name_term_message_state" +
			"                  		 ) name_term_message_state, " +
			"  				   		 DECODE(is_archive, " +
			"                           	'Y', '<b><font color=\"red\">'||is_archive_tsl||'</font></b>', " +
			"                           	'N', '<b><font color=\"blue\">'||is_archive_tsl||'</font></b>', " +
			"                           	is_archive_tsl" +
			"                  		 ) is_archive_tsl " +
        	"                   FROM " + getGeneralDBScheme()+".vc_term_messages_club_all a " +
			"                  WHERE 1=1 ";
        if (!isEmpty(pType)) {
	    	mySQL = mySQL + " AND cd_term_message_type = ? ";
	    	pParam.add(new bcFeautureParam("string", pType));
	    }
        if (!isEmpty(pState)) {
	    	mySQL = mySQL + " AND cd_term_message_state = ? ";
	    	pParam.add(new bcFeautureParam("string", pState));
	    }
        if (!isEmpty(pIsArchive)) {
	    	mySQL = mySQL + " AND is_archive = ? ";
	    	pParam.add(new bcFeautureParam("string", pIsArchive));
	    }
	    if (!isEmpty(pFind)) {
	    	mySQL = mySQL + " AND (UPPER(TO_CHAR(id_term_message)) LIKE UPPER('%'||?||'%') OR " +
	    			" UPPER(text_term_message) LIKE UPPER('%'||?||'%') OR " +
	    			" UPPER(begin_action_date_frmt) LIKE UPPER('%'||?||'%') OR " +
	    			" UPPER(end_action_date_frmt) LIKE UPPER('%'||?||'%') OR " +
	    			" UPPER(begin_action_date_frmt) LIKE UPPER('%'||?||'%'))";
	    	for (int i=0; i<5; i++) {
	    	    pParam.add(new bcFeautureParam("string", pFind));
	    	}
	    }
	    pParam.add(new bcFeautureParam("int", p_end));
	    pParam.add(new bcFeautureParam("int", p_beg));
	    mySQL = mySQL +
			" 				   ORDER BY a.begin_action_date desc) " +
    		"          WHERE ROWNUM < ? " + 
    		"		 ) " +
    		"  WHERE rn >= ? ";
        
        StringBuilder html = new StringBuilder();
        PreparedStatement st = null; 
        boolean hasEditPermission = false;
 
	    try{
       	 	if (isEditMenuPermited("DISPATCH_MESSAGES_TERM")>0) {
       	 		hasEditPermission = true;
        	}
       	 	if ("Y".equalsIgnoreCase(pPrint)) {
       	 		hasEditPermission = false;
       	 	}
            LOGGER.debug(prepareSQLToLog(mySQL, pParam));
       	 	con = Connector.getConnection(getSessionId());
            st = con.prepareStatement(mySQL);
            st = prepareParam(st, pParam);
            ResultSet rset = st.executeQuery();
            ResultSetMetaData mtd = rset.getMetaData();
            
            int colCount = mtd.getColumnCount();
            if (hasEditPermission) {
            	html.append("<form method=\"Post\" name=\"updateForm\" id=\"updateForm\" action=\"../crm/dispatch/messages/term_messagesupdate.jsp\" onSubmit=\"return CheckSelect(this);\">\n");
            	html.append("<input type=\"hidden\" name=\"type\" value=\"general\">\n");
           	 	html.append("<input type=\"hidden\" name=\"action\" value=\"execute\">\n");
           	 	html.append("<input type=\"hidden\" name=\"process\" value=\"yes\">\n");
            }
            
            if ("Y".equalsIgnoreCase(pPrint)) {
            	html.append(getPrintTable());
            } else {
            	html.append(getHeaderTable());
            }
            html.append("<thead><tr>\n");
            if (hasEditPermission) {
            	 String sendSelected = "";
            	 String deleteSelected = "";
            	 String cancelSelected = "";
            	 String toArchiveSelected = "";
            	 String fromArchiveSelected = "";
            	 
            	 if ("send".equalsIgnoreCase(pOperationType)) {
            		 sendSelected = " SELECTED ";
            	 } else if ("delete".equalsIgnoreCase(pOperationType)) {
            		 deleteSelected = " SELECTED ";
            	 } else if ("cancel".equalsIgnoreCase(pOperationType)) {
            		 cancelSelected = " SELECTED ";
            	 } else if ("to_archive".equalsIgnoreCase(pOperationType)) {
            		 toArchiveSelected = " SELECTED ";
            	 } else if ("from_archive".equalsIgnoreCase(pOperationType)) {
            		 fromArchiveSelected = " SELECTED ";
            	 }
	           	 html.append("<th>");
	           	 html.append("<select id=\"operation_type\" name=\"operation_type\" class=\"inputfield\">");
	           	 html.append("<option value=\"send\" " + sendSelected + ">" + messageXML.getfieldTransl("h_operation_send", false) + "</option>");
	           	 html.append("<option value=\"delete\" " + deleteSelected + ">" + messageXML.getfieldTransl("h_operation_delete", false) + "</option>");
	           	 html.append("<option value=\"cancel\" " + cancelSelected + ">" + messageXML.getfieldTransl("h_operation_cancel", false) + "</option>");
	           	 html.append("<option value=\"to_archive\" " + toArchiveSelected + ">" + messageXML.getfieldTransl("h_operation_to_archive", false) + "</option>");
	           	 html.append("<option value=\"from_archive\" " + fromArchiveSelected + ">" + messageXML.getfieldTransl("h_operation_from_archive", false) + "</option>");
	           	 html.append("</select><br>");
	        	 html.append(getSubmitButtonAjax3("../crm/dispatch/messages/term_messagesupdate.jsp", "submit", "CheckSelect(document.getElementById('updateForm'))") + "<br>");
	           	 html.append("<input name=\"mainCheck\" id=\"mainCheck\" type=\"checkbox\" onclick=\"CheckAll(this,'chb')\"></th>\n");
            }
       
            for (int i=1; i <= colCount; i++) {
            	html.append(getBottomFrameTableTH(messageXML, mtd.getColumnName(i)));
            }
            html.append("<tr><tbody>\n");
            
            while (rset.next())
            {
           	html.append("<tr id=\"elem_"+rset.getString("ID_MESSAGE")+"\" onmouseout=\"coloredOut(this)\" onmouseover=\"coloredOver(this,1)\">");
           	 if (hasEditPermission) {
           		 html.append("<td align=\"center\"><input type=\"checkbox\" name=\"chb"+rset.getString("ID_MESSAGE")+"\" value=\"\" id=\"chb"+rset.getString("ID_MESSAGE")+"\" onclick=\"return CheckCB(this);\"></td>\n");
           	 } 
           	 for (int i=1; i <= colCount; i++) {
           		if ("Y".equalsIgnoreCase(pPrint)) {
   					html.append("<td>" + getValue2(rset.getString(i)) + "</td>\n");
                } else {
               		html.append(formatTDData(mtd.getColumnName(i), mtd.getColumnType(i), "")+getHyperLinkHeaderFirst() + "../crm/dispatch/messages/term_messagesspecs.jsp?id="+rset.getString("ID_MESSAGE")+getHyperLinkMiddle()+getValue2(rset.getString(i))+getHyperLinkEnd()+"</td>\n");
                }
           	 }
            }
            html.append("</tbody></table>\n");
            html.append("</form\n");
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
    } // getMessagesHeaderHTML

    public String getContactPrsMessagesHeaderHTML(String pTypeMessage, String pOperationType, String pFind, String pEmailState, String pIsArchive, String p_beg, String p_end, String pPrint) {
    	
    	ArrayList<bcFeautureParam> pParam = initParamArray();

    	String mySQL = "";
        if ("EMAIL".equalsIgnoreCase(pTypeMessage)) {
	        mySQL = 
	        	"SELECT rn, id_message, sname_jur_prs, name_contact_prs, name_post, email, title_message, " +
	        	"       sendings_quantity, error_sendings_quantity, event_date_frmt, state_record_tsl, is_archive_tsl " +
	  		  	"  FROM (SELECT ROWNUM rn, id_message, sname_jur_prs, name_contact_prs, name_post, email, " +
	  		    "               title_message, " +
	  		  	"               sendings_quantity, error_sendings_quantity, event_date_frmt, " +
	  		  	"               DECODE(state_record, " +
	  		  	"                      'PREPARED', '<b><font color=\"black\">'||state_record_tsl||'</font></b>', " +
				"                      'NEW', '<font color=\"black\">'||state_record_tsl||'</font>', " +
				"                      'EXECUTED', '<font color=\"green\">'||state_record_tsl||'</font>', " +
				"                      'EXECUTED_PARTIALLY', '<font color=\"darkgreen\">'||state_record_tsl||'</font>', " +
				"                      'ERROR', '<b><font color=\"red\">'||state_record_tsl||'</font></b>', " +
				"                      'CARRIED_OUT_PARTIALLY', '<b><font color=\"darkgray\">'||state_record_tsl||'</font></b>', " +
				"                      'CARRIED_OUT', '<font color=\"gray\">'||state_record_tsl||'</font>', " +
				"                      'CANCELED', '<font color=\"blue\">'||state_record_tsl||'</font>', " +
				"                      state_record_tsl" +
				"               ) state_record_tsl, " +
				"  		        DECODE(is_archive, " +
				"                      'Y', '<b><font color=\"red\">'||is_archive_tsl||'</font></b>', " +
				"                      'N', '<b><font color=\"blue\">'||is_archive_tsl||'</font></b>', " +
				"                      is_archive_tsl" +
				"               ) is_archive_tsl ";
        } else if ("OFFICE".equalsIgnoreCase(pTypeMessage)) {
	        mySQL = 
	        	"SELECT id_message, sname_jur_prs, name_contact_prs, name_post, title_message, " +
	        	"       sendings_quantity, error_sendings_quantity, event_date_frmt, state_record_tsl, is_archive_tsl " +
	  		  	"  FROM (SELECT ROWNUM rn, id_message, sname_jur_prs, name_contact_prs, name_post, " +
	  		    "               title_message, " +
	  		  	"               sendings_quantity, error_sendings_quantity, event_date_frmt, " +
	  		  	"               DECODE(state_record, " +
	  		  	"                      'PREPARED', '<b><font color=\"black\">'||state_record_tsl||'</font></b>', " +
				"                      'NEW', '<font color=\"black\">'||state_record_tsl||'</font>', " +
				"                      'EXECUTED', '<font color=\"green\">'||state_record_tsl||'</font>', " +
				"                      'EXECUTED_PARTIALLY', '<font color=\"darkgreen\">'||state_record_tsl||'</font>', " +
				"                      'ERROR', '<b><font color=\"red\">'||state_record_tsl||'</font></b>', " +
				"                      'CARRIED_OUT_PARTIALLY', '<b><font color=\"darkgray\">'||state_record_tsl||'</font></b>', " +
				"                      'CARRIED_OUT', '<font color=\"gray\">'||state_record_tsl||'</font>', " +
				"                      'CANCELED', '<font color=\"blue\">'||state_record_tsl||'</font>', " +
				"                      state_record_tsl" +
				"               ) state_record_tsl, " +
				"  				DECODE(is_archive, " +
				"                      'Y', '<b><font color=\"red\">'||is_archive_tsl||'</font></b>', " +
				"                      'N', '<b><font color=\"blue\">'||is_archive_tsl||'</font></b>', " +
				"                      is_archive_tsl" +
				"               ) is_archive_tsl ";
        }
        mySQL = mySQL  +
    		"           FROM (SELECT * " +
    		"                   FROM " + getGeneralDBScheme()+".vc_contact_prs_mes_club_all a " +
    		"                  WHERE type_message = ? ";
        pParam.add(new bcFeautureParam("string", pTypeMessage));
        if (!isEmpty(pEmailState)) {
	    	mySQL = mySQL + " AND state_record = ? ";
	    	pParam.add(new bcFeautureParam("string", pEmailState));
	    }
        if (!isEmpty(pIsArchive)) {
	    	mySQL = mySQL + " AND is_archive = ? ";
	    	pParam.add(new bcFeautureParam("string", pIsArchive));
	    }
        if (!isEmpty(pFind)) {
        	if ("EMAIL".equalsIgnoreCase(pTypeMessage)) {
        		mySQL = mySQL + 
        			" AND (TO_CHAR(id_message) LIKE UPPER('%'||?||'%') OR " +
        			"	   UPPER(sname_jur_prs) LIKE UPPER('%'||?||'%') OR " +
        			"	   UPPER(name_contact_prs) LIKE UPPER('%'||?||'%') OR " +
        			"	   UPPER(name_post) LIKE UPPER('%'||?||'%') OR " +
        			"	   UPPER(email) LIKE UPPER('%'||?||'%') OR " +
	    			"      UPPER(title_message) LIKE UPPER('%'||?||'%'))";
        		for (int i=0; i<6; i++) {
        		    pParam.add(new bcFeautureParam("string", pFind));
        		}
        	} else if ("OFFICE".equalsIgnoreCase(pTypeMessage)) {
        		mySQL = mySQL + 
	    			" AND (TO_CHAR(id_message) LIKE UPPER('%'||?||'%') OR " +
	    			"	   UPPER(sname_jur_prs) LIKE UPPER('%'||?||'%') OR " +
	    			"	   UPPER(name_contact_prs) LIKE UPPER('%'||?||'%') OR " +
	    			"	   UPPER(name_post) LIKE UPPER('%'||?||'%') OR " +
	    			"      UPPER(title_message) LIKE UPPER('%'||?||'%'))";
        		for (int i=0; i<5; i++) {
        		    pParam.add(new bcFeautureParam("string", pFind));
        		}
        	}
	    }
        pParam.add(new bcFeautureParam("int", p_end));
        pParam.add(new bcFeautureParam("int", p_beg));
	    mySQL = mySQL +
			" 				   ORDER BY a.id_message desc) " +
    		"          WHERE ROWNUM < ?" + 
    		"		 ) " +
    		"  WHERE rn >= ?";
        
        StringBuilder html = new StringBuilder();
        PreparedStatement st = null;
        boolean hasEmailEditPermission = false;
        boolean hasOfficeEditPermission = false;
 
	    try{
       	 	if ("Y".equalsIgnoreCase(pPrint)) {
       	 		hasEmailEditPermission = false;
       	 		hasOfficeEditPermission = false;
       	 	} else {
       	 		if (isEditMenuPermited("DISPATCH_MESSAGES_EMAIL")>0) {
       	 			hasEmailEditPermission = true;
       	 		}
       	 		if (isEditMenuPermited("DISPATCH_MESSAGES_OFFICE")>0) {
       	 			hasOfficeEditPermission = true;
       	 		}
       	 	}
            LOGGER.debug(prepareSQLToLog(mySQL, pParam));
       	 	con = Connector.getConnection(getSessionId());
            st = con.prepareStatement(mySQL);
            st = prepareParam(st, pParam);
            ResultSet rset = st.executeQuery();
            ResultSetMetaData mtd = rset.getMetaData();
            
            int colCount = mtd.getColumnCount();
            if (("EMAIL".equalsIgnoreCase(pTypeMessage) && hasEmailEditPermission) || 
            		("OFFICE".equalsIgnoreCase(pTypeMessage) && hasOfficeEditPermission)) {
            	html.append("<form method=\"Post\" name=\"updateForm\" id=\"updateForm\" action=\"../crm/dispatch/messages/email_messagesupdate.jsp\" onSubmit=\"return CheckSelect(this);\">\n");
            	html.append("<input type=\"hidden\" name=\"type\" value=\"general\">\n");
           	 	html.append("<input type=\"hidden\" name=\"action\" value=\"execute\">\n");
           	 	html.append("<input type=\"hidden\" name=\"process\" value=\"yes\">\n");
            }
            
            if ("Y".equalsIgnoreCase(pPrint)) {
            	html.append(getPrintTable());
            } else {
            	html.append(getHeaderTable());
            }
            html.append("<thead><tr>\n");
            if (("EMAIL".equalsIgnoreCase(pTypeMessage) && hasEmailEditPermission) || 
            		("OFFICE".equalsIgnoreCase(pTypeMessage) && hasOfficeEditPermission)) {
            	 String sendSelected = "";
            	 String deleteSelected = "";
            	 String cancelSelected = "";
            	 String toArchiveSelected = "";
            	 String fromArchiveSelected = "";
            	 
            	 if ("send".equalsIgnoreCase(pOperationType)) {
            		 sendSelected = " SELECTED ";
            	 } else if ("delete".equalsIgnoreCase(pOperationType)) {
            		 deleteSelected = " SELECTED ";
            	 } else if ("cancel".equalsIgnoreCase(pOperationType)) {
            		 cancelSelected = " SELECTED ";
            	 } else if ("to_archive".equalsIgnoreCase(pOperationType)) {
            		 toArchiveSelected = " SELECTED ";
            	 } else if ("from_archive".equalsIgnoreCase(pOperationType)) {
            		 fromArchiveSelected = " SELECTED ";
            	 }
	           	 html.append("<th>");
	           	 html.append("<select id=\"operation_type\" name=\"operation_type\" class=\"inputfield\">");
	           	 html.append("<option value=\"send\" " + sendSelected + ">" + messageXML.getfieldTransl("h_operation_send", false) + "</option>");
	           	 html.append("<option value=\"delete\" " + deleteSelected + ">" + messageXML.getfieldTransl("h_operation_delete", false) + "</option>");
	           	 html.append("<option value=\"cancel\" " + cancelSelected + ">" + messageXML.getfieldTransl("h_operation_cancel", false) + "</option>");
	           	 html.append("<option value=\"to_archive\" " + toArchiveSelected + ">" + messageXML.getfieldTransl("h_operation_to_archive", false) + "</option>");
	           	 html.append("<option value=\"from_archive\" " + fromArchiveSelected + ">" + messageXML.getfieldTransl("h_operation_from_archive", false) + "</option>");
	           	 html.append("</select><br>");
	        	 html.append(getSubmitButtonAjax3("../crm/dispatch/messages/email_messagesupdate.jsp", "submit", "CheckSelect(document.getElementById('updateForm'))") + "<br>");
	           	 html.append("<input name=\"mainCheck\" id=\"mainCheck\" type=\"checkbox\" onclick=\"CheckAll(this,'chb')\"></th>\n");
            }
       
            for (int i=1; i <= colCount; i++) {
            	html.append(getBottomFrameTableTH(messageXML, mtd.getColumnName(i)));
            }
            html.append("<tr><tbody>\n");
            
            while (rset.next())
            {
           	 html.append("<tr id=\"elem_"+rset.getString("ID_MESSAGE")+"\" onmouseout=\"coloredOut(this)\" onmouseover=\"coloredOver(this,1)\">");
           	 if (("EMAIL".equalsIgnoreCase(pTypeMessage) && hasEmailEditPermission) || 
             		("OFFICE".equalsIgnoreCase(pTypeMessage) && hasOfficeEditPermission)) {
           		 html.append("<td align=\"center\"><input type=\"checkbox\" name=\"chb"+rset.getString("ID_MESSAGE")+"\" value=\"\" id=\"chb"+rset.getString("ID_MESSAGE")+"\" onclick=\"return CheckCB(this);\"></td>\n");
           	 } 
           	 for (int i=1; i <= colCount; i++) {
           		if ("Y".equalsIgnoreCase(pPrint)) {
   					html.append("<td>" + getValue2(rset.getString(i)) + "</td>\n");
                } else {
                	if ("EMAIL".equalsIgnoreCase(pTypeMessage)) {
                		html.append(formatTDData(mtd.getColumnName(i), mtd.getColumnType(i), "")+getHyperLinkHeaderFirst() + "../crm/dispatch/messages/email_messagesspecs.jsp?id="+rset.getString("ID_MESSAGE")+getHyperLinkMiddle()+getValue2(rset.getString(i))+getHyperLinkEnd()+"</td>\n");
                	} else if ("OFFICE".equalsIgnoreCase(pTypeMessage)) {
                		html.append(formatTDData(mtd.getColumnName(i), mtd.getColumnType(i), "")+getHyperLinkHeaderFirst() + "../crm/dispatch/messages/office_messagesspecs.jsp?id="+rset.getString("ID_MESSAGE")+getHyperLinkMiddle()+getValue2(rset.getString(i))+getHyperLinkEnd()+"</td>\n");
                	}
                }
           	 }
            }
            html.append("</tbody></table>\n");
            html.append("</form\n");
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
    } // getMessagesHeaderHTML

    public String getDispatchOfficeMessagesHeaderHTML(String pOperationType, String pFind, String pDispatchKind, String pMessageOperType, String pStateRecord, String pIsArchive, String p_beg, String p_end, String pPrint) {
    	
    	ArrayList<bcFeautureParam> pParam = initParamArray();

    	StringBuilder html = new StringBuilder();
        PreparedStatement st = null; 
        String mySQL = 
        	"SELECT id_ds_message id_message, name_ds_message_oper_type, name_ds_sender_dispatch_kind, title_ds_message title_message, " +
        	"		sendings_quantity, error_sendings_quantity, event_date_frmt, name_ds_message_state, is_archive_tsl, cd_ds_message_oper_type " +
  		  	"  FROM (SELECT ROWNUM rn, id_ds_message, " +
  		  	"				DECODE(cd_ds_message_oper_type, " +
  		  	"                      'SEND', '<b><font color=\"green\">'||name_ds_message_oper_type||'</font></b>', " +
			"                      'RECEIVE', '<font color=\"blue\">'||name_ds_message_oper_type||'</font>', " +
			"                      name_ds_message_oper_type" +
			"               ) name_ds_message_oper_type, " +
  		  	"				DECODE(cd_ds_message_oper_type, " +
  		  	"                      'RECEIVE',DECODE(cd_ds_sender_dispatch_kind, " +
  		  	"                      				 'CLIENT', '<b><font color=\"green\">'||name_ds_sender_dispatch_kind||'</font></b>', " +
			"                      				 'PARTNER', '<b><font color=\"blue\">'||name_ds_sender_dispatch_kind||'</font></b>', " +
			"                               	 'SYSTEM', '<font color=\"red\"><b>'||name_ds_sender_dispatch_kind||'</b></font>', " +
			"                               	 'TRAINING', '<font color=\"darkgray\">'||name_ds_sender_dispatch_kind||'</font>', " +
			"                               	 'LG_PROMOTER', '<font color=\"black\">'||name_ds_sender_dispatch_kind||'</font>', " +
			"                               	 'UNKNOWN', '', " +
			"                      				 name_ds_sender_dispatch_kind" +
			"							  )," +
			"					   ''" +
			"               ) name_ds_sender_dispatch_kind, " +
  		  	"				DECODE(cd_ds_message_oper_type, " +
  		  	"                      'RECEIVE',''," +
  		  	"							CASE WHEN NVL(receivers_count,0) <= 0 THEN '<font color=\"red\"><b>'||TO_CHAR(NVL(receivers_count,0))||' '||'" + messageXML.getfieldTransl("h_less_5_receivers", false) + "' " +
		  	"							     WHEN NVL(receivers_count,0) = 1 THEN '<font color=\"green\"><b>'||TO_CHAR(NVL(receivers_count,0))||' '||'" + messageXML.getfieldTransl("h_one_receiver", false) + "' " +
		  	"							     WHEN NVL(receivers_count,0) BETWEEN 2 AND 4 THEN '<font color=\"blue\"><b>'||TO_CHAR(NVL(receivers_count,0))||' '||'" + messageXML.getfieldTransl("h_2_4_receivers", false) + "' " +
		  	"							     WHEN NVL(receivers_count,0) >= 5 THEN '<font color=\"blue\"><b>'||TO_CHAR(NVL(receivers_count,0))||' '||'" + messageXML.getfieldTransl("h_less_5_receivers", false) + "' " +
		  	"                           END||'</b></font>'" +
  		  	"				) receivers_count," +
  		  	"				title_ds_message, DECODE(cd_ds_message_oper_type, " +
  		  	"                      'SEND',TO_CHAR(NVL(sendings_quantity,0))," +
  		  	"					   ''" +
  		  	"				) sendings_quantity, " +
  		    "               DECODE(cd_ds_message_oper_type, " +
  		  	"                      'SEND',TO_CHAR(NVL(error_sendings_quantity,0))," +
  		  	"					   ''" +
  		  	"				) error_sendings_quantity, " +
  		  	"				event_date_frmt,  " +
  		  	"               DECODE(cd_ds_message_state, " +
  		  	"                      'PREPARED', '<b><font color=\"black\">'||name_ds_message_state||'</font></b>', " +
			"                      'NEW', '<font color=\"black\">'||name_ds_message_state||'</font>', " +
			"                      'EXECUTED', '<font color=\"green\">'||name_ds_message_state||'</font>', " +
			"                      'EXECUTED_PARTIALLY', '<font color=\"darkgreen\">'||name_ds_message_state||'</font>', " +
			"                      'ERROR', '<b><font color=\"red\">'||name_ds_message_state||'</font></b>', " +
			"                      'CARRIED_OUT_PARTIALLY', '<b><font color=\"darkgray\">'||name_ds_message_state||'</font></b>', " +
			"                      'CARRIED_OUT', '<font color=\"gray\">'||name_ds_message_state||'</font>', " +
			"                      'CANCELED', '<font color=\"blue\">'||name_ds_message_state||'</font>', " +
			"                      name_ds_message_state" +
			"               ) name_ds_message_state, " +
			"  				DECODE(is_archive, " +
			"                      'Y', '<b><font color=\"red\">'||is_archive_tsl||'</font></b>', " + 
			"                      'N', '<b><font color=\"blue\">'||is_archive_tsl||'</font></b>', " +
			"                      is_archive_tsl" +
			"               ) is_archive_tsl, cd_ds_message_oper_type " +
  		  	"        FROM (SELECT * " +
  		  	"                FROM " + getGeneralDBScheme()+".vc_ds_messages_club_all " +
  		  	"               WHERE cd_ds_message_type = 'OFFICE' ";
        if (!isEmpty(pFind)) {
    		mySQL = mySQL + 
    			" AND (UPPER(TO_CHAR(id_ds_message)) LIKE UPPER('%'||?||'%') OR " +
    			"	     UPPER(title_ds_message) LIKE UPPER('%'||?||'%') OR " +
    			"	     UPPER(event_date_frmt) LIKE UPPER('%'||?||'%')) ";
    		for (int i=0; i<3; i++) {
    		    pParam.add(new bcFeautureParam("string", pFind));
    		}
    	}
        if (!isEmpty(pDispatchKind)) {
	    	mySQL = mySQL + " AND name_ds_sender_dispatch_kind = ? ";
	    	pParam.add(new bcFeautureParam("string", pDispatchKind));
	    }
        if (!isEmpty(pMessageOperType)) {
	    	mySQL = mySQL + " AND cd_ds_message_oper_type = ? ";
	    	pParam.add(new bcFeautureParam("string", pMessageOperType));
	    }
        if (!isEmpty(pIsArchive)) {
	    	mySQL = mySQL + " AND is_archive = ? ";
	    	pParam.add(new bcFeautureParam("string", pIsArchive));
	    }
	    if (!isEmpty(pStateRecord)) {
    		mySQL = mySQL + " AND cd_ds_message_state = ? ";
    		pParam.add(new bcFeautureParam("string", pStateRecord));
        }
	    pParam.add(new bcFeautureParam("int", p_end));
	    pParam.add(new bcFeautureParam("int", p_beg));
        mySQL = mySQL + 
  		  	"               ORDER BY cd_ds_message_type DESC) " +
  		  	"       WHERE ROWNUM < ?" +
  		  	"        )"+
  		  	" WHERE rn >= ?";
        
        boolean hasEditPermission = false;
 
        try{
       	 	if (isEditMenuPermited("DISPATCH_MESSAGES_OFFICE")>0) {
       	 		hasEditPermission = true;
        	}
       	 	if ("Y".equalsIgnoreCase(pPrint)) {
       	 		hasEditPermission = false;
       	 	}
            LOGGER.debug(prepareSQLToLog(mySQL, pParam));
       	 	con = Connector.getConnection(getSessionId());
            st = con.prepareStatement(mySQL);
            st = prepareParam(st, pParam);
            ResultSet rset = st.executeQuery();
            ResultSetMetaData mtd = rset.getMetaData();
            
            int colCount = mtd.getColumnCount();
            if (hasEditPermission) {
            	html.append("<form method=\"Post\" name=\"updateForm\" id=\"updateForm\" action=\"../crm/dispatch/messages/office_messagesupdate.jsp\" onSubmit=\"return CheckSelect(this);\">\n");
            	html.append("<input type=\"hidden\" name=\"type\" value=\"general\">\n");
           	 	html.append("<input type=\"hidden\" name=\"action\" value=\"execute\">\n");
           	 	html.append("<input type=\"hidden\" name=\"process\" value=\"yes\">\n");
            }
            
            if ("Y".equalsIgnoreCase(pPrint)) {
            	html.append(getPrintTable());
            } else {
            	html.append(getHeaderTable());
            }
            html.append("<thead><tr>\n");
            if (hasEditPermission && "N".equalsIgnoreCase(pPrint)) {
            	 String sendSelected = "";
            	 String deleteSelected = "";
            	 String cancelSelected = "";
            	 String toArchiveSelected = "";
            	 String fromArchiveSelected = "";
            	 
            	 if ("send".equalsIgnoreCase(pOperationType)) {
            		 sendSelected = " SELECTED ";
            	 } else if ("delete".equalsIgnoreCase(pOperationType)) {
            		 deleteSelected = " SELECTED ";
            	 } else if ("cancel".equalsIgnoreCase(pOperationType)) {
            		 cancelSelected = " SELECTED ";
            	 } else if ("to_archive".equalsIgnoreCase(pOperationType)) {
            		 toArchiveSelected = " SELECTED ";
            	 } else if ("from_archive".equalsIgnoreCase(pOperationType)) {
            		 fromArchiveSelected = " SELECTED ";
            	 }
	           	 html.append("<th>");
	           	 html.append("<select id=\"operation_type\" name=\"operation_type\" class=\"inputfield\">");
	           	 html.append("<option value=\"send\" " + sendSelected + ">" + messageXML.getfieldTransl("h_operation_send", false) + "</option>");
	           	 html.append("<option value=\"delete\" " + deleteSelected + ">" + messageXML.getfieldTransl("h_operation_delete", false) + "</option>");
	           	 html.append("<option value=\"cancel\" " + cancelSelected + ">" + messageXML.getfieldTransl("h_operation_cancel", false) + "</option>");
	           	 html.append("<option value=\"to_archive\" " + toArchiveSelected + ">" + messageXML.getfieldTransl("h_operation_to_archive", false) + "</option>");
	           	 html.append("<option value=\"from_archive\" " + fromArchiveSelected + ">" + messageXML.getfieldTransl("h_operation_from_archive", false) + "</option>");
	           	 html.append("</select><br>");
	        	 html.append(getSubmitButtonAjax3("../crm/dispatch/messages/office_messagesupdate.jsp", "submit", "CheckSelect(document.getElementById('updateForm'))") + "<br>");
	           	 html.append("<input name=\"mainCheck\" id=\"mainCheck\" type=\"checkbox\" onclick=\"CheckAll(this,'chb')\"></th>\n");
            }
       
            for (int i=1; i <= colCount-1; i++) {
            	html.append(getBottomFrameTableTH(messageXML, mtd.getColumnName(i)));
            }
            html.append("<tr><tbody>\n");
            
            while (rset.next())
            {
            	html.append("<tr id=\"elem_"+rset.getString("ID_MESSAGE")+"\" onmouseout=\"coloredOut(this)\" onmouseover=\"coloredOver(this,1)\">");
	           	if (hasEditPermission && "N".equalsIgnoreCase(pPrint)) {
	           		if ("SEND".equalsIgnoreCase(rset.getString("CD_DS_MESSAGE_OPER_TYPE"))) {
	           			html.append("<td align=\"center\"><input type=\"checkbox\" name=\"chb"+rset.getString("ID_MESSAGE")+"\" value=\"\" id=\"chb"+rset.getString("ID_MESSAGE")+"\" onclick=\"return CheckCB(this);\"></td>\n");
	           		} else {
	           			html.append("<td>&nbsp;</td>\n");
	           		}
	           	} 
           	 for (int i=1; i <= colCount-1; i++) {
           		if ("Y".equalsIgnoreCase(pPrint)) {
   					html.append("<td>" + getValue2(rset.getString(i)) + "</td>\n");
                } else {
               		html.append(formatTDData(mtd.getColumnName(i), mtd.getColumnType(i), "")+getHyperLinkHeaderFirst() + "../crm/dispatch/messages/office_messagesspecs.jsp?id="+rset.getString("ID_MESSAGE")+getHyperLinkMiddle()+getValue2(rset.getString(i))+getHyperLinkEnd()+"</td>\n");
                }
           	 }
            }
            html.append("</tbody></table>\n");
            html.append("</form\n");
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
    } // getNatPrsOfficeMessagesHeaderHTML

    public String getDispatchEmailMessagesHeaderHTML(String pOperationType, String pFind, String pDispatchKind, String pMessageOperType, String pStateRecord, String pIsArchive, String p_beg, String p_end, String pPrint) {

    	ArrayList<bcFeautureParam> pParam = initParamArray();

    	String mySQL = 
        	"SELECT id_ds_message id_message, name_ds_message_oper_type, name_ds_sender_dispatch_kind name_dispatch_kind, " +
        	"		email, title_ds_message title_message, " +
        	"		sendings_quantity, error_sendings_quantity, event_date_frmt, name_ds_message_state, " +
        	"		is_archive_tsl, cd_ds_message_oper_type " +
  		  	"  FROM (SELECT ROWNUM rn, id_ds_message, " +
  		  	"				DECODE(cd_ds_message_oper_type, " +
  		  	"                      'SEND', '<b><font color=\"green\">'||name_ds_message_oper_type||'</font></b>', " +
			"                      'RECEIVE', '<font color=\"blue\">'||name_ds_message_oper_type||'</font>', " +
			"                      name_ds_message_oper_type" +
			"               ) name_ds_message_oper_type, " +
  		  	"				DECODE(cd_ds_message_oper_type, " +
  		  	"                      'RECEIVE',DECODE(cd_ds_sender_dispatch_kind, " +
  		  	"                      				 'CLIENT', '<b><font color=\"green\">'||name_ds_sender_dispatch_kind||'</font></b>', " +
			"                      				 'PARTNER', '<b><font color=\"blue\">'||name_ds_sender_dispatch_kind||'</font></b>', " +
			"                               	 'SYSTEM', '<font color=\"red\"><b>'||name_ds_sender_dispatch_kind||'</b></font>', " +
			"                               	 'TRAINING', '<font color=\"darkgray\">'||name_ds_sender_dispatch_kind||'</font>', " +
			"                               	 'LG_PROMOTER', '<font color=\"black\">'||name_ds_sender_dispatch_kind||'</font>', " +
			"                               	 'UNKNOWN', '', " +
			"                      				 name_ds_sender_dispatch_kind" +
			"							  )," +
			"					   ''" +
			"               ) name_ds_sender_dispatch_kind, " +
  		  	"				DECODE(cd_ds_message_oper_type, " +
  		  	"                      'RECEIVE',email," +
  		  	"							CASE WHEN NVL(receivers_count,0) <= 0 THEN '<font color=\"red\"><b>'||TO_CHAR(NVL(receivers_count,0))||' '||'" + messageXML.getfieldTransl("h_less_5_receivers", false) + "' " +
  		  	"							     WHEN NVL(receivers_count,0) = 1 THEN '<font color=\"green\"><b>'||TO_CHAR(NVL(receivers_count,0))||' '||'" + messageXML.getfieldTransl("h_one_receiver", false) + "' " +
		  	"							     WHEN NVL(receivers_count,0) BETWEEN 2 AND 4 THEN '<font color=\"blue\"><b>'||TO_CHAR(NVL(receivers_count,0))||' '||'" + messageXML.getfieldTransl("h_2_4_receivers", false) + "' " +
  		  	"							     WHEN NVL(receivers_count,0) >= 5 THEN '<font color=\"blue\"><b>'||TO_CHAR(NVL(receivers_count,0))||' '||'" + messageXML.getfieldTransl("h_less_5_receivers", false) + "' " +
		  	"                           END||'</b></font>'" +
  		  	"				) email," +
  		  	"				title_ds_message, " +
  		  	"				DECODE(cd_ds_message_oper_type, " +
  		  	"                      'SEND',TO_CHAR(NVL(sendings_quantity,0))," +
  		  	"					   ''" +
  		  	"				) sendings_quantity, " +
  		    "               DECODE(cd_ds_message_oper_type, " +
  		  	"                      'SEND',TO_CHAR(NVL(error_sendings_quantity,0))," +
  		  	"					   ''" +
  		  	"				) error_sendings_quantity, " +
  		    "				event_date_frmt,  " +
  		  	"               DECODE(cd_ds_message_state, " +
  		  	"                      'PREPARED', '<b><font color=\"black\">'||name_ds_message_state||'</font></b>', " +
			"                      'NEW', '<font color=\"black\">'||name_ds_message_state||'</font>', " +
			"                      'EXECUTED', '<font color=\"green\">'||name_ds_message_state||'</font>', " +
			"                      'EXECUTED_PARTIALLY', '<font color=\"darkgreen\">'||name_ds_message_state||'</font>', " +
			"                      'ERROR', '<b><font color=\"red\">'||name_ds_message_state||'</font></b>', " +
			"                      'CARRIED_OUT_PARTIALLY', '<b><font color=\"darkgray\">'||name_ds_message_state||'</font></b>', " +
			"                      'CARRIED_OUT', '<font color=\"gray\">'||name_ds_message_state||'</font>', " +
			"                      'CANCELED', '<font color=\"blue\">'||name_ds_message_state||'</font>', " +
			"                      name_ds_message_state" +
			"               ) name_ds_message_state, " +
			"  				DECODE(is_archive, " +
			"                      'Y', '<b><font color=\"red\">'||is_archive_tsl||'</font></b>', " + 
			"                      'N', '<b><font color=\"blue\">'||is_archive_tsl||'</font></b>', " +
			"                      is_archive_tsl" +
			"               ) is_archive_tsl," +
			"				cd_ds_message_oper_type " +
  		  	"        FROM (SELECT * " +
  		  	"                FROM " + getGeneralDBScheme()+".vc_ds_messages_club_all " +
  		  	"               WHERE cd_ds_message_type = 'EMAIL' ";
        if (!isEmpty(pFind)) {
    		mySQL = mySQL + 
    			" AND (UPPER(TO_CHAR(id_ds_message)) LIKE UPPER('%'||?||'%') OR " +
    			"	     UPPER(email) LIKE UPPER('%'||?||'%') OR " +
    			"	     UPPER(title_ds_message) LIKE UPPER('%'||?||'%') OR " +
    			"	     UPPER(event_date_frmt) LIKE UPPER('%'||?||'%')) ";
    		for (int i=0; i<4; i++) {
    		    pParam.add(new bcFeautureParam("string", pFind));
    		}
    	}
        if (!isEmpty(pDispatchKind)) {
	    	mySQL = mySQL + " AND cd_ds_sender_dispatch_kind = ? ";
	    	pParam.add(new bcFeautureParam("string", pDispatchKind));
	    }
        if (!isEmpty(pMessageOperType)) {
	    	mySQL = mySQL + " AND cd_ds_message_oper_type = ? ";
	    	pParam.add(new bcFeautureParam("string", pMessageOperType));
	    }
        if (!isEmpty(pIsArchive)) {
	    	mySQL = mySQL + " AND is_archive = ? ";
	    	pParam.add(new bcFeautureParam("string", pIsArchive));
	    }
	    if (!isEmpty(pStateRecord)) {
    		mySQL = mySQL + " AND cd_ds_message_state = ? ";
    		pParam.add(new bcFeautureParam("string", pStateRecord));
        }

	    pParam.add(new bcFeautureParam("int", p_end));
	    pParam.add(new bcFeautureParam("int", p_beg));
        mySQL = mySQL + 
  		  	"               ORDER BY id_ds_message DESC) " +
  		  	"       WHERE ROWNUM < ? " + 
  		  	"        )"+
  		  	" WHERE rn >= ?";
        
        StringBuilder html = new StringBuilder();
        PreparedStatement st = null; 
        boolean hasEditPermission = false;
 
        try{
       	 	if (isEditMenuPermited("DISPATCH_MESSAGES_EMAIL")>0) {
       	 		hasEditPermission = true;
        	}
       	 	if ("Y".equalsIgnoreCase(pPrint)) {
       	 		hasEditPermission = false;
       	 	}
            LOGGER.debug(prepareSQLToLog(mySQL, pParam));
       	 	con = Connector.getConnection(getSessionId());
            st = con.prepareStatement(mySQL);
            st = prepareParam(st, pParam);
            ResultSet rset = st.executeQuery();
            ResultSetMetaData mtd = rset.getMetaData();
            
            int colCount = mtd.getColumnCount();
            if (hasEditPermission) {
            	html.append("<form method=\"Post\" name=\"updateForm\" id=\"updateForm\" action=\"../crm/dispatch/messages/email_messagesupdate.jsp\" onSubmit=\"return CheckSelect(this);\">\n");
            	html.append("<input type=\"hidden\" name=\"type\" value=\"general\">\n");
           	 	html.append("<input type=\"hidden\" name=\"action\" value=\"execute\">\n");
           	 	html.append("<input type=\"hidden\" name=\"process\" value=\"yes\">\n");
            }
            
            if ("Y".equalsIgnoreCase(pPrint)) {
            	html.append(getPrintTable());
            } else {
            	html.append(getHeaderTable());
            }
            html.append("<thead><tr>\n");
            if (hasEditPermission && "N".equalsIgnoreCase(pPrint)) {
            	 String sendSelected = "";
            	 String deleteSelected = "";
            	 String cancelSelected = "";
            	 String toArchiveSelected = "";
            	 String fromArchiveSelected = "";
            	 
            	 if ("send".equalsIgnoreCase(pOperationType)) {
            		 sendSelected = " SELECTED ";
            	 } else if ("delete".equalsIgnoreCase(pOperationType)) {
            		 deleteSelected = " SELECTED ";
            	 } else if ("cancel".equalsIgnoreCase(pOperationType)) {
            		 cancelSelected = " SELECTED ";
            	 } else if ("to_archive".equalsIgnoreCase(pOperationType)) {
            		 toArchiveSelected = " SELECTED ";
            	 } else if ("from_archive".equalsIgnoreCase(pOperationType)) {
            		 fromArchiveSelected = " SELECTED ";
            	 }
	           	 html.append("<th>");
	           	 html.append("<select id=\"operation_type\" name=\"operation_type\" class=\"inputfield\">");
	           	 html.append("<option value=\"send\" " + sendSelected + ">" + messageXML.getfieldTransl("h_operation_send", false) + "</option>");
	           	 html.append("<option value=\"delete\" " + deleteSelected + ">" + messageXML.getfieldTransl("h_operation_delete", false) + "</option>");
	           	 html.append("<option value=\"cancel\" " + cancelSelected + ">" + messageXML.getfieldTransl("h_operation_cancel", false) + "</option>");
	           	 html.append("<option value=\"to_archive\" " + toArchiveSelected + ">" + messageXML.getfieldTransl("h_operation_to_archive", false) + "</option>");
	           	 html.append("<option value=\"from_archive\" " + fromArchiveSelected + ">" + messageXML.getfieldTransl("h_operation_from_archive", false) + "</option>");
	           	 html.append("</select><br>");
	        	 html.append(getSubmitButtonAjax3("../crm/dispatch/messages/email_messagesupdate.jsp", "submit", "CheckSelect(document.getElementById('updateForm'))") + "<br>");
	           	 html.append("<input name=\"mainCheck\" id=\"mainCheck\" type=\"checkbox\" onclick=\"CheckAll(this,'chb')\"></th>\n");
            }
       
            for (int i=1; i <= colCount-1; i++) {
            	html.append(getBottomFrameTableTH(messageXML, mtd.getColumnName(i)));
            }
            html.append("<tr><tbody>\n");
            
            while (rset.next()) {
            	html.append("<tr id=\"elem_"+rset.getString("ID_MESSAGE")+"\" onmouseout=\"coloredOut(this)\" onmouseover=\"coloredOver(this,1)\">");
	           	if (hasEditPermission && "N".equalsIgnoreCase(pPrint)) {
	           		if ("SEND".equalsIgnoreCase(rset.getString("CD_DS_MESSAGE_OPER_TYPE"))) {
	           			html.append("<td align=\"center\"><input type=\"checkbox\" name=\"chb"+rset.getString("ID_MESSAGE")+"\" value=\"\" id=\"chb"+rset.getString("ID_MESSAGE")+"\" onclick=\"return CheckCB(this);\"></td>\n");
	           		} else {
	           			html.append("<td>&nbsp;</td>\n");
	           		}
	           	} 
	           	for (int i=1; i <= colCount-1; i++) {
	           		if ("Y".equalsIgnoreCase(pPrint)) {
	   					html.append("<td>" + getValue2(rset.getString(i)) + "</td>\n");
	                } else {
	               		html.append(formatTDData(mtd.getColumnName(i), mtd.getColumnType(i), "")+getHyperLinkHeaderFirst() + "../crm/dispatch/messages/email_messagesspecs.jsp?id="+rset.getString("ID_MESSAGE")+getHyperLinkMiddle()+getValue2(rset.getString(i))+getHyperLinkEnd()+"</td>\n");
	                }
	           	}
            }
            html.append("</tbody></table>\n");
            html.append("</form\n");
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
    } // getNatPrsEmailMessagesHeaderHTML

    public String getClientsPatternsHeaderHTML(String pPath, String pFind, String pCdPatternType, String pCdPatternStatus, String p_beg, String p_end, String pPrint) {
    	
    	ArrayList<bcFeautureParam> pParam = initParamArray();

    	String mySQL = 
        	"SELECT rn, id_ds_pattern id_pattern, name_ds_pattern name_pattern, name_pattern_type, name_ds_sender_dispatch_kind, name_pattern_status, " +
	  		"       begin_action_date_frmt, end_action_date_frmt, can_send_tsl " +
	  		"  FROM (SELECT ROWNUM rn, id_ds_pattern, name_ds_pattern, name_pattern_type, name_pattern_status, name_ds_sender_dispatch_kind, " +
	  		"               begin_action_date_frmt, " +
	  		"               end_action_date_frmt, can_send_tsl " +
	  		"          FROM (SELECT a.id_ds_pattern, a.name_ds_pattern, " +
	  		"                DECODE(a.cd_pattern_status, " +
        	"                       'ACTIVE', '<font color=\"green\"><b>'||name_pattern_status||'</b></font>', " +
        	"                       'SUSPENDED', '<font color=\"brown\"><b>'||name_pattern_status||'</b></font>', " +
        	"                       'CANCEL', '<font color=\"red\"><b>'||name_pattern_status||'</b></font>', " +
        	"                       'FINISH', '<font color=\"blue\"><b>'||name_pattern_status||'</b></font>', " +
        	"                       a.name_pattern_status" +
        	"                ) name_pattern_status, " +
	  		"                a.name_pattern_type, " +
	  		"                DECODE(cd_ds_sender_dispatch_kind, " +
			"                      	'CLIENT', '<b><font color=\"green\">'||name_ds_sender_dispatch_kind||'</font></b>', " +
			"                      	'PARTNER', '<b><font color=\"blue\">'||name_ds_sender_dispatch_kind||'</font></b>', " +
			"                       'SYSTEM', '<font color=\"red\"><b>'||name_ds_sender_dispatch_kind||'</b></font>', " +
			"                       'TRAINING', '<font color=\"darkgray\">'||name_ds_sender_dispatch_kind||'</font>', " +
			"                     	'LG_PROMOTER', '<font color=\"black\">'||name_ds_sender_dispatch_kind||'</font>', " +
			"                       'UNKNOWN', '', " +
			"                       name_ds_sender_dispatch_kind" +
    		"				 )||DECODE(a.text_request,NULL,'',' ('||a.text_request||')') name_ds_sender_dispatch_kind, " +
	  		"				 a.begin_action_date_frmt, " +
	  		"                a.end_action_date_frmt, " +
        	"                DECODE(a.can_send_sms, " +
        	"                       'N', '<font color=\"black\">'||can_send_sms_tsl||'</font>', " +
        	"                       'Y', '<font color=\"red\"><b>'||can_send_sms_tsl||'</b></font>', " +
        	"                       a.can_send_sms_tsl)||'/'||" +
	  		"                DECODE(a.can_send_email, " +
        	"                       'N', '<font color=\"black\">'||can_send_email_tsl||'</font>', " +
        	"                       'Y', '<font color=\"red\"><b>'||can_send_email_tsl||'</b></font>', " +
        	"                       a.can_send_email_tsl)||'/'||" +
        	"                DECODE(a.can_send_office, " +
        	"                       'N', '<font color=\"black\">'||can_send_office_tsl||'</font>', " +
        	"                       'Y', '<font color=\"red\"><b>'||can_send_office_tsl||'</b></font>', " +
        	"                       a.can_send_office_tsl) can_send_tsl " +
	  		"                  FROM " + getGeneralDBScheme()+".vc_ds_pattern_club_all a " +
	  		"                 WHERE 1 = 1";
        if (!isEmpty(pCdPatternType)) {
    		mySQL = mySQL + " AND cd_pattern_type = ? ";
    		pParam.add(new bcFeautureParam("string", pCdPatternType));
    	}
        if (!isEmpty(pCdPatternStatus)) {
    		mySQL = mySQL + " AND cd_pattern_status = ? ";
    		pParam.add(new bcFeautureParam("string", pCdPatternStatus));
    	}
        if (!isEmpty(pPath)) {
        	if ("SMS".equalsIgnoreCase(pPath)) {
        		mySQL = mySQL + " AND can_send_sms = 'Y' ";
        	} else if ("EMAIL".equalsIgnoreCase(pPath)) {
        		mySQL = mySQL + " AND can_send_email = 'Y' ";
        	} else if ("OFFICE".equalsIgnoreCase(pPath)) {
        		mySQL = mySQL + " AND can_send_office = 'Y' ";
        	} else if ("CARD".equalsIgnoreCase(pPath)) {
        		mySQL = mySQL + " AND can_send_card = 'Y' ";
        	}
    	}
        if (!isEmpty(pFind)) {
    		mySQL = mySQL + 
    			" AND (UPPER(TO_CHAR(id_ds_pattern)) LIKE UPPER('%'||?||'%') OR " +
    			"	   UPPER(name_ds_pattern) LIKE UPPER('%'||?||'%') OR " +
    			"	   UPPER(basis_for_operation) LIKE UPPER('%'||?||'%') OR " +
    			"	   UPPER(begin_action_date_frmt) LIKE UPPER('%'||?||'%') OR " +
    			"	   UPPER(end_action_date_frmt) LIKE UPPER('%'||?||'%')) ";
    		for (int i=0; i<5; i++) {
    		    pParam.add(new bcFeautureParam("string", pFind));
    		}
    	}
        pParam.add(new bcFeautureParam("int", p_end));
        pParam.add(new bcFeautureParam("int", p_beg));
        mySQL = mySQL + 
		  		"                ORDER BY name_ds_pattern) " +
		  		"         WHERE ROWNUM < ?" + 
		  		"        )"+
		  		" WHERE rn >= ?";
 
    	return getHeaderParamHTML(
        		mySQL,
        		pParam,
                0,
                "D",
            	0,
            	"",
            	"../crm/dispatch/messages/patternspecs.jsp?id=:FIELD2:", 
            	messageXML,
            	pPrint);
    } // getMessagesHeaderHTML

    public String getCardTasksHeaderHTML(String pType, String pState, String pFind, String p_beg, String p_end, String pPrint) {
    	
    	ArrayList<bcFeautureParam> pParam = initParamArray();

    	String mySQL = 
        		"SELECT rn, cd_card1, name_card_operation_type, " +
        		"       begin_action_date_frmt, end_action_date_frmt, basis_for_operation, " +
        		"       name_card_oper_state, creation_date_frmt, id_card_operation " +
  		  		"  FROM (SELECT ROWNUM rn, id_card_operation, cd_card1, " +
  				"  				'<span title=\"'||opr_param||'\">'||" +
  				"               DECODE(cd_card_operation_type, " +
  				"                      'CHANGE_PARAM', '<font color=\"brown\">'||name_card_operation_type||'</font>', " +
  				"                      'SEND_MESSAGE', '<font color=\"black\">'||name_card_operation_type||'</font>', " +
  				"                      'BLOCK_CARD', '<font color=\"red\"><b>'||name_card_operation_type||'</b></font>', " +
  				"                      'ADD_BON', '<font color=\"green\"><b>'||name_card_operation_type||'</b></font>', " +
  				"                      'WRITE_OFF_BON', '<font color=\"blue\"><b>'||name_card_operation_type||'</b></font>', " +
  				"                      'ADD_GOODS_TO_PURSE', '<font color=\"green\">'||name_card_operation_type||'</font>', " +
  	  			"                      'WRITE_OFF_GOODS_FROM_PURSE', '<font color=\"blue\"><b>'||name_card_operation_type||'</b></font>', " +
  	  			"                      'MOVE_BON', '<font color=\"#FF0000;\"><b>'||name_card_operation_type||'</b></font>', " +
  	  			"                      'SET_CATEGORIES_ON_PERIOD', '<font color=\"brown\"><b>'||name_card_operation_type||'</b></font>', " +
  				"                      name_card_operation_type)||" +
  				"               '</span>' name_card_operation_type, " +
  		  		"               begin_action_date_frmt, end_action_date_frmt, basis_for_operation, " +
  				"  				DECODE(cd_card_oper_state, " +
  				"                      'EXECUTED', '<font color=\"green\"><b>'||name_card_oper_state||'</b></font>', " +
  				"                      'EXECUTED_PARTIALLY', '<font color=\"green\">'||name_card_oper_state||'</font>', " +
  				"                      'CARRIED_OUT', '<font color=\"brown\"><b>'||name_card_oper_state||'</b></font>', " +
  				"                      'CARRIED_OUT_PARTIALLY', '<font color=\"brown\">'||name_card_oper_state||'</font>', " +
  				"                      'NEW', '<b>'||name_card_oper_state||'</b>', " +
  				"                      'CANCELED', '<font color=\"gray\">'||name_card_oper_state||'</font>', " +
  				"                      'ERROR', '<font color=\"red\"><b>'||name_card_oper_state||'</b></font>', " +
  				"                      'PREPARED', '<font color=\"blue\">'||name_card_oper_state||'</font>', " +
  				"                      name_card_oper_state) " +
  				"				name_card_oper_state, creation_date_frmt, opr_param " +
  		  		"          FROM (SELECT * " +
  		  		"                  FROM " + getGeneralDBScheme()+".vc_card_oper_club_all " +
  		  		"                 WHERE 1=1 ";
        if (!isEmpty(pFind)) {
        	mySQL = mySQL + 
        		" AND (cd_card1 LIKE '%'||?||'%' OR " +
        		"  card_serial_number LIKE '%'||?||'%' OR " +
        		"  begin_action_date_frmt LIKE '%'||?||'%' OR " +
        		"  UPPER(basis_for_operation) LIKE UPPER('%'||?||'%'))";
        	for (int i=0; i<4; i++) {
        	    pParam.add(new bcFeautureParam("string", pFind));
        	}
        }
        if (!isEmpty(pType)) {
        	mySQL = mySQL + " AND cd_card_operation_type = ? ";
        	pParam.add(new bcFeautureParam("string", pType));
        }
        if (!isEmpty(pState)) {
        	mySQL = mySQL + " AND cd_card_oper_state = ? ";
        	pParam.add(new bcFeautureParam("string", pState));
        }
        pParam.add(new bcFeautureParam("int", p_end));
        pParam.add(new bcFeautureParam("int", p_beg));
  		mySQL = mySQL + " ORDER BY id_card_operation DESC) WHERE ROWNUM < ?) WHERE rn >= ?";
        
  		return getHeaderParamHTML(
        		mySQL,
        		pParam,
                1,
                "D",
            	0,
            	"",
            	"../crm/cards/card_taskspecs.jsp?type=" + pType + "&id=:FIELD9:", 
            	card_taskXML,
            	pPrint);
    } // getCardTasksHeaderHTML

 
    public String getLogisticClubCardsHeadHTML(String pType, String pFind, String p_beg, String p_end, String pPrint) {
        String myViewName = "";
        String myCountField = "";
        String myFileName = "";
        String mySQL =  "";

        if ("BON_CARD".equalsIgnoreCase(pType)) {
        	myViewName = "vc_lg_card_priv_all";
        	myCountField = "bon_cards_count";
        	myFileName = "../crm/logistic/partners/cardspecs.jsp";
        } else if ("QUESTIONNAIRE".equalsIgnoreCase(pType)) {
        	myViewName = "vc_lg_questionnaire_priv_all";
        	myCountField = "questionnaires_count";
        	myFileName = "../crm/logistic/partners/questspecs.jsp";
        } else if ("TERMINAL".equalsIgnoreCase(pType)) {
        	myViewName = "vc_lg_terminal_priv_all";
        	myCountField = "terminals_count";
        	myFileName = "../crm/logistic/partners/terminalspecs.jsp";
        } else if ("SAM".equalsIgnoreCase(pType)) {
        	myViewName = "vc_lg_sam_priv_all";
        	myCountField = "sam_count";
        	myFileName = "../crm/logistic/partners/samspecs.jsp";
        } else if ("GIFT".equalsIgnoreCase(pType)) {
        	myViewName = "vc_lg_gifts_priv_all";
        	myCountField = "gifts_count";
        	myFileName = "../crm/club_event/warehousespecs.jsp";
        } else if ("OTHER".equalsIgnoreCase(pType)) {
        	myViewName = "vc_lg_other_priv_all";
        	myCountField = "object_count";
        	myFileName = "../crm/logistic/partners/otherspecs.jsp";
        }

        ArrayList<bcFeautureParam> pParam = initParamArray();

        mySQL = 
            	"SELECT rn, id_lg_record, action_date_frmt, operation_desc, " +
            	"       sname_jur_prs_receiver,  sname_jur_prs_sender, " + myCountField + 
            	"  FROM (SELECT ROWNUM rn, id_lg_record, operation_desc, " + myCountField + ", " +
            	"               sname_jur_prs_receiver, sname_jur_prs_sender, action_date_frmt " +
            	"		   FROM (SELECT * " +
            	"                  FROM " + getGeneralDBScheme() + "." + myViewName;
        if (!isEmpty(pFind)) {
        	mySQL = mySQL +
           			  "  WHERE (UPPER(sname_jur_prs_receiver) LIKE UPPER('%'||?||'%')" +
         			  "  OR UPPER(sname_jur_prs_sender) LIKE UPPER('%'||?||'%')) ";
        	for (int i=0; i<2; i++) {
        	    pParam.add(new bcFeautureParam("string", pFind));
        	}
        }
        pParam.add(new bcFeautureParam("int", p_end));
        pParam.add(new bcFeautureParam("int", p_beg));
        mySQL = mySQL + 
	            "               ORDER BY action_date DESC) " +
	            "         WHERE ROWNUM < ?)" + 
	            "  WHERE rn >= ?";

        return getHeaderParamHTML(
        			mySQL,
            		pParam,
                    0,
                    "D",
                	0,
                	"",
                	myFileName + "?id=:FIELD2:", 
                    logisticXML,
                    pPrint);
    }

    public String getLogisticGiftsHeadHTML(String pFind, String p_beg, String p_end, String pPrint) {
        
    	ArrayList<bcFeautureParam> pParam = initParamArray();

    	String mySQL = 
            	"SELECT rn, action_date_frmt, operation_desc, " +
            	"		sname_jur_prs_receiver,  sname_jur_prs_sender, " +
            	"       count_gift_all_frmt, count_gift_given_frmt, count_gift_remain_frmt, id_lg_record " +
            	"  FROM (SELECT ROWNUM rn, id_lg_record, operation_desc, " +
            	"               count_gift_all count_gift_all_frmt, " +
            	"				count_gift_given count_gift_given_frmt, " +
            	"               count_gift_remain count_gift_remain_frmt, " +
            	"       		sname_jur_prs_receiver, sname_jur_prs_sender, action_date_frmt " +
            	"		   FROM (SELECT * " +
            	"                  FROM " + getGeneralDBScheme() + ".vc_lg_gifts_priv_all";
        if (!isEmpty(pFind)) {
        	mySQL = mySQL +
           			  "  WHERE (UPPER(sname_jur_prs_receiver) LIKE UPPER('%'||?||'%')" +
         			  "  OR UPPER(sname_jur_prs_sender) LIKE UPPER('%'||?||'%')) ";
        	for (int i=0; i<2; i++) {
        	    pParam.add(new bcFeautureParam("string", pFind));
        	}
        }
        pParam.add(new bcFeautureParam("int", p_end));
        pParam.add(new bcFeautureParam("int", p_beg));
        mySQL = mySQL + 
	            "               ORDER BY action_date DESC) " +
	            "         WHERE ROWNUM < ?)" + 
	            "  WHERE rn >= ?";

        return getHeaderParamHTML(
        			mySQL,
            		pParam,
                    1,
                    "D",
                	0,
                	"",
                	"../crm/club_event/warehousespecs.jsp?id=:FIELD9:", 
                    logisticXML,
                    pPrint);
    }

    public String getNatPrsGiftRequestHeadHTML(String pType, String pState, String pFind, String p_beg, String p_end, String pPrint) {
        
    	ArrayList<bcFeautureParam> pParam = initParamArray();

    	String mySQL = 
            	"SELECT rn, name_nat_prs_gift_request_type, nm_nat_prs_gift_request_state, " +
            	"       date_accept_frmt, name_nat_prs, phone_contact, name_club_event, " +
            	"       text_request, id_nat_prs_gift_request " +
            	"  FROM (SELECT ROWNUM rn, id_nat_prs_gift_request, full_name name_nat_prs, phone_contact, " +
            	"               DECODE(cd_nat_prs_gift_request_type," +
            	"                      'SMS', '<b><font color=\"green\">'||name_nat_prs_gift_request_type||'</font></b>'," +
            	"                      'EMAIL', '<b><font color=\"darkgray\">'||name_nat_prs_gift_request_type||'</font></b>'," +
            	"                      'PHONE_CALL', '<b><font color=\"blue\">'||name_nat_prs_gift_request_type||'</font></b>'," +
            	"                      'CALL_CENTER', '<b><font color=\"brown\">'||name_nat_prs_gift_request_type||'</font></b>'," +
            	"                      'OFFICE', '<b><font color=\"brown\">'||name_nat_prs_gift_request_type||'</font></b>'," +
            	"                      name_nat_prs_gift_request_type" +
            	"               ) name_nat_prs_gift_request_type, " +
            	"               DECODE(cd_nat_prs_gift_request_state," +
            	"                      'ACCEPT', '<b><font color=\"blue\">'||nm_nat_prs_gift_request_state||'</font></b>'," +
            	"                      'REJECTED', '<b><font color=\"red\">'||nm_nat_prs_gift_request_state||'</font></b>'," +
            	"                      'PROCESSED', '<b><font color=\"green\">'||nm_nat_prs_gift_request_state||'</font></b>'," +
	          	"                      'DISASSEMBLED', '<b><font color=\"brown\">'||nm_nat_prs_gift_request_state||'</font></b>'," +
	          	"                      'ERROR', '<b><font color=\"red\">'||nm_nat_prs_gift_request_state||'</font></b>'," +
            	"                      nm_nat_prs_gift_request_state" +
            	"               ) nm_nat_prs_gift_request_state, " +
            	"               name_club_event, " +
            	"               text_request, date_accept_frmt " +
            	"		   FROM (SELECT * " +
            	"                  FROM " + getGeneralDBScheme() + ".vc_nat_prs_gift_request_cl_all" +
            	"                 WHERE 1=1 ";
        if (!isEmpty(pType)) {
        	mySQL = mySQL + "  AND cd_nat_prs_gift_request_type = ? ";
        	pParam.add(new bcFeautureParam("string", pType));
        }
        if (!isEmpty(pState)) {
        	mySQL = mySQL + "  AND cd_nat_prs_gift_request_state = ? ";
        	pParam.add(new bcFeautureParam("string", pState));
        }
        if (!isEmpty(pFind)) {
        	mySQL = mySQL +
           			  "  AND (UPPER(TO_CHAR(id_nat_prs_gift_request)) LIKE UPPER('%'||?||'%') OR " +
         			  "       UPPER(full_name) LIKE UPPER('%'||?||'%') OR " +
         			  "       UPPER(phone_contact) LIKE UPPER('%'||?||'%') OR " +
         			  "       UPPER(text_request) LIKE UPPER('%'||?||'%') OR " +
         			  "       UPPER(name_club_event) LIKE UPPER('%'||?||'%') OR " +
         			  "       UPPER(date_accept_frmt) LIKE UPPER('%'||?||'%')) ";
        	for (int i=0; i<6; i++) {
        	    pParam.add(new bcFeautureParam("string", pFind));
        	}
        }
        pParam.add(new bcFeautureParam("int", p_end));
        pParam.add(new bcFeautureParam("int", p_beg));
        mySQL = mySQL + 
	            "               ORDER BY date_accept DESC) " +
	            "         WHERE ROWNUM < ?)" + 
	            "  WHERE rn >= ?";

        return getHeaderParamHTML(
        			mySQL,
            		pParam,
                    1,
                    "D",
                	0,
                	"",
                	"../crm/club_event/requestspecs.jsp?id=:FIELD9:", 
                    club_actionXML,
                    pPrint);
    }

    public String getLogisticPromotersHTML(String pFind, String pPost, String pState, String p_beg, String p_end, String pPrint) {

    	ArrayList<bcFeautureParam> pParam = initParamArray();

    	String mySQL = 
    			"SELECT rn, cd_lg_promoter, name_lg_promoter, " +
    			"       jur_prs||DECODE(service_place, null, '', ', '||service_place) jur_prs, name_lg_promoter_post, " +
		  		"       name_lg_promoter_state, date_begin_work_frmt, date_end_work_frmt, id_lg_promoter " +
		  		"  FROM (SELECT ROWNUM rn, id_lg_promoter, cd_lg_promoter, " +
		  		"               name_lg_promoter, sname_jur_prs jur_prs, " +
		  		"               name_service_place service_place, " +
		  		"               DECODE(cd_lg_promoter_post, " +
		  		"                      'PROMOTER', '<font color=\"green\"><b>'||name_lg_promoter_post||'</b></font>', " +
	  			"                      'SUPERVISOR', '<font color=\"red\"><b>'||name_lg_promoter_post||'</b></font>', " +
	  			"                      'CHIEF', '<font color=\"blue\"><b>'||name_lg_promoter_post||'</b></font>', " +
	  			"                      'SENIOR_TELLER', '<font color=\"blue\"><b>'||name_lg_promoter_post||'</b></font>', " +
	  			"                      '<font color=\"black\"><b>'||name_lg_promoter_post||'</b></font>' " +
		  		"               ) name_lg_promoter_post, " +
		  		"               DECODE(cd_lg_promoter_state, " +
	  			"                      'ACCEPTED', '<font color=\"green\"><b>'||name_lg_promoter_state||'</b></font>', " +
	  			"                      'TRANSFERRED', '<font color=\"blue\"><b>'||name_lg_promoter_state||'</b></font>', " +
	  			"                      'DISMISSED', '<font color=\"red\"><b>'||name_lg_promoter_state||'</b></font>', " +
	  			"                      name_lg_promoter_state " +
		  		"               ) name_lg_promoter_state, " +
		  		"               date_begin_work_frmt, date_end_work_frmt " +
		  		"          FROM (SELECT * " +
		  		"                  FROM " + getGeneralDBScheme()+".vc_lg_promoter_club_all " +
		  		"                 WHERE 1=1";
	    if (!isEmpty(pFind)) {
			mySQL = mySQL + 
				" AND (UPPER(TO_CHAR(id_lg_promoter)) LIKE UPPER('%'||?||'%') OR " +
				"	   UPPER(cd_lg_promoter) LIKE UPPER('%'||?||'%') OR " +
				"	   UPPER(name_lg_promoter) LIKE UPPER('%'||?||'%') OR " +
				"	   UPPER(sname_jur_prs) LIKE UPPER('%'||?||'%') OR " +
				"	   UPPER(name_service_place) LIKE UPPER('%'||?||'%') OR " +
				"	   UPPER(date_begin_work_frmt) LIKE UPPER('%'||?||'%') OR " +
				"	   UPPER(date_end_work_frmt) LIKE UPPER('%'||?||'%') OR " +
				"	   UPPER(phone_mobile) LIKE UPPER('%'||?||'%')) ";
			for (int i=0; i<8; i++) {
			    pParam.add(new bcFeautureParam("string", pFind));
			}
		}
	    if (!isEmpty(pPost)) {
			mySQL = mySQL + " AND cd_lg_promoter_post = ? ";
			pParam.add(new bcFeautureParam("string", pPost));
		}
	    if (!isEmpty(pState)) {
	    	if ("WORKS".equalsIgnoreCase(pState)) {
	    		mySQL = mySQL + " AND cd_lg_promoter_state IN ('WORKS','ACCEPTED','TRANSFERRED') ";
	    	} else {
	    		mySQL = mySQL + " AND cd_lg_promoter_state = ? ";
	    		pParam.add(new bcFeautureParam("string", pState));
	    	}
		}
	    pParam.add(new bcFeautureParam("int", p_end));
	    pParam.add(new bcFeautureParam("int", p_beg));
	    mySQL = mySQL +
			  		"                 ORDER BY name_lg_promoter) " +
			  		"         WHERE ROWNUM < ?" + 
			  		"        )"+
			  		" WHERE rn >= ?";

	    return getHeaderParamHTML(
        		mySQL,
        		pParam,
                1,
                "D",
            	0,
            	"",
            	"../crm/logistic/clients/promoterspecs.jsp?id=:FIELD9:", 
            	logisticXML,
            	pPrint);
    } // getCardTasksHeaderHTML

    public String getLogisticPromoterPaymentsHTML(String pFind, String pPayKind, String pPayState, String p_beg, String p_end, String pPrint) {

    	ArrayList<bcFeautureParam> pParam = initParamArray();

    	String mySQL = 
    			"SELECT rn, date_lg_promoter_payment_frmt, name_lg_promoter_pay_state, name_lg_promoter_pay_kind, " +
    			"       date_begin_sale_period_frmt, date_end_sale_period_frmt, " +
    			"       promoters_count, sales_cards_count, amount_all_payment_frmt, amount_penalty_frmt, " +
    			"       amount_currency_payment_frmt, amount_bon_payment_frmt, id_lg_promoter_payment " +
		  		"  FROM (SELECT ROWNUM rn,  " +
		  		"               DECODE(cd_lg_promoter_pay_state, " +
		  		"                      'CREATED', '<font color=\"blue\"><b>'||name_lg_promoter_pay_state||'</b></font>', " +
	  			"                      'CLOSED', '<font color=\"green\"><b>'||name_lg_promoter_pay_state||'</b></font>', " +
	  			"                      '<font color=\"black\"><b>'||name_lg_promoter_pay_state||'</b></font>' " +
		  		"               ) name_lg_promoter_pay_state, " +
		  		"               DECODE(cd_lg_promoter_pay_kind, " +
		  		"                      'CASHIER_PAYMENT', '<font color=\"green\"><b>'||name_lg_promoter_pay_kind||'</b></font>', " +
	  			"                      'PROMOTER_PAYMENT', '<font color=\"red\"><b>'||name_lg_promoter_pay_kind||'</b></font>', " +
	  			"                      'SUPERVISOR_PAYMENT', '<font color=\"blue\"><b>'||name_lg_promoter_pay_kind||'</b></font>', " +
	  			"                      '<font color=\"black\"><b>'||name_lg_promoter_pay_kind||'</b></font>' " +
		  		"               ) name_lg_promoter_pay_kind, " +
		  		"               date_lg_promoter_payment_frmt, date_begin_sale_period_frmt, " +
		  		"               date_end_sale_period_frmt, promoters_count, sales_cards_count, " +
		  		"               amount_all_payment_frmt||' '||sname_currency amount_all_payment_frmt," +
		  		"               amount_currency_payment_frmt||' '||sname_currency amount_currency_payment_frmt," +
		  		"               amount_penalty_frmt||' '||sname_currency amount_penalty_frmt," +
		  		"               amount_bon_payment_frmt," +
		  		"               id_lg_promoter_payment " +
		  		"          FROM (SELECT * " +
		  		"                  FROM " + getGeneralDBScheme()+".vc_lg_promoter_payment_all " +
		  		"                 WHERE 1=1";
	    if (!isEmpty(pFind)) {
			mySQL = mySQL + 
				" AND (UPPER(date_lg_promoter_payment_frmt) LIKE UPPER('%'||?||'%') OR " +
				"	   UPPER(date_begin_sale_period_frmt) LIKE UPPER('%'||?||'%') OR " +
				"	   UPPER(date_end_sale_period_frmt) LIKE UPPER('%'||?||'%')) ";
			for (int i=0; i<3; i++) {
			    pParam.add(new bcFeautureParam("string", pFind));
			}
		}
	    if (!isEmpty(pPayKind)) {
			mySQL = mySQL + " AND cd_lg_promoter_pay_kind = ? ";
			pParam.add(new bcFeautureParam("string", pPayKind));
		}
	    if (!isEmpty(pPayState)) {
			mySQL = mySQL + " AND cd_lg_promoter_pay_state = ? ";
			pParam.add(new bcFeautureParam("string", pPayState));
		}
	    pParam.add(new bcFeautureParam("int", p_end));
	    pParam.add(new bcFeautureParam("int", p_beg));
	    mySQL = mySQL +
			  		"                 ORDER BY date_lg_promoter_payment DESC) " +
			  		"         WHERE ROWNUM < ?" + 
			  		"        )"+
			  		" WHERE rn >= ?";

	    return getHeaderParamHTML(
        		mySQL,
        		pParam,
                1,
                "D",
            	0,
            	"",
            	"../crm/logistic/clients/paymentspecs.jsp?id=:FIELD13:", 
            	logisticXML,
            	pPrint);
    } // getCardTasksHeaderHTML

    public String getLogisticPromoterPenaltiesHTML(String pFind, String p_beg, String p_end, String pPrint) {

    	ArrayList<bcFeautureParam> pParam = initParamArray();

    	String mySQL = 
    			"SELECT rn, name_lg_promoter, date_lg_promoter_penalty_frmt, " +
    			"       value_lg_promoter_penalty_frmt, reason_lg_promoter_penalty, id_lg_promoter_penalty " +
		  		"  FROM (SELECT ROWNUM rn, name_lg_promoter, date_lg_promoter_penalty_frmt, " +
		  		"               value_lg_promoter_penalty_frmt||' '||sname_currency value_lg_promoter_penalty_frmt," +
		  		"               reason_lg_promoter_penalty, id_lg_promoter_penalty " +
		  		"          FROM (SELECT * " +
		  		"                  FROM " + getGeneralDBScheme()+".vc_lg_promoter_penalty_all " +
		  		"                 WHERE 1=1";
	    if (!isEmpty(pFind)) {
			mySQL = mySQL + 
				" AND (UPPER(name_lg_promoter) LIKE UPPER('%'||?||'%') OR " +
				"	   UPPER(date_lg_promoter_penalty_frmt) LIKE UPPER('%'||?||'%') OR " +
				"	   UPPER(value_lg_promoter_penalty_frmt) LIKE UPPER('%'||?||'%') OR " +
				"	   UPPER(reason_lg_promoter_penalty) LIKE UPPER('%'||?||'%')) ";
			for (int i=0; i<4; i++) {
			    pParam.add(new bcFeautureParam("string", pFind));
			}
		}

	    pParam.add(new bcFeautureParam("int", p_end));
	    pParam.add(new bcFeautureParam("int", p_beg));
	    mySQL = mySQL +
			  		"                 ORDER BY date_lg_promoter_penalty DESC) " +
			  		"         WHERE ROWNUM < ?" + 
			  		"        )"+
			  		" WHERE rn >= ?";

	    return getHeaderParamHTML(
        		mySQL,
        		pParam,
                1,
                "D",
            	0,
            	"",
            	"../crm/logistic/clients/penaltyspecs.jsp?id=:FIELD6:", 
            	logisticXML,
            	pPrint);
    } // getCardTasksHeaderHTML

    public String getLogisticDeliveryPointsHTML(String pFind, String p_beg, String p_end, String pPrint) {

    	ArrayList<bcFeautureParam> pParam = initParamArray();

    	String mySQL = 
    			"SELECT rn, cd_club_service_place,  " +
		  		"       name_service_place, sname_jur_prs, adr_full, name_service_place_type, id_club, id_service_place " +
		  		"  FROM (SELECT ROWNUM rn, id_service_place, " +
		  		"				'<b><font color=\"green\">'||cd_club_service_place||'</font></b>' cd_club_service_place, " +
		  		"				name_service_place_type,  " +
		  		"               name_service_place, sname_jur_prs, adr_full, id_club " +
		  		"          FROM (SELECT * " +
		  		"                  FROM " + getGeneralDBScheme()+".vc_lg_service_place_club_all " +
		  		"                 WHERE 1=1";
	    if (!isEmpty(pFind)) {
			mySQL = mySQL + 
				" AND (UPPER(TO_CHAR(ROWNUM)) LIKE UPPER('%'||?||'%') OR " +
				"	   UPPER(cd_club_service_place) LIKE UPPER('%'||?||'%') OR " +
				"	   UPPER(name_service_place) LIKE UPPER('%'||?||'%') OR " +
				"	   UPPER(name_jur_prs) LIKE UPPER('%'||?||'%') OR " +
				"	   UPPER(adr_full) LIKE UPPER('%'||?||'%')) ";
			for (int i=0; i<5; i++) {
			    pParam.add(new bcFeautureParam("string", pFind));
			}
		}
	    pParam.add(new bcFeautureParam("int", p_end));
	    pParam.add(new bcFeautureParam("int", p_beg));
	    mySQL = mySQL +
			  		"                 ORDER BY name_service_place) " +
			  		"         WHERE ROWNUM < ?" + 
			  		"        )"+
			  		" WHERE rn >= ?";

	    return getHeaderParamHTML(
        		mySQL,
        		pParam,
                2,
                "D",
            	0,
            	"",
            	"../crm/logistic/clients/delivery_pointspecs.jsp?id=:FIELD8:&id_club=:FIELD7:", 
            	jurpersonXML,
            	pPrint);
    } // getCardTasksHeaderHTML

    public String getLogisticScheduleHTML(String pFind, String p_beg, String p_end, String pPrint) {

    	ArrayList<bcFeautureParam> pParam = initParamArray();

    	String mySQL = 
    			"SELECT rn, date_card_given_frmt, " +
    			"       service_places_count, promoters_count, sales_cards_count, sales_cards_cashier_count, notes, id_lg_cc_given_schedule " +
		  		"  FROM (SELECT ROWNUM rn, id_lg_cc_given_schedule, date_card_given_frmt, " +
		  		"               service_places_count, promoters_count, sales_cards_count, sales_cards_cashier_count, notes " +
		  		"          FROM (SELECT * " +
		  		"                  FROM " + getGeneralDBScheme()+".vc_lg_cc_given_schedule_cl_all " +
		  		"                 WHERE 1=1";
	    if (!isEmpty(pFind)) {
			mySQL = mySQL + 
				" AND (UPPER(TO_CHAR(date_card_given_frmt)) LIKE UPPER('%'||?||'%') OR " +
				"	   UPPER(notes) LIKE UPPER('%'||?||'%')) ";
			for (int i=0; i<2; i++) {
			    pParam.add(new bcFeautureParam("string", pFind));
			}
		}

	    pParam.add(new bcFeautureParam("int", p_end));
	    pParam.add(new bcFeautureParam("int", p_beg));
	    mySQL = mySQL +
			  		"                 ORDER BY TRUNC(date_card_given) DESC) " +
			  		"         WHERE ROWNUM < ?" + 
			  		"        )"+
			  		" WHERE rn >= ?";

        return getHeaderParamHTML(
        		mySQL,
        		pParam,
                1,
                "D",
            	0,
            	"",
            	"../crm/logistic/clients/operation_schedulespecs.jsp?id=:FIELD8:", 
            	logisticXML,
            	pPrint);
    } // getCardTasksHeaderHTML

    public String getLogisticAnalyticsHeaderHTML(String pFind, String p_beg, String p_end, String pPrint) {
    	
    	String idClubField =  this.getIdClubFieldString("FIRST");
    	
    	ArrayList<bcFeautureParam> pParam = initParamArray();

    	String mySQL = 
        		"SELECT rn, id_jur_prs, sname_jur_prs, bon_cards_received_frmt, " +
  		  		"       bon_cards_distributed_frmt, quest_receiver_frmt, quest_distributed_frmt, " + 
  		  		"       sams_received_frmt, terminals_received_frmt, others_received_frmt, cost_others_received_frmt " + 
  		  		idClubField + 
  		  		"  FROM (SELECT ROWNUM rn " + idClubField + ", id_jur_prs, sname_jur_prs, " +
  		  		"               CASE WHEN NVL(bon_cards_received,0) <> NVL(bon_cards_distributed,0)" +
  		  		"                    THEN '<font color=\"red\"><b>'||TO_CHAR(bon_cards_received)||'</b><font>' " +
  		  		"                    ELSE TO_CHAR(bon_cards_received) " +
  		  		"               END bon_cards_received_frmt," +
  		  		"               CASE WHEN NVL(bon_cards_received,0) <> NVL(bon_cards_distributed,0)" +
  		  		"                    THEN '<font color=\"red\"><b>'||TO_CHAR(bon_cards_distributed)||'</b><font>' " +
  		  		"                    ELSE TO_CHAR(bon_cards_distributed) " +
  		  		"               END bon_cards_distributed_frmt," +
  		  		"               CASE WHEN NVL(questionnaires_receiver,0) <> NVL(questionnaires_distributed,0)" +
  		  		"                    THEN '<font color=\"green\"><b>'||TO_CHAR(questionnaires_receiver)||'</b><font>' " +
  		  		"                    ELSE TO_CHAR(questionnaires_receiver) " +
  		  		"               END quest_receiver_frmt," +
  		  		"               CASE WHEN NVL(questionnaires_receiver,0) <> NVL(questionnaires_distributed,0)" +
  		  		"                    THEN '<font color=\"green\"><b>'||TO_CHAR(questionnaires_distributed)||'</b><font>' " +
  		  		"                    ELSE TO_CHAR(questionnaires_distributed) " +
  		  		"               END quest_distributed_frmt," +
  		  		"               sams_received sams_received_frmt, " +
  		  		"               terminals_received terminals_received_frmt, " +
  		  		"               others_received others_received_frmt, " +
  		  		"               cost_others_received_frmt " +
  		  		"          FROM (SELECT * " +
  		  		"                  FROM " + getGeneralDBScheme()+".vc_lg_stat_club_all ";
        if (!isEmpty(pFind)) {
    		mySQL = mySQL + 
    			" WHERE (UPPER(TO_CHAR(id_jur_prs)) LIKE UPPER('%'||?||'%') OR " +
    			"	     UPPER(sname_jur_prs) LIKE UPPER('%'||?||'%') OR " +
    			"	     UPPER(bon_cards_received) LIKE UPPER('%'||?||'%') OR " +
    			"	     UPPER(bon_cards_distributed) LIKE UPPER('%'||?||'%') OR " +
    			"	     UPPER(questionnaires_receiver) LIKE UPPER('%'||?||'%') OR " +
    			"	     UPPER(questionnaires_distributed) LIKE UPPER('%'||?||'%') OR " +
    			"	     UPPER(sams_received) LIKE UPPER('%'||?||'%') OR " +
    			"	     UPPER(terminals_received) LIKE UPPER('%'||?||'%') OR " +
    			"	     UPPER(others_received) LIKE UPPER('%'||?||'%') OR " +
    			"	     UPPER(cost_others_received_frmt) LIKE UPPER('%'||?||'%')) ";
    		for (int i=0; i<10; i++) {
    		    pParam.add(new bcFeautureParam("string", pFind));
    		}
    	}
        pParam.add(new bcFeautureParam("int", p_end));
        pParam.add(new bcFeautureParam("int", p_beg));
        mySQL = mySQL +
  		  		"                 ORDER BY sname_jur_prs) " +
  		  		"         WHERE ROWNUM < ?" + 
  		  		"        )"+
  		  		" WHERE rn >= ?";

        return getHeaderParamHTML(
        		mySQL,
        		pParam,
                0,
                "D",
            	0,
            	"",
            	"../crm/logistic/partners/analyticspecs.jsp?id=:FIELD2:", 
            	logisticXML,
            	pPrint);
    } // getCardTasksHeaderHTML

    public String getLogisticProductionHTML(String pFind, String p_beg, String p_end, String pPrint) {

    	ArrayList<bcFeautureParam> pParam = initParamArray();

    	String mySQL = 
        		"SELECT rn, cd_lg_production, name_lg_production, " +
        		"       name_currency, cost_lg_production_frmt, id_lg_production " +
  		  		"  FROM (SELECT ROWNUM rn, id_lg_production, cd_lg_production, name_lg_production, " +
  		  		"               name_currency, cost_lg_production_frmt " +
  		  		"          FROM (SELECT * " +
  		  		"                  FROM " + getGeneralDBScheme()+".vc_lg_catalog_club_all " +
  		  		"                 WHERE 1=1 ";
        if (!isEmpty(pFind)) {
    		mySQL = mySQL + 
    			" AND (UPPER(TO_CHAR(id_lg_production)) LIKE UPPER('%'||?||'%') OR " +
    			"	     UPPER(cd_lg_production) LIKE UPPER('%'||?||'%') OR " +
    			"	     UPPER(name_lg_production) LIKE UPPER('%'||?||'%') OR " +
    			"	     UPPER(desc_lg_production) LIKE UPPER('%'||?||'%') OR " +
    			"	     UPPER(cost_lg_production) LIKE UPPER('%'||?||'%')) ";
    		for (int i=0; i<5; i++) {
    		    pParam.add(new bcFeautureParam("string", pFind));
    		}
    	}
        pParam.add(new bcFeautureParam("int", p_end));
        pParam.add(new bcFeautureParam("int", p_beg));
        mySQL = mySQL +
  		  		"                 ORDER BY name_lg_production) " +
  		  		"         WHERE ROWNUM < ?" + 
  		  		"        )"+
  		  		" WHERE rn >= ?";

        return getHeaderParamHTML(
        		mySQL,
        		pParam,
                1,
                "D",
            	0,
            	"",
            	"../crm/logistic/partners/catalogspecs.jsp?id=:FIELD6:", 
            	logisticXML,
            	pPrint);
    } // getCardTasksHeaderHTML
    
    public String getExchangeSMSHTML(String pMessageType, String pFindValue, String p_beg, String p_end, String pPrint) {

    	ArrayList<bcFeautureParam> pParam = initParamArray();

    	String mySQL = 
    		" SELECT rn, id_sms_message, name_sms_message_type, recepient, text_message, " +
			"		 action_date " +
			"   FROM (SELECT ROWNUM rn, id_sms_message, name_sms_message_type, recepient, text_message, " +
			"				 action_date " +
			"           FROM (SELECT id_sms_message, name_sms_message_type, recepient, text_message, " +
			"					     action_date_frmt action_date " +
			"                   FROM " + getGeneralDBScheme()+".vc_ds_sms_all " +
    		"                  WHERE 1 = 1 "; 
    	if (!("".equalsIgnoreCase(pMessageType))) {
    		mySQL = mySQL + " AND cd_sms_message_type = ? ";
    		pParam.add(new bcFeautureParam("string", pMessageType));
    	}
    	pParam.add(new bcFeautureParam("int", p_end));
    	pParam.add(new bcFeautureParam("int", p_beg));
    	mySQL = mySQL + 
    		" 				   ORDER BY id_sms_message DESC) " +
    		"          WHERE ROWNUM < ?" +
    		"		 ) " +
    		"  WHERE rn >= ?";
    	
    	return getHeaderParamHTML(
    			mySQL,
        		pParam,
                1,
                "D",
            	0,
            	"",
                "../crm/exchange/smsspecs.jsp?id=:FIELD2:", 
    			smsXML,
    			pPrint);
    } //getCardCategoriesHeadHTML()

    public String getSMSHTML(String pOperationType, String pFind, String pDispatchKind, String pIsArchive, String pMessageType, String pMessageState, String p_beg, String p_end, String pPrint) {
        StringBuilder html = new StringBuilder();
        PreparedStatement st = null; 

        ArrayList<bcFeautureParam> pParam = initParamArray();

        String mySQL =
        	" SELECT rn, id_sms_message, name_sms_message_type,  " +
			"        name_dispatch_kind, recepient, text_message, " +
			"        event_date_frmt, name_sms_state, all_sendings_count, is_archive_tsl, " +
			"        cd_dispatch_kind " +
			"   FROM (SELECT ROWNUM rn, id_sms_message, name_sms_message_type, " +
			"                name_dispatch_kind, recepient, text_message, " +
			"                event_date_frmt, name_sms_state, all_sendings_count, is_archive_tsl, " +
			"                cd_dispatch_kind " +
			"           FROM (SELECT id_sms_message, " +
			"  				         DECODE(cd_sms_message_type, " +
			"                               'SEND', '<font color=\"green\">'||name_sms_message_type||'</font>', " +
			"                               'RECEIVE', '<font color=\"blue\">'||name_sms_message_type||'</font>', " +
			"                               'CALL_IN', '<font color=\"red\">'||name_sms_message_type||'</font>', " +
			"                               'CALL_OUT', '<font color=\"darkgray\">'||name_sms_message_type||'</font>', " +
			"                              name_sms_message_type" +
			"                        ) name_sms_message_type, " +
			"                        DECODE(cd_dispatch_kind, " +
			"                               'CLIENT', '<font color=\"green\">'||name_dispatch_kind||'</font>', " +
			"                               'PARTNER', '<font color=\"blue\">'||name_dispatch_kind||'</font>', " +
			"                               'SYSTEM', '<font color=\"red\"><b>'||name_dispatch_kind||'</b></font>', " +
			"                               'TRAINING', '<font color=\"darkgray\">'||name_dispatch_kind||'</font>', " +
			"                               'LG_PROMOTER', '<font color=\"black\">'||name_dispatch_kind||'</font>', " +
			"                               'UNKNOWN', '', " +
			"                              name_dispatch_kind" +
			"                        ) name_dispatch_kind, " +
			"                        recepient, " +
			"                        CASE WHEN LENGTH(text_message) > 1000 THEN SUBSTR(text_message,1,1000)||'...' ELSE text_message END text_message, " +
			"                        event_date_frmt, " +
			"  				         DECODE(cd_sms_state, " +
			"                               'PREPARED', '<b><font color=\"black\">'||name_sms_state||'</font></b>', " +
			"                               'NEW', '<font color=\"black\">'||name_sms_state||'</font>', " +
			"                               'IN_PROCESS', '<font color=\"blue\">'||name_sms_state||'</font>', " +
			"                               'DELIVERED', '<font color=\"darkgreen\">'||name_sms_state||'</font>', " +
			"                               'ERROR', '<b><font color=\"red\">'||name_sms_state||'</font></b>', " +
			"                               'REPORT_NOT_RECIEVE', '<b><font color=\"red\">'||name_sms_state||'</font></b>', " +
			"                               'GOES', '<font color=\"blue\">'||name_sms_state||'</font>', " +
			"                               'RECEIVED', '<font color=\"darkgreen\">'||name_sms_state||'</font>', " +
			"                               'WAIT_FOR_CONFIRM', '<font color=\"darkyellow\">'||name_sms_state||'</font>', " +
			"                              name_sms_state" +
			"                        ) name_sms_state, " +
			"                        CASE WHEN cd_sms_message_type IN ('RECEIVE', 'CALL_IN') THEN '' " +
			"                             ELSE CASE WHEN NVL(send_count, 0) > 0 THEN '<b><font color=\"green\">'||TO_CHAR(NVL(send_count, 0))||'</font></b>'" +
			"                                       ELSE TO_CHAR(NVL(send_count, 0)) " +
			"						           END || ' / ' || " +
			"                                  CASE WHEN NVL(error_sendings_quantity, 0) > 0 THEN '<b><font color=\"red\">'||TO_CHAR(NVL(error_sendings_quantity, 0))||'</font></b>'" +
			"                                       ELSE TO_CHAR(NVL(error_sendings_quantity, 0)) " +
			"						           END" +
			"                        END all_sendings_count, " +
			"  				         DECODE(is_archive, " +
			"                               'Y', '<b><font color=\"red\">'||is_archive_tsl||'</font></b>', " +
			"                               'N', '<b><font color=\"blue\">'||is_archive_tsl||'</font></b>', " +
			"                               is_archive_tsl" +
			"                        ) is_archive_tsl, " +
			"                        cd_dispatch_kind " +
			"                   FROM " + getGeneralDBScheme()+".vc_ds_sms_all " +
			"                  WHERE 1=1 ";
        if (!isEmpty(pDispatchKind)) {
	    	mySQL = mySQL + " AND cd_dispatch_kind = ? ";
	    	pParam.add(new bcFeautureParam("string", pDispatchKind));
	    }
        if (!isEmpty(pIsArchive)) {
	    	mySQL = mySQL + " AND is_archive = ? ";
	    	pParam.add(new bcFeautureParam("string", pIsArchive));
	    }
	    if (!isEmpty(pMessageType)) {
	    	mySQL = mySQL + " AND cd_sms_message_type = ? ";
	    	pParam.add(new bcFeautureParam("string", pMessageType));
	    }
	    if (!isEmpty(pMessageState)) {
	    	mySQL = mySQL + " AND cd_sms_state = ? ";
	    	pParam.add(new bcFeautureParam("string", pMessageState));
	    }
		if (!isEmpty(pFind)) {
			mySQL = mySQL + 
				" AND (TO_CHAR(id_sms_message) LIKE UPPER('%'||?||'%') OR " +
				"      UPPER(recepient) LIKE UPPER('%'||?||'%') OR " +
				"      UPPER(text_message) LIKE UPPER('%'||?||'%') OR " +
				"      UPPER(event_date_frmt) LIKE UPPER('%'||?||'%'))";
			for (int i=0; i<4; i++) {
			    pParam.add(new bcFeautureParam("string", pFind));
			}
		}
		pParam.add(new bcFeautureParam("int", p_end));
		pParam.add(new bcFeautureParam("int", p_beg));
	    mySQL = mySQL + 
			" 				   ORDER BY id_sms_message DESC) " +
			"          WHERE ROWNUM < ?" +
			"		 ) " +
			"  WHERE rn >= ?";
        
        boolean hasEditPermission = false;
        
        try{
       	 	if (isEditMenuPermited("DISPATCH_MESSAGES_SMS")>0) {
       	 		hasEditPermission = true;
        	}
       	 	if ("Y".equalsIgnoreCase(pPrint)) {
       	 		hasEditPermission = false;
       	 	}
            LOGGER.debug(prepareSQLToLog(mySQL, pParam));
       	 	con = Connector.getConnection(getSessionId());
            st = con.prepareStatement(mySQL);
            st = prepareParam(st, pParam);
            ResultSet rset = st.executeQuery();
            ResultSetMetaData mtd = rset.getMetaData();
            
            int colCount = mtd.getColumnCount();
            if (hasEditPermission) {
            	html.append("<form method=\"Post\" name=\"updateForm\" id=\"updateForm\" action=\"../crm/dispatch/messages/smsupdate.jsp\" onSubmit=\"return CheckSelect(this);\">\n");
            	html.append("<input type=\"hidden\" name=\"type\" value=\"general\">\n");
           	 	html.append("<input type=\"hidden\" name=\"action\" value=\"execute\">\n");
           	 	html.append("<input type=\"hidden\" name=\"process\" value=\"yes\">\n");
            }
            
            if ("Y".equalsIgnoreCase(pPrint)) {
            	html.append(getPrintTable());
            } else {
            	html.append(getHeaderTable());
            }
            html.append("<thead><tr>\n");
            if (hasEditPermission) {
            	 String sendSelected = "";
            	 String deleteSelected = "";
            	 String prepareSelected = "";
            	 //String cancelSelected = "";
            	 String toArchiveSelected = "";
            	 String fromArchiveSelected = "";
            	 String deliveredSelected = "";
            	 String errorSelected = "";
            	 
            	 if ("send".equalsIgnoreCase(pOperationType)) {
            		 sendSelected = " SELECTED ";
            	 } else if ("delete".equalsIgnoreCase(pOperationType)) {
            		 deleteSelected = " SELECTED ";
            	 } else if ("prepare".equalsIgnoreCase(pOperationType)) {
            		 prepareSelected = " SELECTED ";
            	 } else if ("to_archive".equalsIgnoreCase(pOperationType)) {
            		 toArchiveSelected = " SELECTED ";
            	 } else if ("from_archive".equalsIgnoreCase(pOperationType)) {
            		 fromArchiveSelected = " SELECTED ";
            	 } else if ("delivered".equalsIgnoreCase(pOperationType)) {
            		 deliveredSelected = " SELECTED ";
            	 } else if ("error".equalsIgnoreCase(pOperationType)) {
            		 errorSelected = " SELECTED ";
            	 }
	           	 html.append("<th>");
	           	 html.append("<select id=\"operation_type\" name=\"operation_type\" class=\"inputfield\">");
	           	 html.append("<option value=\"send\" " + sendSelected + ">" + messageXML.getfieldTransl("h_operation_send", false) + "</option>");
	           	 html.append("<option value=\"delete\" " + deleteSelected + ">" + messageXML.getfieldTransl("h_operation_delete", false) + "</option>");
	           	 html.append("<option value=\"prepare\" " + prepareSelected + ">" + messageXML.getfieldTransl("h_operation_prepare", false) + "</option>");
	           	 html.append("<option value=\"to_archive\" " + toArchiveSelected + ">" + messageXML.getfieldTransl("h_operation_to_archive", false) + "</option>");
	           	 html.append("<option value=\"from_archive\" " + fromArchiveSelected + ">" + messageXML.getfieldTransl("h_operation_from_archive", false) + "</option>");
	           	 html.append("<option value=\"delivered\" " + deliveredSelected + ">" + messageXML.getfieldTransl("h_operation_delivered", false) + "</option>");
	           	 html.append("<option value=\"error\" " + errorSelected + ">" + messageXML.getfieldTransl("h_operation_error", false) + "</option>");
	           	 html.append("</select><br>");
	        	 html.append(getSubmitButtonAjax3("../crm/dispatch/messages/smsupdate.jsp", "submit", "CheckSelect(document.getElementById('updateForm'))") + "<br>");
	           	 html.append("<input name=\"mainCheck\" id=\"mainCheck\" type=\"checkbox\" onclick=\"CheckAll(this,'chb')\"></th>\n");
            }
       
            for (int i=1; i <= colCount-1; i++) {
            	html.append(getBottomFrameTableTH(smsXML, mtd.getColumnName(i)));
            }
            html.append("<tr><tbody>\n");
            
            while (rset.next())
            {
            	html.append("<tr id=\"elem_"+rset.getString("ID_SMS_MESSAGE")+"\" onmouseout=\"coloredOut(this)\" onmouseover=\"coloredOver(this,1)\">");
           	 if (hasEditPermission) {
           		 html.append("<td align=\"center\"><input type=\"checkbox\" name=\"chb"+rset.getString("ID_SMS_MESSAGE")+"\" value=\"\" id=\"chb"+rset.getString("ID_SMS_MESSAGE")+"\" onclick=\"return CheckCB(this);\"></td>\n");
           	 } 
           	 for (int i=1; i <= colCount-1; i++) {
           		if ("Y".equalsIgnoreCase(pPrint)) {
					html.append("<td>" + getValue2(rset.getString(i)) + "</td>\n");
                } else {
               		html.append(formatTDData(mtd.getColumnName(i), mtd.getColumnType(i), "")+getHyperLinkHeaderFirst() + "../crm/dispatch/messages/smsspecs.jsp?id="+rset.getString("ID_SMS_MESSAGE")+getHyperLinkMiddle()+getValue2(rset.getString(i))+getHyperLinkEnd()+"</td>\n");
                }
           	 }
            }
            html.append("</tbody></table>\n");
            html.append("</form\n");
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
       } // getEventsHTML

    public String getEmailProfileHTML(String pProfileState, String pFind, String p_beg, String p_end, String pPrint) {

    	ArrayList<bcFeautureParam> pParam = initParamArray();

    	String mySQL = 
    		" SELECT rn, name_email_profile, name_profile_state, sender_email, " +
    		"        smtp_server, smtp_port, smtp_ssl_tsl, id_email_profile " +
			"   FROM (SELECT ROWNUM rn, id_email_profile, name_email_profile, name_profile_state, sender_email, " +
			"                smtp_server, smtp_port, smtp_ssl_tsl " +
			"           FROM (SELECT id_email_profile, name_email_profile, " +
			"						 DECODE(cd_profile_state, " +
            "               				'INACTIVE', '<font color=\"red\"><b>'||name_profile_state||'</b></font>', " +
            "               				'ACTIVE', '<font color=\"green\"><b>'||name_profile_state||'</b></font>', " +
            "               				name_profile_state" +
            "        		 		 ) name_profile_state, sender_email, " +
			"                        smtp_server, smtp_port, " +
			"						 DECODE(smtp_ssl, " +
            "               				'N', '<font color=\"blue\"><b>'||smtp_ssl_tsl||'</b></font>', " +
            "               				'Y', '<font color=\"green\"><b>'||smtp_ssl_tsl||'</b></font>', " +
            "               				smtp_ssl_tsl" +
            "        		 		 ) smtp_ssl_tsl " +
			"                   FROM " + getGeneralDBScheme()+".vc_ds_email_profile_club_all " +
			"                  WHERE 1=1 ";
    	if (!isEmpty(pProfileState)) {
    		mySQL = mySQL + " AND cd_profile_state = ? ";
    		pParam.add(new bcFeautureParam("string", pProfileState));
    	}
    	if (!isEmpty(pFind)) {
    		mySQL = mySQL + 
    			" AND (UPPER(TO_CHAR(id_email_profile)) LIKE UPPER('%'||?||'%') OR " +
    			"	   UPPER(name_email_profile) LIKE UPPER('%'||?||'%') OR " +
    			"	   UPPER(sender_email) LIKE UPPER('%'||?||'%') OR " +
    			"	   UPPER(smtp_server) LIKE UPPER('%'||?||'%') OR " +
    			"	   UPPER(smtp_port) LIKE UPPER('%'||?||'%')) ";
    		for (int i=0; i<5; i++) {
    		    pParam.add(new bcFeautureParam("string", pFind));
    		}
    	}
        pParam.add(new bcFeautureParam("int", p_end));
        pParam.add(new bcFeautureParam("int", p_beg));
        mySQL = mySQL + 
    		" 				   ORDER BY name_email_profile) " +
    		"          WHERE ROWNUM < ?" +
    		"		 ) " +
    		"  WHERE rn >= ?";
        
        
        return getHeaderParamHTML(
    			mySQL,
        		pParam,
                1,
                "D",
            	0,
            	"",
            	"../crm/dispatch/settings/email_profilespecs.jsp?id=:FIELD8:", 
                emailXML, pPrint);
       }
    
    public String getClubRelationshipsHeadHTML(String cdType, String pFind, String p_beg, String p_end, String pPrint) {

    	ArrayList<bcFeautureParam> pParam = initParamArray();

    	String mySQL = 
			" SELECT rn, date_club_rel, name_club_rel_type,  " +
      	  	"        title_society, title_partner, id_club_rel " +
      	  	"   FROM (SELECT ROWNUM rn, id_club_rel, name_club_rel_type, date_club_rel_frmt date_club_rel, " +
      	  	"                desc_club_rel, sname_party1 title_society, " +
      	  	"                sname_party2 title_partner " +
          	"   		  FROM (SELECT * " +
          	"                     FROM " + getGeneralDBScheme()+".vc_club_rel_club_all " +
          	"                    WHERE 1=1 ";
        if (!isEmpty(cdType)) {
        	mySQL = mySQL + " AND cd_club_rel_type = ? ";
        	pParam.add(new bcFeautureParam("string", cdType));
        }
    	if (!isEmpty(pFind)) {
    		mySQL = mySQL + " AND (UPPER(id_club_rel) LIKE UPPER('%'||?||'%') OR " +
    			"UPPER(name_club_rel_type) LIKE UPPER('%'||?||'%') OR " +
    			"UPPER(date_club_rel_frmt) LIKE UPPER('%'||?||'%') OR " +
    			"UPPER(sname_party1) LIKE UPPER('%'||?||'%') OR " + 
    			"UPPER(sname_party2) LIKE UPPER('%'||?||'%')) ";
    		for (int i=0; i<5; i++) {
    		    pParam.add(new bcFeautureParam("string", pFind));
    		}
    	}
    	pParam.add(new bcFeautureParam("int", p_end));
    	pParam.add(new bcFeautureParam("int", p_beg));
        mySQL = mySQL + 
          	  "                  ORDER BY name_club_rel_type, sname_party1, sname_party2) " +
          	  "          WHERE ROWNUM < ?) " +
          	  "  WHERE rn >= ?";

        return getHeaderParamHTML(
        		mySQL,
        		pParam,
                1,
                "",
            	0,
            	"",
            	"../crm/club/relationshipspecs.jsp?id=:FIELD6:", 
            	relationshipXML,
            	pPrint);
    } 
    
    public String getClubRelationshipsNeededHeadHTML(String cdType, String pFind, String p_beg, String p_end, String pPrint) {

    	ArrayList<bcFeautureParam> pParam = initParamArray();

    	String mySQL =  
        	" SELECT rn, name_club_rel_type, sname_party1 sname_party1_full, sname_party2 sname_party2_full, id_club_rel " +
      	  	"   FROM (SELECT ROWNUM rn, sname_party1, sname_party2, name_club_rel_type, id_club_rel " +
          	"   		  FROM (SELECT * " +
          	"                   FROM " + getGeneralDBScheme()+".vc_club_rel_need_club_all " +
          	"                  WHERE 1=1 ";
    	if (!isEmpty(cdType)) {
        	mySQL = mySQL + " AND cd_club_rel_type = ? ";
        	pParam.add(new bcFeautureParam("string", pFind));
        }
    	if (!isEmpty(pFind)) {
    		mySQL = mySQL + " AND (UPPER(sname_party1) LIKE UPPER('%'||?||'%') OR " +
    			"UPPER(sname_party2) LIKE UPPER('%'||?||'%')) ";
    		for (int i=0; i<2; i++) {
    		    pParam.add(new bcFeautureParam("string", pFind));
    		}
    	}
    	pParam.add(new bcFeautureParam("int", p_end));
    	pParam.add(new bcFeautureParam("int", p_beg));
        mySQL = mySQL + 
          	  "                  ORDER BY name_club_rel_type, sname_party1, sname_party2) " +
          	  "          WHERE ROWNUM < ?) " +
          	  "  WHERE rn >= ?";

        return getHeaderParamHTML(
        		mySQL,
        		pParam,
                1,
                "",
            	0,
            	"",
            	"../crm/club/relationshipupdate.jsp?type=general&action=addneeded&process=no&id=:FIELD5:", 
            	relationshipXML,
            	pPrint);
    } 
    
	public String getTERMCardHeadHTML(String pFind, String id_card_status, String p_beg, String p_end, String pPrint) {
        StringBuilder html = new StringBuilder();
        
        PreparedStatement st = null;
        
        String myHyperLink = "";

        String myFont = "<font>";
        String myFontEnd = "</font>";
        String myBgColor = noneBackGroundStyle;
        
		ArrayList<bcFeautureParam> pParam = initParamArray();

		try{
        	setLastResultSetRowCount("0");
        	
            
        	String mySQL = 
            	"SELECT rn, cd_card1, fio_nat_prs name_nat_prs, name_card_status, name_card_type, " +
            	"		name_card_state, nt_icc, " +
            	"		card_serial_number, id_issuer, id_payment_system, id_card_state, id_card " +
            	"  FROM (SELECT ROWNUM rn, cd_card1, fio_nat_prs, " +
            	"				name_card_type, name_card_status,  " +
            	"				name_card_state, nt_icc, id_issuer, " +
            	"				id_payment_system, card_serial_number, id_card_state, id_card " +
            	"		  FROM (SELECT a.* " +
            	"                 FROM " + getGeneralDBScheme()+".vc_card_find_all a " +
            	"                WHERE 1=1 ";
            if (!isEmpty(id_card_status)) {
            	mySQL = mySQL + " AND id_card_status = ? ";
            	pParam.add(new bcFeautureParam("int", id_card_status));
            }
            if (!isEmpty(pFind)) {
            	mySQL = mySQL +
            		" AND (cd_card1 LIKE '%'||?||'%' " +
            		"    OR card_serial_number LIKE UPPER('%'||?||'%')" +
            		"    OR UPPER(full_name) LIKE UPPER('%'||?||'%'))";
            	for (int i=0; i<3; i++) {
            	    pParam.add(new bcFeautureParam("string", pFind));
            	}
            }
            pParam.add(new bcFeautureParam("int", p_end));
            pParam.add(new bcFeautureParam("int", p_beg));
            mySQL = mySQL + ") WHERE ROWNUM < ?)  WHERE rn >= ?";

        	
        	LOGGER.debug(prepareSQLToLog(mySQL, pParam));
        	con = Connector.getConnection(getSessionId());
        	
            st = con.prepareStatement(mySQL);
            st = prepareParam(st, pParam);
            ResultSet rset = st.executeQuery();
            ResultSetMetaData mtd = rset.getMetaData();
            
            int colCount = mtd.getColumnCount();
            if ("Y".equalsIgnoreCase(pPrint)) {
            	html.append(getPrintTable());
            } else {
            	html.append(getHeaderTable());
            }

            html.append("<thead><tr>\n");

            if (!isEmpty(changeHyperLink)) {
            	html.append("<th>&nbsp;</th>\n");
            }
            
            for (int i=1; i <= colCount-5; i++) {
            	html.append(getBottomFrameTableTH(clubcardXML, mtd.getColumnName(i)));
            }
            if (!isEmpty(deleteHyperLink)) {
            	html.append("<th>&nbsp;</th>\n");
            }
            html.append("</tr></thead><tbody>\n");
            
            while (rset.next())
            {
            	html.append("<tr id=\"elem_"+rset.getString("CARD_SERIAL_NUMBER")+"\" onmouseout=\"coloredOut(this)\" onmouseover=\"coloredOver(this,1)\">");
            	
            	myHyperLink = getHyperLinkHeaderFirst()+"../terminal/card/card_param.jsp?cardid=:FIELD8:"+
            		"&iss=:FIELD9:&paysys=:FIELD10:" + getHyperLinkMiddle();
            	for (int i=1; i <= colCount; i++) {
            		if (rset.getString(i)==null) {
            			myHyperLink = myHyperLink.replace(":FIELD"+i+":", "");
            		} else {
            			myHyperLink = myHyperLink.replace(":FIELD"+i+":", rset.getString(i));
            		}
            	}
            	
            	String columnName = "";
            	if (!isEmpty(changeHyperLink)) {
                	html.append("<td align=\"center\"><div class=\"div_button\" onclick=\"var msg='" + changeConfirmMessage + " \\\'" + rset.getString("CD_CARD1") + "\\\'?';var res=window.confirm(msg);if (res) {ajaxpage('"+changeHyperLink + "id=" + rset.getString("CARD_SERIAL_NUMBER") + "&iss=" + rset.getString("ID_ISSUER") + "&paysys=" + rset.getString("ID_PAYMENT_SYSTEM") + "','div_main');}\">" + "<img vspace=\"0\" hspace=\"0\"" +
            				" src=\"../images/oper/change_button.png\" height=\"16\" align=\"top\" style=\"border: 0px;\"" + 
            				" title=\"" + buttonXML.getfieldTransl("set_current", false)+"\">" + getHyperLinkEnd()+"</td>\n");
                }
            	for (int i=1; i <= colCount-5; i++) {
                	if ("0".equalsIgnoreCase(rset.getString("ID_CARD_STATE"))) {
                		myFont = "<font color=\"black\">";
            			myFontEnd = "</font>";
            			myBgColor = noneBackGroundStyle;
                	} else if ("1".equalsIgnoreCase(rset.getString("ID_CARD_STATE"))) {
                		myFont = "<font color=\"black\">";
            			myFontEnd = "</font>";
            			myBgColor = noneBackGroundStyle;
                	} else if ("2".equalsIgnoreCase(rset.getString("ID_CARD_STATE"))) {
                		myFont = "<font color=\"red\">";
            			myFontEnd = "</font>";
            			myBgColor = selectedBackGroundStyle;
                	} else if ("3".equalsIgnoreCase(rset.getString("ID_CARD_STATE"))) {
                		myFont = "<font color=\"red\">";
            			myFontEnd = "</font>";
            			myBgColor = selectedBackGroundStyle;
                	} else if ("4".equalsIgnoreCase(rset.getString("ID_CARD_STATE"))) {
                		myFont = "<font color=\"red\">";
            			myFontEnd = "</font>";
            			myBgColor = selectedBackGroundStyle;
                	} else if ("5".equalsIgnoreCase(rset.getString("ID_CARD_STATE"))) {
                		myFont = "<font color=\"gray\">";
            			myFontEnd = "</font>";
            			myBgColor = noneBackGroundStyle;
                	} else if ("6".equalsIgnoreCase(rset.getString("ID_CARD_STATE"))) {
                		myFont = "<font color=\"gray\">";
            			myFontEnd = "</font>";
            			myBgColor = noneBackGroundStyle;
                	} else if ("7".equalsIgnoreCase(rset.getString("ID_CARD_STATE"))) {
                		myFont = "<font color=\"green\">";
            			myFontEnd = "</font>";
            			myBgColor = noneBackGroundStyle;
                	} else if ("8".equalsIgnoreCase(rset.getString("ID_CARD_STATE"))) {
                		myFont = "<font color=\"red\">";
            			myFontEnd = "</font>";
            			myBgColor = selectedBackGroundStyle;
                	} else {
               			myFont = "<font color=\"black\">";
               			myFontEnd = "</font>";
               			myBgColor = noneBackGroundStyle;
                	}

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
					if ("RN".equalsIgnoreCase(mtd.getColumnName(i))) {
						html.append(getValue2(rset.getString(i)) + "</td>\n");
					} else {
						html.append(myHyperLink + myFont + getValue2(rset.getString(i)) + myFontEnd + getHyperLinkEnd() + "</td>\n");
					}
            	}
            	if (!isEmpty(deleteHyperLink)) {
            		html.append("<td align=\"center\"><div class=\"div_button\" onclick=\"var msg='" + deleteConfirmMessage + " \\\'" + rset.getString("CD_CARD1") + "\\\'?';var res=window.confirm(msg);if (res) {ajaxpage('"+deleteHyperLink + "id=" + rset.getString("CARD_SERIAL_NUMBER") + "&iss=" + rset.getString("ID_ISSUER") + "&paysys=" + rset.getString("ID_PAYMENT_SYSTEM") + "','div_main');}\">" + "<img vspace=\"0\" hspace=\"0\"" +
            				" src=\"../images/oper/rows/delete.png\" height=\"16\" align=\"top\" style=\"border: 0px;\"" + 
            				" title=\"" + buttonXML.getfieldTransl("set_current", false)+"\">" + getHyperLinkEnd()+"</td>\n");
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
        
        deleteHyperLink = "";
		deleteConfirmMessage = "";
		deleteEntryId = "";
		deleteEntryName = "";
		changeHyperLink = "";
		changeConfirmMessage = "";
		changeEntryId = "";
		changeEntryName = "";
        return html.toString();
	}

    public String getTERMBaseTerminalHeadHTML(String pFind, String cd_type, String cd_status, String p_beg, String p_end, String pPrint) {
        StringBuilder html = new StringBuilder();
        PreparedStatement st = null; 

        ArrayList<bcFeautureParam> pParam = initParamArray();

        String mySQL = 
    		" SELECT rn, id_term, term_serial_number, name_term_type, " +
    		"        name_term_status, date_last_ses, description " +
        	"	FROM (SELECT ROWNUM rn, id_term, term_serial_number, " +
        	"                DECODE(cd_term_type, " +
			"                  		'PHYSICAL', '<b><font color=\"green\">'||name_term_type||'</font></b>', " +
			"                  		'WEBPOS', '<b><font color=\"blue\">'||name_term_type||'</font></b>', " +
			"                  		name_term_type" +
			"                ) name_term_type, " +
            "                DECODE(cd_term_status, " +
			"                  		'ACTIVE', '<b><font color=\"green\">'||name_term_status||'</font></b>', " +
			"                  		'SETTING', '<b><font color=\"blue\">'||name_term_status||'</font></b>', " +
			"                 		'EXCLUDED', '<b><font color=\"gray\">'||name_term_status||'</font></b>', " +
			"                  		'BLOCKED', '<b><font color=\"red\">'||name_term_status||'</font></b>', " +
			"                  		name_term_status" +
			"                ) name_term_status, " +
            "                cd_term_status, description, " +
            "                CASE WHEN TRUNC(date_last_ses) < TRUNC(SYSDATE)-1" +
        	"                       THEN '<font color=\"red\"><b>'||date_last_ses_frmt||'</b></font>'" +
        	"                     WHEN TRUNC(date_last_ses) = TRUNC(SYSDATE)-1" +
        	"                       THEN '<font color=\"brown\"><b>'||date_last_ses_frmt||'</b></font>'" +
        	"                     ELSE '<font color=\"green\">'||date_last_ses_frmt||'</font>' " +
        	"                END date_last_ses " +
        	"	        FROM (SELECT * " +
        	"                   FROM " + getGeneralDBScheme()+".base$vc_term_all" +
        	"                  WHERE 1=1 ";
    	if (!isEmpty(cd_type)) {
    		mySQL = mySQL + " AND cd_term_type = ? ";
    		pParam.add(new bcFeautureParam("string", cd_type));
    	}
    	if (!isEmpty(cd_status)) {
    		mySQL = mySQL + " AND cd_term_status = ? ";
    		pParam.add(new bcFeautureParam("string", cd_status));
    	}
    	if (!isEmpty(pFind)) {
    		mySQL = mySQL + 
    			" AND (TO_CHAR(UPPER(id_term)) like UPPER('%'||?||'%') OR " +
    			"  UPPER(id_term_hex) like UPPER('%'||?||'%') OR " +
    			//"  UPPER(id_term_nbu) like UPPER('%'||?||'%') OR " +
    			"  UPPER(date_last_ses_frmt) like UPPER('%'||?||'%') OR " +
    			"  UPPER(description) like UPPER('%'||?||'%') " +
    			") ";
    		for (int i=0; i<6; i++) {
    		    pParam.add(new bcFeautureParam("string", pFind));
    		}
    	}
    	pParam.add(new bcFeautureParam("int", p_end));
    	pParam.add(new bcFeautureParam("int", p_beg));
    	mySQL = mySQL + ") WHERE ROWNUM < ?) WHERE rn >= ?";
        
        boolean hasEditPermission = true;
        
        String myFont = "";
        String myFontEnd = "";
        
        int rowCnt = 0;        
        try{
        	setLastResultSetRowCount("0");
            LOGGER.debug(prepareSQLToLog(mySQL, pParam));
       	 	con = Connector.getConnection(getSessionId());
            st = con.prepareStatement(mySQL);
            st = prepareParam(st, pParam);
            ResultSet rset = st.executeQuery();
            ResultSetMetaData mtd = rset.getMetaData();
            
            int colCount = mtd.getColumnCount();
            if (hasEditPermission && "N".equalsIgnoreCase(pPrint)) {
            	html.append("<form method=\"Post\" name=\"updateForm\" id=\"updateForm\" action=\"../terminal/term/change_termupdate.jsp\" onSubmit=\"return CheckSelect(this);\">\n");
           	 	html.append("<input type=\"hidden\" name=\"type\" value=\"term\">\n");
           	 	html.append("<input type=\"hidden\" name=\"action\" value=\"get_base\">\n");
           	 	html.append("<input type=\"hidden\" name=\"process\" value=\"yes\">\n");
            }
            
            if ("Y".equalsIgnoreCase(pPrint)) {
            	html.append(getPrintTable());
            } else {
            	html.append(getHeaderTable());
            }
            html.append("<thead><tr>\n");
            if (hasEditPermission && "N".equalsIgnoreCase(pPrint)) {
           	 	html.append("<th>");
           	 	html.append(getSubmitButtonAjax3("../terminal/term/change_termupdate.jsp", "button_get_base", "CheckSelect(document.getElementById('updateForm'))"));
            	html.append("<br><input name=\"mainCheck\" id=\"mainCheck\" type=\"checkbox\" onclick=\"CheckAll(this,'chb')\"></th>\n");
            } 
       
            for (int i=1; i <= colCount; i++) {
               	html.append(getBottomFrameTableTH(terminalXML, mtd.getColumnName(i)));
            }
            html.append("</tr></thead>\n");

            //html.append("</div>\n");
            //html.append("<div id=\"div_data\">\n");
            //html.append("<div id=\"div_data_detail\">\n");
            html.append("<tbody>\n");
            
            while (rset.next())
            {
            	rowCnt = rowCnt + 1;
           	 html.append("<tr id=\"term_"+rset.getString("ID_TERM")+"\" onmouseout=\"coloredOut(this)\" onmouseover=\"coloredOver(this,1)\">");
           	 if (hasEditPermission && "N".equalsIgnoreCase(pPrint)) {
           		 html.append("<td align=\"center\"><input type=\"checkbox\" name=\"chb"+rset.getString("ID_TERM")+"\" value=\"\" id=\"chb"+rset.getString("ID_TERM")+"\" onclick=\"return CheckCB(this);\"></td>\n");
           	 } 
                html.append(
                		"<td>" + myFont + getValue2(rset.getString(1)) + myFontEnd + "</td>\n" +
                		"<td>" + myFont + getValue2(rset.getString(2)) + myFontEnd + "</td>\n" +
                		"<td>" + myFont + getValue2(rset.getString(3)) + myFontEnd + "</td>\n" + 
                		"<td>" + myFont + getValue2(rset.getString(4)) + myFontEnd + "</td>\n" + 
                		"<td>" + myFont + getValue2(rset.getString(5)) + myFontEnd + "</td>\n" + 
                		"<td>" + myFont + getValue2(rset.getString(6)) + myFontEnd + "</td>\n" + 
                		"<td>" + myFont + getValue2(rset.getString(7)) + myFontEnd + "</td>\n"
                );
            }
            setLastResultSetRowCount(rowCnt + "");
            html.append("</tbody></table>\n");
            html.append("</form\n");
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
       } // getEventsHTML

    public String getTERMBaseCardHeadHTML(String pFind, String id_card_status, String p_beg, String p_end, String pPrint) {
        StringBuilder html = new StringBuilder();
        PreparedStatement st = null; 

        ArrayList<bcFeautureParam> pParam = initParamArray();

        String mySQL = 
        	"SELECT rn, cd_card1, name_nat_prs, name_card_status, name_card_type, " +
        	"		name_card_state, nt_icc, " +
        	"		card_serial_number, id_issuer, id_payment_system, id_card_state, id_card " +
        	"  FROM (SELECT ROWNUM rn, cd_card1, full_name name_nat_prs, " +
        	"				name_card_type, name_card_status,  " +
        	"				name_card_state, nt_icc, id_issuer, " +
        	"				id_payment_system, card_serial_number, id_card_state, " +
        	"               card_serial_number||'_'||TO_CHAR(id_issuer)||'_'||TO_CHAR(id_payment_system) id_card " +
        	"		  FROM (SELECT a.* " +
        	"                 FROM " + getGeneralDBScheme()+".base$vc_card_find_all a " +
        	"                WHERE 1=1 ";
        if (!isEmpty(id_card_status)) {
        	mySQL = mySQL + " AND id_card_status = ? ";
        	pParam.add(new bcFeautureParam("int", id_card_status));
        }
        if (!isEmpty(pFind)) {
        	mySQL = mySQL +
        		" AND (cd_card1 LIKE '%'||?||'%' " +
        		"    OR card_serial_number LIKE UPPER('%'||?||'%')" +
        		"    OR UPPER(full_name) LIKE UPPER('%'||?||'%'))";
        	for (int i=0; i<3; i++) {
        	    pParam.add(new bcFeautureParam("string", pFind));
        	}
        }
        pParam.add(new bcFeautureParam("int", p_end));
        pParam.add(new bcFeautureParam("int", p_beg));
        mySQL = mySQL + ") WHERE ROWNUM < ?)  WHERE rn >= ?";
        
        boolean hasEditPermission = true;
        
        String myFont = "";
        String myFontEnd = "";
        
        int rowCnt = 0;        
        try{
        	setLastResultSetRowCount("0");
            LOGGER.debug(prepareSQLToLog(mySQL, pParam));
       	 	con = Connector.getConnection(getSessionId());
            st = con.prepareStatement(mySQL);
            st = prepareParam(st, pParam);
            ResultSet rset = st.executeQuery();
            ResultSetMetaData mtd = rset.getMetaData();
            
            int colCount = mtd.getColumnCount();
            if (hasEditPermission && "N".equalsIgnoreCase(pPrint)) {
            	html.append("<form method=\"Post\" name=\"updateForm\" id=\"updateForm\" action=\"../terminal/card/changeupdate.jsp\" onSubmit=\"return CheckSelect(this);\">\n");
           	 	html.append("<input type=\"hidden\" name=\"type\" value=\"card\">\n");
           	 	html.append("<input type=\"hidden\" name=\"action\" value=\"get_base\">\n");
           	 	html.append("<input type=\"hidden\" name=\"process\" value=\"yes\">\n");
            }
            
            if ("Y".equalsIgnoreCase(pPrint)) {
            	html.append(getPrintTable());
            } else {
            	html.append(getHeaderTable());
            }
            html.append("<thead><tr>\n");
            if (hasEditPermission && "N".equalsIgnoreCase(pPrint)) {
           	 	html.append("<th>");
           	 	html.append(getSubmitButtonAjax3("../terminal/card/changeupdate.jsp", "button_get_base", "CheckSelect(document.getElementById('updateForm'))"));
            	html.append("<br><input name=\"mainCheck\" id=\"mainCheck\" type=\"checkbox\" onclick=\"CheckAll(this,'chb')\"></th>\n");
            } 
       
            for (int i=1; i <= colCount-5; i++) {
               	html.append(getBottomFrameTableTH(clubcardXML, mtd.getColumnName(i)));
            }
            html.append("</tr></thead>\n");

            //html.append("</div>\n");
            //html.append("<div id=\"div_data\">\n");
            //html.append("<div id=\"div_data_detail\">\n");
            html.append("<tbody>\n");
            
            while (rset.next())
            {
            	rowCnt = rowCnt + 1;
           	 html.append("<tr id=\"card_"+rset.getString("ID_CARD")+"\" onmouseout=\"coloredOut(this)\" onmouseover=\"coloredOver(this,1)\">");
           	 if (hasEditPermission && "N".equalsIgnoreCase(pPrint)) {
           		 html.append("<td align=\"center\"><input type=\"checkbox\" name=\"chb"+rset.getString("ID_CARD")+"\" value=\"\" id=\"chb"+rset.getString("ID_CARD")+"\" onclick=\"return CheckCB(this);\"></td>\n");
           	 } 
                html.append(
                		"<td>" + myFont + getValue2(rset.getString(1)) + myFontEnd + "</td>\n" +
                		"<td>" + myFont + getValue2(rset.getString(2)) + myFontEnd + "</td>\n" +
                		"<td>" + myFont + getValue2(rset.getString(3)) + myFontEnd + "</td>\n" + 
                		"<td>" + myFont + getValue2(rset.getString(4)) + myFontEnd + "</td>\n" + 
                		"<td>" + myFont + getValue2(rset.getString(5)) + myFontEnd + "</td>\n" + 
                		"<td>" + myFont + getValue2(rset.getString(6)) + myFontEnd + "</td>\n" + 
                		"<td>" + myFont + getValue2(rset.getString(7)) + myFontEnd + "</td>\n"
                );
            }
            setLastResultSetRowCount(rowCnt + "");
            html.append("</tbody></table>\n");
            html.append("</form\n");
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
       } // getEventsHTML
    
    public String getTERMTransCollectionHeadHTML(String pIdTerm, String pFind, String pState, String p_beg, String p_end, String pPrint) {
    	readDateFormat();
    	ArrayList<bcFeautureParam> pParam = initParamArray();
        
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
            "                   FROM " + getGeneralDBScheme() + ".vc_trans_collection_all "+
        	"                  WHERE id_term = ? ";
    	pParam.add(new bcFeautureParam("int", pIdTerm));
    	
        if (!isEmpty(pFind)) {
    		mySQL = mySQL + " AND (TO_CHAR(id_trans_collection) LIKE UPPER('%'||?||'%') OR " +
    			"UPPER(desc_trans_collection) LIKE UPPER('%'||?||'%') OR " + 
    			"TO_CHAR(send_date_trans_collect_frmt) LIKE UPPER('%'||?||'%')) ";
    		for (int i=0; i<3; i++) {
    			pParam.add(new bcFeautureParam("string", pFind));
        	}
    	}
       	if (!isEmpty(pState)) {
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
            	"../terminal/term/all_transspecs.jsp?id=:FIELD2:", 
            	transactionXML,
            	pPrint);
    } //getTermSessionsHeadHTML()
    
    public String getTERMOneTermSessionsForIDHeadHTML(String pIdSession1, String pIdSession2, String pIdSession3, String p_beg, String p_end, String pPrint) {
    	readDateFormat();
    	ArrayList<bcFeautureParam> pParam = initParamArray();
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
            "                   FROM " + getGeneralDBScheme() + ".vc_term_ses_all "+
        	"                  WHERE id_term_ses = ? " ;
        pParam.add(new bcFeautureParam("int", pIdSession1));
        if (!(isEmpty(pIdSession2) || "null".equalsIgnoreCase(pIdSession2))) {
            mySQL = mySQL +
            	"                     OR id_term_ses = ? ";
            pParam.add(new bcFeautureParam("int", pIdSession2));
        }
        if (!(isEmpty(pIdSession3) || "null".equalsIgnoreCase(pIdSession3))) {
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
            	"../terminal/term/term_sespecs.jsp?id=:FIELD2:", 
            	term_sesXML,
            	pPrint);
    } //getTermSessionsHeadHTML()
    
    public String getTERMTransactionsForInterfaceOperHeadHTML(String pIdInterfaceOper, String p_beg, String p_end, String pPrint) {

    	ArrayList<bcFeautureParam> pParam = initParamArray();

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
	    		"                   FROM " + getGeneralDBScheme()+".vc_trans_all " +
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
               	"../terminal/card/pay.jsp?id=:FIELD2:", 
               	transactionXML,
               	pPrint));

        return html.toString();
    } //getTransactionsHeadHTML
    
    public String getTERMTransactionsForCardHeadHTML(String pCardSerialNumber, String pIdIssuer, String pIdPaymentSystem, String pFind, String p_beg, String p_end, String pPrint) {

    	ArrayList<bcFeautureParam> pParam = initParamArray();

    	StringBuilder html = new StringBuilder();
        String mySQL =  
                " SELECT rn, id_trans, type_trans_txt, state_trans_tsl, sys_date_frmt, " +
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
	    		"                   FROM " + getGeneralDBScheme()+".vc_trans_all " +
	    		"                  WHERE 1=1 ";
        if (!isEmpty(pCardSerialNumber)) {
        	mySQL = mySQL +
	    		"                    AND card_serial_number = ? " +
	    		"                    AND id_issuer = ? " +
	    		"                    AND id_payment_system = ? ";
        	pParam.add(new bcFeautureParam("string", pCardSerialNumber));
        	pParam.add(new bcFeautureParam("int", pIdIssuer));
        	pParam.add(new bcFeautureParam("int", pIdPaymentSystem));
        }
        if (!isEmpty(pFind)) {
    		mySQL = mySQL + " AND (TO_CHAR(id_trans) LIKE UPPER('%'||?||'%') OR " +
    			"UPPER(sys_date_frmt) LIKE UPPER('%'||?||'%') OR " + 
    			"TO_CHAR(cd_card1) LIKE UPPER('%'||?||'%') OR " + 
    			"TO_CHAR(opr_sum_frmt) LIKE UPPER('%'||?||'%') OR " + 
    			"TO_CHAR(sum_pay_cash_frmt) LIKE UPPER('%'||?||'%') OR " + 
    			"TO_CHAR(sum_pay_card_frmt) LIKE UPPER('%'||?||'%') OR " + 
    			"TO_CHAR(sum_pay_bon_frmt) LIKE UPPER('%'||?||'%') OR " + 
    			"TO_CHAR(sum_bon_frmt) LIKE UPPER('%'||?||'%') OR " + 
    			"TO_CHAR(sum_disc_frmt) LIKE UPPER('%'||?||'%')) ";
    		for (int i=0; i<9; i++) {
    			pParam.add(new bcFeautureParam("string", pFind));
        	}
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
               	"../terminal/card/transspecs.jsp?id=:FIELD2:", 
               	transactionXML,
               	pPrint));

        return html.toString();
    } //getTransactionsHeadHTML
    
    public String getTERMTermSessionsHeadHTML(String pFind, String pAction, String p_beg, String p_end, String pPrint) {
    	readDateFormat();
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
            "                   FROM " + getGeneralDBScheme() + ".vc_term_ses_all "+
        	"                  WHERE 1=1 ";
        
        ArrayList<bcFeautureParam> pParam = initParamArray();
        if (!isEmpty(pFind)) {
    		mySQL = mySQL + " AND (TO_CHAR(id_term_ses) LIKE UPPER('%'||?||'%') OR " +
    			"UPPER(date_term_ses_frmt) LIKE UPPER('%'||?||'%') OR " + 
    			"TO_CHAR(id_term) LIKE UPPER('%'||?||'%') OR " + 
    			"UPPER(desc_term_ses) LIKE UPPER('%'||?||'%')) ";
    		for (int i=0; i<4; i++) {
    			pParam.add(new bcFeautureParam("string", pFind));
        	}
    	}
       	if (!isEmpty(pAction)) {
       		if ("MONITOR".equalsIgnoreCase(pAction)) {
           		mySQL = mySQL + " AND is_monitoring = ? ";    
           		pParam.add(new bcFeautureParam("int", "1"));   			
       		} else if ("CARD_ONLINE".equalsIgnoreCase(pAction)) {
           		mySQL = mySQL + " AND is_card_online = ? ";    
           		pParam.add(new bcFeautureParam("int", "1"));   				
       		} else if ("ONLINE_PAY".equalsIgnoreCase(pAction)) {
           		mySQL = mySQL + " AND is_online_pay = ? ";    
           		pParam.add(new bcFeautureParam("int", "1"));   			
       		} else if ("COL_DATA".equalsIgnoreCase(pAction)) {
           		mySQL = mySQL + " AND is_col_data = ? ";    
           		pParam.add(new bcFeautureParam("int", "1"));   			
       		} else if ("PARAM".equalsIgnoreCase(pAction)) {
           		mySQL = mySQL + " AND is_param = ? ";    
           		pParam.add(new bcFeautureParam("int", "1"));   		
       		}
       	}
        pParam.add(new bcFeautureParam("int", p_end));
		pParam.add(new bcFeautureParam("int", p_beg));
    	mySQL = mySQL + 
        		"  				   ORDER BY id_term_ses desc) " +
        		"		   WHERE ROWNUM < ?)" +
        		" WHERE rn >= ?";
    	
    	

        return getHeaderParamHTML(
        		mySQL,
        		pParam,
                0,
                "D",
            	0,
            	"",
            	"../terminal/term/term_sespecs.jsp?id=:FIELD2:", 
            	term_sesXML,
            	pPrint);
    } //getTermSessionsHeadHTML()
    
    public String getTERMTransactionsForTerminalHeadHTML(String pIdTerm, String pFind, String p_beg, String p_end, String pPrint) {

    	ArrayList<bcFeautureParam> pParam = initParamArray();

    	StringBuilder html = new StringBuilder();
        String mySQL =  
                " SELECT rn, id_trans, type_trans_txt, state_trans_tsl, sys_date_frmt, " +
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
	    		"                   FROM " + getGeneralDBScheme()+".vc_trans_all " +
	    		"                  WHERE 1=1 ";
        if (!isEmpty(pIdTerm)) {
        	mySQL = mySQL +
	    		"                    AND id_term = ? ";
        	pParam.add(new bcFeautureParam("int", pIdTerm));
        }
        if (!isEmpty(pFind)) {
    		mySQL = mySQL + " AND (TO_CHAR(id_trans) LIKE UPPER('%'||?||'%') OR " +
    			"UPPER(sys_date_frmt) LIKE UPPER('%'||?||'%') OR " + 
    			"TO_CHAR(cd_card1) LIKE UPPER('%'||?||'%') OR " + 
    			"TO_CHAR(opr_sum_frmt) LIKE UPPER('%'||?||'%') OR " + 
    			"TO_CHAR(sum_pay_cash_frmt) LIKE UPPER('%'||?||'%') OR " + 
    			"TO_CHAR(sum_pay_card_frmt) LIKE UPPER('%'||?||'%') OR " + 
    			"TO_CHAR(sum_pay_bon_frmt) LIKE UPPER('%'||?||'%') OR " + 
    			"TO_CHAR(sum_bon_frmt) LIKE UPPER('%'||?||'%') OR " + 
    			"TO_CHAR(sum_disc_frmt) LIKE UPPER('%'||?||'%')) ";
    		for (int i=0; i<9; i++) {
    			pParam.add(new bcFeautureParam("string", pFind));
        	}
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
               	"../terminal/card/transspecs.jsp?id=:FIELD2:", 
               	transactionXML,
               	pPrint));

        return html.toString();
    } //getTransactionsHeadHTML
}
