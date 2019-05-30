<%@page import="java.net.URLDecoder"%>
<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcWebPosBean"  />

<%@page import="java.util.HashMap"%>
<%@page import="webpos.wpClubCardObject"%>
<%@page import="webpos.wpNatPrsObject"%>
<%@page import="webpos.wpChequeObject"%>
<%@page import="java.util.ArrayList"%>
<%@page import="webpos.wpNatPrsTargetPrgObject"%>
<%@ page import = "javax.servlet.RequestDispatcher" %>
<%@ page import="org.apache.log4j.Logger" %>
 

<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.io.PrintWriter"%>
<%@page import="java.io.IOException"%>
<%@page import="bc.util.JndiUtils"%>
<%@page import="java.util.Date"%>
<%@page import="webpos.wpFNInvoiceLineObject"%>
<%@page import="webpos.wpFNInvoiceObject"%><html>
<head>
	<%= Bean.getMetaContent() %>
</head>

<body>
<%
Logger LOGGER = Logger.getLogger( "/report/operationupdate" );
HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());
LOGGER.debug("parameters="+parameters.toString());

String pageFormName = "WEBPOS_SERVICE_OPERATION";

Bean.setJspPageForTabName(pageFormName);

request.setCharacterEncoding("UTF-8");
String type		= Bean.getDecodeParam(parameters.get("type")); 
String action	= Bean.getDecodeParam(parameters.get("action")); 
String process	= Bean.getDecodeParam(parameters.get("process"));

type 	= Bean.isEmpty(type)?"":type;
action 	= Bean.isEmpty(action)?"":action;
process = Bean.isEmpty(process)?"":process;

String id_telgr	= Bean.getDecodeParam(parameters.get("id_telgr"));

Bean.readWebPosMenuHTML();

Bean.loginTerm.getTermFeature();
String termCurrency = Bean.loginTerm.getValue("SNAME_TERM_CURRENCY");

String resultInt 				= Bean.C_SUCCESS_RESULT;
String resultMessage 			= "";
%>


<% if (!Bean.hasMenuPermission(pageFormName, Bean.C_WRITE_MENU_PERMISSION, Bean.loginTerm)) { %>
	<%=Bean.getErrorPermissionMessageShort(pageFormName, Bean.loginTerm) %>
	<% type = "error_term"; %>
<% } %>
<%
if (type.equalsIgnoreCase("error_term")) {
} else if (type.equalsIgnoreCase("trans")) {
	
	wpChequeObject oper = new wpChequeObject(id_telgr, "TXT", Bean.loginTerm);
	
	boolean canOperWithInvoice = false;
	if ("SENT_FOR_PAYMENT".equalsIgnoreCase(oper.getValue("CD_TELGR_STATE"))){
		canOperWithInvoice = true;
	}

	// Подготовлены данные для отправки на robokassa.ru
	// в соответствии с https://www.robokassa.ru/ru/DemoShop/DemoFixed.aspx
	
	boolean canPayFromTerm      = "Y".equalsIgnoreCase(Bean.loginTerm.getValue("HAS_ONLINE_PAY_RBK_PERMISSION"));
	boolean canPayRobokassa1	= false;
	boolean canCheckRobokassa	= false;
	
	String payRobokassaLink 			= "";
	String checkRobokassaPaymentResult 	= "";
	
	//if (canOperWithInvoice) {
		payRobokassaLink 	= Bean.getRobokassaPaymentLink(oper);
			
		if (!Bean.isEmpty(payRobokassaLink)) {
			canPayRobokassa1 	= true;
		}
		
		if (action.equalsIgnoreCase("check_payment")) {
			checkRobokassaPaymentResult 	= Bean.getRobokassaCheckPaymentResultLink(oper);
			
			if (!Bean.isEmpty(checkRobokassaPaymentResult)) {
				canCheckRobokassa 	= true;
			}
		}
	//}
	//canPayRobokassa 	= true;
	//canCheckRobokassa 	= true;
	
	/*
	robokassa collaboration example 
	
	String findPaymentLink=bc.util.JndiUtils.readJndi("robokassa/component/url")+"/payment/findPayment?userId=10&dateBeginInclude=2015-10-01_00:00:00&dateEndExclude=2015-11-01_00:00:00&paymentType=PAID&maxRows=10";
	String findPaymentRawBody=bc.util.RestUtils.getString(findPaymentLink);
	ObjectMapper mapper = new ObjectMapper();
	PaymentExternalVO[] data = mapper.readValue(findPaymentRawBody, PaymentExternalVO[].class);
	*/
	
	if (process.equalsIgnoreCase("no")) {
		
	} else if (process.equalsIgnoreCase("yes")) {
		if (action.equalsIgnoreCase("save")) {
			
			if (canOperWithInvoice) {
				String
					payment_description		= Bean.getDecodeParam(parameters.get("payment_description"));
	
				String[] results = new String[2];
				
				ArrayList<String> pParam = new ArrayList<String>();
				
				pParam.add(id_telgr);
				pParam.add(payment_description);
				
				results 		= Bean.executeFunction("PACK$WEBPOS_UI.change_payment_description", pParam, results.length);
				resultInt 		= results[0];
		 		resultMessage 	= results[1];
			} else {
				resultInt 		= Bean.C_ERROR_RESULT;
		 		resultMessage 	= Bean.webposXML.getfieldTransl("operation_permission_denied", false);
			}
		} else if (action.equalsIgnoreCase("send_robokassa_pay")) {
			
			if (canOperWithInvoice) {
				String
					send_by_sms			= Bean.getDecodeParam(parameters.get("send_robokassa_payment_SMS")),
					phone_mobile		= Bean.getDecodeParam(parameters.get("robokassa_payment_phone")),
					send_by_email		= Bean.getDecodeParam(parameters.get("send_robokassa_payment_EMAIL")),
					email				= Bean.getDecodeParam(parameters.get("robokassa_payment_email"));

				String[] results = new String[2];
				
				ArrayList<String> pParam = new ArrayList<String>();
				
				pParam.add(id_telgr);
				pParam.add(send_by_sms);
				pParam.add(phone_mobile);
				pParam.add(send_by_email);
				pParam.add(email);
				pParam.add(payRobokassaLink);
				
				results 		= Bean.executeFunction("PACK$WEBPOS_UI.send_invoice_robokassa_pay", pParam, results.length);
				resultInt 		= results[0];
		 		resultMessage 	= results[1];
			} else {
				resultInt 		= Bean.C_ERROR_RESULT;
		 		resultMessage 	= Bean.webposXML.getfieldTransl("operation_permission_denied", false);
			}
		} else if (action.equalsIgnoreCase("send_invoice")) {
				
				String
					send_by_email		= Bean.getDecodeParam(parameters.get("send_invoice_payment_EMAIL")),
					email				= Bean.getDecodeParam(parameters.get("invoice_payment_email"));
				
				String fileName = "";
				String fileNameFull = "";
				
				if ("Y".equalsIgnoreCase(send_by_email)) {
					SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
					fileName = "invoice_" + id_telgr + "_" + sdf.format(new Date()) + ".txt";
					fileNameFull = JndiUtils.readJndi("${wmpupt/document/uploaded}") + "/" + fileName;
					
					try { 
						PrintWriter writer = new PrintWriter(fileNameFull, "utf-8");
						writer.println(oper.getInvoiceHTML());
						writer.close();
					} catch (IOException e) { 
						System.out.println("EXCEPTION: "+ e.toString());
					}
				}

				String[] results = new String[2];
				
				ArrayList<String> pParam = new ArrayList<String>();
				
				pParam.add(id_telgr);
				pParam.add(send_by_email);
				pParam.add(email);
				pParam.add(fileName);
				pParam.add(fileNameFull);
				
				results 		= Bean.executeFunction("PACK$WEBPOS_UI.send_invoice", pParam, results.length);
				resultInt 		= results[0];
		 		resultMessage 	= results[1];
		} else if (action.equalsIgnoreCase("confirm")) {

			if ("NEED_CONFIRM".equalsIgnoreCase(oper.getValue("CD_TELGR_STATE"))) {
				String[] results = new String[2];
			
				ArrayList<String> pParam = new ArrayList<String>();
				
				pParam.add(id_telgr);
				
				results 		= Bean.executeFunction("PACK$WEBPOS_UI.confirm_robokassa_pay", pParam, results.length);
				resultInt 		= results[0];
		 		resultMessage 	= results[1];
			} else {
				resultInt 		= Bean.C_ERROR_RESULT;
		 		resultMessage 	= Bean.webposXML.getfieldTransl("operation_permission_denied", false);
			}
		} else if (action.equalsIgnoreCase("check_payment")) {
			if (canOperWithInvoice) {
				if (canCheckRobokassa) {
					String stateOper = "";
					
					String[] results = new String[2];
					
					ArrayList<String> pParam = new ArrayList<String>();
					
					pParam.add(id_telgr);
					pParam.add(("false".equalsIgnoreCase(checkRobokassaPaymentResult))?"WAITING":"PAID");
					
					results 		= Bean.executeFunction("PACK$WEBPOS_UI.set_robokassa_pay_result", pParam, results.length);
					resultInt 		= results[0];
			 		resultMessage 	= results[1];
				}
			} else {
				resultInt 		= Bean.C_ERROR_RESULT;
		 		resultMessage 	= Bean.webposXML.getfieldTransl("operation_permission_denied", false);
			}
		} else if (action.equalsIgnoreCase("oper_confirm")) {
			
			String	pay_description		= Bean.getDecodeParam(parameters.get("pay_description"));

			String[] results = new String[4];
				
			ArrayList<String> pParam = new ArrayList<String>();
				
			pParam.add(id_telgr);
			pParam.add("NONE");
			pParam.add("");
			pParam.add(pay_description);
				
			results 					= Bean.executeFunction("PACK$WEBPOS_UI.oper_confirm", pParam, results.length);
			resultInt 					= results[0];
			String phone_mobile	 		= results[1];
			String can_send_pin_in_sms  = results[2];
	 		resultMessage 				= results[3];
		 		
	 		action = "pay";
		}
 		
 		oper = new wpChequeObject(id_telgr, "TXT", Bean.loginTerm);
		
	} else {
	    %> <%= Bean.getUnknownProcessText(process) %> <%
	}
	
	String oper_types		= Bean.getTransactionShortNameList(oper);
	String oper_state 		= oper.getValue("NAME_TELGR_STATE");
	String cd_pay_type 		= oper.getValue("PAY_TYPE");
	String name_pay_type 	= oper.getValue("NAME_TRANS_PAY_TYPE");
	String opr_sum 			= oper.getValue("OPR_SUM_FRMT");
	String email_operation  = oper.getValue("EMAIL_NAT_PRS");
	
	String snameCurrency 	= ("SMPU_CARD".equalsIgnoreCase(cd_pay_type))?Bean.webposXML.getfieldTransl("title_transfer_points_currency", false):termCurrency;
	
		%>
		<script>

		function validateData(){
			var returnValue = null;
			var formAll = new Array (
			);
			var formSMS = new Array (
				new Array ('invoice_payment_phone', 'varchar2', 1)
			);
			var formEmail = new Array (
				new Array ('invoice_payment_email', 'varchar2', 1)
			);

			try {
				var send_sms = document.getElementById('send_invoice_payment_SMS');
				if (send_sms.checked) {
					formAll = formAll.concat(formSMS);
				}
			} catch(err) {}
			try {
				var send_email = document.getElementById('send_invoice_payment_EMAIL');
				if (send_email.checked) {
					formAll = formAll.concat(formEmail);
				}
			} catch(err) {}
			//alert(formAll);
			returnValue = validateFormForID(formAll, 'updateForm');
			return returnValue;
		}
		
	</script>
		<h1 class="confirm"><%=Bean.webposXML.getfieldTransl("operation_cheque_payment", false) %>: <%=oper_state.toLowerCase() %>&nbsp;<%=Bean.getHelpButton("pay_confirm", "div_action_big") %></h1>
		<form name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST">
			<input type="hidden" name="type" value="trans">
			<input type="hidden" name="process" value="yes">
			<input type="hidden" name="id_telgr" value="<%=id_telgr %>">
			<input type="hidden" name="back_type" value="oper">
			<input type="hidden" name="id_term" id="id_term" value="<%=oper.getValue("ID_TERM") %>">
			<input type="hidden" name="cd_card1" id="cd_card1" value="<%=oper.getValue("CD_CARD1") %>">
		    <table class="action_table">
				<!-- <tr><td <%=Bean.getDetailTableFirstColumnWidth() %>><%=Bean.webposXML.getfieldTransl("cheque_number_full", false) %></td><td><input type="text" name="doc_number" id="doc_number" size="20" value="<%=oper.getValue("NC") %>" readonly class="inputfield_finish"></td></tr> -->
				<tr><td <%=Bean.getDetailTableFirstColumnWidth() %>><%=Bean.webposXML.getfieldTransl("cheque_invoice", false) %></td><td><input type="text" name="rrn_txt" id="rrn_txt" size="20" value="<%=oper.getValue("RRN") %>" readonly class="inputfield_finish_blue"></td></tr>
			  	<tr><td <%=Bean.getDetailTableFirstColumnWidth() %>><%=Bean.webposXML.getfieldTransl("card", false) %></td><td><input type="text" name="cd_card1_hide" id="cd_card1_hide" size="20" value="<%=oper.getValue("CD_CARD1_HIDE") %>" readonly class="inputfield_finish_green"></td></tr>
	  			<tr><td><%=Bean.webposXML.getfieldTransl("operation_date", false) %></td><td><input type="text" name="operation_date" id="operation_date" size="20" value="<%=oper.getValue("DATE_TELGR_DHMF") %>" readonly class="inputfield_finish_green"></td><tr>
				<% if (!Bean.isEmpty(oper.getValue("EXPIRATION_DATE"))) { %>
	  			<tr><td><%=Bean.webposXML.getfieldTransl("invoice_expiration_date", false) %></td><td><input type="text" name="expiration_date" id="expiration_date" size="20" value="<%=oper.getValue("EXPIRATION_DATE_DHMF") %>" readonly class="inputfield_finish_red"></td><tr>
				<% } %>
				<tr><td><%=Bean.webposXML.getfieldTransl("operation_type", false) %></td><td colspan="2"><span id="operation_type" class="inputfield_finish_red"><%=oper_types %></span></td></tr>
				<tr><td class="result_desc"><%=Bean.webposXML.getfieldTransl("operation_sum", false) %></td><td><input type="text" name="input_sum" id="input_sum" size="20" value="<%=opr_sum + " " + snameCurrency %>" readonly class="inputfield_finish_blue"></td></tr>
				<% if (action.equalsIgnoreCase("confirm")) { %>
					<tr><td colspan="2">&nbsp;</td></tr>
					<tr><td colspan="2"><b><%=Bean.webposXML.getfieldTransl("title_external_pay_result", false) %></b></td><td>
					<tr>
						<td><%=Bean.webposXML.getfieldTransl("cd_telgr_external_state", false) %></td>
						<td>
							<% String externalStateClass = "inputfield_finish_green";
								if ("ERROR".equalsIgnoreCase(oper.getValue("CD_TELGR_EXTERNAL_STATE"))) {
									externalStateClass = "inputfield_finish_red";
								} else if ("EXPIRED".equalsIgnoreCase(oper.getValue("CD_TELGR_EXTERNAL_STATE")) ||
										"CANCELLED".equalsIgnoreCase(oper.getValue("CD_TELGR_EXTERNAL_STATE"))) {
									externalStateClass = "inputfield_finish_gray";
								} else if ("WAITING".equalsIgnoreCase(oper.getValue("CD_TELGR_EXTERNAL_STATE"))) {
									externalStateClass = "inputfield_finish_blue";
								}
							%>
							<input type="text" name="cd_telgr_external_state" id="cd_telgr_external_state" size="20" value="<%=Bean.getTelgrExternalStateName(oper.getValue("CD_TELGR_EXTERNAL_STATE")) %>" readonly class="<%=externalStateClass %>">
						</td>
					<tr>
					<% if (!Bean.isEmpty(oper.getValue("DATE_EXTERNAL_PAY"))) { %>
					<tr>
						<td><%=Bean.webposXML.getfieldTransl("date_external_pay", false) %></td>
						<td>
							<input type="text" name="date_external_pay" id="date_external_pay" size="20" value="<%=oper.getValue("DATE_EXTERNAL_PAY_DF") %>" readonly class="inputfield_finish_green">
						</td>
					<tr>
					<% } %>
				<% } %>
				<% if (action.equalsIgnoreCase("confirm") || action.equalsIgnoreCase("oper_confirm")) {%>
					<% if (process.equalsIgnoreCase("yes")) {%>
						<% if (!(Bean.C_SUCCESS_RESULT.equalsIgnoreCase(resultInt))) { %>
						<tr><td colspan="2"><span id="error_title"><%=Bean.webposXML.getfieldTransl("operation_error", false) %></span></td></tr>
						<tr><td colspan="2"><span id="error_description"><%=resultMessage %></span></td></tr>
						<% } else { %>
						<tr><td colspan="2"><span id="confirm_description"><%=resultMessage %></span></td></tr>
						<% } %>
						<tr><td colspan="2">&nbsp;</td></tr>
					<% } %>
				<% } %>
	 			<% if (action.equalsIgnoreCase("save") || action.equalsIgnoreCase("pay") || action.equalsIgnoreCase("oper_confirm") || action.equalsIgnoreCase("check_payment") || action.equalsIgnoreCase("send_invoice") || action.equalsIgnoreCase("send_robokassa_pay")) {%> 
					<% if ("SENT_FOR_PAYMENT".equalsIgnoreCase(oper.getValue("CD_TELGR_STATE"))){ %>
					<tr><td colspan="2" align="left" style="padding-top: 5px; border-top: 1px dashed gray;"><font style="font-weight: bold; color: blue; align:center; font-size: 13px;">Отправка счета</font></td></tr>
						<% if (action.equalsIgnoreCase("save") || action.equalsIgnoreCase("pay") || action.equalsIgnoreCase("check_payment") || action.equalsIgnoreCase("send_invoice") || action.equalsIgnoreCase("send_robokassa_pay")) {%> 
							<%=Bean.getInvoiceSendWays("invoice", oper) %>
						<% } %>
					<% if (action.equalsIgnoreCase("send_invoice") && process.equalsIgnoreCase("yes")) {%>
						<% if (!(Bean.C_SUCCESS_RESULT.equalsIgnoreCase(resultInt))) { %>
							<tr><td colspan="2"><span id="error_title"><%=Bean.webposXML.getfieldTransl("operation_error", false) %></span></td></tr>
							<tr><td colspan="2"><span id="error_description"><%=resultMessage %></span></td></tr>
						<% } else { %>
							<tr><td colspan="2"><span id="confirm_description"><%=resultMessage %></span></td></tr>
						<% } %>
					<% } %>
					<tr><td colspan="2"  align="center">
						<%=Bean.getSubmitButtonAjax("report/operationupdate.jsp?action=send_invoice&", "send", "updateForm", "div_action_big", "validateData") %>
					</td></tr>
					<tr><td colspan="2" align="left" style="padding-top: 5px; border-top: 1px dashed gray;"><font style="font-weight: bold; color: blue; align:center; font-size: 13px;">Оплатить</font></td></tr>
						<tr>
							<td colspan="4">
								<table border="0">
									<tr>
										<%=Bean.getPayTypeImage("CASH", "REPORT_INVOICE_CASH", "report/invoicepay.jsp?pay_type=CASH&process=no", "div_action_big", "updateFormPay", "") %>
										<%=Bean.getPayTypeImage("SMPU_CARD", "REPORT_INVOICE_BANK_CARD", "report/invoicepay.jsp?pay_type=SMPU_CARD&process=yes", "div_action_big", "updateFormPay", "") %>
										<%=Bean.getPayTypeImage("BANK_CARD", "REPORT_INVOICE_POINTS", "report/invoicepay.jsp?pay_type=BANK_CARD&process=no", "div_action_big", "updateFormPay", "") %>
										<% if (canPayRobokassa1 && canPayFromTerm) { %> 
										<%=Bean.getPayTypeImage("ROBOKASSA", "REPORT_INVOICE_ROBOKASSA", "report/invoicepay.jsp?pay_type=ROBOKASSA&process=no", "div_action_big", "updateFormPay", "") %>
										<% } else { %>
										<%=Bean.getPayTypeImageDisable("ROBOKASSA") %>
										<% } %>
									</tr>
								</table>
							</td>
						</tr>
					<% if (!canPayRobokassa1) { %> 
						<tr><td colspan="2"><span id="error_description"><%=Bean.webposXML.getfieldTransl("pay_robokassa_no_service_connection_error", false) %></span></td></tr>
					<% } else { %>
						<% if (!canPayFromTerm) { %>
						<tr><td colspan="2"><span id="error_description">Оплата с помощью Робокассы непосредственно с терминала запрещена настройками терминала</span></td></tr>
						<% } %>
					<% } %>
				<tr><td colspan="2" align="left" style="padding-top: 5px; border-top: 1px dashed gray;"><font style="font-weight: bold; color: blue; align:center; font-size: 13px;">Отправка информации об оплате через Робокассу</font></td></tr>
					<% if (action.equalsIgnoreCase("save") || action.equalsIgnoreCase("pay") || action.equalsIgnoreCase("check_payment") || action.equalsIgnoreCase("send_invoice") || action.equalsIgnoreCase("send_robokassa_pay")) {%> 
						<%=Bean.getInvoiceSendWays("robokassa", oper) %>
					<% } %>
				<% if (action.equalsIgnoreCase("send_robokassa_pay") && process.equalsIgnoreCase("yes")) {%>
					<% if (!(Bean.C_SUCCESS_RESULT.equalsIgnoreCase(resultInt))) { %>
						<tr><td colspan="2"><span id="error_title"><%=Bean.webposXML.getfieldTransl("operation_error", false) %></span></td></tr>
						<tr><td colspan="2"><span id="error_description"><%=resultMessage %></span></td></tr>
					<% } else { %>
						<tr><td colspan="2"><span id="confirm_description"><%=resultMessage %></span></td></tr>
					<% } %>
				<% } %>
				<tr><td colspan="2"  align="center">
					<%=Bean.getSubmitButtonAjax("report/operationupdate.jsp?action=send_robokassa_pay&", "send", "updateForm", "div_action_big", "validateData") %>
				</td></tr>
				<% } else { %>
				<% } %>
			<% if (canPayRobokassa1) { %> 
				<tr><td colspan="2" align="left" style="padding-top: 5px; border-top: 1px dashed gray;"><font style="font-weight: bold; color: blue; align:center; font-size: 13px;">Проверка оплаты через Робокассу</font></td></tr>
				<% if (action.equalsIgnoreCase("check_payment") && process.equalsIgnoreCase("yes")) {%>
					<% if (canCheckRobokassa) { %>
						<% if (!Bean.C_SUCCESS_RESULT.equalsIgnoreCase(resultInt)) { %>
						<tr><td colspan="2"><span id="error_title"><%=Bean.webposXML.getfieldTransl("operation_error", false) %></span></td></tr>
						<tr><td colspan="2"><span id="error_description"><%=resultMessage %></span></td></tr>
						<% } else { %>
						<tr><td colspan="2"><span id="confirm_description"><b>Результат проверки: <%=resultMessage %></b></span></td></tr>
						<% } %>
					<% } else { %>
					<tr><td colspan="2"><span id="error_description"><%=Bean.webposXML.getfieldTransl("check_robokassa_no_service_connection_error", false) %></span></td></tr>
					<% } %>
				<% } else { %>
					<tr><td colspan="2" align="left" style="text-transform:none;">Проверить, оплачен ли счет с помощью Робокассы</td></tr>
				<% } %>
				<tr><td colspan="2"  align="center">
					<%=Bean.getSubmitButtonAjax("report/operationupdate.jsp?action=check_payment&", "check", "updateFormCheck", "div_action_big", "") %>
					<%=Bean.getSubmitButtonAjax("report/operation.jsp", "button_back", "updateFormBack", "div_main") %>
				</td></tr>
			<% } else { %>
			<tr><td colspan="2" align="left" style="padding-top: 5px; border-top: 1px dashed gray;"></td></tr>
				<tr><td colspan="2"  align="center">
					<%=Bean.getSubmitButtonAjax("report/operation.jsp", "button_back", "updateFormBack", "div_main") %>
				</td></tr>
			<% } %>
		<%} else if (action.equalsIgnoreCase("confirm")) {%> 
				<tr><td colspan="2">&nbsp;</td></tr>
				<tr><td colspan="2"  align="center">
				<% if ("NEED_CONFIRM".equalsIgnoreCase(oper.getValue("CD_TELGR_STATE")) &&
						"PAID".equalsIgnoreCase(oper.getValue("CD_TELGR_EXTERNAL_STATE"))) { %>
				<%=Bean.getSubmitButtonAjax("report/operationupdate.jsp?action=confirm&", "confirm", "updateForm", "div_action_big", "validateData") %>
				<% } %> 
				<%=Bean.getSubmitButtonAjax("report/operation.jsp", "button_back", "updateFormBack", "div_main") %>
			</td></tr>			
		<%} else { %> 
			<tr><td colspan="2"  align="center">
				<%= Bean.getUnknownActionText(action) %><br>
				<%=Bean.getSubmitButtonAjax("report/operation.jsp", "button_back", "updateFormBack", "div_main") %>
			</td></tr>
		<% }%>

			</table>
		</form>
		<form name="updateFormPay" id="updateFormPay" accept-charset="UTF-8" method="POST">
			<input type="hidden" name="type" value="term">
		    <input type="hidden" name="action" value="calc">
			<input type="hidden" name="pay_value" value="<%=oper.getValue("OPR_SUM") %>">
			<input type="hidden" name="id_telgr_invoice" value="<%=id_telgr %>">
			<input type="hidden" name="id_term" id="id_term" value="<%=oper.getValue("ID_TERM") %>">
			<input type="hidden" name="cd_card1" id="cd_card1" value="<%=oper.getValue("CD_CARD1") %>">
		</form>
		<form name="updateFormCheck" id="updateFormCheck" accept-charset="UTF-8" method="POST">
			<input type="hidden" name="type" value="trans">
			<input type="hidden" name="process" value="yes">
		    <input type="hidden" name="action" value="check_payment">
			<input type="hidden" name="id_telgr" value="<%=id_telgr %>">
			<input type="hidden" name="back_type" value="oper">
			<input type="hidden" name="id_term" id="id_term" value="<%=oper.getValue("ID_TERM") %>">
			<input type="hidden" name="cd_card1" id="cd_card1" value="<%=oper.getValue("CD_CARD1") %>">
		</form>
		<form name="updateFormBack" id="updateFormBack" accept-charset="UTF-8" method="POST">
			<input type="hidden" name="action" value="show">
		</form>
		<form name="updateFormCancel" id="updateFormCancel" accept-charset="UTF-8" method="POST">
			<input type="hidden" name="id_telgr" value="<%=id_telgr %>">
			<input type="hidden" name="storno_rrn" value="<%=oper.getValue("RRN") %>">
			<input type="hidden" name="back_type" value="operations">
		</form>

<%
} else if (type.equalsIgnoreCase("invline")) {

	String 
		id_invoice		= Bean.getDecodeParam(parameters.get("id_invoice")),
		id_invoice_line	= Bean.getDecodeParam(parameters.get("id_invoice_line"));
	
	wpFNInvoiceObject invoice = new wpFNInvoiceObject(id_invoice);
	
	String
		order_number 				= Bean.getDecodeParamPrepare(parameters.get("order_number")),
		name_nomenkl 				= Bean.getDecodeParamPrepare(parameters.get("name_nomenkl")),
		count_nomenkl				= Bean.getDecodeParamPrepare(parameters.get("count_nomenkl")),
		price_nomenkl 				= Bean.getDecodeParamPrepare(parameters.get("price_nomenkl")),
		tax_percent			 		= Bean.getDecodeParamPrepare(parameters.get("tax_percent")),
		LUD_detail					= Bean.getDecodeParamPrepare(parameters.get("LUD_detail"));
	
	if (process.equalsIgnoreCase("yes"))	{
		%>
		<body>
		<%

		ArrayList<String> pParam = new ArrayList<String>();

		if (action.equalsIgnoreCase("add")) {
			
			String[] results = new String[3]; 

			pParam.add(id_invoice);
			pParam.add(name_nomenkl);
			pParam.add(count_nomenkl);
			pParam.add(price_nomenkl);
			pParam.add(tax_percent);
			
			results 		= Bean.executeFunction("PACK$INVOICE_UI.add_invoice_line", pParam, results.length);
			resultInt 		= results[0];
			id_invoice_line = results[1];
	 		resultMessage 	= results[2];

		} else if (action.equalsIgnoreCase("remove")) {
			
			String[] results = new String[2]; 
					
			pParam.add(id_invoice_line);
			
			results 		= Bean.executeFunction("PACK$INVOICE_UI.delete_invoice_line", pParam, results.length);
			resultInt 		= results[0];
	 		resultMessage 	= results[1];
	
		} else if (action.equalsIgnoreCase("edit")) { 
			
			String[] results = new String[2];
					
	 		pParam.add(id_invoice_line);
			pParam.add(name_nomenkl);
			pParam.add(count_nomenkl);
			pParam.add(price_nomenkl);
			pParam.add(tax_percent);
			pParam.add(LUD_detail);
			
			results 		= Bean.executeFunction("PACK$INVOICE_UI.update_invoice_line", pParam, results.length);
			resultInt 		= results[0];
	 		resultMessage 	= results[1];
				
		} else { 
			resultInt = Bean.C_ERROR_RESULT;
			resultMessage = Bean.form_messageXML.getfieldTransl("unknown_action", false);
		}
		if (Bean.C_SUCCESS_RESULT.equalsIgnoreCase(resultInt)) {
			process = "exit";
			
			String tagContent = "_CONTENT";
			String tagContentFind = "_CONTENT_FIND";
			
			String l_content_page = Bean.getDecodeParam(parameters.get("content_page"));
			Bean.pageCheck(pageFormName + tagContent, l_content_page);
			String l_content_page_beg = Bean.getFirstRowNumber(pageFormName + tagContent);
			String l_content_page_end = Bean.getLastRowNumber(pageFormName + tagContent);

			String content_find	 	= Bean.getDecodeParam(parameters.get("content_find"));
			content_find 				= Bean.checkFindString(pageFormName + tagContentFind, content_find, l_content_page);
			
			%>
			<script type="text/javascript">
				ajaxpage('report/operation.jsp?tab=3&id_invoice=<%=id_invoice%>&action=invdetail', 'div_main');
			</script>
			<%
		} else {
			process = "no";
		}
	}
	
	if (process.equalsIgnoreCase("no")) {
    	if (action.equalsIgnoreCase("add") || action.equalsIgnoreCase("add2")) {
    		
    		%>
		<script>
			var formData = new Array (
				new Array ('name_nomenkl', 'varchar2', 1),
				new Array ('count_nomenkl', 'number', 1),
				new Array ('price_nomenkl', 'oper_sum_zero', 1),
				new Array ('tax_percent', 'oper_sum_zero', 1)
			);
			function myValidateForm(){
				return validateForm(formData);
			}
			calcInvoiceLineTotalSum();
		</script>
<body> 

    <form action="../crm/finance/invoiceupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">
       	<input type="hidden" name="type" value="invline">
        <input type="hidden" name="action" value="add">
        <input type="hidden" name="process" value="yes">
        <input type="hidden" name="id_invoice" value="<%= id_invoice %>">
        <input type="hidden" name="content_page" value="1">
	<table <%=Bean.getTableBottomFilter() %>>
		<tr>
			<td colspan="2"><b><center><%=Bean.clearingXML.getfieldTransl("h_add_invoice_line", false) %></center></b></td>
		</tr>
		<tr>
			<td><%= Bean.clearingXML.getfieldTransl("name_nomenkl", true) %> </td><td><input type="text" name="name_nomenkl" size="40" value="<%=name_nomenkl %>" class="inputfield"></td>
		</tr>
		<tr>
			<td><%= Bean.clearingXML.getfieldTransl("count_nomenkl", true) %> </td><td><input type="text" name="count_nomenkl" id="count_nomenkl" size="20" value="<%=count_nomenkl %>" class="inputfield" onchange="calcInvoiceLineTotalSum();"></td>
		</tr>
 		<tr>
			<td><%= Bean.clearingXML.getfieldTransl("price_nomenkl", true) %>, <%=invoice.getValue("SNAME_CURRENCY") %></td><td><input type="text" name="price_nomenkl" id="price_nomenkl" size="20" value="<%=price_nomenkl %>" class="inputfield" onchange="calcInvoiceLineTotalSum();"></td>
		</tr>
		<tr>
			<td><b><%= Bean.clearingXML.getfieldTransl("total_sum", false) %>, <%=invoice.getValue("SNAME_CURRENCY") %></b></td><td><input type="text" name="total_amount" id="total_amount" size="20" value="" readonly="readonly" class="inputfield-ro"></td>
		</tr>
 		<tr>
			<td><%= Bean.clearingXML.getfieldTransl("tax_percent", true) %> </td><td><input type="text" name="tax_percent" id="tax_percent" size="20" value="<%=tax_percent %>" class="inputfield" onchange="calcInvoiceLineTotalSum();"></td>
		</tr>
		<tr>
			<td><%= Bean.clearingXML.getfieldTransl("tax_sum", false) %>, <%=invoice.getValue("SNAME_CURRENCY") %> </td><td><input type="text" name="tax_amount" id="tax_amount" size="20" value="" readonly="readonly" class="inputfield-ro"></td>
		</tr>
			<% if (!Bean.isEmpty(resultMessage)) { %>
				<% if (!(Bean.C_SUCCESS_RESULT.equalsIgnoreCase(resultInt))) { %>
				<tr><td colspan="2">&nbsp;</td></tr>
				<tr><td colspan="2"><span id="error_title"><%=Bean.webposXML.getfieldTransl("operation_error", false) %></span></td></tr>
				<tr><td colspan="2"><span id="error_description"><%=resultMessage %></span></td></tr>
				<% } else { %>
				<tr><td colspan="2">&nbsp;</td></tr>
				<tr><td colspan="2"><span id="confirm_description"><%=resultMessage %></span></td></tr>
				<% } %>
			<% } %>
  		<tr>
			<td colspan="6" align="center">
				<%=Bean.getSubmitButtonAjax("report/operationupdate.jsp", "button_add", "updateForm1", "div_invoice_line") %>
				<%=Bean.getResetButton() %>
				<%=Bean.getGoBackButton("report/operation.jsp?tab=3&action=invdetail&id_invoice=" + id_invoice, "button_back", "div_main") %>
			</td>
		</tr>

	</table>

	</form>

    <%
		} else if (action.equalsIgnoreCase("edit")) {
			
			wpFNInvoiceLineObject line = new wpFNInvoiceLineObject(id_invoice_line);
    		
    		%>
		<script>
			var formData = new Array (
				new Array ('name_nomenkl', 'varchar2', 1),
				new Array ('count_nomenkl', 'number', 1),
				new Array ('price_nomenkl', 'oper_sum_zero', 1),
				new Array ('tax_percent', 'oper_sum_zero', 1)
			);
			function myValidateForm(){
				return validateForm(formData);
			}
			calcInvoiceLineTotalSum();
		</script>
<body> 
    <form action="../crm/finance/invoiceupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">
       	<input type="hidden" name="type" value="invline">
        <input type="hidden" name="action" value="edit">
        <input type="hidden" name="process" value="yes">
        <input type="hidden" name="id_invoice" value="<%= id_invoice %>">
        <input type="hidden" name="id_invoice_line" value="<%= id_invoice_line %>">
        <input type="hidden" name="content_page" value="1">
        <input type="hidden" name="LUD_detail" value="<%= line.getValue("LUD") %>">
	<table <%=Bean.getTableBottomFilter() %>>
		<tr>
			<td colspan="2"><b><center><%=Bean.clearingXML.getfieldTransl("h_edit_invoice_line", false) %></center></b></td>
		</tr>
		<tr>
			<td><%= Bean.clearingXML.getfieldTransl("name_nomenkl", true) %> </td><td><input type="text" name="name_nomenkl" size="40" value="<%=line.getValue("NAME_NOMENKL") %>" class="inputfield"></td>
		</tr>
		<tr>
			<td><%= Bean.clearingXML.getfieldTransl("count_nomenkl", true) %> </td><td><input type="text" name="count_nomenkl" id="count_nomenkl" size="20" value="<%=line.getValue("COUNT_NOMENKL") %>" class="inputfield" onchange="calcInvoiceLineTotalSum();"></td>
		</tr>
 		<tr>
			<td><%= Bean.clearingXML.getfieldTransl("price_nomenkl", true) %>, <%=invoice.getValue("SNAME_CURRENCY") %></td><td><input type="text" name="price_nomenkl" id="price_nomenkl" size="20" value="<%=line.getValue("PRICE_NOMENKL_FRMT") %>" class="inputfield" onchange="calcInvoiceLineTotalSum();"></td>
		</tr>
		<tr>
			<td><b><%= Bean.clearingXML.getfieldTransl("total_sum", false) %>, <%=invoice.getValue("SNAME_CURRENCY") %></b></td><td><input type="text" name="total_amount" id="total_amount" size="20" value="<%=line.getValue("TOTAL_AMOUNT_FRMT") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
 		<tr>
			<td><%= Bean.clearingXML.getfieldTransl("tax_percent", true) %> </td><td><input type="text" name="tax_percent" id="tax_percent" size="20" value="<%=line.getValue("TAX_PERCENT") %>" class="inputfield" onchange="calcInvoiceLineTotalSum();"></td>
		</tr>
		<tr>
			<td><%= Bean.clearingXML.getfieldTransl("tax_sum", false) %>, <%=invoice.getValue("SNAME_CURRENCY") %> </td><td><input type="text" name="tax_amount" id="tax_amount" size="20" value="<%=line.getValue("TAX_AMOUNT_FRMT") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
			<% if (!Bean.isEmpty(resultMessage)) { %>
				<% if (!(Bean.C_SUCCESS_RESULT.equalsIgnoreCase(resultInt))) { %>
				<tr><td colspan="2">&nbsp;</td></tr>
				<tr><td colspan="2"><span id="error_title"><%=Bean.webposXML.getfieldTransl("operation_error", false) %></span></td></tr>
				<tr><td colspan="2"><span id="error_description"><%=resultMessage %></span></td></tr>
				<% } else { %>
				<tr><td colspan="2">&nbsp;</td></tr>
				<tr><td colspan="2"><span id="confirm_description"><%=resultMessage %></span></td></tr>
				<% } %>
			<% } %>
  		<tr>
			<td colspan="6" align="center">
				<%=Bean.getSubmitButtonAjax("report/operationupdate.jsp", "button_save", "updateForm1", "div_invoice_line") %>
				<%=Bean.getResetButton() %>
				<%=Bean.getGoBackButton("report/operation.jsp?tab=3&action=invdetail&id_invoice=" + id_invoice, "button_back", "div_main") %>
			</td>
		</tr>

	</table>

	</form>

    <%
		} else {
		    %> <%= Bean.getUnknownActionText(action) %><%
		}
	} else if (process.equalsIgnoreCase("exit")) {
	} else {
 	   %> <%= Bean.getUnknownProcessText(process) %> <%
	}

} else {
    %> <%= Bean.getUnknownTypeText(type) %> <%
}

%>


</body>
</html>
