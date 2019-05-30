package bc;

import java.net.URLDecoder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import webpos.wpChequeObject;
import webpos.wpDealerMarginObject;
import webpos.wpHeaders;
import webpos.wpNatPrsRoleObject;
import webpos.wpRobokassaMarginObject;
import webpos.wpTelegramObject;
import webpos.wpTerminalObject;

import java.util.*;

import bc.connection.Connector;
import bc.objects.bcDictionary;
import bc.objects.bcXML;
import bc.service.bcCurrencyRecord;
import bc.service.bcDictionaryRecord;
import bc.service.bcFeautureParam;
import bc.service.bcWebPosMenu;


// TODO !!! REFACTORING !!!
public class bcWebPosBean extends bcBase {
	private static final Logger LOGGER = Logger.getLogger(bcWebPosBean.class);

	public wpHeaders wpHeader = new wpHeaders(this.getDateFormat());
	
	public ArrayList<bcDictionaryRecord> theme = new ArrayList<bcDictionaryRecord>();
	public ArrayList<bcDictionaryRecord> webposTransType = new ArrayList<bcDictionaryRecord>();
	public ArrayList<bcDictionaryRecord> webposTransTypeShort = new ArrayList<bcDictionaryRecord>();
	public ArrayList<bcDictionaryRecord> webposTransState = new ArrayList<bcDictionaryRecord>();
	public ArrayList<bcDictionaryRecord> webposTelegramState = new ArrayList<bcDictionaryRecord>();
	
	public bcXML webposXML = bcDictionary.getInstance("webpos");
	
	public String C_REMIND_PIN_ON_LOGIN         = "29991";
	public String C_MANY_USERS_ON_LOGIN         = "29990";
	public String C_MAX_ERROR_CONNECTION        = "29105";
	
	public String C_MENU_ACCESS_DENIED      	= "29901";
	public String C_TABSHEET_ACCESS_DENIED      = "29902";
	public String C_FUNCTION_ACCESS_DENIED      = "29903";
	public String C_SMS_CONFIRM_CODE_ERROR      = "29904";
	public String C_PIN_ERROR      				= "29905";
	
	public String C_NEED_END_INFO               = "20000";
	public String C_MEMBERSHIP_FEE_ERROR  		= "20001";
	public String C_ISNT_ENOUGH_POINTS         	= "20002";
	public String C_CARD_NOT_GIVEN	         	= "20003";
	public String C_CARD_NOT_QUESTIONED       	= "20004";
	public String C_CARD_NOT_ACTIVATED         	= "20005";
	public String C_NEED_PIN                   	= "20006";
	public String C_NEED_SMS_CONFIRMATION      	= "20007";
	public String C_NEED_ACTIVATION_CODE       	= "20008";
	public String C_ENOUGH_MEANS              	= "20009";
	public String C_NEED_MANY_FEE              	= "20010";
	public String C_SMS_CONFIRM_CREATED        	= "20011";
	public String C_CONFIRM_RETURN             	= "20099";
	
	/*public String C_TP_NEED_SUBSCRIBE           = "20101";
	public String C_TP_NEED_SUBSCRIBE_ENT      	= "20102";
	public String C_TP_NEED_SUBSCRIBE_MEM   	= "20103";
	public String C_TP_NEED_SUBSCRIBE_E_M   	= "20104";
	public String C_TP_NEED_SUBSCRIBE_CONF   	= "20105";
	public String C_TP_NEED_SUBSCRIBE_CONF_ENT  = "20106";
	public String C_TP_NEED_SUBSCRIBE_CONF_MEM  = "20107";
	public String C_TP_NEED_SUBSCRIBE_CONF_E_M  = "20108";
	public String C_TP_NEED_ADMIN_CONFIRM       = "20109";
	public String C_TP_NEED_ENTRANCE_FEE        = "20110";
	public String C_TP_NEED_MEMBERSHIP_FEE      = "20111";
	public String C_TP_NEED_ENT_MEMBER_FEE      = "20112";*/
	
	public String C_REMIND_PASSWORD_SMS_PLACEHOLDER  = "XX XX XX XX";
	public String C_PIN_PLACEHOLDER                  = "XXXX";
	public String C_CONFIRM_OPER_SMS_PLACEHOLDER     = "XXXX";
	public String C_ACTIVATION_CODE_PLACEHOLDER      = "XXXXX";

	public String C_TERM_CAN_OPER_DIFFERENT_CURRENCY = "1";
	
	public int C_SIGNATURE_MAX_SEND_COUNT  			= 2;
	public int C_INVOICE_MAX_SEND_COUNT  			= 2;
	public int C_MAX_ENTER_SMS_CONFIRM_CODE_COUNT  	= 2;

	public void setSessionParam(HttpServletRequest request) {
		setSessionId(request.getSession().getId());
		//header.setPamam();
	}
	
	HashMap<String,String> formParam = new HashMap<String,String>();
	
	public wpNatPrsRoleObject loginUserNatPrsRole = null;
	public wpTerminalObject loginTerm = new wpTerminalObject();
	
	public String getFormParam(String pParamName, String pDefaultValue) {
		String lReturn = "";
		if (formParam.containsKey(pParamName.toUpperCase())) {
			lReturn = formParam.get(pParamName.toUpperCase());
		} else {
			lReturn = pDefaultValue;
		}
		return lReturn;
	}
	
	public String getFormParam(String pParamName) {
		return getFormParam(pParamName, "");
	}
	
	private int errorConnectionCount = 0;
	public int C_MAX_ERROR_CONNECTION_COUNT = 2;
	
	public void addErrorConnectionCount() {
		errorConnectionCount ++;
	}
	
	public void clearErrorConnectionCount() {
		errorConnectionCount = 0;
	}
	
	public int getErrorConnectionCount() {
		return errorConnectionCount;
	}
	
	public void setFormParam(String pParamName, String pFormValue, String pDefaultValue) {
		String lValue = "";
		if (pFormValue==null) {
			lValue = pDefaultValue;
		} else {
			lValue = pFormValue;
		}
		formParam.put(pParamName.toUpperCase(), lValue);
	}

	public bcWebPosBean() {
	}

	public String getContactsPhoneNumbers() {

		return "(866) 493-9580<br>(866) 493-9580";
	}
    
	public String getNoCasheValue() {
		return "" + (new Date().getTime()) + Math.random();
	}
    //private String lastCheckCardSerialNumber = "";
    //private String lastCheckCardIdIssuer = "";
    //private String lastCheckCardIdPaymentSystem = "";
    private String lastCheckCdCard1 = "";
    
    /*public void setLastCheckCard (String pCardSerialNumber, String pCardIdIssuer, String pCardIdPaymentSystem) {
    	lastCheckCardSerialNumber = pCardSerialNumber;
        lastCheckCardIdIssuer = pCardIdIssuer;
        lastCheckCardIdPaymentSystem = pCardIdPaymentSystem;
    }*/
    
    public void setLastCheckCard (String pCdCard1) {
    	lastCheckCdCard1 = pCdCard1;
    }
    
    public String hideCdCard1 (String pCdCard1) {
    	if (isEmpty(pCdCard1)) {
    		return "";
    	}
    	String lCard = pCdCard1;
    	String lReturn = lCard.substring(1,1);
    	//String lRest = "";
    	int lCnt = lCard.length() - 5;
    	for (int i = 0; i <= lCnt; i++) {
    		lReturn = lReturn + "*";
    	}
    	lReturn = lReturn + lCard.substring(lCnt+1);
    	//lRest = lReturn;
    	lCard = lReturn;
    	//System.out.println("lCard="+lCard);
    	lCnt = Math.round(lCard.length()/4);
    	//System.out.println("lCnt="+lCnt);
    	lReturn = "";
    	for (int i = 0; i <= lCnt; i++) {
        	//System.out.println("i="+i);
    		if ("".equalsIgnoreCase(lReturn)) {
    			lReturn = lCard.substring(0, 4);
    		} else {
    			if (i*4+4 < lCard.length()) {
    				lReturn = lReturn + " " + lCard.substring(i*4, i*4+4);
    			} else {
    				lReturn = lReturn + " " + lCard.substring(i*4);
    			}
            	//System.out.println("lReturn="+lReturn);
    		}
    		/*if (i*4+5 < lCard.length()) {
    			lRest = lCard.substring(i*4+5);
    		} else {
    			lRest = "";
    		}*/
    	}
    	//if (lCnt*4+1 < lCard.length()) {
    	//	lReturn = lReturn + " " + lCard.substring(lCnt*4+1);
    	//}
    	return lReturn;
    }
    
    /*public boolean isLastCheckCard (String pCardSerialNumber, String pCardIdIssuer, String pCardIdPaymentSystem) {
    	boolean returnValue = false;
    	if (pCardSerialNumber.equalsIgnoreCase(lastCheckCardSerialNumber) &&
    			pCardIdIssuer.equalsIgnoreCase(lastCheckCardIdIssuer) &&
    			pCardIdPaymentSystem.equalsIgnoreCase(lastCheckCardIdPaymentSystem)) {
    		returnValue = true;
    	}
        return returnValue;
    }*/
    
    public boolean isLastCheckCard (String pCdCard1) {
    	boolean returnValue = false;
    	if (!isEmpty(pCdCard1)) {
	    	if (pCdCard1.equalsIgnoreCase(lastCheckCdCard1)) {
	    		returnValue = true;
	    	}
    	}
        return returnValue;
    }
	
	public boolean isOperationPeriodSet = false;
	public boolean isInvoicePeriodSet = false;
	public boolean isQuestionairePeriodSet = false;
	
	public String getTerminalTitle () {
		return webposXML.getfieldTransl2(getLanguage(),"application_name")+": WebPOS";
	}
	
	public String getLoginFormTitle () {
		StringBuilder lReturn = new StringBuilder();
		lReturn.append("<h1><img class=\"img_logo\" src=\"images/smpu-logo_small.png\">"); 
		lReturn.append("<img id=\"imgWait\" src=\"images/ajax-loader-circle.gif\" align=\"middle\" style=\"visibility: hidden\">"); 
		lReturn.append(getTerminalTitle());
		lReturn.append("</h1>");
		return lReturn.toString();
	}

	public String getTitleLanguages(String pLanguage) {
		StringBuilder lReturn = new StringBuilder();

		if ("EN".equalsIgnoreCase(pLanguage)) {
			lReturn.append("<span class=\"lang\">EN</span>&nbsp;|&nbsp;");
		} else {
			lReturn.append("<a class=\"lang\" href=\"main.jsp?lang=EN\">EN</a>&nbsp;|&nbsp;");
		}
		if ("RU".equalsIgnoreCase(pLanguage)) {
			lReturn.append("<span class=\"lang\">RU</span>&nbsp;|&nbsp;");
		} else {
			lReturn.append("<a class=\"lang\" href=\"main.jsp?lang=RU\">RU</a>&nbsp;|&nbsp;");
		}
		if ("UA".equalsIgnoreCase(pLanguage)) {
			lReturn.append("<span class=\"lang\">UA</span>");
		} else {
			lReturn.append("<a class=\"lang\" href=\"main.jsp?lang=UA\">UA</a>");
		}

		return lReturn.toString();
	}

	public String getTitleUserParam(String pTermCurrency) {
		StringBuilder lReturn = new StringBuilder();

		lReturn.append("<div class=\"user_img\" onclick=\"ajaxpage('admin/setting.jsp?tab=1', 'div_main')\" title=\"" + webposXML.getfieldTransl("title_user", false) + "\"></div>");
		lReturn.append("<span class=\"user\" onclick=\"ajaxpage('admin/setting.jsp?tab=1', 'div_main')\" title=\"" + webposXML.getfieldTransl("title_user", false) + "\">" + getLoginUserNatPrsFIOInitial() + "</span>");
		lReturn.append("<span class=\"service_place\" onclick=\"ajaxpage('admin/setting.jsp?tab=1', 'div_main')\" title=\"" + webposXML.getfieldTransl("title_service_place", false) + "\">" + getLoginUserServicePlaceName() + "&nbsp;</span>");
		lReturn.append("<span class=\"terminal\" onclick=\"ajaxpage('admin/setting.jsp?tab=2', 'div_main')\" title=\"" + webposXML.getfieldTransl("title_terminal", false) + "\">" + getCurrentTerm() + ", " + pTermCurrency + "</span>");

		return lReturn.toString();
	}

	public String getGoBackButton(String pHyperLink, String pDivName) {
		StringBuilder html = new StringBuilder();
		html.append("<img class=\"button_back\" src=\"" + getThemePath() + "/images/menu/back_button.png\" onclick=\"ajaxpage('"
				//+ getContextPath()
				+ pHyperLink
				+ "', '" + 
				pDivName + "')\""
				+ " title=\"" + buttonXML.getfieldTransl("go_back", false) + "\"" 
				+ ">");
		return html.toString();
	}
	
	String accessDeniedType = "";
	
	public boolean hasMenuPermission(String pFormName, String pAccessLevel, wpTerminalObject term) {
		boolean lReturn = false;
		accessDeniedType = "";
		//System.out.println("lReturn(1)="+lReturn);
		if ("ACTIVE".equalsIgnoreCase(term.getValue("CD_TERM_STATUS"))) {
			lReturn = true;
		} else {
			lReturn = false;
			accessDeniedType = "NOACTIVE_TERMINAL";
		}
		//System.out.println("lReturn(2)="+lReturn);
		if (lReturn) {
			if (!isEmpty(pFormName)) {
				if (hasMenuElementPermission(pFormName, pAccessLevel)) {
					lReturn = true;
				} else {
					lReturn = false;
					accessDeniedType = "MENU_ACCESS_DENIED";
				}
			}
		}
		//System.out.println("lReturn(3)="+lReturn);
		return lReturn;
	}
	
	/*public boolean hasMenuPermission(String pFormName, wpTerminalObject term) {
		return hasMenuPermission(pFormName, null, term);
	}*/
	
	public boolean hasTerminalPermission(wpTerminalObject term) {
		return hasMenuPermission("", null, term);
	}
	
	public String getErrorPermissionMessage(String pFormName, wpTerminalObject term) {
		StringBuilder lReturn = new StringBuilder();
		if (accessDeniedType == "NOACTIVE_TERMINAL") {
			lReturn.append(getNoActiveTerminalMessage(term));
		} else if (accessDeniedType == "MENU_ACCESS_DENIED") {
			lReturn.append(getMenuAccessDeniedMessage(pFormName, false));
		}
		return lReturn.toString();
	}
	
	public String getErrorPermissionMessageShort(String pFormName, wpTerminalObject term) {
		StringBuilder lReturn = new StringBuilder();
		if (accessDeniedType == "NOACTIVE_TERMINAL") {
			lReturn.append(getNoActiveTerminalMessage(term));
		} else if (accessDeniedType == "MENU_ACCESS_DENIED") {
			lReturn.append(getMenuAccessDeniedMessage(pFormName, true));
		}
		return lReturn.toString();
	}
	
	private String getNoActiveTerminalMessage(wpTerminalObject term) {
		StringBuilder lReturn = new StringBuilder();
		lReturn.append("<form id=\"access_denied\" action=\"main.jsp\" method=\"POST\">");
		lReturn.append("<input type=\"hidden\" name=\"lang\" value=\"" + getLanguage() + "\">");
		lReturn.append("<input type=\"hidden\" name=\"do\" value=\"exit\">");
		lReturn.append("<table class=\"action_table\" style=\"margin-top: 10px;\">");
		lReturn.append("<tr><td align=\"center\" style=\"padding: 10px;\"><font style=\"font-size: 22px; color:red; font-weight: bold;\">" + webposXML.getfieldTransl2(getLanguage(),"operation_error") + "</font></td></tr>");
		if (isEmpty(term.getValue("CD_TERM_STATUS"))) {
			lReturn.append("<tr><td align=\"center\">" + webposXML.getfieldTranslNoDiv(getLanguage(),"title_entrance_error") + "</td></tr>");
			lReturn.append("<tr><td align=\"center\"><input class=\"button\" value=\"" + buttonXML.getfieldTranslNoDiv(getLanguage(),"button_login") + "\" type=\"submit\"></td></tr>");
		} else if (!"ACTIVE".equalsIgnoreCase(term.getValue("CD_TERM_STATUS"))) {
			lReturn.append("<tr><td align=\"center\">" + webposXML.getfieldTranslNoDiv(getLanguage(),"title_terminal_blocked") + "</td></tr>");
		}
		lReturn.append("<tr><td align=\"center\">&nbsp;</td></tr>");
		lReturn.append("</table>");
		lReturn.append("</form>");
		return lReturn.toString();
	}
	
	private String getMenuAccessDeniedMessage(String pFormName, boolean pShortForm) {
		StringBuilder lReturn = new StringBuilder();
		if (!pShortForm) {
			lReturn.append("<table " + getTableDetail2Param() + ">\n");
			lReturn.append("<tr>\n");
			lReturn.append("<td><div id=\"div_action_big\">\n");
		}
		lReturn.append("<h1>" + currentMenu.getTitleMenuElement() +"</h1>\n");
		lReturn.append("<table class=\"action_table\" style=\"margin-top: 10px;\">\n");
		lReturn.append("<tr><td align=\"center\" style=\"padding: 10px;\"><font style=\"font-size: 22px; color:red; font-weight: bold;\">" + webposXML.getfieldTransl("operation_error", false) + "</font></td></tr>\n");
		lReturn.append("<tr><td align=\"center\">" + webposXML.getfieldTransl2(getLanguage(), "title_access_denied") + "</td></tr>\n");
		lReturn.append("<tr><td align=\"center\">&nbsp;</td></tr>\n");
		lReturn.append("</table>\n");
		if (!pShortForm) {
			lReturn.append("</div></td></tr></table>\n");
		}
		return lReturn.toString();
	}

	public String getTitleUserParamShort(int maxLength) {
		StringBuilder lReturn = new StringBuilder();
		
		String lTermTitleInitial = getTerminalTitle();
		String lTermTitle = lTermTitleInitial;
		String lServicePlaceInitial = getLoginUserServicePlaceName();
		lServicePlaceInitial = prepareFromHTML(lServicePlaceInitial);
		String lServicePlace = lServicePlaceInitial;
		String lUserInitial = getLoginUserNatPrsFIOInitial();
		lUserInitial = prepareFromHTML(lUserInitial);
		String lUser = lUserInitial;
		if (lTermTitle.length() > maxLength) {
			lTermTitle = lTermTitle.substring(0,maxLength) + "...";
			lTermTitle = prepareToHTML(lTermTitle);
		}
		if (lServicePlace.length() > maxLength) {
			lServicePlace = lServicePlace.substring(0,maxLength) + "...";
			lServicePlace = prepareToHTML(lServicePlace);
		}
		if (lUser.length() > maxLength) {
			lUser = lUser.substring(0,maxLength) + "...";
			lUser = prepareToHTML(lUser);
		}

		//lReturn.append("<span class=\"smpy-title\" onclick=\"ajaxpage('admin/setting.jsp?tab=1', 'div_main')\" title=\"" + lTermTitleInitial + "\">" + lTermTitle + "</span>");
		lReturn.append("<span class=\"user\" onclick=\"ajaxpage('admin/setting.jsp?tab=1', 'div_main')\" title=\"" + webposXML.getfieldTransl("title_user", false) + " " + lUserInitial + "\">" + lUser + "</span>");
		lReturn.append("<span class=\"service_place\" onclick=\"ajaxpage('admin/setting.jsp?tab=1', 'div_main')\" title=\"" + webposXML.getfieldTransl("title_service_place", false) + " " + lServicePlaceInitial.replace("\"", "&ldquo;") + "\">" + lServicePlace + "&nbsp;</span>");
		lReturn.append("<span class=\"terminal\" onclick=\"ajaxpage('admin/setting.jsp?tab=2', 'div_main')\" title=\"" + webposXML.getfieldTransl("title_terminal", false) + "\">" + getCurrentTerm() + "</span>");

		return lReturn.toString();
	}
	
	public String getStatusBarHTML(String pCurrentLanguage, String pTermCurrency) {
		StringBuilder lReturn = new StringBuilder();
		
		lReturn.append("<div id=\"div_status_bar\">");
		lReturn.append("	<div id=\"div_status_bar_left\">");
		lReturn.append(getTitleUserParamShort(25));
		lReturn.append("	</div>");
		lReturn.append("	<div id=\"div_status_bar_center\">");
		lReturn.append("		<div class=\"logo\"></div>");
		//lReturn.append("		<div class=\"imgwait\"><img id=\"imgWait\" src=\"images/ajax-loader-circle.gif\" align=\"middle\" style=\"visibility: hidden\"></div>"); 
		lReturn.append("	</div>");
		lReturn.append("	<div id=\"div_status_bar_right\">");
		lReturn.append("		<div id=\"language\">");
		lReturn.append(getTitleLanguages(pCurrentLanguage));
		lReturn.append("			<a class=\"exit\" href=\"main.jsp?do=exit\" title=\"Выход\">&nbsp;</a>");
		lReturn.append("		</div>");
		lReturn.append("		<div id=\"phone\">");
		lReturn.append(getContactsPhoneNumbers());
		lReturn.append("		</div>");
		lReturn.append("	</div>");
		lReturn.append("</div>");

		return lReturn.toString();
	}
	
	public String getStatusBarHTML(String pTermCurrency) {
		return getStatusBarHTML(this.getLanguage(), pTermCurrency);
	}

	public String getDetailTableFirstColumnWidth() {
		return " width=\"45%\" ";	
	}

	public String getTitleLanguages() {
		return (getTitleLanguages(this.getLanguage()));	
	}
	
	public String loginCode = "";
	// private static Connection con = null;

	private String moduleName = "webpos";

	public boolean logIn(String pUsername, String pPassword, String pSMSConfirmCode, HttpServletRequest request) {
		return logIn(pUsername, pPassword, moduleName, pSMSConfirmCode, request);
	}
	
	public String getClientIp(HttpServletRequest request) {
		return getClientIpAddr(request);
	}

	public boolean logIn(String pUsername, String pPassword, String pModuleName, String pSMSConfirmCode, HttpServletRequest request) {
		Connector.removeSessionId(sessionId);
		LOGGER.debug("logIn: pUsername=" + pUsername);
		//LOGGER.debug("logIn: pPassword=" + pPassword);
		LOGGER.debug("logIn: pModuleName=" + pModuleName);
		LOGGER.debug("logIn: sessionId=" + sessionId);
		
		Connection con = null;
		if (isEmpty(sessionId) || isEmpty(pUsername)) {
			con = Connector.getConnection(sessionId);
		} else {
			con = Connector.getConnection(pModuleName, pUsername, pPassword, sessionId, this.getLanguage(), getClientIpAddr(request), pSMSConfirmCode);
		}
		setErrorCode(Connector.errorCode.get());
		setErrorMessage(Connector.errorMessage.get());
		if (con == null) {
			return false;
		}
		//System.out.println("3");
		Connector.closeConnection(con);
		//System.out.println("4");
		clearAllDictionaries();
		//System.out.println("5");
		loginUser.getCurrentUserFeature();
		//System.out.println("6");
		//loginUserParam.getCurntUserParamFeature();
		//System.out.println("7");
		loginUserNatPrsRole = new wpNatPrsRoleObject(loginUser.getValue("ID_NAT_PRS_ROLE"));
		this.setIsLoged(true);
		this.setSessionLanguage();
		this.setSessionDateFormat();
		this.setLoginUserTheme();
		//System.out.println("8");
		/*if (pModuleName == null || "".equalsIgnoreCase(pModuleName)
				|| "crm".equalsIgnoreCase(pModuleName)) {
			moduleName = "crm";
		} else {
			moduleName = pModuleName;
		}*/
		header.setLanguage(this.getLanguage());
		header.setIdClub(this.getCurrentClubID());
		//System.out.println("9");
		this.getLookups();
		this.getWPCurrencies();
		theme.clear();
		webposTransType.clear();
		webposTransTypeShort.clear();
		webposTransState.clear();
		webposTelegramState.clear();
		this.lastCheckCdCard1 = "";
		return true;
		//return openTermSession();
	}


	/*public static String idTermSession;

	public String getIdTermSession() {
		return idTermSession;
	}

	public static void setIdTermSession(String pIdTermSession) {
		idTermSession = pIdTermSession;
	}
	
	public boolean openTermSession() {
		boolean lReturn = true;
		
		String mySQL = "{? = call " + getGeneralDBScheme()
				+ ".pack$webpos_ui.open_term_ses(?,?,?)}";

		String[] results = new String[3];

		String[] pParam = new String[1];
		pParam[0] = loginUser.getValue("ID_USER");

		LOGGER.debug(prepareSQLToLog(mySQL, pParam));
		results = myCallFunctionParam(mySQL, pParam, 3);

		String resultInt = results[0];
		String id_term_ses = results[1];
		//String resultMessage = results[2];

		if (!"0".equalsIgnoreCase(resultInt)) {
			setErrorSQLCode(1234);
			lReturn = false;
		}
		setIdTermSession(id_term_ses);
		return lReturn;
	}*/

	public boolean logInCurrent(String pSessionId) {
		boolean l_return = true;
		setSessionId(pSessionId);
		header.setPamam();
		if (!checkUser()) {
			this.setIsLoged(false);
			l_return = false;
			this.clearLink();
		}
		LOGGER.debug("logInCurrent('" + pSessionId + "') = " + l_return);
		return l_return;
	}

	public String getModulePath() {
		//return getContextPath() + "/webpos";
		return "";
	}

	public String getCurrentThemeId() {
		return "0";
	}
	
	private void setLoginUserTheme () {
		String theme = getUIUserParam("WEBPOS_THEME");
		if (!isEmpty(theme)) {
			setCurrentThemeFolder(theme);
		}
	}	
	
	String currentThemeFolder = "default";

	public String getCurrentThemeFolder() {
		return currentThemeFolder;
	}
	
	private String chequeSaveFormat = "TXT";
	
	public String getChequeSaveFormat () {
		return this.chequeSaveFormat;
	}
	
	public void setChequeSaveFormat (String pChequeSaveFormat) {
		this.chequeSaveFormat = pChequeSaveFormat;
	}

	public void setCurrentThemeFolder(String pCurrentThemeFolder) {
		currentThemeFolder = pCurrentThemeFolder;
	}

	public String getThemePath() {
		return /*getModulePath() + */"theme/" + getCurrentThemeFolder();
	}

	public String getThemeOptions(String id_theme, boolean isNull) {
		return getSelectBodyFromQuery(
				" SELECT id_theme, name_theme, exist_flag "
						+ "   FROM " + getGeneralDBScheme()
						+ ".vp$theme_all" + "  ORDER BY name_theme",
						id_theme, isNull, theme, false);
	}
	
	public String getHelpButton(String pIdMenu, String pDivName) {
		//return "<img class=\"context_help\" src=\"" + getThemePath() + "/images/help.png\" title=\"" + buttonXML.getfieldTransl("button_help", false) + "\" onclick=\"ajaxpage('admin/helpspecs.jsp?id_menu=" + pIdMenu + "', '" + pDivName + "')\">";
		return "";
	}

	public void getWPCurrencies() {
		PreparedStatement st = null;
		Connection con = null;

		String mySQL = "SELECT * FROM " + getGeneralDBScheme()
				+ ".vp$currency_all ORDER BY cd_currency";

		String cd_currency = "";
		String scd_currency = "";
		String name_currency = "";
		String sname_currency = "";
		String exist_flag = "";

		wpcurrency.clear();

		try {
			LOGGER.debug(mySQL);
			con = Connector.getConnection(sessionId);

			st = con.prepareStatement(mySQL);
			ResultSet rset = st.executeQuery();

			while (rset.next()) {
				cd_currency = rset.getString("CD_CURRENCY");
				scd_currency = rset.getString("SCD_CURRENCY");
				name_currency = rset.getString("NAME_CURRENCY");
				sname_currency = rset.getString("SNAME_CURRENCY");
				exist_flag = rset.getString("IS_USED");
				wpcurrency.add(new bcCurrencyRecord(cd_currency, scd_currency,
						name_currency, sname_currency, exist_flag));
			}
		} catch (SQLException e) {
			LOGGER.error("getCurrencies() SQLException: " + e.toString());
		} catch (Exception el) {
			LOGGER.error("getCurrencies() Exception: " + el.toString());
		} finally {
			try {
				if (st != null)
					st.close();
			} catch (SQLException w) {
			}
			Connector.closeConnection(con);
		}
	}

	public ArrayList<bcWebPosMenu> menu = new ArrayList<bcWebPosMenu>();
	//int menuCount = 0;

	private String ajaxPageFirstMenu = "";
	
	public String getAjaxMenuFirstPage () {
		return ajaxPageFirstMenu;
	}
//	
	public void readWebPosMenuHTML() {

		Connection con = null;
		StringBuilder result = new StringBuilder();
		Statement st = null;
		//this.loginUser.getCurrentUserFeature(this.getLanguage(), this.getSessionId(), this.getDateFormat());
		//System.out.println("part 1.1");
		try {
			//System.out.println("part 1.1.1");
			//System.out.println("part 1.1.2");
			String sql = 
				" SELECT * " + 
				"   FROM " + getGeneralDBScheme() + ".VP$USER_MENU_ALL " +
				"  WHERE id_user = " + this.loginUser.getValue("ID_USER") + 
				"  ORDER BY order_number";
			//System.out.println("part 1.1.3");

			con = Connector.getConnection(sessionId);
			//System.out.println("part 1.1.4");
			st = con.createStatement();
			//System.out.println("part 1.1.5");

			ResultSet rs = st.executeQuery(sql);
			//System.out.println("part 1.1.6");
			
			//String menuLanguage = "";

			//System.out.println("part 1.2");
			menu.clear();
			ajaxPageFirstMenu = "";
			while (rs.next()) {
				//menuLanguage = rs.getString("LANGUAGE");
				if ("MENU".equalsIgnoreCase(rs.getString("TYPE_MENU_ELEMENT"))) {
					if (isEmpty(ajaxPageFirstMenu)) {
						ajaxPageFirstMenu = "ajaxpage('" + rs.getString("RELATIVE_PATH") + rs.getString("EXEC_FILE") +".jsp', 'div_main')";
						//System.out.println("ajaxPageFirstMenu="+ajaxPageFirstMenu);
					}
				}
					bcWebPosMenu oneMenu = new bcWebPosMenu();
					oneMenu.addMenu(
							rs.getString("ID_MENU_ELEMENT"), 
							rs.getString("NAME_MENU_ELEMENT"),
							rs.getString("TITLE_MENU_ELEMENT"), 
							rs.getString("TABNAME_MENU_ELEMENT"), 
							rs.getString("TYPE_MENU_ELEMENT"), 
							rs.getString("ID_MENU_ELEMENT_PARENT"), 
							rs.getString("RELATIVE_PATH"), 
							rs.getString("EXEC_FILE"), 
							rs.getString("ID_PRIVILEGE_TYPE"), 
							rs.getString("IMG_SRC"), 
							rs.getString("HAS_HELP"), 
							rs.getString("IS_ENABLE"), 
							rs.getString("IS_VISIBLE"));
					menu.add(oneMenu);
					//menuCount = menuCount + 1;
					//LOGGER.debug("bcWebPosBean.readWebPosMenuHTML(): menu="+rs.getString("TITLE_MENU_ELEMENT"));
					//System.out.println("menu="+rs.getString("ID_MENU_ELEMENT")+", size="+menu.size());
				/*} else if ("TABSHEET".equalsIgnoreCase(rs.getString("TYPE_MENU_ELEMENT"))) {
					for(int counter=0; counter<menu.size();counter++){
						if (rs.getString("ID_MENU_ELEMENT_PARENT").equalsIgnoreCase(menu.get(counter).getIdMenuElement())) {
								menu.get(menu.size()-1).addTabSheet(
									rs.getString("ID_MENU_ELEMENT"), 
									rs.getString("NAME_MENU_ELEMENT"),
									rs.getString("TITLE_MENU_ELEMENT"), 
									rs.getString("TABNAME_MENU_ELEMENT"), 
									rs.getString("ID_PRIVILEGE_TYPE"), 
									rs.getString("IMG_SRC"), 
									rs.getString("HAS_HELP"), 
									rs.getString("IS_ENABLE"), 
									rs.getString("IS_VISIBLE"));
								System.out.println("tabSheet="+rs.getString("ID_MENU_ELEMENT")+", parent="+rs.getString("ID_MENU_ELEMENT_PARENT"));
								break;
						}
					}
				} else if ("FUNCTION".equalsIgnoreCase(rs.getString("TYPE_MENU_ELEMENT"))) {
					for(int counter=0; counter<menu.size();counter++){
						if (rs.getString("ID_MENU_ELEMENT_PARENT").equalsIgnoreCase(menu.get(counter).getIdMenuElement())) {
							menu.get(menu.size()-1).addFunction(
									rs.getString("ID_MENU_ELEMENT"), 
									rs.getString("NAME_MENU_ELEMENT"),
									rs.getString("TITLE_MENU_ELEMENT"), 
									rs.getString("TABNAME_MENU_ELEMENT"), 
									rs.getString("ID_PRIVILEGE_TYPE"), 
									rs.getString("IMG_SRC"), 
									rs.getString("HAS_HELP"), 
									rs.getString("IS_ENABLE"), 
									rs.getString("IS_VISIBLE"));
							System.out.println("function="+rs.getString("ID_MENU_ELEMENT")+", parent="+rs.getString("ID_MENU_ELEMENT_PARENT"));
							break;
						}
					}
				}*/
			}
			//LOGGER.debug("bcWebPosBean.readWebPosMenuHTML(): total menuCount="+(menu.size()));
			//System.out.println("total menuCount="+(menu.size())+", language="+menuLanguage);
		} catch (SQLException e) {
			result.append("<br><b>bcWebPosBean.readWebPosMenuHTML() SQLException:<b><br>"
					+ e.toString());
			LOGGER.debug("bcWebPosBean.readWebPosMenuHTML() SQLException: " + e.toString());
		} catch (Exception e) {
			result.append("<br><b>bcWebPosBean.readWebPosMenuHTML() Exception: <b><br>"
					+ e.toString());
			LOGGER.debug("bcWebPosBean.readWebPosMenuHTML() Exception: " + e.toString());
		} finally {
			try {
				if (st != null)
					st.close();
			} catch (SQLException w) {
				w.toString();
			}
			try {
				if (con != null)
					con.close();
			} catch (SQLException w) {
				w.toString();
			}

			//System.out.println("part 1.4");
			Connector.closeConnection(con);
		} // finally
		//System.out.println("part 1.5");
	}
	
	/*public boolean hasMenuPermission(String pTabName) {
		boolean result = false;
		
		for(int counter=0; counter<menu.size();counter++){
			if (pTabName.equalsIgnoreCase(menu.get(counter).getTabNameMenuElement())) {
				if (!("0".equalsIgnoreCase(menu.get(counter).getIdPrivilegeType()))) {
					result = true;
				}
			}
		}
		return result;
	}*/
	
	public boolean hasMenuElementPermission(String pTabName, String pAccessLevel) {
		boolean result = false;
		
		for(int counter=0; counter<menu.size();counter++){
			if (menu.get(counter).getTabNameMenuElement().equalsIgnoreCase(pTabName)) {
				String idPrivilegeType = "" + menu.get(counter).getIdPrivilegeType();
				//System.out.println("idMenu="+menu.get(counter).getIdMenuElement()+", idPrivilegeType="+idPrivilegeType+", pAccessLevel="+pAccessLevel);
	        	if (isEmpty(idPrivilegeType)) {
	        		idPrivilegeType = "0";
	        	}
	        	if (pAccessLevel == null ) {
		        	if (!("0".equalsIgnoreCase(idPrivilegeType))) {
		        		result = true;
		        	}
	        	} else {
	        		if (C_READ_MENU_PERMISSION.equalsIgnoreCase(pAccessLevel)) {
		        		if (C_READ_MENU_PERMISSION.equalsIgnoreCase(idPrivilegeType) ||
		        				C_WRITE_MENU_PERMISSION.equalsIgnoreCase(idPrivilegeType)) {
			        		result = true;
			        	}
	        		} else {
		        		if (pAccessLevel.equalsIgnoreCase(idPrivilegeType)) {
			        		result = true;
			        	}
	        		}
	        	}
	        		
	        	if ("N".equalsIgnoreCase(menu.get(counter).getIsVisible())) {
	        		result = false;
	        	}
	        	if ("N".equalsIgnoreCase(menu.get(counter).getIsEnable())) {
	        		result = false;
	        	}
			}
		}
		return result;
	}
	
	public boolean hasReadMenuPermission(String pTabName) {
		return hasMenuElementPermission(pTabName, C_READ_MENU_PERMISSION);
	}
	
	public boolean hasWriteMenuPermission(String pTabName) {
		return hasMenuElementPermission(pTabName, C_WRITE_MENU_PERMISSION);
	}
	
	public boolean hasExecuteMenuPermission(String pTabName) {
		return hasMenuElementPermission(pTabName, C_EXECUTE_MENU_PERMISSION);
	}
	
	public boolean isMenuElementEnable(String pTabName) {
		boolean result = false;
		
		for(int counter=0; counter<menu.size();counter++){
			if (menu.get(counter).getTabNameMenuElement().equalsIgnoreCase(pTabName)) {
        		if ("Y".equalsIgnoreCase(menu.get(counter).getIsEnable())) {
					result = true;
				}
			}
		}
		return result;
	}
	
	public boolean isMenuElementVisible(String pTabName) {
		boolean result = false;
		
		for(int counter=0; counter<menu.size();counter++){
			if (menu.get(counter).getTabNameMenuElement().equalsIgnoreCase(pTabName)) {
        		if ("Y".equalsIgnoreCase(menu.get(counter).getIsVisible())) {
					result = true;
				}
			}
		}
		return result;
	}
	
	public boolean hasStornoMenuPermission () {
		boolean returnValue = false;
		if (hasExecuteMenuPermission("WEBPOS_SERVICE_STORNO_CANCELLATION") ||
				hasExecuteMenuPermission("WEBPOS_SERVICE_STORNO_RETURN")) {
			returnValue = true;
		}
		return returnValue;
	}
	
	/*public boolean hasFunctionPermission(String pTabName, String pFunctionName) {
		boolean result = false;
		
		for(int counter=0; counter<menu.size();counter++){
			if (pTabName.equalsIgnoreCase(menu.get(counter).getTabNameMenuElement())) {
				if (menu.get(counter).hasFunctionPermission(pFunctionName)) {
					result = true;
				}
			}
		}
		return result;
	}
	
	public boolean isFunctionEnable(String pTabName, String pFunctionName) {
		boolean result = false;
		
		for(int counter=0; counter<menu.size();counter++){
			if (pTabName.equalsIgnoreCase(menu.get(counter).getTabNameMenuElement())) {
				result = menu.get(counter).isFunctionEnable(pFunctionName);
			}
		}
		return result;
	}
	
	public boolean isFunctionVisible(String pTabName, String pFunctionName) {
		boolean result = false;
		
		for(int counter=0; counter<menu.size();counter++){
			if (pTabName.equalsIgnoreCase(menu.get(counter).getTabNameMenuElement())) {
				result = menu.get(counter).isFunctionVisible(pFunctionName);
			}
		}
		return result;
	}*/
	
	public String getWebPosTitleMenuHTML() {

		StringBuilder result = new StringBuilder();

		//System.out.println("part 1");
        this.readWebPosMenuHTML();
        //System.out.println("part 2");
        
		String lOneMenu = "";
		int colCount = 0;
		String lUnReadMessageCount = "0";
		result.append("<tr>");
		for(int counter=0; counter<menu.size();counter++){
			lOneMenu = "";
			//System.out.println("id(title)="+menu.get(counter).getIdMenuElement()+", type="+menu.get(counter).getTypeMenuElement()+", parent="+menu.get(counter).getIdMenuElementParent());
			
			if ("MENU".equalsIgnoreCase(menu.get(counter).getTypeMenuElement()) &&
					isEmpty(menu.get(counter).getIdMenuElementParent()) &&
					!("REPORT_INVOICE".equalsIgnoreCase(menu.get(counter).getTabNameMenuElement()))) {
				colCount = colCount + 1;
				if ("Y".equalsIgnoreCase(menu.get(counter).getIsVisible())) {
					boolean isWritePermission = false;
					if ("Y".equalsIgnoreCase(menu.get(counter).getIsEnable()) &&
							"2".equalsIgnoreCase(menu.get(counter).getIdPrivilegeType())) {
						isWritePermission = true;
					}
					if (isWritePermission) {
						lUnReadMessageCount = "0";
						if ("webpos_admin_help".equalsIgnoreCase(menu.get(counter).getTabNameMenuElement())) {
							if (hasReadMenuPermission("WEBPOS_ADMIN_HELP_MESSAGES")) {
								lUnReadMessageCount = loginUser.getUserMessageNotReadCount(loginUser.getValue("ID_USER"));
							}
						}
						lOneMenu = "<td>";
						lOneMenu = lOneMenu + "<span class=\"rectangle_big\"><span class=\"" + menu.get(counter).getExecFile() + "_big";
			
						lOneMenu = lOneMenu + "\" onclick=\"ajaxpage('" + menu.get(counter).getRelativePath() + menu.get(counter).getExecFile() +".jsp', 'div_main')\" title=\"" + menu.get(counter).getTitleMenuElement() + "\"></span>";
						lOneMenu = lOneMenu + "<span class=\"caption_big\" onclick=\"ajaxpage('" + menu.get(counter).getRelativePath() + menu.get(counter).getExecFile() +".jsp', 'div_main')\" title=\"" + menu.get(counter).getTitleMenuElement() + "\">" + menu.get(counter).getNameMenuElement() + " ";
						lOneMenu = lOneMenu + (!"0".equalsIgnoreCase(lUnReadMessageCount)?"&nbsp;(<span class=\"blinker\" onclick=\"ajaxpage('" + menu.get(counter).getRelativePath() + menu.get(counter).getExecFile() +".jsp', 'div_main')\" title=\""+webposXML.getfieldTransl("title_unread_messages_count", false)+"\">"+lUnReadMessageCount+"</span>)":"");
						lOneMenu = lOneMenu + "</span></span>";
						lOneMenu = lOneMenu + "</td>";
					} else {
						lOneMenu = "<td>";
						lOneMenu = lOneMenu + "<span class=\"disable rectangle_big\"><span class=\"disable " + menu.get(counter).getExecFile() + "_big\" title=\"" + menu.get(counter).getTitleMenuElement() + ": " + commonXML.getfieldTransl("access_denied", false).toLowerCase() + "\"></span>";
						lOneMenu = lOneMenu + "<span class=\"disable caption_big\">" + menu.get(counter).getNameMenuElement() + "</span></span>";
						lOneMenu = lOneMenu + "</td>";
					}
				}
				result.append(lOneMenu);
				
				if (colCount>= 3) {
					result.append("</tr><tr>");
					colCount = 0;
				}
			}
		}
		//System.out.println("part 3");
		result.append("</tr>");
		//System.out.println(result.toString());
		return result.toString();
	}
	
	/*public String getWebPosMenuHTML(String pCurrentTabName) {
		return "";
	}*/
	
	public String getWebPosMenuHTML(String pCurrentTabName) {

		StringBuilder result = new StringBuilder();

        this.readWebPosMenuHTML();
        
		String lOneMenu = "";
		result.append("<div id=\"div_status\"><div id=\"div_status_top\"><span class=\"img_logo\" title=\"" + getTerminalTitle() + "\">&nbsp;</span>" + getTitleUserParamShort(25) + "</div></div>");
		//result.append("<div id=\"div_title\">");
		//result.append("<div id=\"div_status_bar\">");
		//result.append("<a href=\"main.jsp\"><span title=\"" + commonXML.getfieldTransl(this.getLanguage(), "title_home", false) + "\" class=\"small_button_title menu_home rectangle rectangle_title_small\"></span></a>");
		//result.append("<span class=\"rectangle_title\"> <span class=\"menu_title\">");
		//result.append("<span class=\"text_title\">");
		//result.append(this.getTitleUserParamShort() + "</span></span></span>");
		//result.append("<a href=\"main.jsp?do=exit\"><span title=\"" + commonXML.getfieldTransl(this.getLanguage(), "title_exit", false) + "\" class=\"small_button_title menu_exit rectangle rectangle_title_small\"></span></a>");
		result.append("<div id=\"imgWait\" class=\"imgWaitDialog\"><img id=\"imageImgWait\" src=\"" + getThemePath() + "/images/ajax-loader-circle.gif\" align=\"middle\"></div>");
		//result.append("</div>");
		//result.append("</div>");

		result.append("<div id=\"div_logo\">");
		result.append("<div id=\"img_menu_small\">");
		result.append("<span class=\"general_detail\">");
		for(int counter=0; counter<menu.size();counter++){
			lOneMenu = "";
			//System.out.println("id="+menu.get(counter).getIdMenuElement()+", type="+menu.get(counter).getTypeMenuElement()+", parent="+menu.get(counter).getIdMenuElementParent());
			if ("MENU".equalsIgnoreCase(menu.get(counter).getTypeMenuElement()) &&
					isEmpty(menu.get(counter).getIdMenuElementParent()) &&
					!("REPORT_INVOICE".equalsIgnoreCase(menu.get(counter).getTabNameMenuElement()))) {
				if ("Y".equalsIgnoreCase(menu.get(counter).getIsVisible())) {
					boolean isWritePermission = false;
					if ("Y".equalsIgnoreCase(menu.get(counter).getIsEnable()) &&
							"2".equalsIgnoreCase(menu.get(counter).getIdPrivilegeType())) {
						isWritePermission = true;
					}
					if (isWritePermission) {
						lOneMenu = "<span class=\"small_button " + menu.get(counter).getExecFile();
						if (pCurrentTabName.equalsIgnoreCase(menu.get(counter).getTabNameMenuElement())) {
							lOneMenu = lOneMenu + "_check";
						}
						lOneMenu = lOneMenu + " rectangle\" onclick=\"ajaxpage('" + menu.get(counter).getRelativePath() + menu.get(counter).getExecFile() +".jsp', 'div_main')\" title=\"" + menu.get(counter).getTitleMenuElement() + "\"></span>";
					} else {
						lOneMenu = "<span class=\"disable small_button " + menu.get(counter).getExecFile() + " rectangle\" title=\"" + menu.get(counter).getTitleMenuElement() + ": " + commonXML.getfieldTransl("access_denied", false).toLowerCase() + "\"></span>";
					}
				}
				result.append(lOneMenu);
			}
		}
		result.append("</span>");
		result.append("<span class=\"general_menu\">");
		result.append("<span class=\"empty\"></span>");
		result.append("<a href=\"main.jsp\"><span title=\"" + commonXML.getfieldTransl2(getLanguage(),"title_home") + "\" class=\"small_button home rectangle\"></span></a>");
		result.append("<a href=\"main.jsp?do=exit\"><span title=\"" + commonXML.getfieldTransl2(getLanguage(),"title_exit") + "\" class=\"small_button exit rectangle\"></span></a>");
		result.append("</span>");
		result.append("</div>");
		result.append("</div>");
		//System.out.println(result.toString());
		return result.toString();
	}


	public String getWPCurrencyName(String pCurrencyCode) {
		String return_value = "";
		if (isEmpty(pCurrencyCode)) {
			return return_value;
		}
		for (int i = 0; i <= wpcurrency.size() - 1; i++) {
			if (pCurrencyCode.equalsIgnoreCase(wpcurrency.get(i).getCode())) {
				return_value = wpcurrency.get(i).getName();
			}
		}
		if ("".equalsIgnoreCase(return_value)) {
			return_value = pCurrencyCode + " (unkn.)";
		}
		return return_value;
	}

	public String getWPCurrencyShortName(String pCurrencyCode) {
		String return_value = "";
		if (isEmpty(pCurrencyCode)) {
			return return_value;
		}
		for (int i = 0; i <= wpcurrency.size() - 1; i++) {
			if (pCurrencyCode.equalsIgnoreCase(wpcurrency.get(i).getCode())) {
				return_value = wpcurrency.get(i).getShortName();
			}
		}
		if ("".equalsIgnoreCase(return_value)) {
			return_value = pCurrencyCode + " (unkn.)";
		}
		return return_value;
	}

	public String getWPCurrencyStringCode(String pCurrencyCode) {
		String return_value = "";
		if (isEmpty(pCurrencyCode)) {
			return return_value;
		}
		for (int i = 0; i <= wpcurrency.size() - 1; i++) {
			if (pCurrencyCode.equalsIgnoreCase(wpcurrency.get(i).getCode())) {
				return_value = wpcurrency.get(i).getStringCode();
			}
		}
		if ("".equalsIgnoreCase(return_value)) {
			return_value = pCurrencyCode + " (unkn.)";
		}
		return return_value;
	}

	public String getWPCurrencyOption(String selected_value, String pNameType,
			String pSize, boolean isNull, boolean existsOnly) {
		StringBuilder return_value = new StringBuilder();
		String selectedTxt = "";
		String styleText = "";
		boolean optionExist = true;
		this.selectOprionCount = 0;
		try {
			if (isNull) {
				return_value.append("<option value=\"\" title=\"\"></option>");
			}
			for (int i = 0; i < wpcurrency.size(); i++) {
				String code = wpcurrency.get(i).getCode();
				String value = "";
				if ("SNAME".equalsIgnoreCase(pNameType)) {
					value = wpcurrency.get(i).getShortName();
				} else {
					value = wpcurrency.get(i).getName();
				}
				String existFlag = wpcurrency.get(i).getExistFlag();
				String optionStyle = wpcurrency.get(i).getOptionStyle();
				selectedTxt = "";

				if (selected_value != null) {
					if (selected_value.equalsIgnoreCase(code)
							|| selected_value.equalsIgnoreCase(value)) {
						selectedTxt = "SELECTED";
					}
				}
				String pOptionValue = code;
				String pOptionText = "";
				String pOptionTitle = "";
				styleText = "";
				optionExist = true;
				if ("0".equalsIgnoreCase(existFlag)) {
					styleText = " style=\"font-weight:bold;color: #FF0000;\"";
					optionExist = false;
				}
				if (!isEmpty(optionStyle)) {
					styleText = " " + optionStyle;
				}
				if (isEmpty(pSize)) {
					pOptionText = value;
					pOptionTitle = "";
				} else {
					if (!isEmpty(value)) {
						int lTextSize = Integer.parseInt(pSize);
						if (value.length() > lTextSize) {
							pOptionText = value.substring(0, lTextSize) + "...";
							pOptionTitle = value;
						} else {
							pOptionText = value;
							pOptionTitle = "";
						}
					} else {
						pOptionText = "";
						pOptionTitle = "";
					}
				}
				if (!optionExist) {
					pOptionText = pOptionText
							+ " ("
							+ this.commonXML.getfieldTransl("h_exist_flag_n", false) + ")";
				}
				if (!existsOnly
						|| (existsOnly && "1".equalsIgnoreCase(existFlag))) {
					return_value.append("<option value=\"" + pOptionValue
							+ "\" " + selectedTxt + styleText + " title=\""
							+ pOptionTitle + "\">" + pOptionText + "</option>");
				}
				this.selectOprionCount = this.selectOprionCount + 1;
			}
		} catch (Exception el) {
			LOGGER.debug("getSelectBodyFromQuery Exception: "
					+ el.toString());
		}
		return return_value.toString();
	}

	public String getWPCurrencyNameOption(String selected_value, boolean isNull) {
		return getWPCurrencyOption(selected_value, "NAME", "", isNull, true);
	}

	public String getWPCurrencyShortNameOption(String selected_value,
			boolean isNull) {
		return getWPCurrencyOption(selected_value, "SNAME", "", isNull, true);
	}

	public String getLogOutScript(HttpServletRequest request) {
		StringBuilder result = new StringBuilder();
		String myHyperLink = "";
		if (!this.logInCurrent(request.getSession().getId())) {
			result.append("<script type=\"text/javascript\">");
			result.append("ajaxpage('" + this.getCheckUserLineFull(request)
					+ "', 'div_document')");
			result.append("</script>");
		}
		myHyperLink = request.getRequestURI() + "?";
		if (!(isEmpty(request.getQueryString()) || "null"
					.equalsIgnoreCase(request.getQueryString()))) {
			myHyperLink = myHyperLink + request.getQueryString().toString();
		}
		this.addLink(myHyperLink);
		LOGGER.debug("getLogOutScript='" + result.toString() + "'");
		return result.toString();
	}

	/*public String getWEBPosUserTerminalsRadio(String pFieldName,
			String id_user, String id_terminal) {
		ArrayList<bcFeautureParam> pParam = initParamArray();
		pParam.add(new bcFeautureParam("int", id_user));

		ArrayList<bcDictionaryRecord> lTerm = new ArrayList<bcDictionaryRecord>();

		return getRadioButtonsFromParamQuery(pFieldName,
				" SELECT id_term, id_term name_term " + "   FROM "
						+ getGeneralDBScheme() + ".v_user_term_all"
						+ "  WHERE id_user = ? "
						+ "    AND cd_term_type = 'WEBPOS' "
						+ "    AND exist_flag = 'Y'"
						+ "  ORDER BY id_term", pParam, id_terminal,
				messageXML, lTerm, false);
	}*/

	/*public String getWPUserCurrentTerminal(String id_user) {
		return getOneValueByStringId2(
				"SELECT id_term FROM "
						+ getGeneralDBScheme()
						+ ".v_user_term_all WHERE ID_USER = ? AND cd_term_type = 'WEBPOS' AND ROWNUM < 2",
				id_user);
	}*/


	public String getWEBPosTermOnlineOperOptions(String id_term,
			String id_operation, boolean isNull) {

		ArrayList<bcFeautureParam> pParam = initParamArray();
		pParam.add(new bcFeautureParam("int", id_term));

		return getSelectBodyFromParamQuery(
				" SELECT id_club_online_pay_type, term_card_req_club_pay_id||' - '||name_club_online_pay_type, exist_term_online_pay_type exist_flag "
						+ "   FROM "
						+ getGeneralDBScheme()
						+ ".vc_term_online_pay_type_cl_all"
						+ "  WHERE id_term = ? "
						+ "  ORDER BY term_card_req_club_pay_id", pParam,
				id_operation, null, isNull);
	}

	public String getWEBPosTermReplanishTypeOptions(String id_term,
			String id_type, boolean isNull) {
		ArrayList<bcFeautureParam> pParam = initParamArray();
		pParam.add(new bcFeautureParam("int", id_term));

		return getSelectBodyFromParamQuery(
				" SELECT cd_replanish_type, name_replanish_type " + "   FROM "
						+ getGeneralDBScheme() + ".vc_term_replanish_type_all"
						+ "  WHERE id_term = ? " 
						+ "  ORDER BY name_replanish_type", pParam, id_type,
				null, isNull);
	}

	public String getWEBPosCardPackagesCostScript(String pIdJurPrs, String pIdClub, String pIdCardStatus) {
		
		StringBuilder html = new StringBuilder();
		
		PreparedStatement st = null;
		Connection con = null;

		try {
			String mySQL = " SELECT id_jur_prs_card_pack, total_amount_jp_card_pack_frmt " 
				+ "   FROM " + getGeneralDBScheme() + ".vc_club_jp_card_pack_all "
				+ "  WHERE id_jur_prs = ? "
				+ "    AND id_club = ?"
				+ "    AND id_card_status = ?"
				+ "  ORDER BY total_amount_jp_card_pack";
			con = Connector.getConnection(this.sessionId);
			st = con.prepareStatement(mySQL,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
			st.setString(1, pIdJurPrs);
			st.setString(2, pIdClub);
			st.setString(3, pIdCardStatus);
			LOGGER.debug("bcWebPosBean.getWEBPosCardPackagesCostScript(): "+mySQL);
			ResultSet rset = st.executeQuery();

			//html.append("<input type=\"hidden\" name=\"pay_value\" id=\"pay_value\" value=\"\">");
			//html.append("<script type=\"text/javascript\">");
			html.append("	var cardPackageParam = new Array (");
			
			while (rset.next()) {
				if (rset.isLast()) {
					html.append("	new Array ('"+rset.getString("ID_JUR_PRS_CARD_PACK")+"', '"+rset.getString("TOTAL_AMOUNT_JP_CARD_PACK_FRMT")+"')");					
				} else {
					html.append("	new Array ('"+rset.getString("ID_JUR_PRS_CARD_PACK")+"', '"+rset.getString("TOTAL_AMOUNT_JP_CARD_PACK_FRMT")+"'),");					
				}
			}
			html.append("	);");
			//html.append("</script>");
		} catch (SQLException e) {
			LOGGER.error("getCurrencies() SQLException: " + e.toString());
		} catch (Exception el) {
			LOGGER.error("getCurrencies() Exception: " + el.toString());
		} finally {
			try {
				if (st != null)
					st.close();
			} catch (SQLException w) {
			}
			Connector.closeConnection(con);
		}
		return html.toString();
	}

	public String getWEBPosCardPackagesOptions(String pIdPackage, String pIdJurPrs, String pIdClub, String pIdCardStatus, boolean isNull) {
		ArrayList<bcFeautureParam> pParam = initParamArray();
		pParam.add(new bcFeautureParam("int", pIdJurPrs));
		pParam.add(new bcFeautureParam("int", pIdClub));
		pParam.add(new bcFeautureParam("int", pIdCardStatus));

		return getSelectBodyFromParamQuery(
				" SELECT id_jur_prs_card_pack, name_jur_prs_card_pack " 
						+ "   FROM " + getGeneralDBScheme() + ".vc_club_jp_card_pack_all "
						+ "  WHERE id_jur_prs = ? "
						+ "    AND id_club = ?"
						+ "    AND id_card_status = ?"
						+ "  ORDER BY total_amount_jp_card_pack", pParam, pIdPackage,
				"", isNull);
	}
	
	public String getWEBPosCardPackagesName(String id_pack) {
		return getOneValueByIntId2("SELECT name_jur_prs_card_pack FROM "
				+ getGeneralDBScheme()
				+ ".vc_club_jp_card_pack_all WHERE id_jur_prs_card_pack = ?", id_pack);
	}
	
	public String getWEBPosCardPackagesTotalAmount(String id_pack) {
		return getOneValueByIntId2("SELECT total_amount_jp_card_pack_frmt FROM "
				+ getGeneralDBScheme()
				+ ".vc_club_jp_card_pack_all WHERE id_jur_prs_card_pack = ?", id_pack);
	}

	
	public String getUnknownActionText(String pAction) {

		StringBuilder html = new StringBuilder();
		html.append("<table " + this.getTableMenuParam() + ">");
		html.append("<tr><td colspan=\"10\" align=\"center\">");
		html.append(this.form_messageXML.getfieldTransl("unknown_action", false) + " (action = " + pAction + ") <br>");
		html.append("</td></tr></table>");
		return html.toString();
	}

	public String getUnknownProcessText(String pProcess) {

		StringBuilder html = new StringBuilder();
		html.append("<table " + this.getTableMenuParam() + ">");
		html.append("<tr><td colspan=\"10\" align=\"center\">");
		html.append(this.form_messageXML.getfieldTransl("unknown_process", false)
				+ " (process = "
				+ pProcess
				+ ") <br>");
		html.append("</td></tr></table>");
		return html.toString();
	}

	public String getUnknownTypeText(String pType) {

		StringBuilder html = new StringBuilder();
		html.append("<table " + this.getTableMenuParam() + ">");
		html.append("<tr><td colspan=\"10\" align=\"center\">");
		html.append(this.form_messageXML.getfieldTransl("unktown_type", false) + " (type = " + pType + ") <br>");
		html.append("</td></tr></table>");
		return html.toString();
	}

	public String getCardActivationTabSheets(String pTab) {

		StringBuilder html = new StringBuilder();
		html.append("<table " + this.getTableDetail2Param() + ">\n");
		html.append("<tr>\n");
		html.append("<td>\n");
		html.append("<div id=\"slidetabsmenu\">\n");
		html.append("<ul>\n");
		if (this.hasWriteMenuPermission("WEBPOS_SERVICE_CARD_ACTIVATION_REGISTRATION")) {
			html.append("<li ");
			if ("1".equalsIgnoreCase(pTab)) {
				html.append(" class=\"current\" ");
			}
			html.append("><a href=\"#\"><span onclick=\"ajaxpage('action/new_client.jsp?tab=1', 'div_main')\">" + webposXML.getfieldTransl("title_card_registration", false) + "</span></a></li>\n");
		}
		if (this.hasReadMenuPermission("WEBPOS_SERVICE_CARD_ACTIVATION_QUESTIONNAIRE")) {
			html.append("<li ");
			if ("2".equalsIgnoreCase(pTab)) {
				html.append(" class=\"current\" ");
			}
			html.append("><a href=\"#\"><span onclick=\"ajaxpage('action/new_client.jsp?tab=2', 'div_main')\">" + webposXML.getfieldTransl("title_questionnaire", false) + "</span></a></li>\n");
		}
		if (this.hasWriteMenuPermission("WEBPOS_SERVICE_CARD_ACTIVATION_ACTIVATION")) {
			html.append("<li ");
			if ("3".equalsIgnoreCase(pTab)) {
				html.append(" class=\"current\" ");
			}
			html.append("><a href=\"#\"><span onclick=\"ajaxpage('action/new_client.jsp?tab=3', 'div_main')\">" + webposXML.getfieldTransl("title_activation", false) + "</span></a></li>\n");
		}
		html.append("</ul>\n");
		html.append("</div>\n");
		html.append("<hr>\n");
		html.append("</td>\n");
		html.append("</tr>\n");
		html.append("</table><br>\n");
		//html.append("<br>\n");
		return html.toString();
	}


	public String getWEBPosOnlyTestCards2() {
		StringBuilder html = new StringBuilder();
		if ("EN".equalsIgnoreCase(this.getLanguage())) {
			html.append("<font style=\"font-style: italic;font-size:11px; color:gray\"><br>Only for testing!<br>Possible card number:<br>9900000000028<br>9900000000127<br>9900000000219</font>\n");
		} else if ("UA".equalsIgnoreCase(this.getLanguage())) {
			html.append("<font style=\"font-style: italic;font-size:11px; color:gray\"><br>Тільки для тестування!<br>Можливі номери карт:<br>9900000000028<br>9900000000127<br>9900000000219</font>\n");
		} else {
			html.append("<font style=\"font-style: italic;font-size:11px; color:gray\"><br>Только для тестирования!<br>Возможные номера карт:<br>9900000000028<br>9900000000127<br>9900000000219</font>\n");
		}
		return html.toString();

	}

	/*public String getWEBPosOnlyTestLogin() {
		StringBuilder html = new StringBuilder();

		if ("EN".equalsIgnoreCase(this.getLanguage())) {
			html.append("<br><font style=\"font-style: italic;font-size:11px; color:gray\"><br>Only for testing!<br>Possible logins:<br>9900990010038\n");
		} else if ("UA".equalsIgnoreCase(this.getLanguage())) {
			html.append("<br><font style=\"font-style: italic;font-size:11px; color:gray\"><br>Тільки для тестування!<br>Можливі логіни:<br>9900990010038\n");
		} else {
			html.append("<br><font style=\"font-style: italic;font-size:11px; color:gray\"><br>Только для тестирования!<br>Возможные логины:<br>9900990010038\n");
		}
		return html.toString();

	}*/
	
	public boolean canTestData() {
		boolean returnValue = true;
		String termCanTestMode = loginTerm.getValue("CAN_TEST_MODE");
		if (isEmpty(termCanTestMode) || "N".equalsIgnoreCase(termCanTestMode)) {
			returnValue = false;
		}
		
		String needTestData = loginUser.getValue("IS_TEST_MODE");
		if (isEmpty(needTestData) || "N".equalsIgnoreCase(needTestData)) {
			returnValue = false;
		}
		return returnValue;
	}

	public String getWEBPosOnlyTestCards(boolean needPoint) {
		StringBuilder html = new StringBuilder();
		PreparedStatement st = null;
		Connection con = null;
		
		if (!canTestData()) {
			return "";
		}
		
		ArrayList<bcFeautureParam> pParam = initParamArray();
		pParam.add(new bcFeautureParam("none", ""));
		
		String mySQL = 
			"SELECT cd_card1, form_text, activation_code " +
			"  FROM " + getGeneralDBScheme() + ".vp$test_card" +
			" ORDER BY order_number";

		if ("EN".equalsIgnoreCase(this.getLanguage())) {
			html.append("<font style=\"font-style: italic;font-size:11px; color:gray\"><br>Only for testing!<br>Possible card number:\n");
		} else if ("UA".equalsIgnoreCase(this.getLanguage())) {
			html.append("<font style=\"font-style: italic;font-size:11px; color:gray\"><br>Тільки для тестування!<br>Можливі номери карт:\n");
		} else {
			html.append("<font style=\"font-style: italic;font-size:11px; color:gray\"><br>Только для тестирования!<br>Возможные номера карт:\n");
		}
		try {
			LOGGER.debug(prepareSQLToLog(mySQL, pParam));
			con = Connector.getConnection(this.sessionId);

			st = con.prepareStatement(mySQL);
			st = prepareParam(st, pParam);
			ResultSet rset = st.executeQuery();

			while (rset.next()) {
				if (needPoint) {
					html.append("<br><span class=\"test_value\" onclick=\"document.getElementById('cd_card1').value = '" + rset.getString("CD_CARD1") + "';card_mask2('cd_card1');\" title=\"" + rset.getString("CD_CARD1") + "\">" + rset.getString("CD_CARD1") + "</span> (акт.код " + rset.getString("ACTIVATION_CODE") + ") - " + rset.getString("FORM_TEXT"));
				} else {
					html.append("<br>" + rset.getString("CD_CARD1") + " (акт.код " + rset.getString("ACTIVATION_CODE") + ") - " + rset.getString("FORM_TEXT"));
				}
			}
			html.append("</font>");
		} catch (SQLException e) {
			LOGGER.debug("bcBase.getSelectBodyFromQuery SQLException: "
					+ e.toString());
			html.append("<option value=\"\">SQL Error</option>");
		} catch (Exception el) {
			LOGGER.debug("bcBase.getSelectBodyFromQuery Exception: "
					+ el.toString());
		} finally {
			try {
				if (st != null)
					st.close();
			} catch (SQLException w) {
				w.toString();
			}
			try {
				if (con != null)
					con.close();
			} catch (SQLException w) {
				w.toString();
			}
			Connector.closeConnection(con);
		}
		return html.toString();
	}
	
	public String getWEBPosOnlyTestCards() {
		return getWEBPosOnlyTestCards(true);
	}

	public String getWEBPosOnlyTestCoupon() {
		StringBuilder html = new StringBuilder();
		PreparedStatement st = null;
		Connection con = null;
		
		if (!canTestData()) {
			return "";
		}
		
		ArrayList<bcFeautureParam> pParam = initParamArray();
		pParam.add(new bcFeautureParam("none", ""));
		
		String mySQL = 
			"SELECT cd_club_event_coupon, coupon_control_code, form_text" +
			"  FROM (SELECT cd_club_event_coupon, coupon_control_code, form_text " +
			"          FROM " + getGeneralDBScheme() + ".vp$test_coupon" +
			"         ORDER BY cd_club_event_coupon_state)" +
			" WHERE ROWNUM < 6";

		if ("EN".equalsIgnoreCase(this.getLanguage())) {
			html.append("<font style=\"font-style: italic;font-size:11px; color:gray\"><br>Only for testing!<br>Possible sertificate number:\n");
		} else if ("UA".equalsIgnoreCase(this.getLanguage())) {
			html.append("<font style=\"font-style: italic;font-size:11px; color:gray\"><br>Тільки для тестування!<br>Можливі номери сертифікатів:\n");
		} else {
			html.append("<font style=\"font-style: italic;font-size:11px; color:gray\"><br>Только для тестирования!<br>Возможные номера сертификатов:\n");
		}
		try {
			LOGGER.debug(prepareSQLToLog(mySQL, pParam));
			con = Connector.getConnection(this.sessionId);

			st = con.prepareStatement(mySQL);
			st = prepareParam(st, pParam);
			ResultSet rset = st.executeQuery();

			while (rset.next()) {
				html.append("<br><span class=\"test_value\" onclick=\"document.getElementById('coupon').value='"+rset.getString("CD_CLUB_EVENT_COUPON") + "';\" title=\"" + rset.getString("CD_CLUB_EVENT_COUPON") + "\">" + rset.getString("CD_CLUB_EVENT_COUPON") + "</span>&nbsp(контр.код  " + rset.getString("COUPON_CONTROL_CODE") + ") - " + rset.getString("FORM_TEXT"));
			}
			html.append("</font>");
		} catch (SQLException e) {
			LOGGER.debug("bcBase.getSelectBodyFromQuery SQLException: "
					+ e.toString());
			html.append("<option value=\"\">SQL Error</option>");
		} catch (Exception el) {
			LOGGER.debug("bcBase.getSelectBodyFromQuery Exception: "
					+ el.toString());
		} finally {
			try {
				if (st != null)
					st.close();
			} catch (SQLException w) {
				w.toString();
			}
			try {
				if (con != null)
					con.close();
			} catch (SQLException w) {
				w.toString();
			}
			Connector.closeConnection(con);
		}
		return html.toString();

	}

	public String getWEBPosCheckCardButton(String pElementName, String pBackType, String pFormName, String pDivName) {
		StringBuilder html = new StringBuilder();
		StringBuilder script = new StringBuilder();
		script.append("JavaScript: ");
		script.append("var card = document.getElementById('cd_card1'); ");
		script.append("var formCheckCard = new Array (new Array ('cd_card1', 'card', 1)); ");
		script.append("if (card.value == null || card.value == '') { return false;} ");
		script.append(" else {if (!validateFormForID(formCheckCard, '"+pFormName+"')) {return false;} else { ajaxpage('service/check_card.jsp?back_type=" + pBackType + "&' +  mySubmitForm('" + pFormName + "'),'" + pDivName + "'); return true; } }");
		
		 
		html.append("<span class=\"img_check_card_inactive\" id=\"" + pElementName + "\" name=\"" + pElementName + "\" title=\"" + webposXML.getfieldTransl2(getLanguage(), "title_check_card") + "\" onclick=\"" + script.toString() + "\">&nbsp;</span>");
		return html.toString();

	}

	public String getWEBPosCalcPointsButton(String pElementName, String pFormName, String pDivName) {
		StringBuilder html = new StringBuilder();
		html.append("<span class=\"img_calc_points\" id=\"" + pElementName + "\" name=\"" + pElementName + "\" title=\"" + webposXML.getfieldTransl2(getLanguage(), "title_calc_point_sum") + "\" onclick=\"showPoints();\">&nbsp;</span>");
		return html.toString();

	}

	public String getAdminUserTermOptions(String name_user, boolean isNull) {
		return getSelectBodyFromQuery2(
				" SELECT id_term, id_term name_term, 'Y' exist_flag "
						+ "   FROM " + getGeneralDBScheme()	+ ".v_user_term_all"
						+ "  WHERE cd_user_status = 'OPENED' "
						+ "    AND name_user = '" + name_user + "'"
						+ "  ORDER BY id_term ",
						this.getCurrentTerm(), isNull, false);
	}

	public String getWebposTransactionTypeOptions(String id_type, boolean isNull) {
		return getSelectBodyFromQuery(
				" SELECT cd_trans_type, name_trans_type," +
				"        DECODE(fcd_trans_type, " +
      	  		" 	 	     'REC_PAYMENT', ' style=\"font-weight:bold;color: black;\" ', " +
      	  		"          	 'REC_ACTIVATION', ' style=\"font-weight:bold;color: blue;\" ', " +
      	  		"          	 'REC_PUT_CARD', ' style=\"font-weight:bold;color: #191970;\" ', " +
      	  		"          	 'REC_COUPON', ' style=\"font-weight:bold;color: #8B8989;\" ', " +
      	  		"          	 'REC_MEMBERSHIP_FEE', ' style=\"font-weight:bold;color: #8B3A3A;\" ', " +
      	  		"          	 'REC_POINT_FEE', ' style=\"font-weight:bold;color: #C8860B;\" ', " +
      	  		"          	 'REC_MTF', ' style=\"font-weight:bold;color: #B8860B;\" ', " +
  	  			"          	 'REC_SHARE_FEE', ' style=\"font-weight:bold;color: green;\" ', " +
      	  		"          	 'REC_TRANSFER_GET_POINT', ' style=\"font-weight:bold;color: #BA55D3;\" ', " +
      	  		"          	 'REC_TRANSFER_PUT_POINT', ' style=\"font-weight:bold;color: #BA55D3;\" ', " +
      	  		//"          	 'REC_CANCEL', ' style=\"font-weight:bold;color: red;\" ', " +
      	  		//"          	 'REC_RETURN', ' style=\"font-weight:bold;color: red;\" ', " +
      	  		"          	 '' " +
      	  		"  	     ) option_style " + 
				"   FROM " + getGeneralDBScheme() + ".vc_trans_type_all" +
				"  WHERE is_online_type = 'Y' " + 
				"  ORDER BY name_trans_type", 
				id_type, isNull, webposTransType, false);
	}

	public String getWebposTransactionTypeShortOptions(String id_type, boolean isNull) {
		return getSelectBodyFromQuery(
				" SELECT cd_trans_type, sname_trans_type " + 
				"   FROM " + getGeneralDBScheme() + ".vc_trans_type_all" +
				"  WHERE is_online_type = 'Y' " + 
				"  ORDER BY sname_trans_type", 
				id_type, isNull, webposTransTypeShort, false);
	}
	
	private String getWebposTransactionTypeName2(String cd_type) {
		String returnValue = "";
		if (!isEmpty(cd_type)) {
			for (int i = 0; i < webposTransType.size(); i++) {
				String code = webposTransType.get(i).getCode();
				String value = webposTransType.get(i).getValue();
				if (code.equalsIgnoreCase(cd_type)) {
					returnValue = value;
				}
			}
		}
		return returnValue;
	}
    
    public String getTransactionShortNameList(wpChequeObject oper) {
    	StringBuilder html = new StringBuilder();
    	html.append(getWebposTransactionTypeOptions("", true));

    	String returnValue = "";
    	
    	int ni						= Integer.parseInt(oper.getValue("NI"));
		int n_payment				= Integer.parseInt(oper.getValue("N_PAYMENT"));
		int n_mov_bon				= Integer.parseInt(oper.getValue("N_MOV_BON"));
		int n_chk_card				= Integer.parseInt(oper.getValue("N_CHK_CARD"));
		int n_inval_card			= Integer.parseInt(oper.getValue("N_INVAL_CARD"));
		int n_storno_bon			= Integer.parseInt(oper.getValue("N_STORNO_BON"));
		int n_payment_im			= Integer.parseInt(oper.getValue("N_PAYMENT_IM"));
		int n_payment_ext			= Integer.parseInt(oper.getValue("N_PAYMENT_EXT"));
		int n_actvation				= Integer.parseInt(oper.getValue("N_ACTVATION"));
		//int n_cancel				= Integer.parseInt(oper.getValue("N_CANCEL"));
		int n_coupon				= Integer.parseInt(oper.getValue("N_COUPON"));
		int n_membership_fee		= Integer.parseInt(oper.getValue("N_MEMBERSHIP_FEE"));
		int n_mtf					= Integer.parseInt(oper.getValue("N_MTF"));
		int n_point_fee				= Integer.parseInt(oper.getValue("N_POINT_FEE"));
		int n_put_card				= Integer.parseInt(oper.getValue("N_PUT_CARD"));
		int n_questioning			= Integer.parseInt(oper.getValue("N_QUESTIONING"));
		//int n_return				= Integer.parseInt(oper.getValue("N_RETURN"));
		int n_share_fee				= Integer.parseInt(oper.getValue("N_SHARE_FEE"));
		int n_transfer_get_point	= Integer.parseInt(oper.getValue("N_TRANSFER_GET_POINT"));
		int n_payment_invoice		= Integer.parseInt(oper.getValue("N_PAYMENT_INVOICE"));
		int n_share_fee_change		= Integer.parseInt(oper.getValue("N_SHARE_FEE_CHANGE"));
		int n_transform_from_share	= Integer.parseInt(oper.getValue("N_TRANSFORM_FROM_SHARE"));
		int n_transfer_put_point	= Integer.parseInt(oper.getValue("N_TRANSFER_PUT_POINT"));
		
        String C_REC_PAYMENT 				= getWebposTransactionTypeName2("1") + ", ";
        String C_REC_MOV_BON        		= getWebposTransactionTypeName2("2") + ", ";
        String C_REC_CHK_CARD       		= getWebposTransactionTypeName2("3") + ", ";
        String C_REC_INVAL_CARD     		= getWebposTransactionTypeName2("4") + ", ";
        String C_REC_STORNO_BON     		= getWebposTransactionTypeName2("5") + ", ";
        String C_REC_PAYMENT_IM     		= getWebposTransactionTypeName2("6") + " ";
        String C_REC_PAYMENT_EXT    		= getWebposTransactionTypeName2("7") + ", ";
        String C_REC_ACTIVATION     		= getWebposTransactionTypeName2("8") + ", ";
        //String C_REC_CANCEL         		= getWebposTransactionTypeName2("9") + ", ";
        String C_REC_COUPON         		= getWebposTransactionTypeName2("10") + ", ";
        String C_REC_MEMBERSHIP_FEE 		= getWebposTransactionTypeName2("11") + ", ";
        String C_REC_MTF            		= getWebposTransactionTypeName2("12") + ", ";
        String C_REC_POINT_FEE      		= getWebposTransactionTypeName2("13") + ", ";
        String C_REC_PUT_CARD       		= getWebposTransactionTypeName2("14") + ", ";
        String C_REC_QUESTIONING    		= getWebposTransactionTypeName2("15") + ", ";
        //String C_REC_RETURN         		= getWebposTransactionTypeName2("16") + ", ";
        String C_REC_SHARE_FEE      		= getWebposTransactionTypeName2("17") + ", ";
        String C_REC_TRANSFER_GET_POINT 	= getWebposTransactionTypeName2("18") + ", ";
        String C_REC_PAYMENT_INVOICE		= getWebposTransactionTypeName2("19") + ", ";
        String C_REC_SHARE_FEE_CHANGE 		= getWebposTransactionTypeName2("20") + ", ";
        String C_REC_TRANSFORM_FROM_SHARE 	= getWebposTransactionTypeName2("21") + ", ";
        String C_REC_TRANSFER_PUT_POINT 	= getWebposTransactionTypeName2("22") + ", ";
        
        if (n_payment > 0) { returnValue= returnValue + C_REC_PAYMENT; }
        if (n_mov_bon > 0) { returnValue= returnValue + C_REC_MOV_BON; }
        if (n_chk_card > 0) { returnValue= returnValue + C_REC_CHK_CARD; }
        if (n_inval_card > 0) { returnValue= returnValue + C_REC_INVAL_CARD; }
        if (n_storno_bon > 0) { returnValue= returnValue + C_REC_STORNO_BON; }
        if (n_payment_im > 0) { returnValue= returnValue + C_REC_PAYMENT_IM; }
        if (n_payment_ext > 0) { returnValue= returnValue + C_REC_PAYMENT_EXT; }
        if (n_actvation > 0) { returnValue= returnValue + C_REC_ACTIVATION; }
        //if (n_cancel > 0) { returnValue= returnValue + C_REC_CANCEL; }
        if (n_coupon > 0) { returnValue= returnValue + C_REC_COUPON; }
        if (n_membership_fee > 0) { returnValue= returnValue + C_REC_MEMBERSHIP_FEE; }
        if (n_mtf > 0) { returnValue= returnValue + C_REC_MTF; }
        if (n_point_fee > 0) { returnValue= returnValue + C_REC_POINT_FEE; }
        if (n_put_card > 0) { returnValue= returnValue + C_REC_PUT_CARD; }
        if (n_questioning > 0) { returnValue= returnValue + C_REC_QUESTIONING; }
        //if (n_return > 0) { returnValue= returnValue + C_REC_RETURN; }
        if (n_share_fee > 0) { returnValue= returnValue + C_REC_SHARE_FEE; }
        if (n_transfer_get_point > 0) { returnValue= returnValue + C_REC_TRANSFER_GET_POINT; }
        if (n_payment_invoice > 0) { returnValue= returnValue + C_REC_PAYMENT_INVOICE; }
        if (n_share_fee_change > 0) { returnValue= returnValue + C_REC_SHARE_FEE_CHANGE; }
        if (n_transform_from_share > 0) { returnValue= returnValue + C_REC_TRANSFORM_FROM_SHARE; }
        if (n_transfer_put_point > 0) { returnValue= returnValue + C_REC_TRANSFER_PUT_POINT; }
        if (returnValue.length() > 0) {
        	returnValue = returnValue.substring(0, returnValue.length()-2);	
        	if (ni > 1) {
            	/*returnValue = "<div class=\"div_button\" onclick=\"ajaxpage('action/storno.jsp?id_term=11111111&id_telgr=191048&cd_card1=9900990010014&type=storno&process=no&action=check&back_type=operations','div_main');\">" + returnValue;
        		returnValue = returnValue + "&nbsp;<span class=\"telgr_detail\">&nbsp;</span>";
            	returnValue = returnValue + "</div>";*/
        	}
        }
    	return returnValue;
    }
	
	public String getWebposTransactionStateOptions(String cd_state, boolean isNull) {
		return getSelectBodyFromQuery(
				" SELECT cd_trans_state, name_trans_state, " +
				"        DECODE(fcd_trans_state, " +
      	  		" 	 	     'C_OK_TRANS', ' style=\"font-weight:bold;color: green;\" ', " +
      	  		"          	 'C_IN_PROCESS_TRANS', ' style=\"font-weight:bold;color: blue;\" ', " +
      	  		"          	 'C_ERROR_TRANS', ' style=\"font-weight:bold;color: red;\" ', " +
      	  		"          	 'C_RETURNED_TRANS', ' style=\"font-weight:bold;color: brown;\" ', " +
      	  		"          	 'C_RETURNED_PART_TRANS', ' style=\"font-weight:bold;color: brown;\" ', " +
      	  		"          	 'C_SENT_FOR_PAYMENT', ' style=\"font-weight:bold;color: blue;\" ', " +
	  			"          	 'C_NEED_CONFIRM', ' style=\"font-weight:bold;color: darkblue;\" ', " +
  	  			"          	 'C_CANCELLED_TRANS', ' style=\"font-weight:bold;color: brown;\" ', " +
      	  		"          	 '' " +
      	  		"  	     ) option_style " + 
				"   FROM " + getGeneralDBScheme() + ".vc_trans_state_all" + 
				" WHERE fcd_trans_state <> 'IN_PROCESS' " +
				"   AND is_online_state = 'Y' " +
				"  ORDER BY DECODE(fcd_trans_state, 'C_OK_TRANS', 1, 'C_IN_PROCESS_TRANS', 2, 'C_SENT_FOR_PAYMENT', 6, 'C_NEED_CONFIRM', 7, 'C_ERROR_TRANS', 4, 5), name_trans_state", 
				cd_state, isNull, webposTransState, false);
	}
	
	public String getWebposTelegramStateOptions(String cd_state, boolean isNull) {
		return getSelectBodyFromQuery(
				" SELECT cd_telgr_state, name_telgr_state, " +
				"        DECODE(cd_telgr_state, " +
      	  		" 	 	     'EXECUTED', ' style=\"font-weight:bold;color: green;\" ', " +
      	  		"          	 'IN_PROCESS', ' style=\"font-weight:bold;color: blue;\" ', " +
      	  		"          	 'ERROR', ' style=\"font-weight:bold;color: red;\" ', " +
      	  		"          	 'UNKNOWN', ' style=\"font-weight:bold;color: red;\" ', " +
      	  		"          	 'WARNING', ' style=\"color: red;\" ', " +
      	  		"          	 'FATAL_ERROR', ' style=\"font-weight:bold;color: red;\" ', " +
      	  		"          	 'RETURNED', ' style=\"font-weight:bold;color: brown;\" ', " +
      	  		"          	 'RETURNED_PARTIALLY', ' style=\"font-weight:bold;color: brown;\" ', " +
      	  		"          	 'SENT_FOR_PAYMENT', ' style=\"font-weight:bold;color: blue;\" ', " +
	  			"          	 'NEED_CONFIRM', ' style=\"font-weight:bold;color: darkblue;\" ', " +
  	  			"          	 'CANCELLED', ' style=\"font-weight:bold;color: brown;\" ', " +
      	  		"          	 '' " +
      	  		"  	     ) option_style " + 
				"   FROM " + getGeneralDBScheme() + ".vc_telgr_state_all" + 
				" WHERE cd_telgr_state <> 'IN_PROCESS' " +
				"  ORDER BY DECODE(cd_telgr_state, 'EXECUTED', 1, 'IN_PROCESS', 2, 'SENT_FOR_PAYMENT', 3, 'NEED_CONFIRM', 4, 'CANCELLED', 5, 'ERROR', 8, 'UNKNOWN', 9, 'WARNING', 7, 'FATAL_ERROR', 8, 6), name_telgr_state", 
				cd_state, isNull, webposTelegramState, false);
	}
	
	public String getShareholderDateOfBirthYear(String year, boolean isNull) {
		return getSelectBodyFromQuery2(
				" SELECT year cd_year, year name_year  " + 
				"   FROM " + getGeneralDBScheme() + ".vp$shareholder_age" + 
				"  ORDER BY year DESC", 
				year, isNull, false);
	}
	
	public String getYears(String year, boolean isNull) {
		return getSelectBodyFromQuery2(
				" SELECT year cd_year, year name_year  " + 
				"   FROM " + getGeneralDBScheme() + ".vp$year" + 
				"  ORDER BY year DESC", 
				year, isNull, false);
	}
	
	public String getWebposTransactionTypeName(String id_type) {
		return getOneValueByStringId2("SELECT name_trans_type FROM "
				+ getGeneralDBScheme()
				+ ".vc_trans_type_all WHERE cd_trans_type = ?", id_type);
	}
	
	public String getWebposTransactionStateName(String id_state) {
		return getOneValueByStringId2("SELECT name_trans_state FROM "
				+ getGeneralDBScheme()
				+ ".vc_trans_state_all WHERE cd_trans_state = ?", id_state);
	}
	
	public String getWebposTelegramStateName(String id_state) {
		return getOneValueByStringId2("SELECT name_telgr_state FROM "
				+ getGeneralDBScheme()
				+ ".vc_telgr_state_all WHERE cd_telgr_state = ?", id_state, webposTelegramState);
	}
	
	public String getOblastName(String id_oblast) {
		return getOneValueByIntId2("SELECT name_oblast FROM "
				+ getGeneralDBScheme()
				+ ".vc_oblast_all WHERE id_oblast = ?", id_oblast);
	}
	
	public String getJQueryAutocompleteOblastValues() {
        StringBuilder result = new StringBuilder();
        StringBuilder htmlOne = new StringBuilder();
        StringBuilder htmlCall = new StringBuilder();
        Statement st = null;
        Connection con = null;

        String mySQL = 
        	" SELECT cd_country, id_oblast, name_oblast " +
            "   FROM " + getGeneralDBScheme() + ".vc_oblast_all " +
            " ORDER BY cd_country, name_oblast ";
        String currentCountry = "";
        String oneFirst = "";
        String oneLast = "];\n";
        int countryCount = 0;
        try{
        	con = Connector.getConnection(getSessionId());
        	st = con.createStatement();
        	ResultSet rset = st.executeQuery(mySQL);
        	
        	result.append("jQuery.fn.selectOblast = function(field, country){\n");
        	result.append("return this.each(function(){\n");

            while (rset.next())
            {
            	if (!rset.getString("CD_COUNTRY").equalsIgnoreCase(currentCountry)) {
            		if (countryCount > 0 ) {
            			result.append(oneFirst);
            			htmlOne.delete(htmlOne.length()-2,htmlOne.length());
            			htmlOne.append("\n");
            			result.append(htmlOne.toString());
            			result.append(oneLast);
            		}
            		countryCount ++;
            		htmlOne.delete(0, htmlOne.length());
            		currentCountry = rset.getString("CD_COUNTRY");
            		oneFirst = "var oblast"+currentCountry+" = [\n";
            		
            		htmlCall.append("if (country == '" +currentCountry + "') {\n");
            		htmlCall.append("$(\"#\"+field+\"_name\")\n");
            		htmlCall.append(".autocomplete({\n");
       				htmlCall.append("source: oblast" + currentCountry + ",\n");
       				htmlCall.append("lookupLimit: 10,\n");
       				htmlCall.append("minLength:0,\n");
            		htmlCall.append("select: function( event , ui ) {\n");
            		htmlCall.append(" document.getElementById(field).value =ui.item.data; /*$(\"#\"+field ).val = ui.item.data;*/ /*alert( 'You selected: value=' + ui.item.value + ', data=' +ui.item.data );*/\n");
            		htmlCall.append("}\n");
            		htmlCall.append("})\n");
            		htmlCall.append(".focus(function(){\n");
            		htmlCall.append(" if (this.value == \"\") {  $(this).autocomplete(\"search\"); }\n");
            		//htmlCall.append("})\n");
            		//htmlCall.append(".bind('focus', function() {\n");
            		//htmlCall.append("if(!$(\"#\"+field+\"_name\").val().trim()) {\n");
            		//htmlCall.append("alert('value='+$(\"#\"+field+\"_name\").val().trim());\n");
            		//htmlCall.append("$(\"#\"+field+\"_name\").keydown();} \n");
            		htmlCall.append("});\n");
            		htmlCall.append("}\n");
            	}
            	htmlOne.append(" { value: '" + rset.getString("NAME_OBLAST") + "', data: '" + rset.getString("ID_OBLAST") + "' },\n");
            }
            if (countryCount > 0 ) {
    			result.append(oneFirst);
    			result.append(htmlOne.toString());
    			result.append(oneLast);
    		}
            result.append(htmlCall.toString());
            result.append("});\n");
        	result.append("};\n");
        } // try
        catch (SQLException e) {LOGGER.error(result, e);}
        catch (Exception el) {LOGGER.error(result, el);}
        finally {
            try {
                if (st!=null) st.close();
            } catch (SQLException w) {w.toString();}
            try {
                if (con!=null) con.close();
            } catch (SQLException w) {w.toString();}
            Connector.closeConnection(con);
        } // finally
        return result.toString();
	}
	
	public String getNatPrsRoleStateName (String pState, String pIsChecked) {
		String roleState = "";
		if ("GIVEN".equalsIgnoreCase(pState)) {
			roleState = this.webposXML.getfieldTransl("nat_prs_role_state_given", false);
		} else if ("ACTIVATED".equalsIgnoreCase(pState)) {
			roleState = this.webposXML.getfieldTransl("nat_prs_role_state_activated", false);
		} else if ("QUESTIONED".equalsIgnoreCase(pState)) {
			if ("Y".equalsIgnoreCase(pIsChecked)) {
				roleState = this.webposXML.getfieldTransl("nat_prs_role_state_questioned_checked", false);
			} else {
				roleState = this.webposXML.getfieldTransl("nat_prs_role_state_questioned_not_checked", false);
			}
		} else if ("CANCEL".equalsIgnoreCase(pState)) {
			roleState = this.webposXML.getfieldTransl("nat_prs_role_state_cancel", false);
		} else if ("ERROR".equalsIgnoreCase(pState)) {
			roleState = this.webposXML.getfieldTransl("nat_prs_role_state_error", false);
		}
		return roleState;
	}
	
	public String replyMessage (String pMessage) {
		StringBuilder result = new StringBuilder();
		result.append("\n\n"+("\n"+pMessage).replaceAll("(\r\n|\n)", "\n<< "));
		return result.toString();
	}

	public String getCdCard1(
			String pCardSerialNumber_IdIssuer_IdPaymentSystem) {
		return getOneValueByStringId2(
				" SELECT cd_card1 "
						+ "   FROM "
						+ getGeneralDBScheme()
						+ ".vp$card_short_all "
						+ "  WHERE card_serial_number||'_'||id_issuer||'_'||id_payment_system = ?",
				pCardSerialNumber_IdIssuer_IdPaymentSystem);
	}

	public String getWebPOSRolesOptions(String pIdRole, String pIdServicePlace, boolean isNull) {
		return getSelectBodyFromQuery2(" SELECT r.id_role, r.name_role "
				+ "   FROM " + getGeneralDBScheme() + ".vc_role_all r, v_jur_prs_name_all j"
				+ "  WHERE r.cd_module_type = 'WEBPOS' "
				+ "    AND r.id_jur_prs = j.id_jur_prs_parent "
				+ "    AND j.id_jur_prs = " + pIdServicePlace
				+ "  ORDER BY r.name_role", pIdRole, isNull);
	}

	public String getWebPOSAdminTerminalsOptions(String pIdTerminal, boolean isNull) {
		return getSelectBodyFromQuery2(" SELECT id_term, id_term id_term_name "
				+ "   FROM " + getGeneralDBScheme() + ".vp$term_all"
				+ "  WHERE id_service_place = " + loginUser.getValue("ID_SERVICE_PLACE_WORK")
				+ "  ORDER BY id_term", pIdTerminal, isNull);
	}
	
	public String getInvoiceSendWays(String pType, wpChequeObject oper) {

		StringBuilder lReturn = new StringBuilder();
		
		int SMSSendCount = 0;
		
		if ("INVOICE".equalsIgnoreCase(oper.getValue("PAY_TYPE"))) { 
			String sendSMSTitle = webposXML.getfieldTransl("send_cheque_sms", false).replace("$PHONE_MOBILE$",oper.getValue("PHONE_MOBILE_NAT_PRS_HIDE"));
			String sendSMSImpossible = webposXML.getfieldTransl("send_cheque_sms_impossible", false);
			String sendSMSImpossibleMaxSend = webposXML.getfieldTransl("send_cheque_sms_impossible_max_send", false);
			String sendEmailTitle = webposXML.getfieldTransl("send_cheque_email", false).replace("$EMAIL$",oper.getValue("EMAIL_NAT_PRS_HIDE"));
			String sendEmailImpossible = webposXML.getfieldTransl("send_cheque_email_impossible", false);
			lReturn.append("<tr>");
			
			lReturn.append("<td colspan=\"2\">");
			if ("robokassa".equalsIgnoreCase(pType)) {
				if (!isEmpty(oper.getValue("PHONE_MOBILE_NAT_PRS"))) {
					if (!isEmpty(oper.getValue("SMS_EXTERNAL_PAY_SEND_COUNT"))) {
						SMSSendCount = Integer.parseInt(oper.getValue("SMS_EXTERNAL_PAY_SEND_COUNT"));
					}
					if (SMSSendCount < C_INVOICE_MAX_SEND_COUNT) {
						lReturn.append("	<span id=\"robokassa_phone_setting\"><input type=\"hidden\" id=\"robokassa_payment_phone\" name=\"robokassa_payment_phone\" value=\""+oper.getValue("PHONE_MOBILE_NAT_PRS")+"\"><input id=\"send_robokassa_payment_SMS\" type=\"checkbox\" value=\"Y\" name=\"send_robokassa_payment_SMS\">");
						lReturn.append("	<label class=\"inputfield_finish_green\" for=\"send_robokassa_payment_SMS\">" + sendSMSTitle + "</label>&nbsp;(<span class=\"go_to\" onclick=\"robokassa_change_phone('"+webposXML.getfieldTransl("send_cheque_sms", false).replace("$PHONE_MOBILE$","")+"');\">изменить</span>)</span>");
					} else {
						lReturn.append("	<span style=\"color:red; font-weight:bold; font-family: arial; font-size:11px;\">" + sendSMSImpossibleMaxSend + "</span>");
					}
				} else {
					lReturn.append("	<span id=\"robokassa_phone_setting\"><span style=\"color:red; font-weight:bold; font-family: arial; font-size:11px;\">" + sendSMSImpossible + "</span>&nbsp;(<span class=\"go_to\" onclick=\"robokassa_change_phone('"+webposXML.getfieldTransl("send_cheque_sms", false).replace("$PHONE_MOBILE$","")+"');\">" + webposXML.getfieldTransl2(getLanguage(), "title_set").toLowerCase() + "</span>)</span>");
				}
				lReturn.append("	<br>");
			}
			if (!isEmpty(oper.getValue("EMAIL_NAT_PRS"))) {
				lReturn.append("	<span id=\""+pType+"_email_setting\"><input type=\"hidden\" id=\""+pType+"_payment_email\" name=\""+pType+"_payment_email\" value=\""+oper.getValue("EMAIL_NAT_PRS")+"\"><input id=\"send_"+pType+"_payment_EMAIL\" type=\"checkbox\" checked value=\"Y\" name=\"send_"+pType+"_payment_EMAIL\">");
				lReturn.append("	<label class=\"inputfield_finish_green\" for=\"send_"+pType+"_payment_EMAIL\">" + sendEmailTitle + "</label>&nbsp;(<span class=\"go_to\" onclick=\""+pType+"_change_email('"+webposXML.getfieldTransl("send_cheque_email", false).replace("$EMAIL$","")+"');\">изменить</span>)</span>");
			} else {
				lReturn.append("	<span id=\""+pType+"_email_setting\"><span style=\"color:red; font-weight:bold; font-family: arial; font-size:11px;\">" + sendEmailImpossible + "</span>&nbsp;(<span class=\"go_to\" onclick=\""+pType+"_change_email('"+webposXML.getfieldTransl("send_cheque_email", false).replace("$EMAIL$","")+"');\">" + webposXML.getfieldTransl2(getLanguage(), "title_set").toLowerCase() + "</span>)</span>");
			}
			lReturn.append("</td>");
			lReturn.append("</tr>");
			//lReturn.append("<tr><td colspan=\"4\">&nbsp;</td></tr>");
		}
		return lReturn.toString();
	}
	
	public String getTelegramPaymendDescription(wpTelegramObject oper) {
		StringBuilder lReturn = new StringBuilder();
		
		if (oper==null) {
			return lReturn.toString();
		}
		
		String oprSum = oper.getValue("OPR_SUM_FRMT");
		String snameCurrency = oper.getValue("SNAME_CURRENCY");
		String ncTerm = oper.getValue("NC");
		String sysDate = oper.getValue("DATE_TELGR_DF");
		String rrn = oper.getValue("RRN");
		
		/*
		String beginString = "";
		if ("REC_PAYMENT".equalsIgnoreCase(operType)) {
			beginString = "Оплата ";
		} else if ("REC_ACTIVATION".equalsIgnoreCase(operType)) {
			beginString = "Активация карты";
		//} else if ("REC_CANCEL".equalsIgnoreCase(operType)) {
		//	beginString = "Отмена операции";
		} else if ("REC_COUPON".equalsIgnoreCase(operType)) {
			beginString = "Выдача сертификата";
		} else if ("REC_MEMBERSHIP_FEE".equalsIgnoreCase(operType)) {
			beginString = "Членский взнос";
		} else if ("REC_POINT_FEE".equalsIgnoreCase(operType)) {
			beginString = "Пополнение баллов";
		} else if ("REC_MTF".equalsIgnoreCase(operType)) {
			boolean canSubscribe = "Y".equalsIgnoreCase(oper.getValue("CAN_SUBSCRIBE_TARGET_PRG"));
			boolean hasEntranceFee = false;
			boolean hasMembershipFee = false;
			if (!(oper.getValue("ENTRANCE_FEE_SUM")==null || 
					"".equalsIgnoreCase(oper.getValue("ENTRANCE_FEE_SUM")) ||
					"0".equalsIgnoreCase(oper.getValue("ENTRANCE_FEE_SUM")))) {
				hasEntranceFee = true;
			}
			if (!(oper.getValue("MEMBERSHIP_FEE_SUM")==null || 
					"".equalsIgnoreCase(oper.getValue("MEMBERSHIP_FEE_SUM")) ||
					"0".equalsIgnoreCase(oper.getValue("MEMBERSHIP_FEE_SUM")))) {
				hasMembershipFee = true;
			}
			if (canSubscribe) {
				beginString = "Подписка на ЦП '" + targetProgramName + "'";
			}
			if (hasEntranceFee && hasMembershipFee) {
				beginString = ("".equalsIgnoreCase(beginString))?"ЧЦВ за вступление и участие в ЦП '" + targetProgramName + "'" + targetProgramMembershipTXT:", ЧЦВ за вступление и участие в ЦП" + targetProgramMembershipTXT;
			} else {
				if (hasEntranceFee) {
					beginString = ("".equalsIgnoreCase(beginString))?"ЧЦВ за вступление в ЦП '" + targetProgramName + "'":", ЧЦВ за вступление в ЦП";
				}
				if (hasMembershipFee) {
					beginString = ("".equalsIgnoreCase(beginString))?"ЧЦВ за участие в ЦП '" + targetProgramName + "'" + targetProgramMembershipTXT:", ЧЦВ за участие" + targetProgramMembershipTXT;
				}
			}
			if (!(canSubscribe || hasEntranceFee || hasMembershipFee)) { 
				beginString = "ЧЦВ по ЦП '" + targetProgramName + "'";
			}
		} else if ("REC_PUT_CARD".equalsIgnoreCase(operType)) {
			beginString = "Выдача карты";
		} else if ("REC_QUESTIONING".equalsIgnoreCase(operType)) {
			beginString = "Обработка анкеты";
		//} else if ("REC_RETURN".equalsIgnoreCase(operType)) {
		//	beginString = "Возврат";
		} else if ("REC_SHARE_FEE".equalsIgnoreCase(operType)) {
			beginString = "Паевой взнос";
		} else if ("REC_TRANSFER_POINT".equalsIgnoreCase(operType)) {
			beginString = "Перевод баллов";
		} else {
			beginString = "Операция";
		}*/
		
		if (!(isEmpty(oper.getValue("OPR_SUM")) || "0".equalsIgnoreCase(oper.getValue("OPR_SUM")))) {
			oprSum = "Внесение " + oprSum + " " + snameCurrency;
		} else {
			oprSum = "Операция ";
		}
		lReturn.append(oprSum + " по документу № " + ncTerm + " от " + sysDate + ", RRN " + rrn);
		LOGGER.debug("getInvoicePaymendDescription(), id_telgr=" + oper.getValue("ID_TELGR") + ", description=" + lReturn.toString());
		return lReturn.toString();
	}
	
	public String getInvoicePaymendDescription(wpChequeObject oper) {
		StringBuilder lReturn = new StringBuilder();
		
		if (oper==null) {
			return lReturn.toString();
		}
		
		String oprSum = oper.getValue("OPR_SUM_FRMT");
		String snameCurrency = oper.getValue("SNAME_CURRENCY");
		String ncTerm = oper.getValue("NC");
		String sysDate = oper.getValue("DATE_TELGR_DF");
		String rrn = oper.getValue("RRN");
		
		/*
		String beginString = "";
		if ("REC_PAYMENT".equalsIgnoreCase(operType)) {
			beginString = "Оплата ";
		} else if ("REC_ACTIVATION".equalsIgnoreCase(operType)) {
			beginString = "Активация карты";
		//} else if ("REC_CANCEL".equalsIgnoreCase(operType)) {
		//	beginString = "Отмена операции";
		} else if ("REC_COUPON".equalsIgnoreCase(operType)) {
			beginString = "Выдача сертификата";
		} else if ("REC_MEMBERSHIP_FEE".equalsIgnoreCase(operType)) {
			beginString = "Членский взнос";
		} else if ("REC_POINT_FEE".equalsIgnoreCase(operType)) {
			beginString = "Пополнение баллов";
		} else if ("REC_MTF".equalsIgnoreCase(operType)) {
			boolean canSubscribe = "Y".equalsIgnoreCase(oper.getValue("CAN_SUBSCRIBE_TARGET_PRG"));
			boolean hasEntranceFee = false;
			boolean hasMembershipFee = false;
			if (!(oper.getValue("ENTRANCE_FEE_SUM")==null || 
					"".equalsIgnoreCase(oper.getValue("ENTRANCE_FEE_SUM")) ||
					"0".equalsIgnoreCase(oper.getValue("ENTRANCE_FEE_SUM")))) {
				hasEntranceFee = true;
			}
			if (!(oper.getValue("MEMBERSHIP_FEE_SUM")==null || 
					"".equalsIgnoreCase(oper.getValue("MEMBERSHIP_FEE_SUM")) ||
					"0".equalsIgnoreCase(oper.getValue("MEMBERSHIP_FEE_SUM")))) {
				hasMembershipFee = true;
			}
			if (canSubscribe) {
				beginString = "Подписка на ЦП '" + targetProgramName + "'";
			}
			if (hasEntranceFee && hasMembershipFee) {
				beginString = ("".equalsIgnoreCase(beginString))?"ЧЦВ за вступление и участие в ЦП '" + targetProgramName + "'" + targetProgramMembershipTXT:", ЧЦВ за вступление и участие в ЦП" + targetProgramMembershipTXT;
			} else {
				if (hasEntranceFee) {
					beginString = ("".equalsIgnoreCase(beginString))?"ЧЦВ за вступление в ЦП '" + targetProgramName + "'":", ЧЦВ за вступление в ЦП";
				}
				if (hasMembershipFee) {
					beginString = ("".equalsIgnoreCase(beginString))?"ЧЦВ за участие в ЦП '" + targetProgramName + "'" + targetProgramMembershipTXT:", ЧЦВ за участие" + targetProgramMembershipTXT;
				}
			}
			if (!(canSubscribe || hasEntranceFee || hasMembershipFee)) { 
				beginString = "ЧЦВ по ЦП '" + targetProgramName + "'";
			}
		} else if ("REC_PUT_CARD".equalsIgnoreCase(operType)) {
			beginString = "Выдача карты";
		} else if ("REC_QUESTIONING".equalsIgnoreCase(operType)) {
			beginString = "Обработка анкеты";
		//} else if ("REC_RETURN".equalsIgnoreCase(operType)) {
		//	beginString = "Возврат";
		} else if ("REC_SHARE_FEE".equalsIgnoreCase(operType)) {
			beginString = "Паевой взнос";
		} else if ("REC_TRANSFER_POINT".equalsIgnoreCase(operType)) {
			beginString = "Перевод баллов";
		} else {
			beginString = "Операция";
		}*/
		
		if (!(isEmpty(oper.getValue("OPR_SUM")) || "0".equalsIgnoreCase(oper.getValue("OPR_SUM")))) {
			oprSum = "Внесение " + oprSum + " " + snameCurrency;
		} else {
			oprSum = "Операция ";
		}
		lReturn.append(oprSum + " по документу № " + ncTerm + " от " + sysDate + ", RRN " + rrn);
		LOGGER.debug("getInvoicePaymendDescription(), id_telgr=" + oper.getValue("ID_TELGR") + ", description=" + lReturn.toString());
		return lReturn.toString();
	}
	
	public String getMarginDescription (String pIdClub, String pIdDealer, String pCdTransType) {
		String marginTextInitial = "";
		String marginText = "";
		wpDealerMarginObject margin = null;
		if ("CASH_CHANGE".equalsIgnoreCase(pCdTransType)) {
			margin = new wpDealerMarginObject(pIdClub, pIdDealer, "REC_SHARE_FEE_CHANGE");
			marginTextInitial = "За <u class=\"underline\">зачисление сдачи в паевой фонд</u> ";
		} else {
			margin = new wpDealerMarginObject(pIdClub, pIdDealer, pCdTransType);
			if ("REC_PAYMENT".equalsIgnoreCase(pCdTransType)) {
				marginTextInitial = "За обработку операции <u class=\"underline\">оплаты за товар</u> ";
			} else if ("REC_ACTIVATION".equalsIgnoreCase(pCdTransType)) {
				marginTextInitial = "За <u class=\"underline\">активацию карты</u> ";
			//} else if ("REC_CANCEL".equalsIgnoreCase(pCdTransType)) {
			//	marginTextInitial = "За <u class=\"underline\">отмену операции</u> ";
			} else if ("REC_COUPON".equalsIgnoreCase(pCdTransType)) {
				marginTextInitial = "За <u class=\"underline\">активацию купона</u> ";
			} else if ("REC_MEMBERSHIP_FEE".equalsIgnoreCase(pCdTransType)) {
				marginTextInitial = "За <u class=\"underline\">прием членского взноса</u> ";
			} else if ("REC_MTF".equalsIgnoreCase(pCdTransType)) {
				marginTextInitial = "За <u class=\"underline\">прием ЧЦВ</u> ";
			} else if ("REC_POINT_FEE".equalsIgnoreCase(pCdTransType)) {
				marginTextInitial = "За <u class=\"underline\">пополнение баллов</u> ";
			} else if ("REC_PUT_CARD".equalsIgnoreCase(pCdTransType)) {
				marginTextInitial = "За <u class=\"underline\">выдачу карты</u> ";
			} else if ("REC_QUESTIONING".equalsIgnoreCase(pCdTransType)) {
				marginTextInitial = "За <u class=\"underline\">обработку анкеты</u> ";
			//} else if ("REC_RETURN".equalsIgnoreCase(pCdTransType)) {
			//	marginTextInitial = "За <u class=\"underline\">возврат товара</u> ";
			} else if ("REC_SHARE_FEE".equalsIgnoreCase(pCdTransType)) {
				marginTextInitial = "За <u class=\"underline\">прием паевого взноса</u> ";
			} else if ("REC_TRANSFER_GET_POINT".equalsIgnoreCase(pCdTransType)) {
				marginTextInitial = "За <u class=\"underline\">перевод баллов с карты на карту</u> ";
			} else if ("REC_TRANSFER_PUT_POINT".equalsIgnoreCase(pCdTransType)) {
				marginTextInitial = "За <u class=\"underline\">перевод баллов с карты на карту</u> ";
			} else {
				marginTextInitial = "За <u class=\"underline\">обработку данной операции</u> ";
			}
		}
		
		if (margin.getResultSetRowCount() > 0) {
			if ("N".equalsIgnoreCase(margin.getValue("IS_MARGIN_INTO_AMOUNT"))) {
				if (!isEmpty(margin.getValue("MARGIN_FIXED_AMOUNT"))) {
					marginText = "взымается <b>комиссия</b> в размере <b>"+margin.getValue("MARGIN_FIXED_AMOUNT_FRMT") + " " + margin.getValue("SNAME_CURRENCY")+"</b>";
				} else {
					marginText = "взымается <b>комиссия " + margin.getValue("MARGIN_PERCENT_AMOUNT") + " %</b> " + ("CASH_CHANGE".equalsIgnoreCase(pCdTransType)?"от <u>суммы сдачи, зачисляемой в паевой фонд</u>":"от <u>суммы операции</u>");
				}
				if (!isEmpty(margin.getValue("MARGIN_MIN_AMOUNT"))) {
					marginText = marginText + " (но <b>не меньше " + margin.getValue("MARGIN_MIN_AMOUNT_FRMT") + " " + margin.getValue("SNAME_CURRENCY") + "</b>)";
				}
			}
		}
		if (!isEmpty(marginText)) {
			marginText = marginTextInitial + marginText;
		}
		//LOGGER.debug(marginText);
		return marginText;
	}
	
	public String getMarginChangeToShareAccountDescription (String pIdClub, String pIdDealer) {
		String marginText = "";
		
		wpDealerMarginObject margin = new wpDealerMarginObject(pIdClub, pIdDealer, "REC_SHARE_FEE_CHANGE");
		
		if (margin.getResultSetRowCount() > 0) {
			if (!isEmpty(margin.getValue("MARGIN_FIXED_AMOUNT"))) {
				marginText = marginText + "При <u>зачислении сдачи в паевой фонд</u> взымается <b>комиссия</b> в размере <b>"+margin.getValue("MARGIN_FIXED_AMOUNT_FRMT") + " " + margin.getValue("SNAME_CURRENCY")+"</b>";
			} else {
				marginText = marginText + "При <u>зачислении сдачи в паевой фонд</u> взымается <b>комиссия " + margin.getValue("MARGIN_PERCENT_AMOUNT") + " %</b> от суммы сдачи, зачисляемой в паевой фонд";
			}
			if (!isEmpty(margin.getValue("MARGIN_MIN_AMOUNT"))) {
				marginText = marginText + " (но <b>не меньше " + margin.getValue("MARGIN_MIN_AMOUNT_FRMT") + " " + margin.getValue("SNAME_CURRENCY") + "</b>)";
			}
		}
		return marginText;
	}
	
	public String getMarginRobokassaPaymentOnTerminalDescription (String pIdClub, String pIdDealer) {
		String marginText = "";
		
		
		wpRobokassaMarginObject margin = new wpRobokassaMarginObject(/*pIdClub, pIdDealer*/);
		
		if (margin.getResultSetRowCount() > 0) {
			marginText = "При <u>оплате через Робокассу</u> взымается <b>комиссия</b> в размере <b> " + margin.getMarginPercent() + " %</b> от суммы операции";
		}
		return marginText;
	}
	
	public String getPayTypeImage (String pPayType, String pTabNameMenuElement, String pHyperLink, String pDivName, String pValidationFormName, String pValidationScriptName) {
		StringBuilder html = new StringBuilder();
		boolean hasPermission = false;
		boolean isEnable = false;
		String hyperLink = "";
		//if ("ROBOKASSA".equalsIgnoreCase(pPayType)) {
		//	hyperLink = "window.open('" + pHyperLink + "')";
		//} else {
			if (!isEmpty(pValidationScriptName)) {
				hyperLink = "try {if (!" + pValidationScriptName + "()) {return false;} } catch(err) {} ";
			}
			hyperLink = hyperLink + " ajaxpage('" + pHyperLink + "&' + mySubmitForm('" + pValidationFormName + "'),'" + pDivName + "');";
		//}
		
		if (!isEmpty(pTabNameMenuElement)) {
			if (this.isMenuElementVisible(pTabNameMenuElement)) {
				hasPermission = true;
			}
			if (this.hasExecuteMenuPermission(pTabNameMenuElement) && this.isMenuElementEnable(pTabNameMenuElement)) {
				isEnable = true;
			}
		} else {
			hasPermission = true;
			isEnable = true;
		}
		html.append(getPayTypeImageImplementation(pPayType, hasPermission, isEnable, hyperLink));
		return html.toString();
	}
	
	public String getPayTypeImageDisable (String pPayType) {
		StringBuilder html = new StringBuilder();
		html.append(getPayTypeImageImplementation(pPayType, true, false, ""));
		return html.toString();
	}
	
	public String getPayTypeImageImplementation (String pPayType, Boolean hasPermission, Boolean isEnable, String pHyperLink) {
		StringBuilder html = new StringBuilder();
		String imageName = "";
		String imageCaption = "";
		String imageTitle = "";
		if ("CASH".equalsIgnoreCase(pPayType)) {
			imageName = "cash.jpg";
			imageCaption = this.webposXML.getfieldTransl("goods_pay_cash_caption", false);
			imageTitle = this.webposXML.getfieldTransl("goods_pay_cash", false);
		} else if ("BANK_CARD".equalsIgnoreCase(pPayType)) {
			imageName = "bank_card.jpg";
			imageCaption = this.webposXML.getfieldTransl("goods_pay_card_caption", false);
			imageTitle = this.webposXML.getfieldTransl("goods_pay_card", false);
		} else if ("SMPU_CARD".equalsIgnoreCase(pPayType)) {
			imageName = "smpu_card.jpg";
			imageCaption = this.webposXML.getfieldTransl("goods_pay_points_caption", false);
			imageTitle = this.webposXML.getfieldTransl("goods_pay_points", false);
		} else if ("ROBOKASSA".equalsIgnoreCase(pPayType)) {
			imageName = "robokassa.jpg";
			imageCaption = this.webposXML.getfieldTransl("goods_pay_robokassa_caption", false);
			imageTitle = this.webposXML.getfieldTransl("goods_pay_robokassa", false);
		} else if ("INVOICE".equalsIgnoreCase(pPayType)) {
			imageName = "invoice.jpg";
			imageCaption = this.webposXML.getfieldTransl("goods_pay_invoices_caption", false);
			imageTitle = this.webposXML.getfieldTransl("goods_pay_invoices", false);
		}
		if (hasPermission) {
			if (isEnable) {
				html.append("<td><img class=\"pay_type_button\" src=\"" + getThemePath() + "/images/menu/pay_type/" + imageName + "\"" 
					+ " onclick=\""	+ pHyperLink + "\""
					+ " title=\"" + imageTitle + "\"" 
					+ ">"
					+ "<br><span class=\"pay_type_caption\" " 
					+ " onclick=\""	+ pHyperLink + "\">" + imageCaption + "</span></td>");
			} else {
				html.append("<td><img class=\"pay_type_button_disable\" src=\"" + getThemePath() + "/images/menu/pay_type/" + imageName + "\" "
					+ " title=\"" + imageTitle + "\"" 
					+ ">"
					+"<br><span class=\"pay_type_caption pay_type_caption_disable\">" + imageCaption + "</span></td>");
			}
		}
		return html.toString();
	}
	
	public String getReturnCancelImage (String pOperationType, String pTabNameMenuElement, String pHyperLink, String pDivName, String pValidationFormName, String pValidationScriptName) {
		 return getReturnCancelImageDisable (pOperationType, pTabNameMenuElement, false, pHyperLink, pDivName, pValidationFormName, pValidationScriptName);
	}
	
	public String getReturnCancelImageDisable (String pOperationType, String pTabNameMenuElement, boolean isDisable, String pHyperLink, String pDivName, String pValidationFormName, String pValidationScriptName) {
		StringBuilder html = new StringBuilder();
		boolean hasPermission = false;
		boolean isEnable = false;
		String imageName = "";
		String imageCaption = "";
		String imageTitle = "";
		String hyperLink = "";
		if (!isEmpty(pValidationScriptName)) {
			hyperLink = "try {if (!" + pValidationScriptName + "()) {return false;} } catch(err) {} ";
		}
		hyperLink = hyperLink + " ajaxpage('" + pHyperLink + "&' + mySubmitForm('" + pValidationFormName + "'),'" + pDivName + "');";
		
		
		if (this.isMenuElementVisible(pTabNameMenuElement)) {
			hasPermission = true;
		}
		if (this.hasExecuteMenuPermission(pTabNameMenuElement) && this.isMenuElementEnable(pTabNameMenuElement)) {
			isEnable = true;
		}
		if (isDisable) {
			isEnable = false;
		}
		if ("CANCEL".equalsIgnoreCase(pOperationType)) {
			imageName = "cancellation.jpg";
			imageCaption = this.webposXML.getfieldTransl("storno_type_cancellation", false);
			imageTitle = this.webposXML.getfieldTransl("storno_type_cancellation", false);
		} else if ("RETURN".equalsIgnoreCase(pOperationType)) {
			imageName = "return.jpg";
			imageCaption = this.webposXML.getfieldTransl("storno_type_return", false);
			imageTitle = this.webposXML.getfieldTransl("storno_type_return", false);
		}
		if (hasPermission) {
			if (isEnable) {
				html.append("<img class=\"cancel_type_button\" src=\"" + getThemePath() + "/images/menu/pay_type/" + imageName + "\"" 
					+ " onclick=\""	+ hyperLink + "\""
					+ " title=\"" + imageTitle + "\"" 
					+ ">"
					+ "<br><span class=\"cancel_type_caption\" " 
					+ " onclick=\""	+ hyperLink + "\">" + imageCaption + "</span>");
			} else {
				html.append("<img class=\"cancel_type_button_disable\" src=\"" + getThemePath() + "/images/menu/pay_type/" + imageName + "\" "
					+ " title=\"" + imageTitle + "\"" 
					+ ">"
					+"<br><span class=\"cancel_type_caption cancel_type_caption_disable\">" + imageCaption + "</span>");
			}
		}
		return html.toString();
	}
	
	public String getMembershipMonths (String pFieldName, String pFieldTitle, String pCurrenValue, String pMonthCount, String pMembershipFeeAmount, String pMembershipFeeSNameCurrency) {
		StringBuilder html = new StringBuilder();
		
		LOGGER.debug("getMembershipMonths: pCurrenValue=" + pCurrenValue + ", pMonthCount=" + pMonthCount + ", pMembershipFeeAmount=" + pMembershipFeeAmount + ", pMembershipFeeSNameCurrency=" + pMembershipFeeSNameCurrency);
		
		html.append(getSelectBeginHTML(pFieldName, pFieldTitle));

		try {
			int lMonthCount = 0;
			if (isEmpty(pMonthCount)) {
				lMonthCount = 12;
			} else {
				lMonthCount = Integer.parseInt(pMonthCount);
			}
			//lMonthCount = (lMonthCount>12)?12:lMonthCount;
			
			float lMembershipFeeAmount = Float.parseFloat(pMembershipFeeAmount.replace(",","."));
			float lCurrentAmount = 0;
			String lStringAmount = "";
			String lStringCaption = "";
			String lMonthsCaption = "";
			
			float lCurrentNoPayAmount   = 0;
			String lCurrentNoPayAmountString = "";
			if (!isEmpty(pCurrenValue)) {
				lCurrentNoPayAmount = Float.parseFloat(pCurrenValue.replace(",","."));
				lCurrentNoPayAmountString = (new DecimalFormat("#0.00").format(lCurrentNoPayAmount)+"").replace(".",",");
			}
			
	    	for (int i = 1; i <= lMonthCount; i++) {
	    		lCurrentAmount = i * lMembershipFeeAmount;
	    		lStringAmount = (new DecimalFormat("#0.00").format(lCurrentAmount)+"").replace(".",",");
	    		if (i==1) {
	    			lMonthsCaption = commonXML.getfieldTransl("title_1_month", false).toLowerCase();
	    		} else if (i>1 && i<=4) {
	    			lMonthsCaption = commonXML.getfieldTransl("title_2_4_months", false).toLowerCase();
	    		} else {
	    			lMonthsCaption = commonXML.getfieldTransl("title_5_more_months", false).toLowerCase();
	    		}
	    		lStringCaption = lStringAmount + " " + pMembershipFeeSNameCurrency + " (" + i + " " + lMonthsCaption + ")";
	    		html.append(getSelectOptionHTML(lCurrentNoPayAmountString, lStringAmount, lStringCaption));
	    	}
		} catch (Exception e) {
			LOGGER.error("getMembershipMonths() Exception: " + e.toString());
		}
    	html.append(getSelectEndHTML());
		return html.toString();
	}
	
	public String getTargetProgramPayCounts (String pFieldName, String pFieldTitle, String pCurrenValue, String pPayPeriod, String pPayCount, String pPayAmount, String pPaySNameCurrency) {
		StringBuilder html = new StringBuilder();
		
		LOGGER.debug("getTargetProgramPayCounts: pCurrenValue=" + pCurrenValue + ", pPayPeriod=" + pPayPeriod + ", pPayCount=" + pPayCount + ", pPayAmount=" + pPayAmount + ", pPaySNameCurrency=" + pPaySNameCurrency);

		//int lPayCount = Integer.parseInt(pPayCount);
		//lPayCount = (lPayCount>12)?12:lPayCount;
		int lPayCount = 12;
		
		html.append(getSelectBeginHTML(pFieldName, pFieldTitle));
		
		try {
			float lPayAmount = Float.parseFloat(pPayAmount.replace(",","."));
			float lCurrentAmount = 0;
			String lStringAmount = "";
			String lStringCaption = "";
			String lTmpCaption = "";
			
	    	for (int i = 1; i <= lPayCount; i++) {
	    		lCurrentAmount = i * lPayAmount;
	    		lStringAmount = (new DecimalFormat("#0.00").format(lCurrentAmount)+"").replace(".",",");
	    		int lPayCountValue = i;
	    		if (!isEmpty(pPayCount)) {
	    			lPayCountValue = i * Integer.parseInt(pPayCount);
	    		}
	    		
	    		if ("MONTH".equalsIgnoreCase(pPayPeriod)) { 
		    		if (lPayCountValue==1) {
		    			lTmpCaption = commonXML.getfieldTransl("title_1_month", false).toLowerCase();
		    		} else if (lPayCountValue>1 && lPayCountValue<=4) {
		    			lTmpCaption = commonXML.getfieldTransl("title_2_4_months", false).toLowerCase();
		    		} else {
		    			lTmpCaption = commonXML.getfieldTransl("title_5_more_months", false).toLowerCase();
		    		}
	    		} else if ("STUDY_COUNT".equalsIgnoreCase(pPayPeriod)) { 
		    		if (lPayCountValue==1) {
		    			lTmpCaption = commonXML.getfieldTransl("title_1_study", false).toLowerCase();
		    		} else if (lPayCountValue>1 && lPayCountValue<=4) {
		    			lTmpCaption = commonXML.getfieldTransl("title_2_4_studies", false).toLowerCase();
		    		} else {
		    			lTmpCaption = commonXML.getfieldTransl("title_5_more_studies", false).toLowerCase();
		    		}
	    		}
	    		lStringCaption = lStringAmount + " " + pPaySNameCurrency + " (" + lPayCountValue + " " + lTmpCaption + ")";
	    		html.append(getSelectOptionHTML(pCurrenValue, lStringAmount, lStringCaption));
	    	}
		} catch (Exception e) {
			LOGGER.error("getTargetProgramPayCounts() Exception: " + e.toString());
		}
    	html.append(getSelectEndHTML());
		return html.toString();
	}
	
	public String getTargetProgramPayValues2 (String pPayPeriod, String pPayCount, String pPayAmount, String pPaySNameCurrency, String pScriptName) {
		StringBuilder html = new StringBuilder();
		
		int lPayCount = 12;
		html.append("<script type=\"text/javascript\">\n");
		html.append("function setPayValue(tp_fee) {\n");
		html.append("	document.getElementById('tp_fee').value = tp_fee;	\n");
		if (!isEmpty(pScriptName)) {
			html.append("	"+pScriptName+";	\n");
		}
		html.append("}\n");
		html.append("function getOffset(el) {\n");
		html.append("	  el = el.getBoundingClientRect();\n");
		html.append("	  return {\n");
		html.append("	    left: el.left + window.scrollX,\n");
		html.append("	    top: el.top + window.scrollY\n");
		html.append("	  }\n");
		html.append("	}\n\n");
	        
		html.append("function show(wrp, wnd){\n");
		html.append("	document.getElementById('wrap_div').style.display = wrp;\n");	
		html.append("	document.getElementById('window_div').style.display = wnd;\n");
		html.append("	var okno = document.getElementById('window_div');\n");
		html.append("	var lnk = document.getElementById('listEmem');\n");
		html.append("	okno.style.left = getOffset(lnk).left;\n");
		html.append("    okno.style.top  = getOffset(lnk).top;\n");
		html.append("}\n");
		html.append("</script>\n\n");
		
		html.append("<div onclick=\"show('none','none')\" id=\"wrap_div\"></div>\n");
		html.append("<div id=\"window_div\">\n");
		
		try {
			float lPayAmount = Float.parseFloat(pPayAmount.replace(",","."));
			float lCurrentAmount = 0;
			String lStringAmount = "";
			String lStringCaption = "";
			String lTmpCaption = "";
			
	    	for (int i = 1; i <= lPayCount; i++) {
	    		lCurrentAmount = i * lPayAmount;
	    		lStringAmount = (new DecimalFormat("#0.00").format(lCurrentAmount)+"").replace(".",",");
	    		int lPayCountValue = i;
	    		if (!isEmpty(pPayCount)) {
	    			lPayCountValue = i * Integer.parseInt(pPayCount);
	    		}
	    		
	    		if ("MONTH".equalsIgnoreCase(pPayPeriod)) { 
		    		if (lPayCountValue==1) {
		    			lTmpCaption = commonXML.getfieldTransl("title_1_month", false).toLowerCase();
		    		} else if (lPayCountValue>1 && lPayCountValue<=4) {
		    			lTmpCaption = commonXML.getfieldTransl("title_2_4_months", false).toLowerCase();
		    		} else {
		    			lTmpCaption = commonXML.getfieldTransl("title_5_more_months", false).toLowerCase();
		    		}
	    		} else if ("STUDY_COUNT".equalsIgnoreCase(pPayPeriod)) { 
		    		if (lPayCountValue==1) {
		    			lTmpCaption = commonXML.getfieldTransl("title_1_study", false).toLowerCase();
		    		} else if (lPayCountValue>1 && lPayCountValue<=4) {
		    			lTmpCaption = commonXML.getfieldTransl("title_2_4_studies", false).toLowerCase();
		    		} else {
		    			lTmpCaption = commonXML.getfieldTransl("title_5_more_studies", false).toLowerCase();
		    		}
	    		}
	    		lStringCaption = lStringAmount + " " + pPaySNameCurrency + " (" + lPayCountValue + " " + lTmpCaption + ")";
	    		html.append("<div id=\"value"+lCurrentAmount+"\" class=\"elem_div\" onclick=\"setPayValue('"+lStringAmount+"'); show('none','none')\">"+lStringCaption+"</div>");
	    	}
		} catch (Exception e) {
			LOGGER.error("getTargetProgramPayCounts() Exception: " + e.toString());
		}
		html.append("</div>\n");
		html.append("<a href=\"#\" id=\"listEmem\" onclick=\"show('block','table')\">выбрать из списка</a>\n");
		return html.toString();
	}
	
	public String getTargetProgramPlacesOptions(String idTargetPrg, String idPlace) {
		return getSelectBodyFromQuery2(
				" SELECT id_target_prg_place, sname_service_place "
						+ "   FROM "
						+ getGeneralDBScheme()
						+ ".vc_target_prg_place_all "
						+ "  WHERE id_target_prg =" + idTargetPrg 
						+ "  ORDER BY sname_service_place",
						idPlace, true);

	}
	
	public String getTargetProgramPlaceName(String idPlace) {
		return getOneValueByIntId2("SELECT sname_service_place FROM "
				+ getGeneralDBScheme()
				+ ".vc_target_prg_place_all WHERE id_target_prg_place = ?", idPlace);
	}
	
	public String getWebPOSPointCurrencyName() {
		return webposXML.getfieldTransl("point_currency_name", false);
	}
	
	public String getWebPOSPointCurrencyName(String pValue) {
		String returnValue = "";
		String kor1 = "";
		String kor2 = "";
		try {
			if (isEmpty(pValue)) {
				returnValue = webposXML.getfieldTransl("point_currency_name_5_more", false);
			} else {
				if (pValue.length() == 1) {
					kor1 = "0";
					kor2 = pValue;
				} else {
					kor1 = pValue.substring(pValue.length()-2,pValue.length()-1);
					kor2 = pValue.substring(pValue.length()-1,pValue.length());
				}
			}
			//System.out.println("pValue="+pValue+", kor1="+kor1+", kor2="+kor2);
			if (kor2=="1" && kor1!="1") {
				returnValue = webposXML.getfieldTransl("point_currency_name_1", false);
			} else if  ((kor2=="2" || kor2=="3" || kor2=="4") && (kor1!="1")) {
				returnValue = webposXML.getfieldTransl("point_currency_name_2_4", false);
			} else { 
				returnValue = webposXML.getfieldTransl("point_currency_name_5_more", false);
			}
			/*double doubleValue = Double.parseDouble(pValue.replace(",", "."));
			//LOGGER.debug("doubleValue: " + doubleValue);
			if (doubleValue == Double.parseDouble("1")) {
				returnValue = webposXML.getfieldTransl("point_currency_name_1", false);
			} else {
				if (doubleValue > 0 && doubleValue < 5) {
					returnValue = webposXML.getfieldTransl("point_currency_name_2_4", false);
				} else {
					returnValue = webposXML.getfieldTransl("point_currency_name_5_more", false);
				}
			}*/
		} catch (Exception el) {
			LOGGER.debug("parseDouble Exception: " + el.toString());
			returnValue = "";
		}
		return returnValue;
	}
	
	public String getRobokassaPaymentLink (wpChequeObject oper) {
		String payLink = "";
		
		String nInvId = oper.getValue("ID_TELGR");
		String sDesc = oper.getValue("PAYMENT_DESCRIPTION");
		String sOutSum = oper.getValue("ROBOKASSA_OPR_SUM_FRMT").replace(",", ".");
		String sShpItem = oper.getValue("RRN");
		String sCulture = "RU";
		String sEmail = oper.getValue("EMAIL_NAT_PRS");
		String ExpirationDate = oper.getValue("EXPIRATION_DATE_PREPARED");
		String Encoding = "utf-8";
		String club = oper.getValue("ID_CLUB");
		
		String innerRobokassaLink1=bc.util.JndiUtils.readJndi("robokassa/component/url")+"/payment/generateUrl?id="+nInvId+"&amount="+sOutSum+"&shipItem="+sShpItem+"&club="+club+"&module="+moduleName;
		LOGGER.debug("innerRobokassaLink1="+innerRobokassaLink1);
		payLink 	= bc.util.RestUtils.getString(innerRobokassaLink1);
			
		if (!isEmpty(payLink)) {
			try {
				payLink 	= payLink + "&description="+URLDecoder.decode(sDesc, "UTF-8")+"&Culture="+sCulture+"&Email="+sEmail+"&ExpirationDate="+ExpirationDate+"&Encoding="+Encoding;
			} catch (Exception e) {
				payLink = "";
			}
		}	
		LOGGER.debug("payLink="+payLink);
		
		return payLink;
	}
	
	public String getRobokassaCheckPaymentResultLink (wpChequeObject oper) {
		String checkResult = "";
		
		String nInvId = oper.getValue("ID_TELGR");
		
		String innerRobokassaLink2=bc.util.JndiUtils.readJndi("robokassa/component/url")+"/payment/checkPayment?paymentId="+nInvId+"&module="+moduleName;
		LOGGER.debug("innerRobokassaLink2="+innerRobokassaLink2);
		checkResult 	= bc.util.RestUtils.getString(innerRobokassaLink2);
		LOGGER.debug("checkResult="+checkResult);
		
		return checkResult;
	}
	
	public String getAddButton(String pHyperLink, String pDivName, String pTitle) {
		StringBuilder html = new StringBuilder();
		html.append("<div onclick=\"ajaxpage('" + pHyperLink + "', '" + pDivName + "')\" class=\"div_button\">");
		html.append("<img width=\"30\" vspace=\"0\" hspace=\"0\" height=\"30\" align=\"top\" title=\"" + pTitle + "\" style=\"border: 0px;\" src=\"" + getThemePath() + "/images/add.png\">");
		html.append("</div>");

	return html.toString();
		
	}
}