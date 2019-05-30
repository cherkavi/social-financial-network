<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%= Bean.getLogOutScriptShort(request) %>

<%@page import="java.util.HashMap"%><html>
<%
HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

request.setCharacterEncoding("UTF-8");
String pageFormName = "FIND_CARDS";

//Обрабатываем номера страниц
String l_page = Bean.getDecodeParam(parameters.get("page"));
Bean.pageCheck(pageFormName, l_page);
String l_curr_page = Bean.getCurrentPage(pageFormName);
String l_beg = Bean.getFirstRowNumber(pageFormName);
String l_end = Bean.getLastRowNumber(pageFormName);

String card_code = "";
String code_type = "CD_CARD";
String cd_card1 = Bean.getDecodeParamPrepare(parameters.get("cd_card1"));
if (Bean.isEmpty(cd_card1)) {
	cd_card1 = "";
} else {
	card_code = cd_card1;
	code_type = "CD_CARD";
}

String card_serial_number = Bean.getDecodeParamPrepare(parameters.get("card_serial_number"));
if (Bean.isEmpty(card_serial_number)) {
	card_serial_number = "";
} else {
	card_code = card_serial_number;
	code_type = "SERIAL_NUMBER";
}

String field = Bean.getDecodeParamPrepare(parameters.get("field"));

%>
<head>
	<%=Bean.getMetaContent() %>
	<script src="../../dwr/interface/responseUtility.js" language="javascript" type="text/javascript"></script>
	<script src="../../dwr/interface/ReporterUtility.js" language="javascript" type="text/javascript"></script>
	<script src="../../dwr/engine.js" language="javascript" type="text/javascript"></script>

	<link href="../../CSS/div.css" rel="stylesheet" type="text/css">
	<link href="../../CSS/tablesorter.css" rel="stylesheet" type="text/css">
	<script type="text/javascript" src="../../js/main/services.js"></script>
		
	<script>
		function changeOpener(cdCard, serialNumber, idIssuer, idPaymentSystem){
			window.opener.document.getElementById('<%=field%>_cd_card1').value = cdCard;
			window.opener.document.getElementById('<%=field%>_card_serial_number').value = serialNumber;
			window.opener.document.getElementById('<%=field%>_id_issuer').value = idIssuer;
			window.opener.document.getElementById('<%=field%>_id_payment_system').value = idPaymentSystem;
			window.opener.document.getElementById('<%=field%>_cd_card1').className = "inputfield_modified";
			window.opener.document.getElementById('<%=field%>_card_serial_number').className = "inputfield_modified";
			window.close();
		}
		function send_request() {
			send_request_to_server(
					'GET_CARDS', 
					'<%=code_type%>', 
					'');
		}
		setParam('<%=Bean.getLanguage()%>', 
				'<%= Bean.getSessionId()%>', 
				'<%= Bean.getRowsPerPage()%>');
		setTitle('<%= Bean.getPageTitle(Bean.commonXML.getfieldTransl("FIND_CARDS", false))%>');
		
	</script>

</head>
<body onload="focusInput(); checkExpr(); <% if (!"".equalsIgnoreCase(card_code)) {%>send_request(); return false;<%} %>">

	<%=Bean.getServiceHeaderTable(Bean.commonXML.getfieldTransl("FIND_CARDS", false)) %>
	
	<%=Bean.getServiceFindTable(card_code) %>

<table width="100%" class="tablesorter" id="id_table">
	<thead>
	<tr>
		<th><%= Bean.getClubCardXMLFieldTransl("CARD_SERIAL_NUMBER", false)%></th>
		<th><%= Bean.getClubCardXMLFieldTransl("CD_CARD1", false)%></th>
		<th><%= Bean.getClubCardXMLFieldTransl("NAME_NAT_PRS", false)%></th>
		<th><%= Bean.getClubCardXMLFieldTransl("NAME_CARD_TYPE", false)%></th>
		<th><%= Bean.getClubCardXMLFieldTransl("NAME_CARD_STATUS", false)%></th>
		<th><%= Bean.getClubCardXMLFieldTransl("NAME_CARD_STATE", false)%></th>
		<th><%= Bean.getClubCardXMLFieldTransl("DATE_OPEN_FRMT", false)%></th>
		<th><%= Bean.getClubCardXMLFieldTransl("EXPIRY_DATE_FRMT", false)%></th>
		<th><%= Bean.getClubCardXMLFieldTransl("NT_ICC", false)%></th>
	</tr>
	</thead>
	<tbody id="rows">
		
	</tbody>
</table>
</body>
</html>