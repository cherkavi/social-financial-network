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
String code_type = Bean.getDecodeParamPrepare(parameters.get("code_type"));
String cd_card1 = Bean.getDecodeParamPrepare(parameters.get("cd_card1"));
if (Bean.isEmpty(cd_card1)) {
	cd_card1 = "";
} else {
	card_code = cd_card1;
	code_type = "CARD";
}

String id_nat_prs = Bean.getDecodeParam(parameters.get("id_nat_prs"));
if (Bean.isEmpty(id_nat_prs)) {
	id_nat_prs = "";
} else {
	card_code = Bean.getNatPrsName(id_nat_prs);
	code_type = "NAT_PRS";
}
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
	var page = 1;
	var expr = "";
		function changeOpener(cd_card1, csn, ii, ips, inp, nnp){
			window.opener.document.getElementById('id_nat_prs').value = inp;
			window.opener.document.getElementById('name_nat_prs').value = nnp;
			window.opener.document.getElementById('name_nat_prs').className = "inputfield_modified";
			
			window.opener.document.getElementById('cd_card1').value = cd_card1;
			window.opener.document.getElementById('card_serial_number').value = csn;
			window.opener.document.getElementById('id_issuer').value = ii;
			window.opener.document.getElementById('id_payment_system').value = ips;
			window.opener.document.getElementById('cd_card1').className = "inputfield_modified";
			window.opener.document.getElementById('card_serial_number').className = "inputfield_modified";
			window.close();
		}
		function send_request() {
			send_request_to_server(
					'GET_CARD_ADN_NAT_PRS', 
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