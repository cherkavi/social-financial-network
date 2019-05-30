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

public class bcLGGiftObject extends bcObject {
	private final static Logger LOGGER=Logger.getLogger(bcLGGiftObject.class);
	private Map<String,Comparable<String>> fieldHm = new HashMap<String, Comparable<String>>();
	
	private String idLine;
	
	public bcLGGiftObject(String pIdLine) {
		this.idLine = pIdLine;
		getFeature();
	}
	
	private void getFeature() {
		String mySQL = " SELECT * FROM " + getGeneralDBScheme() + ".VC_LG_GIFTS_CLUB_ALL WHERE id_lg_gift = ?";
		fieldHm = getFeatures2(mySQL, this.idLine, false);
	}
	
	public String getValue(String pColumnName) {
		return getFeautureResult(fieldHm, pColumnName);
	}

	public String getGiftsWinnersHTML(String pFind, String pGiftState, String p_beg, String p_end) {

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
	          "  		         WHERE id_lg_gift = ? ";
		pParam.add(new bcFeautureParam("int", this.idLine));
		  
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
	    boolean hasNatPrsGiftPermission = false;
	    boolean hasNatPrsPermission = false;
	    try{
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
