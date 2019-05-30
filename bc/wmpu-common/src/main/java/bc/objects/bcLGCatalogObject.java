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

public class bcLGCatalogObject extends bcObject {
	private final static Logger LOGGER=Logger.getLogger(bcLGCatalogObject.class);
	
	private Map<String,Comparable<String>> fieldHm = new HashMap<String, Comparable<String>>();
	
	private String idProduction;
	
	public bcLGCatalogObject(String pIdProduction) {
		this.idProduction = pIdProduction;
		getFeature();
	}
	
	private void getFeature() {
		String mySQL = 
			" SELECT * FROM " + getGeneralDBScheme() + ".VC_LG_CATALOG_CLUB_ALL WHERE id_lg_production = ?";
		fieldHm = getFeatures2(mySQL, this.idProduction, false);
	}
	
	public String getValue(String pColumnName) {
		return getFeautureResult(fieldHm, pColumnName);
	}
	
    public String getTransferDataHTML(String pFindString, String p_beg, String p_end) {
        StringBuilder html = new StringBuilder();
        Connection con = null;
        PreparedStatement st = null;

        ArrayList<bcFeautureParam> pParam = initParamArray();

        String mySQL = 
        	" SELECT id_lg_record, action_date_frmt, sname_jur_prs_receiver, sname_jur_prs_sender, " +
            "        count_lg_production, id_jur_prs_receiver, id_jur_prs_sender "+
            "   FROM (SELECT ROWNUM rn, id_lg_record, action_date_frmt, " +
            "				 sname_jur_prs_receiver, sname_jur_prs_sender, count_lg_production, " +
            "                id_jur_prs_receiver, id_jur_prs_sender "+
            "           FROM (SELECT * " +
            "                   FROM " + getGeneralDBScheme() + ".vc_lg_others_all "+
            "                  WHERE id_lg_production = ? ";
        pParam.add(new bcFeautureParam("int", this.idProduction));
        
    	if (!isEmpty(pFindString)) {
    		mySQL = mySQL + 
   				" AND (TO_CHAR(id_sam) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(sam_serial_number) LIKE UPPER('%'||?||'%'))";
    		for (int i=0; i<2; i++) {
    		    pParam.add(new bcFeautureParam("string", pFindString));
    		}
    	}
    	pParam.add(new bcFeautureParam("int", p_end));
    	pParam.add(new bcFeautureParam("int", p_beg));
    	mySQL = mySQL +
            "                  ORDER BY action_date_frmt DESC, sname_jur_prs_receiver, sname_jur_prs_sender) " +
            "          WHERE ROWNUM < ? " + 
            " ) WHERE rn >= ?";
        
        boolean hasOtherTransferPermission = false;
        boolean hasJurPrsPermission = false;
        
        try{
        	if (isEditMenuPermited("LOGISTIC_PARTNERS_OTHERS")>=0) {
        		hasOtherTransferPermission = true;
        	}
        	if (isEditMenuPermited("CLIENTS_YURPERSONS")>=0) {
        		hasJurPrsPermission = true;
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
               html.append(getBottomFrameTableTH(logisticXML, mtd.getColumnName(i)));
            }
            html.append("</tr></thead><tbody>");
            while (rset.next())
            {
            	html.append("<tr>");
          	  	for (int i=1; i <= colCount-2; i++) {
          	  		if ("ID_LG_RECORD".equalsIgnoreCase(mtd.getColumnName(i)) && hasOtherTransferPermission) {
          	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/logistic/partners/otherspecs.jsp?id=" + rset.getString("ID_LG_RECORD"), "", ""));
          	  		} else if ("SNAME_JUR_PRS_RECEIVER".equalsIgnoreCase(mtd.getColumnName(i)) && hasJurPrsPermission) {
          	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/clients/yurpersonspecs.jsp?id=" + rset.getString("ID_JUR_PRS_RECEIVER"), "", ""));
          	  		} else if ("SNAME_JUR_PRS_SENDER".equalsIgnoreCase(mtd.getColumnName(i)) && hasJurPrsPermission) {
          	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/clients/yurpersonspecs.jsp?id=" + rset.getString("ID_JUR_PRS_SENDER"), "", ""));
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
}
