package bc.ajax;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import bc.connection.Connector;
import bc.objects.bcDictionary;
import bc.objects.bcObject;
import bc.objects.bcXML;
import bc.service.bcFeautureParam;

// TODO !!! REFACTORING !!!
public class responseUtility extends bcObject { 

	private final static Logger LOGGER=Logger.getLogger(responseUtility.class);
	
	private static Connection con = null;
	private bcXML commonXML 	= bcDictionary.getInstance("Common");

	public responseUtility(){
		LOGGER.debug("responseUtility constructor:");
	}
	
	/*
	private bcXML accountXML = accountsXML.getInstance();
	private bcXML bk_accountXML = bk_accountsXML.getInstance();
	private bcXML call_centerXML = call_centersXML.getInstance();
	private bcXML club_actionXML = club_actionsXML.getInstance();
	private bcXML natprsXML 	= natpersonsXML.getInstance();
	private bcXML terminalXML	= terminalsXML.getInstance();
	private bcXML userXML = usersXML.getInstance();
	private bcXML loyXML = loysXML.getInstance();
	private bcXML sheduleXML = shedulesXML.getInstance();
	private bcXML posting_settingXML = posting_settingsXML.getInstance();
	private bcXML posting_schemeXML = oper_schemesXML.getInstance();
	private bcXML samXML = samsXML.getInstance();
	private bcXML clubcardXML = clubcardsXML.getInstance();
	private bcXML jurpersonServPlaceXML = jurpersonsserviceplacesXML.getInstance();
	private bcXML relationshipXML = relationshipsXML.getInstance();
	private bcXML messageXML = messagesXML.getInstance();
    public bcXML questionnaireXML = questionnairesXML.getInstance();
	*/
	
	/*
	public static synchronized Connection getConnection() throws NamingException, SQLException, Exception{
		try{
			con = Connector.getConnection();
	    }catch (Exception e){
	    	throw new Exception("Error from GatewayDB.getConnection = "+e,e);
	    }
	    return con;
	}//getConnection
	   
	public void closeConnection(){
		if(con!=null){
			try {
				con.close();
				Connector.closeConnection(con);
				con = null;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	*/
    
    private String getNoDataFountTR(String pRowSpan) {
    	return "<tr><td colspan=\"" + pRowSpan + "\" style=\"color: red\">" + 
    		commonXML.getfieldTransl("NOT_FOUND", false) + 
    		"</td></tr>";
    }
    
    public String[] myCallFunction1(String sql, String pSessionId)
    {
    	String[] results = new String[2];
        CallableStatement cs = null;
        try
        {
        	LOGGER.debug("responceUtitity.myCallFunction1(): " + sql);
        	con = Connector.getConnection(pSessionId);

        	cs = con.prepareCall(sql);
        	cs.registerOutParameter(1, Types.VARCHAR);
        	cs.registerOutParameter(2, Types.VARCHAR);
        	cs.execute();
        	results[0] = cs.getString(1);
        	results[1] = cs.getString(2);
        	cs.close();
        	
        } catch (SQLException e) {
        	results[0] = "1"; 
        	results[1] = e.toString(); 
        	LOGGER.error("SQLException: " + e.toString());
        } catch (Exception el) {
        	results[0] = "1"; 
        	results[1] = el.toString(); 
        	LOGGER.error("Exception: " + el.toString());}
        finally {
            try {
                if (cs!=null) {
					cs.close();
				}
            } catch (SQLException w) {w.toString();}
            try {
                if (con!=null) {
					con.close();
				}
            } catch (SQLException w) {w.toString();}
            Connector.closeConnection(con);
        } // finally

        return results;
    }
    
    public String[] myCallFunctionParam3(String sql, String[] pParam, String pSessionId)
    {
    	String[] results = new String[3];
        CallableStatement cs = null;
        int pParamLength = 0;
        try
        {
        	LOGGER.debug(prepareSQLToLog(sql, pParam));
        	con = Connector.getConnection(pSessionId);

        	if (!(pParam==null)) {
        		pParamLength = pParam.length;
        	}
        	cs = con.prepareCall(sql);
        	cs.registerOutParameter(1, Types.VARCHAR);
        	
        	for(int counter=2;counter<pParamLength+2;counter++){
	        	cs.setString(counter, pParam[counter-2]);
	        }
	        cs.registerOutParameter(pParamLength + 2, Types.VARCHAR);
        	cs.registerOutParameter(pParamLength + 3, Types.VARCHAR);
        	cs.execute();
        	results[0] = cs.getString(1);
        	results[1] = cs.getString(pParamLength + 3);
        	results[2] = cs.getString(pParamLength + 2);
        	cs.close();
        } catch (SQLException e) {
        	results[0] = "1"; 
        	results[1] = e.toString(); 
        	LOGGER.error("SQLException: " + e.toString());
        } catch (Exception el) {
        	results[0] = "1"; 
        	results[1] = el.toString(); 
        	LOGGER.error("Exception: " + el.toString());}
        finally {
            try {
                if (cs!=null) {
					cs.close();
				}
            } catch (SQLException w) {w.toString();}
            try {
                if (con!=null) {
					con.close();
				}
            } catch (SQLException w) {w.toString();}
            Connector.closeConnection(con);
        } // finally

        return results;
    }

    public String getHTMLValue(String pValue){ // TEMPORARY FOR DEMO!!
    	String result;
    	if (pValue == null || "".equalsIgnoreCase(pValue) || "null".equalsIgnoreCase(pValue)) {
    		result = "";
    	} else {
    		result = pValue;    		
    	}
    	result = result.replace("&", "&amp;").replace("\"", "&quot;").replace("<<", "&laquo;").replace(">>", "&raquo;");
    	result = result.replace("<", "&lt;").replace(">", "&gt;").replace("'", "&quot;");
    	return result;
    } // end of getHTMLValue
	
	public transport get_responce(transport mySource) {
		String functionName = mySource.getFunctionName();
		transport myTransport = null;
		setSessionId(mySource.getSessionId());
		if ("GET_BANK_ACCOUNTS".equalsIgnoreCase(functionName)) {
			myTransport = getBankAccountsList(mySource);
		} else if ("GET_BK_ACCOUNTS".equalsIgnoreCase(functionName)) {
			myTransport = getBKAccountsList(mySource);
		} else if ("GET_BK_ACCOUNT_SCHEME".equalsIgnoreCase(functionName)) {
			myTransport = getBKAccountSchemeList(mySource);
		} else if ("GET_BK_BANK_ACCOUNT_TYPE_FOR_CLUB_REL".equalsIgnoreCase(functionName)) {
			myTransport = getBkBankAccountTypeForClubRelOptions(mySource);
		} else if ("GET_BK_OPERATION_SCHEME_LINES".equalsIgnoreCase(functionName)) {
			myTransport = getBKOperationSchemeLinesList(mySource);
		} else if ("CHECK_CALCULATOR_EXPRESSION".equalsIgnoreCase(functionName)) {
			myTransport = check_calculator_expression(mySource);
		} else if ("GET_CALL_CENTER_INQUIRER".equalsIgnoreCase(functionName)) {
			myTransport = getCallCenterInquirerList(mySource);
		} else if ("GET_CALL_CENTER_USERS".equalsIgnoreCase(functionName)) {
			myTransport = getCallCenterUsersList(mySource);
		} else if ("GET_CARD_CATEGORY".equalsIgnoreCase(functionName)) {
			myTransport = getClubCardCategoryForStatusOptions(mySource);
		} else if ("GET_CARD_BON_CATEGORY".equalsIgnoreCase(functionName)) {
			myTransport = getClubCardBonCategoryForStatusOptions(mySource);
		} else if ("GET_CARD_DISC_CATEGORY".equalsIgnoreCase(functionName)) {
			myTransport = getClubCardDiscCategoryForStatusOptions(mySource);
		} else if ("GET_CARD_ADN_NAT_PRS".equalsIgnoreCase(functionName)) {
			myTransport = getClubCardsAndNatPrsList(mySource);
		} else if ("GET_CLUB".equalsIgnoreCase(functionName)) {
			myTransport = getClubList(mySource);
		} else if ("GET_CLUB_EVENT".equalsIgnoreCase(functionName)) {
			myTransport = getClubAction(mySource);
		} else if ("GET_CLUB_EVENT_GIFTS".equalsIgnoreCase(functionName)) {
			myTransport = getClubActionGiftsList(mySource);
		} else if ("GET_CLUB_EVENT_GIFTS2".equalsIgnoreCase(functionName)) {
			myTransport = getClubActionGiftsList2(mySource);
		} else if ("GET_CARDS".equalsIgnoreCase(functionName)) {
			myTransport = getClubCardsList(mySource);
		} else if ("GET_CARD_PACK".equalsIgnoreCase(functionName)) {
			myTransport = getCardPackagesList(mySource);
		} else if ("GET_CLUB_RELATIONSHIPS".equalsIgnoreCase(functionName)) {
			myTransport = getClubRelationshipsList(mySource);
		} else if ("GET_CONTACT_PRS".equalsIgnoreCase(functionName)) {
			myTransport = getContactPrs(mySource);
		} else if ("GET_MESSAGE_SENDER_FOR_PHONE".equalsIgnoreCase(functionName)) {
			myTransport = getDSMessageSenderForPhoneMobileList(mySource);
		} else if ("GET_MESSAGE_SENDER_FOR_EMAIL".equalsIgnoreCase(functionName)) {
			myTransport = getDSMessageSenderForEmailList(mySource);
		} else if ("GET_DOC".equalsIgnoreCase(functionName)) {
			myTransport = getDocumentList(mySource);
		} else if ("GET_DS_CL_PATTERN".equalsIgnoreCase(functionName)) {
			myTransport = getDSClientPatternList(mySource);
		} else if ("GET_DS_EMAIL_PROFILE".equalsIgnoreCase(functionName)) {
			myTransport = getDSEmailProfileList(mySource);
		} else if ("GET_DS_CL_PROFILE".equalsIgnoreCase(functionName)) {
			myTransport = getDSClientProfileList(mySource);
		} else if ("GET_DS_PT_PATTERN".equalsIgnoreCase(functionName)) {
			myTransport = getDSPartnerPatternList(mySource);
		} else if ("GET_DS_PT_PROFILE".equalsIgnoreCase(functionName)) {
			myTransport = getDSPartnerProfileList(mySource);
		} else if ("GET_FAQ".equalsIgnoreCase(functionName)) {
			myTransport = getFAQList(mySource);
		} else if ("GET_GIFTS".equalsIgnoreCase(functionName)) {
			myTransport = getGiftsList(mySource);
		} else if ("GET_GIFTS_LOGISTIC".equalsIgnoreCase(functionName)) {
			myTransport = getGiftsLogisticList(mySource);
		} else if ("GET_JUR_PRS".equalsIgnoreCase(functionName)) {
			myTransport = getJurPrsList(mySource);
		} else if ("GET_JUR_AND_NAT_PRS".equalsIgnoreCase(functionName)) {
			myTransport = getJurAndNatPrsList(mySource);
		} else if ("GET_LOY_SCHEME".equalsIgnoreCase(functionName)) {
			myTransport = getLoyalitySchemesList(mySource);
		} else if ("GET_LG_PROMOTER".equalsIgnoreCase(functionName)) {
			myTransport = getLGPromoter(mySource);
		} else if ("GET_NAT_PRS".equalsIgnoreCase(functionName)) {
			myTransport = sendNatPrsFindResultHTML(mySource);
		} else if ("GET_NAT_PRS_ROLE".equalsIgnoreCase(functionName)) {
			myTransport = sendNatPrsRoleFindResultHTML(mySource);
		} else if ("GET_QUESTIONAIRE_PACK".equalsIgnoreCase(functionName)) {
			myTransport = getQuestionnairePackList(mySource);
		} else if ("GET_REFERRAL_SCHEME".equalsIgnoreCase(functionName)) {
			myTransport = getReferralSchemeList(mySource);
		} else if ("GET_SAM".equalsIgnoreCase(functionName)) {
			myTransport = getSAMList(mySource);
		} else if ("GET_SERVICE_PLACES".equalsIgnoreCase(functionName)) {
			myTransport = getServicePlaceList(mySource);
		} else if ("GET_SCHEDULE".equalsIgnoreCase(functionName)) {
			myTransport = getSchedulesList(mySource);
		} else if ("GET_TERMINALS".equalsIgnoreCase(functionName)) {
			myTransport = getTerminalsList(mySource);
		} else if ("GET_TERM_DEVICE_TYPE".equalsIgnoreCase(functionName)) {
			myTransport = getTermDeviceTypeOptions(mySource);
		} else if ("GET_TERM_LOY_HISTORY".equalsIgnoreCase(functionName)) {
			myTransport = getTerminalLoyalityHistoryList(mySource);
		} else if ("GET_USERS".equalsIgnoreCase(functionName)) {
			myTransport = getUsersList(mySource);
		}
		return myTransport;
	}
	
	public ArrayList<bcFeautureParam> initParamArray() {
    	ArrayList<bcFeautureParam> pParam = new ArrayList<bcFeautureParam>();
    	return pParam;
    }

	private transport getClubCardCategoryForStatusOptions(transport mySource) {
		ArrayList<bcFeautureParam> pParam = initParamArray();
		pParam.add(new bcFeautureParam("int", mySource.getFunctionParam()));
		
		String mySQL = 
			" SELECT DISTINCT id_category, name_category, id_category_name " +
			"   FROM " + getGeneralDBScheme() + ".vc_card_category_club_all " +
			"  WHERE id_card_status = ? " + 
			"    AND exist_flag = 'Y'" +
			"  ORDER BY DECODE(id_category_name, 99, 2, 1), name_category";
	   // LOGGER.debug(mySQL);
		return send_response(mySQL, pParam, mySource);
	}

	private transport getClubCardBonCategoryForStatusOptions(transport mySource) {
		ArrayList<bcFeautureParam> pParam = initParamArray();
		pParam.add(new bcFeautureParam("int", mySource.getFunctionParam()));
		
		String mySQL = 
			" SELECT id_category, name_category " +
			"   FROM (SELECT DISTINCT id_category, name_category, id_category_name " +
			"   		FROM " + getGeneralDBScheme() + ".vc_card_category_club_all " +
			"  		   WHERE id_card_status = ? " + 
			"    		 AND exist_flag = 'Y'" +
			"  		   ORDER BY DECODE(id_category_name, 99, 2, 1), name_category" +
			"		 )";
	   // LOGGER.debug(mySQL);
		return send_response(mySQL, pParam, mySource);
	}

	private transport getClubCardDiscCategoryForStatusOptions(transport mySource) {
		ArrayList<bcFeautureParam> pParam = initParamArray();
		pParam.add(new bcFeautureParam("int", mySource.getFunctionParam()));
		
		String mySQL = 
			" SELECT id_category, name_category " +
			"   FROM (SELECT DISTINCT id_category, name_category, id_category_name " +
			"   		FROM " + getGeneralDBScheme() + ".vc_card_category_club_all " +
			"  		   WHERE id_card_status = ? " + 
			"    		 AND exist_flag = 'Y'" +
			"  		   ORDER BY DECODE(id_category_name, 99, 2, 1), name_category" +
			"		 )";
	   // LOGGER.debug(mySQL);
		return send_response(mySQL, pParam, mySource);
	}

	private transport getBkBankAccountTypeForClubRelOptions(transport mySource) {
		ArrayList<bcFeautureParam> pParam = initParamArray();
		pParam.add(new bcFeautureParam("string", mySource.getFunctionParam()));
		
		String mySQL = 
			" SELECT cd_bk_bank_account_type, name_bk_bank_account_type " +
			"   FROM " + getGeneralDBScheme() + ".vc_club_rel_type_bank_acc_type " +
			"  WHERE cd_club_rel_type = ? " +
			"  ORDER BY name_bk_bank_account_type";
	   // LOGGER.debug(mySQL);
		return send_response(mySQL, pParam, mySource);
	}

	private transport getTermDeviceTypeOptions(transport mySource) {
		ArrayList<bcFeautureParam> pParam = initParamArray();
		pParam.add(new bcFeautureParam("string", mySource.getFunctionParam()));
		
		String mySQL = 
			" SELECT id_device_type, name_device_type " +
			"   FROM " + getGeneralDBScheme() + ".vc_term_device_type_all " +
			"  WHERE cd_term_type = ? " +
			"    AND exist_flag = 'Y'" +
			"  ORDER BY name_device_type";
	   // LOGGER.debug(mySQL);
		return send_response(mySQL, pParam, mySource);
	}
	
	private transport send_response(String mySQL, ArrayList<bcFeautureParam> pParam, transport source){
		transport return_value=new transport();
		return_value.setFunctionName(source.getFunctionName());
		return_value.setIdDestination(source.getIdDestination());
		return_value.setFunctionParam(source.getFunctionParam());
		return_value.setFunctionParam2(source.getFunctionParam2());
		return_value.setFunctionParam3(source.getFunctionParam3());
    	ArrayList<String> keys=new ArrayList<String>();
    	ArrayList<String> values=new ArrayList<String>();
    	
    	PreparedStatement st = null;

		// место наполнения ответа в виде <value> <text>
		// source.getFunctionParam - ID сategory
		// create response
    	
    	LOGGER.debug(prepareSQLToLog(mySQL, pParam));
		try{
			
			con = Connector.getConnection(source.getSessionId());
			st = con.prepareStatement(mySQL);
			st = prepareParam(st, pParam);
			ResultSet rs = st.executeQuery();
	    	while(rs.next()){
	    		keys.add(rs.getString(1));
	    		values.add(rs.getString(2));
	    	}
		} catch (SQLException e) {LOGGER.error("SQLException: " + e.toString());
		} catch (Exception el) {LOGGER.error("Exception: " + el.toString());}
		finally {
            try {
                if (st!=null) {
					st.close();
				}
            } catch (SQLException w) {w.toString();}
            try {
                if (con!=null) {
					con.close();
				}
            } catch (SQLException w) {w.toString();}
            Connector.closeConnection(con);
        } // finally
		return_value.setFunctionKeys(keys.toArray(new String[]{}));
		return_value.setFunctionValues(values.toArray(new String[]{}));
		return return_value;
	}
	
    public transport sendNatPrsFindResultHTML(transport mySource) {
    	
    	bc.service.pages page = new bc.service.pages(mySource.getPageNumber(), mySource.getPageRowCount());
    	String l_beg = page.getFirstRowNumber();
    	String l_end = page.getLastRowNumber();
    	
    	transport return_value=new transport();
		return_value.setFunctionName(mySource.getFunctionName());
		return_value.setIdDestination(mySource.getIdDestination());
		return_value.setFunctionParam(mySource.getFunctionParam());
		
		String findString = mySource.getFunctionParam();
		StringBuilder html = new StringBuilder();
		
		ArrayList<bcFeautureParam> pParam = initParamArray();

		String mySQL =  
        	" SELECT rn, id_nat_prs, full_name, fact_adr_full, date_of_birth, phone_contact, email " +
        	"   FROM (SELECT ROWNUM rn, id_nat_prs, full_name, fact_adr_full, " +
        	"                date_of_birth_frmt date_of_birth, phone_contact, email " +
        	"           FROM (SELECT * " +
        	"                   FROM " + getGeneralDBScheme() + ".vc_nat_prs_club_all n ";
        if (!isEmpty(findString)) {
        	mySQL = mySQL + " WHERE upper(n.FULL_NAME) LIKE UPPER('%'||?||'%') ";
        	pParam.add(new bcFeautureParam("string", findString));
        }
        pParam.add(new bcFeautureParam("int", l_end));
        pParam.add(new bcFeautureParam("int", l_beg));
        mySQL = mySQL +
        	"        ) WHERE ROWNUM < ? " + 
            " ) WHERE rn >= ?";
        
        LOGGER.debug(prepareSQLToLog(mySQL, pParam));
		PreparedStatement st = null;
        String fullName = "";
        String markedFullName = "";
        String idNatPrs = "";
        
        try{
        	con = Connector.getConnection(mySource.getSessionId());
            st = con.prepareStatement(mySQL);
            st = prepareParam(st, pParam);
            ResultSet rset = st.executeQuery();
            int rowCount = 0;
            while (rset.next()){
                fullName = getHTMLValue(rset.getString("FULL_NAME"));
                idNatPrs = getHTMLValue(rset.getString("ID_NAT_PRS"));
                fullName = isEmpty(fullName)?"":fullName;
                idNatPrs = isEmpty(idNatPrs)?"":idNatPrs;
                markedFullName = isEmpty(findString)?fullName:fullName.toUpperCase().replaceAll(findString.toUpperCase(), "<b>"+findString.toUpperCase()+"</b>");
                html.append(
                        "<tr><td><div class=\"div_find\" onClick=\"changeOpener('"+idNatPrs+"','"+fullName+"')\">"+getValue2(idNatPrs) +"</div></td>" +
                        "<td><div class=\"div_find_marked\" onClick=\"changeOpener('"+idNatPrs+"','"+fullName+"')\">"+getValue2(markedFullName)+"</div></td>" +
                        "<td><div class=\"div_find\" onClick=\"changeOpener('"+idNatPrs+"','"+fullName+"')\">"+getValue2(rset.getString("FACT_ADR_FULL"))+"</div></td>" +
                        "<td><div class=\"div_find\" onClick=\"changeOpener('"+idNatPrs+"','"+fullName+"')\">"+getValue2(rset.getString("DATE_OF_BIRTH"))+"</div></td>" +
                        "<td><div class=\"div_find\" onClick=\"changeOpener('"+idNatPrs+"','"+fullName+"')\">"+getValue2(rset.getString("PHONE_CONTACT"))+"</div></td>" +
                        "<td><div class=\"div_find\" onClick=\"changeOpener('"+idNatPrs+"','"+fullName+"')\">"+getValue2(rset.getString("EMAIL"))+"</div></td></tr>");
                       
                rowCount++;
            }
            if (rowCount == 0) {
            	html.append(getNoDataFountTR("6"));
            }
        } // try
        catch (SQLException e) {LOGGER.error(html, e);html.append(prepareSQLToLog(mySQL, pParam) + "<br>" + e.toString());}
        catch (Exception el) {LOGGER.error(html, el);html.append(prepareSQLToLog(mySQL, pParam) + "<br>" + el.toString());}
        finally {
            try {
                if (st!=null) {
					st.close();
				}
            } catch (SQLException w) {w.toString();}
            try {
                if (con!=null) {
					con.close();
				}
            } catch (SQLException w) {w.toString();}
            Connector.closeConnection(con);
        } // finally
        
        html.append("</tbody>");
        html.append("</table>");
        
        return_value.setFunctionHTMLReturnValue(html.toString());
        return return_value;
    } // sendNatPrsFindResultHTML
    
    private String prepareFindString (String pInputValue, String pFindstring) {
    	return isEmpty(pFindstring)?
    			pInputValue:
    				pInputValue.toUpperCase().replaceAll(pFindstring.toUpperCase(), "<b>"+pFindstring.toUpperCase()+"</b>");
    }
	
    public transport sendNatPrsRoleFindResultHTML(transport mySource) {
    	
    	bc.service.pages page = new bc.service.pages(mySource.getPageNumber(), mySource.getPageRowCount());
    	String l_beg = page.getFirstRowNumber();
    	String l_end = page.getLastRowNumber();
    	
    	transport return_value=new transport();
		return_value.setFunctionName(mySource.getFunctionName());
		return_value.setIdDestination(mySource.getIdDestination());
		return_value.setFunctionParam(mySource.getFunctionParam());
		
		String findString = mySource.getFunctionParam();
		String id_service_place_work = mySource.getFunctionParam2();
		StringBuilder html = new StringBuilder();
		
		ArrayList<bcFeautureParam> pParam = initParamArray();

		String mySQL =  
        	" SELECT rn, id_nat_prs_role, fio_nat_prs, phone_mobile, cd_card1, " +
        	"        date_card_sale_frmt, name_nat_prs_role_state " +
        	"   FROM (SELECT ROWNUM rn, id_nat_prs_role, fio_nat_prs, phone_mobile, cd_card1, " +
        	"                date_card_sale_frmt, name_nat_prs_role_state " +
        	"           FROM (SELECT id_nat_prs_role, fio_nat_prs, phone_mobile, cd_card1, " +
        	"                        date_card_sale_frmt, name_nat_prs_role_state " +
        	"                   FROM " + getGeneralDBScheme() + ".vc_nat_prs_role_club_all n " +
        	"                  WHERE 1=1";
        if (!isEmpty(findString)) {
        	mySQL = mySQL + 
        		" AND (UPPER(n.fio_nat_prs) LIKE UPPER('%'||?||'%') OR" +
        		"      cd_card1 LIKE UPPER('%'||?||'%'))";
        	pParam.add(new bcFeautureParam("string", findString));
        	pParam.add(new bcFeautureParam("string", findString));
        }
        if (!isEmpty(id_service_place_work)) {
        	mySQL = mySQL + " AND id_service_place_work = ? ";
        	pParam.add(new bcFeautureParam("int", id_service_place_work));
        }
        pParam.add(new bcFeautureParam("int", l_end));
        pParam.add(new bcFeautureParam("int", l_beg));
        mySQL = mySQL +
        	"        ) WHERE ROWNUM < ? " + 
            " ) WHERE rn >= ?";
        
        LOGGER.debug(prepareSQLToLog(mySQL, pParam));
		PreparedStatement st = null;
        String fioNatPrs = "";
        String markedFioNatPrs = "";
        String idNatPrsRole = "";
        
        try{
        	con = Connector.getConnection(mySource.getSessionId());
            st = con.prepareStatement(mySQL);
            st = prepareParam(st, pParam);
            ResultSet rset = st.executeQuery();
            int rowCount = 0;
            while (rset.next()){
            	fioNatPrs = getHTMLValue(rset.getString("FIO_NAT_PRS"));
            	idNatPrsRole = getHTMLValue(rset.getString("ID_NAT_PRS_ROLE"));
            	markedFioNatPrs = prepareFindString(fioNatPrs, findString);
                html.append(
                        "<td><div class=\"div_find_marked\" onClick=\"changeOpener('"+idNatPrsRole+"','"+fioNatPrs+"')\">"+getValue2(markedFioNatPrs)+"</div></td>" +
                        "<td><div class=\"div_find\" onClick=\"changeOpener('"+idNatPrsRole+"','"+fioNatPrs+"')\">"+getValue2(rset.getString("PHONE_MOBILE"))+"</div></td>" +
                        "<td><div class=\"div_find\" onClick=\"changeOpener('"+idNatPrsRole+"','"+fioNatPrs+"')\">"+getValue2(rset.getString("CD_CARD1"))+"</div></td>" +
                        "<td><div class=\"div_find\" onClick=\"changeOpener('"+idNatPrsRole+"','"+fioNatPrs+"')\">"+getValue2(rset.getString("DATE_CARD_SALE_FRMT"))+"</div></td>" +
                        "<td><div class=\"div_find\" onClick=\"changeOpener('"+idNatPrsRole+"','"+fioNatPrs+"')\">"+getValue2(rset.getString("NAME_NAT_PRS_ROLE_STATE"))+"</div></td></tr>");
                       
                rowCount++;
            }
            if (rowCount == 0) {
            	html.append(getNoDataFountTR("5"));
            }
        } // try
        catch (SQLException e) {LOGGER.error(html, e); html.append(prepareSQLToLog(mySQL, pParam) + "<br>" + e.toString());}
        catch (Exception el) {LOGGER.error(html, el);html.append(prepareSQLToLog(mySQL, pParam) + "<br>" + el.toString());}
        finally {
            try {
                if (st!=null) {
					st.close();
				}
            } catch (SQLException w) {w.toString();}
            try {
                if (con!=null) {
					con.close();
				}
            } catch (SQLException w) {w.toString();}
            Connector.closeConnection(con);
        } // finally
        
        html.append("</tbody>");
        html.append("</table>");
        
        return_value.setFunctionHTMLReturnValue(html.toString());
        return return_value;
    } // sendNatPrsFindResultHTML

    public transport getTerminalsList(transport mySource) {
    	
    	bc.service.pages page = new bc.service.pages(mySource.getPageNumber(), mySource.getPageRowCount());
    	String l_beg = page.getFirstRowNumber();
    	String l_end = page.getLastRowNumber();
    	
    	transport return_value=new transport();
		return_value.setFunctionName(mySource.getFunctionName());
		return_value.setIdDestination(mySource.getIdDestination());
		return_value.setFunctionParam(mySource.getFunctionParam());
		
		String findString = mySource.getFunctionParam();
		String idTerminalType = mySource.getFunctionParam2();
		String idServicePlace = mySource.getFunctionParam3();
		StringBuilder html = new StringBuilder();
		
		ArrayList<bcFeautureParam> pParam = initParamArray();
		
		String mySQL =  
        	" SELECT id_term, id_term_hex, name_term_type, " +
        	"        sname_service_place, adr_service_place, name_term_status " +
        	"   FROM (SELECT ROWNUM rn, id_term, id_term_hex, name_term_type, " +
        	"				 DECODE(cd_term_status, " +
			"                  		'ACTIVE', '<b><font color=\"green\">'||name_term_status||'</font></b>', " +
			"                  		'SETTING', '<font color=\"red\">'||name_term_status||'</font>', " +
			"                  		'EXCLUDED', '<b><font color=\"red\">'||name_term_status||'</font></b>', " +
			"                  		'BLOCKED', '<b><font color=\"red\">'||name_term_status||'</font></b>', " +
			"                  		name_term_status" +
			"                ) name_term_status, " +
            "                sname_service_place, adr_service_place " +
        	"	        FROM (SELECT * " +
        	"                   FROM " + getGeneralDBScheme() + ".vc_term_club_all " +
        	"                  WHERE 1=1 ";
        if (!isEmpty(findString)) {
           	mySQL = mySQL +
           		" AND (TO_CHAR(id_term) LIKE UPPER('%'||?||'%') OR " +
           		"      TO_CHAR(id_term_hex) LIKE UPPER'%'||?||'%')) ";
           	for (int i=0; i<2; i++) {
           	    pParam.add(new bcFeautureParam("string", findString));
           	}
        }
        if (!isEmpty(idServicePlace)) {
        	mySQL = mySQL + " AND id_service_place = ? ";
        	pParam.add(new bcFeautureParam("int", idServicePlace));
        }
        if (!isEmpty(idTerminalType)) {
        	mySQL = mySQL + " AND id_device_type = ? ";
        	pParam.add(new bcFeautureParam("int", idTerminalType));
        }
        pParam.add(new bcFeautureParam("int", l_end));
        pParam.add(new bcFeautureParam("int", l_beg));
        mySQL = mySQL + " ORDER BY id_term" +
        	"         ) WHERE ROWNUM < ?)"+
            "  WHERE rn >= ?";
        
        LOGGER.debug(prepareSQLToLog(mySQL, pParam));
        PreparedStatement st = null;
        String idTerm = "";
        String markedIdTerm = "";
        try{
        	con = Connector.getConnection(mySource.getSessionId());
            st = con.prepareStatement(mySQL);
            st = prepareParam(st, pParam);
            ResultSet rset = st.executeQuery();
            int rowCount = 0;
            while (rset.next()){
                idTerm = getHTMLValue(rset.getString("ID_TERM"));
                markedIdTerm = prepareFindString(idTerm, findString);
                
                html.append(
                        "<tr><td align=\"center\"><div class=\"div_find_marked\" onClick=\"changeOpener('"+idTerm+"')\">"+getValue2(markedIdTerm) +"</div></td>" +
                        "<td align=\"center\"><div class=\"div_find\" onClick=\"changeOpener('"+idTerm+"')\">"+getValue2(rset.getString("ID_TERM_HEX"))+"</div></td>" +
                        "<td><div class=\"div_find\" onClick=\"changeOpener('"+idTerm+"')\">"+getValue2(rset.getString("NAME_TERM_TYPE"))+"</div></td>" +
                        "<td><div class=\"div_find\" onClick=\"changeOpener('"+idTerm+"')\">"+getValue2(rset.getString("SNAME_SERVICE_PLACE"))+"</div></td>" +
                        "<td><div class=\"div_find\" onClick=\"changeOpener('"+idTerm+"')\">"+getValue2(rset.getString("ADR_SERVICE_PLACE"))+"</div></td>" +
                        "<td><div class=\"div_find\" onClick=\"changeOpener('"+idTerm+"')\">"+getValue2(rset.getString("NAME_TERM_STATUS"))+"</div></td></tr>");
                       
                rowCount++;
            }

            if (rowCount == 0) {
            	html.append(getNoDataFountTR("7"));
            }
        } // try
        catch (SQLException e) {LOGGER.error(html, e);html.append(prepareSQLToLog(mySQL, pParam) + "<br>" + e.toString());}
        catch (Exception el) {LOGGER.error(html, el);html.append(prepareSQLToLog(mySQL, pParam) + "<br>" + el.toString());}
        finally {
            try {
                if (st!=null) {
					st.close();
				}
            } catch (SQLException w) {w.toString();}
            try {
                if (con!=null) {
					con.close();
				}
            } catch (SQLException w) {w.toString();}
            Connector.closeConnection(con);
        } // finally

        html.append("</tbody>");
        html.append("</table>");
        
        return_value.setFunctionHTMLReturnValue(html.toString());
        return return_value;
    } // getTerminalsList

    public transport getUsersList(transport mySource) {
    	
    	bc.service.pages page = new bc.service.pages(mySource.getPageNumber(), mySource.getPageRowCount());
    	String l_beg = page.getFirstRowNumber();
    	String l_end = page.getLastRowNumber();
    	
    	transport return_value=new transport();
		return_value.setFunctionName(mySource.getFunctionName());
		return_value.setIdDestination(mySource.getIdDestination());
		return_value.setFunctionParam(mySource.getFunctionParam());
		
		String findString = mySource.getFunctionParam();
		String idNatPrsRole = mySource.getFunctionParam2();
		String id_service_place_work = mySource.getFunctionParam3();
		StringBuilder html = new StringBuilder();
		
		ArrayList<bcFeautureParam> pParam = initParamArray();

		String mySQL = 
        	" SELECT name_user, fio_nat_prs, sname_service_place_work, name_user_status, id_user  " +
	  		"   FROM (SELECT ROWNUM rn, name_user, fio_nat_prs, sname_service_place_work, name_user_status, id_user  " +
  			"           FROM (SELECT * " +
  			"                   FROM " + getGeneralDBScheme() + ".vc_users_all " +
  			"                  WHERE 1=1 ";
		if (!isEmpty(idNatPrsRole)) {
			mySQL = mySQL + " AND id_nat_prs_role = ? ";
       	    pParam.add(new bcFeautureParam("int", idNatPrsRole));
		}
		if (!isEmpty(id_service_place_work)) {
			mySQL = mySQL + " AND id_service_place_work = ? ";
       	    pParam.add(new bcFeautureParam("int", id_service_place_work));
		}
        if (!isEmpty(findString)) {
           	mySQL = mySQL +
           		" AND (TO_CHAR(id_user) LIKE '%'||?||'%' " +
           		"      OR UPPER(name_user) LIKE UPPER('%'||?||'%') " +
           		"      OR UPPER(fio_nat_prs) LIKE UPPER('%'||?||'%')) ";
           	for (int i=0; i<3; i++) {
           	    pParam.add(new bcFeautureParam("string", findString));
           	}
        }
        pParam.add(new bcFeautureParam("int", l_end));
        pParam.add(new bcFeautureParam("int", l_beg));
        mySQL = mySQL +
  			"                  ORDER BY name_user" +
  			"        ) WHERE ROWNUM < ?" + 
  			" ) WHERE rn >= ?";
        
        LOGGER.debug(prepareSQLToLog(mySQL, pParam));
        PreparedStatement st = null;
        String idUser = "";
        String nameUser = "";
        String fioUser = "";
        String markedNameUser = "";
        String markedFioUser = "";
        
        try{
        	con = Connector.getConnection(mySource.getSessionId());
            st = con.prepareStatement(mySQL);
            st = prepareParam(st, pParam);
            ResultSet rset = st.executeQuery();
            int rowCount = 0;
            while (rset.next()){
                idUser = getHTMLValue(rset.getString("ID_USER"));
                nameUser = getHTMLValue(rset.getString("NAME_USER"));
                fioUser = getHTMLValue(rset.getString("FIO_NAT_PRS"));
                markedNameUser = prepareFindString(nameUser, findString);
                markedFioUser = prepareFindString(fioUser, findString);
                String returnNameUser = nameUser + (isEmpty(fioUser)?"":" ("+fioUser+")");
                html.append(
                        "<tr><td><div class=\"div_find_marked\" onClick=\"changeOpener('"+idUser+"','"+returnNameUser+"')\">"+getValue2(markedNameUser) +"</div></td>" +
                        "<td><div class=\"div_find\" onClick=\"changeOpener('"+idUser+"','"+returnNameUser+"')\">"+getValue2(markedFioUser)+"</div></td>" +
                        "<td><div class=\"div_find\" onClick=\"changeOpener('"+idUser+"','"+returnNameUser+"')\">"+getValue2(rset.getString("SNAME_SERVICE_PLACE_WORK"))+"</div></td>" +
                        "<td><div class=\"div_find\" onClick=\"changeOpener('"+idUser+"','"+returnNameUser+"')\">"+getValue2(rset.getString("NAME_USER_STATUS"))+"</div></td></tr>");
                       
                rowCount++;
            }
            if (rowCount == 0) {
            	html.append(getNoDataFountTR("5"));
            }
        } // try
        catch (SQLException e) {LOGGER.error(html, e);html.append(prepareSQLToLog(mySQL, pParam) + "<br>" + e.toString());}
        catch (Exception el) {LOGGER.error(html, el);html.append(prepareSQLToLog(mySQL, pParam) + "<br>" + el.toString());}
        finally {
            try {
                if (st!=null) {
					st.close();
				}
            } catch (SQLException w) {w.toString();}
            try {
                if (con!=null) {
					con.close();
				}
            } catch (SQLException w) {w.toString();}
            Connector.closeConnection(con);
        } // finally

        html.append("</tbody>");
        html.append("</table>");
        return_value.setFunctionHTMLReturnValue(html.toString());
        return return_value;
    } // getUsersList

    public transport getCallCenterUsersList(transport mySource) {
    	
    	bc.service.pages page = new bc.service.pages(mySource.getPageNumber(), mySource.getPageRowCount());
    	String l_beg = page.getFirstRowNumber();
    	String l_end = page.getLastRowNumber();
    	
    	transport return_value=new transport();
		return_value.setFunctionName(mySource.getFunctionName());
		return_value.setIdDestination(mySource.getIdDestination());
		return_value.setFunctionParam(mySource.getFunctionParam());
		
		String findString = mySource.getFunctionParam();
		StringBuilder html = new StringBuilder();
		
		ArrayList<bcFeautureParam> pParam = initParamArray();

		String mySQL = 
        	" SELECT id_user, name_user, desc_user, name_module_type, name_user_status  " +
	  		"   FROM (SELECT ROWNUM rn, id_user, name_user, desc_user, name_module_type, " +
	  		"                name_cc_user_status name_user_status  " +
  			"           FROM (SELECT * " +
  			"                   FROM " + getGeneralDBScheme() + ".vc_cc_users_all ";
        if (!isEmpty(findString)) {
        	mySQL = mySQL +
  			"          WHERE (TO_CHAR(id_user) LIKE UPPER('%'||?||'%') OR " +
  			"                 UPPER(name_user) LIKE UPPER('%'||?||'%') OR " +
  			"                 UPPER(desc_user) LIKE UPPER('%'||?||'%')) ";
        	for (int i=0; i<3; i++) {
        	    pParam.add(new bcFeautureParam("string", findString));
        	}
        }
        pParam.add(new bcFeautureParam("int", l_end));
        pParam.add(new bcFeautureParam("int", l_beg));
  		mySQL = mySQL +
  			"                  ORDER BY name_user" +
  			"        ) WHERE ROWNUM < ?" + 
  			" ) WHERE rn >= ?";
        
        LOGGER.debug(prepareSQLToLog(mySQL, pParam));
        PreparedStatement st = null;
        String idUser = "";
        String nameUser = "";
        String descUser = "";
        String markedIdUser = "";
        String markedNameUser = "";
        String markedDescUser = "";
        
        try{
        	con = Connector.getConnection(mySource.getSessionId());
            st = con.prepareStatement(mySQL);
            st = prepareParam(st, pParam);
            ResultSet rset = st.executeQuery();
            int rowCount = 0;
            while (rset.next()){
                idUser = getHTMLValue(rset.getString("ID_USER"));
                nameUser = getHTMLValue(rset.getString("NAME_USER"));
                descUser = getHTMLValue(rset.getString("DESC_USER"));
                markedIdUser = prepareFindString(idUser, findString);
                markedNameUser = prepareFindString(nameUser, findString);
                markedDescUser = prepareFindString(descUser, findString);
                html.append(
                        "<tr><td><div class=\"div_find\" onClick=\"changeOpener('"+idUser+"','"+nameUser+"')\">"+getValue2(markedIdUser) +"</div></td>" +
                        "<td><div class=\"div_find_marked\" onClick=\"changeOpener('"+idUser+"','"+nameUser+"')\">"+getValue2(markedNameUser)+"</div></td>" +
                        "<td><div class=\"div_find\" onClick=\"changeOpener('"+idUser+"','"+nameUser+"')\">"+getValue2(markedDescUser)+"</div></td>" +
                        "<td><div class=\"div_find\" onClick=\"changeOpener('"+idUser+"','"+nameUser+"')\">"+getValue2(rset.getString("NAME_MODULE_TYPE"))+"</div></td>" +
                        "<td><div class=\"div_find\" onClick=\"changeOpener('"+idUser+"','"+nameUser+"')\">"+getValue2(rset.getString("NAME_USER_STATUS"))+"</div></td></tr>");
                       
                rowCount++;
            }
            if (rowCount == 0) {
            	html.append(getNoDataFountTR("5"));
            }
        } // try
        catch (SQLException e) {LOGGER.error(html, e);html.append(prepareSQLToLog(mySQL, pParam) + "<br>" + e.toString());}
        catch (Exception el) {LOGGER.error(html, el);html.append(prepareSQLToLog(mySQL, pParam) + "<br>" + el.toString());}
        finally {
            try {
                if (st!=null) {
					st.close();
				}
            } catch (SQLException w) {w.toString();}
            try {
                if (con!=null) {
					con.close();
				}
            } catch (SQLException w) {w.toString();}
            Connector.closeConnection(con);
        } // finally

        html.append("</tbody>");
        html.append("</table>");
        
        return_value.setFunctionHTMLReturnValue(html.toString());
        return return_value;
    } // getUsersList

    public transport getBKAccountSchemeList(transport mySource) {
    	
    	bc.service.pages page = new bc.service.pages(mySource.getPageNumber(), mySource.getPageRowCount());
    	String l_beg = page.getFirstRowNumber();
    	String l_end = page.getLastRowNumber();
    	
    	transport return_value=new transport();
		return_value.setFunctionName(mySource.getFunctionName());
		return_value.setIdDestination(mySource.getIdDestination());
		return_value.setFunctionParam(mySource.getFunctionParam());
		
		String findString = mySource.getFunctionParam();
		String idBKAccountScheme = mySource.getFunctionParam2();
		StringBuilder html = new StringBuilder();
		
		ArrayList<bcFeautureParam> pParam = initParamArray();

		String mySQL = 
        	" SELECT desc_bk_account_scheme, id_bk_account_scheme_line, cd_bk_account_scheme_line, name_bk_account_scheme_line, " +
        	"        is_group_tsl, exist_flag_tsl  " +
	  		"   FROM (SELECT ROWNUM rn, desc_bk_account_scheme, id_bk_account_scheme_line, cd_bk_account_scheme_line, " +
	  		"                name_bk_account_scheme_line, is_group_tsl, exist_flag_tsl  " +
  			"           FROM (SELECT * " +
  			"                   FROM " + getGeneralDBScheme() + ".vc_bk_account_sh_ln_club_all " +
  			"                  WHERE 1=1 ";
        if (!isEmpty(idBKAccountScheme)) {
        	mySQL = mySQL + " AND id_bk_account_scheme = ? ";
        	pParam.add(new bcFeautureParam("int", idBKAccountScheme));
        }
        if (!isEmpty(findString)) {
        	mySQL = mySQL +
        	"          AND (TO_CHAR(id_bk_account_scheme_line) LIKE UPPER('%'||?||'%') OR " +
  			"               UPPER(cd_bk_account_scheme_line) LIKE UPPER('%'||?||'%') OR " +
  			"               UPPER(cd_bk_account_scheme_line||' - '||name_bk_account_scheme_line) LIKE UPPER('%'||?||'%') OR " +
  			"               UPPER(name_bk_account_scheme_line) LIKE UPPER('%'||?||'%')) ";
        	for (int i=0; i<4; i++) {
        	    pParam.add(new bcFeautureParam("string", findString));
        	}
        }
        pParam.add(new bcFeautureParam("int", l_end));
        pParam.add(new bcFeautureParam("int", l_beg));
        mySQL = mySQL +
			"          ORDER BY cd_bk_account_scheme_line" +
  			"        ) WHERE ROWNUM < ? " + 
  			" ) WHERE rn >= ?";
        
        LOGGER.debug(prepareSQLToLog(mySQL, pParam));
        PreparedStatement st = null;
        String idScheme = "";
        String cdScheme = "";
        String nameScheme = "";
        String markedIdScheme = "";
        String markedCdScheme = "";
        String markedNameScheme = "";
        
        try{
        	con = Connector.getConnection(mySource.getSessionId());
            st = con.prepareStatement(mySQL);
            st = prepareParam(st, pParam);
            ResultSet rset = st.executeQuery();
            int rowCount = 0;
            while (rset.next()){
                idScheme = getHTMLValue(rset.getString("ID_BK_ACCOUNT_SCHEME_LINE"));
                cdScheme = getHTMLValue(rset.getString("CD_BK_ACCOUNT_SCHEME_LINE"));
                nameScheme = getHTMLValue(rset.getString("NAME_BK_ACCOUNT_SCHEME_LINE"));
                markedIdScheme = prepareFindString(idScheme, findString);
                markedCdScheme = prepareFindString(cdScheme, findString);
                markedNameScheme = prepareFindString(nameScheme, findString);
                
                html.append(
                		"<tr><td><div class=\"div_find\" onClick=\"changeOpener('"+idScheme+"','"+cdScheme+" - "+nameScheme+"')\">"+getValue2(rset.getString("DESC_BK_ACCOUNT_SCHEME"))+"</div></td>" +
                        "<td><div class=\"div_find\" onClick=\"changeOpener('"+idScheme+"','"+cdScheme+" - "+nameScheme+"')\">"+getValue2(markedIdScheme) +"</div></td>" +
                        "<td><div class=\"div_find\" onClick=\"changeOpener('"+idScheme+"','"+cdScheme+" - "+nameScheme+"')\">"+getValue2(markedCdScheme)+"</div></td>" +
                        "<td><div class=\"div_find_marked\" onClick=\"changeOpener('"+idScheme+"','"+cdScheme+" - "+nameScheme+"')\">"+getValue2(markedNameScheme)+"</div></td>" +
                        "<td><div class=\"div_find\" onClick=\"changeOpener('"+idScheme+"','"+cdScheme+" - "+nameScheme+"')\">"+getValue2(rset.getString("IS_GROUP_TSL"))+"</div></td>" +
                        "<td><div class=\"div_find\" onClick=\"changeOpener('"+idScheme+"','"+cdScheme+" - "+nameScheme+"')\">"+getValue2(rset.getString("EXIST_FLAG_TSL"))+"</div></td></tr>");
                       
                rowCount++;
            }
            if (rowCount == 0) {
            	html.append(getNoDataFountTR("5"));
            }
        } // try
        catch (SQLException e) {LOGGER.error(html, e);html.append(prepareSQLToLog(mySQL, pParam) + "<br>" + e.toString());}
        catch (Exception el) {LOGGER.error(html, el);html.append(prepareSQLToLog(mySQL, pParam) + "<br>" + el.toString());}
        finally {
            try {
                if (st!=null) {
					st.close();
				}
            } catch (SQLException w) {w.toString();}
            try {
                if (con!=null) {
					con.close();
				}
            } catch (SQLException w) {w.toString();}
            Connector.closeConnection(con);
        } // finally

        html.append("</tbody>");
        html.append("</table>");
        
        return_value.setFunctionHTMLReturnValue(html.toString());
        return return_value;
    } // getUsersList

    public transport getBKOperationSchemeLinesList(transport mySource) {
    	
    	bc.service.pages page = new bc.service.pages(mySource.getPageNumber(), mySource.getPageRowCount());
    	String l_beg = page.getFirstRowNumber();
    	String l_end = page.getLastRowNumber();
    	
    	transport return_value=new transport();
		return_value.setFunctionName(mySource.getFunctionName());
		return_value.setIdDestination(mySource.getIdDestination());
		return_value.setFunctionParam(mySource.getFunctionParam());
		
		String findString = mySource.getFunctionParam();
		
		StringBuilder html = new StringBuilder();
		
		ArrayList<bcFeautureParam> pParam = initParamArray();

		String mySQL = 
        	" SELECT id_bk_operation_scheme_line, name_club_rel_type, name_bk_operation_type, " +
        	"        name_bk_phase, oper_number, debet_cd_bk_account, " +
        	"        credit_cd_bk_account, oper_content," +
        	"        oper_number||' ('||debet_cd_bk_account||' - '||credit_cd_bk_account||')' full_name  " +
	  		"   FROM (SELECT ROWNUM rn, id_bk_operation_scheme_line, name_club_rel_type, name_bk_operation_type, " +
        	"                name_bk_phase, oper_number, debet_cd_bk_account_sh_line debet_cd_bk_account, " +
        	"                credit_cd_bk_account_sh_line credit_cd_bk_account, oper_content  " +
  			"           FROM (SELECT * " +
  			"                   FROM " + getGeneralDBScheme() + ".vc_oper_scheme_lines_club_all ";
        if (!isEmpty(findString)) {
        	mySQL = mySQL +
        	"          WHERE (TO_CHAR(id_bk_operation_scheme_line) = UPPER(''||?||'') OR " +
  			"                 UPPER(debet_cd_bk_account_sh_line) LIKE UPPER('%'||?||'%') OR " +
  			"                 UPPER(credit_cd_bk_account_sh_line) LIKE UPPER('%'||?||'%') OR " +
  			"                 UPPER(debet_cd_bk_account_sh_line||' - '||credit_cd_bk_account_sh_line||' ('||name_bk_operation_type||')') = UPPER(''||?||'') OR" +
  			"                 UPPER(oper_number) LIKE UPPER('%'||?||'%') OR " +
  			"                 UPPER(oper_content) LIKE UPPER('%'||?||'%')) ";
        	for (int i=0; i<6; i++) {
        	    pParam.add(new bcFeautureParam("string", findString));
        	}
        }
        pParam.add(new bcFeautureParam("int", l_end));
        pParam.add(new bcFeautureParam("int", l_beg));
        mySQL = mySQL +
			"          ORDER BY id_bk_operation_scheme_line" +
  			"        ) WHERE ROWNUM < ?" + 
  			" ) WHERE rn >= ?";
        
        LOGGER.debug(prepareSQLToLog(mySQL, pParam));
        PreparedStatement st = null;
        String idLine = "";
        String debetScheme = "";
        String creditScheme = "";
        String contentScheme = "";
        String markedIdLine = "";
        String markedDebetScheme = "";
        String markedCreditScheme = "";
        String markedContentScheme = "";
        String fullName = "";
        
        try{
        	con = Connector.getConnection(mySource.getSessionId());
            st = con.prepareStatement(mySQL);
            st = prepareParam(st, pParam);
            ResultSet rset = st.executeQuery();
            int rowCount = 0;
            while (rset.next()){
            	fullName = getHTMLValue(rset.getString("FULL_NAME"));
            	idLine = getHTMLValue(rset.getString("ID_BK_OPERATION_SCHEME_LINE"));
                debetScheme = getHTMLValue(rset.getString("DEBET_CD_BK_ACCOUNT"));
                creditScheme = getHTMLValue(rset.getString("CREDIT_CD_BK_ACCOUNT"));
                contentScheme = getHTMLValue(rset.getString("OPER_CONTENT"));
                markedIdLine = prepareFindString(idLine, findString);
                markedDebetScheme = prepareFindString(debetScheme, findString);
                markedCreditScheme = prepareFindString(creditScheme, findString);
                markedContentScheme = prepareFindString(contentScheme, findString);
                
                html.append(
                        "<tr><td><div class=\"div_find\" onClick=\"changeOpener('"+idLine+"','"+fullName+"')\">"+getValue2(markedIdLine) +"</div></td>" +
                        "<td><div class=\"div_find\" onClick=\"changeOpener('"+idLine+"','"+fullName+"')\">"+getValue2(rset.getString("NAME_CLUB_REL_TYPE"))+"</div></td>" +
                        "<td><div class=\"div_find\" onClick=\"changeOpener('"+idLine+"','"+fullName+"')\">"+getValue2(rset.getString("NAME_BK_OPERATION_TYPE"))+"</div></td>" +
                        "<td><div class=\"div_find\" onClick=\"changeOpener('"+idLine+"','"+fullName+"')\">"+getValue2(rset.getString("NAME_BK_PHASE"))+"</div></td>" +
                        "<td><div class=\"div_find\" onClick=\"changeOpener('"+idLine+"','"+fullName+"')\">"+getValue2(rset.getString("OPER_NUMBER"))+"</div></td>" +
                        "<td><div class=\"div_find\" onClick=\"changeOpener('"+idLine+"','"+fullName+"')\">"+getValue2(markedDebetScheme)+"</div></td>" +
                        "<td><div class=\"div_find\" onClick=\"changeOpener('"+idLine+"','"+fullName+"')\">"+getValue2(markedCreditScheme)+"</div></td>" +
                        "<td><div class=\"div_find\" onClick=\"changeOpener('"+idLine+"','"+fullName+"')\">"+getValue2(markedContentScheme)+"</div></td></tr>");
                       
                rowCount++;
            }
            if (rowCount == 0) {
            	html.append(getNoDataFountTR("8"));
            }
        } // try
        catch (SQLException e) {LOGGER.error(html, e);html.append(prepareSQLToLog(mySQL, pParam) + "<br>" + e.toString());}
        catch (Exception el) {LOGGER.error(html, el);html.append(prepareSQLToLog(mySQL, pParam) + "<br>" + el.toString());}
        finally {
            try {
                if (st!=null) {
					st.close();
				}
            } catch (SQLException w) {w.toString();}
            try {
                if (con!=null) {
					con.close();
				}
            } catch (SQLException w) {w.toString();}
            Connector.closeConnection(con);
        } // finally

        html.append("</tbody>");
        html.append("</table>");
        
        return_value.setFunctionHTMLReturnValue(html.toString());
        return return_value;
    } // getUsersList

    public String getValue2(String pValue){ // TEMPORARY FOR DEMO!!
    	String result;
    	if (pValue == null || "null".equalsIgnoreCase(pValue) || "".equalsIgnoreCase(pValue)) {
    		result = "&nbsp;";
    	} else {
    		result = pValue;    		
    	}
    	return result;
    } // end of getValue2

	private transport check_calculator_expression(transport source){
		transport return_value=new transport();
		return_value.setFunctionName(source.getFunctionName());
		return_value.setIdDestination(source.getIdDestination());
		return_value.setFunctionParam(source.getFunctionParam());
		return_value.setFunctionParam2(source.getFunctionParam2());
		
    	ArrayList<String> keys=new ArrayList<String>();
    	ArrayList<String> values=new ArrayList<String>();
		
		String formula = source.getFunctionParam();
		String needCalc = source.getFunctionParam2();
		
    	String mySQL = "{? = call " + getGeneralDBScheme() + ".PACK_UI_CALCULATOR.check_expression(?,?,?,?)}";
    	
    	LOGGER.debug(mySQL);
    	
    	String[] results = new String[2];
    	
    	String[] pParam = new String[1];
		pParam[0] = formula;
		pParam[1] = needCalc;
		
    	results = myCallFunctionParam3(mySQL, pParam, source.getSessionId());
		String resultInt = results[0];
		String resultMessage = results[1];
		String resultString = results[2];
		
		keys.add(resultMessage);
		values.add(resultString);

		if (!"0".equalsIgnoreCase(resultInt)) { 
			resultString = resultString + ":<br> " + resultMessage;
		}

		return_value.setFunctionHTMLReturnValue(resultMessage);
		return_value.setFunctionKeys(keys.toArray(new String[]{}));
		return_value.setFunctionValues(values.toArray(new String[]{}));
		return return_value;
	}

    public transport getJurPrsList(transport mySource) {
    	
    	bc.service.pages page = new bc.service.pages(mySource.getPageNumber(), mySource.getPageRowCount());
    	String l_beg = page.getFirstRowNumber();
    	String l_end = page.getLastRowNumber();
    	
    	transport return_value=new transport();
		return_value.setFunctionName(mySource.getFunctionName());
		return_value.setIdDestination(mySource.getIdDestination());
		return_value.setFunctionParam(mySource.getFunctionParam());
		
		String findString = mySource.getFunctionParam();
		String jurPrsType = mySource.getFunctionParam2();
		String filtr = mySource.getFunctionParam3();
		
		StringBuilder html = new StringBuilder();
		
		ArrayList<bcFeautureParam> pParam = initParamArray();

		String mySQL = 
        	" SELECT id_jur_prs, inn_number, name_jur_prs, sname_jur_prs, jur_adr_full  " +
	  		"   FROM (SELECT ROWNUM rn, id_jur_prs, inn_number, name_jur_prs, sname_jur_prs, jur_adr_full  " +
  			"           FROM (SELECT * " +
  			"                   FROM " + getGeneralDBScheme() + ".vc_jur_prs_club_all " +
  			"                  WHERE cd_jur_prs_status <> 'SERVICE_PLACE' ";
  		if (!isEmpty(findString)) {
  			mySQL = mySQL + 
  				" AND (TO_CHAR(id_jur_prs) LIKE UPPER('%'||?||'%') OR " +
  				"      UPPER(name_jur_prs) LIKE UPPER('%'||?||'%') OR " +
  				"      UPPER(sname_jur_prs) LIKE UPPER('%'||?||'%')) ";
  			for (int i=0; i<3; i++) {
  			    pParam.add(new bcFeautureParam("string", findString));
  			}
  		}
        if (!"ALL".equalsIgnoreCase(jurPrsType)) {
        	if (!"CARD_SELLER".equalsIgnoreCase(jurPrsType)) {
        		mySQL = mySQL + " AND ";
        		if ("DEALER".equalsIgnoreCase(jurPrsType)) {
            		mySQL = mySQL + "NVL(is_dealer, 'N') = 'Y'";
            	} else if ("BANK".equalsIgnoreCase(jurPrsType)) {
            		mySQL = mySQL + "NVL(is_bank, 'N') = 'Y'";
            	} else if ("OPERATOR".equalsIgnoreCase(jurPrsType)) {
            		mySQL = mySQL + "NVL(is_operator, 'N') = 'Y'";
            	} else if ("PARTNER".equalsIgnoreCase(jurPrsType)) {
            		mySQL = mySQL + "NVL(is_partner, 'N') = 'Y'";
            	} else if ("TERMINAL_MANUFACTURER".equalsIgnoreCase(jurPrsType)) {
            		mySQL = mySQL + "NVL(is_terminal_manufacturer, 'N') = 'Y'";
            	//} else if ("SERVICE_PLACE".equalsIgnoreCase(jurPrsType)) {
            	//	mySQL = mySQL + "cd_jur_prs_status = 'SERVICE_PLACE'";
            	}
        	}
        }
        if (!isEmpty(filtr)) {
        	if ("ISSUER".equalsIgnoreCase(filtr)) {
        		mySQL = mySQL + " AND is_issuer = 'Y'";
        	} else if ("FIN_ACQUIRER".equalsIgnoreCase(filtr)) {
        		mySQL = mySQL + " AND is_finance_acquirer = 'Y'";
        	} else if ("TECH_ACQUIRER".equalsIgnoreCase(filtr)) {
        		mySQL = mySQL + " AND is_technical_acquirer = 'Y'";
        	}
        }
        pParam.add(new bcFeautureParam("int", l_end));
        pParam.add(new bcFeautureParam("int", l_beg));
        mySQL = mySQL +
        	"          ORDER BY sname_jur_prs" +
			"       ) WHERE ROWNUM < ? " + 
  			" ) WHERE rn >= ?";
        
        LOGGER.debug(prepareSQLToLog(mySQL, pParam));
        PreparedStatement st = null;
        String idJurPrs = "";
        String nameJurPrs = "";
        String sNameJurPrs = "";
        String markedNameJurPrs = "";
        //String markedSNameJurPrs = "";
       
        try{
        	con = Connector.getConnection(mySource.getSessionId());
            st = con.prepareStatement(mySQL);
            st = prepareParam(st, pParam);
            ResultSet rset = st.executeQuery();
            int rowCount = 0;
            while (rset.next()){
            	idJurPrs = getHTMLValue(rset.getString("ID_JUR_PRS"));
            	nameJurPrs = getHTMLValue(rset.getString("NAME_JUR_PRS"));
            	sNameJurPrs = getHTMLValue(rset.getString("SNAME_JUR_PRS"));
            	markedNameJurPrs = prepareFindString(nameJurPrs, findString);
            	//markedSNameJurPrs = prepareFindString(sNameJurPrs, findString);
                
                html.append(
                        //"<tr><td><div class=\"div_find\" onClick=\"changeOpener('"+idJurPrs+"','"+nameJurPrs+"')\">"+getValue2(markedIdJurPrs) +"</div></td>" +
                        "<td><div class=\"div_find_marked\" onClick=\"changeOpener('"+idJurPrs+"','"+sNameJurPrs+"')\">"+getValue2(markedNameJurPrs)+"</div></td>" +
                        "<td><div class=\"div_find\" onClick=\"changeOpener('"+idJurPrs+"','"+nameJurPrs+"')\">"+getValue2(rset.getString("INN_NUMBER"))+"</div></td>" +
                        //"<td><div class=\"div_find\" onClick=\"changeOpener('"+idJurPrs+"','"+sNameJurPrs+"')\">"+getValue2(markedSNameJurPrs)+"</div></td>" +
                        "<td><div class=\"div_find\" onClick=\"changeOpener('"+idJurPrs+"','"+sNameJurPrs+"')\">"+getValue2(rset.getString("JUR_ADR_FULL"))+"</div></td></tr>");
                       
                rowCount++;
            }
            if (rowCount == 0) {
            	html.append(getNoDataFountTR("5"));
            }
        } // try
        catch (SQLException e) {LOGGER.error(html, e); html.append(prepareSQLToLog(mySQL, pParam) + "<br>" + e.toString());}
        catch (Exception el) {LOGGER.error(html, el); html.append(prepareSQLToLog(mySQL, pParam) + "<br>" + el.toString());}
        finally {
            try {
                if (st!=null) {
					st.close();
				}
            } catch (SQLException w) {w.toString();}
            try {
                if (con!=null) {
					con.close();
				}
            } catch (SQLException w) {w.toString();}
            Connector.closeConnection(con);
        } // finally

        html.append("</tbody>");
        html.append("</table>");
        
        return_value.setFunctionHTMLReturnValue(html.toString());
        return return_value;
    } // getJurPrsList

    public transport getJurAndNatPrsList(transport mySource) {
    	
    	bc.service.pages page = new bc.service.pages(mySource.getPageNumber(), mySource.getPageRowCount());
    	String l_beg = page.getFirstRowNumber();
    	String l_end = page.getLastRowNumber();
    	
    	transport return_value=new transport();
		return_value.setFunctionName(mySource.getFunctionName());
		return_value.setIdDestination(mySource.getIdDestination());
		return_value.setFunctionParam(mySource.getFunctionParam());
		
		String findString = mySource.getFunctionParam();
		String type = mySource.getFunctionParam2();
		String typeId = mySource.getFunctionParam3();
		System.out.println("findString="+findString);
		System.out.println("type="+type);
		System.out.println("typeId="+typeId);
		//String existType = mySource.getFunctionParam3();
		if (isEmpty(type)) {
			type = "ALL";
		}
		String jurPrsTypeName = commonXML.getfieldTransl("entry_type_jur_prs", false);
		String natPrsTypeName = commonXML.getfieldTransl("entry_type_nat_prs", false);
		
		StringBuilder html = new StringBuilder();
        // Выходим при пустой строке поиска
        if (isEmpty(findString) && isEmpty(typeId)) {
        	html.append(getNoDataFountTR("5"));            
            return_value.setFunctionHTMLReturnValue(html.toString());
            return return_value;
        }
		
		ArrayList<bcFeautureParam> pParam = initParamArray();

		String mySQL = 
        	" SELECT entry_type_name, entry_name, entry_address, entry_id, entry_type  " +
	  		"   FROM (SELECT ROWNUM rn, entry_type_name, entry_name, entry_address, entry_id, entry_type  " +
	  		"           FROM (SELECT entry_type_name, entry_name, entry_address, entry_id, entry_type" +
	  		"                   FROM (";
		if ("ALL".equalsIgnoreCase(type) || "DOC".equalsIgnoreCase(type) || "JUR_PRS".equalsIgnoreCase(type)) {
  			mySQL = mySQL + 
  				" SELECT '<font color=\"blue\">" + jurPrsTypeName + "</font>' entry_type_name, sname_jur_prs entry_name, " +
				"        adr_full entry_address, id_jur_prs entry_id, 'JUR_PRS' entry_type" +
				"   FROM " + getGeneralDBScheme() + ".vc_jur_prs_name_all " +
				"  WHERE cd_jur_prs_status <> 'SERVICE_PLACE'";
	  		if (!isEmpty(findString)) {
	  			mySQL = mySQL + 
	  				" AND (UPPER(sname_jur_prs) LIKE UPPER('%'||?||'%') OR " +
	  				"      UPPER(adr_full) LIKE UPPER('%'||?||'%')) ";
	  			for (int i=0; i<2; i++) {
	  			    pParam.add(new bcFeautureParam("string", findString));
	  			}
	  		}
		}
  		if ("ALL".equalsIgnoreCase(type) || "DOC".equalsIgnoreCase(type)) {
  			mySQL = mySQL + " UNION ALL ";
		}
		if ("ALL".equalsIgnoreCase(type) || "DOC".equalsIgnoreCase(type) || "NAT_PRS".equalsIgnoreCase(type)) {
  			mySQL = mySQL + 
  				" SELECT '<font color=\"green\">" + natPrsTypeName + "</font>' entry_type_name, full_name entry_name, " +
				"        fact_adr_full entry_address, id_nat_prs entry_id, 'NAT_PRS' entry_type" +
				"                   FROM " + getGeneralDBScheme() + ".vc_nat_prs_all ";
	  		if (!isEmpty(findString)) {
	  			mySQL = mySQL + 
	  				" WHERE (UPPER(full_name) LIKE UPPER('%'||?||'%') OR " +
	  				"        UPPER(fact_adr_full) LIKE UPPER('%'||?||'%')) ";
	  			for (int i=0; i<2; i++) {
	  			    pParam.add(new bcFeautureParam("string", findString));
	  			}
	  		}
		}

  		if ("DOC".equalsIgnoreCase(type) && !isEmpty(typeId)) {
  			mySQL = mySQL + 
	  			") WHERE entry_id IN (SELECT id_doc_party" +
	  			"                      FROM " + getGeneralDBScheme() + ".v_doc_parties_all" +
	  			"                     WHERE id_doc = ?) ";
  	        pParam.add(new bcFeautureParam("int", typeId));
		} else {
			mySQL = mySQL + ")";
		}
        pParam.add(new bcFeautureParam("int", l_end));
        pParam.add(new bcFeautureParam("int", l_beg));
        mySQL = mySQL +
        	"       ) WHERE ROWNUM < ? " + 
  			" ) WHERE rn >= ?";
        
        LOGGER.debug(prepareSQLToLog(mySQL, pParam));
        PreparedStatement st = null;
        String idEntry = "";
        String nameEntry = "";
        String typeEntry = "";
        String markedNameEntry = "";
        
        try{
        	con = Connector.getConnection(mySource.getSessionId());
            st = con.prepareStatement(mySQL);
            st = prepareParam(st, pParam);
            ResultSet rset = st.executeQuery();
            int rowCount = 0;
            while (rset.next()){
            	idEntry = getHTMLValue(rset.getString("ENTRY_ID"));
            	nameEntry = getHTMLValue(rset.getString("ENTRY_NAME"));
            	typeEntry = getHTMLValue(rset.getString("ENTRY_TYPE"));
            	markedNameEntry = prepareFindString(nameEntry, findString);
            	
                if ("NAT_PRS".equalsIgnoreCase(typeEntry)) {
                	html.append(
                        "<td><div class=\"div_find\" onClick=\"changeOpener('"+idEntry+"','','"+nameEntry+"','NAT_PRS')\">"+getValue2(rset.getString("ENTRY_TYPE_NAME"))+"</div></td>" +
                        "<td><div class=\"div_find_marked\" onClick=\"changeOpener('"+idEntry+"','','"+nameEntry+"','NAT_PRS')\">"+getValue2(markedNameEntry)+"</div></td>" +
                        "<td><div class=\"div_find\" onClick=\"changeOpener('"+idEntry+"','','"+nameEntry+"','NAT_PRS')\">"+getValue2(rset.getString("ENTRY_ADDRESS"))+"</div></td></tr>");
                } else {
                	html.append(
                        "<td><div class=\"div_find\" onClick=\"changeOpener('','"+idEntry+"','"+nameEntry+"','JUR_PRS')\">"+getValue2(rset.getString("ENTRY_TYPE_NAME"))+"</div></td>" +
                        "<td><div class=\"div_find_marked\" onClick=\"changeOpener('','"+idEntry+"','"+nameEntry+"','JUR_PRS')\">"+getValue2(markedNameEntry)+"</div></td>" +
                        "<td><div class=\"div_find\" onClick=\"changeOpener('','"+idEntry+"','"+nameEntry+"','JUR_PRS')\">"+getValue2(rset.getString("ENTRY_ADDRESS"))+"</div></td></tr>");
                }
                rowCount++;
            }
            if (rowCount == 0) {
            	html.append(getNoDataFountTR("5"));
            }
        } // try
        catch (SQLException e) {LOGGER.error(html, e); html.append(prepareSQLToLog(mySQL, pParam) + "<br>" + e.toString());}
        catch (Exception el) {LOGGER.error(html, el); html.append(prepareSQLToLog(mySQL, pParam) + "<br>" + el.toString());}
        finally {
            try {
                if (st!=null) {
					st.close();
				}
            } catch (SQLException w) {w.toString();}
            try {
                if (con!=null) {
					con.close();
				}
            } catch (SQLException w) {w.toString();}
            Connector.closeConnection(con);
        } // finally

        html.append("</tbody>");
        html.append("</table>");
        
        return_value.setFunctionHTMLReturnValue(html.toString());
        return return_value;
    } // getJurPrsList

    public transport getSAMList(transport mySource) {
    	
    	bc.service.pages page = new bc.service.pages(mySource.getPageNumber(), mySource.getPageRowCount());
    	String l_beg = page.getFirstRowNumber();
    	String l_end = page.getLastRowNumber();
    	
    	transport return_value=new transport();
		return_value.setFunctionName(mySource.getFunctionName());
		return_value.setIdDestination(mySource.getIdDestination());
		return_value.setFunctionParam(mySource.getFunctionParam());
		
		String findString = mySource.getFunctionParam();
		
		StringBuilder html = new StringBuilder();
		
		ArrayList<bcFeautureParam> pParam = initParamArray();

		String mySQL = 
        	" SELECT id_sam, sam_serial_number, vk_sys_key_sam, name_sam_type, name_sam_status, " +
        	"        date_beg_frmt, expiry_date_frmt, date_end_frmt, id_term, name_user " +
	  		"   FROM (SELECT ROWNUM rn, id_sam, sam_serial_number, vk_sys_key_sam, name_sam_type, name_sam_status, " +
	  		"                date_beg_frmt, expiry_date_frmt, date_end_frmt, id_term, name_user " +
  			"           FROM (SELECT * " +
  			"                   FROM " + getGeneralDBScheme() + ".vc_sam_club_all ";
        if (!isEmpty(findString)) {
        	mySQL = mySQL +
        	"          WHERE (TO_CHAR(id_sam) LIKE UPPER('%'||?||'%') OR " +
  			"                 UPPER(sam_serial_number) LIKE UPPER('%'||?||'%') OR " +
  			"                 UPPER(name_sam_type) LIKE UPPER('%'||?||'%') OR " +
  			"                 UPPER(name_sam_status) LIKE UPPER('%'||?||'%')) ";
        	for (int i=0; i<4; i++) {
        	    pParam.add(new bcFeautureParam("string", findString));
        	}
        }
        pParam.add(new bcFeautureParam("int", l_end));
        pParam.add(new bcFeautureParam("int", l_beg));
        mySQL = mySQL +
        	"                  ORDER BY id_sam" +
			"        ) WHERE ROWNUM < ? " +
  			" ) WHERE rn >= ?";
        
        LOGGER.debug(prepareSQLToLog(mySQL, pParam));
        PreparedStatement st = null;
        String idSAM = "";
        String samSerialNumber = "";
        String nameCardType = "";
        String nameCardStatus = "";
        String markedIdSAM = "";
        String markedSamSerialNumber = "";
        String markedNameCardType = "";
        String markedNmeCardStatus = "";
        
        try{
        	con = Connector.getConnection(mySource.getSessionId());
            st = con.prepareStatement(mySQL);
            st = prepareParam(st, pParam);
            ResultSet rset = st.executeQuery();
            int rowCount = 0;
            while (rset.next()){
            	idSAM = getHTMLValue(rset.getString("ID_SAM"));
            	samSerialNumber = getHTMLValue(rset.getString("SAM_SERIAL_NUMBER"));
            	nameCardType = getHTMLValue(rset.getString("NAME_SAM_TYPE"));
            	nameCardStatus = getHTMLValue(rset.getString("NAME_SAM_STATUS"));
            	markedIdSAM = prepareFindString(idSAM, findString);
            	markedSamSerialNumber = prepareFindString(samSerialNumber, findString);
            	markedNameCardType = prepareFindString(nameCardType, findString);
            	markedNmeCardStatus = prepareFindString(nameCardStatus, findString);

                html.append(
                        "<tr><td><div class=\"div_find_marked\" onClick=\"changeOpener('"+idSAM+"')\">"+getValue2(markedIdSAM) +"</div></td>" +
                        "<td><div class=\"div_find\" onClick=\"changeOpener('"+idSAM+"')\">"+getValue2(markedSamSerialNumber)+"</div></td>" +
                        "<td><div class=\"div_find\" onClick=\"changeOpener('"+idSAM+"')\">"+getValue2(markedNameCardType)+"</div></td>" +
                        "<td><div class=\"div_find\" onClick=\"changeOpener('"+idSAM+"')\">"+getValue2(markedNmeCardStatus)+"</div></td>" +
                        "<td><div class=\"div_find\" onClick=\"changeOpener('"+idSAM+"')\">"+getValue2(rset.getString("DATE_BEG_FRMT"))+"</div></td>" +
                        "<td><div class=\"div_find\" onClick=\"changeOpener('"+idSAM+"')\">"+getValue2(rset.getString("EXPIRY_DATE_FRMT"))+"</div></td>" +
                        "<td><div class=\"div_find\" onClick=\"changeOpener('"+idSAM+"')\">"+getValue2(rset.getString("DATE_END_FRMT"))+"</div></td>" +
                        "<td><div class=\"div_find\" onClick=\"changeOpener('"+idSAM+"')\">"+getValue2(rset.getString("ID_TERM"))+"</div></td>" +
                        "<td><div class=\"div_find\" onClick=\"changeOpener('"+idSAM+"')\">"+getValue2(rset.getString("NAME_USER"))+"</div></td></tr>");
                       
                rowCount++;
            }
            if (rowCount == 0) {
            	html.append(getNoDataFountTR("9"));
            }
        } // try
        catch (SQLException e) {LOGGER.error(html, e);html.append(prepareSQLToLog(mySQL, pParam) + "<br>" + e.toString());}
        catch (Exception el) {LOGGER.error(html, el);html.append(prepareSQLToLog(mySQL, pParam) + "<br>" + el.toString());}
        finally {
            try {
                if (st!=null) {
					st.close();
				}
            } catch (SQLException w) {w.toString();}
            try {
                if (con!=null) {
					con.close();
				}
            } catch (SQLException w) {w.toString();}
            Connector.closeConnection(con);
        } // finally

        html.append("</tbody>");
        html.append("</table>");
        
        return_value.setFunctionHTMLReturnValue(html.toString());
        return return_value;
    } // getSAMList

    public transport getClubCardsList(transport mySource) {
    	
    	bc.service.pages page = new bc.service.pages(mySource.getPageNumber(), mySource.getPageRowCount());
    	String l_beg = page.getFirstRowNumber();
    	String l_end = page.getLastRowNumber();
    	
    	transport return_value=new transport();
		return_value.setFunctionName(mySource.getFunctionName());
		return_value.setIdDestination(mySource.getIdDestination());
		return_value.setFunctionParam(mySource.getFunctionParam());
		
		String findString = mySource.getFunctionParam();
		String codeType = mySource.getFunctionParam2();
		
		StringBuilder html = new StringBuilder();
		
		ArrayList<bcFeautureParam> pParam = initParamArray();

		String mySQL = 
         	"SELECT cd_card1, card_serial_number, full_name name_nat_prs, name_card_status, name_card_type, " +
        	"		name_card_state, date_open_frmt, expiry_date_frmt, nt_icc, " +
        	"		id_issuer, id_payment_system " +
        	"  FROM (SELECT ROWNUM rn, cd_card1, full_name, " +
        	"				name_card_type, name_card_status,  " +
        	"				name_card_state, date_open_frmt, expiry_date_frmt, nt_icc, id_issuer, " +
        	"				id_payment_system, card_serial_number " +
         	"		  FROM (SELECT * " +
         	"                 FROM " + getGeneralDBScheme()+".vc_card_find_all a " +
         	"                WHERE 1=1 ";
        if (!isEmpty(findString)) {
         	mySQL = mySQL +
         		" AND (UPPER(cd_card1) LIKE ('%'||?||'%') " +
         		"    OR UPPER(date_open_frmt) LIKE UPPER('%'||?||'%')" +
         		"    OR UPPER(card_serial_number) LIKE UPPER('%'||?||'%')" +
         		"    OR UPPER(full_name) LIKE UPPER('%'||?||'%'))";
         	for (int i=0; i<4; i++) {
         	    pParam.add(new bcFeautureParam("string", findString));
         	}
         }
        pParam.add(new bcFeautureParam("int", l_end));
        pParam.add(new bcFeautureParam("int", l_beg));
        mySQL = mySQL + ") WHERE ROWNUM < ?)  WHERE rn >= ?";
		
        LOGGER.debug(prepareSQLToLog(mySQL, pParam));
        PreparedStatement st = null;
        String cdCard = "";
        String cardSerialNumber = "";
        String idIssuer = "";
        String idPaymentSystem = "";
        String markedCdCard = "";
        String markedCardSerialNumber = "";
        
        try{
        	con = Connector.getConnection(mySource.getSessionId());
            st = con.prepareStatement(mySQL);
            st = prepareParam(st, pParam);
            ResultSet rset = st.executeQuery();
            int rowCount = 0;
            while (rset.next()){
            	cdCard = getHTMLValue(rset.getString("CD_CARD1"));
            	cardSerialNumber = getHTMLValue(rset.getString("CARD_SERIAL_NUMBER"));
            	idIssuer = getHTMLValue(rset.getString("ID_ISSUER"));
            	idPaymentSystem = getHTMLValue(rset.getString("ID_PAYMENT_SYSTEM"));
                if (cdCard == null) { cdCard = ""; }
                if (cardSerialNumber == null) { cardSerialNumber = ""; }
                if (idIssuer == null) { idIssuer = ""; }
                if (idPaymentSystem == null) { idPaymentSystem = ""; }
                if ("CD_CARD".equalsIgnoreCase(codeType)) {
                	markedCdCard = prepareFindString(cdCard, findString);
                	markedCardSerialNumber = cardSerialNumber;
                } else {
                	markedCdCard = cdCard;
                	markedCardSerialNumber = prepareFindString(cardSerialNumber, findString);
                }
                html.append(
                        "<tr><td><div class=\"div_find_marked\" onClick=\"changeOpener('"+cdCard+"','"+cardSerialNumber+"','"+idIssuer+"','"+idPaymentSystem+"')\">"+getValue2(markedCdCard) +"</div></td>" +
                        "<td><div class=\"div_find\" onClick=\"changeOpener('"+cdCard+"','"+cardSerialNumber+"','"+idIssuer+"','"+idPaymentSystem+"')\">"+getValue2(markedCardSerialNumber)+"</div></td>" +
                        "<td><div class=\"div_find\" onClick=\"changeOpener('"+cdCard+"','"+cardSerialNumber+"','"+idIssuer+"','"+idPaymentSystem+"')\">"+getValue2(rset.getString("NAME_NAT_PRS"))+"</div></td>" +
                        "<td><div class=\"div_find\" onClick=\"changeOpener('"+cdCard+"','"+cardSerialNumber+"','"+idIssuer+"','"+idPaymentSystem+"')\">"+getValue2(rset.getString("NAME_CARD_STATUS"))+"</div></td>" +
                        "<td><div class=\"div_find\" onClick=\"changeOpener('"+cdCard+"','"+cardSerialNumber+"','"+idIssuer+"','"+idPaymentSystem+"')\">"+getValue2(rset.getString("NAME_CARD_TYPE"))+"</div></td>" +
                        "<td><div class=\"div_find\" onClick=\"changeOpener('"+cdCard+"','"+cardSerialNumber+"','"+idIssuer+"','"+idPaymentSystem+"')\">"+getValue2(rset.getString("NAME_CARD_STATE"))+"</div></td>" +
                        "<td><div class=\"div_find\" onClick=\"changeOpener('"+cdCard+"','"+cardSerialNumber+"','"+idIssuer+"','"+idPaymentSystem+"')\">"+getValue2(rset.getString("DATE_OPEN_FRMT"))+"</div></td>" +
                        "<td><div class=\"div_find\" onClick=\"changeOpener('"+cdCard+"','"+cardSerialNumber+"','"+idIssuer+"','"+idPaymentSystem+"')\">"+getValue2(rset.getString("EXPIRY_DATE_FRMT"))+"</div></td>" +
                        "<td><div class=\"div_find\" onClick=\"changeOpener('"+cdCard+"','"+cardSerialNumber+"','"+idIssuer+"','"+idPaymentSystem+"')\">"+getValue2(rset.getString("NT_ICC"))+"</div></td></tr>");
                       
                rowCount++;
            }
            if (rowCount == 0) {
            	html.append(getNoDataFountTR("9"));
            }
        } // try
        catch (SQLException e) {LOGGER.error(html, e);html.append(prepareSQLToLog(mySQL, pParam) + "<br>" + e.toString());}
        catch (Exception el) {LOGGER.error(html, el);html.append(prepareSQLToLog(mySQL, pParam) + "<br>" + el.toString());}
        finally {
            try {
                if (st!=null) {
					st.close();
				}
            } catch (SQLException w) {w.toString();}
            try {
                if (con!=null) {
					con.close();
				}
            } catch (SQLException w) {w.toString();}
            Connector.closeConnection(con);
        } // finally

        html.append("</tbody>");
        html.append("</table>");
        
        return_value.setFunctionHTMLReturnValue(html.toString());
        return return_value;
    } // getClubCardsList

    public transport getCardPackagesList(transport mySource) {
    	
    	bc.service.pages page = new bc.service.pages(mySource.getPageNumber(), mySource.getPageRowCount());
    	String l_beg = page.getFirstRowNumber();
    	String l_end = page.getLastRowNumber();
    	
    	transport return_value=new transport();
		return_value.setFunctionName(mySource.getFunctionName());
		return_value.setIdDestination(mySource.getIdDestination());
		return_value.setFunctionParam(mySource.getFunctionParam());
		
		String findString = mySource.getFunctionParam();
		String id_jur_prs = mySource.getFunctionParam2();
		
		StringBuilder html = new StringBuilder();
		
		ArrayList<bcFeautureParam> pParam = initParamArray();

		String mySQL = 
			"SELECT rn, sname_jur_prs sname_partner, name_card_status card_status, name_jur_prs_card_pack, total_amount_card_pack_frmt, " +
	  		"		id_jur_prs_card_pack " +
	  		"  FROM (SELECT ROWNUM rn, sname_jur_prs, name_jur_prs_card_pack, name_card_status," +
	  		"				total_amount_jp_card_pack_frmt||' '||sname_currency total_amount_card_pack_frmt , " +
	  		"				id_jur_prs_card_pack " +
	  		"          FROM (SELECT sname_jur_prs, name_jur_prs_card_pack, name_card_status, sname_currency, entrance_fee_frmt, " +
	  		"						membership_fee_frmt, share_fee_frmt, membership_fee_month_count, " +
	  		"						dealer_margin_frmt, agent_margin_frmt, total_amount_jp_card_pack_frmt," +
	  		"                       action_date_beg_frmt, action_date_end_frmt, id_jur_prs_card_pack, " +
	  		"                       id_card_status, id_club " +
            "				   FROM " + getGeneralDBScheme() + ".vc_club_jp_card_pack_club_all " +
         	"                WHERE 1=1 ";
        if (!isEmpty(findString)) {
        	mySQL = mySQL + 
				" AND (UPPER(sname_jur_prs) LIKE UPPER('%'||?||'%') OR " +
				"	   UPPER(name_card_status) LIKE UPPER('%'||?||'%') OR " +
				"	   UPPER(name_jur_prs_card_pack) LIKE UPPER('%'||?||'%') OR " +
				"	   UPPER(total_amount_card_pack_frmt) LIKE UPPER('%'||?||'%')) ";
         	for (int i=0; i<4; i++) {
         	    pParam.add(new bcFeautureParam("string", findString));
         	}
        }
        if (!isEmpty(id_jur_prs)) {
        	mySQL = mySQL + " AND id_jur_prs = ? ";
            pParam.add(new bcFeautureParam("int", id_jur_prs));
        }
        pParam.add(new bcFeautureParam("int", l_end));
        pParam.add(new bcFeautureParam("int", l_beg));
        mySQL = mySQL + ") WHERE ROWNUM < ?)  WHERE rn >= ?";

        LOGGER.debug(prepareSQLToLog(mySQL, pParam));
        PreparedStatement st = null;
        String idPack = "";
        String namePack = "";
        String markedNamePack = "";
        
        try{
        	con = Connector.getConnection(mySource.getSessionId());
            st = con.prepareStatement(mySQL);
            st = prepareParam(st, pParam);
            ResultSet rset = st.executeQuery();
            int rowCount = 0;
            while (rset.next()){
            	idPack = getHTMLValue(rset.getString("ID_JUR_PRS_CARD_PACK"));
            	namePack = getHTMLValue(rset.getString("NAME_JUR_PRS_CARD_PACK"));
            	markedNamePack = prepareFindString(namePack, findString);
            	
                html.append(
                        "<tr><td><div class=\"div_find\" onClick=\"changeOpener('"+idPack+"','"+namePack+"')\">"+getValue2(rset.getString("SNAME_PARTNER")) +"</div></td>" +
                        "<td><div class=\"div_find\" onClick=\"changeOpener('"+idPack+"','"+namePack+"')\">"+getValue2(rset.getString("card_status"))+"</div></td>" +
                        "<td><div class=\"div_find_marked\" onClick=\"changeOpener('"+idPack+"','"+namePack+"')\">"+getValue2(markedNamePack)+"</div></td>" +
                        "<td><div class=\"div_find\" onClick=\"changeOpener('"+idPack+"','"+namePack+"')\">"+getValue2(rset.getString("TOTAL_AMOUNT_CARD_PACK_FRMT"))+"</div></td></tr>");
                
                rowCount++;
            }
            if (rowCount == 0) {
            	html.append(getNoDataFountTR("9"));
            }
        } // try
        catch (SQLException e) {LOGGER.error(html, e);html.append(prepareSQLToLog(mySQL, pParam) + "<br>" + e.toString());}
        catch (Exception el) {LOGGER.error(html, el);html.append(prepareSQLToLog(mySQL, pParam) + "<br>" + el.toString());}
        finally {
            try {
                if (st!=null) {
					st.close();
				}
            } catch (SQLException w) {w.toString();}
            try {
                if (con!=null) {
					con.close();
				}
            } catch (SQLException w) {w.toString();}
            Connector.closeConnection(con);
        } // finally

        html.append("</tbody>");
        html.append("</table>");
        
        return_value.setFunctionHTMLReturnValue(html.toString());
        return return_value;
    } // getClubCardsList

    public transport getClubCardsAndNatPrsList(transport mySource) {
    	
    	bc.service.pages page = new bc.service.pages(mySource.getPageNumber(), mySource.getPageRowCount());
    	String l_beg = page.getFirstRowNumber();
    	String l_end = page.getLastRowNumber();
    	
    	transport return_value=new transport();
		return_value.setFunctionName(mySource.getFunctionName());
		return_value.setIdDestination(mySource.getIdDestination());
		return_value.setFunctionParam(mySource.getFunctionParam());
		
		String findString = mySource.getFunctionParam();
		String findType = mySource.getFunctionParam2();
		
		StringBuilder html = new StringBuilder();
		
		ArrayList<bcFeautureParam> pParam = initParamArray();

		String mySQL = 
        	" SELECT cd_card1, name_nat_prs, name_card_type, name_card_status, " +
        	"        name_card_state, date_open_frmt, expiry_date_frmt, nt_icc, id_issuer, " +
        	"		 id_payment_system, card_serial_number, id_nat_prs " +
        	"  FROM (SELECT ROWNUM rn, cd_card1, name_nat_prs, name_card_type, name_card_status, " +
        	"               name_card_state, date_open_frmt, expiry_date_frmt, nt_icc, id_issuer, " +
        	"	   	        id_payment_system, card_serial_number, id_nat_prs " +
        	"         FROM (SELECT c.cd_card1, " +
        	"			  	       CASE WHEN n.surname IS NULL THEN NULL ELSE (n.surname || ' ' || n.NAME || ' ' || n.patronymic) END name_nat_prs, " +
        	"				       c.name_card_type, c.name_card_status, " +
        	"				       c.name_card_state, c.date_open_frmt, c.expiry_date_frmt, c.nt_icc, c.id_issuer, " +
        	"				       c.id_payment_system, c.card_serial_number, c.id_nat_prs " +
        	"		          FROM (SELECT a.id_nat_prs, a.cd_card1, a.name_card_type, " +
        	"					           a.name_card_status, a.name_card_state, a.date_open_frmt, " +
        	"                              a.expiry_date_frmt, a.nt_icc, a.id_issuer, a.id_payment_system, " +
        	"                              a.card_serial_number " +
        	"				          FROM " + getGeneralDBScheme() + ".vc_card_priv_all a";
        if ("CARD".equalsIgnoreCase(findType)) {
        	mySQL = mySQL + " WHERE (a.cd_card1 LIKE '%'||?||'%') ";
        	pParam.add(new bcFeautureParam("string", findString));
        }
        mySQL = mySQL +
        	"                       ) c ";
        
        if ("CARD".equalsIgnoreCase(findType)) {
            mySQL = mySQL + " LEFT JOIN ";
        } else if ("NAT_PRS".equalsIgnoreCase(findType)) {
        	mySQL = mySQL + " RIGHT JOIN ";
        }
        mySQL = mySQL +
           	"                       (SELECT id_nat_prs, surname, NAME, patronymic " +
            "					       FROM " + getGeneralDBScheme() + ".vc_nat_prs_club_all";
        if ("NAT_PRS".equalsIgnoreCase(findType)) {
        	mySQL = mySQL + " WHERE (   UPPER(surname||' '||name||' '||patronymic) LIKE UPPER('%'||?||'%')" +
        			//"				   OR UPPER(surname) LIKE UPPER('%'||?||'%')" +
          			//"                OR UPPER(name) LIKE UPPER('%'||?||'%')" +
          			//"                OR UPPER(patronymic) LIKE UPPER('%'||?||'%')" +
          			"               ) ";
        	pParam.add(new bcFeautureParam("string", findString));
        }
        mySQL = mySQL +
            "				       ) n " +
            "				       ON (c.id_nat_prs = n.id_nat_prs) ";
        pParam.add(new bcFeautureParam("int", l_end));
        pParam.add(new bcFeautureParam("int", l_beg));
        mySQL = mySQL +
        	"               ORDER BY cd_card1)" +
        	"          WHERE ROWNUM < ? " + 
  			"        ) " +
  			"  WHERE rn >= ?";
        
        LOGGER.debug(prepareSQLToLog(mySQL, pParam));
        PreparedStatement st = null;
        String cdCard = "";
        String cardSerialNumber = "";
        String idIssuer = "";
        String idPaymentSystem = "";
        String markedCdCard = "";
        String idNatPrs = "";
        String fullNameNatPrs = "";
        String markedFullNameNatPrs = "";
        
        try{
        	con = Connector.getConnection(mySource.getSessionId());
            st = con.prepareStatement(mySQL);
            st = prepareParam(st, pParam);
            ResultSet rset = st.executeQuery();
            int rowCount = 0;
            while (rset.next()){
            	cdCard = getHTMLValue(rset.getString("CD_CARD1"));
            	cardSerialNumber = getHTMLValue(rset.getString("CARD_SERIAL_NUMBER"));
            	idIssuer = getHTMLValue(rset.getString("ID_ISSUER"));
            	idPaymentSystem = getHTMLValue(rset.getString("ID_PAYMENT_SYSTEM"));
            	idNatPrs = getHTMLValue(rset.getString("ID_NAT_PRS"));
            	fullNameNatPrs = getHTMLValue(rset.getString("NAME_NAT_PRS"));
                if (cdCard == null) { cdCard = ""; }
                if (cardSerialNumber == null) { cardSerialNumber = ""; }
                if (idIssuer == null) { idIssuer = ""; }
                if (idPaymentSystem == null) { idPaymentSystem = ""; }
                if (idNatPrs == null) { idNatPrs = ""; }
                if (fullNameNatPrs == null) { fullNameNatPrs = ""; }
                if ("CARD".equalsIgnoreCase(findType)) {
                	markedCdCard = cdCard.toUpperCase().replaceAll(findString.toUpperCase(), "<b>"+findString.toUpperCase()+"</b>");
                	markedFullNameNatPrs = fullNameNatPrs;
                } else {
                	markedFullNameNatPrs = fullNameNatPrs.toUpperCase().replaceAll(findString.toUpperCase(), "<b>"+findString.toUpperCase()+"</b>");
                	markedCdCard = cdCard;
                }
                
                html.append(
                        "<tr><td><div class=\"div_find\" onClick=\"changeOpener('"+cdCard+"','"+cardSerialNumber+"','"+idIssuer+"','"+idPaymentSystem+"','"+idNatPrs+"','"+fullNameNatPrs+"')\">"+getValue2(cardSerialNumber) +"</div></td>" +
                        "<td><div class=\"div_find_marked\" onClick=\"changeOpener('"+cdCard+"','"+cardSerialNumber+"','"+idIssuer+"','"+idPaymentSystem+"','"+idNatPrs+"','"+fullNameNatPrs+"')\">"+getValue2(markedCdCard)+"</div></td>" +
                        "<td><div class=\"div_find_marked\" onClick=\"changeOpener('"+cdCard+"','"+cardSerialNumber+"','"+idIssuer+"','"+idPaymentSystem+"','"+idNatPrs+"','"+fullNameNatPrs+"')\">"+getValue2(markedFullNameNatPrs)+"</div></td>" +
                        "<td><div class=\"div_find\" onClick=\"changeOpener('"+cdCard+"','"+cardSerialNumber+"','"+idIssuer+"','"+idPaymentSystem+"','"+idNatPrs+"','"+fullNameNatPrs+"')\">"+getValue2(rset.getString("NAME_CARD_TYPE"))+"</div></td>" +
                        "<td><div class=\"div_find\" onClick=\"changeOpener('"+cdCard+"','"+cardSerialNumber+"','"+idIssuer+"','"+idPaymentSystem+"','"+idNatPrs+"','"+fullNameNatPrs+"')\">"+getValue2(rset.getString("NAME_CARD_STATUS"))+"</div></td>" +
                        "<td><div class=\"div_find\" onClick=\"changeOpener('"+cdCard+"','"+cardSerialNumber+"','"+idIssuer+"','"+idPaymentSystem+"','"+idNatPrs+"','"+fullNameNatPrs+"')\">"+getValue2(rset.getString("NAME_CARD_STATE"))+"</div></td>" +
                        "<td><div class=\"div_find\" onClick=\"changeOpener('"+cdCard+"','"+cardSerialNumber+"','"+idIssuer+"','"+idPaymentSystem+"','"+idNatPrs+"','"+fullNameNatPrs+"')\">"+getValue2(rset.getString("DATE_OPEN_FRMT"))+"</div></td>" +
                        "<td><div class=\"div_find\" onClick=\"changeOpener('"+cdCard+"','"+cardSerialNumber+"','"+idIssuer+"','"+idPaymentSystem+"','"+idNatPrs+"','"+fullNameNatPrs+"')\">"+getValue2(rset.getString("EXPIRY_DATE_FRMT"))+"</div></td>" +
                        "<td><div class=\"div_find\" onClick=\"changeOpener('"+cdCard+"','"+cardSerialNumber+"','"+idIssuer+"','"+idPaymentSystem+"','"+idNatPrs+"','"+fullNameNatPrs+"')\">"+getValue2(rset.getString("NT_ICC"))+"</div></td></tr>");
                       
                rowCount++;
            }
            if (rowCount == 0) {
            	html.append(getNoDataFountTR("9"));
            }
        } // try
        catch (SQLException e) {LOGGER.error(html, e);html.append(prepareSQLToLog(mySQL, pParam) + "<br>" + e.toString());}
        catch (Exception el) {LOGGER.error(html, el);html.append(prepareSQLToLog(mySQL, pParam) + "<br>" + el.toString());}
        finally {
            try {
                if (st!=null) {
					st.close();
				}
            } catch (SQLException w) {w.toString();}
            try {
                if (con!=null) {
					con.close();
				}
            } catch (SQLException w) {w.toString();}
            Connector.closeConnection(con);
        } // finally

        html.append("</tbody>");
        html.append("</table>");
        
        return_value.setFunctionHTMLReturnValue(html.toString());
        return return_value;
    } // getClubCardsList

    public transport getServicePlaceList(transport mySource) {
    	
    	bc.service.pages page = new bc.service.pages(mySource.getPageNumber(), mySource.getPageRowCount());
    	String l_beg = page.getFirstRowNumber();
    	String l_end = page.getLastRowNumber();
    	
    	transport return_value=new transport();
		return_value.setFunctionName(mySource.getFunctionName());
		return_value.setIdDestination(mySource.getIdDestination());
		return_value.setFunctionParam(mySource.getFunctionParam());
		
		String findString = mySource.getFunctionParam();
		String idJurPrs = mySource.getFunctionParam2();
		//String idTerm = mySource.getFunctionParam3();
		
		StringBuilder html = new StringBuilder();
		
		ArrayList<bcFeautureParam> pParam = initParamArray();

		String mySQL = 
        	" SELECT jur_prs, name_service_place, adr_full, id_service_place " +
	  		"   FROM (SELECT ROWNUM rn, id_jur_prs id_service_place, sname_jur_prs_parent jur_prs, sname_jur_prs name_service_place, " +
	  		"                fact_adr_full adr_full " +
  			"           FROM (SELECT * " +
  			"                   FROM " + getGeneralDBScheme() + ".vc_jur_prs_club_all " + 
  			"                  WHERE cd_jur_prs_status = 'SERVICE_PLACE' ";
        if (!isEmpty(findString)) {
        	mySQL = mySQL +
        		"     AND (TO_CHAR(id_jur_prs) LIKE UPPER('%'||?||'%') OR " +
        		"          UPPER(sname_jur_prs_parent) LIKE UPPER('%'||?||'%') OR " +
        		"          UPPER(sname_jur_prs) LIKE UPPER('%'||?||'%') OR " +
        		"          UPPER(fact_adr_full) LIKE UPPER('%'||?||'%')) ";
        	for (int i=0; i<4; i++) {
        	    pParam.add(new bcFeautureParam("string", findString));
        	}
        }
        if (!isEmpty(idJurPrs)) {
        	mySQL = mySQL + " AND id_jur_prs_parent = ? ";
        	pParam.add(new bcFeautureParam("int", idJurPrs));
        }
        mySQL = mySQL +
  			"                  ORDER BY sname_jur_prs_parent, sname_jur_prs )" +
        	"          WHERE ROWNUM < ? " + 
  			"        ) " +
  			"  WHERE rn >= ?";
        pParam.add(new bcFeautureParam("int", l_end));
        pParam.add(new bcFeautureParam("int", l_beg));
        
        LOGGER.debug(prepareSQLToLog(mySQL, pParam));
        PreparedStatement st = null;
        String idServicePlace = "";
        String nameServicePlace = "";
        String markedNameServicePlace = "";
        
        try{
        	con = Connector.getConnection(mySource.getSessionId());
            st = con.prepareStatement(mySQL);
            st = prepareParam(st, pParam);
            ResultSet rset = st.executeQuery();
            int rowCount = 0;
            while (rset.next()){
            	idServicePlace = getHTMLValue(rset.getString("ID_SERVICE_PLACE"));
            	nameServicePlace = getHTMLValue(rset.getString("NAME_SERVICE_PLACE"));
                if (idServicePlace == null) { idServicePlace = ""; }
                if (nameServicePlace == null) { nameServicePlace = ""; }
                markedNameServicePlace = prepareFindString(nameServicePlace, findString);
                
                html.append(
                        "<tr><td><div class=\"div_find_marked\" onClick=\"changeOpener('"+idServicePlace+"','"+nameServicePlace+"')\">"+getValue2(markedNameServicePlace)+"</div></td>" +
                        "<td><div class=\"div_find\" onClick=\"changeOpener('"+idServicePlace+"','"+nameServicePlace+"')\">"+getValue2(rset.getString("JUR_PRS"))+"</div></td>" +
                        "<td><div class=\"div_find\" onClick=\"changeOpener('"+idServicePlace+"','"+nameServicePlace+"')\">"+getValue2(rset.getString("ADR_FULL"))+"</div></td></tr>");
                       
                rowCount++;
            }
            if (rowCount == 0) {
            	html.append(getNoDataFountTR("5"));
            }
        } // try
        catch (SQLException e) {LOGGER.error(html, e);html.append(prepareSQLToLog(mySQL, pParam) + "<br>" + e.toString());}
        catch (Exception el) {LOGGER.error(html, el);html.append(prepareSQLToLog(mySQL, pParam) + "<br>" + el.toString());}
        finally {
            try {
                if (st!=null) {
					st.close();
				}
            } catch (SQLException w) {w.toString();}
            try {
                if (con!=null) {
					con.close();
				}
            } catch (SQLException w) {w.toString();}
            Connector.closeConnection(con);
        } // finally

        html.append("</tbody>");
        html.append("</table>");
        
        return_value.setFunctionHTMLReturnValue(html.toString());
        return return_value;
    } // getServicePlaceList

    public transport getBankAccountsList(transport mySource) {
    	
    	bc.service.pages page = new bc.service.pages(mySource.getPageNumber(), mySource.getPageRowCount());
    	String l_beg = page.getFirstRowNumber();
    	String l_end = page.getLastRowNumber();
    	
    	transport return_value=new transport();
		return_value.setFunctionName(mySource.getFunctionName());
		return_value.setIdDestination(mySource.getIdDestination());
		return_value.setFunctionParam(mySource.getFunctionParam());
		return_value.setFunctionParam2(mySource.getFunctionParam2());
		
		String findString = mySource.getFunctionParam();
		String id_jur_prs = mySource.getFunctionParam2();
		
		StringBuilder html = new StringBuilder();
		
		ArrayList<bcFeautureParam> pParam = initParamArray();

		String mySQL = 
        	" SELECT id_bank_account, name_bank_account_type, number_bank_account, " +
			"		 sname_bank name_bank_alt, sname_owner_bank_account, name_currency, " +
			"        number_bank_account||' - '||name_bank_account_type id_number_bank_account, " +
			"        cd_currency, id_bank, id_owner_bank_account " +
	  		"   FROM (SELECT ROWNUM rn, id_bank_account, name_bank_account_type, number_bank_account, " +
	  		"                id_bank, sname_bank, id_owner_bank_account, sname_owner_bank_account, " +
	  		"                cd_currency, name_currency " +
  			"           FROM (SELECT * " +
  			"                   FROM " + getGeneralDBScheme() + ".vc_bank_account_all " +
  			"                  WHERE 1=1 ";
        if (!isEmpty(id_jur_prs)) {
        	mySQL = mySQL + " AND id_owner_bank_account = ? ";
        	pParam.add(new bcFeautureParam("int", id_jur_prs));
        }
        if (!isEmpty(findString)) {
        	mySQL = mySQL +
  			"            AND (TO_CHAR(id_bank_account) LIKE '%'||?||'%' OR " +
  			"                 UPPER(number_bank_account) LIKE UPPER('%'||?||'%')) ";
        	for (int i=0; i<2; i++) {
        	    pParam.add(new bcFeautureParam("string", findString));
        	}
        }

        pParam.add(new bcFeautureParam("int", l_end));
        pParam.add(new bcFeautureParam("int", l_beg));
        mySQL = mySQL +
        	"          		  ORDER BY id_bank_account" +
			"        ) WHERE ROWNUM < ?" + 
  			" ) WHERE rn >= ?";
        
        LOGGER.debug(prepareSQLToLog(mySQL, pParam));
        PreparedStatement st = null;
        String idBankAccount = "";
        String idNumberBankAccount = "";
        String numberBankAccount = "";
        String markedIdBankAccount = "";
        String markedNumberBankAccount = "";
        String cdCurrency = "";
        String nameCurrency = "";
        String idBank = "";
        String nameBank = "";
        String idOwner = "";
        String nameOwner = "";
        
        try{
        	con = Connector.getConnection(mySource.getSessionId());
            st = con.prepareStatement(mySQL);
            st = prepareParam(st, pParam);
            ResultSet rset = st.executeQuery();
            int rowCount = 0;
            while (rset.next()){
            	idBankAccount = getHTMLValue(rset.getString("ID_BANK_ACCOUNT"));
            	idNumberBankAccount = getHTMLValue(rset.getString("ID_NUMBER_BANK_ACCOUNT"));
            	numberBankAccount = getHTMLValue(rset.getString("NUMBER_BANK_ACCOUNT"));
            	cdCurrency = getHTMLValue(rset.getString("CD_CURRENCY"));
            	nameCurrency = getHTMLValue(rset.getString("NAME_CURRENCY"));
            	idBank = getHTMLValue(rset.getString("ID_BANK"));
            	nameBank = getHTMLValue(rset.getString("NAME_BANK_ALT"));
            	idOwner = getHTMLValue(rset.getString("ID_OWNER_BANK_ACCOUNT"));
            	nameOwner = getHTMLValue(rset.getString("SNAME_OWNER_BANK_ACCOUNT"));
            	markedIdBankAccount = prepareFindString(idBankAccount, findString);
            	markedNumberBankAccount = prepareFindString(numberBankAccount, findString);
            	
                html.append(
                        "<tr><td><div class=\"div_find\" onClick=\"changeOpener('"+idBankAccount+"','"+idNumberBankAccount+"','"+cdCurrency+"','"+nameCurrency+"','"+idBank+"','"+nameBank+"','"+idOwner+"','"+nameOwner+"')\">"+getValue2(markedIdBankAccount) +"</div></td>" +
                        "<td><div class=\"div_find\" onClick=\"changeOpener('"+idBankAccount+"','"+idNumberBankAccount+"','"+cdCurrency+"','"+nameCurrency+"','"+idBank+"','"+nameBank+"','"+idOwner+"','"+nameOwner+"')\">"+getValue2(rset.getString("NAME_BANK_ACCOUNT_TYPE"))+"</div></td>" +
                        "<td><div class=\"div_find_marked\" onClick=\"changeOpener('"+idBankAccount+"','"+idNumberBankAccount+"','"+cdCurrency+"','"+nameCurrency+"','"+idBank+"','"+nameBank+"','"+idOwner+"','"+nameOwner+"')\">"+getValue2(markedNumberBankAccount)+"</div></td>" +
                        "<td><div class=\"div_find\" onClick=\"changeOpener('"+idBankAccount+"','"+idNumberBankAccount+"','"+cdCurrency+"','"+nameCurrency+"','"+idBank+"','"+nameBank+"','"+idOwner+"','"+nameOwner+"')\">"+getValue2(rset.getString("NAME_BANK_ALT"))+"</div></td>" +
                        "<td><div class=\"div_find\" onClick=\"changeOpener('"+idBankAccount+"','"+idNumberBankAccount+"','"+cdCurrency+"','"+nameCurrency+"','"+idBank+"','"+nameBank+"','"+idOwner+"','"+nameOwner+"')\">"+getValue2(rset.getString("SNAME_OWNER_BANK_ACCOUNT"))+"</div></td>" +
                        "<td><div class=\"div_find\" onClick=\"changeOpener('"+idBankAccount+"','"+idNumberBankAccount+"','"+cdCurrency+"','"+nameCurrency+"','"+idBank+"','"+nameBank+"','"+idOwner+"','"+nameOwner+"')\">"+getValue2(rset.getString("NAME_CURRENCY"))+"</div></td></tr>");
                       
                rowCount++;
            }
            if (rowCount == 0) {
            	html.append(getNoDataFountTR("8"));
            }
        } // try
        catch (SQLException e) {LOGGER.error(html, e);html.append(prepareSQLToLog(mySQL, pParam) + "<br>" + e.toString());}
        catch (Exception el) {LOGGER.error(html, el);html.append(prepareSQLToLog(mySQL, pParam) + "<br>" + el.toString());}
        finally {
            try {
                if (st!=null) {
					st.close();
				}
            } catch (SQLException w) {w.toString();}
            try {
                if (con!=null) {
					con.close();
				}
            } catch (SQLException w) {w.toString();}
            Connector.closeConnection(con);
        } // finally

        html.append("</tbody>");
        html.append("</table>");
        
        return_value.setFunctionHTMLReturnValue(html.toString());
        return return_value;
    } // getBankAccountsList

    public transport getReferralSchemeList(transport mySource) {
    	
    	bc.service.pages page = new bc.service.pages(mySource.getPageNumber(), mySource.getPageRowCount());
    	String l_beg = page.getFirstRowNumber();
    	String l_end = page.getLastRowNumber();
    	
    	transport return_value=new transport();
		return_value.setFunctionName(mySource.getFunctionName());
		return_value.setIdDestination(mySource.getIdDestination());
		return_value.setFunctionParam(mySource.getFunctionParam());
		return_value.setFunctionParam2(mySource.getFunctionParam2());
		
		String findString = mySource.getFunctionParam();
		String id_jur_prs = mySource.getFunctionParam2();
		
		StringBuilder html = new StringBuilder();
		
		ArrayList<bcFeautureParam> pParam = initParamArray();

		String mySQL = 
			" SELECT rn, " +
        	"        /*DECODE(cd_referral_scheme_type," +
        	"               'TARGET_PROGRAM', '<font color=\"blue\"><b>'||name_referral_scheme_type||'<b></font>', " +
            "               'PAYMENT', '<font color=\"green\"><b>'||name_referral_scheme_type||'<b></font>', " +
            "               name_referral_scheme_type" +
        	"        ) name_referral_scheme_type,*/ " +
        	"        DECODE(cd_referral_scheme_type," +
        	"               'TARGET_PROGRAM', '<font color=\"blue\"><b>'||name_target_prg||'<b></font>', " +
            "               'PAYMENT', '<font color=\"green\"><b>'||sname_jur_prs||'<b></font>', " +
            "               ''" +
        	"        ) jur_prs_and_target_prg, name_referral_scheme, desc_referral_scheme, " +
        	"        DECODE(cd_referral_scheme_calc_type," +
        	"               'POINT_SUM', '<font color=\"blue\"><b>'||name_referral_scheme_calc_type||'<b></font>', " +
            "               'CLUB_SUM', '<font color=\"green\"><b>'||name_referral_scheme_calc_type||'<b></font>', " +
            "               name_referral_scheme_calc_type" +
        	"        ) name_referral_scheme_calc_type, " +
        	"		 accounting_level_count, accounting_percent_all_frmt, " +
        	"        /*club_name, date_beg_frmt, date_end_frmt,*/ id_referral_scheme " +
        	"   FROM (SELECT ROWNUM rn, sname_jur_prs, name_target_prg, name_referral_scheme, desc_referral_scheme, cd_referral_scheme_type, name_referral_scheme_type, " +
        	"		         cd_referral_scheme_calc_type, name_referral_scheme_calc_type, " +
        	"                accounting_level_count, accounting_percent_all_frmt, " +
        	"                sname_club club_name, date_beg_frmt, date_end_frmt, id_referral_scheme " +
        	"   	    FROM (SELECT * " +
        	"                   FROM " + getGeneralDBScheme()+".vc_referral_scheme_club_all " +
  			"                  WHERE 1=1 ";
        if (!isEmpty(id_jur_prs)) {
        	mySQL = mySQL + " AND id_jur_prs = ? ";
        	pParam.add(new bcFeautureParam("int", id_jur_prs));
        }
        if (!isEmpty(findString)) {
    		mySQL = mySQL + 
    			" AND (UPPER(sname_jur_prs) LIKE UPPER('%'||?||'%') OR " +
    			"	   UPPER(name_referral_scheme) LIKE UPPER('%'||?||'%') OR " +
    			"	   UPPER(desc_referral_scheme) LIKE UPPER('%'||?||'%')) ";
    		for (int i=0; i<3; i++) {
    		    pParam.add(new bcFeautureParam("string", findString));
    		}
    	}

        pParam.add(new bcFeautureParam("int", l_end));
        pParam.add(new bcFeautureParam("int", l_beg));
        mySQL = mySQL +
        	"          		  ORDER BY name_referral_scheme" +
			"        ) WHERE ROWNUM < ?" + 
  			" ) WHERE rn >= ?";
        
        LOGGER.debug(prepareSQLToLog(mySQL, pParam));
        PreparedStatement st = null;
        String idReferralScheme = "";
        String nameReferralScheme = "";
        String markedNameReferralScheme = "";
        
        try{
        	con = Connector.getConnection(mySource.getSessionId());
            st = con.prepareStatement(mySQL);
            st = prepareParam(st, pParam);
            ResultSet rset = st.executeQuery();
            int rowCount = 0;
            while (rset.next()){
            	idReferralScheme = getHTMLValue(rset.getString("ID_REFERRAL_SCHEME"));
            	nameReferralScheme = getHTMLValue(rset.getString("NAME_REFERRAL_SCHEME"));
            	markedNameReferralScheme = prepareFindString(nameReferralScheme, findString);
            	
                html.append(
                        "<tr><td><div class=\"div_find\" onClick=\"changeOpener('"+idReferralScheme+"','"+nameReferralScheme+"')\">"+getValue2(rset.getString("JUR_PRS_AND_TARGET_PRG")) +"</div></td>" +
                        "<td><div class=\"div_find_marked\" onClick=\"changeOpener('"+idReferralScheme+"','"+nameReferralScheme+"')\">"+getValue2(markedNameReferralScheme)+"</div></td>" +
                        "<td><div class=\"div_find\" onClick=\"changeOpener('"+idReferralScheme+"','"+nameReferralScheme+"')\">"+getValue2(rset.getString("NAME_REFERRAL_SCHEME_CALC_TYPE"))+"</div></td>" +
                        "<td><div class=\"div_find\" onClick=\"changeOpener('"+idReferralScheme+"','"+nameReferralScheme+"')\">"+getValue2(rset.getString("ACCOUNTING_LEVEL_COUNT"))+"</div></td>" +
                        "<td><div class=\"div_find\" onClick=\"changeOpener('"+idReferralScheme+"','"+nameReferralScheme+"')\">"+getValue2(rset.getString("ACCOUNTING_PERCENT_ALL_FRMT"))+"</div></td></tr>");
                       
                rowCount++;
            }
            if (rowCount == 0) {
            	html.append(getNoDataFountTR("8"));
            }
        } // try
        catch (SQLException e) {LOGGER.error(html, e);html.append(prepareSQLToLog(mySQL, pParam) + "<br>" + e.toString());}
        catch (Exception el) {LOGGER.error(html, el);html.append(prepareSQLToLog(mySQL, pParam) + "<br>" + el.toString());}
        finally {
            try {
                if (st!=null) {
					st.close();
				}
            } catch (SQLException w) {w.toString();}
            try {
                if (con!=null) {
					con.close();
				}
            } catch (SQLException w) {w.toString();}
            Connector.closeConnection(con);
        } // finally

        html.append("</tbody>");
        html.append("</table>");
        
        return_value.setFunctionHTMLReturnValue(html.toString());
        return return_value;
    } // getBankAccountsList

    public transport getClubAction(transport mySource) {
    	
    	bc.service.pages page = new bc.service.pages(mySource.getPageNumber(), mySource.getPageRowCount());
    	String l_beg = page.getFirstRowNumber();
    	String l_end = page.getLastRowNumber();
    	
    	transport return_value=new transport();
		return_value.setFunctionName(mySource.getFunctionName());
		return_value.setIdDestination(mySource.getIdDestination());
		return_value.setFunctionParam(mySource.getFunctionParam());
		return_value.setFunctionParam2(mySource.getFunctionParam2());
		
		String findString = mySource.getFunctionParam();
		
		StringBuilder html = new StringBuilder();
		
		ArrayList<bcFeautureParam> pParam = initParamArray();

		String mySQL = 
        	" SELECT id_club_event, name_club_event_type, name_club_event, desc_action_club, " +
          	"        date_beg_frmt, date_end_frmt " +
	  		"   FROM (SELECT ROWNUM rn, id_club_event, name_club_event_type, name_club_event, desc_action_club, " +
          	"                date_beg_frmt, date_end_frmt " +
  			"           FROM (SELECT * " +
  			"                   FROM " + getGeneralDBScheme() + ".vc_club_event_club_all " +
  			"                  WHERE 1=1 ";
        if (!isEmpty(findString)) {
        	mySQL = mySQL +" AND (UPPER(name_club_event) LIKE UPPER('%'||?||'%')) ";
        	pParam.add(new bcFeautureParam("string", findString));
        }

        pParam.add(new bcFeautureParam("int", l_end));
        pParam.add(new bcFeautureParam("int", l_beg));
        mySQL = mySQL +
        	"          		  ORDER BY name_club_event" +
			"        ) WHERE ROWNUM < ?" + 
  			" ) WHERE rn >= ?";
        
        LOGGER.debug(prepareSQLToLog(mySQL, pParam));
        PreparedStatement st = null;
        String idClubAction = "";
        String nameClubAction = "";
        String markedNameClubAction = "";
        
        try{
        	con = Connector.getConnection(mySource.getSessionId());
            st = con.prepareStatement(mySQL);
            st = prepareParam(st, pParam);
            ResultSet rset = st.executeQuery();
            int rowCount = 0;
            while (rset.next()){
            	idClubAction = getHTMLValue(rset.getString("ID_CLUB_EVENT"));
            	nameClubAction = getHTMLValue(rset.getString("NAME_CLUB_EVENT"));
            	markedNameClubAction = prepareFindString(nameClubAction, findString);
            	
                html.append(
                        "<tr><td><div class=\"div_find\" onClick=\"changeOpener('"+idClubAction+"','"+nameClubAction+"')\">"+getValue2(idClubAction) +"</div></td>" +
                        "<td><div class=\"div_find\" onClick=\"changeOpener('"+idClubAction+"','"+nameClubAction+"')\">"+getValue2(rset.getString("NAME_CLUB_EVENT_TYPE"))+"</div></td>" +
                        "<td><div class=\"div_find_marked\" onClick=\"changeOpener('"+idClubAction+"','"+nameClubAction+"')\">"+getValue2(markedNameClubAction)+"</div></td>" +
                        "<td><div class=\"div_find\" onClick=\"changeOpener('"+idClubAction+"','"+nameClubAction+"')\">"+getValue2(rset.getString("DESC_ACTION_CLUB"))+"</div></td>" +
                        "<td><div class=\"div_find\" onClick=\"changeOpener('"+idClubAction+"','"+nameClubAction+"')\">"+getValue2(rset.getString("DATE_BEG_FRMT"))+"</div></td>" +
                        "<td><div class=\"div_find\" onClick=\"changeOpener('"+idClubAction+"','"+nameClubAction+"')\">"+getValue2(rset.getString("DATE_END_FRMT"))+"</div></td></tr>");
                       
                rowCount++;
            }
            if (rowCount == 0) {
            	html.append(getNoDataFountTR("4"));
            }
        } // try
        catch (SQLException e) {LOGGER.error(html, e);html.append(prepareSQLToLog(mySQL, pParam) + "<br>" + e.toString());}
        catch (Exception el) {LOGGER.error(html, el);html.append(prepareSQLToLog(mySQL, pParam) + "<br>" + el.toString());}
        finally {
            try {
                if (st!=null) {
					st.close();
				}
            } catch (SQLException w) {w.toString();}
            try {
                if (con!=null) {
					con.close();
				}
            } catch (SQLException w) {w.toString();}
            Connector.closeConnection(con);
        } // finally

        html.append("</tbody>");
        html.append("</table>");
        
        return_value.setFunctionHTMLReturnValue(html.toString());
        return return_value;
    } // getBankAccountsList

    public transport getClubActionGiftsList2(transport mySource) {
    	
    	bc.service.pages page = new bc.service.pages(mySource.getPageNumber(), mySource.getPageRowCount());
    	String l_beg = page.getFirstRowNumber();
    	String l_end = page.getLastRowNumber();
    	
    	transport return_value=new transport();
		return_value.setFunctionName(mySource.getFunctionName());
		return_value.setIdDestination(mySource.getIdDestination());
		return_value.setFunctionParam(mySource.getFunctionParam());
		return_value.setFunctionParam2(mySource.getFunctionParam2());
		
		String findString = mySource.getFunctionParam();
		String id_club_event = mySource.getFunctionParam2();
		
		StringBuilder html = new StringBuilder();
		
		ArrayList<bcFeautureParam> pParam = initParamArray();

		String mySQL = 
        	" SELECT id_club_event_gift, name_club_event, cd_club_event_gift, name_club_event_gift, " +
	  		"        count_gift_all_frmt, count_gift_reserved_frmt, count_gift_given_frmt, " +
	  		"        count_gift_remain_frmt, is_active_tsl, id_gift " +
	  		"   FROM (SELECT ROWNUM rn, id_club_event_gift, name_club_event, cd_club_event_gift, name_club_event_gift, " +
	  		"                count_gift_all count_gift_all_frmt, " +
	  		"                count_gift_reserved count_gift_reserved_frmt, " +
	  		"                count_gift_given count_gift_given_frmt, " +
	  		"                count_gift_remain count_gift_remain_frmt, " +
	  		"                DECODE(is_active," +
	  		"                       'N', '<b><font color=\"red\">'||is_active_tsl||'</font></b>'," +
	  		"                       is_active_tsl" +
	  		"                ) is_active_tsl, id_gift " +
  			"           FROM (SELECT * " +
  			"                   FROM " + getGeneralDBScheme() + ".vc_club_event_gifts_all " +
  			"                  WHERE 1=1 ";
        if (!isEmpty(id_club_event)) {
        	mySQL = mySQL + " AND id_club_event = ? ";
        	pParam.add(new bcFeautureParam("int", id_club_event));
        }
        if (!isEmpty(findString)) {
        	mySQL = mySQL +
  				" AND (UPPER(cd_club_event_gift) LIKE UPPER('%'||?||'%') OR " +
  				"      UPPER(name_club_event_gift) LIKE UPPER('%'||?||'%')) ";
        	for (int i=0; i<2; i++) {
        	    pParam.add(new bcFeautureParam("string", findString));
        	}
        }

        pParam.add(new bcFeautureParam("int", l_end));
        pParam.add(new bcFeautureParam("int", l_beg));
        mySQL = mySQL +
        	"          		  ORDER BY name_club_event, name_club_event_gift" +
			"        ) WHERE ROWNUM < ? " + 
  			" ) WHERE rn >= ?";
        
        LOGGER.debug(prepareSQLToLog(mySQL, pParam));
        PreparedStatement st = null;
        String idClubEventGift = "";
        String cdClubEventGift = "";
        String nameClubEventGift = "";
        String markedCdClubEventGift = "";
        String markedNameClubEventGift = "";
        
        try{
        	con = Connector.getConnection(mySource.getSessionId());
            st = con.prepareStatement(mySQL);
            st = prepareParam(st, pParam);
            ResultSet rset = st.executeQuery();
            int rowCount = 0;
            while (rset.next()){
            	idClubEventGift = getHTMLValue(rset.getString("ID_CLUB_EVENT_GIFT"));
            	cdClubEventGift = getHTMLValue(rset.getString("CD_CLUB_EVENT_GIFT"));
            	nameClubEventGift = getHTMLValue(rset.getString("NAME_CLUB_EVENT_GIFT"));
            	markedCdClubEventGift = prepareFindString(cdClubEventGift, findString);
            	markedNameClubEventGift = prepareFindString(nameClubEventGift, findString);
            	
                html.append(
                        "<tr><td><div class=\"div_find\" onClick=\"changeOpener('"+idClubEventGift+"','"+nameClubEventGift+"')\">"+getValue2(rset.getString("NAME_CLUB_EVENT")) +"</div></td>" +
                        "<td><div class=\"div_find\" onClick=\"changeOpener('"+idClubEventGift+"','"+nameClubEventGift+"')\">"+getValue2(markedCdClubEventGift)+"</div></td>" +
                        "<td><div class=\"div_find_marked\" onClick=\"changeOpener('"+idClubEventGift+"','"+nameClubEventGift+"')\">"+getValue2(markedNameClubEventGift)+"</div></td>" +
                        "<td><div class=\"div_find\" onClick=\"changeOpener('"+idClubEventGift+"','"+nameClubEventGift+"')\">"+getValue2(rset.getString("COUNT_GIFT_ALL_FRMT"))+"</div></td>" +
                        "<td><div class=\"div_find\" onClick=\"changeOpener('"+idClubEventGift+"','"+nameClubEventGift+"')\">"+getValue2(rset.getString("COUNT_GIFT_RESERVED_FRMT"))+"</div></td>" +
                        "<td><div class=\"div_find\" onClick=\"changeOpener('"+idClubEventGift+"','"+nameClubEventGift+"')\">"+getValue2(rset.getString("COUNT_GIFT_GIVEN_FRMT"))+"</div></td>" +
                        "<td><div class=\"div_find\" onClick=\"changeOpener('"+idClubEventGift+"','"+nameClubEventGift+"')\">"+getValue2(rset.getString("COUNT_GIFT_REMAIN_FRMT"))+"</div></td>" +
                        "<td><div class=\"div_find\" onClick=\"changeOpener('"+idClubEventGift+"','"+nameClubEventGift+"')\">"+getValue2(rset.getString("IS_ACTIVE_TSL"))+"</div></td></tr>");
                       
                rowCount++;
            }
            if (rowCount == 0) {
            	html.append(getNoDataFountTR("9"));
            }
        } // try
        catch (SQLException e) {LOGGER.error(html, e);html.append(prepareSQLToLog(mySQL, pParam) + "<br>" + e.toString());}
        catch (Exception el) {LOGGER.error(html, el);html.append(prepareSQLToLog(mySQL, pParam) + "<br>" + el.toString());}
        finally {
            try {
                if (st!=null) {
					st.close();
				}
            } catch (SQLException w) {w.toString();}
            try {
                if (con!=null) {
					con.close();
				}
            } catch (SQLException w) {w.toString();}
            Connector.closeConnection(con);
        } // finally

        html.append("</tbody>");
        html.append("</table>");
        
        return_value.setFunctionHTMLReturnValue(html.toString());
        return return_value;
    } // getBankAccountsList

    public transport getBKAccountsList(transport mySource) {
    	
    	bc.service.pages page = new bc.service.pages(mySource.getPageNumber(), mySource.getPageRowCount());
    	String l_beg = page.getFirstRowNumber();
    	String l_end = page.getLastRowNumber();
    	
    	transport return_value=new transport();
		return_value.setFunctionName(mySource.getFunctionName());
		return_value.setIdDestination(mySource.getIdDestination());
		return_value.setFunctionParam(mySource.getFunctionParam());
		
		String findString = mySource.getFunctionParam();
		String is_group = mySource.getFunctionParam2();
		
		StringBuilder html = new StringBuilder();
		
		ArrayList<bcFeautureParam> pParam = initParamArray();

		String mySQL = 
        	" SELECT id_bk_account, cd_bk_account, name_bk_account, cd_bk_account_parent, " +
	  		"        is_group_tsl, exist_flag_tsl," +
	  		"        CASE WHEN LENGTH(cd_bk_account||' '||name_bk_account)>50 " +
    		"             THEN substr(cd_bk_account||' '||name_bk_account,1,47)||'...' " +
    		"             ELSE cd_bk_account||' '||name_bk_account " +
    		"        END cd_name_bk_account" +
	  		"   FROM (SELECT ROWNUM rn, id_bk_account, cd_bk_account, name_bk_account, " +
	  		"                cd_bk_account_parent, is_group_tsl, exist_flag_tsl " +
  			"           FROM (SELECT * " +
  			"                   FROM " + getGeneralDBScheme() + ".vc_bk_accounts_club_all " +
  			"                  WHERE 1=1 ";
        if (!isEmpty(findString)) {
        	mySQL = mySQL +
  			" AND (TO_CHAR(cd_bk_account) LIKE '%'||?||'%' OR " +
  			"      UPPER(name_bk_account) LIKE UPPER('%'||?||'%')) ";
        	for (int i=0; i<2; i++) {
        	    pParam.add(new bcFeautureParam("string", findString));
        	}
        }
        if (!isEmpty(is_group)) {
        	mySQL = mySQL + " AND is_group = ? ";
        	pParam.add(new bcFeautureParam("string", is_group));
        }
        pParam.add(new bcFeautureParam("int", l_end));
        pParam.add(new bcFeautureParam("int", l_beg));
        mySQL = mySQL +
        	"                  ORDER BY cd_bk_account" +
        	"        ) WHERE ROWNUM < ? " + 
  			" ) WHERE rn >= ?";
        
        LOGGER.debug(prepareSQLToLog(mySQL, pParam));
        PreparedStatement st = null;
        String idBKAccount = "";
        String cdBKAccount = "";
        String cdNameBKAccount = "";
        String nameBKAccount = "";
        String markedIdBKAccount = "";
        String markedCdBKAccount = "";
        String markedNameBKAccount = "";
        
        try{
        	con = Connector.getConnection(mySource.getSessionId());
            st = con.prepareStatement(mySQL);
            st = prepareParam(st, pParam);
            ResultSet rset = st.executeQuery();
            int rowCount = 0;
            while (rset.next()){
            	idBKAccount = getHTMLValue(rset.getString("ID_BK_ACCOUNT"));
            	cdBKAccount = getHTMLValue(rset.getString("CD_BK_ACCOUNT"));
            	cdNameBKAccount = getHTMLValue(rset.getString("CD_NAME_BK_ACCOUNT"));
            	nameBKAccount = getHTMLValue(rset.getString("NAME_BK_ACCOUNT"));
            	markedIdBKAccount = prepareFindString(idBKAccount, findString);
            	markedCdBKAccount = prepareFindString(cdBKAccount, findString);
            	markedNameBKAccount = prepareFindString(nameBKAccount, findString);
                
                html.append(
                        "<tr><td><div class=\"div_find\" onClick=\"changeOpener('"+idBKAccount+"','"+cdNameBKAccount+"')\">"+getValue2(markedIdBKAccount) +"</div></td>" +
                        "<td><div class=\"div_find_marked\" onClick=\"changeOpener('"+idBKAccount+"','"+cdNameBKAccount+"')\">"+getValue2(markedCdBKAccount)+"</div></td>" +
                        "<td><div class=\"div_find\" onClick=\"changeOpener('"+idBKAccount+"','"+cdNameBKAccount+"')\">"+getValue2(markedNameBKAccount)+"</div></td>" +
                        "<td><div class=\"div_find\" onClick=\"changeOpener('"+idBKAccount+"','"+cdNameBKAccount+"')\">"+getValue2(rset.getString("CD_BK_ACCOUNT_PARENT"))+"</div></td>" +
                        "<td><div class=\"div_find\" onClick=\"changeOpener('"+idBKAccount+"','"+cdNameBKAccount+"')\">"+getValue2(rset.getString("IS_GROUP_TSL"))+"</div></td>" +
                        "<td><div class=\"div_find\" onClick=\"changeOpener('"+idBKAccount+"','"+cdNameBKAccount+"')\">"+getValue2(rset.getString("EXIST_FLAG_TSL"))+"</div></td></tr>");
                       
                rowCount++;
            }
            if (rowCount == 0) {
            	html.append(getNoDataFountTR("6"));
            }
        } // try
        catch (SQLException e) {LOGGER.error(html, e);html.append(prepareSQLToLog(mySQL, pParam) + "<br>" + e.toString());}
        catch (Exception el) {LOGGER.error(html, el);html.append(prepareSQLToLog(mySQL, pParam) + "<br>" + el.toString());}
        finally {
            try {
                if (st!=null) {
					st.close();
				}
            } catch (SQLException w) {w.toString();}
            try {
                if (con!=null) {
					con.close();
				}
            } catch (SQLException w) {w.toString();}
            Connector.closeConnection(con);
        } // finally

        html.append("</tbody>");
        html.append("</table>");
        
        return_value.setFunctionHTMLReturnValue(html.toString());
        return return_value;
    } // getBKAccountsList

    public transport getLoyalitySchemesList(transport mySource) {
    	
    	bc.service.pages page = new bc.service.pages(mySource.getPageNumber(), mySource.getPageRowCount());
    	String l_beg = page.getFirstRowNumber();
    	String l_end = page.getLastRowNumber();
    	
    	transport return_value=new transport();
		return_value.setFunctionName(mySource.getFunctionName());
		return_value.setIdDestination(mySource.getIdDestination());
		return_value.setFunctionParam(mySource.getFunctionParam());
		
		String findString = mySource.getFunctionParam();
		String id_jur_prs = mySource.getFunctionParam2();
		
		StringBuilder html = new StringBuilder();
		
		ArrayList<bcFeautureParam> pParam = initParamArray();

		String mySQL = 
			" SELECT rn, name_partner, name_loyality_scheme title_loyality_scheme, " +
            "		 cd_kind_loyality||' - '||name_kind_loyality name_kind_loyality, " +
            "		 date_beg_frmt, date_end_frmt, id_loyality_scheme " +
            "   FROM (SELECT ROWNUM rn, id_loyality_scheme, name_loyality_scheme, " +
            "				 desc_loyality_scheme, cd_kind_loyality, name_kind_loyality, sname_jur_prs name_partner, " +
            "				 date_beg_frmt, date_end_frmt " +
  			"           FROM (SELECT * " +
  			"                   FROM " + getGeneralDBScheme() + ".vc_loyality_scheme_club_all " +
  			"                  WHERE 1=1 ";
		if (!isEmpty(id_jur_prs)) {
			mySQL = mySQL + " AND id_jur_prs = ?";
    	    pParam.add(new bcFeautureParam("int", id_jur_prs));
		}
        if (!isEmpty(findString)) {
        	mySQL = mySQL +
  			"          AND (TO_CHAR(id_loyality_scheme) LIKE '%'||?||'%' OR " +
  			"               UPPER(name_loyality_scheme) LIKE UPPER('%'||?||'%')) ";
        	for (int i=0; i<2; i++) {
        	    pParam.add(new bcFeautureParam("string", findString));
        	}
        }
        pParam.add(new bcFeautureParam("int", l_end));
        pParam.add(new bcFeautureParam("int", l_beg));
        mySQL = mySQL +
        	"                  ORDER BY name_loyality_scheme" +
        	"        ) WHERE ROWNUM < ?" + 
  			" ) WHERE rn >= ?";
        
        LOGGER.debug(prepareSQLToLog(mySQL, pParam));
        PreparedStatement st = null;
        String idScheme = "";
        String nameScheme = "";
        String markedNameScheme = "";
        
        try{
        	con = Connector.getConnection(mySource.getSessionId());
            st = con.prepareStatement(mySQL);
            st = prepareParam(st, pParam);
            ResultSet rset = st.executeQuery();
            int rowCount = 0;
            while (rset.next()){
            	idScheme = getHTMLValue(rset.getString("ID_LOYALITY_SCHEME"));
            	nameScheme = getHTMLValue(rset.getString("TITLE_LOYALITY_SCHEME"));
            	markedNameScheme = prepareFindString(nameScheme, findString);
                
                html.append(
                        "<tr><td><div class=\"div_find\" onClick=\"changeOpener('"+idScheme+"','"+nameScheme+"')\">"+getValue2(rset.getString("NAME_PARTNER")) +"</div></td>" +
                        "<td><div class=\"div_find_marked\" onClick=\"changeOpener('"+idScheme+"','"+nameScheme+"')\">"+getValue2(markedNameScheme)+"</div></td>" +
                        "<td><div class=\"div_find\" onClick=\"changeOpener('"+idScheme+"','"+nameScheme+"')\">"+getValue2(rset.getString("NAME_KIND_LOYALITY"))+"</div></td>" +
                        "<td><div class=\"div_find\" onClick=\"changeOpener('"+idScheme+"','"+nameScheme+"')\">"+getValue2(rset.getString("DATE_BEG_FRMT"))+"</div></td>" +
                        "<td><div class=\"div_find\" onClick=\"changeOpener('"+idScheme+"','"+nameScheme+"')\">"+getValue2(rset.getString("DATE_END_FRMT"))+"</div></td></tr>");
                       
                rowCount++;
            }
            if (rowCount == 0) {
            	html.append(getNoDataFountTR("9"));
            }
        } // try
        catch (SQLException e) {LOGGER.error(html, e);html.append(prepareSQLToLog(mySQL, pParam) + "<br>" + e.toString());}
        catch (Exception el) {LOGGER.error(html, el);html.append(prepareSQLToLog(mySQL, pParam) + "<br>" + el.toString());}
        finally {
            try {
                if (st!=null) {
					st.close();
				}
            } catch (SQLException w) {w.toString();}
            try {
                if (con!=null) {
					con.close();
				}
            } catch (SQLException w) {w.toString();}
            Connector.closeConnection(con);
        } // finally

        html.append("</tbody>");
        html.append("</table>");
        
        return_value.setFunctionHTMLReturnValue(html.toString());
        return return_value;
    } // getLoyalitySchemesList

    public transport getTerminalLoyalityHistoryList(transport mySource) {
    	
    	bc.service.pages page = new bc.service.pages(mySource.getPageNumber(), mySource.getPageRowCount());
    	String l_beg = page.getFirstRowNumber();
    	String l_end = page.getLastRowNumber();
    	
    	transport return_value=new transport();
		return_value.setFunctionName(mySource.getFunctionName());
		return_value.setIdDestination(mySource.getIdDestination());
		return_value.setFunctionParam(mySource.getFunctionParam());
		
		String findString = mySource.getFunctionParam();
		String id_term = mySource.getFunctionParam2();
		String id_service_place = mySource.getFunctionParam3();
		
		StringBuilder html = new StringBuilder();
		
		ArrayList<bcFeautureParam> pParam = initParamArray();

		String mySQL = 
        	" SELECT rn, id_loyality_history, id_term, name_loyality_scheme, name_kind_loyality, " +
            "		 date_beg_frmt, date_end_frmt " +
	  		"   FROM (SELECT ROWNUM rn, id_loyality_history, id_term, name_loyality_scheme, name_kind_loyality, " +
            "		         date_beg_frmt, date_end_frmt " +
  			"           FROM (SELECT id_loyality_history, id_term, name_loyality_scheme, name_kind_loyality, " +
  			"						 date_beg_frmt, date_end_frmt " +
  			"                   FROM " + getGeneralDBScheme() + ".vc_term_loyality_h_club_all " +
  			"				   WHERE id_term = ? " + 
  			"					 AND id_service_place = ?";
		pParam.add(new bcFeautureParam("int", id_term));
		pParam.add(new bcFeautureParam("int", id_service_place));
		
        if (!isEmpty(findString)) {
        	mySQL = mySQL +  
      			"            AND (UPPER(TO_CHAR(id_loyality_history)) = UPPER('%'||?||'%') OR " +
      			"                 date_beg_frmt LIKE '%'||?||'%' OR " +
      			"                 date_end_frmt LIKE '%'||?||'%') ";
        	for (int i=0; i<3; i++) {
        	    pParam.add(new bcFeautureParam("string", findString));
        	}

        }

        pParam.add(new bcFeautureParam("int", l_end));
        pParam.add(new bcFeautureParam("int", l_beg));
        mySQL = mySQL +
			"          ORDER BY date_beg desc " +
        	"         ) WHERE ROWNUM < ? " + 
  			" ) WHERE rn >= ?";
        
        LOGGER.debug(prepareSQLToLog(mySQL, pParam));
        
        PreparedStatement st = null;
        String idLoyHistory = "";
        String dateBeg = "";
        String dateEnd = "";
        String markedIdLoyHistory = "";
        String markedDateBeg = "";
        String markedDateEnd = "";
        
        try{
        	con = Connector.getConnection(mySource.getSessionId());
            st = con.prepareStatement(mySQL);
            st = prepareParam(st, pParam);
            ResultSet rset = st.executeQuery();
            int rowCount = 0;
            while (rset.next()){
            	idLoyHistory = getHTMLValue(rset.getString("ID_LOYALITY_HISTORY"));
            	dateBeg = getHTMLValue(rset.getString("DATE_BEG_FRMT"));
            	dateEnd = getHTMLValue(rset.getString("DATE_END_FRMT"));
            	markedIdLoyHistory = prepareFindString(idLoyHistory, findString);
            	markedDateBeg = prepareFindString(dateBeg, findString);
            	markedDateEnd = prepareFindString(dateEnd, findString);
                
                html.append(
                		"<tr><td><div class=\"div_find\" onClick=\"changeOpener('"+idLoyHistory+"')\">"+getValue2(rset.getString("RN")) +"</div></td>" +
                        "<td><div class=\"div_find\" onClick=\"changeOpener('"+idLoyHistory+"')\">"+getValue2(markedIdLoyHistory) +"</div></td>" +
                        "<td><div class=\"div_find\" onClick=\"changeOpener('"+idLoyHistory+"')\">"+getValue2(rset.getString("ID_TERM"))+"</div></td>" +
                        "<td><div class=\"div_find\" onClick=\"changeOpener('"+idLoyHistory+"')\">"+getValue2(rset.getString("NAME_LOYALITY_SCHEME"))+"</div></td>" +
                        "<td><div class=\"div_find\" onClick=\"changeOpener('"+idLoyHistory+"')\">"+getValue2(rset.getString("NAME_KIND_LOYALITY"))+"</div></td>" +
                        "<td><div class=\"div_find\" onClick=\"changeOpener('"+idLoyHistory+"')\">"+getValue2(markedDateBeg)+"</div></td>" +
                        "<td><div class=\"div_find\" onClick=\"changeOpener('"+idLoyHistory+"')\">"+getValue2(markedDateEnd)+"</div></td></tr>");
                       
                rowCount++;
            }
            if (rowCount == 0) {
            	html.append(getNoDataFountTR("7"));
            }
        } // try
        catch (SQLException e) {LOGGER.error(html, e);html.append(prepareSQLToLog(mySQL, pParam) + "<br>" + e.toString());}
        catch (Exception el) {LOGGER.error(html, el);html.append(prepareSQLToLog(mySQL, pParam) + "<br>" + el.toString());}
        finally {
            try {
                if (st!=null) {
					st.close();
				}
            } catch (SQLException w) {w.toString();}
            try {
                if (con!=null) {
					con.close();
				}
            } catch (SQLException w) {w.toString();}
            Connector.closeConnection(con);
        } // finally

        html.append("</tbody>");
        html.append("</table>");
        
        return_value.setFunctionHTMLReturnValue(html.toString());
        return return_value;
    } // getSchedulesList

    public transport getSchedulesList(transport mySource) {
    	
    	bc.service.pages page = new bc.service.pages(mySource.getPageNumber(), mySource.getPageRowCount());
    	String l_beg = page.getFirstRowNumber();
    	String l_end = page.getLastRowNumber();
    	
    	transport return_value=new transport();
		return_value.setFunctionName(mySource.getFunctionName());
		return_value.setIdDestination(mySource.getIdDestination());
		return_value.setFunctionParam(mySource.getFunctionParam());
		
		String findString = mySource.getFunctionParam();
		
		StringBuilder html = new StringBuilder();
		
		ArrayList<bcFeautureParam> pParam = initParamArray();

		String mySQL = 
        	" SELECT id_shedule, name_shedule, desc_shedule, name_loyality_scheme_default, " +
        	" 	     date_beg_frmt, date_end_frmt " +
	  		"   FROM (SELECT ROWNUM rn, id_shedule, name_shedule, desc_shedule, name_loyality_scheme_default, " +
        	" 	             date_beg_frmt, date_end_frmt " +
  			"           FROM (SELECT * " +
  			"                   FROM " + getGeneralDBScheme() + ".v_ls_shedule_name_club_all ";
        if (!isEmpty(findString)) {
        	mySQL = mySQL +
  			"          WHERE (TO_CHAR(id_shedule) LIKE '%'||?||'%' OR " +
  			"                  UPPER(name_shedule) LIKE UPPER('%'||?||'%')) ";
        	for (int i=0; i<2; i++) {
        	    pParam.add(new bcFeautureParam("string", findString));
        	}
        }
        pParam.add(new bcFeautureParam("int", l_end));
        pParam.add(new bcFeautureParam("int", l_beg));
        mySQL = mySQL +
        	"                  ORDER BY name_shedule" +
        	"        ) WHERE ROWNUM < ? " + 
  			" ) WHERE rn >= ?";
        
        LOGGER.debug(prepareSQLToLog(mySQL, pParam));
        PreparedStatement st = null;
        String idSchedule = "";
        String nameSchedule = "";
        String markedIdSchedule = "";
        String markedNameSchedule = "";
        
        try{
        	con = Connector.getConnection(mySource.getSessionId());
            st = con.prepareStatement(mySQL);
            st = prepareParam(st, pParam);
            ResultSet rset = st.executeQuery();
            int rowCount = 0;
            while (rset.next()){
            	idSchedule = getHTMLValue(rset.getString("ID_SHEDULE"));
            	nameSchedule = getHTMLValue(rset.getString("NAME_SHEDULE"));
            	markedIdSchedule = prepareFindString(idSchedule, findString);
            	markedNameSchedule = prepareFindString(nameSchedule, findString);
                 
                html.append(
                        "<tr><td><div class=\"div_find\" onClick=\"changeOpener('"+idSchedule+"','"+nameSchedule+"')\">"+getValue2(markedIdSchedule) +"</div></td>" +
                        "<td><div class=\"div_find_marked\" onClick=\"changeOpener('"+idSchedule+"','"+nameSchedule+"')\">"+getValue2(markedNameSchedule)+"</div></td>" +
                        "<td><div class=\"div_find\" onClick=\"changeOpener('"+idSchedule+"','"+nameSchedule+"')\">"+getValue2(rset.getString("DESC_SHEDULE"))+"</div></td>" +
                        "<td><div class=\"div_find\" onClick=\"changeOpener('"+idSchedule+"','"+nameSchedule+"')\">"+getValue2(rset.getString("NAME_LOYALITY_SCHEME_DEFAULT"))+"</div></td>" +
                        "<td><div class=\"div_find\" onClick=\"changeOpener('"+idSchedule+"','"+nameSchedule+"')\">"+getValue2(rset.getString("DATE_BEG_FRMT"))+"</div></td>" +
                        "<td><div class=\"div_find\" onClick=\"changeOpener('"+idSchedule+"','"+nameSchedule+"')\">"+getValue2(rset.getString("DATE_END_FRMT"))+"</div></td></tr>");
                       
                rowCount++;
            }
            if (rowCount == 0) {
            	html.append(getNoDataFountTR("7"));
            }
        } // try
        catch (SQLException e) {LOGGER.error(html, e);html.append(prepareSQLToLog(mySQL, pParam) + "<br>" + e.toString());}
        catch (Exception el) {LOGGER.error(html, el);html.append(prepareSQLToLog(mySQL, pParam) + "<br>" + el.toString());}
        finally {
            try {
                if (st!=null) {
					st.close();
				}
            } catch (SQLException w) {w.toString();}
            try {
                if (con!=null) {
					con.close();
				}
            } catch (SQLException w) {w.toString();}
            Connector.closeConnection(con);
        } // finally

        html.append("</tbody>");
        html.append("</table>");
        
        return_value.setFunctionHTMLReturnValue(html.toString());
        return return_value;
    } // getSchedulesList

    public transport getGiftsList(transport mySource) {
    	
    	bc.service.pages page = new bc.service.pages(mySource.getPageNumber(), mySource.getPageRowCount());
    	String l_beg = page.getFirstRowNumber();
    	String l_end = page.getLastRowNumber();
    	
    	transport return_value=new transport();
		return_value.setFunctionName(mySource.getFunctionName());
		return_value.setIdDestination(mySource.getIdDestination());
		return_value.setFunctionParam(mySource.getFunctionParam());
		return_value.setFunctionParam2(mySource.getFunctionParam2());
		
		String findString = mySource.getFunctionParam();
		
		StringBuilder html = new StringBuilder();
		
		ArrayList<bcFeautureParam> pParam = initParamArray();

		String mySQL = 
        		" SELECT id_gift, cd_gift, name_gift, name_gift_type " +
        		"   FROM (SELECT ROWNUM rn, id_gift, cd_gift, name_gift, name_gift_type " +
        		"           FROM (SELECT * " +
        		"                   FROM " + getGeneralDBScheme() + ".vc_gifts_club_all ";
        if (!isEmpty(findString)) {
        	mySQL = mySQL +
  			"          WHERE (TO_CHAR(id_gift) LIKE UPPER('%'||?||'%') OR " +
  			"                 UPPER(cd_gift) LIKE UPPER('%'||?||'%') OR " +
  			"                 UPPER(name_gift) LIKE UPPER('%'||?||'%')) ";
        	for (int i=0; i<3; i++) {
        	    pParam.add(new bcFeautureParam("string", findString));
        	}
        }
        pParam.add(new bcFeautureParam("int", l_end));
        pParam.add(new bcFeautureParam("int", l_beg));
        mySQL = mySQL +
        	"                  ORDER BY name_gift" +
        	"        ) WHERE ROWNUM < ? " + 
  			" ) WHERE rn >= ?";
        
        LOGGER.debug(prepareSQLToLog(mySQL, pParam));
        PreparedStatement st = null;
        String idGift = "";
        String cdGift = "";
        String nameGift = "";
        String markedIdGift = "";
        String markedCdGift = "";
        String markedNameGift = "";
        
        try{
        	con = Connector.getConnection(mySource.getSessionId());
            st = con.prepareStatement(mySQL);
            st = prepareParam(st, pParam);
            ResultSet rset = st.executeQuery();
            int rowCount = 0;
            while (rset.next()){
            	idGift = getHTMLValue(rset.getString("ID_GIFT"));
            	cdGift = getHTMLValue(rset.getString("CD_GIFT"));
            	nameGift = getHTMLValue(rset.getString("NAME_GIFT"));
            	markedIdGift = prepareFindString(idGift, findString);
            	markedCdGift = prepareFindString(cdGift, findString);
            	markedNameGift = prepareFindString(nameGift, findString);
                
                html.append(
                        "<tr>" +
                        "<td><div class=\"div_find\" onClick=\"changeOpener('"+idGift+"','"+nameGift+"')\">"+getValue2(markedIdGift) +"</div></td>" +
                        "<td><div class=\"div_find\" onClick=\"changeOpener('"+idGift+"','"+nameGift+"')\">"+getValue2(markedCdGift)+"</div></td>" +
                        "<td><div class=\"div_find_marked\" onClick=\"changeOpener('"+idGift+"','"+nameGift+"')\">"+getValue2(markedNameGift)+"</div></td>" +
                        "<td><div class=\"div_find\" onClick=\"changeOpener('"+idGift+"','"+nameGift+"')\">"+getValue2(rset.getString("NAME_GIFT_TYPE"))+"</div></td>" +
                        "</tr>");
                       
                rowCount++;
            }
            if (rowCount == 0) {
            	html.append(getNoDataFountTR("4"));
            }
        } // try
        catch (SQLException e) {LOGGER.error(html, e);html.append(prepareSQLToLog(mySQL, pParam) + "<br>" + e.toString());}
        catch (Exception el) {LOGGER.error(html, el);html.append(prepareSQLToLog(mySQL, pParam) + "<br>" + el.toString());}
        finally {
            try {
                if (st!=null) {
					st.close();
				}
            } catch (SQLException w) {w.toString();}
            try {
                if (con!=null) {
					con.close();
				}
            } catch (SQLException w) {w.toString();}
            Connector.closeConnection(con);
        } // finally

        html.append("</tbody>");
        html.append("</table>");
        
        return_value.setFunctionHTMLReturnValue(html.toString());
        return return_value;
    } // getGiftsList

    public transport getGiftsLogisticList(transport mySource) {
    	
    	bc.service.pages page = new bc.service.pages(mySource.getPageNumber(), mySource.getPageRowCount());
    	String l_beg = page.getFirstRowNumber();
    	String l_end = page.getLastRowNumber();
    	
    	transport return_value=new transport();
		return_value.setFunctionName(mySource.getFunctionName());
		return_value.setIdDestination(mySource.getIdDestination());
		return_value.setFunctionParam(mySource.getFunctionParam());
		return_value.setFunctionParam2(mySource.getFunctionParam2());
		
		String findString = mySource.getFunctionParam();
		String id_gift = mySource.getFunctionParam2();
		String exist_show = mySource.getFunctionParam3();
		/*
		 *  Значения exist_show:
		 *  1-я часть - выводить товары, которые уже все выданные
		 *  2-я часть - разрешать выбирать уже выданные товары (остаток буден минусовым)
		*/
		boolean exist_only = false;
		if ("TRUE_TRUE".equalsIgnoreCase(exist_show) || "TRUE_FALSE".equalsIgnoreCase(exist_show)) {
			exist_only = true;
		}
		boolean show_not_exist = false;
		if ("TRUE_TRUE".equalsIgnoreCase(exist_show) || "FALSE_TRUE".equalsIgnoreCase(exist_show)) {
			show_not_exist = true;
		}
		
		StringBuilder html = new StringBuilder();
		
		ArrayList<bcFeautureParam> pParam = initParamArray();

		String mySQL = 
        		" SELECT action_date_frmt, cd_gift, name_gift, desc_gift, " +
        		"        cost_one_gift, count_gift_all_frmt, " +
        		"        count_gift_given_frmt, count_gift_remain_frmt, count_gift_remain, id_lg_gift " +
        		"   FROM (SELECT ROWNUM rn, action_date_frmt, cd_gift, name_gift, desc_gift, " +
        		"                cost_one_gift_frmt||' '||sname_currency cost_one_gift, " +
        		"                DECODE(NVL(count_gift_all,0)," +
        		"                       0, '<b><font color=\"red\">'||TO_CHAR(count_gift_all)||'</font></b>'," +
        		"                       '<b><font color=\"green\">'||TO_CHAR(count_gift_all)||'</font></b>'" +
        		"                ) count_gift_all_frmt, " +
        		"                DECODE(NVL(count_gift_given,0)," +
        		"                       0, TO_CHAR(count_gift_given)," +
        		"                       '<font color=\"blue\">'||TO_CHAR(count_gift_given)||'</font>'" +
        		"                ) count_gift_given_frmt, " +
        		"                DECODE(NVL(count_gift_remain,0)," +
        		"                       0, '<b><font color=\"red\">'||TO_CHAR(count_gift_remain)||'</font></b>'," +
        		"                       '<font color=\"green\">'||TO_CHAR(count_gift_remain)||'</font>'" +
        		"                ) count_gift_remain_frmt, count_gift_remain, id_lg_gift " +
        		"           FROM (SELECT * " +
        		"                   FROM " + getGeneralDBScheme() + ".vc_lg_gifts_club_all " +
        		"                  WHERE 1=1";
        if (!isEmpty(findString)) {
        	mySQL = mySQL + " AND (UPPER(desc_gift) LIKE UPPER('%'||?||'%')) ";
        	pParam.add(new bcFeautureParam("string", findString));
        }
        if (!isEmpty(id_gift)) {
        	mySQL = mySQL + " AND id_gift = ? ";
        	pParam.add(new bcFeautureParam("int", id_gift));
        }
        if (exist_only) {
        	mySQL = mySQL + " AND count_gift_remain > 0 ";
        }
        pParam.add(new bcFeautureParam("int", l_end));
        pParam.add(new bcFeautureParam("int", l_beg));
        mySQL = mySQL +
        	"                  ORDER BY action_date DESC, name_gift" +
        	"        ) WHERE ROWNUM < ? " + 
  			" ) WHERE rn >= ?";
        
        LOGGER.debug(prepareSQLToLog(mySQL, pParam));
        PreparedStatement st = null;
        String idLGGift = "";
        String descGift = "";
        String markedDescGift = "";
        
        try{
        	con = Connector.getConnection(mySource.getSessionId());
            st = con.prepareStatement(mySQL);
            st = prepareParam(st, pParam);
            ResultSet rset = st.executeQuery();
            int rowCount = 0;
            while (rset.next()){
            	idLGGift = getHTMLValue(rset.getString("ID_LG_GIFT"));
            	descGift = getHTMLValue(rset.getString("DESC_GIFT"));
            	markedDescGift = prepareFindString(descGift, findString);
                if (!show_not_exist && "0".equalsIgnoreCase(rset.getString("COUNT_GIFT_REMAIN"))) {
                    html.append(
                            "<tr>" +
                            "<td><div class=\"div_find_none\">"+getValue2(rset.getString("ACTION_DATE_FRMT")) +"</div></td>" +
                            "<td><div class=\"div_find_none\">"+getValue2(rset.getString("CD_GIFT"))+"</div></td>" +
                            "<td><div class=\"div_find_none\">"+getValue2(rset.getString("NAME_GIFT"))+"</div></td>" +
                            "<td><div class=\"div_find_none\">"+getValue2(markedDescGift)+"</div></td>" +
                            "<td><div class=\"div_find_none\">"+getValue2(rset.getString("COST_ONE_GIFT"))+"</div></td>" +
                            "<td><div class=\"div_find_none\" style=\"text-align:center\">"+getValue2(rset.getString("COUNT_GIFT_ALL_FRMT"))+"</div></td>" +
                            "<td><div class=\"div_find_none\" style=\"text-align:center\">"+getValue2(rset.getString("COUNT_GIFT_GIVEN_FRMT"))+"</div></td>" +
                            "<td><div class=\"div_find_none\" style=\"text-align:center\">"+getValue2(rset.getString("COUNT_GIFT_REMAIN_FRMT"))+"</div></td>" +
                            "</tr>");
                } else {
                	html.append(
                        "<tr>" +
                        "<td><div class=\"div_find\" onClick=\"changeOpener('"+idLGGift+"','"+descGift+"')\">"+getValue2(rset.getString("ACTION_DATE_FRMT")) +"</div></td>" +
                        "<td><div class=\"div_find\" onClick=\"changeOpener('"+idLGGift+"','"+descGift+"')\">"+getValue2(rset.getString("CD_GIFT"))+"</div></td>" +
                        "<td><div class=\"div_find_marked\" onClick=\"changeOpener('"+idLGGift+"','"+descGift+"')\">"+getValue2(rset.getString("NAME_GIFT"))+"</div></td>" +
                        "<td><div class=\"div_find\" onClick=\"changeOpener('"+idLGGift+"','"+descGift+"')\">"+getValue2(markedDescGift)+"</div></td>" +
                        "<td><div class=\"div_find\" onClick=\"changeOpener('"+idLGGift+"','"+descGift+"')\">"+getValue2(rset.getString("COST_ONE_GIFT"))+"</div></td>" +
                        "<td><div class=\"div_find\" style=\"text-align:center\" onClick=\"changeOpener('"+idLGGift+"','"+descGift+"')\">"+getValue2(rset.getString("COUNT_GIFT_ALL_FRMT"))+"</div></td>" +
                        "<td><div class=\"div_find\" style=\"text-align:center\" onClick=\"changeOpener('"+idLGGift+"','"+descGift+"')\">"+getValue2(rset.getString("COUNT_GIFT_GIVEN_FRMT"))+"</div></td>" +
                        "<td><div class=\"div_find\" style=\"text-align:center\" onClick=\"changeOpener('"+idLGGift+"','"+descGift+"')\">"+getValue2(rset.getString("COUNT_GIFT_REMAIN_FRMT"))+"</div></td>" +
                        "</tr>");
                } 
                rowCount++;
            }
            if (rowCount == 0) {
            	html.append(getNoDataFountTR("8"));
            }
        } // try
        catch (SQLException e) {LOGGER.error(html, e);html.append(prepareSQLToLog(mySQL, pParam) + "<br>" + e.toString());}
        catch (Exception el) {LOGGER.error(html, el);html.append(prepareSQLToLog(mySQL, pParam) + "<br>" + el.toString());}
        finally {
            try {
                if (st!=null) {
					st.close();
				}
            } catch (SQLException w) {w.toString();}
            try {
                if (con!=null) {
					con.close();
				}
            } catch (SQLException w) {w.toString();}
            Connector.closeConnection(con);
        } // finally

        html.append("</tbody>");
        html.append("</table>");
        
        return_value.setFunctionHTMLReturnValue(html.toString());
        return return_value;
    } // getGiftsList

    public transport getClubActionGiftsList(transport mySource) {
    	
    	bc.service.pages page = new bc.service.pages(mySource.getPageNumber(), mySource.getPageRowCount());
    	String l_beg = page.getFirstRowNumber();
    	String l_end = page.getLastRowNumber();
    	
    	transport return_value=new transport();
		return_value.setFunctionName(mySource.getFunctionName());
		return_value.setIdDestination(mySource.getIdDestination());
		return_value.setFunctionParam(mySource.getFunctionParam());
		return_value.setFunctionParam2(mySource.getFunctionParam2());
		
		String findString = mySource.getFunctionParam();
		String idClubAction = mySource.getFunctionParam2();
		String idRealization = mySource.getFunctionParam3();
		
		StringBuilder html = new StringBuilder();
		
		ArrayList<bcFeautureParam> pParam = initParamArray();

		String mySQL =
        		" SELECT id_gift, name_gift, count_gift_all, count_gift_remain, " +
        		"        bal_count_frmt, name_currency, cd_currency" +
        		"   FROM (SELECT ROWNUM rn, id_gift, name_gift, count_gift_all, count_gift_remain, " +
        		"                bal_count_frmt, name_currency, cd_currency " +
        		"           FROM (SELECT * " +
        		"                   FROM " + getGeneralDBScheme() + ".vc_club_event_gifts_all " +
        		"                  WHERE id_club_event = ? " + 
        		"                    AND id_club_event_realization = ? " + 
        		"                    AND is_active = 'Y'";
		pParam.add(new bcFeautureParam("int", idClubAction));
		pParam.add(new bcFeautureParam("int", idRealization));
		
        if (!isEmpty(findString)) {
        	mySQL = mySQL +
  			"          AND (TO_CHAR(id_gift) LIKE '%'||?||'%' OR " +
  			"               UPPER(name_gift) LIKE UPPER('%'||?||'%')) ";
        	for (int i=0; i<2; i++) {
        	    pParam.add(new bcFeautureParam("string", findString));
        	}
        }
        pParam.add(new bcFeautureParam("int", l_end));
        pParam.add(new bcFeautureParam("int", l_beg));
        mySQL = mySQL +
        	"                  ORDER BY name_gift" +
        	"        ) WHERE ROWNUM < ? " + 
  			" ) WHERE rn >= ?";
        
        LOGGER.debug(prepareSQLToLog(mySQL, pParam));
        PreparedStatement st = null;
        String idGift = "";
        String nameGift = "";
        String markedIdGift = "";
        String markedNameGift = "";
        String cdCurrency = "";
        String nameCurrency = "";
        
         try{
        	con = Connector.getConnection(mySource.getSessionId());
            st = con.prepareStatement(mySQL);
            st = prepareParam(st, pParam);
            ResultSet rset = st.executeQuery();
            int rowCount = 0;
            while (rset.next()){
            	idGift = getHTMLValue(rset.getString("ID_GIFT"));
            	nameGift = getHTMLValue(rset.getString("NAME_GIFT"));
            	cdCurrency = getHTMLValue(rset.getString("CD_CURRENCY"));
            	nameCurrency = getHTMLValue(rset.getString("NAME_CURRENCY"));
            	markedIdGift = prepareFindString(idGift, findString);
            	markedNameGift = prepareFindString(nameGift, findString);
                
                html.append(
                        "<tr>" +
                        "<td><div class=\"div_find\" onClick=\"changeOpener('"+idGift+"','"+nameGift+"','"+cdCurrency+"','"+nameCurrency+"')\">"+getValue2(markedIdGift) +"</div></td>" +
                        "<td><div class=\"div_find_marked\" onClick=\"changeOpener('"+idGift+"','"+nameGift+"','"+cdCurrency+"','"+nameCurrency+"')\">"+getValue2(markedNameGift)+"</div></td>" +
                        "<td><div class=\"div_find\" onClick=\"changeOpener('"+idGift+"','"+nameGift+"','"+cdCurrency+"','"+nameCurrency+"')\">"+getValue2(rset.getString("COUNT_GIFT_ALL"))+"</div></td>" +
                        "<td><div class=\"div_find\" onClick=\"changeOpener('"+idGift+"','"+nameGift+"','"+cdCurrency+"','"+nameCurrency+"')\">"+getValue2(rset.getString("COUNT_GIFT_REMAIN"))+"</div></td>" +
                        "<td><div class=\"div_find\" onClick=\"changeOpener('"+idGift+"','"+nameGift+"','"+cdCurrency+"','"+nameCurrency+"')\">"+getValue2(rset.getString("BAL_COUNT_FRMT"))+"</div></td>" +
                        "<td><div class=\"div_find\" onClick=\"changeOpener('"+idGift+"','"+nameGift+"','"+cdCurrency+"','"+nameCurrency+"')\">"+getValue2(nameCurrency)+"</div></td>" +
                        "</tr>");
                       
                rowCount++;
            }
            if (rowCount == 0) {
            	html.append(getNoDataFountTR("6"));
            }
        } // try
        catch (SQLException e) {LOGGER.error(html, e);html.append(prepareSQLToLog(mySQL, pParam) + "<br>" + e.toString());}
        catch (Exception el) {LOGGER.error(html, el);html.append(prepareSQLToLog(mySQL, pParam) + "<br>" + el.toString());}
        finally {
            try {
                if (st!=null) {
					st.close();
				}
            } catch (SQLException w) {w.toString();}
            try {
                if (con!=null) {
					con.close();
				}
            } catch (SQLException w) {w.toString();}
            Connector.closeConnection(con);
        } // finally

        html.append("</tbody>");
        html.append("</table>");
        
        return_value.setFunctionHTMLReturnValue(html.toString());
        return return_value;
    } // getGiftsList


	public cardCategories getCardCategoriesArray(cardCategories source){
		cardCategories return_value=new cardCategories();
		return_value.setCategoryPrefix(source.getCategoryPrefix());
		return_value.setIdCardStatus(source.getIdCardStatus());
		return_value.setIdBonCategory(source.getIdBonCategory());
		return_value.setIdDiscCategory(source.getIdDiscCategory());
		
    	ArrayList<String> bonCategoriesId=new ArrayList<String>();
    	ArrayList<String> bonCategoriesName=new ArrayList<String>();		
    	ArrayList<String> discCategoriesId=new ArrayList<String>();
    	ArrayList<String> discCategoriesName=new ArrayList<String>();	
    	
    	setSessionId(source.getSessionId());
    	
    	PreparedStatement st = null;
    	
    	// -- Работаем с областями
    	try{
			String mySQL = 
				" SELECT id_category, name_bon_category, name_disc_category" +
				"   FROM (SELECT id_category, " +
				"        		 name_category /*||' ('||club_bon_percent||')'*/ name_bon_category, " +
				"        		 name_category /*||' ('||club_disc_percent||')'*/ name_disc_category, " +
				"        		 id_category_name " +
				"   		FROM " + getGeneralDBScheme() + ".vc_card_category_club_all " +
				"  		   WHERE id_card_status = ? " + 
				"    		 AND exist_flag = 'Y'" +
				"  		   ORDER BY DECODE(id_category_name, 99, 2, 1), name_category" +
				"        )";
	    	LOGGER.debug(mySQL + ", 1={" + source.getIdCardStatus() + ",int}");
	    	
	    	con = Connector.getConnection(source.getSessionId());
	    	st = con.prepareStatement(mySQL);
            st.setInt(1, Integer.parseInt(source.getIdCardStatus()));
            ResultSet rs = st.executeQuery();
            
	    	while(rs.next()){
	    		bonCategoriesId.add(rs.getString("ID_CATEGORY"));
	    		bonCategoriesName.add(rs.getString("NAME_BON_CATEGORY"));
	    		discCategoriesId.add(rs.getString("ID_CATEGORY"));
	    		discCategoriesName.add(rs.getString("NAME_DISC_CATEGORY"));
	    	}
	    	rs.close();
		} catch (SQLException e) {LOGGER.error("(oblast) SQLException: " + e.toString());
		} catch (Exception el) {LOGGER.error("(oblast) Exception: " + el.toString());}
		finally {
            try {
                if (st!=null) {
					st.close();
				}
            } catch (SQLException w) {w.toString();}
            try {
                if (con!=null) {
					con.close();
				}
            } catch (SQLException w) {w.toString();}
            Connector.closeConnection(con);
        } // finally
		
		return_value.setBonCategoriesId(bonCategoriesId.toArray(new String[]{}));
		return_value.setBonCategoriesName(bonCategoriesName.toArray(new String[]{}));
		return_value.setDiscCategoriesId(discCategoriesId.toArray(new String[]{}));
		return_value.setDiscCategoriesName(discCategoriesName.toArray(new String[]{}));
		
		return return_value;
	}


	public address getAddressArrayShort(address source){
		address return_value=new address();
		return_value.setCodeCountry(source.getCodeCountry());
		return_value.setIdOblast(source.getIdOblast());
		
		ArrayList<String> countryListCode=new ArrayList<String>();
    	ArrayList<String> countryListName=new ArrayList<String>();		
    	ArrayList<String> oblastListCode=new ArrayList<String>();
    	ArrayList<String> oblastListName=new ArrayList<String>();
    	
    	setSessionId(source.getSessionId());
    	
    	PreparedStatement st_country = null;
    	PreparedStatement st_oblast = null;
    	
    	String lCodeCountry = source.getCodeCountry();
    	String lIdOblast = source.getIdOblast();
    	
    	if (isEmpty(lCodeCountry)) {
    		lCodeCountry = "-1";
		}
    	if (isEmpty(lIdOblast)) {
    		lIdOblast = "-1";
		}
    	
    	// -- Работаем со странами
    	try{
    		String countrySQL = 
	    		" SELECT code_country, substr(name_country,1,25) name_country " +
				"   FROM " + getGeneralDBScheme() + ".vc_country_all " +
				"  WHERE exist_flag = 'Y' " +
				"  ORDER BY name_country";
	    	LOGGER.debug("(country) " + countrySQL + ", 1={'" + lCodeCountry + "',string}");
	    	
	    	con = Connector.getConnection(source.getSessionId());
	    	st_country = con.prepareStatement(countrySQL);
            ResultSet rs_country = st_country.executeQuery();
            
	    	while(rs_country.next()){
	    		countryListCode.add(rs_country.getString("CODE_COUNTRY"));
	    		countryListName.add(rs_country.getString("NAME_COUNTRY"));
	    	}
	    	rs_country.close();
		} catch (SQLException e) {LOGGER.error("(oblast) SQLException: " + e.toString());
		} catch (Exception el) {LOGGER.error("(oblast) Exception: " + el.toString());}
		finally {
            try {
                if (st_oblast!=null) {
					st_oblast.close();
				}
            } catch (SQLException w) {w.toString();}
            try {
                if (con!=null) {
					con.close();
				}
            } catch (SQLException w) {w.toString();}
            Connector.closeConnection(con);
        } // finally
		
		return_value.setCountryListCode(countryListCode.toArray(new String[]{}));
		return_value.setCountryListName(countryListName.toArray(new String[]{}));
    	
    	// -- Работаем с областями
    	try{
    		String oblastSQL = 
	    		" SELECT id_oblast, name_oblast " +
				"   FROM " + getGeneralDBScheme() + ".vc_oblast_all " +
				"  WHERE cd_country = ? " +
				"  ORDER BY id_oblast";
	    	LOGGER.debug("(oblast) " + oblastSQL + ", 1={'" + lCodeCountry + "',string}" + ", 2={'" + lIdOblast + "',string}");
	    	
	    	con = Connector.getConnection(source.getSessionId());
	    	st_oblast = con.prepareStatement(oblastSQL);
	    	st_oblast.setString(1, lCodeCountry);
            ResultSet rs_oblast = st_oblast.executeQuery();
            
	    	while(rs_oblast.next()){
	    		oblastListCode.add(rs_oblast.getString("ID_OBLAST"));
	    		oblastListName.add(rs_oblast.getString("NAME_OBLAST"));
	    	}
	    	rs_oblast.close();
		} catch (SQLException e) {LOGGER.error("(oblast) SQLException: " + e.toString());
		} catch (Exception el) {LOGGER.error("(oblast) Exception: " + el.toString());}
		finally {
            try {
                if (st_oblast!=null) {
					st_oblast.close();
				}
            } catch (SQLException w) {w.toString();}
            try {
                if (con!=null) {
					con.close();
				}
            } catch (SQLException w) {w.toString();}
            Connector.closeConnection(con);
        } // finally
		
		return_value.setOblastListCode(oblastListCode.toArray(new String[]{}));
		return_value.setOblastListName(oblastListName.toArray(new String[]{}));
		
		return return_value;
	}


	public address getAddressArray(address source){
		address return_value=new address();
		return_value.setCodeCountry(source.getCodeCountry());
		return_value.setIdDistrict(source.getIdDistrict());
		return_value.setIdOblast(source.getIdOblast());
		return_value.setIdCity(source.getIdCity());
		return_value.setIdCityDistrict(source.getIdCityDistrict());
		
    	ArrayList<String> oblastListCode=new ArrayList<String>();
    	ArrayList<String> oblastListName=new ArrayList<String>();		
    	ArrayList<String> districtListCode=new ArrayList<String>();
    	ArrayList<String> districtListName=new ArrayList<String>();		
    	ArrayList<String> cityListCode=new ArrayList<String>();
    	ArrayList<String> cityListName=new ArrayList<String>();		
    	ArrayList<String> cityDistrictListCode=new ArrayList<String>();
    	ArrayList<String> cityDistrictListName=new ArrayList<String>();
    	
    	setSessionId(source.getSessionId());
    	
    	PreparedStatement st_oblast = null;
    	PreparedStatement st_district = null;
    	PreparedStatement st_city = null;
    	PreparedStatement st_city_district = null;
    	
    	String lCodeCountry = source.getCodeCountry();
    	String lIdOblast = source.getIdOblast();
    	String lIdDistrict = source.getIdDistrict();
    	
    	if (isEmpty(lCodeCountry)) {
    		lCodeCountry = "-1";
		}
    	if (isEmpty(lIdOblast)) {
    		lIdOblast = "-1";
		}
    	if (isEmpty(lIdDistrict)) {
    		lIdDistrict = "-1";
		}
    	
    	// -- Работаем с областями
    	try{
    		String oblastSQL = 
	    		" SELECT id_oblast, name_oblast " +
				"   FROM " + getGeneralDBScheme() + ".vc_oblast_all " +
				"  WHERE cd_country = ? " +
				"  ORDER BY id_oblast";
	    	LOGGER.debug("(oblast) " + oblastSQL + ", 1={'" + lCodeCountry + "',string}");
	    	
	    	con = Connector.getConnection(source.getSessionId());
	    	st_oblast = con.prepareStatement(oblastSQL);
	    	st_oblast.setString(1, lCodeCountry);
            ResultSet rs_oblast = st_oblast.executeQuery();
            
	    	while(rs_oblast.next()){
	    		oblastListCode.add(rs_oblast.getString("ID_OBLAST"));
	    		oblastListName.add(rs_oblast.getString("NAME_OBLAST"));
	    	}
	    	rs_oblast.close();
		} catch (SQLException e) {LOGGER.error("(oblast) SQLException: " + e.toString());
		} catch (Exception el) {LOGGER.error("(oblast) Exception: " + el.toString());}
		finally {
            try {
                if (st_oblast!=null) {
					st_oblast.close();
				}
            } catch (SQLException w) {w.toString();}
            try {
                if (con!=null) {
					con.close();
				}
            } catch (SQLException w) {w.toString();}
            Connector.closeConnection(con);
        } // finally
		
		return_value.setOblastListCode(oblastListCode.toArray(new String[]{}));
		return_value.setOblastListName(oblastListName.toArray(new String[]{}));
		
    	// -- Работаем с районами
		try{
			String districtSQL = 
	    		" SELECT id_district, name_district " +
				"   FROM " + getGeneralDBScheme() + ".vc_district_all " +
				"  WHERE cd_country = ? " +
				"    AND id_oblast = ? "+
				"  ORDER BY name_district";
	    	LOGGER.debug("(district) " + districtSQL + 
	    			", 1={'" + lCodeCountry + "',string}" + 
	    			", 2={'" + lIdOblast + "',int}");
	    	
	    	con = Connector.getConnection(source.getSessionId());
	    	st_district = con.prepareStatement(districtSQL);
	    	st_district.setString(1, lCodeCountry);
	    	st_district.setInt(2, Integer.parseInt(lIdOblast));
            ResultSet rs_district = st_district.executeQuery();
	    	
	    	while(rs_district.next()){
	    		districtListCode.add(rs_district.getString("ID_DISTRICT"));
	    		districtListName.add(rs_district.getString("NAME_DISTRICT"));
	    	}
	    	rs_district.close();
		} catch (SQLException e) {LOGGER.error("(district) SQLException: " + e.toString());
		} catch (Exception el) {LOGGER.error("(district) Exception: " + el.toString());}
		finally {
            try {
                if (st_district!=null) {
					st_district.close();
				}
            } catch (SQLException w) {w.toString();}
            try {
                if (con!=null) {
					con.close();
				}
            } catch (SQLException w) {w.toString();}
            Connector.closeConnection(con);
        } // finally
		
		return_value.setDistrictListCode(districtListCode.toArray(new String[]{}));
		return_value.setDistrictListName(districtListName.toArray(new String[]{}));

    	// -- Работаем с городами
		try{
			String citySQL = 
	    		" SELECT id_city, name_city " +
				"   FROM " + getGeneralDBScheme() + ".vc_city_all " +
				"  WHERE cd_country = ? " +
				"    AND id_oblast = ? "+
				"    AND NVL(id_district,-1) = ? " +
				"  ORDER BY name_city";
	    	LOGGER.debug("(city) " + citySQL + 
	    			", 1={'" + lCodeCountry + "',string}" + 
	    			", 2={'" + lIdOblast + "',int}" + 
	    			", 3={'" + lIdDistrict + "',int}");
	    	
	    	con = Connector.getConnection(source.getSessionId());
	    	st_city = con.prepareStatement(citySQL);
	    	st_city.setString(1, lCodeCountry);
	    	st_city.setInt(2, Integer.parseInt(lIdOblast));
	    	st_city.setInt(3, Integer.parseInt(lIdDistrict));
            
            ResultSet rs_city = st_city.executeQuery();
            
	    	while(rs_city.next()){
	    		cityListCode.add(rs_city.getString("ID_CITY"));
	    		cityListName.add(rs_city.getString("NAME_CITY"));
	    	}
	    	rs_city.close();
		} catch (SQLException e) {LOGGER.error("(city) SQLException: " + e.toString());
		} catch (Exception el) {LOGGER.error("(city) Exception: " + el.toString());}
		finally {
            try {
                if (st_city!=null) {
					st_city.close();
				}
            } catch (SQLException w) {w.toString();}
            try {
                if (con!=null) {
					con.close();
				}
            } catch (SQLException w) {w.toString();}
            Connector.closeConnection(con);
        } // finally
		   
    	return_value.setCityListCode(cityListCode.toArray(new String[]{}));
		return_value.setCityListName(cityListName.toArray(new String[]{}));
	
    	// -- Работаем с районами в городах
		try{
			String debugString = "";
			String cityDistrictSQL = 
	    		" SELECT id_city_district, SUBSTR(name_city_district,1,30) name_city_district " +
				"   FROM " + getGeneralDBScheme() + ".vc_city_district_all" +
	    		"  WHERE cd_country = ? " +
	    		"    AND id_oblast = ? "+
	    		"    AND NVL(id_district,-1) = ? " ;
        	if (!isEmpty(source.getIdCity())) {
        		cityDistrictSQL = cityDistrictSQL + " AND id_city = ? ";
    		}
        	cityDistrictSQL = cityDistrictSQL + "  ORDER BY name_city_district";
        	debugString = cityDistrictSQL + 
        		", 1={'" + lCodeCountry + "',string}" + 
				", 2={'" + lIdOblast + "',int}" + 
				", 3={'" + lIdDistrict + "',int}" ;
        	if (!isEmpty(source.getIdCity())) {
        		debugString = debugString + ", 4={'" + source.getIdCity() + "',int}";
    		}
	    	LOGGER.debug(debugString);
	    	
	    	con = Connector.getConnection(source.getSessionId());
	    	st_city_district = con.prepareStatement(cityDistrictSQL);
	    	st_city_district.setString(1, lCodeCountry);
	    	st_city_district.setInt(2, Integer.parseInt(lIdOblast));
	    	st_city_district.setInt(3, Integer.parseInt(lIdDistrict));
	    	if (!isEmpty(source.getIdCity())) {
	    		st_city_district.setInt(4, Integer.parseInt(source.getIdCity()));
	    	}
            
            ResultSet rs_city_district = st_city_district.executeQuery();
	    	
	    	while(rs_city_district.next()){
	    		cityDistrictListCode.add(rs_city_district.getString("ID_CITY_DISTRICT"));
	    		cityDistrictListName.add(rs_city_district.getString("NAME_CITY_DISTRICT"));
	    	}
	    	rs_city_district.close();
		} catch (SQLException e) {LOGGER.error("(city district) SQLException: " + e.toString());
		} catch (Exception el) {LOGGER.error("(city district) Exception: " + el.toString());}
		finally {
            try {
                if (st_city_district!=null) {
					st_city_district.close();
				}
            } catch (SQLException w) {w.toString();}
            try {
                if (con!=null) {
					con.close();
				}
            } catch (SQLException w) {w.toString();}
            Connector.closeConnection(con);
        } // finally
		
		return_value.setCityDistrictListCode(cityDistrictListCode.toArray(new String[]{}));
		return_value.setCityDistrictListName(cityDistrictListName.toArray(new String[]{}));
		
		return return_value;
	}

	public address getOblastArray(address source){
		address return_value=new address();
		return_value.setCodeCountry(source.getCodeCountry());
		return_value.setIdOblast(source.getIdOblast());
		
    	ArrayList<String> oblastListCode=new ArrayList<String>();
    	ArrayList<String> oblastListName=new ArrayList<String>();	
    	
    	PreparedStatement st_oblast = null;
    	
    	setSessionId(source.getSessionId());

    	// -- Работаем с областями
		try{
			String oblastSQL = 
	    		" SELECT id_oblast, name_oblast " +
				"   FROM " + getGeneralDBScheme() + ".vc_oblast_all " +
				"  WHERE cd_country = ? " +
				"  ORDER BY id_oblast";
	    	LOGGER.debug("(oblast) " + oblastSQL + 
	    			", 1={'" + source.getCodeCountry() + "',string}");
	    	
	    	con = Connector.getConnection(source.getSessionId());
	    	st_oblast = con.prepareStatement(oblastSQL);
	    	st_oblast.setString(1, source.getCodeCountry());
            
            ResultSet rs_oblast = st_oblast.executeQuery();
            
	    	while(rs_oblast.next()){
	    		oblastListCode.add(rs_oblast.getString("ID_OBLAST"));
	    		oblastListName.add(rs_oblast.getString("NAME_OBLAST"));
	    	}
	    	rs_oblast.close();
		} catch (SQLException e) {LOGGER.error("(oblast) SQLException: " + e.toString());
		} catch (Exception el) {LOGGER.error("(oblast) Exception: " + el.toString());}
		finally {
            try {
                if (st_oblast!=null) {
					st_oblast.close();
				}
            } catch (SQLException w) {w.toString();}
            try {
                if (con!=null) {
					con.close();
				}
            } catch (SQLException w) {w.toString();}
            Connector.closeConnection(con);
        } // finally
		
		return_value.setOblastListCode(oblastListCode.toArray(new String[]{}));
		return_value.setOblastListName(oblastListName.toArray(new String[]{}));
		
		return return_value;
	}

    public transport getFAQList(transport mySource) {
    	
    	bc.service.pages page = new bc.service.pages(mySource.getPageNumber(), mySource.getPageRowCount());
    	String l_beg = page.getFirstRowNumber();
    	String l_end = page.getLastRowNumber();
    	
    	transport return_value=new transport();
		return_value.setFunctionName(mySource.getFunctionName());
		return_value.setIdDestination(mySource.getIdDestination());
		return_value.setFunctionParam(mySource.getFunctionParam());
		
		String findString = mySource.getFunctionParam();
		
		StringBuilder html = new StringBuilder();
		
		ArrayList<bcFeautureParam> pParam = initParamArray();

		String mySQL = 
        	" SELECT cd_cc_faq, name_cc_faq_category, title_cc_faq, question_cc_faq, answer_cc_faq, id_cc_faq  " +
	  		"   FROM (SELECT ROWNUM rn, cd_cc_faq, name_cc_faq_category, title_cc_faq, question_cc_faq, answer_cc_faq, id_cc_faq " +
  			"           FROM (SELECT * " +
  			"                   FROM " + getGeneralDBScheme() + ".vc_cc_faq_club_all ";
        if (!isEmpty(findString)) {
        	mySQL = mySQL +
  			"          WHERE (UPPER(cd_cc_faq) LIKE UPPER('%'||?||'%') OR " +
  			"                 UPPER(title_cc_faq) LIKE UPPER('%'||?||'%') OR " +
  			"                 UPPER(question_cc_faq) LIKE UPPER('%'||?||'%') OR " +
  			"                 UPPER(answer_cc_faq) LIKE UPPER('%'||?||'%')) ";
        	for (int i=0; i<4; i++) {
        	    pParam.add(new bcFeautureParam("string", findString));
        	}
        }
        pParam.add(new bcFeautureParam("int", l_end));
        pParam.add(new bcFeautureParam("int", l_beg));
        mySQL = mySQL +
  			"                  ORDER BY title_cc_faq" +
  			"        ) WHERE ROWNUM < ? " + 
  			" ) WHERE rn >= ?";
        
        LOGGER.debug(prepareSQLToLog(mySQL, pParam));
        PreparedStatement st = null;
        String idFAQ = "";
        String cdFAQ = "";
        String titleFAQ = "";
        String markedCdFAQ = "";
        String markedTitleFAQ = "";
        
        try{
        	con = Connector.getConnection(mySource.getSessionId());
            st = con.prepareStatement(mySQL);
            st = prepareParam(st, pParam);
            ResultSet rset = st.executeQuery();
            int rowCount = 0;
            while (rset.next()){
            	idFAQ = getHTMLValue(rset.getString("ID_CC_FAQ"));
            	cdFAQ = getHTMLValue(rset.getString("CD_CC_FAQ"));
                titleFAQ = getHTMLValue(rset.getString("TITLE_CC_FAQ"));
                markedCdFAQ = prepareFindString(cdFAQ, findString);
                markedTitleFAQ = prepareFindString(titleFAQ, findString);
                
                html.append(
                        "<tr><td><div class=\"div_find\" onClick=\"changeOpener('"+idFAQ+"','"+cdFAQ + " - "+titleFAQ/*+"','"+questionFAQ+"','"+answerFAQ*/+"')\">"+getValue2(markedCdFAQ) +"</div></td>" +
                        "<td><div class=\"div_find\" onClick=\"changeOpener('"+idFAQ+"','"+cdFAQ + " - "+titleFAQ/*+"','"+questionFAQ+"','"+answerFAQ*/+"')\">"+getValue2(rset.getString("NAME_CC_FAQ_CATEGORY"))+"</div></td>" +
                        "<td><div class=\"div_find_marked\" onClick=\"changeOpener('"+idFAQ+"','"+cdFAQ + " - "+titleFAQ/*+"','"+questionFAQ+"','"+answerFAQ*/+"')\">"+getValue2(markedTitleFAQ)+"</div></td>" +
                        "<td><div class=\"div_find\" onClick=\"changeOpener('"+idFAQ+"','"+cdFAQ + " - "+titleFAQ/*+"','"+questionFAQ+"','"+answerFAQ*/+"')\">"+getValue2(rset.getString("QUESTION_CC_FAQ"))+"</div></td>" +
                        "<td><div class=\"div_find\" onClick=\"changeOpener('"+idFAQ+"','"+cdFAQ + " - "+titleFAQ/*+"','"+questionFAQ+"','"+answerFAQ*/+"')\">"+getValue2(rset.getString("ANSWER_CC_FAQ"))+"</div></td></tr>");
                       
                rowCount++;
            }
            if (rowCount == 0) {
            	html.append(getNoDataFountTR("5"));
            }
        } catch (SQLException e) {LOGGER.error(html, e);html.append(prepareSQLToLog(mySQL, pParam) + "<br>" + e.toString());
        } catch (Exception el) {LOGGER.error(html, el);html.append(prepareSQLToLog(mySQL, pParam) + "<br>" + el.toString());}
        finally {
            try {
                if (st!=null) {
					st.close();
				}
            } catch (SQLException w) {w.toString();}
            try {
                if (con!=null) {
					con.close();
				}
            } catch (SQLException w) {w.toString();}
            Connector.closeConnection(con);
        } // finally

        html.append("</tbody>");
        html.append("</table>");
        
        return_value.setFunctionHTMLReturnValue(html.toString());
        return return_value;
    } // getUsersList

	public relations getClubRelations(relations source){
		relations return_value=new relations();
		return_value.setIdClubRel(source.getIdClubRel());
		return_value.setIdDoc(source.getIdDoc());
		return_value.setIdComission(source.getIdComission());
		return_value.setCdComissionType(source.getCdComissionType());
		
    	ArrayList<String> documentListId=new ArrayList<String>();
    	ArrayList<String> documentListName=new ArrayList<String>();		
    	ArrayList<String> comissionListId=new ArrayList<String>();
    	ArrayList<String> comissionListName=new ArrayList<String>();		
    	ArrayList<String> comissionTypeListCode=new ArrayList<String>();
    	ArrayList<String> comissionTypeListName=new ArrayList<String>();		
    	
    	PreparedStatement st_doc = null;
    	PreparedStatement st_comis = null;
    	PreparedStatement st_type = null;
    	
    	setSessionId(source.getSessionId());
    	String generalDBScheme = getGeneralDBScheme();

		con = Connector.getConnection(source.getSessionId());
    	
    	String docSQL = "";    	
    	String comisSQL = "";    	
    	String typeSQL = "";

    	// -- Работаем с документами
		try{
			docSQL = 
	    		" SELECT id_doc, full_doc " +
				"   FROM " + generalDBScheme + ".vc_doc_priv_all " +
				"  WHERE id_club_rel = ? " +
				"  ORDER BY full_doc";
	    	LOGGER.debug("(doc) " + docSQL + 
	    			", 1={'" + source.getIdClubRel() + "',int}");
	    	
	    	st_doc = con.prepareStatement(docSQL);
	    	st_doc.setInt(1, Integer.parseInt(source.getIdClubRel()));
	    	ResultSet rs_doc = st_doc.executeQuery(); 
	    	while(rs_doc.next()){
	    		documentListId.add(rs_doc.getString("ID_DOC"));
	    		documentListName.add(rs_doc.getString("FULL_DOC"));
	    	}
	    	rs_doc.close();
		} catch (SQLException e) {LOGGER.error("(doc) SQLException: " + docSQL + ": " + e.toString());
		} catch (Exception el) {LOGGER.error("(doc) Exception: " + el.toString());}
		finally {
            try {
                if (st_doc!=null) {
					st_doc.close();
				}
            } catch (SQLException w) {w.toString();}
            try {
                if (con!=null) {
					con.close();
				}
            } catch (SQLException w) {w.toString();}
            Connector.closeConnection(con);
        } // finally
		
		con = Connector.getConnection(source.getSessionId());
	
		return_value.setDocumentListId(documentListId.toArray(new String[]{}));
		return_value.setDocumentListName(documentListName.toArray(new String[]{}));
		
    	// -- Работаем с комиссиями
		try{
			comisSQL = 
	    		" SELECT id_comission, name_comission_type " +
				"   FROM " + generalDBScheme + ".vc_jur_prs_comission_priv_all " +
				"  WHERE id_club_rel = ? " +
				"  ORDER BY name_comission_type";
	    	LOGGER.debug("(comission) " + comisSQL + 
	    			", 1={'" + source.getIdClubRel() + "',int}");
	    	
	    	st_comis = con.prepareStatement(comisSQL);
	    	st_comis.setInt(1, Integer.parseInt(source.getIdClubRel()));
	    	ResultSet rs_comis = st_comis.executeQuery(); 
	    	while(rs_comis.next()){
	    		comissionListId.add(rs_comis.getString("ID_DISTRICT"));
	    		comissionListName.add(rs_comis.getString("NAME_DISTRICT"));
	    	}
	    	rs_comis.close();
		} catch (SQLException e) {LOGGER.error("(comis) SQLException: " + e.toString());
		} catch (Exception el) {LOGGER.error("(comis) Exception: " + el.toString());}
		finally {
            try {
                if (st_comis!=null) {
					st_comis.close();
				}
            } catch (SQLException w) {w.toString();}
            try {
                if (con!=null) {
					con.close();
				}
            } catch (SQLException w) {w.toString();}
            Connector.closeConnection(con);
        } // finally
		
		con = Connector.getConnection(source.getSessionId());
		   
		return_value.setComissionListId(comissionListId.toArray(new String[]{}));
		return_value.setComissionListId(comissionListId.toArray(new String[]{}));

    	// -- Работаем с типами комиссий
		try{
			typeSQL = 
				" SELECT cd_comission_type, name_club_rel_type||'.'||name_comission_type name_comission_type " +
				"   FROM " + generalDBScheme+".vc_comission_type_club_all " +
				"  WHERE cd_club_rel_type IN " +
				"      (SELECT cd_club_rel_type " +
				"         FROM " + generalDBScheme+".vc_club_rel_club_all a " +
				"        WHERE id_club_rel = ?) " +
				" ORDER BY name_club_rel_type||'.'||name_comission_type";
	    	LOGGER.debug("(type) " + typeSQL + 
	    			", 1={'" + source.getIdClubRel() + "',int}");
	    	
	    	st_type = con.prepareStatement(typeSQL);
	    	st_type.setInt(1, Integer.parseInt(source.getIdClubRel()));
	    	ResultSet rs_type = st_type.executeQuery(); 
	    	while(rs_type.next()){
	    		comissionTypeListCode.add(rs_type.getString("ID_CITY"));
	    		comissionTypeListName.add(rs_type.getString("NAME_CITY"));
	    	}
	    	rs_type.close();
		} catch (SQLException e) {LOGGER.error("(type) SQLException: " + e.toString());
		} catch (Exception el) {LOGGER.error("(type) Exception: " + el.toString());}
		finally {
            try {
                if (st_type!=null) {
					st_type.close();
				}
            } catch (SQLException w) {w.toString();}
            try {
                if (con!=null) {
					con.close();
				}
            } catch (SQLException w) {w.toString();}
            Connector.closeConnection(con);
        } // finally
		con = Connector.getConnection(source.getSessionId());
		   
    	return_value.setComissionTypeListCode(comissionTypeListCode.toArray(new String[]{}));
		return_value.setComissionTypeListName(comissionTypeListName.toArray(new String[]{}));
	
		return return_value;
	}


    public transport getQuestionnairePackList(transport mySource) {
    	
    	bc.service.pages page = new bc.service.pages(mySource.getPageNumber(), mySource.getPageRowCount());
    	String l_beg = page.getFirstRowNumber();
    	String l_end = page.getLastRowNumber();
    	
    	transport return_value=new transport();
		return_value.setFunctionName(mySource.getFunctionName());
		return_value.setIdDestination(mySource.getIdDestination());
		return_value.setFunctionParam(mySource.getFunctionParam());
		
		String findString = mySource.getFunctionParam();
		
		StringBuilder html = new StringBuilder();
		
		ArrayList<bcFeautureParam> pParam = initParamArray();

		String mySQL = 
        	" SELECT rn, id_quest_pack, date_reception_pack, sname_jur_pr_who_has_sold_card, " +
        	"        name_serv_plce_where_card_sold, state_pack " +
			"   FROM (SELECT ROWNUM rn, id_quest_pack, date_reception_pack_frmt date_reception_pack, " +
			"                sname_jur_pr_who_has_sold_card," +
			"                name_serv_plce_where_card_sold, state_pack_tsl state_pack " +
			"   		FROM (SELECT * " +
			"                   FROM " + getGeneralDBScheme()+".vc_quest_pack_club_all " +
			"                  WHERE state_pack = 'OPENED' ";
        if (!isEmpty(findString)) {
        	mySQL = mySQL +
  			"           AND (TO_CHAR(id_quest_pack) LIKE '%'||?||'%' OR " +
  			"                 UPPER(date_reception_pack) LIKE UPPER('%'||?||'%')) ";
        	for (int i=0; i<2; i++) {
        	    pParam.add(new bcFeautureParam("string", findString));
        	}
        }
        pParam.add(new bcFeautureParam("int", l_end));
        pParam.add(new bcFeautureParam("int", l_beg));
        mySQL = mySQL +
        	"                  ORDER BY id_quest_pack DESC" +
        	"        ) WHERE ROWNUM < ? " + 
  			" ) WHERE rn >= ?";
        
        LOGGER.debug(prepareSQLToLog(mySQL, pParam));
        PreparedStatement st = null;
        String idPack = "";
        String datePack = "";
        String markedIdPack = "";
        String markedDatePack = "";
        
        try{
        	con = Connector.getConnection(mySource.getSessionId());
            st = con.prepareStatement(mySQL);
            st = prepareParam(st, pParam);
            ResultSet rset = st.executeQuery();
            int rowCount = 0;
            while (rset.next()){
            	idPack = getHTMLValue(rset.getString("ID_QUEST_PACK"));
            	datePack = getHTMLValue(rset.getString("DATE_RECEPTION_PACK"));
            	markedIdPack = prepareFindString(idPack, findString);
            	markedDatePack = prepareFindString(datePack, findString);
                
                html.append(
                        "<tr>" +
                        "<td><div class=\"div_find\" onClick=\"changeOpener('"+idPack+"','"+idPack+"')\">"+getValue2(markedIdPack) +"</div></td>" +
                        "<td><div class=\"div_find\" onClick=\"changeOpener('"+idPack+"','"+idPack+"')\">"+getValue2(markedDatePack)+"</div></td>" +
                        "<td><div class=\"div_find\" onClick=\"changeOpener('"+idPack+"','"+idPack+"')\">"+getValue2(rset.getString("SNAME_JUR_PR_WHO_HAS_SOLD_CARD"))+"</div></td>" +
                        "<td><div class=\"div_find\" onClick=\"changeOpener('"+idPack+"','"+idPack+"')\">"+getValue2(rset.getString("NAME_SERV_PLCE_WHERE_CARD_SOLD"))+"</div></td>" +
                        "<td><div class=\"div_find\" onClick=\"changeOpener('"+idPack+"','"+idPack+"')\">"+getValue2(rset.getString("STATE_PACK"))+"</div></td>" +
                        "</tr>");
                       
                rowCount++;
            }
            if (rowCount == 0) {
            	html.append(getNoDataFountTR("7"));
            }
        } catch (SQLException e) {LOGGER.error(html, e);html.append(prepareSQLToLog(mySQL, pParam) + "<br>" + e.toString());
        } catch (Exception el) {LOGGER.error(html, el);html.append(prepareSQLToLog(mySQL, pParam) + "<br>" + el.toString());}
        finally {
            try {
                if (st!=null) {
					st.close();
				}
            } catch (SQLException w) {w.toString();}
            try {
                if (con!=null) {
					con.close();
				}
            } catch (SQLException w) {w.toString();}
            Connector.closeConnection(con);
        } // finally

        html.append("</tbody>");
        html.append("</table>");
        
        return_value.setFunctionHTMLReturnValue(html.toString());
        return return_value;
    } // getGiftsList


    public transport getClubRelationshipsList(transport mySource) {
    	
    	bc.service.pages page = new bc.service.pages(mySource.getPageNumber(), mySource.getPageRowCount());
    	String l_beg = page.getFirstRowNumber();
    	String l_end = page.getLastRowNumber();
    	
    	transport return_value=new transport();
		return_value.setFunctionName(mySource.getFunctionName());
		return_value.setIdDestination(mySource.getIdDestination());
		return_value.setFunctionParam(mySource.getFunctionParam());
		
		String findString = mySource.getFunctionParam();
		String idClubRelInput = mySource.getFunctionParam2();
		String idJurPrs = mySource.getFunctionParam3();
		
		StringBuilder html = new StringBuilder();
		
		ArrayList<bcFeautureParam> pParam = initParamArray();

		String mySQL = 
        	" SELECT rn, id_club_rel, name_club_rel, name_club_rel_type, full_name_club_rel " +
      	  	"   FROM (SELECT ROWNUM rn, id_club_rel, name_club_rel, name_club_rel_type, full_name_club_rel " +
			"   		FROM (SELECT * " +
			"                   FROM " + getGeneralDBScheme()+".vc_club_rel_club_all " +
			"                  WHERE 1 = 1 ";
        if (!isEmpty(findString)) {
        	mySQL = mySQL +
  			"             AND (UPPER(name_club_rel) LIKE UPPER('%'||?||'%') OR " +
  			"				   UPPER(full_name_club_rel) LIKE UPPER('%'||?||'%'))";
        	for (int i=0; i<2; i++) {
        	    pParam.add(new bcFeautureParam("string", findString));
        	}
        }
        if (!isEmpty(idJurPrs)) {
        	mySQL = mySQL + 
			"  AND (id_party1 = ? " + 
			"       OR id_party2 = ? " + 
			"      )";
        	pParam.add(new bcFeautureParam("int", idJurPrs));
        	pParam.add(new bcFeautureParam("int", idJurPrs));
        }
        pParam.add(new bcFeautureParam("int", l_end));
        pParam.add(new bcFeautureParam("int", l_beg));
        mySQL = mySQL +
        	"                  ORDER BY name_club_rel" +
        	"        ) WHERE ROWNUM < ? " + 
  			" ) WHERE rn >= ?";
        
        LOGGER.debug(prepareSQLToLog(mySQL, pParam));
        PreparedStatement st = null;
        String idClubRel = "";
        String nameClubRel = "";
        String fullNameClubRel = "";
        String markedNameClubRel = "";
        
        try{
        	con = Connector.getConnection(mySource.getSessionId());
            st = con.prepareStatement(mySQL);
            st = prepareParam(st, pParam);
            ResultSet rset = st.executeQuery();
            int rowCount = 0;
            while (rset.next()){
            	idClubRel = getHTMLValue(rset.getString("ID_CLUB_REL"));
            	nameClubRel = getHTMLValue(rset.getString("NAME_CLUB_REL"));
            	fullNameClubRel = getHTMLValue(rset.getString("FULL_NAME_CLUB_REL"));
            	markedNameClubRel = prepareFindString(nameClubRel, findString);
                if (!isEmpty(idClubRelInput)) {
                	if (idClubRel == idClubRelInput) {
                		markedNameClubRel = "<b>" + nameClubRel.toUpperCase() + "</b>";
                	}
                }
                
                html.append(
                        "<tr>" +
                        "<td align=\"center\"><div class=\"div_find\" onClick=\"changeOpener('"+idClubRel+"','"+fullNameClubRel+"')\">"+getValue2(idClubRel) +"</div></td>" +
                        "<td><div class=\"div_find_marked\" onClick=\"changeOpener('"+idClubRel+"','"+fullNameClubRel+"')\">"+getValue2(markedNameClubRel)+"</div></td>" +
                        "<td><div class=\"div_find\" onClick=\"changeOpener('"+idClubRel+"','"+fullNameClubRel+"')\">"+getValue2(rset.getString("NAME_CLUB_REL_TYPE"))+"</div></td>" +
                        "</tr>");
                       
                rowCount++;
            }
            if (rowCount == 0) {
            	html.append(getNoDataFountTR("3"));
            }
        } catch (SQLException e) {LOGGER.error(html, e);html.append(prepareSQLToLog(mySQL, pParam) + "<br>" + e.toString());
        } catch (Exception el) {LOGGER.error(html, el);html.append(prepareSQLToLog(mySQL, pParam) + "<br>" + el.toString());}
        finally {
            try {
                if (st!=null) {
					st.close();
				}
            } catch (SQLException w) {w.toString();}
            try {
                if (con!=null) {
					con.close();
				}
            } catch (SQLException w) {w.toString();}
            Connector.closeConnection(con);
        } // finally

        html.append("</tbody>");
        html.append("</table>");
        
        return_value.setFunctionHTMLReturnValue(html.toString());
        return return_value;
    } // getGiftsList


    public transport getDSClientPatternList(transport mySource) {
    	
    	bc.service.pages page = new bc.service.pages(mySource.getPageNumber(), mySource.getPageRowCount());
    	String l_beg = page.getFirstRowNumber();
    	String l_end = page.getLastRowNumber();
    	
    	transport return_value=new transport();
		return_value.setFunctionName(mySource.getFunctionName());
		return_value.setIdDestination(mySource.getIdDestination());
		return_value.setFunctionParam(mySource.getFunctionParam());
		
		String findString = mySource.getFunctionParam();
		String idPatternInput = mySource.getFunctionParam2();
		
		StringBuilder html = new StringBuilder();
		
		ArrayList<bcFeautureParam> pParam = initParamArray();

		String mySQL = 
        	"SELECT rn, id_pattern, name_pattern, " +
        	"       text_message, begin_action_date_frmt, end_action_date_frmt " +
	  		"  FROM (SELECT ROWNUM rn, id_cl_pattern id_pattern, " +
	  		"               name_cl_pattern name_pattern, " +
	  		"               text_message, begin_action_date_frmt, end_action_date_frmt " +
	  		"          FROM (SELECT * " +
	  		"                  FROM " + getGeneralDBScheme()+".vc_ds_cl_pattern_club_all " +
	  		"                 WHERE 1 = 1";
        if (!isEmpty(findString)) {
        	mySQL = mySQL +
  			"             AND (UPPER(id_cl_pattern) LIKE UPPER('%'||?||'%') OR " +
  			"				   UPPER(name_cl_pattern) LIKE UPPER('%'||?||'%'))";
        	for (int i=0; i<2; i++) {
        	    pParam.add(new bcFeautureParam("string", findString));
        	}
        }
        if (!isEmpty(idPatternInput)) {
        	mySQL = mySQL + " AND id_cl_pattern = ? ";
        	pParam.add(new bcFeautureParam("int", idPatternInput));
        }
        pParam.add(new bcFeautureParam("int", l_end));
        pParam.add(new bcFeautureParam("int", l_beg));
        mySQL = mySQL +
        	"                  ORDER BY name_cl_pattern" +
        	"        ) WHERE ROWNUM < ? " + 
  			" ) WHERE rn >= ?";
        
        LOGGER.debug(prepareSQLToLog(mySQL, pParam));
        PreparedStatement st = null;
        String idPattern = "";
        String namePattern = "";
        String markedNamePattern = "";
        
        try{
        	con = Connector.getConnection(mySource.getSessionId());
            st = con.prepareStatement(mySQL);
            st = prepareParam(st, pParam);
            ResultSet rset = st.executeQuery();
            int rowCount = 0;
            while (rset.next()){
            	idPattern = getHTMLValue(rset.getString("ID_PATTERN"));
            	namePattern = getHTMLValue(rset.getString("NAME_PATTERN"));
            	markedNamePattern = prepareFindString(namePattern, findString);
                if (!isEmpty(idPatternInput)) {
                	if (idPattern == idPatternInput) {
                		markedNamePattern = "<b>" + namePattern.toUpperCase() + "</b>";
                	}
                }
                
                html.append(
                        "<tr>" +
                        "<td align=\"center\"><div class=\"div_find\" onClick=\"changeOpener('"+idPattern+"','"+namePattern+"')\">"+getValue2(idPattern) +"</div></td>" +
                        "<td><div class=\"div_find_marked\" onClick=\"changeOpener('"+idPattern+"','"+namePattern+"')\">"+getValue2(markedNamePattern)+"</div></td>" +
                        "<td><div class=\"div_find\" onClick=\"changeOpener('"+idPattern+"','"+namePattern+"')\">"+getValue2(rset.getString("TEXT_MESSAGE"))+"</div></td>" +
                        "<td><div class=\"div_find\" onClick=\"changeOpener('"+idPattern+"','"+namePattern+"')\">"+getValue2(rset.getString("BEGIN_ACTION_DATE_FRMT"))+"</div></td>" +
                        "<td><div class=\"div_find\" onClick=\"changeOpener('"+idPattern+"','"+namePattern+"')\">"+getValue2(rset.getString("END_ACTION_DATE_FRMT"))+"</div></td>" +
                        "</tr>");
                       
                rowCount++;
            }
            if (rowCount == 0) {
            	html.append(getNoDataFountTR("8"));
            }
        } catch (SQLException e) {LOGGER.error(html, e);html.append(prepareSQLToLog(mySQL, pParam) + "<br>" + e.toString());
        } catch (Exception el) {LOGGER.error(html, el);html.append(prepareSQLToLog(mySQL, pParam) + "<br>" + el.toString());}
        finally {
            try {
                if (st!=null) {
					st.close();
				}
            } catch (SQLException w) {w.toString();}
            try {
                if (con!=null) {
					con.close();
				}
            } catch (SQLException w) {w.toString();}
            Connector.closeConnection(con);
        } // finally

        html.append("</tbody>");
        html.append("</table>");
        
        return_value.setFunctionHTMLReturnValue(html.toString());
        return return_value;
    } // getGiftsList

    public transport getDSEmailProfileList(transport mySource) {
    	
    	bc.service.pages page = new bc.service.pages(mySource.getPageNumber(), mySource.getPageRowCount());
    	String l_beg = page.getFirstRowNumber();
    	String l_end = page.getLastRowNumber();
    	
    	transport return_value=new transport();
		return_value.setFunctionName(mySource.getFunctionName());
		return_value.setIdDestination(mySource.getIdDestination());
		return_value.setFunctionParam(mySource.getFunctionParam());
		
		String findString = mySource.getFunctionParam();
		String idProfileInput = mySource.getFunctionParam3();
		
		StringBuilder html = new StringBuilder();
		
		ArrayList<bcFeautureParam> pParam = initParamArray();

		String mySQL = 
        	" SELECT rn, id_email_profile, name_email_profile, smtp_server, smtp_port, smtp_ssl, " +
        	"        need_autorization, smtp_user, delay_next_letter " +
      	  	"   FROM (SELECT ROWNUM rn, id_email_profile, name_email_profile, smtp_server, smtp_port, smtp_ssl_tsl smtp_ssl, " +
      	  	"                need_autorization_tsl need_autorization, smtp_user, delay_next_letter" +
			"   		FROM (SELECT * " +
			"                   FROM " + getGeneralDBScheme()+".vc_ds_email_profile_club_all " +
			"                  WHERE 1 = 1 ";
        if (!isEmpty(findString)) {
        	mySQL = mySQL +
  			"             AND (TO_CHAR(id_email_profile) LIKE UPPER('%'||?||'%') OR " +
  			"				   UPPER(name_email_profile) LIKE UPPER('%'||?||'%'))";
        	for (int i=0; i<2; i++) {
        	    pParam.add(new bcFeautureParam("string", findString));
        	}
        }
        pParam.add(new bcFeautureParam("int", l_end));
        pParam.add(new bcFeautureParam("int", l_beg));
        mySQL = mySQL +
        	"                  ORDER BY name_email_profile" +
        	"        ) WHERE ROWNUM < ? " + 
  			" ) WHERE rn >= ?";
        
        LOGGER.debug(prepareSQLToLog(mySQL, pParam));
        PreparedStatement st = null;
        String idProfile = "";
        String nameProfile = "";
        String markedNameProfile = "";
        
        try{
        	con = Connector.getConnection(mySource.getSessionId());
            st = con.prepareStatement(mySQL);
            st = prepareParam(st, pParam);
            ResultSet rset = st.executeQuery();
            int rowCount = 0;
            while (rset.next()){
            	idProfile = getHTMLValue(rset.getString("ID_EMAIL_PROFILE"));
            	nameProfile = getHTMLValue(rset.getString("NAME_EMAIL_PROFILE"));
            	markedNameProfile = prepareFindString(nameProfile, findString);
                if (!isEmpty(idProfileInput)) {
                	if (idProfile == idProfileInput) {
                		markedNameProfile = "<b>" + nameProfile.toUpperCase() + "</b>";
                	}
                }
                
                html.append(
                        "<tr>" +
                        "<td align=\"center\"><div class=\"div_find\" onClick=\"changeOpener('"+idProfile+"','"+nameProfile+"')\">"+getValue2(idProfile) +"</div></td>" +
                        "<td><div class=\"div_find_marked\" onClick=\"changeOpener('"+idProfile+"','"+nameProfile+"')\">"+getValue2(markedNameProfile)+"</div></td>" +
                        "<td><div class=\"div_find\" onClick=\"changeOpener('"+idProfile+"','"+nameProfile+"')\">"+getValue2(rset.getString("SMTP_SERVER"))+"</div></td>" +
                        "<td><div class=\"div_find\" onClick=\"changeOpener('"+idProfile+"','"+nameProfile+"')\">"+getValue2(rset.getString("SMTP_PORT"))+"</div></td>" +
                        "<td><div class=\"div_find\" onClick=\"changeOpener('"+idProfile+"','"+nameProfile+"')\">"+getValue2(rset.getString("SMTP_SSL"))+"</div></td>" +
                        "<td><div class=\"div_find\" onClick=\"changeOpener('"+idProfile+"','"+nameProfile+"')\">"+getValue2(rset.getString("NEED_AUTORIZATION"))+"</div></td>" +
                        "<td><div class=\"div_find\" onClick=\"changeOpener('"+idProfile+"','"+nameProfile+"')\">"+getValue2(rset.getString("SMTP_USER"))+"</div></td>" +
                        "<td><div class=\"div_find\" onClick=\"changeOpener('"+idProfile+"','"+nameProfile+"')\">"+getValue2(rset.getString("DELAY_NEXT_LETTER"))+"</div></td>" +
                        "</tr>");
                       
                rowCount++;
            }
            if (rowCount == 0) {
            	html.append(getNoDataFountTR("8"));
            }
        } catch (SQLException e) {LOGGER.error(html, e);html.append(prepareSQLToLog(mySQL, pParam) + "<br>" + e.toString());
        } catch (Exception el) {LOGGER.error(html, el);html.append(prepareSQLToLog(mySQL, pParam) + "<br>" + el.toString());}
        finally {
            try {
                if (st!=null) {
					st.close();
				}
            } catch (SQLException w) {w.toString();}
            try {
                if (con!=null) {
					con.close();
				}
            } catch (SQLException w) {w.toString();}
            Connector.closeConnection(con);
        } // finally

        html.append("</tbody>");
        html.append("</table>");
        
        return_value.setFunctionHTMLReturnValue(html.toString());
        return return_value;
    } // getGiftsList

    public transport getDSClientProfileList(transport mySource) {
    	
    	bc.service.pages page = new bc.service.pages(mySource.getPageNumber(), mySource.getPageRowCount());
    	String l_beg = page.getFirstRowNumber();
    	String l_end = page.getLastRowNumber();
    	
    	transport return_value=new transport();
		return_value.setFunctionName(mySource.getFunctionName());
		return_value.setIdDestination(mySource.getIdDestination());
		return_value.setFunctionParam(mySource.getFunctionParam());
		
		String findString = mySource.getFunctionParam();
		String idProfileInput = mySource.getFunctionParam3();
		
		StringBuilder html = new StringBuilder();
		
		ArrayList<bcFeautureParam> pParam = initParamArray();

		String mySQL = 
        	" SELECT rn, id_ds_cl_profile, name_ds_cl_profile, sname_issuer, name_card_status, name_bon_category," +
        	"        name_disc_category, sex_nat_prs_tsl, name_nat_prs_group " +
      	  	"   FROM (SELECT ROWNUM rn, id_ds_cl_profile, name_ds_cl_profile, sname_issuer, name_card_status, name_bon_category, " +
      	  	"                name_disc_category, sex_nat_prs_tsl, name_nat_prs_group" +
			"   		FROM (SELECT * " +
			"                   FROM " + getGeneralDBScheme()+".vc_ds_cl_profile_club_all " +
			"                  WHERE 1 = 1 ";
        if (!isEmpty(findString)) {
        	mySQL = mySQL +
  			"             AND (UPPER(id_ds_cl_profile) LIKE UPPER('%'||?||'%') OR " +
  			"				   UPPER(name_ds_cl_profile) LIKE UPPER('%'||?||'%'))";
        	for (int i=0; i<2; i++) {
        	    pParam.add(new bcFeautureParam("string", findString));
        	}
        }
        pParam.add(new bcFeautureParam("int", l_end));
        pParam.add(new bcFeautureParam("int", l_beg));
        mySQL = mySQL +
        	"                  ORDER BY name_ds_cl_profile" +
        	"        ) WHERE ROWNUM < ? " + 
  			" ) WHERE rn >= ?";
        
        LOGGER.debug(prepareSQLToLog(mySQL, pParam));
        PreparedStatement st = null;
        String idProfile = "";
        String nameProfile = "";
        String markedNameProfile = "";
        
        try{
        	con = Connector.getConnection(mySource.getSessionId());
            st = con.prepareStatement(mySQL);
            st = prepareParam(st, pParam);
            ResultSet rset = st.executeQuery();
            int rowCount = 0;
            while (rset.next()){
            	idProfile = getHTMLValue(rset.getString("ID_DS_CL_PROFILE"));
            	nameProfile = getHTMLValue(rset.getString("NAME_DS_CL_PROFILE"));
            	markedNameProfile = prepareFindString(nameProfile, findString);
                if (!isEmpty(idProfileInput)) {
                	if (idProfile == idProfileInput) {
                		markedNameProfile = "<b>" + nameProfile.toUpperCase() + "</b>";
                	}
                }
                
                html.append(
                        "<tr>" +
                        "<td align=\"center\"><div class=\"div_find\" onClick=\"changeOpener('"+idProfile+"','"+nameProfile+"')\">"+getValue2(idProfile) +"</div></td>" +
                        "<td><div class=\"div_find_marked\" onClick=\"changeOpener('"+idProfile+"','"+nameProfile+"')\">"+getValue2(markedNameProfile)+"</div></td>" +
                        "<td><div class=\"div_find\" onClick=\"changeOpener('"+idProfile+"','"+nameProfile+"')\">"+getValue2(rset.getString("SNAME_ISSUER"))+"</div></td>" +
                        "<td><div class=\"div_find\" onClick=\"changeOpener('"+idProfile+"','"+nameProfile+"')\">"+getValue2(rset.getString("NAME_CARD_STATUS"))+"</div></td>" +
                        "<td><div class=\"div_find\" onClick=\"changeOpener('"+idProfile+"','"+nameProfile+"')\">"+getValue2(rset.getString("NAME_BON_CATEGORY"))+"</div></td>" +
                        "<td><div class=\"div_find\" onClick=\"changeOpener('"+idProfile+"','"+nameProfile+"')\">"+getValue2(rset.getString("NAME_DISC_CATEGORY"))+"</div></td>" +
                        "<td><div class=\"div_find\" onClick=\"changeOpener('"+idProfile+"','"+nameProfile+"')\">"+getValue2(rset.getString("SEX_NAT_PRS_TSL"))+"</div></td>" +
                        "<td><div class=\"div_find\" onClick=\"changeOpener('"+idProfile+"','"+nameProfile+"')\">"+getValue2(rset.getString("NAME_NAT_PRS_GROUP"))+"</div></td>" +
                        "</tr>");
                       
                rowCount++;
            }
            if (rowCount == 0) {
            	html.append(getNoDataFountTR("8"));
            }
        } catch (SQLException e) {LOGGER.error(html, e);html.append(prepareSQLToLog(mySQL, pParam) + "<br>" + e.toString());
        } catch (Exception el) {LOGGER.error(html, el);html.append(prepareSQLToLog(mySQL, pParam) + "<br>" + el.toString());}
        finally {
            try {
                if (st!=null) {
					st.close();
				}
            } catch (SQLException w) {w.toString();}
            try {
                if (con!=null) {
					con.close();
				}
            } catch (SQLException w) {w.toString();}
            Connector.closeConnection(con);
        } // finally

        html.append("</tbody>");
        html.append("</table>");
        
        return_value.setFunctionHTMLReturnValue(html.toString());
        return return_value;
    } // getGiftsList


    public transport getDSPartnerPatternList(transport mySource) {
    	
    	bc.service.pages page = new bc.service.pages(mySource.getPageNumber(), mySource.getPageRowCount());
    	String l_beg = page.getFirstRowNumber();
    	String l_end = page.getLastRowNumber();
    	
    	transport return_value=new transport();
		return_value.setFunctionName(mySource.getFunctionName());
		return_value.setIdDestination(mySource.getIdDestination());
		return_value.setFunctionParam(mySource.getFunctionParam());
		
		String findString = mySource.getFunctionParam();
		String idPatternInput = mySource.getFunctionParam2();
		
		StringBuilder html = new StringBuilder();
		
		ArrayList<bcFeautureParam> pParam = initParamArray();

		String mySQL = 
        	" SELECT rn, id_pt_pattern, name_pt_pattern, name_pattern_status, name_pattern_type, " +
			"	 	 name_dispatch_type, begin_action_date_frmt, end_action_date_frmt, " +
			"	 	 name_ds_pt_profile " +
			"   FROM (SELECT ROWNUM rn, id_pt_pattern, name_pt_pattern, name_pattern_status, name_pattern_type, " +
			"			 	 name_dispatch_type, begin_action_date_frmt, end_action_date_frmt, " +
			"			 	 name_ds_pt_profile,  " + 
			"                DECODE(a.can_send_email, " +
			"                       'N', '<font color=\"black\">'||a.can_send_email_tsl||'</font>', " +
			"                       'Y', '<font color=\"red\"><b>'||a.can_send_email_tsl||'</b></font>', " +
			"                       a.can_send_email_tsl)||'/'||" +
			"                DECODE(a.can_send_office, " +
			"                       'N', '<font color=\"black\">'||a.can_send_office_tsl||'</font>', " +
			"                       'Y', '<font color=\"red\"><b>'||a.can_send_office_tsl||'</b></font>', " +
			"                       a.can_send_office_tsl)||'/'||" +
			"                DECODE(a.can_send_term, " +
			"                       'N', '<font color=\"black\">'||a.can_send_term_tsl||'</font>', " +
			"                       'Y', '<font color=\"red\"><b>'||a.can_send_term_tsl||'</b></font>', " +
			"                       a.can_send_term_tsl) can_send_partner_tsl " +
		  	"          FROM (SELECT * " +
		  	"                  FROM " + getGeneralDBScheme()+".vc_ds_pt_pattern_club_all " +
		  	"                 WHERE 1=1 ";
        if (!isEmpty(findString)) {
        	mySQL = mySQL +
  			"             AND (TO_CHAR(id_pt_pattern) LIKE UPPER('%'||?||'%') OR " +
  			"				   UPPER(name_pt_pattern) LIKE UPPER('%'||?||'%'))";
        	for (int i=0; i<2; i++) {
        	    pParam.add(new bcFeautureParam("string", findString));
        	}
        }
        if (!isEmpty(idPatternInput)) {
        	mySQL = mySQL + " AND id_pt_pattern = ? ";
        	pParam.add(new bcFeautureParam("int", idPatternInput));
        }
        pParam.add(new bcFeautureParam("int", l_end));
        pParam.add(new bcFeautureParam("int", l_beg));
        mySQL = mySQL +
        	"                  ORDER BY name_pt_pattern" +
        	"        ) WHERE ROWNUM < ? " + 
  			" ) WHERE rn >= ?";
        
        LOGGER.debug(prepareSQLToLog(mySQL, pParam));
        PreparedStatement st = null;
        String idPattern = "";
        String namePattern = "";
        String markedNamePattern = "";
        
        try{
        	con = Connector.getConnection(mySource.getSessionId());
            st = con.prepareStatement(mySQL);
            st = prepareParam(st, pParam);
            ResultSet rset = st.executeQuery();
            int rowCount = 0;
            while (rset.next()){
            	idPattern = getHTMLValue(rset.getString("ID_PATTERN"));
            	namePattern = getHTMLValue(rset.getString("NAME_PATTERN"));
            	markedNamePattern = prepareFindString(namePattern, findString);
                if (!isEmpty(idPatternInput)) {
                	if (idPattern == idPatternInput) {
                		markedNamePattern = "<b>" + namePattern.toUpperCase() + "</b>";
                	}
                }
                
                html.append(
                        "<tr>" +
                        "<td align=\"center\"><div class=\"div_find\" onClick=\"changeOpener('"+idPattern+"','"+namePattern+"')\">"+getValue2(idPattern) +"</div></td>" +
                        "<td><div class=\"div_find\" onClick=\"changeOpener('"+idPattern+"','"+namePattern+"')\">"+getValue2(rset.getString("TYPE_MESSAGE_TSL"))+"</div></td>" +
                        "<td><div class=\"div_find_marked\" onClick=\"changeOpener('"+idPattern+"','"+namePattern+"')\">"+getValue2(markedNamePattern)+"</div></td>" +
                        "<td><div class=\"div_find\" onClick=\"changeOpener('"+idPattern+"','"+namePattern+"')\">"+getValue2(rset.getString("TEXT_MESSAGE"))+"</div></td>" +
                        "<td><div class=\"div_find\" onClick=\"changeOpener('"+idPattern+"','"+namePattern+"')\">"+getValue2(rset.getString("BEGIN_ACTION_DATE_FRMT"))+"</div></td>" +
                        "<td><div class=\"div_find\" onClick=\"changeOpener('"+idPattern+"','"+namePattern+"')\">"+getValue2(rset.getString("END_ACTION_DATE_FRMT"))+"</div></td>" +
                        "</tr>");
                       
                rowCount++;
            }
            if (rowCount == 0) {
            	html.append(getNoDataFountTR("8"));
            }
        } catch (SQLException e) {LOGGER.error(html, e);html.append(prepareSQLToLog(mySQL, pParam) + "<br>" + e.toString());
        } catch (Exception el) {LOGGER.error(html, el);html.append(prepareSQLToLog(mySQL, pParam) + "<br>" + el.toString());}
        finally {
            try {
                if (st!=null) {
					st.close();
				}
            } catch (SQLException w) {w.toString();}
            try {
                if (con!=null) {
					con.close();
				}
            } catch (SQLException w) {w.toString();}
            Connector.closeConnection(con);
        } // finally

        html.append("</tbody>");
        html.append("</table>");
        
        return_value.setFunctionHTMLReturnValue(html.toString());
        return return_value;
    } // getDSPartnerProfileList


    public transport getDSPartnerProfileList(transport mySource) {
    	
    	bc.service.pages page = new bc.service.pages(mySource.getPageNumber(), mySource.getPageRowCount());
    	String l_beg = page.getFirstRowNumber();
    	String l_end = page.getLastRowNumber();
    	
    	transport return_value=new transport();
		return_value.setFunctionName(mySource.getFunctionName());
		return_value.setIdDestination(mySource.getIdDestination());
		return_value.setFunctionParam(mySource.getFunctionParam());
		
		String findString = mySource.getFunctionParam();
		String idProfileInput = mySource.getFunctionParam2();
		
		StringBuilder html = new StringBuilder();
		
		ArrayList<bcFeautureParam> pParam = initParamArray();

		String mySQL = 
        	" SELECT rn, id_ds_pt_profile, name_ds_pt_profile, desc_ds_pt_profile " +
      	  	"   FROM (SELECT ROWNUM rn, id_ds_pt_profile, name_ds_pt_profile, desc_ds_pt_profile " +
			"   		FROM (SELECT * " +
			"                   FROM " + getGeneralDBScheme()+".vc_ds_pt_profile_club_all ";
        if (!isEmpty(findString)) {
        	mySQL = mySQL +
  			"           WHERE (TO_CHAR(id_ds_pt_profile) LIKE UPPER('%'||?||'%') OR " +
  			"				   UPPER(name_ds_pt_profile) LIKE UPPER('%'||?||'%') OR " +
  			"				   UPPER(desc_ds_pt_profile) LIKE UPPER('%'||?||'%'))";
        	for (int i=0; i<3; i++) {
        	    pParam.add(new bcFeautureParam("string", findString));
        	}
        }
        pParam.add(new bcFeautureParam("int", l_end));
        pParam.add(new bcFeautureParam("int", l_beg));
        mySQL = mySQL +
        	"                  ORDER BY name_ds_pt_profile" +
        	"        ) WHERE ROWNUM < ? " + 
  			" ) WHERE rn >= ?";
        
        LOGGER.debug(prepareSQLToLog(mySQL, pParam));
        PreparedStatement st = null;
        String idProfile = "";
        String nameProfile = "";
        String descProfile = "";
        String markedNameProfile = "";
        String markedDescProfile = "";
        
        try{
        	con = Connector.getConnection(mySource.getSessionId());
            st = con.prepareStatement(mySQL);
            st = prepareParam(st, pParam);
            ResultSet rset = st.executeQuery();
            int rowCount = 0;
            while (rset.next()){
            	idProfile = getHTMLValue(rset.getString("ID_DS_PT_PROFILE"));
            	nameProfile = getHTMLValue(rset.getString("NAME_DS_PT_PROFILE"));
            	descProfile = getHTMLValue(rset.getString("DESC_DS_PT_PROFILE"));
            	markedNameProfile = prepareFindString(nameProfile, findString);
            	markedDescProfile = prepareFindString(descProfile, findString);
                if (!isEmpty(idProfileInput)) {
                	if (idProfile == idProfileInput) {
                		markedNameProfile = "<b>" + nameProfile.toUpperCase() + "</b>";
                	}
                }
                
                html.append(
                        "<tr>" +
                        "<td align=\"center\"><div class=\"div_find\" onClick=\"changeOpener('"+idProfile+"','"+nameProfile+"')\">"+getValue2(idProfile) +"</div></td>" +
                        "<td><div class=\"div_find_marked\" onClick=\"changeOpener('"+idProfile+"','"+nameProfile+"')\">"+getValue2(markedNameProfile)+"</div></td>" +
                        "<td><div class=\"div_find\" onClick=\"changeOpener('"+idProfile+"','"+nameProfile+"')\">"+getValue2(markedDescProfile)+"</div></td>" +
                        "</tr>");
                       
                rowCount++;
            }
            if (rowCount == 0) {
            	html.append(getNoDataFountTR("8"));
            }
        } catch (SQLException e) {LOGGER.error(html, e);html.append(prepareSQLToLog(mySQL, pParam) + "<br>" + e.toString());
        } catch (Exception el) {LOGGER.error(html, el);html.append(prepareSQLToLog(mySQL, pParam) + "<br>" + el.toString());}
        finally {
            try {
                if (st!=null) {
					st.close();
				}
            } catch (SQLException w) {w.toString();}
            try {
                if (con!=null) {
					con.close();
				}
            } catch (SQLException w) {w.toString();}
            Connector.closeConnection(con);
        } // finally

        html.append("</tbody>");
        html.append("</table>");
        
        return_value.setFunctionHTMLReturnValue(html.toString());
        return return_value;
    } // getDSPartnerProfileList

    public transport getDocumentList(transport mySource) {
    	
    	bc.service.pages page = new bc.service.pages(mySource.getPageNumber(), mySource.getPageRowCount());
    	String l_beg = page.getFirstRowNumber();
    	String l_end = page.getLastRowNumber();
    	
    	transport return_value=new transport();
		return_value.setFunctionName(mySource.getFunctionName());
		return_value.setIdDestination(mySource.getIdDestination());
		return_value.setFunctionParam(mySource.getFunctionParam());
		
		String findString = mySource.getFunctionParam();
		String id_jur_prs = mySource.getFunctionParam2();
		
		StringBuilder html = new StringBuilder();
		
		ArrayList<bcFeautureParam> pParam = initParamArray();

		String mySQL = 
        	" SELECT full_doc, parties_doc, name_doc_type, name_club_rel_type, name_doc_state, id_doc " +
	  		"   FROM (SELECT ROWNUM rn, full_doc, parties_doc, name_doc_type, name_club_rel_type, name_doc_state, id_doc " +
  			"           FROM (SELECT * " +
  			"                   FROM " + getGeneralDBScheme() + ".vc_doc_priv_all " +
  			"                  WHERE 1=1 ";
        if (!isEmpty(id_jur_prs)) {
        	mySQL = mySQL + " AND (id_jur_prs_party1 = ? OR id_jur_prs_party2 = ? OR id_jur_prs_party3 = ?) ";
        	pParam.add(new bcFeautureParam("int", id_jur_prs));
        	pParam.add(new bcFeautureParam("int", id_jur_prs));
        	pParam.add(new bcFeautureParam("int", id_jur_prs));
        }
        if (!isEmpty(findString)) {
        	mySQL = mySQL +
	        	" AND (TO_CHAR(id_doc) like UPPER('%'||?||'%') OR " +
				"      UPPER(full_doc) like UPPER('%'||?||'%') OR " +
				"      UPPER(sname_jur_prs_party1) like UPPER('%'||?||'%') OR " +
				"      UPPER(sname_jur_prs_party2) like UPPER('%'||?||'%') OR " +
				"      UPPER(sname_jur_prs_party3) like UPPER('%'||?||'%') " +
				") ";
        	for (int i=0; i<5; i++) {
        	    pParam.add(new bcFeautureParam("string", findString));
        	}
        }
        pParam.add(new bcFeautureParam("int", l_end));
        pParam.add(new bcFeautureParam("int", l_beg));
        mySQL = mySQL +
        	"                  ORDER BY full_doc" +
        	"        ) WHERE ROWNUM < ? " + 
  			" ) WHERE rn >= ?";
        
        LOGGER.debug(prepareSQLToLog(mySQL, pParam));
        PreparedStatement st = null;
        String idDoc = "";
        String nameDoc = "";
        String markedNameDoc = "";
        
        try{
        	con = Connector.getConnection(mySource.getSessionId());
            st = con.prepareStatement(mySQL);
            st = prepareParam(st, pParam);
            ResultSet rset = st.executeQuery();
            int rowCount = 0;
            while (rset.next()){
            	idDoc = getHTMLValue(rset.getString("ID_DOC"));
            	nameDoc = getHTMLValue(rset.getString("FULL_DOC"));
            	markedNameDoc = prepareFindString(nameDoc, findString);
                
                html.append(
                        "<tr><td><div class=\"div_find_marked\" onClick=\"changeOpener('"+idDoc+"','"+nameDoc+"')\">"+getValue2(markedNameDoc) +"</div></td>" +
                        "<td><div class=\"div_find\" onClick=\"changeOpener('"+idDoc+"','"+nameDoc+"')\">"+getValue2(rset.getString("PARTIES_DOC"))+"</div></td>" +
                        "<td><div class=\"div_find\" onClick=\"changeOpener('"+idDoc+"','"+nameDoc+"')\">"+getValue2(rset.getString("NAME_DOC_TYPE"))+"</div></td>" +
                        "<td><div class=\"div_find\" onClick=\"changeOpener('"+idDoc+"','"+nameDoc+"')\">"+getValue2(rset.getString("NAME_CLUB_REL_TYPE"))+"</div></td>" +
                        "<td><div class=\"div_find\" onClick=\"changeOpener('"+idDoc+"','"+nameDoc+"')\">"+getValue2(rset.getString("NAME_DOC_STATE"))+"</div></td></tr>");
                       
                rowCount++;
            }
            if (rowCount == 0) {
            	html.append(getNoDataFountTR("9"));
            }
        } // try
        catch (SQLException e) {LOGGER.error(html, e);html.append(prepareSQLToLog(mySQL, pParam) + "<br>" + e.toString());}
        catch (Exception el) {LOGGER.error(html, el);html.append(prepareSQLToLog(mySQL, pParam) + "<br>" + el.toString());}
        finally {
            try {
                if (st!=null) {
					st.close();
				}
            } catch (SQLException w) {w.toString();}
            try {
                if (con!=null) {
					con.close();
				}
            } catch (SQLException w) {w.toString();}
            Connector.closeConnection(con);
        } // finally

        html.append("</tbody>");
        html.append("</table>");
        
        return_value.setFunctionHTMLReturnValue(html.toString());
        return return_value;
    } // getContactPrs

    public transport getContactPrs(transport mySource) {
    	
    	bc.service.pages page = new bc.service.pages(mySource.getPageNumber(), mySource.getPageRowCount());
    	String l_beg = page.getFirstRowNumber();
    	String l_end = page.getLastRowNumber();
    	
    	transport return_value=new transport();
		return_value.setFunctionName(mySource.getFunctionName());
		return_value.setIdDestination(mySource.getIdDestination());
		return_value.setFunctionParam(mySource.getFunctionParam());
		
		String findString = mySource.getFunctionParam();
		String id_jur_prs = mySource.getFunctionParam2();
		String id_service_place = mySource.getFunctionParam3();
		
		StringBuilder html = new StringBuilder();
		
		ArrayList<bcFeautureParam> pParam = initParamArray();

		String mySQL = 
        	" SELECT id_contact_prs, name_contact_prs, sname_service_place_work, " +
        	"        name_post, desc_contact_prs, phone_work, email " +
	  		"   FROM (SELECT ROWNUM rn, id_contact_prs, sname_service_place_work, name_contact_prs, " +
        	"                name_post, desc_contact_prs, phone_work, email " +
  			"           FROM (SELECT * " +
  			"                   FROM " + getGeneralDBScheme() + ".vc_contact_prs_priv_all " +
  			"                  WHERE 1=1 ";
        if (!isEmpty(id_jur_prs)) {
        	mySQL = mySQL + " AND id_jur_prs = ? ";
        	pParam.add(new bcFeautureParam("int", id_jur_prs));
        }
        if (!isEmpty(id_service_place)) {
        	mySQL = mySQL + " AND id_service_place = ? ";
        	pParam.add(new bcFeautureParam("int", id_service_place));
        }
        if (!isEmpty(findString)) {
        	mySQL = mySQL +
  			"          AND (TO_CHAR(id_contact_prs) LIKE '%'||?||'%' OR " +
  			"                 UPPER(sname_service_place_work) LIKE UPPER('%'||?||'%') OR " +
  			"                 UPPER(name_contact_prs) LIKE UPPER('%'||?||'%') OR " +
  			"                 UPPER(name_post) LIKE UPPER('%'||?||'%') OR " +
  			"                 UPPER(desc_contact_prs) LIKE UPPER('%'||?||'%') OR " +
  			"                 UPPER(phone_work) LIKE UPPER('%'||?||'%') OR " +
  			"                 UPPER(email) LIKE UPPER('%'||?||'%')) ";
        	for (int i=0; i<7; i++) {
        	    pParam.add(new bcFeautureParam("string", findString));
        	}
        }
        pParam.add(new bcFeautureParam("int", l_end));
        pParam.add(new bcFeautureParam("int", l_beg));
        mySQL = mySQL +
        	"                  ORDER BY name_contact_prs" +
        	"        ) WHERE ROWNUM < ? " + 
  			" ) WHERE rn >= ?";
        
        LOGGER.debug(prepareSQLToLog(mySQL, pParam));
        PreparedStatement st = null;
        String idContactPrs = "";
        String nameContactPrs = "";
        String markedIdContactPrs = "";
        String markedNameContactPrs = "";
        
        try{
        	con = Connector.getConnection(mySource.getSessionId());
            st = con.prepareStatement(mySQL);
            st = prepareParam(st, pParam);
            ResultSet rset = st.executeQuery();
            int rowCount = 0;
            while (rset.next()){
            	idContactPrs = getHTMLValue(rset.getString("ID_CONTACT_PRS"));
            	nameContactPrs = getHTMLValue(rset.getString("NAME_CONTACT_PRS"));
            	markedIdContactPrs = prepareFindString(idContactPrs, findString);
            	markedNameContactPrs = prepareFindString(nameContactPrs, findString);
                
                html.append(
                        "<tr><td><div class=\"div_find\" onClick=\"changeOpener('"+idContactPrs+"','"+nameContactPrs+"')\">"+getValue2(markedIdContactPrs) +"</div></td>" +
                        "<td><div class=\"div_find\" onClick=\"changeOpener('"+idContactPrs+"','"+nameContactPrs+"')\">"+getValue2(rset.getString("SNAME_SERVICE_PLACE_WORK"))+"</div></td>" +
                        "<td><div class=\"div_find_marked\" onClick=\"changeOpener('"+idContactPrs+"','"+nameContactPrs+"')\">"+getValue2(markedNameContactPrs)+"</div></td>" +
                        "<td><div class=\"div_find\" onClick=\"changeOpener('"+idContactPrs+"','"+nameContactPrs+"')\">"+getValue2(rset.getString("NAME_POST"))+"</div></td>" +
                        "<td><div class=\"div_find\" onClick=\"changeOpener('"+idContactPrs+"','"+nameContactPrs+"')\">"+getValue2(rset.getString("DESC_CONTACT_PRS"))+"</div></td>" +
                        "<td><div class=\"div_find\" onClick=\"changeOpener('"+idContactPrs+"','"+nameContactPrs+"')\">"+getValue2(rset.getString("PHONE_WORK"))+"</div></td>" +
                        "<td><div class=\"div_find\" onClick=\"changeOpener('"+idContactPrs+"','"+nameContactPrs+"')\">"+getValue2(rset.getString("EMAIL"))+"</div></td></tr>");
                       
                rowCount++;
            }
            if (rowCount == 0) {
            	html.append(getNoDataFountTR("9"));
            }
        } // try
        catch (SQLException e) {LOGGER.error(html, e);html.append(prepareSQLToLog(mySQL, pParam) + "<br>" + e.toString());}
        catch (Exception el) {LOGGER.error(html, el);html.append(prepareSQLToLog(mySQL, pParam) + "<br>" + el.toString());}
        finally {
            try {
                if (st!=null) {
					st.close();
				}
            } catch (SQLException w) {w.toString();}
            try {
                if (con!=null) {
					con.close();
				}
            } catch (SQLException w) {w.toString();}
            Connector.closeConnection(con);
        } // finally

        html.append("</tbody>");
        html.append("</table>");
        
        return_value.setFunctionHTMLReturnValue(html.toString());
        return return_value;
    } // getContactPrs

    public transport getLGPromoter(transport mySource) {
    	
    	bc.service.pages page = new bc.service.pages(mySource.getPageNumber(), mySource.getPageRowCount());
    	String l_beg = page.getFirstRowNumber();
    	String l_end = page.getLastRowNumber();
    	
    	transport return_value=new transport();
		return_value.setFunctionName(mySource.getFunctionName());
		return_value.setIdDestination(mySource.getIdDestination());
		return_value.setFunctionParam(mySource.getFunctionParam());
		
		String findString = mySource.getFunctionParam();
		String id_jur_prs = mySource.getFunctionParam2();
		String id_service_place = mySource.getFunctionParam3();
		
		StringBuilder html = new StringBuilder();
		
		ArrayList<bcFeautureParam> pParam = initParamArray();

		String mySQL = 
        	"SELECT id_lg_promoter, cd_lg_promoter, name_lg_promoter, " +
			"       jur_prs||DECODE(service_place, null, '', ', '||service_place) jur_prs, name_lg_promoter_post, " +
	  		"       name_lg_promoter_state, date_begin_work_frmt, date_end_work_frmt " +
	  		"  FROM (SELECT ROWNUM rn, id_lg_promoter, cd_lg_promoter, " +
	  		"               name_lg_promoter, sname_jur_prs jur_prs, " +
	  		"               name_service_place service_place, " +
	  		"               DECODE(cd_lg_promoter_post, " +
	  		"                      'PROMOTER', '<font color=\"green\"><b>'||name_lg_promoter_post||'</b></font>', " +
  			"                      'SUPERVISOR', '<font color=\"red\"><b>'||name_lg_promoter_post||'</b></font>', " +
  			"                      'CHIEF', '<font color=\"blue\"><b>'||name_lg_promoter_post||'</b></font>', " +
  			"                      '<font color=\"black\"><b>'||name_lg_promoter_post||'</b></font>' " +
	  		"               ) name_lg_promoter_post, " +
	  		"               DECODE(cd_lg_promoter_state, " +
  			"                      'ACCEPTED', '<font color=\"green\"><b>'||name_lg_promoter_state||'</b></font>', " +
  			"                      'TRANSFERRED', '<font color=\"blue\"><b>'||name_lg_promoter_state||'</b></font>', " +
  			"                      'DISMISSED', '<font color=\"red\"><b>'||name_lg_promoter_state||'</b></font>', " +
  			"                      name_lg_promoter_state " +
	  		"               ) name_lg_promoter_state, " +
	  		"               date_begin_work_frmt, date_end_work_frmt " +
  			"           FROM (SELECT * " +
  			"                   FROM " + getGeneralDBScheme() + ".vc_lg_promoter_club_all " +
  			"                  WHERE 1=1 ";
        if (!isEmpty(id_jur_prs)) {
        	mySQL = mySQL + " AND id_jur_prs = ? ";
        	pParam.add(new bcFeautureParam("int", id_jur_prs));
        }
        if (!isEmpty(id_service_place)) {
        	mySQL = mySQL + " AND id_service_place = ? ";
        	pParam.add(new bcFeautureParam("int", id_service_place));
        }
        if (!isEmpty(findString)) {
        	mySQL = mySQL +
  			"          AND (TO_CHAR(id_lg_promoter) LIKE '%'||?||'%' OR " +
  			"                 UPPER(cd_lg_promoter) LIKE UPPER('%'||?||'%') OR " +
  			"                 UPPER(name_lg_promoter) LIKE UPPER('%'||?||'%') OR " +
  			"                 UPPER(date_begin_work_frmt) LIKE UPPER('%'||?||'%') OR " +
  			"                 UPPER(date_end_work_frmt) LIKE UPPER('%'||?||'%')) ";
        	for (int i=0; i<5; i++) {
        	    pParam.add(new bcFeautureParam("string", findString));
        	}
        }
        pParam.add(new bcFeautureParam("int", l_end));
        pParam.add(new bcFeautureParam("int", l_beg));
        mySQL = mySQL +
        	"                  ORDER BY name_lg_promoter" +
        	"        ) WHERE ROWNUM < ? " + 
  			" ) WHERE rn >= ?";
        
        LOGGER.debug(prepareSQLToLog(mySQL, pParam));
        PreparedStatement st = null;
        String idLGPromoter = "";
        String nameLGPromoter = "";
        String markedIdLGPromoter = "";
        String markedNameLGPromoter = "";
        
        try{
        	con = Connector.getConnection(mySource.getSessionId());
            st = con.prepareStatement(mySQL);
            st = prepareParam(st, pParam);
            ResultSet rset = st.executeQuery();
            int rowCount = 0;
            while (rset.next()){
            	idLGPromoter = getHTMLValue(rset.getString("ID_LG_PROMOTER"));
            	nameLGPromoter = getHTMLValue(rset.getString("NAME_LG_PROMOTER"));
            	markedIdLGPromoter = prepareFindString(idLGPromoter, findString);
            	markedNameLGPromoter = prepareFindString(nameLGPromoter, findString);
                
                html.append(
                        "<tr><td><div class=\"div_find\" onClick=\"changeOpener('"+idLGPromoter+"','"+nameLGPromoter+"')\">"+getValue2(markedIdLGPromoter) +"</div></td>" +
                        "<td><div class=\"div_find\" onClick=\"changeOpener('"+idLGPromoter+"','"+nameLGPromoter+"')\">"+getValue2(rset.getString("CD_LG_PROMOTER"))+"</div></td>" +
                        "<td><div class=\"div_find_marked\" onClick=\"changeOpener('"+idLGPromoter+"','"+nameLGPromoter+"')\">"+getValue2(markedNameLGPromoter)+"</div></td>" +
                        "<td><div class=\"div_find\" onClick=\"changeOpener('"+idLGPromoter+"','"+nameLGPromoter+"')\">"+getValue2(rset.getString("JUR_PRS"))+"</div></td>" +
                        "<td><div class=\"div_find\" onClick=\"changeOpener('"+idLGPromoter+"','"+nameLGPromoter+"')\">"+getValue2(rset.getString("NAME_LG_PROMOTER_POST"))+"</div></td>" +
                        "<td><div class=\"div_find\" onClick=\"changeOpener('"+idLGPromoter+"','"+nameLGPromoter+"')\">"+getValue2(rset.getString("NAME_LG_PROMOTER_STATE"))+"</div></td>" +
                        "<td><div class=\"div_find\" onClick=\"changeOpener('"+idLGPromoter+"','"+nameLGPromoter+"')\">"+getValue2(rset.getString("DATE_BEGIN_WORK_FRMT"))+"</div></td>" +
                        "<td><div class=\"div_find\" onClick=\"changeOpener('"+idLGPromoter+"','"+nameLGPromoter+"')\">"+getValue2(rset.getString("DATE_END_WORK_FRMT"))+"</div></td></tr>");
                       
                rowCount++;
            }
            if (rowCount == 0) {
            	html.append(getNoDataFountTR("9"));
            }
        } // try
        catch (SQLException e) {LOGGER.error(html, e);html.append(prepareSQLToLog(mySQL, pParam) + "<br>" + e.toString());}
        catch (Exception el) {LOGGER.error(html, el);html.append(prepareSQLToLog(mySQL, pParam) + "<br>" + el.toString());}
        finally {
            try {
                if (st!=null) {
					st.close();
				}
            } catch (SQLException w) {w.toString();}
            try {
                if (con!=null) {
					con.close();
				}
            } catch (SQLException w) {w.toString();}
            Connector.closeConnection(con);
        } // finally

        html.append("</tbody>");
        html.append("</table>");
        
        return_value.setFunctionHTMLReturnValue(html.toString());
        return return_value;
    }

    public transport getDSMessageSenderForPhoneMobileList(transport mySource) {
    	
    	bc.service.pages page = new bc.service.pages(mySource.getPageNumber(), mySource.getPageRowCount());
    	String l_beg = page.getFirstRowNumber();
    	String l_end = page.getLastRowNumber();
    	
    	transport return_value=new transport();
		return_value.setFunctionName(mySource.getFunctionName());
		return_value.setIdDestination(mySource.getIdDestination());
		return_value.setFunctionParam(mySource.getFunctionParam());
		
		String findString = mySource.getFunctionParam();
		String sender_kind = mySource.getFunctionParam2();
		String phone_mobile = mySource.getFunctionParam3();
		
		StringBuilder html = new StringBuilder();
		
		ArrayList<bcFeautureParam> pParam = initParamArray();

		String mySQLContactPrs = 
			" SELECT 'PARTNER' cd_dispatch_kind, id_contact_prs id_sender, " +
  			"		 name_contact_prs name_sender, " +
  			"		 sname_jur_prs||', '||name_service_place||', '||name_post desc_sender, " +
  			"		 phone_mobile, email " +
  			"   FROM " + getGeneralDBScheme() + ".vc_contact_prs_priv_all " +
  			"  WHERE 1=1 ";
		if (!isEmpty(phone_mobile)) {
			mySQLContactPrs = mySQLContactPrs + " AND phone_mobile = ? ";
			pParam.add(new bcFeautureParam("string", phone_mobile));
        }
        if (!isEmpty(findString)) {
        	mySQLContactPrs = mySQLContactPrs +
	  			" AND (TO_CHAR(id_contact_prs) LIKE '%'||?||'%' OR " +
	  			"      UPPER(name_contact_prs) LIKE UPPER('%'||?||'%') ";
        	for (int i=0; i<2; i++) {
        	    pParam.add(new bcFeautureParam("string", findString));
        	}
	       	if (isEmpty(phone_mobile)) {
	       		mySQLContactPrs = mySQLContactPrs +
	      			" OR UPPER(phone_mobile) LIKE UPPER('%'||?||'%')";
	       		pParam.add(new bcFeautureParam("string", findString));
	        }
	       	mySQLContactPrs = mySQLContactPrs + ")";
        }
        
        String mySQLNatPrs = 
			" SELECT 'CLIENT' cd_dispatch_kind, id_nat_prs id_sender, full_name name_sender, fact_adr_full desc_sender, " +
  			"		 phone_mobile, email " +
  			"   FROM " + getGeneralDBScheme() + ".vc_nat_prs_short_all " +
  			"  WHERE 1=1 ";
		if (!isEmpty(phone_mobile)) {
			mySQLNatPrs = mySQLNatPrs + " AND phone_mobile = ? ";
			pParam.add(new bcFeautureParam("string", phone_mobile));
        }
        if (!isEmpty(findString)) {
        	mySQLNatPrs = mySQLNatPrs +
	  			" AND (TO_CHAR(id_nat_prs) LIKE '%'||?||'%' OR " +
	  			"      UPPER(full_name) LIKE UPPER('%'||?||'%') ";
        	for (int i=0; i<2; i++) {
        	    pParam.add(new bcFeautureParam("string", findString));
        	}
	       	if (isEmpty(phone_mobile)) {
	       		mySQLNatPrs = mySQLNatPrs +
	      			" OR UPPER(phone_mobile) LIKE UPPER('%'||?||'%')";
	       		pParam.add(new bcFeautureParam("string", findString));
	        }
	       	mySQLNatPrs = mySQLNatPrs + ")";
        }
        
        String mySQLSystemUser = 
			" SELECT 'SYSTEM' cd_dispatch_kind, id_user id_sender, name_user name_sender, " +
  			"	 	 desc_user desc_sender, phone_mobile, email" +
  			"   FROM " + getGeneralDBScheme() + ".vc_users_all " +
  			"  WHERE 1=1 ";
		if (!isEmpty(phone_mobile)) {
			mySQLSystemUser = mySQLSystemUser + " AND phone_mobile = ? ";
			pParam.add(new bcFeautureParam("string", phone_mobile));
        }
        if (!isEmpty(findString)) {
        	mySQLSystemUser = mySQLSystemUser +
	  			" AND (TO_CHAR(id_user) LIKE '%'||?||'%' OR " +
	  			"      UPPER(name_user) LIKE UPPER('%'||?||'%') ";
        	for (int i=0; i<2; i++) {
        	    pParam.add(new bcFeautureParam("string", findString));
        	}
        	if (isEmpty(phone_mobile)) {
    			mySQLSystemUser = mySQLSystemUser +
      				" OR UPPER(phone_mobile) LIKE UPPER('%'||?||'%')";
    			pParam.add(new bcFeautureParam("string", findString));
            }
        	mySQLSystemUser = mySQLSystemUser + ")";
        }
        
        String mySQLRequest = "";
        if ("CLIENT".equalsIgnoreCase(sender_kind)) {
        	mySQLRequest = mySQLNatPrs;
        } else if ("PARTNER".equalsIgnoreCase(sender_kind)) {
        	mySQLRequest = mySQLContactPrs;
        } else if ("USER".equalsIgnoreCase(sender_kind)) {
        	mySQLRequest = mySQLSystemUser;
        } else {
        	mySQLRequest = 
        		mySQLNatPrs + " UNION ALL " +
        		mySQLContactPrs + " UNION ALL " +
        		mySQLSystemUser;
        }
        
        String mySQL = 
        	" SELECT cd_dispatch_kind, name_dispatch_kind, id_sender, name_sender, desc_sender, " +
        	"        phone_mobile, email " +
	  		"   FROM (SELECT ROWNUM rn, cd_dispatch_kind, " +
	  		"				 DECODE(cd_dispatch_kind, " +
  		  	"                      	'CLIENT', '<b><font color=\"green\">'||name_dispatch_kind||'</font></b>', " +
			"                      	'PARTNER', '<b><font color=\"blue\">'||name_dispatch_kind||'</font></b>', " +
			"                       'SYSTEM', '<font color=\"red\"><b>'||name_dispatch_kind||'</b></font>', " +
			"                       'TRAINING', '<font color=\"darkgray\">'||name_dispatch_kind||'</font>', " +
			"                       'LG_PROMOTER', '<font color=\"black\">'||name_dispatch_kind||'</font>', " +
			"                       'UNKNOWN', '', " +
			"                      	name_dispatch_kind" +
			"				 ) name_dispatch_kind, id_sender, name_sender, desc_sender, " +
        	"                phone_mobile, email " +
  			"           FROM (SELECT a.cd_dispatch_kind, k.name_dispatch_kind, a.id_sender, a.name_sender, a.desc_sender, " +
        	"                        a.phone_mobile, a.email " +
  			"                   FROM ( " + mySQLRequest + ") a, " +
  			"                          " + getGeneralDBScheme() + ".vc_ds_dispatch_kind_all k " +
  			"                  WHERE a.cd_dispatch_kind = k.cd_dispatch_kind" +
        	"                  ORDER BY a.name_sender" +
        	"        ) WHERE ROWNUM < ?" + 
  			" ) WHERE rn >= ?";

        pParam.add(new bcFeautureParam("int", l_end));
        pParam.add(new bcFeautureParam("int", l_beg));
        
        LOGGER.debug(prepareSQLToLog(mySQL, pParam));
        PreparedStatement st = null;
        String idSender = "";
        String nameSender = "";
        String dispatchKind = "";
        String markedIdSender = "";
        String markedNameSender = "";
        
        try{
        	con = Connector.getConnection(mySource.getSessionId());
            st = con.prepareStatement(mySQL);
            st = prepareParam(st, pParam);
            ResultSet rset = st.executeQuery();
            int rowCount = 0;
            while (rset.next()){
            	idSender = getHTMLValue(rset.getString("ID_SENDER"));
            	nameSender = getHTMLValue(rset.getString("NAME_SENDER"));
            	dispatchKind = getHTMLValue(rset.getString("CD_DISPATCH_KIND"));
            	markedIdSender = prepareFindString(idSender, findString);
            	markedNameSender = prepareFindString(nameSender, findString);
                
                html.append(
                        "<tr><td><div class=\"div_find\" onClick=\"changeOpener('"+idSender+"','"+nameSender+"','"+dispatchKind+"')\">"+getValue2(markedIdSender) +"</div></td>" +
                        "<td><div class=\"div_find\" onClick=\"changeOpener('"+idSender+"','"+nameSender+"','"+dispatchKind+"')\">"+getValue2(markedNameSender)+"</div></td>" +
                        "<td><div class=\"div_find\" onClick=\"changeOpener('"+idSender+"','"+nameSender+"','"+dispatchKind+"')\">"+getValue2(rset.getString("NAME_DISPATCH_KIND"))+"</div></td>" +
                        "<td><div class=\"div_find\" onClick=\"changeOpener('"+idSender+"','"+nameSender+"','"+dispatchKind+"')\">"+getValue2(rset.getString("DESC_SENDER"))+"</div></td>" +
                        "<td><div class=\"div_find\" onClick=\"changeOpener('"+idSender+"','"+nameSender+"','"+dispatchKind+"')\">"+getValue2(rset.getString("PHONE_MOBILE"))+"</div></td>" +
                        "<td><div class=\"div_find\" onClick=\"changeOpener('"+idSender+"','"+nameSender+"','"+dispatchKind+"')\">"+getValue2(rset.getString("EMAIL"))+"</div></td></tr>");
                       
                rowCount++;
            }
            if (rowCount == 0) {
            	html.append(getNoDataFountTR("9"));
            }
        } // try
        catch (SQLException e) {LOGGER.error(html, e);html.append(prepareSQLToLog(mySQL, pParam) + "<br>" + e.toString());}
        catch (Exception el) {LOGGER.error(html, el);html.append(prepareSQLToLog(mySQL, pParam) + "<br>" + el.toString());}
        finally {
            try {
                if (st!=null) {
					st.close();
				}
            } catch (SQLException w) {w.toString();}
            try {
                if (con!=null) {
					con.close();
				}
            } catch (SQLException w) {w.toString();}
            Connector.closeConnection(con);
        } // finally

        html.append("</tbody>");
        html.append("</table>");
        
        return_value.setFunctionHTMLReturnValue(html.toString());
        return return_value;
    } // getContactPrs

    public transport getDSMessageSenderForEmailList(transport mySource) {
    	
    	bc.service.pages page = new bc.service.pages(mySource.getPageNumber(), mySource.getPageRowCount());
    	String l_beg = page.getFirstRowNumber();
    	String l_end = page.getLastRowNumber();
    	
    	transport return_value=new transport();
		return_value.setFunctionName(mySource.getFunctionName());
		return_value.setIdDestination(mySource.getIdDestination());
		return_value.setFunctionParam(mySource.getFunctionParam());
		
		String findString = mySource.getFunctionParam();
		String sender_kind = mySource.getFunctionParam2();
		String email = mySource.getFunctionParam3();
		
		StringBuilder html = new StringBuilder();
		
		ArrayList<bcFeautureParam> pParam = initParamArray();

		String mySQLContactPrs = 
			" SELECT 'PARTNER' cd_dispatch_kind, id_contact_prs id_sender, " +
  			"		 name_contact_prs name_sender, " +
  			"		 sname_jur_prs||', '||name_service_place||', '||name_post desc_sender, " +
  			"		 phone_mobile, email " +
  			"   FROM " + getGeneralDBScheme() + ".vc_contact_prs_priv_all " +
  			"  WHERE 1=1 ";
		if (!isEmpty(email)) {
			mySQLContactPrs = mySQLContactPrs + " AND email = ? ";
			pParam.add(new bcFeautureParam("string", email));
        }
        if (!isEmpty(findString)) {
        	mySQLContactPrs = mySQLContactPrs +
	  			" AND (TO_CHAR(id_contact_prs) LIKE '%'||?||'%' OR " +
	  			"      UPPER(name_contact_prs) LIKE UPPER('%'||?||'%') ";
        	for (int i=0; i<2; i++) {
        	    pParam.add(new bcFeautureParam("string", findString));
        	}
	       	if (isEmpty(email)) {
	       		mySQLContactPrs = mySQLContactPrs +
	      			" OR UPPER(email) LIKE UPPER('%'||?||'%')";
				pParam.add(new bcFeautureParam("string", findString));
	        }
	       	mySQLContactPrs = mySQLContactPrs + ")";
        }
        
        String mySQLNatPrs = 
			" SELECT 'CLIENT' cd_dispatch_kind, id_nat_prs id_sender, full_name name_sender, fact_adr_full desc_sender, " +
  			"		 phone_mobile, email " +
  			"   FROM " + getGeneralDBScheme() + ".vc_nat_prs_short_all " +
  			"  WHERE 1=1 ";
		if (!isEmpty(email)) {
			mySQLNatPrs = mySQLNatPrs + " AND email = ? ";
			pParam.add(new bcFeautureParam("string", email));
        }
        if (!isEmpty(findString)) {
        	mySQLNatPrs = mySQLNatPrs +
	  			" AND (TO_CHAR(id_nat_prs) LIKE '%'||?||'%' OR " +
	  			"      UPPER(full_name) LIKE UPPER('%'||?||'%') ";
        	for (int i=0; i<2; i++) {
        	    pParam.add(new bcFeautureParam("string", findString));
        	}
	       	if (isEmpty(email)) {
	       		mySQLNatPrs = mySQLNatPrs +
	      			" OR UPPER(email) LIKE UPPER('%'||?||'%')";
	       		pParam.add(new bcFeautureParam("string", findString));
	        }
	       	mySQLNatPrs = mySQLNatPrs + ")";
        }
        
        String mySQLSystemUser = 
			" SELECT 'SYSTEM' cd_dispatch_kind, id_user id_sender, name_user name_sender, " +
  			"	 	 desc_user desc_sender, phone_mobile, email" +
  			"   FROM " + getGeneralDBScheme() + ".vc_users_all " +
  			"  WHERE 1=1 ";
		if (!isEmpty(email)) {
			mySQLSystemUser = mySQLSystemUser + " AND email = ? '";
			pParam.add(new bcFeautureParam("string", email));
        }
        if (!isEmpty(findString)) {
        	mySQLSystemUser = mySQLSystemUser +
	  			" AND (TO_CHAR(id_user) LIKE '%'||?||'%' OR " +
	  			"      UPPER(name_user) LIKE UPPER('%'||?||'%') ";
        	for (int i=0; i<2; i++) {
        	    pParam.add(new bcFeautureParam("string", findString));
        	}
        	if (isEmpty(email)) {
    			mySQLSystemUser = mySQLSystemUser +
      				" OR UPPER(email) LIKE UPPER('%'||?||'%')";
    			pParam.add(new bcFeautureParam("string", findString));
            }
        	mySQLSystemUser = mySQLSystemUser + ")";
        }
        
        String mySQLRequest = "";
        if ("CLIENT".equalsIgnoreCase(sender_kind)) {
        	mySQLRequest = mySQLNatPrs;
        } else if ("PARTNER".equalsIgnoreCase(sender_kind)) {
        	mySQLRequest = mySQLContactPrs;
        } else if ("USER".equalsIgnoreCase(sender_kind)) {
        	mySQLRequest = mySQLSystemUser;
        } else {
        	mySQLRequest = 
        		mySQLNatPrs + " UNION ALL " +
        		mySQLContactPrs + " UNION ALL " +
        		mySQLSystemUser;
        }
        
        String mySQL = 
        	" SELECT cd_dispatch_kind, name_dispatch_kind, id_sender, name_sender, desc_sender, " +
        	"        phone_mobile, email " +
	  		"   FROM (SELECT ROWNUM rn, cd_dispatch_kind, " +
	  		"				 DECODE(cd_dispatch_kind, " +
  		  	"                      	'CLIENT', '<b><font color=\"green\">'||name_dispatch_kind||'</font></b>', " +
			"                      	'PARTNER', '<b><font color=\"blue\">'||name_dispatch_kind||'</font></b>', " +
			"                       'SYSTEM', '<font color=\"black\"><b>'||name_dispatch_kind||'</b></font>', " +
			"                       'TRAINING', '<font color=\"darkgray\">'||name_dispatch_kind||'</font>', " +
			"                       'UNKNOWN', '', " +
			"                      	name_dispatch_kind" +
			"				 ) name_dispatch_kind, id_sender, name_sender, desc_sender, " +
        	"                phone_mobile, email " +
  			"           FROM (SELECT a.cd_dispatch_kind, k.name_dispatch_kind, a.id_sender, a.name_sender, a.desc_sender, " +
        	"                        a.phone_mobile, a.email " +
  			"                   FROM ( " + mySQLRequest + ") a, " +
  			"                          " + getGeneralDBScheme() + ".vc_ds_dispatch_kind_all k " +
  			"                  WHERE a.cd_dispatch_kind = k.cd_dispatch_kind" +
        	"                  ORDER BY a.name_sender" +
        	"        ) WHERE ROWNUM < ? " + 
  			" ) WHERE rn >= ?";
        pParam.add(new bcFeautureParam("int", l_end));
        pParam.add(new bcFeautureParam("int", l_beg));
        
        LOGGER.debug(prepareSQLToLog(mySQL, pParam));
        PreparedStatement st = null;
        String idSender = "";
        String nameSender = "";
        String dispatchKind = "";
        String markedIdSender = "";
        String markedNameSender = "";
        
        try{
        	con = Connector.getConnection(mySource.getSessionId());
            st = con.prepareStatement(mySQL);
            st = prepareParam(st, pParam);
            ResultSet rset = st.executeQuery();
            int rowCount = 0;
            while (rset.next()){
            	idSender = getHTMLValue(rset.getString("ID_SENDER"));
            	nameSender = getHTMLValue(rset.getString("NAME_SENDER"));
            	dispatchKind = getHTMLValue(rset.getString("CD_DISPATCH_KIND"));
            	markedIdSender = prepareFindString(idSender, findString);
            	markedNameSender = prepareFindString(nameSender, findString);
                
                html.append(
                        "<tr><td><div class=\"div_find\" onClick=\"changeOpener('"+idSender+"','"+nameSender+"','"+dispatchKind+"')\">"+getValue2(markedIdSender) +"</div></td>" +
                        "<td><div class=\"div_find\" onClick=\"changeOpener('"+idSender+"','"+nameSender+"','"+dispatchKind+"')\">"+getValue2(markedNameSender)+"</div></td>" +
                        "<td><div class=\"div_find\" onClick=\"changeOpener('"+idSender+"','"+nameSender+"','"+dispatchKind+"')\">"+getValue2(rset.getString("CD_DISPATCH_KIND"))+"</div></td>" +
                        "<td><div class=\"div_find\" onClick=\"changeOpener('"+idSender+"','"+nameSender+"','"+dispatchKind+"')\">"+getValue2(rset.getString("DESC_SENDER"))+"</div></td>" +
                        "<td><div class=\"div_find\" onClick=\"changeOpener('"+idSender+"','"+nameSender+"','"+dispatchKind+"')\">"+getValue2(rset.getString("PHONE_MOBILE"))+"</div></td>" +
                        "<td><div class=\"div_find\" onClick=\"changeOpener('"+idSender+"','"+nameSender+"','"+dispatchKind+"')\">"+getValue2(rset.getString("EMAIL"))+"</div></td></tr>");
                       
                rowCount++;
            }
            if (rowCount == 0) {
            	html.append(getNoDataFountTR("9"));
            }
        } // try
        catch (SQLException e) {LOGGER.error(html, e);html.append(prepareSQLToLog(mySQL, pParam) + "<br>" + e.toString());}
        catch (Exception el) {LOGGER.error(html, el);html.append(prepareSQLToLog(mySQL, pParam) + "<br>" + el.toString());}
        finally {
            try {
                if (st!=null) {
					st.close();
				}
            } catch (SQLException w) {w.toString();}
            try {
                if (con!=null) {
					con.close();
				}
            } catch (SQLException w) {w.toString();}
            Connector.closeConnection(con);
        } // finally

        html.append("</tbody>");
        html.append("</table>");
        
        return_value.setFunctionHTMLReturnValue(html.toString());
        return return_value;
    } // getContactPrs

    public transport getCallCenterInquirerList(transport mySource) {
    	
    	bc.service.pages page = new bc.service.pages(mySource.getPageNumber(), mySource.getPageRowCount());
    	String l_beg = page.getFirstRowNumber();
    	String l_end = page.getLastRowNumber();
    	
    	transport return_value=new transport();
		return_value.setFunctionName(mySource.getFunctionName());
		return_value.setIdDestination(mySource.getIdDestination());
		return_value.setFunctionParam(mySource.getFunctionParam());
		return_value.setFunctionParam2(mySource.getFunctionParam2());
		
		String findString = mySource.getFunctionParam();
		
		StringBuilder html = new StringBuilder();
		
		ArrayList<bcFeautureParam> pParam = initParamArray();

		String mySQL = 
        	" SELECT rn, id_cc_inquirer, name_cc_inquirer, desc_cc_inquirer, " +
        	"        name_cc_inquirer_state, date_cc_inquirer_frmt " +
        	"	FROM (SELECT ROWNUM rn, id_cc_inquirer, name_cc_inquirer, desc_cc_inquirer, " +
        	"                DECODE(cd_cc_inquirer_state, " +
			"                  		'CONSTUCTION', '<b><font color=\"blue\">'||name_cc_inquirer_state||'</font></b>', " +
			"                  		'APPROVE', '<b><font color=\"green\">'||name_cc_inquirer_state||'</font></b>', " +
			"                  		'CANCEL', '<b><font color=\"red\">'||name_cc_inquirer_state||'</font></b>', " +
			"                  		name_cc_inquirer_state" +
			"                ) name_cc_inquirer_state," +
        	"                date_cc_inquirer_frmt " +
        	"	        FROM (SELECT * " +
        	"                   FROM " + getGeneralDBScheme()+".vc_cc_inquirer_club_all ";
        if (!isEmpty(findString)) {
        	mySQL = mySQL + 
				" WHERE (UPPER(name_cc_inquirer) LIKE UPPER('%'||?||'%') " +
				"    OR UPPER(desc_cc_inquirer) LIKE UPPER('%'||?||'%')) ";
        	for (int i=0; i<2; i++) {
        	    pParam.add(new bcFeautureParam("string", findString));
        	}
        }
        pParam.add(new bcFeautureParam("int", l_end));
        pParam.add(new bcFeautureParam("int", l_beg));
        mySQL = mySQL +
        	"                  ORDER BY date_cc_inquirer DESC, name_cc_inquirer" +
        	"        ) WHERE ROWNUM < ? " + 
  			" ) WHERE rn >= ?";
        
        LOGGER.debug(prepareSQLToLog(mySQL, pParam));
        PreparedStatement st = null;
        String idInquirer = "";
        String nameInquirer = "";
        String markedIdInquirer = "";
        String markedNameInquirer = "";
        
        try{
        	con = Connector.getConnection(mySource.getSessionId());
            st = con.prepareStatement(mySQL);
            st = prepareParam(st, pParam);
            ResultSet rset = st.executeQuery();
            int rowCount = 0;
            while (rset.next()){
            	idInquirer = getHTMLValue(rset.getString("ID_CC_INQUIRER"));
            	nameInquirer = getHTMLValue(rset.getString("NAME_CC_INQUIRER"));
            	markedIdInquirer = prepareFindString(idInquirer, findString);
            	markedNameInquirer = prepareFindString(nameInquirer, findString);
                
                html.append(
                        "<tr>" +
                        "<td><div class=\"div_find\" onClick=\"changeOpener('"+idInquirer+"','"+nameInquirer+"')\">"+getValue2(markedIdInquirer) +"</div></td>" +
                        "<td><div class=\"div_find_marked\" onClick=\"changeOpener('"+idInquirer+"','"+nameInquirer+"')\">"+getValue2(markedNameInquirer)+"</div></td>" +
                        "<td><div class=\"div_find\" onClick=\"changeOpener('"+idInquirer+"','"+nameInquirer+"')\">"+getValue2(rset.getString("DESC_CC_INQUIRER"))+"</div></td>" +
                        "<td><div class=\"div_find\" onClick=\"changeOpener('"+idInquirer+"','"+nameInquirer+"')\">"+getValue2(rset.getString("NAME_CC_INQUIRER_STATE"))+"</div></td>" +
                        "<td><div class=\"div_find\" onClick=\"changeOpener('"+idInquirer+"','"+nameInquirer+"')\">"+getValue2(rset.getString("DATE_CC_INQUIRER_FRMT"))+"</div></td>" +
                        "</tr>");
                       
                rowCount++;
            }
            if (rowCount == 0) {
            	html.append(getNoDataFountTR("5"));
            }
        } // try
        catch (SQLException e) {LOGGER.error(html, e);html.append(prepareSQLToLog(mySQL, pParam) + "<br>" + e.toString());}
        catch (Exception el) {LOGGER.error(html, el);html.append(prepareSQLToLog(mySQL, pParam) + "<br>" + el.toString());}
        finally {
            try {
                if (st!=null) {
					st.close();
				}
            } catch (SQLException w) {w.toString();}
            try {
                if (con!=null) {
					con.close();
				}
            } catch (SQLException w) {w.toString();}
            Connector.closeConnection(con);
        } // finally

        html.append("</tbody>");
        html.append("</table>");
        
        return_value.setFunctionHTMLReturnValue(html.toString());
        return return_value;
    } // getGiftsList

    public transport getClubList(transport mySource) {
    	
    	bc.service.pages page = new bc.service.pages(mySource.getPageNumber(), mySource.getPageRowCount());
    	String l_beg = page.getFirstRowNumber();
    	String l_end = page.getLastRowNumber();
    	
    	transport return_value=new transport();
		return_value.setFunctionName(mySource.getFunctionName());
		return_value.setIdDestination(mySource.getIdDestination());
		return_value.setFunctionParam(mySource.getFunctionParam());
		
		String findString = mySource.getFunctionParam();
		
		StringBuilder html = new StringBuilder();
		
		ArrayList<bcFeautureParam> pParam = initParamArray();

		String mySQL = 
        	" SELECT id_club, name_club, sname_club, sname_operator " +
	  		"   FROM (SELECT ROWNUM rn,id_club, name_club, sname_club, sname_operator " +
  			"           FROM (SELECT * " +
  			"                   FROM " + getGeneralDBScheme() + ".vc_club_names ";
        if (!isEmpty(findString)) {
        	mySQL = mySQL +
        	"          WHERE (TO_CHAR(id_club) LIKE UPPER('%'||?||'%') OR " +
  			"                 UPPER(name_club) LIKE UPPER('%'||?||'%') OR " +
  			"                 UPPER(sname_club) LIKE UPPER('%'||?||'%')) ";
        	for (int i=0; i<3; i++) {
        	    pParam.add(new bcFeautureParam("string", findString));
        	}
        }
        pParam.add(new bcFeautureParam("int", l_end));
        pParam.add(new bcFeautureParam("int", l_beg));
        mySQL = mySQL +
        	"                  ORDER BY id_club" +
			"        ) WHERE ROWNUM < ? " + 
  			" ) WHERE rn >= ?";
        
        LOGGER.debug(prepareSQLToLog(mySQL, pParam));
        PreparedStatement st = null;
        String idClub = "";
        String nameClub = "";
        String shortNameClub = "";
        String markedIdClub = "";
        String markedNameClub = "";
        String markedShortNameClub = "";
        try{
        	con = Connector.getConnection(mySource.getSessionId());
            st = con.prepareStatement(mySQL);
            st = prepareParam(st, pParam);
            ResultSet rset = st.executeQuery();
            int rowCount = 0;
            while (rset.next()){
            	idClub = getHTMLValue(rset.getString("ID_CLUB"));
            	nameClub = getHTMLValue(rset.getString("NAME_CLUB"));
            	shortNameClub = getHTMLValue(rset.getString("SNAME_CLUB"));
            	markedIdClub = prepareFindString(idClub, findString);
            	markedNameClub = prepareFindString(nameClub, findString);
            	markedShortNameClub = prepareFindString(shortNameClub, findString);

                html.append(
                        "<tr><td><div class=\"div_find\" onClick=\"changeOpener('"+idClub+"','"+shortNameClub+"')\">"+getValue2(markedIdClub) +"</div></td>" +
                        "<td><div class=\"div_find_marked\" onClick=\"changeOpener('"+idClub+"','"+shortNameClub+"')\">"+getValue2(markedNameClub)+"</div></td>" +
                        "<td><div class=\"div_find\" onClick=\"changeOpener('"+idClub+"','"+shortNameClub+"')\">"+getValue2(markedShortNameClub)+"</div></td>" +
                        "<td><div class=\"div_find\" onClick=\"changeOpener('"+idClub+"','"+shortNameClub+"')\">"+getValue2(rset.getString("SNAME_OPERATOR"))+"</div></td></tr>");
                       
                rowCount++;
            }
            if (rowCount == 0) {
            	html.append(getNoDataFountTR("9"));
            }
        } // try
        catch (SQLException e) {LOGGER.error(html, e);html.append(prepareSQLToLog(mySQL, pParam) + "<br>" + e.toString());}
        catch (Exception el) {LOGGER.error(html, el);html.append(prepareSQLToLog(mySQL, pParam) + "<br>" + el.toString());}
        finally {
            try {
                if (st!=null) {
					st.close();
				}
            } catch (SQLException w) {w.toString();}
            try {
                if (con!=null) {
					con.close();
				}
            } catch (SQLException w) {w.toString();}
            Connector.closeConnection(con);
        } // finally

        html.append("</tbody>");
        html.append("</table>");
        
        return_value.setFunctionHTMLReturnValue(html.toString());
        return return_value;
    } // getClubList
    

}
