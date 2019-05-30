package webpos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import bc.connection.Connector;
import bc.service.bcFeautureParam;

public class wpChequeObject extends wpObject {
	private final static Logger LOGGER=Logger.getLogger(wpChequeObject.class);
	private Map<String,Comparable<String>> fieldHm = new HashMap<String, Comparable<String>>();
	
	/*
	 * idTransGroup не пустая, если операция - композитная
	 * */
	private String idTelgr;
	private String idTrans;
	private String reportFormat = "HTML";
	private String saveFormat = "";
	private String canSaveCheque = "N";
	//private String canSMSCheque = "N";
	
	int TXTLineMaxLength = 60;
	private String pFirstCellStyle = " style=\"width:220px;\" ";
	private String pSecondCellStyle = " style=\"width:180px;\" ";
	private static final String NEWLINE = System.getProperty("line.separator");
	
	public wpChequeObject() {	}
	
	public wpChequeObject(String pIdTelgr, String pSaveFormat, wpTerminalObject term) {
		this.idTelgr = pIdTelgr;
		this.idTrans = "";
		this.saveFormat = pSaveFormat;
		this.canSaveCheque = term.getValue("OPER_SAVE_CHEQUE");
		//this.canSMSCheque = term.getValue("OPER_SMS_CHEQUE");
		this.getFeature();
	}
	
	public wpChequeObject(String pIdTelgr, String pIdTrans, String pSaveFormat, wpTerminalObject term) {
		this.idTelgr = pIdTelgr;
		this.idTrans = pIdTrans;
		this.saveFormat = pSaveFormat;
		this.canSaveCheque = term.getValue("OPER_SAVE_CHEQUE");
		//this.canSMSCheque = term.getValue("OPER_SMS_CHEQUE");
		this.getFeature();
	}
	
	private void getFeature() {
		String featureSelect = " SELECT * FROM " + getGeneralDBScheme() + ".vp$telgr_all WHERE id_telgr = ?";
		fieldHm = getFeatures2(featureSelect, this.idTelgr, false);
	}
	
	public String getValue(String pColumnName) {
		return getFeautureResult(fieldHm, pColumnName);
	}
	
	private String formatLine(String pColSpan, boolean pCenter, String pFirstClass, String pFirstValue, String pSecondClass, String pSecondValue) {
		StringBuilder html = new StringBuilder();
		String lFirstValue = pFirstValue;
		String lSecondValue = pSecondValue;
		
		if ("HTML".equalsIgnoreCase(reportFormat)) {
			if ("2".equalsIgnoreCase(pColSpan)) {
				html.append("<tr><td colspan=\"2\" class=\"" + pFirstClass + "\">" + lFirstValue + "</td></tr>" + NEWLINE);
			} else {
				html.append("<tr><td " + pFirstCellStyle + " class=\"" + pFirstClass + "\">" + lFirstValue + "</td>" + "<td " + pSecondCellStyle + " class=\"" + pSecondClass + "\">" + lSecondValue + "</td></tr>" + NEWLINE);
			}
		} else if ("TXT".equalsIgnoreCase(reportFormat)) {
			String leftEmpty = "";
			lFirstValue = lFirstValue.replace("&nbsp;", " ").replace("&quot;", "\"");
			lFirstValue = lFirstValue.toUpperCase();
			lSecondValue = lSecondValue.replace("&nbsp;", " ").replace("&quot;", "\"");
			lSecondValue = lSecondValue.toUpperCase();
			if (isEmpty(lSecondValue)) {
				if (pCenter) {
					for (int i=1; i<Math.round((TXTLineMaxLength - lFirstValue.length())/2); i++) {
						leftEmpty = leftEmpty + " ";
					}
				}
				html.append(leftEmpty + lFirstValue + NEWLINE);
			} else {
				if (lFirstValue.length() + lSecondValue.length() >= TXTLineMaxLength) {
					html.append(lFirstValue + " " + lSecondValue + NEWLINE);
				} else {
					for (int i=1; i<(TXTLineMaxLength - lFirstValue.length() - lSecondValue.length()); i++) {
						leftEmpty = leftEmpty + " ";
					}
					html.append(lFirstValue + leftEmpty + lSecondValue + NEWLINE);
				} 
			}
		}
		return html.toString();
	}
	private String formatXMLString(int pLevel, String pString) {
		StringBuilder html = new StringBuilder();
		String lEmpty = "";
		String lOneValue = "";
		if ("XML".equalsIgnoreCase(reportFormat)) {
			for (int i=1; i<pLevel*4; i++) {
				lEmpty = lEmpty + " ";
			}
			if (!isEmpty(pString)) {
				lOneValue = lOneValue + pString;
			}
			html.append(prepareFromHTML(lOneValue) + NEWLINE);
		}
		return html.toString();
	}
	
	private String formatXMLSectionBeg(int pLevel, String pSectionName, String pParam) {
		StringBuilder html = new StringBuilder();
		String lEmpty = "";
		String lOneValue = "";
		if ("XML".equalsIgnoreCase(reportFormat)) {
			for (int i=1; i<pLevel*4; i++) {
				lEmpty = lEmpty + " ";
			}
			lOneValue = lEmpty + "<" + pSectionName;
			if (!isEmpty(pParam)) {
				lOneValue = lOneValue + " " + pParam;
			}
			lOneValue = prepareFromHTML(lOneValue) + ">" + NEWLINE;
			html.append(lOneValue);
		}
		return html.toString();
	}
	
	private String formatXMLSectionEnd(int pLevel, String pSectionName) {
		StringBuilder html = new StringBuilder();
		String lEmpty = "";
		String lOneValue = "";
		if ("XML".equalsIgnoreCase(reportFormat)) {
			for (int i=1; i<pLevel*4; i++) {
				lEmpty = lEmpty + " ";
			}
			lOneValue = lEmpty + "</" + pSectionName + ">" + NEWLINE;
			html.append(prepareFromHTML(lOneValue));
		}
		return html.toString();
	}
	
	private String formatXMLElement(int pLevel, String pElement, String pValue, String pParam) {
		StringBuilder html = new StringBuilder();
		String lEmpty = "";
		String lOneValue = "";
		if ("XML".equalsIgnoreCase(reportFormat)) {
			for (int i=1; i<pLevel*4; i++) {
				lEmpty = lEmpty + " ";
			}
			lOneValue = lEmpty + "<" + pElement;
			if (!isEmpty(pParam)) {
				lOneValue = lOneValue + " " + pParam;
			}
			if (!isEmpty(pValue)) {
				lOneValue = lOneValue + ">" + pValue + "</"+pElement+">" + NEWLINE;
			} else {
				lOneValue = lOneValue + "></"+pElement+">" + NEWLINE;
			}
			html.append(prepareFromHTML(lOneValue));
		}
		return html.toString();
	}
	
	private String formatLineCenter(String pValue) {
		return formatLine("2", true, "centerb", pValue, "", "");
	}
	
	/*private String formatLine2CellBNoBold(String pFirstValue, String pSecondValue) {
		return formatLine("", false, "left", pFirstValue, "right", pSecondValue);
	}*/
	
	private String formatLine2Cell(String pFirstValue, String pSecondValue) {
		return formatLine("", false, "left", pFirstValue, "right", pSecondValue);
	}
	
	private String formatLine2CellB(String pFirstValue, String pSecondValue) {
		return formatLine("", false, "leftb", pFirstValue, "rightb", pSecondValue);
	}
	
	/*
	private String formatTXTEmptyLine() {
		return "\n";
	}
	
	private String formatTXTLineCenter(String pValue) {
		String lValue = pValue.trim();
		String leftEmpty = "";
		
		if (lValue.length() > TXTLineMaxLength) {
			
		} else if (lValue.length() < TXTLineMaxLength) {
			for (int i=1; i<Math.round((TXTLineMaxLength - lValue.length())/2)-1; i++) {
				leftEmpty = leftEmpty + " ";
			}
			lValue = leftEmpty + lValue + "\n";
		} 
		return lValue;
	}
	
	private String formatTXTLine2Cell(String pFirstValue, String pSecondValue) {
		String lFirst = pFirstValue;
		String lSecond = pSecondValue;
		String leftEmpty = "";
		StringBuilder lRes = new StringBuilder();
		
		if (isEmpty(lSecond)) {
			lRes.append(lFirst + "\n");
		}
		if (lFirst.length() + lSecond.length() >= TXTLineMaxLength) {
			lRes.append(lFirst + " " + lSecond + "\n");
		} else {
			for (int i=1; i<(TXTLineMaxLength - lFirst.length() - lSecond.length()); i++) {
				leftEmpty = leftEmpty + " ";
			}
			lRes.append(lFirst + leftEmpty + lSecond + "\n");
		} 
		return lRes.toString();
	}
	*/

	public String getFileName(String pChequeFormat) {
		StringBuilder fileName = new StringBuilder();
		
		String idFileName = idTelgr;
		if ("TXT".equalsIgnoreCase(pChequeFormat)) {
			fileName.append("smpu_cheque_" + idFileName + "_" + this.getSysDateTime().replace(":", "").replace(" ", "").replace("/", "").replace(".", "") + ".txt");
		} else {
			fileName.append("smpu_cheque_" + idFileName + "_" + this.getSysDateTime().replace(":", "").replace(" ", "").replace("/", "").replace(".", "") + ".xml");
		}
		
		return fileName.toString();
	}
	
	public String getChequeSaveButton() {
		if ("Y".equalsIgnoreCase(canSaveCheque)) {
			return "<input type=\"button\" class=\"button button_small\" onclick=\"JavaScript: var cWin=window.open('report/chequesave.jsp?id_telgr=" + this.idTelgr + "&cheque_format=" + this.saveFormat + "','blank','height=600,width=900,top=50,left=150,toolbar=no,menubar=no,location=no,scrollbars=yes,status=yes'); cWin.focus();\" value=\"" + buttonXML.getfieldTransl("button_save", false) +"\" />";
		} else {
			return "";
		}
	}
	
	public String getChequeAllButtons(boolean onCheque, boolean hasStornoMenuPermission) {
		StringBuilder result = new StringBuilder();
		result.append("<script language=\"javascript\">\n");
		result.append("function validateSendCheque(){\n");
		result.append("send_email = document.getElementById('send_EMAIL');\n");
		result.append("send_sms = document.getElementById('send_SMS');\n");
		result.append("if (send_email.checked || send_sms.checked) {\n");
		result.append("document.getElementById('div_result').innerHTML = '';\n");
		result.append("return true;\n");
		result.append("} else {\n");
		result.append("alert('" + webposXML.getfieldTransl("send_cheque_select_options_error", false) + "');\n");
		result.append("return false;\n");
		result.append("}\n");
		result.append("}\n");
		result.append("</script>\n");

		result.append("<a class=\"button button_small\" style=\"height: 22px !important; width: 120px !important; display: block; text-decoration: none; padding-top:5px\" href=\"#openModal2\">" + webposXML.getfieldTransl("title_cheque_actions", false) + "</a>\n");
		result.append("<div class=\"modalDialog\" id=\"openModal2\">\n");;
		result.append("<div>\n");
		result.append("<a class=\"close\" title=\"" + buttonXML.getfieldTransl("close", false) + "\" href=\"#close\">X</a>\n");
		result.append("<form name=\"updateForm21\" id=\"updateForm21\" accept-charset=\"UTF-8\" method=\"POST\">\n");
		result.append("<input type=\"hidden\" name=\"id_term\" value=\""+this.getValue("ID_TERM")+"\">\n");
		result.append("<input type=\"hidden\" name=\"cd_card1\" value=\""+this.getValue("CD_CARD1")+"\">\n");
		result.append("<input type=\"hidden\" name=\"storno_rrn\" value=\""+this.getValue("RRN")+"\">\n");
		result.append("<input type=\"hidden\" name=\"id_telgr\" value=\"" + this.idTelgr + "\">\n");
		result.append("<table class=\"tablebottom\"><thead></thead><tbody>\n");
		
		//String cdTransType = this.getValue("FCD_TRANS_TYPE");
		//boolean isNotCancelOrReturnTrans = true;
		//if ("REC_CANCEL".equalsIgnoreCase(cdTransType) || "REC_RETURN".equalsIgnoreCase(cdTransType)) {
		//	isNotCancelOrReturnTrans = false;
		//}
		
		if (!onCheque /*&& isNotCancelOrReturnTrans*/) {
			if (hasStornoMenuPermission) {
				result.append("<tr><td align=\"left\" style=\"padding-top: 5px;\"><font style=\"font-weight: bold; color: blue; align:center; font-size: 13px;\">" + webposXML.getfieldTransl("title_storno", false) + "</font></td></tr>\n");
				result.append("<tr><td align=\"left\" style=\"padding-bottom: 5px;\"><button type=\"button\" class=\"button button_small\" onclick=\"ajaxpage('action/storno.jsp?id_telgr="+this.getValue("ID_TELGR") + "&storno_rrn="+this.getValue("RRN")+"&back_type=operations&' +  mySubmitForm('updateForm21'),'div_main'); this.disabled=false; this.className = 'button'; \" class=\"button\" type=\"button\">" + buttonXML.getfieldTransl("cancel", false) + "</button></td></tr>\n");
			} else {
				result.append("<tr><td align=\"left\" style=\"padding-top: 5px;\"><font style=\"font-weight: bold; color: blue; align:center; font-size: 13px;\">" + webposXML.getfieldTransl("title_storno", false) + "</font></td></tr>\n");
				result.append("<tr><td align=\"left\" style=\"padding-bottom: 5px;\"><font style=\"font-weight: bold; color: red; \">" + webposXML.getfieldTransl("operation_permission_denied", false) + "</font></td></tr>\n");
			}
		}
		
		if (onCheque) {
			result.append("<tr><td align=\"left\" style=\"padding-top: 5px;\"><font style=\"font-weight: bold; color: blue; align:center; font-size: 13px;\">" + webposXML.getfieldTransl("title_print_cheque", false) + "</font></td></tr>\n");
			result.append("<tr><td align=\"left\" style=\"padding-bottom: 5px;\"><input type=\"button\" class=\"button button_small\" onclick=\"printDiv('printableArea')\" value=\"" + buttonXML.getfieldTransl("button_print", false) + "\" /></td></tr>\n");
		} else {
			result.append("<tr><td align=\"left\" style=\"padding-top: 5px; "+/*(isNotCancelOrReturnTrans?"border-top: 1px dashed gray;":"")+*/"\"><font style=\"font-weight: bold; color: blue; align:center; font-size: 13px;\">" + webposXML.getfieldTransl("title_print_cheque", false) + "</font></td></tr>\n");
			result.append("<tr><td align=\"left\" style=\"padding-bottom: 5px;\">" + this.getChequePrintButton() + "</td></tr>\n");
		}
		result.append("<tr><td align=\"left\" style=\"padding-top: 5px; border-top: 1px dashed gray;\"><font style=\"font-weight: bold; color: blue; align:center; font-size: 13px;\">" + webposXML.getfieldTransl("title_save_cheque", false) + "</font></td></tr>\n");
		if (onCheque) {
			result.append("<tr><td align=\"left\" style=\"text-transform:none;\">" + webposXML.getfieldTransl("save_cheque_hint", false) + "</td></tr>\n");
		}
		result.append("<tr><td align=\"left\" style=\"text-transform:none;\">Имя файла:&nbsp;<b style=\"color:green;\">" + getFileName(this.saveFormat) + "</b></td></tr>\n");
		//result.append("<tr><td style=\"text-transform:none;\">" + webposXML.getfieldTransl("title_cheque_save_format", false) + ":&nbsp;<b style=\"color:green;\">" + this.saveFormat + "</b></td></tr>\n");
		if (onCheque) {
			result.append("<tr><td align=\"left\" style=\"padding-bottom: 5px;\">" + getChequeSaveButtonOnCheque() + "</td></tr>\n");
		} else {
			result.append("<tr><td align=\"left\" style=\"padding-bottom: 5px;\">" + getChequeSaveButton() + "</td></tr>\n");
		}
		if (!onCheque) {
			result.append("<tr><td align=\"left\" style=\"padding-top: 5px; border-top: 1px dashed gray;\"><font style=\"font-weight: bold; color: blue; align:center; font-size: 13px;\">" + webposXML.getfieldTransl("title_check_card", false) + "</font></td></tr>\n");
			result.append("<tr><td align=\"left\" style=\"padding-bottom: 5px;\"><button type=\"button\" class=\"button button_small\" onclick=\"ajaxpage('service/check_card.jsp?' +  mySubmitForm('updateForm21'),'div_action_big'); this.disabled=false; this.className = 'button'; \" class=\"button\" type=\"button\">" + buttonXML.getfieldTransl("button_check", false) + "</button></td></tr>\n");
		}
		boolean canSendEmail = false;
		boolean canSendSMS = false;
		
		result.append("<tr><td align=\"left\" style=\"padding-top: 5px; border-top: 1px dashed gray;\"><font style=\"font-weight: bold; color: blue; align:center; font-size: 13px;\">" + webposXML.getfieldTransl("title_send_cheque", false) + "</font></td></tr>\n");
		result.append("<tr><td align=\"left\"><font style=\"font-weight: bold; color: green; align:center;\">" + webposXML.getfieldTransl("title_send_cheque_email", false) + "</font></td></tr>\n");
		if (!isEmpty(this.getValue("EMAIL_NAT_PRS"))) {
			canSendEmail = true;
			
			result.append("<tr><td align=\"left\" style=\"text-transform:none;\"><input type=\"checkbox\" value=\"Y\" id=\"send_EMAIL\" name=\"send_EMAIL\" class=\"inputfield\"><label for=\"send_EMAIL\">" + webposXML.getfieldTransl("send_cheque_email_action", false) + "<label></td></tr>\n");
			result.append("<tr><td align=\"left\" style=\"text-transform:none;\">" + webposXML.getfieldTransl("send_cheque_email_hint", false) + " <b style=\"color:green;\">" + this.getValue("EMAIL_NAT_PRS_HIDE") + "</b></td></tr>\n");
		} else {
			result.append("<tr><td align=\"left\" style=\"text-transform:none;\"><font style=\"color: red;\"><input type=\"checkbox\" style=\"visibility: hidden\" value=\"N\" id=\"send_EMAIL\" name=\"send_EMAIL\">" + webposXML.getfieldTransl("send_cheque_email_error", false) + "</font></td></tr>\n");
		}

		result.append("<tr><td align=\"left\" style=\"padding-top: 5px;\"><font style=\"font-weight: bold; color: green; align:center;\">" + webposXML.getfieldTransl("title_send_cheque_sms", false) + "</font></td></tr>\n");
		if (!isEmpty(this.getValue("PHONE_MOBILE_NAT_PRS"))) {
			if (isEmpty(this.getValue("SMS_CHEQUE_SEND_COUNT")) || "0".equalsIgnoreCase(this.getValue("SMS_CHEQUE_SEND_COUNT"))) {
				canSendSMS = true;
				
				result.append("<tr><td align=\"left\" style=\"text-transform:none;\"><input type=\"checkbox\" value=\"Y\" id=\"send_SMS\" name=\"send_SMS\" class=\"inputfield\"><label for=\"send_SMS\">" + webposXML.getfieldTransl("send_cheque_sms_action", false) + "<label></td></tr>\n");
				result.append("<tr><td align=\"left\" style=\"text-transform:none;\">" + webposXML.getfieldTransl("send_cheque_sms_hint", false) + " <b style=\"color:green;\">" + this.getValue("PHONE_MOBILE_NAT_PRS_HIDE") + "</b></td></tr>\n");
			} else {
				result.append("<tr><td align=\"left\" style=\"text-transform:none;\"><font style=\"color: red;\"><input type=\"checkbox\" style=\"visibility: hidden\" value=\"N\" id=\"send_SMS\" name=\"send_SMS\">" + webposXML.getfieldTransl("send_cheque_sms_already_send", false) + " "+this.getValue("PHONE_MOBILE_NAT_PRS_HIDE")+"</font></td></tr>\n");
			}
		} else {
			result.append("<tr><td align=\"left\" style=\"text-transform:none;\"><font style=\"color: red;\"><input type=\"checkbox\" style=\"visibility: hidden\" value=\"N\" id=\"send_SMS\" name=\"send_SMS\">" + webposXML.getfieldTransl("send_cheque_sms_error", false) + "</font></td></tr>\n");
		}
		if (canSendEmail || canSendSMS) {
			result.append("<tr><td align=\"left\"><div id=\"div_result\"><button type=\"button\" class=\"button button_small\" onclick=\" try {if (!validateSendCheque()) {return false;} else {ajaxpage('" + (onCheque?"chequesend.jsp":"report/chequesend.jsp") + "?' +  mySubmitForm('updateForm21'),'div_result'); this.disabled=false; this.className = 'button';} } catch(err){};   \" class=\"button\" type=\"button\">" + buttonXML.getfieldTransl("send", false) + "</button></div></td></tr>\n");
		}
		result.append("</tbody></table>\n");
		result.append("</form>\n");
		result.append("</div>\n");
		result.append("</div>\n");
		return result.toString();
	}
	
	public String getChequeAllButtonsShort(boolean hasStornoMenuPermission) {
		return getChequeAllButtons(false, hasStornoMenuPermission);
	}
	
	public String getChequeSaveButtonOnCheque() {
		if ("Y".equalsIgnoreCase(canSaveCheque)) {
			return "<input type=\"button\" class=\"button button_small\" onclick=\"JavaScript: var cWin=window.open('chequesave.jsp?id_telgr=" + this.idTelgr + "&cheque_format=" + this.saveFormat + "','blank','height=600,width=900,top=50,left=150,toolbar=no,menubar=no,location=no,scrollbars=yes,status=yes'); cWin.focus();\" value=\"" + buttonXML.getfieldTransl("button_save", false) +"\" />";
		} else {
			return "";
		}
	}
	
	public String getChequePrintButton() {
		return "<input type=\"button\" class=\"button button_small\" onclick=\"JavaScript: var cWin=window.open('report/cheque.jsp?id_telgr=" + this.idTelgr + "&print=Y','blank','height=600,width=420,top=50,left=150,toolbar=no,menubar=no,location=no,scrollbars=yes,status=no'); cWin.focus(); cWin.printDiv('printableArea');\" value=\"" + buttonXML.getfieldTransl("button_print", false) +"\" />";
	}
	
	public String getChequePrintImage() {
		return "<img class=\"image_cheque image_cheque_print\" onclick=\"JavaScript: var cWin=window.open('report/cheque.jsp?id_telgr=" + this.idTelgr + "&print=Y','blank','height=600,width=420,top=50,left=150,toolbar=no,menubar=no,location=no,scrollbars=yes,status=no'); cWin.focus(); cWin.printDiv('printableArea');\" title=\"" + buttonXML.getfieldTransl("button_print", false) +"\" />";
	}
	
	public String getChequeSaveImage() {
		if ("Y".equalsIgnoreCase(canSaveCheque)) {
			return "<img class=\"image_cheque image_cheque_save\" onclick=\"JavaScript: var cWin=window.open('report/chequesave.jsp?id_telgr=" + this.idTelgr + "&cheque_format=" + this.saveFormat + "','blank','height=600,width=900,top=50,left=150,toolbar=no,menubar=no,location=no,scrollbars=yes,status=yes'); cWin.focus();\" title=\"" + buttonXML.getfieldTransl("button_save", false) +"\" />";
		} else {
			return "";
		}
	}

	/*public String getChequePrintScript(String pOperationId) {
		StringBuilder lReturn = new StringBuilder();
		
		lReturn.append("<script>")
			.append("function showCheque() {")
			.append("var cWin=window.open('report/cheque.jsp?id=" + pOperationId + "&print=Y','blank','height=600,width=420,top=50,left=150,toolbar=no,menubar=no,location=no,scrollbars=yes,status=no'); cWin.focus(); cWin.printDiv('printableArea');")
			.append("}")
			.append("</script>");
		return lReturn.toString();
	}*/
	
	public String getChequeSaveData(boolean fullFormat) {
		this.reportFormat = this.saveFormat;
		return getChequeHTML(fullFormat);
	}
	
    public String getChequeHTML(boolean fullFormat) {
    	StringBuilder html = new StringBuilder();
    	//StringBuilder htmlLast = new StringBuilder();
    	
    	if ("INVOICE".equalsIgnoreCase(this.getValue("PAY_TYPE"))) {
    		html.append(this.getInvoiceHTML());
    		return html.toString();
    	}
    	
    	wpTelegramObject invoice = null;
    	String idTelgrPaid = this.getValue("ID_TELGR_PAID");
    	if (!isEmpty(idTelgrPaid)) {
    		invoice = new wpTelegramObject(idTelgrPaid);
    	}
    	
    	ArrayList<bcFeautureParam> pParam = initParamArray();
		Connection con = null;
		PreparedStatement st = null;
    	
        ArrayList<bcFeautureParam> pParamTargetPrg = initParamArray();
        PreparedStatement stTargetPrg = null;
        Connection conTargetPrg = null;
        
        String dateFormat = "DD.MM.RRRR";
		dateFormat = dateFormat.replaceAll("R", "y").replaceAll("Y", "y").replaceAll("m", "M").replaceAll("D", "d");
		
        String mySQL = 
        		" SELECT * " +
        		"   FROM " + getGeneralDBScheme() + ".vp$trans_all " + 
        		"  WHERE id_telgr = ? ";
        if (!(idTrans == null || "".equalsIgnoreCase(idTrans))) {
        	mySQL = mySQL + " AND id_trans = ? ";
        }
        mySQL = mySQL + 
        		"    AND fcd_trans_type <> 'REC_QUESTIONING'" +
        		"    AND fcd_trans_state <> 'C_IN_PROCESS_TRANS' " +
        		"  ORDER BY id_trans ";
        pParam.add(new bcFeautureParam("int", this.idTelgr));
        if (!(idTrans == null || "".equalsIgnoreCase(idTrans))) {
        	pParam.add(new bcFeautureParam("int", this.idTrans));
        }
    
        
        try{ 
        	
        	LOGGER.debug(prepareSQLToLog(mySQL,pParam));
      	  	con = Connector.getConnection(getSessionId());
      	  	st = con.prepareStatement(mySQL);
      	  	st = prepareParam(st, pParam);
      	  	ResultSet rset = st.executeQuery();
			
	    	String cdCurrency = "&nbsp;" + this.getValue("SCD_CURRENCY");
	    	String emptyCurrency = "&nbsp;";
	    	for (int i=0; i < this.getValue("SCD_CURRENCY").length(); i++) {
	    		emptyCurrency = emptyCurrency + "&nbsp;";
	    	}
      	  	
      	  	//int transCount = 0;
	    	
			//SimpleDateFormat sdf_short0=new SimpleDateFormat(dateFormat);
	    	String operDate = this.getValue("DATE_TELGR_DF");
	    	
	    	String operTime = this.getValue("DATE_TELGR_HMF");
	    	
	    	String operDateFull = this.getValue("DATE_TELGR_DHMF");
      	  	
      	  	html.append(formatXMLString(0, "<?xml version=\"1.0\"?>"));
	    	html.append(formatXMLSectionBeg(0, "cheque","id=\""+this.getValue("ID_TELGR")+"\""));
	    	//html.append("<table class=\"table_cheque\"><tbody>");
	        if (fullFormat) {
		        html.append(formatLineCenter(this.getValue("NAME_CLUB")));
		        html.append(formatLineCenter(this.getValue("NAME_SERVICE_PLACE")));
		        html.append(formatLineCenter(this.getValue("ADR_SERVICE_PLACE")));
		        html.append(formatLineCenter(this.getValue("PHONE_SERVICE_PLACE")));
		        html.append(formatLineCenter(this.getValue("SNAME_OPERATOR")));
		        html.append(formatLineCenter(this.getValue("PHONE_OPERATOR")));
		
		        html.append(formatLine2CellB(this.webposXML.getfieldTransl("cheque_organization_terminal", false) + ":",this.getValue("CD_SERVICE_PLACE") + "/" + this.getValue("ID_TERM")));
		        html.append(formatLine2CellB("&nbsp;","&nbsp;"));
		        
		        //-----------XML----------------
		        html.append(formatXMLElement(1, "name_club", this.getValue("NAME_CLUB"), ""));
	        	html.append(formatXMLElement(1, "name_service_place", this.getValue("NAME_SERVICE_PLACE"), ""));
	        	html.append(formatXMLElement(1, "adr_service_place", this.getValue("ADR_SERVICE_PLACE"), ""));
	        	html.append(formatXMLElement(1, "phone_service_place", this.getValue("PHONE_SERVICE_PLACE"), ""));
	        	html.append(formatXMLElement(1, "name_operator", this.getValue("SNAME_OPERATOR"), ""));
	        	html.append(formatXMLElement(1, "phone_operator", this.getValue("PHONE_OPERATOR"), ""));
	        	html.append(formatXMLElement(1, "cd_service_place", this.getValue("CD_SERVICE_PLACE"), ""));
	        	html.append(formatXMLElement(1, "id_terminal", this.getValue("ID_TERM"), ""));
	        }

	        //html.append(formatLine2CellB(this.webposXML.getfieldTransl("cheque_card_type", false) + ":", this.getValue("NAME_CARD_STATUS")));
	        html.append(formatLine2CellB(this.webposXML.getfieldTransl("cheque_number", false), this.getValue("NC")));	        
	        html.append(formatLine2CellB(this.webposXML.getfieldTransl("cheque_input_card", false) + ":", this.getValue("CD_CARD1_HIDE")));
	        html.append(formatLine2CellB(this.webposXML.getfieldTransl("cheque_date", false) + ":&nbsp;" + operDate, this.webposXML.getfieldTransl("cheque_time", false) + ":&nbsp;" + operTime));
    		html.append(formatLine2CellB(this.webposXML.getfieldTransl("goods_pay_way", false) + ":", this.getValue("NAME_TRANS_PAY_TYPE")));
        	html.append(formatLine2CellB(this.webposXML.getfieldTransl("cheque_sum", false) + ":", this.getValue("OPR_SUM_FRMT") + cdCurrency));
        	if ("CASH".equalsIgnoreCase(this.getValue("PAY_TYPE"))) {
            	html.append(formatLine2CellB(this.webposXML.getfieldTransl("cheque_entered_sum", false) + ":", this.getValue("ENTERED_SUM_FRMT") + cdCurrency));
            	html.append(formatLine2CellB(this.webposXML.getfieldTransl("cheque_sum_change", false) + ":", this.getValue("SUM_CHANGE_FRMT") + cdCurrency));
        	}
	        //-----------XML----------------
	        //html.append(formatXMLElement(1, "card_type", this.getValue("NAME_CARD_STATUS"), ""));
	        html.append(formatXMLElement(1, "document_number", this.getValue("NC"), ""));
    		html.append(formatXMLElement(1, "card_code", this.getValue("CD_CARD1_HIDE"), ""));
	        html.append(formatXMLElement(1, "operation_date", operDateFull, ""));
	        html.append(formatXMLElement(1, "payment_way", this.getValue("NAME_TRANS_PAY_TYPE"), ""));
        	html.append(formatXMLElement(1, "operation_sum", this.getValue("OPR_SUM_FRMT"), "currency=\"" + this.getValue("SCD_CURRENCY") + "\""));
        	if ("CASH".equalsIgnoreCase(this.getValue("PAY_TYPE"))) {
        		html.append(formatXMLElement(1, "entered_sum", this.getValue("ENTERED_SUM_FRMT"), "currency=\"" + this.getValue("SCD_CURRENCY") + "\""));
        		html.append(formatXMLElement(1, "sum_change", this.getValue("SUM_CHANGE_FRMT"), "currency=\"" + this.getValue("SCD_CURRENCY") + "\""));
        	}
	        

	        if ("BANK_CARD".equalsIgnoreCase(this.getValue("PAY_TYPE"))) {
	        	html.append(formatLine("2", false, "left", this.webposXML.getfieldTransl("cheque_barktrn", false) + ":&nbsp;" + this.getValue("BANK_TRN"), "", ""));
	        	//-----------XML----------------
	            html.append(formatXMLElement(1, "bark_trn", this.getValue("BANK_TRN"), ""));
	        }
      	    String err_tx = this.webposXML.getfieldTransl("cheque_err_tx", false) + "&nbsp;" + this.getValue("ERR_TX");
      	    String operState = "";
      	    String confirmationType = "";
	        if ("EXECUTED".equalsIgnoreCase(this.getValue("CD_TELGR_STATE"))) {
	        	operState = this.webposXML.getfieldTransl("cheque_operation_execute", false);
	        } else if ("C_IN_PROCESS_TRANS".equalsIgnoreCase(this.getValue("CD_TELGR_STATE"))) {
	        	operState = this.webposXML.getfieldTransl("cheque_operation_in_process", false);
	        } else if ("C_ERROR_TRANS".equalsIgnoreCase(this.getValue("CD_TELGR_STATE"))) {
	        	operState = this.webposXML.getfieldTransl("cheque_operation_error", false);
	        } else if ("C_CANCELLED_TRANS".equalsIgnoreCase(this.getValue("CD_TELGR_STATE"))) {
	        	operState = this.webposXML.getfieldTransl("cheque_operation_cancel", false);
	        } else {
	        	operState = this.getValue("NAME_TELGR_STATE");
	        }
	        
		    if ("PIN".equalsIgnoreCase(this.getValue("CONFIRMATION_TYPE"))) {
		    	confirmationType = this.webposXML.getfieldTransl("cheque_input_pin", false);
		    } else if ("SMS".equalsIgnoreCase(this.getValue("CONFIRMATION_TYPE"))) {
		    	confirmationType = this.webposXML.getfieldTransl("cheque_input_code_from_sms", false);
		    } else if ("ACTIVATION_CODE".equalsIgnoreCase(this.getValue("CONFIRMATION_TYPE"))) {
		    	confirmationType = this.webposXML.getfieldTransl("cheque_input_activation_code", false);
		    }
		    
	        String totalState = operState + ", " + err_tx;
	        if (!"NONE".equalsIgnoreCase(this.getValue("CONFIRMATION_TYPE"))) {
	        	totalState = totalState + ", " + confirmationType;
	        }
	        
	        html.append(formatLine("2", false, "left", totalState, "", ""));
	    	html.append(formatLine("2", false, "left", this.webposXML.getfieldTransl("cheque_rrn", false) + ":&nbsp;" + this.getValue("RRN"), "", ""));
	    	//-----------XML----------------
        	html.append(formatXMLElement(1, "operation_state", this.webposXML.getfieldTransl("cheque_operation_execute", false), ""));
	        html.append(formatXMLElement(1, "err_tx", this.getValue("ERR_TX"), ""));
	        if (!"NONE".equalsIgnoreCase(this.getValue("CONFIRMATION_TYPE"))) {
	        	html.append(formatXMLElement(1, "confirm_type", this.getValue("CONFIRMATION_TYPE"), ""));
	        }
	        html.append(formatXMLElement(1, "rrn", this.getValue("RRN"), ""));
			
	    	String emptyCurrencyTotal = "&nbsp;&nbsp;&nbsp;&nbsp;";

	        
	  	    while (rset.next()) {
      	    	//transCount ++;
		    	String cdTransType = rset.getString("FCD_TRANS_TYPE");
		    	
		        html.append(formatLine2Cell("&nbsp;","&nbsp;"));
		        	
		    	if (isEmpty(rset.getString("ID_TRANS"))) {
			    	html.append(formatXMLString(0, "<?xml version=\"1.0\"?>"));
			    	html.append(formatXMLSectionBeg(0, "cheque","id=\""+rset.getString("ID_TRANS")+"\""));
		    		html.append(formatLineCenter(this.webposXML.getfieldTransl("cheque_error_not_found", false)));
			        //-----------XML----------------
			        html.append(formatXMLElement(1, "error", this.webposXML.getfieldTransl("cheque_error_not_found", false), ""));
		            html.append(formatXMLSectionEnd(0, "cheque"));
		            
		            return html.toString();
		    	}
			    	
		    	String operName = "";
			        
			    if ("REC_PAYMENT".equalsIgnoreCase(cdTransType)) {
			       	if ("SMPU_CARD".equalsIgnoreCase(rset.getString("PAY_TYPE"))) {
			       		operName = this.webposXML.getfieldTransl("cheque_payment", false);
			       	} else {
			       		operName = this.webposXML.getfieldTransl("cheque_payment_registration", false);
			       	}
			    } else if ("REC_PUT_CARD".equalsIgnoreCase(cdTransType)) {
			       	operName = this.webposXML.getfieldTransl("cheque_put_card", false);
			    } else {
			       	operName = rset.getString("NAME_TRANS_TYPE");
			    }
			    if ("CANCEL".equalsIgnoreCase(rset.getString("CD_TRANS_ACTION"))) {
			    	operName = operName + " (" + this.webposXML.getfieldTransl("title_trans_action_cancel", false) + ")";    	
			    } else if ("RETURN".equalsIgnoreCase(rset.getString("CD_TRANS_ACTION"))) {
			    	operName = operName + " (" + this.webposXML.getfieldTransl("title_trans_action_return", false) + ")";
			    }
			        
			    html.append(formatLine2Cell(operName, this.webposXML.getfieldTransl("cheque_transaction_short", false) + "&nbsp;" + rset.getString("NT_ICC")));
			    //-----------XML----------------
			    html.append(formatXMLElement(1, "operation_type", operName, ""));
			    html.append(formatXMLElement(1, "card_transaction_number", rset.getString("NT_ICC"), ""));

			    if (!"REC_ACTIVATION".equalsIgnoreCase(cdTransType)) {
			      	String payTypeCaption = rset.getString("NAME_TRANS_PAY_TYPE");
			        	
			       	if (!("REC_MOV_BON".equalsIgnoreCase(cdTransType) ||
			       			"REC_CHK_CARD".equalsIgnoreCase(cdTransType) ||
			       			"REC_INVAL_CARD".equalsIgnoreCase(cdTransType) ||
			       			"REC_QUESTIONING".equalsIgnoreCase(cdTransType) ||
			       			"REC_COUPON".equalsIgnoreCase(cdTransType) ||
			       			"REC_POINT_FEE".equalsIgnoreCase(cdTransType) ||
			       			"REC_TRANSFER_GET_POINT".equalsIgnoreCase(cdTransType) ||
			       			"REC_TRANSFER_PUT_POINT".equalsIgnoreCase(cdTransType) ||
			       			"REC_TRANSFORM_FROM_SHARE".equalsIgnoreCase(cdTransType))) {
			           	html.append(formatLine2Cell(this.webposXML.getfieldTransl("cheque_sum", false) + ":", rset.getString("OPR_SUM_CHEQUE_FRMT") + cdCurrency));
			           	if (!rset.getString("PAY_TYPE").equalsIgnoreCase(this.getValue("PAY_TYPE"))) {
			           		html.append(formatLine2Cell(this.webposXML.getfieldTransl("goods_pay_way", false) + ":", payTypeCaption));
			           	}
			       	} else {
			           	html.append(formatLine2Cell(this.webposXML.getfieldTransl("cheque_sum", false) + ":", rset.getString("OPR_SUM_CHEQUE_FRMT") + emptyCurrency));
			       	}
			    	//-----------XML----------------
				    if (!("REC_MOV_BON".equalsIgnoreCase(cdTransType) ||
			      			"REC_CHK_CARD".equalsIgnoreCase(cdTransType) ||
			       			"REC_INVAL_CARD".equalsIgnoreCase(cdTransType) ||
			       			"REC_QUESTIONING".equalsIgnoreCase(cdTransType) ||
			       			"REC_COUPON".equalsIgnoreCase(cdTransType) ||
			       			"REC_POINT_FEE".equalsIgnoreCase(cdTransType) ||
			       			"REC_TRANSFER_GET_POINT".equalsIgnoreCase(cdTransType) ||
			       			"REC_TRANSFER_PUT_POINT".equalsIgnoreCase(cdTransType) ||
			       			"REC_TRANSFORM_FROM_SHARE".equalsIgnoreCase(cdTransType))) {
				       	html.append(formatXMLElement(1, "operation_sum", rset.getString("OPR_SUM_CHEQUE_FRMT"), "currency=\"" + rset.getString("SCD_CURRENCY") + "\""));
				       	if (!rset.getString("PAY_TYPE").equalsIgnoreCase(this.getValue("PAY_TYPE"))) {
				       		html.append(formatXMLElement(1, "payment_way", payTypeCaption, ""));
				       	}
				    } else {
				      	html.append(formatXMLElement(1, "operation_sum", rset.getString("OPR_SUM_CHEQUE_FRMT"), ""));
			       	}
			    }
			    if ("REC_PAYMENT_INVOICE".equalsIgnoreCase(cdTransType) && !isEmpty(idTelgrPaid)) {
			    	String invoiceDescription = this.webposXML.getfieldTransl("cheque_invoice", false) + " " + invoice.getValue("RRN") + " " + 
			    								this.webposXML.getfieldTransl("cheque_invoice_from", false) + " " + invoice.getValue("DATE_TELGR_DF");
			    	html.append(formatLine("2", false, "left", invoiceDescription, "", ""));
			    	//html.append(formatLine2Cell(this.webposXML.getfieldTransl("cheque_invoice", false) + ":", invoice.getValue("RRN")));
			    	//html.append(formatLine2Cell(this.webposXML.getfieldTransl("cheque_invoice_date", false) + ":", invoice.getValue("DATE_TELGR_DF")));
			    	//-----------XML----------------
			    	html.append(formatXMLElement(1, "invoice", invoice.getValue("RRN"), "date=\"" + invoice.getValue("DATE_TELGR_DF") + "\""));
			    }
		        
		        if ("REC_MTF".equalsIgnoreCase(cdTransType)) {
		        	try{
		            	String mySQLTargetPrg = 
		            		" SELECT a.id_trans, a.cd_trans_type, a.id_target_prg, a.name_target_prg, " +
		            		"        a.cd_target_prg_pay_period, a.id_target_prg_place, " +
		            		"        p.sname_jur_prs sname_target_prg_place, a.opr_sum," +
		            		"        a.opr_sum_frmt, a.entrance_fee_sum, a.entrance_fee_sum_frmt, " + 
		            		"        a.membership_fee_sum, a.membership_fee_sum_frmt, a.membership_last_date_frmt, a.sum_put_point," +
		            		"        a.sum_put_point_frmt, a.sum_get_point, a.sum_get_point_frmt," +
		            		"        a.can_subscribe_target_prg " +
		    	            "   FROM " + getGeneralDBScheme() + ".vp$trans_target_prg_all a, vc_target_prg_place_all p " +
		    	            "  WHERE a.id_trans = ? " +
		    	            "    AND a.id_target_prg_place = p.id_target_prg_place (+)" +
		    	            "  ORDER BY a.name_target_prg ";
		            	pParamTargetPrg.add(new bcFeautureParam("int", rset.getString("ID_TRANS")));
		            	
		                LOGGER.debug(prepareSQLToLog(mySQLTargetPrg,pParamTargetPrg));
		            	conTargetPrg = Connector.getConnection(getSessionId());
		            	stTargetPrg = conTargetPrg.prepareStatement(mySQLTargetPrg);
		            	stTargetPrg = prepareParam(stTargetPrg, pParamTargetPrg);	
		            	ResultSet rsetTargetPrg = stTargetPrg.executeQuery();
		
		                while (rsetTargetPrg.next())
		                {
		                	html.append(formatLine("2", false, "leftb", this.webposXML.getfieldTransl("cheque_target_program", false) + ":&nbsp;" + rsetTargetPrg.getString("NAME_TARGET_PRG"), "", ""));
		                	//-----------XML----------------
		             		html.append(formatXMLSectionBeg(2, "target_program","id=\""+rsetTargetPrg.getString("ID_TARGET_PRG")+"\""));
		             	    html.append(formatXMLElement(3, "name", rsetTargetPrg.getString("NAME_TARGET_PRG"), ""));
		        	        if (!isEmpty(rsetTargetPrg.getString("ID_TARGET_PRG_PLACE"))) {
		        	        	html.append(formatLine("2", false, "left", this.webposXML.getfieldTransl("target_prg_place", false) + ":&nbsp;" + rsetTargetPrg.getString("SNAME_TARGET_PRG_PLACE"), "", ""));
			                	//-----------XML----------------
		            	        html.append(formatXMLElement(3, "target_prg_place", rsetTargetPrg.getString("SNAME_TARGET_PRG_PLACE"), ""));
		        	        	
		        	        }
		        	        /*if (!"IRREGULAR".equalsIgnoreCase(rset.getString("CD_TARGET_PRG_PAY_PERIOD"))) {
		        	        	if ("STUDY_COUNT".equalsIgnoreCase(rset.getString("CD_TARGET_PRG_PAY_PERIOD"))) {
			                		html.append(formatLine("2", false, "left", this.webposXML.getfieldTransl("cheque_target_program_period", false) + "&nbsp;" + rset.getString("PAY_COUNT_TOTAL") + "&nbsp;" + this.webposXML.getfieldTransl("cheque_target_prg_poriod_study_count", false), "", ""));
			    	               	//-----------XML----------------
			                	    html.append(formatXMLElement(3, "study_count", rset.getString("PAY_COUNT_TOTAL"), ""));
			                	} else if ("MONTH".equalsIgnoreCase(rset.getString("CD_TARGET_PRG_PAY_PERIOD"))) {
			                		html.append(formatLine("2", false, "left", this.webposXML.getfieldTransl("cheque_target_program_period", false) + "&nbsp;" + rset.getString("PAY_COUNT_TOTAL") + "&nbsp;" + this.webposXML.getfieldTransl("cheque_target_prg_poriod_month", false), "", ""));
		    	                	//-----------XML----------------
		                	        html.append(formatXMLElement(3, "month_count", rset.getString("PAY_COUNT_TOTAL"), ""));
		                		}
		                	}*/
		        	        if (!isEmpty(rsetTargetPrg.getString("CAN_SUBSCRIBE_TARGET_PRG")) &&
		        	        		"Y".equalsIgnoreCase(rsetTargetPrg.getString("CAN_SUBSCRIBE_TARGET_PRG"))) {
		        	        	html.append(formatLine("2", false, "left", this.webposXML.getfieldTransl("cheque_success_subscribe_target_prg", false), "", ""));
			                	//-----------XML----------------
		            	        html.append(formatXMLElement(3, "can_subscribe", rsetTargetPrg.getString("CAN_SUBSCRIBE_TARGET_PRG"), ""));
		        	        	
		        	        }
		        	        if (!isEmpty(rsetTargetPrg.getString("ENTRANCE_FEE_SUM")) || "0".equalsIgnoreCase(rsetTargetPrg.getString("ENTRANCE_FEE_SUM"))) {
		        	        	html.append(formatLine("1", false, "left", this.webposXML.getfieldTransl("cheque_entrance_fee", false) + ":", "right", rsetTargetPrg.getString("ENTRANCE_FEE_SUM_FRMT") + cdCurrency));
			                	//-----------XML----------------
		            	        html.append(formatXMLElement(3, "entrance_fee", rsetTargetPrg.getString("ENTRANCE_FEE_SUM_FRMT"), ""));
		        	        	
		        	        }
		        	        if (!isEmpty(rsetTargetPrg.getString("MEMBERSHIP_FEE_SUM")) || "0".equalsIgnoreCase(rsetTargetPrg.getString("MEMBERSHIP_FEE_SUM"))) {
		        	        	html.append(formatLine("1", false, "left", this.webposXML.getfieldTransl("cheque_membership_fee", false) + ":", "right", rsetTargetPrg.getString("MEMBERSHIP_FEE_SUM_FRMT") + cdCurrency));
		        	        	html.append(formatLine("1", false, "left", this.webposXML.getfieldTransl("membership_last_date", false) + ":", "right", rsetTargetPrg.getString("MEMBERSHIP_LAST_DATE_FRMT")));
			                	//-----------XML----------------
		            	        html.append(formatXMLElement(3, "membership_fee", rsetTargetPrg.getString("MEMBERSHIP_FEE_SUM_FRMT"), ""));
		            	        html.append(formatXMLElement(3, "membership_last_date", rsetTargetPrg.getString("MEMBERSHIP_LAST_DATE_FRMT"), ""));
		        	        	
		        	        }
		                	html.append(formatXMLSectionEnd(2, "target_program"));
		        	    }
		            } // try
		            catch (SQLException e) {LOGGER.error(html, e);}
		            catch (Exception el) {LOGGER.error(html, el);}
		            finally {
		                try {
		                    if (stTargetPrg!=null) stTargetPrg.close();
		                } catch (SQLException w) {w.toString();}
		                try {
		                    if (conTargetPrg!=null) conTargetPrg.close();
		                } catch (SQLException w) {w.toString();}
		                Connector.closeConnection(conTargetPrg);
		            } // finally
		        }
		        
		        if ("REC_TRANSFORM_FROM_SHARE".equalsIgnoreCase(cdTransType)) {
		        	//if (!isEmpty(rset.getString("SUM_GET_SHARE_ACCOUNT"))) {
			        //	if (Float.parseFloat(rset.getString("SUM_GET_SHARE_ACCOUNT")) >0) {
			        //		html.append(formatLine2Cell(this.webposXML.getfieldTransl("cheque_used_share_account", false) + ":", rset.getString("SUM_GET_SHARE_ACCOUNT_FRMT") + cdCurrency));
			        //    	//-----------XML----------------
			    	//    	html.append(formatXMLElement(1, "used_share_account", rset.getString("SUM_GET_SHARE_ACCOUNT_FRMT"), "currency=\"" + rset.getString("SCD_CURRENCY") + "\""));
			        //	}
		        	//}
		        	//if (!isEmpty(rset.getString("SUM_PUT_POINT")) || "0".equalsIgnoreCase(rset.getString("SUM_PUT_POINT"))) {
		            //	html.append(formatLine2Cell(this.webposXML.getfieldTransl("cheque_add_point", false) + ":", rset.getString("SUM_PUT_POINT_FRMT") + emptyCurrency));
		            //	//-----------XML----------------
		    	    //    html.append(formatXMLElement(1, "add_point", rset.getString("SUM_PUT_POINT_FRMT"), ""));
		        	//}
		        	//-----------XML----------------
		        }
		        
		        if ("REC_TRANSFER_GET_POINT".equalsIgnoreCase(cdTransType)) {
		        	//html.append(formatLine2Cell(this.webposXML.getfieldTransl("cheque_write_off_point", false) + ":", rset.getString("SUM_GET_POINT_FRMT") + emptyCurrency));
		        	//-----------XML----------------
			        //html.append(formatXMLElement(1, "write_off_point", rset.getString("SUM_GET_POINT_FRMT"), ""));
		        }
		        
		        if ("REC_TRANSFER_PUT_POINT".equalsIgnoreCase(cdTransType)) {
			        html.append(formatLine("1", false, "leftb", this.webposXML.getfieldTransl("cheque_recepient_card", false) + ":", "rightb", rset.getString("CD_CARD1_HIDE")));
		        	//-----------XML----------------
		     	    html.append(formatXMLElement(1, "card_recepient", rset.getString("CD_CARD1_HIDE"), ""));
		        	//if (!isEmpty(rset.getString("SUM_PUT_POINT")) || "0".equalsIgnoreCase(rset.getString("SUM_PUT_POINT"))) {
		            //	html.append(formatLine2Cell(this.webposXML.getfieldTransl("cheque_add_point", false) + ":", rset.getString("SUM_PUT_POINT_FRMT") + emptyCurrency));
		            //	//-----------XML----------------
		    	    //    html.append(formatXMLElement(1, "add_point", rset.getString("SUM_PUT_POINT_FRMT"), ""));
		        	//}
		        }
		        
		        if (!("REC_SHARE_FEE".equalsIgnoreCase(cdTransType) ||
		        		"REC_TRANSFER_GET_POINT".equalsIgnoreCase(cdTransType) ||
		        		"REC_TRANSFER_PUT_POINT".equalsIgnoreCase(cdTransType) ||
		        		"REC_CANCEL".equalsIgnoreCase(cdTransType) ||
		        		"REC_ACTIVATION".equalsIgnoreCase(cdTransType))) {
		        	if (!isEmpty(rset.getString("SUM_GET_SHARE_ACCOUNT"))) {
			        	if (Float.parseFloat(rset.getString("SUM_GET_SHARE_ACCOUNT")) >0) {
			        		html.append(formatLine2Cell(this.webposXML.getfieldTransl("cheque_used_share_account", false) + ":", rset.getString("SUM_GET_SHARE_ACCOUNT_FRMT") + cdCurrency));
			            	//-----------XML----------------
			    	    	html.append(formatXMLElement(1, "used_share_account", rset.getString("SUM_GET_SHARE_ACCOUNT_FRMT"), "currency=\"" + rset.getString("SCD_CURRENCY") + "\""));
			        	}
		        	}
		        	if ("REC_PAYMENT".equalsIgnoreCase(cdTransType)) {
		        		html.append(formatLine2Cell(this.webposXML.getfieldTransl("cheque_add_point_for_purchase", false) + ":", rset.getString("SUM_PUT_POINT_FRMT") + emptyCurrency));
		            	//-----------XML----------------
		    	        html.append(formatXMLElement(1, "add_point", rset.getString("SUM_PUT_POINT_FRMT"), ""));
		        	//} else if ("REC_CANCEL".equalsIgnoreCase(cdTransType) ||
		            //			"REC_RETURN".equalsIgnoreCase(cdTransType)) {
		        	//	html.append(formatLine2Cell(this.webposXML.getfieldTransl("cheque_add_point_storno", false) + ":"+rset.getString("RRN_REVERSED"), rset.getString("SUM_PUT_POINT_FRMT") + emptyCurrency));
		            	//-----------XML----------------
		    	    //    html.append(formatXMLElement(1, "add_point", rset.getString("SUM_PUT_POINT_FRMT"), "rrn_reversed="+rset.getString("RRN_REVERSED")));
		        	} else if ("REC_POINT_FEE".equalsIgnoreCase(cdTransType)) {
		        		//Баллы будут выведены в итоговом блоке
		        	} else {
					    if (!(rset.getString("SUM_PUT_POINT") == null || "".equalsIgnoreCase(rset.getString("SUM_PUT_POINT")) || "0".equalsIgnoreCase(rset.getString("SUM_PUT_POINT")))) {
			        		html.append(formatLine2Cell(this.webposXML.getfieldTransl("cheque_add_point", false) + ":", rset.getString("SUM_PUT_POINT_FRMT") + emptyCurrency));
			            	//-----------XML----------------
			    	        html.append(formatXMLElement(1, "add_point", rset.getString("SUM_PUT_POINT_FRMT"), ""));
					    }
		        	}
		        	//if ("REC_CANCEL".equalsIgnoreCase(cdTransType) ||
		        	//		"REC_RETURN".equalsIgnoreCase(cdTransType)) {
		        	//	html.append(formatLine2Cell(this.webposXML.getfieldTransl("cheque_write_off_point_storno", false) + ": "+rset.getString("RRN_REVERSED"), rset.getString("SUM_GET_POINT_FRMT") + emptyCurrency));
		            	//-----------XML----------------
		    	    //    html.append(formatXMLElement(1, "write_off_point", rset.getString("SUM_GET_POINT_FRMT"), "reversed_rrn="+rset.getString("RRN_REVERSED")));
		        	//} else 
		        		if ("REC_POINT_FEE".equalsIgnoreCase(cdTransType)) {
		        		// Ничего не выводим
		        	} else {
		        		if (!(rset.getString("SUM_GET_POINT") == null || "".equalsIgnoreCase(rset.getString("SUM_GET_POINT")) || "0".equalsIgnoreCase(rset.getString("SUM_GET_POINT")))) {
			        		html.append(formatLine2Cell(this.webposXML.getfieldTransl("cheque_write_off_point", false) + ":", rset.getString("SUM_GET_POINT_FRMT") + emptyCurrency));
			            	//-----------XML----------------
			    	        html.append(formatXMLElement(1, "write_off_point", rset.getString("SUM_GET_POINT_FRMT"), ""));
		        		}
		        	}
		        } else if ("REC_MTF".equalsIgnoreCase(cdTransType) &&
		        		(!isEmpty(rset.getString("SUM_PUT_POINT")) || "0".equalsIgnoreCase(rset.getString("SUM_PUT_POINT")))) {
		        	html.append(formatLine2Cell(this.webposXML.getfieldTransl("cheque_add_point_total", false) + ":", rset.getString("SUM_PUT_POINT_FRMT") + emptyCurrency));
		        	//-----------XML----------------
			        html.append(formatXMLElement(1, "add_point_total", rset.getString("SUM_PUT_POINT_FRMT"), ""));
		        }
		    	//html.append(formatLine("2", false, "left", this.webposXML.getfieldTransl("cheque_rrn", false) + ":&nbsp;" + rset.getString("RRN"), "", ""));
		    	//-----------XML----------------
		        //html.append(formatXMLElement(1, "rrn", rset.getString("RRN"), ""));
		        
		        
		        if ("REC_PUT_CARD".equalsIgnoreCase(cdTransType)) {
		            //htmlLast.append(formatLine2Cell(this.webposXML.getfieldTransl("cheque_share_account_amount", false) + ":", rset.getString("SHARE_ACCOUNT_VALUE_FRMT") + cdCurrency));
		        	html.append(formatLine2Cell(this.webposXML.getfieldTransl("cheque_entrance_fee", false) + ":", rset.getString("ENTRANCE_FEE_SUM_FRMT") + cdCurrency));
		        	html.append(formatLine2Cell(this.webposXML.getfieldTransl("cheque_membership_fee", false) + ":", rset.getString("MEMBERSHIP_FEE_SUM_FRMT") + cdCurrency));
		        	html.append(formatLine2Cell(this.webposXML.getfieldTransl("cheque_add_share_account", false) + ":", rset.getString("SUM_PUT_SHARE_ACCOUNT_FRMT") + cdCurrency));
			    	//-----------XML----------------
		        	html.append(formatXMLElement(1, "entrance_fee", rset.getString("ENTRANCE_FEE_SUM_FRMT"), "currency=\"" + rset.getString("SCD_CURRENCY") + "\""));
		        	html.append(formatXMLElement(1, "membership_fee", rset.getString("MEMBERSHIP_FEE_SUM_FRMT"), "currency=\"" + rset.getString("SCD_CURRENCY") + "\""));
		        	html.append(formatXMLElement(1, "add_share_account", rset.getString("SUM_PUT_SHARE_ACCOUNT_FRMT"), "currency=\"" + rset.getString("SCD_CURRENCY") + "\""));
		        } else if ("REC_COUPON".equalsIgnoreCase(cdTransType)) {
		        	html.append(formatLine2Cell(this.webposXML.getfieldTransl("coupon", false) + ":", rset.getString("CD_COUPON")));
		        	//-----------XML----------------
		        	html.append(formatXMLElement(1, "coupon", rset.getString("CD_COUPON"), ""));
		        } else if ("REC_SHARE_FEE_CHANGE".equalsIgnoreCase(cdTransType)) {
		        	html.append(formatLine2Cell(this.webposXML.getfieldTransl("cheque_add_share_account", false) + ":", rset.getString("SUM_PUT_SHARE_ACCOUNT_FRMT") + "&nbsp;" + rset.getString("SCD_CURRENCY")));
		        	//-----------XML----------------
		        	html.append(formatXMLElement(1, "add_share_account", rset.getString("SUM_PUT_SHARE_ACCOUNT_FRMT"), "currency=\"" + rset.getString("SCD_CURRENCY") + "\""));
		        } else if ("REC_SHARE_FEE".equalsIgnoreCase(cdTransType)) {
		        	html.append(formatLine2Cell(this.webposXML.getfieldTransl("cheque_add_share_account", false) + ":", rset.getString("SUM_PUT_SHARE_ACCOUNT_FRMT") + "&nbsp;" + rset.getString("SCD_CURRENCY")));
		        	//-----------XML----------------
		        	html.append(formatXMLElement(1, "add_share_account", rset.getString("SUM_PUT_SHARE_ACCOUNT_FRMT"), "currency=\"" + rset.getString("SCD_CURRENCY") + "\""));
		        } else if ("REC_MEMBERSHIP_FEE".equalsIgnoreCase(cdTransType)) {
		        	html.append(formatLine2Cell(this.webposXML.getfieldTransl("cheque_membership_fee", false) + ":", rset.getString("MEMBERSHIP_FEE_SUM_FRMT") + cdCurrency));
			    	//-----------XML----------------
		        	html.append(formatXMLElement(1, "membership_fee", rset.getString("MEMBERSHIP_FEE_SUM_FRMT"), "currency=\"" + rset.getString("SCD_CURRENCY") + "\""));
		        }
		        
			    if (!(rset.getString("COMMON_MARGIN_OUT_OPR_SUM") == null || "".equalsIgnoreCase(rset.getString("COMMON_MARGIN_OUT_OPR_SUM")) || "0".equalsIgnoreCase(rset.getString("COMMON_MARGIN_OUT_OPR_SUM")))) {
			    	html.append(formatLine2Cell(this.webposXML.getfieldTransl("cheque_all_margin", false) + ":", rset.getString("COMMON_MARGIN_OUT_OPR_SUM_FRMT") + cdCurrency));
			    	//-----------XML----------------
			    	html.append(formatXMLElement(1, "cheque_all_margin", rset.getString("COMMON_MARGIN_OUT_OPR_SUM_FRMT"), "currency=\"" + rset.getString("SCD_CURRENCY") + "\""));
			    }
      	    }
	        
	  	    html.append(formatLine2CellB("&nbsp;","&nbsp;"));
	    	html.append(formatLine("2", false, "leftb", this.webposXML.getfieldTransl("cheque_information", false) + ":", "", ""));
	    	if (!isEmpty(this.getValue("MESSAGE_TELGR"))) {			    
			    html.append(formatLine("2", false, "left", this.getValue("MESSAGE_TELGR"), "", ""));
		    	//-----------XML----------------
			    html.append(formatXMLElement(1, "information", this.getValue("MESSAGE_TELGR"), ""));
		    }
		    
		    html.append(formatLine2CellB("&nbsp;","&nbsp;"));

		    //**********************************************************************
        	if (!isEmpty(this.getValue("SUM_PUT_SHARE_TOTAL")) || "0".equalsIgnoreCase(this.getValue("SUM_PUT_SHARE_TOTAL"))) {
        		html.append(formatLine2CellB(this.webposXML.getfieldTransl("cheque_add_share_account", false) + ":", this.getValue("SUM_PUT_SHARE_TOTAL_FRMT") + "&nbsp;" + this.getValue("SCD_CURRENCY")));
        		//-----------XML----------------
        		html.append(formatXMLElement(1, "add_share_account", this.getValue("SUM_PUT_SHARE_TOTAL_FRMT"), "currency=\"" + this.getValue("SCD_CURRENCY") + "\""));
        	}
        	if (!isEmpty(this.getValue("SUM_GET_SHARE_TOTAL")) || "0".equalsIgnoreCase(this.getValue("SUM_GET_SHARE_TOTAL"))) {
        		html.append(formatLine2CellB(this.webposXML.getfieldTransl("cheque_used_share_account", false) + ":", this.getValue("SUM_GET_SHARE_TOTAL_FRMT") + "&nbsp;" + this.getValue("SCD_CURRENCY")));
        		//-----------XML----------------
        		html.append(formatXMLElement(1, "get_share_account", this.getValue("SUM_GET_SHARE_TOTAL_FRMT"), "currency=\"" + this.getValue("SCD_CURRENCY") + "\""));
        	}
        	if (!isEmpty(this.getValue("SUM_PUT_POINT_TOTAL")) || "0".equalsIgnoreCase(this.getValue("SUM_PUT_POINT_TOTAL"))) {
        		html.append(formatLine2CellB(this.webposXML.getfieldTransl("cheque_add_point", false) + ":", this.getValue("SUM_PUT_POINT_TOTAL_FRMT") + emptyCurrencyTotal));
        		//-----------XML----------------
        		html.append(formatXMLElement(1, "add_point", this.getValue("SUM_PUT_POINT_TOTAL_FRMT"), "currency=\"" + emptyCurrencyTotal + "\""));
        	}
        	if (!isEmpty(this.getValue("SUM_GET_POINT_TOTAL")) || "0".equalsIgnoreCase(this.getValue("SUM_GET_POINT_TOTAL"))) {
        		html.append(formatLine2CellB(this.webposXML.getfieldTransl("cheque_write_off_point", false) + ":", this.getValue("SUM_GET_POINT_TOTAL_FRMT") + emptyCurrencyTotal));
        		//-----------XML----------------
        		html.append(formatXMLElement(1, "write_off_point", this.getValue("SUM_GET_POINT_TOTAL_FRMT"), "currency=\"" + emptyCurrencyTotal + "\""));
        	}
        	
        	html.append(formatLine2CellB(this.webposXML.getfieldTransl("cheque_share_account_amount", false) + ":", this.getValue("SHARE_ACCOUNT_VALUE_FRMT") + "&nbsp;" + this.getValue("SCD_CURRENCY")));
        	html.append(formatLine2CellB(this.webposXML.getfieldTransl("cheque_bal_cur", false) + ":", this.getValue("CARD_BAL_CUR_FRMT") + emptyCurrencyTotal));
        	html.append(formatLine2CellB(this.webposXML.getfieldTransl("cheque_bal_acc", false) + ":", this.getValue("CARD_BAL_ACC_FRMT") + emptyCurrencyTotal));	
	    	//-----------XML----------------
        	html.append(formatXMLElement(1, "share_account", this.getValue("SHARE_ACCOUNT_VALUE_FRMT"), "currency=\"" + this.getValue("SCD_CURRENCY") + "\""));
        	html.append(formatXMLElement(1, "card_bal_cur", this.getValue("CARD_BAL_CUR_FRMT"), ""));
        	html.append(formatXMLElement(1, "card_bal_acc", this.getValue("CARD_BAL_ACC_FRMT"), ""));

        	html.append(formatLine2CellB(this.webposXML.getfieldTransl("membership_month_sum", false) + ":", this.getValue("MEMBERSHIP_MONTH_SUM_FRMT") + "&nbsp;" + this.getValue("SCD_CURRENCY") + "/" + this.commonXML.getfieldTransl("title_per_month", false)));
        	html.append(formatLine2CellB(this.webposXML.getfieldTransl("membership_last_date", false) + ":", this.getValue("MEMBERSHIP_LAST_DATE_DF")));
	    	//-----------XML----------------
        	html.append(formatXMLElement(1, "membership_month_sum", this.getValue("MEMBERSHIP_MONTH_SUM_FRMT"), "currency=\"" + this.getValue("SCD_CURRENCY") + "\""));
        	html.append(formatXMLElement(1, "membership_last_date", this.getValue("MEMBERSHIP_LAST_DATE_DF"), ""));
        	//**********************************************************************
        	
        	html.append(formatLine2CellB("&nbsp;","&nbsp;"));
		    html.append(formatLine("2", false, "left", this.webposXML.getfieldTransl("cheque_cashier", false) + ":&nbsp;" + this.getValue("CASHIER_NAME"), "", ""));
		    html.append(formatLine("2", false, "left", this.webposXML.getfieldTransl("cheque_cashier_card", false) + ":&nbsp;" + this.getValue("CASHIER_CD_CARD1_HIDE"), "", ""));
	        
		    html.append(formatLine2CellB("&nbsp;","&nbsp;"));
		    html.append(formatLineCenter(this.webposXML.getfieldTransl("cheque_end_line", false)));
	        //html.append("</tbody></table>\n");
	    	//-----------XML----------------
		    html.append(formatXMLElement(1, "cashier_name", this.getValue("CASHIER_NAME"), ""));
		    html.append(formatXMLElement(1, "cashier_card", this.getValue("CASHIER_CD_CARD1_HIDE"), ""));
		    
		    html.append(formatLine2CellB("&nbsp;","&nbsp;"));
	        html.append(formatXMLElement(1, "end_line", this.webposXML.getfieldTransl("cheque_end_line", false), ""));
	        html.append(formatXMLSectionEnd(0, "cheque"));
        } // try
        catch (SQLException e) {LOGGER.error(html, e);}
        catch (Exception el) {LOGGER.error(html, el);}
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
    } //getClearingPostingsHTML

    public String getInvoiceHTML() {
    	StringBuilder html = new StringBuilder();
    	//StringBuilder htmlLast = new StringBuilder();
    	
    	ArrayList<bcFeautureParam> pParam = initParamArray();
		Connection con = null;
		PreparedStatement st = null;
        
        String dateFormat = "DD.MM.RRRR";
		dateFormat = dateFormat.replaceAll("R", "y").replaceAll("Y", "y").replaceAll("m", "M").replaceAll("D", "d");
		
        String mySQL = 
        		" SELECT * " +
        		"   FROM " + getGeneralDBScheme() + ".vp$trans_all " + 
        		"  WHERE id_telgr = ? " +
        		"    AND fcd_trans_type <> 'REC_QUESTIONING'" +
        		"    AND fcd_trans_state <> 'C_IN_PROCESS_TRANS' " +
        		"  ORDER BY id_trans ";
        pParam.add(new bcFeautureParam("int", this.idTelgr));
    
        
        try{ 
        	
        	LOGGER.debug(prepareSQLToLog(mySQL,pParam));
      	  	con = Connector.getConnection(getSessionId());
      	  	st = con.prepareStatement(mySQL);
      	  	st = prepareParam(st, pParam);
      	  	ResultSet rset = st.executeQuery();
      	  	
      	  	//int transCount = 0;
	    	
			html.append(formatXMLString(0, "<?xml version=\"1.0\"?>"));
	    	html.append(formatXMLSectionBeg(0, "invoice","id=\""+this.getValue("ID_TELGR")+"\""));
	    	//html.append("<table class=\"table_cheque\"><tbody>");
	    	
	    	html.append(formatLine("2", false, "leftb", "Счет действительный при выполнении следующих условий:", "", ""));
	    	html.append(formatLine("2", false, "left", "  - сумма платежа строго соответствует указанной в счете и составляет " + this.getValue("OPR_SUM_FRMT")+ "&nbsp;" + this.getValue("SNAME_CURRENCY"), "", ""));
	    	html.append(formatLine("2", false, "left", "  - платеж произведен в течении 3-х банковских дней с даты его выставления", "", ""));
	        html.append(formatLine2CellB("&nbsp;","&nbsp;"));
	    	html.append(formatLineCenter("Счет № " + this.getValue("RRN") + " от " + this.getValue("DATE_TELGR_DF")));
	    	html.append(formatLineCenter("<hr>"));
	    	html.append(formatLine("2", false, "leftb", "<b>Плательщик:</b> " + this.getValue("CD_CARD1"), "", ""));
	    	html.append(formatLine("2", false, "leftb", "<b>Получатель:</b> " + this.getValue("SNAME_OPERATOR"), "", ""));
	    	if (!isEmpty(this.getValue("PAYMENT_DESCRIPTION"))) {
	        	html.append(formatLine("2", false, "left", "<b>"+this.webposXML.getfieldTransl("pay_description", false) + ":</b>&nbsp;" + this.getValue("PAYMENT_DESCRIPTION"), "", ""));
            	//-----------XML----------------
    	        html.append(formatXMLElement(3, "payment_description", this.getValue("PAYMENT_DESCRIPTION"), ""));
	        	
	        }
    		
		    //-----------XML----------------
		    html.append(formatXMLElement(1, "payer", this.getValue("CD_CARD1"), ""));
		    html.append(formatXMLElement(1, "receiver", this.getValue("NAME_CLUB"), ""));
  	
	        html.append(formatLine2CellB("&nbsp;","&nbsp;"));
	        
	        int rowNumber = 0;
      	  	
      	    while (rset.next()) {
      	    	rowNumber ++;
		    	String emptyCurrency = "&nbsp;";
		    	for (int i=0; i < rset.getString("SCD_CURRENCY").length(); i++) {
		    		emptyCurrency = emptyCurrency + "&nbsp;";
		    	}
    	
		    	if (isEmpty(rset.getString("ID_TRANS"))) {
			    	html.append(formatXMLString(0, "<?xml version=\"1.0\"?>"));
			    	html.append(formatXMLSectionBeg(0, "cheque","id=\""+rset.getString("ID_TRANS")+"\""));
		    		html.append(formatLineCenter(this.webposXML.getfieldTransl("cheque_error_not_found", false)));
			        //-----------XML----------------
			        html.append(formatXMLElement(1, "error", this.webposXML.getfieldTransl("cheque_error_not_found", false), ""));
		            html.append(formatXMLSectionEnd(0, "cheque"));
		            
		            return html.toString();
		    	}
		    	
		    	html.append(formatLine("1", false, "left border", rset.getString("NAME_TRANS_TYPE"), "right border", rset.getString("OPR_SUM_FRMT")+ "&nbsp;" + rset.getString("SNAME_CURRENCY")));
		    	//-----------XML----------------
		    	html.append(formatXMLElement(1, "oper"+rowNumber, rset.getString("NAME_TRANS_TYPE"), ""));
		    	html.append(formatXMLElement(1, "amount"+rowNumber, rset.getString("OPR_SUM_FRMT"), "currency=\"" + rset.getString("SCD_CURRENCY") + "\""));
		    	
      	    }
		    html.append(formatLine("1", false, "leftb border", "ИТОГО К ОПЛАТЕ:", "rightb border", this.getValue("OPR_SUM_FRMT")+ "&nbsp;" + this.getValue("SNAME_CURRENCY")));
	    	//-----------XML----------------
	    	html.append(formatXMLElement(1, "total_amount", this.getValue("OPR_SUM_FRMT"), "currency=\"" + this.getValue("SNAME_CURRENCY") + "\""));
		    
		    html.append(formatLine2CellB("&nbsp;","&nbsp;"));
	        html.append(formatXMLSectionEnd(0, "cheque"));
        } // try
        catch (SQLException e) {LOGGER.error(html, e);}
        catch (Exception el) {LOGGER.error(html, el);}
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
    } //getClearingPostingsHTML
	
	public String getInvoiceAllButtons(boolean onCheque, boolean hasStornoMenuPermission) {
		StringBuilder result = new StringBuilder();
		result.append("<script language=\"javascript\">\n");
		result.append("function validateSendInvoice(){\n");
		result.append("send_email = document.getElementById('send_EMAIL');\n");
		result.append("send_sms = document.getElementById('send_SMS');\n");
		result.append("if (send_email.checked || send_sms.checked) {\n");
		result.append("document.getElementById('div_result').innerHTML = '';\n");
		result.append("return true;\n");
		result.append("} else {\n");
		result.append("alert('" + webposXML.getfieldTransl("send_invoice_select_options_error", false) + "');\n");
		result.append("return false;\n");
		result.append("}\n");
		result.append("}\n");
		result.append("</script>\n");

		result.append("<a class=\"button button_small\" style=\"height: 22px !important; width: 120px !important; display: block; text-decoration: none; padding-top:5px\" href=\"#openModal2\">" + webposXML.getfieldTransl("title_invoice_actions", false) + "</a>\n");
		result.append("<div class=\"modalDialog\" id=\"openModal2\">\n");;
		result.append("<div>\n");
		result.append("<a class=\"close\" title=\"" + buttonXML.getfieldTransl("close", false) + "\" href=\"#close\">X</a>\n");
		result.append("<form name=\"updateForm21\" id=\"updateForm21\" accept-charset=\"UTF-8\" method=\"POST\">\n");
		result.append("<input type=\"hidden\" name=\"id_term\" value=\""+this.getValue("ID_TERM")+"\">\n");
		result.append("<input type=\"hidden\" name=\"cd_card1\" value=\""+this.getValue("CD_CARD1")+"\">\n");
		result.append("<input type=\"hidden\" name=\"id\" value=\"" + this.idTelgr + "\">\n");
		result.append("<table class=\"tablebottom\"><thead></thead><tbody>\n");
		
		//String cdTransType = this.getValue("FCD_TRANS_TYPE");
		//boolean isNotCancelOrReturnTrans = true;
		//if ("REC_CANCEL".equalsIgnoreCase(cdTransType) || "REC_RETURN".equalsIgnoreCase(cdTransType)) {
		//	isNotCancelOrReturnTrans = false;
		//}
		
		if (!onCheque /*&& isNotCancelOrReturnTrans*/) {
			if (hasStornoMenuPermission) {
				result.append("<tr><td align=\"left\" style=\"padding-top: 5px;\"><font style=\"font-weight: bold; color: blue; align:center; font-size: 13px;\">" + webposXML.getfieldTransl("title_storno", false) + "</font></td></tr>\n");
				result.append("<tr><td align=\"left\" style=\"padding-bottom: 5px;\"><button type=\"button\" class=\"button button_small\" onclick=\"ajaxpage('action/storno.jsp?id_telgr="+this.getValue("ID_TELGR") + "&storno_rrn="+this.getValue("RRN")+"&back_type=operations&' +  mySubmitForm('updateForm21'),'div_main'); this.disabled=false; this.className = 'button'; \" class=\"button\" type=\"button\">" + buttonXML.getfieldTransl("cancel", false) + "</button></td></tr>\n");
			} else {
				result.append("<tr><td align=\"left\" style=\"padding-top: 5px;\"><font style=\"font-weight: bold; color: blue; align:center; font-size: 13px;\">" + webposXML.getfieldTransl("title_storno", false) + "</font></td></tr>\n");
				result.append("<tr><td align=\"left\" style=\"padding-bottom: 5px;\"><font style=\"font-weight: bold; color: red; \">" + webposXML.getfieldTransl("operation_permission_denied", false) + "</font></td></tr>\n");
			}
		}
		
		if (onCheque) {
			result.append("<tr><td align=\"left\" style=\"padding-top: 5px;\"><font style=\"font-weight: bold; color: blue; align:center; font-size: 13px;\">" + webposXML.getfieldTransl("title_print_invoice", false) + "</font></td></tr>\n");
			result.append("<tr><td align=\"left\" style=\"padding-bottom: 5px;\"><input type=\"button\" class=\"button button_small\" onclick=\"printDiv('printableArea')\" value=\"" + buttonXML.getfieldTransl("button_print", false) + "\" /></td></tr>\n");
		} else {
			result.append("<tr><td align=\"left\" style=\"padding-top: 5px; "+/*(isNotCancelOrReturnTrans?"border-top: 1px dashed gray;":"")+*/"\"><font style=\"font-weight: bold; color: blue; align:center; font-size: 13px;\">" + webposXML.getfieldTransl("title_print_invoice", false) + "</font></td></tr>\n");
			result.append("<tr><td align=\"left\" style=\"padding-bottom: 5px;\">" + this.getInvoicePrintButton() + "</td></tr>\n");
		}
		result.append("<tr><td align=\"left\" style=\"padding-top: 5px; border-top: 1px dashed gray;\"><font style=\"font-weight: bold; color: blue; align:center; font-size: 13px;\">" + webposXML.getfieldTransl("title_save_invoice", false) + "</font></td></tr>\n");
		if (onCheque) {
			result.append("<tr><td align=\"left\" style=\"text-transform:none;\">" + webposXML.getfieldTransl("save_invoice_hint", false) + "</td></tr>\n");
		}
		result.append("<tr><td align=\"left\" style=\"text-transform:none;\">Имя файла:&nbsp;<b style=\"color:green;\">" + getInvoiceFileName(this.saveFormat) + "</b></td></tr>\n");
		//result.append("<tr><td style=\"text-transform:none;\">" + webposXML.getfieldTransl("title_cheque_save_format", false) + ":&nbsp;<b style=\"color:green;\">" + this.saveFormat + "</b></td></tr>\n");
		if (onCheque) {
			result.append("<tr><td align=\"left\" style=\"padding-bottom: 5px;\">" + getInvoiceSaveButtonOnCheque() + "</td></tr>\n");
		} else {
			result.append("<tr><td align=\"left\" style=\"padding-bottom: 5px;\">" + getInvoiceSaveButton() + "</td></tr>\n");
		}
		if (!onCheque) {
			result.append("<tr><td align=\"left\" style=\"padding-top: 5px; border-top: 1px dashed gray;\"><font style=\"font-weight: bold; color: blue; align:center; font-size: 13px;\">" + webposXML.getfieldTransl("title_check_card", false) + "</font></td></tr>\n");
			result.append("<tr><td align=\"left\" style=\"padding-bottom: 5px;\"><button type=\"button\" class=\"button button_small\" onclick=\"ajaxpage('service/check_card.jsp?' +  mySubmitForm('updateForm21'),'div_action_big'); this.disabled=false; this.className = 'button'; \" class=\"button\" type=\"button\">" + buttonXML.getfieldTransl("button_check", false) + "</button></td></tr>\n");
		}
		boolean canSendEmail = false;
		boolean canSendSMS = false;
		
		result.append("<tr><td align=\"left\" style=\"padding-top: 5px; border-top: 1px dashed gray;\"><font style=\"font-weight: bold; color: blue; align:center; font-size: 13px;\">" + webposXML.getfieldTransl("title_send_invoice", false) + "</font></td></tr>\n");
		result.append("<tr><td align=\"left\"><font style=\"font-weight: bold; color: green; align:center;\">" + webposXML.getfieldTransl("title_send_invoice_email", false) + "</font></td></tr>\n");
		if (!isEmpty(this.getValue("EMAIL_NAT_PRS"))) {
			canSendEmail = true;
			
			result.append("<tr><td align=\"left\" style=\"text-transform:none;\"><input type=\"checkbox\" value=\"Y\" id=\"send_EMAIL\" name=\"send_EMAIL\" class=\"inputfield\"><label for=\"send_EMAIL\">" + webposXML.getfieldTransl("send_invoice_email_action", false) + "<label></td></tr>\n");
			result.append("<tr><td align=\"left\" style=\"text-transform:none;\">" + webposXML.getfieldTransl("send_invoice_email_hint", false) + " <b style=\"color:green;\">" + this.getValue("EMAIL_NAT_PRS_HIDE") + "</b></td></tr>\n");
		} else {
			result.append("<tr><td align=\"left\" style=\"text-transform:none;\"><font style=\"color: red;\"><input type=\"checkbox\" style=\"visibility: hidden\" value=\"N\" id=\"send_EMAIL\" name=\"send_EMAIL\">" + webposXML.getfieldTransl("send_invoice_email_error", false) + "</font></td></tr>\n");
		}

		result.append("<tr><td align=\"left\" style=\"padding-top: 5px;\"><font style=\"font-weight: bold; color: green; align:center;\">" + webposXML.getfieldTransl("title_send_invoice_sms", false) + "</font></td></tr>\n");
		if (!isEmpty(this.getValue("PHONE_MOBILE_NAT_PRS"))) {
			if (isEmpty(this.getValue("SMS_CHEQUE_SEND_COUNT")) || "0".equalsIgnoreCase(this.getValue("SMS_CHEQUE_SEND_COUNT"))) {
				canSendSMS = true;
				
				result.append("<tr><td align=\"left\" style=\"text-transform:none;\"><input type=\"checkbox\" value=\"Y\" id=\"send_SMS\" name=\"send_SMS\" class=\"inputfield\"><label for=\"send_SMS\">" + webposXML.getfieldTransl("send_invoice_sms_action", false) + "<label></td></tr>\n");
				result.append("<tr><td align=\"left\" style=\"text-transform:none;\">" + webposXML.getfieldTransl("send_invoice_sms_hint", false) + " <b style=\"color:green;\">" + this.getValue("PHONE_MOBILE_NAT_PRS_HIDE") + "</b></td></tr>\n");
			} else {
				result.append("<tr><td align=\"left\" style=\"text-transform:none;\"><font style=\"color: red;\"><input type=\"checkbox\" style=\"visibility: hidden\" value=\"N\" id=\"send_SMS\" name=\"send_SMS\">" + webposXML.getfieldTransl("send_invoice_sms_already_send", false) + " "+this.getValue("PHONE_MOBILE_NAT_PRS_HIDE")+"</font></td></tr>\n");
			}
		} else {
			result.append("<tr><td align=\"left\" style=\"text-transform:none;\"><font style=\"color: red;\"><input type=\"checkbox\" style=\"visibility: hidden\" value=\"N\" id=\"send_SMS\" name=\"send_SMS\">" + webposXML.getfieldTransl("send_invoice_sms_error", false) + "</font></td></tr>\n");
		}
		if (canSendEmail || canSendSMS) {
			result.append("<tr><td align=\"left\"><div id=\"div_result\"><button type=\"button\" class=\"button button_small\" onclick=\" try {if (!validateSendInvoice()) {return false;} else {ajaxpage('" + (onCheque?"chequesend.jsp":"report/chequesend.jsp") + "?' +  mySubmitForm('updateForm21'),'div_result'); this.disabled=false; this.className = 'button';} } catch(err){};   \" class=\"button\" type=\"button\">" + buttonXML.getfieldTransl("send", false) + "</button></div></td></tr>\n");
		}
		result.append("</tbody></table>\n");
		result.append("</form>\n");
		result.append("</div>\n");
		result.append("</div>\n");
		return result.toString();
	}
	
	public String getInvoicePrintButton() {
		return "<input type=\"button\" class=\"button button_small\" onclick=\"JavaScript: var cWin=window.open('report/invoice.jsp?id_telgr=" + this.idTelgr + "&print=Y','blank','height=600,width=420,top=50,left=150,toolbar=no,menubar=no,location=no,scrollbars=yes,status=no'); cWin.focus(); cWin.printDiv('printableArea');\" value=\"" + buttonXML.getfieldTransl("button_print", false) +"\" />";
	}
	
	public String getInvoiceSaveButtonOnCheque() {
		if ("Y".equalsIgnoreCase(canSaveCheque)) {
			return "<input type=\"button\" class=\"button button_small\" onclick=\"JavaScript: var cWin=window.open('report/invoicesave.jsp?id_telgr=" + this.idTelgr + "&cheque_format=" + this.saveFormat + "','blank','height=600,width=900,top=50,left=150,toolbar=no,menubar=no,location=no,scrollbars=yes,status=yes'); cWin.focus();\" value=\"" + buttonXML.getfieldTransl("button_save", false) +"\" />";
		} else {
			return "";
		}
	}
	
	public String getInvoiceSaveButton() {
		if ("Y".equalsIgnoreCase(canSaveCheque)) {
			return "<input type=\"button\" class=\"button button_small\" onclick=\"JavaScript: var cWin=window.open('report/invoicesave.jsp?id_telgr=" + this.idTelgr + "&cheque_format=" + this.saveFormat + "','blank','height=600,width=900,top=50,left=150,toolbar=no,menubar=no,location=no,scrollbars=yes,status=yes'); cWin.focus();\" value=\"" + buttonXML.getfieldTransl("button_save", false) +"\" />";
		} else {
			return "";
		}
	}

	public String getInvoiceFileName(String pChequeFormat) {
		StringBuilder fileName = new StringBuilder();
		
		String idFileName = idTelgr;
		String rrn = this.getValue("RRN");
		fileName.append("smpu_invoice_" + idFileName + "_" + rrn + (("TXT".equalsIgnoreCase(pChequeFormat))?".txt":".xml"));
		
		return fileName.toString();
	}
}
