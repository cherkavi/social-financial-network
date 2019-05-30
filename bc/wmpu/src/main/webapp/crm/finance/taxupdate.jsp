<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%= Bean.getLogOutScript(request) %>
<html>
<head>
	<%= Bean.getMetaContent() %>
	<%= Bean.getBottomFrameCSS() %>
</head>

<%
HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String pageFormName = "FINANCE_TAX";

Bean.setJspPageForTabName(pageFormName);

request.setCharacterEncoding("UTF-8");

String id			= Bean.getDecodeParam(parameters.get("id"));
String type			= Bean.getDecodeParam(parameters.get("type")); 
String action		= Bean.getDecodeParam(parameters.get("action")); 
String process		= Bean.getDecodeParam(parameters.get("process"));
String id_club		= Bean.getDecodeParam(parameters.get("id_club"));
String id_value		= Bean.getDecodeParam(parameters.get("id_value"));

if (id==null || ("".equalsIgnoreCase(id))) id="";
if (type==null || ("".equalsIgnoreCase(type))) type="";
if (action==null || ("".equalsIgnoreCase(action))) action="";
if (process==null || ("".equalsIgnoreCase(process))) process="";
if (id_club==null || ("".equalsIgnoreCase(id_club))) id_club="";
if (id_value==null || ("".equalsIgnoreCase(id_value))) id_value="";

if (type.equalsIgnoreCase("value")) {
	if (process.equalsIgnoreCase("no")) {
	   /*  --- Добавити запис --- */
    	if (action.equalsIgnoreCase("add")) {%> 
<body>
	   <script>
	   		var formData = new Array (
	    		new Array ('value_tax', 'varchar2', 1),
	    		new Array ('cd_tax_value_type', 'varchar2', 1),
	    		new Array ('name_club', 'varchar2', 1)
	    	);
	   </script>
	    <%= Bean.getOperationTitle(
	    		Bean.taxXML.getfieldTransl("h_add_tax_value", false),
	    		"Y",
	    		"N") 
		%>
		<form action="../crm/finance/taxupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">
	        <input type="hidden" name="action" value="add">
	        <input type="hidden" name="process" value="yes">
	        <input type="hidden" name="type" value="value">
		        <input type="hidden" name="id" value="<%=id %>">

		<table <%=Bean.getTableDetailParam() %>>
		<tr>
			<td><%= Bean.taxXML.getfieldTransl("value_tax", true) %></td> <td><input type="text" name="value_tax" size="20" value="" class="inputfield"> </td>
			<td><%= Bean.clubXML.getfieldTransl("club", true) %>
				<% id_club = Bean.getCurrentClubID(); %>
				<%= Bean.getGoToClubLink(id_club) %>
			</td>
		  	<td>
				<%=Bean.getWindowFindClub("club", id_club, "25") %>
		  	</td>
		</tr>
		<tr>
			<td><%= Bean.taxXML.getfieldTransl("name_tax_value_type", true) %></td> <td><select name="cd_tax_value_type" id="cd_tax_value_type" class="inputfield"><%= Bean.getTaxValueTypeOptions("PERCENT", false) %></select></td>
			<td valign="top"><%=Bean.taxXML.getfieldTransl("begin_action_date", false)%></td><td><%=Bean.getCalendarInputField("begin_action_date", Bean.getSysDate(), "10") %></td>
		</tr>
		<tr>
			<td><%= Bean.taxXML.getfieldTransl("name_currency", false) %></td> <td><select name="cd_currency" id="cd_currency" class="inputfield"><%= Bean.getCurrencyOptions("", true) %></select></td>
			<td valign="top"><%=Bean.taxXML.getfieldTransl("end_action_date", false)%></td><td><%=Bean.getCalendarInputField("end_action_date", "", "10") %></td>
		</tr>
		<tr>
			<td colspan="6" align="center">
				<%=Bean.getSubmitButtonAjax("../crm/finance/taxupdate.jsp") %>
				<%=Bean.getResetButton() %>
				<%=Bean.getGoBackButton("../crm/finance/taxspecs.jsp?id=" + id) %>
			</td>
		</tr>

	</table>
	</form>	
	<!-- Скрипт для втавки меню вибору дати -->
	<%= Bean.getCalendarScript("begin_action_date", false) %>
	<%= Bean.getCalendarScript("end_action_date", false) %>

       <%
		} else if (action.equalsIgnoreCase("edit")) {
		
			bcTaxValueObject value = new bcTaxValueObject(id_value);
		 %> 
		   <script>
		   		var formData = new Array (
		    		new Array ('value_tax', 'varchar2', 1),
		    		new Array ('cd_tax_value_type', 'varchar2', 1)
		    	);
		   </script>
		    <%= Bean.getOperationTitle(
		    		Bean.taxXML.getfieldTransl("h_edit_tax_value", false),
		    		"Y",
		    		"N") 
			%>
			<form action="../crm/finance/taxupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">
		        <input type="hidden" name="action" value="edit">
		        <input type="hidden" name="process" value="yes">
		        <input type="hidden" name="type" value="value">
		        <input type="hidden" name="id" value="<%=id %>">
		        <input type="hidden" name="id_value" value="<%=id_value %>">

			<table <%=Bean.getTableDetailParam() %>>
			<tr>
				<td><%= Bean.taxXML.getfieldTransl("value_tax", true) %></td> <td><input type="text" name="value_tax" size="20" value="<%=value.getValue("VALUE_TAX") %>" class="inputfield"> </td>
				<td><%= Bean.clubXML.getfieldTransl("club", false) %>
					<%= Bean.getGoToClubLink(value.getValue("ID_CLUB")) %>
				</td>
			  	<td>
					<input type="text" name="sname_club" size="20" value="<%=Bean.getClubShortName(value.getValue("ID_CLUB")) %>" readonly="readonly" class="inputfield-ro">
			  	</td>
			</tr>
			<tr>
				<td><%= Bean.taxXML.getfieldTransl("name_tax_value_type", true) %></td> <td><select name="cd_tax_value_type" id="cd_tax_value_type" class="inputfield"><%= Bean.getTaxValueTypeOptions(value.getValue("CD_TAX_VALUE_TYPE"), false) %></select></td>
				<td valign="top"><%=Bean.taxXML.getfieldTransl("begin_action_date", false)%></td><td><%=Bean.getCalendarInputField("begin_action_date", value.getValue("BEGIN_ACTION_DATE_FRMT"), "10") %></td>
			</tr>
			<tr>
				<td><%= Bean.taxXML.getfieldTransl("name_currency", false) %></td> <td><select name="cd_currency" id="cd_currency" class="inputfield"><%= Bean.getCurrencyOptions(value.getValue("CD_CURRENCY"), true) %></select></td>
				<td valign="top"><%=Bean.taxXML.getfieldTransl("end_action_date", false)%></td><td><%=Bean.getCalendarInputField("end_action_date", value.getValue("END_ACTION_DATE_FRMT"), "10") %></td>
			</tr>
			<%=	Bean.getCreationAndMoficationRecordFields(
					value.getValue(Bean.getCreationDateFieldName()),
					value.getValue("CREATED_BY"),
					value.getValue(Bean.getLastUpdateDateFieldName()),
					value.getValue("LAST_UPDATE_BY")
				) %>
			<tr>
				<td colspan="6" align="center">
					<%=Bean.getSubmitButtonAjax("../crm/finance/taxupdate.jsp") %>
					<%=Bean.getResetButton() %>
					<%=Bean.getGoBackButton("../crm/finance/taxspecs.jsp?id=" + id) %>
				</td>
			</tr>

		</table>
		</form>	
		<!-- Скрипт для втавки меню вибору дати -->
		<%= Bean.getCalendarScript("begin_action_date", false) %>
		<%= Bean.getCalendarScript("end_action_date", false) %>

	       <%
			} else {
	        %> <%= Bean.getUnknownActionText(action) %><%
		}
	} else if (process.equalsIgnoreCase("yes")) {
	    
		String
			begin_action_date	= Bean.getDecodeParam(parameters.get("begin_action_date")),
			end_action_date		= Bean.getDecodeParam(parameters.get("end_action_date")),
			value_tax			= Bean.getDecodeParam(parameters.get("value_tax")),
			cd_currency			= Bean.getDecodeParam(parameters.get("cd_currency")),
			cd_tax_value_type	= Bean.getDecodeParam(parameters.get("cd_tax_value_type"));
	    
	    if (action.equalsIgnoreCase("add")) { 
			
			String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_TAX.add_tax_value(?,?,?,?,?,?,?,?,?,?)}";

			String[] pParam = new String [8];

			pParam[0] = id;
			pParam[1] = cd_tax_value_type;
			pParam[2] = cd_currency;
			pParam[3] = value_tax;
			pParam[4] = begin_action_date;
			pParam[5] = end_action_date;
			pParam[6] = id_club;
			pParam[7] = Bean.getDateFormat();
			
		 	%>
			<%= Bean.getCallResultParam("INSERT", callSQL, pParam, "../crm/finance/taxspecs.jsp?id=" + id + "&id_value=" , "") %>
			<% 	

	    } else if (action.equalsIgnoreCase("remove")) { 
	    	
	    	String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_TAX.delete_tax_value('"+
	    		id_value + "',?)}";

			String[] pParam = new String [1];

			pParam[0] = id_value;
			
	 		%>
			<%= Bean.getCallResultParam("DELETE", callSQL, pParam, "../crm/finance/taxspecs.jsp?id=" + id , "") %>
			<% 	
	 	
	    } else if (action.equalsIgnoreCase("edit")) { 
			
			String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_TAX.update_tax_value(?,?,?,?,?,?,?,?)}";

			String[] pParam = new String [7];

			pParam[0] = id_value;
			pParam[1] = cd_tax_value_type;
			pParam[2] = cd_currency;
			pParam[3] = value_tax;
			pParam[4] = begin_action_date;
			pParam[5] = end_action_date;
			pParam[6] = Bean.getDateFormat();
			
	 		%>
			<%= Bean.getCallResultParam("UPDATE", callSQL, pParam, "../crm/finance/taxspecs.jsp?id=" + id, "") %>
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
<%@page import="bc.objects.bcClubShortObject"%>
<%@page import="bc.objects.bcTaxValueObject"%></html>
