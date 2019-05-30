<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%@page import="bc.objects.bcCallCenterCallGroupObject"%>
<%@page import="java.util.HashMap"%>
<%= Bean.getLogOutScript(request) %>
<html>
<head>
	<%= Bean.getMetaContent() %>
	<%= Bean.getBottomFrameCSS() %>
	
<title></title>
</head>
<body>
<div id="div_tabsheet">

<% 
HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());


String pageFormName = "CALL_CENTER_CALL_GROUP";
String tagClients = "_CLIENTS";
String tagClientFind = "_CLIENT_FIND";
String tagClientOrderColumn = "_ORDER_COLUMN";
String tagClientOrderType = "_ORDER_TYPE";
String tagQuestionType = "_CLIENT_QUESTION_TYPE";
String tagQuestionStatus = "_CLIENT_QUESTION_STATUS";


Bean.setJspPageForTabName(pageFormName);

String id = Bean.getDecodeParam(parameters.get("id"));
String tab = Bean.getDecodeParam(parameters.get("tab"));
if (id==null || "".equalsIgnoreCase(id)) { id=""; }
if (tab==null || "".equalsIgnoreCase(tab)) { tab = Bean.tabsHmGetValue(pageFormName); }

if ("".equalsIgnoreCase(id)) {
%>
	<%=Bean.getIDNotFoundMessage() %>
<% 
} 
else {
	Bean.tabsHmSetValue(pageFormName, tab);
	
	//Обрабатываем номера страниц
	String l_client_page = Bean.getDecodeParam(parameters.get("client_page"));
	Bean.pageCheck(pageFormName + tagClients, l_client_page);
	String l_client_page_beg = Bean.getFirstRowNumber(pageFormName + tagClients);
	String l_client_page_end = Bean.getLastRowNumber(pageFormName + tagClients);

	String client_find 	= Bean.getDecodeParam(parameters.get("client_find"));
	client_find 	= Bean.checkFindString(pageFormName + tagClientFind, client_find, l_client_page);

	String quest_type 	= Bean.getDecodeParam(parameters.get("quest_type"));
	quest_type 	= Bean.checkFindString(pageFormName + tagQuestionType, quest_type, l_client_page);

	String quest_status	= Bean.getDecodeParam(parameters.get("quest_status"));
	quest_status		= Bean.checkFindString(pageFormName + tagQuestionStatus, quest_status, l_client_page);
	
	bcCallCenterCallGroupObject group = new bcCallCenterCallGroupObject(id);
	
	
%>
  <% Bean.currentMenu.setCurrentTab(Bean.currentMenu.getTabSheetName(tab)); %>
	<table <%=Bean.getTableMenuParam() %>>
		<tr>
			<%= Bean.getPageHeader() %>

		<% if (Bean.currentMenu.isCurrentTabAndEditPermitted("CALL_CENTER_CALL_GROUP_INFO")) {%>
		    <%= Bean.getMenuButton("ADD", "../crm/call_center/call_groupupdate.jsp?id=" + group.getValue("ID_CC_CALL_GROUP") + "&type=general&action=add2&process=no", "", "") %>
		    <%= Bean.getMenuButton("DELETE", "../crm/call_center/call_groupupdate.jsp?id=" + group.getValue("ID_CC_CALL_GROUP") + "&type=general&action=remove&process=yes", Bean.call_centerXML.getfieldTransl("h_delete_call_group", false), group.getValue("ID_CC_CALL_GROUP") + " - " +  group.getValue("NAME_CC_CALL_GROUP")) %>
		<% } %>

		<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CALL_CENTER_CALL_GROUP_CLIENTS")) {%>
			<!-- Вывод страниц -->
			<%= Bean.getPagesHTML(pageFormName + tagClients, "../crm/call_center/call_groupspecs.jsp?id=" + id + "&tab="+Bean.currentMenu.getTabID("CALL_CENTER_CALL_GROUP_CLIENTS")+"&", "client_page") %>
		<% } %>
		</tr>
	</table>
	<table <%=Bean.getTableMenuParam() %>>
		<%= Bean.getDetailCaption(group.getValue("ID_CC_CALL_GROUP") + " - " + group.getValue("NAME_CC_CALL_GROUP")) %>
		<tr>
			<td>
				<!-- Выводим перечень закладок -->
				<%= Bean.currentMenu.getFormTabScheeds(tab, "../crm/call_center/call_groupspecs.jsp?id=" + id) %>
			</td>
		</tr>
	</table>
</div>
<div id="div_data">
<div id="div_data_detail">

<% 
if (Bean.currentMenu.isCurrentTabAndPreviewPermitted("CALL_CENTER_CALL_GROUP_INFO")) {%> 
	<form action="../crm/call_center/call_groupupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST">
        <input type="hidden" name="type" value="general">
        <input type="hidden" name="action" value="edit_answer_style">
        <input type="hidden" name="process" value="yes">
		<input type="hidden" name="id" value="<%= group.getValue("ID_CC_CALL_GROUP") %>">
	<table <%=Bean.getTableDetailParam() %>>
		<tr>
			<td><%= Bean.call_centerXML.getfieldTransl("id_cc_call_group", false) %></td><td><input type="text" name="id_cc_call_group" size="20" value="<%= group.getValue("ID_CC_CALL_GROUP") %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.clubXML.getfieldTransl("club", false) %>
				<%= Bean.getGoToClubLink(group.getValue("ID_CLUB")) %>
			</td><td><input type="text" name="name_club" size="40" value="<%= Bean.getClubShortName(group.getValue("ID_CLUB")) %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td><%= Bean.call_centerXML.getfieldTransl("name_cc_call_group", false) %></td><td><input type="text" name="name_cc_call_group" size="53" value="<%= group.getValue("NAME_CC_CALL_GROUP") %>" readonly="readonly" class="inputfield-ro"></td>
			<%
				String pLineStyle = Bean.getUIUserParam("inquirer_line_answer_style");
				if (pLineStyle == null || "".equalsIgnoreCase(pLineStyle)) {
					pLineStyle = "SELECT";
				}
			%>
		  	<td><%= Bean.call_centerXML.getfieldTransl("inquirer_line_answer_style", false) %></td>
				<td><select id="inquirer_line_answer_style" name="inquirer_line_answer_style" class="inputfield">
						<option value="SELECT" <% if ("SELECT".equalsIgnoreCase(pLineStyle)) { %> SELECTED <% } %>><%= Bean.call_centerXML.getfieldTransl("inquirer_line_answer_select", false) %></option>
						<option value="RADIO" <% if ("RADIO".equalsIgnoreCase(pLineStyle)) { %> SELECTED <% } %>><%= Bean.call_centerXML.getfieldTransl("inquirer_line_answer_radio", false) %></option>
				    </select>
				</td>
		</tr>
		<tr>
			<td><%= Bean.call_centerXML.getfieldTransl("cd_cc_call_group_state", false) %></td><td><input type="text" name="cd_cc_call_group_state" size="20" value="<%= group.getValue("NAME_CC_CALL_GROUP_STATE") %>" readonly="readonly" class="inputfield-ro"></td>
			<td colspan="2"><b><%= Bean.call_centerXML.getfieldTransl("cg_criterion", false) %></b></td>
		</tr>			
		<tr>
		  	<td><%= Bean.call_centerXML.getfieldTransl("date_cc_call_group", false) %></td><td><input type="text" name="name_cc_call_group" size="10" value="<%= group.getValue("DATE_CC_CALL_GROUP_FRMT") %>" readonly="readonly" class="inputfield-ro"></td>
		  	<td><%= Bean.call_centerXML.getfieldTransl("cg_crit_begin_purchase_date", false) %></td><td><input type="text" name="cg_crit_begin_purchase_date" size="10" value="<%= group.getValue("CRIT_BEGIN_PURCHASE_DATE_DF") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>			
		<tr>
			<td><%= Bean.call_centerXML.getfieldTransl("name_cc_inquirer", false) %>
				<%= Bean.getGoToCallCenterInquirerLink(group.getValue("ID_CC_INQUIRER")) %>
			</td>
			<td>
				<input type="text" name="name_cc_inquirer" size="53" value="<%= group.getValue("NAME_CC_INQUIRER") %>" readonly="readonly" class="inputfield-ro">
			</td>
		    <td><%= Bean.call_centerXML.getfieldTransl("cg_crit_give_for_event", false) %>
				<%= Bean.getGoToClubEventLink(group.getValue("CRIT_GIVE_FOR_EVENT")) %>
			</td>
		  	<td>
				<input type="text" name="crit_give_for_event" size="40" value="<%= Bean.getClubActionName(group.getValue("CRIT_GIVE_FOR_EVENT")) %>" readonly="readonly" class="inputfield-ro">
			</td>	
		<tr>
			<td><%= Bean.call_centerXML.getfieldTransl("clients_count", false) %></td><td><input type="text" name="clients_count" size="20" value="<%= group.getValue("CLIENTS_COUNT") %>" readonly="readonly" class="inputfield-ro"></td>
		  	<td><%= Bean.call_centerXML.getfieldTransl("cg_crit_given_from_date", false) %></td><td><input type="text" name="cg_crit_given_from_date" size="10" value="<%= group.getValue("CRIT_GIVEN_FROM_DATE_DF") %>" readonly="readonly" class="inputfield-ro">&nbsp;<%= Bean.call_centerXML.getfieldTransl("cg_crit_given_to_date", false) %>&nbsp;<input type="text" name="cg_crit_given_to_date" size="10" value="<%= group.getValue("CRIT_GIVEN_TO_DATE_DF") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>			
		<tr>
			<td rowspan="3"><%= Bean.call_centerXML.getfieldTransl("desc_cc_call_group", false) %></td><td rowspan="3"><textarea name="desc_cc_call_group" cols="50" rows="3" readonly="readonly" class="inputfield-ro"><%= group.getValue("DESC_CC_CALL_GROUP") %></textarea></td>
		  	<td><%= Bean.call_centerXML.getfieldTransl("cg_crit_made_purchases", false) %></td>
				<td>
					<%
					  String p_crit_made_purchase_condition = "";
					  if ("LESS".equalsIgnoreCase(group.getValue("CRIT_MADE_PURCHASE_CONDITION"))) {
						  p_crit_made_purchase_condition = Bean.call_centerXML.getfieldTransl("cg_crit_made_purchase_less", false);
					  } else if ("EQUAL".equalsIgnoreCase(group.getValue("CRIT_MADE_PURCHASE_CONDITION"))) {
						  p_crit_made_purchase_condition = Bean.call_centerXML.getfieldTransl("cg_crit_made_purchase_equal", false);
					  } else if ("MORE".equalsIgnoreCase(group.getValue("CRIT_MADE_PURCHASE_CONDITION"))) {
						  p_crit_made_purchase_condition = Bean.call_centerXML.getfieldTransl("cg_crit_made_purchase_more", false);
					  }
					%>
					<input type="text" name="crit_made_purchase_condition" size="10" value="<%= p_crit_made_purchase_condition %>" readonly="readonly" class="inputfield-ro">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="text" name="crit_made_purchase_value" size="5" value="<%= group.getValue("CRIT_MADE_PURCHASE_VALUE") %>" readonly="readonly" class="inputfield-ro">
				</td>
		</tr>		
		<tr>
		  	<td>&nbsp;&nbsp;&nbsp;&nbsp;<%= Bean.call_centerXML.getfieldTransl("cg_crit_made_purchase_from_date", false) %></td><td><input type="text" name="crit_made_purchase_from_date" size="10" value="<%= group.getValue("CRIT_MADE_PURCHASE_FROM_DATE_DF") %>" readonly="readonly" class="inputfield-ro">&nbsp;<%= Bean.call_centerXML.getfieldTransl("cg_crit_made_purchase_to_date", false) %>&nbsp;<input type="text" name="crit_made_purchase_to_date" size="10" value="<%= group.getValue("CRIT_MADE_PURCHASE_TO_DATE_DF") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>				
		<tr>
		    <td>&nbsp;&nbsp;&nbsp;&nbsp;<%= Bean.call_centerXML.getfieldTransl("crit_made_purchase_dealer", false) %>
				<%= Bean.getGoToJurPrsHyperLink(group.getValue("CRIT_MADE_PURCHASE_DEALER")) %>
			</td>
		  	<td>
				<input type="text" name="crit_made_purchase_dealer" size="40" value="<%= Bean.getJurPersonShortName(group.getValue("CRIT_MADE_PURCHASE_DEALER")) %>" readonly="readonly" class="inputfield-ro">
			</td>	
		</tr>			
		<%=	Bean.getCreationAndMoficationRecordFields(
				group.getValue(Bean.getCreationDateFieldName()),
				group.getValue("CREATED_BY"),
				group.getValue(Bean.getLastUpdateDateFieldName()),
				group.getValue("LAST_UPDATE_BY")
			) %>
		<tr>
			<td colspan="6" align="center">
				<%=Bean.getSubmitButtonAjax("../crm/call_center/call_groupupdate.jsp") %>
				<%=Bean.getResetButton() %>
				<%=Bean.getGoBackButton("../crm/call_center/call_group.jsp") %>
			</td>
		</tr>
		
	</table>
	</form>
<% 	} else if (Bean.currentMenu.isCurrentTabAndEditPermitted("CALL_CENTER_CALL_GROUP_INFO")) { %>
    <script>
		var formDataQuestion = new Array (
			new Array ('name_cc_call_group', 'varchar2', 1),
			new Array ('cd_cc_call_group_state', 'varchar2', 1),
			new Array ('date_cc_call_group', 'varchar2', 1),
			new Array ('cd_cc_question_type', 'varchar2', 1),
			new Array ('cd_cc_question_status', 'varchar2', 1),
			new Array ('cd_cc_question_important', 'varchar2', 1),
			new Array ('cd_cc_question_urgent', 'varchar2', 1),
			new Array ('exist_flag', 'varchar2', 1)
		);
		function myValidateForm() {
			return validateForm(formDataQuestion);
		}
	</script>
    
	<form action="../crm/call_center/call_groupupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return myValidateForm()">
        <input type="hidden" name="type" value="general">
        <input type="hidden" name="action" value="edit">
        <input type="hidden" name="process" value="yes">
		<input type="hidden" name="id" value="<%= group.getValue("ID_CC_CALL_GROUP") %>">
		<input type="hidden" name="LUD" value="<%= group.getValue("LUD") %>">
	<table <%=Bean.getTableDetailParam() %>>
		<tr>
			<td><%= Bean.call_centerXML.getfieldTransl("id_cc_call_group", false) %></td><td><input type="text" name="id_cc_call_group" size="20" value="<%= group.getValue("ID_CC_CALL_GROUP") %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.clubXML.getfieldTransl("club", false) %>
				<%= Bean.getGoToClubLink(group.getValue("ID_CLUB")) %>
			</td><td><input type="text" name="name_club" size="40" value="<%= Bean.getClubShortName(group.getValue("ID_CLUB")) %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td><%= Bean.call_centerXML.getfieldTransl("name_cc_call_group", true) %></td><td><input type="text" name="name_cc_call_group" size="53" value="<%= group.getValue("NAME_CC_CALL_GROUP") %>" class="inputfield"></td>
			<%
				String pLineStyle = Bean.getUIUserParam("inquirer_line_answer_style");
				if (pLineStyle == null || "".equalsIgnoreCase(pLineStyle)) {
					pLineStyle = "SELECT";
				}
			%>
		  	<td><%= Bean.call_centerXML.getfieldTransl("inquirer_line_answer_style", false) %></td>
				<td><select id="inquirer_line_answer_style" name="inquirer_line_answer_style" class="inputfield">
						<option value="SELECT" <% if ("SELECT".equalsIgnoreCase(pLineStyle)) { %> SELECTED <% } %>><%= Bean.call_centerXML.getfieldTransl("inquirer_line_answer_select", false) %></option>
						<option value="RADIO" <% if ("RADIO".equalsIgnoreCase(pLineStyle)) { %> SELECTED <% } %>><%= Bean.call_centerXML.getfieldTransl("inquirer_line_answer_radio", false) %></option>
				    </select>
				</td>
		</tr>
		<tr>
			<td><%= Bean.call_centerXML.getfieldTransl("cd_cc_call_group_state", true) %></td><td><select name="cd_cc_call_group_state" class="inputfield"><%= Bean.getCallCenterCallGroupStateOptions(group.getValue("CD_CC_CALL_GROUP_STATE"), false) %></select></td>
			<td colspan="2"><b><%= Bean.call_centerXML.getfieldTransl("cg_criterion", false) %></b></td>
		</tr>			
		<tr>
		  	<td><%= Bean.call_centerXML.getfieldTransl("date_cc_call_group", true) %></td><td><%=Bean.getCalendarInputField("date_cc_call_group", group.getValue("DATE_CC_CALL_GROUP_FRMT"), "10") %></td>
		  	<td><%= Bean.call_centerXML.getfieldTransl("cg_crit_begin_purchase_date", false) %></td><td><input type="text" name="cg_crit_begin_purchase_date" size="10" value="<%= group.getValue("CRIT_BEGIN_PURCHASE_DATE_DF") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>			
		<tr>
			<td><%= Bean.call_centerXML.getfieldTransl("name_cc_inquirer", false) %>
				<%= Bean.getGoToCallCenterInquirerLink(group.getValue("ID_CC_INQUIRER")) %>
			</td>
			<td>
				<input type="text" name="name_cc_inquirer" size="53" value="<%= group.getValue("NAME_CC_INQUIRER") %>" readonly="readonly" class="inputfield-ro">
			</td>
		    <td><%= Bean.call_centerXML.getfieldTransl("cg_crit_give_for_event", false) %>
				<%= Bean.getGoToClubEventLink(group.getValue("CRIT_GIVE_FOR_EVENT")) %>
			</td>
		  	<td>
				<input type="text" name="crit_give_for_event" size="40" value="<%= Bean.getClubActionName(group.getValue("CRIT_GIVE_FOR_EVENT")) %>" readonly="readonly" class="inputfield-ro">
			</td>	
		<tr>
			<td><%= Bean.call_centerXML.getfieldTransl("clients_count", false) %></td><td><input type="text" name="clients_count" size="20" value="<%= group.getValue("CLIENTS_COUNT") %>" readonly="readonly" class="inputfield-ro"></td>
		  	<td><%= Bean.call_centerXML.getfieldTransl("cg_crit_given_from_date", false) %></td><td><input type="text" name="cg_crit_given_from_date" size="10" value="<%= group.getValue("CRIT_GIVEN_FROM_DATE_DF") %>" readonly="readonly" class="inputfield-ro">&nbsp;<%= Bean.call_centerXML.getfieldTransl("cg_crit_given_to_date", false) %>&nbsp;<input type="text" name="cg_crit_given_to_date" size="10" value="<%= group.getValue("CRIT_GIVEN_TO_DATE_DF") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>			
		<tr>
			<td rowspan="3"><%= Bean.call_centerXML.getfieldTransl("desc_cc_call_group", false) %></td><td rowspan="3"><textarea name="desc_cc_call_group" cols="50" rows="3" class="inputfield"><%= group.getValue("DESC_CC_CALL_GROUP") %></textarea></td>
		  	<td><%= Bean.call_centerXML.getfieldTransl("cg_crit_made_purchases", false) %></td>
				<td>
					<%
					  String p_crit_made_purchase_condition = "";
					  if ("LESS".equalsIgnoreCase(group.getValue("CRIT_MADE_PURCHASE_CONDITION"))) {
						  p_crit_made_purchase_condition = Bean.call_centerXML.getfieldTransl("cg_crit_made_purchase_less", false);
					  } else if ("EQUAL".equalsIgnoreCase(group.getValue("CRIT_MADE_PURCHASE_CONDITION"))) {
						  p_crit_made_purchase_condition = Bean.call_centerXML.getfieldTransl("cg_crit_made_purchase_equal", false);
					  } else if ("MORE".equalsIgnoreCase(group.getValue("CRIT_MADE_PURCHASE_CONDITION"))) {
						  p_crit_made_purchase_condition = Bean.call_centerXML.getfieldTransl("cg_crit_made_purchase_more", false);
					  }
					%>
					<input type="text" name="crit_made_purchase_condition" size="10" value="<%= p_crit_made_purchase_condition %>" readonly="readonly" class="inputfield-ro">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="text" name="crit_made_purchase_value" size="5" value="<%= group.getValue("CRIT_MADE_PURCHASE_VALUE") %>" readonly="readonly" class="inputfield-ro">
				</td>
		</tr>		
		<tr>
		  	<td>&nbsp;&nbsp;&nbsp;&nbsp;<%= Bean.call_centerXML.getfieldTransl("cg_crit_made_purchase_from_date", false) %></td><td><input type="text" name="crit_made_purchase_from_date" size="10" value="<%= group.getValue("CRIT_MADE_PURCHASE_FROM_DATE_DF") %>" readonly="readonly" class="inputfield-ro">&nbsp;<%= Bean.call_centerXML.getfieldTransl("cg_crit_made_purchase_to_date", false) %>&nbsp;<input type="text" name="crit_made_purchase_to_date" size="10" value="<%= group.getValue("CRIT_MADE_PURCHASE_TO_DATE_DF") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>				
		<tr>
		    <td>&nbsp;&nbsp;&nbsp;&nbsp;<%= Bean.call_centerXML.getfieldTransl("crit_made_purchase_dealer", false) %>
				<%= Bean.getGoToJurPrsHyperLink(group.getValue("CRIT_MADE_PURCHASE_DEALER")) %>
			</td>
		  	<td>
				<input type="text" name="crit_made_purchase_dealer" size="40" value="<%= Bean.getJurPersonShortName(group.getValue("CRIT_MADE_PURCHASE_DEALER")) %>" readonly="readonly" class="inputfield-ro">
			</td>	
		</tr>			
		<%=	Bean.getCreationAndMoficationRecordFields(
				group.getValue(Bean.getCreationDateFieldName()),
				group.getValue("CREATED_BY"),
				group.getValue(Bean.getLastUpdateDateFieldName()),
				group.getValue("LAST_UPDATE_BY")
			) %>
		<tr>
			<td colspan="6" align="center">
				<%=Bean.getSubmitButtonAjax("../crm/call_center/call_groupupdate.jsp") %>
				<%=Bean.getResetButton() %>
				<%=Bean.getGoBackButton("../crm/call_center/call_group.jsp") %>
			</td>
		</tr>
	</table>
	</form>	
	<%= Bean.getCalendarScript("date_cc_call_group", false) %>
<%}
if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CALL_CENTER_CALL_GROUP_CLIENTS")) {


	
	String l_client_order_column 	= Bean.getDecodeParam(parameters.get("col"));
	l_client_order_column			= Bean.checkFindString(pageFormName + tagClientOrderColumn, l_client_order_column, l_client_page);
	
	String l_client_order_type 		= Bean.getDecodeParam(parameters.get("order"));
	l_client_order_type			= Bean.checkFindString(pageFormName + tagClientOrderType, l_client_order_type, l_client_page);
	
	%>
   <table <%= Bean.getTableBottomFilter() %>>
		<tr>
		<%= Bean.getFindHTML("client_find", client_find, "../crm/call_center/call_groupspecs.jsp?id=" + id + "&client_page=1") %>

		<%=Bean.getSelectOnChangeBeginHTML("quest_type", "../crm/call_center/call_groupspecs.jsp?id=" + id + "&client_page=1", Bean.call_centerXML.getfieldTransl("cc_question_type", false)) %>
			<%= Bean.getCallCenterQuestionTypeOptions(quest_type, true) %>
		<%=Bean.getSelectOnChangeEndHTML() %>
		<%=Bean.getSelectOnChangeBeginHTML("quest_status", "../crm/call_center/call_groupspecs.jsp?id=" + id + "&client_page=1", Bean.call_centerXML.getfieldTransl("cc_question_status", false)) %>
			<%= Bean.getCallCenterQuestionStatusOptions(quest_status, true) %>
		<%=Bean.getSelectOnChangeEndHTML() %>
		</tr>
	</table>
	<%=group.getClientsHTML(client_find, quest_type, quest_status, l_client_page_beg, l_client_page_end, l_client_order_column, l_client_order_type) %>
<% }

} %>
</div></div>
</body>
</html>
