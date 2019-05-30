<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%@page import="bc.objects.bcPostingObject"%>
<%@page import="java.util.HashMap"%>

<%= Bean.getLogOutScript(request) %>
<%

request.setCharacterEncoding("UTF-8");

String pageFormName = "FINANCE_ACCOUNTING_DOC";
String tagType = "_TYPE";
String tagFindDoc = "_FIND_DOC";
String tagFindLine = "_FIND_Line";
String tagDocPage = "_DOC_PAGE";
String tagLinesPage = "_LINES_PAGE";

Bean.setJspPageForTabName(pageFormName);

HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String specId 		= Bean.getDecodeParamPrepare(parameters.get("specId"));
String find_doc 	= Bean.getDecodeParam(parameters.get("find_doc"));
String find_line 	= Bean.getDecodeParam(parameters.get("find_line"));
String l_doc_page 	= Bean.getDecodeParam(parameters.get("doc_page"));
String l_line_page 	= Bean.getDecodeParam(parameters.get("line_page"));
String id_type 		= Bean.getDecodeParam(parameters.get("id_type"));

find_doc 		= Bean.checkFindString(pageFormName + tagFindDoc, find_doc, l_doc_page);
find_line 		= Bean.checkFindString(pageFormName + tagFindLine, find_line, l_line_page);

id_type 		= Bean.checkFindString(pageFormName + tagType, id_type, "");
if (id_type==null || "".equalsIgnoreCase(id_type)) {
	id_type = "DOCUMENTS";
}

String id_doc 		= Bean.getDecodeParam(parameters.get("id_doc"));
String number_posting = "";
if (id_doc==null || "".equalsIgnoreCase(id_doc)) {
    specId = specId + "&id_doc=";
    number_posting = "";
} else {
	if ("null".equalsIgnoreCase(id_doc)) {
		id_doc = "";
		number_posting = "";
	} else {
		bcPostingObject pos = new bcPostingObject(id_doc);
		pos.getFeature();
		number_posting = pos.getValue("NUMBER_POSTING");
	}
	specId = specId + "&id_doc="+id_doc ;
}
String tabHeader = "";
if ("DOCUMENTS".equalsIgnoreCase(id_type)) {
	specId = specId + "&type=doc";
	tabHeader = "tab_header_documents";
} else if ("POSTINGS".equalsIgnoreCase(id_type)) {
	specId = specId + "&type=posting";
	tabHeader = "tab_header_postings";
}

//Обрабатываем номера страниц
Bean.pageCheck(pageFormName + tagDocPage, l_doc_page);
String l_doc_page_beg = Bean.getFirstRowNumber(pageFormName + tagDocPage);
String l_doc_page_end = Bean.getLastRowNumber(pageFormName + tagDocPage);

Bean.pageCheck(pageFormName + tagLinesPage, l_line_page);
String l_line_page_beg = Bean.getFirstRowNumber(pageFormName + tagLinesPage);
String l_line_page_end = Bean.getLastRowNumber(pageFormName + tagLinesPage);

String print        = Bean.checkPrint(Bean.getDecodeParam(parameters.get("print")));

%>
<html>
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
		    <%= Bean.getMenuButton("RUN", "../crm/finance/accounting_docupdate.jsp?type=header&action=create&process=yes", Bean.postingXML.getfieldTransl("LAB_RUN_DOC", false), "") %>
			<% } %>
		    <%= Bean.getMenuButton("PRINT", "../crm/finance/accounting_doc.jsp?print=Y", "", "") %>

		    <!-- Вывод страниц -->
			<% if ("DOCUMENTS".equalsIgnoreCase(id_type)) { %>
				<%= Bean.getPagesHTML(pageFormName + tagDocPage, "../crm/finance/accounting_doc.jsp?", "doc_page") %>
			<% } else { %>
				<%= Bean.getPagesHTML(pageFormName + tagLinesPage, "../crm/finance/accounting_doc.jsp?", "line_page") %>
			<% } %>
		</tr>
	</table>
	<table <%= Bean.getTableMenuFilter() %>>
		<tr>
			<% if ("DOCUMENTS".equalsIgnoreCase(id_type)) { %>
				<%= Bean.getFindHTML("find_doc", find_doc, "../crm/finance/accounting_doc.jsp?doc_page=1&id_doc=" + id_doc + "&") %>
			<% } else { %>
				<%= Bean.getFindHTML("find_line", find_line, "../crm/finance/accounting_doc.jsp?line_page=1&id_doc=" + id_doc + "&") %>
			<% } %>
			<% String lTitle = ""; 
				if (!(number_posting==null || "".equalsIgnoreCase(number_posting))) { 
					lTitle = Bean.postingXML.getfieldTransl("h_doc_caption", false) + " " + number_posting;
			 }
			%>
			<% if (!(id_doc==null || "".equalsIgnoreCase(id_doc))) { %>
				<td align="center"><b><%=lTitle %></b>
				</td>
			<% } %>
		
			<%=Bean.getSelectOnChangeBeginHTML("id_type", "../crm/finance/accounting_doc.jsp?page=1", Bean.postingXML.getfieldTransl("h_documents_entries", false)) %>
				<%=Bean.getSelectOptionHTML(id_type, "DOCUMENTS", Bean.postingXML.getfieldTransl("tab_header_documents", false)) %>
				<%=Bean.getSelectOptionHTML(id_type, "POSTINGS", Bean.postingXML.getfieldTransl("tab_header_postings", false)) %>
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

<% Bean.header.setDeleteHyperLink("","",""); %>

<% if (Bean.hasAccessMenuPermission(pageFormName)) { %>
	<% if ("DOCUMENTS".equalsIgnoreCase(id_type)) { %>
		<%= Bean.header.getAccountingDocHeadHTML(find_doc, l_doc_page_beg, l_doc_page_end, print) %>
	<% } else { %>
		<%= Bean.header.getAccountingDocPostingHeadHTML(id_doc, find_line, l_line_page_beg, l_line_page_end, print) %>
	<% } %>
<%  
} %>
</div></div>
</body>
</html>