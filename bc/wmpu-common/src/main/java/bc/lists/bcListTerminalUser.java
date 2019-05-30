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


public class bcListTerminalUser extends bc.objects.bcObject {
	private final static Logger LOGGER=Logger.getLogger(bcListTerminalUser.class);
	
	public bcListTerminalUser() {
	}

	public String getTerminalUsersHTML(String pWhereCause, ArrayList<bcFeautureParam> pWhereValue, String pNotVisibleData, String pFindString, String pTermUserAccessType, String pTermUserStatus, String pEditLink, String pDeleteLink, String p_beg, String p_end) {
		StringBuilder html = new StringBuilder();
        Connection con = null;
        PreparedStatement st = null;
        ArrayList<bcFeautureParam> pParam = initParamArray();
        String mySQL = 
        	" SELECT rn, "+("TERMINAL".equalsIgnoreCase(pNotVisibleData)?"":"id_term, ")+
        	"        "+("CONTACT_PRS".equalsIgnoreCase(pNotVisibleData)?"":"name_term_user, ")+
        	"        "+("USER".equalsIgnoreCase(pNotVisibleData)?"":"login_term_user, ")+
        	"        name_term_user_access_type, name_term_user_status, " +
	    	"        is_test_mode_tsl, id_user, id_term_user, id_nat_prs, id_nat_prs_role, id_service_place_work, " +
	    	"        id_term id_term_number, login_term_user login_term_user_init " +
            "   FROM (SELECT ROWNUM rn, login_term_user, name_term_user, id_term," +
            "                DECODE(cd_term_user_access_type, " +
    	    "          	 			'STANDARD', '<font color=\"gray\"><b>'||name_term_user_access_type||'</b></font>', " +
    	    " 	 	     			'CASHIER', '<font color=\"green\"><b>'||name_term_user_access_type||'</b></font>', " +
    	    "          	 			'MANAGER', '<font color=\"blue\"><b>'||name_term_user_access_type||'</b></font>', " +
    	    "          	 			name_term_user_access_type" +
    	  	"  	  			 ) name_term_user_access_type,  " +
            "  		         DECODE(cd_term_user_status, " +
            "         	 	        'OPENED', '<font color=\"green\"><b>'||name_term_user_status||'</b></font>', " +
            "          	 	        'CLOSED', '<font color=\"gray\"><b>'||name_term_user_status||'</b></font>', " +
            "          		        'BLOCKED', '<font color=\"red\"><b>'||name_term_user_status||'</b></font>', " +
            "          		        'DELETED', '<font color=\"red\"><b>'||name_term_user_status||'</b></font>', " +
            "          		        name_term_user_status" +
            "  		         ) name_term_user_status, " +
            "                DECODE(is_test_mode, " +
            "         	 	        'Y', '<font color=\"green\"><b>" + commonXML.getfieldTransl("yes", false) + "</b></font>', " +
            "          	 	        '<font color=\"blue\"><b>" + commonXML.getfieldTransl("no", false) + "</b></font>' " +
            "  		         ) is_test_mode_tsl, id_user, id_term_user, id_nat_prs, id_nat_prs_role, id_service_place_work " +
            "           FROM (SELECT login_term_user, name_term_user, id_term," +
            "                        cd_term_user_access_type, name_term_user_access_type, " +
            "						 cd_term_user_status, name_term_user_status, is_test_mode, " +
            "                        id_user, id_term_user, id_nat_prs, id_nat_prs_role, id_service_place_work " +
            "                   FROM " + getGeneralDBScheme() + ".vc_term_user_all "+
		(isEmpty(pWhereCause)?" WHERE 1=1 ":pWhereCause) ;
        
	   	if (pWhereValue.size() > 0) {
	   		for(int counter=0; counter<=pWhereValue.size()-1;counter++){
	   			pParam.add(pWhereValue.get(counter));
	   		}
	   	}
	        
	   	if (!isEmpty(pFindString)) {
    		mySQL = mySQL + 
   				" AND (TO_CHAR(name_term_user) LIKE UPPER('%'||?||'%') OR " +
   				"      TO_CHAR(login_term_user) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(id_term) LIKE UPPER('%'||?||'%'))";
    		for (int i=0; i<3; i++) {
    		    pParam.add(new bcFeautureParam("string", pFindString));
    		}
	   	}
	   	if (!isEmpty(pTermUserAccessType)) {
    		mySQL = mySQL + " AND cd_term_user_access_type = ? ";
    		pParam.add(new bcFeautureParam("string", pTermUserAccessType));
	   	}
	   	if (!isEmpty(pTermUserStatus)) {
	   		mySQL = mySQL + " AND cd_term_user_status = ? ";
    		pParam.add(new bcFeautureParam("string", pTermUserStatus));
	   	}

        pParam.add(new bcFeautureParam("int", p_end));
        pParam.add(new bcFeautureParam("int", p_beg));
        mySQL = mySQL +
            "   ORDER BY name_term_user)  WHERE ROWNUM < ? " + 
            " ) WHERE rn >= ?";

        boolean hasUserPermission = false;
        boolean hasTerminalPermission = false;
        boolean hasNatPrsPermission = false;
        boolean hasContactPrsPermission = false;
        try{
        	if (isEditMenuPermited("SECURITY_USERS")>=0) {
	    		hasUserPermission = true;
	    	}
	    	if (isEditMenuPermited("CLIENTS_TERMINALS")>=0) {
	    		hasTerminalPermission = true;
	    	}
	    	if (isEditMenuPermited("CLIENTS_NATPERSONS")>=0) {
	    		hasNatPrsPermission = true;
	    	}
	    	if (isEditMenuPermited("CLIENTS_CONTACT_PRS")>=0) {
	    		hasContactPrsPermission = true;
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
            for (int i=1; i <= colCount-7; i++) {
            	html.append(getBottomFrameTableTH(contactXML, mtd.getColumnName(i)));
            }
            if (!isEmpty(pDeleteLink)) {
                html.append("<th>&nbsp;</th>\n");
            }
            if (!isEmpty(pEditLink)) {
               html.append("<th>&nbsp;</th>\n");
            }
            html.append("</tr></thead><tbody>");
            
            while (rset.next())
            {
            	html.append("<tr>");
          	  	for (int i=1; i <= colCount-7; i++) {
          	  		if (hasNatPrsPermission && "NAME_TERM_USER".equalsIgnoreCase(mtd.getColumnName(i)) &&
         	  				!isEmpty(rset.getString("ID_NAT_PRS")) &&
         	  				!"CONTACT_PRS".equalsIgnoreCase(pNotVisibleData)) {
          	  			if (!isEmpty(rset.getString("ID_SERVICE_PLACE_WORK")) &&
          	  					hasContactPrsPermission) {
              	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/clients/contact_prsspecs.jsp?id="+rset.getString("ID_NAT_PRS_ROLE"), "", ""));
          	  			} else if (!isEmpty(rset.getString("ID_NAT_PRS")) &&
          	  					hasNatPrsPermission) {
              	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/clients/natpersonspecs.jsp?id="+rset.getString("ID_NAT_PRS"), "", ""));
          	  			} else {
          	  				html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", "", ""));
          	  			}
         	  		} else if (hasTerminalPermission && "ID_TERM".equalsIgnoreCase(mtd.getColumnName(i)) &&
         	  				!"TERMINAL".equalsIgnoreCase(pNotVisibleData)) {
          	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/clients/terminalspecs.jsp?id="+rset.getString("ID_TERM"), "", ""));
         	  		} else if (hasUserPermission && "LOGIN_TERM_USER".equalsIgnoreCase(mtd.getColumnName(i)) &&
         	  				!isEmpty(rset.getString("ID_USER")) &&
         	  				!"USER".equalsIgnoreCase(pNotVisibleData)) {
          	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/security/userspecs.jsp?id="+rset.getString("ID_USER"), "", ""));
         	  		} else {
          	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", "", ""));
          	  		}
          	  	}
          	    if (!isEmpty(pDeleteLink)) {
                	String myDeleteLink = pEditLink+"&id_term="+rset.getString("ID_TERM_NUMBER")+"&id_term_user="+rset.getString("ID_TERM_USER") + "&process=yes&action=remove";
                    html.append(getDeleteButtonHTML(myDeleteLink, userXML.getfieldTransl("LAB_DELETE_USER", false), rset.getString("LOGIN_TERM_USER_INIT")));
                }
                if (!isEmpty(pEditLink)) {
                	String myEditLink = pDeleteLink+"&id_term="+rset.getString("ID_TERM_NUMBER")+"&id_term_user="+rset.getString("ID_TERM_USER");
             	    html.append(getEditButtonWitDivHTML(myEditLink, "div_data_detail"));
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
