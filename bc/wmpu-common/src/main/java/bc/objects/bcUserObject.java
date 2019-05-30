package bc.objects;

import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import bc.bcBase;
import bc.connection.Connector;
import bc.lists.bcListTerminalUser;
import bc.service.bcFeautureParam;


public class bcUserObject extends bcObject {
	private final static Logger LOGGER=Logger.getLogger(bcUserObject.class);
	private Map<String,Comparable<String>> fieldHm = new HashMap<String, Comparable<String>>();
	
	private Map<String,Comparable<String>> parametersHm = new HashMap<String, Comparable<String>>();
	
	private String idUser;
	
	private String idMenuElementCurrent;
	
	public bcUserObject() { 
		setSessionId(bcBase.SESSION_PARAMETERS.SESSION_ID.getValue() );
		setLanguage(bcBase.SESSION_PARAMETERS.LANG.getValue());
		setDateFormat(bcBase.SESSION_PARAMETERS.DATE_FORMAT.getValue());
		setGeneralDBScheme(bcBase.SESSION_PARAMETERS.GENERAL_DB_SCHEME.getValue());
	}
	
	public bcUserObject(String pIdUser) {
		this.idUser = pIdUser;
		setSessionId(bcBase.SESSION_PARAMETERS.SESSION_ID.getValue() );
		setLanguage(bcBase.SESSION_PARAMETERS.LANG.getValue());
		setDateFormat(bcBase.SESSION_PARAMETERS.DATE_FORMAT.getValue());
		setGeneralDBScheme(bcBase.SESSION_PARAMETERS.GENERAL_DB_SCHEME.getValue());
		getFeature();
		getParametersFeature();
	}

	private void getFeature() {
		if (this.idUser == null || "".equalsIgnoreCase(this.idUser)) {
			getCurrentUserFeature();
		} else {
			String featureSelect = " SELECT * FROM " + getGeneralDBScheme() + ".VC_USERS_ALL WHERE id_user = ?";
			fieldHm = getFeatures2(featureSelect, this.idUser, false);
		}
	}
	
	private void getParametersFeature() {
		String featureSelect = " SELECT * FROM " + getGeneralDBScheme() + ".v_user_param_ln_all WHERE id_user = ?";
		parametersHm = getFeatures2(featureSelect, this.idUser, false);
	}
	
	public void getCurrentUserFeature() {
		String featureCurntUserSelect = " SELECT * FROM VC_USER_CURRENT";
		bcFeautureParam[] array = new bcFeautureParam[1];
		array[0] = new bcFeautureParam("none", "");
		fieldHm = getFeatures2(featureCurntUserSelect, array);
		this.idUser = getValue("ID_USER");
		getParametersFeature();
	}
	
	public String getValue(String pColumnName) {
		return getFeautureResult(fieldHm, pColumnName);
	}
	
	public String getParameterValue(String pColumnName) {
		return getFeautureResult(parametersHm, pColumnName);
	}
	
	public void setCurrentMenuId(String pIdMenuElement) {
		this.idMenuElementCurrent = pIdMenuElement;
	}
	
	public String getCurrentMenuId() {
		return this.idMenuElementCurrent;
	}

    public String getUserMenuHTML(String pFindString, String pModuleType, String pAccessType) {
    	ArrayList<bcFeautureParam> pParam = initParamArray();

    	String mySQL = 
      	  	"SELECT name_menu_element, type_menu_element, " +
      	  	"       DECODE(id_privilege_type, " +
      	  	" 	 	     1, '<font color=\"brown\"><b>'||name_privilege_type||'</b></font>', " +
      	  	"          	 2, '<font color=\"green\"><b>'||name_privilege_type||'</b></font>', " +
      	    "          	 9, '<font color=\"blue\"><b>'||name_privilege_type||'</b></font>', " +
    	  	"          	 name_privilege_type" +
      	  	"  	  ) name_privilege_type, " +
      	  	"     DECODE(type_menu_element, " +
      	  	" 	 	     'MENU', '<font color=\"red\"><b>" + roleXML.getfieldTransl("type_menu_element_menu", false) + "</b></font>', " +
      	    " 	 	     'TABSHEET', '<font color=\"green\"><b>" + roleXML.getfieldTransl("type_menu_element_tabsheet", false) + "</b></font>', " +
    	  	"          	 'FUNCTION', '<font color=\"blue\"><b>" + roleXML.getfieldTransl("type_menu_element_function", false) + "</b></font>', " +
      	  	"          	 'UNKNOWN'" +
      	  	"  	  ) type_menu_element_txt," +
      	  	"    name_module_type, id_menu_element_parent, id_menu_element " +
      	  	"  FROM (SELECT id_menu_element, id_menu_element_parent, name_module_type, type_menu_element," +
      	  	"               name_menu_element, name_privilege_type, order_number, id_privilege_type " +
      	  	"          FROM " + getGeneralDBScheme() + ".vc_user_menu_full_all " +
      	  	"         WHERE id_user = ? "; 
    	pParam.add(new bcFeautureParam("int", this.idUser));
    	
        if (!isEmpty(pFindString)) {
    		mySQL = mySQL + 
   				" AND (TO_CHAR(name_menu_element) LIKE UPPER('%'||?||'%'))";
    		for (int i=0; i<1; i++) {
    		    pParam.add(new bcFeautureParam("string", pFindString));
    		}
    	}
        if (!isEmpty(pModuleType)) {
    		mySQL = mySQL + " AND cd_module_type = ? ";
    		pParam.add(new bcFeautureParam("string", pModuleType));
    	}
        if (!isEmpty(pAccessType)) {
    		mySQL = mySQL + " AND id_privilege_type = ? ";
    		pParam.add(new bcFeautureParam("int", pAccessType));
    	}
        mySQL = mySQL + " ) ORDER BY name_module_type, order_number ";
        StringBuilder html = new StringBuilder();
        Connection con = null;
        PreparedStatement st = null; 
  
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
             for (int i=1; i <= colCount-4; i++) {
            	 html.append(getBottomFrameTableTH(roleXML, mtd.getColumnName(i)));
             }
             html.append("</tr></thead><tbody>\n");
             
             String nbspLine = "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
             String moduleName = "";
             String R= "";
             
             while (rset.next()) {
            	 
            	 if (!(rset.getString("NAME_MODULE_TYPE").equalsIgnoreCase(moduleName))) {
            		 html.append("<tr>");
            		 html.append("<td><font color=\"blue\"><b>"+rset.getString("NAME_MODULE_TYPE")+ "</b></font></td>");
            		 html.append("<td>&nbsp;</td>");
            		 html.append("<td>&nbsp;</td>");
            		 html.append("</tr>\n");
            		 moduleName = rset.getString("NAME_MODULE_TYPE");
            	 }
        		 R = nbspLine + rset.getString("NAME_MENU_ELEMENT");
            	 if(rset.getString("ID_MENU_ELEMENT_PARENT") != null){
            		 R = nbspLine+R; 
            	 }
            	 if(rset.getString("TYPE_MENU_ELEMENT").equalsIgnoreCase("TABSHEET")|| 
            			 rset.getString("TYPE_MENU_ELEMENT").equalsIgnoreCase("FUNCTION")) {
            		 R = nbspLine+R; 
            	 }
                         
            	 html.append("<tr>\n");
            	 if(rset.getString("TYPE_MENU_ELEMENT").equalsIgnoreCase("MENU")) {
            		 if(isEmpty(rset.getString("ID_MENU_ELEMENT_PARENT"))) {
            			 html.append("<td><b><font color=\"red\">"+R+"</font></b></td>");
                		 html.append("<td>"+rset.getString("TYPE_MENU_ELEMENT_TXT")+"</td>\n");
            			 html.append("<td>"+rset.getString("NAME_PRIVILEGE_TYPE")+"</td>\n");
            		 } else {
	            		 html.append("<td><b>"+R+"</b></td>");
                		 html.append("<td>"+rset.getString("TYPE_MENU_ELEMENT_TXT")+"</td>\n");
	            		 html.append("<td><b>"+rset.getString("NAME_PRIVILEGE_TYPE")+"</b></td>\n");
            		 }
            	 } else {
            		 html.append("<td>"+R+"</td>");
            		 html.append("<td>"+rset.getString("TYPE_MENU_ELEMENT_TXT")+"</td>\n");
            		 html.append("<td>"+rset.getString("NAME_PRIVILEGE_TYPE")+"</td>\n");
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
    } // class getUserMenuHTML

    public String getUserParamHTML(String pFindString) {
    	ArrayList<bcFeautureParam> pParam = initParamArray();

    	String mySQL = 
                "SELECT a.cd_param, a.name_param, a.value_param, " + 
                "       a.value_param_tsl, a.date_param_frmt date_param, a.name_lookup_type " +
                "  FROM " + getGeneralDBScheme() + ".vc_user_param_all a " +
                " WHERE a.id_user = ? ";
    	pParam.add(new bcFeautureParam("int", this.idUser));
    	
        if (!isEmpty(pFindString)) {
    		mySQL = mySQL + 
   				" AND (UPPER(cd_param) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(name_param) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(value_param_tsl) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(date_param_frmt) LIKE UPPER('%'||?||'%'))";
    		for (int i=0; i<4; i++) {
    		    pParam.add(new bcFeautureParam("string", pFindString));
    		}
    	}
        mySQL = mySQL + " ORDER BY name_param";
        StringBuilder html = new StringBuilder();
        Connection con = null;
           
        PreparedStatement st = null;
        PreparedStatement st2 = null;
        int myCnt = 0;
        boolean hasEditPermission = false;

        try{
          	if (isEditPermited("SECURITY_USERS_PARAM") >0) {
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
                html.append("<form action=\"../crm/security/userparamupdate.jsp\" name=\"updateForm\" id=\"updateForm\" accept-charset=\"UTF-8\" method=\"POST\">");
                html.append("<input type=\"hidden\" name=\"action\" value=\"edit\">");
                html.append("<input type=\"hidden\" name=\"process\" value=\"yes\">");
                html.append("<input type=\"hidden\" name=\"id\" value=\""+this.idUser+"\">");
            }
            html.append(getBottomFrameTable());
            html.append("<tr>");
            for (int i=1; i <= colCount; i++) {
               String colName = mtd.getColumnName(i);      
               if (!(colName.equalsIgnoreCase("value_param")||colName.equalsIgnoreCase("name_lookup_type"))) {
            	   html.append(getBottomFrameTableTH(settingXML, colName));
               }  
            }
            html.append("</tr></thead><tbody>\n");
         
         while (rset.next())
         {
             myCnt = myCnt + 1;
             html.append("<tr><td>"+rset.getString(1)+
                       "</td><td>"+rset.getString(2));
             if (hasEditPermission) {
                 html.append("<input type=\"hidden\" name=\"nameparam"+myCnt+"\" value=\""+rset.getString(1)+"\">");
                 
                 if (isEmpty(rset.getString("NAME_LOOKUP_TYPE"))) {
                     html.append("</td><td><input type=\"text\" name=\"valueparam"+myCnt+"\" size=\"16\" value=\""+getValue2(rset.getString(4))+"\" class=\"inputfield\">");
                 } else {
                	 String lookupSQL = 
                		 "SELECT lookup_code, meaning " +
                		 "  FROM " + getGeneralDBScheme() + ".vc_lookup_values " +
                		 " WHERE name_lookup_type = ? " +
                		 " ORDER BY number_value";
                	 LOGGER.debug("(lookup) " + lookupSQL + 
                			 ", 1={'" + rset.getString("NAME_LOOKUP_TYPE") + ",string}");
                	 
                	 st2 = con.prepareStatement(lookupSQL);
                	 st2.setString(1, rset.getString("NAME_LOOKUP_TYPE"));
                     ResultSet rset2 = st2.executeQuery();
                     html.append("<td><select name=\"valueparam"+myCnt+"\" class=\"inputfield\">");
                     while (rset2.next())
                     {
                     html.append("<option value="+rset2.getString("lookup_code")+"");
                                  if (rset2.getString("lookup_code").equalsIgnoreCase(rset.getString(3))) {html.append(" SELECTED"); }
                                  html.append(">"+rset2.getString("meaning")+"</option>");
                     }
                     html.append("</select></td>\n");
                     st2.close();
                 }
             } else {
                 html.append("</td><td>"+rset.getString(4));
             }
             
            html.append("</td><td>"+rset.getString(5)+"</td></tr>\n");
         }
         html.append("<input type=\"hidden\" name=\"paramcount\" value=\""+myCnt+"\">");
         if (hasEditPermission) {
             html.append("<tr>");
             html.append("<td colspan=\"4\" align=\"center\">");
             html.append(getSubmitButtonAjax("../crm/security/userparamupdate.jsp"));
             html.append("</td></tr>\n");
         }
         html.append("</table></form>\n");
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
    } // class getUserParamHTML

    public String editUserRolesHTML(String pFindString, String pCdModyleType, String pSelected, String p_beg, String p_end) {
    	ArrayList<bcFeautureParam> pParam = initParamArray();

    	String mySQL = 
        	" SELECT rn, name_module_type, name_role, desc_role, selectdate, checkrole, id_role " +
        	"   FROM (SELECT ROWNUM rn, id_role, " +
        	"				 DECODE(cd_module_type, " +
			"               	 	'WEBCLIENT', '<font color=\"blue\"><b>'||name_module_type||'</b></font>', " +
            "               	 	'WEBPOS', '<font color=\"blue\"><b>'||name_module_type||'</b></font>', " +
            "               		'CRM', '<font color=\"red\"><b>'||name_module_type||'</b></font>', " +
            "               		'SYSTEM', '<font color=\"red\"><b>'||name_module_type||'</b></font>', " +
            "               		'PRIVATE_OFFICE', '<font color=\"green\"><b>'||name_module_type||'</b></font>', " +
            "          		        name_module_type" +
            "  		         ) name_module_type, name_role, desc_role, selectdate, checkrole " +
        	"           FROM (SELECT id_role, cd_module_type, name_module_type, name_role, desc_role, selectdate, checkrole " +
        	"                   FROM (SELECT r.id_role, r.cd_module_type, r.name_module_type, r.name_role, r.desc_role, " +
        	"			                     ur.creation_date_frmt selectdate, DECODE (ur.id_user, NULL, 0, 1) checkrole " +
        	"                           FROM (SELECT * " +
        	"                                   FROM " + getGeneralDBScheme() + ".vc_role_all r " +
        	"                                  WHERE id_jur_prs = ?) r " +
        	"                           LEFT JOIN " + getGeneralDBScheme() + ".VC_USERS_ROLES_ALL ur " +
        	"                             ON (ur.id_role = r.id_role AND ur.id_user = ?) " +
        	"                 ORDER BY r.id_role" +
        	"         ) WHERE 1=1"; 
    	pParam.add(new bcFeautureParam("int", this.getValue("ID_PARTNER_WORK")));
    	pParam.add(new bcFeautureParam("int", this.idUser));
    	
        if (!isEmpty(pFindString)) {
    		mySQL = mySQL + 
    			" AND (UPPER(name_role) LIKE UPPER('%'||?||'%') OR " +
				"      UPPER(desc_role) LIKE UPPER('%'||?||'%') OR " +
				"      UPPER(selectdate) LIKE UPPER('%'||?||'%'))";
    		for (int i=0; i<4; i++) {
    		    pParam.add(new bcFeautureParam("string", pFindString));
    		}
    	}
        if (!isEmpty(pCdModyleType)) {
    		mySQL = mySQL + " AND cd_module_type = ? ";
    		pParam.add(new bcFeautureParam("string", pCdModyleType));
    	}
        if (!isEmpty(pSelected)) {
    		mySQL = mySQL + " AND checkrole = ? ";
    		pParam.add(new bcFeautureParam("int", pSelected));
    	}
        pParam.add(new bcFeautureParam("int", p_end));
        pParam.add(new bcFeautureParam("int", p_beg));

        mySQL = mySQL +
        	"      ) WHERE ROWNUM < ? " + 
        	" ) WHERE rn >= ?";
        StringBuilder html = new StringBuilder();
        Connection con = null;
        PreparedStatement st = null; 
           
        boolean hasRolePermission = false;
        boolean hasEditPermission = false;
  
        try{
        	if (isEditMenuPermited("SECURITY_ROLES") >=0) {
        		hasRolePermission = true;
         	}
          	if (isEditPermited("SECURITY_USERS_PRIVILEGES") >0) {
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
		        html.append("<form action=\"../crm/security/userrolesupdate.jsp\" name=\"updateForm\" id=\"updateForm\" accept-charset=\"UTF-8\" method=\"POST\">");
		        html.append("<input type=\"hidden\" name=\"action\" value=\"edit\">");
		        html.append("<input type=\"hidden\" name=\"process\" value=\"yes\">");
	        }

            html.append(getBottomFrameTable());
            html.append("<tr>");
            for (int i=1; i <= colCount-1; i++) {
            	html.append(getBottomFrameTableTH(roleXML, mtd.getColumnName(i)));
            }
            html.append("</tr></thead><tbody>\n");
            if (hasEditPermission) {
            	html.append("<tr>\n");
	            html.append("<td colspan=\"7\" align=\"center\">\n");
	            html.append(getSubmitButtonAjax("../crm/security/userrolesupdate.jsp"));
	            html.append("</td>\n");
	            html.append("</tr>\n");
            }
                 
            int rowCount = 0;
            while (rset.next()) {   
            	rowCount++;
                String RoleN="r_"+rowCount;
                String tprvCheck="prvchk_"+RoleN;
                String DateRoleprvCheck="dprvchk_"+RoleN;
                String tCheck="chk_"+RoleN;
                     
                html.append("<tr>");
                html.append("<td align=\"center\">"+getHTMLValue(rset.getString("RN"))+"</td>");
                html.append("<td align=\"center\">"+rset.getString("NAME_MODULE_TYPE")+"</td>");
                if (hasRolePermission) {
                	html.append("<td>"+getHyperLinkFirst()+"../crm/security/rolespecs.jsp?id="+rset.getString("ID_ROLE")+getHyperLinkMiddle()+getValue3(rset.getString("NAME_ROLE"))+getHyperLinkEnd()+"\n");
                } else {
                	html.append("<td>"+getValue3(rset.getString("NAME_ROLE")));
                }
                html.append("</td><td>"+getValue3(rset.getString("DESC_ROLE"))+"</td>\n");
                html.append("<td>"+getValue3(rset.getString("SELECTDATE"))+"</td>\n");
                         
                String pDisabled = "";
                if (!hasEditPermission) {
                	pDisabled = " disabled=\"disabled\" ";
                }
                         
                if(rset.getString("CHECKROLE").equalsIgnoreCase("1")){
                	html.append("<td align=\"center\"><INPUT  type=\"checkbox\" " + pDisabled + " value = \""+rset.getString("CHECKROLE")+"\" name=\""+tCheck+"\" checked ></td>\n");
                } else { 
                	html.append("<td align=\"center\"><INPUT  type=\"checkbox\" " + pDisabled + " value = \""+rset.getString("CHECKROLE")+"\" name=\""+tCheck+"\"  ></td>\n");
                }
                         
                html.append("<INPUT type=\"hidden\"  value=\""+rset.getString("CHECKROLE")+"\" name=\""+tprvCheck+"\">");
                html.append("<INPUT type=\"hidden\"  value=\""+rset.getString("ID_ROLE")+"\" name=\""+RoleN+"\">");
                html.append("<INPUT type=\"hidden\"  value=\""+rset.getString("SELECTDATE")+"\" name=\""+DateRoleprvCheck+"\">");
                html.append("</tr> ");        
                           
            }
            if (hasEditPermission) {
	        	html.append("<input type=hidden value="+rowCount+" name=rowCount>");
	        	html.append("<INPUT type=hidden value="+this.idUser+" name=user_id>");
	        	html.append("<tr>\n");
	        	html.append("<td colspan=\"7\" align=\"center\">\n");
	        	html.append(getSubmitButtonAjax("../crm/security/userrolesupdate.jsp"));
	        	html.append("</td>\n");
	        	html.append("</tr>\n");
            }
            html.append("</tbody></table>\n");
            if (hasEditPermission) {
            	html.append("</form>\n");
            }
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
    } // class editUserRolesHTML

    public String getUserReportsHTML(String pFindString, String pModuleType, String pReportKind, String p_beg, String p_end, String pHyperLink, String pDivName) {
    	ArrayList<bcFeautureParam> pParam = initParamArray();

    	String mySQL = 
        	" SELECT id_report, name_report, desc_report, name_module_type, name_report_kind, name_menu_element, id_menu_element " +
        	"   FROM (SELECT ROWNUM rn, id_report, name_report, name_module_type, name_report_kind, desc_report, name_menu_element, id_menu_element " +
        	"           FROM (SELECT a.id_report, a.name_report, a.desc_report, a.name_module_type, a.name_report_kind, a.name_menu_element, a.id_menu_element " +
        	"                   FROM " + getGeneralDBScheme() + ".vc_user_all_reports_all a " +
        	"                  WHERE a.id_user = ? ";
    	pParam.add(new bcFeautureParam("int", this.idUser));
    	
    	if (!isEmpty(pModuleType)) {
			mySQL = mySQL + " AND cd_module_type = ? ";
			pParam.add(new bcFeautureParam("string", pModuleType));
		}
    	if (!isEmpty(pReportKind)) {
    		mySQL = mySQL + " AND cd_report_kind = ? ";
        	pParam.add(new bcFeautureParam("string", pReportKind));
    	}
    	
	    if (!isEmpty(pFindString)) {
			mySQL = mySQL + 
				" AND (UPPER(TO_CHAR(id_report)) LIKE UPPER('%'||?||'%') OR " +
				"	     UPPER(cd_report) LIKE UPPER('%'||?||'%') OR " +
				"	     UPPER(name_report) LIKE UPPER('%'||?||'%') OR " +
				"	     UPPER(desc_report) LIKE UPPER('%'||?||'%') OR " +
				"	     UPPER(name_menu_element) LIKE UPPER('%'||?||'%')) ";
			for (int i=0; i<5; i++) {
			    pParam.add(new bcFeautureParam("string", pFindString));
			}
		}
	    pParam.add(new bcFeautureParam("int", p_end));
	    pParam.add(new bcFeautureParam("int", p_beg));
	    mySQL = mySQL +
        	"                  ORDER BY a.name_report )" + 
        	"          WHERE ROWNUM < ? " + 
        	" ) WHERE rn >= ?";
          StringBuilder html = new StringBuilder();
          Connection con = null;
          PreparedStatement st = null; 
          
          boolean hasSystemReportsPermission = false;
  
          try{
        	 if (isEditMenuPermited("REPORTS_SYSTEM") >=0) {
        		 hasSystemReportsPermission = true;
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
            	 html.append(getBottomFrameTableTH(reportXML, mtd.getColumnName(i)));
             }
             html.append("</tr></thead><tbody>\n");
         
             while (rset.next()) {
            	 html.append("<tr>\n" +
                 	"<td align=\"center\">" + getValue3(rset.getString(1)) + "</td>\n");
            	 if (hasSystemReportsPermission) {
            		 html.append("<td>"+getHyperLinkFirst()+"../crm/reports/rep_specs2.jsp?id="+rset.getString("ID_REPORT")+"&type=user&id_entry="+this.idUser+getHyperLinkMiddle()+getValue3(rset.getString(2))+getHyperLinkEnd()+"</td>\n");
            	 } else {
            		 html.append("<td>" + getValue3(rset.getString(2)) + "</td>\n");
            	 }
            	 html.append("<td>" + getValue3(rset.getString(3)) + "</td>\n" +
            		"<td>" + getValue3(rset.getString(4)) + "</td>\n" +
                 	"<td>" + getValue3(rset.getString(5)) + "</td>\n" +
            	 	"<td>" + getValue3(rset.getString(6)) + "</td>\n" +
            	 	"</tr>\n");
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
    } //getUserReportsHTML

    public String getUserJurPrsPrivilegesHTML(String pFind, String pSelected, String p_beg, String p_end) {
    	ArrayList<bcFeautureParam> pParam = initParamArray();

    	String mySQL = 
        	" SELECT rn, sname_club club, sname_jur_prs jur_prs, has_permission, id_jur_prs, id_club " +
        	"   FROM (SELECT ROWNUM rn, id_jur_prs, sname_club, sname_jur_prs, has_permission, id_club " +
        	"           FROM (SELECT a.id_jur_prs, a.sname_club, a.sname_jur_prs, a.has_permission, a.id_club " +
        	"                   FROM " + getGeneralDBScheme() + ".vc_user_jur_prs_priv_all a " +
        	"                  WHERE a.id_user = ? ";
    	pParam.add(new bcFeautureParam("int", this.idUser));
    	
    	if (!isEmpty(pSelected)) {
    		if ("Y".equalsIgnoreCase(pSelected)) {
        		mySQL = mySQL + " AND has_permission = 'Y' ";
    		} else if ("N".equalsIgnoreCase(pSelected)) {
    			mySQL = mySQL + " AND has_permission = 'N' ";
    		}
    	}
    	if (!isEmpty(pFind)) {
    		mySQL = mySQL + 
    			" AND (UPPER(sname_club) LIKE UPPER('%'||?||'%') OR" +
    			"      UPPER(sname_jur_prs) LIKE UPPER('%'||?||'%')) ";
    		for (int i=0; i<2; i++) {
    		    pParam.add(new bcFeautureParam("string", pFind));
    		}
    	}
    	pParam.add(new bcFeautureParam("int", p_end));
    	pParam.add(new bcFeautureParam("int", p_beg));
    	mySQL = mySQL +
        	"                  ORDER BY a.name_club, a.sname_jur_prs )" + 
        	"          WHERE ROWNUM < ? " + 
        	" ) WHERE rn >= ?";
          StringBuilder html = new StringBuilder();
          Connection con = null;
          PreparedStatement st = null;
          String myFont = "";
          String myFontEnd = "";
          String myBGColor = "";
          
          boolean hasPermission = false;
          boolean hasJurPrsPermission = false;
          boolean hasClubPermission = false;

         try{
        	 if (isEditPermited("SECURITY_USERS_JUR_PRS")>0) {
        		 hasPermission = true;
        	 }
        	 if (isEditMenuPermited("CLIENTS_YURPERSONS")>=0) {
        		 hasJurPrsPermission = true;
        	 }
        	 if (isEditMenuPermited("CLUB_CLUB")>=0) {
        		 hasClubPermission = true;
        	 }
        	 
        	 LOGGER.debug(prepareSQLToLog(mySQL, pParam));
        	 con = Connector.getConnection(getSessionId());
             st = con.prepareStatement(mySQL);
             st = prepareParam(st, pParam);
             ResultSet rset = st.executeQuery();
             ResultSetMetaData mtd = rset.getMetaData();
                
             int colCount = mtd.getColumnCount();
             if (hasPermission) {
            	 html.append("<form method=\"post\" name=\"updateForm\" id=\"updateForm\" action=\"../crm/security/usersupdate.jsp\">\n");
            	 html.append("<input type=\"hidden\" name=\"action\" value=\"set_jur_prs\">\n");
            	 html.append("<input type=\"hidden\" name=\"process\" value=\"yes\">\n");
            	 html.append("<input type=\"hidden\" name=\"id\" value=\"" + this.idUser + "\">\n");
             }
             html.append(getBottomFrameTable());
             html.append("<tr>");
             for (int i=1; i <= colCount-2; i++) {
            	 String colName = mtd.getColumnName(i);                            
                 if (i==colCount-2) {
                     if (hasPermission) {
                       	 html.append("<th>"+ jurpersonXML.getfieldTransl(colName, false)+
                       		"<br><input id=\"mainCheck\" name=\"mainCheck\" type=\"checkbox\" onclick=\"CheckAll(this,'chb_id')\"></th>\n");
                     } else {
                    	 html.append("<th>"+ jurpersonXML.getfieldTransl(colName, false)+"</th>\n");
                     }
                 } else {
                	 html.append(getBottomFrameTableTH(jurpersonXML, colName));
                 }
             }
             html.append("</tr></thead><tbody>\n");
             if (hasPermission) {
             	html.append("<tr>");
             	html.append("<td colspan=\"5\" align=\"center\">");
             	html.append(getSubmitButtonAjax("../crm/security/usersupdate.jsp"));
             	html.append("</td>");
             	html.append("</tr>");
             }

             
             int rowCount = 0;
             while (rset.next()) {
             	rowCount++;
                String jurPrsID="id_"+rset.getString("ID_CLUB")+"_"+rset.getString("ID_JUR_PRS");
                String tprvCheck="prv_"+jurPrsID;
                String tCheck="chb_"+jurPrsID;
                
                if(rset.getString("HAS_PERMISSION").equalsIgnoreCase("Y")){
                	myFont = "<b>";
                	myFontEnd = "</b>";
                	myBGColor = selectedBackGroundStyle;
                } else {
                	myFont = "";
                	myFontEnd = "";
                	myBGColor = "";
                }
                    
                html.append("<tr>\n" +
                    	"<td " + myBGColor + " align=\"center\">" + myFont + rset.getString("RN") + myFontEnd + "</td>\n");
                if (hasClubPermission) {
      	  			html.append(getBottomFrameTableTD("CLUB", rset.getString("CLUB"), "../crm/club/clubspecs.jsp?id="+rset.getString("ID_CLUB"), myFont, myBGColor));
     	  		} else {
     	  			html.append("<td " + myBGColor + ">" + myFont + rset.getString("CLUB") + myFontEnd + "</td>\n");
     	  		}
                if (hasJurPrsPermission) {
      	  			html.append(getBottomFrameTableTD("JUR_PRS", rset.getString("JUR_PRS"), "../crm/clients/yurpersonspecs.jsp?id="+rset.getString("ID_JUR_PRS"), myFont, myBGColor));
     	  		} else {
     	  			html.append("<td " + myBGColor + ">" + myFont + rset.getString("JUR_PRS") + myFontEnd + "</td>\n");
     	  		}
                if (hasPermission) {
	                if(rset.getString("HAS_PERMISSION").equalsIgnoreCase("Y")){
	                  	html.append("<td " + myBGColor + " align=\"center\"><INPUT  type=\"checkbox\" value = \"Y\" name="+tCheck+" id="+tCheck+" checked onclick=\"return CheckCB(this);\"></td>\n");
	                  	html.append("<INPUT type=hidden  value=\"Y\" name="+tprvCheck+">");
	                } else { 
	                    html.append("<td align=\"center\"><INPUT  type=\"checkbox\" value = \"Y\" name="+tCheck+" id="+tCheck+" onclick=\"return CheckCB(this);\"></td>\n");
	                }
                } else {
                	if(rset.getString("HAS_PERMISSION").equalsIgnoreCase("Y")){
	                  	html.append("<td " + myBGColor + " align=\"center\"><INPUT  type=\"checkbox\" value = \"Y\" name="+tCheck+" id="+tCheck+" checked disabled></td>\n");
	                  	html.append("<INPUT type=hidden  value=\"Y\" name="+tprvCheck+">");
	                } else { 
	                    html.append("<td align=\"center\"><INPUT  type=\"checkbox\" value = \"Y\" name="+tCheck+" id="+tCheck+" disabled></td>\n");
	                }
                }
                        
                html.append("<INPUT type=hidden  value="+rset.getString("ID_JUR_PRS")+" name="+jurPrsID+">");
                html.append("</tr> ");        
            }

            if (hasPermission) {
            	html.append("<tr>");
            	html.append("<td colspan=\"5\" align=\"center\">");
            	html.append(getSubmitButtonAjax("../crm/security/usersupdate.jsp"));
            	html.append("</td>");
            	html.append("</tr>");
            }
            html.append("<input type=hidden value="+rowCount+" name=rowCount>");
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
    } //getUserJurPrsPrivilegesHTML

    public String getUserClubPrivilegesHTML(String pFind, String pSelected, String p_beg, String p_end) {
    	ArrayList<bcFeautureParam> pParam = initParamArray();

    	String mySQL = 
        	" SELECT rn, id_club, sname_club, has_permission " +
        	"   FROM (SELECT ROWNUM rn, id_club, sname_club, has_permission " +
        	"           FROM (SELECT a.id_club, a.sname_club, a.has_permission " +
        	"                   FROM " + getGeneralDBScheme() + ".vc_user_club_priv_all a " +
        	"                  WHERE a.id_user = ? ";
    	pParam.add(new bcFeautureParam("int", this.idUser));
    	
    	if (!isEmpty(pSelected)) {
    		if ("Y".equalsIgnoreCase(pSelected)) {
        		mySQL = mySQL + " AND has_permission = 'Y' ";
    		} else if ("N".equalsIgnoreCase(pSelected)) {
    			mySQL = mySQL + " AND has_permission = 'N' ";
    		}
    	}
    	if (!isEmpty(pFind)) {
    		mySQL = mySQL + 
    			" AND (TO_CHAR(id_club) LIKE UPPER('%'||?||'%') OR" +
    			"      UPPER(sname_club) LIKE UPPER('%'||?||'%')) ";
    		for (int i=0; i<2; i++) {
    		    pParam.add(new bcFeautureParam("string", pFind));
    		}
    	}
    	pParam.add(new bcFeautureParam("int", p_end));
    	pParam.add(new bcFeautureParam("int", p_beg));
    	mySQL = mySQL +
        	"                  ORDER BY a.sname_club)" + 
        	"          WHERE ROWNUM < ? " + 
        	" ) WHERE rn >= ?";
          StringBuilder html = new StringBuilder();
          Connection con = null;
          PreparedStatement st = null;
          String myFont = "";
          String myFontEnd = "";
          String myBGColor = "";
          
          boolean hasPermission = false;

         try{
        	 if (isEditPermited("SECURITY_USERS_CLUB_PRIV")>0) {
        		 hasPermission = true;
        	 }
        	 
        	 LOGGER.debug(prepareSQLToLog(mySQL, pParam));
        	 con = Connector.getConnection(getSessionId());
             st = con.prepareStatement(mySQL);
             st = prepareParam(st, pParam);
             ResultSet rset = st.executeQuery();
             ResultSetMetaData mtd = rset.getMetaData();
                
             int colCount = mtd.getColumnCount();
             if (hasPermission) {
            	 html.append("<form method=\"post\" name=\"updateForm\" id=\"updateForm\" action=\"../crm/security/usersupdate.jsp\">\n");
            	 html.append("<input type=\"hidden\" name=\"action\" value=\"set_club_priv\">\n");
            	 html.append("<input type=\"hidden\" name=\"process\" value=\"yes\">\n");
            	 html.append("<input type=\"hidden\" name=\"id\" value=\"" + this.idUser + "\">\n");
             }
             html.append(getBottomFrameTable());
             html.append("<tr>");
             for (int i=1; i <= colCount; i++) {
            	 String colName = mtd.getColumnName(i);                            
                 if (i==colCount) {
                     if (hasPermission) {
                       	 html.append("<th>"+ jurpersonXML.getfieldTransl(colName, false)+
                       		"<br><input id=\"mainCheck\" name=\"mainCheck\" type=\"checkbox\" onclick=\"CheckAll(this,'chb_id')\"></th>\n");
                     } else {
                    	 html.append("<th>"+ jurpersonXML.getfieldTransl(colName, false)+"</th>\n");
                     }
                 } else {
                	 html.append(getBottomFrameTableTH(clubXML, colName));
                 }
             }
             html.append("</tr></thead><tbody>\n");
             if (hasPermission) {
             	html.append("<tr>");
             	html.append("<td colspan=\"5\" align=\"center\">");
             	html.append(getSubmitButtonAjax("../crm/security/usersupdate.jsp"));
             	html.append("</td>");
             	html.append("</tr>");
             }

             
             int rowCount = 0;
             while (rset.next()) {
             	rowCount++;
                String jurPrsID="id_"+rset.getString("ID_CLUB");
                String tprvCheck="prv_"+jurPrsID;
                String tCheck="chb_"+jurPrsID;
                
                if(rset.getString(4).equalsIgnoreCase("Y")){
                	myFont = "<b>";
                	myFontEnd = "</b>";
                	myBGColor = selectedBackGroundStyle;
                } else {
                	myFont = "";
                	myFontEnd = "";
                	myBGColor = "";
                }
                    
                html.append("<tr>\n" +
                	"<td " + myBGColor + " align=\"center\">" + myFont + rset.getString(1) + myFontEnd + "</td>\n" +
                	"<td " + myBGColor + " align=\"center\">" + myFont + rset.getString(2) + myFontEnd + "</td>\n" +
                	"<td " + myBGColor + ">" + myFont + rset.getString(3) + myFontEnd + "</td>\n");
                if (hasPermission) {
	                if(rset.getString(4).equalsIgnoreCase("Y")){
	                  	html.append("<td " + myBGColor + " align=\"center\"><INPUT  type=\"checkbox\" value = \"Y\" name="+tCheck+" id="+tCheck+" checked onclick=\"return CheckCB(this);\"></td>\n");
	                  	html.append("<INPUT type=hidden  value=\"Y\" name="+tprvCheck+">");
	                } else { 
	                    html.append("<td align=\"center\"><INPUT  type=\"checkbox\" value = \"Y\" name="+tCheck+" id="+tCheck+" onclick=\"return CheckCB(this);\"></td>\n");
	                }
                } else {
                	if(rset.getString(4).equalsIgnoreCase("Y")){
	                  	html.append("<td " + myBGColor + " align=\"center\"><INPUT  type=\"checkbox\" value = \"Y\" name="+tCheck+" id="+tCheck+" checked disabled></td>\n");
	                  	html.append("<INPUT type=hidden  value=\"Y\" name="+tprvCheck+">");
	                } else { 
	                    html.append("<td align=\"center\"><INPUT  type=\"checkbox\" value = \"Y\" name="+tCheck+" id="+tCheck+" disabled></td>\n");
	                }
                }
                        
                html.append("<INPUT type=hidden  value="+rset.getString("ID_CLUB")+" name="+jurPrsID+">");
                html.append("</tr> ");        
            }

            if (hasPermission) {
            	html.append("<tr>");
            	html.append("<td colspan=\"5\" align=\"center\">");
            	html.append(getSubmitButtonAjax("../crm/security/usersupdate.jsp"));
            	html.append("</td>");
            	html.append("</tr>");
            }
            html.append("<input type=hidden value="+rowCount+" name=rowCount>");
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
    } //getUserJurPrsPrivilegesHTML
	
    public String getConnectionsHTML(String pFindString, String pModuleType, String p_beg, String p_end) {
        StringBuilder html = new StringBuilder();
        Connection con = null;
        PreparedStatement st = null;
        ArrayList<bcFeautureParam> pParam = initParamArray();

        String mySQL = 
        	" SELECT rn, last_connection_date_frmt, name_module_type, last_connection_ip," +
            "        error_connection_count, sms_confirm_code, date_send_sms_conf_code_frmt, " +
            "        count_send_sms_confirm_code, ip_send_sms_confirm_code, id_user_connection "+
            "   FROM (SELECT ROWNUM rn, creation_date_frmt last_connection_date_frmt, " +
            "				 DECODE(cd_module_type, " +
            "              	        'WEBCLIENT', '<font color=\"blue\"><b>'||name_module_type||'</b></font>', " +
            "              	        'WEBPOS', '<font color=\"blue\"><b>'||name_module_type||'</b></font>', " +
            "                       'CRM', '<font color=\"red\"><b>'||name_module_type||'</b></font>', " +
            "               	    'SYSTEM', '<font color=\"red\"><b>'||name_module_type||'</b></font>', " +
            "               	    'PRIVATE_OFFICE', '<font color=\"green\"><b>'||name_module_type||'</b></font>', " +
            "               	    name_module_type" +
            "        		 ) name_module_type, last_connection_ip," +
            "                error_connection_count, sms_confirm_code, date_send_sms_conf_code_frmt, " +
            "                count_send_sms_confirm_code, ip_send_sms_confirm_code, id_user_connection "+
            "           FROM (SELECT * " +
            "                   FROM " + getGeneralDBScheme() + ".vc_user_connection_all "+
            "                  WHERE id_user = ? ";
        pParam.add(new bcFeautureParam("int", this.idUser));
        
        if (!(pModuleType == null || "".equalsIgnoreCase(pModuleType))) {
        	mySQL = mySQL  + " AND cd_module_type = ? ";
            pParam.add(new bcFeautureParam("string", pModuleType));
        }
        
        if (!(pFindString == null || "".equalsIgnoreCase(pFindString))) {
        	mySQL = mySQL  +
	        	" AND (TO_CHAR(creation_date_frmt) LIKE '%'||?||'%' OR " +
	    		"      UPPER(last_connection_ip) LIKE UPPER('%'||?||'%') OR " +
	    		"      UPPER(sms_confirm_code) LIKE UPPER('%'||?||'%') OR " +
	    		"      UPPER(date_send_sms_conf_code_frmt) LIKE UPPER('%'||?||'%') OR " +
	    		"      UPPER(ip_send_sms_confirm_code) LIKE UPPER('%'||?||'%')" +
	    		"     )";
        	for (int i=0; i<5; i++) {
        	    pParam.add(new bcFeautureParam("string", pFindString));
        	}
        }
        pParam.add(new bcFeautureParam("int", p_end));
        pParam.add(new bcFeautureParam("int", p_beg));
        mySQL = mySQL  +
            "                  ORDER BY creation_date DESC) a " +
            "          WHERE ROWNUM < ? " + 
            " ) WHERE rn >= ?";
        
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
            for (int i=1; i <= colCount-1; i++) {
               html.append(getBottomFrameTableTH(userXML, mtd.getColumnName(i)));
            }
            html.append("</tr></thead><tbody>");
            while (rset.next())
            {

          	  	html.append("<tr>");
          	  	for (int i=1; i <= colCount-1; i++) {
          	  		html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", "", ""));
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
	
    public String getSessionHTML(String pFindString, String p_beg, String p_end) {
        StringBuilder html = new StringBuilder();
        Connection con = null;
        PreparedStatement st = null;
        ArrayList<bcFeautureParam> pParam = initParamArray();

        String mySQL = 
        	" SELECT rn, date_connect_frmt, date_disconnect_frmt, web_session, id_term_session, id_db_session "+
            "   FROM (SELECT ROWNUM rn, id_session id_db_session, date_connect_frmt, date_disconnect_frmt, web_session, id_term_session "+
            "           FROM (SELECT * " +
            "                   FROM " + getGeneralDBScheme() + ".vc_user_session_all "+
            "                  WHERE id_user = ? ";
        pParam.add(new bcFeautureParam("int", this.idUser));
        
        if (!(pFindString == null || "".equalsIgnoreCase(pFindString))) {
        	mySQL = mySQL  +
        		" AND (TO_CHAR(id_session) LIKE '%'||?||'%' OR " +
        		"      UPPER(date_connect_frmt) LIKE UPPER('%'||?||'%') OR " +
        		"      UPPER(date_disconnect_frmt) LIKE UPPER('%'||?||'%') OR " +
        		"      UPPER(web_session) LIKE UPPER('%'||?||'%') OR " +
        		"      UPPER(id_term_session) LIKE UPPER('%'||?||'%')" +
        		"     )";
        	for (int i=0; i<5; i++) {
        	    pParam.add(new bcFeautureParam("string", pFindString));
        	}
        }
        pParam.add(new bcFeautureParam("int", p_end));
        pParam.add(new bcFeautureParam("int", p_beg));
        mySQL = mySQL  +
            "                  ORDER BY date_connect DESC) a " +
            "          WHERE ROWNUM < ? " + 
            " ) WHERE rn >= ?";
        
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
            for (int i=1; i <= colCount-1; i++) {
               html.append(getBottomFrameTableTH(userXML, mtd.getColumnName(i)));
            }
            html.append("</tr></thead><tbody>");
            while (rset.next())
            {

          	  	html.append("<tr>");
          	  	for (int i=1; i <= colCount-1; i++) {
          	  		html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", "", ""));
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
	
    public String getActionsHTML(String pFindString, String p_beg, String p_end) {
        StringBuilder html = new StringBuilder();
        Connection con = null;
        PreparedStatement st = null;
        ArrayList<bcFeautureParam> pParam = initParamArray();
        
        String mySQL = 
        	" SELECT rn, creation_date_frmt, id_menu_element, " +
        	"		 DECODE(input_result," +
        	"               0, '<b><font color=\"green\">" + commonXML.getfieldTransl("result_success", false) + "</font></b>'," +
        	"               '<b><font color=\"red\">" + commonXML.getfieldTransl("result_error", false) + "</font></b>'" +
        	"		 ) input_result_tsl, text_input_operation "+
            "   FROM (SELECT ROWNUM rn, creation_date_frmt, id_menu_element, input_result, text_input_operation "+
            "           FROM (SELECT * " +
            "                   FROM " + getGeneralDBScheme() + ".vc_user_ui_log_all "+
            "                  WHERE id_user = ? ";
        pParam.add(new bcFeautureParam("int", this.idUser));
        
        if (!(pFindString == null || "".equalsIgnoreCase(pFindString))) {
        	mySQL = mySQL  +
        		" AND (TO_CHAR(creation_date_frmt) LIKE UPPER('%'||?||'%') OR " +
        		"      UPPER(text_input_operation) LIKE UPPER('%'||?||'%'))";
        	for (int i=0; i<2; i++) {
        	    pParam.add(new bcFeautureParam("string", pFindString));
        	}
        }
        pParam.add(new bcFeautureParam("int", p_end));
        pParam.add(new bcFeautureParam("int", p_beg));
        mySQL = mySQL  +
            "                  ORDER BY creation_date DESC) a " +
            "          WHERE ROWNUM < ? " + 
            " ) WHERE rn >= ?";
        
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
               html.append(getBottomFrameTableTH(userXML, mtd.getColumnName(i)));
            }
            html.append("</tr></thead><tbody>");
            while (rset.next())
            {

          	  	html.append("<tr>");
          	  	for (int i=1; i <= colCount; i++) {
          	  		html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", "", ""));
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
    
    private String reportsCount = "0";
    
    public String getReportsCount() {
    	return "" + reportsCount;
    }

    public String getWebPOSReportsHTML(String pFindString, String p_beg, String p_end, String pHyperLink, String pDivName) {
    	ArrayList<bcFeautureParam> pParam = initParamArray();

    	String mySQL = 
        	" SELECT rn, /*name_report||' ('||cd_report||')'*/ name_report, /*desc_report,*/ id_report, cd_report_state, row_count " +
        	"   FROM (SELECT ROWNUM rn, id_report, cd_report, name_report, desc_report, name_menu_element, id_menu_element, cd_report_state, row_count " +
        	"           FROM (SELECT a.id_report, a.cd_report, a.name_report, a.desc_report, " +
        	"						 a.name_menu_element, a.id_menu_element, a.cd_report_state," +
        	"                        count(*) over () as row_count " +
        	"                   FROM " + getGeneralDBScheme() + ".vc_user_all_reports_all a " +
        	"                  WHERE a.id_user = ? " + 
        	"                    AND a.id_menu_element = 370000 " +
        	"                    AND a.cd_module_type = 'WEBPOS' ";
    	pParam.add(new bcFeautureParam("int", this.idUser));
    	
	    if (!isEmpty(pFindString)) {
			mySQL = mySQL + 
				" AND (UPPER(TO_CHAR(id_report)) LIKE UPPER('%'||?||'%') OR " +
				"	     UPPER(cd_report) LIKE UPPER('%'||?||'%') OR " +
				"	     UPPER(name_report) LIKE UPPER('%'||?||'%') OR " +
				"	     UPPER(desc_report) LIKE UPPER('%'||?||'%')) ";
			for (int i=0; i<4; i++) {
			    pParam.add(new bcFeautureParam("string", pFindString));
			}
		}
	    pParam.add(new bcFeautureParam("int", p_end));
	    pParam.add(new bcFeautureParam("int", p_beg));
	    mySQL = mySQL +
        	"                  ORDER BY a.name_report )" + 
        	"          WHERE ROWNUM < ? " + 
        	" ) WHERE rn >= ?";
          StringBuilder html = new StringBuilder();
          Connection con = null;
          PreparedStatement st = null; 
          
          String myFont = "";
  
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
             for (int i=1; i <= colCount-3; i++) {
            	 html.append(getBottomFrameTableTH(reportXML, mtd.getColumnName(i)));
             }
             html.append("</tr></thead><tbody>\n");
         
             while (rset.next()) {
            	 reportsCount = rset.getString("ROW_COUNT");
            	 html.append("<tr>\n");
            	 if ("WORKS".equalsIgnoreCase(rset.getString("CD_REPORT_STATE"))) {
            		 myFont = "<font style=\"color:green !important;\">";
            	 } else if ("OLD".equalsIgnoreCase(rset.getString("CD_REPORT_STATE"))) {
            		 myFont = "<font style=\"color:blue !important;\">";
            	 } else {
            		 myFont = "<font style=\"color:gray !important;\">";
            	 }
            	 for (int i=1; i <= colCount-3; i++) {
            		 if ("RN".equalsIgnoreCase(mtd.getColumnName(i))) {
            			 html.append(getBottomFrameTableTDBase("",  mtd.getColumnName(i), Types.OTHER, rset.getString(i), "", "", "", "", ""));
            		 } else {
            			 html.append(getBottomFrameTableTDBase("",  mtd.getColumnName(i), Types.OTHER, rset.getString(i), pHyperLink + "?id_report=" + rset.getString("ID_REPORT"), pDivName, myFont, "", ""));
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
    } //getUserReportsHTML
    
    private String documentsCount = "0";
    
    public String getDocumentsCount() {
    	return documentsCount;
    }

    public String getWebPOSDocTemplatesHTML(String pFindString, String p_beg, String p_end) {
    	ArrayList<bcFeautureParam> pParam = initParamArray();

    	String mySQL = 
        	" SELECT rn, name_template, id_template_section, name_template_section, file_name, row_count " +
        	"   FROM (SELECT ROWNUM rn, name_template, id_template_section, name_template_section, file_name, row_count " +
        	"           FROM (SELECT a.name_template, a.id_template_section, a.name_template_section, a.file_name, count(*) over () as row_count " +
        	"                   FROM " + getGeneralDBScheme() + ".vp$doc_template_all a ";
    	
	    if (!isEmpty(pFindString)) {
			mySQL = mySQL + 
				" WHERE (UPPER(name_template) LIKE UPPER('%'||?||'%')) ";
			for (int i=0; i<1; i++) {
			    pParam.add(new bcFeautureParam("string", pFindString));
			}
		}
	    pParam.add(new bcFeautureParam("int", p_end));
	    pParam.add(new bcFeautureParam("int", p_beg));
	    mySQL = mySQL +
        	"                  ORDER BY a.id_template_section, a.name_template )" + 
        	"          WHERE ROWNUM < ? " + 
        	" ) WHERE rn >= ?";
          StringBuilder html = new StringBuilder();
          Connection con = null;
          PreparedStatement st = null; 
  
          try{
        	  
        	 LOGGER.debug(prepareSQLToLog(mySQL, pParam));
        	 con = Connector.getConnection(getSessionId());
             st = con.prepareStatement(mySQL);
             st = prepareParam(st, pParam);
             ResultSet rset = st.executeQuery();

             html.append(getDocTemplateTable());
             html.append("<tbody>\n");
             
             String idCurrentSection = "";
             int sectionCount=0;
             String src_doc = "";
             documentsCount = "0";
         
             while (rset.next()) {
            	 documentsCount = rset.getString("ROW_COUNT");
            	 if (!rset.getString("ID_TEMPLATE_SECTION").equalsIgnoreCase(idCurrentSection)) {
            		 sectionCount ++;
            		 idCurrentSection = rset.getString("ID_TEMPLATE_SECTION");
            		 if (sectionCount>1) {
            			 html.append("<tr><td>&nbsp;</td></tr>\n");
            		 }
                	 html.append("<tr>\n");
                	 html.append("<td class=\"section\">"+rset.getString("NAME_TEMPLATE_SECTION")+"</td>");
                	 html.append("</tr>\n");
            	 }
            	 if (!isEmpty(rset.getString("FILE_NAME"))) {
             		src_doc = "<a href=\"../FileSender?FILENAME=" + URLEncoder.encode(rset.getString("FILE_NAME"),"UTF-8") + "\" title=\"" + buttonXML.getfieldTransl("open", false) + "\" target=_blank>" +
             				  //"<img vspace=\"0\" hspace=\"0\" src=\"../images/oper/small/open.gif\" align=\"top\" style=\"border: 0px;\">" +
             				  rset.getString("NAME_TEMPLATE") +
             				  "</a>";
             	 } else {
             		src_doc = rset.getString("NAME_TEMPLATE");
             	 }
            	 html.append("<tr>\n");
            	 html.append("<td>"+src_doc+"</td>");
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
    } //getUserReportsHTML
    
    private String messagesCount = "0";
    
    public String getMessagesCount() {
    	return messagesCount;
    }

    public String getUserMessagesHTML(String pFindString, String p_beg, String p_end) {
    	ArrayList<bcFeautureParam> pParam = initParamArray();

    	String mySQL = 
        	" SELECT /*rn,*/ date_user_message_frmt, " +
        	"        user_message_type, text_message_short, " +
        	"        cd_user_message_state, cd_user_message_type, id_user_message, row_count " +
        	"   FROM (SELECT ROWNUM rn, id_user_message, date_user_message_frmt, " +
        	"                DECODE(cd_user_message_type," +
        	"                       'SEND', '<font color=\"green\"><b>" + userXML.getfieldTransl("user_message_type_send", false) + "</b></font>', " +
        	"                       'RECEIVE', '<font color=\"blue\"><b>" + userXML.getfieldTransl("user_message_type_receive", false) + "</b></font>', " +
        			"				'UNKNOWN' " +
        	"                ) user_message_type, " +
        	"                cd_user_message_state, cd_user_message_type, text_message_short, row_count " +
        	"           FROM (SELECT id_user_message, date_user_message_frmt, " +
        	"                        cd_user_message_type, cd_user_message_state, text_message_short, count(*) over () as row_count " +
        	"                   FROM " + getGeneralDBScheme() + ".vc_user_message_all a " +
        	"                  WHERE id_user = ? ";
	    pParam.add(new bcFeautureParam("int", this.idUser));
    	
	    if (!isEmpty(pFindString)) {
			mySQL = mySQL + 
				" AND (UPPER(date_user_message_frmt) LIKE UPPER('%'||?||'%') OR " +
				"      UPPER(text_message) LIKE UPPER('%'||?||'%')) ";
			for (int i=0; i<2; i++) {
			    pParam.add(new bcFeautureParam("string", pFindString));
			}
		}
	    pParam.add(new bcFeautureParam("int", p_end));
	    pParam.add(new bcFeautureParam("int", p_beg));
	    mySQL = mySQL +
        	"                  ORDER BY a.date_user_message desc)" + 
        	"          WHERE ROWNUM < ? " + 
        	" ) WHERE rn >= ?";
        StringBuilder html = new StringBuilder();
        Connection con = null;
        PreparedStatement st = null; 
        
        String lFont = "";
        String lAction = "";
        String lImage = "";
        String lTitle = "";
  
        try{
        	  
        	LOGGER.debug(prepareSQLToLog(mySQL, pParam));
          	con = Connector.getConnection(getSessionId());
          	st = con.prepareStatement(mySQL);
          	st = prepareParam(st, pParam);
          	ResultSet rset = st.executeQuery();
            ResultSetMetaData mtd = rset.getMetaData();
            int colCount = mtd.getColumnCount();
            messagesCount = "0";
              
            html.append(getBottomFrameTable());
            html.append("<tr>");
            html.append("<th>&nbsp;</th>");
            for (int i=1; i <= colCount-4; i++) {
                 html.append(getBottomFrameTableTH(userXML, mtd.getColumnName(i)));
            }
            html.append("</tr></thead><tbody>");
            while (rset.next()) {
            	messagesCount = rset.getString("ROW_COUNT");
            	if ("RECEIVE".equalsIgnoreCase(rset.getString("cd_user_message_type"))) {
	            	if ("NOT_READ".equalsIgnoreCase(rset.getString("cd_user_message_state"))) {
	            		lFont = "<font style=\"font-weight: bold;\">";
	            		lAction = "read";
	            		lImage = "point.png";
	            		lTitle = userXML.getfieldTransl("user_message_state_not_read", false);
	            	} else {
	            		lFont = "";
	            		lAction = "not_read";
	            		lImage = "point_gray.png";
	            		lTitle = userXML.getfieldTransl("user_message_state_read", false);
	            	}
            	} else {
            		lFont = "";
            		lAction = "";
            		lImage = "point_gray.png";
            		lTitle = "";
            	}
            	
            	html.append("<tr>");
            	if ("RECEIVE".equalsIgnoreCase(rset.getString("cd_user_message_type"))) {
	            	html.append("<td style=\"width:15px;text-align:center;\">\n");
	        		html.append(getHyperLinkFirst() + "admin/help.jsp?id_message="+rset.getString("ID_USER_MESSAGE") + "&action="+lAction+"', 'div_main')\">\n");
	        		html.append("<img vspace=\"0\" hspace=\"0\" src=\"images/oper/"+lImage+"\" width=\"16\" height=\"16\" align=\"top\" style=\"border: 0px;\" title=\""+ lTitle +"\">\n");
	        		html.append(getHyperLinkEnd() + "</td>\n");
            	} else {
	            	html.append("<td style=\"width:15px;text-align:center;\">&nbsp;</td>\n");
            	}
            	String myHyperLink = "admin/help.jsp?id_message="+rset.getString("ID_USER_MESSAGE") + "&action=edit";
            	for (int i=1; i <= colCount-4; i++) {
            	  	html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), myHyperLink, lFont, ""));
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
    } //getUserReportsHTML

    public String getCRMUserMessagesHTML(String pFindString, String p_beg, String p_end) {
    	ArrayList<bcFeautureParam> pParam = initParamArray();

    	String mySQL = 
        	" SELECT rn, date_user_message_frmt, " +
        	"        user_message_type, text_message_short, " +
        	"        id_user_message, row_count " +
        	"   FROM (SELECT ROWNUM rn, id_user_message, date_user_message_frmt, " +
        	"                DECODE(cd_user_message_type," +
        	"                       'SEND', '<font color=\"green\"><b>" + userXML.getfieldTransl("user_message_type_send", false) + "</b></font>', " +
        	"                       'RECEIVE', '<font color=\"blue\"><b>" + userXML.getfieldTransl("user_message_type_receive", false) + "</b></font>', " +
        			"				'UNKNOWN' " +
        	"                ) user_message_type, " +
        	"                cd_user_message_state, cd_user_message_type, text_message_short, row_count " +
        	"           FROM (SELECT id_user_message, date_user_message_frmt, " +
        	"                        cd_user_message_type, cd_user_message_state, text_message_short, count(*) over () as row_count " +
        	"                   FROM " + getGeneralDBScheme() + ".vc_user_message_all a " +
        	"                  WHERE id_user = ? ";
	    pParam.add(new bcFeautureParam("int", this.idUser));
    	
	    if (!isEmpty(pFindString)) {
			mySQL = mySQL + 
				" AND (UPPER(date_user_message_frmt) LIKE UPPER('%'||?||'%') OR " +
				"      UPPER(text_message) LIKE UPPER('%'||?||'%')) ";
			for (int i=0; i<2; i++) {
			    pParam.add(new bcFeautureParam("string", pFindString));
			}
		}
	    pParam.add(new bcFeautureParam("int", p_end));
	    pParam.add(new bcFeautureParam("int", p_beg));
	    mySQL = mySQL +
        	"                  ORDER BY a.date_user_message desc)" + 
        	"          WHERE ROWNUM < ? " + 
        	" ) WHERE rn >= ?";
        StringBuilder html = new StringBuilder();
        Connection con = null;
        PreparedStatement st = null; 
  
        try{
        	  
        	LOGGER.debug(prepareSQLToLog(mySQL, pParam));
          	con = Connector.getConnection(getSessionId());
          	st = con.prepareStatement(mySQL);
          	st = prepareParam(st, pParam);
          	ResultSet rset = st.executeQuery();
            ResultSetMetaData mtd = rset.getMetaData();
            int colCount = mtd.getColumnCount();
            messagesCount = "0";
              
            html.append(getBottomFrameTable());
            html.append("<tr>");
            for (int i=1; i <= colCount-2; i++) {
                 html.append(getBottomFrameTableTH(userXML, mtd.getColumnName(i)));
            }
            html.append("</tr></thead><tbody>");
            while (rset.next())
            {
            	html.append("<tr>");
          	  	for (int i=1; i <= colCount-2; i++) {
          	  		html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", "", ""));
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
    } //getUserReportsHTML
    
    public String getUserMessageNotReadCount(String pIdUser) {
    	String mySQL = " SELECT count(*) cnt FROM " + getGeneralDBScheme() + ".vc_user_message_all a WHERE id_user = ? AND cd_user_message_type = 'RECEIVE' AND cd_user_message_state = 'NOT_READ' ";
    	return getOneValueByIntId(mySQL, pIdUser);
    } // getCardStateName
    
    private String accessUserCount = "0";
    
    public String getAccessUserCount() {
    	return accessUserCount;
    }

    public String getWebPOSAccessHTML(String pFindString, String p_beg, String p_end) {
    	ArrayList<bcFeautureParam> pParam = initParamArray();
		String lStateImageSrc = "";
		String lStateImageTitle = "";
        int rowCount = 0;

    	String mySQL = 
        	" SELECT name_user user_param_name_user, fio_nat_prs_initial user_param_fio_nat_prs, id_term, name_role user_role, " +
        	"        title_access_type, cd_term_user_status, name_term_user_status, id_term_user, id_service_place, id_role, row_count " +
        	"   FROM (SELECT ROWNUM rn, name_user, fio_nat_prs_initial, name_role, id_term, " +
        	"                title_access_type, cd_term_user_status, name_term_user_status, id_term_user, id_service_place, id_role, row_count " +
        	"           FROM (SELECT name_user, fio_nat_prs_initial, name_role, id_term, " +
        	//"       				 DECODE(cd_term_user_status, " +
      	  	//" 	 	     					'OPENED', '<font color=\"green\"><b>'||name_term_user_status||'</b></font>', " +
      	  	//"          	 					'CLOSED', '<font color=\"red\"><b>'||name_term_user_status||'</b></font>', " +
      	    //"          	 					name_term_user_status" +
      	  	//"  	  					 ) name_term_user_status, " +
        	"						 cd_term_user_status, name_term_user_status, " +
      	  	"                        DECODE(cd_term_user_access_type, " +
      	    //"          	 				'STANDARD', '<font color=\"gray\"><b>'||name_term_user_access_type||'</b></font>', " +
      	    " 	 	     					'CASHIER', '<font color=\"green\"><b>'||name_term_user_access_type||'</b></font>', " +
      	    "          	 					'MANAGER', '<font color=\"blue\"><b>'||name_term_user_access_type||'</b></font>', " +
      	    "          	 					'<font color=\"red\"><b>" + webposXML.getfieldTransl("title_access_type_unknown", false) + "</b></font>'" +
      	  	"  	  					 ) title_access_type, id_term_user, id_service_place, id_role, count(*) over () as row_count " +
        	"                   FROM " + getGeneralDBScheme() + ".vp$user_all a " +
        	"                  WHERE id_service_place = ?";
	    pParam.add(new bcFeautureParam("int", this.getValue("ID_SERVICE_PLACE_WORK")));
    	
	    if (!isEmpty(pFindString)) {
			mySQL = mySQL + 
				" AND (UPPER(name_user) LIKE UPPER('%'||?||'%') OR " +
				"      UPPER(fio_nat_prs_initial) LIKE UPPER('%'||?||'%') OR " +
				"      UPPER(name_role) LIKE UPPER('%'||?||'%') OR " +
				"      UPPER(id_term) LIKE UPPER('%'||?||'%')) ";
			for (int i=0; i<4; i++) {
			    pParam.add(new bcFeautureParam("string", pFindString));
			}
		}
	    pParam.add(new bcFeautureParam("int", p_end));
	    pParam.add(new bcFeautureParam("int", p_beg));
	    mySQL = mySQL +
        	"                  ORDER BY name_user, fio_nat_prs_initial, id_term)" + 
        	"          WHERE ROWNUM < ? " + 
        	" ) WHERE rn >= ?";
          StringBuilder html = new StringBuilder();
          Connection con = null;
          PreparedStatement st = null; 
          
          boolean hasPermission = false;
  
          try{
        	 if (isEditPermited("WEBPOS_ADMIN_SETTING_ADMINISTRATION")>0) {
        		 hasPermission = true;
        	 }
        	 
        	 accessUserCount = "0";
        	  
        	 LOGGER.debug(prepareSQLToLog(mySQL, pParam));
        	 con = Connector.getConnection(getSessionId());
             st = con.prepareStatement(mySQL);
             st = prepareParam(st, pParam);
             ResultSet rset = st.executeQuery(); 
             ResultSetMetaData mtd = rset.getMetaData();             
             int colCount = mtd.getColumnCount();

             html.append(getBottomFrameTable());
             html.append("<tr>");
             for (int i=1; i <= colCount-6; i++) {
            	 html.append(getBottomFrameTableTH(webposXML, mtd.getColumnName(i)));
             }
             html.append("<th>1</th>\n");
             if (hasPermission) {
                 html.append("<th>2</th>\n");
             }
             html.append("</tr></thead><tbody>\n");
         
             while (rset.next()) {
            	 accessUserCount = rset.getString("ROW_COUNT");
            	 rowCount ++;
            	html.append("<tr>\n");
          	  	for (int i=1; i <= colCount-5; i++) {
          	  		if ("CD_TERM_USER_STATUS".equalsIgnoreCase(mtd.getColumnName(i))) {
        				html.append("<td>");
        				lStateImageSrc = "";
        				lStateImageTitle = rset.getString("NAME_TERM_USER_STATUS");
        				if ("OPENED".equalsIgnoreCase(rset.getString(i))) {
        					lStateImageSrc = "images/user/opened.png";
        				} else if ("BLOCKED".equalsIgnoreCase(rset.getString(i))) {
        					lStateImageSrc = "images/user/blocked.png";
        				} else if ("CLOSED".equalsIgnoreCase(rset.getString(i))) {
        					lStateImageSrc = "images/user/closed.png";
        				} else if ("DELETED".equalsIgnoreCase(rset.getString(i))) {
        					lStateImageSrc = "images/user/deleted.png";
        				} else {
        					html.append("");
        				}
        				if (!isEmpty(lStateImageSrc)) {
        					html.append("<img width=\"16\" vspace=\"0\" hspace=\"0\" height=\"16\" align=\"top\" style=\"border: 0px;\" src=\"" + lStateImageSrc + "\" title=\"" + lStateImageTitle + "\">");
        				} else {
        					html.append("");
        				}
        				html.append("</td>");
        			} else {
          	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", "", ""));
        			}
          	  	}
          	  	if (hasPermission) {
          	  		String myHyperLink = "admin/setting.jsp?id_term_user="+rset.getString("ID_TERM_USER")+ "&tab=3&type=edit&action=edit";
	        		html.append("<td><div class=\"div_button\" onclick=\"ajaxpage('"+ myHyperLink + "','div_main');\">" + "<img vspace=\"0\" hspace=\"0\"" +
	        				" src=\"images/oper/row_edit_v.GIF\" align=\"top\" width=\"16\" height=\"16\" style=\"border: 0px;\"" + 
	        				" title=\"" + buttonXML.getfieldTransl("button_edit", false)+"\">" + getHyperLinkEnd()+"</td>\n");
          	  		
          	  	}
          	  	html.append("</tr>\n");
             }
             html.append("</tbody></table>\n");
             if (rowCount > 0) {
 	            html.append("<br><span style=\"font-size:11px;\">\n");
 	            html.append("<font style=\"color:green; font-weight: bold;\">" + commonXML.getfieldTransl("title_reference_designation", false) + ":</font><br>\n");
 	            html.append(getNoteUserStatus(hasPermission));
 	            html.append("</span>\n");
             }
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
    } //getUserReportsHTML

	public String getNoteUserStatus(boolean hasEditPermission) {

		StringBuilder html = new StringBuilder();
		html.append("&nbsp;&nbsp;&nbsp;<font style=\"color: black; font-weight: bold;\">1 - " + webposXML.getfieldTransl("user_status_full", false).toLowerCase() + ":</font><br>");
		html.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img width=\"16\" vspace=\"0\" hspace=\"0\" height=\"16\" align=\"top\" style=\"border: 0px;\" src=\"images/user/opened.png\"> - " + webposXML.getfieldTransl("user_status_opened", false).toLowerCase() + "<br>");
		html.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img width=\"16\" vspace=\"0\" hspace=\"0\" height=\"16\" align=\"top\" style=\"border: 0px;\" src=\"images/user/closed.png\"> - " + webposXML.getfieldTransl("user_status_closed", false).toLowerCase() + "<br>");
		html.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img width=\"16\" vspace=\"0\" hspace=\"0\" height=\"16\" align=\"top\" style=\"border: 0px;\" src=\"images/user/blocked.png\"> - " + webposXML.getfieldTransl("user_status_blocked", false).toLowerCase() + "<br>");
		html.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img width=\"16\" vspace=\"0\" hspace=\"0\" height=\"16\" align=\"top\" style=\"border: 0px;\" src=\"images/user/deleted.png\"> - " + webposXML.getfieldTransl("user_status_deleted", false).toLowerCase() + "<br>");
		if (hasEditPermission) {
			html.append("&nbsp;&nbsp;&nbsp;<font style=\"color: black; font-weight: bold;\">2 - " + webposXML.getfieldTransl("user_actions", false).toLowerCase() + ":</font><br>");
			html.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img width=\"16\" vspace=\"0\" hspace=\"0\" height=\"16\" align=\"top\" style=\"border: 0px;\" src=\"images/oper/row_edit_v.GIF\"> - " + buttonXML.getfieldTransl("button_edit", false).toLowerCase() + "<br>");
		}
		return html.toString();
	}

    public String getUserTransactionsHTML(String pFindString, String pTypeTrans, String pPayType, String pStateTrans, String p_beg, String p_end) {
    	ArrayList<bcFeautureParam> pParam = initParamArray();
	    
        StringBuilder html = new StringBuilder();
        Connection con = null;
        PreparedStatement st = null; 
          
        boolean hasJurPrsPermission = false;
        boolean hasTermPermission = false;
	    boolean hasTransactionsPermission = false;
	    boolean hasCardPermission = false;
  
        try{

          	 if (isEditMenuPermited("CLIENTS_YURPERSONS")>=0) {
         		 hasJurPrsPermission = true;
         	 }
        	 if (isEditMenuPermited("CLIENTS_TERMINALS") >=0) {
        		 hasTermPermission = true;
         	 }
	         if (isEditMenuPermited("CARDS_TRANSACTIONS")>=0) {
	         	  hasTransactionsPermission = true;
	         }
	         if (isEditMenuPermited("CARDS_CLUBCARDS")>=0) {
	           	hasCardPermission = true;
	         }

         	String mySQL = 
         		" SELECT rn, id_trans, " +
         		"        type_trans_txt, " +
         		"        sys_date_frmt date_trans_frmt, " +
         		"        name_dealer, id_term, cd_card1, " +
     			"        nt_icc, /*nt_ext, action,*/  " +
     			"		 CASE WHEN NVL(opr_sum,0) > 0 " +
     			"             THEN '<font color=\"blue\">'||opr_sum_frmt||'</font>'||' '||sname_currency " +
     			"             ELSE '' " +
     			"        END opr_sum_frmt, /*sum_pay_cash_frmt, sum_pay_card_frmt, sum_pay_bon_frmt,*/ " +
     			"        CASE WHEN NVL(sum_bon,0) > 0 " +
     			"             THEN '<font color=\"blue\">'||sum_bon_frmt||'</font>' " +
     			"             ELSE '' " +
     			"        END sum_bon_frmt, " +
     			"        /*CASE WHEN NVL(sum_disc,0) > 0 " +
     			"             THEN '<font color=\"blue\">'||sum_disc_frmt||'</font>'||' '||sname_currency " +
     			"             ELSE '' " +
     			"        END sum_disc_frmt,*/ state_trans_tsl, id_telgr, id_dealer, card_serial_number, id_issuer, id_payment_system " +
     			"   FROM (SELECT ROWNUM rn, id_trans, " +
     			"			   DECODE(fcd_trans_type, " +
                "                       'REC_PAYMENT',  '<font style = \"color: black\"><b>'||name_trans_type_full||'</b></font>', " +
                "                       'REC_MOV_BON',  '<font style = \"color: blue\"><b>'||name_trans_type_full||'</b></font>', " +
                "                       'REC_CHK_CARD',  '<font style = \"color: green\"><b>'||name_trans_type_full||'</b></font>', " +
                "                       'REC_INVAL_CARD',  '<font style = \"color: red\"><b>'||name_trans_type_full||'</b></font>', " +
                "                       'REC_STORNO_BON',  '<font style = \"color: red\"><b>'||name_trans_type_full||'</b></font>', " +
                "                       'REC_PAYMENT_IM',  '<font style = \"color: black\"><b>'||name_trans_type_full||'</b></font>', " +
                "                       'REC_PAYMENT_EXT',  '<font style = \"color: black\"><b>'||name_trans_type_full||'</b></font>', " +
         		"                       'REC_ACTIVATION',  '<font style = \"color: blue\"><b>'||name_trans_type_full||'</b></font>', " +
         		"                       'REC_QUESTIONING',  '<font style = \"color: blue\"><b>'||name_trans_type_full||'</b></font>', " +
         		"                       'REC_PUT_CARD',  '<font style = \"color: #191970\"><b>'||name_trans_type_full||'</b></font>', " +
                "                       'REC_COUPON',  '<font style = \"color: #8B8989\"><b>'||name_trans_type_full||'</b></font>', " +
         		"                       'REC_MEMBERSHIP_FEE',  '<font style = \"color: #8B3A3A\"><b>'||name_trans_type_full||'</b></font>', " +
         		"                       'REC_POINT_FEE',  '<font style = \"color: #C8860B\"><b>'||name_trans_type_full||'</b></font>', " +
        		"                       'REC_MTF',  '<font style = \"color: #B8860B\"><b>'||name_trans_type_full||'</b></font>', " +
         		"                       'REC_SHARE_FEE',  '<font style = \"color: green\"><b>'||name_trans_type_full||'</b></font>', " +
         		"                       'REC_TRANSFER_GET_POINT',  '<font style = \"color: #BA55D3\"><b>'||name_trans_type_full||'</b></font>', " +
         		"                       'REC_TRANSFER_PUT_POINT',  '<font style = \"color: #BA55D3\"><b>'||name_trans_type_full||'</b></font>', " +
         		//"                       'REC_CANCEL',  '<font style = \"color: red\"><b>'||name_trans_type_full||'</b></font>', " +
         		//"                       'REC_RETURN',  '<font style = \"color: red\"><b>'||name_trans_type_full||'</b></font>', " +
         		"                       name_trans_type_full" +
         		"                ) type_trans_txt, " +
                "                sys_date_frmt, " +
     			"                name_dealer, id_term, cd_card1, " +
     			"                DECODE(fcd_trans_type, " +
	            "                       'REC_PAYMENT', sname_currency, " +
	            "                       'REC_MOV_BON', '', " +
	            "                       'REC_CHK_CARD', '', " +
	            "                       'REC_INVAL_CARD', '', " +
	            "                       'REC_STORNO_BON', sname_currency, " +
	            "                       'REC_PAYMENT_IM', sname_currency, " +
	            "                       'REC_PAYMENT_EXT', sname_currency, " +
	    		"                       'REC_ACTIVATION', '', " +
	    		"                       'REC_QUESTIONING', '', " +
	    		"                       'REC_PUT_CARD', sname_currency, " +
	            "                       'REC_COUPON', '', " +
	    		"                       'REC_MEMBERSHIP_FEE', sname_currency, " +
	    		"                       'REC_MTF', sname_currency, " +
	            "                       'REC_POINT_FEE', '', " +
	    		"                       'REC_SHARE_FEE', sname_currency, " +
	    		"                       'REC_TRANSFER_GET_POINT', '', " +
	    		"                       'REC_TRANSFER_PUT_POINT', '', " +
	    		"                       'REC_PAYMENT_INVOICE', sname_currency, " +
	    		"                       'REC_SHARE_FEE_CHANGE', sname_currency, " +
	    		"                       'REC_TRANSFORM_FROM_SHARE', sname_currency, " +
	    		//"                       'REC_CANCEL', sname_currency, " +
	    		//"                       'REC_RETURN', sname_currency, " +
	    		"                       sname_currency" +
	    		"                ) sname_currency, nt_icc, nt_ext, action,  " +
         		"		         opr_sum, opr_sum_frmt, sum_pay_cash_frmt, sum_pay_card_frmt, sum_pay_bon_frmt, " +
         		"                sum_bon, sum_bon_frmt, sum_disc, sum_disc_frmt, " +
         		"                DECODE(state_trans, " +
         		"                       -99,  '<font color=\"gray\">'||state_trans_tsl||'</font>', " +
         		"                       -55,  '<font color=\"red\">'||state_trans_tsl||'</font>', " +
         		"                       -54,  '<font color=\"blue\">'||state_trans_tsl||'</font>', " +
         		"                       -13,  '<font color=\"red\"><b>'||state_trans_tsl||'</b></font>', " +
         		"                       -12,  '<font color=\"red\"><b>'||state_trans_tsl||'</b></font>', " +
         		"                       -11,  '<font color=\"red\"><b>'||state_trans_tsl||'</b></font>', " +
         		"                       -9,  '<font color=\"gray\">'||state_trans_tsl||'</font>', " +
         		"                       -7,  '<font color=\"blue\">'||state_trans_tsl||'</font>', " +
         		"                       -6,  '<font color=\"blue\">'||state_trans_tsl||'</font>', " +
         		"                       -4,  '<font color=\"green\">'||state_trans_tsl||'</font>', " +
         		"                       -2,  '<font color=\"ligthred\">'||state_trans_tsl||'</font>', " +
         		"                       -1,  '<font color=\"darkbrown\">'||state_trans_tsl||'</font>', " +
         		"                       0,  '<font color=\"black\"><b>'||state_trans_tsl||'</b></font>', " +
         		"                       1,  '<font color=\"red\"><b>'||state_trans_tsl||'</b></font>', " +
         		"                       5,  '<font color=\"red\"><b>'||state_trans_tsl||'</b></font>', " +
         		"                       9,  '<font color=\"red\"><b>'||state_trans_tsl||'</b></font>', " +
         		"                       state_trans_tsl) state_trans_tsl, " +
         		"                id_telgr, id_dealer, card_serial_number, id_issuer, id_payment_system " +
         		"           FROM (SELECT *" +
         		"                   FROM " + getGeneralDBScheme()+".vc_trans_club_all " +
         		"                  WHERE cashier_user_id = ? ";
         	pParam.add(new bcFeautureParam("int", this.idUser));
         	
         	if (!isEmpty(pTypeTrans)) {
         		mySQL = mySQL + " AND type_trans = ? ";
             	pParam.add(new bcFeautureParam("int", pTypeTrans));
             }
             if (!isEmpty(pPayType)) {
             	mySQL = mySQL + " AND pay_type = ? ";
             	pParam.add(new bcFeautureParam("string", pPayType));
             }
             if (!isEmpty(pStateTrans)) {
             	mySQL = mySQL + " AND state_trans = ? ";
             	pParam.add(new bcFeautureParam("int", pStateTrans));
             }
             if (!isEmpty(pFindString)) {
             	mySQL = mySQL + " AND (UPPER(id_trans) LIKE UPPER('%'||?||'%') OR " +
                		"UPPER(TO_CHAR(id_term)) LIKE UPPER('%'||?||'%') OR " +
                		"UPPER(sys_date_full_frmt) LIKE UPPER('%'||?||'%') OR " +
            			"UPPER(card_serial_number) LIKE UPPER('%'||?||'%')) ";
                	for (int i=0; i<4; i++) {
                	    pParam.add(new bcFeautureParam("string", pFindString));
                	}
            	}
         	mySQL = mySQL +
         		"				   ORDER BY sys_date desc, card_serial_number, nt_icc) WHERE ROWNUM < ? " + 
         		"        ) WHERE rn >= ?";
         	
         	pParam.add(new bcFeautureParam("int", p_end));
     	    pParam.add(new bcFeautureParam("int", p_beg));
        	  
        	LOGGER.debug(prepareSQLToLog(mySQL, pParam));
        	con = Connector.getConnection(getSessionId());
            st = con.prepareStatement(mySQL);
            st = prepareParam(st, pParam);
            ResultSet rset = st.executeQuery();
            ResultSetMetaData mtd = rset.getMetaData();                 
            int colCount = mtd.getColumnCount();

             html.append(getBottomFrameTable());
             html.append("<tr>");
             for (int i=1; i <= colCount-5; i++) {
            	 html.append(getBottomFrameTableTH(transactionXML, mtd.getColumnName(i)));
             }
             html.append("</tr></thead><tbody>\n");
         
             while (rset.next()) {
                 html.append("<tr>");
               	 for (int i=1; i <= colCount-5; i++) {
               		if ("ID_TRANS".equalsIgnoreCase(mtd.getColumnName(i)) && hasTransactionsPermission) {
               			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/cards/transactionspecs.jsp?id="+rset.getString(i), "", ""));
               		} else if ("NAME_DEALER".equalsIgnoreCase(mtd.getColumnName(i)) && hasJurPrsPermission) {
               			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/clients/yurpersonspecs.jsp?id="+rset.getString(i), "", ""));
               		} else if ("ID_TERM".equalsIgnoreCase(mtd.getColumnName(i)) && hasTermPermission) {
               			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/clients/terminalspecs.jsp?id="+rset.getString(i), "", ""));
               		} else if (hasCardPermission && "CD_CARD1".equalsIgnoreCase(mtd.getColumnName(i)) && 
      	  					!isEmpty(rset.getString("ID_ISSUER"))) {
      	  				html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/cards/clubcardspecs.jsp?id="+rset.getString("CARD_SERIAL_NUMBER")+"&iss="+rset.getString("ID_ISSUER")+"&paysys="+rset.getString("ID_PAYMENT_SYSTEM"), "", ""));
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
    } //getUserReportsHTML

    public String getTerminalUsersHTML(String pFindString, String pTermUserAccessType, String pTermUserStatus, String p_beg, String p_end) {
    	bcListTerminalUser list = new bcListTerminalUser();
    	
    	String pWhereCause = " WHERE id_user = ? ";
    	
    	ArrayList<bcFeautureParam> pWhereValue = new ArrayList<bcFeautureParam>();
    	pWhereValue.add(new bcFeautureParam("int", this.idUser));
    	String myDeleteLink = "";
    	String myEditLink = "";
    	if (isEditPermited("SECURITY_USERS_TERMINALS")>0) {
    		myDeleteLink = "../crm/clients/terminaluserupdate.jsp?back_type=USER&type=user&id="+this.idUser+"&id_user="+this.idUser+"&action=remove&process=yes";
    	    myEditLink = "../crm/clients/terminaluserupdate.jsp?back_type=USER&type=user&id="+this.idUser+"&id_user="+this.idUser;
    	}
    	
    	return list.getTerminalUsersHTML(pWhereCause, pWhereValue, "", pFindString, pTermUserAccessType, pTermUserStatus, myEditLink, myDeleteLink, p_beg, p_end);
	
    }

	public String getPermittedIPOptions() {
    	ArrayList<bcFeautureParam> pParam = initParamArray();
    	
    	String mySQL = 
    		" SELECT id_user_permit_ip, permit_ip " 
			+ "   FROM "
			+ getGeneralDBScheme() + ".v_user_permit_ip "
			+ "  WHERE id_user = ? "
			+ "  ORDER BY permit_ip";
    	pParam.add(new bcFeautureParam("int", this.idUser));
    	
    	return getSelectBodyFromParamQuery(mySQL, pParam, "", false);
	}

}
