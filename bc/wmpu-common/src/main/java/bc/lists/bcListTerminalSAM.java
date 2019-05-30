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


public class bcListTerminalSAM extends bc.objects.bcObject {
	private final static Logger LOGGER=Logger.getLogger(bcListTerminalSAM.class);
	
	public bcListTerminalSAM() {
	}

	public String getTerminalSAMHTML(String pWhereCause, ArrayList<bcFeautureParam> pWhereValue, String pFindString, String pSAMStatus, String pEditLink, String pDeleteLink, String p_beg, String p_end) {
		StringBuilder html = new StringBuilder();
        Connection con = null;
        PreparedStatement st = null;
        ArrayList<bcFeautureParam> pParam = initParamArray();

        boolean hasSAMPermission = false;
        boolean hasTerminalPermission = false;
        boolean hasEditPermission = false;
        String mySQL = 
        	" SELECT rn, id_term, id_sam, name_sam_type, expiry_date_frmt, name_sam_status, " +
            "        assign_term_date_beg_frmt, assign_term_date_end_frmt, id_term_sam " +
            "   FROM (SELECT ROWNUM rn, id_term, id_sam, name_sam_type, " +
            "  		 		 DECODE(cd_sam_status, " +
            "          				'U', '<font color=\"green\"><b>'||name_sam_status||'</b></font>', " +
            "          				'S', '<font color=\"red\"><b>'||name_sam_status||'</b></font>', " +
            "          				'B', '<font color=\"red\"><b>'||name_sam_status||'</b></font>', " +
            "          				'R', '<font color=\"red\"><b>'||name_sam_status||'</b></font>', " +
            "          				'F', '<font color=\"blue\"><b>'||name_sam_status||'</b></font>', " +
            "          				name_sam_status" +
            "  		 		 ) name_sam_status, " +
            "                expiry_date_frmt, assign_term_date_beg_frmt, assign_term_date_end_frmt, " +
            "                id_term_sam " +
            "           FROM (SELECT * " +
            "                   FROM " + getGeneralDBScheme() + ".vc_term_sam_all "+
		(isEmpty(pWhereCause)?" WHERE 1=1 ":pWhereCause) ;
        
	   	if (pWhereValue.size() > 0) {
	   		for(int counter=0; counter<=pWhereValue.size()-1;counter++){
	   			pParam.add(pWhereValue.get(counter));
	   		}
	   	}
	        
	    if (!isEmpty(pFindString)) {
    		mySQL = mySQL + 
   				" AND (TO_CHAR(id_term) LIKE UPPER('%'||?||'%') OR " +
   				"      TO_CHAR(id_sam) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(sam_serial_number) LIKE UPPER('%'||?||'%'))";
    		for (int i=0; i<3; i++) {
    		    pParam.add(new bcFeautureParam("string", pFindString));
	    	}
	    }
	    if (!isEmpty(pSAMStatus)) {
	    	mySQL = mySQL + " AND cd_sam_status = ? ";
	    	pParam.add(new bcFeautureParam("string", pSAMStatus));
	    }

        pParam.add(new bcFeautureParam("int", p_end));
        pParam.add(new bcFeautureParam("int", p_beg));
        mySQL = mySQL +
            "   ORDER BY id_sam)  WHERE ROWNUM < ? " + 
            " ) WHERE rn >= ?";
        try{
        	if (isEditMenuPermited("CLIENTS_SAM")>=0) {
        		hasSAMPermission = true;
        	}
        	if (isEditMenuPermited("CLIENTS_TERMINALS")>=0) {
        		hasTerminalPermission = true;
        	}
        	if (isEditPermited("CLIENTS_TERMINALS_SAM")>0) {
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
            for (int i=1; i <= colCount-1; i++) {
            	html.append(getBottomFrameTableTH(samXML, mtd.getColumnName(i)));
            }
            if (hasEditPermission) {
               html.append("<th>&nbsp;</th><th>&nbsp;</th>\n");
            }
            html.append("</tr></thead><tbody>");
            
            while (rset.next())
            {
            	html.append("<tr>");
          	  	for (int i=1; i <= colCount-1; i++) {
          	  		if (hasSAMPermission && "ID_SAM".equalsIgnoreCase(mtd.getColumnName(i))) {
          	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/clients/samspecs.jsp?id="+rset.getString("ID_SAM"), "", ""));
         	  		} else if (hasTerminalPermission && "ID_TERM".equalsIgnoreCase(mtd.getColumnName(i))) {
          	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/clients/terminalspecs.jsp?id="+rset.getString("ID_TERM"), "", ""));
         	  		} else {
          	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", "", ""));
          	  		}
          	  	}
                if (hasEditPermission) {
            	   String myDeleteLink = pEditLink+"&id_term_sam="+rset.getString("ID_TERM_SAM");
                   html.append(getDeleteButtonHTML(myDeleteLink, terminalXML.getfieldTransl("detach_sam", false), rset.getString("ID_SAM")));

            	   String myEditLink = pDeleteLink+"&id_term_sam="+rset.getString("ID_TERM_SAM");
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
