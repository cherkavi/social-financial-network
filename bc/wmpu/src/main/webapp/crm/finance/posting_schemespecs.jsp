<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%@page import="bc.objects.bcFNPostingSchemeObject"%>
<%@page import="java.util.HashMap"%><html>

<%= Bean.getLogOutScript(request) %>

<head>
	<%=Bean.getMetaContent() %>
	<%=Bean.getBottomFrameCSS() %>

</head>

<%

HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String pageFormName = "FINANCE_POSTING_SCHEME";

Bean.setJspPageForTabName(pageFormName);

String tagTab = "_TAB_GENERAL";
String tagFind = "_FIND_DET";
String tagLines = "_LINES";
String tagClubRel = "_CLUB_REL_GENERAL";
String tagOperType = "_OPERATION_TYPE_GENERAL";

String id = Bean.getDecodeParam(parameters.get("id"));

if (id==null || "".equalsIgnoreCase(id)) {
	id = "";
}

String tab = Bean.getDecodeParam(parameters.get("tab"));
if (tab==null || "".equalsIgnoreCase(tab)) { tab = Bean.tabsHmGetValue(pageFormName + tagTab); }

if ("".equalsIgnoreCase(id)) { 
%>
	<%=Bean.getIDNotFoundMessage() %>
<% 
}
else { 
	Bean.tabsHmSetValue(pageFormName + tagTab, tab);
	bcFNPostingSchemeObject scheme = new bcFNPostingSchemeObject(id);

	Bean.currentMenu.setExistFlag("FINANCE_POSTING_SCHEME_REPORTS",false);
	Bean.currentMenu.setExistFlag("FINANCE_POSTING_SCHEME_RELATIONSHIPS",false);
	Bean.currentMenu.setExistFlag("FINANCE_POSTING_SCHEME_LINES",true);
	if (Bean.currentMenu.isCurrentTab("FINANCE_POSTING_SCHEME_REPORTS") ||
			Bean.currentMenu.isCurrentTab("FINANCE_POSTING_SCHEME_RELATIONSHIPS")) {
		Bean.currentMenu.setFirstCurrentTab();
		tab = Bean.currentMenu.getCurrentTab();
		Bean.tabsHmSetValue(pageFormName, tab);
	}

	//Обрабатываем номера страниц
	String l_line_page = Bean.getDecodeParam(parameters.get("line_page"));
	Bean.pageCheck(pageFormName + tagLines, l_line_page);
	String l_line_page_beg = Bean.getFirstRowNumber(pageFormName + tagLines);
	String l_line_page_end = Bean.getLastRowNumber(pageFormName + tagLines);

	String find_string = Bean.getDecodeParam(parameters.get("find_string"));
	find_string 		= Bean.checkFindString(pageFormName + tagFind, find_string, l_line_page);

	String club_rel		= Bean.getDecodeParam(parameters.get("club_rel"));
	club_rel 		= Bean.checkFindString(pageFormName + tagClubRel, club_rel, l_line_page);

	String oper_type	= Bean.getDecodeParam(parameters.get("oper_type"));
	oper_type 		= Bean.checkFindString(pageFormName + tagOperType, oper_type, l_line_page);
%>
<body>
<div id="div_tabsheet">

	<table <%=Bean.getTableMenuParam() %>>
		<tr>
			<%= Bean.getPageHeader() %>

			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("FINANCE_POSTING_SCHEME_INFO")) { %>
				<%= Bean.getReportHyperLink("SOSR02", "ID_FN_POSTING_SCHEME=" + id) %>
			<% } %>
			<% if (Bean.currentMenu.isCurrentTabAndEditPermitted("FINANCE_POSTING_SCHEME_INFO")) { %>
				<%= Bean.getMenuButton("ADD", "../crm/finance/posting_schemeupdate.jsp?type=general&action=add&process=no", "", "") %>
				<%= Bean.getMenuButton("DELETE", "../crm/finance/posting_schemeupdate.jsp?id=" + id + "&type=general&action=remove&process=yes", Bean.posting_schemeXML.getfieldTransl("h_delete_scheme", false), scheme.getValue("ID_FN_POSTING_SCHEME") + ": " + scheme.getValue("BEGIN_ACTION_DATE_FRMT") + " - " + scheme.getValue("END_ACTION_DATE_FRMT")) %>
			<% } %>

			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("FINANCE_POSTING_SCHEME_LINES")) { %>
				<% if (Bean.currentMenu.isCurrentTabAndEditPermitted("FINANCE_POSTING_SCHEME_LINES")) { %>
					<%= Bean.getMenuButton("ADD", "../crm/finance/posting_scheme_lineupdate.jsp?id_scheme=" + id + "&type=general&action=add&process=no", "", "") %>
				<% } %>
			    <!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagLines, "../crm/finance/posting_schemespecs.jsp?id=" +id + "&tab="+Bean.currentMenu.getTabID("FINANCE_POSTING_SCHEME_LINES")+"&", "line_page") %>
			<% } %>
	
		</tr>
	</table>
	<table <%=Bean.getTableMenuParam() %>>
		<%= Bean.getDetailCaption(scheme.getValue("ID_FN_POSTING_SCHEME") + ": " + scheme.getValue("BEGIN_ACTION_DATE_FRMT") + " - " + scheme.getValue("END_ACTION_DATE_FRMT")) %>
		<tr>
			<td>
				<!-- Выводим перечень закладок -->
				<%= Bean.currentMenu.getFormTabScheeds(tab, "../crm/finance/posting_schemespecs.jsp?id=" + id) %>
			</td>

		</tr>
	</table>
</div>
<div id="div_data">
<div id="div_data_detail">

<% if (Bean.currentMenu.isCurrentTabAndEditPermitted("FINANCE_POSTING_SCHEME_INFO")) {%> 
    <script>
		var formData = new Array (
			new Array ('cd_fn_posting_scheme_state', 'varchar2', 1),
			new Array ('begin_action_date', 'varchar2', 1),
			new Array ('id_bk_account_scheme', 'varchar2', 1)
		);
		function myValidateForm() {
			return validateForm(formData);
		}

	</script>
	<form action="../crm/finance/posting_schemeupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return myValidateForm()">
	        <input type="hidden" name="type" value="general">
	        <input type="hidden" name="action" value="edit">
	        <input type="hidden" name="process" value="yes">
	        <input type="hidden" name="id" value="<%= id %>">
	
		<table <%=Bean.getTableDetailParam() %>>
        <tr>
			<td><%= Bean.posting_schemeXML.getfieldTransl("id_fn_posting_scheme", false) %></td> <td><input type="text" name="id_fn_posting_scheme" size="20" value="<%=scheme.getValue("ID_FN_POSTING_SCHEME") %>" readonly="readonly" class="inputfield-ro"> </td>
			<td><%= Bean.clubXML.getfieldTransl("club", false) %>
				<%= Bean.getGoToClubLink(scheme.getValue("ID_CLUB")) %>
			</td><td><input type="text" name="name_club" size="30" value="<%= Bean.getClubShortName(scheme.getValue("ID_CLUB")) %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td><%= Bean.posting_schemeXML.getfieldTransl("id_bk_account_scheme", true) %>
				<%= Bean.getGoToFinanceBKSchemeLink(scheme.getValue("ID_BK_ACCOUNT_SCHEME")) %>
			</td> <td><select name="id_bk_account_scheme"  class="inputfield"><%= Bean.getBKAccountSchemeOptions(scheme.getValue("id_bk_account_scheme"), true) %></select></td>
			<td><%= Bean.posting_schemeXML.getfieldTransl("begin_action_date", true) %></td><td><%=Bean.getCalendarInputField("begin_action_date", scheme.getValue("BEGIN_ACTION_DATE_FRMT"), "10") %></td>
		</tr>
		<tr>
			<td><%= Bean.posting_schemeXML.getfieldTransl("cd_fn_posting_scheme_state", true) %></td> <td><select name="cd_fn_posting_scheme_state"  class="inputfield"><%= Bean.getFNPostingSchemeStateOptions(scheme.getValue("CD_FN_POSTING_SCHEME_STATE"), true) %></select></td>
			<td><%= Bean.posting_schemeXML.getfieldTransl("end_action_date", false) %></td><td><%=Bean.getCalendarInputField("end_action_date", scheme.getValue("END_ACTION_DATE_FRMT"), "10") %></td>
		</tr>
		<tr>
			<td><%=Bean.posting_schemeXML.getfieldTransl("desc_fn_posting_scheme", false)%></td><td  colspan="3"><textarea name="desc_fn_posting_scheme" cols="70" rows="3" class="inputfield"><%=scheme.getValue("DESC_FN_POSTING_SCHEME")%></textarea></td>
		</tr>
		<%=	Bean.getCreationAndMoficationRecordFields(
				scheme.getValue(Bean.getCreationDateFieldName()),
				scheme.getValue("CREATED_BY"),
				scheme.getValue(Bean.getLastUpdateDateFieldName()),
				scheme.getValue("LAST_UPDATE_BY")
			) %>
		<tr>
			<td colspan="4" align="center">
				<%=Bean.getSubmitButtonAjax("../crm/finance/posting_schemeupdate.jsp") %>
				<%=Bean.getResetButton() %>
				<%=Bean.getGoBackButton("../crm/finance/posting_scheme.jsp") %>
			</td>
		</tr>
	</table>
	</form>

		<!-- Скрипт для втавки меню вибору дати -->
		<%= Bean.getCalendarScript("begin_action_date", false) %>
		<%= Bean.getCalendarScript("end_action_date", false) %>

<% } else if (Bean.currentMenu.isCurrentTabAndPreviewPermitted("FINANCE_POSTING_SCHEME_INFO")) {%> 
		<table <%=Bean.getTableDetailParam() %>>
        <tr>
			<td><%= Bean.posting_schemeXML.getfieldTransl("id_fn_posting_scheme", false) %></td> <td><input type="text" name="id_fn_posting_scheme" size="20" value="<%=scheme.getValue("ID_FN_POSTING_SCHEME") %>" readonly="readonly" class="inputfield-ro"> </td>
			<td><%= Bean.clubXML.getfieldTransl("club", false) %>
				<%= Bean.getGoToClubLink(scheme.getValue("ID_CLUB")) %>
			</td><td><input type="text" name="name_club" size="30" value="<%= Bean.getClubShortName(scheme.getValue("ID_CLUB")) %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td><%= Bean.posting_schemeXML.getfieldTransl("cd_fn_posting_scheme_state", false) %></td> <td><input type="text" name="name_fn_posting_scheme_state" size="50" value="<%=scheme.getValue("NAME_FN_POSTING_SCHEME_STATE") %>" readonly="readonly" class="inputfield-ro"> </td>
			<td><%= Bean.posting_schemeXML.getfieldTransl("begin_action_date", false) %></td> <td><input type="text" name="begin_action_date" size="20" value="<%=scheme.getValue("BEGIN_ACTION_DATE_FRMT") %>" readonly="readonly" class="inputfield-ro"> </td>
		</tr>
		<tr>
			<td><%= Bean.posting_schemeXML.getfieldTransl("id_bk_account_scheme", false) %>
				<%= Bean.getGoToFinanceBKSchemeLink(scheme.getValue("ID_BK_ACCOUNT_SCHEME")) %>
			</td> <td><input type="text" name="end_action_date" size="50" value="<%=scheme.getValue("DESC_BK_ACCOUNT_SCHEME") %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.posting_schemeXML.getfieldTransl("end_action_date", false) %></td> <td><input type="text" name="end_action_date" size="20" value="<%=scheme.getValue("END_ACTION_DATE_FRMT") %>" readonly="readonly" class="inputfield-ro"> </td>
		</tr>
		<tr>
			<td><%=Bean.posting_schemeXML.getfieldTransl("desc_fn_posting_scheme", false)%></td><td  colspan="3"><textarea name="desc_fn_posting_scheme" cols="70" rows="3" readonly="readonly" class="inputfield-ro"><%=scheme.getValue("DESC_FN_POSTING_SCHEME")%></textarea></td>
		</tr>
		<%=	Bean.getCreationAndMoficationRecordFields(
				scheme.getValue(Bean.getCreationDateFieldName()),
				scheme.getValue("CREATED_BY"),
				scheme.getValue(Bean.getLastUpdateDateFieldName()),
				scheme.getValue("LAST_UPDATE_BY")
			) %>

		<tr>
			<td colspan="4" align="center">
				<%=Bean.getGoBackButton("../crm/finance/posting_scheme.jsp") %>
			</td>
		</tr>
	</table>

<%   } 

if (Bean.currentMenu.isCurrentTabAndAccessPermitted("FINANCE_POSTING_SCHEME_LINES")) { 
	
	
%> 		
	<table <%=Bean.getTableBottomFilter() %>>
		<tr>
			<%= Bean.getFindHTML("find_string", find_string, "../crm/finance/posting_schemespecs.jsp?id=" + id + "&line_page=1&") %>

 			<%=Bean.getSelectOnChangeBeginHTML("club_rel", "../crm/finance/posting_schemespecs.jsp?id=" + id + "&line_page=1", Bean.posting_schemeXML.getfieldTransl("name_club_rel_type", false)) %>
				<%= Bean.getClubRelTypeOptions(club_rel, true) %>
			<%=Bean.getSelectOnChangeEndHTML() %>

			<%=Bean.getSelectOnChangeBeginHTML("oper_type", "../crm/finance/posting_schemespecs.jsp?id=" + id + "&line_page=1", Bean.posting_schemeXML.getfieldTransl("name_bk_operation_type", false)) %>
				<%= Bean.getBKOperationTypeShortOptions(oper_type, true) %>
			<%=Bean.getSelectOnChangeEndHTML() %>
		</tr>

	</table>
	<%= scheme.getLinesHTML(oper_type, club_rel, find_string, l_line_page_beg, l_line_page_end) %>
<% }

}%>

</div></div>
</body>
</html>
