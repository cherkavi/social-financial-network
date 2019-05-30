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
import bc.lists.bcListTerminal;
import bc.service.bcFeautureParam;


public class bcLoySheduleObject extends bcObject {
	private final static Logger LOGGER=Logger.getLogger(bcLoySheduleObject.class);
	private Map<String,Comparable<String>> fieldHm = new HashMap<String, Comparable<String>>();
	
	private String idSchdule;
	
	public bcLoySheduleObject(String pIdSchedule) {
		this.idSchdule = pIdSchedule;
		getFeature();
	}
	
	private void getFeature() {
		String featureSelect = " SELECT * FROM " + getGeneralDBScheme() + ".V_LS_SHEDULE_NAME_CLUB_ALL WHERE id_shedule = ?";
		fieldHm = getFeatures2(featureSelect, this.idSchdule, false);
	}
	
	public String getValue(String pColumnName) {
		return getFeautureResult(fieldHm, pColumnName);
	}

	public String getSheduleLinesHTML(String pFindString, String pTypeSchedule, String p_beg, String p_end) {
		StringBuilder html = new StringBuilder();
		Connection con = null;
		PreparedStatement st = null;
		boolean hasEditPerm = false;
		boolean hasShemePermission = false;

		ArrayList<bcFeautureParam> pParam = initParamArray();

		String mySQL = 
	    	  " SELECT line_number, name_loyality_scheme, date_beg, date_end, type_shedule, " +
              "        day_time_beg, day_time_end, exist_flag_tsl, id_loyality_scheme, id_shedule_line "+
              "   FROM (SELECT ROWNUM rn, name_loyality_scheme, line_number, date_beg_frmt date_beg, "+
              "        		   date_end_frmt date_end, type_shedule, " +
              "        		   day_time_beg, day_time_end, " +
              "                DECODE(exist_flag, " +
    		  "                       'N', '<font color=\"red\"><b>'||exist_flag_tsl||'</b></font>', " +
    		  "                       '<font color=\"green\"><b>'||exist_flag_tsl||'</b></font>'" +
    		  "                ) exist_flag_tsl, id_loyality_scheme, id_shedule_line "+
              "   		  FROM (SELECT * " +
              "                   FROM " + getGeneralDBScheme() + ".vc_ls_shedule_all "+
              "  		         WHERE id_shedule = ? ";
		pParam.add(new bcFeautureParam("int", this.idSchdule));
		
		if (!isEmpty(pFindString)) {
			mySQL = mySQL + 
				" AND (TO_CHAR(line_number) LIKE UPPER('%'||?||'%') OR " +
				"      UPPER(name_loyality_scheme) LIKE UPPER('%'||?||'%') OR " +
				"      UPPER(date_beg_frmt) LIKE UPPER('%'||?||'%') OR " +
				"      UPPER(date_end_frmt) LIKE UPPER('%'||?||'%') OR " +
				"      UPPER(day_time_beg) LIKE UPPER('%'||?||'%') OR " +
				"      UPPER(day_time_end) LIKE UPPER('%'||?||'%'))";
			for (int i=0; i<6; i++) {
			    pParam.add(new bcFeautureParam("string", pFindString));
			}
		}
		if (!isEmpty(pTypeSchedule)) {
			mySQL = mySQL + " AND type_shedule = ? ";
			pParam.add(new bcFeautureParam("string", pTypeSchedule));
		}
		pParam.add(new bcFeautureParam("int", p_end));
		pParam.add(new bcFeautureParam("int", p_beg));
		mySQL = mySQL +
			"                  ORDER BY line_number)" +
			"          WHERE ROWNUM < ? " + 
			" ) WHERE rn >= ?";
		try{
	    	  if (isEditPermited("CLIENTS_SHEDULE_LINES") >0) {
	    		  hasEditPerm = true;
	    	  }
	    	  if (isEditMenuPermited("CLIENTS_LOY") >0) {
	    		  hasShemePermission = true;
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
	        	  html.append(getBottomFrameTableTH(shedulelineXML, mtd.getColumnName(i)));
	          }
	          if (hasEditPerm) {
	        	  html.append("<th>&nbsp;</th><th>&nbsp;</th>\n");
	          }
	          html.append("</tr></thead><tbody>");
	          
	          while (rset.next())
	          {
	        	  html.append("<tr>");
	          	  for (int i=1; i <= colCount-3; i++) {
	          	  		if (hasShemePermission && "NAME_LOYALITY_SCHEME".equalsIgnoreCase(mtd.getColumnName(i))) {
	          	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/clients/loyspecs.jsp?id="+rset.getString("ID_LOYALITY_SCHEME"), "", ""));
	         	  		} else if ("TYPE_SHEDULE".equalsIgnoreCase(mtd.getColumnName(i))) {
	          	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), shedulelineXML.getfieldTransl( rset.getString(i), false), "", "", ""));
	         	  		} else {
	          	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", "", ""));
	          	  		}
	          	  }
	                      
	              if (hasEditPerm) {
	            	  String myHyperLink = "../crm/clients/sheduleupdate.jsp?type=line&id="+this.idSchdule+"&id_line="+rset.getString("ID_SHEDULE_LINE");
	            	  String myDeleteLink = "../crm/clients/sheduleupdate.jsp?type=line&id="+this.idSchdule+"&id_line="+rset.getString("ID_SHEDULE_LINE")+"&action=remove&process=yes";
			          html.append(getDeleteButtonHTML(myDeleteLink, sheduleXML.getfieldTransl("h_remove_line", false), rset.getString("LINE_NUMBER")));
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
	  } // getSheduleLinesHTML

	  public String getTerminalsHTML(String pFindString, String pTermType, String pTermStatus, String p_beg, String p_end) {
	      bcListTerminal list = new bcListTerminal();
	      
	      String pWhereCause = " WHERE s.id_loyality_shedule = ? ";
	    	
	      ArrayList<bcFeautureParam> pWhereValue = new ArrayList<bcFeautureParam>();
	      pWhereValue.add(new bcFeautureParam("int", this.idSchdule));
	      
	      return list.getTerminalsHTML(pWhereCause, pWhereValue, pFindString, pTermType, pTermStatus, "", "", "", p_beg, p_end);
	  }

}
