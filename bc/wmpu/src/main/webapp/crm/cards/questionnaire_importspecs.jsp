<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%@page import="bc.objects.bcQuestionnaireObject"%>
<%@page import="java.net.URLEncoder"%>
<%@page import="bc.objects.bcQuestionnairePackObject"%>
<%@page import="bc.reports.bcReports"%>
<%@page import="java.util.HashMap"%>

<%= Bean.getLogOutScript(request) %>
<html>
<head>
	<%= Bean.getMetaContent() %>
	<%= Bean.getBottomFrameCSS() %>

</head>
<body topmargin="0">
<div id="div_tabsheet">


<%
HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

request.setCharacterEncoding("UTF-8");

String pageFormName = "CARDS_QUESTIONNAIRE_IMPORT";

Bean.setJspPageForTabName(pageFormName);

String tagReports = "_REPORTS";
String tagReportDetail = "_REPORT_DETAIL";
String tagFind = "_FIND_DETAIL";
String tagTasks = "_CARD_TASKS";
String tagTaskFind = "_CARD_TASK_FIND";
String tagTaskType = "_TASK_TYPE";
String tagTaskState = "_TASK_STATE";
String tagMessage = "_MESSAGES";
String tagMessageFind = "_MESSAGES_FIND";
String tagPosting = "_POSTINGS";
String tagPostingFind = "_POSTINGS_FIND";

String id = Bean.getDecodeParam(parameters.get("id"));

String id_report = Bean.getDecodeParam(parameters.get("id_report"));

String l_report_det_page = Bean.getDecodeParam(parameters.get("report_det_page"));

if (id_report==null || "".equalsIgnoreCase(id_report)) {
	l_report_det_page = "1";
}
Bean.pageCheck(pageFormName + tagReportDetail, l_report_det_page);
String l_report_det_page_beg = Bean.getFirstRowNumber(pageFormName + tagReportDetail);
String l_report_det_page_end = Bean.getLastRowNumber(pageFormName + tagReportDetail);

String	tab = Bean.getDecodeParam(parameters.get("tab"));
if (tab==null || "".equalsIgnoreCase(tab)) { tab = Bean.tabsHmGetValue(pageFormName); }

if (id==null || "".equalsIgnoreCase(id)) { id= ""; }

String find_string = Bean.getDecodeParam(parameters.get("find_string"));

if (find_string==null || "".equalsIgnoreCase(find_string)) {
	find_string = "";
	Bean.filtersHmSetValue(pageFormName + tagFind, find_string);
} else {
	if ("CURRENT".equalsIgnoreCase(find_string)) {
		find_string = Bean.filtersHmGetValue(pageFormName + tagFind);
	} else {
		Bean.filtersHmSetValue(pageFormName + tagFind, find_string);
	}
}

if (id==null || "".equalsIgnoreCase(id)) {
%>
	<%=Bean.getIDNotFoundMessage() %>
<% 
} else {
	Bean.tabsHmSetValue(pageFormName, tab);


	String l_report_page = Bean.getDecodeParam(parameters.get("report_page"));
	Bean.pageCheck(pageFormName + tagReports, l_report_page);
	String l_report_page_beg = Bean.getFirstRowNumber(pageFormName + tagReports);
	String l_report_page_end = Bean.getLastRowNumber(pageFormName + tagReports);


	bcQuestionnaireObject quest = new bcQuestionnaireObject(id);
	quest.getFeature();
	
	bcQuestionnairePackObject pack = null;
	String id_jur_prs_who_has_sold_card = "";
	String id_jur_prs_where_card_sold = "";
	String id_serv_place_where_card_sold = "";
	String id_club_event = "";
	
	if (!(quest.getValue("ID_QUEST_PACK")==null || "".equalsIgnoreCase(quest.getValue("ID_QUEST_PACK")))) {
		pack = new bcQuestionnairePackObject(quest.getValue("ID_QUEST_PACK"));
		id_jur_prs_who_has_sold_card = pack.getValue("ID_JUR_PRS_WHO_HAS_SOLD_CARD");
		id_jur_prs_where_card_sold = pack.getValue("ID_JUR_PRS_WHERE_CARD_SOLD");
		id_serv_place_where_card_sold = pack.getValue("ID_SERV_PLACE_WHERE_CARD_SOLD");
		id_club_event = pack.getValue("ID_CLUB_EVENT");
	}
	
	String state_quest = quest.getValue("STATE_QUEST"); 
	
	if ("IMPORTED".equalsIgnoreCase(state_quest)) {
		Bean.currentMenu.setExistFlag("CARDS_QUESTIONNAIRE_IMPORT_TASKS",true);
		Bean.currentMenu.setExistFlag("CARDS_QUESTIONNAIRE_IMPORT_MESSAGES",true);
		Bean.currentMenu.setExistFlag("CARDS_QUESTIONNAIRE_IMPORT_POSTINGS",true);
	} else {
		Bean.currentMenu.setExistFlag("CARDS_QUESTIONNAIRE_IMPORT_TASKS",false);
		Bean.currentMenu.setExistFlag("CARDS_QUESTIONNAIRE_IMPORT_MESSAGES",false);
		Bean.currentMenu.setExistFlag("CARDS_QUESTIONNAIRE_IMPORT_POSTINGS",false);
		if (Bean.currentMenu.isCurrentTab("CARDS_QUESTIONNAIRE_IMPORT_MESSAGES") || 
				Bean.currentMenu.isCurrentTab("CARDS_QUESTIONNAIRE_IMPORT_POSTINGS") || 
				Bean.currentMenu.isCurrentTab("CARDS_QUESTIONNAIRE_IMPORT_TASKS")) {
    		Bean.currentMenu.setFirstCurrentTab();
    		tab = Bean.currentMenu.getCurrentTab();
    		Bean.tabsHmSetValue(pageFormName, tab);
		}
	}
		
	//Обрабатываем номера страниц
	String l_task_page = Bean.getDecodeParam(parameters.get("task_page"));
	Bean.pageCheck(pageFormName + tagTasks, l_task_page);
	String l_task_page_beg = Bean.getFirstRowNumber(pageFormName + tagTasks);
	String l_task_page_end = Bean.getLastRowNumber(pageFormName + tagTasks);
	
	String task_find 	= Bean.getDecodeParam(parameters.get("task_find"));
	task_find 	= Bean.checkFindString(pageFormName + tagTaskFind, task_find, l_task_page);

	String task_type 	= Bean.getDecodeParam(parameters.get("task_type"));
	task_type 	= Bean.checkFindString(pageFormName + tagTaskType, task_type, l_task_page);
	
	String task_state	= Bean.getDecodeParam(parameters.get("task_state"));
	task_state 		= Bean.checkFindString(pageFormName + tagTaskState, task_state, l_task_page);
		
	String l_message_page = Bean.getDecodeParam(parameters.get("message_page"));
	Bean.pageCheck(pageFormName + tagMessage, l_message_page);
	String l_message_page_beg = Bean.getFirstRowNumber(pageFormName + tagMessage);
	String l_message_page_end = Bean.getLastRowNumber(pageFormName + tagMessage);
	
	String message_find 	= Bean.getDecodeParam(parameters.get("message_find"));
	message_find 	= Bean.checkFindString(pageFormName + tagMessageFind, message_find, l_message_page);
		
	String l_posting_page = Bean.getDecodeParam(parameters.get("posting_page"));
	Bean.pageCheck(pageFormName + tagPosting, l_posting_page);
	String l_posting_page_beg = Bean.getFirstRowNumber(pageFormName + tagPosting);
	String l_posting_page_end = Bean.getLastRowNumber(pageFormName + tagPosting);
	
	String posting_find 	= Bean.getDecodeParam(parameters.get("posting_find"));
	posting_find 	= Bean.checkFindString(pageFormName + tagPostingFind, posting_find, l_posting_page);
	
	%>
	<table <%=Bean.getTableMenuParam() %>>
		<tr>
			<%= Bean.getPageHeader() %>

			<% if (Bean.currentMenu.isCurrentTabAndEditPermitted("CARDS_QUESTIONNAIRE_IMPORT_INFO")) { %>
				<% if (((!"IMPORTED".equalsIgnoreCase(state_quest)) && (!("".equalsIgnoreCase(quest.getValue("ID_QUEST_PACK")))))) {%> 
				    <%= Bean.getMenuButton("IMPORT", "../crm/cards/questionnaire_importupdate.jsp?type=general&id=" + id + "&action=import&process=yes", "", "") %>
				<%} %>

			    <%= Bean.getMenuButton("ADD", "../crm/cards/questionnaire_importupdate.jsp?type=general&id=" + id + "&action=add2&process=no", "", "") %>

			    <%= Bean.getMenuButton("DELETE", "../crm/cards/questionnaire_importupdate.jsp?type=general&id=" + id + "&action=remove&process=yes", Bean.buttonXML.getfieldTransl("delete", false), id) %>
			<%  } %>

			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CARDS_QUESTIONNAIRE_IMPORT_TASKS")) { %>
				<%= Bean.getPagesHTML(pageFormName + tagTasks, "../crm/cards/questionnaire_importspecs.jsp?id=" + id + "&tab="+Bean.currentMenu.getTabID("CARDS_QUESTIONNAIRE_IMPORT_TASKS")+"&", "task_page") %>
			<% } %>

			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CARDS_QUESTIONNAIRE_IMPORT_MESSAGES")) { %>
				<%= Bean.getPagesHTML(pageFormName + tagMessage, "../crm/cards/questionnaire_importspecs.jsp?id=" + id + "&tab="+Bean.currentMenu.getTabID("CARDS_QUESTIONNAIRE_IMPORT_MESSAGES")+"&", "message_page") %>
			<% } %>

			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CARDS_QUESTIONNAIRE_IMPORT_POSTINGS")) { %>
				<% if (Bean.currentMenu.isCurrentTabAndEditPermitted("CARDS_QUESTIONNAIRE_IMPORT_POSTINGS")) { %>
				    <%= Bean.getMenuButton("ADD", "../crm/cards/questionnaire_importupdate.jsp?id=" + id + "&type=posting&action=add&process=no", "", "") %>
				    <%= Bean.getMenuButton("DELETE_ALL", "../crm/cards/questionnaire_importupdate.jsp?id=" + id + "&type=posting&action=deleteall&process=yes", Bean.postingXML.getfieldTransl("LAB_DELETE_ALL_POSTINGS", false), "") %>
				    <%= Bean.getMenuButton("POSTING", "../crm/cards/questionnaire_importupdate.jsp?id=" + id + "&type=posting&action=run&process=yes&id_club=" + quest.getValue("ID_CLUB"), Bean.postingXML.getfieldTransl("LAB_RUN_POSTINGS_CARD_SALE", false), id, Bean.postingXML.getfieldTransl("LAB_RUN_POSTINGS_CARD_SALE", false)) %>
				<% } %>
				<%= Bean.getPagesHTML(pageFormName + tagPosting, "../crm/cards/questionnaire_importspecs.jsp?id=" + id + "&tab="+Bean.currentMenu.getTabID("CARDS_QUESTIONNAIRE_IMPORT_POSTINGS")+"&", "posting_page") %>
			<% } %>

			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CARDS_QUESTIONNAIRE_IMPORT_REPORTS")) { %>
		
				<% if (id_report==null || "".equalsIgnoreCase(id_report)) { %>
				    <!-- Вывод страниц -->
					<%= Bean.getPagesHTML(pageFormName + tagReports, "../crm/cards/questionnaire_importspecs.jsp?id=" + id + "&tab="+Bean.currentMenu.getTabID("CARDS_QUESTIONNAIRE_IMPORT_REPORTS")+"&", "report_page") %>
				<% } %>
			<% } %>
		</tr>
	</table>
	<table <%=Bean.getTableMenuParam() %>>
		<%= Bean.getDetailCaption(id) %>
		<tr>
			<td>
				<!-- Выводим перечень закладок -->
				<%= Bean.currentMenu.getFormTabScheeds(tab, "../crm/cards/questionnaire_importspecs.jsp?id=" + id) %>
			</td>	
		</tr>
	</table>

</div>
<div id="div_data">
<div id="div_data_detail">
	<%	if (Bean.currentMenu.isCurrentTabAndEditPermitted("CARDS_QUESTIONNAIRE_IMPORT_INFO")) {
	
		boolean isImported = false;
	
		if ("IMPORTED".equalsIgnoreCase(state_quest)) {
			isImported = true;
		}
		if (!isImported) {
		%>
			<script type="text/javascript">
			function checkGroup(){
				var elementCD = document.getElementById('CD_NAT_PRS_GROUP');
				var elementOther = document.getElementById('NAME_OTHER_VARIANT_GROUP');				
				var elementOtherName = document.getElementById('otherName'); 		
				var elementOtherText = document.getElementById('other'); 
				if (elementCD.value != 'OTHER_VARIANT'){				
					elementOther.value = '';
					elementOther.style.visibility = "hidden";
					elementOtherName.style.visibility = "hidden";
					elementOtherText.style.visibility = "hidden";
				} else {
					elementOther.style.visibility = "visible";
					elementOtherName.style.visibility = "visible";
					elementOtherText.style.visibility = "visible";
				}
			}
			checkGroup();
		
			</script>
			<script>
				var formData = new Array (
					new Array ('date_and_time_card_sale', 'varchar2', 1),
					new Array ('cd_quest_payment_method', 'varchar2', 1),
					new Array ('cd_card', 'varchar2', 1),
					new Array ('surname', 'varchar2', 1),
					new Array ('date_of_birth', 'varchar2', 1),
					new Array ('sex', 'varchar2', 1),
					new Array ('FACT_CODE_COUNTRY', 'varchar2', 1),
					new Array ('FACT_ADR_OBLAST', 'varchar2', 1)
				);
				function myValidateForm() {
					return validateForm(formData); 
				}
				function changePackParam(id, title){
					document.getElementById('id_quest_pack').value = id;
					document.getElementById('name_quest_pack').value = title;
					document.getElementById('name_quest_pack').className = "inputfield_modified";
				}
				dwr_oblast_array('FACT_ADR_OBLAST', document.getElementById('FACT_CODE_COUNTRY').value, document.getElementById('FACT_ADR_OBLAST').value, '<%=Bean.getSessionId() %>');
				dwr_oblast_array('REG_ADR_OBLAST', document.getElementById('REG_CODE_COUNTRY').value, document.getElementById('REG_ADR_OBLAST').value, '<%=Bean.getSessionId() %>');
			</script>
		
				<form action="../crm/cards/questionnaire_importupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return myValidateForm();">
					<input type="hidden" name="action" value="edit">
			    	<input type="hidden" name="process" value="yes">
			    	<input type="hidden" name="id" value="<%= id %>">
			    	<input type="hidden" name="type" value="general">
				<table <%=Bean.getTableDetailParam() %>>
				<tr>
					<td><%= Bean.questionnaireXML.getfieldTransl("id_quest_int", false) %> </td><td><input type="text" name="id_quest_int" size="30" value="<%= quest.getValue("ID_QUEST_INT") %>" readonly="readonly" class="inputfield-ro"></td>
					<td><%= Bean.clubXML.getfieldTransl("club", false) %>
						<%= Bean.getGoToClubLink(quest.getValue("ID_CLUB")) %>
					</td><td><input type="text" name="name_club" size="30" value="<%= Bean.getClubShortName(quest.getValue("ID_CLUB")) %>" readonly="readonly" class="inputfield-ro"></td>
				</tr>
				<tr>
					<td><%= Bean.questionnaireXML.getfieldTransl("state_quest", false) %></td>
					<td>
						<input type="text" name="state_quest_tsl" size="30" value="<%= quest.getValue("STATE_QUEST_TSL") %>" readonly="readonly" class="inputfield-ro">
						<% if ("ERROR".equalsIgnoreCase(quest.getValue("STATE_QUEST"))) { %>
							<img vspace="0" hspace="0" src="../images/oper/rows/info.png" align="top" title="<%=quest.getValue("ERROR_TEXT") %>">
						<% } %>
					</td>
					<% if ("IMPORTED".equalsIgnoreCase(state_quest)) { %>
					<td><%= Bean.questionnaireXML.getfieldTransl("date_import", false) %></td><td><input type="text" name="date_import" size="20" value="<%= quest.getValue("DATE_IMPORT_FRMT") %>" class="inputfield-ro" readonly></td>
					<% } else { %>
					<td colspan="2">&nbsp;</td>
					<% } %>
				</tr>
				<tr>
					<td><%= Bean.questionnaireXML.getfieldTransl("file_name", false) %></td>
					<td colspan="4"><input type="text" name="file_name" size="80" value="<%= quest.getValue("FILE_NAME") %>" class="inputfield">
					<% if (!(quest.getValue("FILE_NAME")==null || "".equalsIgnoreCase(quest.getValue("FILE_NAME")))) { %>
						<a href="../FileSender?FILENAME=<%=URLEncoder.encode(quest.getValue("FILE_NAME"),"UTF-8")%>" title="<%= Bean.buttonXML.getfieldTransl("open", false) %>" target="_blank">
							<img vspace="0" hspace="0" src="../images/oper/small/open.gif" align="top">
						</a>
					<% } %>
					</td>
				</tr>

				<tr>
					<td colspan="4" class="top_line"><b><%= Bean.getClubCardXMLFieldTransl("h_sell_information", false) %></b></td>
				</tr>
				<tr>
					<td><%= Bean.questionnaireXML.getfieldTransl("pack", false) %>
						<%= Bean.getGoToQuestionnaireLink(quest.getValue("ID_QUEST_PACK")) %>
			  		</td>
					<td> 
						<%=Bean.getWindowFindQuestionnairePack("quest_pack", quest.getValue("ID_QUEST_PACK"), "16") %>
					</td>
					<td><%= Bean.questionnaireXML.getfieldTransl("date_and_time_card_sale", true) %></td><td><%=Bean.getCalendarInputField("date_and_time_card_sale", quest.getValue("DATE_CARD_SALE_DHMF"), "20") %></td>
				</tr>
				<tr>
					<%  String goToIssuerHyperLink = Bean.getGoToJurPrsHyperLink(quest.getValue("ID_JUR_PRS_WHO_HAS_SOLD_CARD"));
					%>
				    <td><%= Bean.questionnaireXML.getfieldTransl("sname_jur_pr_who_has_sold_card", false) %><%= goToIssuerHyperLink %></td>
					<td>
						<input type="text" name="id_jur_prs_who_has_sold_card" size="30" value="<%= Bean.getJurPersonShortName(id_jur_prs_who_has_sold_card) %>" readonly="readonly" class="inputfield-ro">
					</td>			
					<td><%= Bean.questionnaireXML.getfieldTransl("cd_quest_payment_method", true)  %></td> <td><select name="cd_quest_payment_method" id="cd_quest_payment_method" class="inputfield"><%=Bean.getQuestionnairePaymentMethodOptions(quest.getValue("CD_QUEST_PAYMENT_METHOD"), true) %></select></td>
				</tr>
				<tr>
					<%  String goToIssuerHyperLink2 = Bean.getGoToJurPrsHyperLink(quest.getValue("ID_JUR_PRS_WHERE_CARD_SOLD"));
					%>
				    <td><%= Bean.questionnaireXML.getfieldTransl("sname_jur_prs_where_card_sold", false) %><%= goToIssuerHyperLink2 %></td>
					<td>
						<input type="text" name="id_jur_prs_where_card_sold" size="30" value="<%= Bean.getJurPersonShortName(id_jur_prs_where_card_sold) %>" readonly="readonly" class="inputfield-ro">
					</td>			
					<td><%= Bean.questionnaireXML.getfieldTransl("cheque_number", false) %></td><td><input type="text" name="cheque_number" size="30" value="<%= quest.getValue("CHEQUE_NUMBER") %>" class="inputfield"></td>
				</tr>
				<tr>
					<td><%= Bean.questionnaireXML.getfieldTransl("name_serv_plce_where_card_sold", false) %>
						<%= Bean.getGoToJurPrsHyperLink(quest.getValue("ID_SERV_PLACE_WHERE_CARD_SOLD")) %>
					</td>
					<td>
						<input type="text" name="id_serv_place_where_card_sold" size="30" value="<%= Bean.getServicePlaceName(id_serv_place_where_card_sold) %>" readonly="readonly" class="inputfield-ro">
					</td>			
					<td><%= Bean.questionnaireXML.getfieldTransl("promoter_code", false) %>
						<%= Bean.getGoToLogisticPromoterLink(quest.getValue("ID_LG_PROMOTER")) %>
			  		</td>
					<td>
						<input type="text" name="promoter_code" size="10" value="<%= quest.getValue("PROMOTER_CODE") %>" class="inputfield">
						<% if (!(quest.getValue("ID_LG_PROMOTER")==null || "".equalsIgnoreCase(quest.getValue("ID_LG_PROMOTER")))) { %>
						<input type="text" name="name_lg_promoter" size="13" value="<%= Bean.getLGPromoterName(quest.getValue("ID_LG_PROMOTER")) %>" readonly="readonly" class="inputfield-ro">
						<% } %>
					</td>
				</tr>
				<tr>
		 		    <td><%= Bean.club_actionXML.getfieldTransl("name_club_event", false) %>
						<%= Bean.getGoToClubEventLink(id_club_event) %>
					</td>
				  	<td><input type="text" id="name_club_event" name="name_club_event" size="30" value="<%= Bean.getClubActionName(id_club_event) %>" readonly="readonly" class="inputfield-ro"></td>
					<td><%= Bean.questionnaireXML.getfieldTransl("validity_percent", false) %></td><td><input type="text" name="validity_percent" size="30" value="<%= quest.getValue("VALIDITY_PERCENT_FRMT") %>" class="inputfield"></td>
				</tr>
				<tr>
				</tr>

				<tr>
					<td colspan="4" class="top_line"><b><%= Bean.questionnaireXML.getfieldTransl("h_bon_card", false) %></b></td>
				</tr>
				<tr>
					<td><%= Bean.questionnaireXML.getfieldTransl("cd_card", true) %>
						<%= Bean.getGoToClubCardLink(
								quest.getValue("CARD_SERIAL_NUMBER"),
								quest.getValue("CARD_ID_ISSUER"),
								quest.getValue("CARD_ID_PAYMENT_SYSTEM")
							) %>
					</td><td><input type="text" name="cd_card" size="30" value="<%= quest.getValue("CD_CARD") %>" class="inputfield"></td>
					<td><%= Bean.questionnaireXML.getfieldTransl("number_bon_category_in_quest", false) %></td><td><input type="text" name="number_bon_category_in_quest" size="10" value="<%= quest.getValue("NUMBER_BON_CATEGORY_IN_QUEST") %>" class="inputfield"></td>
				</tr>
				<tr>
					<td colspan="2">&nbsp;</td>
					<td><%= Bean.questionnaireXML.getfieldTransl("number_disc_category_in_quest", false) %></td><td><input type="text" name="number_disc_category_in_quest" size="10" value="<%= quest.getValue("NUMBER_DISC_CATEGORY_IN_QUEST") %>" class="inputfield"></td>
				</tr>
				<tr>
					<td colspan="4" class="top_line"><b><%= Bean.questionnaireXML.getfieldTransl("h_discount_card", false) %></b></td>
				</tr>
				<tr>
					<td><%= Bean.questionnaireXML.getfieldTransl("discount_card_number", false) %></td><td><input type="text" name="discount_card_number" size="30" value="<%= quest.getValue("DISCOUNT_CARD_NUMBER") %>" class="inputfield"></td>
					<td><%= Bean.questionnaireXML.getfieldTransl("is_discount_card_changed", false) %></td><td><select name="is_discount_card_changed" id="is_discount_card_changed" class="inputfield"><%= Bean.getYesNoLookupOptions(quest.getValue("IS_DISCOUNT_CARD_CHANGED"),true) %></select></td>
				</tr>
				<tr>
					<td><%= Bean.questionnaireXML.getfieldTransl("discount_card_percent", false) %></td><td><input type="text" name="discount_card_percent" size="30" value="<%= quest.getValue("DISCOUNT_CARD_PERCENT") %>" class="inputfield"></td>
					<td><%= Bean.questionnaireXML.getfieldTransl("is_furchet_discount_card", false) %></td><td><select name="is_furchet_discount_card" id="is_furchet_discount_card" class="inputfield"><%= Bean.getYesNoLookupOptions(quest.getValue("IS_FURCHET_DISCOUNT_CARD"),true) %></select></td>
				</tr>
				<tr>
					<td colspan="4" class="top_line"><b><%= Bean.questionnaireXML.getfieldTransl("h_client_info", false) %></b></td>
				</tr>
				<tr>
					<td><%= Bean.questionnaireXML.getfieldTransl("tax_code", false) %></td><td><input type="text" name="tax_code" size="30" value="<%= quest.getValue("TAX_CODE") %>" class="inputfield"></td>
					<td><%= Bean.questionnaireXML.getfieldTransl("pasport_code_country", false) %></td><td><input type="text" name="pasport_code_country" size="30" value="<%= quest.getValue("PASPORT_CODE_COUNTRY") %>" class="inputfield"></td>
				</tr>
				<tr>
					<td><%= Bean.questionnaireXML.getfieldTransl("surname", true) %>
						<%= Bean.getGoToNatPrsLink(quest.getValue("ID_NAT_PRS")) %>
			  		</td><td><input type="text" name="surname" size="30" value="<%= quest.getValue("SURNAME") %>" class="inputfield"></td>
					<td><%= Bean.questionnaireXML.getfieldTransl("pasport_number", false) %></td><td><input type="text" name="pasport_number" size="30" value="<%= quest.getValue("PASPORT_NUMBER") %>" class="inputfield"></td>
				</tr>
				<tr>
					<td><%= Bean.questionnaireXML.getfieldTransl("name", false) %> </td><td><input type="text" name="name" size="30" value="<%= quest.getValue("NAME") %>" class="inputfield"></td>
					<td><%= Bean.questionnaireXML.getfieldTransl("pasport_date", false) %> </td><td><%=Bean.getCalendarInputField("pasport_date", quest.getValue("PASPORT_DATE_FRMT"), "10") %></td>
				</tr>
				<tr>
					<td><%= Bean.questionnaireXML.getfieldTransl("patronymic", false) %></td><td><input type="text" name="patronymic" size="30" value="<%= quest.getValue("PATRONYMIC") %>" class="inputfield"></td>
					<td><%= Bean.questionnaireXML.getfieldTransl("pasport_text", false) %></td><td><input type="text" name="pasport_text" size="30" value="<%= quest.getValue("PASPORT_TEXT") %>" class="inputfield"></td>
				</tr>
				<tr>
					<td><%= Bean.questionnaireXML.getfieldTransl("date_of_birth", true) %></td><td><%=Bean.getCalendarInputField("date_of_birth", quest.getValue("DATE_OF_BIRTH_FRMT"), "10") %></td>
					<td class="top_line"><%= Bean.questionnaireXML.getfieldTransl("surname_eng", false) %> </td><td class="top_line"><input type="text" name="surname_eng" size="30" value="<%= quest.getValue("SURNAME_ENG") %>" class="inputfield"></td>
				</tr>
				<tr>
					<td><%= Bean.questionnaireXML.getfieldTransl("sex", true) %></td><td><select name="sex" class="inputfield"><%= Bean.getMeaningFromLookupNameOptions("MALE_FEMALE", quest.getValue("SEX"), false) %></select> </td>
					<td><%= Bean.questionnaireXML.getfieldTransl("name_eng", false) %></td><td><input type="text" name="name_eng" size="30" value="<%= quest.getValue("NAME_ENG") %>" class="inputfield"></td>
				</tr>
				<tr>
					<td valign=top><%= Bean.questionnaireXML.getfieldTransl("CD_NAT_PRS_GROUP", false) %></td><td><select name="CD_NAT_PRS_GROUP" id="CD_NAT_PRS_GROUP" class="inputfield" onchange="checkGroup()"><%= Bean.getNatPrsGroupsListOptions(quest.getValue("NAME_NAT_PRS_GROUP"),true) %></select></td>			
					<td id='otherName' class="top_line"><%= Bean.questionnaireXML.getfieldTransl("NAME_OTHER_VARIANT_GROUP", true) %></td><td id="other" class="top_line"><input type="text" name="NAME_OTHER_VARIANT_GROUP" id="NAME_OTHER_VARIANT_GROUP" size="30" value="<%=quest.getValue("NAME_OTHER_VARIANT_GROUP") %>"  class="inputfield"></td>
				</tr>

				<tr>
					<td colspan="4" class="top_line"><b><%= Bean.natprsXML.getfieldTransl("h_contact_information", false) %></b></td>
				</tr>
				<tr>
					<td><%= Bean.questionnaireXML.getfieldTransl("phone_work", false) %> </td><td><input type="text" name="phone_work" size="30" value="<%= quest.getValue("PHONE_WORK") %>" class="inputfield"></td>
					<td><%= Bean.questionnaireXML.getfieldTransl("phone_mobile", false) %></td><td><input type="text" name="phone_mobile" size="30" value="<%= quest.getValue("PHONE_MOBILE") %>" class="inputfield"></td>
				</tr>
				<tr>
					<td><%= Bean.questionnaireXML.getfieldTransl("phone_home", false) %></td><td><input type="text" name="phone_home" size="30" value="<%= quest.getValue("PHONE_HOME") %>" class="inputfield"></td>
					<td><%= Bean.questionnaireXML.getfieldTransl("email", false) %> </td><td><input type="text" name="email" size="30" value="<%= quest.getValue("EMAIL") %>" class="inputfield"></td>
				</tr>
				<tr>
					<td colspan="4" class="top_line"><b><%= Bean.natprsXML.getfieldTransl("FACT_ADR_FULL", false) %></b></td>
				</tr>
				<tr>
					<td><%= Bean.questionnaireXML.getfieldTransl("code_country", true) %></td><td><select name="FACT_CODE_COUNTRY" id="FACT_CODE_COUNTRY" class="inputfield" onchange="dwr_oblast_array('FACT_ADR_OBLAST', this.value, document.getElementById('FACT_ADR_OBLAST').value, '<%=Bean.getSessionId() %>');"><option value=""></option><%= Bean.getCountryOptions(quest.getValue("FACT_CODE_COUNTRY"), false) %></select></td>
					<td><%= Bean.questionnaireXML.getfieldTransl("fact_adr_city", false) %></td><td><input type="text" name="FACT_ADR_CITY" size="30" value="<%= quest.getValue("FACT_ADR_CITY") %>" class="inputfield"></td>
				</tr>
				<tr>
					<td><%= Bean.questionnaireXML.getfieldTransl("fact_adr_zip_code", false) %></td><td><input type="text" name="FACT_ADR_ZIP_CODE" size="30" value="<%= quest.getValue("FACT_ADR_ZIP_CODE") %>" class="inputfield"></td>
					<td><%= Bean.questionnaireXML.getfieldTransl("fact_adr_street", false) %> </td><td><input type="text" name="FACT_ADR_STREET" size="30" value="<%= quest.getValue("FACT_ADR_STREET") %>" class="inputfield"></td>
				</tr>
				<tr>
					<td><%= Bean.questionnaireXML.getfieldTransl("fact_adr_oblast", true) %>(<%=quest.getValue("FACT_ADR_NAME_OBLAST") %>)</td><td><select name="FACT_ADR_OBLAST" id="FACT_ADR_OBLAST" class="inputfield"><option value=""></option><%= Bean.getOblastOptions(quest.getValue("FACT_CODE_COUNTRY"), quest.getValue("FACT_ADR_ID_OBLAST"), false) %></select></td>
					<td><%= Bean.questionnaireXML.getfieldTransl("fact_adr_house", false) %>/<%= Bean.questionnaireXML.getfieldTransl("fact_adr_case", false) %></td>
					<td>
						<input type="text" name="FACT_ADR_HOUSE" size="10" value="<%= quest.getValue("FACT_ADR_HOUSE") %>" class="inputfield">
						/&nbsp;<input type="text" name="FACT_ADR_CASE" size="10" value="<%= quest.getValue("FACT_ADR_CASE") %>" class="inputfield">
				    </td>
				</tr>
				<tr>
					<td><%= Bean.questionnaireXML.getfieldTransl("fact_adr_district", false) %></td><td><input type="text" name="FACT_ADR_DISTRICT" size="30" value="<%= quest.getValue("FACT_ADR_DISTRICT") %>" class="inputfield"></td>
					<td><%= Bean.questionnaireXML.getfieldTransl("fact_adr_apartment", false) %></td>
					<td>
						<input type="text" name="FACT_ADR_APARTMENT" size="10" value="<%= quest.getValue("FACT_ADR_APARTMENT") %>" class="inputfield">
				    </td>
				</tr>
				<tr>
					<td colspan="4" class="top_line"><b><%= Bean.natprsXML.getfieldTransl("REG_ADR_FULL", false) %></b></td>
				</tr>
				<tr>
					<td><%= Bean.questionnaireXML.getfieldTransl("code_country", false) %></td><td><select name="REG_CODE_COUNTRY" id="REG_CODE_COUNTRY" class="inputfield" onchange="dwr_oblast_array('REG_ADR_OBLAST', this.value, document.getElementById('REG_ADR_OBLAST').value, '<%=Bean.getSessionId() %>');"><option value=""></option><%= Bean.getCountryOptions(quest.getValue("REG_CODE_COUNTRY"), false) %></select></td>
					<td><%= Bean.questionnaireXML.getfieldTransl("reg_adr_city", false) %> </td><td><input type="text" name="REG_ADR_CITY" size="30" value="<%= quest.getValue("REG_ADR_CITY") %>" class="inputfield"></td>
				</tr>
				<tr>
					<td><%= Bean.questionnaireXML.getfieldTransl("reg_adr_zip_code", false) %> </td><td><input type="text" name="REG_ADR_ZIP_CODE" size="30" value="<%= quest.getValue("REG_ADR_ZIP_CODE") %>" class="inputfield"></td>
					<td><%= Bean.questionnaireXML.getfieldTransl("reg_adr_street", false) %></td><td><input type="text" name="REG_ADR_STREET" size="30" value="<%= quest.getValue("REG_ADR_STREET") %>" class="inputfield"></td>
				</tr>
				<tr>
					<td><%= Bean.questionnaireXML.getfieldTransl("reg_adr_oblast", false) %>(<%=quest.getValue("REG_ADR_NAME_OBLAST") %>)</td><td><select name="reg_adr_oblast" id="REG_ADR_OBLAST" class="inputfield"><option value=""></option><%= Bean.getOblastOptions(quest.getValue("REG_CODE_COUNTRY"), quest.getValue("REG_ADR_ID_OBLAST"), false) %></select></td>
					<td><%= Bean.questionnaireXML.getfieldTransl("reg_adr_house", false) %>/<%= Bean.questionnaireXML.getfieldTransl("reg_adr_case", false) %></td>
					<td>
						<input type="text" name="REG_ADR_HOUSE" size="10" value="<%= quest.getValue("REG_ADR_HOUSE") %>" class="inputfield">
						/&nbsp;<input type="text" name="REG_ADR_CASE" size="10" value="<%= quest.getValue("REG_ADR_CASE") %>" class="inputfield">
					</td>
				</tr>
				<tr>
					<td><%= Bean.questionnaireXML.getfieldTransl("reg_adr_district", false) %></td><td><input type="text" name="REG_ADR_DISTRICT" size="30" value="<%= quest.getValue("REG_ADR_DISTRICT") %>" class="inputfield"></td>
					<td><%= Bean.questionnaireXML.getfieldTransl("reg_adr_apartment", false) %></td>
					<td>
						<input type="text" name="REG_ADR_APARTMENT" size="10" value="<%= quest.getValue("REG_ADR_APARTMENT") %>" class="inputfield">
					</td>
				</tr>

				<tr>
					<td colspan="4" class="top_line"><b><%= Bean.natprsXML.getfieldTransl("additional_info", false) %></b></td>
				</tr>
				<tr>
					<td valign=top><%= Bean.questionnaireXML.getfieldTransl("cd_purchase_frequence", false) %></td><td><select name="cd_purchase_frequence" id="cd_purchase_frequence" class="inputfield" onchange="checkGroup()"><%= Bean.getMeaningFromLookupNameOptions("NAT_PRS_PURCHASE_FREQUENCE",quest.getValue("CD_PURCHASE_FREQUENCE"),true) %></select></td>			
					<td valign=top><%= Bean.questionnaireXML.getfieldTransl("has_auto", false) %></td><td><select name="has_auto" id="has_auto" class="inputfield"><%= Bean.getMeaningFromLookupNumberOptions("YES_NO",quest.getValue("HAS_AUTO"),true) %></select></td>			
				</tr>
				<tr>
					<td valign=top colspan="4" class="top_line"><b><%= Bean.questionnaireXML.getfieldTransl("shop_advantage", false) %></b></td>
				</tr>
				<tr>
					<td valign=top>&nbsp;</td>
					<td>
						<input type="checkbox" name="shop_advantage1" id="shop_advantage1" size="30" value="Y" class="inputfield" <% if (!(quest.getValue("SHOP_ADVANTAGE1")== null) && !"".equalsIgnoreCase(quest.getValue("SHOP_ADVANTAGE1"))) { %>checked<%} %>><label class="checbox_label" for="shop_advantage1"><%= Bean.questionnaireXML.getfieldTransl("shop_advantage1", false) %></label>
					</td>
					<td>
						<input type="checkbox" name="shop_advantage2" id="shop_advantage2" size="30" value="Y" class="inputfield" <% if (!(quest.getValue("SHOP_ADVANTAGE2")== null) && !"".equalsIgnoreCase(quest.getValue("SHOP_ADVANTAGE2"))) { %>checked<%} %>><label class="checbox_label" for="shop_advantage2"><%= Bean.questionnaireXML.getfieldTransl("shop_advantage2", false) %></label>
					</td>
					<td>
						<input type="checkbox" name="shop_advantage5" id="shop_advantage5" size="20" value="Y" class="inputfield" <% if (!(quest.getValue("SHOP_ADVANTAGE5")== null) && !"".equalsIgnoreCase(quest.getValue("SHOP_ADVANTAGE5"))) { %>checked<%} %>><label class="checbox_label" for="shop_advantage5"><%= Bean.questionnaireXML.getfieldTransl("shop_advantage5", false) %></label>
					</td>
				</tr>
				<tr>
					<td valign=top>&nbsp;</td>
					<td>
						<input type="checkbox" name="shop_advantage3" id="shop_advantage3" size="20" value="Y" class="inputfield" <% if (!(quest.getValue("SHOP_ADVANTAGE3")== null) && !"".equalsIgnoreCase(quest.getValue("SHOP_ADVANTAGE3"))) { %>checked<%} %>><label class="checbox_label" for="shop_advantage3"><%= Bean.questionnaireXML.getfieldTransl("shop_advantage3", false) %></label>
					</td>
					<td>
						<input type="checkbox" name="shop_advantage4" id="shop_advantage4" size="20" value="Y" class="inputfield" <% if (!(quest.getValue("SHOP_ADVANTAGE4")== null) && !"".equalsIgnoreCase(quest.getValue("SHOP_ADVANTAGE4"))) { %>checked<%} %>><label class="checbox_label" for="shop_advantage4"><%= Bean.questionnaireXML.getfieldTransl("shop_advantage4", false) %></label>
					</td>
				</tr>
				<tr>
					<td valign=top colspan="4" class="top_line"><b><%= Bean.questionnaireXML.getfieldTransl("reception_information_way", false) %></b></td>
				</tr>
				<tr>
					<td valign=top>&nbsp;</td>
					<td>
						<input type="checkbox" name="reception_information_way1" id="reception_information_way1" size="30" value="phone" class="inputfield" <% if (!(quest.getValue("RECEPTION_INFORMATION_WAY1")== null) && !"".equalsIgnoreCase(quest.getValue("RECEPTION_INFORMATION_WAY1"))) { %>checked<%} %>><label class="checbox_label" for="reception_information_way1"><%= Bean.questionnaireXML.getfieldTransl("reception_information_way1", false) %></label>
					</td>
					<td>
						<input type="checkbox" name="reception_information_way2" id="reception_information_way2" size="30" value="e-mail" class="inputfield" <% if (!(quest.getValue("RECEPTION_INFORMATION_WAY2")== null) && !"".equalsIgnoreCase(quest.getValue("reception_information_way2"))) { %>checked<%} %>><label class="checbox_label" for="reception_information_way2"><%= Bean.questionnaireXML.getfieldTransl("reception_information_way2", false) %></label>
					</td>
				</tr>
				<tr>
					<td valign=top>&nbsp;</td>
					<td>
						<input type="checkbox" name="reception_information_way3" id="reception_information_way3" size="20" value="Y" class="inputfield" <% if (!(quest.getValue("RECEPTION_INFORMATION_WAY3")== null) && !"".equalsIgnoreCase(quest.getValue("RECEPTION_INFORMATION_WAY3"))) { %>checked<%} %>><label class="checbox_label" for="reception_information_way3"><%= Bean.questionnaireXML.getfieldTransl("reception_information_way3", false) %></label>
					</td>
					<td>
						<input type="checkbox" name="reception_information_way4" id="reception_information_way4" size="20" value="Y" class="inputfield" <% if (!(quest.getValue("RECEPTION_INFORMATION_WAY4")== null) && !"".equalsIgnoreCase(quest.getValue("RECEPTION_INFORMATION_WAY4"))) { %>checked<%} %>><label class="checbox_label" for="reception_information_way4"><%= Bean.questionnaireXML.getfieldTransl("reception_information_way4", false) %></label>
					</td>
				</tr>
				<tr>
					<td valign=top colspan="4" class="top_line"><b><%= Bean.questionnaireXML.getfieldTransl("h_rules", false) %></b></td>
				</tr>
				<tr>
					<td valign=top>&nbsp;</td>
					<td>
						<input type="checkbox" name="rule_bon" id="rule_bon" size="30" value="bon" class="inputfield" <% if (!(quest.getValue("RULE_BON")== null) && !"".equalsIgnoreCase(quest.getValue("RULE_BON"))) { %>checked<%} %>><label class="checbox_label" for="rule_bon"><%= Bean.questionnaireXML.getfieldTransl("rule_bon", false) %></label>
					</td>
					<td>
						<input type="checkbox" name="rule_discount_ground" id="rule_discount_ground" size="30" value="discount" class="inputfield" <% if (!(quest.getValue("RULE_DISCOUNT_GROUND")== null) && !"".equalsIgnoreCase(quest.getValue("RULE_DISCOUNT_GROUND"))) { %>checked<%} %>><label class="checbox_label" for="rule_discount_ground"><%= Bean.questionnaireXML.getfieldTransl("rule_discount_ground", false) %></label>
					</td>
				</tr>
				<tr>
					<td valign=top>&nbsp;</td>
					<td>
						<input type="checkbox" name="rule_superbon" id="rule_superbon" size="20" value="Y" class="inputfield" <% if (!(quest.getValue("RULE_SUPERBON")== null) && !"".equalsIgnoreCase(quest.getValue("RULE_SUPERBON"))) { %>checked<%} %>><label class="checbox_label" for="rule_superbon"><%= Bean.questionnaireXML.getfieldTransl("rule_superbon", false) %></label>
					</td>
					<td>
						&nbsp;
					</td>
				</tr>

				<%=	Bean.getCreationAndMoficationRecordFields(
						quest.getValue(Bean.getCreationDateFieldName()),
						quest.getValue("CREATED_BY"),
						quest.getValue(Bean.getLastUpdateDateFieldName()),
						quest.getValue("LAST_UPDATE_BY")
				) %>
				<tr>
					<td colspan="4" align="center">
						<%=Bean.getSubmitButtonAjax("../crm/cards/questionnaire_importupdate.jsp") %>
						<%=Bean.getResetButton() %>
						<%=Bean.getGoBackButton("../crm/cards/questionnaire_import.jsp") %>
					</td>
				</tr>
			</table>
			</form>
			<!-- Скрипт для втавки меню вибору дати -->
			<%= Bean.getCalendarScript("date_and_time_card_sale", true) %>
			<%= Bean.getCalendarScript("date_of_birth", false) %>
			<%= Bean.getCalendarScript("pasport_date", false) %>
		
		
		<%
	} else {
		   %>
			<form action="../crm/cards/questionnaire_importupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return myValidateForm();">
				<input type="hidden" name="action" value="edit_imported">
		    	<input type="hidden" name="process" value="yes">
		    	<input type="hidden" name="id" value="<%= id %>">
		    	<input type="hidden" name="type" value="general">
			<table <%=Bean.getTableDetailParam() %>>
			<tr>
				<td><%= Bean.questionnaireXML.getfieldTransl("id_quest_int", false) %> </td><td><input type="text" name="id_quest_int" size="10" value="<%= quest.getValue("ID_QUEST_INT") %>" readonly="readonly" class="inputfield-ro"></td>
				<td><%= Bean.clubXML.getfieldTransl("club", false) %>
					<%= Bean.getGoToClubLink(quest.getValue("ID_CLUB")) %>
				</td><td><input type="text" name="name_club" size="30" value="<%= Bean.getClubShortName(quest.getValue("ID_CLUB")) %>" readonly="readonly" class="inputfield-ro"></td>
			</tr>
			<tr>
				<td><%= Bean.questionnaireXML.getfieldTransl("state_quest", false) %></td>
				<td><input type="text" name="state_quest" size="30" value="<%= quest.getValue("STATE_QUEST_TSL") %>" readonly="readonly" class="inputfield-ro"></td>
				<% if ("IMPORTED".equalsIgnoreCase(state_quest)) { %>
				<td><%= Bean.questionnaireXML.getfieldTransl("date_import", false) %></td><td><input type="text" name="date_import" size="20" value="<%= quest.getValue("DATE_IMPORT_FRMT") %>" class="inputfield-ro" readonly></td>
				<% } else { %>
				<td colspan="2">&nbsp;</td>
				<% } %>
			</tr>
			<tr>
				<td><%= Bean.questionnaireXML.getfieldTransl("file_name", false) %></td>
				<td colspan="4"><input type="text" name="file_name" size="80" value="<%= quest.getValue("FILE_NAME_SHORT") %>" readonly="readonly" class="inputfield-ro">
				<% if (!(quest.getValue("FILE_NAME")==null || "".equalsIgnoreCase(quest.getValue("FILE_NAME")))) { %>
					<a href="../FileSender?FILENAME=<%=URLEncoder.encode(quest.getValue("FILE_NAME"),"UTF-8")%>" title="<%= Bean.buttonXML.getfieldTransl("open", false) %>" target="_blank">
						<img vspace="0" hspace="0" src="../images/oper/small/open.gif" align="top">
					</a>
				<% } %>
				</td>
			</tr>

			<tr>
				<td colspan="4" class="top_line"><b><%= Bean.getClubCardXMLFieldTransl("h_sell_information", false) %></b></td>
			</tr>
			<tr>
				<td><%= Bean.questionnaireXML.getfieldTransl("pack", false) %>
					<%= Bean.getGoToQuestionnairePackLink(quest.getValue("ID_QUEST_PACK")) %>
		  		</td>
				<td><input type="text" id="name_quest_pack" name="name_quest_pack" size="30" value="<%=quest.getValue("ID_QUEST_PACK") %>" readonly="readonly" class="inputfield-ro"></td>
				<td><%= Bean.questionnaireXML.getfieldTransl("date_card_sale", false) %> </td><td><input type="text" name="date_card_sale" size="20" value="<%= quest.getValue("DATE_CARD_SALE_DHMF") %>" readonly="readonly" class="inputfield-ro"></td>
			</tr>
			<tr>
				<td><%= Bean.questionnaireXML.getfieldTransl("sname_jur_pr_who_has_sold_card", false) %>
					<%= Bean.getGoToJurPrsHyperLink(quest.getValue("ID_JUR_PRS_WHO_HAS_SOLD_CARD")) %>
		  		</td><td><input type="text" name="sname_jur_pr_who_has_sold_card" size="30" value="<%= Bean.getJurPersonShortName(quest.getValue("ID_JUR_PRS_WHO_HAS_SOLD_CARD")) %>" readonly="readonly" class="inputfield-ro"></td>
				<td><%= Bean.questionnaireXML.getfieldTransl("cd_quest_payment_method", false) %> </td><td><input type="text" name="name_quest_payment_method" size="30" value="<%= quest.getValue("NAME_QUEST_PAYMENT_METHOD") %>" readonly="readonly" class="inputfield-ro"></td>
			</tr>
			<tr>
				<td><%= Bean.questionnaireXML.getfieldTransl("sname_jur_prs_where_card_sold", false) %>
					<%= Bean.getGoToJurPrsHyperLink(quest.getValue("ID_JUR_PRS_WHERE_CARD_SOLD")) %>
		  		</td><td><input type="text" name="sname_jur_prs_where_card_sold" size="30" value="<%= Bean.getJurPersonShortName(quest.getValue("ID_JUR_PRS_WHERE_CARD_SOLD")) %>" readonly="readonly" class="inputfield-ro"></td>
				<td><%= Bean.questionnaireXML.getfieldTransl("cheque_number", false) %></td><td><input type="text" name="cheque_number" size="30" value="<%= quest.getValue("CHEQUE_NUMBER") %>" readonly="readonly" class="inputfield-ro"></td>
			</tr>
			<tr>
				<td><%= Bean.questionnaireXML.getfieldTransl("name_serv_plce_where_card_sold", false) %>
					<%= Bean.getGoToJurPrsHyperLink(quest.getValue("ID_SERV_PLACE_WHERE_CARD_SOLD")) %>
		  		</td><td><input type="text" name="name_serv_plce_where_card_sold" size="30" value="<%= Bean.getServicePlaceName(quest.getValue("ID_SERV_PLACE_WHERE_CARD_SOLD")) %>" readonly="readonly" class="inputfield-ro"></td>

					<td><%= Bean.questionnaireXML.getfieldTransl("promoter_code", false) %>
						<%= Bean.getGoToLogisticPromoterLink(quest.getValue("ID_LG_PROMOTER")) %>
			  		</td>
					<td>
						<input type="text" name="promoter_code" size="10" value="<%= quest.getValue("PROMOTER_CODE") %>" class="inputfield">
						<% if (!(quest.getValue("ID_LG_PROMOTER")==null || "".equalsIgnoreCase(quest.getValue("ID_LG_PROMOTER")))) { %>
						<input type="text" name="name_lg_promoter" size="13" value="<%= Bean.getLGPromoterName(quest.getValue("ID_LG_PROMOTER")) %>" readonly="readonly" class="inputfield-ro">
						<% } %>
					</td>
			</tr>
			<tr>
		 	    <td><%= Bean.club_actionXML.getfieldTransl("name_club_event", false) %>
					<%= Bean.getGoToClubEventLink(quest.getValue("ID_CLUB_EVENT")) %>
				</td>
			  	<td><input type="text" id="name_club_event" name="name_club_event" size="30" value="<%= Bean.getClubActionName(quest.getValue("ID_CLUB_EVENT")) %>" readonly="readonly" class="inputfield-ro"></td>
				<td><%= Bean.questionnaireXML.getfieldTransl("validity_percent", false) %> </td><td><input type="text" name="validity_percent" size="30" value="<%= quest.getValue("VALIDITY_PERCENT_FRMT") %>" readonly="readonly" class="inputfield-ro"></td>
			</tr>

			<tr>
				<td colspan="4" class="top_line"><b><%= Bean.questionnaireXML.getfieldTransl("h_bon_card", false) %></b></td>
			</tr>
			<tr>
				<td><%= Bean.questionnaireXML.getfieldTransl("cd_card", false) %>
					<%= Bean.getGoToClubCardLink(
							quest.getValue("CARD_SERIAL_NUMBER"),
							quest.getValue("CARD_ID_ISSUER"),
							quest.getValue("CARD_ID_PAYMENT_SYSTEM")
						) %>
				</td><td><input type="text" name="cd_card" size="30" value="<%= quest.getValue("CD_CARD") %>" readonly="readonly" class="inputfield-ro"></td>
				<td><%= Bean.questionnaireXML.getfieldTransl("number_bon_category_in_quest", false) %></td> <td><input type="text" name="id_bon_category" size="30" value="<%= Bean.getCardCategoryName2(quest.getValue("ID_BON_CATEGORY_IN_QUEST")) %>" readonly="readonly" class="inputfield-ro"> </td>
			</tr>
			<tr>
				<td colspan="2">&nbsp;</td>
				<td><%= Bean.questionnaireXML.getfieldTransl("number_disc_category_in_quest", false) %></td> <td><input type="text" name="id_disc_category" size="30" value="<%= Bean.getCardCategoryName2(quest.getValue("ID_DISC_CATEGORY_IN_QUEST")) %>" readonly="readonly" class="inputfield-ro"> </td>
			</tr>
			<tr>
				<td colspan="4" class="top_line"><b><%= Bean.questionnaireXML.getfieldTransl("h_discount_card", false) %></b></td>
			</tr>
			<tr>
				<td><%= Bean.questionnaireXML.getfieldTransl("discount_card_number", false) %></td><td><input type="text" name="discount_card_number" size="30" value="<%= quest.getValue("DISCOUNT_CARD_NUMBER") %>" class="inputfield"></td>
				<td><%= Bean.questionnaireXML.getfieldTransl("is_discount_card_changed", false) %></td><td><select name="is_discount_card_changed" id="is_discount_card_changed" class="inputfield"><%= Bean.getYesNoLookupOptions(quest.getValue("IS_DISCOUNT_CARD_CHANGED"),true) %></select></td>
			</tr>
			<tr>
				<td><%= Bean.questionnaireXML.getfieldTransl("discount_card_percent", false) %></td><td><input type="text" name="discount_card_percent" size="30" value="<%= quest.getValue("DISCOUNT_CARD_PERCENT") %>" class="inputfield"></td>
				<td><%= Bean.questionnaireXML.getfieldTransl("is_furchet_discount_card", false) %></td><td><select name="is_furchet_discount_card" id="is_furchet_discount_card" class="inputfield"><%= Bean.getYesNoLookupOptions(quest.getValue("IS_FURCHET_DISCOUNT_CARD"),true) %></select></td>
			</tr>
			<tr>
				<td colspan="4" class="top_line"><b><%= Bean.questionnaireXML.getfieldTransl("h_client_info", false) %></b></td>
			</tr>
			<tr>
				<td><%= Bean.questionnaireXML.getfieldTransl("tax_code", false) %></td><td><input type="text" name="tax_code" size="30" value="<%= quest.getValue("TAX_CODE") %>" readonly="readonly" class="inputfield-ro"></td>
				<td><%= Bean.questionnaireXML.getfieldTransl("pasport_code_country", false) %></td><td><input type="text" name="pasport_code_country" size="30" value="<%= quest.getValue("PASPORT_CODE_COUNTRY") %>" readonly="readonly" class="inputfield-ro"></td>
			</tr>
			<tr>
 			  	<td><%= Bean.questionnaireXML.getfieldTransl("surname", false) %>
					<%= Bean.getGoToNatPrsLink(quest.getValue("ID_NAT_PRS")) %>
		  		</td><td><input type="text" name="surname" size="30" value="<%= quest.getValue("SURNAME") %>" readonly="readonly" class="inputfield-ro"></td>
				<td><%= Bean.questionnaireXML.getfieldTransl("pasport_number", false) %></td><td><input type="text" name="pasport_number" size="30" value="<%= quest.getValue("PASPORT_NUMBER") %>" readonly="readonly" class="inputfield-ro"></td>
			</tr>
			<tr>
				<td><%= Bean.questionnaireXML.getfieldTransl("name", false) %> </td><td><input type="text" name="name" size="30" value="<%= quest.getValue("NAME") %>" readonly="readonly" class="inputfield-ro"></td>
				<td><%= Bean.questionnaireXML.getfieldTransl("pasport_date", false) %> </td><td><input type="text" name="pasport_date" size="10" value="<%= quest.getValue("PASPORT_DATE_FRMT") %>" readonly="readonly" class="inputfield-ro"></td>
			</tr>
			<tr>
				<td><%= Bean.questionnaireXML.getfieldTransl("patronymic", false) %></td><td><input type="text" name="patronymic" size="30" value="<%= quest.getValue("PATRONYMIC") %>" readonly="readonly" class="inputfield-ro"></td>
				<td><%= Bean.questionnaireXML.getfieldTransl("pasport_text", false) %></td><td><input type="text" name="pasport_text" size="30" value="<%= quest.getValue("PASPORT_TEXT") %>" readonly="readonly" class="inputfield-ro"></td>
			</tr>
			<tr>
				<td><%= Bean.questionnaireXML.getfieldTransl("date_of_birth", false) %> </td><td><input type="text" name="date_of_birth" size="10" value="<%= quest.getValue("DATE_OF_BIRTH_FRMT") %>" readonly="readonly" class="inputfield-ro"></td>
				<td class="top_line"><%= Bean.questionnaireXML.getfieldTransl("surname_eng", false) %> </td><td class="top_line"><input type="text" name="surname_eng" size="30" value="<%= quest.getValue("SURNAME_ENG") %>" readonly="readonly" class="inputfield-ro"></td>
			</tr>
			<tr>
				<td><%= Bean.questionnaireXML.getfieldTransl("sex", false) %></td><td><input type="text" name="sex" size="30" value="<%= Bean.getMeaningFoCodeValue("MALE_FEMALE", quest.getValue("SEX")) %>" readonly="readonly" class="inputfield-ro"></td>
				<td><%= Bean.questionnaireXML.getfieldTransl("name_eng", false) %></td><td><input type="text" name="name_eng" size="30" value="<%= quest.getValue("NAME_ENG") %>" readonly="readonly" class="inputfield-ro"></td>
			</tr>
			<tr>
				<td valign=top><%= Bean.questionnaireXML.getfieldTransl("CD_NAT_PRS_GROUP", false) %></td><td><input type="text" name="CD_NAT_PRS_GROUP" size="30" value="<%= Bean.getNatPrsGroupName(quest.getValue("NAME_NAT_PRS_GROUP")) %>" readonly="readonly" class="inputfield-ro"></td>
				<% if ("OTHER_VARIANT".equalsIgnoreCase(quest.getValue("NAME_NAT_PRS_GROUP"))) { %>			
					<td id='otherName'><%= Bean.questionnaireXML.getfieldTransl("NAME_OTHER_VARIANT_GROUP", false) %></td><td id="other" ><input type="text" name="NAME_OTHER_VARIANT_GROUP" size="30" value="<%=quest.getValue("NAME_OTHER_VARIANT_GROUP") %>" readonly="readonly" class="inputfield-ro"></td>
				<% } %>
			</tr>

			<tr>
				<td colspan="4" class="top_line"><b><%= Bean.natprsXML.getfieldTransl("h_contact_information", false) %></b></td>
			</tr>
			<tr>
				<td><%= Bean.questionnaireXML.getfieldTransl("phone_work", false) %> </td><td><input type="text" name="phone_work" size="30" value="<%= quest.getValue("PHONE_WORK") %>" readonly="readonly" class="inputfield-ro"></td>
				<td><%= Bean.questionnaireXML.getfieldTransl("phone_mobile", false) %></td><td><input type="text" name="phone_mobile" size="30" value="<%= quest.getValue("PHONE_MOBILE") %>" readonly="readonly" class="inputfield-ro"></td>
			</tr>
			<tr>
				<td><%= Bean.questionnaireXML.getfieldTransl("phone_home", false) %></td><td><input type="text" name="phone_home" size="30" value="<%= quest.getValue("PHONE_HOME") %>" readonly="readonly" class="inputfield-ro"></td>
				<td><%= Bean.questionnaireXML.getfieldTransl("email", false) %> </td><td><input type="text" name="email" size="30" value="<%= quest.getValue("EMAIL") %>" readonly="readonly" class="inputfield-ro"></td>
			</tr>
			<tr>
				<td colspan="4" class="top_line"><b><%= Bean.natprsXML.getfieldTransl("FACT_ADR_FULL", false) %></b></td>
			</tr>
			<tr>
				<td><%= Bean.questionnaireXML.getfieldTransl("code_country", false) %></td><td><input type="text" name="fact_code_country" size="30" value="<%= Bean.getCountryName(quest.getValue("FACT_CODE_COUNTRY")) %>" readonly="readonly" class="inputfield-ro"></td>
				<td><%= Bean.questionnaireXML.getfieldTransl("fact_adr_city", false) %></td><td><input type="text" name="fact_adr_city" size="30" value="<%= quest.getValue("FACT_ADR_CITY") %>" readonly="readonly" class="inputfield-ro"></td>
			</tr>
			<tr>
				<td><%= Bean.questionnaireXML.getfieldTransl("fact_adr_zip_code", false) %></td><td><input type="text" name="fact_adr_zip_code" size="30" value="<%= quest.getValue("FACT_ADR_ZIP_CODE") %>" readonly="readonly" class="inputfield-ro"></td>
				<td><%= Bean.questionnaireXML.getfieldTransl("fact_adr_street", false) %> </td><td><input type="text" name="fact_adr_street" size="30" value="<%= quest.getValue("FACT_ADR_STREET") %>" readonly="readonly" class="inputfield-ro"></td>
			</tr>
			<tr>
				<td><%= Bean.questionnaireXML.getfieldTransl("fact_adr_oblast", false) %> </td><td><input type="text" name="fact_adr_street" size="30" value="<%= quest.getValue("FACT_ADR_NAME_OBLAST") %>" readonly="readonly" class="inputfield-ro"></td>
				<td><%= Bean.questionnaireXML.getfieldTransl("fact_adr_house", false) %>/<%= Bean.questionnaireXML.getfieldTransl("fact_adr_case", false) %></td>
				<td>
					<input type="text" name="fact_adr_house" size="10" value="<%= quest.getValue("FACT_ADR_HOUSE") %>" readonly="readonly" class="inputfield-ro">
					/&nbsp;<input type="text" name="fact_adr_case" size="10" value="<%= quest.getValue("FACT_ADR_CASE") %>" readonly="readonly" class="inputfield-ro">
			    </td>
			</tr>
			<tr>
				<td><%= Bean.questionnaireXML.getfieldTransl("fact_adr_district", false) %></td><td><input type="text" name="fact_adr_district" size="30" value="<%= quest.getValue("FACT_ADR_DISTRICT") %>" readonly="readonly" class="inputfield-ro"></td>
				<td><%= Bean.questionnaireXML.getfieldTransl("fact_adr_apartment", false) %></td>
				<td>
					<input type="text" name="fact_adr_apartment" size="10" value="<%= quest.getValue("FACT_ADR_APARTMENT") %>" readonly="readonly" class="inputfield-ro">
			    </td>
			</tr>
			<tr>
				<td colspan="4" class="top_line"><b><%= Bean.natprsXML.getfieldTransl("REG_ADR_FULL", false) %></b></td>
			</tr>
			<tr>
				<td><%= Bean.questionnaireXML.getfieldTransl("code_country", false) %></td><td><input type="text" name="reg_code_country" size="30" value="<%= Bean.getCountryName(quest.getValue("REG_CODE_COUNTRY")) %>" readonly="readonly" class="inputfield-ro"></td>
				<td><%= Bean.questionnaireXML.getfieldTransl("reg_adr_city", false) %> </td><td><input type="text" name="reg_adr_city" size="30" value="<%= quest.getValue("REG_ADR_CITY") %>" readonly="readonly" class="inputfield-ro"></td>
			</tr>
			<tr>
				<td><%= Bean.questionnaireXML.getfieldTransl("reg_adr_zip_code", false) %> </td><td><input type="text" name="reg_adr_zip_code" size="30" value="<%= quest.getValue("REG_ADR_ZIP_CODE") %>" readonly="readonly" class="inputfield-ro"></td>
				<td><%= Bean.questionnaireXML.getfieldTransl("reg_adr_street", false) %></td><td><input type="text" name="reg_adr_street" size="30" value="<%= quest.getValue("REG_ADR_STREET") %>" readonly="readonly" class="inputfield-ro"></td>
			</tr>
			<tr>
				<td><%= Bean.questionnaireXML.getfieldTransl("reg_adr_oblast", false) %></td><td><input type="text" name="reg_adr_oblast" size="30" value="<%= quest.getValue("REG_ADR_NAME_OBLAST") %>" readonly="readonly" class="inputfield-ro"></td>
				<td><%= Bean.questionnaireXML.getfieldTransl("reg_adr_house", false) %>/<%= Bean.questionnaireXML.getfieldTransl("reg_adr_case", false) %></td>
				<td>
					<input type="text" name="reg_adr_house" size="10" value="<%= quest.getValue("REG_ADR_HOUSE") %>" readonly="readonly" class="inputfield-ro">
					/&nbsp;<input type="text" name="reg_adr_case" size="10" value="<%= quest.getValue("REG_ADR_CASE") %>" readonly="readonly" class="inputfield-ro">
				</td>
			</tr>
			<tr>
				<td><%= Bean.questionnaireXML.getfieldTransl("reg_adr_district", false) %></td><td><input type="text" name="reg_adr_district" size="30" value="<%= quest.getValue("REG_ADR_DISTRICT") %>" readonly="readonly" class="inputfield-ro"></td>
				<td><%= Bean.questionnaireXML.getfieldTransl("reg_adr_apartment", false) %></td>
				<td>
					<input type="text" name="reg_adr_apartment" size="10" value="<%= quest.getValue("REG_ADR_APARTMENT") %>" readonly="readonly" class="inputfield-ro">
				</td>
			</tr>

			<tr>
				<td colspan="4" class="top_line"><b><%= Bean.natprsXML.getfieldTransl("additional_info", false) %></b></td>
			</tr>
			<tr>
				<td><%= Bean.questionnaireXML.getfieldTransl("cd_purchase_frequence", false) %></td><td><input type="text" name="cd_purchase_frequence" size="30" value="<%= Bean.getMeaningFoCodeValue("NAT_PRS_PURCHASE_FREQUENCE",quest.getValue("CD_PURCHASE_FREQUENCE")) %>" readonly="readonly" class="inputfield-ro"></td>
				<td><%= Bean.questionnaireXML.getfieldTransl("has_auto", false) %></td><td><input type="text" name="has_auto" size="10" value="<%= Bean.getMeaningForNumValue("YES_NO",quest.getValue("HAS_AUTO")) %>" readonly="readonly" class="inputfield-ro"></td>
			</tr>
			<tr>
				<td colspan="4" class="top_line"><b><%= Bean.questionnaireXML.getfieldTransl("shop_advantage", false) %></b></td>
			</tr>
			<tr>
				<td valign=top>&nbsp;</td>
				<td>
					<input type="checkbox" name="shop_advantage1" size="30" value="Y" readonly="readonly" class="inputfield-ro" disabled <% if (!(quest.getValue("SHOP_ADVANTAGE1")== null) && !"".equalsIgnoreCase(quest.getValue("SHOP_ADVANTAGE1"))) { %>checked<%} %>><%= Bean.questionnaireXML.getfieldTransl("shop_advantage1", false) %>
				</td>
				<td>
					<input type="checkbox" name="shop_advantage2" size="30" value="Y" readonly="readonly" class="inputfield-ro" disabled <% if (!(quest.getValue("SHOP_ADVANTAGE2")== null) && !"".equalsIgnoreCase(quest.getValue("SHOP_ADVANTAGE2"))) { %>checked<%} %>><%= Bean.questionnaireXML.getfieldTransl("shop_advantage2", false) %>
				</td>
				<td>
					<input type="checkbox" name="shop_advantage5" size="20" value="Y" readonly="readonly" class="inputfield-ro" disabled <% if (!(quest.getValue("SHOP_ADVANTAGE5")== null) && !"".equalsIgnoreCase(quest.getValue("SHOP_ADVANTAGE5"))) { %>checked<%} %>><%= Bean.questionnaireXML.getfieldTransl("shop_advantage5", false) %>
				</td>
			</tr>
			<tr>
				<td valign=top>&nbsp;</td>
				<td>
					<input type="checkbox" name="shop_advantage3" size="20" value="Y" readonly="readonly" class="inputfield-ro" disabled <% if (!(quest.getValue("SHOP_ADVANTAGE3")== null) && !"".equalsIgnoreCase(quest.getValue("SHOP_ADVANTAGE3"))) { %>checked<%} %>><%= Bean.questionnaireXML.getfieldTransl("shop_advantage3", false) %>
				</td>
				<td>
					<input type="checkbox" name="shop_advantage4" size="20" value="Y" readonly="readonly" class="inputfield-ro" disabled <% if (!(quest.getValue("SHOP_ADVANTAGE4")== null) && !"".equalsIgnoreCase(quest.getValue("SHOP_ADVANTAGE4"))) { %>checked<%} %>><%= Bean.questionnaireXML.getfieldTransl("shop_advantage4", false) %>
				</td>
			</tr>
			<tr>
				<td colspan="4" class="top_line"><b><%= Bean.questionnaireXML.getfieldTransl("reception_information_way", false) %></b></td>
			</tr>
			<tr>
				<td valign=top>&nbsp;</td>
				<td>
					<input type="checkbox" name="reception_information_way1" size="30" value="Y" readonly="readonly" class="inputfield-ro" disabled <% if (!(quest.getValue("RECEPTION_INFORMATION_WAY1")== null) && !"".equalsIgnoreCase(quest.getValue("RECEPTION_INFORMATION_WAY1"))) { %>checked<%} %>><%= Bean.questionnaireXML.getfieldTransl("reception_information_way1", false) %>
				</td>
				<td>
					<input type="checkbox" name="reception_information_way2" size="30" value="Y" readonly="readonly" class="inputfield-ro" disabled <% if (!(quest.getValue("RECEPTION_INFORMATION_WAY2")== null) && !"".equalsIgnoreCase(quest.getValue("RECEPTION_INFORMATION_WAY2"))) { %>checked<%} %>><%= Bean.questionnaireXML.getfieldTransl("reception_information_way2", false) %>
				</td>
			</tr>
			<tr>
				<td valign=top>&nbsp;</td>
				<td>
					<input type="checkbox" name="reception_information_way3" size="20" value="Y" readonly="readonly" class="inputfield-ro" disabled <% if (!(quest.getValue("RECEPTION_INFORMATION_WAY3")== null) && !"".equalsIgnoreCase(quest.getValue("RECEPTION_INFORMATION_WAY3"))) { %>checked<%} %>><%= Bean.questionnaireXML.getfieldTransl("reception_information_way3", false) %>
				</td>
				<td>
					<input type="checkbox" name="reception_information_way4" size="20" value="Y" readonly="readonly" class="inputfield-ro" disabled <% if (!(quest.getValue("RECEPTION_INFORMATION_WAY4")== null) && !"".equalsIgnoreCase(quest.getValue("RECEPTION_INFORMATION_WAY4"))) { %>checked<%} %>><%= Bean.questionnaireXML.getfieldTransl("reception_information_way4", false) %>
				</td>
			</tr>
			<tr>
				<td valign=top colspan="4" class="top_line"><b><%= Bean.questionnaireXML.getfieldTransl("h_rules", false) %></b></td>
			</tr>
			<tr>
				<td valign=top>&nbsp;</td>
				<td>
					<input type="checkbox" name="rule_bon" size="20" value="Y" readonly="readonly" class="inputfield-ro" disabled <% if (!(quest.getValue("RULE_BON")== null) && !"".equalsIgnoreCase(quest.getValue("RULE_BON"))) { %>checked<%} %>><%= Bean.questionnaireXML.getfieldTransl("rule_bon", false) %>
				</td>
				<td>
					<input type="checkbox" name="rule_discount_ground" size="20" value="Y" readonly="readonly" class="inputfield-ro" disabled <% if (!(quest.getValue("RULE_DISCOUNT_GROUND")== null) && !"".equalsIgnoreCase(quest.getValue("RULE_DISCOUNT_GROUND"))) { %>checked<%} %>><%= Bean.questionnaireXML.getfieldTransl("rule_discount_ground", false) %>
				</td>
			</tr>
			<tr>
				<td valign=top>&nbsp;</td>
				<td>
					<input type="checkbox" name="rule_superbon" size="20" value="Y" readonly="readonly" class="inputfield-ro" disabled <% if (!(quest.getValue("RULE_SUPERBON")== null) && !"".equalsIgnoreCase(quest.getValue("RULE_SUPERBON"))) { %>checked<%} %>><%= Bean.questionnaireXML.getfieldTransl("rule_superbon", false) %>
				</td>
				<td>
					&nbsp;
				</td>
			</tr>
			<%=	Bean.getCreationAndMoficationRecordFields(
					quest.getValue(Bean.getCreationDateFieldName()),
					quest.getValue("CREATED_BY"),
					quest.getValue(Bean.getLastUpdateDateFieldName()),
					quest.getValue("LAST_UPDATE_BY")
			) %>
				<tr>
					<td colspan="4" align="center">
						<%=Bean.getSubmitButtonAjax("../crm/cards/questionnaire_importupdate.jsp") %>
						<%=Bean.getResetButton() %>
						<%=Bean.getGoBackButton("../crm/cards/questionnaire_import.jsp") %>
					</td>
				</tr>
		</table>
	</form>
	<%
		
		}
	}
	if (Bean.currentMenu.isCurrentTabAndPreviewPermitted("CARDS_QUESTIONNAIRE_IMPORT_INFO")) {
			   %>
				<table <%=Bean.getTableDetailParam() %>>
				<tr>
					<td><%= Bean.questionnaireXML.getfieldTransl("id_quest_int", false) %> </td><td><input type="text" name="id_quest_int" size="10" value="<%= quest.getValue("ID_QUEST_INT") %>" readonly="readonly" class="inputfield-ro"></td>
					<td><%= Bean.clubXML.getfieldTransl("club", false) %>
					<%= Bean.getGoToClubLink(quest.getValue("ID_CLUB")) %>
					</td><td><input type="text" name="name_club" size="30" value="<%= Bean.getClubShortName(quest.getValue("ID_CLUB")) %>" readonly="readonly" class="inputfield-ro"></td>
				</tr>
				<tr>
					<td><%= Bean.questionnaireXML.getfieldTransl("state_quest", false) %></td>
					<td><input type="text" name="state_quest" size="30" value="<%= quest.getValue("STATE_QUEST_TSL") %>" readonly="readonly" class="inputfield-ro"></td>
					<% if ("IMPORTED".equalsIgnoreCase(state_quest)) { %>
					<td><%= Bean.questionnaireXML.getfieldTransl("date_import", false) %></td><td><input type="text" name="date_import" size="20" value="<%= quest.getValue("DATE_IMPORT_FRMT") %>" class="inputfield-ro" readonly></td>
					<% } else { %>
					<td colspan="2">&nbsp;</td>
					<% } %>
				</tr>
				<tr>
					<td><%= Bean.questionnaireXML.getfieldTransl("file_name", false) %></td>
					<td colspan="4"><input type="text" name="file_name" size="80" value="<%= quest.getValue("FILE_NAME_SHORT") %>" readonly="readonly" class="inputfield-ro">
					<% if (!(quest.getValue("FILE_NAME")==null || "".equalsIgnoreCase(quest.getValue("FILE_NAME")))) { %>
						<a href="../FileSender?FILENAME=<%=URLEncoder.encode(quest.getValue("FILE_NAME"),"UTF-8")%>" title="<%= Bean.buttonXML.getfieldTransl("open", false) %>" target="_blank">
							<img vspace="0" hspace="0" src="../images/oper/small/open.gif" align="top">
						</a>
					<% } %>
					</td>
				</tr>

				<tr>
					<td colspan="4" class="top_line"><b><%= Bean.getClubCardXMLFieldTransl("h_sell_information", false) %></b></td>
				</tr>
				<tr>
					<td><%= Bean.questionnaireXML.getfieldTransl("pack", false) %>
						<%= Bean.getGoToQuestionnairePackLink(quest.getValue("ID_QUEST_PACK")) %>
			  		</td>
					<td><input type="text" id="name_quest_pack" name="name_quest_pack" size="30" value="<%=quest.getValue("ID_QUEST_PACK") %>" readonly="readonly" class="inputfield-ro"></td>
					<td><%= Bean.questionnaireXML.getfieldTransl("date_card_sale", false) %> </td><td><input type="text" name="date_card_sale" size="20" value="<%= quest.getValue("DATE_CARD_SALE_DHMF") %>" readonly="readonly" class="inputfield-ro"></td>
				</tr>
				<tr>
					<td><%= Bean.questionnaireXML.getfieldTransl("sname_jur_pr_who_has_sold_card", false) %>
						<%= Bean.getGoToJurPrsHyperLink(quest.getValue("ID_JUR_PRS_WHO_HAS_SOLD_CARD")) %>
			  		</td><td><input type="text" name="sname_jur_pr_who_has_sold_card" size="30" value="<%= Bean.getJurPersonShortName(quest.getValue("ID_JUR_PRS_WHO_HAS_SOLD_CARD")) %>" readonly="readonly" class="inputfield-ro"></td>
					<td><%= Bean.questionnaireXML.getfieldTransl("cd_quest_payment_method", false) %> </td><td><input type="text" name="name_quest_payment_method" size="30" value="<%= quest.getValue("NAME_QUEST_PAYMENT_METHOD") %>" readonly="readonly" class="inputfield-ro"></td>
				</tr>
				<tr>
					<td><%= Bean.questionnaireXML.getfieldTransl("sname_jur_prs_where_card_sold", false) %>
						<%= Bean.getGoToJurPrsHyperLink(quest.getValue("ID_JUR_PRS_WHERE_CARD_SOLD")) %>
			  		</td><td><input type="text" name="sname_jur_prs_where_card_sold" size="30" value="<%= Bean.getJurPersonShortName(quest.getValue("ID_JUR_PRS_WHERE_CARD_SOLD")) %>" readonly="readonly" class="inputfield-ro"></td>
					<td><%= Bean.questionnaireXML.getfieldTransl("cheque_number", false) %></td><td><input type="text" name="cheque_number" size="30" value="<%= quest.getValue("CHEQUE_NUMBER") %>" readonly="readonly" class="inputfield-ro"></td>
				</tr>
				<tr>
					<td><%= Bean.questionnaireXML.getfieldTransl("name_serv_plce_where_card_sold", false) %>
						<%= Bean.getGoToJurPrsHyperLink(quest.getValue("ID_SERV_PLACE_WHERE_CARD_SOLD")) %>
			  		</td><td><input type="text" name="name_serv_plce_where_card_sold" size="30" value="<%= Bean.getServicePlaceName(quest.getValue("ID_SERV_PLACE_WHERE_CARD_SOLD")) %>" readonly="readonly" class="inputfield-ro"></td>
					<td><%= Bean.questionnaireXML.getfieldTransl("promoter_code", false) %>
						<%= Bean.getGoToLogisticPromoterLink(quest.getValue("ID_LG_PROMOTER")) %>
			  		</td>
					<td>
						<input type="text" name="promoter_code" size="10" value="<%= quest.getValue("PROMOTER_CODE") %>" readonly="readonly" class="inputfield-ro">
						<% if (!(quest.getValue("ID_LG_PROMOTER")==null || "".equalsIgnoreCase(quest.getValue("ID_LG_PROMOTER")))) { %>
						<input type="text" name="name_lg_promoter" size="13" value="<%= Bean.getLGPromoterName(quest.getValue("ID_LG_PROMOTER")) %>" readonly="readonly" class="inputfield-ro">
						<% } %>
					</td>
				</tr>
				<tr>
					<td colspan="2">&nbsp;</td>
					<td><%= Bean.questionnaireXML.getfieldTransl("validity_percent", false) %> </td><td><input type="text" name="validity_percent" size="30" value="<%= quest.getValue("VALIDITY_PERCENT_FRMT") %>" readonly="readonly" class="inputfield-ro"></td>
				</tr>

				<tr>
					<td colspan="4" class="top_line"><b><%= Bean.questionnaireXML.getfieldTransl("h_bon_card", false) %></b></td>
				</tr>
				<tr>
					<td><%= Bean.questionnaireXML.getfieldTransl("cd_card", false) %>
						<%= Bean.getGoToClubCardLink(
								quest.getValue("CARD_SERIAL_NUMBER"),
								quest.getValue("CARD_ID_ISSUER"),
								quest.getValue("CARD_ID_PAYMENT_SYSTEM")
							) %>
					</td><td><input type="text" name="cd_card" size="30" value="<%= quest.getValue("CD_CARD") %>" readonly="readonly" class="inputfield-ro"></td>
					<td><%= Bean.questionnaireXML.getfieldTransl("id_bon_category_in_quest", false) %></td> <td><input type="text" name="id_bon_category" size="30" value="<%= Bean.getCardCategoryName2(quest.getValue("ID_BON_CATEGORY_IN_QUEST")) %>" readonly="readonly" class="inputfield-ro"> </td>
				</tr>
				<tr>
					<td colspan="2">&nbsp;</td>
					<td><%= Bean.questionnaireXML.getfieldTransl("id_disc_category_in_quest", false) %></td> <td><input type="text" name="id_disc_category" size="30" value="<%= Bean.getCardCategoryName2(quest.getValue("ID_DISC_CATEGORY_IN_QUEST")) %>" readonly="readonly" class="inputfield-ro"> </td>
				</tr>
				<tr>
					<td colspan="4" class="top_line"><b><%= Bean.questionnaireXML.getfieldTransl("h_discount_card", false) %></b></td>
				</tr>
				<tr>
					<td><%= Bean.questionnaireXML.getfieldTransl("discount_card_number", false) %></td><td><input type="text" name="discount_card_number" size="30" value="<%= quest.getValue("DISCOUNT_CARD_NUMBER") %>" readonly="readonly" class="inputfield-ro"></td>
					<td><%= Bean.questionnaireXML.getfieldTransl("is_discount_card_changed", false) %></td><td><input type="text" name="is_discount_card_changed" size="10" value="<%= Bean.getMeaningFoCodeValue("YES_NO", quest.getValue("IS_DISCOUNT_CARD_CHANGED")) %>" readonly="readonly" class="inputfield-ro"></td>
				</tr>
				<tr>
					<td><%= Bean.questionnaireXML.getfieldTransl("discount_card_percent", false) %></td><td><input type="text" name="discount_card_percent" size="30" value="<%= quest.getValue("DISCOUNT_CARD_PERCENT") %>" readonly="readonly" class="inputfield-ro"></td>
					<td><%= Bean.questionnaireXML.getfieldTransl("is_furchet_discount_card", false) %></td><td><input type="text" name="is_furchet_discount_card" size="10" value="<%= Bean.getMeaningFoCodeValue("YES_NO", quest.getValue("IS_FURCHET_DISCOUNT_CARD")) %>" readonly="readonly" class="inputfield-ro"></td>
				</tr>
				<tr>
					<td colspan="4" class="top_line"><b><%= Bean.questionnaireXML.getfieldTransl("h_client_info", false) %></b></td>
				</tr>
				<tr>
					<td><%= Bean.questionnaireXML.getfieldTransl("tax_code", false) %></td><td><input type="text" name="tax_code" size="30" value="<%= quest.getValue("TAX_CODE") %>" readonly="readonly" class="inputfield-ro"></td>
					<td><%= Bean.questionnaireXML.getfieldTransl("pasport_code_country", false) %></td><td><input type="text" name="pasport_code_country" size="30" value="<%= quest.getValue("PASPORT_CODE_COUNTRY") %>" readonly="readonly" class="inputfield-ro"></td>
				</tr>
				<tr>
	  			  	<td><%= Bean.questionnaireXML.getfieldTransl("surname", false) %>
						<%= Bean.getGoToNatPrsLink(quest.getValue("ID_NAT_PRS")) %>
			  		</td><td><input type="text" name="surname" size="30" value="<%= quest.getValue("SURNAME") %>" readonly="readonly" class="inputfield-ro"></td>
					<td><%= Bean.questionnaireXML.getfieldTransl("pasport_number", false) %></td><td><input type="text" name="pasport_number" size="30" value="<%= quest.getValue("PASPORT_NUMBER") %>" readonly="readonly" class="inputfield-ro"></td>
				</tr>
				<tr>
					<td><%= Bean.questionnaireXML.getfieldTransl("name", false) %> </td><td><input type="text" name="name" size="30" value="<%= quest.getValue("NAME") %>" readonly="readonly" class="inputfield-ro"></td>
					<td><%= Bean.questionnaireXML.getfieldTransl("pasport_date", false) %> </td><td><input type="text" name="pasport_date" size="10" value="<%= quest.getValue("PASPORT_DATE_FRMT") %>" readonly="readonly" class="inputfield-ro"></td>
				</tr>
				<tr>
					<td><%= Bean.questionnaireXML.getfieldTransl("patronymic", false) %></td><td><input type="text" name="patronymic" size="30" value="<%= quest.getValue("PATRONYMIC") %>" readonly="readonly" class="inputfield-ro"></td>
					<td><%= Bean.questionnaireXML.getfieldTransl("pasport_text", false) %></td><td><input type="text" name="pasport_text" size="30" value="<%= quest.getValue("PASPORT_TEXT") %>" readonly="readonly" class="inputfield-ro"></td>
				</tr>
				<tr>
					<td><%= Bean.questionnaireXML.getfieldTransl("date_of_birth", false) %> </td><td><input type="text" name="date_of_birth" size="10" value="<%= quest.getValue("DATE_OF_BIRTH_FRMT") %>" readonly="readonly" class="inputfield-ro"></td>
					<td class="top_line"><%= Bean.questionnaireXML.getfieldTransl("surname_eng", false) %> </td><td class="top_line"><input type="text" name="surname_eng" size="30" value="<%= quest.getValue("SURNAME_ENG") %>" readonly="readonly" class="inputfield-ro"></td>
				</tr>
				<tr>
					<td><%= Bean.questionnaireXML.getfieldTransl("sex", false) %></td><td><input type="text" name="sex" size="30" value="<%= Bean.getMeaningFoCodeValue("MALE_FEMALE", quest.getValue("SEX")) %>" readonly="readonly" class="inputfield-ro"></td>
					<td><%= Bean.questionnaireXML.getfieldTransl("name_eng", false) %></td><td><input type="text" name="name_eng" size="30" value="<%= quest.getValue("NAME_ENG") %>" readonly="readonly" class="inputfield-ro"></td>
				</tr>
				<tr>
					<td valign=top><%= Bean.questionnaireXML.getfieldTransl("CD_NAT_PRS_GROUP", false) %></td><td><input type="text" name="CD_NAT_PRS_GROUP" size="30" value="<%= Bean.getNatPrsGroupName(quest.getValue("NAME_NAT_PRS_GROUP")) %>" readonly="readonly" class="inputfield-ro"></td>
					<% if ("OTHER_VARIANT".equalsIgnoreCase(quest.getValue("NAME_NAT_PRS_GROUP"))) { %>			
						<td id='otherName'><%= Bean.questionnaireXML.getfieldTransl("NAME_OTHER_VARIANT_GROUP", false) %></td><td id="other" ><input type="text" name="NAME_OTHER_VARIANT_GROUP" size="30" value="<%=quest.getValue("NAME_OTHER_VARIANT_GROUP") %>" readonly="readonly" class="inputfield-ro"></td>
					<% } %>
				</tr>

				<tr>
					<td colspan="4" class="top_line"><b><%= Bean.natprsXML.getfieldTransl("h_contact_information", false) %></b></td>
				</tr>
				<tr>
					<td><%= Bean.questionnaireXML.getfieldTransl("phone_work", false) %> </td><td><input type="text" name="phone_work" size="30" value="<%= quest.getValue("PHONE_WORK") %>" readonly="readonly" class="inputfield-ro"></td>
					<td><%= Bean.questionnaireXML.getfieldTransl("phone_mobile", false) %></td><td><input type="text" name="phone_mobile" size="30" value="<%= quest.getValue("PHONE_MOBILE") %>" readonly="readonly" class="inputfield-ro"></td>
				</tr>
				<tr>
					<td><%= Bean.questionnaireXML.getfieldTransl("phone_home", false) %></td><td><input type="text" name="phone_home" size="30" value="<%= quest.getValue("PHONE_HOME") %>" readonly="readonly" class="inputfield-ro"></td>
					<td><%= Bean.questionnaireXML.getfieldTransl("email", false) %> </td><td><input type="text" name="email" size="30" value="<%= quest.getValue("EMAIL") %>" readonly="readonly" class="inputfield-ro"></td>
				</tr>
				<tr>
					<td colspan="4" class="top_line"><b><%= Bean.natprsXML.getfieldTransl("FACT_ADR_FULL", false) %></b></td>
				</tr>
				<tr>
					<td><%= Bean.questionnaireXML.getfieldTransl("code_country", false) %></td><td><input type="text" name="fact_code_country" size="30" value="<%= Bean.getCountryName(quest.getValue("FACT_CODE_COUNTRY")) %>" readonly="readonly" class="inputfield-ro"></td>
					<td><%= Bean.questionnaireXML.getfieldTransl("fact_adr_city", false) %></td><td><input type="text" name="fact_adr_city" size="30" value="<%= quest.getValue("FACT_ADR_CITY") %>" readonly="readonly" class="inputfield-ro"></td>
				</tr>
				<tr>
					<td><%= Bean.questionnaireXML.getfieldTransl("fact_adr_zip_code", false) %></td><td><input type="text" name="fact_adr_zip_code" size="30" value="<%= quest.getValue("FACT_ADR_ZIP_CODE") %>" readonly="readonly" class="inputfield-ro"></td>
					<td><%= Bean.questionnaireXML.getfieldTransl("fact_adr_street", false) %> </td><td><input type="text" name="fact_adr_street" size="30" value="<%= quest.getValue("FACT_ADR_STREET") %>" readonly="readonly" class="inputfield-ro"></td>
				</tr>
				<tr>
					<td><%= Bean.questionnaireXML.getfieldTransl("fact_adr_oblast", false) %> </td><td><input type="text" name="fact_adr_street" size="30" value="<%= quest.getValue("FACT_ADR_NAME_OBLAST") %>" readonly="readonly" class="inputfield-ro"></td>
					<td><%= Bean.questionnaireXML.getfieldTransl("fact_adr_house", false) %>/<%= Bean.questionnaireXML.getfieldTransl("fact_adr_case", false) %></td>
					<td>
						<input type="text" name="fact_adr_house" size="10" value="<%= quest.getValue("FACT_ADR_HOUSE") %>" readonly="readonly" class="inputfield-ro">
						/&nbsp;<input type="text" name="fact_adr_case" size="10" value="<%= quest.getValue("FACT_ADR_CASE") %>" readonly="readonly" class="inputfield-ro">
				    </td>
				</tr>
				<tr>
					<td><%= Bean.questionnaireXML.getfieldTransl("fact_adr_district", false) %></td><td><input type="text" name="fact_adr_district" size="30" value="<%= quest.getValue("FACT_ADR_DISTRICT") %>" readonly="readonly" class="inputfield-ro"></td>
					<td><%= Bean.questionnaireXML.getfieldTransl("fact_adr_apartment", false) %></td>
					<td>
						<input type="text" name="fact_adr_apartment" size="10" value="<%= quest.getValue("FACT_ADR_APARTMENT") %>" readonly="readonly" class="inputfield-ro">
				    </td>
				</tr>
				<tr>
					<td colspan="4" class="top_line"><b><%= Bean.natprsXML.getfieldTransl("REG_ADR_FULL", false) %></b></td>
				</tr>
				<tr>
					<td><%= Bean.questionnaireXML.getfieldTransl("code_country", false) %></td><td><input type="text" name="reg_code_country" size="30" value="<%= Bean.getCountryName(quest.getValue("REG_CODE_COUNTRY")) %>" readonly="readonly" class="inputfield-ro"></td>
					<td><%= Bean.questionnaireXML.getfieldTransl("reg_adr_city", false) %> </td><td><input type="text" name="reg_adr_city" size="30" value="<%= quest.getValue("REG_ADR_CITY") %>" readonly="readonly" class="inputfield-ro"></td>
				</tr>
				<tr>
					<td><%= Bean.questionnaireXML.getfieldTransl("reg_adr_zip_code", false) %> </td><td><input type="text" name="reg_adr_zip_code" size="30" value="<%= quest.getValue("REG_ADR_ZIP_CODE") %>" readonly="readonly" class="inputfield-ro"></td>
					<td><%= Bean.questionnaireXML.getfieldTransl("reg_adr_street", false) %></td><td><input type="text" name="reg_adr_street" size="30" value="<%= quest.getValue("REG_ADR_STREET") %>" readonly="readonly" class="inputfield-ro"></td>
				</tr>
				<tr>
					<td><%= Bean.questionnaireXML.getfieldTransl("reg_adr_oblast", false) %></td><td><input type="text" name="reg_adr_oblast" size="30" value="<%= quest.getValue("REG_ADR_NAME_OBLAST") %>" readonly="readonly" class="inputfield-ro"></td>
					<td><%= Bean.questionnaireXML.getfieldTransl("reg_adr_house", false) %>/<%= Bean.questionnaireXML.getfieldTransl("reg_adr_case", false) %></td>
					<td>
						<input type="text" name="reg_adr_house" size="10" value="<%= quest.getValue("REG_ADR_HOUSE") %>" readonly="readonly" class="inputfield-ro">
						/&nbsp;<input type="text" name="reg_adr_case" size="10" value="<%= quest.getValue("REG_ADR_CASE") %>" readonly="readonly" class="inputfield-ro">
					</td>
				</tr>
				<tr>
					<td><%= Bean.questionnaireXML.getfieldTransl("reg_adr_district", false) %></td><td><input type="text" name="reg_adr_district" size="30" value="<%= quest.getValue("REG_ADR_DISTRICT") %>" readonly="readonly" class="inputfield-ro"></td>
					<td><%= Bean.questionnaireXML.getfieldTransl("reg_adr_apartment", false) %></td>
					<td>
						<input type="text" name="reg_adr_apartment" size="10" value="<%= quest.getValue("REG_ADR_APARTMENT") %>" readonly="readonly" class="inputfield-ro">
					</td>
				</tr>

				<tr>
					<td colspan="4" class="top_line"><b><%= Bean.natprsXML.getfieldTransl("additional_info", false) %></b></td>
				</tr>
				<tr>
					<td><%= Bean.questionnaireXML.getfieldTransl("cd_purchase_frequence", false) %></td><td><input type="text" name="cd_purchase_frequence" size="30" value="<%= Bean.getMeaningFoCodeValue("NAT_PRS_PURCHASE_FREQUENCE",quest.getValue("CD_PURCHASE_FREQUENCE")) %>" readonly="readonly" class="inputfield-ro"></td>
					<td><%= Bean.questionnaireXML.getfieldTransl("has_auto", false) %></td><td><input type="text" name="has_auto" size="10" value="<%= Bean.getMeaningForNumValue("YES_NO",quest.getValue("HAS_AUTO")) %>" readonly="readonly" class="inputfield-ro"></td>
				</tr>
				<tr>
					<td colspan="4" class="top_line"><b><%= Bean.questionnaireXML.getfieldTransl("shop_advantage", false) %></b></td>
				</tr>
				<tr>
					<td valign=top>&nbsp;</td>
					<td>
						<input type="checkbox" name="shop_advantage1" size="30" value="Y" readonly="readonly" class="inputfield-ro" disabled <% if (!(quest.getValue("SHOP_ADVANTAGE1")== null) && !"".equalsIgnoreCase(quest.getValue("SHOP_ADVANTAGE1"))) { %>checked<%} %>><%= Bean.questionnaireXML.getfieldTransl("shop_advantage1", false) %>
					</td>
					<td>
						<input type="checkbox" name="shop_advantage2" size="30" value="Y" readonly="readonly" class="inputfield-ro" disabled <% if (!(quest.getValue("SHOP_ADVANTAGE2")== null) && !"".equalsIgnoreCase(quest.getValue("SHOP_ADVANTAGE2"))) { %>checked<%} %>><%= Bean.questionnaireXML.getfieldTransl("shop_advantage2", false) %>
					</td>
					<td>
						<input type="checkbox" name="shop_advantage5" size="20" value="Y" readonly="readonly" class="inputfield-ro" disabled <% if (!(quest.getValue("SHOP_ADVANTAGE5")== null) && !"".equalsIgnoreCase(quest.getValue("SHOP_ADVANTAGE5"))) { %>checked<%} %>><%= Bean.questionnaireXML.getfieldTransl("shop_advantage5", false) %>
					</td>
				</tr>
				<tr>
					<td valign=top>&nbsp;</td>
					<td>
						<input type="checkbox" name="shop_advantage3" size="20" value="Y" readonly="readonly" class="inputfield-ro" disabled <% if (!(quest.getValue("SHOP_ADVANTAGE3")== null) && !"".equalsIgnoreCase(quest.getValue("SHOP_ADVANTAGE3"))) { %>checked<%} %>><%= Bean.questionnaireXML.getfieldTransl("shop_advantage3", false) %>
					</td>
					<td>
						<input type="checkbox" name="shop_advantage4" size="20" value="Y" readonly="readonly" class="inputfield-ro" disabled <% if (!(quest.getValue("SHOP_ADVANTAGE4")== null) && !"".equalsIgnoreCase(quest.getValue("SHOP_ADVANTAGE4"))) { %>checked<%} %>><%= Bean.questionnaireXML.getfieldTransl("shop_advantage4", false) %>
					</td>
				</tr>
				<tr>
					<td colspan="4" class="top_line"><b><%= Bean.questionnaireXML.getfieldTransl("reception_information_way", false) %></b></td>
				</tr>
				<tr>
					<td valign=top>&nbsp;</td>
					<td>
						<input type="checkbox" name="reception_information_way1" size="30" value="Y" readonly="readonly" class="inputfield-ro" disabled <% if (!(quest.getValue("RECEPTION_INFORMATION_WAY1")== null) && !"".equalsIgnoreCase(quest.getValue("RECEPTION_INFORMATION_WAY1"))) { %>checked<%} %>><%= Bean.questionnaireXML.getfieldTransl("reception_information_way1", false) %>
					</td>
					<td>
						<input type="checkbox" name="reception_information_way2" size="30" value="Y" readonly="readonly" class="inputfield-ro" disabled <% if (!(quest.getValue("RECEPTION_INFORMATION_WAY2")== null) && !"".equalsIgnoreCase(quest.getValue("RECEPTION_INFORMATION_WAY2"))) { %>checked<%} %>><%= Bean.questionnaireXML.getfieldTransl("reception_information_way2", false) %>
					</td>
				</tr>
				<tr>
					<td valign=top>&nbsp;</td>
					<td>
						<input type="checkbox" name="reception_information_way3" size="20" value="Y" readonly="readonly" class="inputfield-ro" disabled <% if (!(quest.getValue("RECEPTION_INFORMATION_WAY3")== null) && !"".equalsIgnoreCase(quest.getValue("RECEPTION_INFORMATION_WAY3"))) { %>checked<%} %>><%= Bean.questionnaireXML.getfieldTransl("reception_information_way3", false) %>
					</td>
					<td>
						<input type="checkbox" name="reception_information_way4" size="20" value="Y" readonly="readonly" class="inputfield-ro" disabled <% if (!(quest.getValue("RECEPTION_INFORMATION_WAY4")== null) && !"".equalsIgnoreCase(quest.getValue("RECEPTION_INFORMATION_WAY4"))) { %>checked<%} %>><%= Bean.questionnaireXML.getfieldTransl("reception_information_way4", false) %>
					</td>
				</tr>
				<tr>
					<td valign=top colspan="4" class="top_line"><b><%= Bean.questionnaireXML.getfieldTransl("h_rules", false) %></b></td>
				</tr>
				<tr>
					<td valign=top>&nbsp;</td>
					<td>
						<input type="checkbox" name="rule_bon" size="20" value="Y" readonly="readonly" class="inputfield-ro" disabled <% if (!(quest.getValue("RULE_BON")== null) && !"".equalsIgnoreCase(quest.getValue("RULE_BON"))) { %>checked<%} %>><%= Bean.questionnaireXML.getfieldTransl("rule_bon", false) %>
					</td>
					<td>
						<input type="checkbox" name="rule_discount_ground" size="20" value="Y" readonly="readonly" class="inputfield-ro" disabled <% if (!(quest.getValue("RULE_DISCOUNT_GROUND")== null) && !"".equalsIgnoreCase(quest.getValue("RULE_DISCOUNT_GROUND"))) { %>checked<%} %>><%= Bean.questionnaireXML.getfieldTransl("rule_discount_ground", false) %>
					</td>
				</tr>
				<tr>
					<td valign=top>&nbsp;</td>
					<td>
						<input type="checkbox" name="rule_superbon" size="20" value="Y" readonly="readonly" class="inputfield-ro" disabled <% if (!(quest.getValue("RULE_SUPERBON")== null) && !"".equalsIgnoreCase(quest.getValue("RULE_SUPERBON"))) { %>checked<%} %>><%= Bean.questionnaireXML.getfieldTransl("rule_superbon", false) %>
					</td>
					<td>
						&nbsp;
					</td>
				</tr>
				<%=	Bean.getCreationAndMoficationRecordFields(
						quest.getValue(Bean.getCreationDateFieldName()),
						quest.getValue("CREATED_BY"),
						quest.getValue(Bean.getLastUpdateDateFieldName()),
						quest.getValue("LAST_UPDATE_BY")
				) %>
				<tr>
					<td colspan="4" align="center">
						<%=Bean.getGoBackButton("../crm/cards/questionnaire_import.jsp") %>
					</td>
				</tr>
			</table>
		<%
	}

	if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CARDS_QUESTIONNAIRE_IMPORT_TASKS")) {%>
	<table <%=Bean.getTableBottomFilter() %>>
	  	<tr>
		<%= Bean.getFindHTML("task_find", task_find, "../crm/cards/questionnaire_importspecs.jsp?id=" + id + "&task_page=1") %>

		<%=Bean.getSelectOnChangeBeginHTML("task_type", "../crm/cards/questionnaire_importspecs.jsp?id=" + id + "&task_page=1", Bean.card_taskXML.getfieldTransl("CD_CARD_OPERATION_TYPE", false)) %>
			<%= Bean.getClubCardOperationTypeOptions(task_type, true) %>
		<%=Bean.getSelectOnChangeEndHTML() %>

		<%=Bean.getSelectOnChangeBeginHTML("task_state", "../crm/cards/questionnaire_importspecs.jsp?id=" + id + "&task_page=1", Bean.card_taskXML.getfieldTransl("cd_card_oper_state", false)) %>
			<%= Bean.getClubCardOperationStateOptions(task_state, true) %>
		<%=Bean.getSelectOnChangeEndHTML() %>
	  	</tr>
	</table>
		<%=quest.getClubCardsTasksHTML(task_find, task_type, task_state, l_task_page_beg, l_task_page_end) %>
	<%}

	if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CARDS_QUESTIONNAIRE_IMPORT_MESSAGES")) {%>
	<table <%=Bean.getTableBottomFilter() %>>
	  	<tr>
		<%= Bean.getFindHTML("message_find", message_find, "../crm/cards/questionnaire_importspecs.jsp?id=" + id + "&message_page=1") %>

		</tr>
	</table>
		<%=quest.getMessagesHTML(message_find, "", l_message_page_beg, l_message_page_end) %>
	<%}

	if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CARDS_QUESTIONNAIRE_IMPORT_POSTINGS")) {%>
	<table <%=Bean.getTableBottomFilter() %>>
	  	<tr>
		<%= Bean.getFindHTML("posting_find", posting_find, "../crm/cards/questionnaire_importspecs.jsp?id=" + id + "&posting_page=1") %>

		</tr>
	</table>
		<%=quest.getPostingsHTML(posting_find, "", l_posting_page_beg, l_posting_page_end) %>
	<%}

	if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CARDS_QUESTIONNAIRE_IMPORT_REPORTS")) { 
		bcReports report = new bcReports(Bean.getReportFormat());
		
		%>
   <table <%= Bean.getTableBottomFilter() %>>
		<tr>
		<%= Bean.getFindHTML("find_string", find_string, "../crm/cards/questionnaire_importspecs.jsp?id=" + id + "&find_string=1") %>

		</tr>
	</table>
		<%= report.getQuestionnaireImportReportHTML("QUEST", id,  id_report, find_string, l_report_page_beg, l_report_page_end) %>
	<br>
	<% if (!(id_report==null || "".equalsIgnoreCase(id_report))) { %>
		<table <%=Bean.getTableMenuParam() %>>
			<tr>
			<td>&nbsp;</td>
		    <!-- Вывод страниц -->
			<%= Bean.getPagesHTML(pageFormName + tagReportDetail, "../crm/cards/questionnaire_importspecs.jsp?id=" + id + "&id_report="+id_report+"&tab="+Bean.currentMenu.getTabID("CARDS_QUESTIONNAIRE_IMPORT_REPORTS")+"&", "report_det_page") %>
		</tr>
		</table>
		<%= report.getQuestionnaireImportReportDetailHTML(id_report, l_report_det_page_beg, l_report_det_page_end) %>
	<% } %>

	<% } 

} %>
</div></div>

</body>
</html>
