<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcWebPosBean"  />

<%@page import="java.util.HashMap"%>
<%@page import="webpos.wpChequeObject"%>
<%

request.setCharacterEncoding("UTF-8");

String pageFormName = "CHEQUE";

HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String inv_id		= Bean.getDecodeParam(parameters.get("inv_id")); 
String InvId		= Bean.getDecodeParam(parameters.get("InvId")); 
String out_summ		= Bean.getDecodeParam(parameters.get("out_summ")); 
String OutSum		= Bean.getDecodeParam(parameters.get("OutSum")); 
String Culture		= Bean.getDecodeParam(parameters.get("Culture")); 
String IsTest		= Bean.getDecodeParam(parameters.get("IsTest")); 
String shp_Item		= Bean.getDecodeParam(parameters.get("shp_Item")); 

inv_id	= StringUtils.isEmpty(inv_id)?InvId:inv_id;
Culture = StringUtils.isEmpty(Culture)?"RU":Culture.toUpperCase();
%>


<%@page import="org.apache.commons.lang.StringUtils"%><html>
	<head>
	<link rel="stylesheet" type="text/css" href="../<%=Bean.getThemePath()%>/CSS/webpos.css">
	<script type="text/javascript" src="../js/frame_emulator.js" > </script>
	<script type="text/javascript" src="../js/webpos.js" > </script>
	<title>WebPOS - <%=Bean.commonXML.getfieldTransl2(Culture, "result_error") %></title>

	<meta content="width=device-width, initial-scale=0.8,maximum-scale=1.5,user-scalable=1" name="viewport">
	
	<link content="yes" name="apple-mobile-web-app-capable">
	<meta content="black" name="apple-mobile-web-app-status-bar-style">
</head>
<body>
<body>
<div id="div_document">
<div id="div_workarea">
<div id="div_main_all">
<table class="tabledetail tabledetail2"><tbody>
<tr>
	<td>
		<div id="div_action_big" style="height:300px;">
			<h1 class="error">Ошибка оплаты</h1>
			<table class="table_cheque"><tbody>
				<tr><td>Операция №</td><td><%=inv_id %></td></tr>
				<% if (!StringUtils.isEmpty(shp_Item)) { %>
				<tr><td>Получатель/счет</td><td><%=shp_Item %></td></tr>
				<% } %>
				<% if (!StringUtils.isEmpty(out_summ)) { %>
				<tr><td>Сумма операции</td><td><%=out_summ %></td></tr>
				<% } %>
			</tbody></table>
		</div>
	</td>
</tr>
</tbody>
</table>
</div>
</div>
</div>


</body>

<%@page import="webpos.wpTransactionObject"%></html>