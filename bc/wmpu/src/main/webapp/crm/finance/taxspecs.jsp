<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%@page import="java.util.HashMap"%>
<%@page import="bc.objects.bcTaxObject"%>

<%= Bean.getLogOutScript(request) %>
<html>
<head>
	<%= Bean.getMetaContent() %>
	<%= Bean.getBottomFrameCSS() %>
<Head>

<%

HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String pageFormName = "FINANCE_TAX";

Bean.setJspPageForTabName(pageFormName);

String tagValues = "_VALUES";
String tagValueFind = "_VALUE_FIND";

String id = Bean.getDecodeParam(parameters.get("id"));
String tab = Bean.getDecodeParam(parameters.get("tab"));


if (tab==null || "".equalsIgnoreCase(tab)) {
	tab = Bean.tabsHmGetValue(pageFormName);
} else {
	Bean.tabsHmSetValue(pageFormName, tab);
}

if (id==null || "".equalsIgnoreCase(id)) { 
%>
	<%=Bean.getIDNotFoundMessage() %>
<% 
} else {
	Bean.tabsHmSetValue(pageFormName, tab);
	bcTaxObject tax = new bcTaxObject(id);	

	//Обрабатываем номера страниц
	String l_value_page = Bean.getDecodeParam(parameters.get("value_page"));
	Bean.pageCheck(pageFormName + tagValues, l_value_page);
	String l_value_page_beg = Bean.getFirstRowNumber(pageFormName + tagValues);
	String l_value_page_end = Bean.getLastRowNumber(pageFormName + tagValues);

	String value_find 	= Bean.getDecodeParam(parameters.get("value_find"));
	value_find 	= Bean.checkFindString(pageFormName + tagValueFind, value_find, l_value_page);


%>

</head>
<body topmargin="0">
<div id="div_tabsheet">

	<table <%=Bean.getTableMenuParam() %>>
		<tr>
			<%= Bean.getPageHeader() %>

			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("FINANCE_TAX_INFO")){ %>
				<% if (Bean.currentMenu.isCurrentTabAndEditPermitted("FINANCE_TAX_INFO")) {%>
					<%= Bean.getMenuButton("ADD", "../crm/finance/taxupdate.jsp?id=" + id + "&type=value&action=add&process=no", "", "") %>
				<% } %>

			    <!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagValues, "../crm/finance/taxspecs.jsp?id=" + id + "&tab="+Bean.currentMenu.getTabID("FINANCE_TAX_INFO")+"&", "value_page") %>
			<% } %>

		</tr>
	</table>
	<table <%=Bean.getTableMenuParam() %>>
		<%= Bean.getDetailCaption(tax.getValue("NAME_TAX")) %>
		<tr>
			<td>
				<!-- Выводим перечень закладок -->
				<%= Bean.currentMenu.getFormTabScheeds(tab, "../crm/finance/taxspecs.jsp?id=" + id) %>
			</td>
	</tr>
</table>
</div>
<div id="div_data">
<div id="div_data_detail">
<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("FINANCE_TAX_INFO")) { %>
	<table <%=Bean.getTableBottomFilter() %>>
	  	<tr>
		<%= Bean.getFindHTML("value_find", value_find, "../crm/finance/taxspecs.jsp?id=" + id + "&value_page=1") %>

		<td>&nbsp;</td>
	  	</tr>
	</table>
	<%= tax.getTaxValuesHTML(value_find, l_value_page_beg, l_value_page_end) %>

<% } %>

<% } %>

</div></div>
</body>

</html>
