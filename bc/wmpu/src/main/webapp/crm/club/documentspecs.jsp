<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%@page import="bc.objects.bcReglamentObject"%>
<%@page import="bc.objects.bcDocumentObject"%>
<%@page import="java.net.URLEncoder"%>
<%@page import="java.util.HashMap"%>
<%@page import="bc.objects.bcLGObject"%><html>

<%= Bean.getLogOutScript(request) %>

<head>
	<%= Bean.getMetaContent() %>
	<%= Bean.getBottomFrameCSS() %>

</head>
<body topmargin="0">
<div id="div_tabsheet">
<%

HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String pageFormName = "CLUB_DOCUMENTS";

Bean.setJspPageForTabName(pageFormName);

String tagBankAccounts = "_BAND_ACCOUNTS";
String tagBankAccountFind = "_BANK_ACCOUNT_FIND";
String tagBankAccountType = "_BANK_ACCOUNT_TYPE";
String tagComission = "_COMISSION";
String tagComissionFind = "_COMISSION_FIND";
//String tagComisType = "_COMIS_TYPE";
String tagCardPack = "_CARD_PACK";
String tagCardPackFind = "_CARD_PACK_FIND";
String tagCardPackageCardStatus = "_CARD_PACKAGE_CARD_STATUS";
String tagReferralScheme = "_REFERRAL_SCHEME";
String tagReferralSchemeFind = "_REFERRAL_SCHEME_FIND";
String tagReferralSchemeType = "_REFERRAL_SCHEME_TYPE";
String tagReferralSchemeCalcType = "_REFERRAL_SCHEME_CALC_TYPE";
String tagTargetProgram = "_TARGET_PROGRAM";
String tagTargetProgramFind = "_TARGET_PROGRAM_FIND";
String tagTargetProgramPayPeriod = "_TARGET_PROGRAM_PAY_PERIOD";
String tagLoyality = "_LOYALITY";
String tagLoyalityFind = "_LOYALITY_FIND";
String tagLoyalityKind = "_LOYALITY_KIND";
String tagFile = "_FILE";
String tagFileFind = "_FILE_FIND";

String docid = Bean.getDecodeParam(parameters.get("id"));
String	tab = Bean.getDecodeParam(parameters.get("tab"));
if (tab==null || "".equalsIgnoreCase(tab)) { tab = Bean.tabsHmGetValue(pageFormName); }
if (docid==null || "".equalsIgnoreCase(docid)) {
%>
	<%=Bean.getIDNotFoundMessage() %>
<% 
} else {
	Bean.tabsHmSetValue(pageFormName, tab);
	bcDocumentObject doc = new bcDocumentObject(docid);
	
	String l_bank_account_page = Bean.getDecodeParam(parameters.get("bank_account_page"));
	Bean.pageCheck(pageFormName + tagBankAccounts, l_bank_account_page);
	String l_bank_account_page_beg = Bean.getFirstRowNumber(pageFormName + tagBankAccounts);
	String l_bank_account_page_end = Bean.getLastRowNumber(pageFormName + tagBankAccounts);

	String bank_acc_find 	= Bean.getDecodeParam(parameters.get("bank_acc_find"));
	bank_acc_find 	= Bean.checkFindString(pageFormName + tagBankAccountFind, bank_acc_find, l_bank_account_page);

	String bank_acc_type 	= Bean.getDecodeParam(parameters.get("bank_acc_type"));
	bank_acc_type 	= Bean.checkFindString(pageFormName + tagBankAccountType, bank_acc_type, l_bank_account_page);
	
	String l_comis_page = Bean.getDecodeParam(parameters.get("comis_page"));
	Bean.pageCheck(pageFormName + tagComission, l_comis_page);
	String l_comis_page_beg = Bean.getFirstRowNumber(pageFormName + tagComission);
	String l_comis_page_end = Bean.getLastRowNumber(pageFormName + tagComission);
	

	/*String comis_type 		= Bean.getDecodeParam(parameters.get("comis_type"));
	if (comis_type==null || "".equalsIgnoreCase(comis_type)) {
		comis_type = "DOCUMENT";
	}
	comis_type = Bean.checkFindString(pageFormName + tagComisType, comis_type, "");*/
	
	String comis_find 	= Bean.getDecodeParam(parameters.get("comis_find"));
	comis_find 	= Bean.checkFindString(pageFormName + tagComissionFind, comis_find, l_comis_page);
	
	String l_card_pack_page = Bean.getDecodeParam(parameters.get("card_pack_page"));
	Bean.pageCheck(pageFormName + tagCardPack, l_card_pack_page);
	String l_card_pack_page_beg = Bean.getFirstRowNumber(pageFormName + tagCardPack);
	String l_card_pack_page_end = Bean.getLastRowNumber(pageFormName + tagCardPack);
	
	String card_pack_find 	= Bean.getDecodeParam(parameters.get("card_pack_find"));
	card_pack_find 	= Bean.checkFindString(pageFormName + tagCardPackFind, card_pack_find, l_card_pack_page);

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
	
	String l_target_program_page = Bean.getDecodeParam(parameters.get("target_program_page"));
	Bean.pageCheck(pageFormName + tagTargetProgram, l_target_program_page);
	String l_target_program_page_beg = Bean.getFirstRowNumber(pageFormName + tagTargetProgram);
	String l_target_program_page_end = Bean.getLastRowNumber(pageFormName + tagTargetProgram);

	String target_program_find 	= Bean.getDecodeParam(parameters.get("target_program_find"));
	target_program_find 	= Bean.checkFindString(pageFormName + tagTargetProgramFind, target_program_find, l_target_program_page);

	String target_program_pay_period 	= Bean.getDecodeParam(parameters.get("target_program_pay_period"));
	target_program_pay_period 	= Bean.checkFindString(pageFormName + tagTargetProgramPayPeriod, target_program_pay_period, l_target_program_page);
	
	String l_loyality_page = Bean.getDecodeParam(parameters.get("loyality_page"));
	Bean.pageCheck(pageFormName + tagLoyality, l_loyality_page);
	String l_loyality_page_beg = Bean.getFirstRowNumber(pageFormName + tagLoyality);
	String l_loyality_page_end = Bean.getLastRowNumber(pageFormName + tagLoyality);

	String loyality_find 	= Bean.getDecodeParam(parameters.get("loyality_find"));
	loyality_find 	= Bean.checkFindString(pageFormName + tagLoyalityFind, loyality_find, l_loyality_page);

	String loyality_kind 	= Bean.getDecodeParam(parameters.get("loyality_kind"));
	loyality_kind 	= Bean.checkFindString(pageFormName + tagLoyalityKind, loyality_kind, l_loyality_page);

	String l_file_page = Bean.getDecodeParam(parameters.get("file_page"));
	Bean.pageCheck(pageFormName + tagFile, l_file_page);
	String l_file_page_beg = Bean.getFirstRowNumber(pageFormName + tagFile);
	String l_file_page_end = Bean.getLastRowNumber(pageFormName + tagFile);

	String file_find 	= Bean.getDecodeParam(parameters.get("file_find"));
	file_find 	= Bean.checkFindString(pageFormName + tagFileFind, file_find, l_file_page);
%>

	<table <%=Bean.getTableMenuParam() %>>
		<tr>
			<%= Bean.getPageHeader() %>

			<% if (Bean.currentMenu.isCurrentTabAndEditPermitted("CLUB_DOCUMENTS_INFO")) { %>
			    <%= Bean.getMenuButton("ADD", "../crm/club/documentupdate.jsp?id_doc=" + docid + "&type=general&action=add2&process=no", "", "") %>
			    <%= Bean.getMenuButton("DELETE", "../crm/club/documentupdate.jsp?id_doc=" + docid + "&type=general&action=remove&process=yes", Bean.documentXML.getfieldTransl("l_remove_doc", false), doc.getValue("NUMBER_DOC") + " - " + doc.getValue("DATE_DOC_FRMT")) %>
			<%  } %>
	
			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLUB_DOCUMENTS_BANK_ACCOUNTS")) { %>
				<% if (Bean.currentMenu.isCurrentTabAndEditPermitted("CLUB_DOCUMENTS_BANK_ACCOUNTS")) { %>
			    	<%= Bean.getMenuButtonBase("ADD", "../crm/club/documentupdate.jsp?type=account&id_doc="+docid+"&action=add&process=no", "", "", Bean.documentXML.getfieldTransl("h_add_doc_bank_account", false), "div_data_detail") %>
					<%= Bean.getMenuButtonBase("ADD_ALL", "../crm/club/documentupdate.jsp?type=account&id_doc="+docid+"&action=addall&process=no", "", "", Bean.documentXML.getfieldTransl("h_add_all_doc_bank_account", false), "div_data_detail") %>
				<% } %>
				<!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagBankAccounts, "../crm/club/documentspecs.jsp?id=" + doc.getValue("ID_DOC") + "&tab="+Bean.currentMenu.getTabID("CLUB_DOCUMENTS_BANK_ACCOUNTS")+"&", "bank_account_page") %>
			<% } %>
	
			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLUB_DOCUMENTS_COMISSIONS")) { %>
				<% if (Bean.currentMenu.isCurrentTabAndEditPermitted("CLUB_DOCUMENTS_COMISSIONS")) { %>
			    	<%= Bean.getMenuButtonBase("ADD", "../crm/club/doccomissionupdate.jsp?id=" + docid + "&type=comission&action=add&process=no", "", "", "", "div_data_detail") %>
				    <%= Bean.getMenuButton("ADD_ALL", "../crm/club/doccomissionupdate.jsp?type=comission&id="+docid+"&action=addall&process=yes", Bean.jurpersonXML.getfieldTransl("h_add_all_comissions", false), "") %>
				    <%= Bean.getMenuButton("DELETE_ALL", "../crm/club/doccomissionupdate.jsp?type=comission&id="+docid+"&action=removeall&process=yes", Bean.jurpersonXML.getfieldTransl("h_delete_all_comissions", false), "") %>
				<% } %>
			    <!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagComission, "../crm/club/documentspecs.jsp?id=" + doc.getValue("ID_DOC") + "&tab="+Bean.currentMenu.getTabID("CLUB_DOCUMENTS_COMISSIONS")+"&", "comis_page") %>
			<% } %>
	
			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLUB_DOCUMENTS_CARD_PACK")) { %>
				<% if (Bean.currentMenu.isCurrentTabAndEditPermitted("CLUB_DOCUMENTS_CARD_PACK")) { %>
				    <%= Bean.getMenuButtonBase("ADD", "../crm/club/card_packageupdate.jsp?back_type=DOC&id=" + doc.getValue("ID_DOC") + "&id_doc=" + doc.getValue("ID_DOC") + "&type=general&action=add&process=no", "", "", "", "div_data_detail") %>
				    <%= Bean.getMenuButtonBase("COPY", "../crm/club/card_packageupdate.jsp?back_type=DOC&id=" + doc.getValue("ID_DOC") + "&id_doc=" + doc.getValue("ID_DOC") + "&type=general&action=select&process=no", "", "", "", "div_data_detail") %>
				<%  } %>
				<!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagCardPack, "../crm/club/documentspecs.jsp?id=" + doc.getValue("ID_DOC") + "&tab="+Bean.currentMenu.getTabID("CLUB_DOCUMENTS_CARD_PACK")+"&", "card_pack_page") %>
			<% } %>
	
			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLUB_DOCUMENTS_REFERRAL_SCHEME")) { %>
				<% if (Bean.currentMenu.isCurrentTabAndEditPermitted("CLUB_DOCUMENTS_REFERRAL_SCHEME")) { %>
				    <%= Bean.getMenuButtonBase("ADD", "../crm/club/referral_schemeupdate.jsp?back_type=DOC&id=" + doc.getValue("ID_DOC") + "&type=general&action=add&process=no", "", "", "", "div_data_detail") %>
				    <%= Bean.getMenuButtonBase("COPY", "../crm/club/referral_schemeupdate.jsp?back_type=DOC&id=" + doc.getValue("ID_DOC") + "&type=general&action=select&process=no", "", "", "", "div_data_detail") %>
				<%  } %>
				<!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagReferralScheme, "../crm/club/documentspecs.jsp?id=" + doc.getValue("ID_DOC") + "&tab="+Bean.currentMenu.getTabID("CLUB_DOCUMENTS_REFERRAL_SCHEME")+"&", "referral_scheme_page") %>
			<% } %>
	
			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLUB_DOCUMENTS_TARGET_PROGRAMS")) { %>
			    <% if (Bean.currentMenu.isCurrentTabAndEditPermitted("CLUB_DOCUMENTS_TARGET_PROGRAMS")) { %>
				    <%= Bean.getMenuButtonBase("ADD", "../crm/club/target_programupdate.jsp?back_type=DOCUMENT&id=" + doc.getValue("ID_DOC") + "&type=general&action=add&process=no", "", "", "", "div_data_detail") %>
				    <%= Bean.getMenuButtonBase("COPY", "../crm/club/target_programupdate.jsp?back_type=DOCUMENT&id=" + doc.getValue("ID_DOC") + "&type=general&action=select&process=no", "", "", "", "div_data_detail") %>
				<%  } %>
				<!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagTargetProgram, "../crm/club/documentspecs.jsp?id=" + doc.getValue("ID_DOC") + "&tab="+Bean.currentMenu.getTabID("CLUB_DOCUMENTS_TARGET_PROGRAMS")+"&", "target_program_page") %>
			<% } %>
	
			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLUB_DOCUMENTS_LOYALITY_SCHEME")) { %>
			    <% if (Bean.currentMenu.isCurrentTabAndEditPermitted("CLUB_DOCUMENTS_LOYALITY_SCHEME")) { %>
				    <%= Bean.getMenuButtonBase("ADD", "../crm/clients/loyupdate.jsp?back_type=DOC&id=" + doc.getValue("ID_DOC") + "&type=general&action=add&process=no", "", "", "", "div_data_detail") %>
				    <%= Bean.getMenuButtonBase("COPY", "../crm/clients/loyupdate.jsp?back_type=DOC&id=" + doc.getValue("ID_DOC") + "&type=general&action=select&process=no", "", "", "", "div_data_detail") %>
				<%  } %>
				<!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagLoyality, "../crm/club/documentspecs.jsp?id=" + doc.getValue("ID_DOC") + "&tab="+Bean.currentMenu.getTabID("CLUB_DOCUMENTS_LOYALITY_SCHEME")+"&", "loyality_page") %>
			<% } %>
	
			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLUB_DOCUMENTS_FILES")) { %>
				<% if (Bean.currentMenu.isCurrentTabAndEditPermitted("CLUB_DOCUMENTS_FILES")) { %>
			    	<%= Bean.getMenuButtonBase("ADD", "../crm/club/documentfiles.jsp?type=general&id="+docid+"&action=add&process=no", "", "", Bean.documentXML.getfieldTransl("h_add_file", false), "div_data_detail") %>
				<% } %>
				<!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagFile, "../crm/club/documentspecs.jsp?id=" + doc.getValue("ID_DOC") + "&tab="+Bean.currentMenu.getTabID("CLUB_DOCUMENTS_FILES")+"&", "file_page") %>
			<% } %>
		</tr>
	</table>
	<table <%=Bean.getTableMenuParam() %>>
		<%= Bean.getDetailCaption(doc.getValue("NUMBER_DOC") + " - " + doc.getValue("DATE_DOC_FRMT")) %>
		<tr>
			<td>
				<!-- Выводим перечень закладок -->
				<%= Bean.currentMenu.getFormTabScheeds(tab, "../crm/club/documentspecs.jsp?id=" + docid) %>
			</td>
		</tr>
	</table>
</div>
<div id="div_data">
<div id="div_data_detail">

<%
if (Bean.currentMenu.isCurrentTabAndPreviewPermitted("CLUB_DOCUMENTS_INFO")) {
 %>	 
	<form>
	<table <%=Bean.getTableDetailParam() %>>
	    <tr>
			<td><%= Bean.documentXML.getfieldTransl("cd_doc_type", false) %></td> <td><input type="text" name="cd_doc_type" size="64" value="<%=doc.getValue("NAME_DOC_TYPE") %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.clubXML.getfieldTransl("club", false) %>
				<%= Bean.getGoToClubLink(doc.getValue("ID_CLUB")) %>
			</td><td><input type="text" name="name_club" size="50" value="<%= Bean.getClubShortName(doc.getValue("ID_CLUB")) %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
    	<tr>
			<td><%= Bean.documentXML.getfieldTransl("number_doc", false) %></td> <td><input type="text" name="number_doc" size="25" value="<%=doc.getValue("NUMBER_DOC") %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.documentXML.getfieldTransl("cd_doc_state", false) %></td> <td><input type="text" name="cd_doc_state" size="50" value="<%=doc.getValue("NAME_DOC_STATE") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
    	<tr>
			<td><%= Bean.documentXML.getfieldTransl("date_doc", false) %></td> <td><input type="text" name="date_doc" size="25" value="<%=doc.getValue("DATE_DOC_FRMT") %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.documentXML.getfieldTransl("date_begin_doc", false) %></td> <td><input type="text" name="date_begin_doc" size="25" value="<%=doc.getValue("DATE_BEGIN_DOC_FRMT") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
    	<tr>
			<td><%= Bean.documentXML.getfieldTransl("name_doc", false) %></td> <td><input type="text" name="name_doc" size="64" value="<%=doc.getValue("NAME_DOC") %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.documentXML.getfieldTransl("date_end_doc", false) %></td> <td><input type="text" name="date_end_doc" size="25" value="<%=doc.getValue("DATE_END_DOC_FRMT") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
    	<tr>
			<td rowspan="4"><%= Bean.documentXML.getfieldTransl("desc_doc", false) %></td><td rowspan="4"><textarea name="desc_doc" cols="60" rows="4" readonly="readonly" class="inputfield-ro"><%=doc.getValue("DESC_DOC") %></textarea></td>
			<% if (!(doc.getValue("ID_LG_RECORD")==null || "".equalsIgnoreCase(doc.getValue("ID_LG_RECORD")))) {
				bcLGObject logistic = new bcLGObject(doc.getValue("ID_LG_RECORD"));
			%>
			<td class="top_line">
				<input type="hidden" name="id_lg_record" value="<%=doc.getValue("ID_LG_RECORD") %>">
				<%= Bean.getLGTitle(logistic.getValue("CD_LG_TYPE")) %>
				<%= Bean.getLGHyperLink(logistic.getValue("CD_LG_TYPE"), doc.getValue("ID_LG_RECORD")) %>
			</td> <td class="top_line"><input type="text" name="operation_name" size="50" value="<%=logistic.getValue("OPERATION_NAME") %>" readonly="readonly" class="inputfield-ro">
			</td>
			<% } else { %>
			<td class="top_line"><%= Bean.documentXML.getfieldTransl("club_rel_type", false) %></td> <td class="top_line"><input type="text" name="name_club_rel_type" size="50" value="<%=doc.getValue("NAME_CLUB_REL_TYPE") %>" readonly="readonly" class="inputfield-ro"></td>
			<% } %>
		</tr>
    	<tr>
			<td><b><%= Bean.documentXML.getfieldTransl("name_party1", false) %></b>
			<%=Bean.getGoToJurPrsHyperLink(doc.getValue("ID_JUR_PRS_PARTY1")) %>
			</td> <td><input type="text" name="name_party1" size="50" value="<%=doc.getValue("SNAME_JUR_PRS_PARTY1") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
    	<tr>
			<td><%= Bean.documentXML.getfieldTransl("name_party2", false) %>
			<%=Bean.getGoToJurPrsHyperLink(doc.getValue("ID_JUR_PRS_PARTY2")) %>
			</td> <td><input type="text" name="name_party2" size="50" value="<%=doc.getValue("SNAME_JUR_PRS_PARTY2") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
    	<tr>
			<td><%= Bean.documentXML.getfieldTransl("name_party3", false) %>
			<%=Bean.getGoToJurPrsHyperLink(doc.getValue("ID_JUR_PRS_PARTY3")) %>
			</td> <td><input type="text" name="name_party3" size="50" value="<%=doc.getValue("SNAME_JUR_PRS_PARTY3") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<%=	Bean.getIdCreationAndMoficationRecordFields(
				doc.getValue("ID_DOC"),
				doc.getValue(Bean.getCreationDateFieldName()),
				doc.getValue("CREATED_BY"),
				doc.getValue(Bean.getLastUpdateDateFieldName()),
				doc.getValue("LAST_UPDATE_BY")
			) %>
		<tr>
			<td colspan="6" align="center">
				<%=Bean.getGoBackButton("../crm/club/documents.jsp") %>
			</td>
		</tr>

	</table>
	</form>

<% } else if (Bean.currentMenu.isCurrentTabAndEditPermitted("CLUB_DOCUMENTS_INFO")) { %>
	
	<script>
		var formData = new Array (
			new Array ('cd_doc_type', 'varchar2', 1),
			new Array ('cd_doc_state', 'varchar2', 1),
			new Array ('number_doc', 'varchar2', 1),
			new Array ('date_doc', 'varchar2', 1),
			new Array ('date_begin_doc', 'varchar2', 1),
			new Array ('name_doc', 'varchar2', 1),
			new Array ('name_party1', 'varchar2', 1)
		);
	</script>
	
    <form action="../crm/club/documentupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">
	    <input type="hidden" name="action" value="edit">
    	<input type="hidden" name="process" value="yes">
    	<input type="hidden" name="type" value="general">
    	<input type="hidden" name="id_doc" value="<%=doc.getValue("ID_DOC") %>">
	<table <%=Bean.getTableDetailParam() %>>
	    <tr>
			<td><%= Bean.documentXML.getfieldTransl("cd_doc_type", true) %></td> <td><select name="cd_doc_type" class="inputfield" > <%= Bean.getDocTypeOptions(doc.getValue("CD_DOC_TYPE"), false) %></select></td>
			<td><%= Bean.clubXML.getfieldTransl("club", false) %>
				<%= Bean.getGoToClubLink(doc.getValue("ID_CLUB")) %>
			</td><td><input type="text" name="name_club" size="55" value="<%= Bean.getClubShortName(doc.getValue("ID_CLUB")) %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
    	<tr>
			<td><%= Bean.documentXML.getfieldTransl("number_doc", true) %></td> <td><input type="text" name="number_doc" size="25" value="<%=doc.getValue("NUMBER_DOC") %>" class="inputfield"></td>
			<td><%= Bean.documentXML.getfieldTransl("cd_doc_state", true) %></td> <td><select name="cd_doc_state" class="inputfield" > <%= Bean.getDocStateOptions(doc.getValue("CD_DOC_STATE"), true) %></select></td>
		</tr>
    	<tr>
			<td><%= Bean.documentXML.getfieldTransl("date_doc", true) %></td> <td><%=Bean.getCalendarInputField("date_doc", doc.getValue("DATE_DOC_FRMT"), "10") %></td>
			<td><%= Bean.documentXML.getfieldTransl("date_begin_doc", false) %></td> <td><%=Bean.getCalendarInputField("date_begin_doc", doc.getValue("DATE_BEGIN_DOC_FRMT"), "10") %></td>
		</tr>
    	<tr>
			<td><%= Bean.documentXML.getfieldTransl("name_doc", true) %></td> <td><input type="text" name="name_doc" size="64" value="<%=doc.getValue("NAME_DOC") %>" class="inputfield"></td>
			<td><%= Bean.documentXML.getfieldTransl("date_end_doc", false) %></td> <td><%=Bean.getCalendarInputField("date_end_doc", doc.getValue("DATE_END_DOC_FRMT"), "10") %></td>
		</tr>
    	<tr>
			<td rowspan="4"><%= Bean.documentXML.getfieldTransl("desc_doc", false) %></td><td rowspan="4"><textarea name="desc_doc" cols="60" rows="4" class="inputfield"><%=doc.getValue("DESC_DOC") %></textarea></td>
			<% if (!(doc.getValue("ID_LG_RECORD")==null || "".equalsIgnoreCase(doc.getValue("ID_LG_RECORD")))) {
				bcLGObject logistic = new bcLGObject(doc.getValue("ID_LG_RECORD"));
			%>
			<td class="top_line">
				<input type="hidden" name="id_lg_record" value="<%=doc.getValue("ID_LG_RECORD") %>">
				<%= Bean.getLGTitle(logistic.getValue("CD_LG_TYPE")) %>
				<%= Bean.getLGHyperLink(logistic.getValue("CD_LG_TYPE"), doc.getValue("ID_LG_RECORD")) %>
			</td> <td class="top_line"><input type="text" name="operation_name" size="55" value="<%=logistic.getValue("OPERATION_NAME") %>" readonly="readonly" class="inputfield-ro">
			</td>
			<% } else { %>
			<td class="top_line"><%= Bean.documentXML.getfieldTransl("club_rel_type", false) %></td> <td class="top_line"><select name="cd_club_rel_type" class="inputfield" > <%= Bean.getClubRelTypeOptions(doc.getValue("CD_CLUB_REL_TYPE"), true) %></select></td>
			<% } %>
		</tr>
    	<tr>
			<td><b><%= Bean.documentXML.getfieldTransl("name_party1", true) %></b>
			<%=Bean.getGoToJurPrsHyperLink(doc.getValue("ID_JUR_PRS_PARTY1")) %>
			</td>
			<td>
				<%=Bean.getWindowFindJurPrs("party1", doc.getValue("ID_JUR_PRS_PARTY1"), doc.getValue("SNAME_JUR_PRS_PARTY1"), "ALL", "50") %>
			</td>			
		</tr>
    	<tr>
			<td><%= Bean.documentXML.getfieldTransl("name_party2", false) %>
			<%=Bean.getGoToJurPrsHyperLink(doc.getValue("ID_JUR_PRS_PARTY2")) %>
			</td>
			<td>
				<%=Bean.getWindowFindJurPrs("party2", doc.getValue("ID_JUR_PRS_PARTY2"), doc.getValue("SNAME_JUR_PRS_PARTY2"), "ALL", "50") %>
			</td>			
		</tr>
    	<tr>
			<td><%= Bean.documentXML.getfieldTransl("name_party3", false) %>
			<%=Bean.getGoToJurPrsHyperLink(doc.getValue("ID_JUR_PRS_PARTY3")) %>
			</td>
			<td>
				<%=Bean.getWindowFindJurPrs("party3", doc.getValue("ID_JUR_PRS_PARTY3"), doc.getValue("SNAME_JUR_PRS_PARTY3"), "ALL", "50") %>
			</td>			
		</tr>
		<%=	Bean.getIdCreationAndMoficationRecordFields(
				doc.getValue("ID_DOC"),
				doc.getValue(Bean.getCreationDateFieldName()),
				doc.getValue("CREATED_BY"),
				doc.getValue(Bean.getLastUpdateDateFieldName()),
				doc.getValue("LAST_UPDATE_BY")
			) %>
		<tr>
			<td colspan="6" align="center">
				<%=Bean.getSubmitButtonAjax("../crm/club/documentupdate.jsp", "submit", "updateForm") %>
				<%=Bean.getResetButton() %>
				<%=Bean.getGoBackButton("../crm/club/documents.jsp") %>
			</td>
		</tr>
	</table>

	</form>
	<%= Bean.getCalendarScript("date_doc", false) %>
	<%= Bean.getCalendarScript("date_begin_doc", false) %>
	<%= Bean.getCalendarScript("date_end_doc", false) %>

<%	} %>

<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLUB_DOCUMENTS_BANK_ACCOUNTS")) { %>
		
		<table <%=Bean.getTableBottomFilter() %>><tbody>
		<tr>
			<%=Bean.getFindHTML("bank_acc_find", bank_acc_find, "../crm/club/documentspecs.jsp?id="+docid + "&bank_account_page=1") %>
		
			<%=Bean.getSelectOnChangeBeginHTML("bank_acc_type", "../crm/club/documentspecs.jsp?id="+docid + "&bank_account_page=1", Bean.accountXML.getfieldTransl("name_doc_bank_account_type", false)) %>
				<%= Bean.getBankAccountTypeOptions(bank_acc_type, true) %>
			<%=Bean.getSelectOnChangeEndHTML() %>

		</tr>
		</tbody>
		</table>

		<%= doc.getDocBankAccountsHTML(bank_acc_find, bank_acc_type, l_bank_account_page_beg, l_bank_account_page_end) %>
<%	} %>

<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLUB_DOCUMENTS_COMISSIONS")) { %>
		
		<table <%=Bean.getTableBottomFilter() %>><tbody>
		<tr>
			<%=Bean.getFindHTML("comis_find", comis_find, "../crm/club/documentspecs.jsp?id="+docid + "&comis_page=1") %>
		
			<td>&nbsp;</td>
 
		</tr>
		</tbody>
		</table>

		<%= doc.getComissionHTML(comis_find, l_comis_page_beg, l_comis_page_end) %>
<%	} %>

<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLUB_DOCUMENTS_CARD_PACK")) { %>
		
		<table <%=Bean.getTableBottomFilter() %>><tbody>
		<tr>
			<%=Bean.getFindHTML("card_pack_find", card_pack_find, "../crm/club/documentspecs.jsp?id="+docid + "&card_pack_page=1") %>
		
			<%=Bean.getSelectOnChangeBeginHTML("card_pack_card_status", "../crm/club/documentspecs.jsp?id="+docid + "&card_pack_page=1", Bean.clubcardXML.getfieldTransl("NAME_CARD_STATUS", false)) %>
				<%= Bean.getClubCardStatusOptions(card_pack_card_status, true) %>
			<%=Bean.getSelectOnChangeEndHTML() %>

		</tr>
		</tbody>
		</table>

		<%= doc.getCardPackagesHTML(card_pack_find, card_pack_card_status, l_card_pack_page_beg, l_card_pack_page_end) %>
<%	} %>

<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLUB_DOCUMENTS_REFERRAL_SCHEME")) { %>
		
		<table <%=Bean.getTableBottomFilter() %>><tbody>
		<tr>
			<%=Bean.getFindHTML("referral_scheme_find", referral_scheme_find, "../crm/club/documentspecs.jsp?id="+docid + "&referral_scheme_page=1") %>

			<%=Bean.getSelectOnChangeBeginHTML("referral_scheme_type", "../crm/club/documentspecs.jsp?id="+docid + "&referral_scheme_page=1", Bean.clubXML.getfieldTransl("name_referral_scheme_type", false)) %>
				<%= Bean.getReferralShemeTypeOptions(referral_scheme_type, true) %>
			<%=Bean.getSelectOnChangeEndHTML() %>
	
			<%=Bean.getSelectOnChangeBeginHTML("referral_scheme_calc_type", "../crm/club/documentspecs.jsp?id="+docid + "&referral_scheme_page=1", Bean.clubXML.getfieldTransl("name_referral_scheme_calc_type", false)) %>
				<%= Bean.getReferralShemeCalcTypeOptions(referral_scheme_calc_type, true) %>
			<%=Bean.getSelectOnChangeEndHTML() %>
 
		</tr>
		</tbody>
		</table>

		<%= doc.getReferralSchemeHTML(referral_scheme_find, referral_scheme_type, referral_scheme_calc_type, l_referral_scheme_page_beg, l_referral_scheme_page_end) %>
<%	} %>

<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLUB_DOCUMENTS_TARGET_PROGRAMS")) { %>
		
		<table <%=Bean.getTableBottomFilter() %>><tbody>
		<tr>
			<%=Bean.getFindHTML("target_program_find", target_program_find, "../crm/club/documentspecs.jsp?id="+docid + "&target_program_page=1") %>
		
			<%=Bean.getSelectOnChangeBeginHTML("target_program_pay_period", "../crm/club/target_program.jsp?page=1&", Bean.clubfundXML.getfieldTransl("target_prg_pay_period", false)) %>
				<%= Bean.getTargetPrgPayPeriodOptions(target_program_pay_period, true) %>
			<%=Bean.getSelectOnChangeEndHTML() %>
	
		</tr>
		</tbody>
		</table>

		<%= doc.getTargetProgramsHTML(target_program_find, target_program_pay_period, l_target_program_page_beg, l_target_program_page_end) %>
<%	} %>

<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLUB_DOCUMENTS_LOYALITY_SCHEME")) { %>
		
		<table <%=Bean.getTableBottomFilter() %>><tbody>
		<tr>
			<%=Bean.getFindHTML("loyality_find", loyality_find, "../crm/club/documentspecs.jsp?id="+docid + "&loyality_page=1") %>

			<%=Bean.getSelectOnChangeBeginHTML("loyality_kind", "../crm/clients/loy.jsp?page=1", Bean.loyXML.getfieldTransl("name_kind_loyality", false)) %>
				<%= Bean.getLoyalityKindOptions(loyality_kind, true) %>
			<%=Bean.getSelectOnChangeEndHTML() %>

		</tr>
		</tbody>
		</table>

		<%= doc.getLoyalitySchemeHTML(loyality_find, loyality_kind, l_loyality_page_beg, l_loyality_page_end) %>
<%	} %>

<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("club_documents_files")) { %>
		
		<table <%=Bean.getTableBottomFilter() %>><tbody>
		<tr>
			<%=Bean.getFindHTML("file_find", file_find, "../crm/club/documentspecs.jsp?id="+docid + "&file_page=1") %>
		
			<td>&nbsp;</td>
 
		</tr>
		</tbody>
		</table>

		<%= doc.getDocFilesHTML(file_find, l_file_page_beg, l_file_page_end) %>
<%	} %>

<% } %>
</div></div>

</body>
</html>
