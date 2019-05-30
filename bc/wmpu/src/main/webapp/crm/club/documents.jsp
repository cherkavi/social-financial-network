<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%= Bean.getLogOutScript(request) %>

<%@page import="java.util.HashMap"%><html>
<%

String pageFormName = "CLUB_DOCUMENTS";
String tagFind = "_FIND";
String tagDocType = "_DOC_TYPE";
String tagDocState = "_DOC_STATE";
String tagRelType = "_DOC_REL_TYPE";
String tagRelKind = "_REL_KIND";

Bean.setJspPageForTabName(pageFormName);

request.setCharacterEncoding("UTF-8");

HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String specId 		= Bean.getDecodeParamPrepare(parameters.get("specId"));
String find_string 	= Bean.getDecodeParam(parameters.get("find_string"));
String l_page 		= Bean.getDecodeParam(parameters.get("page"));
String doc_type 	= Bean.getDecodeParam(parameters.get("doc_type"));
String doc_state 	= Bean.getDecodeParam(parameters.get("doc_state"));
String rel_type 	= Bean.getDecodeParam(parameters.get("rel_type"));

String rel_kind = Bean.getDecodeParam(parameters.get("rel_kind"));

find_string = Bean.checkFindString(pageFormName + tagFind, find_string, l_page);
doc_type = Bean.checkFindString(pageFormName + tagDocType, doc_type, l_page);
doc_state = Bean.checkFindString(pageFormName + tagDocState, doc_state, l_page);
rel_type = Bean.checkFindString(pageFormName + tagRelType, rel_type, l_page);
rel_kind = Bean.checkFindString(pageFormName + tagRelKind, rel_kind, l_page);

if (rel_kind==null || "".equalsIgnoreCase(rel_kind)) {
	rel_kind = "CREATED";
}

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

			<% if (Bean.hasEditMenuPermission(pageFormName)) { %>
		    <%= Bean.getMenuButton("ADD", "../crm/club/documentupdate.jsp?type=general&action=add&process=no", "", "") %>
			<% } %> 
		    <%= Bean.getMenuButton("PRINT", "../crm/club/documents.jsp?print=Y", "", "") %>

		    <!-- Вывод страниц -->
			<%= Bean.getPagesHTML(pageFormName, "../crm/club/documents.jsp?", "page") %>
		</tr>
	</table>
	<table <%= Bean.getTableMenuFilter() %>>
		<tr>
			<%= Bean.getFindHTML("find_string", find_string, "../crm/club/documents.jsp?page=1&") %>
			<!-- <td align="right" width="20">
			  	<select onchange="ajaxpage('../crm/club/documents.jsp?page=1&rel_kind='+this.value, 'div_main')" name="rel_kind" id="rel_kind" class="inputfield">
					<option value="CREATED" <% if ("CREATED".equalsIgnoreCase(rel_kind)) { %>SELECTED<% } %>><%= Bean.relationshipXML.getfieldTransl("h_created", false) %></option>
					<option value="NEEDED" <% if ("NEEDED".equalsIgnoreCase(rel_kind)) { %>SELECTED<% } %>><%= Bean.relationshipXML.getfieldTransl("h_needed", false) %></option>
				</select>
			</td> -->
			<%=Bean.getSelectOnChangeBeginHTML("doc_type", "../crm/club/documents.jsp?page=1", Bean.documentXML.getfieldTransl("id_doc_type", false)) %>
				<%= Bean.getDocTypeOptions(doc_type, true) %>
			<%=Bean.getSelectOnChangeEndHTML() %>
			<% if ("CREATED".equalsIgnoreCase(rel_kind)) { %>
			<%=Bean.getSelectOnChangeBeginHTML("rel_type", "../crm/club/documents.jsp?page=1", Bean.documentXML.getfieldTransl("club_rel_type", false)) %>
				<%= Bean.getClubRelTypeOptions(rel_type, true) %>
			<%=Bean.getSelectOnChangeEndHTML() %>

			<%=Bean.getSelectOnChangeBeginHTML("doc_state", "../crm/club/documents.jsp?page=1", Bean.documentXML.getfieldTransl("cd_doc_state", false)) %>
				<%= Bean.getDocStateOptions(doc_state, true) %>
			<%=Bean.getSelectOnChangeEndHTML() %>
			<% } %>
	
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
<% if (Bean.hasEditMenuPermission(pageFormName)) { %>
	<% if ("CREATED".equalsIgnoreCase(rel_kind)) { %>
		<% Bean.header.setDeleteHyperLink("../crm/club/documentupdate.jsp?type=general&action=remove&process=yes&id=",Bean.documentXML.getfieldTransl("l_remove_doc", false),"ID_DOC", "FULL_DOC"); %>
	<% } else { %>
		<% Bean.header.setDeleteHyperLink("","",""); %>
	<% } %>
<%} else { %>
	<% Bean.header.setDeleteHyperLink("","",""); %>
<%} %>

<% if (Bean.hasAccessMenuPermission(pageFormName)) { %>
	<% if ("CREATED".equalsIgnoreCase(rel_kind)) { %>
		<%= Bean.header.getDocumentsHeadHTML(doc_type, doc_state, rel_type, find_string, l_beg, l_end, print) %>
	<% } else { %>
		<%= Bean.header.getDocumentsNeedHeadHTML(doc_type, find_string, l_beg, l_end, print) %>
	<% } %>
<%} %>
</div></div>
</body>
</html>