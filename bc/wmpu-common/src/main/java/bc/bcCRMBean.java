package bc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import java.util.*;

import bc.connection.Connector;
import bc.objects.bcClubDirectoriesObject;
import bc.objects.bcClubShortObject;
import bc.service.bcDictionaryRecord;
import bc.service.bcFeautureParam;


// TODO !!! REFACTORING !!!
public class bcCRMBean extends bcBase {
	private static final Logger LOGGER = Logger.getLogger(bcCRMBean.class);

	public bcCRMBean() {
	}

	public String getDocumentsSubDirectory() {
		return "CLUB_" + loginUser.getParameterValue("CLUBIDENT") + "/documents/";
	}

	public String getEmailFilesSubDirectory() {
		return "CLUB_" + loginUser.getParameterValue("CLUBIDENT") + "/email/";
	}

	public String getBankStatementsSubDirectory() {
		return "CLUB_" + loginUser.getParameterValue("CLUBIDENT")
				+ "/bank_statements/";
	}

	public String getBankStatementsSubDirectory(String pIdClub) {
		return "CLUB_" + pIdClub + "/bank_statements/";
	}

	public String getCertificateSubDirectory() {
		return "CLUB_" + loginUser.getParameterValue("CLUBIDENT")
				+ "/certificates/";
	}

	public String getCertificateSubDirectory(String pIdClub) {
		return "CLUB_" + pIdClub + "/certificates/";
	}

	public String loginCode = "";
	// private static Connection con = null;

	private String moduleName = "crm";

	public boolean logIn(String pUsername, String pPassword, HttpServletRequest request) {
		return logIn(pUsername, pPassword, moduleName, request);
	}

	public boolean logIn(String pUsername, String pPassword, String pModuleName, HttpServletRequest request) {
		Connector.removeSessionId(sessionId);
		//LOGGER.debug("bcCRMBean.logIn: pUsername=" + pUsername);
		//LOGGER.debug("bcCRMBean.logIn: pPassword=" + pPassword);
		Connection con = null;
		String ip = getClientIpAddr(request);
		if (isEmpty(sessionId) || isEmpty(pUsername)) {
			con = Connector.getConnection(sessionId);
		} else {
			con = Connector.getConnection(pModuleName, pUsername, pPassword, sessionId, this.getLanguage(), ip, "-1");
		}
		if (con == null) {
			return false;
		}
		Connector.closeConnection(con);
		clearAllDictionaries();
		loginUser.getCurrentUserFeature();
		this.setIsLoged(true);
		this.setSessionLanguage();
		this.setSessionDateFormat();
		if (isEmpty(pModuleName) || "crm".equalsIgnoreCase(pModuleName)) {
			moduleName = "crm";
			this.setMenuHashMap();
		} else {
			moduleName = pModuleName;
		}
		header.setLanguage(this.getLanguage());
		header.setIdClub(this.getCurrentClubID());
		//loginUserParam.getCurntUserParamFeature();
		this.getLookups();
		return true;
	}

	public boolean logInCurrent(String pSessionId) {
		boolean l_return = true;
		setSessionId(pSessionId);
		header.setPamam();
		if (!checkUser()) {
			this.setIsLoged(false);
			l_return = false;
			this.clearLink();
		} else {
			if (lookups.size() == 0) {
				this.getLookups();
			}
		}
		LOGGER.debug("bcCRMBean.logInCurrent('" + pSessionId + "') = " + l_return);
		return l_return;
	}

	public String getLogOutScript(HttpServletRequest request) {
		StringBuilder result = new StringBuilder();
		String myHyperLink = "";
		HashMap<String,String> parameters=getUtfParameters(request.getQueryString());
		
		result.append(getLogOutScriptShort(request));
		if (isEmpty(result.toString())) {
			myHyperLink 	= request.getRequestURI() + "?";
			String action	= getDecodeParam(parameters.get("action")); 
			//LOGGER.debug("action="+action);
			if (!(isEmpty(request.getQueryString()) || "null".equalsIgnoreCase(request.getQueryString()))) {
				myHyperLink = myHyperLink + request.getQueryString().toString();
			}
			if (isEmpty(action)) {
				String linkCurrent = myHyperLink;
				if (linkCurrent.indexOf("&noCache") > 0) {
					linkCurrent = linkCurrent.substring(1, linkCurrent.indexOf("&noCache"));
				}
				String linkLast = localLink.getLastLink();
				if (linkLast.indexOf("&noCache") > 0) {
					linkLast = linkLast.substring(1, linkLast.indexOf("&noCache"));
				}
				//LOGGER.debug("linkCurrent="+linkCurrent);
				//LOGGER.debug("linkLast="+linkLast);
				if (myHyperLink.contains("goback=yes") || myHyperLink.contains("goforvard=yes")) {
					this.addLink(myHyperLink);
				} else if (!linkCurrent.equalsIgnoreCase(linkLast)) { 
					this.addLink(myHyperLink);
				}
			}
		}
		//LOGGER.debug(result.toString());
		return result.toString();
	}

	public String getLogOutScriptShort(HttpServletRequest request) {
		StringBuilder result = new StringBuilder();
		if (!this.logInCurrent(request.getSession().getId())) {
			result.append("<script type=\"text/javascript\">");
			result.append("ajaxpage('" + this.getCheckUserLineFull(request)
					+ "', 'div_document')");
			result.append("</script>");
		}
		return result.toString();
	}

	public void setMenuHashMap() {
		String currentMenu = "";
		String curLang = "";
		this.menuMap.clear();
		//LOGGER.debug("bcCRMBean.setMenuHashMap()");
		curLang = this.getSessionLanguage();

		currentMenu = this.getMenuHTML2(null);
		this.menuMap.put(curLang, currentMenu);
	}

	public String getMenuByKey(String pCurrentId) {
		//LOGGER.debug("bcCRMBean.getMenuByKey(" + pCurrentId + ")");
		return makeMenuHtml2("", pCurrentId);
	}

	public String getMenuHTML() {
		StringBuilder result = new StringBuilder();
		String generalDB = getGeneralDBScheme();
		Connection con = null;
		Statement st = null;
		PreparedStatement st2 = null;
		try {
			//LOGGER.debug("bcCRMBean.getMenuHTML()");
			this.loginUser.getCurrentUserFeature();
			String sql = "SELECT * FROM " + generalDB
					+ ".VC_USER_MENU_ALL WHERE id_menu_element_parent IS NULL"
					+ "    AND is_enable = 'Y' and is_visible = 'Y'";

			con = Connector.getConnection(sessionId);
			st = con.createStatement();

			ResultSet rs = st.executeQuery(sql);
			result.append("<ul id=\"treemenu2\" class=\"treeview\">");
			while (rs.next()) {
				result.append("<li><b>" + rs.getString("name_menu_element")
						+ "</b>");
				String id_menu = rs.getString("id_menu_element");
				String sql2 = "SELECT * " + "FROM " + generalDB
						+ ".VC_USER_MENU_ALL "
						+ "WHERE id_menu_element_parent = ? "
						+ "    AND is_enable = 'Y' and is_visible = 'Y'";
				st2 = con.prepareStatement(sql2);
				st2.setInt(1, Integer.parseInt(id_menu));
				ResultSet rs2 = st2.executeQuery();
				if (id_menu.equalsIgnoreCase(this.loginUser
						.getValue("LAST_ID_MENU_ELEMENT"))) {
					result.append("<ul rel=\"open\">");
				} else {
					result.append("<ul>");
				}
				int id = 0;
				while (rs2.next()) {
					id++;
					result.append("<LI id=\""
							+ id
							+ "\" onclick=\"markElement(this)\"><a href=frm_index_copy.jsp?id="
							+ rs2.getString("id_menu_element")
							+ " target = \"frm_data\"> "
							+ rs2.getString("name_menu_element") + "</a></LI>");
					// String category =
					// rs2.getString("RELATIVE_PATH")+rs2.getString("EXEC_FILE");
					// String categoryId = rs2.getString("ID_MENU_ELEMENT");
				}
				rs2.close();
				st2.close();
				result.append("</ul></li>");
				// Connector.closeConnection(con2);
			}
			result.append("</ul>"
					+ "<script type=\"text/javascript\"> ddtreemenu.createTree('treemenu2', false) </script>");
			rs.close();
			st.close();
		} catch (SQLException e) {
			result.append("<br><b>Erooor<b><br>" + e.toString());
			LOGGER.debug("bcCRMBean.getMenuHTML SQLException: " + e.toString());
		} catch (Exception e) {
			result.append("<br><b>Eroooor<b><br>" + e.toString());
			LOGGER.debug("bcCRMBean.getMenuHTML Exception: " + e.toString());
		} finally {
			try {
				if (st != null)
					st.close();
			} catch (SQLException w) {
				w.toString();
			}
			try {
				if (st2 != null)
					st2.close();
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
		} // finally
		//LOGGER.debug("bcCRMBean.getMenuHTML() END");

		return result.toString();
	}

	private ArrayList<String> idMenu = new ArrayList<String>();
	private Map<String, String> nameMenu = new HashMap<String, String>();
	private Map<String, String> fileName = new HashMap<String, String>();
	private Map<String, String> idParentMenu = new HashMap<String, String>();
	private Map<String, String> imgSrc = new HashMap<String, String>();
	private Map<String, String> hasChild = new HashMap<String, String>();
	private Map<String, String> bottomSeparator = new HashMap<String, String>();

	public String getMenuHTML2(String pParentId) {

		Connection con = null;
		StringBuilder result = new StringBuilder();
		Statement st = null;
		this.loginUser.getCurrentUserFeature();

		idMenu.clear();
		nameMenu.clear();
		idParentMenu.clear();
		imgSrc.clear();
		hasChild.clear();
		bottomSeparator.clear();

		try {
			//LOGGER.debug("bcCRMBean.getMenuHTML2()");
			String sql = " SELECT * " + "   FROM " + getGeneralDBScheme()
					+ ".VC_USER_MENU_ALL"
					+ "    WHERE cd_module_type = 'CRM' AND is_enable = 'Y' and is_visible = 'Y'";

			con = Connector.getConnection(sessionId);
			st = con.createStatement();

			ResultSet rs = st.executeQuery(sql);

			//LOGGER.debug("bcCRMBean.getMenuHTML2 step1:");
			while (rs.next()) {
				String id_menu_element = rs.getString("id_menu_element");
				String name_menu_element = rs.getString("name_menu_element");
				String id_menu_element_parent = rs
						.getString("id_menu_element_parent");
				String file_name = rs.getString("relative_path")
						+ rs.getString("exec_file") + ".jsp";
				String img_src = rs.getString("img_src");
				id_menu_element_parent = isEmpty(id_menu_element_parent)?"":id_menu_element_parent;
				img_src = isEmpty(img_src)?"":img_src;
				
				idMenu.add(id_menu_element);
				nameMenu.put(id_menu_element, name_menu_element);
				idParentMenu.put(id_menu_element, id_menu_element_parent);
				fileName.put(id_menu_element, file_name);
				imgSrc.put(id_menu_element, img_src);
				bottomSeparator.put(id_menu_element, rs.getString("bottom_separator"));
				//System.out.println("id_menu_element="+id_menu_element+", bottom_separator="+rs.getString("bottom_separator"));
			}
			rs.close();
			st.close();

			for (int i = 0; i <= idMenu.size() - 1; i++) {
				String idMenuElement = idMenu.get(i);
				if (idParentMenu.containsValue(idMenuElement)) {
					hasChild.put(idMenuElement, "Y");
				} else {
					hasChild.put(idMenuElement, "N");
				}
			}

		} catch (SQLException e) {
			result.append("<br><b>bcCRMBean.getMenuHTML2() SQLException:<b><br>"
					+ e.toString());
			LOGGER.debug("bcCRMBean.getMenuHTML2() SQLException: " + e.toString());
		} catch (Exception e) {
			result.append("<br><b>bcCRMBean.getMenuHTML2() Exception: <b><br>"
					+ e.toString());
			LOGGER.debug("bcCRMBean.getMenuHTML2() Exception: " + e.toString());
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
		} // finally
		//LOGGER.debug("bcCRMBean.getMenuHTML2() END");

		result.append(makeMenuHtml2("", ""));

		return result.toString();
	}

	private boolean isLastMenuElement = false;
	private String openedParentId = "";
	private String papkaName = "";

	public String makeMenuHtml(String pParentId, String pCurrentId) {
		StringBuilder result = new StringBuilder();
		StringBuilder resultFull = new StringBuilder();
		String idMenuElement = "";
		String lastId = "";
		if (isEmpty(pCurrentId)) {
			lastId = this.loginUser.getValue("LAST_ID_MENU_ELEMENT");
		} else {
			lastId = pCurrentId;
		}
		// LOGGER.debug("bcCRMBean.makeMenuHtml(), lastId = " + lastId);

		for (int i = 0; i <= idMenu.size() - 1; i++) {
			idMenuElement = idMenu.get(i);
			if (pParentId.equalsIgnoreCase(idParentMenu.get(idMenuElement))) {
				if ("Y".equalsIgnoreCase(hasChild.get(idMenuElement))) {

					StringBuilder tmp = new StringBuilder();

					tmp.append(makeMenuHtml(idMenuElement, pCurrentId));
					if (idMenuElement.equalsIgnoreCase(openedParentId)) {
						openedParentId = pParentId;
						papkaName = "../images/menu/papkaop.gif";
					} else {
						papkaName = "../images/menu/papka.gif";
					}
					result.append("<li>"
							+ "<b><a href=\"javascript:chhidElem(child_"
							+ idMenuElement + ", top_" + idMenuElement + ")\">"
							+ "<img src=\"" + papkaName + "\" name=\"top_"
							+ idMenuElement
							+ "\" width=\"16\" height=\"16\" border=\"0\">"
							+ nameMenu.get(idMenuElement) + "</a></b>\n");
					result.append(tmp.toString());
					// result.append("</li>");
					if (isEmpty(pParentId)) {
						isLastMenuElement = false;
						papkaName = "../images/menu/papka.gif";
						openedParentId = "";
					}

				} else {
					if (idMenuElement.equalsIgnoreCase(lastId)) {
						isLastMenuElement = true;
						papkaName = "../images/menu/papkaop.gif";
						openedParentId = pParentId;
					} else {
						papkaName = "../images/menu/papka.gif";
					}
					result.append("<LI id=\"" + idMenuElement
							+ "\" onclick=\"markElement(this)\">");
					result.append("<div class=\"div_menu_element\" onclick=\"ajaxpage('"
							+ "../"
							+ fileName.get(idMenuElement)
							+ "?id="
							+ idMenuElement + "', 'div_main')\">");
					String img_src = imgSrc.get(idMenuElement);
					if (!isEmpty(img_src)) {
						result.append("<img src=\"../"
								+ img_src
								+ "\" width=\"16\" height=\"16\" border=\"0\">");
					}
					result.append(nameMenu.get(idMenuElement) + "</div>");
				}
			}
		}
		if (isEmpty(pParentId)) {
			resultFull.append("<ul id=\"treemenu2\" class=\"treeview\">\n");
		} else {
			if (isLastMenuElement && openedParentId.equalsIgnoreCase(pParentId)) {
				resultFull.append("<ul id=\"child_" + pParentId
						+ "\" rel=\"open\" class=\"expelem\">\n");
				openedParentId = pParentId;
			} else {
				resultFull.append("<ul id=\"child_" + pParentId
						+ "\" class=\"colelem\">\n");
			}
		}
		resultFull.append(result.toString());
		resultFull.append("</ul>");
		// LOGGER.debug("bcCRMBean.makeMenuHtml() END, pParentId = " + pParentId);

		return resultFull.toString();
	}

	public String makeMenuHtml2(String pParentId, String pCurrentId) {
		StringBuilder result = new StringBuilder();
		StringBuilder resultFull = new StringBuilder();
		String idMenuElement = "";
		String lastId = "";
		if (isEmpty(pCurrentId)) {
			lastId = this.loginUser.getValue("LAST_ID_MENU_ELEMENT");
		} else {
			lastId = pCurrentId;
		}
		// LOGGER.debug("bcCRMBean.makeMenuHtml(), lastId = " + lastId);

		for (int i = 0; i <= idMenu.size() - 1; i++) {
			idMenuElement = idMenu.get(i);
			if (pParentId.equalsIgnoreCase(idParentMenu.get(idMenuElement))) {
				//System.out.println("idMenuElement["+idMenuElement+"]="+idMenuElement+", bottomSeparator["+idMenuElement+"]="+bottomSeparator.get(idMenuElement));
				if ("Y".equalsIgnoreCase(bottomSeparator.get(idMenuElement))) {
					result.append("<li class=\"separator\"></li>");
					//System.out.println("bottomSeparator["+idMenuElement+"]="+bottomSeparator.get(idMenuElement));
				}
				if ("Y".equalsIgnoreCase(hasChild.get(idMenuElement))) {

					StringBuilder tmp = new StringBuilder();

					tmp.append(makeMenuHtml2(idMenuElement, pCurrentId));
					if (idMenuElement.equalsIgnoreCase(openedParentId)) {
						openedParentId = pParentId;
						papkaName = "../images/menu/papka.gif";
					} else {
						papkaName = "../images/menu/papka.gif";
					}
					result.append("<li>" +
							//+ "<b>"
							//"<a href=\"javascript:chhidElem(child_"
							//+ idMenuElement + ", top_" + idMenuElement + ")\">"
							//+ "<img src=\"" + papkaName + "\" name=\"top_"
							//+ idMenuElement
							//+ "\" width=\"16\" height=\"16\" border=\"0\">" +
							nameMenu.get(idMenuElement) + 
							//"</a>" +
							//"</b>" +
							"\n");
					result.append(tmp.toString());
					// result.append("</li>");
					if (isEmpty(pParentId)) {
						isLastMenuElement = false;
						papkaName = "../images/menu/papka.gif";
						openedParentId = "";
					}

				} else {
					if (idMenuElement.equalsIgnoreCase(lastId)) {
						isLastMenuElement = true;
						papkaName = "../images/menu/papka.gif";
						openedParentId = pParentId;
					} else {
						papkaName = "../images/menu/papka.gif";
					}
					result.append("<LI id=\"" + idMenuElement + "\">");
					result.append("<span onclick=\"ajaxpage('"
							+ "../"
							+ fileName.get(idMenuElement)
							+ "?id="
							+ idMenuElement + "', 'div_main')\">");
					String img_src = imgSrc.get(idMenuElement);
					if (!isEmpty(img_src)) {
						result.append("<img src=\"../"
								+ img_src
								+ "\" width=\"16\" height=\"16\" border=\"0\">");
					}
					result.append(nameMenu.get(idMenuElement) + "</span>");
				}
			}
		}
		if (isEmpty(pParentId)) {
			// resultFull.append("<ul id=\"treemenu2\">\n");
		} else {
			if (isLastMenuElement && openedParentId.equalsIgnoreCase(pParentId)) {
				resultFull.append("<ul id=\"child_" + pParentId + "\">\n");
				openedParentId = pParentId;
			} else {
				resultFull.append("<ul id=\"child_" + pParentId + "\">\n");
			}
		}
		resultFull.append(result.toString());
		
		if (isEmpty(pParentId)) {
			// resultFull.append("<ul id=\"treemenu2\">\n");
		} else {
			resultFull.append("</ul>");
		}
		// resultFull.append("</ul>");
		// LOGGER.debug("bcCRMBean.makeMenuHtml() END, pParentId = " + pParentId);

		return resultFull.toString();
	}


	public String getTopFrameCSS() {

		StringBuilder html = new StringBuilder();
		html.append("<link rel=\"stylesheet\" type=\"text/css\" href=\"../crm/CSS/topframe.css\">\n");

		return html.toString();
	}

	public String getBottomFrameCSS() {

		StringBuilder html = new StringBuilder();
		html.append("<link rel=\"stylesheet\" type=\"text/css\" href=\"../crm/CSS/tablebottom.css\">\n");

		return html.toString();
	}



	public String getGoToJurPrsHyperLink(String pId) {
		return getGoToHyperLink("CLIENTS_YURPERSONS", pId, "../crm/clients/yurpersonspecs.jsp?id=" + pId);
	}
	
	public String getGoToServicePlaceLink (String pId) {
		return getGoToHyperLink("CLIENTS_YURPERSONS", pId, "../crm/clients/yurpersonspecs.jsp?id=" + pId);
	}
	
	public String getGoToLogisticPromoterLink (String pId) {
		return getGoToHyperLink("LOGISTIC_CLIENTS_PROMOTERS", pId, "../crm/logistic/clients/promoterspecs.jsp?id=" + pId);
	}
	
	public String getGoToContactPersonLink (String pId) {
		return getGoToHyperLink("CLIENTS_CONTACT_PRS", pId, "../crm/clients/contact_prsspecs.jsp?id=" + pId);
	}
	
	public String getGoToLoyalityLink (String pId) {
		return getGoToHyperLink("CLIENTS_LOY", pId, "../crm/clients/loyspecs.jsp?id=" + pId);
	}
	
	public String getGoToBankAccountLink (String pId) {
		return getGoToHyperLink("CLIENTS_BANK_ACCOUNTS", pId, "../crm/clients/accountspecs.jsp?id=" + pId);
	}
	
	public String getGoToLoyalitySheduleLink (String pId) {
		return getGoToHyperLink("CLIENTS_SHEDULE", pId, "../crm/clients/shedulespecs.jsp?id=" + pId);
	}
	
	public String getGoToClubLink (String pId) {
		return getGoToHyperLink("CLUB_CLUB", pId, "../crm/club/clubspecs.jsp?id=" + pId);
	}
	
	public String getGoToCardPackageLink (String pId) {
		return getGoToHyperLink("CRM_CLUB_CARD_PACKAGE", pId, "../crm/club/card_packagespecs.jsp?id=" + pId);
	}
	
	public String getGoToTerminalLink (String pId) {
		return getGoToHyperLink("CLIENTS_TERMINALS", pId, "../crm/clients/terminalspecs.jsp?id=" + pId);
	}
	
	public String getGoToTerminalCertificateLink (String pId) {
		return getGoToHyperLink("CLIENTS_CERTIFICATE", pId, "../crm/clients/certificatespecs.jsp?id_profile=C&id_cert=" + pId);
	}
	
	public String getGoToTerminalDeviceTypeLink (String pId) {
		return getGoToHyperLink("CLIENTS_CERTIFICATE", pId, "../crm/clients/certificatespecs.jsp?id_profile=C&id_cert=" + pId);
	}
	
	public String getGoToClubEventLink (String pId) {
		return getGoToHyperLink("CLUB_EVENT_EVENT", pId, "../crm/club_event/clubeventspecs.jsp?id=" + pId);
	}
	
	public String getGoToClubEventGivenPlaceLink (String pId) {
		return getGoToHyperLink("CLUB_ACTIONS_GIVEN_PLACE", pId, "../crm/club_event/given_placespecs.jsp?id=" + pId);
	}
	
	public String getGoToClubEventGiftLink (String pId) {
		return getGoToHyperLink("CLUB_EVENT_GIFTS", pId, "../crm/club_event/giftspecs.jsp?id=" + pId);
	}
	
	public String getGoToDispatchSMSLink (String pId) {
		return getGoToHyperLink("DISPATCH_MESSAGES_SMS", pId, "../crm/dispatch/messages/smsspecs.jsp?id=" + pId);
	}
	
	public String getGoToDispatchMessagePatternLink (String pId) {
		return getGoToHyperLink("DISPATCH_MESSAGES_PATTERN", pId, "../crm/dispatch/messages/patternspecs.jsp?id=" + pId);
	}
	
	public String getGoToDispatchEmailLink (String pId) {
		return getGoToHyperLink("DISPATCH_MESSAGES_EMAIL", pId, "../crm/dispatch/messages/email_messagesspecs.jsp?id=" + pId);
	}
	
	public String getGoToDispatchEmailReceiverLink (String pId) {
		return getGoToHyperLink("DISPATCH_MESSAGES_EMAIL", pId, "../crm/dispatch/messages/email_messagereceiverspecs.jsp?id=" + pId);
	}
	
	public String getGoToDispatchTermMessageLink (String pId) {
		return getGoToHyperLink("DISPATCH_MESSAGES_TERM", pId, "../crm/dispatch/messages/term_messagesspecs.jsp?id=" + pId);
	}
	public String getGoToDispatchSMSProfileLink (String pId) {
		return getGoToHyperLink("DISPATCH_SETTINGS_SMS_PROFILE", pId, "../crm/dispatch/settings/sms_profilespecs.jsp?id=" + pId);
	}
	
	public String getGoToDispatchClientPatternLink (String pId) {
		return getGoToHyperLink("DISPATCH_PATTERNS_CLIENT", pId, "../crm/dispatch/patterns/client_patternspecs.jsp?id=" + pId);
	}
	
	public String getGoToDispatchEmailProfileLink (String pId) {
		return getGoToHyperLink("DISPATCH_SETTINGS_EMAIL_PROFILE", pId, "../crm/dispatch/settings/email_profilespecs.jsp?id=" + pId);
	}
	
	public String getGoToClubEventWarehouseLink (String pId) {
		return getGoToHyperLink("CLUB_EVENT_WAREHOUSE", pId, "../crm/club_event/warehousespecs.jsp?id=" + pId);
	}
	
	public String getGoToClubEventWarehouseGiftLink (String pId) {
		return getGoToHyperLink("CLUB_EVENT_WAREHOUSE", pId, "../crm/club_event/warehousegiftspecs.jsp?id=" + pId);
	}
	
	public String getGoToClubEventRequestLink (String pId) {
		return getGoToHyperLink("CLUB_EVENT_REQUEST", pId, "../crm/club_event/requestspecs.jsp?id=" + pId);
	}
	
	public String getGoToNatPrsLink (String pId) {
		return getGoToHyperLink("CLIENTS_NATPERSONS", pId, "../crm/clients/natpersonspecs.jsp?id=" + pId);
	}
	
	public String getGoToCardTaskLink (String pId) {
		return getGoToHyperLink("CARDS_CARD_TASKS", pId, "../crm/cards/card_taskspecs.jsp?id=" + pId);
	}
	
	public String getGoToReferralSchemeLink (String pId) {
		return getGoToHyperLink("CRM_CLUB_REFERRAL_SCHEME", pId, "../crm/club/referral_schemespecs.jsp?id=" + pId);
	}
	
	public String getGoToQuestionnaireLink (String pId) {
		return getGoToHyperLink("CARDS_QUESTIONNAIRE_IMPORT", pId, "../crm/cards/questionnaire_importspecs.jsp?id=" + pId);
	}
	
	public String getGoToQuestionnairePackLink (String pId) {
		return getGoToHyperLink("CARDS_QUESTIONNAIRE_PACK", pId, "../crm/cards/questionnaire_packspecs.jsp?id=" + pId);
	}
	
	public String getGoToClubCardLink (String pCardSerialNumber, String pIssuer, String pIdPaymentSystem) {
		return getGoToHyperLink(
				"CARDS_CLUBCARDS", 
				pCardSerialNumber, 
				"../crm/cards/clubcardspecs.jsp?id=" + pCardSerialNumber + "&iss=" + pIssuer + "&paysys=" + pIdPaymentSystem);
	}
	
	public String getGoToClubCardPurseLink (String pId) {
		return getGoToHyperLink("CARDS_CLUBCARDS", pId, "../crm/cards/clubcardpursespecs.jsp?id=" + pId);
	}
	
	public String getGoToCallCenterInquirerLink (String pId) {
		return getGoToHyperLink("CALL_CENTER_INQUIRER", pId, "../crm/call_center/inquirerspecs.jsp?id=" + pId);
	}
	
	public String getGoToCallCenterSettingLink (String pId) {
		return getGoToHyperLink("CALL_CENTER_SETTINGS", pId, "../crm/call_center/settingspecs.jsp?id=" + pId);
	}
	
	public String getGoToCallCenterQuestionLink (String pId) {
		return getGoToHyperLink("CALL_CENTER_QUESTIONS", pId, "../crm/call_center/questionspecs.jsp?id=" + pId);
	}
	
	public String getGoToTargetProgramLink (String pId) {
		return getGoToHyperLink("CLUB_TARGET_PROGRAM", pId, "../crm/club/target_programspecs.jsp?id=" + pId);
	}
	
	public String getGoToDocLink (String pId) {
		return getGoToHyperLink("CLUB_DOCUMENTS", pId, "../crm/club/documentspecs.jsp?id=" + pId);
	}
	
	public String getGoToCallCenterCallGroupLink (String pId) {
		return getGoToHyperLink("CALL_CENTER_CALL_GROUP", pId, "../crm/call_center/call_groupspecs.jsp?id=" + pId);
	}
	
	public String getGoToRelatioshipLink (String pId) {
		return getGoToHyperLink("CLUB_RELATIONSHIP", pId, "../crm/club/relationshipspecs.jsp?id=" + pId);
	}
	
	public String getGoToCallCenterFAQLink (String pId) {
		return getGoToHyperLink("CALL_CENTER_FAQ", pId, "../crm/call_center/faqspecs.jsp?id=" + pId);
	}
	
	public String getGoToTransactionLink (String pId) {
		return getGoToHyperLink("CARDS_TRANSACTIONS", pId, "../crm/cards/transactionspecs.jsp?id=" + pId);
	}
	
	public String getGoToTermSessionLink (String pId) {
		return getGoToHyperLink("CLIENTS_TERMSES", pId, "../crm/clients/termsespecs.jsp?id=" + pId);
	}
	
	public String getGoToTelegramLink (String pId) {
		return getGoToHyperLink("CLIENTS_TERMSES", pId, "../crm/clients/telegramspecs.jsp?id=" + pId);
	}
	
	public String getGoToSAMLink (String pId) {
		return getGoToHyperLink("CLIENTS_SAM", pId, "../crm/clients/samspecs.jsp?id=" + pId);
	}
	
	public String getGoToCallCenterAdministrationLink (String pId) {
		return getGoToHyperLink("CALL_CENTER_ADMINISTRATION", pId, "../crm/call_center/administrationspecs.jsp?id=" + pId);
	}
	
	public String getGoToCurrencyLink (String pId) {
		return getGoToHyperLink("SETUP_CURRENCY", pId, "../crm/setup/currencyspecs.jsp?id=" + pId);
	}
	
	public String getGoToSystemUserLink (String pId) {
		return getGoToHyperLink("SECURITY_USERS", pId, "../crm/security/userspecs.jsp?id=" + pId);
	}
	
	public String getGoToFinanceAccountingDocLink (String pId) {
		return getGoToHyperLink("FINANCE_ACCOUNTING_DOC", pId, "../crm/finance/accounting_docspecs.jsp?id_type=DOCUMENTS&id_doc=" + pId);
	}
	
	public String getGoToFinanceBKAccountLink (String pId) {
		return getGoToHyperLink("FINANCE_BK_ACCOUNTS", pId, "../crm/finance/bk_accountspecs.jsp?id=" + pId);
	}
	
	public String getGoToFinanceBKSchemeLink (String pId) {
		return getGoToHyperLink("FINANCE_BK_SCHEME", pId, "../crm/finance/bk_scheme_linespecs.jsp?id=" + pId);
	}
	
	public String getGoToFinanceClearingLink (String pId) {
		return getGoToHyperLink("FINANCE_CLEARING", pId, "../crm/finance/clearingspecs.jsp?id=" + pId);
	}
	
	public String getGoToFinancePostingSchemeLink (String pId) {
		return getGoToHyperLink("FINANCE_POSTING_SCHEME", pId, "../crm/finance/posting_scheme_linespecs.jsp?id=" + pId);
	}
	
	public String getGoToFinanceBankStatementLink (String pId) {
		return getGoToHyperLink("FINANCE_BANKSTATEMENT", pId, "../crm/finance/bankstatementspecs.jsp?id=" + pId);
	}
	
	public String getGoToCardSettingLink (String pId, String pIdClub) {
		return getGoToHyperLink("CARDS_CARDSETTING", pId, "../crm/cards/cardsettingspecs.jsp?id=" + pId + "&id_club=" + pIdClub);
	}

	public String getLGTitle(String pLGType) {
		String lTitle = "";
		if ("BON_CARD".equalsIgnoreCase(pLGType)) {
			lTitle = logisticXML.getfieldTransl("cards", false);
		} else if ("QUESTIONNAIRE".equalsIgnoreCase(pLGType)) {
			lTitle = logisticXML.getfieldTransl(
					"questionnaires", false);
		} else if ("TERMINAL".equalsIgnoreCase(pLGType)) {
			lTitle = logisticXML.getfieldTransl("terminals",
					false);
		} else if ("SAM".equalsIgnoreCase(pLGType)) {
			lTitle = logisticXML.getfieldTransl("sam", false);
		} else if ("GIFT".equalsIgnoreCase(pLGType)) {
			lTitle = logisticXML.getfieldTransl("gifts", false);
		} else if ("OTHER".equalsIgnoreCase(pLGType)) {
			lTitle = logisticXML.getfieldTransl("others", false);
		}
		return lTitle;
	}

	public String getLGHyperLink(String pLGType, String pId) {
		String goToIssuerHyperLink = "";
		if ("BON_CARD".equalsIgnoreCase(pLGType)) {
			goToIssuerHyperLink = getGoToHyperLink("LOGISTIC_PARTNERS_CARDS",
					pId, "../crm/logistic/partners/cardspecs.jsp?id=" + pId);
		} else if ("QUESTIONNAIRE".equalsIgnoreCase(pLGType)) {
			goToIssuerHyperLink = getGoToHyperLink("LOGISTIC_PARTNERS_QUEST",
					pId, "../crm/logistic/partners/questspecs.jsp?id=" + pId);
		} else if ("TERMINAL".equalsIgnoreCase(pLGType)) {
			goToIssuerHyperLink = getGoToHyperLink("LOGISTIC_PARTNERS_TERMINALS", 
					pId, "../crm/logistic/partners/terminalspecs.jsp?id=" + pId);
		} else if ("SAM".equalsIgnoreCase(pLGType)) {
			goToIssuerHyperLink = getGoToHyperLink("LOGISTIC_PARTNERS_SAMS",
					pId, "../crm/logistic/partners/samspecs.jsp?id=" + pId);
		} else if ("OTHER".equalsIgnoreCase(pLGType)) {
			goToIssuerHyperLink = getGoToHyperLink("LOGISTIC_PARTNERS_OTHERS",
					pId, "../crm/logistic/partners/otherspecs.jsp?id=" + pId);
		}
		return goToIssuerHyperLink;
	}

	public String getReportImage() {
		String reportImage = "";
		String reportFormat = this.loginUser.getParameterValue("REPORT_FORMAT");

		if ("PDF".equalsIgnoreCase(reportFormat)) {
			reportImage = "../images/reports/iconPdf.png";
		} else if ("XLS".equalsIgnoreCase(reportFormat)) {
			reportImage = "../images/reports/iconExcel.png";
		} else if ("RTF".equalsIgnoreCase(reportFormat)) {
			reportImage = "../images/reports/iconRtf.png";
		} else if ("HTML".equalsIgnoreCase(reportFormat)) {
			reportImage = "../images/reports/iconHtml.png";
		} else {
			reportImage = "../images/oper/print2.png";
		}
		return reportImage;
	}
	
	private String getReporterLink () {
		return AppConst.appRoot + "/reports/Reporter?";
	}

	public String getReportHyperLink(String pCdReport, String pHyperLinkParam) {

		StringBuilder html = new StringBuilder();

		String reportId = this.getReportId(pCdReport);

		if (!isEmpty(reportId)) {
			html.append("<td width=\"20\">");
			String myHyperLink = "";
			if (!isEmpty(pHyperLinkParam)) {
				myHyperLink = "<a href=\"" + getReporterLink() + pHyperLinkParam
						+ "&REPORT_FORMAT="
						+ this.loginUser.getParameterValue("REPORT_FORMAT")
						+ "&REPORT_ID=" + reportId + "\" target=\"_blank\">";
			} else {
				myHyperLink = "<a href=\"" + getReporterLink() + "REPORT_FORMAT="
						+ this.loginUser.getParameterValue("REPORT_FORMAT")
						+ "&REPORT_ID=" + reportId + "\" target=\"_blank\">";
			}
			html.append(myHyperLink);
			html.append("<img vspace=\"0\" hspace=\"0\" src=\""
					+ this.getReportImage()
					+ "\" align=\"top\" style=\"border: 0px;\" title=\""
					+ this.buttonXML.getfieldTransl("button_report", false) + "\">");
			html.append("</a>");
			html.append("</td>");
		}

		return html.toString();
	}

	public String getReportHyperLinkTitle(String pCdReport, String pTitle,
			String pHyperLinkParam) {

		StringBuilder html = new StringBuilder();

		String reportId = this.getReportId(pCdReport);

		if (!isEmpty(reportId)) {
			html.append("<a href=\"" + getReporterLink() + pHyperLinkParam
					+ "&REPORT_FORMAT="
					+ this.loginUser.getParameterValue("REPORT_FORMAT")
					+ "&REPORT_ID=" + reportId
					+ "\" target=\"_blank\" title=\"" + pTitle + "\">");
			html.append("<img vspace=\"0\" hspace=\"0\" src=\""
					+ this.getReportImage()
					+ "\" align=\"top\" style=\"border: 0px;\">");
			html.append("</a>");
		}

		return html.toString();
	}

	// ========================================================================
	// Функции возвращают поля <OPTION> для экранных элементов <SELECT>
	// ========================================================================

	private int maxSelectOption = 10;


	/*public String getClubMemberTypeOptions(String id_name, boolean isNull) {
		return getSelectBodyFromQuery(
				" SELECT cd_module_type, name_module_type, exist_flag "
						+ "   FROM " + getGeneralDBScheme()
						+ ".vc_module_type_all "
						+ "  ORDER BY name_module_type", id_name, isNull,
				clubMemberType, false);
	}*/

	public String getIssuersListOptions(String id_issuer, boolean isNull) {
		ArrayList<bcFeautureParam> pParam = initParamArray();
		pParam.add(new bcFeautureParam("int", id_issuer));

		ArrayList<bcDictionaryRecord> pDict = new ArrayList<bcDictionaryRecord>();

		return getSelectBodyFromParamQuery(
				" SELECT s.id_payment_system, "
						+ "		 s.name_payment_system||' - '||a1.cd_issuer_in_telgr_hex name_payment_system, a1.exist_flag "
						+ "   FROM (SELECT a.id_issuer, a.id_payment_system, a.cd_issuer_in_telgr_hex "
						+ "           FROM "
						+ getGeneralDBScheme()
						+ ".vc_issuer_priv_all a "
						+ "          WHERE a.id_issuer = ? "
						+ ") a1 RIGHT JOIN "
						+ getGeneralDBScheme()
						+ ".vc_payment_system_all s "
						+ "        ON (a1.id_payment_system = s.id_payment_system)",
				pParam, "", "", isNull, pDict, false, true);
	}

	public String getSAMStatusOptions(String cd_status, boolean isNull) {
		return getSelectBodyFromQuery(
				" SELECT cd_sam_status, name_sam_status, exist_flag, "
						+ "        DECODE(cd_sam_status,"
						+ "               'U', 'style=\"color:green;font-weight:bold;\"',"
						+ "               'S', 'style=\"color:red;font-weight:bold;\"',"
						+ "               'B', 'style=\"color:red;font-weight:bold;\"',"
						+ "               'R', 'style=\"color:red;font-weight:bold;\"',"
						+ "               'F', 'style=\"color:blue;font-weight:bold;\"',"
						+ "		 		''" 
						+ "		 ) option_style "
						+ "   FROM "
						+ getGeneralDBScheme()
						+ ".vc_sam_status_all "
						+ "  ORDER BY DECODE(cd_sam_status,'U',1,'F',2,'S',3,4)",
				cd_status, isNull, SAMStatus, false);
	}

	public String getPaymentSystemOptions(String id_pay_system, boolean isNull) {
		return getSelectBodyFromQuery(
				" SELECT id_payment_system, name_payment_system, exist_flag "
						+ "   FROM " + getGeneralDBScheme()
						+ ".vc_payment_system_all"
						+ "  ORDER BY name_payment_system", id_pay_system,
				isNull, paymentSystem, false);
	}

	public String getEventTypeOptions(String cd_type, boolean isNull) {
		return getSelectBodyFromQuery(
				" SELECT cd_event_type, desc_event_type, exist_flag, "
						+ "        DECODE(cd_event_type,"
						+ "               'C', 'style=\"color:red;font-weight:bold;\"',"
						+ "               'E', 'style=\"color:red;font-weight:bold;\"',"
						+ "               'S', 'style=\"color:red;font-weight:bold;\"',"
						+ "               'M', 'style=\"color:green;\"',"
						+ "               'W', 'style=\"color:blue;font-weight:bold;\"',"
						+ "		 		''" + "		 ) option_style " + "   FROM "
						+ getGeneralDBScheme() + ".vc_sys_event_type_all"
						+ "  ORDER BY desc_event_type", cd_type, isNull,
				sysEventType, false);
	}

	public String getTermSesCardReqStateOptions(String id_state, boolean isNull) {
		return getSelectBodyFromQuery(
				" SELECT cd_term_ses_creq_state, name_term_ses_creq_state, exist_flag, "
						+ "        DECODE(cd_term_ses_creq_state,"
						+ "               -9, 'style=\"color:red;font-weight:bold;\"',"
						+ "               -1, 'style=\"color:blue;\"',"
						+ "               0, 'style=\"color:black;\"',"
						+ "               1, 'style=\"color:green;\"',"
						+ "               9, 'style=\"color:darkgreen;\"',"
						+ "		 		''" + "		 ) option_style " + "   FROM "
						+ getGeneralDBScheme() + ".vc_term_ses_creq_state_all"
						+ "  ORDER BY cd_term_ses_creq_state", id_state,
				isNull, termSesCreqState, false);
	}

	public String getTermSesDataStateOptions(String id_state, boolean isNull) {
		return getSelectBodyFromQuery(
				" SELECT cd_term_ses_data_state, name_term_ses_data_state, exist_flag, "
						+ "        DECODE(cd_term_ses_data_state,"
						+ "               -9, 'style=\"color:red;font-weight:bold;\"',"
						+ "               -1, 'style=\"color:blue;\"',"
						+ "               0, 'style=\"color:black;\"',"
						+ "               1, 'style=\"color:green;\"',"
						+ "               9, 'style=\"color:darkgreen;\"',"
						+ "		 		''" + "		 ) option_style " + "   FROM "
						+ getGeneralDBScheme() + ".vc_term_ses_data_state_all"
						+ "  ORDER BY cd_term_ses_data_state", id_state,
				isNull, termSesDataState, false);
	}

	public String getTermSesParamStateOptions(String id_state, boolean isNull) {
		return getSelectBodyFromQuery(
				" SELECT cd_term_ses_param_state, name_term_ses_param_state, exist_flag, "
						+ "        DECODE(cd_term_ses_param_state,"
						+ "               -9, 'style=\"color:red;font-weight:bold;\"',"
						+ "               -1, 'style=\"color:blue;\"',"
						+ "               0, 'style=\"color:black;\"',"
						+ "               1, 'style=\"color:green;\"',"
						+ "               9, 'style=\"color:darkgreen;\"',"
						+ "		 		''" + "		 ) option_style " + "   FROM "
						+ getGeneralDBScheme() + ".vc_term_ses_param_state_all"
						+ "  ORDER BY cd_term_ses_param_state", id_state,
				isNull, termSesParamState, false);
	}

	public String getTermSesMonStateOptions(String id_state, boolean isNull) {
		return getSelectBodyFromQuery(
				" SELECT cd_term_ses_mon_state, name_term_ses_mon_state, exist_flag, "
						+ "        DECODE(cd_term_ses_mon_state,"
						+ "               -9, 'style=\"color:red;font-weight:bold;\"',"
						+ "               -1, 'style=\"color:blue;\"',"
						+ "               0, 'style=\"color:black;\"',"
						+ "               1, 'style=\"color:green;\"',"
						+ "               9, 'style=\"color:darkgreen;\"',"
						+ "		 		''" + "		 ) option_style " + "   FROM "
						+ getGeneralDBScheme() + ".vc_term_ses_mon_state_all"
						+ "  ORDER BY cd_term_ses_mon_state", id_state, isNull,
				termSesParamState, false);
	}

	public String getSMSTypeOptions(String cd_type, boolean isNull) {
		return getSelectBodyFromQuery(
				" SELECT cd_sms_message_type, name_sms_message_type, exist_flag "
						+ "   FROM " + getGeneralDBScheme() + ".vc_ds_sms_type"
						+ "  ORDER BY name_sms_message_type", cd_type, isNull,
				dsSMSType, false);
	}

	/*
	 * public String getDispatchClientProfileTypeOptions(String cd_type, boolean
	 * isNull) { return getSelectBodyFromQuery(
	 * " SELECT cd_cl_profile_type, name_cl_profile_type " + "   FROM " +
	 * getGeneralDBScheme()+".vc_ds_cl_profile_type_all" +
	 * "  ORDER BY name_cl_profile_type", cd_type, isNull); }
	 */

	public String getDispatchClientSMSProfileOptions(String id_profile,
			boolean isNull) {
		return getSelectBodyFromQuery2(
				" SELECT id_sms_profile, name_sms_profile_full " + "   FROM "
						+ getGeneralDBScheme() + ".vc_ds_sms_profile_club_all"
						+ "  ORDER BY name_sms_profile", id_profile, isNull);
	}

	public String getDispatchClientSMSProfileExcludeSelectedOptions(
			String id_profile, boolean isNull) {
		return getSelectBodyFromQuery2(
				" SELECT id_sms_profile, name_sms_profile_full " + "   FROM "
						+ getGeneralDBScheme() + ".vc_ds_sms_profile_club_all"
						+ "  where id_sms_profile <> " + id_profile
						+ "  ORDER BY name_sms_profile", "", isNull);
	}

	public String getSMSStateOptions(String cd_state, boolean isNull) {
		return getSelectBodyFromQuery(
				" SELECT cd_sms_state, name_sms_state, exist_flag "
						+ "   FROM "
						+ getGeneralDBScheme()
						+ ".vc_ds_sms_state_all"
						+ "  ORDER BY DECODE(cd_sms_state, 'PREPARED', 1, 'NEW', 2, 3), name_sms_state",
				cd_state, isNull, dsSMSState, false);
	}

	public String getDispatchKindOptions(String cd_kind, boolean isNull) {
		return getSelectBodyFromQuery(
				" SELECT cd_dispatch_kind, name_dispatch_kind, exist_flag, "
						+ "        DECODE (cd_dispatch_kind,"
						+ "                'CLIENT', ' style=\"color: green;\"', "
						+ "                'PARTNER', ' style=\"color: blue;\"', "
						+ "                'SYSTEM', ' style=\"color: red;\"', "
						+ "                'TRAINING', ' style=\"color: darkgray;\"', "
						+ "                'LG_PROMOTER', ' style=\"color: black;\"', "
						+ "                'UNKNOWN', ' style=\"font-weight:bold;color: #FF0000;\"',"
						+ "                '' "
						+ "        ) option_style "
						+ "   FROM "
						+ getGeneralDBScheme()
						+ ".vc_ds_dispatch_kind_all"
						+ "  ORDER BY DECODE(cd_dispatch_kind, 'CLIENT', 2, 'PARTNER', 4, 'SYSTEM', 6, 'LG_PROMOTER', 8, 'TRAINING', 10, 20), name_dispatch_kind",
				cd_kind, isNull, dsDispatchKind, false);
	}

	public String getDispatchMessageOperationTypeOptions(String cd_type,
			boolean isNull) {
		return getSelectBodyFromQuery(
				" SELECT cd_ds_message_oper_type, name_ds_message_oper_type, exist_flag "
						+ "   FROM " + getGeneralDBScheme()
						+ ".vc_ds_message_oper_type_all"
						+ "  ORDER BY name_ds_message_oper_type", cd_type,
				isNull, dsMessageOperType, false);
	}

	public String getDSProfileStateOptions(String cd_state, boolean isNull) {
		return getSelectBodyFromQuery(
				" SELECT cd_profile_state, name_profile_state, exist_flag "
						+ "   FROM " + getGeneralDBScheme()
						+ ".vc_ds_profile_state_all"
						+ "  ORDER BY name_profile_state", cd_state, isNull,
				dsProfileState, false);
	}

	public String getCurrentUserRolesOptions(String pIdRole, boolean isNull) {
		return getSelectBodyFromQuery2(" SELECT id_role, name_role "
				+ "   FROM " + getGeneralDBScheme()
				+ ".vc_user_current_roles_all" + "  ORDER BY name_role",
				pIdRole, isNull);
	}

	public String getRolesOptions(String pIdRole, boolean isNull) {
		return getSelectBodyFromQuery2(" SELECT id_role, name_role "
				+ "   FROM " + getGeneralDBScheme() + ".vc_role_all"
				+ "  ORDER BY name_role", pIdRole, isNull);
	}

	public String getClubCardOperationTypeOptions(String cd_type, boolean isNull) {
		return getSelectBodyFromQuery(
				" SELECT cd_card_operation_type, name_card_operation_type, exist_flag, "
						+ "        DECODE (cd_card_operation_type,"
						+ "                'ADD_BON', ' style=\"color: green;font-weight:bold;\"', "
						+ "                'WRITE_OFF_BON', ' style=\"color: blue;font-weight:bold;\"', "
						+ "                'CHANGE_PARAM', ' style=\"color: brown;\"', "
						+ "                'BLOCK_CARD', ' style=\"color: red;font-weight:bold;\"', "
						+ "                'SEND_MESSAGE', ' style=\"color: black;\"', "
						+ "                'SET_CATEGORIES_ON_PERIOD', ' style=\"color: brown;\"', "
						+ "                'ADD_GOODS_TO_PURSE', ' style=\"color: green;\"', "
						+ "                'WRITE_OFF_GOODS_FROM_PURSE', ' style=\"color: blue;\"', "
						+ "                'MOVE_BON', ' style=\"color: #FF0000;\"',"
						+ "                '' "
						+ "        ) option_style "
						+ "   FROM " + getGeneralDBScheme()
						+ ".vc_card_operation_type"
						+ "  ORDER BY name_card_operation_type", cd_type,
				isNull, clubCardOperationType, false);
	}

	public String getClubCardOperationStateOptions(String cd_state, boolean isNull) {
		return getSelectBodyFromQuery(
				" SELECT cd_card_oper_state, name_card_oper_state, exist_flag, "
						+ "        DECODE (cd_card_oper_state,"
						+ "                'EXECUTED', ' style=\"color: green;font-weight:bold;\"', "
						+ "                'EXECUTED_PARTIALLY', ' style=\"color: green;\"', "
						+ "                'CARRIED_OUT', ' style=\"color: brown;font-weight:bold;\"', "
						+ "                'CARRIED_OUT_PARTIALLY', ' style=\"color: brown;\"', "
						+ "                'NEW', ' style=\"color: black;\"', "
						+ "                'CANCELED', ' style=\"color: gray;\"', "
						+ "                'ERROR', ' style=\"color: red;font-weight:bold;\"', "
						+ "                'PREPARED', ' style=\"color: blue\"',"
						+ "                '' "
						+ "        ) option_style "
						+ "   FROM " + getGeneralDBScheme()
						+ ".vc_card_oper_state_all"
						+ "  ORDER BY DECODE(cd_card_oper_state, 'PREPARED', 1, 'NEW', 2, 'CANCELED', 98, 'ERROR', 99, 10), name_card_oper_state", cd_state,
				isNull, clubCardOperationState, false);
	}

	public String getDSDispatchKindRadio(String pFieldName, String cd_kind) {
		return getRadioButtonsFromQuery(
				pFieldName,
				" SELECT cd_dispatch_kind, "
						+ "        DECODE(cd_dispatch_kind, "
						+ "               'UNKNOWN', '<b><font color=\"red\">'||name_dispatch_kind||'</font><b>', "
						+ "                name_dispatch_kind"
						+ "        ) name_dispatch_kind2, exist_flag "
						+ "   FROM "
						+ getGeneralDBScheme()
						+ ".vc_ds_dispatch_kind_all"
						+ "  ORDER BY DECODE(cd_dispatch_kind, 'UNKNOWN', 2, 1), name_dispatch_kind",
				cd_kind, messageXML, dsDispatchKind, false);
	}


	public String getClubCardOperationTypeRadio(String pFieldName,
			String cd_type) {
		return getRadioButtonsFromQuery(pFieldName,
				" SELECT cd_card_operation_type, name_card_operation_type, exist_flag "
						+ "   FROM " + getGeneralDBScheme()
						+ ".vc_card_operation_type"
						+ "  ORDER BY name_card_operation_type", cd_type,
				card_taskXML, clubCardOperationType, false);
	}

	public String getDispatchPatternTypeRadio(String pFieldName, String cd_type) {
		return getRadioButtonsFromQuery(
				pFieldName,
				" SELECT cd_pattern_type, name_pattern_type||' ('||name_dispatch_type||')' name_pattern_type, exist_flag "
						+ "   FROM "
						+ getGeneralDBScheme()
						+ ".vc_ds_pattern_type_all"
						+ "  ORDER BY name_pattern_type", cd_type, messageXML,
				dsPatternTypeAddition, false);
	}

	public String getRemittanceStateOptions(String cd_state, boolean isNull) {
		return getSelectBodyFromQuery(
				" SELECT cd_remittance_state, name_remittance_state, exist_flag "
						+ "   FROM " + getGeneralDBScheme()
						+ ".vc_remittance_state_all"
						+ "  ORDER BY name_remittance_state", cd_state, isNull,
				remittanceState, false);
	}

	public String getRemittanceTypeOptions(String cd_type, boolean isNull) {
		return getSelectBodyFromQuery(
				" SELECT cd_remittance_type, name_remittance_type, exist_flag "
						+ "   FROM " + getGeneralDBScheme()
						+ ".vc_remittance_type_all"
						+ "  ORDER BY name_remittance_type", cd_type, isNull,
				remittanceType, false);
	}

	public String getRemittanceTypeRadio(String pFieldName, String cd_type) {
		return getRadioButtonsFromQuery(pFieldName,
				" SELECT cd_remittance_type, name_remittance_type, exist_flag "
						+ "   FROM " + getGeneralDBScheme()
						+ ".vc_remittance_type_all"
						+ "  ORDER BY name_remittance_type", cd_type,
				remittanceXML, remittanceType, false);
	}

	public String getSAMTypeOptions(String cd_type, boolean isNull) {
		return getSelectBodyFromQuery(
				" SELECT cd_sam_type, name_sam_type, exist_flag " + "   FROM "
						+ getGeneralDBScheme() + ".vc_sam_type_all "
						+ "  ORDER BY DECODE(cd_sam_type,'M',1,2)", cd_type,
				isNull, SAMType, false);
	}

	public String getTerminalsListOptions(String id_terminal, boolean isNull) {
		return getSelectBodyFromQuery2(
				" SELECT id_term id_term1, id_term id_term2 " + "   FROM "
						+ getGeneralDBScheme() + ".vc_term_club_all "
						+ "  ORDER BY id_term", id_terminal, isNull);
	}

	public String getWarningStatusOptions(String cd_status, boolean isNull) {
		return getSelectBodyFromLookups("WARNING_STATUS", "CODE_MEANING",
				"NUMBER", cd_status, null, isNull, false);
	}

	public String getUsersListOptions(String id_user, boolean isNull) {
		return getSelectBodyFromQuery2(" SELECT id_user, name_user "
				+ "   FROM " + getGeneralDBScheme() + ".vc_users_all "
				+ "  ORDER BY UPPER(name_user)", id_user, isNull);
	}

	public String getRoleFreeUsersListOptions(String id_role, boolean isNull) {
		ArrayList<bcFeautureParam> pParam = initParamArray();
		pParam.add(new bcFeautureParam("int", id_role));

		return getSelectBodyFromParamQuery(" SELECT * "
				+ "   FROM (SELECT id_user, name_user " + "   		FROM "
				+ getGeneralDBScheme() + ".vc_users_all " + "  		   MINUS "
				+ " 		  SELECT id_user, name_user " + "   		FROM "
				+ getGeneralDBScheme() + ".vc_users_roles_all "
				+ "          WHERE id_role = ? "
				+ " ) ORDER BY UPPER(name_user)", pParam, "", "", isNull);
	}

	public String getClubRelationishipAllComissionOptions(String id_club_rel,
			String jurPrsTypeParty1, String jurPrsTypeParty2, boolean isNull) {
		ArrayList<bcFeautureParam> pParam = initParamArray();
		pParam.add(new bcFeautureParam("int", id_club_rel));
		pParam.add(new bcFeautureParam("int", jurPrsTypeParty1));
		pParam.add(new bcFeautureParam("int", jurPrsTypeParty2));
		pParam.add(new bcFeautureParam("int", jurPrsTypeParty1));
		pParam.add(new bcFeautureParam("int", jurPrsTypeParty2));

		return getSelectBodyFromParamQuery(
				" SELECT id_comission_type, name_comission_type "
						+ "   FROM "
						+ getGeneralDBScheme()
						+ ".vc_comission_type_club_all "
						+ "  WHERE cd_club_rel_type IN "
						+ "      (SELECT cd_club_rel_type "
						+ "         FROM "
						+ getGeneralDBScheme()
						+ ".vc_club_rel_club_all a "
						+ "        WHERE id_club_rel = ?)"
						+ "    AND ("
						+ "         (NVL(cd_jur_prs_type_payer, 'NULL') = NVL(?, 'NULL') AND "
						+ "          NVL(cd_jur_prs_type_receiver, 'NULL') = NVL(?, 'NULL')) "
						+ "         OR "
						+ "         (NVL(cd_jur_prs_type_receiver, 'NULL') = NVL(?, 'NULL') AND "
						+ "          NVL(cd_jur_prs_type_payer, 'NULL') = NVL(?, 'NULL')) "
						+ "        )" + " ORDER BY name_comission_type",
				pParam, "", "", isNull);
	}

	public String getTerminalClubRelationshipsOptions(String clubRelType,
			boolean isNull) {
		return getSelectBodyFromQuery(
				" SELECT cd_club_rel_type, name_club_rel_type "
						+ "   FROM "
						+ getGeneralDBScheme()
						+ ".vc_club_rel_type_all "
						+ "  WHERE cd_club_rel_type IN ('OPERATOR-TERMINAL_MANUFACTURER','OPERATOR-DEALER', "
						+ "			'OPERATOR-TECHNICAL_ACQUIRER', 'DEALER-TECHNICAL_ACQUIRER', "
						+ "           'OPERATOR-FINANCE_ACQUIRER', 'DEALER-FINANCE_ACQUIRER', "
						+ "           'OPERATOR-PARTNER')"
						+ " ORDER BY name_club_rel_type", clubRelType, isNull,
				clubRelType2, false);
	}

	public String getTermSessionTelgrListOptions(String id_session,
			String id_telgr) {
		return getSelectBodyFromQuery2(
				" SELECT id_telgr, cd_telgr_type " + "   FROM "
						+ getGeneralDBScheme() + ".vc_telgr_club_all "
						+ "  WHERE id_term_ses = " + id_session
						+ "  ORDER BY id_telgr", id_telgr, false);
	}

	public String getDTExchangeFilesOptions(String idFile, boolean isNull) {
		return getSelectBodyFromQuery2(
				" SELECT id_exchange_file, name_exchange_file " + "   FROM "
						+ getGeneralDBScheme()
						+ ".v_dt_exchange_file_club_all "
						+ "  ORDER BY name_exchange_file", idFile, isNull);
	}

	public String getNatPrsCardsOptions(String id_card, String id_nat_prs,
			boolean isNull) {
		ArrayList<bcFeautureParam> pParam = initParamArray();
		pParam.add(new bcFeautureParam("int", id_nat_prs));

		return getSelectBodyFromParamQuery(
				" SELECT card_serial_number||'_'||TO_CHAR(id_issuer)||'_'||TO_CHAR(id_payment_system) id_card, cd_card1 "
						+ "   FROM "
						+ getGeneralDBScheme()
						+ ".vc_card_all "
						+ "  WHERE id_nat_prs = ?"
						+ "  ORDER BY cd_card1", pParam, id_card, "", isNull);
	}

	public String getDTTasksOptions(String idTask, boolean isNull) {
		return getSelectBodyFromQuery2(
				" SELECT id_task, name_task_full name_task " + "   FROM "
						+ getGeneralDBScheme() + ".vc_dt_task_all "
						+ "  ORDER BY name_task ", idTask, isNull);
	}

	public String getDTMenuOptions(String idMenu, boolean isNull) {
		return getSelectBodyFromQuery2(" SELECT id_dt_menu, name_dt_menu "
				+ "   FROM " + getGeneralDBScheme() + ".vc_dt_menu "
				+ "  ORDER BY name_dt_menu", idMenu, isNull);
	}

	public String getDTTasksExcludeOptions(String id_task,
			String id_task_exclude, boolean isNull) {
		ArrayList<bcFeautureParam> pParam = initParamArray();
		pParam.add(new bcFeautureParam("int", id_task_exclude));

		return getSelectBodyFromParamQuery(" SELECT id_task, name_task_full "
				+ "   FROM " + getGeneralDBScheme() + ".vc_dt_task_all "
				+ "  WHERE id_task <> ?"
				+ "  ORDER BY RPAD(TO_CHAR(id_task),10,'0')", pParam, id_task,
				"", isNull);
	}

	public String getDTTermOptions(String idTerm, boolean isNull) {
		return getSelectBodyFromQuery2(" SELECT id_term, id_term name_term "
				+ "   FROM " + getGeneralDBScheme() + ".vc_dt_term_club_all "
				+ "  ORDER BY id_term", idTerm, isNull);
	}

	public String getDSEmailProfileOptions(String idEmailProfile, boolean isNull) {
		return getSelectBodyFromQuery2(
				" SELECT id_email_profile, name_email_profile " + "   FROM "
						+ getGeneralDBScheme()
						+ ".vc_ds_email_profile_club_all "
						+ "  ORDER BY name_email_profile", idEmailProfile,
				isNull);
	}

	public String getLogisticNotInSchedulePromotersOptions(String idLgSchedule,
			String idLgPromoter, boolean isNull) {
		ArrayList<bcFeautureParam> pParam = initParamArray();
		pParam.add(new bcFeautureParam("int", idLgSchedule));

		return getSelectBodyFromParamQuery(
				" SELECT id_lg_promoter, name_lg_promoter "
						+ "   FROM (SELECT id_lg_promoter, name_lg_promoter "
						+ "           FROM " + getGeneralDBScheme()
						+ ".vc_lg_promoter_club_all "
						+ "          WHERE cd_lg_promoter_state = 'WORKS' "
						+ "          MINUS "
						+ "         SELECT id_lg_promoter, name_lg_promoter "
						+ "           FROM " + getGeneralDBScheme()
						+ ".vc_lg_cc_given_schedule_ln_all "
						+ "          WHERE id_lg_cc_given_schedule = ?"
						+ "        )" + "  ORDER BY name_lg_promoter", pParam,
				idLgPromoter, "", isNull);
	}

	public String getClubCardFinanceOperationStateOptions(String cd_state,
			boolean isNull) {
		return getSelectBodyFromQuery(
				" SELECT cd_fn_card_oper_state, name_fn_card_oper_state "
						+ "   FROM " + getGeneralDBScheme()
						+ ".vc_fn_card_oper_state_all "
						+ "  ORDER BY name_fn_card_oper_state", cd_state,
				isNull, fnCardOperState, false);
	}

	public String getDistrictOptions(String cd_country, String id_oblast,
			String id_district, boolean isNull) {
		ArrayList<bcFeautureParam> pParam = initParamArray();

		String lCodeCountry = isEmpty(cd_country)?"-1":cd_country;
		String lIdOblast =  isEmpty(id_oblast)?"-1":id_oblast;
		
		pParam.add(new bcFeautureParam("string", lCodeCountry));
		pParam.add(new bcFeautureParam("int", lIdOblast));

		return getSelectBodyFromParamQuery(
				" SELECT id_district, name_district " + "   FROM "
						+ getGeneralDBScheme() + ".vc_district_all "
						+ "  WHERE cd_country = ? " + "    AND id_oblast = ? "
						+ "  ORDER BY name_district", pParam, id_district, "",
				isNull);
	}

	public String getCityOptions(String cd_country, String id_oblast,
			String id_district, String id_city, boolean isNull) {
		ArrayList<bcFeautureParam> pParam = initParamArray();

		String lCodeCountry = isEmpty(cd_country)?"-1":cd_country;
		String lIdOblast =  isEmpty(id_oblast)?"-1":id_oblast;
		String lIdDistrict =  isEmpty(id_district)?"-1":id_district;
		
		pParam.add(new bcFeautureParam("string", lCodeCountry));
		pParam.add(new bcFeautureParam("int", lIdOblast));
		pParam.add(new bcFeautureParam("int", lIdDistrict));

		return getSelectBodyFromParamQuery(" SELECT id_city, name_city "
				+ "   FROM " + getGeneralDBScheme() + ".vc_city_all "
				+ "  WHERE cd_country = ? " + "    AND id_oblast = ? "
				+ "    AND NVL(id_district,-1) = ? " + "  ORDER BY name_city",
				pParam, id_city, "", isNull);
	}

	public String getCityDistrictOptions(String cd_country, String id_oblast,
			String id_district, String id_city, String id_city_district,
			boolean isNull) {
		ArrayList<bcFeautureParam> pParam = initParamArray();

		String lCodeCountry = isEmpty(cd_country)?"-1":cd_country;
		String lIdOblast =  isEmpty(id_oblast)?"-1":id_oblast;
		String lIdDistrict =  isEmpty(id_district)?"-1":id_district;
		
		pParam.add(new bcFeautureParam("string", lCodeCountry));
		pParam.add(new bcFeautureParam("int", lIdOblast));
		pParam.add(new bcFeautureParam("int", lIdDistrict));

		String mySQL = " SELECT id_city_district, SUBSTR(name_city_district,1,30) name_city_district "
				+ "   FROM "
				+ getGeneralDBScheme()
				+ ".vc_city_district_all"
				+ "  WHERE cd_country = ? "
				+ "    AND id_oblast = ? "
				+ "    AND NVL(id_district,-1) = ? ";
		if (!isEmpty(id_city)) {
			mySQL = mySQL + " AND id_city = ? ";
			pParam.add(new bcFeautureParam("int", id_city));
		}
		mySQL = mySQL + " ORDER BY name_city_district";

		return getSelectBodyFromParamQuery(mySQL, pParam, id_city_district, "",
				isNull);
	}

	public String getTermMessageTypeOptions(String cd_type, boolean isNull) {
		return getSelectBodyFromQuery(
				" SELECT cd_term_message_type, name_term_message_type, exist_flag "
						+ "   FROM " + getGeneralDBScheme()
						+ ".vc_term_message_type_all "
						+ "  ORDER BY name_term_message_type", cd_type, isNull,
				termMessageType, false);
	}

	public String getTermMessageStateOptions(String cd_state, boolean isNull) {
		return getSelectBodyFromQuery(
				" SELECT cd_term_message_state, name_term_message_state, exist_flag "
						+ "   FROM " + getGeneralDBScheme()
						+ ".vc_term_message_state_all "
						+ "  ORDER BY name_term_message_state", cd_state,
				isNull, termMessageState, false);
	}

	public String getBKOperationSchemeOptions(String id_scheme, boolean isNull) {
		return getSelectBodyFromQuery2(
				" SELECT id_fn_posting_scheme, desc_fn_posting_scheme "
						+ "   FROM " + getGeneralDBScheme()
						+ ".vc_fn_posting_scheme_club_all "
						+ "  ORDER BY desc_fn_posting_scheme", id_scheme,
				isNull);
	}

	public String getFNPostingSchemeStateOptions(String cd_state, boolean isNull) {
		return getSelectBodyFromQuery(
				" SELECT cd_fn_posting_scheme_state, name_fn_posting_scheme_state, exist_flag "
						+ "   FROM " + getGeneralDBScheme()
						+ ".vc_fn_posting_scheme_state_all "
						+ "  ORDER BY name_fn_posting_scheme_state", cd_state,
				isNull, fnPostingSchemeState, false);
	}

	public String getMessagePatternTypeWitoutTerminals(String lookup_code,
			boolean isNull) {
		String[] pExcludeValues = new String[1];
		pExcludeValues[0] = "TERMINALS";
		return getSelectBodyFromLookups("MESSAGE_PATTERN_TYPE", "CODE_MEANING",
				"CODE", pExcludeValues, null, lookup_code, null, isNull, false);
	}

	public String getRemittanceTypeOptions(String from_to, String type,
			boolean isNull) {
		if ("FROM".equalsIgnoreCase(from_to)) {
			return getSelectBodyFromLookups("REMITTANCE_TYPE", "CODE_MEANING",
					"CODE", null, null, type, null, isNull, false);
		} else if ("FROM_CARD".equalsIgnoreCase(from_to)) {
			String[] pOnlyValues = new String[3];
			pOnlyValues[0] = "CARD_WAS_LOST";
			pOnlyValues[1] = "CARD_TO_EXCLUDE";
			pOnlyValues[2] = "CARD_WORKED";
			return getSelectBodyFromLookups("REMITTANCE_TYPE", "CODE_MEANING",
					"CODE", null, pOnlyValues, type, null, isNull, false);
		} else {
			String[] pOnlyValues = new String[3];
			pOnlyValues[0] = "CARD_WAS_LOST";
			pOnlyValues[1] = "CARD_TO_EXCLUDE";
			return getSelectBodyFromLookups("REMITTANCE_TYPE", "CODE_MEANING",
					"CODE", null, pOnlyValues, type, null, isNull, false);
		}
	}

	public String getClearingBankAccountOwnerOptions(String pIdClearing,
			String pIdOwner, boolean isNull) {
		ArrayList<bcFeautureParam> pParam = initParamArray();
		pParam.add(new bcFeautureParam("int", pIdClearing));
		pParam.add(new bcFeautureParam("int", pIdClearing));

		return getSelectBodyFromParamQuery(
				" SELECT id_owner, sname_owner "
						+ "   FROM (SELECT receiver_id_owner_ba id_owner, receiver_sname_owner_ba sname_owner "
						+ "           FROM "
						+ getGeneralDBScheme()
						+ ".vc_clearing_lines_club_all "
						+ "          WHERE id_clearing = ? "
						+ " 		   UNION "
						+ "         SELECT payer_id_owner_ba id_owner, payer_sname_owner_ba sname_owner "
						+ "           FROM " + getGeneralDBScheme()
						+ ".vc_clearing_lines_club_all "
						+ "          WHERE id_clearing = ? " + "		 ) "
						+ " ORDER BY sname_owner ", pParam, pIdOwner, "",
				isNull);
	}

	public String getDictionaryGroupOptions(String lookup_code, boolean isNull) {
		return getSelectBodyFromLookups("DICTIONARY_GROUP", "CODE_MEANING",
				"MEANING", lookup_code, null, isNull, false);
	}

	public String getTermUserStatusOptions(String cd_status, boolean isNull) {
		return getSelectBodyFromQuery(
				" SELECT cd_user_status, name_user_status, exist_flag," +
				"        DECODE(cd_user_status, " +
				"               'BLOCKED', 'style=\"color:red;font-weight:bold;\"', " +
				"               'CLOSED', 'style=\"color:red;font-weight:bold;\"', " +
				"               'DELETED', 'style=\"color:red;font-weight:bold;\"', " +
				"               'OPENED', 'style=\"color:green;font-weight:bold;\"', " +
				"               'NEED_CHANGE_PASSWORD', 'style=\"color:blue;font-weight:bold;\"', " +
				"               ''" +
				"  		) option_style "
						+ "   FROM " + getGeneralDBScheme()
						+ ".vc_user_status_all"
						+ "  ORDER BY name_user_status", cd_status,
				isNull, termUserStatus, false);
	}

	public String getFinanceInvoiceStateOptions(String cd_state, boolean isNull) {
		return getSelectBodyFromQuery(
				" SELECT cd_fn_invoice_state, name_fn_invoice_state, exist_flag,"
					+ "        DECODE(cd_fn_invoice_state, "
					+ "               'PAID', 'style=\"color:green;font-weight:bold;\"', "
					+ "               'ISNT_PAID', 'style=\"color:red;font-weight:bold;\"', "
					+ "               'CANCELLED', 'style=\"color:blue;font-weight:bold;\"', "
					+ "               ''"
					+ "  		) option_style "
					+ "   FROM " + getGeneralDBScheme()
					+ ".vc_fn_invoice_state_all"
					+ "  ORDER BY DECODE(cd_fn_invoice_state, 'ISNT_PAID', 1, 'PAID', 2, 'CANCELLED', 3, 9), name_fn_invoice_state", cd_state,
				isNull, fnInvoiceState, false);
	}

	public String getFinanceInvoiceCreationOptions(String cd_creation, boolean isNull) {
		return getSelectBodyFromQuery(
				" SELECT cd_fn_invoice_creation, name_fn_invoice_creation, exist_flag,"
					+ "        DECODE(cd_fn_invoice_creation, "
					+ "               'AUTOMATIC', 'style=\"color:blue;font-weight:bold;\"', "
					+ "               'MANUAL', 'style=\"color:green;font-weight:bold;\"', "
					+ "               ''"
					+ "  		) option_style "
					+ "   FROM " + getGeneralDBScheme()
					+ ".vc_fn_invoice_creation_all"
					+ "  ORDER BY DECODE(cd_fn_invoice_creation, 'AUTOMATIC', 1, 'MANUAL', 2, 9), name_fn_invoice_creation", cd_creation,
				isNull, fnInvoiceCreation, false);
	}

	public String getTermUserAccessTypeOptions(String cd_type, boolean isNull) {
		return getSelectBodyFromQuery(
				" SELECT cd_term_user_access_type, name_term_user_access_type, exist_flag "
						+ "   FROM " + getGeneralDBScheme()
						+ ".vc_term_user_access_type_all"
						+ "  ORDER BY name_term_user_access_type", cd_type, isNull,
						termUserAccessType, false);
	}

	public String getTermUserAccessTypeOptions(String pTermType, String cd_type, boolean isNull) {
		String mySQL = 
			" SELECT cd_term_user_access_type, name_term_user_access_type, exist_flag "
			+ "   FROM " + getGeneralDBScheme()
			+ ".vc_term_user_access_type_all";
		if (!isEmpty(pTermType)) {
			if ("PHYSICAL".equalsIgnoreCase(pTermType)) {
				mySQL = mySQL + " WHERE NVL(is_physical_term_access_type, 'N') = 'Y' ";
			} else if ("WEBPOS".equalsIgnoreCase(pTermType)) {
				mySQL = mySQL + " WHERE NVL(is_webpos_term_access_type, 'N') = 'Y' ";
			}
		}
		mySQL = mySQL + "  ORDER BY name_term_user_access_type";
		return getSelectBodyFromQuery2(mySQL, cd_type, isNull, false);
	}

	public String getNatPrsGroupsListOptions(String cd_group, boolean isNull) {
		return getSelectBodyFromQuery(
				" SELECT cd_nat_prs_group, name_nat_prs_group, exist_flag "
						+ "   FROM " + getGeneralDBScheme()
						+ ".vc_nat_prs_group_all"
						+ "  ORDER BY name_nat_prs_group", cd_group, isNull,
				natPrsGroup, false);
	}

	public String getDSPforileStateListOptions(String cd_state, boolean isNull) {
		return getSelectBodyFromQuery(
				" SELECT cd_profile_state, name_profile_state, exist_flag "
						+ "   FROM " + getGeneralDBScheme()
						+ ".vc_ds_profile_state_all"
						+ "  ORDER BY name_profile_state", cd_state, isNull,
				dsProfileState, false);
	}

	public String getClubListOptions(String id_club, boolean isNull) {
		return getSelectBodyFromQuery2(" SELECT id_club, sname_club "
				+ "   FROM " + getGeneralDBScheme() + ".vc_club_info"
				+ "  ORDER BY sname_club", id_club, isNull);
	}

	public String getClubFundOperTypeOptions(String cd_type, boolean isNull) {
		return getSelectBodyFromQuery2(" SELECT cd_club_fund_oper_type, name_club_fund_oper_type, exist_flag, "
				+ "        DECODE(cd_club_fund_oper_type, "
				+ "               'PAYMENT', 'style=\"color:green;font-weight:bold;\"', "
				+ "               'WRITE_OFF', 'style=\"color:blue;font-weight:bold;\"', " 
				+ "               ''" 
				+ "  		) option_style "
				+ "   FROM " + getGeneralDBScheme() + ".vc_club_fund_oper_type_all"
				+ "  ORDER BY name_club_fund_oper_type", cd_type, isNull);
	}

	public String getClubFundPaymentKindOptions(String cd_kind, boolean isNull) {
		return getSelectBodyFromQuery2(" SELECT cd_club_fund_payment_kind, name_club_fund_payment_kind, exist_flag "
				+ "   FROM " + getGeneralDBScheme() + ".vc_club_fund_payment_kind_all"
				+ "  ORDER BY name_club_fund_payment_kind", cd_kind, isNull);
	}

	public String getClubFundPaymentKindOptions(String id_club_fund, String cd_kind, boolean isNull) {
		if (isEmpty(id_club_fund)) {
			return getClubFundPaymentKindOptions (cd_kind, isNull);
		} else {
			return getSelectBodyFromQuery2(" SELECT cd_club_fund_payment_kind, name_club_fund_payment_kind, exist_flag "
				+ "   FROM " + getGeneralDBScheme() + ".vc_club_fund_cf_pay_kind_all "
				+ "  WHERE id_club_fund = " + id_club_fund
				+ "  ORDER BY name_club_fund_payment_kind", cd_kind, isNull);
		}
	}

	public String getCallCenterFAQCategoryOptions(String id_category,
			boolean isNull) {
		return getSelectBodyFromQuery(
				" SELECT id_cc_faq_category, name_cc_faq_category_frmt, exist_flag "
						+ "   FROM " + getGeneralDBScheme()
						+ ".vc_cc_faq_category_all "
						+ "  ORDER BY name_cc_faq_category_frmt ", id_category,
				isNull, ccFAQCategory, false);
	}

	public String getCallCenterCallGroupStateOptions(String cd_state,
			boolean isNull) {
		return getSelectBodyFromQuery(
				" SELECT cd_cc_call_group_state, name_cc_call_group_state, exist_flag "
						+ "   FROM "
						+ getGeneralDBScheme()
						+ ".vc_cc_call_group_state_all "
						+ "  ORDER BY DECODE(cd_cc_call_group_state, 'NEW', 1, 'IN_PROCESS', 2, 'EXECUTED', 3, 'CANCEL', 4, 5), name_cc_call_group_state ",
				cd_state, isNull, ccCallGroupState, false);
	}

	public String getCallCenterInquirerLineTypeOptions(String cd_type,
			boolean isNull) {
		return getSelectBodyFromQuery(
				" SELECT cd_cc_inquirer_line_type, name_cc_inquirer_line_type, exist_flag "
						+ "   FROM "
						+ getGeneralDBScheme()
						+ ".vc_cc_inquirer_line_type_all "
						+ "  ORDER BY DECODE(cd_cc_inquirer_line_type, 'QUESTION', 1, 2), name_cc_inquirer_line_type ",
				cd_type, isNull, ccInquirerLineType, false);
	}

	public String getCallCenterInquirerStateOptions(String cd_state,
			boolean isNull) {
		return getSelectBodyFromQuery(
				" SELECT cd_cc_inquirer_state, name_cc_inquirer_state, exist_flag "
						+ "   FROM "
						+ getGeneralDBScheme()
						+ ".vc_cc_inquirer_state_all "
						+ "  ORDER BY DECODE(cd_cc_inquirer_state, 'CONSTUCTION', 1, 'APPROVE', 2, 'CANCEL', 3, 4), name_cc_inquirer_state ",
				cd_state, isNull, ccInquirerState, false);
	}

	public String getCardOnlinePaymentTypeOptions(String cd_state,
			boolean isNull) {
		return getSelectBodyFromQuery2(
				" SELECT id_club_online_pay_type, name_club_online_pay_type||' ('||term_card_req_club_pay_id_def||')' name_club_online_pay_type, exist_flag "
						+ "   FROM "
						+ getGeneralDBScheme()
						+ ".vc_club_online_pay_type_cl_all "
						+ "  ORDER BY name_club_online_pay_type||' ('||term_card_req_club_pay_id_def||')' ",
				cd_state, isNull, false);
	}

	public String getCallCenterSettingStateOptions(String cd_state,
			boolean isNull) {
		return getSelectBodyFromQuery(
				" SELECT cd_cc_setting_state, name_cc_setting_state, exist_flag "
						+ "   FROM " + getGeneralDBScheme()
						+ ".vc_cc_setting_state_all "
						+ "  ORDER BY name_cc_setting_state ", cd_state,
				isNull, ccSettingState, false);
	}

	public String getCallCenterUserStatusOptions(String id_status,
			boolean isNull) {
		return getSelectBodyFromQuery(
				" SELECT cd_cc_user_status, name_cc_user_status, exist_flag "
						+ "   FROM " + getGeneralDBScheme()
						+ ".vc_cc_user_status_all "
						+ "  ORDER BY name_cc_user_status", id_status, isNull,
				ccUserStatus, false);
	}

	public String getCallCenterUserQuestionTypeOptions(String id_user,
			boolean isNull) {
		if (isEmpty(id_user)) {
			return "";
		} else {
			ArrayList<bcFeautureParam> pParam = initParamArray();
			pParam.add(new bcFeautureParam("int", id_user));

			return getSelectBodyFromParamQuery(
					" SELECT cd_cc_question_type, name_cc_question_type "
							+ "   FROM " + getGeneralDBScheme()
							+ ".vc_cc_user_question_type_all "
							+ "  WHERE id_user = ? "
							+ "  ORDER BY name_cc_question_type", pParam, "",
					"", isNull);
		}
	}

	public String getCallCenterUserQuestionTypeHidden(String id_user) {
		StringBuilder return_value = new StringBuilder();
		Connection con = null;
		ArrayList<bcFeautureParam> pParam = initParamArray();
		pParam.add(new bcFeautureParam("int", id_user));

		String mySQL = " SELECT cd_cc_question_type, name_cc_question_type "
				+ "   FROM " + getGeneralDBScheme()
				+ ".vc_cc_user_question_type_all " + "  WHERE id_user = ? "
				+ "  ORDER BY name_cc_question_type";
		PreparedStatement st = null;
		try {
			LOGGER.debug(prepareSQLToLog(mySQL, pParam));
			con = Connector.getConnection(sessionId);
			st = con.prepareStatement(mySQL);
			st = prepareParam(st, pParam);
			ResultSet rset = st.executeQuery();

			while (rset.next()) {
				return_value.append("<input type=\"hidden\" value=\""
						+ rset.getString(1) + "\" name=\"p_"
						+ rset.getString(1) + "\" id=\"p_" + rset.getString(1)
						+ "\">");
			}
		} catch (SQLException e) {
			LOGGER.debug("bcCRMBean.getCallCenterUserQuestionTypeOptions SQLException: "
					+ e.toString());
			return_value.append("<option value=\"\">SQL Error</option>");
		} catch (Exception el) {
			LOGGER.debug("bcCRMBean.getCallCenterUserQuestionTypeOptions Exception: "
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
		} // finally
		return return_value.toString();
	}

	public String getCallCenterContactTypeOptions(String cd_contact_type,
			boolean isNull) {
		return getSelectBodyFromQuery(
				" SELECT cd_cc_contact_type, name_cc_contact_type, exist_flag "
						+ "   FROM " + getGeneralDBScheme()
						+ ".vc_cc_contact_type_all "
						+ "  ORDER BY name_cc_contact_type", cd_contact_type,
				isNull, ccContactType, false);
	}

	public String getCallCenterQuestionImportantOptions(
			String cd_question_important, boolean isNull) {
		return getSelectBodyFromQuery(
				" SELECT cd_cc_question_important, name_cc_question_important, exist_flag "
						+ "   FROM " + getGeneralDBScheme()
						+ ".vc_cc_question_important_all "
						+ "  ORDER BY name_cc_question_important",
				cd_question_important, isNull, ccQuestionImpotant, false);
	}
	
	public String getClubMemberStatusOptions(String cd_status, boolean isNull) {
		return getSelectBodyFromQuery(
				" SELECT cd_club_member_status, name_club_member_status, exist_flag, "
						+ "        DECODE(cd_club_member_status, "
						+ "               'IN_PROCESS', 'style=\"color:blue;\"', "
						+ "               'PARTICIPATE', 'style=\"color:green;\"', " 
						+ "               'CLOSED', 'style=\"color:red;font-weight:bold;\"', " 
						+ "               'OUT_OF_CLUB', 'style=\"color:gray;\"', " 
						+ "               ''" 
						+ "  		) option_style "
						+ "   FROM " + getGeneralDBScheme()
						+ ".vc_club_member_status_all "
						+ " WHERE cd_club_member_status <> 'SERVICE_PLACE' "
						+ "  ORDER BY DECODE(cd_club_member_status, 'IN_PROCESS', 1, 'PARTICIPATE', 2, 'CLOSED', 5, 'OUT_OF_CLUB', 6, 99), name_club_member_status", cd_status,
				isNull, clubMemberStatus, false);
	}
	
	public String getClubMemberTypeOptions(String cd_type, boolean isNull) {
		return getSelectBodyFromQuery(
				" SELECT cd_club_member_type, name_club_member_type, exist_flag, "
						+ "        DECODE(cd_club_member_type, "
						+ "               'REGISTRATOR', 'style=\"color:black;font-weight:bold;\"', " 
						+ "               'COORDINATOR', 'style=\"color:green;font-weight:bold;\"', " 
						+ "               'CURATOR', 'style=\"color:green;font-weight:bold;\"', " 
						+ "               'SHAREHOLDER', 'style=\"color:green;font-weight:bold;\"', " 
						+ "               'SERVICE_PLACE', 'style=\"color:blue;\"', " 
						+ "               'DEALER', 'style=\"color:blue;font-weight:bold;\"', " 
						+ "               'OPERATOR', 'style=\"color:red;font-weight:bold;\"', " 
						+ "               'TERMINAL_MANUFACTURER', 'style=\"color:gray;\"', " 
						+ "               'BANK', 'style=\"color:gray;\"', " 
						+ "               'AGENT', 'style=\"color:red;font-weight:bold;\"', " 
						+ "               'ISSUER', 'style=\"color:gray;\"', " 
						+ "               'FIN_ACQUIRER', 'style=\"color:gray;\"', " 
						+ "               'TECH_ACQUIRER', 'style=\"color:gray;\"', " 
						+ "               ''" 
						+ "  		) option_style "
						+ "   FROM " + getGeneralDBScheme()
						+ ".vc_club_member_type_all "
						+ "  ORDER BY DECODE(cd_club_member_type, 'OPERATOR', 1, 'AGENT', 2, 'DEALER', 3, 'SHAREHOLDER', 11, 'REGISTRATOR', 12, 'COORDINATOR', 13, 'CURATOR', 14, 'SERVICE_PLACE', 15, 'OUTSIDE', 50, 99), name_club_member_type", cd_type,
				isNull, clubMemberType, false);
	}

	public String getFNOperTypeOptions(String cd_type, boolean isNull) {
		return getSelectBodyFromQuery(
				" SELECT cd_fn_oper_type, full_name_fn_oper_type, exist_flag "
						+ "   FROM " + getGeneralDBScheme()
						+ ".vc_fn_oper_type_all "
						+ "  ORDER BY full_name_fn_oper_type", cd_type, isNull,
				fnOperType, false);
	}

	public String getFNOperExecTypeOptions(String cd_type, boolean isNull) {
		return getSelectBodyFromQuery(
				" SELECT cd_fn_oper_exec_type, name_fn_oper_exec_type, exist_flag "
						+ "   FROM " + getGeneralDBScheme()
						+ ".vc_fn_oper_exec_type_all "
						+ "  ORDER BY name_fn_oper_exec_type", cd_type, isNull,
				fnOperExecType, false);
	}

	public String getFNOperStateOptions(String cd_state, boolean isNull) {
		return getSelectBodyFromQuery(
				" SELECT cd_fn_oper_state, name_fn_oper_state, exist_flag "
						+ "   FROM " + getGeneralDBScheme()
						+ ".vc_fn_oper_state_all "
						+ "  ORDER BY name_fn_oper_state", cd_state, isNull,
				fnOperState, false);
	}

	public String getBKOperationTypeOptions(String cd_type, boolean isNull) {
		return getSelectBodyFromSizedQuery(
				" SELECT cd_bk_operation_type, name_bk_operation_type, exist_flag "
						+ "   FROM " + getGeneralDBScheme()
						+ ".vc_bk_operation_type "
						+ "  ORDER BY name_bk_operation_type", cd_type, "100",
				isNull, bkOperationType, false);
	}

	public String getBKOperationTypeShortOptions(String cd_type, boolean isNull) {
		return getSelectBodyFromSizedQuery(
				" SELECT cd_bk_operation_type, name_bk_operation_type, exist_flag "
						+ "   FROM " + getGeneralDBScheme()
						+ ".vc_bk_operation_type "
						+ "  ORDER BY name_bk_operation_type", cd_type, "60",
				isNull, bkOperationType, false);
	}

	public String getBKDocTypeOptions(String cd_type, boolean isNull) {
		return getSelectBodyFromQuery(
				" SELECT cd_bk_doc_type, name_bk_doc_type, exist_flag "
						+ "   FROM " + getGeneralDBScheme()
						+ ".vc_bk_doc_type " + "  ORDER BY name_bk_doc_type",
				cd_type, isNull, bkDocType, false);
	}

	public String getDSSMSDeliveryStatusOptions(String id_status, boolean isNull) {
		return getSelectBodyFromQuery(
				" SELECT id_deliv_status, name_deliv_status, exist_flag "
						+ "   FROM " + getGeneralDBScheme()
						+ ".vc_ds_sms_deliv_status "
						+ "  ORDER BY name_deliv_status", id_status, isNull,
				dsSMSDelivStatus, false);
	}

	public String getDSSMSSendStatusOptions(String id_status, boolean isNull) {
		return getSelectBodyFromQuery(
				" SELECT id_send_status, name_send_status, exist_flag "
						+ "   FROM " + getGeneralDBScheme()
						+ ".vc_ds_sms_send_status "
						+ "  ORDER BY name_send_status", id_status, isNull,
				dsSMSSendStatus, false);
	}

	public String getBKOperationTypeConditionOptions(String cd_type,
			String cd_condition, boolean isNull) {
		ArrayList<bcFeautureParam> pParam = initParamArray();
		pParam.add(new bcFeautureParam("string", cd_type));

		return getSelectBodyFromParamQuery(
				" SELECT cd_bk_condition, name_bk_condition " + "   FROM "
						+ getGeneralDBScheme() + ".vc_bk_operation_type_cond "
						+ "  WHERE cd_bk_operation_type = ? "
						+ "  ORDER BY name_bk_condition", pParam, cd_condition,
				"", isNull);
	}

	public String getBKOperationTypeAccountParticipantOptions(String cd_type,
			String cd_participant, boolean isNull) {
		ArrayList<bcFeautureParam> pParam = initParamArray();
		pParam.add(new bcFeautureParam("string", cd_type));

		return getSelectBodyFromParamQuery(
				" SELECT cd_bk_account_participant, name_bk_account_participant "
						+ "   FROM " + getGeneralDBScheme()
						+ ".vc_bk_operation_type_part "
						+ "  WHERE cd_bk_operation_type = ? "
						+ "  ORDER BY name_bk_account_participant", pParam,
				cd_participant, "", isNull);
	}

	public String getCallCenterQuestionStatusOptions(String cd_question_status,
			boolean isNull) {
		return getSelectBodyFromQuery(
				" SELECT cd_cc_question_status, name_cc_question_status, exist_flag"
						+ "   FROM " + getGeneralDBScheme()
						+ ".vc_cc_question_status_all "
						+ "  ORDER BY name_cc_question_status",
				cd_question_status, isNull, ccQuestionStatus, false);
	}

	public String getCallCenterQuestionTypeOptions(String pType, boolean isNull) {
		String mySQL = " SELECT cd_cc_question_type, name_cc_question_type, exist_flag "
				+ "   FROM "
				+ getGeneralDBScheme()
				+ ".vc_cc_question_type_all "
				+ "  ORDER BY name_cc_question_type";
		return getSelectBodyFromQuery(mySQL, pType, isNull, ccQuestionType,
				false);
	}

	public String getCallCenterQuestionTypeUserOptions(String pNoneUser,
			boolean isNull) {
		if (!isEmpty(pNoneUser)) {
			ArrayList<bcFeautureParam> pParam = initParamArray();
			pParam.add(new bcFeautureParam("string", pNoneUser));

			String mySQL = " SELECT cd_cc_question_type, name_cc_question_type "
					+ "   FROM "
					+ getGeneralDBScheme()
					+ ".vc_cc_question_type_all "
					+ "  ORDER BY name_cc_question_type"
					+ " MINUS "
					+ " SELECT cd_cc_question_type, name_cc_question_type "
					+ "   FROM "
					+ getGeneralDBScheme()
					+ ".vc_cc_user_question_type_all "
					+ "  WHERE id_user = ? "
					+ "  ORDER BY name_cc_question_type";
			return getSelectBodyFromParamQuery(mySQL, pParam, "", "", isNull);
		} else {
			String mySQL = " SELECT cd_cc_question_type, name_cc_question_type "
					+ "   FROM "
					+ getGeneralDBScheme()
					+ ".vc_cc_question_type_all "
					+ "  ORDER BY name_cc_question_type";
			return getSelectBodyFromQuery2(mySQL, "", isNull);
		}
	}

	public String getCallCenterQuestionUrgentOptions(String cd_question_urgent,
			boolean isNull) {
		return getSelectBodyFromQuery(
				" SELECT cd_cc_question_urgent, name_cc_question_urgent, exist_flag "
						+ "   FROM " + getGeneralDBScheme()
						+ ".vc_cc_question_urgent_all "
						+ "  ORDER BY name_cc_question_urgent",
				cd_question_urgent, isNull, ccQuestionUrgent, false);
	}

	public String getTermStatusOptions(String pFieldName, String pCdTermStatus, String pNameTermStatus, boolean pIsNull, boolean pReadOnly, String pWidth) {
		
		StringBuilder html = new StringBuilder();
		if (pReadOnly) {
			String pName = pNameTermStatus;
			if (isEmpty(pNameTermStatus) && !isEmpty(pCdTermStatus)) {
				pName = getTermStatusName(pCdTermStatus);
			}
			html.append(getInputTextElement(pFieldName, "", pName, pReadOnly, "150"));
		} else {
			html.append("<select name=\"" + pFieldName + "\" class=\"inputfield\">");
			html.append(getSelectBodyFromQuery(
				" SELECT cd_term_status, name_term_status, exist_flag, "
						+ "        DECODE(cd_term_status,"
						+ "               'ACTIVE', 'style=\"color:green;font-weight:bold;\"',"
						+ "               'SETTING', 'style=\"color:blue;font-weight:bold;\"',"
						+ "               'EXCLUDED', 'style=\"color:gray;font-weight:bold;\"',"
						+ "               'BLOCKED', 'style=\"color:red;font-weight:bold;\"',"
						+ "		 		''" + "		 ) option_style "
						+ "   FROM " + getGeneralDBScheme()
						+ ".vc_term_status_names "
						+ "  ORDER BY cd_term_status", pCdTermStatus, pReadOnly,
				termStatus, false));
			html.append("</select>");
		}
		return html.toString();
	}

	public String getTermStatusOptions(String pCdTermStatus, boolean pIsNull) {
		
		return getSelectBodyFromQuery(
				" SELECT cd_term_status, name_term_status, exist_flag, "
						+ "        DECODE(cd_term_status,"
						+ "               'ACTIVE', 'style=\"color:green;font-weight:bold;\"',"
						+ "               'SETTING', 'style=\"color:blue;font-weight:bold;\"',"
						+ "               'EXCLUDED', 'style=\"color:gray;font-weight:bold;\"',"
						+ "               'BLOCKED', 'style=\"color:red;font-weight:bold;\"',"
						+ "		 		''" + "		 ) option_style "
						+ "   FROM " + getGeneralDBScheme()
						+ ".vc_term_status_names "
						+ "  ORDER BY cd_term_status", pCdTermStatus, pIsNull,
				termStatus, false);
	}

	public String getTermTypeOptions(String cd_type, boolean isNull) {
		return getSelectBodyFromQuery(
				" SELECT cd_term_type, name_term_type, exist_flag, "
						+ "        DECODE(cd_term_type,"
						+ "               'PHYSICAL', 'style=\"color:green;\"',"
						+ "               'WEBPOS', 'style=\"color:blue;\"',"
						+ "		 		''" + "		 ) option_style "
						+ "   FROM " + getGeneralDBScheme()
						+ ".vc_term_type_names " + "  ORDER BY cd_term_type",
						cd_type, isNull, termType, false);
	}

	public String getTermTypeRadio(String pFieldName, String cd_type) {
		return getRadioButtonsFromQuery(
				pFieldName,
				" SELECT cd_term_type, name_term_type, exist_flag, "
					+ "        DECODE(cd_term_type,"
					+ "               'PHYSICAL', 'style=\"color:green;\"',"
					+ "               'WEBPOS', 'style=\"color:blue;\"',"
					+ "		 		''" + "		 ) option_style "
					+ "   FROM " + getGeneralDBScheme()
					+ ".vc_term_type_names " + "  ORDER BY cd_term_type",
						cd_type, terminalXML, termType, false);
	}

	public String getLGCatalogOptions(String id_lg_production, boolean isNull) {
		return getSelectBodyFromQuery2(
				" SELECT id_lg_production, name_lg_production " + "   FROM "
						+ getGeneralDBScheme() + ".vc_lg_catalog_club_all "
						+ "  ORDER BY name_lg_production", id_lg_production,
				isNull);
	}

	public String getLGTypeOptions(String cd_lg_type, boolean isNull) {
		return getSelectBodyFromQuery(
				" SELECT cd_lg_type, name_lg_type, exist_flag " + "   FROM "
						+ getGeneralDBScheme() + ".vc_lg_type_all "
						+ "  ORDER BY name_lg_type", cd_lg_type, isNull,
				lgType, false);
	}

	public String getLoyalityTermTypeOptions(String id_name, boolean isNull) {
		return getSelectBodyFromQuery(" SELECT cd_term_type, name_term_type "
				+ "   FROM " + getGeneralDBScheme() + ".vc_term_type_names "
				+ "  WHERE cd_term_type in ('PHYSICAL','WEBPOS')"
				+ "  ORDER BY cd_term_type", id_name, isNull,
				termTypeForLoyality, false);
	}

	public String getTargetPrgPayPeriodOptions(String cd_period, boolean isNull) {
		return getSelectBodyFromQuery(
				" SELECT cd_target_prg_pay_period, name_target_prg_pay_period, exist_flag, "
						+ "        DECODE(cd_target_prg_pay_period,"
						+ "               'STUDY_COUNT', 'style=\"color:blue;font-weight:bold;\"',"
						+ "               'MONTH', 'style=\"color:green;font-weight:bold;\"',"
						+ "               'WEEK', 'style=\"color:green;\"',"
						+ "               'DAY', 'style=\"color:green;\"',"
						+ "               'HOUR', 'style=\"color:green;\"',"
						+ "               'IRREGULAR', 'style=\"color:red;font-weight:bold;\"',"
						+ "		 		''" + "		 ) option_style "
						+ "   FROM " + getGeneralDBScheme()
						+ ".vc_target_prg_pay_period_all "
						+ "  ORDER BY DECODE(cd_target_prg_pay_period, 'STUDY_COUNT', 1, 'MONTH', 2, 'WEEK', 3, 'DAY', 4, 'HOUR', 5, 'IRREGULAR', 9, 6), name_target_prg_pay_period", cd_period,
				isNull, targetPrgPayPeriod, false);
	}

	public String getTaxValueTypeOptions(String cd_tax_value, boolean isNull) {
		return getSelectBodyFromQuery(
				" SELECT cd_tax_value_type, name_tax_value_type, exist_flag "
						+ "   FROM " + getGeneralDBScheme()
						+ ".vc_tax_value_type_all "
						+ "  ORDER BY name_tax_value_type", cd_tax_value,
				isNull, taxValueType, false);
	}

	public String getTaxOptions(String cd_tax, boolean isNull) {
		return getSelectBodyFromQuery2(
				" SELECT cd_tax, name_tax " + "   FROM " + getGeneralDBScheme()
						+ ".vc_tax_all " + "  ORDER BY name_tax", cd_tax,
				isNull);
	}

	public String getBKPhaseListOptions(String cd_phase, boolean isNull) {
		return getSelectBodyFromQuery(
				" SELECT cd_phase, name_phase, exist_flag " + "   FROM "
						+ getGeneralDBScheme() + ".vc_bk_operation_phase "
						+ "  WHERE exist_flag='Y' " + "  ORDER BY name_phase",
				cd_phase, isNull, bkOperationPhase, false);
	}

	public String getLoyalityKindOptions(String cd_kind, boolean isNull) {
		return getSelectBodyFromQuery(
				" SELECT cd_kind_loyality, cd_kind_loyality||' - '||name_kind_loyality, exist_flag "
						+ "   FROM "
						+ getGeneralDBScheme()
						+ ".vc_loyality_kind_names "
						+ "  ORDER BY cd_kind_loyality", cd_kind, isNull,
				loyKindFull, false);
	}

	public String getBKAccountSchemeOptions(String id_acc_scheme, boolean isNull) {
		return getSelectBodyFromSizedQuery(
				" SELECT id_bk_account_scheme, desc_bk_account_scheme "
						+ "   FROM " + getGeneralDBScheme()
						+ ".vc_bk_account_scheme_club_all "
						+ "  ORDER BY desc_bk_account_scheme", id_acc_scheme,
				"50", isNull, false);
	}

	public String getLoyalityKindOnlyCDOptions(String cd_kind, boolean isNull) {
		return getSelectBodyFromQuery(
				" SELECT cd_kind_loyality, cd_kind_loyality name_kind_loyality, exist_flag "
						+ "   FROM " + getGeneralDBScheme()
						+ ".vc_loyality_kind_names "
						+ "  ORDER BY cd_kind_loyality", cd_kind, isNull,
				loyKindCodeOnly, false);
	}


	public String getPostingSettingsOptions(String id_scheme, boolean isNull) {
		return getSelectBodyFromSizedQuery(
				" SELECT id_bk_account_scheme_line, name_bk_account_scheme_line "
						+ "   FROM (SELECT id_bk_account_scheme_line, cd_bk_account_scheme_line||' '||name_bk_account_scheme_line name_bk_account_scheme_line "
						+ "           FROM " + getGeneralDBScheme()
						+ ".vc_bk_account_sh_ln_club_all) "
						+ "  ORDER BY name_bk_account_scheme_line", id_scheme,
				"50", isNull, false);
	}

	public String getPostingSettingsGroupOptions(String id_scheme,
			boolean isNull) {
		return getSelectBodyFromSizedQuery(
				" SELECT id_bk_account_scheme_line, name_bk_account_scheme_line "
						+ "   FROM (SELECT id_bk_account_scheme_line, cd_bk_account_scheme_line||' '||name_bk_account_scheme_line name_bk_account_scheme_line "
						+ "           FROM " + getGeneralDBScheme()
						+ ".vc_bk_account_sh_ln_club_all "
						+ "          WHERE exist_flag='Y' "
						+ "            AND is_group = 'Y') "
						+ "  ORDER BY name_bk_account_scheme_line", id_scheme,
				"50", isNull, false);
	}

	public String getPostingSettingsCodeOptions(String id_scheme, boolean isNull) {
		return getSelectBodyFromQuery2(
				" SELECT id_bk_account_scheme_line, cd_bk_account_scheme_line "
						+ "   FROM " + getGeneralDBScheme()
						+ ".vc_bk_account_sh_ln_club_all "
						+ "  WHERE exist_flag='Y' "
						+ "  ORDER BY cd_bk_account_scheme_line", id_scheme,
				isNull);
	}

	public String getClubRelationshipComissionOptions(String cd_club_rel,
			String cd_com_type, String jurPrsTypeParty1,
			String jurPrsTypeParty2, boolean isNull) {
		ArrayList<bcFeautureParam> pParam = initParamArray();
		pParam.add(new bcFeautureParam("string", cd_club_rel));
		pParam.add(new bcFeautureParam("string", jurPrsTypeParty1));
		pParam.add(new bcFeautureParam("string", jurPrsTypeParty2));
		pParam.add(new bcFeautureParam("string", jurPrsTypeParty1));
		pParam.add(new bcFeautureParam("string", jurPrsTypeParty2));

		return getSelectBodyFromParamQuery(
				" SELECT b.id_comission_type, b.name_club_rel_type||'.'||b.name_comission_type name_comission_type "
						+ "   FROM "
						+ getGeneralDBScheme()
						+ ".vc_comission_type_club_all b "
						+ "  WHERE b.cd_club_rel_type = ? "
						+ "    AND ("
						+ "         (NVL(cd_jur_prs_type_payer, 'NULL') = NVL(?, 'NULL') AND "
						+ "          NVL(cd_jur_prs_type_receiver, 'NULL') = NVL(?, 'NULL')) "
						+ "         OR "
						+ "         (NVL(cd_jur_prs_type_receiver, 'NULL') = NVL(?, 'NULL') AND "
						+ "          NVL(cd_jur_prs_type_payer, 'NULL') = NVL(?, 'NULL')) "
						+ "        )"
						+ "  ORDER BY b.name_club_rel_type||'.'||b.name_comission_type",
				pParam, cd_com_type, "", isNull);
	}

	public String getClubRelationshipComissionTypeArray(String cd_club_rel,
			String jurPrsTypeParty1, String jurPrsTypeParty2,
			String elem_payer, String elem_receiver) {
		ArrayList<bcFeautureParam> pParam = initParamArray();
		pParam.add(new bcFeautureParam("string", cd_club_rel));
		pParam.add(new bcFeautureParam("string", jurPrsTypeParty1));
		pParam.add(new bcFeautureParam("string", jurPrsTypeParty2));
		pParam.add(new bcFeautureParam("string", jurPrsTypeParty1));
		pParam.add(new bcFeautureParam("string", jurPrsTypeParty2));

		String mySQL = "SELECT id_comission_type, cd_participant_payer, cd_participant_receiver "
				+ "  FROM "
				+ getGeneralDBScheme()
				+ ".vc_comission_type_club_all "
				+ " WHERE cd_club_rel_type = ? "
				+ "    AND ("
				+ "         (NVL(cd_jur_prs_type_payer, 'NULL') = NVL(?, 'NULL') AND "
				+ "          NVL(cd_jur_prs_type_receiver, 'NULL') = NVL(?, 'NULL')) "
				+ "         OR "
				+ "         (NVL(cd_jur_prs_type_receiver, 'NULL') = NVL(?, 'NULL') AND "
				+ "          NVL(cd_jur_prs_type_payer, 'NULL') = NVL(?, 'NULL')) "
				+ "        )"
				+ " ORDER BY name_club_rel_type||'.'||name_comission_type";
		StringBuilder return_value = new StringBuilder();
		PreparedStatement st = null;
		Connection con = null;
		int i = 0;
		try {
			LOGGER.debug(prepareSQLToLog(mySQL, pParam));
			con = Connector.getConnection(sessionId);
			st = con.prepareStatement(mySQL);
			st = prepareParam(st, pParam);
			ResultSet rset = st.executeQuery();

			while (rset.next()) {
				return_value.append(elem_payer + "[" + i + "]=\""
						+ rset.getString("CD_PARTICIPANT_PAYER") + "\"; ");
				return_value.append(elem_receiver + "[" + i + "]=\""
						+ rset.getString("CD_PARTICIPANT_RECEIVER") + "\"; ");
				i = i + 1;
			}
		} catch (SQLException e) {
			LOGGER.debug("bcCRMBean.getClubRelationshipComissionTypeArray() SQLException: "
					+ e.toString());
			return_value.append("<option value=\"\">SQL Error</option>");
		} catch (Exception el) {
			LOGGER.debug("bcCRMBean.getClubRelationshipComissionTypeArray() Exception: "
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
		} // finally
		return return_value.toString();
	}

	public String getWarningUserListOptions(String id_user, boolean isNull) {
		return getSelectBodyFromQuery2(" SELECT id_user, name_user "
				+ "   FROM " + getGeneralDBScheme() + ".v_warning_user_all "
				+ "  ORDER BY id_user ", id_user, isNull);
	}

	public String getBankAccountTypeOptions(String cd_type, boolean isNull) {
		return getSelectBodyFromQuery(
				" SELECT cd_bank_account_type, name_bank_account_type, exist_flag," +
				"        DECODE(cd_bank_account_type, " +
				"               'SETTLEMENT_ACCOUNT', 'style=\"color:green;font-weight:bold;\"', " +
				"               'CORRESPONDENT_ACCOUNT', 'style=\"color:blue;font-weight:bold;\"', " +
				"               ''" +
				"  		) option_style "
						+ "   FROM " + getGeneralDBScheme()
						+ ".vc_bank_account_type"
						+ "  ORDER BY DECODE(cd_bank_account_type, 'SETTLEMENT_ACCOUNT', 1, 99), name_bank_account_type", cd_type, isNull,
				bankAccountType, false);
	}

	public String getBankAccountStateOptions(String cd_state, boolean isNull) {
		return getSelectBodyFromQuery(
				" SELECT cd_bank_account_state, name_bank_account_state, exist_flag," +
				"        DECODE(cd_bank_account_state, " +
				"               'BLOCKED', 'style=\"color:red;font-weight:bold;\"', " +
				"               'CLOSED', 'style=\"color:red;font-weight:bold;\"', " +
				"               'OPENED', 'style=\"color:green;font-weight:bold;\"', " +
				"               ''" +
				"  		) option_style "
						+ "   FROM " + getGeneralDBScheme()
						+ ".vc_bank_account_state"
						+ "  ORDER BY name_bank_account_state", cd_state, isNull,
				bankAccountState, false);
	}

	public String getBKBankAccountTypeOptions(String id_name, boolean isNull) {
		return getSelectBodyFromQuery(
				" SELECT cd_bk_bank_account_type, name_bk_bank_account_type, exist_flag "
						+ "   FROM " + getGeneralDBScheme()
						+ ".vc_bk_bank_account_type"
						+ "  ORDER BY name_bk_bank_account_type", id_name,
				isNull, bkBankAccountType, false);
	}

	public String getBKBankAccountTypeForSchemeOptions(String id_bk_acc_scheme,
			String id_name, boolean isNull) {
		if (isEmpty(id_bk_acc_scheme)) {
			return "";
		} else {
			ArrayList<bcFeautureParam> pParam = initParamArray();
			pParam.add(new bcFeautureParam("int", id_bk_acc_scheme));

			return getSelectBodyFromParamQuery(
					" SELECT cd_bk_bank_account_type, name_bk_bank_account_type "
							+ "   FROM " + getGeneralDBScheme()
							+ ".vc_bk_bank_account_type"
							+ "  WHERE cd_participant IN ("
							+ "		SELECT cd_participant " + "		  FROM "
							+ getGeneralDBScheme()
							+ ".vc_bk_account_scheme_part_all "
							+ "        WHERE id_bk_account_scheme_line = ? "
							+ " ) ORDER BY name_bk_bank_account_type", pParam,
					id_name, "", isNull);
		}
	}

	public String getBankAccountOptions(String l_account_number, boolean isNull) {
		return getSelectBodyFromQuery2(
				" SELECT id_bank_account, SUBSTR(number_bank_account||' '||name_bank_account_type,1,50) name_bank_account, "
						+ "        number_bank_account "
						+ "   FROM "
						+ getGeneralDBScheme()
						+ ".v_bank_account_all "
						+ "  ORDER BY number_bank_account", l_account_number,
				isNull);
	}

	public String getBKAccountsGroupOptions(String id_group, boolean isNull) {
		return getSelectBodyFromSizedQuery(
				" SELECT id_bk_account, cd_bk_account||' '||name_bk_account name_bk_account "
						+ "   FROM " + getGeneralDBScheme()
						+ ".vc_bk_accounts_club_all " + "  WHERE is_group='Y'"
						+ "  ORDER BY cd_bk_account", id_group, "50", isNull,
				false);
	}

	public String getBKAccountsOptions(String id_account, boolean isNull) {
		return getSelectBodyFromSizedQuery(
				" SELECT id_bk_account, cd_bk_account||' '||name_bk_account name_bk_account "
						+ "   FROM " + getGeneralDBScheme()
						+ ".v_bk_accounts_all" + "  ORDER BY cd_bk_account",
				id_account, "50", isNull, false);
	}

	public String getDSPatternTypeOptions(String SMSProfileType, boolean isNull) {
		return getSelectBodyFromQuery(
				" SELECT cd_pattern_type, name_pattern_type||' ('||name_dispatch_type||')' name_pattern_type, exist_flag "
						+ "   FROM "
						+ getGeneralDBScheme()
						+ ".vc_ds_pattern_type_all"
						+ "  ORDER BY name_pattern_type||' ('||name_dispatch_type||')'",
				SMSProfileType, isNull, dsPatternTypeAddition, false);
	}

	public String getDSPatternTypeOptions(String SMSProfileType, String pSize,
			boolean isNull) {
		return getSelectBodyFromSizedQuery(
				" SELECT cd_pattern_type, name_pattern_type||' ('||name_dispatch_type||')' name_pattern_type, exist_flag "
						+ "   FROM "
						+ getGeneralDBScheme()
						+ ".vc_ds_pattern_type_all"
						+ "  ORDER BY name_pattern_type||' ('||name_dispatch_type||')'",
				SMSProfileType, pSize, isNull, dsPatternTypeAddition, false);
	}

	public String getDsDispatchTypeOptions(String SMSDispatchType,
			boolean isNull) {
		return getSelectBodyFromQuery(
				" SELECT cd_dispatch_type, name_dispatch_type, exist_flag "
						+ "   FROM " + getGeneralDBScheme()
						+ ".vc_ds_dispatch_type_all"
						+ "  ORDER BY name_dispatch_type", SMSDispatchType,
				isNull, dsDispatchType, false);
	}

	public String getDsPatternStatusOptions(String DSPatternStatus,
			boolean isNull) {
		return getSelectBodyFromQuery(
				" SELECT cd_pattern_status, name_pattern_status, exist_flag "
						+ "   FROM " + getGeneralDBScheme()
						+ ".vc_ds_pattern_status_all"
						+ "  ORDER BY name_pattern_status", DSPatternStatus,
				isNull, dsPatternStatus, false);
	}

	public String getClubCardStateOptions(String id_state, boolean isNull) {
		return getSelectBodyFromQuery(
				" SELECT id_card_state, name_card_state, exist_flag "
						+ "   FROM " + getGeneralDBScheme() + ".vc_card_state"
						+ "  ORDER BY name_card_state", id_state, isNull,
				clubCardState, false);
	}

	public String getNatPrsPostOptions(String cd_post, boolean isNull) {
		return getSelectBodyFromQuery2(" SELECT cd_post, name_post "
				+ "   FROM " + getGeneralDBScheme() + ".vc_post_all"
				+ "  ORDER BY name_post", cd_post, isNull, false);
	}

	public String getClubCardTypeOptions(String id_type, boolean isNull) {
		return getSelectBodyFromQuery(
				" SELECT id_card_type, name_card_type, exist_flag "
						+ "   FROM " + getGeneralDBScheme() + ".vc_card_type"
						+ "  ORDER BY name_card_type", id_type, isNull,
				clubCardType, false);
	}

	public String getQuestionnairePaymentMethodOptions(String cd_method,
			boolean isNull) {
		return getSelectBodyFromQuery(
				" SELECT cd_quest_payment_method, name_quest_payment_method, exist_flag "
						+ "   FROM " + getGeneralDBScheme()
						+ ".vc_quest_payment_method_all"
						+ "  ORDER BY name_quest_payment_method", cd_method,
				isNull, questPaymentMethod, false);
	}

	public String getReferralSchemeRecTypeOptions(String cd_type, boolean isNull) {
		return getSelectBodyFromQuery2(" SELECT cd_referral_scheme_rec_type, name_referral_scheme_rec_type "
				+ "   FROM " + getGeneralDBScheme() + ".vc_referral_scheme_rc_type_all"
				+ "  ORDER BY name_referral_scheme_rec_type", cd_type, isNull, false);
	}

	public String getContactPrsTypeOptions(String id_type, boolean isNull) {
		return getSelectBodyFromQuery2(" SELECT cd_post, name_post "
				+ "   FROM " + getGeneralDBScheme() + ".vc_post_all"
				+ "  ORDER BY name_post", id_type, isNull, false);
	}

	/*public String getPrivateOfficeStateOptions(String cd_state, boolean isNull) {
		return getSelectBodyFromQuery(
				" SELECT cd_office_private_state, name_office_private_state, exist_flag "
						+ "   FROM " + getGeneralDBScheme()
						+ ".vc_office_private_state_all"
						+ "  ORDER BY name_office_private_state", cd_state,
				isNull, officePrivateState, false);
	}

	public String getPrivateOfficeTypeOptions(String cd_type, boolean isNull) {
		return getSelectBodyFromQuery(
				" SELECT cd_office_private_type, name_office_private_type, exist_flag "
						+ "   FROM " + getGeneralDBScheme()
						+ ".vc_office_private_type_all"
						+ "  ORDER BY name_office_private_type", cd_type,
				isNull, officePrivateType, false);
	}


	public String getPrivateOfficeOptions(String cd_state, boolean isNull) {
		return getSelectBodyFromQuery2(
				" SELECT id_office_private, sname_office_private " + "   FROM "
						+ getGeneralDBScheme() + ".vc_office_private_all"
						+ "  ORDER BY sname_office_private", cd_state, isNull,
				false);
	}*/

	public String getDocOptions(String id_doc, boolean isNull) {
		return getSelectBodyFromSizedQuery(" SELECT id_doc, full_doc "
				+ "   FROM " + getGeneralDBScheme() + ".vc_doc_priv_all"
				+ "  ORDER BY full_doc", id_doc, "70", isNull, false);
	}

	public String getDocForClubRelationshipOptions(String id_club_rel,
			String id_doc, boolean isNull) {
		ArrayList<bcFeautureParam> pParam = initParamArray();
		pParam.add(new bcFeautureParam("int", id_club_rel));

		return getSelectBodyFromParamQuery(" SELECT id_doc, full_doc "
				+ "   FROM " + getGeneralDBScheme() + ".vc_doc_priv_all"
				+ "  WHERE id_club_rel = ? " + "  ORDER BY full_doc", pParam,
				id_doc, "70", isNull);
	}

	public String getDocForJurPrsAndClubOptions(String pIdJurPrs,
			String id_doc, boolean isNull) {
		ArrayList<bcFeautureParam> pParam = initParamArray();
		pParam.add(new bcFeautureParam("int", pIdJurPrs));
		pParam.add(new bcFeautureParam("int", pIdJurPrs));
		pParam.add(new bcFeautureParam("int", pIdJurPrs));

		return getSelectBodyFromParamQuery(" SELECT id_doc, full_doc "
				+ "   FROM " + getGeneralDBScheme() + ".vc_doc_priv_all"
				+ "  WHERE id_jur_prs_party1 = ? "
				+ "     OR id_jur_prs_party2 = ? "
				+ "     OR id_jur_prs_party3 = ? " + "  ORDER BY full_doc",
				pParam, id_doc, "70", isNull);
	}

	public String getLGDocTypeOptions(String cd_type, boolean isNull) {
		return getSelectBodyFromQuery(
				" SELECT cd_doc_type, name_doc_type, exist_flag " + "   FROM "
						+ getGeneralDBScheme() + ".vc_doc_type_all"
						+ "  WHERE NVL(used_in_logistic, 'N') = 'Y' "
						+ "  ORDER BY name_doc_type", cd_type, isNull,
				lgDocType, false);
	}

	public String getDocTypeOptions(String id_type, boolean isNull) {
		return getSelectBodyFromQuery(
				" SELECT cd_doc_type, name_doc_type, exist_flag " + "   FROM "
						+ getGeneralDBScheme() + ".vc_doc_type_all"
						+ "  ORDER BY name_doc_type", id_type, isNull, docType,
				false);
	}

	public String getDocStateOptions(String cd_state, boolean isNull) {
		return getSelectBodyFromQuery(
				" SELECT cd_doc_state, name_doc_state, exist_flag, "
						+ "        DECODE(cd_doc_state,"
						+ "               'SIGNED', 'style=\"color:green;font-weight:bold;\"',"
						+ "               'WORKING_OUT', 'style=\"color:blue;font-weight:bold;\"',"
						+ "               'FINISCHED', 'style=\"color:red;font-weight:bold;\"',"
						+ "		 		''" + "		 ) option_style "
						+ "   FROM " + getGeneralDBScheme()
						+ ".vc_doc_state_all" + "  ORDER BY name_doc_state",
				cd_state, isNull, docState, false);
	}

	public String getReferralShemeTypeOptions(String cd_type, boolean isNull) {
		return getSelectBodyFromQuery(
				" SELECT cd_referral_scheme_type, name_referral_scheme_type, exist_flag, "
						+ "        DECODE(cd_referral_scheme_type,"
						+ "               'TARGET_PROGRAM', 'style=\"color:blue;font-weight:bold;\"',"
						+ "               'PAYMENT', 'style=\"color:green;font-weight:bold;\"',"
						+ "		 		''" + "		 ) option_style "
						+ "   FROM " + getGeneralDBScheme()
						+ ".vc_referral_scheme_type_all" + "  ORDER BY name_referral_scheme_type",
						cd_type, isNull, referralSchemeType, false);
	}

	public String getReferralShemeTypeRadio(String pFieldName, String cd_type) {
		return getRadioButtonsFromQuery(
				pFieldName,
				" SELECT cd_referral_scheme_type, name_referral_scheme_type, exist_flag, "
				+ "        DECODE(cd_referral_scheme_type,"
				+ "               'TARGET_PROGRAM', 'style=\"color:blue;font-weight:bold;\"',"
				+ "               'PAYMENT', 'style=\"color:green;font-weight:bold;\"',"
				+ "		 		''" + "		 ) option_style "
				+ "   FROM " + getGeneralDBScheme()
				+ ".vc_referral_scheme_type_all" + "  ORDER BY name_referral_scheme_type",
				cd_type, clubXML, referralSchemeType, false);
	}

	public String getReferralShemeTypeName(String cd_type) {
		return getOneValueByStringId2("SELECT name_referral_scheme_type FROM "
				+ getGeneralDBScheme()
				+ ".vc_referral_scheme_type_all WHERE cd_referral_scheme_type = ?",
				cd_type, referralSchemeType);
	}

	public String getReferralShemeCalcTypeOptions(String cd_type, boolean isNull) {
		return getSelectBodyFromQuery(
				" SELECT cd_referral_scheme_calc_type, name_referral_scheme_calc_type, exist_flag, "
						+ "        DECODE(cd_referral_scheme_calc_type,"
						+ "               'POINT_SUM', 'style=\"color:blue;font-weight:bold;\"',"
						+ "               'CLUB_SUM', 'style=\"color:green;font-weight:bold;\"',"
						+ "		 		''" + "		 ) option_style "
						+ "   FROM " + getGeneralDBScheme()
						+ ".vc_referral_scheme_cl_type_all" + "  ORDER BY name_referral_scheme_calc_type",
						cd_type, isNull, referralSchemeCalcType, false);
	}

	public String getNatPrsDocTypeOptions(String id_type, boolean isNull) {
		return getSelectBodyFromQuery(
				" SELECT cd_nat_prs_doc_type, name_nat_prs_doc_type, exist_flag "
						+ "   FROM " + getGeneralDBScheme()
						+ ".vc_nat_prs_doc_type_all"
						+ "  ORDER BY name_nat_prs_doc_type", id_type, isNull,
				natPrsDocType, false);
	}

	public String getBKAccountBankAccountsOptions(String id_bk_account,
			String id_bank_account, boolean isNull) {
		ArrayList<bcFeautureParam> pParam = initParamArray();
		pParam.add(new bcFeautureParam("int", id_bk_account));

		return getSelectBodyFromParamQuery(
				" SELECT id_bank_account, number_bank_account " + "   FROM "
						+ getGeneralDBScheme()
						+ ".vc_bk_account_bank_account_all"
						+ "  WHERE id_bk_account = ? "
						+ "  ORDER BY number_bank_account", pParam,
				id_bank_account, "", isNull);
	}

	public String getClubCardCategoryOptions(String id_category, boolean isNull) {
		return getSelectBodyFromQuery2(
				" SELECT id_category_name, name_category_name, exist_flag "
						+ "   FROM "
						+ getGeneralDBScheme()
						+ ".vc_card_category_name_all "
						+ "  ORDER BY DECODE(id_category_name,99,2,1), name_category_name",
				id_category, isNull, false);
	}

	public String getClubCardCategoryForStatusOptions(String id_card_status,
			String id_category, String type_category, boolean isNull) {
		if (isEmpty(id_card_status)) {
			return "";
		} else {
			String lNameCategory = " name_category ";
			ArrayList<bcFeautureParam> pParam = initParamArray();
			pParam.add(new bcFeautureParam("int", id_card_status));

			return getSelectBodyFromParamQuery(
					" SELECT id_category, name_category, exist_flag "
							+ "   FROM (SELECT DISTINCT id_category, "
							+ lNameCategory
							+ " name_category, exist_flag, "
							+ "   			 id_category_name "
							+ "           FROM "
							+ getGeneralDBScheme()
							+ ".vc_card_category_club_all "
							+ "          WHERE id_card_status = ? "
							+ "    		 /*AND exist_flag = 'Y'*/ "
							+ "  		   ORDER BY DECODE(id_category_name, 99, 2, 1), name_category"
							+ "		 ) ", pParam, id_category, "", isNull);
		}
	}

	public String getClubCardCategoryForStatusOptions(String id_card_status,
			String id_category, boolean isNull) {
		if (isEmpty(id_card_status)) {
			return "";
		} else {
			ArrayList<bcFeautureParam> pParam = initParamArray();
			pParam.add(new bcFeautureParam("int", id_card_status));

			return getSelectBodyFromParamQuery(
					" SELECT id_category, name_category, exist_flag "
							+ "   FROM (SELECT DISTINCT id_category, name_category, exist_flag, "
							+ "   			 id_category_name "
							+ "           FROM "
							+ getGeneralDBScheme()
							+ ".vc_card_category_club_all "
							+ "          WHERE id_card_status = ? "
							+ "    		 /*AND exist_flag = 'Y'*/ "
							+ "  		   ORDER BY DECODE(id_category_name, 99, 2, 1), name_category"
							+ "		 ) ", pParam, id_category, "", isNull);
		}
	}

	public String getTermDeviceOptions(String pTermType, String pDeviceType,
			boolean isNull) {
		ArrayList<bcFeautureParam> pParam = initParamArray();
		pParam.add(new bcFeautureParam("none", ""));
		String mySQL = " SELECT id_device_type, name_device_type " + "   FROM "
				+ getGeneralDBScheme() + ".vc_term_device_type_names";
		if (!isEmpty(pTermType)) {
			mySQL = mySQL + "  WHERE cd_term_type = ? ";
			pParam.add(new bcFeautureParam("string", pTermType));
		}
		mySQL = mySQL + "  ORDER BY name_device_type";
		return getSelectBodyFromParamQuery(mySQL, pParam, pDeviceType, "",
				isNull);
	}

	public String getTermDeviceForManufacturerOptions(String id_manufacturer,
			String id_name, boolean isNull) {
		ArrayList<bcFeautureParam> pParam = initParamArray();
		pParam.add(new bcFeautureParam("int", id_manufacturer));

		return getSelectBodyFromParamQuery(
				" SELECT id_device_type, name_device_type " + "   FROM "
						+ getGeneralDBScheme() + ".vc_term_device_type_names"
						+ "  WHERE id_jur_prs_manufacturer = ? "
						+ "  ORDER BY name_device_type", pParam, id_name, "",
				isNull);
	}

	public String getClubCardPurses(String cardId, String id_issuer,
			String id_pay_sys, String pIdPurse, boolean isNull) {
		ArrayList<bcFeautureParam> pParam = initParamArray();
		pParam.add(new bcFeautureParam("string", cardId));
		pParam.add(new bcFeautureParam("int", id_issuer));
		pParam.add(new bcFeautureParam("int", id_pay_sys));

		return getSelectBodyFromParamQuery(
				" SELECT id_card_purse, number_card_purse||' - '||name_card_purse_type name_card_purse"
						+ "   FROM "
						+ getGeneralDBScheme()
						+ ".vc_card_purse_all "
						+ "  WHERE card_serial_number = ? "
						+ "    AND id_issuer = ? "
						+ "    AND id_payment_system = ? ", pParam, pIdPurse,
				"", isNull);
	}

	public String getClubCardPurses2(String id_card, String pIdPurse,
			boolean isNull) {
		ArrayList<bcFeautureParam> pParam = initParamArray();
		pParam.add(new bcFeautureParam("string", id_card));

		return getSelectBodyFromParamQuery(
				" SELECT id_card_purse, number_card_purse||' - '||name_card_purse_type name_card_purse"
						+ "   FROM "
						+ getGeneralDBScheme()
						+ ".vc_card_purse_all "
						+ "  WHERE card_serial_number||'_'||id_issuer||'_'||id_payment_system = ?",
				pParam, pIdPurse, "", isNull);
	}

	/*public String getTermServicePlacesOptions(String id_term,
			String idServicePlace, boolean isNull) {
		if (isEmpty(id_term)) {
			return getSelectBodyFromQuery2(
					" SELECT id_service_place, name_service_place "
							+ "   FROM " + getGeneralDBScheme()
							+ ".vc_term_service_places_all"
							+ "  ORDER BY name_service_place", idServicePlace,
					isNull, false);
		} else {
			ArrayList<bcFeautureParam> pParam = initParamArray();
			pParam.add(new bcFeautureParam("int", id_term));

			return getSelectBodyFromParamQuery(
					" SELECT id_service_place, name_service_place "
							+ "   FROM " + getGeneralDBScheme()
							+ ".vc_term_service_places_all"
							+ "  WHERE id_term = ? "
							+ "  ORDER BY name_service_place", pParam,
					idServicePlace, "", isNull);
		}
	}*/

	public String getShedulesOptions(String id_shed, boolean isNull) {
		return getSelectBodyFromQuery2(" SELECT id_shedule, name_shedule "
				+ "   FROM " + getGeneralDBScheme()
				+ ".v_ls_shedule_name_club_all " + "  ORDER BY name_shedule",
				id_shed, isNull, false);
	}

	public String getSAMNotUsedOptions(String id_sam, boolean isNull) {
		return getSelectBodyFromQuery2(
				" SELECT id_sam id_sam1, id_sam||' - '||sam_serial_number id_sam2 "
						+ "   FROM " + getGeneralDBScheme()
						+ ".vc_sam_not_used_all " 
						+ "  ORDER BY id_sam||' - '||sam_serial_number",
				id_sam, isNull, false);
	}

	public String getClubCardPurseTypeOptions(String cd_type, boolean isNull) {
		return getSelectBodyFromQuery(
				" SELECT cd_card_purse_type, name_card_purse_type, exist_flag "
						+ "   FROM " + getGeneralDBScheme()
						+ ".vc_card_purse_type_all "
						+ "  ORDER BY name_card_purse_type", cd_type, isNull,
				clubCardPurseType, false);
	}

	public String getSAMListOption(String id_sam, boolean isNull) {
		return getSelectBodyFromQuery2(
				" SELECT id_sam id_sam2, id_sam||' - '||sam_serial_number id_sam1 "
						+ "   FROM " + getGeneralDBScheme()
						+ ".vc_sam_club_all " + "  ORDER BY id_sam", id_sam,
				isNull, false);
	}

	public String getBKParticipantOptions(String cd_participant, boolean isNull) {
		return getSelectBodyFromQuery(
				" SELECT cd_participant, name_participant, exist_flag "
						+ "   FROM " + getGeneralDBScheme()
						+ ".vc_bk_participant " + "  WHERE exist_flag='Y' "
						+ "  ORDER BY name_participant", cd_participant,
				isNull, bkParticipant, false);
	}

	public String getClubComissionParticipantOptions(String cd_participant, boolean isNull) {
		return getSelectBodyFromQuery(
				" SELECT cd_club_comission_part, name_club_comission_part, exist_flag "
						+ "   FROM " + getGeneralDBScheme()
						+ ".vc_club_comission_part_all " 
						+ "  ORDER BY name_club_comission_part", cd_participant,
				isNull, clubComissionParticipant, false);
	}

	public String getClubCardBonCategoryOptions(String id_status,
			String id_category, boolean isNull) {
		ArrayList<bcFeautureParam> pParam = initParamArray();
		pParam.add(new bcFeautureParam("int", id_status));

		return getSelectBodyFromParamQuery(
				" SELECT id_bon_category, name_bon_category " + "   FROM "
						+ getGeneralDBScheme() + ".vc_card_bon_category_all "
						+ "  WHERE id_card_status = ? "
						+ "  ORDER BY name_bon_category", pParam, id_category,
				"", isNull);
	}

	/*public String getJurPrsTypeOptions(String id_type, boolean isNull) {
		return getSelectBodyFromQuery(
				" SELECT cd_jur_prs_type, name_jur_prs_type, exist_flag, "
						+ "        DECODE(cd_jur_prs_type,"
						+ "               'OPERATOR', 'style=\"color:red;font-weight:bold;\"',"
						+ "               'DEALER', 'style=\"color:green;font-weight:bold;\"',"
						+ "               'BANK', 'style=\"color:blue;font-weight:bold;\"',"
						+ "               'AGENT', 'style=\"color:red;\"',"
						+ "		 		''" + "		 ) option_style " + "   FROM "
						+ getGeneralDBScheme() + ".vc_jur_prs_type_all"
						+ "  ORDER BY DECODE(cd_jur_prs_type, 'OPERATOR', 1, 'AGENT', 2, 'DEALER', 3, 'BANK', 4, 99), name_jur_prs_type", id_type, isNull,
				jurPrsType, false);
	}*/

	public String getClubRelationshipOptions(String id_jur_prs,
			String id_club_rel, boolean isNull) {
		ArrayList<bcFeautureParam> pParam = initParamArray();
		pParam.add(new bcFeautureParam("none", ""));

		String mySQL = " SELECT id_club_rel, full_name_club_rel " + "   FROM "
				+ getGeneralDBScheme() + ".vc_club_rel_club_all ";
		if (!isEmpty(id_jur_prs)) {
			mySQL = mySQL + "  WHERE id_party1 = ? " + "     OR id_party2 = ?";
			pParam.add(new bcFeautureParam("int", id_jur_prs));
			pParam.add(new bcFeautureParam("int", id_jur_prs));
		}
		mySQL = mySQL + "  ORDER BY desc_club_rel";
		return getSelectBodyFromParamQuery(mySQL, pParam, id_club_rel, "",
				isNull);
	}

	public String getClubCardDiscCategoryOptions(String id_status,
			String id_category, boolean isNull) {
		ArrayList<bcFeautureParam> pParam = initParamArray();
		pParam.add(new bcFeautureParam("int", id_status));

		return getSelectBodyFromParamQuery(
				" SELECT id_disc_category, name_disc_category " + "   FROM "
						+ getGeneralDBScheme() + ".vc_card_disc_category_all "
						+ "  WHERE id_card_status = ? "
						+ "  ORDER BY name_disc_category", pParam, id_category,
				"", isNull);
	}

	public String getJurPrsFormOptions(String id_type, boolean isNull) {
		return getSelectBodyFromQuery2(
				" SELECT id_jur_prs_form, name_jur_prs_form, "
						+ "        DECODE(id_jur_prs_form,"
						+ "               0, 'style=\"color:red;font-weight:bold;\"',"
						+ "		 		''" + "		 ) option_style "
						+ "   FROM "
						+ getGeneralDBScheme()
						+ ".vc_jur_prs_form_all"
						+ "  ORDER BY DECODE(id_jur_prs_form, 0, 0, 1), name_jur_prs_form",
				id_type, isNull, false);
	}

	public String getGiftTypeOptions(String cd_gift_type, boolean isNull) {
		return getSelectBodyFromQuery(
				" SELECT cd_gift_type, name_gift_type, exist_flag "
						+ "   FROM " + getGeneralDBScheme()
						+ ".vc_gift_type_all" + "  ORDER BY name_gift_type",
				cd_gift_type, isNull, giftType, false);
	}

	public String getJurPrsReferralSchemeOptions(String id_jur_prs, String id_scheme, boolean isNull) {
		String mySQL = 
			" SELECT id_referral_scheme, name_referral_scheme "
			+ "   FROM " + getGeneralDBScheme() + ".vc_referral_scheme_club_all" ;
		if (!isEmpty(id_jur_prs)) {
			mySQL = mySQL + "  WHERE id_jur_prs = " + id_jur_prs;
		}
		mySQL = mySQL + " ORDER BY name_referral_scheme";
		return getSelectBodyFromQuery2(mySQL, id_scheme, isNull, false);
	}

	public String getLogisticPromoterStateOptions(String cd_state,
			boolean isNull) {
		return getSelectBodyFromQuery(
				" SELECT cd_lg_promoter_state, name_lg_promoter_state, exist_flag "
						+ "   FROM "
						+ getGeneralDBScheme()
						+ ".vc_lg_promoter_state_all"
						+ "  ORDER BY DECODE(cd_lg_promoter_state, 'ACCEPTED', 1, 'TRANSFERRED', 2, 3), name_lg_promoter_state",
				cd_state, isNull, lgPromoterState, false);
	}

	public String getLogisticPromoterGiveStateOptions(String cd_state,
			boolean isNull) {
		return getSelectBodyFromQuery(
				" SELECT cd_lg_promoter_give_state, name_lg_promoter_give_state, exist_flag "
						+ "   FROM "
						+ getGeneralDBScheme()
						+ ".vc_lg_promoter_give_state_all"
						+ "  ORDER BY DECODE(cd_lg_promoter_give_state, 'WORKS', 1, 2), name_lg_promoter_give_state",
				cd_state, isNull, lgPromoterGiveState, false);
	}

	public String getLogisticPromoterPostOptions(String cd_state, boolean isNull) {
		return getSelectBodyFromQuery(
				" SELECT cd_lg_promoter_post, name_lg_promoter_post, exist_flag "
						+ "   FROM " + getGeneralDBScheme()
						+ ".vc_lg_promoter_post_all"
						+ "  ORDER BY name_lg_promoter_post", cd_state, isNull,
				lgPromoterPost, false);
	}

	public String getLogisticPromoterPayKindOptions(String cd_kind,
			boolean isNull) {
		return getSelectBodyFromQuery(
				" SELECT cd_lg_promoter_pay_kind, name_lg_promoter_pay_kind, exist_flag "
						+ "   FROM " + getGeneralDBScheme()
						+ ".vc_lg_promoter_pay_kind_all"
						+ "  ORDER BY name_lg_promoter_pay_kind", cd_kind,
				isNull, lgPromoterPayKind, false);
	}

	public String getLogisticPromoterPayStateOptions(String cd_state,
			boolean isNull) {
		return getSelectBodyFromQuery(
				" SELECT cd_lg_promoter_pay_state, name_lg_promoter_pay_state, exist_flag "
						+ "   FROM " + getGeneralDBScheme()
						+ ".vc_lg_promoter_pay_state_all"
						+ "  ORDER BY name_lg_promoter_pay_state", cd_state,
				isNull, lgPromoterPayState, false);
	}

	public String getLogisticPromoterPayKindRadio(String pFieldName,
			String cd_kind) {
		return getRadioButtonsFromQuery(pFieldName,
				" SELECT cd_lg_promoter_pay_kind, name_lg_promoter_pay_kind "
						+ "   FROM " + getGeneralDBScheme()
						+ ".vc_lg_promoter_pay_kind_all"
						+ "  ORDER BY name_lg_promoter_pay_kind", cd_kind,
				logisticXML, lgPromoterPayKind, false);
	}

	public String getLogisticPromoterPostRadio(String pFieldName, String cd_post) {
		return getRadioButtonsFromQuery(pFieldName,
				" SELECT cd_lg_promoter_post, name_lg_promoter_post "
						+ "   FROM " + getGeneralDBScheme()
						+ ".vc_lg_promoter_post_all"
						+ "  ORDER BY name_lg_promoter_post", cd_post,
				logisticXML, lgPromoterPost, false);
	}

	public String getJurPrsServicePlaceOptions(String id_place, boolean isNull) {
		return getSelectBodyFromSizedQuery(
				" SELECT id_service_place, name_service_place " + "   FROM "
						+ getGeneralDBScheme()
						+ ".vc_jur_prs_service_place_all"
						+ "  ORDER BY name_service_place", id_place, "40",
				isNull, false);
	}

	public String getReportTaskStateOptions(String cd_state, boolean isNull) {
		return getSelectBodyFromQuery(
				" SELECT cd_report_task_state, name_report_task_state, exist_flag "
						+ "   FROM " + getGeneralDBScheme()
						+ ".vc_reports_task_state_all"
						+ "  ORDER BY name_report_task_state", cd_state,
				isNull, reportTaskState, false);
	}

	public String getOfficePrivateActionStateOptions(String cd_state,
			boolean isNull) {
		return getSelectBodyFromQuery(
				" SELECT cd_op_action_state, name_op_action_state, exist_flag "
						+ "   FROM " + getGeneralDBScheme()
						+ ".vc_op_action_state_all"
						+ "  ORDER BY name_op_action_state", cd_state, isNull,
				OPActionState, false);
	}

	public String getOfficePrivateActionTypeOptions(String cd_type,
			boolean isNull) {
		return getSelectBodyFromQuery(
				" SELECT cd_op_action_type, name_op_action_type, exist_flag "
						+ "   FROM " + getGeneralDBScheme()
						+ ".vc_op_action_type_all"
						+ "  ORDER BY name_op_action_type", cd_type, isNull,
				OPActionType, false);
	}

	public String getClubActionTypeOptions(String cd_type, boolean isNull) {
		return getSelectBodyFromQuery(
				" SELECT cd_club_event_type, name_club_event_type, exist_flag "
						+ "   FROM " + getGeneralDBScheme()
						+ ".vc_club_event_type_all"
						+ "  ORDER BY name_club_event_type", cd_type, isNull,
				clubEventType, false);
	}

	public String getClubActionStateOptions(String cd_state, boolean isNull) {
		return getSelectBodyFromQuery(
				" SELECT cd_club_event_state, name_club_event_state, exist_flag "
						+ "   FROM " + getGeneralDBScheme()
						+ ".vc_club_event_state_all"
						+ "  ORDER BY name_club_event_state", cd_state, isNull,
				clubEventState, false);
	}

	public String getNatPrsGiftStateOptions(String cd_state, boolean isNull) {
		return getSelectBodyFromQuery(
				" SELECT cd_nat_prs_gift_state, name_nat_prs_gift_state, exist_flag "
						+ "   FROM "
						+ getGeneralDBScheme()
						+ ".vc_nat_prs_gift_state_all"
						+ "  ORDER BY DECODE(cd_nat_prs_gift_state, 'RESERVED', 1, 'GIVEN', 2, 'RETURNED', 3, 4), name_nat_prs_gift_state",
				cd_state, isNull, natPrsGiftState, false);
	}

	public String getNatPrsGiftStateGivenReservedOptions2(String cd_state,
			boolean isNull) {
		return getSelectBodyFromQuery2(
				" SELECT cd_nat_prs_gift_state, name_nat_prs_gift_state "
						+ "   FROM "
						+ getGeneralDBScheme()
						+ ".vc_nat_prs_gift_state_all"
						+ "  WHERE cd_nat_prs_gift_state IN ('GIVEN', 'RESERVED')"
						+ "  ORDER BY DECODE(cd_nat_prs_gift_state, 'RESERVED', 1, 'GIVEN', 2, 'RETURNED', 3, 4), name_nat_prs_gift_state",
				cd_state, isNull, false);
	}

	public String getNatPrsGiftRequestStateOptions(String cd_state,
			boolean isNull) {
		return getSelectBodyFromQuery(
				" SELECT cd_nat_prs_gift_request_state, nm_nat_prs_gift_request_state, exist_flag "
						+ "   FROM "
						+ getGeneralDBScheme()
						+ ".vc_nat_prs_gift_request_st_all"
						+ "  ORDER BY DECODE(cd_nat_prs_gift_request_state, 'ACCEPT', 1, 'DISASSEMBLED', 2, 'PROCESSED', 3, 'REJECTED', 4, 'ERROR', 5, 6)",
				cd_state, isNull, natPrsGiftRequestState, false);
	}

	public String getNatPrsGiftRequestTypeOptions(String cd_type, boolean isNull) {
		return getSelectBodyFromQuery(
				" SELECT cd_nat_prs_gift_request_type, name_nat_prs_gift_request_type, exist_flag "
						+ "   FROM "
						+ getGeneralDBScheme()
						+ ".vc_nat_prs_gift_request_tp_all"
						+ "  ORDER BY name_nat_prs_gift_request_type", cd_type,
				isNull, natPrsGiftRequestType, false);
	}

	public String getNatPrsGiftRequestTypeOptionsExclude(String cd_type,
			String pExcludeString, boolean isNull) {
		String mySQL = " SELECT cd_nat_prs_gift_request_type, name_nat_prs_gift_request_type "
				+ "   FROM "
				+ getGeneralDBScheme()
				+ ".vc_nat_prs_gift_request_tp_all";
		if (!isEmpty(pExcludeString)) {
			mySQL = mySQL + " WHERE cd_nat_prs_gift_request_type NOT IN ("
					+ pExcludeString + ")";
		}
		mySQL = mySQL + "  ORDER BY name_nat_prs_gift_request_type";

		return getSelectBodyFromQuery2(mySQL, cd_type, isNull, false);
	}

	public String getNomenklUnitOptions(String cd_unit, boolean isNull) {
		return getSelectBodyFromQuery(
				" SELECT cd_nomenkl_unit, name_nomenkl_unit, exist_flag "
						+ "   FROM " + getGeneralDBScheme()
						+ ".vc_nomenkl_unit_all"
						+ "  ORDER BY name_nomenkl_unit", cd_unit, isNull,
				nomenklUnit, false);
	}

	public String getGiftsOptions(String id_gift, boolean isNull) {
		return getSelectBodyFromQuery2(" SELECT id_gift, name_gift "
				+ "   FROM " + getGeneralDBScheme() + ".vc_gifts_club_all"
				+ "  ORDER BY name_gift", id_gift, isNull, false);
	}

	public String getClubRelTypeOptions(String cd_rel_type, boolean isNull) {
		return getSelectBodyFromQuery(
				" SELECT cd_club_rel_type, name_club_rel_type " + "   FROM "
						+ getGeneralDBScheme() + ".vc_club_rel_type_all"
						+ "  ORDER BY name_club_rel_type", cd_rel_type, isNull,
				clubRelType, false);
	}

	public String getClubRelTypeRadio(String pFieldName, String cd_type) {
		return getRadioButtonsFromQuery(
				pFieldName,
				" SELECT cd_club_rel_type, name_club_rel_type "
				+ "   FROM " + getGeneralDBScheme()
				+ ".vc_club_rel_type_all" + "  ORDER BY name_club_rel_type",
				cd_type, null, clubRelType, false);
	}

	public String getJurPrsClubRelTypeOptions(String is_operator, String is_dealer, String is_bank,
			String is_partner, String is_terminal_manufacturer,
			String is_issuer, String is_finance_acquirer,
			String is_technical_acquirer, String cd_rel_type, boolean isNull) {
		String pFiltr = "";
		if ("Y".equalsIgnoreCase(is_dealer)) {
			pFiltr = pFiltr
					+ " ,'OPERATOR-CARD_SELLER', 'OPERATOR-DEALER', 'OPERATOR-ISSUER', 'OPERATOR-FINANCE_ACQUIRER', 'OPERATOR-TECHNICAL_ACQUIRER', 'DEALER-FINANCE_ACQUIRER', 'DEALER-TECHNICAL_ACQUIRER'";
		}
		if ("Y".equalsIgnoreCase(is_operator)) {
			pFiltr = pFiltr
					+ " ,'DEALER-FINANCE_ACQUIRER', 'DEALER-TECHNICAL_ACQUIRER'";
		}
		if ("Y".equalsIgnoreCase(is_terminal_manufacturer)) {
			pFiltr = pFiltr
					+ " ,'OPERATOR-CARD_SELLER', 'OPERATOR-TERMINAL_MANUFACTURER', 'OPERATOR-TECHNICAL_ACQUIRER', 'DEALER-TECHNICAL_ACQUIRER'";
		}
		if ("Y".equalsIgnoreCase(is_bank)) {
			pFiltr = pFiltr
					+ " ,'OPERATOR-CARD_SELLER', 'OPERATOR-FINANCE_ACQUIRER', 'OPERATOR-ISSUER', 'OPERATOR-TECHNICAL_ACQUIRER', 'DEALER-FINANCE_ACQUIRER', 'DEALER-TECHNICAL_ACQUIRER'";
		}
		if ("Y".equalsIgnoreCase(is_partner)) {
			pFiltr = pFiltr
					+ " ,'OPERATOR-CARD_SELLER', 'OPERATOR-PARTNER', 'OPERATOR-TECHNICAL_ACQUIRER', 'DEALER-TECHNICAL_ACQUIRER'";
		}
		if ("Y".equalsIgnoreCase(is_issuer)) {
			pFiltr = pFiltr + " ,'OPERATOR-ISSUER'";
		}
		if ("Y".equalsIgnoreCase(is_finance_acquirer)) {
			pFiltr = pFiltr + " ,'OPERATOR-FINANCE_ACQUIRER'";
		}
		if ("Y".equalsIgnoreCase(is_technical_acquirer)) {
			pFiltr = pFiltr + " ,'OPERATOR-TECHNICAL_ACQUIRER'";
		}
		return getSelectBodyFromQuery2(
				" SELECT cd_club_rel_type, name_club_rel_type " + "   FROM "
						+ getGeneralDBScheme() + ".vc_club_rel_type_all "
						+ "  WHERE cd_club_rel_type IN (''" + pFiltr + ")"
						+ "  ORDER BY name_club_rel_type", cd_rel_type, isNull,
				false);
	}

	public String getEstimateCriterionsOptions(String id_crit, boolean isNull) {
		return getSelectBodyFromQuery(
				" SELECT id_club_event_estim_crit, name_club_event_estim_crit, exist_flag "
						+ "   FROM " + getGeneralDBScheme()
						+ ".vc_club_event_estim_crit_all"
						+ "  ORDER BY name_club_event_estim_crit", id_crit,
				isNull, clubEstimateCriterion, false);
	}

	public String getIssuerNamesOptions(String id_name, boolean isNull) {
		return getSelectBodyFromQuery2(" SELECT id_jur_prs, sname_jur_prs "
				+ "   FROM " + getGeneralDBScheme()
				+ ".vc_jur_prs_short_club_all" + "  WHERE is_issuer = 'Y' "
				+ "  ORDER BY sname_jur_prs", id_name, isNull, false);
	}

	public String getDocumentParticipantsOptions(String id_owner, boolean isNull) {
		return getSelectBodyFromQuery2(
				" WITH t AS (SELECT id_jur_prs_party1, sname_jur_prs_party1, " +
				"	                id_jur_prs_party2, sname_jur_prs_party2, " +
				"	                id_jur_prs_party3, sname_jur_prs_party3 " +
				"              FROM " + getGeneralDBScheme() + ".vc_doc_all) " +
				" SELECT DISTINCT id_jur_prs, sname_jur_prs " +
				"   FROM (SELECT id_jur_prs_party1 id_jur_prs, sname_jur_prs_party1 sname_jur_prs " +
				"           FROM t" +
				"          UNION ALL " +
				"         SELECT id_jur_prs_party2, sname_jur_prs_party2 " +
				"           FROM t" +
				"          UNION ALL " +
				"         SELECT id_jur_prs_party3, sname_jur_prs_party3 " +
				"           FROM t)" +
				"  WHERE id_jur_prs IS NOT NULL" +
				"  ORDER BY sname_jur_prs ", id_owner, isNull, false);
	}

	public String getJurPrsKindOptions(String cd_kind, boolean isNull) {
		return getSelectBodyFromQuery(
				" SELECT cd_jur_prs_kind, name_jur_prs_kind, exist_flag "
						+ "   FROM " + getGeneralDBScheme()
						+ ".vc_jur_prs_kind_all"
						+ "  ORDER BY name_jur_prs_kind", cd_kind, isNull,
				jurPrsKind, false);
	}

	public String getComissionTypeOptions(String id_type, boolean isNull) {
		return getSelectBodyFromSizedQuery(
				" SELECT id_comission_type, name_comission_type " + "   FROM "
						+ getGeneralDBScheme() + ".vc_comission_type_club_all"
						+ "  ORDER BY name_comission_type", id_type, "40",
				isNull, false);
	}

	public String getComissionTypeRadio(String pFieldName, String cd_type) {
		ArrayList<bcDictionaryRecord> comissionType = new ArrayList<bcDictionaryRecord>();
		return getRadioButtonsFromQuery(
				pFieldName,
				" SELECT id_comission_type, name_comission_type, exist_flag "
						+ "   FROM "
						+ getGeneralDBScheme()
						+ ".vc_comission_type_club_all"
						+ "  ORDER BY name_comission_type",
						cd_type, relationshipXML, comissionType, false);
	}
	
	public String getNatPrsRoleStateOptions(String cd_state, boolean isNull) {
		return getSelectBodyFromQuery(
				" SELECT cd_nat_prs_role_state, name_nat_prs_role_state, exist_flag, "
						+ "        DECODE(cd_nat_prs_role_state,"
						+ "               'GIVEN', 'style=\"color:black;font-weight:bold;\"',"
						+ "               'QUESTIONED', 'style=\"color:blue;font-weight:bold;\"',"
						+ "               'ACTIVATED', 'style=\"color:green;font-weight:bold;\"',"
						+ "               'WITHOUT_CARD', 'style=\"color:gray;font-weight:bold;\"',"
						+ "		 		''" + "		 ) option_style " + "   FROM "
						+ getGeneralDBScheme() + ".vc_nat_prs_role_state_all"
						+ "  ORDER BY DECODE(cd_nat_prs_role_state, 'GIVEN', 1, 'QUESTIONED', 2, 'ACTIVATED', 3, 4), name_nat_prs_role_state", cd_state, 
				isNull, natPrsRoleState, false);
	}

	public String getTermSessionActionOptions(String cd_action, boolean isNull) {
		return getSelectBodyFromQuery2(
				" SELECT cd_action, name_action FROM "
						+ "(SELECT 1 id_action, 'MONITOR' cd_action, 'Мониторинг' name_action FROM dual UNION ALL "
						+ " SELECT 2 id_action, 'CARD_ONLINE' cd_action, 'Проверка карты' name_action FROM dual UNION ALL "
						+ " SELECT 3 id_action, 'ONLINE_PAY' cd_action, 'Онлайн платеж' name_action FROM dual UNION ALL "
						+ " SELECT 4 id_action, 'COL_DATA' cd_action, 'Отправка сбора' name_action FROM dual UNION ALL "
						+ " SELECT 5 id_action, 'PARAM' cd_action, 'Получение параметров' name_action FROM dual) "
						+ "  ORDER BY id_action", cd_action, isNull);
	}

	public String getComissionTypeForCalculatorOptions(String cdClubRelType,
			String id_type, boolean isNull) {
		ArrayList<bcFeautureParam> pParam = initParamArray();
		pParam.add(new bcFeautureParam("none", ""));

		String mySQL = "SELECT cd_comission_type, name_comission_type "
				+ "  FROM (SELECT b.cd_club_rel_type||'.'||b.cd_comission_type cd_comission_type, "
				+ "       		   b.name_club_rel_type||'.'||b.name_comission_type name_comission_type "
				+ "  		  FROM " + getGeneralDBScheme()
				+ ".vc_comission_type_club_all b ";
		if (!isEmpty(cdClubRelType)) {
			mySQL = mySQL + " WHERE b.cd_club_rel_type = ? ";
			pParam.add(new bcFeautureParam("string", cdClubRelType));
		}
		mySQL = mySQL + "        ) " + "  ORDER BY name_comission_type";

		return getSelectBodyFromParamQuery(mySQL, pParam, id_type, "70", isNull);
	}

	public String getRoleMenuOptions(String pModuleType, String id_role) {
		ArrayList<bcFeautureParam> pParam = initParamArray();
		pParam.add(new bcFeautureParam("string", pModuleType));
		pParam.add(new bcFeautureParam("int", id_role));

		return getSelectBodyFromParamQuery(
				"SELECT s1.id_menu_element, "
						+ "       CASE WHEN m.name_menu_element IS NULL "
						+ "            THEN s1.name_menu_element "
						+ "            ELSE m.name_menu_element||' -> '||s1.name_menu_element "
						+ "       END name_menu_element   "
						+ "  FROM (SELECT id_menu_element, id_menu_element_parent, name_menu_element, "
						+ "               type_menu_element, type_menu_element_tsl, order_number "
						+ "          FROM "
						+ getGeneralDBScheme()
						+ ".vc_menu_full_all a "
						+ "                 WHERE a.is_enable = 'Y'"
						+ "                   AND a.is_visible = 'Y' "
						+ "                   AND a.cd_module_type = ? "
						+ "        MINUS  "
						+ "        SELECT a.id_menu_element, a.id_menu_element_parent, a.name_menu_element, "
						+ "               a.type_menu_element, a.type_menu_element_tsl, a.order_number "
						+ "          FROM "
						+ getGeneralDBScheme()
						+ ".vc_menu_privilege_all a where a.id_role = ? "
						+ "       ) s1 LEFT JOIN "
						+ getGeneralDBScheme()
						+ ".vc_menu_all m "
						+ "    ON (s1.id_menu_element_parent = m.id_menu_element) "
						+ " ORDER BY s1.order_number", pParam, id_role, "",
				false);
	}

	public String getRoleReportsOptions(String id_role) {
		ArrayList<bcFeautureParam> pParam = initParamArray();
		pParam.add(new bcFeautureParam("int", "id_role"));

		return getSelectBodyFromParamQuery(
				" SELECT id_report, name_report   "
						+ "   FROM (SELECT id_menu_element, id_report, name_menu_element||': '||name_report name_report    "
						+ "           FROM "
						+ getGeneralDBScheme()
						+ ".vc_reports_all "
						+ "          WHERE id_menu_element IN (SELECT id_menu_element "
						+ "                                      FROM "
						+ getGeneralDBScheme()
						+ ".vc_menu_privilege_all"
						+ "                                     WHERE id_menu_element in (710, 720, 730)) "
						+ "          MINUS "
						+ "         SELECT id_menu_element, id_report, name_menu_element||': '||name_report name_report "
						+ "           FROM " + getGeneralDBScheme()
						+ ".vc_report_privilege_all"
						+ "          WHERE id_role = ?) "
						+ " ORDER BY id_menu_element, name_report", pParam, "",
				"", false);
	}

	public String getMenuProfileList(String id_menu, String id_profile) {
		StringBuilder html = new StringBuilder();
		PreparedStatement st = null;
		Connection con = null;

		ArrayList<bcFeautureParam> pParam = initParamArray();
		pParam.add(new bcFeautureParam("int", id_menu));

		String mySQL = " SELECT id_menu_element, name_menu_element "
				+ "   FROM " + getGeneralDBScheme() + ".vc_menu_profile_all "
				+ "  WHERE id_menu_element_parent = ? "
				+ "    AND is_enable = 'Y' " + "    AND is_visible = 'Y' "
				+ "  ORDER BY order_number";
		int myCnt = 0;
		try {
			LOGGER.debug(prepareSQLToLog(mySQL, pParam));
			con = Connector.getConnection(sessionId);
			st = con.prepareStatement(mySQL);
			st = prepareParam(st, pParam);
			ResultSet rset = st.executeQuery();
			while (rset.next()) {
				myCnt = myCnt + 1;
				html.append("<option value="
						+ rset.getString("id_menu_element") + "");
				if (rset.getString("id_menu_element").equalsIgnoreCase(
						id_profile)) {
					html.append(" SELECTED");
				}
				html.append(">" + rset.getString("name_menu_element")
						+ "</option>");
			}
		} catch (SQLException e) {
			LOGGER.error("bcCRMBean.getMenuProfileList SQLException: "
					+ e.toString());
		} catch (Exception el) {
			LOGGER.error("bcCRMBean.getMenuProfileList Exception: " + el.toString());
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
		} // finally
		return html.toString();
	}

	// ========================================================================
	// Функции возвращают одно значение из запроса
	// ========================================================================


	public String getCurrencyNameById(String currency_id) {
		return getOneValueByIntId2("select name_currency FROM "
				+ getGeneralDBScheme()
				+ ".vc_currency_all WHERE cd_currency = ? ", currency_id);
	}

	public String getNatPrsNameByIdRole(String pIdNatPrsRole) {
		if (isEmpty(pIdNatPrsRole)) {
			return "";
		}
		ArrayList<bcFeautureParam> pParam = initParamArray();
		pParam.add(new bcFeautureParam("int", pIdNatPrsRole));

		String mySQL = 
			" SELECT fio_nat_prs " +
			"   FROM " + getGeneralDBScheme() + ".vc_nat_prs_role_short_all " +
			" WHERE id_nat_prs_role = ?";
		return getOneValueByParamId(mySQL, pParam);
	}

	public String getNatPrsNameAndCardByIdRole(String pIdNatPrsRole) {
		if (isEmpty(pIdNatPrsRole)) {
			return "";
		}
		ArrayList<bcFeautureParam> pParam = initParamArray();
		pParam.add(new bcFeautureParam("int", pIdNatPrsRole));

		String mySQL = 
			" SELECT CASE WHEN cd_card1 IS NULL THEN fio_nat_prs ELSE cd_card1||' ('||fio_nat_prs||')' END fio_nat_prs " +
			"   FROM " + getGeneralDBScheme() + ".vc_nat_prs_role_short2_all " +
			" WHERE id_nat_prs_role = ?";
		return getOneValueByParamId(mySQL, pParam);
	}

	/*public String getNatPrsNameByCardNumber(String cardId, String id_issuer,
			String id_pay_sys) {
		if (isEmpty(cardId)) {
			return "";
		}
		ArrayList<bcFeautureParam> pParam = initParamArray();
		pParam.add(new bcFeautureParam("string", cardId));
		pParam.add(new bcFeautureParam("int", id_issuer));
		pParam.add(new bcFeautureParam("int", id_pay_sys));

		String mySQL = "SELECT full_name FROM " + getGeneralDBScheme()
				+ ".vc_nat_prs_club_all WHERE id_nat_prs = "
				+ "(SELECT id_nat_prs FROM " + getGeneralDBScheme()
				+ ".vc_card_all " + "  WHERE card_serial_number = ? "
				+ "    AND id_issuer = ? " + "    AND id_payment_system = ?)";
		return getOneValueByParamId(mySQL, pParam);
	}*/

	public String getNatPrsName(String id_nat_prs) {
		return getOneValueByIntId2("SELECT full_name FROM "
				+ getGeneralDBScheme()
				+ ".vc_nat_prs_all WHERE id_nat_prs = ?", id_nat_prs);
	}

	public String getNatPrsNameFromRole(String id_nat_prs_role) {
		return getOneValueByIntId2("SELECT fio_nat_prs FROM "
				+ getGeneralDBScheme()
				+ ".vc_nat_prs_role_all WHERE id_nat_prs_role = ?", id_nat_prs_role);
	}

	/*public String getTermFirstServicePlace(String pIdTerm) {
		return getOneValueByIntId2("SELECT id_service_place FROM "
				+ getGeneralDBScheme() + ".vc_term_service_places_all"
				+ " WHERE id_term = ? AND ROWNUM <2 ", pIdTerm);
	}*/

	/*public boolean getTermAddServicePlacePermission(String pIdTerm,
			String pTermType) {
		if ("INTERNET_TERMINAL".equalsIgnoreCase(pTermType)) {
			return true;
		}
		String servicePlaceCount = getOneValueByIntId2("SELECT count(*) FROM "
				+ getGeneralDBScheme() + ".vc_term_service_places_all"
				+ " WHERE id_term = ? ", pIdTerm);
		if ("0".equalsIgnoreCase(servicePlaceCount)) {
			return true;
		} else {
			return false;
		}
	}*/

	public String getBKOperationSchemeLineName(String idBkOperationSchemeLine) {
		return getOneValueByIntId2(
				"SELECT oper_number FROM "
						+ getGeneralDBScheme()
						+ ".vc_oper_scheme_lines_club_all WHERE id_bk_operation_scheme_line = ?",
				idBkOperationSchemeLine);
	}

	public String getBKOperationSchemeLineAdditionalName(
			String idBkOperationSchemeLine) {
		return getOneValueByIntId2(
				"SELECT oper_number||' ('||debet_cd_bk_account_sh_line||' - '||credit_cd_bk_account_sh_line||')' name_scheme FROM "
						+ getGeneralDBScheme()
						+ ".vc_oper_scheme_lines_club_all WHERE id_bk_operation_scheme_line = ?",
				idBkOperationSchemeLine);
	}

	public String getLGTypeName(String cdLGType) {
		return getOneValueByIntId2(
				"SELECT name_lg_type FROM " + getGeneralDBScheme()
						+ ".vc_lg_type_all WHERE cd_lg_type = ?", cdLGType,
				lgType);
	}

	public String getNatPrsGroupName(String cd_group) {
		return getOneValueByStringId2(
				"SELECT name_nat_prs_group FROM "
						+ getGeneralDBScheme()
						+ ".vc_nat_prs_group_all WHERE UPPER(cd_nat_prs_group) = UPPER(?)",
				cd_group, natPrsGroup);
	}

	public String getClubCardOperationTypeName(String cd_type) {
		return getOneValueByStringId2(
				"SELECT name_card_operation_type FROM "
						+ getGeneralDBScheme()
						+ ".vc_card_operation_type WHERE UPPER(cd_card_operation_type) = UPPER(?)",
						cd_type, clubCardOperationType);
	}

	public String getClubCardOperationStateName(String cd_state) {
		return getOneValueByStringId2(
				"SELECT name_card_oper_state FROM "
						+ getGeneralDBScheme()
						+ ".vc_card_oper_state_all WHERE UPPER(cd_card_oper_state) = UPPER(?)",
						cd_state, clubCardOperationState);
	}

	public String getLGPromoterName(String id_promoter) {
		return getOneValueByIntId2("SELECT name_lg_promoter FROM "
				+ getGeneralDBScheme()
				+ ".vc_lg_promoter_club_all WHERE id_lg_promoter = ?",
				id_promoter);
	}

	public String getClubCardOperationTypeAction(String cd_task) {
		return getOneValueByStringId2(
				"SELECT app_card_resp_action FROM "
						+ getGeneralDBScheme()
						+ ".vc_card_operation_type WHERE UPPER(cd_card_operation_type) = UPPER(?)",
				cd_task, clubCardOperationTypeCardRespAction);
	}

	public String getTermSesCardReqState(String id_state) {
		return getOneValueByIntId2(
				"SELECT name_term_ses_creq_state FROM "
						+ getGeneralDBScheme()
						+ ".vc_term_ses_creq_state_all WHERE cd_term_ses_creq_state = ?",
				id_state, termSesCreqState);
	}

	public String getTermSesDataState(String id_state) {
		return getOneValueByIntId2(
				"SELECT name_term_ses_data_state FROM "
						+ getGeneralDBScheme()
						+ ".vc_term_ses_data_state_all WHERE cd_term_ses_data_state = ?",
				id_state, termSesDataState);
	}

	public String getTermSesParamState(String id_state) {
		return getOneValueByIntId2(
				"SELECT name_term_ses_param_state FROM "
						+ getGeneralDBScheme()
						+ ".vc_term_ses_param_state_all WHERE cd_term_ses_param_state = ?",
				id_state, termSesParamState);
	}

	public String getTermSesMonState(String id_state) {
		return getOneValueByIntId2("SELECT name_term_ses_mon_state FROM "
				+ getGeneralDBScheme()
				+ ".vc_term_ses_mon_state_all WHERE cd_term_ses_mon_state = ?",
				id_state, termSesMonState);
	}

	public String getTermDeviceTypeName(String id_device_type) {
		return getOneValueByIntId2("SELECT name_device_type FROM "
				+ getGeneralDBScheme()
				+ ".vc_term_device_type_names WHERE id_device_type = ?",
				id_device_type);
	}

	public String getSysERRTXDescription(String err_tx_code) {
		return getOneValueByStringId2("SELECT err_tx_desc FROM "
				+ getGeneralDBScheme()
				+ ".vc_sys_err_tx_code_all WHERE err_tx_code = UPPER(?)",
				err_tx_code, sysErrTXCode);
	}


	public String getTermSesForTelgr(String idTelgr) {
		return getOneValueByIntId2("SELECT id_term_ses FROM "
				+ getGeneralDBScheme()
				+ ".vc_telgr_club_all WHERE id_telgr = ?", idTelgr);
	}

	public String getJurPersonName(String jurPerson_id) {
		return getOneValueByIntId2("SELECT name_jur_prs FROM "
				+ getGeneralDBScheme()
				+ ".vc_jur_prs_short_all WHERE id_jur_prs = ?", jurPerson_id);
	}

	public String getJurPersonKindName(String cd_kind) {
		return getOneValueByStringId2("SELECT name_jur_prs_kind FROM "
				+ getGeneralDBScheme()
				+ ".vc_jur_prs_kind_all WHERE cd_jur_prs_kind = ?", cd_kind,
				jurPrsKind);
	}

	/*public String getJurPersonTypeName(String cd_type) {
		return getOneValueByStringId2("SELECT name_jur_prs_type FROM "
				+ getGeneralDBScheme()
				+ ".vc_jur_prs_type_all WHERE cd_jur_prs_type = ?", cd_type,
				jurPrsType);
	}*/

	public String getBKParticipantName(String cd_participant) {
		return getOneValueByStringId2("SELECT name_participant FROM "
				+ getGeneralDBScheme()
				+ ".vc_bk_participant WHERE cd_participant= ?", cd_participant);
	}

	public String getUserJurPrsPermissionCount(String id_user) {
		return getOneValueByIntId2(
				"SELECT count(*) FROM "
						+ getGeneralDBScheme()
						+ ".vc_user_jur_prs_priv_all WHERE id_user= ? AND has_permission='Y'",
				id_user);
	}

	public String getRoleReportsPermissionCount(String id_role) {
		return getOneValueByIntId2("SELECT count(*) FROM "
				+ getGeneralDBScheme()
				+ ".vc_report_privilege_all WHERE id_role= ?", id_role);
	}

	public String getSMSTypeName(String cd_type) {
		return getOneValueByStringId2("SELECT name_sms_message_type FROM "
				+ getGeneralDBScheme()
				+ ".vc_ds_sms_type WHERE cd_sms_message_type= ?", cd_type,
				dsSMSType);
	}

	public String getNatPrsPostName(String cd_post) {
		return getOneValueByStringId2("SELECT name_post FROM "
				+ getGeneralDBScheme() + ".vc_post_all WHERE cd_post= ?",
				cd_post);
	}

	public String getTermTypeName(String cd_term_type) {
		return getOneValueByStringId2("SELECT name_term_type FROM "
				+ getGeneralDBScheme()
				+ ".vc_term_type_names WHERE cd_term_type= ?", cd_term_type,
				termType);
	}

	public String getNatPrsRoleStateName(String cd_state) {
		return getOneValueByStringId2("SELECT name_nat_prs_role_state FROM "
				+ getGeneralDBScheme()
				+ ".vc_nat_prs_role_state_all WHERE cd_nat_prs_role_state= ?", cd_state,
				natPrsRoleState);
	}

	public String getRemittanceTypeName(String cd_type) {
		return getOneValueByStringId2("SELECT name_remittance_type FROM "
				+ getGeneralDBScheme()
				+ ".vc_remittance_type_all WHERE cd_remittance_type= ?",
				cd_type, remittanceType);
	}

	public String getBKOperationTypeConditionName(String cd_condition) {
		return getOneValueByStringId2("SELECT name_bk_condition FROM "
				+ getGeneralDBScheme()
				+ ".vc_bk_operation_type_cond WHERE cd_bk_operation_type= ?",
				cd_condition, bkOperationTypeCond);
	}

	public String getClubCardPurseTypeForPurseNumber(String id_card_purse) {
		return getOneValueByIntId2(
				"SELECT TO_CHAR(number_card_purse)||' - '||name_card_purse_type FROM "
						+ getGeneralDBScheme()
						+ ".vc_card_purse_all WHERE id_card_purse= ?",
				id_card_purse);
	}

	public String getClubCardPurseTypeName(String cd_card_purse_type) {
		return getOneValueByStringId2("SELECT name_card_purse_type FROM "
				+ getGeneralDBScheme()
				+ ".vc_card_purse_type_all WHERE cd_card_purse_type= ?",
				cd_card_purse_type, clubCardPurseType);
	}

	public String getBKOperationTypeAccountParticipantName(String cd_participant) {
		return getOneValueByStringId2(
				"SELECT name_bk_account_participant FROM "
						+ getGeneralDBScheme()
						+ ".vc_bk_operation_type_part WHERE cd_bk_account_participant= ?",
				cd_participant, bkOperationTypePart);
	}

	public String getBKDocTypeName(String cd_bk_doc_type) {
		return getOneValueByStringId2("SELECT name_bk_doc_type FROM "
				+ getGeneralDBScheme()
				+ ".vc_bk_doc_type WHERE cd_bk_doc_type= ?", cd_bk_doc_type,
				bkDocType);
	}

	public String getBKAccountSchemeLineName(String id_line) {
		return getOneValueByIntId2(
				"SELECT cd_bk_account_scheme_line||' - '||name_bk_account_scheme_line FROM "
						+ getGeneralDBScheme()
						+ ".vc_bk_account_sh_ln_club_all WHERE id_bk_account_scheme_line = ?",
				id_line);
	}

	public String getBKAccountSchemeLineCode(String id_scheme) {
		return getOneValueByIntId2(
				"SELECT cd_bk_account_scheme_line FROM "
						+ getGeneralDBScheme()
						+ ".vc_bk_account_sh_ln_club_all WHERE id_bk_account_scheme_line = ?",
				id_scheme);
	}

	public String getBKAccountName(String id_account) {
		return getOneValueByIntId2(
				"SELECT cd_bk_account||' '||name_bk_account FROM "
						+ getGeneralDBScheme()
						+ ".vc_bk_accounts_club_all WHERE id_bk_account = ?",
				id_account);
	}

	public String getClubJurPrsCardPackName(String id_card_pack) {
		return getOneValueByIntId2(
				"SELECT name_jur_prs_card_pack FROM "
						+ getGeneralDBScheme()
						+ ".vc_club_jp_card_pack_all WHERE id_jur_prs_card_pack = ?",
						id_card_pack);
	}

	public String getBankAccountNumber(String id_account) {
		return getOneValueByIntId2("SELECT number_bank_account FROM "
				+ getGeneralDBScheme()
				+ ".vc_bank_account_all WHERE id_bank_account = ?", id_account);
	}

	public String getBankAccountNumberAndName(String id_account) {
		return getOneValueByIntId2(
				"SELECT number_bank_account||' - '||name_bank_account_type FROM "
						+ getGeneralDBScheme()
						+ ".vc_bank_account_all WHERE id_bank_account = ?",
				id_account);
	}

	public String getComissionTypeName(String id_type) {
		return getOneValueByStringId2("SELECT name_comission_type FROM "
				+ getGeneralDBScheme()
				+ ".vc_comission_type_club_all WHERE id_comission_type = ?",
				id_type);
	}

	public String getPaymentSystemName(String id_pay_system) {
		return getOneValueByIntId2("SELECT name_payment_system FROM "
				+ getGeneralDBScheme()
				+ ".vc_payment_system_all WHERE id_payment_system = ?",
				id_pay_system);
	}

	public String getBKAccountCdAndName(String id_account) {
		return getOneValueByIntId2(
				" SELECT CASE WHEN LENGTH(cd_bk_account||' '||name_bk_account)>50 "
						+ " 			  THEN substr(cd_bk_account||' '||name_bk_account,1,47)||'...' "
						+ "             ELSE cd_bk_account||' '||name_bk_account "
						+ "        END " + "  FROM " + getGeneralDBScheme()
						+ ".v_bk_accounts_all " + " WHERE id_bk_account = ?",
				id_account);
	}

	public String getBKAccountCd(String id_account) {
		return getOneValueByIntId2("SELECT cd_bk_account FROM "
				+ getGeneralDBScheme()
				+ ".v_bk_accounts_all WHERE id_bk_account = ?", id_account);
	}

	public String getSystemParamValue(String cd_param) {
		return getOneValueByStringId2("SELECT value_param FROM "
				+ getGeneralDBScheme() + ".v_system_param WHERE cd_param = ?",
				cd_param);
	}

	public String getLoySchemeCdAndName(String id_scheme) {
		return getOneValueByIntId2(
				"SELECT name_loyality_scheme "
						+ "  FROM " + getGeneralDBScheme()
						+ ".vc_loyality_scheme_club_all "
						+ " WHERE id_loyality_scheme = ?", id_scheme);
	}

	public String getLoySchemeName(String id_scheme) {
		return getOneValueByIntId2("SELECT name_loyality_scheme FROM "
				+ getGeneralDBScheme()
				+ ".vc_loyality_scheme_club_all WHERE id_loyality_scheme = ?",
				id_scheme);
	}

	public String getLoySchemeCd(String id_scheme) {
		return getOneValueByIntId2("SELECT name_loyality_scheme FROM "
				+ getGeneralDBScheme()
				+ ".vc_loyality_scheme_club_all WHERE id_loyality_scheme = ?",
				id_scheme);
	}

	public String getLoyScheduleName(String id_schedule) {
		return getOneValueByIntId2("SELECT name_shedule FROM "
				+ getGeneralDBScheme()
				+ ".v_ls_shedule_name_club_all WHERE id_shedule = ?",
				id_schedule);
	}

	public String getNatPrsGiftRequestTypeName(String cd_type) {
		return getOneValueByStringId2(
				"SELECT name_nat_prs_gift_request_type FROM "
						+ getGeneralDBScheme()
						+ ".vc_nat_prs_gift_request_tp_all WHERE cd_nat_prs_gift_request_type = ?",
				cd_type, jurPrsGiftRequestType);
	}

	public String getNatPrsGiftRequestStateName(String cd_state) {
		return getOneValueByStringId2(
				"SELECT nm_nat_prs_gift_request_state FROM "
						+ getGeneralDBScheme()
						+ ".vc_nat_prs_gift_request_st_all WHERE cd_nat_prs_gift_request_state = ?",
				cd_state, jurPrsGiftRequestState);
	}

	public String getMenuName(String id_menu) {
		return getOneValueByIntId2("SELECT name_menu_element FROM "
				+ getGeneralDBScheme()
				+ ".vc_user_menu_and_tabsheet_all WHERE id_menu_element = ?",
				id_menu);
	}

	public String getLGPromoterPostName(String cd_post) {
		return getOneValueByStringId2("SELECT name_lg_promoter_post FROM "
				+ getGeneralDBScheme()
				+ ".vc_lg_promoter_post_all WHERE cd_lg_promoter_post = ?",
				cd_post);
	}

	public String getLGPromoterStateName(String cd_state) {
		return getOneValueByStringId2("SELECT name_lg_promoter_state FROM "
				+ getGeneralDBScheme()
				+ ".vc_lg_promoter_state_all WHERE cd_lg_promoter_state = ?",
				cd_state, lgPromoterState);
	}

	public String getDocName(String id_doc) {
		return getOneValueByIntId2("SELECT full_doc FROM "
				+ getGeneralDBScheme() + ".vc_doc_priv_all WHERE id_doc = ?",
				id_doc);
	}

	public String getNatPrsGiftStateName(String cd_state) {
		return getOneValueByStringId2("SELECT name_nat_prs_gift_state FROM "
				+ getGeneralDBScheme()
				+ ".vc_nat_prs_gift_state_all WHERE cd_nat_prs_gift_state = ?",
				cd_state, natPrsGiftState);
	}

	public String getClubActionName(String id_event) {
		return getOneValueByIntId2("SELECT name_club_event FROM "
				+ getGeneralDBScheme()
				+ ".vc_club_event_all WHERE id_club_event = ?", id_event);
	}

	public String getDSEmailProfileName(String id_email_profile) {
		return getOneValueByIntId2("SELECT name_email_profile FROM "
				+ getGeneralDBScheme()
				+ ".vc_ds_email_profile_club_all WHERE id_email_profile = ?",
				id_email_profile);
	}

	public String getClubName(String id_club) {
		if ("-1".equalsIgnoreCase(id_club)) {
			return this.clubXML.getfieldTransl("h_out_of_clubs", false).toUpperCase();
		} else {
			return getOneValueByIntId2(
					"SELECT name_club FROM " + getGeneralDBScheme()
							+ ".vc_club_names WHERE id_club = ?", id_club);
		}
	}

	public String getClubShortName(String id_club) {
		if ("-1".equalsIgnoreCase(id_club)) {
			return this.clubXML.getfieldTransl("h_out_of_clubs", false).toUpperCase();
		} else {
			return getOneValueByIntId2(
					"SELECT sname_club FROM " + getGeneralDBScheme()
							+ ".vc_club_names WHERE id_club = ?", id_club);
		}
	}

	public String getJurPersonShortName(String jurPerson_id) {
		return getOneValueByIntId2("SELECT sname_jur_prs FROM "
				+ getGeneralDBScheme()
				+ ".vc_jur_prs_short_all WHERE id_jur_prs = ?", jurPerson_id);
	}

	public String getTargetPrgName(String id_target_prg) {
		return getOneValueByIntId2("SELECT name_target_prg FROM "
				+ getGeneralDBScheme()
				+ ".vc_target_prg_all WHERE id_target_prg = ?", id_target_prg);
	}

	public String getDispatchKindName(String cd_kind) {
		return getOneValueByStringId2("SELECT name_dispatch_kind FROM "
				+ getGeneralDBScheme()
				+ ".vc_ds_dispatch_kind_all WHERE cd_dispatch_kind = ?",
				cd_kind, dsDispatchKind);
	}

	public String getCardStateName(String id_card_state) {
		return getOneValueByIntId2("SELECT name_card_state FROM "
				+ getGeneralDBScheme()
				+ ".vc_card_state WHERE id_card_state = ?", id_card_state,
				clubCardState);
	}

	public String getOfficePrivateShortName(String id_office_private) {
		return getOneValueByIntId2("SELECT sname_office_private FROM "
				+ getGeneralDBScheme()
				+ ".vc_office_private_all WHERE id_office_private = ?",
				id_office_private);
	}

	public String getCardCategoryName(String id_category) {
		return getOneValueByIntId2("SELECT name_category_name FROM "
				+ getGeneralDBScheme()
				+ ".vc_card_category_name_all WHERE id_category_name = ?",
				id_category);
	}

	public String getContactPrsName(String id_contact_prs) {
		return getOneValueByIntId2("SELECT name_contact_prs FROM "
				+ getGeneralDBScheme()
				+ ".vc_contact_prs_priv_all WHERE id_contact_prs = ?",
				id_contact_prs);
	}
	
	public String getTermPayConfirmationWayName(String cd_name) {
		return getOneValueByStringId2("SELECT name_term_pay_confirm_way FROM "
				+ getGeneralDBScheme()
				+ ".vc_term_pay_confirm_way_all WHERE cd_term_pay_confirm_way = ?",
				cd_name);
	}

	public String getDSSMSProfileName(String id_profile) {
		return getOneValueByIntId2("SELECT name_sms_profile_full FROM "
				+ getGeneralDBScheme()
				+ ".vc_ds_sms_profile_club_all WHERE id_sms_profile = ?",
				id_profile);
	}

	public String getCardCategoryName2(String id_category) {
		return getOneValueByIntId2("SELECT name_category FROM "
				+ getGeneralDBScheme()
				+ ".vc_card_category_club_all WHERE id_category = ?",
				id_category);
	}

	public String getClubCardCode(
			String pCardSerialNumber_IdIssuer_IdPaymentSystem) {
		return getOneValueByStringId2(
				" SELECT cd_card1 "
						+ "   FROM "
						+ getGeneralDBScheme()
						+ ".vc_card_all "
						+ "  WHERE card_serial_number||'_'||id_issuer||'_'||id_payment_system = ?",
				pCardSerialNumber_IdIssuer_IdPaymentSystem);
	}

	public String getBCUserName(String id_user) {
		return getOneValueByIntId2("SELECT name_user FROM "
				+ getGeneralDBScheme() + ".vc_users_all WHERE id_user = ?",
				id_user);
	}

	public String getBCUserNatPrs(String id_user) {
		return getOneValueByIntId2("SELECT name_user||(CASE WHEN fio_nat_prs IS NULL THEN '' ELSE ' ('||fio_nat_prs||')' END) name_user FROM "
				+ getGeneralDBScheme() + ".vc_users_all WHERE id_user = ?",
				id_user);
	}

	public String getCallCenterInquirerName(String id_inqurer) {
		return getOneValueByIntId2("SELECT name_cc_inquirer FROM "
				+ getGeneralDBScheme()
				+ ".vc_cc_inquirer_club_all WHERE id_cc_inquirer = ?",
				id_inqurer);
	}

	public String getDispatchPatternTypeName(String cd_pattern_type) {
		return getOneValueByStringId2("SELECT name_pattern_type FROM "
				+ getGeneralDBScheme()
				+ ".vc_ds_pattern_type_all WHERE cd_pattern_type = ?",
				cd_pattern_type, dsPatternType);
	}

	public String getFAQTitle(String id_cc_faq) {
		return getOneValueByIntId2(
				"SELECT title_cc_faq FROM "
						+ getGeneralDBScheme()
						+ ".vc_cc_faq_club_all WHERE id_cc_faq = ? AND exist_flag = 'Y'",
				id_cc_faq);
	}

	public String getDSProfileStateName(String cd_profile_state) {
		return getOneValueByStringId2("SELECT name_profile_state FROM "
				+ getGeneralDBScheme()
				+ ".vc_ds_profile_state_all WHERE cd_profile_state = ?",
				cd_profile_state, dsProfileState);
	}

	public String getDSPatternName(String id_pattern) {
		return getOneValueByIntId2("SELECT name_ds_pattern FROM "
				+ getGeneralDBScheme()
				+ ".vc_ds_pattern_club_all WHERE id_ds_pattern = ?", id_pattern);
	}

	public String getCallCenterUserName(String id_user) {
		return getOneValueByIntId2("SELECT name_user FROM "
				+ getGeneralDBScheme() + ".vc_cc_users_all WHERE id_user = ?",
				id_user);
	}

	public String getDTTaskName(String id_task) {
		return getOneValueByIntId2("SELECT name_task FROM "
				+ getGeneralDBScheme() + ".vc_dt_task_all WHERE id_task = ?",
				id_task);
	}

	public String getSMSPatternCountry(String id_ds_pattern) {
		return getOneValueByIntId2("SELECT sms_code_country FROM "
				+ getGeneralDBScheme()
				+ ".vc_ds_pattern_club_all WHERE id_ds_pattern = ?",
				id_ds_pattern + "");
	}

	public String getTermStatusName(String cd_term_status) {
		return getOneValueByStringId2("SELECT name_term_status FROM "
				+ getGeneralDBScheme()
				+ ".vc_term_status_names WHERE cd_term_status = ?",
				cd_term_status);
	}

	public String getTermLoyalityHistoryName(String id_loyality_history) {
		return getOneValueByIntId2(
				"SELECT date_beg_frmt||' - '||date_end_frmt FROM "
						+ getGeneralDBScheme()
						+ ".vc_term_loyality_h_club_all WHERE id_loyality_history = ?",
				id_loyality_history);
	}

	public String getClubRelationshipName(String id_club_rel) {
		return getOneValueByIntId2("SELECT full_name_club_rel FROM "
				+ getGeneralDBScheme()
				+ ".vc_club_rel_club_all WHERE id_club_rel = ?", id_club_rel);
	}

	public String getServicePlaceName(String id_service_place) {
		return getOneValueByIntId2("SELECT name_jur_prs name_service_place FROM "
				+ getGeneralDBScheme()
				+ ".vc_jur_prs_all WHERE id_jur_prs = ?",
				id_service_place);
	}

	public String getServicePlaceShortName(String id_service_place) {
		return getOneValueByIntId2("SELECT sname_jur_prs sname_service_place FROM "
				+ getGeneralDBScheme()
				+ ".vc_jur_prs_all WHERE id_jur_prs = ?",
				id_service_place);
	}

	public String getBKBankAccountTypeName(String cd_name) {
		return getOneValueByStringId2("SELECT name_bk_bank_account_type FROM "
				+ getGeneralDBScheme()
				+ ".vc_bk_bank_account_type WHERE cd_bk_bank_account_type = ?",
				cd_name, bkBankAccountType);
	}

	public String getGiftName(String id_gift) {
		return getOneValueByIntId2(
				"SELECT name_gift FROM " + getGeneralDBScheme()
						+ ".vc_gifts_club_all WHERE id_gift = ?", id_gift);
	}

	public String getReportId(String cd_report) {
		return getOneValueByStringId2("SELECT id_report FROM "
				+ getGeneralDBScheme() + ".v_reports_all WHERE cd_report = ?",
				cd_report);
	}

	public String getJurPrsComissionNeedCount(String id_bank) {
		String returnValue = getOneValueByStringId2(
				"SELECT "
						+ getGeneralDBScheme()
						+ ".PACK_UI_COMISSION.get_need_jur_prs_comis_count(?,NULL) FROM dual",
				id_bank);
		return isEmpty(returnValue)?"0":returnValue;
	}

	public String getComisTypeComissionNeedCount(String id_comis_type) {
		String returnValue = getOneValueByStringId2(
				" SELECT "
						+ getGeneralDBScheme()
						+ ".PACK_UI_COMISSION.get_need_comis_type_count(?,NULL) FROM dual",
				id_comis_type);
		return isEmpty(returnValue)?"0":returnValue;
	}

	public String getComisTypeComissionCreatedCount(String id_comis_type) {
		String returnValue = getOneValueByStringId2(" SELECT " + getGeneralDBScheme()
				+ ".PACK$COMIS_TYPE_UI.get_comission_count(?) FROM dual",
				id_comis_type);
		return isEmpty(returnValue)?"0":returnValue;
	}

	public String getClubComissionNeedCount(String id_club, String pIsIssuer,
			String pIsTechAcquirer) {
		String returnValue = getOneValueByStringId2(" SELECT " + getGeneralDBScheme()
				+ ".PACK$CLUB_UI.get_not_created_com_count(?,NULL) FROM dual",
				id_club);
		return isEmpty(returnValue)?"0":returnValue;
	}

	public String getJurPrsComissionNeedCount(String id_jur_prs,
			String pIsIssuer, String pIsTechAcquirer) {
		String returnValue = getOneValueByStringId2(
				" SELECT "
						+ getGeneralDBScheme()
						+ ".PACK$JUR_PRS_UI.get_not_created_com_count(?,NULL) FROM dual",
				id_jur_prs);
		return isEmpty(returnValue)?"0":returnValue;
	}

	public String getClubRelTypeName(String cd_club_rel_type) {
		return getOneValueByStringId2("SELECT name_club_rel_type FROM "
				+ getGeneralDBScheme()
				+ ".vc_club_rel_type_all WHERE cd_club_rel_type = ?",
				cd_club_rel_type, clubRelType);
	}

	public String getCalculatorExpression(String idScheme, String fieldName) {
		String mySQL = "{? = call " + getGeneralDBScheme()
				+ ".PACK_UI_CALCULATOR.get_prepared_expression(?,?,?,?)}";

		String[] results = new String[3];

		String[] pParam = new String[2];
		pParam[0] = idScheme;
		pParam[1] = fieldName;

		LOGGER.debug(prepareSQLToLog(mySQL, pParam));
		results = myCallFunctionParam(mySQL, pParam, 3);

		String resultInt = results[2];
		String resultMessage = results[1];
		String resultString = results[0];

		if (!"0".equalsIgnoreCase(resultInt)) {
			resultString = resultString + ": " + resultInt + ": "
					+ resultMessage;
		}
		return resultString;
	}

	public String getCalculatorRelationShipExpression(
			String id_club_rel_oper_scheme, String fieldName) {
		String[] pParam = new String[1];
		pParam[0] = id_club_rel_oper_scheme;

		String mySQL = "{? = call " + getGeneralDBScheme()
				+ ".PACK_UI_CALCULATOR.get_rel_prepared_expression(?,?,?)}";

		String[] results = new String[2];

		LOGGER.debug(prepareSQLToLog(mySQL, pParam));
		results = myCallFunctionParam(mySQL, pParam, 3);

		String resultInt = results[2];
		String resultMessage = results[1];
		String resultString = results[0];

		if (!"0".equalsIgnoreCase(resultInt)) {
			resultString = resultString + ": " + resultInt + ": "
					+ resultMessage;
		}
		return resultString;
	}

	public String getComissionTypeArray(String cdClubRelType,
			String pElementName) {
		ArrayList<bcFeautureParam> pParam = initParamArray();
		pParam.add(new bcFeautureParam("none", ""));

		String mySQL = "SELECT cd_comission_type, name_comission_type "
				+ "  FROM (SELECT b.cd_club_rel_type||'.'||b.cd_comission_type cd_comission_type, "
				+ "       		   b.name_club_rel_type||'.'||b.name_comission_type name_comission_type "
				+ "  		  FROM " + getGeneralDBScheme()
				+ ".vc_comission_type_club_all b";
		if (!isEmpty(cdClubRelType)) {
			mySQL = mySQL + " WHERE b.cd_club_rel_type = ? ";
			pParam.add(new bcFeautureParam("string", cdClubRelType));
		}
		mySQL = mySQL + "         ) " + "  ORDER BY name_comission_type";
		StringBuilder return_value = new StringBuilder();
		PreparedStatement st = null;
		Connection con = null;
		int i = 0;
		try {
			LOGGER.debug(prepareSQLToLog(mySQL, pParam));
			con = Connector.getConnection(sessionId);
			st = con.prepareStatement(mySQL);
			st = prepareParam(st, pParam);
			ResultSet rset = st.executeQuery();

			while (rset.next()) {
				return_value.append(pElementName + "[" + i + "]=\""
						+ rset.getString(2) + "\"; ");
				i = i + 1;
			}
		} catch (SQLException e) {
			LOGGER.debug("bcCRMBean.getComissionTypeArray() SQLException: "
					+ e.toString());
			return_value.append("<option value=\"\">SQL Error</option>");
		} catch (Exception el) {
			LOGGER.debug("bcCRMBean.getComissionTypeArray() Exception: "
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
		} // finally
		return return_value.toString();
	}

	public String getSMSRegionOblastList(String pLanguage) {

		StringBuilder html = new StringBuilder();
		boolean isError = false;

		ArrayList<String> regionId = new ArrayList<String>();
		ArrayList<String> regionName = new ArrayList<String>();

		PreparedStatement stRegion = null;
		Connection con = null;

		try {
			String regionSQL = " SELECT id_sms_region, name_sms_region "
					+ "   FROM " + getGeneralDBScheme()
					+ ".vc_ds_sms_region_all "
					+ "  WHERE UPPER(sms_language) = UPPER(?) "
					+ "  ORDER BY name_sms_region";

			con = Connector.getConnection(getSessionId());
			LOGGER.debug(regionSQL + ", 1={'" + pLanguage + "',string}");
			stRegion = con.prepareStatement(regionSQL);
			stRegion.setString(1, pLanguage);
			ResultSet rset = stRegion.executeQuery();

			while (rset.next()) {
				regionId.add(rset.getString("ID_SMS_REGION"));
				regionName.add(rset.getString("NAME_SMS_REGION"));
			}
			stRegion.close();
		} catch (SQLException e) {
			LOGGER.error("bcCRMBean.getSMSRegionOblastList SQLException: "
					+ e.toString());
			isError = true;
		} catch (Exception el) {
			LOGGER.error("bcCRMBean.getSMSRegionOblastList Exception: "
					+ el.toString());
			isError = true;
		} finally {
			try {
				if (stRegion != null)
					stRegion.close();
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
		} // finally

		if (isError) {
			return html.toString();
		}

		ArrayList<String> regionOblastId = new ArrayList<String>();
		ArrayList<String> oblastId = new ArrayList<String>();
		ArrayList<String> oblastName = new ArrayList<String>();

		PreparedStatement stOblast = null;
		try {
			String oblastSQL = " SELECT id_sms_region, id_oblast, name_oblast "
					+ "   FROM " + getGeneralDBScheme()
					+ ".vc_ds_sms_region_oblast_all "
					+ "  WHERE UPPER(sms_language) = UPPER(?) "
					+ "  ORDER BY name_oblast";

			LOGGER.debug(oblastSQL + ", 1={'" + pLanguage + "',string}");
			con = Connector.getConnection(getSessionId());
			stOblast = con.prepareStatement(oblastSQL);
			stOblast.setString(1, pLanguage);
			ResultSet rset = stOblast.executeQuery(oblastSQL);

			while (rset.next()) {
				regionOblastId.add(rset.getString("ID_SMS_REGION"));
				oblastId.add(rset.getString("ID_OBLAST"));
				oblastName.add(rset.getString("NAME_OBLAST"));
			}
			stOblast.close();
		} catch (SQLException e) {
			LOGGER.debug("bcCRMBean.getSMSRegionOblastList SQLException: "
					+ e.toString());
			isError = true;
		} catch (Exception el) {
			LOGGER.debug("bcCRMBean.getSMSRegionOblastList Exception: "
					+ el.toString());
			isError = true;
		} finally {
			try {
				if (stOblast != null)
					stOblast.close();
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
		} // finally

		if (isError) {
			return html.toString();
		}

		if (regionId.size() > 0) {
			for (int counter = 0; counter < regionId.size(); counter++) {
				if (counter == 0) {
					html.append("<b>" + regionName.get(counter) + "</b>: ");
				} else {
					html.append("<br><b>" + regionName.get(counter) + "</b>: ");
				}
				if (regionOblastId.size() > 0) {
					int foundOblast = 0;
					for (int counter2 = 0; counter2 < regionOblastId.size(); counter2++) {
						if (regionOblastId.get(counter2).equalsIgnoreCase(
								regionId.get(counter))) {
							foundOblast = foundOblast + 1;
							if (foundOblast == 1) {
								html.append(oblastName.get(counter2));
							} else {
								html.append(", " + oblastName.get(counter2));
							}
						}
					}
				}
			}
		}
		return html.toString();
	}

	public String getImgPs(String pCurrentSegment, String pSegmentName,
			String pSegmentValue, String pSegmentParticipang, String pHyperLink) {
		String return_value = "";
		String pImage = "";
		String pButton = "";
		if ("".equalsIgnoreCase(pCurrentSegment)) {
			if ("X".equalsIgnoreCase(pSegmentValue) && !isEmpty(pSegmentParticipang)) {
				pImage = "info3.png";
				pButton = "button_edit";
			} else {
				pImage = "info2.png";
				pButton = "button_add";
			}

			return_value = "<input type=\"text\" name=\"" + pSegmentName
					+ "\" size=\"5\" value=\"" + pSegmentValue
					+ "\" class=\"inputfield\"";
			if (!isEmpty(pSegmentParticipang)) {
				return_value = return_value + " title = \""
						+ getBKParticipantName(pSegmentParticipang) + "\" ";
			}
			return_value = return_value
					+ "><img class=\"img_ps\" vspace=\"0\" hspace=\"0\" src=\""
					+ "../images/oper/rows/" + pImage
					+ "\" align=\"top\" title=\""
					+ buttonXML.getfieldTransl(pButton, false)
					+ "\" onclick=\"ajaxpage('" + pHyperLink
					+ "', 'div_main')\">";
		} else {
			return_value = "<input type=\"text\" name=\""
					+ pSegmentName
					+ "\" size=\"5\" value=\""
					+ pSegmentValue
					+ "\" readonly class=\"inputfield-ro\"><img class=\"img_ps\" vspace=\"0\" hspace=\"0\" src=\""
					+ "../images/oper/rows/info2.gif\" align=\"top\">";
		}
		return return_value;
	}

	public String getBKAccountSegment(String pName, String pValue,
			String pParticipant) {
		StringBuilder html = new StringBuilder();
		String lTitle = "";

		if (!isEmpty(pParticipant)) {
			if ("OPERATOR".equalsIgnoreCase(pParticipant)
					|| "ISSUER".equalsIgnoreCase(pParticipant)
					|| "FINANCE_ACQUIRER".equalsIgnoreCase(pParticipant)
					|| "TECHNICAL_ACQUIRER".equalsIgnoreCase(pParticipant)
					|| "DEALER".equalsIgnoreCase(pParticipant)) {
				lTitle = getJurPersonShortName(pValue);
			} else if ("CLIENT".equalsIgnoreCase(pParticipant)) {
				lTitle = getNatPrsName(pValue);
			} else if ("CARD".equalsIgnoreCase(pParticipant)) {
				lTitle = getClubCardXMLFieldTransl("cd_card1", false);
			}
		}
		html.append("<input type=\"text\" name=\"" + pName
				+ "\" size=\"5\" value=\"" + pValue
				+ "\" class=\"inputfield\" title=\"" + lTitle + "\">");
		if (!isEmpty(pParticipant)) {
			html.append("<img vspace=\"0\" hspace=\"0\" src=\""
					+ "../images/oper/rows/info3.png\" align=\"top\" style=\"border: 0px;\">");
		}

		return html.toString();
	}
	
	private String getImgChange() {
		StringBuilder html = new StringBuilder();
		html.append("<img vspace=\"0\" hspace=\"0\" src=\""
				+ "../images/oper/window_open/change.png\" align=\"top\" style=\"border:0px; margin-left:-3px;\" title=\""
				+ buttonXML.getfieldTransl("button_edit", false)
				+ "\"></a>\n");
		return html.toString();
	}
	
	private String getImgDelete() {
		StringBuilder html = new StringBuilder();
		html.append("<img vspace=\"0\" hspace=\"0\" src=\""
				+ "../images/oper/window_open/delete.png\" align=\"top\" style=\"border:0px; margin-left:-3px;\" title=\""
				+ buttonXML.getfieldTransl("button_delete",
						false) + "\"></a>\n");
		return html.toString();
	}

	public String getWindowFindJurPrs(String pField, String pValue,
			String pType, String pSize) {
		StringBuilder html = new StringBuilder();
		String pName = getJurPersonShortName(pValue);

		html.append("<input type=\"hidden\" id=\"id_" + pField
				+ "\" name=\"id_" + pField + "\" value=\"" + pValue + "\">\n");
		html.append("<input type=\"text\" id=\"name_" + pField
				+ "\" name=\"name_" + pField + "\" size=\"" + pSize
				+ "\" value=\"" + pName
				+ "\" readonly class=\"inputfield-ro\" title=\"" + pName + "\">\n");
		html.append("<A HREF=\"#\" onClick=\"JavaScript: var cWin=window.open('"
				+ "../crm/services/findjurprs.jsp?id_jur_prs='+document.getElementById('id_"
				+ pField
				+ "').value+'&field="
				+ pField
				+ "&type="
				+ pType
				+ "','blank','height=600,width=900,top=50,left=150,toolbar=no,menubar=no,location=no,scrollbars=yes,status=yes'); cWin.focus();\">\n");
		html.append(getImgChange());
		html.append("<A HREF=\"#\" onClick=\"JavaScript: document.getElementById('id_"
				+ pField
				+ "').value = ''; document.getElementById('name_"
				+ pField + "').value = ''; return false;\">\n");
		html.append(getImgDelete());

		return html.toString();
	}

	public String getWindowFindJurPrs(String pField, String pValue,
			String pName, String pType, String pSize) {
		return getWindowFindJurPrs(pField, pValue, pName, pType, pSize, false);
	}

	public String getWindowFindJurPrs(String pField, String pValue,
			String pName, String pType, String pSize, boolean pReadOnly) {
		StringBuilder html = new StringBuilder();
		
		String lNameJurPrs = pName;
		if (isEmpty(pName) && !isEmpty(pValue)) {
			lNameJurPrs = getJurPersonShortName(pValue);
		}
		if (pReadOnly) {
			html.append("<input type=\"hidden\" id=\"id_" + pField
					+ "\" name=\"id_" + pField + "\" value=\"" + pValue + "\">\n");
			html.append("<input type=\"text\" id=\"name_" + pField
					+ "\" name=\"name_" + pField + "\" size=\"" + (Integer.parseInt(pSize)+12)
					+ "\" value=\"" + lNameJurPrs
					+ "\" readonly class=\"inputfield-ro\" title=\"" + lNameJurPrs
					+ "\">\n");
		} else {
			html.append("<input type=\"hidden\" id=\"id_" + pField
				+ "\" name=\"id_" + pField + "\" value=\"" + pValue + "\">\n");
			html.append("<input type=\"text\" id=\"name_" + pField
				+ "\" name=\"name_" + pField + "\" size=\"" + pSize
				+ "\" value=\"" + lNameJurPrs
				+ "\" readonly class=\"inputfield-ro\" title=\"" + lNameJurPrs
				+ "\">\n");
			html.append("<A HREF=\"#\" onClick=\"JavaScript: var cWin=window.open('"
				+ "../crm/services/findjurprs.jsp?id_jur_prs='+document.getElementById('id_"
				+ pField
				+ "').value+'&field="
				+ pField
				+ "&type="
				+ pType
				+ "','blank','height=600,width=900,top=50,left=150,toolbar=no,menubar=no,location=no,scrollbars=yes,status=yes'); cWin.focus();\">\n");
			html.append(getImgChange());
			html.append("<A HREF=\"#\" onClick=\"JavaScript: document.getElementById('id_"
				+ pField
				+ "').value = ''; document.getElementById('name_"
				+ pField + "').value = ''; return false;\">\n");
			html.append(getImgDelete());
		}

		return html.toString();
	}

	public String getWindowFindJurAndNatPrs(String pValue,
			String pFindType, String pExistType, String pFindEntryId, String pSize) {
		StringBuilder html = new StringBuilder();
		String pName = "";
		if (!isEmpty(pValue)) {
			if ("NAT_PRS".equalsIgnoreCase(pExistType)) {
				pName = getNatPrsName(pValue) + " (" + commonXML.getfieldTransl("entry_type_nat_prs", false).toLowerCase() + ")";
			} else {
				pName = getJurPersonShortName(pValue) + " (" + commonXML.getfieldTransl("entry_type_jur_prs", false).toLowerCase() + ")";
			}
		}

		if ("NAT_PRS".equalsIgnoreCase(pExistType)) {
			html.append("<input type=\"hidden\" id=\"id_nat_prs\" name=\"id_nat_prs\" value=\"" + pValue + "\">\n");
			html.append("<input type=\"hidden\" id=\"id_jur_prs\" name=\"id_jur_prs\" value=\"\">\n");
			html.append("<input type=\"hidden\" id=\"type_entry\" name=\"type_entry\" value=\"NAT_PRS\">\n");
		} else {
			html.append("<input type=\"hidden\" id=\"id_nat_prs\" name=\"id_nat_prs\" value=\"\">\n");
			html.append("<input type=\"hidden\" id=\"id_jur_prs\" name=\"id_jur_prs\" value=\"" + pValue + "\">\n");
			html.append("<input type=\"hidden\" id=\"type_entry\" name=\"type_entry\" value=\"JUR_PRS\">\n");
		}
		html.append("<input type=\"hidden\" id=\"id_entry\" name=\"id_entry\" value=\"" + pValue + "\">\n");
		html.append("<input type=\"text\" id=\"name_entry\" name=\"name_entry\" size=\"" + pSize
				+ "\" value=\"" + pName
				+ "\" readonly class=\"inputfield-ro\" title=\"" + pName + "\">\n");
		html.append("<A HREF=\"#\" onClick=\"JavaScript: var cWin=window.open('"
				+ "../crm/services/findjurandnatprs.jsp?id='+document.getElementById('id_entry').value+'&findtype="
				+ pFindType
				+ "&existtype="
				+ pExistType
				+ "&typeid="
				+ pFindEntryId
				+ "','blank','height=600,width=900,top=50,left=150,toolbar=no,menubar=no,location=no,scrollbars=yes,status=yes'); cWin.focus();\">\n");
		html.append(getImgChange());
		html.append("<A HREF=\"#\" onClick=\"JavaScript: document.getElementById('id_nat_prs').value = ''; " + 
				"document.getElementById('id_jur_prs').value = '';  " + 
				"document.getElementById('type_entry').value = '';  " + 
				"document.getElementById('id_entry').value = '';  " + 
				"document.getElementById('name_entry').value = ''; return false;\">\n");
		html.append(getImgDelete());

		return html.toString();
	}

	public String getWindowFindJurAndNatPrs(String pValue,
			String pFindType, String pExistType, String pSize) {
		return getWindowFindJurAndNatPrs(pValue, pFindType, pExistType, "", pSize);
	}

	public String getWindowFindServicePlace(String pField, String pValue,
			String pSize) {
		return getWindowFindServicePlace(pField, pValue, "", "", "", pSize);
	}

	public String getWindowFindServicePlace(String pField, String pValue,
			String pNameServicePlace, String pSize) {
		return getWindowFindServicePlace(pField, pValue, pNameServicePlace, "", "", pSize);
	}

	public String getWindowFindServicePlace(String pField, String pValue, String pNameServicePlace, 
			String pJurPrs, String pTerm, String pSize) {
		StringBuilder html = new StringBuilder();
		String lName = isEmpty(pNameServicePlace)?getServicePlaceName(pValue):pNameServicePlace;

		html.append("<input type=\"hidden\" id=\"id_" + pField
				+ "\" name=\"id_" + pField + "\" value=\"" + pValue + "\">\n");
		html.append("<input type=\"text\" id=\"name_" + pField
				+ "\" name=\"name_" + pField + "\" size=\"" + pSize
				+ "\" value=\"" + lName
				+ "\" readonly class=\"inputfield-ro\" title=\"" + lName + "\">\n");
		html.append("<A HREF=\"#\" onClick=\"JavaScript: var cWin=window.open('"
				+ "../crm/services/findserviceplaces.jsp?id_service_place='+document.getElementById('id_"
				+ pField
				+ "').value+'&id_jur_prs="
				+ pJurPrs
				+ "&id_term="
				+ pTerm
				+ "&field="
				+ pField
				+ "','blank','height=600,width=900,top=50,left=150,toolbar=no,menubar=no,location=no,scrollbars=yes,status=yes'); cWin.focus();\">\n");
		html.append(getImgChange());
		html.append("<A HREF=\"#\" onClick=\"JavaScript: document.getElementById('id_"
				+ pField
				+ "').value = ''; document.getElementById('name_"
				+ pField + "').value = ''; return false;\">\n");
		html.append(getImgDelete());

		return html.toString();
	}

	public String getWindowFindQuestionnairePack(String pField, String pValue,
			String pSize) {
		StringBuilder html = new StringBuilder();
		String pName = pValue;

		html.append("<input type=\"hidden\" id=\"id_" + pField
				+ "\" name=\"id_" + pField + "\" value=\"" + pValue + "\">\n");
		html.append("<input type=\"text\" id=\"name_" + pField
				+ "\" name=\"name_" + pField + "\" size=\"" + pSize
				+ "\" value=\"" + pName
				+ "\" readonly class=\"inputfield-ro\" title=\"" + pName + "\">\n");
		html.append("<A HREF=\"#\" onClick=\"JavaScript: var cWin=window.open('"
				+ "../crm/services/findquestpack.jsp?id_quest_pack='+document.getElementById('id_"
				+ pField
				+ "').value,'blank','height=600,width=900,top=50,left=150,toolbar=no,menubar=no,location=no,scrollbars=yes,status=yes'); cWin.focus();\">\n");
		html.append(getImgChange());
		html.append("<A HREF=\"#\" onClick=\"JavaScript: document.getElementById('id_"
				+ pField
				+ "').value = ''; document.getElementById('name_"
				+ pField + "').value = ''; return false;\">\n");
		html.append(getImgDelete());

		return html.toString();
	}

	public String getWindowFindTerm(String pField, String pValue, String pSize) {
		return getWindowFindTerm(pField, pValue, "", pSize);
	}

	public String getWindowFindTerm(String pField, String pValue, String pIdServicePlace, String pSize) {
		StringBuilder html = new StringBuilder();

		html.append("<input type=\"text\" id=\"id_" + pField + "\" name=\"id_"
				+ pField + "\" size=\"" + pSize
				+ "\" value=\"" + pValue
				+ "\" class=\"inputfield\" title=\"" + pValue + "\">\n");
		html.append("<A HREF=\"#\" onClick=\"JavaScript: var cWin=window.open('"
				+ "../crm/services/findterminals.jsp?id_term='+document.getElementById('id_"
				+ pField
				+ "').value+'&id_service_place="
				+ pIdServicePlace
				+ "&field="
				+ pField
				+ "','blank','height=600,width=900,top=50,left=150,toolbar=no,menubar=no,location=no,scrollbars=yes,status=yes'); cWin.focus();\">\n");
		html.append(getImgChange());
		html.append("<A HREF=\"#\" onClick=\"JavaScript: document.getElementById('id_"
				+ pField + "').value = ''; return false;\">\n");
		html.append(getImgDelete());

		return html.toString();
	}

	public String getWindowFindSAM(String pField, String pValue, String pSize) {
		StringBuilder html = new StringBuilder();

		html.append("<input type=\"text\" id=\"id_" + pField + "\" name=\"id_"
				+ pField + "\" size=\"" + pSize
				+ "\" value=\"" + pValue
				+ "\" class=\"inputfield\" title=\"" + pValue + "\">\n");
		html.append("<A HREF=\"#\" onClick=\"JavaScript: var cWin=window.open('"
				+ "../crm/services/findSAM.jsp?id_sam='+document.getElementById('id_"
				+ pField
				+ "').value,'blank','height=600,width=900,top=50,left=150,toolbar=no,menubar=no,location=no,scrollbars=yes,status=yes'); cWin.focus();\">\n");
		html.append(getImgChange());
		html.append("<A HREF=\"#\" onClick=\"JavaScript: document.getElementById('id_"
				+ pField + "').value = ''; return false;\">\n");
		html.append(getImgDelete());

		return html.toString();
	}

	public String getReferralSchemeName(String id_scheme) {
		return getOneValueByStringId2("SELECT name_referral_scheme FROM "
				+ getGeneralDBScheme() + ".v_referral_scheme_all WHERE id_referral_scheme= ?",
				id_scheme);
	}

	public String getWindowFindReferralScheme(String pField, String pId, String pName1, String pIdJurPrs, String pSize, boolean pReadOnly) {
		StringBuilder html = new StringBuilder();
		String lName = pName1;
		if (isEmpty(pName1)) {
			lName = getReferralSchemeName(pId);
		}

		if (pReadOnly) {
			html.append("<input type=\"hidden\" id=\"id_" + pField
					+ "\" name=\"id_" + pField + "\" value=\"" + pId + "\">\n");
				html.append("<input type=\"text\" id=\"name_" + pField
					+ "\" name=\"name_" + pField + "\" size=\"" + (Integer.parseInt(pSize)+12)
					+ "\" value=\"" + lName
					+ "\" readonly class=\"inputfield-ro\" title=\"" + lName + "\">\n");
		} else {
			html.append("<input type=\"hidden\" id=\"id_" + pField
				+ "\" name=\"id_" + pField + "\" value=\"" + pId + "\">\n");
			html.append("<input type=\"text\" id=\"name_" + pField
				+ "\" name=\"name_" + pField + "\" size=\"" + pSize
				+ "\" value=\"" + lName
				+ "\" readonly class=\"inputfield-ro\" title=\"" + lName + "\">\n");
			html.append("<A HREF=\"#\" onClick=\"JavaScript: var cWin=window.open('"
				+ "../crm/services/findreferralscheme.jsp?id_referral_scheme='+document.getElementById('id_"
				+ pField
				+ "').value+'&field="
				+ pField
				+ "&id_jur_prs="
				+ pIdJurPrs
				+ "','blank','height=600,width=900,top=50,left=150,toolbar=no,menubar=no,location=no,scrollbars=yes,status=yes'); cWin.focus();\">\n");
			html.append(getImgChange());
			html.append("<A HREF=\"#\" onClick=\"JavaScript: document.getElementById('id_"
				+ pField
				+ "').value = ''; document.getElementById('name_"
				+ pField + "').value = ''; return false;\">\n");
			html.append(getImgDelete());
		}
		return html.toString();
	}

	public String getWindowFindBKOperationScheme(String pField, String pValue,
			String pSize) {
		StringBuilder html = new StringBuilder();
		String pName = getBKOperationSchemeLineAdditionalName(pValue);

		html.append("<input type=\"hidden\" id=\"id_" + pField
				+ "\" name=\"id_" + pField + "\" value=\"" + pValue + "\">\n");
		html.append("<input type=\"text\" id=\"name_" + pField
				+ "\" name=\"name_" + pField + "\" size=\"" + pSize
				+ "\" value=\"" + pName
				+ "\" readonly class=\"inputfield-ro\" title=\"" + pName + "\">\n");
		html.append("<A HREF=\"#\" onClick=\"JavaScript: var cWin=window.open('"
				+ "../crm/services/findoperscheme.jsp?id_bk_operation_scheme_line='+document.getElementById('id_"
				+ pField
				+ "').value+'&field="
				+ pField
				+ "','blank','height=600,width=900,top=50,left=150,toolbar=no,menubar=no,location=no,scrollbars=yes,status=yes'); cWin.focus();\">\n");
		html.append(getImgChange());
		html.append("<A HREF=\"#\" onClick=\"JavaScript: document.getElementById('id_"
				+ pField
				+ "').value = ''; document.getElementById('name_"
				+ pField + "').value = ''; return false;\">\n");
		html.append(getImgDelete());

		return html.toString();
	}

	public String getWindowFindBankAccount(String pField, String pValue,
			String pSize) {
		StringBuilder html = new StringBuilder();
		String pName = getBankAccountNumberAndName(pValue);

		html.append("<input type=\"hidden\" id=\"id_" + pField
				+ "\" name=\"id_" + pField + "\" value=\"" + pValue + "\">\n");
		html.append("<input type=\"text\" id=\"name_" + pField
				+ "\" name=\"name_" + pField + "\" size=\"" + pSize
				+ "\" value=\"" + pName
				+ "\" readonly class=\"inputfield-ro\" title=\"" + pName + "\">\n");
		html.append("<A HREF=\"#\" onClick=\"JavaScript: var cWin=window.open('"
				+ "../crm/services/findbankaccounts.jsp?id_bank_account='+document.getElementById('id_"
				+ pField
				+ "').value+'&id_jur_prs='+getElementById('id_jur_prs').value,'blank','height=600,width=900,top=50,left=150,toolbar=no,menubar=no,location=no,scrollbars=yes,status=yes'); cWin.focus();\">\n");
		html.append(getImgChange());
		html.append("<A HREF=\"#\" onClick=\"JavaScript: document.getElementById('id_"
				+ pField
				+ "').value = ''; document.getElementById('name_"
				+ pField + "').value = ''; return false;\">\n");
		html.append(getImgDelete());

		return html.toString();
	}

	public String getWindowFindBankAccountJurPrs(String pField, String pValue,
			String pIdJurPrs, String pSize) {
		StringBuilder html = new StringBuilder();
		String pName = getBankAccountNumberAndName(pValue);

		html.append("<input type=\"hidden\" id=\"id_" + pField
				+ "\" name=\"id_" + pField + "\" value=\"" + pValue + "\">\n");
		html.append("<input type=\"text\" id=\"name_" + pField
				+ "\" name=\"name_" + pField + "\" size=\"" + pSize
				+ "\" value=\"" + pName
				+ "\" readonly class=\"inputfield-ro\" title=\"" + pName + "\">\n");
		html.append("<A HREF=\"#\" onClick=\"JavaScript: var cWin=window.open('"
				+ "../crm/services/findbankaccounts2.jsp?id_bank_account='+document.getElementById('id_"
				+ pField
				+ "').value+'&id_jur_prs=" 
				+ pIdJurPrs 
				+ "&field=" 
				+ pField 
				+ "','blank','height=600,width=900,top=50,left=150,toolbar=no,menubar=no,location=no,scrollbars=yes,status=yes'); cWin.focus();\">\n");
		html.append(getImgChange());
		html.append("<A HREF=\"#\" onClick=\"JavaScript: document.getElementById('id_"
				+ pField
				+ "').value = ''; document.getElementById('name_"
				+ pField + "').value = ''; return false;\">\n");
		html.append(getImgDelete());

		return html.toString();
	}

	public String getWindowFindBankAccount(String pField, String pValue,
			String pShortResult, String pSuffics, String pSize) {
		StringBuilder html = new StringBuilder();
		String pName = getBankAccountNumberAndName(pValue);

		html.append("<input type=\"hidden\" id=\"id_" + pField
				+ "\" name=\"id_" + pField + "\" value=\"" + pValue + "\">\n");
		html.append("<input type=\"text\" id=\"name_" + pField
				+ "\" name=\"name_" + pField + "\" size=\"" + pSize
				+ "\" value=\"" + pName
				+ "\" readonly class=\"inputfield-ro\" title=\"" + pName + "\">\n");
		html.append("<A HREF=\"#\" onClick=\"JavaScript: var cWin=window.open('"
				+ "../crm/services/findbankaccounts.jsp?id_bank_account='+document.getElementById('id_"
				+ pField
				+ "').value+'&short_result="
				+ pShortResult
				+ "&suf="
				+ pSuffics
				+ "','blank','height=600,width=900,top=50,left=150,toolbar=no,menubar=no,location=no,scrollbars=yes,status=yes'); cWin.focus();\">\n");
		html.append(getImgChange());
		html.append("<A HREF=\"#\" onClick=\"JavaScript: document.getElementById('id_"
				+ pField
				+ "').value = ''; document.getElementById('name_"
				+ pField + "').value = ''; return false;\">\n");
		html.append(getImgDelete());

		return html.toString();
	}

	public String getWindowFindBankAccount2(String pField, String pValue,
			String pSize) {
		StringBuilder html = new StringBuilder();
		String pName = getBankAccountNumberAndName(pValue);

		html.append("<input type=\"hidden\" id=\"id_" + pField
				+ "\" name=\"id_" + pField + "\" value=\"" + pValue + "\">\n");
		html.append("<input type=\"text\" id=\"name_" + pField
				+ "\" name=\"name_" + pField + "\" size=\"" + pSize
				+ "\" value=\"" + pName
				+ "\" readonly class=\"inputfield-ro\" title=\"" + pName + "\">\n");
		html.append("<A HREF=\"#\" onClick=\"JavaScript: var cWin=window.open('"
				+ "../crm/services/findbankaccounts2.jsp?id_bank_account='+document.getElementById('id_"
				+ pField
				+ "').value+'&field="
				+ pField
				+ "','blank','height=600,width=900,top=50,left=150,toolbar=no,menubar=no,location=no,scrollbars=yes,status=yes'); cWin.focus();\">\n");
		html.append(getImgChange());
		html.append("<A HREF=\"#\" onClick=\"JavaScript: document.getElementById('id_"
				+ pField
				+ "').value = ''; document.getElementById('name_"
				+ pField + "').value = ''; return false;\">\n");
		html.append(getImgDelete());

		return html.toString();
	}

	public String getWindowFindNatPrs(String pField, String pValue, String pSize) {
		String pName = getNatPrsName(pValue);

		return getWindowFindNatPrs(pField, pValue, pName, pSize);
	}

	public String getWindowFindNatPrs(String pField, String pValue,
			String pNameNatPrs, String pSize) {
		StringBuilder html = new StringBuilder();

		html.append("<input type=\"hidden\" id=\"id_" + pField
				+ "\" name=\"id_" + pField + "\" value=\"" + pValue + "\">\n");
		html.append("<input type=\"text\" id=\"name_" + pField
				+ "\" name=\"name_" + pField + "\" size=\"" + pSize
				+ "\" value=\"" + pNameNatPrs
				+ "\" readonly class=\"inputfield-ro\" title=\"" + pNameNatPrs
				+ "\">\n");
		html.append("<A HREF=\"#\" onClick=\"JavaScript: var cWin=window.open('"
				+ "../crm/services/findnatprs.jsp?id_nat_prs='+document.getElementById('id_"
				+ pField
				+ "').value+'&field="
				+ pField
				+ "','blank','height=600,width=900,top=50,left=150,toolbar=no,menubar=no,location=no,scrollbars=yes,status=yes'); cWin.focus();\">\n");
		html.append(getImgChange());
		html.append("<A HREF=\"#\" onClick=\"JavaScript: document.getElementById('id_"
				+ pField
				+ "').value = ''; document.getElementById('name_"
				+ pField + "').value = ''; return false;\">\n");
		html.append(getImgDelete());

		return html.toString();
	}

	public String getWindowFindNatPrsRole(String pField, String pIdNatPrsRole,
			String pNameNatPrs, String pSize) {
		return getWindowFindNatPrsRole(pField, pIdNatPrsRole, pNameNatPrs, "", pSize);
	}

	public String getWindowFindNatPrsRole(String pField, String pIdNatPrsRole, String pSize) {
		return getWindowFindNatPrsRole(pField, pIdNatPrsRole, "", "", pSize);
	}

	public String getWindowFindNatPrsRole(String pField, String pIdNatPrsRole,
			String pNameNatPrs, String pIdJurPrs, String pSize) {
		StringBuilder html = new StringBuilder();
		String lNameNatPrs = isEmpty(pNameNatPrs)?getNatPrsNameAndCardByIdRole(pIdNatPrsRole):pNameNatPrs;
		
		html.append("<input type=\"hidden\" id=\"id_" + pField
				+ "\" name=\"id_" + pField + "\" value=\"" + pIdNatPrsRole + "\">\n");
		html.append("<input type=\"text\" id=\"name_" + pField
				+ "\" name=\"name_" + pField + "\" size=\"" + pSize
				+ "\" value=\"" + lNameNatPrs
				+ "\" readonly class=\"inputfield-ro\" title=\"" + lNameNatPrs
				+ "\">\n");
		html.append("<A HREF=\"#\" onClick=\"JavaScript: var cWin=window.open('"
				+ "../crm/services/findnatprsrole.jsp?id_nat_prs_role='+document.getElementById('id_"
				+ pField
				+ "').value+'&field="
				+ pField
				+ "&id_jur_prs="
				+ pIdJurPrs
				+ "','blank','height=600,width=900,top=50,left=150,toolbar=no,menubar=no,location=no,scrollbars=yes,status=yes'); cWin.focus();\">\n");
		html.append(getImgChange());
		html.append("<A HREF=\"#\" onClick=\"JavaScript: document.getElementById('id_"
				+ pField
				+ "').value = ''; document.getElementById('name_"
				+ pField + "').value = ''; return false;\">\n");
		html.append(getImgDelete());

		return html.toString();
	}

	public String getWindowFindLoyScheme(String pField, String pValue,
			String pSize, boolean pApplyLoy) {
		return getWindowFindLoyScheme(pField, pValue, "", pSize, pApplyLoy);
	}

	public String getWindowFindLoyScheme(String pField, String pValue,
			String pIdJurPrs, String pSize, boolean pApplyLoy) {
		StringBuilder html = new StringBuilder();
		String applyLoy = "";
		if (pApplyLoy) {
			applyLoy = "try{ document.getElementById('applyLoy').disabled = true;} catch(err){}; ";
		}
		String pName = getLoySchemeCdAndName(pValue);

		html.append("<input type=\"hidden\" id=\"id_" + pField
				+ "\" name=\"id_" + pField + "\" value=\"" + pValue + "\">\n");
		html.append("<input type=\"text\" id=\"name_" + pField
				+ "\" name=\"name_" + pField + "\" size=\"" + pSize
				+ "\" value=\"" + pName
				+ "\" readonly class=\"inputfield-ro\" title=\"" + pName + "\">\n");
		html.append("<A HREF=\"#\" onClick=\"JavaScript: "
				+ applyLoy
				+ " var cWin=window.open('"
				+ "../crm/services/findloyscheme.jsp?cd_scheme='+document.getElementById('id_"
				+ pField
				+ "').value+'&field="
				+ pField
				+ "&id_jur_prs="
				+ pIdJurPrs
				+ "','blank','height=600,width=900,top=50,left=150,toolbar=no,menubar=no,location=no,scrollbars=yes'); cWin.focus();\">\n");
		html.append(getImgChange());
		html.append("<A HREF=\"#\" onClick=\"JavaScript: " + applyLoy
				+ " document.getElementById('id_" + pField
				+ "').value = ''; document.getElementById('name_" + pField
				+ "').value = ''; return false;\">\n");
		html.append(getImgDelete());

		return html.toString();
	}

	public String getWindowFindLoySchedule(String pField, String pValue,
			String pSize, boolean pApplyLoy) {
		StringBuilder html = new StringBuilder();
		String applyLoy = "";
		if (pApplyLoy) {
			applyLoy = "document.getElementById('applyLoy').disabled = true;";
		}
		String pName = getLoyScheduleName(pValue);

		html.append("<input type=\"hidden\" id=\"id_" + pField
				+ "\" name=\"id_" + pField + "\" value=\"" + pValue + "\">\n");
		html.append("<input type=\"text\" id=\"name_" + pField
				+ "\" name=\"name_" + pField + "\" size=\"" + pSize
				+ "\" value=\"" + pName
				+ "\" readonly class=\"inputfield-ro\" title=\"" + pName + "\">\n");
		html.append("<A HREF=\"#\" onClick=\"JavaScript: "
				+ applyLoy
				+ " var cWin=window.open('"
				+ "../crm/services/findschedule.jsp?id_schedule='+document.getElementById('id_"
				+ pField
				+ "').value+'&field="
				+ pField
				+ "','blank','height=600,width=900,top=50,left=150,toolbar=no,menubar=no,location=no,scrollbars=yes'); cWin.focus();\">\n");
		html.append(getImgChange());
		html.append("<A HREF=\"#\" onClick=\"JavaScript: " + applyLoy
				+ " document.getElementById('id_" + pField
				+ "').value = ''; document.getElementById('name_" + pField
				+ "').value = ''; return false;\">\n");
		html.append(getImgDelete());

		return html.toString();
	}

	public String getWindowFindClurRelationship(String pField, String pValue,
			String pParticipant, String pSize) {
		StringBuilder html = new StringBuilder();
		String pName = getClubRelationshipName(pValue);

		html.append("<input type=\"hidden\" id=\"id_" + pField
				+ "\" name=\"id_" + pField + "\" value=\"" + pValue + "\">\n");
		html.append("<input type=\"text\" id=\"name_" + pField
				+ "\" name=\"name_" + pField + "\" size=\"" + pSize
				+ "\" value=\"" + pName
				+ "\" readonly class=\"inputfield-ro\" title=\"" + pName + "\">\n");
		html.append("<A HREF=\"#\" onClick=\"JavaScript: var cWin=window.open('"
				+ "../crm/services/findrelationship.jsp?id_participant="
				+ pParticipant
				+ "&id_club_rel='+document.getElementById('id_"
				+ pField
				+ "').value,'blank','height=600,width=900,top=50,left=150,toolbar=no,menubar=no,location=no,scrollbars=yes,status=yes'); cWin.focus();\">\n");
		html.append(getImgChange());
		html.append("<A HREF=\"#\" onClick=\"JavaScript: document.getElementById('id_"
				+ pField
				+ "').value = ''; document.getElementById('name_"
				+ pField + "').value = ''; return false;\">\n");
		html.append(getImgDelete());

		return html.toString();
	}

	public String getWindowFindClubCard(String pCdCard1, String pSize, boolean pReadOnly) {
		return getWindowFindClubCard("", pCdCard1, "", "", "", pSize, pReadOnly);
	}

	public String getWindowFindClubCard(String pCdCard1,
			String pCardSerialNumber, String pIdIssuer,
			String pIdPaymentSystem, String pSize) {
		return getWindowFindClubCard("", pCdCard1, pCardSerialNumber, pIdIssuer,pIdPaymentSystem, pSize, true);
	}

	public String getWindowFindClubCard(String pPrefix, String pCdCard1,
			String pCardSerialNumber, String pIdIssuer,
			String pIdPaymentSystem, String pSize) {
		return getWindowFindClubCard(pPrefix, pCdCard1, pCardSerialNumber, pIdIssuer, pIdPaymentSystem, pSize, true);
	}

	private String getWindowFindClubCard(String pPrefix, String pCdCard1,
			String pCardSerialNumber, String pIdIssuer,
			String pIdPaymentSystem, String pSize, boolean pReadOnly) {
		StringBuilder html = new StringBuilder();

		String l_cd_card = "";
		if (isEmpty(pCdCard1)) {
			l_cd_card = this.getClubCardCode(pCardSerialNumber + "_"
					+ pIdIssuer + "_" + pIdPaymentSystem);
		} else {
			l_cd_card = pCdCard1;
		}

		html.append("<input type=\"hidden\" id=\""+pPrefix+"card_serial_number\" name=\""+pPrefix+"card_serial_number\" value=\""
				+ pCardSerialNumber + "\">\n");
		html.append("<input type=\"hidden\" id=\""+pPrefix+"id_issuer\" name=\""+pPrefix+"id_issuer\" value=\""
				+ pIdIssuer + "\">\n");
		html.append("<input type=\"hidden\" id=\""+pPrefix+"id_payment_system\" name=\""+pPrefix+"id_payment_system\" value=\""
				+ pIdPaymentSystem + "\">\n");
		html.append("<input type=\"text\" id=\""+pPrefix+"cd_card1\" name=\""+pPrefix+"cd_card1\" size=\""
				+ pSize
				+ "\" value=\""
				+ l_cd_card
				+ "\" " + (pReadOnly?" readonly class=\"inputfield-ro\" ":" class=\"inputfield\" ") + "  title=\""
				+ l_cd_card
				+ "\">\n");
		html.append("<A HREF=\"#\" onClick=\"JavaScript: var cWin=window.open('"
				+ "../crm/services/findclubcards.jsp?prefix="+pPrefix+"&cd_card1='+document.getElementById('"+pPrefix+"cd_card1').value+'&card_serial_number='+document.getElementById('"+pPrefix+"card_serial_number').value,'blank','height=600,width=900,top=50,left=150,toolbar=no,menubar=no,location=no,scrollbars=yes,status=yes'); cWin.focus();\">\n");
		html.append(getImgChange());
		html.append("<A HREF=\"#\" onClick=\"JavaScript: document.getElementById('"+pPrefix+"cd_card1').value = ''; document.getElementById('"+pPrefix+"card_serial_number').value = ''; document.getElementById('"+pPrefix+"id_issuer').value = ''; document.getElementById('"+pPrefix+"id_payment_system').value = ''; return false;\">\n");
		html.append(getImgDelete());

		return html.toString();
	}

	public String getWindowFindClubCardRemittance(String pPrefix,
			String pCdCard1, String pCardSerialNumber, String pIdIssuer,
			String pIdPaymentSystem, String pSize) {
		StringBuilder html = new StringBuilder();

		String l_cd_card = "";
		if (isEmpty(pCdCard1)) {
			l_cd_card = this.getClubCardCode(pCardSerialNumber + "_"
					+ pIdIssuer + "_" + pIdPaymentSystem);
		} else {
			l_cd_card = pCdCard1;
		}

		String l_prefix = "";
		if (!isEmpty(pPrefix)) {
			l_prefix = pPrefix + "_";
		}

		html.append("<input type=\"hidden\" id=\"" + l_prefix
				+ "card_serial_number\" name=\"" + l_prefix
				+ "card_serial_number\" value=\"" + pCardSerialNumber + "\">\n");
		html.append("<input type=\"hidden\" id=\"" + l_prefix
				+ "id_issuer\" name=\"" + l_prefix + "id_issuer\" value=\""
				+ pIdIssuer + "\">\n");
		html.append("<input type=\"hidden\" id=\"" + l_prefix
				+ "id_payment_system\" name=\"" + l_prefix
				+ "id_payment_system\" value=\"" + pIdPaymentSystem + "\">\n");
		html.append("<input type=\"text\" id=\"" + l_prefix
				+ "cd_card1\" name=\"" + l_prefix + "cd_card1\" size=\""
				+ pSize + "\" value=\"" + l_cd_card
				+ "\" readonly class=\"inputfield-ro\" title=\"" + l_cd_card
				+ "\">\n");
		html.append("<A HREF=\"#\" onClick=\"JavaScript: var cWin=window.open('"
				+ "../crm/services/findclubcardsremittance.jsp?cd_card1='+document.getElementById('"
				+ l_prefix
				+ "cd_card1').value+'&card_serial_number='+document.getElementById('"
				+ l_prefix
				+ "card_serial_number').value+'&field="
				+ pPrefix
				+ "','blank','height=600,width=900,top=50,left=150,toolbar=no,menubar=no,location=no,scrollbars=yes,status=yes'); cWin.focus();\">\n");
		html.append(getImgChange());
		html.append("<A HREF=\"#\" onClick=\"JavaScript: document.getElementById('"
				+ l_prefix
				+ "cd_card1').value = ''; document.getElementById('"
				+ l_prefix
				+ "card_serial_number').value = ''; document.getElementById('"
				+ l_prefix
				+ "id_issuer').value = ''; document.getElementById('"
				+ l_prefix
				+ "id_payment_system').value = ''; return false;\">\n");
		html.append(getImgDelete());

		return html.toString();
	}

	public String getWindowFindBKAccount(String pField, String pValue,
			String pSize) {
		StringBuilder html = new StringBuilder();
		String pName = getBKAccountName(pValue);

		html.append("<input type=\"hidden\" id=\"" + pField
				+ "_id_bk_account\" name=\"" + pField
				+ "_id_bk_account\" value=\"" + pValue + "\">\n");
		html.append("<input type=\"text\" id=\"" + pField
				+ "_name_bk_account\" name=\"" + pField
				+ "_name_bk_account\" size=\"" + pSize + "\" value=\"" + pName
				+ "\" readonly class=\"inputfield-ro\" title=\"" + pName + "\">\n");
		html.append("<A HREF=\"#\" onClick=\"JavaScript: var cWin=window.open('"
				+ "../crm/services/findbkaccounts.jsp?id_bk_account='+document.getElementById('"
				+ pField
				+ "_id_bk_account').value+'&field="
				+ pField
				+ "','blank','height=600,width=900,top=50,left=150,toolbar=no,menubar=no,location=no,scrollbars=yes,status=yes'); cWin.focus();\">\n");
		html.append(getImgChange());
		html.append("<A HREF=\"#\" onClick=\"JavaScript: document.getElementById('"
				+ pField
				+ "_id_bk_account').value = ''; document.getElementById('"
				+ pField + "_name_bk_account').value = ''; return false;\">\n");
		html.append(getImgDelete());

		return html.toString();
	}

	public String getWindowFindNatPrsRoleUser(String pField, String pValue,
			String pNameUser, String pIdNatPrsRole, String pSize) {
		StringBuilder html = new StringBuilder();

		html.append("<input type=\"hidden\" id=\"id_" + pField
				+ "\" name=\"id_" + pField + "\" value=\"" + pValue + "\">\n");
		html.append("<input type=\"text\" id=\"name_" + pField
				+ "\" name=\"name_" + pField + "\" size=\"" + pSize
				+ "\" value=\"" + pNameUser
				+ "\" readonly class=\"inputfield-ro\" title=\"" + pNameUser
				+ "\">\n");
		html.append("<A HREF=\"#\" onClick=\"JavaScript: var cWin=window.open('"
				+ "../crm/services/finduser.jsp?id_user='+document.getElementById('id_"
				+ pField
				+ "').value+'&field="
				+ pField
				+ "&id_nat_prs_role="
				+ pIdNatPrsRole
				+ "','blank','height=600,width=900,top=50,left=150,toolbar=no,menubar=no,location=no,scrollbars=yes,status=yes'); cWin.focus();\">\n");
		html.append(getImgChange());
		html.append("<A HREF=\"#\" onClick=\"JavaScript: document.getElementById('id_"
				+ pField
				+ "').value = ''; document.getElementById('name_"
				+ pField + "').value = ''; return false;\">\n");
		html.append(getImgDelete());

		return html.toString();
	}

	public String getWindowFindUser(String pField, String pValue, String pSize) {
		return getWindowFindUserBase(pField, pValue, "", "", pSize);
	}

	public String getWindowFindUser(String pField, String pValue,
			String pNameUser, String pSize) {
		return getWindowFindUserBase(pField, pValue, pNameUser, "", pSize);
	}

	public String getWindowFindUserBase(String pField, String pValue,
			String pNameUser, String pIdJurPrs, String pSize) {
		StringBuilder html = new StringBuilder();
		String lNameUser = pNameUser;
		if (isEmpty(pNameUser)) {
			//lNameUser = getUserName(pValue);
			lNameUser = getUserNatPrsName2(pValue);
		}

		html.append("<input type=\"hidden\" id=\"id_" + pField
				+ "\" name=\"id_" + pField + "\" value=\"" + pValue + "\">\n");
		html.append("<input type=\"text\" id=\"name_" + pField
				+ "\" name=\"name_" + pField + "\" size=\"" + pSize
				+ "\" value=\"" + lNameUser
				+ "\" readonly class=\"inputfield-ro\" title=\"" + lNameUser
				+ "\">\n");
		html.append("<A HREF=\"#\" onClick=\"JavaScript: var cWin=window.open('"
				+ "../crm/services/finduser.jsp?id_user='+document.getElementById('id_"
				+ pField
				+ "').value+'&field="
				+ pField
				+ "&id_jur_prs="
				+ pIdJurPrs
				+ "','blank','height=600,width=900,top=50,left=150,toolbar=no,menubar=no,location=no,scrollbars=yes,status=yes'); cWin.focus();\">\n");
		html.append(getImgChange());
		html.append("<A HREF=\"#\" onClick=\"JavaScript: document.getElementById('id_"
				+ pField
				+ "').value = ''; document.getElementById('name_"
				+ pField + "').value = ''; return false;\">\n");
		html.append(getImgDelete());

		return html.toString();
	}

	public String getWindowFindDSMessageSender(String pSenderKind,
			String pField, String pValue, String pName, String pMessageType,
			String pMessageContact, String pSize) {
		StringBuilder html = new StringBuilder();

		html.append("<input type=\"hidden\" id=\"id_" + pField
				+ "\" name=\"id_" + pField + "\" value=\"" + pValue + "\">\n");
		html.append("<input type=\"text\" id=\"name_" + pField
				+ "\" name=\"name_" + pField + "\" size=\"" + pSize
				+ "\" value=\"" + pName
				+ "\" readonly class=\"inputfield-ro\" title=\"" + pName + "\">\n");
		html.append("<A HREF=\"#\" onClick=\"JavaScript: var cWin=window.open('"
				+ "../crm/services/finddsmessagesender.jsp?id_sender='+document.getElementById('id_"
				+ pField
				+ "').value+'&name_sender='+document.getElementById('name_"
				+ pField
				+ "').value+'&sender_kind="
				+ pSenderKind
				+ "&message_type="
				+ pMessageType
				+ "&message_contact="
				+ pMessageContact
				+ "&field_name="
				+ pField
				+ "','blank','height=600,width=900,top=50,left=150,toolbar=no,menubar=no,location=no,scrollbars=yes,status=yes'); cWin.focus();\">\n");
		html.append(getImgChange());
		html.append("<A HREF=\"#\" onClick=\"JavaScript: document.getElementById('id_"
				+ pField
				+ "').value = ''; document.getElementById('name_"
				+ pField + "').value = ''; return false;\">\n");
		html.append(getImgDelete());

		return html.toString();
	}

	public String getWindowFindCallCenterInquirer(String pField,
			String pInInquirer, String pNameInquirer, String pSize) {
		StringBuilder html = new StringBuilder();

		html.append("<input type=\"hidden\" id=\"id_" + pField
				+ "\" name=\"id_" + pField + "\" value=\"" + pInInquirer
				+ "\">\n");
		html.append("<input type=\"text\" id=\"name_" + pField
				+ "\" name=\"name_" + pField + "\" size=\"" + pSize
				+ "\" value=\"" + pNameInquirer
				+ "\" readonly class=\"inputfield\" title=\"" + pNameInquirer
				+ "\">\n");
		html.append("<A HREF=\"#\" onClick=\"JavaScript: var cWin=window.open('"
				+ "../crm/services/findcallcenterinquirer.jsp?id_cc_inquirere='+document.getElementById('id_"
				+ pField
				+ "').value,'blank','height=600,width=900,top=50,left=150,toolbar=no,menubar=no,location=no,scrollbars=yes,status=yes'); cWin.focus();\">\n");
		html.append(getImgChange());
		html.append("<A HREF=\"#\" onClick=\"JavaScript: document.getElementById('id_"
				+ pField
				+ "').value = ''; document.getElementById('name_"
				+ pField + "').value = ''; return false;\">\n");
		html.append(getImgDelete());

		return html.toString();
	}

	public String getWindowFindCallCenterNatPrs(String pField, String pValue,
			String pNameNatPrs, String pSize) {
		StringBuilder html = new StringBuilder();

		html.append("<input type=\"hidden\" id=\"id_" + pField
				+ "\" name=\"id_" + pField + "\" value=\"" + pValue + "\">\n");
		html.append("<input type=\"text\" id=\"name_" + pField
				+ "\" name=\"name_" + pField + "\" size=\"" + pSize
				+ "\" value=\"" + pNameNatPrs
				+ "\" readonly class=\"inputfield-ro\" title=\"" + pNameNatPrs
				+ "\">\n");
		html.append("<A HREF=\"#\" onClick=\"JavaScript: var cWin=window.open('"
				+ "../crm/services/findcallcenterclubcards.jsp?code_type=NAT_PRS&id_nat_prs='+document.getElementById('id_"
				+ pField
				+ "').value,'blank','height=600,width=900,top=50,left=150,toolbar=no,menubar=no,location=no,scrollbars=yes,status=yes'); cWin.focus();\">\n");
		html.append(getImgChange());
		html.append("<A HREF=\"#\" onClick=\"JavaScript: document.getElementById('id_"
				+ pField
				+ "').value = ''; document.getElementById('name_"
				+ pField + "').value = ''; return false;\">\n");
		html.append(getImgDelete());

		return html.toString();
	}

	public String getWindowFindCallCenterClubCard(String pCdCard1,
			String pCardSerialNumber, String pIdIssuer,
			String pIdPaymentSystem, String pSize) {
		StringBuilder html = new StringBuilder();

		html.append("<input type=\"hidden\" id=\"card_serial_number\" name=\"card_serial_number\" value=\""
				+ pCardSerialNumber + "\">\n");
		html.append("<input type=\"hidden\" id=\"id_issuer\" name=\"id_issuer\" value=\""
				+ pIdIssuer + "\">\n");
		html.append("<input type=\"hidden\" id=\"id_payment_system\" name=\"id_payment_system\" value=\""
				+ pIdPaymentSystem + "\">\n");
		html.append("<input type=\"text\" id=\"cd_card1\" name=\"cd_card1\" size=\""
				+ pSize
				+ "\" value=\""
				+ pCdCard1
				+ "\" readonly class=\"inputfield-ro\" title=\""
				+ pCdCard1
				+ "\">\n");
		html.append("<A HREF=\"#\" onClick=\"JavaScript: var cWin=window.open('"
				+ "../crm/services/findcallcenterclubcards.jsp?code_type=CARDS&cd_card1='+document.getElementById('cd_card1').value,'blank','height=600,width=900,top=50,left=150,toolbar=no,menubar=no,location=no,scrollbars=yes,status=yes'); cWin.focus();\">\n");
		html.append(getImgChange());
		html.append("<A HREF=\"#\" onClick=\"JavaScript: document.getElementById('cd_card1').value = ''; document.getElementById('card_serial_number').value = ''; document.getElementById('id_issuer').value = ''; document.getElementById('id_payment_system').value = ''; return false;\">\n");
		html.append(getImgDelete());

		return html.toString();
	}

	public String getWindowFindCallCenterUser(String pField, String pValue,
			String pNameUser, String pSize) {
		StringBuilder html = new StringBuilder();

		html.append("<input type=\"hidden\" id=\"id_" + pField
				+ "\" name=\"id_" + pField + "\" value=\"" + pValue + "\">\n");
		html.append("<input type=\"text\" id=\"name_" + pField
				+ "\" name=\"name_" + pField + "\" size=\"" + pSize
				+ "\" value=\"" + pNameUser
				+ "\" readonly class=\"inputfield-ro\" title=\"" + pNameUser
				+ "\">\n");
		html.append("<A HREF=\"#\" onClick=\"JavaScript: var cWin=window.open('"
				+ "../crm/services/findcallcenteruser.jsp?id_user='+document.getElementById('id_"
				+ pField
				+ "').value,'blank','height=600,width=900,top=50,left=150,toolbar=no,menubar=no,location=no,scrollbars=yes,status=yes'); cWin.focus();\">\n");
		html.append(getImgChange());
		html.append("<A HREF=\"#\" onClick=\"JavaScript: document.getElementById('id_"
				+ pField
				+ "').value = ''; document.getElementById('name_"
				+ pField + "').value = ''; return false;\">\n");
		html.append(getImgDelete());

		return html.toString();
	}

	public String getWindowFindCallCenterFAQ(String pField, String pValue,
			String pSize) {
		StringBuilder html = new StringBuilder();
		String pTitle = getFAQTitle(pValue);

		html.append("<input type=\"hidden\" id=\"id_" + pField
				+ "\" name=\"id_" + pField + "\" value=\"" + pValue + "\">\n");
		html.append("<input type=\"text\" id=\"name_" + pField
				+ "\" name=\"name_" + pField + "\" size=\"" + pSize
				+ "\" value=\"" + pTitle
				+ "\" readonly class=\"inputfield-ro\" title=\"" + pTitle
				+ "\">\n");
		html.append("<A HREF=\"#\" onClick=\"JavaScript: var cWin=window.open('"
				+ "../crm/services/findfaq.jsp?id_cc_faq='+document.getElementById('id_"
				+ pField
				+ "').value,'blank','height=600,width=900,top=50,left=150,toolbar=no,menubar=no,location=no,scrollbars=yes,status=yes'); cWin.focus();\">\n");
		html.append(getImgChange());
		html.append("<A HREF=\"#\" onClick=\"JavaScript: document.getElementById('id_"
				+ pField
				+ "').value = ''; document.getElementById('name_"
				+ pField + "').value = ''; return false;\">\n");
		html.append(getImgDelete());

		return html.toString();
	}

	public String getWindowBKAccountSchemeLine(String pField, String pId,
			String pCode, String pScheme, String pSize) {
		StringBuilder html = new StringBuilder();

		html.append("<input type=\"hidden\" id=\"" + pField
				+ "id_bk_account_scheme_line\" name=\"" + pField
				+ "id_bk_account_scheme_line\" value=\"" + pId + "\">\n");
		html.append("<input type=\"text\" id=\"" + pField
				+ "name_bk_account_scheme_line\" name=\"" + pField
				+ "name_bk_account_scheme_line\" size=\"" + pSize
				+ "\" value=\"" + pCode
				+ "\" readonly class=\"inputfield\" title=\"" + pCode + "\">\n");
		html.append("<A HREF=\"#\" onClick=\"JavaScript: var cWin=window.open('"
				+ "../crm/services/findpostingsettings.jsp?id_bk_account_scheme="
				+ pScheme
				+ "&type_bk_account_scheme_line="
				+ pField
				+ "&id_bk_account_scheme_line='+document.getElementById('"
				+ pField
				+ "id_bk_account_scheme_line').value,'blank','height=800,width=700,top=50,left=150,toolbar=no,menubar=no,location=no,scrollbars=yes,status=yes'); cWin.focus();\">\n");
		html.append(getImgChange());
		html.append("<A HREF=\"#\" onClick=\"JavaScript: document.getElementById('"
				+ pField
				+ "id_bk_account_scheme_line').value = ''; document.getElementById('"
				+ pField
				+ "name_bk_account_scheme_line').value = ''; return false;\">\n");
		html.append(getImgDelete());

		return html.toString();
	}

	public String getWindowCardPackage(String pField, String pValue,
			String pName, String pIdJurPrs, String pSize) {
		StringBuilder html = new StringBuilder();

		html.append("<input type=\"hidden\" id=\"id_" + pField
				+ "\" name=\"id_" + pField + "\" value=\"" + pValue + "\">\n");
		html.append("<input type=\"text\" id=\"name_" + pField
				+ "\" name=\"name_" + pField + "\" size=\"" + pSize
				+ "\" value=\"" + pName
				+ "\" readonly class=\"inputfield-ro\" title=\"" + pName
				+ "\">\n");
		html.append("<A HREF=\"#\" onClick=\"JavaScript: var cWin=window.open('"
				+ "../crm/services/findcardpack.jsp?id_card_pack='+document.getElementById('id_"
				+ pField
				+ "').value+'&id_jur_prs="
				+ pIdJurPrs
				+ "&field="
				+ pField
				+ "','blank','height=600,width=900,top=50,left=150,toolbar=no,menubar=no,location=no,scrollbars=yes,status=yes'); cWin.focus();\">\n");
		html.append(getImgChange());
		html.append("<A HREF=\"#\" onClick=\"JavaScript: document.getElementById('id_"
				+ pField
				+ "').value = ''; document.getElementById('name_"
				+ pField + "').value = ''; return false;\">\n");
		html.append(getImgDelete());

		return html.toString();
	}

	public String getWindowGifts(String pField, String pValue, String pSize) {
		StringBuilder html = new StringBuilder();
		String pGiftName = getGiftName(pValue);

		html.append("<input type=\"hidden\" id=\"id_" + pField
				+ "\" name=\"id_" + pField + "\" value=\"" + pValue + "\">\n");
		html.append("<input type=\"text\" id=\"name_" + pField
				+ "\" name=\"name_" + pField + "\" size=\"" + pSize
				+ "\" value=\"" + pGiftName
				+ "\" readonly class=\"inputfield\" title=\"" + pGiftName
				+ "\">\n");
		html.append("<A HREF=\"#\" onClick=\"JavaScript: var cWin=window.open('"
				+ "../crm/services/findgifts.jsp?id_gift='+document.getElementById('id_"
				+ pField
				+ "').value,'blank','height=600,width=900,top=50,left=150,toolbar=no,menubar=no,location=no,scrollbars=yes,status=yes'); cWin.focus();\">\n");
		html.append(getImgChange());
		html.append("<A HREF=\"#\" onClick=\"JavaScript: document.getElementById('id_"
				+ pField
				+ "').value = ''; document.getElementById('name_"
				+ pField + "').value = ''; return false;\">\n");
		html.append(getImgDelete());

		return html.toString();
	}

	public String getWindowDocuments(String pField, String pValue, String pSize) {
		StringBuilder html = new StringBuilder();
		String lDocName = "";
		if (!isEmpty(pValue)) {
			lDocName = getDocName(pValue);
		}

		html.append("<script>\n");
		html.append("function getDocJurPrs() {\n");
		html.append("var idJurPrs = '';\n");
		html.append("try {\n");
		html.append("idJurPrs = document.getElementById('id_jur_prs').value; \n");
		html.append("} catch (e) {}\n");
		html.append("return idJurPrs;\n");
		html.append("}\n");
		html.append("</script>\n");
		html.append("<input type=\"hidden\" id=\"id_" + pField
				+ "\" name=\"id_" + pField + "\" value=\"" + pValue + "\">\n");
		html.append("<input type=\"text\" id=\"name_" + pField
				+ "\" name=\"name_" + pField + "\" size=\"" + pSize
				+ "\" value=\"" + lDocName
				+ "\" readonly class=\"inputfield-ro\" title=\"" + lDocName
				+ "\">\n");
		html.append("<A HREF=\"#\" onClick=\"JavaScript: var cWin=window.open('"
				+ "../crm/services/finddoc.jsp?id_doc='+document.getElementById('id_"
				+ pField
				+ "').value+'&id_jur_prs='+getDocJurPrs()+'&field="
				+ pField
				+ "','blank','height=600,width=900,top=50,left=150,toolbar=no,menubar=no,location=no,scrollbars=yes,status=yes'); cWin.focus();\">\n");
		html.append(getImgChange());
		html.append("<A HREF=\"#\" onClick=\"JavaScript: document.getElementById('id_"
				+ pField
				+ "').value = ''; document.getElementById('name_"
				+ pField + "').value = ''; return false;\">\n");
		html.append(getImgDelete());

		return html.toString();
	}

	public String getWindowContactPersons(String pField, String pValue,
			String id_jur_prs, String id_service_place, String pSize) {
		StringBuilder html = new StringBuilder();
		String pGiftName = getContactPrsName(pValue);

		html.append("<input type=\"hidden\" id=\"id_" + pField
				+ "\" name=\"id_" + pField + "\" value=\"" + pValue + "\">\n");
		html.append("<input type=\"text\" id=\"name_" + pField
				+ "\" name=\"name_" + pField + "\" size=\"" + pSize
				+ "\" value=\"" + pGiftName
				+ "\" readonly class=\"inputfield\" title=\"" + pGiftName
				+ "\">\n");
		html.append("<A HREF=\"#\" onClick=\"JavaScript: var cWin=window.open('"
				+ "../crm/services/findcontactprs.jsp?id_contact_prs='+document.getElementById('id_"
				+ pField
				+ "').value+'&id_jur_prs="
				+ id_jur_prs
				+ "&id_service_place="
				+ id_service_place
				+ "&field="
				+ pField
				+ "','blank','height=600,width=900,top=50,left=150,toolbar=no,menubar=no,location=no,scrollbars=yes,status=yes'); cWin.focus();\">\n");
		html.append(getImgChange());
		html.append("<A HREF=\"#\" onClick=\"JavaScript: document.getElementById('id_"
				+ pField
				+ "').value = ''; document.getElementById('name_"
				+ pField + "').value = ''; return false;\">\n");
		html.append(getImgDelete());

		return html.toString();
	}

	public String getWindowFindLGPromoter(String pField, String pValue,
			String id_jur_prs, String id_service_place, String pSize) {
		StringBuilder html = new StringBuilder();
		String pGiftName = getContactPrsName(pValue);

		html.append("<input type=\"hidden\" id=\"id_" + pField
				+ "\" name=\"id_" + pField + "\" value=\"" + pValue + "\">\n");
		html.append("<input type=\"text\" id=\"name_" + pField
				+ "\" name=\"name_" + pField + "\" size=\"" + pSize
				+ "\" value=\"" + pGiftName
				+ "\" readonly class=\"inputfield-ro\" title=\"" + pGiftName
				+ "\">\n");
		html.append("<A HREF=\"#\" onClick=\"JavaScript: var cWin=window.open('"
				+ "../crm/services/findlgpromoter.jsp?id_contact_prs='+document.getElementById('id_"
				+ pField
				+ "').value+'&id_jur_prs="
				+ id_jur_prs
				+ "&id_service_place="
				+ id_service_place
				+ "&field="
				+ pField
				+ "','blank','height=600,width=900,top=50,left=150,toolbar=no,menubar=no,location=no,scrollbars=yes,status=yes'); cWin.focus();\">\n");
		html.append(getImgChange());
		html.append("<A HREF=\"#\" onClick=\"JavaScript: document.getElementById('id_"
				+ pField
				+ "').value = ''; document.getElementById('name_"
				+ pField + "').value = ''; return false;\">\n");
		html.append(getImgDelete());

		return html.toString();
	}

	public String getWindowClubActionGifts(String pField, String pValue,
			String pSize) {
		StringBuilder html = new StringBuilder();
		String pGiftName = getGiftName(pValue);

		html.append("<input type=\"hidden\" id=\"id_" + pField
				+ "\" name=\"id_" + pField + "\" value=\"" + pValue + "\">\n");
		html.append("<input type=\"text\" id=\"name_" + pField
				+ "\" name=\"name_" + pField + "\" size=\"" + pSize
				+ "\" value=\"" + pGiftName
				+ "\" readonly class=\"inputfield\" title=\"" + pGiftName
				+ "\">\n");
		html.append("<A HREF=\"#\" onClick=\"JavaScript: var cWin=window.open('"
				+ "../crm/services/findgiftsclubaction.jsp?id_gift='+document.getElementById('id_"
				+ pField
				+ "').value+'&id_club_event='+document.getElementById('id_club_event').value+'&id_realization='+document.getElementById('id_realization').value,'blank','height=600,width=900,top=50,left=150,toolbar=no,menubar=no,location=no,scrollbars=yes,status=yes'); cWin.focus();\">\n");
		html.append(getImgChange());
		html.append("<A HREF=\"#\" onClick=\"JavaScript: document.getElementById('id_"
				+ pField
				+ "').value = ''; document.getElementById('name_"
				+ pField + "').value = ''; return false;\">\n");
		html.append(getImgDelete());

		return html.toString();
	}

	public String getWindowEmailProfiles(String pField, String pValue,
			String pSize) {
		StringBuilder html = new StringBuilder();

		String clubSelect = getSelectBodyFromSizedQuery(
				" SELECT id_email_profile, name_email_profile " + "   FROM "
						+ getGeneralDBScheme()
						+ ".vc_ds_email_profile_club_all "
						+ "  ORDER BY name_email_profile", pValue, pSize, true,
				false);

		String pName = getDSEmailProfileName(pValue);

		if (this.selectOprionCount > this.maxSelectOption) {
			html.append("<input type=\"hidden\" id=\"id_" + pField
					+ "\" name=\"id_" + pField + "\" value=\"" + pValue
					+ "\">\n");
			html.append("<input type=\"text\" id=\"name_" + pField
					+ "\" name=\"name_" + pField + "\" size=\"" + pSize
					+ "\" value=\"" + pName
					+ "\" readonly class=\"inputfield\" title=\"" + pName
					+ "\">\n");
			html.append("<A HREF=\"#\" onClick=\"JavaScript: var cWin=window.open('"
					+ "../crm/services/finddsemailprofile.jsp?id_profile='+document.getElementById('id_"
					+ pField
					+ "').value,'blank','height=600,width=900,top=50,left=150,toolbar=no,menubar=no,location=no,scrollbars=yes,status=yes'); cWin.focus();\">\n");
			html.append(getImgChange());
			html.append("<A HREF=\"#\" onClick=\"JavaScript: document.getElementById('id_"
					+ pField
					+ "').value = ''; document.getElementById('name_"
					+ pField + "').value = ''; return false;\">\n");
			html.append(getImgDelete());
		} else {
			html.append("<script type=\"text/javascript\">");
			html.append("function changeEmailProfileNameScript() {");
			html.append("   document.getElementById('id_" + pField
					+ "').value = document.getElementById('name_" + pField
					+ "').value;");
			// html.append("   alert(document.getElementById('name_" + pField +
			// "').value);");
			html.append("}");
			html.append("changeEmailProfileNameScript();");
			html.append("</script>");
			html.append("<input type=\"hidden\" id=\"id_" + pField
					+ "\" name=\"id_" + pField + "\" value=\"" + pValue
					+ "\">\n");
			html.append("<select name=\"name_"
					+ pField
					+ "\" id=\"name_"
					+ pField
					+ "\" class=\"inputfield\" onchange=\"changeEmailProfileNameScript()\">\n");
			html.append(clubSelect);
			html.append("</select>\n");
		}
		return html.toString();
	}

	/*
	 * public String getWindowClientProfiles(String pField, String pValue,
	 * String pSize) { StringBuilder html = new StringBuilder();
	 * 
	 * String clubSelect = getSelectBodyFromQuery(
	 * " SELECT id_ds_cl_profile, name_ds_cl_profile " + "   FROM " +
	 * getGeneralDBScheme()+".vc_ds_cl_profile_club_all" +
	 * "  ORDER BY name_ds_cl_profile", pValue, pSize, true);
	 * 
	 * String pName = getDsClientProfileName(pValue);
	 * 
	 * if (this.selectOprionCount > this.maxSelectOption) {
	 * html.append("<input type=\"hidden\" id=\"id_"
	 * +pField+"\" name=\"id_"+pField+"\" value=\""+pValue+"\">\n");
	 * html.append(
	 * "<input type=\"text\" id=\"name_"+pField+"\" name=\"name_"+pField
	 * +"\" size=\""+pSize+"\" value=\"" + pName +
	 * "\" readonly class=\"inputfield\" title=\"" + pName + "\">\n");
	 * html.append("<A HREF=\"#\" onClick=\"JavaScript: var cWin=window.open('"
	 * "../crm/services/finddsclientsprofile.jsp?id_profile='+document.getElementById('id_"
	 * +pField+
	 * "').value,'blank','height=600,width=900,top=50,left=150,toolbar=no,menubar=no,location=no,scrollbars=yes,status=yes'); cWin.focus();\">\n"
	 * ); html.append("<img vspace=\"0\" hspace=\"0\" src=\"" 
	 * +
	 * "../images/oper/window_open/change.png\" align=\"top\" style=\"border: 0px;\" title=\""
	 * + buttonXML.getfieldTransl("button_edit", false) +
	 * "\"></a>\n"); html.append(
	 * "<A HREF=\"#\" onClick=\"JavaScript: document.getElementById('id_"
	 * +pField+"').value = ''; document.getElementById('name_"+pField+
	 * "').value = ''; return false;\">\n");
	 * html.append("<img vspace=\"0\" hspace=\"0\" src=\"" + 
	 * "../images/oper/window_open/delete.png\" align=\"top\" style=\"border: 0px;\" title=\""
	 * + buttonXML.getfieldTransl("button_delete", false) +
	 * "\"></a>\n"); } else { html.append("<script type=\"text/javascript\">");
	 * html.append("function changeClientProfileNameScript() {");
	 * html.append("   document.getElementById('id_" + pField +
	 * "').value = document.getElementById('name_" + pField + "').value;");
	 * //html.append("   alert(document.getElementById('name_" + pField +
	 * "').value);"); html.append("}");
	 * html.append("changeClientProfileNameScript();");
	 * html.append("</script>");
	 * html.append("<input type=\"hidden\" id=\"id_"+pField
	 * +"\" name=\"id_"+pField+"\" value=\"" + pValue + "\">\n");
	 * html.append("<select name=\"name_" + pField + "\" id=\"name_" + pField +
	 * "\" class=\"inputfield\" onchange=\"changeClientProfileNameScript()\">\n"
	 * ); html.append(clubSelect); html.append("</select>\n"); }
	 * 
	 * return html.toString(); }
	 */

	/*
	 * public String getWindowPartnerProfiles(String pField, String pValue,
	 * String pSize) { StringBuilder html = new StringBuilder();
	 * 
	 * String clubSelect = getSelectBodyFromQuery(
	 * " SELECT id_ds_pt_profile, name_ds_pt_profile " + "   FROM " +
	 * getGeneralDBScheme()+".vc_ds_pt_profile_club_all" +
	 * "  ORDER BY name_ds_pt_profile", pValue, pSize, true);
	 * 
	 * String pName = getDsPartnerProfileName(pValue);
	 * 
	 * if (this.selectOprionCount > this.maxSelectOption) {
	 * html.append("<input type=\"hidden\" id=\"id_"
	 * +pField+"\" name=\"id_"+pField+"\" value=\""+pValue+"\">\n");
	 * html.append(
	 * "<input type=\"text\" id=\"name_"+pField+"\" name=\"name_"+pField
	 * +"\" size=\""+pSize+"\" value=\"" + pName +
	 * "\" readonly class=\"inputfield\" title=\"" + pName + "\">\n");
	 * html.append("<A HREF=\"#\" onClick=\"JavaScript: var cWin=window.open('"
	 * "../crm/services/finddspartnersprofile.jsp?id_profile='+document.getElementById('id_"
	 * +pField+
	 * "').value,'blank','height=600,width=900,top=50,left=150,toolbar=no,menubar=no,location=no,scrollbars=yes,status=yes'); cWin.focus();\">\n"
	 * ); html.append("<img vspace=\"0\" hspace=\"0\" src=\"" + 
	 * "../images/oper/window_open/change.png\" align=\"top\" style=\"border: 0px;\" title=\""
	 * + buttonXML.getfieldTransl("button_edit", false) +
	 * "\"></a>\n"); html.append(
	 * "<A HREF=\"#\" onClick=\"JavaScript: document.getElementById('id_"
	 * +pField+"').value = ''; document.getElementById('name_"+pField+
	 * "').value = ''; return false;\">\n");
	 * html.append("<img vspace=\"0\" hspace=\"0\" src=\"" + 
	 * "../images/oper/window_open/delete.png\" align=\"top\" style=\"border: 0px;\" title=\""
	 * + buttonXML.getfieldTransl("button_delete", false) +
	 * "\"></a>\n"); } else { html.append("<script type=\"text/javascript\">");
	 * html.append("function changePartnerProfileNameScript() {");
	 * html.append("   document.getElementById('id_" + pField +
	 * "').value = document.getElementById('name_" + pField + "').value;");
	 * //html.append("   alert(document.getElementById('name_" + pField +
	 * "').value);"); html.append("}");
	 * html.append("changePartnerProfileNameScript();");
	 * html.append("</script>");
	 * html.append("<input type=\"hidden\" id=\"id_"+pField
	 * +"\" name=\"id_"+pField+"\" value=\"" + pValue + "\">\n");
	 * html.append("<select name=\"name_" + pField + "\" id=\"name_" + pField +
	 * "\" class=\"inputfield\" onchange=\"changePartnerProfileNameScript()\">\n"
	 * ); html.append(clubSelect); html.append("</select>\n"); }
	 * 
	 * return html.toString(); }
	 */

	public String getWindowCalculator(String pField, String pId,
			String pIdLine, String pValue, String pCalc, String pNeedOper,
			String pCols, String pRows) {
		StringBuilder html = new StringBuilder();

		html.append("<textarea name=\"" + pField + "\" id=\"" + pField
				+ "\" cols=\"" + pCols + "\" rows=\"" + pRows
				+ "\" readonly class=\"inputfield\">" + pValue + "</textarea>");
		html.append("<A HREF=\"#\" id=\"span_"
				+ pField
				+ "\" onClick=\"JavaScript: var cWin=window.open('"
				+ "../crm/finance/calculator.jsp?id_line="
				+ pIdLine
				+ "&calc="
				+ pCalc
				+ "&need_operation="
				+ pNeedOper
				+ "&field="
				+ pField
				+ "&id="
				+ pId
				+ "&cd_club_rel_type='+document.getElementById('cd_club_rel_type').value+'&cd_bk_operation_type='+document.getElementById('cd_bk_operation_type').value,'blank','height=550,width=1000,top=150,left=150,toolbar=no,menubar=no,location=no,status=yes,scrollbars=yes'); cWin.focus();\">");
		html.append("<img vspace=\"0\" hspace=\"0\" src=\""
				+ "../images/oper/rows/formula.png\" align=\"top\" style=\"border: 0px;\" title=\""
				+ buttonXML.getfieldTransl("change", false)
				+ "\">");
		html.append("</a>");

		return html.toString();
	}

	/*
	 * public String getWindowFindDSClientPatterns(String pType, String pField,
	 * String pValue, String pSize) { StringBuilder html = new StringBuilder();
	 * 
	 * String clubSelect = getSelectBodyFromSizedQuery(
	 * " SELECT id_cl_pattern, name_cl_pattern " + "   FROM " +
	 * getGeneralDBScheme()+".vc_ds_cl_pattern_club_all" +
	 * "  ORDER BY name_cl_pattern", pValue, pSize, true);
	 * 
	 * String pName = getDSClientPatternName(pValue);
	 * 
	 * if (this.selectOprionCount > this.maxSelectOption) {
	 * html.append("<input type=\"hidden\" id=\"id_"
	 * +pField+"\" name=\"id_"+pField+"\" value=\""+pValue+"\">\n");
	 * html.append(
	 * "<input type=\"text\" id=\"name_"+pField+"\" name=\"name_"+pField
	 * +"\" size=\""+pSize+"\" value=\"" + pName +
	 * "\" readonly class=\"inputfield-ro\" title=\"" + pName + "\">\n");
	 * html.append("<A HREF=\"#\" onClick=\"JavaScript: var cWin=window.open('"
	 * "../crm/services/finddsclientpattern.jsp?id_cl_pattern='+document.getElementById('id_"
	 * +pField+"').value+'&field_name="+pField+
	 * "','blank','height=600,width=900,top=50,left=150,toolbar=no,menubar=no,location=no,scrollbars=yes,status=yes'); cWin.focus();\">\n"
	 * ); html.append("<img vspace=\"0\" hspace=\"0\" src=\"" 
	 * +
	 * "../images/oper/window_open/change.png\" align=\"top\" style=\"border: 0px;\" title=\""
	 * + buttonXML.getfieldTransl("button_edit", false) +
	 * "\"></a>\n"); html.append(
	 * "<A HREF=\"#\" onClick=\"JavaScript: document.getElementById('id_"
	 * +pField+"').value = ''; document.getElementById('name_"+pField+
	 * "').value = ''; return false;\">\n");
	 * html.append("<img vspace=\"0\" hspace=\"0\" src=\"" + 
	 * "../images/oper/window_open/delete.png\" align=\"top\" style=\"border: 0px;\" title=\""
	 * + buttonXML.getfieldTransl("button_delete", false) +
	 * "\"></a>\n"); } else { html.append("<script type=\"text/javascript\">");
	 * html.append("function changeClientPatternNameScript() {");
	 * html.append("   document.getElementById('id_" + pField +
	 * "').value = document.getElementById('name_" + pField + "').value;");
	 * //html.append("   alert(document.getElementById('name_" + pField +
	 * "').value);"); html.append("}");
	 * html.append("changeClientPatternNameScript();");
	 * html.append("</script>");
	 * html.append("<input type=\"hidden\" id=\"id_"+pField
	 * +"\" name=\"id_"+pField+"\" value=\"" + pValue + "\">\n");
	 * html.append("<select name=\"name_" + pField + "\" id=\"name_" + pField +
	 * "\" class=\"inputfield\" onchange=\"changeClientPatternNameScript()\">\n"
	 * ); html.append(clubSelect); html.append("</select>\n"); }
	 * 
	 * return html.toString(); }
	 */

	/*
	 * public String getWindowFindDSPartnerPatterns(String pType, String pField,
	 * String pValue, String pSize) { StringBuilder html = new StringBuilder();
	 * 
	 * String clubSelect = getSelectBodyFromQuery(
	 * " SELECT id_pt_pattern, name_pt_pattern " + "   FROM " +
	 * getGeneralDBScheme()+".vc_ds_pt_pattern_club_all" +
	 * "  ORDER BY name_pt_pattern", pValue, pSize, true);
	 * 
	 * String pName = getDSPartnerPatternName(pValue);
	 * 
	 * if (this.selectOprionCount > this.maxSelectOption) {
	 * html.append("<input type=\"hidden\" id=\"id_"
	 * +pField+"\" name=\"id_"+pField+"\" value=\""+pValue+"\">\n");
	 * html.append(
	 * "<input type=\"text\" id=\"name_"+pField+"\" name=\"name_"+pField
	 * +"\" size=\""+pSize+"\" value=\"" + pName +
	 * "\" readonly class=\"inputfield-ro\" title=\"" + pName + "\">\n");
	 * html.append("<A HREF=\"#\" onClick=\"JavaScript: var cWin=window.open('"
	 * "../crm/services/finddspartnerpattern.jsp?id_pt_pattern='+document.getElementById('id_"
	 * +pField+"').value+'&type_message="+pType+"&field_name="+pField+
	 * "','blank','height=600,width=900,top=50,left=150,toolbar=no,menubar=no,location=no,scrollbars=yes,status=yes'); cWin.focus();\">\n"
	 * ); html.append("<img vspace=\"0\" hspace=\"0\" src=\"" 
	 * +
	 * "../images/oper/window_open/change.png\" align=\"top\" style=\"border: 0px;\" title=\""
	 * + buttonXML.getfieldTransl("button_edit", false) +
	 * "\"></a>\n"); html.append(
	 * "<A HREF=\"#\" onClick=\"JavaScript: document.getElementById('id_"
	 * +pField+"').value = ''; document.getElementById('name_"+pField+
	 * "').value = ''; return false;\">\n");
	 * html.append("<img vspace=\"0\" hspace=\"0\" src=\"" + 
	 * "../images/oper/window_open/delete.png\" align=\"top\" style=\"border: 0px;\" title=\""
	 * + buttonXML.getfieldTransl("button_delete", false) +
	 * "\"></a>\n"); } else { html.append("<script type=\"text/javascript\">");
	 * html.append("function changePartnerPatternNameScript() {");
	 * html.append("   document.getElementById('id_" + pField +
	 * "').value = document.getElementById('name_" + pField + "').value;");
	 * //html.append("   alert(document.getElementById('name_" + pField +
	 * "').value);"); html.append("}");
	 * html.append("changePartnerPatternNameScript();");
	 * html.append("</script>");
	 * html.append("<input type=\"hidden\" id=\"id_"+pField
	 * +"\" name=\"id_"+pField+"\" value=\"" + pValue + "\">\n");
	 * html.append("<select name=\"name_" + pField + "\" id=\"name_" + pField +
	 * "\" class=\"inputfield\" onchange=\"changePartnerPatternNameScript()\">\n"
	 * ); html.append(clubSelect); html.append("</select>\n"); }
	 * 
	 * return html.toString(); }
	 */

	public String getWindowTermLoyalityHistory(String pField, String pValue,
			String pIdTerm, String pIdServicePlace, String pSize) {
		StringBuilder html = new StringBuilder();

		String pName = getTermLoyalityHistoryName(pValue);

		html.append("<input type=\"hidden\" id=\"id_" + pField
				+ "\" name=\"id_" + pField + "\" value=\"" + pValue + "\">\n");
		html.append("<input type=\"text\" id=\"name_" + pField
				+ "\" name=\"name_" + pField + "\" size=\"" + pSize
				+ "\" value=\"" + pName
				+ "\" readonly class=\"inputfield\" title=\"" + pName + "\">\n");
		html.append("<A HREF=\"#\" onClick=\"JavaScript: var cWin=window.open('"
				+ "../crm/services/findtermloyhistory.jsp?id_term="
				+ pIdTerm
				+ "&id_service_place="
				+ pIdServicePlace
				+ "&id_loyality_history="
				+ pValue
				+ "','blank','height=600,width=900,top=50,left=150,toolbar=no,menubar=no,location=no,scrollbars=yes,status=yes'); cWin.focus();\">\n");
		html.append(getImgChange());
		html.append("<A HREF=\"#\" onClick=\"JavaScript: document.getElementById('id_"
				+ pField
				+ "').value = ''; document.getElementById('name_"
				+ pField + "').value = ''; return false;\">\n");
		html.append(getImgDelete());

		return html.toString();
	}

	public String getWindowFindClub(String pField, String pValue, String pSize) {
		return getWindowFindClub(pField, pValue, "", pSize, "");
	}

	public String getWindowFindClub(String pField, String pValue,
			String pShortNameClub, String pSize) {
		return getWindowFindClub(pField, pValue, pShortNameClub, pSize, "");
	}

	public String getWindowFindClub(String pField, String pValue,
			String pShortNameClub, String pSize, String pOnChangeScriptName) {
		StringBuilder html = new StringBuilder();

		String clubSelect = getSelectBodyFromSizedQuery(
				" SELECT id_club, sname_club " + "   FROM "
						+ getGeneralDBScheme() + ".vc_club_names "
						+ "  ORDER BY sname_club", pValue, pSize, true, false);

		String lClubName = "";
		if (isEmpty(pShortNameClub)) {
			lClubName = pShortNameClub;
		} else {
			lClubName = this.getClubShortName(pValue);
		}

		if (this.selectOprionCount > this.maxSelectOption) {
			html.append("<input type=\"hidden\" id=\"id_" + pField
					+ "\" name=\"id_" + pField + "\" value=\"" + pValue
					+ "\">\n");
			html.append("<input type=\"text\" id=\"name_" + pField
					+ "\" name=\"name_" + pField + "\" size=\"" + pSize
					+ "\" value=\"" + lClubName
					+ "\" readonly class=\"inputfield-ro\" title=\"" + lClubName
					+ "\">\n");
			html.append("<A HREF=\"#\" onClick=\"JavaScript: "
					+ pOnChangeScriptName
					+ "var cWin=window.open('"
					+ "../crm/services/findclub.jsp?id_club='+document.getElementById('id_"
					+ pField
					+ "').value+'&field="
					+ pField
					+ "','blank','height=600,width=900,top=50,left=150,toolbar=no,menubar=no,location=no,scrollbars=yes,status=yes'); cWin.focus();\">\n");
			html.append(getImgChange());
			html.append("<A HREF=\"#\" onClick=\"JavaScript: document.getElementById('id_"
					+ pField
					+ "').value = ''; document.getElementById('name_"
					+ pField + "').value = ''; return false;\">\n");
			html.append(getImgDelete());
		} else {
			html.append("<script type=\"text/javascript\">");
			html.append("function changeClubNameScript() {");
			html.append("   document.getElementById('id_" + pField
					+ "').value = document.getElementById('name_" + pField
					+ "').value;");
			// html.append("   alert(document.getElementById('name_" + pField +
			// "').value);");
			html.append("}");
			html.append("changeClubNameScript();");
			html.append("</script>");
			html.append("<input type=\"hidden\" id=\"id_" + pField
					+ "\" name=\"id_" + pField + "\" value=\"" + pValue
					+ "\">\n");
			html.append("<select name=\"name_"
					+ pField
					+ "\" id=\"name_"
					+ pField
					+ "\" class=\"inputfield\" onchange=\"changeClubNameScript(); "
					+ pOnChangeScriptName + "\">\n");
			html.append(clubSelect);
			html.append("</select>\n");
		}

		return html.toString();
	}

	public String getWindowFindTargetPrg(String pField, String pValue,
			String pName, String pSize) {
		StringBuilder html = new StringBuilder();

		String targetPrgSelect = getSelectBodyFromSizedQuery(
				" SELECT id_target_prg, name_target_prg " + "   FROM "
						+ getGeneralDBScheme() + ".vc_target_prg_all "
						+ "  ORDER BY name_target_prg", pValue, pSize, true, false);

		String lName = isEmpty(pName)?"":this.getTargetPrgName(pValue);
		
		if (this.selectOprionCount > this.maxSelectOption) {
			html.append("<input type=\"hidden\" id=\"id_" + pField
					+ "\" name=\"id_" + pField + "\" value=\"" + pValue
					+ "\">\n");
			html.append("<input type=\"text\" id=\"name_" + pField
					+ "\" name=\"name_" + pField + "\" size=\"" + pSize
					+ "\" value=\"" + lName
					+ "\" readonly class=\"inputfield-ro\" title=\"" + lName
					+ "\">\n");
			html.append("<A HREF=\"#\" onClick=\"JavaScript: "
					+ "var cWin=window.open('"
					+ "../crm/services/findtargetprg.jsp?id_club='+document.getElementById('id_"
					+ pField
					+ "').value+'&field="
					+ pField
					+ "','blank','height=600,width=900,top=50,left=150,toolbar=no,menubar=no,location=no,scrollbars=yes,status=yes'); cWin.focus();\">\n");
			html.append(getImgChange());
			html.append("<A HREF=\"#\" onClick=\"JavaScript: document.getElementById('id_"
					+ pField
					+ "').value = ''; document.getElementById('name_"
					+ pField + "').value = ''; return false;\">\n");
			html.append(getImgDelete());
		} else {
			html.append("<script type=\"text/javascript\">");
			html.append("function changeTargetPrgNameScript() {");
			html.append("   document.getElementById('id_" + pField
					+ "').value = document.getElementById('name_" + pField
					+ "').value;");
			// html.append("   alert(document.getElementById('name_" + pField +
			// "').value);");
			html.append("}");
			html.append("changeClubNameScript();");
			html.append("</script>");
			html.append("<input type=\"hidden\" id=\"id_" + pField
					+ "\" name=\"id_" + pField + "\" value=\"" + pValue
					+ "\">\n");
			html.append("<select name=\"name_"
					+ pField
					+ "\" id=\"name_"
					+ pField
					+ "\" class=\"inputfield\" onchange=\"changeTargetPrgNameScript(); "
					+ "\">\n");
			html.append(targetPrgSelect);
			html.append("</select>\n");
		}

		return html.toString();
	}

	public String getWindowFindClubActionGivenPlaces(String pField,
			String pIdClubAction, String pGiftsGivenPlace, String pValue,
			String pName, String pSize) {
		StringBuilder html = new StringBuilder();

		html.append("<input type=\"hidden\" id=\"id_" + pField
				+ "\" name=\"id_" + pField + "\" value=\"" + pValue + "\">\n");
		html.append("<input type=\"text\" id=\"name_" + pField
				+ "\" name=\"name_" + pField + "\" size=\"" + pSize
				+ "\" value=\"" + pName
				+ "\" readonly class=\"inputfield-ro\" title=\"" + pName + "\">\n");
		html.append("<A HREF=\"#\" onClick=\"JavaScript: var cWin=window.open('"
				+ "../crm/services/findclubactiongivenplaces.jsp?id_club_event="
				+ pIdClubAction
				+ "&id_gifts_given_place="
				+ pGiftsGivenPlace
				+ "&id_ca_given_place='+document.getElementById('id_"
				+ pField
				+ "').value + '&name_ca_given_place="
				+ pName
				+ "','blank','height=600,width=900,top=50,left=150,toolbar=no,menubar=no,location=no,scrollbars=yes,status=yes'); cWin.focus();\">\n");
		html.append(getImgChange());
		html.append("<A HREF=\"#\" onClick=\"JavaScript: document.getElementById('id_"
				+ pField
				+ "').value = ''; document.getElementById('name_"
				+ pField + "').value = ''; return false;\">\n");
		html.append(getImgDelete());

		return html.toString();
	}

	public String getWindowFindClubActionGivenSchedule(String pField,
			String pIdClubAction, String pValue, String pName, String pSize) {
		StringBuilder html = new StringBuilder();

		html.append("<input type=\"hidden\" id=\"id_" + pField
				+ "\" name=\"id_" + pField + "\" value=\"" + pValue + "\">\n");
		html.append("<input type=\"text\" id=\"name_" + pField
				+ "\" name=\"name_" + pField + "\" size=\"" + pSize
				+ "\" value=\"" + pName
				+ "\" readonly class=\"inputfield-ro\" title=\"" + pName + "\">\n");
		html.append("<A HREF=\"#\" onClick=\"JavaScript: var cWin=window.open('"
				+ "../crm/services/findclubactiongivenschedule.jsp?id_club_event="
				+ pIdClubAction
				+ "&id_ca_given_schedule='+document.getElementById('id_"
				+ pField
				+ "').value + '&name_ca_given_schedule="
				+ pName
				+ "','blank','height=600,width=900,top=50,left=150,toolbar=no,menubar=no,location=no,scrollbars=yes,status=yes'); cWin.focus();\">\n");
		html.append(getImgChange());
		html.append("<A HREF=\"#\" onClick=\"JavaScript: document.getElementById('id_"
				+ pField
				+ "').value = ''; document.getElementById('name_"
				+ pField + "').value = ''; return false;\">\n");
		html.append(getImgDelete());

		return html.toString();
	}

	public String getWindowFindClubActionGifts(String pField,
			String pIdClubAction, String pValue, String pName, String pSize) {
		StringBuilder html = new StringBuilder();

		html.append("<input type=\"hidden\" id=\"id_" + pField
				+ "\" name=\"id_" + pField + "\" value=\"" + pValue + "\">\n");
		html.append("<input type=\"text\" id=\"name_" + pField
				+ "\" name=\"name_" + pField + "\" size=\"" + pSize
				+ "\" value=\"" + pName
				+ "\" readonly class=\"inputfield-ro\" title=\"" + pName + "\">\n");
		html.append("<A HREF=\"#\" onClick=\"JavaScript: var cWin=window.open('"
				+ "../crm/services/findclubactiongifts.jsp?id_club_event="
				+ pIdClubAction
				+ "&id_gift='+document.getElementById('id_"
				+ pField
				+ "').value + '&name_gift="
				+ pName
				+ "','blank','height=600,width=900,top=50,left=150,toolbar=no,menubar=no,location=no,scrollbars=yes,status=yes'); cWin.focus();\">\n");
		html.append(getImgChange());
		html.append("<A HREF=\"#\" onClick=\"JavaScript: document.getElementById('id_"
				+ pField
				+ "').value = ''; document.getElementById('name_"
				+ pField + "').value = ''; return false;\">\n");
		html.append(getImgDelete());

		return html.toString();
	}

	public String getWindowFindGiftsLogistic(String pField, String pIdGift,
			String pValue, String pName, String pSize) {
		StringBuilder html = new StringBuilder();

		html.append("<input type=\"hidden\" id=\"id_" + pField
				+ "\" name=\"id_" + pField + "\" value=\"" + pValue + "\">\n");
		html.append("<input type=\"text\" id=\"name_" + pField
				+ "\" name=\"name_" + pField + "\" size=\"" + pSize
				+ "\" value=\"" + pName
				+ "\" readonly class=\"inputfield-ro\" title=\"" + pName + "\">\n");
		html.append("<A HREF=\"#\" onClick=\"JavaScript: var cWin=window.open('"
				+ "../crm/services/findgiftslogistic.jsp?id_gift="
				+ pIdGift
				+ "&id_lg_gift='+document.getElementById('id_"
				+ pField
				+ "').value + '&desc_gift="
				+ pName
				+ "','blank','height=600,width=900,top=50,left=150,toolbar=no,menubar=no,location=no,scrollbars=yes,status=yes'); cWin.focus();\">\n");
		html.append(getImgChange());
		html.append("<A HREF=\"#\" onClick=\"JavaScript: document.getElementById('id_"
				+ pField
				+ "').value = ''; document.getElementById('name_"
				+ pField + "').value = ''; return false;\">\n");
		html.append(getImgDelete());

		return html.toString();
	}

	public String getWindowFindClubAction(String pField, String pValue,
			String pName, String pSize) {
		StringBuilder html = new StringBuilder();

		html.append("<input type=\"hidden\" id=\"id_" + pField
				+ "\" name=\"id_" + pField + "\" value=\"" + pValue + "\">\n");
		html.append("<input type=\"text\" id=\"name_" + pField
				+ "\" name=\"name_" + pField + "\" size=\"" + pSize
				+ "\" value=\"" + pName
				+ "\" readonly class=\"inputfield-ro\" title=\"" + pName + "\">\n");
		html.append("<A HREF=\"#\" onClick=\"JavaScript: var cWin=window.open('"
				+ "../crm/services/findclubactions.jsp?id_club_event='+document.getElementById('id_"
				+ pField
				+ "').value + '&name_club_event="
				+ pName
				+ "','blank','height=600,width=900,top=50,left=150,toolbar=no,menubar=no,location=no,scrollbars=yes,status=yes'); cWin.focus();\">\n");
		html.append(getImgChange());
		html.append("<A HREF=\"#\" onClick=\"JavaScript: document.getElementById('id_"
				+ pField
				+ "').value = ''; document.getElementById('name_"
				+ pField + "').value = ''; return false;\">\n");
		html.append(getImgDelete());

		return html.toString();
	}

	public String getWindowFindTrainingPerson(String pField, String pValue,
			String pName, String pSize) {
		StringBuilder html = new StringBuilder();

		html.append("<input type=\"hidden\" id=\"id_" + pField
				+ "\" name=\"id_" + pField + "\" value=\"" + pValue + "\">\n");
		html.append("<input type=\"text\" id=\"name_" + pField
				+ "\" name=\"name_" + pField + "\" size=\"" + pSize
				+ "\" value=\"" + pName
				+ "\" readonly class=\"inputfield-ro\" title=\"" + pName + "\">\n");
		html.append("<A HREF=\"#\" onClick=\"JavaScript: var cWin=window.open('"
				+ "../crm/services/findtrainingperson.jsp?id_person='+document.getElementById('id_"
				+ pField
				+ "').value + '&name_person="
				+ pName
				+ "&field="
				+ pField
				+ "','blank','height=600,width=900,top=50,left=150,toolbar=no,menubar=no,location=no,scrollbars=yes,status=yes'); cWin.focus();\">\n");
		html.append(getImgChange());
		html.append("<A HREF=\"#\" onClick=\"JavaScript: document.getElementById('id_"
				+ pField
				+ "').value = ''; document.getElementById('name_"
				+ pField + "').value = ''; return false;\">\n");
		html.append(getImgDelete());

		return html.toString();
	}
	

	String imgWidth = "30";
	String imgHeight = "30";
	
	private String getMenuButtonTitle(String pType) {
		String lTitle = "";
		if ("PRINT".equalsIgnoreCase(pType)) {
			lTitle = buttonXML.getfieldTransl("button_print",
					false);
		} else if ("ADD".equalsIgnoreCase(pType)) {
			lTitle = buttonXML.getfieldTransl("button_add",
					false);
		} else if ("ADD_ALL".equalsIgnoreCase(pType)) {
			lTitle = buttonXML.getfieldTransl("button_add_all",
					false);
		} else if ("ADD_LINE".equalsIgnoreCase(pType)) {
			lTitle = buttonXML.getfieldTransl("button_add",
					false);
		} else if ("COPY".equalsIgnoreCase(pType)) {
			lTitle = buttonXML.getfieldTransl("button_copy",
					false);
		} else if ("EDIT_ALL".equalsIgnoreCase(pType)) {
			lTitle = buttonXML.getfieldTransl("button_add",
					false);
		} else if ("DELETE".equalsIgnoreCase(pType)) {
			lTitle = buttonXML.getfieldTransl("button_delete",
					false);
		} else if ("DELETE_ALL".equalsIgnoreCase(pType)) {
			lTitle = buttonXML.getfieldTransl(
					"button_delete_all", false);
		} else if ("CHECK".equalsIgnoreCase(pType)) {
			lTitle = buttonXML.getfieldTransl("button_check",
					false);
		} else if ("CHECK_ALL".equalsIgnoreCase(pType)) {
			lTitle = buttonXML.getfieldTransl("button_checkall",
					false);
		} else if ("POSTING".equalsIgnoreCase(pType)) {
			lTitle = buttonXML.getfieldTransl("button_posting",
					false);
		} else if ("CREATE".equalsIgnoreCase(pType)) {
			lTitle = buttonXML.getfieldTransl("button_create",
					false);
		} else if ("APPLY".equalsIgnoreCase(pType)) {
			lTitle = buttonXML.getfieldTransl("button_apply",
					false);
		} else if ("DETACH".equalsIgnoreCase(pType)) {
			lTitle = buttonXML.getfieldTransl(
					"button_apply_minus", false);
		} else if ("RUN".equalsIgnoreCase(pType)) {
			lTitle = buttonXML.getfieldTransl("button_run",
					false);
		} else if ("RUN_PARAM".equalsIgnoreCase(pType)) {
			lTitle = buttonXML.getfieldTransl("button_run",
					false);
		} else if ("IMPORT".equalsIgnoreCase(pType)) {
			lTitle = buttonXML.getfieldTransl("import", false);
		} else if ("IMPORT_ALL".equalsIgnoreCase(pType)) {
			lTitle = buttonXML.getfieldTransl(
					"button_importall", false);
		} else if ("EXPORT".equalsIgnoreCase(pType)) {
			lTitle = buttonXML.getfieldTransl("export", false);
		} else if ("LOAD_FROM_FILE".equalsIgnoreCase(pType)) {
			lTitle = buttonXML.getfieldTransl(
					"button_load_from_file", false);
		} else if ("HISTORY".equalsIgnoreCase(pType)) {
			lTitle = buttonXML.getfieldTransl("button_history",
					false);
		}
		return lTitle;
	}

	public String getMenuButton(String pType, String pHyperLink,
			String pParam1, String pParam2) {
		String lTitle = getMenuButtonTitle(pType);
		return getMenuButtonBase(pType, pHyperLink, pParam1, pParam2, lTitle, "div_main");
	}

	public String getMenuButton(String pType, String pHyperLink,
			String pParam1, String pParam2, String pTitle) {
		String lTitle = pTitle;
		if (isEmpty(lTitle)) {
			lTitle = getMenuButtonTitle(pType);
		}
		return getMenuButtonBase(pType, pHyperLink, pParam1, pParam2, lTitle, "div_main");
	}
	
	public String getClubDirectory(String pIdClub, String pFieldName) {
		bcClubDirectoriesObject dir = new bcClubDirectoriesObject(pIdClub);
		return dir.getValue(pFieldName);
	}
	
	public String getClubDocFilesDirectory(String pIdClub) {
		return getClubDirectory(pIdClub, "DOC_FILE_DIR");
	}
	
	public String getClubTermCertificatesDirectory(String pIdClub) {
		return getClubDirectory(pIdClub, "TERM_CERTIFICATES_DIR");
	}
	
	public String getClubBKExportDirectory(String pIdClub) {
		return getClubDirectory(pIdClub, "BK_EXPORT_DIR");
	}
	
	public String getClubClearingExportDirectory(String pIdClub) {
		return getClubDirectory(pIdClub, "CLEARING_EXPORT_DIR");
	}
	
	public String getClubBankStatementImportDirectory(String pIdClub) {
		return getClubDirectory(pIdClub, "BANK_STATEMENT_IMPORT_DIR");
	}
	
	public String getClubDocFilesDirectory(bcClubShortObject pClub) {
		return pClub.getValue("DOC_FILE_DIR");
	}
	
	public String getClubTermCertificatesDirectory(bcClubShortObject pClub) {
		return pClub.getValue("TERM_CERTIFICATES_DIR");
	}
	
	public String getClubBKExportDirectory(bcClubShortObject pClub) {
		return pClub.getValue("BK_EXPORT_DIR");
	}
	
	public String getClubClearingExportDirectory(bcClubShortObject pClub) {
		return pClub.getValue("CLEARING_EXPORT_DIR");
	}
	
	public String getClubBankStatementImportDirectory(bcClubShortObject pClub) {
		return pClub.getValue("BANK_STATEMENT_IMPORT_DIR");
	}

	public String getMenuButtonBase(String pType, String pHyperLink,
			String pParam1, String pParam2, String pTitle, String pDivName) {
		String return_value = "";
		String lImage = "";
		String lConfirmFirst = "";
		String lConfirmLast = "";
		
		String lTitle = pTitle;
		if (isEmpty(lTitle)) {
			lTitle = getMenuButtonTitle(pType);
		}

		if (isEmpty(pType)) {
			return_value = "";
		} else if ("PRINT".equalsIgnoreCase(pType)) {
			lImage = "<img height=\""
					+ imgHeight
					+ "\" width=\""
					+ imgWidth
					+ "\" vspace=\"0\" hspace=\"0\""
					+ " src=\""
					+ "../images/oper/print2.png\" align=\"top\" style=\"border: 0px;\""
					+ " title=\"" + lTitle + "\">";
			return_value = "<td width=\"20\"><a href=\"" + pHyperLink + "\" target=\"frm_print\">" + lImage
					+ "</a></td>";
		} else {
			if ("ADD".equalsIgnoreCase(pType)) {
				lImage = "<img height=\""
						+ imgHeight
						+ "\" width=\""
						+ imgWidth
						+ "\" vspace=\"0\" hspace=\"0\""
						+ " src=\""
						+ "../images/oper/add.png\" align=\"top\" style=\"border: 0px;\""
						+ " title=\"" + lTitle + "\">";
			} else if ("ADD_ALL".equalsIgnoreCase(pType)) {
				lImage = "<img height=\""
						+ imgHeight
						+ "\" width=\""
						+ imgWidth
						+ "\" vspace=\"0\" hspace=\"0\""
						+ " src=\""
						+ "../images/oper/add_all.png\" align=\"top\" style=\"border: 0px;\""
						+ " title=\"" + lTitle + "\">";
			} else if ("ADD_LINE".equalsIgnoreCase(pType)) {
				lImage = "<img height=\""
						+ imgHeight
						+ "\" width=\""
						+ imgWidth
						+ "\" vspace=\"0\" hspace=\"0\""
						+ " src=\""
						+ "../images/oper/add.png\" align=\"top\" style=\"border: 0px;\""
						+ " title=\"" + lTitle + "\">";
			} else if ("COPY".equalsIgnoreCase(pType)) {
				lImage = "<img height=\""
						+ imgHeight
						+ "\" width=\""
						+ imgWidth
						+ "\" vspace=\"0\" hspace=\"0\""
						+ " src=\""
						+ "../images/oper/copy.png\" align=\"top\" style=\"border: 0px;\""
						+ " title=\"" + lTitle + "\">";
			} else if ("EDIT_ALL".equalsIgnoreCase(pType)) {
				lImage = "<img height=\""
						+ imgHeight
						+ "\" width=\""
						+ imgWidth
						+ "\" vspace=\"0\" hspace=\"0\""
						+ " src=\""
						+ "../images/oper/edit.png\" align=\"top\" style=\"border: 0px;\""
						+ " title=\"" + lTitle + "\">";
			} else if ("DELETE".equalsIgnoreCase(pType)) {
				lImage = "<img height=\""
						+ imgHeight
						+ "\" width=\""
						+ imgWidth
						+ "\" vspace=\"0\" hspace=\"0\""
						+ " src=\""
						+ "../images/oper/delete.png\" align=\"top\" style=\"border: 0px;\""
						+ " title=\"" + lTitle + "\">";
			} else if ("DELETE_ALL".equalsIgnoreCase(pType)) {
				lImage = "<img height=\""
						+ imgHeight
						+ "\" width=\""
						+ imgWidth
						+ "\" vspace=\"0\" hspace=\"0\""
						+ " src=\""
						+ "../images/oper/delete_all.png\" align=\"top\" style=\"border: 0px;\""
						+ " title=\"" + lTitle + "\">";
			} else if ("CHECK".equalsIgnoreCase(pType)) {
				lImage = "<img height=\""
						+ imgHeight
						+ "\" width=\""
						+ imgWidth
						+ "\" vspace=\"0\" hspace=\"0\""
						+ " src=\""
						+ "../images/oper/check.png\" align=\"top\" style=\"border: 0px;\""
						+ " title=\"" + lTitle + "\">";
			} else if ("CHECK_ALL".equalsIgnoreCase(pType)) {
				lImage = "<img height=\""
						+ imgHeight
						+ "\" width=\""
						+ imgWidth
						+ "\" vspace=\"0\" hspace=\"0\""
						+ " src=\""
						+ "../images/oper/check_all.png\" align=\"top\" style=\"border: 0px;\""
						+ " title=\"" + lTitle + "\">";
			} else if ("POSTING".equalsIgnoreCase(pType)) {
				lImage = "<img height=\""
						+ imgHeight
						+ "\" width=\""
						+ imgWidth
						+ "\" vspace=\"0\" hspace=\"0\""
						+ " src=\""
						+ "../images/oper/posting.png\" align=\"top\" style=\"border: 0px;\""
						+ " title=\"" + lTitle + "\">";
			} else if ("CANCEL".equalsIgnoreCase(pType)) {
				lImage = "<img height=\""
						+ imgHeight
						+ "\" width=\""
						+ imgWidth
						+ "\" vspace=\"0\" hspace=\"0\""
						+ " src=\""
						+ "../images/oper/cancel.png\" align=\"top\" style=\"border: 0px;\""
						+ " title=\"" + lTitle + "\">";
			} else if ("CREATE".equalsIgnoreCase(pType)) {
				lImage = "<img height=\""
						+ imgHeight
						+ "\" width=\""
						+ imgWidth
						+ "\" vspace=\"0\" hspace=\"0\""
						+ " src=\""
						+ "../images/row_create.gif\" align=\"top\" style=\"border: 0px;\""
						+ " title=\"" + lTitle + "\">";
			} else if ("APPLY".equalsIgnoreCase(pType)) {
				lImage = "<img height=\""
						+ imgHeight
						+ "\" width=\""
						+ imgWidth
						+ "\" vspace=\"0\" hspace=\"0\""
						+ " src=\""
						+ "../images/oper/apply.png\" align=\"top\" style=\"border: 0px;\""
						+ " title=\"" + lTitle + "\">";
			} else if ("DETACH".equalsIgnoreCase(pType)) {
				lImage = "<img height=\""
						+ imgHeight
						+ "\" width=\""
						+ imgWidth
						+ "\" vspace=\"0\" hspace=\"0\""
						+ " src=\""
						+ "../images/oper/detach.png\" align=\"top\" style=\"border: 0px;\""
						+ " title=\"" + lTitle + "\">";
			} else if ("RUN".equalsIgnoreCase(pType)) {
				lImage = "<img height=\""
						+ imgHeight
						+ "\" width=\""
						+ imgWidth
						+ "\" vspace=\"0\" hspace=\"0\""
						+ " src=\""
						+ "../images/oper/run.png\" align=\"top\" style=\"border: 0px;\""
						+ " title=\"" + lTitle + "\">";
			} else if ("RUN_PARAM".equalsIgnoreCase(pType)) {
				lImage = "<img height=\""
						+ imgHeight
						+ "\" width=\""
						+ imgWidth
						+ "\" vspace=\"0\" hspace=\"0\""
						+ " src=\""
						+ "../images/oper/run.png\" align=\"top\" style=\"border: 0px;\""
						+ " title=\"" + lTitle + "\">";
			} else if ("IMPORT".equalsIgnoreCase(pType)) {
				lImage = "<img height=\""
						+ imgHeight
						+ "\" width=\""
						+ imgWidth
						+ "\" vspace=\"0\" hspace=\"0\""
						+ " src=\""
						+ "../images/oper/import.png\" align=\"top\" style=\"border: 0px;\""
						+ " title=\"" + lTitle + "\">";
			} else if ("IMPORT_ALL".equalsIgnoreCase(pType)) {
				lImage = "<img height=\""
						+ imgHeight
						+ "\" width=\""
						+ imgWidth
						+ "\" vspace=\"0\" hspace=\"0\""
						+ " src=\""
						+ "../images/oper/import.png\" align=\"top\" style=\"border: 0px;\""
						+ " title=\"" + lTitle + "\">";
			} else if ("EXPORT".equalsIgnoreCase(pType)) {
				lImage = "<img height=\""
						+ imgHeight
						+ "\" width=\""
						+ imgWidth
						+ "\" vspace=\"0\" hspace=\"0\""
						+ " src=\""
						+ "../images/oper/export.png\" align=\"top\" style=\"border: 0px;\""
						+ " title=\"" + lTitle + "\">";
			} else if ("LOAD_FROM_FILE".equalsIgnoreCase(pType)) {
				lImage = "<img height=\""
						+ imgHeight
						+ "\" width=\""
						+ imgWidth
						+ "\" vspace=\"0\" hspace=\"0\""
						+ " src=\""
						+ "../images/oper/load.png\" align=\"top\" style=\"border: 0px;\""
						+ " title=\"" + lTitle + "\">";
			} else if ("REPEAT".equalsIgnoreCase(pType)) {
				lImage = "<img height=\""
						+ imgHeight
						+ "\" width=\""
						+ imgWidth
						+ "\" vspace=\"0\" hspace=\"0\""
						+ " src=\""
						+ "../images/oper/repeat.png\" align=\"top\" style=\"border: 0px;\""
						+ " title=\"" + lTitle + "\">";
			} else if ("HISTORY".equalsIgnoreCase(pType)) {
				lImage = "<img height=\""
						+ imgHeight
						+ "\" width=\""
						+ imgWidth
						+ "\" vspace=\"0\" hspace=\"0\""
						+ " src=\""
						+ "../images/oper/history.png\" align=\"top\" style=\"border: 0px;\""
						+ " title=\"" + lTitle + "\">";
			}
			if (("DELETE".equalsIgnoreCase(pType)
					|| "DELETE_ALL".equalsIgnoreCase(pType)
					|| "ADD_ALL".equalsIgnoreCase(pType)
					|| "POSTING".equalsIgnoreCase(pType)
					|| "CREATE".equalsIgnoreCase(pType)
					|| "CANCEL".equalsIgnoreCase(pType)
					|| "CHECK".equalsIgnoreCase(pType)
					|| "CHECK_ALL".equalsIgnoreCase(pType)
					|| "APPLY".equalsIgnoreCase(pType)
					|| "DETACH".equalsIgnoreCase(pType)
					|| "RUN".equalsIgnoreCase(pType)
					|| "IMPORT_ALL".equalsIgnoreCase(pType) || "LOAD_FROM_FILE"
						.equalsIgnoreCase(pType))
					&& (!isEmpty(pParam1))) {
				if (isEmpty(pParam2)) {
					lConfirmFirst = "var msg='" + pParam1 + "?';"
							+ "var res=window.confirm(msg); " + "if (res) {";
				} else {
					lConfirmFirst = "var msg='" + pParam1 + " \\\'" + pParam2
							+ "\\\'?';" + "var res=window.confirm(msg); "
							+ "if (res) {";
				}
				lConfirmLast = "}";
				return_value = "<td width=\"20\"><div class=\"div_button\" onclick=\""
						+ lConfirmFirst
						+ " ajaxpage('"
						+ pHyperLink
						+ "', '" + pDivName + "');"
						+ lConfirmLast
						+ "\">"
						+ lImage + getHyperLinkEnd() + "</td>";
			} else {
				return_value = "<td width=\"20\"><div class=\"div_button\" onclick=\""
						+ lConfirmFirst
						+ "ajaxpage('"
						+ pHyperLink
						+ "', '" + pDivName + "')"
						+ lConfirmLast
						+ "\">"
						+ lImage + getHyperLinkEnd() + "</td>";
			}

		}
		return return_value;
	}

	public String getPageHeader(String pHeader) {
		StringBuilder html = new StringBuilder();
		boolean hasHelp = false;
		if (isEmpty(currentMenu.getCurrentTabIdMenuElement())) {
			if ("Y".equalsIgnoreCase(currentMenu.getHasHelpFlag())) {
				hasHelp = true;
			}
		} else {
			if ("Y".equalsIgnoreCase(currentMenu.getCurrentTaHasHelpFlag())) {
				hasHelp = true;
			}
		}

		String lHeader = isEmpty(pHeader)?getHeaderName():pHeader;
		
		//html.append("<td style=\"width:100px;\">&nbsp;</td>");
		html.append(this.getLinkButtons());
		html.append("<td style=\"width:20px;\"><img id=\"imgWait\" src=\""
				+ "../images/ajax-loader-circle.gif\" align=\"middle\" style=\"visibility: hidden\"></td>");
		html.append("<td align=\"left\"><div class=\"div_title\" onclick=\"ajaxpage('"
				+ "../"
				+ currentMenu.getCurrentPage()
				+ ".jsp"
				+ getHyperLinkMiddle()
				+ "<font class=\"font_title\">"
				+ lHeader + "</font>" + getHyperLinkEnd() + "</td>");
		html.append("<td width=\"20\"><div class=\"div_button\" onclick=\"ajaxpage('"
				+ "../admin/warningsupdate.jsp?type=general&action=add_form&process=no&id_menu="
				+ currentMenu.getIdMenuElement()
				+ "&id_tab="
				+ currentMenu.getCurrentTabIdMenuElement()
				+ "', 'div_main')\">"
				+ "<img height=\""
				+ imgHeight
				+ "\" width=\""
				+ imgWidth
				+ "\" vspace=\"0\" hspace=\"0\""
				+ " src=\""
				+ "../images/oper/warning.png\" align=\"top\" style=\"border: 0px;\""
				+ " title=\""
				+ buttonXML.getfieldTransl("button_add_warning",
						false) + "\">" + getHyperLinkEnd() + "</td>");
		if (hasHelp) {
			html.append("<td width=\"20\"><a href=\""
					+ "../admin/help.jsp?id_menu="
					+ currentMenu.getIdMenuElement()
					+ "&id_tab="
					+ currentMenu.getCurrentTabIdMenuElement()
					+ "\" target=\"frm_help\">"
					+ "<img height=\""
					+ imgHeight
					+ "\" width=\""
					+ imgWidth
					+ "\" vspace=\"0\" hspace=\"0\""
					+ " src=\""
					+ "../images/oper/help.png\" align=\"top\" style=\"border: 0px;\""
					+ " title=\""
					+ buttonXML.getfieldTransl("button_help",
							false) + "\"></a></td>");
		}
		return html.toString();
	}

	public String getPageHeader(String pHeader, String pHyperLink) {
		StringBuilder html = new StringBuilder();
		String lHeader = "";
		if (isEmpty(pHeader)) {
			LOGGER.debug("pHeader is empty");
			if (isEmpty(currentMenu.getTitleMenuElement())) {
				lHeader = pHeader;
			} else {
				lHeader = currentMenu.getTitleMenuElement();
				if (!isEmpty(pHeader)) {
					lHeader = lHeader + pHeader;
				}
			}
		} else {
			lHeader = pHeader;
		}

		boolean hasHelp = false;
		if (isEmpty(currentMenu.getCurrentTabIdMenuElement())) {
			if ("Y".equalsIgnoreCase(currentMenu.getHasHelpFlag())) {
				hasHelp = true;
			}
		} else {
			if ("Y".equalsIgnoreCase(currentMenu.getCurrentTaHasHelpFlag())) {
				hasHelp = true;
			}
		}
		//html.append("<td style=\"width:100px;\">&nbsp;</td>");
		html.append(this.getLinkButtons());
		html.append("<td style=\"width:20px;\"><img id=\"imgWait\" src=\""
				+ "../images/ajax-loader-circle.gif\" align=\"middle\" style=\"visibility: hidden\"></td>");
		html.append("<td align=\"left\"><div class=\"div_title\" onclick=\"ajaxpage('"
				+ pHyperLink
				+ getHyperLinkMiddle()
				+ "<font class=\"font_title\">"
				+ lHeader
				+ "</font>"
				+ getHyperLinkEnd() + "</td>");

		html.append("<td width=\"20\"><div class=\"div_button\" onclick=\"ajaxpage('"
				+ "../admin/warningsupdate.jsp?type=general&action=add_form&process=no&id_menu="
				+ currentMenu.getIdMenuElement()
				+ "&id_tab="
				+ currentMenu.getCurrentTabIdMenuElement()
				+ "', 'div_main')\">"
				+ "<img height=\""
				+ imgHeight
				+ "\" width=\""
				+ imgWidth
				+ "\" vspace=\"0\" hspace=\"0\""
				+ " src=\""
				+ "../images/oper/warning.png\" align=\"top\" style=\"border: 0px;\""
				+ " title=\""
				+ buttonXML.getfieldTransl("button_add_warning",
						false) + "\">" + getHyperLinkEnd() + "</td>");
		if (hasHelp) {
			html.append("<td width=\"20\"><a href=\""
				+ "../admin/help.jsp?id_menu="
				+ currentMenu.getIdMenuElement()
				+ "&id_tab="
				+ currentMenu.getCurrentTabIdMenuElement()
				+ "\" target=\"frm_help\">"
				+ "<img height=\""
				+ imgHeight
				+ "\" width=\""
				+ imgWidth
				+ "\" vspace=\"0\" hspace=\"0\""
				+ " src=\""
				+ "../images/oper/help.png\" align=\"top\" style=\"border: 0px;\""
				+ " title=\""
				+ buttonXML.getfieldTransl("button_help", false)
				+ "\"></a></td>");
		}
		return html.toString();
	}

	public String getPageHeader() {
		return getPageHeader("");
	}

	public String getIDNotFoundMessage() {
		StringBuilder html = new StringBuilder();

		html.append("<table " + this.getTableMenuParam() + "><tbody>\n");
		html.append("<tr>\n");

		html.append(this.getPageHeader());

		html.append("</tr>\n");
		html.append("<tr>\n");
		html.append("<td colspan=\"10\">\n");
		html.append(commonXML.getfieldTransl("H_ID_NOT_FOUND",
				false));
		html.append("</td>\n");
		html.append("</tr>\n");
		html.append("</tbody></table>");

		return html.toString();
	}

	public String getOperationTitle(String pTitle, String pNeedObv,
			String pNeedDateFormat) {

		StringBuilder html = new StringBuilder();
		String lObv = "";

		html.append("<table " + getTableMenuParam() + ">");
		html.append("<tr>");
		html.append(getPageHeader());
		html.append("</tr>");
		html.append("</table>");

		html.append("<div id=\"div_oper_caption\"><center><b style=\"font-size:14px;\">" + pTitle + "</b>");
		if ("Y".equalsIgnoreCase(pNeedObv)
				|| "Y".equalsIgnoreCase(pNeedDateFormat)) {
			html.append("&nbsp;<br>(");
		}
		if ("Y".equalsIgnoreCase(pNeedObv)) {
			lObv = commonXML.getFirstObligatoryText();
			if (!("".equalsIgnoreCase(commonXML.getFirstObligatoryText()))
					&& !("".equalsIgnoreCase(commonXML.getLastObligatoryText()))) {
				lObv = lObv + "\"Text\"";
			}
			lObv = lObv + commonXML.getLastObligatoryText();
			html.append(lObv
					+ "<i> - "
					+ commonXML.getfieldTransl("LAB_OBV_FIELDS", false) + "</i>");
		}
		if ("Y".equalsIgnoreCase(pNeedObv)
				&& "Y".equalsIgnoreCase(pNeedDateFormat)) {
			html.append(",&nbsp;");
		}
		if ("Y".equalsIgnoreCase(pNeedDateFormat)) {
			html.append("<i>"
					+ "<font color=\"red\"><b>" + commonXML.getfieldTransl("date_format", false) + "</b></font> - <b>"
					+ this.getDateFormatTitle() + "</b></i>");
		}
		if ("Y".equalsIgnoreCase(pNeedObv)
				|| "Y".equalsIgnoreCase(pNeedDateFormat)) {
			html.append(")");
		}
		html.append("</center><br></div>");
		html.append("<script type=\"text/javascript\" src=\""
				+ "../js/formValidate-"
				+ this.getLanguage() + ".js\"></script>");

		return html.toString();
	}

	public String getOperationTitleShort(String pColSpan, String pTitle, String pNeedObv,
			String pNeedDateFormat) {

		StringBuilder html = new StringBuilder();
		String lObv = "";

		html.append("<td colspan=4 align=\"center\" style=\"height: 40px; vertical-align: middle; font-size: 14px;\"><div id=\"div_oper_caption\"><br><b><center>" + pTitle + "</b><br>");
		if ("Y".equalsIgnoreCase(pNeedObv)
				|| "Y".equalsIgnoreCase(pNeedDateFormat)) {
			html.append("&nbsp;(");
		}
		if ("Y".equalsIgnoreCase(pNeedObv)) {
			lObv = commonXML.getFirstObligatoryText();
			if (!("".equalsIgnoreCase(commonXML.getFirstObligatoryText()))
					&& !("".equalsIgnoreCase(commonXML.getLastObligatoryText()))) {
				lObv = lObv + "\"Text\"";
			}
			lObv = lObv + commonXML.getLastObligatoryText();
			html.append(lObv
					+ "<i> - "
					+ commonXML.getfieldTransl("LAB_OBV_FIELDS", false) + "</i>");
		}
		if ("Y".equalsIgnoreCase(pNeedObv)
				&& "Y".equalsIgnoreCase(pNeedDateFormat)) {
			html.append(",&nbsp;");
		}
		if ("Y".equalsIgnoreCase(pNeedDateFormat)) {
			html.append("<i>"
					+ commonXML.getfieldTransl("date_format", false) + " - <b>"
					+ this.getDateFormat() + "</b></i>");
		}
		if ("Y".equalsIgnoreCase(pNeedObv)
				|| "Y".equalsIgnoreCase(pNeedDateFormat)) {
			html.append(")");
		}
		html.append("</center><br></div></tr>");

		return html.toString();
	}

	public String getUnknownActionText(String pAction) {

		StringBuilder html = new StringBuilder();
		html.append("<table " + this.getTableMenuParam() + ">");
		html.append("<tr>");
		html.append(this.getPageHeader());
		html.append("</tr><tr><td colspan=\"10\" align=\"center\">");
		html.append(this.form_messageXML.getfieldTransl("unknown_action", false) + " (action = " + pAction + ") <br>");
		html.append("</td></tr></table>");
		return html.toString();
	}

	public String getUnknownProcessText(String pProcess) {

		StringBuilder html = new StringBuilder();
		html.append("<table " + this.getTableMenuParam() + ">");
		html.append("<tr>");
		html.append(this.getPageHeader());
		html.append("</tr><tr><td colspan=\"10\" align=\"center\">");
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
		html.append("<tr>");
		html.append(this.getPageHeader());
		html.append("</tr><tr><td colspan=\"10\" align=\"center\">");
		html.append(this.form_messageXML.getfieldTransl("unktown_type", false) + " (type = " + pType + ") <br>");
		html.append("</td></tr></table>");
		return html.toString();
	}



	public String showCallSQL(String pCallSQL) {
		return "<br>callSQL = \"" + pCallSQL + "\"<br>";
	}

	private String callResultInt = "0";

	public String getCallResultInt() {
		return callResultInt;
	}


	public String getCallResultGeneral(String pMenu, String pTabName,
			String pType, String pCallSQL, String[] pParam,
			String pToHyperLink, String pBackHyperLink, String pManyHyperLink) {

		StringBuilder html = new StringBuilder();
		/*LOGGER.debug("getCallResultGeneral(): \n pMenu="+pMenu+
				"\n, pTabName="+pTabName+
				"\n, pType="+pType+
				"\n, pCallSQL="+pCallSQL+
				"\n, pToHyperLink="+pToHyperLink+
				"\n, pBackHyperLink="+pBackHyperLink+
				"\n, pManyHyperLink="+pManyHyperLink);*/
		String[] results = null;

		String resultMessage = "";
		String resultID = "";
		String errorString = "";
		String processingString = "";

		boolean hasEditPermission = false;

		if (!isEmpty(pMenu)) {
			hasEditPermission = hasEditMenuPermission(pMenu);
		} else if (!isEmpty(pTabName)) {
			hasEditPermission = hasEditTabsheetPermission(pTabName);
		} else {
			hasEditPermission = true;
		}
		html.append("<html><head>\n");
		html.append("<script type=\"text/javascript\" src=\"../js/ajax_form_submit.js\" > </script>\n");
		html.append("<script type=\"text/javascript\" src=\"../js/frame_emulator.js\" > </script>\n");
		html.append("<script type=\"text/javascript\" src=\"../crm/js/crm.js\" > </script>\n");
		html.append("</head><body>\n");
		
		//html.append("<table " + getTableMenuParam() + ">");
		//html.append("<tr>");
		//html.append(getPageHeader());
		//html.append("</tr>");
		//html.append("</table>");

		if (!hasEditPermission) {
			String errorPermissionString = this.commonXML.getfieldTransl("h_error_edit_permission", false);
			String backString = this.buttonXML.getfieldTransl("go_back", false);

			html.append("<font color=\"red\">" + errorPermissionString
					+ "</font> <br>\n");
			html.append("<center><font color=\"blue\"><u><a href=\"#\" onclick=\"goBack();\">"
					+ backString + "</a></u></font></center>\n");
			html.append("<script language=\"JavaScript\"> window.alert('"
					+ errorPermissionString + "'); </script>\n");

			return html.toString();
		}

		if ("INSERT".equalsIgnoreCase(pType)) {
			results = new String[3];
			errorString = this.form_messageXML.getfieldTransl("add_error", false);
			processingString = this.form_messageXML.getfieldTransl("processing_insert", false);
		} else if ("DELETE".equalsIgnoreCase(pType)) {
			results = new String[2];
			errorString = this.form_messageXML.getfieldTransl("remove_error", false);
			processingString = this.form_messageXML.getfieldTransl("processing_delete", false);
		} else if ("UPDATE".equalsIgnoreCase(pType)) {
			results = new String[2];
			errorString = this.form_messageXML.getfieldTransl("save_error", false);
			processingString = this.form_messageXML.getfieldTransl("processing_update", false);
		} else if ("RUN".equalsIgnoreCase(pType)) {
			results = new String[2];
			errorString = this.form_messageXML.getfieldTransl("save_error", false);
			processingString = this.form_messageXML.getfieldTransl("processing_run", false);
		}

		if ("INSERT".equalsIgnoreCase(pType)) {
			results = myCallFunctionParam(pCallSQL, pParam, 3);
			this.callResultInt = results[0];
			resultID = results[1];
			resultMessage = results[2];
		} else if ("DELETE".equalsIgnoreCase(pType)
				|| "UPDATE".equalsIgnoreCase(pType)) {
			results = myCallFunctionParam(pCallSQL, pParam, 2);
			this.callResultInt = results[0];
			resultMessage = results[1];
		} else if ("RUN".equalsIgnoreCase(pType)) {
			results = myCallFunctionParam(pCallSQL, pParam, 3);
			this.callResultInt = results[0];
			resultID = results[1];
			resultMessage = results[2];
		}
		if ("0".equalsIgnoreCase(this.callResultInt)) {
			if (!isEmpty(resultMessage)) {
				resultMessage = processingString + "<br><br>" + resultMessage;
			} else {
				resultMessage = processingString;
			}
		}

		String showSQL = prepareSQLToLog(pCallSQL, pParam, true);
		
		if ("RUN".equalsIgnoreCase(pType)) {
			if ("0".equalsIgnoreCase(this.callResultInt)
					&& isEmpty(resultID)) {
				html.append(this.showCallResult(showSQL, this.callResultInt,
						resultMessage, pManyHyperLink, pBackHyperLink,
						errorString));
			} else {
				html.append(this.showCallResult(showSQL, this.callResultInt,
						resultMessage, pToHyperLink + resultID, pBackHyperLink,
						errorString));
			}
		} else {
			html.append(this.showCallResult(showSQL, this.callResultInt,
					resultMessage, pToHyperLink + resultID, pBackHyperLink,
					errorString));
		}
		//LOGGER.debug("html="+html.toString());

		return html.toString();
	}

	public String getCallResultParam(String pType, String pCallSQL,
			String[] pParam, String pToHyperLink, String pBackHyperLink,
			String pManyHyperLink) {
		return getCallResultGeneral("", "", pType, pCallSQL, pParam,
				pToHyperLink, pBackHyperLink, pManyHyperLink);
	}

	public String getCallResultParam(String pType, String pCallSQL,
			String[] pParam, String pToHyperLink, String pBackHyperLink) {
		return getCallResultGeneral("", "", pType, pCallSQL, pParam,
				pToHyperLink, pBackHyperLink, "");
	}

	public String getCallResultParam(String pType, String pCallSQL,
			ArrayList<String> pParam, String pToHyperLink, String pBackHyperLink) {
		String[] pParam2 = new String[pParam.size()];
		for(int counter=0; counter<=pParam.size()-1;counter++){
			pParam2[counter] = pParam.get(counter);
			//System.out.println("pParam.get("+counter+")="+pParam.get(counter));
		}
		return getCallResultGeneral("", "", pType, pCallSQL, pParam2,
				pToHyperLink, pBackHyperLink, "");
	}
	public String getCallResultCheckParam(String pMenu, String pTabName,
			String pType, String pCallSQL, String[] pParam,
			String pToHyperLink, String pBackHyperLink) {
		return getCallResultGeneral(pMenu, pTabName, pType, pCallSQL, pParam,
				pToHyperLink, pBackHyperLink, "");
	}

	public String showCallResult(String pCallSQL, String pResult,
			String pResultMessage, String pToHyperLink, String pBackHyperLink,
			String pErrorString) {

		StringBuilder html = new StringBuilder();
		if (!("0".equalsIgnoreCase(pResult))) {
			LOGGER.debug("showCallResult(): \n pCallSQL="+pCallSQL+
				"\n, pResult="+pResult+
				"\n, pResultMessage="+pResultMessage+
				"\n, pToHyperLink="+pToHyperLink+
				"\n, pBackHyperLink="+pBackHyperLink+
				"\n, pErrorString="+pErrorString);
		}
		//LOGGER.debug("bcBase.showCallResult(): pResult=" + pResult);

		String lBackHL = isEmpty(pBackHyperLink)?pToHyperLink:pBackHyperLink;
		
		if ("0".equalsIgnoreCase(pResult)) {
			//html.append("<html><head>\n");
			//html.append("<script type=\"text/javascript\" src=\"../js/ajax_form_submit.js\" > </script>\n");
			//html.append("<script type=\"text/javascript\" src=\"../js/frame_emulator.js\" > </script>\n");
			//html.append("</head><body>\n");
			html.append("<table " + getTableDetailParam() + ">");
			html.append("<tr><td>");
			html.append(pResultMessage);
			html.append("<input type=\"hidden\" id=\"operation_result\" value=\"OPERATION_COMPLETE_SUCCESSFUL\"><br><br>\n");
			//html.append(getSubmitButton(pToHyperLink));
			html.append("<span onclick=\"ajaxpage('" + pToHyperLink
					+ "','div_main');\">go</span>");
			html.append("<script language=\"JavaScript\">\n");
			html.append(" try {ajaxpage('" + pToHyperLink
					+ "','div_main'); } catch (err) {/*alert(err)*/;}\n\n" +
					" function onAjaxDone(){/*alert('onAjaxDone');*/}");
			html.append("</script>\n");
			html.append("</td></tr>");
			html.append("</table>");
			//html.append("</body></html>\n");
		} else {
			html.append("<html><head>\n");
			html.append("<script type=\"text/javascript\" src=\"../js/frame_emulator.js\" > </script>\n");
			html.append("</head><body>\n");
			html.append("<center><font class=\"font_title\">" + pErrorString
					+ "</font></center><br>");
			html.append("<form action=\""
					+ "../admin/warningupdate.jsp\" name=\"updateForm\" id=\"updateForm\" accept-charset=\"UTF-8\" method=\"POST\">\n");
			html.append("<input type=\"hidden\" id=\"operation_result\" value=\"OPERATION_COMPLETE_ERROR\">\n");
			html.append("<input type=\"hidden\" value=\""
					+ currentMenu.getIdMenuElement() + "\" name=\"id_menu\">");
			html.append("<input type=\"hidden\" value=\""
					+ currentMenu.getCurrentTabIdMenuElement()
					+ "\" name=\"id_tab\">");
			html.append("<input type=\"hidden\" value=\"" + lBackHL
					+ "\" name=\"bank_url\">");
			html.append("<input type=\"hidden\" value=\""
					+ loginUser.getValue("ID_USER") + "\" name=\"found_by\">");
			html.append("<input type=\"hidden\" value=\""
					+ loginUser.getValue("ID_USER")
					+ "\" name=\"id_menu_element\">");
			html.append("<input type=\"hidden\" value=\"yes\" name=\"process\">");
			html.append("<input type=\"hidden\" value=\"adderror\" name=\"action\">");
			html.append("<table " + getTableDetailParam() + "><tbody>\n");
			html.append("<tr>\n");
			html.append("<td valign=\"middle\" align=\"center\">\n");
			html.append("<textarea name=\"desc_warning\" cols=\"110\" rows=\"15\" class=\"inputfield\">");
			if (!isEmpty(pCallSQL)) {
				html.append(pResultMessage + "\n\n");
				html.append("callSQL =" + pCallSQL);
			} else {
				html.append(pResultMessage);
			}
			html.append("</textarea>");
			html.append("</td>\n");
			html.append("</tr>\n");
			html.append("<tr>\n");
			html.append("<td valign=\"middle\" align=\"center\">\n");
			html.append(getSubmitButtonAjax("/admin/warningsupdate.jsp",
					"button_save_warning", "updateForm"));
			html.append(getGoBackButton(lBackHL));
			html.append("</td>\n");
			html.append("</tr>\n");
			html.append("</tbody></table>\n");
			html.append("</body></html>\n");
		}

		return html.toString();
	}

	public String getCallResultErrorString(String pCallSQL,
			String pResultMessage, String pErrorString, String pBackString) {

		StringBuilder html = new StringBuilder();

		html.append(showCallSQL(pCallSQL));

		html.append("<center><b>" + pErrorString + ":</b></center><br>\n");
		html.append("<font color=\"red\">" + pResultMessage + "</font> <br>\n");
		html.append("<center><font color=\"blue\"><u><a href=\"#\" onclick=\"goBack();\">"
				+ pBackString + "?</a></u></font></center>\n");
		html.append("<script language=\"JavaScript\"> window.alert('"
				+ pErrorString + "'); </script>\n");

		return html.toString();
	}

	public String getJSPHeadHTML(String pPrint) {

		return getJSPHeadHTML(null, pPrint);
	}

	public String getJSPHeadHTML(String pTitle, String pPrint) {

		String lTitle = isEmpty(pTitle)?this.getHeaderName():pTitle;
		
		StringBuilder html = new StringBuilder();
		html.append(this.getMetaContent() + "\n");
		if ("N".equalsIgnoreCase(pPrint)) {
			html.append("<link rel=\"stylesheet\" type=\"text/css\" href=\"../../crm/CSS/topframe.css\">\n");
			html.append(this.getTableSorterCSS("[0,0],[2,1]") + "\n");
		} else {
			html.append("<link type=\"image/x-icon\" href=\"../../images/favicon.png\" rel=\"shortcut icon\"/>\n");
			html.append("<title>"
					+ this.commonXML.getfieldTransl("application_name", false) + " - " + lTitle
					+ "</title>\n");
			html.append("<link rel=\"stylesheet\" type=\"text/css\" href=\"../../crm/CSS/tablebottom.css\">\n");
		}

		return html.toString();
	}

	public String getServiceHeaderTable(String pTitle) {
		StringBuilder html = new StringBuilder();

		html.append("<table width=\"100%\" border=\"0\" style=\"border: 0px; background: url(../images/top.gif) repeat-x center top;\">\n");
		html.append("	<tr>\n");
		html.append("		<td align=\"left\" style=\"border: 0px; background: url(../images/top.gif) repeat-x center top;\">\n");
		html.append("			<i><b> <font color=\"#333333\">&nbsp;" + pTitle
				+ "</font></b></i></td>\n");
		html.append("		<td align=\"right\" style=\"border: 0px; background: url(../images/top.gif) repeat-x center top;\">\n");
		html.append("		</td>\n");
		html.append("	</tr>\n");
		html.append("</table>\n");

		return html.toString();
	}

	public String getServiceFindTable(String pExpr) {
		StringBuilder html = new StringBuilder();

		html.append("<form action=\"#\" method=\"post\" accept-charset=\"UTF-8\" onsubmit=\"checkExpr(); send_request(); return false;\" style=\"margin: 0px;\">\n");
		html.append("<table style=\"border: 0px;\">\n");
		html.append("	<tr>\n");
		html.append("		<td align=\"center\" style=\"border: 0px;\">\n");
		html.append("			<input type=\"text\" size=\"20\" name=\"expr\" id=\"expr\" value=\""
				+ pExpr + "\" style=\"background-color: #FEFFF1\">\n");
		html.append("			<input type=\"button\" value=\""
				+ buttonXML.getfieldTransl("search", false)
				+ "\" onclick=\"checkExpr(); send_request();\" style=\"background-color: white\">\n");
		html.append("			<input type=\"button\" value=\""
				+ buttonXML.getfieldTransl("clear", false)
				+ "\" onclick=\"expr.value=''; return false;\" style=\"background-color: white\">&nbsp;\n");
		html.append("			<img id=\"imgWait\" src=\"images/ajax-loader-circle.gif\" align=\"absmiddle\" style=\"visibility: hidden\">\n");
		html.append("		</td>\n");
		html.append("		<td align=\"right\" style=\"border: 0px; color: #003300; font-family: monospace; font-weight: bold;\" >\n");
		html.append(commonXML.getfieldTransl("PAGE", false)
				+ "\n");
		html.append("			<span name=\"pcounter\" id=\"pcounter\">1</span>\n");
		html.append("			<c:choose>\n");
		html.append("				<c:when test=\"${1 != 1}\">\n");
		html.append("					<a id=\"a_beg\" href=\"#\" onclick=\"goFirst(); return false;\" title=\"go First\" style=\"background-color: azure\"><img name=\"arr_beg\" id=\"arr_beg\" vspace=\"0\" hspace=\"0\" src=\""
				+ "../images/go/arr_beg.gif\" align=\"top\" style=\"border: 0px;\"></a>\n");
		html.append("					<a id=\"a_left\" href=\"#\" onclick=\"goPrev(); return false;\" title=\"go Previous\" style=\"background-color: azure\"><img name=\"arr_left\" id=\"arr_left\" vspace=\"0\" hspace=\"0\" src=\""
				+ "../images/go/arr_left.gif\" align=\"top\" style=\"border: 0px;\"></a>\n");
		html.append("				</c:when>\n");
		// html.append("				<c:otherwise>\n");
		// html.append("					<a id=\"a_beg\" title=\"go First\" style=\"background-color: azure\"><img name=\"arr_beg\" id=\"arr_beg\" vspace=\"0\" hspace=\"0\" src=\""
		// "../images/go/arr_beg2.gif\" align=\"top\" style=\"border: 0px;\"></a>\n");
		// html.append("					<a id=\"a_left\" title=\"go Previous\" style=\"background-color: azure\"><img name=\"arr_left\" id=\"arr_left\" vspace=\"0\" hspace=\"0\" src=\""
		// "../images/go/arr_left2.gif\" align=\"top\" style=\"border: 0px;\"></a>\n");
		// html.append("				</c:otherwise>\n");
		html.append("			</c:choose>\n");
		html.append("			<a id=\"a_next\" href=\"#\" onclick=\"goNext(); return false;\" title=\"go Next\" style=\"background-color: azure\"><img name=\"arr_right\" id=\"arr_right\" vspace=\"0\" hspace=\"0\" src=\""
				+ "../images/go/arr_right.gif\" align=\"top\" style=\"border: 0px;\"></a>\n");
		html.append("		</td>\n");
		html.append("	</tr>\n");
		html.append("</table>\n");
		html.append("</form>\n");

		return html.toString();
	}

	public String executeInsertFunction(String pCallSQL,
			ArrayList<String> pParam, String pToHyperLink, String pBackHyperLink) {
		return executeFunction("", "", "INSERT", pCallSQL, pParam,
				pToHyperLink, pBackHyperLink, "");
	}

	public String executeUpdateFunction(String pCallSQL,
			ArrayList<String> pParam, String pToHyperLink, String pBackHyperLink) {
		return executeFunction("", "", "UPDATE", pCallSQL, pParam,
				pToHyperLink, pBackHyperLink, "");
	}

	public String executeDeleteFunction(String pCallSQL,
			ArrayList<String> pParam, String pToHyperLink, String pBackHyperLink) {
		return executeFunction("", "", "DELETE", pCallSQL, pParam,
				pToHyperLink, pBackHyperLink, "");
	}

	public String executeFunction(String pType, String pCallSQL,
			ArrayList<String> pParam, String pToHyperLink, String pBackHyperLink) {
		return executeFunction("", "", pType, pCallSQL, pParam,
				pToHyperLink, pBackHyperLink, "");
	}

	public String executeFunction(String pMenu, String pTabName,
			String pType, String pFunction, ArrayList<String> pParam,
			String pToHyperLink, String pBackHyperLink, String pManyHyperLink) {

		StringBuilder html = new StringBuilder();
		/*LOGGER.debug("getCallResultGeneral(): \n pMenu="+pMenu+
				"\n, pTabName="+pTabName+
				"\n, pType="+pType+
				"\n, pFunction="+pFunction+
				"\n, pParam.size()="+pParam.size()+
				"\n, pToHyperLink="+pToHyperLink+
				"\n, pBackHyperLink="+pBackHyperLink+
				"\n, pManyHyperLink="+pManyHyperLink);*/
		String[] results = null;

		String resultMessage = "";
		String resultID = "";
		String errorString = "";
		String processingString = "";

		boolean hasEditPermission = false;

		if (!isEmpty(pMenu)) {
			hasEditPermission = hasEditMenuPermission(pMenu);
		} else if (!isEmpty(pTabName)) {
			hasEditPermission = hasEditTabsheetPermission(pTabName);
		} else {
			hasEditPermission = true;
		}
		html.append("<html><head>\n");
		html.append("<script type=\"text/javascript\" src=\"../js/ajax_form_submit.js\" > </script>\n");
		html.append("<script type=\"text/javascript\" src=\"../js/frame_emulator.js\" > </script>\n");
		html.append("<script type=\"text/javascript\" src=\"../crm/js/crm.js\" > </script>\n");
		html.append("</head><body>\n");
		
		//html.append("<table " + getTableMenuParam() + ">");
		//html.append("<tr>");
		//html.append(getPageHeader());
		//html.append("</tr>");
		//html.append("</table>");

		if (!hasEditPermission) {
			String errorPermissionString = this.commonXML.getfieldTransl("h_error_edit_permission", false);
			String backString = this.buttonXML.getfieldTransl("go_back", false);

			html.append("<font color=\"red\">" + errorPermissionString
					+ "</font> <br>\n");
			html.append("<center><font color=\"blue\"><u><a href=\"#\" onclick=\"goBack();\">"
					+ backString + "</a></u></font></center>\n");
			html.append("<script language=\"JavaScript\"> window.alert('"
					+ errorPermissionString + "'); </script>\n");

			return html.toString();
		}

		if ("INSERT".equalsIgnoreCase(pType)) {
			results = new String[3];
			errorString = this.form_messageXML.getfieldTransl("add_error", false);
			processingString = this.form_messageXML.getfieldTransl("processing_insert", false);
		} else if ("DELETE".equalsIgnoreCase(pType)) {
			results = new String[2];
			errorString = this.form_messageXML.getfieldTransl("remove_error", false);
			processingString = this.form_messageXML.getfieldTransl("processing_delete", false);
		} else if ("UPDATE".equalsIgnoreCase(pType)) {
			results = new String[2];
			errorString = this.form_messageXML.getfieldTransl("save_error", false);
			processingString = this.form_messageXML.getfieldTransl("processing_update", false);
		} else if ("RUN".equalsIgnoreCase(pType)) {
			results = new String[2];
			errorString = this.form_messageXML.getfieldTransl("save_error", false);
			processingString = this.form_messageXML.getfieldTransl("processing_run", false);
		}
		
		if ("INSERT".equalsIgnoreCase(pType)) {
			results = executeFunction(pFunction, pParam, results.length);
			this.callResultInt = results[0];
			resultID = results[1];
			resultMessage = results[2];
		} else if ("DELETE".equalsIgnoreCase(pType)
				|| "UPDATE".equalsIgnoreCase(pType)) {
			results = executeFunction(pFunction, pParam, results.length);
			this.callResultInt = results[0];
			resultMessage = results[1];
		} else if ("RUN".equalsIgnoreCase(pType)) {
			results = executeFunction(pFunction, pParam, results.length);
			this.callResultInt = results[0];
			resultID = results[1];
			resultMessage = results[2];
		}
		String showSQL = prepareSQLToLog2(getCallFunctionSQL(), pParam);
		
		if ("0".equalsIgnoreCase(this.callResultInt)) {
			if (!isEmpty(resultMessage)) {
				resultMessage = processingString + "<br><br>" + resultMessage;
			} else {
				resultMessage = processingString;
			}
		}
		
		if ("RUN".equalsIgnoreCase(pType)) {
			if ("0".equalsIgnoreCase(this.callResultInt) && isEmpty(resultID)) {
				html.append(this.showCallResult(showSQL, this.callResultInt,
						resultMessage, pManyHyperLink, pBackHyperLink,
						errorString));
			} else {
				html.append(this.showCallResult(showSQL, this.callResultInt,
						resultMessage, pToHyperLink + resultID, pBackHyperLink,
						errorString));
			}
		} else {
			html.append(this.showCallResult(showSQL, this.callResultInt,
					resultMessage, pToHyperLink + resultID, pBackHyperLink,
					errorString));
		}
		LOGGER.debug("pToHyperLink="+pToHyperLink + resultID);

		return html.toString();
	}

	public String makeValue(String pTitle, String pValue) {
		StringBuilder html = new StringBuilder();
		
		if (!isEmpty(pValue) && !"0".equalsIgnoreCase(pValue)) {
			html.append("<b><font color=\"brown\">"+pTitle+"</font></b>");
		} else {
			html.append(pTitle);
		}

		return html.toString();
	}

}