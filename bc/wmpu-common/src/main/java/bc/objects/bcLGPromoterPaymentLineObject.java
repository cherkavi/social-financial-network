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

public class bcLGPromoterPaymentLineObject extends bcObject {
	private final static Logger LOGGER=Logger.getLogger(bcLGPromoterPaymentLineObject.class);
	
	private Map<String,Comparable<String>> fieldHm = new HashMap<String, Comparable<String>>();
	
	private String idLGPromoter;
	
	public bcLGPromoterPaymentLineObject(String pIdLGPromoter) {
		this.idLGPromoter = pIdLGPromoter;
		getFeature();
	}
	
	private void getFeature() {
		String mySQL = " SELECT * FROM " + getGeneralDBScheme() + ".VC_LG_PROMOTER_CLUB_ALL WHERE id_lg_promoter = ?";
		fieldHm = getFeatures2(mySQL, this.idLGPromoter, false);
	}
	
	public String getValue(String pColumnName) {
		return getFeautureResult(fieldHm, pColumnName);
	}
	
    public String getSchedulePreviewHTML(String pFindString, String promoterState, String p_beg, String p_end) {
        StringBuilder html = new StringBuilder();
        Connection con = null;
        PreparedStatement st = null;

        ArrayList<bcFeautureParam> pParam = initParamArray();

        String mySQL = 
        	" SELECT rn, date_card_given_frmt," +
        	"        name_service_place service_place, name_lg_promoter_give_state, " +
        	"        begin_work_time_frmt, end_work_time_frmt, sales_cards_count, notes, " +
        	"        id_service_place, id_lg_cc_given_schedule "+
            "   FROM (SELECT ROWNUM rn, id_lg_cc_given_schedule, date_card_given_frmt, " +
            "                name_service_place, name_lg_promoter_give_state, " +
            "                begin_work_time begin_work_time_frmt, " +
            "                end_work_time end_work_time_frmt, sales_cards_count, notes," +
            "                id_service_place "+
            "           FROM (SELECT * " +
            "                   FROM " + getGeneralDBScheme() + ".vc_lg_cc_given_schedule_ln_all "+
            "                  WHERE id_lg_promoter = ? ";
        pParam.add(new bcFeautureParam("int", this.idLGPromoter));
        
        if (!isEmpty(promoterState)) {
           	mySQL = mySQL + " AND cd_lg_promoter_give_state = ? ";
           	pParam.add(new bcFeautureParam("string", promoterState));
        }
    	if (!isEmpty(pFindString)) {
    		mySQL = mySQL + 
   				" AND (UPPER(date_card_given_frmt) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(name_service_place) LIKE UPPER('%'||?||'%'))";
    		for (int i=0; i<2; i++) {
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
        boolean hasServicePlacePermission = false;
        
        try{
        	if (isEditMenuPermited("LOGISTIC_CLIENTS_OPERATION_SCHEDULE")>=0) {
        		hasSchedulePermission = true;
        	}
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
          	  		} else if ("SERVICE_PLACE".equalsIgnoreCase(mtd.getColumnName(i)) && hasServicePlacePermission) {
          	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/clients/service_placespecs.jsp?id=" + rset.getString("ID_SERVICE_PLACE"), "", ""));
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
	
    public String getScheduleEditHTML(String pFindString, String promoterState, String p_beg, String p_end) {
    	StringBuilder html_form = new StringBuilder();
        StringBuilder html_body = new StringBuilder();
        StringBuilder html_apply = new StringBuilder();
        Connection con = null;
        PreparedStatement st = null;

        ArrayList<bcFeautureParam> pParam = initParamArray();

        String mySQL = 
        	" SELECT rn, date_card_given_frmt, " +
        	"        name_service_place service_place, name_lg_promoter_give_state, " +
        	"        begin_work_time, end_work_time, sales_cards_count, notes, " +
        	"        id_service_place, id_lg_cc_given_schedule, cd_lg_promoter_give_state," +
        	"        id_lg_cc_given_schedule_line "+
            "   FROM (SELECT ROWNUM rn, date_card_given_frmt, name_service_place, " +
            "                name_lg_promoter_give_state, begin_work_time, end_work_time, " +
            "                sales_cards_count, notes, id_service_place, " +
            "                id_lg_cc_given_schedule, cd_lg_promoter_give_state," +
            "                id_lg_cc_given_schedule_line "+
            "           FROM (SELECT * " +
            "                   FROM " + getGeneralDBScheme() + ".vc_lg_cc_given_schedule_ln_all "+
            "                  WHERE id_lg_promoter = ? ";
        pParam.add(new bcFeautureParam("int", this.idLGPromoter));
        
        if (!isEmpty(promoterState)) {
        	mySQL = mySQL + " AND cd_lg_promoter_give_state = ? ";
        	pParam.add(new bcFeautureParam("string", promoterState));
        }
    	if (!isEmpty(pFindString)) {
    		mySQL = mySQL + 
   				" AND (UPPER(date_card_given_frmt) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(name_service_place) LIKE UPPER('%'||?||'%'))";
    		for (int i=0; i<2; i++) {
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
        boolean hasServicePlacePermission = false;
        
        String myBGColor = "";
        try{
        	if (isEditMenuPermited("LOGISTIC_CLIENTS_OPERATION_SCHEDULE")>=0) {
        		hasSchedulePermission = true;
        	}
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
            
            int rowCount = 0;

            
            html_form.append("<form method=\"post\" name=\"updateForm\" id=\"updateForm\" action=\"../crm/logistic/clients/promoterupdate.jsp\">\n");
            html_form.append("<input type=\"hidden\" name=\"type\" value=\"set_schedule\">\n");
            html_form.append("<input type=\"hidden\" name=\"action\" value=\"set\">\n");
            html_form.append("<input type=\"hidden\" name=\"process\" value=\"yes\">\n");
            html_form.append("<input type=\"hidden\" name=\"id\" value=\"" + this.idLGPromoter + "\">\n");
            
            html_form.append(getBottomFrameTable());
            html_form.append("<tr>");
            for (int i=1; i <= colCount-4; i++) {
            	html_form.append(getBottomFrameTableTH(logisticXML, mtd.getColumnName(i)));
            }
            html_form.append("<th>&nbsp;</th>");
            html_form.append("</tr></thead><tbody>");
            
            html_apply.append("<tr>");
            html_apply.append("<td colspan=\"11\" align=\"center\">");
            html_apply.append(getSubmitButtonAjax("../crm/logistic/clients/promoterupdate.jsp"));
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
	          	  	if ("SERVICE_PLACE".equalsIgnoreCase(mtd.getColumnName(i))) {
	          	  		String tmp_sp = "<INPUT TYPE=\"hidden\" name=\"id_"+id+"\" value=\""+id+"\" class=\"inputfield\">";
	          	  		if (!isEmpty(rset.getString("ID_SERVICE_PLACE"))) {
		          	  		if (hasServicePlacePermission) {
		          	  			html_body.append(getBottomFrameTableTD(mtd.getColumnName(i), tmp_sp + rset.getString(i), "../crm/clients/service_placespecs.jsp?id=" + rset.getString("ID_SERVICE_PLACE"), "", myBGColor));
		          	  		} else {
		          	  			html_body.append(getBottomFrameTableTD(mtd.getColumnName(i), tmp_sp + rset.getString(i), "", "", myBGColor));
		          	  		}
	          	  		} else {
	          	  			html_body.append(getBottomFrameTableTD(mtd.getColumnName(i), tmp_sp + "&nbsp;", "", "", myBGColor));
	          	  		} 
	          	  	} else if ("DATE_CARD_GIVEN_FRMT".equalsIgnoreCase(mtd.getColumnName(i)) && hasSchedulePermission) {
	          	  		html_body.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/logistic/clients/operation_schedulespecs.jsp?id=" + rset.getString("ID_LG_CC_GIVEN_SCHEDULE"), "", myBGColor));
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
          	  	String myDeleteLink = "../crm/logistic/clients/promoterupdate.jsp?type=line&id="+this.idLGPromoter+"&id_line="+id+"&action=remove&process=yes";
          	  	html_body.append(getDeleteButtonStyle2HTML(myDeleteLink, myBGColor, logisticXML.getfieldTransl("h_delete_schedule_line", false), id));
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
	
    public String getQuestionnairesHTML(String pFindString, String p_beg, String p_end) {
    	StringBuilder html = new StringBuilder();
        Connection con = null;
        PreparedStatement st = null;

        ArrayList<bcFeautureParam> pParam = initParamArray();

        String mySQL = 
        	" SELECT rn, id_quest_int, date_card_sale_frmt, " +
        	"        nm_serv_place_where_card_sold name_serv_plce_where_card_sold, cd_card, full_name_nat_prs, " +
           	"        discount_card_number, discount_card_percent, state_quest_tsl, date_import, " +
           	"        card_serial_number, card_id_issuer, card_id_payment_system," +
           	"        id_nat_prs, id_serv_place_where_card_sold " +
           	"   FROM (SELECT ROWNUM rn, id_quest_int, date_card_sale_frmt, nm_serv_place_where_card_sold, " +
           	"                cd_card, surname||' '||name||' '||patronymic full_name_nat_prs, " +
           	"                discount_card_number, discount_card_percent, " +
           	"        		 DECODE(state_quest, " +
            "               		'NEW', '<b>'||state_quest_tsl||'</b>', " +
            "               		'ERROR', '<font color=\"red\"><b>'||state_quest_tsl||'</b></font>', " +
            "               		'IMPORTED', '<font color=\"green\">'||state_quest_tsl||'</font>', " +
            "               		state_quest_tsl" +
            "        		 ) state_quest_tsl, date_import_frmt date_import, " +
            "                card_serial_number, card_id_issuer, card_id_payment_system," +
           	"        		 id_nat_prs, id_serv_place_where_card_sold " +
            "           FROM (SELECT * " +
            "                   FROM " + getGeneralDBScheme()+".vc_lg_promoter_quest_all " +
            "                  WHERE id_lg_promoter = ? ";
        pParam.add(new bcFeautureParam("int", this.idLGPromoter));
        
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
        boolean hasServicePlacePermissont = false;
        
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
        	if (isEditMenuPermited("CLIENTS_SERVICE_PLACE")>=0) {
        		hasServicePlacePermissont = true;
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
          	  		} else if ("NAME_SERV_PLCE_WHERE_CARD_SOLD".equalsIgnoreCase(mtd.getColumnName(i)) && hasServicePlacePermissont &&
      	  					!isEmpty(rset.getString("ID_SERV_PLACE_WHERE_CARD_SOLD"))) {
      	  				html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/clients/service_placespecs.jsp?id=" + rset.getString("ID_SERV_PLACE_WHERE_CARD_SOLD"), "", ""));
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
	
    public String getWorkPlacesHistoryHTML(String pFindString, String p_beg, String p_end) {
        StringBuilder html = new StringBuilder();
        PreparedStatement st = null;
        Connection con = null;

        ArrayList<bcFeautureParam> pParam = initParamArray();

        String mySQL = 
        	" SELECT rn, sname_jur_prs jur_prs, name_service_place service_place, name_lg_promoter_post, name_lg_promoter_state,  " +
        	"        date_begin_work_frmt, date_end_work_frmt, id_lg_promoter_work, id_jur_prs, id_service_place "+
            "   FROM (SELECT ROWNUM rn, sname_jur_prs, name_service_place, " +
            "                DECODE(cd_lg_promoter_post, " +
	  		"                      'PROMOTER', '<font color=\"black\"><b>'||name_lg_promoter_post||'</b></font>', " +
	  		"                      'SUPERVISOR', '<font color=\"blue\"><b>'||name_lg_promoter_post||'</b></font>', " +
	  		"                      name_lg_promoter_post " +
		  	"                ) name_lg_promoter_post, " +
            "                DECODE(cd_lg_promoter_state, " +
	  		"                      'ACCEPTED', '<font color=\"green\"><b>'||name_lg_promoter_state||'</b></font>', " +
	  		"                      'TRANSFERRED', '<font color=\"blue\"><b>'||name_lg_promoter_state||'</b></font>', " +
	  		"                      'DISMISSED', '<font color=\"red\"><b>'||name_lg_promoter_state||'</b></font>', " +
	  		"                      name_lg_promoter_state " +
		  	"                ) name_lg_promoter_state,  " +
        	"                date_begin_work_frmt, date_end_work_frmt, id_lg_promoter_work, id_jur_prs, id_service_place "+
            "           FROM (SELECT * " +
            "                   FROM " + getGeneralDBScheme() + ".vc_lg_promoter_work_h_all" + 
            "                  WHERE id_lg_promoter = ? ";
        pParam.add(new bcFeautureParam("int", this.idLGPromoter));
        
    	if (!isEmpty(pFindString)) {
    		mySQL = mySQL + 
   				" AND (TO_CHAR(sname_jur_prs) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(name_service_place) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(name_lg_promoter_post) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(name_lg_promoter_state) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(date_begin_work_frmt) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(date_end_work_frmt) LIKE UPPER('%'||?||'%'))";
    		for (int i=0; i<6; i++) {
    		    pParam.add(new bcFeautureParam("string", pFindString));
    		}
    	}
    	pParam.add(new bcFeautureParam("int", p_end));
    	pParam.add(new bcFeautureParam("int", p_beg));
        mySQL = mySQL +
            "         ORDER BY date_begin_work DESC) " +
            "      WHERE ROWNUM < ? " + 
            " ) WHERE rn >= ?";
        
        boolean hasJurPrsPermission = false;
        boolean hasServicePlacePermission = false;
        boolean hasEditPermission = false;
        try{
        	if (isEditPermited("LOGISTIC_CLIENTS_PROMOTERS_INFO")>0) {
        		hasEditPermission = true;
        	}
        	if (isEditMenuPermited("CLIENTS_YURPERSONS")>=0) {
        		hasJurPrsPermission = true;
        	}
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
            for (int i=1; i <= colCount-3; i++) {
               html.append(getBottomFrameTableTH(logisticXML, mtd.getColumnName(i)));
            }
            if (hasEditPermission) {
            	html.append("<th width=\"10\">&nbsp;</th><th width=\"10\">&nbsp;</th>\n");
            }
            html.append("</tr></thead><tbody>\n");
            while (rset.next())
            {
            	html.append("<tr>");
          	  	for (int i=1; i <= colCount-3; i++) {
          	  		if (("JUR_PRS".equalsIgnoreCase(mtd.getColumnName(i))) && hasJurPrsPermission &&
          	  				!isEmpty(rset.getString("ID_JUR_PRS"))) {
          	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/clients/yurpersonspecs.jsp?id=" + rset.getString("ID_JUR_PRS"), "", ""));
          	  		} else if (("SERVICE_PLACE".equalsIgnoreCase(mtd.getColumnName(i))) && hasServicePlacePermission &&
          	  				!isEmpty(rset.getString("ID_SERVICE_PLACE"))) {
          	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/clients/service_placespecs.jsp?id=" + rset.getString("ID_SERVICE_PLACE"), "", ""));
          	  		} else {
          	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", "", ""));
          	  		}
          	  	}
                if (hasEditPermission) {
                  	String myHyperLink = "../crm/logistic/clients/promoterupdate.jsp?type=work&id=" + this.idLGPromoter + "&id_work=" + rset.getString("ID_LG_PROMOTER_WORK");
                  	String myDeleteLink = "../crm/logistic/clients/promoterupdate.jsp?type=work&id=" + this.idLGPromoter + "&id_work=" + rset.getString("ID_LG_PROMOTER_WORK") + "&action=remove&process=yes";
                	html.append(getDeleteButtonHTML(myDeleteLink, logisticXML.getfieldTransl("l_remove_work_h", false), rset.getString("DATE_BEGIN_WORK_FRMT")));
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
    } // getContactListHTML
	
    public String getManagerHistoryHTML(String pFindString, String p_beg, String p_end) {
        StringBuilder html = new StringBuilder();
        PreparedStatement st = null;
        Connection con = null;
        String lPromoterPos = this.getValue("CD_LG_PROMOTER_POST");
        
        String mySQL = "";

        ArrayList<bcFeautureParam> pParam = initParamArray();

        if ("PROMOTER".equalsIgnoreCase(lPromoterPos)) {
        	mySQL = mySQL +
	        	" SELECT rn, date_begin_set_frmt, date_end_set_frmt, name_lg_supervisor, value_paym_for_oc_sale_frmt, " +
	        	"        id_lg_promoter_manager, id_lg_supervisor "+
	            "   FROM (SELECT ROWNUM rn, name_lg_manager name_lg_supervisor, " +
	            "                date_begin_set_frmt, date_end_set_frmt, " +
	            "                DECODE (value_paym_for_one_card_sale," +
	            "                        NULL, ''," +
	            "                        value_paym_for_oc_sale_frmt||' '||sname_currency_payment" +
	            "                ) value_paym_for_oc_sale_frmt, " +
	        	"                id_lg_promoter_manager, id_lg_manager id_lg_supervisor "+
	            "           FROM (SELECT * " +
	            "                   FROM " + getGeneralDBScheme() + ".vc_lg_promoter_mngr_h_all" + 
	            "                  WHERE id_lg_promoter_work = ? ";
        	pParam.add(new bcFeautureParam("int", this.getValue("ID_LG_PROMOTER_WORK_CURRENT")));
        	
	    	if (!isEmpty(pFindString)) {
	    		mySQL = mySQL + 
	   				" AND (TO_CHAR(name_lg_manager) LIKE UPPER('%'||?||'%') OR " +
	   				"      UPPER(date_begin_set_frmt) LIKE UPPER('%'||?||'%') OR " +
	   				"      UPPER(date_end_set_frmt) LIKE UPPER('%'||?||'%'))";
	    		for (int i=0; i<3; i++) {
	    		    pParam.add(new bcFeautureParam("string", pFindString));
	    		}
	    	}
	    	pParam.add(new bcFeautureParam("int", p_end));
	    	pParam.add(new bcFeautureParam("int", p_beg));
	        mySQL = mySQL +
	            "         ORDER BY date_begin_set DESC) " +
	            "      WHERE ROWNUM < ? " + 
	            " ) WHERE rn >= ?";
        } else if ("CASHIER".equalsIgnoreCase(lPromoterPos) ||
        		"GAS_STATION_OPERATOR".equalsIgnoreCase(lPromoterPos) ||
        		"SELLER".equalsIgnoreCase(lPromoterPos)) {
        	mySQL = mySQL +
	        	" SELECT rn, date_begin_set_frmt, date_end_set_frmt, name_lg_manager, value_paym_for_oc_sale_frmt, " +
	        	"        id_lg_promoter_manager, id_lg_supervisor "+
	            "   FROM (SELECT ROWNUM rn, name_lg_manager, " +
	            "                date_begin_set_frmt, date_end_set_frmt, " +
	            "                DECODE (value_paym_for_one_card_sale," +
	            "                        NULL, ''," +
	            "                        value_paym_for_oc_sale_frmt||' '||sname_currency_payment" +
	            "                ) value_paym_for_oc_sale_frmt, " +
	        	"                id_lg_promoter_manager, id_lg_manager id_lg_supervisor "+
	            "           FROM (SELECT * " +
	            "                   FROM " + getGeneralDBScheme() + ".vc_lg_promoter_mngr_h_all" + 
	            "                  WHERE id_lg_promoter_work = ? ";
        	pParam.add(new bcFeautureParam("int", this.getValue("ID_LG_PROMOTER_WORK_CURRENT")));
        	
	    	if (!isEmpty(pFindString)) {
	    		mySQL = mySQL + 
	   				" AND (TO_CHAR(name_lg_manager) LIKE UPPER('%'||?||'%') OR " +
	   				"      UPPER(date_begin_set_frmt) LIKE UPPER('%'||?||'%') OR " +
	   				"      UPPER(date_end_set_frmt) LIKE UPPER('%'||?||'%'))";
	    		for (int i=0; i<3; i++) {
	    		    pParam.add(new bcFeautureParam("string", pFindString));
	    		}
	    	}
	    	pParam.add(new bcFeautureParam("int", p_end));
	    	pParam.add(new bcFeautureParam("int", p_beg));
	        mySQL = mySQL +
	            "         ORDER BY date_begin_set DESC) " +
	            "      WHERE ROWNUM < ? " +
	            " ) WHERE rn >= ?";
        }
        
        boolean hasSuperVisorPermission = false;
        boolean hasEditPermission = false;
        try{
        	if (isEditPermited("LOGISTIC_CLIENTS_PROMOTERS_INFO")>0) {
        		hasEditPermission = true;
        	}
        	if (isEditMenuPermited("LOGISTIC_CLIENTS_PROMOTERS")>=0) {
        		hasSuperVisorPermission = true;
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
            	html.append("<th width=\"10\">&nbsp;</th><th width=\"10\">&nbsp;</th>\n");
            }
            html.append("</tr></thead><tbody>\n");
            while (rset.next())
            {
            	html.append("<tr>");
          	  	for (int i=1; i <= colCount-2; i++) {
          	  		if (("NAME_LG_SUPERVISOR".equalsIgnoreCase(mtd.getColumnName(i))) && hasSuperVisorPermission) {
          	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/logistic/clients/promoterspecs.jsp?id=" + rset.getString("ID_LG_SUPERVISOR"), "", ""));
          	  		} else {
          	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", "", ""));
          	  		}
          	  	}
                if (hasEditPermission) {
                  	String myHyperLink = "../crm/logistic/clients/promoterupdate.jsp?type=supervisor&id=" + this.idLGPromoter + "&id_supervisor=" + rset.getString("ID_LG_PROMOTER_MANAGER");
                  	String myDeleteLink = "../crm/logistic/clients/promoterupdate.jsp?type=supervisor&id=" + this.idLGPromoter + "&id_supervisor=" + rset.getString("ID_LG_PROMOTER_MANAGER") + "&action=remove&process=yes";
                	html.append(getDeleteButtonHTML(myDeleteLink, logisticXML.getfieldTransl("l_remove_supervisor_h", false), rset.getString("DATE_BEGIN_SET_FRMT")));
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
    } // getContactListHTML
	
    public String getPaymentParamHistoryHTML(String pFindString, String p_beg, String p_end) {
        StringBuilder html = new StringBuilder();
        PreparedStatement st = null;
        Connection con = null;
        String mySQL = "";
        String lPromoterPos = this.getValue("CD_LG_PROMOTER_POST");

        ArrayList<bcFeautureParam> pParam = initParamArray();

        if ("CASHIER".equalsIgnoreCase(lPromoterPos) ||
        		"GAS_STATION_OPERATOR".equalsIgnoreCase(lPromoterPos) ||
        		"SELLER".equalsIgnoreCase(lPromoterPos)) {
	        mySQL = 
	        	" SELECT rn, date_begin_pay_param_frmt, " +
	        	"        cashier_pay_oc_salary_frmt, cashier_first_card_not_pay, " +
	        	"        id_lg_promoter_pay_param "+
	            "   FROM (SELECT ROWNUM rn, date_begin_pay_param_frmt, " +
	            "                DECODE(cashier_pay_one_card_salary," +
	            "                       NULL, ''," +
	            "                       cashier_pay_oc_salary_frmt||' '||sname_currency" +
	            "                ) cashier_pay_oc_salary_frmt, " +
	            "                cashier_first_card_not_pay, " +
	        	"                id_lg_promoter_pay_param "+
	            "           FROM (SELECT * " +
	            "                   FROM " + getGeneralDBScheme() + ".vc_lg_promoter_pay_param_h_all" + 
	            "                  WHERE id_lg_promoter_work = ? ";
	        pParam.add(new bcFeautureParam("int", this.getValue("ID_LG_PROMOTER_WORK_CURRENT")));
	        
	    	if (!isEmpty(pFindString)) {
	    		mySQL = mySQL + 
	   				" AND (TO_CHAR(date_begin_pay_param_frmt) LIKE UPPER('%'||?||'%') OR " +
	   				"      UPPER(cashier_pay_oc_salary_frmt) LIKE UPPER('%'||?||'%') OR " +
	   				"      UPPER(cashier_first_card_not_pay) LIKE UPPER('%'||?||'%'))";
	    		for (int i=0; i<3; i++) {
	    		    pParam.add(new bcFeautureParam("string", pFindString));
	    		}
	    	}
	    	pParam.add(new bcFeautureParam("int", p_end));
	    	pParam.add(new bcFeautureParam("int", p_beg));
	        mySQL = mySQL +
	            "         ORDER BY date_begin_pay_param DESC) " +
	            "      WHERE ROWNUM < ? " + 
	            " ) WHERE rn >= ?";
        } else if ("PROMOTER".equalsIgnoreCase(lPromoterPos)) {
	        mySQL = 
	        	" SELECT rn, date_begin_pay_param_frmt, promoter_min_day_salary_frmt, " +
	        	"        promoter_cost_oc_less_frmt, promoter_cost_oc_over_frmt, promoter_plan_day_salary_frmt, " +
	        	"        id_lg_promoter_pay_param "+
	            "   FROM (SELECT ROWNUM rn, date_begin_pay_param_frmt, " +
	            "                DECODE(promoter_min_day_salary," +
	            "                       NULL, ''," +
	            "                       promoter_min_day_salary_frmt||' '||sname_currency" +
	            "                ) promoter_min_day_salary_frmt, " +
	            "                DECODE(promoter_cost_one_card_less," +
	            "                       NULL, ''," +
	            "                       promoter_cost_oc_less_frmt||' '||sname_currency" +
	            "                ) promoter_cost_oc_less_frmt, " +
	            "                DECODE(promoter_cost_one_card_over," +
	            "                       NULL, ''," +
	            "                       promoter_cost_oc_over_frmt||' '||sname_currency" +
	            "                ) promoter_cost_oc_over_frmt, " +
	            "                DECODE(promoter_plan_day_salary," +
	            "                       NULL, ''," +
	            "                       promoter_plan_day_salary_frmt||' '||sname_currency" +
	            "                ) promoter_plan_day_salary_frmt, " +
	        	"                id_lg_promoter_pay_param "+
	            "           FROM (SELECT * " +
	            "                   FROM " + getGeneralDBScheme() + ".vc_lg_promoter_pay_param_h_all" + 
	            "                  WHERE id_lg_promoter_work = ? ";
	        pParam.add(new bcFeautureParam("int", this.getValue("ID_LG_PROMOTER_WORK_CURRENT")));
	        
	    	if (!isEmpty(pFindString)) {
	    		mySQL = mySQL + 
	   				" AND (TO_CHAR(date_begin_pay_param_frmt) LIKE UPPER('%'||?||'%') OR " +
	   				"      UPPER(promoter_min_day_salary_frmt) LIKE UPPER('%'||?||'%') OR " +
	   				"      UPPER(promoter_cost_oc_less_frmt) LIKE UPPER('%'||?||'%') OR " +
	   				"      UPPER(promoter_cost_oc_over_frmt) LIKE UPPER('%'||?||'%') OR " +
	   				"      UPPER(promoter_plan_day_salary_frmt) LIKE UPPER('%'||?||'%'))";
	    		for (int i=0; i<5; i++) {
	    		    pParam.add(new bcFeautureParam("string", pFindString));
	    		}
	    	}
	    	pParam.add(new bcFeautureParam("int", p_end));
	    	pParam.add(new bcFeautureParam("int", p_beg));
	        mySQL = mySQL +
	            "         ORDER BY date_begin_pay_param DESC) " +
	            "      WHERE ROWNUM < ? " + 
	            " ) WHERE rn >= ?";
        } else if ("SUPERVISOR".equalsIgnoreCase(lPromoterPos)) {
	        mySQL = 
	        	" SELECT rn, date_begin_pay_param_frmt, " +
	        	"        supervisor_month_br_frmt, supervisor_cost_oc_overp_frmt, " +
	        	"        id_lg_promoter_pay_param "+
	            "   FROM (SELECT ROWNUM rn, date_begin_pay_param_frmt, " +
	            "                DECODE(supervisor_month_base_rate," +
	            "                       NULL, ''," +
	            "                       supervisor_month_br_frmt||' '||sname_currency" +
	            "                ) supervisor_month_br_frmt, " +
	            "                DECODE(supervisor_cost_one_card_overp," +
	            "                       NULL, ''," +
	            "                       supervisor_cost_oc_overp_frmt||' '||sname_currency" +
	            "                ) supervisor_cost_oc_overp_frmt, " +
	        	"                id_lg_promoter_pay_param "+
	            "           FROM (SELECT * " +
	            "                   FROM " + getGeneralDBScheme() + ".vc_lg_promoter_pay_param_h_all" + 
	            "                  WHERE id_lg_promoter_work = ? ";
	        pParam.add(new bcFeautureParam("int", this.getValue("ID_LG_PROMOTER_WORK_CURRENT")));
	        
	    	if (!isEmpty(pFindString)) {
	    		mySQL = mySQL + 
	   				" AND (TO_CHAR(date_begin_pay_param_frmt) LIKE UPPER('%'||?||'%') OR " +
	   				"      UPPER(supervisor_month_br_frmt) LIKE UPPER('%'||?||'%') OR " +
	   				"      UPPER(supervisor_cost_oc_overp_frmt) LIKE UPPER('%'||?||'%'))";
	    		for (int i=0; i<3; i++) {
	    		    pParam.add(new bcFeautureParam("string", pFindString));
	    		}
	    	}
	    	pParam.add(new bcFeautureParam("int", p_end));
	    	pParam.add(new bcFeautureParam("int", p_beg));
	        mySQL = mySQL +
	            "         ORDER BY date_begin_pay_param DESC) " +
	            "      WHERE ROWNUM < ? " + 
	            " ) WHERE rn >= ?";
        }
        
        boolean hasEditPermission = false;
        try{
        	if (isEditPermited("LOGISTIC_CLIENTS_PROMOTERS_INFO")>0) {
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
            for (int i=1; i <= colCount-1; i++) {
               html.append(getBottomFrameTableTH(logisticXML, mtd.getColumnName(i)));
            }
            if (hasEditPermission) {
            	html.append("<th width=\"10\">&nbsp;</th><th width=\"10\">&nbsp;</th>\n");
            }
            html.append("</tr></thead><tbody>\n");
            while (rset.next())
            {
            	html.append("<tr>");
          	  	for (int i=1; i <= colCount-1; i++) {
          	  		html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", "", ""));
          	  	}
                if (hasEditPermission) {
                  	String myHyperLink = "../crm/logistic/clients/promoterupdate.jsp?type=pay_param&id=" + this.idLGPromoter + "&id_pay_param=" + rset.getString("ID_LG_PROMOTER_PAY_PARAM");
                  	String myDeleteLink = "../crm/logistic/clients/promoterupdate.jsp?type=pay_param&id=" + this.idLGPromoter + "&id_pay_param=" + rset.getString("ID_LG_PROMOTER_PAY_PARAM") + "&action=remove&process=yes";
                	html.append(getDeleteButtonHTML(myDeleteLink, logisticXML.getfieldTransl("l_remove_pay_param_h", false), rset.getString("DATE_BEGIN_PAY_PARAM_FRMT")));
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
    } // getContactListHTML
	
    public String getPaymentCardHistoryHTML(String pFindString, String p_beg, String p_end) {
        StringBuilder html = new StringBuilder();
        PreparedStatement st = null;
        Connection con = null;

        ArrayList<bcFeautureParam> pParam = initParamArray();

        String mySQL = 
        	" SELECT rn, date_begin_pay_card_frmt, date_end_pay_card_frmt, " +
        	"        cd_card1 bon_card_for_payment, bon_pay_percent_frmt, " +
        	"        id_lg_promoter_pay_card, card_serial_number, card_id_issuer, card_id_payment_system "+
            "   FROM (SELECT ROWNUM rn, date_begin_pay_card_frmt, date_end_pay_card_frmt, cd_card1, bon_pay_percent_frmt, " +
            "                id_lg_promoter_pay_card, card_serial_number, card_id_issuer, card_id_payment_system "+
            "           FROM (SELECT * " +
            "                   FROM " + getGeneralDBScheme() + ".vc_lg_promoter_pay_card_h_all" + 
            "                  WHERE id_lg_promoter = ? ";
        pParam.add(new bcFeautureParam("int", this.idLGPromoter));
        
    	if (!isEmpty(pFindString)) {
    		mySQL = mySQL + 
   				" AND (TO_CHAR(date_begin_pay_card_frmt) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(date_end_pay_card_frmt) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(cd_card1) LIKE UPPER('%'||?||'%'))";
    		for (int i=0; i<3; i++) {
    		    pParam.add(new bcFeautureParam("string", pFindString));
    		}
    	}
    	pParam.add(new bcFeautureParam("int", p_end));
    	pParam.add(new bcFeautureParam("int", p_beg));
        mySQL = mySQL +
            "         ORDER BY date_begin_pay_card DESC) " +
            "      WHERE ROWNUM < ? " + 
            " ) WHERE rn >= ?";
        
        boolean hasBonCardPermission = false;
        boolean hasEditPermission = false;
        try{
        	if (isEditPermited("LOGISTIC_CLIENTS_PROMOTERS_INFO")>0) {
        		hasEditPermission = true;
        	}
        	if (isEditMenuPermited("CARDS_CLUBCARDS")>=0) {
        		hasBonCardPermission = true;
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
            if (hasEditPermission) {
            	html.append("<th width=\"10\">&nbsp;</th><th width=\"10\">&nbsp;</th>\n");
            }
            html.append("</tr></thead><tbody>\n");
            while (rset.next())
            {
            	html.append("<tr>");
          	  	for (int i=1; i <= colCount-4; i++) {
          	  		if (("BON_CARD_FOR_PAYMENT".equalsIgnoreCase(mtd.getColumnName(i))) && hasBonCardPermission && 
          	  				!isEmpty(rset.getString("CARD_SERIAL_NUMBER"))) {
          	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/cards/clubcardspecs.jsp?id=" + rset.getString("CARD_SERIAL_NUMBER") + "&iss=" + rset.getString("CARD_ID_ISSUER") + "&paysys=" + rset.getString("CARD_ID_PAYMENT_SYSTEM"), "", ""));
          	  		} else {
          	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", "", ""));
          	  		}
          	  	}
                if (hasEditPermission) {
                  	String myHyperLink = "../crm/logistic/clients/promoterupdate.jsp?type=pay_card&id=" + this.idLGPromoter + "&id_pay_card=" + rset.getString("ID_LG_PROMOTER_PAY_CARD");
                  	String myDeleteLink = "../crm/logistic/clients/promoterupdate.jsp?type=pay_card&id=" + this.idLGPromoter + "&id_pay_card=" + rset.getString("ID_LG_PROMOTER_PAY_CARD") + "&action=remove&process=yes";
                	html.append(getDeleteButtonHTML(myDeleteLink, logisticXML.getfieldTransl("l_remove_pay_card_h", false), rset.getString("DATE_BEGIN_PAY_CARD_FRMT")));
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
    } // getContactListHTML
	
    public String getSubordinatesHistoryHTML(String pFindString, String pPost, String pState, String p_beg, String p_end) {
        StringBuilder html = new StringBuilder();
        PreparedStatement st = null;
        Connection con = null;

        ArrayList<bcFeautureParam> pParam = initParamArray();

        String mySQL = 
        	" SELECT rn, cd_lg_promoter, name_lg_promoter, name_lg_promoter_post, " +
        	"        name_lg_promoter_state, phone_mobile, date_begin_set_frmt, " +
        	"        date_end_set_frmt, value_paym_for_oc_sale_frmt, " +
        	"        id_lg_promoter_manager, id_lg_promoter "+
            "   FROM (SELECT ROWNUM rn, cd_lg_promoter, name_lg_promoter, " +
            "                DECODE(cd_lg_promoter_post, " +
  			"                       'PROMOTER', '<font color=\"green\"><b>'||name_lg_promoter_post||'</b></font>', " +
  			"                       'SUPERVISOR', '<font color=\"red\"><b>'||name_lg_promoter_post||'</b></font>', " +
  			"                       'CHIEF', '<font color=\"blue\"><b>'||name_lg_promoter_post||'</b></font>', " +
  			"                       '<font color=\"black\"><b>'||name_lg_promoter_post||'</b></font>' " +
	  		"                ) name_lg_promoter_post, " +
	  		"                DECODE(cd_lg_promoter_state, " +
  			"                       'ACCEPTED', '<font color=\"green\"><b>'||name_lg_promoter_state||'</b></font>', " +
  			"                       'TRANSFERRED', '<font color=\"blue\"><b>'||name_lg_promoter_state||'</b></font>', " +
  			"                       'DISMISSED', '<font color=\"red\"><b>'||name_lg_promoter_state||'</b></font>', " +
  			"                       name_lg_promoter_state " +
	  		"                ) name_lg_promoter_state, " +
            "                phone_mobile, date_begin_set_frmt, " +
            "                date_end_set_frmt, " +
            "                DECODE(value_paym_for_one_card_sale," +
            "                       NULL, ''," +
            "                       value_paym_for_oc_sale_frmt||' '||sname_currency_payment" +
            "                ) value_paym_for_oc_sale_frmt," +
            "                id_lg_promoter_manager, id_lg_promoter "+
            "           FROM (SELECT * " +
            "                   FROM " + getGeneralDBScheme() + ".vc_lg_promoter_mngr2_h_all" + 
            "                  WHERE id_lg_manager = ? ";
        pParam.add(new bcFeautureParam("int", this.idLGPromoter));
        
    	if (!isEmpty(pFindString)) {
    		mySQL = mySQL + 
   				" AND (TO_CHAR(cd_lg_promoter) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(name_lg_promoter) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(phone_mobile) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(date_begin_set_frmt) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(date_end_set_frmt) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(value_paym_for_oc_sale_frmt) LIKE UPPER('%'||?||'%'))";
    		for (int i=0; i<6; i++) {
    		    pParam.add(new bcFeautureParam("string", pFindString));
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
            "         ORDER BY date_begin_set DESC) " +
            "      WHERE ROWNUM < ? " + 
            " ) WHERE rn >= ?";
        
        boolean hasEditPermission = false;
        try{
        	if (isEditPermited("LOGISTIC_CLIENTS_PROMOTERS_SOBORDINATES")>0) {
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
               html.append(getBottomFrameTableTH(logisticXML, mtd.getColumnName(i)));
            }
            if (hasEditPermission) {
            	html.append("<th width=\"10\">&nbsp;</th><th width=\"10\">&nbsp;</th>\n");
            }
            html.append("</tr></thead><tbody>\n");
            while (rset.next())
            {
            	html.append("<tr>");
          	  	for (int i=1; i <= colCount-2; i++) {
          	  		if (("NAME_LG_PROMOTER".equalsIgnoreCase(mtd.getColumnName(i)))) {
          	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/logistic/clients/promoterspecs.jsp?id=" + rset.getString("ID_LG_PROMOTER"), "", ""));
          	  		} else {
          	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", "", ""));
          	  		}
          	  	}
                if (hasEditPermission) {
                  	String myHyperLink = "../crm/logistic/clients/promoterupdate.jsp?type=subordinate&id=" + this.idLGPromoter + "&id_promoter_manager=" + rset.getString("ID_LG_PROMOTER_MANAGER");
                  	String myDeleteLink = "../crm/logistic/clients/promoterupdate.jsp?type=subordinate&id=" + this.idLGPromoter + "&id_promoter_manager=" + rset.getString("ID_LG_PROMOTER_MANAGER") + "&action=remove&process=yes";
                	html.append(getDeleteButtonHTML(myDeleteLink, logisticXML.getfieldTransl("l_remove_manager_h", false), rset.getString("DATE_BEGIN_SET_FRMT")));
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
    } // getContactListHTML
	
    public String getSupervisorDeliveryPointHistoryHTML(String pFindString, String p_beg, String p_end) {
        StringBuilder html = new StringBuilder();
        PreparedStatement st = null;
        Connection con = null;

        ArrayList<bcFeautureParam> pParam = initParamArray();

        String mySQL = 
        	" SELECT rn, sname_jur_prs jur_prs, name_service_place service_place, adr_full, id_club, " +
        	"        cd_club_service_place, date_begin_set_frmt, date_end_set_frmt,  " +
        	"        id_lg_promoter_sv_dp, id_lg_delivery_point, id_jur_prs "+
            "   FROM (SELECT ROWNUM rn, sname_jur_prs, name_service_place, adr_full, id_club, " +
            "                cd_club_service_place, date_begin_set_frmt, date_end_set_frmt, " +
            "                id_lg_promoter_sv_dp, id_lg_delivery_point, id_jur_prs "+
            "           FROM (SELECT * " +
            "                   FROM " + getGeneralDBScheme() + ".vc_lg_promoter_sv_dp_h_cl_all" + 
            "                  WHERE id_lg_supervisor_work = ? ";
        pParam.add(new bcFeautureParam("int", this.getValue("ID_LG_PROMOTER_WORK_CURRENT")));
        
    	if (!isEmpty(pFindString)) {
    		mySQL = mySQL + 
   				" AND (TO_CHAR(sname_jur_prs) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(name_service_place) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(adr_full) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(cd_club_service_place) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(date_begin_set_frmt) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(date_end_set_frmt) LIKE UPPER('%'||?||'%'))";
    		for (int i=0; i<6; i++) {
    		    pParam.add(new bcFeautureParam("string", pFindString));
    		}
    	}
    	pParam.add(new bcFeautureParam("int", p_end));
    	pParam.add(new bcFeautureParam("int", p_beg));
        mySQL = mySQL +
            "         ORDER BY date_begin_set DESC) " +
            "      WHERE ROWNUM < ? " + 
            " ) WHERE rn >= ?";
        
        boolean hasEditPermission = false;
        boolean hasClubPermission = false;
        boolean hasJurPrsPermission = false;
        boolean hasServicePlacePermission = false;
        try{
        	if (isEditPermited("LOGISTIC_CLIENTS_PROMOTERS_DELIVERY_POINTS")>0) {
        		hasEditPermission = true;
        	}
        	if (isEditMenuPermited("CLUB_CLUB")>=0) {
        		hasClubPermission = true;
        	}
        	if (isEditMenuPermited("CLIENTS_YURPERSONS")>=0) {
        		hasJurPrsPermission = true;
        	}
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
            for (int i=1; i <= colCount-3; i++) {
               html.append(getBottomFrameTableTH(logisticXML, mtd.getColumnName(i)));
            }
            if (hasEditPermission) {
            	html.append("<th width=\"10\">&nbsp;</th><th width=\"10\">&nbsp;</th>\n");
            }
            html.append("</tr></thead><tbody>\n");
            while (rset.next())
            {
            	html.append("<tr>");
          	  	for (int i=1; i <= colCount-3; i++) {
	          	  	if ("ID_CLUB".equalsIgnoreCase(mtd.getColumnName(i)) && hasClubPermission) {
	      	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/club/clubspecs.jsp?id=" + rset.getString("ID_CLUB"), "", ""));
	      	  		} else if ("JUR_PRS".equalsIgnoreCase(mtd.getColumnName(i)) && hasJurPrsPermission) {
	      	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/clients/yurpersonspecs.jsp?id=" + rset.getString("ID_JUR_PRS"), "", ""));
	      	  		} else if ("SERVICE_PLACE".equalsIgnoreCase(mtd.getColumnName(i)) && hasServicePlacePermission) {
	      	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/clients/service_placespecs.jsp?id=" + rset.getString("ID_LG_DELIVERY_POINT"), "", ""));
	      	  		} else {
          	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", "", ""));
          	  		}
          	  	}
                if (hasEditPermission) {
                  	String myHyperLink = "../crm/logistic/clients/promoterupdate.jsp?type=delivery_point&id=" + this.idLGPromoter + "&id_delivery_point=" + rset.getString("ID_LG_DELIVERY_POINT");
                  	String myDeleteLink = "../crm/logistic/clients/promoterupdate.jsp?type=delivery_point&id=" + this.idLGPromoter + "&id_delivery_point=" + rset.getString("ID_LG_DELIVERY_POINT") + "&action=remove&process=yes";
                	html.append(getDeleteButtonHTML(myDeleteLink, logisticXML.getfieldTransl("l_remove_delivery_point_h", false), rset.getString("DATE_BEGIN_SET_FRMT")));
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
    } // getContactListHTML
	
    public String getPromoterPenaltiesHTML(String pFindString, String p_beg, String p_end) {
        StringBuilder html = new StringBuilder();
        PreparedStatement st = null;
        Connection con = null;
        
        ArrayList<bcFeautureParam> pParam = initParamArray();

        String mySQL = 
	        	" SELECT rn, date_lg_promoter_penalty_frmt, value_lg_promoter_penalty_frmt, " +
	        	"        reason_lg_promoter_penalty, id_lg_promoter_penalty "+
	            "   FROM (SELECT ROWNUM rn, date_lg_promoter_penalty_frmt, " +
	            "                DECODE (value_lg_promoter_penalty," +
	            "                        NULL, ''," +
	            "                        value_lg_promoter_penalty_frmt||' '||sname_currency" +
	            "                ) value_lg_promoter_penalty_frmt, " +
	        	"                reason_lg_promoter_penalty, id_lg_promoter_penalty "+
	            "           FROM (SELECT * " +
	            "                   FROM " + getGeneralDBScheme() + ".vc_lg_promoter_penalty_all" + 
	            "                  WHERE id_lg_promoter_work = ? ";
        pParam.add(new bcFeautureParam("int", this.getValue("ID_LG_PROMOTER_WORK_CURRENT")));
        
	    if (!isEmpty(pFindString)) {
	    	mySQL = mySQL + 
	   			" AND (TO_CHAR(date_lg_promoter_penalty_frmt) LIKE UPPER('%'||?||'%') OR " +
	   			"      UPPER(value_lg_promoter_penalty_frmt) LIKE UPPER('%'||?||'%') OR " +
	   			"      UPPER(reason_lg_promoter_penalty) LIKE UPPER('%'||?||'%'))";
	    	for (int i=0; i<3; i++) {
	    	    pParam.add(new bcFeautureParam("string", pFindString));
	    	}
	    }
	    pParam.add(new bcFeautureParam("int", p_end));
	    pParam.add(new bcFeautureParam("int", p_beg));
	    mySQL = mySQL +
	        "         ORDER BY date_lg_promoter_penalty DESC) " +
	        "      WHERE ROWNUM < ? " + 
	        " ) WHERE rn >= ?";
        
        boolean hasEditPermission = false;
        try{
        	if (isEditPermited("LOGISTIC_CLIENTS_PROMOTERS_PENALTIES")>0) {
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
            for (int i=1; i <= colCount-1; i++) {
               html.append(getBottomFrameTableTH(logisticXML, mtd.getColumnName(i)));
            }
            if (hasEditPermission) {
            	html.append("<th width=\"10\">&nbsp;</th><th width=\"10\">&nbsp;</th>\n");
            }
            html.append("</tr></thead><tbody>\n");
            while (rset.next())
            {
            	html.append("<tr>");
          	  	for (int i=1; i <= colCount-1; i++) {
          	  		html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", "", ""));
          	  	}
                if (hasEditPermission) {
                  	String myHyperLink = "../crm/logistic/clients/promoterupdate.jsp?type=penalty&id=" + this.idLGPromoter + "&id_penalty=" + rset.getString("ID_LG_PROMOTER_PENALTY");
                  	String myDeleteLink = "../crm/logistic/clients/promoterupdate.jsp?type=penalty&id=" + this.idLGPromoter + "&id_penalty=" + rset.getString("ID_LG_PROMOTER_PENALTY") + "&action=remove&process=yes";
                	html.append(getDeleteButtonHTML(myDeleteLink, logisticXML.getfieldTransl("l_remove_supervisor_h", false), rset.getString("DATE_LG_PROMOTER_PENALTY_FRMT")));
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
    } // getContactListHTML
	
    public String getPromoterPaymentsHTML(String pFindString, String p_beg, String p_end) {
        StringBuilder html = new StringBuilder();
        PreparedStatement st = null;
        Connection con = null;
        
        ArrayList<bcFeautureParam> pParam = initParamArray();

        String mySQL = 
	        	" SELECT rn, date_lg_promoter_penalty_frmt, value_lg_promoter_penalty_frmt, " +
	        	"        reason_lg_promoter_penalty, id_lg_promoter_penalty "+
	            "   FROM (SELECT ROWNUM rn, date_lg_promoter_penalty_frmt, " +
	            "                DECODE (value_lg_promoter_penalty," +
	            "                        NULL, ''," +
	            "                        value_lg_promoter_penalty_frmt||' '||sname_currency" +
	            "                ) value_lg_promoter_penalty_frmt, " +
	        	"                reason_lg_promoter_penalty, id_lg_promoter_penalty "+
	            "           FROM (SELECT * " +
	            "                   FROM " + getGeneralDBScheme() + ".vc_lg_promoter_penalty_all" + 
	            "                  WHERE id_lg_promoter_work = ? ";
        pParam.add(new bcFeautureParam("int", this.getValue("ID_LG_PROMOTER_WORK_CURRENT")));
        
	    if (!isEmpty(pFindString)) {
	    	mySQL = mySQL + 
	   			" AND (TO_CHAR(date_lg_promoter_penalty_frmt) LIKE UPPER('%'||?||'%') OR " +
	   			"      UPPER(value_lg_promoter_penalty_frmt) LIKE UPPER('%'||?||'%') OR " +
	   			"      UPPER(reason_lg_promoter_penalty) LIKE UPPER('%'||?||'%'))";
	    	for (int i=0; i<3; i++) {
	    	    pParam.add(new bcFeautureParam("string", pFindString));
	    	}
	    }
	    pParam.add(new bcFeautureParam("int", p_end));
	    pParam.add(new bcFeautureParam("int", p_beg));
	    mySQL = mySQL +
	        "         ORDER BY date_lg_promoter_penalty DESC) " +
	        "      WHERE ROWNUM < ? " + 
	        " ) WHERE rn >= ?";
        
        boolean hasEditPermission = false;
        try{
        	if (isEditPermited("LOGISTIC_CLIENTS_PROMOTERS_PAYMENTS")>0) {
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
            for (int i=1; i <= colCount-1; i++) {
               html.append(getBottomFrameTableTH(logisticXML, mtd.getColumnName(i)));
            }
            if (hasEditPermission) {
            	html.append("<th width=\"10\">&nbsp;</th><th width=\"10\">&nbsp;</th>\n");
            }
            html.append("</tr></thead><tbody>\n");
            while (rset.next())
            {
            	html.append("<tr>");
          	  	for (int i=1; i <= colCount-1; i++) {
          	  		html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", "", ""));
          	  	}
                if (hasEditPermission) {
                  	String myHyperLink = "../crm/logistic/clients/promoterupdate.jsp?type=penalty&id=" + this.idLGPromoter + "&id_penalty=" + rset.getString("ID_LG_PROMOTER_PENALTY");
                  	String myDeleteLink = "../crm/logistic/clients/promoterupdate.jsp?type=penalty&id=" + this.idLGPromoter + "&id_penalty=" + rset.getString("ID_LG_PROMOTER_PENALTY") + "&action=remove&process=yes";
                	html.append(getDeleteButtonHTML(myDeleteLink, logisticXML.getfieldTransl("l_remove_supervisor_h", false), rset.getString("DATE_LG_PROMOTER_PENALTY_FRMT")));
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
    } // getContactListHTML
}
