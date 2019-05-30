<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcWebClientBean"  />
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<%@page import="bc.AppConst"%>
<%@page import="bc.objects.bcTerminalObject"%>
<%@page import="bc.objects.bcClubCardObject"%><html>
	<head>

<% Bean.setSessionId(request.getSession().getId()); %>
<%
final int INVALID_USER_PASSWORD = 1017;
String lang = request.getParameter("lang");
if (lang == null || "".equalsIgnoreCase(lang)) {
	lang = "RU";
}
String tmpusername=request.getParameter("username");
String tmppassword=request.getParameter("password");

String action = request.getParameter("do");

if (action != null && action.equalsIgnoreCase("exit")){ // обработка выхода
    Bean.logOut(session);
}

boolean isLogged = false;
String error = "general";

if (Bean.logIn(tmpusername,tmppassword,"webclient",request)) { 
	isLogged = true;
} else {
    if (tmpusername==null || "".equalsIgnoreCase(tmpusername)) {
  	  error = "";
    }
} 
	//Bean.setLanguage(lang);
	//Bean.setContextPath(Bean.getFullContext(request));
	Bean.clearHmEntries();


if (!isLogged) {%>
  <title>WebClient - <%=AppConst.getInstanceName() %></title>
	 <%=Bean.getMetaContent() %>
	<link rel="shortcut icon" href="images/favicon.ico" type="image/x-icon"/>
	<link rel="stylesheet" type="text/css" href="CSS/login.css">

<% } else { %>
		<META HTTP-EQUIV="PRAGMA" CONTENT="Cache-Control: max-age=0">
		<META HTTP-EQUIV="PRAGMA" CONTENT="NO-STORE">
		<META HTTP-EQUIV="CACHE-CONTROL" CONTENT="Cache-Control: max-age=0">
		<META HTTP-EQUIV="PRAGMA" CONTENT="NO-CACHE">
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
		<meta http-equiv="refresh">
		<title>WebClient - <%=AppConst.getInstanceName() %></title>

	</head>
	<link type="image/x-icon" href="images/favicon.ico" rel="shortcut icon"/>
	<link href="CSS/div.css" rel="stylesheet" type="text/css">
	<script type="text/javascript" src="js/frame_emulator.js" > </script>
	<link rel="stylesheet" type="text/css" href="CSS/menu.css">
	<link rel="stylesheet" type="text/css" href="CSS/webclient.css">

	<script type="text/javascript" src="js/jquery-1.3.2.js"></script>
    <script type="text/javascript" src="js/ui.core.js"></script>
    <script type="text/javascript" src="js/ui.datepicker.js"></script>
    <script type="text/javascript" src="js/jquery-ui-i18n.min.js"></script>

	<link type="text/css" href="CSS/ui/ui.theme.css" rel="stylesheet" />
  	<link type="text/css" href="CSS/ui/ui.core.css" rel="stylesheet" />
 	<link type="text/css" href="CSS/ui/ui.accordion.css" rel="stylesheet" />
  	<link type="text/css" href="CSS/ui/ui.dialog.css" rel="stylesheet" />
  	<link type="text/css" href="CSS/ui/ui.progressbar.css" rel="stylesheet" />
  	<link type="text/css" href="CSS/ui/ui.resizable.css" rel="stylesheet" />
  	<link type="text/css" href="CSS/ui/ui.slider.css" rel="stylesheet" />
  	<link type="text/css" href="CSS/ui/ui.tabs.css" rel="stylesheet" />
  	<link type="text/css" href="CSS/ui/ui.datepicker.css" rel="stylesheet" />

	<script type="text/javascript" src="js/dwr.js" > </script> 
	<script type="text/javascript" src="js/ajax_form_submit.js" > </script> 
	<script type="text/javascript" src="js/webclient.js" > </script> 

	<link rel="stylesheet" type="text/css" href="CSS/jquery.realperson.css"> 
	<script type="text/javascript" src="js/jquery.plugin.js"></script> 
	<script type="text/javascript" src="js/jquery.realperson.js"></script>
<% } %>
	
	<% Bean.setCurrentUserClubCards(Bean.getLoginUserIDNatPrs()); %>

<body>
<div id="div_document">

<% if (!isLogged) { %>
<form id="login" action="main.jsp" method="POST">
   	<input type="hidden" name="lang" value="<%=lang %>">
    <h1>WebClient</h1>
    <fieldset id="inputs">
        <input id="username" name="username" placeholder="Логин" autofocus="" required="" type="text" value="wadmin">   
        <input id="password" name="password" placeholder="Пароль" required="" type="password" value="wadmin">
    </fieldset>
	<% if (!(error==null || "".equalsIgnoreCase(error))) { %>
    <fieldset id="results">
        <%
      if ("checkUser_bad_login".equalsIgnoreCase(error)) out.println("<font color='red'>" + Bean.commonXML.getfieldTranslNoDiv(lang,"login_registration_error") + "</font>");
      if ("general".equalsIgnoreCase(error)) out.println("<font color='red'>" + Bean.commonXML.getfieldTranslNoDiv(lang,"login_access_error") + "</font>");
      %>
    </fieldset>
	<% } %>
    <fieldset id="actions">
        <input id="submit" value="Войти" type="submit"><br><br>
		<a href="recover/recoverpwd.jsp" id="forget">Забыли пароль?</a><br>
		<a href="registr/add_client.jsp" id="registration">Регистрация</a><br>
    </fieldset>
</form>
<% } else { %>
		<div id="workarea">
		<div id="logo">
			
			<div id="slidetabsmenu">
			<ul>
			<!-- <li id="current"><a href="#"><span onclick="ajaxpage('action/action.jsp', 'div_main')">Действия</span></a></li> -->
			<li><a href="#"><span onclick="ajaxpage('card/card_param.jsp', 'div_main')">Карты</span></a></li>
			<li><a href="#"><span onclick="ajaxpage('operation/oper.jsp', 'div_main')">Операции</span></a></li>
			<li><a href="#"><span onclick="ajaxpage('profile/profile.jsp', 'div_main')">Профиль</span></a></li>
			<li><a href="main.jsp?do=exit" title="Выход"><span><font color="red"><%= Bean.buttonXML.getfieldTransl("quit", false) %></font></span></a></li>
			</ul>
			<img id="imgWait" src="images/ajax-loader-circle.gif" align="middle" style="visibility: hidden">
			</div>
		</div>
				<div id="div_main">
				<script type="text/javascript">
					ajaxpage('card/card_param.jsp', 'div_main')
 				</script>
			</div>
		</div>
<% } %>
</div>
</body>

</html>
