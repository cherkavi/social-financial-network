<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%= Bean.getLogOutScript(request) %>

<%@page import="java.util.HashMap"%><html>
<%

String pageFormName = "CLUB_RELATIONSHIP";
String tagFindCreated = "_FIND_CREATED";
String tagFindNeeded = "_FIND_NEEDED";
String tagRelTypeCreated = "_REL_TYPE_CREATED";
String tagRelTypeNeeded = "_REL_TYPE_NEEDED";
String tagRelKind = "_REL_KIND";
String tagPageCreated = "_PAGE_CREATED";
String tagPageNeeded = "_PAGE_NEEDED";

Bean.setJspPageForTabName(pageFormName);

request.setCharacterEncoding("UTF-8");

HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String specId 			= Bean.getDecodeParamPrepare(parameters.get("specId"));
String find_created 	= Bean.getDecodeParam(parameters.get("find_created"));
String find_needed 		= Bean.getDecodeParam(parameters.get("find_needed"));
String l_page_created	= Bean.getDecodeParam(parameters.get("page_created"));
String l_page_needed	= Bean.getDecodeParam(parameters.get("page_needed"));
String rel_type_created	= Bean.getDecodeParam(parameters.get("rel_type_created"));
String rel_type_needed	= Bean.getDecodeParam(parameters.get("rel_type_needed"));
String rel_kind 	= Bean.getDecodeParam(parameters.get("rel_kind"));

find_created 		= Bean.checkFindString(pageFormName + tagFindCreated, find_created, l_page_created);
rel_type_created 	= Bean.checkFindString(pageFormName + tagRelTypeCreated, rel_type_created, l_page_created);

find_needed	 		= Bean.checkFindString(pageFormName + tagFindNeeded, find_needed, l_page_needed);
rel_type_needed 	= Bean.checkFindString(pageFormName + tagRelTypeNeeded, rel_type_needed, l_page_needed);

rel_kind 			= Bean.checkFindString(pageFormName + tagRelKind, rel_kind, "");
if (rel_kind==null || "".equalsIgnoreCase(rel_kind)) {
	rel_kind = "CREATED";
}

Bean.pageCheck(pageFormName + tagPageCreated, l_page_created);
String l_page_created_beg = Bean.getFirstRowNumber(pageFormName + tagPageCreated);
String l_page_created_end = Bean.getLastRowNumber(pageFormName + tagPageCreated);

Bean.pageCheck(pageFormName + tagPageNeeded, l_page_needed);
String l_page_needed_beg = Bean.getFirstRowNumber(pageFormName + tagPageNeeded);
String l_page_needed_end = Bean.getLastRowNumber(pageFormName + tagPageNeeded);

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
		    <%= Bean.getMenuButton("ADD", "../crm/club/relationshipupdate.jsp?type=general&action=add&process=no", "", "") %>
			<% } %>
		    <%= Bean.getMenuButton("PRINT", "../crm/club/relationship.jsp?print=Y&rel_kind=" + rel_kind, "", "") %>
		
			<% if ("CREATED".equalsIgnoreCase(rel_kind)) { %>
			    <!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagPageCreated, "../crm/club/relationship.jsp?", "page_created") %>
			<% } else { %>
				<!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagPageNeeded, "../crm/club/relationship.jsp?", "page_needed") %>
			<% } %>
		</tr>
	</table>
	<table <%= Bean.getTableMenuFilter() %>>
		<tr>
			<% if ("CREATED".equalsIgnoreCase(rel_kind)) { %>
				<%= Bean.getFindHTML("find_created", find_created, "../crm/club/relationship.jsp?page_created=1&") %>
			<% } else { %>
				<%= Bean.getFindHTML("find_needed", find_needed, "../crm/club/relationship.jsp?page_needed=1&") %>
			<% } %>
	
			<td align="right" width="20">
			  	<select onchange="ajaxpage('../crm/club/relationship.jsp?page_created=1&page_needed=1&rel_kind='+this.value, 'div_main')" name="rel_kind" id="rel_kind" class="inputfield">
					<option value="CREATED" <% if ("CREATED".equalsIgnoreCase(rel_kind)) { %>SELECTED<% } %>><%= Bean.relationshipXML.getfieldTransl("h_created", false) %></option>
					<option value="NEEDED" <% if ("NEEDED".equalsIgnoreCase(rel_kind)) { %>SELECTED<% } %>><%= Bean.relationshipXML.getfieldTransl("h_needed", false) %></option>
				</select>
			</td>
	
			<% if ("CREATED".equalsIgnoreCase(rel_kind)) { %>
				<%=Bean.getSelectOnChangeBeginHTML("rel_type_created", "../crm/club/relationship.jsp?page=1", Bean.relationshipXML.getfieldTransl("name_club_rel_type", false)) %>
					<%= Bean.getClubRelTypeOptions(rel_type_created, true) %>
				<%=Bean.getSelectOnChangeEndHTML() %>
			<% } else { %>
				<%=Bean.getSelectOnChangeBeginHTML("rel_type_needed", "../crm/club/relationship.jsp?page=1", Bean.relationshipXML.getfieldTransl("name_club_rel_type", false)) %>
					<%= Bean.getClubRelTypeOptions(rel_type_needed, true) %>
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
<% Bean.header.setDeleteHyperLink("","",""); %>

<% if (Bean.hasAccessMenuPermission(pageFormName)) { %>
 	<% if ("CREATED".equalsIgnoreCase(rel_kind)) { %>
		<% if (Bean.hasEditMenuPermission(pageFormName)) { %>
			<% Bean.header.setDeleteHyperLink("../crm/club/relationshipupdate.jsp?type=general&action=remove&process=yes&id=",Bean.relationshipXML.getfieldTransl("h_delete_relationship", false),"ID_CLUB_REL"); %>
		<%} %>

		<%= Bean.header.getClubRelationshipsHeadHTML(rel_type_created, find_created, l_page_created_beg, l_page_created_end, print) %>
	<% } else { %>
		<%= Bean.header.getClubRelationshipsNeededHeadHTML(rel_type_needed, find_needed, l_page_needed_beg, l_page_needed_end, print) %>
	<% } %>
<%} %>
</div></div>
</body>
</html>