<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%= Bean.getLogOutScript(request) %>

<%@page import="java.util.HashMap"%>
<%@page import="bc.objects.bcLGObject"%>

<%@page import="bc.objects.bcLGPromoterPaymentObject"%><html>
<head>
	<%= Bean.getMetaContent() %>
	<%= Bean.getBottomFrameCSS() %>
</head>
<body topmargin="0">
<div id="div_tabsheet">

<%

HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String pageFormName = "LOGISTIC_CLIENTS_PAYMENTS";

String tagLine = "_LINES";
String tagLineFind = "_LINES_FIND";
String tagQuestionnaire = "_QUESTIONNAIRE";
String tagQuestionnaireFind = "_QUESTIONNAIRE_FIND";
String tagTask = "_TASK";
String tagTaskFind = "_TASK_FIND";
String tagMessage = "_MESSAGE";
String tagMessageFind = "_MESSAGE_FIND";


Bean.setJspPageForTabName(pageFormName);

String id = Bean.getDecodeParam(parameters.get("id"));
String id_club = Bean.getDecodeParam(parameters.get("id_club"));
String	tab = Bean.getDecodeParam(parameters.get("tab"));
if (tab==null || "".equalsIgnoreCase(tab)) { 
	tab = Bean.tabsHmGetValue(pageFormName);
}

if (id==null || "".equals(id)) {
%>

	<%=Bean.getIDNotFoundMessage() %>

<% } else {
	Bean.tabsHmSetValue(pageFormName, tab);
	bcLGPromoterPaymentObject payment = new bcLGPromoterPaymentObject(id);

	String l_line_page = Bean.getDecodeParam(parameters.get("line_page"));
	Bean.pageCheck(pageFormName + tagLine, l_line_page);
	String l_line_page_beg = Bean.getFirstRowNumber(pageFormName + tagLine);
	String l_line_page_end = Bean.getLastRowNumber(pageFormName + tagLine);

	String line_find 	= Bean.getDecodeParam(parameters.get("line_find"));
	line_find 	= Bean.checkFindString(pageFormName + tagLineFind, line_find, l_line_page);

	String l_quest_page = Bean.getDecodeParam(parameters.get("quest_page"));
	Bean.pageCheck(pageFormName + tagQuestionnaire, l_quest_page);
	String l_quest_page_beg = Bean.getFirstRowNumber(pageFormName + tagQuestionnaire);
	String l_quest_page_end = Bean.getLastRowNumber(pageFormName + tagQuestionnaire);

	String quest_find 	= Bean.getDecodeParam(parameters.get("quest_find"));
	quest_find 	= Bean.checkFindString(pageFormName + tagQuestionnaireFind, quest_find, l_quest_page);

	String l_task_page = Bean.getDecodeParam(parameters.get("task_page"));
	Bean.pageCheck(pageFormName + tagTask, l_task_page);
	String l_task_page_beg = Bean.getFirstRowNumber(pageFormName + tagTask);
	String l_task_page_end = Bean.getLastRowNumber(pageFormName + tagTask);

	String task_find 	= Bean.getDecodeParam(parameters.get("task_find"));
	task_find 	= Bean.checkFindString(pageFormName + tagTaskFind, task_find, l_task_page);

	String l_message_page = Bean.getDecodeParam(parameters.get("message_page"));
	Bean.pageCheck(pageFormName + tagMessage, l_message_page);
	String l_message_page_beg = Bean.getFirstRowNumber(pageFormName + tagMessage);
	String l_message_page_end = Bean.getLastRowNumber(pageFormName + tagMessage);

	String message_find 	= Bean.getDecodeParam(parameters.get("message_find"));
	message_find 	= Bean.checkFindString(pageFormName + tagMessageFind, message_find, l_message_page);
%>

	<table <%=Bean.getTableMenuParam() %>>
		<tr>
			<%= Bean.getPageHeader() %>
			<% if (Bean.currentMenu.isCurrentTabAndEditPermitted("LOGISTIC_CLIENTS_PAYMENTS_INFO")) {%>
				<%= Bean.getMenuButton("RUN", "../crm/logistic/clients/paymentupdate.jsp?id=" + id + "&type=general&action=run2&process=no", "", "") %>
				<%= Bean.getMenuButton("DELETE", "../crm/logistic/clients/paymentupdate.jsp?id=" + id + "&type=general&action=remove&process=yes", Bean.logisticXML.getfieldTransl("h_action_promoter_payment_delete", false), payment.getValue("DATE_LG_PROMOTER_PAYMENT_FRMT") + " - " +  payment.getValue("NAME_LG_PROMOTER_PAY_KIND")) %>
			<% } %>

			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("LOGISTIC_CLIENTS_PAYMENTS_DETAIL")) {%>
				<!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagLine, "../crm/logistic/clients/paymentspecs.jsp?id=" + id + "&id_club=" + id_club + "&tab="+Bean.currentMenu.getTabID("LOGISTIC_CLIENTS_PAYMENTS_DETAIL") + "&", "line_page") %>
			<% } %>

			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("LOGISTIC_CLIENTS_PAYMENTS_QUESTIONNAIRES")) {%>
				<!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagQuestionnaire, "../crm/logistic/clients/paymentspecs.jsp?id=" + id + "&id_club=" + id_club + "&tab="+Bean.currentMenu.getTabID("LOGISTIC_CLIENTS_PAYMENTS_QUESTIONNAIRES") + "&", "quest_page") %>
			<% } %>

			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("LOGISTIC_CLIENTS_PAYMENTS_BON_CARD_TASKS")) {%>
				<!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagTask, "../crm/logistic/clients/paymentspecs.jsp?id=" + id + "&id_club=" + id_club + "&tab="+Bean.currentMenu.getTabID("LOGISTIC_CLIENTS_PAYMENTS_BON_CARD_TASKS") + "&", "task_page") %>
			<% } %>

			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("LOGISTIC_CLIENTS_PAYMENTS_MESSAGES")) {%>
				<!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagMessage, "../crm/logistic/clients/paymentspecs.jsp?id=" + id + "&id_club=" + id_club + "&tab="+Bean.currentMenu.getTabID("LOGISTIC_CLIENTS_PAYMENTS_MESSAGES") + "&", "message_page") %>
			<% } %>

		</tr>
	</table>
	<table <%=Bean.getTableMenuParam() %>>
		<%= Bean.getDetailCaption(payment.getValue("DATE_LG_PROMOTER_PAYMENT_FRMT") + " - " +  payment.getValue("NAME_LG_PROMOTER_PAY_KIND")) %>
		<tr>
			<td>
				<!-- Выводим перечень закладок -->
				<%= Bean.currentMenu.getFormTabScheeds(tab, "../crm/logistic/clients/paymentspecs.jsp?id=" + id + "&id_club=" + id_club) %>
			</td>
		</tr>
	</table>
</div>
<div id="div_data">
<div id="div_data_detail">
<%
if (Bean.currentMenu.isCurrentTabAndAccessPermitted("LOGISTIC_CLIENTS_PAYMENTS_INFO")) {
	boolean hasInfoEditPermission = false;
	if (Bean.currentMenu.isCurrentTabAndEditPermitted("LOGISTIC_CLIENTS_PAYMENTS_INFO")) {
		hasInfoEditPermission = true;
	}
	hasInfoEditPermission = false;
%>
    <form>
	<table <%=Bean.getTableDetailParam() %>>

		<tr>
			<td><%= Bean.logisticXML.getfieldTransl("id_lg_promoter_payment", false) %></td> <td><input type="text" name="id_lg_promoter_payment" size="20" value="<%=payment.getValue("ID_LG_PROMOTER_PAYMENT") %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.logisticXML.getfieldTransl("promoters_count", false) %></td> <td><input type="text" name="promoters_count" size="20" value="<%=payment.getValue("PROMOTERS_COUNT") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td><%= Bean.logisticXML.getfieldTransl("date_lg_promoter_payment", false) %></td> <td><input type="text" name="date_lg_promoter_payment" size="20" value="<%=payment.getValue("DATE_LG_PROMOTER_PAYMENT_FRMT") %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.logisticXML.getfieldTransl("sales_cards_count", false) %></td> <td><input type="text" name="sales_cards_count" size="20" value="<%=payment.getValue("SALES_CARDS_COUNT") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td><%= Bean.logisticXML.getfieldTransl("name_lg_promoter_pay_state", false) %></td> <td><input type="text" name="name_lg_promoter_pay_state" size="50" value="<%=payment.getValue("NAME_LG_PROMOTER_PAY_STATE") %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.logisticXML.getfieldTransl("amount_all_payment", false) %></td> <td><input type="text" name="amount_all_payment" size="20" value="<%=payment.getValue("AMOUNT_ALL_PAYMENT_FRMT") %> <%=payment.getValue("SNAME_CURRENCY") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td><%= Bean.logisticXML.getfieldTransl("name_lg_promoter_pay_kind", false) %></td> <td><input type="text" name="name_lg_promoter_pay_kind" size="50" value="<%=payment.getValue("NAME_LG_PROMOTER_PAY_KIND") %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.logisticXML.getfieldTransl("amount_penalty", false) %></td> <td><input type="text" name="amount_penalty" size="20" value="<%=payment.getValue("AMOUNT_PENALTY_FRMT") %> <%=payment.getValue("SNAME_CURRENCY") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td><%= Bean.logisticXML.getfieldTransl("date_begin_sale_period", false) %></td> <td><input type="text" name="date_begin_sale_period" size="20" value="<%=payment.getValue("DATE_BEGIN_SALE_PERIOD_FRMT") %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.logisticXML.getfieldTransl("amount_currency_payment", false) %></td> <td><input type="text" name="amount_currency_paymen" size="20" value="<%=payment.getValue("AMOUNT_CURRENCY_PAYMENT_FRMT") %> <%=payment.getValue("SNAME_CURRENCY") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td><%= Bean.logisticXML.getfieldTransl("date_end_sale_period", false) %></td> <td><input type="text" name="date_end_sale_period" size="20" value="<%=payment.getValue("DATE_END_SALE_PERIOD_FRMT") %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.logisticXML.getfieldTransl("amount_bon_payment", false) %></td> <td><input type="text" name="amount_bon_payment" size="20" value="<%=payment.getValue("AMOUNT_BON_PAYMENT_FRMT") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<%=	Bean.getCreationAndMoficationRecordFields(
				payment.getValue(Bean.getCreationDateFieldName()),
				payment.getValue("CREATED_BY"),
				payment.getValue(Bean.getLastUpdateDateFieldName()),
				payment.getValue("LAST_UPDATE_BY")
			) %>
		<tr>
			<td colspan="6" align="center">
				<%=Bean.getResetButton() %>
				<%=Bean.getGoBackButton("../crm/logistic/clients/delivery_point.jsp") %>
			</td>
		</tr>
	</table>
	</form>


<% }

	if (Bean.currentMenu.isCurrentTabAndAccessPermitted("LOGISTIC_CLIENTS_PAYMENTS_DETAIL")) { %> 
		<table <%= Bean.getTableBottomFilter() %>>
			<tr>
			<%= Bean.getFindHTML("line_find", line_find, "../crm/logistic/clients/delivery_pointspecs.jsp?id=" + id + "&id_club=" + id_club + "&line_page=1") %>
	
			<td>&nbsp;</td>
	
			</tr>
		</table> 
		<%= payment.getPaymentLinesHTML(line_find, "", l_line_page_beg, l_line_page_end) %> 
	<%}

	if (Bean.currentMenu.isCurrentTabAndAccessPermitted("LOGISTIC_CLIENTS_PAYMENTS_QUESTIONNAIRES")) { %> 
		<table <%= Bean.getTableBottomFilter() %>>
			<tr>
			<%= Bean.getFindHTML("quest_find", quest_find, "../crm/logistic/clients/delivery_pointspecs.jsp?id=" + id + "&id_club=" + id_club + "&quest_page=1") %>
	
			<td>&nbsp;</td>
	
			</tr>
		</table> 
		 
	<%}
	
		if (Bean.currentMenu.isCurrentTabAndAccessPermitted("LOGISTIC_CLIENTS_PAYMENTS_BON_CARD_TASKS")) { %> 
		<table <%= Bean.getTableBottomFilter() %>>
			<tr>
			<%= Bean.getFindHTML("task_find", task_find, "../crm/logistic/clients/delivery_pointspecs.jsp?id=" + id + "&id_club=" + id_club + "&task_page=1") %>
	
			<td>&nbsp;</td>
	
			</tr>
		</table> 
		 
	<%}
	
		if (Bean.currentMenu.isCurrentTabAndAccessPermitted("LOGISTIC_CLIENTS_PAYMENTS_MESSAGES")) { %> 
		<table <%= Bean.getTableBottomFilter() %>>
			<tr>
			<%= Bean.getFindHTML("message_find", message_find, "../crm/logistic/clients/delivery_pointspecs.jsp?id=" + id + "&id_club=" + id_club + "&message_page=1") %>
	
			<td>&nbsp;</td>
	
			</tr>
		</table> 
		 
	<%}

}
		
%>
</div></div>
</body>

</html>
