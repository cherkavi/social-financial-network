package bc.objects;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import bc.connection.Connector;
import bc.service.bcFeautureParam;


public class bcReportObject extends bcObject {
	private final static Logger LOGGER=Logger.getLogger(bcReportObject.class);
	private Map<String,Comparable<String>> fieldHm = new HashMap<String, Comparable<String>>();
	
	private String idReport;
	
	public bcReportObject(String pIdReport) {
		this.idReport = pIdReport;
		getFeature();
	}
	
	private void getFeature() {
		String featureSelect = " SELECT * FROM " + getGeneralDBScheme() + ".VC_USER_REPORTS_ALL WHERE id_report = ?";
		fieldHm = getFeatures2(featureSelect, this.idReport, false);
	}
	
	public String getValue(String pColumnName) {
		return getFeautureResult(fieldHm, pColumnName);
	}
	
    public String getReportParametersHTML() {
	        StringBuilder html = new StringBuilder();
	        Connection con = null;
	        PreparedStatement st = null;
	        ArrayList<bcFeautureParam> pParam = initParamArray();
	        String mySQL = 
	        	"SELECT * "+
           		"  FROM " + getGeneralDBScheme() + ".vc_user_reports_param_all "+
           		" WHERE id_report = ? ";
	        pParam.add(new bcFeautureParam("int", this.idReport));
	        try{
	        	LOGGER.debug(prepareSQLToLog(mySQL, pParam));
	        	con = Connector.getConnection(getSessionId());
	        	st = con.prepareStatement(mySQL);
	        	st = prepareParam(st, pParam);
	        	ResultSet rset = st.executeQuery();
	            html.append(getBottomFrameTable());
	            html.append("<form action=\"../crm/reports/rep_execute.jsp\" name=\"updateForm\" id=\"updateForm\" accept-charset=\"UTF-8\" method=\"POST\">");
	            html.append("<input type=\"hidden\" name=\"id_report\" value=\""+this.idReport+"\">");
	            
	            while (rset.next())
	            {
	            	html.append("<tr>\n");
	            	html.append("<td>" + getValue(rset.getString("NAME_PARAM")) + "</td>\n");
	            	html.append("<td><input type=\"text\" name=\""+getValue(rset.getString("CD_PARAM"))+"\" size=\"70\" value=\""+getValue(rset.getString("DEFAULT_VALUE"))+"\" readonly class=\"inputfield-ro\"></td>\n");
	            	html.append("</tr>\n");
	            }

	            html.append("<tr>\n");
	            html.append("<td colspan=\"4\" align=\"center\">");
	            html.append(getSubmitButtonAjax("../crm/reports/rep_execute.jsp"));
	            html.append("</td>\n");
	            html.append("</tr>\n");
	            html.append("</form>");
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
	    } // getReportParametersHTML


}
