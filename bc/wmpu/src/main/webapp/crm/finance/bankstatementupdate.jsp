<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />	

<%@page import="bc.objects.bcLoySheduleObject"%>
<%@page import="bc.objects.bcLoySheduleLineObject"%>
<%@page import="bc.objects.bcBankStatementHeaderObject"%>
<%@page import="bc.objects.bcBankStatementLineObject"%>
<%@page import="java.util.HashMap"%>
<%@page import="bc.objects.bcClubShortObject"%>
<%@page import="bc.objects.bcPostingDetailObject"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.Set"%>
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
String line		= Bean.getDecodeParam(parameters.get("line"));
String type		= Bean.getDecodeParam(parameters.get("type"));
String action	= Bean.getDecodeParam(parameters.get("action")); 
String process	= Bean.getDecodeParam(parameters.get("process"));

if (id==null || ("".equalsIgnoreCase(id))) id="empty";
if (type==null || ("".equalsIgnoreCase(type))) type="empty";
if (action==null || ("".equalsIgnoreCase(action))) action="empty";
if (process==null || ("".equalsIgnoreCase(process))) process="empty";

if (type.equalsIgnoreCase("general")) {
  if (process.equalsIgnoreCase("no"))
/* вибираємо тип дії (добавити, видалити...)*/
	{
	   /*  --- Добавити запис --- */
    	if (action.equalsIgnoreCase("add") || action.equalsIgnoreCase("add2")) {

    		bcClubShortObject club = new bcClubShortObject(Bean.getCurrentClubID());
    		%> 
	<script>
		var formData = new Array (
			new Array ('date_bank_statement', 'varchar2', 1),
			new Array ('name_club', 'varchar2', 1),
			new Array ('name_bank_account_client', 'varchar2', 1)
		);
	</script>

		<%= Bean.getOperationTitle(
				Bean.bank_statementXML.getfieldTransl("h_add_header", false),
				"Y",
				"Y") 
		%>
		 <form action="../crm/finance/bankstatementupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">
	        <input type="hidden" name="type" value="general">
	        <input type="hidden" name="action" value="add">
	        <input type="hidden" name="process" value="yes">
		<table <%=Bean.getTableDetailParam() %>>
			<tr>
				<td><%= Bean.bank_statementXML.getfieldTransl("id_bank_statement", false) %> </td><td><input type="text" name="id_bank_statement" size="15" value="" readonly="readonly" class="inputfield-ro"></td>
				<td><%= Bean.clubXML.getfieldTransl("club", true) %>
					<%= Bean.getGoToClubLink(club.getValue("ID_CLUB")) %>
			  	</td>
			  	<td>
					<%=Bean.getWindowFindClub("club", club.getValue("ID_CLUB"), club.getValue("SNAME_CLUB"), "25") %>
			  	</td>
			</tr>
			<tr>
				<td><%= Bean.bank_statementXML.getfieldTransl("number_bank_statement", false) %></td> <td><input type="text" name="number_bank_statement" size="15" value="" class="inputfield"> </td>
				<td><%= Bean.bank_statementXML.getfieldTransl("number_bank_account_client", true) %></td>
				<td>
					<%=Bean.getWindowFindBankAccount("bank_account_client", "", "N", "_client", "70") %>
				</td>			
			</tr>
			<tr>
				<td><%=Bean.bank_statementXML.getfieldTransl("date_bank_statement", true)%></td> <td><%=Bean.getCalendarInputField("date_bank_statement", Bean.getSysDate(), "10") %></td>
				<td><%= Bean.bank_statementXML.getfieldTransl("name_jur_prs_client", false) %></td>
				<td>
					<input type="hidden" name="id_jur_prs_client" id="id_jur_prs_client" size="70" value="" readonly="readonly" class="inputfield-ro"> 
					<input type="text" name="name_jur_prs_client" id="name_jur_prs_client" size="70" value="" readonly="readonly" class="inputfield-ro"> 
				</td>			
			</tr>
			<tr>
				<td>&nbsp;</td><td>&nbsp;</td>
				<td><%= Bean.bank_statementXML.getfieldTransl("name_bank_branch_client", false) %></td>
				<td>
					<input type="hidden" id="id_bank" name="id_bank" value="" readonly="readonly" class="inputfield">
					<input type="text" id="name_bank" name="name_bank" size="70" value="" readonly="readonly" class="inputfield-ro">
				</td>			
			</tr>
			<tr>
				<td>&nbsp;</td><td>&nbsp;</td>
				<td>&nbsp;</td><td>&nbsp;</td>
			</tr>
			<tr>
				<td>&nbsp;</td><td>&nbsp;</td>
				<td><%= Bean.bank_statementXML.getfieldTransl("cd_currency_client", false) %></td>
					<td><input type="hidden" id="cd_currency" name="cd_currency" value="">
					<input type="text" id="name_currency" name="name_currency" size="20" value="" readonly="readonly" class="inputfield-ro"></td>
			</tr>
			<tr>
				<td>&nbsp;</td><td>&nbsp;</td>
				<td><%= Bean.bank_statementXML.getfieldTransl("begin_balance", false) %></td><td><input type="text" name="begin_balance" size="15" value="" class="inputfield"></td>
			</tr>
			<tr>
				<td>&nbsp;</td><td>&nbsp;</td>
				<td><%= Bean.bank_statementXML.getfieldTransl("end_balance", false) %></td><td><input type="text" name="end_balance" size="15" value="" class="inputfield"></td>
			</tr>
		<tr>
			<td colspan="6" align="center">
				<%=Bean.getSubmitButtonAjax("../crm/finance/bankstatementupdate.jsp") %>
				<%=Bean.getResetButton() %>
				<% if ("add".equalsIgnoreCase(action)) { %>
				<%=Bean.getGoBackButton("../crm/finance/bankstatement.jsp") %>
				<% } else { %>
					<%=Bean.getGoBackButton("../crm/finance/bankstatementspecs.jsp?id=" + id) %>
				<% } %>
			</td>
		</tr>
		</table>
		</form>
	
	<!-- Скрипт для втавки меню вибору дати -->
	<%= Bean.getCalendarScript("date_bank_statement", false) %>


	        <%
    	} else {
    	    %> <%= Bean.getUnknownActionText(action) %><%
    	}
	} else if (process.equalsIgnoreCase("yes"))	{
    
		String
			imported 					= Bean.getDecodeParam(parameters.get("imported")),
    		
    		id_bank_statement 			= Bean.getDecodeParam(parameters.get("id_bank_statement")),
    		number_bank_statement 		= Bean.getDecodeParam(parameters.get("number_bank_statement")),
    		date_bank_statement 		= Bean.getDecodeParam(parameters.get("date_bank_statement")),
    		id_bank_account 			= Bean.getDecodeParam(parameters.get("id_bank_account_client")),
    		begin_balance 				= Bean.getDecodeParam(parameters.get("begin_balance")), 
    		end_balance 				= Bean.getDecodeParam(parameters.get("end_balance")), 
    		debet_total 				= Bean.getDecodeParam(parameters.get("debet_total")),
    		credit_total 				= Bean.getDecodeParam(parameters.get("credit_total")), 
    		line_count 					= Bean.getDecodeParam(parameters.get("line_count")),
    		debet_line_count 			= Bean.getDecodeParam(parameters.get("debet_line_count")),
    		credit_line_count 			= Bean.getDecodeParam(parameters.get("credit_line_count")),
    		id_club			 			= Bean.getDecodeParam(parameters.get("id_club")),
    		client_bank_mfo 			= Bean.getDecodeParam(parameters.get("src_client_mfo_bank")), 
			client_bank_name 			= Bean.getDecodeParam(parameters.get("src_client_name_bank")), 
			client_bank_account_number 	= Bean.getDecodeParam(parameters.get("src_client_number_bank_account")), 
			client_inn_number			= Bean.getDecodeParam(parameters.get("src_client_inn_number")), 
			client_name 				= Bean.getDecodeParam(parameters.get("src_client_name")), 
			currency_code 				= Bean.getDecodeParam(parameters.get("src_currency_code"));
    
 		if (action.equalsIgnoreCase("add")) { 
 			String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_BANK_STATEMENT.add_bank_statement_header("+
				"?,?,?,?,?,?,?,?,?)}";

    		String[] pParam = new String [7];

    		pParam[0] = number_bank_statement;
    		pParam[1] = date_bank_statement;
    		pParam[2] = id_bank_account;
    		pParam[3] = begin_balance;
    		pParam[4] = end_balance;
    		pParam[5] = id_club;
    		pParam[6] = Bean.getDateFormat();

   		 	%>
   			<%= Bean.getCallResultParam("INSERT", callSQL, pParam, "../crm/finance/bankstatementspecs.jsp?id=" , "../crm/finance/bankstatement.jsp") %>
   			<% 	

 		} else if (action.equalsIgnoreCase("remove")) { 
 			
 			String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_BANK_STATEMENT.delete_bank_statement_header(?,?)}";

 			String[] pParam = new String [1];

 			pParam[0] = id;

   		 	%>
   			<%= Bean.getCallResultParam("DELETE", callSQL, pParam, "../crm/finance/bankstatement.jsp" , "") %>
   			<% 	

		} else if (action.equalsIgnoreCase("remove_line")) { 
			
			String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_BANK_STATEMENT.delete_bank_statement_line(?,?)}";

	 		String[] pParam = new String [1];

	 		pParam[0] = line;

   		 	%>
   			<%= Bean.getCallResultParam("DELETE", callSQL, pParam, "../crm/finance/bankstatementspecs.jsp?id=" + id, "") %>
   			<% 	

		} else if (action.equalsIgnoreCase("edit")) { 
 			String callSQL = "";
 			if ("yes".equalsIgnoreCase(imported)) { 
 				callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_BANK_STATEMENT.update_bank_statement_header("+
 					",?,?,?,?,?,?,?)}";

 	    		String[] pParam = new String [7];

 	    		pParam[0] = id;
 	    		pParam[1] = number_bank_statement;
 	    		pParam[2] = date_bank_statement;
 	    		pParam[3] = id_bank_account;
 	    		pParam[4] = begin_balance;
 	    		pParam[5] = end_balance;
 	    		pParam[6] = Bean.getDateFormat();
 	   		 	%>
 	   			<%= Bean.getCallResultParam("UPDATE", callSQL, pParam, "../crm/finance/bankstatementspecs.jsp?id=" + id, "") %>
 	   			<% 	
 			} else {
	 			callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_BANK_STATEMENT.update_bank_statement_hd_src("+
	 				"?,?,?,?,'?,?,?,?,?,?,?,?,?,?)}";

	    		String[] pParam = new String [13];

	    		pParam[0] = id;
	    		pParam[1] = number_bank_statement;
	    		pParam[2] = date_bank_statement;
	    		pParam[3] = client_bank_mfo;
	    		pParam[4] = client_bank_name;
	    		pParam[5] = client_bank_account_number;
	    		pParam[6] = client_inn_number;
	    		pParam[7] = client_name;
	    		pParam[8] = currency_code;
	    		pParam[9] = begin_balance;
	    		pParam[10] = end_balance;
	    		pParam[11] = id_club;
	    		pParam[12] = Bean.getDateFormat();
	   		 	%>
	   			<%= Bean.getCallResultParam("UPDATE", callSQL, pParam, "../crm/finance/bankstatementspecs.jsp?id=" + id, "") %>
	   			<% 	
 			}

 		} else { %> 
 			<%= Bean.getUnknownActionText(action) %><% 
 		}
	} else {%> 
		<%= Bean.getUnknownProcessText(process) %> <%
  	}

//БЛОК РАБОТЫ СО СТРОКАМИ
} else if (type.equalsIgnoreCase("line")) {
	if (process.equalsIgnoreCase("no")) {
%>
	<script>
		var formData = new Array (
			new Array ('line_number', 'integer', 1),
			new Array ('operation_date', 'varchar2', 1),
			new Array ('name_bank_account_correspondent', 'varchar2', 1)
		);
	</script>

	<%
		if (action.equalsIgnoreCase("add")) { %> 
			
			<%= Bean.getOperationTitle(
					Bean.bank_statementXML.getfieldTransl("h_add_line", false),
					"Y",
					"Y") 
			%>

  	        <form action="../crm/finance/bankstatementupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">
	  	        <input type="hidden" name="type" value="line">
	  	        <input type="hidden" name="action" value="add">
	  	        <input type="hidden" name="process" value="yes">
	  	        <input type="hidden" name="id" value="<%= id %>">
		  	<table <%=Bean.getTableDetailParam() %>>
			<tr>
				<td><%= Bean.bank_statementXML.getfieldTransl("id_bank_statement", false) %> </td><td><input type="text" name="id_bank_statement" size="15" value="<%=id %>" readonly="readonly" class="inputfield-ro"></td>
				<td><%= Bean.bank_statementXML.getfieldTransl("num_bank_account_correspondent", true) %></td>
				<td>
					<%=Bean.getWindowFindBankAccount("bank_account_correspondent", "", "N", "_correspondent", "70") %>
				</td>			
			</tr>
			<tr>
				<td><%= Bean.bank_statementXML.getfieldTransl("line_number", true) %></td> <td><input type="text" name="line_number" size="15" value="" class="inputfield"> </td>
				<td><%= Bean.bank_statementXML.getfieldTransl("name_jur_prs_client", false) %></td>
				<td>
					<input type="hidden" name="id_jur_prs_correspondent" id="id_jur_prs_correspondent" size="70" value="" readonly="readonly" class="inputfield-ro"> 
					<input type="text" name="name_jur_prs_correspondent" id="name_jur_prs_correspondent" size="70" value="" readonly="readonly" class="inputfield-ro"> 
				</td>			
			</tr>
			<tr>
				<td><%= Bean.bank_statementXML.getfieldTransl("cd_operation", false) %></td><td><input type="text" name="operation_code" size="15" value="" class="inputfield"></td>
				<td><%= Bean.bank_statementXML.getfieldTransl("name_bank_branch_client", false) %></td>
				<td>
					<input type="hidden" id="id_bank" name="id_bank" value="" readonly="readonly" class="inputfield">
					<input type="text" id="name_bank" name="name_bank" size="70" value="" readonly="readonly" class="inputfield-ro">
				</td>			
			</tr>
			<tr>
				<td><%=Bean.bank_statementXML.getfieldTransl("date_operation", true)%></td> <td><%=Bean.getCalendarInputField("operation_date", Bean.getSysDate(), "10") %></td>
				<td><%= Bean.bank_statementXML.getfieldTransl("cd_currency_client", false) %></td>
					<td><input type="hidden" id="cd_currency" name="cd_currency" value="" readonly="readonly" class="inputfield">
					<input type="text" id="name_currency" name="name_currency" size="70" value="" readonly="readonly" class="inputfield-ro"></td>
			</tr>
			<tr>
				<td>&nbsp;</td><td>&nbsp;</td>
				<td>&nbsp;</td><td>&nbsp;</td>
			</tr>
			<tr>
				<td><%= Bean.bank_statementXML.getfieldTransl("number_document", false) %></td><td><input type="text" name="doc_number" size="15" value="" class="inputfield"></td>
				<td><%= Bean.bank_statementXML.getfieldTransl("debet_amount", false) %></td><td><input type="text" name="debet_amount" size="15" value="" class="inputfield"></td>
			</tr>
			<tr>
				<td><%=Bean.bank_statementXML.getfieldTransl("date_document", false)%></td> <td><%=Bean.getCalendarInputField("doc_date", "", "10") %></td>
				<td><%= Bean.bank_statementXML.getfieldTransl("credit_amount", false) %></td><td><input type="text" name="credit_amount" size="15" value="" class="inputfield"></td>
			</tr>
			<tr>
				<td><%= Bean.bank_statementXML.getfieldTransl("payment_assignment", false) %></td><td  colspan="3"><textarea name="assignment" cols="90" rows="2" class="inputfield"></textarea> </td>
			</tr>

            <tr>
	  			<td colspan="6" align="center">
					<%=Bean.getSubmitButtonAjax("../crm/finance/bankstatementupdate.jsp") %>
					<%=Bean.getResetButton() %>
					<%=Bean.getGoBackButton("../crm/finance/bankstatementspecs.jsp?id=" + id) %>
	  			</td>
	  		</tr>

		  	</table>

		  </form>
		<%= Bean.getCalendarScript("doc_date", false) %>
		<%= Bean.getCalendarScript("operation_date", false) %>

        <%
	   	} else {
	   	    %> <%= Bean.getUnknownActionText(action) %><%
	   	}
	
	} else if (process.equalsIgnoreCase("yes"))	{
		      
		String
			id_bank_statement_line 	= Bean.getDecodeParam(parameters.get("id_bank_statement_line")), 
		    line_number 			= Bean.getDecodeParam(parameters.get("line_number")), 
		    id_bank_statement 		= Bean.getDecodeParam(parameters.get("id_bank_statement")),
		    corr_id_bank_account 	= Bean.getDecodeParam(parameters.get("id_bank_account_correspondent")), 
		    operation_code 			= Bean.getDecodeParam(parameters.get("operation_code")),
		    operation_date 			= Bean.getDecodeParam(parameters.get("operation_date")), 
		    debet_amount 			= Bean.getDecodeParam(parameters.get("debet_amount")), 
		    credit_amount 			= Bean.getDecodeParam(parameters.get("credit_amount")), 
		    doc_number 				= Bean.getDecodeParam(parameters.get("doc_number")),
		    doc_date 				= Bean.getDecodeParam(parameters.get("doc_date")), 
		    assignment 				= Bean.getDecodeParam(parameters.get("assignment")), 
		    reconcile_state			= Bean.getDecodeParam(parameters.get("reconcile_state"));
		      
		if (action.equalsIgnoreCase("add")) { 
			
			String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_BANK_STATEMENT.add_bank_statement_line("+
				"?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";

			String[] pParam = new String [12];

			pParam[0] = id;
			pParam[1] = line_number;
			pParam[2] = corr_id_bank_account;
			pParam[3] = operation_code;
			pParam[4] = operation_date;
			pParam[5] = debet_amount;
			pParam[6] = credit_amount;
			pParam[7] = doc_number;
			pParam[8] = doc_date;
			pParam[9] = assignment;
			pParam[10] = "UNRECONCILE";
			pParam[11] = Bean.getDateFormat();

   		 	%>
   			<%= Bean.getCallResultParam("INSERT", callSQL, pParam, "../crm/finance/bankstatementspecs.jsp?id=" + id + "&id_line=", "") %>
   			<% 	

		} else if (action.equalsIgnoreCase("remove")) { 
			
			String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_BANK_STATEMENT.delete_bank_statement_line(?,?)}";

			String[] pParam = new String [1];

			pParam[0] = line;

   		 	%>
   			<%= Bean.getCallResultParam("DELETE", callSQL, pParam, "../crm/finance/bankstatementspecs.jsp?id=" + id, "") %>
   			<% 	

		} else if (action.equalsIgnoreCase("set_need_import")) { 
			
			ArrayList<String> id_value=new ArrayList<String>();
			ArrayList<String> prv_value=new ArrayList<String>();
	
	    	String callSQL = "";
	    	Set<String> keySet = parameters.keySet();
			Iterator<String> keySetIterator = keySet.iterator();
			String key = "";
	    	while(keySetIterator.hasNext()) {
	   			try{
	   				key = (String)keySetIterator.next();
	   				if(key.contains("chb_id")){
	   					id_value.add(key.substring(7));
	   				}
	   				if(key.contains("prv_id")){
	   					prv_value.add(key.substring(7));
	   				}
	   			}
	   			catch(Exception ex){
	   				Bean.writeException(
	   						"../crm/finance/bankstatementupdate.jsp",
	   						type,
	   						process,
	   						action,
	   						Bean.commonXML.getfieldTransl("h_get_param_value_error", false)+key+": " + ex.toString());
	   			}
	   		}
	
	   	    String resultInt = "";
	   	    String resultFull = "0";
	   	    String resultMessage = "";
	   	    String resultMessageFull = "";
	   	    String[] results = new String[4];

			String[] pParam = new String [2];
	   	    
		   	 if (id_value.size()>0) {
	  	 		 for(int counter=0;counter<id_value.size();counter++){ 
	  	 			 if (!(prv_value.contains(id_value.get(counter)))) {
	  	 				 
	  	 				callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_BANK_STATEMENT.set_need_bs_line_import(?,?,?)}";

	   					pParam[0] = id_value.get(counter);
	   					pParam[1] = "Y";
				
						results = Bean.myCallFunctionParam(callSQL, pParam, 2);
						resultInt = results[0];
						resultMessage = results[1];
						
						%>
						<%= Bean.showCallSQL(callSQL) %>
						<%
					
						if (!("0".equalsIgnoreCase(resultInt))) {
							resultFull = resultInt;
							resultMessageFull = resultMessageFull + "; " +resultMessage;
						}
					}
	  	 		}
			}
		   	 	if (prv_value.size()>0) {
		   	 		for(int counter=0;counter<prv_value.size();counter++){ 
					 	if (!(id_value.contains(prv_value.get(counter)))) {
					   	 				 
					 		callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_BANK_STATEMENT.set_need_bs_line_import(?,?,?)}";
					
		   					pParam[0] = prv_value.get(counter);
		   					pParam[1] = "N";
					
							results = Bean.myCallFunctionParam(callSQL, pParam, 2);
							resultInt = results[0];
							resultMessage = results[1];
							
							%>
							<%= Bean.showCallSQL(callSQL) %>
							<%
						
							if (!("0".equalsIgnoreCase(resultInt))) {
								resultFull = resultInt;
								resultMessageFull = resultMessageFull + "; " +resultMessage;
							}
					 	}
		  	 		 }
		   	 		
		   	 	}
		
		   	 	%>
		  	    <%=Bean.showCallResult(
		   	    		callSQL, 
		   	    		resultFull, 
		   	    		resultMessageFull, 
		   	    		"../crm/finance/bankstatementspecs.jsp?id=" + id, 
		   	    		"../crm/finance/bankstatementspecs.jsp?id=" + id, 
		   	    		Bean.form_messageXML.getfieldTransl("save_error", false)) %>
		   		<% 

		} else { %> 
			<%= Bean.getUnknownActionText(action) %><% 
		}
	} else {%> 
		<%= Bean.getUnknownProcessText(process) %> <%
	}
} else if (type.equalsIgnoreCase("posting")) {
	if (process.equalsIgnoreCase("no")) {
		if (action.equalsIgnoreCase("edit")) { 
		
			String id_posting_detail	= Bean.getDecodeParam(parameters.get("id_posting_detail"));
		
			bcPostingEditObject posting = new bcPostingEditObject();
			
		%>
	
		<%= Bean.getOperationTitle(
			Bean.postingXML.getfieldTransl("h_postings_edit", false),
			"Y",
			"Y") 
		%>
		<form action="../crm/finance/bankstatementupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">
	        <input type="hidden" name="type" value="posting">
	        <input type="hidden" name="action" value="edit">
	        <input type="hidden" name="process" value="yes">
	        <input type="hidden" name="id" value="<%=id %>">
		<table <%=Bean.getTableDetailParam() %>>
	
			<%=posting.getPostingEditHTML(id_posting_detail, Bean.getDateFormatTitle()) %>
	
			<tr>
				<td colspan="6" align="center">
					<%=Bean.getSubmitButtonAjax("../crm/finance/bankstatementupdate.jsp") %>
					<%=Bean.getResetButton() %>
					<%=Bean.getGoBackButton("../crm/finance/bankstatementspecs.jsp?id=" + id) %>
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
			id_posting_detail			= Bean.getDecodeParam(parameters.get("id_posting_detail")),
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
			<%= Bean.getCallResultParam("RUN", callSQL, pParam, "../crm/finance/bankstatementspecs.jsp?id=" + id+ "&id_report=", "") %>
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
			<%= Bean.getCallResultParam("UPDATE", callSQL, pParam, "../crm/finance/bankstatementspecs.jsp?id=" + id, "") %>
			<% 	
			
		} else if (action.equalsIgnoreCase("remove")) { 
		       
	       	String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_BANK_STATEMENT.delete_posting(?,?)}";

	    	String[] pParam = new String [1];

	    	pParam[0] = id_posting_detail;
	
		 	%>
			<%= Bean.getCallResultParam("DELETE", callSQL, pParam, "../crm/finance/bankstatementspecs.jsp?id=" + id, "") %>
			<% 	
			
		} else if (action.equalsIgnoreCase("deleteall")) { 
	       
	       	String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_BANK_STATEMENT.delete_all_bs_posting(?,?)}";

	    	String[] pParam = new String [1];

	    	pParam[0] = id;
	
		 	%>
			<%= Bean.getCallResultParam("DELETE", callSQL, pParam, "../crm/finance/bankstatementspecs.jsp?id=" + id, "") %>
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
