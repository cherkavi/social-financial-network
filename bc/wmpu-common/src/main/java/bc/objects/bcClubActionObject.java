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
import bc.lists.bcListCardOperation;
import bc.service.bcFeautureParam;


public class bcClubActionObject extends bcObject {
	private final static Logger LOGGER=Logger.getLogger(bcClubActionObject.class);
	
    private Map<String,Comparable<String>> fieldHm = new HashMap<String, Comparable<String>>();

    private String idAction;
	
	public bcClubActionObject(String pIdClubAction) {
		this.idAction = pIdClubAction;
		getFeature();
	}
	
	private void getFeature() {
		String featureSelect = "SELECT * FROM " + getGeneralDBScheme() + ".VC_CLUB_EVENT_CLUB_ALL WHERE id_club_event = ?";
		fieldHm = getFeatures2(featureSelect, this.idAction, false);
	}
	
	public String getValue(String pColumnName) {
		return getFeautureResult(fieldHm, pColumnName);
	}
	
	public String getClubActionGivenScheduleHTML(String pFind, String pGivenDay, String p_beg, String p_end) {
		ArrayList<bcFeautureParam> pParam = initParamArray();

		String mySQL = 
	      	" SELECT id_club_event_given_schedule, name_given_day, given_period, " +
	     	"        name_gifts_given_place, adr_gifts_given_place, id_gifts_given_place " +
	        "   FROM (SELECT ROWNUM rn, id_club_event_given_schedule, name_given_day, given_period, " +
	        "                name_gifts_given_place, adr_gifts_given_place, id_gifts_given_place " +
	        "           FROM (SELECT * " +
	        "                   FROM " + getGeneralDBScheme() + ".vc_club_event_given_sh_cl_all " +
	        "                  WHERE id_club_event = ? ";
		pParam.add(new bcFeautureParam("int", this.idAction));
		
	    if (!isEmpty(pFind)) {
	    	mySQL = mySQL + 
				" AND (TO_CHAR(id_club_event_given_schedule) LIKE UPPER('%'||?||'%') OR " +
				"	   UPPER(given_period) LIKE UPPER('%'||?||'%') OR " +
				"	   UPPER(name_gifts_given_place) LIKE UPPER('%'||?||'%') OR " +
				"	   UPPER(adr_gifts_given_place) LIKE UPPER('%'||?||'%')) ";
	    	for (int i=0; i<4; i++) {
	    	    pParam.add(new bcFeautureParam("string", pFind));
	    	}
	    }
        if (!isEmpty(pGivenDay)) {
          	mySQL = mySQL + "  AND cd_given_day = ? ";
          	pParam.add(new bcFeautureParam("string", pGivenDay));
        }

        pParam.add(new bcFeautureParam("int", p_end));
        pParam.add(new bcFeautureParam("int", p_beg));
	    mySQL = mySQL +
	       	"                  ORDER BY number_given_day, given_period) " +
	      	"         WHERE ROWNUM < ? " + 
	      	" ) WHERE rn >= ?";
	    StringBuilder html = new StringBuilder();
	    PreparedStatement st = null;
	    Connection con = null;
	    boolean hasEditPermission = false; 
	    boolean hasGiftGivenPlacePermission = false;
	    try{
	    	  if (isEditPermited("CLUB_EVENT_EVENT_GIVEN_SCHEDULE")>0) {
	    		  hasEditPermission = true;
	    	  }
	    	  if (isEditMenuPermited("CLIENTS_YURPERSONS")>=0) {
	    		  hasGiftGivenPlacePermission = true;
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
	          if (hasEditPermission) {
	          	  html.append("<th>&nbsp;</th><th>&nbsp;</th>\n");  
	          }
	          html.append("</tr></thead><tbody>\n");
	          
	          while (rset.next())
	          {
	        	  html.append("<tr>");
	          	  for (int i=1; i <= colCount-1; i++) {
	          		  if ("NAME_GIFTS_GIVEN_PLACE".equalsIgnoreCase(mtd.getColumnName(i)) && hasGiftGivenPlacePermission) {
	          			  html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/clients/service_placespecs.jsp?id="+rset.getString("ID_GIFTS_GIVEN_PLACE"), "", ""));
	          		  } else {
	          			  html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", "", ""));
	          		  }
	          	  }
	              if (hasEditPermission) {
	            	  String myHyperLink = "../crm/club_event/clubeventupdate.jsp?type=given_schedule&id="+this.idAction+"&id_club_event_given_schedule="+rset.getString("ID_CLUB_EVENT_GIVEN_SCHEDULE");
	            	  String myDeleteLink = "../crm/club_event/clubeventupdate.jsp?type=given_schedule&id="+this.idAction+"&id_club_event_given_schedule="+rset.getString("ID_CLUB_EVENT_GIVEN_SCHEDULE")+"&action=remove&process=yes";
	            	  html.append(getDeleteButtonHTML(myDeleteLink, club_actionXML.getfieldTransl("h_delete_club_event_given_schedule", false), rset.getString("NAME_GIFTS_GIVEN_PLACE")));
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
	  } // getClubActionGiftsHTML
	
	public String getClubActionRequestsHTML(String pType, String pState, String pFind, String p_beg, String p_end) {
		ArrayList<bcFeautureParam> pParam = initParamArray();

		String mySQL = 
	        "SELECT rn, id_nat_prs_gift_request, name_nat_prs_gift_request_type, nm_nat_prs_gift_request_state, " +
	        "       date_accept_frmt, name_nat_prs, phone_contact, " +
	      	"       text_request, id_nat_prs " +
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
	       	"               text_request, date_accept_frmt, id_nat_prs " +
	       	"		   FROM (SELECT * " +
	       	"                  FROM " + getGeneralDBScheme() + ".vc_nat_prs_gift_request_cl_all" +
	        "                 WHERE id_club_event = ? ";
		pParam.add(new bcFeautureParam("int", this.idAction));
		
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
        		  "       UPPER(date_accept_frmt) LIKE UPPER('%'||?||'%')) ";
           	for (int i=0; i<5; i++) {
           	    pParam.add(new bcFeautureParam("string", pFind));
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
	      boolean hasEditPermission = false; 
	      boolean hasRequestPermission = false;
	      boolean hagNatPrsPermission = false;
	      try{
	    	  if (isEditPermited("CLUB_EVENT_EVENT_REQUESTS")>0) {
	    		  hasEditPermission = true;
	    	  }
	    	  if (isEditMenuPermited("CLUB_EVENT_REQUEST")>=0) {
	    		  hasRequestPermission = true;
	    	  }
	    	  if (isEditMenuPermited("CLIENTS_NATPERSONS")>=0) {
	    		  hagNatPrsPermission = true;
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
	          if (hasEditPermission) {
	          	  html.append("<th>&nbsp;</th>\n");  
	          }
	          html.append("</tr></thead><tbody>\n");
	          
	          while (rset.next())
	          {
	        	  html.append("<tr>");
	          	  for (int i=1; i <= colCount-1; i++) {
	          		  if ("ID_NAT_PRS_GIFT_REQUEST".equalsIgnoreCase(mtd.getColumnName(i)) && hasRequestPermission) {
	          			  html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/club_event/requestspecs.jsp?id="+rset.getString("ID_NAT_PRS_GIFT_REQUEST"), "", ""));
	          		  } else if ("NAME_NAT_PRS".equalsIgnoreCase(mtd.getColumnName(i)) && hagNatPrsPermission) {
	          			  html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/clients/natpersonspecs.jsp?id="+rset.getString("ID_NAT_PRS"), "", ""));
	          		  } else {
	          			  html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", "", ""));
	          		  }
	          	  }
	              if (hasEditPermission) {
	            	  String myDeleteLink = "../crm/club_event/clubeventupdate.jsp?type=request&id="+this.idAction+"&id_nat_prs_gift_request="+rset.getString("ID_NAT_PRS_GIFT_REQUEST")+"&action=remove&process=yes";
	            	  html.append(getDeleteButtonHTML(myDeleteLink, club_actionXML.getfieldTransl("h_delete_nat_prs_gift_request", false), rset.getString("ID_NAT_PRS_GIFT_REQUEST")));
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
	
	public String getClubActionGiftsHTML(String pFind, String p_beg, String p_end, String pOrderField, String pOrderType) {
		ArrayList<bcFeautureParam> pParam = initParamArray();

		String mySQL = 
	      	" SELECT cd_club_event_gift, name_club_event_gift, " +
	      	"        count_gift_all_frmt, " +
	     	"        count_gift_reserved_frmt, count_gift_given_frmt, " +
	      	"        count_gift_remain_frmt, " +
	      	"        cd_given_event_type, is_active_tsl, id_club_event_gift, id_gift, " +
	      	"        ge_write_off_type, ge_add_good_type " +
	        "   FROM (SELECT ROWNUM rn, " +
	        "                cd_club_event_gift, name_club_event_gift, " +
	        " 		         DECODE(NVL(count_gift_all,0)," +
	        "                       0, TO_CHAR(count_gift_all)," +
	        "                       '<font color=\"green\"><b>'||TO_CHAR(count_gift_all)||'</b></font>'" +
	        "                ) count_gift_all_frmt, " +
	        " 		         DECODE(NVL(count_gift_reserved,0)," +
	        "                       0, TO_CHAR(count_gift_reserved)," +
	        "                       '<font color=\"blue\"><b>'||TO_CHAR(count_gift_reserved)||'</b></font>'" +
	        "                ) count_gift_reserved_frmt, " +
	        " 		         DECODE(NVL(count_gift_given,0)," +
	        "                       0, TO_CHAR(count_gift_given)," +
	        "                       '<font color=\"blue\"><b>'||TO_CHAR(count_gift_given)||'</b></font>'" +
	        "                ) count_gift_given_frmt, " +
	        " 		         DECODE(NVL(count_gift_remain,0)," +
	        "                       0, TO_CHAR(count_gift_remain)," +
	        "                       '<font color=\"red\"><b>'||TO_CHAR(count_gift_remain)||'</b></font>'" +
	        "                ) count_gift_remain_frmt, " +
	        "                cd_given_event_type, " +
	        " 		         DECODE(is_active," +
	        "                       'Y', is_active_tsl," +
	        "                       'N', '<font color=\"red\"><b>'||is_active_tsl||'</b></font>'," +
	        "                       is_active_tsl" +
	        "                ) is_active_tsl, " +
	        "                id_club_event_gift, id_gift, ge_write_off_type, ge_add_good_type " +
	        "           FROM (SELECT cd_club_event_gift, name_club_event_gift, " +
	        "                        count_gift_all, count_gift_reserved, count_gift_given, count_gift_remain, " +
	    	"        				 cd_given_event_type, ge_write_off_type, ge_add_good_type, " +
	     	"                        is_active_tsl, is_active, id_club_event_gift, id_gift" +
	        "                   FROM " + getGeneralDBScheme() + ".vc_club_event_gifts_all " +
	        "                  WHERE id_club_event = ? ";
		pParam.add(new bcFeautureParam("int", this.idAction));
	    
		if (!isEmpty(pFind)) {
			mySQL = mySQL + 
					" AND (UPPER(cd_gift) LIKE UPPER('%'||?||'%') OR " +
					"	   UPPER(name_gift) LIKE UPPER('%'||?||'%')) ";
			for (int i=0; i<2; i++) {
			    pParam.add(new bcFeautureParam("string", pFind));
			}
		}
		if (!isEmpty(pOrderField)) {
			mySQL = mySQL +
	          	" ORDER BY " + pOrderField;
			if (!isEmpty(pOrderType)) {
		    	  mySQL = mySQL + " " + pOrderType;
			}
		}
		pParam.add(new bcFeautureParam("int", p_end));
		pParam.add(new bcFeautureParam("int", p_beg));
	    mySQL = mySQL +
	      		"					) " +
	          	"         WHERE ROWNUM < ?" + 
	        	" ) WHERE rn >= ?";
	    
	    StringBuilder html = new StringBuilder();
	    PreparedStatement st = null;
	    Connection con = null;
	    boolean hasEditPermission = false; 
	    boolean hasGiftPermission = false;
	    String eventName = "";
	    
	    try{
	    	  if (isEditPermited("CLUB_EVENT_EVENT_GIFTS")>0) {
	    		  hasEditPermission = true;
	    	  }
	    	  if (isEditMenuPermited("CLUB_EVENT_GIFTS")>=0) {
	    		  hasGiftPermission = true;
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
	        	  html.append(getBottomFrameTableExtendedTH(
	        			  club_actionXML,
	        			  i,
	        			  mtd.getColumnName(i), 
	        			  pOrderField,
	        			  pOrderType,
	        			  "../crm/club_event/clubeventspecs.jsp?id="+this.idAction));
	          }
	          if (hasEditPermission) {
	          	  html.append("<th>&nbsp;</th><th>&nbsp;</th>\n");  
	          }
	          html.append("</tr></thead><tbody>\n");
	          
	          while (rset.next())
	          {
	        	  html.append("<tr>");
	          	  for (int i=1; i <= colCount-4; i++) {
	          		  if ("NAME_CLUB_EVENT_GIFT".equalsIgnoreCase(mtd.getColumnName(i)) && hasGiftPermission) {
	          			  html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/club_event/giftspecs.jsp?id="+rset.getString("ID_GIFT"), "", ""));
	          		  } else if ("CD_GIVEN_EVENT_TYPE".equalsIgnoreCase(mtd.getColumnName(i))) {
	          			  if ("NONE".equalsIgnoreCase(rset.getString(i))){
	          				  eventName = club_actionXML.getfieldTransl("event_type_none", false);
	          			  } else if ("UP_CATEGORIES".equalsIgnoreCase(rset.getString(i))){
	          				  eventName = club_actionXML.getfieldTransl("event_type_up_categories", false);
	          			  } else if ("WRITE_OFF_GOODS".equalsIgnoreCase(rset.getString(i))){
	          				  if ("WRITE_OFF_BONS".equalsIgnoreCase(rset.getString("GE_WRITE_OFF_TYPE"))){
	          					  eventName = club_actionXML.getfieldTransl("event_type_write_off_bons", false);
	          				  } else if ("WRITE_OFF_GOODS_FROM_PURSE".equalsIgnoreCase(rset.getString("GE_WRITE_OFF_TYPE"))){
	          					  eventName = club_actionXML.getfieldTransl("event_type_write_off_purse", false);
	          				  } else {
	          					  eventName = club_actionXML.getfieldTransl("event_type_put_gifts", false);
	          				  }
	          			  } else if ("ADD_GOODS".equalsIgnoreCase(rset.getString(i))){
	          				  if ("ADD_BONS".equalsIgnoreCase(rset.getString("GE_ADD_GOOD_TYPE"))){
	          					  eventName = club_actionXML.getfieldTransl("event_type_add_goods_bons", false);
	          				  } else if ("ADD_GOODS_TO_PURSE".equalsIgnoreCase(rset.getString("GE_ADD_GOOD_TYPE"))){
	          					  eventName = club_actionXML.getfieldTransl("event_type_add_goods_purse", false);
	          				  } else {
	          					  eventName = club_actionXML.getfieldTransl("event_type_add_goods", false);
	          				  }
	          			  }
	          			  html.append(getBottomFrameTableTD(mtd.getColumnName(i), eventName, "", "", ""));
	          		  } else {
	          			  html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", "", ""));
	          		  }
	          	  }
	              if (hasEditPermission) {
	            	  String myHyperLink = "../crm/club_event/clubeventupdate.jsp?type=gifts&id="+this.idAction+"&id_club_event_gift="+rset.getString("ID_CLUB_EVENT_GIFT");
	            	  String myDeleteLink = "../crm/club_event/clubeventupdate.jsp?type=gifts&id="+this.idAction+"&id_club_event_gift="+rset.getString("ID_CLUB_EVENT_GIFT")+"&action=remove&process=yes";
	            	  html.append(getDeleteButtonHTML(myDeleteLink, club_actionXML.getfieldTransl("h_delete_gift", false), rset.getString("NAME_CLUB_EVENT_GIFT")));
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
	  } // getClubActionGiftsHTML
	
	public String getClubActionAddAllGiftsHTML(String pFind, String p_beg, String p_end) {
		ArrayList<bcFeautureParam> pParam = initParamArray();
		String mySQL = 
	    	  	" SELECT cd_gift, name_gift, id_gift " +
	            "   FROM (SELECT ROWNUM rn, id_gift, cd_gift, name_gift " +
	            "           FROM (SELECT id_gift, cd_gift, name_gift" +
	            "                   FROM " + getGeneralDBScheme() + ".vc_club_event_gifts_ca_all " +
	            "                  WHERE id_club_event = ? " +
	            "					 AND id_club_event_gift IS NULL";
		pParam.add(new bcFeautureParam("int", this.idAction));
		
	    if (!isEmpty(pFind)) {
	    	mySQL = mySQL + 
				" AND (UPPER(cd_gift) LIKE UPPER('%'||?||'%') OR " +
				"	   UPPER(name_gift) LIKE UPPER('%'||?||'%')) ";
	    	for (int i=0; i<2; i++) {
	    	    pParam.add(new bcFeautureParam("string", pFind));
	    	}
	    }
	    pParam.add(new bcFeautureParam("int", p_end));
	    pParam.add(new bcFeautureParam("int", p_beg));
	    mySQL = mySQL +
	      		"				   ORDER BY name_gift) " +
	          	"         WHERE ROWNUM < ?" + 
	        	" ) WHERE rn >= ?";
	    StringBuilder html = new StringBuilder();
	    PreparedStatement st = null;
	    Connection con = null;
	    boolean hasEditPermission = false; 
	    boolean hasGiftPermission = false;
	     
	    Integer rowcount = 0;
	    	
	    try{
	    	  if (isEditPermited("CLUB_EVENT_EVENT_GIFTS")>0) {
	    		  hasEditPermission = true;
	    	  }
	    	  if (isEditMenuPermited("CLUB_EVENT_GIFTS")>=0) {
	    		  hasGiftPermission = true;
	    	  }
	    	  
	    	  LOGGER.debug(prepareSQLToLog(mySQL, pParam));
	    	  con = Connector.getConnection(getSessionId());
	          st = con.prepareStatement(mySQL);
	          st = prepareParam(st, pParam);

	          ResultSet rset = st.executeQuery();
	          ResultSetMetaData mtd = rset.getMetaData();
	          
	          int colCount = mtd.getColumnCount();
	          
	          if (hasEditPermission) {
	           	 	html.append("<form method=\"post\" name=\"updateForm\" id=\"updateForm\" action=\"../crm/club_event/clubeventupdate.jsp\">\n");
	           	 	html.append("<input type=\"hidden\" name=\"type\" value=\"gifts\">\n");
	        	 	html.append("<input type=\"hidden\" name=\"action\" value=\"addall\">\n");
	           	 	html.append("<input type=\"hidden\" name=\"process\" value=\"yes\">\n");
	           	 	html.append("<input type=\"hidden\" name=\"id\" value=\"" + this.idAction + "\">\n");
	          }
	            
	          html.append(getBottomFrameTable());
	          html.append("<tr>");

	          if (hasEditPermission) {
	        	  html.append("<th> "+ 
	           			"<input id=\"mainCheck\" name=\"mainCheck\" type=\"checkbox\" onclick=\"CheckAll(this)\"></th>\n");
	          }
	          for (int i=1; i <= colCount-1; i++) {
	        	  html.append(getBottomFrameTableTH(club_actionXML, mtd.getColumnName(i)));
	          }
	          html.append(getBottomFrameTableTH(club_actionXML, "cd_club_event_gift"));
	          html.append(getBottomFrameTableTH(club_actionXML, "name_club_event_gift"));
	          html.append(getBottomFrameTableTH(club_actionXML, "count_gift_all"));
	          html.append("</tr>");
	          if (hasEditPermission) {
	             	html.append("<tr>");
	             	html.append("<td colspan=\"8\" align=\"center\">");
	             	html.append(getSubmitButtonAjax("../crm/club_event/clubeventupdate.jsp"));
	             	html.append(getGoBackButton("../crm/club_event/clubeventspecs.jsp?id=" + this.idAction));
	             	html.append("</td>");
	             	html.append("</tr>");	
	          }
	          html.append("</thead><tbody>\n");
	          
	          rowcount = 0;
	          while (rset.next())
	          {
	        	  rowcount = rowcount + 1;
	        	  String tCheck			= "chb_" + rowcount;

	        	  //String id_gift 		= rset.getString("ID_GIFT");
	        	  String tId 			= "id_" + rowcount;
	        	  String tCd 			= "cd_" + rowcount;
	        	  String tName 			= "name_" + rowcount;
	        	  String tCount 		= "count_" + rowcount;
	        	  
	        	  
	        	  html.append("<tr>");
	        	  html.append("<td>"+
	        			  "<INPUT  type=\"checkbox\" value = \"Y\" name="+tCheck+" id="+tCheck+" checked onclick=\"return CheckCB(this);\">" +
	        			  "<input type=\"hidden\" name="+tId+" id="+tId+" value=\""+ rset.getString("ID_GIFT") +"\">"+
	        	  		  "</td>\n");
	          	  for (int i=1; i <= colCount-1; i++) {
	          		  if ("NAME_GIFT".equalsIgnoreCase(mtd.getColumnName(i)) && hasGiftPermission) {
	          			  html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/club_event/giftspecs.jsp?id="+rset.getString("ID_GIFT"), "", ""));
	          		  } else {
	          			  html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", "", ""));
	          		  }
	          	  }
	        	  html.append("<td><INPUT type=\"text\" name="+tCd+" id="+tCd+" size=\"8\" value=\""+rset.getString("CD_GIFT")+"\" class=\"inputfield\"></td>\n");
	        	  html.append("<td><INPUT type=\"text\" name="+tName+" id="+tName+" size=\"20\" value=\""+rset.getString("NAME_GIFT")+"\" class=\"inputfield\"></td>\n");
	        	  html.append("<td><INPUT type=\"text\" name="+tCount+" id="+tCount+" size=\"8\" value=\"\" class=\"inputfield\"></td>\n");
	              html.append("</tr>\n");
	          }
	          if (hasEditPermission) {

	     	     String rowStr = "" + rowcount;
	             html.append("<tr>");
	             html.append("<td colspan=\"8\" align=\"center\">");
	             html.append("<input type=\"hidden\" name=\"rowcount\" value=\""+ rowStr +"\">");
    	    	 html.append(getSubmitButtonAjax("../crm/club_event/clubeventupdate.jsp"));
	             html.append(getGoBackButton("../crm/club_event/clubeventspecs.jsp?id=" + this.idAction));
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
	  } // getClubActionGiftsHTML

	  public String getClubActionWinnersHTML(String pFind, String pGiftState, String p_beg, String p_end) {
		  ArrayList<bcFeautureParam> pParam = initParamArray();

		  String mySQL =
	    	  " SELECT rn, id_nat_prs_gift, name_nat_prs, name_nat_prs_gift_state, " +
		      "        name_gift, cost_gift_frmt, " +
		      "        date_reserve_frmt, date_given_frmt, id_gift, id_nat_prs " +
		      "   FROM (SELECT ROWNUM rn, id_nat_prs_gift, name_nat_prs, name_nat_prs_gift_state, " +
		      "                name_gift, cost_gift_frmt, date_reserve_frmt, " +
		      "                date_given_frmt, id_gift, id_nat_prs " +
		      "           FROM (SELECT id_nat_prs_gift, full_name name_nat_prs, " +
		      "                        DECODE(cd_nat_prs_gift_state, " +
		      "                               'RESERVED', '<font color=\"green\"><b>'||name_nat_prs_gift_state||'<b></font>', " +
		      "                               'GIVEN', '<font color=\"blue\"><b>'||name_nat_prs_gift_state||'<b></font>', " +
		      "                               '<font color=\"red\"><b>'||name_nat_prs_gift_state||'<b></font>' " +
		      "                        ) name_nat_prs_gift_state, " +
		      "                        name_gift, " +
		      "                        cost_gift_frmt||' '||sname_currency cost_gift_frmt, " +
		      "                        date_reserve_frmt, date_given_frmt, " +
		      "                        id_gift, id_nat_prs " +
	          "                  FROM " + getGeneralDBScheme() + ".vc_nat_prs_gifts_club_all" +
	          "  		           WHERE id_club_event = ? ";
		  pParam.add(new bcFeautureParam("int", this.idAction));
		  
	      if (!isEmpty(pGiftState)) {
	    	  mySQL = mySQL + " AND cd_nat_prs_gift_state = ? ";
	    	  pParam.add(new bcFeautureParam("string", pGiftState));
	      }
	      if (!isEmpty(pFind)) {
	    	  mySQL = mySQL + 
					" AND (TO_CHAR(id_nat_prs_gift) LIKE UPPER('%'||?||'%') OR " +
					"	   UPPER(name_gift) LIKE UPPER('%'||?||'%') OR " +
					"	   UPPER(full_name) LIKE UPPER('%'||?||'%') OR " +
					"	   UPPER(date_reserve_frmt) LIKE UPPER('%'||?||'%') OR " +
					"	   UPPER(date_given_frmt) LIKE UPPER('%'||?||'%')) ";
	    	  for (int i=0; i<5; i++) {
	    		    pParam.add(new bcFeautureParam("string", pFind));
	    	  }
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
	      boolean hasEditPermission = false;
	      boolean hasGiftPermission = false;
	      boolean hasNatPrsGiftPermission = false;
	      boolean hasNatPrsPermission = false;
	      try{
	    	  if (isEditPermited("CLUB_EVENT_EVENT_WINNERS")>0) {
	    		  hasEditPermission = true;
	    	  }
	    	  if (isEditMenuPermited("CLUB_EVENT_DELIVERY")>=0) {
	    		  hasNatPrsGiftPermission = true;
	    	  }
	    	  if (isEditMenuPermited("CLUB_EVENT_GIFTS")>=0) {
	    		  hasGiftPermission = true;
	    	  }
	    	  if (isEditMenuPermited("CLIENTS_NATPERSONS")>=0) {
	    		  hasNatPrsPermission = true;
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
	          if (hasEditPermission) {
	        	  html.append("<th>&nbsp;</th>\n");  
	          }
	          html.append("</tr></thead><tbody>\n");
	          
	          while (rset.next())
	          {
	        	  html.append("<tr>");
	          	  for (int i=1; i <= colCount-2; i++) {
	          		  if ("ID_NAT_PRS_GIFT".equalsIgnoreCase(mtd.getColumnName(i)) && hasNatPrsGiftPermission) {
	          			  html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/club_event/deliveryspecs.jsp?id="+rset.getString("ID_NAT_PRS_GIFT"), "", ""));
	          		  } else if ("NAME_GIFT".equalsIgnoreCase(mtd.getColumnName(i)) && hasGiftPermission) {
	          			  html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/club_event/giftspecs.jsp?id="+rset.getString("ID_GIFT"), "", ""));
	          		  } else if ("NAME_NAT_PRS".equalsIgnoreCase(mtd.getColumnName(i)) && hasNatPrsPermission) {
	          			  html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/clients/natpersonspecs.jsp?id="+rset.getString("ID_NAT_PRS"), "", ""));
	          		  } else {
	          			  html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", "", ""));
	          	  	  }
	          	  }
	              if (hasEditPermission) {
	            	  String myDeleteLink = "../crm/club_event/clubeventupdate.jsp?type=winner&id="+this.idAction+"&id_nat_prs_gift="+rset.getString("ID_NAT_PRS_GIFT")+"&action=remove&process=yes";
	            	  html.append(getDeleteButtonHTML(myDeleteLink, club_actionXML.getfieldTransl("h_delete_winner", false), rset.getString("NAME_NAT_PRS")));
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
	  } // getClubActionWinnersHTML

	  public String getClubActionEstimateHTML(String pFind, String pCriterion, String p_beg, String p_end) {
		  ArrayList<bcFeautureParam> pParam = initParamArray();

		  String mySQL =
	    	  " SELECT name_club_event_estim_crit, value_criterion, note_action, " +
	          "        id_club_event_estimate " +
	          "   FROM (SELECT ROWNUM rn, name_club_event_estim_crit, value_criterion, note_action, " +
	          "        		  id_club_event_estimate " +
	          "   		 FROM (SELECT * " +
	          "                  FROM " + getGeneralDBScheme() + ".vc_club_event_estimate_all " +
	          "  	  	        WHERE id_club_event = ? ";
		  pParam.add(new bcFeautureParam("int", this.idAction));
		  
	      if (!isEmpty(pFind)) {
	    	  mySQL = mySQL + 
					" AND (TO_CHAR(name_club_event_estim_crit) LIKE UPPER('%'||?||'%') OR " +
					"	   UPPER(value_criterion) LIKE UPPER('%'||?||'%') OR " +
					"	   UPPER(note_action) LIKE UPPER('%'||?||'%')) ";
	    	  for (int i=0; i<3; i++) {
	    		    pParam.add(new bcFeautureParam("string", pFind));
	    	  }
	      }
	      if (!isEmpty(pCriterion)) {
	    	  mySQL = mySQL + " AND id_club_event_estim_crit = ? ";
	    	  pParam.add(new bcFeautureParam("int", pCriterion));
	      }
	      pParam.add(new bcFeautureParam("int", p_end));
	      pParam.add(new bcFeautureParam("int", p_beg));
	      mySQL = mySQL +
	          "                 ORDER BY name_club_event_estim_crit) " +
	          "         WHERE ROWNUM < ? " + 
	          " ) WHERE rn >= ?";
	      StringBuilder html = new StringBuilder();
	      PreparedStatement st = null;
	      Connection con = null;
	      boolean hasEditPermission = false; 
	      try{
	    	  if (isEditPermited("CLUB_EVENT_EVENT_ESTIMATE")>0) {
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
	        	  html.append(getBottomFrameTableTH(club_actionXML, mtd.getColumnName(i)));
	          }
	          if (hasEditPermission) {
	        	  html.append("<th>&nbsp;</th><th>&nbsp;</th>\n");  
	          }
	          html.append("</tr></thead><tbody>\n");
	          
	          while (rset.next())
	          {
	        	  html.append("<tr>");
	          	  for (int i=1; i <= colCount-1; i++) {
	          		  html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", "", ""));
	          	  }
	              if (hasEditPermission) {
	            	  String myHyperLink = "../crm/club_event/clubeventupdate.jsp?type=estimate&id="+this.idAction+"&id_estimate="+rset.getString("ID_CLUB_EVENT_ESTIMATE");
	            	  String myDeleteLink = "../crm/club_event/clubeventupdate.jsp?type=estimate&id="+this.idAction+"&id_estimate="+rset.getString("ID_CLUB_EVENT_ESTIMATE")+"&action=remove&process=yes";
	                  html.append(getDeleteButtonHTML(myDeleteLink, club_actionXML.getfieldTransl("h_delete_estimate", false), rset.getString("NAME_CLUB_EVENT_ESTIM_CRIT")));
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
	  } // getClubActionEstimateHTML

	  public String getMessagesHTML(String pFind, String pTypeMessage, String p_beg, String p_end) {
		  ArrayList<bcFeautureParam> pParam = initParamArray();

		  String mySQL = 
	    		"SELECT rn, id_message, type_message, full_name_nat_prs, recepient, text_message, " + 
	    		"		sendings_quantity, error_sendings_quantity, event_date_frmt, " +
	    		"       state_record_tsl, is_archive_tsl, type_message2, " + 
	    		"		id_nat_prs " +
	    		"  FROM (SELECT ROWNUM rn, id_message, type_message, full_name_nat_prs, recepient, text_message, " + 
	    		"				sendings_quantity, error_sendings_quantity, event_date_frmt, " +
	    		"               state_record_tsl, is_archive_tsl, type_message2, " + 
	    		"				id_nat_prs " +
	    		"          FROM (SELECT id_message, type_message_ext type_message, full_name_nat_prs, recepient, " +
	    		"                       CASE WHEN LENGTH(text_message) > 200 " +
	    		"                            THEN SUBSTR(text_message,1,197)||'...'" +
	    		"                            ELSE text_message" +
	    		"                       END text_message, " + 
	    		"						sendings_quantity, error_sendings_quantity, event_date_frmt, " +
	    		"               		DECODE(state_record, " +
	  		  	"                      			'PREPARED', '<b><font color=\"black\">'||state_record_tsl||'</font></b>', " +
				"                      			'NEW', '<font color=\"black\">'||state_record_tsl||'</font>', " +
				"                      			'EXECUTED', '<font color=\"green\">'||state_record_tsl||'</font>', " +
				"                      			'EXECUTED_PARTIALLY', '<font color=\"darkgreen\">'||state_record_tsl||'</font>', " +
				"                      			'ERROR', '<b><font color=\"red\">'||state_record_tsl||'</font></b>', " +
				"                      			'CARRIED_OUT_PARTIALLY', '<b><font color=\"darkgray\">'||state_record_tsl||'</font></b>', " +
				"                      			'CARRIED_OUT', '<font color=\"gray\">'||state_record_tsl||'</font>', " +
				"                      			'CANCELED', '<font color=\"blue\">'||state_record_tsl||'</font>', " +
				"                      			state_record_tsl) state_record_tsl, " +
				"  				       	DECODE(is_archive, " +
				"                             	'Y', '<b><font color=\"red\">'||is_archive_tsl||'</font></b>', " +
				"                             	is_archive_tsl) is_archive_tsl, " +
	  		  	"                       type_message type_message2, id_nat_prs " +
	    		"                  FROM " + getGeneralDBScheme() + ".vc_ds_cl_pattern_mess_club_all " +
	    		"                 WHERE id_club_event = ? ";
		  pParam.add(new bcFeautureParam("int", this.idAction));
		  
	      if (!isEmpty(pFind)) {
	        	mySQL = mySQL +
	        		" AND (TO_CHAR(id_message) LIKE '%'||?||'%' " +
	        		"    OR UPPER(full_name_nat_prs) LIKE UPPER('%'||?||'%')" +
	        		"    OR UPPER(recepient) LIKE UPPER('%'||?||'%')" +
	        		"    OR UPPER(text_message) LIKE UPPER('%'||?||'%')" +
	        		"    OR UPPER(event_date_frmt) LIKE UPPER('%'||?||'%'))";
	        	for (int i=0; i<5; i++) {
	        	    pParam.add(new bcFeautureParam("string", pFind));
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
	    		"         WHERE ROWNUM < ?" + 
	    		" ) WHERE rn >= ?";
	        
	        StringBuilder html = new StringBuilder();
	        Connection con = null;
	        PreparedStatement st = null; 
	        
	        boolean hasSMSPermission = false;
	        boolean hasEmailPermission = false;
	        boolean hasOfficePermission = false;
  	        boolean hasNatPrsPermission = false;

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
		    	if (isEditMenuPermited("CLIENTS_NATPERSONS")>=0) {
		    		hasNatPrsPermission = true;
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
	            		} else if (hasNatPrsPermission && "FULL_NAME_NAT_PRS".equalsIgnoreCase(mtd.getColumnName(i))) {
		          			  html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/clients/natpersonspecs.jsp?id="+rset.getString("ID_NAT_PRS"), "", ""));
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
	    
	    public String getClubCardsTasksHTML(String pFindString, String pOperType, String pOperState, String p_beg, String p_end) {
	    	bcListCardOperation list = new bcListCardOperation();
	    	
	    	String pWhereCause = "   WHERE id_club_event = ? ";
	    	
	    	ArrayList<bcFeautureParam> pWhereValue = new ArrayList<bcFeautureParam>();
	    	pWhereValue.add(new bcFeautureParam("int", this.idAction));

	    	String lDeleteLink = "";
	    	String lEditLink = "";
	    	
	    	return list.getCardOperationsHTML(pWhereCause, pWhereValue, pFindString, pOperType, pOperState, lEditLink, lDeleteLink, "div_data_detail", p_beg, p_end);
	    	
	    }

	public String getClubActionBonCardsHTML(String pFind, String p_beg, String p_end) {
		ArrayList<bcFeautureParam> pParam = initParamArray();

		String mySQL =
			"SELECT rn, cd_card1, full_name name_nat_prs, name_card_status, name_card_type, " +
        	"		name_card_state, date_card_sale_frmt, nt_icc, " +
        	"		card_serial_number, id_issuer, id_payment_system, id_nat_prs " +
        	"  FROM (SELECT ROWNUM rn, cd_card1, full_name, " +
        	"				name_card_type, name_card_status,  " +
        	"				name_card_state, date_card_sale_frmt, nt_icc, " +
        	"               card_serial_number,  id_issuer, id_payment_system, id_nat_prs " +
        	"		  FROM (SELECT a.* " +
        	"                 FROM " + getGeneralDBScheme()+".vc_card_find_all a " +
        	"                WHERE id_club_event = ? ";
		pParam.add(new bcFeautureParam("int", this.idAction));
		
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
        mySQL = mySQL + ") WHERE ROWNUM < ?) WHERE rn >= ?";
    
	    StringBuilder html = new StringBuilder();
	    PreparedStatement st = null;
	    Connection con = null;
	    boolean hasNatPrsPermission = false;
	    boolean hasClubCardPermission = false;
	    try{
	    	  if (isEditMenuPermited("CLIENTS_NATPERSONS")>=0) {
	    		  hasNatPrsPermission = true;
	    	  }
	    	  if (isEditMenuPermited("CARDS_CLUBCARDS")>=0) {
	    		  hasClubCardPermission = true;
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
	              String colName = mtd.getColumnName(i);
	              html.append(getBottomFrameTableTH(clubcardXML, colName));
	          }
	          html.append("</tr></thead><tbody>\n");
	          
	          while (rset.next())
	          {
	        	  html.append("<tr>");
	          	  for (int i=1; i <= colCount-4; i++) {
	          		  if (hasNatPrsPermission && "NAME_NAT_PRS".equalsIgnoreCase(mtd.getColumnName(i))) {
	          			  html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/clients/natpersonspecs.jsp?id="+rset.getString("ID_NAT_PRS"), "", ""));
	          		  } else if ("CD_CARD1".equalsIgnoreCase(mtd.getColumnName(i)) && hasClubCardPermission) {
	          			  html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/cards/clubcardspecs.jsp?id="+rset.getString("CARD_SERIAL_NUMBER")+"&iss="+rset.getString("ID_ISSUER")+"&paysys="+rset.getString("ID_PAYMENT_SYSTEM"), "", ""));
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

	} // getClubActionWinnersHTML
	    
}
