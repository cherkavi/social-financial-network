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

public class bcDsMessagePatternObject extends bcObject {
	private final static Logger LOGGER=Logger.getLogger(bcDsMessagePatternObject.class);
	private Map<String,Comparable<String>> fieldHm = new HashMap<String, Comparable<String>>();
	
	private String idPattern;
	
	public bcDsMessagePatternObject(String pIdPattern) {
		this.idPattern = pIdPattern;
		getFeature();
	}
	
	private void getFeature() {
		String mySQL = " SELECT * FROM " + getGeneralDBScheme() + ".VC_DS_PATTERN_CLUB_ALL WHERE id_ds_pattern = ?";
        fieldHm = getFeatures2(mySQL, this.idPattern, false);
	}
	
	public String getValue(String pColumnName) {
		return getFeautureResult(fieldHm, pColumnName);
	}

	public String getMessagesAppliedHTML(String pOperationType, String pFind, String pMessageType, String pDispatchKind, String pMessageOperType, String pStateRecord, String pIsArchive, String p_beg, String p_end) {

		ArrayList<bcFeautureParam> pParam = initParamArray();

		String mySQL = 
    		"SELECT id_ds_message id_message, name_ds_message_type, name_ds_message_oper_type, name_dispatch_kind, " +
        	"		email, title_ds_message title_message, " +
        	"		sendings_quantity, error_sendings_quantity, event_date_frmt, name_ds_message_state, " +
        	"		is_archive_tsl, cd_ds_message_oper_type, id_nat_prs, id_contact_prs, id_tr_person, " +
        	"		id_user, cd_ds_message_type " +
  		  	"  FROM (SELECT ROWNUM rn, id_ds_message, " +
  		  	"				DECODE(cd_ds_message_type," +
            "                      'SMS', '<b><font color=\"green\">'||name_ds_message_type||'</font></b>'," +
            "                      'EMAIL', '<b><font color=\"darkgray\">'||name_ds_message_type||'</font></b>'," +
            "                      'OFFICE', '<b><font color=\"brown\">'||name_ds_message_type||'</font></b>'," +
            "                      name_ds_message_type" +
            "               ) name_ds_message_type," +
            "				DECODE(cd_ds_message_oper_type, " +
  		  	"                      'SEND', '<b><font color=\"green\">'||name_ds_message_oper_type||'</font></b>', " +
			"                      'RECEIVE', '<font color=\"blue\">'||name_ds_message_oper_type||'</font>', " +
			"                      name_ds_message_oper_type" +
			"               ) name_ds_message_oper_type, " +
  		  	"				DECODE(cd_ds_message_oper_type, " +
  		  	"                      'RECEIVE',DECODE(cd_dispatch_kind, " +
  		  	"                      				 'CLIENT', '<b><font color=\"green\">'||name_dispatch_kind||'</font></b>', " +
			"                      				 'PARTNER', '<b><font color=\"blue\">'||name_dispatch_kind||'</font></b>', " +
			"                               	 'SYSTEM', '<font color=\"red\"><b>'||name_dispatch_kind||'</b></font>', " +
			"                               	 'TRAINING', '<font color=\"darkgray\">'||name_dispatch_kind||'</font>', " +
			"                               	 'LG_PROMOTER', '<font color=\"black\">'||name_dispatch_kind||'</font>', " +
			"                               	 'UNKNOWN', '', " +
			"                      				 name_dispatch_kind" +
			"							  )," +
			"					   ''" +
			"               ) name_dispatch_kind, " +
			"               DECODE(cd_ds_message_type, " +
			"                      'SMS', recepient, " + 
  		  	"					   DECODE(cd_ds_message_oper_type, " +
  		  	"                      		  'RECEIVE',email," +
  		  	"					   		  '<font color=\"red\"><b>'||TO_CHAR(NVL(receivers_count,0))||' '||" +
  		  	"								CASE WHEN NVL(receivers_count,0) = 0 THEN '" + messageXML.getfieldTransl("h_less_5_receivers", false) + "' " +
  		  	"							     	 WHEN NVL(receivers_count,0) = 1 THEN '" + messageXML.getfieldTransl("h_one_receiver", false) + "' " +
		  	"							     	 WHEN NVL(receivers_count,0) BETWEEN 2 AND 4 THEN '" + messageXML.getfieldTransl("h_2_4_receivers", false) + "' " +
  		  	"							     	 WHEN NVL(receivers_count,0) >= 5 THEN '" + messageXML.getfieldTransl("h_less_5_receivers", false) + "' " +
		  	"                           	END||'</b></font>'" +
  		  	"					   ) " +
  		  	"				) email," +
  		  	"				title_ds_message, " +
  		  	"				DECODE(cd_ds_message_oper_type, " +
  		  	"                      'SEND',TO_CHAR(NVL(sendings_quantity,0))," +
  		  	"					   ''" +
  		  	"				) sendings_quantity, " +
  		    "               DECODE(cd_ds_message_oper_type, " +
  		  	"                      'SEND',TO_CHAR(NVL(error_sendings_quantity,0))," +
  		  	"					   ''" +
  		  	"				) error_sendings_quantity, " +
  		    "				event_date_frmt,  " +
  		  	"               DECODE(cd_ds_message_state, " +
  		  	"                      'PREPARED', '<b><font color=\"black\">'||name_ds_message_state||'</font></b>', " +
			"                      'NEW', '<font color=\"black\">'||name_ds_message_state||'</font>', " +
			"                      'EXECUTED', '<font color=\"green\">'||name_ds_message_state||'</font>', " +
			"                      'EXECUTED_PARTIALLY', '<font color=\"darkgreen\">'||name_ds_message_state||'</font>', " +
			"                      'ERROR', '<b><font color=\"red\">'||name_ds_message_state||'</font></b>', " +
			"                      'CARRIED_OUT_PARTIALLY', '<b><font color=\"darkgray\">'||name_ds_message_state||'</font></b>', " +
			"                      'CARRIED_OUT', '<font color=\"gray\">'||name_ds_message_state||'</font>', " +
			"                      'CANCELED', '<font color=\"blue\">'||name_ds_message_state||'</font>', " +
			"                      name_ds_message_state" +
			"               ) name_ds_message_state, " +
			"  				DECODE(is_archive, " +
			"                      'Y', '<b><font color=\"red\">'||is_archive_tsl||'</font></b>', " + 
			"                      'N', '<b><font color=\"blue\">'||is_archive_tsl||'</font></b>', " +
			"                      is_archive_tsl" +
			"               ) is_archive_tsl," +
			"				cd_ds_message_oper_type, id_nat_prs_sender id_nat_prs, id_contact_prs_sender id_contact_prs, " +
			"				id_tr_person_sender id_tr_person, id_user_sender id_user, " +
			"				cd_ds_message_type " +
  		  	"        FROM (SELECT * " +
  		  	"                FROM " + getGeneralDBScheme()+".vc_ds_pattern_mess_club_all " +
    		"                 WHERE id_ds_pattern = ? ";
		pParam.add(new bcFeautureParam("int", this.idPattern));
		
    	if (!isEmpty(pFind)) {
    		mySQL = mySQL + 
    			" AND (UPPER(TO_CHAR(id_ds_message)) LIKE UPPER('%'||?||'%') OR " +
    			"	     UPPER(email) LIKE UPPER('%'||?||'%') OR " +
    			"	     UPPER(title_ds_message) LIKE UPPER('%'||?||'%') OR " +
    			"	     UPPER(event_date_frmt) LIKE UPPER('%'||?||'%')) ";
    		for (int i=0; i<4; i++) {
    		    pParam.add(new bcFeautureParam("string", pFind));
    		}
    	}
        if (!isEmpty(pMessageType)) {
	    	mySQL = mySQL + " AND cd_ds_message_type = ? ";
	    	pParam.add(new bcFeautureParam("string", pMessageType));
	    }
        if (!isEmpty(pDispatchKind)) {
	    	mySQL = mySQL + " AND cd_ds_sender_dispatch_kind = ? ";
	    	pParam.add(new bcFeautureParam("string", pDispatchKind));
	    }
        if (!isEmpty(pMessageOperType)) {
	    	mySQL = mySQL + " AND cd_ds_message_oper_type = ? ";
	    	pParam.add(new bcFeautureParam("string", pMessageOperType));
	    }
        if (!isEmpty(pIsArchive)) {
	    	mySQL = mySQL + " AND is_archive = ? ";
	    	pParam.add(new bcFeautureParam("string", pIsArchive));
	    }
	    if (!isEmpty(pStateRecord)) {
    		mySQL = mySQL + " AND cd_ds_message_state = ? ";
    		pParam.add(new bcFeautureParam("string", pStateRecord));
        }
	    pParam.add(new bcFeautureParam("int", p_end));
	    pParam.add(new bcFeautureParam("int", p_beg));
    	mySQL = mySQL + 
    		"                 ORDER BY begin_action_date desc, id_ds_message) "+
    		"         WHERE ROWNUM < ? " + 
    		" ) WHERE rn >= ?";
   		
        StringBuilder html = new StringBuilder();
        Connection con = null;
        PreparedStatement st = null;
        boolean hasEmailMessagePermission = false;
        boolean hasSMSPermission = false;
        boolean hasOfficeMessagePermission = false;
	    boolean hasNatPrsPermission = false;
	    boolean hasContactPrsPermission = false;
	    boolean hasTrainingPersonPermission = false;
	    boolean hasSystemUserPermission = false;
	    
        boolean hasEditPermission = false;
        try{
        	if (isEditMenuPermited("DISPATCH_MESSAGES_EMAIL")>=0) {
        		hasEmailMessagePermission = true;
        	}
        	if (isEditMenuPermited("DISPATCH_MESSAGES_SMS")>=0) {
        		hasSMSPermission = true;
        	}
        	if (isEditMenuPermited("DISPATCH_MESSAGES_OFFICE")>=0) {
        		hasOfficeMessagePermission = true;
        	}
	    	if (isEditMenuPermited("CLIENTS_NATPERSONS")>=0) {
	    		hasNatPrsPermission = true;
        	}
	    	if (isEditMenuPermited("CLIENTS_CONTACT_PRS")>=0) {
	    		hasContactPrsPermission = true;
        	}
	    	if (isEditMenuPermited("TRAINING_PERCONS")>=0) {
	    		hasTrainingPersonPermission = true;
        	}
	    	if (isEditMenuPermited("SECURITY_USERS")>=0) {
	    		hasSystemUserPermission = true;
        	}
	    	
       	 	if (isEditPermited("DISPATCH_PATTERNS_CLIENT_MESSAGES")>0) {
       	 		hasEditPermission = true;
        	}
        	
        	LOGGER.debug(prepareSQLToLog(mySQL, pParam));
        	con = Connector.getConnection(getSessionId());;
        	st = con.prepareStatement(mySQL);
        	st = prepareParam(st, pParam);
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
            for (int i=1; i <= colCount-7; i++) {
            	html.append(getBottomFrameTableTH(messageXML, mtd.getColumnName(i)));
            }
            html.append("</tr></thead><tbody>\n");
            while (rset.next())
            {
                html.append("<tr>\n");
                if (hasEditPermission) {
              		 html.append("<td align=\"center\"><input type=\"checkbox\" name=\"chb"+rset.getString("ID_MESSAGE")+"_"+rset.getString("TYPE_MESSAGE2")+"\" value=\"\" id=\"chb"+rset.getString("ID_MESSAGE")+"_"+rset.getString("TYPE_MESSAGE2")+"\" onclick=\"return CheckCB(this);\"></td>\n");
              	}
                for (int i=1; i<=colCount-7; i++) {
                	if ("ID_MESSAGE".equalsIgnoreCase(mtd.getColumnName(i)) ||
                			"TITLE_MESSAGE".equalsIgnoreCase(mtd.getColumnName(i))) {
                		if ("OFFICE".equalsIgnoreCase(rset.getString("CD_DS_MESSAGE_TYPE")) && hasOfficeMessagePermission) {
                			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/dispatch/messages/office_messagesspecs.jsp?id="+rset.getString("ID_MESSAGE"), "", ""));
                		} else if ("EMAIL".equalsIgnoreCase(rset.getString("CD_DS_MESSAGE_TYPE")) && hasEmailMessagePermission) {
                			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/dispatch/messages/email_messagesspecs.jsp?id="+rset.getString("ID_MESSAGE"), "", ""));
                		} else if ("SMS".equalsIgnoreCase(rset.getString("CD_DS_MESSAGE_TYPE")) && hasSMSPermission) {
                			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/dispatch/messages/smsspecs.jsp?id="+rset.getString("ID_MESSAGE"), "", ""));
                		}
           			} else if ("EMAIL".equalsIgnoreCase(mtd.getColumnName(i))) {
           				if (hasNatPrsPermission &&
     	        			!isEmpty(rset.getString("ID_NAT_PRS"))) {
       	        			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/clients/natpersonspecs.jsp?id="+rset.getString("ID_NAT_PRS"), "", ""));
       	        		} else if (hasContactPrsPermission &&
       	        			!isEmpty(rset.getString("ID_CONTACT_PRS"))) {
       	        			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/clients/contact_prsspecs.jsp?id="+rset.getString("ID_CONTACT_PRS"), "", ""));
       	        		} else if (hasTrainingPersonPermission &&
       	        			!isEmpty(rset.getString("ID_TR_PERSON"))) {
       	        			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/training/participant_personspecs.jsp?id="+rset.getString("ID_TR_PERSON"), "", ""));
       	        		} else if (hasSystemUserPermission &&
       	        			!isEmpty(rset.getString("ID_USER"))) {
       	        			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/security/userspecs.jsp?id="+rset.getString("ID_USER"), "", ""));
       	        		} else {
       	        			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", "", ""));
       	        		}
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
    		if (isEditPermited("DISPATCH_MESSAGES_PATTERN_SMS")>0) {
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
            ResultSet rset = stRegion.executeQuery(regionSQL);

			while(rset.next()){
				regionId.add(rset.getString("ID_SMS_REGION"));
				regionName.add(rset.getString("NAME_SMS_REGION"));
			}
		}
		catch (SQLException e) {
			LOGGER.error(html, e); 
			html.append(showCRMException(e));
			isError = true;
		}
        catch (Exception el) {
        	LOGGER.error(html, el); 
        	html.append(showCRMException(el));
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
        		"           FROM " + getGeneralDBScheme()+".vc_ds_pattern_sms_oblast_all a " +
        		"          WHERE id_ds_pattern = ? " + 
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
            ResultSet rset = stOblast.executeQuery(oblastSQL);

			while(rset.next()){
				regionOblastId.add(rset.getString("ID_SMS_REGION"));
				oblastId.add(rset.getString("ID_OBLAST"));
				oblastName.add(rset.getString("NAME_OBLAST"));
				oblastExist.add(rset.getString("EXIST_FLAG"));
			}
		}
		catch (SQLException e) {
			LOGGER.error(html, e); 
			html.append(showCRMException(e));
			isError = true;
		}
        catch (Exception el) {
        	LOGGER.error(html, el); 
        	html.append(showCRMException(el));
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
        	" SELECT id_ds_pattern_file, file_name, stored_file_name " +
            "   FROM " + getGeneralDBScheme() + ".vc_ds_pattern_files_all" +
            "  WHERE id_ds_pattern = ? " +
            "    AND type_ds_pattern_file = ? " + 
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
       	  			html.append("&nbsp;<a href=\"#\" onclick=\"if (window.confirm('" + documentXML.getfieldTransl("l_remove_file", false) + " \\\'" + rset.getString("file_name") + "\\\'?')) {ajaxpage('../crm/dispatch/messages/patternupdate.jsp?id=" + this.idPattern + "&id_file=" + rset.getString("ID_DS_PATTERN_FILE") +  "&type=" + pType + "&process=yes&action=remove_attached_file&id_file=" + rset.getString("ID_DS_PATTERN_FILE") + "', 'div_main')}\" " +
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
