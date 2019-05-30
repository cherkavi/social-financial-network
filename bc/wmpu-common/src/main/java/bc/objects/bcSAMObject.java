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
import bc.lists.bcListTerminalSAM;
import bc.service.bcFeautureParam;

public class bcSAMObject extends bcObject {
	private final static Logger LOGGER=Logger.getLogger(bcSAMObject.class);
	private Map<String,Comparable<String>> fieldHm = new HashMap<String, Comparable<String>>();
	
	private String idSAM;
	
	public bcSAMObject(String pIdSAM){
		this.idSAM = pIdSAM;
		getFeature();
	}
	
	private void getFeature() {
		String featureSelect = " SELECT * FROM " + getGeneralDBScheme() + ".VC_SAM_CLUB_ALL WHERE id_sam = ?";
		fieldHm = getFeatures2(featureSelect, this.idSAM, true);
	}
	
	public String getValue(String pColumnName) {
		return getFeautureResult(fieldHm, pColumnName);
	}

    public String getTermSAMHTML(String pFindString, String pSAMStatus, String p_beg, String p_end) {
    	bcListTerminalSAM list = new bcListTerminalSAM();
    	
    	String pWhereCause = " WHERE id_sam = ? ";
    	
    	ArrayList<bcFeautureParam> pWhereValue = new ArrayList<bcFeautureParam>();
    	pWhereValue.add(new bcFeautureParam("int", this.idSAM));
    	
    	String myDeleteLink = "../crm/clients/samupdate.jsp?type=term&id="+this.idSAM+"&action=remove&process=yes";
	    String myEditLink = "../crm/clients/samupdate.jsp?type=term&id="+this.idSAM;
    	
    	return list.getTerminalSAMHTML(pWhereCause, pWhereValue, pFindString, pSAMStatus, myEditLink, myDeleteLink, p_beg, p_end);
    	
    }
	
    public String getLogisticHTML(String pFindString, String p_beg, String p_end) {
        StringBuilder html = new StringBuilder();
        Connection con = null;
        PreparedStatement st = null;
        ArrayList<bcFeautureParam> pParam = initParamArray();
        String mySQL = 
        	"SELECT rn, action_date_frmt, operation_desc, sam_count, " +
        	"		sname_jur_prs_receiver, sname_jur_prs_sender," +
        	"       id_jur_prs_receiver, id_jur_prs_sender, id_lg_record " +
        	"  FROM (SELECT ROWNUM rn, id_lg_record, operation_desc, sam_count, " +
        	"               sname_jur_prs_receiver, sname_jur_prs_sender, action_date_frmt," +
        	"               id_jur_prs_receiver, id_jur_prs_sender " +
        	"		   FROM (SELECT * " +
            "                   FROM " + getGeneralDBScheme() + ".vc_lg_sam_priv_all "+
            "                  WHERE id_lg_record IN " +
            "                       (SELECT id_lg_record " +
            "                          FROM " + getGeneralDBScheme() + ".vc_lg_sam_all " +
            "                         WHERE id_sam = ?)";
        pParam.add(new bcFeautureParam("string", this.idSAM));
        
    	if (!(pFindString==null || "".equalsIgnoreCase(pFindString))) {
    		mySQL = mySQL + 
   				" AND (TO_CHAR(id_lg_record) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(action_date_frmt) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(operation_desc) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(sname_jur_prs_receiver) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(sname_jur_prs_sender) LIKE UPPER('%'||?||'%'))";
    		for (int i=0; i<5; i++) {
    		    pParam.add(new bcFeautureParam("string", pFindString));
    		}
    	}
    	pParam.add(new bcFeautureParam("int", p_end));
    	pParam.add(new bcFeautureParam("int", p_beg));
        mySQL = mySQL +
            "                  ORDER BY action_date) " +
            "          WHERE ROWNUM < ? " + 
            " ) WHERE rn >= ?";
        
        boolean hasLogisticPermission = false;
        boolean hasJurPrsPermission = false;
        
        try{
        	if (isEditMenuPermited("LOGISTIC_PARTNERS_SAMS")>=0) {
        		hasLogisticPermission = true;
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
            for (int i=1; i <= colCount-3; i++) {
                html.append(getBottomFrameTableTH(logisticXML, mtd.getColumnName(i)));
            }
            html.append("</tr></thead><tbody>");
            while (rset.next())
            {
            	html.append("<tr>");
          	  	for (int i=1; i <= colCount-3; i++) {
          	  		if (hasLogisticPermission && "ACTION_DATE_FRMT".equalsIgnoreCase(mtd.getColumnName(i))) {
          	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/logistic/partners/samspecs.jsp?id="+rset.getString(i), "", ""));
	      	  		} else if ("SNAME_JUR_PRS_RECEIVER".equalsIgnoreCase(mtd.getColumnName(i)) &&
	      	  				hasJurPrsPermission &&
	      	  				!(rset.getString("ID_JUR_PRS_RECEIVER")==null || "".equalsIgnoreCase(rset.getString("ID_JUR_PRS_RECEIVER")))) {
	                    html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/clients/yurpersonspecs.jsp?id="+rset.getString("ID_JUR_PRS_RECEIVER"), "", ""));
	      	  		} else if ("SNAME_JUR_PRS_SENDER".equalsIgnoreCase(mtd.getColumnName(i)) &&
	      	  				hasJurPrsPermission &&
	      	  				!(rset.getString("ID_JUR_PRS_SENDER")==null || "".equalsIgnoreCase(rset.getString("ID_JUR_PRS_SENDER")))) {
	      	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/clients/yurpersonspecs.jsp?id="+rset.getString("ID_JUR_PRS_SENDER"), "", ""));
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
    } //getAssignTermHistoryHTML
}
