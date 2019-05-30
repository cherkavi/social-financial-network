<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%= Bean.getLogOutScriptShort(request) %>

<%@page import="java.util.HashMap"%><html>
<%
HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

request.setCharacterEncoding("UTF-8");
String pageFormName = "FIND_CARD_PACK";

//Обрабатываем номера страниц
String l_page = Bean.getDecodeParam(parameters.get("page"));
Bean.pageCheck(pageFormName, l_page);
String l_curr_page = Bean.getCurrentPage(pageFormName);
String l_beg = Bean.getFirstRowNumber(pageFormName);
String l_end = Bean.getLastRowNumber(pageFormName);

String id_card_pack = Bean.getDecodeParamPrepare(parameters.get("id_card_pack"));
String name_card_pack = Bean.isEmpty(id_card_pack)?"":Bean.getClubJurPrsCardPackName(id_card_pack);

String field = Bean.getDecodeParamPrepare(parameters.get("field"));

String id_jur_prs = Bean.getDecodeParamPrepare(parameters.get("id_jur_prs"));
String name_jur_prs = Bean.isEmpty(id_jur_prs)?"":Bean.getJurPersonShortName(id_jur_prs);

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
		function changeOpener(id, Name){
			window.opener.document.getElementById('id_<%=field%>').value = id;
			window.opener.document.getElementById('name_<%=field%>').value = Name;
			window.opener.document.getElementById('name_<%=field%>').className = "inputfield_modified";
			window.close();
		}
		function send_request() {
			send_request_to_server(
					'GET_CARD_PACK', 
					'<%=id_jur_prs%>', 
					'');
		}
		setParam('<%=Bean.getLanguage()%>', 
				'<%= Bean.getSessionId()%>', 
				'<%= Bean.getRowsPerPage()%>');
		setTitle('<%= Bean.getPageTitle(Bean.commonXML.getfieldTransl("FIND_CARD_PACK", false))%><%=!Bean.isEmpty(name_jur_prs)?" ("+name_jur_prs+")":""%>');
		
	</script>

</head>
<body onload="focusInput(); checkExpr(); <% if (!"".equalsIgnoreCase(name_card_pack)) {%>send_request(); return false;<%} %>">

	<%=Bean.getServiceHeaderTable(Bean.commonXML.getfieldTransl("FIND_CARD_PACK", false) + (!Bean.isEmpty(name_jur_prs)?" ("+name_jur_prs+")":"")) %>
	
	<%=Bean.getServiceFindTable(name_card_pack) %>

<table width="100%" class="tablesorter" id="id_table">
	<thead>
	<tr>
		<th><%= Bean.getClubCardXMLFieldTransl("SNAME_PARTNER", false)%></th>
		<th><%= Bean.getClubCardXMLFieldTransl("CARD_STATUS", false)%></th>
		<th><%= Bean.getClubCardXMLFieldTransl("NAME_JUR_PRS_CARD_PACK", false)%></th>
		<th><%= Bean.getClubCardXMLFieldTransl("TOTAL_AMOUNT_CARD_PACK_FRMT", false)%></th>
	</tr>
	</thead>
	<tbody id="rows">
		
	</tbody>
</table>
</body>
</html>