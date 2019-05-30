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

public class bcDsPartnerProfileObject extends bcObject {
	private final static Logger LOGGER=Logger.getLogger(bcDsPartnerProfileObject.class);
	
	private Map<String,Comparable<String>> fieldHm = new HashMap<String, Comparable<String>>();
	
	private String idDispatchPartnerProfile;
	
	public bcDsPartnerProfileObject(String pIdDispatchPartnerProfile){
		this.idDispatchPartnerProfile = pIdDispatchPartnerProfile;
		getFeature();
	}
	
	private void getFeature() {
		String featureSelect = " SELECT * FROM " + getGeneralDBScheme() + ".VC_DS_PT_PROFILE_CLUB_ALL WHERE id_ds_pt_profile = ?";
		fieldHm = getFeatures2(featureSelect, this.idDispatchPartnerProfile, false);
	}
	
	public String getValue(String pColumnName) {
		return getFeautureResult(fieldHm, pColumnName);
	}

    public String getPatternsHTML(String pFindString, String p_beg, String p_end) {
    	StringBuilder html = new StringBuilder();
    	Connection con = null;
    	PreparedStatement st = null;
	    boolean hasPatternPermission = false;

	    ArrayList<bcFeautureParam> pParam = initParamArray();

	    String mySQL = 
	    		" SELECT rn, id_pt_pattern id_pattern, name_pt_pattern name_pattern, name_pattern_status, " +
	    		"        name_pattern_type, name_dispatch_type, begin_action_date_frmt, end_action_date_frmt, " +
				"		 can_send_partner_tsl " +
				"   FROM (SELECT ROWNUM rn, id_pt_pattern, name_pt_pattern, name_pattern_status, name_pattern_type, " +
				"				 name_dispatch_type, begin_action_date_frmt, end_action_date_frmt, " +
				"				 can_send_partner_tsl " +
				"           FROM (SELECT a.id_pt_pattern, a.name_pt_pattern, a.name_pattern_status, a.name_pattern_type, " + 
				"						 a.name_dispatch_type, a.begin_action_date_frmt, a.end_action_date_frmt, " + 
				"						 DECODE(a.can_send_email, " +
				"                               'N', '<font color=\"black\">'||a.can_send_email_tsl||'</font>', " +
				"                               'Y', '<font color=\"red\"><b>'||a.can_send_email_tsl||'</b></font>', " +
				"                               a.can_send_email_tsl)||'/'||" +
				"                        DECODE(a.can_send_office, " +
				"                               'N', '<font color=\"black\">'||a.can_send_office_tsl||'</font>', " +
				"                               'Y', '<font color=\"red\"><b>'||a.can_send_office_tsl||'</b></font>', " +
				"                               a.can_send_office_tsl)||'/'||" +
				"                        DECODE(a.can_send_term, " +
				"                               'N', '<font color=\"black\">'||a.can_send_term_tsl||'</font>', " +
				"                               'Y', '<font color=\"red\"><b>'||a.can_send_term_tsl||'</b></font>', " +
				"                               a.can_send_term_tsl) can_send_partner_tsl " +
				"                   FROM " + getGeneralDBScheme()+".vc_ds_pt_pattern_club_all a " +
				"                  WHERE a.id_ds_pt_profile = ? ";
	    pParam.add(new bcFeautureParam("int", this.idDispatchPartnerProfile));
	    
    	if (!isEmpty(pFindString)) {
    		mySQL = mySQL + 
   				" AND (TO_CHAR(id_pt_pattern) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(name_pt_pattern) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(begin_action_date_frmt) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(end_action_date_frmt) LIKE UPPER('%'||?||'%'))";
    		for (int i=0; i<4; i++) {
    		    pParam.add(new bcFeautureParam("string", pFindString));
    		}
    	}
    	pParam.add(new bcFeautureParam("int", p_end));
    	pParam.add(new bcFeautureParam("int", p_beg));
	    mySQL = mySQL +
	    		" 				   ORDER BY a.name_pt_pattern) " +
	    		"          WHERE ROWNUM < ? " +
	    		"		 ) " +
	    		"  WHERE rn >= ?";
	    try{
	    	if (isEditMenuPermited("DISPATCH_PATTERNS_PARTNER")>=0) {
	    		hasPatternPermission = true;
        	}
	    	
	    	LOGGER.debug(prepareSQLToLog(mySQL, pParam));
	    	con = Connector.getConnection(getSessionId());
	    	st = con.prepareStatement(mySQL);
	    	st = prepareParam(st, pParam);
	    	ResultSet rset = st.executeQuery();
            ResultSetMetaData mtd = rset.getMetaData();
            
            int colCount = mtd.getColumnCount();

            html.append(getBottomFrameTable());
	        html.append("<tr>\n");
	        for (int i=1; i <= colCount; i++) {
	        	html.append(getBottomFrameTableTH(messageXML, mtd.getColumnName(i)));
            }
	        html.append("</tr></thead><tbody>");
	        
	        while (rset.next()) {
	        	html.append("<tr>\n");
		        for (int i=1; i <= colCount; i++) {
	        		if ("ID_PATTERN".equalsIgnoreCase(mtd.getColumnName(i)) &&
	        				hasPatternPermission) {
	        			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/dispatch/patterns/partner_patternspecs.jsp?id="+rset.getString(i), "", ""));
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
	  } // bcDsPartnerProfileObject
	
	public String getContactPrsListHTML(String pFindString, String pCdPost, String pHasJoin, String p_beg, String p_end) {
        StringBuilder html = new StringBuilder();
        PreparedStatement st = null;
        Connection con = null;

        ArrayList<bcFeautureParam> pParam = initParamArray();

        String mySQL = 
        	" SELECT rn, id_contact_prs, name_contact_prs, sname_jur_prs, " +
        	"        name_service_place, name_post, " +
        	"        desc_contact_prs, email, has_join, id_jur_prs, id_service_place " +
            "   FROM (SELECT ROWNUM rn, id_contact_prs, name_contact_prs, sname_jur_prs, " +
        	"                name_service_place, name_post, " +
        	"                desc_contact_prs, email, has_join, id_jur_prs, id_service_place " +
            "           FROM (SELECT id_contact_prs, name_contact_prs, sname_jur_prs, " +
        	"                        name_service_place, name_post, " +
        	"                        desc_contact_prs, email, has_join, id_jur_prs, id_service_place " +
        	"                   FROM (SELECT a.id_contact_prs, a.name_contact_prs, a.sname_jur_prs, " +
        	"                                a.name_service_place, a.name_post, " +
        	"                                a.desc_contact_prs, a.email, a.id_jur_prs, a.id_service_place, " +
        	"                                DECODE(p.id_contact_prs, NULL, 'N', 'Y') has_join " +
        	"                           FROM (SELECT *" +
        	"                                   FROM " + getGeneralDBScheme()+".vc_contact_prs_priv_all " +
        	"                                  WHERE 1=1 ";
    	if (!isEmpty(pFindString)) {
    		mySQL = mySQL + 
   				" AND (TO_CHAR(id_contact_prs) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(name_contact_prs) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(sname_jur_prs) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(name_service_place) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(desc_contact_prs) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(email) LIKE UPPER('%'||?||'%'))";
    		for (int i=0; i<6; i++) {
    		    pParam.add(new bcFeautureParam("string", pFindString));
    		}
    	}
        if (!(pCdPost== null || "".equalsIgnoreCase(pCdPost))) {
           	mySQL = mySQL + " AND cd_post = ? ";
           	pParam.add(new bcFeautureParam("string", pCdPost));
        }	
        mySQL = mySQL + 
            "                                  ORDER BY name_contact_prs) a left JOIN " +
        	"                                (SELECT id_contact_prs " +
        	"                                   FROM " + getGeneralDBScheme()+".vc_ds_pt_profile_cont_prs_sh " +
        	"                                  WHERE id_ds_pt_profile = ?) p" +
        	"                             ON (a.id_contact_prs = p.id_contact_prs)" +
        	"                        ) ";
        pParam.add(new bcFeautureParam("int", this.idDispatchPartnerProfile));
        
        if (!(pHasJoin== null || "".equalsIgnoreCase(pHasJoin))) {
           	mySQL = mySQL + " WHERE has_join = ? ";
           	pParam.add(new bcFeautureParam("string", pHasJoin));
        }
        pParam.add(new bcFeautureParam("int", p_end));
        pParam.add(new bcFeautureParam("int", p_beg));
    	mySQL = mySQL + 
        	"              ORDER BY name_contact_prs) WHERE ROWNUM < ? " + 
            "       ) WHERE rn >= ?";
        
        boolean hasPartnerPermission = false;
        boolean hasServicePlacePermission = false;
        boolean hasContactPrsPermission = false;
        
        boolean hasEditPermission = false;
        String myFont = "";
        String myBGColor = "";
        
        try{
        	if (isEditMenuPermited("CLIENTS_YURPERSONS")>=0) {
        		hasPartnerPermission = true;
            }
        	if (isEditMenuPermited("CLIENTS_SERVICE_PLACE")>=0) {
        		hasServicePlacePermission = true;
            }
        	if (isEditMenuPermited("CLIENTS_CONTACT_PRS")>=0) {
        		hasContactPrsPermission = true;
            }
        	
        	if (isEditPermited("DISPATCH_SETTINGS_PARTNER_PROFILE_CONTACT_PRS")>0) {
       	 		hasEditPermission = true;
       	 	}
        	
       	 	LOGGER.debug(prepareSQLToLog(mySQL, pParam));
       	 	con = Connector.getConnection(getSessionId());
       	 	st = con.prepareStatement(mySQL);
       	 	st = prepareParam(st, pParam);
       	 	ResultSet rset = st.executeQuery();
            ResultSetMetaData mtd = rset.getMetaData();
            int colCount = mtd.getColumnCount();

            if (hasEditPermission) {
           	 	html.append("<form method=\"post\" name=\"updateForm\" id=\"updateForm\" action=\"../crm/dispatch/settings/partner_profileupdate.jsp\">\n");
           	 	html.append("<input type=\"hidden\" name=\"type\" value=\"contact_prs\">\n");
        	 	html.append("<input type=\"hidden\" name=\"action\" value=\"set\">\n");
           	 	html.append("<input type=\"hidden\" name=\"process\" value=\"yes\">\n");
           	 	html.append("<input type=\"hidden\" name=\"id\" value=\"" + this.idDispatchPartnerProfile + "\">\n");
            }
            
            html.append(getBottomFrameTable());
            html.append("<tr>");
            for (int i=1; i <= colCount-3; i++) {
               html.append(getBottomFrameTableTH(contactXML, mtd.getColumnName(i)));
            }
            if (hasEditPermission) {
            	html.append("<th align=\"center\"> "+ 
           			messageXML.getfieldTransl("has_join", false) +
           			"<br><input id=\"mainCheck\" name=\"mainCheck\" type=\"checkbox\" onclick=\"CheckAll(this)\"></th>\n");
            }
            html.append("</tr>");
            html.append("</thead><tbody>\n");
            if (hasEditPermission) {
             	html.append("<tr>");
             	html.append("<td colspan=\"9\" align=\"center\">");
             	html.append(getSubmitButtonAjax("../crm/dispatch/settings/partner_profileupdate.jsp"));
             	html.append("</td>");
             	html.append("</tr>");
             }
            while (rset.next())
            {
                String idContactPrs=rset.getString("ID_CONTACT_PRS");
                String id = "id_" + idContactPrs;
                String tprvCheck="prv_"+id;
                String tCheck="chb_"+id;
                
                if ("Y".equalsIgnoreCase(rset.getString("HAS_JOIN"))){
                	myFont = "<b>";
                	myBGColor = selectedBackGroundStyle;
                } else {
                	myFont = "";
                	myBGColor = "";
                }
                html.append("<tr>");
                
                for (int i=1; i <= colCount-3; i++) {
                	if (hasContactPrsPermission && "ID_CONTACT_PRS".equalsIgnoreCase(mtd.getColumnName(i))) {
        				html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/clients/contact_prsspecs.jsp?id="+rset.getString("ID_CONTACT_PRS"), myFont, myBGColor));
        			} else if (hasPartnerPermission && "SNAME_JUR_PRS".equalsIgnoreCase(mtd.getColumnName(i))) {
        				html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/clients/yurpersonspecs.jsp?id="+rset.getString("ID_JUR_PRS"), myFont, myBGColor));
        			} else if (hasServicePlacePermission && "NAME_SERVICE_PLACE".equalsIgnoreCase(mtd.getColumnName(i))) {
        				html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/clients/service_placespecs.jsp?id="+rset.getString("ID_SERVICE_PLACE"), myFont, myBGColor));
        			} else {
        				html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", myFont, myBGColor));
    	            }
                }
                
                if (hasEditPermission) {
                	html.append("<td " + myBGColor + ">");
                	if ("Y".equalsIgnoreCase(rset.getString("HAS_JOIN"))){
                		html.append("<INPUT type=hidden  value=\"Y\" name="+tprvCheck+">");
                    	html.append("<INPUT  type=\"checkbox\" value = \"Y\" name="+tCheck+" id="+tCheck+" checked onclick=\"return CheckCB(this);\"></td>\n");
                	} else {
                    	html.append("<INPUT  type=\"checkbox\" value = \"Y\" name="+tCheck+" id="+tCheck+" onclick=\"return CheckCB(this);\"></td>\n");
                	}
                	html.append("</td>");
                }
                html.append("</tr>\n");
            }
            if (hasEditPermission) {
             	html.append("<tr>\n");
             	html.append("<td colspan=\"9\" align=\"center\">\n");
             	html.append(getSubmitButtonAjax("../crm/dispatch/settings/partner_profileupdate.jsp"));
             	html.append("</td>\n");
             	html.append("</tr>\n");
             }
            html.append("</tbody></table></form>\n");
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
    }
	
	public String getTerminalsListHTML(String pFindString, String pHasJoin, String p_beg, String p_end) {
        StringBuilder html = new StringBuilder();
        PreparedStatement st = null;
        Connection con = null;

        ArrayList<bcFeautureParam> pParam = initParamArray();

        String mySQL = 
        	" SELECT rn, id_term, name_term_type, name_device_type, name_term_status, name_service_place, " +
	        "        adr_full, date_location, date_extract, has_join, id_service_place, id_device_type " +
            "   FROM (SELECT ROWNUM rn, id_term, name_term_type, name_device_type, name_term_status, name_service_place, " +
	        "                adr_full, date_location, date_extract, has_join, id_service_place, id_device_type " +
            "           FROM (SELECT id_term, name_term_type, name_device_type, name_term_status, name_service_place, " +
	        "                        adr_full, date_location, date_extract, has_join, id_service_place, id_device_type " +
        	"                   FROM (SELECT a.id_term, a.name_term_type, a.name_device_type, " +
        	"  				       	  		 DECODE(a.cd_term_status, " +
			"                       		  		'ACTIVE', '<b><font color=\"green\">'||a.name_term_status||'</font></b>', " +
			"                       		  		'SETTING', '<font color=\"red\">'||a.name_term_status||'</font>', " +
			"                       		  		'EXCLUDED', '<b><font color=\"red\">'||a.name_term_status||'</font></b>', " +
			"                       		  		'BLOCKED', '<b><font color=\"red\">'||a.name_term_status||'</font></b>', " +
			"                       		  		a.name_term_status" +
			"                        		 ) name_term_status, " +
  		  	"								 a.id_service_place, a.name_service_place, " +
	        "                                a.adr_full, a.date_location, a.date_extract, " +
        	"                                DECODE(p.id_term, NULL, 'N', 'Y') has_join, a.id_device_type " +
        	"                           FROM (SELECT s.id_term, s.name_term_type, s.name_device_type, s.cd_term_status, s.name_term_status, s.id_service_place, s.name_service_place, " +
	        "                                    	 s.adr_full, s.date_location_frmt date_location, s.date_extract_frmt date_extract, s.id_device_type " +
	        "                               	FROM " + getGeneralDBScheme() + ".vc_term_service_places_sh_all s " +
	        "                                  WHERE 1=1 ";
    	if (!isEmpty(pFindString)) {
    		mySQL = mySQL + 
   				" AND (TO_CHAR(id_term) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(name_service_place) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(adr_full) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(date_location) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(date_extract) LIKE UPPER('%'||?||'%'))";
    		for (int i=0; i<5; i++) {
    		    pParam.add(new bcFeautureParam("string", pFindString));
    		}
    	}
        mySQL = mySQL + 
            "                                  ORDER BY id_term) a left JOIN " +
        	"                                (SELECT id_term, id_service_place " +
        	"                                   FROM " + getGeneralDBScheme()+".vc_ds_pt_profile_term_sh_all " +
        	"                                  WHERE id_ds_pt_profile = ?) p" +
        	"                             ON (a.id_term = p.id_term AND" +
        	"                                 a.id_service_place = p.id_service_place)" +
        	"                        ) ";
        pParam.add(new bcFeautureParam("int", this.idDispatchPartnerProfile));
        
        if (!(pHasJoin== null || "".equalsIgnoreCase(pHasJoin))) {
           	mySQL = mySQL + " WHERE has_join = ? ";
           	pParam.add(new bcFeautureParam("string", pHasJoin));
        }
        pParam.add(new bcFeautureParam("int", p_end));
        pParam.add(new bcFeautureParam("int", p_beg));
    	mySQL = mySQL + 
        	"              ORDER BY id_term) WHERE ROWNUM < ? " +  
            "       ) WHERE rn >= ?";
        
        boolean hasTermPermission = false;
        boolean hasServicePlacePermission = false;
        boolean hasDeviceTypePermission = false;
        
        boolean hasEditPermission = false;
        String myFont = "";
        String myBGColor = "";
        
        try{
        	if (isEditMenuPermited("CLIENTS_TERMINALS")>=0) {
        		hasTermPermission = true;
        	}
        	if (isEditMenuPermited("CLIENTS_SERVICE_PLACE")>=0) {
        		hasServicePlacePermission = true;
        	}
        	if (isEditMenuPermited("CLUB_TERM_DEVICE_TYPE")>=0) {
        		hasDeviceTypePermission = true;
        	}
        	
        	if (isEditPermited("DISPATCH_SETTINGS_PARTNER_PROFILE_TERMINALS")>0) {
       	 		hasEditPermission = true;
       	 	}
        	
       	 	LOGGER.debug(prepareSQLToLog(mySQL, pParam));
       	 	con = Connector.getConnection(getSessionId());
       	 	st = con.prepareStatement(mySQL);
       	 	st = prepareParam(st, pParam);
       	 	ResultSet rset = st.executeQuery();
            ResultSetMetaData mtd = rset.getMetaData();
            int colCount = mtd.getColumnCount();

            if (hasEditPermission) {
           	 	html.append("<form method=\"post\" name=\"updateForm\" id=\"updateForm\" action=\"../crm/dispatch/settings/partner_profileupdate.jsp\">\n");
           	 	html.append("<input type=\"hidden\" name=\"type\" value=\"terminal\">\n");
        	 	html.append("<input type=\"hidden\" name=\"action\" value=\"set\">\n");
           	 	html.append("<input type=\"hidden\" name=\"process\" value=\"yes\">\n");
           	 	html.append("<input type=\"hidden\" name=\"id\" value=\"" + this.idDispatchPartnerProfile + "\">\n");
            }
            
            html.append(getBottomFrameTable());
            html.append("<tr>");
            for (int i=1; i <= colCount-3; i++) {
               html.append(getBottomFrameTableTH(terminalXML, mtd.getColumnName(i)));
            }
            if (hasEditPermission) {
            	html.append("<th align=\"center\"> "+ 
           			messageXML.getfieldTransl("has_join", false) +
           			"<br><input id=\"mainCheck\" name=\"mainCheck\" type=\"checkbox\" onclick=\"CheckAll(this)\"></th>\n");
            }
            html.append("</tr>");
            html.append("</thead><tbody>\n");
            if (hasEditPermission) {
             	html.append("<tr>");
             	html.append("<td colspan=\"10\" align=\"center\">");
             	html.append(getSubmitButtonAjax("../crm/dispatch/settings/partner_profileupdate.jsp"));
             	html.append("</td>");
             	html.append("</tr>");
             }
            while (rset.next())
            {
                String idContactPrs=rset.getString("ID_TERM") + "_" + rset.getString("ID_SERVICE_PLACE");
                String id = "id_" + idContactPrs;
                String tprvCheck="prv_"+id;
                String tCheck="chb_"+id;
                
                if ("Y".equalsIgnoreCase(rset.getString("HAS_JOIN"))){
                	myFont = "<b>";
                	myBGColor = selectedBackGroundStyle;
                } else {
                	myFont = "";
                	myBGColor = "";
                }
                html.append("<tr>");
                
                for (int i=1; i <= colCount-3; i++) {
                	if (hasTermPermission && "ID_TERM".equalsIgnoreCase(mtd.getColumnName(i))) {
        				html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/clients/terminalspecs.jsp?id="+rset.getString("ID_TERM"), myFont, myBGColor));
                	} else if (hasServicePlacePermission && "NAME_SERVICE_PLACE".equalsIgnoreCase(mtd.getColumnName(i))) {
        				html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/clients/service_placespecs.jsp?id="+rset.getString("ID_SERVICE_PLACE"), myFont, myBGColor));
                	} else if (hasDeviceTypePermission && "NAME_DEVICE_TYPE".equalsIgnoreCase(mtd.getColumnName(i)) &&
         	  				!isEmpty(rset.getString("ID_DEVICE_TYPE"))) {
          	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/club/term_device_typespecs.jsp?id="+rset.getString("ID_DEVICE_TYPE"), myFont, myBGColor));
         	  		} else {
        				html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", myFont, myBGColor));
    	            }
                }
                
                if (hasEditPermission) {
                	html.append("<td " + myBGColor + ">");
                	if ("Y".equalsIgnoreCase(rset.getString("HAS_JOIN"))){
                		html.append("<INPUT type=hidden  value=\"Y\" name="+tprvCheck+">");
                    	html.append("<INPUT  type=\"checkbox\" value = \"Y\" name="+tCheck+" id="+tCheck+" checked onclick=\"return CheckCB(this);\"></td>\n");
                	} else {
                    	html.append("<INPUT  type=\"checkbox\" value = \"Y\" name="+tCheck+" id="+tCheck+" onclick=\"return CheckCB(this);\"></td>\n");
                	}
                	html.append("</td>");
                }
                html.append("</tr>\n");
            }
            if (hasEditPermission) {
             	html.append("<tr>\n");
             	html.append("<td colspan=\"10\" align=\"center\">\n");
             	html.append(getSubmitButtonAjax("../crm/dispatch/settings/partner_profileupdate.jsp"));
             	html.append("</td>\n");
             	html.append("</tr>\n");
             }
            html.append("</tbody></table></form>\n");
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
    }
}
