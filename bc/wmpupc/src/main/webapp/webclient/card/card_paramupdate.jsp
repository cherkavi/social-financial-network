<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcWebClientBean"  />

<%= Bean.getLogOutScript(request) %>

<%@page import="java.util.HashMap"%>
<%@page import="bc.objects.wcClubCardObject"%>
<%@page import="bc.objects.wcNatPrsObject"%><html>
<head>
	<%= Bean.getMetaContent() %>
</head>

<body>
<%
HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String pageFormName = "CARD_CHANGE";

request.setCharacterEncoding("UTF-8");
String type		= Bean.getDecodeParam(parameters.get("type")); 
String id		= Bean.getDecodeParam(parameters.get("id")); 
String action	= Bean.getDecodeParam(parameters.get("action")); 
String process	= Bean.getDecodeParam(parameters.get("process"));

if (id==null || ("".equalsIgnoreCase(id))) id="";
if (type==null || ("".equalsIgnoreCase(type))) type="";
if (action==null || ("".equalsIgnoreCase(action))) action="";
if (process==null || ("".equalsIgnoreCase(process))) process="";

if (type.equalsIgnoreCase("card")) {

	if (process.equalsIgnoreCase("no")) {
	    if (action.equalsIgnoreCase("add")) {%>
	<script>
	$('#defaultReal').realperson();
	</script> 

	<div id="div_card_add">
		<span style="font-size: 14px; font-weight: bold"><u>Добавить карту</u></span><br><br>
		Вы всегда можете добавить еще одну свою карту или карту члена семьи.<br>
		Это позволит просматривать историю покупок, вести общую бухгалтерию, вместе накапливать бонусы<br> 
		на одну из карт и другие преимущества.<br><br>
		<span style="display: block; border: 1px solid; padding: 5px">
		Внимание!<br><br>
		Для привязки карты к своему аккаунту, необходимо името доступ к мобильному телефону.<br>
		Если карта была привязана к другому аккаунту, от и доступ к мобильному телефону этого аккаунта.<br><br>
		</span><br><br>
		<form action="card/card_paramupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">
			<input type="hidden" name="type" value="card">
			<input type="hidden" name="action" value="addconfirm">
			<input type="hidden" name="process" value="no">
			<input type="hidden" name="id_nat_prs" value="1">
			<table class="tablebottom-filter">
				<tr>
					<td>Код карты</td><td><input type="text" name="cd_card1" size="20" value="" class="inputfield"></td>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td>Код активации карты</td><td><input type="text" name="activation_code" size="20" value="" class="inputfield"></td>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td>Код с картинки выше</td><td><input id="defaultReal" name="defaultReal" required="" type="text" value="" class="inputfield"></td>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td colspan=4><center>
						<%=Bean.getSubmitButtonAjax("card/card_paramupdate.jsp", "apply", "updateForm", "div_card_detail") %>
					</center>
					</td>
				</tr>
			</table>
		</form>
	</div>
	<%
	    } else if (action.equalsIgnoreCase("addconfirm")) {
	    	
	    	String 
	    		cd_card1		= Bean.getDecodeParam(parameters.get("cd_card1")),
	    		acivation_code	= Bean.getDecodeParam(parameters.get("acivation_code")),
	    		id_nat_prs_init	= Bean.getDecodeParam(parameters.get("id_nat_prs"));
	    	
	    	String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".pack$web_client_ui.attach_card_initial(" +
				"?,?,?, ?,?,?,?,?,?)}";
	
			String[] pParam = new String [3];
					
			pParam[0] = cd_card1;
			pParam[1] = acivation_code;
			pParam[2] = id_nat_prs_init;
			
			String[] results = new String[7];
			
			results 				= Bean.myCallFunctionParam(callSQL, pParam, 7);
			String resultInt 		= results[0];
			String cardid		 	= results[1];
			String iss	 			= results[2];
			String paysys 			= results[3];
			String id_nat_prs		= results[4];
			String phone_mobile		= results[5];
	 		String resultMessage 	= results[6];
	    	
		    wcClubCardObject clubcard = new wcClubCardObject(cardid, iss, paysys);
	    	
	    	if (!("0".equalsIgnoreCase(resultInt))) {
	    %>
	    <div id="div_card_add">
			<table class="tablebottom-filter">
				<thead>
				<tr>
					<th>Ошибка!</th>
				</tr>
				</thead>
				<tbody>
				<tr>
					<td>
						<%=cd_card1 %>: <%=resultMessage %>
					</td>
				</tr>
				</tbody>
				</table>
		</div>
	    <%
			} else {
	    
	    %>
			<div id="div_card_add">
			<form action="card/card_paramupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">
				<input type="hidden" name="cardid" value="<%=cardid %>">
    			<input type="hidden" name="iss" value="<%=iss %>">
    			<input type="hidden" name="paysys" value="<%=paysys %>">
    			<input type="hidden" name="id_nat_prs" value="<%=id_nat_prs %>">
    			<input type="hidden" name="phone_mobile" value="<%=phone_mobile %>">
    			<input type="hidden" name="type" value="card">
    			<input type="hidden" name="action" value="codeconfirm">
    			<input type="hidden" name="process" value="no">
    			<table class="tablebottom-filter">
				<thead>
				<tr>
					<th>СМС-подтверждение</th>
				</tr>
				</thead>
				<tbody>
				<tr>
					<td>
						<span>На Ваш мобильный телефон отправлено СМС с кодом</span><br>
						<span>Ваш телефон: <font color="red"><%=phone_mobile %></font></span><br><br>
						<span>Пароль из СМС</span><br>
					</td>
				</tr>
				<tr>
					<td>
						<input class="code" id="code1" name="code1" placeholder="XX" autofocus="" required="" type="text" size="5" value="" maxlength="2">   
				        <input class="code" id="code2" name="code2" placeholder="XX" required="" type="text" size="5"  value="" maxlength="2">   
				        <input class="code" id="code3" name="code3" placeholder="XX" required="" type="text" size="5"  value="" maxlength="2">   
				        <input class="code" id="code4" name="code4" placeholder="XX" required="" type="text" size="5"  value="" maxlength="2">   
						<br><span>(должен прийти на Ваш мобильный телефон)</span><br>
					</td>
				</tr>
				<tr>
					<td>
				        <%=Bean.getSubmitButtonAjax("card/card_paramupdate.jsp", "apply", "updateForm", "div_card_detail") %>
					</td>
				</tr>
				</tbody>
				</table>
			</form>
		</div>
			<% }

	    } else if (action.equalsIgnoreCase("codeconfirm")) {
	    	
	    	String 
	    		cardid			= Bean.getDecodeParam(parameters.get("cardid")),
	    		iss				= Bean.getDecodeParam(parameters.get("iss")),
	    		paysys			= Bean.getDecodeParam(parameters.get("paysys")),
	    		id_nat_prs		= Bean.getDecodeParam(parameters.get("id_nat_prs")),
	    		phone_mobile	= Bean.getDecodeParam(parameters.get("phone_mobile")),
	    		code1			= Bean.getDecodeParam(parameters.get("code1")),
	    		code2			= Bean.getDecodeParam(parameters.get("code2")),
	    		code3			= Bean.getDecodeParam(parameters.get("code3")),
	    		code4			= Bean.getDecodeParam(parameters.get("code4"));
	    	
	    	String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".pack$web_client_ui.attach_card_confirm(" +
				"?,?,?,?,?,?,?,?,?, ?)}";
	
			String[] pParam = new String [9];
					
			pParam[0] = cardid;
			pParam[1] = iss;
			pParam[2] = paysys;
			pParam[3] = id_nat_prs;
			pParam[4] = phone_mobile;
			pParam[5] = code1;
			pParam[6] = code2;
			pParam[7] = code3;
			pParam[8] = code4;
			
			String[] results = new String[2];
			
			results 				= Bean.myCallFunctionParam(callSQL, pParam, 2);
			String resultInt 		= results[0];
	 		String resultMessage 	= results[1];
	    	
		    wcClubCardObject clubcard = new wcClubCardObject(cardid, iss, paysys);
	    	
	    	if (!("0".equalsIgnoreCase(resultInt))) {
	    %>
	    <div id="div_card_add">
			<table class="tablebottom-filter">
				<thead>
				<tr>
					<th>Ошибка!</th>
				</tr>
				</thead>
				<tbody>
				<tr>
					<td>
						<%=resultMessage %>
					</td>
				</tr>
				</tbody>
				</table>
		</div>
	    <%
			} else {
	    
	    %>
			<jsp:forward page="main.jsp"></jsp:forward>
			<% }
	    } 
	} else if (process.equalsIgnoreCase("yes")) {
		String
			id_user						= Bean.loginUser.getValue("ID_USER"),
			card_serial_number			= Bean.getDecodeParam(parameters.get("id")),
			id_isssuer 					= Bean.getDecodeParam(parameters.get("iss")),
			id_payment_system			= Bean.getDecodeParam(parameters.get("paysys"));

		if (action.equalsIgnoreCase("add")) {
			
			String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK$UI_CARD.get_base_card(?,?,?,?)}";

			String[] pParam = new String [3];
			
			pParam[0] = card_serial_number;
			pParam[1] = id_isssuer;
			pParam[2] = id_payment_system;

    	 	%>
    		<%= Bean.getCallResultParam("UPDATE", callSQL, pParam, "/card/card_param.jsp", "") %>
    		<% 	
    		
    	} else { %> 
			<%= Bean.getUnknownActionText(action) %><% 
		}
	} else {
	    %> <%= Bean.getUnknownProcessText(process) %> <%
	}
} else {
    %> <%= Bean.getUnknownTypeText(type) %> <%
}

%>


</body>
</html>
