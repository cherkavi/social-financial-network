<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%@page import="bc.objects.bcReglamentObject"%>
<%@page import="java.util.HashMap"%><html>

<%= Bean.getLogOutScript(request) %>

<head>
	<%= Bean.getMetaContent() %>
	<%= Bean.getBottomFrameCSS() %>
</head>

<body>
<%
HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String pageFormName = "SECURITY_REGLAMENTS";

Bean.setJspPageForTabName(pageFormName);

request.setCharacterEncoding("UTF-8");

String id		= Bean.getDecodeParam(parameters.get("id")); 
String action	= Bean.getDecodeParam(parameters.get("action"));
String process	= Bean.getDecodeParam(parameters.get("process"));

if (id==null || ("".equalsIgnoreCase(id))) id="empty";
if (action==null || ("".equalsIgnoreCase(action))) action="empty";
if (process==null || ("".equalsIgnoreCase(process))) process="empty";

if (process.equalsIgnoreCase("no"))
/* вибираємо тип дії (добавити, видалити...)*/
	{
	   /*  --- Добавити запис --- */
    	if (action.equalsIgnoreCase("add") || action.equalsIgnoreCase("add2")) {%> 

			<%= Bean.getOperationTitle(
					Bean.reglamentXML.getfieldTransl("LAB_INS_JOB", false),
					"Y",
					"N") 
			%>
    <form action="../crm/security/reglamentsupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST">
        <input type="hidden" name="action" value="add">
        <input type="hidden" name="process" value="yes">

	<table <%=Bean.getTableDetailParam() %>>
		<tr>
			<td><%= Bean.reglamentXML.getfieldTransl("id_job", false) %></td> <td><input type="text" name="id_job" size="10" value="" readonly="readonly" class="inputfield-ro"> </td>
			<td rowspan="3"><%= Bean.reglamentXML.getfieldTransl("desc_job", false) %></td> <td rowspan="3"><textarea name="desc_job" cols="100" rows="3" class="inputfield"></textarea></td>
 		</tr>
 		<tr>
			<td><%= Bean.reglamentXML.getfieldTransl("num_job", false) %></td> <td><input type="text" name="num_job" size="10" value="" readonly="readonly" class="inputfield-ro"> </td>
		</tr>
		<tr>
			<td><%= Bean.reglamentXML.getfieldTransl("name_job", false) %></td> <td><input type="text" name="name_job" size="40" value="" class="inputfield"> </td>
		</tr>
 		<tr>
			<td><%= Bean.reglamentXML.getfieldTransl("next_date", false) %></td> <td><input type="text" name="next_date" size="40" value="SYSDATE" class="inputfield"> </td>
			<td rowspan="3"><%= Bean.reglamentXML.getfieldTransl("what", false) %></td> <td rowspan="3"><textarea name="what" cols="100" rows="10" class="inputfield"></textarea></td>
		</tr>
		<tr>
			<td><%= Bean.reglamentXML.getfieldTransl("interval", false) %></td> <td><input type="text" name="interval" size="40" value="" class="inputfield"> </td>
		</tr>
 		<tr>
			<td><%= Bean.reglamentXML.getfieldTransl("is_enable", false) %></td> <td><select name="is_enable" class="inputfield"><%=Bean.getMeaningFromLookupNameOptions("YES_NO", "", false) %></select></td>
		</tr>
		<tr>
			<td colspan="6" align="center">
				<%=Bean.getSubmitButtonAjax("../crm/security/reglamentsupdate.jsp") %>
				<%=Bean.getResetButton() %>
				<% if (action.equalsIgnoreCase("add")) { %>
					<%=Bean.getGoBackButton("../crm/security/reglaments.jsp") %>
				<% } else { %>
					<%=Bean.getGoBackButton("../crm/security/reglamentspecs.jsp?id=" + id) %>
				<% } %>
			</td>
		</tr>
	</table>

</form>

     <%
    } else {
        %> <%= Bean.getUnknownActionText(action) %><%
    }
} else if (process.equalsIgnoreCase("yes"))	{
    
	String  
    	id_job		= Bean.getDecodeParam(parameters.get("id_job")),
    	name_job	= Bean.getDecodeParam(parameters.get("name_job")),
    	desc_job	= Bean.getDecodeParam(parameters.get("desc_job")),
    	what		= Bean.getDecodeParam(parameters.get("what")),
    	next_date	= Bean.getDecodeParam(parameters.get("next_date")),
    	interval	= Bean.getDecodeParam(parameters.get("interval")),
    	num_job		= Bean.getDecodeParam(parameters.get("num_job")),
    	is_enable	= Bean.getDecodeParam(parameters.get("is_enable")),
    	is_enable_prapared = "";
	
	if ("Y".equalsIgnoreCase(is_enable)) {
		is_enable_prapared = "1";
	} else {
		is_enable_prapared = "0";
	}
    
    if (next_date == null || next_date.trim().length() == 0) next_date = "SYSDATE";
    
    if (action.equalsIgnoreCase("add")) {
    	
    	String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_JOB.add_job(?,?,?,?,?,?,?,?,?)}";

		String[] pParam = new String [7];

		pParam[0] = name_job;
		pParam[1] = desc_job;
		pParam[2] = what;
		pParam[3] = next_date;
		pParam[4] = interval;
		pParam[5] = is_enable_prapared;
		pParam[6] = "DD.MM.RRRR HH24:MI:SS";

	 	%>
		<%= Bean.getCallResultParam("INSERT", callSQL, pParam, "../crm/security/reglamentspecs.jsp?id=" , "../crm/security/reglaments.jsp") %>
		<% 	

    } else if (action.equalsIgnoreCase("remove")) {
    	
		bcReglamentObject reglament = new bcReglamentObject(id);

		String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_JOB.delete_job(?,?,?)}";

		String[] pParam = new String [2];

		pParam[0] = id;
		pParam[1] = reglament.getValue("name_job");
 		
	 	%>
		<%= Bean.getCallResultParam("DELETE", callSQL, pParam, "../crm/security/reglaments.jsp" , "") %>
		<% 	

	} else if (action.equalsIgnoreCase("run")) {
    	
		String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_JOB.run_job(?,?)}";

		String[] pParam = new String [1];

		pParam[0] = id;
 		
	 	%>
		<%= Bean.getCallResultParam("DELETE", callSQL, pParam, "../crm/security/reglaments.jsp" , "") %>
		<% 	

    } else if (action.equalsIgnoreCase("edit")) { 
    	
    	String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_JOB.update_job(?,?,?,?,?,?,?,?,?)}";

		String[] pParam = new String [8];

		pParam[0] = id_job;
		pParam[1] = name_job;
		pParam[2] = desc_job;
		pParam[3] = what;
		pParam[4] = next_date;
		pParam[5] = interval;
		pParam[6] = is_enable_prapared;
		pParam[7] = "DD.MM.RRRR HH24:MI:SS";

	 	%>
		<%= Bean.getCallResultParam("UPDATE", callSQL, pParam, "../crm/security/reglamentspecs.jsp?id=" + id_job, "") %>
		<% 	

    } else { %> 
    	<%= Bean.getUnknownActionText(action) %><% 
    }
} else {%> 
	<%= Bean.getUnknownProcessText(process) %> <%
}

%>


</body>
</html>
