<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcWebPosBean"  />

<%@page import="java.util.HashMap"%>
<%@page import="webpos.wpClubCardObject"%>
<%@page import="webpos.wpNatPrsObject"%>
<%@page import="webpos.wpChequeObject"%>
<%@page import="webpos.wpTerminalObject"%>
<%@page import="java.util.ArrayList"%>
<%@page import="webpos.wpNatPrsTargetPrgObject"%>
<%@ page import = "javax.servlet.RequestDispatcher" %>

<%@page import="webpos.wpTelegramObject"%><html>
<head>
	<%= Bean.getMetaContent() %>
	<link rel="stylesheet" type="text/css" href="../<%=Bean.getThemePath()%>/CSS/webpos.css">
	<script type="text/javascript" src="../js/frame_emulator.js" > </script>
	<script type="text/javascript" src="../js/webpos.js" > </script>

</head>

<body>
<div id="div_main">
<%
HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String pageFormName = "WEBPOS_SERVICE_OPERATION";

Bean.setJspPageForTabName(pageFormName);

request.setCharacterEncoding("UTF-8");
String type		= Bean.getDecodeParam(parameters.get("type")); 
String action	= Bean.getDecodeParam(parameters.get("action")); 
String process	= Bean.getDecodeParam(parameters.get("process"));

if (type==null || ("".equalsIgnoreCase(type))) type="";
if (action==null || ("".equalsIgnoreCase(action))) action="";
if (process==null || ("".equalsIgnoreCase(process))) process="";

String id_trans	= Bean.getDecodeParam(parameters.get("id"));

Bean.readWebPosMenuHTML();

wpTerminalObject term = new wpTerminalObject(Bean.getCurrentTerm());
term.getTermFeature();

String[] results = new String[2];
results[0] = Bean.C_SUCCESS_RESULT;
results[1] = "";
String resultInt 				= results[0];
String resultMessage 			= results[1];
%>


<%if (type.equalsIgnoreCase("trans")) {
	
	wpTelegramObject oper = new wpTelegramObject(id_trans);
	
	if (process.equalsIgnoreCase("no")) {%>

	<form name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST">
			<input type="hidden" name="type" value="trans">
			<input type="hidden" name="process" value="yes">
			<input type="hidden" name="id" value="<%=id_trans %>">
			<input type="hidden" name="id_term" id="id_term" value="<%=oper.getValue("ID_TERM") %>">
			<input type="hidden" name="cd_card1" id="cd_card1" value="<%=oper.getValue("CD_CARD1") %>">
		    <table class="action_table">
				<tr><td <%=Bean.getDetailTableFirstColumnWidth() %>><%=Bean.webposXML.getfieldTransl("cheque_number_full", false) %></td><td><%=oper.getValue("NC_TERM") %></td></tr>
				<tr><td colspan="2">
				Заглушка!!!<br>
				При нажатии кнопки "Оплатить" будет осуществлена эмуляция успешной оплаты через сервис Робокасса<br><br>
				<%=Bean.getSubmitButtonAjax("/wmpupt/webpos/test/test_robokassa.jsp", "pay", "updateForm", "div_main") %>
			</td></tr>

			</table>
		</form>
		
	<%} else if (process.equalsIgnoreCase("yes")) {
		ArrayList<String> pParam = new ArrayList<String>();
			
		pParam.add(id_trans);
		pParam.add("PAID");
			
		results 		= Bean.executeFunction("PACK$WEBPOS_UI.set_trans_external_state", pParam, results.length);
		resultInt 		= results[0];
	 	resultMessage 	= results[1];
 		
 		oper = new wpTelegramObject(id_trans);
 		
 		if (Bean.C_SUCCESS_RESULT.equalsIgnoreCase(resultInt)) {%>

 		<form name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST">
 				<input type="hidden" name="type" value="trans">
 				<input type="hidden" name="process" value="yes">
 				<input type="hidden" name="id" value="<%=id_trans %>">
 				<input type="hidden" name="id_term" id="id_term" value="<%=oper.getValue("ID_TERM") %>">
 				<input type="hidden" name="cd_card1" id="cd_card1" value="<%=oper.getValue("CD_CARD1") %>">
 			    <table class="action_table">
					<tr><td <%=Bean.getDetailTableFirstColumnWidth() %>><%=Bean.webposXML.getfieldTransl("cheque_number_full", false) %></td><td><%=oper.getValue("NC_TERM") %></td></tr>
 					<tr><td colspan="2">
 					Заглушка!!!<br>
 					Эмуляция оплаты через сервис Робокасса выполнена успешно - операция оплачена!
 				</td></tr>

 				</table>
 			</form>
 			
 		<%} else {%>

 		<form name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST">
 				<input type="hidden" name="type" value="trans">
 				<input type="hidden" name="process" value="yes">
 				<input type="hidden" name="id" value="<%=id_trans %>">
 				<input type="hidden" name="id_term" id="id_term" value="<%=oper.getValue("ID_TERM") %>">
 				<input type="hidden" name="cd_card1" id="cd_card1" value="<%=oper.getValue("CD_CARD1") %>">
 			    <table class="action_table">
					<tr><td <%=Bean.getDetailTableFirstColumnWidth() %>><%=Bean.webposXML.getfieldTransl("cheque_number_full", false) %></td><td><%=oper.getValue("NC_TERM") %></td></tr>
 					<tr><td colspan="2">Заглушка!!!</td></tr>
 					<tr><td colspan="2"><span id="error_title"><%=Bean.webposXML.getfieldTransl("operation_error", false) %></span></td></tr>
					<tr><td colspan="2"><span id="error_description"><%=resultMessage %></span></td></tr>
					<tr><td colspan="2">
	 					<%=Bean.getSubmitButtonAjax("/wmpupt/webpos/test/test_robokassa.jsp", "repeat", "updateForm", "div_main") %>
	 				</td></tr>

 				</table>
 			</form>
 			
 		<%}
		
	} else {
	    %> <%= Bean.getUnknownProcessText(process) %> <%
	}
} else {
    %> <%= Bean.getUnknownTypeText(type) %> <%
}

%>

</div>
</body>
</html>
