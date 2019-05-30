	<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />
<%@page import="bc.objects.bcFNBKSchemeObject"%>
<%@page import="java.util.HashMap"%>
<%@page import="bc.objects.bcClubShortObject"%>
<%@page import="java.util.ArrayList"%>
<%@page import="bc.objects.bcFNInvoiceObject"%>
<%@page import="bc.objects.bcFNInvoiceLineObject"%><html>

<%= Bean.getLogOutScript(request) %>

<head>
	<%= Bean.getMetaContent() %> 
	<%= Bean.getBottomFrameCSS() %>

</head>


<%
HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String pageFormName = "CRM_FINANCE_INVOICE";

Bean.setJspPageForTabName(pageFormName);

request.setCharacterEncoding("UTF-8");

String id		= Bean.getDecodeParam(parameters.get("id"));
String type	= Bean.getDecodeParam(parameters.get("type"));
String action	= Bean.getDecodeParam(parameters.get("action")); 
String process	= Bean.getDecodeParam(parameters.get("process"));

if (id==null || ("".equalsIgnoreCase(id))) id="empty";
if (type==null || ("".equalsIgnoreCase(type))) type="empty";
if (action==null || ("".equalsIgnoreCase(action))) action="empty";
if (process==null || ("".equalsIgnoreCase(process))) process="empty";

if (type.equalsIgnoreCase("general")) {
	if (process.equalsIgnoreCase("no")) {
    	if (action.equalsIgnoreCase("add") || action.equalsIgnoreCase("add2")) {

    		bcClubShortObject club = new bcClubShortObject(Bean.getCurrentClubID());
    		
    		%>
		<script>
			var formData = new Array (
				new Array ('date_invoice', 'varchar2', 1),
				new Array ('name_jur_prs_payer', 'varchar2', 1),
				new Array ('name_jur_prs_receiver', 'varchar2', 1),
				new Array ('cd_currency', 'varchar2', 1),
				new Array ('name_club', 'varchar2', 1),
				new Array ('cd_fn_priority', 'varchar2', 1),
				new Array ('name_bank_account_receiver', 'varchar2', 1),
				new Array ('cd_fn_invoice_state', 'varchar2', 1)
			);
			function myValidateForm(){
				return validateForm(formData);
			}
		</script>
<body> 
		<%= Bean.getOperationTitle(
					Bean.clearingXML.getfieldTransl("h_add_invoice", false),
					"Y",
					"Y") 
		%>        

    <form action="../crm/finance/invoiceupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">
       	<input type="hidden" name="type" value="general">
        <input type="hidden" name="action" value="add">
        <input type="hidden" name="process" value="yes">
        <input type="hidden" name="id" value="<%= id %>">
	<table <%=Bean.getTableDetailParam() %>>
		<tr>
			<td><%= Bean.clearingXML.getfieldTransl("number_invoice", false) %> </td><td><input type="text" name="number_invoice" size="20" value="" class="inputfield"></td>
			<td><%= Bean.clubXML.getfieldTransl("club", true) %>
				<%= Bean.getGoToClubLink(club.getValue("ID_CLUB")) %>
			</td>
		  	<td>
				<%=Bean.getWindowFindClub("club", club.getValue("ID_CLUB"), club.getValue("SNAME_CLUB"), "25") %>
	  		</td>
		</tr>
		<tr>
			<td><%= Bean.clearingXML.getfieldTransl("date_invoice", true) %></td> <td><%=Bean.getCalendarInputField("date_invoice", "", "10") %></td>
			<td><%= Bean.clearingXML.getfieldTransl("name_fn_priority", true) %></td> <td><select name="cd_fn_priority" class="inputfield"><%= Bean.getFinancePriorityOptions("MEDIUM", true) %></select></td>
		</tr>
		<tr>
			<td><%= Bean.clearingXML.getfieldTransl("sname_jur_prs_receiver", true) %></td>
			<td>
				<%=Bean.getWindowFindJurPrs("jur_prs_receiver", "", "", "ALL", "50") %>
			</td>			
			<td colspan="2">&nbsp;</td>
		</tr>
		<tr>
			<td><%= Bean.clearingXML.getfieldTransl("sname_jur_prs_payer", true) %></td>
			<td>
				<%=Bean.getWindowFindJurPrs("jur_prs_payer", "", "", "ALL", "50") %>
			</td>			
			<td colspan="2"><b><i><%= Bean.clearingXML.getfieldTransl("title_invoice_period", false) %></i></b></td>
		</tr>
		<tr>
			<td><%= Bean.clearingXML.getfieldTransl("invoice_doc", false) %></td>
			<td>
				<%=Bean.getWindowDocuments("doc", "", "50") %>
			</td>			
			<td><%= Bean.clearingXML.getfieldTransl("begin_period_date", false) %></td> <td><%=Bean.getCalendarInputField("begin_period_date", "", "10") %></td>
		</tr>
		<tr>
			<td><%= Bean.clearingXML.getfieldTransl("cd_currency", true) %></td> <td><select name="cd_currency" class="inputfield"><%= Bean.getCurrencyOptions(club.getValue("CD_CURRENCY_BASE"), true) %></select></td>
			<td><%= Bean.clearingXML.getfieldTransl("end_period_date", false) %></td> <td><%=Bean.getCalendarInputField("end_period_date", "", "10") %></td>
		</tr>
		<tr><td colspan="4">&nbsp;</td></tr>
		<tr><td colspan="4" class="top_line_gray"><%= Bean.clearingXML.getfieldTransl("title_payment_details", false) %></td></tr>
		<tr>
			<td><%= Bean.clearingXML.getfieldTransl("bank_account_receiver", true) %></td>
			<td>
				<%=Bean.getWindowFindBankAccountJurPrs("bank_account_receiver", "", "'+document.getElementById('id_jur_prs_receiver').value+'", "50") %>
			</td>			
			<td><%= Bean.clearingXML.getfieldTransl("name_fn_invoice_state", true) %></td> <td><select name="cd_fn_invoice_state" class="inputfield"><%= Bean.getFinanceInvoiceStateOptions("ISNT_PAID", true) %></select></td>
		</tr>
		<tr><td colspan="4">&nbsp;</td></tr>
 		<tr>
			<td colspan="6" align="center">
				<%=Bean.getSubmitButtonAjax("../crm/finance/invoiceupdate.jsp", "submit", "updateForm", "div_data_detail") %>
				<%=Bean.getResetButton() %>
				<% if ("add".equalsIgnoreCase(action)) { %>
					<%=Bean.getGoBackButton("../crm/finance/invoice.jsp") %>
				<% } else { %>
					<%=Bean.getGoBackButton("../crm/finance/invoicespecs.jsp?id=" + id) %>
				<% } %>
			</td>
		</tr>

	</table>

	</form>
	<%= Bean.getCalendarScript("date_invoice", false) %>
	<%= Bean.getCalendarScript("begin_period_date", false) %>
	<%= Bean.getCalendarScript("end_period_date", false) %>

    <%
    	} else if (action.equalsIgnoreCase("createdet")) {
    		bcFNInvoiceObject invoice = new bcFNInvoiceObject(id);
    		
    		%>
		<script>
			var linkType = "";
			var formData = new Array (
				new Array ('action', 'varchar2', 1)
			);
			
			function myValidateForm(){
				var res = validateForm(formData);
				if (res) {
					if (linkType == 'NEW') {
						ajaxpage('../crm/finance/invoiceupdate.jsp?' + mySubmitForm('updateForm'),'div_main');
					} else {
						alert(linkType);
						ajaxpage('../crm/finance/invoiceupdate.jsp?' + mySubmitForm('updateForm'),'div_data_detail');
					}
				}
				return res;
			}
			function setButtonLink (tp) {
				linkType = tp;
			}
		</script>
<body> 
		<%= Bean.getOperationTitleShort(
					"",
					Bean.clearingXML.getfieldTransl("h_invoice_actions", false),
					"Y",
					"Y") 
		%>        

    <form action="../crm/finance/invoiceupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">
       	<input type="hidden" name="type" value="general">
        <input type="hidden" name="process" value="no">
        <input type="hidden" name="id" value="<%= id %>">
	<table <%=Bean.getTableDetailParam() %>>
		<tr>
			<td><%= Bean.clearingXML.getfieldTransl("number_invoice", false) %> </td><td><input type="text" name="number_invoice" size="20" value="<%= invoice.getValue("NUMBER_INVOICE") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td><%= Bean.clearingXML.getfieldTransl("date_invoice", false) %></td> <td><input type="text" name="date_invoice" size="20" value="<%= invoice.getValue("DATE_INVOICE_DF") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td><%= Bean.clearingXML.getfieldTransl("total_sum", false) %>, <%=invoice.getValue("SNAME_CURRENCY") %> </td><td><input type="text" name="total_sum" size="20" value="<%= invoice.getValue("TOTAL_SUM_FRMT") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td><%= Bean.clearingXML.getfieldTransl("title_current_invoice", false) %> </td>
			<td>
				<%=Bean.getInputRadioGroupElement("action", "pay", Bean.clearingXML.getfieldTransl("title_current_invoice_pay", false), "pay", "color:green;", "setButtonLink('CURRENT');", false, false) %>
			</td>
		</tr>
		<tr>
			<td>&nbsp;</td>
			<td>
				<%=Bean.getInputRadioGroupElement("action", "cancel", Bean.clearingXML.getfieldTransl("title_current_invoice_cancel", false), "cancel", "color:blue;", "setButtonLink('CURRENT');", false, false) %>
			</td>
		</tr>
		<tr>
			<td><%= Bean.clearingXML.getfieldTransl("title_new_invoice", false) %> </td>
			<td>
				<%=Bean.getInputRadioGroupElement("action", "create", Bean.clearingXML.getfieldTransl("h_create_invoice", false), "create", "font-weight:bold;", "setButtonLink('NEW');", false, false) %><br>
			</td>
		</tr>
 		<tr>
			<td colspan="6" align="center">
				<%=Bean.getSubmitButtonAjax("../crm/finance/invoiceupdate.jsp", "save", "updateForm", "div_main", "myValidateForm", "submitButton1", false) %>
				<%=Bean.getResetButton() %>
				<% if ("add".equalsIgnoreCase(action)) { %>
					<%=Bean.getGoBackButton("../crm/finance/invoice.jsp") %>
				<% } else { %>
					<%=Bean.getGoBackButton("../crm/finance/invoicespecs.jsp?id=" + id) %>
				<% } %>
			</td>
		</tr>

	</table>

	</form>

    <%
    		
    	} else if (action.equalsIgnoreCase("create") || action.equalsIgnoreCase("create2")) {

    		bcClubShortObject club = new bcClubShortObject(Bean.getCurrentClubID());
    		
    		%>
		<script>
			var formData = new Array (
				new Array ('date_invoice', 'varchar2', 1),
				new Array ('name_partner1', 'varchar2', 1),
				new Array ('name_partner2', 'varchar2', 1),
				new Array ('cd_currency', 'varchar2', 1),
				new Array ('name_club', 'varchar2', 1),
				new Array ('cd_fn_priority', 'varchar2', 1),
				new Array ('begin_period_date', 'varchar2', 1),
				new Array ('end_period_date', 'varchar2', 1)
			);
			function myValidateForm(){
				return validateForm(formData);
			}
		</script>
<body> 
		<%= Bean.getOperationTitle(
					Bean.clearingXML.getfieldTransl("h_create_invoice", false),
					"Y",
					"Y") 
		%>
	<script type="text/javascript">
		function checkAutomatic() {
			var invoiceNumber = document.getElementById('number_invoice');
			var isAutomatic = document.getElementById('create_automatic').checked;
			invoiceNumber.className = isAutomatic?"inputfield-ro":"inputfield";
			invoiceNumber.readOnly = isAutomatic?"readonly":"";
			invoiceNumber.value = isAutomatic?"":invoiceNumber.value;
		}
	</script>
        

    <form action="../crm/finance/invoiceupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">
       	<input type="hidden" name="type" value="general">
        <input type="hidden" name="action" value="create">
        <input type="hidden" name="process" value="yes">
        <input type="hidden" name="id" value="<%= id %>">
	<table <%=Bean.getTableDetailParam() %>>
		<tr>
			<td><%= Bean.clearingXML.getfieldTransl("number_invoice", false) %> </td>
			<td>
				<input type="text" name="number_invoice" id="number_invoice" size="20" value="" class="inputfield">
				<%=Bean.getCheckBoxBase("create_automatic", "", "Создать автоматически", "color:red;", "checkAutomatic()", false, false) %>
			</td>
			<td><%= Bean.clubXML.getfieldTransl("club", true) %>
				<%= Bean.getGoToClubLink(club.getValue("ID_CLUB")) %>
			</td>
		  	<td>
				<%=Bean.getWindowFindClub("club", club.getValue("ID_CLUB"), club.getValue("SNAME_CLUB"), "25") %>
	  		</td>
		</tr>
		<tr>
			<td><%= Bean.clearingXML.getfieldTransl("date_invoice", true) %></td> <td><%=Bean.getCalendarInputField("date_invoice", Bean.getSysDate(), "10") %></td>
			<td><%= Bean.clearingXML.getfieldTransl("name_fn_priority", true) %></td> <td><select name="cd_fn_priority" class="inputfield"><%= Bean.getFinancePriorityOptions("MEDIUM", true) %></select></td>
		</tr>
		<tr>
			<td><%= Bean.clearingXML.getfieldTransl("partner1", true) %></td>
			<td>
				<%=Bean.getWindowFindJurPrs("partner1", club.getValue("ID_OPERATOR"), "", "ALL", "50") %>
			</td>			
			<td colspan="2">&nbsp;</td>
		</tr>
		<tr>
			<td><%= Bean.clearingXML.getfieldTransl("partner2", true) %></td>
			<td>
				<%=Bean.getWindowFindJurPrs("partner2", "", "", "ALL", "50") %>
			</td>			
			<td colspan="2"><b><i><%= Bean.clearingXML.getfieldTransl("title_invoice_period", false) %></i></b></td>
		</tr>
		<tr>
			<td><%= Bean.clearingXML.getfieldTransl("invoice_doc", false) %></td>
			<td>
				<%=Bean.getWindowDocuments("doc", "", "50") %>
			</td>			
			<td><%= Bean.clearingXML.getfieldTransl("begin_period_date", true) %></td> <td><%=Bean.getCalendarInputField("begin_period_date", "", "10") %></td>
		</tr>
		<tr>
			<td><%= Bean.clearingXML.getfieldTransl("cd_currency", true) %></td> <td><select name="cd_currency" class="inputfield"><%= Bean.getCurrencyOptions(club.getValue("CD_CURRENCY_BASE"), true) %></select></td>
			<td><%= Bean.clearingXML.getfieldTransl("end_period_date", true) %></td> <td><%=Bean.getCalendarInputField("end_period_date", "", "10") %></td>
		</tr>
		<tr><td colspan="4">&nbsp;</td></tr>
 		<tr>
			<td colspan="6" align="center">
				<%=Bean.getSubmitButtonAjax("../crm/finance/invoiceupdate.jsp", "submit", "updateForm", "div_main") %>
				<%=Bean.getResetButton() %>
				<% if ("create".equalsIgnoreCase(action)) { %>
					<%=Bean.getGoBackButton("../crm/finance/invoice.jsp") %>
				<% } else { %>
					<%=Bean.getGoBackButton("../crm/finance/invoicespecs.jsp?id=" + id) %>
				<% } %>
			</td>
		</tr>

	</table>

	</form>
	<%= Bean.getCalendarScript("date_invoice", false) %>
	<%= Bean.getCalendarScript("begin_period_date", false) %>
	<%= Bean.getCalendarScript("end_period_date", false) %>

    <%
    	} else {
    	    %> <%= Bean.getUnknownActionText(action) %><%
    	}
	
	} else if (process.equalsIgnoreCase("yes"))	{
%>
<body>
<%
		String
			number_invoice 				= Bean.getDecodeParam(parameters.get("number_invoice")),
			create_automatic 			= Bean.getDecodeParam(parameters.get("create_automatic")),
			date_invoice 				= Bean.getDecodeParam(parameters.get("date_invoice")),
			id_jur_prs_receiver			= Bean.getDecodeParam(parameters.get("id_jur_prs_receiver")),
			id_jur_prs_payer 			= Bean.getDecodeParam(parameters.get("id_jur_prs_payer")),
			id_doc			 			= Bean.getDecodeParam(parameters.get("id_doc")),
			id_club			 			= Bean.getDecodeParam(parameters.get("id_club")),
			cd_fn_priority			 	= Bean.getDecodeParam(parameters.get("cd_fn_priority")),
			cd_currency			 		= Bean.getDecodeParam(parameters.get("cd_currency")),
			id_bank_account_receiver	= Bean.getDecodeParam(parameters.get("id_bank_account_receiver")),
			cd_fn_invoice_state			= Bean.getDecodeParam(parameters.get("cd_fn_invoice_state")),
			cd_fn_invoice_creation		= "MANUAL",
			cd_fn_invoice_module		= "CRM",
			begin_period_date			= Bean.getDecodeParam(parameters.get("begin_period_date")),
			end_period_date				= Bean.getDecodeParam(parameters.get("end_period_date")),
			LUD							= Bean.getDecodeParam(parameters.get("LUD")),
			id_partner1		 			= Bean.getDecodeParam(parameters.get("id_partner1")),
			id_partner2		 			= Bean.getDecodeParam(parameters.get("id_partner2")),
			paid_sum		 			= Bean.getDecodeParam(parameters.get("paid_sum"));

		ArrayList<String> pParam = new ArrayList<String>();

		if (action.equalsIgnoreCase("add")) { 

			pParam.add(number_invoice);
			pParam.add(date_invoice);
			pParam.add(id_jur_prs_receiver);
			pParam.add(id_bank_account_receiver);
			pParam.add(id_jur_prs_payer);
			pParam.add(id_doc);
			pParam.add(cd_currency);
			pParam.add(cd_fn_priority);
			pParam.add(cd_fn_invoice_state);
			pParam.add(paid_sum);
			pParam.add(cd_fn_invoice_creation);
			pParam.add(cd_fn_invoice_module);
			pParam.add(begin_period_date);
			pParam.add(end_period_date);
			pParam.add(id_club);
			pParam.add(Bean.getDateFormat());

		 	%>
			<%= Bean.executeInsertFunction("PACK$INVOICE_UI.add_invoice", pParam, "../crm/finance/invoicespecs.jsp?id=" , "../crm/finance/invoice.jsp") %>
			<% 	

		} else if (action.equalsIgnoreCase("create")) { 

			pParam.add(number_invoice);
			pParam.add(create_automatic);
			pParam.add(date_invoice);
			pParam.add(id_partner1);
			pParam.add(id_partner2);
			pParam.add(id_doc);
			pParam.add(cd_currency);
			pParam.add(cd_fn_priority);
			pParam.add("AUTOMATIC");
			pParam.add("CRM");
			pParam.add(begin_period_date);
			pParam.add(end_period_date);
			pParam.add(id_club);
			pParam.add(Bean.getDateFormat());

		 	%>
			<%= Bean.executeInsertFunction("PACK$INVOICE_UI.create_invoice", pParam, "../crm/finance/invoicespecs.jsp?id=" , "../crm/finance/invoice.jsp") %>
			<% 	

		} else if (action.equalsIgnoreCase("remove")) { 
			
			pParam.add(id);
	
		 	%>
			<%= Bean.executeDeleteFunction("PACK$INVOICE_UI.delete_invoice", pParam, "../crm/finance/invoice.jsp" , "") %>
			<% 	

		} else if (action.equalsIgnoreCase("edit")) { 
			
	 		pParam.add(id);
			pParam.add(number_invoice);
			pParam.add(date_invoice);
			pParam.add(id_jur_prs_receiver);
			pParam.add(id_bank_account_receiver);
			pParam.add(id_jur_prs_payer);
			pParam.add(id_doc);
			pParam.add(cd_currency);
			pParam.add(cd_fn_priority);
			pParam.add(cd_fn_invoice_state);
			pParam.add(paid_sum);
			pParam.add(begin_period_date);
			pParam.add(end_period_date);
			pParam.add(LUD);
			pParam.add(Bean.getDateFormat());
		
		 	%>
			<%= Bean.executeUpdateFunction("PACK$INVOICE_UI.update_invoice", pParam, "../crm/finance/invoicespecs.jsp?id=" + id, "") %>
			<% 	

		} else { %> 
			<%= Bean.getUnknownActionText(action) %><% 
		}
	} else {
 	   %> <%= Bean.getUnknownProcessText(process) %> <%
	}
} else if (type.equalsIgnoreCase("line")) {

	bcFNInvoiceObject invoice = new bcFNInvoiceObject(id);
	
	if (process.equalsIgnoreCase("no")) {
    	if (action.equalsIgnoreCase("add") || action.equalsIgnoreCase("add2")) {
    		
    		%>
		<script>
			var formData = new Array (
				new Array ('name_nomenkl', 'varchar2', 1),
				new Array ('count_nomenkl', 'number', 1),
				new Array ('price_nomenkl', 'oper_sum_zero', 1),
				new Array ('tax_percent', 'oper_sum_zero', 1)
			);
			function myValidateForm(){
				return validateForm(formData);
			}
			calcInvoiceLineTotalSum();
		</script>
<body> 
		<%= Bean.getOperationTitleShort(
					"",
					Bean.clearingXML.getfieldTransl("h_add_invoice_line", false),
					"Y",
					"Y") 
		%>        

    <form action="../crm/finance/invoiceupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">
       	<input type="hidden" name="type" value="line">
        <input type="hidden" name="action" value="add">
        <input type="hidden" name="process" value="yes">
        <input type="hidden" name="id" value="<%= id %>">
	<table <%=Bean.getTableDetailParam() %>>
		<tr>
			<td><%= Bean.clearingXML.getfieldTransl("name_nomenkl", true) %> </td><td><input type="text" name="name_nomenkl" size="60" value="" class="inputfield"></td>
		</tr>
		<tr>
			<td><%= Bean.clearingXML.getfieldTransl("count_nomenkl", true) %> </td><td><input type="text" name="count_nomenkl" id="count_nomenkl" size="20" value="" class="inputfield" onchange="calcInvoiceLineTotalSum();"></td>
		</tr>
 		<tr>
			<td><%= Bean.clearingXML.getfieldTransl("price_nomenkl", true) %>, <%=invoice.getValue("SNAME_CURRENCY") %></td><td><input type="text" name="price_nomenkl" id="price_nomenkl" size="20" value="" class="inputfield" onchange="calcInvoiceLineTotalSum();"></td>
		</tr>
		<tr>
			<td><b><%= Bean.clearingXML.getfieldTransl("total_sum", false) %>, <%=invoice.getValue("SNAME_CURRENCY") %></b></td><td><input type="text" name="total_amount" id="total_amount" size="20" value="" readonly="readonly" class="inputfield-ro"></td>
		</tr>
 		<tr>
			<td><%= Bean.clearingXML.getfieldTransl("tax_percent", true) %> </td><td><input type="text" name="tax_percent" id="tax_percent" size="20" value="" class="inputfield" onchange="calcInvoiceLineTotalSum();"></td>
		</tr>
		<tr>
			<td><%= Bean.clearingXML.getfieldTransl("tax_sum", false) %>, <%=invoice.getValue("SNAME_CURRENCY") %> </td><td><input type="text" name="tax_amount" id="tax_amount" size="20" value="" readonly="readonly" class="inputfield-ro"></td>
		</tr>
  		<tr>
			<td colspan="6" align="center">
				<%=Bean.getSubmitButtonAjax("../crm/finance/invoiceupdate.jsp", "submit", "updateForm", "div_data_detail") %>
				<%=Bean.getResetButton() %>
				<%=Bean.getGoBackButton("../crm/finance/invoicespecs.jsp?id=" + id) %>
			</td>
		</tr>

	</table>

	</form>

    <%
		} else if (action.equalsIgnoreCase("edit")) {
			
			String id_invoice_line 		= Bean.getDecodeParam(parameters.get("id_invoice_line"));

    		bcFNInvoiceLineObject line = new bcFNInvoiceLineObject(id_invoice_line);
    		
    		%>
		<script>
			var formData = new Array (
				new Array ('name_nomenkl', 'varchar2', 1),
				new Array ('count_nomenkl', 'number', 1),
				new Array ('price_nomenkl', 'oper_sum_zero', 1),
				new Array ('tax_percent', 'oper_sum_zero', 1)
			);
			function myValidateForm(){
				return validateForm(formData);
			}
			calcInvoiceLineTotalSum();
		</script>
<body> 
		<%= Bean.getOperationTitleShort(
					"",
					Bean.clearingXML.getfieldTransl("h_edit_invoice_line", false),
					"Y",
					"Y") 
		%>        

    <form action="../crm/finance/invoiceupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">
       	<input type="hidden" name="type" value="line">
        <input type="hidden" name="action" value="edit">
        <input type="hidden" name="process" value="yes">
        <input type="hidden" name="id" value="<%= id %>">
        <input type="hidden" name="id_invoice_line" value="<%= id_invoice_line %>">
	<table <%=Bean.getTableDetailParam() %>>
		<tr>
			<td><%= Bean.clearingXML.getfieldTransl("name_nomenkl", true) %> </td><td><input type="text" name="name_nomenkl" size="60" value="<%=line.getValue("NAME_NOMENKL") %>" class="inputfield"></td>
		</tr>
		<tr>
			<td><%= Bean.clearingXML.getfieldTransl("count_nomenkl", true) %> </td><td><input type="text" name="count_nomenkl" id="count_nomenkl" size="20" value="<%=line.getValue("COUNT_NOMENKL") %>" class="inputfield" onchange="calcInvoiceLineTotalSum();"></td>
		</tr>
 		<tr>
			<td><%= Bean.clearingXML.getfieldTransl("price_nomenkl", true) %>, <%=invoice.getValue("SNAME_CURRENCY") %></td><td><input type="text" name="price_nomenkl" id="price_nomenkl" size="20" value="<%=line.getValue("PRICE_NOMENKL_FRMT") %>" class="inputfield" onchange="calcInvoiceLineTotalSum();"></td>
		</tr>
		<tr>
			<td><b><%= Bean.clearingXML.getfieldTransl("total_sum", false) %>, <%=invoice.getValue("SNAME_CURRENCY") %></b></td><td><input type="text" name="total_amount" id="total_amount" size="20" value="<%=line.getValue("TOTAL_AMOUNT_FRMT") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
 		<tr>
			<td><%= Bean.clearingXML.getfieldTransl("tax_percent", true) %> </td><td><input type="text" name="tax_percent" id="tax_percent" size="20" value="<%=line.getValue("TAX_PERCENT") %>" class="inputfield" onchange="calcInvoiceLineTotalSum();"></td>
		</tr>
		<tr>
			<td><%= Bean.clearingXML.getfieldTransl("tax_sum", false) %>, <%=invoice.getValue("SNAME_CURRENCY") %> </td><td><input type="text" name="tax_amount" id="tax_amount" size="20" value="<%=line.getValue("TAX_AMOUNT_FRMT") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
  		<tr>
			<td colspan="6" align="center">
				<%=Bean.getSubmitButtonAjax("../crm/finance/invoiceupdate.jsp", "submit", "updateForm", "div_data_detail") %>
				<%=Bean.getResetButton() %>
				<%=Bean.getGoBackButton("../crm/finance/invoicespecs.jsp?id=" + id) %>
			</td>
		</tr>

	</table>

	</form>

    <%
		} else {
		    %> <%= Bean.getUnknownActionText(action) %><%
		}
	
	} else if (process.equalsIgnoreCase("yes"))	{
		%>
		<body>
		<%
		String
			id_invoice_line 			= Bean.getDecodeParam(parameters.get("id_invoice_line")),
			order_number 				= Bean.getDecodeParam(parameters.get("order_number")),
			name_nomenkl 				= Bean.getDecodeParam(parameters.get("name_nomenkl")),
			count_nomenkl				= Bean.getDecodeParam(parameters.get("count_nomenkl")),
			price_nomenkl 				= Bean.getDecodeParam(parameters.get("price_nomenkl")),
			tax_percent			 		= Bean.getDecodeParam(parameters.get("tax_percent")),
			LUD							= Bean.getDecodeParam(parameters.get("LUD"));

		ArrayList<String> pParam = new ArrayList<String>();

		if (action.equalsIgnoreCase("add")) { 

			pParam.add(id);
			pParam.add(name_nomenkl);
			pParam.add(count_nomenkl);
			pParam.add(price_nomenkl);
			pParam.add(tax_percent);

			%>
			<%= Bean.executeInsertFunction("PACK$INVOICE_UI.add_invoice_line", pParam, "../crm/finance/invoicespecs.jsp?id=" + id + "&id_invoice_line=" , "") %>
			<% 	

		} else if (action.equalsIgnoreCase("remove")) { 
					
			pParam.add(id_invoice_line);
	
		 	%>
			<%= Bean.executeDeleteFunction("PACK$INVOICE_UI.delete_invoice_line", pParam, "../crm/finance/invoicespecs.jsp?id=" + id, "") %>
			<% 	

		} else if (action.equalsIgnoreCase("edit")) { 
					
	 		pParam.add(id_invoice_line);
			pParam.add(name_nomenkl);
			pParam.add(count_nomenkl);
			pParam.add(price_nomenkl);
			pParam.add(tax_percent);
			pParam.add(LUD);
				
		 	%>
			<%= Bean.executeUpdateFunction("PACK$INVOICE_UI.update_invoice_line", pParam, "../crm/finance/invoicespecs.jsp?id=" + id, "") %>
			<% 	

		} else { %> 
			<%= Bean.getUnknownActionText(action) %><% 
		}
	} else {
 	   %> <%= Bean.getUnknownProcessText(process) %> <%
	}
} else {
   %> <%= Bean.getUnknownTypeText(type) %> <%
}
%>


</body>
</html>
