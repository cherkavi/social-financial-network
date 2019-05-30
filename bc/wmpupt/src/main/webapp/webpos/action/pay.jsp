<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcWebPosBean"  />

<%@page import="java.util.HashMap"%>
<%@page import="java.text.DecimalFormat" %>

<%@page import="java.util.ArrayList"%><html>
<%
Bean.setSessionParam(request);
request.setCharacterEncoding("UTF-8");

String pageFormName = "WEBPOS_SERVICE_PAY";

String tagTermCurrency = "_TERM_CURRENCY";

Bean.setJspPageForTabName(pageFormName);

HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String tab = Bean.getDecodeParam(parameters.get("tab"));
if (tab == null || "".equalsIgnoreCase(tab)) {
	tab = Bean.tabsHmGetValue(pageFormName);
}
Bean.tabsHmSetValue(pageFormName, tab);

String id_term = Bean.getCurrentTerm();
Bean.loginTerm.getFeature();
String termCurrency = Bean.loginTerm.getValue("SNAME_TERM_CURRENCY");

String idClub = Bean.loginTerm.getValue("ID_CLUB");
String idDealer = Bean.loginTerm.getValue("ID_DEALER");


String cd_card1			= Bean.getDecodeParam(parameters.get("cd_card1"));
cd_card1				= !Bean.isEmpty(cd_card1)?cd_card1.replace(" ", ""):"";

String pay_currency 	= Bean.getDecodeParam(parameters.get("pay_currency"));
pay_currency 			= Bean.checkFindString(pageFormName + tagTermCurrency, pay_currency, "");
String currencyName     = Bean.getCurrencyShortNameById(pay_currency);

String action		 	= Bean.getDecodeParam(parameters.get("action"));
action 	= Bean.isEmpty(action)?"show":action;

String
	pay_value				= Bean.getDecodeAmountParam(parameters.get("pay_value")),
	pay_for_goods			= Bean.getDecodeParamPrepare(parameters.get("pay_for_goods")),
	pay_type				= Bean.getDecodeParamPrepare(parameters.get("pay_type")),
	entered_sum				= Bean.getDecodeParamPrepare(parameters.get("entered_sum")),
	sum_change				= Bean.getDecodeParamPrepare(parameters.get("sum_change")),
	change_to_share_account	= Bean.getDecodeParamPrepare(parameters.get("change_to_share_account")),
	bank_trn				= Bean.getDecodeParamPrepare(parameters.get("bank_trn")),
	sum_point				= Bean.getDecodeParamPrepare(parameters.get("sum_point")),
	percent_point			= Bean.getDecodeParamPrepare(parameters.get("percent_point")),
	calc_point				= Bean.getDecodeParamPrepare(parameters.get("calc_point")),
	membership_fee			= Bean.getDecodeParamPrepare(parameters.get("membership_fee")),
	membership_fee_margin	= Bean.getDecodeParamPrepare(parameters.get("membership_fee_margin")),
	share_fee_margin		= Bean.getDecodeParamPrepare(parameters.get("share_fee_margin")),
	promo_code				= Bean.getDecodeParamPrepare(parameters.get("promo_code"));

percent_point 	= Bean.isEmpty(percent_point)?"":percent_point.replace(",",".");
sum_point 		= Bean.isEmpty(sum_point)?"":sum_point.replace(",",".");

String lastPayType = "";
String lastPayTypeName = "";
int enableCount = 0;
if (Bean.hasExecuteMenuPermission("WEBPOS_SERVICE_PAY_CASH")) {
	if (Bean.isMenuElementEnable("WEBPOS_SERVICE_PAY_CASH")) {
		lastPayType = "CASH";
		lastPayTypeName = Bean.webposXML.getfieldTransl("goods_pay_cash", false);
		enableCount ++;
	}
}
if (Bean.hasExecuteMenuPermission("WEBPOS_SERVICE_PAY_BANK_CARD")) { 
	if (Bean.isMenuElementEnable("WEBPOS_SERVICE_PAY_BANK_CARD")) {
		lastPayType = "BANK_CARD";
		lastPayTypeName = Bean.webposXML.getfieldTransl("goods_pay_card", false);
		enableCount ++;
	}
}
if (Bean.hasExecuteMenuPermission("WEBPOS_SERVICE_PAY_POINTS")) {
	if (Bean.isMenuElementEnable("WEBPOS_SERVICE_PAY_POINTS")) {
		lastPayType = "SMPU_CARD";
		lastPayTypeName = Bean.webposXML.getfieldTransl("goods_pay_points", false);
		enableCount ++;
	}
}
if (Bean.hasExecuteMenuPermission("WEBPOS_SERVICE_MAKE_INVOICE")) {
	if (Bean.isMenuElementEnable("WEBPOS_SERVICE_MAKE_INVOICE")) {
		lastPayType = "INVOICE";
		lastPayTypeName = Bean.webposXML.getfieldTransl("goods_pay_invoices", false);
		enableCount ++;
	}
}

boolean extLoyality = false;
if ("1".equalsIgnoreCase(Bean.loginTerm.getValue("EXT_LOYL","LOYALITY"))) { 
	extLoyality = true;
}

String 
	id_card_status					= "",
	membership_last_date			= "",
	replenish_month_value			= "",
	membership_nopay_month   		= "", 
	//membership_need_pay_sum  		= "",
	membership_max_pay_month 		= "",
	resultInt						= Bean.C_SUCCESS_RESULT,
	resultMessage            		= "";

Boolean checkCardError          	= false;


%>
<body>
	<%=Bean.getWebPosMenuHTML(pageFormName) %>
<% if (!Bean.hasMenuPermission(pageFormName, Bean.C_WRITE_MENU_PERMISSION, Bean.loginTerm)) { %>
	<%=Bean.getErrorPermissionMessage(pageFormName, Bean.loginTerm) %>
<% } else { 

	boolean needMembershipFee 		= false;
	String marginMembershipFeeText 	= "";
	
	String replenish_kind 			= Bean.getDecodeParam(parameters.get("replenish_kind"));
	String replenish_pay_type 		= Bean.getDecodeParam(parameters.get("replenish_pay_type"));
	//String share_fee_margin         = "";
	
	if ("check".equalsIgnoreCase(action)) { 
		ArrayList<String> pParam = new ArrayList<String>();
		
		String membership_cd_currency = "";	
		
		pParam.add(id_term);
		pParam.add(cd_card1);
		pParam.add("PAYMENT");
		pParam.add(Bean.getDateFormat());
		
		String[] results = new String[10];
		
		results 						= Bean.executeFunction("PACK$WEBPOS_UI.oper_check_card", pParam, results.length);
		resultInt 						= results[0];
		id_card_status					= results[1];
		replenish_month_value  	  		= results[2];
		membership_last_date			= results[3];
		membership_nopay_month          = results[4];
		membership_fee					= results[5];
		membership_max_pay_month		= results[6];
		membership_cd_currency          = results[7];
		membership_fee_margin			= results[8];
		resultMessage 					= results[9];
		
		if (!(Bean.C_SUCCESS_RESULT.equalsIgnoreCase(resultInt) ||
				Bean.C_MEMBERSHIP_FEE_ERROR.equalsIgnoreCase(resultInt))) {
			checkCardError = true;
		}
	}

	if (!Bean.isEmpty(membership_fee)) {
		needMembershipFee = true;
		marginMembershipFeeText = Bean.getMarginDescription(idClub, idDealer, "REC_MEMBERSHIP_FEE");
	}

	if ("show".equalsIgnoreCase(action) ||
			("check".equalsIgnoreCase(action) && checkCardError)) { %>
	<script>

	function validatePayParam(){
		var formParam = new Array (
			new Array ('cd_card1', 'card', 1)
		);
		return validateFormForID(formParam, 'updateForm1');
	}
	card_mask2("cd_card1");

	</script>
	<table <%=Bean.getTableDetail2Param() %>>
		<tr>
			<td><div id="div_action_big">
				<% if ("check".equalsIgnoreCase(action) && checkCardError) { %>
				<h1 class="error"><%=Bean.currentMenu.getNameMenuElement() %>: <%=Bean.webposXML.getfieldTransl("operation_error", false) %><%=Bean.getHelpButton("pay", "div_action_big") %></h1>
				<% } else { %>
				<h1><%=Bean.currentMenu.getNameMenuElement() %><%=Bean.getHelpButton("pay", "div_action_big") %></h1>
				<% } %>
				<form name="updateForm1" id="updateForm1" accept-charset="UTF-8" method="POST">
			        <input type="hidden" name="type" value="term">
			        <input type="hidden" name="action" value="check">
			        <input type="hidden" name="process" value="no">
			        <input type="hidden" name="id_term" value="<%=id_term %>">
					<table class="action_table">
			  			<tr><td <%=Bean.getDetailTableFirstColumnWidth() %>><%=Bean.webposXML.getfieldTransl("card", true) %></td><td><input type="text" name="cd_card1" id="cd_card1" size="30" maxlength="20" value="<%=cd_card1 %>" class="inputfield" onkeyup="showCheckCardButton(this)">&nbsp;<%=Bean.getWEBPosCheckCardButton("check_card", "pay", "updateForm1", "div_action_big") %><br><span style="font-size: 10px;"><%=Bean.webposXML.getfieldTransl("cd_card1_hint", false) %></span></td></tr>
						<tr><td colspan="2">&nbsp;</td></tr>	
						<tr><td colspan=2  class="center">
							<%=Bean.getSubmitButtonAjax("action/pay.jsp", "button_further", "updateForm1", "div_main", "validatePayParam") %>
						</td></tr>
						
						<% if (checkCardError) { %>
							<tr><td colspan="2" class="left">&nbsp;</td></tr>
							<tr><td colspan="2"><span id="error_title"><%=Bean.webposXML.getfieldTransl("operation_error", false) %></span></td></tr>
							<tr><td colspan="2"><span id="error_description"><%=resultMessage %></span></td></tr>
						<% } %>
						<tr><td colspan="2" class="left">
						<div id=div_hints>
							<i><%=Bean.getWEBPosOnlyTestCards() %></i>
						</div>
						</td></tr>
					</table>
				</form>
				</div>
			</td>
		</tr>
	</table>
	<% } 
	
	if (("check".equalsIgnoreCase(action) && !checkCardError)) { %>
	<script>

	var isCalc = false;
	
	function validatePayParam(){
		var formParam = new Array (
			new Array ('cd_card1', 'card', 1),
			new Array ('pay_for_goods', 'oper_sum', 1),
			<% if (extLoyality) {%>
			new Array ('sum_point', 'oper_sum_zero', 1),
			<% } %>
			new Array ('pay_currency', 'number', 1)
		);
		var formPointPercent = new Array (
			new Array ('percent_point', 'oper_sum_zero', 1)
		);
		
		if (isCalc) {
			formParam = formParam.concat(formPointPercent);
		}
		return validateFormForID(formParam, 'updateForm1');
	}

	<% if (extLoyality) { %>
		function showPoints(){
			isCalc = true;
			var span_sum_point = document.getElementById('span_sum_point');
			span_sum_point.innerHTML = '<input type="hidden" id="calc_point" name="calc_point" value="Y">'+
			   					   '<input type="text" name="percent_point" id="percent_point" size="15" maxlength="15" value="<%=percent_point%>" onchange="calcPoints()" class="inputfield"><br>'+
								   '<input type="text" name="sum_point" id="sum_point" size="15" maxlength="15" value="<%=sum_point%>" readonly class="inputfield-ro"><input type="text" name="sname_point_currency" size="5" value="<%=Bean.webposXML.getfieldTransl("point_currency_name", false) %>" readonly class="inputfield-ro">' +
								   '&nbsp;<span class="img_calc_points_disable" id="img_calc_points_disable" name="img_calc_points_disable" title="Скрыть расчет суммы баллов" onclick="hidePoints();">&nbsp;</span>';
			span_title_point = document.getElementById('span_title_point');
			span_title_point.innerHTML = '<b><font color="red"><%=Bean.webposXML.getfieldTransl("percent_point", false) %></font></b><br>'+
								   '<font color="blue"><%=Bean.webposXML.getfieldTransl("sum_point", false) %></font>';
		}
		
		function hidePoints(){
			isCalc = false;
			var span_sum_point = document.getElementById('span_sum_point');
			span_sum_point.innerHTML = '<input type="hidden" id="calc_point" name="calc_point" value="N">'+
			   					   '<input type="text" name="sum_point" id="sum_point" size="15" maxlength="15" value="<%=sum_point%>" class="inputfield"><input type="text" name="sname_point_currency" size="5" value="<%=Bean.webposXML.getfieldTransl("point_currency_name", false) %>" readonly class="inputfield-ro">&nbsp;' +
								   '&nbsp;<span class="img_calc_points" id="img_calc_points" name="img_calc_points" title="Рассчитать сумму баллов" onclick="showPoints();">&nbsp;</span>';
			span_title_point = document.getElementById('span_title_point');
			span_title_point.innerHTML = '<b><font color="red"><%=Bean.webposXML.getfieldTransl("cheque_add_point_all", false) %></font></b>';
		}

		function getStandardPointPercent() {
			document.getElementById('percent_point').value='11';
			calcPoints();
		}

		function calcPoints(){
			pay_for_goods = document.getElementById('pay_for_goods');
			percent_point = document.getElementById('percent_point');
			sum_point = document.getElementById('sum_point');
			try {
				if (isCalc) {
					sum_point.value = (pay_for_goods.value.replace(",",".") * percent_point.value.replace(",",".") /100).toFixed(2);
				}
			} catch(err) {alert(err);}
				
		}
		<% if ("Y".equalsIgnoreCase(calc_point)) {%>
			showPoints();
		<% } %>
	<% } %>

	<% if (needMembershipFee) { %>
		calcPaymentTotalSum();
	<% } %>
	</script>
	
	<% 	//int checkBoxCount = 0; 
		
	%>
	<table <%=Bean.getTableDetail2Param() %>>
		<tr>
			<td><div id="div_action_big">
				<h1><%=Bean.currentMenu.getNameMenuElement() %><%=Bean.getHelpButton("pay", "div_action_big") %></h1>
				<form name="updateForm1" id="updateForm1" accept-charset="UTF-8" method="POST">
			        <input type="hidden" name="type" value="term">
			        <input type="hidden" name="action" value="calc">
					<% if (extLoyality) { %>
			        <input type="hidden" name="ext_loyl" value="Y">
					<% } else { %>
			        <input type="hidden" name="ext_loyl" value="N">
					<% } %>
			        <input type="hidden" name="id_term" value="<%=id_term %>">
					<input type="hidden" name="cd_card1" id="cd_card1" value="<%=cd_card1 %>">
					<table class="action_table">
			  			<tr><td <%=Bean.getDetailTableFirstColumnWidth() %>><%=Bean.webposXML.getfieldTransl("card", false) %></td><td colspan="2"><input type="text" name="cd_card1_hide" id="cd_card1_hide" size="20" value="<%=Bean.hideCdCard1(cd_card1) %>" readonly="readonly" class="inputfield_finish_blue"></td></tr>
						<tr>
							<td><%=Bean.webposXML.getfieldTransl("goods_pay_sum", true) %></td>
							<td>
								<% if (!Bean.C_TERM_CAN_OPER_DIFFERENT_CURRENCY.equalsIgnoreCase(Bean.loginTerm.getValue("CAN_OPER_DIFFERENT_CURRENCY"))) { %>
								<input type="text" id="pay_for_goods" name="pay_for_goods" size="15" maxlength="15" value="<%=pay_for_goods %>" class="inputfield" onchange="<%=(needMembershipFee)?"calcPaymentTotalSum();":"" %><%=(extLoyality)?"calcPoints();":"" %>"><input type="hidden" name="pay_currency" value="<%=Bean.loginTerm.getValue("CD_TERM_CURRENCY") %>"><input type="text" name="sname_term_currency" size="5" value="<%=termCurrency %>" readonly class="inputfield-ro">
								<% } else { %>
								<input type="text" id="pay_for_goods" name="pay_for_goods" size="15" maxlength="15" value="<%=pay_for_goods %>" class="inputfield" onchange="<%=(needMembershipFee)?"calcPaymentTotalSum();":"" %><%=(extLoyality)?"calcPoints();":"" %>">
								<%=Bean.getSelectBeginHTML("pay_currency", Bean.webposXML.getfieldTransl("title_term_currency", false)) %>
							 		<%= Bean.getWPCurrencyShortNameOption(pay_currency, true) %>
								<%=Bean.getSelectOnChangeEndHTML() %>
								<% } %>
							</td>
						</tr>
						<% if (extLoyality) { %>
							<tr><td>
									<span id="span_title_point">
									<%=Bean.webposXML.getfieldTransl("cheque_add_point_all", true) %>
									</span>
								</td>
								<td>
									<span id="span_sum_point">
									<input type="text" name="sum_point" id="sum_point" size="15" maxlength="15" value="<%=sum_point %>" class="inputfield"><input type="text" name="sname_point_currency" size="5" value="<%=Bean.webposXML.getfieldTransl("point_currency_name", false) %>" readonly class="inputfield-ro">&nbsp;<%=Bean.getWEBPosCalcPointsButton("calc_point", "updateForm1", "div_action_big") %>
									</span>
								</td>
							</tr>
						<% } %>
						<% if (needMembershipFee) { %>
							<% if ("check".equalsIgnoreCase(action) && !checkCardError) { %>
							<tr><td colspan="2" class="another_fee top_line_gray line_dashed"><span id="error_description"><%=resultMessage %></span></td></tr>
							<% } %>
							<tr>
								<td class="another_fee"><%=Bean.webposXML.getfieldTransl("membership_fee_sum", false) %></td>
								<td colspan="2"  class="another_fee">
									<input type="text" name="membership_fee" id="membership_fee" size="15" value="<%=Bean.formatAmount(membership_fee) %>" readonly class="inputfield-ro"><input type="text" name="sname_mf_currency" size="5" value="<%=termCurrency %>" readonly="readonly" class="inputfield-ro">
								</td>
							</tr>
							<% if (!Bean.isEmpty(membership_fee_margin)) { %>
							<tr>
								<td class="another_fee"><%=Bean.webposXML.getfieldTransl("membership_fee_dealer_margin", false) %></td>
								<td colspan="2" class="another_fee">
									<input type="text" name="membership_fee_margin" id="membership_fee_margin" size="15" value="<%=Bean.formatAmount(membership_fee_margin) %>" readonly class="inputfield-ro"><input type="text" name="sname_mf_margin_currency" size="5" value="<%=termCurrency %>" readonly="readonly" class="inputfield-ro">
								</td>
							</tr>
							<% } %>
							<tr>
								<td class="top_line_gray line_dashed"><b><%=Bean.webposXML.getfieldTransl("pay_total", false) %></b></td>
								<td colspan="2" class="top_line_gray line_dashed">
									<input type="text" name="pay_value" id="pay_value" size="15" value="" readonly class="inputfield-ro"><input type="text" name="sname_total_currency" size="5" value="<%=termCurrency %>" readonly="readonly" class="inputfield-ro">
								</td>
							</tr>
						<% } %>
						<tr><td><%=Bean.webposXML.getfieldTransl("promo_code", false) %></td><td><input type="text" name="promo_code" id="promo_code" size="24" maxlength="20" value="<%=promo_code %>" class="inputfield"></td></tr>
						<% if (enableCount==1) { %>
						<tr>
							<td><%=Bean.webposXML.getfieldTransl("goods_pay_way", false) %></td>
							<td>
								<input type="hidden" name="pay_type" id="pay_type" value="<%=lastPayType %>">
								<% if ("CASH".equalsIgnoreCase(lastPayType) || "BANK_CARD".equalsIgnoreCase(lastPayType)) { %>
								<input type="hidden" name="process" id="process" value="no">
								<% } else { %>
								<input type="hidden" name="process" id="process" value="yes">
								<% } %>
								<input type="text" name="sum_point_txt" id="sum_point_txt" size="20" value="<%=lastPayTypeName %>" readonly class="inputfield_finish_green">
							</td>
						</tr>
						<tr><td colspan="2">&nbsp;</td></tr>
						<tr><td colspan="2"  class="center">
							<% if ("CASH".equalsIgnoreCase(lastPayType) || "BANK_CARD".equalsIgnoreCase(lastPayType)) { %>
								<%=Bean.getSubmitButtonAjax("action/pay.jsp", "button_further", "updateForm1", "div_main", "validatePayParam") %>
							<% } else { %>
								<%=Bean.getSubmitButtonAjax("action/payupdate.jsp", "button_further", "updateForm1", "div_action_big", "validatePayParam") %>
							<% } %>
							<%=Bean.getSubmitButtonAjax("action/pay.jsp", "button_back", "formBack", "div_main") %>
						</td></tr>
						<% } else if (enableCount>0) { %>
						<tr>
							<td colspan="4"><br><%=Bean.webposXML.getfieldTransl("goods_pay_way", true) %></td>
						</tr>
						<tr>
							<td colspan="4">
								<table border="0">
									<tr>
										<%=Bean.getPayTypeImage("CASH", "WEBPOS_SERVICE_PAY_CASH", "action/pay.jsp?pay_type=CASH&process=no", "div_main", "updateForm1", "validatePayParam") %>
										<%=Bean.getPayTypeImage("SMPU_CARD", "WEBPOS_SERVICE_PAY_POINTS", "action/payupdate.jsp?pay_type=SMPU_CARD&process=yes", "div_action_big", "updateForm1", "validatePayParam") %>
										<%=Bean.getPayTypeImage("BANK_CARD", "WEBPOS_SERVICE_PAY_BANK_CARD", "action/pay.jsp?pay_type=BANK_CARD&process=no", "div_main", "updateForm1", "validatePayParam") %>
										<%=Bean.getPayTypeImage("INVOICE", "WEBPOS_SERVICE_MAKE_INVOICE", "action/payupdate.jsp?pay_type=INVOICE&process=yes", "div_action_big", "updateForm1", "validatePayParam") %>
									</tr>
								</table>
							</td>
						</tr>
						<tr><td colspan="2"  class="center">
							<%=Bean.getSubmitButtonAjax("action/pay.jsp", "button_back", "formBack", "div_main") %>
						</td></tr>
						<% } else if (enableCount==0) { %>
						<tr><td colspan="2">&nbsp;</td></tr>
						<tr><td colspan="2"><span id="error_title"><%=Bean.webposXML.getfieldTransl("operation_error", false) %></span></td></tr>
						<tr><td colspan="2"><span id="error_description"><%=Bean.webposXML.getfieldTransl("operation_permission_denied", false) %></span></b></td></tr>
						<tr><td colspan="2" class="center">
							<%=Bean.getSubmitButtonAjax("action/pay.jsp", "button_back", "formBack", "div_main") %>
						</td></tr>
						<% } %>
				<% if (needMembershipFee || "SMPU_CARD".equalsIgnoreCase(lastPayType)) { %>
				<tr><td colspan="2" class="left">
				<div id=div_hints>
					<i><b><%=Bean.webposXML.getfieldTransl("title_note", false) %></b>
					<% if ("SMPU_CARD".equalsIgnoreCase(lastPayType)) { %>
						<br><%=Bean.webposXML.getfieldTransl("title_note_pay", false) %>
					<% } %>
					<% if (needMembershipFee) { %>
						<br><%=marginMembershipFeeText %>
					<% } %>
					</i>
				</div>
				</td></tr>
				<% } %>
					</table>
				</form>

				<form name="formBack" id="formBack" accept-charset="UTF-8" method="POST">
					<input type="hidden" name="action" value="show">
					<input type="hidden" name="id_term" value="<%=id_term %>">
					<input type="hidden" name="cd_card1" id="cd_card1" value="<%=cd_card1 %>">
				</form>
				</div>
			</td>
		</tr>
	</table>
	<% }
	
	if ("calc".equalsIgnoreCase(action)) { 
	
		String marginText = "";
		if ("CASH".equalsIgnoreCase(pay_type)) {
			marginText = marginText + "<br>" + Bean.getMarginChangeToShareAccountDescription(idClub, idDealer);
		}
		//if ("SHARE_FEE".equalsIgnoreCase(replenish_kind)) {
		//	marginText = marginText + "<br>" + Bean.getMarginDescription(idClub, idDealer, "REC_SHARE_FEE");
		//}
		if ("CASH".equalsIgnoreCase(replenish_pay_type)) {
			marginText = marginText + "<br>" + Bean.getMarginChangeToShareAccountDescription(idClub, idDealer);
		}
		//System.out.println("marginText="+marginText);
	%>
		
<script>
	function validateBonTerm(){
		var returnValue = null;
		try {
		var formAll = new Array (
		);
		var formParam = new Array (
			new Array ('cd_card1', 'card', 1)
		);
		var formPayBankCard = new Array (
			new Array ('bank_trn', 'varchar2', 1)
		);

		formAll = formParam;
		<% if ("BANK_CARD".equalsIgnoreCase(pay_type)) { %>
		formAll = formAll.concat(formPayBankCard);
		<% } %>
		returnValue = validateFormForID(formAll, 'updateForm1');
		<% if ("CASH".equalsIgnoreCase(pay_type) || "CASH".equalsIgnoreCase(replenish_pay_type)) { %>
		if (returnValue) {
			returnValue = validateChange();
		}
		<% } %>
		} catch(err2) {alert(err2);}
		return returnValue;
	}
	</script>
	<table <%=Bean.getTableDetail2Param() %>>
		<tr>
			<td><div id="div_action_big">
				<h1 class="confirm"><%=Bean.webposXML.getfieldTransl("payment_for_goods", false) %>: <%=Bean.webposXML.getfieldTransl("oper_confirmation", false) %><%=Bean.getHelpButton("pay", "div_action_big") %></h1>
				<form name="updateForm1" id="updateForm1" accept-charset="UTF-8" method="POST">
			        <input type="hidden" name="type" value="term">
			        <input type="hidden" name="action" value="calc">
			        <input type="hidden" name="process" value="yes">
			        <input type="hidden" name="cd_card1" value="<%=cd_card1 %>">
			        <input type="hidden" name="pay_currency" value="<%=pay_currency %>">
			        <input type="hidden" name="id_term" value="<%=id_term %>">
			        <input type="hidden" name="pay_for_goods" id="pay_for_goods" value="<%=pay_for_goods %>">
					<% pay_value = Bean.isEmpty(pay_value)?pay_for_goods:pay_value; %>
			        <input type="hidden" name="pay_value" id="pay_value" value="<%=pay_value %>">
					<% if (!Bean.isEmpty(membership_fee)) { %>
			        <input type="hidden" name="membership_fee" id="membership_fee" value="<%=membership_fee %>">
			        <input type="hidden" name="membership_fee_margin" id="membership_fee_margin" value="<%=membership_fee_margin %>">
					<% } %>
					<% if ("SHARE_FEE".equalsIgnoreCase(replenish_kind)) { %>
			        <input type="hidden" name="replenish_kind" value="<%=replenish_kind %>">
			        <input type="hidden" name="share_fee" value="<%=pay_value %>">
			        <input type="hidden" name="pay_type" value="<%=replenish_pay_type %>">
					<% } else { %>
			        <input type="hidden" name="pay_type" value="<%=pay_type %>">
					<% } %>
			        <input type="hidden" name="share_fee_margin" id="share_fee_margin" value="<%=share_fee_margin %>">
			        <input type="hidden" name="calc_point" id="calc_point" value="<%=calc_point %>">
			        <input type="hidden" name="sum_point" id="sum_point" value="<%=sum_point %>">
			        <input type="hidden" name="percent_point" id="percent_point" value="<%=percent_point %>">
					<table class="action_table">
			  			<tr><td <%=Bean.getDetailTableFirstColumnWidth() %>><%=Bean.webposXML.getfieldTransl("card", false) %></td><td><input type="text" name="cd_card1_hide" id="cd_card1_hide" size="20" value="<%=Bean.hideCdCard1(cd_card1) %>" readonly class="inputfield_finish_blue"></td></tr>
			  			<tr><td><%=Bean.webposXML.getfieldTransl("goods_pay_sum", false) %></td><td><input type="text" name="pay_for_goods_txt" id="pay_for_goods_txt" size="20" value="<%=Bean.formatAmount(pay_for_goods) %> <%=currencyName %>" readonly class="inputfield_finish_red"></td></tr>
						<% if (extLoyality) { %>
							<tr><td><%=Bean.webposXML.getfieldTransl("point_calc_way", false) %></td><td><input type="text" name="sum_point_txt" id="sum_point_txt" size="20" value="<%=Bean.webposXML.getfieldTransl("point_calc_way_term", false) %>" readonly class="inputfield_finish_blue"></td></tr>
							<% if ("Y".equalsIgnoreCase(calc_point)) { %>
				  			<tr><td><%=Bean.webposXML.getfieldTransl("percent_point", false) %></td><td><input type="text" name="percent_point_txt" id="percent_point_txt" size="20" value="<%=Bean.formatAmount(percent_point) %> %" readonly class="inputfield_finish"></td></tr>
							<% } %>
							<tr><td><%=Bean.webposXML.getfieldTransl("cheque_add_point_all", false) %></td><td><input type="text" name="sum_point_txt" id="sum_point_txt" size="20" value="<%=Bean.formatAmount(sum_point) %> <%=Bean.getWebPOSPointCurrencyName(sum_point) %>" readonly class="inputfield_finish_blue"></td></tr>
						<% } %>
						<% if (!Bean.isEmpty(membership_fee)) { %>
				  			<tr><td class="another_fee top_line_gray line_dashed"><%=Bean.webposXML.getfieldTransl("membership_fee_sum", false) %></td><td class="another_fee top_line_gray line_dashed"><input type="text" name="membership_fee_txt" id="membership_fee_txt" size="20" value="<%=Bean.formatAmount(membership_fee) %> <%=currencyName %>" readonly class="inputfield_finish_green"></td></tr>
							<% if (!Bean.isEmpty(membership_fee_margin)) { %>
				  			<tr><td class="another_fee"><%=Bean.webposXML.getfieldTransl("membership_fee_dealer_margin", false) %></td><td class="another_fee"><input type="text" name="membership_fee_margin_txt" id="membership_fee_margin_txt" size="20" value="<%=Bean.formatAmount(membership_fee_margin) %> <%=currencyName %>" readonly class="inputfield_finish_gray"></td></tr>
							<% } %>
				  			<tr><td class="top_line_gray line_dashed"><b><%=Bean.webposXML.getfieldTransl("pay_total", false) %></b></td><td class="top_line_gray line_dashed"><input type="text" name="pay_value_txt" id="pay_value_txt" size="20" value="<%=Bean.formatAmount(pay_value) %> <%=currencyName %>" readonly class="inputfield_finish_red"></td></tr>
						<% } else { %>
				  			<tr><td class="top_line_gray line_dashed"><b><%=Bean.webposXML.getfieldTransl("pay_total", false) %></b></td><td class="top_line_gray line_dashed"><input type="text" name="pay_value_txt" id="pay_value_txt" size="20" value="<%=Bean.formatAmount(pay_for_goods) %> <%=currencyName %>" readonly class="inputfield_finish_red"></td></tr>
						<% } %>
						<tr>
							<td><%=Bean.webposXML.getfieldTransl("goods_pay_way", false) %></td>
							<td>
								<% if ("CASH".equalsIgnoreCase(pay_type)) { %>
								<input type="text" name="pay_type_txt" id="pay_type_txt" size="20" value="<%=Bean.webposXML.getfieldTransl("goods_pay_cash", false) %>" readonly class="inputfield_finish_green">
							</td>
						</tr>
			  			<tr>
							<td><%=Bean.webposXML.getfieldTransl("entered_sum", false) %></td>
							<td>
								<input type="text" name="entered_sum" id="entered_sum" size="15" maxlength="15" value="<%=entered_sum %>" class="inputfield" onchange="calcChange();" title="<%=Bean.webposXML.getfieldTransl("entered_sum_title", false) %>"><input type="text" name="sname_term_currency" size="5" value="<%=termCurrency %>" readonly class="inputfield-ro">
							</td>
						</tr>
			  			<tr>
							<td><%=Bean.webposXML.getfieldTransl("sum_change", false) %></td>
							<td><input type="text" name="sum_change" id="sum_change" size="15" maxlength="15" value="<%=sum_change %>" class="inputfield-ro" readonly title="<%=Bean.webposXML.getfieldTransl("sum_change_title", false) %>"><input type="text" name="sname_term_currency" size="5" value="<%=termCurrency %>" readonly class="inputfield-ro">
								<input type="hidden" name="change_calc_error" id="change_calc_error" value="N">
								<br>
								<input type="checkbox" name="change_to_share_account" id="change_to_share_account" <% if ("Y".equalsIgnoreCase(change_to_share_account)) { %> CHECKED <% } %> disabled="disabled" class="inputfield-ro" title="<%=Bean.webposXML.getfieldTransl("change_to_share_account", false) %>">
								<label class="checbox_label" for="change_to_share_account"><%=Bean.webposXML.getfieldTransl("change_to_share_account", false) %></label>
								<% } else if ("BANK_CARD".equalsIgnoreCase(pay_type)) {  %>
								<input type="text" name="pay_type_txt" id="pay_type_txt" size="20" value="<%=Bean.webposXML.getfieldTransl("goods_pay_card", false) %>" readonly class="inputfield_finish_green">
							</td>
						</tr>
			  			<tr>
							<td><%=Bean.webposXML.getfieldTransl("bank_trn", true) %></td>
							<td>
								<input type="text" name="bank_trn" id="bank_trn" size="20" value="<%=bank_trn %>" class="inputfield">
								<% } else if ("SMPU_CARD".equalsIgnoreCase(pay_type)) {  %>
								<input type="text" name="pay_type_txt" id="pay_type_txt" size="20" value="<%=Bean.webposXML.getfieldTransl("goods_pay_points", false) %>" readonly class="inputfield_finish_green">
								<% } else if ("INVOICE".equalsIgnoreCase(pay_type)) {  %>
								<input type="text" name="pay_type_txt" id="pay_type_txt" size="20" value="<%=Bean.webposXML.getfieldTransl("goods_pay_invoices", false) %>" readonly class="inputfield_finish_green">
								<% } %>
							</td>
						</tr>
						
						<% if ("SMPU_CARD".equalsIgnoreCase(pay_type)) {  %>
							<% if ("SHARE_FEE".equalsIgnoreCase(replenish_kind)) { %>
								<tr><td colspan="2" class="through_share_fee" style="color:green"><%=Bean.webposXML.getfieldTransl("title_payment_through_share_fee", false) %></td></tr>
								<tr><td class="through_share_fee result_desc"><%=Bean.webposXML.getfieldTransl("cheque_share_fee", false) %></td><td colspan="2" class="through_share_fee"><input type="text" name="opr_sum_txt" id="opr_sum_txt" size="20" value="<%=pay_value %> <%=termCurrency %>" readonly class="inputfield_finish_red"></td></tr>
								<tr>
									<td class="through_share_fee"><%=Bean.webposXML.getfieldTransl("goods_pay_way", false) %></td>
									<td class="through_share_fee">
										<% if ("CASH".equalsIgnoreCase(replenish_pay_type)) { %>
										<input type="text" name="pay_type_txt" id="pay_type_txt" size="20" value="<%=Bean.webposXML.getfieldTransl("goods_pay_cash", false) %>" readonly class="inputfield_finish_green">
									</td>
								</tr>
					  			<tr>
									<td class="through_share_fee"><%=Bean.webposXML.getfieldTransl("entered_sum", false) %></td>
									<td class="through_share_fee">
										<input type="text" name="entered_sum" id="entered_sum" size="15" maxlength="15" value="<%=entered_sum %>" class="inputfield" onchange="calcChange();" title="<%=Bean.webposXML.getfieldTransl("entered_sum_title", false) %>"><input type="text" name="sname_term_currency" size="5" value="<%=termCurrency %>" readonly class="inputfield-ro">
									</td>
								</tr>
					  			<tr>
									<td class="through_share_fee"><%=Bean.webposXML.getfieldTransl("sum_change", false) %></td>
									<td class="through_share_fee">
										<input type="text" name="sum_change" id="sum_change" size="15" value="<%=sum_change %>" class="inputfield-ro" readonly title="<%=Bean.webposXML.getfieldTransl("sum_change_title", false) %>"><input type="text" name="sname_term_currency" size="5" value="<%=termCurrency %>" readonly class="inputfield-ro">
										<input type="hidden" name="change_calc_error" id="change_calc_error" value="N">
										<br>
										<input type="checkbox" name="change_to_share_account" id="change_to_share_account" <% if ("Y".equalsIgnoreCase(change_to_share_account)) { %> CHECKED <% } %> disabled="disabled" class="inputfield-ro" title="<%=Bean.webposXML.getfieldTransl("change_to_share_account_title", false) %>">
										<label class="checbox_label" for="change_to_share_account"><%=Bean.webposXML.getfieldTransl("change_to_share_account", false) %></label>
										<% } else if ("BANK_CARD".equalsIgnoreCase(replenish_pay_type)) {  %>
										<input type="text" name="pay_type_txt" id="pay_type_txt" size="20" value="<%=Bean.webposXML.getfieldTransl("goods_pay_card", false) %>" readonly class="inputfield_finish_green">
									</td>
								</tr>
					  			<tr>
									<td class="through_share_fee"><%=Bean.webposXML.getfieldTransl("bank_trn", true) %></td>
									<td class="through_share_fee">
										<input type="text" name="bank_trn" id="bank_trn" size="20" value="<%=bank_trn %>" class="inputfield">
										<% } else if ("INVOICE".equalsIgnoreCase(replenish_pay_type)) {  %>
										<input type="text" name="pay_type_txt" id="pay_type_txt" size="20" value="<%=Bean.webposXML.getfieldTransl("goods_pay_invoices", false) %>" readonly class="inputfield_finish_green">
										<% } %>
									</td>
								</tr>
							<% } %>
						<% } %>
						<tr><td colspan="2" class="left">&nbsp;</td></tr>
						<tr><td colspan="4" class="center">
						<% if (enableCount > 0) { %>
							<%=Bean.getSubmitButtonAjax("action/payupdate.jsp", "pay", "updateForm1", "div_action_big", "validateBonTerm", "pay_button", true) %> 
						<% } else { %>
				  			<br><span style="font-weight: bold; color: red;"><%=Bean.webposXML.getfieldTransl("operation_permission_denied", false) %></span>
						<% } %>
							<%=Bean.getSubmitButtonAjax("action/pay.jsp", "button_back", "updateFormBack", "div_main") %>
						</td></tr>
						<tr><td colspan="2" class="left">
							<div id=div_hints>
								<% if (!Bean.isEmpty(marginText)) { %>
								<i><b><%=Bean.webposXML.getfieldTransl("title_note", false) %></b><br>
									<%=marginText %><br>
								</i>
								<% } %> 
							</div>
						</td></tr>
					</table>
				</form>
				</div>
			</td>
		</tr>
	</table>
			<% if (Bean.C_NEED_SMS_CONFIRMATION.equalsIgnoreCase(resultInt)) { %>
			<form class="hiddenForm" name="updateFormGetSMS" id="updateFormGetSMS" accept-charset="UTF-8" method="POST">
				<input type="hidden" name="action" value="get_sms_code">
				<input type="hidden" name="id_term" value="<%=id_term %>">
				<input type="hidden" name="back_type" value="pay">
			</form>
			<% } %>
			<% if (Bean.C_NEED_PIN.equalsIgnoreCase(resultInt)) { %>
			<form class="hiddenForm" name="updateForm2" id="updateForm2" accept-charset="UTF-8" method="POST">
    			<input type="hidden" name="type" value="term">
    			<input type="hidden" name="action" value="oper_confirm">
    			<input type="hidden" name="confirm_type" value="REMIND_PIN">
    			<input type="hidden" name="process" value="no">
				<input type="hidden" name="id_term" value="<%=id_term %>">
    			<input type="hidden" name="cd_card1" value="<%=cd_card1 %>">
    			<input type="hidden" name="pay_type" value="<%=pay_type %>">
    			<input type="hidden" name="pay_for_goods" value="<%=pay_for_goods %>">
    			<input type="hidden" name="pay_value" value="<%=pay_value %>">
			</form>
			<% } %>
	<form class="hiddenForm" name="updateFormBack" id="updateFormBack" accept-charset="UTF-8" method="POST">
		<input type="hidden" name="cd_card1" value="<%=cd_card1 %>">
		<input type="hidden" name="pay_for_goods" value="<%=pay_for_goods %>">
		<input type="hidden" name="pay_currency" value="<%=pay_currency %>">
		<input type="hidden" name="action" value="check">
	    <input type="hidden" name="calc_point" id="calc_point" value="<%=calc_point %>">
		<input type="hidden" name="sum_point" id="sum_point" value="<%=sum_point %>">
		<input type="hidden" name="percent_point" id="percent_point" value="<%=percent_point %>">
 	</form>
	<% } %>
<% } %>
	<script type="text/javascript">
		moveFocusAfterEnterButton('pay_for_goods', 'pay_type_CASH');
		moveFocusAfterEnterButton('pay_type_CASH', 'pay_type_BANK_CARD');
		moveFocusAfterEnterButton('pay_type_BANK_CARD', 'pay_type_SMPU_CARD');
		moveFocusAfterEnterButton('pay_type_SMPU_CARD', 'pay_type_INVOICE');
		clickAfterEnterButton('pay_type_SMPU_CARD', 'pay_button');
	</script>

</body>
</html>