<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%@page import="bc.objects.bcClubShortObject"%>
<%@page import="bc.objects.bcBKAccountObject"%>
<%@page import="java.util.HashMap"%>

<%= Bean.getLogOutScript(request) %>
<html>
<head>
	<%= Bean.getMetaContent() %>
	<%= Bean.getBottomFrameCSS() %>

</head>
<body topmargin="0">
<div id="div_tabsheet">

<%

HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String pageFormName = "FINANCE_BK_ACCOUNTS";
String tagRests = "_RESTS";
String tagFindRest = "_FIND_REST";
String tagFindRestBegin = "_FIND_REST_BEGIN";
String tagFindRestEnd = "_FIND_REST_END";
String tagBankAccounts = "_BANK_ACCOUNTS";
String tagBankAccountFind = "_BANK_ACCOUNT_FIND";

Bean.setJspPageForTabName(pageFormName);

String accountid = Bean.getDecodeParam(parameters.get("id"));
String	tab = Bean.getDecodeParam(parameters.get("tab"));
String sementFull = "";
if (tab==null || "".equalsIgnoreCase(tab)) { tab = Bean.tabsHmGetValue(pageFormName); }
if (accountid==null) {
%>
	<%=Bean.getIDNotFoundMessage() %>
<% 
}
else {
	Bean.tabsHmSetValue(pageFormName, tab);
	bcBKAccountObject account = new bcBKAccountObject(accountid);
	
	//Обрабатываем номера страниц
	String l_rest_page = Bean.getDecodeParam(parameters.get("rest_page"));
	Bean.pageCheck(pageFormName + tagRests, l_rest_page);
	String l_rest_page_beg = Bean.getFirstRowNumber(pageFormName + tagRests);
	String l_rest_page_end = Bean.getLastRowNumber(pageFormName + tagRests);

	String find_rest	 	= Bean.getDecodeParam(parameters.get("find_rest"));
	find_rest 				= Bean.checkFindString(pageFormName + tagFindRest, find_rest, "");

	String begin_period	 	= Bean.getDecodeParam(parameters.get("begin_period"));
	begin_period 			= Bean.checkFindString(pageFormName + tagFindRestBegin, begin_period, "");

	String end_period	 	= Bean.getDecodeParam(parameters.get("end_period"));
	end_period 				= Bean.checkFindString(pageFormName + tagFindRestEnd, end_period, "");

	String l_bank_accnt_page = Bean.getDecodeParam(parameters.get("bank_accnt_page"));
	Bean.pageCheck(pageFormName + tagBankAccounts, l_bank_accnt_page);
	String l_bank_accnt_page_beg = Bean.getFirstRowNumber(pageFormName + tagBankAccounts);
	String l_bank_accnt_page_end = Bean.getLastRowNumber(pageFormName + tagBankAccounts);

	String bank_accnt_find = Bean.getDecodeParam(parameters.get("bank_accnt_find"));
	bank_accnt_find = Bean.checkFindString(pageFormName + tagBankAccountFind, bank_accnt_find, l_bank_accnt_page);
%>

	<table <%=Bean.getTableMenuParam() %>>
		<tr>
			<%= Bean.getPageHeader() %>

			<% if (Bean.currentMenu.isCurrentTabAndEditPermitted("FINANCE_BK_ACCOUNTS_INFO")) { %>
				<%= Bean.getMenuButton("ADD", "../crm/finance/bk_accountsupdate.jsp?id=" + accountid + "&type=general&action=add2&process=no", "", "") %>
				<%= Bean.getMenuButton("COPY", "../crm/finance/bk_accountsupdate.jsp?id=" + accountid + "&type=general&action=copy&process=no", "", "") %>
				<%= Bean.getMenuButton("DELETE", "../crm/finance/bk_accountsupdate.jsp?id=" + accountid + "&type=general&action=remove&process=yes", Bean.bk_accountXML.getfieldTransl("h_delete_bk_account", false), account.getValue("CD_BK_ACCOUNT") + " - " + account.getValue("NAME_BK_ACCOUNT")) %>
			<% } %>
	
			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("FINANCE_BK_ACCOUNTS_BANK_ACCOUNTS")) { %>
			    <!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagBankAccounts, "../crm/finance/bk_accountspecs.jsp?id=" + accountid + "&tab="+Bean.currentMenu.getTabID("FINANCE_BK_ACCOUNTS_BANK_ACCOUNTS")+"&", "bank_accnt_page") %>
			<% } %>
	
			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("FINANCE_BK_ACCOUNTS_RESTS")) { %>
				<% if (Bean.currentMenu.isCurrentTabAndEditPermitted("FINANCE_BK_ACCOUNTS_RESTS")) { %>
					<%= Bean.getMenuButton("RUN", "../crm/finance/bk_accountsupdate.jsp?id=" + accountid + "&type=general&action=calc_rests&process=no", "", "", Bean.bk_accountXML.getfieldTransl("h_calc_rests", false)) %>
				<% } %>
			    <!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagRests, "../crm/finance/bk_accountspecs.jsp?id=" + accountid + "&tab="+Bean.currentMenu.getTabID("FINANCE_BK_ACCOUNTS_RESTS")+"&", "rest_page") %>
			<% } %>
	
		</tr>
	</table>
	<table <%=Bean.getTableMenuParam() %>>
		<%= Bean.getDetailCaption(account.getValue("CD_BK_ACCOUNT") + " - " + account.getValue("NAME_BK_ACCOUNT")) %>
		<tr>
			<td>
				<!-- Выводим перечень закладок -->
				<%= Bean.currentMenu.getFormTabScheeds(tab, "../crm/finance/bk_accountspecs.jsp?id=" + accountid) %>
			</td>
		</tr>
	</table>
</div>
<div id="div_data">
<div id="div_data_detail">
<%
if (Bean.currentMenu.isCurrentTabAndEditPermitted("FINANCE_BK_ACCOUNTS_INFO")) {
	%>
	<script>
		var formData = new Array (
			new Array ('segment1', 'varchar2', 1),
			new Array ('name_bk_account', 'varchar2', 1),
			new Array ('internal_name_bk_account', 'varchar2', 1)
		);
	</script>

    <form action="../crm/finance/bk_accountsupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">
        <input type="hidden" name="action" value="edit">
        <input type="hidden" name="process" value="yes">
        <input type="hidden" name="id" value="<%= accountid %>">
	<table <%=Bean.getTableDetailParam() %>>
		<tr>
			<td><%= Bean.bk_accountXML.getfieldTransl("id_bk_account", false) %> </td><td><input type="text" name="id_bk_account" size="20" value="<%= account.getValue("ID_BK_ACCOUNT") %>" readonly="readonly" class="inputfield-ro"></td>
			<td>&nbsp;</td> <td>&nbsp;</td>
		</tr>
		<tr>
			<td><%= Bean.bk_accountXML.getfieldTransl("cd_bk_account", true) %> </td>
			<td  colspan="3">
			<% //int bkCount = Integer.parseInt(Bean.club_bk_account_segments_count); 
    		
			bcClubShortObject club = new bcClubShortObject(account.getValue("ID_CLUB"));
			
			int i = 0;
			for (i=1; i<=Integer.parseInt(club.getValue("BK_ACCOUNT_SEGMENTS_COUNT")); i++) { %>
				<% if (i==1) {	%>
					<%=Bean.getBKAccountSegment("segment1", account.getValue("SEGMENT1"), account.getValue("SEGMENT1_PARTICIPANT")) %>
			    <% } else if (i==2) { %>	
					<%=Bean.getBKAccountSegment("segment2", account.getValue("SEGMENT2"), account.getValue("SEGMENT2_PARTICIPANT")) %>
				<% } else if (i==3) { %>	
					<%=Bean.getBKAccountSegment("segment3", account.getValue("SEGMENT3"), account.getValue("SEGMENT3_PARTICIPANT")) %>
				<% } else if (i==4) { %>	
					<%=Bean.getBKAccountSegment("segment4", account.getValue("SEGMENT4"), account.getValue("SEGMENT4_PARTICIPANT")) %>
				<% } else if (i==5) { %>	
					<%=Bean.getBKAccountSegment("segment5", account.getValue("SEGMENT5"), account.getValue("SEGMENT5_PARTICIPANT")) %>
				<% } else if (i==6) { %>	
					<%=Bean.getBKAccountSegment("segment6", account.getValue("SEGMENT6"), account.getValue("SEGMENT6_PARTICIPANT")) %>
				<% } else if (i==7) { %>	
					<%=Bean.getBKAccountSegment("segment7", account.getValue("SEGMENT7"), account.getValue("SEGMENT7_PARTICIPANT")) %>
				<% } else if (i==8) { %>	
					<%=Bean.getBKAccountSegment("segment8", account.getValue("SEGMENT8"), account.getValue("SEGMENT8_PARTICIPANT")) %>
				<% } else if (i==9) { %>	
					<%=Bean.getBKAccountSegment("segment9", account.getValue("SEGMENT9"), account.getValue("SEGMENT9_PARTICIPANT")) %>
				<% } else if (i==10) { %>	
					<%=Bean.getBKAccountSegment("segment10", account.getValue("SEGMENT10"), account.getValue("SEGMENT10_PARTICIPANT")) %>
				<% } else if (i==11) { %>	
					<%=Bean.getBKAccountSegment("segment11", account.getValue("SEGMENT11"), account.getValue("SEGMENT11_PARTICIPANT")) %>
				<% } else if (i==12) { %>	
					<%=Bean.getBKAccountSegment("segment12", account.getValue("SEGMENT12"), account.getValue("SEGMENT12_PARTICIPANT")) %>
				<% } else if (i==13) { %>	
					<%=Bean.getBKAccountSegment("segment13", account.getValue("SEGMENT13"), account.getValue("SEGMENT13_PARTICIPANT")) %>
				<% } else if (i==14) { %>	
					<%=Bean.getBKAccountSegment("segment14", account.getValue("SEGMENT14"), account.getValue("SEGMENT14_PARTICIPANT")) %>
				<% } else if (i==15) { %>	
					<%=Bean.getBKAccountSegment("segment15", account.getValue("SEGMENT15"), account.getValue("SEGMENT15_PARTICIPANT")) %>
			<% } 
			}%>
		</tr>
		<tr>
			<td><%= Bean.bk_accountXML.getfieldTransl("id_bk_account_scheme", false) %>
				<%= Bean.getGoToFinanceBKSchemeLink(account.getValue("ID_BK_ACCOUNT_SCHEME_LINE")) %>
			</td> <td><select name="id_bk_account_scheme_line"  class="inputfield"><option value=""></option><%= Bean.getPostingSettingsOptions(account.getValue("ID_BK_ACCOUNT_SCHEME_LINE"), false) %></select></td>
			<td><%= Bean.clubXML.getfieldTransl("club", false) %>
				<%= Bean.getGoToClubLink(account.getValue("ID_CLUB")) %>
			</td><td><input type="text" name="name_club" size="30" value="<%= Bean.getClubShortName(account.getValue("ID_CLUB")) %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td><%= Bean.bk_accountXML.getfieldTransl("name_bk_account", true) %></td> <td><input type="text" name="name_bk_account" size="70" value="<%= account.getValue("NAME_BK_ACCOUNT") %>" class="inputfield"> </td>
			<td><%= Bean.bk_accountXML.getfieldTransl("is_group_tsl", false) %></td> <td><select name="is_group" class="inputfield"><%= Bean.getMeaningFromLookupNameOptions("YES_NO", account.getValue("IS_GROUP"), false) %></select> </td>
		</tr>
		<tr>
			<td><%= Bean.bk_accountXML.getfieldTransl("internal_name_bk_account", true) %></td><td><input type="text" name="internal_name_bk_account" size="70" value="<%= account.getValue("INTERNAL_NAME_BK_ACCOUNT") %>" class="inputfield"></td>
			<td><%= Bean.bk_accountXML.getfieldTransl("cd_bk_account_parent", false) %> </td> <td><select name="id_bk_account_parent"  class="inputfield"><%= Bean.getBKAccountsGroupOptions(account.getValue("ID_BK_ACCOUNT_PARENT"), true) %></select></td>
		</tr>
		<tr>
			<td valign=top><%= Bean.bk_accountXML.getfieldTransl("desc_bk_account", false) %></td> <td valign=top><textarea name="desc_bk_account" cols="66" rows="4" class="inputfield"><%= account.getValue("DESC_BK_ACCOUNT") %></textarea> </td>
			<td><%= Bean.bk_accountXML.getfieldTransl("exist_flag", false) %> </td><td><select name="exist_flag" class="inputfield"><%= Bean.getMeaningFromLookupNameOptions("YES_NO", account.getValue("EXIST_FLAG"), false) %></select></td>
		</tr>
		<%=	Bean.getCreationAndMoficationRecordFields(
				account.getValue(Bean.getCreationDateFieldName()),
				account.getValue("CREATED_BY"),
				account.getValue(Bean.getLastUpdateDateFieldName()),
				account.getValue("LAST_UPDATE_BY")
			) %>
 		<tr>
			<td colspan="6" align="center">
				<%=Bean.getSubmitButtonAjax("../crm/finance/bk_accountsupdate.jsp") %>
				<%=Bean.getResetButton() %>
				<%=Bean.getGoBackButton("../crm/finance/bk_accounts.jsp") %>
			</td>
		</tr>

	</table>

	</form>
<% } else if (Bean.currentMenu.isCurrentTabAndPreviewPermitted("FINANCE_BK_ACCOUNTS_INFO")) {

 %>
	<form>
	<table <%=Bean.getTableDetailParam() %>>
		<tr>
			<td><%= Bean.bk_accountXML.getfieldTransl("id_bk_account", false) %> </td><td><input type="text" name="id_bk_account" size="20" value="<%= account.getValue("ID_BK_ACCOUNT") %>" readonly="readonly" class="inputfield-ro"></td>
			<td>&nbsp;</td> <td>&nbsp;</td>
		</tr>
		<tr>
			<td><%= Bean.bk_accountXML.getfieldTransl("cd_bk_account", false) %> </td><td><input type="text" name="cd_bk_account" size="40" value="<%= account.getValue("CD_BK_ACCOUNT") %>" readonly="readonly" class="inputfield-ro"></td>
			<td>&nbsp;</td> <td>&nbsp;</td>
		</tr>
		<tr>
			<td><%= Bean.bk_accountXML.getfieldTransl("id_bk_account_scheme", false) %>
				<%= Bean.getGoToFinanceBKSchemeLink(account.getValue("ID_BK_ACCOUNT_SCHEME_LINE")) %>
			</td> <td><input type="text" name="name_bk_account_scheme_line" size="110" value="<%= account.getValue("NAME_BK_ACCOUNT_SCHEME_LINE") %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.clubXML.getfieldTransl("club", false) %>
				<%= Bean.getGoToClubLink(account.getValue("ID_CLUB")) %>
			</td><td><input type="text" name="name_club" size="30" value="<%= Bean.getClubShortName(account.getValue("ID_CLUB")) %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td><%= Bean.bk_accountXML.getfieldTransl("name_bk_account", false) %></td> <td><input type="text" name="name_bk_account" size="70" value="<%= account.getValue("NAME_BK_ACCOUNT") %>" readonly="readonly" class="inputfield-ro"> </td>
			<td><%= Bean.bk_accountXML.getfieldTransl("is_group_tsl", false) %></td> <td><input type="text" name="is_group_tsl" size="20" value="<%= account.getValue("IS_GROUP_TSL") %>" readonly="readonly" class="inputfield-ro"> </td>
		</tr>
		<tr>
			<td><%= Bean.bk_accountXML.getfieldTransl("internal_name_bk_account", false) %> </td><td><input type="text" name="internal_name_bk_account" size="70" value="<%= account.getValue("INTERNAL_NAME_BK_ACCOUNT") %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.bk_accountXML.getfieldTransl("cd_bk_account_parent", false) %> </td> <td><input type="text" name="cd_bk_account_parent" size="70" value="<%= account.getValue("CD_BK_ACCOUNT_PARENT") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td><%= Bean.bk_accountXML.getfieldTransl("desc_bk_account", false) %></td> <td><textarea name="desc_bk_account" cols="66" rows="4" readonly="readonly" class="inputfield-ro"><%= account.getValue("DESC_BK_ACCOUNT") %></textarea> </td>
			<td valign=top><%= Bean.bk_accountXML.getfieldTransl("exist_flag", false) %></td> <td valign=top><input type="text" name="exist_flag" size="70" value="<%= Bean.getMeaningFoCodeValue("YES_NO", account.getValue("EXIST_FLAG")) %>" readonly="readonly" class="inputfield-ro"> </td>
		</tr>
		<%=	Bean.getCreationAndMoficationRecordFields(
				account.getValue(Bean.getCreationDateFieldName()),
				account.getValue("CREATED_BY"),
				account.getValue(Bean.getLastUpdateDateFieldName()),
				account.getValue("LAST_UPDATE_BY")
			) %>
 		<tr>
			<td colspan="6" align="center">
				<%=Bean.getGoBackButton("../crm/finance/bk_accounts.jsp") %>
			</td>
		</tr>
	</table>
	</form>
    
<% } %>

<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("FINANCE_BK_ACCOUNTS_RESTS")) {

   %>
	
	<form action="../crm/finance/bk_accountsupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">
        <input type="hidden" name="id" value="<%= accountid %>">
	<table <%=Bean.getTableBottomFilter() %>>
		<tr>
			<td valign="top"><%= Bean.bk_accountXML.getfieldTransl("h_begin_period", false) %>&nbsp;<%=Bean.getCalendarInputField("begin_period", begin_period, "10") %>&nbsp;&nbsp;&nbsp;
			<%= Bean.bk_accountXML.getfieldTransl("h_end_period", false) %>&nbsp;<%=Bean.getCalendarInputField("end_period", end_period, "10") %>&nbsp;&nbsp;&nbsp;
			<%= Bean.bk_accountXML.getfieldTransl("h_find_string", false) %>&nbsp;
			<input type="text" name="find_rest" id="find_rest" size="30" value="<%=find_rest %>" class="inputfield" title="<%= Bean.buttonXML.getfieldTransl("find_string", false) %>">&nbsp;
			<%=Bean.getSubmitButtonAjax("../crm/finance/bk_accountspecs.jsp?id=" + accountid + "&rest_page=1&", "find", "updateForm") %>&nbsp;
			</td>
			<td>&nbsp;</td>
		</tr>
	</table>
	</form>
	<%= Bean.getCalendarScript("begin_period", false) %>
	<%= Bean.getCalendarScript("end_period", false) %>

	<%= account.getRestsHTML(begin_period, end_period, find_rest, l_rest_page_beg, l_rest_page_end) %>
<%} %>

<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("FINANCE_BK_ACCOUNTS_BANK_ACCOUNTS")) { %>
	<table <%=Bean.getTableBottomFilter() %>>
	  	<tr>
		<%= Bean.getFindHTML("bank_accnt_find", bank_accnt_find, "../crm/finance/bk_accountspecs.jsp?id=" + accountid + "&bank_accnt_page=1&") %>

		<td>&nbsp;</td>
	  	</tr>
	</table>
	<%= account.getBankAccountsHTML(bank_accnt_find, l_bank_accnt_page_beg, l_bank_accnt_page_end) %>
<% } %>

<% } %>
</div></div>
</body></html>
