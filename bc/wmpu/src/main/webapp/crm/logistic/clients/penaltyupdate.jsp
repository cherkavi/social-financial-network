<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%= Bean.getLogOutScript(request) %>

<%@page import="java.util.HashMap"%>
<html>
	<%= Bean.getMetaContent() %>
	<%= Bean.getBottomFrameCSS() %>
<%
HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String pageFormName = "LOGISTIC_CLIENTS_PENALTY";

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
	if (process.equalsIgnoreCase("no"))
	/* вибираємо тип дії (добавити, видалити...)*/
	{
	   /*  --- Добавити запис --- */
    	if (action.equalsIgnoreCase("add") || action.equalsIgnoreCase("add2")) { %>
	<script>
		var formData = new Array (
			new Array ('name_lg_promoter', 'varchar2', 1),
			new Array ('reason_lg_promoter_penalty', 'varchar2', 1),
			new Array ('date_lg_promoter_penalty', 'varchar2', 1),
			new Array ('value_lg_promoter_penalty', 'varchar2', 1)
		);
	</script> 
		<%= Bean.getOperationTitle(
	    		Bean.logisticXML.getfieldTransl("h_action_penalty_add", false),
				"N",
				"N") 
		%>
    <form action="../crm/logistic/clients/penaltyupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">
        <input type="hidden" name="action" value="add">
        <input type="hidden" name="process" value="yes">
    	<input type="hidden" name="type" value="general">
	<table <%=Bean.getTableDetailParam() %>>
 		<tr>
			<td><%= Bean.logisticXML.getfieldTransl("name_lg_promoter", true) %></td><td><%=Bean.getWindowFindLGPromoter("lg_promoter", "", "", "", "50") %></td>
			<td><%= Bean.logisticXML.getfieldTransl("date_lg_promoter_penalty", true) %></td><td><%=Bean.getCalendarInputField("date_lg_promoter_penalty", Bean.getSysDate(), "16") %></td>
		</tr>
 		<tr>
			<td><%= Bean.logisticXML.getfieldTransl("reason_lg_promoter_penalty", true) %></td> <td><textarea name="reason_lg_promoter_penalty" cols="56" rows="3" class="inputfield"></textarea></td>
			<td><%= Bean.logisticXML.getfieldTransl("value_lg_promoter_penalty", true) %></td><td><input type="text" name="value_lg_promoter_penalty" size="20" value="" class="inputfield"></td>
		</tr>
 		<tr>
			<td colspan="6" align="center">
				<%=Bean.getSubmitButtonAjax("../crm/logistic/clients/penaltyupdate.jsp") %>
				<%=Bean.getResetButton() %>
				<% if ("add".equalsIgnoreCase(action)) { %>
					<%=Bean.getGoBackButton("../crm/logistic/clients/penalty.jsp") %>
				<% } else { %>
					<%=Bean.getGoBackButton("../crm/logistic/clients/penaltyspecs.jsp?id=" + id) %>
				<% } %>
			</td>
		</tr>

	</table>
	
	</form>
	<!-- Скрипт для втавки меню вибору дати -->
	<%= Bean.getCalendarScript("date_lg_promoter_penalty", true) %>

	        <%
    	} else {
    	    %> <%= Bean.getUnknownActionText(action) %><%
    	}
	
	} else if (process.equalsIgnoreCase("yes"))	{
    	
		String
			id_lg_promouter_penalty		= Bean.getDecodeParam(parameters.get("id_lg_promouter_penalty")),
			id_lg_promoter				= Bean.getDecodeParam(parameters.get("id_lg_promoter")),
			date_lg_promoter_penalty	= Bean.getDecodeParam(parameters.get("date_lg_promoter_penalty")),
			value_lg_promoter_penalty	= Bean.getDecodeParam(parameters.get("value_lg_promoter_penalty")),
			reason_lg_promoter_penalty	= Bean.getDecodeParam(parameters.get("reason_lg_promoter_penalty"));

    	if (action.equalsIgnoreCase("add")) { 
    		String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_LG_CARD.add_promoter_penalty(?,?,?,?,?,?,?)}";

    		String[] pParam = new String [5];

    		pParam[0] = id_lg_promoter;
    		pParam[1] = date_lg_promoter_penalty;
    		pParam[2] = value_lg_promoter_penalty;
    		pParam[3] = reason_lg_promoter_penalty;
    		pParam[4] = Bean.getDateFormat();
		
		 	%>
			<%= Bean.getCallResultParam("INSERT", callSQL, pParam, "../crm/logistic/clients/penaltyspecs.jsp?id=" , "../crm/logistic/clients/penalty.jsp") %>
			<% 	

    	} else if (action.equalsIgnoreCase("remove")) { 
    		
    		String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_LG_CARD.delete_promoter_penalty(?,?)}";

			String[] pParam = new String [5];

			pParam[0] = id;

		 	%>
			<%= Bean.getCallResultParam("DELETE", callSQL, pParam, "../crm/logistic/clients/penalty.jsp" , "") %>
			
			<% 	

    	} else if (action.equalsIgnoreCase("edit")) { 
    		String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_LG_CARD.update_promoter_penalty(?,?,?,?,?,?,?)}";

        	String[] pParam = new String [5];

        	pParam[0] = id;
        	pParam[1] = date_lg_promoter_penalty;
        	pParam[2] = value_lg_promoter_penalty;
        	pParam[3] = reason_lg_promoter_penalty;
        	pParam[4] = Bean.getDateFormat();
		
		 	%>
			<%= Bean.getCallResultParam("UPDATE", callSQL, pParam, "../crm/logistic/clients/penaltyspecs.jsp?id=" + id, "../crm/logistic/clients/penaltyspecs.jsp?id=" + id) %>
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


</html>
