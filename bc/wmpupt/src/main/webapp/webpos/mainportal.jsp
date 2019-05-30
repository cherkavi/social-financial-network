<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcWebPosBean"  />
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<%@page import="bc.AppConst"%>
<%@page import="webpos.wpTerminalObject"%><html>
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
String tmpterminal=request.getParameter("terminal");

String action = request.getParameter("do");

if (action != null && action.equalsIgnoreCase("exit")){ // обработка выхода
    Bean.logOut(session);
}

boolean isLogged = false;
String error = "general";

if (Bean.logIn(tmpusername,tmppassword,"webposportal",request)) { 
	isLogged = true;
	wpTerminalObject term = new wpTerminalObject(tmpterminal);
	term.getFeature();
	String id_term = term.getValue("ID_TERM"); //Bean.loginUserParam.getValue("CURRENT_TERM");
	if (id_term==null || "".equalsIgnoreCase(id_term)) {
		isLogged = false;
		error = "invalid_terminal";
	} else if (!("BON_TERMINAL".equalsIgnoreCase(term.getValue("CD_TERM_TYPE")))) {
		isLogged = false;
		error = "invalid_terminal_type";
	} else {
		Bean.setCurrentTerm(tmpterminal);
	}
} else {
    if (tmpusername==null || "".equalsIgnoreCase(tmpusername)) {
  	  error = "login_param_empty";
    }
	  if (INVALID_USER_PASSWORD == Bean.getErrorCode()) {
		  error = "checkUser_bad_login";
	  }
} 
	//Bean.setLanguage(lang);
	//Bean.setContextPath(Bean.getFullContext(request));
	Bean.clearHmEntries();


if (!isLogged) {%>
  <title>WebPOS - <%=AppConst.getInstanceName() %></title>
	 <%=Bean.getMetaContent() %>
	<link rel="shortcut icon" href="/images/favicon.ico" type="image/x-icon"/>
	<link rel="stylesheet" type="text/css" href="CSS/login.css">

<% } else { %>
		<META HTTP-EQUIV="PRAGMA" CONTENT="Cache-Control: max-age=0">
		<META HTTP-EQUIV="PRAGMA" CONTENT="NO-STORE">
		<META HTTP-EQUIV="CACHE-CONTROL" CONTENT="Cache-Control: max-age=0">
		<META HTTP-EQUIV="PRAGMA" CONTENT="NO-CACHE">
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
		<meta http-equiv="refresh">
		<title>WebPos - <%=AppConst.getInstanceName() %></title>

	</head>
	<link type="image/x-icon" href="images/favicon.ico" rel="shortcut icon"/>
	<link href="CSS/div.css" rel="stylesheet" type="text/css">
	<script type="text/javascript" src="js/frame_emulator.js" > </script>
	<script type="text/javascript" src="js/tooltips.js"></script> 
	<link rel="stylesheet" type="text/css" href="CSS/hints.css">
	<link rel="stylesheet" type="text/css" href="CSS/menu.css">
	<link rel="stylesheet" type="text/css" href="CSS/webpos.css">
    <%=Bean.getTableSorterCSS("[0,0]") %>
	<%= Bean.getFormValidateJS() %>
	<%= Bean.getGoBackScript() %>
	<%= Bean.getResponseUtilityJS() %>
	<%= Bean.getCalendarJS() %>

	<script type="text/javascript" src="js/jquery-1.3.2.js"></script>
    <script type="text/javascript" src="js/ui.core.js"></script>
    <script type="text/javascript" src="js/ui.datepicker.js"></script>
    <script type="text/javascript" src="js/jquery-ui-i18n.min.js"></script>

	<link type="text/css" href="CSS/ui.theme.css" rel="stylesheet" />
  	<link type="text/css" href="CSS/ui.core.css" rel="stylesheet" />
 	<link type="text/css" href="CSS/ui.accordion.css" rel="stylesheet" />
  	<link type="text/css" href="CSS/ui.dialog.css" rel="stylesheet" />
  	<link type="text/css" href="CSS/ui.progressbar.css" rel="stylesheet" />
  	<link type="text/css" href="CSS/ui.resizable.css" rel="stylesheet" />
  	<link type="text/css" href="CSS/ui.slider.css" rel="stylesheet" />
  	<link type="text/css" href="CSS/ui.tabs.css" rel="stylesheet" />
  	<link type="text/css" href="CSS/ui.datepicker.css" rel="stylesheet" />

	<script type="text/javascript" src="js/main/dwr.js" > </script> 
	<script type="text/javascript" src="js/main/comission.js" > </script> 
	<script type="text/javascript" src="js/ajax_form_submit.js" > </script> 
	<script type="text/javascript" src="js/uwnd.js?2"></script>
 	<link type="text/css" href="CSS/uWnd.css" rel="stylesheet" />
	<script type="text/javascript" src="js/webpos.js" > </script> 

	<%=Bean.getColoredScript() %>

<% } %>



<body>
	<div id="div_document">

<% if (!isLogged) { %>

<form id="login">
   	<input type="hidden" name="lang" value="<%=lang %>">
    <h1>WebPOS</h1>
    <% if (!(error==null || "".equalsIgnoreCase(error))) { %>
    <fieldset id="results">
        <%
      //out.println("Error inported from session= "+error);
      if ("checkUser_bad_login".equalsIgnoreCase(error)) out.println("<font color='red'>" + Bean.commonXML.getfieldTranslNoDiv(lang,"login_registration_error") + "</font>");
      if ("general".equalsIgnoreCase(error)) out.println("<font color='red'>" + Bean.commonXML.getfieldTranslNoDiv(lang,"login_access_error") + "</font>");
      if ("invalid_terminal".equalsIgnoreCase(error)) out.println("<font color='red'>Неправильный номер терминала</font>");
      if ("invalid_terminal_type".equalsIgnoreCase(error)) out.println("<font color='red'>Неправильный тип терминала</font>");
      //out.println(error);
      if (request.getParameter("error")==null) out.println("");
      %>
    </fieldset>
	<% } %>
</form>

	<%} else { %>
		<% Bean.loginUser.getCurrentUserFeature(); %>
		<% 	String id_term = Bean.getCurrentTerm();%>
		<div id="workarea">
			<div id="div_logo">
				<div>
				<table>
					<tr>
						<td><span id="webpos_title" onclick="ajaxpage('action/action.jsp', 'div_main')">WebPos</span><img id="imgWait" src="images/ajax-loader-circle.gif" align="middle" style="visibility: hidden"></td>
						<td style="text-align: center;">Пользователь <b><%=Bean.loginUser.getValue("NAME_USER") %></b></td>
						<td style="text-align: center;">Терминал <span id="term_title" title="Терминал" onclick="ajaxpage('payservice/term_param.jsp', 'div_main')"><%=id_term %></span></td>
						<td style="text-align: right;">&nbsp;</td>
					</tr>
				</table>
				</div>
				
				<div id="slidetabsmenu">
				<ul>
				<li id="current"><a href="#"><span onclick="ajaxpage('service/bon_term.jsp', 'div_main')">Оплатить</span></a></li>
				<li><a href="#"><span onclick="ajaxpage('service/replenish_card.jsp', 'div_main')">Пополнить карту</span></a></li>
				<li><a href="#"><span onclick="ajaxpage('service/new_client.jsp', 'div_main')">Выдать карту</span></a></li>
				<li><a href="#"><span onclick="ajaxpage('service/transfer.jsp', 'div_main')">Перевести на другую карту</span></a></li>
				<li><a href="#"><span onclick="ajaxpage('service/coupon.jsp', 'div_main')">Выдать/проверить купон</span></a></li>
				<li><a href="#"><span onclick="ajaxpage('service/mobile.jsp', 'div_main')">Пополнить моб.телефон</span></a></li>
				</ul>
				</div>
			</div>
			<div id="div_main">
				<div id="div_action">
				<script type="text/javascript">
					ajaxpage('action/action.jsp', 'div_main')
				</script>
				</div>
			</div>
		</div>
	<%}  %>
	</div>
</body>

</html>
