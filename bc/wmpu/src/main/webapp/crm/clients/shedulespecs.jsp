<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%@page import="bc.objects.bcLoySheduleObject"%>
<%@page import="java.util.HashMap"%><html>

<%= Bean.getLogOutScript(request) %>

<head>
	<%= Bean.getMetaContent() %>
	<%= Bean.getBottomFrameCSS() %>
</head>
<body topmargin="0">
<div id="div_tabsheet">
<%

HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String pageFormName = "CLIENTS_SHEDULE";

Bean.setJspPageForTabName(pageFormName);

String tagLines = "_LINES";
String tagLineFind = "_LINE_FIND";
String tagLineType = "_LINE_TYPE";
String tagTerminals = "_TERMINALS";
String tagTerminalFind = "_TERMINAL_FIND";
String tagTerminalType = "_TERMINAL_TYPE";
String tagTerminalStatus = "_TERMINAL_STATUS";

String shedName_id = Bean.getDecodeParam(parameters.get("id"));
String tab = Bean.getDecodeParam(parameters.get("tab"));
if (tab==null || "".equalsIgnoreCase(tab)) { tab = Bean.tabsHmGetValue(pageFormName); }
if (shedName_id==null || "".equalsIgnoreCase(shedName_id)) {
%>
	<%=Bean.getIDNotFoundMessage() %>
<% 
} else {
	Bean.tabsHmSetValue(pageFormName, tab);
	bcLoySheduleObject shedule = new bcLoySheduleObject(shedName_id);
	
	//Обрабатываем номера страниц
	String l_lines_page = Bean.getDecodeParam(parameters.get("lines_page"));
	Bean.pageCheck(pageFormName + tagLines, l_lines_page);
	String l_lines_page_beg = Bean.getFirstRowNumber(pageFormName + tagLines);
	String l_lines_page_end = Bean.getLastRowNumber(pageFormName + tagLines);
	
	String line_find 	= Bean.getDecodeParam(parameters.get("line_find"));
	line_find 	= Bean.checkFindString(pageFormName + tagLineFind, line_find, l_lines_page);
	
	String line_type 	= Bean.getDecodeParam(parameters.get("line_type"));
	line_type 	= Bean.checkFindString(pageFormName + tagLineType, line_type, l_lines_page);
	
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
	
%>

	<table <%=Bean.getTableMenuParam() %>>
		<tr>
			<%= Bean.getPageHeader() %>

			<% if (Bean.currentMenu.isCurrentTabAndEditPermitted("CLIENTS_SHEDULE_INFO")) { %>
			    <%= Bean.getMenuButton("ADD", "../crm/clients/sheduleupdate.jsp?id=" + shedName_id + "&type=general&action=add2&process=no", "", "") %>
			    <%= Bean.getMenuButton("DELETE", "../crm/clients/sheduleupdate.jsp?id=" + shedName_id + "&type=general&action=remove&process=yes", Bean.sheduleXML.getfieldTransl("h_shedulelines_delete", false), shedule.getValue("NAME_SHEDULE")) %>
			<%  } %>
	
			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLIENTS_SHEDULE_LINES")){ %>	
				<% if (Bean.currentMenu.isCurrentTabAndEditPermitted("CLIENTS_SHEDULE_LINES")){ %>	
				    <%= Bean.getMenuButton("ADD", "../crm/clients/sheduleupdate.jsp?id=" + shedName_id + "&line=0&type=line&action=add&process=no", "", "") %>
				<% } %>
			    <!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagLines, "../crm/clients/shedulespecs.jsp?id=" + shedName_id + "&tab="+Bean.currentMenu.getTabID("CLIENTS_SHEDULE_LINES")+"&", "lines_page") %>
			<% } %>
	
			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLIENTS_SHEDULE_TERM")){ %>
			    <!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagTerminals, "../crm/clients/shedulespecs.jsp?id=" + shedName_id + "&tab="+Bean.currentMenu.getTabID("CLIENTS_SHEDULE_TERM")+"&", "terminals_page") %>
			<% } %>
		</tr>
	</table>
	<table <%=Bean.getTableMenuParam() %>>
		<%= Bean.getDetailCaption(shedule.getValue("NAME_SHEDULE")) %>
		<tr>
			<td>
				<!-- Выводим перечень закладок -->
				<%= Bean.currentMenu.getFormTabScheeds(tab, "../crm/clients/shedulespecs.jsp?id=" + shedName_id) %>
			</td>
		</tr>
	</table>
</div>
<div id="div_data">
<div id="div_data_detail">
<%
if (Bean.currentMenu.isCurrentTabAndPreviewPermitted("CLIENTS_SHEDULE_INFO")) {

 %>
	<table <%=Bean.getTableDetailParam() %>>
		<tr>
			<td><%= Bean.sheduleXML.getfieldTransl("name_shedule", false) %></td> <td><input type="text" name="name_shedule" size="60" value="<%= shedule.getValue("NAME_SHEDULE") %>" readonly="readonly" class="inputfield-ro"> </td>
			<td><%= Bean.clubXML.getfieldTransl("club", false) %>
					<%= Bean.getGoToClubLink(shedule.getValue("ID_CLUB")) %>
			</td><td><input type="text" name="name_club" size="40" value="<%= Bean.getClubShortName(shedule.getValue("ID_CLUB")) %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td rowspan="3"><%= Bean.sheduleXML.getfieldTransl("desc_shedule", false) %></td><td rowspan="3"><textarea name="desc_shedule" cols="57" rows="3" readonly="readonly" class="inputfield-ro"><%= shedule.getValue("DESC_SHEDULE") %></textarea></td>
			<td><%= Bean.sheduleXML.getfieldTransl("date_beg", false) %> </td> <td><input type="text" name="date_beg" size="10" value="<%= shedule.getValue("DATE_BEG_FRMT") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td><%= Bean.sheduleXML.getfieldTransl("date_end", false) %> </td> <td><input type="text" name="date_end" size="10" value="<%= shedule.getValue("DATE_END_FRMT") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td colspan="2">&nbsp;</td>
		</tr>
		<tr>
			<td><%= Bean.sheduleXML.getfieldTransl("name_loyality_scheme_default", false) %>
				<%= Bean.getGoToLoyalityLink(shedule.getValue("ID_LOYALITY_SCHEME_DEFAULT")) %>
			</td> <td><input type="text" name="name_loyality_scheme_default" size="60" value="<%= shedule.getValue("NAME_LOYALITY_SCHEME_DEFAULT") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td colspan="2">&nbsp;</td>
		</tr>
		<%=	Bean.getIdCreationAndMoficationRecordFields(
				shedule.getValue("ID_SHEDULE"),
				shedule.getValue(Bean.getCreationDateFieldName()),
				shedule.getValue("CREATED_BY"),
				shedule.getValue(Bean.getLastUpdateDateFieldName()),
				shedule.getValue("LAST_UPDATE_BY")
			) %>
 		<tr>
			<td colspan="6" align="center">
				<%=Bean.getGoBackButton("../crm/clients/shedule.jsp") %>
			</td>
		</tr>
	</table>

<% } else if (Bean.currentMenu.isCurrentTabAndEditPermitted("CLIENTS_SHEDULE_INFO")) { %>
	<script>
		var formData = new Array (
			new Array ('name_shedule', 'varchar2', 1),
			new Array ('name_loyality_scheme_default', 'varchar2', 1),
			new Array ('date_beg', 'varchar2', 1)
		);
	</script> 
    <form action="../crm/clients/sheduleupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">
        <input type="hidden" name="action" value="edit">
        <input type="hidden" name="process" value="yes">
        <input type="hidden" name="type" value="general">
        <input type="hidden" name="id" value="<%= shedName_id %>">
	<table <%=Bean.getTableDetailParam() %>>
		<tr>
			<td><%= Bean.sheduleXML.getfieldTransl("name_shedule", true) %> </td> <td><input type="text" name="name_shedule" size="60" value="<%= shedule.getValue("NAME_SHEDULE") %>" class="inputfield"> </td>
			<td><%= Bean.clubXML.getfieldTransl("club", false) %>
					<%= Bean.getGoToClubLink(shedule.getValue("ID_CLUB")) %>
			</td><td><input type="text" name="name_club" size="40" value="<%= Bean.getClubShortName(shedule.getValue("ID_CLUB")) %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td rowspan="3"><%= Bean.sheduleXML.getfieldTransl("desc_shedule", false) %></td><td rowspan="3"><textarea name="desc_shedule" cols="57" rows="3" class="inputfield"><%= shedule.getValue("DESC_SHEDULE") %></textarea></td>
			<td><%= Bean.sheduleXML.getfieldTransl("date_beg", true) %> </td><td><%=Bean.getCalendarInputField("date_beg", shedule.getValue("DATE_BEG_FRMT"), "10") %></td>
		</tr>
		<tr>
			<td valign="top"><%= Bean.sheduleXML.getfieldTransl("date_end", false) %></td><td valign="top"><%=Bean.getCalendarInputField("date_end", shedule.getValue("DATE_END_FRMT"), "10") %></td>
		</tr>
		<tr>
			<td colspan="2">&nbsp;</td>
		</tr>
		<tr>
			<td><%= Bean.sheduleXML.getfieldTransl("name_loyality_scheme_default", true) %>
				<%= Bean.getGoToLoyalityLink(shedule.getValue("ID_LOYALITY_SCHEME_DEFAULT")) %>
			</td>
			<td>
				<%=Bean.getWindowFindLoyScheme("loyality_scheme_default", shedule.getValue("ID_LOYALITY_SCHEME_DEFAULT"), "50", false) %>
			</td>			
		</tr>
		<%=	Bean.getIdCreationAndMoficationRecordFields(
				shedule.getValue("ID_SHEDULE"),
				shedule.getValue(Bean.getCreationDateFieldName()),
				shedule.getValue("CREATED_BY"),
				shedule.getValue(Bean.getLastUpdateDateFieldName()),
				shedule.getValue("LAST_UPDATE_BY")
			) %>
		<tr>
			<td colspan="2">&nbsp;</td>
		</tr>
 		<tr>
			<td colspan="6" align="center">
				<%=Bean.getSubmitButtonAjax("../crm/clients/sheduleupdate.jsp") %>
				<%=Bean.getResetButton() %>
				<%=Bean.getGoBackButton("../crm/clients/shedule.jsp") %>
			</td>
		</tr>

	</table>

	</form>	
	<!-- Скрипт для втавки меню вибору дати -->
		<%= Bean.getCalendarScript("date_beg", false) %>
		<%= Bean.getCalendarScript("date_end", false) %>
<% }
if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLIENTS_SHEDULE_LINES")) {%> 
		<table <%=Bean.getTableBottomFilter() %>><tbody>
		<tr>
			<%=Bean.getFindHTML("line_find", line_find, "../crm/clients/shedulespecs.jsp?id=" + shedName_id + "&lines_page=1") %>
		
			<%=Bean.getSelectOnChangeBeginHTML("line_type", "../crm/clients/shedulespecs.jsp?id=" + shedName_id + "&lines_page=1", Bean.shedulelineXML.getfieldTransl("type_schedule", false)) %>
				<%=Bean.getSelectOptionHTML(line_type, "", "") %>
				<%=Bean.getSelectOptionHTML(line_type, "DAY_IN_MONTH", Bean.shedulelineXML.getfieldTransl("DAY_IN_MONTH_H", false)) %>
				<%=Bean.getSelectOptionHTML(line_type, "DAYS_INTERVAL", Bean.shedulelineXML.getfieldTransl("DAYS_INTERVAL", false)) %>
				<%=Bean.getSelectOptionHTML(line_type, "EVERY_DAY", Bean.shedulelineXML.getfieldTransl("EVERY_DAY", false)) %>
				<%=Bean.getSelectOptionHTML(line_type, "EVERY_WEEK", Bean.shedulelineXML.getfieldTransl("EVERY_WEEK", false)) %>
			<%=Bean.getSelectOnChangeEndHTML() %>
		</tr>
		</tbody>
		</table>
	<%= shedule.getSheduleLinesHTML(line_find, line_type, l_lines_page_beg, l_lines_page_end) %> 
<%}
if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLIENTS_SHEDULE_TERM")) {%> 
		<table <%=Bean.getTableBottomFilter() %>><tbody>
		<tr>
			<%=Bean.getFindHTML("terminal_find", terminal_find, "../crm/clients/shedulespecs.jsp?id=" + shedName_id + "&terminals_page=1") %>

			<%=Bean.getSelectOnChangeBeginHTML("terminal_type", "../crm/clients/shedulespecs.jsp?id=" + shedName_id + "&terminals_page=1", Bean.terminalXML.getfieldTransl("cd_term_type", false)) %>
				<%= Bean.getTermTypeOptions(terminal_type, true) %>
			<%=Bean.getSelectOnChangeEndHTML() %>

			<%=Bean.getSelectOnChangeBeginHTML("terminal_status", "../crm/clients/shedulespecs.jsp?id=" + shedName_id + "&terminals_page=1", Bean.terminalXML.getfieldTransl("cd_term_status", false)) %>
				<%= Bean.getTermStatusOptions(terminal_status, true) %>
			<%=Bean.getSelectOnChangeEndHTML() %>
		</tr>
		</tbody>
		</table>
	<%= shedule.getTerminalsHTML(terminal_find, terminal_type, terminal_status, l_terminals_page_beg, l_terminals_page_end) %> 
<%}

%>

<%   } %>
</div></div>
</body>
</html>
