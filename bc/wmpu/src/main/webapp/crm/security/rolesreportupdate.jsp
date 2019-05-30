<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />
<%@page import="bc.objects.bcRoleObject"%>
<%@page import="bc.objects.bcPrivilegeObject"%>
<%@page import="java.util.Enumeration"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Set"%>
<%@page import="java.util.Iterator"%><html>

<%= Bean.getLogOutScript(request) %>

<head>
	<%= Bean.getMetaContent() %>
	<%= Bean.getBottomFrameCSS() %>
</head>

<body>
<%
HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String pageFormName = "SECURITY_ROLES";

Bean.setJspPageForTabName(pageFormName);

request.setCharacterEncoding("UTF-8");

String id_role		= Bean.getDecodeParam(parameters.get("id_role")); 
String id_report	= Bean.getDecodeParam(parameters.get("id_report"));
String action		= Bean.getDecodeParam(parameters.get("action")); 
String process		= Bean.getDecodeParam(parameters.get("process"));

if (id_role==null || ("".equalsIgnoreCase(id_role))) id_role="empty";
if (action==null || ("".equalsIgnoreCase(action))) action="empty";
if (process==null || ("".equalsIgnoreCase(process))) process="empty";

if (process.equalsIgnoreCase("no"))
/* вибираємо тип дії (добавити, видалити...)*/
	{

    if (action.equalsIgnoreCase("add")) {

    	bcRoleObject role = new bcRoleObject(id_role);
    	
	    %> 
		<%= Bean.getOperationTitle(
				Bean.roleXML.getfieldTransl("LAB_GRANT_REPORT_PRIV", false),
				"Y",
				"N") 
		%>

    <form action="../crm/security/rolesreportupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST">
        <input type="hidden" name="action" value="add">
        <input type="hidden" name="process" value="yes">	
		<input type="hidden" name="id_role" value="<%= id_role %>">
	<table <%=Bean.getTableDetailParam() %>>
	<tr>
		<td><%= Bean.roleXML.getfieldTransl("NAME_ROLE", false) %> </td><td><input type="text" name="NAME_ROLE" size="40" value="<%= role.getValue("NAME_ROLE") %>" readonly="readonly" class="inputfield-ro"></td>
	</tr>
    <tr>
		<td><%= Bean.reportXML.getfieldTransl("name_report", false) %></td> <td><select name="id_report" class="inputfield"><%= Bean.getRoleReportsOptions(id_role) %></select></td>
	</tr>
	<tr>
		<td colspan="2" align="center">
			<%=Bean.getSubmitButtonAjax("../crm/security/rolesreportupdate.jsp") %>
			<%=Bean.getResetButton() %>
			<%=Bean.getGoBackButton("../crm/security/rolespecs.jsp?id=" + id_role) %>
		</td>
	</tr>

</table>

</form> <br><%
   	} else {
    	    %><%= Bean.getUnknownActionText(action) %><%
    	}
	}

else if (process.equalsIgnoreCase("yes"))
	{
    String[] results = new String[2];
    
    if (action.equalsIgnoreCase("add")) { %>
		
		<%= Bean.form_messageXML.getfieldTransl("processing_insert", false) %>
		<% 
		String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK$ROLE_UI.add_report_privilege(?,?,?,?)}";

		String[] pParam = new String [3];

		pParam[0] = id_role;
		pParam[1] = id_report;
		pParam[2] = "Y";
		
		results = Bean.myCallFunctionParam(callSQL, pParam, 2);
 		String resultInt = results[0];
 		String resultMessage = results[1];
 		String fullResult = "0";
 		
 		%>
		<%= Bean.showCallSQL(callSQL) %>
		<%
 		
 		if (!"0".equalsIgnoreCase(resultInt)) {
 			fullResult = resultMessage;
 		}
 		
 		callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".FNC_GRANT_ROLE_DB_PRIV(?,?,?)}";

		String[] pParam2 = new String [2];

		pParam2[0] = id_role;
		pParam2[1] = "N";
		
		results = Bean.myCallFunctionParam(callSQL, pParam2, 2);
 		resultInt = results[0];
 		resultMessage = results[1];
 		
 		if (!"0".equalsIgnoreCase(resultInt)) {
 			fullResult = resultMessage;
 		}

 		%>
  	    <%=Bean.showCallResult(
   	    		callSQL, 
   	    		fullResult, 
   	    		fullResult, 
   	    		"../crm/security/rolespecs.jsp?id=" + id_role, 
   	    		"../crm/security/rolespecs.jsp?id=" + id_role, 
   	    		Bean.form_messageXML.getfieldTransl("add_error", false)) %>
   		<% 

    } else if (action.equalsIgnoreCase("remove")) { %>

		<%= Bean.form_messageXML.getfieldTransl("processing_delete", false) %>
		<%
    	
		String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK$ROLE_UI.delete_report_privilege(?,?,?)}";

		String[] pParam = new String [2];

		pParam[0] = id_role;
		pParam[1] = id_report;
		
		results = Bean.myCallFunctionParam(callSQL, pParam, 2);
 		String resultInt = results[0];
 		String resultMessage = results[1];
 		String fullResult = "0";
 		
 		%>
		<%= Bean.showCallSQL(callSQL) %>
		<%
 		
 		if (!"0".equalsIgnoreCase(resultInt)) {
 			fullResult = resultMessage;
 		}
 		
 		callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".FNC_GRANT_ROLE_DB_PRIV(?,?,?)}";

		String[] pParam2 = new String [2];

		pParam2[0] = id_role;
		pParam2[1] = "N";
		
		results = Bean.myCallFunctionParam(callSQL, pParam2, 2);
 		resultInt = results[0];
 		resultMessage = results[1];
 		
 		if (!"0".equalsIgnoreCase(resultInt)) {
 			fullResult = resultMessage;
 		}
 		
 		%>
  	    <%=Bean.showCallResult(
   	    		callSQL, 
   	    		fullResult, 
   	    		fullResult, 
   	    		"../crm/security/rolespecs.jsp?id=" + id_role, 
   	    		"../crm/security/rolespecs.jsp?id=" + id_role, 
   	    		Bean.form_messageXML.getfieldTransl("remove_error", false)) %>
   		<% 

    } else if (action.equalsIgnoreCase("set_reports")) { %> 
     
		<%= Bean.form_messageXML.getfieldTransl("processing_update", false) %>
	   	<% 
 
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
   						"../crm/security/rolesreportupdate.jsp",
   						"",
   						process,
   						action,
   						Bean.commonXML.getfieldTransl("h_get_param_value_error", false)+key+": " + ex.toString());
			}
		}
		
	    String resultInt = "";
	    String resultFull = "0";
	    String resultMessage = "";
	    String resultMessageFull = "";

	    if (id_value.size()>0) {
	    	 String[] pParam = new String [3];
	    	 
	 		 for(int counter=0;counter<id_value.size();counter++){ 
	 			 if (!(prv_value.contains(id_value.get(counter)))) {
	 				 
		        	callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK$ROLE_UI.add_report_privilege(?,?,?,?)}";
		        	
		        	%>
		        	<%= Bean.showCallSQL(callSQL) %>
		        	<%
		        		
		        	pParam[0] = id_role;
					pParam[1] = id_value.get(counter);
					pParam[2] = "Y";
					
					results = Bean.myCallFunctionParam(callSQL, pParam, 2);
					resultInt = results[0];
					resultMessage = results[1];
				
					if (!("0".equalsIgnoreCase(resultInt))) {
						resultFull = resultInt;
						resultMessageFull = resultMessageFull + "; " +resultMessage;
					}
				}
	 		}
		}
	 	if (prv_value.size()>0) {
	    	String[] pParam = new String [2];
	    	 
	 		for(int counter=0;counter<prv_value.size();counter++){ 
			 	if (!(id_value.contains(prv_value.get(counter)))) {
			   	 				 
		        	callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK$ROLE_UI.delete_report_privilege(?,?,?)}";
					
		        	pParam[0] = id_role;
					pParam[1] = prv_value.get(counter);
		        		
					results = Bean.myCallFunctionParam(callSQL, pParam, 2);
					resultInt = results[0];
					resultMessage = results[1];
				
					if (!("0".equalsIgnoreCase(resultInt))) {
						resultFull = resultInt;
						resultMessageFull = resultMessageFull + "; " +resultMessage;
					}
			 	}
	 		 }
	 		
	 	}

	 	callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK$ROLE_UI.grant_report_priv(?,?)}";

		String[] pParam2 = new String [1];

		pParam2[0] = id_role;
			
		results = Bean.myCallFunctionParam(callSQL, pParam2, 2);
		
		resultInt = results[0];
		resultMessage = results[1];

		if (!("0".equalsIgnoreCase(resultInt))) {
			resultFull = resultInt;
			resultMessageFull = resultMessageFull + "; " +resultMessage;
		}
		
		%>
  	    <%=Bean.showCallResult(
   	    		callSQL, 
   	    		resultFull, 
   	    		resultMessageFull, 
   	    		"../crm/security/rolespecs.jsp?id=" + id_role, 
   	    		"../crm/security/rolespecs.jsp?id=" + id_role, 
   	    		Bean.form_messageXML.getfieldTransl("save_error", false)) %>
   		<% 

    } else { %> 
    	<%= Bean.getUnknownActionText(action) %><% 
    }
} else {
    %> <%= Bean.getUnknownProcessText(process) %> <%
}

%>


</body>
</html>
