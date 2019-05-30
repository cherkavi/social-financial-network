<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%= Bean.getLogOutScript(request) %>

<%@page import="bc.objects.bcPostingEditObject"%><html>
<head>
	<%= Bean.getMetaContent() %>
	<%= Bean.getBottomFrameCSS() %>
</head>
<body>

<%
HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String pageFormName = "FINANCE_REMITTANCE";

Bean.setJspPageForTabName(pageFormName);

request.setCharacterEncoding("UTF-8");

String id			= Bean.getDecodeParam(parameters.get("id"));
String type			= Bean.getDecodeParam(parameters.get("type")); 
String action		= Bean.getDecodeParam(parameters.get("action")); 
String process		= Bean.getDecodeParam(parameters.get("process"));
String id_club	= Bean.getDecodeParam(parameters.get("id_club"));

if (id==null || ("".equalsIgnoreCase(id))) id="";
if (type==null || ("".equalsIgnoreCase(type))) type="";
if (action==null || ("".equalsIgnoreCase(action))) action="";
if (process==null || ("".equalsIgnoreCase(process))) process="";
if (id_club==null || ("".equalsIgnoreCase(id_club))) id_club="";

if (type.equalsIgnoreCase("general")) {
	if (process.equalsIgnoreCase("no")) {
	   /*  --- Добавити запис --- */
    	if (action.equalsIgnoreCase("add") || action.equalsIgnoreCase("add2")) {
	        
	        %>
		<script>
			var formData = new Array (
					new Array ('type', 'varchar2', 1)
				);
		</script>
			<%= Bean.getOperationTitle(
					Bean.remittanceXML.getfieldTransl("h_remittance_add", false),
					"Y",
					"N") 
			%>
        <form action="../crm/finance/remittanceupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST">
			<input type="hidden" name="type" value="general">
			<input type="hidden" name="action" value="adddet">
			<input type="hidden" name="action_prev" value="<%=action %>">
			<input type="hidden" name="process" value="no">
			<input type="hidden" name="id" value="<%=id %>">
			<table <%=Bean.getTableDetailParam() %>>
				<tr>
					<%
					  	String pRemittanceType = Bean.getDecodeParam(parameters.get("cd_remittance_type"));
						if (pRemittanceType==null || "".equalsIgnoreCase(pRemittanceType)) {
							String pUserRemittanceType = Bean.getUIUserParam("REMITTANCE_TYPE");	
							if (pUserRemittanceType==null || "".equalsIgnoreCase(pUserRemittanceType)) {
								pRemittanceType = "REMITTANCE_GOODS";
							} else {
								pRemittanceType = pUserRemittanceType;
							}
						}
					%>
					<td><%=Bean.remittanceXML.getfieldTransl("cd_remittance_type", true)%></td><td><%= Bean.getRemittanceTypeRadio("cd_remittance_type", pRemittanceType) %></td>
				</tr>
		 		<tr>
					<td colspan="2" align="center">
						<%=Bean.getSubmitButtonAjax("../crm/finance/remittanceupdate.jsp") %>
						<%=Bean.getResetButton() %>
						<% if (action.equalsIgnoreCase("add")) { %>
							<%=Bean.getGoBackButton("../crm/finance/remittance.jsp") %>
						<% } else { %>
							<%=Bean.getGoBackButton("../crm/finance/remittancespecs.jsp?id=" + id) %>
						<% } %>
					</td>
				</tr>
		
			</table>
		</form>

	        <%
	        /*  --- Видалити запис --- */
		} else if (action.equalsIgnoreCase("adddet")) {
			
			bcClubShortObject club = new bcClubShortObject(Bean.getCurrentClubID());
			
			String 
				action_prev				= Bean.getDecodeParam(parameters.get("action_prev")),
				cd_remittance_type		= Bean.getDecodeParam(parameters.get("cd_remittance_type"));
%> 

	<script>
		var formAll = new Array (
		);
		var formGeneral = new Array (
			new Array ('date_remittance', 'varchar2', 1),
			new Array ('name_club', 'varchar2', 1),
			new Array ('basis_of_remittance', 'varchar2', 1)
		);
		var formFromBankAccount = new Array (
			new Array ('name_bank_account_from', 'varchar2', 1)
		);
		var formFromCard = new Array (
			new Array ('from_cd_card1', 'varchar2', 1)
		);
		var formToBankAccount = new Array (
			new Array ('name_bank_account_to', 'varchar2', 1)
		);
		var formToCard = new Array (
			new Array ('to_cd_card1', 'varchar2', 1)
		);
		function myValidateForm() {
			var fromType = '<%=cd_remittance_type%>';
			var toType = '<%=cd_remittance_type%>';
			
			formAll = formGeneral;
			if (fromType == 'BON_CARD') {
				formAll = formAll.concat(formFromCard);
			} else if (remittanceType == 'BANK_ACCOUNT') {
				formAll = formAll.concat(formFromBankAccount);
			}
			if (toType == 'BON_CARD') {
				formAll = formAll.concat(formToCard);
			} else if (remittanceType == 'BANK_ACCOUNT') {
				toType = formAll.concat(formToBankAccount);
			}
			//alert(formAll);
			return validateForm(formAll);
		}
	</script>
		<%= Bean.getOperationTitle(
			Bean.remittanceXML.getfieldTransl("h_remittance_add", false),
			"Y",
			"N") 
		%>
    <form action="../crm/finance/remittanceupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">
        <input type="hidden" name="action" value="add">
        <input type="hidden" name="process" value="yes">
        <input type="hidden" name="type" value="general">
        <input type="hidden" name="cd_remittance_type" value="<%= cd_remittance_type %>">

	<table <%=Bean.getTableDetailParam() %>>
		<tr>
			<td><%= Bean.clubXML.getfieldTransl("club", false) %>
				<%= Bean.getGoToClubLink(club.getValue("ID_CLUB")) %>
			</td>
			<td>
				<input type="text" name="name_club" size="35" value="<%= club.getValue("SNAME_CLUB") %>" readonly="readonly" class="inputfield-ro">
			</td>
		</tr>
		<tr>
			<td><%=Bean.remittanceXML.getfieldTransl("cd_remittance_type", false)%> </td><td><input type="text" name="cd_remittance_type" size="64" value="<%=Bean.getRemittanceTypeName(cd_remittance_type)%>" readonly="readonly" class="inputfield-ro"></td>
			<% if ("BON_CARD".equalsIgnoreCase(cd_remittance_type)) {%>
			<td><%=Bean.remittanceXML.getfieldTransl("from_participant", true)%></td> 
			<td>
				<%=Bean.getWindowFindClubCardRemittance("from", "", "", "", "", "30") %>
			</td>
			<% } else if ("BANK_ACCOUNT".equalsIgnoreCase(cd_remittance_type)) {%>
			<td><%=Bean.remittanceXML.getfieldTransl("from_participant", true)%></td>
			<td>
				<%=Bean.getWindowFindBankAccount2("bank_account_from", "", "30") %>
			</td>			
			<% } else if ("CASH".equalsIgnoreCase(cd_remittance_type)) {%>
			<td><b><%=Bean.remittanceXML.getfieldTransl("from_participant", false)%></b></td><td><input type="text" name="from_patricipant" size="35" value="<%=Bean.remittanceXML.getfieldTransl("participant_cash", false)%>" readonly="readonly" class="inputfield-ro"></td>
			<% } %>
		</tr>
		<tr>
			<td><%=Bean.remittanceXML.getfieldTransl("date_remittance", true)%></td><td><%=Bean.getCalendarInputField("date_remittance", Bean.getSysDate(), "10") %></td>
			<% if ("BON_CARD".equalsIgnoreCase(cd_remittance_type)) {%>
			<td><%=Bean.remittanceXML.getfieldTransl("to_participant", true)%></td> 
			<td>
				<%=Bean.getWindowFindClubCardRemittance("to", "", "", "", "", "30") %>
			</td>
			<% } else if ("BANK_ACCOUNT".equalsIgnoreCase(cd_remittance_type)) {%>
			<td><%=Bean.remittanceXML.getfieldTransl("to_participant", true)%></td>
			<td>
				<%=Bean.getWindowFindBankAccount2("bank_account_to", "", "30") %>
			</td>			
			<% } else if ("CASH".equalsIgnoreCase(cd_remittance_type)) {%>
			<td><b><%=Bean.remittanceXML.getfieldTransl("to_participant", false)%></b></td><td><input type="text" name="to_patricipant" size="35" value="<%=Bean.remittanceXML.getfieldTransl("participant_cash", false)%>" readonly="readonly" class="inputfield-ro"></td>
			<% } %>
		</tr>
		<tr>
			<td valign="top"><%= Bean.remittanceXML.getfieldTransl("basis_of_remittance", true) %></td><td colspan="3" colspan="3"><textarea name="basis_of_remittance" cols="60" rows="3" class="inputfield"></textarea></td>
			<% if ("REMITTANCE".equalsIgnoreCase(cd_remittance_type) ||
					"WRITE_OFF_BON_CASH".equalsIgnoreCase(cd_remittance_type) ||
					"WRITE_OFF_BON_NON_CASH".equalsIgnoreCase(cd_remittance_type) ||
					"ADD_BON_CASH".equalsIgnoreCase(cd_remittance_type) ||
					"ADD_BON_FROM_BANK_ACCOUNT".equalsIgnoreCase(cd_remittance_type)) {%>
			<td><%=Bean.remittanceXML.getfieldTransl("bal_full", true)%> </td><td><input type="text" name="bal_full" size="20" value="" class="inputfield"></td>
			<% } %>
		</tr>
		<tr>
			<td colspan="2">&nbsp;</td>
		</tr>
		<tr>
			<td colspan="2">&nbsp;</td>
		</tr>
		<tr>
			<td colspan="6" align="center">
				<%=Bean.getSubmitButtonAjax("../crm/finance/remittanceupdate.jsp") %>
				<%=Bean.getResetButton() %>
				<%=Bean.getGoBackButton("../crm/finance/remittanceupdate.jsp?id=" + id + "&type=general&process=no&action=" + action_prev) %>
			</td>
		</tr>

	</table>
	</form>	
	<!-- Скрипт для втавки меню вибору дати -->
	<%= Bean.getCalendarScript("date_remittance", false) %>
	
       <%
		} else {
	        %> <%= Bean.getUnknownActionText(action) %><%
		}
	} else if (process.equalsIgnoreCase("yes")) {
	    
		String
			date_remittance				= Bean.getDecodeParam(parameters.get("date_remittance")),
	    	from_remittance_type		= Bean.getDecodeParam(parameters.get("from_remittance_type")),
	    	from_card_serial_number		= Bean.getDecodeParam(parameters.get("from_card_serial_number")),
	    	from_card_id_issuer			= Bean.getDecodeParam(parameters.get("from_id_issuer")),
	    	from_card_id_payment_system	= Bean.getDecodeParam(parameters.get("from_id_payment_system")),
	    	from_id_bank_account		= Bean.getDecodeParam(parameters.get("id_bank_account_from")),
	    	from_action_date			= Bean.getDecodeParam(parameters.get("action_date")),
	    	to_remittance_type			= Bean.getDecodeParam(parameters.get("to_remittance_type")),
	    	to_card_serial_number		= Bean.getDecodeParam(parameters.get("to_card_serial_number")),
	    	to_card_id_issuer			= Bean.getDecodeParam(parameters.get("to_id_issuer")),
	    	to_card_id_payment_system	= Bean.getDecodeParam(parameters.get("to_id_payment_system")),
	    	to_id_bank_account			= Bean.getDecodeParam(parameters.get("id_bank_account_to")),
	    	bal_acc						= Bean.getDecodeParam(parameters.get("bal_acc")),
	    	bal_cur						= Bean.getDecodeParam(parameters.get("bal_cur")),
	    	basis_of_remittance			= Bean.getDecodeParam(parameters.get("basis_of_remittance")),
	    	remittance_action			= Bean.getDecodeParam(parameters.get("remittance_action")),
	    	basis_for_action			= Bean.getDecodeParam(parameters.get("basis_for_action"));
	    
	    if (action.equalsIgnoreCase("add")) { 
			
			String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_REMITTANCE.add_remittance("+
				"?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";

			String[] pParam = new String [17];

			pParam[0] = date_remittance;
			pParam[1] = basis_of_remittance;
			pParam[2] = from_remittance_type;
			pParam[3] = from_card_serial_number;
			pParam[4] = from_card_id_issuer;
			pParam[5] = from_card_id_payment_system;
			pParam[6] = from_id_bank_account;
			pParam[7] = from_action_date;
			pParam[8] = to_remittance_type;
			pParam[9] = to_card_serial_number;
			pParam[10] = to_card_id_issuer;
			pParam[11] = to_card_id_payment_system;
			pParam[12] = to_id_bank_account;
			pParam[13] = bal_acc;
			pParam[14] = bal_cur;
			pParam[15] = id_club;
			pParam[16] = Bean.getDateFormat();
			
		 	%>
			<%= Bean.getCallResultParam("INSERT", callSQL, pParam, "../crm/finance/remittancespecs.jsp?id=" , "../crm/finance/remittance.jsp") %>
			<% 	

	    } else if (action.equalsIgnoreCase("remove")) { 
	    	
	    	String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_REMITTANCE.delete_remittance(?,?)}";

			String[] pParam = new String [1];

			pParam[0] = id;
			
	 		%>
			<%= Bean.getCallResultParam("DELETE", callSQL, pParam, "../crm/finance/remittance.jsp" , "") %>
			<% 	
	 	
	    } else if (action.equalsIgnoreCase("edit")) { 
			
			String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_REMITTANCE.update_remittance("+
				"?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";

			String[] pParam = new String [16];

			pParam[0] = id;
			pParam[1] = date_remittance;
			pParam[2] = basis_of_remittance;
			pParam[3] = from_card_serial_number;
			pParam[4] = from_card_id_issuer;
			pParam[5] = from_card_id_payment_system;
			pParam[6] = from_id_bank_account;
			pParam[7] = from_action_date;
			pParam[8] = to_card_serial_number;
			pParam[9] = to_card_id_issuer;
			pParam[10] = to_card_id_payment_system;
			pParam[11] = to_id_bank_account;
			pParam[12] = bal_acc;
			pParam[13] = bal_cur;
			pParam[14] = id_club;
			pParam[15] = Bean.getDateFormat();
			
	 		%>
			<%= Bean.getCallResultParam("UPDATE", callSQL, pParam, "../crm/finance/remittancespecs.jsp?id=" + id, "") %>
			<% 		
	 	
	    } else if (action.equalsIgnoreCase("set_action")) { 
			
			String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_REMITTANCE.update_remittance_action(?,?,?,?)}";

			String[] pParam = new String [3];

			pParam[0] = id;
			pParam[1] = remittance_action;
			pParam[2] = basis_for_action;
			
	 		%>
			<%= Bean.getCallResultParam("UPDATE", callSQL, pParam, "../crm/finance/remittancespecs.jsp?id=" + id, "") %>
			<%
			
	    } else if (action.equalsIgnoreCase("remove_posting")) { 
			
			String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_REMITTANCE.delete_posting(?,?)}";

			String[] pParam = new String [1];

			pParam[0] = id;
			
	 		%>
			<%= Bean.getCallResultParam("DELETE", callSQL, pParam, "../crm/finance/remittancespecs.jsp?id=" + id, "") %>
			<%
			
	    } else { %> 
	    	<%= Bean.getUnknownActionText(action) %><% 
	    }
	} else {
	    %> <%= Bean.getUnknownProcessText(process) %> <%
	}
	
} else if (type.equalsIgnoreCase("posting")) { %>
	<script>
		var formAll = new Array();
		var formData = new Array (
			new Array ('operation_date', 'varchar2', 1),
			new Array ('debet_name_bk_account', 'varchar2', 1),
			new Array ('cd_currency', 'varchar2', 1),
			new Array ('credit_name_bk_account', 'varchar2', 1),
			new Array ('entered_amount', 'varchar2', 1),
			new Array ('run_postings_export', 'varchar2', 1),
			new Array ('using_in_clearing', 'varchar2', 1)
		);
		var formDataClearing = new Array (
			new Array ('name_bank_account_debet', 'varchar2', 1),
			new Array ('name_bank_account_credit', 'varchar2', 1)
		);
		var formDataExport = new Array (
			new Array ('cd_bk_doc_type', 'varchar2', 1)
		);
	
		function myValidateForm() {
			var using = document.getElementById('using_in_clearing').value;
			var run_exp = document.getElementById('run_postings_export').value;
	
			formAll = formData;
			if (using == 'Y') {
				formAll = formAll.concat(formDataClearing);
			}
			if (run_exp == 'Y') {
				formAll = formAll.concat(formDataExport);
			}
			return validateForm(formAll);
		}
		
		function checkClearing(){
			var using = document.getElementById('using_in_clearing').value;
			if (using == 'Y') {
				document.getElementById('span_receiver_id_bank_account').innerHTML='<%= Bean.postingXML.getfieldTransl("receiver_id_bank_account", true) %>';
				document.getElementById('span_payer_id_bank_account').innerHTML='<%= Bean.postingXML.getfieldTransl("payer_id_bank_account", true) %>';
			} else {
				document.getElementById('span_receiver_id_bank_account').innerHTML='<%= Bean.postingXML.getfieldTransl("receiver_id_bank_account", false) %>';
				document.getElementById('span_payer_id_bank_account').innerHTML='<%= Bean.postingXML.getfieldTransl("payer_id_bank_account", false) %>';
			}
		}
		
		function checkBKDocType(){
			var run_exp = document.getElementById('run_postings_export').value;
			if (run_exp == 'Y') {
				document.getElementById('span_cd_bk_doc_type').innerHTML='<%= Bean.oper_schemeXML.getfieldTransl("cd_bk_doc_type", true) %>';
			} else {
				document.getElementById('span_cd_bk_doc_type').innerHTML='<%= Bean.oper_schemeXML.getfieldTransl("cd_bk_doc_type", false) %>';
			}
		}
		checkClearing();
		checkBKDocType();
		
		function changeRelationShipParam(id, name){
			document.getElementById('id_club_rel').value = id;
			document.getElementById('name_club_rel').value = name;
			document.getElementById('name_club_rel').className = "inputfield_modified";
		}
	</script>
	

	<%
	String
		id_posting_detail	= Bean.getDecodeParam(parameters.get("id_posting_detail"));
	
	if (process.equalsIgnoreCase("no")) {
		if (action.equalsIgnoreCase("add")) { 
		
			bcPostingEditObject posting = new bcPostingEditObject();
		
	%>

	<%= Bean.getOperationTitle(
		Bean.remittanceXML.getfieldTransl("LAB_ADD_POSTING", false),
		"Y",
		"Y") 
	%>
	<form action="../crm/finance/remittanceupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">
        <input type="hidden" name="type" value="posting">
        <input type="hidden" name="action" value="add">
        <input type="hidden" name="process" value="yes">
        <input type="hidden" name="id" value="<%=id %>">
        <input type="hidden" name="id_club" value="<%=id_club %>">
	<table <%=Bean.getTableDetailParam() %>>

			<%=posting.getPostingAddHTML("", Bean.getDateFormatTitle()) %>

		<tr>
			<td colspan="6" align="center">
				<%=Bean.getSubmitButtonAjax("../crm/finance/remittanceupdate.jsp") %>
				<%=Bean.getResetButton() %>
				<%=Bean.getGoBackButton("../crm/finance/remittancespecs.jsp?id=" + id) %>
			</td>
		</tr>
	</table>
	</form>	
	<script type="text/javascript">
  		Calendar.setup({
			inputField  : "id_operation_date",         // ID поля вводу дати
      		ifFormat    : "<%= Bean.getJSPDateFormat()%>",    // формат дати (23.03.2008)
      		button      : "btn_operation_date"       // ID кнопки для меню вибору дати
    	});
  		</script>
  		<script type="text/javascript">
    	Calendar.setup({
			inputField  : "id_conversion_date",         // ID поля вводу дати
      		ifFormat    : "<%= Bean.getJSPDateFormat()%>",    // формат дати (23.03.2008)
      		button      : "btn_conversion_date"       // ID кнопки для меню вибору дати
    	});
	</script>

	<% } else if (action.equalsIgnoreCase("edit")) { 
	
		bcPostingEditObject posting = new bcPostingEditObject();
		
	%>

	<%= Bean.getOperationTitle(
		Bean.remittanceXML.getfieldTransl("LAB_EDIT_POSTING", false),
		"Y",
		"Y") 
	%>
	<form action="../crm/finance/remittanceupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">
        <input type="hidden" name="type" value="posting">
        <input type="hidden" name="action" value="edit">
        <input type="hidden" name="process" value="yes">
        <input type="hidden" name="id" value="<%=id %>">
	<table <%=Bean.getTableDetailParam() %>>

			<%=posting.getPostingEditHTML(id_posting_detail, Bean.getDateFormatTitle()) %>

		<tr>
			<td colspan="6" align="center">
				<%=Bean.getSubmitButtonAjax("../crm/finance/remittanceupdate.jsp") %>
				<%=Bean.getResetButton() %>
				<%=Bean.getGoBackButton("../crm/finance/remittancespecs.jsp?id=" + id) %>
			</td>
		</tr>
	</table>
	</form>	
	<script type="text/javascript">
  		Calendar.setup({
			inputField  : "id_operation_date",         // ID поля вводу дати
      		ifFormat    : "<%= Bean.getJSPDateFormat()%>",    // формат дати (23.03.2008)
      		button      : "btn_operation_date"       // ID кнопки для меню вибору дати
    	});
  		</script>
  		<script type="text/javascript">
    	Calendar.setup({
			inputField  : "id_conversion_date",         // ID поля вводу дати
      		ifFormat    : "<%= Bean.getJSPDateFormat()%>",    // формат дати (23.03.2008)
      		button      : "btn_conversion_date"       // ID кнопки для меню вибору дати
    	});
	</script>
		<%
		} else { %> 
			<%= Bean.getUnknownActionText(action) %><% 
		}
		
	} else if (process.equalsIgnoreCase("yes")) {
		String
			operation_date				= Bean.getDecodeParam(parameters.get("operation_date")),
			debet_id_bk_account			= Bean.getDecodeParam(parameters.get("debet_id_bk_account")),
			cd_currency					= Bean.getDecodeParam(parameters.get("cd_currency")),
			credit_id_bk_account		= Bean.getDecodeParam(parameters.get("credit_id_bk_account")),
			entered_amount				= Bean.getDecodeParam(parameters.get("entered_amount")),
			assignment_posting			= Bean.getDecodeParam(parameters.get("assignment_posting")),
			base_currency				= Bean.getDecodeParam(parameters.get("base_currency")),
			exchange_rate				= Bean.getDecodeParam(parameters.get("exchange_rate")),
			id_bk_operation_scheme_line	= Bean.getDecodeParam(parameters.get("id_bk_operation_scheme_line")),
			conversion_date				= Bean.getDecodeParam(parameters.get("conversion_date")),
			id_club_rel					= Bean.getDecodeParam(parameters.get("id_club_rel")),
			accounted_amount			= Bean.getDecodeParam(parameters.get("accounted_amount")),
			using_in_clearing			= Bean.getDecodeParam(parameters.get("using_in_clearing")),
			run_postings_export			= Bean.getDecodeParam(parameters.get("run_postings_export")),
			cd_bk_doc_type				= Bean.getDecodeParam(parameters.get("cd_bk_doc_type")),
			id_bank_account_debet		= Bean.getDecodeParam(parameters.get("id_bank_account_debet")),
			id_bank_account_credit		= Bean.getDecodeParam(parameters.get("id_bank_account_credit")),
			payment_function			= Bean.getDecodeParam(parameters.get("payment_function"));

		if (action.equalsIgnoreCase("run")) { 
			
			String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_REMITTANCE.run_posting(?,?,?,?)}";

			String[] pParam = new String [2];

			pParam[0] = id_club;
			pParam[1] = id;
			
	 		%>
			<%= Bean.getCallResultParam("RUN", callSQL, pParam, "../crm/finance/remittancespecs.jsp?id=" + id + "&id_report=", "") %>
			<%
			
		} else if (action.equalsIgnoreCase("run_all")) { 
			
			String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_REMITTANCE.run_posting_all(?,?,?)}";

			String[] pParam = new String [1];

			pParam[0] = id_club;
			
	 		%>
			<%= Bean.getCallResultParam("RUN", callSQL, pParam, "../crm/finance/remittancespecs.jsp?id=" + id + "&id_report=", "") %>
			<%
			
		} else if (action.equalsIgnoreCase("add")) { 
		       
	       	String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_REMITTANCE.add_posting("+
	       		"?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";

	    	String[] pParam = new String [21];

	    	pParam[0] = id;
	    	pParam[1] = debet_id_bk_account;
	    	pParam[2] = credit_id_bk_account;
	    	pParam[3] = cd_currency;
	    	pParam[4] = operation_date;
	    	pParam[5] = entered_amount;
	    	pParam[6] = base_currency;
	    	pParam[7] = exchange_rate;
	    	pParam[8] = conversion_date;
	    	pParam[9] = accounted_amount;
	    	pParam[10] = assignment_posting;
	    	pParam[11] = id_bk_operation_scheme_line;
	    	pParam[12] = id_club_rel;
	    	pParam[13] = using_in_clearing;
	    	pParam[14] = run_postings_export;
	    	pParam[15] = cd_bk_doc_type;
	    	pParam[16] = id_bank_account_debet;
	    	pParam[17] = id_bank_account_credit;
	    	pParam[18] = payment_function;
	    	pParam[19] = id_club;
	    	pParam[20] = Bean.getDateFormat();
	
		 	%>
			<%= Bean.getCallResultParam("INSERT", callSQL, pParam, "../crm/finance/remittancespecs.jsp?id=" + id + "&id_posting_detail=", "../crm/finance/remittancespecs.jsp?id=" + id) %>
			<% 	
			
		} else if (action.equalsIgnoreCase("edit")) { 
		       
	       	String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_REMITTANCE.update_posting("+
	       		"?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";

	   	  	String[] pParam = new String [20];

	    	pParam[0] = id_posting_detail;
	    	pParam[1] = debet_id_bk_account;
	    	pParam[2] = credit_id_bk_account;
	    	pParam[3] = cd_currency;
	    	pParam[4] = operation_date;
	    	pParam[5] = entered_amount;
	    	pParam[6] = base_currency;
	    	pParam[7] = exchange_rate;
	    	pParam[8] = conversion_date;
	    	pParam[9] = accounted_amount;
	    	pParam[10] = assignment_posting;
	    	pParam[11] = id_bk_operation_scheme_line;
	    	pParam[12] = id_club_rel;
	    	pParam[13] = using_in_clearing;
	    	pParam[14] = run_postings_export;
	    	pParam[15] = cd_bk_doc_type;
	    	pParam[16] = id_bank_account_debet;
	    	pParam[17] = id_bank_account_credit;
	    	pParam[18] = payment_function;
	    	pParam[19] = Bean.getDateFormat();
	
		 	%>
			<%= Bean.getCallResultParam("UPDATE", callSQL, pParam, "../crm/finance/remittancespecs.jsp?id=" + id, "") %>
			<% 	
			
		} else if (action.equalsIgnoreCase("delete")) { 
		       
	       	String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_REMITTANCE.delete_posting(?,?)}";

			String[] pParam = new String [1];

			pParam[0] = id_posting_detail;
	
		 	%>
			<%= Bean.getCallResultParam("DELETE", callSQL, pParam, "../crm/finance/remittancespecs.jsp?id=" + id, "") %>
			<% 	
			
	    } else if (action.equalsIgnoreCase("deleteall")) { 
			
			String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_REMITTANCE.delete_all_remit_posting(?,?)}";

			String[] pParam = new String [1];

			pParam[0] = id;
			
	 		%>
			<%= Bean.getCallResultParam("DELETE", callSQL, pParam, "../crm/finance/remittancespecs.jsp?id=" + id, "") %>
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
	
<%@page import="bc.objects.bcPostingDetailObject"%>
<%@page import="java.util.HashMap"%>
<%@page import="bc.objects.bcClubShortObject"%></html>
