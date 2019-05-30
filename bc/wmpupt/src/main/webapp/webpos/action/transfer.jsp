<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcWebPosBean"  />

<%@page import="java.util.HashMap"%>
<html>
<%

request.setCharacterEncoding("UTF-8");

String pageFormName = "WEBPOS_SERVICE_TRANSFER";

Bean.setJspPageForTabName(pageFormName);

HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String tab = Bean.getDecodeParam(parameters.get("tab"));
if (tab == null || "".equalsIgnoreCase(tab)) {
	tab = Bean.tabsHmGetValue(pageFormName);
}
Bean.tabsHmSetValue(pageFormName, tab);

String id_term = Bean.getCurrentTerm();
Bean.loginTerm.getTermFeature();

String from_card		= Bean.getDecodeParam(parameters.get("from_card"));
from_card				= !(from_card == null)?from_card.replace(" ", ""):"";

String to_card			= Bean.getDecodeParam(parameters.get("to_card"));
to_card  				= !(to_card == null)?to_card.replace(" ", ""):"";

String transfer_value	= Bean.getDecodeParam(parameters.get("transfer_value"));
transfer_value  		= !(transfer_value == null)?transfer_value.replace(" ", ""):"";

%>
<body>
	<%=Bean.getWebPosMenuHTML(pageFormName) %>
<% if (!Bean.hasMenuPermission(pageFormName, Bean.C_WRITE_MENU_PERMISSION, Bean.loginTerm)) { %>
	<%=Bean.getErrorPermissionMessage(pageFormName, Bean.loginTerm) %>
<% } else { %>

	<script>

	function validateTransfer(){
		var returnValue = null;
		var formParam = new Array (
			new Array ('from_card', 'card', 1),
			new Array ('to_card', 'card', 1),
			new Array ('transfer_value', 'oper_sum', 1)
		);
		returnValue = validateForm(formParam, 'updateForm4');
		return returnValue;
	}
	card_mask2("from_card");
	card_mask2("to_card");
	</script>
	<table <%=Bean.getTableDetail2Param() %>>
		<tr>
			<td><div id="div_action_big">
				<h1><%=Bean.webposXML.getfieldTransl("title_transfer", false) %><%=Bean.getHelpButton("transfer", "div_action_big") %></h1>
				<form name="updateForm4" id="updateForm4" accept-charset="UTF-8" method="POST">
			        <input type="hidden" name="type" value="transfer">
			        <input type="hidden" name="action" value="put_to_another_card">
			        <input type="hidden" name="process" value="no">
					<input type="hidden" name="id_term" value="<%=id_term %>">
					<input type="hidden" name="transfer_currency" value="<%=Bean.loginTerm.getValue("CD_TERM_CURRENCY") %>">
					<table class="action_table">
						<tr><td <%=Bean.getDetailTableFirstColumnWidth() %>><%=Bean.webposXML.getfieldTransl("transfer_from_card", true) %></td><td><input type="text" name="from_card" id="from_card" size="30" value="<%=from_card %>"  class="inputfield"></td></tr>
						<tr>
							<td><%=Bean.webposXML.getfieldTransl("transfer_amount", true) %></td>
							<td>
								<input type="text" name="transfer_value" id="transfer_value" size="20" value="<%=transfer_value %>" class="inputfield" maxlength="15"><input type="text" name="transfer_point_currency" id="transfer_point_currency" size="5" value="<%=Bean.webposXML.getfieldTransl("title_transfer_points_currency", false) %>" readonly class="inputfield-ro">
							</td>
						</tr>
						<tr><td><%=Bean.webposXML.getfieldTransl("transfer_to_card", true) %></td><td><input type="text" name="to_card" id="to_card" size="30" value="<%=to_card %>"  class="inputfield"></td></tr>
					
			  			<tr><td colspan="2"  align="center"><%=Bean.getSubmitButtonAjax("action/transferupdate.jsp", "transfer", "updateForm4", "div_action_big", "validateTransfer") %></td></tr>
				<tr><td colspan="2">
				<div id=div_hints>
					<i><b><%=Bean.webposXML.getfieldTransl("title_note", false) %>
					</b><br><%=Bean.webposXML.getfieldTransl("title_note_transfer", false) %></i>
					<%=Bean.getWEBPosOnlyTestCards(false) %>
				</div>
				</td></tr>
					</table>
				</form>
				</div>
			</td>
		</tr>
	</table>
<% } %>
</body>
</html>