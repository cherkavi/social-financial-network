<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcWebPosBean"  />
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<%@page import="bc.AppConst"%>
<%@page import="java.util.ArrayList"%>
<%@page import="bc.captcha.CaptchaService"%><html>
	<head>
<style type="text/css">
.popup{
  display: none;
  background: #fff;
  box-shadow: 0 0 10px rgba(0,0,0,1);
  width: 600px;
  position: fixed;
  top: 50%;
  left: 50%;
  margin-left: -300px;
  margin-top: -250px;
}
.popup_title{
  font-weight: bold;
  padding: 10px;
}
.popup_content{
  padding: 10px;
  border-top: 1px solid #ccc;
}
.closer{
  float: right;
  cursor: pointer;
}
</style>

<% Bean.setSessionId(request.getSession().getId()); %>
<%
String lang = request.getParameter("lang");
System.out.println("lang="+lang);
//String login = request.getParameter("login");
if (lang == null || "".equalsIgnoreCase(lang) || "null".equalsIgnoreCase(lang)) {
	lang = Bean.getLanguage();
	if (lang == null || "".equalsIgnoreCase(lang)) {
		lang = "RU";
	}
} else {
	Bean.setLanguage(lang);
}
//Bean.setLanguage(lang);
String tmpusername=request.getParameter("username");
String tmppassword=request.getParameter("password");
String smsCofirmCode=request.getParameter("sms");
if (Bean.isEmpty(tmpusername)) {
	tmpusername = "";
	tmppassword = "";
}

smsCofirmCode	= Bean.isEmpty(smsCofirmCode)?"":smsCofirmCode.replace(" ", "");

String action = request.getParameter("do");

boolean isLogged = false;
String errorMessage = "";
String errorCode = "";

System.out.println("action="+action);
if (action != null && action.equalsIgnoreCase("exit")){ // обработка выхода
    Bean.logOut(session);
} else {
	if (!(lang == null || "".equalsIgnoreCase(lang))) {
		Bean.setLanguage(lang);
	}
	System.out.println("1");
	isLogged = Bean.logInCurrent(Bean.getSessionId());
	System.out.println("2");
	//System.out.println("isLogged="+isLogged);
	if ("login".equalsIgnoreCase(action)) {
		if( ( Bean.getErrorConnectionCount() > Bean.C_MAX_ERROR_CONNECTION_COUNT)  && CaptchaService.INSTANCE.checkData(request, request.getParameter("capcha"))==false){
			isLogged=false;
			errorCode = Integer.toString(99);
			errorMessage = Bean.webposXML.getfieldTransl("title_wrong_captcha", false);
		}else{
			if (Bean.logIn(tmpusername,tmppassword,smsCofirmCode,request)) { 
				isLogged = true;
				//lang = Bean.loginUserParam.getValue("UIL");
				//if ((lang == null || "".equalsIgnoreCase(lang))) {
				//	lang = "RU";
				//}
				//Bean.setLanguage(lang);
				
				Bean.setLanguage(lang);
				ArrayList<String> pParam = new ArrayList<String>();
				pParam.add(Bean.getLoginUserId());
				pParam.add("UIL");
				pParam.add(lang);
				pParam.add("Y");
				pParam.add("N");
					
				String[] results = new String[2];
				results = Bean.executeFunction("PACK$USER_UI.change_user_param", pParam, results.length);
				String resultInt = results[0];
				String resultMessage = results[1];
				Bean.clearErrorConnectionCount();
			} else {
			    if (Bean.isEmpty(tmpusername)) {
			    	errorCode = "";
				} else {
					errorCode = Integer.toString(Bean.getErrorCode());
					errorMessage = Bean.getErrorMessage();
					Bean.addErrorConnectionCount();
				}
			} 
		}
	}
	

	if (isLogged) {
		Bean.setLanguage(lang);
		Bean.loginUser.getCurrentUserFeature();
		Bean.setDateFormat("DD.MM.RRRR");
		String id_term = Bean.getLoginUserTerm();
		Bean.loginTerm.setIdTerm(id_term);
		Bean.loginTerm.getFeature();
		Bean.setCurrentTerm(id_term);
	}
}
	//Bean.setLanguage(lang);
	//Bean.setContextPath(Bean.getFullContext(request));
	Bean.clearHmEntries();

%>

	<script type="text/javascript" src="js/jquery-1.11.3.js"></script>
    <script type="text/javascript" src="js/jquery-ui.js"></script>
	<script type="text/javascript" src="js/jquery.maskedinput.js"></script>
	<script type="text/javascript" src="js/jquery.dirtyforms.min.js" ></script>

	<script type="text/javascript" src="js/mask.js"></script>
	<script type="text/javascript" src="js/robokassa.js"></script>
	<script type="text/javascript" language="javascript" src="../dwr/interface/responseUtility.js"></script>
	<script type="text/javascript" language="javascript" src="../dwr/engine.js"></script>
	<script type="text/javascript" src="js/main/element_navigator.js" ></script>
	
	<link content="yes" name="apple-mobile-web-app-capable">
	<meta content="black" name="apple-mobile-web-app-status-bar-style">

	<script type="text/javascript">

	 


  	function checkMaxRowNumber(hyperLink, rowCount, enteredRow){
  		//alert ('checkMaxRowNumber: enteredRow=' + enteredRow + ', rowCount='+rowCount);
  		if (rowCount != '0') {
	  	  	if (enteredRow > rowCount || enteredRow <= 0) {
	  	  	  	alert ('<%= Bean.commonXML.getfieldTransl("error_max_row_count", false) %> ' + rowCount);
	  	  	  	return false;
	  	  	}
  		}
  		ajaxpage(hyperLink, 'div_main');
 	 }

  	function checkMaxRowNumberForDiv(hyperLink, rowCount, enteredRow, div_name){
  		//alert ('checkMaxRowNumber: enteredRow=' + enteredRow + ', rowCount='+rowCount);
  		if (rowCount != '0') {
	  	  	if (enteredRow > rowCount || enteredRow <= 0) {
	  	  	  	alert ('<%= Bean.commonXML.getfieldTransl("error_max_row_count", false) %> ' + rowCount);
	  	  	  	return false;
	  	  	}
  		}
  		ajaxpage(hyperLink, div_name);
 	 }
	</script>

<%

if (isLogged && Bean.C_MANY_USERS_ON_LOGIN.equalsIgnoreCase(""+Bean.getErrorCode()) && "set_term".equalsIgnoreCase(action)) {
    String id_term_set = request.getParameter("id_term_set");
	Bean.setCurrentTerm(id_term_set);
	Bean.loginTerm.setIdTerm(id_term_set);
	Bean.loginTerm.getFeature();
	Bean.setErrorCode(0);
}

if (!isLogged || (isLogged && (Bean.C_MANY_USERS_ON_LOGIN.equalsIgnoreCase(""+Bean.getErrorCode())))) {%>
  <title><%=Bean.getTerminalTitle() %></title>
	 <%=Bean.getMetaContent() %>

	<meta content="width=device-width, initial-scale=0.8,maximum-scale=1.5,user-scalable=1" name="viewport">
	
	<!-- <link rel="stylesheet" href="CSS/login_smartphone.css" media="only screen and (min-device-width : 320px) and (max-device-width : 480px)"> -->
	<link rel="stylesheet" href="CSS/login.css">
	<!-- <link rel="stylesheet" href="CSS/login_smartphone.css" media="only screen and (max-device-width: 640px)"> -->
	<link type="image/x-icon" href="<%=Bean.getThemePath() %>/images/favicon.png" rel="shortcut icon"/>
	<script type="text/javascript" src="js/frame_emulator.js" > </script>
	<script type="text/javascript" src="js/webpos.js" > </script>
	<script type="text/javascript" src="js/wmpu_zoom.js" > </script> 
	<script type="text/javascript" src="<%=Bean.getThemePath() %>/js/formValidate-RU.js" > </script>

	<script type="text/javascript">
		//jQuery(document).ready(function(){
		//	zoom_element('login', 0.5);
		//});
	</script>		
<% } else { %>
		<META HTTP-EQUIV="PRAGMA" CONTENT="Cache-Control: max-age=0">
		<META HTTP-EQUIV="PRAGMA" CONTENT="NO-STORE">
		<META HTTP-EQUIV="CACHE-CONTROL" CONTENT="Cache-Control: max-age=0">
		<META HTTP-EQUIV="PRAGMA" CONTENT="NO-CACHE">
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
		<meta http-equiv="refresh">
		<meta content="width=device-width, initial-scale=1.0,maximum-scale=1.5,user-scalable=1" name="viewport">
		
		<title><%=Bean.getTerminalTitle() %></title>

	</head>
	<link type="image/x-icon" href="<%=Bean.getThemePath() %>/images/favicon.png" rel="shortcut icon"/>
	<script type="text/javascript" src="js/frame_emulator.js" > </script>
	
	<script type="text/javascript" src="js/dwr.js" > </script> 
	<script type="text/javascript" src="js/ajax_form_submit.js" > </script> 
	<script type="text/javascript" src="js/uwnd.js?2"></script>
	<script type="text/javascript" src="js/webpos.js" > </script> 
	<script type="text/javascript" src="js/wmpu_zoom.js" > </script>

	<script type="text/javascript" src="<%=Bean.getThemePath() %>/js/formValidate-RU.js" > </script>
	<script type="text/javascript" src="<%=Bean.getThemePath() %>/js/tooltips.js"></script> 
	<link rel="stylesheet" type="text/css" href="<%=Bean.getThemePath()%>/CSS/menu.css" />
	<link rel="stylesheet" type="text/css" href="<%=Bean.getThemePath()%>/CSS/webpos.css" />
	<!-- <link rel="stylesheet" href="<%=Bean.getThemePath()%>/CSS/webpos_smartphone.css" media="only screen and (max-device-width: 640px)"> -->


	<link rel="stylesheet" type="text/css" href="<%=Bean.getThemePath()%>/CSS/calendar.css" />
    <script type="text/javascript" src="<%=Bean.getThemePath() %>/js/calendar-emix.js"></script>
    <script type="text/javascript" src="<%=Bean.getThemePath() %>/js/calendar-RU.js"></script>
    <script type="text/javascript" src="<%=Bean.getThemePath() %>/js/calendar-setup.js"></script>

    <%= Bean.getGoBackScript() %>


 	<link type="text/css" href="<%=Bean.getThemePath() %>/CSS/uWnd.css" rel="stylesheet" />


	<%=Bean.getColoredScript() %>
	

<% } %>
<body>
	<div id="div_document">

<% if (!isLogged) { %>

<% boolean needCapcha = false;
   if (Bean.getErrorConnectionCount() > Bean.C_MAX_ERROR_CONNECTION_COUNT) {
	   needCapcha = true;
   }
%>
<div id="div_login">
<form id="login" action="main.jsp" method="POST">
   	<input type="hidden" name="lang" value="<%=lang %>">
   	<input type="hidden" name="do" value="login">
    <%= Bean.getLoginFormTitle() %>
    <fieldset id="inputs">
		<span><%=Bean.webposXML.getfieldTransl("login_login", false) %></span>
        <input id="username" name="username" title="<%=Bean.webposXML.getfieldTransl("login_login", false) %>" autofocus="" required="" type="text" value="<%=tmpusername %>">
		<span><%=Bean.webposXML.getfieldTransl("login_password", false) %></span>
        <input id="password" name="password" title="<%=Bean.webposXML.getfieldTransl("login_password", false) %>" required="" type="password" value="<%=tmppassword %>">
		<% if (needCapcha) { %>
		<span><%=Bean.webposXML.getfieldTransl("capcha_title", false) %></span><br>
		<input type="text" id="txtInput" name="capcha" style="background-position: -1000px center; width: 205px;"/>
        <img id="cpc" />
		<% 
		}
		%>
    </fieldset>
    <fieldset id="buttons" style="width: 100%;">
		<div style="float: left;"><a class="forgot_password" onclick="ajaxpage('forgot_pwd.jsp?type=send_phone&lang=<%=lang %>', 'div_login')"><%=Bean.webposXML.getfieldTransl("forgot_password", false) %></a></div>
		<input style="float: right;" class="button" id="formsubmit" value="<%=Bean.buttonXML.getfieldTranslNoDiv(lang,"button_login") %>" type="submit" <% if (needCapcha) { %> onclick="return ValidCaptcha();" <% } %>>
    </fieldset>

	<% if (!Bean.isEmpty(errorCode)) { %>
    <fieldset id="results">
		<span id="error_description">
        <%
      //out.println("Error inported from session= "+error);
	  if (Bean.C_REMIND_PIN_ON_LOGIN.equalsIgnoreCase(errorCode)) {%>
			<script>
			sms_mask('sms');
			</script>
			<font color='red'><%=Bean.webposXML.getfieldTransl("forgot_password_sms1", false) %></font><br>
			<font color='red'><%=Bean.webposXML.getfieldTransl("forgot_password_sms2", false) %></font><br>
			<%= errorMessage %><br>
    		<input class="sms" id="sms" name="sms" placeholder="<%=Bean.C_REMIND_PASSWORD_SMS_PLACEHOLDER %>" required="" value="" maxlength="<%=Bean.C_REMIND_PASSWORD_SMS_PLACEHOLDER.length() %>">
	  <% } else if (Bean.C_MAX_ERROR_CONNECTION.equalsIgnoreCase(errorCode)) { %>
   		  	<font color='red'><%=errorMessage %></font><br>
			<a class="forgot_password" onclick="ajaxpage('forgot_pwd.jsp?type=send_phone&lang=<%=lang %>', 'div_login')"><%=Bean.webposXML.getfieldTransl("restore_access", false) %></a>
	  <% } else { %>
   		  	<font color='red'><%=errorMessage %></font><br>
	  <% } %>
		</span>
    </fieldset>
	<% } %>
	<fieldset id="languages">
	<% if ("EN".equalsIgnoreCase(lang)) { %>
			<span class="lang">EN</span>&nbsp;|&nbsp;
	<% } else { %>
			<a class="lang" href="main.jsp?lang=EN&username=<%=tmpusername %>">EN</a>&nbsp;|&nbsp;
	<% } %>
	<% if ("RU".equalsIgnoreCase(lang)) { %>
			<span class="lang">RU</span>&nbsp;|&nbsp;
	<% } else { %>
			<a class="lang" href="main.jsp?lang=RU&username=<%=tmpusername %>">RU</a>&nbsp;|&nbsp;
	<% } %>
	<% if ("UA".equalsIgnoreCase(lang)) { %>
			<span class="lang">UA</span>
	<% } else { %>
			<a class="lang" href="main.jsp?lang=UA&username=<%=tmpusername %>">UA</a>
	<% } %>
    </fieldset>
</form>
</div>

		<% if (needCapcha) { %>
		<script type="text/javascript">
			GenerateCaptcha();
		</script>
		<% } %>
	<%} else { 
	%>
      <% if (Bean.C_MANY_USERS_ON_LOGIN.equalsIgnoreCase(""+Bean.getErrorCode())) { %>
	<div id="div_login">
	<form id="login" action="main.jsp" method="POST">
	   	<input type="hidden" name="lang" value="<%=lang %>">
	   	<input type="hidden" name="do" value="set_term">
	    <%= Bean.getLoginFormTitle() %>
	    <fieldset id="inputs">
			<span><%=Bean.webposXML.getfieldTransl("login_login", false) %></span>
	        <input id="username" name="username" class="readonly" title="<%=Bean.webposXML.getfieldTransl("login_login", false) %>" autofocus="" required="" readonly type="text" value="<%=Bean.getLoginUserName() %>"><br>
			<span><%=Bean.webposXML.getfieldTransl("title_select_terminal", false) %></span>
			<select name="id_term_set" id="id_term_set" ><%= Bean.getAdminUserTermOptions(Bean.getLoginUserName(), false) %></select>
	    </fieldset>
	    <fieldset id="buttons">
			<input class="button" value="<%=Bean.buttonXML.getfieldTranslNoDiv(lang,"button_set") %>" type="submit">
			<a href="main.jsp?do=exit&lang=<%=lang %>" class="button"><%=Bean.buttonXML.getfieldTransl("button_back", false) %></a>
	    </fieldset>
	</form>
	</div>
	  <% } else { %>

		<div id="div_workarea">
			<div id="div_main_all">
				<div id="div_main">
							<div id="imgWait" class="imgWaitDialog" style="visibility: hidden"><img id="imageImgWait" src="<%=Bean.getThemePath()%>/images/ajax-loader-circle.gif" align="middle"></div>
					<div id="div_title_general">
						<div id="div_status_bar">
							<span class="rectangle_title"> <span class="menu_title">
							<span class="text_title">
							<%=Bean.getTitleUserParamShort(35) %></span></span></span>
							<a href="main.jsp?do=exit"><span title="<%=Bean.commonXML.getfieldTransl("title_exit", false) %>" class="small_button_title menu_exit rectangle rectangle_title_small"></span></a>
						</div>
					</div>
				<% if (!Bean.hasTerminalPermission(Bean.loginTerm)) { %>
					<%=Bean.getErrorPermissionMessage("", Bean.loginTerm) %>
				<% } else { %>
					<div id="img_menu">
					<table id="menu_table"> 
					<%=Bean.getWebPosTitleMenuHTML() %>
					</table>
					</div>
				<% } %>
				<div id="div_bottom_bar">&copy;&nbsp;2016&nbsp;<%=Bean.getTerminalTitle() %>.&nbsp;<%=Bean.commonXML.getfieldTransl("title_copyright", false) %></div>
				</div>
			</div>
		</div>
		<% } %>
	<%}  %>
	</div>
				
</body>

</html>
