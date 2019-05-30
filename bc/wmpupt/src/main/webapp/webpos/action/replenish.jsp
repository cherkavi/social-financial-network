<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcWebPosBean"  />

<%@page import="java.util.HashMap"%>
<%@page import="webpos.wpTargetPrgObject"%>
<%@page import="java.util.ArrayList"%>
<%@page import="webpos.wpClubCardObject"%><html>
<%

request.setCharacterEncoding("UTF-8");

String pageFormName = "WEBPOS_SERVICE_REPLANISH";
String tagAllTPGeneral = "_ALL_TP_GENERAL";
String tagAllTPGeneralFind = "_ALL_TP_FIND_GENERAL";
String tagAllTP = "_ALL_TP";
String tagAllTPFind = "_ALL_TP_FIND";
String tagUserTP = "_USER_TP";
String tagUserTPFind = "_USER_TP_FIND";

Bean.setJspPageForTabName(pageFormName);

HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());


String id_term = Bean.getCurrentTerm();
Bean.loginTerm.getTermFeature();
String termCurrency = Bean.loginTerm.getValue("SNAME_TERM_CURRENCY");

String idClub = Bean.loginTerm.getValue("ID_CLUB");
String idDealer = Bean.loginTerm.getValue("ID_DEALER");

String cd_card1			= Bean.getDecodeParam(parameters.get("cd_card1"));
String replenish_type	= Bean.getDecodeParam(parameters.get("replenish_type"));

String action			= Bean.getDecodeParam(parameters.get("action")); 
action 	= Bean.isEmpty(action)?"show":action;

cd_card1				= !Bean.isEmpty(cd_card1)?cd_card1.replace(" ", ""):"";

String point_fee		= Bean.getDecodeParamPrepare(parameters.get("point_fee"));
String share_fee		= Bean.getDecodeParamPrepare(parameters.get("share_fee"));
String membership_fee	= Bean.getDecodeParamPrepare(parameters.get("membership_fee"));
String tp_fee			= Bean.getDecodeParamPrepare(parameters.get("tp_fee"));
String pay_type			= Bean.getDecodeParamPrepare(parameters.get("pay_type"));
String bank_trn			= Bean.getDecodeParamPrepare(parameters.get("bank_trn"));


String id_card_status			= "",
	   membership_last_date		= "",
       replenish_month_value	= "",
       membership_nopay_month   = "", 
       membership_need_pay_sum  = "",
       membership_max_pay_month = "",
       share_fee_margin         = "",
       membership_fee_margin	= "",
       resultInt				= Bean.C_SUCCESS_RESULT,
       resultMessage            = "";


String id_prg_parent	= Bean.getDecodeParam(parameters.get("id_prg_parent"));

String tab2 = Bean.getDecodeParam(parameters.get("tab2"));



String l_user_tp_page = Bean.getDecodeParam(parameters.get("user_tp_page"));
Bean.pageCheck(pageFormName + tagUserTP, l_user_tp_page);
String l_user_tp_page_beg = Bean.getFirstRowNumber(pageFormName + tagUserTP);
String l_user_tp_page_end = Bean.getLastRowNumber(pageFormName + tagUserTP);

String user_tp_find 	= Bean.getDecodeParam(parameters.get("user_tp_find"));
user_tp_find 	= Bean.checkFindString(pageFormName + tagUserTPFind, user_tp_find, l_user_tp_page);


String l_all_tp_user_page = Bean.getDecodeParam(parameters.get("all_tp_user_page"));
Bean.pageCheck(pageFormName + tagAllTP, l_all_tp_user_page);
String l_all_tp_user_page_beg = Bean.getFirstRowNumber(pageFormName + tagAllTP);
String l_all_tp_user_page_end = Bean.getLastRowNumber(pageFormName + tagAllTP);

String all_tp_user_find 	= Bean.getDecodeParam(parameters.get("all_tp_user_find"));
all_tp_user_find 	= Bean.checkFindString(pageFormName + tagAllTPFind, all_tp_user_find, l_all_tp_user_page);

String l_all_tp_general_page = Bean.getDecodeParam(parameters.get("all_tp_general_page"));
Bean.pageCheck(pageFormName + tagAllTPGeneral, l_all_tp_general_page);
String l_all_tp_general_page_beg = Bean.getFirstRowNumber(pageFormName + tagAllTPGeneral);
String l_all_tp_general_page_end = Bean.getLastRowNumber(pageFormName + tagAllTPGeneral);

String all_tp_general_find 	= Bean.getDecodeParam(parameters.get("all_tp_general_find"));
all_tp_general_find 	= Bean.checkFindString(pageFormName + tagAllTPGeneralFind, all_tp_general_find, l_all_tp_general_page);

%>
<body>
	<%=Bean.getWebPosMenuHTML(pageFormName) %>
<% if (!Bean.hasMenuPermission(pageFormName, Bean.C_READ_MENU_PERMISSION, Bean.loginTerm)) { %>
	<%=Bean.getErrorPermissionMessage(pageFormName, Bean.loginTerm) %>
<% } else { 
	
	String tab = Bean.getDecodeParam(parameters.get("tab"));
	if (tab == null || "".equalsIgnoreCase(tab)) {
		tab = Bean.tabsHmGetValue(pageFormName);
		if (!("1".equalsIgnoreCase(tab) || "2".equalsIgnoreCase(tab) || "3".equalsIgnoreCase(tab) || "4".equalsIgnoreCase(tab) || "5".equalsIgnoreCase(tab))) {
			tab = "1";
		}
	}

	boolean tab1HasPerm = Bean.hasWriteMenuPermission("WEBPOS_SERVICE_REPLANISH_POINT");
	boolean tab2HasPerm = Bean.hasWriteMenuPermission("WEBPOS_SERVICE_REPLANISH_SHARE_FEE");
	boolean tab3HasPerm = Bean.hasWriteMenuPermission("WEBPOS_SERVICE_REPLANISH_MEMBERSHIP_FEE");
	boolean tab4HasPerm = Bean.hasWriteMenuPermission("WEBPOS_SERVICE_REPLANISH_MEMBERSHIP_TARGET_FEE");
	boolean tab5HasPerm = Bean.hasWriteMenuPermission("WEBPOS_SERVICE_REPLANISH_TARGET_PROGRAMS");
	int tabCount = (tab1HasPerm?1:0)+(tab2HasPerm?1:0)+(tab3HasPerm?1:0)+(tab4HasPerm?1:0)+(tab5HasPerm?1:0);
	


	if ("1".equalsIgnoreCase(tab) && !tab1HasPerm) {
		tab = !tab2HasPerm?(!tab3HasPerm?(!tab4HasPerm?(!tab5HasPerm?"6":"5"):"4"):"3"):"2";
	} else if ("2".equalsIgnoreCase(tab) && !tab2HasPerm) {
		tab = !tab1HasPerm?(!tab3HasPerm?(!tab4HasPerm?(!tab5HasPerm?"6":"5"):"4"):"3"):"1";
	} else if ("3".equalsIgnoreCase(tab) && !tab3HasPerm) {
		tab = !tab1HasPerm?(!tab2HasPerm?(!tab4HasPerm?(!tab5HasPerm?"6":"5"):"4"):"2"):"1";
	} else if ("4".equalsIgnoreCase(tab) && !tab4HasPerm) {
		tab = !tab1HasPerm?(!tab2HasPerm?(!tab3HasPerm?(!tab5HasPerm?"6":"5"):"4"):"2"):"1";
	} else if ("5".equalsIgnoreCase(tab) && !tab5HasPerm) {
		tab = !tab1HasPerm?(!tab2HasPerm?(!tab3HasPerm?(!tab4HasPerm?"6":"4"):"3"):"2"):"1";
	}
	
	Bean.tabsHmSetValue(pageFormName, tab);

%>
<% if (tabCount > 0) { %>
	<table <%=Bean.getTableDetail2Param() %>>
		<tr>
			<td><div id="div_action_big">


	<h1>
	<%if ("1".equalsIgnoreCase(tab) && tab1HasPerm) {%>
	<%=Bean.webposXML.getfieldTransl("fee_kind_point", false) %>
	<% } %>
	<%if ("2".equalsIgnoreCase(tab) && tab2HasPerm) {%>
	<%=Bean.webposXML.getfieldTransl("fee_kind_share", false) %>
	<% } %>
	<%if ("3".equalsIgnoreCase(tab) && tab3HasPerm) {%>
	<%=Bean.webposXML.getfieldTransl("fee_kind_membership", false) %>
	<% } %>
	<%if ("4".equalsIgnoreCase(tab) && tab4HasPerm) {%>
	<%=Bean.webposXML.getfieldTransl("fee_kind_admission", false) %>
	<% } %>
	<%if ("5".equalsIgnoreCase(tab) && tab5HasPerm) {%>
	<%=Bean.webposXML.getfieldTransl("title_target_programs", false) %>
	<% } %>
	<%=Bean.getHelpButton("replenish", "div_action_big") %>
	</h1>
	<table <%=Bean.getTableDetail2Param()%>>
		<tr>
			<td>
				<div id="slidetabsmenu">
				<ul>
				<% if (tab1HasPerm) { %>
				<li <%if ("1".equalsIgnoreCase(tab)) {%> class="current"<% } %>><a href="#"><span onclick="ajaxpage('action/replenish.jsp?tab=1&cd_card1='+getCdCard1(), 'div_main')"><%=Bean.webposXML.getfieldTransl("fee_kind_point2", false) %></span></a></li>
				<% } %>
				<% if (tab2HasPerm) { %>
				<li <%if ("2".equalsIgnoreCase(tab)) {%> class="current"<% } %>><a href="#"><span onclick="ajaxpage('action/replenish.jsp?tab=2&cd_card1='+getCdCard1(), 'div_main')"><%=Bean.webposXML.getfieldTransl("fee_kind_share", false) %></span></a></li>
				<% } %>
				<% if (tab3HasPerm) { %>
				<li <%if ("3".equalsIgnoreCase(tab)) {%> class="current"<% } %>><a href="#"><span onclick="ajaxpage('action/replenish.jsp?tab=3&cd_card1='+getCdCard1(), 'div_main')"><%=Bean.webposXML.getfieldTransl("fee_kind_membership", false) %></span></a></li>
				<% } %>
				<% if (tab4HasPerm) { %>
				<li <%if ("4".equalsIgnoreCase(tab)) {%> class="current"<% } %>><a href="#"><span onclick="ajaxpage('action/replenish.jsp?tab=4&cd_card1='+getCdCard1(), 'div_main')"><%=Bean.webposXML.getfieldTransl("fee_kind_admission_short2", false) %></span></a></li>
				<% } %>
				<% if (tab5HasPerm) { %>
				<li <%if ("5".equalsIgnoreCase(tab)) {%> class="current"<% } %>><a href="#"><span onclick="ajaxpage('action/replenish.jsp?tab=5&cd_card1='+getCdCard1(), 'div_main')"><%=Bean.webposXML.getfieldTransl("title_target_programs2", false) %></span></a></li>
				<% } %>
				</ul>
				</div>
			</td>
		</tr>
	</table>
<br>

<% if ("check_card".equalsIgnoreCase(action)) { 
	ArrayList<String> pParam = new ArrayList<String>();
	
	String membership_cd_currency = "";	
	
	pParam.add(id_term);
	pParam.add(cd_card1);
	pParam.add(replenish_type);
	pParam.add(Bean.getDateFormat());
	
	String[] results = new String[10];
	
	results 						= Bean.executeFunction("PACK$WEBPOS_UI.oper_check_card", pParam, results.length);
	resultInt 						= results[0];
	id_card_status					= results[1];
	replenish_month_value  	  		= results[2];
	membership_last_date			= results[3];
	membership_nopay_month          = results[4];
	membership_need_pay_sum			= results[5];
	membership_max_pay_month		= results[6];
	membership_cd_currency			= results[7];
	membership_fee_margin			= results[8];
	resultMessage 					= results[9];
	
	if (Bean.C_SUCCESS_RESULT.equalsIgnoreCase(resultInt)) {
		if (("3".equalsIgnoreCase(tab))) {
			if (!Bean.isEmpty(membership_need_pay_sum)) {
				if (Bean.isEmpty(membership_fee)) {
					membership_fee = membership_need_pay_sum;
				}
			}
		}
	}
} %>

<% if (("1".equalsIgnoreCase(tab) && tab1HasPerm) ||
		("2".equalsIgnoreCase(tab) && tab2HasPerm) ||
		("3".equalsIgnoreCase(tab) && tab3HasPerm) ||
		("4".equalsIgnoreCase(tab) && tab4HasPerm)) { %>
	<%
	
	String cdTransType = "REC_SHARE_FEE";
	if ("1".equalsIgnoreCase(tab)) {
		cdTransType = "REC_POINT_FEE";
	} else if ("2".equalsIgnoreCase(tab)) {
		cdTransType = "REC_SHARE_FEE";
	} else if ("3".equalsIgnoreCase(tab)) {
		cdTransType = "REC_MEMBERSHIP_FEE";
	} else if ("4".equalsIgnoreCase(tab) || "5".equalsIgnoreCase(tab)) {
		cdTransType = "REC_MTF";
	}
	
	String marginText = Bean.getMarginDescription(idClub, idDealer, cdTransType);
	String marginMembershipFeeText = "";
	if (!"REC_MEMBERSHIP_FEE".equalsIgnoreCase(cdTransType) &&
			Bean.C_MEMBERSHIP_FEE_ERROR.equalsIgnoreCase(resultInt)) {
		marginMembershipFeeText = Bean.getMarginDescription(idClub, idDealer, "REC_MEMBERSHIP_FEE");
	}
	
	%>
	
	<script type="text/javascript">
		
			function validateReplanishCard(){
				var formParam = new Array (
					new Array ('cd_card1', 'card', 1)
				);
				return validateForm(formParam, 'updateForm5');
			}
		
			function validateReplanishSum(){
				var returnValue = false;
				var formParam = new Array (
					new Array ('cd_card1', 'card', 1),
					<% if ("1".equalsIgnoreCase(tab)) { %>
						<% if (Bean.C_MEMBERSHIP_FEE_ERROR.equalsIgnoreCase(resultInt)) { %>
						new Array ('point_fee', 'oper_sum_zero', 1),
						<% } else {%>
						new Array ('point_fee', 'oper_sum', 1),
						<% } %>
					<% } else if ("2".equalsIgnoreCase(tab)) { %>
						<% if (Bean.C_MEMBERSHIP_FEE_ERROR.equalsIgnoreCase(resultInt)) { %>
						new Array ('share_fee', 'oper_sum_zero', 1),
						<% } else {%>
						new Array ('share_fee', 'oper_sum', 1),
						<% } %>
					<% } else if ("3".equalsIgnoreCase(tab)) { %>
					new Array ('membership_fee', 'oper_sum', 1),
					<% } else if ("4".equalsIgnoreCase(tab)) { %>
					new Array ('tp_fee', 'oper_sum', 1),
					<% } %>
					new Array ('replenish_currency', 'number', 1)
				);		
				<% if ("1".equalsIgnoreCase(tab) && Bean.C_MEMBERSHIP_FEE_ERROR.equalsIgnoreCase(resultInt)) { %>
					returnValue = calcMultipleFeeSum('point_fee');
					if (!returnValue) {
						return returnValue;
					}
				<% } else if ("2".equalsIgnoreCase(tab) && Bean.C_MEMBERSHIP_FEE_ERROR.equalsIgnoreCase(resultInt)) { %>
					returnValue = calcMultipleFeeSum('share_fee');
					if (!returnValue) {
						return returnValue;
					}
				<% } %>
				returnValue = validateForm(formParam, 'updateForm5');
				//alert('returnValue='+returnValue);
				return returnValue;
			}
			function showCheckCardButton(card){
				//checkBox = document.getElementById(elem);
				element = document.getElementById('check_card');
				if (card.value == '' || card.value == null) {
					element.className = 'img_check_card_inactive';
				} else {
					element.className = 'img_check_card';
				}
			}
			function calcMultipleFeeSum(elem) {
				var returnValue = false;
				var membership_fee_sum = 0;
				//var membership_fee_margin_sum = 0;
				//var share_fee_margin_sum = 0;
				var calc_fee_sum = 0;
					
				calc_fee_element = document.getElementById(elem);
				
				try {
					membership_fee_element = document.getElementById('membership_fee');
					membership_fee_sum = membership_fee_element.value.replace(",",".");
				} catch(err) {}
				//try {
				//	membership_fee_margin_element = document.getElementById('membership_fee_margin');
				//	membership_fee_margin_sum = membership_fee_margin_element.value.replace(",",".");
				//} catch(err) {}
				//try {
				//	share_fee_margin_element = document.getElementById('share_fee_margin');
				//	share_fee_margin_sum = share_fee_margin_element.value.replace(",",".");
				//} catch(err) {}
				try {
					calc_fee_sum = calc_fee_element.value.replace(",",".");
				} catch(err) {}
				
				total_fee = document.getElementById('total_fee');
				
				try {
					totalSum = (+calc_fee_sum + +membership_fee_sum /*+ +membership_fee_margin_sum + +share_fee_margin_sum*/).toFixed(2);
					//totalSum = (totalSum + 0).toFixed(2);
					
					if (isNaN(totalSum)) {
						total_fee.value = 'Ошибка';
					} else {
						total_fee.value = ("" + totalSum).replace(".",",");
					}
					try {
						if (calc_fee_element.value!='') {
							calc_fee_element.value = ((+calc_fee_sum).toFixed(2)).replace(".",",");
						}
					} catch(err) {
						calc_fee_element.value = calc_fee_sum;
					}	
				} catch(err) {
					alert(err);
					return false;
				}
				return true;
			}
			<% if ("1".equalsIgnoreCase(tab) && Bean.C_MEMBERSHIP_FEE_ERROR.equalsIgnoreCase(resultInt)) { %>
			calcMultipleFeeSum('point_fee');
			<% } else if ("2".equalsIgnoreCase(tab) && Bean.C_MEMBERSHIP_FEE_ERROR.equalsIgnoreCase(resultInt)) { %>
			calcMultipleFeeSum('share_fee');
			<% } %>
			<% if (!("check_card".equalsIgnoreCase(action) && Bean.C_SUCCESS_RESULT.equalsIgnoreCase(resultInt))) { %>
			card_mask2("cd_card1");
			<% } %>
			</script>
		<form name="updateForm5" id="updateForm5" accept-charset="UTF-8" method="POST">
			<% if ("check_card".equalsIgnoreCase(action)) {  %>
				<% if (!(Bean.C_SUCCESS_RESULT.equalsIgnoreCase(resultInt))) { %>
					<% if ((!"3".equalsIgnoreCase(tab) && Bean.C_MEMBERSHIP_FEE_ERROR.equalsIgnoreCase(resultInt))) {%>
					<input type="hidden" name="type" value="card">
					<input type="hidden" name="action" value="replenish">
					<input type="hidden" name="cd_card1" id="cd_card1" value="<%=cd_card1 %>">
					<% } else { %>
					<input type="hidden" name="action" value="check_card">
					<% } %>
				<% } else { %>
					<% if (!"4".equalsIgnoreCase(tab)) { %>
					<input type="hidden" name="type" value="card">
					<input type="hidden" name="action" value="replenish">
					<% } %>
					<input type="hidden" name="cd_card1" id="cd_card1" value="<%=cd_card1 %>">
				<% } %>
			<% } else { %>
				<input type="hidden" name="action" value="check_card">
			<% } %>
			<input type="hidden" name="id_term" value="<%=id_term %>">
			<% if (("1".equalsIgnoreCase(tab))) { %>
			<input type="hidden" name="replenish_type" value="POINT_FEE">
			<input type="hidden" name="process" value="yes">
			<% } else if (("2".equalsIgnoreCase(tab))) { %>
			<input type="hidden" name="replenish_type" value="SHARE_FEE">
			<% } else if (("3".equalsIgnoreCase(tab))) { %>
			<input type="hidden" name="replenish_type" value="MEMBERSHIP_FEE">
			<% } else if (("4".equalsIgnoreCase(tab))) { %>
			<input type="hidden" name="replenish_type" value="MEMBERSHIP_TARGET_FEE">
			<% } %>
			<input type="hidden" name="membership_month_sum" value="<%=replenish_month_value %>">

			<input type="hidden" name="replenish_currency" value="<%=Bean.loginTerm.getValue("CD_TERM_CURRENCY") %>">
			<table class="action_table">
				<% if (!"check_card".equalsIgnoreCase(action)) {  %>
					<tr><td <%=Bean.getDetailTableFirstColumnWidth() %>><%=Bean.webposXML.getfieldTransl("card", true) %></td><td><input type="text" name="cd_card1" id="cd_card1" size="20" value="<%=cd_card1 %>"  class="inputfield" onkeyup="showCheckCardButton(this)">&nbsp;<%=Bean.getWEBPosCheckCardButton("check_card", "replenish", "updateForm5", "div_action_big") %><br><span style="font-size: 10px;"><%=Bean.webposXML.getfieldTransl("cd_card1_hint", false) %></span></td></tr>
					<tr><td colspan="2">&nbsp;</td></tr>	
					<tr><td colspan=2  class="center">
					<%=Bean.getSubmitButtonAjax("action/replenish.jsp", "button_further", "updateForm5", "div_main", "validateReplanishCard") %>
					</td></tr>
				<% } else { %>
					<% if (Bean.C_SUCCESS_RESULT.equalsIgnoreCase(resultInt) || 
							(!"3".equalsIgnoreCase(tab) && Bean.C_MEMBERSHIP_FEE_ERROR.equalsIgnoreCase(resultInt))) { %>
						<tr><td <%=Bean.getDetailTableFirstColumnWidth() %>><%=Bean.webposXML.getfieldTransl("card", false) %></td><td colspan="2"><input type="text" name="cd_card1_hide" id="cd_card1_hide" size="20" value="<%=Bean.hideCdCard1(cd_card1) %>" readonly="readonly" class="inputfield_finish_blue"></td></tr>
						<% if (("3".equalsIgnoreCase(tab))) { %>
							<tr><td><%= Bean.webposXML.getfieldTransl("membership_month_sum", false) %></td><td><input type="text" name="membership_month_sum_txt" id="membership_month_sum_txt" size="20" value="<%=replenish_month_value + " " + termCurrency %>" readonly class="inputfield_finish_red"></td></tr>
							<tr><td><%= Bean.webposXML.getfieldTransl("membership_last_date", false) %></td><td><input type="text" name="membership_last_date" id="membership_last_date" size="20" value="<%=membership_last_date %>" readonly class="inputfield_finish_green"></td></tr>
							<% if (!Bean.isEmpty(membership_nopay_month)) { %>
							<tr><td><%= Bean.webposXML.getfieldTransl("membership_nopay_month", false) %></td><td><input type="text" name="membership_nopay_month" id="membership_nopay_month" size="20" value="<%=membership_nopay_month %>" readonly class="inputfield_finish_red"></td></tr>
							<% } %>
						<% } %>

						<% if ("4".equalsIgnoreCase(tab)) { 
						
							String userTargetProgramCount = "0";
							%>

							<% StringBuilder html = new StringBuilder(); %>
							<% if ("1".equalsIgnoreCase(tab2) || Bean.isEmpty(tab2)) { %> 
								<% 	wpClubCardObject card = new wpClubCardObject(cd_card1);
									html.append(card.getUserTargetProgramImagesHTML(user_tp_find, l_user_tp_page_beg, l_user_tp_page_end));
									userTargetProgramCount = card.getTargetProgramCount();
									if ("0".equalsIgnoreCase(userTargetProgramCount)) {

										if (tab2 == null || "".equalsIgnoreCase(tab2)) {
											if (tab2 == null || "".equalsIgnoreCase(tab2)) {
												tab2 = "2";
											}
										}
									} else {
										tab2 = "1";
									}
								}
								%>
							<% if ("1".equalsIgnoreCase(tab2)) { %> 
							<tr>
								<td colspan="3">
									<table <%=Bean.getTableBottomFilter() %>>
									  	<tr>
										<%= Bean.getFindHTML("user_tp_find", user_tp_find, "action/replenish.jsp?action=check_card&tab2=" + tab2 + "&id_term=" + id_term + "&replenish_type=" + replenish_type + "&cd_card1=" + cd_card1 + "&id_prg_parent=" + id_prg_parent + "&user_tp_page=1&", "div_main") %>
										
										
									    <!-- Вывод страниц -->
										<%= Bean.getPagesHTML(pageFormName + tagUserTP, "action/replenish.jsp?action=check_card&tab2=" + tab2 + "&id_term=" + id_term + "&replenish_type=" + replenish_type + "&cd_card1=" + cd_card1 + "&id_prg_parent=" + id_prg_parent + "&cnt=" + userTargetProgramCount + "&", "user_tp_page", userTargetProgramCount, "div_main") %>
									  	</tr>
									</table>
									
							</td></tr>
							<tr>
								<td colspan="3">
									<% if ("0".equalsIgnoreCase(userTargetProgramCount)) { %>
									<br><span style="color:black;font-size:14px;"><%=Bean.webposXML.getfieldTransl("title_target_program_not_found_find_all", false) %><br>
										<span style="color:red; text-decoration: underline; cursor: pointer;" onclick="ajaxpage('action/replenish.jsp?action=check_card&tab2=2&cd_card1=<%=cd_card1 %>&id_term=<%=id_term %>&replenish_type=<%=replenish_type %>', 'div_main')"><%=Bean.webposXML.getfieldTransl("all_target_program", false) %></span>
									</span><br><br>
									<% } else { %>
									<span style="color:red; text-decoration: underline; cursor: pointer;" onclick="ajaxpage('action/replenish.jsp?action=check_card&tab2=2&all_tp_find=&all_tp_page=1&cd_card1=<%=cd_card1 %>&id_term=<%=id_term %>&replenish_type=<%=replenish_type %>', 'div_main')"><%=Bean.webposXML.getfieldTransl("all_target_program", false) %></span>
									<% } %>
								</td>
							</tr>
							<tr><td colspan=3  class="center">
							<%=Bean.getSubmitButtonAjax("action/replenish.jsp", "button_back", "updateForm5", "div_main") %>
							</td></tr>
							<tr><td colspan=3  class="center">
							<%= html.toString() %>
							</td></tr>
							<% } %>
							<% if ("2".equalsIgnoreCase(tab2)) { %>
								<% 	html.append(Bean.loginTerm.getAllTargetProgramImagesHTML("USER", id_prg_parent, all_tp_user_find, l_all_tp_user_page_beg, l_all_tp_user_page_end));
								%>
							<tr>
								<td colspan="3">
									<table <%=Bean.getTableBottomFilter() %>>
									  	<tr>
										<%= Bean.getFindHTML("all_tp_user_find", all_tp_user_find, "action/replenish.jsp?action=check_card&tab2=" + tab2 + "&id_term=" + id_term + "&replenish_type=" + replenish_type + "&cd_card1=" + cd_card1 + "&id_prg_parent=" + id_prg_parent + "&all_tp_user_page=1&", "div_main") %>
										
										
									    <!-- Вывод страниц -->
										<%= Bean.getPagesHTML(pageFormName + tagAllTP, "action/replenish.jsp?action=check_card&tab2=" + tab2 + "&id_term=" + id_term + "&replenish_type=" + replenish_type + "&cd_card1=" + cd_card1 + "&id_prg_parent=" + id_prg_parent + "&cnt=" + Bean.loginTerm.getTargetProgramCount() + "&", "all_tp_user_page", Bean.loginTerm.getTargetProgramCount(), "div_main") %>
									  	</tr>
									</table>
							</td></tr>
							<tr>
								<td colspan="3">
									<span style="color:red; text-decoration: underline; cursor: pointer;" onclick="ajaxpage('action/replenish.jsp?action=check_card&tab2=1&all_tp_find=&all_tp_page=1&cd_card1=<%=cd_card1 %>&id_term=<%=id_term %>&replenish_type=<%=replenish_type %>', 'div_main')"><%=Bean.webposXML.getfieldTransl("user_target_program", false) %></span>
								</td>
							</tr>
							<% if ("0".equalsIgnoreCase(Bean.loginTerm.getTargetProgramCount())) { %>
							<tr>
								<td colspan="3">
									<span style="color:red;font-size:14px;"><%=Bean.webposXML.getfieldTransl("title_target_program_not_found", false) %></span>
								</td>
							</tr>
							<% } %>
							<tr><td colspan=3  class="center">
							<% if (!Bean.isEmpty(id_prg_parent)) { %>
							<%=Bean.getSubmitButtonAjax("action/replenishupdate.jsp", "button_back", "updateForm3", "div_action_big") %>
							<% } else { %>
							<%=Bean.getSubmitButtonAjax("action/replenish.jsp", "button_back", "updateForm5", "div_main") %>
							<% } %>
							<% if (!Bean.isEmpty(id_prg_parent)) { 
								wpTargetPrgObject prg = new wpTargetPrgObject(id_prg_parent);
							%>
							<br><div style="color:green; width:100%; text-align: center"><%=prg.getValue("NAME_TARGET_PRG") %></div>
							<% } %>
							</td></tr>
							<tr><td colspan=3  class="center">
							<%= html.toString() %>
							</td></tr>
							<% } %>
						<% } else { %>
							<% if ("1".equalsIgnoreCase(tab)) { %>
								<% if (Bean.C_MEMBERSHIP_FEE_ERROR.equalsIgnoreCase(resultInt)) { %>
								<tr><td colspan="2"><span id="error_description"><%=resultMessage %></span></td></tr>
								<tr>
									<td><%=Bean.webposXML.getfieldTransl("point_fee_sum", true) %></td>
									<td colspan="2">
										<input type="text" name="point_fee" id="point_fee" size="15" value="<%=point_fee %>" class="inputfield" maxlength="15" onchange="calcMultipleFeeSum('point_fee')"><input type="text" name="sname_term_currency" size="5" value="<%=Bean.getWebPOSPointCurrencyName() %>" readonly="readonly" class="inputfield-ro">
									</td>
								</tr>
								<tr>
									<td><%=Bean.webposXML.getfieldTransl("membership_fee_sum", false) %></td>
									<td colspan="2">
										<input type="text" name="membership_fee" id="membership_fee" size="15" value="<%=membership_need_pay_sum %>" readonly class="inputfield-ro" maxlength="15" ><input type="text" name="sname_term_currency" size="5" value="<%=Bean.getWebPOSPointCurrencyName() %>" readonly="readonly" class="inputfield-ro">
									</td>
								</tr>
								<tr>
									<td><%=Bean.webposXML.getfieldTransl("fee_sum_total", false) %></td>
									<td colspan="2">
										<input type="text" name="total_fee" id="total_fee" size="15" value="" readonly class="inputfield-ro" maxlength="15" ><input type="text" name="sname_term_currency" size="5" value="<%=Bean.getWebPOSPointCurrencyName() %>" readonly="readonly" class="inputfield-ro">
									</td>
								</tr>
								<% } else { %>
								<tr>
									<td><%=Bean.webposXML.getfieldTransl("point_fee_sum", true) %></td>
									<td colspan="2">
										<input type="text" name="point_fee" id="point_fee" size="15" value="<%=point_fee %>" class="inputfield" maxlength="15" ><input type="text" name="sname_term_currency" size="5" value="<%=Bean.getWebPOSPointCurrencyName() %>" readonly="readonly" class="inputfield-ro">
										<input type="hidden" name="pay_type" value="SMPU_CARD">
									</td>
								</tr>
								<% } %>
							<% } else if ("2".equalsIgnoreCase(tab)) { %>
								<% if (Bean.C_MEMBERSHIP_FEE_ERROR.equalsIgnoreCase(resultInt)) { %>
								<tr><td colspan="2"><span id="error_description"><%=resultMessage %></span></td></tr>
								<tr>
									<td><%=Bean.webposXML.getfieldTransl("share_fee_sum", true) %></td>
									<td colspan="2">
										<input type="text" name="share_fee" id="share_fee" size="15" value="<%=share_fee %>" class="inputfield" maxlength="15" onchange="calcMultipleFeeSum('share_fee')"><input type="text" name="sname_term_currency" size="5" value="<%=termCurrency %>" readonly="readonly" class="inputfield-ro">
									</td>
								</tr>
								<tr>
									<td><%=Bean.webposXML.getfieldTransl("membership_fee_sum", false) %></td>
									<td colspan="2">
										<input type="text" name="membership_fee" id="membership_fee" size="15" value="<%=membership_need_pay_sum %>" readonly class="inputfield-ro" maxlength="15" ><input type="text" name="sname_term_currency" size="5" value="<%=termCurrency %>" readonly="readonly" class="inputfield-ro">
									</td>
								</tr>
								<tr>
									<td><%=Bean.webposXML.getfieldTransl("fee_sum_total", false) %></td>
									<td colspan="2">
										<input type="text" name="total_fee" id="total_fee" size="15" value="" readonly class="inputfield-ro" maxlength="15" ><input type="text" name="sname_term_currency" size="5" value="<%=termCurrency %>" readonly="readonly" class="inputfield-ro">
									</td>
								</tr>
								<% } else { %>
								<tr>
									<td><%=Bean.webposXML.getfieldTransl("share_fee_sum", true) %></td>
									<td colspan="2">
										<input type="text" name="share_fee" id="share_fee" size="15" value="<%=share_fee %>" class="inputfield" maxlength="15" ><input type="text" name="sname_term_currency" size="5" value="<%=termCurrency %>" readonly="readonly" class="inputfield-ro">
									</td>
								</tr>
								<% } %>
							<% } else if ("3".equalsIgnoreCase(tab)) { %>
							<tr>
								<td><%=Bean.webposXML.getfieldTransl("membership_fee_sum", true) %></td>
								<td colspan="2">
									<%= Bean.getMembershipMonths("membership_fee", Bean.webposXML.getfieldTransl("fee_sum", false), membership_fee, membership_max_pay_month, replenish_month_value, termCurrency) %>
								</td>
							</tr>
							<% } else { %>
							<tr>
								<td><%=Bean.webposXML.getfieldTransl("mtf_sum", true) %></td>
								<td colspan="2">
									<input type="text" name="tp_fee" id="tp_fee" size="15" value="<%=tp_fee %>" class="inputfield" maxlength="15" ><input type="text" name="sname_term_currency" size="5" value="<%=termCurrency %>" readonly="readonly" class="inputfield-ro">
								</td>
							</tr>
							<% } %>
							<% if ("1".equalsIgnoreCase(tab)) { %>
							<tr><td colspan="2">&nbsp;</td></tr>	
							<tr><td colspan=2  class="center">
								<%=Bean.getSubmitButtonAjax("action/replenishupdate.jsp", "button_further", "updateForm5", "div_action_big", "validateReplanishSum") %>
								<%=Bean.getSubmitButtonAjax("action/replenish.jsp", "button_back", "formBack", "div_main") %>
							</td></tr>
							<% } else { %>
							<tr>
								<td colspan="4"><br><%=Bean.webposXML.getfieldTransl("goods_pay_way", true) %></td>
							</tr>
							<tr>
								<td colspan="4">
									<table border="0">
										<tr>
											<% if (("2".equalsIgnoreCase(tab))) { %>
												<%=Bean.getPayTypeImage("CASH", "WEBPOS_SERVICE_REPLANISH_SHARE_FEE_PAY_CASH", "action/replenishupdate.jsp?pay_type=CASH&process=no", "div_action_big", "updateForm5", "validateReplanishSum") %>
												<%=Bean.getPayTypeImage("BANK_CARD", "WEBPOS_SERVICE_REPLANISH_SHARE_FEE_PAY_BANK_CARD", "action/replenishupdate.jsp?pay_type=BANK_CARD&process=no", "div_action_big", "updateForm5", "validateReplanishSum") %>
												<%=Bean.getPayTypeImage("INVOICE", "WEBPOS_SERVICE_REPLANISH_SHARE_FEE_MAKE_INVOICE", "action/replenishupdate.jsp?pay_type=INVOICE&process=yes", "div_action_big", "updateForm5", "validateReplanishSum") %>
											<% } else if (("3".equalsIgnoreCase(tab))) { %>
												<%=Bean.getPayTypeImage("CASH", "WEBPOS_SERVICE_REPLANISH_MEMBERSHIP_FEE_PAY_CASH", "action/replenishupdate.jsp?pay_type=CASH&process=no", "div_action_big", "updateForm5", "validateReplanishSum") %>
												<%=Bean.getPayTypeImage("SMPU_CARD", "WEBPOS_SERVICE_REPLANISH_MEMBERSHIP_FEE_PAY_BANK_CARD", "action/replenishupdate.jsp?pay_type=SMPU_CARD&process=yes", "div_action_big", "updateForm5", "validateReplanishSum") %>
												<%=Bean.getPayTypeImage("BANK_CARD", "WEBPOS_SERVICE_REPLANISH_MEMBERSHIP_FEE_PAY_POINTS", "action/replenishupdate.jsp?pay_type=BANK_CARD&process=no", "div_action_big", "updateForm5", "validateReplanishSum") %>
												<%=Bean.getPayTypeImage("INVOICE", "WEBPOS_SERVICE_REPLANISH_MEMBERSHIP_FEE_MAKE_INVOICE", "action/replenishupdate.jsp?pay_type=INVOICE&process=yes", "div_action_big", "updateForm5", "validateReplanishSum") %>
											<% } %>
										</tr>
									</table>
								</td>
							</tr>
							<tr><td colspan="2">&nbsp;</td></tr>	
							<tr><td colspan=2  class="center">
								<%=Bean.getSubmitButtonAjax("action/replenish.jsp", "button_back", "formBack", "div_main") %>
							</td></tr>
							<% } %>
						<% } %>
					<% } else { %>
						<tr><td <%=Bean.getDetailTableFirstColumnWidth() %>><%=Bean.webposXML.getfieldTransl("card", true) %></td><td><input type="text" name="cd_card1" id="cd_card1" size="20" value="<%=cd_card1 %>"  class="inputfield" onkeyup="showCheckCardButton(this)">&nbsp;<%=Bean.getWEBPosCheckCardButton("check_card", "replenish", "updateForm5", "div_action_big") %><br><span style="font-size: 10px;"><%=Bean.webposXML.getfieldTransl("cd_card1_hint", false) %></span></td></tr>
						<tr><td colspan="2">&nbsp;</td></tr>	
						<tr><td colspan=2  class="center">
							<%=Bean.getSubmitButtonAjax("action/replenish.jsp", "button_further", "updateForm5", "div_main", "validateReplanishCard") %>
						</td></tr>
						<tr><td colspan="2"><span id="error_title"><%=Bean.webposXML.getfieldTransl("operation_error", false) %></span></td></tr>
						<tr><td colspan="2"><span id="error_description"><%=resultMessage %></span></td></tr>
						<% if (Bean.C_MEMBERSHIP_FEE_ERROR.equalsIgnoreCase(resultInt) && Bean.hasWriteMenuPermission("WEBPOS_SERVICE_REPLANISH_MEMBERSHIP_FEE")) { %>
							<tr><td colspan="2"  class="center">
								<%=Bean.getSubmitButtonAjax("action/replenish.jsp", "bring", "updateForm2", "div_main") %> 
							</td></tr>
						<% } else if (Bean.C_CARD_NOT_GIVEN.equalsIgnoreCase(resultInt) && Bean.hasWriteMenuPermission("WEBPOS_SERVICE_CARD_ACTIVATION_REGISTRATION")) { %>
							<tr><td colspan="2"  class="center">
								<%=Bean.getSubmitButtonAjax("action/new_client.jsp", "button_give", "updateForm2", "div_main") %>
							</td></tr>
						<% } else if (Bean.C_CARD_NOT_QUESTIONED.equalsIgnoreCase(resultInt) && Bean.hasWriteMenuPermission("WEBPOS_SERVICE_CARD_ACTIVATION_QUESTIONNAIRE")) { %>
							<tr><td colspan="2"  class="center">
								<%=Bean.getSubmitButtonAjax("action/new_client_questionnaire.jsp", "button_questionnaire", "updateForm2", "div_action_big") %>
							</td></tr>
						<% } else if (Bean.C_CARD_NOT_ACTIVATED.equalsIgnoreCase(resultInt) && Bean.hasWriteMenuPermission("WEBPOS_SERVICE_CARD_ACTIVATION_ACTIVATION")) { %>
							<tr><td colspan="2"  class="center">
								<%=Bean.getSubmitButtonAjax("action/new_client_activation.jsp", "activate", "updateForm2", "div_action_big", "validateReplanishCard") %>
							</td></tr>
						<% } %>
					<% } %>
				<% } %>
				<% if (!Bean.isEmpty(marginText)) { %>
				<tr><td colspan="2">
					<div id=div_hints>
						<i><b><%=Bean.webposXML.getfieldTransl("title_note", false) %>
						</b><br><%=marginText %>
						<% if (!Bean.isEmpty(marginMembershipFeeText)) { %>
							<br><%=marginMembershipFeeText %>
						<% } %>
						<% if ("check_card".equalsIgnoreCase(action) && Bean.C_SUCCESS_RESULT.equalsIgnoreCase(resultInt) &&
								!Bean.isEmpty(resultMessage)) {  %>
							<br><%=resultMessage %>
						<% } %>
						</i>
					</div>
				</td></tr>
				<% } %>
				<% if (!"check_card".equalsIgnoreCase(action) || !Bean.C_SUCCESS_RESULT.equalsIgnoreCase(resultInt)) { %>
				<tr><td colspan="2">
					<div id=div_hints>
						<%=Bean.getWEBPosOnlyTestCards() %>
					</div>
				</td></tr>
				<% } %>
			</table>
		</form>

		<form name="formBack" id="formBack" accept-charset="UTF-8" method="POST">
			<input type="hidden" name="action" value="show">
			<input type="hidden" name="id_term" value="<%=id_term %>">
			<input type="hidden" name="cd_card1" id="cd_card1" value="<%=cd_card1 %>">
			<input type="hidden" name="replenish_type" value="<%=replenish_type %>">
		</form>


		<% if (Bean.C_MEMBERSHIP_FEE_ERROR.equalsIgnoreCase(resultInt)) { %>
			<form name="updateForm2" id="updateForm2" accept-charset="UTF-8" method="POST">
			    <input type="hidden" name="action" value="check_card">
				<input type="hidden" name="cd_card1" id="cd_card1" value="<%=cd_card1 %>">
				<input type="hidden" name="tab" value="3">
				<input type="hidden" name="replenish_type" value="MEMBERSHIP_FEE">
			</form>
		<% } %>
		<% if (Bean.C_CARD_NOT_GIVEN.equalsIgnoreCase(resultInt)) { %>
			<form name="updateForm2" id="updateForm2" accept-charset="UTF-8" method="POST">
				<input type="hidden" name="tab" value="1">
				<input type="hidden" name="id_term" value="<%=id_term %>">
				<input type="hidden" name="cd_card1" id="cd_card1" value="<%=cd_card1 %>">
			</form>
		<% } %>
		<% if (Bean.C_CARD_NOT_ACTIVATED.equalsIgnoreCase(resultInt)) { %>
			<form name="updateForm2" id="updateForm2" accept-charset="UTF-8" method="POST">
				<input type="hidden" name="type" value="client">
				<input type="hidden" name="action" value="check_card">
				<input type="hidden" name="process" value="yes">
				<input type="hidden" name="id_term" value="<%=id_term %>">
				<input type="hidden" name="cd_card1" id="cd_card1" value="<%=cd_card1 %>">
			</form>
		<% } %>
		<% if (Bean.C_CARD_NOT_QUESTIONED.equalsIgnoreCase(resultInt)) { %>
			<% wpClubCardObject card = new wpClubCardObject(cd_card1); %>
			<form name="updateForm2" id="updateForm2" accept-charset="UTF-8" method="POST">
				<input type="hidden" name="action" value="edit">
				<input type="hidden" name="id_term" value="<%=id_term %>">
				<input type="hidden" name="cd_card1" id="cd_card1" value="<%=cd_card1 %>">
				<input type="hidden" name="id_role" value="<%=card.getValue("ID_NAT_PRS_ROLE_CURRENT") %>">
			</form>
		<% } %>
<% } else if ("5".equalsIgnoreCase(tab) && tab5HasPerm) { 
	String id_target_prg = Bean.getDecodeParamPrepare(parameters.get("id_target_prg"));
	String prgName              = "";
	String prgEntranceFee    	= "";
	String prgPayEntranceFee    = "";
	String prgPayPeriod         = "";
	String prgPayPeriodName		= "";
	String prgPayAmount			= "";
	String prgPayAmountFull		= "";
	String prgCanSubscribe      = "";
	String prgNeedSubscribe     = "";
	String prgUseType			= "";
	String prgNeedAdminConfirm	= "";
	
	String backLink 			= "";
	String backDiv				= "";
	
	if (Bean.isEmpty(id_target_prg)) {
		StringBuilder html = new StringBuilder();
		html.append(Bean.loginTerm.getAllTargetProgramImagesHTML("ALL", id_prg_parent, all_tp_general_find, l_all_tp_general_page_beg, l_all_tp_general_page_end));
		%>
			<form name="updateForm5" id="updateForm5" accept-charset="UTF-8" method="POST">
				<input type="hidden" name="tab" value="5">
				<input type="hidden" name="id_term" value="<%=id_term %>">
				<input type="hidden" name="replenish_type" value="TARGET_PROGRAMS">
				<input type="hidden" name="replenish_currency" value="<%=Bean.loginTerm.getValue("CD_TERM_CURRENCY") %>">
				<table class="action_table">
					<tr>
						<td colspan="3">
							<table <%=Bean.getTableBottomFilter() %>>
							  	<tr>
								<%= Bean.getFindHTML("all_tp_general_find", all_tp_general_find, "action/replenish.jsp?tab=5&action=select&id_term=" + id_term + "&replenish_type=" + replenish_type + "&cd_card1=" + cd_card1 + "&id_prg_parent=" + id_prg_parent + "&all_tp_general_page=1&", "div_main") %>
								
								
							    <!-- Вывод страниц -->
								<%= Bean.getPagesHTML(pageFormName + tagAllTPGeneral, "action/replenish.jsp?tab=5&action=select&id_term=" + id_term + "&replenish_type=" + replenish_type + "&cd_card1=" + cd_card1 + "&id_prg_parent=" + id_prg_parent + "&cnt=" + Bean.loginTerm.getTargetProgramCount() + "&", "all_tp_general_page", Bean.loginTerm.getTargetProgramCount(), "div_main") %>
							  	</tr>
							</table>
					</td></tr>
					<tr>
						<td colspan="3">
							<% if ("0".equalsIgnoreCase(Bean.loginTerm.getTargetProgramCount())) { %>
							<span style="color:red;font-size:14px;"><%=Bean.webposXML.getfieldTransl("title_target_program_not_found", false) %></span><br>
							<% } %>
						</td>
					</tr>
					<% if (!Bean.isEmpty(id_prg_parent)) { 
						wpTargetPrgObject prg = new wpTargetPrgObject(id_prg_parent);
					%>
					<tr><td colspan=3  class="center">
						<%=Bean.getSubmitButtonAjax("action/replenish.jsp", "button_back", "updateForm2", "div_main") %>
						<br><div style="color:green; width:100%; text-align: center"><%=prg.getValue("NAME_TARGET_PRG") %></div>
					</td></tr>
					<% } %>
					<tr><td colspan=3  class="center">
					<%= html.toString() %>
					</td></tr>
				</table>
			</form>
			<form name="updateForm5" id="updateForm2" accept-charset="UTF-8" method="POST">
				<input type="hidden" name="tab" value="5">
				<input type="hidden" name="id_term" value="<%=id_term %>">
				<input type="hidden" name="id_prg_parent" value="">
				<input type="hidden" name="replenish_type" value="MEMBERSHIP_TARGET_FEE">
			</form>
	<% } else { 

		wpTargetPrgObject prg = new wpTargetPrgObject(id_target_prg);
		prgEntranceFee      = prg.getValue("ENTRANCE_FEE");
		prgPayEntranceFee 	= "";
		prgName 			= prg.getValue("NAME_TARGET_PRG");
		prgPayPeriod 		= prg.getValue("CD_TARGET_PRG_PAY_PERIOD");
		prgPayPeriodName    = prg.getValue("NAME_TARGET_PRG_PAY_PERIOD");
		prgPayAmount    	= prg.getValue("PAY_AMOUNT_FRMT");
		prgPayAmountFull   	= prg.getValue("PAY_AMOUNT_FULL_FRMT");
		prgCanSubscribe     = "N";
		prgNeedSubscribe    = prg.getValue("NEED_SUBSCRIBE");
		prgUseType			= "";
		prgNeedAdminConfirm	= prg.getValue("NEED_ADMINISTRATOR_CONFIRM");
		
		backLink 			= "action/replenish.jsp";
		backDiv				= "div_main";
	%>

	<% boolean canPay = true; 
	   boolean canEntranceFee = false;
	   boolean canSubscribe = false;
	   String validateScriptName = "";
	%>
					
	<% if (Bean.isEmpty(prgEntranceFee)) { 
	/*Целевая программа не требует вступительного взноса*/
	  } else { %>
		<% if (Bean.isEmpty(prgPayEntranceFee)) {
			canPay = false;
			canEntranceFee = true;
		   } else { 
			/*Вступительный взнос оплачен*/
		 } %>
	<% } %>
	<% 
					if ("Y".equalsIgnoreCase(prgCanSubscribe)) {
						canSubscribe = true;
					}
					%>
			
			<script>
			function validateData(){
				var returnValue = null;
				var formParam = new Array (
					new Array ('cd_card1', 'card', 1)
				);
				returnValue = validateFormForID(formParam, 'updateForm5');	
				return returnValue;
			}
			card_mask2("cd_card1");
		   	</script>
			<form name="updateForm5" id="updateForm5" accept-charset="UTF-8" method="POST">
				<input type="hidden" name="type" value="card">
				<input type="hidden" name="action" value="select_program">
				<input type="hidden" name="process" value="no">
				<input type="hidden" name="id_term" value="<%=id_term %>">
				<input type="hidden" name="replenish_type" value="<%=replenish_type %>">
				<input type="hidden" name="replenish_currency" value="<%=Bean.loginTerm.getValue("CD_TERM_CURRENCY") %>">
				<input type="hidden" name="id_target_prg" value="<%=id_target_prg %>">
			
				<table class="action_table">
					<tr><td <%=Bean.getDetailTableFirstColumnWidth() %>><%=Bean.webposXML.getfieldTransl("target_prg", false) %></td><td colspan="2"><input type="text" name="name_target_prg" id="name_target_prg" size="20" value="<%=prgName %>" readonly="readonly" class="inputfield_finish_green"></td></tr>
					<% if (!"IRREGULAR".equalsIgnoreCase(prgPayPeriod)) { %>
					<tr>
						<td><%=Bean.webposXML.getfieldTransl("target_prg_pay_frequency", false) %></td>
						<td colspan="2">
							<input type="text" name="pay_period_frmt" id="pay_period_frmt" size="20" value="<%=prgPayAmountFull %>" readonly="readonly" class="inputfield_finish_red">
							<input type="hidden" name="pay_period" id="pay_period" value="<%=prgPayAmount %>">
						</td>
					</tr>
					<% } else { %>
					<tr><td><%=Bean.webposXML.getfieldTransl("target_prg_pay_period", false) %></td><td colspan="2"><input type="text" name="target_prg_pay_period" id="target_prg_pay_period" size="20" value="<%=prgPayPeriodName %>" readonly="readonly" class="inputfield_finish_red"></td></tr>
					<% } %>
					<tr><td <%=Bean.getDetailTableFirstColumnWidth() %>><%=Bean.webposXML.getfieldTransl("card", true) %></td><td><input type="text" name="cd_card1" id="cd_card1" size="20" value="<%=cd_card1 %>"  class="inputfield" onkeyup="showCheckCardButton(this)">&nbsp;<%=Bean.getWEBPosCheckCardButton("check_card", "replenish", "updateForm5", "div_action_big") %><br><span style="font-size: 10px;"><%=Bean.webposXML.getfieldTransl("cd_card1_hint", false) %></span></td></tr>
					
					<tr><td colspan=3  class="center">&nbsp;</td></tr>
						<tr><td colspan=3  class="center">
							<%=Bean.getSubmitButtonAjax("action/replenishupdate.jsp", "button_further", "updateForm5", "div_action_big", "validateData()") %>
							<%=Bean.getSubmitButtonAjax(backLink, "button_back", "updateFormBack", backDiv) %>
						</td></tr>
				<tr><td colspan="2">
					<div id=div_hints>
						<%=Bean.getWEBPosOnlyTestCards() %>
					</div>
				</td></tr>
				</table>
			</form>
			<form name="updateFormBack" id="updateFormBack" accept-charset="UTF-8" method="POST">
				<input type="hidden" name="id_term" value="<%=id_term %>">
				<input type="hidden" name="replenish_type" value="<%=replenish_type %>">
				<input type="hidden" name="replenish_currency" value="<%=Bean.loginTerm.getValue("CD_TERM_CURRENCY") %>">
			</form>
	<% } %>
<% } else { %>
	<table <%=Bean.getTableDetail2Param() %>>
						<tr><td align="center" style="padding: 10px;"><font style="font-size: 22px; color:red; font-weight: bold;"><%=Bean.webposXML.getfieldTransl("operation_error", false) %></font></td></tr>
						<tr><td align="center"><%=Bean.webposXML.getfieldTransl("title_access_denied", false) %><br><br></td></tr>
	</table>

	<% } %>
<% } } %>
</body>
</html>