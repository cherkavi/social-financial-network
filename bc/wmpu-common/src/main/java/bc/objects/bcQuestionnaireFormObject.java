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


public class bcQuestionnaireFormObject extends bcObject {
	private final static Logger LOGGER=Logger.getLogger(bcQuestionnaireFormObject.class);
	private Map<String,Comparable<String>> fieldHm = new HashMap<String, Comparable<String>>();
	
    private String idQuestForm;
    
    public bcQuestionnaireFormObject(String pIdQuestForm) {
    	this.idQuestForm = pIdQuestForm;
		this.getFeature();
    }
	
	public void getFeature() {
		String featureSelect = " SELECT * FROM " + getGeneralDBScheme() + ".VC_QUEST_FORM_CLUB_ALL WHERE id_quest_form = ?";
		fieldHm = getFeatures2(featureSelect, this.idQuestForm, false);
	}
	
	public String getValue(String pColumnName) {
		return getFeautureResult(fieldHm, pColumnName);
	}
	
    public String getQuestionnaireFormFieldListHTML(String pFindString, String p_beg, String p_end) {
        Connection con = null;
        
        ArrayList<bcFeautureParam> pParam = initParamArray();

        String mySQL = 
        	" SELECT rn, name_quest_field_block, name_quest_field, required_quest_form_field_frmt, " +
        	"        id_quest_form_field, id_quest_form_field_block, type_data," +
        	"        field_cnt, order_in_block_quest_field, block_cnt, order_quest_form_field_block " +
        	"   FROM (SELECT ROWNUM rn, id_quest_form_field, name_quest_field_block, cd_quest_field, name_quest_field, " +
        	"				 required_quest_form_field_frmt, id_quest_form_field_block, type_data," +
        	"                field_cnt, order_in_block_quest_field, block_cnt, order_quest_form_field_block " +
        	"           FROM (SELECT id_quest_form_field, " +
        	"                        '<b>'||name_quest_field_block||'</b>' name_quest_field_block, " +
        	"                        cd_quest_field, name_quest_field, " +
        	"                        DECODE(required_quest_form_field," +
        	"                               'Y','<font color=\"red\"><b>'||required_quest_form_field||'</b></font>'," +
        	"                               required_quest_form_field" +
        	"                        ) required_quest_form_field_frmt, " +
        	"                        id_quest_form_field_block, type_data," +
        	"                        field_cnt, order_in_block_quest_field," +
        	"                        block_cnt, order_quest_form_field_block "+
    		"   				FROM " + getGeneralDBScheme() + ".vc_quest_form_field_all " +
    		"                  WHERE id_quest_form = ? ";
        pParam.add(new bcFeautureParam("int", this.idQuestForm));
        
        if (!isEmpty(pFindString)) {
           	mySQL = mySQL + 
           		" AND (UPPER(cd_quest_field) LIKE UPPER('%'||?||'%') OR " +
           		"      UPPER(name_quest_field) LIKE UPPER('%'||?||'%')) ";
           	for (int i=0; i<3; i++) {
           	    pParam.add(new bcFeautureParam("string", pFindString));
           	}
        }
        pParam.add(new bcFeautureParam("int", p_end));
        pParam.add(new bcFeautureParam("int", p_beg));
    	mySQL = mySQL +
        	"                  ORDER BY order_quest_form_field_block, blc_ord, order_quest_form_field) " +
        	"          WHERE ROWNUM < ?)" +
        	"  WHERE rn >= ?";
        StringBuilder html = new StringBuilder();
        PreparedStatement st = null;
        
        boolean hasEditPremission = false;
        
        try{
        	if (isEditPermited("CARDS_QUESTIONNAIRE_FORM_FIELD")>=0) {
        		hasEditPremission = true;
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
            for (int i=1; i <= colCount-5; i++) {
            	html.append(getBottomFrameTableTH(questionnaireXML, mtd.getColumnName(i)));
            }
            if (hasEditPremission) {
            	 html.append("<th>&nbsp;</th><th>&nbsp;</th><th>&nbsp;</th>\n");
            }
            html.append("</tr></thead><tbody>\n");
            int rowIndex = 0;
            while (rset.next()) {
            	html.append("<tr>\n");
            	rowIndex = rowIndex + 1;
                for (int i=1; i<=colCount-5; i++) {
                	html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", "", ""));
                }
                if (hasEditPremission) {
                	if ("BLOCK".equalsIgnoreCase(rset.getString("TYPE_DATA"))) {
	                	if ("1".equalsIgnoreCase(rset.getString("ORDER_QUEST_FORM_FIELD_BLOCK"))) {
	                   	   	html.append("<td aling=\"center\"><img vspace=\"0\" hspace=\"0\"" +
	                 				" src=\"../images/form/mup1.png\" width=\"16\" height=\"16\" align=\"top\" style=\"border: 0px;\"" + 
	                 				" title=\"" + buttonXML.getfieldTransl("button_up", false)+"\">" + getHyperLinkEnd()+"</td>\n");
	                	} else {
	                		html.append("<td aling=\"center\"><div class=\"div_button\" onclick=\"ajaxpage('../crm/cards/questionnaire_formupdate.jsp?id="+this.idQuestForm+"&id_block="+rset.getString("ID_QUEST_FORM_FIELD_BLOCK")+"&id_field="+rset.getString("ID_QUEST_FORM_FIELD")+"&type=block&action=moveup&process=yes" + "','div_main');\">" + "<img vspace=\"0\" hspace=\"0\"" +
	                 				" src=\"../images/form/mup.png\" width=\"16\" height=\"16\" align=\"top\" style=\"border: 0px;\"" + 
	                 				" title=\"" + buttonXML.getfieldTransl("button_up", false)+"\">" + getHyperLinkEnd()+"</td>\n");
	                	}
	                	if (rset.getString("BLOCK_CNT").equalsIgnoreCase(rset.getString("ORDER_QUEST_FORM_FIELD_BLOCK"))) {
	                   	   	html.append("<td aling=\"center\"><img vspace=\"0\" hspace=\"0\"" +
	                 				" src=\"../images/form/mb.png\" width=\"16\" height=\"16\" align=\"top\" style=\"border: 0px;\"" + 
	                 				" title=\"" + buttonXML.getfieldTransl("button_up", false)+"\">" + getHyperLinkEnd()+"</td>\n");
	                	} else {
	                   	    html.append("<td aling=\"center\"><div class=\"div_button\" onclick=\"ajaxpage('../crm/cards/questionnaire_formupdate.jsp?id="+this.idQuestForm+"&id_block="+rset.getString("ID_QUEST_FORM_FIELD_BLOCK")+"&id_field="+rset.getString("ID_QUEST_FORM_FIELD")+"&type=block&action=movedown&process=yes" + "','div_main');\">" + "<img vspace=\"0\" hspace=\"0\"" +
	                 				" src=\"../images/form/md.png\" width=\"16\" height=\"16\" align=\"top\" style=\"border: 0px;\"" + 
	                 				" title=\"" + buttonXML.getfieldTransl("button_down", false)+"\">" + getHyperLinkEnd()+"</td>\n");
	                	}
                	} else if ("FIELD".equalsIgnoreCase(rset.getString("TYPE_DATA"))) {
	                	if ("1".equalsIgnoreCase(rset.getString("ORDER_IN_BLOCK_QUEST_FIELD"))) {
	                   	   	html.append("<td aling=\"center\"><img vspace=\"0\" hspace=\"0\"" +
	                 				" src=\"../images/form/mup1.png\" width=\"16\" height=\"16\" align=\"top\" style=\"border: 0px;\"" + 
	                 				" title=\"" + buttonXML.getfieldTransl("button_up", false)+"\">" + getHyperLinkEnd()+"</td>\n");
	                	} else {
	                   	   	html.append("<td aling=\"center\"><div class=\"div_button\" onclick=\"ajaxpage('../crm/cards/questionnaire_formupdate.jsp?id="+this.idQuestForm+"&id_block="+rset.getString("ID_QUEST_FORM_FIELD_BLOCK")+"&id_field="+rset.getString("ID_QUEST_FORM_FIELD")+"&type=field&action=moveup&process=yes" + "','div_main');\">" + "<img vspace=\"0\" hspace=\"0\"" +
	                 				" src=\"../images/form/mup.png\" width=\"16\" height=\"16\" align=\"top\" style=\"border: 0px;\"" + 
	                 				" title=\"" + buttonXML.getfieldTransl("button_up", false)+"\">" + getHyperLinkEnd()+"</td>\n");
	                	}
	                	if (rset.getString("FIELD_CNT").equalsIgnoreCase(rset.getString("ORDER_IN_BLOCK_QUEST_FIELD"))) {
	                   	   	html.append("<td aling=\"center\"><img vspace=\"0\" hspace=\"0\"" +
	                 				" src=\"../images/form/mb.png\" width=\"16\" height=\"16\" align=\"top\" style=\"border: 0px;\"" + 
	                 				" title=\"" + buttonXML.getfieldTransl("button_up", false)+"\">" + getHyperLinkEnd()+"</td>\n");
	                	} else {
	                   	    html.append("<td aling=\"center\"><div class=\"div_button\" onclick=\"ajaxpage('../crm/cards/questionnaire_formupdate.jsp?id="+this.idQuestForm+"&id_block="+rset.getString("ID_QUEST_FORM_FIELD_BLOCK")+"&id_field="+rset.getString("ID_QUEST_FORM_FIELD")+"&type=field&action=movedown&process=yes" + "','div_main');\">" + "<img vspace=\"0\" hspace=\"0\"" +
	                 				" src=\"../images/form/md.png\" width=\"16\" height=\"16\" align=\"top\" style=\"border: 0px;\"" + 
	                 				" title=\"" + buttonXML.getfieldTransl("button_down", false)+"\">" + getHyperLinkEnd()+"</td>\n");
	                	}
                	}
               	    String myDeleteLink = "../crm/cards/questionnaire_formupdate.jsp?id_role="+this.idQuestForm+"&id_field="+rset.getString("ID_QUEST_FORM_FIELD")+"&type=field&action=remove&process=yes";
               	    html.append(getDeleteButtonHTML(myDeleteLink, roleXML.getfieldTransl("LAB_REMOVE_PRIVILEGE", false), rset.getString("ID_QUEST_FORM_FIELD")));
                }
                html.append("</tr>\n");
            }
            html.append("</tbody>\n");
            //html.append("</table>\n");
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
    } // getBKAccountsHTML
	
    public String getQuestionnaireListHTML(String p_beg, String p_end) {
        Connection con = null;
        
        ArrayList<bcFeautureParam> pParam = initParamArray();

        String mySQL = 
        	" SELECT rn, id_quest_int, quest_add_date, date_import, cd_card, full_name_nat_prs, " +
        	"        discount_card_number, discount_card_percent, " +
        	"        DECODE(state_quest, " +
        	"               'NEW', '<b>'||state_quest_tsl||'</b>', " +
        	"               'ERROR', '<font color=\"red\"><b>'||state_quest_tsl||'</b></font>', " +
        	"               'IMPORTED', '<font color=\"green\">'||state_quest_tsl||'</font>', " +
        	"               state_quest_tsl" +
        	"        ) state_quest_tsl, " +
        	"        id_nat_prs," +
        	"        state_quest, error_text, " +
    		"        card_serial_number, card_id_issuer, card_id_payment_system " +
        	"   FROM (SELECT ROWNUM rn, id_quest_int, quest_add_date, date_import, cd_card, full_name_nat_prs, " +
        	"                discount_card_number, discount_card_percent, id_nat_prs," +
        	"                state_quest_tsl, state_quest, error_text, " +
    		"                card_serial_number, card_id_issuer, card_id_payment_system " +
        	"           FROM (SELECT id_quest_int, creation_date_frmt quest_add_date, date_import_frmt date_import, cd_card, " +
        	"                        surname||' '||name||' '||patronymic full_name_nat_prs, " +
    		"        			 	 discount_card_number, discount_card_percent, " +
    		"        			 	 id_nat_prs, state_quest_tsl, state_quest, error_text, " +
    		"                        card_serial_number, card_id_issuer, card_id_payment_system "+
    		"   				FROM " + getGeneralDBScheme() + ".vc_quest_int_club_all " +
    		"                  WHERE id_quest_form = ? " +
        	"                  ORDER BY id_quest_int DESC) " +
        	"          WHERE ROWNUM < ?)" +
        	"  WHERE rn >= ?";
        pParam.add(new bcFeautureParam("int", this.idQuestForm));
        pParam.add(new bcFeautureParam("int", p_end));
        pParam.add(new bcFeautureParam("int", p_beg));
        StringBuilder html = new StringBuilder();
        PreparedStatement st = null;
        
        boolean hasNatPrsPermission = false;
        boolean hasCardPermission = false;
        
        String myBgColor = noneBackGroundStyle;
        String myFont = "<font color=\"black\">";
    	String myFontEnd = "</font>";
        
        try{
        	if (isEditMenuPermited("CLIENTS_NATPERSONS")>=0) {
        		hasNatPrsPermission = true;
        	}
        	if (isEditMenuPermited("CARDS_CLUBCARDS")>=0) {
        		hasCardPermission = true;
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
            	html.append(getBottomFrameTableTH(questionnaireXML, mtd.getColumnName(i)));
            }
            html.append("</tr></thead><tbody>\n");
            while (rset.next())
            {
            	
            	if ("ERROR".equalsIgnoreCase(rset.getString("STATE_QUEST"))) {
               		myBgColor = selectedBackGroundStyle;
               		myFont = "<font color=\"red\">";
                   	myFontEnd = "</font>";
               	} else {
               		myBgColor = noneBackGroundStyle;
               		myFont = "<font color=\"black\">";
                   	myFontEnd = "</font>";
               	}
                html.append("<tr>\n");
                for (int i=1; i<=colCount-6; i++) {
                	if ("ID_QUEST_INT".equalsIgnoreCase(mtd.getColumnName(i))) {
                		String errorText = "";
                		if ("ERROR".equalsIgnoreCase(rset.getString("STATE_QUEST"))) { 
                			errorText = "&nbsp;<img vspace=\"0\" hspace=\"0\" src=\"../images/oper/rows/info.png\" align=\"top\" style=\"border: 0px;\" title=\"" + getValue2(rset.getString("ERROR_TEXT")) +"\">";
					    }
                		html.append("<td "+myBgColor+" >" + myFont + getHyperLinkFirst()+"../crm/cards/questionnaire_importspecs.jsp?type=QUEST&id="+rset.getString("ID_QUEST_INT")+getHyperLinkMiddle()+rset.getString("ID_QUEST_INT")+getHyperLinkEnd() + errorText + myFontEnd + "</td>\n");
                	} else if ("CD_CARD".equalsIgnoreCase(mtd.getColumnName(i))) {
                		if (hasCardPermission && !isEmpty(rset.getString("CARD_SERIAL_NUMBER"))) {
                			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/cards/clubcardspecs.jsp?id="+rset.getString("CARD_SERIAL_NUMBER")+"&iss="+rset.getString("CARD_ID_ISSUER")+"&paysys="+rset.getString("CARD_ID_PAYMENT_SYSTEM"), myFont, myBgColor));
                		} else {
                			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", myFont, myBgColor));
                		}
                    } else if ("FULL_NAME_NAT_PRS".equalsIgnoreCase(mtd.getColumnName(i))) {
                		if (hasNatPrsPermission && !isEmpty(rset.getString("ID_NAT_PRS"))) {
                			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/clients/natpersonspecs.jsp?id="+rset.getString("ID_NAT_PRS"), myFont, myBgColor));
                		} else {
                			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", myFont, myBgColor));
                		}
                    } else {
                    	html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", myFont, myBgColor));
                    }
                }
                html.append("</tr>\n");
            }
            html.append("</tbody>\n");
            //html.append("</table>\n");
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
    } // getBKAccountsHTML

}
