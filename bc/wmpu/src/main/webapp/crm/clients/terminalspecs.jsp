<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%= Bean.getLogOutScript(request) %>

<%@ page import="bc.objects.bcTerminalObject"%>
<%@ page import="bc.objects.bcTerminalCertificateObject"%>
<%@ page import="bc.objects.bcTerminalParamObject"%>
<%@ page import="bc.objects.bcTerminalLoyalityObject"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="org.apache.log4j.Logger" %>

<html>
<head>
	<%= Bean.getMetaContent() %>
	<%= Bean.getBottomFrameCSS() %>

<%
Logger LOGGER = Logger.getLogger( "source.jsp" );

HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String pageFormName = "CLIENTS_TERMINALS";

String tagSAM = "_SAM";
String tagSAMFind = "_SAM_FIND";
String tagSAMStatus = "_SAM_STATUS";
String tagLoyalityLines = "_LOYALITY_LINES";
String tagLoyalityLinesFind = "_LOYALITY_LINES_FIND";
String tagLoyalityLinesCategory = "_LOYALITY_LINES_CARD_CATEGORY";
String tagLoyalityLinesStatus = "_LOYALITY_LINES_CARD_STATUS";
String tagLoyalityLinesKind = "_LOYALITY_LINES_LOYALITY_KIND";

String tagLoyHistory = "_LOY_HISTORY";
String tagLoyHistoryFind = "_LOY_HISTORY_FIND";

String tagCertificates = "_CERTIFICATES";
String tagCertificateFind = "_CERTIFICATE_FIND";

String tagTermSession = "_TERM_SESSION";
String tagTermSessionFind = "_TERM_SESSION_FIND";
String tagTermSessionDataType = "_TERM_SESSION_DATA_TYPE";
String tagTermSessionDataState = "_TERM_SESSION_DATA_STATE";
String tagTransaction = "_TRANSACTION";
String tagTransactionFind = "_TRANSACTION_FIND";
String tagTransactionType = "_TRANSACTION_TYPE";
String tagTransactionState = "_TRANSACTION_STATE";
String tagTransactionPayType = "_TRANSACTION_PAY_TYPE";
String tagRelationships = "_RELATIONSHIPS";
String tagRelationshipFind = "_RELATIONSHIP_FIND";
String tagRelationshipKind = "_RELATIONSHIP_KIND";
String tagComissions = "_COMISSIONS";
String tagComissionFind = "_COMISSION_FIND";
String tagComissionRelType = "_COMISSION_REL_TYPE";
String tagLogistic = "_LOGISTIC";
String tagLogisticFind = "_LOGISTIC_FIND";
String tagMessages = "_MESSAGES";
String tagMessageFind = "_MESSAGE_FIND";
String tagMessageIsArchive = "_MESSAGE_IS_ARCHIVE";
String tagMonitoring = "_MONITORING";
String tagMonitoringFind = "_MONITORING_FIND";
String tagTermUser = "_TERM_USER";
String tagTermUserFind = "_TERM_USER_FIND";
String tagTermUserStatus = "_TERM_USER_STATUS";
String tagTermUserAccessType = "_TERM_USER_ACCESS_TYPE";
String tagTermOPType = "_TERM_OP_TYPE";
String tagTermOPTypeFind = "_TERM_OP_TYPE_FIND";
String tagHistory = "_HISTORY";
String tagHistoryFind = "_HISTORY_FIND";

Bean.setJspPageForTabName(pageFormName);


String termid = Bean.getDecodeParam(parameters.get("id"));
String	tab = Bean.getDecodeParam(parameters.get("tab"));

if (tab==null || "".equalsIgnoreCase(tab)) { tab = Bean.tabsHmGetValue(pageFormName); }

if (termid==null || "".equalsIgnoreCase(termid)) {
%>
	<%=Bean.getIDNotFoundMessage() %>
<% 
}
else {
	Bean.tabsHmSetValue(pageFormName, tab);

	bcTerminalObject terminal = new bcTerminalObject(termid);
	terminal.getFeature();
	
	String cdTermType = terminal.getValue("CD_TERM_TYPE");
	boolean isWebPosTerminal = "WEBPOS".equalsIgnoreCase(cdTermType);
	boolean isPhisicalTerminal = "PHYSICAL".equalsIgnoreCase(cdTermType);
	
	bcTerminalParamObject param = null;
    bcTerminalLoyalityObject loy = null;
    
    if (isPhisicalTerminal) {
		Bean.currentMenu.setExistFlag("CLIENTS_TERMINALS_MONITORING", true);
    } else {
    	Bean.currentMenu.setExistFlag("CLIENTS_TERMINALS_MONITORING", false);
    	
    	if (Bean.currentMenu.isCurrentTab("CLIENTS_TERMINALS_MONITORING")) {
    		Bean.currentMenu.setFirstCurrentTab();
    		tab = Bean.currentMenu.getCurrentTab();
    		Bean.tabsHmSetValue(pageFormName, tab);
    	}
    }

	//String id_service_place2 = terminal.getValue("ID_SERVICE_PLACE");
	
	String id_loy_history = Bean.getDecodeParam(parameters.get("id_loy_history"));
	
	if (id_loy_history==null || "".equalsIgnoreCase(id_loy_history) || "null".equalsIgnoreCase(id_loy_history)) {
		id_loy_history = "";
	}
	
	String hist = Bean.getDecodeParam(parameters.get("hist"));
	if (hist == null || "".equalsIgnoreCase(hist)) {
		hist = "";
	}
	
	String termParamLink = "../crm/clients/terminalspecs.jsp?id=" + terminal.getValue("ID_TERM") + "&hist=y&&id_loy_history="+id_loy_history;
	
	//Обрабатываем номера страниц
	String l_sam_page = Bean.getDecodeParam(parameters.get("sam_page"));
	Bean.pageCheck(pageFormName + tagSAM, l_sam_page);
	String l_sam_page_beg = Bean.getFirstRowNumber(pageFormName + tagSAM);
	String l_sam_page_end = Bean.getLastRowNumber(pageFormName + tagSAM);
	
	String sam_find 	= Bean.getDecodeParam(parameters.get("sam_find"));
	sam_find 	= Bean.checkFindString(pageFormName + tagSAMFind, sam_find, l_sam_page);
	
	String sam_status 	= Bean.getDecodeParam(parameters.get("sam_status"));
	sam_status 	= Bean.checkFindString(pageFormName + tagSAMStatus, sam_status, l_sam_page);
	
	String l_loy_lines_page = Bean.getDecodeParam(parameters.get("loy_lines_page"));
	Bean.pageCheck(pageFormName + tagLoyalityLines, l_loy_lines_page);
	String l_loy_lines_page_beg = Bean.getFirstRowNumber(pageFormName + tagLoyalityLines);
	String l_loy_lines_page_end = Bean.getLastRowNumber(pageFormName + tagLoyalityLines);
	
	String loy_lines_find 	= Bean.getDecodeParam(parameters.get("loy_lines_find"));
	loy_lines_find 	= Bean.checkFindString(pageFormName + tagLoyalityLinesFind, loy_lines_find, l_loy_lines_page);
	
	String id_category = Bean.getDecodeParam(parameters.get("id_category"));
	id_category 	= Bean.checkFindString(pageFormName + tagLoyalityLinesCategory, id_category, "");

	String id_status = Bean.getDecodeParam(parameters.get("id_status"));
	id_status 		= Bean.checkFindString(pageFormName + tagLoyalityLinesStatus, id_status, "");

	String cd_kind = Bean.getDecodeParam(parameters.get("cd_kind"));
	cd_kind 		= Bean.checkFindString(pageFormName + tagLoyalityLinesKind, cd_kind, "");

	String l_term_ses_page = Bean.getDecodeParam(parameters.get("term_ses_page"));
	Bean.pageCheck(pageFormName + tagTermSession, l_term_ses_page);
	String l_term_ses_page_beg = Bean.getFirstRowNumber(pageFormName + tagTermSession);
	String l_term_ses_page_end = Bean.getLastRowNumber(pageFormName + tagTermSession);
	
	String term_ses_find 	= Bean.getDecodeParam(parameters.get("term_ses_find"));
	term_ses_find 	= Bean.checkFindString(pageFormName + tagTermSessionFind, term_ses_find, l_term_ses_page);
	
	String term_ses_data_type 	= Bean.getDecodeParam(parameters.get("term_ses_data_type"));
	term_ses_data_type 	= Bean.checkFindString(pageFormName + tagTermSessionDataType, term_ses_data_type, l_term_ses_page);
	
	String term_ses_data_state 	= Bean.getDecodeParam(parameters.get("term_ses_data_state"));
	term_ses_data_state 	= Bean.checkFindString(pageFormName + tagTermSessionDataState, term_ses_data_state, l_term_ses_page);
	
	String l_trans_page = Bean.getDecodeParam(parameters.get("trans_page"));
	Bean.pageCheck(pageFormName + tagTransaction, l_trans_page);
	String l_trans_page_beg = Bean.getFirstRowNumber(pageFormName + tagTransaction);
	String l_trans_page_end = Bean.getLastRowNumber(pageFormName + tagTransaction);
	
	String transaction_find 	= Bean.getDecodeParam(parameters.get("transaction_find"));
	transaction_find 	= Bean.checkFindString(pageFormName + tagTransactionFind, transaction_find, l_trans_page);
	
	String transaction_type 	= Bean.getDecodeParam(parameters.get("transaction_type"));
	transaction_type 	= Bean.checkFindString(pageFormName + tagTransactionType, transaction_type, l_trans_page);

	String transaction_state 	= Bean.getDecodeParam(parameters.get("transaction_state"));
	transaction_state 	= Bean.checkFindString(pageFormName + tagTransactionState, transaction_state, l_trans_page);

	String transaction_pay_type 	= Bean.getDecodeParam(parameters.get("transaction_pay_type"));
	transaction_pay_type 	= Bean.checkFindString(pageFormName + tagTransactionPayType, transaction_pay_type, l_trans_page);
	
	String l_messages_page = Bean.getDecodeParam(parameters.get("messages_page"));
	Bean.pageCheck(pageFormName + tagMessages, l_messages_page);
	String l_messages_beg = Bean.getFirstRowNumber(pageFormName + tagMessages);
	String l_messages_end = Bean.getLastRowNumber(pageFormName + tagMessages);
	
	String message_find 	= Bean.getDecodeParam(parameters.get("message_find"));
	message_find 	= Bean.checkFindString(pageFormName + tagMessageFind, message_find, l_messages_page);

	String message_archive = Bean.getDecodeParam(parameters.get("message_archive"));
	message_archive 		= Bean.checkFindString(pageFormName + tagMessageIsArchive, message_archive, l_messages_page);
	
	String l_comission_page = Bean.getDecodeParam(parameters.get("comission_page"));
	Bean.pageCheck(pageFormName + tagComissions, l_comission_page);
	String l_comission_page_beg = Bean.getFirstRowNumber(pageFormName + tagComissions);
	String l_comission_page_end = Bean.getLastRowNumber(pageFormName + tagComissions);
	
	String comission_find 	= Bean.getDecodeParam(parameters.get("comission_find"));
	comission_find 	= Bean.checkFindString(pageFormName + tagComissionFind, comission_find, l_comission_page);
	
	String comission_rel_type = Bean.getDecodeParam(parameters.get("comission_rel_type"));
	comission_rel_type 	= Bean.checkFindString(pageFormName + tagComissionRelType, comission_rel_type, l_comission_page);
	
	String l_relation_page = Bean.getDecodeParam(parameters.get("relation_page"));
	Bean.pageCheck(pageFormName + tagRelationships, l_relation_page);
	String l_relation_page_beg = Bean.getFirstRowNumber(pageFormName + tagRelationships);
	String l_relation_page_end = Bean.getLastRowNumber(pageFormName + tagRelationships);
	
	String relation_find 	= Bean.getDecodeParam(parameters.get("relation_find"));
	relation_find 	= Bean.checkFindString(pageFormName + tagRelationshipFind, relation_find, l_relation_page);
	
	String relation_kind = Bean.getDecodeParam(parameters.get("relation_kind"));
	if (relation_kind==null || "".equalsIgnoreCase(relation_kind)) {
		relation_kind = "CREATED";
	}
	relation_kind 		= Bean.checkFindString(pageFormName + tagRelationshipKind, relation_kind, l_relation_page);
	
	String l_logistic_page = Bean.getDecodeParam(parameters.get("logistic_page"));
	Bean.pageCheck(pageFormName + tagLogistic, l_logistic_page);
	String l_logistic_page_beg = Bean.getFirstRowNumber(pageFormName + tagLogistic);
	String l_logistic_page_end = Bean.getLastRowNumber(pageFormName + tagLogistic);
	
	String logistic_find 	= Bean.getDecodeParam(parameters.get("logistic_find"));
	logistic_find 	= Bean.checkFindString(pageFormName + tagLogisticFind, logistic_find, l_logistic_page);
	
	String l_monitoring_page = Bean.getDecodeParam(parameters.get("monitoring_page"));
	Bean.pageCheck(pageFormName + tagMonitoring, l_monitoring_page);
	String l_monitoring_page_beg = Bean.getFirstRowNumber(pageFormName + tagMonitoring);
	String l_monitoring_page_end = Bean.getLastRowNumber(pageFormName + tagMonitoring);
	
	String monitoring_find 	= Bean.getDecodeParam(parameters.get("monitoring_find"));
	monitoring_find 	= Bean.checkFindString(pageFormName + tagMonitoringFind, monitoring_find, l_monitoring_page);
	
	String l_term_user = Bean.getDecodeParam(parameters.get("term_user_page"));
	Bean.pageCheck(pageFormName + tagTermUser, l_term_user);
	String l_term_user_beg = Bean.getFirstRowNumber(pageFormName + tagTermUser);
	String l_term_user_end = Bean.getLastRowNumber(pageFormName + tagTermUser);
	
	String term_user_find 	= Bean.getDecodeParam(parameters.get("term_user_find"));
	term_user_find 	= Bean.checkFindString(pageFormName + tagTermUserFind, term_user_find, l_term_user);
	
	String term_user_status 	= Bean.getDecodeParam(parameters.get("term_user_status"));
	term_user_status 	= Bean.checkFindString(pageFormName + tagTermUserStatus, term_user_status, l_term_user);
	
	String term_user_access_type 	= Bean.getDecodeParam(parameters.get("term_user_access_type"));
	term_user_access_type 	= Bean.checkFindString(pageFormName + tagTermUserAccessType, term_user_access_type, l_term_user);
	
	String l_op_type = Bean.getDecodeParam(parameters.get("op_type_page"));
	Bean.pageCheck(pageFormName + tagTermOPType, l_op_type);
	String l_op_type_beg = Bean.getFirstRowNumber(pageFormName + tagTermOPType);
	String l_op_type_end = Bean.getLastRowNumber(pageFormName + tagTermOPType);
	
	String op_type_find 	= Bean.getDecodeParam(parameters.get("op_type_find"));
	op_type_find 	= Bean.checkFindString(pageFormName + tagTermOPTypeFind, op_type_find, l_op_type);

	String l_history_page = Bean.getDecodeParam(parameters.get("history_page"));
	Bean.pageCheck(pageFormName + tagHistory, l_history_page);
	String l_history_page_beg = Bean.getFirstRowNumber(pageFormName + tagHistory);
	String l_history_page_end = Bean.getLastRowNumber(pageFormName + tagHistory);
	
	String history_find 	= Bean.getDecodeParam(parameters.get("history_find"));
	history_find 	= Bean.checkFindString(pageFormName + tagHistoryFind, history_find, l_history_page);
	
	String l_cert_page = Bean.getDecodeParam(parameters.get("cert_page"));
	Bean.pageCheck(pageFormName + tagCertificates, l_cert_page);
	String l_cert_page_beg = Bean.getFirstRowNumber(pageFormName + tagCertificates);
	String l_cert_page_end = Bean.getLastRowNumber(pageFormName + tagCertificates);
	
	String cert_find 	= Bean.getDecodeParam(parameters.get("cert_find"));
	cert_find 	= Bean.checkFindString(pageFormName + tagCertificateFind, cert_find, l_cert_page);
	


	String l_loy_history_page = Bean.getDecodeParam(parameters.get("loy_history_page"));
	Bean.pageCheck(pageFormName + tagLoyHistory, l_loy_history_page);
	String l_loy_history_page_beg = Bean.getFirstRowNumber(pageFormName + tagLoyHistory);
	String l_loy_history_page_end = Bean.getLastRowNumber(pageFormName + tagLoyHistory);
	
	String loy_history_find 	= Bean.getDecodeParam(parameters.get("loy_history_find"));
	loy_history_find 	= Bean.checkFindString(pageFormName + tagLoyHistoryFind, loy_history_find, l_loy_history_page);
	
	/*if (id_service_place == null || "".equalsIgnoreCase(id_service_place)) {
		id_service_place = Bean.getTermFirstServicePlace(termid);
	} else {
		
	}*/
	

%>
</head>
<body>
<div id="div_tabsheet">
	
	<table <%=Bean.getTableMenuParam() %>>
		<tr>
			<%= Bean.getPageHeader() %>

			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLIENTS_TERMINALS_INFO")) { %>
				<% if (Bean.currentMenu.isCurrentTabAndEditPermitted("CLIENTS_TERMINALS_INFO")) { %>
				    <%= Bean.getMenuButton("ADD", "../crm/clients/terminalupdate.jsp?id_term=" + terminal.getValue("ID_TERM") + "&type=term&action=add2&process=no", "", "") %>
				    <%= Bean.getMenuButton("DELETE", "../crm/clients/terminalupdate.jsp?id_term=" + terminal.getValue("ID_TERM") + "&type=term&action=remove&process=yes", Bean.terminalXML.getfieldTransl("h_delete_term", false), terminal.getValue("ID_TERM")) %>
				<% } %>
				<%= Bean.getReportHyperLink("SR10", "ID_TERM=" + terminal.getValue("ID_TERM") + "&DATE_REPORT=" + Bean.getSysDate()) %>
			<% } %>
			
			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLIENTS_TERMINALS_SAM")) { %>
				<% if (Bean.currentMenu.isCurrentTabAndEditPermitted("CLIENTS_TERMINALS_SAM")) { %>
				    <%= Bean.getMenuButtonBase("ADD", "../crm/clients/terminalupdate.jsp?id_term=" + terminal.getValue("ID_TERM") + "&type=sam&action=add&process=no", "", "", "", "div_data_detail") %>
				<% } %>
			    <!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagSAM, termParamLink + "&tab="+Bean.currentMenu.getTabID("CLIENTS_TERMINALS_SAM")+"&", "sam_page") %>
			<%  } %>
			
			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLIENTS_TERMINALS_LOY")) { %>
				<%
				param = new bcTerminalParamObject(termid, terminal.getValue("ID_PARAM_HISTORY"));
				
				id_loy_history = Bean.isEmpty(id_loy_history)?terminal.getValue("ID_LOYALITY_HISTORY"):id_loy_history;
				
				loy = new bcTerminalLoyalityObject(termid, id_loy_history);
				%>
				<%= Bean.getReportHyperLink("SR08", "ID_TERM=" + termid + "&ID_LOYALITY_HISTORY=" + id_loy_history) %>
			<%  } %>
		
			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLIENTS_TERMINALS_MESSAGES")) { %>
				<% if (Bean.currentMenu.isCurrentTabAndEditPermitted("CLIENTS_TERMINALS_MESSAGES")) { %>
				    <%= Bean.getMenuButtonBase("ADD", "../crm/clients/terminalupdate.jsp?id_term=" + terminal.getValue("ID_TERM") + "&type=message&action=add&process=no", "", "", "", "div_data_detail") %>
				<% } %>

			    <!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagMessages, termParamLink + "&tab="+Bean.currentMenu.getTabID("CLIENTS_TERMINALS_MESSAGES")+"&", "messages_page") %>

			<%  } %>
		
			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLIENTS_TERMINALS_TERMSES")) { %>
			    <!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagTermSession, termParamLink + "&tab="+Bean.currentMenu.getTabID("CLIENTS_TERMINALS_TERMSES")+"&", "term_ses") %>
			<%  } %>
		
			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLIENTS_TERMINALS_TRANS")) { %>
			    <!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagTransaction, termParamLink + "&tab="+Bean.currentMenu.getTabID("CLIENTS_TERMINALS_TRANS")+"&", "trans_page") %>
			<%  } %>
		
			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLIENTS_TERMINALS_COMISSIONS")) { %>
			    <!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagComissions, termParamLink + "&tab="+Bean.currentMenu.getTabID("CLIENTS_TERMINALS_COMISSIONS")+"&comission_rel_type="+comission_rel_type+"&", "comission_page") %>
			<%  } %>
		
			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLIENTS_TERMINALS_RELATIONSHIPS")) { %>
			    <!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagRelationships, termParamLink + "&tab="+Bean.currentMenu.getTabID("CLIENTS_TERMINALS_RELATIONSHIPS")+"&", "relation_page") %>
			<%  } %>
		
			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLIENTS_TERMINALS_LOGISTIC")) { %>
			    <!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagLogistic, termParamLink + "&tab="+Bean.currentMenu.getTabID("CLIENTS_TERMINALS_LOGISTIC")+"&", "logistic_page") %>
			<%  } %>
		
			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLIENTS_TERMINALS_MONITORING")) { %>
			    <!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagMonitoring, termParamLink + "&tab="+Bean.currentMenu.getTabID("CLIENTS_TERMINALS_MONITORING")+"&", "monitoring_page") %>
			<%  } %>
		
			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLIENTS_TERMINALS_USERS")) { %>
			    <% if (Bean.currentMenu.isCurrentTabAndEditPermitted("CLIENTS_TERMINALS_USERS")) { %>
					<% if (isPhisicalTerminal) { %>
				    <%= Bean.getMenuButtonBase("ADD", "../crm/clients/terminaluserupdate.jsp?id_term=" + terminal.getValue("ID_TERM") + "&id=" + terminal.getValue("ID_TERM") + "&type=user&action=add&process=no", "", "", "", "div_data_detail") %>
					<% } else { %>
				    <%= Bean.getMenuButtonBase("ADD", "../crm/clients/terminaluserupdate.jsp?id_term=" + terminal.getValue("ID_TERM") + "&id=" + terminal.getValue("ID_TERM") + "&type=user&action=adduserlist&process=no", "", "", "", "div_data_detail") %>
					<% } %>
				<% } %>
			    <!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagTermUser, termParamLink + "&tab="+Bean.currentMenu.getTabID("CLIENTS_TERMINALS_USERS")+"&", "term_user_page") %>
			<%  } %>
		
			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLIENTS_TERMINALS_OP_TYPES")) { %>
			    <% if (Bean.currentMenu.isCurrentTabAndEditPermitted("CLIENTS_TERMINALS_OP_TYPES")) { %>
				    <%= Bean.getMenuButton("ADD", "../crm/clients/terminalupdate.jsp?id_term=" + terminal.getValue("ID_TERM") + "&type=online_type&action=add&process=no", "", "") %>
				<% } %>
			    <!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagTermOPType, termParamLink + "&tab="+Bean.currentMenu.getTabID("CLIENTS_TERMINALS_OP_TYPES")+"&", "op_type_page") %>
			<%  } %>
		
			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLIENTS_TERMINALS_CHANGE_HISTORY")) { %>
			    <!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagHistory, termParamLink + "&tab="+Bean.currentMenu.getTabID("CLIENTS_TERMINALS_CHANGE_HISTORY")+"&", "history_page") %>
			<%  } %>
		</tr>
	</table>
	<table <%=Bean.getTableMenuParam() %>>
		<%= Bean.getDetailCaption(terminal.getValue("ID_TERM") + " - " + terminal.getValue("ID_TERM_HEX")) %>
		<tr>
			<td>
				<!-- Выводим перечень закладок -->
				<%= Bean.currentMenu.getFormTabScheeds(tab, termParamLink) %>
			</td>
		</tr>
	</table>
</div>
<div id="div_data">
<div id="div_data_detail">
<%
if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLIENTS_TERMINALS_INFO")) { 

	boolean readPermission = !Bean.currentMenu.isCurrentTabAndEditPermitted("CLIENTS_TERMINALS_INFO");
	%>
	<% if (!readPermission) { %>
		<script>
			var formPhisicalTerm = new Array (
					new Array ('cd_term_status', 'varchar2', 1),
					new Array ('id_device_type', 'varchar2', 1),
					new Array ('name_club', 'varchar2', 1),
					new Array ('club_registration_date', 'varchar2', 1),
					new Array ('name_finance_acquirer', 'varchar2', 1),
					new Array ('need_update_param', 'varchar2', 1),
					new Array ('need_sys_lock', 'varchar2', 1),
					new Array ('term_code_page', 'varchar2', 1),
					new Array ('crypt_telgr', 'varchar2', 1),
					new Array ('resp_time', 'varchar2', 1),
					new Array ('vk_enc', 'varchar2', 1),
					new Array ('ver_telgr', 'varchar2', 1),
					new Array ('nincmax', 'varchar2', 1),
					new Array ('connect_ekka', 'varchar2', 1),
					new Array ('tr_limit', 'varchar2', 1),
					new Array ('cd_term_currency', 'varchar2', 1),
					new Array ('sname_term_currency', 'varchar2', 1),
					new Array ('can_oper_different_currency', 'varchar2', 1)
			);

			var formOtherTerm = new Array (
					new Array ('cd_term_status', 'varchar2', 1),
					new Array ('name_club', 'varchar2', 1),
					new Array ('club_registration_date', 'varchar2', 1),
					new Array ('name_service_place', 'varchar2', 1),
					new Array ('date_location', 'varchar2', 1),
					new Array ('name_referral_scheme', 'varchar2', 1),
					new Array ('can_test_mode', 'varchar2', 1),
					new Array ('cd_term_currency', 'varchar2', 1),
					new Array ('sname_term_currency', 'varchar2', 1)
			);

			function myValidateForm(){
				var termType = document.getElementById('cd_term_type').value;
				if (termType == 'PHYSICAL') {
					return validateForm(formPhisicalTerm);
				} else {
					return validateForm(formOtherTerm);
				}
			}
			function checkTermType(){
				var termType = document.getElementById('cd_term_type').value;
				if (termType == 'PHYSICAL') {
					document.getElementById('span_finance_acquirer').innerHTML='<%= Bean.terminalXML.getfieldTransl("name_finance_acquirer", true) %>';
				} else {
					document.getElementById('span_finance_acquirer').innerHTML='<%= Bean.terminalXML.getfieldTransl("name_finance_acquirer", false) %>';
				}
			}
	   
			function submitDateForm(form){
				form.datetime.value = '';
				var origDate = form.date.value;
				if (origDate != '') {
					var hours = form.hours.value;
					var min = form.min.value;
					var sec = form.sec.value;
					form.datetime.value = origDate+' '+hours+':'+min+':'+sec;
				}			
				form.submit();
			}		
			checkTermType();
		</script>
	
    <form action="../crm/clients/terminalupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return myValidateForm()">
        <input type="hidden" name="type" value="term">
        <input type="hidden" name="action" value="edit">
        <input type="hidden" name="process" value="yes">
        <input type="hidden" name="cd_term_type" id="cd_term_type" value="<%=cdTermType%>">
	<% } %>

	<table <%=Bean.getTableDetailParam() %>>
		<tr>
			<td><%= Bean.terminalXML.getfieldTransl("id_term", false) %></td>
			<td>
				<%=Bean.getInputTextElement("id_term", "", terminal.getValue("ID_TERM"), true, "150") %>
				<span><%=terminal.getValue("ID_TERM_HEX") %> (HEX)</span>
			</td>
 		    <td><%= Bean.clubXML.getfieldTransl("club", false) %>
				<%= Bean.getGoToClubLink(terminal.getValue("ID_CLUB")) %>
			</td><td><%=Bean.getInputTextElement("name_club", "", Bean.getClubShortName(terminal.getValue("ID_CLUB")), true, "230") %></td>
		</tr>
		<tr>
			<td><%= Bean.terminalXML.getfieldTransl("term_serial_number", false) %></td><td><%=Bean.getInputTextElement("TERM_SERIAL_NUMBER", "", terminal.getValue("TERM_SERIAL_NUMBER"), readPermission, "150") %></td>
			<td><%= Bean.clubXML.getfieldTransl("club_date_beg", !readPermission) %></td><td><%=Bean.getCalendarInputField("club_registration_date", terminal.getValue("CLUB_REGISTRATION_DATE_FRMT"), "15", "", readPermission) %></td>
		</tr>
		<tr>
			<td><%= Bean.terminalXML.getfieldTransl("cd_term_status", !readPermission) %></td> <td><%=Bean.getSelectElement("cd_term_status", "", terminal.getValue("CD_TERM_STATUS"), terminal.getValue("NAME_TERM_STATUS"), new StringBuffer(Bean.getTermStatusOptions(terminal.getValue("CD_TERM_STATUS"), true)), readPermission, "150" ) %></td>
			<td><%= Bean.terminalXML.getfieldTransl("term_owner", false) %>
				<%=Bean.getGoToJurPrsHyperLink(terminal.getValue("ID_TERM_OWNER")) %>
			</td>
			<td>
				<%=Bean.getWindowFindJurPrs("term_owner", terminal.getValue("ID_TERM_OWNER"), terminal.getValue("SNAME_TERM_OWNER"), "ALL", "30", readPermission) %>
			</td>			
		</tr>
		<tr>
			<td><%= Bean.terminalXML.getfieldTransl("NAME_TERM_TYPE", false) %></td><td><%=Bean.getInputTextElement("NAME_TERM_TYPE", "", terminal.getValue("NAME_TERM_TYPE"), true, "150") %></td>
			<% if (isPhisicalTerminal) { %>
			<td><span id="span_finance_acquirer"><%= Bean.terminalXML.getfieldTransl("name_finance_acquirer", false) %></span>
				<%=Bean.getGoToJurPrsHyperLink(terminal.getValue("ID_FINANCE_ACQUIRER")) %>
			</td> 
			<td>
				<%=Bean.getWindowFindJurPrs("finance_acquirer", terminal.getValue("ID_FINANCE_ACQUIRER"), terminal.getValue("SNAME_FINANCE_ACQUIRER"), "FIN_ACQUIRER", "30", readPermission) %>
			</td>
			<% } else { %>
			<td colspan="2">&nbsp;</td>
			<% } %>
		</tr>
		<tr>
			<td><%= Bean.terminalXML.getfieldTransl("device_type", isPhisicalTerminal && !readPermission) %></td> <td><%=Bean.getSelectElement("id_device_type", "", terminal.getValue("ID_DEVICE_TYPE"), Bean.getTermDeviceTypeName(terminal.getValue("ID_DEVICE_TYPE")), new StringBuffer(Bean.getTermDeviceOptions(terminal.getValue("CD_TERM_TYPE"), terminal.getValue("ID_DEVICE_TYPE"), true)), readPermission, "150" ) %></td>
			<% if (isPhisicalTerminal) { %>
			<td><%= Bean.terminalXML.getfieldTransl("id_technical_acquirer", false) %>
				<%=Bean.getGoToJurPrsHyperLink(terminal.getValue("ID_TECHNICAL_ACQUIRER")) %>
			</td> 
			<td>
				<%=Bean.getWindowFindJurPrs("technical_acquirer", terminal.getValue("ID_TECHNICAL_ACQUIRER"), terminal.getValue("SNAME_TECHNICAL_ACQUIRER"), "TECH_ACQUIRER", "30", readPermission) %>
			</td>			
			<% } else { %>
			<td colspan="2">&nbsp;</td>
			<% } %>
		</tr>

		<tr>
			<td class="top_line" colspan="4"><b><%=Bean.terminalXML.getfieldTransl("h_term_location_param", false) %></b></td>
		</tr>
		<tr>
			<td><%= Bean.terminalXML.getfieldTransl("name_service_place", isWebPosTerminal && !readPermission) %>
				<%=Bean.getGoToServicePlaceLink(terminal.getValue("ID_SERVICE_PLACE")) %>
			</td> 
			<td>
				<% if (isWebPosTerminal && !readPermission) { %>
				<%=Bean.getWindowFindServicePlace("service_place", terminal.getValue("ID_SERVICE_PLACE"), terminal.getValue("SNAME_SERVICE_PLACE"), terminal.getValue("ID_DEALER"), "", "30") %>
				<% } else { %>
        		<input type="hidden" name="id_service_place" value="<%=terminal.getValue("ID_SERVICE_PLACE")%>">
				<%=Bean.getInputTextElement("sname_service_place", "", terminal.getValue("SNAME_SERVICE_PLACE"), true, "250") %>
				<% } %>
			</td>			
			<td><%= Bean.terminalXML.getfieldTransl("referral_scheme", isWebPosTerminal && !readPermission) %>
				<%= Bean.getGoToReferralSchemeLink(terminal.getValue("ID_REFERRAL_SCHEME")) %>
			</td> 
			<td>
				<%=Bean.getWindowFindReferralScheme("referral_scheme", terminal.getValue("ID_REFERRAL_SCHEME"), "", "", "30", readPermission) %>
			</td>		
		</tr>
		<tr>
			<td><%= Bean.terminalXML.getfieldTransl("date_location", isWebPosTerminal && !readPermission) %></td><td><%=Bean.getCalendarInputField("date_location", terminal.getValue("DATE_LOCATION_DF"), "15", "", readPermission) %></td>
			<% if (isWebPosTerminal) { %>
			<td><%= Bean.terminalXML.getfieldTransl("can_test_mode", !readPermission) %></td> <td><%=Bean.getSelectElement("can_test_mode", "", terminal.getValue("CAN_TEST_MODE"), Bean.getMeaningFoCodeValue("YES_NO", terminal.getValue("CAN_TEST_MODE")), new StringBuffer(Bean.getMeaningFromLookupNameOptions("YES_NO", terminal.getValue("CAN_TEST_MODE"), true)), readPermission, "150" ) %></td>
			<% } else { %>
			<td colspan="2">&nbsp;</td>
			<% } %>
		</tr>
		<% if (isPhisicalTerminal) { %>
		<tr>
			<td><%= Bean.terminalXML.getfieldTransl("cash_desk_number", false) %></td><td><%=Bean.getInputTextElement("cash_desk_number", "", terminal.getValue("CASH_DESK_NUMBER"), readPermission, "150") %></td>
		</tr>
		<% } %>

		<tr>
			<td class="top_line" colspan="4"><b><%=Bean.terminalXML.getfieldTransl("h_term_setting_param", false) %></b></td>
		</tr>
		<% if (isPhisicalTerminal) { %>
		<tr>
			<td><%= Bean.terminalXML.getfieldTransl("need_update_param", !readPermission) %></td> <td><%=Bean.getSelectElement("need_update_param", "", terminal.getValue("NEED_UPDATE_PARAM"), Bean.getMeaningFoCodeValue("YES_NO", terminal.getValue("NEED_UPDATE_PARAM")), new StringBuffer(Bean.getMeaningFromLookupNameOptions("YES_NO", terminal.getValue("NEED_UPDATE_PARAM"), true)), readPermission, "150" ) %></td>
			<td><%= Bean.terminalXML.getfieldTranslDisable( "term_mon_interval_day", false, !isPhisicalTerminal) %></td><td><%=Bean.getInputTextElement("term_mon_interval_day", "", terminal.getValue("TERM_MON_INTERVAL_DAY"), readPermission, "150") %></td>
		</tr>
		<tr>
			<td><%= Bean.terminalXML.getfieldTransl("need_sys_lock_name", !readPermission) %></td> <td><%=Bean.getSelectElement("need_sys_lock", "", terminal.getValue("NEED_SYS_LOCK"), Bean.getMeaningFoCodeValue("YES_NO", terminal.getValue("NEED_SYS_LOCK")), new StringBuffer(Bean.getMeaningFromLookupNameOptions("YES_NO", terminal.getValue("NEED_SYS_LOCK"), true)), readPermission, "150" ) %></td>
			<td><%= Bean.terminalXML.getfieldTransl("term_mon_rep_next_date", false) %></td><td><%=Bean.getCalendarInputField("term_mon_rep_next_date", terminal.getValue("TERM_MON_REP_NEXT_DATE_DHMF"), "19", "", readPermission) %></td>
		</tr>
		<tr>
			<td><%= Bean.terminalXML.getfieldTransl("TERM_CODE_PAGE", !readPermission) %></td> <td><%=Bean.getSelectElement("term_code_page", "", terminal.getValue("TERM_CODE_PAGE"), Bean.getMeaningFoCodeValue("TERMINAL_CODE_PAGE", terminal.getValue("TERM_CODE_PAGE")), new StringBuffer(Bean.getMeaningFromLookupNameOptions("TERMINAL_CODE_PAGE", terminal.getValue("TERM_CODE_PAGE"), true)), readPermission, "150" ) %></td>
			<td><%= Bean.terminalXML.getfieldTransl("VER_TELGR", !readPermission) %></td> <td><%=Bean.getSelectElement("ver_telgr", "VER_TELGR", terminal.getValue("VER_TELGR"), Bean.getMeaningFoCodeValue("VER_TELGR", terminal.getValue("VER_TELGR")), new StringBuffer(Bean.getMeaningFromLookupNameOptions("VER_TELGR", terminal.getValue("VER_TELGR"), true)), readPermission, "150" ) %></td>
		</tr>
		<tr>
			<td><%= Bean.terminalXML.getfieldTransl("CRYPT_TELGR", !readPermission) %></td><td><%=Bean.getSelectElement("crypt_telgr", "CRYPT_TELGR", terminal.getValue("CRYPT_TELGR"), Bean.getMeaningForNumValue("YES_NO", terminal.getValue("CRYPT_TELGR")), new StringBuffer(Bean.getMeaningFromLookupNumberOptions("YES_NO", terminal.getValue("CRYPT_TELGR"), true)), readPermission, "150" ) %></td>
			<td valign="top"><%= Bean.terminalXML.getfieldTransl("nincmax", !readPermission) %></td>  <td><%=Bean.getInputTextElement("nincmax", "NINCMAX", terminal.getValue("NINCMAX"), readPermission, "150") %></td>
		</tr>
		<tr>
			<td><%= Bean.terminalXML.getfieldTransl("RESP_TIME", !readPermission) %></td> <td><%=Bean.getInputTextElement("resp_time", "RESP_TIME", terminal.getValue("RESP_TIME"), readPermission, "150") %></td>
			<td><%= Bean.terminalXML.getfieldTransl("connect_ekka", !readPermission) %></td> <td><%=Bean.getSelectElement("connect_ekka", "CONNECT_EKKA", terminal.getValue("CONNECT_EKKA"), Bean.getMeaningForNumValue("YES_NO", terminal.getValue("CONNECT_EKKA")), new StringBuffer(Bean.getMeaningFromLookupNumberOptions("YES_NO", terminal.getValue("CONNECT_EKKA"), true)), readPermission, "150" ) %></td>
		</tr>
		<tr>
			<td valign="top"><%= Bean.terminalXML.getfieldTransl("VK_ENC", !readPermission) %></td> <td valign="top"><%=Bean.getSelectElement("vk_enc", "VK_ENC", terminal.getValue("VK_ENC"), Bean.getMeaningFoCodeValue("VER_KEY", terminal.getValue("VK_ENC")), new StringBuffer(Bean.getMeaningFromLookupNameOptions("VER_KEY", terminal.getValue("VK_ENC"), true)), readPermission, "150" ) %></td>
			<td valign="top"><%= Bean.terminalXML.getfieldTransl("tr_limit", !readPermission) %></td>  <td><%=Bean.getInputTextElement("tr_limit", "TR_LIMIT", terminal.getValue("TR_LIMIT"), readPermission, "150") %></td>
		</tr>
		<% } else { %>
		<tr>
			<td><%= Bean.terminalXML.getfieldTransl("need_update_param", !readPermission) %></td> <td><%=Bean.getSelectElement("need_update_param", "", terminal.getValue("NEED_UPDATE_PARAM"), Bean.getMeaningFoCodeValue("YES_NO", terminal.getValue("NEED_UPDATE_PARAM")), new StringBuffer(Bean.getMeaningFromLookupNameOptions("YES_NO", terminal.getValue("NEED_UPDATE_PARAM"), true)), readPermission, "150" ) %></td>
		</tr>
		<% } %>

		<tr>
			<td colspan="4">&nbsp;</td>
		</tr>
		<% if (isPhisicalTerminal) { %>
		<tr>
			<td><b><%= Bean.terminalXML.getfieldTransl("h_bon_cards_types", false) %></b></td>
			<td><b><%= Bean.terminalXML.getfieldTransl("h_operations_types", false) %></b></td>
			<td colspan="2"><b><%= Bean.terminalXML.getfieldTransl("h_print_cheque_settings", false) %></b></td>
		</tr>
		<tr>
			<td><%=Bean.getCheckBoxBase("card_type_active_nsmep", terminal.getValue("CARD_TYPE_ACTIVE_NSMEP"), Bean.terminalXML.getfieldTransl("card_type_active_nsmep", false), true, readPermission) %></td>
			<td><%=Bean.getCheckBoxBase("oper_type_active_nsmep", terminal.getValue("OPER_TYPE_ACTIVE_NSMEP"), Bean.terminalXML.getfieldTransl("oper_type_active_nsmep", false), true, readPermission) %></td>
			<td><%=Bean.getCheckBoxBase("noprint_cheque_discount_club", terminal.getValue("NOPRINT_CHEQUE_DISCOUNT_CLUB"), Bean.terminalXML.getfieldTransl("noprint_cheque_discount_club", false), true, readPermission) %></td>
			<td><%=Bean.getCheckBoxBase("noprint_cheque_pay_cash", terminal.getValue("NOPRINT_CHEQUE_PAY_CASH"), Bean.terminalXML.getfieldTransl("noprint_cheque_pay_cash", false), true, readPermission) %></td>
		</tr>
		<tr>
			<td><%=Bean.getCheckBoxBase("card_type_active_magnetic", terminal.getValue("CARD_TYPE_ACTIVE_MAGNETIC"), Bean.terminalXML.getfieldTransl("card_type_active_magnetic", false), true, readPermission) %></td>
			<td><%=Bean.getCheckBoxBase("oper_type_active_cash", terminal.getValue("OPER_TYPE_ACTIVE_CASH"), Bean.terminalXML.getfieldTransl("oper_type_active_cash", false), true, readPermission) %></td>
			<td><%=Bean.getCheckBoxBase("noprint_cheque_bonus_club", terminal.getValue("NOPRINT_CHEQUE_BONUS_CLUB"), Bean.terminalXML.getfieldTransl("noprint_cheque_bonus_club", false), true, readPermission) %></td>
			<td><%=Bean.getCheckBoxBase("noprint_cheque_pay_card", terminal.getValue("NOPRINT_CHEQUE_PAY_CARD"), Bean.terminalXML.getfieldTransl("noprint_cheque_pay_card", false), true, readPermission) %></td>
		</tr>
		<tr>
			<td><%=Bean.getCheckBoxBase("card_type_active_chip", terminal.getValue("CARD_TYPE_ACTIVE_CHIP"), Bean.terminalXML.getfieldTransl("card_type_active_chip", false), true, readPermission) %></td>
			<td><%=Bean.getCheckBoxBase("oper_type_active_bon", terminal.getValue("OPER_TYPE_ACTIVE_BON"), Bean.terminalXML.getfieldTransl("oper_type_active_bon", false), true, readPermission) %></td>
			<td><%=Bean.getCheckBoxBase("noprint_cheque_mov_bon", terminal.getValue("NOPRINT_CHEQUE_MOV_BON"), Bean.terminalXML.getfieldTransl("noprint_cheque_mov_bon", false), true, readPermission) %></td>
			<td>&nbsp;</td>
		</tr>
		<tr>
			<td><%=Bean.getCheckBoxBase("card_type_active_emv", terminal.getValue("CARD_TYPE_ACTIVE_EMV"), Bean.terminalXML.getfieldTransl("card_type_active_emv", false), true, readPermission) %></td>
			<td><%=Bean.getCheckBoxBase("oper_type_active_cheque", terminal.getValue("OPER_TYPE_ACTIVE_CHEQUE"), Bean.terminalXML.getfieldTransl("oper_type_active_cheque", false), true, readPermission) %></td>
			<td><%=Bean.getCheckBoxBase("noprint_cheque_check_param", terminal.getValue("NOPRINT_CHEQUE_CHECK_PARAM"), Bean.terminalXML.getfieldTransl("noprint_cheque_check_param", false), true, readPermission) %></td>
			<td>&nbsp;</td>
		</tr>
		<tr>
			<td><%=Bean.getCheckBoxBase("card_type_active_barcode", terminal.getValue("CARD_TYPE_ACTIVE_BARCODE"), Bean.terminalXML.getfieldTransl("card_type_active_barcode", false), true, readPermission) %></td>
			<td><%=Bean.getCheckBoxBase("oper_type_active_emv", terminal.getValue("OPER_TYPE_ACTIVE_EMV"), Bean.terminalXML.getfieldTransl("OPER_TYPE_ACTIVE_EMV", false), true, readPermission) %></td>
			<td><%=Bean.getCheckBoxBase("noprint_cheque_pay_bon", terminal.getValue("noprint_cheque_pay_bon"), Bean.terminalXML.getfieldTransl("NOPRINT_CHEQUE_PAY_BON", false), true, readPermission) %></td>
			<td>&nbsp;</td>
		</tr>
		<tr>
			<td colspan="4">&nbsp;</td>
		</tr>

		<tr>
			<td colspan="2"><b><%= Bean.terminalXML.getfieldTransl("h_currencies_param", false) %></b></td>
			<td colspan="2"><b><%= Bean.terminalXML.getfieldTransl("h_points_param", false) %></b></td>
		</tr>
		<tr>
			<td><%= Bean.terminalXML.getfieldTransl("cd_term_currency", !readPermission) %></td> <td><%=Bean.getSelectElement("cd_term_currency", "", terminal.getValue("CD_TERM_CURRENCY"), Bean.getCurrencyNameById(terminal.getValue("CD_TERM_CURRENCY")), new StringBuffer(Bean.getCurrencyOptions(terminal.getValue("CD_TERM_CURRENCY"), true)), readPermission, "150" ) %></td>
			<td><%= Bean.terminalXML.getfieldTransl("can_calc_point", false) %></td> <td><%=Bean.getSelectElement("can_calc_point", "", terminal.getValue("CAN_CALC_POINT"), Bean.getMeaningForNumValue("YES_NO", terminal.getValue("CAN_CALC_POINT")), new StringBuffer(Bean.getMeaningFromLookupNumberOptions("YES_NO", terminal.getValue("CAN_CALC_POINT"), true)), readPermission, "150" ) %></td>
		</tr>
		<tr>
			<td><%= Bean.terminalXML.getfieldTransl("sname_term_currency", false) %></td> <td><%=Bean.getInputTextElement("sname_term_currency", "", terminal.getValue("SNAME_TERM_CURRENCY"), readPermission, "150") %></td>
			<td><%= Bean.terminalXML.getfieldTransl("sname_point", false) %></td> <td><%=Bean.getInputTextElement("sname_point", "", terminal.getValue("SNAME_POINT"), readPermission, "150") %></td>
		</tr>
		<tr>
			<td><%= Bean.terminalXML.getfieldTransl("can_oper_different_currency", !readPermission) %></td> <td><%=Bean.getSelectElement("can_oper_different_currency", "", terminal.getValue("CAN_OPER_DIFFERENT_CURRENCY"), Bean.getMeaningForNumValue("YES_NO", terminal.getValue("CAN_OPER_DIFFERENT_CURRENCY")), new StringBuffer(Bean.getMeaningFromLookupNumberOptions("YES_NO", terminal.getValue("CAN_OPER_DIFFERENT_CURRENCY"), true)), readPermission, "150" ) %></td>
			<td><%= Bean.terminalXML.getfieldTransl("multiplicator_point", false) %></td> <td><%=Bean.getInputTextElement("multiplicator_point", "", terminal.getValue("MULTIPLICATOR_POINT"), readPermission, "150") %></td>
		</tr>
		<tr>
			<td colspan="4">&nbsp;</td>
		</tr>

		<tr>
			<td colspan="4"><b><%= Bean.terminalXML.getfieldTransl("h_need_online_pin", false) %></b></td>
		</tr>
		<tr>
			<td><%= Bean.terminalXML.getfieldTransl("need_calc_online_bon_pay_pin", false) %></td> <td><%=Bean.getSelectElement("need_calc_online_bon_pay_pin", "", terminal.getValue("NEED_CALC_ONLINE_BON_PAY_PIN"), Bean.getMeaningFoCodeValue("YES_NO", terminal.getValue("NEED_CALC_ONLINE_BON_PAY_PIN")), new StringBuffer(Bean.getMeaningFromLookupNameOptions("YES_NO", terminal.getValue("NEED_CALC_ONLINE_BON_PAY_PIN"), true)), readPermission, "150" ) %></td>
			<td colspan="2"><%= Bean.terminalXML.getfieldTransl("need_calc_online_club_pay_pin", true) %></td>
		</tr>
		<tr>
			<td><%= Bean.terminalXML.getfieldTransl("need_calc_online_storno_pin", false) %></td> <td><%=Bean.getSelectElement("need_calc_online_storno_pin", "", terminal.getValue("NEED_CALC_ONLINE_STORNO_PIN"), Bean.getMeaningFoCodeValue("YES_NO", terminal.getValue("NEED_CALC_ONLINE_STORNO_PIN")), new StringBuffer(Bean.getMeaningFromLookupNameOptions("YES_NO", terminal.getValue("NEED_CALC_ONLINE_STORNO_PIN"), true)), readPermission, "150" ) %></td>
			<td colspan="2">&nbsp;</td>
		</tr>
		<% } else { %>

		<tr>
			<td colspan="2"><b><%= Bean.terminalXML.getfieldTransl("h_currencies_param", false) %></b></td>
			<td colspan="2"><b><%= Bean.terminalXML.getfieldTransl("h_print_cheque_settings", false) %></b></td>
		</tr>
		<tr>
			<td><%= Bean.terminalXML.getfieldTransl("cd_term_currency", !readPermission) %></td> <td><%=Bean.getSelectElement("cd_term_currency", "", terminal.getValue("CD_TERM_CURRENCY"), Bean.getCurrencyNameById(terminal.getValue("CD_TERM_CURRENCY")), new StringBuffer(Bean.getCurrencyOptions(terminal.getValue("CD_TERM_CURRENCY"), true)), readPermission, "150" ) %></td>
			<td><%=Bean.getCheckBoxBase("oper_save_cheque", terminal.getValue("OPER_SAVE_CHEQUE"), Bean.terminalXML.getfieldTransl("oper_save_cheque", false), false, readPermission) %></td>
			<td>&nbsp;</td>
		</tr>
		<tr>
			<td><%= Bean.terminalXML.getfieldTransl("sname_term_currency", false) %></td> <td><%=Bean.getInputTextElement("sname_term_currency", "", terminal.getValue("SNAME_TERM_CURRENCY"), readPermission, "150") %></td>
			<td><%=Bean.getCheckBoxBase("oper_sms_cheque", terminal.getValue("OPER_SMS_CHEQUE"), Bean.terminalXML.getfieldTransl("oper_sms_cheque", false), false, readPermission) %></td>
			<td>&nbsp;</td>
		</tr>
		<tr>
			<td colspan="4">&nbsp;</td>
		</tr>
		<tr>
			<td colspan="4"><b><%= Bean.terminalXML.getfieldTransl("h_online_confirmations", false) %></b></td>
		</tr>
		<tr>
			<td valign="top"><%= Bean.terminalXML.getfieldTransl("cd_online_pay_cash_conf_type", false) %></td> <td valign="top"><%=Bean.getSelectElement("cd_online_pay_cash_conf_type", "", terminal.getValue("CD_ONLINE_PAY_CASH_CONF_TYPE"), Bean.getTermPayConfirmationWayName(terminal.getValue("CD_ONLINE_PAY_CASH_CONF_TYPE")), new StringBuffer(Bean.getTermPayConfirmationWayOptions(terminal.getValue("CD_ONLINE_PAY_CASH_CONF_TYPE"), true)), readPermission, "150" ) %></td>
			<td valign="top"><%= Bean.terminalXML.getfieldTransl("cd_online_pay_rbk_conf_type", false) %></td> <td valign="top"><%=Bean.getSelectElement("cd_online_pay_rbk_conf_type", "", terminal.getValue("CD_ONLINE_PAY_RBK_CONF_TYPE"), Bean.getTermPayConfirmationWayName(terminal.getValue("CD_ONLINE_PAY_RBK_CONF_TYPE")), new StringBuffer(Bean.getTermPayConfirmationWayOptions(terminal.getValue("CD_ONLINE_PAY_RBK_CONF_TYPE"), true)), readPermission, "150" ) %></td>
		</tr>
		<tr>
			<td valign="top"><%= Bean.terminalXML.getfieldTransl("cd_online_pay_card_conf_type", false) %></td> <td valign="top"><%=Bean.getSelectElement("cd_online_pay_card_conf_type", "", terminal.getValue("CD_ONLINE_PAY_CARD_CONF_TYPE"), Bean.getTermPayConfirmationWayName(terminal.getValue("CD_ONLINE_PAY_CARD_CONF_TYPE")), new StringBuffer(Bean.getTermPayConfirmationWayOptions(terminal.getValue("CD_ONLINE_PAY_CARD_CONF_TYPE"), true)), readPermission, "150" ) %></td>
			<td valign="top"><%= Bean.terminalXML.getfieldTransl("has_online_pay_rbk_permission", false) %></td> <td valign="top"><%=Bean.getSelectElement("has_online_pay_rbk_permission", "", terminal.getValue("HAS_ONLINE_PAY_RBK_PERMISSION"), Bean.getMeaningFoCodeValue("YES_NO", terminal.getValue("HAS_ONLINE_PAY_RBK_PERMISSION")), new StringBuffer(Bean.getMeaningFromLookupNameOptions("YES_NO", terminal.getValue("HAS_ONLINE_PAY_RBK_PERMISSION"), true)), readPermission, "150" ) %></td>
		</tr>
		<tr>
			<td valign="top"><%= Bean.terminalXML.getfieldTransl("cd_online_pay_point_conf_type", false) %></td> <td valign="top"><%=Bean.getSelectElement("cd_online_pay_point_conf_type", "", terminal.getValue("CD_ONLINE_PAY_POINT_CONF_TYPE"), Bean.getTermPayConfirmationWayName(terminal.getValue("CD_ONLINE_PAY_POINT_CONF_TYPE")), new StringBuffer(Bean.getTermPayConfirmationWayOptions(terminal.getValue("CD_ONLINE_PAY_POINT_CONF_TYPE"), true)), readPermission, "150" ) %></td>
			<td colspan="2">&nbsp;</td>
		</tr>
		<% } %>
		<tr>
			<td colspan="4">&nbsp;</td>
		</tr>

		<tr><td colspan="4"><b><%= Bean.terminalXML.getfieldTransl("title_session_parameters", false) %>&nbsp;<span onclick="show_addit_param();" id="addit_button">&gt;&gt;</span></b></td></tr>
		<tr id="addit_row1" style="display:none">
			<td class="gray_background"><%= Bean.terminalXML.getfieldTransl("IS_OPENED_SESSION", false) %></td><td class="gray_background"><select name="IS_OPENED_SESSION" class="inputfield"><%= Bean.getMeaningFromLookupNumHTML("YES_NO", terminal.getValue("IS_OPENED_SESSION"), false) %></select></td>
			<td class="gray_background"><%= Bean.terminalXML.getfieldTransl("ID_LAST_SES", false) %>
				<%= Bean.getGoToTermSessionLink(terminal.getValue("ID_LAST_SES")) %>
			</td><td class="gray_background"><input type="text" name="ID_LAST_SES" size="20" value="<%= terminal.getValue("ID_LAST_SES") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr id="addit_row2" style="display:none">
			<td class="gray_background"><%= Bean.terminalXML.getfieldTransl("ERROR_MESSAGES_CNT", false) %></td><td class="gray_background"><input type="text" name="ERROR_MESSAGES_CNT" size="8" value="<%=terminal.getValue("ERROR_MESSAGES_CNT")%>" class="inputfield"></td>
			<td class="gray_background"><%= Bean.terminalXML.getfieldTransl("DATE_LAST_SES", false) %></td><td class="gray_background"><input type="text" name="DATE_LAST_SES" size="20" value="<%= terminal.getValue("DATE_LAST_SES_FRMT") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr id="addit_row3" style="display:none">
			<td class="gray_background"><%= Bean.terminalXML.getfieldTransl("DATE_LAST_TELGR", false) %></td><td class="gray_background"><input type="text" name="DATE_LAST_TELGR" size="20" value="<%= terminal.getValue("DATE_LAST_TELGR_FRMT")%>" readonly="readonly" class="inputfield-ro"></td>
			<td class="gray_background"><%= Bean.terminalXML.getfieldTransl("ID_SAM_LAST_SES", false) %>
				<%= Bean.getGoToSAMLink(terminal.getValue("ID_SAM_LAST_SES")) %>
			</td><td class="gray_background"><input type="text" name="ID_SAM_LAST_SES" size="20" value="<%=terminal.getValue("ID_SAM_LAST_SES") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr id="addit_row4" style="display:none">
			<td class="gray_background"><%= Bean.terminalXML.getfieldTransl("CREATING_APP_TRAR_DATA_DATE", false) %></td><td class="gray_background"><input type="text" name="CREATING_APP_TRAR_DATA_DATE" size="20" value="<%= terminal.getValue("CREATING_APP_TRAR_DATA_DATE_DHMSF") %>" readonly="readonly" class="inputfield-ro"></td>
			<td class="gray_background"><%= Bean.terminalXML.getfieldTransl("NT_SAM_LAST_SES", false) %></td><td class="gray_background"><input type="text" name="NT_SAM_LAST_SES" size="20" value="<%= terminal.getValue("NT_SAM_LAST_SES")%>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr id="addit_row5" style="display:none">
			<td class="gray_background"><%= Bean.terminalXML.getfieldTransl("date_last_term_col_data", false) %></td><td class="gray_background"><input type="text" name="date_last_term_col_data" size="20" value="<%= terminal.getValue("DATE_LAST_TERM_COL_DATA_DHMSF") %>" readonly="readonly" class="inputfield-ro"></td>
			<td class="gray_background"><%= Bean.terminalXML.getfieldTransl("NT_MSG_B_LAST_SES", false) %></td> <td class="gray_background"><input type="text" name="NT_MSG_B_LAST_SES" size="20" value="<%= terminal.getValue("NT_MSG_B_LAST_SES") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr id="addit_row6" style="display:none">
			<td class="gray_background"><%= Bean.terminalXML.getfieldTransl("date_last_term_card_req", false) %></td><td class="gray_background"><input type="text" name="date_last_term_card_req" size="20" value="<%= terminal.getValue("DATE_LAST_TERM_CARD_REQ_DHMSF") %>" readonly="readonly" class="inputfield-ro"></td>
			<td class="gray_background"><%= Bean.terminalXML.getfieldTransl("date_last_rec_payment", false) %></td> <td class="gray_background"><input type="text" name="date_last_rec_payment" size="20" value="<%= terminal.getValue("DATE_LAST_REC_PAYMENT_DHMSF") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr id="addit_row7" style="display:none">
			<td class="gray_background"><%= Bean.terminalXML.getfieldTransl("date_last_rec_mov_bon", false) %></td><td class="gray_background"><input type="text" name="date_last_rec_mov_bon" size="20" value="<%= terminal.getValue("DATE_LAST_REC_MOV_BON_DHMSF") %>" readonly="readonly" class="inputfield-ro"></td>
			<td class="gray_background"><%= Bean.terminalXML.getfieldTransl("date_last_rec_chk_card", false) %></td> <td class="gray_background"><input type="text" name="date_last_rec_chk_card" size="20" value="<%= terminal.getValue("DATE_LAST_REC_CHK_CARD_DHMSF") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr id="addit_row8" style="display:none">
			<td class="gray_background"><%= Bean.terminalXML.getfieldTransl("date_last_rec_inval_card", false) %></td><td class="gray_background"><input type="text" name="date_last_rec_inval_card" size="20" value="<%= terminal.getValue("DATE_LAST_REC_INVAL_CARD_DHMSF") %>" readonly="readonly" class="inputfield-ro"></td>
			<td class="gray_background"><%= Bean.terminalXML.getfieldTransl("date_last_rec_storno_bon", false) %></td> <td class="gray_background"><input type="text" name="date_last_rec_storno_bon" size="20" value="<%= terminal.getValue("DATE_LAST_REC_STORNO_BON_DHMSF") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>

		<%=	Bean.getIdCreationAndMoficationRecordFields(
				terminal.getValue("ID_TERM"),
				terminal.getValue(Bean.getCreationDateFieldName()),
				terminal.getValue("CREATED_BY"),
				terminal.getValue(Bean.getLastUpdateDateFieldName()),
				terminal.getValue("LAST_UPDATE_BY")
			) %>
		<tr>
			<td colspan="4" align="center">
				<% if (!readPermission) { %>
				<%=Bean.getSubmitButtonAjax("../crm/clients/terminalupdate.jsp", "submit", "updateForm", "div_data_detail") %>
				<%=Bean.getResetButton() %>
				<% } %>
				<%=Bean.getGoBackButton("../crm/clients/terminals.jsp") %>
			</td>
		</tr>
	</table>
	</form> 
	<% if (!readPermission) { %>
		<%= Bean.getCalendarScript("club_registration_date", false) %>
		<%= Bean.getCalendarScript("date_location", false) %>
		<% if (isPhisicalTerminal) { %>
		<%= Bean.getCalendarScript("term_mon_rep_next_date", true) %>
		<% } %>
	<% } %>
<%	}

if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLIENTS_TERMINALS_SAM")) {%>
	<table <%=Bean.getTableBottomFilter() %>>
	  	<tr>
		<%= Bean.getFindHTML("sam_find", sam_find, termParamLink + "&sam_page=1&") %>


		<%=Bean.getSelectOnChangeBeginHTML("sam_status", termParamLink + "&sam_page=1&", Bean.samXML.getfieldTransl("name_sam_status", false)) %>
			<%= Bean.getSAMStatusOptions(sam_status, true) %>
		<%=Bean.getSelectOnChangeEndHTML() %>
	  	</tr>
	</table>
	<%= terminal.getTermSAMHTML(sam_find, sam_status, l_sam_page_beg, l_sam_page_end) %>
<%}



if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLIENTS_TERMINALS_LOY")) {
	
	 boolean hasEditPerm = Bean.currentMenu.isCurrentTabAndEditPermitted("CLIENTS_TERMINALS_LOY");
	 
	if ("all".equalsIgnoreCase(hist)) {
	
	%>
	<br>
	<div id="div_oper_caption"><center>
		<b style="font-size:14px;"><%= Bean.terminalXML.getfieldTransl("h_loyality_history", false) %></b>
		<br>
		<%=Bean.getGoBackButton(termParamLink, "button_back") %>
	</center></div>
	<br>
	<%
	termParamLink = "../crm/clients/terminalspecs.jsp?id=" + terminal.getValue("ID_TERM") + "&hist=all&id_loy_history="+id_loy_history;
	%>
	<table <%=Bean.getTableBottomFilter() %>>	
		<tr>
			<%=Bean.getFindHTML("loy_history_find", loy_history_find, termParamLink + "&loy_history_page=1") %>
		
			<!-- Вывод страниц -->
			<%= Bean.getPagesHTML(pageFormName + tagLoyHistory, termParamLink + "&tab="+Bean.currentMenu.getTabID("CLIENTS_TERMINALS_LOY")+"&comission_rel_type="+comission_rel_type+"&", "loy_history_page") %>			

		</tr> 
	</table>	
		<%= terminal.getTermLoyalityHistoryHTML(loy_history_find, l_loy_history_page_beg, l_loy_history_page_end) %>
	<%} else {

	String resp_time 				= param.getValue("RESP_TIME");
	String ver_telgr 				= param.getValue("VER_TELGR");
	String crypt_telgr		 		= param.getValue("CRYPT_TELGR");
	String crypt_telgr_name 		= Bean.getMeaningForNumValue("YES_NO", param.getValue("CRYPT_TELGR"));
	String vk_enc 					= param.getValue("VK_ENC");
	
	String
	    id_loyality_scheme_history  = "",
	    id_loyality_scheme		    = "",
	  	name_loyality_scheme		= "", 
	  	cd_kind_loyality 			= "", 
	  	name_kind_loyality 			= "", 
	  	cd_name_kind_loyality 		= "", 
	  	type_calc_tsl 				= "", 
	  	max_bonus_frmt 				= "", 
	  	cash_bon_tsl 				= "",
	  	min_amount_frmt 			= "",
	  	bon_bon_tsl 				= "",
	  	max_sumpayoffline_frmt 		= "",
	  	rounding_rule_tsl 			= "",
	  	max_sumpaynopin_frmt 		= "",
	  	ext_loyl 					= "",
	  	loyality_for_all_nsmep		= "",
	  	limit_cash					= "",
	  	max_date_onl_term 			= "",
	  	nomenkl			 			= "",
	  	date_beg                	= "",
	  	date_end                	= "",
	  	snameCurrency               = "";
	 
	if (!Bean.isEmpty(id_loy_history)) {
	  	String id_term = loy.getValue("ID_TERM");
	    System.out.println("id_loy_history="+id_loy_history);
	  	id_loyality_scheme_history = loy.getValue("ID_LOYALITY_HISTORY");
	 	id_loyality_scheme 		= loy.getValue("ID_LOYALITY_SCHEME");
		name_loyality_scheme		= loy.getValue("NAME_LOYALITY_SCHEME"); 
		cd_kind_loyality 			= loy.getValue("CD_KIND_LOYALITY");
		name_kind_loyality 		= loy.getValue("NAME_KIND_LOYALITY"); 
		cd_name_kind_loyality		= cd_kind_loyality + " - " + name_kind_loyality;
		type_calc_tsl 				= loy.getValue("TYPE_CALC_NAME"); 
		max_bonus_frmt 			= loy.getValue("MAX_BONUS_FRMT"); 
		cash_bon_tsl 				= loy.getValue("CASH_BON_NAME");
		min_amount_frmt 			= loy.getValue("MIN_AMOUNT_FRMT");
		bon_bon_tsl 				= loy.getValue("BON_BON_TSL");
		max_sumpayoffline_frmt 	= loy.getValue("MAX_SUMPAYOFFLINE_FRMT");
		rounding_rule_tsl 			= loy.getValue("ROUNDING_RULE_NAME");
		max_sumpaynopin_frmt 		= loy.getValue("MAX_SUMPAYNOPIN_FRMT");
		ext_loyl 					= loy.getValue("EXT_LOYL_TSL");
		max_date_onl_term 			= loy.getValue("MAX_DATE_ONL_TERM");
		loyality_for_all_nsmep		= loy.getValue("LOYALITY_FOR_ALL_NSMEP_TSL");
		limit_cash		 			= loy.getValue("LIMIT_CASH_FRMT");
		nomenkl		 			= loy.getValue("NOMENKL");
		date_beg 					= loy.getValue("DATE_BEG_FRMT");
		date_end		 			= loy.getValue("DATE_END_FRMT");
		snameCurrency	 			= ", " + Bean.getCurrencyShortNameById(loy.getValue("CD_CURRENCY"));
	}
	 %>
	
	<script>
		<% if (isPhisicalTerminal) { %>
		var formTermHistoryParam = new Array (
				new Array ('id_term', 'varchar2', 1),
				new Array ('resp_time_history', 'varchar2', 1),
				new Array ('ver_telgr_history', 'varchar2', 1),
				new Array ('crypt_telgr_history', 'varchar2', 1),
				new Array ('vk_enc_history', 'varchar2', 1)
		);
		<% } else { %>
		var formTermHistoryParam = new Array (
				new Array ('id_term', 'varchar2', 1)
		);
		<% } %>

		function myValidateForm(){
			return validateForm(formTermHistoryParam);
		}

		function changeLoyHistoryParam(id_history) {
			var linkPage = '../crm/clients/terminalspecs.jsp?id=<%=termid %>&loy_lines_page=1&id_loy_history='+id_history;
			//alert(linkPage);
			ajaxpage(linkPage, 'div_main');
		}

	</script>
	
	<form id="updateForm" name="updateForm" action="../crm/clients/terminalspecs.jsp" method="post" >
		<input type="hidden" name="id_term" value="<%=terminal.getValue("ID_TERM") %>">
		<input type="hidden" name="id_loy_history" value="<%=id_loy_history %>">
		<input type="hidden" name="datetime" value="">

	<table <%=Bean.getTableDetailParam() %>>
		<tr>
			<td colspan="4"><b><font color="red"><%= Bean.loyXML.getfieldTransl("h_new_parameters", false) %></font></b></td>
		</tr>
		<% if (hasEditPerm) { %>
		<tr>
			<td><%= Bean.terminalXML.getfieldTransl("NAME_LOYALITY_SCHEME", false) %>
				<%= Bean.getGoToLoyalityLink(terminal.getValue("ID_LOYALITY_SCHEME_NEXT")) %>
			</td>
			<td>
				<%=Bean.getWindowFindLoyScheme("loyality_scheme_next", terminal.getValue("ID_LOYALITY_SCHEME_NEXT"), "40", false) %>
			</td>			
			<td><%= Bean.terminalXML.getfieldTransl("autosynchronize_loy_param", true) %></td><td><select name="autosynchronize_loy_param" id="autosynchronize_loy_param" class="inputfield"><%= Bean.getMeaningFromLookupNameOptions("YES_NO", terminal.getValue("AUTOSYNCHRONIZE_LOYALITY_PARAM"), false) %></select></td>
		</tr>
		<tr>
			<td><%= Bean.terminalXML.getfieldTransl("ID_SHEDULE", false) %>
				<%= Bean.getGoToLoyalitySheduleLink(terminal.getValue("ID_LOYALITY_SHEDULE")) %>
			</td>
			<td>
				<%=Bean.getWindowFindLoySchedule("shedule",  terminal.getValue("ID_LOYALITY_SHEDULE"), "40", false) %>
			</td>			
			<td>&nbsp;</td><td>&nbsp;</td>
		</tr>
		<tr>
			<td colspan="4" align="center">
				<%=Bean.getSubmitButtonAjax("../crm/clients/terminalupdate.jsp?id_term=" + terminal.getValue("ID_TERM") + "&type=loy&action=edit&process=yes&id_loyality_scheme_next='+getElementById('id_loyality_scheme_next').value+'&id_shedule='+getElementById('id_shedule').value+'&autosynchronize_loy_param='+getElementById('autosynchronize_loy_param').value", "submit", "updateForm", "div_data_detail") %>
				<%=Bean.getResetButton() %>
			</td>
		</tr>
		<% } else { %>
		<tr>
			<td><%= Bean.terminalXML.getfieldTransl("NAME_LOYALITY_SCHEME", false) %>
				<%= Bean.getGoToLoyalityLink(terminal.getValue("ID_LOYALITY_SCHEME_NEXT")) %>
            </td>
			<td><input type="text" id="name_loyality_scheme_next" name="name_loyality_scheme_next" size="40" value="<%= Bean.getLoySchemeCdAndName(terminal.getValue("ID_LOYALITY_SCHEME_NEXT")) %>" readonly="readonly" class="inputfield-ro">
			</td>			
			<td><%= Bean.terminalXML.getfieldTransl("autosynchronize_loy_param", false) %></td> <td><input type="text" name="autosynchronize_loy_param" size="20" value="<%=Bean.getMeaningFoCodeValue("YES_NO", terminal.getValue("AUTOSYNCHRONIZE_LOYALITY_PARAM")) %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td><%= Bean.terminalXML.getfieldTransl("ID_SHEDULE", false) %>
				<%= Bean.getGoToLoyalitySheduleLink(terminal.getValue("ID_LOYALITY_SHEDULE")) %>
			</td>
			<td>
				<input type="text" id="name_shedule" name="name_shedule" size="40" value="<%= Bean.getLoyScheduleName(terminal.getValue("ID_LOYALITY_SHEDULE")) %>" readonly="readonly" class="inputfield-ro">
			</td>			
			<td>&nbsp;</td><td>&nbsp;</td>
		</tr>
		<% } %>
		<tr>
			<td colspan="4" class="top_line"><b>
			<% if (id_loy_history.equalsIgnoreCase(terminal.getValue("ID_LOYALITY_HISTORY"))) { %>
				<%= Bean.loyXML.getfieldTransl("h_current_parameters", false) %>
			<% } else { %>
				<font color="red"><%= Bean.loyXML.getfieldTransl("h_historical_parameters", false) %></font>
			<% } %>

			<% if ("".equalsIgnoreCase(id_loyality_scheme_history)) { %>
				<font color="red">&nbsp;(<%= Bean.terminalXML.getfieldTransl("data_not_found", false).toLowerCase() %>)</font>
			<% } else { %>
				<% if ("Y".equalsIgnoreCase(loy.getValue("WAS_END"))) { %><font color="red">&nbsp;(<%= Bean.terminalXML.getfieldTransl("was_end", false).toLowerCase() %>)</font><% } %>
			<% } %>
			</b>&nbsp;&nbsp;
			<span class="div_href2" onclick="ajaxpage('../crm/clients/terminalspecs.jsp?id=<%=termid %>&hist=all', 'div_main')"><%= Bean.terminalXML.getfieldTransl("h_loyality_history", false) %></span>
			<% if (!id_loy_history.equalsIgnoreCase(terminal.getValue("ID_LOYALITY_HISTORY"))) { %>
				<% if (!Bean.isEmpty(terminal.getValue("ID_LOYALITY_HISTORY"))) { %>
				&nbsp;&nbsp;
				(<span class="div_href3" onclick="ajaxpage('../crm/clients/terminalspecs.jsp?id=<%=termid %>&hist=y&id_loy_history=<%=terminal.getValue("ID_LOYALITY_HISTORY")%>', 'div_main')"><%= Bean.terminalXML.getfieldTransl("h_goto_current_loyality", false).toLowerCase() %></span>)
				<% } %>
			<% } %>
			</td>
		</tr>
		<% 
			boolean hasParamEditPermission = 
			hasEditPerm && 
			(!(terminal.getValue("ID_PARAM_HISTORY")==null || "".equalsIgnoreCase(terminal.getValue("ID_PARAM_HISTORY")))) &&
			id_loy_history.equalsIgnoreCase(terminal.getValue("ID_LOYALITY_HISTORY")); %>

		<% if (!Bean.isEmpty(id_loyality_scheme_history)) { %>
		<tr>
			<td><%= Bean.terminalXML.getfieldTransl("NAME_LOYALITY_SCHEME", false) %>
				<%= Bean.getGoToLoyalityLink(id_loyality_scheme) %>
            </td>
            <td><input type="text" name="name_loyality_scheme" size="50" value="<%= name_loyality_scheme %>" readonly="readonly" class="inputfield-ro"></td>
			<% if (hasParamEditPermission) { %>
			<td><%= Bean.terminalXML.getfieldTransl("RESP_TIME", true) %></td> <td><input type="text" name="resp_time_history" id="resp_time_history" size="20" value="<%=resp_time %>" class="inputfield" title="RESP_TIME"></td>
			<% } else { %>
			<td><%= Bean.terminalXML.getfieldTransl("RESP_TIME", false) %></td> <td><input type="text" name="resp_time_history" size="20" value="<%=resp_time %>" readonly="readonly" class="inputfield-ro" title="RESP_TIME"></td>
			<% } %>
		</tr>
		<tr>
			<td><%= Bean.terminalXML.getfieldTransl("NAME_KIND_LOYALITY", false) %></td><td><input type="text" name="name_kind_loyality" size="50" value="<%= cd_name_kind_loyality %>" readonly="readonly" class="inputfield-ro"></td>
			<% if (hasParamEditPermission) { %>
			<td><%= Bean.terminalXML.getfieldTransl("VER_TELGR", true) %></td> <td><select name="ver_telgr_history" id="ver_telgr_history" class="inputfield" title="TEL_VERSION"><%= Bean.getMeaningFromLookupNameOptions("VER_TELGR", ver_telgr, true) %></select></td>
			<% } else { %>
			<td><%= Bean.terminalXML.getfieldTransl("VER_TELGR", false) %></td> <td><input type="text" name="ver_telgr_history" size="20" value="<%= ver_telgr %>" readonly="readonly" class="inputfield-ro" title="TEL_VERSION"> </td>
			<% } %>
		</tr>
		<tr>
			<td><%= Bean.terminalXML.getfieldTransl("date_beg", false) %></td><td><input type="text" name="date_beg" size="20" value="<%=date_beg%>" readonly="readonly" class="inputfield-ro"></td>
			<% if (hasParamEditPermission) { %>
			<td><%= Bean.terminalXML.getfieldTransl("CRYPT_TELGR", true) %></td><td><select name="crypt_telgr_history" id="crypt_telgr_history" class="inputfield" title="CRYPT_TELGR"><%= Bean.getMeaningFromLookupNumHTML("YES_NO", crypt_telgr, true) %></select></td>
			<% } else { %>
			<td><%= Bean.terminalXML.getfieldTransl("CRYPT_TELGR_TSL", false) %></td><td><input type="text" name="crypt_telgr_history" size="20" value="<%= crypt_telgr_name %>" readonly="readonly" class="inputfield-ro" title="CRYPT_TELGR"></td>
			<% } %>
		</tr>
		<tr>
			<td><%= Bean.terminalXML.getfieldTransl("date_end", false) %></td><td><input type="text" name="date_end" size="20" value="<%=date_end%>" readonly="readonly" class="inputfield-ro"></td>
			<% if (hasParamEditPermission) { %>
			<td valign="top"><%= Bean.terminalXML.getfieldTransl("VK_ENC", true) %></td> <td valign="top"><select name="vk_enc_history" id="vk_enc_history" class="inputfield" title="VK_ENC"><%= Bean.getMeaningFromLookupNameOptions("VER_KEY", vk_enc, true) %></select></td>
			<% } else { %>
			<td><%= Bean.terminalXML.getfieldTransl("VK_ENC", false) %></td> <td><input type="text" name="vk_enc_history" size="20" value="<%= vk_enc %>" readonly="readonly" class="inputfield-ro" title="VK_ENC"> </td>
			<% } %>
		</tr>
		<% if (hasParamEditPermission) { %>
		<tr>
			<td>&nbsp;</td><td>&nbsp;</td>
			<td colspan="2" align="center">
				<%=Bean.getSubmitButtonAjax("../crm/clients/terminalupdate.jsp?id_term=" + terminal.getValue("ID_TERM") + "&id_param_history=" + terminal.getValue("ID_PARAM_HISTORY") + "&type=current_param&action=edit&process=yes&resp_time_history='+getElementById('resp_time_history').value+'&ver_telgr_history='+getElementById('ver_telgr_history').value+'&crypt_telgr_history='+getElementById('crypt_telgr_history').value+'&vk_enc_history='+getElementById('vk_enc_history').value", "submit", "updateForm", "div_data_detail") %>
				<%=Bean.getResetButton() %>
			</td>
		</form>
		</tr>
		<% } %>

		<tr>
			<td class="top_line"><%= Bean.terminalXML.getfieldTransl("TYPE_CALC_TSL", false) %></td><td class="top_line"><input type="text" name="type_calc_name" size="50" value="<%= type_calc_tsl %>" readonly="readonly" class="inputfield-ro" title="TYPE_CALC"></td>
			<td class="top_line">&nbsp;</td> <td class="top_line">&nbsp;</td>
		</tr>
		<tr>
			<td><%= Bean.terminalXML.getfieldTransl("MAX_BONUS", false) %><%=snameCurrency %></td> <td><input type="text" name="max_bonus" size="20" value="<%= max_bonus_frmt %>" readonly="readonly" class="inputfield-ro" title="MAX_BONUS"> </td>
			<td><%= Bean.terminalXML.getfieldTransl("CASH_BON_TSL", false) %></td><td><input type="text" name="cash_bon_name" size="20" value="<%= cash_bon_tsl %>" readonly="readonly" class="inputfield-ro" title="CASH_BON"></td>
		</tr>
		<tr>
			<td><%= Bean.terminalXML.getfieldTransl("MIN_AMOUNT", false) %><%=snameCurrency %></td> <td><input type="text" name="min_amount" size="20" value="<%= min_amount_frmt %>" readonly="readonly" class="inputfield-ro" title="MIN_AMOUNT"> </td>
			<td><%= Bean.terminalXML.getfieldTransl("BON_BON", false) %></td> <td><input type="text" name="bon_bon" size="20" value="<%= bon_bon_tsl %>" readonly="readonly" class="inputfield-ro" title="BON_BON"> </td>
		</tr>
		<tr>
			<td><%= Bean.terminalXML.getfieldTransl("MAX_SUMPAYOFFLINE", false) %><%=snameCurrency %></td> <td><input type="text" name="max_sumpayoffline" size="20" value="<%= max_sumpayoffline_frmt %>" readonly="readonly" class="inputfield-ro" title="MAX_SUMPAYOFFLINE"> </td>
			<td><%= Bean.terminalXML.getfieldTransl("ROUNDING_RULE_TSL", false) %></td><td><input type="text" name="rounding_rule_name" size="20" value="<%= rounding_rule_tsl %>" readonly="readonly" class="inputfield-ro" title="ROUNDING_RULE"></td>
		</tr>
		<tr>
			<td><%= Bean.terminalXML.getfieldTransl("MAX_SUMPAYNOPIN", false) %><%=snameCurrency %></td> <td><input type="text" name="max_sumpaynopin" size="20" value="<%= max_sumpaynopin_frmt %>" readonly="readonly" class="inputfield-ro" title="MAX_SUMPAYNOPIN"> </td>
			<td><%= Bean.loyXML.getfieldTransl("term_loyality_for_all_nsmep", false) %></td> <td> <input type="text" name="loyality_for_all_nsmep" size="20" value="<%= loyality_for_all_nsmep %>" readonly="readonly" class="inputfield-ro" title="MAX_DATE_ONL_TEMR"> </td>
		</tr>
		<tr>
			<td><%= Bean.loyXML.getfieldTransl("term_max_date_onl_term", false) %> </td> <td><input type="text" name="MAX_DATE_ONL_TERM" size="20" value="<%= max_date_onl_term %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.terminalXML.getfieldTransl("EXT_LOYL", false) %></td> <td><input type="text" name="ext_loyl" size="20" value="<%= ext_loyl %>" readonly="readonly" class="inputfield-ro" title="EXT_LOYL"> </td>
		</tr>
		<tr>
			<td><%= Bean.loyXML.getfieldTransl("term_limit_cash", false) %></td> <td><input type="text" name="limit_cash" size="20" value="<%= limit_cash %>" readonly="readonly" class="inputfield-ro" title="TR_LIMIT"> </td>
			<td><%= Bean.terminalXML.getfieldTransl("nomenkl", false) %></td> <td> <input type="text" name="nomenkl" size="20" value="<%= nomenkl %>" readonly="readonly" class="inputfield-ro" title="NOMENKL: <%=nomenkl %>"> </td>
		</tr>
		<tr>
			<td colspan="2">&nbsp;</td>
		</tr>
		<% } else { %>

		<tr>
			<td colspan="2">&nbsp;</td>
		</tr>
		<% } %>
	</table>


	<script>
		function changeLoyHistoryParam(id_history) {
			var linkPage = '../crm/clients/terminalspecs.jsp?id=<%=termid %>&loy_lines_page=1&id_loy_history='+id_history;
			//alert(linkPage);
			ajaxpage(linkPage, 'div_main');
		}

	</script>
	<br>
	<% if (!Bean.isEmpty(id_loyality_scheme_history)) { %>
	<table <%=Bean.getTableBottomFilter() %>>		
		<tr>
			<td colspan="4"><b><%= Bean.loyXML.getfieldTransl("CLIENTS_LOY_BON", false) %></b></td>
		</tr>
		<tr>
			<%=Bean.getFindHTML("loy_lines_find", loy_lines_find, termParamLink + "&loy_lines_page=1") %>
		
			<%=Bean.getSelectOnChangeBeginHTML("cd_kind", termParamLink + "&loy_lines_page=1", Bean.loylineXML.getfieldTransl("CD_KIND_LOYALITY", false)) %>
				<%= Bean.getLoyalityKindOnlyCDOptions(cd_kind, true) %>
			<%=Bean.getSelectOnChangeEndHTML() %>

			<%=Bean.getSelectOnChangeBeginHTML("id_status", termParamLink + "&loy_lines_page=1", Bean.loylineXML.getfieldTransl("NAME_CARD_STATUS", false)) %>
				<%= Bean.getClubCardStatusOptions(id_status, true) %>
			<%=Bean.getSelectOnChangeEndHTML() %>

			<%=Bean.getSelectOnChangeBeginHTML("id_category", termParamLink + "&loy_lines_page=1", Bean.loylineXML.getfieldTransl("NAME_CATEGORY", false)) %>
				<%= Bean.getClubCardCategoryForStatusOptions(id_status, id_category, true) %>
			<%=Bean.getSelectOnChangeEndHTML() %>

			<!-- Вывод страниц -->
			<%= Bean.getPagesHTML(pageFormName + tagLoyalityLines, termParamLink + "&tab="+Bean.currentMenu.getTabID("CLIENTS_TERMINALS_LOY")+"&comission_rel_type="+comission_rel_type+"&", "loy_lines_page") %>			

		</tr>
	</table>	
	<%= terminal.getTermLoyalityLinesHTML(loy_lines_find, id_loy_history, id_status, id_category, cd_kind, l_loy_lines_page_beg, l_loy_lines_page_end) %>
	<% } %>	
	<% } %>
<%}

if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLIENTS_TERMINALS_CERTIFICATE")) {
	boolean hasEditPerm = Bean.currentMenu.isCurrentTabAndEditPermitted("CLIENTS_TERMINALS_CERTIFICATE");

	%>
		<% if (hasEditPerm) {%>
	  <form action="../crm/clients/terminalupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST">
		<input type="hidden" name="type" value="certificate">
	    <input type="hidden" name="action" value="edit">
	    <input type="hidden" name="process" value="yes">
	    <input type="hidden" name="id_term" value="<%= terminal.getValue("ID_TERM") %>">
	<%} %>

	<%
	if ("N".equalsIgnoreCase(terminal.getValue("WORK_WITH_CERTIFICATE"))) {%>
		<%= Bean.terminalXML.getfieldTransl("l_work_without_certificate", false) %>

	<%} else {%>
		
	<table <%=Bean.getTableDetailParam() %>>
		<%if ("".equalsIgnoreCase(terminal.getValue("ID_CURRENT_CERTIFICATE"))) {
%> 
		<tr>
			<td colspan="4"><b><i><%= Bean.terminalXML.getfieldTransl("l_current_certificate", false) %></i></b></td>
		</tr>
		<tr>
			<td colspan="4"><b><font color="red"><%= Bean.terminalXML.getfieldTransl("l_certificate_not_found", false) %></font></b></td>
		</tr>
	<% } else {
		bcTerminalCertificateObject cert = new bcTerminalCertificateObject(terminal.getValue("ID_CURRENT_CERTIFICATE"), terminal.getValue("ID_TERM"));
		cert.getFeature();
	%>
		<tr>
			<td colspan="4"><b><i><%= Bean.terminalXML.getfieldTransl("l_current_certificate", false) %></i></b></td>
		</tr>
		<tr>
			<td><%= Bean.terminalCertificateXML.getfieldTransl("id_term_certificate", false) %>
				<%= Bean.getGoToTerminalCertificateLink(terminal.getValue("ID_CURRENT_CERTIFICATE")) %>
			</td><td><input type="text" name="id_current_certificate" size="20" value="<%= terminal.getValue("ID_CURRENT_CERTIFICATE") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td><%= Bean.terminalCertificateXML.getfieldTransl("text_certificate", false) %></td><td colspan="5"><input type="text" name="text_certificate" size="150" value="<%=cert.getValue("TEXT_CERTIFICATE") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td><%= Bean.terminalCertificateXML.getfieldTransl("begin_action_date", false) %></td><td><input type="text" name="begin_action_date" size="20" value="<%=cert.getValue("BEGIN_ACTION_DATE_FRMT") %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.terminalCertificateXML.getfieldTransl("is_certificate_received", false) %></td><td><input type="text" name="is_certificate_received" size="20" value="<%=Bean.getMeaningFoCodeValue("YES_NO", terminal.getValue("IS_CERTIFICATE_RECEIVED")) %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td><%= Bean.terminalCertificateXML.getfieldTransl("end_action_date", false) %></td><td><input type="text" name="end_action_date" size="20" value="<%=cert.getValue("END_ACTION_DATE_FRMT") %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.terminalCertificateXML.getfieldTransl("date_certificate_received", false) %></td><td><input type="text" name="date_certificate_received" size="20" value="<%=terminal.getValue("DATE_CERTIFICATE_RECEIVED_FRMT") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
	<% } %>
		<tr>
			<td colspan="4" class="top_line"><b><i><%= Bean.terminalXML.getfieldTransl("l_next_certificate", false) %></i></b></td>
		</tr>
	<% if (hasEditPerm) {%>
		<tr>
			<td><%= Bean.terminalCertificateXML.getfieldTransl("id_term_certificate", false) %>
				<%= Bean.getGoToTerminalCertificateLink(terminal.getValue("ID_NEXT_CERTIFICATE")) %>
			</td><td><select name="id_next_certificate" class="inputfield"><option value=""></option><%= terminal.getTermCertificateOptions(terminal.getValue("ID_NEXT_CERTIFICATE"), "" /*terminal.getValue("ID_CURRENT_CERTIFICATE")*/, false) %> </select></td>
			<td><%= Bean.terminalXML.getfieldTransl("need_update_certificate", false) %></td><td><select name="need_update_certificate" class="inputfield"><%= Bean.getMeaningFromLookupNameOptions("YES_NO", terminal.getValue("NEED_UPDATE_CERTIFICATE"), false) %> </select></td>
		</tr>
		<tr>
			<td colspan="4" align="center">
				<%=Bean.getSubmitButtonAjax("../crm/clients/terminalupdate.jsp") %>
			</td>
		</tr>
	<% } else { %>
		<tr>
			<td><%= Bean.terminalCertificateXML.getfieldTransl("id_term_certificate", false) %>
				<%= Bean.getGoToTerminalCertificateLink(terminal.getValue("ID_NEXT_CERTIFICATE")) %>
			</td><td><input type="text" name="id_next_certificate" size="20" value="<%=terminal.getValue("ID_NEXT_CERTIFICATE") %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.terminalXML.getfieldTransl("need_update_certificate", false) %></td><td><input type="text" name="need_update_certificate" size="20" value="<%=Bean.getMeaningFoCodeValue("YES_NO", terminal.getValue("NEED_UPDATE_CERTIFICATE")) %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
	<% } %>
	</table>
<% }
	%>
	<br>
	<table <%=Bean.getTableBottomFilter() %>>
		<tr>
			<%=Bean.getFindHTML("cert_find", cert_find, termParamLink + "&cert_page=1") %>
		
		    <!-- Вывод страниц -->
			<%= Bean.getPagesHTML(pageFormName + tagCertificates, termParamLink + "&tab="+Bean.currentMenu.getTabID("CLIENTS_TERMINALS_CERTIFICATE")+"&", "cert_page") %>
		</tr>
	</table>
	<%= terminal.getLoadedCertificatesHTML(cert_find, l_cert_page_beg, l_cert_page_end) %>
	<%
}

if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLIENTS_TERMINALS_MESSAGES")) { %>
	<table <%=Bean.getTableBottomFilter() %>>
	  	<tr>
		<%= Bean.getFindHTML("message_find", message_find, termParamLink + "&messages_page=1&") %>

		<%=Bean.getSelectOnChangeBeginHTML("message_archive", termParamLink + "&messages_page=1", "") %>
			<%= Bean.getMeaningFromLookupNameOptions("YES_NO", message_archive, true) %>
		<%=Bean.getSelectOnChangeEndHTML() %>
	  	</tr>
	</table>

	<%= terminal.getTermMessagesHTML(message_find, message_archive, l_messages_beg, l_messages_end) %>
<%	}

if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLIENTS_TERMINALS_TERMSES")) {%>

	<table <%=Bean.getTableBottomFilter() %>>
	  <tr>
		<%= Bean.getFindHTML("term_ses_find", term_ses_find, termParamLink + "&term_ses_page=1&") %>

			<%=Bean.getSelectOnChangeBeginHTML("term_ses_data_type", termParamLink + "&term_ses_page=1&term_ses_data_state=", Bean.term_sesXML.getfieldTransl("h_data_type", false)) %>
				<%=Bean.getSelectOptionHTML(term_ses_data_type, "", "") %>
				<%=Bean.getSelectOptionHTML(term_ses_data_type, "TERM_CARD_REQ", Bean.term_sesXML.getfieldTransl("need_card_req_tsl", false)) %>
				<%=Bean.getSelectOptionHTML(term_ses_data_type, "TERM_CARD_CHECK", "- " + Bean.term_sesXML.getfieldTransl("need_card_check_tsl", false)) %>
				<%=Bean.getSelectOptionHTML(term_ses_data_type, "TERM_CLUB_PAY", "- " + Bean.term_sesXML.getfieldTransl("need_club_pay_tsl", false)) %>
				<%=Bean.getSelectOptionHTML(term_ses_data_type, "TERM_ONLINE_PAY", "- " + Bean.term_sesXML.getfieldTransl("need_online_pay_tsl", false)) %>
				<%=Bean.getSelectOptionHTML(term_ses_data_type, "TERM_ADV_PAY", "- " + Bean.term_sesXML.getfieldTransl("need_adv_pay_tsl", false)) %>
				<%=Bean.getSelectOptionHTML(term_ses_data_type, "TERM_ONLINE_STORNO", "- " + Bean.term_sesXML.getfieldTransl("need_online_storno_tsl", false)) %>
				<%=Bean.getSelectOptionHTML(term_ses_data_type, "TERM_COL_DATA", Bean.term_sesXML.getfieldTransl("need_col_data_tsl", false)) %>
				<%=Bean.getSelectOptionHTML(term_ses_data_type, "APP_TPAR_DATA", Bean.term_sesXML.getfieldTransl("need_tpar_data_tsl", false)) %>
				<%=Bean.getSelectOptionHTML(term_ses_data_type, "TERM_MON_REP", Bean.term_sesXML.getfieldTransl("need_term_mon_tsl", false)) %>
			<%=Bean.getSelectOnChangeEndHTML() %>
	
			<% if ("TERM_CARD_REQ".equalsIgnoreCase(term_ses_data_type)) {%>
				<%=Bean.getSelectOnChangeBeginHTML("term_ses_data_state", termParamLink + "&term_ses_page=1&term_ses_data_type="+term_ses_data_type, Bean.term_sesXML.getfieldTransl("h_data_state", false)) %>
					<%=Bean.getTermSesCardReqStateOptions(term_ses_data_state, true) %>
				<%=Bean.getSelectOnChangeEndHTML() %>
			<% } %>
			<% if ("TERM_CARD_CHECK".equalsIgnoreCase(term_ses_data_type)) {%>
				<%=Bean.getSelectOnChangeBeginHTML("term_ses_data_state", termParamLink + "&term_ses=1&term_ses_data_type="+term_ses_data_type, Bean.term_sesXML.getfieldTransl("h_data_state", false)) %>
					<%=Bean.getTermSesCardReqStateOptions(term_ses_data_state, true) %>
				<%=Bean.getSelectOnChangeEndHTML() %>
			<% } %>
			<% if ("TERM_CLUB_PAY".equalsIgnoreCase(term_ses_data_type)) {%>
				<%=Bean.getSelectOnChangeBeginHTML("term_ses_data_state", termParamLink + "&term_ses=1&term_ses_data_type="+term_ses_data_type, Bean.term_sesXML.getfieldTransl("h_data_state", false)) %>
					<%=Bean.getTermSesCardReqStateOptions(term_ses_data_state, true) %>
				<%=Bean.getSelectOnChangeEndHTML() %>
			<% } %>
			<% if ("TERM_ONLINE_PAY".equalsIgnoreCase(term_ses_data_type)) {%>
				<%=Bean.getSelectOnChangeBeginHTML("term_ses_data_state", termParamLink + "&term_ses=1&term_ses_data_type="+term_ses_data_type, Bean.term_sesXML.getfieldTransl("h_data_state", false)) %>
					<%=Bean.getTermSesCardReqStateOptions(term_ses_data_state, true) %>
				<%=Bean.getSelectOnChangeEndHTML() %>
			<% } %>
			<% if ("TERM_ADV_PAY".equalsIgnoreCase(term_ses_data_type)) {%>
				<%=Bean.getSelectOnChangeBeginHTML("term_ses_data_state", termParamLink + "&term_ses=1&term_ses_data_type="+term_ses_data_type, Bean.term_sesXML.getfieldTransl("h_data_state", false)) %>
					<%=Bean.getTermSesCardReqStateOptions(term_ses_data_state, true) %>
				<%=Bean.getSelectOnChangeEndHTML() %>
			<% } %>
			<% if ("TERM_ONLINE_STORNO".equalsIgnoreCase(term_ses_data_type)) {%>
				<%=Bean.getSelectOnChangeBeginHTML("term_ses_data_state", termParamLink + "&term_ses=1&term_ses_data_type="+term_ses_data_type, Bean.term_sesXML.getfieldTransl("h_data_state", false)) %>
					<%=Bean.getTermSesCardReqStateOptions(term_ses_data_state, true) %>
				<%=Bean.getSelectOnChangeEndHTML() %>
			<% } %>
			<% if ("TERM_COL_DATA".equalsIgnoreCase(term_ses_data_type)) {%>
				<%=Bean.getSelectOnChangeBeginHTML("term_ses_data_state", termParamLink + "&term_ses=1&term_ses_data_type="+term_ses_data_type, Bean.term_sesXML.getfieldTransl("h_data_state", false)) %>
					<%=Bean.getTermSesDataStateOptions(term_ses_data_state, true) %>
				<%=Bean.getSelectOnChangeEndHTML() %>
			<% } %>
			<% if ("APP_TPAR_DATA".equalsIgnoreCase(term_ses_data_type)) {%>
				<%=Bean.getSelectOnChangeBeginHTML("term_ses_data_state", termParamLink + "&term_ses=1&term_ses_data_type="+term_ses_data_type, Bean.term_sesXML.getfieldTransl("h_data_state", false)) %>
					<%=Bean.getTermSesParamStateOptions(term_ses_data_state, true) %>
				<%=Bean.getSelectOnChangeEndHTML() %>
			<% } %>
			<% if ("TERM_MON_REP".equalsIgnoreCase(term_ses_data_type)) {%>
				<%=Bean.getSelectOnChangeBeginHTML("term_ses_data_state", termParamLink + "&term_ses=1&term_ses_data_type="+term_ses_data_type, Bean.term_sesXML.getfieldTransl("h_data_state", false)) %>
					<%=Bean.getTermSesMonStateOptions(term_ses_data_state, true) %>
				<%=Bean.getSelectOnChangeEndHTML() %>
			<% } %>
	  </tr>
	</table>
	<%= terminal.getTermSessionsHTML(term_ses_find, term_ses_data_type, term_ses_data_state, l_term_ses_page_beg, l_term_ses_page_end) %>
<%	}

if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLIENTS_TERMINALS_TRANS")) {%>
	<table <%=Bean.getTableBottomFilter() %>>
	  	<tr>
		<%= Bean.getFindHTML("transaction_find", transaction_find, termParamLink + "&trans_page=1&") %>

		<%=Bean.getSelectOnChangeBeginHTML("transaction_type", termParamLink + "&trans_page=1", Bean.transactionXML.getfieldTransl("type_trans", false)) %>
			<%= Bean.getTransTypeOptions(transaction_type, true) %>
		<%=Bean.getSelectOnChangeEndHTML() %>
	
		<%=Bean.getSelectOnChangeBeginHTML("transaction_pay_type", termParamLink + "&trans_page=1", Bean.transactionXML.getfieldTransl("pay_type", false)) %>
			<%= Bean.getTransPayTypeOptions(transaction_pay_type, true) %>
		<%=Bean.getSelectOnChangeEndHTML() %>
	
		<%=Bean.getSelectOnChangeBeginHTML("transaction_state", termParamLink + "&trans_page=1", Bean.transactionXML.getfieldTransl("state_trans", false)) %>
			<%= Bean.getTransStateOptions(transaction_state, true) %>
		<%=Bean.getSelectOnChangeEndHTML() %>
	  	</tr>
	</table>
	<%= terminal.getTransactionsHTML(transaction_find, transaction_type, transaction_pay_type, transaction_state, l_trans_page_beg, l_trans_page_end) %>
<%	}

if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLIENTS_TERMINALS_COMISSIONS")) {%>
	<table <%=Bean.getTableBottomFilter() %>>
	  	<tr>
		<%= Bean.getFindHTML("comission_find", comission_find, termParamLink + "&comission_page=1&") %>

		<%=Bean.getSelectOnChangeBeginHTML("comission_rel_type", termParamLink + "&comission_page=1", Bean.transactionXML.getfieldTransl("type_trans", false)) %>
			<%= Bean.getTerminalClubRelationshipsOptions(comission_rel_type, true) %>
		<%=Bean.getSelectOnChangeEndHTML() %>
	  	</tr>
	</table>
	<%= terminal.getComissionListHTML(comission_find, comission_rel_type, l_comission_page_beg, l_comission_page_end) %>
<%	}


if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLIENTS_TERMINALS_RELATIONSHIPS")) {%>
	<table <%=Bean.getTableBottomFilter() %>>
	  	<tr>
		<%= Bean.getFindHTML("relation_find", relation_find, termParamLink + "&relation_page=1&") %>

		<td align="right">
	<% String needComission = terminal.getRelationShipsNeededCount();
	  
	  if (!(needComission.equalsIgnoreCase("0"))) {
		  %>
			<b><font color=red><%= Bean.relationshipXML.getfieldTransl("need_relationships_count", false) %> -  <%=needComission %></font></b><br>
		  <%
	  } else {
		  %>
			<b><font color=green><%= Bean.relationshipXML.getfieldTransl("all_relationships_was_created", false) %></font></b><br>
		  <%
	  }
	%>
		</td>
		<%=Bean.getSelectOnChangeBeginHTML("relation_kind", termParamLink + "&relation_page=1", Bean.transactionXML.getfieldTransl("type_trans", false)) %>
			<%=Bean.getSelectOptionHTML(relation_kind, "CREATED", Bean.relationshipXML.getfieldTransl("h_created", false), "") %>
			<%=Bean.getSelectOptionHTML(relation_kind, "NEEDED", Bean.relationshipXML.getfieldTransl("h_needed", false), "") %>
		<%=Bean.getSelectOnChangeEndHTML() %>
	  	</tr>
	</table>
	<% if ("CREATED".equalsIgnoreCase(relation_kind)) { %>
		<%= terminal.getRelationShipsHTML(relation_find, l_relation_page_beg, l_relation_page_end) %>
	<% } else { %>
		<%= terminal.getRelationShipsNeededHTML(relation_find, l_relation_page_beg, l_relation_page_end) %>
	<% } %>
<%	}

if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLIENTS_TERMINALS_LOGISTIC")) {%>
	<table <%=Bean.getTableBottomFilter() %>>
	  	<tr>
		<%= Bean.getFindHTML("logistic_find", logistic_find, termParamLink + "&logistic_page=1&") %>
 
		<td>&nbsp;</td>
	  	</tr>
	</table>
	<%= terminal.getLogisticHTML(logistic_find, l_logistic_page_beg, l_logistic_page_end) %>
<%	}

if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLIENTS_TERMINALS_MONITORING")) {%>
	<table <%=Bean.getTableBottomFilter() %>>
	  	<tr>
		<%= Bean.getFindHTML("monitoring_find", monitoring_find, termParamLink + "&monitoring_page=1&") %>

		<td>&nbsp;</td>
	  	</tr>
	</table>
	<%= terminal.getMonitoringHTML(monitoring_find, l_monitoring_page_beg, l_monitoring_page_end) %>
<%	}

if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLIENTS_TERMINALS_USERS")) { %>
<table <%=Bean.getTableBottomFilter() %>>
  	<tr>
	<%= Bean.getFindHTML("term_user_find", term_user_find, termParamLink + "&term_user_page=1&") %>

	<%=Bean.getSelectOnChangeBeginHTML("term_user_access_type", termParamLink + "&term_user_page=1&", Bean.terminalXML.getfieldTransl("cd_term_user_access_type", false)) %>
		<%= Bean.getTermUserAccessTypeOptions(term_user_access_type, true) %>
	<%=Bean.getSelectOnChangeEndHTML() %>

	<%=Bean.getSelectOnChangeBeginHTML("term_user_status", termParamLink + "&term_user_page=1&", Bean.terminalXML.getfieldTransl("cd_term_user_status", false)) %>
		<%= Bean.getTermUserStatusOptions(term_user_status, true) %>
	<%=Bean.getSelectOnChangeEndHTML() %>

	</tr>
</table>

<%= terminal.getTerminalUsersHTML(term_user_find, term_user_status, term_user_access_type, l_term_user_beg, l_term_user_end) %>

<% }

if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLIENTS_TERMINALS_OP_TYPES")) { %>
<table <%=Bean.getTableBottomFilter() %>>
  	<tr>
	<%= Bean.getFindHTML("op_type_find", op_type_find, termParamLink + "&op_type_page=1&") %>

	</tr>
</table>

<%= terminal.getOnlinePaymentTypesHTML(op_type_find, l_op_type_beg, l_op_type_end) %>

<% }

if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLIENTS_TERMINALS_CHANGE_HISTORY")) {%>
	<table <%=Bean.getTableBottomFilter() %>>
	  	<tr>
		<%= Bean.getFindHTML("history_find", history_find, termParamLink + "&history_page=1&") %>

		<td>&nbsp;</td>
	  	</tr>
	</table>
	<%= terminal.getTerminalHistoryHTML(history_find, l_history_page_beg, l_history_page_end) %>
<%	}

} %>
</div></div>
</body>
</html>
