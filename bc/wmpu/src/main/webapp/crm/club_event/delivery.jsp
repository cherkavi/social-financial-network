<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%= Bean.getLogOutScript(request) %>

<%@page import="java.util.HashMap"%><html>
<%
request.setCharacterEncoding("UTF-8");

String pageFormName = "CLUB_EVENT_DELIVERY";
String tagFind = "_FIND";
String tagGiftState = "_GIFT_STATE";

Bean.setJspPageForTabName(pageFormName);

HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String specId 		= Bean.getDecodeParamPrepare(parameters.get("specId"));
String find_string 	= Bean.getDecodeParam(parameters.get("find_string"));
String l_page 		= Bean.getDecodeParam(parameters.get("page"));
String gift_state  = Bean.getDecodeParam(parameters.get("gift_state"));

find_string 	= Bean.checkFindString(pageFormName + tagFind, find_string, l_page);
gift_state	 	= Bean.checkFindString(pageFormName + tagGiftState, gift_state, l_page);

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
			    <%= Bean.getMenuButton("ADD", "../crm/club_event/deliveryupdate.jsp?type=general&action=add&process=no", "", "") %>
			<% } %>
		    <%= Bean.getMenuButton("PRINT", "../crm/club_event/delivery.jsp?print=Y", "", "") %>
	
		    <!-- Вывод страниц -->
			<%= Bean.getPagesHTML(pageFormName, "../crm/club_event/delivery.jsp?", "page") %>
		</tr>
	</table>
	<table <%= Bean.getTableMenuFilter() %>>
		<tr>
			<%= Bean.getFindHTML("find_string", find_string, "../crm/club_event/delivery.jsp?page=1&") %>
	
			<%=Bean.getSelectOnChangeBeginHTML("gift_state", "../crm/club_event/delivery.jsp?page=1", Bean.club_actionXML.getfieldTransl("name_gift_type", false)) %>
				<%= Bean.getNatPrsGiftStateOptions(gift_state, true) %>
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
	<% Bean.header.setDeleteHyperLink("../crm/club_event/deliveryupdate.jsp?type=general&action=remove&process=yes&id=",Bean.club_actionXML.getfieldTransl("h_delete_gift", false),"ID_NAT_PRS_GIFT", "NAME_GIFT"); %>
<%} else { %>
	<% Bean.header.setDeleteHyperLink("","",""); %>
<%} %>

<% if (Bean.hasAccessMenuPermission(pageFormName)) { %>
	<%= Bean.header.getClubActionGiftsDeliveryHeadHTML(gift_state, find_string, l_beg, l_end, print) %>
<%} %>
</div></div>
</body>
</html>