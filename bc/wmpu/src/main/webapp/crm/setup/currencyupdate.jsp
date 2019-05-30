<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />
<%@page import="bc.objects.bcDailyRateObject"%>
<%@page import="java.util.HashMap"%>
<%@page import="bc.objects.bcCurrencyObject"%>
<%@page import="java.util.ArrayList"%><html>

<%= Bean.getLogOutScript(request) %>

<head>
	<%= Bean.getMetaContent() %>
	<%= Bean.getBottomFrameCSS() %>
</head>

<body>
<%
HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String pageFormName = "SETUP_CURRENCY";

Bean.setJspPageForTabName(pageFormName);

request.setCharacterEncoding("UTF-8");

String id		= Bean.getDecodeParam(parameters.get("id")); 
String action	= Bean.getDecodeParam(parameters.get("action")); 
String process	= Bean.getDecodeParam(parameters.get("process"));
String type 	= Bean.getDecodeParam(parameters.get("type"));

if (id==null || ("".equalsIgnoreCase(id))) id="empty";
if (type==null || ("".equalsIgnoreCase(type))) type="empty";
if (action==null || ("".equalsIgnoreCase(action))) action="empty";
if (process==null || ("".equalsIgnoreCase(process))) process="empty";

if (type.equalsIgnoreCase("currency")) {
	
	if (process.equalsIgnoreCase("yes")){
    
		String
    		cd_currency 	= Bean.getDecodeParam(parameters.get("cd_currency")),
    		is_used 		= Bean.getDecodeParam(parameters.get("is_used")),
    		currency_hex 	= Bean.getDecodeParam(parameters.get("cd_currency_in_telgr_hex"));
    
    	if (action.equalsIgnoreCase("edit")) { 
    		
    		String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK$SYS_CURRENCY_UI.set_currency_param(?,?,?,?)}";

    		ArrayList<String> pParam = new ArrayList<String>();

    		pParam.add(cd_currency);
    		pParam.add(is_used);
    		pParam.add(currency_hex);
	
   		 	%>
   			<%= Bean.executeUpdateFunction("PACK$SYS_CURRENCY_UI.set_currency_param", pParam, "../crm/setup/currencyspecs.jsp?id=" + cd_currency, "") %>
   			<% 	

    	} else {%> 
			<%= Bean.getUnknownActionText(action) %><%
      	}

	} else {%> 
		<%= Bean.getUnknownProcessText(process) %> <%
  	}
  
  
} else if (type.equalsIgnoreCase("rate")) {
	bcCurrencyObject currency = new bcCurrencyObject(id);

	if (process.equalsIgnoreCase("no")) {
    	if (action.equalsIgnoreCase("add")) {
		    		
    		String  l_from_currency		= Bean.getDecodeParam(parameters.get("from"));
    		l_from_currency 			= Bean.isEmpty(l_from_currency)?id:l_from_currency;
		    		
	        %>
			<%= Bean.getOperationTitle(
					Bean.daily_rateXML.getfieldTransl("h_rate_add", false),
					"Y",
					"N") 
			%>
	        <form action="../crm/setup/currencyupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST">
		        <input type="hidden" name="type" value="rate">
		        <input type="hidden" name="action" value="add">
		        <input type="hidden" name="process" value="yes">
		        <input type="hidden" name="id" value="<%=id %>">
			<table <%=Bean.getTableDetailParam() %>>
		        <tr>
					<td><%= Bean.daily_rateXML.getfieldTransl("from_currency", true) %></td><td><input type="text" name="from_currency" size="15" value="<%=currency.getValue("NAME_CURRENCY") %>" readonly="readonly" class="inputfield-ro"></td>
					<td><%= Bean.daily_rateXML.getfieldTransl("to_currency", true) %></td> <td><select name="to_currency"  class="inputfield"><%= Bean.getCurrencyOptions("", false) %></select> </td>
				</tr>
				<tr>
					<td><%= Bean.daily_rateXML.getfieldTransl("conversion_date", true) %></td> <td><%=Bean.getCalendarInputField("conversion_date", "", "10") %></td>
					<td><%= Bean.daily_rateXML.getfieldTransl("conversion_type", true) %></td> <td><input type="text" name="conversion_type" size="15" value="" class="inputfield"></td>
				</tr>
				<tr>
					<td><%= Bean.daily_rateXML.getfieldTransl("src_data", true) %></td> <td><select name="src_data" class="inputfield"><%= Bean.getMeaningFromLookupNameOptions("INPUT_DATA_SOURCE", "U", false) %></select></td>
					<td><%= Bean.daily_rateXML.getfieldTransl("conversion_rate", true) %></td> <td><input type="text" name="conversion_rate" size="15" value="" class="inputfield"></td>
				</tr>
		 		<tr>
					<td colspan="4" align="center">
						<%=Bean.getSubmitButtonAjax("../crm/setup/currencyupdate.jsp") %>
						<%=Bean.getResetButton() %>
						<%=Bean.getGoBackButton("../crm/setup/currencyspecs.jsp?id=" + l_from_currency) %>
					</td>
				</tr>

			</table>

		</form>
		<!-- Скрипт для втавки меню вибору дати -->
		<%= Bean.getCalendarScript("conversion_date", false) %>

			        <%
		    	} else if (action.equalsIgnoreCase("edit")) {
		    		
		    		String id_daily_rate		= Bean.getDecodeParam(parameters.get("id_daily_rate"));
		    		
		    		bcDailyRateObject dailyRate = new bcDailyRateObject(id_daily_rate);
		    		
		    	    %> 
				<%= Bean.getOperationTitle(
						Bean.daily_rateXML.getfieldTransl("h_rate_edit", false),
						"Y",
						"N") 
				%>

	        <form action="../crm/setup/currencyupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST">
		        <input type="hidden" name="type" value="rate">
		        <input type="hidden" name="action" value="edit">
		        <input type="hidden" name="process" value="yes">
		        <input type="hidden" name="id" value="<%=id %>">
		        <input type="hidden" name="id_daily_rate" value="<%=id_daily_rate %>">
			<table <%=Bean.getTableDetailParam() %>>
		        <tr>
					<td><%= Bean.daily_rateXML.getfieldTransl("from_currency", false) %></td><td><input type="text" name="from_currency_name" size="30" value="<%=dailyRate.getValue("FROM_CURRENCY_NAME") %>" readonly="readonly" class="inputfield-ro"></td>
					<td><%= Bean.daily_rateXML.getfieldTransl("to_currency", false) %></td> <td><input type="text" name="to_currency_name" size="30" value="<%=dailyRate.getValue("TO_CURRENCY_NAME") %>" readonly="readonly" class="inputfield-ro"></td>
				</tr>
				<tr>
					<td><%= Bean.daily_rateXML.getfieldTransl("conversion_date", true) %></td> <td><%=Bean.getCalendarInputField("conversion_date", dailyRate.getValue("CONVERSION_DATE_FRMT"), "10") %></td>
					<td><%= Bean.daily_rateXML.getfieldTransl("conversion_type", true) %></td> <td><input type="text" name="conversion_type" size="15" value="<%=dailyRate.getValue("CONVERSION_TYPE") %>" class="inputfield"></td>
				</tr>
				<tr>
					<td><%= Bean.daily_rateXML.getfieldTransl("src_data", true) %></td> <td><select name="src_data" class="inputfield"><%= Bean.getMeaningFromLookupNameOptions("INPUT_DATA_SOURCE", dailyRate.getValue("SRC_DATA"), false) %></select></td>
					<td><%= Bean.daily_rateXML.getfieldTransl("conversion_rate", true) %></td> <td><input type="text" name="conversion_rate" size="15" value="<%=dailyRate.getValue("CONVERSION_RATE") %>" class="inputfield"></td>
				</tr>
				<%=	Bean.getCreationAndMoficationRecordFields(
						dailyRate.getValue(Bean.getCreationDateFieldName()),
						dailyRate.getValue("CREATED_BY"),
						dailyRate.getValue(Bean.getLastUpdateDateFieldName()),
						dailyRate.getValue("LAST_UPDATE_BY")
					) %>
				<tr>
					<td colspan="4" align="center">
						<%=Bean.getSubmitButtonAjax("../crm/setup/currencyupdate.jsp") %>
						<%=Bean.getResetButton() %>
						<%=Bean.getGoBackButton("../crm/setup/currencyspecs.jsp?id=" + id) %>
					</td>
				</tr>

			</table>

		</form> 
		<%= Bean.getCalendarScript("conversion_date", false) %>

		<br><%
		} else {
		    %> <%= Bean.getUnknownActionText(action) %><%
		}
	} else if (process.equalsIgnoreCase("yes")){
		
		String
			id_daily_rate		= Bean.getDecodeParam(parameters.get("id_daily_rate")),
			from_currency		= Bean.getDecodeParam(parameters.get("from_currency")),
	    	to_currency 		= Bean.getDecodeParam(parameters.get("to_currency")),
	    	src_data 			= Bean.getDecodeParam(parameters.get("src_data")),
	    	conversion_date	= Bean.getDecodeParam(parameters.get("conversion_date")),
	    	conversion_type 	= Bean.getDecodeParam(parameters.get("conversion_type")),
	    	conversion_rate 	= Bean.getDecodeParam(parameters.get("conversion_rate"));

		if (action.equalsIgnoreCase("add")) { 
		    
		    ArrayList<String> pParam = new ArrayList<String>();

		    pParam.add(id);
		    pParam.add(to_currency);
		    pParam.add(conversion_date);
		    pParam.add(conversion_type);
		    pParam.add(conversion_rate);
		    pParam.add(src_data);
		    pParam.add(Bean.getDateFormat());
			
   		 	%>
   			<%= Bean.executeInsertFunction("PACK$SYS_CURRENCY_UI.add_daily_rate", pParam, "../crm/setup/currencyspecs.jsp?id=" + id + "&id_daily_rate=", "") %>
   			<% 	

		} else if (action.equalsIgnoreCase("remove")) { 
			
			ArrayList<String> pParam = new ArrayList<String>();

			pParam.add(id_daily_rate);
			
			%>
   			<%= Bean.executeDeleteFunction("PACK$SYS_CURRENCY_UI.delete_daily_rate", pParam, "../crm/setup/currencyspecs.jsp?id=" + id, "") %>
   			<% 	

		} else if (action.equalsIgnoreCase("edit")) { 
			
			ArrayList<String> pParam = new ArrayList<String>();

			pParam.add(id_daily_rate);
			pParam.add(conversion_date);
			pParam.add(conversion_type);
			pParam.add(conversion_rate);
			pParam.add(src_data);
			pParam.add(Bean.getDateFormat());
				
   		 	%>
   			<%= Bean.executeUpdateFunction("PACK$SYS_CURRENCY_UI.update_daily_rate", pParam, "../crm/setup/currencyspecs.jsp?id=" + id, "") %>
   			<% 	

		} else { %> 
			<%= Bean.getUnknownActionText(action) %><% 
		}
	} else { %> 
		<%= Bean.getUnknownProcessText(process) %> <%
	}

} else { %> 
	<%= Bean.getUnknownTypeText(type) %> <%
}

%>


</body>
</html>
