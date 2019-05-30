<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%@page import="bc.objects.bcSAMObject"%>
<%@page import="java.util.HashMap"%><html>

<%= Bean.getLogOutScript(request) %>

<head>
	<%= Bean.getMetaContent() %>
	<%= Bean.getBottomFrameCSS() %>

</head>
<body>


<% 

HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String pageFormName = "CLIENTS_SAM";
String tagTerminals = "_ASSING_TERM";
String tagTerminalFind = "_ASSIGN_TERM_FIND";
String tagSAMStatus = "_ASSIGN_TERM_STATUS";
String tagLogistic = "_LOGISTIC";
String tagLogisticFind = "_LOGISTIC_FIND";

Bean.setJspPageForTabName(pageFormName);

String id = Bean.getDecodeParam(parameters.get("id"));

String	tab = Bean.getDecodeParam(parameters.get("tab"));
if (tab==null || "".equalsIgnoreCase(tab)) { tab = Bean.tabsHmGetValue(pageFormName); }

if (id==null || "".equalsIgnoreCase(id)) { 
%>

	<%=Bean.getIDNotFoundMessage() %>

<% }
else {
	Bean.tabsHmSetValue(pageFormName, tab);
	bcSAMObject sam = new bcSAMObject(id);

	//Обрабатываем номера страниц
	String l_history_page = Bean.getDecodeParam(parameters.get("history_page"));
	Bean.pageCheck(pageFormName + tagTerminals, l_history_page);
	String l_history_page_beg = Bean.getFirstRowNumber(pageFormName + tagTerminals);
	String l_history_page_end = Bean.getLastRowNumber(pageFormName + tagTerminals);

	String history_find 	= Bean.getDecodeParam(parameters.get("history_find"));
	history_find 	= Bean.checkFindString(pageFormName + tagTerminalFind, history_find, l_history_page);

	String history_status 	= Bean.getDecodeParam(parameters.get("history_status"));
	history_status 	= Bean.checkFindString(pageFormName + tagSAMStatus, history_status, l_history_page);

	String l_logistic_page = Bean.getDecodeParam(parameters.get("logistic_page"));
	Bean.pageCheck(pageFormName + tagLogistic, l_logistic_page);
	String l_logistic_page_beg = Bean.getFirstRowNumber(pageFormName + tagLogistic);
	String l_logistic_page_end = Bean.getLastRowNumber(pageFormName + tagLogistic);

	String logistic_find 	= Bean.getDecodeParam(parameters.get("logistic_find"));
	logistic_find 	= Bean.checkFindString(pageFormName + tagLogisticFind, logistic_find, l_logistic_page);
%>
<body>

<div id="div_tabsheet">
	<table <%=Bean.getTableMenuParam() %>>
		<tr>
			<%= Bean.getPageHeader() %>
			<% if (Bean.currentMenu.isCurrentTabAndEditPermitted("CLIENTS_SAM_INFO")) { %>
				<%= Bean.getMenuButton("ADD", "../crm/clients/samupdate.jsp?type=general&action=adddet&process=no&id=" + id, "", "") %>
			<% } %>
			
			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLIENTS_SAM_TERM_HISTORY")) { %>
			    <% if (Bean.currentMenu.isCurrentTabAndEditPermitted("CLIENTS_SAM_TERM_HISTORY")) { %>
				    <%= Bean.getMenuButtonBase("ADD", "../crm/clients/samupdate.jsp?id=" + id + "&type=term&action=add&process=no", "", "", "", "div_data_detail") %>
				<% } %>
				<!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagTerminals, "../crm/clients/samspecs.jsp?id=" + id + "&tab="+Bean.currentMenu.getTabID("CLIENTS_SAM_TERM_HISTORY")+"&", "history_page") %>
			<% } %>

			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLIENTS_SAM_LOGISTIC")) { %>
			    <!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagLogistic, "../crm/clients/samspecs.jsp?id=" + id + "&tab="+Bean.currentMenu.getTabID("CLIENTS_SAM_LOGISTIC")+"&", "logistic_page") %>
			<% } %>

		</tr>
	</table>
	<table <%=Bean.getTableMenuParam() %>>
	<tr>
		<td><i><%= sam.getValue("ID_SAM") %> - <%= sam.getValue("name_sam_type") %></i><br>
			<!-- Выводим перечень закладок -->
			<%= Bean.currentMenu.getFormTabScheeds(tab, "../crm/clients/samspecs.jsp?id=" + id) %>
		</td>

	</tr>
	</table>
</div>
<div id="div_data">
<div id="div_data_detail">
<%

if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLIENTS_SAM_INFO")) {
	boolean hasEditPerm = Bean.currentMenu.isCurrentTabAndEditPermitted("CLIENTS_SAM_INFO");
		 %>

	<%if (hasEditPerm) { %>
	<script>
		var formData = new Array (
			new Array ('cd_sam_type', 'varchar2', 1),
			new Array ('club_registration_date', 'varchar2', 1)
		);
	</script>

	<form action="../crm/clients/samupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">	        
	        <input type="hidden" name="action" value="edit">
	        <input type="hidden" name="process" value="yes">
	        <input type="hidden" name="id_sam" value="<%= sam.getValue("ID_SAM") %>">
	        
	<%} %>
	<table <%=Bean.getTableDetailParam() %>>
    <% if (hasEditPerm) {%>
		<tr>
			<td><%= Bean.samXML.getfieldTransl("id_sam", false) %> </td><td><input type="text" name="id_sam" size="20" value="<%= sam.getValue("ID_SAM") %>" readonly="readonly" class="inputfield-ro">&nbsp;<span><%= sam.getValue("ID_SAM_HEX") %> (HEX)</span></td>
 		    <td><%= Bean.clubXML.getfieldTransl("club", false) %>
				<%= Bean.getGoToClubLink(sam.getValue("ID_CLUB")) %>
			</td><td><input type="text" name="name_club" size="30" value="<%= Bean.getClubShortName(sam.getValue("ID_CLUB")) %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td><%= Bean.samXML.getfieldTransl("sam_serial_number", false) %></td><td><input type="text" name="sam_serial_number" size="20" value="<%= sam.getValue("SAM_SERIAL_NUMBER")%>" class="inputfield"></td>
			<td><%= Bean.samXML.getfieldTransl("club_registration_date", true) %></td><td><%=Bean.getCalendarInputField("club_registration_date", sam.getValue("CLUB_REGISTRATION_DATE_FRMT"), "10") %></td>
		</tr>
		<tr>
			<td><%= Bean.samXML.getfieldTransl("name_sam_type", true) %></td><td><select name="cd_sam_type" class="inputfield"><%= Bean.getSAMTypeOptions(sam.getValue("CD_SAM_TYPE"), true) %></select></td>
		    <td><%= Bean.samXML.getfieldTransl("nt_sam_last_ses", false) %></td><td><input type="text" name="nt_sam_last_ses" size="20" value="<%= sam.getValue("NT_SAM_LAST_SES") %>" class="inputfield"></td>
		</tr>
		<tr>
			<td><%= Bean.samXML.getfieldTransl("expiry_date", false) %></td><td><%=Bean.getCalendarInputField("expiry_date", sam.getValue("EXPIRY_DATE_FRMT"), "10") %></td>
			<td><%= Bean.samXML.getfieldTransl("nc", false) %> </td><td><input type="text" name="nc" size="20" value="<%= sam.getValue("NC") %>" class="inputfield"></td>
		</tr>
		<%=	Bean.getIdCreationAndMoficationRecordFields(
				sam.getValue("ID_SAM"),
				sam.getValue(Bean.getCreationDateFieldName()),
				sam.getValue("CREATED_BY"),
				sam.getValue(Bean.getLastUpdateDateFieldName()),
				sam.getValue("LAST_UPDATE_BY")
			) %>
		<tr>
			<td colspan="6" align="center">
				<%=Bean.getSubmitButtonAjax("../crm/clients/samupdate.jsp") %>
				<%=Bean.getResetButton() %>
				<%=Bean.getGoBackButton("../crm/clients/sam.jsp") %>
			</td>
		</tr>
	<% } else { %>
		<tr>
			<td><%= Bean.samXML.getfieldTransl("id_sam", false) %> </td><td><input type="text" name="id_sam" size="20" value="<%= sam.getValue("ID_SAM") %>" readonly="readonly" class="inputfield-ro">&nbsp;<span><%= sam.getValue("ID_SAM_HEX") %> (HEX)</span></td>
 		    <td><%= Bean.clubXML.getfieldTransl("club", false) %>
				<%= Bean.getGoToClubLink(sam.getValue("ID_CLUB")) %>
			</td><td><input type="text" name="name_club" size="30" value="<%= Bean.getClubShortName(sam.getValue("ID_CLUB")) %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td><%= Bean.samXML.getfieldTransl("sam_serial_number", false) %></td><td><input type="text" name="sam_serial_number" size="20" value="<%= sam.getValue("SAM_SERIAL_NUMBER")%>" readonly="readonly" class="inputfield-ro"></td>
		    <td><%= Bean.samXML.getfieldTransl("club_registration_date", false) %></td><td><input type="text" name="club_registration_date" size="20" value="<%= sam.getValue("CLUB_REGISTRATION_DATE_FRMT") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td><%= Bean.samXML.getfieldTransl("name_sam_type", false) %></td><td><input type="text" name="name_sam_type" size="20" value="<%= sam.getValue("NAME_SAM_TYPE") %>" readonly="readonly" class="inputfield-ro"></td>
		    <td><%= Bean.samXML.getfieldTransl("nt_sam_last_ses", false) %></td><td><input type="text" name="nt_sam_last_ses" size="20" value="<%= sam.getValue("NT_SAM_LAST_SES") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
		    <td><%= Bean.samXML.getfieldTransl("expiry_date", false) %></td><td><input type="text" name="expiry_date" size="20" value="<%= sam.getValue("EXPIRY_DATE_FRMT") %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.samXML.getfieldTransl("nc", false) %> </td><td><input type="text" name="nc" size="20" value="<%= sam.getValue("NC") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<%=	Bean.getIdCreationAndMoficationRecordFields(
				sam.getValue("ID_SAM"),
				sam.getValue(Bean.getCreationDateFieldName()),
				sam.getValue("CREATED_BY"),
				sam.getValue(Bean.getLastUpdateDateFieldName()),
				sam.getValue("LAST_UPDATE_BY")
			) %>
		<tr>
			<td colspan="6" align="center">
				<%=Bean.getGoBackButton("../crm/clients/sam.jsp") %>
			</td>
		</tr>
		<% } %>

		</table>
		</form>
	
	<% if (hasEditPerm) { %>
	<!-- Скрипт для втавки меню вибору дати -->
		<%= Bean.getCalendarScript("expiry_date", false) %>
		<%= Bean.getCalendarScript("club_registration_date", false) %>
	<%} %>


<%  }

	if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLIENTS_SAM_TERM_HISTORY")) { %>
	<table <%= Bean.getTableBottomFilter() %>>
		<tr>
		<%= Bean.getFindHTML("history_find", history_find, "../crm/clients/samspecs.jsp?id=" + id + "&history_page=1") %>

		<%=Bean.getSelectOnChangeBeginHTML("history_status", "../crm/clients/samspecs.jsp?id=" + id + "&history_page=1", Bean.samXML.getfieldTransl("name_sam_status", false)) %>
			<%= Bean.getSAMStatusOptions(history_status, true) %>
		<%=Bean.getSelectOnChangeEndHTML() %>
		</tr>
	</table>
		<%=sam.getTermSAMHTML(history_find, history_status, l_history_page_beg, l_history_page_end) %>
<%	}
	
	if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLIENTS_SAM_LOGISTIC")) { %>
	<table <%= Bean.getTableBottomFilter() %>>
		<tr>
		<%= Bean.getFindHTML("logistic_find", logistic_find, "../crm/clients/samspecs.jsp?id=" + id + "&logistic_page=1") %>

		<td>&nbsp;</td>
		</tr>
	</table>
		<%=sam.getLogisticHTML(logistic_find, l_logistic_page_beg, l_logistic_page_end) %>
<%	}
	
} %>
</div></div>
</body>
</html>
