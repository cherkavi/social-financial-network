<%@page contentType="text/html;charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcWebClientBean"  />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<%@page import="java.util.HashMap"%>
<%@page import="bc.*"%>
<%@page import="java.sql.Connection"%>
<%@page import="bc.AppConst;"%>

<%

AppConst.appPath = application.getRealPath("/");
//Bean.setContextPath(Bean.getFullContext(request));


HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());
%>

<html>
	<script type="text/javascript" src="js/frame_emulator.js" > </script>
	<script type="text/javascript" src="js/webclient.js" > </script> 

	<%= Bean.getCalendarJS() %>

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


<head>
  <title>WebClient - <%=AppConst.getInstanceName() %></title>
	 <%=Bean.getMetaContent() %>
	<link rel="shortcut icon" href="images/favicon.ico" type="image/x-icon"/>
	<link rel="stylesheet" type="text/css" href="CSS/login.css">

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

String type		= Bean.getDecodeParam(parameters.get("type")); 

String 
	client_surname		= Bean.getValue(Bean.getDecodeParam(parameters.get("client_surname"))),
	client_name			= Bean.getValue(Bean.getDecodeParam(parameters.get("client_name"))),
	client_patronimyc	= Bean.getValue(Bean.getDecodeParam(parameters.get("client_patronimyc"))),
	sex					= Bean.getValue(Bean.getDecodeParam(parameters.get("sex"))),
	adr_city			= Bean.getValue(Bean.getDecodeParam(parameters.get("adr_city"))),
	adr_street			= Bean.getValue(Bean.getDecodeParam(parameters.get("adr_street"))),
	adr_house			= Bean.getValue(Bean.getDecodeParam(parameters.get("adr_house"))),
	mobile				= Bean.getValue(Bean.getDecodeParam(parameters.get("mobile"))),
	email				= Bean.getValue(Bean.getDecodeParam(parameters.get("email"))),
	password			= Bean.getValue(Bean.getDecodeParam(parameters.get("password"))),
	password_confirm	= Bean.getValue(Bean.getDecodeParam(parameters.get("password_confirm"))),
	card				= Bean.getValue(Bean.getDecodeParam(parameters.get("card"))),
	activation_code		= Bean.getValue(Bean.getDecodeParam(parameters.get("activation_code"))),
	dont_have_card		= Bean.getValue(Bean.getDecodeParam(parameters.get("dont_have_card"))),
	friend_card			= Bean.getValue(Bean.getDecodeParam(parameters.get("friend_card"))),
	confirm_rule		= Bean.getValue(Bean.getDecodeParam(parameters.get("confirm_rule")));

if (sex==null || "".equalsIgnoreCase(sex)) {
	sex = "M";
}
if (type==null || "".equalsIgnoreCase(type)) {
	type = "add";
}

String[] results = new String[3];

String resultInt 		= "1";
String id_nat_prs		= "";
String resultMessage 	= "";

if ("confirm".equalsIgnoreCase(type)) {
	String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".pack$web_client_ui.add_new_client(" +
		"?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?, ?,?)}";
	
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
	
	Bean.setWRConnection();
	Connection wrCon = Bean.getWRConnection();
	
	results 		= Bean.myCallFunctionParam(wrCon, callSQL, pParam, 3);
	resultInt 		= results[0];
	id_nat_prs		= results[1];
	resultMessage 	= results[2];
}

%>
<body onload="document.getElementById('userInp').focus()">
<div id="div_document">
		<script type="text/javascript">
			 if(top!=self) top.location=self.location;
		</script>

<% if ("add".equalsIgnoreCase(type) || ("confirm".equalsIgnoreCase(type) && !("0".equalsIgnoreCase(resultInt)))) { %>
	<form action="registr/add_client.jsp" name="registr_form" id="registr_form" accept-charset="UTF-8" method="POST">
   	<input type="hidden" name="lang" value="<%=lang %>">
    <input type="hidden" name="type" value="confirm">
    <h1>Регистрация</h1>

    <fieldset id="registr">
	<table border=0 width="100%">
		<tr>
			<td><input id="mobile" name="mobile" placeholder="Номер телефона (будет логином)" autofocus="" type="text" value="<%=mobile %>"></td>
			<td><input id="client_surname" name="client_surname" placeholder="Фамилия" type="text" value="<%=client_surname %>"></td>
		</tr>   
        <tr>
			<td><input id="email" name="email" placeholder="E-mail" type="text" value="<%=email %>"></td>
			<td><input id="client_name" name="client_name" placeholder="Имя" type="text" value="<%=client_name %>"></td>
		</tr>
        <tr>
			<td><input id="password" name="password" placeholder="Пароль" type="password" value="<%=password %>"></td>
			<td><input id="client_patronimyc" name="client_patronimyc" placeholder="Отчество" type="text" value="<%=client_patronimyc %>"></td>
		</tr>
        <tr>
			<td><input id="password_confirm" name="password_confirm" placeholder="Повторите пароль" type="password" value="<%=password_confirm %>"></td>
			<td>
				<%=Bean.getCalendarInputField("date_of_birth", "", "10") %>
				<input id="sex_male" name="sex" type="radio" value="M" <% if ("M".equalsIgnoreCase(sex)) { %>checked="checked"<% } %> style="width: 20px"><label for="sex_male">Мужчина</label>
				<input id="sex_female" name="sex" type="radio" value="F" <% if ("F".equalsIgnoreCase(sex)) { %>checked="checked"<% } %> style="width: 20px"><label for="sex_female">Женщина</label>
			</td>
		</tr>
        <tr>
			<td style="border-top: 1px dashed #ccc; padding-top: 10px"><input id="card" name="card" placeholder="Номер карты" type="text" value="<%=card %>"></td>
			<td style="border-top: 1px dashed #ccc; padding-top: 10px">Адрес проживания</td>
		</tr>
        <tr>
			<td><input id="activation_code" name="activation_code" placeholder="Код активации карты" type="text" value="<%=activation_code %>"></td>
			<td><input id="adr_city" name="adr_city" placeholder="Город" type="text" value="<%=adr_city %>"></td>
		</tr>
        <tr>
			<td><input id="dont_have_card" name="dont_have_card" type="checkbox" value="Y" style="width:30px"><label for="dont_have_card">У меня еще нет карты</label></td>
			<td>
				<input id="adr_street" name="adr_street" placeholder="Улица" type="text" value="<%=adr_street %>" style="width: 218px">
				<input id="adr_house" name="adr_house" placeholder="Дом" type="text" value="<%=adr_house %>" style="width: 130px">
			</td>
		</tr>
        <tr>
			<td><input id="frend_card" name="friend_card" placeholder="Номер карты друга" type="text" value=""></td>
			<% if ("confirm".equalsIgnoreCase(type) && !("0".equalsIgnoreCase(resultInt))) { %>
			<td rowspan=2 style="color:red; font-weight: bold; font-size: 10px">Ошибка: <%=resultMessage %>;</td>
			<% } %>
		</tr>
        <tr>
			<td><input id="confirm_rule" name="confirm_rule" type="checkbox" value="Y" style="width:30px"><label for="confirm_rule">Я согласен с</label> <a href="#">правилами СМУР</a></td>
		</tr>
	</table>
        <button id="submit" type="button" onclick="ajaxpage('/BonusDemo/webclient/registr/add_client.jsp?' + mySubmitForm('registr_form'),'div_document') " >Подтвердить</button>

    </fieldset>
</form>
<%= Bean.getCalendarScript("date_of_birth", false) %>
<% } else if ("confirm".equalsIgnoreCase(type)) { %>		
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
<% } %>
</div>
</body>
</html>
