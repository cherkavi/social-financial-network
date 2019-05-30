package bc.objects;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import bc.connection.Connector;
import bc.service.bcFeautureParam;


public class bcIssuerObject extends bcObject {
	private final static Logger LOGGER=Logger.getLogger(bcIssuerObject.class);
	private String idIssuer;
	
	public bcIssuerObject(String pIdIssuer) {
		this.idIssuer = pIdIssuer;
	}
	
    public String getIssuerParametersHTML(String pFindString, String pTabName, String pHyperLink, String p_beg, String p_end) {
        StringBuilder html = new StringBuilder();
        Connection con = null;
        PreparedStatement st = null;

        Statement st_code = null;
        Statement st_state = null;
        ArrayList<String> array_value=new ArrayList<String>();
        ArrayList<String> array_code=new ArrayList<String>();
        String option_code = "";
        
        ArrayList<bcFeautureParam> pParam = initParamArray();

        String mySQL = 
        	" SELECT id_club, name_payment_system, cd_issuer_in_telgr_hex, " +
        	"        exist_flag, exist_flag_tsl, id_payment_system "+
            "   FROM (SELECT ROWNUM rn, id_payment_system, name_payment_system, cd_issuer_in_telgr_hex, " +
            "                exist_flag, exist_flag_tsl, id_club "+
            "           FROM (SELECT * " +
            "                   FROM " + getGeneralDBScheme() + ".vc_issuer_priv_all "+
            "                  WHERE id_issuer = ? ";
        pParam.add(new bcFeautureParam("int", this.idIssuer));
        
    	if (!isEmpty(pFindString)) {
    		mySQL = mySQL + 
   				" AND (UPPER(name_payment_system) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(cd_issuer_in_telgr_hex) LIKE UPPER('%'||?||'%'))";
    		for (int i=0; i<2; i++) {
    		    pParam.add(new bcFeautureParam("string", pFindString));
    		}
    	}
    	pParam.add(new bcFeautureParam("int", p_end));
    	pParam.add(new bcFeautureParam("int", p_beg));
        mySQL = mySQL + 
            "		           ORDER BY id_club, name_payment_system) " +
            "          WHERE ROWNUM < ? " + 
        	" ) WHERE rn >= ?";
        boolean hasEditPermission = false;
        boolean hasClubPermission = false;
        int myCnt = 0;
        try{
        	if (isEditPermited(pTabName)>0) {
        	    hasEditPermission = true;
        	}
        	if (isEditMenuPermited("CLUB_CLUB")>=0) {
        		hasClubPermission = true;
            }
        	
        	LOGGER.debug(prepareSQLToLog(mySQL, pParam));
        	
        	con = Connector.getConnection(getSessionId());
            
        	st_code = con.createStatement();
        	ResultSet rset_code = st_code.executeQuery("SELECT lookup_code FROM " + getGeneralDBScheme() + ".vc_lookup_values WHERE UPPER(name_lookup_type)=UPPER('YES_NO')");
        	
        	while (rset_code.next()) {
        		option_code = "";
        		st_state = con.createStatement();
                ResultSet rset_state = st_state.executeQuery("SELECT lookup_code, meaning FROM " + getGeneralDBScheme() + ".vc_lookup_values WHERE UPPER(name_lookup_type)=UPPER('YES_NO')");
                while (rset_state.next())
                {
                	option_code = option_code + "<option value="+rset_state.getString("lookup_code");
                    if (rset_state.getString("lookup_code").equalsIgnoreCase(rset_code.getString("lookup_code"))) {
                    	option_code = option_code + " SELECTED";
                    }
                    option_code = option_code + ">"+rset_state.getString("meaning")+"</option>";
                }
                array_code.add(rset_code.getString("lookup_code"));
                array_value.add(option_code);
                st_state.close();
        	}
        	st_code.close();
        	
        } // try
        catch (SQLException e) {LOGGER.error(html, e); html.append(showCRMException(e));}
       	catch (Exception el) {LOGGER.error(html, el); html.append(showCRMException(el));}
        finally {
            try {
                if (st_code!=null) st_code.close();
                if (st_state!=null) st_state.close();
            } catch (SQLException w) {w.toString();}
            try {
                if (con!=null) con.close();
            } catch (SQLException w) {w.toString();}
            Connector.closeConnection(con);
        } // finally
        
        try {
        	
        	con = Connector.getConnection(getSessionId());
        	st = con.prepareStatement(mySQL);
        	st = prepareParam(st, pParam);
        	ResultSet rset = st.executeQuery();
            ResultSetMetaData mtd = rset.getMetaData();
            int colCount = mtd.getColumnCount();
            
            if (hasEditPermission) { 
            	html.append("<form action=\"" + pHyperLink + "\" name=\"updateForm\" id=\"updateForm\" accept-charset=\"UTF-8\" method=\"POST\">\n");
            	html.append("<input type=\"hidden\" name=\"type\" value=\"issuer\">\n");
            	html.append("<input type=\"hidden\" name=\"action\" value=\"edit\">\n");
            	html.append("<input type=\"hidden\" name=\"process\" value=\"yes\">\n");
            	html.append("<input type=\"hidden\" name=\"id\" value=\"" + this.idIssuer + "\">\n");
            
            }
            html.append(getBottomFrameTable());
            html.append("<tr>");
            for (int i=1; i <= colCount-2; i++) {
               html.append(getBottomFrameTableTH(issuerXML, mtd.getColumnName(i)));
            }
            html.append("</tr></thead><tbody>");
            while (rset.next())
            {
            	myCnt = myCnt + 1;
            	html.append("<tr>");
          	  	for (int i=1; i <= 2; i++) {
	          	  	if ("ID_CLUB".equalsIgnoreCase(mtd.getColumnName(i)) && hasClubPermission) {
	      	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/club/clubspecs.jsp?id="+rset.getString("ID_CLUB"), "", ""));
	      	  		} else {
	      	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", "", ""));
	      	  		}
          	  	}
                if (hasEditPermission) {
                	html.append("<td align=\"center\">\n");
                	html.append("<input type=\"hidden\" name=\"id_club"+myCnt+"\" value=\""+rset.getString("ID_CLUB")+"\">\n");
               	 	html.append("<input type=\"hidden\" name=\"idpaysys"+myCnt+"\" value=\""+rset.getString("ID_PAYMENT_SYSTEM")+"\">\n");
               	 	html.append("<input type=\"text\" name=\"cdintelgr"+myCnt+"\" size=\"25\" value=\""+getHTMLValue(rset.getString("CD_ISSUER_IN_TELGR_HEX"))+"\" class=\"inputfield\"></td>\n");
               	 	html.append("<td align=\"center\"><select name=\"existflag"+myCnt+"\" class=\"inputfield\" > ");
               	 		for(int counter=0;counter<array_code.size();counter++){
                        	if(array_code.get(counter).equalsIgnoreCase(rset.getString("EXIST_FLAG"))){
                        		html.append(array_value.get(counter));
                        	}
                        }
               	 	html.append(" </select></td>\n");
               	} else {
               		html.append(getBottomFrameTableTD(mtd.getColumnName(3), rset.getString("CD_ISSUER_IN_TELGR_HEX"), "", "", ""));
               		html.append(getBottomFrameTableTD(mtd.getColumnName(5), rset.getString("EXIST_FLAG_TSL"), "", "", ""));
                }
                html.append("</tr>\n");
            }
            
            if (hasEditPermission) {
            	if (myCnt>0) {
           		 html.append("<tr>\n");
           		 html.append("<input type=\"hidden\" name=\"paramcount\" value=\""+myCnt+"\">\n");
                 html.append("<td colspan=\"5\" align=\"center\">\n");
           		 html.append(getSubmitButtonAjax(pHyperLink));
           		 html.append("</td></tr>\n");
           	  }
           	  html.append("</form>\n");
            } 
            
            html.append("</tbody></table>\n");
            
            Connector.closeConnection(con);
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
