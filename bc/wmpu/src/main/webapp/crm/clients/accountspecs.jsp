<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%= Bean.getLogOutScript(request) %>

<%@page import="bc.objects.bcBankAccountObject"%>

<%@page import="java.util.HashMap"%><html>

<head>
	<%= Bean.getMetaContent() %>
	<%= Bean.getBottomFrameCSS() %>
</head>

<%

HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String pageFormName = "CLIENTS_BANK_ACCOUNTS";

Bean.setJspPageForTabName(pageFormName);

String tagBKAccounts = "_BK_ACCOUNTS";
String tagBKAccountFind = "_BK_ACCOUNT";
String tagDocument = "_DOCUMENT";
String tagDocumentFind = "_DOCUMENT_FIND";
String tagDocType = "_DOC_TYPE";
String tagDocBankAccountType = "_DOC_BANK_ACCOUNT_TYPE";
String tagDocClubRelType = "_DOC_CLUB_REL_TYPE";
String tagRests = "_RESTS";

String id = Bean.getDecodeParam(parameters.get("id"));
String	tab = Bean.getDecodeParam(parameters.get("tab"));
if (tab==null || "".equalsIgnoreCase(tab)) { 
	tab	= Bean.tabsHmGetValue(pageFormName); 
}

if (id==null || "".equalsIgnoreCase(id)) {
%>

	<%=Bean.getIDNotFoundMessage() %>

<% }
else {
	Bean.tabsHmSetValue(pageFormName, tab);
	bcBankAccountObject account = new bcBankAccountObject(id);

	//Обрабатываем номера страниц
	String l_bk_acc_page = Bean.getDecodeParam(parameters.get("bk_acc_page"));
	Bean.pageCheck(pageFormName + tagBKAccounts, l_bk_acc_page);
	String l_bk_acc_page_beg = Bean.getFirstRowNumber(pageFormName + tagBKAccounts);
	String l_bk_acc_page_end = Bean.getLastRowNumber(pageFormName + tagBKAccounts);

	String bk_acc_find 	= Bean.getDecodeParam(parameters.get("bk_acc_find"));
	bk_acc_find 	= Bean.checkFindString(pageFormName + tagBKAccountFind, bk_acc_find, l_bk_acc_page);

	String l_doc_page = Bean.getDecodeParam(parameters.get("doc_page"));
	Bean.pageCheck(pageFormName + tagDocument, l_doc_page);
	String l_doc_page_beg = Bean.getFirstRowNumber(pageFormName + tagDocument);
	String l_doc_page_end = Bean.getLastRowNumber(pageFormName + tagDocument);

	String doc_find 	= Bean.getDecodeParam(parameters.get("doc_find"));
	doc_find 	= Bean.checkFindString(pageFormName + tagDocumentFind, doc_find, l_doc_page);

	String doc_type	= Bean.getDecodeParam(parameters.get("doc_type"));
	doc_type		= Bean.checkFindString(pageFormName + tagDocType, doc_type, l_doc_page);

	String doc_bank_account_type	= Bean.getDecodeParam(parameters.get("doc_bank_account_type"));
	doc_bank_account_type		= Bean.checkFindString(pageFormName + tagDocBankAccountType, doc_bank_account_type, l_doc_page);

	String club_rel_type	= Bean.getDecodeParam(parameters.get("club_rel_type"));
	club_rel_type		= Bean.checkFindString(pageFormName + tagDocClubRelType, club_rel_type, l_doc_page);

	String l_rest_page = Bean.getDecodeParam(parameters.get("rest_page"));
	Bean.pageCheck(pageFormName + tagRests, l_rest_page);
	String l_rest_page_beg = Bean.getFirstRowNumber(pageFormName + tagRests);
	String l_rest_page_end = Bean.getLastRowNumber(pageFormName + tagRests);
%>
<body>
<div id="div_tabsheet">
	<table <%=Bean.getTableMenuParam() %>>
		<tr>
			<%= Bean.getPageHeader() %>

			<% if (Bean.currentMenu.isCurrentTabAndEditPermitted("CLIENTS_BANK_ACCOUNTS_INFO")) { %>
				<%= Bean.getMenuButton("ADD", "../crm/clients/accountsupdate.jsp?id_bank_account=" + account.getValue("ID_BANK_ACCOUNT") + "&type=general&action=add2&process=no", "", "") %>
				<%= Bean.getMenuButton("DELETE", "../crm/clients/accountsupdate.jsp?id_bank_account=" + account.getValue("ID_BANK_ACCOUNT") + "&type=general&action=remove&process=yes", Bean.accountXML.getfieldTransl("h_delete_bank_account", false), account.getValue("NUMBER_BANK_ACCOUNT")) %>
			<% } %>
	
			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLIENTS_BANK_ACCOUNTS_BK_ACCOUNTS")) { %>
			    <!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagBKAccounts, "../crm/clients/accountspecs.jsp?id=" + account.getValue("ID_BANK_ACCOUNT") + "&tab="+Bean.currentMenu.getTabID("CLIENTS_BANK_ACCOUNTS_BK_ACCOUNTS")+"&", "bk_acc_page") %>
			<% } %>
	
			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLIENTS_BANK_ACCOUNTS_DOCUMENTS")) { %>
			    <!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagDocument, "../crm/clients/accountspecs.jsp?id=" + account.getValue("ID_BANK_ACCOUNT") + "&tab="+Bean.currentMenu.getTabID("CLIENTS_BANK_ACCOUNTS_DOCUMENTS")+"&", "doc_page") %>
			<% } %>
	
			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLIENTS_BANK_ACCOUNTS_RESTS")) { %>
				<% if (Bean.currentMenu.isCurrentTabAndEditPermitted("CLIENTS_BANK_ACCOUNTS_RESTS")) { %>
					<%= Bean.getMenuButtonBase("RUN", "../crm/clients/accountsupdate.jsp?id_bank_account=" + account.getValue("ID_BANK_ACCOUNT") + "&type=general&action=calc_rests&process=no", "", "", Bean.accountXML.getfieldTransl("h_calc_rests", false), "div_data_detail") %>
				<% } %>
			    <!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagRests, "../crm/clients/accountspecs.jsp?id=" + account.getValue("ID_BANK_ACCOUNT") + "&tab="+Bean.currentMenu.getTabID("CLIENTS_BANK_ACCOUNTS_RESTS")+"&", "rest_page") %>
			<% } %>
		</tr>
	</table>
	<table <%=Bean.getTableMenuParam() %>>
		<%= Bean.getDetailCaption(account.getValue("NUMBER_BANK_ACCOUNT") + " - " + account.getValue("NAME_BANK_ACCOUNT_TYPE")) %>
		<tr>
			<td>
				<!-- Выводим перечень закладок -->
				<%= Bean.currentMenu.getFormTabScheeds(tab, "../crm/clients/accountspecs.jsp?id=" + account.getValue("ID_BANK_ACCOUNT")) %>
			</td>
		</tr>
	</table>
</div>
<div id="div_data">
<div id="div_data_detail">
<%
if (Bean.currentMenu.isCurrentTabAndPreviewPermitted("CLIENTS_BANK_ACCOUNTS_INFO")) {
%>
	<form>
	<table <%=Bean.getTableDetailParam() %>>
		<tr>
			<% if (Bean.isEmpty(account.getValue("ID_NAT_PRS"))) { %>
			<td><%=Bean.accountXML.getfieldTransl("name_owner_bank_account", true)%>
				<%=Bean.getGoToJurPrsHyperLink(account.getValue("ID_JUR_PRS")) %>
			</td>
			<% } else { %>
			<td><%= Bean.accountXML.getfieldTransl("name_owner_bank_account", true) %>
				<%= Bean.getGoToNatPrsLink(account.getValue("ID_NAT_PRS")) %>
			</td>
			<% } %>
			<td><input type="text" name="name_owner" size="68" value="<%=account.getValue("SNAME_OWNER_BANK_ACCOUNT")%>" readonly="readonly" class="inputfield-ro"></td>
 		    <td><%= Bean.clubXML.getfieldTransl("club", false) %>
				<%= Bean.getGoToClubLink(account.getValue("ID_CLUB")) %>
			</td><td><input type="text" name="name_club" size="50" value="<%= Bean.getClubShortName(account.getValue("ID_CLUB")) %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td><%=Bean.accountXML.getfieldTransl("name_bank_account_type", false)%></td><td><input type="text" name="cd_bank_account_type" size="68" value="<%=account.getValue("NAME_BANK_ACCOUNT_TYPE")%>" readonly="readonly" class="inputfield-ro"></td>
			<td><%=Bean.clubXML.getfieldTransl("club_date_beg", false)%></td> <td><input type="text" name="date_beg" size="20" value="<%=account.getValue("DATE_BEG_FRMT")%>" readonly="readonly" class="inputfield-ro"> </td>
		</tr>
		<tr>
			<td><%=Bean.accountXML.getfieldTransl("number_bank_account", false)%> </td><td><input type="text" name="number_bank_account" size="68" value="<%=account.getValue("NUMBER_BANK_ACCOUNT")%>" readonly="readonly" class="inputfield-ro"></td>
			<td rowspan="3"><%=Bean.accountXML.getfieldTransl("desc_bank_account", false)%></td><td rowspan="3"><textarea name="desc_bank_account" cols="47" rows="3" readonly="readonly" class="inputfield-ro"><%=account.getValue("DESC_BANK_ACCOUNT")%></textarea></td>
		</tr>
		<tr>
			<td><%=Bean.accountXML.getfieldTransl("name_bank_alt", false)%>
				<%= Bean.getGoToJurPrsHyperLink(account.getValue("ID_BANK")) %>
			</td> <td><input type="text" name="name_bank" size="68" value="<%=account.getValue("SNAME_BANK")%>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td><%=Bean.accountXML.getfieldTransl("name_currency", false)%></td> <td><input type="text" name="name_currency" size="20" value="<%=account.getValue("NAME_CURRENCY")%>" readonly="readonly" class="inputfield-ro"> </td>
		</tr>
		<%=	Bean.getIdCreationAndMoficationRecordFields(
				account.getValue("ID_BANK_ACCOUNT"),
				account.getValue(Bean.getCreationDateFieldName()),
				account.getValue("CREATED_BY"),
				account.getValue(Bean.getLastUpdateDateFieldName()),
				account.getValue("LAST_UPDATE_BY")
			) %>
		<tr>
			<td colspan="6" align="center">
				<%=Bean.getGoBackButton("../crm/clients/accounts.jsp") %>
			</td>
		</tr>
	</table>
	</form>

<%
} else if (Bean.currentMenu.isCurrentTabAndEditPermitted("CLIENTS_BANK_ACCOUNTS_INFO")) {
%> 
	<script>
		var formData = new Array (
			new Array ('name_entry', 'varchar2', 1),
			new Array ('cd_bank_account_type', 'varchar2', 1),
			new Array ('number_bank_account', 'varchar2', 1),
			new Array ('name_bank', 'varchar2', 1),
			new Array ('cd_currency', 'varchar2', 1),
			new Array ('name_club', 'varchar2', 1),
			new Array ('date_beg', 'varchar2', 1)
		);
		function myValidateForm() {
			return validateForm(formData);
		}
	</script>
	
    <form action="../crm/clients/accountsupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">
        <input type="hidden" name="action" value="edit">
        <input type="hidden" name="process" value="yes">
        <input type="hidden" name="id_bank_account" value="<%=account.getValue("ID_BANK_ACCOUNT")%>">
	<table <%=Bean.getTableDetailParam() %>>
		<tr>
			<% if (Bean.isEmpty(account.getValue("ID_NAT_PRS"))) { %>
			<td><%=Bean.accountXML.getfieldTransl("name_owner_bank_account", true)%>
				<%=Bean.getGoToJurPrsHyperLink(account.getValue("ID_JUR_PRS")) %>
			</td>
			<td>
				<%=Bean.getWindowFindJurAndNatPrs(account.getValue("ID_JUR_PRS"), "ALL", "JUR_PRS", "37") %>
			</td>
			<% } else { %>
			<td><%= Bean.accountXML.getfieldTransl("name_owner_bank_account", true) %>
				<%= Bean.getGoToNatPrsLink(account.getValue("ID_NAT_PRS")) %>
			</td>
			<td>
				<%=Bean.getWindowFindJurAndNatPrs(account.getValue("ID_NAT_PRS"), "ALL", "NAT_PRS", "37") %>
			</td>
			<% } %>
 		    <td><%= Bean.clubXML.getfieldTransl("club", false) %>
				<%= Bean.getGoToClubLink(account.getValue("ID_CLUB")) %>
			</td><td><input type="text" name="name_club" size="50" value="<%= Bean.getClubShortName(account.getValue("ID_CLUB")) %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>		
			<td><%=Bean.accountXML.getfieldTransl("name_bank_account_type", true)%></td><td><select name="cd_bank_account_type"  class="inputfield"><%=Bean.getBankAccountTypeOptions(account.getValue("CD_BANK_ACCOUNT_TYPE"), true)%></select></td>
			<td><%=Bean.clubXML.getfieldTransl("club_date_beg", true)%></td> <td><%=Bean.getCalendarInputField("date_beg", account.getValue("DATE_BEG_FRMT"), "10") %></td>
		</tr>
		<tr>		
			<td><%=Bean.accountXML.getfieldTransl("number_bank_account", true)%></td><td><input type="text" name="number_bank_account" size="50" value="<%=account.getValue("NUMBER_BANK_ACCOUNT")%>" class="inputfield"></td>
			<td rowspan="3"><%=Bean.accountXML.getfieldTransl("desc_bank_account", false)%></td><td rowspan="3"><textarea name="desc_bank_account" cols="47" rows="3" class="inputfield"><%=account.getValue("DESC_BANK_ACCOUNT")%></textarea></td>
		</tr>
		<tr>
			<td><%=Bean.accountXML.getfieldTransl("name_bank_alt", true)%>
				<%= Bean.getGoToJurPrsHyperLink(account.getValue("ID_BANK")) %>
			</td>
			<td>
				<%=Bean.getWindowFindJurPrs("bank", account.getValue("ID_BANK"), "BANK", "37") %>
			</td>			
		</tr>
		<tr>
			<td><%=Bean.accountXML.getfieldTransl("name_currency", true)%></td> <td><select name="cd_currency" class="inputfield"><%=Bean.getCurrencyOptions(account.getValue("CD_CURRENCY"), true)%></select> </td>
		</tr>
		<%=	Bean.getIdCreationAndMoficationRecordFields(
				account.getValue("ID_BANK_ACCOUNT"),
				account.getValue(Bean.getCreationDateFieldName()),
				account.getValue("CREATED_BY"),
				account.getValue(Bean.getLastUpdateDateFieldName()),
				account.getValue("LAST_UPDATE_BY")
			) %>

		<tr>
			<td colspan="6" align="center">
				<%=Bean.getSubmitButtonAjax("../crm/clients/accountsupdate.jsp") %>
				<%=Bean.getResetButton() %>
				<%=Bean.getGoBackButton("../crm/clients/accounts.jsp") %>
			</td>
		</tr>

	</table>
	</form>
	
	<!-- Скрипт для втавки меню вибору дати -->
	<%= Bean.getCalendarScript("date_beg", false) %>

<% }

if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLIENTS_BANK_ACCOUNTS_BK_ACCOUNTS")) {%>
	<table <%= Bean.getTableBottomFilter() %>>
		<tr>
		<%= Bean.getFindHTML("bk_acc_find", bk_acc_find, "../crm/clients/accountspecs.jsp?id=" + id + "&bk_acc_page=1") %>

		<td>&nbsp;</td>
		</tr>
	</table>
	<%= account.getBKAccountsHTML(bk_acc_find, l_bk_acc_page_beg, l_bk_acc_page_end) %>
<%}

if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLIENTS_BANK_ACCOUNTS_DOCUMENTS")) {%>
	<table <%= Bean.getTableBottomFilter() %>>
		<tr>
		<%= Bean.getFindHTML("doc_find", doc_find, "../crm/clients/accountspecs.jsp?id=" + id + "&doc_page=1") %>

		<%=Bean.getSelectOnChangeBeginHTML("doc_type", "../crm/clients/accountspecs.jsp?id=" + id + "&doc_page=1", Bean.documentXML.getfieldTransl("name_doc_type", false)) %>
			<%= Bean.getDocTypeOptions(doc_type, true) %>
		<%=Bean.getSelectOnChangeEndHTML() %>

		<%=Bean.getSelectOnChangeBeginHTML("club_rel_type", "../crm/clients/accountspecs.jsp?id=" + id + "&doc_page=1", Bean.documentXML.getfieldTransl("name_doc_bank_account_type", false)) %>
			<%= Bean.getClubRelTypeOptions(club_rel_type, true) %>
		<%=Bean.getSelectOnChangeEndHTML() %>

		<%=Bean.getSelectOnChangeBeginHTML("doc_bank_account_type", "../crm/clients/accountspecs.jsp?id=" + id + "&doc_page=1", Bean.documentXML.getfieldTransl("name_doc_bank_account_type", false)) %>
			<%= Bean.getBankAccountTypeOptions(doc_bank_account_type, true) %>
		<%=Bean.getSelectOnChangeEndHTML() %>

		</tr>
	</table>
	<%= account.getDocumentsHTML(doc_find, doc_type, club_rel_type, doc_bank_account_type, l_doc_page_beg, l_doc_page_end) %>
<%} %>

<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLIENTS_BANK_ACCOUNTS_RESTS")) {

    String tagFindRest = "_FIND_REST";
    String tagFindRestBegin = "_FIND_REST_BEGIN";
    String tagFindRestEnd = "_FIND_REST_END";
    
	String find_rest	 	= Bean.getDecodeParam(parameters.get("find_rest"));
	find_rest 				= Bean.checkFindString(pageFormName + tagFindRest, find_rest, "");
	String begin_period	 	= Bean.getDecodeParam(parameters.get("begin_period"));
	begin_period 			= Bean.checkFindString(pageFormName + tagFindRestBegin, begin_period, "");
	String end_period	 	= Bean.getDecodeParam(parameters.get("end_period"));
	end_period 				= Bean.checkFindString(pageFormName + tagFindRestEnd, end_period, "");

   %>
	
	<form action="../crm/clients/accountsupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">
        <input type="hidden" name="id_bank_account" value="<%= account.getValue("ID_BANK_ACCOUNT") %>">
	<table <%=Bean.getTableBottomFilter() %>>
		<tr>
			<td valign="top"><%= Bean.accountXML.getfieldTransl("h_begin_period", false) %>&nbsp;<%=Bean.getCalendarInputField("begin_period", begin_period, "10") %>&nbsp;&nbsp;&nbsp;&nbsp;
			<%= Bean.accountXML.getfieldTransl("h_end_period", false) %>&nbsp;<%=Bean.getCalendarInputField("end_period", end_period, "10") %>&nbsp;&nbsp;&nbsp;
			<%= Bean.accountXML.getfieldTransl("h_find_string", false) %>&nbsp;&nbsp;&nbsp;
			<input type="text" name="find_rest" id="find_rest" size="30" value="<%=find_rest %>" class="inputfield" title="<%= Bean.buttonXML.getfieldTransl("find_string", false) %>">&nbsp;
			<%=Bean.getSubmitButtonAjax("../crm/clients/accountspecs.jsp?id=" + account.getValue("ID_BANK_ACCOUNT") + "&rest_page=1&", "find", "updateForm") %>
			</td>
			<td>&nbsp;</td>
		</tr>
	</table>
	</form>
	<!-- Скрипт для втавки меню вибору дати -->
		<%= Bean.getCalendarScript("begin_period", false) %>
		<%= Bean.getCalendarScript("end_period", false) %>
	<%= account.getRestsHTML(begin_period, end_period, find_rest, l_rest_page_beg, l_rest_page_end) %>
<%} %>

<% } %>
</div></div>
</body>
</html>
