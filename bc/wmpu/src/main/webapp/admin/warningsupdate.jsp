	<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%@page import="bc.objects.bcWarningObject"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.ArrayList"%><html>

<%= Bean.getLogOutScript(request) %>

<head>
	<%= Bean.getMetaContent() %>
	<%= Bean.getBottomFrameCSS() %>
</head>

<body>
<%
String pageFormName = "FORM_WARNINGS";

Bean.setJspPageForTabName(pageFormName);

request.setCharacterEncoding("UTF-8");

HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String id		= Bean.getDecodeParam(parameters.get("id_warning")); 
String action	= Bean.getDecodeParam(parameters.get("action")); 
String process	= Bean.getDecodeParam(parameters.get("process"));

String id_menu 	= Bean.getDecodeParam(parameters.get("id_menu"));
String id_tab 	= Bean.getDecodeParam(parameters.get("id_tab"));

String id_form_menu = "";
if (id_tab==null || "".equalsIgnoreCase(id_tab)) {
	id_form_menu = id_menu;
} else {
	id_form_menu = id_tab;
}

if (id==null || ("".equalsIgnoreCase(id))) id="empty";
if (action==null || ("".equalsIgnoreCase(action))) action="empty";
if (process==null || ("".equalsIgnoreCase(process))) process="empty";

if (process.equalsIgnoreCase("no"))
/* вибираємо тип дії (добавити, видалити...)*/
	{

	/*  --- Добавити запис --- */
    	if (action.equalsIgnoreCase("add") || action.equalsIgnoreCase("add2")) {
	        %> 
	
    <%= Bean.getOperationTitle(
    		Bean.warningXML.getfieldTransl("h_warning_add", false),
			"Y",
			"N") 
	%>

		<script>
			var formDataWarning = new Array (
				new Array ('desc_warning', 'varchar2', 1)
			);
			function myValidateForm() {
				return validateForm(formDataWarning);
			}
		</script>
    <form action="../admin/warningsupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return myValidateForm()">
	        <input type="hidden" name="action" value="add">
	        <input type="hidden" name="process" value="yes">
	<table <%=Bean.getTableDetailParam() %>>
		<tr>
			<td><%= Bean.warningXML.getfieldTransl("id_menu", false) %></td> <td><select name="id_menu_element" class="inputfield"><%=Bean.getAllMenuOptions("", true) %></select></td>
			<td><%= Bean.warningXML.getfieldTransl("type_warning", true) %></td> <td><select name="type_warning" class="inputfield"><%=Bean.getMeaningFromLookupNameOptions("SYS_WARNING_TYPE", "ERROR", false) %></select></td>
		</tr>
		<tr>
			<td rowspan=8><%= Bean.warningXML.getfieldTransl("desc_warning", true) %></td><td rowspan=8><textarea name="desc_warning" cols="90" rows="10" class="inputfield"></textarea></td>			
			<td><%= Bean.warningXML.getfieldTransl("status_warning", true) %></td> <td><select name="status_warning" class="inputfield"><%=Bean.getMeaningFromLookupNameOptions("WARNING_STATUS", "N", false) %></select></td>
		</tr>
		<tr>
			<td><%= Bean.warningXML.getfieldTransl("found_by", false) %></td> 
			<td>
				<input type="hidden" id="found_by" name="found_by" value="<%=Bean.loginUser.getValue("ID_USER") %>" readonly="readonly" class="inputfield">
				<input type="text" id="found_by_name" name="found_by_name" size="20" value="<%=Bean.loginUser.getValue("NAME_USER") %>" readonly="readonly" class="inputfield-ro"> 
			</td>
		</tr>
		<tr>
			<td>&nbsp;</td><td>&nbsp;</td>
		</tr>
		<tr>
			<td>&nbsp;</td><td>&nbsp;</td>
		</tr>
		<tr>
			<td>&nbsp;</td><td>&nbsp;</td>
		</tr>
		<tr>
			<td>&nbsp;</td><td>&nbsp;</td>
		</tr>
		<tr>
			<td>&nbsp;</td><td>&nbsp;</td>
		</tr>
		<tr>
			<td>&nbsp;</td><td>&nbsp;</td>
		</tr>
		<tr>
			<td colspan="4" align="center">
				<%=Bean.getSubmitButtonAjax("../admin/warningsupdate.jsp") %>
				<%=Bean.getResetButton() %>
				<% if (action.equalsIgnoreCase("add")) { %>
					<%=Bean.getGoBackButton("../admin/warnings.jsp") %>
				<% } else { %>
					<%=Bean.getGoBackButton("../admin/warningspecs.jsp?id=" + id) %>
				<% } %>
			</td>
		</tr>

	</table>

</form>

	        <%
    	} else if (action.equalsIgnoreCase("add_form")) {
		        %> 
		<script>
			var formDataWarning = new Array (
				new Array ('desc_warning', 'varchar2', 1)
			);
			function myValidateForm() {
				return validateForm(formDataWarning);
			}
		</script>

	    <%= Bean.getOperationTitle(
	    		Bean.warningXML.getfieldTransl("h_warning_add", false),
				"Y",
				"N") 
		%>

	    <form action="../admin/warningsupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return myValidateForm()">
		        <input type="hidden" name="action" value="add">
		        <input type="hidden" name="process" value="yes">
		        <input type="hidden" name="id_menu_element" value="<%=id_form_menu %>">
		<table <%=Bean.getTableDetailParam() %>>
			<tr>
				<td><%= Bean.warningXML.getfieldTransl("id_menu", false) %></td> <td><input type="text" name="id_menu" size="110" value="<%=Bean.getMenuName(id_menu) %>" readonly="readonly" class="inputfield-ro"> </td>
				<td><%= Bean.warningXML.getfieldTransl("type_warning", true) %></td> <td><select name="type_warning" class="inputfield"><%=Bean.getMeaningFromLookupNameOptions("SYS_WARNING_TYPE", "ERROR", false) %></select></td>
			</tr>
			<tr>
				<td><%= Bean.warningXML.getfieldTransl("id_tab", false) %></td> <td><input type="text" name="id_tab" size="110" value="<%=Bean.getMenuName(id_tab) %>" readonly="readonly" class="inputfield-ro"> </td>
				<td><%= Bean.warningXML.getfieldTransl("status_warning", true) %></td> <td><select name="status_warning" class="inputfield"><%=Bean.getMeaningFromLookupNameOptions("WARNING_STATUS", "N", false) %></select></td>
			</tr>
			<tr>
				<td><%= Bean.warningXML.getfieldTransl("desc_warning", true) %></td><td><textarea name="desc_warning" cols="105" rows="10" class="inputfield"></textarea></td>			
				<td><%= Bean.warningXML.getfieldTransl("found_by", false) %></td> 
				<td>
					<input type="hidden" id="found_by" name="found_by" value="<%=Bean.loginUser.getValue("ID_USER") %>" readonly="readonly" class="inputfield">
					<input type="text" id="found_by_name" name="found_by_name" size="30" value="<%=Bean.loginUser.getValue("NAME_USER") %>" readonly="readonly" class="inputfield-ro"> 
				</td>
			</tr>
			<tr>
				<td colspan="4" align="center">
					<%=Bean.getSubmitButtonAjax("../admin/warningsupdate.jsp") %>
					<%=Bean.getResetButton() %>
				</td>
			</tr>

		</table>

	</form>

		        <%
    	} else {
    	    %> <%= Bean.getUnknownActionText(action) %><%
    	}
	}

else if (process.equalsIgnoreCase("yes"))
	{
    String
    desc_warning 	= Bean.getDecodeParamPrepare(parameters.get("desc_warning")),
    type_warning 	= Bean.getDecodeParamPrepare(parameters.get("type_warning")),
    status_warning 	= Bean.getDecodeParamPrepare(parameters.get("status_warning")),
    found_by 		= Bean.getDecodeParamPrepare(parameters.get("found_by")),
    realization 	= Bean.getDecodeParamPrepare(parameters.get("realization")),
    id_menu_element	= Bean.getDecodeParamPrepare(parameters.get("id_menu_element"));
	
    if (action.equalsIgnoreCase("add")) { 
    	
		String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK$SYS_WARNING_UI.add_warning(?,?,?,?,?,?)}";

		ArrayList<String> pParam = new ArrayList<String>();

		pParam.add(desc_warning);
		pParam.add(type_warning);
		pParam.add(status_warning);
		pParam.add(id_menu_element);
	
		%>
		<%= Bean.getCallResultParam("INSERT", callSQL, pParam, "../admin/warningspecs.jsp?id=" , "../admin/warnings.jsp") %>
		<% 	

   	} else if (action.equalsIgnoreCase("adderror")) { 
    	String backUrl 	= Bean.getDecodeParam(parameters.get("bank_url"));
    	
    	String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK$SYS_WARNING_UI.add_warning(?,'ERROR','N',?,?,?)}";

    	ArrayList<String> pParam = new ArrayList<String>();
				
		pParam.add(desc_warning);
		pParam.add(id_form_menu);
	
		%>
		<%= Bean.getCallResultParam("INSERT", callSQL, pParam, backUrl+"&id_warning=" , "") %>
		<% 	

   	} else if (action.equalsIgnoreCase("remove")) { 
   		
   		String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK$SYS_WARNING_UI.delete_warning(?,?)}";

   		ArrayList<String> pParam = new ArrayList<String>();
				
		pParam.add(id);
	
		%>
		<%= Bean.getCallResultParam("DELETE", callSQL, pParam, "../admin/warnings.jsp" , "") %>
		<% 	
	
   	} else if (action.equalsIgnoreCase("edit")) { 
   		
   		String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK$SYS_WARNING_UI.update_warning(?,?,?,?,?,?,?)}";

   		ArrayList<String> pParam = new ArrayList<String>();
			
		pParam.add(id);
		pParam.add(desc_warning);
		pParam.add(type_warning);
		pParam.add(status_warning);
		pParam.add(id_menu_element);
		pParam.add(realization);
		
		%>
		<%= Bean.getCallResultParam("UPDATE", callSQL, pParam, "../admin/warnings.jsp?id=" + id , "") %>
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
