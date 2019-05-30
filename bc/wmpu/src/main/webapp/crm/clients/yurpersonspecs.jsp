<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%@page import="bc.objects.bcJurPrsObject"%>
<%@page import="bc.objects.bcIssuerObject"%>
<%@page import="bc.objects.bcContactsObject"%>
<%@page import="bc.objects.bcDocumentObject"%>
<%@page import="java.util.HashMap"%><html>

<%= Bean.getLogOutScript(request) %> 

<head>
	<%= Bean.getMetaContent() %>
	<%= Bean.getBottomFrameCSS() %>
	
<title></title>
</head>
<body>
<div id="div_tabsheet">

<% 

HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String pageFormName = "CLIENTS_YURPERSONS";

Bean.setJspPageForTabName(pageFormName);

String tagIssuerParam = "_ISSUER_PARAM";
String tagIssuerParamFind = "_ISSUER_PARAM_FIND";
String tagBKAccounts = "_BK_ACCOUNTS";
String tagBKAccountFind = "_BK_ACCOUNT_FIND";
String tagBKAccountExist = "_BK_ACCOUNT_EXIST";
String tagSubmission = "_SUBMISSION";
String tagSubmissionFind = "_SUBMISSION_FIND";
String tagSubmissionForm = "_SUBMISSION_FORM";
String tagBankAccounts = "_BAND_ACCOUNTS";
String tagBankAccountFind = "_BANK_ACCOUNT_FIND";
String tagBankAccountType = "_BANK_ACCOUNT_TYPE";
String tagTerminals = "_TERMINALS";
String tagTerminalFind = "_TERMINAL_FIND";
String tagTerminalType = "_TERMINAL_TYPE";
String tagTerminalStatus = "_TERMINAL_STATUS";
String tagTerminalRole = "_TERMINAL_ROLE";
String tagComission = "_COMISSION";
String tagComissionFind = "_COMISSION_FIND";
String tagComissionType = "_COMISSION_TYPE";
String tagContacts = "_CONTACTS";
String tagContactFind = "_CONTACT_FIND";
String tagContactPost = "_CONTACT_POST";
String tagDoc = "_DOC";
String tagDocFind = "_DOC_FIND";
String tagDocType = "_DOC_TYPE";
String tagDocState = "_DOC_STATE";
String tagDocClubRelType = "_DOC_CLUB_REL_TYPE";
String tagRelationShips = "_RELATIONSHIPS";
String tagRelationFind = "_RELATION_FIND";
String tagRelationKind = "_RELATION_KIND";
String tagDevices = "_DEVICES";
String tagDeviceFind = "_DIVICE_FIND";
String tagDeviceType = "_DIVICE_TYPE";
String tagAccess = "_ACCESS";
String tagAccessFind = "_ACCESS_FIND";
String tagAccessSelected = "_ACCESS_SELECTED";

String tagNomenkl = "_NOMENKL";
String tagNomenklFind = "_NOMENTK_FIND";
String tagLogistic = "_LOGISTIC";
String tagLogisticFind = "_LOGISTIC_FIND";
String tagCardPackage = "_CARD_PACKAGE";
String tagCardPackageFind = "_CARD_PACKAGE_FIND";
String tagCardPackageCardStatus = "_CARD_PACKAGE_CARD_STATUS";
String tagReferralScheme = "_REFERRAL_SCHEME";
String tagReferralSchemeFind = "_REFERRAL_SCHEME_FIND";
String tagReferralSchemeType = "_REFERRAL_SCHEME_TYPE";
String tagReferralSchemeCalcType = "_REFERRAL_SCHEME_CALC_TYPE";

String tagLoyality = "_LOYALITY";
String tagLoyalityFind = "_LOYALITY_FIND";
String tagLoyalityKind = "_LOYALITY_KIND";
String tagTargetProgram = "_TARGET_PROGRAM";
String tagTargetProgramFind = "_TARGET_PROGRAM_FIND";
String tagTargetProgramPayPeriod = "_TARGET_PROGRAM_PAY_PERIOD";

String tagRole = "_ACCESS_ROLE";
String tagRoleFind = "_ACCESS_ROLE_FIND";
String tagRoleModuleType = "_ACCESS_ROLE_MODULE_TYPE";

String tagTrans = "_TRANS";
String tagTransFind = "_TRANS_FIND";
String tagTransType = "_TRANS_TYPE";
String tagTransState = "_TRANS_STATE";
String tagTransPayType = "_TRANS_PAY_TYPE";

String yurpersonid = Bean.getDecodeParam(parameters.get("id"));
String	tab = Bean.getDecodeParam(parameters.get("tab"));
if (tab==null || "".equalsIgnoreCase(tab)) { tab = Bean.tabsHmGetValue(pageFormName); }
if (yurpersonid==null || "".equalsIgnoreCase(yurpersonid)) { yurpersonid=""; }
if ("".equalsIgnoreCase(yurpersonid)) {
%>
	<%=Bean.getIDNotFoundMessage() %>
<% 
} 
else {
	Bean.tabsHmSetValue(pageFormName, tab);
	bcJurPrsObject jurprs = new bcJurPrsObject(yurpersonid);
	
	boolean isPartner = "PARTNER".equalsIgnoreCase(jurprs.getValue("CD_JUR_PRS_STATUS"));
	
	if ("SERVICE_PLACE".equalsIgnoreCase(jurprs.getValue("CD_JUR_PRS_STATUS"))) {
		Bean.currentMenu.setExistFlag("CLIENTS_YURPERSONS_DOCUMENTS", false);
		Bean.currentMenu.setExistFlag("CLIENTS_YURPERSONS_BANKACCNT", false);
		Bean.currentMenu.setExistFlag("CLIENTS_YURPERSONS_SERVICEPLACE", false);
		Bean.currentMenu.setExistFlag("CLIENTS_YURPERSONS_CARD_PACKAGE", false);
		Bean.currentMenu.setExistFlag("CLIENTS_YURPERSONS_REFERRAL_SCHEME", false);
		Bean.currentMenu.setExistFlag("CLIENTS_YURPERSONS_LOYALITY_SCHEME", false);
		Bean.currentMenu.setExistFlag("CLIENTS_YURPERSONS_TARGET_PROGRAMS", false);
		Bean.currentMenu.setExistFlag("CLIENTS_YURPERSONS_ACCESS_ROLES", false);
		Bean.currentMenu.setExistFlag("CLIENTS_YURPERSONS_COMISSION", false);
		Bean.currentMenu.setExistFlag("CLIENTS_YURPERSONS_ISSUER", false);
		Bean.currentMenu.setExistFlag("CLIENTS_YURPERSONS_TERM_DEVICE_TYPES", false);
		Bean.currentMenu.setExistFlag("CLIENTS_YURPERSONS_BK_ACCOUNTS", false);
		Bean.currentMenu.setExistFlag("CLIENTS_YURPERSONS_RELATIONSHIPS", false);
		Bean.currentMenu.setExistFlag("CLIENTS_YURPERSONS_NOMENKLATURE", false);
		Bean.currentMenu.setExistFlag("CLIENTS_YURPERSONS_LOGISTIC", false);
		if (Bean.currentMenu.isCurrentTab("CLIENTS_YURPERSONS_DOCUMENTS") ||
				Bean.currentMenu.isCurrentTab("CLIENTS_YURPERSONS_BANKACCNT") ||
				Bean.currentMenu.isCurrentTab("CLIENTS_YURPERSONS_SERVICEPLACE") ||
				Bean.currentMenu.isCurrentTab("CLIENTS_YURPERSONS_CARD_PACKAGE") ||
				Bean.currentMenu.isCurrentTab("CLIENTS_YURPERSONS_REFERRAL_SCHEME") ||
				Bean.currentMenu.isCurrentTab("CLIENTS_YURPERSONS_LOYALITY_SCHEME") ||
				Bean.currentMenu.isCurrentTab("CLIENTS_YURPERSONS_TARGET_PROGRAMS") ||
				Bean.currentMenu.isCurrentTab("CLIENTS_YURPERSONS_ACCESS_ROLES") ||
				Bean.currentMenu.isCurrentTab("CLIENTS_YURPERSONS_COMISSION") ||
				Bean.currentMenu.isCurrentTab("CLIENTS_YURPERSONS_ISSUER") ||
				Bean.currentMenu.isCurrentTab("CLIENTS_YURPERSONS_TERM_DEVICE_TYPES") ||
				Bean.currentMenu.isCurrentTab("CLIENTS_YURPERSONS_BK_ACCOUNTS") ||
				Bean.currentMenu.isCurrentTab("CLIENTS_YURPERSONS_RELATIONSHIPS") ||
				Bean.currentMenu.isCurrentTab("CLIENTS_YURPERSONS_NOMENKLATURE") ||
				Bean.currentMenu.isCurrentTab("CLIENTS_YURPERSONS_LOGISTIC")) {
			Bean.currentMenu.setFirstCurrentTab();
			tab = Bean.currentMenu.getCurrentTab();
			Bean.tabsHmSetValue(pageFormName, tab);
		}
	}
	
	
	//Обрабатываем номера страниц
	String l_issuer_page = Bean.getDecodeParam(parameters.get("issuer_page"));
	Bean.pageCheck(pageFormName + tagIssuerParam, l_issuer_page);
	String l_issuer_page_beg = Bean.getFirstRowNumber(pageFormName + tagIssuerParam);
	String l_issuer_page_end = Bean.getLastRowNumber(pageFormName + tagIssuerParam);

	String issuer_find 	= Bean.getDecodeParam(parameters.get("issuer_find"));
	issuer_find 	= Bean.checkFindString(pageFormName + tagIssuerParamFind, issuer_find, l_issuer_page);

	String l_bk_acc_page = Bean.getDecodeParam(parameters.get("bk_acc_page"));
	Bean.pageCheck(pageFormName + tagBKAccounts, l_bk_acc_page);
	String l_bk_acc_page_beg = Bean.getFirstRowNumber(pageFormName + tagBKAccounts);
	String l_bk_acc_page_end = Bean.getLastRowNumber(pageFormName + tagBKAccounts);
	
	String bk_acc_find 	= Bean.getDecodeParam(parameters.get("bk_acc_find"));
	bk_acc_find 	= Bean.checkFindString(pageFormName + tagBKAccountFind, bk_acc_find, l_bk_acc_page);
	
	String bk_acc_exist 	= Bean.getDecodeParam(parameters.get("bk_acc_exist"));
	bk_acc_exist 	= Bean.checkFindString(pageFormName + tagBKAccountExist, bk_acc_exist, l_bk_acc_page);
	
	String l_submission_page = Bean.getDecodeParam(parameters.get("submission_page"));
	Bean.pageCheck(pageFormName + tagSubmission, l_submission_page);
	String l_submission_page_beg = Bean.getFirstRowNumber(pageFormName + tagSubmission);
	String l_submission_page_end = Bean.getLastRowNumber(pageFormName + tagSubmission);
	
	String submission_find 	= Bean.getDecodeParam(parameters.get("submission_find"));
	submission_find 	= Bean.checkFindString(pageFormName + tagSubmissionFind, submission_find, l_submission_page);
	
	String submission_form 	= Bean.getDecodeParam(parameters.get("submission_form"));
	submission_form 	= Bean.checkFindString(pageFormName + tagSubmissionForm, submission_form, l_submission_page);
	
	String l_bank_account_page = Bean.getDecodeParam(parameters.get("bank_account_page"));
	Bean.pageCheck(pageFormName + tagBankAccounts, l_bank_account_page);
	String l_bank_account_page_beg = Bean.getFirstRowNumber(pageFormName + tagBankAccounts);
	String l_bank_account_page_end = Bean.getLastRowNumber(pageFormName + tagBankAccounts);

	String bank_acc_find 	= Bean.getDecodeParam(parameters.get("bank_acc_find"));
	bank_acc_find 	= Bean.checkFindString(pageFormName + tagBankAccountFind, bank_acc_find, l_bank_account_page);

	String bank_acc_type 	= Bean.getDecodeParam(parameters.get("bank_acc_type"));
	bank_acc_type 	= Bean.checkFindString(pageFormName + tagBankAccountType, bank_acc_type, l_bank_account_page);
	
	String l_terminals_page = Bean.getDecodeParam(parameters.get("terminals_page"));
	Bean.pageCheck(pageFormName + tagTerminals, l_terminals_page);
	String l_terminals_page_beg = Bean.getFirstRowNumber(pageFormName + tagTerminals);
	String l_terminals_page_end = Bean.getLastRowNumber(pageFormName + tagTerminals);

	String terminal_find 	= Bean.getDecodeParam(parameters.get("terminal_find"));
	terminal_find 	= Bean.checkFindString(pageFormName + tagTerminalFind, terminal_find, l_terminals_page);
	
	String terminal_status	= Bean.getDecodeParam(parameters.get("terminal_status"));
	terminal_status		= Bean.checkFindString(pageFormName + tagTerminalStatus, terminal_status, l_terminals_page);
	
	String terminal_type	= Bean.getDecodeParam(parameters.get("terminal_type"));
	terminal_type		= Bean.checkFindString(pageFormName + tagTerminalType, terminal_type, l_terminals_page);

	String term_role = Bean.getDecodeParam(parameters.get("term_role"));
	term_role 		= Bean.checkFindString(pageFormName + tagTerminalRole, term_role, l_terminals_page);
	if (term_role==null || "".equalsIgnoreCase(term_role)) {
		term_role = "JUR_PRS_PLACE";
		term_role = Bean.checkFindString(pageFormName + tagTerminalRole, term_role, l_terminals_page);
	}
	
	String l_contact_page = Bean.getDecodeParam(parameters.get("contact_page"));
	Bean.pageCheck(pageFormName + tagContacts, l_contact_page);
	String l_contact_page_beg = Bean.getFirstRowNumber(pageFormName + tagContacts);
	String l_contact_page_end = Bean.getLastRowNumber(pageFormName + tagContacts);
	
	String contact_find 	= Bean.getDecodeParam(parameters.get("contact_find"));
	contact_find 	= Bean.checkFindString(pageFormName + tagContactFind, contact_find, l_contact_page);
	
	String contact_post 	= Bean.getDecodeParam(parameters.get("contact_post"));
	contact_post 	= Bean.checkFindString(pageFormName + tagContactPost, contact_post, l_contact_page);
    
	String l_doc_page = Bean.getDecodeParam(parameters.get("doc_page"));
	Bean.pageCheck(pageFormName + tagDoc, l_doc_page);
	String l_doc_page_beg = Bean.getFirstRowNumber(pageFormName + tagDoc);
	String l_doc_page_end = Bean.getLastRowNumber(pageFormName + tagDoc);

	String doc_find 	= Bean.getDecodeParam(parameters.get("doc_find"));
	doc_find 	= Bean.checkFindString(pageFormName + tagDocFind, doc_find, l_doc_page);

	String doc_type	= Bean.getDecodeParam(parameters.get("doc_type"));
	doc_type		= Bean.checkFindString(pageFormName + tagDocType, doc_type, l_doc_page);

	String doc_state	= Bean.getDecodeParam(parameters.get("doc_state"));
	doc_state		= Bean.checkFindString(pageFormName + tagDocState, doc_state, l_doc_page);

	String club_rel_type	= Bean.getDecodeParam(parameters.get("club_rel_type"));
	club_rel_type		= Bean.checkFindString(pageFormName + tagDocClubRelType, club_rel_type, l_doc_page);
    
	String l_comis_page = Bean.getDecodeParam(parameters.get("comis_page"));
	Bean.pageCheck(pageFormName + tagComission, l_comis_page);
	String l_comis_page_beg = Bean.getFirstRowNumber(pageFormName + tagComission);
	String l_comis_page_end = Bean.getLastRowNumber(pageFormName + tagComission);

	String comis_find 	= Bean.getDecodeParam(parameters.get("comis_find"));
	comis_find 	= Bean.checkFindString(pageFormName + tagComissionFind, comis_find, l_comis_page);

	String comis_type = Bean.getDecodeParam(parameters.get("comis_type"));
	comis_type 		= Bean.checkFindString(pageFormName + tagComissionType, comis_type, l_comis_page);

	String l_relation_page = Bean.getDecodeParam(parameters.get("relation_page"));
	Bean.pageCheck(pageFormName + tagRelationShips, l_relation_page);
	String l_relation_page_beg = Bean.getFirstRowNumber(pageFormName + tagRelationShips);
	String l_relation_page_end = Bean.getLastRowNumber(pageFormName + tagRelationShips);

	String relation_find 	= Bean.getDecodeParam(parameters.get("relation_find"));
	relation_find 	= Bean.checkFindString(pageFormName + tagRelationFind, relation_find, l_relation_page);
    
	String rel_kind = Bean.getDecodeParam(parameters.get("rel_kind"));
	if (rel_kind==null || "".equalsIgnoreCase(rel_kind)) {
		rel_kind = "CREATED";
	}
	rel_kind 		= Bean.checkFindString(pageFormName + tagRelationKind, rel_kind, l_relation_page);
	
	String l_devices_page = Bean.getDecodeParam(parameters.get("device_page"));
	Bean.pageCheck(pageFormName + tagDevices, l_devices_page);
	String l_devices_page_beg = Bean.getFirstRowNumber(pageFormName + tagDevices);
	String l_devices_page_end = Bean.getLastRowNumber(pageFormName + tagDevices);

	String device_find 	= Bean.getDecodeParam(parameters.get("device_find"));
	device_find 	= Bean.checkFindString(pageFormName + tagDeviceFind, device_find, l_devices_page);

	String device_type 	= Bean.getDecodeParam(parameters.get("device_type"));
	device_type 	= Bean.checkFindString(pageFormName + tagDeviceType, device_type, l_devices_page);
	
	String l_access_page = Bean.getDecodeParam(parameters.get("access_page"));
	Bean.pageCheck(pageFormName + tagAccess, l_access_page);
	String l_access_page_beg = Bean.getFirstRowNumber(pageFormName + tagAccess);
	String l_access_page_end = Bean.getLastRowNumber(pageFormName + tagAccess);

	String access_find 	= Bean.getDecodeParam(parameters.get("access_find"));
	access_find 	= Bean.checkFindString(pageFormName + tagAccessFind, access_find, l_access_page);
	
	String access_selected 	= Bean.getDecodeParam(parameters.get("access_selected"));
	access_selected 	= Bean.checkFindString(pageFormName + tagAccessSelected, access_selected, l_access_page);
	
	String l_nomenkl_page = Bean.getDecodeParam(parameters.get("nomenkl_page"));
	Bean.pageCheck(pageFormName + tagNomenkl, l_nomenkl_page);
	String l_nomenkl_page_beg = Bean.getFirstRowNumber(pageFormName + tagNomenkl);
	String l_nomenkl_page_end = Bean.getLastRowNumber(pageFormName + tagNomenkl);

	String nomenkl_find 	= Bean.getDecodeParam(parameters.get("nomenkl_find"));
	nomenkl_find 	= Bean.checkFindString(pageFormName + tagNomenklFind, nomenkl_find, l_nomenkl_page);
	
	String l_logistic_page = Bean.getDecodeParam(parameters.get("logistic_page"));
	Bean.pageCheck(pageFormName + tagLogistic, l_logistic_page);
	String l_logistic_page_beg = Bean.getFirstRowNumber(pageFormName + tagLogistic);
	String l_logistic_page_end = Bean.getLastRowNumber(pageFormName + tagLogistic);

	String logistic_find 	= Bean.getDecodeParam(parameters.get("logistic_find"));
	logistic_find 	= Bean.checkFindString(pageFormName + tagLogisticFind, logistic_find, l_logistic_page);
	
	String l_card_pack_page = Bean.getDecodeParam(parameters.get("card_pack_page"));
	Bean.pageCheck(pageFormName + tagCardPackage, l_card_pack_page);
	String l_card_pack_page_beg = Bean.getFirstRowNumber(pageFormName + tagCardPackage);
	String l_card_pack_page_end = Bean.getLastRowNumber(pageFormName + tagCardPackage);

	String card_pack_find 	= Bean.getDecodeParam(parameters.get("card_pack_find"));
	card_pack_find 	= Bean.checkFindString(pageFormName + tagCardPackageFind, card_pack_find, l_card_pack_page);

	String card_pack_card_status 	= Bean.getDecodeParam(parameters.get("card_pack_card_status"));
	card_pack_card_status 	= Bean.checkFindString(pageFormName + tagCardPackageCardStatus, card_pack_card_status, l_card_pack_page);
	
	String l_referral_scheme_page = Bean.getDecodeParam(parameters.get("referral_scheme_page"));
	Bean.pageCheck(pageFormName + tagReferralScheme, l_referral_scheme_page);
	String l_referral_scheme_page_beg = Bean.getFirstRowNumber(pageFormName + tagReferralScheme);
	String l_referral_scheme_page_end = Bean.getLastRowNumber(pageFormName + tagReferralScheme);

	String referral_scheme_find 	= Bean.getDecodeParam(parameters.get("referral_scheme_find"));
	referral_scheme_find 	= Bean.checkFindString(pageFormName + tagReferralSchemeFind, referral_scheme_find, l_referral_scheme_page);

	String referral_scheme_type 	= Bean.getDecodeParam(parameters.get("referral_scheme_type"));
	referral_scheme_type 	= Bean.checkFindString(pageFormName + tagReferralSchemeType, referral_scheme_type, l_referral_scheme_page);

	String referral_scheme_calc_type 	= Bean.getDecodeParam(parameters.get("referral_scheme_calc_type"));
	referral_scheme_calc_type 	= Bean.checkFindString(pageFormName + tagReferralSchemeCalcType, referral_scheme_calc_type, l_referral_scheme_page);
	
	String l_loyality_page = Bean.getDecodeParam(parameters.get("loyality_page"));
	Bean.pageCheck(pageFormName + tagLoyality, l_loyality_page);
	String l_loyality_page_beg = Bean.getFirstRowNumber(pageFormName + tagLoyality);
	String l_loyality_page_end = Bean.getLastRowNumber(pageFormName + tagLoyality);

	String loyality_find 	= Bean.getDecodeParam(parameters.get("loyality_find"));
	loyality_find 	= Bean.checkFindString(pageFormName + tagLoyalityFind, loyality_find, l_loyality_page);

	String loyality_kind 	= Bean.getDecodeParam(parameters.get("loyality_kind"));
	loyality_kind 	= Bean.checkFindString(pageFormName + tagLoyalityKind, loyality_kind, l_loyality_page);
	
	String l_target_program_page = Bean.getDecodeParam(parameters.get("target_program_page"));
	Bean.pageCheck(pageFormName + tagTargetProgram, l_target_program_page);
	String l_target_program_page_beg = Bean.getFirstRowNumber(pageFormName + tagTargetProgram);
	String l_target_program_page_end = Bean.getLastRowNumber(pageFormName + tagTargetProgram);

	String target_program_find 	= Bean.getDecodeParam(parameters.get("target_program_find"));
	target_program_find 	= Bean.checkFindString(pageFormName + tagTargetProgramFind, target_program_find, l_target_program_page);

	String target_program_pay_period 	= Bean.getDecodeParam(parameters.get("target_program_pay_period"));
	target_program_pay_period 	= Bean.checkFindString(pageFormName + tagTargetProgramPayPeriod, target_program_pay_period, l_target_program_page);
	
	String l_role_page = Bean.getDecodeParam(parameters.get("role_page"));
	Bean.pageCheck(pageFormName + tagRole, l_role_page);
	String l_role_page_beg = Bean.getFirstRowNumber(pageFormName + tagRole);
	String l_role_page_end = Bean.getLastRowNumber(pageFormName + tagRole);

	String role_find 	= Bean.getDecodeParam(parameters.get("role_find"));
	role_find 	= Bean.checkFindString(pageFormName + tagRoleFind, role_find, l_role_page);

	String role_module_type 	= Bean.getDecodeParam(parameters.get("role_module_type"));
	role_module_type 	= Bean.checkFindString(pageFormName + tagRoleModuleType, role_module_type, l_role_page);
	
	String l_trans_page = Bean.getDecodeParam(parameters.get("trans_page"));
	Bean.pageCheck(pageFormName + tagTrans, l_trans_page);
	String l_trans_page_beg = Bean.getFirstRowNumber(pageFormName + tagTrans);
	String l_trans_page_end = Bean.getLastRowNumber(pageFormName + tagTrans);

	String trans_find 	= Bean.getDecodeParam(parameters.get("trans_find"));
	trans_find 	= Bean.checkFindString(pageFormName + tagTransFind, trans_find, l_trans_page);

	String trans_type 	= Bean.getDecodeParam(parameters.get("trans_type"));
	trans_type 	= Bean.checkFindString(pageFormName + tagTransType, trans_type, l_trans_page);

	String trans_state 	= Bean.getDecodeParam(parameters.get("trans_state"));
	trans_state 	= Bean.checkFindString(pageFormName + tagTransState, trans_state, l_trans_page);

	String trans_pay_type 	= Bean.getDecodeParam(parameters.get("trans_pay_type"));
	trans_pay_type 	= Bean.checkFindString(pageFormName + tagTransPayType, trans_pay_type, l_trans_page);


%>
  <% Bean.currentMenu.setCurrentTab(Bean.currentMenu.getTabSheetName(tab)); %>

	<table <%=Bean.getTableMenuParam() %>>
		<tr>
			<%= Bean.getPageHeader() %>
			
			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLIENTS_YURPERSONS_INFO")) { %>
				<% if (Bean.currentMenu.isCurrentTabAndEditPermitted("CLIENTS_YURPERSONS_INFO")) { %>
					<% if (isPartner) { %>
					    <%= Bean.getMenuButton("ADD", "../crm/clients/yurpersonupdate.jsp?id=" + jurprs.getValue("ID_JUR_PRS") + "&type=general&action=add2&process=no", "", "") %>
					    <%= Bean.getMenuButton("DELETE", "../crm/clients/yurpersonupdate.jsp?id=" + jurprs.getValue("ID_JUR_PRS") + "&type=general&action=remove&process=yes", Bean.jurpersonXML.getfieldTransl("LAB_DELETE_FLD", false), jurprs.getValue("SNAME_JUR_PRS")) %>
					<% } else { %>
				    	<%= Bean.getMenuButton("ADD", "../crm/clients/yurpersonserviceplaceupdate.jsp?id_jur_prs=" + jurprs.getValue("ID_JUR_PRS_PARENT") + "&type=general&action=add&process=no", "", "") %>
				    	<%= Bean.getMenuButton("DELETE", "../crm/clients/yurpersonserviceplaceupdate.jsp?id_jur_prs=" + jurprs.getValue("ID_JUR_PRS") + "&id_service_place=" + jurprs.getValue("ID_JUR_PRS") + "&type=general&action=remove&process=yes", Bean.jurpersonXML.getfieldTransl("LAB_DELETE_SERVICE_PLACE", false), jurprs.getValue("SNAME_JUR_PRS")) %>
					<% } %>
				<% } %>
				<%= Bean.getReportHyperLink("SR07", "ID_JUR_PRS=" + jurprs.getValue("ID_JUR_PRS") + "&DATE_REPORT=" + Bean.getSysDate()) %>
			<% } %>
			
			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLIENTS_YURPERSONS_ISSUER")) {%>
			    <!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagIssuerParam, "../crm/clients/yurpersonspecs.jsp?id=" + jurprs.getValue("ID_JUR_PRS") + "&tab="+Bean.currentMenu.getTabID("CLIENTS_YURPERSONS_ISSUER")+"&", "issuer_page") %>
			<% } %>
			
			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLIENTS_YURPERSONS_CONTACTS")) {%>
				<% if (Bean.currentMenu.isCurrentTabAndEditPermitted("CLIENTS_YURPERSONS_CONTACTS")) { %>
				    <%= Bean.getMenuButtonBase("ADD", "../crm/clients/contact_prsupdate.jsp?back_type=PARTNER&id=" + jurprs.getValue("ID_JUR_PRS") + "&id_jur_prs="+(isPartner?jurprs.getValue("ID_JUR_PRS"):jurprs.getValue("ID_JUR_PRS_PARENT")) + "&id_service_place_work=" + (!isPartner?jurprs.getValue("ID_JUR_PRS"):"") + "&type=general&action=add&process=no", "", "", "", "div_data_detail") %>
				<% } %>
			    <!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagContacts, "../crm/clients/yurpersonspecs.jsp?id=" + jurprs.getValue("ID_JUR_PRS") + "&tab="+Bean.currentMenu.getTabID("CLIENTS_YURPERSONS_CONTACTS")+"&", "contact_page") %>
			<% } %>
			
			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLIENTS_YURPERSONS_DOCUMENTS")) {%>
				<% if (Bean.currentMenu.isCurrentTabAndEditPermitted("CLIENTS_YURPERSONS_DOCUMENTS")) { %>
				    <%= Bean.getMenuButtonBase("ADD", "../crm/club/documentupdate.jsp?back_type=PARTNER&id=" + jurprs.getValue("ID_JUR_PRS") + "&id_jur_prs=" + jurprs.getValue("ID_JUR_PRS") + "&type=general&action=add&process=no", "", "", "", "div_data_detail") %>
				<% } %>
			    <!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagDoc, "../crm/clients/yurpersonspecs.jsp?id=" + jurprs.getValue("ID_JUR_PRS") + "&tab="+Bean.currentMenu.getTabID("CLIENTS_YURPERSONS_DOCUMENTS")+"&", "doc_page") %>
			<% } %>
	
			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLIENTS_YURPERSONS_SERVICEPLACE")) { %>
				<% if (Bean.currentMenu.isCurrentTabAndEditPermitted("CLIENTS_YURPERSONS_SERVICEPLACE")) { %>
				    <%= Bean.getMenuButtonBase("ADD", "../crm/clients/yurpersonserviceplaceupdate.jsp?id_jur_prs=" + jurprs.getValue("ID_JUR_PRS") + "&type=general&action=add&process=no", "", "", "", "div_data_detail") %>
				<% } %>
			    <!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagSubmission, "../crm/clients/yurpersonspecs.jsp?id=" + jurprs.getValue("ID_JUR_PRS") + "&tab="+Bean.currentMenu.getTabID("CLIENTS_YURPERSONS_SERVICEPLACE")+"&", "submission_page") %>
			<% } %>
	
			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLIENTS_YURPERSONS_BANKACCNT")) { %>
				<% if (Bean.currentMenu.isCurrentTabAndEditPermitted("CLIENTS_YURPERSONS_BANKACCNT")) { %>
				    <%= Bean.getMenuButtonBase("ADD", "../crm/clients/accountsupdate.jsp?back_type=PARTNER&id=" + jurprs.getValue("ID_JUR_PRS") + "&id_jur_prs=" + jurprs.getValue("ID_JUR_PRS") + "&type=general&action=add&process=no", "", "", "", "div_data_detail") %>
				<% } %>
			    <!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagBankAccounts, "../crm/clients/yurpersonspecs.jsp?id=" + jurprs.getValue("ID_JUR_PRS") + "&tab="+Bean.currentMenu.getTabID("CLIENTS_YURPERSONS_BANKACCNT")+"&", "bank_account_page") %>
			<% } %>
	
			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLIENTS_YURPERSONS_TERM")) { %>
				<% if (Bean.currentMenu.isCurrentTabAndEditPermitted("CLIENTS_YURPERSONS_TERM")) { %>
			    	<%= Bean.getMenuButtonBase("ADD", "../crm/clients/terminalupdate.jsp?back_type=" + (isPartner?"PARTNER":"SERVICE_PLACE") + "&id=" + jurprs.getValue("ID_JUR_PRS") + "&id_partner=" + (isPartner?jurprs.getValue("ID_JUR_PRS"):jurprs.getValue("ID_JUR_PRS_PARENT")) + "&type=term&action=add&process=no", "", "", "", "div_data_detail") %>
				<% } %>
			    <!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagTerminals, "../crm/clients/yurpersonspecs.jsp?id=" + jurprs.getValue("ID_JUR_PRS") + "&tab="+Bean.currentMenu.getTabID("CLIENTS_YURPERSONS_TERM")+"&", "terminals_page") %>
			<% } %>
	
			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLIENTS_YURPERSONS_TRANS")) { %>
			    <!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagTrans, "../crm/clients/yurpersonspecs.jsp?id=" + jurprs.getValue("ID_JUR_PRS") + "&tab="+Bean.currentMenu.getTabID("CLIENTS_YURPERSONS_TRANS")+"&", "trans_page") %>
			<% } %>
	
			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLIENTS_YURPERSONS_TERM_DEVICE_TYPES")) { %>
				<% if (Bean.currentMenu.isCurrentTabAndEditPermitted("CLIENTS_YURPERSONS_TERM_DEVICE_TYPES")) { %>
			    	<%= Bean.getMenuButtonBase("ADD", "../crm/clients/yurpersonupdate.jsp?id=" + jurprs.getValue("ID_JUR_PRS") + "&type=device&action=add&process=no", "", "", "", "div_data_detail") %>
				<% } %>
	
			    <!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagDevices, "../crm/clients/yurpersonspecs.jsp?id=" + jurprs.getValue("ID_JUR_PRS") + "&tab="+Bean.currentMenu.getTabID("CLIENTS_YURPERSONS_TERM_DEVICE_TYPES")+"&", "device_page") %>
			<% } %>
	
			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLIENTS_YURPERSONS_COMISSION")) { %>
				<% if (Bean.currentMenu.isCurrentTabAndEditPermitted("CLIENTS_YURPERSONS_COMISSION")) {	%>
					<%= Bean.getMenuButtonBase("ADD", "../crm/clients/yurpersonscomissionupdate.jsp?id=" + jurprs.getValue("ID_JUR_PRS") + "&type=comission&action=add&process=no", "", "", "", "div_data_detail") %>
					<%= Bean.getMenuButtonBase("ADD_ALL", "../crm/clients/yurpersonscomissionupdate.jsp?id=" + jurprs.getValue("ID_JUR_PRS") + "&type=comission&action=addall&process=yes", "", "", Bean.jurpersonXML.getfieldTransl("h_add_all_comissions", false), "div_data_detail") %>
					<%= Bean.getMenuButton("DELETE_ALL", "../crm/clients/yurpersonscomissionupdate.jsp?id=" + jurprs.getValue("ID_JUR_PRS") + "&type=comission&action=removeall&process=yes", Bean.jurpersonXML.getfieldTransl("h_delete_all_comissions", false), "") %>
				<% } %>
			    <!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagComission, "../crm/clients/yurpersonspecs.jsp?id=" + jurprs.getValue("ID_JUR_PRS") + "&tab="+Bean.currentMenu.getTabID("CLIENTS_YURPERSONS_COMISSION")+"&", "comis_page") %>
			<% } %>
	
			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLIENTS_YURPERSONS_BK_ACCOUNTS")) { %>
			    <!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagBKAccounts, "../crm/clients/yurpersonspecs.jsp?id=" + jurprs.getValue("ID_JUR_PRS") + "&tab="+Bean.currentMenu.getTabID("CLIENTS_YURPERSONS_BK_ACCOUNTS")+"&", "bk_acc_page") %>
			<% } %>
	
			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLIENTS_YURPERSONS_RELATIONSHIPS")) { %>
				<% if (Bean.currentMenu.isCurrentTabAndEditPermitted("CLIENTS_YURPERSONS_RELATIONSHIPS")) { %>
				    <%= Bean.getMenuButton("ADD", "../crm/clients/yurpersonupdate.jsp?id=" + jurprs.getValue("ID_JUR_PRS") + "&type=relationship&action=add&process=no", "", "") %>
				<% } %>
			    <!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagRelationShips, "../crm/clients/yurpersonspecs.jsp?id=" + jurprs.getValue("ID_JUR_PRS") + "&tab="+Bean.currentMenu.getTabID("CLIENTS_YURPERSONS_RELATIONSHIPS")+"&rel_kind=" + rel_kind + "&", "relation_page") %>
			<% } %>
	
			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLIENTS_YURPERSONS_NOMENKLATURE")) { %>
				<% if (Bean.currentMenu.isCurrentTabAndEditPermitted("CLIENTS_YURPERSONS_NOMENKLATURE")) { %>
				    <%= Bean.getMenuButtonBase("ADD", "../crm/clients/yurpersonupdate.jsp?id=" + jurprs.getValue("ID_JUR_PRS") + "&type=nomenkl&action=add&process=no", "", "", "", "div_data_detail") %>
				<% } %>
			    <!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagNomenkl, "../crm/clients/yurpersonspecs.jsp?id=" + jurprs.getValue("ID_JUR_PRS") + "&tab="+Bean.currentMenu.getTabID("CLIENTS_YURPERSONS_NOMENKLATURE")+"&", "nomenkl_page") %>
			<% } %>
	
			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLIENTS_YURPERSONS_LOGISTIC")) { %>
			    <!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagLogistic, "../crm/clients/yurpersonspecs.jsp?id=" + jurprs.getValue("ID_JUR_PRS") + "&tab="+Bean.currentMenu.getTabID("CLIENTS_YURPERSONS_LOGISTIC")+"&", "logistic_page") %>
			<% } %>
	
			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLIENTS_YURPERSONS_ACCESS")) { %>
			    <!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagAccess, "../crm/clients/yurpersonspecs.jsp?id=" + jurprs.getValue("ID_JUR_PRS") + "&tab="+Bean.currentMenu.getTabID("CLIENTS_YURPERSONS_ACCESS")+"&", "access_page") %>
			<% } %>
	
			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLIENTS_YURPERSONS_CARD_PACKAGE")) { %>
			    <% if (Bean.currentMenu.isCurrentTabAndEditPermitted("CLIENTS_YURPERSONS_CARD_PACKAGE")) { %>
				    <%= Bean.getMenuButtonBase("ADD", "../crm/club/card_packageupdate.jsp?back_type=PARTNER&id=" + jurprs.getValue("ID_JUR_PRS") + "&id_jur_prs=" + jurprs.getValue("ID_JUR_PRS") + "&type=general&action=add&process=no", "", "", "", "div_data_detail") %>
				    <%= Bean.getMenuButtonBase("COPY", "../crm/club/card_packageupdate.jsp?back_type=PARTNER&id=" + jurprs.getValue("ID_JUR_PRS") + "&id_jur_prs=" + jurprs.getValue("ID_JUR_PRS") + "&type=general&action=select&process=no", "", "", "", "div_data_detail") %>
				<%  } %>
				<!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagCardPackage, "../crm/clients/yurpersonspecs.jsp?id=" + jurprs.getValue("ID_JUR_PRS") + "&tab="+Bean.currentMenu.getTabID("CLIENTS_YURPERSONS_CARD_PACKAGE")+"&", "card_pack_page") %>
			<% } %>
	
			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLIENTS_YURPERSONS_REFERRAL_SCHEME")) { %>
			    <% if (Bean.currentMenu.isCurrentTabAndEditPermitted("CLIENTS_YURPERSONS_REFERRAL_SCHEME")) { %>
				    <%= Bean.getMenuButtonBase("ADD", "../crm/club/referral_schemeupdate.jsp?back_type=PARTNER&id=" + jurprs.getValue("ID_JUR_PRS") + "&type=general&action=add&process=no", "", "", "", "div_data_detail") %>
				    <%= Bean.getMenuButtonBase("COPY", "../crm/club/referral_schemeupdate.jsp?back_type=PARTNER&id=" + jurprs.getValue("ID_JUR_PRS") + "&type=general&action=select&process=no", "", "", "", "div_data_detail") %>
				<%  } %>
				<!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagReferralScheme, "../crm/clients/yurpersonspecs.jsp?id=" + jurprs.getValue("ID_JUR_PRS") + "&tab="+Bean.currentMenu.getTabID("CLIENTS_YURPERSONS_REFERRAL_SCHEME")+"&", "referral_scheme_page") %>
			<% } %>
	
			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLIENTS_YURPERSONS_LOYALITY_SCHEME")) { %>
			    <% if (Bean.currentMenu.isCurrentTabAndEditPermitted("CLIENTS_YURPERSONS_LOYALITY_SCHEME")) { %>
				    <%= Bean.getMenuButtonBase("ADD", "../crm/clients/loyupdate.jsp?back_type=PARTNER&id=" + jurprs.getValue("ID_JUR_PRS") + "&type=general&action=add&process=no", "", "", "", "div_data_detail") %>
				    <%= Bean.getMenuButtonBase("COPY", "../crm/clients/loyupdate.jsp?back_type=PARTNER&id=" + jurprs.getValue("ID_JUR_PRS") + "&type=general&action=select&process=no", "", "", "", "div_data_detail") %>
				<%  } %>
			    <!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagLoyality, "../crm/clients/yurpersonspecs.jsp?id=" + jurprs.getValue("ID_JUR_PRS") + "&tab="+Bean.currentMenu.getTabID("CLIENTS_YURPERSONS_LOYALITY_SCHEME")+"&", "loyality_page") %>
			<% } %>
	
			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLIENTS_YURPERSONS_TARGET_PROGRAMS")) { %>
			    <% if (Bean.currentMenu.isCurrentTabAndEditPermitted("CLIENTS_YURPERSONS_TARGET_PROGRAMS")) { %>
				    <%= Bean.getMenuButtonBase("ADD", "../crm/club/target_programupdate.jsp?back_type=PARTNER&id=" + jurprs.getValue("ID_JUR_PRS") + "&type=general&action=add&process=no", "", "", "", "div_data_detail") %>
				    <%= Bean.getMenuButtonBase("COPY", "../crm/club/target_programupdate.jsp?back_type=PARTNER&id=" + jurprs.getValue("ID_JUR_PRS") + "&type=general&action=select&process=no", "", "", "", "div_data_detail") %>
				<%  } %>
				 <!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagTargetProgram, "../crm/clients/yurpersonspecs.jsp?id=" + jurprs.getValue("ID_JUR_PRS") + "&tab="+Bean.currentMenu.getTabID("CLIENTS_YURPERSONS_TARGET_PROGRAMS")+"&", "target_program_page") %>
			<% } %>
	
			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLIENTS_YURPERSONS_ACCESS_ROLES")) { %>
			    <% if (Bean.currentMenu.isCurrentTabAndEditPermitted("CLIENTS_YURPERSONS_ACCESS_ROLES")) { %>
				    <%= Bean.getMenuButtonBase("ADD", "../crm/security/rolesupdate.jsp?back_type=PARTNER&id=" + jurprs.getValue("ID_JUR_PRS") + "&type=general&action=add&process=no", "", "", "", "div_data_detail") %>
				    <%= Bean.getMenuButtonBase("COPY", "../crm/security/rolesupdate.jsp?back_type=PARTNER&id=" + jurprs.getValue("ID_JUR_PRS") + "&type=general&action=select&process=no", "", "", "", "div_data_detail") %>
				<%  } %>
			    <!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagRole, "../crm/clients/yurpersonspecs.jsp?id=" + jurprs.getValue("ID_JUR_PRS") + "&tab="+Bean.currentMenu.getTabID("CLIENTS_YURPERSONS_ACCESS_ROLES")+"&", "role_page") %>
			<% } %>
		</tr>
	</table>
	<table <%=Bean.getTableMenuParam() %>>
		<%= Bean.getDetailCaption(jurprs.getValue("SNAME_JUR_PRS") + (isPartner?"":" ("+jurprs.getValue("SNAME_JUR_PRS_PARENT")+")")) %>
		<tr>
			<td>
				<% if ("N".equalsIgnoreCase(jurprs.getValue("IS_ISSUER"))) { 
	            	if (Bean.currentMenu.isCurrentTab("CLIENTS_YURPERSONS_ISSUER")) {
	            		Bean.currentMenu.setFirstCurrentTab();
	            		tab = Bean.currentMenu.getCurrentTab();
	            		Bean.tabsHmSetValue(pageFormName, tab);
	            	}
	            	Bean.currentMenu.setExistFlag("CLIENTS_YURPERSONS_ISSUER", false);
	            } else {
	            	if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLIENTS_YURPERSONS_ISSUER")) {
	            		Bean.currentMenu.setExistFlag("CLIENTS_YURPERSONS_ISSUER", true);
	            	}
	            } %>
				<% if (!("Y".equalsIgnoreCase(jurprs.getValue("IS_TERMINAL_MANUFACTURER")))) { 
	            	if (Bean.currentMenu.isCurrentTab("CLIENTS_YURPERSONS_TERM_DEVICE_TYPES")) {
	            		Bean.currentMenu.setFirstCurrentTab();
	            		tab = Bean.currentMenu.getCurrentTab();
	            		Bean.tabsHmSetValue(pageFormName, tab);
	            	}
	            	Bean.currentMenu.setExistFlag("CLIENTS_YURPERSONS_TERM_DEVICE_TYPES", false);
	            } else {
	            	if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLIENTS_YURPERSONS_TERM_DEVICE_TYPES")) { 
	            		Bean.currentMenu.setExistFlag("CLIENTS_YURPERSONS_TERM_DEVICE_TYPES", true);
	            	}
	            } %>
				<!-- Выводим перечень закладок -->
				<%= Bean.currentMenu.getFormTabScheeds(tab, "../crm/clients/yurpersonspecs.jsp?id=" + jurprs.getValue("ID_JUR_PRS") + "&adr=full") %>
			</td>
		</tr>
	</table>
</div>
<div id="div_data">
<div id="div_data_detail">

<%

if (Bean.currentMenu.isCurrentTabAndEditPermitted("CLIENTS_YURPERSONS_INFO")) {
	
	if (isPartner) {
%>

			<script language="JavaScript">
				var formGeneral = new Array (
					new Array ('cd_jur_prs_kind', 'varchar2', 1),
					new Array ('SNAME_JUR_PRS', 'varchar2', 1),
					new Array ('NAME_JUR_PRS', 'varchar2', 1),
					new Array ('reg_code_country', 'varchar2', 1),
					new Array ('tax_percent', 'varchar2', 1),
					new Array ('cd_club_jur_prs', 'varchar2', 1),
					new Array ('cd_club_member_status', 'varchar2', 1),
					new Array ('cd_club_member_type', 'varchar2', 1),
					new Array ('club_date_beg', 'varchar2', 1)
				);
				
				function myValidateForm() {
					return validateForm(formGeneral);
				}

				var status = document.getElementById('cd_club_member_status').value;
				checkJurPrsClubMemberStatus(status);
			</script>
    <form action="../crm/clients/yurpersonupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return myValidateForm()">
        <input type="hidden" name="type" value="general">
        <input type="hidden" name="action" value="edit">
        <input type="hidden" name="process" value="yes">
	    <input type="hidden" name="cd_jur_prs_status" value="<%=jurprs.getValue("CD_JUR_PRS_STATUS") %>">
        <input type="hidden" name="id_jur_prs" value="<%=jurprs.getValue("ID_JUR_PRS") %>">
        <input type="hidden" name="LUD" value="<%=jurprs.getValue("LUD") %>">
	<table <%=Bean.getTableDetailParam() %>>
		<tr>
			<td>
				<%= Bean.jurpersonXML.getfieldTransl("jur_prs_kind", true) %></td><td><select name="cd_jur_prs_kind" id="cd_jur_prs_kind" class="inputfield"><%= Bean.getJurPrsKindOptions(jurprs.getValue("CD_JUR_PRS_KIND"), true) %> </select>
			</td>
			<td><%= Bean.jurpersonXML.getfieldTransl("reg_code_country", true) %></td><td><select name="reg_code_country" class="inputfield"> <%= Bean.getCountryOptions(jurprs.getValue("REG_CODE_COUNTRY"), true) %> </select>
		</tr>
		<tr>
			<td>&nbsp;</td>
			<td>
				<%=Bean.getCheckBox("is_resident", jurprs.getValue("IS_RESIDENT"), Bean.jurpersonXML.getfieldTransl("is_resident", false)) %>
			</td>
			<td><span id="inn_number_span"><%= Bean.jurpersonXML.getfieldTransl("inn_number", false) %></span></td> <td><input type="text" name="inn_number" id="inn_number" size="20" value="<%= jurprs.getValue("INN_NUMBER") %>" class="inputfield"> </td>
		</tr>
		<tr>
			<td><%= Bean.jurpersonXML.getfieldTransl("NAME_JUR_PRS", true) %></td> <td><input type="text" name="NAME_JUR_PRS" size="45" value="<%= jurprs.getValue("NAME_JUR_PRS") %>" class="inputfield"></td>
			<td><%= Bean.jurpersonXML.getfieldTransl("kpp_number", false) %></td> <td><input type="text" name="kpp_number" id="kpp_number" size="20" value="<%= jurprs.getValue("KPP_NUMBER") %>" class="inputfield"> </td>
		</tr>
		<tr>
			<td><%= Bean.jurpersonXML.getfieldTransl("SNAME_JUR_PRS", true) %></td> <td><input type="text" name="SNAME_JUR_PRS" size="45" value="<%= jurprs.getValue("SNAME_JUR_PRS") %>" class="inputfield"></td>
			<td><%= Bean.jurpersonXML.getfieldTransl("ogrn_number", false) %></td> <td><input type="text" name="ogrn_number" id="ogrn_number" size="20" value="<%= jurprs.getValue("OGRN_NUMBER") %>" class="inputfield"> </td>
		</tr>
		<tr>
			<td><%= Bean.jurpersonXML.getfieldTransl("id_jur_prs_parent", false) %>
				<%=Bean.getGoToJurPrsHyperLink(jurprs.getValue("ID_JUR_PRS_PARENT")) %>
			</td>
			<td>
				<%=Bean.getWindowFindJurPrs("jur_prs_parent", jurprs.getValue("ID_JUR_PRS_PARENT"), "ALL", "30") %>
			</td>			
			<td><%= Bean.jurpersonXML.getfieldTransl("mfo_bank", false) %></td> <td><input type="text" name="mfo_bank" id="mfo_bank" size="20" value="<%= jurprs.getValue("MFO_BANK") %>" class="inputfield"></td>
		</tr>
		<tr>
			<td colspan="2">&nbsp;</td>			
			<td><%= Bean.jurpersonXML.getfieldTransl("tax_percent", true) %></td> <td><input type="text" name="tax_percent" id="tax_percent" size="10" value="<%= jurprs.getValue("TAX_PERCENT") %>" class="inputfield"></td>
		</tr>

		<tr>							
			<td colspan="2" class="top_line"><b><%= Bean.jurpersonXML.getfieldTransl("h_club_registration", false) %></b></td>
			<td colspan="2" class="top_line"><b><%= Bean.jurpersonXML.getfieldTransl("h_club_member_parameters", false) %></b></td>
		</tr>
		<tr>
			<td><%= Bean.clubXML.getfieldTransl("club", false) %>
				<%= Bean.getGoToClubLink(jurprs.getValue("ID_CLUB")) %>
			</td> 
			<td>
				<input type="hidden" name="id_club" size="35" value="<%=jurprs.getValue("ID_CLUB") %>">
				<input type="text" name="name_club" size="35" value="<%=Bean.getClubShortName(jurprs.getValue("ID_CLUB")) %>" readonly="readonly" class="inputfield-ro">
			</td>
			<td><%=Bean.getCheckBoxBase("is_shareholder", jurprs.getValue("IS_SHAREHOLDER"), Bean.jurpersonXML.getfieldTransl("is_shareholder", false), "color:green;", "checkJurPrsShareholder();", false, false) %></td>
			<td><%=Bean.getCheckBoxBase("is_dealer", jurprs.getValue("IS_DEALER"), Bean.jurpersonXML.getfieldTransl("is_dealer", false), "color:blue;", false, false) %></td>
		</tr>

		<tr>
			<td><%= Bean.jurpersonXML.getfieldTransl("cd_club_jur_prs", true) %></td> <td><input type="text" name="cd_club_jur_prs" size="20" value="<%= jurprs.getValue("CD_CLUB_JUR_PRS") %>" class="inputfield"></td>
			<td><%=Bean.getCheckBoxBase("is_registrator", jurprs.getValue("IS_REGISTRATOR"), Bean.jurpersonXML.getfieldTransl("is_registrator", false), "color:green;", false, false) %></td>
			<td><%=Bean.getCheckBox("is_bank", jurprs.getValue("IS_BANK"), Bean.jurpersonXML.getfieldTransl("is_bank", false)) %></td>
		</tr>
		<tr>
			<td><%= Bean.jurpersonXML.getfieldTransl("club_date_beg", true) %></td> <td><%=Bean.getCalendarInputField("club_date_beg", jurprs.getValue("DATE_BEG_FRMT"), "10") %></td>
			<td><%=Bean.getCheckBoxBase("is_coordinator", jurprs.getValue("IS_COORDINATOR"), Bean.jurpersonXML.getfieldTransl("is_coordinator", false), "color:green;", false, false) %></td>
			<td><%=Bean.getCheckBox("is_issuer", jurprs.getValue("IS_ISSUER"), Bean.jurpersonXML.getfieldTransl("is_issuer", false)) %></td>
		</tr>
		<tr>
			<td><%= Bean.jurpersonXML.getfieldTransl("club_date_end", false) %></td> <td><%=Bean.getCalendarInputField("club_date_end", jurprs.getValue("DATE_END_FRMT"), "10") %></td>
			<td><%=Bean.getCheckBoxBase("is_curator", jurprs.getValue("IS_CURATOR"), Bean.jurpersonXML.getfieldTransl("is_curator", false), "color:green;", false, false) %></td>
			<td><%=Bean.getCheckBox("is_finance_acquirer", jurprs.getValue("IS_FINANCE_ACQUIRER"), Bean.jurpersonXML.getfieldTransl("is_finance_acquirer", false)) %></td>
		</tr>
		<tr>
			<td><%= Bean.clubXML.getfieldTransl("club_member_status", true) %></td><td><select name="cd_club_member_status" id="cd_club_member_status" class="inputfield" onchange="checkJurPrsClubMemberStatus(this.value);"> <%= Bean.getClubMemberStatusOptions(jurprs.getValue("CD_CLUB_MEMBER_STATUS"), true) %> </select></td>
			<td><%=Bean.getCheckBoxBase("is_operator", jurprs.getValue("IS_OPERATOR"), Bean.jurpersonXML.getfieldTransl("is_operator", false), "color:red;", false, false) %></td>
			<td><%=Bean.getCheckBox("is_technical_acquirer", jurprs.getValue("IS_TECHNICAL_ACQUIRER"), Bean.jurpersonXML.getfieldTransl("is_technical_acquirer", false)) %></td>
		</tr>
		<tr>
			<td colspan="2">&nbsp;</td>
			<td><%=Bean.getCheckBoxBase("is_agent", jurprs.getValue("IS_AGENT"), Bean.jurpersonXML.getfieldTransl("is_agent", false), "color:red;", false, false) %></td>
			<td><%=Bean.getCheckBox("is_terminal_manufacturer", jurprs.getValue("IS_TERMINAL_MANUFACTURER"), Bean.jurpersonXML.getfieldTransl("is_terminal_manufacturer", false)) %></td>
		</tr>
		<tr>							
			<td colspan="4" class="top_line"><b><%= Bean.jurpersonXML.getfieldTransl("jur_adr_full", false) %></b></td>
		</tr>
		<tr>								
			<td><%= Bean.jurpersonXML.getfieldTransl("name_country", false) %></td><td><select name="JUR_ADR_CODE_COUNTRY" id="JUR_ADR_CODE_COUNTRY" class="inputfield" onchange="dwr_make_jur_adr('<%=Bean.getSessionId() %>');"><%= Bean.getCountryOptions(jurprs.getValue("JUR_ADR_CODE_COUNTRY"), true) %></select></td>
			<td><%= Bean.jurpersonXML.getfieldTransl("ADR_CITY", false) %></td><td><input type="text" name="JUR_ADR_CITY" id="JUR_ADR_CITY" size="25" value="<%=jurprs.getValue("JUR_ADR_CITY") %>"  class="inputfield"></td>				
		</tr>
		<tr>
			<td><%= Bean.jurpersonXML.getfieldTransl("ADR_ZIP_CODE", false) %></td><td><input type="text" name="JUR_ADR_ZIP_CODE" id="JUR_ADR_ZIP_CODE" size="25" value="<%=jurprs.getValue("JUR_ADR_ZIP_CODE") %>"  class="inputfield"></td>
			<td><%= Bean.jurpersonXML.getfieldTransl("ADR_CITY_DISTRICT", false) %></td><td><input type="text" name="JUR_ADR_CITY_DISTRICT" id="JUR_ADR_CITY_DISTRICT" size="25" value="<%=jurprs.getValue("JUR_ADR_CITY_DISTRICT") %>"  class="inputfield"></td>				
		</tr>
		<tr>
			<td><%= Bean.jurpersonXML.getfieldTransl("ADR_OBLAST", false) %></td><td><select name="JUR_ADR_ID_OBLAST" id="JUR_ADR_ID_OBLAST" class="inputfield" onchange="dwr_make_jur_adr('<%=Bean.getSessionId() %>');"><%= Bean.getOblastOptions(jurprs.getValue("JUR_ADR_CODE_COUNTRY"), jurprs.getValue("JUR_ADR_ID_OBLAST"), true) %></select></td>				
			<td><%= Bean.jurpersonXML.getfieldTransl("ADR_STREET", false) %></td><td><input type="text" name="JUR_ADR_STREET" id="JUR_ADR_STREET" size="25" value="<%=jurprs.getValue("JUR_ADR_STREET") %>" class="inputfield"></td>
		</tr>
		<tr>
			<td><%= Bean.jurpersonXML.getfieldTransl("ADR_DISTRICT", false) %></td><td><input type="text" name="JUR_ADR_DISTRICT" id="JUR_ADR_DISTRICT" size="25" value="<%=jurprs.getValue("JUR_ADR_DISTRICT") %>"  class="inputfield"></td>				
			<td><%= Bean.jurpersonXML.getfieldTransl("ADR_HOUSE_CASE_APARTMENT", false) %></td><td><input type="text" name="JUR_ADR_HOUSE" id="JUR_ADR_HOUSE" size="6" value="<%=jurprs.getValue("JUR_ADR_HOUSE") %>"  class="inputfield">/<input type="text" name="JUR_ADR_CASE" id="JUR_ADR_CASE" size="6" value="<%=jurprs.getValue("JUR_ADR_CASE") %>"  class="inputfield">/<input type="text" name="JUR_ADR_APARTMENT" id="JUR_ADR_APARTMENT" size="6" value="<%=jurprs.getValue("JUR_ADR_APARTMENT") %>"  class="inputfield"></td>
		</tr>
		<tr>								
			<td colspan="4" class="top_line"><b><%= Bean.jurpersonXML.getfieldTransl("fact_adr_full", false) %></b>
			&nbsp;&nbsp;<button type="button" class="button" onclick="copyJurPrsFactAddress('<%=Bean.getSessionId() %>'); "><%= Bean.jurpersonXML.getfieldTransl("button_copy_jur_address", false) %> </button>
			</td>
		</tr>
		<tr>								
			<td><%= Bean.jurpersonXML.getfieldTransl("name_country", false) %></td><td><select name="FACT_ADR_CODE_COUNTRY" id="FACT_ADR_CODE_COUNTRY" class="inputfield" onchange="dwr_make_fact_adr('<%=Bean.getSessionId() %>');"><%= Bean.getCountryOptions(jurprs.getValue("FACT_ADR_CODE_COUNTRY"), true) %> </select></td>
			<td><%= Bean.jurpersonXML.getfieldTransl("ADR_CITY", false) %></td><td><input type="text" name="FACT_ADR_CITY" id="FACT_ADR_CITY" size="25" value="<%=jurprs.getValue("FACT_ADR_CITY") %>"  class="inputfield"></td>				
		</tr>
		<tr>
			<td><%= Bean.jurpersonXML.getfieldTransl("ADR_ZIP_CODE", false) %></td><td><input type="text" name="FACT_ADR_ZIP_CODE" id="FACT_ADR_ZIP_CODE" size="25" value="<%=jurprs.getValue("FACT_ADR_ZIP_CODE") %>"  class="inputfield"></td>
			<td><%= Bean.jurpersonXML.getfieldTransl("ADR_CITY_DISTRICT", false) %></td><td><input type="text" name="FACT_ADR_CITY_DISTRICT" id="FACT_ADR_CITY_DISTRICT" size="25" value="<%=jurprs.getValue("FACT_ADR_CITY_DISTRICT") %>"  class="inputfield"></td>				
		</tr>
		<tr>
			<td><%= Bean.jurpersonXML.getfieldTransl("ADR_OBLAST", false) %></td><td><select name="FACT_ADR_ID_OBLAST" id="FACT_ADR_ID_OBLAST" class="inputfield" onchange="dwr_make_fact_adr('<%=Bean.getSessionId() %>');"><%= Bean.getOblastOptions(jurprs.getValue("FACT_ADR_CODE_COUNTRY"), jurprs.getValue("FACT_ADR_ID_OBLAST"), true) %></select></td>				
			<td><%= Bean.jurpersonXML.getfieldTransl("ADR_STREET", false) %></td><td><input type="text" name="FACT_ADR_STREET" id="FACT_ADR_STREET" size="25" value="<%=jurprs.getValue("FACT_ADR_STREET") %>" class="inputfield"></td>
		</tr>
		<tr>
			<td><%= Bean.jurpersonXML.getfieldTransl("ADR_DISTRICT", false) %></td><td><input type="text" name="FACT_ADR_DISTRICT" id="FACT_ADR_DISTRICT" size="25" value="<%=jurprs.getValue("FACT_ADR_DISTRICT") %>"  class="inputfield"></td>				
			<td><%= Bean.jurpersonXML.getfieldTransl("ADR_HOUSE_CASE_APARTMENT", false) %></td><td><input type="text" name="FACT_ADR_HOUSE" id="FACT_ADR_HOUSE" size="6" value="<%=jurprs.getValue("FACT_ADR_HOUSE") %>"  class="inputfield">/<input type="text" name="FACT_ADR_CASE" id="FACT_ADR_CASE" size="6" value="<%=jurprs.getValue("FACT_ADR_CASE") %>"  class="inputfield">/<input type="text" name="FACT_ADR_APARTMENT" id="FACT_ADR_APARTMENT" size="6" value="<%=jurprs.getValue("FACT_ADR_APARTMENT") %>"  class="inputfield"></td>
		</tr>
		<tr>								
			<td colspan="4" class="top_line"><b><%= Bean.jurpersonXML.getfieldTransl("title_contact_info", false) %></b></td>
		</tr>
		<tr>								
			<td><%= Bean.jurpersonXML.getfieldTransl("phone_work", false) %></td><td><input type="text" name="phone_work" id="phone_work" size="25" value="<%=jurprs.getValue("PHONE_WORK") %>"  class="inputfield"></td>
			<td><%= Bean.jurpersonXML.getfieldTransl("web_site", false) %></td><td><input type="text" name="web_site" id="web_site" size="25" value="<%=jurprs.getValue("WEB_SITE") %>"  class="inputfield"></td>
		</tr>
		<tr>
			<td><%= Bean.jurpersonXML.getfieldTransl("fax", false) %></td><td><input type="text" name="fax" id="fax" size="25" value="<%=jurprs.getValue("FAX") %>"  class="inputfield"></td>
			<td><%= Bean.jurpersonXML.getfieldTransl("email", false) %></td><td><input type="text" name="email" id="email" size="25" value="<%=jurprs.getValue("EMAIL") %>"  class="inputfield"></td>
		</tr>
		<tr>								
			<td colspan="4" class="top_line"><b><%= Bean.jurpersonXML.getfieldTransl("title_others", false) %></b></td>
		</tr>
		<tr>								
			<td><%= Bean.jurpersonXML.getfieldTransl("name_jur_prs_in_sms", false) %></td><td><input type="text" name="name_jur_prs_in_sms" id="name_jur_prs_in_sms" size="45" value="<%=jurprs.getValue("NAME_JUR_PRS_IN_SMS") %>"  class="inputfield"></td>
			<td colspan="2">&nbsp;</td>
		</tr>
		<%=	Bean.getIdCreationAndMoficationRecordFields(
				jurprs.getValue("ID_JUR_PRS"),
				jurprs.getValue(Bean.getCreationDateFieldName()),
				jurprs.getValue("CREATED_BY"),
				jurprs.getValue(Bean.getLastUpdateDateFieldName()),
				jurprs.getValue("LAST_UPDATE_BY")
			) %>
		<tr>
			<td colspan="4" align="center">
				<%=Bean.getSubmitButtonAjax("../crm/clients/yurpersonupdate.jsp") %>
				<%=Bean.getResetButton() %>
				<%=Bean.getGoBackButton("../crm/clients/yurpersons.jsp") %>
			</td>
		</tr>

	</table>

	</form> 
		<%= Bean.getCalendarScript("club_date_beg", false) %>
		<%= Bean.getCalendarScript("club_date_end", false) %>
	<% } else { 
	
		bcJurPrsObject parent = new bcJurPrsObject(jurprs.getValue("ID_JUR_PRS_PARENT"));
		%>
		<script language="JavaScript">
			function myValidateForm() {
				var formData = new Array (
					new Array ('name_service_place', 'varchar2', 1),
					new Array ('sname_service_place', 'varchar2', 1),
					new Array ('id_jur_prs_form', 'varchar2', 1),
					new Array ('cd_club_service_place', 'varchar2', 1),
					new Array ('club_date_beg', 'varchar2', 1),
					new Array ('cd_club_member_status', 'varchar2', 1)
				);
				return validateForm(formData);
			}
    	    function copyJurPrsFactAddress(){
    	    	document.getElementById('ADR_ZIP_CODE').value = '<%=parent.getValue("FACT_ADR_ZIP_CODE")%>';
    	    	document.getElementById('ADR_DISTRICT').value = '<%=parent.getValue("FACT_ADR_STREET")%>';
    	    	document.getElementById('ADR_CITY').value = '<%=parent.getValue("FACT_ADR_STREET")%>';
    	    	document.getElementById('ADR_CITY_DISTRICT').value = '<%=parent.getValue("FACT_ADR_STREET")%>';
    	    	document.getElementById('ADR_STREET').value = '<%=parent.getValue("FACT_ADR_STREET")%>';
    	    	document.getElementById('ADR_HOUSE').value = '<%=parent.getValue("FACT_ADR_HOUSE")%>';
    	    	document.getElementById('ADR_CASE').value = '<%=parent.getValue("FACT_ADR_CASE")%>';
    	    	document.getElementById('ADR_APARTMENT').value = '<%=parent.getValue("FACT_ADR_APARTMENT")%>';
    	    	dwr_make_ser_place_adr_copy('<%=parent.getValue("FACT_ADR_CODE_COUNTRY")%>', '<%=parent.getValue("FACT_ADR_ID_OBLAST")%>', '<%=Bean.getSessionId() %>');
    		}
    	    dwr_make_ser_place_adr('<%=Bean.getSessionId() %>');
    	    
		</script> 
    	<form action="../crm/clients/yurpersonserviceplaceupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">
	        <input type="hidden" name="action" value="edit">
	        <input type="hidden" name="process" value="yes">
	        <input type="hidden" name="cd_jur_prs_status" value="<%=jurprs.getValue("CD_JUR_PRS_STATUS") %>">
	        <input type="hidden" name="id_jur_prs" value="<%=jurprs.getValue("ID_JUR_PRS_PARENT") %>">
	        <input type="hidden" name="id_service_place" value="<%=jurprs.getValue("ID_JUR_PRS") %>">
	        <input type="hidden" name="LUD" value="<%=jurprs.getValue("LUD") %>">
		<table <%=Bean.getTableDetailParam() %>>

		<tr>
			<td><%= Bean.jurpersonXML.getfieldTransl("name_jur_prs_form", true) %></td> <td><select name="id_jur_prs_form" class="inputfield"><%= Bean.getJurPrsFormOptions(jurprs.getValue("ID_JUR_PRS_FORM"), true) %></select></td>
			<td><%= Bean.clubXML.getfieldTransl("club", false) %>
				<%= Bean.getGoToClubLink(jurprs.getValue("ID_CLUB")) %>
			</td> 
			<td>
				<input type="hidden" name="id_club" value="<%=jurprs.getValue("ID_CLUB") %>">
				<input type="text" name="name_club" size="35" value="<%=Bean.getClubShortName(jurprs.getValue("ID_CLUB")) %>" readonly="readonly" class="inputfield-ro">
			</td>
		</tr>
		<tr>
			<td>&nbsp;</td>
			<td>
				<%=Bean.getCheckBox("is_resident", jurprs.getValue("IS_RESIDENT"), Bean.jurpersonXML.getfieldTransl("is_resident", false)) %>
			</td>
			<td><%= Bean.jurpersonXML.getfieldTransl("cd_club_service_place", true) %></td> <td><input type="text" name="cd_club_service_place" size="10" value="<%=jurprs.getValue("CD_CLUB_JUR_PRS") %>" class="inputfield"></td>
		</tr>
		<tr>
			<td><%= Bean.jurpersonXML.getfieldTransl("name_service_place", true) %></td> <td><input type="text" name="name_service_place" size="45" value="<%=jurprs.getValue("NAME_JUR_PRS") %>" class="inputfield"></td>
			<td><%= Bean.jurpersonXML.getfieldTransl("club_date_beg", true) %></td> <td><%=Bean.getCalendarInputField("club_date_beg", jurprs.getValue("DATE_BEG_FRMT"), "10") %></td>
		</tr>
		<tr>
			<td><%= Bean.jurpersonXML.getfieldTransl("sname_service_place", true) %></td> <td><input type="text" name="sname_service_place" size="45" value="<%=jurprs.getValue("SNAME_JUR_PRS") %>" class="inputfield"></td>
			<td><%= Bean.jurpersonXML.getfieldTransl("club_date_end", false) %></td> <td><%=Bean.getCalendarInputField("club_date_end", jurprs.getValue("DATE_END_FRMT"), "10") %></td>
		</tr>
		<tr>
			<td><%= Bean.jurpersonXML.getfieldTransl("title_partner", false) %>
				<%=Bean.getGoToJurPrsHyperLink(jurprs.getValue("ID_JUR_PRS_PARENT")) %>
			</td> <td><input type="text" name="name_jur_prs" size="45" value="<%=jurprs.getValue("SNAME_JUR_PRS_PARENT") %>" readonly="readonly" class="inputfield-ro"> </td>
			<td><%= Bean.clubXML.getfieldTransl("club_member_status", true) %></td><td><select name="cd_club_member_status" class="inputfield"> <%= Bean.getClubMemberStatusOptions(jurprs.getValue("CD_CLUB_MEMBER_STATUS"), true) %> </select></td>
		</tr>
		<tr>								
			<td colspan=8 class="top_line"><b><%= Bean.jurpersonXML.getfieldTransl("fact_adr_full", false) %></b>
			&nbsp;&nbsp;<button type="button" class="button" onclick="copyJurPrsFactAddress(); "><%= Bean.jurpersonXML.getfieldTransl("button_copy_fact_address", false) %> </button>
			</td>
		</tr>
		<tr>								
			<td><%= Bean.jurpersonXML.getfieldTransl("name_country", false) %></td><td><select name="ADR_CODE_COUNTRY" id="ADR_CODE_COUNTRY" class="inputfield" onchange="dwr_oblast_array('ADR_ID_OBLAST', document.getElementById('ADR_CODE_COUNTRY').value, document.getElementById('ADR_ID_OBLAST').value, '<%=Bean.getSessionId() %>');"><%= Bean.getCountryOptions(jurprs.getValue("FACT_ADR_CODE_COUNTRY"), false) %></select></td>
			<td><%= Bean.jurpersonXML.getfieldTransl("ADR_CITY", false) %></td><td><input type="text" name="ADR_CITY" id="ADR_CITY" size="35" value="<%=jurprs.getValue("FACT_ADR_CITY") %>"  class="inputfield"></td>				
		</tr>
		<tr>
			<td><%= Bean.jurpersonXML.getfieldTransl("ADR_ZIP_CODE", false) %></td><td><input type="text" name="ADR_ZIP_CODE" id="ADR_ZIP_CODE" size="35" value="<%=jurprs.getValue("FACT_ADR_ZIP_CODE") %>"  class="inputfield"></td>
			<td><%= Bean.jurpersonXML.getfieldTransl("ADR_CITY_DISTRICT", false) %></td><td><input type="text" name="ADR_CITY_DISTRICT" id="ADR_CITY_DISTRICT" size="35" value="<%=jurprs.getValue("FACT_ADR_CITY_DISTRICT") %>"  class="inputfield"></td>				
		</tr>
		<tr>
			<td><%= Bean.jurpersonXML.getfieldTransl("ADR_OBLAST", false) %></td><td><select name="ADR_ID_OBLAST" id="ADR_ID_OBLAST" class="inputfield"><option value="<%=jurprs.getValue("FACT_ADR_ID_OBLAST") %>"></option></select></td>				
			<td><%= Bean.jurpersonXML.getfieldTransl("ADR_STREET", false) %></td><td><input type="text" name="ADR_STREET" id="ADR_STREET" size="35" value="<%=jurprs.getValue("FACT_ADR_STREET") %>" class="inputfield"></td>
		</tr>
		<tr>
			<td><%= Bean.jurpersonXML.getfieldTransl("ADR_DISTRICT", false) %></td><td><input type="text" name="ADR_DISTRICT" id="ADR_DISTRICT" size="35" value="<%=jurprs.getValue("FACT_ADR_DISTRICT") %>"  class="inputfield"></td>				
			<td><%= Bean.jurpersonXML.getfieldTransl("ADR_HOUSE_CASE_APARTMENT", false) %></td><td><input type="text" name="ADR_HOUSE" id="ADR_HOUSE" size="6" value="<%=jurprs.getValue("FACT_ADR_HOUSE") %>"  class="inputfield">/<input type="text" name="ADR_CASE" id="ADR_CASE" size="6" value="<%=jurprs.getValue("FACT_ADR_CASE") %>"  class="inputfield">/<input type="text" name="ADR_APARTMENT" id="ADR_APARTMENT" size="6" value="<%=jurprs.getValue("FACT_ADR_APARTMENT") %>"  class="inputfield"></td>
		</tr>
		<tr>								
			<td colspan="4" class="top_line"><b><%= Bean.jurpersonXML.getfieldTransl("title_contact_info", false) %></b></td>
		</tr>
		<tr>								
			<td><%= Bean.jurpersonXML.getfieldTransl("phone_work", false) %></td><td><input type="text" name="phone_work" id="phone_work" size="35" value="<%=jurprs.getValue("PHONE_WORK") %>"  class="inputfield"></td>
			<td><%= Bean.jurpersonXML.getfieldTransl("web_site", false) %></td><td><input type="text" name="web_site" id="web_site" size="35" value="<%=jurprs.getValue("WEB_SITE") %>"  class="inputfield"></td>
		</tr>
		<tr>
			<td><%= Bean.jurpersonXML.getfieldTransl("fax", false) %></td><td><input type="text" name="fax" id="fax" size="35" value="<%=jurprs.getValue("FAX") %>"  class="inputfield"></td>
			<td><%= Bean.jurpersonXML.getfieldTransl("email", false) %></td><td><input type="text" name="email" id="email" size="35" value="<%=jurprs.getValue("EMAIL") %>"  class="inputfield"></td>
		</tr>
		<tr>								
			<td colspan="4" class="top_line"><b><%= Bean.jurpersonXML.getfieldTransl("title_others", false) %></b></td>
		</tr>
		<tr>								
			<td><%= Bean.jurpersonXML.getfieldTransl("name_service_place_in_sms", false) %></td><td><input type="text" name="name_jur_prs_in_sms" id="name_jur_prs_in_sms" size="45" value="<%=jurprs.getValue("NAME_JUR_PRS_IN_SMS") %>"  class="inputfield"></td>
			<td colspan="2">&nbsp;</td>
		</tr>
		<%=	Bean.getIdCreationAndMoficationRecordFields(
				jurprs.getValue("ID_JUR_PRS"),
				jurprs.getValue(Bean.getCreationDateFieldName()),
				jurprs.getValue("CREATED_BY"),
				jurprs.getValue(Bean.getLastUpdateDateFieldName()),
				jurprs.getValue("LAST_UPDATE_BY")
			) %>
		<tr>
			<td colspan="8" align="center">
				<%=Bean.getSubmitButtonAjax("../crm/clients/yurpersonserviceplaceupdate.jsp") %>
				<%=Bean.getResetButton() %>
				<%=Bean.getGoBackButton("../crm/clients/yurpersons.jsp") %>
			</td>
		</tr>

	</table>

	</form>
		<%= Bean.getCalendarScript("club_date_beg", false) %>
		<%= Bean.getCalendarScript("club_date_end", false) %>
	<% } %>

 <% } else if (Bean.currentMenu.isCurrentTabAndPreviewPermitted("CLIENTS_YURPERSONS_INFO")) { %>
	    <form>
 	<table <%=Bean.getTableDetailParam() %>>
	<% if (isPartner) { %>
		<tr>
			<td>
				<%= Bean.jurpersonXML.getfieldTransl("jur_prs_kind", false) %></td> <td><input type="text" name="cd_jur_prs_kind" size="29" value="<%= Bean.getJurPersonKindName(jurprs.getValue("CD_JUR_PRS_KIND")) %>" readonly="readonly" class="inputfield-ro">
			</td>
			<td><%= Bean.jurpersonXML.getfieldTransl("reg_code_country", false) %></td> <td><input type="text" name="reg_code_country" size="30" value="<%= Bean.getCountryName(jurprs.getValue("reg_code_country")) %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td>&nbsp;</td>
			<td>
				<%=Bean.getCheckBoxDisabled("is_resident", jurprs.getValue("IS_RESIDENT"), Bean.jurpersonXML.getfieldTransl("is_resident", false)) %>
			</td>
			<td><%= Bean.jurpersonXML.getfieldTransl("inn_number", false) %></td> <td><input type="text" name="inn_number" size="30" value="<%= jurprs.getValue("INN_NUMBER") %>" readonly="readonly" class="inputfield-ro"> </td>
		</tr>
		<tr>
			<td><%= Bean.jurpersonXML.getfieldTransl("NAME_JUR_PRS", false) %></td> <td><input type="text" name="NAME_JUR_PRS" size="45" value="<%= jurprs.getValue("NAME_JUR_PRS") %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.jurpersonXML.getfieldTransl("kpp_number", false) %></td> <td><input type="text" name="kpp_number" size="30" value="<%= jurprs.getValue("KPP_NUMBER") %>" readonly="readonly" class="inputfield-ro"> </td>
		</tr>
		<tr>
			<td><%= Bean.jurpersonXML.getfieldTransl("SNAME_JUR_PRS", false) %></td> <td><input type="text" name="SNAME_JUR_PRS" size="45" value="<%= jurprs.getValue("SNAME_JUR_PRS") %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.jurpersonXML.getfieldTransl("ogrn_number", false) %></td> <td><input type="text" name="ogrn_number" size="30" value="<%= jurprs.getValue("OGRN_NUMBER") %>" readonly="readonly" class="inputfield-ro"> </td>
		</tr>
		<tr>
			<td><%= Bean.jurpersonXML.getfieldTransl("id_jur_prs_parent", false) %>
				<%=Bean.getGoToJurPrsHyperLink(jurprs.getValue("ID_JUR_PRS_PARENT")) %>
			</td> <td><input type="text" name="id_jur_prs_parent" size="45" value="<%= Bean.getJurPersonShortName(jurprs.getValue("ID_JUR_PRS_PARENT")) %>" readonly="readonly" class="inputfield-ro"> </td>
			<td><%= Bean.jurpersonXML.getfieldTransl("mfo_bank", false) %></td> <td><input type="text" name="mfo_bank" size="30" value="<%= jurprs.getValue("MFO_BANK") %>" readonly="readonly" class="inputfield-ro"> </td>
		</tr>
		<tr>
			<td colspan="2">&nbsp;</td>
			<td><%= Bean.jurpersonXML.getfieldTransl("tax_percent", false) %></td> <td><input type="text" name="tax_percent" size="10" value="<%= jurprs.getValue("TAX_PERCENT") %>" readonly="readonly" class="inputfield-ro"> </td>
		</tr>
		<tr>							
			<td colspan="4" class="top_line"><b><%= Bean.jurpersonXML.getfieldTransl("h_club_registration", false) %></b></td>
		</tr>
		<tr>								
			<td><%= Bean.clubXML.getfieldTransl("club", false) %>
				<%= Bean.getGoToClubLink(jurprs.getValue("ID_CLUB")) %>
			</td> 
			<td> <input type="text" name="name_club" size="30" value="<%= Bean.getClubShortName(jurprs.getValue("ID_CLUB")) %>" readonly="readonly" class="inputfield-ro"> </td>
			<td><%=Bean.getCheckBoxBase("is_shareholder", jurprs.getValue("IS_SHAREHOLDER"), Bean.jurpersonXML.getfieldTransl("is_shareholder", false), "color:green;", false, true) %></td>
			<td><%=Bean.getCheckBoxBase("is_dealer", jurprs.getValue("IS_DEALER"), Bean.jurpersonXML.getfieldTransl("is_dealer", false), "color:blue;", false, true) %></td>
		</tr>
		<tr>
			<td><%= Bean.jurpersonXML.getfieldTransl("cd_club_jur_prs", false) %></td> <td><input type="text" name="cd_club_jur_prs" size="30" value="<%= jurprs.getValue("CD_CLUB_JUR_PRS") %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%=Bean.getCheckBoxBase("is_registrator", jurprs.getValue("IS_REGISTRATOR"), Bean.jurpersonXML.getfieldTransl("is_registrator", false), "color:green;", false, true) %></td>
			<td><%=Bean.getCheckBoxDisabled("is_bank", jurprs.getValue("IS_BANK"), Bean.jurpersonXML.getfieldTransl("is_bank", false)) %></td>
		</tr>
		<tr>
			<td><%= Bean.jurpersonXML.getfieldTransl("club_date_beg", false) %></td><td><input type="text" name="club_date_beg" size="10" value="<%= jurprs.getValue("DATE_BEG_FRMT") %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%=Bean.getCheckBoxBase("is_coordinator", jurprs.getValue("IS_COORDINATOR"), Bean.jurpersonXML.getfieldTransl("is_coordinator", false), "color:green;", false, true) %></td>
			<td><%=Bean.getCheckBoxDisabled("is_issuer", jurprs.getValue("IS_ISSUER"), Bean.jurpersonXML.getfieldTransl("is_issuer", false)) %></td>
		</tr>
		<tr>
			<td><%= Bean.jurpersonXML.getfieldTransl("club_date_end", false) %></td><td><input type="text" name="club_date_end" size="10" value="<%= jurprs.getValue("DATE_END_FRMT") %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%=Bean.getCheckBoxBase("is_curator", jurprs.getValue("IS_CURATOR"), Bean.jurpersonXML.getfieldTransl("is_curator", false), "color:green;", false, true) %></td>
			<td><%=Bean.getCheckBoxDisabled("is_finance_acquirer", jurprs.getValue("IS_FINANCE_ACQUIRER"), Bean.jurpersonXML.getfieldTransl("is_finance_acquirer", false)) %></td>
		</tr>
		<tr>
			<td><%= Bean.clubXML.getfieldTransl("club_member_status", false) %></td><td><input type="text" name="cd_club_member_status" size="30" value="<%= jurprs.getValue("NAME_CLUB_MEMBER_STATUS") %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%=Bean.getCheckBoxBase("is_operator", jurprs.getValue("IS_OPERATOR"), Bean.jurpersonXML.getfieldTransl("is_operator", false), "color:red;", false, true) %></td>
			<td><%=Bean.getCheckBoxDisabled("is_technical_acquirer", jurprs.getValue("IS_TECHNICAL_ACQUIRER"), Bean.jurpersonXML.getfieldTransl("is_technical_acquirer", false)) %></td>
		</tr>
		<tr>
			<td colspan="2">&nbsp;</td>
			<td><%=Bean.getCheckBoxBase("is_agent", jurprs.getValue("IS_AGENT"), Bean.jurpersonXML.getfieldTransl("is_agent", false), "color:red;", false, true) %></td>
			<td><%=Bean.getCheckBoxDisabled("is_terminal_manufacturer", jurprs.getValue("IS_TERMINAL_MANUFACTURER"), Bean.jurpersonXML.getfieldTransl("is_terminal_manufacturer", false)) %></td>
		</tr>
		<tr>								
			<td colspan=6 class="top_line"><b><%= Bean.jurpersonXML.getfieldTransl("jur_adr_full", false) %></b></td>
		</tr>
		<tr>								
			<td><%= Bean.jurpersonXML.getfieldTransl("name_country", false) %></td><td><input type="text" name="JUR_ADR_CODE_COUNTRY" id="JUR_ADR_CODE_COUNTRY" size="30" value="<%=jurprs.getValue("JUR_ADR_NAME_COUNTRY") %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.jurpersonXML.getfieldTransl("ADR_CITY", false) %></td><td><input type="text" name="JUR_ADR_CITY" id="JUR_ADR_CITY" size="30" value="<%=jurprs.getValue("JUR_ADR_CITY") %>" readonly="readonly" class="inputfield-ro"></td>				
		</tr>
		<tr>
			<td><%= Bean.jurpersonXML.getfieldTransl("ADR_ZIP_CODE", false) %></td><td><input type="text" name="JUR_ADR_ZIP_CODE" id="JUR_ADR_ZIP_CODE" size="30" value="<%=jurprs.getValue("JUR_ADR_ZIP_CODE") %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.jurpersonXML.getfieldTransl("ADR_CITY_DISTRICT", false) %></td><td><input type="text" name="JUR_ADR_CITY_DISTRICT" id="JUR_ADR_CITY_DISTRICT" size="30" value="<%=jurprs.getValue("JUR_ADR_CITY_DISTRICT") %>" readonly="readonly" class="inputfield-ro"></td>				
		</tr>
		<tr>
			<td><%= Bean.jurpersonXML.getfieldTransl("ADR_OBLAST", false) %></td><td><input type="text" name="JUR_ADR_ID_OBLAST" id="JUR_ADR_ID_OBLAST" size="30" value="<%=jurprs.getValue("JUR_ADR_NAME_OBLAST") %>" readonly="readonly" class="inputfield-ro"></td>				
			<td><%= Bean.jurpersonXML.getfieldTransl("ADR_STREET", false) %></td><td><input type="text" name="JUR_ADR_STREET" id="JUR_ADR_STREET" size="30" value="<%=jurprs.getValue("JUR_ADR_STREET") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td><%= Bean.jurpersonXML.getfieldTransl("ADR_DISTRICT", false) %></td><td><input type="text" name="JUR_ADR_DISTRICT" id="JUR_ADR_DISTRICT" size="30" value="<%=jurprs.getValue("JUR_ADR_DISTRICT") %>" readonly="readonly" class="inputfield-ro"></td>				
			<td><%= Bean.jurpersonXML.getfieldTransl("ADR_HOUSE_CASE_APARTMENT", false) %></td><td><input type="text" name="JUR_ADR_HOUSE" id="JUR_ADR_HOUSE" size="6" value="<%=jurprs.getValue("JUR_ADR_HOUSE") %>" readonly="readonly" class="inputfield-ro">/<input type="text" name="JUR_ADR_CASE" id="JUR_ADR_CASE" size="6" value="<%=jurprs.getValue("JUR_ADR_CASE") %>" readonly="readonly" class="inputfield-ro">/<input type="text" name="JUR_ADR_APARTMENT" id="JUR_ADR_APARTMENT" size="6" value="<%=jurprs.getValue("JUR_ADR_APARTMENT") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>								
			<td colspan=6 class="top_line"><b><%= Bean.jurpersonXML.getfieldTransl("fact_adr_full", false) %></b></td>
		</tr>
		<tr>								
			<td><%= Bean.jurpersonXML.getfieldTransl("name_country", false) %></td><td><input type="text" name="FACT_ADR_CODE_COUNTRY" id="FACT_ADR_CODE_COUNTRY" size="30" value="<%=jurprs.getValue("FACT_ADR_NAME_COUNTRY") %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.jurpersonXML.getfieldTransl("ADR_CITY", false) %></td><td><input type="text" name="FACT_ADR_CITY" id="FACT_ADR_CITY" size="30" value="<%=jurprs.getValue("FACT_ADR_CITY") %>" readonly="readonly" class="inputfield-ro"></td>				
		</tr>
		<tr>
			<td><%= Bean.jurpersonXML.getfieldTransl("ADR_ZIP_CODE", false) %></td><td><input type="text" name="FACT_ADR_ZIP_CODE" id="FACT_ADR_ZIP_CODE" size="30" value="<%=jurprs.getValue("FACT_ADR_ZIP_CODE") %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.jurpersonXML.getfieldTransl("ADR_CITY_DISTRICT", false) %></td><td><input type="text" name="FACT_ADR_CITY_DISTRICT" id="FACT_ADR_CITY_DISTRICT" size="30" value="<%=jurprs.getValue("FACT_ADR_CITY_DISTRICT") %>" readonly="readonly" class="inputfield-ro"></td>				
		</tr>
		<tr>
			<td><%= Bean.jurpersonXML.getfieldTransl("ADR_OBLAST", false) %></td><td><input type="text" name="FACT_ADR_ID_OBLAST" id="FACT_ADR_ID_OBLAST" size="30" value="<%=jurprs.getValue("FACT_ADR_NAME_OBLAST") %>" readonly="readonly" class="inputfield-ro"></td>				
			<td><%= Bean.jurpersonXML.getfieldTransl("ADR_STREET", false) %></td><td><input type="text" name="FACT_ADR_STREET" id="FACT_ADR_STREET" size="30" value="<%=jurprs.getValue("FACT_ADR_STREET") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td><%= Bean.jurpersonXML.getfieldTransl("ADR_DISTRICT", false) %></td><td><input type="text" name="FACT_ADR_DISTRICT" id="FACT_ADR_DISTRICT" size="30" value="<%=jurprs.getValue("FACT_ADR_DISTRICT") %>" readonly="readonly" class="inputfield-ro"></td>				
			<td><%= Bean.jurpersonXML.getfieldTransl("ADR_HOUSE_CASE_APARTMENT", false) %></td><td><input type="text" name="FACT_ADR_HOUSE" id="FACT_ADR_HOUSE" size="6" value="<%=jurprs.getValue("FACT_ADR_HOUSE") %>" readonly="readonly" class="inputfield-ro">/<input type="text" name="FACT_ADR_CASE" id="FACT_ADR_CASE" size="6" value="<%=jurprs.getValue("FACT_ADR_CASE") %>" readonly="readonly" class="inputfield-ro">/<input type="text" name="FACT_ADR_APARTMENT" id="FACT_ADR_APARTMENT" size="6" value="<%=jurprs.getValue("FACT_ADR_APARTMENT") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>								
			<td colspan="4" class="top_line"><b><%= Bean.jurpersonXML.getfieldTransl("title_contact_info", false) %></b></td>
		</tr>
		<tr>								
			<td><%= Bean.jurpersonXML.getfieldTransl("phone_work", false) %></td><td><input type="text" name="phone_work" id="phone_work" size="30" value="<%=jurprs.getValue("PHONE_WORK") %>"  class="inputfield-ro"></td>
			<td><%= Bean.jurpersonXML.getfieldTransl("web_site", false) %></td><td><input type="text" name="web_site" id="web_site" size="30" value="<%=jurprs.getValue("WEB_SITE") %>"  class="inputfield-ro"></td>
		</tr>
		<tr>
			<td><%= Bean.jurpersonXML.getfieldTransl("fax", false) %></td><td><input type="text" name="fax" id="fax" size="30" value="<%=jurprs.getValue("FAX") %>"  class="inputfield-ro"></td>
			<td><%= Bean.jurpersonXML.getfieldTransl("email", false) %></td><td><input type="text" name="email" id="email" size="30" value="<%=jurprs.getValue("EMAIL") %>"  class="inputfield-ro"></td>
		</tr>
		<tr>								
			<td colspan="4" class="top_line"><b><%= Bean.jurpersonXML.getfieldTransl("title_others", false) %></b></td>
		</tr>
		<tr>								
			<td><%= Bean.jurpersonXML.getfieldTransl("name_jur_prs_in_sms", false) %></td><td><input type="text" name="name_jur_prs_in_sms" id="name_jur_prs_in_sms" size="45" value="<%=jurprs.getValue("NAME_JUR_PRS_IN_SMS") %>" readonly="readonly" class="inputfield-ro"></td>
			<td colspan="2">&nbsp;</td>
		</tr>
	<% } else { %>

		<tr>
			<td><%= Bean.jurpersonXML.getfieldTransl("name_jur_prs_form", false) %></td> <td><input type="text" name="name_jur_prs_form" size="45" value="<%=jurprs.getValue("NAME_JUR_PRS_FORM") %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.clubXML.getfieldTransl("club", false) %>
				<%= Bean.getGoToClubLink(jurprs.getValue("ID_CLUB")) %>
			</td> 
			<td>
				<input type="hidden" name="id_club" value="<%=jurprs.getValue("ID_CLUB") %>">
				<input type="text" name="name_club" size="35" value="<%=Bean.getClubShortName(jurprs.getValue("ID_CLUB")) %>" readonly="readonly" class="inputfield-ro">
			</td>
		</tr>
		<tr>
			<td>&nbsp;</td>
			<td>
				<%=Bean.getCheckBoxDisabled("is_resident", jurprs.getValue("IS_RESIDENT"), Bean.jurpersonXML.getfieldTransl("is_resident", false)) %>
			</td>
			<td><%= Bean.jurpersonXML.getfieldTransl("cd_club_service_place", false) %></td> <td><input type="text" name="cd_club_service_place" size="20" value="<%=jurprs.getValue("CD_CLUB_JUR_PRS") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td><%= Bean.jurpersonXML.getfieldTransl("name_service_place", false) %></td> <td><input type="text" name="name_service_place" size="45" value="<%=jurprs.getValue("NAME_JUR_PRS") %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.jurpersonXML.getfieldTransl("club_date_beg", false) %></td> <td><input type="text" name="club_date_beg" size="20" value="<%=jurprs.getValue("DATE_BEG_DF") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td><%= Bean.jurpersonXML.getfieldTransl("sname_service_place", false) %></td> <td><input type="text" name="sname_service_place" size="45" value="<%=jurprs.getValue("SNAME_JUR_PRS") %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.jurpersonXML.getfieldTransl("club_date_end", false) %></td> <td><input type="text" name="club_date_end" size="20" value="<%=jurprs.getValue("DATE_END_DF") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td><%= Bean.jurpersonXML.getfieldTransl("title_partner", false) %>
				<%=Bean.getGoToJurPrsHyperLink(jurprs.getValue("ID_JUR_PRS_PARENT")) %>
			</td> <td><input type="text" name="name_jur_prs" size="45" value="<%=jurprs.getValue("SNAME_JUR_PRS_PARENT") %>" readonly="readonly" class="inputfield-ro"> </td>
			<td><%= Bean.clubXML.getfieldTransl("club_member_status", false) %></td><td><input type="text" name="name_club_member_status" size="20" value="<%=jurprs.getValue("NAME_CLUB_MEMBER_STATUS") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>								
			<td colspan=8 class="top_line"><b><%= Bean.jurpersonXML.getfieldTransl("fact_adr_full", false) %></b></td>
		</tr>
		<tr>								
			<td><%= Bean.jurpersonXML.getfieldTransl("name_country", false) %></td><td><input type="text" name="FACT_ADR_CODE_COUNTRY" id="FACT_ADR_CODE_COUNTRY" size="35" value="<%=jurprs.getValue("FACT_ADR_NAME_COUNTRY") %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.jurpersonXML.getfieldTransl("ADR_CITY", false) %></td><td><input type="text" name="FACT_ADR_CITY" id="FACT_ADR_CITY" size="35" value="<%=jurprs.getValue("FACT_ADR_CITY") %>" readonly="readonly" class="inputfield-ro"></td>				
		</tr>
		<tr>
			<td><%= Bean.jurpersonXML.getfieldTransl("ADR_ZIP_CODE", false) %></td><td><input type="text" name="FACT_ADR_ZIP_CODE" id="FACT_ADR_ZIP_CODE" size="35" value="<%=jurprs.getValue("FACT_ADR_ZIP_CODE") %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.jurpersonXML.getfieldTransl("ADR_CITY_DISTRICT", false) %></td><td><input type="text" name="FACT_ADR_CITY_DISTRICT" id="FACT_ADR_CITY_DISTRICT" size="35" value="<%=jurprs.getValue("FACT_ADR_CITY_DISTRICT") %>" readonly="readonly" class="inputfield-ro"></td>				
		</tr>
		<tr>
			<td><%= Bean.jurpersonXML.getfieldTransl("ADR_OBLAST", false) %></td><td><input type="text" name="FACT_ADR_ID_OBLAST" id="FACT_ADR_ID_OBLAST" size="35" value="<%=jurprs.getValue("FACT_ADR_NAME_OBLAST") %>" readonly="readonly" class="inputfield-ro"></td>				
			<td><%= Bean.jurpersonXML.getfieldTransl("ADR_STREET", false) %></td><td><input type="text" name="FACT_ADR_STREET" id="FACT_ADR_STREET" size="35" value="<%=jurprs.getValue("FACT_ADR_STREET") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td><%= Bean.jurpersonXML.getfieldTransl("ADR_DISTRICT", false) %></td><td><input type="text" name="FACT_ADR_DISTRICT" id="FACT_ADR_DISTRICT" size="35" value="<%=jurprs.getValue("FACT_ADR_DISTRICT") %>" readonly="readonly" class="inputfield-ro"></td>				
			<td><%= Bean.jurpersonXML.getfieldTransl("ADR_HOUSE_CASE_APARTMENT", false) %></td><td><input type="text" name="FACT_ADR_HOUSE" id="FACT_ADR_HOUSE" size="6" value="<%=jurprs.getValue("FACT_ADR_HOUSE") %>" readonly="readonly" class="inputfield-ro">/<input type="text" name="FACT_ADR_CASE" id="FACT_ADR_CASE" size="6" value="<%=jurprs.getValue("FACT_ADR_CASE") %>" readonly="readonly" class="inputfield-ro">/<input type="text" name="FACT_ADR_APARTMENT" id="FACT_ADR_APARTMENT" size="6" value="<%=jurprs.getValue("FACT_ADR_APARTMENT") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>								
			<td colspan="4" class="top_line"><b><%= Bean.jurpersonXML.getfieldTransl("title_contact_info", false) %></b></td>
		</tr>
		<tr>								
			<td><%= Bean.jurpersonXML.getfieldTransl("phone_work", false) %></td><td><input type="text" name="phone_work" id="phone_work" size="35" value="<%=jurprs.getValue("PHONE_WORK") %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.jurpersonXML.getfieldTransl("web_site", false) %></td><td><input type="text" name="web_site" id="web_site" size="35" value="<%=jurprs.getValue("WEB_SITE") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td><%= Bean.jurpersonXML.getfieldTransl("fax", false) %></td><td><input type="text" name="fax" id="fax" size="35" value="<%=jurprs.getValue("FAX") %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.jurpersonXML.getfieldTransl("email", false) %></td><td><input type="text" name="email" id="email" size="35" value="<%=jurprs.getValue("EMAIL") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>								
			<td colspan="4" class="top_line"><b><%= Bean.jurpersonXML.getfieldTransl("title_others", false) %></b></td>
		</tr>
		<tr>								
			<td><%= Bean.jurpersonXML.getfieldTransl("name_service_place_in_sms", false) %></td><td><input type="text" name="name_jur_prs_in_sms" id="name_jur_prs_in_sms" size="45" value="<%=jurprs.getValue("NAME_JUR_PRS_IN_SMS") %>" readonly="readonly" class="inputfield-ro"></td>
			<td colspan="2">&nbsp;</td>
		</tr>
	<% } %>
		<%=	Bean.getIdCreationAndMoficationRecordFields(
				jurprs.getValue("ID_JUR_PRS"),
				jurprs.getValue(Bean.getCreationDateFieldName()),
				jurprs.getValue("CREATED_BY"),
				jurprs.getValue(Bean.getLastUpdateDateFieldName()),
				jurprs.getValue("LAST_UPDATE_BY")
			) %>
		<tr>
			<td colspan="8" align="center">
				<%=Bean.getGoBackButton("../crm/clients/yurpersons.jsp") %>
			</td>
		</tr>
	</table>

	</form> 

 <% }

if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLIENTS_YURPERSONS_ISSUER")) { 
	if ("Y".equalsIgnoreCase(jurprs.getValue("IS_ISSUER"))) {
		bcIssuerObject issuer = new bcIssuerObject(jurprs.getValue("ID_JUR_PRS"));%>
 
	<table <%= Bean.getTableBottomFilter() %>>
		<tr>
		<%= Bean.getFindHTML("issuer_find", issuer_find, "../crm/clients/yurpersonspecs.jsp?id=" + yurpersonid + "&issuer_page=1") %>

		<td>&nbsp;</td>
		</tr>
	</table>
		<%= issuer.getIssuerParametersHTML(issuer_find, "CLIENTS_YURPERSONS_ISSUER", "../crm/clients/yurpersonupdate.jsp", l_issuer_page_beg, l_issuer_page_end) %>
<%  } 
}

if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLIENTS_YURPERSONS_CONTACTS")) {
%>	<table <%= Bean.getTableBottomFilter() %>>
		<tr>
		<%= Bean.getFindHTML("contact_find", contact_find, "../crm/clients/yurpersonspecs.jsp?id=" + yurpersonid + "&contact_page=1") %>

		<%=Bean.getSelectOnChangeBeginHTML("contact_post", "../crm/clients/yurpersonspecs.jsp?id=" + yurpersonid + "&contact_page=1", Bean.contactXML.getfieldTransl("name_post", false)) %>
			<%= Bean.getContactPrsTypeOptions(contact_post, true) %>
		<%=Bean.getSelectOnChangeEndHTML() %>
		</tr>
	</table>
 
	<%= jurprs.getContactListHTML(contact_find, contact_post, l_contact_page_beg, l_contact_page_end, "CLIENTS_YURPERSONS_CONTACTS", "../crm/clients/contact_prsupdate.jsp?back_type=PARTNER&id=" + jurprs.getValue("ID_JUR_PRS")) %> 
<%}

if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLIENTS_YURPERSONS_DOCUMENTS")) {
%> 
	<table <%= Bean.getTableBottomFilter() %>>
		<tr>
		<%= Bean.getFindHTML("doc_find", doc_find, "../crm/clients/yurpersonspecs.jsp?id=" + yurpersonid + "&doc_page=1") %>

		<%=Bean.getSelectOnChangeBeginHTML("doc_type", "../crm/clients/yurpersonspecs.jsp?id=" + yurpersonid + "&doc_page=1", Bean.documentXML.getfieldTransl("name_doc_type", false)) %>
			<%= Bean.getDocTypeOptions(doc_type, true) %>
		<%=Bean.getSelectOnChangeEndHTML() %>

		<%=Bean.getSelectOnChangeBeginHTML("club_rel_type", "../crm/clients/yurpersonspecs.jsp?id=" + yurpersonid + "&doc_page=1", Bean.documentXML.getfieldTransl("club_rel_type", false)) %>
			<%= Bean.getClubRelTypeOptions(club_rel_type, true) %>
		<%=Bean.getSelectOnChangeEndHTML() %>

		<%=Bean.getSelectOnChangeBeginHTML("doc_state", "../crm/clients/yurpersonspecs.jsp?id=" + yurpersonid + "&doc_page=1", Bean.documentXML.getfieldTransl("name_doc_state", false)) %>
			<%= Bean.getDocStateOptions(doc_state, true) %>
		<%=Bean.getSelectOnChangeEndHTML() %>

		</tr>
	</table>
	<%= jurprs.getDocumentsListHTML(doc_find, doc_type, doc_state, club_rel_type, l_doc_page_beg, l_doc_page_end, "CLIENTS_YURPERSONS_DOCUMENTS", "../crm/clients/yurpersondocupdate.jsp?id=" + jurprs.getValue("ID_JUR_PRS")) %> 
<%}

if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLIENTS_YURPERSONS_SERVICEPLACE")) {%>
	<table <%= Bean.getTableBottomFilter() %>>
		<tr>
		<%= Bean.getFindHTML("submission_find", submission_find, "../crm/clients/yurpersonspecs.jsp?id=" + yurpersonid + "&submission_page=1") %>

		<%=Bean.getSelectOnChangeBeginHTML("submission_form", "../crm/clients/yurpersonspecs.jsp?id=" + yurpersonid + "&submission_page=1", Bean.jurpersonXML.getfieldTransl("name_jur_prs_form", false)) %>
			<%= Bean.getJurPrsFormOptions(submission_form, true) %>
		<%=Bean.getSelectOnChangeEndHTML() %>

		</tr>
	</table>
	<%= jurprs.getJurPersonServicePlacesHTML(submission_find, submission_form, l_submission_page_beg, l_submission_page_end) %>
<%}
if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLIENTS_YURPERSONS_BANKACCNT")) {%>    
	<table <%= Bean.getTableBottomFilter() %>>
		<tr>
		<%= Bean.getFindHTML("bank_acc_find", bank_acc_find, "../crm/clients/yurpersonspecs.jsp?id=" + yurpersonid + "&bank_account_page=1") %>

		<%=Bean.getSelectOnChangeBeginHTML("bank_acc_type", "../crm/clients/yurpersonspecs.jsp?id=" + yurpersonid + "&bank_account_page=1", Bean.accountXML.getfieldTransl("name_bank_account_type", false)) %>
			<%= Bean.getBankAccountTypeOptions(bank_acc_type, true) %>
		<%=Bean.getSelectOnChangeEndHTML() %>

		</tr>
	</table>
	<%= jurprs.getBankAccountsHTML(bank_acc_find, bank_acc_type, l_bank_account_page_beg, l_bank_account_page_end) %>
<%}
if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLIENTS_YURPERSONS_TERM")) {%>
	<table <%=Bean.getTableBottomFilter() %>>
		<tr>
			<%= Bean.getFindHTML("terminal_find", terminal_find, "../crm/clients/yurpersonspecs.jsp?id=" + yurpersonid + "&terminals_page=1") %>

			<%=Bean.getSelectOnChangeBeginHTML("terminal_type", "../crm/clients/yurpersonspecs.jsp?id=" + yurpersonid + "&terminals_page=1", Bean.terminalXML.getfieldTransl("cd_term_type", false)) %>
				<%= Bean.getTermTypeOptions(terminal_type, true) %>
			<%=Bean.getSelectOnChangeEndHTML() %>

			<%=Bean.getSelectOnChangeBeginHTML("terminal_status", "../crm/clients/yurpersonspecs.jsp?id=" + yurpersonid + "&terminals_page=1", Bean.terminalXML.getfieldTransl("cd_term_status", false)) %>
				<%= Bean.getTermStatusOptions(terminal_status, true) %>
			<%=Bean.getSelectOnChangeEndHTML() %>

			<%=Bean.getSelectOnChangeBeginHTML("term_role", "../crm/clients/yurpersonspecs.jsp?id=" + jurprs.getValue("ID_JUR_PRS") + "&terminals_page=1", "") %>
				<%=Bean.getSelectOptionHTML(term_role, "JUR_PRS_PLACE", Bean.terminalXML.getfieldTransl("h_jur_prs_place", false)) %>
				<%=Bean.getSelectOptionHTML(term_role, "FINANCE_ACQUIRER", Bean.terminalXML.getfieldTransl("h_finance_acquirer", false)) %>
				<%=Bean.getSelectOptionHTML(term_role, "MANUFACTURER", Bean.terminalXML.getfieldTransl("h_term_manufacturer", false)) %>
				<%=Bean.getSelectOptionHTML(term_role, "TECHNICAL_ACQUIRER", Bean.terminalXML.getfieldTransl("h_technical_acquirer", false)) %>
				<%=Bean.getSelectOptionHTML(term_role, "OWNER", Bean.terminalXML.getfieldTransl("h_term_owner", false)) %>
			<%=Bean.getSelectOnChangeEndHTML() %>
		</tr>
	</table>

	<%= jurprs.getTerminalsHTML(terminal_find, terminal_type, terminal_status, term_role, l_terminals_page_beg, l_terminals_page_end) %>
<%}
if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLIENTS_YURPERSONS_TRANS")) {%>
<table <%=Bean.getTableBottomFilter() %>>
	<tr>
		<%= Bean.getFindHTML("trans_find", trans_find, "../crm/clients/yurpersonspecs.jsp?id=" + yurpersonid + "&trans_page=1") %>

		<%=Bean.getSelectOnChangeBeginHTML("trans_type", "../crm/clients/yurpersonspecs.jsp?id=" + yurpersonid + "&trans_page=1", Bean.transactionXML.getfieldTransl("type_trans", false)) %>
			<%= Bean.getTransTypeOptions(trans_type, true) %>
		<%=Bean.getSelectOnChangeEndHTML() %>
	
		<%=Bean.getSelectOnChangeBeginHTML("trans_pay_type", "../crm/clients/yurpersonspecs.jsp?id=" + yurpersonid + "&trans_page=1", Bean.transactionXML.getfieldTransl("pay_type", false)) %>
			<%= Bean.getTransPayTypeOptions(trans_pay_type, true) %>
		<%=Bean.getSelectOnChangeEndHTML() %>
	
		<%=Bean.getSelectOnChangeBeginHTML("trans_state", "../crm/clients/yurpersonspecs.jsp?id=" + yurpersonid + "&trans_page=1", Bean.transactionXML.getfieldTransl("state_trans", false)) %>
			<%= Bean.getTransStateOptions(trans_state, true) %>
		<%=Bean.getSelectOnChangeEndHTML() %>
	</tr>
</table>

<%= jurprs.getTransactionsHTML(trans_find, trans_type, trans_pay_type, trans_state, l_trans_page_beg, l_trans_page_end) %>
<%}

if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLIENTS_YURPERSONS_TERM_DEVICE_TYPES")) { 
	if ("Y".equalsIgnoreCase(jurprs.getValue("IS_TERMINAL_MANUFACTURER"))) {
	%>     
	<table <%= Bean.getTableBottomFilter() %>>
		<tr>
		<%= Bean.getFindHTML("device_find", device_find, "../crm/clients/yurpersonspecs.jsp?id=" + yurpersonid + "&device_page=1") %>

	
		<%=Bean.getSelectOnChangeBeginHTML("device_type", "../crm/clients/yurpersonspecs.jsp?id=" + yurpersonid + "&device_page=1", Bean.terminalXML.getfieldTransl("cd_term_type", false)) %>
			<%= Bean.getTermTypeOptions(device_type, true) %>
		<%=Bean.getSelectOnChangeEndHTML() %>
		</tr>
	</table>
		<%= jurprs.getDeviceTypesHTML(device_find, device_type, l_devices_page_beg, l_devices_page_end) %>
<%  } 
}


if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLIENTS_YURPERSONS_COMISSION")) {
%> 
	<table <%= Bean.getTableBottomFilter() %>>
		<tr>
		<%= Bean.getFindHTML("comis_find", comis_find, "../crm/clients/yurpersonspecs.jsp?id=" + yurpersonid + "&comis_page=1") %>
		<%
		String needComission = Bean.getJurPrsComissionNeedCount(yurpersonid);
		
		if (!(needComission.equalsIgnoreCase("0"))) {
			  %>
				<td align="right"><b><font color=red><%= Bean.comissionXML.getfieldTransl("need_comission_count", false) %> -  <%=needComission %></font></b><br></td>
			  <%
		} else {
			  %>
				<td align="right"><b><font color=green><%= Bean.comissionXML.getfieldTransl("all_comission_was_created", false) %></font></b><br></td>
			  <%
		}
		
		%>
		<%=Bean.getSelectOnChangeBeginHTML("comis_type", "../crm/clients/yurpersonspecs.jsp?id=" + yurpersonid + "&comis_page=1", Bean.comissionXML.getfieldTransl("name_comission_type", false)) %>
			<%= Bean.getComissionTypeOptions(comis_type, true) %>
		<%=Bean.getSelectOnChangeEndHTML() %>
		</tr>
	</table>

	<%= jurprs.getJurPersonComissionHTML(comis_find, comis_type, l_comis_page_beg, l_comis_page_end) %>
<%} 
if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLIENTS_YURPERSONS_BK_ACCOUNTS")) {
	%>
	<table <%=Bean.getTableBottomFilter() %>>
	  <tr>
		<%= Bean.getFindHTML("bk_acc_find", bk_acc_find, "../crm/clients/yurpersonspecs.jsp?id=" + yurpersonid + "&bk_acc_page=1&") %>

		<%=Bean.getSelectOnChangeBeginHTML("bk_acc_exist", "../crm/clients/yurpersonspecs.jsp?id=" + yurpersonid + "&bk_acc_page=1", Bean.bk_accountXML.getfieldTransl("exist_flag", false)) %>
			<%= Bean.getMeaningFromLookupNameOptions("YES_NO", bk_acc_exist, true) %>
		<%=Bean.getSelectOnChangeEndHTML() %>

	  </tr>
	</table>
	<%= jurprs.getJurPersonBKAccountsHTML(bk_acc_find, bk_acc_exist, jurprs.getValue("ID_JUR_PRS"),"'ISSUER','FINANCE_ACQUIRER','TECHNICAL_ACQUIRER', 'DEALER'", l_bk_acc_page_beg, l_bk_acc_page_end) %>
<%}

if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLIENTS_YURPERSONS_RELATIONSHIPS")) {%>
	<table <%= Bean.getTableBottomFilter() %>>
		<tr>
		<%= Bean.getFindHTML("relation_find", relation_find, "../crm/clients/yurpersonspecs.jsp?id=" + yurpersonid + "&relation_page=1") %>
		<%
		String needRelationships = jurprs.getRelationShipsNeededCount();
		
		  if (!(needRelationships.equalsIgnoreCase("0"))) {
			  %>
				<td align="right"><b><font color=red><%= Bean.relationshipXML.getfieldTransl("need_relationships_count", false) %> -  <%=needRelationships %></font></b><br></td>
			  <%
		  } else {
			  %>
				<td align="right"><b><font color=green><%= Bean.relationshipXML.getfieldTransl("all_relationships_was_created", false) %></font></b><br></td>
			  <%
		  }
		
		%>
		<%=Bean.getSelectOnChangeBeginHTML("rel_kind", "../crm/clients/yurpersonspecs.jsp?id=" + yurpersonid + "&relation_page=1", "") %>
			<%= Bean.getSelectOptionHTML(rel_kind, "CREATED", Bean.relationshipXML.getfieldTransl("h_created", false)) %>
			<%= Bean.getSelectOptionHTML(rel_kind, "NEEDED", Bean.relationshipXML.getfieldTransl("h_needed", false)) %>
		<%=Bean.getSelectOnChangeEndHTML() %>
		</tr>
	</table>


	<% if ("CREATED".equalsIgnoreCase(rel_kind)) { %>
		<%= jurprs.getRelationShipsHTML(relation_find, l_relation_page_beg, l_relation_page_end) %>
	<% } else { %>
		<%= jurprs.getRelationShipsNeededHTML(relation_find, l_relation_page_beg, l_relation_page_end) %>
	<% } %>
<%}

if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLIENTS_YURPERSONS_NOMENKLATURE")) {%>
	<table <%=Bean.getTableBottomFilter() %>>
	  <tr>
		<%= Bean.getFindHTML("nomenkl_find", nomenkl_find, "../crm/clients/yurpersonspecs.jsp?id=" + yurpersonid + "&nomenkl_page=1&") %>

		<td>&nbsp;</td>

	  </tr>
	</table>
	<%= jurprs.getJurPersonNomenklaturesHTML(nomenkl_find, l_nomenkl_page_beg, l_nomenkl_page_end) %>
<%}

if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLIENTS_YURPERSONS_LOGISTIC")) {%>
	<table <%=Bean.getTableBottomFilter() %>>
	  <tr>
		<%= Bean.getFindHTML("logistic_find", logistic_find, "../crm/clients/yurpersonspecs.jsp?id=" + yurpersonid + "&logistic_page=1&") %>

		<td>&nbsp;</td>

	  </tr>
	</table>
	<%= jurprs.getLogisticHTML(logistic_find, l_logistic_page_beg, l_logistic_page_end) %>
<%}

if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLIENTS_YURPERSONS_ACCESS")) {%>
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
		<%= Bean.getFindHTML("access_find", access_find, "../crm/clients/yurpersonspecs.jsp?id=" + yurpersonid + "&access_page=1&") %>

		<%=Bean.getSelectOnChangeBeginHTML("access_selected", "../crm/clients/yurpersonspecs.jsp?id=" + yurpersonid + "&access_selected=", Bean.jurpersonXML.getfieldTransl("has_permission", false)) %>
			<%=Bean.getSelectOptionHTML(access_selected, "", "") %>
			<%=Bean.getSelectOptionHTML(access_selected, "Y", Bean.commonXML.getfieldTransl("yes", false)) %>
			<%=Bean.getSelectOptionHTML(access_selected, "N", Bean.commonXML.getfieldTransl("no", false)) %>
		<%=Bean.getSelectOnChangeEndHTML() %>
		</tr>
	</table>

	<%= jurprs.getUserJurPrsPrivilegesHTML(access_find, access_selected, l_access_page_beg, l_access_page_end) %>
<%}

if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLIENTS_YURPERSONS_CARD_PACKAGE")) {%>
	<table <%=Bean.getTableBottomFilter() %>>
	  <tr>
		<%= Bean.getFindHTML("card_pack_find", card_pack_find, "../crm/clients/yurpersonspecs.jsp?id=" + yurpersonid + "&card_pack_page=1&") %>

		<%=Bean.getSelectOnChangeBeginHTML("card_pack_card_status", "../crm/clients/yurpersonspecs.jsp?id=" + yurpersonid + "&card_pack_page=1", Bean.clubcardXML.getfieldTransl("NAME_CARD_STATUS", false)) %>
			<%= Bean.getClubCardStatusOptions(card_pack_card_status, true) %>
		<%=Bean.getSelectOnChangeEndHTML() %>

	  </tr>
	</table>
	<%= jurprs.getCardPackagesHTML(card_pack_find, card_pack_card_status, l_card_pack_page_beg, l_card_pack_page_end) %>
<%}

if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLIENTS_YURPERSONS_REFERRAL_SCHEME")) {%>
	<table <%=Bean.getTableBottomFilter() %>>
	  <tr>
		<%= Bean.getFindHTML("referral_scheme_find", referral_scheme_find, "../crm/clients/yurpersonspecs.jsp?id=" + yurpersonid + "&referral_scheme_page=1&") %>

		<%=Bean.getSelectOnChangeBeginHTML("referral_scheme_type", "../crm/clients/yurpersonspecs.jsp?id=" + yurpersonid + "&referral_scheme_page=1", Bean.clubXML.getfieldTransl("name_referral_scheme_type", false)) %>
			<%= Bean.getReferralShemeTypeOptions(referral_scheme_type, true) %>
		<%=Bean.getSelectOnChangeEndHTML() %>

		<%=Bean.getSelectOnChangeBeginHTML("referral_scheme_calc_type", "../crm/clients/yurpersonspecs.jsp?id=" + yurpersonid + "&referral_scheme_page=1", Bean.clubXML.getfieldTransl("name_referral_scheme_calc_type", false)) %>
			<%= Bean.getReferralShemeCalcTypeOptions(referral_scheme_calc_type, true) %>
		<%=Bean.getSelectOnChangeEndHTML() %>

	  </tr>
	</table>
	<%= jurprs.getReferralSchemeHTML(referral_scheme_find, referral_scheme_type, referral_scheme_calc_type, l_referral_scheme_page_beg, l_referral_scheme_page_end) %>
<%}

if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLIENTS_YURPERSONS_LOYALITY_SCHEME")) {%>
	<table <%=Bean.getTableBottomFilter() %>>
	  <tr>
		<%= Bean.getFindHTML("loyality_find", loyality_find, "../crm/clients/yurpersonspecs.jsp?id=" + yurpersonid + "&loyality_page=1&") %>

		<%=Bean.getSelectOnChangeBeginHTML("loyality_kind", "../crm/clients/yurpersonspecs.jsp?page=1", Bean.loyXML.getfieldTransl("name_kind_loyality", false)) %>
			<%= Bean.getLoyalityKindOptions(loyality_kind, true) %>
		<%=Bean.getSelectOnChangeEndHTML() %>

	  </tr>
	</table>
	<%= jurprs.getLoyalitySchemeHTML(loyality_find, loyality_kind, l_loyality_page_beg, l_loyality_page_end) %>
<%}

if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLIENTS_YURPERSONS_TARGET_PROGRAMS")) {%>
	<table <%=Bean.getTableBottomFilter() %>>
	  <tr>
		<%= Bean.getFindHTML("target_program_find", target_program_find, "../crm/clients/yurpersonspecs.jsp?id=" + yurpersonid + "&target_program_page=1&") %>

		<%=Bean.getSelectOnChangeBeginHTML("target_program_pay_period", "../crm/club/yurpersonspecs.jsp?page=1&", Bean.clubfundXML.getfieldTransl("target_prg_pay_period", false)) %>
			<%= Bean.getTargetPrgPayPeriodOptions(target_program_pay_period, true) %>
		<%=Bean.getSelectOnChangeEndHTML() %>
	
	  </tr>
	</table>
	<%= jurprs.getTargetProgramsHTML(target_program_find, target_program_pay_period, l_target_program_page_beg, l_target_program_page_end) %>
<%}

if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLIENTS_YURPERSONS_ACCESS_ROLES")) {%>
	<table <%=Bean.getTableBottomFilter() %>>
	  <tr>
		<%= Bean.getFindHTML("role_find", role_find, "../crm/clients/yurpersonspecs.jsp?id=" + yurpersonid + "&role_page=1&") %>

		<%=Bean.getSelectOnChangeBeginHTML("role_module_type", "../crm/clients/yurpersonspecs.jsp?id=" + yurpersonid + "&role_page=1&", Bean.clubfundXML.getfieldTransl("target_prg_pay_period", false)) %>
			<%= Bean.getSysModuleTypeOptions(role_module_type, true) %>
		<%=Bean.getSelectOnChangeEndHTML() %>
	
	  </tr>
	</table>
	<%= jurprs.getSystemRolesHTML(role_find, role_module_type, l_role_page_beg, l_role_page_end) %>
<%}

} %>
</div></div>
</body>
</html>
