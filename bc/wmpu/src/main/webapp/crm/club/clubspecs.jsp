<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />
<%@page import="bc.objects.bcClubObject"%>
<%@page import="java.util.HashMap"%>
<%@page import="bc.objects.bcNatPrsRoleObject"%><html>

<%= Bean.getLogOutScript(request) %>

<head>
	<%= Bean.getMetaContent() %>
	<%= Bean.getBottomFrameCSS() %>
</head>

<%
HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String pageFormName = "CLUB_CLUB";

Bean.setJspPageForTabName(pageFormName);

String tagParticipant = "_PARTICIPANT";
String tagParticipantFind = "_PARTICIPANT_FIND";
String tagParticipantSelected = "_PARTICIPANT_SELECTED";
String tagParticipantType = "_PARTICIPANT_TYPE";
String tagAccess = "_ACCESS";
String tagUserFind = "_USER_FIND";
String tagUserSelected = "_USER_SELECTED";
String tagUserStatus = "_USER_STATUS";
String tagFund = "_FUND";
String tagFundFind = "_FUND_FIND";
String tagTargetProgram = "_TARGET_PROGRAM";
String tagTargetProgramFind = "_TARGET_PROGRAM_FIND";
String tagTargetProgramPayPeriod = "_TARGET_PROGRAM_PAY_PERIOD";

String	id = Bean.getDecodeParam(parameters.get("id"));
String	tab = Bean.getDecodeParam(parameters.get("tab"));

if (tab==null || "".equalsIgnoreCase(tab)) { tab = Bean.tabsHmGetValue(pageFormName); }
if (id==null || "".equalsIgnoreCase(id)) {
%>
	<%=Bean.getIDNotFoundMessage() %>
<% 
} else {
	Bean.tabsHmSetValue(pageFormName, tab);
	bcClubObject club = new bcClubObject(id);
	
	String l_participant_page = Bean.getDecodeParam(parameters.get("participant_page"));
	Bean.pageCheck(pageFormName + tagParticipant, l_participant_page);
	String l_participant_page_beg = Bean.getFirstRowNumber(pageFormName + tagParticipant);
	String l_participant_page_end = Bean.getLastRowNumber(pageFormName + tagParticipant);

	String participant_find 	= Bean.getDecodeParam(parameters.get("participant_find"));
	participant_find 	= Bean.checkFindString(pageFormName + tagParticipantFind, participant_find, l_participant_page);

	String participant_type 	= Bean.getDecodeParam(parameters.get("participant_type"));
	participant_type 	= Bean.checkFindString(pageFormName + tagParticipantType, participant_type, l_participant_page);
	
	String participant_selected 	= Bean.getDecodeParam(parameters.get("participant_selected"));
	participant_selected 	= Bean.checkFindString(pageFormName + tagParticipantSelected, participant_selected, l_participant_page);
	
	String l_access_page = Bean.getDecodeParam(parameters.get("access_page"));
	Bean.pageCheck(pageFormName + tagAccess, l_access_page);
	String l_access_page_beg = Bean.getFirstRowNumber(pageFormName + tagAccess);
	String l_access_page_end = Bean.getLastRowNumber(pageFormName + tagAccess);
	
	String user_selected 	= Bean.getDecodeParam(parameters.get("user_selected"));
	user_selected 	= Bean.checkFindString(pageFormName + tagUserSelected, user_selected, l_access_page);

	String user_find 	= Bean.getDecodeParam(parameters.get("user_find"));
	user_find 	= Bean.checkFindString(pageFormName + tagUserFind, user_find, l_access_page);

	String user_status 	= Bean.getDecodeParam(parameters.get("user_status"));
	user_status 	= Bean.checkFindString(pageFormName + tagUserStatus, user_status, l_access_page);
	
	String l_fund_page = Bean.getDecodeParam(parameters.get("fund_page"));
	Bean.pageCheck(pageFormName + tagFund, l_fund_page);
	String l_fund_page_beg = Bean.getFirstRowNumber(pageFormName + tagFund);
	String l_fund_page_end = Bean.getLastRowNumber(pageFormName + tagFund);

	String fund_find 	= Bean.getDecodeParam(parameters.get("fund_find"));
	fund_find 	= Bean.checkFindString(pageFormName + tagFundFind, fund_find, l_fund_page);
	
	String l_target_program_page = Bean.getDecodeParam(parameters.get("target_program_page"));
	Bean.pageCheck(pageFormName + tagTargetProgram, l_target_program_page);
	String l_target_program_page_beg = Bean.getFirstRowNumber(pageFormName + tagTargetProgram);
	String l_target_program_page_end = Bean.getLastRowNumber(pageFormName + tagTargetProgram);

	String target_program_find 	= Bean.getDecodeParam(parameters.get("target_program_find"));
	target_program_find 	= Bean.checkFindString(pageFormName + tagTargetProgramFind, target_program_find, l_target_program_page);

	String target_program_pay_period 	= Bean.getDecodeParam(parameters.get("target_program_pay_period"));
	target_program_pay_period 	= Bean.checkFindString(pageFormName + tagTargetProgramPayPeriod, target_program_pay_period, l_target_program_page);


%>
<% Bean.currentMenu.setCurrentTab(Bean.currentMenu.getTabSheetName(tab)); %>
<body>
<div id="div_tabsheet">

	<table <%=Bean.getTableMenuParam() %>>
		<tr>
			<%= Bean.getPageHeader() %>

			<% if (Bean.currentMenu.isCurrentTabAndEditPermitted("CLUB_CLUB_PARAM")) { %>
			    <%= Bean.getMenuButton("ADD", "../crm/club/clubupdate.jsp?id=" + id + "&type=club&action=add2&process=no", "", "") %>
			<%  } %>

			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLUB_CLUB_PARTICIPANT")) { %>
			    <!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagParticipant, "../crm/club/clubspecs.jsp?id=" + id + "&tab="+Bean.currentMenu.getTabID("CLUB_CLUB_PARTICIPANT")+"&", "participant_page") %>
			<% } %>

			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLUB_CLUB_FUNDS")) { %>
			    <% if (Bean.currentMenu.isCurrentTabAndEditPermitted("CLUB_CLUB_FUNDS")) { %>
				    <%= Bean.getMenuButtonBase("ADD", "../crm/club/fundupdate.jsp?back_type=CLUB&id=" + id + "&type=general&action=add&process=no", "", "", "", "div_data_detail") %>
				<%  } %>
			    <!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagFund, "../crm/club/clubspecs.jsp?id=" + id + "&tab="+Bean.currentMenu.getTabID("CLUB_CLUB_FUNDS")+"&", "fund_page") %>
			<% } %>

			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLUB_CLUB_TARGET_PROGRAM")) { %>
			    <% if (Bean.currentMenu.isCurrentTabAndEditPermitted("CLUB_CLUB_TARGET_PROGRAM")) { %>
				    <%= Bean.getMenuButtonBase("ADD", "../crm/club/target_programupdate.jsp?back_type=CLUB&id=" + id + "&type=general&action=add&process=no", "", "", "", "div_data_detail") %>
				<%  } %>
			    <!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagTargetProgram, "../crm/club/clubspecs.jsp?id=" + id + "&tab="+Bean.currentMenu.getTabID("CLUB_CLUB_TARGET_PROGRAM")+"&", "target_program_page") %>
			<% } %>

			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLUB_CLUB_ACCESS")) { %>
			    <!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagAccess, "../crm/club/clubspecs.jsp?id=" + id + "&tab="+Bean.currentMenu.getTabID("CLUB_CLUB_ACCESS")+"&", "access_page") %>
			<% } %>

	
		</tr>
	</table>
	<table <%=Bean.getTableMenuParam() %>>
		<%= Bean.getDetailCaption(club.getValue("SNAME_CLUB")) %>
		<tr>
			<td>
	            <!-- Выводим перечень закладок -->
				<%= Bean.currentMenu.getFormTabScheeds(tab, "../crm/club/clubspecs.jsp?id=" + id + "&adr=full") %>
			</td>
	</tr>
</table>
</div>
<div id="div_data">
<div id="div_data_detail">
<%

if (Bean.currentMenu.isCurrentTabAndEditPermitted("CLUB_CLUB_PARAM")) { %>

			<script>
				var formClubParam = new Array (
					new Array ('cd_club', 'varchar2', 1),
					new Array ('name_club', 'varchar2', 1),
					new Array ('sname_club', 'varchar2', 1),
					new Array ('name_operator', 'varchar2', 1),
					new Array ('cd_country_def', 'varchar2', 1),
					new Array ('name_clearing_bank', 'varchar2', 1),
					new Array ('cd_currency_base', 'varchar2', 1)
				);
				function myValidateForm() {
					return validateForm(formClubParam);
				}
			</script>

	<form action="../crm/club/clubupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formClubParam)">
		<input type="hidden" name="type" value="club">
	    <input type="hidden" name="action" value="edit_general">
	    <input type="hidden" name="process" value="yes">
	    <input type="hidden" name="id" value="<%=id %>">
	<table <%=Bean.getTableDetailParam() %>>
		<tr>
			<td><%= Bean.clubXML.getfieldTransl("cd_club", true) %> </td><td><input type="text" name="cd_club" size="20" value="<%= club.getValue("CD_CLUB") %>" class="inputfield"></td>
			<td><%= Bean.clubXML.getfieldTransl("name_country_def", true) %></td> <td><select name="cd_country_def" class="inputfield"><%= Bean.getCountryOptions(club.getValue("CODE_COUNTRY_DEF"), true) %></select></td>
		</tr>
		<tr>
			<td><%= Bean.clubXML.getfieldTransl("name_club", true) %> </td><td><input type="text" name="name_club" size="70" value="<%= club.getValue("NAME_CLUB") %>" class="inputfield"></td>
			<td><%= Bean.clubXML.getfieldTransl("cd_currency_base", true) %>
				<%= Bean.getGoToCurrencyLink(club.getValue("CD_CURRENCY_BASE")) %>
			  </td> <td><select name="cd_currency_base" class="inputfield"><%= Bean.getCurrencyOptions(club.getValue("CD_CURRENCY_BASE"), true) %></select></td>
		</tr>
		<tr>
			<td><%= Bean.clubXML.getfieldTransl("sname_club", true) %> </td><td><input type="text" name="sname_club" size="70" value="<%= club.getValue("SNAME_CLUB") %>" class="inputfield"></td>
			<td><%= Bean.clubXML.getfieldTransl("shareholder_point_percent", true) %> </td><td><input type="text" name="shareholder_point_percent" size="10" value="<%= club.getValue("SHAREHOLDER_POINT_PERCENT") %>" class="inputfield"></td>
		</tr>
		<tr>
			<td><%= Bean.clubXML.getfieldTransl("name_operator", true) %>
				<%= Bean.getGoToJurPrsHyperLink(club.getValue("ID_OPERATOR")) %>
			  </td>
			  <td>
				  <%=Bean.getWindowFindJurPrs("operator", club.getValue("ID_OPERATOR"), club.getValue("SNAME_JUR_PRS"), "OPERATOR", "25") %>
			  </td>
			<% 
				String id_nat_prs_referral = "";
				String name_nat_prs_referral = "";
				if (!Bean.isEmpty(club.getValue("ID_NAT_PRS_ROLE_DEF_REFFERAL"))) {
					bcNatPrsRoleObject role = new bcNatPrsRoleObject(club.getValue("ID_NAT_PRS_ROLE_DEF_REFFERAL"));
					id_nat_prs_referral = role.getValue("ID_NAT_PRS");
					name_nat_prs_referral = role.getValue("CD_CARD1") + " (" + role.getValue("FIO_NAT_PRS") + ")";
				}
			%>
			<td><%= Bean.clubXML.getfieldTransl("default_referral", true) %>
				<%= Bean.getGoToNatPrsLink(id_nat_prs_referral) %>
			  </td>
			  <td>
				  <%=Bean.getWindowFindNatPrsRole("nat_prs_role_def_refferal", club.getValue("ID_NAT_PRS_ROLE_DEF_REFFERAL"), name_nat_prs_referral, "25") %>
			  </td>
		</tr>
		<tr>
			<td><%= Bean.clubXML.getfieldTransl("name_clearing_bank", true) %>
				<%= Bean.getGoToJurPrsHyperLink(club.getValue("ID_CLEARING_BANK")) %>
			  </td> 
			  <td>
				  <%=Bean.getWindowFindJurPrs("clearing_bank", club.getValue("ID_CLEARING_BANK"), club.getValue("SNAME_CLEARING_BANK"), "BANK", "25") %>
			  </td>
		</tr>
		<tr>
			<td><%= Bean.clubXML.getfieldTransl("doc_file_dir", true) %> </td><td colspan="3"><input type="text" name="doc_file_dir" size="70" value="<%= club.getValue("doc_file_dir") %>" class="inputfield"></td>
		</tr>
		<%=	Bean.getIdCreationAndMoficationRecordFields(
				club.getValue("ID_CLUB"),
				club.getValue(Bean.getCreationDateFieldName()),
				club.getValue("CREATED_BY"),
				club.getValue(Bean.getLastUpdateDateFieldName()),
				club.getValue("LAST_UPDATE_BY")
			) %>
 		<tr>
			<td colspan="6" align="center">
				<%=Bean.getSubmitButtonAjax("../crm/club/clubupdate.jsp") %>
				<%=Bean.getResetButton() %>
				<%=Bean.getGoBackButton("../crm/club/club.jsp") %>
			</td>
		</tr>
	</table>
	</form>
<% } else if (Bean.currentMenu.isCurrentTabAndPreviewPermitted("CLUB_CLUB_PARAM")) { %>
	<form>
	<table <%=Bean.getTableDetailParam() %>>
		<tr>
			<td><%= Bean.clubXML.getfieldTransl("cd_club", false) %> </td><td><input type="text" name="cd_club" size="20" value="<%= club.getValue("CD_CLUB") %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.clubXML.getfieldTransl("name_country_def", false) %></td> 
			<td><input type="text" name="name_country_def" size="30" value="<%= Bean.getCountryName(club.getValue("CODE_COUNTRY_DEF")) %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td><%= Bean.clubXML.getfieldTransl("name_club", false) %> </td><td><input type="text" name="name_club" size="70" value="<%= club.getValue("NAME_CLUB") %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.clubXML.getfieldTransl("cd_currency_base", false) %>
				<%= Bean.getGoToCurrencyLink(club.getValue("CD_CURRENCY_BASE")) %>
			  </td> <td><input type="text" name="cd_currency_base" size="30" value="<%= club.getValue("NAME_CURRENCY_BASE") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td><%= Bean.clubXML.getfieldTransl("sname_club", false) %> </td><td><input type="text" name="sname_club" size="70" value="<%= club.getValue("SNAME_CLUB") %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.clubXML.getfieldTransl("shareholder_point_percent", false) %> </td><td><input type="text" name="shareholder_point_percent" size="10" value="<%= club.getValue("SHAREHOLDER_POINT_PERCENT") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td><%= Bean.clubXML.getfieldTransl("name_operator", false) %>
				<%= Bean.getGoToJurPrsHyperLink(club.getValue("ID_OPERATOR")) %>
			</td>
			<td><input type="text" name="name_operator" size="30" value="<%= club.getValue("SNAME_JUR_PRS") %>" readonly="readonly" class="inputfield-ro"></td>
			
			<% 
				String id_nat_prs_referral = "";
				String name_nat_prs_referral = "";
				if (!Bean.isEmpty(club.getValue("ID_NAT_PRS_ROLE_DEF_REFFERAL"))) {
					bcNatPrsRoleObject role = new bcNatPrsRoleObject(club.getValue("ID_NAT_PRS_ROLE_DEF_REFFERAL"));
					id_nat_prs_referral = role.getValue("ID_NAT_PRS");
					name_nat_prs_referral = role.getValue("CD_CARD1") + " (" + role.getValue("FIO_NAT_PRS") + ")";
				}
			%>
			<td><%= Bean.clubXML.getfieldTransl("default_referral", false) %>
				<%= Bean.getGoToNatPrsLink(id_nat_prs_referral) %>
			</td>
			<td><input type="text" name="default_referral" size="30" value="<%= name_nat_prs_referral %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td><%= Bean.clubXML.getfieldTransl("name_clearing_bank", false) %>
				<%= Bean.getGoToJurPrsHyperLink(club.getValue("ID_CLEARING_BANK")) %>
			  </td> 
			  <td><input type="text" name="name_clearing_bank" size="30" value="<%= club.getValue("NAME_CLEARING_BANK") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td><%= Bean.clubXML.getfieldTransl("doc_file_dir", false) %> </td><td colspan="3"><input type="text" name="doc_file_dir" size="70" value="<%= club.getValue("doc_file_dir") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<%=	Bean.getIdCreationAndMoficationRecordFields(
				club.getValue("ID_CLUB"),
				club.getValue(Bean.getCreationDateFieldName()),
				club.getValue("CREATED_BY"),
				club.getValue(Bean.getLastUpdateDateFieldName()),
				club.getValue("LAST_UPDATE_BY")
			) %>
 		<tr>
			<td colspan="6" align="center">
				<%=Bean.getGoBackButton("../crm/club/club.jsp") %>
			</td>
		</tr>
	</table>
	</form>

<%   
 }  

if (Bean.currentMenu.isCurrentTabAndEditPermitted("CLUB_CLUB_INTERCHANGE_TERMINALS")) { %>

	<script>
		var formClubParam = new Array (
			new Array ('auth_server_host', 'varchar2', 1),
			new Array ('auth_server_port', 'integer', 1),
			new Array ('auth_server_charset', 'varchar2', 1),
			new Array ('reserve_auth_server_host', 'varchar2', 1),
			new Array ('reserve_auth_server_port', 'integer', 1),
			new Array ('reserve_auth_server_charset', 'varchar2', 1),
			new Array ('expect_time', 'integer', 1),
			new Array ('no_actions_with_card_max_days', 'varchar2', 1),
			new Array ('term_code_page', 'varchar2', 1),
			new Array ('def_ver_telgr', 'varchar2', 1),
			new Array ('crypt_telgr', 'varchar2', 1),
			new Array ('def_ver_key', 'varchar2', 1),
			new Array ('def_nincmax', 'varchar2', 1),
			new Array ('term_parm_time', 'varchar2', 1),
			new Array ('max_error_messages', 'integer', 1),
			new Array ('max_resp_time', 'integer', 1),
			new Array ('exc_wtx_time', 'integer', 1),
			new Array ('def_tr_limit', 'varchar2', 1),
			new Array ('parse_trans_immediately', 'varchar2', 1),
			new Array ('account_postings_immediate', 'varchar2', 1),
			new Array ('parse_trans_avg_time_limit', 'integer', 1),
			new Array ('parse_trans_count', 'integer', 1),
			new Array ('parse_trans_total_time', 'integer', 1),
			new Array ('need_calc_online_bon_pay_pin', 'varchar2', 1),
			new Array ('need_calc_online_storno_pin', 'varchar2', 1)
		);
		function myValidateForm() {
			return validateForm(formClubParam);
		}
	</script>

		<form action="../crm/club/clubupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formClubParam)">
		<input type="hidden" name="type" value="club">
		<input type="hidden" name="action" value="edit_interchange">
		<input type="hidden" name="process" value="yes">
		<input type="hidden" name="id" value="<%=id %>">
		
		<table <%=Bean.getTableDetailParam() %>>
			<tr>							
				<td colspan="2"><b><%= Bean.clubXML.getfieldTransl("h_general_auth_server", false) %></b></td>
				<td colspan="2"><b><%= Bean.clubXML.getfieldTransl("h_reserve_auth_server", false) %></b></td>
			</tr>
			<tr>
				<td><%= Bean.clubXML.getfieldTransl("auth_server_host", true) %></td> <td><input type="text" name="auth_server_host" size="20" value="<%=club.getValue("AUTH_SERVER_HOST") %>" class="inputfield"></td>
				<td><%= Bean.clubXML.getfieldTransl("auth_server_host", true) %></td> <td><input type="text" name="reserve_auth_server_host" size="20" value="<%=club.getValue("RESERVE_AUTH_SERVER_HOST") %>" class="inputfield"></td>
			</tr>
			<tr>
				<td><%= Bean.clubXML.getfieldTransl("auth_server_port", true) %></td> <td><input type="text" name="auth_server_port" size="20" value="<%=club.getValue("AUTH_SERVER_PORT") %>" class="inputfield"></td>
				<td><%= Bean.clubXML.getfieldTransl("auth_server_port", true) %></td> <td><input type="text" name="reserve_auth_server_port" size="20" value="<%=club.getValue("RESERVE_AUTH_SERVER_PORT") %>" class="inputfield"></td>
			</tr>
			<tr>
				<td><%= Bean.clubXML.getfieldTransl("auth_server_charset", true) %></td> <td><select name="auth_server_charset" class="inputfield"><%=Bean.getMeaningFromLookupNameOptions("AUTH_SERVER_CODE_PAGE", club.getValue("AUTH_SERVER_CHARSET"), true) %></select></td>
				<td><%= Bean.clubXML.getfieldTransl("auth_server_charset", true) %></td> <td><select name="reserve_auth_server_charset" class="inputfield"><%=Bean.getMeaningFromLookupNameOptions("AUTH_SERVER_CODE_PAGE", club.getValue("RESERVE_AUTH_SERVER_CHARSET"), true) %></select></td>
			</tr>
			<tr>							
				<td colspan=6 class="top_line"><b><%= Bean.clubXML.getfieldTransl("h_card_processing", false) %></b></td>
			</tr>
			<tr>
				<td><%= Bean.clubXML.getfieldTransl("expect_time", true) %></td> <td><input type="text" name="expect_time" size="20" value="<%= club.getValue("EXPECT_TIME") %>" class="inputfield"> </td>
				<td><%= Bean.clubXML.getfieldTransl("no_actions_with_card_max_days", true) %></td> <td><input type="text" name="no_actions_with_card_max_days" size="20" value="<%=club.getValue("NO_ACTIONS_WITH_CARD_MAX_DAYS") %>" class="inputfield"></td>
			</tr>
			<tr>							
				<td colspan=6 class="top_line"><b><%= Bean.clubXML.getfieldTransl("h_term_exchange", false) %></b></td>
			</tr>
			<tr>
				<td><%= Bean.clubXML.getfieldTransl("TERM_CODE_PAGE", true) %></td> <td><select name="term_code_page"  class="inputfield"><%= Bean.getMeaningFromLookupNameOptions("TERMINAL_CODE_PAGE", club.getValue("TERM_CODE_PAGE"), true) %></select></td>
				<td><%= Bean.clubXML.getfieldTransl("term_parm_time", true) %></td><td><input type="text" name="term_parm_time" size="20" value="<%= club.getValue("TERM_PARM_TIME") %>" class="inputfield"></td>
			</tr>
			<tr>
				<td><%= Bean.clubXML.getfieldTransl("def_ver_telgr", true) %></td> <td><select name="def_ver_telgr" class="inputfield"><%= Bean.getMeaningFromLookupNameOptions("VER_TELGR", club.getValue("DEF_VER_TELGR"), true) %></select></td>
				<td><%= Bean.clubXML.getfieldTransl("max_error_messages", true) %></td> <td><input type="text" name="max_error_messages" size="20" value="<%= club.getValue("MAX_ERROR_MESSAGES") %>" class="inputfield"></td>
			</tr>
			<tr>
				<td><%= Bean.clubXML.getfieldTransl("CRYPT_TELGR", true) %></td><td><select name="crypt_telgr" class="inputfield"><%= Bean.getMeaningFromLookupNumHTML("YES_NO", club.getValue("CRYPT_TELGR"), true) %></select></td>
				<td><%= Bean.clubXML.getfieldTransl("max_resp_time", true) %></td> <td><input type="text" name="max_resp_time" size="20" value="<%=club.getValue("MAX_RESP_TIME") %>" class="inputfield"></td>
			</tr>
			<tr>
				<td><%= Bean.clubXML.getfieldTransl("def_ver_key", true) %></td> <td><select name="def_ver_key" class="inputfield"><%= Bean.getMeaningFromLookupNameOptions("VER_KEY", club.getValue("DEF_VER_KEY"), true) %></select></td>
				<td><%= Bean.clubXML.getfieldTransl("exc_wtx_time", true) %></td><td><input type="text" name="exc_wtx_time" size="20" value="<%= club.getValue("EXC_WTX_TIME") %>" class="inputfield"></td>
			</tr>
			<tr>
				<td><%= Bean.loyXML.getfieldTransl("term_nincmax", true) %></td><td><input type="text" name="def_nincmax" size="20" value="<%= club.getValue("DEF_NINCMAX") %>" class="inputfield"></td>
				<td><%= Bean.loyXML.getfieldTransl("term_tr_limit", true) %></td><td><input type="text" name="def_tr_limit" size="20" value="<%= club.getValue("DEF_TR_LIMIT") %>" class="inputfield"></td>
			</tr>
			<tr>
				<td><%= Bean.clubXML.getfieldTransl("term_certificates_dir", false) %></td><td colspan=7><input type="text" name="term_certificates_dir" size="90" value="<%= club.getValue("TERM_CERTIFICATES_DIR") %>" title="<%= club.getValue("TERM_CERTIFICATES_DIR") %>" class="inputfield"></td>
			</tr>
			<tr>							
				<td colspan=6 class="top_line"><b><%= Bean.clubXML.getfieldTransl("h_transaction_processing", false) %></b></td>
			</tr>
			<tr>
				<td><%= Bean.clubXML.getfieldTransl("parse_trans_immediately", true) %></td> <td><select name="parse_trans_immediately" class="inputfield"><%= Bean.getMeaningFromLookupNameOptions("PARSE_TRANS_IMMEDIATELY", club.getValue("PARSE_TRANS_IMMEDIATELY"), true) %></select> </td>
				<td><%= Bean.clubXML.getfieldTransl("parse_trans_count", true) %></td> <td><input type="text" name="parse_trans_count" size="20" value="<%=club.getValue("PARSE_TRANS_COUNT") %>" class="inputfield"></td>
			</tr>
			<tr>
				<td><%= Bean.clubXML.getfieldTransl("account_postings_immediate", true) %></td><td><select name="account_postings_immediate" class="inputfield"><%= Bean.getMeaningFromLookupNameOptions("YES_NO", club.getValue("ACCOUNT_POSTINGS_IMMEDIATE"), true) %></select></td>
				<td><%= Bean.clubXML.getfieldTransl("parse_trans_total_time", true) %></td> <td><input type="text" name="parse_trans_total_time" size="20" value="<%=club.getValue("PARSE_TRANS_TOTAL_TIME") %>" class="inputfield"></td>
			</tr>
			<tr>
				<td><%= Bean.clubXML.getfieldTransl("parse_trans_avg_time_limit", true) %></td> <td><input type="text" name="parse_trans_avg_time_limit" size="20" value="<%=club.getValue("PARSE_TRANS_AVG_TIME_LIMIT") %>" class="inputfield"></td>
				<td><%= Bean.clubXML.getfieldTransl("parse_trans_avg_time", false) %></td> <td><input type="text" name="parse_trans_avg_time" size="20" value="<%=club.getValue("PARSE_TRANS_AVG_TIME") %>" class="inputfield-ro" readonly></td>
			</tr>

			<tr>
				<td colspan="4" class="top_line"><b><%= Bean.terminalXML.getfieldTransl("h_need_online_pin", false) %></b></td>
			</tr>
			<tr>
				<td><%= Bean.terminalXML.getfieldTransl("need_calc_online_bon_pay_pin", true) %></td> <td><select name="need_calc_online_bon_pay_pin"  class="inputfield"><%= Bean.getMeaningFromLookupNameOptions("YES_NO", club.getValue("NEED_CALC_ONLINE_BON_PAY_PIN"), true) %></select></td>
				<td colspan="2"><%= Bean.terminalXML.getfieldTransl("need_calc_online_club_pay_pin", true) %></td>
			</tr>
			<tr>
				<td><%= Bean.terminalXML.getfieldTransl("need_calc_online_storno_pin", true) %></td> <td><select name="need_calc_online_storno_pin"  class="inputfield"><%= Bean.getMeaningFromLookupNameOptions("YES_NO", club.getValue("NEED_CALC_ONLINE_STORNO_PIN"), true) %></select></td>
				<td colspan="2">&nbsp;</td>
			</tr>
			<tr>
				<td colspan="6" align="center">
					<%=Bean.getSubmitButtonAjax("../crm/club/clubupdate.jsp") %>
					<%=Bean.getResetButton() %>
					<%=Bean.getGoBackButton("../crm/club/club.jsp") %>
				</td>
			</tr>
		</table>
		</form>
<% } else if (Bean.currentMenu.isCurrentTabAndPreviewPermitted("CLUB_CLUB_INTERCHANGE_TERMINALS")) { %>
		<form>
		<table <%=Bean.getTableDetailParam() %>>
			<tr>							
				<td colspan="2"><b><%= Bean.clubXML.getfieldTransl("h_general_auth_server", false) %></b></td>
				<td colspan="2"><b><%= Bean.clubXML.getfieldTransl("h_reserve_auth_server", false) %></b></td>
			</tr>
			<tr>
				<td><%= Bean.clubXML.getfieldTransl("auth_server_host", false) %></td> <td><input type="text" name="auth_server_host" size="20" value="<%=club.getValue("AUTH_SERVER_HOST") %>" readonly="readonly" class="inputfield-ro"></td>
				<td><%= Bean.clubXML.getfieldTransl("auth_server_host", false) %></td> <td><input type="text" name="reserve_auth_server_host" size="20" value="<%=club.getValue("RESERVE_AUTH_SERVER_HOST") %>" readonly="readonly" class="inputfield-ro"></td>
			</tr>
			<tr>
				<td><%= Bean.clubXML.getfieldTransl("auth_server_port", false) %></td> <td><input type="text" name="auth_server_port" size="20" value="<%=club.getValue("AUTH_SERVER_PORT") %>" readonly="readonly" class="inputfield-ro"></td>
				<td><%= Bean.clubXML.getfieldTransl("auth_server_port", false) %></td> <td><input type="text" name="reserve_auth_server_port" size="20" value="<%=club.getValue("RESERVE_AUTH_SERVER_PORT") %>" readonly="readonly" class="inputfield-ro"></td>
			</tr>
			<tr>
				<td><%= Bean.clubXML.getfieldTransl("auth_server_charset", false) %></td> <td><input type="text" name="auth_server_charset" size="20" value="<%= Bean.getMeaningFoCodeValue("AUTH_SERVER_CODE_PAGE", club.getValue("AUTH_SERVER_CHARSET")) %>" readonly="readonly" class="inputfield-ro"></td>
				<td><%= Bean.clubXML.getfieldTransl("auth_server_charset", false) %></td> <td><input type="text" name="reserve_auth_server_charset" size="20" value="<%= Bean.getMeaningFoCodeValue("AUTH_SERVER_CODE_PAGE", club.getValue("RESERVE_AUTH_SERVER_CHARSET")) %>" readonly="readonly" class="inputfield-ro"></td>
			</tr>
			<tr>							
				<td colspan=6 class="top_line"><b><%= Bean.clubXML.getfieldTransl("h_card_processing", false) %></b></td>
			</tr>
			<tr>
				<td><%= Bean.clubXML.getfieldTransl("expect_time", false) %></td> <td><input type="text" name="expect_time" size="20" value="<%= club.getValue("EXPECT_TIME") %>" readonly="readonly" class="inputfield-ro"> </td>
				<td><%= Bean.clubXML.getfieldTransl("no_actions_with_card_max_days", false) %></td> <td><input type="text" name="no_actions_with_card_max_days" size="20" value="<%=club.getValue("NO_ACTIONS_WITH_CARD_MAX_DAYS") %>" readonly="readonly" class="inputfield-ro"></td>
			</tr>
			<tr>							
				<td colspan=6 class="top_line"><b><%= Bean.clubXML.getfieldTransl("h_term_exchange", false) %></b></td>
			</tr>
			<tr>
				<td><%= Bean.clubXML.getfieldTransl("term_code_page", false) %></td> <td><input type="text" name="term_code_page" size="20" value="<%= club.getValue("TERM_CODE_PAGE") %>" readonly="readonly" class="inputfield-ro"></td>
				<td><%= Bean.clubXML.getfieldTransl("term_parm_time", false) %> </td><td><input type="text" name="term_parm_time" size="20" value="<%= club.getValue("TERM_PARM_TIME") %>" readonly="readonly" class="inputfield-ro"></td>
			</tr>
			<tr>
				<td><%= Bean.clubXML.getfieldTransl("def_ver_telgr", false) %></td> <td><input type="text" name="def_ver_telgr" size="20" value="<%= club.getValue("DEF_VER_TELGR") %>" readonly="readonly" class="inputfield-ro"></td>
				<td><%= Bean.clubXML.getfieldTransl("max_error_messages", false) %></td> <td><input type="text" name="max_error_messages" size="20" value="<%= club.getValue("MAX_ERROR_MESSAGES") %>" readonly="readonly" class="inputfield-ro"></td>
			</tr>
			<tr>
				<td><%= Bean.clubXML.getfieldTransl("crypt_telgr", false) %></td> <td><input type="text" name="crypt_telgr" size="20" value="<%= Bean.getMeaningForNumValue("YES_NO",club.getValue("CRYPT_TELGR")) %>" readonly="readonly" class="inputfield-ro"></td>
				<td><%= Bean.clubXML.getfieldTransl("max_resp_time", false) %></td> <td><input type="text" name="max_resp_time" size="20" value="<%=club.getValue("MAX_RESP_TIME") %>" readonly="readonly" class="inputfield-ro"></td>
			</tr>
			<tr>
				<td><%= Bean.clubXML.getfieldTransl("def_ver_key", false) %></td> <td><input type="text" name="def_ver_key" size="20" value="<%= Bean.getMeaningFoCodeValue("VER_KEY", club.getValue("DEF_VER_KEY")) %>" readonly="readonly" class="inputfield-ro"></td>
				<td><%= Bean.clubXML.getfieldTransl("exc_wtx_time", false) %> </td><td><input type="text" name="exc_wtx_time" size="20" value="<%= club.getValue("EXC_WTX_TIME") %>" readonly="readonly" class="inputfield-ro"></td>
			</tr>
			<tr>
				<td><%= Bean.loyXML.getfieldTransl("term_nincmax", false) %></td><td><input type="text" name="def_nincmax" size="20" value="<%= club.getValue("DEF_NINCMAX") %>" readonly="readonly" class="inputfield-ro"></td>
				<td><%= Bean.loyXML.getfieldTransl("term_tr_limit", false) %></td><td><input type="text" name="def_tr_limit" size="20" value="<%= club.getValue("DEF_TR_LIMIT") %>" readonly="readonly" class="inputfield-ro"></td>
			</tr>
			<tr>
				<td><%= Bean.clubXML.getfieldTransl("term_certificates_dir", false) %></td><td colspan=7><input type="text" name="term_certificates_dir" size="90" value="<%= club.getValue("TERM_CERTIFICATES_DIR") %>" title="<%= club.getValue("TERM_CERTIFICATES_DIR") %>" readonly="readonly" class="inputfield-ro"></td>
			</tr>
			<tr>							
				<td colspan=6 class="top_line"><b><%= Bean.clubXML.getfieldTransl("h_transaction_processing", false) %></b></td>
			</tr>
			<tr>
				<td><%= Bean.clubXML.getfieldTransl("parse_trans_immediately", false) %></td> <td><input type="text" name="parse_trans_immediately" size="20" value="<%= Bean.getMeaningFoCodeValue("PARSE_TRANS_IMMEDIATELY", club.getValue("PARSE_TRANS_IMMEDIATELY")) %>" readonly="readonly" class="inputfield-ro"></td>
				<td><%= Bean.clubXML.getfieldTransl("parse_trans_count", false) %></td> <td><input type="text" name="parse_trans_count" size="20" value="<%=club.getValue("PARSE_TRANS_COUNT") %>" readonly="readonly" class="inputfield-ro"></td>
			</tr>
			<tr>
				<td><%= Bean.clubXML.getfieldTransl("account_postings_immediate", false) %> </td><td><input type="text" name="account_postings_immediate" size="20" value="<%= Bean.getMeaningFoCodeValue("YES_NO", club.getValue("ACCOUNT_POSTINGS_IMMEDIATE")) %>" readonly="readonly" class="inputfield-ro"></td>
				<td><%= Bean.clubXML.getfieldTransl("parse_trans_total_time", false) %></td> <td><input type="text" name="parse_trans_total_time" size="20" value="<%=club.getValue("PARSE_TRANS_TOTAL_TIME") %>" readonly="readonly" class="inputfield-ro"></td>
			</tr>
			<tr>
				<td><%= Bean.clubXML.getfieldTransl("parse_trans_avg_time_limit", false) %></td> <td><input type="text" name="parse_trans_avg_time_limit" size="20" value="<%=club.getValue("PARSE_TRANS_AVG_TIME_LIMIT") %>" readonly="readonly" class="inputfield-ro"></td>
				<td><%= Bean.clubXML.getfieldTransl("parse_trans_avg_time", false) %></td> <td><input type="text" name="parse_trans_avg_time" size="20" value="<%=club.getValue("PARSE_TRANS_AVG_TIME") %>" readonly="readonly" class="inputfield-ro"></td>
			</tr>

			<tr>
				<td colspan="4" class="top_line"><b><%= Bean.terminalXML.getfieldTransl("h_need_online_pin", false) %></b></td>
			</tr>
			<tr>
				<td><%= Bean.terminalXML.getfieldTransl("need_calc_online_bon_pay_pin", false) %></td> <td><input type="text" name="need_calc_online_bon_pay_pin" size="20" value="<%=Bean.getMeaningFoCodeValue("YES_NO", club.getValue("NEED_CALC_ONLINE_BON_PAY_PIN")) %>" readonly="readonly" class="inputfield-ro"></td>
				<td colspan="2"><%= Bean.terminalXML.getfieldTransl("need_calc_online_club_pay_pin", true) %></td>
			</tr>
			<tr>
				<td><%= Bean.terminalXML.getfieldTransl("need_calc_online_storno_pin", false) %></td> <td><input type="text" name="need_calc_online_storno_pin" size="20" value="<%=Bean.getMeaningFoCodeValue("YES_NO", club.getValue("NEED_CALC_ONLINE_STORNO_PIN")) %>" readonly="readonly" class="inputfield-ro"></td>
				<td colspan="2">&nbsp;</td>
			</tr>
			<tr>
				<td colspan="6" align="center">
					<%=Bean.getGoBackButton("../crm/club/club.jsp") %>
				</td>
			</tr>
		</table>
		</form>

<%   
}

if (Bean.currentMenu.isCurrentTabAndEditPermitted("CLUB_CLUB_FINANCE")) { %>

<script>
	var formClubParam = new Array (
		new Array ('bk_account_segments_count', 'varchar2', 1),
		new Array ('rounding_rule', 'varchar2', 1),
		new Array ('currency_rounding_rule', 'varchar2', 1),
		new Array ('posting_doc_number', 'integer', 1),
		new Array ('posting_doc_pattern', 'varchar2', 1),
		new Array ('bk_export_format', 'varchar2', 1),
		new Array ('bk_export_coding', 'varchar2', 1),
		new Array ('bk_export_dir', 'varchar2', 1),
		new Array ('clearing_doc_number', 'integer', 1),
		new Array ('clearing_doc_pattern', 'varchar2', 1),
		new Array ('group_clearing_lines', 'varchar2', 1),
		new Array ('clearing_export_format', 'varchar2', 1),
		new Array ('clearing_export_coding', 'varchar2', 1),
		new Array ('clearing_export_dir', 'varchar2', 1),
		new Array ('bank_statement_import_format', 'varchar2', 1),
		new Array ('bank_statement_import_coding', 'varchar2', 1),
		new Array ('bank_statement_import_dir', 'varchar2', 1)
	);
	function myValidateForm() {
		return validateForm(formClubParam);
	}
</script>

	<form action="../crm/club/clubupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formClubParam)">
	<input type="hidden" name="type" value="club">
	<input type="hidden" name="action" value="edit_finance">
	<input type="hidden" name="process" value="yes">
	<input type="hidden" name="id" value="<%=id %>">
	
	<table <%=Bean.getTableDetailParam() %>>
		<tr>							
			<td colspan=6><b><%= Bean.clubXML.getfieldTransl("h_bk_accounts", false) %></b></td>
		</tr>
		<tr>
			<td><%= Bean.clubXML.getfieldTransl("bk_account_segments_count", true) %></td> <td><select name="bk_account_segments_count" class="inputfield"><%= Bean.getMeaningFromLookupNumberOptions("BK_SEGMENT_COUNT", club.getValue("BK_ACCOUNT_SEGMENTS_COUNT"), false) %></select></td>
			<td colspan="2">&nbsp;</td>
		</tr>
		<tr>							
			<td colspan=6 class="top_line"><b><%= Bean.clubXML.getfieldTransl("h_posting", false) %></b></td>
		</tr>
		<tr>
			<td><%= Bean.clubXML.getfieldTransl("rounding_rule", true) %></td> <td><select name="rounding_rule" class="inputfield"><%= Bean.getMeaningFromLookupNameOptions("ROUNDING_RULE", club.getValue("ROUNDING_RULE"), true) %></select></td>
			<td><%= Bean.clubXML.getfieldTransl("currency_rounding_rule", true) %></td> <td><select name="currency_rounding_rule" class="inputfield"><%= Bean.getMeaningFromLookupNameOptions("ROUNDING_RULE", club.getValue("CURRENCY_ROUNDING_RULE"), true) %></select></td>
		</tr>

		<tr>							
			<td colspan=2 class="top_line"><b><%= Bean.clubXML.getfieldTransl("h_bk_documents", false) %></b></td>
			<td colspan=2 class="top_line"><b><%= Bean.clubXML.getfieldTransl("h_bk_export", false) %></b></td>
		</tr>
		<tr>
			<td><%= Bean.clubXML.getfieldTransl("posting_doc_number", true) %></td> <td><input type="text" name="posting_doc_number" size="20" value="<%=club.getValue("POSTING_DOC_NUMBER") %>" class="inputfield"></td>
			<td><%= Bean.clubXML.getfieldTransl("bk_export_format", true) %></td> <td><select name="bk_export_format" class="inputfield"><%= Bean.getMeaningFromLookupNameOptions("POSTINGS_EXPORT_FILE_FORMAT", club.getValue("BK_EXPORT_FORMAT"), true) %></select> </td>
		</tr>
		<tr>
			<td><%= Bean.clubXML.getfieldTransl("posting_doc_pattern", true) %></td> <td><input type="text" name="posting_doc_pattern" size="20" value="<%=club.getValue("POSTING_DOC_PATTERN") %>" class="inputfield"></td>
			<td><%= Bean.clubXML.getfieldTransl("bk_export_coding", true) %></td> <td><select name="bk_export_coding" class="inputfield"><%= Bean.getMeaningFromLookupNameOptions("FILES_CHARSET", club.getValue("BK_EXPORT_CODING"), true) %></select> </td>
		</tr>
		<tr>
			<td colspan="2">&nbsp;</td>
			<td><%= Bean.clubXML.getfieldTransl("bk_export_dir", true) %></td> <td><input type="text" name="bk_export_dir" size="30" value="<%= club.getValue("BK_EXPORT_DIR") %>" title="<%= club.getValue("BK_EXPORT_DIR") %>" class="inputfield"></td>
		</tr>
		<tr>							
			<td colspan=2 class="top_line"><b><%= Bean.clubXML.getfieldTransl("h_clearing", false) %></b></td>
			<td colspan=2 class="top_line"><b><%= Bean.clubXML.getfieldTransl("h_clearing_export", false) %></b></td>
		</tr>
		<tr>
			<td><%= Bean.clubXML.getfieldTransl("clearing_doc_number", true) %></td> <td><input type="text" name="clearing_doc_number" size="20" value="<%= club.getValue("CLEARING_DOC_NUMBER") %>" class="inputfield"> </td>
			<td><%= Bean.clubXML.getfieldTransl("clearing_export_format", true) %></td> <td><select name="clearing_export_format" class="inputfield"><%= Bean.getMeaningFromLookupNameOptions("CLIENT_BANK_INTERCHANGE_FORMAT", club.getValue("CLEARING_EXPORT_FORMAT"), true) %></select> </td>
		</tr>
		<tr>
			<td><%= Bean.clubXML.getfieldTransl("clearing_doc_pattern", true) %></td> <td><input type="text" name="clearing_doc_pattern" size="20" value="<%= club.getValue("CLEARING_DOC_PATTERN") %>" class="inputfield"> </td>
			<td><%= Bean.clubXML.getfieldTransl("clearing_export_coding", true) %></td> <td><select name="clearing_export_coding" class="inputfield"><%= Bean.getMeaningFromLookupNameOptions("FILES_CHARSET", club.getValue("CLEARING_EXPORT_CODING"), true) %></select> </td>
		</tr>
		<tr>
			<td><%= Bean.clubXML.getfieldTransl("group_clearing_lines", true) %></td> <td><select name="group_clearing_lines" class="inputfield"><%= Bean.getMeaningFromLookupNameOptions("YES_NO", club.getValue("GROUP_CLEARING_LINES"), true) %></select></td>
			<td><%= Bean.clubXML.getfieldTransl("clearing_export_dir", true) %></td> <td><input type="text" name="clearing_export_dir" size="30" value="<%= club.getValue("CLEARING_EXPORT_DIR") %>" title="<%= club.getValue("CLEARING_EXPORT_DIR") %>" class="inputfield"></td>
		</tr>
		<tr>							
			<td colspan=6 class="top_line"><b><%= Bean.clubXML.getfieldTransl("h_bank_statements_import", false) %></b></td>
		</tr>
		<tr>
			<td><%= Bean.clubXML.getfieldTransl("bank_statement_import_format", true) %></td> <td><select name="bank_statement_import_format" class="inputfield"><%= Bean.getMeaningFromLookupNameOptions("CLIENT_BANK_INTERCHANGE_FORMAT", club.getValue("BANK_STATEMENT_IMPORT_FORMAT"), true) %></select> </td>
			<td colspan="2">&nbsp;</td>
		</tr>
		<tr>
			<td><%= Bean.clubXML.getfieldTransl("bank_statement_import_coding", true) %></td> <td><select name="bank_statement_import_coding" class="inputfield"><%= Bean.getMeaningFromLookupNameOptions("FILES_CHARSET", club.getValue("bank_statement_import_CODING"), true) %></select> </td>
			<td colspan="2">&nbsp;</td>
		</tr>
		<tr>
			<td><%= Bean.clubXML.getfieldTransl("bank_statement_import_dir", true) %></td> <td><input type="text" name="bank_statement_import_dir" size="30" value="<%= club.getValue("BANK_STATEMENT_IMPORT_DIR") %>" title="<%= club.getValue("BANK_STATEMENT_IMPORT_DIR") %>" class="inputfield"></td>
			<td colspan="2">&nbsp;</td>
		</tr>
		<tr>
			<td colspan="6" align="center">
				<%=Bean.getSubmitButtonAjax("../crm/club/clubupdate.jsp") %>
				<%=Bean.getResetButton() %>
				<%=Bean.getGoBackButton("../crm/club/club.jsp") %>
			</td>
		</tr>
	</table>
	</form>
<% } else if (Bean.currentMenu.isCurrentTabAndPreviewPermitted("CLUB_CLUB_FINANCE")) { %>
	<form>
	<table <%=Bean.getTableDetailParam() %>>
		<tr>							
			<td colspan=6><b><%= Bean.clubXML.getfieldTransl("h_bk_accounts", false) %></b></td>
		</tr>
		<tr>
			<td><%= Bean.clubXML.getfieldTransl("bk_account_segments_count", false) %></td> <td><input type="text" name="bk_account_segments_count" size="20" value="<%= Bean.getMeaningFoCodeValue("BK_SEGMENT_COUNT", club.getValue("BK_ACCOUNT_SEGMENTS_COUNT")) %>" readonly="readonly" class="inputfield-ro"></td>
			<td colspan="2">&nbsp;</td>
		</tr>
		<tr>							
			<td colspan=6 class="top_line"><b><%= Bean.clubXML.getfieldTransl("h_posting", false) %></b></td>
		</tr>
		<tr>
			<td><%= Bean.clubXML.getfieldTransl("rounding_rule", false) %></td> <td><input type="text" name="rounding_rule" size="20" value="<%= Bean.getMeaningFoCodeValue("ROUNDING_RULE", club.getValue("ROUNDING_RULE")) %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.clubXML.getfieldTransl("currency_rounding_rule", false) %></td> <td><input type="text" name="currency_rounding_rule" size="20" value="<%= Bean.getMeaningFoCodeValue("ROUNDING_RULE", club.getValue("CURRENCY_ROUNDING_RULE")) %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>							
			<td colspan=2 class="top_line"><b><%= Bean.clubXML.getfieldTransl("h_bk_documents", false) %></b></td>
			<td colspan=2 class="top_line"><b><%= Bean.clubXML.getfieldTransl("h_bk_export", false) %></b></td>
		</tr>
		<tr>
			<td><%= Bean.clubXML.getfieldTransl("posting_doc_number", false) %></td> <td><input type="text" name="posting_doc_number" size="20" value="<%=club.getValue("POSTING_DOC_NUMBER") %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.clubXML.getfieldTransl("bk_export_format", false) %></td> <td><input type="text" name="bk_export_format" size="20" value="<%= Bean.getMeaningFoCodeValue("POSTINGS_EXPORT_FILE_FORMAT", club.getValue("BK_EXPORT_FORMAT")) %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td><%= Bean.clubXML.getfieldTransl("posting_doc_pattern", false) %></td> <td><input type="text" name="posting_doc_pattern" size="20" value="<%=club.getValue("POSTING_DOC_PATTERN") %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.clubXML.getfieldTransl("bk_export_coding", false) %></td> <td><input type="text" name="bk_export_coding" size="20" value="<%= Bean.getMeaningFoCodeValue("FILES_CHARSET", club.getValue("BK_EXPORT_CODING")) %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td colspan="2">&nbsp;</td>
			<td><%= Bean.clubXML.getfieldTransl("bk_export_dir", false) %></td> <td><input type="text" name="bk_export_dir" size="30" value="<%= club.getValue("BK_EXPORT_DIR") %>" title="<%= club.getValue("BK_EXPORT_DIR") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>							
			<td colspan=2 class="top_line"><b><%= Bean.clubXML.getfieldTransl("h_clearing", false) %></b></td>
			<td colspan=2 class="top_line"><b><%= Bean.clubXML.getfieldTransl("h_clearing_export", false) %></b></td>
		</tr>
		<tr>
			<td><%= Bean.clubXML.getfieldTransl("clearing_doc_number", false) %></td> <td><input type="text" name="clearing_doc_number" size="20" value="<%= club.getValue("CLEARING_DOC_NUMBER") %>" readonly="readonly" class="inputfield-ro"> </td>
			<td><%= Bean.clubXML.getfieldTransl("clearing_export_format", false) %></td> <td><input type="text" name="clearing_export_format" size="20" value="<%= Bean.getMeaningFoCodeValue("CLIENT_BANK_INTERCHANGE_FORMAT", club.getValue("CLEARING_EXPORT_FORMAT")) %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td><%= Bean.clubXML.getfieldTransl("clearing_doc_pattern", false) %></td> <td><input type="text" name="clearing_doc_pattern" size="20" value="<%= club.getValue("CLEARING_DOC_PATTERN") %>" readonly="readonly" class="inputfield-ro"> </td>
			<td><%= Bean.clubXML.getfieldTransl("clearing_export_coding", false) %></td> <td><input type="text" name="clearing_export_coding" size="20" value="<%= Bean.getMeaningFoCodeValue("FILES_CHARSET", club.getValue("CLEARING_EXPORT_CODING")) %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td><%= Bean.clubXML.getfieldTransl("group_clearing_lines", false) %></td> <td><input type="text" name="group_clearing_lines" size="20" value="<%= Bean.getMeaningFoCodeValue("YES_NO",club.getValue("GROUP_CLEARING_LINES")) %>" readonly="readonly" class="inputfield-ro"> </td>
			<td><%= Bean.clubXML.getfieldTransl("clearing_export_dir", false) %></td> <td><input type="text" name="clearing_export_dir" size="30" value="<%= club.getValue("CLEARING_EXPORT_DIR") %>" title="<%= club.getValue("CLEARING_EXPORT_DIR") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>							
			<td colspan=6 class="top_line"><b><%= Bean.clubXML.getfieldTransl("h_bank_statements_import", false) %></b></td>
		</tr>
		<tr>
			<td><%= Bean.clubXML.getfieldTransl("bank_statement_import_format", false) %></td> <td><input type="text" name="bank_statement_import_format" size="20" value="<%= Bean.getMeaningFoCodeValue("CLIENT_BANK_INTERCHANGE_FORMAT", club.getValue("BANK_STATEMENT_IMPORT_FORMAT")) %>" readonly="readonly" class="inputfield-ro"></td>
			<td colspan="2">&nbsp;</td>
		</tr>
		<tr>
			<td><%= Bean.clubXML.getfieldTransl("bank_statement_import_coding", false) %></td> <td><input type="text" name="bank_statement_import_coding" size="20" value="<%= Bean.getMeaningFoCodeValue("FILES_CHARSET", club.getValue("bank_statement_import_CODING")) %>" readonly="readonly" class="inputfield-ro"></td>
			<td colspan="2">&nbsp;</td>
		</tr>
		<tr>
			<td><%= Bean.clubXML.getfieldTransl("bank_statement_import_dir", false) %></td> <td><input type="text" name="bank_statement_import_dir" size="30" value="<%= club.getValue("BANK_STATEMENT_IMPORT_DIR") %>" title="<%= club.getValue("BANK_STATEMENT_IMPORT_DIR") %>" readonly="readonly" class="inputfield-ro"></td>
			<td colspan="2">&nbsp;</td>
		</tr>
		<tr>
			<td colspan="6" align="center">
				<%=Bean.getGoBackButton("../crm/club/club.jsp") %>
			</td>
		</tr>
	</table>
	</form>

<%   
}

if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLUB_CLUB_PARTICIPANT")) { %>
<script type="text/javascript">
 	function CheckCB(Element) {
		myCheck = true;

		thisCheckBoxes = document.getElementsByTagName('input');
		for (i = 1; i < thisCheckBoxes.length; i++) { 
			myName = thisCheckBoxes[i].name;
			if (myName.substr(0,6) == 'chb_id' || myName.substr(0,6) == 'chb_sp'){
				myCheck = myCheck && thisCheckBoxes[i].checked;
			}
		}
		if (document.getElementById('mainCheck')) {
			document.getElementById('mainCheck').checked = myCheck;
		}
	}
	function CheckAll(Element) {
		thisCheckBoxes = document.getElementsByTagName('input');
		for (i = 1; i < thisCheckBoxes.length; i++) { 
			myName = thisCheckBoxes[i].name;
			
			if (myName.substr(0,6) == 'chb_id' || myName.substr(0,6) == 'chb_sp'){
					thisCheckBoxes[i].checked = document.getElementById('mainCheck').checked;
			}
		}
	}
	CheckCB();
</script>

	<table <%= Bean.getTableBottomFilter() %>>
		<tr>
		<%= Bean.getFindHTML("participant_find", participant_find, "../crm/club/clubspecs.jsp?id=" + id + "&participant_page=1") %>

		<%=Bean.getSelectOnChangeBeginHTML("participant_type", "../crm/club/clubspecs.jsp?id=" + id + "&participant_page=1", Bean.clubXML.getfieldTransl("club_member_status", false)) %>
			<%= Bean.getClubMemberStatusOptions(participant_type, true) %>
		<%=Bean.getSelectOnChangeEndHTML() %>

		<%=Bean.getSelectOnChangeBeginHTML("participant_selected", "../crm/club/clubspecs.jsp?id=" + id + "&participant_page=1&user_selected=", Bean.clubXML.getfieldTransl("is_club_member", false)) %>
			<%=Bean.getSelectOptionHTML(participant_selected, "", "") %>
			<%=Bean.getSelectOptionHTML(participant_selected, "Y", Bean.commonXML.getfieldTransl("yes", false)) %>
			<%=Bean.getSelectOptionHTML(participant_selected, "N", Bean.commonXML.getfieldTransl("no", false)) %>
		<%=Bean.getSelectOnChangeEndHTML() %>

		</tr>
	</table>
 
	<%= club.getClubParticipantHTML(participant_find, participant_type, participant_selected, l_participant_page_beg, l_participant_page_end, Bean.getJSPDateFormat()) %> 
<%}

if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLUB_CLUB_FUNDS")) {%>
	<table <%= Bean.getTableBottomFilter() %>>
		<tr>
		<%= Bean.getFindHTML("fund_find", fund_find, "../crm/club/clubspecs.jsp?id=" + id + "&fund_page=1") %>

		</tr>
	</table>
	<%= club.getClubFundsHTML(fund_find, l_fund_page_beg, l_fund_page_end) %>
<%}

if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLUB_CLUB_TARGET_PROGRAM")) {%>
	<table <%= Bean.getTableBottomFilter() %>>
		<tr>
		<%= Bean.getFindHTML("target_program_find", target_program_find, "../crm/club/clubspecs.jsp?id=" + id + "&target_program_page=1") %>
		
		<%=Bean.getSelectOnChangeBeginHTML("target_program_pay_period", "../crm/club/clubspecs.jsp?id=" + id + "&target_program_page=1&", Bean.clubfundXML.getfieldTransl("target_prg_pay_period", false)) %>
			<%= Bean.getTargetPrgPayPeriodOptions(target_program_pay_period, true) %>
		<%=Bean.getSelectOnChangeEndHTML() %>
		</tr>
	</table>
	<%= club.getTargetProgramsHTML(target_program_find, target_program_pay_period, l_target_program_page_beg, l_target_program_page_end) %>
<%}

if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLUB_CLUB_ACCESS")) {%>
<script type="text/javascript">
 	function CheckCB(Element) {
		myCheck = true;

		thisCheckBoxes = document.getElementsByTagName('input');
		for (i = 1; i < thisCheckBoxes.length; i++) { 
			myName = thisCheckBoxes[i].name;
			if (myName.substr(0,6) == 'chb_id'){
				myCheck = myCheck && thisCheckBoxes[i].checked;
			}
		}
		if (document.getElementById('mainCheck')) {
			document.getElementById('mainCheck').checked = myCheck;
		}
	}
	function CheckAll(Element,Name) {
		thisCheckBoxes = document.getElementsByTagName('input');
		for (i = 1; i < thisCheckBoxes.length; i++) { 
			myName = thisCheckBoxes[i].name;
			
			if (myName.substr(0,6) == Name){
					thisCheckBoxes[i].checked = document.getElementById('mainCheck').checked;
			}
		}
	}
	CheckCB();
</script>
	<table <%= Bean.getTableBottomFilter() %>>
		<tr>
		<%= Bean.getFindHTML("user_find", user_find, "../crm/club/clubspecs.jsp?id=" + id + "&access_page=1") %>

		<%=Bean.getSelectOnChangeBeginHTML("user_status", "../crm/club/clubspecs.jsp?id=" + id + "&access_page=1", Bean.userXML.getfieldTransl("name_user_status", false)) %>
			<%= Bean.getUserStatusOptions(user_status, true) %>
		<%=Bean.getSelectOnChangeEndHTML() %>

		<%=Bean.getSelectOnChangeBeginHTML("user_selected", "../crm/club/clubspecs.jsp?id=" + id + "&access_page=1&user_selected=", Bean.userXML.getfieldTransl("club_data_access", false)) %>
			<%=Bean.getSelectOptionHTML(user_selected, "", "") %>
			<%=Bean.getSelectOptionHTML(user_selected, "Y", Bean.commonXML.getfieldTransl("yes", false)) %>
			<%=Bean.getSelectOptionHTML(user_selected, "N", Bean.commonXML.getfieldTransl("no", false)) %>
		<%=Bean.getSelectOnChangeEndHTML() %>
		</tr>
	</table>
	<%= club.getUserPrivilegesHTML(user_find, user_status, user_selected, l_access_page_beg, l_access_page_end) %>
<%}
}
%>

</div></div>
</body>
</html>
