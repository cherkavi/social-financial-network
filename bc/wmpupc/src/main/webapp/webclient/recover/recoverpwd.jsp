<%@page contentType="text/html;charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcWebClientBean"  />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<%@page import="bc.*"%><html>
<%@page import="bc.AppConst;"%>

<head>
  <title>WebClient - <%=AppConst.getInstanceName() %></title>
	 <%=Bean.getMetaContent() %>
	<link rel="shortcut icon" href="images/favicon.ico" type="image/x-icon"/>
	<link rel="stylesheet" type="text/css" href="CSS/login.css">

	<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js"></script>
	<link rel="stylesheet" type="text/css" href="CSS/jquery.realperson.css"> 
	<script type="text/javascript" src="js/jquery.plugin.js"></script> 
	<script type="text/javascript" src="js/jquery.realperson.js"></script>
</head>
<%

String lang = request.getParameter("lang");
if (lang == null || "".equalsIgnoreCase(lang)) {
	lang = "RU";
} else {
	if (!"RU".equalsIgnoreCase(lang) &&
		!"UA".equalsIgnoreCase(lang) &&
		!"EN".equalsIgnoreCase(lang)) {
		lang = "RU";
	}
}
String action = request.getParameter("do");
	if (action != null && action.equalsIgnoreCase("exit")){ // обработка выхода
	    Bean.logOut(session);
	}

%>
<body onload="document.getElementById('userInp').focus()">
		<script type="text/javascript">
			 if(top!=self)
			  top.location=self.location;
		</script>

<form id="login_forgot" action="webclient.jsp" method="POST" OnSubmit="return validate();">
   	<input type="hidden" name="lang" value="<%=lang %>">
    <h1>Восстановление пароля</h1>
    <fieldset id="inputs">
        <input id="mobile" name="mobile" placeholder="Номер мобильного телефона" autofocus="" required="" type="text" value="">   
        <input id="email" name="email" placeholder="E-mail" required="" type="text" value="">
		<input id="defaultReal" name="defaultReal" placeholder="Введите текст" required="" type="text" value="">
		<script>
		$('#defaultReal').realperson();
		</script> 
    </fieldset>
    <fieldset id="actions">
        <input id="submit" value="Восстановить" type="submit">
    </fieldset>
    <fieldset id="results">
        <%
      String error = request.getParameter("error");
      //out.println("Error inported from session= "+error);
      if ("checkUser_bad_login".equalsIgnoreCase(error)) out.println("<font color='red'>" + Bean.commonXML.getfieldTranslNoDiv(lang,"login_registration_error") + "</font>");
      if ("general".equalsIgnoreCase(error)) out.println("<font color='red'>" + Bean.commonXML.getfieldTranslNoDiv(lang,"login_access_error") + "</font>");
      //out.println(error);
      if (request.getParameter("error")==null) out.println("");
      %>
    </fieldset>
</form>


</body>
</html>
