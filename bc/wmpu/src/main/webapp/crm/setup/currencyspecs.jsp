<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />
<%@page import="bc.objects.bcCurrencyObject"%>
<%@page import="java.util.HashMap"%><html>

<%= Bean.getLogOutScript(request) %>

<head>
	<%= Bean.getMetaContent() %>
	<%= Bean.getBottomFrameCSS() %>

<title></title>
</head>
<body>
<div id="div_tabsheet">

<% 

HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String pageFormName = "SETUP_CURRENCY";

String tagDailyRates = "_DAILY_RATES";
String tagDailyRatesFind = "_DAILY_RATES_FIND";

Bean.setJspPageForTabName(pageFormName);

String cd = Bean.getDecodeParam(parameters.get("id"));
String	tab = Bean.getDecodeParam(parameters.get("tab"));
String	category = "currency";
if (tab==null || "".equalsIgnoreCase(tab)) { tab = Bean.tabsHmGetValue(pageFormName); }

if (cd==null || "".equalsIgnoreCase(cd)) {
%>
	<%=Bean.getIDNotFoundMessage() %>
<% 
}else {
	Bean.tabsHmSetValue(pageFormName, tab);
	bcCurrencyObject currency = new bcCurrencyObject(cd);
	
	//Обрабатываем номера страниц
	String l_daily_rates_page = Bean.getDecodeParam(parameters.get("daily_rates_page"));
	Bean.pageCheck(pageFormName + tagDailyRates, l_daily_rates_page);
	String l_daily_rates_page_beg = Bean.getFirstRowNumber(pageFormName + tagDailyRates);
	String l_daily_rates_page_end = Bean.getLastRowNumber(pageFormName + tagDailyRates);
	
	String daily_rates_find 	= Bean.getDecodeParam(parameters.get("daily_rates_find"));
	daily_rates_find 	= Bean.checkFindString(pageFormName + tagDailyRatesFind, daily_rates_find, l_daily_rates_page);

%>

	<table <%=Bean.getTableMenuParam() %>>
		<tr>
			<%= Bean.getPageHeader() %>

			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("SETUP_CURRENCY_EXCHANGE")) { %>
				<% if (Bean.currentMenu.isCurrentTabAndEditPermitted("SETUP_CURRENCY_EXCHANGE")) { %>
				    <%= Bean.getMenuButton("ADD", "../crm/setup/currencyupdate.jsp?id=" + cd + "&type=rate&action=add&process=no", "", "") %>
				<% } %>
	
			    <!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagDailyRates, "../crm/setup/currencyspecs.jsp?id=" + cd + "&tab="+Bean.currentMenu.getTabID("SETUP_CURRENCY_EXCHANGE")+"&", "daily_rates_page") %>
			<% } %>
		</tr>
	</table>
	<table <%=Bean.getTableMenuParam() %>>
		<%= Bean.getDetailCaption(currency.getValue("CD_CURRENCY") + " - " + currency.getValue("NAME_CURRENCY")) %>
		<tr>
			<td>
				<!-- Выводим перечень закладок -->
				<%= Bean.currentMenu.getFormTabScheeds(tab, "../crm/setup/currencyspecs.jsp?id=" + cd) %>
			</td>
		</tr>
	</table>
</div>
<div id="div_data">
<div id="div_data_detail">

<%

if (Bean.currentMenu.isCurrentTabAndAccessPermitted("SETUP_CURRENCY_INFO")) {
	boolean hasEditPerm = false;
	if (Bean.currentMenu.isCurrentTabAndEditPermitted("SETUP_CURRENCY_INFO")) {
		hasEditPerm = true;
	}
   %>
	<%if (hasEditPerm) { %>

		<script>
			var formData = new Array (
				new Array ('cd_currency_in_telgr_hex', 'varchar2', 1)
			);
		</script>

	  <form action="../crm/setup/currencyupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">
	        <input type="hidden" name="type" value="currency">
	        <input type="hidden" name="action" value="edit">
	        <input type="hidden" name="process" value="yes">
	<%} %>
	<table <%=Bean.getTableDetailParam() %>>
		<tr>
			<td><%= Bean.currencyXML.getfieldTransl("cd_currency", false) %> </td><td><input type="text" name="cd_currency" size="20" value="<%= currency.getValue("CD_CURRENCY") %>" readonly="readonly" class="inputfield-ro"></td>
			<%if (hasEditPerm) { %>
			  	<td><%= Bean.currencyXML.getfieldTransl("is_used", true) %></td> <td><select name="is_used" class="inputfield"><%= Bean.getMeaningFromLookupNumHTML("YES_NO", currency.getValue("IS_USED"), false) %></select> </td>
			<% } else { %>
			  	<td><%= Bean.currencyXML.getfieldTransl("is_used", false) %></td> <td><input type="text" name="is_used" size="16" value="<%= currency.getValue("IS_USED_TSL") %>" readonly="readonly" class="inputfield-ro"> </td>
			<% } %>  
			
		</tr>
		<tr>
			<td><%= Bean.currencyXML.getfieldTransl("name_currency", false) %> </td> <td><input type="text" name="name_currency" size="60" value="<%= currency.getValue("NAME_CURRENCY") %>" readonly="readonly" class="inputfield-ro"></td>
			<%if (hasEditPerm) { %>
  			  <td><%= Bean.currencyXML.getfieldTransl("cd_currency_in_telgr_hex", true) %></td> <td><input type="text" name="cd_currency_in_telgr_hex" size="16" value="<%= currency.getValue("CD_CURRENCY_IN_TELGR_HEX") %>" class="inputfield"> </td>
  			<% } else { %>
			  <td><%= Bean.currencyXML.getfieldTransl("cd_currency_in_telgr_hex", false) %></td> <td><input type="text" name="cd_currency_in_telgr_hex" size="16" value="<%= currency.getValue("CD_CURRENCY_IN_TELGR_HEX") %>" readonly="readonly" class="inputfield-ro"> </td>
			<% } %>    
		</tr>
		<tr>
			<td><%= Bean.currencyXML.getfieldTransl("sname_currency", false) %></td> <td><input type="text" name="sname_currency" size="60" value="<%= currency.getValue("SNAME_CURRENCY") %>" readonly="readonly" class="inputfield-ro"> </td>
			<td colspan="2">&nbsp;</td>
		</tr>
		<tr>
			<td><%= Bean.currencyXML.getfieldTransl("scd_currency", false) %></td> <td><input type="text" name="scd_currency" size="20" value="<%= currency.getValue("SCD_CURRENCY") %>" readonly="readonly" class="inputfield-ro"> </td>
			<td colspan="2">&nbsp;</td>
		</tr>
		<%=	Bean.getCreationAndMoficationRecordFields(
				currency.getValue(Bean.getCreationDateFieldName()),
				currency.getValue("CREATED_BY"),
				currency.getValue(Bean.getLastUpdateDateFieldName()),
				currency.getValue("LAST_UPDATE_BY")
			) %>
		<%if (hasEditPerm) { %>
		<tr>
			<td colspan="6" align="center">
				<%=Bean.getSubmitButtonAjax("../crm/setup/currencyupdate.jsp") %>
				<%=Bean.getResetButton() %>
				<%=Bean.getGoBackButton("../crm/setup/currency.jsp") %>
			</td>
		</tr>
		<% } else {%>
		<tr>
			<td colspan="6" align="center">
				<%=Bean.getGoBackButton("../crm/setup/currency.jsp") %>
			</td>
		</tr>
		<% } %>
	</table>
	</form>

<%  }
if (Bean.currentMenu.isCurrentTabAndAccessPermitted("SETUP_CURRENCY_EXCHANGE")) { %>
	<table <%=Bean.getTableBottomFilter() %>>
	  <tr>
		<%= Bean.getFindHTML("daily_rates_find", daily_rates_find, "../crm/setup/currencyspecs.jsp?id=" + cd + "&daily_ratess_page=1&") %>

		<td>&nbsp;</td>

	  </tr>
	</table>
 <%= currency.getCurrencyExangeRatesHTML(daily_rates_find, l_daily_rates_page_beg, l_daily_rates_page_end) %>
 <%
} 
}%>
</div></div>
</body>
</html>
