<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%= Bean.getLogOutScript(request) %>

<%@page import="java.util.HashMap"%><html>
<%

String pageFormName = "CLIENTS_TERMSES";
String tagFind = "_FIND";
String tagTelgrType = "_TELGR_TYPE";
String tagTelgrState = "_TELGR_STATE";
String tagPage = "_TELEGRAM_PAGE";

Bean.setJspPageForTabName(pageFormName);

request.setCharacterEncoding("UTF-8");

HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String specId 		= Bean.getDecodeParamPrepare(parameters.get("specId"));
String find_string 	= Bean.getDecodeParam(parameters.get("find_string"));
String l_page 		= Bean.getDecodeParam(parameters.get("page"));
String telgr_type	= Bean.getDecodeParam(parameters.get("telgr_type"));
String telgr_state	= Bean.getDecodeParam(parameters.get("telgr_state"));

find_string 	= Bean.checkFindString(pageFormName + tagFind, find_string, l_page);
telgr_type 		= Bean.checkFindString(pageFormName + tagTelgrType, telgr_type, l_page);
telgr_state		= Bean.checkFindString(pageFormName + tagTelgrState, telgr_state, l_page);


//Обрабатываем номера страниц
Bean.pageCheck(pageFormName + tagPage, l_page);
String l_beg = Bean.getFirstRowNumber(pageFormName + tagPage);
String l_end = Bean.getLastRowNumber(pageFormName + tagPage);

String print        = Bean.checkPrint(Bean.getDecodeParam(parameters.get("print")));

%>
<head>
	<%=Bean.getJSPHeadHTML(Bean.telegramXML.getfieldTransl("general", false), print) %>
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
			<%= Bean.getPageHeader(Bean.telegramXML.getfieldTransl("general", false)) %>

		    <%= Bean.getMenuButton("PRINT", "../crm/clients/telegrams.jsp?print=Y", "", "") %>
	
		    <!-- Вывод страниц -->
			<%= Bean.getPagesHTML(pageFormName + tagPage, "../crm/clients/telegrams.jsp?telgr_type=" + telgr_type + "&telgr_state=" + telgr_state + "&", "page") %>
		</tr>
	</table>
	
	<table <%= Bean.getTableMenuFilter() %>>
		<tr>
			<%= Bean.getFindHTML("find_string", find_string, "../crm/clients/telegrams.jsp?page=1&") %>
			<td align="right" width="20">
				<select name="type_view" class="inputfield" onchange="changeTermSesOrTrans(this)">
					<option value="TERM_SES"><%= Bean.term_sesXML.getfieldTransl("general", false) %></option>
					<option value="TELEGRAMS" SELECTED><%= Bean.telegramXML.getfieldTransl("general", false) %></option>
				</select>
			</td>
			<%=Bean.getSelectOnChangeBeginHTML("telgr_type", "../crm/clients/telegrams.jsp?page=1", Bean.term_sesXML.getfieldTransl("id_term", false)) %>
				<%=Bean.getLookupCodeOptions("TELEGRAM_TYPE", telgr_type, true) %>
			<%=Bean.getSelectOnChangeEndHTML() %>
		
			<%=Bean.getSelectOnChangeBeginHTML("telgr_state", "../crm/clients/telegrams.jsp?page=1&telgr_type="+telgr_type, Bean.term_sesXML.getfieldTransl("data_type", false)) %>
				<%=Bean.getMeaningFromLookupNameOptions("SYS_MONITOR_CHECK_RESULT", telgr_state, true) %>
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
	<%= Bean.header.getTelegramsHeadHTML(find_string, telgr_type, telgr_state, l_beg, l_end, print) %>
<%} %>
</div></div>
</body>
</html>