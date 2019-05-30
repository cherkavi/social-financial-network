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


public class bcListCardPackage extends bc.objects.bcObject {
	private final static Logger LOGGER=Logger.getLogger(bcListCardPackage.class);
	
	public bcListCardPackage() {
	}
	
	public String getCardPackagesHTML(String pWhereCause, ArrayList<bcFeautureParam> pWhereValue, String pNotVisibleData, String pFindString, String pCardStatus, String pEditLink, String pCopyLink, String pDeleteLink, String p_beg, String p_end) {
		return getCardPackagesHTMLBase(false, pWhereCause, pWhereValue, pNotVisibleData, pFindString, pCardStatus, pEditLink, pCopyLink, pDeleteLink, p_beg, p_end);
	}
	
	public String getCardPackagesHTMLOnlySelect(String pFindString, String pCardStatus, String pGoToLink, String p_beg, String p_end) {
		String pWhereCause = "";
    	
    	ArrayList<bcFeautureParam> pWhereValue = new ArrayList<bcFeautureParam>();
    	
    	return getCardPackagesHTMLBase(true, pWhereCause, pWhereValue, "", pFindString, pCardStatus, pGoToLink, "", "", p_beg, p_end);
	}

	public String getCardPackagesHTMLBase(boolean pOnlySelect, String pWhereCause, ArrayList<bcFeautureParam> pWhereValue, String pNotVisibleData, String pFindString, String pCardStatus, String pEditLink, String pCopyLink, String pDeleteLink, String p_beg, String p_end) {
		StringBuilder html = new StringBuilder();
        Connection con = null;
        PreparedStatement st = null;
        ArrayList<bcFeautureParam> pParam = initParamArray();

        boolean hasPartnerPermission = false;
        boolean hasClubPermission = false;
        boolean hasCardStatusPermission = false;
        boolean hasCardPackagePermission = false;
        
        String mySQL = 
        	" SELECT rn, "+("PARTNER".equalsIgnoreCase(pNotVisibleData)?"":"sname_jur_prs sname_partner, ")+"name_card_status card_status, name_jur_prs_card_pack, total_amount_card_pack_frmt, entrance_fee_frmt, " +
	  		"		membership_fee_frmt, share_fee_frmt, dealer_margin_frmt, agent_margin_frmt, " +
	  		"       /*action_date_beg_frmt, action_date_end_frmt,*/ id_jur_prs_card_pack, " +
	  		"       id_card_status, id_jur_prs, id_club " +
	  		"  FROM (SELECT ROWNUM rn, sname_jur_prs, name_jur_prs_card_pack, name_card_status," +
	  		"				CASE WHEN NVL(entrance_fee,0) > 0 " +
   			"             		 THEN '<font color=\"blue\">'||entrance_fee_frmt||'</font>'||' '||sname_currency " +
   			"             		 ELSE '' " +
   			"        		END entrance_fee_frmt, " +
	  		"				CASE WHEN NVL(membership_fee,0) > 0 " +
   			"           		 THEN '<font color=\"blue\">'||membership_fee_frmt||'</font>'||' '||sname_currency||' ('||membership_fee_month_count||'"+webposXML.getfieldTransl("cheque_target_prg_poriod_month", false)+")' " +
   			"           		 ELSE '' " +
   			"        		END membership_fee_frmt, " +
	  		"               CASE WHEN NVL(share_fee,0) > 0 " +
   			"             		 THEN '<font color=\"blue\">'||share_fee_frmt||'</font>'||' '||sname_currency " +
   			"             		 ELSE '' " +
   			"        		END share_fee_frmt, " +
	  		"				CASE WHEN NVL(dealer_margin,0) > 0 " +
   			"             		 THEN '<font color=\"blue\">'||dealer_margin_frmt||'</font>'||' '||sname_currency " +
   			"             		 ELSE '' " +
   			"        		END dealer_margin_frmt, " +
	  		"               CASE WHEN NVL(agent_margin,0) > 0 " +
   			"             		 THEN '<font color=\"blue\">'||agent_margin_frmt||'</font>'||' '||sname_currency " +
   			"             		 ELSE '' " +
   			"        		END agent_margin_frmt, " +
	  		"               CASE WHEN NVL(total_amount_jp_card_pack,0) > 0 " +
   			"             		 THEN '<font color=\"red\">'||total_amount_jp_card_pack_frmt||'</font>'||' '||sname_currency " +
   			"             		 ELSE '' " +
   			"        		END total_amount_card_pack_frmt, " +
	  		"				action_date_beg_frmt, action_date_end_frmt, id_jur_prs_card_pack, " +
	  		"               id_card_status, id_jur_prs, id_club " +
	  		"          FROM (SELECT a.* " +
            "				   FROM " + getGeneralDBScheme() + ".vc_club_jp_card_pack_club_all a" +
        	(isEmpty(pWhereCause)?" WHERE 1=1 ":pWhereCause);
        
	   	if (pWhereValue.size() > 0) {
	   		for(int counter=0; counter<=pWhereValue.size()-1;counter++){
	   			pParam.add(pWhereValue.get(counter));
	   		}
	   	}
	        
	   	if (!isEmpty(pFindString)) {
    		mySQL = mySQL + 
    			"  AND (UPPER(sname_jur_prs) LIKE UPPER('%'||?||'%') OR " +
    			"		UPPER(name_jur_prs_card_pack) LIKE UPPER('%'||?||'%') OR " +
    			"		UPPER(total_amount_jp_card_pack_frmt) LIKE UPPER('%'||?||'%')) ";
    		for (int i=0; i<3; i++) {
    		    pParam.add(new bcFeautureParam("string", pFindString));
    		}
    	}
	   	if (!isEmpty(pCardStatus)) {
    		mySQL = mySQL + " AND id_card_status = ? ";
    		pParam.add(new bcFeautureParam("string", pCardStatus));
    	}

        pParam.add(new bcFeautureParam("int", p_end));
        pParam.add(new bcFeautureParam("int", p_beg));
        mySQL = mySQL +
            "   ORDER BY sname_jur_prs, name_jur_prs_card_pack)  WHERE ROWNUM < ? " + 
            " ) WHERE rn >= ?";
        try{
        	if (isEditMenuPermited("CLIENTS_YURPERSONS")>=0) {
        		hasPartnerPermission = true;
            }
        	if (isEditMenuPermited("CLUB_CLUB")>=0) {
        		hasClubPermission = true;
            }
        	if (isEditMenuPermited("CARDS_CARDSETTING")>=0) {
        		hasCardStatusPermission = true;
            }
        	if (isEditMenuPermited("CRM_CLUB_CARD_PACKAGE")>=0) {
        		hasCardPackagePermission = true;
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
            	html.append(getBottomFrameTableTH(clubcardXML, mtd.getColumnName(i)));
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
          	  		if ("SNAME_CLUB".equalsIgnoreCase(mtd.getColumnName(i)) &&
          	  			hasClubPermission && !pOnlySelect) {
          	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/club/clubspecs.jsp?id="+rset.getString("ID_CLUB"), "", ""));
          	  		} else if ("CARD_STATUS".equalsIgnoreCase(mtd.getColumnName(i)) &&
          	  			hasCardStatusPermission && !pOnlySelect) {
          	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/cards/cardsettingspecs.jsp?id="+rset.getString("ID_CARD_STATUS") + "&id_club="+rset.getString("ID_CLUB"), "", ""));
          	  		} else if ("NAME_JUR_PRS_CARD_PACK".equalsIgnoreCase(mtd.getColumnName(i)) &&
          	  			hasCardPackagePermission) {
          	  			if (!pOnlySelect) {
          	  				html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/club/card_packagespecs.jsp?id="+rset.getString("ID_JUR_PRS_CARD_PACK"), "", ""));
          	  			} else {
          	  				html.append(getBottomFrameTableTDWithDiv(mtd.getColumnName(i), rset.getString(i), pEditLink+"&id_jur_prs_card_pack="+rset.getString("ID_JUR_PRS_CARD_PACK"), "", "", "div_data_detail"));
          	  			}
          	  		} else if ("SNAME_PARTNER".equalsIgnoreCase(mtd.getColumnName(i)) &&
          	  			hasPartnerPermission && !pOnlySelect) {
          	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/clients/yurpersonspecs.jsp?id="+rset.getString("ID_JUR_PRS"), "", ""));
          	  		} else {
          	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", "", ""));
          	  		}
          	  	}
          	  	if (!isEmpty(pDeleteLink) && !pOnlySelect) {
            	   String myDeleteLink = pDeleteLink+"&id_jur_prs_card_pack="+rset.getString("ID_JUR_PRS_CARD_PACK");
                   html.append(getDeleteButtonHTML(myDeleteLink, clubcardXML.getfieldTransl("h_delete_pack", false), rset.getString("CARD_STATUS")));
          	  	}
          	  	if (!isEmpty(pCopyLink) && !pOnlySelect) {
             	   String myCopyLink = pEditLink+"&id_jur_prs_card_pack="+rset.getString("ID_JUR_PRS_CARD_PACK");
             	   html.append(getCopyButtonStyleHTML(myCopyLink, "", "div_data_detail"));
                }
          	  	if (!isEmpty(pEditLink) && !pOnlySelect) {
            	   String myEditLink = pEditLink+"&id_jur_prs_card_pack="+rset.getString("ID_JUR_PRS_CARD_PACK");
            	   html.append(getEditButtonWitDivHTML(myEditLink, "div_data_detail"));
                }
          	  	if (pOnlySelect) {
             	   String mySelectLink = pEditLink+"&id_jur_prs_card_pack="+rset.getString("ID_JUR_PRS_CARD_PACK");
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
