<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%@page import="java.util.HashMap"%>

<%@page import="bc.objects.bcReferralSchemeObject"%><html>

<%= Bean.getLogOutScript(request) %>

<head>
	<%= Bean.getMetaContent() %>
	<%= Bean.getBottomFrameCSS() %>
</head>
<body topmargin="0">
<div id="div_tabsheet">
<%

HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String pageFormName = "CRM_CLUB_REFERRAL_SCHEME";

String tagLine = "_LINE";
String tagLineFind = "_LINE_FIND";

String tagPartner = "_PARTNER";
String tagPartnerFind = "_PARTNER_FIND";

String tagTaskType = "_TASK_TYPE";
String tagTaskState = "_TASK_STATE";
String tagTaskFind = "_TASK_FIND";
String tagTasks = "_TASKS";

Bean.setJspPageForTabName(pageFormName);

String id = Bean.getDecodeParam(parameters.get("id"));
String	tab = Bean.getDecodeParam(parameters.get("tab"));
if (tab==null || "".equalsIgnoreCase(tab)) { tab = Bean.tabsHmGetValue(pageFormName); }
if (id==null || "".equalsIgnoreCase(id)) {
%>
	<%=Bean.getIDNotFoundMessage() %>
<% 
} else {
	Bean.tabsHmSetValue(pageFormName, tab);
	bcReferralSchemeObject scheme = new bcReferralSchemeObject(id);
	
	String l_line_page = Bean.getDecodeParam(parameters.get("line_page"));
	Bean.pageCheck(pageFormName + tagLine, l_line_page);
	String l_line_page_beg = Bean.getFirstRowNumber(pageFormName + tagLine);
	String l_line_page_end = Bean.getLastRowNumber(pageFormName + tagLine);
	
	String line_find 	= Bean.getDecodeParam(parameters.get("line_find"));
	line_find 	= Bean.checkFindString(pageFormName + tagLineFind, line_find, l_line_page);

	String l_partner_page = Bean.getDecodeParam(parameters.get("partner_page"));
	Bean.pageCheck(pageFormName + tagPartner, l_partner_page);
	String l_partner_page_beg = Bean.getFirstRowNumber(pageFormName + tagPartner);
	String l_partner_page_end = Bean.getLastRowNumber(pageFormName + tagPartner);
	
	String partner_find 	= Bean.getDecodeParam(parameters.get("partner_find"));
	partner_find 	= Bean.checkFindString(pageFormName + tagPartnerFind, partner_find, l_partner_page);
	
	String l_tasks_page = Bean.getDecodeParam(parameters.get("tasks_page"));
	Bean.pageCheck(pageFormName + tagTasks, l_tasks_page);
	String l_tasks_page_beg = Bean.getFirstRowNumber(pageFormName + tagTasks);
	String l_tasks_page_end = Bean.getLastRowNumber(pageFormName + tagTasks);

	String task_type 	= Bean.getDecodeParam(parameters.get("task_type"));
	task_type 	= Bean.checkFindString(pageFormName + tagTaskType, task_type, l_tasks_page);
	
	String task_state	= Bean.getDecodeParam(parameters.get("task_state"));
	task_state 		= Bean.checkFindString(pageFormName + tagTaskState, task_state, l_tasks_page);
	
	String task_find 	= Bean.getDecodeParam(parameters.get("task_find"));
	task_find 	= Bean.checkFindString(pageFormName + tagTaskFind, task_find, l_tasks_page);
%>

	<table <%=Bean.getTableMenuParam() %>>
		<tr>
			<%= Bean.getPageHeader() %>

			<% if (Bean.currentMenu.isCurrentTabAndEditPermitted("CRM_CLUB_REFERRAL_SCHEME_INFO")) { %>
			    <%= Bean.getMenuButton("ADD", "../crm/club/referral_schemeupdate.jsp?id_referral_scheme=" + id + "&type=general&action=add2&process=no", "", "") %>
			    <%= Bean.getMenuButton("DELETE", "../crm/club/referral_schemeupdate.jsp?id_referral_scheme=" + id + "&type=general&action=remove&process=yes", Bean.clubXML.getfieldTransl("h_delete_referral_scheme", false), scheme.getValue("NAME_REFERRAL_SCHEME")) %>
			<%  } %>
			
			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CRM_CLUB_REFERRAL_SCHEME_LINES")) { %>
				<% if (Bean.currentMenu.isCurrentTabAndEditPermitted("CRM_CLUB_REFERRAL_SCHEME_LINES")) {	%>
					<%= Bean.getMenuButtonBase("ADD", "../crm/club/referral_schemeupdate.jsp?id_referral_scheme=" + id + "&type=line&action=add&process=no", "", "", "", "div_data_detail") %>
				<% } %>
			    <!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagLine, "../crm/club/referral_schemespecs.jsp?id=" + id + "&tab="+Bean.currentMenu.getTabID("CRM_CLUB_REFERRAL_SCHEME_LINES")+"&", "line_page") %>
			<% } %>
			
			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CRM_CLUB_REFERRAL_SCHEME_ACCOUNTINGS")) { %>
			    <!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagTasks, "../crm/club/referral_schemespecs.jsp?id=" + id + "&tab="+Bean.currentMenu.getTabID("CRM_CLUB_REFERRAL_SCHEME_ACCOUNTINGS")+"&", "task_page") %>
			<% } %>
		</tr>
	</table>
	<table <%=Bean.getTableMenuParam() %>>
		<%= Bean.getDetailCaption(scheme.getValue("NAME_REFERRAL_SCHEME")) %>
		<tr>
			<td>
				<!-- Выводим перечень закладок -->
				<%= Bean.currentMenu.getFormTabScheeds(tab, "../crm/club/referral_schemespecs.jsp?id=" + id) %>
			</td>
		</tr>
	</table>
</div>
<div id="div_data">
<div id="div_data_detail">
<%
if (Bean.currentMenu.isCurrentTabAndPreviewPermitted("CRM_CLUB_REFERRAL_SCHEME_INFO")) {
 %>	 
	<form>
	<table <%=Bean.getTableDetailParam() %>>
	    <tr>
			<td><%= Bean.clubXML.getfieldTransl("name_referral_scheme_type", false) %></td> <td><%=Bean.getInputTextElement("name_referral_scheme_type", "", scheme.getValue("NAME_REFERRAL_SCHEME_TYPE"), true, "350") %></td>
			<td><%= Bean.clubXML.getfieldTransl("club", false) %>
				<%= Bean.getGoToClubLink(scheme.getValue("ID_CLUB")) %>
			</td><td><%=Bean.getInputTextElement("name_club", "", Bean.getClubShortName(scheme.getValue("ID_CLUB")), true, "150") %></td>
   		</tr>
	    <tr>
			<% if ("PAYMENT".equalsIgnoreCase(scheme.getValue("CD_REFERRAL_SCHEME_TYPE"))) { %>
			<td><%= Bean.clubXML.getfieldTransl("jur_prs", false) %>
				<%=Bean.getGoToJurPrsHyperLink(scheme.getValue("ID_JUR_PRS")) %>
			</td> 
			<td>
				<%=Bean.getInputTextElement("name_jur_prs", "", scheme.getValue("SNAME_JUR_PRS"), true, "350") %>
			</td>
			<% } else { %>
			<td><%= Bean.clubXML.getfieldTransl("target_prg", false) %>
				<%=Bean.getGoToTargetProgramLink(scheme.getValue("ID_TARGET_PRG")) %>
			</td> 
			<td>
				<%=Bean.getInputTextElement("name_target_prg", "", scheme.getValue("NAME_TARGET_PRG"), true, "350") %>
			</td>
			<% } %>
			<td><%= Bean.clubXML.getfieldTransl("date_beg", false) %></td> <td><input type="text" name="date_beg" size="10" value="<%=scheme.getValue("DATE_BEG_DF") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
	    <tr>
			<td><%= Bean.clubXML.getfieldTransl("name_referral_scheme", false) %></td> <td><%=Bean.getInputTextElement("name_referral_scheme", "", scheme.getValue("NAME_REFERRAL_SCHEME"), true, "350") %></td>
			<td><%= Bean.clubXML.getfieldTransl("date_end", false) %></td> <td><input type="text" name="date_end" size="10" value="<%=scheme.getValue("DATE_END_DF") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
	    <tr>
			<td rowspan="3"><%= Bean.clubXML.getfieldTransl("desc_referral_scheme", false) %></td> <td rowspan="3"><textarea name="desc_referral_scheme"  style="width:350px !important; height:60px !important;" readonly="readonly" class="inputfield-ro"><%= scheme.getValue("DESC_REFERRAL_SCHEME") %></textarea></td>
			<td><%= Bean.clubXML.getfieldTransl("accounting_level_count", false) %></td> <td><input type="text" name="accounting_level_count" size="10" value="<%=scheme.getValue("ACCOUNTING_LEVEL_COUNT") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
	    <tr>
			<td><%= Bean.clubXML.getfieldTransl("accounting_percent_all", false) %></td> <td><input type="text" name="accounting_percent_all" size="10" value="<%=scheme.getValue("ACCOUNTING_PERCENT_ALL_FRMT") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
	    <tr>
			<td colspan="2">&nbsp;</td>
		</tr>
	    <tr>
			<td><%= Bean.clubXML.getfieldTransl("name_referral_scheme_calc_type", false) %></td> <td><%=Bean.getInputTextElement("name_referral_scheme_calc_type", "", scheme.getValue("name_referral_scheme_calc_type"), true, "350") %></td>
			<td colspan="2">&nbsp;</td>
		</tr>
   		<tr>
			<td><%= Bean.clubcardXML.getfieldTransl("id_doc", false) %>
				<%= Bean.getGoToDocLink(scheme.getValue("ID_DOC")) %>
			</td> 
			<td>
				<%=Bean.getInputTextElement("name_doc", "", Bean.getDocName(scheme.getValue("ID_DOC")), true, "350") %>
			</td>
			<td colspan="2">&nbsp;</td>
    	</tr>
		<%=	Bean.getIdCreationAndMoficationRecordFields(
				scheme.getValue("ID_REFERRAL_SCHEME"),
				scheme.getValue(Bean.getCreationDateFieldName()),
				scheme.getValue("CREATED_BY"),
				scheme.getValue(Bean.getLastUpdateDateFieldName()),
				scheme.getValue("LAST_UPDATE_BY")
			) %>
		<tr>
			<td colspan="6" align="center">
				<%=Bean.getGoBackButton("../crm/club/referral_scheme.jsp") %>
			</td>
		</tr>
	</table>
	</form>

<% } else if (Bean.currentMenu.isCurrentTabAndEditPermitted("CRM_CLUB_REFERRAL_SCHEME_INFO")) { %>
	
		<script>
			var formData = new Array (
				<% if ("PAYMENT".equalsIgnoreCase(scheme.getValue("CD_REFERRAL_SCHEME_TYPE"))) { %>
				new Array ('name_jur_prs', 'varchar2', 1),
				<% } else { %>
				new Array ('name_target_prg', 'varchar2', 1),
				<% } %>
				new Array ('name_referral_scheme', 'varchar2', 1),
				new Array ('date_beg', 'varchar2', 1)
			);
			function myValidateForm() {
				return validateForm(formData);
			}
		</script>
	<div id="div_detail">
    <form action="../crm/club/referral_schemeupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST">
    	<input type="hidden" name="action" value="edit">
    	<input type="hidden" name="process" value="yes">
    	<input type="hidden" name="type" value="general">
    	<input type="hidden" name="id_referral_scheme" value="<%=scheme.getValue("ID_REFERRAL_SCHEME") %>">
    	<input type="hidden" name="LUD" value="<%=scheme.getValue("LUD") %>">
	<table <%=Bean.getTableDetailParam() %>>
	    <tr>
			<td><%= Bean.clubXML.getfieldTransl("name_referral_scheme_type", false) %></td> <td><input type="text" name="name_referral_scheme_type" size="70" value="<%=scheme.getValue("NAME_REFERRAL_SCHEME_TYPE") %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.clubXML.getfieldTransl("club", false) %>
				<%= Bean.getGoToClubLink(scheme.getValue("ID_CLUB")) %>
			</td><td><input type="text" name="name_club" size="30" value="<%= Bean.getClubShortName(scheme.getValue("ID_CLUB")) %>" readonly="readonly" class="inputfield-ro"></td>
   		</tr>
	    <tr>
			<% if ("PAYMENT".equalsIgnoreCase(scheme.getValue("CD_REFERRAL_SCHEME_TYPE"))) { %>
			<td><%= Bean.clubXML.getfieldTransl("jur_prs", true) %>
				<%=Bean.getGoToJurPrsHyperLink(scheme.getValue("ID_JUR_PRS")) %>
			</td> 
			<td>
				<%=Bean.getWindowFindJurPrs("jur_prs", scheme.getValue("ID_JUR_PRS"), scheme.getValue("SNAME_JUR_PRS"), "ALL", "60") %>
			</td>
			<% } else { %>
			<td><%= Bean.clubXML.getfieldTransl("target_prg", true) %>
				<%= Bean.getGoToTargetProgramLink(scheme.getValue("ID_TARGET_PRG")) %>
			</td>
			<td>
				<%=Bean.getWindowFindTargetPrg("target_prg", scheme.getValue("ID_TARGET_PRG"), scheme.getValue("NAME_TARGET_PRG"), "60") %>
			</td>
			<% } %>
            <td><%= Bean.clubXML.getfieldTransl("date_beg", true) %></td><td><%=Bean.getCalendarInputField("date_beg", scheme.getValue("DATE_BEG_DF"), "10") %></td>
		</tr>
   		<tr>
			<td><%= Bean.clubXML.getfieldTransl("name_referral_scheme", true) %></td> <td><input type="text" name="name_referral_scheme" size="70" value="<%=scheme.getValue("NAME_REFERRAL_SCHEME") %>" class="inputfield"></td>
            <td><%= Bean.clubXML.getfieldTransl("date_end", false) %></td><td><%=Bean.getCalendarInputField("date_end", scheme.getValue("DATE_END_DF"), "10") %></td>
   		</tr>
	    <tr>
			<td rowspan="3"><%= Bean.clubXML.getfieldTransl("desc_referral_scheme", false) %></td> <td rowspan="3"><textarea name="desc_referral_scheme" cols="67" rows="3" class="inputfield"><%= scheme.getValue("DESC_REFERRAL_SCHEME") %></textarea></td>
			<td><%= Bean.clubXML.getfieldTransl("accounting_level_count", false) %></td> <td><input type="text" name="accounting_level_count" size="10" value="<%=scheme.getValue("ACCOUNTING_LEVEL_COUNT") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
	    <tr>
			<td><%= Bean.clubXML.getfieldTransl("accounting_percent_all", false) %></td> <td><input type="text" name="accounting_percent_all" size="10" value="<%=scheme.getValue("ACCOUNTING_PERCENT_ALL_FRMT") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
	    <tr>
			<td colspan="2">&nbsp;</td>
		</tr>
	    <tr>
			<td><%= Bean.clubXML.getfieldTransl("name_referral_scheme_calc_type", false) %></td> <td><input type="text" name="name_referral_scheme_calc_type" size="70" value="<%=scheme.getValue("NAME_REFERRAL_SCHEME_CALC_TYPE") %>" readonly="readonly" class="inputfield-ro"></td>
			<td colspan="2">&nbsp;</td>
		</tr>
   		<tr>
			<td><%= Bean.clubcardXML.getfieldTransl("id_doc", false) %>
				<%= Bean.getGoToDocLink(scheme.getValue("ID_DOC")) %>
			</td> 
			<td>
				<%=Bean.getWindowDocuments("doc", scheme.getValue("ID_DOC"), "60") %>
			</td>
 			<td colspan="2">&nbsp;</td>
   	</tr>
		<%=	Bean.getIdCreationAndMoficationRecordFields(
				scheme.getValue("ID_REFERRAL_SCHEME"),
				scheme.getValue(Bean.getCreationDateFieldName()),
				scheme.getValue("CREATED_BY"),
				scheme.getValue(Bean.getLastUpdateDateFieldName()),
				scheme.getValue("LAST_UPDATE_BY")
			) %>
		<tr>
			<td colspan="6" align="center">
				<%=Bean.getSubmitButtonAjax("../crm/club/referral_schemeupdate.jsp", "submit", "updateForm", "div_detail") %>
				<%=Bean.getResetButton() %>
				<%=Bean.getGoBackButton("../crm/club/referral_scheme.jsp") %>
			</td>
		</tr>
	</table>

	</form>
	</div>
		<%= Bean.getCalendarScript("date_beg", false) %>
		<%= Bean.getCalendarScript("date_end", false) %>

<% } %>

<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CRM_CLUB_REFERRAL_SCHEME_LINES")) {%>

		<table <%=Bean.getTableBottomFilter() %>><tbody>
		<tr>
			<%=Bean.getFindHTML("line_find", line_find, "../crm/club/referral_schemespecs.jsp?id=" + id + "&line_page=1") %>
		</tr>
		</tbody>
		</table> 
	<%= scheme.getLinesHTML("", line_find, l_line_page_beg, l_line_page_end) %>
<%} %>

<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CRM_CLUB_REFERRAL_SCHEME_ACCOUNTINGS")) {%>

		<table <%=Bean.getTableBottomFilter() %>><tbody>
		  	<tr>
			<%= Bean.getFindHTML("task_find", task_find, "../crm/club/referral_schemespecs.jsp?id=" + id +  "&tasks_page=1") %>
	
			<%=Bean.getSelectOnChangeBeginHTML("task_type", "../crm/club/referral_schemespecs.jsp?id=" + id + "&tasks_page=1", Bean.card_taskXML.getfieldTransl("CD_CARD_OPERATION_TYPE", false)) %>
				<%= Bean.getClubCardOperationTypeOptions(task_type, true) %>
			<%=Bean.getSelectOnChangeEndHTML() %>
	
			<%=Bean.getSelectOnChangeBeginHTML("task_state", "../crm/club/referral_schemespecs.jsp?id=" + id + "&tasks_page=1", Bean.card_taskXML.getfieldTransl("cd_card_oper_state", false)) %>
				<%= Bean.getClubCardOperationStateOptions(task_state, true) %>
			<%=Bean.getSelectOnChangeEndHTML() %>
		  	</tr>
		</tbody>
		</table> 
	<%= scheme.getClubCardsTasksHTML(task_find, task_type, task_state, l_tasks_page_beg, l_tasks_page_end) %>
	
<%} %>

<% } %>
</div></div>
</body>
</html>
