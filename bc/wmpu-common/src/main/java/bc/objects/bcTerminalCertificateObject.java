package bc.objects;

import java.net.URLEncoder;
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


public class bcTerminalCertificateObject extends bcObject {
	private final static Logger LOGGER=Logger.getLogger(bcTerminalCertificateObject.class);
	private Map<String,Comparable<String>> fieldHm = new HashMap<String, Comparable<String>>();
	
	private String idCertificateInt;
	private String idTerm;
	
	public bcTerminalCertificateObject(String pIdCertificateInt, String pIdTerm) {
		this.idCertificateInt = pIdCertificateInt;
		this.idTerm = pIdTerm;
	}
	
	public void getFeature() {
		if (!isEmpty(this.idCertificateInt)) {
			String featureSelect = " SELECT * FROM " + getGeneralDBScheme() + ".VC_TERM_CERTIFICATES_PRIV_ALL WHERE id_term_certificate = ?";
			fieldHm = getFeatures2(featureSelect, this.idCertificateInt, false);
		}
	}
	
	public void getInterfaceFeature() {
		if (!isEmpty(this.idCertificateInt)) {
			String featureInterfaceSelect = " SELECT * FROM " + getGeneralDBScheme() + ".VC_TERM_CERTIFICATES_INT_P_ALL WHERE id_term_certificate = ?";
			fieldHm = getFeatures2(featureInterfaceSelect, this.idCertificateInt, false);
		}
	}
	
	public String getValue(String pColumnName) {
		return getFeautureResult(fieldHm, pColumnName);
	}
	
    public String getCurrentCertificatesHTML() {
        StringBuilder html = new StringBuilder();
        Connection con = null;
        PreparedStatement st = null;
        ArrayList<bcFeautureParam> pParam = initParamArray();

        String mySQL = 
        	" SELECT id_term, id_term_certificate, text_certificate, begin_action_date_frmt, end_action_date_frmt,  "+
            "   	 is_certificate_received_tsl, date_certificate_received_frmt, file_name " +
            "   FROM (SELECT ROWNUM rn, id_term, id_term_certificate, text_certificate, begin_action_date_frmt, " +
            "                end_action_date_frmt, is_certificate_received_tsl, date_certificate_received_frmt," +
            "                file_name " +
            " 	 	    FROM " + getGeneralDBScheme() + ".vc_term_certificates_priv_all "+
            " 	 	   WHERE id_term = ? AND is_certificate_current ='Y')"+
            "  ORDER BY id_term_certificate";
        pParam.add(new bcFeautureParam("int", this.idTerm));
        
        boolean hasEditPermission = false;
        boolean hasTerminalPermission = false;
        try{
        	if (isEditPermited("CLIENTS_CERTIFICATE_INFO") >0) {
        		hasEditPermission = true;
        	}
        	if (isEditMenuPermited("CLIENTS_TERMINALS") >0) {
        		hasTerminalPermission = true;
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
            html.append("<th colspan=\"9\"><b>"+ terminalCertificateXML.getfieldTransl( "IS_CERTIFICATE_CURRENT", false) + "</b></th></tr>");
            html.append("<tr>\n");
            for (int i=1; i <= colCount; i++) {
                html.append(getBottomFrameTableTH(terminalCertificateXML, mtd.getColumnName(i)));
            }
            if (hasEditPermission) {
            	html.append("<th>&nbsp;</th>");
            }
            html.append("</tr></thead><tbody>");

            while (rset.next())
            {
                html.append("<tr>\n");
                for (int i=1; i <= colCount; i++) {
          	  		if (hasTerminalPermission && "id_term".equalsIgnoreCase(mtd.getColumnName(i))) {
          	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/clients/terminalspecs.jsp?id="+rset.getString(i), "", ""));
         	  		} else {
          	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", "", ""));
          	  		}
          	  	}
                if (hasEditPermission) {
            	   String myHyperLink = "../crm/clients/certificateupdate.jsp?type=general&filtr_type=C&id_cert="+rset.getString(2)+"&id_term="+this.idTerm + "&process=yes&action=remove_from_term";
                   html.append(getDeleteButtonHTML(myHyperLink));
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
    } // getCurrentCertificatesHTML
	
    public String getNextCertificatesHTML() {
        StringBuilder html = new StringBuilder();
        Connection con = null;
        PreparedStatement st = null;
        ArrayList<bcFeautureParam> pParam = initParamArray();

        String mySQL = 
        	" SELECT id_term, id_term_certificate, text_certificate, begin_action_date_frmt, end_action_date_frmt,  "+
            "   	 need_update_certificate, file_name " +
            "   FROM (SELECT ROWNUM rn, id_term, id_term_certificate, text_certificate, begin_action_date_frmt, " +
            "                end_action_date_frmt, need_update_certificate_tsl need_update_certificate, file_name " +
            " 	 	    FROM " + getGeneralDBScheme() + ".vc_term_certificates_priv_all "+
            " 	 	   WHERE id_term = ? AND is_certificate_next ='Y')"+
            "  ORDER BY id_term_certificate";
        pParam.add(new bcFeautureParam("int", this.idTerm));
        
        boolean hasEditPermission = false;
        boolean hasTerminalPermission = false;
        int myCount = 0;
        try{
        	if (isEditPermited("CLIENTS_CERTIFICATE_INFO") >0) {
        		hasEditPermission = true;
        	}
        	if (isEditMenuPermited("CLIENTS_TERMINALS") >0) {
        		hasTerminalPermission = true;
        	}
        	
        	LOGGER.debug(prepareSQLToLog(mySQL, pParam));
        	con = Connector.getConnection(getSessionId());
        	st = con.prepareStatement(mySQL);
        	st = prepareParam(st, pParam);
        	ResultSet rset = st.executeQuery();
            ResultSetMetaData mtd = rset.getMetaData();
            int colCount = mtd.getColumnCount();

            if (hasEditPermission) {
            	html.append("<form action=\"../crm/clients/certificateupdate.jsp\" name=\"updateForm\" id=\"updateForm\" accept-charset=\"UTF-8\" method=\"POST\">\n");
            	html.append("<input type=\"hidden\" name=\"type\" value=\"revoke_from_term\">\n");
            	html.append("<input type=\"hidden\" name=\"process\" value=\"yes\">\n");
            	html.append("<input type=\"hidden\" name=\"action\" value=\"set\">\n");
            	html.append("<input type=\"hidden\" name=\"id_term\" value=\""+ this.idTerm + "\">\n");
            }
            html.append(getBottomFrameTable());
            html.append("<tr><th colspan=\"9\"><b>"+ terminalCertificateXML.getfieldTransl( "IS_CERTIFICATE_NEXT", false) + "</b></th></tr>");
            html.append("<tr>\n");
            for (int i=1; i <= colCount; i++) {
            	html.append(getBottomFrameTableTH(terminalCertificateXML, mtd.getColumnName(i)));
            }
            if (hasEditPermission) {
            	html.append("<th>&nbsp;</th>");
            }
            html.append("</tr></thead><tbody>");

            while (rset.next())
            {
            	myCount = myCount + 1;
                html.append("<tr>\n");
                for (int i=1; i <= colCount; i++) {
          	  		if (hasTerminalPermission && "id_term".equalsIgnoreCase(mtd.getColumnName(i))) {
          	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/clients/terminalspecs.jsp?id="+rset.getString(i), "", ""));
         	  		} else {
          	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", "", ""));
          	  		}
          	  	}
                if (hasEditPermission) {
                	html.append("<input type=\"hidden\" name=\"id_cert\" value=\""+rset.getString(2)+"\"></td>\n");
                	
                	String myHyperLink = "../crm/clients/certificateupdate.jsp?type=general&filtr_type=C&id_cert="+rset.getString(2)+"&id_term="+this.idTerm + "&process=yes&action=remove_from_term";
                	html.append(getDeleteButtonHTML(myHyperLink));
                }
                html.append("</tr>\n");
            }
            if (myCount > 0 ) {
            	if (hasEditPermission) {
                	html.append("<tr><td colspan=\"9\" align=\"left\">");
                	html.append(getSubmitButtonAjax2("../crm/clients/certificateupdate.jsp", "button_revoke_from_term"));
                	html.append("</td></tr>\n");
                }
            }
            html.append("</tbody></table>\n");
        	html.append("</form>\n");
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
    } // getNextCertificatesHTML

    public String getLoadedCertificatesHTML(String pFindString, String p_beg, String p_end) {
        StringBuilder html = new StringBuilder();
        Connection con = null;
        PreparedStatement st = null;
        ArrayList<bcFeautureParam> pParam = initParamArray();

        String mySQL = 
        	" SELECT id_term, id_term_certificate, text_certificate, begin_action_date_frmt, " +
        	"        end_action_date_frmt, file_name, stored_file_name, stored_full_file_name " +
            "   FROM (SELECT ROWNUM rn, id_term, id_term_certificate, text_certificate, begin_action_date_frmt, " +
            "                end_action_date_frmt, file_name, stored_file_name, stored_full_file_name " +
            " 	 	    FROM (SELECT * " +
            "                   FROM " + getGeneralDBScheme() + ".vc_term_certificates_priv_all "+
            " 	 	           WHERE id_term = ? ";
        pParam.add(new bcFeautureParam("int", this.idTerm));
        
        if (!isEmpty(pFindString)) {
    		mySQL = mySQL + 
   				" AND (TO_CHAR(id_term) LIKE UPPER('%'||?||'%') OR " +
   				"      TO_CHAR(id_term_certificate) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(text_certificate) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(begin_action_date_frmt) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(end_action_date_frmt) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(file_name) LIKE UPPER('%'||?||'%'))";
    		for (int i=0; i<6; i++) {
    		    pParam.add(new bcFeautureParam("string", pFindString));
    		}
    	}
        pParam.add(new bcFeautureParam("int", p_end));
        pParam.add(new bcFeautureParam("int", p_beg));
        mySQL = mySQL +
            "                  ORDER BY begin_action_date DESC, id_term_certificate DESC" +
            "                ) " +
            "          WHERE ROWNUM < ? " + 
        	"        ) " +
        	"  WHERE rn >= ?";
        //
        boolean hasEditPermission = false;
        boolean hasTerminalPermission = false;
        
        try{
        	if (isEditPermited("CLIENTS_CERTIFICATE_LOADED") >0) {
        		hasEditPermission = true;
        	}
        	if (isEditMenuPermited("CLIENTS_TERMINALS") >0) {
        		hasTerminalPermission = true;
        	}
        	
        	LOGGER.debug(prepareSQLToLog(mySQL, pParam));
        	con = Connector.getConnection(getSessionId());
        	st = con.prepareStatement(mySQL);
        	st = prepareParam(st, pParam);
        	ResultSet rset = st.executeQuery();
            ResultSetMetaData mtd = rset.getMetaData();
            int colCount = mtd.getColumnCount();

            if (hasEditPermission) {
            	html.append("<form action=\"../crm/clients/certificateupdate.jsp\" name=\"updateForm\" id=\"updateForm\" accept-charset=\"UTF-8\" method=\"POST\">\n");
            	html.append("<input type=\"hidden\" name=\"type\" value=\"set_to_term\">\n");
            	html.append("<input type=\"hidden\" name=\"process\" value=\"yes\">\n");
            	html.append("<input type=\"hidden\" name=\"action\" value=\"set\">\n");
            	html.append("<input type=\"hidden\" name=\"id_term\" value=\""+ this.idTerm + "\">\n");
            }
            html.append(getBottomFrameTable());
            html.append("<tr>");
            if (hasEditPermission) {
            	html.append("<th>&nbsp;</th>");
            }
            for (int i=1; i <= colCount-2; i++) {
            	html.append(getBottomFrameTableTH(terminalCertificateXML, mtd.getColumnName(i)));
            }
            if (hasEditPermission) {
            	html.append("<th>&nbsp;</th>");
            }
            html.append("</tr></thead><tbody>");
            html.append("<tr>");
            if (hasEditPermission) {
            	html.append("<th colspan=\"9\" align=\"left\">");
            	html.append(getSubmitButtonAjax2("../crm/clients/certificateupdate.jsp", "button_set_to_term"));
            	html.append("</th>\n");
            }
        	html.append("</tr>\n");

            while (rset.next())
            {
                html.append("<tr>\n");
                if (hasEditPermission) {
                	html.append("<td><input type=\"radio\" name=\"id_cert\" value=\""+rset.getString(2)+"\"></td>");
                }
                for (int i=1; i <= colCount-2; i++) {
          	  		if (hasTerminalPermission && "id_term".equalsIgnoreCase(mtd.getColumnName(i))) {
          	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/clients/terminalspecs.jsp?id="+rset.getString(i), "", ""));
          	  		} else if ("id_term_certificate".equalsIgnoreCase(mtd.getColumnName(i))) {
          	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/clients/certificatespecs.jsp?id_term=" + rset.getString("id_term") + "&id_profile=C&id_cert="+rset.getString(i), "", ""));
          	  		} else {
         	  			if ("file_name".equalsIgnoreCase(mtd.getColumnName(i))) {
         	          	  	if (!isEmpty(rset.getString("STORED_FULL_FILE_NAME"))) {
         	          	  		html.append(getBottomFrameTableTD(mtd.getColumnName(i), "<a href=\"../FileSender?FILENAME=" + URLEncoder.encode(rset.getString("STORED_FULL_FILE_NAME"),"UTF-8") + "\" title=\"" + buttonXML.getfieldTransl("open", false) + "\" target=_blank>" + rset.getString("FILE_NAME") + "</a>", "", "", ""));
         	          	  	} else {
             	  				html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", "", ""));
         	          	  	}
         	  			} else {
         	  				html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", "", ""));
         	  			}
          	  		}
          	  	}
                if (hasEditPermission) {
            	   String myHyperLink = "../crm/clients/certificateupdate.jsp?type=general&filtr_type=C&id_cert="+rset.getString(2)+"&id_term="+this.idTerm + "&process=yes&action=remove_from_term";
                   html.append(getDeleteButtonHTML(myHyperLink));
                }
                html.append("</tr>\n");
            }
            if (hasEditPermission) {
            	html.append("<tr><td colspan=\"9\" align=\"left\">");
            	html.append(getSubmitButtonAjax2("../crm/clients/certificateupdate.jsp", "button_set_to_term"));
            	html.append("</td></tr>\n");
            }
            html.append("</tbody></table>\n");
        	html.append("</form>\n");
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
    } // getValidityCertificatesHTML
	
    public String getInterfaceCertificatesHTML(String pFindString, String p_beg, String p_end) {
        StringBuilder html = new StringBuilder();
        Connection con = null;
        PreparedStatement st = null;
        ArrayList<bcFeautureParam> pParam = initParamArray();

        String mySQL = 
        	" SELECT id_term, id_term_certificate, text_certificate, begin_action_date_frmt, " +
        	"        end_action_date_frmt, file_name, stored_file_name, stored_full_file_name " +
            "   FROM (SELECT ROWNUM rn, id_term, id_term_certificate, text_certificate, begin_action_date_frmt, " +
            "                end_action_date_frmt, file_name, stored_file_name, stored_full_file_name " +
            " 	 	    FROM (SELECT * " +
            "                   FROM " + getGeneralDBScheme() + ".vc_term_certificates_int_p_all "+
            " 	 	           WHERE id_term = ? ";
        pParam.add(new bcFeautureParam("int", this.idTerm));
        
        if (!isEmpty(pFindString)) {
    		mySQL = mySQL + 
   				" AND (TO_CHAR(id_term) LIKE UPPER('%'||?||'%') OR " +
   				"      TO_CHAR(id_term_certificate) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(text_certificate) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(begin_action_date_frmt) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(end_action_date_frmt) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(file_name) LIKE UPPER('%'||?||'%'))";
    		for (int i=0; i<6; i++) {
    		    pParam.add(new bcFeautureParam("string", pFindString));
    		}
    	}
        pParam.add(new bcFeautureParam("int", p_end));
        pParam.add(new bcFeautureParam("int", p_beg));
        mySQL = mySQL +
            "                  ORDER BY begin_action_date DESC, id_term_certificate DESC" +
            "                ) " +
            "          WHERE ROWNUM < ? " + 
        	"        ) " +
        	"  WHERE rn >= ?";
        //
        boolean hasEditPermission = false;
        boolean hasTerminalPermission = false;
        
        try{
        	if (isEditPermited("CLIENTS_CERTIFICATE_INTERFACE") >0) {
        		hasEditPermission = true;
        	}
        	if (isEditMenuPermited("CLIENTS_TERMINALS") >0) {
        		hasTerminalPermission = true;
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
            	html.append(getBottomFrameTableTH(terminalCertificateXML, mtd.getColumnName(i)));
            }
            if (hasEditPermission) {
            	html.append("<th>&nbsp;</th>");
            }
            html.append("</tr></thead><tbody>");

            while (rset.next())
            {
                html.append("<tr>\n");
                for (int i=1; i <= colCount-2; i++) {
          	  		if (hasTerminalPermission && "id_term".equalsIgnoreCase(mtd.getColumnName(i))) {
          	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/clients/terminalspecs.jsp?id="+rset.getString(i), "", ""));
          	  		} else if ("id_term_certificate".equalsIgnoreCase(mtd.getColumnName(i))) {
          	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/clients/certificatespecs.jsp?id_term=" + rset.getString("id_term") + "&id_profile=I&id_cert="+rset.getString(i), "", ""));
          	  		} else {
         	  			if ("file_name".equalsIgnoreCase(mtd.getColumnName(i))) {
         	          	  	if (!isEmpty(rset.getString("STORED_FULL_FILE_NAME"))) {
         	          	  		html.append(getBottomFrameTableTD(mtd.getColumnName(i), "<a href=\"../FileSender?FILENAME=" + URLEncoder.encode(rset.getString("STORED_FULL_FILE_NAME"),"UTF-8") + "\" title=\"" + buttonXML.getfieldTransl("open", false) + "\" target=_blank>" + rset.getString("FILE_NAME") + "</a>", "", "", ""));
         	          	  	} else {
             	  				html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", "", ""));
         	          	  	}
         	  			} else {
         	  				html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", "", ""));
         	  			}
          	  		}
          	  	}
                if (hasEditPermission) {
            	   String myHyperLink = "../crm/clients/certificateupdate.jsp?type=general&filtr_type=I&id_cert="+rset.getString(2)+"&id_term="+this.idTerm + "&process=yes&action=remove_from_term";
                   html.append(getDeleteButtonHTML(myHyperLink));
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
    } // getEndOfTermCertificatesHTML

	
}
