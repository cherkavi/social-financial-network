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

public class bcClubOnlinePaymentTypeObject extends bcObject {
	private final static Logger LOGGER=Logger.getLogger(bcClubOnlinePaymentTypeObject.class);
	
	private String idOnlinePaymenttype;
	
	private Map<String,Comparable<String>> fieldHm = new HashMap<String, Comparable<String>>();
	
	public bcClubOnlinePaymentTypeObject(String pIdOnlinePaymentType){
		this.idOnlinePaymenttype = pIdOnlinePaymentType;
		getFeature();
	}
	
	private void getFeature() {
		bcFeautureParam[] array = new bcFeautureParam[1];
		array[0] = new bcFeautureParam("int", idOnlinePaymenttype);
		String mySQL = " SELECT * FROM " + getGeneralDBScheme() + ".vc_club_online_pay_type_cl_all WHERE id_club_online_pay_type = ?";
		fieldHm = getFeatures2(mySQL, array);
	}
	
	public String getValue(String pColumnName) {
		return getFeautureResult(fieldHm, pColumnName);
	}

    public String getTerminalsHTML(String pFind, String p_beg, String p_end) {
    	StringBuilder html = new StringBuilder();
	      Connection con = null;
	      PreparedStatement st = null;
	      boolean hasEditPermission = false;
	      boolean hasTerminalPermission = false;

	      ArrayList<bcFeautureParam> pParam = initParamArray();

	      String mySQL = 
	    	  " SELECT rn, id_term, term_card_req_club_pay_id, " +
	    	  "        DECODE(need_calc_pin, " +
    		  "               'Y', '<font color=\"red\"><b>'||need_calc_pin_tsl||'</b></font>', " +
    		  "               need_calc_pin_tsl" +
    		  "        ) need_calc_pin_tsl, " + 
			  "        DECODE(exist_term_online_pay_type, " +
			  "               'Y', '<b><font color=\"green\">'||exist_term_online_pay_type_tsl||'</font></b>', " +
			  "               '<b><font color=\"red\">'||exist_term_online_pay_type_tsl||'</font></b>'" +
			  "        ) exist_term_online_pay_type_tsl," +
			  "        id_term_online_pay_type " +
			  "   FROM (SELECT ROWNUM rn, id_term, term_card_req_club_pay_id, need_calc_pin, need_calc_pin_tsl, " +
			  "                exist_term_online_pay_type," +
			  "                exist_term_online_pay_type_tsl, id_term_online_pay_type " +
			  "           FROM (SELECT id_term, term_card_req_club_pay_id, " +
			  "                        need_calc_pin, need_calc_pin_tsl, exist_term_online_pay_type, " +
			  "                        exist_term_online_pay_type_tsl, id_term_online_pay_type " +
			  "                   FROM " + getGeneralDBScheme() + ".vc_term_online_pay_type_cl_all "+
			  "                  WHERE id_club_online_pay_type = ? ";
	      pParam.add(new bcFeautureParam("int", this.idOnlinePaymenttype));
	      
	      if (!isEmpty(pFind)) {
	    		mySQL = mySQL + 
	   				" AND (TO_CHAR(id_term) LIKE UPPER('%'||?||'%') OR " +
	   				"      UPPER(term_card_req_club_pay_id) LIKE UPPER('%'||?||'%'))";
	    		for (int i=0; i<2; i++) {
	    		    pParam.add(new bcFeautureParam("string", pFind));
	    		}
	      }
	      pParam.add(new bcFeautureParam("int", p_end));
	      pParam.add(new bcFeautureParam("int", p_beg));
	      mySQL = mySQL +
            "                  ORDER BY id_term)" +
            "          WHERE ROWNUM < ? " + 
            " ) WHERE rn >= ?";
	      try{
	    	  if (isEditPermited("CLUB_ONLINE_PAY_TYPE_TERM")>0) {
	    		  hasEditPermission = true;
	    	  }
	    	  if (isEditMenuPermited("CLIENTS_TERMINALS")>=0) {
	    		  hasTerminalPermission = true;
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
	               html.append(getBottomFrameTableTH(terminalXML, mtd.getColumnName(i)));
	          }
	          if (hasEditPermission) {
	            html.append("<th>&nbsp;</th><th>&nbsp;</th>\n");
	          }
	          html.append("</tr></thead><tbody>\n");
	          while (rset.next())
	          {
	        	  html.append("<tr>");
	          	  for (int i=1; i <= colCount-1; i++) {
	          		  if ("ID_TERM".equalsIgnoreCase(mtd.getColumnName(i)) && hasTerminalPermission) {
	          			  html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/clients/terminalspecs.jsp?id=" + rset.getString(i), "", ""));
	          		  } else {
	          			  html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", "", ""));
	          		  }
	          	  }
	            if (hasEditPermission) {
	            	String myHyperLink = "../crm/club/online_pay_typeupdate.jsp?id="+this.idOnlinePaymenttype+"&id_term_online_pay_type="+rset.getString("ID_TERM_ONLINE_PAY_TYPE") + "&type=term";
	            	String myDeleteLink = "../crm/club/online_pay_typeupdate.jsp?id="+this.idOnlinePaymenttype+"&id_term_online_pay_type="+rset.getString("ID_TERM_ONLINE_PAY_TYPE")+"&type=term&action=remove&process=yes";
	                html.append(getDeleteButtonHTML(myDeleteLink, buttonXML.getfieldTransl("delete", false), rset.getString("ID_TERM")));
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
    } // getCardRequestsHTML
    
}
