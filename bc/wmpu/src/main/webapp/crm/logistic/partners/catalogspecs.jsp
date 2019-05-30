<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%@page import="java.util.HashMap"%>
<%@page import="bc.objects.bcLGCatalogObject"%>

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

String pageFormName = "LOGISTIC_PARTNERS_CATALOG";
String tagTransferData = "_TRANSFER_DATA";
String tagTransferDataFind = "_TRANSFER_DATA_FIND";

Bean.setJspPageForTabName(pageFormName);

String id = Bean.getDecodeParam(parameters.get("id"));
String filtr = Bean.getDecodeParam(parameters.get("filtr"));
String	tab = Bean.getDecodeParam(parameters.get("tab"));
if (tab==null || "".equalsIgnoreCase(tab)) { 
	tab = Bean.tabsHmGetValue(pageFormName);
}

if (id==null || "".equals(id)) {
%>

	<%=Bean.getIDNotFoundMessage() %>

<% } else {
	Bean.tabsHmSetValue(pageFormName, tab);
	bcLGCatalogObject lgCatalog = new bcLGCatalogObject(id);

	//Обрабатываем номера страниц
	String l_transfer_page = Bean.getDecodeParam(parameters.get("transfer_page"));
	Bean.pageCheck(pageFormName + tagTransferData, l_transfer_page);
	String l_transfer_page_beg = Bean.getFirstRowNumber(pageFormName + tagTransferData);
	String l_transfer_page_end = Bean.getLastRowNumber(pageFormName + tagTransferData);

	String transfer_find 	= Bean.getDecodeParam(parameters.get("transfer_find"));
	transfer_find 	= Bean.checkFindString(pageFormName + tagTransferDataFind, transfer_find, l_transfer_page);
%>

	<table <%=Bean.getTableMenuParam() %>>
		<tr>
			<%= Bean.getPageHeader() %>
			<% if (Bean.currentMenu.isCurrentTabAndEditPermitted("LOGISTIC_PARTNERS_CATALOG_INFO")) {%>
				<%= Bean.getMenuButton("ADD", "../crm/logistic/partners/catalogupdate.jsp?id=" + lgCatalog.getValue("ID_LG_PRODUCTION") + "&type=general&action=add2&process=no", "", "") %>
				<%= Bean.getMenuButton("DELETE", "../crm/logistic/partners/catalogupdate.jsp?id=" + lgCatalog.getValue("ID_LG_PRODUCTION") + "&type=general&action=remove&process=yes", Bean.logisticXML.getfieldTransl("h_production_delete", false), lgCatalog.getValue("ID_LG_PRODUCTION") + " - " +  lgCatalog.getValue("NAME_LG_PRODUCTION")) %>
			<% } %>

			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("LOGISTIC_PARTNERS_CATALOG_TRANSFER_DATA")) { %>
			    <!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagTransferData, "../crm/logistic/partners/catalogspecs.jsp?id=" + lgCatalog.getValue("ID_LG_PRODUCTION") + "&tab="+Bean.currentMenu.getTabID("LOGISTIC_PARTNERS_CATALOG_TRANSFER_DATA") + "&", "transfer_page") %>
			<% } %>

		</tr>
	</table>
	<table <%=Bean.getTableMenuParam() %>>
		<%= Bean.getDetailCaption(lgCatalog.getValue("ID_LG_PRODUCTION") + " - " + lgCatalog.getValue("NAME_LG_PRODUCTION")) %>
		<tr>
			<td>
				<!-- Выводим перечень закладок -->
				<%= Bean.currentMenu.getFormTabScheeds(tab, "../crm/logistic/partners/catalogspecs.jsp?id=" + lgCatalog.getValue("ID_LG_PRODUCTION")) %>
			</td>
		</tr>
	</table>
</div>
<div id="div_data">
<div id="div_data_detail">
<%
if (Bean.currentMenu.isCurrentTabAndAccessPermitted("LOGISTIC_PARTNERS_CATALOG_INFO")) {
%>
	
		<script language="JavaScript">
			var formData = new Array (
				new Array ('action_date', 'varchar2', 1)
			);
			</script>

		<% if (Bean.currentMenu.isCurrentTabAndEditPermitted("LOGISTIC_PARTNERS_CATALOG_INFO")) { %>
		  <form action="../crm/logistic/partners/catalogupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onsubmit="return validateForm(formData);">
			<input type="hidden" name="action" value="edit">
	    	<input type="hidden" name="process" value="yes">
	    	<input type="hidden" name="type" value="general">
	    	<input type="hidden" name="id" value="<%= lgCatalog.getValue("ID_LG_PRODUCTION") %>">
		<%} %>

		<table <%=Bean.getTableDetailParam() %>>

		<% if (Bean.currentMenu.isCurrentTabAndEditPermitted("LOGISTIC_PARTNERS_CATALOG_INFO")) {%>
		<tr>
			<td><%= Bean.logisticXML.getfieldTransl("id_lg_production", false) %></td><td><input type="text" name="id_lg_production" size="20" value="<%= lgCatalog.getValue("ID_LG_PRODUCTION") %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.clubXML.getfieldTransl("club", false) %>
				<%= Bean.getGoToClubLink(lgCatalog.getValue("ID_CLUB")) %>
			</td><td><input type="text" name="name_club" size="50" value="<%= Bean.getClubShortName(lgCatalog.getValue("ID_CLUB")) %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td><%= Bean.logisticXML.getfieldTransl("cd_lg_production", false) %></td><td><input type="text" name="cd_lg_production" size="65" value="<%= lgCatalog.getValue("CD_LG_PRODUCTION") %>" class="inputfield"></td>
			<td><%= Bean.logisticXML.getfieldTransl("cd_currency", false) %></td> <td><select name="cd_currency" id="cd_currency" class="inputfield"><%= Bean.getCurrencyOptions(lgCatalog.getValue("CD_CURRENCY"), true) %></select></td>
		</tr>
		<tr>
			<td><%= Bean.logisticXML.getfieldTransl("name_lg_production", true) %></td><td><input type="text" name="name_lg_production" size="65" value="<%= lgCatalog.getValue("NAME_LG_PRODUCTION") %>" class="inputfield"></td>
			<td><%= Bean.logisticXML.getfieldTransl("cost_lg_production", false) %></td><td><input type="text" name="cost_lg_production" size="20" value="<%= lgCatalog.getValue("COST_LG_PRODUCTION_FRMT") %>" class="inputfield"></td>
		</tr>
		<tr>
			<td><%= Bean.logisticXML.getfieldTransl("desc_lg_production", false) %></td><td><textarea name="desc_lg_production" cols="60" rows="3" class="inputfield"><%= lgCatalog.getValue("DESC_LG_PRODUCTION") %></textarea></td>
		</tr>
		<%=	Bean.getCreationAndMoficationRecordFields(
				lgCatalog.getValue(Bean.getCreationDateFieldName()),
				lgCatalog.getValue("CREATED_BY"),
				lgCatalog.getValue(Bean.getLastUpdateDateFieldName()),
				lgCatalog.getValue("LAST_UPDATE_BY")
			) %>
		<tr>
			<td colspan="6" align="center">
				<%=Bean.getSubmitButtonAjax("../crm/logistic/partners/catalogupdate.jsp") %>
				<%=Bean.getResetButton() %>
				<%=Bean.getGoBackButton("../crm/logistic/partners/catalog.jsp") %>
			</td>
		</tr>

		<%} else {%>
		<tr>
			<td><%= Bean.logisticXML.getfieldTransl("id_lg_production", false) %></td><td><input type="text" name="id_lg_production" size="20" value="<%= lgCatalog.getValue("ID_LG_PRODUCTION") %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.clubXML.getfieldTransl("club", false) %>
				<%= Bean.getGoToClubLink(lgCatalog.getValue("ID_CLUB")) %>
			</td><td><input type="text" name="name_club" size="50" value="<%= Bean.getClubShortName(lgCatalog.getValue("ID_CLUB")) %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td><%= Bean.logisticXML.getfieldTransl("cd_lg_production", false) %></td><td><input type="text" name="cd_lg_production" size="65" value="<%= lgCatalog.getValue("CD_LG_PRODUCTION") %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.logisticXML.getfieldTransl("cd_currency", false) %></td> <td><input type="text" name="cd_currency" size="20" value="<%= lgCatalog.getValue("NAME_CURRENCY") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td><%= Bean.logisticXML.getfieldTransl("name_lg_production", true) %></td><td><input type="text" name="name_lg_production" size="65" value="<%= lgCatalog.getValue("NAME_LG_PRODUCTION") %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.logisticXML.getfieldTransl("cost_lg_production", false) %></td><td><input type="text" name="cost_lg_production" size="20" value="<%= lgCatalog.getValue("COST_LG_PRODUCTION_FRMT") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td><%= Bean.logisticXML.getfieldTransl("desc_lg_production", false) %></td><td><textarea name="desc_lg_production" cols="60" rows="3" readonly="readonly" class="inputfield-ro"><%= lgCatalog.getValue("DESC_LG_PRODUCTION") %></textarea></td>
		</tr>
		<%=	Bean.getCreationAndMoficationRecordFields(
				lgCatalog.getValue(Bean.getCreationDateFieldName()),
				lgCatalog.getValue("CREATED_BY"),
				lgCatalog.getValue(Bean.getLastUpdateDateFieldName()),
				lgCatalog.getValue("LAST_UPDATE_BY")
			) %>
		<tr>
			<td colspan="4" align="center">
				<%=Bean.getGoBackButton("../crm/logistic/partners/catalog.jsp") %>
			</td>
		</tr>
		<%} %>

	</table>
	</form>

<% }

if (Bean.currentMenu.isCurrentTabAndAccessPermitted("LOGISTIC_PARTNERS_CATALOG_TRANSFER_DATA")) {
%>  
	<table <%= Bean.getTableBottomFilter() %>>
		<tr>
		<%= Bean.getFindHTML("transfer_find", transfer_find, "../crm/logistic/partners/catalogspecs.jsp?id=" + id + "&transfer_page=1") %>

		<td>&nbsp;</td>

		</tr>
	</table>
	<%= lgCatalog.getTransferDataHTML(transfer_find, l_transfer_page_beg, l_transfer_page_end) %> 
<%}

}
		
%>
</div></div>
</body>
</html>
