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

public class bcLGClientsUnitsObject extends bcObject {
	private final static Logger LOGGER=Logger.getLogger(bcLGClientsUnitsObject.class);
	
	private Map<String,Comparable<String>> fieldHm = new HashMap<String, Comparable<String>>();
	
	private String idLGUnit;
	
	public bcLGClientsUnitsObject(String pIdLGUnit) {
		this.idLGUnit = pIdLGUnit;
		getFeature();
	}
	
	private void getFeature() {
		String mySQL = " SELECT * FROM " + getGeneralDBScheme() + ".VC_LG_UNITS_CLUB_ALL WHERE id_lg_unit = ?";
		fieldHm = getFeatures2(mySQL, this.idLGUnit, false);
	}
	
	public String getValue(String pColumnName) {
		return getFeautureResult(fieldHm, pColumnName);
	}
	
    public String getServicePlacesHTML(String isInclude, String p_beg, String p_end) {
    	StringBuilder html_form = new StringBuilder();
        StringBuilder html_body = new StringBuilder();
        StringBuilder html_apply = new StringBuilder();
        Connection con = null;
        PreparedStatement st = null;

        ArrayList<bcFeautureParam> pParam = initParamArray();

        String mySQL = 
        	" SELECT rn, jur_prs, service_place, adr_full, date_include_frmt," +
        	"        include_to_unit, include_to_unit_tsl, id_jur_prs, id_service_place, id_lg_unit_service_place "+
            "   FROM (SELECT ROWNUM rn, sname_jur_prs jur_prs, name_service_place service_place, adr_full, date_include_frmt, " +
            "                include_to_unit, include_to_unit_tsl, id_jur_prs, id_service_place, id_lg_unit_service_place "+
            "           FROM (SELECT * " +
            "                   FROM " + getGeneralDBScheme() + ".vc_lg_unit_serv_plc_incl_all "+
            "                  WHERE id_lg_unit = ? ";
        pParam.add(new bcFeautureParam("int", this.idLGUnit));
        if (!isEmpty(isInclude)) {
        	mySQL = mySQL + " AND include_to_unit = ? ";
        	pParam.add(new bcFeautureParam("string", isInclude));
        }
        pParam.add(new bcFeautureParam("int", p_end));
        pParam.add(new bcFeautureParam("int", p_beg));
        mySQL = mySQL + 
            "                  ORDER BY DECODE(include_to_unit, 'Y', 1, 2), name_service_place) " +
            "          WHERE ROWNUM < ? " + 
            " ) WHERE rn >= ?";
        
        boolean hasJurPrsPermission = false;
        boolean hasServicePlacePermission = false;
        boolean hasEditPermission = false;

        String myBGColor = "";
        
        try{
        	if (isEditMenuPermited("CLIENTS_YURPERSONS")>=0) {
        		hasJurPrsPermission = true;
        	}
        	if (isEditMenuPermited("CLIENTS_SERVICE_PLACE")>=0) {
        		hasServicePlacePermission = true;
        	}
        	if (isEditPermited("LOGISTIC_CLIENTS_UNITS_SERVICE_PLACES")>0) {
        		hasEditPermission = true;
        	}
        	
        	LOGGER.debug(prepareSQLToLog(mySQL, pParam));
        	con = Connector.getConnection(getSessionId());
        	st = con.prepareStatement(mySQL);
        	st = prepareParam(st, pParam);
        	ResultSet rset = st.executeQuery();
            ResultSetMetaData mtd = rset.getMetaData();
            int colCount = mtd.getColumnCount();
            
            int rowCount = 0;

            if (hasEditPermission) {
            	html_form.append("<form method=\"post\" name=\"updateForm\" id=\"updateForm\" action=\"../crm/logistic/clients/unitupdate.jsp\">\n");
            	html_form.append("<input type=\"hidden\" name=\"type\" value=\"set_service_place\">\n");
            	html_form.append("<input type=\"hidden\" name=\"action\" value=\"set\">\n");
            	html_form.append("<input type=\"hidden\" name=\"process\" value=\"yes\">\n");
            	html_form.append("<input type=\"hidden\" name=\"id\" value=\"" + this.idLGUnit + "\">\n");
            }
            
            html_form.append(getBottomFrameTable());
            html_form.append("<tr>");
            for (int i=1; i <= colCount-5; i++) {
            	html_form.append(getBottomFrameTableTH(logisticXML, mtd.getColumnName(i)));
            }
            if (hasEditPermission) {
            	html_form.append("<th> "+ 
           			"<input id=\"mainCheck\" name=\"mainCheck\" type=\"checkbox\" onclick=\"CheckAll(this)\"></th>\n");
            } else {
            	html_form.append("<th> "+ logisticXML.getfieldTransl("include_to_unit", false) + "</th>\n");
            }
            html_form.append("</tr></thead><tbody>");
            if (hasEditPermission) {
            	html_apply.append("<tr>");
            	html_apply.append("<td colspan=\"6\" align=\"center\">");
            	html_apply.append(getSubmitButtonAjax("../crm/logistic/clients/unitupdate.jsp"));
            	html_apply.append("</td>");
            	html_apply.append("</tr>");
             }
            while (rset.next())
            {
            	rowCount = rowCount + 1;
            	String id = rset.getString("ID_SERVICE_PLACE");
                String tprvCheck="prv_"+id;
                String tCheck="chb_"+id;
                
                String myChecked = "";
                
                if ("Y".equalsIgnoreCase(rset.getString("INCLUDE_TO_UNIT"))){
                	myBGColor = selectedBackGroundStyle;
                	myChecked = "checked";
                } else {
                	myBGColor = "";
                	myChecked = "";
                }
                html_body.append("<tr>");
          	  	for (int i=1; i <= colCount-5; i++) {
          	  		
          	  		if ("JUR_PRS".equalsIgnoreCase(mtd.getColumnName(i)) && hasJurPrsPermission) {
          	  			html_body.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/clients/yurpersonspecs.jsp?id=" + rset.getString("ID_JUR_PRS"), "", myBGColor));
          	  		} else if ("SERVICE_PLACE".equalsIgnoreCase(mtd.getColumnName(i)) && hasServicePlacePermission) {
          	  			html_body.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/clients/service_placespecs.jsp?id=" + rset.getString("ID_SERVICE_PLACE"), "", myBGColor));
          	  		} else {
          	  			html_body.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", "", myBGColor));
          	  		}
          	  	}
      	  		if (hasEditPermission) {
      	  			html_body.append("<td " + myBGColor + "><INPUT  type=\"checkbox\" value = \"Y\" name="+tCheck+" id="+tCheck+" "+myChecked+" onclick=\"return CheckCB(this);\">");
      	  			if ("Y".equalsIgnoreCase(rset.getString("INCLUDE_TO_UNIT"))){
      	  				html_body.append("<INPUT type=hidden  value=\"Y\" name="+tprvCheck+">"); 
      	  			}
      	  			html_body.append("</td>\n");
      	  		} else {
      	  			html_body.append(getBottomFrameTableTD("INCLUDE_TO_UNIT_TSL", rset.getString("INCLUDE_TO_UNIT_TSL"), "", "", myBGColor));
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
	
    public String getPromoterssHTML(String isInclude, String p_beg, String p_end) {
    	StringBuilder html_form = new StringBuilder();
        StringBuilder html_body = new StringBuilder();
        StringBuilder html_apply = new StringBuilder();
        Connection con = null;
        PreparedStatement st = null;

        ArrayList<bcFeautureParam> pParam = initParamArray();

        String mySQL = 
        	" SELECT rn, name_lg_promoter, jur_prs, service_place, phone_mobile, name_lg_promoter_post," +
            "        date_begin_work_frmt, date_end_work_frmt, name_lg_promoter_state, date_include_frmt, " +
            "        include_to_unit, include_to_unit_tsl, id_jur_prs, id_service_place, id_lg_promoter "+
            "   FROM (SELECT ROWNUM rn, name_lg_promoter, sname_jur_prs jur_prs, name_service_place service_place, " +
            "                phone_mobile, name_lg_promoter_post," +
            "                date_begin_work_frmt, date_end_work_frmt, name_lg_promoter_state, date_include_frmt, " +
            "                include_to_unit, include_to_unit_tsl, id_jur_prs, id_service_place, id_lg_promoter "+
            "           FROM (SELECT * " +
            "                   FROM " + getGeneralDBScheme() + ".vc_lg_unit_promoters_incl_all "+
            "                  WHERE id_lg_unit = ? ";
        pParam.add(new bcFeautureParam("int", this.idLGUnit));
        
        if (!isEmpty(isInclude)) {
        	mySQL = mySQL + " AND include_to_unit = ? ";
        	pParam.add(new bcFeautureParam("string", isInclude));
        }
        pParam.add(new bcFeautureParam("int", p_end));
        pParam.add(new bcFeautureParam("int", p_beg));
        mySQL = mySQL + 
            "                  ORDER BY DECODE(include_to_unit, 'Y', 1, 2), name_lg_promoter) " +
            "          WHERE ROWNUM < ? " + 
            " ) WHERE rn >= ?";
        
        boolean hasLGPromoterPermission = false;
        boolean hasJurPrsPermission = false;
        boolean hasServicePlacePermission = false;
        boolean hasEditPermission = false;

        String myBGColor = "";
        
        try{
        	if (isEditMenuPermited("LOGISTIC_CLIENTS_PROMOTERS")>=0) {
        		hasLGPromoterPermission = true;
        	}
        	if (isEditMenuPermited("CLIENTS_YURPERSONS")>=0) {
        		hasJurPrsPermission = true;
        	}
        	if (isEditMenuPermited("CLIENTS_SERVICE_PLACE")>=0) {
        		hasServicePlacePermission = true;
        	}
        	if (isEditPermited("LOGISTIC_CLIENTS_UNITS_PROMOTERS")>0) {
        		hasEditPermission = true;
        	}
        	
        	LOGGER.debug(prepareSQLToLog(mySQL, pParam));
        	con = Connector.getConnection(getSessionId());
        	st = con.prepareStatement(mySQL);
        	st = prepareParam(st, pParam);
        	ResultSet rset = st.executeQuery();
            ResultSetMetaData mtd = rset.getMetaData();
            int colCount = mtd.getColumnCount();
            
            int rowCount = 0;

            if (hasEditPermission) {
            	html_form.append("<form method=\"post\" name=\"updateForm\" id=\"updateForm\" action=\"../crm/logistic/clients/unitupdate.jsp\">\n");
            	html_form.append("<input type=\"hidden\" name=\"type\" value=\"set_promoter\">\n");
            	html_form.append("<input type=\"hidden\" name=\"action\" value=\"set\">\n");
            	html_form.append("<input type=\"hidden\" name=\"process\" value=\"yes\">\n");
            	html_form.append("<input type=\"hidden\" name=\"id\" value=\"" + this.idLGUnit + "\">\n");
            }
            
            html_form.append(getBottomFrameTable());
            html_form.append("<tr>");
            for (int i=1; i <= colCount-5; i++) {
            	html_form.append(getBottomFrameTableTH(logisticXML, mtd.getColumnName(i)));
            }
            if (hasEditPermission) {
            	html_form.append("<th> "+ 
       				"<input id=\"mainCheck\" name=\"mainCheck\" type=\"checkbox\" onclick=\"CheckAll(this)\"></th>\n");
            } else {
            	html_form.append("<th> "+ logisticXML.getfieldTransl("include_to_unit", false) + "</th>\n");
            }
            html_form.append("</tr></thead><tbody>");
            if (hasEditPermission) {
            	html_apply.append("<tr>");
            	html_apply.append("<td colspan=\"11\" align=\"center\">");
            	html_apply.append(getSubmitButtonAjax("../crm/logistic/clients/unitupdate.jsp"));
            	html_apply.append("</td>");
            	html_apply.append("</tr>");
             }
            while (rset.next())
            {
            	rowCount = rowCount + 1;
            	String id = rset.getString("ID_LG_PROMOTER");
                String tprvCheck="prv_"+id;
                String tCheck="chb_"+id;
                
                String myChecked = "";
                
                if ("Y".equalsIgnoreCase(rset.getString("INCLUDE_TO_UNIT"))){
                	myBGColor = selectedBackGroundStyle;
                	myChecked = "checked";
                } else {
                	myBGColor = "";
                	myChecked = "";
                }
                html_body.append("<tr>");
          	  	for (int i=1; i <= colCount-5; i++) {
          	  		
          	  		if ("JUR_PRS".equalsIgnoreCase(mtd.getColumnName(i)) && hasJurPrsPermission) {
          	  			html_body.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/clients/yurpersonspecs.jsp?id=" + rset.getString("ID_JUR_PRS"), "", myBGColor));
          	  		} else if ("SERVICE_PLACE".equalsIgnoreCase(mtd.getColumnName(i)) && hasServicePlacePermission) {
          	  			html_body.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/clients/service_placespecs.jsp?id=" + rset.getString("ID_SERVICE_PLACE"), "", myBGColor));
          	  		} else if ("NAME_LG_PROMOTER".equalsIgnoreCase(mtd.getColumnName(i)) && hasLGPromoterPermission) {
          	  			html_body.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/logistic/clients/promoterspecs.jsp?id=" + rset.getString("ID_LG_PROMOTER"), "", myBGColor));
          	  		} else {
          	  			html_body.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", "", myBGColor));
          	  		}
          	  	}
      	  		if (hasEditPermission) {
      	  			html_body.append("<td " + myBGColor + "><INPUT  type=\"checkbox\" value = \"Y\" name="+tCheck+" id="+tCheck+" "+myChecked+" onclick=\"return CheckCB(this);\">");
      	  			if ("Y".equalsIgnoreCase(rset.getString("INCLUDE_TO_UNIT"))){
      	  				html_body.append("<INPUT type=hidden  value=\"Y\" name="+tprvCheck+">"); 
      	  			}
      	  			html_body.append("</td>\n");
      	  		} else {
      	  			html_body.append(getBottomFrameTableTD("INCLUDE_TO_UNIT_TSL", rset.getString("INCLUDE_TO_UNIT_TSL"), "", "", myBGColor));
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
	
    public String getScheduleHTML(String p_beg, String p_end) {
        StringBuilder html = new StringBuilder();
        Connection con = null;
        PreparedStatement st = null;
        String mySQL = 
        	" SELECT rn, id_lg_unit_schedule, date_lg_unit_schedule_frmt,  " +
        	"        service_places_count, promoters_count, sales_cards_count "+
            "   FROM (SELECT ROWNUM rn, id_lg_unit_schedule, date_lg_unit_schedule_frmt, " +
            "                service_places_count, promoters_count, sales_cards_count "+
            "           FROM (SELECT * " +
            "                   FROM " + getGeneralDBScheme() + ".vc_lg_unit_schedule_all "+
            "                  WHERE id_lg_unit = ? " + 
            "                  ORDER BY date_lg_unit_schedule DESC) " +
            "          WHERE ROWNUM < ? " +
            " ) WHERE rn >= ?";
        
        boolean hasSchedulePermission = false;
        
        try{
        	if (isEditMenuPermited("LOGISTIC_CLIENTS_OPERATION_SCHEDULE")>=0) {
        		hasSchedulePermission = true;
        	}
        	
        	LOGGER.debug(mySQL + 
            		", 1={" + this.idLGUnit + ",int}" + 
            		", 2={" + p_end + ",int}" + 
            		", 3={" + p_beg + ",int}");
        	con = Connector.getConnection(getSessionId());
        	st = con.prepareStatement(mySQL);
        	st.setInt(1, Integer.parseInt(this.idLGUnit));
        	st.setInt(2, Integer.parseInt(p_end));
        	st.setInt(3, Integer.parseInt(p_beg));
        	ResultSet rset = st.executeQuery();
            ResultSetMetaData mtd = rset.getMetaData();
            int colCount = mtd.getColumnCount();
            
            html.append(getBottomFrameTable());
            html.append("<tr>");
            for (int i=1; i <= colCount; i++) {
               html.append(getBottomFrameTableTH(logisticXML, mtd.getColumnName(i)));
            }
            html.append("</tr></thead><tbody>");
            while (rset.next())
            {
            	html.append("<tr>");
          	  	for (int i=1; i <= colCount; i++) {
          	  		if ("ID_LG_UNIT_SCHEDULE".equalsIgnoreCase(mtd.getColumnName(i)) && hasSchedulePermission) {
          	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/logistic/clients/operation_schedulespecs.jsp?id=" + rset.getString("ID_LG_UNIT_SCHEDULE"), "", ""));
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
}
