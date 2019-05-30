package bc;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URLDecoder;
import java.security.SecureRandom;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import BonCard.DataBase.UtilityConnector;
import bc.connection.Connector;
import bc.headers.bcHeaders;
import bc.objects.bcDictionary;
import bc.objects.bcParamXML;
import bc.objects.bcUserObject;
import bc.objects.bcXML;
import bc.service.bcCurrencyRecord;
import bc.service.bcDictionaryRecord;
import bc.service.bcFeautureParam;
import bc.service.bcLookupRecord;
import bc.service.menuElement;
import bc.service.myLinks;


// TODO !!! REFACTORING !!!
public class bcBase {
	private static final Logger LOGGER = Logger.getLogger(bcBase.class);

	/**
	 * Thread parameters from HttpSession
	 */
	public static enum SESSION_PARAMETERS{
		/** id of session */
		SESSION_ID,
		/** language of the person */
		LANG,
		/** format of Date */
		DATE_FORMAT,
		/** format of Date */
		GENERAL_DB_SCHEME;
		
		private ThreadLocal<String> value=new ThreadLocal<String>();
		
		public String getValue(){
			return value.get();
		}
		public void setValue(String newValue){
			this.value.set(newValue);
		}
		
	}
	
	@Override
	public void finalize() {
		for(SESSION_PARAMETERS eachValue:SESSION_PARAMETERS.values()){
			eachValue.setValue(null);
		}
	}


	
	public bcBase() {
	}
	

	public String C_SUCCESS_RESULT              = "0";
	public String C_ERROR_RESULT              	= "1";
	public String C_SQL_EXCEPTION 				= "20999";
	
	public String C_READ_MENU_PERMISSION      	= "1";
	public String C_WRITE_MENU_PERMISSION      	= "2";
	public String C_EXECUTE_MENU_PERMISSION    	= "9";
	
	public String prepareToHTML (String pValue) {
		String result = "";
		result = pValue.replace("&", "&amp;")
					   .replace("\"", "&quot;")
					   .replace("<<", "&laquo;")
					   .replace(">>", "&raquo;")
					   .replace("<", "&lt;")
					   .replace(">", "&gt;");
		return result;
	}
	
	public String prepareFromHTML (String pValue) {
		String result = "";
		result = pValue.replace("&amp;", "&")
					   .replace("&quot;", "\"")
					   .replace("&laquo;", "<<")
					   .replace("&raquo;", ">>")
					   .replace("&lt;", "<")
					   .replace("&gt;", ">")
					   .replace("&nbsp;", " ");
		return result;
	}

	public String sessionId = "";

	public bcUserObject loginUser = new bcUserObject();
	//public bcUserParametersObject loginUserParam = new bcUserParametersObject();
	
	public boolean isEmpty(String pValue) {
		return StringUtils.isEmpty(pValue);
	}
	
	public boolean isEmptyAmount(String pValue) {
		boolean result = true;
		//System.out.println("pValue="+pValue);
		result = StringUtils.isEmpty(pValue);
		if (result) {
			return result;
		}
		//System.out.println("result (1)="+result);
		try {
			Double amount = Double.parseDouble(pValue.replace(",", "."));
			if (amount > 0) {
				result = false;
			} else {
				result = true;
			}
			//System.out.println("result (2)="+result);
		} catch (Exception e) {
			result = true;
			LOGGER.error("isEmptyAmount() Exception: " + e.toString());
		}
		return result;
	}

	public String getDirectorySystemParamValue(String cd_param) {
		String result = getOneValueByStringId2("SELECT value_param FROM "
				+ getGeneralDBScheme() + ".v_system_param WHERE cd_param = ?",
				cd_param);
		if (isEmpty(result)) {
			result = "files/";
		} else {
			String lastSymbol = result.substring(result.length() - 1,
					result.length());
			if (!("\\".equalsIgnoreCase(lastSymbol) || "/"
					.equalsIgnoreCase(lastSymbol))) {
				result = result + "/";
			}
		}
		return result;
	}
	
	public String getLoginUserId() {
		return loginUser.getValue("ID_USER");
	}
	
	public String getLoginUserName() {
		return loginUser.getValue("NAME_USER");
	}
	
	public String getLoginUserNatPrsFIO() {
		return loginUser.getValue("FIO_NAT_PRS");
	}
	
	public String getLoginUserNatPrsFIOInitial() {
		return loginUser.getValue("FIO_NAT_PRS_INITIAL");
	}
	
	public String getLoginUserServicePlaceId() {
		return loginUser.getValue("ID_SERVICE_PLACE_WORK");
	}
	
	public String getLoginUserServicePlaceName() {
		return loginUser.getValue("SNAME_SERVICE_PLACE_WORK");
	}
	
	public String getLoginUserServicePlaceAdr() {
		return loginUser.getValue("ADR_JUR_PRS_SHORT");
	}
	
	public String getLoginUserHideCardNumber() {
		return loginUser.getValue("CD_CARD1_HIDE");
	}
	
	public String getLoginUserTerm() {
		return loginUser.getValue("ID_TERM");
	}
	
	public String getLoginUserIdTermUser() {
		return loginUser.getValue("ID_TERM_USER");
	}
	
	public String getLoginUserAccessType() {
		return loginUser.getValue("CD_USER_ACCESS_TYPE");
	}

	public String getUserName(String id_user) {
		return getOneValueByIntId2("SELECT name_user FROM "
				+ getGeneralDBScheme() + ".vc_users_all WHERE id_user = ?",
				id_user);
	}

	public String getUserNatPrsName(String id_user) {
		return getOneValueByIntId2("SELECT CASE WHEN fio_nat_prs IS NULL THEN name_user ELSE fio_nat_prs||' ('||name_user||')' END name_user FROM "
				+ getGeneralDBScheme() + ".vc_users_all WHERE id_user = ?",
				id_user);
	}

	public String getUserNatPrsName2(String id_user) {
		return getOneValueByIntId2("SELECT CASE WHEN fio_nat_prs IS NULL THEN name_user ELSE name_user||' ('||fio_nat_prs||')' END name_user FROM "
				+ getGeneralDBScheme() + ".vc_users_all WHERE id_user = ?",
				id_user);
	}

	public void setSessionId(String pSessionId) {
		if(isEmpty(bcBase.SESSION_PARAMETERS.SESSION_ID.getValue())){
			bcBase.SESSION_PARAMETERS.SESSION_ID.setValue(pSessionId);
		}
		this.sessionId = pSessionId;
		header.setSessionId(this.sessionId);
		loginUser.setSessionId(this.sessionId);
		//loginUserParam.setSessionId(this.sessionId);
		currentMenu.setSessionId(this.sessionId);
	}

	public void setSessionId() {
		SecureRandom random = new SecureRandom();

		this.sessionId = new BigInteger(130, random).toString(32);

		LOGGER.debug("this.sessionId=" + this.sessionId);
		header.setSessionId(this.sessionId);
		loginUser.setSessionId(this.sessionId);
		//loginUserParam.setSessionId(this.sessionId);
		currentMenu.setSessionId(this.sessionId);
	}

	public String getSessionId() {
		return this.sessionId;
	}

	public static String getClientIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
	
	private Integer errorCode;
	private String errorMessage;
	
	public Integer getErrorCode() {
		return errorCode;
	}
	
	public void setErrorCode(Integer pErrorCode) {
		errorCode = pErrorCode;
	}
	
	public String getErrorMessage() {
		return errorMessage;
	}
	
	public void setErrorMessage(String pErrorMessage) {
		errorMessage = pErrorMessage;
	}

	public String getSysDate() {
		String dateFormat = loginUser.getParameterValue("DATEFORMAT");

		SimpleDateFormat sdf_short = new SimpleDateFormat(dateFormat
				.replaceAll("R", "y").replaceAll("Y", "y").replaceAll("m", "M")
				.replaceAll("D", "d"));

		return sdf_short.format(new Date());
	}

	public String getSysDateTime() {
		String dateFormat = loginUser.getParameterValue("DATEFORMAT") + " H:m";

		SimpleDateFormat sdf_short = new SimpleDateFormat(dateFormat
				.replaceAll("R", "y").replaceAll("Y", "y").replaceAll("m", "M")
				.replaceAll("D", "d"));

		return sdf_short.format(new Date());
	}

	public String getObligatorySymbol(String pText) {
		return "<span class=\"obligatory_text\" title=\"Обязательно для заполнения\">"
				+ pText + "&nbsp;*</span>";
	}

	/*public String getLoginUserIDNatPrs() {
		String idNatPrs = "1";
		return idNatPrs;
	}*/

	public String getCreationDateFieldName() {
		return "CREATION_DATE_DHMF";
	}

	public String getLastUpdateDateFieldName() {
		return "LAST_UPDATE_DATE_DHMF";
	}

	public String getCreationAndMoficationRecordFields(
			String pGeneralColSpan,	String pColCount, 
			String pEntryId, String pEntryIdColSpan,
			String pCreationDate, String pCreationDateColSpan, 
			String pCreatedBy, String pCreatedByColSpan, 
			String pLastUpdateDate,	String pLastUpdateDateColSpan, 
			String pLastUpdateBy, String pLastUpdateByColSpan) {
		StringBuilder html = new StringBuilder();

		String lGeneralCS = " colspan=4";
		String lEntryCS = "";
		String lCreationDateCS = "";
		String lCreatedByCS = "";
		String lLastUpdateDateCS = "";
		String lLastUpdateByCS = "";

		if (!isEmpty(pGeneralColSpan)) {
			lGeneralCS = " colspan=" + pGeneralColSpan;
		}
		if (!isEmpty(pEntryIdColSpan)) {
			lEntryCS = " colspan=" + pEntryIdColSpan;
		}
		if (!isEmpty(pCreationDateColSpan)) {
			lCreationDateCS = " colspan=" + pCreationDateColSpan;
		}
		if (!isEmpty(pCreatedByColSpan)) {
			lCreatedByCS = " colspan=" + pCreatedByColSpan;
		}
		if (!isEmpty(pLastUpdateDateColSpan)) {
			lLastUpdateDateCS = " colspan=" + pLastUpdateDateColSpan;
		}
		if (!isEmpty(pLastUpdateByColSpan)) {
			lLastUpdateByCS = " colspan=" + pLastUpdateByColSpan;
		}

		if ("1".equalsIgnoreCase(pColCount)) {
			html.append("<tr>");
			html.append("<td colspan=2><b>"
					+ this.commonXML.getfieldTransl("h_record_param", false)
					+ "&nbsp;<span id=\"change_button\" onclick=\"show_change_row1();\">>></span></b></td>");
			html.append("</tr>");
			if (!isEmpty(pEntryId)) {
				html.append("<tr id=\"id_param_row1\" style=\"display:none\">");
				html.append("<td class=\"gray_background\">"
						+ this.commonXML.getfieldTransl("entry_id", false)
						+ "</td> <td "
						+ lEntryCS
						+ " class=\"gray_background\"><input type=\"text\" name=\"entry_id\" size=\"20\" value=\""
						+ pEntryId
						+ "\" readonly class=\"inputfield-ro\"> </td>");
				html.append("</tr>");
			}
			html.append("<tr id=\"change_param_row1\" style=\"display:none\">");
			if (!isEmpty(pCreationDate)) {
				html.append("<td class=\"gray_background\">"
					+ this.commonXML.getfieldTransl("creation_date", false)
					+ "</td> <td "
					+ lCreationDateCS
					+ " class=\"gray_background\"><input type=\"text\" name=\"creation_date\" size=\"20\" value=\""
					+ pCreationDate
					+ "\" readonly class=\"inputfield-ro\"> </td>");
				html.append("</tr>");
				html.append("<tr id=\"change_param_row2\" style=\"display:none\">");
				html.append("<td class=\"gray_background\">"
					+ this.commonXML.getfieldTransl("created_by", false)
					+ "</td> <td "
					+ lCreatedByCS
					+ " class=\"gray_background\"><input type=\"text\" name=\"created_by\" size=\"20\" value=\""
					+ this.getSystemUserName(pCreatedBy)
					+ "\" readonly class=\"inputfield-ro\"> </td>");
			}
			html.append("</tr>");
			html.append("<tr id=\"update_param_row1\" style=\"display:none\">");
			if (!isEmpty(pLastUpdateDate)) {
				html.append("<td class=\"gray_background\">"
					+ this.commonXML.getfieldTransl("last_update_date", false)
					+ "</td> <td "
					+ lLastUpdateDateCS
					+ " class=\"gray_background\"><input type=\"text\" name=\"last_update_date\" size=\"20\" value=\""
					+ pLastUpdateDate
					+ "\" readonly class=\"inputfield-ro\"> </td>");
				html.append("</tr>");
				html.append("<tr id=\"update_param_row2\" style=\"display:none\">");
				html.append("<td class=\"gray_background\">"
					+ this.commonXML.getfieldTransl("last_update_by", false)
					+ "</td> <td "
					+ lLastUpdateByCS
					+ " class=\"gray_background\"><input type=\"text\" name=\"last_update_by\" size=\"20\" value=\""
					+ this.getSystemUserName(pLastUpdateBy)
					+ "\" readonly class=\"inputfield-ro\"> </td>");
			}
			html.append("</tr>");
		} else {
			html.append("<tr>");
			html.append("<td "
					+ lGeneralCS
					+ "><b>"
					+ this.commonXML.getfieldTransl("h_record_param", false)
					+ "&nbsp;<span id=\"change_button\" onclick=\"show_change_row2();\">>></span></b></td>");
			html.append("</tr>");
			if (!isEmpty(pEntryId)) {
				html.append("<tr id=\"id_param_row1\" style=\"display:none\">");
				html.append("<td class=\"gray_background\">"
						+ this.commonXML.getfieldTransl("entry_id", false)
						+ "</td> <td "
						+ lEntryCS
						+ " class=\"gray_background\"><input type=\"text\" name=\"entry_id\" size=\"20\" value=\""
						+ pEntryId
						+ "\" readonly class=\"inputfield-ro\"> </td>");
				html.append("<td class=\"gray_background\" colspan=\"2\">&nbsp;</td>");
				html.append("</tr>");
			}
			html.append("<tr id=\"change_param_row\" style=\"display:none\">");
			if (!isEmpty(pCreationDate)) {
				html.append("<td class=\"gray_background\">"
					+ this.commonXML.getfieldTransl("creation_date", false)
					+ "</td> <td "
					+ lCreationDateCS
					+ " class=\"gray_background\"><input type=\"text\" name=\"creation_date\" size=\"20\" value=\""
					+ pCreationDate
					+ "\" readonly class=\"inputfield-ro\"> </td>");
			}
			if (!isEmpty(pLastUpdateDate)) {
				html.append("<td class=\"gray_background\">"
					+ this.commonXML.getfieldTransl("last_update_date", false)
					+ "</td> <td "
					+ lLastUpdateDateCS
					+ " class=\"gray_background\"><input type=\"text\" name=\"last_update_date\" size=\"20\" value=\""
					+ pLastUpdateDate
					+ "\" readonly class=\"inputfield-ro\"> </td>");
			}
			html.append("</tr>");
			html.append("<tr id=\"update_param_row\" style=\"display:none\">");

			if (!isEmpty(pCreationDate)) {
				html.append("<td class=\"gray_background\">"
					+ this.commonXML.getfieldTransl("created_by", false)
					+ "</td> <td "
					+ lCreatedByCS
					+ " class=\"gray_background\"><input type=\"text\" name=\"created_by\" size=\"20\" value=\""
					+ this.getSystemUserName(pCreatedBy)
					+ "\" readonly class=\"inputfield-ro\"> </td>");
			}
			if (!isEmpty(pLastUpdateDate)) {
				html.append("<td class=\"gray_background\">"
					+ this.commonXML.getfieldTransl("last_update_by", false)
					+ "</td> <td "
					+ lLastUpdateByCS
					+ " class=\"gray_background\"><input type=\"text\" name=\"last_update_by\" size=\"20\" value=\""
					+ this.getSystemUserName(pLastUpdateBy)
					+ "\" readonly class=\"inputfield-ro\"> </td>");
			}
			html.append("</tr>");
		}

		return html.toString();
	}

	public String getCreationAndMoficationRecordFields(String pGeneralColSpan,
			String pCreationDate, String pCreationDateColSpan,
			String pCreatedBy, String pCreatedByColSpan,
			String pLastUpdateDate, String pLastUpdateDateColSpan,
			String pLastUpdateBy, String pLastUpdateByColSpan) {
		return getCreationAndMoficationRecordFields(pGeneralColSpan, "2",
				"", "", 
				pCreationDate, pCreationDateColSpan, pCreatedBy,
				pCreatedByColSpan, pLastUpdateDate, pLastUpdateDateColSpan,
				pLastUpdateBy, pLastUpdateByColSpan);
	}

	public String getIdCreationAndMoficationRecordFields(String pId, String pCreationDate,
			String pCreatedBy, String pLastUpdateDate, String pLastUpdateBy) {
		return getCreationAndMoficationRecordFields("", "2", pId, "", pCreationDate, "",
				pCreatedBy, "", pLastUpdateDate, "", pLastUpdateBy, "");
	}

	public String getCreationAndMoficationRecordFields(String pCreationDate,
			String pCreatedBy, String pLastUpdateDate, String pLastUpdateBy) {
		return getCreationAndMoficationRecordFields("", pCreationDate, "",
				pCreatedBy, "", pLastUpdateDate, "", pLastUpdateBy, "");
	}

	public String getIdCreationAndMoficationRecordFieldsOneColl(String pId, 
			String pCreationDate, String pCreatedBy, String pLastUpdateDate,
			String pLastUpdateBy) {
		return getCreationAndMoficationRecordFields("", "1", pId, "", pCreationDate, "",
				pCreatedBy, "", pLastUpdateDate, "", pLastUpdateBy, "");
	}

	public String getCreationAndMoficationRecordFieldsOneColl(
			String pCreationDate, String pCreatedBy, String pLastUpdateDate,
			String pLastUpdateBy) {
		return getCreationAndMoficationRecordFields("", "1", "", "", pCreationDate, "",
				pCreatedBy, "", pLastUpdateDate, "", pLastUpdateBy, "");
	}

	public String getDocumentsPrefix() {
		SimpleDateFormat sdf_short = new SimpleDateFormat("yyyymmddhhmmss");

		return sdf_short.format(new Date());
	}

	public String getGeneralDBScheme() {
		String l_return = loginUser.getParameterValue("GENERAL_DB_SCHEME");
		if (isEmpty(l_return)) {
			loginUser.getCurrentUserFeature();
			l_return = loginUser.getParameterValue("GENERAL_DB_SCHEME");
		}
		return l_return;
		
	}

	private String windowStatus = "yes";

	public String getWindowStatus() {
		return windowStatus;
	}

	public String getUWndHelp(String pText) {
		String lText = pText;
		String pHelpContext = "";
		try {
			lText = pText.replaceAll("'", "\'").replaceAll("\"", "\\\"");
			pHelpContext = "<span style=\"vertical-align: top;\">&nbsp;[<a href=\"javascript://\" onclick=\"new _uWnd('HelpWd','Помощник',360,150,{autosize:1,maxh:300,minh:100,popup:1,closeonesc:1},'"
					+ lText + "');return false;\"><b>?</b></a>]</span>";
		} catch (Exception w) {
			w.toString();
		}
		return pHelpContext;
	}

	// private static String username = "";
	// private static String password = "";
	private String language = "RU";
	private String dateFormat = "DD.MM.RRRR";
	private String dateFormatTitle = "DD.MM.RRRR";
	private String jspDateFormat = "%d.%m.%Y";
	private boolean isLoged = false;
	public String loginCode = "";
	// private static Connection con = null;

	public ArrayList<bcDictionaryRecord> bankAccountType = new ArrayList<bcDictionaryRecord>();
	public ArrayList<bcDictionaryRecord> bankAccountState = new ArrayList<bcDictionaryRecord>();
	public ArrayList<bcDictionaryRecord> bkBankAccountType = new ArrayList<bcDictionaryRecord>();
	public ArrayList<bcDictionaryRecord> bkDocType = new ArrayList<bcDictionaryRecord>();
	public ArrayList<bcDictionaryRecord> bkOperationPhase = new ArrayList<bcDictionaryRecord>();
	public ArrayList<bcDictionaryRecord> bkOperationType = new ArrayList<bcDictionaryRecord>();
	public ArrayList<bcDictionaryRecord> bkOperationTypeCond = new ArrayList<bcDictionaryRecord>();
	public ArrayList<bcDictionaryRecord> bkOperationTypePart = new ArrayList<bcDictionaryRecord>();
	public ArrayList<bcDictionaryRecord> bkParticipant = new ArrayList<bcDictionaryRecord>();
	public ArrayList<bcDictionaryRecord> ccCallGroupState = new ArrayList<bcDictionaryRecord>();
	public ArrayList<bcDictionaryRecord> ccContactType = new ArrayList<bcDictionaryRecord>();
	public ArrayList<bcDictionaryRecord> ccFAQCategory = new ArrayList<bcDictionaryRecord>();
	public ArrayList<bcDictionaryRecord> ccInquirerLineType = new ArrayList<bcDictionaryRecord>();
	public ArrayList<bcDictionaryRecord> ccInquirerState = new ArrayList<bcDictionaryRecord>();
	public ArrayList<bcDictionaryRecord> ccSettingState = new ArrayList<bcDictionaryRecord>();
	public ArrayList<bcDictionaryRecord> ccQuestionImpotant = new ArrayList<bcDictionaryRecord>();
	public ArrayList<bcDictionaryRecord> ccQuestionStatus = new ArrayList<bcDictionaryRecord>();
	public ArrayList<bcDictionaryRecord> ccQuestionType = new ArrayList<bcDictionaryRecord>();
	public ArrayList<bcDictionaryRecord> ccQuestionUrgent = new ArrayList<bcDictionaryRecord>();
	public ArrayList<bcDictionaryRecord> ccUserStatus = new ArrayList<bcDictionaryRecord>();
	public ArrayList<bcDictionaryRecord> clubCardInfoStore = new ArrayList<bcDictionaryRecord>();
	public ArrayList<bcDictionaryRecord> clubCardMaterial = new ArrayList<bcDictionaryRecord>();
	public ArrayList<bcDictionaryRecord> clubCardOperationState = new ArrayList<bcDictionaryRecord>();
	public ArrayList<bcDictionaryRecord> clubCardOperationType = new ArrayList<bcDictionaryRecord>();
	public ArrayList<bcDictionaryRecord> clubCardOperationTypeCardRespAction = new ArrayList<bcDictionaryRecord>();
	public ArrayList<bcDictionaryRecord> clubCardPurseType = new ArrayList<bcDictionaryRecord>();
	public ArrayList<bcDictionaryRecord> clubCardState = new ArrayList<bcDictionaryRecord>();
	public ArrayList<bcDictionaryRecord> clubCardStatus = new ArrayList<bcDictionaryRecord>();
	public ArrayList<bcDictionaryRecord> clubCardType = new ArrayList<bcDictionaryRecord>();
	public ArrayList<bcDictionaryRecord> clubComissionParticipant = new ArrayList<bcDictionaryRecord>();
	public ArrayList<bcDictionaryRecord> clubEstimateCriterion = new ArrayList<bcDictionaryRecord>();
	public ArrayList<bcDictionaryRecord> clubEventState = new ArrayList<bcDictionaryRecord>();
	public ArrayList<bcDictionaryRecord> clubEventType = new ArrayList<bcDictionaryRecord>();
	public ArrayList<bcDictionaryRecord> clubMemberStatus = new ArrayList<bcDictionaryRecord>();
	public ArrayList<bcDictionaryRecord> clubMemberType = new ArrayList<bcDictionaryRecord>();
	public ArrayList<bcDictionaryRecord> clubRelType = new ArrayList<bcDictionaryRecord>();
	public ArrayList<bcDictionaryRecord> clubRelType2 = new ArrayList<bcDictionaryRecord>();
	public ArrayList<bcCurrencyRecord> wpcurrency = new ArrayList<bcCurrencyRecord>();
	public ArrayList<bcDictionaryRecord> docState = new ArrayList<bcDictionaryRecord>();
	public ArrayList<bcDictionaryRecord> docType = new ArrayList<bcDictionaryRecord>();
	public ArrayList<bcDictionaryRecord> dsDispatchKind = new ArrayList<bcDictionaryRecord>();
	public ArrayList<bcDictionaryRecord> dsDispatchType = new ArrayList<bcDictionaryRecord>();
	public ArrayList<bcDictionaryRecord> dsMessageOperType = new ArrayList<bcDictionaryRecord>();
	public ArrayList<bcDictionaryRecord> dsPatternStatus = new ArrayList<bcDictionaryRecord>();
	public ArrayList<bcDictionaryRecord> dsPatternType = new ArrayList<bcDictionaryRecord>();
	public ArrayList<bcDictionaryRecord> dsPatternTypeAddition = new ArrayList<bcDictionaryRecord>();
	public ArrayList<bcDictionaryRecord> dsProfileState = new ArrayList<bcDictionaryRecord>();
	public ArrayList<bcDictionaryRecord> dsSMSDelivStatus = new ArrayList<bcDictionaryRecord>();
	public ArrayList<bcDictionaryRecord> dsSMSSendStatus = new ArrayList<bcDictionaryRecord>();
	public ArrayList<bcDictionaryRecord> dsSMSState = new ArrayList<bcDictionaryRecord>();
	public ArrayList<bcDictionaryRecord> dsSMSType = new ArrayList<bcDictionaryRecord>();
	public ArrayList<bcDictionaryRecord> fnCardOperState = new ArrayList<bcDictionaryRecord>();
	public ArrayList<bcDictionaryRecord> fnInvoiceCreation = new ArrayList<bcDictionaryRecord>();
	public ArrayList<bcDictionaryRecord> fnInvoiceState = new ArrayList<bcDictionaryRecord>();
	public ArrayList<bcDictionaryRecord> fnOperExecType = new ArrayList<bcDictionaryRecord>();
	public ArrayList<bcDictionaryRecord> fnOperState = new ArrayList<bcDictionaryRecord>();
	public ArrayList<bcDictionaryRecord> fnOperType = new ArrayList<bcDictionaryRecord>();
	public ArrayList<bcDictionaryRecord> fnPostingSchemeState = new ArrayList<bcDictionaryRecord>();
	public ArrayList<bcDictionaryRecord> fnPriority = new ArrayList<bcDictionaryRecord>();
	public ArrayList<bcDictionaryRecord> giftType = new ArrayList<bcDictionaryRecord>();
	public ArrayList<bcDictionaryRecord> jurPrsGiftRequestType = new ArrayList<bcDictionaryRecord>();
	public ArrayList<bcDictionaryRecord> jurPrsGiftRequestState = new ArrayList<bcDictionaryRecord>();
	public ArrayList<bcDictionaryRecord> jurPrsKind = new ArrayList<bcDictionaryRecord>();
	//public ArrayList<bcDictionaryRecord> jurPrsType = new ArrayList<bcDictionaryRecord>();
	public ArrayList<bcDictionaryRecord> languagesAll = new ArrayList<bcDictionaryRecord>();
	public ArrayList<bcDictionaryRecord> lgDocType = new ArrayList<bcDictionaryRecord>();
	public ArrayList<bcDictionaryRecord> lgPromoterGiveState = new ArrayList<bcDictionaryRecord>();
	public ArrayList<bcDictionaryRecord> lgPromoterPayKind = new ArrayList<bcDictionaryRecord>();
	public ArrayList<bcDictionaryRecord> lgPromoterPayState = new ArrayList<bcDictionaryRecord>();
	public ArrayList<bcDictionaryRecord> lgPromoterPost = new ArrayList<bcDictionaryRecord>();
	public ArrayList<bcDictionaryRecord> lgPromoterState = new ArrayList<bcDictionaryRecord>();
	public ArrayList<bcDictionaryRecord> lgType = new ArrayList<bcDictionaryRecord>();
	public ArrayList<bcDictionaryRecord> loyKindCodeOnly = new ArrayList<bcDictionaryRecord>();
	public ArrayList<bcDictionaryRecord> loyKindFull = new ArrayList<bcDictionaryRecord>();
	public ArrayList<bcDictionaryRecord> natPrsDocType = new ArrayList<bcDictionaryRecord>();
	public ArrayList<bcDictionaryRecord> natPrsGiftState = new ArrayList<bcDictionaryRecord>();
	public ArrayList<bcDictionaryRecord> natPrsGiftRequestState = new ArrayList<bcDictionaryRecord>();
	public ArrayList<bcDictionaryRecord> natPrsGiftRequestType = new ArrayList<bcDictionaryRecord>();
	public ArrayList<bcDictionaryRecord> natPrsGroup = new ArrayList<bcDictionaryRecord>();
	public ArrayList<bcDictionaryRecord> natPrsRoleState = new ArrayList<bcDictionaryRecord>();
	public ArrayList<bcDictionaryRecord> nomenklUnit = new ArrayList<bcDictionaryRecord>();
	public ArrayList<bcDictionaryRecord> officePrivateState = new ArrayList<bcDictionaryRecord>();
	public ArrayList<bcDictionaryRecord> officePrivateType = new ArrayList<bcDictionaryRecord>();
	public ArrayList<bcDictionaryRecord> OPActionState = new ArrayList<bcDictionaryRecord>();
	public ArrayList<bcDictionaryRecord> OPActionType = new ArrayList<bcDictionaryRecord>();
	public ArrayList<bcDictionaryRecord> paymentSystem = new ArrayList<bcDictionaryRecord>();
	public ArrayList<bcDictionaryRecord> privilegeType = new ArrayList<bcDictionaryRecord>();
	public ArrayList<bcDictionaryRecord> questPaymentMethod = new ArrayList<bcDictionaryRecord>();
	public ArrayList<bcDictionaryRecord> referralSchemeType = new ArrayList<bcDictionaryRecord>();
	public ArrayList<bcDictionaryRecord> referralSchemeCalcType = new ArrayList<bcDictionaryRecord>();
	public ArrayList<bcDictionaryRecord> remittanceState = new ArrayList<bcDictionaryRecord>();
	public ArrayList<bcDictionaryRecord> remittanceType = new ArrayList<bcDictionaryRecord>();
	public ArrayList<bcDictionaryRecord> reportKind = new ArrayList<bcDictionaryRecord>();
	public ArrayList<bcDictionaryRecord> reportTaskState = new ArrayList<bcDictionaryRecord>();
	public ArrayList<bcDictionaryRecord> SAMStatus = new ArrayList<bcDictionaryRecord>();
	public ArrayList<bcDictionaryRecord> SAMType = new ArrayList<bcDictionaryRecord>();
	public ArrayList<bcDictionaryRecord> submissionType = new ArrayList<bcDictionaryRecord>();
	public ArrayList<bcDictionaryRecord> sysErrTXCode = new ArrayList<bcDictionaryRecord>();
	public ArrayList<bcDictionaryRecord> sysEventType = new ArrayList<bcDictionaryRecord>();
	public ArrayList<bcDictionaryRecord> sysModuleType = new ArrayList<bcDictionaryRecord>();
	public ArrayList<bcDictionaryRecord> sysUserAccessType = new ArrayList<bcDictionaryRecord>();
	public ArrayList<bcDictionaryRecord> sysUserStatus = new ArrayList<bcDictionaryRecord>();
	public ArrayList<bcDictionaryRecord> targetPrgPayPeriod = new ArrayList<bcDictionaryRecord>();
	public ArrayList<bcDictionaryRecord> taxValueType = new ArrayList<bcDictionaryRecord>();
	public ArrayList<bcDictionaryRecord> telgrExternalState = new ArrayList<bcDictionaryRecord>();
	public ArrayList<bcDictionaryRecord> termMessageState = new ArrayList<bcDictionaryRecord>();
	public ArrayList<bcDictionaryRecord> termMessageType = new ArrayList<bcDictionaryRecord>();
	public ArrayList<bcDictionaryRecord> termPayConfirmationWay = new ArrayList<bcDictionaryRecord>();
	public ArrayList<bcDictionaryRecord> termSesCreqState = new ArrayList<bcDictionaryRecord>();
	public ArrayList<bcDictionaryRecord> termSesDataState = new ArrayList<bcDictionaryRecord>();
	public ArrayList<bcDictionaryRecord> termSesMonState = new ArrayList<bcDictionaryRecord>();
	public ArrayList<bcDictionaryRecord> termSesParamState = new ArrayList<bcDictionaryRecord>();
	public ArrayList<bcDictionaryRecord> termStatus = new ArrayList<bcDictionaryRecord>();
	public ArrayList<bcDictionaryRecord> termType = new ArrayList<bcDictionaryRecord>();
	public ArrayList<bcDictionaryRecord> termTypeForLoyality = new ArrayList<bcDictionaryRecord>();
	public ArrayList<bcDictionaryRecord> termUserStatus = new ArrayList<bcDictionaryRecord>();
	public ArrayList<bcDictionaryRecord> termUserAccessType = new ArrayList<bcDictionaryRecord>();
	public ArrayList<bcDictionaryRecord> trainingPostType = new ArrayList<bcDictionaryRecord>();
	public ArrayList<bcDictionaryRecord> transType = new ArrayList<bcDictionaryRecord>();
	public ArrayList<bcDictionaryRecord> transState = new ArrayList<bcDictionaryRecord>();
	public ArrayList<bcDictionaryRecord> transPayType = new ArrayList<bcDictionaryRecord>();
	public ArrayList<bcDictionaryRecord> warningStatus = new ArrayList<bcDictionaryRecord>();

	public ArrayList<bcDictionaryRecord> systemUsers = new ArrayList<bcDictionaryRecord>();

	public ArrayList<bcLookupRecord> lookups = new ArrayList<bcLookupRecord>();

	/*
	 * public bcXML accountXML = accountsXML.getInstance(); public bcXML bankXML
	 * = banksXML.getInstance(); public bcXML bank_statementXML =
	 * bank_statementsXML.getInstance(); public bcXML bk_accountXML =
	 * bk_accountsXML.getInstance(); public bcXML bk_schemeXML =
	 * bk_schemesXML.getInstance(); public bcXML buttonXML =
	 * buttonsXML.getInstance(); public bcXML cardsettingXML =
	 * cardsettingsXML.getInstance(); public bcXML calculatorXML =
	 * calculatorsXML.getInstance(); public bcXML card_taskXML =
	 * card_tasksXML.getInstance(); public bcXML call_centerXML =
	 * call_centersXML.getInstance(); public bcXML clearingXML =
	 * clearingsXML.getInstance(); public bcXML clubcardXML =
	 * clubcardsXML.getInstance(); public bcXML clubXML =
	 * clubsXML.getInstance(); public bcXML club_actionXML =
	 * club_actionsXML.getInstance(); public bcXML clubfundXML =
	 * clubfundsXML.getInstance(); public bcXML comissionXML =
	 * comissionsXML.getInstance(); public bcXML commonXML =
	 * commonsXML.getInstance(); public bcXML contactXML =
	 * contactsXML.getInstance(); public bcXML currencyXML =
	 * currenciesXML.getInstance(); public bcXML daily_rateXML =
	 * daily_ratesXML.getInstance(); public bcXML data_terminalXML =
	 * data_terminalsXML.getInstance(); public bcXML dictionaryXML =
	 * dictionariesXML.getInstance(); public bcXML documentXML =
	 * documentsXML.getInstance(); public bcXML eventXML =
	 * eventsXML.getInstance(); public bcXML emailXML = emailsXML.getInstance();
	 * public bcXML exp_fileXML = exp_filesXML.getInstance(); public bcXML
	 * form_messageXML = form_messagesXML.getInstance(); public bcXML issuerXML
	 * = issuersXML.getInstance(); public bcXML jurpersonServPlaceXML =
	 * jurpersonsserviceplacesXML.getInstance(); public bcXML jurpersonXML =
	 * jurpersonsXML.getInstance(); public bcXML kvitovkaXML =
	 * kvitovkasXML.getInstance(); public bcXML logisticXML =
	 * logisticsXML.getInstance(); public bcXML loylineXML =
	 * loylinesXML.getInstance(); public bcXML loyXML = loysXML.getInstance();
	 * public bcXML messageXML = messagesXML.getInstance(); public bcXML
	 * natprsXML = natpersonsXML.getInstance(); public bcXML oper_schemeXML =
	 * oper_schemesXML.getInstance(); public bcXML postingXML =
	 * postingsXML.getInstance(); public bcXML posting_schemeXML =
	 * posting_schemesXML.getInstance(); public bcXML private_officeXML =
	 * private_officesXML.getInstance(); public bcXML purchaseXML =
	 * purchasesXML.getInstance(); public bcXML questionnaireXML =
	 * questionnairesXML.getInstance(); public bcXML reglamentXML =
	 * reglamentsXML.getInstance(); public bcXML relationshipXML =
	 * relationshipsXML.getInstance(); public bcXML remittanceXML =
	 * remittancesXML.getInstance(); public bcXML reportXML =
	 * reportsXML.getInstance(); public bcXML roleXML = rolesXML.getInstance();
	 * public bcXML samXML = samsXML.getInstance(); public bcXML
	 * security_monitorXML = security_monitorsXML.getInstance(); public bcXML
	 * settingXML = settingsXML.getInstance(); public bcXML shedulelineXML =
	 * shedulelinesXML.getInstance(); public bcXML sheduleXML =
	 * shedulesXML.getInstance(); public bcXML smsXML = SMSsXML.getInstance();
	 * public bcXML stornoXML = stornosXML.getInstance(); public bcXML syslogXML
	 * = syslogsXML.getInstance(); public bcXML sysmonitorXML =
	 * sysmonitoresXML.getInstance(); public bcXML taxXML =
	 * taxesXML.getInstance(); public bcXML telegramXML =
	 * telegramsXML.getInstance(); public bcXML terminalXML =
	 * terminalsXML.getInstance(); public bcXML terminalCertificateXML =
	 * terminalcertificatesXML.getInstance(); public bcXML term_sesXML =
	 * term_sessionsXML.getInstance(); public bcXML trainingXML =
	 * trainingsXML.getInstance(); public bcXML transactionXML =
	 * transactionsXML.getInstance(); public bcXML transcorrectionXML =
	 * transcorrectionsXML.getInstance(); public bcXML userXML =
	 * usersXML.getInstance(); public bcXML warningXML =
	 * warningsXML.getInstance();
	 */

	/*
	 * public bcXML accountXML = bcXMLRealization.getInstance("accounts.xml");
	 * public bcXML bankXML = bcXMLRealization.getInstance("Banks.xml"); public
	 * bcXML bank_statementXML =
	 * bcXMLRealization.getInstance("bank_statements.xml"); public bcXML
	 * bk_accountXML = bcXMLRealization.getInstance("bk_accounts.xml"); public
	 * bcXML bk_schemeXML = bcXMLRealization.getInstance("bk_scheme.xml");
	 * public bcXML buttonXML = bcXMLRealization.getInstance("buttons.xml");
	 * public bcXML cardsettingXML =
	 * bcXMLRealization.getInstance("cardsettings.xml"); public bcXML
	 * card_taskXML = bcXMLRealization.getInstance("card_tasks.xml"); public
	 * bcXML calculatorXML = bcXMLRealization.getInstance("calculator.xml");
	 * public bcXML call_centerXML =
	 * bcXMLRealization.getInstance("call_center.xml"); public bcXML clearingXML
	 * = bcXMLRealization.getInstance("clearing.xml"); public bcXML clubXML =
	 * bcXMLRealization.getInstance("club.xml"); public bcXML club_actionXML =
	 * bcXMLRealization.getInstance("club_actions.xml"); public bcXML
	 * clubcardXML = bcXMLRealization.getInstance("clubcards.xml"); public bcXML
	 * clubfundXML = bcXMLRealization.getInstance("clubfund.xml"); public bcXML
	 * comissionXML = bcXMLRealization.getInstance("comission.xml"); public
	 * bcXML commonXML = bcXMLRealization.getInstance("Common.xml"); public
	 * bcXML contactXML = bcXMLRealization.getInstance("contacts.xml"); public
	 * bcXML currencyXML = bcXMLRealization.getInstance("currency.xml"); public
	 * bcXML daily_rateXML = bcXMLRealization.getInstance("daily_rates.xml");
	 * public bcXML data_terminalXML =
	 * bcXMLRealization.getInstance("data_terminals.xml"); public bcXML
	 * dictionaryXML = bcXMLRealization.getInstance("dictionary.xml"); public
	 * bcXML documentXML = bcXMLRealization.getInstance("documents.xml"); public
	 * bcXML emailXML = bcXMLRealization.getInstance("emails.xml"); public bcXML
	 * eventXML = bcXMLRealization.getInstance("Events.xml"); public bcXML
	 * exp_fileXML = bcXMLRealization.getInstance("exp_files.xml"); public bcXML
	 * form_messageXML = bcXMLRealization.getInstance("form_messages.xml");
	 * public bcXML issuerXML = bcXMLRealization.getInstance("issuers.xml");
	 * public bcXML jurpersonServPlaceXML =
	 * bcXMLRealization.getInstance("JurPersonsServicePlace.xml"); public bcXML
	 * jurpersonXML = bcXMLRealization.getInstance("JurPersons.xml"); public
	 * bcXML kvitovkaXML = bcXMLRealization.getInstance("kvitovka.xml"); public
	 * bcXML logisticXML = bcXMLRealization.getInstance("logistics.xml"); public
	 * bcXML loylineXML = bcXMLRealization.getInstance("loylines.xml"); public
	 * bcXML loyXML = bcXMLRealization.getInstance("loy.xml"); public bcXML
	 * messageXML = bcXMLRealization.getInstance("messages.xml"); public bcXML
	 * natprsXML = bcXMLRealization.getInstance("NatPersons.xml"); public bcXML
	 * oper_schemeXML = bcXMLRealization.getInstance("oper_scheme.xml"); public
	 * bcXML postingXML = bcXMLRealization.getInstance("postings.xml"); public
	 * bcXML posting_schemeXML =
	 * bcXMLRealization.getInstance("posting_scheme.xml"); public bcXML
	 * private_officeXML = bcXMLRealization.getInstance("private_office.xml");
	 * public bcXML purchaseXML = bcXMLRealization.getInstance("purchase.xml");
	 * public bcXML questionnaireXML =
	 * bcXMLRealization.getInstance("questionnaire.xml"); public bcXML
	 * reglamentXML = bcXMLRealization.getInstance("Reglaments.xml"); public
	 * bcXML relationshipXML = bcXMLRealization.getInstance("relationship.xml");
	 * public bcXML remittanceXML =
	 * bcXMLRealization.getInstance("remittances.xml"); public bcXML reportXML =
	 * bcXMLRealization.getInstance("reports.xml"); public bcXML roleXML =
	 * bcXMLRealization.getInstance("Roles.xml"); public bcXML samXML =
	 * bcXMLRealization.getInstance("sam.xml"); public bcXML security_monitorXML
	 * = bcXMLRealization.getInstance("security_monitor.xml"); public bcXML
	 * settingXML = bcXMLRealization.getInstance("Settings.xml"); public bcXML
	 * shedulelineXML = bcXMLRealization.getInstance("shedulelines.xml"); public
	 * bcXML sheduleXML = bcXMLRealization.getInstance("shedule.xml"); public
	 * bcXML smsXML = bcXMLRealization.getInstance("sms.xml"); public bcXML
	 * stornoXML = bcXMLRealization.getInstance("storno.xml"); public bcXML
	 * syslogXML = bcXMLRealization.getInstance("sys_log.xml"); public bcXML
	 * sysmonitorXML = bcXMLRealization.getInstance("sys_monitor.xml"); public
	 * bcXML taxXML = bcXMLRealization.getInstance("taxes.xml"); public bcXML
	 * telegramXML = bcXMLRealization.getInstance("telegrams.xml"); public bcXML
	 * terminalXML = bcXMLRealization.getInstance("terminals.xml"); public bcXML
	 * terminalCertificateXML =
	 * bcXMLRealization.getInstance("terminalCertificate.xml"); public bcXML
	 * term_manufacturerXML =
	 * bcXMLRealization.getInstance("term_manufacturers.xml"); public bcXML
	 * term_sesXML = bcXMLRealization.getInstance("term_ses.xml"); public bcXML
	 * trainingXML = bcXMLRealization.getInstance("training.xml"); public bcXML
	 * transactionXML = bcXMLRealization.getInstance("transactions.xml"); public
	 * bcXML transcorrectionXML =
	 * bcXMLRealization.getInstance("Transcorrections.xml"); public bcXML
	 * userXML = bcXMLRealization.getInstance("Users.xml"); public bcXML
	 * warningXML = bcXMLRealization.getInstance("warnings.xml");
	 */

	public bcXML accountXML = bcDictionary.getInstance("accounts");
	public bcXML bank_statementXML = bcDictionary.getInstance("bank_statements");
	public bcXML bk_accountXML = bcDictionary.getInstance("bk_accounts");
	public bcXML bk_schemeXML = bcDictionary.getInstance("bk_scheme");
	public bcXML buttonXML = bcDictionary.getInstance("buttons");
	public bcXML cardsettingXML = bcDictionary.getInstance("cardsettings");
	public bcXML card_taskXML = bcDictionary.getInstance("card_tasks");
	public bcXML calculatorXML = bcDictionary.getInstance("calculator");
	public bcXML call_centerXML = bcDictionary.getInstance("call_center");
	public bcXML clearingXML = bcDictionary.getInstance("clearing");
	public bcXML clubXML = bcDictionary.getInstance("club");
	public bcXML club_actionXML = bcDictionary.getInstance("club_actions");
	public bcXML clubcardXML = bcDictionary.getInstance("clubcards");
	public bcXML clubfundXML = bcDictionary.getInstance("clubfund");
	public bcXML comissionXML = bcDictionary.getInstance("comission");
	public bcXML commonXML = bcDictionary.getInstance("Common");
	public bcXML contactXML = bcDictionary.getInstance("contacts");
	public bcXML currencyXML = bcDictionary.getInstance("currency");
	public bcXML daily_rateXML = bcDictionary.getInstance("daily_rates");
	public bcXML dictionaryXML = bcDictionary.getInstance("dictionary");
	public bcXML documentXML = bcDictionary.getInstance("documents");
	public bcXML emailXML = bcDictionary.getInstance("emails");
	public bcXML eventXML = bcDictionary.getInstance("Events");
	public bcXML exp_fileXML = bcDictionary.getInstance("exp_files");
	public bcXML form_messageXML = bcDictionary.getInstance("form_messages");
	public bcXML issuerXML = bcDictionary.getInstance("issuers");
	public bcXML jurpersonXML = bcDictionary.getInstance("JurPersons");
	public bcXML kvitovkaXML = bcDictionary.getInstance("kvitovka");
	public bcXML logisticXML = bcDictionary.getInstance("logistics");
	public bcXML loylineXML = bcDictionary.getInstance("loylines");
	public bcXML loyXML = bcDictionary.getInstance("loy");
	public bcXML messageXML = bcDictionary.getInstance("messages");
	public bcXML natprsXML = bcDictionary.getInstance("NatPersons");
	public bcXML oper_schemeXML = bcDictionary.getInstance("oper_scheme");
	public bcXML postingXML = bcDictionary.getInstance("postings");
	public bcXML posting_schemeXML = bcDictionary.getInstance("posting_scheme");
	public bcXML private_officeXML = bcDictionary.getInstance("private_office");
	public bcXML questionnaireXML = bcDictionary.getInstance("questionnaire");
	public bcXML reglamentXML = bcDictionary.getInstance("Reglaments");
	public bcXML relationshipXML = bcDictionary.getInstance("relationship");
	public bcXML remittanceXML = bcDictionary.getInstance("remittances");
	public bcXML reportXML = bcDictionary.getInstance("reports");
	public bcXML roleXML = bcDictionary.getInstance("Roles");
	public bcXML samXML = bcDictionary.getInstance("sam");
	public bcXML security_monitorXML = bcDictionary
			.getInstance("security_monitor");
	public bcXML settingXML = bcDictionary.getInstance("Settings");
	public bcXML shedulelineXML = bcDictionary.getInstance("shedulelines");
	public bcXML sheduleXML = bcDictionary.getInstance("shedule");
	public bcXML smsXML = bcDictionary.getInstance("sms");
	public bcXML stornoXML = bcDictionary.getInstance("storno");
	public bcXML syslogXML = bcDictionary.getInstance("sys_log");
	public bcXML sysmonitorXML = bcDictionary.getInstance("sys_monitor");
	public bcXML taxXML = bcDictionary.getInstance("taxes");
	public bcXML telegramXML = bcDictionary.getInstance("telegrams");
	public bcXML terminalXML = bcDictionary.getInstance("terminals");
	public bcXML terminalCertificateXML = bcDictionary
			.getInstance("terminalCertificate");
	public bcXML term_manufacturerXML = bcDictionary
			.getInstance("term_manufacturers");
	public bcXML term_sesXML = bcDictionary.getInstance("term_ses");
	public bcXML trainingXML = bcDictionary.getInstance("training");
	public bcXML transactionXML = bcDictionary.getInstance("transactions");
	public bcXML userXML = bcDictionary.getInstance("Users");
	public bcXML warningXML = bcDictionary.getInstance("warnings");

	public bcParamXML param = bcParamXML.getInstance();


	public String getXMLFieldTransl(bcXML pXML, String pFieldName,
			boolean pMandatory) {
		return pXML.getfieldTransl(pFieldName, pMandatory);
	}

	public String getClubCardXMLFieldTransl(String pFieldName,
			boolean pMandatory) {
		return clubcardXML.getfieldTransl(pFieldName,
				pMandatory);
	}

	protected myLinks localLink = new myLinks();

	public void goBack() {
		localLink.goBack();
	}

	public void addLink(String pLink) {
		localLink.addLink(pLink);
	}

	public void clearLink() {
		localLink.clearAll();
	}

	public String getLinkButtons() {
		return localLink.formatButtons();
	}

	public void writeException(String pForm, String pType, String pProcess,
			String pAction, String pMessage) {
		LOGGER.debug(pForm + ": type=" + pType + ", process=" + pProcess
				+ ", action=" + pAction + ", " + pMessage);
	}

	public bcHeaders header = new bcHeaders(this.getDateFormat());

	private Map<String, bc.service.pages> pagesObjHm = new HashMap<String, bc.service.pages>(); // форма,
																								// значение

	public void pageCheck(String pFormName, String pPageNumber) {

		if (!pagesObjHm.containsKey(pFormName)) {
			pageInitialSet(pFormName);
		} else {
			if (isEmpty(pPageNumber)) {
				String lCurPage = this.pagesObjHm.get(pFormName)
						.getCurrentPage();
				if (isEmpty(lCurPage)) {
					pageSetValue(pFormName, "1");
				}
			} else {
				pageSetValue(pFormName, pPageNumber);
			}
		}
		pPageNumber = getPageNumber(pFormName);
	}

	public String getPageNumber(String pFormName) {
		String lCurPage = "";
		if (pagesObjHm.containsKey(pFormName)) {
			lCurPage = this.pagesObjHm.get(pFormName).getCurrentPage();
		}
		if (isEmpty(lCurPage)) {
			lCurPage = "1";
		}
		return lCurPage;
	}

	private void pageInitialSet(String pFormName) {

		String rowsPerPageLoginUser = loginUser.getParameterValue("ROWS_ON_PAGE");

		bc.service.pages onePage = new bc.service.pages(rowsPerPageLoginUser);
		pagesObjHm.put(pFormName, onePage);
	}

	private void pageSetValue(String pFormName, String pPageNumber) {
		if (!pagesObjHm.containsKey(pFormName)) {
			pageInitialSet(pFormName);
		}
		if (isEmpty(pPageNumber)) {
			pPageNumber = "";
		}
		try {
			Integer.parseInt(pPageNumber);
		} catch (Exception e) {
			pPageNumber = "";
		}

		String rowsPerPageLoginUser = this.loginUser.getParameterValue("ROWS_ON_PAGE");

		bc.service.pages onePage = new bc.service.pages(pPageNumber,
				rowsPerPageLoginUser);
		pagesObjHm.put(pFormName, onePage);
	}

	public String getCurrentPage(String pFormName) {
		return this.pagesObjHm.get(pFormName).getCurrentPage();
	}

	public String getNexPage(String pFormName) {
		return this.pagesObjHm.get(pFormName).getNexPage();
	}

	public String getFirstRowNumber(String pFormName) {
		return this.pagesObjHm.get(pFormName).getFirstRowNumber();
	}

	public String getLastRowNumber(String pFormName) {
		return this.pagesObjHm.get(pFormName).getLastRowNumber();
	}

	public String getRowsPerPage() {
		String pValue = this.loginUser.getParameterValue("ROWS_ON_PAGE");
		if (isEmpty(pValue)) {
			pValue = "50";
		}
		return pValue;
	}

	public String getPagesHTML(String pFormName, String pHyperLink,
			String pPageTagName, String pRowCount, String pDivName) {

		int rowsPerPage = 0;
		String rowsPerPageLoginUser = this.loginUser.getParameterValue("ROWS_ON_PAGE");

		if (!isEmpty(rowsPerPageLoginUser)) {
			rowsPerPage = Integer.parseInt(rowsPerPageLoginUser);
		} else {
			rowsPerPage = 50;
		}

		String lDiv = isEmpty(pDivName)?"div_main":pDivName;
		
		StringBuilder html = new StringBuilder();
		String l_page_cur = getCurrentPage(pFormName);
		String l_page_next = getNexPage(pFormName);
		String l_page_prev = "" + (Integer.parseInt(l_page_cur) - 1);

		int l_page_count = 0;
		int l_row_count = 0;
		if (!(isEmpty(pRowCount) || "0".equalsIgnoreCase(pRowCount))) {
			l_row_count = Integer.parseInt(pRowCount);
			l_page_count = Math.round((float) Integer.parseInt(pRowCount)
					/ rowsPerPage);
			int l_temp = l_row_count - l_page_count * rowsPerPage;
			if (l_temp > 0) {
				l_page_count = l_page_count + 1;
			}
		}

		html.append("<td valign=\"middle\" width=\"20\">\n");
		html.append("<input type=\"hidden\" id=\"row_count\" value=\""
				+ l_page_count + "\">\n");
		if (l_page_count > 0) {
			html.append(commonXML
					.getfieldTransl( "page_short", false)
					//+ "&nbsp;"
					+ l_page_cur
					+ "&nbsp;"
					+ "из"
					+ "&nbsp;"
					+ l_page_count
					+ "&nbsp;&nbsp;\n");
		} else {
			html.append(commonXML
					.getfieldTransl( "page_short", false)
					//+ "&nbsp;"
					/*+ l_page_cur*/ + "&nbsp;" + /*"&nbsp;&nbsp;" +*/ "\n");
		}
		html.append("</td>\n");

		if (!isEmpty(pRowCount) && l_page_count < 2) {
			html.append("<td valign=\"middle\" width=\"20\">\n");
			html.append("<input type=\"text\" name=\""
					+ pPageTagName + "\" id=\"id_" + pPageTagName
					+ "\" size=\"1\" value=\"" + l_page_cur
					+ "\" class=\"inputfield-ro\" title=\""
					+ buttonXML.getfieldTransl( "go_to_page", false)
					+ "\">\n");
			html.append("</td>\n");
			html.append("<td valign=\"middle\" width=\"20\">\n");
			html.append("<button type=\"button\" disabled class=\"button2\" title=\""
					+ buttonXML.getfieldTransl( "button_go_to_page",
							false) + "\">></button>\n");
			html.append("</td>\n");
		} else {
			html.append("<td valign=\"middle\" width=\"20\">\n");
			html.append("<input type=\"text\" style=\"background:white\" name=\""
					+ pPageTagName + "\" id=\"id_" + pPageTagName
					+ "\" size=\"1\" value=\"" + l_page_cur
					+ "\" class=\"inputfield\" title=\""
					+ buttonXML.getfieldTransl( "go_to_page", false)
					+ "\">\n");
			html.append("</td>\n");
			html.append("<td valign=\"middle\" width=\"20\">\n");
			html.append("<button type=\"button\" onclick=\"checkMaxRowNumberForDiv('"
					+ pHyperLink
					+ pPageTagName
					+ "=' + getElementById('id_"
					+ pPageTagName
					+ "').value, '"
					+ l_page_count
					+ "', document.getElementById('id_"
					+ pPageTagName
					+ "').value, '" + lDiv + "'); \" class=\"button2\" title=\""
					+ buttonXML.getfieldTransl( "button_go_to_page",
							false) + "\">></button>\n");
			html.append("</td>\n");
		}
		if (!"1".equalsIgnoreCase(l_page_cur)) {
			html.append("<td valign=\"middle\" width=\"20\">\n");
			html.append("<div class=\"div_pages\" onclick=\"ajaxpage('"
					+ pHyperLink + pPageTagName + "=1', '"
					+ lDiv + "')\">\n");
			html.append("<img vspace=\"0\" hspace=\"0\" src=\"images/go/arr_beg.gif\" align=\"top\" title=\""
					+ buttonXML.getfieldTransl( "button_begin",
							false) + "\">\n");
			html.append("</div></td>\n");
			html.append("<td valign=\"middle\" width=\"20\">\n");
			html.append("<div class=\"div_pages\" onclick=\"ajaxpage('"
					+ pHyperLink + pPageTagName + "="
					+ l_page_prev + "', '" + lDiv + "')\">\n");
			html.append("<img vspace=\"0\" hspace=\"0\" src=\"images/go/arr_left.gif\" align=\"top\" title=\""
					+ buttonXML.getfieldTransl(
							"button_previous", false) + "\">\n");
			html.append("</div></td>");
		} else {
			html.append("<td valign=\"middle\" width=\"20\">\n");
			html.append("<img vspace=\"0\" hspace=\"0\" src=\"images/go/arr_beg2.gif\" align=\"top\">\n");
			html.append("</td>\n");
			html.append("<td valign=\"middle\" width=\"20\">");
			html.append("<img vspace=\"0\" hspace=\"0\" src=\"images/go/arr_left2.gif\" align=\"top\">\n");
			html.append("</td>\n");
		}
		if (!isEmpty(pRowCount) && l_page_count < 2) {
			html.append("<td valign=\"middle\" width=\"20\">");
			html.append("<img vspace=\"0\" hspace=\"0\" src=\"images/go/arr_right2.gif\" align=\"top\">\n");
			html.append("</td>\n");
		} else {
			//System.out.println("l_page_cur="+l_page_cur+", l_page_count="+l_page_count);
			if (Integer.parseInt(l_page_cur) < l_page_count || l_page_count==0) {
				html.append("<td valign=\"middle\" width=\"20\">\n");
				html.append("<div class=\"div_pages\" onclick=\"ajaxpage('"
						+ pHyperLink + pPageTagName + "="
						+ l_page_next + "', '" + lDiv + "')\">\n");
				html.append("<img vspace=\"0\" hspace=\"0\" src=\"images/go/arr_right.gif\" align=\"top\" title=\""
						+ buttonXML.getfieldTransl(
								"button_next", false) + "\">\n");
				html.append("</div></td>\n");
			} else {
				html.append("<td valign=\"middle\" width=\"20\">");
				html.append("<img vspace=\"0\" hspace=\"0\" src=\"images/go/arr_right2.gif\" align=\"top\">\n");
				html.append("</td>\n");
			}
		}
		if (!isEmpty(pRowCount) && l_page_count < 2) {
			if (Integer.parseInt(l_page_cur) < l_page_count) {
				html.append("<td valign=\"middle\" width=\"20\">\n");
				html.append("<div class=\"div_pages\" onclick=\"ajaxpage('"
						+ pHyperLink + pPageTagName + "="
						+ l_page_count + "', '" + lDiv + "')\">\n");
				html.append("<img vspace=\"0\" hspace=\"0\" src=\"images/go/arr_end.gif\" align=\"top\" title=\""
						+ buttonXML.getfieldTransl(
								"button_next", false) + "\">\n");
				html.append("</div></td>\n");
			} else {
				html.append("<td valign=\"middle\" width=\"20\">");
				html.append("<img vspace=\"0\" hspace=\"0\" src=\"images/go/arr_end2.gif\" align=\"top\">\n");
				html.append("</td>\n");
			}
		}
		// html.append("</form>\n");

		return html.toString();
	}

	public String getPagesHTML(String pFormName, String pHyperLink,
			String pPageTagName, String pRowCount) {
		return getPagesHTML(pFormName, pHyperLink, pPageTagName, pRowCount, "");
	}

	public String getPagesHTML(String pFormName, String pHyperLink,
			String pPageTagName) {
		return getPagesHTML(pFormName, pHyperLink, pPageTagName, "", "");
	}

	// Массив текущих закладок на формах
	private Map<String, String> tabsHm = new HashMap<String, String>(); // форма,
																		// значение

	public void tabsHmSetValue(String pFormName, String pTabValue) {
		if (tabsHm.containsKey(pFormName)) {
			tabsHm.remove(pFormName);
		}
		tabsHm.put(pFormName, pTabValue);
		currentMenu.setCurrentTabForId(pTabValue);
	}

	public String getPageTitle(String pFormName) {
		return commonXML.getfieldTransl( "application_name", false)
				+ "-" + pFormName;
	}

	public String tabsHmGetValue(String pFormName) {
		String oneValue = "";
		if (tabsHm.containsKey(pFormName)) {
			oneValue = tabsHm.get(pFormName).toString();
			if ("0".equalsIgnoreCase(oneValue)) {
				oneValue = "1";
				tabsHmSetValue(pFormName, oneValue);
			}
		}
		if (isEmpty(oneValue)) {
			oneValue = "1";
			tabsHmSetValue(pFormName, oneValue);
		}
		return oneValue;
	}

	// Массив текущих фильтров на страницах
	private Map<String, String> filtersHm = new HashMap<String, String>(); // форма,
																			// значение

	public void filtersHmSetValue(String pFormName, String pFiltrValue) {
		filtersHm.put(pFormName, pFiltrValue);
	}

	public String filtersHmGetValue(String pFormName) {
		String oneValue = "";
		if (filtersHm.containsKey(pFormName)) {
			oneValue = filtersHm.get(pFormName).toString();
		}
		if ("".equalsIgnoreCase(oneValue)) {
			oneValue = "";
			filtersHmSetValue(pFormName, oneValue);
		}
		return oneValue;
	}

	public String filtersHmGetValue2(String pFormName) {
		String oneValue = null;
		if (filtersHm.containsKey(pFormName)) {
			oneValue = filtersHm.get(pFormName).toString();
		}
		if ("".equalsIgnoreCase(oneValue)) {
			oneValue = "";
			filtersHmSetValue(pFormName, oneValue);
		}
		return oneValue;
	}

	// Массив профилей страниц
	private Map<String, String> profilesHm = new HashMap<String, String>(); // форма,
																			// значение

	public void profilesHmSetValue(String pFormName, String pProfileValue) {
		profilesHm.put(pFormName, pProfileValue);
	}

	public String profilesHmGetValue(String pFormName) {
		String oneValue = "";
		if (profilesHm.containsKey(pFormName)) {
			oneValue = profilesHm.get(pFormName).toString();
		}
		if ("".equalsIgnoreCase(oneValue)) {
			oneValue = "";
			profilesHmSetValue(pFormName, oneValue);
		}
		return oneValue;
	}

	public void clearHmEntries() {
		this.tabsHm.clear();
		this.pagesObjHm.clear();
		this.filtersHm.clear();
		this.profilesHm.clear();
	}

	public void clearAllDictionaries() {
		bankAccountType.clear();
		bkBankAccountType.clear();
		bkDocType.clear();
		bkOperationPhase.clear();
		bkOperationType.clear();
		bkOperationTypeCond.clear();
		bkOperationTypePart.clear();
		bkParticipant.clear();
		ccCallGroupState.clear();
		ccContactType.clear();
		ccFAQCategory.clear();
		ccInquirerLineType.clear();
		ccInquirerState.clear();
		ccSettingState.clear();
		ccQuestionImpotant.clear();
		ccQuestionStatus.clear();
		ccQuestionType.clear();
		ccQuestionUrgent.clear();
		ccUserStatus.clear();
		clubCardInfoStore.clear();
		clubCardMaterial.clear();
		clubCardOperationState.clear();
		clubCardOperationType.clear();
		clubCardOperationTypeCardRespAction.clear();
		clubCardPurseType.clear();
		clubCardState.clear();
		clubCardStatus.clear();
		clubCardType.clear();
		clubComissionParticipant.clear();
		clubEstimateCriterion.clear();
		clubEventState.clear();
		clubEventType.clear();
		clubMemberStatus.clear();
		clubMemberType.clear();
		clubRelType.clear();
		clubRelType2.clear();
		wpcurrency.clear();
		docState.clear();
		docType.clear();
		dsDispatchKind.clear();
		dsDispatchType.clear();
		dsMessageOperType.clear();
		dsPatternStatus.clear();
		dsPatternType.clear();
		dsPatternTypeAddition.clear();
		dsProfileState.clear();
		dsSMSDelivStatus.clear();
		dsSMSSendStatus.clear();
		dsSMSState.clear();
		dsSMSType.clear();
		fnCardOperState.clear();
		fnInvoiceCreation.clear();
		fnInvoiceState.clear();
		fnOperExecType.clear();
		fnOperState.clear();
		fnOperType.clear();
		fnPostingSchemeState.clear();
		fnPriority.clear();
		giftType.clear();
		jurPrsGiftRequestType.clear();
		jurPrsGiftRequestState.clear();
		jurPrsKind.clear();
		//jurPrsType.clear();
		languagesAll.clear();
		lgDocType.clear();
		lgPromoterGiveState.clear();
		lgPromoterPayKind.clear();
		lgPromoterPayState.clear();
		lgPromoterPost.clear();
		lgPromoterState.clear();
		lgType.clear();
		loyKindCodeOnly.clear();
		loyKindFull.clear();
		natPrsDocType.clear();
		natPrsGiftState.clear();
		natPrsGiftRequestState.clear();
		natPrsGiftRequestType.clear();
		natPrsGroup.clear();
		natPrsRoleState.clear();
		nomenklUnit.clear();
		officePrivateState.clear();
		officePrivateType.clear();
		OPActionState.clear();
		OPActionType.clear();
		paymentSystem.clear();
		privilegeType.clear();
		questPaymentMethod.clear();
		remittanceState.clear();
		remittanceType.clear();
		reportTaskState.clear();
		SAMStatus.clear();
		SAMType.clear();
		submissionType.clear();
		sysErrTXCode.clear();
		sysEventType.clear();
		sysUserAccessType.clear();
		sysUserStatus.clear();
		targetPrgPayPeriod.clear();
		taxValueType.clear();
		termMessageState.clear();
		termMessageType.clear();
		termPayConfirmationWay.clear();
		termSesCreqState.clear();
		termSesDataState.clear();
		termSesMonState.clear();
		termSesParamState.clear();
		termStatus.clear();
		termType.clear();
		termTypeForLoyality.clear();
		termUserAccessType.clear();
		trainingPostType.clear();
		transType.clear();
		transState.clear();
		telgrExternalState.clear();
		transPayType.clear();
		warningStatus.clear();
		systemUsers.clear();
	}

	public Map<String, String> menuMap = new HashMap<String, String>();

	public String getCurrentClubID() {
		String lClubId = loginUser.getParameterValue("CLUBIDENT");
		lClubId = isEmpty(lClubId)?"":lClubId;
		return lClubId;
	}
	
	public String getCurrentMenuID() {
		//String lIdMenu = currentMenu.getIdMenuElement();
		return currentMenu.getIdMenuElement();
	}

	public boolean getIsLoged() {
		return this.isLoged;
	}

	public void setIsLoged(boolean isLoged) {
		this.isLoged = isLoged;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		/*if(bcBase.SESSION_PARAMETERS.LANG.getValue()==null){
			bcBase.SESSION_PARAMETERS.LANG.setValue(language);
			header.setLanguage(language);
			loginUser.setLanguage(language);
			//loginUserParam.setSessionId(this.sessionId);
			currentMenu.setLanguage(language);
			System.out.println("set language="+language);
		}*/
		if (!isEmpty(language)) {
			bcBase.SESSION_PARAMETERS.LANG.setValue(language);
			header.setLanguage(language);
			loginUser.setLanguage(language);
			//loginUserParam.setSessionId(this.sessionId);
			currentMenu.setLanguage(language);
			//System.out.println("set language="+language);
		}
		this.language = language;
	}

	public String getReportFormat() {
		return this.loginUser.getParameterValue("REPORT_FORMAT");
	}

	public String getDateFormat() {
		if (isEmpty(dateFormat)) {
			setSessionDateFormat();
		}
		return dateFormat;
	}

	public void setDateFormat(String dateFormat) {
		if(isEmpty(bcBase.SESSION_PARAMETERS.DATE_FORMAT.getValue())) {
			bcBase.SESSION_PARAMETERS.DATE_FORMAT.setValue(dateFormat);
		}
		this.dateFormat = dateFormat;
		setJSPDateFormat(dateFormat);
	}

	public String getDateFormatTitle() {
		return this.dateFormatTitle;
	}
	
	public void readDateFormat() {
		if (isEmpty(this.dateFormat)) {
			dateFormat = getOneValueByNoneId("SELECT dateformat FROM v_user_param_ln");
		}
	}

	public void setDateFormatTitle(String dateFormat) {
		this.dateFormatTitle = this.dateFormat;
	}

	public String getJSPDateFormat() {
		return jspDateFormat;
	}

	public void setJSPDateFormat(String dateFormat) {
		/* Процедуру нужно дописать */
		if (dateFormat.equalsIgnoreCase("dd.mm.rrrr")
				|| dateFormat.equalsIgnoreCase("dd.mm.yyyy")) {
			this.jspDateFormat = "%d.%m.%Y";
		} else if (dateFormat.equalsIgnoreCase("dd.mm.rr")
				|| dateFormat.equalsIgnoreCase("dd.mm.yy")) {
			this.jspDateFormat = "%d.%m.%y";
		} else if (dateFormat.equalsIgnoreCase("mm/dd/rrrr")
				|| dateFormat.equalsIgnoreCase("mm/dd/yyyy")) {
			this.jspDateFormat = "%m/%d/%Y";
		} else if (dateFormat.equalsIgnoreCase("mm/dd/rr")
				|| dateFormat.equalsIgnoreCase("mm/dd/yy")) {
			this.jspDateFormat = "%m/%d/%y";
		} else {
			this.jspDateFormat = "%d.%m.%Y";
		}
	}

	public String getCalendarScript(String pFieldName, boolean showsTime) {
		return getCalendarScript(pFieldName, this.getJSPDateFormat(), showsTime);
	}
	
    public String getCalendarScript (String pFieldName, String pJSPDateFormat, boolean showsTime) {
    	StringBuilder html = new StringBuilder();
    	
    	html.append("<script type=\"text/javascript\">\n");
    	html.append("Calendar.setup({\n");
    	html.append("	inputField  : \"id_" + pFieldName + "\",         // ID поля вводу дати\n");
    	html.append("	button      : \"btn_" + pFieldName + "\",       // ID кнопки для меню вибору дати\n");
    	
    	if (!showsTime) {
    		html.append("	ifFormat    : \"" + pJSPDateFormat + "\"    // формат дати (23.03.2008)\n");
    	} else {
    		html.append("	ifFormat    : \"" + pJSPDateFormat + "  %H:%M\",    // формат дати (23.03.2008)\n");
    		if (showsTime) {
    			html.append("	showsTime   : true\n");
    		} else {
    			html.append("	showsTime   : false\n");
    		}
    	}
    	html.append("});\n");	
    	html.append("</script>\n");
		
		return html.toString();
    }

	public String getCalendarInputField(String pFieldName, String pValue,
			String pSize) {
		return getCalendarInputField(pFieldName, pValue, pSize, "", false);
	}

	public String getCalendarInputField(String pFieldName, String pValue,
			String pSize, String pOnClickScript, boolean pReadOnly) {
		StringBuilder html = new StringBuilder();

		String lOnClick = "";
		String lSize = "";

		if (!isEmpty(pSize)) {
			lSize = " style=\"width:" + (Integer.parseInt(pSize) * 8)
					+ "px !important;\"";
		}

		if (!isEmpty(pOnClickScript)) {
			lOnClick = pOnClickScript;
		}

		if (pReadOnly) {
			html.append("<input type=\"text\" name=\"" + pFieldName + "\" " + lSize
					+ " value=\"" + pValue + "\" readonly=\"readonly\" class=\"inputfield-ro\" title=\""
					+ this.getDateFormatTitle() + "\">\n");
		} else {
			html.append("<input type=\"text\" name=\"" + pFieldName + "\" " + lSize
				+ " value=\"" + pValue + "\"  class=\"inputfield\" title=\""
				+ this.getDateFormatTitle() + "\" id=\"id_" + pFieldName
				+ "\" onclick=\"" + lOnClick + "\">\n");
			html.append("<img src=\""
				+ "../images/calendar.gif\" id=\"btn_"
				+ pFieldName
				+ "\" class=\"ui-datepicker-calendar-img\" title=\""
				+ this.buttonXML.getfieldTransl("button_data_selector", false) + "\" />\n");
		}
		return html.toString();
	}

    public String getCalendarInputFieldLanguage (String pFieldName, String pValue, String pSize) {
    	StringBuilder html = new StringBuilder();
    	
    	html.append("<input type=\"text\" name=\"" + pFieldName + "\" size=\""+ pSize + "\" value=\"" + pValue + "\"  class=\"inputfield\" title=\"" + /*this.getDateFormatTitle()  +*/ "\" id=\"id_" + pFieldName + "\">\n");
    	html.append("<img src=\"../images/calendar.gif\" id=\"btn_" + pFieldName + "\" class=\"ui-datepicker-calendar-img\" title=\"" + this.buttonXML.getfieldTransl("button_data_selector", false)  + "\" />\n");
    	
		return html.toString();
    }
        
	public String getCurrPage() {
		return currentMenu.getCurrentPage();
	}

	private String currentTerm;

	public String getCurrentTerm() {
		return isEmpty(this.currentTerm)?"":this.currentTerm;
	}

	public void setCurrentTerm(String pCurrentTerm) {
		this.currentTerm = pCurrentTerm;
	}

	private String moduleName = "crm";


	public void getLookups() {
		PreparedStatement st = null;
		Connection con = null;
		ResultSet rset = null;

		String mySQL = "SELECT * FROM " + getGeneralDBScheme()
				+ ".vc_lookup_values";

		String lookup_type = "";
		String lookup_code = "";
		String lookup_meaning = "";
		String lookup_description = "";
		int lookup_number_value = 0;
		String existFlag = "";
		String optionStyle = "";

		lookups.clear();

		try {
			//LOGGER.debug(mySQL);
			con = Connector.getConnection(this.sessionId);

			st = con.prepareStatement(mySQL);
			rset = st.executeQuery();

			while (rset.next()) {
				lookup_type = rset.getString("NAME_LOOKUP_TYPE");
				lookup_code = rset.getString("LOOKUP_CODE");
				lookup_meaning = rset.getString("MEANING");
				lookup_description = rset.getString("DESCRIPTION");
				lookup_number_value = rset.getInt("NUMBER_VALUE");
				existFlag = rset.getString("ENABLED_FLAG");
				optionStyle = rset.getString("WEB_STYLE");
				lookups.add(new bcLookupRecord(lookup_type, lookup_code,
						lookup_meaning, lookup_description,
						lookup_number_value, existFlag, optionStyle));
				// LOGGER.debug(lookup_type + " - " + lookup_code);
			}
		} catch (SQLException e) {
			LOGGER.error("bcBase.getLookups SQLException: " + e.toString());
		} catch (Exception el) {
			LOGGER.error("bcBase.getLookups Exception: " + el.toString());
		} finally {
			try {
				if (st != null) {
					st.close();
				}
			} catch (SQLException w) {
			}
			UtilityConnector.closeQuietly(st);
			UtilityConnector.closeQuietly(con);
			try {
				if (con != null) {
					con.close();
				}
			} catch (SQLException w) {
			}
			Connector.closeConnection(con);
		}
	}

	public void logOut(HttpSession session) {
		LOGGER.debug("bcBase.logOut('" + session + "')");
		this.clearHmEntries();
		this.clearAllDictionaries();
		Connector.removeSessionId(this.sessionId);
		session.invalidate();
	}

	/*public String getContextPath() {
		return AppConst.contextPath;
	}

	public void setContextPath(String p_contextPath) {
		AppConst.contextPath = p_contextPath;
		LOGGER.debug("!!!!!!!!!!!!!! AppConst.contextPath="
				+ AppConst.contextPath);
	}*/

	public String getFullContext(HttpServletRequest request) {
		String lPath = "http://" + request.getServerName() + ":"
				+ request.getLocalPort() + request.getContextPath();

		LOGGER.debug("path='" + lPath + "'");
		return lPath;
	}

	public boolean checkUser() {
		boolean resultFull = false;
		Statement st = null;
		String result = "0";
		Connection con = null;
		try {
			//LOGGER.debug("bcBase.checkUser()");
			con = Connector.getConnection(this.sessionId);
			st = con.createStatement();
			ResultSet rset = st.executeQuery("SELECT 0 FROM dual");

			while (rset.next()) {
				result = rset.getString(1);
			}
			if ("0".equalsIgnoreCase(result)) {
				resultFull = true;
			}
		} // try
		catch (SQLException se) {
			LOGGER.error("bcBase.checkUser() SQLException: " + se.toString());
			setErrorCode(se.getErrorCode());
		} catch (Exception e) {
			LOGGER.error("bcBase.checkUser() Exception: " + e.toString());
		} finally {
			try {
				if (st != null) {
					st.close();
				}
			} catch (SQLException w) {
				w.toString();
			}
			try {
				if (con != null) {
					con.close();
				}
			} catch (SQLException w) {
				w.toString();
			}
			Connector.closeConnection(con);
		} // finally

		//LOGGER.debug("checkUser(), result=" + resultFull);
		return resultFull;
	}

	public boolean isLoged() {
		int result = -1;
		CallableStatement st = null;
		Connection con = null;
		String execSQL = "";
		try {
			execSQL = "{? = call " + getGeneralDBScheme()
					+ ".CHECK_USER_session() }";
			con = Connector.getConnection(this.sessionId);
			st = con.prepareCall(execSQL);
			st.registerOutParameter(1, Types.NUMERIC);
			st.execute();
			result = st.getInt(1);
			st.close();
		} catch (SQLException e) {
			e.toString();
		} catch (Exception el) {
			el.toString();
		} finally {
			try {
				if (st != null) {
					st.close();
				}
			} catch (SQLException w) {
				w.toString();
			}
			try {
				if (con != null) {
					con.close();
				}
			} catch (SQLException w) {
				w.toString();
			}
			Connector.closeConnection(con);
		} // finally

		if (result == 0) {
			return true;
		} else {
			return false;
		}

	}

	public boolean changeLanguage(String language) {
		int result = -1;
		CallableStatement st = null;
		Connection con = null;
		String execSQL = "";
		try {
			execSQL = "{? = call " + getGeneralDBScheme()
					+ ".FNC_SET_LANGUAGE(?)}";
			//LOGGER.debug("bcBase.changeLanguage()");
			con = Connector.getConnection(this.sessionId);
			st = con.prepareCall(execSQL);
			st.registerOutParameter(1, Types.NUMERIC);
			st.setString(2, language);
			st.execute();
			result = st.getInt(1);
			st.close();
		} catch (SQLException e) {
			LOGGER.error("SQLException="+e.toString());
		} catch (Exception el) {
			LOGGER.error("Exception="+el.toString());
		} finally {
			try {
				if (st != null) {
					st.close();
				}
			} catch (SQLException w) {
				w.toString();
			}
			try {
				if (con != null) {
					con.close();
				}
			} catch (SQLException w) {
				w.toString();
			}
			Connector.closeConnection(con);
		} // finally
		//LOGGER.debug("bcBase.changeLanguage() END");

		this.setLanguage(language);
		if (result == 0) {
			return true;
		} else {
			return false;
		}
	}

	public menuElement currentMenu = new menuElement();

	public void setJspPage(String menuId) {
		//LOGGER.debug("bcBase.setJspPage()");
		currentMenu.setMenuElement(menuId, "");
		loginUser.setCurrentMenuId(menuId);
		//LOGGER.debug("bcBase.setJspPage() END");
	}

	public void setJspPageForTabName(String pTabNameMenuElement) {
		//LOGGER.debug("bcBase.setJspPage('" + pTabNameMenuElement + "')");
		currentMenu.setMenuElement("", pTabNameMenuElement);
		loginUser.setCurrentMenuId(currentMenu.getIdMenuElement());
		setLastIdMenuElement(currentMenu.getIdMenuElement());
	}

	public String setLastIdMenuElement(String id_menu) {
		CallableStatement st = null;
		Connection con = null;
		String execSQL = "";
		try {
			execSQL = "{call " + getGeneralDBScheme()
					+ ".PACK$USER_UI.set_id_menu_element_cur(?)}";
			//LOGGER.debug("bcBase.setLastIdMenuElement(" + id_menu + ")");
			con = Connector.getConnection(this.sessionId);
			st = con.prepareCall(execSQL);
			st.setString(1, id_menu);
			st.execute();
			st.close();
		} catch (SQLException e) {
			LOGGER.error("bcBase.setLastIdMenuElement(" + id_menu + ") SQLException: "
					+ e.toString());
		} catch (Exception el) {
			LOGGER.error("bcBase.setLastIdMenuElement(" + id_menu + ") Exception: "
					+ el.toString());
		} finally {
			try {
				if (st != null) {
					st.close();
				}
			} catch (SQLException w) {
				w.toString();
			}
			try {
				if (con != null) {
					con.close();
				}
			} catch (SQLException w) {
				w.toString();
			}
			Connector.closeConnection(con);
		} // finally
		//LOGGER.debug("bcBase.setLastIdMenuElement() END");
		return "";
	}

	public String getCheckScripts() {
		StringBuilder html = new StringBuilder();

		html.append("<script type=\"text/javascript\">\n");

		html.append("var root = window.addEventListener || window.attachEvent ? window : document.addEventListener ? document : null;\n");
		html.append("var cf_modified = false;\n");
		html.append("var WIN_CLOSE_MSG = \""
				+ commonXML.getfieldTransl(
						"msg_cancel_edit_form_confirm", false) + "\";\n");

		html.append("function set_modified(e){\n");
		html.append("  var el = window.event ? window.event.srcElement : e.currentTarget;\n");
		html.append("  el.className = \"inputfield_modified\";\n");
		html.append("  cf_modified = true;\n");
		html.append("}");

		html.append("function set_modified_element(myel){\n");
		html.append("	  myel.className = \"inputfield_modified\";\n");
		html.append("	  cf_modified = true;\n");
		html.append("	}\n");

		html.append("function ignore_modified(){\n");
		html.append("  if (typeof(root.onbeforeunload) != \"undefined\") root.onbeforeunload = null;\n");
		html.append("}\n");

		html.append("function check_cf(){\n");
		html.append("  if (cf_modified) return WIN_CLOSE_MSG;\n");
		html.append("}\n");

		html.append("function init(){\n");
		html.append("  if (typeof(root.onbeforeunload) != \"undefined\") root.onbeforeunload = check_cf;\n");
		html.append("  else return;\n");

		html.append("  for (var i = 0; oCurrForm = document.forms[i]; i++){\n");
		html.append("    for (var j = 0; oCurrFormElem = oCurrForm.elements[j]; j++){\n");
		html.append("      if (oCurrFormElem.getAttribute(\"cf\")){\n");
		html.append("        if (oCurrFormElem.addEventListener) oCurrFormElem.addEventListener(\"change\", set_modified, false);\n");
		html.append("        else if (oCurrFormElem.attachEvent) oCurrFormElem.attachEvent(\"onchange\", set_modified);\n");
		html.append("      }\n");
		html.append("    }\n");
		html.append("    if (oCurrForm.addEventListener) oCurrForm.addEventListener(\"submit\", ignore_modified, false);\n");
		html.append("    else if (oCurrForm.attachEvent) oCurrForm.attachEvent(\"onsubmit\", ignore_modified);\n");
		html.append("  }\n");
		html.append("}\n");

		html.append("if (root){\n");
		html.append("  if (root.addEventListener) root.addEventListener(\"load\", init, false);\n");
		html.append("  else if (root.attachEvent) root.attachEvent(\"onload\", init);\n");
		html.append("}\n");
		html.append("</script>\n");

		return html.toString();
	}

	public String getCheckUserLineFull(HttpServletRequest request) {
		String return_value = this.getFullContext(request);
		if ("crm".equalsIgnoreCase(moduleName)) {
			return_value = return_value
					+ "/crm/main.jsp?error=checkUser_bad_login&lang="
					+ getLanguage();
		} else if ("crmportal".equalsIgnoreCase(moduleName)) {
			return_value = return_value
					+ "/crm/mainportal.jsp?error=checkUser_bad_login&lang="
					+ getLanguage();
		} else if ("webclient".equalsIgnoreCase(moduleName)) {
			return_value = return_value
					+ "/webclient/main.jsp?error=checkUser_bad_login&lang="
					+ getLanguage();
		} else if ("webclientportal".equalsIgnoreCase(moduleName)) {
			return_value = return_value
					+ "/webclient/mainportal.jsp?error=checkUser_bad_login&lang="
					+ getLanguage();
		} else if ("webpos".equalsIgnoreCase(moduleName)) {
			return_value = return_value
					+ "/webpos/main.jsp?error=checkUser_bad_login&lang="
					+ getLanguage();
		} else if ("webposportal".equalsIgnoreCase(moduleName)) {
			return_value = return_value
					+ "/webpos/mainportal.jsp?error=checkUser_bad_login&lang="
					+ getLanguage();
		} else if ("terminal".equalsIgnoreCase(moduleName)) {
			return_value = return_value
					+ "/terminal.jsp?error=checkUser_bad_login&lang="
					+ getLanguage();
		}
		LOGGER.debug("getCheckUserLineFull() return: "+return_value);
		return return_value;

	}

	public String getValue(String pValue) { // TEMPORARY FOR DEMO!!
		String result = isEmpty(pValue)?"":pValue;
		
		result = result.replaceAll("&", "&amp;").replaceAll("\"", "&quot;")
				.replaceAll("<<", "&laquo;").replaceAll(">>", "&raquo;");
		result = result.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
		return result;
	}

	public String getValue2(String pValue) { // TEMPORARY FOR DEMO!!
		String result;
		if (isEmpty(pValue) || "null".equalsIgnoreCase(pValue)) {
			result = "&nbsp;";
		} else {
			result = pValue;
		}
		return result;
	} 

	public String getValue3(String pValue){
    	String result;
    	if (isEmpty(pValue) || "null".equalsIgnoreCase(pValue)) {
    		result = "&nbsp;";
    	} else {
    		result = pValue;    		
    	}
    	return result;
    } // end of getValue3

	public boolean hasEditTabsheetPermission(String pTabName) {
		PreparedStatement st = null;
		Connection con = null;
		String mySQL = " SELECT id_privilege_type " + "   FROM "
				+ getGeneralDBScheme() + ".vc_user_menu_tabsheet_all "
				+ "  WHERE UPPER(tabname_menu_element)=UPPER(?) "
				+ "    AND is_enable = 'Y' and is_visible = 'Y'";
		boolean result = false;
		try {
			//LOGGER.debug(mySQL);
			//LOGGER.debug("bcBase.hasEditTabsheetPermission('" + pTabName + "')");
			con = Connector.getConnection(this.sessionId);

			st = con.prepareStatement(mySQL);
			st.setString(1, pTabName);
			ResultSet rset = st.executeQuery();

			while (rset.next()) {
				if (rset.getString(1).equalsIgnoreCase("2")) {
					result = true;
				}
			}
		} // try
		catch (SQLException e) {
			LOGGER.debug("bcBase.hasEditTabsheetPermission('" + pTabName + "') SQLException: "
					+ e.toString());
		} catch (Exception el) {
			LOGGER.debug("bcBase.hasEditTabsheetPermission('" + pTabName + "') Exception: "
					+ el.toString());
		} finally {
			try {
				if (st != null) {
					st.close();
				}
			} catch (SQLException w) {
				w.toString();
			}
			try {
				if (con != null) {
					con.close();
				}
			} catch (SQLException w) {
				w.toString();
			}
			Connector.closeConnection(con);
		} // finally
		//LOGGER.debug("bcBase.hasEditTabsheetPermission('" + pTabName
		//		+ "') END, result=" + result);
		return result;
	}

	public boolean hasAccessTabsheetPermission(String pTabName) {
		PreparedStatement st = null;
		Connection con = null;
		String mySQL = " SELECT id_privilege_type " + "   FROM "
				+ getGeneralDBScheme() + ".vc_user_menu_tabsheet_all "
				+ "  WHERE UPPER(tabname_menu_element)=UPPER(?)"
				+ "    AND is_enable = 'Y' and is_visible = 'Y'";
		boolean result = false;
		try {
			//LOGGER.debug(mySQL);
			//LOGGER.debug("bcBase.hasAccessTabsheetPermission('" + pTabName
			//		+ "')");
			con = Connector.getConnection(this.sessionId);

			st = con.prepareStatement(mySQL);
			st.setString(1, pTabName);
			ResultSet rset = st.executeQuery();

			while (rset.next()) {
				if (rset.getString(1).equalsIgnoreCase("1")
						|| rset.getString(1).equalsIgnoreCase("2")) {
					result = true;
				}
			}
		} // try
		catch (SQLException e) {
			LOGGER.debug("bcBase.hasAccessTabsheetPermission('" + pTabName + "') SQLException: "
					+ e.toString());
		} catch (Exception el) {
			LOGGER.debug("bcBase.hasAccessTabsheetPermission('" + pTabName + "') Exception: "
					+ el.toString());
		} finally {
			try {
				if (st != null) {
					st.close();
				}
			} catch (SQLException w) {
				w.toString();
			}
			try {
				if (con != null) {
					con.close();
				}
			} catch (SQLException w) {
				w.toString();
			}
			Connector.closeConnection(con);
		} // finally
		//LOGGER.debug("bcBase.hasAccessTabsheetPermission('" + pTabName
		//		+ "') END, result=" + result);
		return result;
	}


	public boolean hasEditMenuPermission(String pTabNameMenu) {

		boolean result = false;

		if (currentMenu.getTabNameMenuElement().equalsIgnoreCase(pTabNameMenu)) {

			//LOGGER.debug("bcBase.hasEditMenuPermission('" + pTabNameMenu
			//		+ "'), current menu");

			String idPrivilege = currentMenu.getIdPrivilegeType();
			if (idPrivilege.equalsIgnoreCase("2")) {
				result = true;
			} else {
				result = false;
			}
			//LOGGER.debug("bcBase.hasEditMenuPermission('" + pTabNameMenu
			//		+ "') END, result=" + result);
			return result;
		}

		Connection con = null;
		PreparedStatement st = null;
		final String mySQL = " SELECT id_privilege_type " + "   FROM "
				+ getGeneralDBScheme() + ".vc_user_menu_all "
				+ "  WHERE UPPER(tabname_menu_element) = UPPER(?)"
				+ "    AND is_enable = 'Y' and is_visible = 'Y'";
		try {
			//LOGGER.debug(mySQL);
			con = Connector.getConnection(this.sessionId);

			st = con.prepareStatement(mySQL);
			st.setString(1, pTabNameMenu);
			ResultSet rset = st.executeQuery();

			while (rset.next()) {
				if (rset.getString(1).equalsIgnoreCase("2")) {
					result = true;
				}
			}
		} // try
		catch (SQLException e) {
			LOGGER.debug("bcBase.hasEditMenuPermission('" + pTabNameMenu + "') SQLException: "
					+ e.toString());
		} catch (Exception el) {
			LOGGER.debug("bcBase.hasEditMenuPermission('" + pTabNameMenu + "') Exception: "
					+ el.toString());
		} finally {
			try {
				if (st != null) {
					st.close();
				}
			} catch (SQLException w) {
				w.toString();
			}
			try {
				if (con != null) {
					con.close();
				}
			} catch (SQLException w) {
				w.toString();
			}
			Connector.closeConnection(con);
		} // finally
		//LOGGER.debug("bcBase.hasEditMenuPermission('" + pTabNameMenu
		//		+ "') END, result=" + result);
		return result;
	}

	public boolean hasAccessMenuPermission(String pTabNameMenu) {

		boolean result = false;

		if (currentMenu.getTabNameMenuElement().equalsIgnoreCase(pTabNameMenu)) {

			//LOGGER.debug("bcBase.hasAccessMenuPermission('" + pTabNameMenu
			//		+ "'), current menu");

			String idPrivilege = currentMenu.getIdPrivilegeType();
			if (idPrivilege.equalsIgnoreCase("1")
					|| idPrivilege.equalsIgnoreCase("2")) {
				result = true;
			} else {
				result = false;
			}
			//LOGGER.debug("bcBase.hasAccessMenuPermission('" + pTabNameMenu
			//		+ "') END, result=" + result);
			return result;
		}

		Connection con = null;
		PreparedStatement st = null;
		final String mySQL = " SELECT id_privilege_type " + "   FROM "
				+ getGeneralDBScheme() + ".vc_user_menu_all "
				+ "  WHERE UPPER(tabname_menu_element) = UPPER(?)"
				+ "    AND is_enable = 'Y' and is_visible = 'Y'";
		try {
			//LOGGER.debug(mySQL);
			con = Connector.getConnection(this.sessionId);

			st = con.prepareStatement(mySQL);
			st.setString(1, pTabNameMenu);
			ResultSet rset = st.executeQuery();

			while (rset.next()) {
				if (rset.getString(1).equalsIgnoreCase("1")
						|| rset.getString(1).equalsIgnoreCase("2")) {
					result = true;
				}
			}
		} // try
		catch (SQLException e) {
			LOGGER.debug("bcBase.hasAccessMenuPermission('" + pTabNameMenu + "') SQLException: "
					+ e.toString());
		} catch (Exception el) {
			LOGGER.debug("bcBase.hasAccessMenuPermission('" + pTabNameMenu + "') Exception: "
					+ el.toString());
		} finally {
			try {
				if (st != null) {
					st.close();
				}
			} catch (SQLException w) {
				w.toString();
			}
			try {
				if (con != null) {
					con.close();
				}
			} catch (SQLException w) {
				w.toString();
			}
			Connector.closeConnection(con);
		} // finally
		//LOGGER.debug("bcBase.hasAccessMenuPermission('" + pTabNameMenu
		//		+ "') END, result=" + result);
		return result;
	}

	private static boolean reExecutionRequired(SQLException e) {
		return "72000".equals(e.getSQLState()) && e.getErrorCode() == 4068;
	}

	private static void executePkg(Connection conn, CallableStatement cstmt)
			throws Exception {
		try {
			cstmt.execute();
			conn.commit();
		} catch (SQLException e) {
			throw e;
		}

	}

	private void writeFunctionResultToDataBase(String pSQL, String pResult) {
		String callSQL = "{? = call " + this.getGeneralDBScheme()
				+ ".FNC_UI_DEBUG(?,?,?,?,?)}";
		// String callSQL = "{? = call " + this.getGeneralDBScheme() +
		// ".FNC_UI_DEBUG1('','','',?)}";
		String[] results = new String[2];
		CallableStatement cs = null;
		Connection con = null;
		try {
			results[0] = C_ERROR_RESULT;
			con = Connector.getConnection(this.sessionId);

			cs = con.prepareCall(callSQL);
			cs.registerOutParameter(1, Types.VARCHAR);
			cs.setString(2, this.getLoginUserId());
			cs.setString(3, this.currentMenu.getCurrentTabIdMenuElement());
			cs.setString(4, pSQL);
			cs.setString(5, pResult);
			cs.registerOutParameter(6, Types.VARCHAR);
			executePkg(con, cs);
			results[0] = cs.getString(1);
			results[1] = cs.getString(6);
			// this.test="checkUser_ok"+result;
			cs.close();
			/*
			 * results[0] = "1"; con = Connector.getConnection(this.sessionId);
			 * 
			 * cs = con.prepareCall(callSQL); LOGGER.debug(callSQL); try {
			 * cs.registerOutParameter(0, Types.VARCHAR); } catch (Exception e)
			 * { LOGGER.debug("1 = " + e.toString()); throw e; } try {
			 * cs.setString(1, this.currentMenu.getCurrentTabIdMenuElement()); }
			 * catch (Exception e) { LOGGER.debug("2 = " + e.toString());
			 * throw e; } try { cs.setString(2, pSQL); } catch (Exception e) {
			 * LOGGER.debug("3 = " + e.toString()); throw e; } try {
			 * cs.setString(3, pResult); } catch (Exception e) {
			 * LOGGER.debug("4 = " + e.toString()); throw e; } try {
			 * cs.registerOutParameter(4, Types.VARCHAR); } catch (Exception e)
			 * { LOGGER.debug("5 = " + e.toString()); throw e; } try {
			 * executePkg(con, cs); } catch (Exception e) {
			 * LOGGER.debug("6 = " + e.toString()); throw e; } try {
			 * results[0] = cs.getString(1); } catch (Exception e) {
			 * LOGGER.debug("7 = " + e.toString()); throw e; } try {
			 * results[1] = cs.getString(2); } catch (Exception e) {
			 * LOGGER.debug("8 = " + e.toString()); throw e; }
			 * //this.test="checkUser_ok"+result; cs.close();
			 */
		} catch (SQLException e) {
			if (reExecutionRequired(e)) {
				LOGGER.debug("bcBase.writeFunctionResultToDataBase() ORA-04068 detected - re-executing the package...");
				try {
					executePkg(con, cs);
					results[0] = cs.getString(1);
					results[1] = cs.getString(2);
				} catch (Exception e2) {
					results[0] = C_ERROR_RESULT;
					results[1] = e.toString();
					LOGGER.debug("bcBase.writeFunctionResultToDataBase() executePkg: "
							+ e.toString());
				}
			} else {
				results[0] = C_SQL_EXCEPTION;
				results[1] = e.toString();
				LOGGER.debug("bcBase.writeFunctionResultToDataBase() SQLException: "
						+ e.toString());
			}
		} catch (Exception el) {
			results[0] = C_ERROR_RESULT;
			results[1] = el.toString();
			LOGGER.debug("bcBase.writeFunctionResultToDataBase() Exception: "
					+ el.toString());
		} finally {
			try {
				if (cs != null) {
					cs.close();
				}
			} catch (SQLException w) {
				w.toString();
			}
			try {
				if (con != null) {
					con.close();
				}
			} catch (SQLException w) {
				w.toString();
			}
			Connector.closeConnection(con);
		} // finally

	}

	public String prepareSQLToLog2(String pSql, ArrayList<String> pParam) {
		String[] pParam2 = new String[pParam.size()];
		for(int counter=0; counter<=pParam.size()-1;counter++){
			pParam2[counter] = pParam.get(counter);
			//System.out.println("pParam.get("+counter+")="+pParam.get(counter));
		}
		return prepareSQLToLog(pSql, pParam2, false);
	}
	
	public String prepareSQLToLog(String pSql, String[] pParam) {
		return prepareSQLToLog(pSql, pParam, false);
	}

	public String prepareSQLToLog(String pSql, String[] pParam,
			boolean hasNewLine) {
		String result = pSql;
		int pParamLength = 0;
		if (!(pParam == null)) {
			pParamLength = pParam.length;
		}
		for (int counter = 2; counter < pParamLength + 2; counter++) {
			if (hasNewLine) {
				result = result + ",\n[" + counter + "]='" + pParam[counter - 2]
						+ "'";
			} else {
				result = result + ", [" + counter + "]='" + pParam[counter - 2]
						+ "'";
			}
		}
		return result;
	}
    
	public String prepareSQLToLog(String pSQL, ArrayList<bcFeautureParam> pParam) {
		String pOutputMessage = pSQL;

        for(int counter=1; counter<pParam.size()+1;counter++){
        	if ("string".equalsIgnoreCase(pParam.get(counter-1).getDataType())) {
        		pOutputMessage = pOutputMessage + ", " + counter + "={'" + pParam.get(counter-1).getValue() + "', " + pParam.get(counter-1).getDataType() + "}";
        	} else {
        		pOutputMessage = pOutputMessage + ", " + counter + "={" + pParam.get(counter-1).getValue() + ", " + pParam.get(counter-1).getDataType() + "}";
        	}
        }
        return pOutputMessage;
	}
	
	
	public String prepareSQLToLog(String pSQL, String[] pParam, String[] pResults) {
		StringBuilder pOutputMessage = new StringBuilder();
		String lResult = "";

		pOutputMessage.append(prepareSQLToLog(pSQL, pParam, false));
		pOutputMessage.append(", result[0]='" + pResults[0] + "'");
        /*for(int counter=1; counter<pParam.length+1;counter++){
        	pOutputMessage = pOutputMessage + ", " + counter + "={'" + pParam[counter-1] + "'}";
        }*/
		if (pResults.length > 1) {
			pOutputMessage.append(", outputs: ");
		}
        for(int counter=2; counter<pResults.length+1;counter++){
        	if (isEmpty(pResults[counter-1])) {
        		lResult = "";
        	} else {
        		lResult = pResults[counter-1];
        	}
        	pOutputMessage.append("[" + (pParam.length + counter-1) + "]='" + lResult + "', ");
        }
        return pOutputMessage.toString();
	}

	public String[] executeFunction(String pFunctionName, ArrayList<String> pParam,
			int pResultRowCount) {
		
		StringBuilder mySQL = new StringBuilder();
		
		mySQL.append("{? = call " + getGeneralDBScheme() + "."+ pFunctionName + "(");
		for(int counter=0; counter<=pParam.size()-1;counter++){
			mySQL.append("?,");
		}
		for(int counter=2; counter<=pResultRowCount;counter++){
			if (counter == pResultRowCount) {
				mySQL.append("?");
			} else {
				mySQL.append("?,");
			}
		}
		mySQL.append(")}");
		//LOGGER.debug(mySQL.toString());
		String[] pParam2 = new String[pParam.size()];
		for(int counter=0; counter<=pParam.size()-1;counter++){
			pParam2[counter] = pParam.get(counter);
		}
		
		return myCallFunctionParam(null, mySQL.toString(), pParam2, pResultRowCount);
	}

	public String[] executeAdminFunction(String pFunctionType, ArrayList<String> pParam,
			int pResultRowCount) {
		
		StringBuilder mySQL = new StringBuilder();
		Connection con = null;
		String lFunctionName = "";
		
		if ("RESTORE_SMS".equalsIgnoreCase(pFunctionType)) {
			lFunctionName = "PACK$WEBPOS_UI.restore_password_sms";
		} else if ("RESTORE_CONFIRM".equalsIgnoreCase(pFunctionType)) {
			lFunctionName = "PACK$WEBPOS_UI.restore_password_confirm";
		} else if ("RESTORE".equalsIgnoreCase(pFunctionType)) {
			lFunctionName = "PACK$WEBPOS_UI.restore_password";
		} else {
			lFunctionName = "";
		}
		
		mySQL.append("{? = call " + lFunctionName + "(");
		for(int counter=0; counter<=pParam.size()-1;counter++){
			mySQL.append("?,");
		}
		for(int counter=2; counter<=pResultRowCount;counter++){
			if (counter == pResultRowCount) {
				mySQL.append("?");
			} else {
				mySQL.append("?,");
			}
		}
		mySQL.append(")}");
		LOGGER.debug(mySQL.toString());
		String[] pParam2 = new String[pParam.size()];
		for(int counter=0; counter<=pParam.size()-1;counter++){
			pParam2[counter] = pParam.get(counter);
		}
		con = Connector.getAdminConnection(this.language);
		
		return myCallFunctionParam(con, mySQL.toString(), pParam2, pResultRowCount);
	}

	public String[] myCallFunctionParam(String pSql, ArrayList<String> pParam,
			int pResultRowCount) {
		String[] pParam2 = new String[pParam.size()];
		for(int counter=0; counter<=pParam.size()-1;counter++){
			pParam2[counter] = pParam.get(counter);
		}
		
		return myCallFunctionParam(null, pSql, pParam2, pResultRowCount);
	}

	public String[] myCallFunctionParam(String pSql, String[] pParam,
			int pResultRowCount) {
		return myCallFunctionParam(null, pSql, pParam, pResultRowCount);
	}

	private String myCallFunctionSQl = "";
	public String getCallFunctionSQL () {
		return myCallFunctionSQl;
	}
	
	public String[] myCallFunctionParam(Connection pCon, String pSql,
			String[] pParam, int pResultRowCount) {
		String[] results = new String[pResultRowCount];
		CallableStatement cs = null;
		Connection con = null;
		int pParamLength = 0;

		try {
			LOGGER.debug(prepareSQLToLog(pSql, pParam));

			if (!(pParam == null)) {
				pParamLength = pParam.length;
			}

			results[0] = C_ERROR_RESULT;
			if (pCon == null || pCon.isClosed()) {
				con = Connector.getConnection(this.sessionId);
			} else {
				con = pCon;
			}
			
			myCallFunctionSQl = pSql;

			cs = con.prepareCall(pSql);
			cs.registerOutParameter(1, Types.VARCHAR);
			// LOGGER.debug("pResultRowCount=" + pResultRowCount);

			for (int counter = 2; counter < pParamLength + 2; counter++) {
				cs.setString(counter, pParam[counter - 2]);
				// LOGGER.debug("param["+counter+"]=" +
				// pParam[counter-2]);
			}
			if (pResultRowCount >= 2) {
				for (int counter = 2; counter < pResultRowCount; counter++) {
					cs.registerOutParameter(pParamLength + counter,
							Types.VARCHAR);
				}
				cs.registerOutParameter(pParamLength + pResultRowCount,
						Types.VARCHAR);
			}
			/*
			 * if (pResultRowCount==2) { cs.registerOutParameter(pParamLength +
			 * 2, Types.VARCHAR); } else if (pResultRowCount==3) {
			 * cs.registerOutParameter(pParamLength + 2, Types.NUMERIC);
			 * cs.registerOutParameter(pParamLength + 3, Types.VARCHAR); } else
			 * if (pResultRowCount==4) { cs.registerOutParameter(pParamLength +
			 * 2, Types.NUMERIC); cs.registerOutParameter(pParamLength + 3,
			 * Types.NUMERIC); cs.registerOutParameter(pParamLength + 4,
			 * Types.VARCHAR); }
			 */
			executePkg(con, cs);
			// LOGGER.debug("Executed...");

			results[0] = cs.getString(1);
			if (pResultRowCount >= 2) {
				for (int counter = 2; counter < pResultRowCount; counter++) {
					results[counter - 1] = cs.getString(pParamLength + counter);
					// LOGGER.debug("counter=" + (pParamLength +
					// counter));
				}
				// LOGGER.debug("counter=" + (pParamLength +
				// pResultRowCount));
				results[pResultRowCount - 1] = cs.getString(pParamLength
						+ pResultRowCount);
			}
			LOGGER.debug(prepareSQLToLog(pSql, pParam, results));

			/*
			 * if (pResultRowCount==2) { results[0] = cs.getString(1);
			 * results[1] = cs.getString(pParamLength + 2); } else if
			 * (pResultRowCount==3) { results[0] = cs.getString(1); results[1] =
			 * cs.getString(pParamLength + 3); results[2] =
			 * cs.getString(pParamLength + 2); } else if (pResultRowCount==4) {
			 * results[0] = cs.getString(1); results[1] =
			 * cs.getString(pParamLength + 4); results[2] =
			 * cs.getString(pParamLength + 2); results[3] =
			 * cs.getString(pParamLength + 3); }
			 */

			cs.close();
		} catch (SQLException e) {
			if (reExecutionRequired(e)) {
				LOGGER.debug("bcBase.myCallFunctionParam() ORA-04068 detected - re-executing the package...");
				try {
					executePkg(con, cs);
					results[0] = cs.getString(1);
					if (pResultRowCount >= 2) {
						for (int counter = 2; counter < pResultRowCount; counter++) {
							results[counter - 1] = cs.getString(pParamLength
									+ counter);
							// LOGGER.debug("counter=" + (pParamLength +
							// counter));
						}
						// LOGGER.debug("counter=" + (pParamLength +
						// pResultRowCount));
						results[pResultRowCount - 1] = cs
								.getString(pParamLength + pResultRowCount);
					}
					LOGGER.debug(prepareSQLToLog(pSql, pParam, results));
					/*
					 * if (pResultRowCount==2) { results[0] = cs.getString(1);
					 * results[1] = cs.getString(pParamLength + 2); } else if
					 * (pResultRowCount==3) { results[0] = cs.getString(1);
					 * results[1] = cs.getString(pParamLength + 3); results[2] =
					 * cs.getString(pParamLength + 2); } else if
					 * (pResultRowCount==4) { results[0] = cs.getString(1);
					 * results[1] = cs.getString(pParamLength + 4); results[2] =
					 * cs.getString(pParamLength + 2); results[3] =
					 * cs.getString(pParamLength + 3); }
					 */
				} catch (Exception e2) {
					LOGGER.debug(prepareSQLToLog(pSql, pParam));
					results[0] = C_ERROR_RESULT;
					results[pResultRowCount - 1] = e.toString();
					LOGGER.error("bcBase.myCallFunctionParam() executePkg: "
							+ e.toString());
				}
			} else {
				LOGGER.debug(prepareSQLToLog(pSql, pParam));
				results[0] = C_SQL_EXCEPTION;
				results[pResultRowCount - 1] = e.toString();
				LOGGER.debug("bcBase.myCallFunctionParam() SQLException: "
						+ e.toString());
			}
		} catch (Exception el) {
			LOGGER.debug(prepareSQLToLog(pSql, pParam));
			results[0] = C_ERROR_RESULT;
			results[pResultRowCount - 1] = el.toString();
			LOGGER.debug("bcBase.myCallFunctionParam() Exception: "
					+ el.toString());
		} finally {
			try {
				if (cs != null) {
					cs.close();
				}
			} catch (SQLException w) {
				w.toString();
			}
			try {
				if (con != null) {
					con.close();
				}
			} catch (SQLException w) {
				w.toString();
			}
			Connector.closeConnection(con);
		} // finally

		writeFunctionResultToDataBase(prepareSQLToLog(pSql, pParam, results),
				results[0]);
		return results;
	}


	public String prepareStringToSQL(String input_string) {
		// LOGGER.debug("input_string = " + input_string);
		if (isEmpty(input_string) || "null".equalsIgnoreCase(input_string)) {
			return "";
		} else {
			input_string = input_string.replaceAll("_NR_", "\\\n");
			input_string = input_string.replaceAll("'", "''");
			return input_string;
		}
	}

	public String getCalendarJS() {

		StringBuilder html = new StringBuilder();
		html.append("<!-- для календаря -->\n");
		html.append("<link rel=\"stylesheet\" type=\"text/css\" href=\"../CSS/calendar-blue.css\">\n");
		html.append("<script type=\"text/javascript\" src=\"../js/calendar-emix.js\"></script>\n");
		html.append("<script type=\"text/javascript\" src=\"../js/calendar-" + this.getLanguage()
				+ ".js\"></script>\n");
		html.append("<script type=\"text/javascript\" src=\"../js/calendar-setup.js\"></script>\n");

		return html.toString();
	}

	public String getFormValidateJS() {

		StringBuilder html = new StringBuilder();
		html.append("<script type=\"text/javascript\" src=\""
				+ "../js/formValidate-"
				+ this.getLanguage() + ".js\"></script>\n");

		return html.toString();
	}

	public String getResponseUtilityJS() {

		StringBuilder html = new StringBuilder();
		html.append("<script type=\"text/javascript\" language=\"javascript\" src=\""
				+ "../dwr/interface/responseUtility.js\"></script>\n");
		html.append("<script type=\"text/javascript\" language=\"javascript\" src=\""
				+ "../dwr/interface/ReporterUtility.js\"></script>\n");
		html.append("<script type=\"text/javascript\" language=\"javascript\" src=\""
				+ "../dwr/engine.js\"></script>\n");

		return html.toString();
	}

	public String getGoBackScript() {

		StringBuilder html = new StringBuilder();
		html.append("<script language=\"JavaScript\">\n");
		html.append("	var indx_location;\n");
		html.append("	function goBack() { history.back(); }\n");
		html.append("</script>\n");

		return html.toString();
	}

	public String getMetaContent() {

		StringBuilder html = new StringBuilder();
		html.append("<meta content=\"text/html;charset=UTF-8\" http-equiv=\"Content-Type\">\n");

		return html.toString();
	}

	public String getTableSorterCSS(String pSortList) {

		StringBuilder html = new StringBuilder();
		html.append("<link rel=\"stylesheet\" type=\"text/css\" href=\""
				+ "../CSS/tablesorter.css\" media=\"screen\" title=\"Flora (Default)\">\n");
		html.append("<script type=\"text/javascript\" src=\""
				+ "../js/headeractiv.js\"> </script>\n");
		html.append("<script type=\"text/javascript\" src=\""
				+ "../js/jquery.js\"></script>\n");
		html.append("<script type=\"text/javascript\" src=\""
				+ "../js/jquery.tablesorter.js\"></script>\n");
		/*
		 * html.append("<script>");
		 * html.append("	$(document).ready(function(){");
		 * html.append("		$(\"#id_table\").tablesorter({sortList:["
		 * +pSortList+"], widgets: ['zebra']});\n"); html.append("	});");
		 * html.append("</script>");
		 */
		return html.toString();
	}

	public String getObligatoryText() {

		return "<font color=\"red\">*</font>";
	}

	public String setObligatoryText(String pTitle) {

		return "<font color=\"red\">" + pTitle + "</font>";
	}

	public String getGoToHyperLink(String pMenu, String pId, String pHyperLink,
			String pMessage) {

		StringBuilder html = new StringBuilder();

		boolean hasPermission = false;
		if (isEmpty(pMenu)) {
			hasPermission = true;
		} else if (this.hasAccessMenuPermission(pMenu)) {
			hasPermission = true;
		}

		if (hasPermission) {
			if (!isEmpty(pId)) {
				html.append("<span class=\"div_goto\" onclick=\"ajaxpage('"
						+ pHyperLink + "', 'div_main')\">&nbsp;(" + pMessage
						+ ")</span>");
			}
		}

		return html.toString();
	}

	public String getGoToHyperLink(String pMenu, String pId, String pHyperLink) {

		return getGoToHyperLink(pMenu, pId, pHyperLink,
				this.form_messageXML.getfieldTransl("go_to", false));

	}

	public String getGoToTabMenuHyperLink(String pTabName, String pId,
			String pHyperLink, String pMessage) {

		StringBuilder html = new StringBuilder();

		boolean hasPermission = false;
		if (isEmpty(pTabName)) {
			hasPermission = true;
		} else if (this.hasAccessTabsheetPermission(pTabName)) {
			hasPermission = true;
		}

		if (hasPermission) {
			if (!isEmpty(pId)) {
				html.append("<span class=\"div_goto\" onclick=\"ajaxpage('"
						+ pHyperLink + "', 'div_main')\">&nbsp;(" + pMessage
						+ ")</span>");
			}
		}

		return html.toString();
	}

	public String getGoToTabMenuHyperLink(String pTabName, String pId,
			String pHyperLink) {

		return getGoToTabMenuHyperLink(pTabName, pId, pHyperLink,
				this.form_messageXML.getfieldTransl("go_to", false));

	}

	public String getCheckBoxBase(String pName, String pValue, String pLabelCaption, String pLabelStyle, String pOnChangeScript, boolean isNumberValue, boolean pDisabled) {
		StringBuilder checkbox = new StringBuilder();
		boolean isChecked = false;
		String isCheckedTitle = "";
		if ("Y".equalsIgnoreCase(pValue) || "1".equalsIgnoreCase(pValue)) {
			isChecked = true;
			isCheckedTitle = " CHECKED ";
		}
		checkbox.append("<input type=\"checkbox\" style=\"height: inherit;padding:0;margin-top:4px;\" name=\"" + pName
				+ "\" id=\"" + pName + "\" value=\""+(isNumberValue?"1":"Y")+"\" "
				+ isCheckedTitle + (pDisabled?" DISABLED ":"") + (isEmpty(pOnChangeScript)?"":" onchange=\"" + pOnChangeScript + "\" ") + ">");
		checkbox.append(((pDisabled)?"<span class=\"checbox_label_disable "+(isChecked?"checbox_label_checked":"")+" \" " + (!isEmpty(pLabelStyle)?" style=\""+ pLabelStyle+ "\"":"") + ">" + pLabelCaption + "</span>":"<label id=\"" + pName + "_label\" class=\"checbox_label "+(isChecked?"checbox_label_checked":"")+" \" " + (!isEmpty(pLabelStyle)?" style=\""+ pLabelStyle+ "\"":"") + " for=\""	+ pName + "\">" + pLabelCaption + "</label>"));
		return checkbox.toString();
	}

	public String getCheckBoxBase(String pName, String pValue, String pLabelCaption, String pLabelStyle, boolean isNumberValue, boolean pDisabled) {
		return getCheckBoxBase(pName, pValue, pLabelCaption, pLabelStyle, "", isNumberValue, pDisabled);
	}

	public String getCheckBoxBase(String pName, String pValue, String pLabelCaption, boolean isNumberValue, boolean pDisabled) {
		return getCheckBoxBase(pName, pValue, pLabelCaption, "", "", isNumberValue, pDisabled);
	}

	public String getCheckBoxNumber(String pName, String pValue, String pLabelCaption) {
		return getCheckBoxBase(pName, pValue, pLabelCaption, true, false);
	}

	public String getCheckBoxNumberDisabled(String pName, String pValue, String pLabelCaption) {
		return getCheckBoxBase(pName, pValue, pLabelCaption, true, true);
	}

	public String getCheckBox(String pName, String pValue, String pLabelCaption) {
		return getCheckBoxBase(pName, pValue, pLabelCaption, false, false);
	}

	public String getCheckBoxDisabled(String pName, String pValue, String pLabelCaption) {
		return getCheckBoxBase(pName, pValue, pLabelCaption, false, true);
	}
	
	// ========================================================================
	// Функции возвращают поля <OPTION> для экранных элементов <SELECT>
	// ========================================================================

	public int selectOprionCount = 0;
	
	public int getSelectOptionCount () {
		return selectOprionCount;
	}

	public String getSelectBodyFromQuery2(String query, String selected_value,
			boolean isNull) {
		ArrayList<bcFeautureParam> pParam = initParamArray();
		pParam.add(new bcFeautureParam("none", ""));
		ArrayList<bcDictionaryRecord> pDict = new ArrayList<bcDictionaryRecord>();

		return getSelectBodyFromParamQuery(query, pParam, selected_value, null,
				isNull, pDict, false, true);
	}

	public String getSelectBodyFromQuery2(String query, String selected_value,
			boolean isNull, boolean existsOnly) {
		ArrayList<bcFeautureParam> pParam = initParamArray();
		pParam.add(new bcFeautureParam("none", ""));
		ArrayList<bcDictionaryRecord> pDict = new ArrayList<bcDictionaryRecord>();

		return getSelectBodyFromParamQuery(query, pParam, selected_value, null,
				isNull, pDict, existsOnly, true);
	}

	public String getSelectBodyFromQuery(String query, String selected_value,
			boolean isNull, ArrayList<bcDictionaryRecord> pDict,
			boolean existsOnly) {
		ArrayList<bcFeautureParam> pParam = initParamArray();
		pParam.add(new bcFeautureParam("none", ""));

		return getSelectBodyFromParamQuery(query, pParam, selected_value, null,
				isNull, pDict, existsOnly, true);
	}

	public String getSelectBodyFromSizedQuery(String query,
			String selected_value, String pSize, boolean isNull,
			ArrayList<bcDictionaryRecord> pDict, boolean existsOnly) {
		ArrayList<bcFeautureParam> pParam = initParamArray();
		pParam.add(new bcFeautureParam("none", ""));

		return getSelectBodyFromParamQuery(query, pParam, selected_value,
				pSize, isNull, pDict, existsOnly, true);
	}

	public String getSelectBodyFromSizedQuery(String query,
			String selected_value, String pSize, boolean isNull,
			boolean existsOnly) {
		ArrayList<bcFeautureParam> pParam = initParamArray();
		pParam.add(new bcFeautureParam("none", ""));
		ArrayList<bcDictionaryRecord> pDict = new ArrayList<bcDictionaryRecord>();

		return getSelectBodyFromParamQuery(query, pParam, selected_value,
				pSize, isNull, pDict, existsOnly, true);
	}

	public String getSelectBodyFromParamQueryShort(String query,
			ArrayList<bcFeautureParam> pParam, String selected_value,
			String pSize, boolean isNull) {
		ArrayList<bcDictionaryRecord> pDict = new ArrayList<bcDictionaryRecord>();

		return getSelectBodyFromParamQuery(query, pParam, selected_value,
				pSize, isNull, pDict, false, true);
	}

	public String getSelectBodyFromParamQuery(String query,
			ArrayList<bcFeautureParam> pParam, String selected_value,
			String pSize, boolean isNull, ArrayList<bcDictionaryRecord> pDict,
			boolean existsOnly, boolean styledValue) {
		StringBuilder return_value = new StringBuilder();
		PreparedStatement st = null;
		Connection con = null;
		String selectedTxt = "";
		String styleText = "";
		boolean optionExist = true;
		this.selectOprionCount = 0;

		if (pDict.size() > 0) {
			return getSelectBodyFromSavedArray2(pDict, selected_value, pSize,
					isNull, existsOnly);
		}

		try {
			LOGGER.debug(prepareSQLToLog(query, pParam));
			con = Connector.getConnection(this.sessionId);

			st = con.prepareStatement(query);
			st = prepareParam(st, pParam);
			ResultSet rset = st.executeQuery();

			ResultSetMetaData mtd = rset.getMetaData();
			int colCount = mtd.getColumnCount();
			// return_value.append("\n");
			if (isNull) {
				return_value.append("<option value=\"\" title=\"\"></option>");
			}

			while (rset.next()) {
				String code = rset.getString(1);
				String value = rset.getString(2);
				String existFlag = "";
				String optionStyle = "";
				if (colCount > 2) {
					for (int i = 3; i <= colCount; i++) {
						if ("EXIST_FLAG".equalsIgnoreCase(mtd.getColumnName(i))) {
							existFlag = rset.getString("EXIST_FLAG");
						} else if ("OPTION_STYLE".equalsIgnoreCase(mtd
								.getColumnName(i))) {
							optionStyle = rset.getString("OPTION_STYLE");
						}
					}
				}
				pDict.add(new bcDictionaryRecord(code, value, existFlag,
						optionStyle));

				selectedTxt = "";
				if (selected_value != null) {
					for (int i = 1; i <= 2 /* colCount */; i++) {
						if (selected_value.equalsIgnoreCase(rset.getString(i))) {
							selectedTxt = "SELECTED";
						}
					}
				}
				String pOptionValue = rset.getString(1);
				String pOptionText = "";
				String pOptionTitle = "";
				styleText = "";
				optionExist = true;
				if (colCount > 2) {
					for (int i = 3; i <= colCount; i++) {
						if ("EXIST_FLAG".equalsIgnoreCase(mtd.getColumnName(i))
								&& "N".equalsIgnoreCase(rset.getString(i))) {
							styleText = " style=\"font-weight:bold;color: #FF0000;\"";
							optionExist = false;
						}
						if ("OPTION_STYLE".equalsIgnoreCase(mtd
								.getColumnName(i))
								&& !isEmpty(rset.getString(i))
								&& styledValue) {
							styleText = " " + rset.getString(i);
						}
					}
				}
				if (isEmpty(pSize)) {
					pOptionText = rset.getString(2);
					pOptionTitle = "";
				} else {
					if (!isEmpty(rset.getString(2))) {
						int lTextSize = Integer.parseInt(pSize);
						if (rset.getString(2).length() > lTextSize) {
							pOptionText = rset.getString(2).substring(0,
									lTextSize)
									+ "...";
							pOptionTitle = rset.getString(2);
						} else {
							pOptionText = rset.getString(2);
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
				if (!existsOnly || (existsOnly && optionExist)) {
					return_value.append("<option value=\"" + pOptionValue
							+ "\" " + selectedTxt + styleText + " title=\""
							+ pOptionTitle + "\">" + pOptionText + "</option>");
					this.selectOprionCount = this.selectOprionCount + 1;
				}
			}
		} catch (SQLException e) {
			LOGGER.debug("bcBase.getSelectBodyFromQuery SQLException: "
					+ e.toString());
			return_value.append("<option value=\"\">SQL Error</option>");
		} catch (Exception el) {
			LOGGER.debug("bcBase.getSelectBodyFromQuery Exception: "
					+ el.toString());
		} finally {
			try {
				if (st != null) {
					st.close();
				}
			} catch (SQLException w) {
				w.toString();
			}
			try {
				if (con != null) {
					con.close();
				}
			} catch (SQLException w) {
				w.toString();
			}
			Connector.closeConnection(con);
		}
		return return_value.toString();
	}

	public String getSelectBodyFromParamQuery(String query,
			ArrayList<bcFeautureParam> pParam, String selected_value,
			String pSize, boolean isNull) {
		ArrayList<bcDictionaryRecord> pDict = new ArrayList<bcDictionaryRecord>();

		return getSelectBodyFromParamQuery(query, pParam, selected_value,
				pSize, isNull, pDict, false, true);
	}

	public String getSelectBodyFromSavedArray2(
			ArrayList<bcDictionaryRecord> pDict, String selected_value,
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
			for (int i = 0; i < pDict.size(); i++) {
				String code = pDict.get(i).getCode();
				String value = pDict.get(i).getValue();
				String existFlag = pDict.get(i).getExistFlag();
				String optionStyle = pDict.get(i).getOptionStyle();
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
				if ("N".equalsIgnoreCase(existFlag)) {
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
						|| (existsOnly && "Y".equalsIgnoreCase(existFlag))) {
					return_value.append("<option value=\"" + pOptionValue
							+ "\" " + selectedTxt + styleText + " title=\""
							+ pOptionTitle + "\">" + pOptionText + "</option>");
				}
				this.selectOprionCount = this.selectOprionCount + 1;
			}
		} catch (Exception el) {
			LOGGER.debug("bcBase.getSelectBodyFromQuery Exception: "
					+ el.toString());
		}
		return return_value.toString();
	}

	public String getRadioButtonsFromQuery(String pName, String pQuery,
			String pSelectedValue, bcXML helpXML,
			ArrayList<bcDictionaryRecord> pDict, boolean existsOnly) {
		ArrayList<bcFeautureParam> pParam = initParamArray();
		pParam.add(new bcFeautureParam("none", ""));

		return getRadioButtonsFromParamQuery(pName, pQuery, pParam,
				pSelectedValue, helpXML, pDict, existsOnly);
	}

	public String getRadioButtonsFromParamQuery(String pName, String pQuery,
			ArrayList<bcFeautureParam> pParam, String pSelectedValue,
			bcXML helpXML, ArrayList<bcDictionaryRecord> pDict,
			boolean existsOnly) {
		StringBuilder return_value = new StringBuilder();
		PreparedStatement st = null;
		Connection con = null;
		String selectedChecked = "";
		String selectedStyle = "";
		boolean optionExist = true;
		this.selectOprionCount = 0;

		if (pDict.size() > 0) {

			boolean pHasValue = false;
			for (int i = 0; i < pDict.size(); i++) {
				selectedChecked = "";
				selectedStyle = "";
				String code = pDict.get(i).getCode();
				String value = pDict.get(i).getValue();
				String existFlag = pDict.get(i).getExistFlag();
				String optionStyle = pDict.get(i).getOptionStyle();
				if (pSelectedValue != null) {
					if (pSelectedValue.equalsIgnoreCase(code)
							|| pSelectedValue.equalsIgnoreCase(value)) {
						selectedChecked = " CHECKED ";
					}
				}
				String pOptionValue = code;
				String pOptionText = value;
				optionExist = true;
				if (!existsOnly
						|| (existsOnly && "Y".equalsIgnoreCase(existFlag))) {
					//LOGGER.debug("code=" + code + ", value=" + value
					//		+ ", existFlag=" + existFlag);
					if (pHasValue) {
						return_value.append("<br>");
					}
					if ("N".equalsIgnoreCase(existFlag)) {
						selectedStyle = selectedStyle + " style=\"font-weight:bold;color: #FF0000;\"";
						optionExist = false;
					}
					if (!isEmpty(optionStyle)) {
						selectedStyle = selectedStyle + " " + optionStyle;
					}
					if (!optionExist) {
						pOptionText = pOptionText
								+ " ("
								+ this.commonXML.getfieldTransl("h_exist_flag_n",false) + ")";
					}
					return_value.append("<input type=\"radio\" name=\"" + pName
							+ "\" id=\"" + pName + "_" + pOptionValue
							+ "\" value=\"" + pOptionValue + "\" "
							+ selectedChecked + ">");
					return_value.append("<label class=\"checbox_label\" for=\""
							+ pName + "_" + pOptionValue + "\" " + selectedStyle
							+ ">" + pOptionText + "</label>");
					if (!(helpXML == null)) {
						String pHelpValue = helpXML.getfieldTransl("help_" + pOptionValue, false);
						if (!(isEmpty(pHelpValue) || "null"
									.equalsIgnoreCase(pHelpValue))) {
							return_value.append(this.getUWndHelp(pHelpValue));
						}
					}
					pHasValue = true;
					this.selectOprionCount = this.selectOprionCount + 1;
				}
			}
		} else {
			try {
				LOGGER.debug(prepareSQLToLog(pQuery, pParam));
				con = Connector.getConnection(this.sessionId);
				st = con.prepareStatement(pQuery);
				st = prepareParam(st, pParam);
				ResultSet rset = st.executeQuery();

				ResultSetMetaData mtd = rset.getMetaData();
				int colCount = mtd.getColumnCount();

				boolean pHasValue = false;
				while (rset.next()) {
					String code = rset.getString(1);
					String value = rset.getString(2);
					String existFlag = "";
					String optionStyle = "";
					if (colCount > 2) {
						for (int i = 3; i <= colCount; i++) {
							if ("EXIST_FLAG".equalsIgnoreCase(mtd
									.getColumnName(i))) {
								existFlag = rset.getString("EXIST_FLAG");
							} else if ("OPTION_STYLE".equalsIgnoreCase(mtd
									.getColumnName(i))) {
								optionStyle = rset.getString("OPTION_STYLE");
							}
						}
					}
					pDict.add(new bcDictionaryRecord(code, value, existFlag,
							optionStyle));

					selectedChecked = "";
					if (pSelectedValue != null) {
						for (int i = 1; i <= colCount; i++) {
							if (pSelectedValue.equalsIgnoreCase(rset
									.getString(1))) {
								selectedChecked = "CHECKED";
							}
						}
					}
					String pOptionValue = rset.getString(1);
					String pOptionText = rset.getString(2);
					if (pHasValue) {
						return_value.append("<br>");
					}
					return_value.append("<input type=\"radio\" name=\"" + pName
							+ "\" id=\"" + pName + "_" + pOptionValue
							+ "\" value=\"" + pOptionValue + "\" "
							+ selectedChecked + ">");
					return_value.append("<label class=\"checbox_label\" for=\""
							+ pName + "_" + pOptionValue + " " + optionStyle + "\">" + pOptionText
							+ "</label>");
					if (!(helpXML == null)) {
						String pHelpValue = helpXML.getfieldTransl("help_" + pOptionValue,false);
						if (!(isEmpty(pHelpValue) || "null"
									.equalsIgnoreCase(pHelpValue))) {
							return_value.append(this.getUWndHelp(pHelpValue));
						}
					}
					pHasValue = true;
					this.selectOprionCount = this.selectOprionCount + 1;
				}
			} catch (SQLException e) {
				LOGGER.debug("bcBase.getSelectBodyFromQuery SQLException: "
						+ e.toString());
				return_value.append("<option value=\"\">SQL Error</option>");
			} catch (Exception el) {
				LOGGER.debug("bcBase.getSelectBodyFromQuery Exception: "
						+ el.toString());
			} finally {
				try {
					if (st != null) {
						st.close();
					}
				} catch (SQLException w) {
					w.toString();
				}
				try {
					if (con != null) {
						con.close();
					}
				} catch (SQLException w) {
					w.toString();
				}
				Connector.closeConnection(con);
			}
		}
		return return_value.toString();
	}

	public String getSelectBodyFromLookups(String pLookupType,
			String pFieldNames, String pOrderField, String[] pExcludeValues,
			String[] pOnlyValues, String selected_value, String pSize,
			boolean isNull, boolean existsOnly) {
		StringBuilder return_value = new StringBuilder();
		String selectedTxt = "";
		String styleText = "";
		boolean optionExist = true;
		this.selectOprionCount = 0;
		String code = "";
		String value = "";
		String existFlag = "";
		String optionStyle = "";
		// String orderField = "";

		ArrayList<bcLookupRecord> lSoruceLookup = new ArrayList<bcLookupRecord>();

		try {
			if (isNull) {
				return_value.append("<option value=\"\" title=\"\"></option>");
			}
			for (int i = 0; i < lookups.size(); i++) {
				if (lookups.get(i).getType().equalsIgnoreCase(pLookupType)) {
					if (pExcludeValues != null) {
						// LOGGER.debug("pExcludeValues.length="+pExcludeValues.length);
						String pValue = "";
						boolean valueFound = false;
						if ("CODE_CODE".equalsIgnoreCase(pFieldNames)
								|| "CODE_MEANING".equalsIgnoreCase(pFieldNames)
								|| "CODE_DESCRIPTION"
										.equalsIgnoreCase(pFieldNames)) {
							pValue = lookups.get(i).getCode();
						} else if ("NUMBER_MEANING"
								.equalsIgnoreCase(pFieldNames)
								|| "NUMBER_DESCRIPTION"
										.equalsIgnoreCase(pFieldNames)) {
							pValue = lookups.get(i).getNumberValue() + "";
						}
						//LOGGER.debug("pValue=" + pValue);
						for (int counter = 0; counter < pExcludeValues.length; counter++) {
							// LOGGER.debug("lExclude="+lExclude+", pValue="+pValue);
							if (pValue
									.equalsIgnoreCase(pExcludeValues[counter])) {
								// LOGGER.debug("add");
								//LOGGER.debug("pExcludeValues[counter]="
								//		+ pExcludeValues[counter] + ", pValue="
								//		+ pValue);
								valueFound = true;
							}
						}
						if (!valueFound) {
							lSoruceLookup.add(lookups.get(i));
						}
					} else {
						// LOGGER.debug("pExcludeValues is null");
						if (pOnlyValues != null) {
							String pValue = "";
							boolean valueFound = false;
							if ("CODE_CODE".equalsIgnoreCase(pFieldNames)
									|| "CODE_MEANING"
											.equalsIgnoreCase(pFieldNames)
									|| "CODE_DESCRIPTION"
											.equalsIgnoreCase(pFieldNames)) {
								pValue = lookups.get(i).getCode();
							} else if ("NUMBER_MEANING"
									.equalsIgnoreCase(pFieldNames)
									|| "NUMBER_DESCRIPTION"
											.equalsIgnoreCase(pFieldNames)) {
								pValue = lookups.get(i).getNumberValue() + "";
							}
							for (int counter = 0; counter < pOnlyValues.length; counter++) {
								if (pValue
										.equalsIgnoreCase(pOnlyValues[counter])) {
									valueFound = true;
								}
							}
							if (valueFound) {
								lSoruceLookup.add(lookups.get(i));
							}
						} else {
							// LOGGER.debug("Add values");
							lSoruceLookup.add(lookups.get(i));
						}
					}
					// LOGGER.debug("lookups.code=" +
					// lookups.get(i).getCode() + "lookups.meaning=" +
					// lookups.get(i).getMeaning());
				}
			}
			// Сортируем данные
			// LOGGER.debug("Sort order = " + pOrderField);
			if ("CODE".equalsIgnoreCase(pOrderField)) {
				Collections.sort(lSoruceLookup,
						new bcLookupRecord.CompareCode());
			} else if ("MEANING".equalsIgnoreCase(pOrderField)) {
				Collections.sort(lSoruceLookup,
						new bcLookupRecord.CompareMeaning());
			} else if ("DESCRIPTION".equalsIgnoreCase(pOrderField)) {
				Collections.sort(lSoruceLookup,
						new bcLookupRecord.CompareDescription());
			} else if ("NUMBER".equalsIgnoreCase(pOrderField)) {
				Collections.sort(lSoruceLookup,
						new bcLookupRecord.CompareNumber());
			} else {
				Collections.sort(lSoruceLookup,
						new bcLookupRecord.CompareCode());
			}

			// Выводим данные
			for (int i = 0; i < lSoruceLookup.size(); i++) {
				if ("CODE_CODE".equalsIgnoreCase(pFieldNames)) {
					code = lSoruceLookup.get(i).getCode();
					value = lSoruceLookup.get(i).getCode();
				} else if ("CODE_MEANING".equalsIgnoreCase(pFieldNames)) {
					code = lSoruceLookup.get(i).getCode();
					value = lSoruceLookup.get(i).getMeaning();
				} else if ("CODE_DESCRIPTION".equalsIgnoreCase(pFieldNames)) {
					code = lSoruceLookup.get(i).getCode();
					value = lSoruceLookup.get(i).getDescription();
				} else if ("NUMBER_MEANING".equalsIgnoreCase(pFieldNames)) {
					code = lSoruceLookup.get(i).getNumberValue() + "";
					value = lSoruceLookup.get(i).getMeaning();
				} else if ("NUMBER_DESCRIPTION".equalsIgnoreCase(pFieldNames)) {
					code = lSoruceLookup.get(i).getNumberValue() + "";
					value = lSoruceLookup.get(i).getDescription();
				} else {
					code = lSoruceLookup.get(i).getCode();
					value = lSoruceLookup.get(i).getCode();
				}
				existFlag = lSoruceLookup.get(i).getExistFlag();
				optionStyle = lSoruceLookup.get(i).getOptionStyle();

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
				if ("N".equalsIgnoreCase(existFlag)) {
					styleText = " style=\"font-weight:bold;color: #FF0000;\"";
					optionExist = false;
				}
				if (!isEmpty(optionStyle)) {
					styleText = " style=\"" + optionStyle +"\"";
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
						|| (existsOnly && "Y".equalsIgnoreCase(existFlag))) {
					return_value.append("<option value=\"" + pOptionValue
							+ "\" " + selectedTxt + styleText + " title=\""
							+ pOptionTitle + "\">" + pOptionText + "</option>");
				}
				this.selectOprionCount = this.selectOprionCount + 1;
			}
		} catch (Exception el) {
			LOGGER.debug("bcBase.getSelectBodyFromLookups Exception: "
					+ el.toString());
		}
		return return_value.toString();
	}

	public String getYesNoLookupNameOptions(String lookup_code, boolean isNull) {
		return getSelectBodyFromLookups("YES_NO", "CODE_MEANING", "MEANING",
				lookup_code, null, isNull, false);
	}

	public String getSelectBodyFromLookups(String pLookupType,
			String pFieldNames, String pOrderField, String selected_value,
			String pSize, boolean isNull, boolean existsOnly) {
		return getSelectBodyFromLookups(pLookupType, pFieldNames, pOrderField,
				null, null, selected_value, pSize, isNull, existsOnly);
	}

	public String getYesNoLookupOptions(String pValue, boolean isNull) {
		return getSelectBodyFromLookups("YES_NO", "CODE_MEANING", "MEANING",
				pValue, null, isNull, false);
	}

	public String getMeaningFromLookupNameOptions(String lookup_name,
			String lookup_code, boolean isNull) {
		return getSelectBodyFromLookups(lookup_name, "CODE_MEANING", "MEANING",
				lookup_code, null, isNull, false);
	}

	public String getLookupCodeOptions(String lookup_name, String lookup_code,
			boolean isNull) {
		return getSelectBodyFromLookups(lookup_name, "CODE_CODE", "CODE",
				lookup_code, null, isNull, false);
	}

	public String getMeaningFromLookupNameOrderByNymberValueOptions(
			String lookup_name, String lookup_code, boolean isNull) {
		return getSelectBodyFromLookups(lookup_name, "CODE_MEANING", "NUMBER",
				lookup_code, null, isNull, false);
	}

	public String getMeaningFromLookupNumberOptions(String lookup_name,
			String number_value, boolean isNull) {
		return getSelectBodyFromLookups(lookup_name, "NUMBER_MEANING",
				"NUMBER", number_value, null, isNull, false);
	}

	public String getMeaningFromLookupNumHTML(String lookup_name,
			String number_value, boolean isNull) {
		return getSelectBodyFromLookups(lookup_name, "NUMBER_MEANING",
				"MEANING", number_value, null, isNull, false);
	}

	public String getDescriptionFromLookupNumOptions(String lookup_name,
			String number_value, boolean isNull) {
		return getSelectBodyFromLookups(lookup_name, "NUMBER_DESCRIPTION",
				"DESCRIPTION", number_value, null, isNull, false);
	}

	public String getAllMenuOptions(String id_menu, boolean isNull) {

		StringBuilder return_value = new StringBuilder();
		Statement st = null;
		Connection con = null;
		String selectedTxt = "";
		String styleText = "";
		boolean optionExist = true;
		String mySQL = " SELECT id_menu_element, REPLACE(name_menu_element_frmt, ' ', '&nbsp;'),"
				+ "        id_menu_element_parent, type_menu_element,"
				+ "        CASE WHEN is_enable = 'Y' "
				+ "             THEN is_visible "
				+ "             ELSE is_enable "
				+ "        END exist_flag "
				+ "   FROM "
				+ getGeneralDBScheme()
				+ ".vc_menu_and_tabsheet_all " + "  ORDER BY order_number";
		this.selectOprionCount = 0;
		try {
			con = Connector.getConnection(this.sessionId);
			st = con.createStatement();

			LOGGER.debug(mySQL);
			ResultSet rset = st.executeQuery(mySQL);

			if (isNull) {
				return_value.append("<option value=\"\"></option>");
			}

			while (rset.next()) {
				selectedTxt = "";
				if (id_menu != null) {
					for (int i = 1; i <= 2 /* colCount */; i++) {
						if (id_menu.equalsIgnoreCase(rset.getString(i))) {
							selectedTxt = "SELECTED";
						}
					}
				}
				String pOptionValue = rset.getString(1);
				String pOptionText = rset.getString(2);
				styleText = "";
				optionExist = true;

				if (isEmpty(rset.getString("ID_MENU_ELEMENT_PARENT"))) {
					styleText = " style=\"font-weight:bold;color: green;\" ";
				} else if ("MENU".equalsIgnoreCase(rset
						.getString("TYPE_MENU_ELEMENT"))) {
					styleText = " style=\"font-weight:bold;color: #000000;\" ";
				}
				if ("N".equalsIgnoreCase(rset.getString("EXIST_FLAG"))) {
					styleText = " style=\"font-weight:bold;color: #FF0000;\"";
					optionExist = false;
				}
				if (!optionExist) {
					pOptionText = pOptionText
							+ " ("
							+ this.commonXML.getfieldTransl("h_exist_flag_n", false) + ")";
				}
				return_value.append("<option value=\"" + pOptionValue + "\" "
						+ selectedTxt + styleText + ">" + pOptionText
						+ "</option>");
				this.selectOprionCount = this.selectOprionCount + 1;
			}
		} catch (SQLException e) {
			LOGGER.debug("bcBase.getAllMenuOptions SQLException: "
					+ e.toString());
			return_value.append("<option value=\"\">SQL Error</option>");
		} catch (Exception el) {
			LOGGER.debug("bcBase.getAllMenuOptions Exception: " + el.toString());
		} finally {
			try {
				if (st != null) {
					st.close();
				}
			} catch (SQLException w) {
				w.toString();
			}
			try {
				if (con != null) {
					con.close();
				}
			} catch (SQLException w) {
				w.toString();
			}
			Connector.closeConnection(con);
		}
		return return_value.toString();
	}

	public String getUserDistinctReportMenuNameOptions(String id_menu,
			boolean isNull) {
		return getSelectBodyFromQuery2(
				" SELECT id_menu_element, title_menu_element||' ('||type_menu_element_tsl||', '||TO_CHAR(id_menu_element)||')' name_menu_element "
						+ "   FROM "
						+ getGeneralDBScheme()
						+ ".vc_menu_and_tabsheet_all "
						+ "  WHERE is_enable = 'Y' "
						+ "    AND is_visible = 'Y' "
						+ "    AND id_menu_element IN ("
						+ "          SELECT DISTINCT id_menu_element "
						+ "            FROM "
						+ getGeneralDBScheme()
						+ ".vc_reports_all) " + "  ORDER BY order_number",
				id_menu, isNull);
	}

	public String getCalculatorVariablesOptions(String pField, String pOperType) {
		StringBuilder html = new StringBuilder();

		html.append("<select multiple=\"multiple\" id=\"source_numbers\" class=\"inputfield2\" size=\"7\">\n");
		if ("assignment_posting".equals(pField)) {
			html.append(this.getMeaningFromLookupNumberOptions(
					"CALCULATOR_POSTING_PARAM", "", false));
		}

		if (("REC_PAYMENT_01".equalsIgnoreCase(pOperType) || "REC_STORNO_BON_01"
				.equalsIgnoreCase(pOperType))) {
			html.append(getSelectBodyFromLookups("CALCULATOR_VARIABLES",
					"NUMBER_MEANING", "NUMBER", null, null, "", null, false,
					false));
		} else if ("REC_MOV_BON_01".equalsIgnoreCase(pOperType)) {
			String[] pOnlyValues = new String[3];
			pOnlyValues[0] = "BAL_ACC";
			html.append(getSelectBodyFromLookups("CALCULATOR_2_BON_TYPE",
					"NUMBER_MEANING", "NUMBER", null, pOnlyValues, "", null,
					false, false));
		} else if ("REMITTANCE_BANK_ACCOUNT_CARD".equalsIgnoreCase(pOperType)
				|| "REMITTANCE_CARD_MONEY".equalsIgnoreCase(pOperType)
				|| "REMITTANCE_CARD_BANK_ACCOUNT".equalsIgnoreCase(pOperType)
				|| "REMITTANCE_CARD_CARD".equalsIgnoreCase(pOperType)
				|| "REMITTANCE_MONEY_CARD".equalsIgnoreCase(pOperType)) {
			String[] pOnlyValues = new String[3];
			pOnlyValues[0] = "BAL_CUR";
			html.append(getSelectBodyFromLookups("CALCULATOR_2_BON_TYPE",
					"NUMBER_MEANING", "NUMBER", null, pOnlyValues, "", null,
					false, false));
		} else if ("COMPULSORY_CARD_CLOSING".equalsIgnoreCase(pOperType)
				|| "CONTRACT_CANCELLATION".equalsIgnoreCase(pOperType)
				|| "ACCOUNT_ADMINISTRATION".equalsIgnoreCase(pOperType)) {
			html.append(getSelectBodyFromLookups("CALCULATOR_2_BON_TYPE",
					"NUMBER_MEANING", "NUMBER", null, null, "", null, false,
					false));
		} else if ("CARD_PURCHASE_DEALER".equalsIgnoreCase(pOperType)
				|| "CARD_PURCHASE_BANK".equalsIgnoreCase(pOperType)
				|| "CARD_PURCHASE_OPERATOR".equalsIgnoreCase(pOperType)
				|| "CARD_PURCHASE_PARTNER".equalsIgnoreCase(pOperType)) {
		}
		html.append("</select>");

		return html.toString();
	}

	public String getLanguageOptions(String cd_language, boolean isNull) {
		return getSelectBodyFromQuery(
				" SELECT cd_language, name_language, exist_flag " + "   FROM "
						+ getGeneralDBScheme() + ".vc_language_all"
						+ "  ORDER BY name_language", cd_language, isNull,
				languagesAll, false);
	}

	public String getSysModuleTypeOptions(String id_type, boolean isNull) {
		return getSelectBodyFromQuery(
				" SELECT cd_module_type, name_module_type," +
				"        DECODE(cd_module_type, " +
      	  		" 	 	     'WEBCLIENT', ' style=\"font-weight:bold;color: blue;\" ', " +
      	  		"          	 'WEBPOS', ' style=\"font-weight:bold;color: blue;\" ', " +
      	  		"          	 'CRM', ' style=\"font-weight:bold;color: red;\" ', " +
      	  		"          	 'SYSTEM', ' style=\"font-weight:bold;color: red;\" ', " +
      	  		"          	 'PRIVATE_OFFICE', ' style=\"font-weight:bold;color: green;\" ', " +
	  			"          	 '' " +
      	  		"  	     ) option_style " + 
				"   FROM " + getGeneralDBScheme() + ".vc_module_type_all" + 
				"  ORDER BY name_module_type", 
				id_type, isNull, sysModuleType, false);
	}

	public String getReportKindOptions(String cd_kind, boolean isNull) {
		return getSelectBodyFromQuery(
				" SELECT cd_report_kind, name_report_kind, " +
				"        DECODE(cd_report_kind, " +
      	  		" 	 	     'SYSTEM', ' style=\"font-weight:bold;color: black;\" ', " +
      	  		"          	 'ACCOUNTING', ' style=\"font-weight:bold;color: green;\" ', " +
      	  		"          	 'MARKETING', ' style=\"font-weight:bold;color: green;\" ', " +
      	  		"          	 'PARTNER', ' style=\"font-weight:bold;color: blue;\" ', " +
      	  		"          	 'SHAREHOLDER', ' style=\"font-weight:bold;color: blue;\" ', " +
      	  		"          	 'TARGET_PROGRAM', ' style=\"font-weight:bold;color: blue;\" ', " +
      	  		"          	 'AGENT', ' style=\"font-weight:bold;color: blue;\" ', " +
      	  		"          	 'UNKNOWN', ' style=\"font-weight:bold;color: red;\" ', " +
	  			"          	 '' " +
      	  		"  	     ) option_style, " +
      	  		"        exist_flag " +
      	  		"   FROM " + getGeneralDBScheme()
      	  		+ ".vc_report_kind_all "
      	  		+ "  ORDER BY name_report_kind", 
      	  	cd_kind, isNull, reportKind, false);
	}

	public String getPrivilegeTypeOptions(String id_type, boolean isNull) {
		return getSelectBodyFromQuery(
				" SELECT id_privilege_type, name_privilege_type," +
				"        DECODE(id_privilege_type, " +
      	  		" 	 	     1, ' style=\"font-weight:bold;color: brown;\" ', " +
      	  		"          	 2, ' style=\"font-weight:bold;color: green;\" ', " +
      	  		"          	 9, ' style=\"font-weight:bold;color: blue;\" ', " +
      	  		"          	 '' " +
      	  		"  	     ) option_style " + 
				"   FROM " + getGeneralDBScheme() + ".vc_privilege_type_all" + 
				"  ORDER BY id_privilege_type", 
				id_type, isNull, privilegeType, false);
	}

	public String getUserAccessTypeOptions(String cd_access_type, boolean isNull) {
		return getSelectBodyFromQuery(
				" SELECT cd_user_access_type, name_user_access_type, " +
				"        DECODE(cd_user_access_type, " +
      	  		" 	 	     'MANAGER', ' style=\"font-weight:bold;color: red;\" ', " +
      	  		" 	 	     'CASHIER', ' style=\"font-weight:bold;color: blue;\" ', " +
      	  		" 	 	     'STANDARD', ' style=\"font-weight:bold;color: green;\" ', " +
      	  		"          	 '' " +
      	  		"  	     ) option_style, exist_flag "	+ 
      	  		"   FROM " + getGeneralDBScheme() + ".vc_user_access_type_all "+ 
      	  		"  ORDER BY name_user_access_type", cd_access_type, isNull,
				sysUserAccessType, false);
	}

	public String getUserStatusOptions(String cd_status, boolean isNull) {
		return getSelectBodyFromQuery(
				" SELECT cd_user_status, name_user_status, " +
				"        DECODE(cd_user_status, " +
      	  		" 	 	     'OPENED', ' style=\"font-weight:bold;color: green;\" ', " +
      	  		" 	 	     'NEED_CHANGE_PASSWORD', ' style=\"font-weight:blue;color: green;\" ', " +
      	  		"          	 ' style=\"font-weight:bold;color: red;\" ' " +
      	  		"  	     ) option_style, exist_flag "	+ 
      	  		"   FROM " + getGeneralDBScheme() + ".vc_user_status_all "+ 
      	  		"  ORDER BY name_user_status", cd_status, isNull,
				sysUserStatus, false);
	}
	
	public String getFinancePriorityOptions(String priority, boolean isNull) {
		return getSelectBodyFromQuery(
				" SELECT cd_fn_priority, name_fn_priority, exist_flag,"
					+ "        DECODE(cd_fn_priority, "
					+ "               'CRITICAL', 'style=\"color:red;font-weight:bold;\"', "
					+ "               'HIGH', 'style=\"color:red;\"', "
					+ "               'MEDIUM', 'style=\"color:blue;\"', "
					+ "               'LOW', 'style=\"color:green;\"', "
					+ "               ''"
					+ "  		) option_style "
					+ "   FROM " + getGeneralDBScheme()
					+ ".vc_fn_priority_all"
					+ "  ORDER BY DECODE(cd_fn_priority, 'CRITICAL', 1, 'HIGH', 2, 'MEDIUM', 3, 'LOW', 4, 9), name_fn_priority", priority,
				isNull, fnPriority, false);
	}

	// ========================================================================
	// Функции возвращают одно значение из запроса
	// ========================================================================

	public PreparedStatement prepareParam(PreparedStatement st,
			ArrayList<bcFeautureParam> pParam) {
		try {

			int fieldCounter = 0;
			for (int i = 0; i < pParam.size(); i++) {
				String key = pParam.get(i).getDataType();
				String value = pParam.get(i).getValue();
				if ("string".equalsIgnoreCase(key)) {
					fieldCounter++;
					st.setString(fieldCounter, value);
				} else if ("int".equalsIgnoreCase(key)) {
					int myId = Integer.parseInt(value);
					fieldCounter++;
					st.setInt(fieldCounter, myId);
				}
			}

		} catch (SQLException e) {
			LOGGER.error("SQLException: " + e.toString());
		}
		return st;
	}

	public String getOneValueByParamId(String pSQL,	ArrayList<bcFeautureParam> pParam) {
		String result = "";
		PreparedStatement st = null;
		Connection con = null;

		try {
			//LOGGER.debug(prepareSQLToLog(pSQL, pParam));
			con = Connector.getConnection(this.sessionId);
			st = con.prepareStatement(pSQL);
			st = prepareParam(st, pParam);
			ResultSet rset = st.executeQuery();

			while (rset.next()) {
				result = rset.getString(1).trim();
			}
			result = getValue(result);
			LOGGER.debug("result=" + result + ", " + prepareSQLToLog(pSQL, pParam));
		} // try
		catch (SQLException e) {
			LOGGER.error(prepareSQLToLog(pSQL, pParam) + "\n, SQLException: " + e.toString());
		} catch (Exception el) {
			LOGGER.error(prepareSQLToLog(pSQL, pParam) + "\n, Exception: " + el.toString());
		} finally {
			try {
				if (st != null) {
					st.close();
				}
			} catch (SQLException w) {
				w.toString();
			}
			try {
				if (con != null) {
					con.close();
				}
			} catch (SQLException w) {
				w.toString();
			}
			Connector.closeConnection(con);
		} // finally
		//LOGGER.debug("bcBase.getOneValueById END: result=" + result);
		return result;
	}

	public String getOneValueFromLookups(String pLookupType, String pCodeType,
			String pValue, String pReturnType) {
		String result = "";
		String code = "";
		String value = "";
		String existFlag = "";

		if (isEmpty(pLookupType)) {
			return "";
		}
		if (isEmpty(pValue)) {
			return "";
		}

		try {
			for (int i = 0; i < lookups.size(); i++) {
				if (lookups.get(i).getType().equalsIgnoreCase(pLookupType)) {
					existFlag = lookups.get(i).getExistFlag();

					if ("CODE".equalsIgnoreCase(pCodeType)) {
						code = lookups.get(i).getCode();
					} else if ("NUMBER".equalsIgnoreCase(pCodeType)) {
						code = lookups.get(i).getNumberValue() + "";
					} else {
						code = lookups.get(i).getCode();
					}
					if ("CODE".equalsIgnoreCase(pReturnType)) {
						value = lookups.get(i).getCode();
					} else if ("MEANING".equalsIgnoreCase(pReturnType)) {
						value = lookups.get(i).getMeaning();
					} else if ("DESCRIPTION".equalsIgnoreCase(pReturnType)) {
						value = lookups.get(i).getDescription();
					} else if ("NUMBER".equalsIgnoreCase(pReturnType)) {
						value = lookups.get(i).getNumberValue() + "";
					} else {
						value = lookups.get(i).getCode();
					}

					if (code.equalsIgnoreCase(pValue)) {
						result = value;
						if ("N".equalsIgnoreCase(existFlag)) {
							result = result
									+ " ("
									+ this.commonXML.getfieldTransl("h_exist_flag_n", false) + ")";
						}
						// LOGGER.debug("getOneValueFromLookups.result=" +
						// result);
					}
				}
			}
			if (isEmpty(result)) {
				LOGGER.error("getOneValueFromLookups ERROR: " +
					"pLookupType="+pLookupType+", pCodeType="+pCodeType+
					", pValue="+pValue+", pReturnType="+pReturnType+", result=\"" + result + "\"");
			}
		} catch (Exception el) {
			LOGGER.error("getOneValueFromLookups Exception: " + 
					"pLookupType="+pLookupType+", pCodeType="+pCodeType+
					", pValue="+pValue+", pReturnType="+pReturnType+", exception: "+el.toString());
			//LOGGER.debug("bcBase.getSelectBodyFromLookups Exception: "
			//		+ el.toString());
		}
		return result;
	}

	public ArrayList<bcFeautureParam> initParamArray() {
		ArrayList<bcFeautureParam> pParam = new ArrayList<bcFeautureParam>();
		return pParam;
	}

	public String getOneValueByStringId2(String pSQL, String pId) {
		if (isEmpty(pId)) {
			return "";
		}
		ArrayList<bcFeautureParam> pParam = initParamArray();
		pParam.add(new bcFeautureParam("string", pId));
		return getOneValueByParamId(pSQL, pParam);
	}

	public String getOneValueByStringId2(String pSQL, String pId,
			ArrayList<bcDictionaryRecord> pDict) {
		String return_value = "";
		if (isEmpty(pId)) {
			return_value = "";
		}
		if (pDict.size() == 0) {
			ArrayList<bcFeautureParam> pParam = initParamArray();
			pParam.add(new bcFeautureParam("string", pId));
			return_value = getOneValueByParamId(pSQL, pParam);
			pDict.add(new bcDictionaryRecord(pId, return_value));
		} else {
			boolean valueExist = false;
			for (int i = 0; i < pDict.size(); i++) {
				String code = pDict.get(i).getCode();
				String value = pDict.get(i).getValue();
				if (pId.equalsIgnoreCase(code)) {
					return_value = value;
					valueExist = true;
				}
			}
			if (!valueExist) {
				ArrayList<bcFeautureParam> pParam = initParamArray();
				pParam.add(new bcFeautureParam("string", pId));
				return_value = getOneValueByParamId(pSQL, pParam);
				pDict.add(new bcDictionaryRecord(pId, return_value));
			}
		}
		return return_value;
	}

	public String getOneValueByIntId2(String pSQL, String pId) {
		if (isEmpty(pId)) {
			return "";
		}
		ArrayList<bcFeautureParam> pParam = initParamArray();
		pParam.add(new bcFeautureParam("int", pId));
		return getOneValueByParamId(pSQL, pParam);
	}

	public String getOneValueByIntId2(String pSQL, String pId,
			ArrayList<bcDictionaryRecord> pDict) {
		String return_value = "";
		if (isEmpty(pId)) {
			return_value = "";
		} else {
			if (pDict.size() == 0) {
				ArrayList<bcFeautureParam> pParam = initParamArray();
				pParam.add(new bcFeautureParam("int", pId));
				return_value = getOneValueByParamId(pSQL, pParam);
				pDict.add(new bcDictionaryRecord(pId, return_value));
			} else {
				boolean valueExist = false;
				for (int i = 0; i < pDict.size(); i++) {
					String code = pDict.get(i).getCode();
					String value = pDict.get(i).getValue();
					if (pId.equalsIgnoreCase(code)) {
						return_value = value;
						valueExist = true;
					}
				}
				if (!valueExist) {
					ArrayList<bcFeautureParam> pParam = initParamArray();
					pParam.add(new bcFeautureParam("int", pId));
					return_value = getOneValueByParamId(pSQL, pParam);
					pDict.add(new bcDictionaryRecord(pId, return_value));
				}
			}
		}
		return return_value;
	}

	public String getOneValueByNoneId(String pSQL) {
		ArrayList<bcFeautureParam> pParam = initParamArray();
		pParam.add(new bcFeautureParam("none", ""));
		return getOneValueByParamId(pSQL, pParam);
	}

	public String getVersion() {
		return getOneValueByNoneId("SELECT current_version FROM "
				+ getGeneralDBScheme() + ".v_version");
	}

	public String getMeaningForNumValue(String lookup_type, String numValue) {
		return getOneValueFromLookups(lookup_type, "NUMBER", numValue,
				"MEANING");
	}

	public String getMeaningFoCodeValue(String lookup_type, String lookupCode) {
		return getOneValueFromLookups(lookup_type, "CODE", lookupCode,
				"MEANING");
	}

	public String getDescriptionFoCodeValue(String lookup_type,
			String lookupCode) {
		return getOneValueFromLookups(lookup_type, "CODE", lookupCode,
				"DESCRIPTION");
	}

	public String getDescriptionFoNumValue(String lookup_type, String NumValue) {
		return getOneValueFromLookups(lookup_type, "NUMBER", NumValue,
				"DESCRIPTION");
	}

	public String getSystemUserName(String id_user) {
		String return_value = "";
		return_value = getOneValueByIntId2("SELECT name_user FROM "
				+ getGeneralDBScheme() + ".v_user_names WHERE id_user = ?",
				id_user, systemUsers);
		if (!isEmpty(id_user)) {
			if (isEmpty(return_value)) {
				return_value = "UNKNOWN (" + id_user + ")";
			}
		}
		return return_value;
	}

	public void setSessionLanguage() {
		String myLang = loginUser.getParameterValue("UIL");
		this.setLanguage(myLang);
	}

	public String getSessionLanguage() {
		return loginUser.getParameterValue("UIL");
	}

	public void setSessionDateFormat() {
		String myDF = getOneValueByNoneId("SELECT dateformat FROM v_user_param_ln");
		this.setDateFormat(myDF);
		this.setDateFormatTitle(myDF);
	}

	public String getUIUserParam(String cd_param) {
		String returnValue = "";
		returnValue = getOneValueByStringId2("SELECT value_param FROM "
				+ getGeneralDBScheme()
				+ ".v_user_param_ui WHERE UPPER(cd_param)=UPPER(?)", cd_param);
		LOGGER.debug("getUIUserParam('"+cd_param+"')=" + returnValue);
		return returnValue;
	}

	public String decodeUtf(String value) {
		String returnValue = "";
		byte[] array = new byte[value.length()];

		for (int counter = 0; counter < value.length(); counter++) {
			array[counter] = (byte) value.charAt(counter);
		}
		try {
			ByteArrayInputStream bais = new ByteArrayInputStream(array);
			InputStreamReader isr = new InputStreamReader(bais, "UTF-8");
			BufferedReader reader = new BufferedReader(isr);
			returnValue = reader.readLine();
		} catch (UnsupportedEncodingException e) {
			LOGGER.debug("bcBase.decodeUtf() UnsupportedEncodingException: "
					+ e.toString());
			e.printStackTrace();
		} catch (Exception ex) {
			LOGGER.debug("bcBase.decodeUtf() Exception: " + ex.toString());
		}
		if (isEmpty(returnValue) || 
				"null".equalsIgnoreCase(returnValue) || 
				"&nbsp;".equalsIgnoreCase(returnValue)) {
			returnValue = "";
		}
		return returnValue;
	}

	public String getHyperLinkFirst() {
		return "<div class=\"div_button\" onclick=\"ajaxpage('";
	}

	public String getHyperLinkMiddle() {
		return "', 'div_main')\">";
	}

	public String getHyperLinkEnd() {
		return "</div>";
	}


	public String getTopMenu(String pTitle, String pByttonType1,
			String pHyperLink1, String pByttonType2, String pHyperLink2,
			String pByttonType3, String pHyperLink3, String pByttonType4,
			String pHyperLink4, String pByttonType5, String pHyperLink5,
			String pByttonType6, String pHyperLink6, String pByttonType7,
			String pHyperLink7) {
		StringBuilder html = new StringBuilder();

		return html.toString();
	}

	public String getTableMenuParam() {
		return " class=\"form-header\"";
	}

	public String getTableMenuFilter() {
		return " class=\"form-header-filter\"";
	}

	public String getTableBottomParam() {
		return " class=\"tablebottom\"";
	}

	public String getTableBottomFilter() {
		return " class=\"tablebottom-filter\"";
	}

	public String getTableDetailParam() {
		return " class=\"tabledetail\"";
	}

	public String getTableDetail2Param() {
		return " class=\"tabledetail tabledetail2\"";
	}

	public String getTableReportParam() {
		return " class=\"tablereport\"";
	}

	public String getTableDetailOperationsParam() {
		return " class=\"tabledetailoperations\"";
	}

	public String getHeaderName() {
		String lHeader = "";
		if (isEmpty(currentMenu.getTitleMenuElement())) {
			lHeader = "";
		} else {
			lHeader = currentMenu.getTitleMenuElement();
		}
		return lHeader;
	}

	public String getColoredScript() {
		StringBuilder html = new StringBuilder();

		html.append("<script>\n");
		html.append("	var currentColor = '';\n");
		html.append("	var mouseOverColor = '#F3F3F3';\n");
		html.append("	var mouseOutColor = '#FFFFFF';\n");
		html.append("\n");
		html.append("	function colored(theCells,theColor){\n");
		html.append("		var rowCellsCnt  = theCells.length;\n");
		html.append("		for (var c = 0; c < rowCellsCnt; c++) {\n");
		html.append("			theCells[c].style.backgroundColor = theColor;\n");
		html.append("		}\n");
		html.append("	}\n");
		html.append("\n");
		html.append("	function coloredOver(theRow,stopColor) {\n");
		html.append("		var theCells = null;\n");
		html.append("		if (typeof(document.getElementsByTagName) != 'undefined') {theCells = theRow.getElementsByTagName('td');}\n");
		html.append("		else if (typeof(theRow.cells) != 'undefined') {theCells = theRow.cells;}\n");
		html.append("		else {return false;}\n");
		html.append("		if (stopColor && theRow.ismarked==\"1\"){return;}\n");
		html.append("		currentColor = theCells[1].style.backgroundColor;\n");
		html.append("		colored(theCells,mouseOverColor);\n");
		html.append("	}\n");
		html.append("\n");
		html.append("	function coloredOut(theRow,stopColor) {\n");
		html.append("		var theCells = null;\n");
		html.append("		if (typeof(document.getElementsByTagName) != 'undefined') {theCells = theRow.getElementsByTagName('td');}\n");
		html.append("		else if (typeof(theRow.cells) != 'undefined') {theCells = theRow.cells;}\n");
		html.append("		else {return false;}\n");
		html.append("		if (stopColor && theRow.ismarked==\"1\"){return;}\n");
		html.append("		colored(theCells,currentColor);\n");
		html.append("	}\n");
		html.append("</script>\n");
		html.append("\n");

		return html.toString();
	}

	public String getPageHeaderPrint() {
		StringBuilder html = new StringBuilder();

		html.append(getColoredScript());
		html.append("<td>" + currentMenu.getTitleMenuElement() + "</td>\n");

		return html.toString();
	}

	public String getPageHeaderPrint(String pTitle) {
		StringBuilder html = new StringBuilder();

		html.append(getColoredScript());
		html.append("<td>" + pTitle + "</td>\n");

		return html.toString();
	}

	public String getDetailCaption(String pCaption) {
		StringBuilder html = new StringBuilder();
		String capt = pCaption;
		if (capt.length() > 100) {
			capt = capt.substring(1, 100) + "...";
		}
		html.append("<tr><td align=\"left\"><i>" + capt + "</i></td></tr>");
		return html.toString();
	}

	public String getFindHTML(String pFindElement, String pFindData,
			String pHyperLink) {
		StringBuilder html = new StringBuilder();
		html.append("<td width=\"20\">");
		html.append("<input type=\"text\" name=\"" + pFindElement + "\" id=\""
				+ pFindElement + "\" size=\"20\" value=\"" + pFindData
				+ "\" class=\"inputfield\" title=\""
				+ buttonXML.getfieldTransl( "find_string", false)
				+ "\">");
		html.append("</td>");
		html.append("<td width=\"15\">");
		html.append("<button type=\"button\" id=\"find_button\" onclick=\"ajaxpage('"
				+ pHyperLink
				+ pFindElement
				+ "='+getElementById('"
				+ pFindElement
				+ "').value, 'div_main')\">"
				+ buttonXML.getfieldTransl( "find", false)
				+ "</button>");
		html.append("<script type=\"text/javascript\">");
		html.append("AjaxUtility.TextField(\"" + pFindElement
				+ "\",\"find_button\", \"" + pHyperLink
				+ "\",\"" + pFindElement + "\");");
		html.append("</script>");
		html.append("</td><td></td>");
		//LOGGER.debug(html.toString());
		return html.toString();
	}

	public String getFindHTML(String pFindElement, String pFindData,
			String pHyperLink, String pDivName) {
		StringBuilder html = new StringBuilder();
		html.append("<td width=\"20\">");
		html.append("<input type=\"text\" name=\"" + pFindElement + "\" id=\""
				+ pFindElement + "\" size=\"20\" value=\"" + pFindData
				+ "\" class=\"inputfield\" title=\""
				+ buttonXML.getfieldTransl( "find_string", false)
				+ "\">");
		html.append("</td>");
		html.append("<td width=\"15\">");
		html.append("<button type=\"button\" id=\"find_button\" onclick=\"ajaxpage('"
				+ pHyperLink
				+ pFindElement
				+ "='+getElementById('"
				+ pFindElement
				+ "').value, '"
				+ pDivName
				+ "')\">"
				+ buttonXML.getfieldTransl( "find", false)
				+ "</button>");
		html.append("<script type=\"text/javascript\">");
		html.append("AjaxUtility.TextField2(\"" + pFindElement
				+ "\",\"find_button\", \"" + pHyperLink
				+ "\",\"" + pFindElement + "\",\"" + pDivName + "\");");
		html.append("</script>");
		html.append("</td><td></td>");
		//LOGGER.debug(html.toString());
		return html.toString();
	}

	public String getButtonAjax(String pHyperLink, String pButton, String pTitle) {
		StringBuilder html = new StringBuilder();
		html.append("<button type=\"button\" class=\"button\" title=\""
				+ pTitle + "\" onclick=\"ajaxpage('" 
				+ pHyperLink + "', 'div_main')\">"
				+ buttonXML.getfieldTransl( pButton, false)
				+ "</button>");
		return html.toString();
	}

	public String getGoBackButton(String pHyperLink) {
		return getGoBackButton(pHyperLink, "button_back", "");
	}

	public String getGoBackButton(String pHyperLink, String pButtonName) {
		return getGoBackButton(pHyperLink, pButtonName, "");
	}

	public String getGoBackButton(String pHyperLink, String pButtonName, String pDivName) {
		StringBuilder html = new StringBuilder();
		String lButton = isEmpty(pButtonName)?"cancel":pButtonName;
		html.append("<button type=\"button\" class=\"button\" onclick=\"ajaxpage('"
				+ pHyperLink
				+ "', '" + (isEmpty(pDivName)?"div_main":pDivName) + "')\">"
				+ buttonXML.getfieldTransl( lButton, false)
				+ "</button>");
		return html.toString();
	}

	public String getSubmitButton2() {
		StringBuilder html = new StringBuilder();
		html.append("<button type=\"submit\" class=\"button\">"
				+ buttonXML.getfieldTransl( "submit", false)
				+ "</button>");
		return html.toString();
	}

	public String getSubmitButton() {
		StringBuilder html = new StringBuilder();
		html.append("<button type=\"submit\" class=\"button\">"
				+ buttonXML.getfieldTransl( "submit", false)
				+ "</button>");
		return html.toString();
	}

	public String getSubmitButton(String pHyperLink) {
		return getSubmitButton(pHyperLink, "submit");
	}

	public String getSubmitButton(String pHyperLink, String pButton) {
		StringBuilder html = new StringBuilder();
		String lHyperLink = pHyperLink.trim();
		String lEndChar = lHyperLink.substring(lHyperLink.length() - 5);
		if (!("value".equalsIgnoreCase(lEndChar.trim()))) {
			lHyperLink = lHyperLink + "' ";
		}
		html.append("<button type=\"button\" class=\"button\" onclick=\" try {if (!myValidateForm()) {return false;} } catch(err){try {if (!validateForm(formData)) {return false;} } catch(err){} } ajaxpage('"
				+ lHyperLink
				+ ",'div_main')\">"
				+ buttonXML.getfieldTransl( pButton, false)
				+ "</button>");
		return html.toString();

	}

	public String getSubmitHyperLink(String pHyperLink, String pCaption) {
		return getSubmitHyperLink(pHyperLink, pCaption, null);
	}

	public String getSubmitHyperLink(String pHyperLink, String pCaption,
			String pConfirm) {
		StringBuilder html = new StringBuilder();
		String lHyperLink = pHyperLink.trim();
		String lEndChar = lHyperLink.substring(lHyperLink.length() - 5);
		String lAjaxExec = "ajaxpage('" + lHyperLink
				+ "','div_main');";
		String lConfirmScript = "";
		if (!("value".equalsIgnoreCase(lEndChar.trim()))) {
			lHyperLink = lHyperLink + "' ";
		}
		if (!isEmpty(pConfirm)) {
			lConfirmScript = "var msg='" + pConfirm
					+ "?';var res=window.confirm(msg); if (res) { " + lAjaxExec
					+ "}";
		} else {
			lConfirmScript = lAjaxExec;
		}
		html.append("<a href=\"#\" class=\"submitlink\" onclick=\""
				+ lConfirmScript + "\">" + pCaption + "</a>");
		return html.toString();

	}

	public String getSubmitButtonAjax(String pHyperLink) {
		return getSubmitButtonAjax(pHyperLink, "submit", "updateForm",
				"div_main", "myValidateForm");
	}

	public String getSubmitButtonAjax(String pHyperLink, String pTitle) {
		return getSubmitButtonAjax(pHyperLink, pTitle, "updateForm",
				"div_main", "myValidateForm");
	}

	public String getSubmitButtonAjax(String pHyperLink, String pTitle,
			String pFormName) {
		return getSubmitButtonAjax(pHyperLink, pTitle, pFormName, "div_main",
				"myValidateForm");
	}

	public String getSubmitButtonAjax(String pHyperLink, String pTitle,
			String pFormName, String pDivName) {
		return getSubmitButtonAjax(pHyperLink, pTitle, pFormName, pDivName,
				"myValidateForm");
	}

	public String getSubmitButtonAjax(String pHyperLink, String pTitle,
			String pFormName, String pDivName, String pValidateScriptName) {
		return getSubmitButtonAjax(pHyperLink, pTitle, pFormName, pDivName, pValidateScriptName, (String)null, true);		
	}
	
	public String getSubmitButtonAjax(String pHyperLink, String pTitle,
			String pFormName, String pDivName, String pValidateScriptName, String buttonId, boolean buttonDisable) {
		StringBuilder html = new StringBuilder();
		String lHyperLink = pHyperLink.trim();
		String lEnd5Char = lHyperLink.substring(lHyperLink.length() - 5);
		String lEndChar = lHyperLink.substring(lHyperLink.length() - 1);
		if ("?".equalsIgnoreCase(lEndChar) || "&".equalsIgnoreCase(lEndChar)) {
			lHyperLink = "'" + lHyperLink + "'";
		} else {
			if ("value".equalsIgnoreCase(lEnd5Char.trim())) {
				lHyperLink = "'" + lHyperLink + " + '&'";
			} else {
				lHyperLink = "'" + lHyperLink + "?'";
			}
		}
		if (!isEmpty(pFormName)) {
			lHyperLink = lHyperLink + " + mySubmitForm('"	+ pFormName+ "')";
		}
		String lValidateStr = "";
		if (!isEmpty(pValidateScriptName)) {
			lValidateStr = "try {if (!"	+ pValidateScriptName+(pValidateScriptName.endsWith(")")?"":"()") + ") {return false;} } catch(err)";
		}
		html.append("<button type=\"button\" class=\"button\" onclick=\""
				+ lValidateStr
				+ "{try {if (!validateForm(formData)) {return false;} } catch(err){} } ajaxpage("
				+ lHyperLink
				+ ",'"
				+ pDivName
				+ "'); " + (!buttonDisable?"this.disabled=false; this.className = 'button'; ":"")+ " \""
				+ ( (buttonId!=null)?" id=\""+buttonId.trim()+"\" ":"" )
				+ ">"
				+ buttonXML.getfieldTransl( pTitle, false)
				+ "</button>");
		return html.toString();
	}

	public String getSubmitButtonMultiPart(String pHyperLink, String pTitle,
			String pFormName) {
		StringBuilder html = new StringBuilder();
		html.append("<button type=\"button\" name=\"submit_button\" id=\"submit_button\" class=\"button\" onclick=\" try {if (!myValidateForm()) {return false;} } catch(err){try {if (!validateForm(formData)) {return false;} } catch(err){} } this.disabled=true; this.className = 'button_disable'; post_form('"
				+ pHyperLink
				+ "','"
				+ pFormName
				+ "') \">"
				+ buttonXML.getfieldTransl( pTitle, false)
				+ "</button>");
		return html.toString();
	}

	public String getSubmitButtonMultiPart(String pHyperLink, String pTitle,
			String pFormName, String pFirstOnClickEvent) {
		StringBuilder html = new StringBuilder();
		html.append("<button type=\"button\" name=\"submit_button\" id=\"submit_button\" class=\"button\" onclick=\""
				+ pFirstOnClickEvent
				+ "; try {if (!myValidateForm()) {return false;} } catch(err){try {if (!validateForm(formData)) {return false;} } catch(err){} } post_form('"
				+ pHyperLink
				+ "','"
				+ pFormName
				+ "') \">"
				+ buttonXML.getfieldTransl( pTitle, false)
				+ "</button>");
		return html.toString();
	}

	public String getSubmitButtonMultiPart2(String pHyperLink, String pTitle,
			String pFormName) {
		StringBuilder html = new StringBuilder();
		html.append("<button type=\"button\" name=\"submit_button\" id=\"submit_button\" class=\"button\" onclick=\"post_form('"
				+ pHyperLink
				+ "','"
				+ pFormName
				+ "') \">"
				+ buttonXML.getfieldTransl( pTitle, false)
				+ "</button>");
		return html.toString();
	}

	public String getSubmitButtonMultiPart3(String pHyperLink, String pTitle,
			String pFormName, String pLastScript) {
		StringBuilder html = new StringBuilder();
		html.append("<button type=\"button\" name=\"submit_button\" id=\"submit_button\" class=\"button\" onclick=\" try {if (!myValidateForm()) {return false;} } catch(err){try {if (!validateForm(formData)) {return false;} } catch(err){} } this.disabled=true; this.className = 'button_disable'; post_form('"
				+ pHyperLink
				+ "','"
				+ pFormName
				+ "')" + (!isEmpty(pLastScript)?";"+pLastScript:"")+" \">"
				+ buttonXML.getfieldTransl( pTitle, false)
				+ "</button>");
		return html.toString();
	}

	public String getSubmitButtonMultiPart4(String pHyperLink, String pTitle,
			String pFormName, String pDivName, String pLastScript) {
		StringBuilder html = new StringBuilder();
		html.append("<button type=\"button\" name=\"submit_button\" id=\"submit_button\" class=\"button\" onclick=\" try {if (!myValidateForm()) {return false;} } catch(err){try {if (!validateForm(formData)) {return false;} } catch(err){} } this.disabled=true; this.className = 'button_disable'; post_form('"
				+ pHyperLink
				+ "','"
				+ pFormName
				+ "','"
				+ pDivName 
				+ "')" + (!isEmpty(pLastScript)?";"+pLastScript:"")+" \">"
				+ buttonXML.getfieldTransl( pTitle, false)
				+ "</button>");
		return html.toString();
	}

	public String getResetButton() {
		StringBuilder html = new StringBuilder();
		// html.append("<button type=\"reset\" class=\"button\">" +
		// buttonXML.getfieldTransl("reset", false) +
		// "</button>");
		return html.toString();
	}

	public String getExecuteButton() {
		StringBuilder html = new StringBuilder();
		html.append("<button type=\"submit\" class=\"button\">"
				+ buttonXML.getfieldTransl( "execute", false)
				+ "</button>");
		return html.toString();
	}

	public String checkFindString(String pTag, String pValue, String pPage) {
		String lValue = pValue;
		if (lValue == null) {
			lValue = filtersHmGetValue(pTag);
			if (isEmpty(lValue)) {
				lValue = "";
			}
		} else {
			filtersHmSetValue(pTag, lValue);
		}
		return lValue;
	}

	public String checkFindString3(String pTag, String pValue, String pDefault,
			String pPage) {
		String lValue = pValue;
		if (lValue == null) {
			lValue = filtersHmGetValue2(pTag);
			if (lValue == null) {
				lValue = pDefault;
				filtersHmSetValue(pTag, lValue);
			}
		} else {
			filtersHmSetValue(pTag, lValue);
		}
		return lValue;
	}

	public void checkFindString2(String pTag, String pValue, String pPage) {
		pValue = checkFindString(pTag, pValue, pPage);
	}

	public String checkPrint(String pPring) {
		String returnValue = pPring;
		if (isEmpty(pPring)) {
			returnValue = "N";
		}
		return returnValue;
	}

	public HashMap<String, String> getUtfParameters(String query) {
		HashMap<String, String> returnValue = new HashMap<String, String>();
		try {
			StringTokenizer tokenizer = new StringTokenizer(query, "&");
			while (tokenizer.hasMoreTokens()) {
				String nextElement = tokenizer.nextToken();
				int index = nextElement.indexOf('=');
				if (index > 0) {
					returnValue.put(nextElement.substring(0, index),
							nextElement.substring(index + 1));
				}
			}
		} catch (Exception e) {

		}
		return returnValue;
	}

	public String getDecodeParam(String pParam) {
		String lValue = null;
		try {
			lValue = URLDecoder.decode(pParam, "UTF-8");
		} catch (Exception e) {
			lValue = null;
		}
		if (!isEmpty(lValue)) {
			lValue = lValue.replace("'", "''");
			lValue = lValue.replace("&nbsp;", "");
		}
		if ("null".equalsIgnoreCase(lValue)) {
			lValue = "";
		}
		return lValue;
	}

	public String getDecodeCheckBoxNumber(String pParam) {
		String lValue = getDecodeParamPrepare(pParam);
		if (!isEmpty(lValue)) {
			lValue = "1";
		}
		return lValue;
	}

	public String getDecodeParamPrepare(String pParam) {
		String lValue = getDecodeParam(pParam);
		if (isEmpty(lValue) || "null".equalsIgnoreCase(lValue)
				|| "empty".equalsIgnoreCase(lValue)) {
			lValue = "";
		}
		return lValue;
	}

	public String getDecodeAmountParam(String pParam) {
		String lValue = "";
		if (isEmpty(pParam) || "null".equalsIgnoreCase(pParam)
				|| "empty".equalsIgnoreCase(pParam)) {
			lValue = "";
		} else {
			lValue = getDecodeParam(pParam).replace(" ", "");
		}
		return lValue;
	}
	
	private String pDefaultAmountFormat = "###,###.00";

	public String formatAmount(String pAmount, String pFormat) {
		String lAmount = getDecodeAmountParam(pAmount);
		//System.out.println("formatAmount(1): pAmount="+pAmount+", lAmount="+lAmount);
		String lValue = "";
		if (!isEmpty(lAmount)) {
			DecimalFormat myFormatter = new DecimalFormat(pFormat);
			Double lTmp = new Double(lAmount.replace(",", "."));
			lValue = myFormatter.format(lTmp);
			//System.out.println("formatAmount(2): pAmount="+pAmount+", lTmp="+lTmp);
		}
		return lValue;
	}

	public String formatAmount(String pAmount) {
		return formatAmount(pAmount, pDefaultAmountFormat);
	}

	public String getSelectOnChangeBeginHTML(String pName, String pHyperLink) {
		return getSelectOnChangeBeginHTML(pName, pHyperLink, "");
	}

	public String getSelectOnChangeBeginHTML(String pName, String pHyperLink,
			String pTitle) {
		return getSelectOnChangeBeginHTML("", pName, pHyperLink, pTitle, "");
	}

	public String getSelectOnChangeBeginHTML(String pText, String pName,
			String pHyperLink, String pTitle, String pDivName) {
		String return_value = "";
		return_value = "";
		if (!isEmpty(pText)) {
			return_value = return_value + "<td align=\"right\">\n " + pText
					+ "&nbsp;&nbsp;</td>";
		}
		return_value = return_value + "<td align=\"right\" width=\"20\">\n "
				+ "<select onchange=\"ajaxpage('" 
				+ pHyperLink + "&" + pName
				+ "='+this.value, '"+(isEmpty(pDivName)?"div_main":pDivName)+"')\" name=\"" + pName + "\" id=\""
				+ pName + "\" class=\"inputfield\" ";
		if (!isEmpty(pTitle)) {
			return_value = return_value + "title=\"" + pTitle + "\"";
		}
		return_value = return_value + ">\n";
		return return_value;
	}

	public String getSelectOnChangeBegin2HTML(String pName, String pHyperLink,
			String pTitle) {
		String return_value = "";
		return_value = "<select onchange=\"ajaxpage('" 
				+ pHyperLink + "&" + pName
				+ "='+this.value, 'div_main')\" name=\"" + pName + "\" id=\""
				+ pName + "\" class=\"inputfield\" ";
		if (!isEmpty(pTitle)) {
			return_value = return_value + "title=\"" + pTitle + "\"";
		}
		return_value = return_value + ">\n";
		return return_value;
	}

	public String getSelectOnChangeEndHTML() {
		return "</select>\n</td>\n";
	}

	public String getSelectBeginHTML(String pName, String pTitle) {
		return getSelectBeginHTML(pName, pTitle, null);
	}

	public String getSelectBeginHTML(String pName, String pTitle, String pJavaScriptOnChange) {
		String return_value = "";
		return_value = "<select name=\"" + pName + "\" id=\"" + pName
				+ "\" class=\"inputfield\" ";
		if (!isEmpty(pTitle)) {
			return_value = return_value + " title=\"" + pTitle + "\"";
		}
		if (!isEmpty(pJavaScriptOnChange)) {
			return_value = return_value + " onchange=\"" + pJavaScriptOnChange + "\"";
		}
		return_value = return_value + ">\n";
		return return_value;
	}

	public String getSelectEndHTML() {
		return "</select>\n";
	}

	public String getSelectOptionHTML(String pCurrent, String pValue,
			String pCaption) {
		return getSelectOptionHTML(pCurrent, pValue, pCaption, "");
	}

	public String getSelectOptionHTML(String pCurrent, String pValue,
			String pCaption, String pStyle) {
		String returnValue = "";
		returnValue = "<option value=\"" + pValue + "\"";
		if (pValue.equalsIgnoreCase(pCurrent)) {
			returnValue = returnValue + " SELECTED ";
		}
		returnValue = returnValue + " " + pStyle + ">" + pCaption + "</option>\n";
		return returnValue;
	}

	public String getMessageLengthTextAreaEvent(String destinationElement) {
		String return_value = " onchange=\"getMessageLength(this,'"
				+ destinationElement + "');\""
				+ " onfocus=\"getMessageLength(this,'" + destinationElement
				+ "');\"" + " onkeyup=\"getMessageLength(this,'"
				+ destinationElement + "');\""
				+ " onmouseup=\"getMessageLength(this,'" + destinationElement
				+ "');\"";

		return return_value;
	}

	public String getMessageLengthTextAreaInitialScript(String sourceElement,
			String destinationElement) {
		String return_value = " <script>\n"
				+ "	getMessageLength(document.getElementById('" + sourceElement
				+ "'),'" + destinationElement + "');\n" + " </script>\n";

		return return_value;
	}

	public String getCountryOptions(String code_country, boolean isNull) {
		return getSelectBodyFromQuery2(
				" SELECT code_country, substr(name_country,1,25) name_country "
						+ "   FROM " + getGeneralDBScheme() + ".vc_country_all"
						+ "  WHERE exist_flag = 'Y' "
						+ "  ORDER BY name_country", code_country, isNull);
	}

	public String getCountryName(String cd_country) {
		return getOneValueByStringId2("SELECT name_country FROM "
				+ getGeneralDBScheme()
				+ ".vc_country_all WHERE code_country = ?", cd_country);
	}


	public String getOblastOptions(String cd_country, String id_oblast,
			boolean isNull) {
		ArrayList<bcFeautureParam> pParam = initParamArray();
		String lCodeCountry = cd_country;
		if (isEmpty(lCodeCountry)) {
			lCodeCountry = "-1";
		}
		pParam.add(new bcFeautureParam("string", lCodeCountry));

		return getSelectBodyFromParamQuery(" SELECT id_oblast, name_oblast "
				+ "   FROM " + getGeneralDBScheme() + ".vc_oblast_all "
				+ "  WHERE cd_country = ? " + "  ORDER BY id_oblast", pParam,
				id_oblast, "", isNull);
	}

	public String getTransTypeOptions(String cd_type, boolean isNull) {
		return getSelectBodyFromQuery(
				" SELECT cd_trans_type, name_trans_type," +
				"        DECODE(fcd_trans_type, " +
      	  		" 	 	     'REC_PAYMENT', ' style=\"font-weight:bold;color: black;\" ', " +
      	  		"            'REC_MOV_BON',  ' style=\"font-weight:bold;color: blue;\" ', " +
      	  		"            'REC_CHK_CARD',  ' style=\"font-weight:bold;color: green;\" ', " +
      	  		"            'REC_INVAL_CARD',  ' style=\"font-weight:bold;color: red;\" ', " +
      	  		"            'REC_STORNO_BON',  ' style=\"font-weight:bold;color: red;\" ', " +
      	  		"            'REC_PAYMENT_IM',  ' style=\"font-weight:bold;color: black;\" ', " +
      	  		"            'REC_PAYMENT_EXT',  ' style=\"font-weight:bold;color: black;\" ', " +
      	  		"          	 'REC_ACTIVATION', ' style=\"font-weight:bold;color: blue;\" ', " +
      	  		"            'REC_QUESTIONING',  ' style=\"font-weight:bold;color: blue;\" ', " +
      	  		"          	 'REC_PUT_CARD', ' style=\"font-weight:bold;color: #191970;\" ', " +
      	  		"          	 'REC_COUPON', ' style=\"font-weight:bold;color: #8B8989;\" ', " +
      	  		"          	 'REC_MEMBERSHIP_FEE', ' style=\"font-weight:bold;color: #8B3A3A;\" ', " +
      	  		"          	 'REC_POINT_FEE', ' style=\"font-weight:bold;color: #C8860B;\" ', " +
      	  		"          	 'REC_MTF', ' style=\"font-weight:bold;color: #B8860B;\" ', " +
  	  			"          	 'REC_SHARE_FEE', ' style=\"font-weight:bold;color: green;\" ', " +
      	  		"          	 'REC_TRANSFER_GET_POINT', ' style=\"font-weight:bold;color: #BA55D3;\" ', " +
      	  		"          	 'REC_TRANSFER_PUT_POINT', ' style=\"font-weight:bold;color: #BA55D3;\" ', " +
      	  		"          	 'REC_ENTRANCE_FEE', ' style=\"font-weight:bold;color: #191970;\" ', " +
      	  		//"          	 'REC_CANCEL', ' style=\"font-weight:bold;color: red;\" ', " +
      	  		//"          	 'REC_RETURN', ' style=\"font-weight:bold;color: red;\" ', " +
      	  		"          	 '' " +
      	  		"  	     ) option_style, exist_flag " + 
      	  		"   FROM " + getGeneralDBScheme() + ".vc_trans_type_all" + 
      	  		"  ORDER BY name_trans_type", cd_type, isNull,
				transType, false);
	}

	public String getTransTypeName(String cd_type) {
		return getOneValueByStringId2("SELECT name_trans_type FROM "
				+ getGeneralDBScheme()
				+ ".vc_trans_type_all WHERE cd_trans_type = ?", cd_type,
				transType);
	}

	public String getTransPayTypeOptions(String pay_type, boolean isNull) {
		return getSelectBodyFromQuery(
				" SELECT cd_trans_pay_type, name_trans_pay_type, " +
				" DECODE(cd_trans_pay_type, " +
	    		"        'CASH',  ' style=\"color: green;\" ', " +
	    		"        'BANK_CARD',  ' style=\"color: blue;\" ', " +
	    		"        'SMPU_CARD',  ' style=\"font-weight:bold; color: black;\" ', " +
	    		"        'INVOICE',  ' style=\"font-weight:bold; color: brown;\" ', " +
	    		"        'UNKNOWN',  ' style=\"font-weight:bold; color: red;\" ', " +
	    		"        ' style=\"color: red;\" ' " +
	    		" ) option_style, exist_flag "
						+ "   FROM " + getGeneralDBScheme()
						+ ".vc_trans_pay_type"
						+ "  ORDER BY DECODE(cd_trans_pay_type, 'CASH', 1, 'BANK_CARD', 2, 'SMPU_CARD', 3, 'INVOICE', 4, 'UNKNOWN', 99, 9), name_trans_pay_type", pay_type, isNull,
				transPayType, false);
	}

	public String getTransPayTypeName(String cd_type) {
		return getOneValueByStringId2("SELECT name_trans_pay_type FROM "
				+ getGeneralDBScheme()
				+ ".vc_trans_pay_type WHERE cd_trans_pay_type = ?", cd_type);
	}

	public String getTransStateOptions(String cd_state, boolean isNull) {
		return getSelectBodyFromQuery(
				" SELECT cd_trans_state, name_trans_state, " +
				" DECODE(cd_trans_state, " +
	    		"        -99,  ' style=\"color: gray;\" ', " +
	    		"        -55,  ' style=\"color: red;\" ', " +
	    		"        -54,  ' style=\"color: blue;\" ', " +
	    		"        -13,  ' style=\"font-weight:bold;color: red;\" ', " +
	    		"        -12,  ' style=\"font-weight:bold;color: red;\" ', " +
	    		"        -11,  ' style=\"font-weight:bold;color: red;\" ', " +
	    		"        -9,  ' style=\"color: gray;\" ', " +
	    		"        -7,  ' style=\"color: blue;\" ', " +
	    		"        -6,  ' style=\"color: blue;\" ', " +
	    		"        -4,  ' style=\"color: green;\" ', " +
	    		"        -2,  ' style=\"color: ligthred;\" ', " +
	    		"        -1,  ' style=\"color: darkbrown;\" ', " +
	    		"        0,  ' style=\"font-weight:bold;color: black;\" ', " +
	    		"        1,  ' style=\"font-weight:bold;color: red;\" ', " +
	    		"        5,  ' style=\"font-weight:bold;color: red;\" ', " +
	    		"        9,  ' style=\"font-weight:bold;color: red;\" ', " +
	    		"        '' " +
	    		" ) option_style, exist_flag "
						+ "   FROM " + getGeneralDBScheme()
						+ ".vc_trans_state_all"
						+ "  ORDER BY name_trans_state", cd_state, isNull,
				transState, false);
	}

	public String getTransStateName(String cd_state) {
		return getOneValueByStringId2("SELECT name_trans_state FROM "
				+ getGeneralDBScheme()
				+ ".vc_trans_state_all WHERE cd_trans_state = ?", cd_state,
				transState);
	}

	public String getTelgrExternalStateOptions(String cd_state, boolean isNull) {
		return getSelectBodyFromQuery(
				" SELECT cd_telgr_external_state, name_telgr_external_state, exist_flag "
						+ "   FROM " + getGeneralDBScheme()
						+ ".vc_telgr_external_state_all"
						+ "  ORDER BY name_telgr_external_state", cd_state, isNull,
						telgrExternalState, false);
	}

	public String getTelgrExternalStateName(String cd_state) {
		return getOneValueByStringId2("SELECT name_telgr_external_state FROM "
				+ getGeneralDBScheme()
				+ ".vc_telgr_external_state_all WHERE cd_telgr_external_state = ?", cd_state,
				telgrExternalState);
	}

	public String getClubCardStatusOptions(String id_status, boolean isNull) {
		return getSelectBodyFromQuery(
				" SELECT id_card_status, name_card_status, exist_flag "
						+ "   FROM " + getGeneralDBScheme() + ".vc_card_status"
						+ "  ORDER BY name_card_status", id_status, isNull,
				clubCardStatus, false);
	}

	public String getCardStatusName(String id_card_status) {
		return getOneValueByIntId2("SELECT name_card_status FROM "
				+ getGeneralDBScheme()
				+ ".vc_card_status WHERE id_card_status = ?", id_card_status,
				clubCardStatus);
	}

	public String getCurrencyOptions(String cd_currency, boolean isNull) {
		return getSelectBodyFromQuery2(" SELECT cd_currency, name_currency "
				+ "   FROM " + getGeneralDBScheme() + ".v_currency_all "
				+ "  WHERE is_used=1" + "  ORDER BY name_currency",
				cd_currency, isNull);
	}

	public String getCurrencyShortNameOptions(String cd_currency, boolean isNull) {
		return getSelectBodyFromQuery2(" SELECT cd_currency, sname_currency "
				+ "   FROM " + getGeneralDBScheme() + ".v_currency_all "
				+ "  WHERE is_used=1" + "  ORDER BY sname_currency",
				cd_currency, isNull);
	}

	public String getCurrencyNameById(String currency_id) {
		return getOneValueByIntId2("select name_currency FROM "
				+ getGeneralDBScheme()
				+ ".vc_currency_all WHERE cd_currency = ? ", currency_id);
	}

	public String getCurrencyShortNameById(String currency_id) {
		return getOneValueByIntId2("select sname_currency FROM "
				+ getGeneralDBScheme()
				+ ".vc_currency_all WHERE cd_currency = ? ", currency_id);
	}
	
	public String getTermPayConfirmationWayName(String cd_way) {
		return getOneValueByStringId2("SELECT name_term_pay_confirm_way FROM "
				+ getGeneralDBScheme()
				+ ".vc_term_pay_confirm_way_all WHERE cd_term_pay_confirm_way = ?", cd_way);
	}

	public String getTermPayConfirmationWayOptions(String cd_way, boolean isNull) {
		return getSelectBodyFromQuery(" SELECT cd_term_pay_confirm_way, name_term_pay_confirm_way "
				+ "   FROM " + getGeneralDBScheme() + ".vc_term_pay_confirm_way_all "
				+ "  ORDER BY name_term_pay_confirm_way",
				cd_way, isNull, termPayConfirmationWay, false);
	}

	public String getInputTextElement(String pElementName, String pTitle, String pValue, boolean readOnly, String pWidth) {
		StringBuilder html = new StringBuilder();
		
		html.append("<input type=\"text\" name=\"" + pElementName + "\" title = \"" + pTitle + "\" style=\"width:" + pWidth + "px !important;\"  value=\"" + pValue + "\" " + (readOnly?" readonly=\"readonly\" class=\"inputfield-ro\" ":" class=\"inputfield\" ") + ">");

		return html.toString();
	}

	public String getTextareaElement(String pName, String pTitle, String pValue, boolean readOnly, String pWidth, String pHeight) {
		StringBuilder html = new StringBuilder();
		
		html.append("<textarea name=\"" + pName + "\" title = \"" + pTitle + "\" style=\"width:" + pWidth + "px !important; height:" + pHeight + "px !important;\" " + (readOnly?" readonly=\"readonly\" class=\"inputfield-ro\" ":" class=\"inputfield\" ") + ">" + pValue + "</textarea>");

		return html.toString();
	}

	public String getSelectElement(String pElementName, String pTitle, String pCode, String pName, StringBuffer pSelectOptions, boolean pReadOnly, String pWidth) {
		StringBuilder html = new StringBuilder();
		if (pReadOnly) {
			String lName = pName;
			if (isEmpty(pName)) {
				lName = pCode;
			}
			html.append(getInputTextElement(pElementName, "", lName, pReadOnly, "150"));
		} else {
			html.append("<select name=\"" + pElementName + "\" class=\"inputfield\">");
			html.append(pSelectOptions.toString());
			html.append("</select>");
		}
		return html.toString();
	}

	public String getInputRadioElement(String pElementName, String pTitle, String pValue, String pStyle, boolean pChecked, boolean pDisabled) {
		StringBuilder html = new StringBuilder();
		
		html.append("<input type=\"radio\" name=\"" + pElementName
				+ "\" id=\"" + pElementName + "_" + pValue
				+ "\" value=\"" + pValue + "\" "
				+ (pDisabled?" DISABLED ":"") + " "
				+ (pChecked?" CHECKED ":"") + ">");
		html.append("<label class=\"checbox_label\" for=\""
				+ pElementName + "_" + pValue + "\" " 
				+ (!"".equalsIgnoreCase(pStyle)?" style=\"" + pStyle + "\" ":" ")
				+ ">" + pTitle + "</label>");

		return html.toString();
	}

	public String getInputRadioGroupElement(String pGroupName, String pElementName, String pTitle, String pValue, String pStyle, String pOnClickFunctions, boolean pChecked, boolean pDisabled) {
		StringBuilder html = new StringBuilder();
		
		html.append("<input type=\"radio\" name=\"" + pGroupName
				+ "\" id=\"" + pGroupName + "_" + pElementName + "_" + pValue
				+ "\" value=\"" + pValue + "\" "
				+ (!isEmpty(pOnClickFunctions)?" onclick=\"javascript:" + pOnClickFunctions + "\"":"") + " "
				+ (pDisabled?" DISABLED ":"") + " "
				+ (pChecked?" CHECKED ":"") + ">");
		html.append("<label class=\"checbox_label\" for=\""
				+ pGroupName + "_" + pElementName + "_" + pValue + "\" " 
				+ (!"".equalsIgnoreCase(pStyle)?" style=\"" + pStyle + "\" ":" ")
				+ ">" + pTitle + "</label>");

		return html.toString();
	}

	
}