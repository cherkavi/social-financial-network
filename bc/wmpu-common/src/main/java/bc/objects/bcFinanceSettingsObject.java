package bc.objects;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import bc.connection.Connector;


public class bcFinanceSettingsObject extends bcObject {
	private final static Logger LOGGER=Logger.getLogger(bcFinanceSettingsObject.class);
    private Map<String,Comparable<String>> fieldHm = new HashMap<String, Comparable<String>>();
	
	private String idGift;
	
	public bcFinanceSettingsObject(String pIdGift) {
		this.idGift = pIdGift;
		getFeature();
	}
	
	private void getFeature() {
		String featureSelect = " SELECT * FROM " + getGeneralDBScheme() + ".VC_FINANCE_SETTINGS_CLUB_ALL WHERE id_gift = ?";
		fieldHm = getFeatures2(featureSelect, this.idGift, false);
	}
	
	public String getValue(String pColumnName) {
		return getFeautureResult(fieldHm, pColumnName);
	}

	public String getVATParametersHTML(String p_beg, String p_end) {
	    String mySQL = 
	    	  	" SELECT id_club_event, name_club_event, date_beg, date_end, " +
	    	  	"        bal_count, count_gift_all, count_gift_remain, name_currency " +
	            "   FROM (SELECT ROWNUM rn, id_club_event, name_club_event, date_beg_frmt date_beg, date_end_frmt date_end, " +
	            " 		         bal_count_frmt bal_count, count_gift_all, count_gift_remain, name_currency " +
	            "           FROM (SELECT * " +
	            "                   FROM " + getGeneralDBScheme() + ".vc_club_event_gifts_all " +
	            "                  WHERE id_gift = ? " +
	          	"                  ORDER BY id_club_event, date_beg) " +
	          	"         WHERE ROWNUM < ? " +
	        	" ) WHERE rn >= ?";
	      StringBuilder html = new StringBuilder();
	      Connection con = null;
	      PreparedStatement st = null;
	      boolean hasActionPermission = false; 
	      try{
	    	  if (isEditMenuPermited("CLUB_EVENT_EVENT")>=0) {
	    		  hasActionPermission = true;
	    	  }
	    	  
	    	  LOGGER.debug(mySQL + 
	            		", 1={" + this.idGift + ",int}" + 
	            		", 2={" + p_end + ",int}" + 
	            		", 3={" + p_beg + ",int}");
	    	  con = Connector.getConnection(getSessionId());
	          st = con.prepareStatement(mySQL);
	          st.setInt(1, Integer.parseInt(this.idGift));
	          st.setInt(2, Integer.parseInt(p_end));
	          st.setInt(3, Integer.parseInt(p_beg));
	          ResultSet rset = st.executeQuery();
	          ResultSetMetaData mtd = rset.getMetaData();	          
	          int colCount = mtd.getColumnCount();
	          
	          html.append(getBottomFrameTable());
	          html.append("<tr>");
	          for (int i=1; i <= colCount; i++) {
	        	  html.append(getBottomFrameTableTH(club_actionXML, mtd.getColumnName(i)));
	          }
	          html.append("</tr></thead><tbody>\n");
	          
	          while (rset.next())
	          {
	        	  html.append("<tr>");
	        	  for (int i=1; i <= colCount; i++) {
	        		  if (hasActionPermission && "ID_CLUB_EVENT".equalsIgnoreCase(mtd.getColumnName(i))) {
	        			  html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/club_event/clubeventspecs.jsp?id="+rset.getString(i), "", ""));
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
