<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%= Bean.getLogOutScript(request) %>

<%@page import="java.util.HashMap"%><html>
<%
request.setCharacterEncoding("UTF-8");
	
String pageFormName = "CRM_CLUB_REFERRAL_SCHEME";
String tagFind = "_FIND";
String tagType = "_TYPE";
String tagCalcType = "_CALC_TYPE";

Bean.setJspPageForTabName(pageFormName);

HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String specId 		= Bean.getDecodeParamPrepare(parameters.get("specId"));
String l_page 		= Bean.getDecodeParam(parameters.get("page"));
String find_string 	= Bean.getDecodeParam(parameters.get("find_string"));
String scheme_type 	= Bean.getDecodeParam(parameters.get("scheme_type"));
String calc_type 	= Bean.getDecodeParam(parameters.get("calc_type"));

find_string 	= Bean.checkFindString(pageFormName + tagFind, find_string, l_page);
scheme_type = Bean.checkFindString(pageFormName + tagType, scheme_type, l_page);
calc_type = Bean.checkFindString(pageFormName + tagCalcType, calc_type, l_page);

Bean.pageCheck(pageFormName, l_page);
String l_beg = Bean.getFirstRowNumber(pageFormName);
String l_end = Bean.getLastRowNumber(pageFormName);

String print        = Bean.checkPrint(Bean.getDecodeParam(parameters.get("print")));

%>
<head>
	<%=Bean.getJSPHeadHTML(print) %>
</head>
<body>
<div id="div_tabsheet">
<% if ("N".equalsIgnoreCase(print)) { %>
	<table <%= Bean.getTableMenuParam() %>>
		<tr>
			<%= Bean.getPageHeader() %>

			<% if (Bean.hasEditMenuPermission(pageFormName)) { %>
		    <%= Bean.getMenuButton("ADD", "../crm/club/referral_schemeupdate.jsp?type=general&action=add&process=no", "", "") %>
			<% } %>
		    <%= Bean.getMenuButton("PRINT", "../crm/club/referral_scheme.jsp?print=Y", "", "") %>

			<!-- Вывод страниц -->
			<%= Bean.getPagesHTML(pageFormName, "../crm/club/referral_scheme.jsp?", "page") %>
		</tr>
	</table>
	<table <%= Bean.getTableMenuFilter() %>>
		<tr>
			<%= Bean.getFindHTML("find_string", find_string, "../crm/club/referral_scheme.jsp?page=1&") %>
	
			<%=Bean.getSelectOnChangeBeginHTML("scheme_type", "../crm/club/referral_scheme.jsp?page=1", Bean.clubXML.getfieldTransl("name_referral_scheme_type", false)) %>
				<%= Bean.getReferralShemeTypeOptions(scheme_type, true) %>
			<%=Bean.getSelectOnChangeEndHTML() %>

			<%=Bean.getSelectOnChangeBeginHTML("calc_type", "../crm/club/referral_scheme.jsp?page=1", Bean.clubXML.getfieldTransl("name_referral_scheme_calc_type", false)) %>
				<%= Bean.getReferralShemeCalcTypeOptions(calc_type, true) %>
			<%=Bean.getSelectOnChangeEndHTML() %>
		</tr>
	</table>
<% } else { %>
	<table <%= Bean.getTableMenuParam() %>>
		<tr>
			<%= Bean.getPageHeaderPrint() %>
		</tr>
	</table>
<% } %>
</div>
<div id="div_data">
<div id="div_data_detail">
<% if (Bean.hasAccessMenuPermission(pageFormName)) { %>
	<%= Bean.header.getReferralSchemeHeaderHTML(find_string, scheme_type, calc_type, l_beg, l_end, print) %>
<%} %>
</div></div>
</body>
</html>