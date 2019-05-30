<%@page contentType="text/html;charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcWebClientBean"  />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<%@page import="java.util.HashMap"%>
<%@page import="bc.*"%><html>
<%@page import="bc.AppConst;"%>

<%

AppConst.appPath = application.getRealPath("/");
//Bean.setContextPath(Bean.getFullContext(request));

%>

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


HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());
String 
	client_surname		= Bean.getDecodeParam(parameters.get("client_surname")),
	client_name			= Bean.getDecodeParam(parameters.get("client_name")),
	client_patronimyc	= Bean.getDecodeParam(parameters.get("client_patronimyc")),
	sex					= Bean.getDecodeParam(parameters.get("sex")),
	adr_city			= Bean.getDecodeParam(parameters.get("adr_city")),
	adr_street			= Bean.getDecodeParam(parameters.get("adr_street")),
	adr_house			= Bean.getDecodeParam(parameters.get("adr_house")),
	mobile				= Bean.getDecodeParam(parameters.get("mobile")),
	email				= Bean.getDecodeParam(parameters.get("email")),
	password			= Bean.getDecodeParam(parameters.get("password")),
	password_confirm	= Bean.getDecodeParam(parameters.get("password_confirm")),
	card				= Bean.getDecodeParam(parameters.get("card")),
	activation_code		= Bean.getDecodeParam(parameters.get("activation_code")),
	dont_have_card		= Bean.getDecodeParam(parameters.get("dont_have_card")),
	friend_card			= Bean.getDecodeParam(parameters.get("friend_card")),
	confirm_rule		= Bean.getDecodeParam(parameters.get("confirm_rule"));

String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".pack$web_client_ui.attach_card_initial(" +
	"?,?,?, ?,?,?,?,?,?)}";

String[] pParam = new String [16];
		
pParam[0] = client_surname;
pParam[1] = client_name;
pParam[2] = client_patronimyc;
pParam[3] = sex;
pParam[4] = adr_city;
pParam[5] = adr_street;
pParam[6] = adr_house;
pParam[7] = mobile;
pParam[8] = email;
pParam[9] = password;
pParam[10] = password_confirm;
pParam[11] = card;
pParam[12] = activation_code;
pParam[13] = dont_have_card;
pParam[14] = friend_card;
pParam[15] = confirm_rule;

String[] results = new String[3];

results 				= Bean.myCallFunctionParam(callSQL, pParam, 3);
String resultInt 		= results[0];
String id_nat_prs		= results[1];
String resultMessage 	= results[2];
%>
<body onload="document.getElementById('userInp').focus()">
		<script type="text/javascript">
			 if(top!=self)
			  top.location=self.location;
		</script>
	    	<%if ("0".equalsIgnoreCase(resultInt)) {
	    %>
<form id="login_forgot" action="registr/confirmupdate.jsp" method="POST" OnSubmit="return validate();">
   	<input type="hidden" name="lang" value="<%=lang %>">
    <input type="hidden" name="id_nat_prs" value="<%=id_nat_prs %>">
    <h1>СМС подтверждение</h1>
    <fieldset id="inputs">
		<span>На Ваш мобильный телефон отправлено СМС с кодом</span><br>
		<span>Ваш телефон: <font color="red"><%=mobile %></font></span><br><br>
		<span>Пароль из СМС</span><br>
        <input class="code" id="code1" name="code1" placeholder="XX" autofocus="" required="" type="text" value="" maxlength="2">   
        <input class="code" id="code2" name="code2" placeholder="XX" autofocus="" required="" type="text" value="" maxlength="2">   
        <input class="code" id="code3" name="code3" placeholder="XX" autofocus="" required="" type="text" value="" maxlength="2">   
        <input class="code" id="code4" name="code4" placeholder="XX" autofocus="" required="" type="text" value="" maxlength="2">   
		<span>(должен прийти на Ваш мобильный телефон)</span><br>
    </fieldset>
    <fieldset id="actions">
        <input id="submit" value="Подтвердить" type="submit">
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
<% } else { %>
<% } %>

</body>
</html>
