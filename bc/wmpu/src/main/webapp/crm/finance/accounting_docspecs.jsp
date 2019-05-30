<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%@page import="bc.objects.bcPostingObject"%>

<%@page import="bc.objects.bcPostingLineObject"%>
<%@page import="java.util.HashMap"%>

<%= Bean.getLogOutScript(request) %>
<html>
<head>
	<%= Bean.getMetaContent() %>
	<%= Bean.getBottomFrameCSS() %>
</Head>
<body topmargin="0">
<div id="div_tabsheet">

<%

HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String pageFormName = "FINANCE_ACCOUNTING_DOC";

Bean.setJspPageForTabName(pageFormName);

String tagLines = "_LINES";
String tagLineFind = "_LINE_FIND";
String tagDetail = "_DETAIL";
String tagDetailFind = "_DETAIL_FIND";
String tagExport = "_EXPORT";
String tagExportFind = "_EXPORT_FIND";

String type 		= Bean.getDecodeParam(parameters.get("type"));
String id_doc 		= Bean.getDecodeParam(parameters.get("id_doc"));
String id_posting 	= Bean.getDecodeParam(parameters.get("id_posting"));
String tab 			= Bean.getDecodeParam(parameters.get("tab"));
String id_report 	= Bean.getDecodeParam(parameters.get("id_report"));


if (tab==null || "".equalsIgnoreCase(tab)) {
	tab = Bean.tabsHmGetValue(pageFormName);
}

if (type==null || "".equalsIgnoreCase(type)) {
	id_posting = "";
	id_doc = "";
}
if (id_doc==null || "".equalsIgnoreCase(id_doc)) { 
	id_doc=""; 
}
if (id_posting==null || "".equalsIgnoreCase(id_posting)) { 
	id_posting=""; 
}
if (("doc".equalsIgnoreCase(type) && "".equalsIgnoreCase(id_doc)) ||
		("posting".equalsIgnoreCase(type) && "".equalsIgnoreCase(id_posting))) { 
%>
	<%=Bean.getIDNotFoundMessage() %>
<% 
} else {
	Bean.tabsHmSetValue(pageFormName, tab);
	bcPostingObject posting2 = null;
	
	bcPostingLineObject postingLine = null;
	if ("doc".equalsIgnoreCase(type)) {
		posting2 = new bcPostingObject(id_doc);
		posting2.getFeature();
	} else if ("posting".equalsIgnoreCase(type)) {
		postingLine = new bcPostingLineObject(id_posting);
		posting2 = new bcPostingObject(postingLine.getValue("ID_POSTING"));
		posting2.getFeature();
	}

	//Обрабатываем номера страниц
	String l_line_page = Bean.getDecodeParam(parameters.get("line_page"));
	Bean.pageCheck(pageFormName + tagLines, l_line_page);
	String l_line_page_beg = Bean.getFirstRowNumber(pageFormName + tagLines);
	String l_line_page_end = Bean.getLastRowNumber(pageFormName + tagLines);
	
	String line_find 	= Bean.getDecodeParam(parameters.get("line_find"));
	line_find 	= Bean.checkFindString(pageFormName + tagLineFind, line_find, l_line_page);
	
	String l_detail_page = Bean.getDecodeParam(parameters.get("detail_page"));
	Bean.pageCheck(pageFormName + tagDetail, l_detail_page);
	String l_detail_page_beg = Bean.getFirstRowNumber(pageFormName + tagDetail);
	String l_detail_page_end = Bean.getLastRowNumber(pageFormName + tagDetail);
	
	String detail_find 	= Bean.getDecodeParam(parameters.get("detail_find"));
	detail_find 	= Bean.checkFindString(pageFormName + tagDetailFind, detail_find, l_detail_page);
	
	String l_export_page = Bean.getDecodeParam(parameters.get("export_page"));
	Bean.pageCheck(pageFormName + tagExport, l_detail_page);
	String l_export_page_beg = Bean.getFirstRowNumber(pageFormName + tagExport);
	String l_export_page_end = Bean.getLastRowNumber(pageFormName + tagExport);
	
	String export_find 	= Bean.getDecodeParam(parameters.get("export_find"));
	export_find 	= Bean.checkFindString(pageFormName + tagExportFind, export_find, l_export_page);
	
	String pTitle = "";
	if ("doc".equalsIgnoreCase(type)) {
		pTitle = "Документ: " + posting2.getValue("NUMBER_POSTING") + " - " +posting2.getValue("DATE_POSTING_FRMT");
	} else {
		pTitle = "Проводка: " + postingLine.getValue("DEBET_CD_BK_ACCOUNT") + " - " +postingLine.getValue("CREDIT_CD_BK_ACCOUNT");
	}
	%>

	<table <%=Bean.getTableMenuParam() %>>
		<tr>
			<%= Bean.getPageHeader() %>

			<% if (Bean.currentMenu.isCurrentTabAndEditPermitted("FINANCE_ACCOUNTING_DOC_INFO")) { %>
				<% if (!("".equalsIgnoreCase(id_doc))) { %>
				<%= Bean.getMenuButton("DELETE", "../crm/finance/accounting_docupdate.jsp?type=header&id_doc=" + id_doc + "&action=remove&process=yes", Bean.buttonXML.getfieldTransl("delete", false), pTitle) %>
				<% } %>
			<% } %>

			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("FINANCE_ACCOUNTING_DOC_DETAIL")) { %>
			    <!-- Вывод страниц -->
				<% if ("doc".equalsIgnoreCase(type)) { %>
					<% if ((id_posting==null || "".equalsIgnoreCase(id_posting))) {%>
					<%= Bean.getPagesHTML(pageFormName + tagLines, "../crm/finance/accounting_docspecs.jsp?type=" + type + "&id_doc=" + id_doc + "&id_posting=" + id_posting + "&tab="+Bean.currentMenu.getTabID("FINANCE_ACCOUNTING_DOC_DETAIL")+"&", "line_page") %>
					<% } %>
				<% } else { %>
					<%= Bean.getPagesHTML(pageFormName + tagDetail, "../crm/finance/accounting_docspecs.jsp?type=" + type + "&id_doc=" + id_doc + "&id_posting=" + id_posting + "&tab="+Bean.currentMenu.getTabID("FINANCE_ACCOUNTING_DOC_DETAIL")+"&", "detail_page") %>
				<% } %>
			<% } %>

			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("FINANCE_ACCOUNTING_DOC_EXPORT")) { %>
				<% if (Bean.currentMenu.isCurrentTabAndEditPermitted("FINANCE_ACCOUNTING_DOC_EXPORT")) { %>
					<% if ("doc".equalsIgnoreCase(type)) { %>
						<%= Bean.getMenuButton("EXPORT", "../crm/finance/accounting_docupdate.jsp?type=header&id_doc=" + id_doc + "&action=export&process=no", Bean.buttonXML.getfieldTransl("delete", false), pTitle) %>
					<% } %>
				<% } %>
			    <!-- Вывод страниц -->
				<% if ("doc".equalsIgnoreCase(type)) { %>
					<%= Bean.getPagesHTML(pageFormName + tagExport, "../crm/finance/accounting_docspecs.jsp?type=" + type + "&id_doc=" + id_doc + "&id_posting=" + id_posting + "&tab="+Bean.currentMenu.getTabID("FINANCE_ACCOUNTING_DOC_EXPORT")+"&", "export_page") %>
				<% } else if ("posting".equalsIgnoreCase(type)) { %>
					<%= Bean.getPagesHTML(pageFormName + tagDetail, "../crm/finance/accounting_docspecs.jsp?type=" + type + "&id_doc=" + id_doc + "&id_posting=" + id_posting + "&tab="+Bean.currentMenu.getTabID("FINANCE_ACCOUNTING_DOC_DETAIL")+"&", "detail_page") %>
				<% } %>
			<% } %>
	
		</tr>
	</table>
	<table <%=Bean.getTableMenuParam() %>>
		<%= Bean.getDetailCaption(pTitle) %>
		<tr>
			<td>
				<!-- Выводим перечень закладок -->
				<%= Bean.currentMenu.getFormTabScheeds(tab, "../crm/finance/accounting_docspecs.jsp?type=" + type + "&id_doc=" + id_doc + "&id_posting=" + id_posting) %>
			</td>
		</tr>
	</table>
</div>
<div id="div_data">
<div id="div_data_detail">
<%
if (Bean.currentMenu.isCurrentTabAndAccessPermitted("FINANCE_ACCOUNTING_DOC_INFO")) { 
	boolean hasEditPermission = 	Bean.currentMenu.isCurrentTabAndEditPermitted("FINANCE_ACCOUNTING_DOC_INFO");
	
	if ("doc".equalsIgnoreCase(type)) { %>
		<script>
			var formData = new Array (
				new Array ('number_posting', 'varchar2', 1),
				new Array ('state_posting', 'varchar2', 1),
				new Array ('date_posting', 'varchar2', 1)
			);
		</script>
		<% if (hasEditPermission) { %>
			<form action="../crm/finance/accounting_docupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">
		    <input type="hidden" name="type" value="header">
		    <input type="hidden" name="action" value="edit">
		    <input type="hidden" name="process" value="yes">
			<input type="hidden" name="id_doc" value="<%= posting2.getValue("ID_POSTING") %>">
		<% } else { %>
			
		<% } %>
		<table <%=Bean.getTableDetailParam() %>>
			<tr>
				<td><%= Bean.postingXML.getfieldTransl("id_posting", false) %> </td><td><input type="text" name="id_posting" size="15" value="<%= posting2.getValue("ID_POSTING") %>" readonly="readonly" class="inputfield-ro"></td>
				<td><%= Bean.clubXML.getfieldTransl("club", false) %>
					<%= Bean.getGoToClubLink(posting2.getValue("ID_CLUB")) %>
				</td><td><input type="text" name="name_club" size="30" value="<%= Bean.getClubShortName(posting2.getValue("ID_CLUB")) %>" readonly="readonly" class="inputfield-ro"></td>
			</tr>
			<tr>
				<% if (hasEditPermission) { %>
				<td><%= Bean.postingXML.getfieldTransl("number_posting", true) %> </td><td><input type="text" name="number_posting" size="15" value="<%= posting2.getValue("NUMBER_POSTING") %>" class="inputfield"></td>
				<% } else { %>
				<td><%= Bean.postingXML.getfieldTransl("number_posting", false) %> </td><td><input type="text" name="number_posting" size="15" value="<%= posting2.getValue("NUMBER_POSTING") %>" readonly="readonly" class="inputfield-ro"></td>
				<% } %>
				<td><%= Bean.postingXML.getfieldTransl("posting_count", false) %></td> <td><input type="text" name="posting_count" size="15" value="<%= posting2.getValue("POSTING_COUNT") %>" readonly="readonly" class="inputfield-ro"> </td>
			</tr>
			<tr>
				<% if (hasEditPermission) { %>
				<td><%= Bean.postingXML.getfieldTransl("date_posting", true) %></td><td><%=Bean.getCalendarInputField("date_posting", posting2.getValue("DATE_POSTING_FRMT"), "10") %></td>
				<% } else { %>
				<td><%= Bean.postingXML.getfieldTransl("date_posting", false) %></td> <td><input type="text" name="date_posting" size="15" value="<%= posting2.getValue("DATE_POSTING_FRMT") %>" readonly="readonly" class="inputfield-ro"> </td>
				<% } %>
				<td><%= Bean.postingXML.getfieldTransl("line_count", false) %></td> <td><input type="text" name="line_count" size="15" value="<%= posting2.getValue("LINE_COUNT") %>" readonly="readonly" class="inputfield-ro"> </td>
			</tr>
			<tr>
				<% if (hasEditPermission) { %>
				<td><%= Bean.postingXML.getfieldTransl("state_posting", true) %></td> <td><select name="state_posting"  class="inputfield"><%= Bean.getMeaningFromLookupNameOptions("ACCOUNTS_POSTING_STATE", posting2.getValue("STATE_POSTING"), false) %></select></td>
				<% } else { %>
				<td><%= Bean.postingXML.getfieldTransl("state_posting", false) %></td> <td><input type="text" name="state_posting" size="25" value="<%= posting2.getValue("STATE_POSTING_TSL") %>" readonly="readonly" class="inputfield-ro"> </td>
				<% } %>
				<td colspan="2">&nbsp;</td>
			</tr>
			<%=	Bean.getCreationAndMoficationRecordFields(
					posting2.getValue(Bean.getCreationDateFieldName()),
					posting2.getValue("CREATED_BY"),
					posting2.getValue(Bean.getLastUpdateDateFieldName()),
					posting2.getValue("LAST_UPDATE_BY")
				) %>
			<% if (hasEditPermission) { %>
			<tr>
				<td colspan="8" align="center">
					<%=Bean.getSubmitButtonAjax("../crm/finance/accounting_docupdate.jsp") %>
					<%=Bean.getResetButton() %>
					<%=Bean.getGoBackButton("../crm/finance/accounting_doc.jsp") %>
				</td>
			</tr>
			<% } else {%>
			<tr>
				<td colspan="8" align="center">
					<%=Bean.getGoBackButton("../crm/finance/accounting_doc.jsp") %>
				</td>
			</tr>
			<% } %>
		</table>
		</form>
	
		<% if (hasEditPermission) { %>
			<%= Bean.getCalendarScript("date_posting", false) %>
	<% } 
	} else if ("posting".equalsIgnoreCase(type)) {
		if (hasEditPermission) { %>
		<form action="../crm/finance/accounting_docupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST">
	        <input type="hidden" name="type" value="line">
	        <input type="hidden" name="action" value="edit">
	        <input type="hidden" name="process" value="yes">
		<table <%=Bean.getTableDetailParam() %>>
			<tr>
				<td><%= Bean.postingXML.getfieldTransl("id_posting_line", false) %> </td><td><input type="text" name="id_posting_line" size="15" value="<%= postingLine.getValue("ID_POSTING_LINE") %>" readonly="readonly" class="inputfield-ro"></td>
				<td><%= Bean.clubXML.getfieldTransl("club", false) %>
					<%= Bean.getGoToClubLink(posting2.getValue("ID_CLUB")) %>
				</td><td><input type="text" name="name_club" size="30" value="<%= Bean.getClubShortName(posting2.getValue("ID_CLUB")) %>" readonly="readonly" class="inputfield-ro"></td>
			</tr>
			<tr>
				<td><%= Bean.postingXML.getfieldTransl("id_posting", false) %> 
					<%= Bean.getGoToFinanceAccountingDocLink(postingLine.getValue("ID_POSTING")) %>
				</td><td><input type="text" name="id_posting" size="15" value="<%= postingLine.getValue("NUMBER_POSTING") %>" readonly="readonly" class="inputfield-ro"> </td>
			  	<td><%= Bean.postingXML.getfieldTransl("operation_date", true) %></td><td><%=Bean.getCalendarInputField("operation_date", postingLine.getValue("OPERATION_DATE_FRMT"), "10") %></td>
			</tr>
			<tr>
				<td><%= Bean.postingXML.getfieldTransl("debet_id_bk_account", true) %>
					<%= Bean.getGoToFinanceBKAccountLink(postingLine.getValue("DEBET_ID_BK_ACCOUNT")) %>
				</td> 
				<td>
					<%=Bean.getWindowFindBKAccount("debet", postingLine.getValue("DEBET_ID_BK_ACCOUNT"), "70") %>
				</td>
				<td><%= Bean.postingXML.getfieldTransl("name_currency", true)%></td> <td><select name="name_currency" class="inputfield"><%=Bean.getCurrencyOptions(postingLine.getValue("CD_CURRENCY"), false)%></select> </td>
			</tr>
			<tr>
				<td><%= Bean.postingXML.getfieldTransl("credit_id_bk_account", true) %>
					<%= Bean.getGoToFinanceBKAccountLink(postingLine.getValue("CREDIT_ID_BK_ACCOUNT")) %>
				</td>
				<td>
					<%=Bean.getWindowFindBKAccount("credit", postingLine.getValue("CREDIT_ID_BK_ACCOUNT"), "70") %>
				</td>
				<td valign="top"><%= Bean.postingXML.getfieldTransl("state_posting_line", true)%></td> <td valign="top"><select name="state_posting_line" class="inputfield"><%=Bean.getMeaningFromLookupNameOptions("ACCOUNTS_POSTING_STATE", postingLine.getValue("STATE_POSTING_LINE"), false)%></select> </td>
			</tr>
			<tr>
				<td valign="top"><%= Bean.postingXML.getfieldTransl("assignment_posting", false) %></td><td valign="top"><textarea name="assignment_posting" cols="70" rows="4" class="inputfield"><%= postingLine.getValue("ASSIGNMENT_POSTING") %></textarea></td>
				<td><%= Bean.postingXML.getfieldTransl("entered_amount", true) %></td> <td><input type="text" name="entered_amount" size="16" value="<%= postingLine.getValue("ENTERED_AMOUNT_FRMT") %>" class="inputfield"> </td>
			</tr>
			<%=	Bean.getCreationAndMoficationRecordFields(
					posting2.getValue(Bean.getCreationDateFieldName()),
					posting2.getValue("CREATED_BY"),
					posting2.getValue(Bean.getLastUpdateDateFieldName()),
					posting2.getValue("LAST_UPDATE_BY")
				) %>
			<tr>
				<td colspan="6" align="center">
					<%=Bean.getSubmitButtonAjax("../crm/finance/accounting_docupdate.jsp") %>
					<%=Bean.getResetButton() %>
					<%=Bean.getGoBackButton("../crm/finance/accounting_doc.jsp") %>
				</td>
			</tr>
		</table>
		</form>	
		<%= Bean.getCalendarScript("operation_date", false) %>
	
	<% } else { %>
		<form>
		<table <%=Bean.getTableDetailParam() %>>
			<tr>
				<td><%= Bean.postingXML.getfieldTransl("id_posting_detail", false) %> </td><td><input type="text" name="id_posting_detail" size="15" value="<%= postingLine.getValue("ID_POSTING_DETAIL") %>" readonly="readonly" class="inputfield-ro"></td>
				<td><%= Bean.clubXML.getfieldTransl("club", false) %>
					<%= Bean.getGoToClubLink(posting2.getValue("ID_CLUB")) %>
				</td><td><input type="text" name="name_club" size="30" value="<%= Bean.getClubShortName(posting2.getValue("ID_CLUB")) %>" readonly="readonly" class="inputfield-ro"></td>
			</tr>
			<tr>
				<td><%= Bean.postingXML.getfieldTransl("id_posting", false) %> 
					<%= Bean.getGoToFinanceAccountingDocLink(postingLine.getValue("ID_POSTING")) %>
				</td><td><input type="text" name="id_posting" size="15" value="<%= postingLine.getValue("NUMBER_POSTING") %>" readonly="readonly" class="inputfield-ro"> </td>
			  	<td><%= Bean.postingXML.getfieldTransl("operation_date", false) %></td> <td><input type="text" name="operation_date" size="16" value="<%= postingLine.getValue("OPERATION_DATE_FRMT") %>" readonly="readonly" class="inputfield-ro"> </td>
			</tr>
			<tr>
				<td><%= Bean.postingXML.getfieldTransl("debet_id_bk_account", false) %>
					<%= Bean.getGoToFinanceBKAccountLink(postingLine.getValue("DEBET_ID_BK_ACCOUNT")) %>
				</td> <td><input type="text" name="debet_id_bk_account" size="70" value="<%= Bean.getBKAccountName(postingLine.getValue("DEBET_ID_BK_ACCOUNT")) %>" readonly="readonly" class="inputfield-ro"> </td>
				<td><%= Bean.postingXML.getfieldTransl("name_currency", false) %></td><td><input type="text" name="name_currency" size="16" value="<%= postingLine.getValue("NAME_CURRENCY") %>" readonly="readonly" class="inputfield-ro"></td>
			</tr>
			<tr>
				<td><%= Bean.postingXML.getfieldTransl("credit_id_bk_account", false) %>
					<%= Bean.getGoToFinanceBKAccountLink(postingLine.getValue("CREDIT_ID_BK_ACCOUNT")) %>
				</td><td><input type="text" name="credit_id_bk_account" size="70" value="<%= Bean.getBKAccountName(postingLine.getValue("CREDIT_ID_BK_ACCOUNT")) %>" readonly="readonly" class="inputfield-ro"></td>
				<td><%= Bean.postingXML.getfieldTransl("entered_amount", false) %></td> <td><input type="text" name="entered_amount" size="16" value="<%= postingLine.getValue("ENTERED_AMOUNT_FRMT") %>" readonly="readonly" class="inputfield-ro"> </td>
			</tr>
			<tr>
				<td valign="top"><%= Bean.postingXML.getfieldTransl("assignment_posting", false) %></td><td valign="top"><textarea name="assignment_posting" cols="66" rows="4" readonly="readonly" class="inputfield-ro"><%= postingLine.getValue("ASSIGNMENT_POSTING") %></textarea></td>
				<td valign="top"><%= Bean.postingXML.getfieldTransl("state_posting_line", false) %></td> <td valign="top"> <input type="text" name="state_posting_line" size="16" value="<%= postingLine.getValue("STATE_POSTING_LINE_TSL") %>" readonly="readonly" class="inputfield-ro"> </td>
			</tr>
			<%=	Bean.getCreationAndMoficationRecordFields(
					postingLine.getValue(Bean.getCreationDateFieldName()),
					postingLine.getValue("CREATED_BY"),
					postingLine.getValue(Bean.getLastUpdateDateFieldName()),
					postingLine.getValue("LAST_UPDATE_BY")
				) %>
			<tr>
				<td colspan="6" align="center">
					<%=Bean.getGoBackButton("../crm/finance/accounting_doc.jsp") %>
				</td>
			</tr>
		</table>
		</form>
	<%
	}
	}

	%>
	<br>
<% } %>

<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("FINANCE_ACCOUNTING_DOC_DETAIL")) {
	if ("doc".equalsIgnoreCase(type)) { %>
	<table <%= Bean.getTableBottomFilter() %>>
		<tr>
			<% if ((id_posting==null || "".equalsIgnoreCase(id_posting))) {%>
			<%= Bean.getFindHTML("line_find", line_find, "../crm/finance/accounting_docspecs.jsp?type=" + type + "&id_doc=" + id_doc + "&id_posting=" + id_posting + "&line_page=1") %>
			<% } else { %>
			<td>&nbsp;</td>
			<% } %>
	
			<td>&nbsp;</td>
		</tr>
	</table>
		<%=posting2.getPostingLinesHTML(id_posting, line_find, l_line_page_beg, l_line_page_end) %>
		<% if (!(id_posting==null || "".equalsIgnoreCase(id_posting))) { 
			bcPostingLineObject postingLine2 = new bcPostingLineObject(id_posting);
			%>
	<br>
	<br>
	<table <%= Bean.getTableBottomFilter() %>>
		<tr>
			<%= Bean.getFindHTML("detail_find", detail_find, "../crm/finance/accounting_docspecs.jsp?type=" + type + "&id_doc=" + id_doc + "&id_posting=" + id_posting + "&detail_page=1") %>
	
			<%= Bean.getPagesHTML(pageFormName + tagDetail, "../crm/finance/accounting_docspecs.jsp?type=" + type + "&id_doc=" + id_doc + "&id_posting=" + id_posting + "&tab="+Bean.currentMenu.getTabID("FINANCE_ACCOUNTING_DOC_DETAIL")+"&", "detail_page") %>
		</tr>
	</table>
			<%= postingLine2.getPostingTransHTML(detail_find, l_detail_page_beg, l_detail_page_end) %>		
		<% } %>
	<% } else if ("posting".equalsIgnoreCase(type)) { %>
	<table <%= Bean.getTableBottomFilter() %>>
		<tr>
			<%= Bean.getFindHTML("detail_find", detail_find, "../crm/finance/accounting_docspecs.jsp?type=" + type + "&id_doc=" + id_doc + "&id_posting=" + id_posting + "&detail_page=1") %>
	
			<td>&nbsp;</td>
		</tr>
	</table>
		<%=postingLine.getPostingTransHTML(detail_find, l_detail_page_beg, l_detail_page_end) %>

	<% } %>
<% } %>

<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("FINANCE_ACCOUNTING_DOC_EXPORT")) { %>
	<table <%= Bean.getTableBottomFilter() %>>
		<tr>
			<%= Bean.getFindHTML("export_find", export_find, "../crm/finance/accounting_docspecs.jsp?type=" + type + "&id_doc=" + id_doc + "&id_posting=" + id_posting + "&export_page=1") %>
	
			<td>&nbsp;</td>
		</tr>
	</table> 
   <%= posting2.getPostingFilesHTML("", export_find, l_export_page_beg, l_export_page_end) %> 
 <%} %>

<% } %>

</div></div>
</body>
</html>
