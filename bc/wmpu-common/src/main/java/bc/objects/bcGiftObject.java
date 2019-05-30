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


public class bcGiftObject extends bcObject {
	private final static Logger LOGGER=Logger.getLogger(bcGiftObject.class);
    private Map<String,Comparable<String>> fieldHm = new HashMap<String, Comparable<String>>();
	
	private String idGift;
	
	public bcGiftObject(String pIdGift) {
		this.idGift = pIdGift;
		getFeature();
	}
	
	private void getFeature() {
		String featureSelect = " SELECT * FROM " + getGeneralDBScheme() + ".VC_GIFTS_CLUB_ALL WHERE id_gift = ?";
		fieldHm = getFeatures2(featureSelect, this.idGift, false);
	}
	
	public String getValue(String pColumnName) {
		return getFeautureResult(fieldHm, pColumnName);
	}

	public String getClubActionHTML(String pFind, String p_beg, String p_end) {
		ArrayList<bcFeautureParam> pParam = initParamArray();

		String mySQL = 
	    	    " SELECT rn, name_club_event, cd_club_event_gift, name_club_event_gift, " +
	    	  	"        count_gift_all_frmt, " +
	    	  	"        count_gift_reserved_frmt, count_gift_given_frmt, " +
	    	  	"        count_gift_remain_frmt, " +
	    	  	"        is_active_tsl, id_club_event_gift, id_club_event " +
	            "   FROM (SELECT ROWNUM rn, name_club_event, " +
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
	      		" 		         DECODE(is_active," +
	            "                       'Y', is_active_tsl," +
	            "                       'N', '<font color=\"red\"><b>'||is_active_tsl||'</b></font>'," +
	            "                       is_active_tsl" +
	            "                ) is_active_tsl, " +
	            "                id_club_event_gift, id_club_event " +
	            "           FROM (SELECT name_club_event, cd_club_event_gift, name_club_event_gift, " +
	            "                        count_gift_all, count_gift_reserved, count_gift_given, count_gift_remain, " +
	    	  	"        				 is_active_tsl, is_active, id_club_event_gift, id_club_event" +
	    	  	"                   FROM " + getGeneralDBScheme() + ".vc_club_event_gifts_all " +
	            "                  WHERE id_gift = ?";
		pParam.add(new bcFeautureParam("int", this.idGift));
		
	    if (!isEmpty(pFind)) {
	    	mySQL = mySQL + 
	    		" AND (UPPER(name_club_event) LIKE UPPER('%'||?||'%') OR " +
	    		"	   UPPER(cd_club_event_gift) LIKE UPPER('%'||?||'%') OR " +
	    		"	   UPPER(name_club_event_gift) LIKE UPPER('%'||?||'%')) ";
	    	for (int i=0; i<3; i++) {
	    		pParam.add(new bcFeautureParam("string", pFind));
	    	}
	    }
	    pParam.add(new bcFeautureParam("int", p_end));
	    pParam.add(new bcFeautureParam("int", p_beg));
	    mySQL = mySQL +
	          	"                  ORDER BY id_club_event, date_beg) " +
	          	"         WHERE ROWNUM < ?" + 
	        	" ) WHERE rn >= ?";
	      StringBuilder html = new StringBuilder();
	      Connection con = null;
	      PreparedStatement st = null;
	      boolean hasActionPermission = false; 
	      try{
	    	  if (isEditMenuPermited("CLUB_EVENT_EVENT")>=0) {
	    		  hasActionPermission = true;
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
	        	  html.append(getBottomFrameTableTH(club_actionXML, mtd.getColumnName(i)));
	          }
	          html.append("</tr></thead><tbody>\n");
	          
	          while (rset.next())
	          {
	        	  html.append("<tr>");
	        	  for (int i=1; i <= colCount-2; i++) {
	        		  if (hasActionPermission && "NAME_CLUB_EVENT".equalsIgnoreCase(mtd.getColumnName(i))) {
	        			  html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/club_event/clubeventspecs.jsp?id="+rset.getString("ID_CLUB_EVENT"), "", ""));
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

	  public String getWinnersHTML(String pFind, String pGiftState, String p_beg, String p_end) {
		  ArrayList<bcFeautureParam> pParam = initParamArray();

		  String mySQL = 
			  " SELECT rn, id_nat_prs_gift, name_nat_prs, name_nat_prs_gift_state, " +
		      "        name_gift, cost_gift_frmt, " +
		      "        date_reserve_frmt, date_given_frmt, id_nat_prs " +
		      "   FROM (SELECT ROWNUM rn, id_nat_prs_gift, name_nat_prs, name_nat_prs_gift_state, " +
		      "                name_gift, cost_gift_frmt, date_reserve_frmt, " +
		      "                date_given_frmt, id_nat_prs " +
		      "           FROM (SELECT id_nat_prs_gift, full_name name_nat_prs, " +
		      "                        DECODE(cd_nat_prs_gift_state, " +
		      "                               'RESERVED', '<font color=\"green\"><b>'||name_nat_prs_gift_state||'<b></font>', " +
		      "                               'GIVEN', '<font color=\"blue\"><b>'||name_nat_prs_gift_state||'<b></font>', " +
		      "                               '<font color=\"red\"><b>'||name_nat_prs_gift_state||'<b></font>' " +
		      "                        ) name_nat_prs_gift_state, " +
		      "                        name_gift, " +
		      "                        cost_gift_frmt||' '||sname_currency cost_gift_frmt, " +
		      "                        date_reserve_frmt, date_given_frmt, " +
		      "                        id_nat_prs " +
	          "                  FROM " + getGeneralDBScheme() + ".vc_nat_prs_gifts_club_all" +
	          "  		         WHERE id_gift = ? ";
		  pParam.add(new bcFeautureParam("int", this.idGift));
		  
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
	      Connection con = null;
	      PreparedStatement st = null; 
	      boolean hasEditPermission = false;
	      boolean hasNatPrsGiftPermission = false;
	      boolean hasNatPrsPermission = false;
	      try{
	    	  if (isEditPermited("CLUB_EVENT_GIFTS_WINNERS")>0) {
	    		  hasEditPermission = true;
	    	  }
	    	  if (isEditMenuPermited("CLUB_EVENT_DELIVERY")>=0) {
	    		  hasNatPrsGiftPermission = true;
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

	          for (int i=1; i <= colCount-1; i++) {
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
	          	  for (int i=1; i <= colCount-1; i++) {
	          		  if ("ID_NAT_PRS_GIFT".equalsIgnoreCase(mtd.getColumnName(i)) && hasNatPrsGiftPermission) {
	          			  html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/club_event/deliveryspecs.jsp?id="+rset.getString("ID_NAT_PRS_GIFT"), "", ""));
	          		  } else if ("NAME_NAT_PRS".equalsIgnoreCase(mtd.getColumnName(i)) && hasNatPrsPermission) {
	          			  html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/clients/natpersonspecs.jsp?id="+rset.getString("ID_NAT_PRS"), "", ""));
	          		  } else {
	          			  html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", "", ""));
	          	  	  }
	          	  }
	              if (hasEditPermission) {
	            	  String myDeleteLink = "../crm/club_event/giftupdate.jsp?type=winner&id="+this.idGift+"&id_nat_prs_gift="+rset.getString("ID_NAT_PRS_GIFT")+"&action=remove&process=yes";
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

	public String getWarehouseGiftsHTML(String pFind, String p_beg, String p_end) {
		ArrayList<bcFeautureParam> pParam = initParamArray();

		String mySQL = 
	    	  	" SELECT action_date_frmt lg_action_date_frmt, desc_gift, cost_one_gift_frmt, count_gift_all_frmt, " +
	    	  	"        count_gift_given_frmt, count_gift_remain_frmt, id_lg_record " +
	            "   FROM (SELECT ROWNUM rn, action_date_frmt, desc_gift, " +
	            "                cost_one_gift_frmt||' '||sname_currency cost_one_gift_frmt, " +
	            " 		         DECODE(NVL(count_gift_all,0)," +
	            "                       0, '<font color=\"red\"><b>'||TO_CHAR(count_gift_all)||'</b></font>'," +
	            "                       TO_CHAR(count_gift_all)" +
	            "                ) count_gift_all_frmt, " +
	            " 		         DECODE(NVL(count_gift_given,0)," +
	            "                       0, TO_CHAR(count_gift_given)," +
	            "                       '<font color=\"blue\"><b>'||TO_CHAR(count_gift_given)||'</b></font>'" +
	            "                ) count_gift_given_frmt, " +
	      		" 		         DECODE(NVL(count_gift_remain,0)," +
	            "                       0, '<font color=\"red\"><b>'||TO_CHAR(count_gift_remain)||'</b></font>'," +
	            "                       TO_CHAR(count_gift_remain)" +
	            "                ) count_gift_remain_frmt, " +
	            "                id_lg_record " +
	            "           FROM (SELECT * " +
	            "                   FROM " + getGeneralDBScheme() + ".vc_lg_gifts_club_all " +
	            "                  WHERE id_gift = ? ";
		pParam.add(new bcFeautureParam("int", this.idGift));
		
	    if (!isEmpty(pFind)) {
	    	mySQL = mySQL + 
	    		" AND (UPPER(action_date_frmt) LIKE UPPER('%'||?||'%') OR " +
	    		"	   UPPER(desc_gift) LIKE UPPER('%'||?||'%') OR " +
	    		"	   UPPER(cost_one_gift_frmt||' '||sname_currency) LIKE UPPER('%'||?||'%')) ";
	    	for (int i=0; i<3; i++) {
	    	    pParam.add(new bcFeautureParam("string", pFind));
	    	}
	    }
	    pParam.add(new bcFeautureParam("int", p_end));
	    pParam.add(new bcFeautureParam("int", p_beg));
	    mySQL = mySQL +
	    	"                  ORDER BY action_date DESC) " +
	    	"         WHERE ROWNUM < ? " + 
	    	" ) WHERE rn >= ?";
	    StringBuilder html = new StringBuilder();
	    Connection con = null;
	    PreparedStatement st = null;
	    boolean hasWarehousePermission = false; 
	    try{
	    	  if (isEditMenuPermited("CLUB_EVENT_WAREHOUSE")>=0) {
	    		  hasWarehousePermission = true;
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
	        		  if (hasWarehousePermission && "LG_ACTION_DATE_FRMT".equalsIgnoreCase(mtd.getColumnName(i))) {
	        			  html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/club_event/clubeventspecs.jsp?id="+rset.getString("ID_LG_RECORD"), "", ""));
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
}
