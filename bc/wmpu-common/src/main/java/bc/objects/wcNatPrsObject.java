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

public class wcNatPrsObject extends bcObject {
	private final static Logger LOGGER=Logger.getLogger(wcNatPrsObject.class);
	private Map<String,Comparable<String>> fieldHm = new HashMap<String, Comparable<String>>();
	
	private String idNatPrs;
	
	public wcNatPrsObject(String pIdNatPrs) {
		this.idNatPrs = pIdNatPrs;
		getFeature();
	}
	
	private void getFeature() {
		String featureSelect = " SELECT * FROM " + getGeneralDBScheme() + ".vw$nat_prs_all WHERE id_nat_prs = ?";
		fieldHm = getFeatures2(featureSelect, this.idNatPrs, false);
	}
	
	public String getValue(String pColumnName) {
		return getFeautureResult(fieldHm, pColumnName);
	}

    public String getNatPersonMessagesHTML(String pFindString, String pTypeMessage, String p_beg, String p_end) {
    	ArrayList<bcFeautureParam> pParam = initParamArray();

    	String mySQL = 
    		"SELECT rn, type_message, title_message, event_date_frmt, state_record_tsl " +
    		"  FROM (SELECT ROWNUM rn, id_message, type_message, title_message, " + 
    		"				sendings_quantity, error_sendings_quantity, event_date_frmt, " +
    		"               state_record_tsl, is_archive_tsl, type_message2, " + 
    		"				id_nat_prs " +
    		"          FROM (SELECT id_ds_message id_message, name_ds_message_type type_message, " +
    		"                       title_ds_message title_message, " + 
    		"						sendings_quantity, error_sendings_quantity, event_date_frmt, " +
    		"               		DECODE(cd_ds_message_state, " +
  		  	"                      			'PREPARED', '<b><font color=\"black\">'||name_ds_message_state||'</font></b>', " +
			"                      			'NEW', '<font color=\"black\">'||name_ds_message_state||'</font>', " +
			"                      			'EXECUTED', '<font color=\"green\">'||name_ds_message_state||'</font>', " +
			"                      			'EXECUTED_PARTIALLY', '<font color=\"darkgreen\">'||name_ds_message_state||'</font>', " +
			"                      			'ERROR', '<b><font color=\"red\">'||name_ds_message_state||'</font></b>', " +
			"                      			'CARRIED_OUT_PARTIALLY', '<b><font color=\"darkgray\">'||name_ds_message_state||'</font></b>', " +
			"                      			'CARRIED_OUT', '<font color=\"gray\">'||name_ds_message_state||'</font>', " +
			"                      			'CANCELED', '<font color=\"blue\">'||name_ds_message_state||'</font>', " +
			"                      			name_ds_message_state) state_record_tsl, " +
			"  				       	DECODE(is_archive, " +
			"                             	'Y', '<b><font color=\"red\">'||is_archive_tsl||'</font></b>', " +
			"                             	is_archive_tsl) is_archive_tsl, " +
  		  	"                       cd_ds_message_type type_message2, id_nat_prs " +
    		"                  FROM " + getGeneralDBScheme() + ".vw$nat_prs_messages_all " +
    		"                 WHERE id_nat_prs = ? ";
    	pParam.add(new bcFeautureParam("int", this.idNatPrs));
    	
    	if (!isEmpty(pFindString)) {
    		mySQL = mySQL + 
   				" AND (TO_CHAR(id_ds_message) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(title_ds_message) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(event_date_frmt) LIKE UPPER('%'||?||'%'))";
    		for (int i=0; i<4; i++) {
    		    pParam.add(new bcFeautureParam("string", pFindString));
    		}
    	}
    	if (!isEmpty(pTypeMessage)) {
    		mySQL = mySQL + " AND cd_ds_message_type = ? ";
    		pParam.add(new bcFeautureParam("string", pTypeMessage));
    	}
    	pParam.add(new bcFeautureParam("int", p_end));
    	pParam.add(new bcFeautureParam("int", p_beg));
    	mySQL = mySQL + 
    		"                 ORDER BY event_date desc, id_ds_message) "+
    		"         WHERE ROWNUM < ? " + 
    		" ) WHERE rn >= ?";
        
        StringBuilder html = new StringBuilder();
        Connection con = null;
        PreparedStatement st = null; 
        //
        try{
        	
        	LOGGER.debug(prepareSQLToLog(mySQL, pParam));
        	con = Connector.getConnection(getSessionId());
            st = con.prepareStatement(mySQL);
            st = prepareParam(st, pParam);
            ResultSet rset = st.executeQuery();
            ResultSetMetaData mtd = rset.getMetaData();
            int colCount = mtd.getColumnCount();
            
            html.append(getBottomFrameTable());
            html.append("<tr>");
            for (int i=1; i <= colCount; i++) {
            	html.append(getBottomFrameTableTH(messageXML, mtd.getColumnName(i)));
            }
            html.append("</tr></thead><tbody>");
            while (rset.next())
            {
       		 	html.append("<tr>\n");
            	for (int i=1; i<=colCount; i++) {
            		html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", "", ""));
                }
            	html.append("</tr>\n");
            }
            html.append("</tbody></table>\n");
        } // try
        catch (SQLException e) {LOGGER.error(html, e);}
        catch (Exception el) {LOGGER.error(html, el);}
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
	
    public String getGiftsHTML(String pFindString, String pState, String p_beg, String p_end) {
    	ArrayList<bcFeautureParam> pParam = initParamArray();

    	String mySQL = 
        	" SELECT rn, id_nat_prs_gift, name_nat_prs_gift_state, " +
	        "        name_gift, cost_gift_frmt, name_club_event, " +
	        "        date_reserve_frmt, date_given_frmt, id_gift, id_club_event " +
	        "   FROM (SELECT ROWNUM rn, id_nat_prs_gift, name_nat_prs_gift_state, " +
	        "                name_gift, cost_gift_frmt, name_club_event, date_reserve_frmt, " +
	        "                date_given_frmt, id_gift, id_club_event " +
	        "           FROM (SELECT id_nat_prs_gift, " +
	        "                        DECODE(cd_nat_prs_gift_state, " +
	        "                               'RESERVED', '<font color=\"green\"><b>'||name_nat_prs_gift_state||'<b></font>', " +
	        "                               'GIVEN', '<font color=\"blue\"><b>'||name_nat_prs_gift_state||'<b></font>', " +
	        "                               '<font color=\"red\"><b>'||name_nat_prs_gift_state||'<b></font>' " +
	        "                        ) name_nat_prs_gift_state, " +
	        "                        name_gift, " +
	        "                        cost_gift_frmt||' '||sname_currency cost_gift_frmt, " +
	        "                        name_club_event, date_reserve_frmt, date_given_frmt, " +
	        "                        id_gift, id_club_event " +
	        "                   FROM " + getGeneralDBScheme() + ".vc_nat_prs_gifts_club_all " +
	        "  		           WHERE id_nat_prs = ? ";
    	pParam.add(new bcFeautureParam("int", this.idNatPrs));
    	
    	if (!isEmpty(pFindString)) {
    		mySQL = mySQL + 
   				" AND (TO_CHAR(id_nat_prs_gift) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(name_gift) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(cost_gift_frmt||' '||sname_currency) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(name_club_event) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(date_reserve_frmt) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(date_given_frmt) LIKE UPPER('%'||?||'%'))";
    		for (int i=0; i<6; i++) {
    		    pParam.add(new bcFeautureParam("string", pFindString));
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
	        "          WHERE ROWNUM < ? " + 
	        " ) WHERE rn >= ?";
        StringBuilder html = new StringBuilder();
        PreparedStatement st = null;
        Connection con = null;
        boolean hasGiftPermission = false;
        boolean hasNatPrsGiftPermission = false;
        boolean hasClubEventPermission = false;
        try{

	    	  if (isEditMenuPermited("CLUB_EVENT_DELIVERY")>=0) {
	    		  hasNatPrsGiftPermission = true;
	    	  }
	    	  if (isEditMenuPermited("CLUB_EVENT_GIFTS")>=0) {
	    		  hasGiftPermission = true;
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

	          for (int i=1; i <= colCount-2; i++) {
	              String colName = mtd.getColumnName(i);
	              html.append(getBottomFrameTableTH(club_actionXML, colName));
	          }
	          html.append("</tr></thead><tbody>\n");
	          
	          while (rset.next())
	          {
	        	  html.append("<tr>");
	          	  for (int i=1; i <= colCount-2; i++) {
	          		  if ("ID_NAT_PRS_GIFT".equalsIgnoreCase(mtd.getColumnName(i)) && hasNatPrsGiftPermission) {
	          			  html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "/club_event/deliveryspecs.jsp?id="+rset.getString("ID_NAT_PRS_GIFT"), "", ""));
	          		  } else if ("NAME_GIFT".equalsIgnoreCase(mtd.getColumnName(i)) && hasGiftPermission) {
	          			  html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "/club_event/giftspecs.jsp?id="+rset.getString("ID_GIFT"), "", ""));
	          		  } else if ("NAME_CLUB_EVENT".equalsIgnoreCase(mtd.getColumnName(i)) && hasClubEventPermission) {
	          			  html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "/club_event/clubeventspecs.jsp?id="+rset.getString("ID_CLUB_EVENT"), "", ""));
	          		  } else {
	          			  html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", "", ""));
	          	  	  }
	          	  }
	              html.append("</td>\n");
	          }
	          html.append("</tbody></table>\n");
	      } // try
	      catch (SQLException e) {LOGGER.error(html, e);}
	      catch (Exception el) {LOGGER.error(html, el);}
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
