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


public class bcNatPrsGiftRequestObject extends bcObject {
	private final static Logger LOGGER=Logger.getLogger(bcNatPrsGiftRequestObject.class);
	private Map<String,Comparable<String>> fieldHm = new HashMap<String, Comparable<String>>();
	
	private String idRequest;
	
	public bcNatPrsGiftRequestObject(String pIdRequest) {
		this.idRequest = pIdRequest;
		getFeature();
	}
	
	private void getFeature() {
		String featureSelect = " SELECT * FROM " + getGeneralDBScheme() + ".VC_NAT_PRS_GIFT_REQUEST_CL_ALL WHERE id_nat_prs_gift_request = ?";
		fieldHm = getFeatures2(featureSelect, this.idRequest, false);
	}
	
	public String getValue(String pColumnName) {
		return getFeautureResult(fieldHm, pColumnName);
	}    
	
	public String getGiftsHTML(String pFind, String pState, String p_beg, String p_end) {
		ArrayList<bcFeautureParam> pParam = initParamArray();

		String mySQL = 
	    	" SELECT rn, id_nat_prs_gift, name_nat_prs, name_nat_prs_gift_state, " +
	        "        name_gift, cost_gift_frmt, name_club_event, " +
	        "        date_reserve_frmt, date_given_frmt, id_gift, id_nat_prs, id_club_event " +
	        "   FROM (SELECT ROWNUM rn, id_nat_prs_gift, name_nat_prs, name_nat_prs_gift_state, " +
	        "                name_gift, cost_gift_frmt, name_club_event, date_reserve_frmt, " +
	        "                date_given_frmt, id_gift, id_nat_prs, id_club_event " +
	        "           FROM (SELECT id_nat_prs_gift, full_name name_nat_prs, " +
	        "                        DECODE(cd_nat_prs_gift_state, " +
	        "                               'RESERVED', '<font color=\"green\"><b>'||name_nat_prs_gift_state||'<b></font>', " +
	        "                               'GIVEN', '<font color=\"blue\"><b>'||name_nat_prs_gift_state||'<b></font>', " +
	        "                               '<font color=\"red\"><b>'||name_nat_prs_gift_state||'<b></font>' " +
	        "                        ) name_nat_prs_gift_state, " +
	        "                        name_gift, " +
	        "                        cost_gift_frmt||' '||sname_currency cost_gift_frmt, " +
	        "                        name_club_event, date_reserve_frmt, date_given_frmt, " +
	        "                        id_gift, id_nat_prs, id_club_event " +
          	"                  FROM " + getGeneralDBScheme() + ".vc_nat_prs_gifts_club_all" +
	        "                 WHERE id_nat_prs_gift_request = ? ";
		pParam.add(new bcFeautureParam("int", this.idRequest));
		
		if (!isEmpty(pFind)) {
			mySQL = mySQL + 
	    	  	" AND (UPPER(full_name) LIKE UPPER('%'||?||'%') OR " +
	    	  	"	   UPPER(name_gift) LIKE UPPER('%'||?||'%') OR " +
	    	  	"	   UPPER(name_club_event) LIKE UPPER('%'||?||'%') OR " +
	    	  	"	   UPPER(date_reserve_frmt) LIKE UPPER('%'||?||'%') OR " +
	    	  	"	   UPPER(date_given_frmt) LIKE UPPER('%'||?||'%')) ";
			for (int i=0; i<5; i++) {
			    pParam.add(new bcFeautureParam("string", pFind));
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
	          	"         WHERE ROWNUM < ? " + 
	        	" ) WHERE rn >= ?";
		StringBuilder html = new StringBuilder();
		PreparedStatement st = null;
		Connection con = null;
		boolean hasEditPermission = false; 
		boolean hasGiftPermission = false;
		boolean hasNatPrsGiftPermission = false;
		boolean hasNatPrsPermission = false;
		boolean hasClubEventPermission = false;
		try{
	    	  if (isEditPermited("CLUB_EVENT_REQUEST_GIFTS")>0) {
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

	          for (int i=1; i <= colCount-3; i++) {
	        	  html.append(getBottomFrameTableTH(club_actionXML, mtd.getColumnName(i)));
	          }
	          if (hasEditPermission) {
	          	  html.append("<th>&nbsp;</th>\n");  
	          }
	          html.append("</tr></thead><tbody>\n");
	          
	          while (rset.next())
	          {
	        	  html.append("<tr>");
	          	  for (int i=1; i <= colCount-3; i++) {
	          		  if ("ID_NAT_PRS_GIFT".equalsIgnoreCase(mtd.getColumnName(i)) && hasNatPrsGiftPermission) {
	          			  html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/club_event/deliveryspecs.jsp?id="+rset.getString("ID_NAT_PRS_GIFT"), "", ""));
	          		  } else if ("NAME_GIFT".equalsIgnoreCase(mtd.getColumnName(i)) && hasGiftPermission) {
	          			  html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/club_event/giftspecs.jsp?id="+rset.getString("ID_GIFT"), "", ""));
	          		  } else if ("NAME_NAT_PRS".equalsIgnoreCase(mtd.getColumnName(i)) && hasNatPrsPermission) {
	          			  html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/clients/natpersonspecs.jsp?id="+rset.getString("ID_NAT_PRS"), "", ""));
	          		  } else if ("NAME_CLUB_EVENT".equalsIgnoreCase(mtd.getColumnName(i)) && hasClubEventPermission) {
	          			  html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/club_event/clubeventspecs.jsp?id="+rset.getString("ID_CLUB_EVENT"), "", ""));
	          		  } else {
	          			  html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", "", ""));
	          		  }
	          	  }
	              if (hasEditPermission) {
	            	  String myDeleteLink = "../crm/club_event/requestupdate.jsp?type=gift&id="+this.idRequest+"&id_nat_prs_gift="+rset.getString("ID_NAT_PRS_GIFT")+"&action=remove&process=yes";
	            	  html.append(getDeleteButtonHTML(myDeleteLink, club_actionXML.getfieldTransl("h_delete_gift", false), rset.getString("ID_NAT_PRS_GIFT") + " - " + rset.getString("NAME_GIFT")));
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
