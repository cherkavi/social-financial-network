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


public class bcLoySchemeObject extends bcObject {
	private final static Logger LOGGER=Logger.getLogger(bcLoySchemeObject.class);
	private Map<String,Comparable<String>> fieldHm = new HashMap<String, Comparable<String>>();
	
	private String idScheme;
	
	public bcLoySchemeObject(String pIDLoyalityScheme) {
		this.idScheme = pIDLoyalityScheme;
		getFeature();
	}
	
	private void getFeature() {
		String featureSelect = " SELECT * FROM " + getGeneralDBScheme() + ".VC_LOYALITY_SCHEME_CLUB_ALL WHERE id_loyality_scheme = ?";
		fieldHm = getFeatures2(featureSelect, this.idScheme, true);
	}
	
	public String getValue(String pColumnName) {
		return getFeautureResult(fieldHm, pColumnName);
	}
	public String getLoyalityKindOnlyCDOptions(String cd_kind, boolean isNull) {
    	return getSelectBodyFromQuery(
    			" SELECT cd_kind_loyality, cd_kind_loyality name_kind_loyality " +
    			"   FROM " + getGeneralDBScheme() + ".vc_loyality_kind_names " +
    			"  ORDER BY cd_kind_loyality", cd_kind, isNull);
     } // getLoyalityKindOnlyCDOptions()
	
	public String getClubCardStatusOptions(String id_status, boolean isNull) {
    	return getSelectBodyFromQuery(
    			" SELECT id_card_status, name_card_status " +
    			"   FROM " + getGeneralDBScheme() + ".vc_card_status", id_status, isNull);
     } // getClubCardStatusOptions()
	
	public String getClubCardCategoryOptions(String id_category, boolean isNull) {
    	return getSelectBodyFromQuery(
    			" SELECT id_category_name, name_category_name " +
    			"   FROM " + getGeneralDBScheme() + ".vc_card_category_name_all " +
    			"  WHERE exist_flag = 'Y'", id_category, isNull);
     } // getClubCardCategoryOptions()
	
	public String getStandardLoyalityTerminalsHTML(String id_scheme) {
	      StringBuilder html = new StringBuilder();
	      Connection con = null;
	      PreparedStatement st = null;
	      String mySQL = 
    		  " SELECT id_term, name_device_type, name_term_status, jur_prs_name name_jur_prs, adr_full, " +
    		  "	 	   name_finance_acquirer, date_location_frmt, date_extract_frmt " +
    		  "   FROM " + getGeneralDBScheme() + ".vc_term_all "+
    		  "  WHERE id_loyality_scheme = ? ";
		  boolean hasTerminalPermission = false;
	      try{
		      if (isEditMenuPermited("CLIENTS_TERMINALS")>=0) {
		    	  hasTerminalPermission = true;
		      }
	    	  	    	  
	    	  LOGGER.debug(mySQL + 
	            		", 1={" + id_scheme + ",int}");
	    	  con = Connector.getConnection(getSessionId());
	    	  st = con.prepareStatement(mySQL);
	    	  st.setInt(1, Integer.parseInt(id_scheme));
	    	  ResultSet rset = st.executeQuery();
	          ResultSetMetaData mtd = rset.getMetaData();	          
	          int colCount = mtd.getColumnCount();
	          
	          html.append(getBottomFrameTable());
	          html.append("<tr>");
	          for (int i=1; i <= colCount; i++) {
	             html.append(getBottomFrameTableTH(terminalXML, mtd.getColumnName(i)));
	          }
	          html.append("</tr></thead><tbody>\n");
	          
	          while (rset.next())
	          {
	          	  	html.append("<tr>");
	          	  	for (int i=1; i <= colCount; i++) {
	          	  		if (hasTerminalPermission && "ID_TERM".equalsIgnoreCase(mtd.getColumnName(i))) {
	          	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/clients/terminalspecs.jsp?id="+rset.getString(i), "", ""));
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
	  } // getStandardLoyalityTerminalsHTML

    public String getLoyalityLinesHTML(String pFindString, String pCdLoyalityKind, String pIdCardStatus, String pIdCategory, String p_beg, String p_end) {
    	ArrayList<bcFeautureParam> pParam = initParamArray();

    	String mySQL = 
    		" SELECT rn, cd_kind_loyality, name_card_status, name_category, " +
    		"        opr_sum_frmt, bon_percent_value_percent, bon_fixed_value_frmt, " +
    		"        max_bon_st_frmt, bonus_transfer_term, disc_percent_value_percent, disc_fixed_value_frmt, " +
    		"        max_disc_st_frmt, bonus_calc_term, nullperiod_flag_tsl, active_tsl, id_loyality_scheme, id_line " +
    		"   FROM ( " + 
    		" SELECT ROWNUM rn, id_line, id_loyality_scheme, cd_kind_loyality, name_card_status, name_category, " +
        	"        opr_sum_frmt, bon_percent_value_percent, bon_fixed_value_frmt, " +
        	"        max_bon_st_frmt, bonus_transfer_term, disc_percent_value_percent, disc_fixed_value_frmt, " +
        	"        max_disc_st_frmt, bonus_calc_term, " +
        	"        DECODE(nullperiod_flag, 0, '<b><font color=\"red\">'||nullperiod_flag_tsl||'</font><b>', nullperiod_flag_tsl) nullperiod_flag_tsl, " +
        	"        DECODE(active, 0, '<b><font color=\"red\">'||active_tsl||'</font><b>', active_tsl) active_tsl, opr_sum " +
        	"   FROM (SELECT * " +
        	"           FROM " + getGeneralDBScheme() + ".vc_loyality_scheme_lines_all " +
        	"          WHERE id_loyality_scheme = ? ";
    	pParam.add(new bcFeautureParam("int", this.idScheme));
    	
        if (!isEmpty(pFindString)) {
    		mySQL = mySQL + 
   				" AND (TO_CHAR(id_line) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(opr_sum_frmt) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(bon_percent_value_percent) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(bon_fixed_value_frmt) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(max_bon_st_frmt) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(bonus_transfer_term) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(disc_percent_value_percent) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(disc_fixed_value_frmt) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(max_disc_st_frmt) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(bonus_calc_term) LIKE UPPER('%'||?||'%'))";
    		for (int i=0; i<10; i++) {
    		    pParam.add(new bcFeautureParam("string", pFindString));
    		}
    	}
    	if (!isEmpty(pCdLoyalityKind)) {
    		mySQL = mySQL +" AND cd_kind_loyality = ? ";
    		pParam.add(new bcFeautureParam("string", pCdLoyalityKind));

    	}
    	if (!isEmpty(pIdCardStatus)) {
    		mySQL = mySQL +" AND id_card_status = ? ";
    		pParam.add(new bcFeautureParam("int", pIdCardStatus));
    	}
    	if (!isEmpty(pIdCategory)) {
    		mySQL = mySQL +" AND id_category = ? ";
    		pParam.add(new bcFeautureParam("int", pIdCategory));
    	}

    	pParam.add(new bcFeautureParam("int", p_end));
    	pParam.add(new bcFeautureParam("int", p_beg));
    	mySQL = mySQL +
    		"  ORDER BY id_loyality_scheme, cd_kind_loyality, name_card_status, name_category, opr_sum " + 
    		" ) WHERE ROWNUM < ? " + 
    		" ) WHERE rn >= ?";
    	
        StringBuilder html = new StringBuilder();
        Connection con = null;
        PreparedStatement st = null;
        boolean hasEditPermission = false;
        try{
        	if (isEditPermited("CLIENTS_LOY_BON")>0) {
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
            for (int i=1; i <= colCount-2; i++) {
            	if ("OPR_SUM_FRMT".equalsIgnoreCase(mtd.getColumnName(i)) ||
            			"MAX_BON_ST_FRMT".equalsIgnoreCase(mtd.getColumnName(i)) ||
            			"MAX_DISC_ST_FRMT".equalsIgnoreCase(mtd.getColumnName(i))) {
            		html.append(getBottomFrameTableTH3(loylineXML, mtd.getColumnName(i), "", ", " + this.getValue("SNAME_CURRENCY").toLowerCase()));
            	} else {
            		html.append(getBottomFrameTableTH(loylineXML, mtd.getColumnName(i)));
            	}
            	//getBottomFrameTableTH3
            }
            if (hasEditPermission) {
            	html.append("<th>&nbsp;</th><th>&nbsp;</th>\n");
            }
            html.append("</tr></thead><tbody>\n");
            while (rset.next())
            {
                html.append("<tr>\n");
                for (int i=1; i<=colCount-2; i++) {
                	html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", "", ""));
                }
                if (hasEditPermission) {
	            	 String myHyperLink = "../crm/clients/loylineupdate.jsp?type=loylines&id_loyality_scheme="+rset.getString("ID_LOYALITY_SCHEME")+"&id_line="+rset.getString("ID_LINE");
	            	 String myDeleteLink = "../crm/clients/loylineupdate.jsp?type=loylines&id_loyality_scheme="+rset.getString("ID_LOYALITY_SCHEME")+"&id_line="+rset.getString("ID_LINE")+"&action=remove&process=yes";
		             html.append(getDeleteButtonHTML(myDeleteLink, loylineXML.getfieldTransl("h_loyality_delete", false), rset.getString("ID_LINE")));
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
    } //getLoyalityLinesHTML()

	public String getTerminalsHTML(String pFindString, String pTermType, String pTermStatus, String p_beg, String p_end) {
		bcListTerminal list = new bcListTerminal();
	      
	    String pWhereCause = " WHERE s.id_loyality_scheme_next = ? ";
	    	
	    ArrayList<bcFeautureParam> pWhereValue = new ArrayList<bcFeautureParam>();
	    pWhereValue.add(new bcFeautureParam("int", this.idScheme));
	      
	    return list.getTerminalsHTML(pWhereCause, pWhereValue, pFindString, pTermType, pTermStatus, "", "", "", p_beg, p_end);
	}
    
}
