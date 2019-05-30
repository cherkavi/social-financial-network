<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%@page import="bc.objects.bcBankStatementHeaderObject"%>
<%@page import="java.util.HashMap"%><html>

<%= Bean.getLogOutScript(request) %>

<head>
	<%= Bean.getMetaContent() %>
	<%= Bean.getBottomFrameCSS() %>

</head>

<% 

HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String pageFormName = "FINANCE_KVITOVKA";

Bean.setJspPageForTabName(pageFormName);

String tagLines = "_LINES";

//Обрабатываем номера страниц
String l_lines_page = Bean.getDecodeParam(parameters.get("lines_page"));
Bean.pageCheck(pageFormName + tagLines, l_lines_page);
String l_lines_page_beg = Bean.getFirstRowNumber(pageFormName + tagLines);
String l_lines_page_end = Bean.getLastRowNumber(pageFormName + tagLines);

String id = Bean.getDecodeParam(parameters.get("id"));
String line = Bean.getDecodeParam(parameters.get("line"));
String date = Bean.getDecodeParam(parameters.get("date"));
String number = Bean.getDecodeParam(parameters.get("number"));
if (!((number==null)||"".equalsIgnoreCase(number))) {
  number = new String(number.getBytes("ISO-8859-1"), "UTF-8");
}


String clearing_amount = Bean.getDecodeParam(parameters.get("clearing_amount"));

String id_bank_account_client = Bean.getDecodeParam(parameters.get("id_bank_account_client"));
if (!((id_bank_account_client==null)||"".equalsIgnoreCase(id_bank_account_client))) {
	id_bank_account_client = new String(id_bank_account_client.getBytes("ISO-8859-1"), "UTF-8");
}
String id_bank_account_correspondent = Bean.getDecodeParam(parameters.get("id_bank_account_correspondent"));
if (!((id_bank_account_correspondent==null)||"".equalsIgnoreCase(id_bank_account_correspondent))) {
	id_bank_account_correspondent = new String(id_bank_account_correspondent.getBytes("ISO-8859-1"), "UTF-8");
}
String entered_amount = Bean.getDecodeParam(parameters.get("entered_amount"));
String	tab = Bean.getDecodeParam(parameters.get("tab"));
if (tab==null || "".equalsIgnoreCase(tab)) { tab = Bean.tabsHmGetValue(pageFormName); }

if (id==null || "".equalsIgnoreCase(id)) {
	id = "";
}
{
	Bean.tabsHmSetValue(pageFormName, tab);
%>

<body topmargin="0">
<div id="div_tabsheet">

<table   <%=Bean.getTableMenuParam() %>>
	<tr>
		<td><i> &nbsp;</i><br>
			<% String myHyperLink = "";
			if ("".equalsIgnoreCase(id)) { 
            	Bean.currentMenu.setExistFlag("FINANCE_KVITOVKA_EXEC", false);
            	myHyperLink = "../crm/finance/kvitovkaspecs.jsp?id=";
            } else {
            	Bean.currentMenu.setExistFlag("FINANCE_KVITOVKA_EXEC", true);
            	myHyperLink = "../crm/finance/kvitovkaspecs.jsp?id=" + id + "&date=" + date + "&number=" + number + "&entered_amount=" + entered_amount + "&clearing_amount=" + clearing_amount + "&id_bank_account_client=" + id_bank_account_client + "&id_bank_account_correspondent=" + id_bank_account_correspondent;
            } %>

			<!-- Выводим перечень закладок -->
			<%= Bean.currentMenu.getFormTabScheeds(tab, myHyperLink) %>
 		</td>


	</tr>
	</table>
</div>
<div id="div_data">
<div id="div_data_detail">
	
	<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("FINANCE_KVITOVKA_EXEC")) { 
		// Получаем номера страниц
		String pagesHTML = Bean.getPagesHTML(pageFormName + tagLines, "../crm/finance/kvitovkaspecs.jsp?id=" + id + "&tab="+Bean.currentMenu.getTabID("FINANCE_KVITOVKA_EXEC")+"&date=" + date + "&number=" + number + "&entered_amount=" + entered_amount + "&clearing_amount=" + clearing_amount + "&id_bank_account_client=" + id_bank_account_client + "&id_bank_account_correspondent=" + id_bank_account_correspondent + "&", "lines_page");
	%> 
	
		<% if (Bean.currentMenu.isCurrentTabAndEditPermitted("FINANCE_KVITOVKA_EXEC")) { %>
		<form action="../crm/finance/kvitovkaspecs.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST">
			<input type="hidden" name="id" value="<%= id %>">
			<input type="hidden" name="clearing_amount" value="<%= clearing_amount %>">
		<table <%=Bean.getTableDetailParam() %>>
			<tr>
				<td><%= Bean.bank_statementXML.getfieldTransl("number_bank_account_client", false) %></td>
				<td>
					<%=Bean.getWindowFindBankAccount("bank_account_client", id_bank_account_client, "Y", "_client", "50") %>
				</td>			
				<td><%= Bean.clearingXML.getfieldTransl("operation_date", false) %></td><td><%=Bean.getCalendarInputField("date", date, "10") %>
					<A HREF="#" onClick="JavaScript: document.getElementById('id_operation_date').value = ''; return false;" title="<%=Bean.buttonXML.getfieldTransl("button_delete", false)%>">
					<img vspace="0" hspace="0" src="../images/oper/window_open/delete.png" align="top"></a>
			  	</td>
				<td><%= Bean.clearingXML.getfieldTransl("entered_amount", false) %></td>
					<td><input type="text" name="entered_amount" id="entered_amount" size="20" value="<%= entered_amount %>" class="inputfield">
					<A HREF="#" onClick="JavaScript: document.getElementById('entered_amount').value = ''; return false;" title="<%=Bean.buttonXML.getfieldTransl("button_delete", false)%>">
					<img vspace="0" hspace="0" src="../images/oper/window_open/delete.png" align="top"></a>
					</td>
				<td colspan="6" align="center">
					<%=Bean.getSubmitButtonAjax("../crm/finance/kvitovkaspecs.jsp", "find", "updateForm") %>
				</td>
			</tr>
			<tr>
				<td><%= Bean.bank_statementXML.getfieldTransl("num_bank_account_correspondent", false) %></td>
				<td>
					<%=Bean.getWindowFindBankAccount("bank_account_correspondent", id_bank_account_correspondent, "Y", "_correspondent", "50") %>
				</td>			

				<td><%= Bean.clearingXML.getfieldTransl("number_clearing", false) %></td>
					<td><input type="text" name="number" id="number" size="20" value="<%= number %>" class="inputfield">
					<A HREF="#" onClick="JavaScript: document.getElementById('number').value = ''; return false;" title="<%=Bean.buttonXML.getfieldTransl("button_delete", false)%>">
					<img vspace="0" hspace="0" src="../images/oper/window_open/delete.png" align="top"></a>
					</td>
				<td>&nbsp;</td><td>&nbsp;</td>
			</tr>
			<tr>
				<td colspan="8" align="center">
					<%=Bean.getGoBackButton("../crm/finance/kvitovka.jsp") %>
				</td>
			</tr>
		</table>
		</form>
		<br>
		<!-- Скрипт для втавки меню вибору дати -->
		<%= Bean.getCalendarScript("date", false) %>
		<%} 
	
		bcBankStatementHeaderObject bankStatement = new bcBankStatementHeaderObject();
	%>
	<%= bankStatement.getBankStatementsForKvitovkaHTML(id_bank_account_client, id_bank_account_correspondent, id, date, number, entered_amount, clearing_amount, pagesHTML, l_lines_page_beg, l_lines_page_end) %> 	
<%} 
} %>
</div></div>
</body>
</html>
