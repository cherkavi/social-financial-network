<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

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

String pageFormName = "FINANCE_CHECK_MODEL";

Bean.setJspPageForTabName(pageFormName);

String	tab = Bean.getDecodeParam(parameters.get("tab"));
String sementFull = "";
if (tab==null || "".equalsIgnoreCase(tab)) { tab = Bean.tabsHmGetValue(pageFormName); }
Bean.tabsHmSetValue(pageFormName, tab);
%>

	<table <%=Bean.getTableMenuParam() %>>
		<tr>
			<%= Bean.getPageHeader() %>

		</tr>
	</table>
	<table <%=Bean.getTableMenuParam() %>>
		<tr>
			<td>
				<!-- Выводим перечень закладок -->
				<%= Bean.currentMenu.getFormTabScheeds(tab, "../crm/finance/check_modelspecs.jsp?id=") %>
			</td>
		</tr>
	</table>
</div>
<div id="div_data">
<div id="div_data_detail">
<%
if (Bean.currentMenu.isCurrentTabAndEditPermitted("FINANCE_CHECK_MODEL_RUN")) {
	%>
    <form action="../crm/finance/bk_accountsupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">
        <input type="hidden" name="action" value="run">
        <input type="hidden" name="process" value="yes">
        <input type="hidden" name="type" value="check">
	<table <%=Bean.getTableDetailParam() %>>
		<tr>
			<td><td><input type="radio" name="check_type" id="check_all" value="ALL" CHECKED class="inputfield"><label for="check_all"><%= Bean.getMeaningFoCodeValue("REPORT_TYPE", "ALL_FINANCE_MODEL_CHECK") %></label></td>
		</tr>
		<tr>
			<td><td><input type="radio" name="check_type" id="check_rel" value="CLUB_REL" class="inputfield"><label for="check_rel"><%= Bean.getMeaningFoCodeValue("REPORT_TYPE", "CLUB_REL_CHECK") %></label></td>
		</tr>
		<tr>
			<td><td><input type="radio" name="check_type" id="check_oper" value="BK_OPERATION" class="inputfield"><label for="check_oper"><%= Bean.getMeaningFoCodeValue("REPORT_TYPE", "BK_OPERATION_CHECK") %></label></td>
		</tr>
 		<tr>
			<td colspan="6" align="center">
				<%=Bean.getSubmitButtonAjax("../crm/finance/check_modelupdate.jsp") %>
				<%=Bean.getResetButton() %>
				<%=Bean.getGoBackButton("../crm/finance/check_model.jsp") %>
			</td>
		</tr>

	</table>

	</form>
<% } %>
</div></div>
</body>
</html>
