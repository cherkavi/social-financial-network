<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />
<%@page import="bc.objects.bcClubObject"%>
<%@page import="java.util.HashMap"%>
<%@page import="bc.objects.bcClubOnlinePaymentTypeObject"%><html>

<%= Bean.getLogOutScript(request) %>

<head>
	<%= Bean.getMetaContent() %>
	<%= Bean.getBottomFrameCSS() %>
</head>

<%
HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String pageFormName = "CLUB_ONLINE_PAY_TYPE";

Bean.setJspPageForTabName(pageFormName);

String tagTerminal = "_TERMINAL";
String tagTerminalFind = "_TERMINAL_FIND";
String tagPayment = "_PAYMENT";
String tagPaymentFind = "_PAYMENT_FIND";

String	id 			= Bean.getDecodeParam(parameters.get("id"));
String	id_club 	= Bean.getDecodeParam(parameters.get("id_club"));
String	tab 		= Bean.getDecodeParam(parameters.get("tab"));

if (tab==null || "".equalsIgnoreCase(tab)) { tab = Bean.tabsHmGetValue(pageFormName); }
if (id==null || "".equalsIgnoreCase(id)) {
%>
	<%=Bean.getIDNotFoundMessage() %>
<% 
} else {
	Bean.tabsHmSetValue(pageFormName, tab);
	bcClubOnlinePaymentTypeObject pay = new bcClubOnlinePaymentTypeObject(id);
	
	String l_term_page = Bean.getDecodeParam(parameters.get("term_page"));
	Bean.pageCheck(pageFormName + tagTerminal, l_term_page);
	String l_term_page_beg = Bean.getFirstRowNumber(pageFormName + tagTerminal);
	String l_term_page_end = Bean.getLastRowNumber(pageFormName + tagTerminal);

	String term_find 	= Bean.getDecodeParam(parameters.get("term_find"));
	term_find 	= Bean.checkFindString(pageFormName + tagTerminalFind, term_find, l_term_page);
	
	String l_payment_page = Bean.getDecodeParam(parameters.get("payment_page"));
	Bean.pageCheck(pageFormName + tagPayment, l_payment_page);
	String l_payment_page_beg = Bean.getFirstRowNumber(pageFormName + tagPayment);
	String l_payment_page_end = Bean.getLastRowNumber(pageFormName + tagPayment);

	String payment_find 	= Bean.getDecodeParam(parameters.get("payment_find"));
	payment_find 	= Bean.checkFindString(pageFormName + tagPaymentFind, payment_find, l_payment_page);

%>
<% Bean.currentMenu.setCurrentTab(Bean.currentMenu.getTabSheetName(tab)); %>
<body>
<div id="div_tabsheet">

	<table <%=Bean.getTableMenuParam() %>>
		<tr>
			<%= Bean.getPageHeader() %>

			<% if (Bean.currentMenu.isCurrentTabAndEditPermitted("CLUB_ONLINE_PAY_TYPE_INFO")) { %>
			    <%= Bean.getMenuButton("ADD", "../crm/club/online_pay_typeupdate.jsp?id_club=" + pay.getValue("ID_CLUB") + "&id=" + id + "&type=general&action=add2&process=no", "", "") %>
				<%= Bean.getMenuButton("DELETE", "../crm/club/online_pay_typeupdate.jsp?id_club=" + pay.getValue("ID_CLUB") + "&id=" + id + "&type=general&action=remove&process=yes", Bean.buttonXML.getfieldTransl("delete", false), pay.getValue("ID_CLUB_ONLINE_PAY_TYPE") + " - " +  pay.getValue("NAME_CLUB_ONLINE_PAY_TYPE")) %>
			<%  } %>

			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLUB_ONLINE_PAY_TYPE_TERM")) { %>
			    <% if (Bean.currentMenu.isCurrentTabAndEditPermitted("CLUB_ONLINE_PAY_TYPE_TERM")) { %>
				    <%= Bean.getMenuButton("ADD", "../crm/club/online_pay_typeupdate.jsp?id_club=" + pay.getValue("ID_CLUB") + "&id=" + id + "&type=term&action=add&process=no", "", "") %>
				<% } %>
			    <!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagTerminal, "../crm/club/online_pay_typespecs.jsp?id_club=" + pay.getValue("ID_CLUB") + "&id=" + id + "&tab="+Bean.currentMenu.getTabID("CLUB_ONLINE_PAY_TYPE_TERM") + "&", "term_page") %>
			<% } %>

			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLUB_ONLINE_PAY_TYPE_PAYMENTS")) { %>
			    <!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagPayment, "../crm/club/online_pay_typespecs.jsp?id_club=" + pay.getValue("ID_CLUB") + "&id=" + id + "&tab="+Bean.currentMenu.getTabID("CLUB_ONLINE_PAY_TYPE_PAYMENTS") + "&", "payment_page") %>
			<% } %>

	
		</tr>
	</table>
	<table <%=Bean.getTableMenuParam() %>>
		<%= Bean.getDetailCaption(pay.getValue("ID_CLUB_ONLINE_PAY_TYPE") + " - " + pay.getValue("NAME_CLUB_ONLINE_PAY_TYPE")) %>
		<tr>
			<td>
	            <!-- Выводим перечень закладок -->
				<%= Bean.currentMenu.getFormTabScheeds(tab, "../crm/club/online_pay_typespecs.jsp?id_club=" + pay.getValue("ID_CLUB") + "&id=" + id) %>
			</td>
	</tr>
</table>
</div>
<div id="div_data">
<div id="div_data_detail">
<%

if (Bean.currentMenu.isCurrentTabAndEditPermitted("CLUB_ONLINE_PAY_TYPE_INFO")) { %>

	<script>
		var formClubParam = new Array (
			new Array ('name_club_online_pay_type', 'varchar2', 1),
			new Array ('term_card_req_club_pay_id_def', 'varchar2', 1),
			new Array ('exist_club_online_pay_type', 'varchar2', 1),
			new Array ('need_calc_pin', 'varchar2', 1)
		);
		function myValidateForm() {
			return validateForm(formClubParam);
		}
	</script>

	<form action="../crm/club/online_pay_typeupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formClubParam)">
		<input type="hidden" name="type" value="general">
	    <input type="hidden" name="action" value="update">
	    <input type="hidden" name="process" value="yes">
	    <input type="hidden" name="id" value="<%=id %>">
	    <input type="hidden" name="id_club" value="<%= pay.getValue("ID_CLUB") %>">
	<table <%=Bean.getTableDetailParam() %>>
		<tr>
			<td><%= Bean.clubXML.getfieldTransl("id_club_online_pay_type", false) %> </td><td><input type="text" name="id_club_online_pay_type" size="40" value="<%= pay.getValue("ID_CLUB_ONLINE_PAY_TYPE") %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.clubXML.getfieldTransl("club", false) %>
				<%= Bean.getGoToClubLink(pay.getValue("ID_CLUB")) %>
			</td>
			<td>
				<input type="text" name="name_club" size="35" value="<%= pay.getValue("SNAME_CLUB") %>" readonly="readonly" class="inputfield-ro">
			</td>
		</tr>
		<tr>
			<td><%= Bean.clubXML.getfieldTransl("name_club_online_pay_type", true) %> </td><td><input type="text" name="name_club_online_pay_type" size="60" value="<%= pay.getValue("NAME_CLUB_ONLINE_PAY_TYPE") %>" class="inputfield"></td>
			<td><%= Bean.clubXML.getfieldTransl("term_card_req_club_pay_id_def", true) %> </td><td><input type="text" name="term_card_req_club_pay_id_def" size="20" value="<%= pay.getValue("TERM_CARD_REQ_CLUB_PAY_ID_DEF") %>" class="inputfield"></td>
		</tr>
	    <tr>
			<td rowspan="3"><%= Bean.clubXML.getfieldTransl("desc_club_online_pay_type", false) %></td> <td rowspan="3"><textarea name="desc_club_online_pay_type" cols="57" rows="3" class="inputfield"><%= pay.getValue("DESC_CLUB_ONLINE_PAY_TYPE") %></textarea></td>
			<td><%= Bean.clubXML.getfieldTransl("need_calc_pin", true) %> </td><td><select name="need_calc_pin" class="inputfield"><%= Bean.getMeaningFromLookupNameOptions("YES_NO", pay.getValue("NEED_CALC_PIN"), true) %></select></td>
		</tr>
	    <tr>
			<td><%= Bean.clubXML.getfieldTransl("exist_club_online_pay_type", true) %> </td><td><select name="exist_club_online_pay_type" class="inputfield"><%= Bean.getMeaningFromLookupNameOptions("YES_NO", pay.getValue("EXIST_CLUB_ONLINE_PAY_TYPE"), true) %></select></td>
		</tr>
	    <tr>
			<td colspan="2">&nbsp;</td>
		</tr>
		<%=	Bean.getCreationAndMoficationRecordFields(
				pay.getValue(Bean.getCreationDateFieldName()),
				pay.getValue("CREATED_BY"),
				pay.getValue(Bean.getLastUpdateDateFieldName()),
				pay.getValue("LAST_UPDATE_BY")
			) %>
 		<tr>
			<td colspan="6" align="center">
				<%=Bean.getSubmitButtonAjax("../crm/club/online_pay_typeupdate.jsp") %>
				<%=Bean.getResetButton() %>
				<%=Bean.getGoBackButton("../crm/club/online_pay_type.jsp") %>
			</td>
		</tr>
	</table>
	</form>
<% } else if (Bean.currentMenu.isCurrentTabAndPreviewPermitted("CLUB_ONLINE_PAY_TYPE_INFO")) { %>
	<form>
	<% String outputdir = Bean.getSystemParamValue("UPLOADED_FILES_DIR");
	%>
	<table <%=Bean.getTableDetailParam() %>>
		<tr>
			<td><%= Bean.clubXML.getfieldTransl("id_club_online_pay_type", false) %> </td><td><input type="text" name="id_club_online_pay_type" size="40" value="<%= pay.getValue("ID_CLUB_ONLINE_PAY_TYPE") %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.clubXML.getfieldTransl("club", false) %>
				<%= Bean.getGoToClubLink(pay.getValue("ID_CLUB")) %>
			</td>
			<td>
				<input type="text" name="name_club" size="35" value="<%= pay.getValue("SNAME_CLUB") %>" readonly="readonly" class="inputfield-ro">
			</td>
		</tr>
		<tr>
			<td><%= Bean.clubXML.getfieldTransl("name_club_online_pay_type", true) %> </td><td><input type="text" name="name_club_online_pay_type" size="60" value="<%= pay.getValue("NAME_CLUB_ONLINE_PAY_TYPE") %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.clubXML.getfieldTransl("term_card_req_club_pay_id_def", true) %> </td><td><input type="text" name="term_card_req_club_pay_id_def" size="20" value="<%= pay.getValue("TERM_CARD_REQ_CLUB_PAY_ID_DEF") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
	    <tr>
			<td rowspan="3"><%= Bean.clubXML.getfieldTransl("desc_club_online_pay_type", false) %></td> <td rowspan="3"><textarea name="desc_club_online_pay_type" cols="57" rows="3" readonly="readonly" class="inputfield-ro"><%= pay.getValue("DESC_CLUB_ONLINE_PAY_TYPE") %></textarea></td>
			<td><%= Bean.clubXML.getfieldTransl("need_calc_pin", true) %> </td><td><input type="text" name="need_calc_pin" size="20" value="<%= pay.getValue("NEED_CALC_PIN_TSL") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
	    <tr>
			<td><%= Bean.clubXML.getfieldTransl("exist_club_online_pay_type", true) %> </td><td><input type="text" name="exist_club_online_pay_type" size="20" value="<%= pay.getValue("EXIST_CLUB_ONLINE_PAY_TYPE_TSL") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
	    <tr>
			<td colspan="2">&nbsp;</td>
		</tr>
		<%=	Bean.getCreationAndMoficationRecordFields(
				pay.getValue(Bean.getCreationDateFieldName()),
				pay.getValue("CREATED_BY"),
				pay.getValue(Bean.getLastUpdateDateFieldName()),
				pay.getValue("LAST_UPDATE_BY")
			) %>
 		<tr>
			<td colspan="6" align="center">
				<%=Bean.getGoBackButton("../crm/club/online_pay_type.jsp") %>
			</td>
		</tr>
	</table>
	</form>

<%   
 }  

if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLUB_ONLINE_PAY_TYPE_TERM")) {%>
	<table <%= Bean.getTableBottomFilter() %>>
		<tr>
		<%= Bean.getFindHTML("term_find", term_find, "../crm/club/online_pay_typespecs.jsp?id=" + id + "&term_page=1") %>
		</tr>
	</table>
	<%= pay.getTerminalsHTML(term_find, l_term_page_beg, l_term_page_end) %>
<%}

if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLUB_ONLINE_PAY_TYPE_PAYMENTS")) {%>
<table <%= Bean.getTableBottomFilter() %>>
	<tr>
	<%= Bean.getFindHTML("payment_find", payment_find, "../crm/club/online_pay_typespecs.jsp?id=" + id + "&payment_page=1") %>
	</tr>
</table>

<%}

}
%>

</div></div>
</body>
</html>
