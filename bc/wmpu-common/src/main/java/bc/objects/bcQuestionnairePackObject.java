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


public class bcQuestionnairePackObject extends bcObject {
	private final static Logger LOGGER=Logger.getLogger(bcQuestionnairePackObject.class);
	private Map<String,Comparable<String>> fieldHm = new HashMap<String, Comparable<String>>();
	
    private String idPack;
    
    public bcQuestionnairePackObject(String pIdPack) {
    	this.idPack = pIdPack;
		this.getFeature();
    }
	
	public void getFeature() {
		String featureSelect = " SELECT * FROM " + getGeneralDBScheme() + ".VC_QUEST_PACK_CLUB_ALL WHERE id_quest_pack = ?";
		fieldHm = getFeatures2(featureSelect, this.idPack, false);
	}
	
	public String getValue(String pColumnName) {
		return getFeautureResult(fieldHm, pColumnName);
	}
	
    public String getQuestionnaireListHTML(String pFindString, String pStateQuest, String pStatePack, String p_beg, String p_end) {
        Connection con = null;
        
        ArrayList<bcFeautureParam> pParam = initParamArray();

        String packFiltr = "";
        if ("-1".equalsIgnoreCase(this.idPack)) {
        	packFiltr = " WHERE id_quest_pack IS NULL ";
        } else {
        	packFiltr = " WHERE id_quest_pack = ? ";
        	pParam.add(new bcFeautureParam("int", this.idPack));
        }
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
    		packFiltr;
    	if (!isEmpty(pFindString)) {
    		mySQL = mySQL + 
    			" AND (TO_CHAR(id_quest_int) LIKE UPPER('%'||?||'%') OR " +
    			"      UPPER(creation_date_frmt) LIKE UPPER('%'||?||'%') OR " +
    			"      UPPER(date_import_frmt) LIKE UPPER('%'||?||'%') OR " +
    			"      UPPER(cd_card) LIKE UPPER('%'||?||'%') OR " +
    			"      UPPER(surname||' '||name||' '||patronymic) LIKE UPPER('%'||?||'%'))";
    		for (int i=0; i<5; i++) {
    		    pParam.add(new bcFeautureParam("string", pFindString));
    		}
    	}
    	if (!isEmpty(pStateQuest)) {
    		mySQL = mySQL + " AND state_quest = ? ";
    		pParam.add(new bcFeautureParam("string", pStateQuest));
    	}
    	pParam.add(new bcFeautureParam("int", p_end));
    	pParam.add(new bcFeautureParam("int", p_beg));
    	mySQL = mySQL +
        	"                  ORDER BY id_quest_int DESC) " +
        	"          WHERE ROWNUM < ?)" +
        	"  WHERE rn >= ?";
        StringBuilder html = new StringBuilder();
        PreparedStatement st = null;
        
        boolean hasEditPremission = false;
        boolean hasNatPrsPermission = false;
        boolean hasCardPermission = false;
        
        String myBgColor = noneBackGroundStyle;
        String myFont = "<font color=\"black\">";
    	String myFontEnd = "</font>";
        
        try{
        	if (isEditPermited("CARDS_QUESTIONNAIRE_IMPORT_INFO")>=0) {
        		hasEditPremission = true;
        	}
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
            if (hasEditPremission && "OPENED".equalsIgnoreCase(pStatePack)) {
            	html.append("<th><input id=\"mainCheck\" name=\"mainCheck\" type=\"checkbox\" onclick=\"CheckAll(this,'id_quest')\"></th>\n");
            }
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
                if (hasEditPremission && "OPENED".equalsIgnoreCase(pStatePack)) {
                	html.append("<td "+myBgColor+" >" + myFont + "<INPUT  type=\"checkbox\" value = \"Y\" name=id_quest_"+rset.getString("ID_QUEST_INT")+" id=id_quest_"+rset.getString("ID_QUEST_INT")+" onclick=\"return CheckCB(this);\">" + myFontEnd + "</td>\n");
                }
                for (int i=1; i<=colCount-6; i++) {
                	if ("ID_QUEST_INT".equalsIgnoreCase(mtd.getColumnName(i))) {
                		html.append("<td "+myBgColor+" >" + myFont + getHyperLinkFirst()+"../crm/cards/questionnaire_importspecs.jsp?type=QUEST&id="+rset.getString("ID_QUEST_INT")+getHyperLinkMiddle()+rset.getString("ID_QUEST_INT")+getHyperLinkEnd() + myFontEnd + "</td>\n");
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
                    } else if ("STATE_QUEST_TSL".equalsIgnoreCase(mtd.getColumnName(i))) {
                		String errorText = getValue2(rset.getString("STATE_QUEST_TSL"));
                		if ("ERROR".equalsIgnoreCase(rset.getString("STATE_QUEST"))) { 
                			errorText = errorText + "<br>(<font color=\"red\">" + getValue2(rset.getString("ERROR_TEXT")) +"</font>)";
					    }
                		html.append("<td "+myBgColor+" >" + myFont + errorText + myFontEnd + "</td>\n");
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
