package bc.lists;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import BonCard.DataBase.UtilityConnector;
import bc.connection.Connector;
import bc.service.bcFeautureParam;


public class bcListTerminalOld extends bc.objects.bcObject {
	private final static Logger LOGGER=Logger.getLogger(bcListTerminalOld.class);
	
	public bcListTerminalOld(String pLanguage, String pSessionId, String pDateFormat) {
		setLanguage(pLanguage);
		setSessionId(pSessionId);
		setDateFormat(pDateFormat);
	}

	public String getTerminalsHTML(String pWhereCause, String pWhereValue, String pFindString, String p_beg, String p_end) {
	      StringBuilder html = new StringBuilder();
	      Connection con = null;
	      PreparedStatement st = null;
	      ResultSet rset=null;

	      List<bcFeautureParam> pParam = initParamArray();
	      StringBuilder mySQL =new StringBuilder();
	      
	      makeSQL(mySQL, pParam, pWhereCause, pWhereValue, pFindString, p_beg, p_end);
	      
	      try{
	    	  LOGGER.debug(prepareSQLToLog(mySQL.toString(), pParam));
	    	  con = Connector.getConnection(getSessionId());
	    	  st = con.prepareStatement(mySQL.toString());
	    	  prepareParam(st, pParam);
	    	  
	    	  rset = st.executeQuery();
	          
	          makeHTML(html, rset, rset.getMetaData());
	          
	      }
	      catch (SQLException e) {LOGGER.error(html, e); html.append(showCRMException(e));}
          catch (Exception el) {LOGGER.error(html, el); html.append(showCRMException(el));}
	      finally {
	    	  UtilityConnector.closeQuietly(rset);
	          UtilityConnector.closeQuietly(st);
	          Connector.closeConnection(con);
	        } // finally
	      return html.toString();
	  } // getSheduleTerminalsHTML

	
	private void makeHTML(StringBuilder html, ResultSet rset,
			ResultSetMetaData mtd) throws SQLException {
		
		int colCount = mtd.getColumnCount();
		
        html.append(getBottomFrameTable());
        html.append("<tr>");
        for (int i=1; i <= colCount-2; i++) {
             html.append(getBottomFrameTableTH(terminalXML, mtd.getColumnName(i)));
        }
        html.append("</tr></thead>\n");
        html.append("<tbody>\n");

        boolean hasTerminalPermission = isEditMenuPermited("CLIENTS_TERMINALS")>=0;
        boolean hasServicePlacePermission = isEditMenuPermited("CLIENTS_SERVICE_PLACE")>=0;
        boolean hasDeviceTypePermission = isEditMenuPermited("CLUB_TERM_DEVICE_TYPE")>=0;
        
        while (rset.next()) {
      	  	  html.append("<tr>");
        	  for (int i=1; i <= colCount-2; i++) {
        	  		if (hasTerminalPermission && "ID_TERM".equalsIgnoreCase(mtd.getColumnName(i))) {
        	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/clients/terminalspecs.jsp?id="+rset.getString(i), "", ""));
        	  			continue;
        	  		}
        	  		
        	  		if (hasServicePlacePermission && "NAME_SERVICE_PLACE".equalsIgnoreCase(mtd.getColumnName(i))) {
        	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/clients/service_placespecs.jsp?id="+rset.getString("ID_SERVICE_PLACE"), "", ""));
        	  			continue;
        	  		}  
        	  		if (hasDeviceTypePermission && "NAME_DEVICE_TYPE".equalsIgnoreCase(mtd.getColumnName(i)) &&
        	  				!(rset.getString("ID_DEVICE_TYPE")==null || "".equalsIgnoreCase(rset.getString("ID_DEVICE_TYPE")))) {
        	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/club/term_device_typespecs.jsp?id="+rset.getString("ID_DEVICE_TYPE"), "", ""));
        	  			continue;
        	  		} 
        	  		html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", "", ""));
        	  }
        	  html.append("</tr>\n");
        }
        html.append("</tbody></table>\n");
	}

	private void makeSQL(StringBuilder mySQL, List<bcFeautureParam> pParam, String pWhereCause, String pWhereValue, String pFindString, String p_beg, String p_end) {
	      mySQL
	      .append(" SELECT rn, id_term, name_term_type, name_device_type, name_term_status, name_service_place, ")
	      .append("        adr_full, date_location_frmt, date_extract_frmt, id_service_place, id_device_type ")
	      .append("   FROM (SELECT ROWNUM rn, id_term, name_term_type, name_device_type, name_term_status, name_service_place, ")
	      .append("                adr_full, date_location_frmt, date_extract_frmt, id_service_place, id_device_type ")
	      .append("   		FROM (SELECT s.id_term, s.name_term_type, s.name_device_type, ")
	      .append("						 DECODE(s.cd_term_status, ")
	      .append("                       		  'ACTIVE', '<b><font color=\"green\">'||s.name_term_status||'</font></b>', ")
	      .append("                       		  'SETTING', '<font color=\"red\">'||s.name_term_status||'</font>', " )
	      .append("                       		  'EXCLUDED', '<b><font color=\"red\">'||s.name_term_status||'</font></b>', " )
		  .append("                       		  'BLOCKED', '<b><font color=\"red\">'||s.name_term_status||'</font></b>', " )
		  .append("                       		  s.name_term_status" )
		  .append("                        ) name_term_status, " )
		  .append("						 s.id_service_place, s.name_service_place, " )
		  .append("                      	 s.adr_service_place adr_full, s.date_location_frmt, s.date_extract_frmt, s.id_device_type " )
		  .append("                  	FROM " + getGeneralDBScheme() + ".vc_term_all s ")
	      .append(pWhereCause);
	      
	      pParam.add(new bcFeautureParam("int", pWhereValue));
	      
	      fillByFindString(pFindString, mySQL, pParam);
	      
	      pParam.add(new bcFeautureParam("int", p_end));
	      pParam.add(new bcFeautureParam("int", p_beg));
	      
	      mySQL 
	           .append("                  ORDER BY s.id_term ")
	           .append("  		 ) WHERE ROWNUM < ? ") 
	           .append(" ) WHERE rn >= ?");
	}

	private void fillByFindString(String pFindString, StringBuilder mySQL,
			List<bcFeautureParam> pParam, String ... fieldNames) {
		String findString=StringUtils.trimToNull(pFindString);
		if(findString==null){
			return;
		}
  	  	mySQL.append(" AND (  TO_CHAR(id_term) LIKE UPPER('%'||?||'%')  ");
  	  	pParam.add(new bcFeautureParam("string", findString));
  	  	if(fieldNames!=null){
  	  		for(String eachField:fieldNames){
  	  			mySQL.append("      OR UPPER("+eachField+") LIKE UPPER('%'||?||'%')");
  	    	  	pParam.add(new bcFeautureParam("string", findString));
  	  		}
  	  	}
  	  	mySQL.append("     ) ");
		
	}


}
