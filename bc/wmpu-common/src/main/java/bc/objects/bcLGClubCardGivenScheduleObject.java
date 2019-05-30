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

public class bcLGClubCardGivenScheduleObject extends bcObject {
	private final static Logger LOGGER=Logger.getLogger(bcLGClubCardGivenScheduleObject.class);
	private Map<String,Comparable<String>> fieldHm = new HashMap<String, Comparable<String>>();
	
	private String idLGCCGivenSchedule;
	
	public bcLGClubCardGivenScheduleObject(String pIdLGCCGivenSchedule) {
		this.idLGCCGivenSchedule = pIdLGCCGivenSchedule;
		getFeature();
	}
	
	private void getFeature() {
		String mySQL = " SELECT * FROM " + getGeneralDBScheme() + ".VC_LG_CC_GIVEN_SCHEDULE_CL_ALL WHERE id_lg_cc_given_schedule = ?";
		fieldHm = getFeatures2(mySQL, this.idLGCCGivenSchedule, false);
	}
	
	public String getValue(String pColumnName) {
		return getFeautureResult(fieldHm, pColumnName);
	}
	
    public String getServicePlacesHTML(String pFindString, String p_beg, String p_end) {
    	StringBuilder html = new StringBuilder();
        Connection con = null;
        PreparedStatement st = null;

        ArrayList<bcFeautureParam> pParam = initParamArray();

        String mySQL = 
        	" SELECT rn, name_service_place service_place, " +
        	"        promoters_count, sales_cards_count, sales_cards_cashier_count, id_service_place "+
            "   FROM (SELECT ROWNUM rn, id_service_place, name_service_place, " +
            "				 promoters_count, sales_cards_count, sales_cards_cashier_count "+
            "           FROM (SELECT id_service_place, name_service_place, " +
            "                        COUNT(DISTINCT id_lg_promoter) promoters_count, " +
            "                        SUM(sales_cards_count) sales_cards_count, " +
            "                        SUM(sales_cards_cashier_count) sales_cards_cashier_count " +
            "                   FROM " + getGeneralDBScheme() + ".vc_lg_cc_given_schedule_ln_all "+
            "                  WHERE id_lg_cc_given_schedule = ? ";
        pParam.add(new bcFeautureParam("int", this.idLGCCGivenSchedule));
        
    	if (!isEmpty(pFindString)) {
    		mySQL = mySQL + 
   				" AND (UPPER(name_service_place) LIKE UPPER('%'||?||'%'))";
    		pParam.add(new bcFeautureParam("string", pFindString));
    	}
    	pParam.add(new bcFeautureParam("int", p_end));
    	pParam.add(new bcFeautureParam("int", p_beg));
    	mySQL = mySQL +
            "                  GROUP BY id_service_place, name_service_place" + 
            "                  ORDER BY name_service_place" +
            "                ) " +
            "          WHERE ROWNUM < ? " + 
            " ) WHERE rn >= ?";
        
        boolean hasServicePlacePermission = false;
        
        try{
        	if (isEditMenuPermited("CLIENTS_SERVICE_PLACE")>=0) {
        		hasServicePlacePermission = true;
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
            	html.append(getBottomFrameTableTH(logisticXML, mtd.getColumnName(i)));
            }
            html.append("</tr></thead><tbody>");
            
            while (rset.next())
            {
            	
            	html.append("<tr>");
          	  	for (int i=1; i <= colCount-1; i++) {
          	  		if ("SERVICE_PLACE".equalsIgnoreCase(mtd.getColumnName(i)) && hasServicePlacePermission &&
          	  				!isEmpty(rset.getString("ID_SERVICE_PLACE"))) {
          	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/clients/service_placespecs.jsp?id=" + rset.getString("ID_SERVICE_PLACE"), "", ""));
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
	
    public String getScheduleLinesEditHTML(String pFindString, String promoterState, String p_beg, String p_end) {
    	StringBuilder html_form = new StringBuilder();
        StringBuilder html_body = new StringBuilder();
        StringBuilder html_apply = new StringBuilder();
        Connection con = null;
        PreparedStatement st = null;

        ArrayList<bcFeautureParam> pParam = initParamArray();

        String mySQL = 
        	" SELECT rn, service_place, name_lg_promoter, name_lg_promoter_post, name_lg_promoter_give_state, " +
        	"        begin_work_time, end_work_time, sales_cards_count, sales_cards_cashier_count, notes, " +
        	"        id_lg_promoter, id_lg_cc_given_schedule_line, " +
        	"        cd_lg_promoter_give_state, id_service_place "+
            "   FROM (SELECT ROWNUM rn, id_lg_cc_given_schedule_line, " +
            "                id_service_place, name_service_place service_place, name_lg_promoter, name_lg_promoter_post, " +
            "                name_lg_promoter_give_state, begin_work_time, end_work_time, " +
            "                sales_cards_count, sales_cards_cashier_count, notes, id_lg_promoter, cd_lg_promoter_give_state "+
            "           FROM (SELECT * " +
            "                   FROM " + getGeneralDBScheme() + ".vc_lg_cc_given_schedule_ln_all "+
            "                  WHERE id_lg_cc_given_schedule = ? ";
        pParam.add(new bcFeautureParam("int", this.idLGCCGivenSchedule));
        
        if (!isEmpty(promoterState)) {
        	mySQL = mySQL + " AND cd_lg_promoter_give_state = ? ";
        	pParam.add(new bcFeautureParam("string", promoterState));
        }
    	if (!isEmpty(pFindString)) {
    		mySQL = mySQL + 
   				" AND (UPPER(name_service_place) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(name_lg_promoter) LIKE UPPER('%'||?||'%'))";
    		for (int i=0; i<2; i++) {
    		    pParam.add(new bcFeautureParam("string", pFindString));
    		}
    	}
    	pParam.add(new bcFeautureParam("int", p_end));
    	pParam.add(new bcFeautureParam("int", p_beg));
        mySQL = mySQL + 
            "                  ORDER BY name_lg_promoter ) " +
            "          WHERE ROWNUM < ? " + 
            " ) WHERE rn >= ?";
        
        boolean hasLGPromoterPermission = false;
        boolean hasServicePlacePermission = false;
        
        String myBGColor = "";
        try{
        	if (isEditMenuPermited("CLIENTS_SERVICE_PLACE")>=0) {
        		hasServicePlacePermission = true;
        	}
        	if (isEditMenuPermited("LOGISTIC_CLIENTS_PROMOTERS")>=0) {
        		hasLGPromoterPermission = true;
        	}
        	
        	LOGGER.debug(prepareSQLToLog(mySQL, pParam));
        	con = Connector.getConnection(getSessionId());
        	st = con.prepareStatement(mySQL);
        	st = prepareParam(st, pParam);
        	ResultSet rset = st.executeQuery();
            ResultSetMetaData mtd = rset.getMetaData();
            int colCount = mtd.getColumnCount();
            
            int rowCount = 0;

            
            html_form.append("<form method=\"post\" name=\"updateForm\" id=\"updateForm\" action=\"../crm/logistic/clients/operation_scheduleupdate.jsp\">\n");
            html_form.append("<input type=\"hidden\" name=\"type\" value=\"set_schedule\">\n");
            html_form.append("<input type=\"hidden\" name=\"action\" value=\"set\">\n");
            html_form.append("<input type=\"hidden\" name=\"process\" value=\"yes\">\n");
            html_form.append("<input type=\"hidden\" name=\"id\" value=\"" + this.idLGCCGivenSchedule + "\">\n");
            
            html_form.append(getBottomFrameTable());
            html_form.append("<tr>");
            for (int i=1; i <= colCount-4; i++) {
            	html_form.append(getBottomFrameTableTH(logisticXML, mtd.getColumnName(i)));
            }
            html_form.append("</tr></thead><tbody>");
            
            html_apply.append("<tr>");
            html_apply.append("<td colspan=\"10\" align=\"center\">");
            html_apply.append(getSubmitButtonAjax("../crm/logistic/clients/operation_scheduleupdate.jsp"));
            html_apply.append("</td>");
            html_apply.append("</tr>");
            
            while (rset.next())
            {
            	rowCount = rowCount + 1;
            	String id = rset.getString("ID_LG_CC_GIVEN_SCHEDULE_LINE");
            	
            	if ("WORKS".equalsIgnoreCase(rset.getString("CD_LG_PROMOTER_GIVE_STATE"))) {
            		myBGColor = "";
            	} else {
            		myBGColor = selectedBackGroundStyle;
            	}
                
                html_body.append("<tr id=\"tr_"+id+"\">");
          	  	for (int i=1; i <= colCount-4; i++) {
          	  		html_body.append("<INPUT TYPE=\"hidden\" name=\"id_"+id+"\" value=\""+id+"\" class=\"inputfield\">");
	          	  	if ("NAME_LG_PROMOTER".equalsIgnoreCase(mtd.getColumnName(i)) && hasLGPromoterPermission &&
          	  				!isEmpty(rset.getString("ID_LG_PROMOTER"))) {
	          	  		html_body.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/logistic/clients/promoterspecs.jsp?id=" + rset.getString("ID_LG_PROMOTER"), "", myBGColor));
	          	  	} else if ("SERVICE_PLACE".equalsIgnoreCase(mtd.getColumnName(i)) && hasServicePlacePermission &&
          	  				!isEmpty(rset.getString("ID_SERVICE_PLACE"))) {
		          		html_body.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/clients/service_placespecs.jsp?id=" + rset.getString("ID_SERVICE_PLACE"), "", myBGColor));
          	  		} else if ("BEGIN_WORK_TIME".equalsIgnoreCase(mtd.getColumnName(i))) {
	          	  		String tmp_sp = "<INPUT TYPE=\"text\" size=\"10\" name=\"begin_"+id+"\" value=\""+this.getHTMLValue(rset.getString(i))+"\" class=\"inputfield\">";
	          	  		html_body.append(getBottomFrameTableTD(mtd.getColumnName(i), tmp_sp, "", "", myBGColor));
		          	} else if ("END_WORK_TIME".equalsIgnoreCase(mtd.getColumnName(i))) {
	          	  		String tmp_sp = "<INPUT TYPE=\"text\" size=\"10\" name=\"end_"+id+"\" value=\""+this.getHTMLValue(rset.getString(i))+"\" class=\"inputfield\">";
	          	  		html_body.append(getBottomFrameTableTD(mtd.getColumnName(i), tmp_sp, "", "", myBGColor));
		          	} else if ("NOTES".equalsIgnoreCase(mtd.getColumnName(i))) {
	          	  		String tmp_sp = "<textarea name=\"notes_"+id+"\" cols=\"30\" rows=\"2\" class=\"inputfield\">"+this.getHTMLValue(rset.getString(i))+"</textarea>";
	          	  		html_body.append(getBottomFrameTableTD(mtd.getColumnName(i), tmp_sp, "", "", myBGColor));
	          	  	} else {
	          	  		html_body.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", "", myBGColor));
	          	  	}
          	  	}
          	  	html_body.append("</tr>\n");
            }
            if (rowCount > 0 ) {
            	html_form.append(html_apply);
            	html_form.append(html_body);
                html_form.append(html_apply);
            } else {
            	html_form.append(html_body);
            }
            html_form.append("</tbody></table>\n");
        } // try
        catch (SQLException e) {LOGGER.error(html_form, e); html_form.append(showCRMException(e));}
       	catch (Exception el) {LOGGER.error(html_form, el); html_form.append(showCRMException(el));}
        finally {
            try {
                if (st!=null) st.close();
            } catch (SQLException w) {w.toString();}
            try {
                if (con!=null) con.close();
            } catch (SQLException w) {w.toString();}
            Connector.closeConnection(con);
        } // finally
        return html_form.toString();
    }
	
    public String getScheduleLinesPreviewHTML(String pFindString, String promoterState, String p_beg, String p_end) {
    	StringBuilder html = new StringBuilder();
        Connection con = null;
        PreparedStatement st = null;

        ArrayList<bcFeautureParam> pParam = initParamArray();

        String mySQL = 
        	" SELECT rn, service_place, name_lg_promoter, name_lg_promoter_give_state, " +
        	"        begin_work_time_frmt, end_work_time_frmt, sales_cards_count, sales_cards_cashier_count, notes, " +
        	"        id_lg_cc_given_schedule_line, id_lg_promoter, cd_lg_promoter_give_state," +
        	"        id_service_place "+
            "   FROM (SELECT ROWNUM rn, id_lg_cc_given_schedule_line, " +
            "                id_service_place, name_service_place service_place, name_lg_promoter, " +
            "				 name_lg_promoter_give_state, begin_work_time begin_work_time_frmt, " +
            "                end_work_time end_work_time_frmt, " +
            "                sales_cards_count, sales_cards_cashier_count, notes, id_lg_promoter, cd_lg_promoter_give_state "+
            "           FROM (SELECT * " +
            "                   FROM " + getGeneralDBScheme() + ".vc_lg_cc_given_schedule_ln_all "+
            "                  WHERE id_lg_cc_given_schedule = ? ";
        pParam.add(new bcFeautureParam("int", this.idLGCCGivenSchedule));
        
    	if (!isEmpty(pFindString)) {
    		mySQL = mySQL + 
   				" AND (UPPER(name_service_place) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(name_lg_promoter) LIKE UPPER('%'||?||'%'))";
    		for (int i=0; i<2; i++) {
    		    pParam.add(new bcFeautureParam("string", pFindString));
    		}
    	}
        if (!isEmpty(promoterState)) {
        	mySQL = mySQL + " AND cd_lg_promoter_give_state = ? ";
        	pParam.add(new bcFeautureParam("string", promoterState));
        }
        pParam.add(new bcFeautureParam("int", p_end));
        pParam.add(new bcFeautureParam("int", p_beg));
        mySQL = mySQL + 
            "                  ORDER BY name_lg_promoter ) " +
            "          WHERE ROWNUM < ? " + 
            " ) WHERE rn >= ?";
        
        boolean hasLGPromoterPermission = false;
        boolean hasServicePlacePermission = false;
        
        String myBGColor = "";
        
        try{
        	if (isEditMenuPermited("CLIENTS_SERVICE_PLACE")>=0) {
        		hasServicePlacePermission = true;
        	}
        	if (isEditMenuPermited("LOGISTIC_CLIENTS_PROMOTERS")>=0) {
        		hasLGPromoterPermission = true;
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
            	html.append(getBottomFrameTableTH(logisticXML, mtd.getColumnName(i)));
            }
            html.append("</tr></thead><tbody>");
            
            while (rset.next())
            {
            	
            	if ("WORKS".equalsIgnoreCase(rset.getString("CD_LG_PROMOTER_GIVE_STATE"))) {
            		myBGColor = "";
            	} else {
            		myBGColor = selectedBackGroundStyle;
            	}
            	
            	html.append("<tr>");
          	  	for (int i=1; i <= colCount-4; i++) {
          	  		if ("NAME_LG_PROMOTER".equalsIgnoreCase(mtd.getColumnName(i)) && hasLGPromoterPermission &&
      	  					!isEmpty(rset.getString("ID_LG_PROMOTER"))) {
          	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/logistic/clients/promoterspecs.jsp?id=" + rset.getString("ID_LG_PROMOTER"), "", myBGColor));
          	  		} else if ("SERVICE_PLACE".equalsIgnoreCase(mtd.getColumnName(i)) && hasServicePlacePermission &&
      	  					!isEmpty(rset.getString("ID_SERVICE_PLACE"))) {
          	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/clients/service_placespecs.jsp?id=" + rset.getString("ID_SERVICE_PLACE"), "", myBGColor));
      	  			} else {
          	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", "", myBGColor));
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
	
    public String getQuestionnairesHTML(String pFindString, String p_beg, String p_end) {
    	StringBuilder html = new StringBuilder();
        Connection con = null;
        PreparedStatement st = null;

        ArrayList<bcFeautureParam> pParam = initParamArray();

        String mySQL = 
        	" SELECT rn, id_quest_int, date_card_sale_frmt, name_lg_promoter lg_promoter, cd_card, full_name_nat_prs, " +
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
            "                   FROM " + getGeneralDBScheme()+".vc_lg_cc_given_quest_all " +
            "                  WHERE TO_CHAR(date_card_sale, 'YYYYMMDD') = ? ";
        pParam.add(new bcFeautureParam("string", this.getValue("DATE_CARD_GIVEN_ID")));
        
    	if (!isEmpty(pFindString)) {
    		mySQL = mySQL + 
   				" AND (TO_CHAR(id_quest_int) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(date_card_sale_frmt) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(name_lg_promoter) LIKE UPPER('%'||?||'%') OR " +
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
        boolean hasLGPromoterPermissont = false;
        
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
        		hasLGPromoterPermissont = true;
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
          	  		} else if ("LG_PROMOTER".equalsIgnoreCase(mtd.getColumnName(i)) && hasLGPromoterPermissont &&
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
