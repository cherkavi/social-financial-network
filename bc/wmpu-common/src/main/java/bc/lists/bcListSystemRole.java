package bc.lists;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import bc.connection.Connector;
import bc.service.bcFeautureParam;


public class bcListSystemRole extends bc.objects.bcObject {
	private final static Logger LOGGER=Logger.getLogger(bcListSystemRole.class);
	
	public bcListSystemRole() {
	}
	
	public String getSystemRolesHTML(String pWhereCause, ArrayList<bcFeautureParam> pWhereValue, String pNotVisibleData, String pFindString, String pCdModuleType, String pEditLink, String pCopyLink, String pDeleteLink, String p_beg, String p_end) {
		return getSystemRolesHTMLBase(false, pWhereCause, pWhereValue, pNotVisibleData, pFindString, pCdModuleType, pEditLink, pCopyLink, pDeleteLink, p_beg, p_end);
	}

	public String getSystemRolesHTMLOnlySelect(String pFindString, String pCdModuleType, String pGoToLink, String p_beg, String p_end) {
		String pWhereCause = "";
    	
    	ArrayList<bcFeautureParam> pWhereValue = new ArrayList<bcFeautureParam>();
    	
    	return getSystemRolesHTMLBase(true, pWhereCause, pWhereValue, "", pFindString, pCdModuleType, pGoToLink, "", "", p_beg, p_end);
	}

	public String getSystemRolesHTMLBase(boolean pOnlySelect, String pWhereCause, ArrayList<bcFeautureParam> pWhereValue, String pNotVisibleData, String pFindString, String pCdModuleType, String pEditLink, String pCopyLink, String pDeleteLink, String p_beg, String p_end) {
		StringBuilder html = new StringBuilder();
        Connection con = null;
        PreparedStatement st = null;
        ArrayList<bcFeautureParam> pParam = initParamArray();

        boolean hasPartnerPermission = false;
        boolean hasRolePermission = false;
        
        String mySQL = 
        	" SELECT rn, "+("PARTNER".equalsIgnoreCase(pNotVisibleData)?"":"sname_jur_prs partner, ")+" name_role, " +
        	"		 desc_role, name_module_type, id_role, id_jur_prs " +
			"   FROM (SELECT ROWNUM rn, sname_jur_prs, id_role, name_role, desc_role, " +
			"  		         DECODE(cd_module_type, " +
			"               	 	'WEBCLIENT', '<font color=\"blue\"><b>'||name_module_type||'</b></font>', " +
            "               	 	'WEBPOS', '<font color=\"blue\"><b>'||name_module_type||'</b></font>', " +
            "               		'CRM', '<font color=\"red\"><b>'||name_module_type||'</b></font>', " +
            "               		'SYSTEM', '<font color=\"red\"><b>'||name_module_type||'</b></font>', " +
            "               		'PRIVATE_OFFICE', '<font color=\"green\"><b>'||name_module_type||'</b></font>', " +
            "          		        name_module_type" +
            "  		         ) name_module_type, id_jur_prs " +
    		"           FROM (SELECT * " +
    		"                   FROM " + getGeneralDBScheme()+".vc_role_all " +
        	(isEmpty(pWhereCause)?" WHERE 1=1 ":pWhereCause);
        
	   	if (pWhereValue.size() > 0) {
	   		for(int counter=0; counter<=pWhereValue.size()-1;counter++){
	   			pParam.add(pWhereValue.get(counter));
	   		}
	   	}
	        
	   	if (!isEmpty(pFindString)) {
    		mySQL = mySQL + 
    			" AND (UPPER(TO_CHAR(id_role)) LIKE UPPER('%'||?||'%') OR " +
    			"	     UPPER(name_role) LIKE UPPER('%'||?||'%') OR " +
    			"	     UPPER(desc_role) LIKE UPPER('%'||?||'%')) ";
    		for (int i=0; i<3; i++) {
    		    pParam.add(new bcFeautureParam("string", pFindString));
    		}
    	}
        if (!isEmpty(pCdModuleType)) {
    		mySQL = mySQL + " AND cd_module_type = ? ";
    		pParam.add(new bcFeautureParam("string", pCdModuleType));
    	}

        pParam.add(new bcFeautureParam("int", p_end));
        pParam.add(new bcFeautureParam("int", p_beg));
        mySQL = mySQL +
            "   ORDER BY sname_jur_prs, name_role)  WHERE ROWNUM < ? " + 
            " ) WHERE rn >= ?";
        try{
        	if (isEditMenuPermited("CLIENTS_YURPERSONS")>=0) {
        		hasPartnerPermission = true;
            }
        	if (isEditMenuPermited("SECURITY_ROLES")>=0) {
        		hasRolePermission = true;
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
            	html.append(getBottomFrameTableTH(roleXML, mtd.getColumnName(i)));
            }
            if (!isEmpty(pDeleteLink) && !pOnlySelect) {
                html.append("<th>&nbsp;</th>\n");
            }
            if (!isEmpty(pCopyLink) && !pOnlySelect) {
                html.append("<th>&nbsp;</th>\n");
            }
            if (!isEmpty(pEditLink) && !pOnlySelect) {
               html.append("<th>&nbsp;</th>\n");
            }
            if (pOnlySelect) {
                html.append("<th>&nbsp;</th>\n");
            }
            html.append("</tr></thead><tbody>");
            
            while (rset.next())
            {
            	html.append("<tr>");
          	  	for (int i=1; i <= colCount-2; i++) {
          	  		if ("NAME_ROLE".equalsIgnoreCase(mtd.getColumnName(i)) &&
          	  			hasRolePermission) {
          	  			if (!pOnlySelect) {
          	  				html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/security/rolespecs.jsp?id="+rset.getString("ID_ROLE"), "", ""));
          	  			} else {
          	  				html.append(getBottomFrameTableTDWithDiv(mtd.getColumnName(i), rset.getString(i), pEditLink+"&id_role="+rset.getString("ID_ROLE"), "", "", "div_data_detail"));
          	  			}
          	  		} else if ("PARTNER".equalsIgnoreCase(mtd.getColumnName(i)) &&
          	  			hasPartnerPermission && !pOnlySelect) {
          	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/clients/yurpersonspecs.jsp?id="+rset.getString("ID_JUR_PRS"), "", ""));
          	  		} else {
          	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", "", ""));
          	  		}
          	  	}
          	  	if (!isEmpty(pDeleteLink) && !pOnlySelect) {
            	   String myDeleteLink = pDeleteLink+"&id_role="+rset.getString("ID_ROLE");
                   html.append(getDeleteButtonHTML(myDeleteLink, roleXML.getfieldTransl("LAB_DELETE_ROLE", false), rset.getString("NAME_ROLE")));
          	  	}
          	  	if (!isEmpty(pCopyLink) && !pOnlySelect) {
            	   String myCopyLink = pEditLink+"&id_role="+rset.getString("ID_ROLE");
            	   html.append(getCopyButtonStyleHTML(myCopyLink, "", "div_data_detail"));
                }
          	  	if (!isEmpty(pEditLink) && !pOnlySelect) {
             	   String myEditLink = pEditLink+"&id_role="+rset.getString("ID_ROLE");
             	   html.append(getEditButtonWitDivHTML(myEditLink, "div_data_detail"));
                }
          	  	if (pOnlySelect) {
              	   String mySelectLink = pEditLink+"&id_role="+rset.getString("ID_ROLE");
              	   html.append(getSelectButtonStyleHTML(mySelectLink, "", "div_data_detail"));
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
}
