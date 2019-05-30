<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%= Bean.getLogOutScript(request) %>

<%@page import="java.util.HashMap"%><html>
<%

String pageFormName = "CLUB_EVENT_GIFTS";
String tagFind = "_FIND";
String tagGiftType = "_GIFT_TYPE";
String tagOrderColumn = "_ORDER_COLUMN";
String tagOrderType = "_ORDER_TYPE";

Bean.setJspPageForTabName(pageFormName);

request.setCharacterEncoding("UTF-8");

HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String specId 			= Bean.getDecodeParamPrepare(parameters.get("specId"));
String find_string 		= Bean.getDecodeParam(parameters.get("find_string"));
String l_page 			= Bean.getDecodeParam(parameters.get("page"));
String gift_type		= Bean.getDecodeParam(parameters.get("gift_type"));
String l_order_column	= Bean.getDecodeParam(parameters.get("col"));
String l_order_type		= Bean.getDecodeParam(parameters.get("order"));

find_string = Bean.checkFindString(pageFormName + tagFind, find_string, l_page);
gift_type 	= Bean.checkFindString(pageFormName + tagGiftType, gift_type, l_page);
l_order_column 	= Bean.checkFindString(pageFormName + tagOrderColumn, l_order_column, l_page);
l_order_type 	= Bean.checkFindString(pageFormName + tagOrderType, l_order_type, l_page);

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
		    <%= Bean.getMenuButton("ADD", "../crm/club_event/giftupdate.jsp?type=general&action=add&process=no", "", "") %>
			<% } %>
		    <%= Bean.getMenuButton("PRINT", "../crm/club_event/gifts.jsp?print=Y", "", "") %>

		    <!-- Вывод страниц -->
			<%= Bean.getPagesHTML(pageFormName, "../crm/club_event/gifts.jsp?", "page") %>
		</tr>
	</table>
	<table <%= Bean.getTableMenuFilter() %>>
		<tr>
			<%= Bean.getFindHTML("find_string", find_string, "../crm/club_event/gifts.jsp?page=1&") %>
	
			<%=Bean.getSelectOnChangeBeginHTML("gift_type", "../crm/club_event/gifts.jsp?page=1", Bean.club_actionXML.getfieldTransl("name_gift_type", false)) %>
				<%= Bean.getGiftTypeOptions(gift_type, true) %>
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
<% if (Bean.hasEditMenuPermission(pageFormName)) { %>
	<% Bean.header.setDeleteHyperLink("../crm/club_event/giftupdate.jsp?type=general&action=remove&process=yes&id=",Bean.club_actionXML.getfieldTransl("h_delete_gift", false),"ID_GIFT", "NAME_GIFT"); %>
<%} else { %>
	<% Bean.header.setDeleteHyperLink("","",""); %>
<%} %>

<% if (Bean.hasAccessMenuPermission(pageFormName)) { %>
<%= Bean.header.getClubActionsGiftsHeadHTML(gift_type, find_string, l_beg, l_end, l_order_column, l_order_type, print) %>
<%} %>
</div></div>
</body>
</html>