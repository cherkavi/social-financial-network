<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%= Bean.getLogOutScript(request) %>

<%@page import="java.util.HashMap"%>
<%@page import="bc.objects.bcLGAnalyticObject"%>
<html>
<head>
	<%= Bean.getMetaContent() %>
	<%= Bean.getBottomFrameCSS() %>
</head>
<body topmargin="0">
<div id="div_tabsheet">

<%

HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String pageFormName = "LOGISTIC_PARTNERS_ANALYTICS";
String tagOperationList = "_OPERATION_LIST";
String tagFind = "_FIND";
String tagLGType = "_LG_TYPE";

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
	bcLGAnalyticObject analytic = new bcLGAnalyticObject("JUR_PRS", id);

	String l_list_page = Bean.getDecodeParam(parameters.get("list_page"));
	//Обрабатываем номера страниц
	Bean.pageCheck(pageFormName + tagOperationList, l_list_page);
	String l_list_beg = Bean.getFirstRowNumber(pageFormName + tagOperationList);
	String l_list_end = Bean.getLastRowNumber(pageFormName + tagOperationList);

	String find_string 	= Bean.getDecodeParam(parameters.get("find_string"));
	find_string 	= Bean.checkFindString(pageFormName + tagFind, find_string, l_list_page);

	String lg_type 	= Bean.getDecodeParam(parameters.get("lg_type"));
	lg_type 		= Bean.checkFindString(pageFormName + tagLGType, lg_type, l_list_page);
%>

	<table <%=Bean.getTableMenuParam() %>>
		<tr>
			<%= Bean.getPageHeader() %>
			
			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("LOGISTIC_PARTNERS_ANALYTICS_INFO")) {%>
				<% if (Bean.currentMenu.isCurrentTabAndEditPermitted("LOGISTIC_PARTNERS_ANALYTICS_INFO")) { %>
				    <%= Bean.getMenuButton("RUN", "../crm/logistic/partners/analyticupdate.jsp?type=general&action=run&process=yes&id="+id, Bean.logisticXML.getfieldTransl("h_update_jur_prs_analytics", false) + " \\\'" + analytic.getValue("SNAME_JUR_PRS") + "\\\'", "") %>
				<% } %>
	    		<!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagOperationList, "../crm/logistic/partners/analyticspecs.jsp?id=" + id + "&tab="+Bean.currentMenu.getTabID("LOGISTIC_PARTNERS_ANALYTICS_INFO") + "&", "list_page") %>
			<% } %>
		</tr>
	</table>
	<table <%=Bean.getTableMenuParam() %>>
		<%= Bean.getDetailCaption(analytic.getValue("ID_JUR_PRS") + " - " + analytic.getValue("SNAME_JUR_PRS")) %>
		<tr>
			<td>
				<!-- Выводим перечень закладок -->
				<%= Bean.currentMenu.getFormTabScheeds(tab, "../crm/logistic/partners/analyticspecs.jsp?id=" + id) %>
			</td>

		</tr>
	</table>
</div>
<div id="div_data">
<div id="div_data_detail">
<%
if (Bean.currentMenu.isCurrentTabAndAccessPermitted("LOGISTIC_PARTNERS_ANALYTICS_INFO")) {
	%> 
	<table <%= Bean.getTableBottomFilter() %>>
		<tr>
			<%= Bean.getFindHTML("find_string", find_string, "../crm/logistic/partners/analyticspecs.jsp?list_page=1&id="+id + "&") %>
			
			<%=Bean.getSelectOnChangeBeginHTML("lg_type", "../crm/logistic/partners/analyticspecs.jsp?list_page=1&id="+id, Bean.logisticXML.getfieldTransl("name_lg_type", false)) %>
				<%=Bean.getLGTypeOptions(lg_type, true) %>
			<%=Bean.getSelectOnChangeEndHTML() %>

		</tr>
	</table>
	<%= analytic.getOperationListHTML(find_string, lg_type, l_list_beg, l_list_end) %>
<%
}


}
		
%>
</div></div>
</body>
</html>
