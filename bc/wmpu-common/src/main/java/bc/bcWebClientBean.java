package bc;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.security.SecureRandom;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import webpos.wpHeaders;

import java.util.*;

import bc.connection.Connector;
import bc.service.bcCardParamRecord;
import bc.service.bcCurrencyRecord;


// TODO !!! REFACTORING !!!
public class bcWebClientBean extends bcBase {
	private static final Logger LOGGER = Logger.getLogger(bcWebClientBean.class);

	public wpHeaders wpHeader = new wpHeaders(this.getDateFormat());

	public bcWebClientBean() {
	}
	
	private Connection wrCon = null;
	private String wrSessionId = null;

	public String getWRSessionId() {
		if (this.wrSessionId == null || "".equalsIgnoreCase(this.wrSessionId)) {
			this.setWRSessionId();
		}
		return this.wrSessionId;
	}

	public void setWRSessionId() {
		// if (this.wrSessionId==null || "".equalsIgnoreCase(this.wrSessionId))
		// {
		SecureRandom random = new SecureRandom();

		this.wrSessionId = new BigInteger(130, random).toString(32);
		LOGGER.debug("this.wrSessionId=" + this.wrSessionId);
		this.setSessionId(this.wrSessionId);
		// }
	}

	public void setWRSessionId(String pSID) {
		this.setSessionId(pSID);
	}

	public Connection getWRConnection() {
		try {
			if (wrCon == null || wrCon.isClosed()) {
				setWRConnection();
			}
		} catch (SQLException e) {
			LOGGER.debug("getWRConnection() SQLException: "
					+ e.toString());
		}
		return wrCon;
	}

	public boolean setWRConnection() {
		String wpUserName = param.getValue("anonymous_username");
		String wpPassWord = param.getValue("anonymous_password");
		LOGGER.debug("setWPConnection: wpUserName=" + wpUserName
				+ ", wpPassWord=" + wpPassWord);
		// this.setWRSessionId();
		try {
			if (wrCon == null || wrCon.isClosed()) {
				wrCon = Connector.getConnection(moduleName, wpUserName, wpPassWord,
						this.getWRSessionId(), this.getLanguage(), null, "-1");
			} else {
				wrCon = Connector.getConnection(this.getWRSessionId());
			}

		} catch (SQLException e) {
			LOGGER.debug("setWRConnection() SQLException: "
					+ e.toString());
		}
		if (wrCon == null) {
			return false;
		}
		return true;
	}

	
	public String loginCode = "";
	// private static Connection con = null;

	private String moduleName = "webpos";

	public boolean logIn(String pUsername, String pPassword, HttpServletRequest request) {
		return logIn(pUsername, pPassword, moduleName, request);
	}

	public boolean logIn(String pUsername, String pPassword, String pModuleName, HttpServletRequest request) {
		Connector.removeSessionId(sessionId);
		LOGGER.debug("logIn: pUsername=" + pUsername);
		LOGGER.debug("logIn: pPassword=" + pPassword);
		LOGGER.debug("logIn: pModuleName=" + pModuleName);
		LOGGER.debug("logIn: sessionId=" + sessionId);
		Connection con = null;
		String ip = getClientIpAddr(request);
		if ("".equalsIgnoreCase(sessionId) || pUsername == null
				|| "".equalsIgnoreCase(pUsername)) {
			con = Connector.getConnection(sessionId);
		} else {
			con = Connector.getConnection(pModuleName, pUsername, pPassword, sessionId, this.getLanguage(), ip, "-1");
		}
		if (con == null) {
			return false;
		}
		Connector.closeConnection(con);
		clearAllDictionaries();
		this.setIsLoged(true);
		this.setSessionLanguage();
		this.setSessionDateFormat();
		if (pModuleName == null || "".equalsIgnoreCase(pModuleName)
				|| "crm".equalsIgnoreCase(pModuleName)) {
			moduleName = "crm";
		} else {
			moduleName = pModuleName;
		}
		header.setLanguage(this.getLanguage());
		header.setIdClub(this.getCurrentClubID());
		loginUser.getCurrentUserFeature();
		//loginUserParam.getCurntUserParamFeature();
		this.getLookups();
		this.getWPCurrencies();
		return true;
	}

	public boolean logInCurrent(String pSessionId) {
		boolean l_return = true;
		if (sessionId == null || "".equalsIgnoreCase(sessionId)) {
			sessionId = pSessionId;
		}
		if (!checkUser()) {
			this.setIsLoged(false);
			l_return = false;
			this.clearLink();
		}
		LOGGER.debug("logInCurrent('" + pSessionId + "') = " + l_return);
		return l_return;
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

	public String getWPCurrencyName(String pCurrencyCode) {
		String return_value = "";
		if (pCurrencyCode == null || "".equalsIgnoreCase(pCurrencyCode)) {
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
		if (pCurrencyCode == null || "".equalsIgnoreCase(pCurrencyCode)) {
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
		if (pCurrencyCode == null || "".equalsIgnoreCase(pCurrencyCode)) {
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
				if (!(optionStyle == null || "".equalsIgnoreCase(optionStyle))) {
					styleText = " " + optionStyle;
				}
				if (pSize == null || "".equalsIgnoreCase(pSize)) {
					pOptionText = value;
					pOptionTitle = "";
				} else {
					if (!(value == null || "".equalsIgnoreCase(value))) {
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
		if (!(request.getQueryString() == null
				|| "".equalsIgnoreCase(request.getQueryString()) || "null"
					.equalsIgnoreCase(request.getQueryString()))) {
			myHyperLink = myHyperLink + request.getQueryString().toString();
		}
		this.addLink(myHyperLink);
		LOGGER.debug(result.toString());
		return result.toString();
	}
	
	private ArrayList<bcCardParamRecord> activeUserCard = new ArrayList<bcCardParamRecord>();
	private ArrayList<bcCardParamRecord> blockUserCard = new ArrayList<bcCardParamRecord>();
	private ArrayList<bcCardParamRecord> allUserCard = new ArrayList<bcCardParamRecord>();
	private bcCardParamRecord currentCard = new bcCardParamRecord();

	public void setCurrentCard(String pCardSerialNumber, String pIdIssuer,
			String pIdPaymentSystem) {
		for (int i = 0; i <= allUserCard.size() - 1; i++) {
			if (allUserCard.get(i).get_card_serial_number()
					.equalsIgnoreCase(pCardSerialNumber)
					&& allUserCard.get(i).get_id_issuer()
							.equalsIgnoreCase(pIdIssuer)
					&& allUserCard.get(i).get_id_payment_system()
							.equalsIgnoreCase(pIdPaymentSystem)) {
				currentCard = allUserCard.get(i);
			}
		}
	}

	public String getCurrentCardSerialNumber() {
		return currentCard.get_card_serial_number();
	}

	public String getCurrentCardIdIssuer() {
		return currentCard.get_id_issuer();
	}

	public String getCurrentCardIdPaymentsystem() {
		return currentCard.get_id_payment_system();
	}

	public String getCurrentCardCDCard1() {
		return currentCard.get_cd_card1();
	}

	public void setCurrentUserClubCards(String pIdNatPrs) {
		String generalDB = getGeneralDBScheme();
		Connection con = null;
		PreparedStatement st = null;

		int cardCnt = 0;

		try {
			activeUserCard.clear();
			allUserCard.clear();
			blockUserCard.clear();

			LOGGER.debug("setCurrentUserClubCards() BEGIN, pIdNatPrs="
					+ pIdNatPrs);
			this.loginUser.getCurrentUserFeature();
			String sql = " SELECT * " + "   FROM " + generalDB
					+ ".vw$card_all " + "  WHERE id_nat_prs = ? "
					+ "  ORDER BY cd_card1";

			con = Connector.getConnection(sessionId);

			st = con.prepareStatement(sql);
			st.setInt(1, Integer.parseInt(pIdNatPrs));
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				cardCnt = cardCnt + 1;
				bcCardParamRecord oneCard = new bcCardParamRecord();
				oneCard.set_card_serial_number(rs
						.getString("card_serial_number"));
				oneCard.set_nt_icc(rs.getString("nt_icc"));
				oneCard.set_cd_card1(rs.getString("cd_card1"));
				oneCard.set_cd_card2(rs.getString("cd_card2"));
				oneCard.set_id_club(rs.getString("id_club"));
				oneCard.set_id_issuer(rs.getString("id_issuer"));
				oneCard.set_id_payment_system(rs.getString("id_payment_system"));
				oneCard.set_name_payment_system(rs
						.getString("name_payment_system"));
				oneCard.set_cd_currency(rs.getString("cd_currency"));
				oneCard.set_name_currency(rs.getString("name_currency"));
				oneCard.set_sname_currency(rs.getString("sname_currency"));
				oneCard.set_id_nat_prs(rs.getString("id_nat_prs"));
				oneCard.set_expiry_date(rs.getString("expiry_date_frmt"));
				oneCard.set_id_card_status(rs.getString("id_card_status"));
				oneCard.set_name_card_status(rs.getString("name_card_status"));
				oneCard.set_id_card_state(rs.getString("id_card_state"));
				oneCard.set_name_card_state(rs.getString("name_card_state"));
				oneCard.set_id_card_type(rs.getString("id_card_type"));
				oneCard.set_name_card_type(rs.getString("name_card_type"));
				oneCard.set_id_bon_category(rs.getString("id_bon_category"));
				oneCard.set_name_bon_category(rs.getString("name_bon_category"));
				oneCard.set_bonus_transfer_term(rs
						.getString("bonus_transfer_term"));
				oneCard.set_bonus_calc_term(rs.getString("bonus_calc_term"));
				oneCard.set_next_date_calc(rs.getString("next_date_calc_frmt"));
				oneCard.set_id_disc_category(rs.getString("id_disc_category"));
				oneCard.set_name_disc_category(rs
						.getString("name_disc_category"));
				oneCard.set_club_bon(rs.getString("club_bon_frmt"));
				oneCard.set_club_disc(rs.getString("club_disc_frmt"));
				oneCard.set_bal_acc(rs.getString("bal_acc_frmt"));
				oneCard.set_bal_cur(rs.getString("bal_cur_frmt"));
				oneCard.set_bal_exist(rs.getString("bal_exist_frmt"));
				oneCard.set_bal_bon_per(rs.getString("bal_bon_per_frmt"));
				oneCard.set_bal_disc_per(rs.getString("bal_disc_per_frmt"));
				oneCard.set_date_acc(rs.getString("date_acc_frmt"));
				oneCard.set_date_mov(rs.getString("date_mov_frmt"));
				oneCard.set_date_calc(rs.getString("date_calc_frmt"));
				oneCard.set_date_onl(rs.getString("date_onl_frmt"));
				oneCard.set_date_onl_next(rs.getString("date_onl_next_frmt"));
				oneCard.set_bal_full(rs.getString("bal_full_frmt"));
				oneCard.set_disc_full(rs.getString("disc_full_frmt"));
				oneCard.set_date_last_trans(rs
						.getString("date_last_trans_frmt"));
				oneCard.set_date_card_sale(rs.getString("date_card_sale_frmt"));
				oneCard.set_pay_bon_full(rs.getString("pay_bon_full_frmt"));
				oneCard.set_id_card_kind(rs.getString("id_card_kind"));

				allUserCard.add(oneCard);
				if ("0".equalsIgnoreCase(rs.getString("id_card_state"))
						|| "1".equalsIgnoreCase(rs.getString("id_card_state"))) {
					activeUserCard.add(oneCard);
				} else {
					blockUserCard.add(oneCard);
				}
				if (currentCard.get_card_serial_number() == null
						|| "".equalsIgnoreCase(currentCard
								.get_card_serial_number())) {
					currentCard = oneCard;
					LOGGER.debug("current card="
							+ currentCard.get_card_serial_number());
				}
			}
			rs.close();
			st.close();
		} catch (SQLException e) {
			LOGGER.debug("setCurrentUserClubCards SQLException: "
					+ e.toString());
		} catch (Exception e) {
			LOGGER.debug("setCurrentUserClubCards Exception: "
					+ e.toString());
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
		LOGGER.debug("setCurrentUserClubCards() END, cardCnt=" + cardCnt);
	}

	
	public String getWebClientCardOptions(String cd_card1, String pCardState,
			boolean isNull) {
		StringBuilder html = new StringBuilder();

		ArrayList<bcCardParamRecord> needCard = new ArrayList<bcCardParamRecord>();

		if ("ACTIVE".equalsIgnoreCase(pCardState)) {
			needCard = activeUserCard;
		} else if ("BLOCK".equalsIgnoreCase(pCardState)) {
			needCard = blockUserCard;
		} else {
			needCard = allUserCard;
		}

		if (isNull) {
			html.append("<option value=\"\" title=\"\"></option>");
		}

		for (int i = 0; i <= needCard.size() - 1; i++) {
			String l_cd_card1 = needCard.get(i).get_cd_card1();
			String tagSelected = "";
			if (cd_card1.equalsIgnoreCase(l_cd_card1)) {
				tagSelected = " SELECTED ";
			}
			html.append("<option " + tagSelected + " value=\"" + l_cd_card1
					+ "\" title=\"" + l_cd_card1 + "\">" + l_cd_card1
					+ "</option>");
		}

		return html.toString();
	}

	public String getWebClientDealerOptions(String id_dealer, boolean isNull) {
		return getSelectBodyFromQuery2(
				" SELECT id_jur_prs id_dealer, sname_jur_prs sname_dealer "
						+ "   FROM " + getGeneralDBScheme() + ".vw$dealer_all "
						+ "  ORDER BY sname_jur_prs", id_dealer, isNull, false);
	}

	public String getWebClientCardParamList(String cd_card1, String pCardState,
			String pCardPageNumber) {
		StringBuilder html_active = new StringBuilder();
		StringBuilder html_noactive = new StringBuilder();
		String ajaxPage = "";

		boolean isAddActiveTitle = false;
		boolean isAddNoactiveTitle = false;

		ArrayList<bcCardParamRecord> needCard = new ArrayList<bcCardParamRecord>();

		if ("ACTIVE".equalsIgnoreCase(pCardState)) {
			needCard = activeUserCard;
		} else if ("BLOCK".equalsIgnoreCase(pCardState)) {
			needCard = blockUserCard;
		} else {
			needCard = allUserCard;
		}

		if (cd_card1 == null || "".equalsIgnoreCase(cd_card1)) {
			cd_card1 = currentCard.get_cd_card1();
		}

		for (int i = 0; i <= needCard.size() - 1; i++) {
			bcCardParamRecord card = needCard.get(i);
			StringBuilder html_card = new StringBuilder();
			ajaxPage = "ajaxpage('webclient/card/card_param.jsp?cardid="
					+ card.get_card_serial_number() + "&iss="
					+ card.get_id_issuer() + "&paysys="
					+ card.get_id_payment_system() + "&cd_card1="
					+ card.get_cd_card1() + "&cardpage=" + pCardPageNumber
					+ "', 'div_main')";
			if (cd_card1.equalsIgnoreCase(card.get_cd_card1())) {
				html_card.append("<div id=\"div_active_card\" onclick=\""
						+ ajaxPage + "\">");
				html_card.append("<span class=\"card_kind\">"
						+ card.get_name_payment_system()
						+ "</span><span class=\"card_status\">"
						+ card.get_name_card_status() + "</span>");
				html_card.append("<span class=\"card_code\">"
						+ card.get_cd_card1() + "</span>");
				html_card
						.append("<span class=\"card_category_title\">Категория<span class=\"card_category\">"
								+ card.get_name_bon_category()
								+ "</span></span>");
				String lCardState = card.get_name_card_state();
				if (lCardState.length() > 10) {
					lCardState = lCardState.substring(0, 10) + "...";
				}
				html_card
						.append("<span class=\"card_state_title\">Состояние<span class=\"card_state\">"
								+ lCardState + "</span></span>");
				html_card
						.append("<span class=\"card_bal_acc_title\">Накопленные, но еще недоступные бонусы<span class=\"card_bal_acc\">"
								+ card.get_bal_acc()
								+ " "
								+ card.get_sname_currency() + "</span></span>");
				html_card
						.append("<span class=\"card_bal_cur_title\">Доступные бонусы<span class=\"card_bal_cur\">"
								+ card.get_bal_cur()
								+ " "
								+ card.get_sname_currency() + "</span></span>");
				html_card
						.append("<span class=\"card_used_time_tile\">Карта активна с<span class=\"card_used_time\">"
								+ getValue2(card.get_date_card_sale())
								+ "</span></span>");
				html_card.append("</div>");
			} else {
				html_card.append("<div id=\"div_card\" onclick=\"" + ajaxPage
						+ "\">");
				html_card.append("<span class=\"card_kind\">"
						+ card.get_name_payment_system()
						+ "</span><span class=\"card_status\">"
						+ card.get_name_card_status() + "</span>");
				html_card.append("<span class=\"card_code\">"
						+ card.get_cd_card1() + "</span>");
				html_card.append("<span class=\"card_category\">"
						+ card.get_name_bon_category() + "</span>");
				html_card.append("<span class=\"card_state\">"
						+ card.get_name_card_state() + "</span>");
				html_card.append("</div>");
			}
			if ("0".equalsIgnoreCase(card.get_id_card_state())
					|| "1".equalsIgnoreCase(card.get_id_card_state())) {
				if (!isAddActiveTitle) {
					html_active
							.append("<div class=\"div_card_group\">Активные</div>");
					isAddActiveTitle = true;
				}
				html_active.append(html_card.toString());
			} else {
				if (!isAddNoactiveTitle) {
					html_noactive
							.append("<div class=\"div_card_group div_noactive_card_group\">Неактивные</div>");
					isAddNoactiveTitle = true;
				}
				html_noactive.append(html_card.toString());
			}
		}
		html_active.append(html_noactive.toString());

		return html_active.toString();
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

	private String callResultInt = "0";

	public String getCallResultInt() {
		return callResultInt;
	}


	public String getCallResultGeneral(String pMenu, String pTabName,
			String pType, String pCallSQL, String[] pParam,
			String pToHyperLink, String pBackHyperLink, String pManyHyperLink) {

		StringBuilder html = new StringBuilder();
		LOGGER.debug("bcBase.getCallResult()");
		String[] results = null;

		String resultMessage = "";
		String resultID = "";

		String errorString = "";

		boolean hasEditPermission = false;

		if (!(pMenu == null || "".equalsIgnoreCase(pMenu))) {
			hasEditPermission = hasEditMenuPermission(pMenu);
		} else if (!(pTabName == null || "".equalsIgnoreCase(pTabName))) {
			hasEditPermission = hasEditTabsheetPermission(pTabName);
		} else {
			hasEditPermission = true;
		}

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
			html.append(this.form_messageXML.getfieldTransl("processing_insert", false) + "\n\n");
		} else if ("DELETE".equalsIgnoreCase(pType)) {
			results = new String[2];
			errorString = this.form_messageXML.getfieldTransl("remove_error", false);
			html.append(this.form_messageXML.getfieldTransl("processing_delete", false) + "\n\n");
		} else if ("UPDATE".equalsIgnoreCase(pType)) {
			results = new String[2];
			errorString = this.form_messageXML.getfieldTransl("save_error", false);
			html.append(this.form_messageXML.getfieldTransl("processing_update", false) + "\n\n");
		} else if ("RUN".equalsIgnoreCase(pType)) {
			results = new String[2];
			errorString = this.form_messageXML.getfieldTransl("save_error", false);
			html.append(this.form_messageXML.getfieldTransl("processing_run", false) + "\n\n");
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

		String showSQL = prepareSQLToLog(pCallSQL, pParam, true);
		
		if ("RUN".equalsIgnoreCase(pType)) {
			if ("0".equalsIgnoreCase(this.callResultInt)
					&& (resultID == null || "".equalsIgnoreCase(resultID))) {
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

		return html.toString();
	}

	public String showCallResult(String pCallSQL, String pResult,
			String pResultMessage, String pToHyperLink, String pBackHyperLink,
			String pErrorString) {

		StringBuilder html = new StringBuilder();
		LOGGER.debug("bcBase.showCallResult(): pResult=" + pResult);

		String lBackHL = "";
		if (pBackHyperLink == null || "".equalsIgnoreCase(pBackHyperLink)) {
			lBackHL = pToHyperLink;
		} else {
			lBackHL = pBackHyperLink;
		}

		if ("0".equalsIgnoreCase(pResult)) {
			html.append("<html><head>\n");
			html.append("</head><body>\n");
			html.append("<input type=\"hidden\" id=\"operation_result\" value=\"OPERATION_COMPLETE_SUCCESSFUL\">\n");
			html.append("<script language=\"JavaScript\">\n");
			html.append("ajaxpage('" + pToHyperLink
					+ "','div_main');\n");
			html.append("</script>\n");
			html.append("</body></html>\n");
		} else {
			html.append("<html><head>\n");
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
			if (!(pCallSQL == null || "".equalsIgnoreCase(pCallSQL))) {
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
			html.append(getSubmitButton(lBackHL, "cancel"));
			html.append("</td>\n");
			html.append("</tr>\n");
			html.append("</tbody></table>\n");
			html.append("</body></html>\n");
		}

		return html.toString();
	}

}