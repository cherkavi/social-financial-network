<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcWebClientBean"  />

<%= Bean.getLogOutScript(request) %>

<%@page import="java.util.HashMap"%><html>
<%

request.setCharacterEncoding("UTF-8");

String pageFormName = "WEBCLIENT_ACTION_ACTION";

Bean.setJspPageForTabName(pageFormName);

HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String tab = Bean.getDecodeParam(parameters.get("tab"));
if (tab == null || "".equalsIgnoreCase(tab)) {
	tab = Bean.tabsHmGetValue(pageFormName);
}
Bean.tabsHmSetValue(pageFormName, tab);

%>
<body>

	<table <%=Bean.getTableDetail2Param() %>>
		<tr>
			<td>
				<div id="div_action">
				<h1>Пополнить карту</h1>
				<form action="action/actionupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">
			        <input type="hidden" name="type" value="card">
			        <input type="hidden" name="action" value="put_money">
			        <input type="hidden" name="process" value="no">
					<table>
			  			<tr>
							<td>Сумма</td>
							<td>
								<input type="text" name="transfer_value" size="10" value="" class="inputfield">
								<select name="transfer_currency" class="inputfield"><option value="RUB">руб.</option></select>
							</td>
						</tr>
						<tr><td>На карту</td><td><select name="to_card" class="inputfield"><%= Bean.getWebClientCardOptions("", "ACTIVE", true) %></select></td></tr>
						<tr><td colspan=2  align="center"><%=Bean.getSubmitButtonAjax("/crm/cards/clubcardupdate.jsp") %></td></tr>
					</table>
				</form>
				</div>
			</td>
			<td>
				<div id="div_action">
				<h1>Зарегистрировать покупку у Партнера</h1>
				<form action="action/actionupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">
			        <input type="hidden" name="type" value="card">
			        <input type="hidden" name="action" value="set_payment">
			        <input type="hidden" name="process" value="no">
					<table>
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
						<tr><td colspan=2  align="center"><%=Bean.getSubmitButtonAjax("/crm/cards/clubcardupdate.jsp") %></td></tr>
					</table>
				</form>
				</div>
			</td>
		</tr>
		<tr>
			<td>
				<div id="div_action">
				<h1>Перевести на другую карту</h1>
				<form action="action/actionupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">
			        <input type="hidden" name="type" value="card">
			        <input type="hidden" name="action" value="put_to_another_card">
			        <input type="hidden" name="process" value="yes">
					<table>
						<tr><td>С карты</td><td><select name="from_card" class="inputfield"><%= Bean.getWebClientCardOptions("", "ALL", true) %></select></td></tr>
			  			<tr><td>На карту</td><td><input type="text" name="to_card" size="30" value=""  class="inputfield"></td></tr>
			  			<tr>
							<td>Сумма</td>
							<td>
								<input type="text" name="transfer_value" size="10" value="" class="inputfield">
								<select name="transfer_currency" class="inputfield"><option value="RUB">руб.</option></select>
							</td>
						</tr>
						<tr><td colspan=2  align="center"><%=Bean.getSubmitButtonAjax("/crm/cards/clubcardupdate.jsp") %></td></tr>
					</table>
				</form>
				</div>
			</td>
			<td>
				<div id="div_action">
				<h1>Личные сообщения</h1>
				<div id="div_message"><a href="#">Вам 100 рублей за рекомендацию другу</a></div>
				<div id="div_message"><a href="#">Получать призы стало удобнее</a></div>
				<div id="div_message"><a href="#">Купон на чашку кофе Americano от кафе <b>Аленка</b></a></div>
				</div>
			</td>
		</tr>
	</table>
</body>
</html>