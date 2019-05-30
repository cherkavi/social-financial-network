<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%= Bean.getLogOutScriptShort(request) %>

<%@page import="java.util.HashMap"%><html>

<%
HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

request.setCharacterEncoding("UTF-8");
String pageFormName = "FIND_CARD_AND_NAT_PRS";

//Обрабатываем номера страниц
String l_page = Bean.getDecodeParam(parameters.get("page"));
Bean.pageCheck(pageFormName, l_page);
String l_curr_page = Bean.getCurrentPage(pageFormName);
String l_beg = Bean.getFirstRowNumber(pageFormName);
String l_end = Bean.getLastRowNumber(pageFormName);

String find_type = Bean.getDecodeParamPrepare(parameters.get("find_type"));
find_type = Bean.isEmpty(find_type)?"NAT_PRS":find_type;

String id_nat_prs = Bean.getDecodeParamPrepare(parameters.get("id_nat_prs"));
String full_name = "";
String cd_card1 = Bean.getDecodeParamPrepare(parameters.get("cd_card1"));

if ("NAT_PRS".equalsIgnoreCase(find_type)) {
  	full_name = Bean.isEmpty(id_nat_prs)?"":Bean.getNatPrsName(id_nat_prs);
} else if ("CARD".equalsIgnoreCase(find_type)) {
	full_name = Bean.isEmpty(cd_card1)?"":cd_card1;
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
		setParam('<%=Bean.getLanguage()%>', 
				'<%= Bean.getSessionId()%>', 
				'<%= Bean.getRowsPerPage()%>');
		function send_request() {
			send_request_to_server(
					'GET_CARD_ADN_NAT_PRS', 
					'<%=find_type%>', 
					'');
		}
		setTitle('<%= Bean.getPageTitle(Bean.commonXML.getfieldTransl("FIND_NAT_PRS", false))%>');
		
	</script>

</head>
<body onload="focusInput(); checkExpr(); <% if (!"".equalsIgnoreCase(full_name)) {%>send_request(); return false;<%} %>">

	<%=Bean.getServiceHeaderTable(Bean.commonXML.getfieldTransl("FIND_NAT_PRS", false)) %>
	
	<%=Bean.getServiceFindTable(full_name) %>

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