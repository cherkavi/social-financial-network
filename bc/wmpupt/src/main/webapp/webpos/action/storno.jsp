<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcWebPosBean"  />

<%@page import="java.util.HashMap"%>
<%@page import="webpos.wpClubCardObject"%>
<%@page import="java.util.ArrayList"%>
<%@page import="webpos.wpTelegramObject"%><html>
<%

request.setCharacterEncoding("UTF-8");

String pageFormName = "WEBPOS_SERVICE_STORNO";

Bean.setJspPageForTabName(pageFormName);

HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String tab = Bean.getDecodeParam(parameters.get("tab"));
if (tab == null || "".equalsIgnoreCase(tab)) {
	tab = Bean.tabsHmGetValue(pageFormName);
}
Bean.tabsHmSetValue(pageFormName, tab);

String id_term = Bean.getCurrentTerm();
Bean.loginTerm.getTermFeature();

String 
	storno_rrn			= Bean.getDecodeParam(parameters.get("storno_rrn"));

storno_rrn		= Bean.isEmpty(storno_rrn)?"":storno_rrn;

%>
<body>
	<%=Bean.getWebPosMenuHTML(pageFormName) %>
<% if (!Bean.hasMenuPermission(pageFormName, Bean.C_WRITE_MENU_PERMISSION, Bean.loginTerm)) { %>
	<%=Bean.getErrorPermissionMessage(pageFormName, Bean.loginTerm) %>
<% } else { 
	%>
	<script>
	function validateCheckStorno(){
		var formParam = new Array (
			new Array ('storno_rrn', 'varchar2', 1)
		);
		return validateForm(formParam, 'updateForm1');
	}
	function showCheckCardButton(card){
		//checkBox = document.getElementById(elem);
		element = document.getElementById('check_card');
		if (card.value == '' || card.value == null) {
			element.className = 'img_check_card_inactive';
		} else {
			element.className = 'img_check_card';
		}
	}
	//card_mask2("cd_card1");
	</script>
	<table <%=Bean.getTableDetail2Param() %>>
		<tr>
			<td><div id="div_action_big">
				<h1><%=Bean.webposXML.getfieldTransl("title_storno", false) %><%=Bean.getHelpButton("storno", "div_action_big") %></h1>
				<form name="updateForm1" id="updateForm1" accept-charset="UTF-8" method="POST">
			        <input type="hidden" name="type" value="storno">
			        <input type="hidden" name="action" value="check">
			        <input type="hidden" name="process" value="no">
			        <input type="hidden" name="id_term" value="<%=id_term %>">
					<table class="action_table">
						<tr><td <%=Bean.getDetailTableFirstColumnWidth() %>><%=Bean.webposXML.getfieldTransl("storno_rrn", true) %></td><td><input type="text" name="storno_rrn" id="storno_rrn" size="30" value="<%=storno_rrn %>"  class="inputfield"></td></tr>
						<tr><td colspan=2  align="center">&nbsp;</td></tr>
						<tr><td colspan=2  align="center">
							<%=Bean.getSubmitButtonAjax("action/stornoupdate.jsp", "button_further", "updateForm1", "div_action_big") %>
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