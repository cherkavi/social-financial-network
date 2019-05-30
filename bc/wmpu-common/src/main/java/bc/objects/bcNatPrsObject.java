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
import bc.lists.bcListBankAccount;
import bc.service.bcFeautureParam;

public class bcNatPrsObject extends bcObject {
	private final static Logger LOGGER=Logger.getLogger(bcNatPrsObject.class);
	
	private Map<String,Comparable<String>> fieldHm = new HashMap<String, Comparable<String>>();
	
	private String idNatPrs;
	
	public bcNatPrsObject(String pIdNatPrs) {
		this.idNatPrs = pIdNatPrs;
		getFeature();
	}
	
	private void getFeature() {
		String featureSelect = " SELECT * FROM " + getGeneralDBScheme() + ".VC_NAT_PRS_PRIV_ALL WHERE id_nat_prs = ?";
		fieldHm = getFeatures2(featureSelect, this.idNatPrs, false);
	}
	
	public String getValue(String pColumnName) {
		return getFeautureResult(fieldHm, pColumnName);
	}
	
    public String getNatPersonCardsHTML(String pFindString, String pClubMemberStatus, String pRoleState, String p_beg, String p_end) {
    	ArrayList<bcFeautureParam> pParam = initParamArray();

    	String mySQL = 
    		"SELECT rn, sname_club club, name_club_member_status, " +
    		"       cd_card1, date_card_sale_frmt, total_amount_card_sale_frmt,  " +
            "       membership_last_date_frmt, name_nat_prs_role_state, sname_service_place_work, " +
            "       name_post, " +
            "       card_serial_number, card_id_issuer, " +
            "       card_id_payment_system, id_service_place_work, id_club, id_nat_prs_role " +
    		"  FROM (SELECT ROWNUM rn, cd_card1, date_card_sale_frmt, " +
    		"				CASE WHEN cd_card1 IS NULL THEN '' ELSE total_amount_card_sale_frmt END total_amount_card_sale_frmt,  " +
            " 		        membership_last_date_frmt, " +
            "               DECODE(cd_nat_prs_role_state, " +
  		  	"                     'GIVEN', '<b><font color=\"black\">'||name_nat_prs_role_state||'</font></b>', " +
			"                     'QUESTIONED', '<font color=\"blue\">'||name_nat_prs_role_state||'</font>', " +
			"                     'ACTIVATED', '<font color=\"green\">'||name_nat_prs_role_state||'</font>', " +
			"                     'WITHOUT_CARD', '<font color=\"gray\">'||name_nat_prs_role_state||'</font>', " +
			"               	  name_nat_prs_role_state" +
			"				) name_nat_prs_role_state, sname_service_place_work, " +
            " 		        name_post, sname_club, " +
            "               DECODE(cd_club_member_status, " +
       		"                      'CLOSED', '<font color=\"red\"><b>'||name_club_member_status||'</b></font>', " +
       		"                      'OUT_OF_CLUB', '<font color=\"gray\">'||name_club_member_status||'</font>', " +
       		"                      'IN_PROCESS', '<font color=\"blue\">'||name_club_member_status||'</font>', " +
       		"                      'PARTICIPATE', '<font color=\"green\">'||name_club_member_status||'</font>', " +
       		"                      name_club_member_status" +
       		"               ) name_club_member_status, " +
            " 		        club_date_beg_frmt, club_date_end_frmt, card_serial_number, card_id_issuer, " +
            "               card_id_payment_system, id_service_place_work, id_club, id_nat_prs_role " +
    		"          FROM (SELECT cd_card1, date_card_sale_frmt, total_amount_card_sale_frmt,  " +
            " 		                membership_last_date_frmt, cd_nat_prs_role_state, name_nat_prs_role_state, sname_service_place_work, " +
            " 		                name_post, sname_club, cd_club_member_status, name_club_member_status, " +
            " 		                club_date_beg_frmt, club_date_end_frmt, card_serial_number, card_id_issuer, " +
            "                       card_id_payment_system, id_service_place_work, id_club, id_nat_prs_role " +
    		"                  FROM " + getGeneralDBScheme() + ".vc_nat_prs_role_club_all " +
    		"                 WHERE id_nat_prs = ? ";
    	pParam.add(new bcFeautureParam("int", this.idNatPrs));
    	
    	if (!isEmpty(pFindString)) {
    		mySQL = mySQL + 
   				" AND (TO_CHAR(cd_card1) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(card_serial_number) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(card_serial_number) LIKE UPPER('%'||?||'%'))";
    		for (int i=0; i<3; i++) {
    		    pParam.add(new bcFeautureParam("string", pFindString));
    		}
    	}
    	if (!isEmpty(pClubMemberStatus)) {
    		mySQL = mySQL + " AND cd_club_member_status = ?";
    		pParam.add(new bcFeautureParam("string", pClubMemberStatus));
    	}
    	if (!isEmpty(pRoleState)) {
    		mySQL = mySQL + " AND cd_nat_prs_role_state = ?";
    		pParam.add(new bcFeautureParam("string", pRoleState));
    	}
    	pParam.add(new bcFeautureParam("int", p_end));
    	pParam.add(new bcFeautureParam("int", p_beg));
    	mySQL = mySQL + 
    		"                 ORDER BY club_date_beg desc) "+
    		"         WHERE ROWNUM < ? " + 
    		" ) WHERE rn >= ?";
        StringBuilder html = new StringBuilder();
        Connection con = null;
        PreparedStatement st = null; 
        boolean hasCardPermission = false;
        boolean hasClubPermission = false;
        boolean hasPartnerPermission = false;
        boolean hasContactPrsPermission = false;
        boolean hasEditPermission = false;
        try{
        	if (isEditMenuPermited("CARDS_CLUBCARDS")>=0) {
        		hasCardPermission = true;
        	}
        	if (isEditMenuPermited("CLUB_CLUB")>=0) {
        		hasClubPermission = true;
        	}
        	if (isEditMenuPermited("CLIENTS_YURPERSONS")>=0) {
        		hasPartnerPermission = true;
        	}
        	if (isEditMenuPermited("CLIENTS_CONTACT_PRS")>=0) {
        		hasContactPrsPermission = true;
        	}
        	if (isEditPermited("CLIENTS_NATPERSONS_CARDS")>0) {
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
            html.append("<th>&nbsp;</th>");
            html.append("<th colspan=\"2\">" + natprsXML.getfieldTransl("title_nat_prs_club", false) + "</th>");
            html.append("<th colspan=\"5\">" + natprsXML.getfieldTransl("title_nat_prs_cards", false) + "</th>");
            html.append("<th colspan=\"2\">" + natprsXML.getfieldTransl("title_nat_prs_contact_prs", false) + "</th>");
            if (hasEditPermission) {
            	html.append("<th rowspan=\"2\">&nbsp;</th>");
            }
            html.append("</tr>");
            html.append("<tr>");
            for (int i=1; i <= colCount-6; i++) {
            	html.append(getBottomFrameTableTH(clubcardXML, mtd.getColumnName(i)));
            }
            html.append("</tr></thead><tbody>");
            while (rset.next())
            {
            	html.append("<tr>");
          	  	for (int i=1; i <= colCount-6; i++) {
          	  		if (hasCardPermission && "CD_CARD1".equalsIgnoreCase(mtd.getColumnName(i))) {
          	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/cards/clubcardspecs.jsp?id="+rset.getString("CARD_SERIAL_NUMBER")+"&iss="+rset.getString("CARD_ID_ISSUER")+"&paysys="+rset.getString("CARD_ID_PAYMENT_SYSTEM"), "", ""));
         	  		} else if (hasClubPermission && "CLUB".equalsIgnoreCase(mtd.getColumnName(i))) {
          	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/club/clubspecs.jsp?id="+rset.getString("ID_CLUB"), "", ""));
         	  		} else if (hasPartnerPermission && "SNAME_SERVICE_PLACE_WORK".equalsIgnoreCase(mtd.getColumnName(i))) {
          	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/clients/yurpersonspecs.jsp?id="+rset.getString("ID_SERVICE_PLACE_WORK"), "", ""));
         	  		} else if (hasContactPrsPermission && "NAME_POST".equalsIgnoreCase(mtd.getColumnName(i))) {
          	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/clients/contact_prsspecs.jsp?id="+rset.getString("ID_NAT_PRS_ROLE"), "", ""));
         	  		} else {
          	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", "", ""));
          	  		}
          	  	}
                if (hasEditPermission) {
                	String myHyperLink = "../crm/clients/natpersonupdate.jsp?type=role&id="+this.idNatPrs+"&id_nat_prs_role="+rset.getString("ID_NAT_PRS_ROLE");
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
    }

    public String getNatPersonMessagesHTML(String pFindString, String pTypeMessage, String p_beg, String p_end) {
    	ArrayList<bcFeautureParam> pParam = initParamArray();

    	String mySQL = 
    		"SELECT rn, id_message, type_message, title_message, " + 
    		"		sendings_quantity, error_sendings_quantity, event_date_frmt, " +
    		"       state_record_tsl, is_archive_tsl, type_message2, " + 
    		"		id_nat_prs " +
    		"  FROM (SELECT ROWNUM rn, id_message, type_message, title_message, " + 
    		"				sendings_quantity, error_sendings_quantity, event_date_frmt, " +
    		"               state_record_tsl, is_archive_tsl, type_message2, " + 
    		"				id_nat_prs " +
    		"          FROM (SELECT id_ds_message id_message, name_ds_message_type type_message, " +
    		"                       title_ds_message title_message, " + 
    		"						sendings_quantity, error_sendings_quantity, event_date_frmt, " +
    		"               		DECODE(cd_ds_message_state, " +
  		  	"                      			'PREPARED', '<b><font color=\"black\">'||name_ds_message_state||'</font></b>', " +
			"                      			'NEW', '<font color=\"black\">'||name_ds_message_state||'</font>', " +
			"                      			'EXECUTED', '<font color=\"green\">'||name_ds_message_state||'</font>', " +
			"                      			'EXECUTED_PARTIALLY', '<font color=\"darkgreen\">'||name_ds_message_state||'</font>', " +
			"                      			'ERROR', '<b><font color=\"red\">'||name_ds_message_state||'</font></b>', " +
			"                      			'CARRIED_OUT_PARTIALLY', '<b><font color=\"darkgray\">'||name_ds_message_state||'</font></b>', " +
			"                      			'CARRIED_OUT', '<font color=\"gray\">'||name_ds_message_state||'</font>', " +
			"                      			'CANCELED', '<font color=\"blue\">'||name_ds_message_state||'</font>', " +
			"                      			name_ds_message_state) state_record_tsl, " +
			"  				       	DECODE(is_archive, " +
			"                             	'Y', '<b><font color=\"red\">'||is_archive_tsl||'</font></b>', " +
			"                             	is_archive_tsl) is_archive_tsl, " +
  		  	"                       cd_ds_message_type type_message2, id_nat_prs " +
    		"                  FROM " + getGeneralDBScheme() + ".vc_nat_prs_messages_all " +
    		"                 WHERE id_nat_prs = ? ";
    	pParam.add(new bcFeautureParam("int", this.idNatPrs));
    	
    	if (!isEmpty(pFindString)) {
    		mySQL = mySQL + 
   				" AND (TO_CHAR(id_message) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(title_message) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(event_date_frmt) LIKE UPPER('%'||?||'%'))";
    		for (int i=0; i<4; i++) {
    		    pParam.add(new bcFeautureParam("string", pFindString));
    		}
    	}
    	if (!isEmpty(pTypeMessage)) {
    		mySQL = mySQL + " AND type_message = ? ";
    		pParam.add(new bcFeautureParam("string", pTypeMessage));
    	}
    	pParam.add(new bcFeautureParam("int", p_end));
    	pParam.add(new bcFeautureParam("int", p_beg));
    	mySQL = mySQL + 
    		"                 ORDER BY event_date desc, id_message) "+
    		"         WHERE ROWNUM < ? " + 
    		" ) WHERE rn >= ?";
        
        StringBuilder html = new StringBuilder();
        Connection con = null;
        PreparedStatement st = null; 
        
        boolean hasSMSPermission = false;
        boolean hasEmailPermission = false;
        boolean hasOfficePermission = false;
        //
        try{
        	if (isEditMenuPermited("DISPATCH_MESSAGES_SMS")>=0) {
        		hasSMSPermission = true;
        	}
        	if (isEditMenuPermited("DISPATCH_MESSAGES_EMAIL")>=0) {
        		hasEmailPermission = true;
        	}
        	if (isEditMenuPermited("DISPATCH_MESSAGES_OFFICE")>=0) {
        		hasOfficePermission = true;
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
            	html.append(getBottomFrameTableTH(messageXML, mtd.getColumnName(i)));
            }
            html.append("</tr></thead><tbody>");
            while (rset.next())
            {
       		 	html.append("<tr>\n");
            	for (int i=1; i<=colCount-2; i++) {
            		if ("ID_MESSAGE".equalsIgnoreCase(mtd.getColumnName(i))) {
            			if (hasSMSPermission && "SMS".equalsIgnoreCase(rset.getString("TYPE_MESSAGE2"))) {
            				html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/dispatch/messages/smsspecs.jsp?id="+rset.getString(i), "", ""));
                		} else if (hasEmailPermission && "EMAIL".equalsIgnoreCase(rset.getString("TYPE_MESSAGE2"))) {
                			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/dispatch/messages/email_messagesspecs.jsp?id="+rset.getString(i), "", ""));
                		} else if (hasOfficePermission && "OFFICE".equalsIgnoreCase(rset.getString("TYPE_MESSAGE2"))) {
                			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/dispatch/messages/office_messagesspecs.jsp?id="+rset.getString(i), "", ""));
                		} else {
                			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", "", ""));
                		}
            		} else if ("TEXT_MESSAGE".equalsIgnoreCase(mtd.getColumnName(i))) {
            			html.append(getBottomFrameTableTD(mtd.getColumnName(i), getHTMLValue(rset.getString(i)), "", "", ""));
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
    }//getNatPersonMessagesHTML

    public String getBankAccountsHTML(String pFindString, String pBankAccountType, String p_beg, String p_end) {
    	bcListBankAccount list = new bcListBankAccount();
    	
    	String pWhereCause = " WHERE id_nat_prs = ? ";
    	
    	ArrayList<bcFeautureParam> pWhereValue = new ArrayList<bcFeautureParam>();
    	pWhereValue.add(new bcFeautureParam("int", this.idNatPrs));
    	String myDeleteLink = "";
    	String myEditLink = "";
    	String myCopyLink = "";
    	if (isEditPermited("CLIENTS_NATPERSONS_BANK_ACCOUNTS")>0) {
    		myDeleteLink = "../crm/clients/accountsupdate.jsp?back_type=NAT_PRS&type=general&id="+this.idNatPrs+"&action=remove&process=yes";
    	    myEditLink = "../crm/clients/accountsupdate.jsp?back_type=NAT_PRS&type=general&id="+this.idNatPrs;
    	}
    	
    	return list.getCardPackagesHTML(pWhereCause, pWhereValue, "OWNER", pFindString, pBankAccountType, myEditLink, myCopyLink, myDeleteLink, p_beg, p_end);
    	
    }
	
    public String getDocumentsHTML(String pFindString, String pType, String p_beg, String p_end) {
        StringBuilder html = new StringBuilder();
        PreparedStatement st = null;
        Connection con = null;
        String src_doc = "";
        boolean hasEditPermission = false;

        ArrayList<bcFeautureParam> pParam = initParamArray();

        String mySQL = 
        	" SELECT rn, name_doc_type, number_nat_prs_doc number_doc, " +
        	"       date_nat_prs_doc_frmt date_doc, name_nat_prs_doc name_doc, file_nat_prs_doc, id_doc "+
            "   FROM (SELECT ROWNUM rn, id_doc, name_doc_type, number_nat_prs_doc, date_nat_prs_doc_frmt, name_nat_prs_doc, file_nat_prs_doc "+
            "           FROM (SELECT id_nat_prs_doc id_doc, name_nat_prs_doc_type name_doc_type, " +
            "                        number_nat_prs_doc, date_nat_prs_doc_frmt, name_nat_prs_doc, file_nat_prs_doc " +
            "                   FROM " + getGeneralDBScheme() + ".vc_nat_prs_doc_all "+
            "                  WHERE id_nat_prs = ? ";
        pParam.add(new bcFeautureParam("int", this.idNatPrs));
        
    	if (!isEmpty(pFindString)) {
    		mySQL = mySQL + 
   				" AND (TO_CHAR(id_nat_prs_doc) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(full_nat_prs_doc) LIKE UPPER('%'||?||'%'))";
    		for (int i=0; i<2; i++) {
    		    pParam.add(new bcFeautureParam("string", pFindString));
    		}
    	}
        if (!isEmpty(pType)) {
        	mySQL = mySQL + " AND cd_nat_prs_doc_type = ? ";
        	pParam.add(new bcFeautureParam("string", pType));
        }
        pParam.add(new bcFeautureParam("int", p_end));
        pParam.add(new bcFeautureParam("int", p_beg));
        mySQL = mySQL +
            "                  ORDER BY full_nat_prs_doc) " +
            "          WHERE ROWNUM < ? " + 
            " ) WHERE rn >= ?";
        try{
        	if (isEditPermited("CLIENTS_NATPERSONS_DOC")>0) {
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
        	//html.append(getBottomFrameTableTH(documentXML, "RN"));
            for (int i=1; i <= colCount-2; i++) {
            	html.append(getBottomFrameTableTH(documentXML, mtd.getColumnName(i)));
            }
            html.append("<th style=\"width:15px;text-align:center;\">&nbsp;</th>\n");
            if (hasEditPermission) {
                html.append("<th>&nbsp;</th><th>&nbsp;</th>\n");
            }
            html.append("</tr></thead><tbody>\n");
            while (rset.next())
            {
            	if (!isEmpty(rset.getString("FILE_NAT_PRS_DOC"))) {
            		src_doc = "<a href=\"../FileSender?FILENAME=" + URLEncoder.encode(rset.getString("FILE_NAT_PRS_DOC"),"UTF-8") + "\" title=\"" + buttonXML.getfieldTransl("open", false) + "\" target=_blank> " +
            				  "<img vspace=\"0\" hspace=\"0\" src=\"../images/oper/small/open.gif\" align=\"top\" style=\"border: 0px;\">" +
            				  "</a>";
            	} else {
            		src_doc = "";
            	}
            	html.append("<tr>");
  	  			//html.append(getBottomFrameTableTD("RN", rset.getString("RN"), "", "", ""));
          	  	for (int i=1; i <= colCount-2; i++) {
	          	  	if ("NUMBER".equalsIgnoreCase(mtd.getColumnName(i))) {
	      	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), "../crm/club/documentspecs.jsp?id="+rset.getString("ID_DOC"), "", "", ""));
	     	  		} else {
	      	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", "", ""));
	      	  		}
           	  	}
            	html.append(getBottomFrameTableTD("", src_doc, "", "", ""));
                if (hasEditPermission) {
                	String myHyperLink = "../crm/clients/natpersondocupdate.jsp?type=doc&id="+this.idNatPrs+"&id_doc="+rset.getString("ID_DOC");
                	String myDeleteLink = "../crm/clients/natpersondocupdate.jsp?type=doc&id="+this.idNatPrs+"&id_doc="+rset.getString("ID_DOC")+"&action=remove&process=yes";
                	html.append(getDeleteButtonHTML(myDeleteLink, documentXML.getfieldTransl("l_remove_doc", false), rset.getString("ID_DOC")));
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
    }
	
    public String getGiftsHTML(String pFindString, String pState, String p_beg, String p_end) {
    	ArrayList<bcFeautureParam> pParam = initParamArray();

    	String mySQL = 
        	" SELECT rn, id_nat_prs_gift, name_nat_prs_gift_state, " +
	        "        name_gift, cost_gift_frmt, name_club_event, " +
	        "        date_reserve_frmt, date_given_frmt, id_club_event " +
	        "   FROM (SELECT ROWNUM rn, id_nat_prs_gift, name_nat_prs_gift_state, " +
	        "                name_gift, cost_gift_frmt, name_club_event, date_reserve_frmt, " +
	        "                date_given_frmt, id_club_event " +
	        "           FROM (SELECT id_nat_prs_gift, " +
	        "                        DECODE(cd_nat_prs_gift_state, " +
	        "                               'RESERVED', '<font color=\"green\"><b>'||name_nat_prs_gift_state||'<b></font>', " +
	        "                               'GIVEN', '<font color=\"blue\"><b>'||name_nat_prs_gift_state||'<b></font>', " +
	        "                               '<font color=\"red\"><b>'||name_nat_prs_gift_state||'<b></font>' " +
	        "                        ) name_nat_prs_gift_state, " +
	        "                        name_nat_prs_gift name_gift, " +
	        "                        cost_lg_gift_frmt||' '||sname_lg_currency cost_gift_frmt, " +
	        "                        name_club_event, date_reserve_frmt, date_given_frmt, " +
	        "                        id_club_event " +
	        "                   FROM " + getGeneralDBScheme() + ".vc_nat_prs_gifts_club_all " +
	        "  		           WHERE id_nat_prs = ? ";
    	pParam.add(new bcFeautureParam("int", this.idNatPrs));
    	
    	if (!isEmpty(pFindString)) {
    		mySQL = mySQL + 
   				" AND (TO_CHAR(id_nat_prs_gift) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(name_gift) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(cost_gift_frmt||' '||sname_currency) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(name_club_event) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(date_reserve_frmt) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(date_given_frmt) LIKE UPPER('%'||?||'%'))";
    		for (int i=0; i<6; i++) {
    		    pParam.add(new bcFeautureParam("string", pFindString));
    		}
    	}
        if (!isEmpty(pState)) {
        	mySQL = mySQL + " AND cd_nat_prs_gift_state = ? ";
        	pParam.add(new bcFeautureParam("string", pState));
        }
        pParam.add(new bcFeautureParam("int", p_end));
        pParam.add(new bcFeautureParam("int", p_beg));
        mySQL = mySQL +
	        "                  ORDER BY date_reserve DESC) " +
	        "          WHERE ROWNUM < ? " + 
	        " ) WHERE rn >= ?";
        StringBuilder html = new StringBuilder();
        PreparedStatement st = null;
        Connection con = null;
        boolean hasGiftPermission = false;
        boolean hasNatPrsGiftPermission = false;
        boolean hasClubEventPermission = false;
        try{

	    	  if (isEditMenuPermited("CLUB_EVENT_DELIVERY")>=0) {
	    		  hasNatPrsGiftPermission = true;
	    	  }
	    	  if (isEditMenuPermited("CLUB_EVENT_GIFTS")>=0) {
	    		  hasGiftPermission = true;
	    	  }
	    	  if (isEditMenuPermited("CLUB_EVENT_EVENT")>=0) {
	    		  hasClubEventPermission = true;
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
	              String colName = mtd.getColumnName(i);
	              html.append(getBottomFrameTableTH(club_actionXML, colName));
	          }
	          html.append("</tr></thead><tbody>\n");
	          
	          while (rset.next())
	          {
	        	  html.append("<tr>");
	          	  for (int i=1; i <= colCount-2; i++) {
	          		  if ("ID_NAT_PRS_GIFT".equalsIgnoreCase(mtd.getColumnName(i)) && hasNatPrsGiftPermission) {
	          			  html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "/club_event/deliveryspecs.jsp?id="+rset.getString("ID_NAT_PRS_GIFT"), "", ""));
	          		  } else if ("NAME_GIFT".equalsIgnoreCase(mtd.getColumnName(i)) && hasGiftPermission) {
	          			  html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "/club_event/giftspecs.jsp?id="+rset.getString("ID_GIFT"), "", ""));
	          		  } else if ("NAME_CLUB_EVENT".equalsIgnoreCase(mtd.getColumnName(i)) && hasClubEventPermission) {
	          			  html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "/club_event/clubeventspecs.jsp?id="+rset.getString("ID_CLUB_EVENT"), "", ""));
	          		  } else {
	          			  html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", "", ""));
	          	  	  }
	          	  }
	              html.append("</td>\n");
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
	
	public String getGiftsRequestsHTML(String pType, String pState, String pFindString, String p_beg, String p_end) {

		ArrayList<bcFeautureParam> pParam = initParamArray();

		String mySQL = 
	    	    "SELECT rn, id_nat_prs_gift_request, name_nat_prs_gift_request_type, nm_nat_prs_gift_request_state, " +
	    	    "       name_club_event, date_accept_frmt, " +
	          	"       text_request, id_club_event " +
	          	"  FROM (SELECT ROWNUM rn, id_nat_prs_gift_request,  " +
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
	          	"               text_request, name_club_event, date_accept_frmt, id_club_event " +
	          	"		   FROM (SELECT * " +
	          	"                  FROM " + getGeneralDBScheme() + ".vc_nat_prs_gift_request_cl_all" +
	            "                 WHERE id_nat_prs = ? ";
		pParam.add(new bcFeautureParam("int", this.idNatPrs));
		
		if (!isEmpty(pType)) {
            	mySQL = mySQL + " AND cd_nat_prs_gift_request_type = ? ";
            	pParam.add(new bcFeautureParam("string", pType));
		}
		if (!isEmpty(pState)) {
            	mySQL = mySQL + " AND cd_nat_prs_gift_request_state = ? ";
            	pParam.add(new bcFeautureParam("string", pState));
		}
		if (!isEmpty(pFindString)) {
			mySQL = mySQL +
				"  AND (UPPER(TO_CHAR(id_nat_prs_gift_request)) LIKE UPPER('%'||?||'%') OR " +
				"       UPPER(name_club_event) LIKE UPPER('%'||?||'%') OR " +
				"       UPPER(text_request) LIKE UPPER('%'||?||'%') OR " +
				"       UPPER(date_accept_frmt) LIKE UPPER('%'||?||'%')) ";
			for (int i=0; i<4; i++) {
			    pParam.add(new bcFeautureParam("string", pFindString));
			}
		}
		pParam.add(new bcFeautureParam("int", p_end));
		pParam.add(new bcFeautureParam("int", p_beg));
		mySQL = mySQL + 
	          	"                 ORDER BY date_accept DESC) " +
	          	"         WHERE ROWNUM < ? " + 
	        	" ) WHERE rn >= ?";
		StringBuilder html = new StringBuilder();
		PreparedStatement st = null;
		Connection con = null;
		boolean hasRequestPermission = false;
		boolean hasClubEventPermission = false;
		try{
	    	  if (isEditMenuPermited("CLUB_EVENT_REQUEST")>=0) {
	    		  hasRequestPermission = true;
	    	  }
	    	  if (isEditMenuPermited("CLUB_EVENT_EVENT")>=0) {
	    		  hasClubEventPermission = true;
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
	        	  html.append(getBottomFrameTableTH(club_actionXML, mtd.getColumnName(i)));
	          }
	          html.append("</tr></thead><tbody>\n");
	          
	          while (rset.next())
	          {
	        	  html.append("<tr>");
	          	  for (int i=1; i <= colCount-1; i++) {
	          		  if ("ID_NAT_PRS_GIFT_REQUEST".equalsIgnoreCase(mtd.getColumnName(i)) && hasRequestPermission) {
	          			  html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "/club_event/requestspecs.jsp?id="+rset.getString("ID_NAT_PRS_GIFT_REQUEST"), "", ""));
	          		  } else if ("NAME_CLUB_EVENT".equalsIgnoreCase(mtd.getColumnName(i)) && hasClubEventPermission) {
	          			  html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "/club_event/clubeventspecs.jsp?id="+rset.getString("ID_CLUB_EVENT"), "", ""));
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
	  } // getClubActionGiftsHTML

	public String getBKAccountsHTML(String pFindString, String pExistFlag, String p_beg, String p_end) {
		StringBuilder html = new StringBuilder();
		Connection con = null;
		PreparedStatement st = null;
		ArrayList<bcFeautureParam> pParam = initParamArray();

		String mySQL = 
	        	" SELECT id_bk_account, cd_bk_account, name_bk_account, " +
	            "        internal_name_bk_account, " +
	            "        DECODE(exist_flag, " +
				"          		'Y', '<b><font color=\"green\">'||exist_flag_tsl||'</font></b>', " +
				"              	'<b><font color=\"red\">'||exist_flag_tsl||'</font></b>' " +
				"        ) exist_flag_tsl, " +
	            "        exist_flag " +
	            "   FROM (SELECT ROWNUM rn, id_bk_account, cd_bk_account, name_bk_account, " +
	            "                internal_name_bk_account, exist_flag, exist_flag_tsl " +
	            "           FROM (SELECT *" +
	            "                   FROM " + getGeneralDBScheme() + ".vc_bk_accounts_member_priv_all "+
	            "                  WHERE UPPER(cd_participant) = 'CLIENT'" +
	            "                    AND id_member = ? ";
		pParam.add(new bcFeautureParam("string", this.idNatPrs));
		
		if (!isEmpty(pFindString)) {
			mySQL = mySQL + 
					" AND (TO_CHAR(id_bk_account) LIKE UPPER('%'||?||'%') OR " +
					"      UPPER(cd_bk_account) LIKE UPPER('%'||?||'%') OR " +
					"      UPPER(name_bk_account) LIKE UPPER('%'||?||'%') OR " +
					"      UPPER(internal_name_bk_account) LIKE UPPER('%'||?||'%')) ";
			for (int i=0; i<4; i++) {
			    pParam.add(new bcFeautureParam("string", pFindString));
			}
		}
		if (!isEmpty(pExistFlag)) {
			mySQL = mySQL + " AND exist_flag = ? ";
			pParam.add(new bcFeautureParam("string", pExistFlag));
		}
		pParam.add(new bcFeautureParam("int", p_end));
		pParam.add(new bcFeautureParam("int", p_beg));
		mySQL = mySQL + 
	            "                  ORDER BY cd_bk_account) " +
	            "          WHERE ROWNUM < ?)" +
	            " WHERE rn >= ? ";
        	
        	
		boolean hasBKAccountPermission = false; 
	        
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
	            for (int i=1; i <= colCount-1; i++) {
	                html.append(getBottomFrameTableTH(bk_accountXML, mtd.getColumnName(i)));
	            }
	            html.append("</tr></thead><tbody>\n");
	            while (rset.next())
	            {
	          	  	if ("Y".equalsIgnoreCase(rset.getString("EXIST_FLAG"))) {
	                		myFont = "";
	                		myBgColor = noneBackGroundStyle;
	                } else {
	                		myFont = "<font color=\"red\">";
	                		myBgColor = selectedBackGroundStyle;
	                }

	          	  	html.append("<tr>");
	          	  	for (int i=1; i <= colCount-1; i++) {
	          	  		if (hasBKAccountPermission && 
	          	  				("ID_BK_ACCOUNT".equalsIgnoreCase(mtd.getColumnName(i)) ||
	          	  				 "CD_BK_ACCOUNT".equalsIgnoreCase(mtd.getColumnName(i)))) {
	          	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/finance/bk_accountspecs.jsp?id="+rset.getString(i), myFont, myBgColor));
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
	                if (st!=null) st.close();
	            } catch (SQLException w) {w.toString();}
	            try {
	                if (con!=null) con.close();
	            } catch (SQLException w) {w.toString();}
	            Connector.closeConnection(con);
	        } // finally
	        return html.toString();
	} // getJurPersonBKAccountsHTML

}
