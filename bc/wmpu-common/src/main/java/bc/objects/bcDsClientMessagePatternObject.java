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

public class bcDsClientMessagePatternObject extends bcObject {
	private final static Logger LOGGER=Logger.getLogger(bcDsClientMessagePatternObject.class);
	
	private Map<String,Comparable<String>> fieldHm = new HashMap<String, Comparable<String>>();
	
	private String idPattern;
	
	public bcDsClientMessagePatternObject(String pIdPattern) {
		this.idPattern = pIdPattern;
		getFeature();
	}
	
	private void getFeature() {
		String mySQL = " SELECT * FROM " + getGeneralDBScheme() + ".VC_DS_CL_PATTERN_CLUB_ALL WHERE id_cl_pattern = ?";
        fieldHm = getFeatures2(mySQL, this.idPattern, false);
	}
	
	public String getValue(String pColumnName) {
		return getFeautureResult(fieldHm, pColumnName);
	}

	public String getMessagesAppliedHTML(String pOperationType, String pFindString, String pTypeMessage, String pIsArchive, String p_beg, String p_end) {

		ArrayList<bcFeautureParam> pParam = initParamArray();

		String mySQL = 
    		"SELECT rn, id_message, type_message, full_name_nat_prs, recepient, text_message, " + 
    		"		sendings_quantity, error_sendings_quantity, event_date_frmt, state_record_tsl, is_archive_tsl, type_message2, " + 
    		"		card_serial_number, card_id_issuer, card_id_payment_system, id_nat_prs " +
    		"  FROM (SELECT ROWNUM rn, id_message, type_message, full_name_nat_prs, recepient, text_message, " + 
    		"				sendings_quantity, error_sendings_quantity, event_date_frmt, state_record_tsl, is_archive_tsl, type_message2, " + 
    		"				card_serial_number, card_id_issuer, card_id_payment_system, id_nat_prs " +
    		"          FROM (SELECT id_message, type_message_tsl type_message, full_name_nat_prs, recepient, " +
    		"						CASE WHEN LENGTH(text_message) > 200 " +
    		"                            THEN SUBSTR(text_message,1,197)||'...'" +
    		"                            ELSE text_message" +
    		"                       END text_message, " + 
    		"						sendings_quantity, error_sendings_quantity, event_date_frmt, " +
    		"               		DECODE(state_record, " +
    		"								/* Состояния сообщений, кроме SMS */" +
  		  	"                      			'PREPARED', '<b><font color=\"black\">'||state_record_tsl||'</font></b>', " +
			"                      			'NEW', '<font color=\"black\">'||state_record_tsl||'</font>', " +
			"                      			'EXECUTED', '<font color=\"green\">'||state_record_tsl||'</font>', " +
			"                      			'EXECUTED_PARTIALLY', '<font color=\"darkgreen\">'||state_record_tsl||'</font>', " +
			"                      			'ERROR', '<b><font color=\"red\">'||state_record_tsl||'</font></b>', " +
			"                      			'CARRIED_OUT_PARTIALLY', '<b><font color=\"darkgray\">'||state_record_tsl||'</font></b>', " +
			"                      			'CARRIED_OUT', '<font color=\"gray\">'||state_record_tsl||'</font>', " +
			"                      			'CANCELED', '<font color=\"blue\">'||state_record_tsl||'</font>', " +
			"								/* Дополнительные состояния SMS-сообщений  */" +
			"                               'IN_PROCESS', '<font color=\"gray\">'||state_record_tsl||'</font>', " +
			"                               'DELIVERED', '<font color=\"darkgreen\">'||state_record_tsl||'</font>', " +
			"                               'REPORT_NOT_RECIEVE', '<b><font color=\"red\">'||state_record_tsl||'</font></b>', " +
			"                               'RECEIVED', '<font color=\"blue\">'||state_record_tsl||'</font>', " +
			"                               'WAIT_FOR_CONFIRM', '<font color=\"darkyellow\">'||state_record_tsl||'</font>', " +
			"                      			state_record_tsl) state_record_tsl, " +
			"  				       	DECODE(is_archive, " +
			"                             	'Y', '<b><font color=\"red\">'||is_archive_tsl||'</font></b>', " +
			"                             	is_archive_tsl) is_archive_tsl, " +
  		  	"                       type_message type_message2," + 
    		"						card_serial_number, card_id_issuer, card_id_payment_system, id_nat_prs " +
    		"                  FROM " + getGeneralDBScheme() + ".vc_ds_cl_pattern_mess_club_all " +
    		"                 WHERE id_cl_pattern = ? ";
		pParam.add(new bcFeautureParam("int", this.idPattern));
		
    	if (!isEmpty(pTypeMessage)) {
    		mySQL = mySQL + " AND type_message = ? ";
    		pParam.add(new bcFeautureParam("string", pTypeMessage));
    	}
        if (!isEmpty(pIsArchive)) {
	    	mySQL = mySQL + " AND is_archive = ? ";
	    	pParam.add(new bcFeautureParam("string", pIsArchive));
	    }
        if (!isEmpty(pFindString)) {
    		mySQL = mySQL + 
    			" AND (UPPER(TO_CHAR(id_message)) LIKE UPPER('%'||?||'%') OR " +
    			"	     UPPER(full_name_nat_prs) LIKE UPPER('%'||?||'%') OR " +
    			"	     UPPER(recepient) LIKE UPPER('%'||?||'%') OR " +
    			"	     UPPER(text_message) LIKE UPPER('%'||?||'%')) ";
    		for (int i=0; i<4; i++) {
    		    pParam.add(new bcFeautureParam("string", pFindString));
    		}
    	}
        pParam.add(new bcFeautureParam("int", p_end));
        pParam.add(new bcFeautureParam("int", p_beg));
        mySQL = mySQL + 
    		"                 ORDER BY begin_action_date desc, id_message) "+
    		"         WHERE ROWNUM < ? " + 
    		" ) WHERE rn >= ?";
   		
        StringBuilder html = new StringBuilder();
        Connection con = null;
        PreparedStatement st = null;
        boolean hasClientPermission = false;
        boolean hasEditPermission = false;
        try{
        	if (isEditMenuPermited("CLIENTS_NATPERSONS")>=0) {
        		hasClientPermission = true;
        	}
       	 	if (isEditPermited("DISPATCH_PATTERNS_CLIENT_MESSAGES")>0) {
       	 		hasEditPermission = true;
        	}
        	
        	LOGGER.debug(prepareSQLToLog(mySQL, pParam));
        	con = Connector.getConnection(getSessionId());;
            st = con.prepareStatement(mySQL);
            ResultSet rset = st.executeQuery();
            ResultSetMetaData mtd = rset.getMetaData();
            int colCount = mtd.getColumnCount();
            
            if (hasEditPermission) {
            	html.append("<form method=\"Post\" name=\"updateForm\" id=\"updateForm\" action=\"../crm/dispatch/patterns/client_patternupdate.jsp\" onSubmit=\"return CheckSelect(this);\">\n");
            	html.append("<input type=\"hidden\" name=\"type\" value=\"message\">\n");
           	 	html.append("<input type=\"hidden\" name=\"action\" value=\"execute\">\n");
           	 	html.append("<input type=\"hidden\" name=\"process\" value=\"yes\">\n");
           	 	html.append("<input type=\"hidden\" name=\"id\" value=\"" + this.idPattern + "\">\n");
            }
            
            html.append(getBottomFrameTable());
            html.append("<tr>");
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
	        	 html.append(getSubmitButtonAjax3("../crm/dispatch/patterns/client_patternupdate.jsp", "submit", "CheckSelect(document.getElementById('updateForm'))") + "<br>");
	           	 html.append("<input name=\"mainCheck\" id=\"mainCheck\" type=\"checkbox\" onclick=\"CheckAll(this,'chb')\"></th>\n");
           }
            for (int i=1; i <= colCount-5; i++) {
            	html.append(getBottomFrameTableTH(messageXML, mtd.getColumnName(i)));
            }
            html.append("</tr></thead><tbody>\n");
            while (rset.next())
            {
                html.append("<tr>\n");
                if (hasEditPermission) {
              		 html.append("<td align=\"center\"><input type=\"checkbox\" name=\"chb"+rset.getString("ID_MESSAGE")+"_"+rset.getString("TYPE_MESSAGE2")+"\" value=\"\" id=\"chb"+rset.getString("ID_MESSAGE")+"_"+rset.getString("TYPE_MESSAGE2")+"\" onclick=\"return CheckCB(this);\"></td>\n");
              	}
                for (int i=1; i<=colCount-5; i++) {
                	if ("ID_MESSAGE".equalsIgnoreCase(mtd.getColumnName(i))) {
                		if ("OFFICE".equalsIgnoreCase(rset.getString("TYPE_MESSAGE2"))) {
                			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/dispatch/messages/office_messagesspecs.jsp?id="+rset.getString(i), "", ""));
                		} else if ("EMAIL".equalsIgnoreCase(rset.getString("TYPE_MESSAGE2"))) {
                			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/dispatch/messages/email_messagesspecs.jsp?id="+rset.getString(i), "", ""));
                		} else if ("SMS".equalsIgnoreCase(rset.getString("TYPE_MESSAGE2"))) {
                			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/dispatch/messages/smsspecs.jsp?id="+rset.getString(i), "", ""));
                		}
           			} else if ("RECEPIENT".equalsIgnoreCase(mtd.getColumnName(i))) {
           				if (!"CARDS".equalsIgnoreCase(rset.getString("TYPE_MESSAGE2")) && 
           						!(rset.getString("ID_NAT_PRS") == null || "".equalsIgnoreCase(rset.getString("ID_NAT_PRS"))) && 
           						hasClientPermission) {
           					html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/clients/natpersonspecs.jsp?id="+rset.getString("ID_NAT_PRS"), "", ""));
           				} else {
           					html.append(getBottomFrameTableTDBase("", mtd.getColumnName(i), mtd.getColumnType(i), rset.getString(i), "", "", ""));
           				}
            		} else if ("TEXT_MESSAGE".equalsIgnoreCase(mtd.getColumnName(i))) {
            			html.append(getBottomFrameTableTD(mtd.getColumnName(i), getHTMLValue(rset.getString(i)), "", "", ""));
           			} else {
           				html.append(getBottomFrameTableTDBase("", mtd.getColumnName(i), mtd.getColumnType(i), rset.getString(i), "", "", ""));
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
    } // getMessagesAppliedHTML
	
    public String getSMSRegionOblastList(String pCodeCountry, boolean pEdit, String pLanguage){
    	
    	StringBuilder html =new StringBuilder();
    	Connection con = null;
    	boolean isError = false;
        
    	ArrayList <String> regionId = new ArrayList<String>();
    	ArrayList <String> regionName = new ArrayList<String>();
    	
    	PreparedStatement stRegion = null;
    	
    	boolean hasEditPermission = false;
    	
    	try{
    		if (isEditPermited("DISPATCH_PATTERNS_CLIENT_SMS")>0) {
      		  	hasEditPermission = true;
      	  	}
	    	
        	String regionSQL = 
        		" SELECT id_sms_region, name_sms_region " +
        		"   FROM " + getGeneralDBScheme()+".vc_ds_sms_region_all " +
        		"  WHERE code_country = ? " + 
        		"    AND UPPER(sms_language) = UPPER(?) " +
        		"  ORDER BY name_sms_region";
        	
            con = Connector.getConnection(getSessionId());
            LOGGER.debug("(regions)" + regionSQL + 
	    			", 1={'" + pCodeCountry + "',string}" + 
	    			", 2={'" + pLanguage + "',string}");
            stRegion = con.prepareStatement(regionSQL);
            stRegion.setString(1, pCodeCountry);
            stRegion.setString(2, pLanguage);
            ResultSet rset = stRegion.executeQuery();

			while(rset.next()){
				regionId.add(rset.getString("ID_SMS_REGION"));
				regionName.add(rset.getString("NAME_SMS_REGION"));
			}
		}
		catch (SQLException e) {
			LOGGER.error(html, e); html.append(showCRMException(e));
			isError = true;
		}
        catch (Exception el) {
        	LOGGER.error(html, el); html.append(showCRMException(el));
			isError = true;
        }
        finally {
            try {
                if (stRegion!=null) stRegion.close();
            } catch (SQLException w) {w.toString();}
            try {
                if (con!=null) con.close();
            } catch (SQLException w) {w.toString();}
            Connector.closeConnection(con);
        } // finally
        
        if (isError) {
        	return html.toString();
        }
    	
        ArrayList <String> regionOblastId = new ArrayList<String>();
        ArrayList <String> oblastId = new ArrayList<String>();
    	ArrayList <String> oblastName = new ArrayList<String>();
    	ArrayList <String> oblastExist = new ArrayList<String>();
    	
    	PreparedStatement stOblast = null;
    	try{
        	String oblastSQL = 
        		" SELECT o.id_sms_region, o.id_oblast, o.name_oblast, NVL(r.exist_flag, 'N') exist_flag " +
        		"   FROM (SELECT a.id_sms_region, a.id_oblast, a.exist_flag " +
        		"           FROM " + getGeneralDBScheme()+".vc_ds_cl_pattern_sms_obl_all a " +
        		"          WHERE id_cl_pattern = ? " + 
        		"            AND UPPER(sms_language) = UPPER(?)) r " +
        		"  RIGHT JOIN (SELECT id_sms_region, id_oblast, name_oblast " +
        		"                FROM " + getGeneralDBScheme()+".vc_ds_sms_region_oblast_all" +
        		"               WHERE code_country = ?) o " +
        		"    ON (r.id_sms_region = o.id_sms_region AND r.id_oblast = o.id_oblast) " +
        		"  ORDER BY o.name_oblast";
        	
            con = Connector.getConnection(getSessionId());
            LOGGER.debug("(oblast) " + oblastSQL + 
            		", 1={" + this.idPattern + ",int}" + 
	    			", 2={'" + pLanguage + "',string}" + 
	    			", 3={'" + pCodeCountry + "',string}");
            stOblast = con.prepareStatement(oblastSQL);
            stOblast.setInt(1, Integer.parseInt(this.idPattern));
            stOblast.setString(2, pLanguage);
            stOblast.setString(3, pCodeCountry);
            ResultSet rset = stOblast.executeQuery();

			while(rset.next()){
				regionOblastId.add(rset.getString("ID_SMS_REGION"));
				oblastId.add(rset.getString("ID_OBLAST"));
				oblastName.add(rset.getString("NAME_OBLAST"));
				oblastExist.add(rset.getString("EXIST_FLAG"));
			}
		}
		catch (SQLException e) {
			LOGGER.error(html, e); html.append(showCRMException(e));
			isError = true;
		}
        catch (Exception el) {
        	LOGGER.error(html, el); html.append(showCRMException(el));
			isError = true;
        }
        finally {
            try {
                if (stOblast!=null) stOblast.close();
            } catch (SQLException w) {w.toString();}
            try {
                if (con!=null) con.close();
            } catch (SQLException w) {w.toString();}
            Connector.closeConnection(con);
        } // finally
        
        if (isError) {
        	return html.toString();
        }
        String td1 = "<td class=\"top_line\">";
        String td2 = "<td class=\"top_line\">";
        String td3 = "<td class=\"top_line\">";
        String td4 = "<td class=\"top_line\">";
        String prom = "";
        if (regionId.size()>0) {
        	for(int counter=0;counter<regionId.size();counter++){
        		if (counter == 0) {
        			//html.append("<b>"+regionName.get(counter)+"</b>: ");
        		} else {
        			//html.append("<br><b>"+regionName.get(counter)+"</b>: ");
        		}
        		td1 = td1 + "<b><span class=\"checkAllLink\" title=\"" + buttonXML.getfieldTransl("button_select_all", false) + "\" onclick=\"CheckAllOblast('" + pCodeCountry + "_" + regionId.get(counter) + "_" + pLanguage + "')\">"+regionName.get(counter)+"</span></b><br>";
	        		// + "<img vspace=\"0\" hspace=\"0\"" +
					//" src=\"" + getContextPath() + "/images/oper/rows/check_all.png\" align=\"top\" style=\"border: 0px;\"" + 
					//" title=\"" + buttonXML.getfieldTransl("button_select_all", false) + "\" "+
					//" onclick=\"CheckAllOblast('" + pCodeCountry + "_" + regionId.get(counter) + "_" + pLanguage + "')\"><br>";
        		
        			// +"<input type=\"button\" class=\"inputfield\" value=\""+buttonXML.getfieldTransl("button_select_all", false)+"\" onclick=\"CheckAllOblast('" + pCodeCountry + "_" + regionId.get(counter) + "_" + pLanguage + "')\"><br>";
        		td2 = td2 + "&nbsp;<br>";
        		td3 = td3 + "&nbsp;<br>";
        		td4 = td4 + "&nbsp;<br>";
        		if (regionOblastId.size()>0) {
    				int foundOblast = 0;
    		        int rowCount = 0;
        			for(int counter2=0;counter2<regionOblastId.size();counter2++){
        				if (regionOblastId.get(counter2).equalsIgnoreCase(regionId.get(counter))) {
        					prom = "";
                    		foundOblast = foundOblast + 1;
                    		if (hasEditPermission) {
                             	rowCount++;
                                String oblID="id_"+oblastId.get(counter2);
                                String tprvCheck="prv_"+oblID;
                                String tCheck="chb_"+oblID;
                                String tCheckId=pCodeCountry + "_" + regionId.get(counter) + "_" + pLanguage + "_chb_" + oblID;
                                
                                if("Y".equalsIgnoreCase(oblastExist.get(counter2)) && pEdit) {
                                	prom = prom + "<INPUT type=hidden  value=\"Y\" name="+tprvCheck+">"; 
	                              	//html.append("<INPUT type=hidden  value=\"Y\" name="+tprvCheck+">");
	                            }
                                prom = prom + "<input type=\"checkbox\" name=\"" + tCheck + "\"id=\"" + tCheckId + "\"";
	                    		//html.append("<input type=\"checkbox\" name=\"" + tCheck + "\"");
	                    		if("Y".equalsIgnoreCase(oblastExist.get(counter2))) {
	                    			prom = prom + " CHECKED ";
	            	        		//html.append(" CHECKED ");
	            	        	}
	                    		if (pEdit) {
	                    			prom = prom + " class=\"inputfield-ro\"";
	                    		} else {
	                    			prom = prom + " disabled class=\"inputfield-ro\"";
	                    		}
	                    		prom = prom + "><label class=\"checbox_label\" for=\"" + tCheck + "\">" +oblastName.get(counter2) + "</label><br>";
	                    		//html.append(">" +oblastName.get(counter2) + "\n");
                    		} else {
                    			prom = prom + oblastName.get(counter2) + "<br>";
                    			//html.append("&nbsp;" + oblastName.get(counter2) + "\n");
                    		}
                    		if (foundOblast==1) {
                    			td1 = td1 + prom;
                    		} else if (foundOblast==2) {
                    			td2 = td2 + prom;
                    		} else if (foundOblast==3) {
                    			td3 = td3 + prom;
                    		} else if (foundOblast==4) {
                    			td4 = td4 + prom;
                    		}
                    		if (foundOblast >= 4) {
                    			foundOblast = 0;
                    		} 
                    	}
        			}
        			if (foundOblast!=0) {
	    				if (foundOblast<=2) {
	            			td2 = td2 + "&nbsp;<br>";
	            		}
	    				if (foundOblast<=3) {
	            			td3 = td3 + "&nbsp;<br>";
	            		}
	    				if (foundOblast<=4) {
	            			td4 = td4 + "&nbsp;<br>";
	            		}
        			}
        		}
        	}
        }
        html.append(td1 + "</td>" + td2 + "</td>" + td3 + "</td>" + td4 + "</td>");
		return html.toString();
	} 

    public String getAttachedFilesHTML(String pPatternType, boolean hasEditPermission) {
        StringBuilder html = new StringBuilder();
        Connection con = null;
        PreparedStatement st = null;
        String mySQL = 
        	" SELECT id_pattern_file, file_name, stored_file_name " +
            "   FROM " + getGeneralDBScheme() + ".vc_ds_cl_pattern_files_all" +
            "  WHERE id_cl_pattern = ? " + 
            "    AND type_cl_pattern = ? " + 
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
       	  			html.append("&nbsp;<a href=\"#\" onclick=\"if (window.confirm('" + documentXML.getfieldTransl("l_remove_file", false) + " \\\'" + rset.getString("file_name") + "\\\'?')) {ajaxpage('../crm/dispatch/patterns/client_patternupdate.jsp?id=" + this.idPattern + "&id_file=" + rset.getString("ID_PATTERN_FILE") +  "&type=" + pType + "&process=yes&action=remove_attached_file&id_file=" + rset.getString("id_pattern_file") + "', 'div_main')}\" " +
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
