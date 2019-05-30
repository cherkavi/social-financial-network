<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%= Bean.getLogOutScript(request) %>

<%@page import="java.util.HashMap"%>
<html>
	<%= Bean.getMetaContent() %>
	<%= Bean.getBottomFrameCSS() %>
<%
HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String pageFormName = "LOGISTIC_CLIENTS_PAYMENTS";

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
    	if (action.equalsIgnoreCase("run") || action.equalsIgnoreCase("run2")) { %>
		<%= Bean.getOperationTitle(
	    		Bean.logisticXML.getfieldTransl("h_action_promoter_payment_add", false),
				"N",
				"N") 
		%>
    <form action="../crm/logistic/clients/paymentupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">
        <input type="hidden" name="action" value="run">
        <input type="hidden" name="process" value="yes">
    	<input type="hidden" name="type" value="general">
	<table <%=Bean.getTableDetailParam() %>>
		<tr>
			<td width="30%"><%= Bean.logisticXML.getfieldTransl("cd_lg_promoter_pay_kind", true) %></td><td><%=Bean.getLogisticPromoterPayKindRadio("cd_lg_promoter_pay_kind", "CASHIER_PAYMENT") %></td>
			<td colspan="2"></td>			
		</tr>
		<tr>
			<td><%= Bean.logisticXML.getfieldTransl("date_begin_sale", false) %></td><td><%=Bean.getCalendarInputField("date_begin_sale", "", "10") %></td>
			<td colspan="2"></td>			
		</tr>
		<tr>
			<td><%= Bean.logisticXML.getfieldTransl("date_end_sale", false) %></td><td><%=Bean.getCalendarInputField("date_end_sale", Bean.getSysDate(), "10") %></td>
			<td colspan="2">&nbsp;</td>
		</tr>
 		<tr>
			<td colspan="6" align="center">
				<%=Bean.getSubmitButtonAjax("../crm/logistic/clients/paymentupdate.jsp") %>
				<%=Bean.getResetButton() %>
				<% if ("run".equalsIgnoreCase(action)) { %>
					<%=Bean.getGoBackButton("../crm/logistic/clients/payment.jsp") %>
				<% } else { %>
					<%=Bean.getGoBackButton("../crm/logistic/clients/paymentspecs.jsp?id=" + id) %>
				<% } %>
			</td>
		</tr>

	</table>
	
	</form>
	<!-- Скрипт для втавки меню вибору дати -->
	<%= Bean.getCalendarScript("date_begin_sale", false) %>
	<%= Bean.getCalendarScript("date_end_sale", false) %>

	        <%
    	} else {
    	    %> <%= Bean.getUnknownActionText(action) %><%
    	}
	
	} else if (process.equalsIgnoreCase("yes"))	{
    	
		String
			cd_lg_promoter_pay_kind		= Bean.getDecodeParam(parameters.get("cd_lg_promoter_pay_kind")),
			id_lg_promoter_payment		= Bean.getDecodeParam(parameters.get("id_lg_promoter_payment")),
			date_begin_sale				= Bean.getDecodeParam(parameters.get("date_begin_sale")),
			date_end_sale				= Bean.getDecodeParam(parameters.get("date_end_sale"));

    	if (action.equalsIgnoreCase("run")) { 
    		String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_LG_CARD.add_promoter_payment(?,?,?,?,?,?)}";

    		String[] pParam = new String [4];

    		pParam[0] = cd_lg_promoter_pay_kind;
    		pParam[1] = date_begin_sale;
    		pParam[2] = date_end_sale;
    		pParam[3] = Bean.getDateFormat();
		
		 	%>
			<%= Bean.getCallResultParam("INSERT", callSQL, pParam, "../crm/logistic/clients/paymentspecs.jsp?id=" , "../crm/logistic/clients/payment.jsp") %>
			<% 	

    	} else if (action.equalsIgnoreCase("remove")) { 
    		
    		String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_LG_CARD.delete_promoter_payment(?,?)}";

			String[] pParam = new String [1];

			pParam[0] = id;

		 	%>
			<%= Bean.getCallResultParam("DELETE", callSQL, pParam, "../crm/logistic/clients/payment.jsp" , "") %>
			
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
