<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />
<%@page import="bc.objects.bcPostingObject"%>

<%= Bean.getLogOutScript(request) %>


<%@page import="java.util.HashMap"%><html>
<head>
	<%= Bean.getMetaContent() %>
	<%= Bean.getBottomFrameCSS() %>
</head>

<body>
<%
HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String pageFormName = "FINANCE_ACCOUNTING_DOC";

Bean.setJspPageForTabName(pageFormName);

request.setCharacterEncoding("UTF-8");

String id_doc	= Bean.getDecodeParam(parameters.get("id_doc")); 
String action	= Bean.getDecodeParam(parameters.get("action")); 
String process	= Bean.getDecodeParam(parameters.get("process"));
String type		= Bean.getDecodeParam(parameters.get("type"));

if (id_doc==null || ("".equalsIgnoreCase(id_doc))) id_doc="empty";
if (type==null || ("".equalsIgnoreCase(type))) type="empty";
if (action==null || ("".equalsIgnoreCase(action))) action="empty";
if (process==null || ("".equalsIgnoreCase(process))) process="empty";

if (type.equalsIgnoreCase("header")) {
	if (process.equalsIgnoreCase("no")) {
    	 if (action.equalsIgnoreCase("export")) {
    		 
    	    %>
			<%= Bean.getOperationTitle(
					Bean.postingXML.getfieldTransl("h_export", false),
					"Y",
					"Y") 
			%>

      	<script>
			var formExport = new Array (
				new Array ('exp_file', 'varchar2', 1)
			);
		</script>
		<%
		 bcPostingObject posting = new bcPostingObject(id_doc);
		 posting.getFeature();

		%>
	  <form action="../crm/finance/accounting_docupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formExport)">
	    <input type="hidden" name="type" value="header">
	  	<input type="hidden" name="process" value="yes">
	  	<input type="hidden" name="action" value="export">
	  	<input type="hidden" name="id_doc" value="<%=id_doc %>">
	  	<input type="hidden" name="id_club" value="<%=posting.getValue("ID_CLUB") %>">
	  	<input type="hidden" name="export_type" value="group">
	  <table <%=Bean.getTableDetailParam() %>>
		<tr>
			<td>
				<font size=2><%= Bean.postingXML.getfieldTransl("id_posting", false) %>:&nbsp;</font>
			</td>
			<td>
				<input type="text" name="id_posting" size="30" value="<%= posting.getValue("NUMBER_POSTING") + " - " +posting.getValue("DATE_POSTING_FRMT") %>" readonly="readonly" class="inputfield-ro">
			</td>
		</tr>
	    <tr>
	       	<td>
	          	<font size=2><%= Bean.exp_fileXML.getfieldTransl("name_file", false) %>:&nbsp;</font>
	       	</td>
	       	<td>
	          	<input type="text" name="exp_file" size="30" value="" class="inputfield">
	       	</td>
	    </tr>
	    <tr>
	       	<td> 
	          	<font size=2><%= Bean.postingXML.getfieldTransl("export_with_some_bank_account", false) %></font>
	       	</td>
	       	<td>
	       		<select name="export_some_accounts" class="inputfield"><%= Bean.getMeaningFromLookupNameOptions("YES_NO", "N", false) %></select>
			</td>
	    </tr>
	    <tr>
	       	<td> 
	          	<font size=2><%= Bean.postingXML.getfieldTransl("export_0_amount", false) %></font>
	       </td>
	       <td>
	       		<select name="export_0_amount" class="inputfield"><%= Bean.getMeaningFromLookupNameOptions("YES_NO", "N", false) %></select>
	       </td>
	    </tr>
		<tr>
		   <td align="left" width="50%"> 
		      <font size=2><%= Bean.postingXML.getfieldTransl("debug", false) %></font>
		   </td>
		   <td align="left"><select name="debug" class="inputfield"><%= Bean.getMeaningFromLookupNameOptions("YES_NO", Bean.getUIUserParam("CLEARING_EXPORT_DEBUG"), false) %></select></td>
		</tr>
	    <tr>
	        <td colspan="2" align="center"> 
				<%=Bean.getSubmitButtonAjax("../crm/finance/accounting_docupdate.jsp") %>
				<%=Bean.getResetButton() %>
				<%=Bean.getGoBackButton("../crm/finance/accounting_docspecs.jsp?type=doc&id_doc="+id_doc) %>
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

	   		String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_POSTING.delete_group_posting(?,?)}";

			String[] pParam = new String [1];

			pParam[0] = id_doc;

		 	%>
			<%= Bean.getCallResultParam("DELETE", callSQL, pParam, "../crm/finance/accounting_doc.jsp?id_type=DOCUMENTS" , "") %>
			<% 	

		} else if (action.equalsIgnoreCase("edit")) {

			String 
				number_posting		= Bean.getDecodeParam(parameters.get("number_posting")),
				date_posting		= Bean.getDecodeParam(parameters.get("date_posting")),
				state_posting		= Bean.getDecodeParam(parameters.get("state_posting"));
    
			String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_POSTING.update_group_posting(?,?,?,?,?,?)}";

			String[] pParam = new String [5];

			pParam[0] = id_doc;
			pParam[1] = number_posting;
			pParam[2] = date_posting;
			pParam[3] = state_posting;
			pParam[4] = Bean.getDateFormat();
		
		 	%>
			<%= Bean.getCallResultParam("UPDATE", callSQL, pParam, "../crm/finance/accounting_docspecs.jsp?id_type=DOCUMENTS&id_doc=" + id_doc, "") %>
			<% 	

		} else if (action.equalsIgnoreCase("create")) {
			String id_club          = Bean.getCurrentClubID();

			String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_POSTING.group_posting(?,?,?)}";

			String[] pParam = new String [1];

			pParam[0] = id_club;

			%>
			<%= Bean.getCallResultParam("RUN", callSQL, pParam, "../crm/finance/accounting_docspecs.jsp?type=doc&id_doc=", "../crm/finance/accounting_doc.jsp") %>
			<% 	
			
		} else if (action.equalsIgnoreCase("export")) {
			String export_some_accounts 	= Bean.getDecodeParam(parameters.get("export_some_accounts"));
			String export_0_amount 			= Bean.getDecodeParam(parameters.get("export_0_amount"));
			String date_beg 				= Bean.getDecodeParam(parameters.get("date_beg"));
			String date_end 				= Bean.getDecodeParam(parameters.get("date_end"));
			String exp_file 				= Bean.getDecodeParam(parameters.get("exp_file"));
			String id_posting 				= Bean.getDecodeParam(parameters.get("id_posting"));
			String export_type 				= Bean.getDecodeParam(parameters.get("export_type"));
			String id_club	 				= Bean.getDecodeParam(parameters.get("id_club"));
			String debug	 				= Bean.getDecodeParam(parameters.get("debug"));

			String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_POSTING.run_posting_export1(?,?,?,?,?,?,?,?,?,?,?,?)}";

			String[] pParam = new String [10];

			pParam[0] = export_type;
			pParam[1] = id_posting;
			pParam[2] = date_beg;
			pParam[3] = date_end;
			pParam[4] = exp_file;
			pParam[5] = export_some_accounts;
			pParam[6] = export_0_amount;
			pParam[7] = id_club;
			pParam[8] = debug;
			pParam[9] = Bean.getDateFormat();

			%>
			<%= Bean.getCallResultParam("RUN", callSQL, pParam, "../crm/finance/accounting_docspecs.jsp?type=doc&id_doc=", "") %>
			<% 	
				
		} else { %> 
			<%= Bean.getUnknownActionText(action) %><% 
		}
	} else {
    	%> <%= Bean.getUnknownProcessText(process) %><%
	}
} else {
    %> <%= Bean.getUnknownTypeText(type) %><%
}

%>


</body>
</html>
