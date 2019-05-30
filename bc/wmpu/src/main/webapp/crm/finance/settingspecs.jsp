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

String pageFormName = "FINANCE_SETTINGS";
String tagVAT = "_VAT";

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
	bcFinanceSettingsObject finance = new bcFinanceSettingsObject(accountid);
	
	//Обрабатываем номера страниц
	String l_vat_page = Bean.getDecodeParam(parameters.get("vat_page"));
	Bean.pageCheck(pageFormName + tagVAT, l_vat_page);
	String l_vat_page_beg = Bean.getFirstRowNumber(pageFormName + tagVAT);
	String l_vat_page_end = Bean.getLastRowNumber(pageFormName + tagVAT);
%>

	<table <%=Bean.getTableMenuParam() %>>
		<tr>
			<%= Bean.getPageHeader() %>

			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("FINANCE_SETTINGS_VAT")) { %>
			    <!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagVAT, "../crm/finance/settingspecs.jsp?id=" + accountid + "&tab="+Bean.currentMenu.getTabID("FINANCE_SETTINGS_VAT")+"&", "vat_page") %>
			<% } %>
	
		</tr>
	</table>
	<table <%=Bean.getTableMenuParam() %>>
		<%= Bean.getDetailCaption(finance.getValue("CD_BK_ACCOUNT") + " - " + finance.getValue("NAME_BK_ACCOUNT")) %>
		<tr>
			<td>
				<!-- Выводим перечень закладок -->
				<%= Bean.currentMenu.getFormTabScheeds(tab, "../crm/finance/settingspecs.jsp?id=" + accountid) %>
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

    <form action="../crm/finance/settingsupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">
        <input type="hidden" name="action" value="edit">
        <input type="hidden" name="process" value="yes">
        <input type="hidden" name="id" value="<%= accountid %>">
	<table <%=Bean.getTableDetailParam() %>>
		<tr>
			<td><%= Bean.bk_accountXML.getfieldTransl("cd_bk_account", true) %> </td>
			<td  colspan="3">
			<% //int bkCount = Integer.parseInt(Bean.club_bk_account_segments_count); 
    		
			bcClubShortObject club = new bcClubShortObject(Bean.getCurrentClubID());
			
			int i = 0;
			for (i=1; i<=Integer.parseInt(club.getValue("BK_ACCOUNT_SEGMENTS_COUNT")); i++) { %>
				<% if (i==1) {	%>
					<%=Bean.getBKAccountSegment("segment1", finance.getValue("SEGMENT1"), finance.getValue("SEGMENT1_PARTICIPANT")) %>
			    <% } else if (i==2) { %>	
					<%=Bean.getBKAccountSegment("segment2", finance.getValue("SEGMENT2"), finance.getValue("SEGMENT2_PARTICIPANT")) %>
				<% } else if (i==3) { %>	
					<%=Bean.getBKAccountSegment("segment3", finance.getValue("SEGMENT3"), finance.getValue("SEGMENT3_PARTICIPANT")) %>
				<% } else if (i==4) { %>	
					<%=Bean.getBKAccountSegment("segment4", finance.getValue("SEGMENT4"), finance.getValue("SEGMENT4_PARTICIPANT")) %>
				<% } else if (i==5) { %>	
					<%=Bean.getBKAccountSegment("segment5", finance.getValue("SEGMENT5"), finance.getValue("SEGMENT5_PARTICIPANT")) %>
				<% } else if (i==6) { %>	
					<%=Bean.getBKAccountSegment("segment6", finance.getValue("SEGMENT6"), finance.getValue("SEGMENT6_PARTICIPANT")) %>
				<% } else if (i==7) { %>	
					<%=Bean.getBKAccountSegment("segment7", finance.getValue("SEGMENT7"), finance.getValue("SEGMENT7_PARTICIPANT")) %>
				<% } else if (i==8) { %>	
					<%=Bean.getBKAccountSegment("segment8", finance.getValue("SEGMENT8"), finance.getValue("SEGMENT8_PARTICIPANT")) %>
				<% } else if (i==9) { %>	
					<%=Bean.getBKAccountSegment("segment9", finance.getValue("SEGMENT9"), finance.getValue("SEGMENT9_PARTICIPANT")) %>
				<% } else if (i==10) { %>	
					<%=Bean.getBKAccountSegment("segment10", finance.getValue("SEGMENT10"), finance.getValue("SEGMENT10_PARTICIPANT")) %>
				<% } else if (i==11) { %>	
					<%=Bean.getBKAccountSegment("segment11", finance.getValue("SEGMENT11"), finance.getValue("SEGMENT11_PARTICIPANT")) %>
				<% } else if (i==12) { %>	
					<%=Bean.getBKAccountSegment("segment12", finance.getValue("SEGMENT12"), finance.getValue("SEGMENT12_PARTICIPANT")) %>
				<% } else if (i==13) { %>	
					<%=Bean.getBKAccountSegment("segment13", finance.getValue("SEGMENT13"), finance.getValue("SEGMENT13_PARTICIPANT")) %>
				<% } else if (i==14) { %>	
					<%=Bean.getBKAccountSegment("segment14", finance.getValue("SEGMENT14"), finance.getValue("SEGMENT14_PARTICIPANT")) %>
				<% } else if (i==15) { %>	
					<%=Bean.getBKAccountSegment("segment15", finance.getValue("SEGMENT15"), finance.getValue("SEGMENT15_PARTICIPANT")) %>
			<% } 
			}%>
		</tr>
		<tr>
			<td><%= Bean.bk_accountXML.getfieldTransl("id_bk_account_scheme", false) %>
				<%= Bean.getGoToFinanceBKSchemeLink(finance.getValue("ID_BK_ACCOUNT_SCHEME_LINE")) %>
			</td> <td><select name="id_bk_account_scheme_line"  class="inputfield"><option value=""></option><%= Bean.getPostingSettingsOptions(finance.getValue("ID_BK_ACCOUNT_SCHEME_LINE"), false) %></select></td>
			<td><%= Bean.clubXML.getfieldTransl("club", false) %>
				<%= Bean.getGoToClubLink(finance.getValue("ID_CLUB")) %>
			</td><td><input type="text" name="name_club" size="30" value="<%= Bean.getClubShortName(finance.getValue("ID_CLUB")) %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td><%= Bean.bk_accountXML.getfieldTransl("name_bk_account", true) %></td> <td><input type="text" name="name_bk_account" size="90" value="<%= finance.getValue("NAME_BK_ACCOUNT") %>" class="inputfield"> </td>
			<td><%= Bean.bk_accountXML.getfieldTransl("is_group_tsl", false) %></td> <td><select name="is_group" class="inputfield"><%= Bean.getMeaningFromLookupNameOptions("YES_NO", finance.getValue("IS_GROUP"), false) %></select> </td>
		</tr>
		<tr>
			<td><%= Bean.bk_accountXML.getfieldTransl("internal_name_bk_account", true) %></td><td><input type="text" name="internal_name_bk_account" size="90" value="<%= finance.getValue("INTERNAL_NAME_BK_ACCOUNT") %>" class="inputfield"></td>
			<td><%= Bean.bk_accountXML.getfieldTransl("cd_bk_account_parent", false) %> </td> <td><select name="id_bk_account_parent"  class="inputfield"><%= Bean.getBKAccountsGroupOptions(finance.getValue("ID_BK_ACCOUNT_PARENT"), true) %></select></td>
		</tr>
		<tr>
			<td valign=top><%= Bean.bk_accountXML.getfieldTransl("desc_bk_account", false) %></td> <td valign=top><textarea name="desc_bk_account" cols="90" rows="4" class="inputfield"><%= finance.getValue("DESC_BK_ACCOUNT") %></textarea> </td>
			<td><%= Bean.bk_accountXML.getfieldTransl("exist_flag", false) %> </td><td><select name="exist_flag" class="inputfield"><%= Bean.getMeaningFromLookupNameOptions("YES_NO", finance.getValue("EXIST_FLAG"), false) %></select></td>
		</tr>
 		<tr>
			<td colspan="6" align="center">
				<%=Bean.getSubmitButtonAjax("../crm/finance/settingsupdate.jsp") %>
				<%=Bean.getResetButton() %>
				<%=Bean.getGoBackButton("../crm/finance/settings.jsp") %>
			</td>
		</tr>

	</table>

	</form>
<% } else if (Bean.currentMenu.isCurrentTabAndPreviewPermitted("FINANCE_BK_ACCOUNTS_INFO")) {

 %>
	<form>
	<table <%=Bean.getTableDetailParam() %>>
		<tr>
			<td><%= Bean.bk_accountXML.getfieldTransl("cd_bk_account", false) %> </td><td><input type="text" name="cd_bk_account" size="40" value="<%= finance.getValue("CD_BK_ACCOUNT") %>" readonly="readonly" class="inputfield-ro"></td>
			<td>&nbsp;</td> <td>&nbsp;</td>
		</tr>
		<tr>
			<td><%= Bean.bk_accountXML.getfieldTransl("id_bk_account_scheme", false) %>
				<%= Bean.getGoToFinanceBKSchemeLink(finance.getValue("ID_BK_ACCOUNT_SCHEME_LINE")) %>
			</td> <td><input type="text" name="name_bk_account_scheme_line" size="110" value="<%= finance.getValue("NAME_BK_ACCOUNT_SCHEME_LINE") %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.clubXML.getfieldTransl("club", false) %>
				<%= Bean.getGoToClubLink(finance.getValue("ID_CLUB")) %>
			</td><td><input type="text" name="name_club" size="30" value="<%= Bean.getClubShortName(finance.getValue("ID_CLUB")) %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td><%= Bean.bk_accountXML.getfieldTransl("name_bk_account", false) %></td> <td><input type="text" name="name_bk_account" size="110" value="<%= finance.getValue("NAME_BK_ACCOUNT") %>" readonly="readonly" class="inputfield-ro"> </td>
			<td><%= Bean.bk_accountXML.getfieldTransl("is_group_tsl", false) %></td> <td><input type="text" name="is_group_tsl" size="20" value="<%= finance.getValue("IS_GROUP_TSL") %>" readonly="readonly" class="inputfield-ro"> </td>
		</tr>
		<tr>
			<td><%= Bean.bk_accountXML.getfieldTransl("internal_name_bk_account", false) %> </td><td><input type="text" name="internal_name_bk_account" size="110" value="<%= finance.getValue("INTERNAL_NAME_BK_ACCOUNT") %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.bk_accountXML.getfieldTransl("cd_bk_account_parent", false) %> </td> <td><input type="text" name="cd_bk_account_parent" size="70" value="<%= finance.getValue("CD_BK_ACCOUNT_PARENT") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td><%= Bean.bk_accountXML.getfieldTransl("desc_bk_account", false) %></td> <td><textarea name="desc_bk_account" cols="110" rows="4" readonly="readonly" class="inputfield-ro"><%= finance.getValue("DESC_BK_ACCOUNT") %></textarea> </td>
			<td valign=top><%= Bean.bk_accountXML.getfieldTransl("exist_flag", false) %></td> <td valign=top><input type="text" name="exist_flag" size="70" value="<%= Bean.getMeaningFoCodeValue("YES_NO", finance.getValue("EXIST_FLAG")) %>" readonly="readonly" class="inputfield-ro"> </td>
		</tr>
 		<tr>
			<td colspan="6" align="center">
				<%=Bean.getGoBackButton("../crm/finance/settings.jsp") %>
			</td>
		</tr>
	</table>
	</form>
    
<% } %>

<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("FINANCE_SETTINGS_VAT")) { %>
	<%= finance.getVATParametersHTML(l_vat_page_beg, l_vat_page_end) %>
<% } %>

<% } %>
</div></div>
</body>
<%@page import="bc.objects.bcFinanceSettingsObject"%></html>
