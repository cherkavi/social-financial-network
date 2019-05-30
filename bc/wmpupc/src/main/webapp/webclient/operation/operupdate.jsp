<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcWebClientBean"  />

<%= Bean.getLogOutScript(request) %>

<%@page import="java.util.HashMap"%>
<%@page import="bc.objects.wcClubCardObject"%>
<%@page import="bc.objects.wcNatPrsObject"%>
<%@page import="bc.objects.bcAutoreconcilation2Object"%><html>
<head>
	<%= Bean.getMetaContent() %>

</head>

<body>
<%
HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String pageFormName = "WEBCLIENT_OPERATIONS";

request.setCharacterEncoding("UTF-8");
String type		= Bean.getDecodeParam(parameters.get("type")); 
String id		= Bean.getDecodeParam(parameters.get("id")); 
String action	= Bean.getDecodeParam(parameters.get("action")); 
String process	= Bean.getDecodeParam(parameters.get("process"));
String cd_card1	= Bean.getDecodeParam(parameters.get("cd_card1"));

if (id==null || ("".equalsIgnoreCase(id))) id="";
if (type==null || ("".equalsIgnoreCase(type))) type="";
if (action==null || ("".equalsIgnoreCase(action))) action="";
if (process==null || ("".equalsIgnoreCase(process))) process="";

if (type.equalsIgnoreCase("card")) {

	if (process.equalsIgnoreCase("no")) {
	    if (action.equalsIgnoreCase("add_bonus")) {
	       bcAutoreconcilation2Object  aa = new bcAutoreconcilation2Object();
	    %>
		<script>
		var formParam = new Array (
			new Array ('transfer_value', 'varchar2', 1),
			new Array ('transfer_currency', 'varchar2', 1),
			new Array ('to_card', 'varchar2', 1)
		);

		function myValidateForm(){
			return validateForm(formParam, "updateForm");
		}
	</script>
		<form action="operation/operupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return myValidateForm()">
			        <input type="hidden" name="type" value="card">
			        <input type="hidden" name="action" value="add_bonus2">
			        <input type="hidden" name="process" value="no">
					<table class="tablebottom-filter">
						<thead><tr>
							<th colspan=2>Пополнение карты</th>
						</tr>
						</thead>
						<tbody>
						<tr><td>&nbsp;</td></tr>
			  			<tr>
							<td>Сумма</td>
							<td>
								<input type="text" name="transfer_value" size="10" value="" class="inputfield">
								<select name="transfer_currency" class="inputfield"><%=Bean.getWPCurrencyShortNameOption("804", false) %></select>
							</td>
						</tr>
						<tr><td>На карту</td><td><select name="to_card" class="inputfield"><%= Bean.getWebClientCardOptions("", "ACTIVE", true) %></select></td></tr>
						<tr>
							<td colspan=2  align="center">
								<br><%=Bean.getSubmitButtonAjax("operation/operupdate.jsp", "apply", "updateForm", "div_oper_param", "myValidateForm") %>
							</td>
						</tr>
					</tbody>
					</table>
				</form>
	<%
	    } else if (action.equalsIgnoreCase("add_bonus2")) {
	    
	    	String
	    		to_card						= Bean.getDecodeParam(parameters.get("to_card")	),
	    		transfer_value				= Bean.getDecodeParam(parameters.get("transfer_value")),
	    		transfer_currency 			= Bean.getDecodeParam(parameters.get("transfer_currency"));
	    	
	    	String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".pack$web_client_ui.add_goods_to_card(" +
				"?,?,?, ?)}";
	
			String[] pParam = new String [3];
					
			pParam[0] = to_card;
			pParam[1] = transfer_value;
			pParam[2] = transfer_currency;
			
			String[] results = new String[2];
			
			results 				= Bean.myCallFunctionParam(callSQL, pParam, 2);
			String resultInt 		= results[0];
	 		String resultMessage 	= results[1];
	    	
	    	if (!("0".equalsIgnoreCase(resultInt))) {
	    %>
		<script>
		var formParam = new Array (
			new Array ('transfer_value', 'varchar2', 1),
			new Array ('transfer_currency', 'varchar2', 1),
			new Array ('to_card', 'varchar2', 1)
		);

		function myValidateForm(){
			return validateForm(formParam, "updateForm");
		}
	</script>
		<form action="operation/operupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return myValidateForm()">
			        <input type="hidden" name="type" value="card">
			        <input type="hidden" name="action" value="add_bonus2">
			        <input type="hidden" name="process" value="no">
					<table class="tablebottom-filter">
						<thead><tr>
							<th colspan=2 class="error">Ошибка пополнения карты</th>
						</tr>
						</thead>
						<tbody>
						<tr><td>&nbsp;</td></tr>
			  			<tr>
							<td>Сумма</td>
							<td>
								<input type="text" name="transfer_value" size="10" value="<%=transfer_value %>" class="inputfield">
								<select name="transfer_currency" class="inputfield"><%=Bean.getWPCurrencyShortNameOption(transfer_currency, false) %></select>
							</td>
						</tr>
						<tr><td>На карту</td><td><select name="to_card" class="inputfield"><%= Bean.getWebClientCardOptions(to_card, "ACTIVE", true) %></select></td></tr>
						<tr><td colspan=2 style="color:red"><%= resultMessage%></tr>
						<tr>
							<td colspan=2  align="center">
								<br><%=Bean.getSubmitButtonAjax("operation/operupdate.jsp", "apply", "updateForm", "div_oper_param", "myValidateForm") %>
							</td>
						</tr>
					</tbody>
					</table>
				</form>
	    <%
			} else {
	    
	    %>
		<table class="tablebottom-filter">
			<thead><tr>
				<th colspan=2 class="success">Пополнение карты</th>
			</tr>
			</thead>
			<tbody>
				<tr><td>&nbsp;</td></tr>
	  			<tr><td>Сумма</td><td><input type="text" name="transfer_value_frmt" size="10" value="<%=transfer_value %> <%=Bean.getWPCurrencyShortName(transfer_value) %>" readonly class="inputfield-ro"></td></tr>
	  			<tr><td>На карту</td><td><input type="text" name="to_card_frmt" size="20" value="<%=to_card %>" readonly class="inputfield-ro"></td></tr>
				<tr>
					<td colspan=2>
					<br><b><span style="color:red;">Заглушка!!!</span><br><br>
						В этом меню будет подключение к платежным сервисам для пополнения карты.<br>
						В тестовом варианте деньги в сумме <%=transfer_value %> <%=Bean.getWPCurrencyShortName(transfer_currency) %> зачислены на карту <%=to_card %></b>
					</td>
				</tr>
				<tr>
					<td colspan=2  align="center">
						<br><%=Bean.getSubmitButtonAjax("operation/operupdate.jsp", "apply", "updateForm", "div_oper_param") %>
					</td>
				</tr>
			</tbody>
			</table>
		</form>
	<%
			}
	    } else if (action.equalsIgnoreCase("transfer")) {
	    	
	    %>
		<script>
			var formParam = new Array (
				new Array ('from_card', 'varchar2', 1),
				new Array ('to_card', 'varchar2', 1),
				new Array ('transfer_value', 'varchar2', 1),
				new Array ('transfer_currency', 'varchar2', 1)
			);
	
			function myValidateForm(){
				return validateForm(formParam);
			}
		</script>
				<form action="operation/operupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return myValidateForm()">
			        <input type="hidden" name="type" value="card">
			        <input type="hidden" name="action" value="transfer2">
			        <input type="hidden" name="process" value="no">
					<table class="tablebottom-filter">
						<thead><tr>
							<th colspan=2>Перевод средств на другую карту</th>
						</tr>
						</thead>
						<tbody>
						<tr><td>&nbsp;</td></tr>
						<tr><td>С карты</td><td><select name="from_card" class="inputfield"><%= Bean.getWebClientCardOptions("", "ALL", true) %></select></td></tr>
			  			<tr><td>На карту</td><td><input type="text" name="to_card" size="30" value=""  class="inputfield"></td></tr>
			  			<tr>
							<td>Сумма</td>
							<td>
								<input type="text" name="transfer_value" size="10" value="" class="inputfield">
								<select name="transfer_currency" class="inputfield"><%=Bean.getWPCurrencyShortNameOption("804", false) %></select>
							</td>
						</tr>
						<tr>
							<td colspan=2  align="center">
								<br><%=Bean.getSubmitButtonAjax("operation/operupdate.jsp", "apply", "updateForm", "div_oper_param", "myValidateForm") %>
							</td>
						</tr>
					</tbody>
					</table>
				</form>
			<% 

	    } else if (action.equalsIgnoreCase("transfer2")) {
		    
	    	String
	    		from_card					= Bean.getDecodeParam(parameters.get("from_card")),
    			to_card						= Bean.getDecodeParam(parameters.get("to_card")),
	    		transfer_value				= Bean.getDecodeParam(parameters.get("transfer_value")),
	    		transfer_currency 			= Bean.getDecodeParam(parameters.get("transfer_currency"));
	    	
	    	String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".pack$web_client_ui.transfer_goods_from_to_card(" +
				"?,?,?,?, ?)}";
	
			String[] pParam = new String [4];
					
			pParam[0] = from_card;
			pParam[1] = to_card;
			pParam[2] = transfer_value;
			pParam[3] = transfer_currency;
			
			String[] results = new String[2];
			
			results 				= Bean.myCallFunctionParam(callSQL, pParam, 2);
			String resultInt 		= results[0];
	 		String resultMessage 	= results[1];
	    	
	    	if (!("0".equalsIgnoreCase(resultInt))) {
	    %>
		<script>
			var formParam = new Array (
				new Array ('from_card', 'varchar2', 1),
				new Array ('to_card', 'varchar2', 1),
				new Array ('transfer_value', 'varchar2', 1),
				new Array ('transfer_currency', 'varchar2', 1)
			);
	
			function myValidateForm(){
				return validateForm(formParam);
			}
		</script>
		<form action="operation/operupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return myValidateForm()">
			        <input type="hidden" name="type" value="card">
			        <input type="hidden" name="action" value="transfer2">
			        <input type="hidden" name="process" value="no">
					<table class="tablebottom-filter">
						<thead><tr>
							<th colspan=2 class="error">Ошибка перевода средств на другую карту</th>
						</tr>
						</thead>
						<tbody>
						<tr><td>&nbsp;</td></tr>
			  			<tr><td>С карты</td><td><select name="from_card" class="inputfield"><%= Bean.getWebClientCardOptions(from_card, "ALL", true) %></select></td></tr>
			  			<tr><td>На карту</td><td><input type="text" name="to_card" size="30" value="<%=to_card %>"  class="inputfield"></td></tr>
			  			<tr>
							<td>Сумма</td>
							<td>
								<input type="text" name="transfer_value" size="10" value="<%=transfer_value %>" class="inputfield">
								<select name="transfer_currency" class="inputfield"><%=Bean.getWPCurrencyShortNameOption(transfer_currency, false) %></select>
							</td>
						</tr>
						<tr><td colspan=2 style="color:red"><%= resultMessage%></tr>
						<tr>
							<td colspan=2  align="center">
								<br><%=Bean.getSubmitButtonAjax("operation/operupdate.jsp", "apply", "updateForm", "div_oper_param", "myValidateForm") %>
							</td>
						</tr>
					</tbody>
					</table>
				</form>
	    <%
			} else {
	    
	    %>
		<table class="tablebottom-filter">
			<thead><tr>
				<th colspan=2 class="success">Перевод средств на другую карту</th>
			</tr>
			</thead>
			<tbody>
				<tr><td>Операция выполнена успешно!</td></tr>
	  			<tr><td>С карты</td><td><input type="text" name="to_card_frmt" size="20" value="<%=from_card %>" readonly class="inputfield-ro"></td></tr>
				<tr><td>На карту</td><td><input type="text" name="to_card_frmt" size="20" value="<%=to_card %>" readonly class="inputfield-ro"></td></tr>
				<tr><td>Сумма</td><td><input type="text" name="transfer_value_frmt" size="10" value="<%=transfer_value %> <%=Bean.getWPCurrencyShortName(transfer_currency) %>" readonly class="inputfield-ro"></td></tr>
	  			<tr>
					<td colspan=2  align="center">
						<br><%=Bean.getSubmitButtonAjax("operation/operupdate.jsp", "apply", "updateForm", "div_oper_param") %>
					</td>
				</tr>
			</tbody>
			</table>
		</form>
	<%
			}
	    } else if (action.equalsIgnoreCase("set_purchase")) {
	    	
		    %>
					<form action="operation/operupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">
			        <input type="hidden" name="type" value="card">
			        <input type="hidden" name="action" value="set_purchase">
			        <input type="hidden" name="process" value="yes">
					<table class="tablebottom-filter">
						<thead><tr>
							<th colspan=2>Регистрация покупок у Партнеров</th>
						</tr>
						</thead>
						<tbody>
						<tr><td>&nbsp;</td></tr>
						<tr><td>Партрер</td><td><select name="id_dealer" class="inputfield"><%= Bean.getWebClientDealerOptions("", true) %></select></td></tr>
						<tr><td>Карта</td><td><select name="card" class="inputfield"><%= Bean.getWebClientCardOptions("", "ALL", true) %></select></td></tr>
			  			<tr><td>Код товара</td><td><input type="text" name="good_code" size="30" value="" class="inputfield"></td></tr>
			  			<tr>
							<td>Сумма</td>
							<td>
								<input type="text" name="transfer_value" size="10" value="" class="inputfield">
								<select name="transfer_currency" class="inputfield"><option value="RUB">руб.</option></select>
							</td>
						</tr>
			  			<tr><td>Дата покупки</td><td><input type="text" name="sell_date" size="10" value="" class="inputfield"></td></tr>
			  			<tr><td>Номер заказа</td><td><input type="text" name="order_number" size="30" value="" class="inputfield"></td></tr>
						<tr>
							<td colspan=2  align="center">
								Заглушка!!! Функция в процессе реализации
								<!-- <br><%=Bean.getSubmitButtonAjax("operation/operupdate.jsp", "apply", "updateForm", "div_oper_param") %> -->
							</td>
						</tr>
					</tbody>
					</table>
				</form>
				<% 
	    } else if (action.equalsIgnoreCase("reg_stratch")) {
	    	
		    %>
					<form action="action/actionupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">
				        <input type="hidden" name="type" value="card">
				        <input type="hidden" name="action" value="reg_stratch">
				        <input type="hidden" name="process" value="yes">
						<table class="tablebottom-filter">
							<thead><tr>
								<th colspan=2>Регистрация стреч-карт</th>
							</tr>
							</thead>
							<tbody>
							<tr><td>&nbsp;</td></tr>
							<tr><td>Номер бонусной карточки</td><td><input type="text" name="bonus_card" size="30" value=""  class="inputfield"></td></tr>
				  			<tr><td>Пин-код</td><td><input type="text" name="pin_code" size="30" value=""  class="inputfield"></td></tr>
							<tr>
							<td colspan=2  align="center">
								Заглушка!!! Функция в процессе реализации
								<!-- <br><%=Bean.getSubmitButtonAjax("operation/operupdate.jsp", "apply", "updateForm", "div_oper_param") %>-->
							</td>
						</tr>
						</tbody>
						</table>
					</form>
				<% 
	    } else if (action.equalsIgnoreCase("reg_bonus_code")) {
	    	
		    %>
					<form action="action/actionupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">
				        <input type="hidden" name="type" value="card">
				        <input type="hidden" name="action" value="reg_bonus_code">
				        <input type="hidden" name="process" value="yes">
						<table class="tablebottom-filter">
							<thead><tr>
								<th colspan=2>Регистрация бонусных кодов</th>
							</tr>
							</thead>
							<tbody>
							<tr><td>&nbsp;</td></tr>
							<tr><td>Бонусный код</td><td><input type="text" name="bonus_code" size="30" value=""  class="inputfield"></td></tr>
				  			<tr>
							<td colspan=2  align="center">
								Заглушка!!! Функция в процессе реализации
								<!-- <br><%=Bean.getSubmitButtonAjax("operation/operupdate.jsp", "apply", "updateForm", "div_oper_param") %>-->
							</td>
						</tr>
						</tbody>
						</table>
					</form>
				<% 
	    } else if (action.equalsIgnoreCase("get_virtual_card")) {
	    	
		    %>
			<script>
			$('#defaultReal').realperson();
			</script> 
					<form action="action/actionupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">
				        <input type="hidden" name="type" value="card">
				        <input type="hidden" name="action" value="get_virtual_card">
				        <input type="hidden" name="process" value="yes">
						<table class="tablebottom-filter">
							<thead><tr>
								<th colspan=2>Получить виртуальную карту</th>
							</tr>
							</thead>
							<tbody>
							<tr><td>&nbsp;</td></tr>
							<tr><td>Номер карты</td><td><input type="text" name="cd_card1" size="30" value="990002123456" readonly class="inputfield-ro"></td></tr>
				  			<tr><td>Номер телефона</td><td><input type="text" name="phone_mobile" size="30" value="+38"  class="inputfield"></td></tr>
				  			<tr><td>Код с картинки выше</td><td><input id="defaultReal" name="defaultReal" required="" type="text" value="" class="inputfield"></td></tr>
							<tr>
							<td colspan=2  align="center">
								Заглушка!!! Функция в процессе реализации
								<!-- <br><%=Bean.getSubmitButtonAjax("operation/operupdate.jsp", "apply", "updateForm", "div_oper_param") %>-->
							</td>
						</tr>
						</tbody>
						</table>
					</form>
				<% 
			    } 
	} else if (process.equalsIgnoreCase("yes")) {

		if (action.equalsIgnoreCase("add_bonus")) {
			
			String
				card_serial_number			= Bean.getDecodeParam(parameters.get("cardid")),
				id_isssuer 					= Bean.getDecodeParam(parameters.get("iss")),
				id_payment_system			= Bean.getDecodeParam(parameters.get("paysys")),
				transfer_value				= Bean.getDecodeParam(parameters.get("transfer_value")),
				transfer_currency			= Bean.getDecodeParam(parameters.get("transfer_currency"));
			
			String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK$WEB_CLIENT_UI.add_goods_to_card(" +
					"?,?,?,?,?, ?)}";

			String[] pParam = new String [5];
			
			pParam[0] = card_serial_number;
			pParam[1] = id_isssuer;
			pParam[2] = id_payment_system;
			pParam[3] = transfer_value;
			pParam[4] = transfer_currency;
			
			String[] results = new String[2];
			
			results 				= Bean.myCallFunctionParam(callSQL, pParam, 2);
			String resultInt 		= results[0];
	 		String resultMessage 	= results[1];

    	 	if (!("0".equalsIgnoreCase(resultInt))) {
	    %>
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
	    <%
			} else {
	    
	    %>
			<jsp:forward page="main2.jsp"></jsp:forward>
			<% }
    		
    	} else if (action.equalsIgnoreCase("transfer")) {
			
			String
				from_card					= Bean.getDecodeParam(parameters.get("from_card")),
				to_card 					= Bean.getDecodeParam(parameters.get("to_card")),
				transfer_value				= Bean.getDecodeParam(parameters.get("transfer_value")),
				transfer_currency			= Bean.getDecodeParam(parameters.get("transfer_currency"));
			
			String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK$WEB_CLIENT_UI.transfer_goods_from_to_card(" +
					"?,?,?,?, ?)}";

			String[] pParam = new String [4];
			
			pParam[0] = from_card;
			pParam[1] = to_card;
			pParam[2] = transfer_value;
			pParam[3] = transfer_currency;
			
			String[] results = new String[2];
			
			results 				= Bean.myCallFunctionParam(callSQL, pParam, 2);
			String resultInt 		= results[0];
	 		String resultMessage 	= results[1];

    	 	if (!("0".equalsIgnoreCase(resultInt))) {
	    %>
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
	    <%
			} else {
	    
	    %>
			<jsp:forward page="main2.jsp"></jsp:forward>
			<% }
    		
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
