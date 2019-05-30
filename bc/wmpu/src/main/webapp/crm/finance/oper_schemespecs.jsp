<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%@page import="bc.objects.bcFNOperSchemeObject"%>
<%@page import="java.util.HashMap"%><html>

<%= Bean.getLogOutScript(request) %>

<head>
	<%=Bean.getMetaContent() %>
	<%=Bean.getBottomFrameCSS() %>

</head>

<%

HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String pageFormName = "FINANCE_OPER_SCHEME";

Bean.setJspPageForTabName(pageFormName);

String tagTab = "_TAB_GENERAL";
String tagFind = "_FIND_DET";
String tagOper = "_OPERATIONS";

String id = Bean.getDecodeParam(parameters.get("id"));

if (id==null || "".equalsIgnoreCase(id)) {
	id = "";
}

String tab = Bean.getDecodeParam(parameters.get("tab"));
if (tab==null || "".equalsIgnoreCase(tab)) { tab = Bean.tabsHmGetValue(pageFormName + tagTab); }

String find_string = Bean.getDecodeParam(parameters.get("find_string"));

if (find_string==null) { 
	find_string = Bean.filtersHmGetValue(pageFormName + tagFind); 
} else if ("".equalsIgnoreCase(find_string)) {
	Bean.filtersHmSetValue(pageFormName + tagFind, find_string);
} else {
	Bean.filtersHmSetValue(pageFormName + tagFind, find_string);
}

//Обрабатываем номера страниц
String l_oper_page = Bean.getDecodeParam(parameters.get("oper_page"));
Bean.pageCheck(pageFormName + tagOper, l_oper_page);
String l_oper_page_beg = Bean.getFirstRowNumber(pageFormName + tagOper);
String l_oper_page_end = Bean.getLastRowNumber(pageFormName + tagOper);

if ("".equalsIgnoreCase(id)) { 
%>
	<%=Bean.getIDNotFoundMessage() %>
<% 
}
else { 
	Bean.tabsHmSetValue(pageFormName + tagTab, tab);
	bcFNOperSchemeObject scheme = new bcFNOperSchemeObject(id);

%>
<body>
<div id="div_tabsheet">

	<table <%=Bean.getTableMenuParam() %>>
		<tr>
			<%= Bean.getPageHeader() %>

			<% if (Bean.currentMenu.isCurrentTabAndEditPermitted("FINANCE_OPER_SCHEME_INFO")) { %>
				<%= Bean.getMenuButton("ADD", "../crm/finance/oper_schemeupdate.jsp?type=general&action=add&process=no", "", "") %>
				<%= Bean.getMenuButton("DELETE", "../crm/finance/oper_schemeupdate.jsp?id=" + id + "&type=general&action=remove&process=yes", Bean.oper_schemeXML.getfieldTransl("h_delete_scheme", false), scheme.getValue("ID_FN_OPER_SCHEME") + ": " + scheme.getValue("BEGIN_ACTION_DATE_FRMT") + " - " + scheme.getValue("END_ACTION_DATE_FRMT")) %>
			<% } %>

			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("FINANCE_OPER_SCHEME_OPER")) { %>
				<% if (Bean.currentMenu.isCurrentTabAndEditPermitted("FINANCE_OPER_SCHEME_OPER")) { %>
					<%= Bean.getMenuButton("ADD", "../crm/finance/oper_scheme_lineupdate.jsp?id_scheme=" + id + "&type=general&action=add&process=no", "", "") %>
				<% } %>
			    <!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagOper, "../crm/finance/oper_schemespecs.jsp?id=" +id + "&tab="+Bean.currentMenu.getTabID("FINANCE_OPER_SCHEME_OPER")+"&", "oper_page") %>
			<% } %>
	
		</tr>
	</table>
	<table <%=Bean.getTableMenuParam() %>>
		<%= Bean.getDetailCaption(scheme.getValue("ID_FN_OPER_SCHEME") + " - " + scheme.getValue("NAME_FN_OPER_SCHEME")) %>
		<tr>
			<td>
				<!-- Выводим перечень закладок -->
				<%= Bean.currentMenu.getFormTabScheeds(tab, "../crm/finance/oper_schemespecs.jsp?id=" + id) %>
			</td>

		</tr>
	</table>
</div>
<div id="div_data">
<div id="div_data_detail">

<% if (Bean.currentMenu.isCurrentTabAndEditPermitted("FINANCE_OPER_SCHEME_INFO")) {%> 
    <script>
		var formData = new Array (
			new Array ('cd_fn_oper_scheme', 'varchar2', 1),
			new Array ('name_fn_oper_scheme', 'varchar2', 1),
			new Array ('cd_fn_oper_state', 'varchar2', 1),
			new Array ('is_system_oper', 'varchar2', 1)
		);
		function myValidateForm() {
			return validateForm(formData);
		}

	</script>
	<form action="../crm/finance/oper_schemeupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return myValidateForm()">
	        <input type="hidden" name="type" value="general">
	        <input type="hidden" name="action" value="edit">
	        <input type="hidden" name="process" value="yes">
	        <input type="hidden" name="id" value="<%= id %>">
	
		<table <%=Bean.getTableDetailParam() %>>
        <tr>
			<td><%= Bean.oper_schemeXML.getfieldTransl("id_fn_oper_scheme", false) %></td> <td><input type="text" name="id_fn_oper_scheme" size="20" value="<%=scheme.getValue("ID_FN_OPER_SCHEME") %>" readonly="readonly" class="inputfield-ro"> </td>
			<td><%= Bean.clubXML.getfieldTransl("club", false) %>
				<%= Bean.getGoToClubLink(scheme.getValue("ID_CLUB")) %>
			</td><td><input type="text" name="name_club" size="30" value="<%= Bean.getClubShortName(scheme.getValue("ID_CLUB")) %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td><%= Bean.oper_schemeXML.getfieldTransl("cd_fn_oper_scheme", true) %></td> <td><input type="text" name="cd_fn_oper_scheme" size="74" value="<%=scheme.getValue("CD_FN_OPER_SCHEME") %>" class="inputfield"> </td>
			<td><%= Bean.oper_schemeXML.getfieldTransl("cd_fn_oper_exec_type", false) %></td> <td><input type="text" name="name_fn_oper_exec_type" size="35" value="<%=scheme.getValue("NAME_FN_OPER_EXEC_TYPE") %>" readonly="readonly" class="inputfield-ro"> </td>
		</tr>
		<tr>
			<td><%= Bean.oper_schemeXML.getfieldTransl("name_fn_oper_scheme", true) %></td> <td><input type="text" name="name_fn_oper_scheme" size="74" value="<%=scheme.getValue("NAME_FN_OPER_SCHEME") %>" class="inputfield"> </td>
			<td><%= Bean.oper_schemeXML.getfieldTransl("cd_fn_oper_state", true) %></td> <td><select name="cd_fn_oper_state" id="cd_fn_oper_state" class="inputfield"><%= Bean.getFNOperStateOptions(scheme.getValue("CD_FN_OPER_STATE"), true) %></select></td>
		</tr>
		<tr>
			<td><%= Bean.oper_schemeXML.getfieldTransl("desc_fn_oper_scheme", false)%></td><td><textarea name="desc_fn_oper_scheme" cols="70" rows="3" class="inputfield"><%=scheme.getValue("DESC_FN_OPER_SCHEME")%></textarea></td>
			<td><%= Bean.oper_schemeXML.getfieldTransl("is_system_oper", true) %></td> <td><select name="is_system_oper" id="is_system_oper" class="inputfield"><%= Bean.getMeaningFromLookupNameOptions("YES_NO", scheme.getValue("IS_SYSTEM_OPER"), true) %></select></td>
		</tr>
		<tr>
			<td><%= Bean.oper_schemeXML.getfieldTransl("note_fn_oper_scheme", false)%></td><td><textarea name="note_fn_oper_scheme" cols="70" rows="3" class="inputfield"><%=scheme.getValue("NOTE_FN_OPER_SCHEME")%></textarea></td>
			<td colspan="2">&nbsp;</td>
		</tr>
		<%=	Bean.getCreationAndMoficationRecordFields(
				scheme.getValue(Bean.getCreationDateFieldName()),
				scheme.getValue("CREATED_BY"),
				scheme.getValue(Bean.getLastUpdateDateFieldName()),
				scheme.getValue("LAST_UPDATE_BY")
			) %>
		<tr>
			<td colspan="4" align="center">
				<%=Bean.getSubmitButtonAjax("../crm/finance/oper_schemeupdate.jsp") %>
				<%=Bean.getResetButton() %>
				<%=Bean.getGoBackButton("../crm/finance/oper_scheme.jsp") %>
			</td>
		</tr>
	</table>
	</form>

<% } else if (Bean.currentMenu.isCurrentTabAndPreviewPermitted("FINANCE_OPER_SCHEME_INFO")) {%> 
		<table <%=Bean.getTableDetailParam() %>>
        <tr>
			<td><%= Bean.oper_schemeXML.getfieldTransl("id_bk_operation_scheme", false) %></td> <td><input type="text" name="id_bk_operation_scheme" size="20" value="<%=scheme.getValue("ID_BK_OPERATION_SCHEME") %>" readonly="readonly" class="inputfield-ro"> </td>
			<td><%= Bean.clubXML.getfieldTransl("club", false) %>
				<%= Bean.getGoToClubLink(scheme.getValue("ID_CLUB")) %>
			</td><td><input type="text" name="name_club" size="30" value="<%= Bean.getClubShortName(scheme.getValue("ID_CLUB")) %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td><%= Bean.oper_schemeXML.getfieldTransl("state_bk_operation_scheme", false) %></td> <td><input type="text" name="state_bk_operation_scheme" size="50" value="<%=scheme.getValue("STATE_BK_OPERATION_SCHEME_TSL") %>" readonly="readonly" class="inputfield-ro"> </td>
			<td><%= Bean.oper_schemeXML.getfieldTransl("begin_action_date", false) %></td> <td><input type="text" name="begin_action_date" size="20" value="<%=scheme.getValue("BEGIN_ACTION_DATE_FRMT") %>" readonly="readonly" class="inputfield-ro"> </td>
		</tr>
		<tr>
			<td><%= Bean.oper_schemeXML.getfieldTransl("id_bk_account_scheme", true) %></td> <td><select name="desc_bk_account_scheme"  class="inputfield"><%= scheme.getValue("desc_bk_account_scheme") %></select></td>
			<td><%= Bean.oper_schemeXML.getfieldTransl("end_action_date", false) %></td> <td><input type="text" name="end_action_date" size="20" value="<%=scheme.getValue("END_ACTION_DATE_FRMT") %>" readonly="readonly" class="inputfield-ro"> </td>
		</tr>
		<tr>
			<td><%=Bean.oper_schemeXML.getfieldTransl("desc_bk_operation_scheme", false)%></td><td  colspan="3"><textarea name="desc_bk_operation_scheme" cols="70" rows="3" readonly="readonly" class="inputfield-ro"><%=scheme.getValue("DESC_BK_OPERATION_SCHEME")%></textarea></td>
		</tr>
		<%=	Bean.getCreationAndMoficationRecordFields(
				scheme.getValue(Bean.getCreationDateFieldName()),
				scheme.getValue("CREATED_BY"),
				scheme.getValue(Bean.getLastUpdateDateFieldName()),
				scheme.getValue("LAST_UPDATE_BY")
			) %>

		<tr>
			<td colspan="4" align="center">
				<%=Bean.getGoBackButton("../crm/finance/oper_scheme.jsp") %>
			</td>
		</tr>
	</table>

<%   } 

if (Bean.currentMenu.isCurrentTabAndAccessPermitted("FINANCE_OPER_SCHEME_OPER")) { 
	
	String tagOperType = "_OPERATION_TYPE_GENERAL";

	String oper_type	= Bean.getDecodeParam(parameters.get("oper_type"));

	oper_type 		= Bean.checkFindString(pageFormName + tagOperType, oper_type, l_oper_page);
	
%> 		
	<table <%=Bean.getTableMenuParam() %>>
		<tr>
			<%= Bean.getFindHTML("find_string", find_string, "../crm/finance/oper_schemespecs.jsp?id=" + id + "&oper_page=1&") %>

			<%=Bean.getSelectOnChangeBeginHTML("oper_type", "../crm/finance/oper_schemespecs.jsp?id=" + id + "&oper_page=1", Bean.oper_schemeXML.getfieldTransl("name_bk_operation_type", false)) %>
				<%= Bean.getFNOperTypeOptions(oper_type, true) %>
			<%=Bean.getSelectOnChangeEndHTML() %>
		</tr>

	</table>
	<%= scheme.getLinesHTML(oper_type, find_string, l_oper_page_beg, l_oper_page_end) %>
<% }

}%>

</div></div>
</body>
</html>
