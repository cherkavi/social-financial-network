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


public class bcRoleObject extends bcObject {
	private final static Logger LOGGER=Logger.getLogger(bcRoleObject.class);
	private Map<String,Comparable<String>> fieldHm = new HashMap<String, Comparable<String>>();
	
	private String idRole;
	
	public bcRoleObject(String pIdRole) {
		this.idRole = pIdRole;
		getFeature();
	}
	
	private void getFeature() {
		String featureSelect = " SELECT * FROM " + getGeneralDBScheme() + ".VC_ROLE_ALL WHERE id_role = ?";
		fieldHm = getFeatures2(featureSelect, this.idRole, false);
	}
	
	public String getValue(String pColumnName) {
		return getFeautureResult(fieldHm, pColumnName);
	}

    public String getRoleUsersHTML(String pFindString, String pUserStatus, String p_beg, String p_end) {
    	ArrayList<bcFeautureParam> pParam = initParamArray();

    	String mySQL = 
        	" SELECT rn, name_user, desc_user, fio_nat_prs, phone_mobile, name_user_status, assign_to_user_date, id_user, id_nat_prs" + 
            "   FROM (SELECT ROWNUM rn, id_user, name_user, desc_user, fio_nat_prs, " +
            "			     DECODE(cd_user_status, " +
            "         	 	        'OPENED', '<font color=\"green\"><b>'||name_user_status||'</b></font>', " +
            "          	 	        'CLOSED', '<font color=\"gray\"><b>'||name_user_status||'</b></font>', " +
            "          		        'BLOCKED', '<font color=\"red\"><b>'||name_user_status||'</b></font>', " +
            "          		        'DELETED', '<font color=\"red\"><b>'||name_user_status||'</b></font>', " +
            "          		        name_user_status" +
            "  		       	 ) name_user_status, phone_mobile, creation_date_frmt assign_to_user_date, id_nat_prs" + 
            "   		FROM (SELECT * " +
            "                   FROM " + getGeneralDBScheme() + ".vc_users_roles_all " +
            "  		           WHERE id_role = ? " ;
    	pParam.add(new bcFeautureParam("int", this.idRole));
    	
        if (!isEmpty(pFindString)) {
    		mySQL = mySQL + 
   				" AND (TO_CHAR(id_user) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(name_user) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(desc_user) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(creation_date_frmt) LIKE UPPER('%'||?||'%'))";
    		for (int i=0; i<4; i++) {
    		    pParam.add(new bcFeautureParam("string", pFindString));
    		}
    	}
        pParam.add(new bcFeautureParam("int", p_end));
        pParam.add(new bcFeautureParam("int", p_beg));
        mySQL = mySQL +
            "                  ORDER BY UPPER(name_user)) " + 
            "          WHERE ROWNUM < ?" + 
            " ) WHERE rn >= ?";
        
        StringBuilder html = new StringBuilder();
        Connection con = null;
        PreparedStatement st = null; 
        
        boolean hasUserPermission = false;
        boolean hasNatPrsPermission = false;
        boolean hasEditPermission = false;
       
        try{
   	 	 	if (isEditMenuPermited("SECURITY_USERS") >0) {
   	 	 		hasUserPermission = true;
   	 	 	}
   	 	 	if (isEditMenuPermited("CLIENTS_NATPERSONS") >0) {
   	 	 		hasNatPrsPermission = true;
	 	 	}
	 	 	if (isEditPermited("SECURITY_ROLES_USERS") >0) {
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
            	html.append(getBottomFrameTableTH(userXML, mtd.getColumnName(i)));
            }
            if (hasEditPermission) {
              	html.append("<th>&nbsp;</th>\n");
            }
            html.append("</tr></thead><tbody>\n");
            
            while (rset.next())
            {
            	html.append("<tr>");
            	for (int i=1; i <= colCount-2; i++) {
          	  		if (hasUserPermission && (/*"ID_USER".equalsIgnoreCase(mtd.getColumnName(i)) ||*/ "NAME_USER".equalsIgnoreCase(mtd.getColumnName(i))) ) {
          	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/security/userspecs.jsp?id="+rset.getString("ID_USER"), "", ""));
         	  		} else if (hasNatPrsPermission && (/*"ID_USER".equalsIgnoreCase(mtd.getColumnName(i)) ||*/ "FIO_NAT_PRS".equalsIgnoreCase(mtd.getColumnName(i))) ) {
          	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/clients/natpersonspecs.jsp?id="+rset.getString("ID_NAT_PRS"), "", ""));
         	  		} else {
          	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", "", ""));
          	  		}
          	  	}
                if (hasEditPermission) {
            	   String myHyperLink = "../crm/security/rolesupdate.jsp?id_role="+this.idRole+"&id_user="+rset.getString(1)+"&type=user&action=remove&process=yes";
                   html.append(getDeleteButtonHTML(myHyperLink, roleXML.getfieldTransl("LAB_DETACH_USER", false), rset.getString("NAME_USER")));
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

    public String getRolesMenuHTML(String pFindString, String pAccessType) {
    	ArrayList<bcFeautureParam> pParam = initParamArray();

    	String mySQL =
       	   " SELECT DECODE(type_menu_element, " +
      	  	" 	 	     'MENU', '<font color=\"red\"><b>'||type_menu_element_tsl||'</b></font>', " +
      	    " 	 	     'TABSHEET', '<font color=\"green\"><b>'||type_menu_element_tsl||'</b></font>', " +
    	  	"          	 'FUNCTION', '<font color=\"blue\"><b>'||type_menu_element_tsl||'</b></font>', " +
      	  	"          	 'UNKNOWN'" +
      	  	"  	    ) type_menu_element_tsl, name_menu_element, name_privilege_type, " +
       	   "        id_menu_element, id_menu_element_parent, menu_level, type_menu_element " + 
       	   "   FROM (SELECT ROWNUM rn, id_menu_element, type_menu_element_tsl, name_menu_element, " +
	       "  		        DECODE(id_privilege_type, " +
	       "         	 	       1, '<font color=\"blue\"><b>'||name_privilege_type||'</b></font>', " +
	       "          	 	       2, '<font color=\"green\"><b>'||name_privilege_type||'</b></font>', " +
	       "          	 		   9, '<font color=\"blue\"><b>'||name_privilege_type||'</b></font>', " +
   	  	   "          		       name_privilege_type" +
	       "  		        ) name_privilege_type, " +
       	   "                id_menu_element_parent, menu_level, type_menu_element, id_privilege_type " + 
       	   "           FROM (SELECT id_menu_element, type_menu_element_tsl, " +
       	   "						REPLACE(name_menu_element_frmt,' ', '&nbsp;') name_menu_element, " +
       	   "						name_privilege_type, id_menu_element_parent, " +
       	   "						menu_level, type_menu_element, id_privilege_type " + 
       	   "   				   FROM " + getGeneralDBScheme() + ".vc_menu_privilege_all " +
       	   "                  WHERE id_role = ? "; 
    	pParam.add(new bcFeautureParam("int", this.idRole));
    	
        if (!isEmpty(pFindString)) {
    		mySQL = mySQL + 
   				" AND (TO_CHAR(id_menu_element) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(name_menu_element) LIKE UPPER('%'||?||'%'))";
    		for (int i=0; i<2; i++) {
    		    pParam.add(new bcFeautureParam("string", pFindString));
    		}
    	}
        if (!isEmpty(pAccessType)) {
    		mySQL = mySQL + " AND id_privilege_type = ? ";
    		pParam.add(new bcFeautureParam("int", pAccessType));
    	}
        mySQL = mySQL + "))";
        StringBuilder html = new StringBuilder();
        Connection con = null;
        PreparedStatement st = null;
        boolean hasEditPermision = false;
        
        try{
       	 	if (isEditPermited("SECURITY_ROLES_PRIVILEGES") >0) {
       	 		hasEditPermision = true;
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
            for (int i=1; i <= colCount-4; i++) {
            	html.append(getBottomFrameTableTH(roleXML, mtd.getColumnName(i)));
            }
            if (hasEditPermision) {
             	 html.append("<th>&nbsp;</th><th>&nbsp;</th>\n");
            }
            html.append("</tr></thead><tbody>\n");
            
            while (rset.next())
            {   
              String R=rset.getString("NAME_MENU_ELEMENT");
              /*if(rset.getString(6).equalsIgnoreCase("2")){
                  R = "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + R; 
              }
              if(rset.getString(6).equalsIgnoreCase("3")){
                  R = "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" +
                  	   "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + R; 
              }
              */  
              if(rset.getString("type_menu_element").equalsIgnoreCase("MENU")){
                  R = "<b>" + R + "</b>"; 
              }
              if(isEmpty(rset.getString("id_menu_element_parent"))){
                  R = "<font color=\"red\">" + R + "</font>"; 
              }
              if(rset.getString("type_menu_element").equalsIgnoreCase("TABSHEET")){
                  R = "<font color=\"green\">" + R + "</font>"; 
              }
              html.append("<tr>");
              html.append("<td>"+rset.getString("TYPE_MENU_ELEMENT_TSL")+"</td>");
              html.append("<td>"+R+"</td>");
              html.append("<td>"+rset.getString("NAME_PRIVILEGE_TYPE")+"</td>");
              if (hasEditPermision) {
           	   	String myHyperLink = "../crm/security/rolesmenuupdate.jsp?id_role="+this.idRole+"&id_menu="+rset.getString("ID_MENU_ELEMENT");
           	    String myDeleteLink = "../crm/security/rolesmenuupdate.jsp?id_role="+this.idRole+"&id_menu="+rset.getString("ID_MENU_ELEMENT")+"&action=remove&process=yes";
        	   	html.append(getDeleteButtonHTML(myDeleteLink, roleXML.getfieldTransl("LAB_REMOVE_PRIVILEGE", false), rset.getString("NAME_MENU_ELEMENT")));
           	   	html.append(getEditButtonHTML(myHyperLink));
              }
           }
            html.append("</tr></table>\n");
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
       } // class getkUsersMenuHTML
    
    public String getPrivilegeTypeOptions(String id_type, boolean isNull) {
    	return getSelectBodyFromQuery(
    			" SELECT id_privilege_type, name_privilege_type " +
         		"   FROM " + getGeneralDBScheme() + ".vc_privilege_type_all", id_type, isNull);
    } // getPrivilegeTypeOptions()

    public String getRoleMenuListFullHTML(String pFindString, String pPrivilegeType, String p_beg, String p_end) {
    	ArrayList<bcFeautureParam> pParam = initParamArray();

    	String mySQL =
    			" SELECT  id_menu_element, type_menu_element_tsl, name_menu_element, name_privilege_type, " +
   	   			"		  id_menu_element_parent, type_menu_element" +
   	   			"   FROM ( " +
   	   			" SELECT  ROWNUM rn, id_menu_element, type_menu_element_tsl, name_menu_element, name_privilege_type, " +
	   	   		"		  id_menu_element_parent, type_menu_element" +
	   	   		"   FROM ( " +
				" SELECT  s1.id_menu_element, s1.type_menu_element_tsl, s1.name_menu_element, s1.id_privilege_type name_privilege_type, " +
    	   	   	"		  s1.id_menu_element_parent, s1.type_menu_element" +
    			"   FROM (SELECT a.id_menu_element, a.id_menu_element_parent, a.name_menu_element," +
    			" 				 a.order_number, a.type_menu_element, b.id_privilege_type, a.type_menu_element_tsl" +
    			"           FROM (SELECT id_menu_element, id_menu_element_parent, name_menu_element, " +
    			"						 order_number, type_menu_element, type_menu_element_tsl" +
    			"					FROM (SELECT id_menu_element, id_menu_element_parent, REPLACE(name_menu_element_frmt,' ', '&nbsp;') name_menu_element, " +
    			"								 order_number, type_menu_element, type_menu_element_tsl" +
                "						    FROM " + getGeneralDBScheme() + ".vc_menu_and_tabsheet_all" +
                "                          WHERE is_enable = 'Y'" +
                "                            AND is_visible = 'Y'" +
                "							 AND cd_module_type = ?)) a" +
                "					LEFT JOIN (SELECT id_menu_element, id_privilege_type, name_privilege_type" +
                "								 FROM (SELECT id_menu_element, id_privilege_type, name_privilege_type" +
                "										 FROM " + getGeneralDBScheme() + ".vc_menu_privilege_all " +
                "                                       WHERE id_role = ?)) b" +
                "					 ON (a.id_menu_element = b.id_menu_element)) s1 ";
    	pParam.add(new bcFeautureParam("string", this.getValue("CD_MODULE_TYPE")));
    	pParam.add(new bcFeautureParam("int", this.idRole));

        if (!isEmpty(pFindString)) {
    		mySQL = mySQL + 
   				" AND (TO_CHAR(id_menu_element) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(name_menu_element) LIKE UPPER('%'||?||'%'))";
    		for (int i=0; i<2; i++) {
    		    pParam.add(new bcFeautureParam("string", pFindString));
    		}
    	}
    	if (!isEmpty(pPrivilegeType)) {
    		if ("0".equalsIgnoreCase(pPrivilegeType)) {
    			mySQL = mySQL + 
    				" WHERE s1.id_privilege_type IS NULL ";
    		} else if ("1".equalsIgnoreCase(pPrivilegeType)) {
    			mySQL = mySQL + " WHERE s1.id_privilege_type = ? ";
    	    	pParam.add(new bcFeautureParam("int", "1"));
    		} else if ("2".equalsIgnoreCase(pPrivilegeType)) {
    			mySQL = mySQL + " WHERE s1.id_privilege_type = ? ";
    	    	pParam.add(new bcFeautureParam("int", "2"));
    		}
    	}
    	pParam.add(new bcFeautureParam("int", p_end));
    	pParam.add(new bcFeautureParam("int", p_beg));
    	mySQL = mySQL +
                "  ORDER BY s1.order_number )" +
           	   	"          WHERE ROWNUM < ? " + 
           	   	" ) WHERE rn >= ?";

    	StringBuilder html = new StringBuilder();
    	Connection con = null;
    	PreparedStatement st = null;
    	boolean hasEditPermision = false;
    	String permissionNone = "";
    	String permissionRead = "";
    	String permissionWrite = "";
    	
    	Integer rowcount = 0;
    	String rowStr = "";
    		     
    	try{
    	 	 if (isEditPermited("SECURITY_ROLES_PRIVILEGES") >0) {
    	  		hasEditPermision = true;
    	  		permissionNone = "<option value=\"\" SELECTED></option>"+getPrivilegeTypeOptions("", false);
    	  		permissionRead = "<option value=\"\"></option>"+getPrivilegeTypeOptions("1", false);
    	  		permissionWrite = "<option value=\"\"></option>"+getPrivilegeTypeOptions("2", false);
    	  	 }
    	 	 
    	 	 LOGGER.debug(prepareSQLToLog(mySQL, pParam));
    	 	 con = Connector.getConnection(getSessionId());
    	     st = con.prepareStatement(mySQL);
    	     st = prepareParam(st, pParam);
    	     ResultSet rset = st.executeQuery();
    	     ResultSetMetaData mtd = rset.getMetaData();
    		         
    	     int colCount = mtd.getColumnCount();
    	     
    	     if (hasEditPermision) {
    	    	 html.append("<form action=\"../crm/security/rolesmenuupdate.jsp\" name=\"updateForm\" id=\"updateForm\" accept-charset=\"UTF-8\" method=\"POST\">");
    	    	 html.append("<input type=\"hidden\" name=\"action\" value=\"editall\">");
    	    	 html.append("<input type=\"hidden\" name=\"process\" value=\"yes\">");
    	    	 html.append("<input type=\"hidden\" name=\"roleid\" value=\""+this.idRole+"\">");
    	     }
    		  
    	     html.append(getBottomFrameTable());
             html.append("<tr>");
    	     for (int i=1; i <= colCount-2; i++) {
    	    	 html.append(getBottomFrameTableTH(roleXML, mtd.getColumnName(i)));
    	     }
    	     html.append("</tr></thead><tbody>\n");
    	     if (hasEditPermision) {
    	    	 html.append("<tr><td colspan=\"4\" align=\"center\">");
    	    	 html.append(getSubmitButtonAjax("../crm/security/rolesmenuupdate.jsp"));
    	    	 html.append(getGoBackButton("../crm/security/rolespecs.jsp?id=" + this.idRole));
    	    	 html.append("</td></tr>\n");
    	     }
    	        
    	     while (rset.next())
    	     {   
    	    	 rowcount = rowcount + 1;
    	       String R = rset.getString(3);
    	       /*
    	       if(rset.getString(6).equalsIgnoreCase("MENU") && !isEmpty(rset.getString(5))){
    	          R = "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + R; 
    	       }
    	       if(rset.getString(6).equalsIgnoreCase("TABSHEET")){
    	          R = "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" +
    	         	   "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + R; 
    	       }
    	       */
    	       if(rset.getString("type_menu_element").equalsIgnoreCase("MENU")){
                   R = "<b>" + R + "</b>"; 
               }
               if(isEmpty(rset.getString("id_menu_element_parent"))){
                   R = "<font color=\"red\">" + R + "</font>"; 
               }
               html.append("<tr><td>"+rset.getString(1)+
	            		   "</td><td>"+rset.getString(2)+
	            		   "</td><td>"+R+
	            		   "</td><td>");
               if (hasEditPermision) {
            	   html.append("<input type=\"hidden\" name=\"menu"+rowcount+"\" value=\""+ rset.getString(1) +"\">");
            	   if ("2".equalsIgnoreCase(rset.getString(4))) {
            		   html.append("<SELECT name=\"perm"+rowcount+"\" class=\"inputfield\">"+permissionWrite+"</SELECT></td>\n");
            	   } else if ("1".equalsIgnoreCase(rset.getString(4))) {
            		   html.append("<SELECT name=\"perm"+rowcount+"\" class=\"inputfield\">"+permissionRead+"</SELECT></td>\n");
            	   } else if (isEmpty(rset.getString(4))) {
            		   html.append("<SELECT name=\"perm"+rowcount+"\" class=\"inputfield\">"+permissionNone+"</SELECT></td>\n");
            	   }
               }
   	         }
    	     html.append("</tr>\n");
    	     rowStr = "" + rowcount;
    	     if (hasEditPermision) {
    	    	 html.append("<tr><td colspan=\"4\" align=\"center\">");
    	    	 html.append("<input type=\"hidden\" name=\"rowcount\" value=\""+ rowStr +"\">");
    	    	 html.append(getSubmitButtonAjax("../crm/security/rolesmenuupdate.jsp"));
    	    	 html.append(getGoBackButton("../crm/security/rolespecs.jsp?id=" + this.idRole));
    	    	 html.append("</td></tr>\n");
    	     }
    	     html.append("</tbody></table></form>");
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
    } // class getRoleMenuListFullHTML

    public String getRoleReportsHTML(String pIdMenuElement, String pHasPermission, String pFindString, String pModuleType, String pReportKind, String p_beg, String p_end) {
    	ArrayList<bcFeautureParam> pParam = initParamArray();

    	String mySQL = 
        	" SELECT id_report, name_report, name_menu_element, name_module_type, name_report_kind, has_permission " + 
       	   	"   FROM (SELECT ROWNUM rn, id_report, cd_report, name_report, name_module_type, name_report_kind, name_menu_element, id_menu_element, has_permission " + 
       	   	"           FROM (SELECT id_report, cd_report, name_report, name_menu_element||' ('||type_menu_element_tsl||', '||TO_CHAR(id_menu_element)||')' name_menu_element, id_menu_element, " +
       	   	"				         cd_module_type, name_module_type, cd_report_kind, name_report_kind, DECODE(id_perm_report, NULL, 'N', 'Y') has_permission " +
       	   	"                   FROM (SELECT a.id_report, a.cd_report, a.id_menu_element, a.name_menu_element, a.type_menu_element_tsl, " +
       	   	"                                a.name_report, a.name_module_type, a.cd_module_type, a.name_report_kind, a.cd_report_kind, p.id_report id_perm_report " +
       	   	"                           FROM (SELECT * FROM " + getGeneralDBScheme() + ".vc_reports_all ) a " +
       	   	"                           LEFT JOIN " +
       	   	"                                (SELECT * FROM " + getGeneralDBScheme() + ".vc_report_privilege_all " +
       	   	"									WHERE id_role = ?) p " +
       	   	"							  ON (a.id_report = p.id_report))" +
       	   	"                      WHERE 1=1 ";
    	pParam.add(new bcFeautureParam("int", this.idRole));
    	
        if (!isEmpty(pIdMenuElement)) {
        	mySQL = mySQL + " AND id_menu_element = ? ";
        	pParam.add(new bcFeautureParam("int", pIdMenuElement));
        }
        if (!isEmpty(pHasPermission)) {
        	mySQL = mySQL + " AND DECODE(id_perm_report, NULL, 'N', 'Y') = ? ";
        	pParam.add(new bcFeautureParam("string", pHasPermission));
        }
        if (!isEmpty(pFindString)) {
        	mySQL = mySQL + 
        		" AND (TO_CHAR(id_report) LIKE UPPER('%'||?||'%') OR " +
        		"      UPPER(cd_report) LIKE UPPER('%'||?||'%') OR " +
        		"      UPPER(name_report) LIKE UPPER('%'||?||'%')) ";
        	for (int i=0; i<3; i++) {
        	    pParam.add(new bcFeautureParam("string", pFindString));
        	}
        }
    	if (!isEmpty(pModuleType)) {
    		mySQL = mySQL + " AND cd_module_type = ? ";
        	pParam.add(new bcFeautureParam("string", pModuleType));
    	}
    	if (!isEmpty(pReportKind)) {
    		mySQL = mySQL + " AND cd_report_kind = ? ";
        	pParam.add(new bcFeautureParam("string", pReportKind));
    	}
        pParam.add(new bcFeautureParam("int", p_end));
        pParam.add(new bcFeautureParam("int", p_beg));
        mySQL = mySQL + 
    	   	"                 ORDER BY DECODE(id_perm_report, NULL, 2, 1), id_report) " +
       	   	"          WHERE ROWNUM < ? " + 
       	   	" ) WHERE rn >= ?";
          
          StringBuilder html = new StringBuilder();
          Connection con = null;
          PreparedStatement st = null;
          String myFont = "";
          String myFontEnd = "";
          String myBGColor = "";
          
          boolean hasPermission = false;
          boolean hasSystemReportsPermission = false;
  
          try{
        	 if (isEditMenuPermited("REPORTS_SYSTEM") >=0) {
        		 hasSystemReportsPermission = true;
         	 }
        	 if (isEditPermited("SECURITY_ROLES_REPORTS")>0) {
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
            	 html.append("<form method=\"post\" name=\"updateForm\" id=\"updateForm\" action=\"../crm/security/rolesreportupdate.jsp\">\n");
            	 html.append("<input type=\"hidden\" name=\"action\" value=\"set_reports\">\n");
            	 html.append("<input type=\"hidden\" name=\"process\" value=\"yes\">\n");
            	 html.append("<input type=\"hidden\" name=\"id_role\" value=\"" + this.idRole + "\">\n");
             }
             html.append(getBottomFrameTable());
             html.append("<tr>");
             for (int i=1; i <= colCount; i++) {
            	 String colName = mtd.getColumnName(i);                            
                 if (i==colCount) {
                     if (hasPermission) {
                       	 html.append("<th>"+ reportXML.getfieldTransl( colName, false)+
                       		"<br><input id=\"mainCheck\" name=\"mainCheck\" type=\"checkbox\" onclick=\"CheckAll(this,'chb_id')\"></th>\n");
                        } 
                 } else {
                	 html.append(getBottomFrameTableTH(reportXML, colName));
                 }
             }

             html.append("</tr></thead><tbody>\n");
             if (hasPermission) {
              	html.append("<tr>");
              	html.append("<td colspan=\"6\" align=\"center\">");
              	html.append(getSubmitButtonAjax("../crm/security/rolesreportupdate.jsp"));
              	html.append("</td>");
              	html.append("</tr>");
              }
             int rowCount = 0;
             while (rset.next()) {
             	rowCount++;
                String jurPrsID="id_"+rset.getString("ID_REPORT");
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
                	"<td " + myBGColor + ">" + myFont + rset.getString(1) + myFontEnd + "</td>\n");
                if (hasSystemReportsPermission) {
           		 	html.append("<td>"+getHyperLinkFirst()+"../crm/reports/rep_specs2.jsp?id="+rset.getString("ID_REPORT")+"&type=role&id_entry="+this.idRole+getHyperLinkMiddle()+getValue3(rset.getString(2))+getHyperLinkEnd()+"</td>\n");
           	 	} else {
           	 		html.append("<td>" + getValue3(rset.getString(2)) + "</td>\n");
           	 	}
                html.append("<td " + myBGColor + ">" + myFont + rset.getString(3) + myFontEnd + "</td>\n" +
                	"<td " + myBGColor + ">" + myFont + rset.getString(4) + myFontEnd + "</td>\n"+
            		"<td " + myBGColor + ">" + myFont + rset.getString(5) + myFontEnd + "</td>\n");
                if(rset.getString("HAS_PERMISSION").equalsIgnoreCase("Y")){
                  	html.append("<td " + myBGColor + "><INPUT  type=\"checkbox\" value = \"Y\" name="+tCheck+" id="+tCheck+" checked onclick=\"return CheckCB(this);\"></td>\n");
                  	html.append("<INPUT type=hidden  value=\"Y\" name="+tprvCheck+">");
                } else { 
                    html.append("<td><INPUT  type=\"checkbox\" value = \"Y\" name="+tCheck+" id="+tCheck+" onclick=\"return CheckCB(this);\"></td>\n");
                }
                        
                html.append("<INPUT type=hidden  value="+rset.getString(1)+" name="+jurPrsID+">");
                html.append("</tr> ");        
            }

            if (hasPermission) {
            	html.append("<tr>");
            	html.append("<td colspan=\"6\" align=\"center\">");
            	html.append(getSubmitButtonAjax("../crm/security/rolesreportupdate.jsp"));
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

    public String getRoleReports2HTML(String p_beg, String p_end) {
    	ArrayList<bcFeautureParam> pParam = initParamArray();

    	String mySQL =
        	" SELECT id_report, cd_report, name_report, name_menu_element, id_menu_element " + 
       	   	"   FROM (SELECT ROWNUM rn, id_report, cd_report, name_report, name_menu_element, id_menu_element " + 
       	   	"           FROM (SELECT id_report, cd_report, name_report, name_menu_element, id_menu_element " + 
       	   	"                  FROM " + getGeneralDBScheme() + ".vc_report_privilege_all " +
       	   	"                 WHERE id_role = ? " + 
       	   	//"                   AND id_menu_element in (710, 720, 730, 740, 750)" +
       	   	"                 ORDER BY id_menu_element, name_report) " +
       	   	"          WHERE ROWNUM < " + p_end + 
       	   	" ) WHERE rn >= " + p_beg;
    	pParam.add(new bcFeautureParam("int", this.idRole));
    	pParam.add(new bcFeautureParam("int", p_end));
    	pParam.add(new bcFeautureParam("int", p_beg));
    	
        StringBuilder html = new StringBuilder();
        Connection con = null;
        PreparedStatement st = null;
        boolean hasEditPermision = false;
        
        try{
       	 	if (isEditPermited("SECURITY_ROLES_REPORTS") >0) {
       	 		hasEditPermision = true;
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
            if (hasEditPermision) {
            	html.append("<th>&nbsp;</th>\n");
            }
            html.append("</tr></thead><tbody>\n");
            
            while (rset.next()) {   
            	html.append("<tr><td>"+rset.getString(1)+
               		   "</td><td>"+rset.getString(2)+
               		   "</td><td>"+rset.getString(3)+
               		   "</td><td>"+rset.getString(4)+
               		   //"</td><td>"+rset.getString(5)+
               		   "</td>");
                if (hasEditPermision) {
            	   String myHyperLink = "../crm/security/rolesreportupdate.jsp?id_role="+this.idRole+"&id_report="+rset.getString("ID_REPORT")+"&action=remove&process=yes";
                   html.append(getDeleteButtonHTML(myHyperLink, roleXML.getfieldTransl("LAB_REMOVE_REPORT_PRIV", false), rset.getString("NAME_REPORT")));
                }
            }
            html.append("</tr></table>\n");
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
       } // class getRoleReportsHTML

}
