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

public class bcDsPartnerPatternObject extends bcObject {
	private final static Logger LOGGER=Logger.getLogger(bcDsPartnerPatternObject.class);
	private Map<String,Comparable<String>> fieldHm = new HashMap<String, Comparable<String>>();
	
	private String idPattern;
	
	public bcDsPartnerPatternObject(String pIdPattern){
		this.idPattern = pIdPattern;
		getFeature();
	}
	
	private void getFeature() {
		String featureSelect = " SELECT * FROM " + getGeneralDBScheme() + ".VC_DS_PT_PATTERN_CLUB_ALL WHERE id_pt_pattern = ?";
		fieldHm = getFeatures2(featureSelect, this.idPattern, false);
	}
	
	public String getValue(String pColumnName) {
		return getFeautureResult(fieldHm, pColumnName);
	}

    public String getMessagesHTML(String pTabName, String pOperationType, String pIsArchive, String p_beg, String p_end) {
    	StringBuilder html = new StringBuilder();
    	Connection con = null;
    	PreparedStatement st = null;
	    
	    boolean hasTermPermission = false;	    
	    boolean hasPartnerPermission = false;   
	    boolean hasServicePlacePermission = false;        
	    boolean hasEmailMessagePermission = false;        
	    boolean hasOfficeMessagePermission = false;  
	    boolean hasTermMessagePermission = false;
	    boolean hasContactPrsPermission = false;
	    
	    String mySQL = "";
	    String jurPrsHyperLink = "";
	    
	    ArrayList<bcFeautureParam> pParam = initParamArray();

	    mySQL = 
	    	" SELECT id_message, type_message, sname_jur_prs, name_service_place, " +
			"        name_contact_prs, email, id_term, title_message, sendings_quantity, " +
			"        error_sendings_quantity, event_date_frmt, state_record_tsl, is_archive_tsl, " +
			"        id_jur_prs, id_service_place, id_contact_prs, type_message2 " +
			"   FROM (SELECT ROWNUM rn, id_message, type_message, sname_jur_prs, name_service_place, " +
			"                name_contact_prs, email, id_term, title_message, sendings_quantity, " +
			"                error_sendings_quantity, event_date_frmt, state_record_tsl, is_archive_tsl, " +
			"                id_jur_prs, id_service_place, id_contact_prs, type_message2 " +
			"   		FROM (SELECT id_message, type_message, sname_jur_prs, name_service_place, " +
			"                		 name_contact_prs, email, id_term, title_message, sendings_quantity, " +
			"                		 error_sendings_quantity, event_date_frmt, state_record_tsl, is_archive_tsl, " +
			"                		 id_jur_prs, id_service_place, id_contact_prs, type_message2 " +
			"           		FROM (SELECT a.id_message, a.type_message_tsl type_message, a.sname_jur_prs, a.name_service_place, " +
			"                        		 a.name_contact_prs, a.email, NULL id_term, " +
			"                        		 a.title_message, a.sendings_quantity, a.error_sendings_quantity, a.event_date_frmt, " +
			"                		 		 DECODE(a.state_record, " +
	  		"                						'PREPARED', '<b><font color=\"black\">'||a.state_record_tsl||'</font></b>', " +
			"                      					'NEW', '<font color=\"black\">'||a.state_record_tsl||'</font>', " +
			"                      					'EXECUTED', '<font color=\"green\">'||a.state_record_tsl||'</font>', " +
			"                      					'EXECUTED_PARTIALLY', '<font color=\"darkgreen\">'||a.state_record_tsl||'</font>', " +
			"                      					'ERROR', '<b><font color=\"red\">'||a.state_record_tsl||'</font></b>', " +
			"                      					'CARRIED_OUT_PARTIALLY', '<b><font color=\"darkgray\">'||a.state_record_tsl||'</font></b>', " +
			"                      					'CARRIED_OUT', '<font color=\"gray\">'||a.state_record_tsl||'</font>', " +
			"                      					'CANCELED', '<font color=\"blue\">'||a.state_record_tsl||'</font>', " +
			"                      					a.state_record_tsl) " +
			"						 		 state_record_tsl, " +
			"                        		 a.is_archive, " +
			"  				   		 		 DECODE(a.is_archive, " +
			"                         				'Y', '<b><font color=\"red\">'||a.is_archive_tsl||'</font></b>', " +
			"                        				a.is_archive_tsl) " +
			"						 		 is_archive_tsl, " +
			"                		 		 a.id_jur_prs, a.id_service_place, a.id_contact_prs, a.type_message type_message2 " +
			"                   	    FROM " + getGeneralDBScheme()+".vc_contact_prs_mes_club_all a " +
			"                  		   WHERE a.id_pt_pattern = ? " + 
			"           	  		  UNION ALL" +
			"				  		  SELECT a.id_message, 'TERM' type_message, a.sname_jur_prs, a.name_service_place, " +
			"                        		 NULL name_contact_prs, NULL email, a.id_term, " +
			"                        		 a.text_message title_message, a.sendings_quantity, NULL error_sendings_quantity, event_date_frmt, " +
			"               		 		 DECODE(a.state_record, " +
	  		"                      					'PREPARED', '<b><font color=\"black\">'||a.state_record_tsl||'</font></b>', " +
			"                      					'NEW', '<font color=\"black\">'||a.state_record_tsl||'</font>', " +
			"                      					'EXECUTED', '<font color=\"green\">'||a.state_record_tsl||'</font>', " +
			"                      					'EXECUTED_PARTIALLY', '<font color=\"darkgreen\">'||a.state_record_tsl||'</font>', " +
			"                      					'ERROR', '<b><font color=\"red\">'||a.state_record_tsl||'</font></b>', " +
			"                      					'CARRIED_OUT_PARTIALLY', '<b><font color=\"darkgray\">'||a.state_record_tsl||'</font></b>', " +
			"                      					'CARRIED_OUT', '<font color=\"gray\">'||a.state_record_tsl||'</font>', " +
			"                      					'CANCELED', '<font color=\"blue\">'||a.state_record_tsl||'</font>', " +
			"                      					state_record_tsl) " +
			"                        		 state_record_tsl, " +
			"                        		 a.is_archive, " +
			"  				       	 		 DECODE(a.is_archive, " +
			"                             			'Y', '<b><font color=\"red\">'||a.is_archive_tsl||'</font></b>', " +
			"                             			a.is_archive_tsl) " +
			"                        		 is_archive_tsl, " +
			"                        		 NULL id_jur_prs, a.id_service_place, NULL id_contact_prs, 'TERM' type_message2 " +
			"                   		FROM " + getGeneralDBScheme()+".vc_term_messages_club_all a " +
			"                  		   WHERE a.id_pt_pattern = ? " + 
			"				) WHERE 1=1 ";
	    pParam.add(new bcFeautureParam("int", this.idPattern));
	    pParam.add(new bcFeautureParam("int", this.idPattern));
	    
	    if (!isEmpty(pIsArchive)) {
		   	mySQL = mySQL + " AND is_archive = ? ";
		   	pParam.add(new bcFeautureParam("string", pIsArchive));
		}
	    pParam.add(new bcFeautureParam("int", p_end));
	    pParam.add(new bcFeautureParam("int", p_beg));
	    mySQL = mySQL +
			" 				   ORDER BY id_message desc) " +
	    	"          WHERE ROWNUM < ? " +
	    	"		 ) " +
	    	"  WHERE rn >= ? ";
	    	
	    
        boolean hasEditPermission = false;
        
	    try{
	    	if (isEditMenuPermited("CLIENTS_TERMINALS")>=0) {
	    		hasTermPermission = true;
        	}
        	if (isEditMenuPermited("CLIENTS_YURPERSONS")>=0) {
        		hasPartnerPermission = true;
        	}
        	if (isEditMenuPermited("CLIENTS_SERVICE_PLACE")>=0) {
        		hasServicePlacePermission = true;
        	}
	    	if (isEditMenuPermited("DISPATCH_MESSAGES_EMAIL")>=0) {
	    		hasEmailMessagePermission = true;
        	}
	    	if (isEditMenuPermited("DISPATCH_MESSAGES_OFFICE")>=0) {
	    		hasOfficeMessagePermission = true;
        	}
	    	if (isEditMenuPermited("DISPATCH_MESSAGES_TERM")>=0) {
	    		hasTermMessagePermission = true;
        	}
	    	if (isEditMenuPermited("CLIENTS_CONTACT_PRS")>=0) {
	    		hasContactPrsPermission = true;
        	}
       	 	if (isEditPermited(pTabName)>0) {
       	 		hasEditPermission = true;
        	}
	    	
	    	LOGGER.debug(prepareSQLToLog(mySQL, pParam));
	    	con = Connector.getConnection(getSessionId());
	    	st = con.prepareStatement(mySQL);
	    	st = prepareParam(st, pParam);
	    	ResultSet rset = st.executeQuery();
            ResultSetMetaData mtd = rset.getMetaData();
            
            int colCount = mtd.getColumnCount();

            if (hasEditPermission) {
            	html.append("<form method=\"Post\" name=\"updateForm\" id=\"updateForm\" action=\"../crm/dispatch/patterns/partner_patternupdate.jsp\" onSubmit=\"return CheckSelect(this);\">\n");
            	html.append("<input type=\"hidden\" name=\"type\" value=\"general\">\n");
           	 	html.append("<input type=\"hidden\" name=\"action\" value=\"execute\">\n");
           	 	html.append("<input type=\"hidden\" name=\"process\" value=\"yes\">\n");
           	 	html.append("<input type=\"hidden\" name=\"id\" value=\"" + this.idPattern + "\">\n");
            }
            html.append(getBottomFrameTable());
	        html.append("<tr>\n");
	        if (hasEditPermission) {
	        	String sendSelected = "";
	           	 String deleteSelected = "";
	           	 String prepareSelected = "";
	           	 //String cancelSelected = "";
	           	 String toArchiveSelected = "";
	           	 String fromArchiveSelected = "";
	           	 String deliveredSelected = "";
	           	 String errorSelected = "";
          	 
	           	 if ("send".equalsIgnoreCase(pOperationType)) {
	           		sendSelected = " SELECTED ";
		       	 } else if ("delete".equalsIgnoreCase(pOperationType)) {
		       		 deleteSelected = " SELECTED ";
		       	 } else if ("prepare".equalsIgnoreCase(pOperationType)) {
		       		 prepareSelected = " SELECTED ";
		       	 } else if ("to_archive".equalsIgnoreCase(pOperationType)) {
		       		 toArchiveSelected = " SELECTED ";
		       	 } else if ("from_archive".equalsIgnoreCase(pOperationType)) {
		       		 fromArchiveSelected = " SELECTED ";
		       	 } else if ("delivered".equalsIgnoreCase(pOperationType)) {
		       		 deliveredSelected = " SELECTED ";
		       	 } else if ("error".equalsIgnoreCase(pOperationType)) {
		       		 errorSelected = " SELECTED ";
		       	 }
		           	 html.append("<th>");
		           	 html.append("<select id=\"operation_type\" name=\"operation_type\" class=\"inputfield\">");
		           	 html.append("<option value=\"send\" " + sendSelected + ">" + messageXML.getfieldTransl("h_operation_send", false) + "</option>");
		           	 html.append("<option value=\"delete\" " + deleteSelected + ">" + messageXML.getfieldTransl("h_operation_delete", false) + "</option>");
		           	 html.append("<option value=\"prepare\" " + prepareSelected + ">" + messageXML.getfieldTransl("h_operation_prepare", false) + "</option>");
		           	 html.append("<option value=\"to_archive\" " + toArchiveSelected + ">" + messageXML.getfieldTransl("h_operation_to_archive", false) + "</option>");
		           	 html.append("<option value=\"from_archive\" " + fromArchiveSelected + ">" + messageXML.getfieldTransl("h_operation_from_archive", false) + "</option>");
		           	 html.append("<option value=\"delivered\" " + deliveredSelected + ">" + messageXML.getfieldTransl("h_operation_delivered", false) + "</option>");
		           	 html.append("<option value=\"error\" " + errorSelected + ">" + messageXML.getfieldTransl("h_operation_error", false) + "</option>");
		           	 html.append("</select><br>");
		        	 html.append(getSubmitButtonAjax3("../crm/dispatch/patterns/partner_patternupdate.jsp", "submit", "CheckSelect(document.getElementById('updateForm'))") + "<br>");
		           	 html.append("<input name=\"mainCheck\" id=\"mainCheck\" type=\"checkbox\" onclick=\"CheckAll(this,'chb')\"></th>\n");
	           }
	        for (int i=1; i <= colCount-4; i++) {
	        	html.append(getBottomFrameTableTH(messageXML, mtd.getColumnName(i)));
            }
	        html.append("</tr></thead><tbody>");
	        
	        while (rset.next()) {
	        	html.append("<tr>\n");
                if (hasEditPermission) {
             		 html.append("<td align=\"center\"><input type=\"checkbox\" name=\"chb"+rset.getString("ID_MESSAGE")+"_"+rset.getString("TYPE_MESSAGE2")+"\" value=\"\" id=\"chb"+rset.getString("ID_MESSAGE")+"_"+rset.getString("TYPE_MESSAGE2")+"\" onclick=\"return CheckCB(this);\"></td>\n");
             	}
		        for (int i=1; i <= colCount-4; i++) {
	        		if ("ID_TERM".equalsIgnoreCase(mtd.getColumnName(i)) && 
	        				hasTermPermission &&
	        				(!isEmpty(rset.getString(i)))) {
	        			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/clients/terminalspecs.jsp?id="+rset.getString(i), "", ""));
	        		} else if ("NAME_SERVICE_PLACE".equalsIgnoreCase(mtd.getColumnName(i)) && 
	        				hasServicePlacePermission &&
	        				(!isEmpty(rset.getString("ID_SERVICE_PLACE")))) {
	        			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/clients/service_placespecs.jsp?id="+rset.getString("ID_SERVICE_PLACE"), "", ""));
	        		} else if ("ID_MESSAGE".equalsIgnoreCase(mtd.getColumnName(i)) &&
	        				"EMAIL".equalsIgnoreCase(rset.getString("TYPE_MESSAGE2")) &&
	        				hasEmailMessagePermission) {
	        			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/dispatch/messages/email_messagesspecs.jsp?id="+rset.getString(i), "", ""));
	        		} else if ("ID_MESSAGE".equalsIgnoreCase(mtd.getColumnName(i)) && 
	        				"OFFICE".equalsIgnoreCase(rset.getString("TYPE_MESSAGE2")) &&
	        				hasOfficeMessagePermission) {
	        			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/dispatch/messages/office_messagesspecs.jsp?id="+rset.getString(i), "", ""));
	        		} else if ("ID_MESSAGE".equalsIgnoreCase(mtd.getColumnName(i)) && 
	        				"TERM".equalsIgnoreCase(rset.getString("TYPE_MESSAGE2")) &&
	        				hasTermMessagePermission) {
	        			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/dispatch/messages/term_messagesspecs.jsp?id="+rset.getString(i), "", ""));
	        		} else if ("SNAME_JUR_PRS".equalsIgnoreCase(mtd.getColumnName(i)) && 
	        				hasPartnerPermission &&
	        				(!isEmpty(rset.getString("ID_JUR_PRS")))) {
	        	    	jurPrsHyperLink = "../crm/clients/yurpersonspecs.jsp?id="+rset.getString("ID_JUR_PRS");
	                	html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), jurPrsHyperLink, "", ""));
	        		} else if ("NAME_CONTACT_PRS".equalsIgnoreCase(mtd.getColumnName(i)) && 
	        				hasContactPrsPermission &&
	        				(!isEmpty(rset.getString("ID_CONTACT_PRS")))) {
	        			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/clients/contact_prsspecs.jsp?id="+rset.getString("ID_CONTACT_PRS"), "", ""));
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
	  } // getMessagesHTML

    public String getAttachedFilesHTML(String pPatternType, boolean hasEditPermission) {
        StringBuilder html = new StringBuilder();
        Connection con = null;
        PreparedStatement st = null;
        String mySQL = 
        	" SELECT id_pattern_file, file_name, stored_file_name " +
            "   FROM " + getGeneralDBScheme() + ".vc_ds_pt_pattern_files_all" +
            "  WHERE id_pt_pattern = ? " +  
            "    AND type_pt_pattern = ? " + 
            "  ORDER BY file_name";
        String pType = "";
        if ("EMAIL".equalsIgnoreCase(pPatternType)) {
        	pType = "email";
        } else if ("OFFICE".equalsIgnoreCase(pPatternType)) {
        	pType = "office";
        }
        try{
        	LOGGER.debug(mySQL + 
            		", 1={" + this.idPattern + ",int}" + 
	    			", 2={'" + pPatternType + "',string}");
        	con = Connector.getConnection(getSessionId());
            st = con.prepareStatement(mySQL);
            st.setInt(1, Integer.parseInt(this.idPattern));
            st.setString(2, pPatternType);
            ResultSet rset = st.executeQuery();
            
            while (rset.next())
            {
          	  	html.append(rset.getString("file_name") + 
   	  				"&nbsp;&nbsp;<a href=\"../FileSender?FILENAME=" + URLEncoder.encode(rset.getString("stored_file_name"),"UTF-8") + "\" " +
					"  title=\"" + buttonXML.getfieldTransl("open", false) + "\" target=_blank>" +
					"<img vspace=\"0\" hspace=\"0\" src=\"../images/oper/small/open.gif\" align=\"top\" style=\"border: 0px;\"> " +
					"</a>");
      	  		if (hasEditPermission) {
       	  			html.append("&nbsp;<a href=\"#\" onclick=\"if (window.confirm('" + documentXML.getfieldTransl("l_remove_file", false) + " \\\'" + rset.getString("file_name") + "\\\'?')) {ajaxpage('../crm/dispatch/patterns/partner_patternupdate.jsp?id=" + this.idPattern + "&id_file=" + rset.getString("ID_PATTERN_FILE") +  "&type=" + pType + "&&process=yes&action=remove_attached_file&id_file=" + rset.getString("id_pattern_file") + "', 'div_main')}\" " +
  						" title=\"" + buttonXML.getfieldTransl("button_delete", false) + "\"> " + 
  						"<img vspace=\"0\" hspace=\"0\" src=\"../images/oper/small/delete.gif\" align=\"top\" style=\"border: 0px;\">" +
      	  				"</a>");
       	  		}
       	  		html.append("<br>");
            }
        }
         // try
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
    } // getTermMessagesActionsHTML
}
