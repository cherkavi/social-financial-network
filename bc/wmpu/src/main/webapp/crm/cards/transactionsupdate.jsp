<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%@page import="bc.objects.bcTransactionObject"%>

<%= Bean.getLogOutScript(request) %>

<%@page import="bc.objects.bcPostingEditObject"%><html>
<head>
	<%= Bean.getMetaContent() %>
	<%= Bean.getBottomFrameCSS() %>

<%
HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String pageFormName = "CARDS_TRANSACTIONS";

Bean.setJspPageForTabName(pageFormName);

request.setCharacterEncoding("UTF-8");

String type			= Bean.getDecodeParam(parameters.get("type"));
String id			= Bean.getDecodeParam(parameters.get("id")); 
String action		= Bean.getDecodeParam(parameters.get("action")); 
String process		= Bean.getDecodeParam(parameters.get("process"));
String debug		= Bean.getDecodeParam(parameters.get("debug"));
String id_club	= Bean.getDecodeParam(parameters.get("id_club"));

if (id==null || ("".equalsIgnoreCase(id))) id="";
if (type==null || ("".equalsIgnoreCase(type))) type="";
if (action==null || ("".equalsIgnoreCase(action))) action="";
if (process==null || ("".equalsIgnoreCase(process))) process="";
if (id_club==null || ("".equalsIgnoreCase(id_club))) id_club="";


%>
</head>
<body topmargin="0">

<%
if (type.equalsIgnoreCase("rejection")) {
  	if (process.equalsIgnoreCase("yes")) {
  	   	if (action.equalsIgnoreCase("start")) {     
  		    
  		    String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK$TRANS_UI.start_trans_rejection(?,?)}";

  			String[] pParam = new String [1];
  					
  			pParam[0] = id;
  		
		 	%>
			<%= Bean.getCallResultParam("UPDATE", callSQL, pParam, "../crm/cards/transactionspecs.jsp?id=" + id, "") %>
			<% 	

  	   	} else if (action.equalsIgnoreCase("revert")) { 
   		
	    	String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK$TRANS_UI.revert_trans_rejection(?,?)}";

  			String[] pParam = new String [1];
  					
  			pParam[0] = id;

		 	%>
			<%= Bean.getCallResultParam("UPDATE", callSQL, pParam, "../crm/cards/transactionspecs.jsp?id=" + id, "") %>
			<% 	

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
		
			bcTransactionObject trans = new bcTransactionObject(id);
    		
			bcPostingEditObject posting = new bcPostingEditObject();
			
		%>

		<%= Bean.getOperationTitle(
			Bean.transactionXML.getfieldTransl("LAB_ADD_POSTING", false),
			"Y",
			"Y") 
		%>
		<form action="../crm/cards/transactionsupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">
	        <input type="hidden" name="type" value="posting">
	        <input type="hidden" name="action" value="add">
	        <input type="hidden" name="process" value="yes">
	        <input type="hidden" name="id" value="<%=id %>">
        <input type="hidden" name="id_club" value="<%=id_club %>">
		<table <%=Bean.getTableDetailParam() %>>

			<%=posting.getPostingAddHTML(trans.getValue("CD_CURRENCY"), Bean.getDateFormatTitle()) %>

			<tr>
				<td colspan="6" align="center">
					<%=Bean.getSubmitButtonAjax("../crm/cards/transactionsupdate.jsp") %>
					<%=Bean.getResetButton() %>
					<%=Bean.getGoBackButton("../crm/cards/transactionspecs.jsp?id=" + id) %>
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
			Bean.transactionXML.getfieldTransl("LAB_EDIT_POSTING", false),
			"Y",
			"Y") 
		%>
		<form action="../crm/cards/transactionsupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">
	        <input type="hidden" name="type" value="posting">
	        <input type="hidden" name="action" value="edit">
	        <input type="hidden" name="process" value="yes">
	        <input type="hidden" name="id" value="<%=id %>">
		<table <%=Bean.getTableDetailParam() %>>

			<%=posting.getPostingEditHTML(id_posting_detail, Bean.getDateFormatTitle()) %>

			<tr>
				<td colspan="6" align="center">
					<%=Bean.getSubmitButtonAjax("../crm/cards/transactionsupdate.jsp") %>
					<%=Bean.getResetButton() %>
					<%=Bean.getGoBackButton("../crm/cards/transactionspecs.jsp?id=" + id) %>
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
			payment_function			= Bean.getDecodeParam(parameters.get("payment_function"));
		
		if (action.equalsIgnoreCase("run")) { %>
    	
	       <%
	       	String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK$TRANS_UI.run_posting(?,?,?,?)}";

 	       	String[] pParam = new String [2];
 	       			
 	       	pParam[0] = id_club;
 	       	pParam[1] = id;
	        %>
			<%= Bean.getCallResultParam("RUN", callSQL, pParam, "../crm/cards/transactionspecs.jsp?id=" + id+ "&id_report=", "") %>
			<% 	
		} else if (action.equalsIgnoreCase("run_all")) { %>
    	
	       <%
	       	String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK$TRANS_UI.run_posting_all(?,?,?)}";

	     	String[] pParam = new String [1];
	     	       			
	     	pParam[0] = id_club;
	        %>
			<%= Bean.getCallResultParam("RUN", callSQL, pParam, "../crm/cards/transactionspecs.jsp?id=" + id+ "&id_report=", "") %>
			<% 	
		} else if (action.equalsIgnoreCase("cancel")) { %>
    	
	       <%
	       	String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK$TRANS_UI.cancel_posting(?,?,?)}";

	     	String[] pParam = new String [1];
	     	       			
	     	pParam[0] = id;
	        %>
			<%= Bean.getCallResultParam("RUN", callSQL, pParam, "../crm/cards/transactionspecs.jsp?id=" + id+ "&id_report=", "") %>
			<% 	
		} else if (action.equalsIgnoreCase("add")) { 
		       
	       	String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK$TRANS_UI.add_posting('"+
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
			<%= Bean.getCallResultParam("INSERT", callSQL, pParam, "../crm/cards/transactionspecs.jsp?id=" + id + "&id_posting_detail=", "") %>
			<% 	
			
		} else if (action.equalsIgnoreCase("edit")) { 
			
	       	 String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK$TRANS_UI.update_posting('"+
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
			<%= Bean.getCallResultParam("UPDATE", callSQL, pParam, "../crm/cards/transactionspecs.jsp?id=" + id, "") %>
			<% 	
			
		} else if (action.equalsIgnoreCase("remove")) { 
		       
	       	String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK$TRANS_UI.delete_posting(?,?)}";

			String[] pParam = new String [1];
				
			pParam[0] = id_posting_detail;
	
		 	%>
			<%= Bean.getCallResultParam("DELETE", callSQL, pParam, "../crm/cards/transactionspecs.jsp?id=" + id, "") %>
			<% 	
			
		} else if (action.equalsIgnoreCase("deleteall")) { 
	       
	       	String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK$TRANS_UI.delete_all_trans_posting(?,?)}";

			String[] pParam = new String [1];
				
			pParam[0] = id;
	
		 	%>
			<%= Bean.getCallResultParam("DELETE", callSQL, pParam, "../crm/cards/transactionspecs.jsp?id=" + id, "") %>
			<% 	
			
		} else { %> 
			<%= Bean.getUnknownActionText(action) %><% 
		}
	} else {%> 
		<%= Bean.getUnknownProcessText(process) %> <%
	}
} else if (type.equalsIgnoreCase("set_state")) {
	if (process.equalsIgnoreCase("yes")) {
		if (action.equalsIgnoreCase("start")) { 
	       
	       	String id_state		= Bean.getDecodeParam(parameters.get("id_state"));
	       
	       	String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK$TRANS_UI.set_trans_state(?,?,?)}";

			String[] pParam = new String [2];
				
			pParam[0] = id;
			pParam[1] = id_state;
	
		 	%>
			<%= Bean.getCallResultParam("UPDATE", callSQL, pParam, "../crm/cards/transactionspecs.jsp?id=" + id , "") %>
			<% 	

		} else { %> 
			<%= Bean.getUnknownActionText(action) %><% 
		}
	} else {%> 
		<%= Bean.getUnknownProcessText(process) %> <%
	}
} else {%> 
	<%= Bean.getUnknownTypeText(type) %><%
}
%>
</body>


<%@page import="bc.objects.bcPostingDetailObject"%>
<%@page import="java.util.HashMap"%>
<%@page import="bc.objects.bcClubShortObject"%></html>