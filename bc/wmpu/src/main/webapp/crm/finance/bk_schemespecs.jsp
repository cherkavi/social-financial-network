<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%@page import="bc.objects.bcFNBKSchemeObject"%>

<%@page import="java.util.HashMap"%>

<%= Bean.getLogOutScript(request) %>
<html>
<head>
	<link rel="stylesheet" type="text/css" href="../CSS/tablebottom.css">
	<%= Bean.getMetaContent() %>
	<%= Bean.getBottomFrameCSS() %>
</head>

<%

HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String pageFormName = "FINANCE_BK_SCHEME";

Bean.setJspPageForTabName(pageFormName);

String tagTab = "_TAB_GENERAL";
String tagLines = "_LINES_DETAIL";
String tagLineFind = "_LINE_FIND";
String tagLineGroup = "_LINE_GROUP";
String tagLineExist = "_LINE_EXIST";

String id = Bean.getDecodeParam(parameters.get("id"));
String tab = Bean.getDecodeParam(parameters.get("tab"));

if (id==null || "".equalsIgnoreCase(id)) { id=""; }

if (tab==null || "".equalsIgnoreCase(tab)) { tab = Bean.tabsHmGetValue(pageFormName + tagTab); }

if (id==null || "".equalsIgnoreCase(id)) {
%>
	<%=Bean.getIDNotFoundMessage() %>
<% 
}
else {
	Bean.tabsHmSetValue(pageFormName + tagTab, tab);
	bcFNBKSchemeObject setting = new bcFNBKSchemeObject(id);

	Bean.currentMenu.setExistFlag("FINANCE_BK_SCHEME_BK_ACCOUNTS",false);
	Bean.currentMenu.setExistFlag("FINANCE_BK_SCHEME_POSTING_SCHEME",false);
	Bean.currentMenu.setExistFlag("FINANCE_BK_SCHEME_LINES",true);
	if (Bean.currentMenu.isCurrentTab("FINANCE_BK_SCHEME_BK_ACCOUNTS") ||
			Bean.currentMenu.isCurrentTab("FINANCE_BK_SCHEME_POSTING_SCHEME")) {
		Bean.currentMenu.setFirstCurrentTab();
		tab = Bean.currentMenu.getCurrentTab();
		Bean.tabsHmSetValue(pageFormName, tab);
	}

	//Обрабатываем номера страниц
	String l_line_page = Bean.getDecodeParam(parameters.get("line_page"));
	Bean.pageCheck(pageFormName + tagLines, l_line_page);
	String l_line_page_beg = Bean.getFirstRowNumber(pageFormName + tagLines);
	String l_line_page_end = Bean.getLastRowNumber(pageFormName + tagLines);

	String line_find = Bean.getDecodeParam(parameters.get("line_find"));
	line_find = Bean.checkFindString(pageFormName + tagLineFind, line_find, l_line_page);

	String line_group = Bean.getDecodeParam(parameters.get("line_group"));
	line_group = Bean.checkFindString(pageFormName + tagLineGroup, line_group, l_line_page);

	String line_exist = Bean.getDecodeParam(parameters.get("line_exist"));
	line_exist = Bean.checkFindString(pageFormName + tagLineExist, line_exist, l_line_page);

%>
<body>
<div id="div_tabsheet">

<table <%=Bean.getTableMenuParam() %>>
		<tr>
			<%= Bean.getPageHeader() %>

			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("FINANCE_BK_SCHEME_INFO")) { %>
				<%= Bean.getReportHyperLink("SR_BK_ACC_SCHEME", "ID_BK_ACCOUNT_SCHEME=" + id) %>
				<% if (Bean.currentMenu.isCurrentTabAndEditPermitted("FINANCE_BK_SCHEME_INFO")) { %>
					<%= Bean.getMenuButton("ADD", "../crm/finance/bk_schemeupdate.jsp?id=" + id + "&type=general&action=add2&process=no", "", "") %>
					<%= Bean.getMenuButton("COPY", "../crm/finance/bk_schemeupdate.jsp?id=" + id + "&type=general&action=copy&process=no", "", "") %>
					<%= Bean.getMenuButton("DELETE", "../crm/finance/bk_schemeupdate.jsp?id=" + id + "&type=general&action=remove&process=yes", Bean.bk_schemeXML.getfieldTransl("h_delete_bk_scheme", false), setting.getValue("ID_BK_ACCOUNT_SCHEME") + " - " + setting.getValue("DESC_BK_ACCOUNT_SCHEME")) %>
				<% } %>
			<%} %>
	
			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("FINANCE_BK_SCHEME_LINES")){ %>
	    		<% if (Bean.currentMenu.isCurrentTabAndEditPermitted("FINANCE_BK_SCHEME_LINES")) { %>
					<%= Bean.getMenuButton("ADD", "../crm/finance/bk_scheme_lineupdate.jsp?id_scheme=" + id + "&type=participant&action=add&process=no", "", "") %>
				<% } %>
	
				<!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagLines, "../crm/finance/bk_schemespecs.jsp?id=" + id + "&tab="+Bean.currentMenu.getTabID("FINANCE_BK_SCHEME_LINES")+"&", "line_page") %>
			<% } %>

	
		</tr>
	</table>
	<table <%=Bean.getTableMenuParam() %>>
		<%= Bean.getDetailCaption(setting.getValue("ID_BK_ACCOUNT_SCHEME") + " - " + setting.getValue("DESC_BK_ACCOUNT_SCHEME")) %>
		<tr>
			<td>
				<!-- Выводим перечень закладок -->
				<%= Bean.currentMenu.getFormTabScheeds(tab, "../crm/finance/bk_schemespecs.jsp?id=" + id) %>
			</td>
		</tr>
	</table>
</div>
<div id="div_data">
<div id="div_data_detail">
<%
if (Bean.currentMenu.isCurrentTabAndEditPermitted("FINANCE_BK_SCHEME_INFO")) {
	%>

		<script>
			var formData = new Array (
				new Array ('state_bk_account_scheme', 'varchar2', 1),
				new Array ('begin_action_date', 'varchar2', 1)
			);
		</script>


        <form action="../crm/finance/bk_schemeupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">
			<input type="hidden" name="type" value="general">
	        <input type="hidden" name="action" value="edit">
	        <input type="hidden" name="process" value="yes">
	        <input type="hidden" name="id" value="<%= id %>">
		<table <%=Bean.getTableDetailParam() %>>
		<tr>
			<td><%= Bean.bk_schemeXML.getfieldTransl("id_bk_account_scheme", false) %></td> <td><input type="text" name="id_bk_account_scheme" size="20" value="<%= setting.getValue("ID_BK_ACCOUNT_SCHEME") %>" readonly="readonly" class="inputfield-ro"> </td>
 		    <td><%= Bean.clubXML.getfieldTransl("club", false) %>
				<%= Bean.getGoToClubLink(setting.getValue("ID_CLUB")) %>
			</td><td><input type="text" name="name_club" size="30" value="<%= Bean.getClubShortName(setting.getValue("ID_CLUB")) %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td><%= Bean.bk_schemeXML.getfieldTransl("state_bk_account_scheme", true) %></td> <td><select name="state_bk_account_scheme"  class="inputfield"><%= Bean.getMeaningFromLookupNameOptions("BK_OPERATION_SCHEME_STATE", setting.getValue("state_bk_account_scheme"), true) %></select></td>
			<td><%= Bean.bk_schemeXML.getfieldTransl("begin_action_date", true) %></td><td><%=Bean.getCalendarInputField("begin_action_date", setting.getValue("BEGIN_ACTION_DATE_FRMT"), "10") %></td>
		</tr>
		<tr>
			<td><%=Bean.bk_schemeXML.getfieldTransl("desc_bk_account_scheme", false)%></td><td><textarea name="desc_bk_account_scheme" cols="60" rows="3" class="inputfield"><%=setting.getValue("DESC_BK_ACCOUNT_SCHEME")%></textarea></td>
			<td><%= Bean.bk_schemeXML.getfieldTransl("end_action_date", false) %></td><td><%=Bean.getCalendarInputField("end_action_date", setting.getValue("END_ACTION_DATE_FRMT"), "10") %></td>
		</tr>
		<%=	Bean.getCreationAndMoficationRecordFields(
				setting.getValue(Bean.getCreationDateFieldName()),
				setting.getValue("CREATED_BY"),
				setting.getValue(Bean.getLastUpdateDateFieldName()),
				setting.getValue("LAST_UPDATE_BY")
			) %>
		<tr>
			<td colspan="6" align="center">
				<%=Bean.getSubmitButtonAjax("../crm/finance/bk_schemeupdate.jsp") %>
				<%=Bean.getResetButton() %>
				<%=Bean.getGoBackButton("../crm/finance/bk_scheme.jsp") %>
			</td>
		</tr>

	</table>

	</form>


		<!-- Скрипт для втавки меню вибору дати -->
		<%= Bean.getCalendarScript("begin_action_date", false) %>
		<%= Bean.getCalendarScript("end_action_date", false) %>

<% } else if (Bean.currentMenu.isCurrentTabAndPreviewPermitted("FINANCE_BK_SCHEME_INFO")) {

 %>
	<form>
	<table <%=Bean.getTableDetailParam() %>>
		<tr>
			<td><%= Bean.bk_schemeXML.getfieldTransl("id_bk_account_scheme", false) %></td> <td><input type="text" name="id_bk_account_scheme" size="20" value="<%=setting.getValue("ID_BK_ACCOUNT_SCHEME") %>" readonly="readonly" class="inputfield-ro"> </td>
 		    <td><%= Bean.clubXML.getfieldTransl("club", false) %>
				<%= Bean.getGoToClubLink(setting.getValue("ID_CLUB")) %>
			</td><td><input type="text" name="name_club" size="30" value="<%= Bean.getClubShortName(setting.getValue("ID_CLUB")) %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td><%= Bean.bk_schemeXML.getfieldTransl("state_bk_account_scheme", false) %></td> <td><input type="text" name="state_bk_account_scheme" size="50" value="<%=setting.getValue("STATE_BK_ACCOUNT_SCHEME_TSL") %>" readonly="readonly" class="inputfield-ro"> </td>
			<td><%= Bean.bk_schemeXML.getfieldTransl("begin_action_date", false) %></td> <td><input type="text" name="begin_action_date" size="20" value="<%=setting.getValue("BEGIN_ACTION_DATE_FRMT") %>" readonly="readonly" class="inputfield-ro"> </td>
		</tr>
		<tr>
			<td><%=Bean.bk_schemeXML.getfieldTransl("desc_bk_account_scheme", false)%></td><td><textarea name="desc_bk_account_scheme" cols="70" rows="3" readonly="readonly" class="inputfield-ro"><%=setting.getValue("DESC_BK_ACCOUNT_SCHEME")%></textarea></td>
			<td><%= Bean.bk_schemeXML.getfieldTransl("end_action_date", false) %></td> <td><input type="text" name="end_action_date" size="20" value="<%=setting.getValue("END_ACTION_DATE_FRMT") %>" readonly="readonly" class="inputfield-ro"> </td>
		</tr>
		<%=	Bean.getCreationAndMoficationRecordFields(
				setting.getValue(Bean.getCreationDateFieldName()),
				setting.getValue("CREATED_BY"),
				setting.getValue(Bean.getLastUpdateDateFieldName()),
				setting.getValue("LAST_UPDATE_BY")
			) %>
		<tr>
			<td colspan="6" align="center">
				<%=Bean.getGoBackButton("../crm/finance/bk_scheme.jsp") %>
			</td>
		</tr>
	</table>
	</form>
    
<% }

if (Bean.currentMenu.isCurrentTabAndAccessPermitted("FINANCE_BK_SCHEME_LINES")) { %>
	<table <%=Bean.getTableBottomFilter() %>>
	  	<tr>
		<%= Bean.getFindHTML("line_find", line_find, "../crm/finance/bk_schemespecs.jsp?id=" + id + "&line_page=1&") %>

		<%=Bean.getSelectOnChangeBeginHTML("line_group", "../crm/finance/bk_schemespecs.jsp?id=" + id + "&line_page=1", Bean.bk_schemeXML.getfieldTransl("is_group", false)) %>
			<%= Bean.getMeaningFromLookupNameOrderByNymberValueOptions("YES_NO", line_group, true) %>
		<%=Bean.getSelectOnChangeEndHTML() %>

		<%=Bean.getSelectOnChangeBeginHTML("line_exist", "../crm/finance/bk_schemespecs.jsp?id=" + id + "&line_page=1", Bean.bk_schemeXML.getfieldTransl("exist_flag", false)) %>
			<%= Bean.getMeaningFromLookupNameOrderByNymberValueOptions("YES_NO", line_exist, true) %>
		<%=Bean.getSelectOnChangeEndHTML() %>
	  	</tr>
	</table>
	<%= setting.getLinesHTML(line_find, line_group, line_exist, l_line_page_beg, l_line_page_end) %>
<%
}

 } %>
</div></div>
</body>
</html>
