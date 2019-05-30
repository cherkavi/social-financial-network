<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcWebClientBean"  />

<%=Bean.getLogOutScript(request)%>

<%@page import="java.util.HashMap"%>
<%@page import="bc.objects.wcClubCardObject"%>
<%@page import="bc.objects.wcNatPrsObject"%><html>
<%
	request.setCharacterEncoding("UTF-8");

String pageFormName = "WEBCLIENT_OPER_PARAM";

HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());
%>
<body>
	<table <%=Bean.getTableDetail2Param() %>>
		<tr>
			<td>
				<div id="div_oper_list">
					<div class="div_oper_group">Операции с картой</div>
					<div class="div_oper" onclick="ajaxpage('operation/operupdate.jsp?type=card&action=add_bonus&process=no', 'div_oper_param')">Поплнение карты</div>
					<div class="div_oper" onclick="ajaxpage('operation/operupdate.jsp?type=card&action=transfer&process=no', 'div_oper_param')">Перевод на другую карту</div>
					<div class="div_oper" onclick="ajaxpage('operation/operupdate.jsp?type=card&action=set_purchase&process=no', 'div_oper_param')">Регистрация покупок</div>
					<div class="div_oper" onclick="ajaxpage('operation/operupdate.jsp?type=card&action=reg_stratch&process=no', 'div_oper_param')">Регистрация стреч-карт</div>
					<div class="div_oper" onclick="ajaxpage('operation/operupdate.jsp?type=card&action=reg_bonus_code&process=no', 'div_oper_param')">Регистрация бонусных кодов</div>
					<div class="div_oper" onclick="ajaxpage('operation/operupdate.jsp?type=card&action=get_virtual_card&process=no', 'div_oper_param')">Заказ виртуальной карты</div>
				</div>
			</td>
			<td>
				<div id="div_oper_param">
				</div>
			</td>
		</tr>
	</table>
</body>
</html>