<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcWebPosBean"  />


<%@page import="bc.AppConst"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.ArrayList"%>
<%@page import="bc.captcha.CaptchaService"%><html>
<head>
</head>

<body>
<%
HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String pageFormName = "WEBPOS_USER_PARAM";

request.setCharacterEncoding("UTF-8");
String type		= Bean.getDecodeParam(parameters.get("type")); 
String action	= Bean.getDecodeParam(parameters.get("action")); 
String process	= Bean.getDecodeParam(parameters.get("process"));

type 	= Bean.isEmpty(type)?"":type;
action 	= Bean.isEmpty(action)?"":action;
process = Bean.isEmpty(process)?"":process;

String phone_mobile		= Bean.getDecodeParam(parameters.get("phone_mobile"));
String lang = request.getParameter("lang");

if (type.equalsIgnoreCase("send_phone")) { 
	if (!Bean.isEmpty(lang)) {
		Bean.setLanguage(lang);
	}
    %>
	<div id="div_login">
	<script>
	
	function validatePhone(){
		var returnValue = null;
		var formParam = new Array (
			new Array ('phone_mobile', 'varchar2', 1),
			new Array ('capcha', 'varchar2', 1)
		);
		returnValue = validateFormForID(formParam, 'login');
		// move to server validation
		// if (returnValue) {
		// 	returnValue = ValidCaptcha();
		// }
		return returnValue;
	}
	
	phone_mask("phone_mobile","RU");
	</script>
	<form id="login" name="login" action="forgot_pwd.jsp" method="POST">
		<input type="hidden" name="lang" value="<%=lang %>">
		<input type="hidden" name="type" value="get_sms">
	<%= Bean.getLoginFormTitle() %>
	<fieldset id="inputs">
		<font><%=Bean.webposXML.getfieldTransl("forgot_password_title", false) %></font><br>
	    <input id="phone_mobile" name="phone_mobile" placeholder="<%=Bean.webposXML.getfieldTransl("forgot_password_phone_mobile", false) %>" autofocus="" required="" type="text" value=""><br>
		<span style="font-size: 10px; color:gray; text-transform:none;"><i><%=Bean.webposXML.getfieldTransl("forgot_password_hint_title", false) %></i></span><br>
		<span style="font-size: 10px; color:gray; text-transform:none;"><i><%=Bean.webposXML.getfieldTransl("forgot_password_hint_example", false) %></i></span><br><br>
		
		<span><%=Bean.webposXML.getfieldTransl("capcha_title", false) %></span><br>
		<input type="text" id="txtInput" name="capcha" style="background-position: -1000px center; width: 180px;"/>
        <img id="cpc"  />
	</fieldset>
	<fieldset id="buttons">
		<%=Bean.getSubmitButtonAjax("forgot_pwd.jsp", "confirm", "login", "div_login", "validatePhone") %>
			<a href="main.jsp?do=exit&lang=<%=lang %>" class="button"><%=Bean.buttonXML.getfieldTransl("cancel", false) %></a>
	</fieldset>
	</form>

		<script type="text/javascript">
			GenerateCaptcha();
		</script>
	</div>
<%} else if (type.equalsIgnoreCase("get_sms")) {
	
		ArrayList<String> pParam = new ArrayList<String>();
		pParam.add(phone_mobile);

		String[] results = new String[3];
		String titleWrongCaptcha = Bean.webposXML.getfieldTransl("title_wrong_captcha", false);
	
		if(!CaptchaService.INSTANCE.checkData(request, request.getParameter("capcha"))){
			results=new String[]{"1", null, titleWrongCaptcha};
		}else{
			results 						= Bean.executeAdminFunction("RESTORE_SMS", pParam, results.length);
		}
		
		String resultInt 				= results[0];
		String phone_mobile_confirm 	= results[1];
		String resultMessage 			= results[2];
		
		%>
		<% 
		if (Bean.C_SUCCESS_RESULT.equalsIgnoreCase(resultInt)) {
		
		%>
		<script>
		sms_mask('sms_code');
		</script>
		<form id="login" name="login" action="forgot_pwd.jsp" method="POST">
   		<input type="hidden" name="lang" value="<%=lang %>">
   		<input type="hidden" name="type" value="confirm">
   		<input type="hidden" name="phone_mobile" value="<%=phone_mobile %>">
    	<%= Bean.getLoginFormTitle() %>
   		 <fieldset id="inputs">
			<span><%=Bean.webposXML.getfieldTransl("sms_send", false) %></span><br>
			<span><%=Bean.webposXML.getfieldTransl("sms_phone", false) %>:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<font color="red"><%=phone_mobile_confirm %></font></span><br>
			<span><%=Bean.webposXML.getfieldTransl("sms_password", true) %>:</span>
			<span><input class="inputfield" id="sms_code" name="sms_code" placeholder="<%=Bean.C_REMIND_PASSWORD_SMS_PLACEHOLDER %>" autofocus="" required="" type="text" size="10" value="" maxlength="<%=Bean.C_REMIND_PASSWORD_SMS_PLACEHOLDER.length() %>"></span>
    	</fieldset>
    	<fieldset id="buttons">
			<%=Bean.getSubmitButtonAjax("forgot_pwd.jsp", "confirm", "login", "div_login") %>&nbsp;
			<a href="main.jsp?do=exit&lang=<%=lang %>" class="button"><%=Bean.buttonXML.getfieldTransl("cancel", false) %></a>
    	</fieldset>
		</form>

		<% } else { %>
		<script>
			function validatePhone(){
				var returnValue = null;
				var formParam = new Array (
					new Array ('phone_mobile', 'varchar2', 1),
					new Array ('capcha', 'varchar2', 1)
				);
				returnValue = validateFormForID(formParam, 'login');
				// move to server validation
				// if (returnValue) {
				// 	returnValue = ValidCaptcha();
				// }
				return returnValue;
			}
		
			phone_mask("phone_mobile","RU");
		</script>
		<form id="login" name="login" action="forgot_pwd.jsp" method="POST">
   		<input type="hidden" name="lang" value="<%=lang %>">
   		<input type="hidden" name="type" value="get_sms">
    	<%= Bean.getLoginFormTitle() %>
	    <fieldset id="inputs">
	        <font><%=Bean.webposXML.getfieldTransl("forgot_password_title", false) %></font><br>
	    <input id="phone_mobile" name="phone_mobile" placeholder="<%=Bean.webposXML.getfieldTransl("forgot_password_phone_mobile", false) %>" autofocus="" required="" type="text" value="<%=phone_mobile %>"><br>
		<span style="font-size: 10px; color:gray; text-transform:none;"><i><%=Bean.webposXML.getfieldTransl("forgot_password_hint_title", false) %></i></span><br>
		<span style="font-size: 10px; color:gray; text-transform:none;"><i><%=Bean.webposXML.getfieldTransl("forgot_password_hint_example", false) %></i></span><br><br>
		
		<span><%=Bean.webposXML.getfieldTransl("capcha_title", false) %></span><br>
		<input type="text" id="txtInput" name="capcha" style="background-position: -1000px center; width: 180px;"/>
        <img id="cpc" />
	    </fieldset>
		<fieldset id="results">
			<span id="error_title"><%=Bean.webposXML.getfieldTransl("operation_error", false) %></span><br>
			<span id="error_description"><%=resultMessage %></span><br><br>
    	</fieldset>
    	<fieldset id="buttons">
			<%=Bean.getSubmitButtonAjax("forgot_pwd.jsp", "confirm", "login", "div_login", "validatePhone") %>
			<a href="main.jsp?do=exit&lang=<%=lang %>" class="button"><%=Bean.buttonXML.getfieldTransl("cancel", false) %></a>
    	</fieldset>
		</form>

		<script type="text/javascript">
			GenerateCaptcha();
		</script>
		<% } 
} else if (type.equalsIgnoreCase("confirm")) {
	
	String sms_code		= Bean.getDecodeParam(parameters.get("sms_code"));
	
	ArrayList<String> pParam = new ArrayList<String>();
	
	pParam.add(phone_mobile);
	pParam.add(sms_code);
	
	String[] results = new String[4];
	
	results 				= Bean.executeAdminFunction("RESTORE_CONFIRM", pParam, results.length);
	String resultInt 		= results[0];
	String id_user		 	= results[1];
	String name_user		= results[2];
	String resultMessage 	= results[3];
	
	
	%>
	<% 
	if (Bean.C_SUCCESS_RESULT.equalsIgnoreCase(resultInt)) {
	
	%>
		<script>

		function validatePassword(){
			var returnValue = false;
			var formParam = new Array (
				new Array ('new_password', 'varchar2', 1),
				new Array ('new_password_confirm', 'varchar2', 1)
			);
			returnValue = validateFormForID(formParam, 'login');

			if (returnValue) {
				var password = document.getElementById('password');
				var password_confirm = document.getElementById('password_confirm');
		
				if (password.value != password_confirm.value) {
					document.getElementById('error_place').innerHTML = '<%=Bean.webposXML.getfieldTransl("operation_error", false) %><br><%=Bean.webposXML.getfieldTransl("passwords_not_equals", false) %><br><br>';
					//alert('<%=Bean.webposXML.getfieldTransl("passwords_not_equals", false) %>');
					returnValue = false;
				}
				if (password.value.length < 4 || password_confirm.value.length < 4) {
					document.getElementById('error_place').innerHTML = '<%=Bean.webposXML.getfieldTransl("operation_error", false) %><br><%=Bean.webposXML.getfieldTransl("password_length_error", false) %><br><br>';
					//alert('<%=Bean.webposXML.getfieldTransl("passwords_not_equals", false) %>');
					returnValue = false;
				}
			}
			return returnValue;
		}
	
		phone_mask("phone_mobile","RU");
		</script>
	<form id="login" name="login" action="forgot_pwd.jsp" method="POST">
   		<input type="hidden" name="lang" value="<%=lang %>">
   		<input type="hidden" name="type" value="restore">
   		<input type="hidden" name="id_user" value="<%=id_user %>">
   		<input type="hidden" name="sms_code" value="<%=sms_code %>">
	<%= Bean.getLoginFormTitle() %>
	<fieldset id="inputs">
		<span style="font-weight:bold;"><%=Bean.webposXML.getfieldTransl("password_restore_title", false) %></span><br><br>
		<span><%=Bean.webposXML.getfieldTransl("login_login", false) %></span>
	    <input class="readonly" id="username" name="name_user" type="text" value="<%=name_user %>" readonly>   
	    <span><%=Bean.webposXML.getfieldTransl("login_password_new", false) %></span>
	    <input id="password" name="new_password" type="password" value="">   
	    <span><%=Bean.webposXML.getfieldTransl("login_password_confirm", false) %></span>
	    <input id="password_confirm" name="new_password_confirm" type="password" value=""> <br> 
		<span id="error_place" style="color:red; text-transform:none;">&nbsp;</span>
	</fieldset>
	<br>
	<fieldset id="buttons">
			<%=Bean.getSubmitButtonAjax("forgot_pwd.jsp", "confirm", "login", "div_login", "validatePassword") %>
			<a href="main.jsp?do=exit&lang=<%=lang %>" class="button"><%=Bean.buttonXML.getfieldTransl("cancel", false) %></a>
	</fieldset>
	</form>

	<% } else { %>
	<form id="login" name="login" action="forgot_pwd.jsp" method="POST">
	<%= Bean.getLoginFormTitle() %>
	<fieldset id="results">
		<span id="error_title"><%=Bean.webposXML.getfieldTransl("sms_confirmation_error", false) %></span><br>
		<span id="error_description"><%=resultMessage %></span>
	</fieldset>
	<fieldset id="buttons">
		<a href="main.jsp?do=exit&lang=<%=lang %>" class="button"><%=Bean.buttonXML.getfieldTransl("cancel", false) %></a>
	</fieldset>
	</form>
	<% } 
} else if (type.equalsIgnoreCase("restore")) {
	
	String 
		id_user					= Bean.getDecodeParam(parameters.get("id_user")),
		name_user				= Bean.getDecodeParam(parameters.get("name_user")),
		sms_code				= Bean.getDecodeParam(parameters.get("sms_code")),
		new_password			= Bean.getDecodeParam(parameters.get("new_password")),
		new_password_confirm	= Bean.getDecodeParam(parameters.get("new_password_confirm"));
	
	String clientIP             = Bean.getClientIp(request);
	
	
	ArrayList<String> pParam = new ArrayList<String>();
	
	pParam.add(id_user);
	pParam.add(sms_code);
	pParam.add(clientIP);
	pParam.add(new_password);
	pParam.add(new_password_confirm);
	
	String[] results = new String[2];
	
	results 				= Bean.executeAdminFunction("RESTORE", pParam, results.length);
	String resultInt 		= results[0];
	String resultMessage 	= results[1];
	
	
	%>
	<% 
	if (Bean.C_SUCCESS_RESULT.equalsIgnoreCase(resultInt)) {
	
	%>
	<form id="login" name="login" action="forgot_pwd.jsp" method="POST">
	<%= Bean.getLoginFormTitle() %>
	<fieldset id="inputs">
		<span><%=Bean.webposXML.getfieldTransl("password_restore_success", false) %></span><br>
	</fieldset>
<br>
	<fieldset id="buttons">
		<a href="main.jsp?do=exit&lang=<%=lang %>&username=<%=name_user %>&password=" class="button"><%=Bean.buttonXML.getfieldTransl("button_login", false) %></a>
	</fieldset>
	</form>

	<% } else { %>
	<form id="login" name="login" action="forgot_pwd.jsp" method="POST">
	<%= Bean.getLoginFormTitle() %>
	<fieldset id="results">
		<span id="error_title"><%=Bean.webposXML.getfieldTransl("password_restore_error", false) %></span><br>
		<span id="error_description"><%=resultMessage %></span>
	</fieldset>
	<fieldset id="buttons">
		<a href="main.jsp?do=exit&lang=<%=lang %>" class="button"><%=Bean.buttonXML.getfieldTransl("cancel", false) %></a>
	</fieldset>
	</form>
	<% } 
} else {
    %> <%= Bean.getUnknownTypeText(type) %> <%
}
%>

</body>
</html>
