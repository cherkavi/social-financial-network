<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcWebPosBean"  />

<%@page import="java.util.HashMap"%>
<%@page import="webpos.wpClubCardObject"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Set"%>
<%@page import="java.util.Iterator"%>
<%@page import="webpos.wpChequeObject"%>
<%@page import="webpos.wpTargetPrgObject"%>
<%@page import="webpos.wpNatPrsTargetPrgObject"%>

<%@page import="webpos.wpTelegramObject"%>
<%@page import="webpos.wpNatPrsObject"%><html>
<head>
	<%= Bean.getMetaContent() %>
</head>

<body>
<%
HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String pageFormName = "WEBPOS_SERVICE_REPLANISH";
String tabSheetSFName = "WEBPOS_SERVICE_REPLANISH_SHARE_FEE";
String tabSheetMFName = "WEBPOS_SERVICE_REPLANISH_MEMBERSHIP_FEE";
String tabSheetMTFName = "WEBPOS_SERVICE_REPLANISH_MEMBERSHIP_TARGET_FEE";
String tagAllTP = "_ALL_TP";
String tagAllTPFind = "_ALL_TP_FIND";
String tagUserTP = "_USER_TP";
String tagUserTPFind = "_USER_TP_FIND";

Bean.setJspPageForTabName(pageFormName);

request.setCharacterEncoding("UTF-8");
String type		= Bean.getDecodeParam(parameters.get("type")); 
String action	= Bean.getDecodeParam(parameters.get("action")); 
String process	= Bean.getDecodeParam(parameters.get("process"));

type 	= Bean.isEmpty(type)?"":type;
action 	= Bean.isEmpty(action)?"":action;
process = Bean.isEmpty(process)?"":process;

String id_term			= Bean.getCurrentTerm();
String cd_card1			= Bean.getDecodeParam(parameters.get("cd_card1"));
cd_card1 = Bean.isEmpty(cd_card1)?"":cd_card1.replace(" ", "");

String id_prg_parent	= Bean.getDecodeParam(parameters.get("id_prg_parent"));

String tab = Bean.getDecodeParam(parameters.get("tab"));
tab		= Bean.isEmpty(tab)?Bean.tabsHmGetValue(pageFormName):tab;

String tab2 = Bean.getDecodeParam(parameters.get("tab2"));
tab2	= Bean.isEmpty(tab2)?"1":tab2;

String
	replenish_type				= Bean.getDecodeParam(parameters.get("replenish_type")),
	replenish_kind				= Bean.getDecodeParam(parameters.get("replenish_kind")),
	replenish_currency			= Bean.getDecodeParam(parameters.get("replenish_currency"));

Bean.loginTerm.getTermFeature();
String termCurrency = Bean.loginTerm.getValue("SNAME_TERM_CURRENCY");

String idClub = Bean.loginTerm.getValue("ID_CLUB");
String idDealer = Bean.loginTerm.getValue("ID_DEALER");

Bean.readWebPosMenuHTML();

String l_user_tp_page = Bean.getDecodeParam(parameters.get("user_tp_page"));
Bean.pageCheck(pageFormName + tagUserTP, l_user_tp_page);
String l_user_tp_page_beg = Bean.getFirstRowNumber(pageFormName + tagUserTP);
String l_user_tp_page_end = Bean.getLastRowNumber(pageFormName + tagUserTP);

String user_tp_find 	= Bean.getDecodeParam(parameters.get("user_tp_find"));
user_tp_find 	= Bean.checkFindString(pageFormName + tagUserTPFind, user_tp_find, l_user_tp_page);


String l_all_tp_page = Bean.getDecodeParam(parameters.get("all_tp_page"));
Bean.pageCheck(pageFormName + tagAllTP, l_all_tp_page);
String l_all_tp_page_beg = Bean.getFirstRowNumber(pageFormName + tagAllTP);
String l_all_tp_page_end = Bean.getLastRowNumber(pageFormName + tagAllTP);

String all_tp_find 	= Bean.getDecodeParam(parameters.get("all_tp_find"));
all_tp_find 	= Bean.checkFindString(pageFormName + tagAllTPFind, all_tp_find, l_all_tp_page);

String snamePointCurrency = Bean.webposXML.getfieldTransl("point_currency_name", false);

%>


<% if (!Bean.hasMenuPermission(pageFormName, Bean.C_WRITE_MENU_PERMISSION, Bean.loginTerm)) { %>
	<%=Bean.getErrorPermissionMessageShort(pageFormName, Bean.loginTerm) %>
	<% type = "error_term"; %>
<% } %>
<%
if (type.equalsIgnoreCase("error_term")) {
} else if (type.equalsIgnoreCase("card")) {
	
	
	boolean tab1HasPerm = Bean.hasWriteMenuPermission("WEBPOS_SERVICE_REPLANISH_POINT");
	boolean tab2HasPerm = Bean.hasWriteMenuPermission("WEBPOS_SERVICE_REPLANISH_SHARE_FEE");
	boolean tab3HasPerm = Bean.hasWriteMenuPermission("WEBPOS_SERVICE_REPLANISH_MEMBERSHIP_FEE");
	boolean tab4HasPerm = Bean.hasWriteMenuPermission("WEBPOS_SERVICE_REPLANISH_MEMBERSHIP_TARGET_FEE");
	boolean tab5HasPerm = Bean.hasWriteMenuPermission("WEBPOS_SERVICE_REPLANISH_TARGET_PROGRAMS");
	int tabCount = (tab1HasPerm?1:0)+(tab2HasPerm?1:0)+(tab3HasPerm?1:0)+(tab4HasPerm?1:0)+(tab5HasPerm?1:0);
	

	String id_card_status			= Bean.getDecodeParamPrepare(parameters.get("id_card_status"));
	String membership_month_sum		= Bean.getDecodeParamPrepare(parameters.get("membership_month_sum"));
	String membership_last_date		= Bean.getDecodeParamPrepare(parameters.get("membership_last_date"));
	String membership_nopay_month   = Bean.getDecodeParamPrepare(parameters.get("membership_nopay_month"));
	String membership_need_pay_sum  = Bean.getDecodeParamPrepare(parameters.get("membership_need_pay_sum"));
	String membership_max_pay_month = Bean.getDecodeParamPrepare(parameters.get("membership_max_pay_month"));

	String total_fee	    		= Bean.getDecodeParamPrepare(parameters.get("total_fee"));
	String point_fee	    		= Bean.getDecodeParamPrepare(parameters.get("point_fee"));
	String share_fee	    		= Bean.getDecodeParamPrepare(parameters.get("share_fee"));
	
	String need_membership_fee		= Bean.getDecodeParamPrepare(parameters.get("need_membership_fee"));
	String membership_fee    		= Bean.getDecodeParamPrepare(parameters.get("membership_fee"));
	
	String point_fee_margin			= Bean.getDecodeParamPrepare(parameters.get("point_fee_margin"));
	String share_fee_margin			= Bean.getDecodeParamPrepare(parameters.get("share_fee_margin"));
	String membership_fee_margin	= Bean.getDecodeParamPrepare(parameters.get("membership_fee_margin"));
	String mtf_margin				= Bean.getDecodeParamPrepare(parameters.get("mtf_margin"));
	String change_margin			= Bean.getDecodeParamPrepare(parameters.get("change_margin"));
	String total_margin				= Bean.getDecodeParamPrepare(parameters.get("total_margin"));
	String opr_sum					= Bean.getDecodeParamPrepare(parameters.get("opr_sum"));
	String sum_share_fee_need		= Bean.getDecodeParamPrepare(parameters.get("sum_share_fee_need"));
	
	String need_tp_subscribe		= Bean.getDecodeParamPrepare(parameters.get("need_tp_subscribe"));
	String need_tp_admin_confirm	= Bean.getDecodeParamPrepare(parameters.get("need_tp_admin_confirm"));
	String need_tp_entrance_fee		= Bean.getDecodeParamPrepare(parameters.get("need_tp_entrance_fee"));
	String tp_entrance_fee			= Bean.getDecodeParamPrepare(parameters.get("tp_entrance_fee"));
	String need_tp_membership_fee	= Bean.getDecodeParamPrepare(parameters.get("need_tp_membership_fee"));
	String tp_membership_fee   		= Bean.getDecodeParamPrepare(parameters.get("tp_membership_fee"));
	String tp_membership_last_date	= Bean.getDecodeParamPrepare(parameters.get("tp_membership_last_date"));
	String tp_fee   				= Bean.getDecodeParamPrepare(parameters.get("tp_fee"));
	
	String feeName					= Bean.webposXML.getfieldTransl("fee_sum", false);
	
	if ("MEMBERSHIP_FEE".equalsIgnoreCase(replenish_type)) {
		feeName = Bean.webposXML.getfieldTransl("cheque_entrance_fee", false);
	}
	
	String pay_type						= Bean.getDecodeParamPrepare(parameters.get("pay_type"));
	String bank_trn						= Bean.getDecodeParamPrepare(parameters.get("bank_trn"));
	
	String entered_sum					= Bean.getDecodeParamPrepare(parameters.get("entered_sum"));
	String sum_change					= Bean.getDecodeParamPrepare(parameters.get("sum_change"));
	String change_to_share_account		= Bean.getDecodeParamPrepare(parameters.get("change_to_share_account"));
	String id_target_prg 				= Bean.getDecodeParamPrepare(parameters.get("id_target_prg"));
	String id_target_prg_place			= Bean.getDecodeParamPrepare(parameters.get("id_target_prg_place"));

	String can_use_share_account		= Bean.getDecodeParamPrepare(parameters.get("can_use_share_account"));
	String input_sum                	= "";
	String input_sum_initial        	= "";
	String calc_point_total         	= "";
	String calc_point_shareholder   	= "";
	String opr_sum_initial          	= "";
	String sum_put_to_share_account     = "";
	String sum_get_from_share_account   = "";
	String sum_get_point                = "";
	String id_nat_prs					= Bean.getDecodeParamPrepare(parameters.get("id_nat_prs"));
	String nc_term                  	= Bean.loginTerm.getNextDocumentNumber();
	String pay_description          	= Bean.getDecodeParamPrepare(parameters.get("pay_description"));
	
	String feeKindName = "";
	if ("POINT_FEE".equalsIgnoreCase(replenish_type)) {
		feeKindName = Bean.webposXML.getfieldTransl("fee_kind_point", false);
	} else if ("SHARE_FEE".equalsIgnoreCase(replenish_type)) {
		feeKindName = Bean.webposXML.getfieldTransl("fee_kind_share", false);
	} else if ("MEMBERSHIP_FEE".equalsIgnoreCase(replenish_type)) {
		feeKindName = Bean.webposXML.getfieldTransl("fee_kind_membership", false);
	} else if ("MEMBERSHIP_TARGET_FEE".equalsIgnoreCase(replenish_type)) {
		feeKindName = Bean.webposXML.getfieldTransl("fee_kind_admission_short", false);
	}
	
	
	if (process.equalsIgnoreCase("no")) {
		
		if (action.equalsIgnoreCase("replenish")) {
			

			ArrayList<String> pParam = new ArrayList<String>();
			
			pParam.add(id_term);
			pParam.add(cd_card1);
			pParam.add(replenish_type);
			pParam.add(total_fee);
			pParam.add(share_fee);
			pParam.add(share_fee_margin);
			pParam.add(membership_fee);
			pParam.add(membership_fee_margin);
			pParam.add(tp_entrance_fee);
			pParam.add(tp_membership_fee);
			pParam.add(tp_fee);
			
			String[] results = new String[7];
			
			results 						= Bean.executeFunction("PACK$WEBPOS_UI.replenish_check_sum", pParam, results.length);
			String resultInt 				= results[0];
			if (!Bean.C_SQL_EXCEPTION.equalsIgnoreCase(resultInt)) {
				id_card_status				= results[1];
				share_fee_margin			= results[2];
				membership_fee_margin		= results[3];
				mtf_margin					= results[4];
				opr_sum						= results[5];
			}
	 		String resultMessage 			= results[6];
	 		
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
	    	String marginText = "";
	    	
	    	if ("CASH".equalsIgnoreCase(pay_type)) {
	    		marginText = Bean.getMarginDescription(idClub, idDealer, "CASH_CHANGE");
	    	}
	    	%>
			<script type="text/javascript">
			//alert('<%=parameters%>');
			function validateReplanishCard(){
				var returnValue = null;
				var formAll = new Array (
				);
				var formParam = new Array (
					new Array ('cd_card1', 'card', 1),
					new Array ('opr_sum', 'oper_sum', 1),
					new Array ('pay_type', 'varchar2', 1)
				);
				var formPayBankCard = new Array (
					new Array ('bank_trn', 'varchar2', 1)
				);
		
				formAll = formParam;
				<% if ("BANK_CARD".equalsIgnoreCase(pay_type)) { %>
				formAll = formAll.concat(formPayBankCard);
				<% } %>
				//alert(formAll);
				returnValue = validateForm(formAll, 'updateForm5');
				<% if ("CASH".equalsIgnoreCase(pay_type)) { %>
				if (returnValue) {
					returnValue = validateChange();
				}
				<% } %>
				return returnValue;
			}
			</script>
				<%if (Bean.C_SUCCESS_RESULT.equalsIgnoreCase(resultInt)) { %>
				<h1><%=feeKindName %><%=Bean.getHelpButton("replenish", "div_action_big") %></h1>
				<% } else { %>
				<h1 class="error"><%=feeKindName %>: <%=Bean.webposXML.getfieldTransl("operation_error", false) %><%=Bean.getHelpButton("replenish", "div_action_big") %></h1>
				<% } %>
	<% if (tabCount>1) { %>
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
	<% } %>
				<form name="updateForm5" id="updateForm5" accept-charset="UTF-8" method="POST">
					<input type="hidden" name="type" value="card">
					<input type="hidden" name="process" value="yes">
					<input type="hidden" name="action" value="replenish">
					<input type="hidden" name="id_term" value="<%=id_term %>">
					<input type="hidden" name="cd_card1" id="cd_card1" value="<%=cd_card1 %>">
					<input type="hidden" name="pay_type" id="pay_type" value="<%=pay_type %>">
					<input type="hidden" name="share_fee" id="share_fee" value="<%=share_fee %>">
					<input type="hidden" name="need_tp_subscribe" id="need_tp_subscribe" value="<%=need_tp_subscribe %>">
					<input type="hidden" name="need_tp_admin_confirm" id="need_tp_admin_confirm" value="<%=need_tp_admin_confirm %>">
					<input type="hidden" name="need_tp_entrance_fee" id="need_tp_entrance_fee" value="<%=need_tp_entrance_fee %>">
					<input type="hidden" name="tp_entrance_fee" id="tp_entrance_fee" value="<%=tp_entrance_fee %>">
					<input type="hidden" name="need_membership_fee" id="need_membership_fee" value="<%=need_membership_fee %>">
					<input type="hidden" name="membership_fee" id="membership_fee" value="<%=membership_fee %>">
					<input type="hidden" name="need_tp_membership_fee" id="need_tp_membership_fee" value="<%=need_tp_membership_fee %>">
					<input type="hidden" name="tp_membership_fee" id="tp_membership_fee" value="<%=tp_membership_fee %>">
					<input type="hidden" name="tp_fee" id="tp_fee" value="<%=tp_fee %>">
					<input type="hidden" name="replenish_type" value="<%=replenish_type %>">
					<input type="hidden" name="replenish_kind" value="<%=replenish_kind %>">
					<input type="hidden" name="id_card_status" value="<%=id_card_status %>">
					<input type="hidden" name="replenish_currency" value="<%=Bean.loginTerm.getValue("CD_TERM_CURRENCY") %>">
					<input type="hidden" name="opr_sum" id="opr_sum" value="<%=opr_sum %>">
					<input type="hidden" name="id_target_prg" id="id_target_prg" value="<%=id_target_prg %>">
					<input type="hidden" name="membership_month_sum" value="<%=membership_month_sum %>">
					<input type="hidden" name="share_fee_margin" value="<%=share_fee_margin %>">
					<input type="hidden" name="membership_fee_margin" value="<%=membership_fee_margin %>">
					<input type="hidden" name="mtf_margin" value="<%=mtf_margin %>">
					<input type="hidden" name="total_fee" value="<%=opr_sum %>">
					<table class="action_table">
						
						<tr><td <%=Bean.getDetailTableFirstColumnWidth() %>><%=Bean.webposXML.getfieldTransl("card", false) %></td><td colspan="2"><input type="text" name="cd_card1_hide" id="cd_card1_hide" size="20" value="<%=Bean.hideCdCard1(cd_card1) %>" readonly="readonly" class="inputfield_finish_blue"></td></tr>
						<% if ("MEMBERSHIP_TARGET_FEE".equalsIgnoreCase(replenish_type)) { 
							wpTargetPrgObject prg 		= new wpTargetPrgObject(id_target_prg);
							String prgName 				= prg.getValue("NAME_TARGET_PRG");
							String prgPayPeriod 		= prg.getValue("CD_TARGET_PRG_PAY_PERIOD");
							String prgPayPeriodName    	= prg.getValue("NAME_TARGET_PRG_PAY_PERIOD");
							String prgPayAmount    		= prg.getValue("PAY_AMOUNT_FRMT");
							String prgPayAmountFull   	= prg.getValue("PAY_AMOUNT_FULL_FRMT");
							String prgMinPayAmount 		= prg.getValue("MIN_PAY_AMOUNT");
							String prgMinPayAmountFrmt 	= prg.getValue("MIN_PAY_AMOUNT_FRMT");
							String prgSNameCurrency     = prg.getValue("SNAME_CURRENCY");
							
							%>
							<tr><td <%=Bean.getDetailTableFirstColumnWidth() %>><%=Bean.webposXML.getfieldTransl("target_prg", false) %></td><td colspan="2"><input type="text" name="name_target_prg" id="name_target_prg" size="20" value="<%=prgName %>" readonly="readonly" class="inputfield_finish_green"></td></tr>
							<% if (!"IRREGULAR".equalsIgnoreCase(prgPayPeriod)) { %>
							<tr>
								<td><%=Bean.webposXML.getfieldTransl("target_prg_pay_frequency", false) %></td>
								<td colspan="2">
									<input type="text" name="pay_period_frmt" id="pay_period_frmt" size="20" value="<%=prgPayAmountFull %>" readonly="readonly" class="inputfield_finish_green">
									<input type="hidden" name="pay_period" id="pay_period" value="<%=prgPayAmount %>">
								</td>
							</tr>
							<% } else { %>
							<tr><td><%=Bean.webposXML.getfieldTransl("target_prg_pay_period", false) %></td><td colspan="2"><input type="text" name="target_prg_pay_period" id="target_prg_pay_period" size="20" value="<%=prgPayPeriodName %>" readonly="readonly" class="inputfield_finish_red"></td></tr>
							<% } %>
							<% if (!Bean.isEmpty(prgMinPayAmount)) { %>
							<tr>
								<td><%=Bean.clubfundXML.getfieldTransl("min_pay_amount", false) %></td>
								<td colspan="2">
									<input type="text" name="min_pay_amount_frmt" id="min_pay_amount_frmt" size="20" value="<%=prgMinPayAmountFrmt %> <%=prgSNameCurrency %>" readonly="readonly" class="inputfield_finish_green">
								</td>
							</tr>
							<% } %>
							<% if (!Bean.isEmpty(id_target_prg_place)) { %>
							<tr>
								<td><%=Bean.webposXML.getfieldTransl("target_prg_place", false) %></td>
								<td colspan="2">
									<input type="hidden" name="id_target_prg_place" id="id_target_prg_place" value="<%=id_target_prg_place %>">
									<input type="text" name="name_target_prg_place" id="name_target_prg_place" size="20" value="<%=Bean.getTargetProgramPlaceName(id_target_prg_place) %>" readonly="readonly" class="inputfield_finish_blue">
								</td>
							</tr>
							<% } %>
						<% } %>
						<% if (!"SHARE_FEE".equalsIgnoreCase(replenish_kind)) { %>
							<% if (!Bean.isEmpty(share_fee)) { %>
							<tr><td><%=Bean.webposXML.getfieldTransl("share_fee_sum", false) %></td><td colspan="2"><input type="text" name="share_fee_txt" id="share_fee_txt" size="20" value="<%=share_fee %> <%=termCurrency %>" class="inputfield_finish_blue" maxlength="15"></td></tr>
							<% } %>
						<% } %>
						<% if (!"SHARE_FEE".equalsIgnoreCase(replenish_kind)) { %>
							<% if (!Bean.isEmpty(share_fee_margin)) { %>
							<tr><td><%=Bean.webposXML.getfieldTransl("share_fee_dealer_margin", false) %></td><td colspan="2"><input type="text" name="share_fee_margin_txt" id="share_fee_margin_txt" size="20" value="<%=share_fee_margin %> <%=termCurrency %>" class="inputfield_finish_gray" maxlength="15"></td></tr>
							<% } %>
						<% } %>
						<% if ("Y".equalsIgnoreCase(need_tp_subscribe)) { %>
							<tr><td colspan="3">
								<input type="checkbox" name="need_tp_subscribe" id="need_tp_subscribe" disabled="disabled" CHECKED>
								<label class="checbox_label" for="need_tp_subscribe"><%=Bean.webposXML.getfieldTransl("title_need_tp_subscribe", false) %></label>
							</tr>
						<% } %>
						<% if (!Bean.isEmpty(membership_fee)) { %>
						<tr><td class="another_fee"><%=Bean.webposXML.getfieldTransl("membership_fee_sum", false) %></td><td colspan="2" class="another_fee"><input type="text" name="membership_fee_txt" id="membership_fee_txt" size="20" value="<%=membership_fee %> <%=termCurrency %>" class="inputfield_finish_blue" maxlength="15"></td></tr>
						<% } %>
						<% if (!Bean.isEmpty(membership_fee_margin)) { %>
						<tr><td class="another_fee"><%=Bean.webposXML.getfieldTransl("membership_fee_dealer_margin", false) %></td><td colspan="2" class="another_fee"><input type="text" name="membership_fee_margin_txt" id="membership_fee_margin_txt" size="20" value="<%=membership_fee_margin %> <%=termCurrency %>" class="inputfield_finish_gray" maxlength="15"></td></tr>
						<% } %>
						<% if (!Bean.isEmpty(tp_entrance_fee)) { %>
						<tr><td><%=Bean.webposXML.getfieldTransl("cheque_tp_entrance_fee", false) %></td><td colspan="2"><input type="text" name="tp_entrance_fee_txt" id="tp_entrance_fee_txt" size="20" value="<%=tp_entrance_fee %> <%=termCurrency %>" class="inputfield_finish_blue" maxlength="15"></td></tr>
						<% } %>
						<% if (!Bean.isEmpty(tp_membership_fee)) { %>
						<tr><td><%=Bean.webposXML.getfieldTransl("cheque_tp_membership_fee", false) %></td><td colspan="2"><input type="text" name="tp_membership_fee_txt" id="tp_membership_fee_txt" size="20" value="<%=tp_membership_fee %> <%=termCurrency %>" class="inputfield_finish_blue" maxlength="15"></td></tr>
						<% } %>
						<% if (!Bean.isEmpty(tp_fee)) { %>
						<tr><td><%=Bean.webposXML.getfieldTransl("cheque_mtf", false) %></td><td colspan="2"><input type="text" name="tp_fee_txt" id="tp_fee_txt" size="20" value="<%=tp_fee %> <%=termCurrency %>" class="inputfield_finish_blue" maxlength="15"></td></tr>
						<% } %>
						<% if (!Bean.isEmpty(mtf_margin)) { %>
						<tr><td><%=Bean.webposXML.getfieldTransl("mtf_dealer_margin", false) %></td><td colspan="2"><input type="text" name="mtf_margin_txt" id="mtf_margin_txt" size="20" value="<%=mtf_margin %> <%=termCurrency %>" class="inputfield_finish_gray" maxlength="15"></td></tr>
						<% } %>
						<% 	String throughShafeFeeClass = ""; 
							String throughShafeFeeClassFull = "";
						%>
						<% if ("SHARE_FEE".equalsIgnoreCase(replenish_kind)) { 
							throughShafeFeeClass = "through_share_fee";
							throughShafeFeeClassFull = "class=\"through_share_fee\"";
						    %>
							<tr><td><%=Bean.webposXML.getfieldTransl("goods_pay_way", false) %></td><td><input type="text" name="pay_type_txt" id="pay_type_txt" size="20" value="<%=Bean.webposXML.getfieldTransl("goods_pay_points", false) %>" readonly class="inputfield_finish_green"></td></tr>
							<tr><td colspan="2" class="top_line_gray line_dashed <%=throughShafeFeeClass %>" style="color:green"><%=Bean.webposXML.getfieldTransl("title_fees_through_share_fee", false) %></td></tr>
							<% if (!Bean.isEmpty(opr_sum)) { %>
							<tr><td class="result_desc <%=throughShafeFeeClass %>"><%=Bean.webposXML.getfieldTransl("share_fee_sum", false) %></td><td colspan="2" <%=throughShafeFeeClassFull %>><input type="text" name="share_fee_txt" id="share_fee_txt" size="20" value="<%=share_fee %> <%=termCurrency %>" class="inputfield_finish_red" maxlength="15"></td></tr>
							<% } %>
							<% if (!Bean.isEmpty(share_fee_margin)) { %>
							<tr><td <%=throughShafeFeeClassFull %>><%=Bean.webposXML.getfieldTransl("share_fee_dealer_margin", false) %></td><td colspan="2" <%=throughShafeFeeClassFull %>><input type="text" name="share_fee_margin_txt" id="share_fee_margin_txt" size="20" value="<%=share_fee_margin %> <%=termCurrency %>" class="inputfield_finish_gray" maxlength="15"></td></tr>
							<% } %>
						<% } else { %>
							<% if (!Bean.isEmpty(opr_sum)) { %>
							<tr><td class="result_desc top_line_gray line_dashed <%=throughShafeFeeClass %>"><%=Bean.webposXML.getfieldTransl("pay_total", false) %></td><td colspan="2" class="top_line_gray line_dashed <%=throughShafeFeeClass %>"><input type="text" name="opr_sum_txt" id="opr_sum_txt" size="20" value="<%=opr_sum %> <%=termCurrency %>" class="inputfield_finish_red" maxlength="15"></td></tr>
							<% } %>
						<% } %>
					
			  			<tr>
							<td <%=throughShafeFeeClassFull %>><%=Bean.webposXML.getfieldTransl("goods_pay_way", false) %></td>
							<td <%=throughShafeFeeClassFull %>>
							<% if ("CASH".equalsIgnoreCase(pay_type)) { %>
								<input type="text" name="pay_type_txt" id="pay_type_txt" size="20" value="<%=Bean.webposXML.getfieldTransl("goods_pay_cash", false) %>" readonly class="inputfield_finish_green">
								<% } else if ("BANK_CARD".equalsIgnoreCase(pay_type)) {  %>
								<input type="text" name="pay_type_txt" id="pay_type_txt" size="20" value="<%=Bean.webposXML.getfieldTransl("goods_pay_card", false) %>" readonly class="inputfield_finish_green">
								<% } else if ("SMPU_CARD".equalsIgnoreCase(pay_type)) {  %>
								<input type="text" name="pay_type_txt" id="pay_type_txt" size="20" value="<%=Bean.webposXML.getfieldTransl("goods_pay_points", false) %>" readonly class="inputfield_finish_green">
								<% } else if ("INVOICE".equalsIgnoreCase(pay_type)) {  %>
								<input type="text" name="pay_type_txt" id="pay_type_txt" size="20" value="<%=Bean.webposXML.getfieldTransl("goods_pay_invoice", false) %>" readonly class="inputfield_finish_green">
								<% } %>
							</td>
						</tr>


						<%if (Bean.C_SUCCESS_RESULT.equalsIgnoreCase(resultInt)) { %>	
							<% if ("CASH".equalsIgnoreCase(pay_type)) { %>
				  			<tr>
								<td <%=throughShafeFeeClassFull %>><%=Bean.webposXML.getfieldTransl("entered_sum", false) %></td>
								<td <%=throughShafeFeeClassFull %>>
									<input type="text" name="entered_sum" id="entered_sum" size="15" value="<%=entered_sum %>" class="inputfield" maxlength="20"  onchange="calcChangeExtend('<%=("SHARE_FEE".equalsIgnoreCase(replenish_kind))?"share_fee":"opr_sum" %>');" title="<%=Bean.webposXML.getfieldTransl("entered_sum_title", false) %>"><input type="text" name="sname_term_currency" size="5" value="<%=termCurrency %>" readonly class="inputfield-ro">
								</td>
							</tr>
				  			<tr>
								<td <%=throughShafeFeeClassFull %>><%=Bean.webposXML.getfieldTransl("sum_change", false) %></td>
								<td <%=throughShafeFeeClassFull %>><input type="text" name="sum_change" id="sum_change" size="15" value="<%=sum_change %>" class="inputfield-ro" readonly maxlength="20" title="<%=Bean.webposXML.getfieldTransl("sum_change_title", false) %>"><input type="text" name="sname_term_currency" size="5" value="<%=termCurrency %>" readonly class="inputfield-ro"><input type="hidden" name="change_calc_error" id="change_calc_error" value="N">
									<br>
									<input type="checkbox" name="change_to_share_account" id="change_to_share_account" <% if ("Y".equalsIgnoreCase(change_to_share_account)) { %> CHECKED <% } %> disabled="disabled" class="inputfield-ro" title="<%=Bean.webposXML.getfieldTransl("change_to_share_account_title", false) %>">
									<label class="checbox_label" for="change_to_share_account"><%=Bean.webposXML.getfieldTransl("change_to_share_account", false) %></label>
								</td>
							</tr>
							<% } else if ("BANK_CARD".equalsIgnoreCase(pay_type)) {  %>
				  			<tr>
								<td <%=throughShafeFeeClassFull %>><%=Bean.webposXML.getfieldTransl("bank_trn", true) %></td>
								<td <%=throughShafeFeeClassFull %>>
									<input type="text" name="bank_trn" id="bank_trn" size="20" value="<%=bank_trn %>" class="inputfield">
								</td>
							</tr>
							<% } %>
							<tr><td colspan="2" class="left">&nbsp;</td></tr>
							<tr><td colspan="2"  class="center">
								<%=Bean.getSubmitButtonAjax("action/replenishupdate.jsp", "button_further", "updateForm5", "div_action_big", "validateReplanishCard()") %>
								<% if ("MEMBERSHIP_TARGET_FEE".equalsIgnoreCase(replenish_type)) { %>
								<%=Bean.getSubmitButtonAjax("action/replenishupdate.jsp", "button_back", "formBack", "div_action_big") %>
								<% } else { %>
								<%=Bean.getSubmitButtonAjax("action/replenish.jsp", "button_back", "formBack", "div_main") %>
								<% } %>
							</td></tr>

							<% if (!Bean.isEmpty(resultMessage)) { %>
							<tr><td colspan="2">
								<div id=div_hints>
									<i><b><%=Bean.webposXML.getfieldTransl("title_note", false) %>
									</b><br><%=resultMessage %>
									<% if (!Bean.isEmpty(marginText)) { %>
									<br><%=marginText %>
									<% } %>
									</i>
							</div>
							</td></tr>
							<% } else { %>
								<% if (!Bean.isEmpty(marginText)) { %>
								<tr><td colspan="2">
									<div id=div_hints>
										<i><b><%=Bean.webposXML.getfieldTransl("title_note", false) %><br>
										</b><%=marginText %>
										</i>
								</div>
								</td></tr>
								<% } %>
							<% } %>
						<% } else { %>
							<tr><td colspan="2" class="left">&nbsp;</td></tr>
							<tr><td colspan="2"  class="center">
								<% if ("MEMBERSHIP_TARGET_FEE".equalsIgnoreCase(replenish_type)) { %>
								<%=Bean.getSubmitButtonAjax("action/replenishupdate.jsp", "button_back", "formBack", "div_action_big") %>
								<% } else { %>
								<%=Bean.getSubmitButtonAjax("action/replenish.jsp", "button_back", "formBack", "div_main") %>
								<% } %>
							</td></tr>
							 <% if (!Bean.isEmpty(resultMessage)) { %>
							<tr><td colspan="2">
								<tr><td colspan="2"><span id="error_title"><%=Bean.webposXML.getfieldTransl("operation_error", false) %></span></td></tr>
								<tr><td colspan="2"><span id="error_description"><%=resultMessage %></span></td></tr>
							</div>
							</td></tr>
							<% } %>
						<% } %>
						</table>
					</form>
					<form name="formBack" id="formBack" accept-charset="UTF-8" method="POST">
						<% if ("MEMBERSHIP_TARGET_FEE".equalsIgnoreCase(replenish_type)) { %>
							<input type="hidden" name="type" value="card">
							<input type="hidden" name="process" value="no">
							<input type="hidden" name="action" value="select_program">
						<% } else { %>
							<input type="hidden" name="action" value="check_card">
						<% } %>
						<input type="hidden" name="id_term" value="<%=id_term %>">
						<input type="hidden" name="cd_card1" id="cd_card1" value="<%=cd_card1 %>">
						<input type="hidden" name="replenish_type" value="<%=replenish_type %>">
						<input type="hidden" name="id_card_status" value="<%=id_card_status %>">
						<input type="hidden" name="replenish_currency" value="<%=Bean.loginTerm.getValue("CD_TERM_CURRENCY") %>">
						<input type="hidden" name="id_target_prg" id="id_target_prg" value="<%=id_target_prg %>">
						<input type="hidden" name="id_target_prg_place" id="id_target_prg_place" value="<%=id_target_prg_place %>">
		    			<input type="hidden" name="share_fee" id="share_fee" value="<%=share_fee %>">
						<input type="hidden" name="need_tp_subscribe" id="need_tp_subscribe" value="<%=need_tp_subscribe %>">
						<input type="hidden" name="need_tp_admin_confirm" id="need_tp_admin_confirm" value="<%=need_tp_admin_confirm %>">
						<input type="hidden" name="need_tp_entrance_fee" id="need_tp_entrance_fee" value="<%=need_tp_entrance_fee %>">
						<input type="hidden" name="tp_entrance_fee" id="tp_entrance_fee" value="<%=tp_entrance_fee %>">
						<input type="hidden" name="need_membership_fee" id="need_membership_fee" value="<%=need_membership_fee %>">
						<input type="hidden" name="membership_fee" id="membership_fee" value="<%=membership_fee %>">
						<input type="hidden" name="need_tp_membership_fee" id="need_tp_membership_fee" value="<%=need_tp_membership_fee %>">
						<input type="hidden" name="tp_membership_fee" id="tp_membership_fee" value="<%=tp_membership_fee %>">
						<input type="hidden" name="tp_fee" id="tp_fee" value="<%=tp_fee %>">
					
					</form>
					<form name="updateForm6" id="updateForm6" accept-charset="UTF-8" method="POST">
					</form>
		<%
		

	 		
		} else if (action.equalsIgnoreCase("select_program")) {
			
			replenish_type = "MEMBERSHIP_TARGET_FEE";
			feeKindName = Bean.webposXML.getfieldTransl("fee_kind_admission_short", false);
			
			wpClubCardObject card 		= new wpClubCardObject(cd_card1);
			
			wpNatPrsTargetPrgObject prg = new wpNatPrsTargetPrgObject(id_target_prg, card.getValue("ID_NAT_PRS"));
			
			ArrayList<String> pParam = new ArrayList<String>();
			
			pParam.add(id_term);
			pParam.add(cd_card1);
			pParam.add(id_target_prg);
			pParam.add(Bean.getDateFormat());
			
			String[] results = new String[17];
			
			results 						= Bean.executeFunction("PACK$WEBPOS_UI.replenish_check_target_prg", pParam, results.length);
			String resultInt 				= results[0];
			if (!Bean.C_SQL_EXCEPTION.equalsIgnoreCase(resultInt)) {
				id_card_status					= results[1];
			    need_membership_fee 			= results[2];
				membership_month_sum  	  		= results[3];
				membership_last_date			= results[4];
				membership_nopay_month          = results[5];
				membership_need_pay_sum			= results[6];
				membership_max_pay_month        = results[7];
				//membership_fee_margin			= results[8];
			    need_tp_subscribe 				= results[8];
			    need_tp_admin_confirm 			= results[9];
			    need_tp_entrance_fee 			= results[10];
			    tp_entrance_fee 				= results[11];
			    need_tp_membership_fee          = results[12];
			    tp_membership_fee           	= results[13];
			    tp_membership_last_date         = results[14];
				total_fee        				= results[15];
			}
	 		String resultMessage 			= results[16];
	 		
	 		//need_tp_membership_fee = "N";
	 		//need_tp_entrance_fee = "N";
	 		//need_tp_membership_fee = "N";
	 		
	 		boolean canPay = "N".equalsIgnoreCase(need_tp_subscribe) &&
	 			"N".equalsIgnoreCase(need_tp_admin_confirm) /* &&
	 			"N".equalsIgnoreCase(need_entrance_fee) &&
	 			"N".equalsIgnoreCase(need_membership_fee) &&
	 			"N".equalsIgnoreCase(need_tp_membership_fee)*/;
	 		
	 		boolean canSubscribeOnly = "Y".equalsIgnoreCase(need_tp_subscribe) &&
	 			"N".equalsIgnoreCase(need_tp_admin_confirm) &&
	 			"N".equalsIgnoreCase(need_tp_entrance_fee) &&
	 			"N".equalsIgnoreCase(need_tp_membership_fee) &&
	 			"N".equalsIgnoreCase(need_tp_membership_fee);
	 		
	 		boolean canManyPay = "Y".equalsIgnoreCase(need_tp_entrance_fee) ||
	 			"Y".equalsIgnoreCase(need_tp_membership_fee) ||
	 			"Y".equalsIgnoreCase(need_tp_membership_fee);
	 		
	 		%>


			<script>
			function validateData(){
				var returnValue = null;
				var formParam = new Array (
					new Array ('cd_card1', 'card', 1),
					<% if (canManyPay) { %>
					new Array ('tp_fee', 'oper_sum_zero', 1)
					<% } else { %>
					new Array ('tp_fee', 'oper_sum', 1)
					<% } %>
				);

				returnValue = validateFormForID(formParam, 'updateForm5');	
				return returnValue;
			}

			function calcMTFFeeSum() {
				var returnValue = false;
				var membership_fee_sum = 0;
				//var membership_fee_margin_sum = 0;
				var tp_entrance_fee_sum = 0;
				var tp_membership_fee_sum = 0;
				var pay_sum = 0;
				try {
					membership_fee_element = document.getElementById('membership_fee');
					membership_fee_sum = membership_fee_element.value.replace(",",".");
				} catch(err) {}
				//try {
				//	membership_fee_margin_element = document.getElementById('membership_fee_margin');
				//	membership_fee_margin_sum = membership_fee_margin_element.value.replace(",",".");
				//} catch(err) {}
				try {
					tp_entrance_fee_element = document.getElementById('tp_entrance_fee');
					tp_entrance_fee_sum = tp_entrance_fee_element.value.replace(",",".");
				} catch(err) {}
				try {
					tp_membership_fee_element = document.getElementById('tp_membership_fee');
					tp_membership_fee_sum = tp_membership_fee_element.value.replace(",",".");
				} catch(err) {}
				try {
					tp_fee_element = document.getElementById('tp_fee');
					tp_fee_sum = tp_fee_element.value.replace(",",".");
					try {
						if (tp_fee_element.value!='') {
							tp_fee_element.value = ((+tp_fee_sum).toFixed(2)).replace(".",",");
						}
					} catch(err) {
						tp_fee_element.value = tp_fee_sum;
					}
				} catch(err) {}
				
				try {
					total_element = document.getElementById('total_fee');
					totalSum = (+membership_fee_sum + /*+membership_fee_margin_sum +*/ +tp_entrance_fee_sum + +tp_membership_fee_sum + +tp_fee_sum).toFixed(2);
					if (isNaN(totalSum)) {
						total_element.value = 'Ошибка';
					} else {
						total_element.value = totalSum.replace(".",",");
					}
				} catch(err) {
					//alert(err);
					return false;
				}
				return true;
			}
			calcMTFFeeSum();
		   	</script>
			<h1><%=feeKindName %><%=Bean.getHelpButton("replenish", "div_action_big") %></h1>
	<% if (tabCount>1) { %>
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
	<% } %>
			<form name="updateForm5" id="updateForm5" accept-charset="UTF-8" method="POST">
				<input type="hidden" name="type" value="card">
				<input type="hidden" name="action" value="replenish">
				<input type="hidden" name="id_term" value="<%=id_term %>">
				<input type="hidden" name="replenish_type" value="<%=replenish_type %>">
				<input type="hidden" name="replenish_currency" value="<%=Bean.loginTerm.getValue("CD_TERM_CURRENCY") %>">
				<input type="hidden" name="id_target_prg" value="<%=id_target_prg %>">
				<input type="hidden" name="need_tp_subscribe" id="need_tp_subscribe" value="<%=need_tp_subscribe %>">
				<input type="hidden" name="need_tp_admin_confirm" id="need_tp_admin_confirm" value="<%=need_tp_admin_confirm %>">
				<input type="hidden" name="need_tp_entrance_fee" id="need_tp_entrance_fee" value="<%=need_tp_entrance_fee %>">
				<input type="hidden" name="need_tp_membership_fee" id="need_tp_membership_fee" value="<%=need_tp_membership_fee %>">
				<input type="hidden" name="cd_card1" id="cd_card1" value="<%=cd_card1 %>">
				<% if (canSubscribeOnly) { %>
                  <input type="hidden" name="process" id="process" value="yes">
				<% } %>
			
				<table class="action_table">
					<tr><td <%=Bean.getDetailTableFirstColumnWidth() %>><%=Bean.webposXML.getfieldTransl("card", false) %></td><td colspan="2"><input type="text" name="cd_card1_hide" id="cd_card1_hide" size="20" value="<%=Bean.hideCdCard1(cd_card1) %>" readonly="readonly" class="inputfield_finish_blue"></td></tr>
					<tr><td <%=Bean.getDetailTableFirstColumnWidth() %>><%=Bean.webposXML.getfieldTransl("target_prg", false) %></td><td colspan="2"><input type="text" name="name_target_prg" id="name_target_prg" size="20" value="<%=prg.getValue("NAME_TARGET_PRG") %>" readonly="readonly" class="inputfield_finish_green"></td></tr>
					<% if (!"IRREGULAR".equalsIgnoreCase(prg.getValue("CD_TARGET_PRG_PAY_PERIOD"))) { %>
					<tr>
						<td><%=Bean.webposXML.getfieldTransl("target_prg_pay_frequency", false) %></td>
						<td colspan="2">
							<input type="text" name="pay_period_frmt" id="pay_period_frmt" size="20" value="<%=prg.getValue("PAY_AMOUNT_FULL_FRMT") %>" readonly="readonly" class="inputfield_finish_green">
							<input type="hidden" name="pay_period" id="pay_period" value="<%=prg.getValue("PAY_AMOUNT_FRMT") %>">
						</td>
					</tr>
					<% } else { %>
					<tr><td><%=Bean.webposXML.getfieldTransl("target_prg_pay_period", false) %></td><td colspan="2"><input type="text" name="target_prg_pay_period" id="target_prg_pay_period" size="20" value="<%=prg.getValue("NAME_TARGET_PRG_PAY_PERIOD") %>" readonly="readonly" class="inputfield_finish_green"></td></tr>
					<% } %>
					<% if (!Bean.isEmpty(prg.getValue("MIN_PAY_AMOUNT"))) { %>
					<tr>
						<td><%=Bean.clubfundXML.getfieldTransl("min_pay_amount", false) %></td>
						<td colspan="2">
							<input type="text" name="min_pay_amount_frmt" id="min_pay_amount_frmt" size="20" value="<%=prg.getValue("MIN_PAY_AMOUNT_FRMT") %> <%=prg.getValue("SNAME_CURRENCY") %>" readonly="readonly" class="inputfield_finish_green">
						</td>
					</tr>
					<% } %>
					<% if (canPay && Bean.C_SUCCESS_RESULT.equalsIgnoreCase(resultInt)) { %>
						<% StringBuilder places = new StringBuilder(); 
							places.append(Bean.getTargetProgramPlacesOptions(id_target_prg, id_target_prg_place));
						%>
						<% if (Bean.getSelectOptionCount() > 0) { %>
							<tr>
								<td><%=Bean.webposXML.getfieldTransl("target_prg_place", false) %></td>
								<td><%=Bean.getSelectBeginHTML("id_target_prg_place", Bean.transactionXML.getfieldTransl("target_prg_place", false)) %>
								 	<%= places.toString() %>
								<%=Bean.getSelectOnChangeEndHTML() %></td>
							</tr>
						<% } %>
					<% } %>
					<% if (!Bean.C_SUCCESS_RESULT.equalsIgnoreCase(resultInt)) { %>
						<tr><td colspan="2">&nbsp;</td></tr>
						<tr><td colspan="2"><span id="error_title"><%=Bean.webposXML.getfieldTransl("operation_error", false) %></span></td></tr>
						<tr><td colspan="2"><span id="error_description"><%=resultMessage %></span></td></tr>
						<tr><td colspan="2">&nbsp;</td></tr>
						<% if (Bean.C_MEMBERSHIP_FEE_ERROR.equalsIgnoreCase(resultInt) && Bean.hasWriteMenuPermission("WEBPOS_SERVICE_REPLANISH_MEMBERSHIP_FEE")) { %>
							<tr><td colspan="2"  class="center">
								<%=Bean.getSubmitButtonAjax("action/replenish.jsp", "bring", "updateForm2", "div_main") %> 
								<%=Bean.getSubmitButtonAjax("action/replenish.jsp", "button_back", "updateFormBack", "div_main") %>
							</td></tr>
						<% } else if (Bean.C_CARD_NOT_GIVEN.equalsIgnoreCase(resultInt) && Bean.hasWriteMenuPermission("WEBPOS_SERVICE_CARD_ACTIVATION_REGISTRATION")) { %>
							<tr><td colspan="2"  class="center">
								<%=Bean.getSubmitButtonAjax("action/new_client.jsp", "button_give", "updateForm2", "div_main") %>
								<%=Bean.getSubmitButtonAjax("action/replenish.jsp", "button_back", "updateFormBack", "div_main") %>
							</td></tr>
						<% } else if (Bean.C_CARD_NOT_QUESTIONED.equalsIgnoreCase(resultInt) && Bean.hasWriteMenuPermission("WEBPOS_SERVICE_CARD_ACTIVATION_QUESTIONNAIRE")) { %>
							<tr><td colspan="2"  class="center">
								<%=Bean.getSubmitButtonAjax("action/new_client_questionnaire.jsp", "button_questionnaire", "updateForm2", "div_action_big") %>
								<%=Bean.getSubmitButtonAjax("action/replenish.jsp", "button_back", "updateFormBack", "div_main") %>
							</td></tr>
						<% } else if (Bean.C_CARD_NOT_ACTIVATED.equalsIgnoreCase(resultInt) && Bean.hasWriteMenuPermission("WEBPOS_SERVICE_CARD_ACTIVATION_ACTIVATION")) { %>
							<tr><td colspan="2"  class="center">
								<%=Bean.getSubmitButtonAjax("action/new_client_activation.jsp", "activate", "updateForm2", "div_action_big", "validateReplanishCard") %>
								<%=Bean.getSubmitButtonAjax("action/replenish.jsp", "button_back", "updateFormBack", "div_main") %>
							</td></tr>
						<% } else { %>
							<tr><td colspan="2"  class="center">
								<%=Bean.getSubmitButtonAjax("action/replenish.jsp", "button_back", "updateFormBack", "div_main") %>
							</td></tr>
						<% } %>
					<% } %>

					<% if (Bean.C_SUCCESS_RESULT.equalsIgnoreCase(resultInt)) { %>
						<% if (!Bean.isEmpty(resultMessage)) { %>
						<tr><td colspan="2" class="top_line_gray line_dashed"><span id="error_description"><%=resultMessage %></span></td></tr>
						<% } %>
							<% if ("Y".equalsIgnoreCase(need_tp_subscribe)) { %>
								<tr><td colspan="3">
									<input type="checkbox" name="need_tp_subscribe_txt" id="need_tp_subscribe_txt" disabled="disabled" CHECKED class="inputfield_finish">
									<label class="inputfield_finish checbox_label " for="need_tp_subscribe_txt"><%=Bean.webposXML.getfieldTransl("title_need_tp_subscribe", false) %></label>
								</tr>
							<% } %>
							<% if ("Y".equalsIgnoreCase(need_membership_fee)) { %>
								<% if (!(membership_last_date == null || "".equalsIgnoreCase(membership_last_date))) { %>
								<tr><td class="another_fee"><%= Bean.webposXML.getfieldTransl("membership_last_date", false) %></td><td class="another_fee"><input type="text" name="membership_last_date" id="membership_last_date" size="20" value="<%=membership_last_date %>" readonly class="inputfield_finish_green"></td></tr>
								<% } %>
								<tr><td class="another_fee"><%=Bean.webposXML.getfieldTransl("membership_fee_sum", false) %></td>
									<td colspan="2" class="another_fee">
										<input type="text" name="membership_fee" id="membership_fee" size="15" value="<%=membership_need_pay_sum %>" readonly class="inputfield-ro"><input type="text" name="sname_membership_currency" size="5" value="<%=termCurrency %>" readonly="readonly" class="inputfield-ro">
									</td>
								</tr>
							<% } %>
							<% if ("Y".equalsIgnoreCase(need_tp_entrance_fee)) { %>
								<tr><td><%=Bean.webposXML.getfieldTransl("cheque_tp_entrance_fee", false) %></td>
									<td colspan="2">
										<input type="text" name="tp_entrance_fee" id="tp_entrance_fee" size="15" value="<%=tp_entrance_fee %>" readonly class="inputfield-ro"><input type="text" name="sname_entrance_currency" size="5" value="<%=termCurrency %>" readonly="readonly" class="inputfield-ro">
									</td>
								</tr>
							<% } %>
							<% if ("Y".equalsIgnoreCase(need_tp_membership_fee)) { %>
								<tr><td><%=Bean.webposXML.getfieldTransl("cheque_tp_membership_fee", false) %></td>
									<td colspan="2">
										<input type="text" name="tp_membership_fee" id="tp_membership_fee" size="15" value="<%=tp_membership_fee %>" readonly="readonly" class="inputfield-ro"><input type="text" name="sname_tp_membership_currency" size="5" value="<%=termCurrency %>" readonly="readonly" class="inputfield-ro">
									</td>
								</tr>
							<% } %>
						
						<% if (canPay) { %>
						<tr><td><%=Bean.webposXML.getfieldTransl("cheque_mtf", true) %></td>
							<td colspan="2">
								<input type="text" name="tp_fee" id="tp_fee" size="15" value="<%=tp_fee %>" class="inputfield" onchange="calcMTFFeeSum()"><input type="text" name="sname_pay_currency" size="5" value="<%=termCurrency %>" readonly="readonly" class="inputfield-ro">
								<% if (!"IRREGULAR".equalsIgnoreCase(prg.getValue("CD_TARGET_PRG_PAY_PERIOD"))) { %>
								<br>
								<%= Bean.getTargetProgramPayValues2(prg.getValue("CD_TARGET_PRG_PAY_PERIOD"), prg.getValue("PAY_COUNT"), prg.getValue("PAY_AMOUNT_FRMT"), termCurrency, "calcMTFFeeSum()") %>
								<% } %>
							</td>
						</tr>
							<% if ("Y".equalsIgnoreCase(need_membership_fee) || 
									"Y".equalsIgnoreCase(need_tp_entrance_fee) || 
									"Y".equalsIgnoreCase(need_tp_membership_fee) || 
									"Y".equalsIgnoreCase(need_tp_membership_fee)) { %>
								<tr><td><%=Bean.webposXML.getfieldTransl("fee_sum_total", false) %></td>
									<td colspan="2">
										<input type="text" name="total_fee" id="total_fee" size="15" value="<%=total_fee %>" readonly class="inputfield-ro"><input type="text" name="sname_total_currency" size="5" value="<%=termCurrency %>" readonly="readonly" class="inputfield-ro">
									</td>
								</tr>
							<% } %>
						<% } %>
						<% if (canPay || "Y".equalsIgnoreCase(need_tp_entrance_fee) || "Y".equalsIgnoreCase(need_tp_membership_fee)) { %>	
						<tr>
							<td colspan="4"><br><%=Bean.webposXML.getfieldTransl("goods_pay_way", true) %></td>
						</tr>
						<tr>
							<td colspan="4">
								<table border="0">
									<tr>
										<%=Bean.getPayTypeImage("CASH", "WEBPOS_SERVICE_REPLANISH_MTF_PAY_CASH", "action/replenishupdate.jsp?pay_type=CASH&process=no", "div_action_big", "updateForm5", "validateData") %>
										<%=Bean.getPayTypeImage("SMPU_CARD", "WEBPOS_SERVICE_REPLANISH_MTF_PAY_BANK_CARD", "action/replenishupdate.jsp?pay_type=SMPU_CARD&process=yes", "div_action_big", "updateForm5", "validateData") %>
										<%=Bean.getPayTypeImage("BANK_CARD", "WEBPOS_SERVICE_REPLANISH_MTF_PAY_POINTS", "action/replenishupdate.jsp?pay_type=BANK_CARD&process=no", "div_action_big", "updateForm5", "validateData") %>
										<%=Bean.getPayTypeImage("INVOICE", "WEBPOS_SERVICE_REPLANISH_MTF_MAKE_INVOICE", "action/replenishupdate.jsp?pay_type=INVOICE&process=yes", "div_action_big", "updateForm5", "validateData") %>
									</tr>
								</table>
							</td>
						</tr>
						<% } %>
						<tr><td colspan="2">&nbsp;</td></tr>
						<tr><td colspan=3  class="center">
							<% if (canSubscribeOnly) { %>
								<%=Bean.getSubmitButtonAjax("action/replenishupdate.jsp", "subscribe", "updateForm5", "div_action_big") %>
							<% } %>
							<%=Bean.getSubmitButtonAjax("action/replenish.jsp", "button_back", "updateFormBack", "div_main") %>
						</td></tr>
					<% } %>
					


					<%
					String marginText = Bean.getMarginDescription(idClub, idDealer, "REC_MTF");
					%>
					<% if (Bean.isEmpty(cd_card1)) { %>
					<tr><td colspan="2">
						<div id=div_hints>
							<% if (!Bean.isEmpty(marginText)) { %>
							<i><b><%=Bean.webposXML.getfieldTransl("title_note", false) %>
							</b><br><%=marginText %></i><br>
							<% } %>
							<%=Bean.getWEBPosOnlyTestCards() %>
						</div>
					</td></tr>
					<% } else if (!Bean.isEmpty(marginText)) { %>
						<tr><td colspan="2">
						<div id=div_hints>
							<i><b><%=Bean.webposXML.getfieldTransl("title_note", false) %>
							</b><br><%=marginText %></i><br>
						</div>
					</td></tr>
					<% } %>
				</table>
			</form>
			<form name="updateForm3" id="updateForm3" accept-charset="UTF-8" method="POST">
			</form>
			<form name="updateForm6" id="updateForm6" accept-charset="UTF-8" method="POST">
			</form>
			<form name="updateFormBack" id="updateFormBack" accept-charset="UTF-8" method="POST">
				<input type="hidden" name="tab2" value="<%=tab2 %>">
				<input type="hidden" name="id_term" value="<%=id_term %>">
				<input type="hidden" name="replenish_type" value="<%=replenish_type %>">
				<input type="hidden" name="replenish_currency" value="<%=Bean.loginTerm.getValue("CD_TERM_CURRENCY") %>">
				<input type="hidden" name="id_target_prg" value="<%=id_target_prg %>">
				<% if (!Bean.isEmpty(cd_card1)) { %>
				<input type="hidden" name="cd_card1" id="cd_card1" value="<%=cd_card1 %>">
				<input type="hidden" name="action" value="check_card">
				<% } else { %>
				<% } %>
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
			<form name="updateForm2" id="updateForm2" accept-charset="UTF-8" method="POST">
				<input type="hidden" name="action" value="edit">
				<input type="hidden" name="id_term" value="<%=id_term %>">
				<input type="hidden" name="cd_card1" id="cd_card1" value="<%=cd_card1 %>">
				<input type="hidden" name="id_role" value="<%=card.getValue("ID_NAT_PRS_ROLE_CURRENT") %>">
			</form>
		<% } %>
				<%
	 		
	 		
		}  else { %> 
		<%= Bean.getUnknownActionText(action) %><% 
		}
		
		
		
		
		
		
		
		
		
		
		
		
		
		
	} else if (process.equalsIgnoreCase("yes")) {
		
   	    String resultFull 					= Bean.C_SUCCESS_RESULT;
   	    String resultMessageFull 			= "";
 		
 		String id_telgr 				= Bean.getDecodeParam(parameters.get("id_telgr"));
		String phone_mobile_confirm		= "";
		String can_send_pin_in_sms		= "";
   	    
		String
			confirm_type		= Bean.getDecodeParam(parameters.get("confirm_type")),
			confirm_code		= Bean.getDecodeParam(parameters.get("confirm_code"));
		
		String operCurrency = termCurrency;
		if ("POINT_FEE".equalsIgnoreCase(replenish_type) || "SMPU_CARD".equalsIgnoreCase(pay_type)) {
			operCurrency = Bean.webposXML.getfieldTransl("title_transfer_points_currency", false);
		}
		
		if (action.equalsIgnoreCase("replenish")) {

			boolean hasPermission = true;		
			String resultGeneral			= Bean.C_SUCCESS_RESULT;
			String resultMessageGeneral		= "";
			//System.out.println("replenish_type="+replenish_type+", pay_type="+pay_type);
			if ("POINT_FEE".equalsIgnoreCase(replenish_type)) {
			} else if ("SHARE_FEE".equalsIgnoreCase(replenish_type) || "SHARE_FEE".equalsIgnoreCase(replenish_kind)) {
				if ("CASH".equalsIgnoreCase(pay_type)) {
					if (!(Bean.hasExecuteMenuPermission("WEBPOS_SERVICE_REPLANISH_SHARE_FEE_PAY_CASH"))) {
						resultGeneral 			= Bean.C_FUNCTION_ACCESS_DENIED;
						resultMessageGeneral 	= Bean.webposXML.getfieldTransl("goods_pay_cash_forbidden", false);
						hasPermission 			= false;
					}
				} else if ("BANK_CARD".equalsIgnoreCase(pay_type)) {
					if (!(Bean.hasExecuteMenuPermission("WEBPOS_SERVICE_REPLANISH_SHARE_FEE_PAY_BANK_CARD"))) {
						resultGeneral 			= Bean.C_FUNCTION_ACCESS_DENIED;
						resultMessageGeneral 	= Bean.webposXML.getfieldTransl("goods_pay_card_forbidden", false);
						hasPermission 			= false;
					}
				} else if ("INVOICE".equalsIgnoreCase(pay_type)) {
					if (!(Bean.hasExecuteMenuPermission("WEBPOS_SERVICE_REPLANISH_SHARE_FEE_MAKE_INVOICE"))) {
						resultGeneral 			= Bean.C_FUNCTION_ACCESS_DENIED;
						resultMessageGeneral 	= Bean.webposXML.getfieldTransl("goods_pay_points_forbidden", false);
						hasPermission 			= false;
					}
				} else {
					resultGeneral 			= Bean.C_FUNCTION_ACCESS_DENIED;
					resultMessageGeneral 	= Bean.webposXML.getfieldTransl("goods_pay_unknown_title", false);
					hasPermission 			= false;
				}
			} else if ("MEMBERSHIP_FEE".equalsIgnoreCase(replenish_type)) {
				if ("CASH".equalsIgnoreCase(pay_type)) {
					if (!(Bean.hasExecuteMenuPermission("WEBPOS_SERVICE_REPLANISH_MEMBERSHIP_FEE_PAY_CASH"))) {
						resultGeneral 			= Bean.C_FUNCTION_ACCESS_DENIED;
						resultMessageGeneral 	= Bean.webposXML.getfieldTransl("goods_pay_cash_forbidden", false);
						hasPermission 			= false;
					}
				} else if ("BANK_CARD".equalsIgnoreCase(pay_type)) {
					if (!(Bean.hasExecuteMenuPermission("WEBPOS_SERVICE_REPLANISH_MEMBERSHIP_FEE_PAY_BANK_CARD"))) {
						resultGeneral 			= Bean.C_FUNCTION_ACCESS_DENIED;
						resultMessageGeneral 	= Bean.webposXML.getfieldTransl("goods_pay_card_forbidden", false);
						hasPermission 			= false;
					}
				} else if ("SMPU_CARD".equalsIgnoreCase(pay_type)) {
					if (!(Bean.hasExecuteMenuPermission("WEBPOS_SERVICE_REPLANISH_MEMBERSHIP_FEE_PAY_POINTS"))) {
						resultGeneral 			= Bean.C_FUNCTION_ACCESS_DENIED;
						resultMessageGeneral 	= Bean.webposXML.getfieldTransl("goods_pay_points_forbidden", false);
						hasPermission 			= false;
					}
				} else if ("INVOICE".equalsIgnoreCase(pay_type)) {
					if (!(Bean.hasExecuteMenuPermission("WEBPOS_SERVICE_REPLANISH_MEMBERSHIP_FEE_MAKE_INVOICE"))) {
						resultGeneral 			= Bean.C_FUNCTION_ACCESS_DENIED;
						resultMessageGeneral 	= Bean.webposXML.getfieldTransl("goods_pay_points_forbidden", false);
						hasPermission 			= false;
					}
				} else {
					resultGeneral 			= Bean.C_FUNCTION_ACCESS_DENIED;
					resultMessageGeneral 	= Bean.webposXML.getfieldTransl("goods_pay_unknown_title", false);
					hasPermission 			= false;
				}
			} else if ("MEMBERSHIP_TARGET_FEE".equalsIgnoreCase(replenish_type)) {
				if ("CASH".equalsIgnoreCase(pay_type)) {
					if (!(Bean.hasExecuteMenuPermission("WEBPOS_SERVICE_REPLANISH_MTF_PAY_CASH"))) {
						resultGeneral 			= Bean.C_FUNCTION_ACCESS_DENIED;
						resultMessageGeneral 	= Bean.webposXML.getfieldTransl("goods_pay_cash_forbidden", false);
						hasPermission 			= false;
					}
				} else if ("BANK_CARD".equalsIgnoreCase(pay_type)) {
					if (!(Bean.hasExecuteMenuPermission("WEBPOS_SERVICE_REPLANISH_MTF_PAY_BANK_CARD"))) {
						resultGeneral 			= Bean.C_FUNCTION_ACCESS_DENIED;
						resultMessageGeneral 	= Bean.webposXML.getfieldTransl("goods_pay_card_forbidden", false);
						hasPermission 			= false;
					}
				} else if ("SMPU_CARD".equalsIgnoreCase(pay_type)) {
					if (!(Bean.hasExecuteMenuPermission("WEBPOS_SERVICE_REPLANISH_MTF_PAY_POINTS"))) {
						resultGeneral 			= Bean.C_FUNCTION_ACCESS_DENIED;
						resultMessageGeneral 	= Bean.webposXML.getfieldTransl("goods_pay_points_forbidden", false);
						hasPermission 			= false;
					}
				} else if ("INVOICE".equalsIgnoreCase(pay_type)) {
					if (!(Bean.hasExecuteMenuPermission("WEBPOS_SERVICE_REPLANISH_MTF_MAKE_INVOICE"))) {
						resultGeneral 			= Bean.C_FUNCTION_ACCESS_DENIED;
						resultMessageGeneral 	= Bean.webposXML.getfieldTransl("goods_pay_points_forbidden", false);
						hasPermission 			= false;
					}
				}
			} else {
				resultGeneral 			= Bean.C_FUNCTION_ACCESS_DENIED;
				resultMessageGeneral 	= Bean.webposXML.getfieldTransl("fee_kind_unknown_title", false);
				hasPermission 			= false;
			}
			
			if (hasPermission) {
		
				ArrayList<String> pParam = new ArrayList<String>();
				
				pParam.add(id_term);
				pParam.add(cd_card1);

				pParam.add(replenish_type);
				pParam.add(replenish_kind);
				pParam.add(pay_type);
				pParam.add(total_fee);
				pParam.add(point_fee);
				pParam.add(share_fee);
				pParam.add(share_fee_margin);
				pParam.add(membership_fee);
				pParam.add(membership_fee_margin);
				pParam.add(tp_entrance_fee);
				pParam.add(tp_membership_fee);
				pParam.add(tp_fee);
				pParam.add(mtf_margin);
				pParam.add(bank_trn);
				pParam.add(entered_sum);
				pParam.add(sum_change);
				pParam.add(change_to_share_account);
				pParam.add(id_target_prg);
				pParam.add(id_target_prg_place);
				//pParam.add(pay_count);
				//pParam.add(entrance_fee);
				pParam.add(need_tp_subscribe);
				pParam.add(can_use_share_account);
				//pParam.add(pay_description);
				pParam.add(Bean.getDateFormat());
				
				String[] results = new String[20];
				
				results 					= Bean.executeFunction("PACK$WEBPOS_UI.replenish", pParam, results.length);
				resultGeneral				= results[0];
				if (!Bean.C_SQL_EXCEPTION.equalsIgnoreCase(resultGeneral)) {
					id_telgr					= results[1];
					id_card_status				= results[2];
					sum_share_fee_need			= results[3];
					opr_sum						= results[4];
					point_fee_margin			= results[5];
					share_fee_margin			= results[6];
					membership_fee_margin		= results[7];
					mtf_margin					= results[8];
					change_margin				= results[9];
					total_margin				= results[10];
					membership_last_date		= results[11];
					calc_point_total			= results[12];
					calc_point_shareholder		= results[13];
					sum_put_to_share_account	= results[14];
					sum_get_from_share_account	= results[15];
					sum_get_point				= results[16];
					phone_mobile_confirm		= results[17];
					can_send_pin_in_sms			= results[18];
				}
		 		resultMessageGeneral		= results[19];
			}
 		
	   	 	if (!(Bean.C_SUCCESS_RESULT.equalsIgnoreCase(resultGeneral))) {
				resultFull = resultGeneral;
				if (!Bean.isEmpty(resultMessageFull)) {
					resultMessageFull = resultMessageFull + "; " +resultMessageGeneral;
				} else {
					resultMessageFull = resultMessageGeneral;
				}
			}
		} else if (action.equalsIgnoreCase("oper_confirm")) {
			
			ArrayList<String> pParam = new ArrayList<String>();
			
			pParam.add(id_telgr);
			pParam.add(confirm_type);
			pParam.add(confirm_code);
			pParam.add(pay_description);
			
			String[] results = new String[4];
			
			results 					= Bean.executeFunction("PACK$WEBPOS_UI.oper_confirm", pParam, results.length);
			resultFull 					= results[0];
			if (!Bean.C_SQL_EXCEPTION.equalsIgnoreCase(resultFull)) {
				phone_mobile_confirm	= results[1];
				can_send_pin_in_sms  	= results[2];
			}
	 		resultMessageFull			= results[3];
 		} else { %> 
			<%= Bean.getUnknownActionText(action) %><% 
		}
		
	   	boolean isErrorResult = true;
	 	if (Bean.C_SUCCESS_RESULT.equalsIgnoreCase(resultFull) || 
	 			Bean.C_NEED_END_INFO.equalsIgnoreCase(resultFull) || 
				Bean.C_NEED_PIN.equalsIgnoreCase(resultFull) || 
	 			Bean.C_NEED_SMS_CONFIRMATION.equalsIgnoreCase(resultFull) || 
	 			Bean.C_NEED_ACTIVATION_CODE.equalsIgnoreCase(resultFull) || 
				Bean.C_ISNT_ENOUGH_POINTS.equalsIgnoreCase(resultFull) || 
				Bean.C_SMS_CONFIRM_CREATED.equalsIgnoreCase(resultFull)) {
	 		isErrorResult = false;
	 	}

		if (action.equalsIgnoreCase("oper_confirm") && Bean.C_SUCCESS_RESULT.equalsIgnoreCase(resultFull)) {
			
			wpChequeObject cheque = new wpChequeObject(id_telgr, Bean.getChequeSaveFormat(), Bean.loginTerm);
		%>
			<h1 class="confirm"><%=feeKindName %>: <%=Bean.webposXML.getfieldTransl("operation_confirm", false) %><%=Bean.getHelpButton("pay", "div_action_big") %></h1>
 					<table class="table_cheque"><tbody>
					<tr><td class="centerb">
						<%= cheque.getChequeAllButtonsShort(Bean.hasStornoMenuPermission()) %></td><td>
						<%=Bean.getSubmitButtonAjax("action/replenish.jsp", "button_back", "updateForm", "div_main") %><br><br>
					</td></tr>
					<%=cheque.getChequeHTML(true) %>
					</tbody></table>
			<form name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST">
    			<input type="hidden" name="cd_card1" id="cd_card1" value="<%=cd_card1 %>">
			</form>
		<% } else { %> 
			
			<script>
				function validateData(){
					var formParam = new Array (
						new Array ('confirm_code', 'number', 1)
					);
					return validateFormForID(formParam, 'updateForm');
				}
			</script>
			<% 
				wpClubCardObject card = new wpClubCardObject(cd_card1);
				id_nat_prs = card.getValue("ID_NAT_PRS");
				wpTelegramObject oper = null;
				if (!Bean.isEmpty(id_telgr)) {
					oper = new wpTelegramObject(id_telgr);
				}
				
				/*if (!Bean.isEmpty(id_telgr)) {
					oper = new wpOnlineOperationObject(id_telgr);
					id_term = oper.getValue("ID_TERM");
					nc_term = oper.getValue("NC_TERM");
					cd_card1 = oper.getValue("CD_CARD1");
					pay_type = oper.getValue("PAY_TYPE");
					replenish_type = oper.getValue("FCD_TRANS_TYPE");
					if ("REC_POINT_FEE".equalsIgnoreCase(replenish_type)) {
						replenish_type = "POINT_FEE";
					} else if ("REC_MTF".equalsIgnoreCase(replenish_type)) {
						replenish_type = "MEMBERSHIP_TARGET_FEE";
					} else if ("REC_MEMBERSHIP_FEE".equalsIgnoreCase(replenish_type)) {
						replenish_type = "MEMBERSHIP_FEE";
					} else if ("REC_SHARE_FEE".equalsIgnoreCase(replenish_type)) {
						replenish_type = "SHARE_FEE";
					}
					id_target_prg = oper.getValue("ID_TARGET_PRG");
					id_nat_prs = oper.getValue("ID_NAT_PRS");
					bank_trn = oper.getValue("BANK_TRN");
					//pay_description = oper.getValue("PAYMENT_DESCRIPTION");

					membership_last_date = oper.getValue("MEMBERSHIP_LAST_DATE_DF");
					if (!(oper.getValue("ENTRANCE_FEE_SUM") == null || "".equalsIgnoreCase(oper.getValue("ENTRANCE_FEE_SUM")) || "0".equalsIgnoreCase(oper.getValue("ENTRANCE_FEE_SUM")))) {
						entrance_fee = oper.getValue("ENTRANCE_FEE_SUM_FRMT");
					} else {
						entrance_fee = "";
					}
					if (!(oper.getValue("DEALER_MARGIN_OUT_OPR_SUM") == null || "".equalsIgnoreCase(oper.getValue("DEALER_MARGIN_OUT_OPR_SUM")) || "0".equalsIgnoreCase(oper.getValue("DEALER_MARGIN_OUT_OPR_SUM")))) {
						dealer_margin = oper.getValue("DEALER_MARGIN_OUT_OPR_SUM_FRMT");
					} else {
						dealer_margin = "";
					}
					if (!(oper.getValue("INPUT_SUM") == null || "".equalsIgnoreCase(oper.getValue("INPUT_SUM")) || "0".equalsIgnoreCase(oper.getValue("INPUT_SUM")))) {
						pay_value = oper.getValue("INPUT_SUM_FRMT");
					} else {
						pay_value = "";
					}
					opr_sum = oper.getValue("OPR_SUM_FRMT");
					sum_put_point = oper.getValue("SUM_PUT_POINT_FRMT");
				}*/
				//System.out.println("id_nat_prs="+id_nat_prs);
				

				wpNatPrsTargetPrgObject prg = null;

				String backLink				= "action/replenishupdate.jsp";
				String backDiv				= "div_action_big";	
				if (!("CASH".equalsIgnoreCase(pay_type) || "BANK_CARD".equalsIgnoreCase(pay_type)) && 
						!"MEMBERSHIP_TARGET_FEE".equalsIgnoreCase(replenish_type)) {
					backLink				= "action/replenish.jsp";
					backDiv					= "div_main";	
				}		
				if ("INVOICE".equalsIgnoreCase(pay_type) || "SMPU_CARD".equalsIgnoreCase(pay_type)) {
					input_sum_initial       = input_sum;
					opr_sum_initial			= opr_sum;
				}
				
				if ("MEMBERSHIP_TARGET_FEE".equalsIgnoreCase(replenish_type)) { 				
					prg 			= new wpNatPrsTargetPrgObject(id_target_prg, id_nat_prs);
				}
				%>

				<%if (isErrorResult) { %>
				<h1 class="error"><%=feeKindName %>: <%=Bean.webposXML.getfieldTransl("operation_error", false) %><%=Bean.getHelpButton("replenish", "div_action_big") %></h1>
				<% } else { %>
				<h1 class="confirm"><%=feeKindName %>: <%=Bean.webposXML.getfieldTransl("oper_confirmation", false) %><%=Bean.getHelpButton("pay", "div_action_big") %></h1>
				<% } %>

	<% if (tabCount>1) { %>
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
	<% } %>
			<form name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST">
				<input type="hidden" name="id_telgr" value="<%=id_telgr %>">
				<% if (Bean.C_ISNT_ENOUGH_POINTS.equalsIgnoreCase(resultFull)) { %>
	    			<input type="hidden" name="type" value="card">
			    	<input type="hidden" name="action" value="replenish">
					<input type="hidden" name="process" value="yes">
					<input type="hidden" name="share_fee" value="<%=sum_get_from_share_account %>">
					<input type="hidden" name="replenish_kind" value="GET_FROM_SHARE_ACCOUNT">
				<% } else { %>
					<% if ("INVOICE".equalsIgnoreCase(pay_type)) { %>
    				<input type="hidden" name="type" value="trans">
					<% } else { %>
    				<input type="hidden" name="type" value="card">
					<% } %>
					<% if (Bean.C_ENOUGH_MEANS.equalsIgnoreCase(resultFull)) { %>
					<input type="hidden" name="action" value="replenish">
					<input type="hidden" name="process" value="no">
					<input type="hidden" name="share_fee" id="share_fee" value="<%=sum_share_fee_need %>">
					<% } else { %>
					<input type="hidden" name="action" value="oper_confirm">
					<input type="hidden" name="process" value="yes">
					<input type="hidden" name="share_fee" id="share_fee" value="<%=share_fee %>">
					<% } %>
				<% } %>
				<% if (!Bean.C_ENOUGH_MEANS.equalsIgnoreCase(resultFull)) { %>
    			<input type="hidden" name="pay_type" value="<%=pay_type %>">
				<% } %>
    			<input type="hidden" name="replenish_type" value="<%=replenish_type %>">
				<% if (Bean.C_NEED_PIN.equalsIgnoreCase(resultFull)) { %>
    			<input type="hidden" name="confirm_type" value="PIN">
				<% } else if (Bean.C_NEED_SMS_CONFIRMATION.equalsIgnoreCase(resultFull)) { %>
    			<input type="hidden" name="confirm_type" value="SMS">
				<% } else if (Bean.C_SMS_CONFIRM_CREATED.equalsIgnoreCase(resultFull)) { %>
    			<input type="hidden" name="confirm_type" value="SMS">
				<% } else if (Bean.C_NEED_ACTIVATION_CODE.equalsIgnoreCase(resultFull)) { %>
    			<input type="hidden" name="confirm_type" value="ACTIVATION_CODE">
				<% } else if (Bean.C_NEED_END_INFO.equalsIgnoreCase(resultFull)) { %>
    			<input type="hidden" name="confirm_type" value="NONE">
				<% } %>

    			<input type="hidden" name="cd_card1" id="cd_card1" value="<%=cd_card1 %>">
				<input type="hidden" name="id_term" value="<%=id_term %>">
				<input type="hidden" name="id_target_prg" value="<%=id_target_prg %>">
				<input type="hidden" name="id_target_prg_place" value="<%=id_target_prg_place %>">
				<input type="hidden" name="id_nat_prs" value="<%=id_nat_prs %>">
				<input type="hidden" name="share_fee_margin" id="share_fee_margin" value="<%=share_fee_margin %>">
				<input type="hidden" name="need_tp_subscribe" id="need_tp_subscribe" value="<%=need_tp_subscribe %>">
				<input type="hidden" name="need_tp_admin_confirm" id="need_tp_admin_confirm" value="<%=need_tp_admin_confirm %>">
				<input type="hidden" name="need_tp_entrance_fee" id="need_tp_entrance_fee" value="<%=need_tp_entrance_fee %>">
				<input type="hidden" name="tp_entrance_fee" id="tp_entrance_fee" value="<%=tp_entrance_fee %>">
				<input type="hidden" name="need_membership_fee" id="need_membership_fee" value="<%=need_membership_fee %>">
				<input type="hidden" name="membership_fee" id="membership_fee" value="<%=membership_fee %>">
				<input type="hidden" name="membership_fee_margin" id="membership_fee_margin" value="<%=membership_fee_margin %>">
				<input type="hidden" name="need_tp_membership_fee" id="need_tp_membership_fee" value="<%=need_tp_membership_fee %>">
				<input type="hidden" name="tp_membership_fee" id="tp_membership_fee" value="<%=tp_membership_fee %>">
				<input type="hidden" name="tp_fee" id="tp_fee" value="<%=tp_fee %>">
				<input type="hidden" name="mtf_margin" id="mtf_margin" value="<%=mtf_margin %>">
				<% if (Bean.isEmpty(id_telgr)) { %>
    			<input type="hidden" name="bank_trn" value="<%=bank_trn %>">
    			<input type="hidden" name="need_tp_subscribe" value="<%=need_tp_subscribe %>">
    			<!-- <input type="hidden" name="pay_description" value="<%=pay_description %>">-->
				<% } else { %>
				<% if (!"MEMBERSHIP_TARGET_FEE".equalsIgnoreCase(replenish_type)) { %>
				<input type="hidden" name="pay_description" value="">
				<% } %>
				<% } %>

    			<table class="action_table">
				<tbody>
				<% if (!Bean.isEmpty(id_telgr)) { %>
				<!-- <tr><td <%=Bean.getDetailTableFirstColumnWidth() %>><%=Bean.webposXML.getfieldTransl("cheque_number_full", false) %></td><td><input type="text" name="doc_number" id="doc_number" size="20" value="<%=nc_term %>" readonly class="inputfield_finish"></td></tr> -->
				<% } %>
				<tr><td <%=Bean.getDetailTableFirstColumnWidth() %>><%=Bean.webposXML.getfieldTransl("card", false) %></td><td><input type="text" name="cd_card1_hide" id="cd_card1_hide" size="20" value="<%=Bean.hideCdCard1(cd_card1) %>" readonly class="inputfield_finish_blue"></td></tr>
				<% if ("MEMBERSHIP_TARGET_FEE".equalsIgnoreCase(replenish_type)) {  %>
					<tr><td><%=Bean.webposXML.getfieldTransl("target_prg", false) %></td><td colspan="2"><input type="text" name="name_target_prg" id="name_target_prg" size="20" value="<%=prg.getValue("NAME_TARGET_PRG") %>" readonly class="inputfield_finish_green"></td></tr>
					<% if (!"IRREGULAR".equalsIgnoreCase(prg.getValue("CD_TARGET_PRG_PAY_PERIOD"))) { %>
					<tr><td><%=Bean.webposXML.getfieldTransl("target_prg_pay_frequency", false) %></td><td colspan="2"><input type="text" name="pay_period_frmt" id="pay_period_frmt" size="20" value="<%=prg.getValue("PAY_AMOUNT_FULL_FRMT") %>" readonly class="inputfield_finish_red"></td></tr>
					<% } else { %>
					<tr><td><%=Bean.webposXML.getfieldTransl("target_prg_pay_period", false) %></td><td colspan="2"><input type="text" name="target_prg_pay_period" id="target_prg_pay_period" size="20" value="<%=prg.getValue("NAME_TARGET_PRG_PAY_PERIOD") %>" readonly class="inputfield_finish_red"></td></tr>
					<% } %>
					<% if (!Bean.isEmpty(prg.getValue("MIN_PAY_AMOUNT"))) { %>
					<tr>
						<td><%=Bean.clubfundXML.getfieldTransl("min_pay_amount", false) %></td>
						<td colspan="2">
							<input type="text" name="min_pay_amount_frmt" id="min_pay_amount_frmt" size="20" value="<%=prg.getValue("MIN_PAY_AMOUNT_FRMT") %> <%=prg.getValue("SNAME_CURRENCY") %>" readonly="readonly" class="inputfield_finish_green">
						</td>
					</tr>
					<% } %>
					<% if (!Bean.isEmpty(id_target_prg_place)) { %>
					<tr>
						<td><%=Bean.webposXML.getfieldTransl("target_prg_place", false) %></td>
						<td colspan="2">
							<input type="hidden" name="id_target_prg_place" id="id_target_prg_place" value="<%=id_target_prg_place %>">
							<input type="text" name="name_target_prg_place" id="name_target_prg_place" size="20" value="<%=Bean.getTargetProgramPlaceName(id_target_prg_place) %>" readonly="readonly" class="inputfield_finish_blue">
						</td>
					</tr>
					<% } %>
				<% } %>
						<% if (!Bean.isEmpty(point_fee)) { %>
						<tr><td><%=Bean.webposXML.getfieldTransl("point_fee_sum", false) %></td><td colspan="2"><input type="text" name="point_fee_txt" id="point_fee_txt" size="20" value="<%=point_fee %> <%=Bean.getWebPOSPointCurrencyName(point_fee) %>" class="inputfield_finish_blue" maxlength="15"></td></tr>
						<% } %>
						<% if (!("SHARE_FEE".equalsIgnoreCase(replenish_kind) ||
								"GET_FROM_SHARE_ACCOUNT".equalsIgnoreCase(replenish_kind))) { %>
							<% if (!Bean.isEmpty(share_fee)) { %>
							<tr><td><%=Bean.webposXML.getfieldTransl("share_fee_sum", false) %></td><td colspan="2"><input type="text" name="share_fee_txt" id="share_fee_txt" size="20" value="<%=share_fee %> <%=operCurrency %>" class="inputfield_finish_blue" maxlength="15"></td></tr>
							<% } %>
							<% if (!Bean.isEmpty(share_fee_margin)) { %>
							<tr><td><%=Bean.webposXML.getfieldTransl("share_fee_dealer_margin", false) %></td><td colspan="2"><input type="text" name="share_fee_margin_txt" id="share_fee_margin_txt" size="20" value="<%=share_fee_margin %> <%=termCurrency %>" class="inputfield_finish_gray" maxlength="15"></td></tr>
							<% } %>
						<% } %>
						<% if ("Y".equalsIgnoreCase(need_tp_subscribe)) { %>
							<tr><td colspan="3">
								<input type="checkbox" name="need_tp_subscribe" id="need_tp_subscribe" disabled="disabled" CHECKED>
								<label class="checbox_label" for="need_tp_subscribe"><%=Bean.webposXML.getfieldTransl("title_need_tp_subscribe", false) %></label>
							</tr>
						<% } %>
						<% if (!("SHARE_FEE".equalsIgnoreCase(replenish_kind) ||
								"GET_FROM_SHARE_ACCOUNT".equalsIgnoreCase(replenish_kind)) &&
								!Bean.isEmpty(membership_fee)) { %>
							<% if (!Bean.isEmpty(membership_last_date)) { %>
							<tr><td class="another_fee"><%=Bean.webposXML.getfieldTransl("membership_last_date", false) %></td><td colspan="2" class="another_fee"><input type="text" name="membership_last_date_txt" id="membership_last_date_txt" size="20" value="<%=membership_last_date %>" class="inputfield_finish_blue" maxlength="15"></td></tr>
							<% } %>
						<% } %>
						<% if (!Bean.isEmpty(membership_fee)) { %>
						<tr><td class="another_fee"><%=Bean.webposXML.getfieldTransl("membership_fee_sum", false) %></td><td colspan="2" class="another_fee"><input type="text" name="membership_fee_txt" id="membership_fee_txt" size="20" value="<%=membership_fee %> <%=termCurrency %>" class="inputfield_finish_blue" maxlength="15"></td></tr>
						<% } %>
						<% if (!Bean.isEmpty(membership_fee_margin)) { %>
						<tr><td class="another_fee"><%=Bean.webposXML.getfieldTransl("membership_fee_dealer_margin", false) %></td><td colspan="2" class="another_fee"><input type="text" name="membership_fee_margin_txt" id="membership_fee_margin_txt" size="20" value="<%=membership_fee_margin %> <%=termCurrency %>" class="inputfield_finish_gray" maxlength="15"></td></tr>
						<% } %>
						<% if (!Bean.isEmpty(tp_entrance_fee)) { %>
						<tr><td><%=Bean.webposXML.getfieldTransl("cheque_tp_entrance_fee", false) %></td><td colspan="2"><input type="text" name="tp_entrance_fee_txt" id="tp_entrance_fee_txt" size="20" value="<%=tp_entrance_fee %> <%=termCurrency %>" class="inputfield_finish_blue" maxlength="15"></td></tr>
						<% } %>
						<% if (!Bean.isEmpty(tp_membership_fee)) { %>
						<tr><td><%=Bean.webposXML.getfieldTransl("cheque_tp_membership_fee", false) %></td><td colspan="2"><input type="text" name="tp_membership_fee_txt" id="tp_membership_fee_txt" size="20" value="<%=tp_membership_fee %> <%=termCurrency %>" class="inputfield_finish_blue" maxlength="15"></td></tr>
						<% } %>
						<% if (!Bean.isEmpty(tp_fee)) { %>
						<tr><td><%=Bean.webposXML.getfieldTransl("cheque_mtf", false) %></td><td colspan="2"><input type="text" name="tp_fee_txt" id="tp_fee_txt" size="20" value="<%=tp_fee %> <%=termCurrency %>" class="inputfield_finish_blue" maxlength="15"></td></tr>
						<% } %>
						<% if (!Bean.isEmpty(mtf_margin)) { %>
						<tr><td><%=Bean.webposXML.getfieldTransl("mtf_dealer_margin", false) %></td><td colspan="2"><input type="text" name="mtf_margin_txt" id="mtf_margin_txt" size="20" value="<%=mtf_margin %> <%=termCurrency %>" class="inputfield_finish_gray" maxlength="15"></td></tr>
						<% } %>

					<% if (!"1".equalsIgnoreCase(tab)) { %>
						<% if ("SHARE_FEE".equalsIgnoreCase(replenish_kind)) { %>
							<tr><td><%=Bean.webposXML.getfieldTransl("goods_pay_way", false) %></td><td><input type="text" name="pay_type_txt" id="pay_type_txt" size="20" value="<%=Bean.webposXML.getfieldTransl("goods_pay_points", false) %>" readonly class="inputfield_finish_green"></td></tr>
							<tr><td colspan="2" class="top_line_gray line_dashed through_share_fee" style="color:green"><%=Bean.webposXML.getfieldTransl("title_fees_through_share_fee", false) %></td></tr>
							<tr><td class="through_share_fee result_desc"><%=Bean.webposXML.getfieldTransl("share_fee_sum", false) %></td><td colspan="2" class="through_share_fee"><input type="text" name="opr_sum_txt" id="opr_sum_txt" size="20" value="<%=share_fee %> <%=termCurrency %>" class="inputfield_finish_red" maxlength="15"></td></tr>
							
							<% if (!Bean.isEmpty(share_fee_margin)) { %>
							<tr><td class="through_share_fee"><%=Bean.webposXML.getfieldTransl("share_fee_dealer_margin", false) %></td><td colspan="2" class="through_share_fee"><input type="text" name="share_fee_margin_txt" id="share_fee_margin_txt" size="20" value="<%=share_fee_margin %> <%=termCurrency %>" class="inputfield_finish_gray" maxlength="15"></td></tr>
							<% } %>
							<tr>
								<td class="through_share_fee"><%=Bean.webposXML.getfieldTransl("goods_pay_way", false) %></td>
								<td class="through_share_fee">
									<% if ("CASH".equalsIgnoreCase(pay_type)) { 
										entered_sum = Bean.isEmpty(entered_sum)?share_fee:entered_sum;
										sum_change = Bean.isEmpty(sum_change)?"0":sum_change;
										
									%>
									<input type="text" name="pay_type_txt" id="pay_type_txt" size="20" value="<%=Bean.webposXML.getfieldTransl("goods_pay_cash", false) %>" readonly class="inputfield_finish_green">
								</td>
							</tr>
				  			<tr>
								<td class="through_share_fee"><%=Bean.webposXML.getfieldTransl("entered_sum", false) %></td>
								<td class="through_share_fee">
									<input type="text" name="entered_sum_title" id="entered_sum_title" size="20" value="<%=entered_sum %> <%=termCurrency %>" readonly class="inputfield_finish_blue">
								</td>
							</tr>
				  			<tr>
							<% if ("Y".equalsIgnoreCase(change_to_share_account)) { %>
								<tr><td class="through_share_fee"><%=Bean.webposXML.getfieldTransl("sum_change_to_share_account", false) %></td><td class="through_share_fee"><input type="text" name="sum_change_title" id="sum_change_title" size="20" value="<%=sum_change %> <%=termCurrency %>" readonly class="inputfield_finish_green"></td></tr>
							<% } else { %>
								<tr><td class="through_share_fee"><%=Bean.webposXML.getfieldTransl("sum_change", false) %></td><td class="through_share_fee"><input type="text" name="sum_change_title" id="sum_change_title" size="20" value="<%=sum_change %> <%=termCurrency %>" readonly class="inputfield_finish_green"></td></tr>
							<% } %>
							<% if (!Bean.isEmpty(change_margin)) { %>
								<tr><td class="through_share_fee"><i><%=Bean.webposXML.getfieldTransl("share_fee_change_margin", false) %></i></td><td colspan="2" class="through_share_fee"><input type="text" name="change_margin_txt" id="change_margin_txt" size="20" value="<%=change_margin %> <%=termCurrency %>" class="inputfield_finish_gray" maxlength="15"></td></tr>
							<% } %>
				  			<tr>
								<% if ("Y".equalsIgnoreCase(change_to_share_account)) { %>
									<td class="through_share_fee"><b><%=Bean.webposXML.getfieldTransl("add_share_account_from_change", false) %></b></td><td class="through_share_fee"><input type="text" name="sum_put_share_account" id="sum_put_share_account" size="20" value="<%=sum_put_to_share_account %> <%=termCurrency %>" readonly class="inputfield_finish_red"></td><td>
								<% } else { %>
									<td class="through_share_fee">
								<% } %>
									<% } else if ("BANK_CARD".equalsIgnoreCase(pay_type)) {  %>
									<input type="text" name="pay_type_txt" id="pay_type_txt" size="20" value="<%=Bean.webposXML.getfieldTransl("goods_pay_card", false) %>" readonly class="inputfield_finish_green">
								</td>
							</tr>
				  			<tr>
								<td class="through_share_fee"><%=Bean.webposXML.getfieldTransl("bank_trn", false) %></td>
								<td class="through_share_fee">
									<input type="text" name="bank_trn" id="bank_trn" size="20" value="<%=bank_trn %>" readonly class="inputfield_finish_green">
									<% } else if ("INVOICE".equalsIgnoreCase(pay_type)) {  %>
									<input type="text" name="pay_type_txt" id="pay_type_txt" size="20" value="<%=Bean.webposXML.getfieldTransl("goods_pay_invoices", false) %>" readonly class="inputfield_finish_green">
									<% } %>
								</td>
							</tr>
						<% } else { %>
							<% if (!Bean.isEmpty(opr_sum)) { %>
							<tr><td class="result_desc top_line_gray line_dashed"><%=Bean.webposXML.getfieldTransl("pay_total", false) %></td><td colspan="2" class="top_line_gray line_dashed"><input type="text" name="opr_sum_txt" id="opr_sum_txt" size="20" value="<%=opr_sum %> <%=operCurrency %>" class="inputfield_finish_red" maxlength="15"></td></tr>
							<% } %>
				  			<tr>
								<td><%=Bean.webposXML.getfieldTransl("goods_pay_way", false) %></td>
								<td>
								<% if ("CASH".equalsIgnoreCase(pay_type)) { %>
									<input type="text" name="pay_type_txt" id="pay_type_txt" size="20" value="<%=Bean.webposXML.getfieldTransl("goods_pay_cash", false) %>" readonly class="inputfield_finish_green">
									<% } else if ("BANK_CARD".equalsIgnoreCase(pay_type)) {  %>
									<input type="text" name="pay_type_txt" id="pay_type_txt" size="20" value="<%=Bean.webposXML.getfieldTransl("goods_pay_card", false) %>" readonly class="inputfield_finish_green">
									<% } else if ("SMPU_CARD".equalsIgnoreCase(pay_type)) {  %>
									<input type="text" name="pay_type_txt" id="pay_type_txt" size="20" value="<%=Bean.webposXML.getfieldTransl("goods_pay_points", false) %>" readonly class="inputfield_finish_green">
									<% } else if ("INVOICE".equalsIgnoreCase(pay_type)) {  %>
									<input type="text" name="pay_type_txt" id="pay_type_txt" size="20" value="<%=Bean.webposXML.getfieldTransl("goods_pay_invoices", false) %>" readonly class="inputfield_finish_green">
									<% } %>
								</td>
							</tr>
						<% if ("BANK_CARD".equalsIgnoreCase(pay_type)) {  %>
				  			<tr>
								<td><%=Bean.webposXML.getfieldTransl("bank_trn", false) %></td>
								<td>
									<input type="text" name="bank_trn" id="bank_trn" size="20" value="<%=bank_trn %>" readonly class="inputfield_finish_green">
								</td>
							</tr>
						<% } %>
						<% if ("GET_FROM_SHARE_ACCOUNT".equalsIgnoreCase(replenish_kind)) { %>
							<tr><td><%=Bean.webposXML.getfieldTransl("cheque_used_share_account", false) %></td><td><input type="text" name="sum_get_from_share_account_txt" id="sum_get_from_share_account_txt" size="20" value="<%=sum_get_from_share_account %> <%=termCurrency %>" readonly class="inputfield_finish_blue"></td>	</tr>
							<tr><td><%=Bean.webposXML.getfieldTransl("cheque_recepient_get_point", false) %></td><td><input type="text" name="sum_get_point_txt" id="sum_get_point_txt" size="20" value="<%=sum_get_point %> <%=Bean.getWebPOSPointCurrencyName(sum_get_point) %>" readonly class="inputfield_finish_blue"></td>	</tr>
						<% } %>
						<% if (!Bean.isEmpty(sum_change)) {  %>
				  			<tr>
								<td><%=Bean.webposXML.getfieldTransl("entered_sum", false) %></td><td><input type="text" name="entered_sum_title" id="entered_sum_title" size="20" value="<%=entered_sum %> <%=termCurrency %>" readonly class="inputfield_finish_blue"></td>
							</tr>
							
				  			<tr>
							<% if ("Y".equalsIgnoreCase(change_to_share_account)) { %>
								<tr><td class="pay_cash_change"><%=Bean.webposXML.getfieldTransl("sum_change_to_share_account", false) %></td><td class="pay_cash_change"><input type="text" name="sum_change_title" id="sum_change_title" size="20" value="<%=sum_change %> <%=termCurrency %>" readonly class="inputfield_finish_green"></td></tr>
							<% } else { %>
								<tr><td class="pay_cash_change"><%=Bean.webposXML.getfieldTransl("sum_change", false) %></td><td class="pay_cash_change"><input type="text" name="sum_change_title" id="sum_change_title" size="20" value="<%=sum_change %> <%=termCurrency %>" readonly class="inputfield_finish_green"></td></tr>
							<% } %>
							<% if (!Bean.isEmpty(change_margin)) { %>
								<tr><td class="pay_cash_change"><i><%=Bean.webposXML.getfieldTransl("share_fee_change_margin", false) %></i></td><td colspan="2" class="pay_cash_change"><input type="text" name="change_margin_txt" id="change_margin_txt" size="20" value="<%=change_margin %> <%=termCurrency %>" class="inputfield_finish_gray" maxlength="15"></td></tr>
							<% } %>
				  			<tr>
							<% if ("Y".equalsIgnoreCase(change_to_share_account) && 
									!Bean.isEmpty(sum_put_to_share_account)) { %>
								<td class="pay_cash_change"><b><%=Bean.webposXML.getfieldTransl("add_share_account_from_change", false) %></b></td><td class="pay_cash_change"><input type="text" name="sum_put_share_account" id="sum_put_share_account" size="20" value="<%=sum_put_to_share_account %> <%=termCurrency %>" readonly class="inputfield_finish_red"></td><td>
							<% } %>
						<% } %>
						<% if ("POINT_FEE".equalsIgnoreCase(replenish_type) ||
								"MEMBERSHIP_TARGET_FEE".equalsIgnoreCase(replenish_type)) { %>
							<% if (!Bean.isEmpty(calc_point_shareholder)) { %>
							<tr><td><%=Bean.webposXML.getfieldTransl("cheque_add_point", false) %></td><td><input type="text" name="sum_put_point_frmt" id="sum_put_point_frmt" size="20" value="<%=calc_point_shareholder %> <%=snamePointCurrency %>" readonly class="inputfield_finish_red"></td></tr>
							<% } %>
						<% } %>
						<% if ("INVOICE".equalsIgnoreCase(pay_type)) {  %>
							<tr>
								<td class="result_desc"><%=Bean.webposXML.getfieldTransl("pay_description", false) %></td><td><textarea name="pay_description" cols="27" rows="3" class="inputfield"><%=!Bean.isEmpty(id_telgr)?Bean.getTelegramPaymendDescription(oper):"" %></textarea></td>
							</tr>
						<% }  %>
					<% }  %>
				<% } %>
				<tr><td colspan="2">&nbsp;</td></tr>
				<% if (Bean.C_ISNT_ENOUGH_POINTS.equalsIgnoreCase(resultFull)) { %>
				<tr><td colspan="2"><span id="confirm_description"><%=resultMessageFull %></span></td></tr>
				<tr><td colspan="2">&nbsp;</td></tr>
				<tr>
					<td><span class="can_use_share_account"><%= Bean.webposXML.getfieldTransl("can_use_share_account", false) %></span></td><td><select name="can_use_share_account" class="inputfield"><%= Bean.getMeaningFromLookupNameOptions("YES_NO", can_use_share_account, false) %></select></td>
				</tr>
				<tr><td colspan="2">&nbsp;</td></tr>
				<tr>
					<td colspan="2" class="center">
				        <%=Bean.getSubmitButtonAjax("action/replenishupdate.jsp", "confirm", "updateForm", "div_action_big", "validateData") %>
				        <%=Bean.getSubmitButtonAjax(backLink, "button_back", "updateFormBack", backDiv) %>
					</td>
				</tr>
				<% } else if (Bean.C_NEED_PIN.equalsIgnoreCase(resultFull)) { %>
				<tr><td colspan="2"><%=Bean.webposXML.getfieldTransl("title_need_pin", false) %></td></tr>
				<tr><td colspan="2"><span id="error_description"><%=resultMessageFull %></span></td></tr>
				<tr><td <%=Bean.getDetailTableFirstColumnWidth() %>><span class="need_pin"><%=Bean.webposXML.getfieldTransl("title_pin", false) %></span></td><td><input class="inputfield" id="confirm_code" name="confirm_code" placeholder="<%=Bean.C_PIN_PLACEHOLDER %>" autofocus="" required="" type="password" size="10" value="" maxlength="<%=Bean.C_PIN_PLACEHOLDER.length() %>"></td></tr>
				<tr><td colspan="2">&nbsp;</td></tr>
				<tr>
					<td colspan="2" class="center">
				        <%=Bean.getSubmitButtonAjax("action/replenishupdate.jsp", "confirm", "updateForm", "div_action_big", "validateData") %>
				        <%=Bean.getSubmitButtonAjax(backLink, "button_back", "updateFormBack", backDiv) %>
					</td>
				</tr>
				<tr><td colspan="2">&nbsp;</td></tr>
				<% if ("Y".equalsIgnoreCase(can_send_pin_in_sms)) { %>
				<tr><td colspan="2"><%=Bean.webposXML.getfieldTransl("title_pin_remind", false) %> <%=phone_mobile_confirm %>)</td></tr>
				<tr>
					<td colspan="2" class="center">
				        <%=Bean.getSubmitButtonAjax("action/replenishupdate.jsp", "remind_pin", "updateForm2", "div_action_big") %>
					</td>
				</tr>
				<% } %>
				<% } else if (Bean.C_NEED_SMS_CONFIRMATION.equalsIgnoreCase(resultFull)) { %>
				<tr><td colspan="2">
					<div id="div_sms_confirmation">
						<%=(!Bean.isEmpty(resultMessageFull))?"<span id=\"error_description\">" + resultMessageFull + "</span><br><br>":"" %>
						<div style="width:100%; text-align: center;">
					        <%=Bean.getSubmitButtonAjax("service/get_sms_code.jsp", "button_send_sms", "updateFormGetSMS", "div_sms_confirmation", "") %>
					        <%=Bean.getSubmitButtonAjax(backLink, "button_back", "updateFormBack", backDiv) %>
						</div>
					</div>
				</td></tr>
				<% } else if (Bean.C_SMS_CONFIRM_CREATED.equalsIgnoreCase(resultFull)) {
		    		wpNatPrsObject nat_prs = new wpNatPrsObject(oper.getValue("ID_NAT_PRS"));
		    	    %>
				<tr><td colspan="2">
					<div id="div_sms_confirmation">
		    		<% if (!(nat_prs.getValue("FULL_PHOTO_FILE_NAME") == null || "".equalsIgnoreCase(nat_prs.getValue("FULL_PHOTO_FILE_NAME")))) { %>
		    		<div class="photo_rect_small" id="div_photo" style="float:right;margin:7px 0 7px 7px;">
		    			<img src="../NatPrsPhoto?id_nat_prs=<%=oper.getValue("ID_NAT_PRS") %>&noCache=<%=Bean.getNoCasheValue() %>" class="photo_image_small">
		    		</div>
		    		<% } %>
		    		<span id="succes_description"><%=resultMessageFull %></span><br>
		    		<% 
		    			int signatureSendCount = 0; 
		    			if (!Bean.isEmpty(oper.getValue("SMS_SIGNATURE_SEND_COUNT"))) {
		    				signatureSendCount = Integer.parseInt(oper.getValue("SMS_SIGNATURE_SEND_COUNT"));
		    			}
		    			if (signatureSendCount < Bean.C_SIGNATURE_MAX_SEND_COUNT) {
							String backType = "replenish";
		    				if ("MEMBERSHIP_FEE".equalsIgnoreCase(replenish_type)) {
		    					backType="membership_fee";
		    				}
		    		%>
		    		<span>Не получили СМС? <span class="go_to" onclick="ajaxpage('service/get_sms_code.jsp?id_telgr=<%= id_telgr%>&action=get_sms_code&back_type=<%=backType %>', 'div_sms_confirmation')" title="<%=Bean.buttonXML.getfieldTransl("button_questionnaire", false) %>">Отправить еще раз</span></span>
		    		<br>
		    		<% } else { %>
		    		<br>
		    		<% } %>
		    		<br>
		    		<span class="need_sms_confirmation"><%=Bean.webposXML.getfieldTransl("sms_password", false) %></span>&nbsp;&nbsp;&nbsp;<input class="inputfield" id="confirm_code" name="confirm_code" placeholder="<%=Bean.C_CONFIRM_OPER_SMS_PLACEHOLDER %>" autofocus="" required="" type="password" size="10" value="" maxlength="<%=Bean.C_CONFIRM_OPER_SMS_PLACEHOLDER.length() %>"><br><br>
		    		<div style="width:100%; text-align: center;">
		    		    <%=Bean.getSubmitButtonAjax("action/replenishupdate.jsp", "confirm", "updateForm", "div_action_big", "validateData") %>
		    		    <%=Bean.getSubmitButtonAjax(backLink, "button_back", "updateFormBack", backDiv) %>
		    		</div>
					</div>
				</td></tr>
				<tr><td colspan="2">&nbsp;</td></tr>
				<% } else if (Bean.C_NEED_ACTIVATION_CODE.equalsIgnoreCase(resultFull)) { %>
				<tr><td colspan="2"><%=Bean.webposXML.getfieldTransl("title_need_activation_code", false) %></td></tr>
				<tr><td <%=Bean.getDetailTableFirstColumnWidth() %>><span class="need_sms_confirmation"><%=Bean.webposXML.getfieldTransl("card_activation_code", false) %></span></td><td><input class="inputfield" id="confirm_code" name="confirm_code" placeholder="<%=Bean.C_ACTIVATION_CODE_PLACEHOLDER %>" autofocus="" required="" type="password" size="10" value="" maxlength="<%=Bean.C_ACTIVATION_CODE_PLACEHOLDER.length() %>"></td></tr>
				<tr><td colspan="2">&nbsp;</td></tr>
				<tr>
					<td colspan="2" class="center">
				        <%=Bean.getSubmitButtonAjax("action/replenishupdate.jsp", "confirm", "updateForm", "div_action_big", "validateData") %>
				        <%=Bean.getSubmitButtonAjax(backLink, "button_back", "updateFormBack", backDiv) %>
					</td>
				</tr>
				<% } else if (Bean.C_ENOUGH_MEANS.equalsIgnoreCase(resultFull)) { %>
				<tr><td colspan="2" class="through_share_fee"><span id="error_title"><%=Bean.webposXML.getfieldTransl("operation_error", false) %></span></td></tr>
				<tr><td colspan="2" class="through_share_fee"><span id="error_description"><%=resultMessageFull %></span><% if (tab2HasPerm) { %><br><b><%=Bean.webposXML.getfieldTransl("title_can_share_fee", false) %><% } %></b></td></tr>
				<% if (tab2HasPerm) { %>
					<tr>
						<td colspan="4" class="through_share_fee">
							<table border="0"class="payTypeTable">
								<tr>
									<%=Bean.getPayTypeImage("CASH", "WEBPOS_SERVICE_REPLANISH_SHARE_FEE_PAY_CASH", "action/replenishupdate.jsp?pay_type=CASH&process=no&replenish_kind=SHARE_FEE", "div_action_big", "updateForm", "") %>
									<%=Bean.getPayTypeImage("BANK_CARD", "WEBPOS_SERVICE_REPLANISH_SHARE_FEE_PAY_BANK_CARD", "action/replenishupdate.jsp?pay_type=BANK_CARD&process=no&replenish_kind=SHARE_FEE", "div_action_big", "updateForm", "") %>
									<%=Bean.getPayTypeImage("INVOICE", "WEBPOS_SERVICE_REPLANISH_SHARE_FEE_MAKE_INVOICE", "action/replenishupdate.jsp?pay_type=INVOICE&process=yes&replenish_kind=SHARE_FEE", "div_action_big", "updateForm", "") %>
								</tr>
							</table>
						</td>
					</tr>
				<% } %>
				<tr><td colspan="2">&nbsp;</td></tr>
				<tr>
					<td colspan="2" class="center">
				        <%=Bean.getSubmitButtonAjax(backLink, "button_back", "updateFormBack", backDiv) %>
					</td>
				</tr>
				<% } else if (Bean.C_NEED_END_INFO.equalsIgnoreCase(resultFull)) { %>
				
				<% if (!Bean.isEmpty(resultMessageFull)) { %>
				<tr><td colspan="2"><span id="succes_description"><%=resultMessageFull %></span></td></tr>
				<tr><td colspan="2">&nbsp;</td></tr>
				<% } %>
				<tr>
					<td colspan="2" class="center">
						<% if ("INVOICE".equalsIgnoreCase(pay_type)) { %>
				        	<%=Bean.getSubmitButtonAjax("report/operationupdate.jsp", "confirm", "updateForm", "div_action_big", "") %>
						<% } else { %>
				        	<%=Bean.getSubmitButtonAjax("action/replenishupdate.jsp", "confirm", "updateForm", "div_action_big", "") %>
						<% } %>
				        <%=Bean.getSubmitButtonAjax(backLink, "button_back", "updateFormBack", backDiv) %>
					</td>
				</tr>
				<% } else { %>
	 				<% if (isErrorResult) { %>
						<tr><td colspan="2"><span id="error_title"><%=Bean.webposXML.getfieldTransl("operation_error", false) %></span></td></tr>
						<tr><td colspan="2"><span id="error_description"><%=resultMessageFull %></span></td></tr>
						<tr>
							<td colspan="2" class="center">
						        <%=Bean.getSubmitButtonAjax(backLink, "button_back", "updateFormBack", backDiv) %>
							</td>
						</tr>
					<% } else { %>
						<% if (!Bean.isEmpty(resultMessageFull)) { %>
							<tr><td colspan="2"><span id="confirm_description"><%=resultMessageFull %></span></td></tr>
						<% } %>
						<tr>
							<td colspan="2" class="center">
						        <%=Bean.getSubmitButtonAjax("action/replenishupdate.jsp", "confirm", "updateForm", "div_action_big", "validateData") %>
						        <%=Bean.getSubmitButtonAjax(backLink, "button_back", "updateFormBack", backDiv) %>
							</td>
						</tr>
					<% } %>
				<% } %> 
				</tbody>
				</table>
			</form>
			<form name="updateFormBack" id="updateFormBack" accept-charset="UTF-8" method="POST">
				<input type="hidden" name="id_telgr" value="<%=id_telgr %>">
    			<input type="hidden" name="type" value="card">
    			<input type="hidden" name="id_target_prg" value="<%=id_target_prg %>">
    			<input type="hidden" name="id_target_prg_place" value="<%=id_target_prg_place %>">
				<% if ("MEMBERSHIP_TARGET_FEE".equalsIgnoreCase(replenish_type)) {  %>
    			<input type="hidden" name="action" value="select_program">
				<% } else { %>
					<% if (!("CASH".equalsIgnoreCase(pay_type) || "BANK_CARD".equalsIgnoreCase(pay_type))) {   %>
    				<input type="hidden" name="action" value="check_card">
					<% } else { %>
    				<input type="hidden" name="action" value="replenish">
					<% } %>
				<% } %>
    			<input type="hidden" name="process" value="no">
				<input type="hidden" name="id_term" value="<%=id_term %>">
				<input type="hidden" name="pay_type" id="pay_type" value="<%=pay_type %>">
				<input type="hidden" name="cd_card1" id="cd_card1" value="<%=cd_card1 %>">
				<input type="hidden" name="replenish_type" value="<%=replenish_type %>">
				<input type="hidden" name="replenish_kind" value="<%=replenish_kind %>">
				<input type="hidden" name="id_card_status" value="<%=id_card_status %>">
				<input type="hidden" name="replenish_currency" value="<%=Bean.loginTerm.getValue("CD_TERM_CURRENCY") %>">
				<input type="hidden" name="id_target_prg" id="id_target_prg" value="<%=id_target_prg %>">
				<input type="hidden" name="id_target_prg_place" id="id_target_prg_place" value="<%=id_target_prg_place %>">
    			<input type="hidden" name="point_fee" id="point_fee" value="<%=point_fee %>">
				<input type="hidden" name="share_fee" id="share_fee" value="<%=share_fee %>">
				<input type="hidden" name="need_tp_subscribe" id="need_tp_subscribe" value="<%=need_tp_subscribe %>">
				<input type="hidden" name="need_tp_admin_confirm" id="need_tp_admin_confirm" value="<%=need_tp_admin_confirm %>">
				<input type="hidden" name="need_tp_entrance_fee" id="need_tp_entrance_fee" value="<%=need_tp_entrance_fee %>">
				<input type="hidden" name="tp_entrance_fee" id="tp_entrance_fee" value="<%=tp_entrance_fee %>">
				<input type="hidden" name="need_membership_fee" id="need_membership_fee" value="<%=need_membership_fee %>">
				<input type="hidden" name="membership_fee" id="membership_fee" value="<%=membership_fee %>">
				<input type="hidden" name="need_tp_membership_fee" id="need_tp_membership_fee" value="<%=need_tp_membership_fee %>">
				<input type="hidden" name="tp_membership_fee" id="tp_membership_fee" value="<%=tp_membership_fee %>">
				<input type="hidden" name="tp_fee" id="tp_fee" value="<%=tp_fee %>">
				<input type="hidden" name="bank_trn" id="bank_trn" value="<%=bank_trn %>">
			</form>
			
			<% if (Bean.C_NEED_SMS_CONFIRMATION.equalsIgnoreCase(resultFull)) { %>
			<form name="updateFormGetSMS" id="updateFormGetSMS" accept-charset="UTF-8" method="POST">
				<input type="hidden" name="id_telgr" value="<%=id_telgr %>">
    			<input type="hidden" name="action" value="get_sms_code">
				<input type="hidden" name="id_term" value="<%=id_term %>">
				<% if ("MEMBERSHIP_FEE".equalsIgnoreCase(replenish_type)) { %>
				<input type="hidden" name="back_type" value="membership_fee">
				<% } else { %>
				<input type="hidden" name="back_type" value="replenish">
				<% } %>
			</form>
			<% } %>
			<% if (Bean.C_ENOUGH_MEANS.equalsIgnoreCase(resultFull)) { %>
			<form name="updateForm2" id="updateForm2" accept-charset="UTF-8" method="POST">
				<input type="hidden" name="id_telgr" value="<%=id_telgr %>">
    			<input type="hidden" name="type" value="card">
    			<input type="hidden" name="action" value="replenish">
    			<input type="hidden" name="process" value="no">
				<input type="hidden" name="id_term" value="<%=id_term %>">
				<input type="hidden" name="cd_card1" id="cd_card1" value="<%=cd_card1 %>">
				<input type="hidden" name="replenish_type" value="SHARE_FEE">
    			<input type="hidden" name="pay_type" value="<%=pay_type %>">
				<input type="hidden" name="id_card_status" value="<%=id_card_status %>">
				<input type="hidden" name="replenish_currency" value="<%=Bean.loginTerm.getValue("CD_TERM_CURRENCY") %>">
				<input type="hidden" name="id_target_prg" id="id_target_prg" value="<%=id_target_prg %>">
				<input type="hidden" name="id_target_prg_place" id="id_target_prg_place" value="<%=id_target_prg_place %>">
    			<input type="hidden" name="share_fee" id="share_fee" value="<%=share_fee %>">
				<input type="hidden" name="need_tp_subscribe" id="need_tp_subscribe" value="<%=need_tp_subscribe %>">
				<input type="hidden" name="need_tp_admin_confirm" id="need_tp_admin_confirm" value="<%=need_tp_admin_confirm %>">
				<input type="hidden" name="need_tp_entrance_fee" id="need_tp_entrance_fee" value="<%=need_tp_entrance_fee %>">
				<input type="hidden" name="tp_entrance_fee" id="tp_entrance_fee" value="<%=tp_entrance_fee %>">
				<input type="hidden" name="need_membership_fee" id="need_membership_fee" value="<%=need_membership_fee %>">
				<input type="hidden" name="membership_fee" id="membership_fee" value="<%=membership_fee %>">
				<input type="hidden" name="need_tp_membership_fee" id="need_tp_membership_fee" value="<%=need_tp_membership_fee %>">
				<input type="hidden" name="tp_membership_fee" id="tp_membership_fee" value="<%=tp_membership_fee %>">
				<input type="hidden" name="tp_fee" id="tp_fee" value="<%=tp_fee %>">
				<input type="hidden" name="share_fee" id="share_fee" value="<%=opr_sum %>">
			</form>
			<% } %>
			<% if (Bean.C_NEED_PIN.equalsIgnoreCase(resultFull)) { %>
			<form name="updateForm2" id="updateForm2" accept-charset="UTF-8" method="POST">
				<input type="hidden" name="id_telgr" value="<%=id_telgr %>">
    			<input type="hidden" name="type" value="card">
    			<input type="hidden" name="action" value="oper_confirm">
				<input type="hidden" name="confirm_type" value="REMIND_PIN">
    			<input type="hidden" name="process" value="yes">
				<input type="hidden" name="id_term" value="<%=id_term %>">
				<input type="hidden" name="cd_card1" id="cd_card1" value="<%=cd_card1 %>">
				<input type="hidden" name="replenish_type" value="<%=replenish_type %>">
    			<input type="hidden" name="pay_type" value="<%=pay_type %>">
				<input type="hidden" name="id_card_status" value="<%=id_card_status %>">
				<input type="hidden" name="replenish_currency" value="<%=Bean.loginTerm.getValue("CD_TERM_CURRENCY") %>">
				<input type="hidden" name="id_target_prg" id="id_target_prg" value="<%=id_target_prg %>">
				<input type="hidden" name="id_target_prg_place" id="id_target_prg_place" value="<%=id_target_prg_place %>">
    			<input type="hidden" name="share_fee" id="share_fee" value="<%=share_fee %>">
				<input type="hidden" name="need_tp_subscribe" id="need_tp_subscribe" value="<%=need_tp_subscribe %>">
				<input type="hidden" name="need_tp_admin_confirm" id="need_tp_admin_confirm" value="<%=need_tp_admin_confirm %>">
				<input type="hidden" name="need_tp_entrance_fee" id="need_tp_entrance_fee" value="<%=need_tp_entrance_fee %>">
				<input type="hidden" name="tp_entrance_fee" id="tp_entrance_fee" value="<%=tp_entrance_fee %>">
				<input type="hidden" name="need_membership_fee" id="need_membership_fee" value="<%=need_membership_fee %>">
				<input type="hidden" name="membership_fee" id="membership_fee" value="<%=membership_fee %>">
				<input type="hidden" name="need_tp_membership_fee" id="need_tp_membership_fee" value="<%=need_tp_membership_fee %>">
				<input type="hidden" name="tp_membership_fee" id="tp_membership_fee" value="<%=tp_membership_fee %>">
				<input type="hidden" name="tp_fee" id="tp_fee" value="<%=tp_fee %>">
			</form>
			<% } %>
		<% } 
			
	
 		
	} else {
	    %> <%= Bean.getUnknownProcessText(process) %> <%
	}
} else {
    %> <%= Bean.getUnknownTypeText(type) %> <%
}

%>


</body>
</html>
