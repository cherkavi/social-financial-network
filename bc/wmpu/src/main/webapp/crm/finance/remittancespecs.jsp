<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%= Bean.getLogOutScript(request) %>

<%@page import="bc.objects.bcRemittanceObject"%>
<%@page import="bc.objects.bcClubCardObject"%>
<%@page import="java.util.HashMap"%>
<%@page import="bc.objects.bcSysLogObject"%><html>

<head>
	<%= Bean.getMetaContent() %>
	<%= Bean.getBottomFrameCSS() %>
<Head>

<%

HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String pageFormName = "FINANCE_REMITTANCE";

Bean.setJspPageForTabName(pageFormName);

String tagCardTasks = "_CARD_TASKS";
String tagPostings = "_POSTINGS";
String tagLogRowType = "_LOG_ROWTYPE";
String tagLogType = "_LOG_TYPE";
String tagLog = "_LOG";

String id = Bean.getDecodeParam(parameters.get("id"));
String	tab = Bean.getDecodeParam(parameters.get("tab"));
if (tab==null || "".equalsIgnoreCase(tab)) { 
	tab	= Bean.tabsHmGetValue(pageFormName); 
}

if (id==null || "".equalsIgnoreCase(id)) {
%>
	<%=Bean.getIDNotFoundMessage() %>
<% 
}
else {
	Bean.tabsHmSetValue(pageFormName, tab);
	
	bcRemittanceObject remit = new bcRemittanceObject(id);
	
	String l_log_row_type = Bean.getDecodeParam(parameters.get("log_row_type"));
	if (l_log_row_type==null || "".equalsIgnoreCase(l_log_row_type)) {
		if (Bean.filtersHmGetValue(pageFormName + tagLogRowType) == null) {
			Bean.filtersHmSetValue(pageFormName + tagLogRowType, "");
		}
		l_log_row_type = Bean.filtersHmGetValue(pageFormName + tagLogRowType);
	} else {
		if ("0".equalsIgnoreCase(l_log_row_type)) {
			l_log_row_type = "";
		}
		Bean.filtersHmSetValue(pageFormName + tagLogRowType, l_log_row_type);
	}
	
	String l_log_type = Bean.getDecodeParam(parameters.get("log_type"));
	if (l_log_type==null || "".equalsIgnoreCase(l_log_type)) {
		if (Bean.filtersHmGetValue(pageFormName + tagLogType) == null) {
			Bean.filtersHmSetValue(pageFormName + tagLogType, "");
		}
		l_log_type = Bean.filtersHmGetValue(pageFormName + tagLogType);
	} else {
		if ("0".equalsIgnoreCase(l_log_type)) {
			l_log_type = "";
		}
		Bean.filtersHmSetValue(pageFormName + tagLogType, l_log_type);
	}

	//Обрабатываем номера страниц
	String l_task_page = Bean.getDecodeParam(parameters.get("task_page"));
	Bean.pageCheck(pageFormName + tagCardTasks, l_task_page);
	String l_task_page_beg = Bean.getFirstRowNumber(pageFormName + tagCardTasks);
	String l_task_page_end = Bean.getLastRowNumber(pageFormName + tagCardTasks);

	String l_posting_page = Bean.getDecodeParam(parameters.get("posting_page"));
	Bean.pageCheck(pageFormName + tagPostings, l_posting_page);
	String l_posting_page_beg = Bean.getFirstRowNumber(pageFormName + tagPostings);
	String l_posting_page_end = Bean.getLastRowNumber(pageFormName + tagPostings);
	
	String l_log_page = Bean.getDecodeParam(parameters.get("log_page"));
	Bean.pageCheck(pageFormName + tagLog, l_log_page);
	String l_log_beg = Bean.getFirstRowNumber(pageFormName + tagLog);
	String l_log_end = Bean.getLastRowNumber(pageFormName + tagLog);
	
	bcClubCardObject card = null;
	boolean from_is_card = false;
	if ("CARD_WORKED".equalsIgnoreCase(remit.getValue("FROM_REMITTANCE_TYPE")) ||
		"CARD_TO_EXCLUDE".equalsIgnoreCase(remit.getValue("FROM_REMITTANCE_TYPE")) ||
		"CARD_WAS_LOST".equalsIgnoreCase(remit.getValue("FROM_REMITTANCE_TYPE"))) {
		
		card = new bcClubCardObject(
				remit.getValue("FROM_CARD_SERIAL_NUMBER"),
				remit.getValue("FROM_CARD_ID_ISSUER"),
				remit.getValue("FROM_CARD_ID_PAYMENT_SYSTEM"));
		from_is_card = true;
	}

%>
<body topmargin="0">
<div id="div_tabsheet">
	<table <%=Bean.getTableMenuParam() %>>
		<tr>
			<%= Bean.getPageHeader() %>
			<% if (Bean.currentMenu.isCurrentTabAndEditPermitted("FINANCE_REMITTANCE_INFO")) {%>
			    <%= Bean.getMenuButton("ADD", "../crm/finance/remittanceupdate.jsp?id=" + id + "&type=general&action=add2&process=no", "", "") %>
			    <%= Bean.getMenuButton("DELETE", "../crm/finance/remittanceupdate.jsp?id=" + id + "&type=general&action=remove&process=yes", Bean.cardsettingXML.getfieldTransl("LAB_DELETE", false), remit.getValue("ID_REMITTANCE") + " - " +  remit.getValue("DATE_REMITTANCE_FRMT")) %>
			<% } %>

			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("FINANCE_REMITTANCE_CARD_TASKS")) { %>
			    <!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagCardTasks, "../crm/finance/remittancespecs.jsp?id=" + id + "&tab="+Bean.currentMenu.getTabID("FINANCE_REMITTANCE_CARD_TASKS")+"&", "task_page") %>
			<% } %>
	
			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("FINANCE_REMITTANCE_POSTINGS")) { %>
	
				<% if (Bean.currentMenu.isCurrentTabAndEditPermitted("FINANCE_REMITTANCE_POSTINGS")) { %>
				    <%= Bean.getMenuButton("ADD", "../crm/finance/remittanceupdate.jsp?id=" + id + "&type=posting&action=add&process=no&id_club=" + remit.getValue("ID_CLUB"), "", "") %>
				    <%= Bean.getMenuButton("DELETE_ALL", "../crm/finance/remittanceupdate.jsp?id=" + id + "&type=posting&action=removeall&process=yes", Bean.postingXML.getfieldTransl("LAB_DELETE_ALL_POSTINGS", false), "") %>
				    <%= Bean.getMenuButton("POSTING", "../crm/finance/remittanceupdate.jsp?id=" + id + "&type=posting&action=run&process=yes&id_club=" + remit.getValue("ID_CLUB"), Bean.postingXML.getfieldTransl("LAB_RUN_POSTINGS_REMITTANCE", false), id, Bean.postingXML.getfieldTransl("LAB_RUN_POSTINGS_REMITTANCE", false)) %>
				<% } %>
	
			    <!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagPostings, "../crm/finance/remittancespecs.jsp?id=" + id + "&tab="+Bean.currentMenu.getTabID("FINANCE_REMITTANCE_POSTINGS")+"&", "posting_page") %>
			<% } %>
	
			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("FINANCE_REMITTANCE_LOG")) { %>
			    <!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagLog, "../crm/finance/remittancespecs.jsp?id=" + id + "&tab="+Bean.currentMenu.getTabID("FINANCE_REMITTANCE_LOG")+"&", "log_page") %>
			<% } %>
	
		</tr>
	</table>
	<table <%=Bean.getTableMenuParam() %>>
		<%= Bean.getDetailCaption(remit.getValue("ID_REMITTANCE") + " - " +  remit.getValue("DATE_REMITTANCE_FRMT")) %>
		<tr>
			<td>
				<!-- Выводим перечень закладок -->
				<%= Bean.currentMenu.getFormTabScheeds(tab, "../crm/finance/remittancespecs.jsp?id=" + id) %>
			</td>
			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("FINANCE_REMITTANCE_LOG")) { %>
				<td width=20>
				  	<select onchange="ajaxpage('../crm/finance/remittancespecs.jsp?id=<%=id %>&log_page=1&log_type='+getElementById('log_type').value, 'div_main')" name="log_type" id="log_type" class="inputfield">
						<option value="0"></option>
						<option value="POSTING" <% if ("POSTING".equalsIgnoreCase(l_log_type)) { %>SELECTED <%} %>><%= Bean.transactionXML.getfieldTransl("h_log_posting", false) %></option>
					</select>
				</td>
				<td width=20>
				  <select onchange="ajaxpage('../crm/finance/remittancespecs.jsp?id=<%=id %>&log_page=1&log_row_type='+getElementById('log_row_type').value, 'div_main')" name="log_row_type" id="log_row_type" class="inputfield"><option value="0"></option><%= Bean.getMeaningFromLookupNameOptions("SYS_LOG_ROW_TYPE", l_log_row_type, false) %></select>
				</td>
			<% } %>
		</tr>
	</table>
</div>
<div id="div_data">
<div id="div_data_detail">
<%
if (Bean.currentMenu.isCurrentTabAndAccessPermitted("FINANCE_REMITTANCE_INFO")) {
	boolean hasPermission = Bean.currentMenu.isCurrentTabAndEditPermitted("FINANCE_REMITTANCE_INFO");
	

	if (hasPermission) {
%>
	<script>
		var formAll = new Array (
		);
		var formGeneral = new Array (
			new Array ('date_remittance', 'varchar2', 1),
			new Array ('name_club', 'varchar2', 1),
			new Array ('basis_of_remittance', 'varchar2', 1)
		);
		var formFromBankAccount = new Array (
			new Array ('name_bank_account_from', 'varchar2', 1)
		);
		var formFromCard = new Array (
			new Array ('from_cd_card1', 'varchar2', 1)
		);
		var formToBankAccount = new Array (
			new Array ('name_bank_account_to', 'varchar2', 1)
		);
		var formToCard = new Array (
			new Array ('to_cd_card1', 'varchar2', 1)
		);
		function myValidateForm() {
			var fromType = '<%=remit.getValue("CD_FROM_PARTICIPANT_TYPE")%>';
			var toType = '<%=remit.getValue("CD_TO_PARTICIPANT_TYPE")%>';
			
			formAll = formGeneral;
			if (fromType == 'BON_CARD') {
				formAll = formAll.concat(formFromCard);
			} else if (remittanceType == 'BANK_ACCOUNT') {
				formAll = formAll.concat(formFromBankAccount);
			}
			if (toType == 'BON_CARD') {
				formAll = formAll.concat(formToCard);
			} else if (remittanceType == 'BANK_ACCOUNT') {
				toType = formAll.concat(formToBankAccount);
			}
			//alert(formAll);
			return validateForm(formAll);
		}
	</script>


    <form action="../crm/finance/remittanceupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">
        <input type="hidden" name="type" value="general">
        <input type="hidden" name="action" value="edit">
        <input type="hidden" name="process" value="yes">
        <input type="hidden" name="id" value="<%=id%>">
	<table <%=Bean.getTableDetailParam() %>>
		<tr>
			<td valign="top"><%=Bean.remittanceXML.getfieldTransl("id_remittance", false)%> </td><td><input type="text" name="id_remittance" size="30" value="<%=remit.getValue("ID_REMITTANCE")%>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.clubXML.getfieldTransl("club", false) %>
				<%= Bean.getGoToClubLink(remit.getValue("ID_CLUB")) %>
			</td>
			<td>
				<input type="text" name="name_club" size="35" value="<%= Bean.getClubShortName(remit.getValue("ID_CLUB")) %>" readonly="readonly" class="inputfield-ro">
			</td>
		</tr>
		<tr>
			<td><%=Bean.remittanceXML.getfieldTransl("cd_remittance_type", false)%> </td><td><input type="text" name="cd_remittance_type" size="64" value="<%=remit.getValue("NAME_REMITTANCE_TYPE")%>" readonly="readonly" class="inputfield-ro"></td>
			<% if ("BON_CARD".equalsIgnoreCase(remit.getValue("CD_FROM_PARTICIPANT_TYPE"))) {%>
			<td><%=Bean.remittanceXML.getfieldTransl("from_participant", true)%>
				<%= Bean.getGoToClubCardLink(
						remit.getValue("FROM_CARD_SERIAL_NUMBER"),
						remit.getValue("FROM_CARD_ID_ISSUER"),
						remit.getValue("FROM_CARD_ID_PAYMENT_SYSTEM")
					) %>
			</td> 
			<td>
				<%=Bean.getWindowFindClubCardRemittance("from", remit.getValue("FROM_PARTICIPANT"),remit.getValue("FROM_CARD_SERIAL_NUMBER"),remit.getValue("FROM_CARD_ID_ISSUER"),remit.getValue("FROM_CARD_ID_PAYMENT_SYSTEM"), "30") %>
			</td>
			<% } else if ("BANK_ACCOUNT".equalsIgnoreCase(remit.getValue("CD_FROM_PARTICIPANT_TYPE"))) {%>
			<td><%=Bean.remittanceXML.getfieldTransl("from_participant", true)%>
			 	<%= Bean.getGoToBankAccountLink(remit.getValue("FROM_ID_BANK_ACCOUNT")) %>
			</td>
			<td>
				<%=Bean.getWindowFindBankAccount2("bank_account_from", remit.getValue("FROM_ID_BANK_ACCOUNT"), "30") %>
			</td>			
			<% } else if ("CASH".equalsIgnoreCase(remit.getValue("CD_FROM_PARTICIPANT_TYPE"))) {%>
			<td><b><%=Bean.remittanceXML.getfieldTransl("from_participant", false)%></b></td><td><input type="text" name="from_patricipant" size="35" value="<%=Bean.remittanceXML.getfieldTransl("participant_cash", false)%>" readonly="readonly" class="inputfield-ro"></td>
			<% } %>
		</tr>
		<tr>
			<td><%=Bean.remittanceXML.getfieldTransl("cd_remittance_state", false)%> </td><td><input type="text" name="cd_remittance_state" size="64" value="<%=remit.getValue("NAME_REMITTANCE_STATE")%>" readonly="readonly" class="inputfield-ro"></td>
			<% if ("BON_CARD".equalsIgnoreCase(remit.getValue("CD_TO_PARTICIPANT_TYPE"))) {%>
			<td><%=Bean.remittanceXML.getfieldTransl("to_participant", true)%>
				<%= Bean.getGoToClubCardLink(
						remit.getValue("TO_CARD_SERIAL_NUMBER"),
						remit.getValue("TO_CARD_ID_ISSUER"),
						remit.getValue("TO_CARD_ID_PAYMENT_SYSTEM")
					) %>
			</td> 
			<td>
				<%=Bean.getWindowFindClubCardRemittance("to", remit.getValue("TO_PARTICIPANT"),remit.getValue("TO_CARD_SERIAL_NUMBER"),remit.getValue("TO_CARD_ID_ISSUER"),remit.getValue("TO_CARD_ID_PAYMENT_SYSTEM"), "30") %>
			</td>
			<% } else if ("BANK_ACCOUNT".equalsIgnoreCase(remit.getValue("CD_TO_PARTICIPANT_TYPE"))) {%>
			<td><%=Bean.remittanceXML.getfieldTransl("to_participant", true)%>
			 	<%= Bean.getGoToBankAccountLink(remit.getValue("TO_ID_BANK_ACCOUNT")) %>
			</td>
			<td>
				<%=Bean.getWindowFindBankAccount2("bank_account_to", remit.getValue("TO_ID_BANK_ACCOUNT"), "30") %>
			</td>			
			<% } else if ("CASH".equalsIgnoreCase(remit.getValue("CD_TO_PARTICIPANT_TYPE"))) {%>
			<td><b><%=Bean.remittanceXML.getfieldTransl("to_participant", false)%></b></td><td><input type="text" name="to_patricipant" size="35" value="<%=Bean.remittanceXML.getfieldTransl("participant_cash", false)%>" readonly="readonly" class="inputfield-ro"></td>
			<% } %>
		</tr>
		<tr>
			<td><%=Bean.remittanceXML.getfieldTransl("date_remittance", true)%></td><td><%=Bean.getCalendarInputField("date_remittance", remit.getValue("DATE_REMITTANCE_FRMT"), "10") %></td>
			<% if ("REMITTANCE".equalsIgnoreCase(remit.getValue("CD_REMITTANCE_TYPE")) ||
					"WRITE_OFF_BON_CASH".equalsIgnoreCase(remit.getValue("CD_REMITTANCE_TYPE")) ||
					"WRITE_OFF_BON_NON_CASH".equalsIgnoreCase(remit.getValue("CD_REMITTANCE_TYPE")) ||
					"ADD_BON_CASH".equalsIgnoreCase(remit.getValue("CD_REMITTANCE_TYPE")) ||
					"ADD_BON_FROM_BANK_ACCOUNT".equalsIgnoreCase(remit.getValue("CD_REMITTANCE_TYPE"))) {%>
			<td><%=Bean.remittanceXML.getfieldTransl("bal_full", true)%> </td><td><input type="text" name="bal_full" size="20" value="<%=remit.getValue("BAL_FULL_FRMT")%>" class="inputfield"></td>
			<% } %>
		</tr>
		<tr>
			<td valign="top"><%= Bean.remittanceXML.getfieldTransl("basis_of_remittance", true) %></td><td colspan="3" colspan="3"><textarea name="basis_of_remittance" cols="60" rows="3" class="inputfield"><%= remit.getValue("BASIS_OF_REMITTANCE") %></textarea></td>
		</tr>
		<%=	Bean.getCreationAndMoficationRecordFields(
				remit.getValue(Bean.getCreationDateFieldName()),
				remit.getValue("CREATED_BY"),
				remit.getValue(Bean.getLastUpdateDateFieldName()),
				remit.getValue("LAST_UPDATE_BY")
			) %>
		<tr>
			<td colspan="8" align="center">
				<%=Bean.getSubmitButtonAjax("../crm/finance/remittanceupdate.jsp") %>
				<%=Bean.getResetButton() %>
				<%=Bean.getGoBackButton("../crm/finance/remittance.jsp") %>
			</td>
		</tr>

	</table>
	</form>	
	<!-- Скрипт для втавки меню вибору дати -->
	<%= Bean.getCalendarScript("date_remittance", false) %>


<%
	} else {
%> 
	<form>
	<table <%=Bean.getTableDetailParam() %>>
		<tr>
			<td valign="top"><%=Bean.remittanceXML.getfieldTransl("id_remittance", false)%> </td><td><input type="text" name="id_remittance" size="30" value="<%=remit.getValue("ID_REMITTANCE")%>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.clubXML.getfieldTransl("club", false) %>
				<%= Bean.getGoToClubLink(remit.getValue("ID_CLUB")) %>
			</td>
			<td>
				<input type="text" name="name_club" size="35" value="<%= Bean.getClubShortName(remit.getValue("ID_CLUB")) %>" readonly="readonly" class="inputfield-ro">
			</td>
		</tr>
		<tr>
			<td><%=Bean.remittanceXML.getfieldTransl("cd_remittance_type", false)%> </td><td><input type="text" name="cd_remittance_type" size="64" value="<%=remit.getValue("NAME_REMITTANCE_TYPE")%>" readonly="readonly" class="inputfield-ro"></td>
			<% if ("BON_CARD".equalsIgnoreCase(remit.getValue("CD_FROM_PARTICIPANT_TYPE"))) {%>
			<td><%=Bean.remittanceXML.getfieldTransl("from_participant", false)%>
				<%= Bean.getGoToClubCardLink(
						remit.getValue("FROM_CARD_SERIAL_NUMBER"),
						remit.getValue("FROM_CARD_ID_ISSUER"),
						remit.getValue("FROM_CARD_ID_PAYMENT_SYSTEM")
					) %>
			</td> 
			<td>
				<input type="text" name="from_participant" id="from_participant" size="35" value="<%=remit.getValue("FROM_PARTICIPANT") %>" readonly="readonly" class="inputfield-ro">
			</td>
			<% } else if ("BANK_ACCOUNT".equalsIgnoreCase(remit.getValue("CD_FROM_PARTICIPANT_TYPE"))) {%>
			<td><%=Bean.remittanceXML.getfieldTransl("from_participant", false)%>
			 	<%= Bean.getGoToBankAccountLink(remit.getValue("FROM_ID_BANK_ACCOUNT")) %>
			</td>
			<td>
				<input type="text" name="from_participant" id="from_participant" size="35" value="<%=remit.getValue("FROM_PARTICIPANT") %>" readonly="readonly" class="inputfield-ro">
			</td>			
			<% } else if ("CASH".equalsIgnoreCase(remit.getValue("CD_FROM_PARTICIPANT_TYPE"))) {%>
			<td><b><%=Bean.remittanceXML.getfieldTransl("from_participant", false)%></b></td><td><input type="text" name="from_patricipant" size="35" value="<%=Bean.remittanceXML.getfieldTransl("participant_cash", false)%>" readonly="readonly" class="inputfield-ro"></td>
			<% } %>
		</tr>
		<tr>
			<td><%=Bean.remittanceXML.getfieldTransl("cd_remittance_state", false)%> </td><td><input type="text" name="cd_remittance_state" size="64" value="<%=remit.getValue("NAME_REMITTANCE_STATE")%>" readonly="readonly" class="inputfield-ro"></td>
			<% if ("BON_CARD".equalsIgnoreCase(remit.getValue("CD_TO_PARTICIPANT_TYPE"))) {%>
			<td><%=Bean.remittanceXML.getfieldTransl("to_participant", false)%>
				<%= Bean.getGoToClubCardLink(
						remit.getValue("TO_CARD_SERIAL_NUMBER"),
						remit.getValue("TO_CARD_ID_ISSUER"),
						remit.getValue("TO_CARD_ID_PAYMENT_SYSTEM")
					) %>
			</td> 
			<td>
				<input type="text" name="to_participant" id="to_participant" size="35" value="<%=remit.getValue("TO_PARTICIPANT") %>" readonly="readonly" class="inputfield-ro">
			</td>
			<% } else if ("BANK_ACCOUNT".equalsIgnoreCase(remit.getValue("CD_TO_PARTICIPANT_TYPE"))) {%>
			<td><%=Bean.remittanceXML.getfieldTransl("to_participant", false)%>
			 	<%= Bean.getGoToBankAccountLink(remit.getValue("TO_ID_BANK_ACCOUNT")) %>
			</td>
			<td>
				<input type="text" name="to_participant" id="to_participant" size="35" value="<%=remit.getValue("TO_PARTICIPANT") %>" readonly="readonly" class="inputfield-ro">
			</td>			
			<% } else if ("CASH".equalsIgnoreCase(remit.getValue("CD_TO_PARTICIPANT_TYPE"))) {%>
			<td><b><%=Bean.remittanceXML.getfieldTransl("to_participant", false)%></b></td><td><input type="text" name="to_patricipant" size="35" value="<%=Bean.remittanceXML.getfieldTransl("participant_cash", false)%>" readonly="readonly" class="inputfield-ro"></td>
			<% } %>
		</tr>
		<tr>
			<td><%=Bean.remittanceXML.getfieldTransl("date_remittance", false)%></td><td><input type="text" name="date_remittance" size="20" value="<%=remit.getValue("DATE_REMITTANCE_FRMT")%>" readonly="readonly" class="inputfield-ro"></td>
			<% if ("REMITTANCE".equalsIgnoreCase(remit.getValue("CD_REMITTANCE_TYPE")) ||
					"WRITE_OFF_BON_CASH".equalsIgnoreCase(remit.getValue("CD_REMITTANCE_TYPE")) ||
					"WRITE_OFF_BON_NON_CASH".equalsIgnoreCase(remit.getValue("CD_REMITTANCE_TYPE")) ||
					"ADD_BON_CASH".equalsIgnoreCase(remit.getValue("CD_REMITTANCE_TYPE")) ||
					"ADD_BON_FROM_BANK_ACCOUNT".equalsIgnoreCase(remit.getValue("CD_REMITTANCE_TYPE"))) {%>
			<td><%=Bean.remittanceXML.getfieldTransl("bal_full", false)%> </td><td><input type="text" name="bal_full" size="20" value="<%=remit.getValue("BAL_FULL_FRMT")%>" readonly="readonly" class="inputfield-ro"></td>
			<% } %>
		</tr>
		<tr>
			<td valign="top"><%= Bean.remittanceXML.getfieldTransl("basis_of_remittance", false) %></td><td colspan="3" colspan="3"><textarea name="basis_of_remittance" cols="60" rows="3" readonly="readonly" class="inputfield-ro"><%= remit.getValue("BASIS_OF_REMITTANCE") %></textarea></td>
		</tr>
		<%=	Bean.getCreationAndMoficationRecordFields(
				remit.getValue(Bean.getCreationDateFieldName()),
				remit.getValue("CREATED_BY"),
				remit.getValue(Bean.getLastUpdateDateFieldName()),
				remit.getValue("LAST_UPDATE_BY")
			) %>
		<tr>
			<td colspan="8" align="center">
				<%=Bean.getResetButton() %>
				<%=Bean.getGoBackButton("../crm/finance/remittance.jsp") %>
			</td>
		</tr>
	</table>
	</form>


<% }
}

if (Bean.currentMenu.isCurrentTabAndAccessPermitted("FINANCE_REMITTANCE_ACTIONS")) {
	boolean hasPermission = Bean.currentMenu.isCurrentTabAndEditPermitted("FINANCE_REMITTANCE_ACTIONS");

	if (!hasPermission) {
%>
	<form>
	<table <%=Bean.getTableDetailParam() %>>
		<tr>
			<td><%=Bean.remittanceXML.getfieldTransl("id_remittance", false)%> </td><td><input type="text" name="id_remittance" size="30" value="<%=remit.getValue("ID_REMITTANCE")%>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td><%=Bean.remittanceXML.getfieldTransl("date_remittance", false)%></td> <td><input type="text" name="date_remittance" size="15" value="<%= remit.getValue("DATE_REMITTANCE_FRMT")%>" readonly="readonly" class="inputfield-ro"> </td>
		</tr>
		<tr>
			<td><%=Bean.remittanceXML.getfieldTransl("remittance_action", false)%></td> <td><input type="text" name="remittance_action" size="30" value="<%= remit.getValue("REMITTANCE_ACTION_TSL")%>" readonly="readonly" class="inputfield-ro"> </td>
		</tr>
		<tr>
			<td valign="top"><%= Bean.remittanceXML.getfieldTransl("basis_for_action", false) %></td> 
				<td><textarea name="basis_for_action" cols="70" rows="5" readonly="readonly" class="inputfield-ro"><%= remit.getValue("BASIS_FOR_ACTION") %></textarea></td>
		</tr>
		<tr>
			<td colspan="6" align="center">
				<%=Bean.getGoBackButton("../crm/finance/remittance.jsp") %>
			</td>
		</tr>
	</table>
	</form>

<%
	} else {
%> 
	<script>
		var formData = new Array (
			new Array ('remittance_action', 'varchar2', 1)
		);
		
		var formData2 = new Array (
			new Array ('remittance_action', 'varchar2', 1),
			new Array ('basis_for_action', 'varchar2', 1)
		);
		function setBasisVisibility() {
			var codeAction = document.getElementById('remittance_action').value;
			if (codeAction == 'WAITING' || codeAction == 'EXECUTE') {
 				document.getElementById('span_basis').innerHTML = '<%= Bean.remittanceXML.getfieldTransl("basis_for_action", false) %>';
			} else {
 				document.getElementById('span_basis').innerHTML = '<%= Bean.remittanceXML.getfieldTransl("basis_for_action", true) %>';
			}
		}
		function myValidateForm() {
			var codeAction = document.getElementById('remittance_action').value;
			
			if (codeAction == 'WAITING' || codeAction == 'EXECUTE') {
				return validateForm(formData);
			} else {
				return validateForm(formData2);
			}
		}
	</script>

    <form action="../crm/finance/remittanceupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return myValidateForm()">
        <input type="hidden" name="type" value="general">
        <input type="hidden" name="action" value="set_action">
        <input type="hidden" name="process" value="yes">
        <input type="hidden" name="id" value="<%=id%>">
	<table <%=Bean.getTableDetailParam() %>>
		<tr>
			<td valign="top"><%=Bean.remittanceXML.getfieldTransl("id_remittance", false)%> </td><td><input type="text" name="id_remittance" size="30" value="<%=remit.getValue("ID_REMITTANCE")%>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td><%=Bean.remittanceXML.getfieldTransl("date_remittance", false)%></td> <td><input type="text" name="date_remittance" size="15" value="<%= remit.getValue("DATE_REMITTANCE_FRMT")%>" readonly="readonly" class="inputfield-ro"> </td>
		</tr>
		<tr>
			<td valign="top"><%=Bean.remittanceXML.getfieldTransl("remittance_action", true)%></td> <td valign="top"><select name="remittance_action" id="remittance_action" class="inputfield" onchange="setBasisVisibility();"><%= Bean.getMeaningFromLookupNameOrderByNymberValueOptions("REMITTANCE_ACTION", remit.getValue("REMITTANCE_ACTION"), false) %></select></td>
		</tr>
		<tr>
			<td valign="top"><span id="span_basis"><%= Bean.remittanceXML.getfieldTransl("basis_for_action", true) %></span></td> 
			<td><textarea name="basis_for_action" cols="70" rows="5" class="inputfield"><%= remit.getValue("BASIS_FOR_ACTION") %></textarea></td>
		</tr>
		<tr>
			<td colspan="6" align="center">
				<%=Bean.getSubmitButtonAjax("../crm/finance/remittanceupdate.jsp") %>
				<%=Bean.getResetButton() %>
				<%=Bean.getGoBackButton("../crm/finance/remittance.jsp") %>
			</td>
		</tr>

	</table>
	</form>	

<% }
}

if (Bean.currentMenu.isCurrentTabAndAccessPermitted("FINANCE_REMITTANCE_CARD_TASKS")) {%>
	<%= remit.getClubCardsTasksHTML("", "", "", l_task_page_beg, l_task_page_end) %>
<%}

if (Bean.currentMenu.isCurrentTabAndAccessPermitted("FINANCE_REMITTANCE_POSTINGS")) {%>
	<%= remit.getPostingsHTML("", l_posting_page_beg, l_posting_page_end) %>
<%}

if (Bean.currentMenu.isCurrentTabAndAccessPermitted("FINANCE_REMITTANCE_LOG")) {%>
	<% bcSysLogObject log = new bcSysLogObject(); 
	 if (!(l_log_type==null || "".equalsIgnoreCase(l_log_type))) {
		 l_log_type = "'"+l_log_type+"'";
	 }
	%>
	
	<%= log.getSysLogHTML("", l_log_type, "'REMITTANCE'", id, "", "", "", "", l_log_row_type, l_log_beg, l_log_end) %>
<%}

}
%>
</div></div>
</body>
</html>
