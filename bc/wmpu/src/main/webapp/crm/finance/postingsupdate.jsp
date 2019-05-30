<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />
<%@page import="bc.objects.bcPostingObject"%>

<%= Bean.getLogOutScript(request) %>

<%@page import="java.util.HashMap"%>
<html>
<head>
	<%= Bean.getMetaContent() %>
	<%= Bean.getBottomFrameCSS() %>
</head>

<body>
<%
HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String pageFormName = "FINANCE_POSTINGS";

Bean.setJspPageForTabName(pageFormName);

request.setCharacterEncoding("UTF-8");

String id		= Bean.getDecodeParam(parameters.get("id")); 
String action	= Bean.getDecodeParam(parameters.get("action")); 
String process	= Bean.getDecodeParam(parameters.get("process"));

if (id==null || ("".equalsIgnoreCase(id))) id="empty";
if (action==null || ("".equalsIgnoreCase(action))) action="empty";
if (process==null || ("".equalsIgnoreCase(process))) process="empty";

if (process.equalsIgnoreCase("no")) {
 	if (action.equalsIgnoreCase("create")) {
 	    %> 
			<%= Bean.getOperationTitle(
					Bean.postingXML.getfieldTransl("LAB_RUN_POSTINGS", false),
					"N",
					"N") 
			%>
	  <form action="../crm/finance/postingsupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST">
			<input type="hidden" name="action" value="create">
	        <input type="hidden" name="process" value="yes">
	  <table <%=Bean.getTableDetailParam() %>>
		<tr>
	       <td align="left" colspan="2"> 
	          &nbsp;&nbsp;&nbsp;&nbsp;<font size=2><%= Bean.postingXML.getfieldTransl("h_posting_creation_title", false) %><br>&nbsp;</font>
	       </td>
		</tr>
		<tr>
			<% String idClub = Bean.getCurrentClubID(); %>
			<% if ("".equalsIgnoreCase(idClub)) {%>
 		    <td><%= Bean.clubXML.getfieldTransl("club", false) %></td>
		  	<td>
				<%=Bean.getWindowFindClub("club", "", "25") %>
		  	</td>
			<% } else { %>
 		    <td><%= Bean.clubXML.getfieldTransl("club", false) %>
				<%= Bean.getGoToClubLink(idClub) %>
			</td><td>
				<input type="hidden" name="id_club" value="<%= idClub %>">
				<input type="text" name="name_club" size="25" value="<%= Bean.getClubShortName(idClub) %>" readonly="readonly" class="inputfield-ro">
			</td>
			<% } %>
	    </tr>
	    <tr>
	       <td align="left"> 
	          <font size=2><%= Bean.postingXML.getfieldTransl("posting_execution_type", false) %></font>
	       </td>
	       <td align="left"><select name="execution_type" class="inputfield"><%= Bean.getMeaningFromLookupNameOrderByNymberValueOptions("POSTING_EXECUTION_TYPE", "ALL", false) %></select></td>
	    </tr>
	    <tr>
	       <td align="left"> 
	          <font size="2"><%= Bean.postingXML.getfieldTransl("debug", false) %></font>
	       </td>
	       <td align="left"><select name="debug" class="inputfield"><%= Bean.getMeaningFromLookupNameOptions("YES_NO", "N", false) %></select></td>
	    </tr>
	    <tr>
	        <td align="center" colspan="4"> 
				<%=Bean.getSubmitButtonAjax("../crm/finance/postingsupdate.jsp") %>
				<%=Bean.getResetButton() %>
				<%=Bean.getGoBackButton("../crm/finance/postings.jsp") %>
	        </td>
	    </tr>
	 </table>
	 </form>
	<%
	} else {
    	%><%= Bean.getUnknownActionText(action) %><%
	}
		
} else if (process.equalsIgnoreCase("yes")) {
	    
	if (action.equalsIgnoreCase("remove")) {
   		String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_POSTING.delete_posting(?,?)}";

		String[] pParam = new String [1];

		pParam[0] = id;

	 	%>
		<%= Bean.getCallResultParam("DELETE", callSQL, pParam, "../crm/finance/postings.jsp" , "") %>
		<% 	

	} else if (action.equalsIgnoreCase("create")) {
		String[] results = new String[2];
		String callSQL = "";
		String resultInt = "";
		String resultMessage = "";
		String resultID = "";
		String reportID = "";
		

		String debug 			= Bean.getDecodeParam(parameters.get("debug"));
		String execution_type	= Bean.getDecodeParam(parameters.get("execution_type"));
		String id_club			= Bean.getDecodeParam(parameters.get("id_club"));
		
		callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_POSTING.run_and_apply_posting(?,?,?,?,?,?)}";

		String[] pParam = new String [4];

		pParam[0] = id_club;
		pParam[1] = "";
		pParam[2] = execution_type;
		pParam[3] = debug;
		
		results = Bean.myCallFunctionParam(callSQL, pParam, 3);
		resultInt = results[0];
		resultMessage = results[1];
		reportID = results[2];

		String logRowType = "";
		if (!("0".equalsIgnoreCase(reportID))) {
			logRowType = "E";
		}
		%>
	    <%=Bean.showCallResult(
	    		callSQL, 
	    		resultInt, 
	    		resultMessage, 
	    		"../crm/finance/postings.jsp?id_type=REPORTS&page_report=1&page_report_det=1&page_report_posting=1&id_report=" + reportID + "&log_row_type=" + logRowType, 
	    		"../crm/finance/postings.jsp", 
	    		Bean.form_messageXML.getfieldTransl("save_error", false)) %>
		<% 
		
	} else if (action.equalsIgnoreCase("edit")) { 
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
			payment_function			= Bean.getDecodeParam(parameters.get("payment_function"));
	       
       	String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_POSTING.update_posting("+
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
		<%= Bean.getCallResultParam("UPDATE", callSQL, pParam, "../crm/finance/postingspecs.jsp?id=" + id_posting_detail, "") %>
		<% 	
		
	} else { %> 
		<%= Bean.getUnknownActionText(action) %><% 
	}
} else {
   	%> <%= Bean.getUnknownProcessText(process) %><%
}

%>


</body>
</html>
