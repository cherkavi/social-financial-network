<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%= Bean.getLogOutScript(request) %>

<%@page import="java.util.HashMap"%><html>
<%

String pageFormName = "CLIENTS_TERMSES";
String tagDataType = "_DATA_TYPE";
String tagDataState = "_DATA_STATE";
String tagFind = "_FIND_TERM_SES";
String tagPage = "_TERMSES_PAGE";
//String tagViewType = "_VIEW_TYPE";

Bean.setJspPageForTabName(pageFormName);

request.setCharacterEncoding("UTF-8");

HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String specId 		= Bean.getDecodeParamPrepare(parameters.get("specId"));
String find_string 	= Bean.getDecodeParam(parameters.get("find_string"));
String l_page 		= Bean.getDecodeParam(parameters.get("page"));
String data_type	= Bean.getDecodeParam(parameters.get("data_type"));
String data_state	= Bean.getDecodeParam(parameters.get("data_state"));

find_string 	= Bean.checkFindString(pageFormName + tagFind, find_string, l_page);
data_type 		= Bean.checkFindString(pageFormName + tagDataType, data_type, l_page);
data_state 		= Bean.checkFindString(pageFormName + tagDataState, data_state, l_page);
//type_view 		= Bean.checkFindString(pageFormName + tagViewType, type_view, l_page);
if (data_type == null || "".equalsIgnoreCase(data_type)) {
	data_type = "";
}

//Обрабатываем номера страниц
Bean.pageCheck(pageFormName + tagPage, l_page);
String l_beg = Bean.getFirstRowNumber(pageFormName + tagPage);
String l_end = Bean.getLastRowNumber(pageFormName + tagPage);

String print        = Bean.checkPrint(Bean.getDecodeParam(parameters.get("print")));

%>
<head>
	<%=Bean.getJSPHeadHTML(print) %>
</head>
<body>

<div id="div_tabsheet">
	<script>
		function changeTermSesOrTrans(element) {
			myValue = element.value;
			if (myValue=='TERM_SES') {
				ajaxpage('../crm/clients/termses.jsp?type_view='+myValue, 'div_main');
			} else {
				ajaxpage('../crm/clients/telegrams.jsp?type_view='+myValue, 'div_main');
			}
		}
	</script>

<% if ("N".equalsIgnoreCase(print)) { %>
	<table <%= Bean.getTableMenuParam() %>>
		<tr>
			<%= Bean.getPageHeader(Bean.term_sesXML.getfieldTransl("general", false)) %>

		    <%= Bean.getMenuButton("PRINT", "../crm/clients/termses.jsp?print=Y", "", "") %>

		    <!-- Вывод страниц -->
			<%= Bean.getPagesHTML(pageFormName + tagPage, "../crm/clients/termses.jsp?&data_type=" + data_type + "&data_state=" + data_state + "&", "page") %>
		</tr>
	</table>
	<table <%= Bean.getTableMenuFilter() %>>
		<tr>
			<%= Bean.getFindHTML("find_string", find_string, "../crm/clients/termses.jsp?page=1&") %>
			<td align="right" width="20">
				<select name="type_view" class="inputfield" onchange="changeTermSesOrTrans(this)">
					<option value="TERM_SES" SELECTED><%= Bean.term_sesXML.getfieldTransl("general", false) %></option>
					<option value="TELEGRAMS"><%= Bean.telegramXML.getfieldTransl("general", false) %></option>
				</select>
			</td>
			
			<%=Bean.getSelectOnChangeBeginHTML("data_type", "../crm/clients/termses.jsp?page=1&data_state=", Bean.term_sesXML.getfieldTransl("h_data_type", false)) %>
				<%=Bean.getSelectOptionHTML(data_type, "", "") %>
				<%=Bean.getSelectOptionHTML(data_type, "TERM_CARD_REQ", Bean.term_sesXML.getfieldTransl("need_card_req_tsl", false)) %>
				<%=Bean.getSelectOptionHTML(data_type, "TERM_CARD_CHECK", "- " + Bean.term_sesXML.getfieldTransl("need_card_check_tsl", false)) %>
				<%=Bean.getSelectOptionHTML(data_type, "TERM_CLUB_PAY", "- " + Bean.term_sesXML.getfieldTransl("need_club_pay_tsl", false)) %>
				<%=Bean.getSelectOptionHTML(data_type, "TERM_ONLINE_PAY", "- " + Bean.term_sesXML.getfieldTransl("need_online_pay_tsl", false)) %>
				<%=Bean.getSelectOptionHTML(data_type, "TERM_ADV_PAY", "- " + Bean.term_sesXML.getfieldTransl("need_adv_pay_tsl", false)) %>
				<%=Bean.getSelectOptionHTML(data_type, "TERM_ONLINE_STORNO", "- " + Bean.term_sesXML.getfieldTransl("need_online_storno_tsl", false)) %>
				<%=Bean.getSelectOptionHTML(data_type, "TERM_COL_DATA", Bean.term_sesXML.getfieldTransl("need_col_data_tsl", false)) %>
				<%=Bean.getSelectOptionHTML(data_type, "APP_TPAR_DATA", Bean.term_sesXML.getfieldTransl("need_tpar_data_tsl", false)) %>
				<%=Bean.getSelectOptionHTML(data_type, "TERM_MON_REP", Bean.term_sesXML.getfieldTransl("need_term_mon_tsl", false)) %>
			<%=Bean.getSelectOnChangeEndHTML() %>
	
			<% if ("TERM_CARD_REQ".equalsIgnoreCase(data_type)) {%>
				<%=Bean.getSelectOnChangeBeginHTML("data_state", "../crm/clients/termses.jsp?page=1&data_type="+data_type, Bean.term_sesXML.getfieldTransl("h_data_state", false)) %>
					<%=Bean.getTermSesCardReqStateOptions(data_state, true) %>
				<%=Bean.getSelectOnChangeEndHTML() %>
			<% } %>
			<% if ("TERM_CARD_CHECK".equalsIgnoreCase(data_type)) {%>
				<%=Bean.getSelectOnChangeBeginHTML("data_state", "../crm/clients/termses.jsp?page=1&data_type="+data_type, Bean.term_sesXML.getfieldTransl("h_data_state", false)) %>
					<%=Bean.getTermSesCardReqStateOptions(data_state, true) %>
				<%=Bean.getSelectOnChangeEndHTML() %>
			<% } %>
			<% if ("TERM_CLUB_PAY".equalsIgnoreCase(data_type)) {%>
				<%=Bean.getSelectOnChangeBeginHTML("data_state", "../crm/clients/termses.jsp?page=1&data_type="+data_type, Bean.term_sesXML.getfieldTransl("h_data_state", false)) %>
					<%=Bean.getTermSesCardReqStateOptions(data_state, true) %>
				<%=Bean.getSelectOnChangeEndHTML() %>
			<% } %>
			<% if ("TERM_ONLINE_PAY".equalsIgnoreCase(data_type)) {%>
				<%=Bean.getSelectOnChangeBeginHTML("data_state", "../crm/clients/termses.jsp?page=1&data_type="+data_type, Bean.term_sesXML.getfieldTransl("h_data_state", false)) %>
					<%=Bean.getTermSesCardReqStateOptions(data_state, true) %>
				<%=Bean.getSelectOnChangeEndHTML() %>
			<% } %>
			<% if ("TERM_ADV_PAY".equalsIgnoreCase(data_type)) {%>
				<%=Bean.getSelectOnChangeBeginHTML("data_state", "../crm/clients/termses.jsp?page=1&data_type="+data_type, Bean.term_sesXML.getfieldTransl("h_data_state", false)) %>
					<%=Bean.getTermSesCardReqStateOptions(data_state, true) %>
				<%=Bean.getSelectOnChangeEndHTML() %>
			<% } %>
			<% if ("TERM_ONLINE_STORNO".equalsIgnoreCase(data_type)) {%>
				<%=Bean.getSelectOnChangeBeginHTML("data_state", "../crm/clients/termses.jsp?page=1&data_type="+data_type, Bean.term_sesXML.getfieldTransl("h_data_state", false)) %>
					<%=Bean.getTermSesCardReqStateOptions(data_state, true) %>
				<%=Bean.getSelectOnChangeEndHTML() %>
			<% } %>
			<% if ("TERM_COL_DATA".equalsIgnoreCase(data_type)) {%>
				<%=Bean.getSelectOnChangeBeginHTML("data_state", "../crm/clients/termses.jsp?page=1&data_type="+data_type, Bean.term_sesXML.getfieldTransl("h_data_state", false)) %>
					<%=Bean.getTermSesDataStateOptions(data_state, true) %>
				<%=Bean.getSelectOnChangeEndHTML() %>
			<% } %>
			<% if ("APP_TPAR_DATA".equalsIgnoreCase(data_type)) {%>
				<%=Bean.getSelectOnChangeBeginHTML("data_state", "../crm/clients/termses.jsp?page=1&data_type="+data_type, Bean.term_sesXML.getfieldTransl("h_data_state", false)) %>
					<%=Bean.getTermSesParamStateOptions(data_state, true) %>
				<%=Bean.getSelectOnChangeEndHTML() %>
			<% } %>
			<% if ("TERM_MON_REP".equalsIgnoreCase(data_type)) {%>
				<%=Bean.getSelectOnChangeBeginHTML("data_state", "../crm/clients/termses.jsp?page=1&data_type="+data_type, Bean.term_sesXML.getfieldTransl("h_data_state", false)) %>
					<%=Bean.getTermSesMonStateOptions(data_state, true) %>
				<%=Bean.getSelectOnChangeEndHTML() %>
			<% } %>
			
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
		<%= Bean.header.getTermSessionsHeadHTML(find_string, data_type, data_state, l_beg, l_end, print) %>
	<% } %>
<%//} %>
</div></div>
</body>
</html>