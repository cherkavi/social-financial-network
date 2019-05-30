<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%= Bean.getLogOutScript(request) %>

<%@page import="java.util.HashMap"%>
<%@page import="bc.objects.bcLGObject"%>

<%@page import="bc.objects.bcLGPromoterPenaltyObject"%><html>
<head>
	<%= Bean.getMetaContent() %>
	<%= Bean.getBottomFrameCSS() %>
</head>
<body topmargin="0">
<div id="div_tabsheet">

<%

HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String pageFormName = "LOGISTIC_CLIENTS_PENALTY";

String tagWriteOff = "_WRITE_OFF";
String tagWriteOffFind = "_WRITE_OFF_FIND";


Bean.setJspPageForTabName(pageFormName);

String id = Bean.getDecodeParam(parameters.get("id"));
String	tab = Bean.getDecodeParam(parameters.get("tab"));
if (tab==null || "".equalsIgnoreCase(tab)) { 
	tab = Bean.tabsHmGetValue(pageFormName);
}

if (id==null || "".equals(id)) {
%>

	<%=Bean.getIDNotFoundMessage() %>

<% } else {
	Bean.tabsHmSetValue(pageFormName, tab);
	bcLGPromoterPenaltyObject penalty = new bcLGPromoterPenaltyObject(id);
	
	String l_write_off_page = Bean.getDecodeParam(parameters.get("write_off_page"));
	Bean.pageCheck(pageFormName + tagWriteOff, l_write_off_page);
	String l_write_off_page_beg = Bean.getFirstRowNumber(pageFormName + tagWriteOff);
	String l_write_off_page_end = Bean.getLastRowNumber(pageFormName + tagWriteOff);

	String write_off_find 	= Bean.getDecodeParam(parameters.get("write_off_find"));
	write_off_find 	= Bean.checkFindString(pageFormName + tagWriteOffFind, write_off_find, l_write_off_page);
%>

	<table <%=Bean.getTableMenuParam() %>>
		<tr>
			<%= Bean.getPageHeader() %>
			<% if (Bean.currentMenu.isCurrentTabAndEditPermitted("LOGISTIC_CLIENTS_PENALTY_INFO")) {%>
				<%= Bean.getMenuButton("ADD", "../crm/logistic/clients/penaltyupdate.jsp?id=" + id + "&type=general&action=add2&process=no", "", "") %>
				<%= Bean.getMenuButton("DELETE", "../crm/logistic/clients/penaltyupdate.jsp?id=" + id + "&type=general&action=remove&process=yes", Bean.logisticXML.getfieldTransl("h_action_penalty_delete", false), penalty.getValue("NAME_LG_PROMOTER") + " - " +  penalty.getValue("DATE_LG_PROMOTER_PENALTY_FRMT")) %>
			<% } %>

			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("LOGISTIC_CLIENTS_PENALTY_WRITE_OFF")) {%>
				<!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagWriteOff, "../crm/logistic/clients/penaltyspecs.jsp?id=" + id + "&tab="+Bean.currentMenu.getTabID("LOGISTIC_CLIENTS_PENALTY_WRITE_OFF") + "&", "write_off_page") %>
			<% } %>


		</tr>
	</table>
	<table <%=Bean.getTableMenuParam() %>>
		<%= Bean.getDetailCaption(penalty.getValue("NAME_LG_PROMOTER") + " - " +  penalty.getValue("DATE_LG_PROMOTER_PENALTY_FRMT")) %>
		<tr>
			<td>
				<!-- Выводим перечень закладок -->
				<%= Bean.currentMenu.getFormTabScheeds(tab, "../crm/logistic/clients/penaltyspecs.jsp?id=" + id) %>
			</td>
		</tr>
	</table>
</div>
<div id="div_data">
<div id="div_data_detail">
<%
   if (Bean.currentMenu.isCurrentTabAndEditPermitted("LOGISTIC_CLIENTS_PENALTY_INFO")) {
%>
	
		<script language="JavaScript">
			var formData = new Array (
				new Array ('cd_lg_promoter', 'varchar2', 1),
				new Array ('name_lg_promoter', 'varchar2', 1)
			);
			</script>

		  <form action="../crm/logistic/clients/penaltyupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onsubmit="return validateForm(formData);">
			<input type="hidden" name="action" value="edit">
	    	<input type="hidden" name="process" value="yes">
	    	<input type="hidden" name="type" value="general">
	    	<input type="hidden" name="id" value="<%= id %>">

		<table <%=Bean.getTableDetailParam() %>>

		<tr>
			<td><%= Bean.logisticXML.getfieldTransl("id_lg_promoter_penalty", false) %></td><td><input type="text" name="id_lg_promoter_penalty" size="20" value="<%=penalty.getValue("ID_LG_PROMOTER_PENALTY") %>" readonly="readonly" class="inputfield-ro"></td>
			<td colspan="2">&nbsp;</td>
		</tr>
 		<tr>
			<td><%= Bean.logisticXML.getfieldTransl("name_lg_promoter", true) %></td><td><input type="text" name="name_lg_promoter" size="60" value="<%= penalty.getValue("NAME_LG_PROMOTER") %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.logisticXML.getfieldTransl("date_lg_promoter_penalty", true) %></td><td><%=Bean.getCalendarInputField("date_lg_promoter_penalty", penalty.getValue("DATE_LG_PROMOTER_PENALTY_FRMT"), "16") %></td>
		</tr>
 		<tr>
			<td><%= Bean.logisticXML.getfieldTransl("reason_lg_promoter_penalty", true) %></td> <td><textarea name="reason_lg_promoter_penalty" cols="56" rows="3" class="inputfield"><%= penalty.getValue("REASON_LG_PROMOTER_PENALTY") %></textarea></td>
			<td><%= Bean.logisticXML.getfieldTransl("value_lg_promoter_penalty", true) %>, <%=penalty.getValue("SNAME_CURRENCY") %></td><td><input type="text" name="value_lg_promoter_penalty" size="20" value="<%= penalty.getValue("VALUE_LG_PROMOTER_PENALTY_FRMT") %>" class="inputfield"></td>
		</tr>
		<%=	Bean.getCreationAndMoficationRecordFields(
				penalty.getValue(Bean.getCreationDateFieldName()),
				penalty.getValue("CREATED_BY"),
				penalty.getValue(Bean.getLastUpdateDateFieldName()),
				penalty.getValue("LAST_UPDATE_BY")
			) %>
		<tr>
			<td colspan="6" align="center">
				<%=Bean.getSubmitButtonAjax("../crm/logistic/clients/penaltyupdate.jsp") %>
				<%=Bean.getResetButton() %>
				<%=Bean.getGoBackButton("../crm/logistic/clients/penalty.jsp") %>
			</td>
		</tr>
	</table>
	</form>
	<%= Bean.getCalendarScript("date_lg_promoter_penalty", true) %>


<% } else if (Bean.currentMenu.isCurrentTabAndPreviewPermitted("LOGISTIC_CLIENTS_PENALTY_INFO")) {
	%>
	
	  <form>
	<table <%=Bean.getTableDetailParam() %>>

		<tr>
			<td><%= Bean.logisticXML.getfieldTransl("id_lg_promoter", false) %></td><td><input type="text" name="id_lg_promoter" size="20" value="<%=penalty.getValue("ID_LG_PROMOTER") %>" readonly="readonly" class="inputfield-ro"></td>
			<td class="bottom_line"><%= Bean.clubXML.getfieldTransl("club", false) %>
				<%= Bean.getGoToClubLink(penalty.getValue("ID_CLUB")) %>
			</td><td class="bottom_line"><input type="text" name="name_club" size="50" value="<%= Bean.getClubShortName(penalty.getValue("ID_CLUB")) %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td><%= Bean.logisticXML.getfieldTransl("jur_prs", false) %>
					<%=Bean.getGoToJurPrsHyperLink(penalty.getValue("ID_JUR_PRS")) %>
			</td><td><input type="text" name="sname_jur_prs" size="50" value="<%=penalty.getValue("SNAME_JUR_PRS") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<%=	Bean.getCreationAndMoficationRecordFields(
				penalty.getValue(Bean.getCreationDateFieldName()),
				penalty.getValue("CREATED_BY"),
				penalty.getValue(Bean.getLastUpdateDateFieldName()),
				penalty.getValue("LAST_UPDATE_BY")
			) %>
	<tr>
		<td colspan="6" align="center">
			<%=Bean.getGoBackButton("../crm/logistic/clients/penalty.jsp") %>
		</td>
	</tr>
</table>
</form>

<% }

	if (Bean.currentMenu.isCurrentTabAndAccessPermitted("LOGISTIC_CLIENTS_PENALTY_WRITE_OFF")) { %> 
		<table <%= Bean.getTableBottomFilter() %>>
			<tr>
			<%= Bean.getFindHTML("write_off_find", write_off_find, "../crm/logistic/clients/penaltyspecs.jsp?id=" + id + "&write_off_page=1") %>
	
			<td>&nbsp;</td>
	
			</tr>
		</table> 
		<%= penalty.getPenaltyWriteOffHTML(write_off_find, l_write_off_page_beg, l_write_off_page_end) %> 
	<%}

}
		
%>
</div></div>
</body>

<%@page import="bc.objects.bcLGPromoterObject"%></html>
