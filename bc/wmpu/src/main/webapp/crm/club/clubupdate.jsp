<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />
<%@page import="bc.objects.bcContactsObject"%>
<%@page import="bc.objects.bcDocumentObject"%>
<%@page import="bc.objects.bcBankAccountObject"%>
<%@page import="java.util.Enumeration"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Set"%>
<%@page import="java.util.Iterator"%>
<%@page import="bc.objects.bcClubShortObject"%>
<%@page import="bc.objects.bcClubOnlinePaymentTypeObject"%>
<%@page import="bc.objects.bcClubObject"%>
<%@page import="bc.objects.bcClubTargetProgramObject"%>
<%@page import="java.util.Date"%><html>

<%= Bean.getLogOutScript(request) %>

<head>
	<%= Bean.getMetaContent() %>
	<%= Bean.getBottomFrameCSS() %>

</head>

<body>
<%
HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String pageFormName = "CLUB_CLUB";

Bean.setJspPageForTabName(pageFormName);

request.setCharacterEncoding("UTF-8");

String p_id		= Bean.getDecodeParamPrepare(parameters.get("id"));
String type		= Bean.getDecodeParamPrepare(parameters.get("type"));
String action	= Bean.getDecodeParamPrepare(parameters.get("action")); 
String process	= Bean.getDecodeParamPrepare(parameters.get("process"));

if (type.equalsIgnoreCase("club")) {
	if (process.equalsIgnoreCase("no")) {
		if (action.equalsIgnoreCase("add") || action.equalsIgnoreCase("add2")) { %>

			<%= Bean.getOperationTitle(
					Bean.clubXML.getfieldTransl("H_ADD_CLUB", false),
					"Y",
					"Y") 
			%>
			<script>
				var formClubParam = new Array (
					new Array ('cd_club', 'varchar2', 1),
					new Array ('name_club', 'varchar2', 1),
					new Array ('sname_club', 'varchar2', 1),
					new Array ('name_operator', 'varchar2', 1),
					new Array ('cd_country_def', 'varchar2', 1),
					new Array ('name_clearing_bank', 'varchar2', 1),
					new Array ('cd_currency_base', 'varchar2', 1),
					new Array ('def_ver_telgr', 'varchar2', 1),
					new Array ('max_error_messages', 'integer', 1),
					new Array ('def_ver_key', 'varchar2', 1),
					new Array ('exc_wtx_time', 'integer', 1),
					new Array ('term_parm_time', 'varchar2', 1),
					new Array ('parse_trans_immediately', 'varchar2', 1),
					new Array ('parse_trans_count', 'integer', 1),
					new Array ('parse_trans_total_time', 'integer', 1),
					new Array ('parse_trans_avg_time_limit', 'integer', 1),
					new Array ('auth_server_host', 'varchar2', 1),
					new Array ('auth_server_port', 'integer', 1),
					new Array ('auth_server_charset', 'varchar2', 1),
					new Array ('reserve_auth_server_host', 'varchar2', 1),
					new Array ('reserve_auth_server_port', 'integer', 1),
					new Array ('reserve_auth_server_charset', 'varchar2', 1),
					new Array ('bk_account_segments_count', 'varchar2', 1),
					new Array ('rounding_rule', 'varchar2', 1),
					new Array ('posting_doc_number', 'integer', 1),
					new Array ('account_postings_immediate', 'varchar2', 1),
					new Array ('posting_doc_pattern', 'varchar2', 1),
					new Array ('bk_export_coding', 'varchar2', 1),
					new Array ('currency_rounding_rule', 'varchar2', 1),
					new Array ('clearing_doc_number', 'integer', 1),
					new Array ('clearing_export_format', 'varchar2', 1),
					new Array ('bank_statement_import_format', 'varchar2', 1),
					new Array ('clearing_doc_pattern', 'varchar2', 1),
					new Array ('clearing_export_coding', 'varchar2', 1),
					new Array ('bank_statement_import_coding', 'varchar2', 1),
					new Array ('expect_time', 'integer', 1),
					new Array ('max_resp_time', 'integer', 1),
					new Array ('no_actions_with_card_max_days', 'varchar2', 1),
					new Array ('group_clearing_lines', 'varchar2', 1),
					new Array ('term_code_page', 'varchar2', 1),
					new Array ('crypt_telgr', 'varchar2', 1),
					new Array ('def_tr_limit', 'varchar2', 1),
					new Array ('def_nincmax', 'varchar2', 1),
					new Array ('need_calc_online_bon_pay_pin', 'varchar2', 1),
					new Array ('need_calc_online_storno_pin', 'varchar2', 1),
					new Array ('shareholder_point_percent', 'varchar2', 1)
				);
				function myValidateForm() {
					return validateForm(formClubParam);
				}
			</script>

	<% String outputdir = Bean.getSystemParamValue("UPLOADED_FILES_DIR");
	%>
	<form action="../crm/club/clubupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formClubParam)">
		<input type="hidden" name="type" value="club">
	    <input type="hidden" name="action" value="add">
	    <input type="hidden" name="process" value="yes">
	<table <%=Bean.getTableDetailParam() %>>
		<tr>							
			<td colspan=6><b><%= Bean.clubXML.getfieldTransl("h_general_parameters", false) %></b></td>
		</tr>
		<tr>
			<td><%= Bean.clubXML.getfieldTransl("cd_club", true) %> </td><td><input type="text" name="cd_club" size="20" value="" class="inputfield"></td>
		</tr>
		<tr>
			<td><%= Bean.clubXML.getfieldTransl("name_club", true) %> </td><td  colspan="3"><input type="text" name="name_club" size="70" value="" class="inputfield"></td>
		</tr>
		<tr>
			<td><%= Bean.clubXML.getfieldTransl("sname_club", true) %> </td><td><input type="text" name="sname_club" size="30" value="" class="inputfield"></td>
			<td><%= Bean.clubXML.getfieldTransl("name_country_def", true) %></td> <td><select name="cd_country_def" class="inputfield"><%= Bean.getCountryOptions("", true) %></select></td>
		</tr>
		<tr>
			<td><%= Bean.clubXML.getfieldTransl("name_operator", true) %></td>
			  <td>
				  <%=Bean.getWindowFindJurPrs("operator", "", "", "OPERATOR", "25") %>
			  </td>
			<td><%= Bean.clubXML.getfieldTransl("cd_currency_base", true) %></td> <td><select name="cd_currency_base" class="inputfield"><%= Bean.getCurrencyOptions("", true) %></select></td>
		</tr>
		<tr>
			<td><%= Bean.clubXML.getfieldTransl("name_clearing_bank", true) %></td> 
			  <td>
				  <%=Bean.getWindowFindJurPrs("clearing_bank", "", "", "BANK", "25") %>
			  </td>
			<td><%= Bean.clubXML.getfieldTransl("shareholder_point_percent", true) %> </td><td><input type="text" name="shareholder_point_percent" size="10" value="" class="inputfield"></td>
		</tr>
		<tr>							
			<td colspan=6 class="top_line"><b><%= Bean.clubXML.getfieldTransl("h_general_auth_server", false) %></b></td>
		</tr>
		<tr>
			<td><%= Bean.clubXML.getfieldTransl("auth_server_host", true) %></td> <td><input type="text" name="auth_server_host" size="20" value="" class="inputfield"></td>
			<td><%= Bean.clubXML.getfieldTransl("auth_server_charset", true) %></td> <td><select name="auth_server_charset" class="inputfield"><%=Bean.getMeaningFromLookupNameOptions("AUTH_SERVER_CODE_PAGE", "", false) %></select></td>
		</tr>
		<tr>
			<td><%= Bean.clubXML.getfieldTransl("auth_server_port", true) %></td> <td><input type="text" name="auth_server_port" size="20" value="" class="inputfield"></td>
			<td>&nbsp;</td> <td>&nbsp;</td>
		</tr>
		<tr>							
			<td colspan=6 class="top_line"><b><%= Bean.clubXML.getfieldTransl("h_reserve_auth_server", false) %></b></td>
		</tr>
		<tr>
			<td><%= Bean.clubXML.getfieldTransl("auth_server_host", true) %></td> <td><input type="text" name="reserve_auth_server_host" size="20" value="" class="inputfield"></td>
			<td><%= Bean.clubXML.getfieldTransl("auth_server_charset", true) %></td> <td><select name="reserve_auth_server_charset" class="inputfield"><%=Bean.getMeaningFromLookupNameOptions("AUTH_SERVER_CODE_PAGE", "", false) %></select></td>
		</tr>
		<tr>
			<td><%= Bean.clubXML.getfieldTransl("auth_server_port", true) %></td> <td><input type="text" name="reserve_auth_server_port" size="20" value="" class="inputfield"></td>
			<td>&nbsp;</td> <td>&nbsp;</td>
		</tr>
		<tr>							
			<td colspan=6 class="top_line"><b><%= Bean.clubXML.getfieldTransl("h_card_processing", false) %></b></td>
		</tr>
		<tr>
			<td><%= Bean.clubXML.getfieldTransl("expect_time", true) %></td> <td><input type="text" name="expect_time" size="20" value="" class="inputfield"> </td>
			<td><%= Bean.clubXML.getfieldTransl("no_actions_with_card_max_days", true) %></td> <td><input type="text" name="no_actions_with_card_max_days" size="20" value="" class="inputfield"></td>
		</tr>
		<tr>							
			<td colspan=6 class="top_line"><b><%= Bean.clubXML.getfieldTransl("h_term_exchange", false) %></b></td>
		</tr>
		<tr>
			<td><%= Bean.clubXML.getfieldTransl("TERM_CODE_PAGE", true) %></td> <td><select name="term_code_page"  class="inputfield"><%= Bean.getMeaningFromLookupNameOptions("TERMINAL_CODE_PAGE", "", true) %></select></td>
			<td><%= Bean.clubXML.getfieldTransl("term_parm_time", true) %></td><td><input type="text" name="term_parm_time" size="20" value="" class="inputfield"></td>
		</tr>
		<tr>
			<td><%= Bean.clubXML.getfieldTransl("def_ver_telgr", true) %></td> <td><select name="def_ver_telgr" class="inputfield"><%= Bean.getMeaningFromLookupNameOptions("VER_TELGR", "", false) %></select></td>
			<td><%= Bean.clubXML.getfieldTransl("max_error_messages", true) %></td> <td><input type="text" name="max_error_messages" size="20" value="" class="inputfield"></td>
		</tr>
		<tr>
			<td><%= Bean.clubXML.getfieldTransl("CRYPT_TELGR", true) %></td><td><select name="crypt_telgr" class="inputfield"><%= Bean.getMeaningFromLookupNumHTML("YES_NO", "", true) %></select></td>
			<td><%= Bean.clubXML.getfieldTransl("max_resp_time", true) %></td> <td><input type="text" name="max_resp_time" size="20" value="" class="inputfield"></td>
		</tr>
		<tr>
			<td><%= Bean.clubXML.getfieldTransl("def_ver_key", true) %></td> <td><select name="def_ver_key" class="inputfield"><%= Bean.getMeaningFromLookupNameOptions("VER_KEY", "", false) %></select></td>
			<td><%= Bean.clubXML.getfieldTransl("exc_wtx_time", true) %></td><td><input type="text" name="exc_wtx_time" size="20" value="" class="inputfield"></td>
		</tr>
		<tr>
			<td><%= Bean.loyXML.getfieldTransl("term_nincmax", true) %></td><td><input type="text" name="def_nincmax" size="20" value="" class="inputfield"></td>
			<td><%= Bean.loyXML.getfieldTransl("term_tr_limit", true) %></td><td><input type="text" name="def_tr_limit" size="20" value="" class="inputfield"></td>
		</tr>
		<tr>							
			<td colspan=6 class="top_line"><b><%= Bean.clubXML.getfieldTransl("h_transaction_processing", false) %></b></td>
		</tr>
		<tr>
			<td><%= Bean.clubXML.getfieldTransl("parse_trans_immediately", true) %></td> <td><select name="parse_trans_immediately" class="inputfield"><%= Bean.getMeaningFromLookupNameOptions("PARSE_TRANS_IMMEDIATELY", "", false) %></select> </td>
			<td><%= Bean.clubXML.getfieldTransl("parse_trans_count", true) %></td> <td><input type="text" name="parse_trans_count" size="20" value="" class="inputfield"></td>
		</tr>
		<tr>
			<td><%= Bean.clubXML.getfieldTransl("account_postings_immediate", true) %></td><td><select name="account_postings_immediate" class="inputfield"><%= Bean.getMeaningFromLookupNameOptions("YES_NO", "", false) %></select></td>
			<td><%= Bean.clubXML.getfieldTransl("parse_trans_total_time", true) %></td> <td><input type="text" name="parse_trans_total_time" size="20" value="" class="inputfield"></td>
		</tr>
		<tr>
			<td><%= Bean.clubXML.getfieldTransl("parse_trans_avg_time_limit", true) %></td> <td><input type="text" name="parse_trans_avg_time_limit" size="20" value="" class="inputfield"></td>
			<td colspan="2">&nbsp;</td>
		</tr>
		<tr>							
			<td colspan=6><b><%= Bean.clubXML.getfieldTransl("h_bk_accounts", false) %></b></td>
		</tr>
		<tr>
			<td><%= Bean.clubXML.getfieldTransl("bk_account_segments_count", true) %></td> <td><select name="bk_account_segments_count" class="inputfield"><%= Bean.getMeaningFromLookupNumberOptions("BK_SEGMENT_COUNT", "5", false) %></select></td>
			<td colspan="2">&nbsp;</td>
		</tr>
		<tr>							
			<td colspan=6 class="top_line"><b><%= Bean.clubXML.getfieldTransl("h_posting", false) %></b></td>
		</tr>
		<tr>
			<td><%= Bean.clubXML.getfieldTransl("rounding_rule", true) %></td> <td><select name="rounding_rule" class="inputfield"><%= Bean.getMeaningFromLookupNameOptions("ROUNDING_RULE", "NEAREST", true) %></select></td>
			<td><%= Bean.clubXML.getfieldTransl("currency_rounding_rule", true) %></td> <td><select name="currency_rounding_rule" class="inputfield"><%= Bean.getMeaningFromLookupNameOptions("ROUNDING_RULE", "NEAREST", true) %></select></td>
		</tr>

		<tr>							
			<td colspan=6 class="top_line"><b><%= Bean.clubXML.getfieldTransl("h_bk_documents", false) %></b></td>
		</tr>
		<tr>
			<td><%= Bean.clubXML.getfieldTransl("posting_doc_number", true) %></td> <td><input type="text" name="posting_doc_number" size="20" value="1" class="inputfield"></td>
			<td><%= Bean.clubXML.getfieldTransl("bk_export_format", false) %></td> <td><select name="bk_export_format" class="inputfield"><%= Bean.getMeaningFromLookupNameOptions("POSTINGS_EXPORT_FILE_FORMAT", "", true) %></select> </td>
		</tr>
		<tr>
			<td><%= Bean.clubXML.getfieldTransl("posting_doc_pattern", true) %></td> <td><input type="text" name="posting_doc_pattern" size="20" value="" class="inputfield"></td>
			<td><%= Bean.clubXML.getfieldTransl("bk_export_coding", true) %></td> <td><select name="bk_export_coding" class="inputfield"><%= Bean.getMeaningFromLookupNameOptions("FILES_CHARSET", "", true) %></select> </td>
		</tr>
		<tr>							
			<td colspan=6 class="top_line"><b><%= Bean.clubXML.getfieldTransl("h_clearing", false) %></b></td>
		</tr>
		<tr>
			<td><%= Bean.clubXML.getfieldTransl("clearing_doc_number", true) %></td> <td><input type="text" name="clearing_doc_number" size="20" value="1" class="inputfield"> </td>
			<td><%= Bean.clubXML.getfieldTransl("clearing_export_format", true) %></td> <td><select name="clearing_export_format" class="inputfield"><%= Bean.getMeaningFromLookupNameOptions("CLIENT_BANK_INTERCHANGE_FORMAT", "", true) %></select> </td>
		</tr>
		<tr>
			<td><%= Bean.clubXML.getfieldTransl("clearing_doc_pattern", true) %></td> <td><input type="text" name="clearing_doc_pattern" size="20" value="" class="inputfield"> </td>
			<td><%= Bean.clubXML.getfieldTransl("clearing_export_coding", true) %></td> <td><select name="clearing_export_coding" class="inputfield"><%= Bean.getMeaningFromLookupNameOptions("FILES_CHARSET", "", true) %></select> </td>
		</tr>
		<tr>
			<td><%= Bean.clubXML.getfieldTransl("group_clearing_lines", true) %></td> <td><select name="group_clearing_lines" class="inputfield"><%= Bean.getMeaningFromLookupNameOptions("YES_NO", "N", true) %></select></td>
			<td colspan="2">&nbsp;</td>
		</tr>
		<tr>							
			<td colspan=6 class="top_line"><b><%= Bean.clubXML.getfieldTransl("h_bank_statements", false) %></b></td>
		</tr>
		<tr>
			<td><%= Bean.clubXML.getfieldTransl("bank_statement_import_format", true) %></td> <td><select name="bank_statement_import_format" class="inputfield"><%= Bean.getMeaningFromLookupNameOptions("CLIENT_BANK_INTERCHANGE_FORMAT", "", true) %></select> </td>
			<td colspan="2">&nbsp;</td>
		</tr>
		<tr>
			<td><%= Bean.clubXML.getfieldTransl("bank_statement_import_coding", true) %></td> <td><select name="bank_statement_import_coding" class="inputfield"><%= Bean.getMeaningFromLookupNameOptions("FILES_CHARSET", "", true) %></select> </td>
			<td colspan="2">&nbsp;</td>
		</tr>

		<tr>
			<td colspan="4" class="top_line"><b><%= Bean.terminalXML.getfieldTransl("h_need_online_pin", false) %></b></td>
		</tr>
		<tr>
			<td><%= Bean.terminalXML.getfieldTransl("need_calc_online_bon_pay_pin", true) %></td> <td><select name="need_calc_online_bon_pay_pin"  class="inputfield"><%= Bean.getMeaningFromLookupNameOptions("YES_NO", "Y", true) %></select></td>
			<td colspan="2"><%= Bean.terminalXML.getfieldTransl("need_calc_online_club_pay_pin", true) %></td>
		</tr>
		<tr>
			<td><%= Bean.terminalXML.getfieldTransl("need_calc_online_storno_pin", true) %></td> <td><select name="need_calc_online_storno_pin"  class="inputfield"><%= Bean.getMeaningFromLookupNameOptions("YES_NO", "Y", true) %></select></td>
			<td colspan="2">&nbsp;</td>
		</tr>
 		<tr>
			<td colspan="6" align="center">
				<%=Bean.getSubmitButtonAjax("../crm/club/clubupdate.jsp") %>
				<%=Bean.getResetButton() %>
				<% if ("add".equalsIgnoreCase(action)) { %>
					<%=Bean.getGoBackButton("../crm/club/club.jsp") %>
				<% } else { %>
					<%=Bean.getGoBackButton("../crm/club/clubspecs.jsp?id=" + p_id) %>
				<% } %>
			</td>
		</tr>
	</table>
	</form>
		<% 	} else { %>
			<%= Bean.getUnknownActionText(action) %><% 
		}
	} else if (process.equalsIgnoreCase("yes")) {
		String
	    	cd_club 						= Bean.getDecodeParam(parameters.get("cd_club")), 
	    	name_club 						= Bean.getDecodeParam(parameters.get("name_club")), 
	    	sname_club 						= Bean.getDecodeParam(parameters.get("sname_club")), 
	    	id_operator						= Bean.getDecodeParam(parameters.get("id_operator")), 
	    	id_clearing_bank 				= Bean.getDecodeParam(parameters.get("id_clearing_bank")), 
	    	cd_currency_base 				= Bean.getDecodeParam(parameters.get("cd_currency_base")),
	    	account_postings_immediate 		= Bean.getDecodeParam(parameters.get("account_postings_immediate")), 
	    	cd_country_def 					= Bean.getDecodeParam(parameters.get("cd_country_def")),
	    	currency_rounding_rule 			= Bean.getDecodeParam(parameters.get("currency_rounding_rule")), 
	    	exc_wtx_time 					= Bean.getDecodeParam(parameters.get("exc_wtx_time")), 
	    	expect_time 					= Bean.getDecodeParam(parameters.get("expect_time")), 
	    	max_error_messages 				= Bean.getDecodeParam(parameters.get("max_error_messages")), 
	    	rounding_rule 					= Bean.getDecodeParam(parameters.get("rounding_rule")), 
	    	term_parm_time 					= Bean.getDecodeParam(parameters.get("term_parm_time")), 
	    	def_ver_key 					= Bean.getDecodeParam(parameters.get("def_ver_key")), 
	    	def_ver_telgr 					= Bean.getDecodeParam(parameters.get("def_ver_telgr")),
	    	bk_account_segments_count 		= Bean.getDecodeParam(parameters.get("bk_account_segments_count")),
	    	clearing_doc_number 			= Bean.getDecodeParam(parameters.get("clearing_doc_number")),
	    	clearing_doc_pattern 			= Bean.getDecodeParam(parameters.get("clearing_doc_pattern")),
	    	bk_export_format 				= Bean.getDecodeParam(parameters.get("bk_export_format")),
	    	bk_export_coding 				= Bean.getDecodeParam(parameters.get("bk_export_coding")),
	    	//bk_import_format 				= Bean.getDecodeParam(parameters.get("bk_import_format")),
	    	//bk_import_dir 				= Bean.getDecodeParam(parameters.get("bk_import_dir")),
	    	//bk_import_coding 				= Bean.getDecodeParam(parameters.get("bk_import_coding")),
	    	clearing_export_format 			= Bean.getDecodeParam(parameters.get("clearing_export_format")),
	    	clearing_export_coding 			= Bean.getDecodeParam(parameters.get("clearing_export_coding")),
	    	bank_statement_import_format 	= Bean.getDecodeParam(parameters.get("bank_statement_import_format")),
	    	bank_statement_import_coding 	= Bean.getDecodeParam(parameters.get("bank_statement_import_coding")),
	    	auth_server_host 				= Bean.getDecodeParam(parameters.get("auth_server_host")),
	    	auth_server_port 				= Bean.getDecodeParam(parameters.get("auth_server_port")),
	    	auth_server_charset 			= Bean.getDecodeParam(parameters.get("auth_server_charset")),
	    	reserve_auth_server_host 		= Bean.getDecodeParam(parameters.get("reserve_auth_server_host")),
	    	reserve_auth_server_port 		= Bean.getDecodeParam(parameters.get("reserve_auth_server_port")),
	    	reserve_auth_server_charset 	= Bean.getDecodeParam(parameters.get("reserve_auth_server_charset")),
	    	parse_trans_immediately 		= Bean.getDecodeParam(parameters.get("parse_trans_immediately")),
	    	parse_trans_count 				= Bean.getDecodeParam(parameters.get("parse_trans_count")),
	    	parse_trans_total_time 			= Bean.getDecodeParam(parameters.get("parse_trans_total_time")),
	    	parse_trans_avg_time_limit 		= Bean.getDecodeParam(parameters.get("parse_trans_avg_time_limit")),
	    	max_resp_time 					= Bean.getDecodeParam(parameters.get("max_resp_time")),
	    	no_actions_with_card_max_days 	= Bean.getDecodeParam(parameters.get("no_actions_with_card_max_days")),
	    	posting_doc_number 				= Bean.getDecodeParam(parameters.get("posting_doc_number")),
	    	posting_doc_pattern 			= Bean.getDecodeParam(parameters.get("posting_doc_pattern")),
	    	group_clearing_lines 			= Bean.getDecodeParam(parameters.get("group_clearing_lines")),
	    	term_code_page		 			= Bean.getDecodeParam(parameters.get("term_code_page")),
	    	crypt_telgr			 			= Bean.getDecodeParam(parameters.get("crypt_telgr")),
		    def_nincmax						= Bean.getDecodeParam(parameters.get("def_nincmax")),
		    def_tr_limit					= Bean.getDecodeParam(parameters.get("def_tr_limit")),
		    need_calc_online_bon_pay_pin	= Bean.getDecodeParam(parameters.get("need_calc_online_bon_pay_pin")),
		    need_calc_online_storno_pin		= Bean.getDecodeParam(parameters.get("need_calc_online_storno_pin")),
		    shareholder_point_percent		= Bean.getDecodeParam(parameters.get("shareholder_point_percent")),
		    id_nat_prs_role_def_refferal	= Bean.getDecodeParam(parameters.get("id_nat_prs_role_def_refferal")),
		    term_certificates_dir			= Bean.getDecodeParam(parameters.get("term_certificates_dir")),
		    bk_export_dir					= Bean.getDecodeParam(parameters.get("bk_export_dir")),
		    clearing_export_dir				= Bean.getDecodeParam(parameters.get("clearing_export_dir")),
		    bank_statement_import_dir		= Bean.getDecodeParam(parameters.get("bank_statement_import_dir"));

		if (action.equalsIgnoreCase("add")) {
			
			ArrayList<String> pParam = new ArrayList<String>();
							
			pParam.add(cd_club);
			pParam.add(name_club);
			pParam.add(sname_club);
			pParam.add(id_operator);
			pParam.add(id_clearing_bank);
			pParam.add(cd_currency_base);
			pParam.add(cd_country_def);
			pParam.add(shareholder_point_percent);
			pParam.add(account_postings_immediate);
			pParam.add(currency_rounding_rule);
			pParam.add(exc_wtx_time);
			pParam.add(expect_time);
			pParam.add(max_error_messages);
			pParam.add(rounding_rule);
			pParam.add(term_parm_time);
			pParam.add(def_ver_key);
			pParam.add(def_ver_telgr);
			pParam.add(bk_account_segments_count);
			pParam.add(posting_doc_number);
			pParam.add(posting_doc_pattern);
			pParam.add(clearing_doc_number);				
			pParam.add(clearing_doc_pattern);
			pParam.add(bk_export_format);
			pParam.add(bk_export_coding);
			pParam.add(clearing_export_format);
			pParam.add(clearing_export_coding);
			pParam.add(bank_statement_import_format);
			pParam.add(bank_statement_import_coding);
			pParam.add(auth_server_host);
			pParam.add(auth_server_port);
			pParam.add(auth_server_charset);				
			pParam.add(reserve_auth_server_host);
			pParam.add(reserve_auth_server_port);
			pParam.add(reserve_auth_server_charset);
			pParam.add(parse_trans_immediately);
			pParam.add(parse_trans_count);
			pParam.add(parse_trans_total_time);
			pParam.add(parse_trans_avg_time_limit);
			pParam.add(max_resp_time);
			pParam.add(no_actions_with_card_max_days);
			pParam.add(group_clearing_lines);				
			pParam.add(term_code_page);
			pParam.add(crypt_telgr);
			pParam.add(def_tr_limit);
			pParam.add(def_nincmax);
			pParam.add(need_calc_online_bon_pay_pin);
			pParam.add(need_calc_online_storno_pin);
			
		
		 	%>
			<%= Bean.executeInsertFunction("PACK$CLUB_UI.add_club", pParam, "../crm/club/clubspecs.jsp?id=", "") %>
		
		<% 	} else if (action.equalsIgnoreCase("edit_general")) {
	
			ArrayList<String> pParam = new ArrayList<String>();
					
			pParam.add(p_id);
			pParam.add(cd_club);
			pParam.add(name_club);
			pParam.add(sname_club);
			pParam.add(id_operator);
			pParam.add(id_clearing_bank);
			pParam.add(cd_currency_base);
			pParam.add(cd_country_def);
			pParam.add(shareholder_point_percent);
			pParam.add(id_nat_prs_role_def_refferal);
		
		 	%>
			<%= Bean.executeUpdateFunction("PACK$CLUB_UI.update_club_param_general", pParam, "../crm/club/clubspecs.jsp?id=" + p_id, "") %>
		
		<% 	} else if (action.equalsIgnoreCase("edit_interchange")) {
	
			ArrayList<String> pParam = new ArrayList<String>();
						
			pParam.add(p_id);
			pParam.add(auth_server_host);
			pParam.add(auth_server_port);
			pParam.add(auth_server_charset);
			pParam.add(reserve_auth_server_host);
			pParam.add(reserve_auth_server_port);
			pParam.add(reserve_auth_server_charset);
			pParam.add(expect_time);
			pParam.add(no_actions_with_card_max_days);
			pParam.add(term_code_page);
			pParam.add(def_ver_telgr);
			pParam.add(crypt_telgr);
			pParam.add(def_ver_key);
			pParam.add(def_nincmax);
			pParam.add(term_parm_time);
			pParam.add(max_error_messages);
			pParam.add(max_resp_time);
			pParam.add(exc_wtx_time);
			pParam.add(def_tr_limit);
			pParam.add(term_certificates_dir);
			pParam.add(parse_trans_immediately);
			pParam.add(account_postings_immediate);
			pParam.add(parse_trans_avg_time_limit);
			pParam.add(parse_trans_count);
			pParam.add(parse_trans_total_time);
			pParam.add(need_calc_online_bon_pay_pin);
			pParam.add(need_calc_online_storno_pin);
		
		 	%>
			<%= Bean.executeUpdateFunction("PACK$CLUB_UI.update_club_param_interchange", pParam, "../crm/club/clubspecs.jsp?id=" + p_id, "") %>
		
		<% 	} else if (action.equalsIgnoreCase("edit_finance")) {
	
			ArrayList<String> pParam = new ArrayList<String>();
						
			pParam.add(p_id);
			pParam.add(bk_account_segments_count);
			pParam.add(rounding_rule);
			pParam.add(currency_rounding_rule);
			pParam.add(posting_doc_number);
			pParam.add(posting_doc_pattern);
			pParam.add(bk_export_format);
			pParam.add(bk_export_coding);
			pParam.add(bk_export_dir);
			pParam.add(clearing_doc_number);
			pParam.add(clearing_doc_pattern);	
			pParam.add(group_clearing_lines);
			pParam.add(clearing_export_format);
			pParam.add(clearing_export_coding);
			pParam.add(clearing_export_dir);	
			pParam.add(bank_statement_import_format);
			pParam.add(bank_statement_import_coding);
			pParam.add(bank_statement_import_dir);
		
		 	%>
			<%= Bean.executeUpdateFunction("PACK$CLUB_UI.update_club_param_finance", pParam, "../crm/club/clubspecs.jsp?id=" + p_id, "") %>

		<% 	} else { %>
			<%= Bean.getUnknownActionText(action) %><% 
		} 
	} else { %>
		<%= Bean.getUnknownProcessText(process) %><% 
	}
} else if (type.equalsIgnoreCase("set_jur_prs")) {
	if (process.equalsIgnoreCase("yes")) {
		if (action.equalsIgnoreCase("set")) {%> 
        
   		<%= Bean.form_messageXML.getfieldTransl("processing_update", false) %>
   	   	<% 
    
    	String[] results = new String[2];

    	ArrayList<String> id_value=new ArrayList<String>();
		ArrayList<String> prv_id_value=new ArrayList<String>();
		Map<String,String> date_id_value=new HashMap<String,String>();
		Map<String,String> status_id_value=new HashMap<String,String>();

    	String callSQL = "";

    	Set<String> keySet = parameters.keySet();
		Iterator<String> keySetIterator = keySet.iterator();
		String key = "";
    	while(keySetIterator.hasNext()) {
   			try{
   				key = (String)keySetIterator.next();
   				if(key.contains("chb_id")){
   					id_value.add(key.substring(7));
   				}
   				if(key.contains("prv_id")){
   					prv_id_value.add(key.substring(7));
   				}
   				if(key.contains("reg_id")){
   					date_id_value.put(key.substring(7), Bean.getDecodeParam(parameters.get(key)));
   				}
   				if(key.contains("sts_id")){
   					status_id_value.put(key.substring(7), Bean.getDecodeParam(parameters.get(key)));
   				}
   			}
   			catch(Exception ex){
   				Bean.writeException(
   						"../crm/club/clubupdate.jsp",
   						type,
   						process,
   						action,
   						Bean.commonXML.getfieldTransl("h_get_param_value_error", false) + key+": " + ex.toString());
   			}
   		}
   		
   	    String resultInt = "";
   	    String resultFull = "0";
   	    String resultMessage = "";
   	    String resultMessageFull = "";
		
		if (id_value.size()>0) {
	 		 for(int counter=0;counter<id_value.size();counter++){
	 			
	 			 if (!(prv_id_value.contains(id_value.get(counter)))) {
		 			 
		        	callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK$CLUB_UI.add_participant_to_club(?,?,?,?,?,?)}";

		        	ArrayList<String> pParam = new ArrayList<String>();
		        			
		        	pParam.add(p_id);
		        	pParam.add(id_value.get(counter));
		        	pParam.add(status_id_value.get(id_value.get(counter)));
		        	pParam.add(date_id_value.get(id_value.get(counter)));
		        	pParam.add(Bean.getDateFormat());
			
					results = Bean.myCallFunctionParam(callSQL, pParam, 2);
					resultInt = results[0];
					resultMessage = results[1];
					
					%>
					<%= Bean.showCallSQL(callSQL) %>
					<%
					
					if (!("0".equalsIgnoreCase(resultInt))) {
						resultFull = resultInt;
						resultMessageFull = resultMessageFull + "; " +resultMessage;
					}
				} else {
		 			 
		        	callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK$CLUB_UI.update_club_registration_date(?,?,?,?,?,?)}";

		        	ArrayList<String> pParam = new ArrayList<String>();
		        		
		        	pParam.add(p_id);
		        	pParam.add(id_value.get(counter));
		        	pParam.add(status_id_value.get(id_value.get(counter)));
		        	pParam.add(date_id_value.get(id_value.get(counter)));
		        	pParam.add(Bean.getDateFormat());
				
					results = Bean.myCallFunctionParam(callSQL, pParam, 2);
					resultInt = results[0];
					resultMessage = results[1];
					
					%>
					<%= Bean.showCallSQL(callSQL) %>
					<%
					
					if (!("0".equalsIgnoreCase(resultInt))) {
						resultFull = resultInt;
						resultMessageFull = resultMessageFull + "; " +resultMessage;
					}
					
				}
			}
		}
		

   	 if (prv_id_value.size()>0) {
   	 		for(int counter=0;counter<prv_id_value.size();counter++){ 
			 	if (!(id_value.contains(prv_id_value.get(counter)))) {
			   	 			 
			 		callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK$CLUB_UI.delete_participant_from_club(?,?,?)}";
			
			 		ArrayList<String> pParam = new ArrayList<String>();
		        		
			 		pParam.add(p_id);
			 		pParam.add(prv_id_value.get(counter));
					
					results = Bean.myCallFunctionParam(callSQL, pParam, 2);
					resultInt = results[0];
					resultMessage = results[1];
					
					%>
					<%= Bean.showCallSQL(callSQL) %>
					<%
				
					if (!("0".equalsIgnoreCase(resultInt))) {
						resultFull = resultInt;
						resultMessageFull = resultMessageFull + "; " +resultMessage;
					}
					
			 	}
  	 		 }
   	 		
   	 	}
   	 
   			%>
	    	<%=Bean.showCallResult(callSQL, 
	    		resultFull, 
	    		resultMessage, 
	    		"../crm/club/clubspecs.jsp?id=" + p_id, 
	    		"../crm/club/clubspecs.jsp?id=" + p_id, 
	    		Bean.form_messageXML.getfieldTransl("save_error", false)) %>
			<% 
		
		} else { %>
			<%= Bean.getUnknownActionText(action) %><% 
		} 
	} else { %>
		<%= Bean.getUnknownProcessText(process) %><% 
	}
}  else if (type.equalsIgnoreCase("access")) {
	if (process.equalsIgnoreCase("yes")) {
		if (action.equalsIgnoreCase("set")) {%>
    	
	   		<%= Bean.form_messageXML.getfieldTransl("processing_update", false) %>
	   	   	<% 
	    
	    	ArrayList<String> id_value=new ArrayList<String>();
			ArrayList<String> prv_value=new ArrayList<String>();
	
	    	String callSQL = "";
	    	Set<String> keySet = parameters.keySet();
			Iterator<String> keySetIterator = keySet.iterator();
			String key = "";
	    	while(keySetIterator.hasNext()) {
	   			try{
	   				key = (String)keySetIterator.next();
	   				if(key.contains("chb_id")){
	   					id_value.add(key.substring(7));
	   				}
	   				if(key.contains("prv_id")){
	   					prv_value.add(key.substring(7));
	   				}
	   			}
	   			catch(Exception ex){
	   				Bean.writeException(
	   						"../crm/club/clubupdate.jsp",
	   						"",
	   						process,
	   						action,
	   						Bean.commonXML.getfieldTransl("h_get_param_value_error", false)+key+": " + ex.toString());
	   			}
	   		}
	
	   	    String resultInt = "";
	   	    String resultFull = "0";
	   	    String resultMessage = "";
	   	    String resultMessageFull = "";
	   	    String[] results = new String[4];
	
	   	    if (id_value.size()>0) {
	  	 		 for(int counter=0;counter<id_value.size();counter++){ 
	  	 			 if (!(prv_value.contains(id_value.get(counter)))) {
	  	 				 
			        	callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK$CLUB_UI.add_user_club(?,?,?)}";
				
						String[] pParam = new String [2];
		        		
				        pParam[0] = id_value.get(counter);
					    pParam[1] = p_id;
						
						results = Bean.myCallFunctionParam(callSQL, pParam, 2);
						resultInt = results[0];
						resultMessage = results[1];
						
						%>
						<%= Bean.showCallSQL(callSQL) %>
						<%
					
						if (!("0".equalsIgnoreCase(resultInt))) {
							resultFull = resultInt;
							resultMessageFull = resultMessageFull + "; " +resultMessage;
						}
					}
	  	 		}
			}
	   	 	if (prv_value.size()>0) {
	   	 		for(int counter=0;counter<prv_value.size();counter++){ 
				 	if (!(id_value.contains(prv_value.get(counter)))) {
				   	 				 
			        	callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK$CLUB_UI.delete_user_club(?,?,?)}";
				
						String[] pParam = new String [2];
		        		
				        pParam[0] = prv_value.get(counter);
					    pParam[1] = p_id;
						
						results = Bean.myCallFunctionParam(callSQL, pParam, 2);
						resultInt = results[0];
						resultMessage = results[1];
						
						%>
						<%= Bean.showCallSQL(callSQL) %>
						<%
					
						if (!("0".equalsIgnoreCase(resultInt))) {
							resultFull = resultInt;
							resultMessageFull = resultMessageFull + "; " +resultMessage;
						}
				 	}
	  	 		 }
	   	 		
	   	 	}
	
	   	 	%>
	  	    <%=Bean.showCallResult(
	   	    		callSQL, 
	   	    		resultFull, 
	   	    		resultMessageFull, 
	   	    		"../crm/club/clubspecs.jsp?id=" + p_id, 
	   	    		"../crm/club/clubspecs.jsp?id=" + p_id, 
	   	    		Bean.form_messageXML.getfieldTransl("save_error", false)) %>
	   		<% 
	
	    } else { %> 
	    	<%= Bean.getUnknownActionText(action) %><% 
	    }
	} else {
	    %> <%= Bean.getUnknownProcessText(process) %> <%
	}
} else {%> 
	<%= Bean.getUnknownTypeText(type) %><%
}
  
%>
</body>
</html>
