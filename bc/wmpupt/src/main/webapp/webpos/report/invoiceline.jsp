<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcWebPosBean"  />

<%@page import="java.util.HashMap"%>

<%

request.setCharacterEncoding("UTF-8");

String pageFormName = "WEBPOS_SERVICE_OPERATION";

HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String action	= Bean.getDecodeParam(parameters.get("action")); 


%>
<html>
<body>
	<% if (action.equalsIgnoreCase("add")) { %>
		<form name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST">
			<input type="hidden" name="type" value="trans">
			<input type="hidden" name="process" value="yes">
		    <table class="action_table">
				<tr><td colspan="2"><%=Bean.webposXML.getfieldTransl("cheque_invoice", false) %></td>
				<tr><td <%=Bean.getDetailTableFirstColumnWidth() %>><%=Bean.webposXML.getfieldTransl("cheque_invoice", false) %></td><td><input type="text" name="rrn_txt" id="rrn_txt" size="20" value="" readonly class="inputfield_finish_blue"></td></tr>
			</table>
		</form>
	<% } %>
</body>

</html>