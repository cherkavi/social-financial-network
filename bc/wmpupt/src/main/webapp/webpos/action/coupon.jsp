<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcWebPosBean"  />

<%@page import="java.util.HashMap"%>
<html>
<%

request.setCharacterEncoding("UTF-8");

String pageFormName = "WEBPOS_SERVICE_COUPON";

Bean.setJspPageForTabName(pageFormName);

HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String tab = Bean.getDecodeParam(parameters.get("tab"));
if (tab == null || "".equalsIgnoreCase(tab)) {
	tab = Bean.tabsHmGetValue(pageFormName);
}
Bean.tabsHmSetValue(pageFormName, tab);

Bean.loginTerm.getTermFeature();
String
	cd_card1		= Bean.getDecodeParam(parameters.get("cd_card1")),
	coupon			= Bean.getDecodeParam(parameters.get("coupon"));

cd_card1				= !(cd_card1 == null)?cd_card1.replace(" ", ""):"";
coupon					= !(coupon == null)?coupon:"";

%>
<body>
	<%=Bean.getWebPosMenuHTML(pageFormName) %>
<% if (!Bean.hasMenuPermission(pageFormName, Bean.C_WRITE_MENU_PERMISSION, Bean.loginTerm)) { %>
	<%=Bean.getErrorPermissionMessage(pageFormName, Bean.loginTerm) %>
<% } else { %>
	<script>
	function validateCheckCoupon(){
		var returnValue = null;
		var formParam = new Array (
			new Array ('cd_card1', 'card', 1),
			new Array ('coupon', 'varchar2', 1),
			new Array ('coupon_control_code', 'varchar2', 1)
		);
		returnValue = validateForm(formParam, 'updateForm3');
		return returnValue;
	}
	card_mask2("cd_card1");
	</script>
	<table <%=Bean.getTableDetail2Param() %>>
		<tr>
			<td><div id="div_action_big">
				<h1><%=Bean.webposXML.getfieldTransl("title_coupon", false) %><%=Bean.getHelpButton("coupon", "div_action_big") %></h1>
				<form name="updateForm3" id="updateForm3" accept-charset="UTF-8" method="POST">
			        <input type="hidden" name="type" value="coupon">
			        <input type="hidden" name="action" value="put">
			        <input type="hidden" name="process" value="yes">
			        <input type="hidden" name="id_term" value="<%=Bean.getCurrentTerm() %>">
					<table class="action_table">
			  			<tr><td <%=Bean.getDetailTableFirstColumnWidth() %>><%=Bean.webposXML.getfieldTransl("card", true) %></td><td><input type="text" name="cd_card1" id="cd_card1" size="20" value="<%=cd_card1 %>"  class="inputfield"></td></tr>
              			<tr><td><%=Bean.webposXML.getfieldTransl("coupon", true) %></td><td><input type="text" name="coupon" id="coupon" size="20" value="<%=coupon %>"  class="inputfield"></td></tr>
			  			<tr><td><%=Bean.webposXML.getfieldTransl("coupon_control_code", true) %></td><td><input type="text" name="coupon_control_code" id="coupon_control_code" size="20" value=""  class="inputfield"></td></tr>
			  			<tr><td colspan="2" class="center"><%=Bean.getSubmitButtonAjax("action/couponupdate.jsp", "check", "updateForm3", "div_action_big", "validateCheckCoupon") %></td></tr>
				<tr><td colspan="2" class="left">
				<div id=div_hints><%=Bean.getWEBPosOnlyTestCards() %>
				<br>
				<%=Bean.getWEBPosOnlyTestCoupon() %>
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