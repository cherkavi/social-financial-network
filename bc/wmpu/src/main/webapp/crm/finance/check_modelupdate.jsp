<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%@page import="java.util.HashMap"%><html>

<%= Bean.getLogOutScript(request) %>

<head>
	<%= Bean.getMetaContent() %>
	<%= Bean.getBottomFrameCSS() %>
</head>

<body>
<%
HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String pageFormName = "FINANCE_CHECK_MODEL";

Bean.setJspPageForTabName(pageFormName);

request.setCharacterEncoding("UTF-8");

String id		= Bean.getDecodeParam(parameters.get("id_clearing")); 
String action	= Bean.getDecodeParam(parameters.get("action")); 
String process	= Bean.getDecodeParam(parameters.get("process"));
String type		= Bean.getDecodeParam(parameters.get("type"));

if (id==null || ("".equalsIgnoreCase(id))) id="empty";
if (type==null || ("".equalsIgnoreCase(type))) type="empty";
if (action==null || ("".equalsIgnoreCase(action))) action="empty";
if (process==null || ("".equalsIgnoreCase(process))) process="empty";

if (type.equalsIgnoreCase("check")) {
	if (process.equalsIgnoreCase("no")) {
		if (action.equalsIgnoreCase("run")) {
    	    %> 
			<%= Bean.getOperationTitle(
					Bean.clearingXML.getfieldTransl("h_export", false),
					"Y",
					"N") 
			%>
			<form action="../crm/finance/clearingupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">
			    <input type="hidden" name="type" value="export">
			    <input type="hidden" name="action" value="run">
			    <input type="hidden" name="process" value="yes">
				<input type="hidden" name="id_clearing" value="<%= id %>">
		    <table <%=Bean.getTableDetailParam() %>>
		    <tr>   
		       <td align="left"> 
		          <font size=2><%= Bean.exp_fileXML.getfieldTransl("file_type", false) %></font>
		       </td>
		       <td align="left"> 
		          <select name="exp_format" class="inputfield"><%= Bean.getMeaningFromLookupNameOptions("CLIENT_BANK_INTERCHANGE_FORMAT", "", false) %></select>
		       </td>
		    </tr>
		    <tr>
		       <td align="left"> 
		          <font size=2><%= Bean.postingXML.getfieldTransl("debug", false) %></font>
		       </td>
		       <td align="left"><select name="debug" class="inputfield"><%= Bean.getMeaningFromLookupNameOptions("YES_NO", "N", false) %></select></td>
		    </tr>
		    <tr>
		       <td align="left"> 
		          <font size=2><%= Bean.postingXML.getfieldTransl("export_with_some_bank_account", false) %></font>
		       </td>
		       <td align="left"><select name="export_some_accounts" class="inputfield"><%= Bean.getMeaningFromLookupNameOptions("YES_NO", "N", false) %></select></td>
		    </tr>
		    <tr>
		       <td align="left"> 
		          <font size=2><%= Bean.postingXML.getfieldTransl("export_0_amount", false) %></font>
		       </td>
		       <td align="left"><select name="export_0_amount" class="inputfield"><%= Bean.getMeaningFromLookupNameOptions("YES_NO", "N", false) %></select></td>
		    </tr>
		    <tr>
		        <td align="center" colspan="2">
					<%=Bean.getSubmitButtonAjax("../crm/finance/clearingupdate.jsp", "export", "updateForm") %>
					<%=Bean.getResetButton() %>
					<%=Bean.getGoBackButton("../crm/finance/clearingspecs.jsp?id="+id) %>
		        </td>
		    </tr>
		 </table>
		 </form>
			<%
			
		}
	} else if (process.equalsIgnoreCase("yes")) {
		
		if (action.equalsIgnoreCase("run")) {
			String check_type = Bean.getDecodeParam(parameters.get("check_type"));
			
			String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_FIN_MODEL_CHECK.check_model(?,?,?,?)}";

			String[] pParam = new String [2];

			pParam[0] = check_type;
			pParam[1] = Bean.getCurrentClubID();
			
		 	%>
			<%= Bean.getCallResultParam("RUN", callSQL, pParam, "../crm/finance/check_model.jsp?id_report=" , "") %>
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
