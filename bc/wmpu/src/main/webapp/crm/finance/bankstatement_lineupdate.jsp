<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />	

<%@page import="bc.objects.bcLoySheduleObject"%>
<%@page import="bc.objects.bcLoySheduleLineObject"%>
<%@page import="bc.objects.bcBankStatementHeaderObject"%>
<%@page import="bc.objects.bcBankStatementLineObject"%>
<%@page import="java.util.HashMap"%>
<%@page import="bc.objects.bcClubShortObject"%>
<%@page import="bc.objects.bcPostingDetailObject"%>
<%@page import="bc.objects.bcPostingEditObject"%><html>

<%= Bean.getLogOutScript(request) %>

<head>
	<%= Bean.getMetaContent() %>
	<%= Bean.getBottomFrameCSS() %>
</head>

<body>
<%
HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String pageFormName = "FINANCE_BANKSTATEMENT";

Bean.setJspPageForTabName(pageFormName);

request.setCharacterEncoding("UTF-8");

String id		= Bean.getDecodeParam(parameters.get("id")); 
String type		= Bean.getDecodeParam(parameters.get("type"));
String action	= Bean.getDecodeParam(parameters.get("action")); 
String process	= Bean.getDecodeParam(parameters.get("process"));

if (id==null || ("".equalsIgnoreCase(id))) id="empty";
if (type==null || ("".equalsIgnoreCase(type))) type="empty";
if (action==null || ("".equalsIgnoreCase(action))) action="empty";
if (process==null || ("".equalsIgnoreCase(process))) process="empty";

if (type.equalsIgnoreCase("general")) {

	if (process.equalsIgnoreCase("yes"))	{
		      
		String
			id_bank_statement_line 	= Bean.getDecodeParam(parameters.get("id_bank_statement_line")), 
			id_bank_statement	 	= Bean.getDecodeParam(parameters.get("id_bank_statement")), 
		    line_number 			= Bean.getDecodeParam(parameters.get("line_number")), 
		    corr_id_bank_account 	= Bean.getDecodeParam(parameters.get("id_bank_account_correspondent")), 
		    name_bank_account_correspondent 	= Bean.getDecodeParam(parameters.get("name_bank_account_correspondent")), 
		    corr_inn_number 				= Bean.getDecodeParam(parameters.get("corr_inn_number")), 
		    corr_name 				= Bean.getDecodeParam(parameters.get("corr_name")), 
		    mfo_bank_correspondent 	= Bean.getDecodeParam(parameters.get("mfo_bank_correspondent")), 
		    name_bank_correspondent = Bean.getDecodeParam(parameters.get("name_bank_correspondent")), 
		    operation_code 			= Bean.getDecodeParam(parameters.get("operation_code")),
		    operation_date 			= Bean.getDecodeParam(parameters.get("operation_date")), 
		    debet_amount 			= Bean.getDecodeParam(parameters.get("debet_amount")), 
		    credit_amount 			= Bean.getDecodeParam(parameters.get("credit_amount")), 
		    doc_number 				= Bean.getDecodeParam(parameters.get("doc_number")),
		    doc_date 				= Bean.getDecodeParam(parameters.get("doc_date")), 
		    assignment 				= Bean.getDecodeParam(parameters.get("assignment")), 
		    reconcile_state			= Bean.getDecodeParam(parameters.get("reconcile_state")),
		    need_bs_line_import 	= Bean.getDecodeParam(parameters.get("need_bs_line_import")), 
		    
		    imported 				= Bean.getDecodeParam(parameters.get("imported"));
		      
		if (action.equalsIgnoreCase("remove")) { 
			
			String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_BANK_STATEMENT.delete_bank_statement_line(?,?)}";

			String[] pParam = new String [1];

			pParam[0] = id;

   		 	%>
   			<%= Bean.getCallResultParam("DELETE", callSQL, pParam, "../crm/finance/bankstatement.jsp", "") %>
   			<% 	

		} else if (action.equalsIgnoreCase("edit")) { 
			
			String callSQL = "";
 			if ("yes".equalsIgnoreCase(imported)) { 
 				callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_BANK_STATEMENT.update_bank_statement_line("+
					"?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";

				String[] pParam = new String [13];

				pParam[0] = id_bank_statement;
				pParam[1] = id;
				pParam[2] = line_number;
				pParam[3] = corr_id_bank_account;
				pParam[4] = operation_code;
				pParam[5] = operation_date;
				pParam[6] = debet_amount;
				pParam[7] = credit_amount;
				pParam[8] = doc_number;
				pParam[9] = doc_date;
				pParam[10] = assignment;
				pParam[11] = reconcile_state;
				pParam[12] = Bean.getDateFormat();
				
 	   		 	%>
 	   			<%= Bean.getCallResultParam("UPDATE", callSQL, pParam, "../crm/finance/bankstatement_linespecs.jsp?id=" + id, "") %>
 	   			<% 	
 			} else {
	 			callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_BANK_STATEMENT.update_bank_statement_ln_src("+
					"?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";

				String[] pParam = new String [18];

				pParam[0] = id_bank_statement;
				pParam[1] = id;
				pParam[2] = line_number;
				pParam[3] = mfo_bank_correspondent;
				pParam[4] = name_bank_correspondent;
				pParam[5] = name_bank_account_correspondent;
				pParam[6] = corr_inn_number;
				pParam[7] = corr_name;
				pParam[8] = operation_code;
				pParam[9] = operation_date;
				pParam[10] = debet_amount;
				pParam[11] = credit_amount;
				pParam[12] = doc_number;
				pParam[13] = doc_date;
				pParam[14] = assignment;
				pParam[15] = reconcile_state;
				pParam[16] = need_bs_line_import;
				pParam[17] = Bean.getDateFormat();
	   		 	%>
	   			<%= Bean.getCallResultParam("UPDATE", callSQL, pParam, "../crm/finance/bankstatement_linespecs.jsp?id=" + id, "") %>
	   			<% 	
 			}

		} else { %> 
			<%= Bean.getUnknownActionText(action) %><% 
		}
	} else {%> 
		<%= Bean.getUnknownProcessText(process) %> <%
	}
} else if (type.equalsIgnoreCase("posting")) {%>
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
			document.getElementById('span_cd_bk_doc_type').innerHTML='<%= Bean.posting_schemeXML.getfieldTransl("cd_bk_doc_type", true) %>';
		} else {
			document.getElementById('span_cd_bk_doc_type').innerHTML='<%= Bean.posting_schemeXML.getfieldTransl("cd_bk_doc_type", false) %>';
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
	
		bcBankStatementLineObject line = new bcBankStatementLineObject(id);
		
        bcPostingEditObject posting = new bcPostingEditObject();
        
        bcClubShortObject club = new bcClubShortObject(line.getValue("ID_CLUB"));
		
	%>

	<%= Bean.getOperationTitle(
		Bean.postingXML.getfieldTransl("h_postings_add", false),
		"Y",
		"Y") 
	%>
	<form action="../crm/finance/bankstatement_lineupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">
        <input type="hidden" name="type" value="posting">
        <input type="hidden" name="action" value="add">
        <input type="hidden" name="process" value="yes">
        <input type="hidden" name="id" value="<%=id %>">
    	<input type="hidden" name="id_club" value="<%=line.getValue("ID_CLUB") %>">
	<table <%=Bean.getTableDetailParam() %>>

		<%=posting.getPostingAddHTML(club.getValue("CD_CURRENCY_BASE"), Bean.getDateFormatTitle()) %>

		<tr>
			<td colspan="6" align="center">
				<%=Bean.getSubmitButtonAjax("../crm/finance/bankstatement_lineupdate.jsp") %>
				<%=Bean.getResetButton() %>
				<%=Bean.getGoBackButton("../crm/finance/bankstatement_linespecs.jsp?id=" + id) %>
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
		Bean.postingXML.getfieldTransl("h_postings_edit", false),
		"Y",
		"Y") 
	%>
	<form action="../crm/finance/bankstatement_lineupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">
        <input type="hidden" name="type" value="posting">
        <input type="hidden" name="action" value="edit">
        <input type="hidden" name="process" value="yes">
        <input type="hidden" name="id" value="<%=id %>">
	<table <%=Bean.getTableDetailParam() %>>

		<%=posting.getPostingEditHTML(id_posting_detail, Bean.getDateFormatTitle()) %>

		<tr>
			<td colspan="6" align="center">
				<%=Bean.getSubmitButtonAjax("../crm/finance/bankstatement_lineupdate.jsp") %>
				<%=Bean.getResetButton() %>
				<%=Bean.getGoBackButton("../crm/finance/bankstatement_linespecs.jsp?id=" + id) %>
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

	<% } else { %> 
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
		payment_function			= Bean.getDecodeParam(parameters.get("payment_function")),
		id_club						= Bean.getDecodeParam(parameters.get("id_club"));
	
	if (action.equalsIgnoreCase("run")) { %>
	
       <%
       	String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_BANK_STATEMENT.run_posting(?,?,?,?)}";

	    String[] pParam = new String [2];

	    pParam[0] = id_club;
	    pParam[1] = id;
	    
        %>
		<%= Bean.getCallResultParam("RUN", callSQL, pParam, "../crm/finance/bankstatement_linespecs.jsp?id=" + id+ "&id_report=", "") %>
		<% 	 	
	} else if (action.equalsIgnoreCase("add")) { 
	       
       	String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_BANK_STATEMENT.add_posting("+
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
		<%= Bean.getCallResultParam("INSERT", callSQL, pParam, "../crm/finance/bankstatement_linespecs.jsp?id=" + id + "&id_posting_detail=", "") %>
		<% 	
		
	} else if (action.equalsIgnoreCase("edit")) { 
	       
       	String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_BANK_STATEMENT.update_posting("+
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
		<%= Bean.getCallResultParam("UPDATE", callSQL, pParam, "../crm/finance/bankstatement_linespecs.jsp?id=" + id, "") %>
		<% 	
		
	} else if (action.equalsIgnoreCase("remove")) { 
	       
       	String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_BANK_STATEMENT.delete_posting(?,?)}";

		String[] pParam = new String [1];

		pParam[0] = id_posting_detail;

	 	%>
		<%= Bean.getCallResultParam("DELETE", callSQL, pParam, "../crm/finance/bankstatement_linespecs.jsp?id=" + id, "") %>
		<% 	
		
	} else if (action.equalsIgnoreCase("deleteall")) { 
       
       	String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_BANK_STATEMENT.delete_all_bs_line_posting(?,?)}";

		String[] pParam = new String [1];

		pParam[0] = id;

	 	%>
		<%= Bean.getCallResultParam("DELETE", callSQL, pParam, "../crm/finance/bankstatement_linespecs.jsp?id=" + id, "") %>
		<% 	
		
	} else { %> 
		<%= Bean.getUnknownActionText(action) %><% 
	}
} else {%> 
	<%= Bean.getUnknownProcessText(process) %> <%
}
} else {%> 
	<%= Bean.getUnknownTypeText(type) %> <%
}
	
%>


</body>
</html>
