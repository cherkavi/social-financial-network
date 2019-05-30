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

public class bcLGServicePlaceObject extends bcObject {
	private final static Logger LOGGER=Logger.getLogger(bcLGServicePlaceObject.class);
	
	private Map<String,Comparable<String>> fieldHm = new HashMap<String, Comparable<String>>();
	
	private String idServicePlace;
	private String idClub;
	
	public bcLGServicePlaceObject(String pIdServicePlace, String pIdClub) {
		this.idServicePlace = pIdServicePlace;
		this.idClub = pIdClub;
		getFeature();
	}
	
	private void getFeature() {
		String mySQL = " SELECT * FROM " + getGeneralDBScheme() + ".VC_LG_SERVICE_PLACE_CLUB_ALL WHERE id_service_place = ? AND id_club = ?";
		
		bcFeautureParam[] array = new bcFeautureParam[2];
		array[0] = new bcFeautureParam("int", this.idServicePlace);
		array[1] = new bcFeautureParam("int", this.idClub);

		fieldHm = getFeatures2(mySQL, array);
	}
	
	public String getValue(String pColumnName) {
		return getFeautureResult(fieldHm, pColumnName);
	}
	
    public String getPlanLinesHTML(String pFindString, String p_beg, String p_end) {
        StringBuilder html = new StringBuilder();
        Connection con = null;
        PreparedStatement st = null;

        ArrayList<bcFeautureParam> pParam = initParamArray();

        String mySQL = 
        	" SELECT rn, supervisor_date_beg_frmt, supervisor_date_end_frmt,  " +
            "        name_lg_supervisor, plan_promoter_count, plan_day_sale_prom_card_count, " +
            "        plan_day_all_prom_card_count, id_lg_delivery_point_plan, id_lg_supervisor "+
            "   FROM (SELECT ROWNUM rn, name_lg_supervisor, supervisor_date_beg_frmt, supervisor_date_end_frmt,  " +
            "				 plan_promoter_count, plan_day_sale_prom_card_count, " +
            "                plan_day_all_prom_card_count, id_lg_delivery_point_plan, id_lg_supervisor "+
            "           FROM (SELECT * " +
            "                   FROM " + getGeneralDBScheme() + ".vc_lg_delivery_point_plan_all "+
            "                  WHERE id_lg_delivery_point = ? " + 
            "                    AND id_club = ? ";
        pParam.add(new bcFeautureParam("int", this.idServicePlace));
        pParam.add(new bcFeautureParam("int", this.idClub));
        
    	if (!isEmpty(pFindString)) {
    		mySQL = mySQL + 
   				" AND (UPPER(name_lg_supervisor) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(supervisor_date_beg_frmt) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(supervisor_date_end_frmt) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(plan_promoter_count) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(plan_day_sale_prom_card_count) LIKE UPPER('%'||?||'%'))";
    		for (int i=0; i<5; i++) {
    		    pParam.add(new bcFeautureParam("string", pFindString));
    		}
    	}
    	pParam.add(new bcFeautureParam("int", p_end));
    	pParam.add(new bcFeautureParam("int", p_beg));
    	mySQL = mySQL +
            "                  ORDER BY supervisor_date_beg DESC, name_lg_supervisor) " +
            "          WHERE ROWNUM < ? " + 
            " ) WHERE rn >= ?";
        
        boolean hasEditPermission = false;
        boolean hasPromoterPermission = false;
        
        try{
        	if (isEditPermited("LOGISTIC_CLIENTS_DELIVERY_POINTS_PLAN_LINES")>=0) {
        		hasEditPermission = true;
        	}
        	if (isEditMenuPermited("LOGISTIC_CLIENTS_PROMOTERS")>=0) {
        		hasPromoterPermission = true;
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
               html.append(getBottomFrameTableTH(logisticXML, mtd.getColumnName(i)));
            }
            if (hasEditPermission) {
            	html.append("<th>&nbsp;</th><th>&nbsp;</th>\n");
            }
            html.append("</tr></thead><tbody>");
            while (rset.next())
            {
            	html.append("<tr>");
          	  	for (int i=1; i <= colCount-2; i++) {
          	  		if ("NAME_LG_SUPERVISOR".equalsIgnoreCase(mtd.getColumnName(i)) && hasPromoterPermission) {
          	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/logistic/clients/promoterspecs.jsp?id=" + rset.getString("ID_LG_SUPERVISOR"), "", ""));
          	  		} else {
          	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", "", ""));
          	  		}
          	  	}
	            if (hasEditPermission) {
	            	String myHyperLink = "../crm/logistic/clients/delivery_pointupdate.jsp?id="+this.idServicePlace+"&id_club="+this.idClub+"&id_plan="+rset.getString("ID_LG_DELIVERY_POINT_PLAN")+"&type=plan";
	            	String myDeleteLink = "../crm/logistic/clients/delivery_pointupdate.jsp?id="+this.idServicePlace+"&id_club="+this.idClub+"&id_plan="+rset.getString("ID_LG_DELIVERY_POINT_PLAN")+"&type=plan&action=remove&process=yes";
	                html.append(getDeleteButtonHTML(myDeleteLink, logisticXML.getfieldTransl("h_delete_plan", false), rset.getString("NAME_LG_SUPERVISOR")));
	            	html.append(getEditButtonHTML(myHyperLink));
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
	
    public String getSchedulePreviewHTML(String pFindString, String promoterState, String p_beg, String p_end) {
        StringBuilder html = new StringBuilder();
        Connection con = null;
        PreparedStatement st = null;

        ArrayList<bcFeautureParam> pParam = initParamArray();

        String mySQL = 
        	" SELECT rn, date_card_given_frmt," +
        	"        name_lg_promoter, name_lg_promoter_give_state, " +
        	"        begin_work_time_frmt, end_work_time_frmt, sales_cards_count, notes, " +
        	"        id_lg_promoter, id_lg_cc_given_schedule "+
            "   FROM (SELECT ROWNUM rn, id_lg_cc_given_schedule, date_card_given_frmt, " +
            "                name_lg_promoter, name_lg_promoter_give_state, " +
            "                begin_work_time begin_work_time_frmt, " +
            "                end_work_time end_work_time_frmt, sales_cards_count, notes," +
            "                id_lg_promoter "+
            "           FROM (SELECT * " +
            "                   FROM " + getGeneralDBScheme() + ".vc_lg_cc_given_schedule_ln_all "+
            "                  WHERE id_service_place = ? ";
        pParam.add(new bcFeautureParam("int", this.idServicePlace));
        
        if (!isEmpty(promoterState)) {
           	mySQL = mySQL + " AND cd_lg_promoter_give_state = ? ";
           	pParam.add(new bcFeautureParam("string", promoterState));
        }
    	if (!isEmpty(pFindString)) {
    		mySQL = mySQL + 
   				" AND (UPPER(date_card_given_frmt) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(name_lg_promoter) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(begin_work_time_frmt) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(end_work_time_frmt) LIKE UPPER('%'||?||'%'))";
    		for (int i=0; i<4; i++) {
    		    pParam.add(new bcFeautureParam("string", pFindString));
    		}
    	}
    	pParam.add(new bcFeautureParam("int", p_end));
    	pParam.add(new bcFeautureParam("int", p_beg));
        mySQL = mySQL + 
            "                  ORDER BY date_card_given DESC, name_service_place) " +
            "          WHERE ROWNUM < ? " + 
            " ) WHERE rn >= ?";
        
        boolean hasSchedulePermission = false;
        boolean hasPromoterPermission = false;
        
        try{
        	if (isEditMenuPermited("LOGISTIC_CLIENTS_OPERATION_SCHEDULE")>=0) {
        		hasSchedulePermission = true;
        	}
        	if (isEditMenuPermited("LOGISTIC_CLIENTS_PROMOTERS")>=0) {
        		hasPromoterPermission = true;
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
               html.append(getBottomFrameTableTH(logisticXML, mtd.getColumnName(i)));
            }
            html.append("</tr></thead><tbody>");
            while (rset.next())
            {
            	html.append("<tr>");
          	  	for (int i=1; i <= colCount-2; i++) {
          	  		if ("DATE_CARD_GIVEN_FRMT".equalsIgnoreCase(mtd.getColumnName(i)) && hasSchedulePermission) {
          	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/logistic/clients/operation_schedulespecs.jsp?id=" + rset.getString("ID_LG_CC_GIVEN_SCHEDULE"), "", ""));
          	  		} else if ("NAME_LG_PROMOTER".equalsIgnoreCase(mtd.getColumnName(i)) && hasPromoterPermission) {
          	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/logistic/clients/promoterspecs.jsp?id=" + rset.getString("ID_LG_PROMOTER"), "", ""));
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
    }
	
    public String getQuestionnairesHTML(String pFindString, String p_beg, String p_end) {
    	StringBuilder html = new StringBuilder();
        Connection con = null;
        PreparedStatement st = null;

        ArrayList<bcFeautureParam> pParam = initParamArray();

        String mySQL = 
        	" SELECT rn, id_quest_int, date_card_sale_frmt, " +
        	"        name_lg_promoter name_serv_plce_where_card_sold, cd_card, full_name_nat_prs, " +
           	"        discount_card_number, discount_card_percent, state_quest_tsl, date_import, " +
           	"        card_serial_number, card_id_issuer, card_id_payment_system," +
           	"        id_nat_prs, id_lg_promoter " +
           	"   FROM (SELECT ROWNUM rn, id_quest_int, date_card_sale_frmt, name_lg_promoter, " +
           	"                cd_card, surname||' '||name||' '||patronymic full_name_nat_prs, " +
           	"                discount_card_number, discount_card_percent, " +
           	"        		 DECODE(state_quest, " +
            "               		'NEW', '<b>'||state_quest_tsl||'</b>', " +
            "               		'ERROR', '<font color=\"red\"><b>'||state_quest_tsl||'</b></font>', " +
            "               		'IMPORTED', '<font color=\"green\">'||state_quest_tsl||'</font>', " +
            "               		state_quest_tsl" +
            "        		 ) state_quest_tsl, date_import_frmt date_import, " +
            "                card_serial_number, card_id_issuer, card_id_payment_system," +
           	"        		 id_nat_prs, id_lg_promoter " +
            "           FROM (SELECT * " +
            "                   FROM " + getGeneralDBScheme()+".vc_lg_promoter_quest_all " +
            "                  WHERE id_serv_place_where_card_sold = ? ";
        pParam.add(new bcFeautureParam("int", this.idServicePlace));
        
    	if (!isEmpty(pFindString)) {
    		mySQL = mySQL + 
   				" AND (TO_CHAR(id_quest_int) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(date_card_sale_frmt) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(nm_serv_place_where_card_sold) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(cd_card) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(surname||' '||name||' '||patronymic) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(date_import_frmt) LIKE UPPER('%'||?||'%'))";
    		for (int i=0; i<6; i++) {
    		    pParam.add(new bcFeautureParam("string", pFindString));
    		}
    	}
    	pParam.add(new bcFeautureParam("int", p_end));
    	pParam.add(new bcFeautureParam("int", p_beg));
    	mySQL = mySQL +
            "                  ORDER BY date_card_sale DESC) " +
            "          WHERE ROWNUM < ? " + 
            " ) WHERE rn >= ?";
        
        boolean hasQuestionnairePermission = false;
        boolean hasClubCardPermission = false;
        boolean hasNatPrsPermissont = false;
        boolean hasPromoterPermissont = false;
        
        try{
        	if (isEditMenuPermited("CARDS_QUESTIONNAIRE_IMPORT")>=0) {
        		hasQuestionnairePermission = true;
        	}
        	if (isEditMenuPermited("CARDS_CLUBCARDS")>=0) {
        		hasClubCardPermission = true;
        	}
        	if (isEditMenuPermited("CLIENTS_NATPERSONS")>=0) {
        		hasNatPrsPermissont = true;
        	}
        	if (isEditMenuPermited("LOGISTIC_CLIENTS_PROMOTERS")>=0) {
        		hasPromoterPermissont = true;
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
            html.append("</tr></thead><tbody>");
            
            while (rset.next())
            {
            	
            	html.append("<tr>");
          	  	for (int i=1; i <= colCount-5; i++) {
          	  		if ("ID_QUEST_INT".equalsIgnoreCase(mtd.getColumnName(i)) && hasQuestionnairePermission) {
          	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/cards/questionnaire_importspecs.jsp?id=" + rset.getString("ID_QUEST_INT"), "", ""));
          	  		} else if ("CD_CARD".equalsIgnoreCase(mtd.getColumnName(i)) && hasClubCardPermission &&
          	  				!isEmpty(rset.getString("CARD_SERIAL_NUMBER"))) {
          	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/cards/clubcardspecs.jsp?id=" + rset.getString("CARD_SERIAL_NUMBER") + "&iss=" + rset.getString("CARD_ID_ISSUER") + "&paysys=" + rset.getString("CARD_ID_PAYMENT_SYSTEM"), "", ""));
          	  		} else if ("FULL_NAME_NAT_PRS".equalsIgnoreCase(mtd.getColumnName(i)) && hasNatPrsPermissont &&
          	  				!isEmpty(rset.getString("ID_NAT_PRS"))) {
          	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/clients/natpersonspecs.jsp?id=" + rset.getString("ID_NAT_PRS"), "", ""));
          	  		} else if ("NAME_LG_PROMOTER".equalsIgnoreCase(mtd.getColumnName(i)) && hasPromoterPermissont &&
      	  					!isEmpty(rset.getString("ID_LG_PROMOTER"))) {
      	  				html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/logistic/clients/promoterspecs.jsp?id=" + rset.getString("ID_LG_PROMOTER"), "", ""));
      	  			} else {
          	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", "", ""));
          	  		}
          	  	}
                html.append("</tr>\n");
            }
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
