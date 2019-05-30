package bc.lists;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import bc.connection.Connector;
import bc.service.bcFeautureParam;


public class bcListUser extends bc.objects.bcObject {
	private final static Logger LOGGER=Logger.getLogger(bcListUser.class);
	
	public bcListUser() {
	}

	public String getUsersHTML(String pWhereCause, ArrayList<bcFeautureParam> pWhereValue, String pNotVisibleData, String pFindString, String pUserStatus, String pGoToLink, String pEditLink, String pDeleteLink, String pDivName, String p_beg, String p_end) {
		StringBuilder html = new StringBuilder();
        Connection con = null;
        PreparedStatement st = null;
        ArrayList<bcFeautureParam> pParam = initParamArray();
        String mySQL = 
        	" SELECT rn, name_user, " +
        	"        "+("NAT_PRS".equalsIgnoreCase(pNotVisibleData)?"":"fio_nat_prs, ")+
        	"        "+("JUR_PRS".equalsIgnoreCase(pNotVisibleData)?"":"sname_service_place_work, ")+
        	"        "+("CONTACT_PRS".equalsIgnoreCase(pNotVisibleData)?"":"name_post, ")+
        	"        phone_mobile, " +
        	"		 email_work, name_user_status, id_user, id_nat_prs, id_nat_prs_role, id_service_place_work," +
        	"        name_user name_user_initial " +
	  		"   FROM (SELECT ROWNUM rn, name_user, fio_nat_prs, sname_service_place_work, name_post, phone_mobile, " +
	  		"  		         email_work, " +
	  		"  		         DECODE(cd_user_status, " +
            "         	 	        'OPENED', '<font color=\"green\"><b>'||name_user_status||'</b></font>', " +
            "          	 	        'CLOSED', '<font color=\"gray\"><b>'||name_user_status||'</b></font>', " +
            "          		        'BLOCKED', '<font color=\"red\"><b>'||name_user_status||'</b></font>', " +
            "          		        'DELETED', '<font color=\"red\"><b>'||name_user_status||'</b></font>', " +
            "          		        name_user_status" +
            "  		         ) name_user_status, " +
            "                id_user, id_nat_prs, id_nat_prs_role, id_service_place_work  " +
  			"           FROM (SELECT * " +
  			"                   FROM " + getGeneralDBScheme()+".vc_users_all "+
		(isEmpty(pWhereCause)?" WHERE 1=1 ":pWhereCause) ;
        
	   	if (pWhereValue.size() > 0) {
	   		for(int counter=0; counter<=pWhereValue.size()-1;counter++){
	   			pParam.add(pWhereValue.get(counter));
	   		}
	   	}
	        
	   	if (!isEmpty(pFindString)) {
    		mySQL = mySQL + " AND (UPPER(name_user) LIKE UPPER('%'||?||'%')";
    		pParam.add(new bcFeautureParam("string", pFindString));
    		if (!"NAT_PRS".equalsIgnoreCase(pNotVisibleData)) {
    			mySQL = mySQL + " OR UPPER(fio_nat_prs) LIKE UPPER('%'||?||'%')";
    			pParam.add(new bcFeautureParam("string", pFindString));
    		}
    		mySQL = mySQL + ") ";
	   	}
	   	if (!isEmpty(pUserStatus)) {
	   		mySQL = mySQL + " AND cd_user_status = ? ";
    		pParam.add(new bcFeautureParam("string", pUserStatus));
	   	}

        pParam.add(new bcFeautureParam("int", p_end));
        pParam.add(new bcFeautureParam("int", p_beg));
        mySQL = mySQL +
            "   ORDER BY sname_service_place_work, name_user)  WHERE ROWNUM < ? " + 
            " ) WHERE rn >= ?";

        boolean hasUserPermission = false;
        boolean hasJurPrsPermission = false;
        boolean hasNatPrsPermission = false;
        boolean hasContactPrsPermission = false;
        try{
        	if (isEditMenuPermited("SECURITY_USERS")>=0) {
	    		hasUserPermission = true;
	    	}
	    	if (isEditMenuPermited("CLIENTS_YURPERSONS")>=0) {
	    		hasJurPrsPermission = true;
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
            
            html.append(isEmpty(pGoToLink)?getBottomFrameTable():getTableSorterTable());
            html.append("<tr>");
            for (int i=1; i <= colCount-5; i++) {
            	html.append(getBottomFrameTableTH(userXML, mtd.getColumnName(i)));
            }
            if (!isEmpty(pDeleteLink)) {
                html.append("<th>&nbsp;</th>\n");
            }
            if (!isEmpty(pEditLink)) {
               html.append("<th>&nbsp;</th>\n");
            }
            html.append("</tr></thead><tbody>");
            String lLink = "";
            String lGoLink = "";
            
            while (rset.next())
            {
            	html.append("<tr>");
          	  	for (int i=1; i <= colCount-5; i++) {
          	  		lGoLink = pGoToLink+"&id_user="+rset.getString("ID_USER");
          	  		if (hasNatPrsPermission && "FIO_NAT_PRS".equalsIgnoreCase(mtd.getColumnName(i)) &&
         	  				!isEmpty(rset.getString("ID_NAT_PRS")) &&
         	  				!"NAT_PRS".equalsIgnoreCase(pNotVisibleData)) {
          	  			lLink = isEmpty(pGoToLink)?"../crm/clients/natpersonspecs.jsp?id="+rset.getString("ID_NAT_PRS"):lGoLink;
          	  			//html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/clients/natpersonspecs.jsp?id="+rset.getString("ID_NAT_PRS"), "", ""));
         	  		} else if (hasContactPrsPermission && "NAME_POST".equalsIgnoreCase(mtd.getColumnName(i)) &&
         	  				!isEmpty(rset.getString("ID_NAT_PRS_ROLE")) &&
         	  				!"CONTACT_PRS".equalsIgnoreCase(pNotVisibleData)) {
         	  			lLink = isEmpty(pGoToLink)?"../crm/clients/contact_prsspecs.jsp?id="+rset.getString("ID_NAT_PRS_ROLE"):lGoLink;
          	  			//html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/clients/contact_prsspecs.jsp?id="+rset.getString("ID_NAT_PRS_ROLE"), "", ""));
         	  		} else if (hasJurPrsPermission && "SNAME_SERVICE_PLACE_WORK".equalsIgnoreCase(mtd.getColumnName(i)) &&
         	  				!isEmpty(rset.getString("ID_SERVICE_PLACE_WORK")) &&
         	  				!"JUR_PRS".equalsIgnoreCase(pNotVisibleData)) {
         	  			lLink = isEmpty(pGoToLink)?"../crm/clients/yurpersonspecs.jsp?id="+rset.getString("ID_SERVICE_PLACE_WORK"):lGoLink;
          	  			//html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/clients/yurpersonspecs.jsp?id="+rset.getString("ID_JUR_PRS"), "", ""));
         	  		} else if (hasUserPermission && "NAME_USER".equalsIgnoreCase(mtd.getColumnName(i)) &&
         	  				!"USER".equalsIgnoreCase(pNotVisibleData)) {
         	  			lLink = isEmpty(pGoToLink)?"../crm/security/userspecs.jsp?id="+rset.getString("ID_USER"):lGoLink;
          	  			//html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/security/userspecs.jsp?id="+rset.getString("ID_USER"), "", ""));
         	  		} else {
         	  			lLink = isEmpty(pGoToLink)?"":lGoLink;
          	  			//html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", "", ""));
          	  		}
          	  		html.append(getBottomFrameTableTDBase("", mtd.getColumnName(i), Types.OTHER, rset.getString(i), lLink, isEmpty(pDivName)?"div_main":pDivName, "", "", ""));
          	  	}
          	    if (!isEmpty(pDeleteLink)) {
                	String myDeleteLink = pEditLink+"&id_user="+rset.getString("ID_USER") + "&process=yes&action=remove";
                    html.append(getDeleteButtonHTML(myDeleteLink, userXML.getfieldTransl("LAB_DELETE_USER", false), rset.getString("NAME_USER_INITIAL")));
                }
                if (!isEmpty(pEditLink)) {
                	String myEditLink = pDeleteLink+"&id_user="+rset.getString("ID_USER");
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
