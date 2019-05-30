<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%@page import="bc.objects.bcClubCardOperationObject"%>
<%@page import="bc.objects.bcClubCardObject"%>
<%@page import="java.util.HashMap"%>
<%@page import="bc.objects.bcClubCardPurseObject"%><html>

<%=Bean.getLogOutScript(request)%>

<head>
	<%=Bean.getMetaContent()%>
	<%=Bean.getBottomFrameCSS()%>

</head>

<%
	HashMap<String, String> parameters = Bean.getUtfParameters(request.getQueryString());

	String pageFormName = "CARDS_CARD_TASKS";
	String tagActions = "_ACTION";
	String tagActionFind = "_ACTION_FIND";
	String tagActionState = "_ACTION_STATE";
	String tagPostings = "_POSTINGS";
	String tagPostingFind = "_POSTING_FIND";
	String tagPostingBKOper = "_POSTING_BK_OPER";

	Bean.setJspPageForTabName(pageFormName);

	String id = Bean.getDecodeParam(parameters.get("id"));
	String type = Bean.getDecodeParam(parameters.get("type"));
	String tab = Bean.getDecodeParam(parameters.get("tab"));
	tab = Bean.isEmpty(tab)?Bean.tabsHmGetValue(pageFormName):tab;

	String l_action_page = Bean.getDecodeParam(parameters.get("action_page"));
	Bean.pageCheck(pageFormName + tagActions, l_action_page);
	String l_action_page_beg = Bean.getFirstRowNumber(pageFormName + tagActions);
	String l_action_page_end = Bean.getLastRowNumber(pageFormName + tagActions);
	
	String action_find 	= Bean.getDecodeParam(parameters.get("action_find"));
	action_find 	= Bean.checkFindString(pageFormName + tagActionFind, action_find, l_action_page);

	String action_state	= Bean.getDecodeParam(parameters.get("action_state"));
	action_state 		= Bean.checkFindString(pageFormName + tagActionState, action_state, l_action_page);
	
	String l_posting_page = Bean.getDecodeParam(parameters.get("posting_page"));
	Bean.pageCheck(pageFormName + tagPostings, l_posting_page);
	String l_posting_page_beg = Bean.getFirstRowNumber(pageFormName + tagPostings);
	String l_posting_page_end = Bean.getLastRowNumber(pageFormName + tagPostings);
	
	String posting_find 	= Bean.getDecodeParam(parameters.get("posting_find"));
	posting_find 	= Bean.checkFindString(pageFormName + tagPostingFind, posting_find, l_posting_page);

	String posting_bk_oper	= Bean.getDecodeParam(parameters.get("posting_bk_oper"));
	posting_bk_oper 		= Bean.checkFindString(pageFormName + tagPostingBKOper, posting_bk_oper, l_posting_page);

	if (Bean.isEmpty(id)) {
%>
	<%=Bean.getIDNotFoundMessage()%>
<%
	} else {
		Bean.tabsHmSetValue(pageFormName, tab);
		bcClubCardOperationObject operation = new bcClubCardOperationObject(id);
		boolean hasEditParamPermission = false;

		if (Bean.currentMenu.isCurrentTabAndEditPermitted("CARDS_CARD_TASKS_INFO")) {
			hasEditParamPermission = true;
		}
%>
<body>
<div id="div_tabsheet">

	<table <%=Bean.getTableMenuParam()%>>
		<tr>
			<%=Bean.getPageHeader()%>
			<% if (Bean.currentMenu.isCurrentTabAndEditPermitted("CARDS_CARD_TASKS_INFO")) {%>
			    <%=Bean.getMenuButton("ADD","../crm/cards/card_tasksupdate.jsp?id=" + id+ "&type=general&action=add2&process=no","", "")%>
				<% if ("NEW".equalsIgnoreCase(operation.getValue("CD_CARD_OPER_STATE"))) {%>
				    <%= Bean.getMenuButton("DELETE","../crm/cards/card_tasksupdate.jsp?id=" + id + "&type=general&action=remove&process=yes",Bean.card_taskXML.getfieldTransl("h_task_delete",false),operation.getValue("ID_CARD_OPERATION") + " - " + operation.getValue("NAME_CARD_OPERATION_TYPE"))%>
				<% } %>
			<% } %>

			<% if (Bean.currentMenu .isCurrentTabAndAccessPermitted("CARDS_CARD_TASKS_ACTIONS")) { %>
			    <!-- Вывод страниц -->
				<%=Bean.getPagesHTML(pageFormName + tagActions, "../crm/cards/card_taskspecs.jsp?id=" + id + "&tab=" + Bean.currentMenu.getTabID("CARDS_CARD_TASKS_ACTIONS") + "&", "action_page")%>
			<% } %>
			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CARDS_CARD_TASKS_POSTINGS")) { %>
				<% if (Bean.currentMenu .isCurrentTabAndEditPermitted("CARDS_CARD_TASKS_POSTINGS")) { %>
				    <%=Bean.getMenuButton("ADD", "../crm/cards/card_tasksupdate.jsp?id=" + id + "&type=posting&action=add&process=no&id_club=" + operation.getValue("ID_CLUB"),"", "")%>
				    <%=Bean.getMenuButton("DELETE_ALL", "../crm/cards/card_tasksupdate.jsp?id=" + id + "&type=posting&action=removeall&process=yes", Bean.postingXML.getfieldTransl("LAB_DELETE_ALL_POSTINGS", false), "")%>
				    <%=Bean.getMenuButton("POSTING", "../crm/cards/card_tasksupdate.jsp?id=" + id + "&type=posting&action=run&process=yes&id_club=" + operation.getValue("ID_CLUB"), Bean.postingXML.getfieldTransl("LAB_RUN_POSTINGS_CARD_TASKS", false), id, Bean.postingXML.getfieldTransl("LAB_RUN_POSTINGS_CARD_TASKS", false))%>
				<% } %>
			    <!-- Вывод страниц -->
				<%=Bean.getPagesHTML(pageFormName + tagPostings, "../crm/cards/card_taskspecs.jsp?id=" + id + "&tab=" + Bean.currentMenu.getTabID("CARDS_CARD_TASKS_POSTINGS") + "&", "posting_page")%>
			<% } %>
		</tr>
	</table>
	<table <%=Bean.getTableMenuParam()%>>
		<%=Bean.getDetailCaption(operation .getValue("ID_CARD_OPERATION") + " - " + operation.getValue("NAME_CARD_OPERATION_TYPE"))%>
		<tr>
			<td>
				<!-- Выводим перечень закладок -->
				<%=Bean.currentMenu.getFormTabScheeds(tab, "../crm/cards/card_taskspecs.jsp?type=" + type + "&id=" + id)%>
			</td>
		</tr>
	</table>
</div>
<div id="div_data">
<div id="div_data_detail">

<%
	if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CARDS_CARD_TASKS_INFO")) {
			bcClubCardObject card = null;
			if ("WRITE_OFF_BON".equalsIgnoreCase(operation.getValue("CD_CARD_OPERATION_TYPE"))) {
				card = new bcClubCardObject(operation.getValue("CARD_SERIAL_NUMBER"), operation.getValue("CARD_ID_ISSUER"), operation.getValue("CARD_ID_PAYMENT_SYSTEM"));
			}

			bcClubCardPurseObject purse = null;
			if ("ADD_GOODS_TO_PURSE".equalsIgnoreCase(operation.getValue("CD_CARD_OPERATION_TYPE")) ||
					"WRITE_OFF_GOODS_FROM_PURSE".equalsIgnoreCase(operation.getValue("CD_CARD_OPERATION_TYPE"))) {
				purse = new bcClubCardPurseObject(operation.getValue("ID_CARD_PURSE"));
			}

			if (!hasEditParamPermission) {
%>
	<form>
	<table <%=Bean.getTableDetailParam()%>>
		<tr>
			<td><%=Bean.card_taskXML.getfieldTransl("ID_CARD_OPERATION",false)%></td> <td><input type="text" name="id_card_operation" size="20" value="<%=operation.getValue("ID_CARD_OPERATION")%>" readonly="readonly" class="inputfield-ro"> </td>
			<td><%=Bean.clubXML.getfieldTransl("club", false)%>
					<%=Bean.getGoToClubLink(operation.getValue("ID_CLUB"))%>
			</td><td><input type="text" name="name_club" size="40" value="<%=Bean.getClubShortName(operation.getValue("ID_CLUB"))%>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td><%=Bean.card_taskXML.getfieldTransl("cd_card1", false)%>
				<%=Bean.getGoToClubCardLink(
						operation.getValue("CARD_SERIAL_NUMBER"),
						operation.getValue("CARD_ID_ISSUER"),
						operation.getValue("CARD_ID_PAYMENT_SYSTEM"))%>
			</td> 
			<td><input type="text" name="cd_card1" id = "cd_card1" size="40" value="<%=operation.getValue("CD_CARD1")%>" readonly="readonly" class="inputfield-ro" title="CD_CARD"></td>			
			<td><%=Bean.card_taskXML.getfieldTransl("cd_card_oper_state",false)%></td> <td>
				<input type="hidden" name="cd_card_oper_state" value="<%=operation.getValue("CD_CARD_OPER_STATE")%>">
				<input type="text" name="name_card_oper_state" size="40" value="<%=Bean.getClubCardOperationStateName(operation.getValue("CD_CARD_OPER_STATE"))%>" readonly="readonly" class="inputfield-ro"> 
			</td>
		</tr>
		<% if ("SET_CATEGORIES_ON_PERIOD".equalsIgnoreCase(operation.getValue("CD_CARD_OPERATION_TYPE"))) {	%>
		<tr>
			<td><%=Bean.card_taskXML.getfieldTransl("CD_CARD_OPERATION_TYPE", false)%>
			 	<%=Bean.getUWndHelp(Bean.card_taskXML.getfieldTransl("help_"+operation.getValue("CD_CARD_OPERATION_TYPE"), false)) %>
			</td> <td><input type="text" name="name_card_operation_type" size="40" value="<%=operation.getValue("NAME_CARD_OPERATION_TYPE")%>" readonly="readonly" class="inputfield-ro"> </td>
			<td><%=Bean.card_taskXML.getfieldTransl("begin_period_date", false)%></td> <td><input type="text" name="begin_action_date" size="20" value="<%=operation.getValue("BEGIN_ACTION_DATE_FRMT")%>" readonly="readonly" class="inputfield-ro"> </td>
		</tr>
		<tr>
			<% String app_card_resp_action = Bean.getMeaningForNumValue( "APP_CARD_RESP_ACTION", operation.getValue("APP_CARD_RESP_ACTION"));
			%>
			<td><%=Bean.card_taskXML.getfieldTransl("app_card_resp_action", false)%></td> <td><input type="text" name="app_card_resp_action" size="40" value="<%=operation.getValue("APP_CARD_RESP_ACTION") + " - " + app_card_resp_action%>" readonly="readonly" class="inputfield-ro" title = "<%=operation.getValue("APP_CARD_RESP_ACTION") + " - " + app_card_resp_action%>"> </td>
			<td><%=Bean.card_taskXML.getfieldTransl("end_period_date", false)%></td> <td><input type="text" name="end_action_date" size="20" value="<%=operation.getValue("END_ACTION_DATE_FRMT")%>" readonly="readonly" class="inputfield-ro"> 
			  <font color="green">&nbsp;(<%=operation.getValue("PERIOD_DURATION")%>&nbsp;<%=Bean.commonXML.getfieldTransl("h_days",false)%>)</font>
			</td>
		</tr>
		<tr>
			<td><%=Bean.card_taskXML.getfieldTransl("PRIORITY", false)%></td> <td><input type="text" name="priority" size="20" value="<%=operation.getValue("PRIORITY")%>" readonly="readonly" class="inputfield-ro"> </td>
			<td><%=Bean.card_taskXML.getfieldTransl("need_apply", false)%></td> <td><input type="text" name="need_apply" size="20" value="<%=Bean.getMeaningFoCodeValue("YES_NO",operation.getValue("NEED_APPLY"))%>" readonly="readonly" class="inputfield-ro"> </td>
		</tr>
		<% } else if ("ADD_GOODS_TO_PURSE".equalsIgnoreCase(operation.getValue("CD_CARD_OPERATION_TYPE")) ||
				"WRITE_OFF_GOODS_FROM_PURSE".equalsIgnoreCase(operation.getValue("CD_CARD_OPERATION_TYPE"))) {	%>
		<tr>
			<td><%=Bean.card_taskXML.getfieldTransl("CD_CARD_OPERATION_TYPE", false)%>
			 	<%=Bean.getUWndHelp(Bean.card_taskXML.getfieldTransl("help_"+operation.getValue("CD_CARD_OPERATION_TYPE"), false)) %>
			</td> <td><input type="text" name="name_card_operation_type" size="40" value="<%=operation.getValue("NAME_CARD_OPERATION_TYPE")%>" readonly="readonly" class="inputfield-ro"> </td>
			<td><%=Bean.card_taskXML.getfieldTransl("begin_period_date", false)%></td> <td><input type="text" name="begin_action_date" size="20" value="<%=operation.getValue("BEGIN_ACTION_DATE_FRMT")%>" readonly="readonly" class="inputfield-ro"> </td>
		</tr>
		<tr>
			<td><%=Bean.card_taskXML.getfieldTransl("PRIORITY", false)%></td> <td><input type="text" name="priority" size="20" value="<%=operation.getValue("PRIORITY")%>" readonly="readonly" class="inputfield-ro"> </td>
			<td><%=Bean.card_taskXML.getfieldTransl("need_apply", false)%></td> <td><input type="text" name="need_apply" size="20" value="<%=Bean.getMeaningFoCodeValue("YES_NO",operation.getValue("NEED_APPLY"))%>" readonly="readonly" class="inputfield-ro"> </td>
		</tr>
		<% } else { %>
		<tr>
			<td><%=Bean.card_taskXML.getfieldTransl("CD_CARD_OPERATION_TYPE", false)%>
			 	<%=Bean.getUWndHelp(Bean.card_taskXML.getfieldTransl("help_"+operation.getValue("CD_CARD_OPERATION_TYPE"), false)) %>
			</td> <td><input type="text" name="name_card_operation_type" size="40" value="<%=operation.getValue("NAME_CARD_OPERATION_TYPE")%>" readonly="readonly" class="inputfield-ro"> </td>
			<td><%=Bean.card_taskXML.getfieldTransl("begin_period_date", false)%></td> <td><input type="text" name="begin_action_date" size="20" value="<%=operation.getValue("BEGIN_ACTION_DATE_FRMT")%>" readonly="readonly" class="inputfield-ro"> </td>
		</tr>
		<tr>
			<% String app_card_resp_action = Bean.getMeaningForNumValue( "APP_CARD_RESP_ACTION", operation.getValue("APP_CARD_RESP_ACTION"));
			%>
			<td><%=Bean.card_taskXML.getfieldTransl("app_card_resp_action", false)%></td> <td><input type="text" name="app_card_resp_action" size="40" value="<%=operation.getValue("APP_CARD_RESP_ACTION") + " - " + app_card_resp_action%>" readonly="readonly" class="inputfield-ro" title = "<%=operation.getValue("APP_CARD_RESP_ACTION") + " - " + app_card_resp_action%>"> </td>
			<td><%=Bean.card_taskXML.getfieldTransl("need_apply", false)%></td> <td><input type="text" name="need_apply" size="20" value="<%=Bean.getMeaningFoCodeValue("YES_NO",operation.getValue("NEED_APPLY"))%>" readonly="readonly" class="inputfield-ro"> </td>
		</tr>
		<tr>
			<td><%=Bean.card_taskXML.getfieldTransl("PRIORITY", false)%></td> <td><input type="text" name="priority" size="20" value="<%=operation.getValue("PRIORITY")%>" readonly="readonly" class="inputfield-ro"> </td>
			<td colspan="2">&nbsp;</td>
		</tr>
		<% } %>

		<tr>
			<td rowspan="2"><%=Bean.card_taskXML.getfieldTransl("basis_for_operation",false)%></td><td  colspan="3" rowspan="2"><textarea name="basis_for_operation" cols="120" rows="3" readonly="readonly" class="inputfield-ro"><%=operation.getValue("BASIS_FOR_OPERATION")%></textarea></td>
		</tr>
		<tr>
		</tr>

		<%
			if ("ADD_GOODS_TO_PURSE".equalsIgnoreCase(operation.getValue("CD_CARD_OPERATION_TYPE"))) {
		%>
		<tr>
		    <td colspan="4" class="top_line"><b><%=Bean.card_taskXML.getfieldTransl("tag_param", false)%></b></td>
		</tr>
		<tr>
			<td><%= Bean.card_taskXML.getfieldTransl("card_purse", false) %>
				<%= Bean.getGoToClubCardPurseLink(purse.getValue("ID_CARD_PURSE")) %>
			</td> 
			<td>
				<input type="hidden" name="id_card_purse" value="<%= purse.getValue("ID_CARD_PURSE") %>">
				<input type="text" name="name_card_purse" size="40" value="<%= purse.getValue("NUMBER_CARD_PURSE") %> - <%= purse.getValue("NAME_CARD_PURSE_TYPE") %>" readonly="readonly" class="inputfield-ro">
			</td>
			<td colspan="2">&nbsp;</td> 
		</tr>
		<tr>
			<td><%=Bean.card_taskXML.getfieldTransl("h_value_card_purse_add", false)%></td> <td><input type="text" name="value_card_purse" size="20" value="<%=operation.getValue("VALUE_CARD_PURSE_FRMT")%>" readonly="readonly" class="inputfield-ro"></td>
			<td colspan="2">&nbsp;</td> 
		</tr>

		<%
			} else if ("WRITE_OFF_GOODS_FROM_PURSE".equalsIgnoreCase(operation.getValue("CD_CARD_OPERATION_TYPE"))) {
				
				double cardPurseValue = 0;
				double operationPurseValue = 0;
				if (!Bean.isEmpty(purse.getValue("VALUE_CARD_PURSE"))) {
					cardPurseValue = Double.parseDouble(purse.getValue("VALUE_CARD_PURSE"))/100;
				}
				if (!Bean.isEmpty(operation.getValue("VALUE_CARD_PURSE"))) {
					operationPurseValue = Double.parseDouble(operation.getValue("VALUE_CARD_PURSE"))/100;
				}

			%>
			
		<tr>
		    <td colspan="4" class="top_line"><b><%=Bean.card_taskXML.getfieldTransl("tag_param", false)%></b>
			<%
			if (cardPurseValue < operationPurseValue) {
				%>
					<b><font color="red">&nbsp;(<%=Bean.card_taskXML.getfieldTransl("h_exception_in_purse_value",false)%>)</font></b>
				<%
					}
			%>
			</td>
		</tr>
		<tr>
			<td><%= Bean.card_taskXML.getfieldTransl("card_purse", false) %>
				<%= Bean.getGoToClubCardPurseLink(purse.getValue("ID_CARD_PURSE")) %>
			</td> 
			<td>
				<input type="hidden" name="id_card_purse" value="<%= purse.getValue("ID_CARD_PURSE") %>">
				<input type="text" name="name_card_purse" size="40" value="<%= purse.getValue("NUMBER_CARD_PURSE") %> - <%= purse.getValue("NAME_CARD_PURSE_TYPE") %>" readonly="readonly" class="inputfield-ro">
			</td>
			<td colspan="2">&nbsp;</td> 
		</tr>
		<tr>
			<td><%=Bean.card_taskXML.getfieldTransl("h_value_card_purse_write_off", false)%></td> <td><input type="text" name="value_card_purse" size="20" value="<%=operation.getValue("VALUE_CARD_PURSE_FRMT")%>" readonly="readonly" class="inputfield-ro">
				(<%=Bean.card_taskXML.getfieldTransl("h_has_purse_value",false)%>&nbsp;
				<b><span style="color:green"><%="" + cardPurseValue%></span></b>)
            </td>
			<td colspan="2">&nbsp;</td> 
		</tr>

		<%
			} else if ("ADD_BON".equalsIgnoreCase(operation.getValue("CD_CARD_OPERATION_TYPE"))) {
		%>
		<tr>
		    <td colspan="4" class="top_line"><b><%=Bean.card_taskXML.getfieldTransl("tag_param", false)%></b></td>
		</tr>
		<tr>
			<td><%=Bean.card_taskXML.getfieldTransl("h_bal_acc_add", false)%></td> 
            <td><input type="text" name="bal_acc" size="20" value="<%=operation.getValue("BAL_ACC_FRMT")%>" readonly="readonly" class="inputfield-ro"></td>
			<td><%=Bean.card_taskXML.getfieldTransl("date_acc", false)%></td> <td><input type="text" name="date_acc" size="20" value="<%=operation.getValue("DATE_ACC_FRMT")%>" readonly="readonly" class="inputfield-ro"> </td>
		</tr>
		<tr>
			<td><%=Bean.card_taskXML.getfieldTransl("h_bal_cur_add", false)%></td> 
            <td><input type="text" name="bal_cur" size="20" value="<%=operation.getValue("BAL_CUR_FRMT")%>" readonly="readonly" class="inputfield-ro"></td>

            <td colspan="2">&nbsp;</td>
		</tr>
		<tr>
			<td valign="top"><%=Bean.card_taskXML.getfieldTransl("text_message",false)%></td><td  colspan="3"><textarea name="text_message" cols="120" rows="2" readonly="readonly" class="inputfield-ro"><%=operation.getValue("TEXT_MESSAGE")%></textarea></td>
		</tr>

		<%
			} else if ("WRITE_OFF_BON".equalsIgnoreCase(operation.getValue("CD_CARD_OPERATION_TYPE"))) {
		%>
		<tr>
		    <td colspan="4" class="top_line"><b><%=Bean.card_taskXML.getfieldTransl("tag_param", false)%></b>
			<%
				double cardBalAcc = 0;
				double cardBalCur = 0;
				double cardBalFull = 0;
				double operationBalFull = 0;

				if (!Bean.isEmpty(card.getValue("BAL_ACC"))) {
					cardBalAcc = Double.parseDouble(card.getValue("BAL_ACC"))/100;
				}
				if (!Bean.isEmpty(card.getValue("BAL_CUR"))) {
					cardBalCur = Double.parseDouble(card.getValue("BAL_CUR"))/100;
				}
				cardBalFull = cardBalAcc + cardBalCur;
				if (!Bean.isEmpty(operation.getValue("BAL_FULL"))) {
					operationBalFull = Double.parseDouble(operation.getValue("BAL_FULL"))/100;
				}

				if (cardBalFull < operationBalFull) {
			%>
				<b><font color="red">&nbsp;(<%=Bean.card_taskXML.getfieldTransl("h_exception_in_bon",false)%>)</font></b>
			<%
				}
			%>
			</td>
		</tr>
		<tr>
			<td><%=Bean.card_taskXML.getfieldTransl("h_bal_delete", false)%></td> 
            <td  colspan="3"><input type="text" name="bal_full" size="20" value="<%=operation.getValue("BAL_FULL_FRMT")%>" readonly="readonly" class="inputfield-ro">
				(<%=Bean.card_taskXML.getfieldTransl("h_has_bon",false)%>&nbsp;
				<b>
					<span style="color:green" title="<%= Bean.getClubCardXMLFieldTransl("BAL_ACC", false) %>"><%="" + cardBalAcc%></span>&nbsp;+
					<span style="color:green" title="<%= Bean.getClubCardXMLFieldTransl("BAL_CUR", false) %>"><%="" + cardBalCur%></span>&nbsp;=
					<span style="color:green" title="<%= Bean.getClubCardXMLFieldTransl("BAL_FULL", false) %>"><%="" + cardBalFull%></span>
				</b>)
            </td>
		</tr>
		<tr>
			<td valign="top"><%=Bean.card_taskXML.getfieldTransl("text_message",false)%></td><td  colspan="3"><textarea name="text_message" cols="120" rows="2" readonly="readonly" class="inputfield-ro"><%=operation.getValue("TEXT_MESSAGE")%></textarea></td>
		</tr>

		<%
			} else if ("CHANGE_PARAM".equalsIgnoreCase(operation.getValue("CD_CARD_OPERATION_TYPE"))) {
		%>
		<tr>
		    <td colspan="4" class="top_line"><b><%=Bean.card_taskXML.getfieldTransl("tag_param", false)%></b></td>
		</tr>
		<tr>
			<td><%=Bean.card_taskXML.getfieldTransl("id_card_status",false)%></td> <td><input type="text" name="id_card_status" size="20" value="<%=Bean.getCardStatusName(operation.getValue("ID_CARD_STATUS"))%>" readonly="readonly" class="inputfield-ro"> </td>
			<td><%=Bean.getClubCardXMLFieldTransl("bal_acc", false)%></td> <td><input type="text" name="bal_acc" size="20" value="<%=operation.getValue("BAL_ACC_FRMT")%>" readonly="readonly" class="inputfield-ro" title="BAL_ACC"> </td>
		</tr>
		<tr>
			<td><%=Bean.card_taskXML.getfieldTransl("id_bon_category",false)%></td> <td class="top_line_gray"><input type="text" name="id_bon_category" size="20" value="<%=Bean.getCardCategoryName2(operation.getValue("ID_BON_CATEGORY"))%>" readonly="readonly" class="inputfield-ro"> </td>
			<td><%=Bean.getClubCardXMLFieldTransl("bal_cur", false)%></td> <td><input type="text" name="bal_cur" size="20" value="<%=operation.getValue("BAL_CUR_FRMT")%>" readonly="readonly" class="inputfield-ro" title="BAL_CUR"> </td>
		</tr>
		<tr>
			<td>&nbsp;</td>
			<td>
				<%
					String bonReductionChecked = "";
					String bonReductionLabel = Bean.card_taskXML.getfieldTransl("can_bon_category_reduction",false);
					if ("Y".equalsIgnoreCase(operation.getValue("CAN_BON_CATEGORY_REDUCTION"))) {
						bonReductionChecked = " CHECKED ";
						bonReductionLabel = "<b>" + Bean.card_taskXML.getfieldTransl("can_bon_category_reduction",false) + "</b>";
					}
				%>
				<input type="checkbox" name="can_bon_category_reduction" id="can_bon_category_reduction" disabled value="Y" <%=bonReductionChecked %>>
				<label class="checbox_label" for="can_bon_category_reduction"><%=bonReductionLabel %></label>
			</td>
			<td><%=Bean.getClubCardXMLFieldTransl("bal_bon_per",false)%></td> <td><input type="text" name="bal_bon_per" size="20" value="<%=operation.getValue("BAL_BON_PER_FRMT")%>" readonly="readonly" class="inputfield-ro" title="BAL_BON_PER"> </td>
		</tr>
		<tr>
			<td><%=Bean.card_taskXML.getfieldTransl("id_disc_category",false)%></td> <td class="top_line_gray"><input type="text" name="id_disc_category" size="20" value="<%=Bean.getCardCategoryName2(operation.getValue("ID_DISC_CATEGORY"))%>" readonly="readonly" class="inputfield-ro"> </td>
			<td><%=Bean.getClubCardXMLFieldTransl("bal_disc_per",false)%></td> <td valign="top"><input type="text" name="bal_disc_per" size="20" value="<%=operation.getValue("BAL_DISC_PER_FRMT")%>" readonly="readonly" class="inputfield-ro" title="BAL_DISC_PER"> </td>
		</tr>
		<tr>
			<td>&nbsp;</td>
			<td>
				<%
					String discReductionChecked = "";
					String discReductionLabel = Bean.card_taskXML.getfieldTransl("can_disc_category_reduction",false);
					if ("Y".equalsIgnoreCase(operation.getValue("CAN_DISC_CATEGORY_REDUCTION"))) {
						discReductionChecked = " CHECKED ";
						discReductionLabel = "<b>" + Bean.card_taskXML.getfieldTransl("can_disc_category_reduction",false) + "</b>";
					}
				%>
				<input type="checkbox" name="can_disc_category_reduction" id="can_disc_category_reduction" disabled value="Y" <%=discReductionChecked %>>
				<label class="checbox_label" for="can_disc_category_reduction"><%=discReductionLabel %></label>
			</td>
			<td class="top_line_gray"><%=Bean.getClubCardXMLFieldTransl("date_acc", false)%></td> <td class="top_line_gray"><input type="text" name="date_acc" size="20" value="<%=operation.getValue("DATE_ACC_FRMT")%>" readonly="readonly" class="inputfield-ro" title="DATE_ACC"> </td>
		</tr>
		<tr>
			<td colspan="2">&nbsp;</td>
			<td><%=Bean.getClubCardXMLFieldTransl("date_mov", false)%></td> <td><input type="text" name="date_mov" size="20" value="<%=operation.getValue("DATE_MOV_FRMT")%>" readonly="readonly" class="inputfield-ro" title="DATE_MOV"> </td>
		</tr>
		<tr>
			<td valign="top"><%=Bean.card_taskXML.getfieldTransl("text_message",false)%></td><td  colspan="3"><textarea name="text_message" cols="120" rows="2" readonly="readonly" class="inputfield-ro"><%=operation.getValue("TEXT_MESSAGE")%></textarea></td>
		</tr>
		<%
			} else if ("SET_CATEGORIES_ON_PERIOD".equalsIgnoreCase(operation.getValue("CD_CARD_OPERATION_TYPE"))) {
		%>
		<tr>
		    <td colspan="4" class="top_line"><b><%=Bean.card_taskXML.getfieldTransl("tag_param", false)%></b></td>
		</tr>
		<tr>
			<td><%=Bean.card_taskXML.getfieldTransl("id_card_status",false)%></td> <td><input type="text" name="id_card_status" size="20" value="<%=Bean.getCardStatusName(operation.getValue("ID_CARD_STATUS"))%>" readonly="readonly" class="inputfield-ro"> </td>
		</tr>
		<tr>
			<td><%=Bean.card_taskXML.getfieldTransl("id_bon_category",false)%></td> <td><input type="text" name="id_bon_category" size="20" value="<%=Bean.getCardCategoryName2(operation.getValue("ID_BON_CATEGORY"))%>" readonly="readonly" class="inputfield-ro"> </td>
			<td><%=Bean.card_taskXML.getfieldTransl("id_disc_category",false)%></td> <td><input type="text" name="id_disc_category" size="20" value="<%=Bean.getCardCategoryName2(operation.getValue("ID_DISC_CATEGORY"))%>" readonly="readonly" class="inputfield-ro"> </td>
		</tr>
		<tr>
			<td>&nbsp;</td>
			<td>
				<%
					String bonReductionChecked = "";
					String bonReductionLabel = Bean.card_taskXML.getfieldTransl("can_bon_category_reduction",false);
					if ("Y".equalsIgnoreCase(operation.getValue("CAN_BON_CATEGORY_REDUCTION"))) {
						bonReductionChecked = " CHECKED ";
						bonReductionLabel = "<b>" + Bean.card_taskXML.getfieldTransl("can_bon_category_reduction",false) + "</b>";
					}
				%>
				<input type="checkbox" name="can_bon_category_reduction" id="can_bon_category_reduction" disabled value="Y" <%=bonReductionChecked %>>
				<label class="checbox_label" for="can_bon_category_reduction"><%=bonReductionLabel %></label>
			</td>
			<td>&nbsp;</td>
			<td>
				<%
					String discReductionChecked = "";
					String discReductionLabel = Bean.card_taskXML.getfieldTransl("can_disc_category_reduction",false);
					if ("Y".equalsIgnoreCase(operation.getValue("CAN_DISC_CATEGORY_REDUCTION"))) {
						discReductionChecked = " CHECKED ";
						discReductionLabel = "<b>" + Bean.card_taskXML.getfieldTransl("can_disc_category_reduction",false) + "</b>";
					}
				%>
				<input type="checkbox" name="can_disc_category_reduction" id="can_disc_category_reduction" disabled value="Y" <%=discReductionChecked %>>
				<label class="checbox_label" for="can_disc_category_reduction"><%=discReductionLabel %></label>
			</td>
		</tr>
		<tr>
			<td valign="top"><%=Bean.card_taskXML.getfieldTransl("text_message",false)%></td><td  colspan="3"><textarea name="text_message" cols="120" rows="2" readonly="readonly" class="inputfield-ro"><%=operation.getValue("TEXT_MESSAGE")%></textarea></td>
		</tr>
		<%
			} else if ("BLOCK_CARD".equalsIgnoreCase(operation.getValue("CD_CARD_OPERATION_TYPE"))) {
		%>
		<tr>
		    <td colspan="4" class="top_line"><b><%=Bean.card_taskXML.getfieldTransl("tag_param", false)%></b></td>
		</tr>
		<tr>
			<td valign="top"><%=Bean.card_taskXML.getfieldTransl("text_message",false)%></td><td  colspan="3"><textarea name="text_message" cols="120" rows="2" readonly="readonly" class="inputfield-ro"><%=operation.getValue("TEXT_MESSAGE")%></textarea></td>
		</tr>
		<%
			} else if ("SEND_MESSAGE".equalsIgnoreCase(operation.getValue("CD_CARD_OPERATION_TYPE"))) {
		%>
		<tr>
		    <td colspan="4" class="top_line"><b><%=Bean.card_taskXML.getfieldTransl("tag_param", false)%></b></td>
		</tr>
		<tr>
			<td valign="top"><%=Bean.card_taskXML.getfieldTransl("text_message",false)%></td><td  colspan="3"><textarea name="text_message" cols="120" rows="2" readonly="readonly" class="inputfield-ro"><%=operation.getValue("TEXT_MESSAGE")%></textarea></td>
		</tr>
		<%
			}
		%>
		<tr>
		    <td colspan="4" class="top_line"><b><%=Bean.card_taskXML.getfieldTransl("tag_source_param",false)%></b></td>
		</tr>
		<tr>
			<td><%=Bean.card_taskXML.getfieldTransl("id_sequrity_monitor",false)%></td> <td><input type="text" name="id_sequrity_monitor" size="20" value="<%=operation.getValue("ID_SEQURITY_MONITOR")%>" readonly="readonly" class="inputfield-ro"> </td>
			<td><%=Bean.card_taskXML.getfieldTransl("id_previous_operation", false)%>
				<%=Bean.getGoToCardTaskLink(operation.getValue("ID_PREVIOUS_OPERATION"))
				%>
			  </td> <td><input type="text" name="id_previous_operation" size="20" value="<%=operation.getValue("ID_PREVIOUS_OPERATION")%>" readonly="readonly" class="inputfield-ro"> </td>
		</tr>
		<tr>
			<td><%=Bean.card_taskXML.getfieldTransl("id_remittance", false)%></td> <td><input type="text" name="id_remittance" size="20" value="<%=operation.getValue("ID_REMITTANCE")%>" readonly="readonly" class="inputfield-ro"> </td>
			<td><%=Bean.card_taskXML.getfieldTransl("id_club_event", false)%>
				<%=Bean.getGoToClubEventLink(operation.getValue("ID_CLUB_EVENT"))
				%>
			  </td> <td><input type="text" name="id_quest_int" size="20" value="<%=operation.getValue("ID_CLUB_EVENT")%>" readonly="readonly" class="inputfield-ro"> </td>
		</tr>
		<tr>
			<td><%=Bean.card_taskXML.getfieldTransl("id_quest_int", false)%>
				<%=Bean.getGoToQuestionnaireLink(operation.getValue("ID_QUEST_INT"))
				%>
			  </td> <td><input type="text" name="id_quest_int" size="20" value="<%=operation.getValue("ID_QUEST_INT")%>" readonly="readonly" class="inputfield-ro"> </td>
			<td colspan="2">&nbsp;</td>
		</tr>
		<%=	Bean.getCreationAndMoficationRecordFields(
				operation.getValue(Bean.getCreationDateFieldName()),
				operation.getValue("CREATED_BY"),
				operation.getValue(Bean.getLastUpdateDateFieldName()),
				operation.getValue("LAST_UPDATE_BY")
			) %>
		<tr>
			<td colspan="6" align="center">
				<%=Bean.getGoBackButton("../crm/cards/card_tasks.jsp")%>
			</td>
		</tr>
	</table>
	</form>

<%
	} else {
		
%>

	<script>
		var formData = new Array (
			new Array ('priority', 'varchar2', 1),
			new Array ('cd_card1', 'varchar2', 1),
			new Array ('begin_action_date', 'varchar2', 1),
			new Array ('cd_card_oper_state', 'varchar2', 1),
			new Array ('need_apply', 'varchar2', 1),
			new Array ('basis_for_operation', 'varchar2', 1)
		);
		var formDataCHANGE_PARAM = new Array (
			new Array ('id_card_status', 'varchar2', 1)
		);
		var formDataWRITE_OFF_BON = new Array (
			new Array ('bal_full', 'varchar2', 1)
		);
		var formDataSEND_MESSAGE = new Array (
			new Array ('text_message', 'varchar2', 1)
		);
		var formDataSET_CATEGORIES_ON_PERIOD = new Array (
			new Array ('end_action_date', 'varchar2', 1),
			new Array ('id_card_status', 'varchar2', 1)
		);
		var formDataPurseOperations = new Array (
			new Array ('value_card_purse', 'varchar2', 1)
		);
		<%if ("CHANGE_PARAM".equalsIgnoreCase(operation.getValue("CD_CARD_OPERATION_TYPE"))) {%>
			formData = formData.concat(formDataCHANGE_PARAM);
		<%} else if ("WRITE_OFF_BON".equalsIgnoreCase(operation.getValue("CD_CARD_OPERATION_TYPE"))) {%>
			formData = formData.concat(formDataWRITE_OFF_BON);
		<%} else if ("SET_CATEGORIES_ON_PERIOD".equalsIgnoreCase(operation.getValue("CD_CARD_OPERATION_TYPE"))) {%>
			formData = formData.concat(formDataSET_CATEGORIES_ON_PERIOD);
		<%} else if ("SEND_MESSAGE".equalsIgnoreCase(operation.getValue("CD_CARD_OPERATION_TYPE"))) {%>
			formData = formData.concat(formDataSEND_MESSAGE);
		<% } else if ("ADD_GOODS_TO_PURSE".equalsIgnoreCase(operation.getValue("CD_CARD_OPERATION_TYPE")) ||
				"WRITE_OFF_GOODS_FROM_PURSE".equalsIgnoreCase(operation.getValue("CD_CARD_OPERATION_TYPE"))) { %>
			formData = formData.concat(formDataPurseOperations);
		<%}%>

		function myValidateForm() {
			return validateForm(formData);
		}
	</script>

	<%=Bean.getMessageLengthTextAreaInitialScript("text_message", "length_message")%>

    <form action="../crm/cards/card_tasksupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">
        <input type="hidden" name="type" value="general">
        <input type="hidden" name="action" value="edit">
        <input type="hidden" name="process" value="yes">
		<input type="hidden" name="id" value="<%=id%>">
		<input type="hidden" name="cd_card_operation_type" value="<%=operation.getValue("CD_CARD_OPERATION_TYPE")%>">
		<input type="hidden" name="card_serial_number" value="<%= operation.getValue("CARD_SERIAL_NUMBER") %>">
		<input type="hidden" name="id_issuer" value="<%= operation.getValue("CARD_ID_ISSUER") %>">
		<input type="hidden" name="id_payment_system" value="<%= operation.getValue("CARD_ID_PAYMENT_SYSTEM") %>">
	<table <%=Bean.getTableDetailParam()%>>
		<tr>
			<td><%= Bean.card_taskXML.getfieldTransl("cd_card1", false) %>
				<%= Bean.getGoToClubCardLink(
						operation.getValue("CARD_SERIAL_NUMBER"),
						operation.getValue("CARD_ID_ISSUER"),
						operation.getValue("CARD_ID_PAYMENT_SYSTEM")
					) %>
			</td> 
			<td><input type="text" name="cd_card1" size="40" value="<%= operation.getValue("CD_CARD1") %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%=Bean.clubXML.getfieldTransl("club", false)%>
					<%=Bean.getGoToClubLink(operation.getValue("ID_CLUB")) %>
			</td><td><input type="text" name="name_club" size="30" value="<%=Bean.getClubShortName(operation.getValue("ID_CLUB"))%>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td><%= Bean.card_taskXML.getfieldTransl("CD_CARD_OPERATION_TYPE", false) %></td> <td><input type="text" name="name_card_operation_type" size="40" value="<%= operation.getValue("NAME_CARD_OPERATION_TYPE") %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%=Bean.card_taskXML.getfieldTransl("cd_card_oper_state", true)%></td> <td><select name="cd_card_oper_state" class="inputfield"><%=Bean.getClubCardOperationStateOptions(operation.getValue("CD_CARD_OPER_STATE"),true)%></select></td>
		</tr>
		<% if ("SET_CATEGORIES_ON_PERIOD".equalsIgnoreCase(operation.getValue("CD_CARD_OPERATION_TYPE"))) {	%>
		<tr>
			<%
				String app_card_resp_action = Bean.getMeaningForNumValue("APP_CARD_RESP_ACTION",operation.getValue("APP_CARD_RESP_ACTION"));
			%>
			<td><%=Bean.card_taskXML.getfieldTransl("app_card_resp_action",false)%></td> <td><input type="text" name="app_card_resp_action" size="40" value="<%=operation.getValue("APP_CARD_RESP_ACTION") + " - " + app_card_resp_action%>" readonly="readonly" class="inputfield-ro" title = "<%=operation.getValue("APP_CARD_RESP_ACTION") + " - " + app_card_resp_action%>"> </td>
		    <td><%=Bean.card_taskXML.getfieldTransl("begin_period_date",true)%></td><td><%=Bean.getCalendarInputField("begin_action_date", operation.getValue("BEGIN_ACTION_DATE_FRMT"), "10") %></td>
		</tr>
		<tr>
			<td><%=Bean.card_taskXML.getfieldTransl("PRIORITY", false)%></td> <td><input type="text" name="priority" size="20" value="<%=operation.getValue("PRIORITY")%>" readonly="readonly" class="inputfield-ro"> </td>
		    <td><%=Bean.card_taskXML.getfieldTransl("end_period_date",true)%></td><td><%=Bean.getCalendarInputField("end_action_date", operation.getValue("END_ACTION_DATE_FRMT"), "10") %>
			  <font color="green">&nbsp;(<%=operation.getValue("PERIOD_DURATION")%>&nbsp;<%=Bean.commonXML.getfieldTransl("h_days",false)%>)</font>
		    </td>
		</tr>
		<tr>
			<td colspan="2">&nbsp;</td>
			<td><%=Bean.card_taskXML.getfieldTransl("need_apply", true)%></td> <td><select name="need_apply" class="inputfield"><%=Bean.getYesNoLookupOptions(operation.getValue("NEED_APPLY"),false)%></select></td>
		</tr>
		<% } else if ("ADD_GOODS_TO_PURSE".equalsIgnoreCase(operation.getValue("CD_CARD_OPERATION_TYPE")) ||
				"WRITE_OFF_GOODS_FROM_PURSE".equalsIgnoreCase(operation.getValue("CD_CARD_OPERATION_TYPE"))) {	%>
		<tr>
			<td><%=Bean.card_taskXML.getfieldTransl("PRIORITY", false)%></td> <td><input type="text" name="priority" size="20" value="<%=operation.getValue("PRIORITY")%>" readonly="readonly" class="inputfield-ro"> </td>
		    <td><%=Bean.card_taskXML.getfieldTransl("begin_action_date",true)%></td><td><%=Bean.getCalendarInputField("begin_action_date", operation.getValue("BEGIN_ACTION_DATE_FRMT"), "10") %></td>
		</tr>
		<tr>
			<td colspan="2">&nbsp;</td>
			<td><%=Bean.card_taskXML.getfieldTransl("need_apply", true)%></td> <td><select name="need_apply" class="inputfield"><%=Bean.getYesNoLookupOptions(operation.getValue("NEED_APPLY"),false)%></select></td>
		</tr>
		<% } else { %>
		<tr>
			<%
				String app_card_resp_action = Bean.getMeaningForNumValue("APP_CARD_RESP_ACTION",operation.getValue("APP_CARD_RESP_ACTION"));
			%>
			<td><%=Bean.card_taskXML.getfieldTransl("app_card_resp_action",false)%></td> <td><input type="text" name="app_card_resp_action" size="40" value="<%=operation.getValue("APP_CARD_RESP_ACTION") + " - " + app_card_resp_action%>" readonly="readonly" class="inputfield-ro" title = "<%=operation.getValue("APP_CARD_RESP_ACTION") + " - " + app_card_resp_action%>"> </td>
		    <td><%=Bean.card_taskXML.getfieldTransl("begin_action_date",true)%></td><td><%=Bean.getCalendarInputField("begin_action_date", operation.getValue("BEGIN_ACTION_DATE_FRMT"), "10") %></td>
		</tr>
		<tr>
			<td><%=Bean.card_taskXML.getfieldTransl("PRIORITY", false)%></td> <td><input type="text" name="priority" size="20" value="<%=operation.getValue("PRIORITY")%>" readonly="readonly" class="inputfield-ro"> </td>
			<td><%=Bean.card_taskXML.getfieldTransl("need_apply", true)%></td> <td><select name="need_apply" class="inputfield"><%=Bean.getYesNoLookupOptions(operation.getValue("NEED_APPLY"),false)%></select></td>
		</tr>
		<% } %>
		<tr>
			<td><%=Bean.card_taskXML.getfieldTransl("basis_for_operation",true)%></td><td  colspan="3"><textarea name="basis_for_operation" cols="120" rows="3" class="inputfield"><%=operation.getValue("BASIS_FOR_OPERATION")%></textarea></td>
		</tr>

		<%
			if ("ADD_GOODS_TO_PURSE".equalsIgnoreCase(operation.getValue("CD_CARD_OPERATION_TYPE"))) {
		%>
		<tr>
		    <td colspan="4" class="top_line"><b><%=Bean.card_taskXML.getfieldTransl("tag_param", false)%></b></td>
		</tr>
		<tr>
			<td><%= Bean.card_taskXML.getfieldTransl("card_purse", false) %>
				<%= Bean.getGoToClubCardPurseLink(purse.getValue("ID_CARD_PURSE")) %>
			</td> 
			<td>
				<input type="hidden" name="id_card_purse" value="<%= purse.getValue("ID_CARD_PURSE") %>">
				<input type="text" name="name_card_purse" size="40" value="<%= purse.getValue("NUMBER_CARD_PURSE") %> - <%= purse.getValue("NAME_CARD_PURSE_TYPE") %>" readonly="readonly" class="inputfield-ro">
			</td>
			<td colspan="2">&nbsp;</td> 
		</tr>
		<tr>
			<td><%=Bean.card_taskXML.getfieldTransl("h_value_card_purse_add", true)%></td> <td><input type="text" name="value_card_purse" size="20" value="<%=operation.getValue("VALUE_CARD_PURSE_FRMT")%>" class="inputfield"></td>
			<td colspan="2">&nbsp;</td> 
		</tr>

		<%
			} else if ("WRITE_OFF_GOODS_FROM_PURSE".equalsIgnoreCase(operation.getValue("CD_CARD_OPERATION_TYPE"))) {
				
				double cardPurseValue = 0;
				double operationPurseValue = 0;
				if (!Bean.isEmpty(purse.getValue("VALUE_CARD_PURSE"))) {
					cardPurseValue = Double.parseDouble(purse.getValue("VALUE_CARD_PURSE"))/100;
				}
				if (!Bean.isEmpty(operation.getValue("VALUE_CARD_PURSE"))) {
					operationPurseValue = Double.parseDouble(operation.getValue("VALUE_CARD_PURSE"))/100;
				}

			%>
			
		<tr>
		    <td colspan="4" class="top_line"><b><%=Bean.card_taskXML.getfieldTransl("tag_param", false)%></b>
			<%
			if (cardPurseValue < operationPurseValue) {
				%>
					<b><font color="red">&nbsp;(<%=Bean.card_taskXML.getfieldTransl("h_exception_in_purse_value",false)%>)</font></b>
				<%
					}
			%>
			</td>
		</tr>
		<tr>
			<td><%= Bean.card_taskXML.getfieldTransl("card_purse", false) %>
				<%= Bean.getGoToClubCardPurseLink(purse.getValue("ID_CARD_PURSE")) %>
			</td> 
			<td>
				<input type="hidden" name="id_card_purse" value="<%= purse.getValue("ID_CARD_PURSE") %>">
				<input type="text" name="name_card_purse" size="40" value="<%= purse.getValue("NUMBER_CARD_PURSE") %> - <%= purse.getValue("NAME_CARD_PURSE_TYPE") %>" readonly="readonly" class="inputfield-ro">
			</td>
			<td colspan="2">&nbsp;</td> 
		</tr>
		<tr>
			<td><%=Bean.card_taskXML.getfieldTransl("h_value_card_purse_write_off", true)%></td> <td><input type="text" name="value_card_purse" size="20" value="<%=operation.getValue("VALUE_CARD_PURSE_FRMT")%>" class="inputfield">
				(<%=Bean.card_taskXML.getfieldTransl("h_has_purse_value",false)%>&nbsp;
				<b><span style="color:green"><%="" + cardPurseValue%></span></b>)
            </td>
			<td colspan="2">&nbsp;</td> 
		</tr>

		<%
			} else if ("ADD_BON".equalsIgnoreCase(operation.getValue("CD_CARD_OPERATION_TYPE"))) {
		%>
		<tr>
		    <td colspan="4" class="top_line"><b><%=Bean.card_taskXML.getfieldTransl("tag_param", false)%></b></td>
		</tr>
		<tr>
			<td><%=Bean.card_taskXML.getfieldTransl("h_bal_acc_add", false)%></td> <td><input type="text" name="bal_acc" size="20" value="<%=operation.getValue("BAL_ACC_FRMT")%>" class="inputfield"></td>
			<td><%=Bean.card_taskXML.getfieldTransl("date_acc", false)%></td><td><%=Bean.getCalendarInputField("date_acc", operation.getValue("DATE_ACC_FRMT"), "10") %></td>
		</tr>
		<tr>
			<td><%=Bean.card_taskXML.getfieldTransl("h_bal_cur_add", false)%></td> 
              <td><input type="text" name="bal_cur" size="20" value="<%=operation.getValue("BAL_CUR_FRMT")%>" class="inputfield"></td>
			<td colspan="2">&nbsp;</td> 
		</tr>
		<tr>
			<td valign="top"><%=Bean.card_taskXML.getfieldTransl("text_message",false)%></td><td  colspan="3"><textarea name="text_message" id="text_message" cols="120" rows="2" class="inputfield" <%=Bean.getMessageLengthTextAreaEvent("length_message")%>><%=operation.getValue("TEXT_MESSAGE")%></textarea></td>
		</tr>
		<tr>
			<td valign="top"><%=Bean.messageXML.getfieldTransl("length_message",false)%></td><td valign="top"><input type="text" name="length_message" id="length_message" size="8" value="" readonly="readonly" class="inputfield-ro"></td>
		</tr>

		<%
			} else if ("WRITE_OFF_BON".equalsIgnoreCase(operation.getValue("CD_CARD_OPERATION_TYPE"))) {
		%>
		<tr>
		    <td colspan="4" class="top_line"><b><%=Bean.card_taskXML.getfieldTransl("tag_param", false)%></b>
			<%
				double cardBalAcc = 0;
				double cardBalCur = 0;
				double cardBalFull = 0;
				double operationBalFull = 0;

				if (!Bean.isEmpty(card.getValue("BAL_ACC"))) {
					cardBalAcc = Double.parseDouble(card.getValue("BAL_ACC"))/100;
				}
				if (!Bean.isEmpty(card.getValue("BAL_CUR"))) {
					cardBalCur = Double.parseDouble(card.getValue("BAL_CUR"))/100;
				}
				cardBalFull = cardBalAcc + cardBalFull;
				if (!Bean.isEmpty(operation.getValue("BAL_FULL"))) {
					operationBalFull = Double.parseDouble(operation.getValue("BAL_FULL"))/100;
				}

				if (cardBalFull < operationBalFull) {
			%>
				<b><font color="red">&nbsp;(<%=Bean.card_taskXML.getfieldTransl("h_exception_in_bon",false)%>)</font></b>
			<%
				}
			%>
			</td>
		</tr>
		<tr>
			<td><%=Bean.card_taskXML.getfieldTransl("h_bal_delete", true)%></td> 
            <td  colspan="3"><input type="text" name="bal_full" size="20" value="<%=operation.getValue("BAL_FULL_FRMT")%>" class="inputfield">
				(<%=Bean.card_taskXML.getfieldTransl("h_has_bon",false)%>&nbsp;
				<b>
					<span style="color:green" title="<%= Bean.getClubCardXMLFieldTransl("BAL_ACC", false) %>"><%="" + cardBalAcc%></span>&nbsp;+
					<span style="color:green" title="<%= Bean.getClubCardXMLFieldTransl("BAL_CUR", false) %>"><%="" + cardBalCur%></span>&nbsp;=
					<span style="color:green" title="<%= Bean.getClubCardXMLFieldTransl("BAL_FULL", false) %>"><%="" + cardBalFull%></span>
				</b>)
            </td>
		</tr>
		<tr>
			<td valign="top"><%=Bean.card_taskXML.getfieldTransl("text_message",false)%></td><td  colspan="3"><textarea name="text_message" cols="120" rows="2" class="inputfield"><%=operation.getValue("TEXT_MESSAGE")%></textarea></td>
		</tr>
		<%
			} else if ("CHANGE_PARAM".equalsIgnoreCase(operation.getValue("CD_CARD_OPERATION_TYPE"))) {
		%>
		<tr>
		    <td colspan="4" class="top_line"><b><%=Bean.card_taskXML.getfieldTransl("tag_param", false)%></b></td>
		</tr>
		<tr>
			<td><%=Bean.card_taskXML.getfieldTransl("id_card_status",true)%></td><td><select name="id_card_status" id="id_card_status" class="inputfield" onchange="dwr_get_card_bon_disc_category3('',this,'','','<%=Bean.getSessionId()%>');"><%=Bean.getClubCardStatusOptions(operation.getValue("ID_CARD_STATUS"),false)%> </select></td>
		    <td><%=Bean.getClubCardXMLFieldTransl("date_acc", false)%></td><td><%=Bean.getCalendarInputField("date_acc", operation.getValue("DATE_ACC_FRMT"), "10") %></td>
		</tr>
		<tr>
			<td><%=Bean.card_taskXML.getfieldTransl("id_bon_category",false)%></td> 
			<td class="top_line_gray">
				<select name="id_bon_category"  id="id_bon_category" class="inputfield"><%=Bean.getClubCardCategoryForStatusOptions(operation.getValue("ID_CARD_STATUS"),operation.getValue("ID_BON_CATEGORY"),"BON",true) %></select>
			</td>
		    <td><%=Bean.getClubCardXMLFieldTransl("date_mov", false)%></td><td><%=Bean.getCalendarInputField("date_mov", operation.getValue("DATE_MOV_FRMT"), "10") %></td>
		</tr>
		<tr>
			<td>&nbsp;</td>
			<td>
				<%
					String bonReductionChecked = "";
					String bonReductionLabel = Bean.card_taskXML.getfieldTransl("can_bon_category_reduction",false);
					if ("Y".equalsIgnoreCase(operation.getValue("CAN_BON_CATEGORY_REDUCTION"))) {
						bonReductionChecked = " CHECKED ";
						bonReductionLabel = "<b><font color=\"red\">" + Bean.card_taskXML.getfieldTransl("can_bon_category_reduction",false) + "</font></b>";
					}
				%>
				<input type="checkbox" name="can_bon_category_reduction" id="can_bon_category_reduction" value="Y" <%=bonReductionChecked %>>
				<label class="checbox_label" for="can_bon_category_reduction"><%=bonReductionLabel %></label>
			 		<%=Bean.getUWndHelp(Bean.card_taskXML.getfieldTransl("help_change_param_can_category_reduction", false)) %>
			</td>
		    <td><%=Bean.getClubCardXMLFieldTransl("date_onl_next", false)%></td><td><%=Bean.getCalendarInputField("date_onl", operation.getValue("DATE_ONL_FRMT"), "10") %></td>
		</tr>
		<tr>
			<td><%=Bean.card_taskXML.getfieldTransl("id_disc_category",false)%></td> 
			<td class="top_line_gray">
				<select name="id_disc_category"  id="id_disc_category" class="inputfield"><%=Bean.getClubCardCategoryForStatusOptions(operation.getValue("ID_CARD_STATUS"),operation.getValue("ID_DISC_CATEGORY"),"DISC",true) %></select>
			</td>
			<td class="top_line_gray"><%=Bean.getClubCardXMLFieldTransl("bal_acc", false)%></td> <td class="top_line_gray"><input type="text" name="bal_acc" size="20" value="<%=operation.getValue("BAL_ACC_FRMT")%>" class="inputfield" title="BAL_ACC"> </td>
		</tr>
		<tr>
			<td>&nbsp;</td>
			<td>
				<%
					String discReductionChecked = "";
					String discReductionLabel = Bean.card_taskXML.getfieldTransl("can_disc_category_reduction",false);
					if ("Y".equalsIgnoreCase(operation.getValue("CAN_DISC_CATEGORY_REDUCTION"))) {
						discReductionChecked = " CHECKED ";
						discReductionLabel = "<b><font color=\"red\">" + Bean.card_taskXML.getfieldTransl("can_disc_category_reduction",false) + "</font></b>";
					}
				%>
				<input type="checkbox" name="can_disc_category_reduction" id="can_disc_category_reduction" value="Y" <%=discReductionChecked %>>
				<label class="checbox_label" for="can_disc_category_reduction"><%=discReductionLabel %></label>
			 		<%=Bean.getUWndHelp(Bean.card_taskXML.getfieldTransl("help_change_param_can_category_reduction", false)) %>
			</td>
			<td><%=Bean.getClubCardXMLFieldTransl("bal_cur", false)%></td> <td><input type="text" name="bal_cur" size="20" value="<%=operation.getValue("BAL_CUR_FRMT")%>" class="inputfield" title="BAL_CUR"> </td>
		</tr>
		<tr>
			<td colspan="2">&nbsp;</td>
			<td><%=Bean.getClubCardXMLFieldTransl("bal_bon_per",false)%></td> <td><input type="text" name="bal_bon_per" size="20" value="<%=operation.getValue("BAL_BON_PER_FRMT")%>" class="inputfield" title="BAL_BON_PER"> </td>
		</tr>
		<tr>
			<td colspan="2">&nbsp;</td>
			<td valign="top"><%=Bean.getClubCardXMLFieldTransl("bal_disc_per",false)%></td> <td valign="top"><input type="text" name="bal_disc_per" size="20" value="<%=operation.getValue("BAL_DISC_PER_FRMT")%>" class="inputfield" title="BAL_DISC_PER"> </td>
		</tr>
		<tr>
			<td valign="top"><%=Bean.card_taskXML.getfieldTransl("text_message", false)%></td><td  colspan="3"><textarea name="text_message" id="text_message" cols="120" rows="2" class="inputfield" <%=Bean.getMessageLengthTextAreaEvent("length_message")%>><%=operation.getValue("TEXT_MESSAGE")%></textarea></td>
		</tr>
		<tr>
			<td valign="top"><%=Bean.messageXML.getfieldTransl("length_message", false)%></td><td valign="top"><input type="text" name="length_message" id="length_message" size="8" value="" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<%
			} else if ("SET_CATEGORIES_ON_PERIOD".equalsIgnoreCase(operation.getValue("CD_CARD_OPERATION_TYPE"))) {
		%>
		<tr>
		    <td colspan="4" class="top_line"><b><%=Bean.card_taskXML.getfieldTransl("tag_param", false)%></b></td>
		</tr>
		<tr>
			<td><%=Bean.card_taskXML.getfieldTransl("id_card_status",true)%></td><td><select name="id_card_status" id="id_card_status" class="inputfield" onchange="dwr_get_card_bon_disc_category3('',this,'','','<%=Bean.getSessionId()%>');"><%=Bean.getClubCardStatusOptions(operation.getValue("ID_CARD_STATUS"),false)%> </select></td>
		    <td><%=Bean.getClubCardXMLFieldTransl("date_onl_next", false)%></td><td><%=Bean.getCalendarInputField("date_onl", operation.getValue("DATE_ONL_FRMT"), "10") %></td>
		</tr>
		<tr>
			<td><%=Bean.card_taskXML.getfieldTransl("id_bon_category",false)%></td> 
			<td class="top_line_gray">
				<select name="id_bon_category"  id="id_bon_category" class="inputfield"><%=Bean.getClubCardCategoryForStatusOptions(operation.getValue("ID_CARD_STATUS"),operation.getValue("ID_BON_CATEGORY"),"BON",true) %></select>
			</td>
			<td colspan="2">&nbsp;</td>
		</tr>
		<tr>
			<td>&nbsp;</td>
			<td>
				<%
					String bonReductionChecked = "";
					String bonReductionLabel = Bean.card_taskXML.getfieldTransl("can_bon_category_reduction",false);
					if ("Y".equalsIgnoreCase(operation.getValue("CAN_BON_CATEGORY_REDUCTION"))) {
						bonReductionChecked = " CHECKED ";
						bonReductionLabel = "<b><font color=\"red\">" + Bean.card_taskXML.getfieldTransl("can_bon_category_reduction",false) + "</font></b>";
					}
				%>
				<input type="checkbox" name="can_bon_category_reduction" id="can_bon_category_reduction" value="Y" <%=bonReductionChecked %>>
				<label class="checbox_label" for="can_bon_category_reduction"><%=bonReductionLabel %></label>
			 		<%=Bean.getUWndHelp(Bean.card_taskXML.getfieldTransl("help_set_categories_can_category_reduction", false)) %>
			</td>
			<td colspan="2">&nbsp;</td>
		</tr>
		<tr>
			<td><%=Bean.card_taskXML.getfieldTransl("id_disc_category",false)%></td> 
			<td class="top_line_gray">
				<select name="id_disc_category"  id="id_disc_category" class="inputfield"><%=Bean.getClubCardCategoryForStatusOptions(operation.getValue("ID_CARD_STATUS"),operation.getValue("ID_DISC_CATEGORY"),"DISC",true) %></select>
			</td>
			<td colspan="2">&nbsp;</td>
		</tr>
		<tr>
			<td>&nbsp;</td>
			<td>
				<%
					String discReductionChecked = "";
					String discReductionLabel = Bean.card_taskXML.getfieldTransl("can_disc_category_reduction",false);
					if ("Y".equalsIgnoreCase(operation.getValue("CAN_DISC_CATEGORY_REDUCTION"))) {
						discReductionChecked = " CHECKED ";
						discReductionLabel = "<b><font color=\"red\">" + Bean.card_taskXML.getfieldTransl("can_disc_category_reduction",false) + "</font></b>";
					}
				%>
				<input type="checkbox" name="can_disc_category_reduction" id="can_disc_category_reduction" value="Y" <%=discReductionChecked %>>
				<label class="checbox_label" for="can_disc_category_reduction"><%=discReductionLabel %></label>
			 		<%=Bean.getUWndHelp(Bean.card_taskXML.getfieldTransl("help_set_categories_can_category_reduction", false)) %>
			</td>
			<td colspan="2">&nbsp;</td>
		</tr>
		<tr>
			<td valign="top"><%=Bean.card_taskXML.getfieldTransl("text_message", false)%></td><td  colspan="3"><textarea name="text_message" id="text_message" cols="120" rows="2" class="inputfield" <%=Bean.getMessageLengthTextAreaEvent("length_message")%>><%=operation.getValue("TEXT_MESSAGE")%></textarea></td>
		</tr>
		<tr>
			<td valign="top"><%=Bean.messageXML.getfieldTransl("length_message", false)%></td><td valign="top"><input type="text" name="length_message" id="length_message" size="8" value="" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<%
			} else if ("BLOCK_CARD".equalsIgnoreCase(operation.getValue("CD_CARD_OPERATION_TYPE"))) {
		%>
		<tr>
		    <td colspan="4" class="top_line"><b><%=Bean.card_taskXML.getfieldTransl("tag_param", false)%></b></td>
		</tr>
		<tr>
			<td valign="top"><%=Bean.card_taskXML.getfieldTransl("text_message", false)%></td><td  colspan="3"><textarea name="text_message" id="text_message" cols="120" rows="2" class="inputfield" <%=Bean.getMessageLengthTextAreaEvent("length_message")%>><%=operation.getValue("TEXT_MESSAGE")%></textarea></td>
		</tr>
		<tr>
			<td valign="top"><%=Bean.messageXML.getfieldTransl("length_message",false)%></td><td valign="top"><input type="text" name="length_message" id="length_message" size="8" value="" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<%
			} else if ("SEND_MESSAGE".equalsIgnoreCase(operation.getValue("CD_CARD_OPERATION_TYPE"))) {
		%>
		<tr>
		    <td colspan="4" class="top_line"><b><%=Bean.card_taskXML.getfieldTransl("tag_param", false)%></b></td>
		</tr>
		<tr>
			<td valign="top"><%=Bean.card_taskXML.getfieldTransl("text_message", true)%></td><td  colspan="3"><textarea name="text_message" id="text_message" cols="120" rows="2" class="inputfield" <%=Bean.getMessageLengthTextAreaEvent("length_message")%>><%=operation.getValue("TEXT_MESSAGE")%></textarea></td>
		</tr>
		<tr>
			<td valign="top"><%=Bean.messageXML.getfieldTransl("length_message", false)%></td><td valign="top"><input type="text" name="length_message" id="length_message" size="8" value="" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<%
			}
		%>

		<tr><td colspan="4"><b><%= Bean.card_taskXML.getfieldTransl("tag_source_param", false) %>&nbsp;<span onclick="show_addit_param();" id="addit_button">&gt;&gt;</span></b></td></tr>
		<tr id="addit_row1" style="display:none">
			<td class="gray_background"><%= Bean.card_taskXML.getfieldTransl("id_sequrity_monitor", false) %></td><td class="gray_background"><input type="text" name="id_sequrity_monitor" size="20" value="<%=operation.getValue("ID_SEQURITY_MONITOR")%>" readonly="readonly" class="inputfield-ro"></td>
			<td class="gray_background"><%= Bean.card_taskXML.getfieldTransl("id_previous_operation", false) %>
				<%=Bean.getGoToCardTaskLink(operation.getValue("ID_PREVIOUS_OPERATION"))%>
			</td><td class="gray_background"><input type="text" name="id_previous_operation" size="20" value="<%=operation.getValue("ID_PREVIOUS_OPERATION")%>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr id="addit_row2" style="display:none">
			<td class="gray_background"><%= Bean.card_taskXML.getfieldTransl("id_remittance", false) %></td><td class="gray_background"><input type="text" name="id_remittance" size="20" value="<%=operation.getValue("ID_REMITTANCE")%>" readonly="readonly" class="inputfield-ro"></td>
			<td class="gray_background"><%= Bean.card_taskXML.getfieldTransl("id_club_event", false) %>
				<%=Bean.getGoToClubEventLink(operation.getValue("ID_CLUB_EVENT"))%>
			</td><td class="gray_background"><input type="text" name="id_quest_int" size="20" value="<%=operation.getValue("ID_CLUB_EVENT")%>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr id="addit_row3" style="display:none">
			<td class="gray_background"><%= Bean.card_taskXML.getfieldTransl("id_quest_int", false) %>
				<%=Bean.getGoToQuestionnaireLink(operation.getValue("ID_QUEST_INT"))%>
			</td><td class="gray_background"><input type="text" name="id_quest_int" size="20" value="<%=operation.getValue("ID_QUEST_INT")%>" readonly="readonly" class="inputfield-ro"></td>
			<td colspan="2" class="gray_background">&nbsp;</td>
		</tr>
		<%=	Bean.getIdCreationAndMoficationRecordFields(
				operation.getValue("ID_CARD_OPERATION"),
				operation.getValue(Bean.getCreationDateFieldName()),
				operation.getValue("CREATED_BY"),
				operation.getValue(Bean.getLastUpdateDateFieldName()),
				operation.getValue("LAST_UPDATE_BY")
			) %>
		<tr>
			<td colspan="6" align="center">
				<%=Bean.getSubmitButtonAjax("../crm/cards/card_tasksupdate.jsp")%>
				<%=Bean.getResetButton()%>
				<%=Bean.getGoBackButton("../crm/cards/card_tasks.jsp")%>
			</td>
		</tr>
	</table>
	</form>
	<!-- Скрипт для втавки меню вибору дати -->
	<% if ("NEW".equalsIgnoreCase(operation.getValue("CD_CARD_OPER_STATE"))) { %>
		<%= Bean.getCalendarScript("begin_action_date", false) %>
		
		<% if ("SET_CATEGORIES_ON_PERIOD".equalsIgnoreCase(operation.getValue("CD_CARD_OPERATION_TYPE"))) {	%>
			<%= Bean.getCalendarScript("end_action_date", false) %>
		<% } %>

   	<% } %>
   	<% if (("ADD_BON".equalsIgnoreCase(operation.getValue("CD_CARD_OPERATION_TYPE")) || 
    			"CHANGE_PARAM".equalsIgnoreCase(operation.getValue("CD_CARD_OPERATION_TYPE")))) { %>
		
		<%= Bean.getCalendarScript("date_acc", false) %>
    <% } %>
    <% if (("CHANGE_PARAM".equalsIgnoreCase(operation.getValue("CD_CARD_OPERATION_TYPE")))) { %>
		
		<%= Bean.getCalendarScript("date_mov", false) %>
    <%	} %>
    <% if (("SET_CATEGORIES_ON_PERIOD".equalsIgnoreCase(operation.getValue("CD_CARD_OPERATION_TYPE")) || 
    				"CHANGE_PARAM".equalsIgnoreCase(operation.getValue("CD_CARD_OPERATION_TYPE")))) { %>

		<%= Bean.getCalendarScript("date_onl", false) %>
   	<% } %>
 
	<% 	}
	}
	if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CARDS_CARD_TASKS_ACTIONS")) { %>

	<table <%=Bean.getTableBottomFilter() %>>
	  	<tr>
		<%= Bean.getFindHTML("action_find", action_find, "../crm/cards/card_taskspecs.jsp?id=" + id + "&action_page=1") %>

		<%=Bean.getSelectOnChangeBeginHTML("action_state", "../crm/cards/card_taskspecs.jsp?id=" + id + "&action_page=1", Bean.card_taskXML.getfieldTransl("state_action_tsl", false)) %>
			<%= Bean.getMeaningFromLookupNameOptions("SEND_MESSAGE_ACTION_STATE", action_state, true) %>
		<%=Bean.getSelectOnChangeEndHTML() %>

	  	</tr>
	</table>


		<%=operation.getActionsHTML(action_find, operation.getValue("CD_CARD_OPERATION_TYPE"), action_state, l_action_page_beg, l_action_page_end)%>
	<% } 
	if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CARDS_CARD_TASKS_POSTINGS")) { %>

	<table <%=Bean.getTableBottomFilter() %>>
	  <tr>
		<%= Bean.getFindHTML("posting_find", posting_find, "../crm/cards/card_taskspecs.jsp?id=" + id + "&posting_page=1&") %>

		<%=Bean.getSelectOnChangeBeginHTML("posting_bk_oper", "../crm/cards/card_taskspecs.jsp?id=" + id + "&posting_page=1", "") %>
			<%= Bean.getBKOperationTypeShortOptions(posting_bk_oper, true) %>
		<%=Bean.getSelectOnChangeEndHTML() %>

	  </tr>
	</table>


		<%=operation.getPostingsHTML(posting_find, posting_bk_oper, l_posting_page_beg, l_posting_page_end)%>
	<%
	}

}
%>

</div></div>
</body>
</html>
