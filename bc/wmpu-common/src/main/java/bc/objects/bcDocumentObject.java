package bc.objects;

import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import bc.connection.Connector;
import bc.lists.bcListCardPackage;
import bc.lists.bcListLoyalityScheme;
import bc.lists.bcListReferralScheme;
import bc.lists.bcListTargetProgram;
import bc.service.bcFeautureParam;


public class bcDocumentObject extends bcObject {
	private final static Logger LOGGER=Logger.getLogger(bcDocumentObject.class);	
	private Map<String,Comparable<String>> fieldHm = new HashMap<String, Comparable<String>>();
	
	private String idDoc;
	
	public bcDocumentObject(String pIdDoc) {
		this.idDoc = pIdDoc;
		getFeature();
	}
	
	private void getFeature() {
		String featureSelect = " SELECT * FROM " + getGeneralDBScheme() + ".VC_DOC_PRIV_ALL WHERE id_doc = ?";
		fieldHm = getFeatures2(featureSelect, this.idDoc, false);
	}
	
	public String getValue(String pColumnName) {
		return getFeautureResult(fieldHm, pColumnName);
	}

    public String getComissionHTML(String pFindString, /*String pKind,*/ String pBeg, String pEnd) {

    	ArrayList<bcFeautureParam> pParam = initParamArray();

    	String mySQL = 
    		"SELECT rn, name_comission_type, " +
    		"       sname_jur_prs_payer jur_prs_payer, sname_jur_prs_receiver jur_prs_receiver, " + 
  			"       name_payment_system, /*begin_action_date_frmt, end_action_date_frmt,*/ " +
  			"       fixed_value_frmt, percent_value_frmt, description, exist_flag_tsl, " +
  			"       id_comission, id_doc, id_comission_type, exist_flag, " +
	  		"       payer_has_error, payer_has_error_type_tsl, receiver_has_error, receiver_has_error_type_tsl," +
	  		"       id_jur_prs_payer, id_jur_prs_receiver, is_special_comission " +
  			"  FROM (SELECT ROWNUM rn, name_comission_type, " +
  			"               a.sname_jur_prs_payer, a.sname_jur_prs_receiver, " + 
  			"               a.name_payment_system, a.begin_action_date_frmt, a.end_action_date_frmt, " +
  			"               a.fixed_value_frmt, a.percent_value_frmt, a.description, a.exist_flag_tsl, " +
  			"               a.id_comission, a.id_doc, a.id_comission_type, a.exist_flag, " +
	  		"               a.payer_has_error, a.payer_has_error_type_tsl, " +
	  		"               a.receiver_has_error, a.receiver_has_error_type_tsl," +
	  		"               a.id_jur_prs_payer, a.id_jur_prs_receiver, a.is_special_comission " +
  			"  		   FROM (SELECT * " +
  			"                  FROM " + getGeneralDBScheme() + ".vc_jur_prs_comission_priv_all " +
  			"                 WHERE id_doc = ? ";
    	pParam.add(new bcFeautureParam("int", this.idDoc));
        if (!isEmpty(pFindString)) {
    		mySQL = mySQL + 
   				" AND (UPPER(name_comission_type_full) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(sname_jur_prs_payer) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(sname_jur_prs_receiver) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(begin_action_date_frmt) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(end_action_date_frmt) LIKE UPPER('%'||?||'%'))";
    		for (int i=0; i<5; i++) {
    		    pParam.add(new bcFeautureParam("string", pFindString));
    		}
    	}
        pParam.add(new bcFeautureParam("int", pEnd));
        pParam.add(new bcFeautureParam("int", pBeg));
        mySQL = mySQL +
      	  	"                 ORDER BY name_comission_type_full, sname_jur_prs_payer, sname_jur_prs_receiver) a " +
      	  	"        WHERE ROWNUM < ? " + 
      	  	" ) WHERE rn >= ?";
        
        StringBuilder html = new StringBuilder();
        StringBuilder html_temp = new StringBuilder();
        Connection con = null;
        PreparedStatement st = null;
        int myCnt = 0;
        boolean hasEditPermission = false;
        String myFont = "";
        String myFontEnd = "";
           
        boolean hasPartnerPermission = false;
        boolean hasComisTypePermission = false;
           
        String payerHyperLink = "";
        String receiverHyperLink = "";
  
        try{
        	if (isEditPermited("CLUB_DOCUMENTS_COMISSIONS")>0) {
        		hasEditPermission = true;
        	}
        	if (isEditMenuPermited("CLIENTS_YURPERSONS")>=0) {
        		hasPartnerPermission = true;
        	}
        	if (isEditMenuPermited("CLUB_COMISTYPE")>=0) {
        		hasComisTypePermission = true;
        	}
        	  
        	LOGGER.debug(prepareSQLToLog(mySQL, pParam));
        	con = Connector.getConnection(getSessionId());
            st = con.prepareStatement(mySQL);
            st = prepareParam(st, pParam);
            ResultSet rset = st.executeQuery();
            ResultSetMetaData mtd = rset.getMetaData();
                 
            int colCount = mtd.getColumnCount();
            
            if (hasEditPermission) { 
            	html.append("<form action=\"../crm/club/doccomissionupdate.jsp\" name=\"updateForm\" id=\"updateForm\" accept-charset=\"UTF-8\" method=\"POST\">\n");
            	html.append("<input type=\"hidden\" name=\"type\" value=\"comission\">\n");
            	html.append("<input type=\"hidden\" name=\"action\" value=\"editall\">\n");
            	html.append("<input type=\"hidden\" name=\"process\" value=\"yes\">\n");
            	html.append("<input type=\"hidden\" name=\"id\" value=\""+this.idDoc+"\">\n");
            }
            html.append(getBottomFrameTable());
            html.append("<tr>");
            html.append("<th>&nbsp;</th>\n");
            for (int i=2; i <= colCount-11; i++) {
            	html.append(getBottomFrameTableTH(comissionXML, mtd.getColumnName(i)));
            }
            if (hasEditPermission) {
            	html.append("<th>&nbsp;</th><th>&nbsp;</th>\n");
            }
            html.append("</tr></thead><tbody>\n");
         
         while (rset.next())
         {
        	 myCnt = myCnt + 1;
        	 if ("N".equalsIgnoreCase(rset.getString("EXIST_FLAG"))) {
        		 myFont = "<b><font color=\"red\">";
        		 myFontEnd = "</font></b>";
        	 } else if ("Y".equalsIgnoreCase(rset.getString("IS_SPECIAL_COMISSION"))) {
        		 myFont = "<b><font color=\"green\">";
        		 myFontEnd = "</font></b>";
        	 } else {
        		 myFont = "";
        		 myFontEnd = "";
        	 }
        	 
        	 if (!isEmpty(rset.getString("ID_JUR_PRS_PAYER")) && hasPartnerPermission) {
         		payerHyperLink = getHyperLinkFirst()+"../crm/clients/yurpersonspecs.jsp?id="+rset.getString("ID_JUR_PRS_PAYER") + getHyperLinkMiddle();
         	} else {
         		payerHyperLink = "";
         	}
         	
         	if (!isEmpty(rset.getString("ID_JUR_PRS_RECEIVER")) && hasPartnerPermission) {
         		receiverHyperLink = getHyperLinkFirst()+"../crm/clients/yurpersonspecs.jsp?id="+rset.getString("ID_JUR_PRS_RECEIVER") + getHyperLinkMiddle();
         	} else {
         		receiverHyperLink = "";
         	}
        	 
         	html_temp.append("<tr><td>" + myFont + getValue2(rset.getString("RN")) +  myFontEnd + "</td>\n");
         	
 			 if (hasComisTypePermission) {
 				html_temp.append("<td>" + myFont + getHyperLinkFirst()+"../crm/club/comistypespecs.jsp?id="+rset.getString("ID_COMISSION_TYPE") + getHyperLinkMiddle() + getValue2(rset.getString("NAME_COMISSION_TYPE")) + getHyperLinkEnd() + myFontEnd + "</td>");
   		 	 } else {
   		 		html_temp.append("<td>" + myFont + getValue2(rset.getString("NAME_COMISSION_TYPE")) +  myFontEnd + "</td>");
   			 }

         	html_temp.append("<td>" + myFont + payerHyperLink + getValue2(rset.getString("JUR_PRS_PAYER")) + getHyperLinkEnd() + myFontEnd);
        	 if (!"N".equalsIgnoreCase(rset.getString("PAYER_HAS_ERROR"))) {
        		 html_temp.append("&nbsp;<img vspace=\"0\" hspace=\"0\" src=\"../images/oper/rows/warning.png\" align=\"top\" style=\"border: 0px;\" title=\""+rset.getString("PAYER_HAS_ERROR_TYPE_TSL")+"\">");
        	 }
        	 html_temp.append("</td><td>" + myFont + receiverHyperLink + getValue2(rset.getString("JUR_PRS_RECEIVER")) + getHyperLinkEnd() + myFontEnd);
        	 if (!"N".equalsIgnoreCase(rset.getString("RECEIVER_HAS_ERROR"))) {
        		 html_temp.append("&nbsp;<img vspace=\"0\" hspace=\"0\" src=\"../images/oper/rows/warning.png\" align=\"top\" style=\"border: 0px;\" title=\""+rset.getString("RECEIVER_HAS_ERROR_TYPE_TSL")+"\">");
        	 }
        	 html_temp.append("</td><td>" + myFont + getValue2(rset.getString("NAME_PAYMENT_SYSTEM")) +  myFontEnd + 
            		   //"</td><td>" + myFont + getValue2(rset.getString("BEGIN_ACTION_DATE_FRMT")) +  myFontEnd + 
            		   //"</td><td>" + myFont + getValue2(rset.getString("END_ACTION_DATE_FRMT")) +  myFontEnd + 
            		   "</td>\n");
             if (hasEditPermission) {
            	 html_temp.append("<input type=\"hidden\" name=\"nameparam"+myCnt+"\" value=\""+rset.getString("ID_COMISSION")+"\">");
            	 html_temp.append("<td><input type=\"text\" name=\"fixedvalue"+myCnt+"\" size=\"8\" value=\""+getHTMLValue(rset.getString("FIXED_VALUE_FRMT"))+"\" class=\"inputfield\"></td>\n");
            	 html_temp.append("<td><input type=\"text\" name=\"percentvalue"+myCnt+"\" size=\"8\" value=\""+getHTMLValue(rset.getString("PERCENT_VALUE_FRMT"))+"\" class=\"inputfield\"></td>\n");
             } else {
            	 html_temp.append("<td>" + myFont + getValue2(rset.getString("FIXED_VALUE_FRMT")) +  myFontEnd + "</td>\n");
            	 html_temp.append("<td>" + myFont + getValue2(rset.getString("PERCENT_VALUE_FRMT")) +  myFontEnd + "</td>\n");
             }
             html_temp.append("<td>" + myFont + getValue2(rset.getString("DESCRIPTION")) +  myFontEnd + 
          		   "</td><td>" + myFont + getValue2(rset.getString("EXIST_FLAG_TSL")) +  myFontEnd + 
          		   "</td>\n");
             if (hasEditPermission) {
            	String myHyperLink = "../crm/club/doccomissionupdate.jsp?type=comission&id=" + this.idDoc + "&code="+rset.getString("ID_COMISSION");
            	String myDeleteLink = "../crm/club/doccomissionupdate.jsp?type=comission&id=" + this.idDoc + "&code="+rset.getString("ID_COMISSION")+"&action=remove&process=yes";
            	html_temp.append(getDeleteButtonHTML(myDeleteLink, clubXML.getfieldTransl("h_delete_comission", false), rset.getString("NAME_COMISSION_TYPE")));
            	html_temp.append(getEditButtonWitDivHTML(myHyperLink, "div_data_detail"));
             }
             html_temp.append("</tr>\n");
         }
         html.append("<input type=\"hidden\" name=\"paramcount\" value=\""+myCnt+"\">");
         if (hasEditPermission && (myCnt>0)) {
        	 if (myCnt>0) {
        		 html.append("<tr>");
        		 html.append("<td colspan=\"14\" align=\"center\">");
        		 html.append(getSubmitButtonAjax("../crm/club/doccomissionupdate.jsp"));
        		 html.append("</td></tr>\n");
        	 }
         }
         html.append(html_temp.toString());
         if (hasEditPermission && (myCnt>0)) {
        	 if (myCnt>0) {
        		 html.append("<tr>");
        		 html.append("<td colspan=\"14\" align=\"center\">");
        		 html.append(getSubmitButtonAjax("../crm/club/doccomissionupdate.jsp"));
        		 html.append("</td></tr>\n");
        	 }
         }
    	 html.append("</form></table>\n");
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
    } // class getComissionHTML

    public String getCardPackagesHTML(String pFindString, String pCardStatus, String p_beg, String p_end) {
    	bcListCardPackage list = new bcListCardPackage();
    	
    	String pWhereCause = " WHERE id_doc = ? ";
    	
    	ArrayList<bcFeautureParam> pWhereValue = new ArrayList<bcFeautureParam>();
    	pWhereValue.add(new bcFeautureParam("int", this.idDoc));
    	String myDeleteLink = "";
    	String myEditLink = "";
    	String myCopyLink = "";
    	if (isEditPermited("CLUB_DOCUMENTS_CARD_PACK")>0) {
    		myDeleteLink = "../crm/club/card_packageupdate.jsp?back_type=DOC&type=general&id="+this.idDoc+"&action=remove&process=yes";
    	    myEditLink = "../crm/club/card_packageupdate.jsp?back_type=DOC&type=general&id="+this.idDoc;
    	    myCopyLink = "../crm/club/card_packageupdate.jsp?back_type=DOC&type=general&id="+this.idDoc;
    	}
    	
    	//return list.getCardPackagesHTML(pWhereCause, pWhereValue, "", pFindString, pCardStatus, myEditLink, myCopyLink, myDeleteLink, p_beg, p_end);
    	return list.getCardPackagesHTML(pWhereCause, pWhereValue, "", pFindString, pCardStatus, myEditLink, myCopyLink, myDeleteLink, p_beg, p_end);
    	
    }
	
    public String getCardPackagesHTML(String pFind, String p_beg, String p_end) {
    	ArrayList<bcFeautureParam> pParam = initParamArray();
        
        String sNameJurPrsField = "";
        String idJurPrsField = "";
        int jurPrsFieldCount = 0;
        /*if (!"SERVICE_PLACE".equalsIgnoreCase(this.getValue("CD_JUR_PRS_STATUS"))) {
        	sNameJurPrsField = " sname_jur_prs, ";
        	idJurPrsField = " id_jur_prs, ";
        	jurPrsFieldCount = 1;
        }*/

    	String mySQL = 
    		"SELECT rn, " + sNameJurPrsField + " name_card_status, name_jur_prs_card_pack, total_amount_card_pack_frmt, entrance_fee_frmt, " +
	  		"		membership_fee_frmt, share_fee_frmt, dealer_margin_frmt, agent_margin_frmt, " +
	  		"       action_date_beg_frmt, action_date_end_frmt, " + idJurPrsField + " id_jur_prs_card_pack, id_card_status, id_club " +
	  		"  FROM (SELECT ROWNUM rn, " + sNameJurPrsField + " name_jur_prs_card_pack, name_card_status," +
	  		"				total_amount_card_pack_frmt, entrance_fee_frmt, membership_fee_frmt, " +
	  		"				share_fee_frmt, dealer_margin_frmt, agent_margin_frmt, " +
	  		"				action_date_beg_frmt, action_date_end_frmt, " + idJurPrsField + " id_jur_prs_card_pack, id_card_status, id_club " +
	  		"          FROM (SELECT " + sNameJurPrsField + " name_jur_prs_card_pack, name_card_status, sname_currency, " +
	  		"                       CASE WHEN NVL(entrance_fee,0) > 0 " +
   			"             				 THEN '<font color=\"blue\">'||entrance_fee_frmt||'</font>'||' '||sname_currency " +
   			"             				 ELSE '' " +
   			"        				END entrance_fee_frmt, " +
	  		"						CASE WHEN NVL(membership_fee,0) > 0 " +
   			"             				 THEN '<font color=\"blue\">'||membership_fee_frmt||'</font>'||' '||sname_currency||' ('||membership_fee_month_count||'"+clubfundXML.getfieldTransl("target_prg_poriod_month", false)+")' " +
   			"             				 ELSE '' " +
   			"        				END membership_fee_frmt, " +
	  		"                       CASE WHEN NVL(share_fee,0) > 0 " +
   			"             				 THEN '<font color=\"blue\">'||share_fee_frmt||'</font>'||' '||sname_currency " +
   			"             				 ELSE '' " +
   			"        				END share_fee_frmt, " +
	  		"						CASE WHEN NVL(dealer_margin,0) > 0 " +
   			"             				 THEN '<font color=\"blue\">'||dealer_margin_frmt||'</font>'||' '||sname_currency " +
   			"             				 ELSE '' " +
   			"        				END dealer_margin_frmt, " +
	  		"                       CASE WHEN NVL(agent_margin,0) > 0 " +
   			"             				 THEN '<font color=\"blue\">'||agent_margin_frmt||'</font>'||' '||sname_currency " +
   			"             				 ELSE '' " +
   			"        				END agent_margin_frmt, " +
	  		"                       CASE WHEN NVL(total_amount_jp_card_pack,0) > 0 " +
   			"             				 THEN '<font color=\"red\">'||total_amount_jp_card_pack_frmt||'</font>'||' '||sname_currency " +
   			"             				 ELSE '' " +
   			"        				END total_amount_card_pack_frmt," +
	  		"                       action_date_beg_frmt, action_date_end_frmt, " + idJurPrsField + " id_jur_prs_card_pack, " +
	  		"                       id_card_status, id_club " +
            "				   FROM " + getGeneralDBScheme() + ".vc_club_jp_card_pack_club_all a " + 
            "                  WHERE a.id_doc = ? ";
    	pParam.add(new bcFeautureParam("int", this.idDoc));
    	
    	if (!isEmpty(pFind)) {
    		mySQL = mySQL + 
   				" AND (UPPER(name_jur_prs_card_pack) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(total_amount_card_pack_frmt) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(action_date_beg_frmt) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(action_date_end_frmt) LIKE UPPER('%'||?||'%'))";
    		for (int i=0; i<3; i++) {
    		    pParam.add(new bcFeautureParam("string", pFind));
    		}
    	}
        pParam.add(new bcFeautureParam("int", p_end));
        pParam.add(new bcFeautureParam("int", p_beg));
  		mySQL = mySQL + 
  			"                 ORDER BY name_card_status, name_jur_prs_card_pack, action_date_beg_frmt"+
  			"                ) " +
  			"         WHERE ROWNUM < ?" + 
  			") WHERE rn >= ?";
  		
        StringBuilder html = new StringBuilder();
        PreparedStatement st = null;
        Connection con = null;
        //boolean hasEditPermission = false;
        boolean hasCardStatusPermission = false;
        
        try{
        	//if (isEditPermited("CLUB_DOCUMENTS_CARD_PACK")>0) {
        	//	hasEditPermission = true;
        	//}
        	if (isEditMenuPermited("CARDS_CARDSETTING")>=0) {
        		hasCardStatusPermission = true;
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
            for (int i=1; i <= colCount-(3 + jurPrsFieldCount); i++) {
            	html.append(getBottomFrameTableTH(clubcardXML, mtd.getColumnName(i)));
            }
            //if (hasEditPermission) {
            //	html.append("<th>&nbsp;</th><th>&nbsp;</th>\n"); 
            //}
            html.append("</tr></thead><tbody>\n");
            while (rset.next())
            {
                html.append("<tr>\n");
                for (int i=1; i<=colCount-(3 + jurPrsFieldCount); i++) {
                	if ("NAME_CARD_STATUS".equalsIgnoreCase(mtd.getColumnName(i)) && hasCardStatusPermission) {
                		html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/cards/cardsettingspecs.jsp?id="+rset.getString("ID_CARD_STATUS") + "&id_club="+rset.getString("ID_CLUB"), "", ""));
                	} else if ("SNAME_JUR_PRS".equalsIgnoreCase(mtd.getColumnName(i))) {
         	  			if (this.getValue("ID_JUR_PRS").equalsIgnoreCase(rset.getString("ID_JUR_PRS"))) {
         	  				html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", "", ""));
         	  			} else {
              	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/clients/yurpersonspecs.jsp?id="+rset.getString("ID_JUR_PRS"), "", ""));
         	  			}
         	  		} else {
                		html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", "", ""));
                	}

                }
                //if (hasEditPermission) {
	            //	String myHyperLink = "../crm/clients/yurpersonupdate.jsp?type=card_pack&id="+this.idJurPrs+"&id_card_pack="+rset.getString("ID_JUR_PRS_CARD_PACK");
	            //	String myDeleteLink = "../crm/clients/yurpersonupdate.jsp?type=card_pack&id="+this.idJurPrs+"&id_card_pack="+rset.getString("ID_JUR_PRS_CARD_PACK")+"&action=remove&process=yes";
	            //    html.append(getDeleteButtonHTML(myDeleteLink, clubcardXML.getfieldTransl("h_delete_pack", false), rset.getString("NAME_JUR_PRS_CARD_PACK")));
	            //	html.append(getEditButtonHTML(myHyperLink));
	            //}
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
    } // getClubCardsTasksHTML   

    public String getReferralSchemeHTML(String pFindString, String pSchemeType, String pCalcType, String p_beg, String p_end) {
    	bcListReferralScheme list = new bcListReferralScheme();
    	
    	String pWhereCause = " WHERE id_doc = ? ";
    	
    	ArrayList<bcFeautureParam> pWhereValue = new ArrayList<bcFeautureParam>();
    	pWhereValue.add(new bcFeautureParam("int", this.idDoc));
    	String myDeleteLink = "";
    	String myEditLink = "";
    	String myCopyLink = "";
    	if (isEditPermited("CLUB_DOCUMENTS_REFERRAL_SCHEME")>0) {
    		myDeleteLink = "../crm/club/referral_schemeupdate.jsp?back_type=DOC&type=general&id="+this.idDoc+"&action=remove&process=yes";
    	    myEditLink = "../crm/club/referral_schemeupdate.jsp?back_type=DOC&type=general&id="+this.idDoc;
    	    myCopyLink = "../crm/club/referral_schemeupdate.jsp?back_type=DOC&type=general&id="+this.idDoc;
    	}
    	
    	return list.getReferralSchemesHTML(pWhereCause, pWhereValue, pFindString, pSchemeType, pCalcType, myEditLink, myCopyLink, myDeleteLink, p_beg, p_end);
    	
    }

    public String getTargetProgramsHTML(String pFindString, String pPayPeriod, String p_beg, String p_end) {
    	bcListTargetProgram list = new bcListTargetProgram();
    	
    	String pWhereCause = " WHERE id_doc = ? ";
    	
    	ArrayList<bcFeautureParam> pWhereValue = new ArrayList<bcFeautureParam>();
    	pWhereValue.add(new bcFeautureParam("int", this.idDoc));
    	String myDeleteLink = "";
    	String myEditLink = "";
    	String myCopyLink = "";
    	if (isEditPermited("CLUB_DOCUMENTS_BANK_ACCOUNTS")>0) {
    		myDeleteLink = "../crm/club/target_programupdate.jsp?back_type=DOCUMENT&type=general&id="+this.idDoc+"&action=remove&process=yes";
    	    myEditLink = "../crm/club/target_programupdate.jsp?back_type=DOCUMENT&type=general&id="+this.idDoc;
    	    myCopyLink = "../crm/club/target_programupdate.jsp?back_type=DOCUMENT&type=general&id="+this.idDoc;
    	}
    	
    	return list.getTargetProgramsHTML(pWhereCause, pWhereValue, "", pFindString, pPayPeriod, myEditLink, myCopyLink, myDeleteLink, p_beg, p_end);
    	
    }

    public String getLoyalitySchemeHTML(String pFindString, String pCdKindLoyality, String p_beg, String p_end) {
    	bcListLoyalityScheme list = new bcListLoyalityScheme();
    	
    	String pWhereCause = " WHERE id_doc = ? ";
    	
    	ArrayList<bcFeautureParam> pWhereValue = new ArrayList<bcFeautureParam>();
    	pWhereValue.add(new bcFeautureParam("int", this.idDoc));
    	String myDeleteLink = "";
    	String myEditLink = "";
    	String myCopyLink = "";
    	if (isEditPermited("CLUB_DOCUMENTS_LOYALITY_SCHEME")>0) {
    		myDeleteLink = "../crm/clients/loyupdate.jsp?back_type=DOC&type=general&id="+this.idDoc+"&action=remove&process=yes";
    	    myEditLink = "../crm/clients/loyupdate.jsp?back_type=DOC&type=general&id="+this.idDoc;
    	    myCopyLink = "../crm/clients/loyupdate.jsp?back_type=DOC&type=general&id="+this.idDoc;
    	}
    	
    	return list.getLoyalitySchemesHTML(pWhereCause, pWhereValue, "", pFindString, pCdKindLoyality, myEditLink, myCopyLink, myDeleteLink, p_beg, p_end);
    	
    }

    public String getDocBankAccountsHTML(String pFindString, String pType, String p_beg, String p_end) {
        StringBuilder html = new StringBuilder();
        Connection con = null;
        PreparedStatement st = null;

        ArrayList<bcFeautureParam> pParam = initParamArray();

        String mySQL = 
        	" SELECT rn, sname_owner_bank_account, number_bank_account, " +
        	"        sname_bank||' ('||" +
        	"        DECODE(cd_bank_account_type, " +
            "         	    'SETTLEMENT_ACCOUNT', '<font color=\"green\">'||name_bank_account_type||'</font>', " +
            "          	    'CORRESPONDENT_ACCOUNT', '<font color=\"blue\">'||name_bank_account_type||'</font>', " +
            "          	    name_bank_account_type" +
            "  		 )||')' name_bank, name_doc_bank_account_type, " +
            "        id_owner_bank_account, " +
            "        type_owner_bank_account, id_bank_account, id_bank, cd_currency, id_doc_bank_account "+
            "   FROM (SELECT ROWNUM rn, id_bank_account, number_bank_account number_bank_account, " +
            "                DECODE(cd_doc_bank_account_type, " +
            "         	 	        'SETTLEMENT_ACCOUNT', '<font color=\"green\"><b>'||name_doc_bank_account_type||'</b></font>', " +
            "          	 	        'CORRESPONDENT_ACCOUNT', '<font color=\"blue\"><b>'||name_doc_bank_account_type||'</b></font>', " +
            "          		        name_doc_bank_account_type" +
            "  		         ) name_doc_bank_account_type, sname_bank, " +
            "                cd_currency, name_currency, " +
            "                id_owner_bank_account, sname_owner_bank_account, " +
            "                type_owner_bank_account, cd_bank_account_type, name_bank_account_type, " +
            "                id_bank, id_doc_bank_account "+
            "           FROM (SELECT * " +
            "                   FROM " + getGeneralDBScheme() + ".vc_doc_bank_account_all "+
            "                  WHERE id_doc = ? ";
        pParam.add(new bcFeautureParam("int", this.idDoc));
        
	    if (!isEmpty(pFindString)) {
	    		mySQL = mySQL + 
	   				" AND (TO_CHAR(id_bank_account) LIKE UPPER('%'||?||'%') OR " +
	   				"      UPPER(number_bank_account) LIKE UPPER('%'||?||'%') OR " +
	   				"      UPPER(desc_bank_account) LIKE UPPER('%'||?||'%') OR " +
	   				"      UPPER(sname_bank) LIKE UPPER('%'||?||'%'))";
	    		for (int i=0; i<4; i++) {
	    		    pParam.add(new bcFeautureParam("string", pFindString));
	    		}
	    }
	    if (!isEmpty(pType)) {
        	mySQL = mySQL + " AND cd_doc_bank_account_type = ? ";
        	pParam.add(new bcFeautureParam("string", pType));
        }
        
	    pParam.add(new bcFeautureParam("int", p_end));
	    pParam.add(new bcFeautureParam("int", p_beg));
        mySQL = mySQL +
            "                  ORDER BY number_bank_account)" +
            "          WHERE ROWNUM < ?" + 
            " ) WHERE rn >= ?";
        boolean hasBankAccountPermission = false;
        boolean hasCurrencyPermission = false;
        boolean hasPartnerPermission = false;
        boolean hasNatPrsPermission = false;
        boolean hasEditPermission = false;
        try{
        	if (isEditMenuPermited("CLIENTS_BANK_ACCOUNTS")>=0) {
            	hasBankAccountPermission = true;
            }
        	if (isEditMenuPermited("SETUP_CURRENCY")>=0) {
            	hasCurrencyPermission = true;
            }
        	if (isEditMenuPermited("CLIENTS_YURPERSONS")>=0) {
        		hasPartnerPermission = true;
            }
        	if (isEditMenuPermited("CLIENTS_NATPERSONS")>=0) {
        		hasNatPrsPermission = true;
            }
        	if (isEditPermited("CLUB_DOCUMENTS_BANK_ACCOUNTS")>0) {
        		hasEditPermission = true;
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
            for (int i=1; i <= colCount-6; i++) {
                html.append(getBottomFrameTableTH(accountXML, mtd.getColumnName(i)));
            }
            if (hasEditPermission) {
                html.append("<th>&nbsp;</th><th>&nbsp;</th>\n");
            }
            html.append("</tr></thead><tbody>\n");
            while (rset.next())
            {
            	html.append("<tr>");
          	  	for (int i=1; i <= colCount-6; i++) {
          	  		if ("NAME_BANK".equalsIgnoreCase(mtd.getColumnName(i))) {
          	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/clients/yurpersonspecs.jsp?id="+rset.getString("ID_BANK"), "", ""));
          	  		} else if (hasBankAccountPermission && 
          	  				("NUMBER_BANK_ACCOUNT".equalsIgnoreCase(mtd.getColumnName(i)))) {
          	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/clients/accountspecs.jsp?id="+rset.getString("ID_BANK_ACCOUNT"), "", ""));
          	  		} else if ("SNAME_OWNER_BANK_ACCOUNT".equalsIgnoreCase(mtd.getColumnName(i))) {
 	          	  		if ("JUR_PRS".equalsIgnoreCase(rset.getString("TYPE_OWNER_BANK_ACCOUNT")) && hasPartnerPermission) {
 	          	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/clients/yurpersonspecs.jsp?id="+rset.getString("ID_OWNER_BANK_ACCOUNT"), "", ""));
 	          	  		} else if ("NAT_PRS".equalsIgnoreCase(rset.getString("TYPE_OWNER_BANK_ACCOUNT")) && hasNatPrsPermission) {
 	          	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/clients/natpersonspecs.jsp?id="+rset.getString("ID_OWNER_BANK_ACCOUNT"), "", ""));
 	          	  		} else {
 	          	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", "", ""));
 	          	  		}
 	          	  	} else if (hasCurrencyPermission && 
          	  				("NAME_CURRENCY".equalsIgnoreCase(mtd.getColumnName(i)))) {
          	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/setup/currencyspecs.jsp?id="+rset.getString("CD_CURRENCY"), "", ""));
          	  		} else {
          	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", "", ""));
          	  		}
          	  	}
                if (hasEditPermission) {
	            	String myHyperLink = "../crm/club/documentupdate.jsp?type=account&id_doc="+this.idDoc+"&id_doc_bank_account="+rset.getString("ID_DOC_BANK_ACCOUNT");
	            	String myDeleteLink = "../crm/club/documentupdate.jsp?type=account&id_doc="+this.idDoc+"&id_doc_bank_account="+rset.getString("ID_DOC_BANK_ACCOUNT")+"&action=remove&process=yes";
	                html.append(getDeleteButtonHTML(myDeleteLink, documentXML.getfieldTransl("h_delete_doc_bank_account", false), rset.getString("NUMBER_BANK_ACCOUNT")));
	            	html.append(getEditButtonWitDivHTML(myHyperLink, "div_data_detail"));
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
    } // getJurPersonAccountsHTML

    public String getDocFilesHTML(String pFindString, String p_beg, String p_end) {
        StringBuilder html = new StringBuilder();
        Connection con = null;
        PreparedStatement st = null;
        boolean hasEditPermission = false;

        ArrayList<bcFeautureParam> pParam = initParamArray();

        String mySQL = 
        	" SELECT rn, file_caption, file_version, file_name, creation_date_frmt file_load_date_frmt, id_doc_file, stored_file_name "+
            "   FROM (SELECT ROWNUM rn, file_caption, file_version, file_name, stored_file_name, creation_date_frmt, id_doc_file "+
            "           FROM (SELECT * " +
            "                   FROM " + getGeneralDBScheme() + ".vc_doc_file_all "+
            "                  WHERE id_doc = ? ";
        pParam.add(new bcFeautureParam("int", this.idDoc));
        
	    if (!isEmpty(pFindString)) {
	    		mySQL = mySQL + 
	   				" AND (TO_CHAR(file_caption) LIKE UPPER('%'||?||'%') OR " +
	   				"      UPPER(file_version) LIKE UPPER('%'||?||'%') OR " +
	   				"      UPPER(file_name) LIKE UPPER('%'||?||'%'))";
	    		for (int i=0; i<3; i++) {
	    		    pParam.add(new bcFeautureParam("string", pFindString));
	    		}
	    }
        
	    pParam.add(new bcFeautureParam("int", p_end));
	    pParam.add(new bcFeautureParam("int", p_beg));
        mySQL = mySQL +
            "                  ORDER BY file_caption)" +
            "          WHERE ROWNUM < ?" + 
            " ) WHERE rn >= ?";
        try{
        	if (isEditPermited("CLUB_DOCUMENTS_FILES")>0) {
        		hasEditPermission = true;
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
            html.append("<th>&nbsp;</th>\n");
            if (hasEditPermission) {
                html.append("<th>&nbsp;</th><th>&nbsp;</th>\n");
            }
            html.append("</tr></thead><tbody>\n");
            while (rset.next())
            {
            	html.append("<tr>");
          	  	for (int i=1; i <= colCount-2; i++) {
          	  		html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", "", ""));
          	  	}
          	    String myOpenDocLink = "<td style=\"width:20px;text-align:center;\">&nbsp;</td>";
          	  	if (!isEmpty(rset.getString("STORED_FILE_NAME"))) {
          	  		myOpenDocLink = "<td style=\"width:20px;text-align:center;\"><a href=\"../FileSender?FILENAME=" + URLEncoder.encode(rset.getString("STORED_FILE_NAME"),"UTF-8")+ "\" title=\"" + buttonXML.getfieldTransl("open", false) + "\" target=\"_blank\"><img vspace=\"0\" hspace=\"0\" src=\"../images/oper/small/open.gif\" align=\"top\" width=\"20\" height=\"16\" style=\"border: 0px;\"></a></td>";
          	  	}
            	html.append(myOpenDocLink);
                if (hasEditPermission) {
                	String myHyperLink = "../crm/club/documentfiles.jsp?type=general&id=" + this.idDoc + "&id_doc_file="+rset.getString("ID_DOC_FILE");
                	String myDeleteLink = "../crm/club/documentfiles.jsp?type=general&id="+this.idDoc+"&id_doc_file="+rset.getString("ID_DOC_FILE")+"&action=remove&process=yes";
	                html.append(getDeleteButtonHTML(myDeleteLink, documentXML.getfieldTransl("h_delete_file", false), rset.getString("FILE_NAME")));
                	html.append(getEditButtonWitDivHTML(myHyperLink, "div_data_detail"));
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
    } // getJurPersonAccountsHTML
    
    public String getAllBankAccountsHTML(String pFindString, String pType, String p_beg, String p_end) {
        StringBuilder html = new StringBuilder();
        Connection con = null;
        PreparedStatement st = null;

        ArrayList<bcFeautureParam> pParam = initParamArray();

        String mySQL = 
        	" SELECT rn, number_bank_account, name_bank||' ('||name_bank_account_type||')' name_bank, name_currency, " +
            "        sname_owner_bank_account, id_owner_bank_account, " +
            "        type_owner_bank_account, id_bank_account, id_bank, " +
            "        cd_currency, id_doc_bank_account, cd_bank_account_type, cd_doc_bank_account_type "+
            "   FROM (SELECT ROWNUM rn, id_bank_account, number_bank_account, " +
            "                DECODE(cd_bank_account_type, " +
            "         	 	        'SETTLEMENT_ACCOUNT', '<font color=\"green\"><b>'||name_bank_account_type||'</b></font>', " +
            "          	 	        'CORRESPONDENT_ACCOUNT', '<font color=\"blue\"><b>'||name_bank_account_type||'</b></font>', " +
            "          		        name_bank_account_type" +
            "  		         ) name_bank_account_type, sname_bank name_bank,  " +
            "                cd_currency, name_currency, " +
            "                id_owner_bank_account, sname_owner_bank_account, " +
            "                type_owner_bank_account, " +
            "                id_bank, cd_bank_account_type, id_doc_bank_account, cd_doc_bank_account_type "+
            "           FROM (SELECT a.id_bank_account, a.number_bank_account, a.cd_bank_account_type, " +
            "                        a.name_bank_account_type, a.sname_bank, a.cd_currency, a.name_currency, " +
            "                        a.id_owner_bank_account, a.sname_owner_bank_account, " +
            "                        a.type_owner_bank_account, a.id_bank, b.id_doc_bank_account, b.cd_doc_bank_account_type " +
            "                   FROM (SELECT * " +
            "                           FROM " + getGeneralDBScheme() + ".vc_bank_account_all a "+
                    "                  WHERE id_jur_prs = ? ";
        pParam.add(new bcFeautureParam("int", this.getValue("ID_JUR_PRS_PARTY1")));
        if (!isEmpty(this.getValue("ID_JUR_PRS_PARTY2"))) {
        	mySQL = mySQL + " OR id_jur_prs = ? ";
            pParam.add(new bcFeautureParam("int", this.getValue("ID_JUR_PRS_PARTY2")));
        }
        if (!isEmpty(this.getValue("ID_JUR_PRS_PARTY3"))) {
        	mySQL = mySQL + " OR id_jur_prs = ? ";
            pParam.add(new bcFeautureParam("int", this.getValue("ID_JUR_PRS_PARTY3")));
        }
        
	    if (!isEmpty(pFindString)) {
	    		mySQL = mySQL + 
	   				" AND (TO_CHAR(id_bank_account) LIKE UPPER('%'||?||'%') OR " +
	   				"      UPPER(number_bank_account) LIKE UPPER('%'||?||'%') OR " +
	   				"      UPPER(desc_bank_account) LIKE UPPER('%'||?||'%') OR " +
	   				"      UPPER(sname_bank) LIKE UPPER('%'||?||'%'))";
	    		for (int i=0; i<4; i++) {
	    		    pParam.add(new bcFeautureParam("string", pFindString));
	    		}
	    }
	    if (!isEmpty(pType)) {
        	mySQL = mySQL + " AND cd_bank_account_type = ? ";
        	pParam.add(new bcFeautureParam("string", pType));
        }

	    pParam.add(new bcFeautureParam("int", this.idDoc));
	    pParam.add(new bcFeautureParam("int", p_end));
	    pParam.add(new bcFeautureParam("int", p_beg));
        mySQL = mySQL +
            "                  ORDER BY number_bank_account) a LEFT JOIN " +
            "          (SELECT * " +
            "             FROM " + getGeneralDBScheme() + ".v_doc_bank_account_all" +
            "            WHERE id_doc = ?) b" +
            "       ON (a.id_bank_account = b.id_bank_account)" +
            "   )       WHERE ROWNUM < ?" + 
            " ) WHERE rn >= ?";
        

        //String myFont = "";
        //String myFontEnd = "";
        String myBGColor = "";
        boolean hasBankAccountPermission = false;
        boolean hasCurrencyPermission = false;
        boolean hasPartnerPermission = false;
        boolean hasNatPrsPermission = false;
        boolean hasEditPermission = false;
        
        Statement st_type = null;
		ArrayList<String> cd_type=new ArrayList<String>();
		ArrayList<String> name_type=new ArrayList<String>();
		ArrayList<String> style_type=new ArrayList<String>();
		
        String mySQLType = 
        	" SELECT cd_bank_account_type, name_bank_account_type," +
			"        DECODE(cd_bank_account_type, " +
			"               'SETTLEMENT_ACCOUNT', 'style=\"color:green;font-weight:bold;\"', " +
			"               'CORRESPONDENT_ACCOUNT', 'style=\"color:blue;font-weight:bold;\"', " +
			"               ''" +
			"  		) option_style " +
			"   FROM " + getGeneralDBScheme()+".vc_bank_account_type " +
			"  ORDER BY name_bank_account_type";
        
        try{
        	LOGGER.debug(mySQLType);
       	 	con = Connector.getConnection(getSessionId());
       	 	st_type = con.createStatement();
        	ResultSet rset_type = st_type.executeQuery(mySQLType);
        	while (rset_type.next()) {
        		cd_type.add(rset_type.getString("CD_BANK_ACCOUNT_TYPE"));
        		name_type.add(rset_type.getString("NAME_BANK_ACCOUNT_TYPE"));
        		style_type.add(rset_type.getString("OPTION_STYLE"));
        	}
        } // try
        catch (SQLException e) {LOGGER.error(html, e); html.append(showCRMException(e));}
        catch (Exception el) {LOGGER.error(html, el); html.append(showCRMException(el));}
        finally {
            try {
                if (st_type!=null) st_type.close();
            } catch (SQLException w) {w.toString();}
            try {
                if (con!=null) con.close();
            } catch (SQLException w) {w.toString();}
            Connector.closeConnection(con);
        } // finally
        
        try{
        	if (isEditMenuPermited("CLIENTS_BANK_ACCOUNTS")>=0) {
            	hasBankAccountPermission = true;
            }
        	if (isEditMenuPermited("SETUP_CURRENCY")>=0) {
            	hasCurrencyPermission = true;
            }
        	if (isEditMenuPermited("CLIENTS_YURPERSONS")>=0) {
        		hasPartnerPermission = true;
            }
        	if (isEditMenuPermited("CLIENTS_NATPERSONS")>=0) {
        		hasEditPermission = true;
            }
        	if (isEditPermited("CLUB_DOCUMENTS_BANK_ACCOUNTS")>0) {
        		hasEditPermission = true;
        	}
        	
        	LOGGER.debug(prepareSQLToLog(mySQL, pParam));
        	con = Connector.getConnection(getSessionId());
        	st = con.prepareStatement(mySQL);
        	st = prepareParam(st, pParam);
        	ResultSet rset = st.executeQuery();
            ResultSetMetaData mtd = rset.getMetaData();
            int colCount = mtd.getColumnCount();

       	 	html.append("<form method=\"post\" name=\"updateForm\" id=\"updateForm\" action=\"../crm/club/documentupdate.jsp\">\n");
       	 	html.append("<input type=\"hidden\" name=\"type\" value=\"account\">\n");
    	 	html.append("<input type=\"hidden\" name=\"action\" value=\"addall\">\n");
       	 	html.append("<input type=\"hidden\" name=\"process\" value=\"yes\">\n");
       	 	html.append("<input type=\"hidden\" name=\"id_doc\" value=\"" + this.idDoc + "\">\n");
            html.append(getBottomFrameTable());
            html.append("<tr>");
            for (int i=1; i <= colCount-8; i++) {
                html.append(getBottomFrameTableTH(accountXML, mtd.getColumnName(i)));
            }
            //html.append("<th><input id=\"mainCheck\" name=\"mainCheck\" type=\"checkbox\" onclick=\"CheckAll(this)\"></th>\n");
            html.append(getBottomFrameTableTH(accountXML, "NAME_DOC_BANK_ACCOUNT_TYPE"));
            html.append("</tr></thead><tbody>\n");
            if (hasEditPermission) {
             	html.append("<tr>");
             	html.append("<td colspan=\"8\" align=\"center\">");
             	html.append(getSubmitButtonAjax("../crm/club/documentupdate.jsp"));
             	html.append(getGoBackButton("../crm/club/documentspecs.jsp?id="+this.idDoc));
             	html.append("</td>");
             	html.append("</tr>");
             }
            while (rset.next())
            {
                String id = "id_" + rset.getString("ID_BANK_ACCOUNT");
                String typeValue = "tp_"+rset.getString("ID_BANK_ACCOUNT");
                //String tprvCheck="prv_"+id;
                //String tCheck="chb_"+id;
                //String tStatus="sts_"+id;

                myBGColor = isEmpty(rset.getString("ID_DOC_BANK_ACCOUNT"))?"":selectedBackGroundStyle;
            	html.append("<tr>");
          	  	for (int i=1; i <= colCount-8; i++) {
          	  		if ("NAME_BANK".equalsIgnoreCase(mtd.getColumnName(i))) {
          	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/clients/yurpersonspecs.jsp?id="+rset.getString("ID_BANK"), "", myBGColor));
          	  		} else if (hasBankAccountPermission && 
          	  				("NUMBER_BANK_ACCOUNT".equalsIgnoreCase(mtd.getColumnName(i)))) {
          	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/clients/accountspecs.jsp?id="+rset.getString("ID_BANK_ACCOUNT"), "", myBGColor));
	          	  	} else if ("SNAME_OWNER_BANK_ACCOUNT".equalsIgnoreCase(mtd.getColumnName(i))) {
	          	  		if ("JUR_PRS".equalsIgnoreCase(rset.getString("TYPE_OWNER_BANK_ACCOUNT")) && hasPartnerPermission) {
	          	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/clients/yurpersonspecs.jsp?id="+rset.getString("ID_OWNER_BANK_ACCOUNT"), "", myBGColor));
	          	  		} else if ("NAT_PRS".equalsIgnoreCase(rset.getString("TYPE_OWNER_BANK_ACCOUNT")) && hasNatPrsPermission) {
	          	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/clients/natpersonspecs.jsp?id="+rset.getString("ID_OWNER_BANK_ACCOUNT"), "", myBGColor));
	          	  		} else {
	          	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", "", myBGColor));
	          	  		}
	          	  	} else if (hasCurrencyPermission && 
          	  				("NAME_CURRENCY".equalsIgnoreCase(mtd.getColumnName(i)))) {
          	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/setup/currencyspecs.jsp?id="+rset.getString("CD_CURRENCY"), "", myBGColor));
          	  		} else {
          	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", "", myBGColor));
          	  		}
          	  	}
          	    /*if (!isEmpty(rset.getString("ID_DOC_BANK_ACCOUNT"))) {
          	    	html.append("<td " + myBGColor + " align=\"center\"><INPUT  type=\"checkbox\" value = \"Y\" name="+tCheck+" id="+tCheck+" checked onclick=\"return CheckCB(this);\">\n");
                  	html.append("<INPUT type=hidden  value=\"Y\" name="+tprvCheck+"></td>");
          	    } else {
          	    	html.append("<td " + myBGColor + " align=\"center\"><INPUT  type=\"checkbox\" value = \"Y\" name="+tCheck+" id="+tCheck+" onclick=\"return CheckCB(this);\"></td>\n");
          	    }*/
          	  	if (hasEditPermission) {
	          	  	String selectOption = "<option value=\"\"></option>";
	                String selectStyle = "";
	                String memberSelect = "<INPUT type=hidden value=\"Y\" name=\""+id+"\">";
	                if (cd_type.size()>0) {
                		for(int counter=0;counter<cd_type.size();counter++){
                			if (cd_type.get(counter).equalsIgnoreCase(rset.getString("CD_DOC_BANK_ACCOUNT_TYPE"))) {
                				selectOption = selectOption + "<option value=\"" + cd_type.get(counter) + "\" SELECTED " + style_type.get(counter) + ">" + name_type.get(counter) + "</option>";
                				selectStyle = style_type.get(counter);
                			} else {
                				selectOption = selectOption + "<option value=\"" + cd_type.get(counter) + "\" " + style_type.get(counter) + ">" + name_type.get(counter) + "</option>";
                			}
                		}
                	} else {
                		
                	}
                	memberSelect = memberSelect + "<select name=\"" + typeValue + "\" id=\"" + typeValue + "\" class=\"inputfield\" " + selectStyle + ">";
                	memberSelect = memberSelect + selectOption;
                    memberSelect = memberSelect + "</select>";
                	html.append(getBottomFrameTableTD("NAME_CLUB_MEMBER_STATUS", memberSelect, "", "", myBGColor));
          	  	} else {
          	  		String typeName = "";
	                if (cd_type.size()>0) {
                		for(int counter=0;counter<cd_type.size();counter++){
                			if (cd_type.get(counter).equalsIgnoreCase(rset.getString("CD_DOC_BANK_ACCOUNT_TYPE"))) {
                				typeName = name_type.get(counter);
                			}
                		}
                	}
	                html.append("<td " + myBGColor + " align=\"center\">" + typeName + "</td>\n");
          	  	}
                html.append("</tr>\n");
            }
            if (hasEditPermission) {
             	html.append("<tr>");
             	html.append("<td colspan=\"8\" align=\"center\">");
             	html.append(getSubmitButtonAjax("../crm/club/documentupdate.jsp"));
             	html.append(getGoBackButton("../crm/club/documentspecs.jsp?id="+this.idDoc));
             	html.append("</td>");
             	html.append("</tr>");
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
    } // getJurPersonAccountsHTML
    

}
