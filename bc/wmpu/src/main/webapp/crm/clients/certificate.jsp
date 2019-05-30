<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%= Bean.getLogOutScript(request) %>

<%@page import="java.util.HashMap"%><html>
<%
request.setCharacterEncoding("UTF-8");

String pageFormName = "CLIENTS_CERTIFICATE";
String tagProfile = "_PROFILE";
String tagFind = "_FIND";
String tagCertType = "_CERT_TYPE";

Bean.setJspPageForTabName(pageFormName);

HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String specId 			= Bean.getDecodeParamPrepare(parameters.get("specId"));
String l_page 			= Bean.getDecodeParam(parameters.get("page"));
String find_string 		= Bean.getDecodeParam(parameters.get("find_string"));
String id_profile 		= Bean.getDecodeParam(parameters.get("id_profile"));
String cert_type 		= Bean.getDecodeParam(parameters.get("cert_type"));

id_profile 	= Bean.checkFindString(pageFormName + tagProfile, id_profile, l_page);
find_string = Bean.checkFindString(pageFormName + tagFind, find_string, l_page);
cert_type 	= Bean.checkFindString(pageFormName + tagCertType, cert_type, l_page);
if (cert_type==null || "".equalsIgnoreCase(cert_type)) {
	cert_type = "T";
}

//Обрабатываем номера страниц
Bean.pageCheck(pageFormName, l_page);
String l_beg = Bean.getFirstRowNumber(pageFormName);
String l_end = Bean.getLastRowNumber(pageFormName);

String print        = Bean.checkPrint(Bean.getDecodeParam(parameters.get("print")));

%>
<head>
	<%=Bean.getJSPHeadHTML(print) %>
</head>

<body>
<div id="div_tabsheet">
<% if ("N".equalsIgnoreCase(print)) { %>
	<table <%= Bean.getTableMenuParam() %>>
		<tr>
			<%= Bean.getPageHeader() %>

			<% if (Bean.hasEditMenuPermission(pageFormName) && "I".equalsIgnoreCase(cert_type)) { %>
			    <%= Bean.getMenuButton("LOAD_FROM_FILE", "../crm/clients/certificateimport.jsp?type=import&process=no&action=load_from_file", "", "", Bean.terminalXML.getfieldTransl("h_load_certificates", false)) %>
			    <%= Bean.getMenuButton("IMPORT_ALL", "../crm/clients/certificateupdate.jsp?type=applyall&process=yes", Bean.terminalXML.getfieldTransl("h_import_all_certificates", false), "") %>
			<% } %>
		    <%= Bean.getMenuButton("PRINT", "../crm/clients/certificate.jsp?print=Y", "", "") %>
	
		    <!-- Вывод страниц -->
			<%= Bean.getPagesHTML(pageFormName, "../crm/clients/certificate.jsp?", "page") %>
		</tr>
	</table>
	<table <%= Bean.getTableMenuFilter() %>>
		<tr>
			<%= Bean.getFindHTML("find_string", find_string, "../crm/clients/certificate.jsp?page=1&") %>
		
			<% if ("T".equalsIgnoreCase(cert_type)) { %>
			
			<%=Bean.getSelectOnChangeBeginHTML("id_profile", "../crm/clients/certificate.jsp?page=1&cert_type="+cert_type, Bean.transactionXML.getfieldTransl("id_profile", false)) %>
				<%=Bean.getSelectOptionHTML(id_profile, "", "") %>
				<%= Bean.getMenuProfileList("245", id_profile) %>
			<%=Bean.getSelectOnChangeEndHTML() %>
			<% } %>

			<%=Bean.getSelectOnChangeBeginHTML("cert_type", "../crm/clients/certificate.jsp?page=1", Bean.terminalCertificateXML.getfieldTransl("h_status", false)) %>
				<%=Bean.getSelectOptionHTML(cert_type, "T", Bean.terminalCertificateXML.getfieldTransl("filtr_term", false)) %>
				<%=Bean.getSelectOptionHTML(cert_type, "C", Bean.terminalCertificateXML.getfieldTransl("filtr_loaded", false)) %>
				<%=Bean.getSelectOptionHTML(cert_type, "I", Bean.terminalCertificateXML.getfieldTransl("filtr_interface", false)) %>
			<%=Bean.getSelectOnChangeEndHTML() %>
		</tr>
	</table>
<% } else { %>
	<table <%= Bean.getTableMenuParam() %>>
		<tr>
			<%= Bean.getPageHeaderPrint() %>
		</tr>
	</table>
<% } %>
</div>
<div id="div_data">
<div id="div_data_detail">
<% if (Bean.hasAccessMenuPermission(pageFormName)) { %>
	<%= Bean.header.getTerminalCertificateHeadHTML(cert_type, id_profile, find_string, l_beg, l_end, print) %>
<%} %>
</div></div>
</body>
</html>