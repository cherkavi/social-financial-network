<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>

<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<% Bean.setSessionId(request.getSession().getId()); %>

<%
String lang = request.getParameter("lang");
if (lang == null || "".equalsIgnoreCase(lang)) {
	lang = "RU";
}
String tmpusername=request.getParameter("username");
String tmppassword=request.getParameter("password");

String action = request.getParameter("do");

boolean isLogged = false;
String errorTmp = "";

if ("exit".equalsIgnoreCase(action)){ // обработка выхода
    Bean.logOut(session);
} else {
	if (Bean.logIn(tmpusername,tmppassword,"crm",request)) { 
		isLogged = true;
	} else {
		errorTmp = "" + Bean.getErrorCode();
	}
}
	Bean.clearHmEntries();

%>

<%@page import="bc.AppConst"%><html>
	<head>
		<META HTTP-EQUIV="PRAGMA" CONTENT="Cache-Control: max-age=0">
		<META HTTP-EQUIV="PRAGMA" CONTENT="NO-STORE">
		<META HTTP-EQUIV="CACHE-CONTROL" CONTENT="Cache-Control: max-age=0">
		<META HTTP-EQUIV="PRAGMA" CONTENT="NO-CACHE">
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
		<meta http-equiv="refresh">
		<title><%=Bean.commonXML.getfieldTranslNoDiv(lang,"application_name") %>: CRM</title>

	</head>
	<link type="image/x-icon" href="../images/favicon.png" rel="shortcut icon"/>
	<link href="CSS/div.css" rel="stylesheet" type="text/css">
	<script type="text/javascript" src="../js/frame_emulator.js" > </script>
	<script type="text/javascript" src="../js/tooltips.js"></script> 
	<link rel="stylesheet" type="text/css" href="../crm/CSS/hints.css">
	<%=Bean.getTopFrameCSS() %>
	<%= Bean.getBottomFrameCSS() %>
    	<%=Bean.getTableSorterCSS("[0,0]") %>
	<%= Bean.getFormValidateJS() %>
	<%= Bean.getGoBackScript() %>
	<%= Bean.getResponseUtilityJS() %>
		<%= Bean.getCalendarJS() %>

	<script type="text/javascript" src="../js/jquery-1.11.3.js"></script>
    <script type="text/javascript" src="../js/jquery-ui.js"></script>

	<script type="text/javascript" src="../CSS/menu/js/jquery.menu.js"></script>
    <link type="text/css" href="../CSS/menu/css/menu.css" rel="stylesheet" />
  	
    <script type="text/javascript" src="../js/ui.core.js"></script>
    <script type="text/javascript" src="../js/ui.datepicker.js"></script>

	<link type="text/css" href="../CSS/jquery-ui.css" rel="stylesheet" />
	<!-- 
  	<link type="text/css" href="../CSS/ui.theme.css" rel="stylesheet" />
  	<link type="text/css" href="../CSS/ui.core.css" rel="stylesheet" />
 	<link type="text/css" href="../CSS/ui.accordion.css" rel="stylesheet" />
  	<link type="text/css" href="../CSS/ui.dialog.css" rel="stylesheet" />
  	<link type="text/css" href="../CSS/ui.progressbar.css" rel="stylesheet" />
  	<link type="text/css" href="../CSS/ui.resizable.css" rel="stylesheet" />
  	<link type="text/css" href="../CSS/ui.slider.css" rel="stylesheet" />
  	<link type="text/css" href="../CSS/ui.tabs.css" rel="stylesheet" />
  	<link type="text/css" href="../CSS/ui.datepicker.css" rel="stylesheet" />
 -->
 	<script type="text/javascript" src="../js/main/dwr.js" > </script> 
	<script type="text/javascript" src="../js/main/comission.js" > </script> 
	<script type="text/javascript" src="../js/ajax_form_submit.js" > </script> 
	<script type="text/javascript" src="../js/uwnd.js?2"></script>
 	<link type="text/css" href="../CSS/uWnd.css" rel="stylesheet" />
	<script type="text/javascript" src="../js/bootstrap.js" > </script>
	<script type="text/javascript" src="../crm/js/crm.js" > </script>
	<script type="text/javascript" src="../crm/js/service.js" > </script>

	<link rel="stylesheet" type="text/css" href="../crm/CSS/login.css">

	<script type="text/javascript">
	 function validateCurrentClub() {
		 var currentClub = '<%=Bean.getCurrentClubID()%>';
		 if (currentClub=='' || currentClub=='null' || currentClub=='NULL') {
			 currentClub = '';
		 }
		 try {
		 	var formClub = document.getElementById("id_club").value;
		 	if (formClub=='' || formClub=='null' || formClub=='NULL') {
		 		formClub='';
		 	}
		 	//alert("currentClub="+currentClub+", formClub="+formClub);
		 	if (currentClub!='') {
			 	if (currentClub!=formClub) {
			 		var msg='<%=Bean.commonXML.getfieldTransl("h_change_club_confirm", false)%>';
					return window.confirm(msg);
			 	}
		 	}
		 } catch(err){
			 return true;
		 }
		 
		 return true;
	 }
		
	</script>

	<%=Bean.getColoredScript() %>

<%
	String pageId = request.getParameter("page_id");
	if (pageId==null) {
		pageId = "";
	}
%>

<body <% if (!"".equalsIgnoreCase(pageId)) {%>onload="ajaxpage('<%=pageId %>', 'div_main');" <%} %>>
<div id="div_document">

<%
	
	if (!isLogged) { 

      String error = "" + Bean.getErrorCode();
      if (tmpusername==null || "".equalsIgnoreCase(tmpusername)) {
    	  error = "";
      }
	   %>

<form id="login" action="main.jsp" method="POST">
   	<input type="hidden" name="lang" value="<%=lang %>">
	<img src="../images/favicon.png">
    <h1><%=Bean.commonXML.getfieldTranslNoDiv(lang,"application_name") %></h1>
    <fieldset id="inputs">
        <input id="username" name="username" placeholder="Логин" autofocus="" required="" type="text" value="">   
        <input id="password" name="password" placeholder="Пароль" required="" type="password" value="">
    </fieldset>
	<% if (!(error==null || "".equalsIgnoreCase(error))) { %>
   <fieldset id="results">
        <%
      //out.println("Error inported from session= "+error);
      if ("checkUser_bad_login".equalsIgnoreCase(error)) out.println("<font color='red'>" + Bean.commonXML.getfieldTranslNoDiv(lang,"login_registration_error") + "</font>");
      if ("general".equalsIgnoreCase(error)) out.println("<font color='red'>" + Bean.commonXML.getfieldTranslNoDiv(lang,"login_access_error") + "</font>");
      %>
<br>
<%=errorTmp %>
    </fieldset>
	<% } %>
    <fieldset id="actions">
        <input id="submit" value="Войти" type="submit">
    </fieldset>
 </form>
	<%

	} else { 
	%>
		<div id="menu-bar">
			<ul class="main-menu">
				<li><img src="../images/favicon.png"></li>
				<%= Bean.getMenuByKey("") %>
				<LI style="float: right; line-height: 14px;"><span><a style="color:red" href="main.jsp?do=exit"><%= Bean.buttonXML.getfieldTransl("quit", false) %></a></span></li>
				<LI style="float: right; line-height: 14px;"><span style="color:green" onclick="ajaxpage('../crm/setup/userparam.jsp', 'div_main')" title="<%= Bean.userXML.getfieldTransl("t_usersettings", false) %>"><%= Bean.loginUser.getValue("NAME_USER") %></span></li>
	        </ul>
		</div>
		
		<div id="workarea">
			<div id="div_main">
				
			</div>
		</div>
	
	<% if (Bean.hasAccessMenuPermission("SETUP_EVENTS")) { %>
	<script type="text/javascript">
		ajaxpage('../crm/setup/events.jsp', 'div_main')
 	</script>
	<% } %>
	<% } %>
</div>
</body>

</html>
