package bc.objects;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import bc.connection.Connector;
import bc.service.bcFeautureParam;

public class bcSysLogObject extends bcObject {
	private final static Logger LOGGER=Logger.getLogger(bcSysLogObject.class);
	
	public bcSysLogObject(){
	}

    public String getSysLogHTML(String pFindString, String pOperLog, String pTypeLogEntry, String pIdLogEntry, String idTermSes, String pIdTelgr, String pIdTrans, String pIdReport, String pRowType, String p_beg, String p_end) {
    	ArrayList<bcFeautureParam> pParam = initParamArray();

    	String mySQL = 
        	" SELECT rn, id_log, oper_log, text_log, creation_date, row_type_tsl, row_type" +
            "   FROM (SELECT ROWNUM rn, id_log, oper_log, text_log, creation_date, " +
            "                DECODE(row_type," +
            "                       'U', '<b><font color=\"gray\">'||row_type_tsl||'</font></b>'," +
            "                       'I', row_type_tsl," +
            "                       'W', '<font color=\"blue\">'||row_type_tsl||'</font>'," +
            "                       'E', '<b><font color=\"red\">'||row_type_tsl||'</font></b>'," +
            "                       'C', '<font color=\"green\">'||row_type_tsl||'</font>'," +
            "                       row_type_tsl) " +
            "                row_type_tsl," +
            "                row_type " +
            "           FROM (SELECT id_log, oper_log, text_log, creation_date_frmt creation_date," +
            "                        row_type, row_type_tsl " +
            "                   FROM " + getGeneralDBScheme() + ".vc_sys_log_all " +
            "                  WHERE 1=1 ";
        if (!(pFindString == null || "".equalsIgnoreCase(pFindString))) {
        	mySQL = mySQL + " AND UPPER(text_log) LIKE UPPER('%'||?||'%') ";
        	pParam.add(new bcFeautureParam("string", pFindString));
        }
        if (!(pOperLog == null || "".equalsIgnoreCase(pOperLog))) {
        	mySQL = mySQL + " AND oper_log IN (" + pOperLog + ") ";
        }
        if (!(pTypeLogEntry == null || "".equalsIgnoreCase(pTypeLogEntry))) {
        	mySQL = mySQL + " AND type_log_entry IN (" + pTypeLogEntry + ") ";
        }
        if (!(pIdLogEntry == null || "".equalsIgnoreCase(pIdLogEntry))) {
        	mySQL = mySQL + " AND id_log_entry = ? ";
        	pParam.add(new bcFeautureParam("int", pIdLogEntry));
        }
        if (!(idTermSes == null || "".equalsIgnoreCase(idTermSes))) {
        	mySQL = mySQL + 
        		" AND id_telgr in (SELECT id_telgr " +
        		"                    FROM " + getGeneralDBScheme() + ".vc_telgr_club_all " +
        		"                   WHERE id_term_ses = ?)";
        	pParam.add(new bcFeautureParam("int", idTermSes));
        }
        if (!(pIdTelgr == null || "".equalsIgnoreCase(pIdTelgr))) {
        	mySQL = mySQL + " AND id_telgr = ? ";
        	pParam.add(new bcFeautureParam("int", pIdTelgr));
        }
        if (!(pIdTrans == null || "".equalsIgnoreCase(pIdTrans))) {
        	mySQL = mySQL + 
        		" AND (id_trans = ? OR " + 
        		"       (type_log_entry = 'TRANSACTION' AND id_log_entry = ?) " + 
        		"     ) ";
        	pParam.add(new bcFeautureParam("int", pIdTrans));
        	pParam.add(new bcFeautureParam("int", pIdTrans));
        }
        if (!isEmpty(pRowType)) {
        	mySQL = mySQL + " and NVL(row_type,'U') = ? ";
        	pParam.add(new bcFeautureParam("string", pRowType));
        }
        if (!isEmpty(pIdReport)) {
        	mySQL = mySQL + " AND id_report = ? " ;
        	pParam.add(new bcFeautureParam("int", pIdReport));
        }
        pParam.add(new bcFeautureParam("int", p_end));
        pParam.add(new bcFeautureParam("int", p_beg));
        mySQL = mySQL + " ORDER BY id_log) a WHERE ROWNUM < ?) WHERE rn >= ?";
        
        StringBuilder html = new StringBuilder();
        Connection con = null;
        PreparedStatement st = null; 
        
        String myBgColor = noneBackGroundStyle;
       
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
               html.append(getBottomFrameTableTH(syslogXML, mtd.getColumnName(i)));
            }
            html.append("</tr></thead><tbody>\n");
            
            while (rset.next())
                {
                    if (rset.getString("ROW_TYPE").equalsIgnoreCase("E")) {
                    	myBgColor = selectedBackGroundStyle;
                    } else if (rset.getString("ROW_TYPE").equalsIgnoreCase("W")) {
                    	myBgColor = selectedBackGroundStyle;
                    } else {
                    	myBgColor = noneBackGroundStyle;;
                    }
                    html.append("<tr>");
              	  	for (int i=1; i <= colCount-1; i++) {
              	  		html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", "", myBgColor));
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
    
    public String getSysLogReportHTML(String pIdReport, String pFindString, String pRowType, String p_beg, String p_end) {
    	return getSysLogHTML(pFindString, "", "", "", "", "", "", pIdReport, pRowType, p_beg, p_end);
    }
    
    public String getSysLogTermInterchangeHTML(String pFindString, String pOperLog, String idTermSes, String pIdTelgr, String pIdTrans, String pRowType, String p_beg, String p_end) {
    	return getSysLogHTML(pFindString, pOperLog, "", "", idTermSes, pIdTelgr, pIdTrans, "", pRowType, p_beg, p_end);
    }
}
