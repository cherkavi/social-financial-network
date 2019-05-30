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

public class bcDsClientProfileObject extends bcObject {
	private final static Logger LOGGER=Logger.getLogger(bcDsClientProfileObject.class);
	private Map<String,Comparable<String>> fieldHm = new HashMap<String, Comparable<String>>();
	
	private String idDispatchClientProfile;
	
	public bcDsClientProfileObject(String pIdDispatchClientProfile){
		this.idDispatchClientProfile = pIdDispatchClientProfile;
		getFeature();
	}
	
	private void getFeature() {
		String featureSelect = " SELECT * FROM " + getGeneralDBScheme() + ".VC_DS_CL_PROFILE_CLUB_ALL WHERE id_ds_cl_profile = ?";
		fieldHm = getFeatures2(featureSelect, this.idDispatchClientProfile, false);
	}
	
	public String getValue(String pColumnName) {
		return getFeautureResult(fieldHm, pColumnName);
	}

    public String getPatternsHTML(String pFind, String p_beg, String p_end) {
    	StringBuilder html = new StringBuilder();
    	Connection con = null;
    	PreparedStatement st = null;
	    boolean hasPatternPermission = false;

	    ArrayList<bcFeautureParam> pParam = initParamArray();

	    String mySQL = 
    		" SELECT rn, id_cl_pattern, name_cl_pattern, name_pattern_type, name_dispatch_type, " +
			"        name_pattern_status, begin_action_date, end_action_date " +
			"   FROM (SELECT ROWNUM rn, id_cl_pattern, name_cl_pattern, name_pattern_type, name_dispatch_type, " +
			"                name_pattern_status, begin_action_date, end_action_date " +
			"           FROM (SELECT a.id_cl_pattern, a.name_cl_pattern, a.name_pattern_type, " +
			"                 		 DECODE(a.cd_dispatch_type, " +
        	"                       		'MANUAL', '<font color=\"green\"><b>'||name_dispatch_type||'</b></font>', " +
        	"                       		'AUTOMATIC', '<font color=\"blue\"><b>'||name_dispatch_type||'</b></font>', " +
        	"                       		'AUTOMATIC_AND_ON_DEMAND', '<font color=\"blue\"><b>'||name_dispatch_type||'</b></font>', " +
        	"                       		'PROGRAM', '<font color=\"blue\"><b>'||name_dispatch_type||'</b></font>', " +
        	"                       		'ON_DEMAND', '<font color=\"red\"><b>'||name_dispatch_type||'</b></font>', " +
        	"                       		a.name_dispatch_type" +
        	"                		 ) name_dispatch_type, " +
	  		"                 		 DECODE(a.cd_pattern_status, " +
        	"                        		'ACTIVE', '<font color=\"green\"><b>'||name_pattern_status||'</b></font>', " +
        	"                        		'SUSPENDED', '<font color=\"brown\"><b>'||name_pattern_status||'</b></font>', " +
        	"                        		'CANCEL', '<font color=\"red\"><b>'||name_pattern_status||'</b></font>', " +
        	"                        		'FINISH', '<font color=\"blue\"><b>'||name_pattern_status||'</b></font>', " +
        	"                        		a.name_pattern_status" +
        	"						 ) name_pattern_status, " +
	  		" 						 a.begin_action_date_frmt begin_action_date," +
			"                        a.end_action_date_frmt end_action_date " +
			"                   FROM " + getGeneralDBScheme()+".vc_ds_cl_pattern_club_all a " +
			"                  WHERE a.id_ds_cl_profile = ? ";
	    pParam.add(new bcFeautureParam("int", this.idDispatchClientProfile));
	    
	    if (!isEmpty(pFind)) {
	    	mySQL = mySQL + 
	    		" AND (UPPER(a.id_cl_pattern) LIKE UPPER('%'||?||'%') OR " +
	    		"      UPPER(a.begin_action_date_frmt) LIKE UPPER('%'||?||'%') OR " +
	    		"      UPPER(a.end_action_date_frmt) LIKE UPPER('%'||?||'%') OR " +
	    		"      UPPER(a.name_cl_pattern) LIKE UPPER('%'||?||'%'))";
	    	for (int i=0; i<4; i++) {
	    	    pParam.add(new bcFeautureParam("string", pFind));
	    	}
	    }
	    pParam.add(new bcFeautureParam("int", p_end));
	    pParam.add(new bcFeautureParam("int", p_beg));
	    mySQL = mySQL + 
	    	" 				   ORDER BY a.name_cl_pattern) " +
    		"          WHERE ROWNUM < ? " +
    		"		 ) " +
    		"  WHERE rn >= ? ";
	    try{
	    	if (isEditMenuPermited("DISPATCH_PATTERNS_CLIENT")>=0) {
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
            html.append("<tr>");
	        for (int i=1; i <= colCount; i++) {
	        	html.append(getBottomFrameTableTH(messageXML, mtd.getColumnName(i)));
            }
	        html.append("</tr></thead><tbody>");
	        
	        while (rset.next()) {
	        	html.append("<tr>\n");
		        for (int i=1; i <= colCount; i++) {
	        		if (("ID_CL_PATTERN".equalsIgnoreCase(mtd.getColumnName(i)) ||
	        				"NAME_CL_PATTERN".equalsIgnoreCase(mtd.getColumnName(i))) && 
	        				hasPatternPermission) {
        				html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/dispatch/patterns/client_patternspecs.jsp?id="+rset.getString("ID_CL_PATTERN"), "", ""));
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
	  } // getRelationShipsHTML

    public String getRecepientsHTML(String pFind, String p_beg, String p_end) {
    	StringBuilder html = new StringBuilder();
    	Connection con = null;
    	PreparedStatement st = null;
	    boolean hasNatPrsPermission = false;
	    
	    boolean isIssuerNotNull = false;
	    boolean isCardStatusNotNull = false;
	    boolean isBonCategoryNotNull = false;
	    boolean isDiscCategoryNotNull = false;
	    boolean isSexNotNull = false;
	    boolean isNatPrsGroupNotNull = false;
	    
	    ArrayList<bcFeautureParam> pParam = initParamArray();

	    String mySQL = 
	    	" SELECT id_nat_prs, full_name, fact_adr_full, date_of_birth_frmt, phone_mobile " +
    		"   FROM (SELECT ROWNUM rn, id_nat_prs, full_name, fact_adr_full, date_of_birth_frmt, phone_mobile " +
    		"  		    FROM (SELECT id_nat_prs, full_name, fact_adr_full, date_of_birth_frmt, phone_mobile " +
    		"                   FROM " + getGeneralDBScheme()+".vc_nat_prs_priv_all " +
    		"                  WHERE 1=1 ";
    	if (!isEmpty(pFind)) {
    		mySQL = mySQL + 
    			" AND (UPPER(full_name) LIKE UPPER('%'||?||'%') OR " +
    			"      UPPER(fact_adr_full) LIKE UPPER('%'||?||'%') OR " +
    			"      UPPER(date_of_birth_frmt) LIKE UPPER('%'||?||'%') OR " +
    			"      UPPER(phone_contact) LIKE UPPER('%'||?||'%') OR " + 
    			"      UPPER(email) LIKE UPPER('%'||?||'%')) ";
    		for (int i=0; i<5; i++) {
    		    pParam.add(new bcFeautureParam("string", pFind));
    		}
    	}
	    	    
	    if (!isEmpty(this.getValue("ID_ISSUER"))) {
	    	isIssuerNotNull = true;
	    }
	    if (!isEmpty(this.getValue("ID_CARD_STATUS"))) {
	    	isCardStatusNotNull = true;
	    }
	    if (!isEmpty(this.getValue("ID_BON_CATEGORY"))) {
	    	isBonCategoryNotNull = true;
	    }
	    if (!isEmpty(this.getValue("ID_DISC_CATEGORY"))) {
	    	isDiscCategoryNotNull = true;
	    }
	    if (!isEmpty(this.getValue("SEX_NAT_PRS"))) {
	    	isSexNotNull = true;
	    }
	    if (!isEmpty(this.getValue("CD_NAT_PRS_GROUP"))) {
	    	isNatPrsGroupNotNull = true;
	    }
	    mySQL = mySQL +
	    	" AND id_nat_prs IN (SELECT id_nat_prs " +
	    	"                      FROM " + getGeneralDBScheme()+".vc_card_priv_all " +
	    	"                     WHERE 1=1 ";
	
	    if (isIssuerNotNull || isCardStatusNotNull || isBonCategoryNotNull || isDiscCategoryNotNull) {
	    	if (isIssuerNotNull) {
	    		mySQL = mySQL +	" AND id_issuer = ? ";
	    		pParam.add(new bcFeautureParam("int", this.getValue("ID_ISSUER")));
	    	}
	    	if (isCardStatusNotNull) {
	    		mySQL = mySQL +	" AND id_card_status = ? ";
	    		pParam.add(new bcFeautureParam("int", this.getValue("ID_CARD_STATUS")));
	    	}
	    	if (isBonCategoryNotNull) {
	    		mySQL = mySQL +	" AND id_bon_category = ? ";
	    		pParam.add(new bcFeautureParam("int", this.getValue("ID_BON_CATEGORY")));
	    	}
	    	if (isDiscCategoryNotNull) {
	    		mySQL = mySQL +	" AND id_disc_category = ? ";
	    		pParam.add(new bcFeautureParam("int", this.getValue("ID_DISC_CATEGORY")));
	    	}
	    }
		mySQL = mySQL +
			") ";

	    
	    if (isSexNotNull) {
	    	mySQL = mySQL + " AND sex = ? ";
	    	pParam.add(new bcFeautureParam("string", this.getValue("SEX_NAT_PRS")));
	    }
	    if (isNatPrsGroupNotNull) {
	    	mySQL = mySQL + " AND cd_nat_prs_group = ? ";
	    	pParam.add(new bcFeautureParam("string", this.getValue("CD_NAT_PRS_GROUP")));
	    }
	    pParam.add(new bcFeautureParam("int", p_end));
	    pParam.add(new bcFeautureParam("int", p_beg));
	    mySQL = mySQL +
			" 				   ORDER BY full_name) " +
    		"          WHERE ROWNUM < ? " +
    		"		 ) " +
    		"  WHERE rn >= ? ";
	    try{
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
	        for (int i=1; i <= colCount; i++) {
	        	html.append(getBottomFrameTableTH(natprsXML, mtd.getColumnName(i)));
            }
	        html.append("</tr></thead><tbody>");
	        
	        while (rset.next()) {
	        	html.append("<tr>\n");
		        for (int i=1; i <= colCount; i++) {
		        	if (hasNatPrsPermission && "ID_NAT_PRS".equalsIgnoreCase(mtd.getColumnName(i))) {
          	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/clients/natpersonspecs.jsp?id="+rset.getString(i), "", ""));
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
	  }	
	public String getNatPrsListHTML(String pFind, String pHasJoin, String p_beg, String p_end) {
        StringBuilder html = new StringBuilder();
        PreparedStatement st = null;
        Connection con = null;

        ArrayList<bcFeautureParam> pParam = initParamArray();

        String mySQL = 
        	" SELECT rn, id_nat_prs, full_name, fact_adr_full, date_of_birth, " +
            "        phone_contact, email, has_join " +
            "   FROM (SELECT ROWNUM rn, id_nat_prs, full_name, fact_adr_full, date_of_birth, " +
            "                phone_contact, email, has_join " +
            "           FROM (SELECT id_nat_prs, full_name, fact_adr_full, date_of_birth, " +
            "                        phone_contact, email, has_join " +
        	"                   FROM (SELECT a.id_nat_prs, a.full_name, a.fact_adr_full, a.date_of_birth_frmt date_of_birth, " +
        	"                                a.phone_contact, a.email, " +
        	"                                DECODE(p.id_nat_prs, NULL, 'N', 'Y') has_join " +
        	"                           FROM (SELECT *" +
        	"                                   FROM " + getGeneralDBScheme()+".vc_nat_prs_priv_all " + 
    		"                                  WHERE 1=1 ";
    	if (!isEmpty(pFind)) {
    		mySQL = mySQL + 
    			" AND (UPPER(full_name) LIKE UPPER('%'||?||'%') OR " +
    			"      UPPER(fact_adr_full) LIKE UPPER('%'||?||'%') OR " +
    			"      UPPER(date_of_birth_frmt) LIKE UPPER('%'||?||'%') OR " +
    			"      UPPER(phone_contact) LIKE UPPER('%'||?||'%') OR " + 
    			"      UPPER(email) LIKE UPPER('%'||?||'%')) ";
    		for (int i=0; i<5; i++) {
    		    pParam.add(new bcFeautureParam("string", pFind));
    		}
    	}
    	mySQL = mySQL + 
		    "                                ) a LEFT JOIN " +
        	"                                (SELECT id_nat_prs " +
        	"                                   FROM " + getGeneralDBScheme()+".vc_ds_cl_profile_nat_prs_all " +
        	"                                  WHERE id_ds_cl_profile = ?) p" +
        	"                             ON (a.id_nat_prs = p.id_nat_prs) " +
        	"                          ORDER BY DECODE(p.id_nat_prs, NULL, 2, 1)" +
        	"                        ) ";
    	pParam.add(new bcFeautureParam("int", this.idDispatchClientProfile));
    	
        if (!(pHasJoin== null || "".equalsIgnoreCase(pHasJoin))) {
           	mySQL = mySQL + " WHERE has_join = ? ";
           	pParam.add(new bcFeautureParam("string", pHasJoin));
        }
        pParam.add(new bcFeautureParam("int", p_end));
        pParam.add(new bcFeautureParam("int", p_beg));
    	mySQL = mySQL + 
        	"              ) WHERE ROWNUM < ? " + 
            "       ) WHERE rn >= ?";
        
        boolean hasNatPrsPermission = false;
        boolean hasEditPermission = false;
        String myFont = "";
        String myBGColor = "";
        
        try{
        	if (isEditMenuPermited("CLIENTS_NATPERSONS")>=0) {
        		hasNatPrsPermission = true;
            }
        	
        	if (isEditPermited("CLUB_CLUB_PARTICIPANT")>0) {
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
           	 	html.append("<form method=\"post\" name=\"updateForm\" id=\"updateForm\" action=\"../crm/dispatch/settings/client_profileupdate.jsp\">\n");
           	 	html.append("<input type=\"hidden\" name=\"type\" value=\"general\">\n");
        	 	html.append("<input type=\"hidden\" name=\"action\" value=\"receiver\">\n");
           	 	html.append("<input type=\"hidden\" name=\"process\" value=\"yes\">\n");
           	 	html.append("<input type=\"hidden\" name=\"id\" value=\"" + this.idDispatchClientProfile + "\">\n");
            }
            
            html.append(getBottomFrameTable());
            html.append("<tr>");
            for (int i=1; i <= colCount-1; i++) {
               html.append(getBottomFrameTableTH(natprsXML, mtd.getColumnName(i)));
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
             	html.append(getSubmitButtonAjax("../crm/dispatch/settings/client_profileupdate.jsp"));
             	html.append("</td>");
             	html.append("</tr>");
             }
            while (rset.next())
            {
                String idContactPrs=rset.getString("ID_NAT_PRS");
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
                
                for (int i=1; i <= colCount-1; i++) {
                	if (hasNatPrsPermission && "ID_NAT_PRS".equalsIgnoreCase(mtd.getColumnName(i))) {
        				html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/clients/natpersonspecs.jsp?id="+rset.getString("ID_NAT_PRS"), myFont, myBGColor));
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
             	html.append(getSubmitButtonAjax("../crm/dispatch/settings/client_profileupdate.jsp"));
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
