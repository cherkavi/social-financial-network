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

String pageFormName = "CARDS_QUESTIONNAIRE_PACK";

Bean.setJspPageForTabName(pageFormName);

String tagQuest = "_QUEST";
String tagQuestProfile = "_PACKPROFILE";
String tagReports = "_REPORTS";
String tagReportDetail = "_REPORT_DETAIL";
String tagReportFind = "_REPORT_FIND_DETAIL";

String id = Bean.getDecodeParam(parameters.get("id"));

String state_pack_quest = Bean.getDecodeParam(parameters.get("state_pack_quest"));
if (state_pack_quest==null) {
	state_pack_quest = Bean.filtersHmGetValue(pageFormName + state_pack_quest);;
} else {
	Bean.filtersHmSetValue(pageFormName + tagQuestProfile, state_pack_quest);
}

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

String report_find = Bean.getDecodeParam(parameters.get("find_string"));

if (report_find==null || "".equalsIgnoreCase(report_find)) {
	report_find = "";
	Bean.filtersHmSetValue(pageFormName + tagReportFind, report_find);
} else {
	if ("CURRENT".equalsIgnoreCase(report_find)) {
		report_find = Bean.filtersHmGetValue(pageFormName + tagReportFind);
	} else {
		Bean.filtersHmSetValue(pageFormName + tagReportFind, report_find);
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


	bcQuestionnairePackObject pack = new bcQuestionnairePackObject(id);;
	
	//Обрабатываем номера страниц
	String l_quest_page = Bean.getDecodeParam(parameters.get("quest_page"));
	Bean.pageCheck(pageFormName + tagQuest, l_quest_page);
	String l_quest_page_beg = Bean.getFirstRowNumber(pageFormName + tagQuest);
	String l_quest_page_end = Bean.getLastRowNumber(pageFormName + tagQuest);
	%>

	<table <%=Bean.getTableMenuParam() %>>
		<tr>
			<%= Bean.getPageHeader() %>

			<% if (Bean.currentMenu.isCurrentTabAndEditPermitted("CARDS_QUESTIONNAIRE_PACK_INFO")) { %>
				<% if (!("-1".equalsIgnoreCase(id)) && "OPENED".equalsIgnoreCase(pack.getValue("STATE_PACK"))) {%> 

				    <%= Bean.getMenuButton("IMPORT", "../crm/cards/questionnaire_packupdate.jsp?type=general&id=" + id + "&action=import&process=yes", "", "") %>
				<%} %>
		
			    <%= Bean.getMenuButton("ADD", "../crm/cards/questionnaire_packupdate.jsp?type=general&id=" + id + "&action=add2&process=no", "", "") %>

				<% if (!("-1".equalsIgnoreCase(id))) {%> 
				    <%= Bean.getMenuButton("DELETE", "../crm/cards/questionnaire_packupdate.jsp?type=general&id=" + id + "&action=remove&process=yes", Bean.buttonXML.getfieldTransl("delete", false), id) %>
				<%} %>
			<%  } %>

			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CARDS_QUESTIONNAIRE_PACK_QUEST")) { %>
				<% if (!("-1".equalsIgnoreCase(id)) && "OPENED".equalsIgnoreCase(pack.getValue("STATE_PACK"))) {%> 

				    <%= Bean.getMenuButton("IMPORT", "../crm/cards/questionnaire_packupdate.jsp?type=general&id=" + id + "&action=import&process=yes", "", "") %>
				<%} %>
		
			    <!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagQuest, "../crm/cards/questionnaire_packspecs.jsp?id=" + id + "&tab="+Bean.currentMenu.getTabID("CARDS_QUESTIONNAIRE_PACK_QUEST")+"&", "quest_page") %>
			<%  } %>

			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CARDS_QUESTIONNAIRE_PACK_REPORTS")) { %>
		
				<% if (id_report==null || "".equalsIgnoreCase(id_report)) { %>
				    <!-- Вывод страниц -->
					<%= Bean.getPagesHTML(pageFormName + tagReports, "../crm/cards/questionnaire_packspecs.jsp?id=" + id + "&tab="+Bean.currentMenu.getTabID("CARDS_QUESTIONNAIRE_PACK_REPORTS")+"&", "report_page") %>
				<% } %>
			<% } %>
		</tr>
	</table>
	<table <%=Bean.getTableMenuParam() %>>
		<%= Bean.getDetailCaption(id) %>
		<tr>
			<td>
				<!-- Выводим перечень закладок -->
				<%= Bean.currentMenu.getFormTabScheeds(tab, "../crm/cards/questionnaire_packspecs.jsp?id=" + id) %>
			</td>	
		</tr>
	</table>

</div>
<div id="div_data">
<div id="div_data_detail">
<%
		if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CARDS_QUESTIONNAIRE_PACK_INFO")) {

			boolean hasEditPermission = false;
			if (Bean.currentMenu.isCurrentTabAndEditPermitted("CARDS_QUESTIONNAIRE_PACK_INFO")) {
				hasEditPermission = true;
			}

			String l_id_quest_pack = id;
			String l_date_reception_pack = "";
			String l_state_pack = "";
			String l_state_pack_tsl = "";
			String l_import_state_pack = "";
			String l_import_state_pack_tsl = "";
			String l_id_jur_prs_where_card_sold = "";
			String l_sname_jur_prs_where_card_sold = "";
			String l_id_serv_place_where_card_sold = "";
			String l_name_serv_plce_where_card_sold = "";
			String l_expected_quest_quantity = "";
			String l_id_jur_prs_who_has_sold_card = "";
			String l_sname_jur_pr_who_has_sold_card = "";
			String l_real_quest_quantity = "";
			String l_imported_quest_quantity = "";
			String l_id_club = "";
			String l_id_club_event = "";
			String l_creation_date = "";
			String l_created_by = "";
			String l_last_update_date = "";
			String l_last_update_by = "";
			String l_note_quest_pack = "";
			if (!("-1".equalsIgnoreCase(id))) {
				l_id_quest_pack = pack.getValue("ID_QUEST_PACK");
				l_date_reception_pack = pack.getValue("DATE_RECEPTION_PACK_FRMT");
				l_state_pack = pack.getValue("STATE_PACK");
				l_state_pack_tsl = pack.getValue("STATE_PACK_TSL");
				l_import_state_pack = pack.getValue("IMPORT_STATE_PACK");
				l_import_state_pack_tsl = pack.getValue("IMPORT_STATE_PACK_TSL");
				l_id_jur_prs_where_card_sold = pack.getValue("ID_JUR_PRS_WHERE_CARD_SOLD");
				l_sname_jur_prs_where_card_sold = pack.getValue("SNAME_JUR_PRS_WHERE_CARD_SOLD");
				l_id_serv_place_where_card_sold = pack.getValue("ID_SERV_PLACE_WHERE_CARD_SOLD");
				l_name_serv_plce_where_card_sold = pack.getValue("NAME_SERV_PLCE_WHERE_CARD_SOLD");
				l_expected_quest_quantity = pack.getValue("EXPECTED_QUEST_QUANTITY");
				l_id_jur_prs_who_has_sold_card = pack.getValue("ID_JUR_PRS_WHO_HAS_SOLD_CARD");
				l_sname_jur_pr_who_has_sold_card = pack.getValue("SNAME_JUR_PR_WHO_HAS_SOLD_CARD");
				l_real_quest_quantity = pack.getValue("REAL_QUEST_QUANTITY");
				l_imported_quest_quantity = pack.getValue("IMPORTED_QUEST_QUANTITY");
				l_creation_date = pack.getValue(Bean.getCreationDateFieldName());
				l_created_by = pack.getValue("CREATED_BY");
				l_last_update_date = pack.getValue(Bean.getLastUpdateDateFieldName());
				l_last_update_by = pack.getValue("LAST_UPDATE_BY");
				l_id_club = pack.getValue("ID_CLUB");
				l_note_quest_pack = pack.getValue("NOTE_QUEST_PACK");
				l_id_club_event = pack.getValue("ID_CLUB_EVENT");
			} else {
				hasEditPermission = false;
			}
			
			if (hasEditPermission) {
	%>

			<script>
			var formData = new Array (
				new Array ('date_reception_pack', 'varchar2', 1),
				new Array ('name_club', 'varchar2', 1),
				new Array ('name_jur_prs_who_has_sold_card', 'varchar2', 1),
				new Array ('name_jur_prs_where_card_sold', 'varchar2', 1),
				new Array ('state_pack', 'varchar2', 1),
				new Array ('expected_quest_quantity', 'varchar2', 1)
			);
			function myValidateForm() {
				return validateForm(formData);
			}
		</script>
	        <form action="../crm/cards/questionnaire_packupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">
	        <input type="hidden" name="action" value="edit">
	        <input type="hidden" name="process" value="yes">
	        <input type="hidden" name="type" value="general">
	        <input type="hidden" name="id" value="<%=id %>">
		<table <%=Bean.getTableDetailParam() %>>
			<tr>
				<td><%= Bean.questionnaireXML.getfieldTransl("id_quest_pack", false) %></td><td><input type="text" name="id_quest_pack" size="30" value="<%= l_id_quest_pack %>" readonly="readonly" class="inputfield-ro"></td>
	 		    <td><%= Bean.clubXML.getfieldTransl("club", false) %>
					<%= Bean.getGoToClubLink(l_id_club) %>
				</td><td><input type="text" name="name_club" size="30" value="<%= Bean.getClubShortName(l_id_club) %>" readonly="readonly" class="inputfield-ro"></td>
			</tr>
			<tr>
			    <td><%= Bean.questionnaireXML.getfieldTransl("date_reception_pack", true) %></td><td><%=Bean.getCalendarInputField("date_reception_pack", l_date_reception_pack, "10") %></td>
	 		    <td><%= Bean.club_actionXML.getfieldTransl("name_club_event", false) %>
					<%= Bean.getGoToClubEventLink(l_id_club_event) %>
				</td>
			  	<td>
					<%=Bean.getWindowFindClubAction("club_event", l_id_club_event, Bean.getClubActionName(l_id_club_event), "50") %>
		  		</td>
			</tr>
			<tr>
				<td><%= Bean.questionnaireXML.getfieldTransl("state_pack", false) %></td><td><select name="state_pack" class="inputfield"><%= Bean.getMeaningFromLookupNameOptions("NAT_PRS_INT_PACK_STATE", l_state_pack, false) %></select></td>
				<td><%= Bean.questionnaireXML.getfieldTransl("sname_jur_pr_who_has_sold_card", true) %><%= Bean.getGoToJurPrsHyperLink(l_id_jur_prs_who_has_sold_card) %></td>
				<td>
					<%=Bean.getWindowFindJurPrs("jur_prs_who_has_sold_card", l_id_jur_prs_who_has_sold_card, l_sname_jur_pr_who_has_sold_card, "ALL", "50") %>
				</td>			
			</tr>
			<tr>
				<td><%= Bean.questionnaireXML.getfieldTransl("import_state_pack", false) %></td><td><input type="text" name="import_state_pack" size="30" value="<%= l_import_state_pack_tsl %>" readonly="readonly" class="inputfield-ro"></td>
				<td><%= Bean.questionnaireXML.getfieldTransl("sname_jur_prs_where_card_sold", true) %><%= Bean.getGoToJurPrsHyperLink(l_id_jur_prs_where_card_sold) %></td>
				<td>
					<%=Bean.getWindowFindJurPrs("jur_prs_where_card_sold", l_id_jur_prs_where_card_sold, l_sname_jur_prs_where_card_sold, "ALL", "50") %>
				</td>			
			</tr>
			<tr>
				<td><%= Bean.questionnaireXML.getfieldTransl("expected_quest_quantity", true) %></td><td><input type="text" name="expected_quest_quantity" size="15" value="<%= l_expected_quest_quantity %>" class="inputfield"></td>
				<td><%= Bean.questionnaireXML.getfieldTransl("name_serv_plce_where_card_sold", false) %>
					<%= Bean.getGoToJurPrsHyperLink(l_id_serv_place_where_card_sold) %></td>
				<td>
					<%=Bean.getWindowFindServicePlace("service_place", l_id_serv_place_where_card_sold, "", "'+document.getElementById('id_jur_prs_where_card_sold').value+'", "", "50") %>
				</td>			
			</tr>
			<tr>
				<td><%= Bean.questionnaireXML.getfieldTransl("real_quest_quantity", false) %></td><td><input type="text" name="real_quest_quantity" size="15" value="<%= l_real_quest_quantity %>" readonly="readonly" class="inputfield-ro"></td>
				<td valign="top" rowspan="3"><%= Bean.questionnaireXML.getfieldTransl("note_quest_pack", false) %></td><td valign="top" rowspan="3"><textarea name="note_quest_pack" cols="60" rows="3" class="inputfield"><%= l_note_quest_pack %></textarea></td>
			</tr>
			<tr>
				<td><%= Bean.questionnaireXML.getfieldTransl("imported_quest_quantity", false) %></td><td><input type="text" name="imported_quest_quantity" size="15" value="<%= l_imported_quest_quantity %>" readonly="readonly" class="inputfield-ro"></td>
			</tr>
			<tr>
				<td colspan="2">&nbsp;</td>
			</tr>
			<%=	Bean.getCreationAndMoficationRecordFields(
						l_creation_date,
						l_created_by,
						l_last_update_date,
						l_last_update_by
				) %>
			<tr>
				<td colspan="6" align="center">
					<%=Bean.getSubmitButtonAjax("../crm/cards/questionnaire_packupdate.jsp") %>
					<%=Bean.getResetButton() %>
					<%=Bean.getGoBackButton("../crm/cards/questionnaire_pack.jsp") %>
				</td>
			</tr>
		</table>
		</form>
		<!-- Скрипт для втавки меню вибору дати -->
		<%= Bean.getCalendarScript("date_reception_pack", false) %>

		<%
			} else if (!hasEditPermission) {
				%>

			<table <%=Bean.getTableDetailParam() %>>
				<tr>
					<td><%= Bean.questionnaireXML.getfieldTransl("id_quest_pack", false) %></td><td><input type="text" name="id_quest_pack" size="30" value="<%= l_id_quest_pack %>" readonly="readonly" class="inputfield-ro"></td>
					<td><%= Bean.clubXML.getfieldTransl("club", false) %>
						<%= Bean.getGoToClubLink(l_id_club) %>
					</td><td><input type="text" name="name_club" size="30" value="<%= Bean.getClubShortName(l_id_club) %>" readonly="readonly" class="inputfield-ro"></td>
				</tr>
				<tr>
				    <td><%= Bean.questionnaireXML.getfieldTransl("date_reception_pack", false) %></td><td><input type="text" name="date_reception_pack" size="15" value="<%= l_date_reception_pack %>" readonly="readonly" class="inputfield-ro"></td>
		 		    <td><%= Bean.club_actionXML.getfieldTransl("name_club_event", false) %>
						<%= Bean.getGoToClubEventLink(l_id_club_event) %>
					</td>
				  	<td><input type="text" id="name_club_event" name="name_club_event" size="50" value="<%= Bean.getClubActionName(l_id_club_event) %>" readonly="readonly" class="inputfield-ro"></td>
				</tr>
				<tr>
					<td><%= Bean.questionnaireXML.getfieldTransl("state_pack", false) %></td><td><input type="text" name="state_pack" size="30" value="<%= l_state_pack_tsl %>" readonly="readonly" class="inputfield-ro"></td>
					<td><%= Bean.questionnaireXML.getfieldTransl("sname_jur_pr_who_has_sold_card", false) %>
						<%= Bean.getGoToJurPrsHyperLink(l_id_jur_prs_who_has_sold_card) %>
					</td><td><input type="text" id="sname_jur_prs_where_card_sold" name="name_jur_prs" size="50" value="<%= l_sname_jur_pr_who_has_sold_card %>" readonly="readonly" class="inputfield-ro"></td>			
				</tr>
				<tr>
					<td><%= Bean.questionnaireXML.getfieldTransl("import_state_pack", false) %></td><td><input type="text" name="import_state_pack" size="30" value="<%= l_import_state_pack_tsl %>" readonly="readonly" class="inputfield-ro"></td>
					<td><%= Bean.questionnaireXML.getfieldTransl("sname_jur_prs_where_card_sold", false) %>
						<%= Bean.getGoToJurPrsHyperLink(l_id_jur_prs_where_card_sold) %>
					</td><td><input type="text" id="sname_jur_prs_where_card_sold" name="name_jur_prs" size="50" value="<%= l_sname_jur_prs_where_card_sold %>" readonly="readonly" class="inputfield-ro"></td>			
				</tr>
				<tr>
					<td><%= Bean.questionnaireXML.getfieldTransl("expected_quest_quantity", false) %></td><td><input type="text" name="expected_quest_quantity" size="15" value="<%= l_expected_quest_quantity %>" readonly="readonly" class="inputfield-ro"></td>
					<td><%= Bean.questionnaireXML.getfieldTransl("name_serv_plce_where_card_sold", false) %>
						<%= Bean.getGoToJurPrsHyperLink(l_id_serv_place_where_card_sold) %>
					</td><td><input type="text" id="name_service_place" name="name_serv_plce_where_card_sold" size="50" value="<%= l_name_serv_plce_where_card_sold %>" readonly="readonly" class="inputfield-ro"></td>			
				</tr>
				<tr>
					<td><%= Bean.questionnaireXML.getfieldTransl("real_quest_quantity", false) %></td><td><input type="text" name="real_quest_quantity" size="15" value="<%= l_real_quest_quantity %>" readonly="readonly" class="inputfield-ro"></td>
					<td valign="top" rowspan="3"><%= Bean.questionnaireXML.getfieldTransl("note_quest_pack", false) %></td><td valign="top" rowspan="3"><textarea name="note_quest_pack" cols="60" rows="3" class="inputfield"><%= l_note_quest_pack %></textarea></td>
				</tr>
				<tr>
					<td><%= Bean.questionnaireXML.getfieldTransl("imported_quest_quantity", false) %></td><td><input type="text" name="imported_quest_quantity" size="15" value="<%= l_imported_quest_quantity %>" readonly="readonly" class="inputfield-ro"></td>
				</tr>
				<tr>
					<td colspan="2">&nbsp;</td>
				</tr>
				<tr>
				    <td colspan="4" class="top_line"><b><%=Bean.commonXML.getfieldTransl("h_record_param", false)%></b></td>
				</tr>
				<tr>
					<td><%=Bean.commonXML.getfieldTransl("creation_date", false)%></td> <td><input type="text" name="creation_date" size="20" value="<%=l_creation_date%>" readonly="readonly" class="inputfield-ro"> </td>
					<td><%=Bean.commonXML.getfieldTransl("last_update_date",false)%></td> <td><input type="text" name="last_update_date" size="20" value="<%=l_last_update_date%>" readonly="readonly" class="inputfield-ro"> </td>
				</tr>
				<tr>
					<td><%=Bean.commonXML.getfieldTransl("created_by", false)%></td> <td><input type="text" name="created_by" size="20" value="<%=Bean.getSystemUserName(l_created_by)%>" readonly="readonly" class="inputfield-ro"> </td>
					<td><%=Bean.commonXML.getfieldTransl("last_update_by", false)%></td> <td><input type="text" name="last_update_by" size="20" value="<%=Bean.getSystemUserName(l_last_update_by)%>" readonly="readonly" class="inputfield-ro"> </td>
				</tr>
				<tr>
					<td colspan="6" align="center">
						<%=Bean.getGoBackButton("../crm/cards/questionnaire_pack.jsp") %>
					</td>
				</tr>
			</table>

			<%
			
			}
}
if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CARDS_QUESTIONNAIRE_PACK_QUEST")) {%>
<script type="text/javascript">
 	function CheckCB(Element) {
		myCheck = true;

		thisCheckBoxes = document.getElementsByTagName('input');
		for (i = 1; i < thisCheckBoxes.length; i++) { 
			myName = thisCheckBoxes[i].name;
			if (myName.substr(0,8) == 'id_quest'){
				myCheck = myCheck && thisCheckBoxes[i].checked;
			}
		}
		if (document.getElementById('mainCheck')) {
			document.getElementById('mainCheck').checked = myCheck;
		}
	}
	function CheckAll(Element,Name) {
		thisCheckBoxes = document.getElementsByTagName('input');
		for (i = 1; i < thisCheckBoxes.length; i++) { 
			myName = thisCheckBoxes[i].name;
			
			if (myName.substr(0,8) == Name){
					thisCheckBoxes[i].checked = document.getElementById('mainCheck').checked;
			}
		}
	}

	function CheckSelect(form)  {
    	for (i = 0; i < form.elements.length; i++) {
       		var item = form.elements[i];
       		if (item.name.substr(0,8) == 'id_quest'){
       			if (item.checked)  {
       				return true;
       			}
       		}
       	}
   		alert("<%= Bean.questionnaireXML.getfieldTransl("t_quest_not_selected", false) %>");
    	return false;
	 	}

	function changePackParam(id, title){
		document.getElementById('id_quest_pack').value = id;
		document.getElementById('name_quest_pack').value = title;
		document.getElementById('name_quest_pack').className = "inputfield_modified";
	}
</script>
		<% if ("-1".equalsIgnoreCase(id) || "OPENED".equalsIgnoreCase(pack.getValue("STATE_PACK"))) { %>
	        <form action="../crm/cards/questionnaire_packupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return CheckSelect(this);">
	        <input type="hidden" name="action" value="set_pack">
	        <input type="hidden" name="process" value="yes">
	        <input type="hidden" name="type" value="general">
	        <input type="hidden" name="id" value="<%=id %>">
		<% } %>
			<table class="tablebottom"><tbody>
			<tr>
				<% if ("-1".equalsIgnoreCase(id) || "OPENED".equalsIgnoreCase(pack.getValue("STATE_PACK"))) { %>
				<th colspan=7 align="left">
				<%= Bean.questionnaireXML.getfieldTransl("t_move_to_pack", false) %>: 
					<%=Bean.getWindowFindQuestionnairePack("quest_pack", "", "20") %>
					<button class="button" onclick=" if (CheckSelect(document.getElementById('updateForm'))) { ajaxpage('../crm/cards/questionnaire_packupdate.jsp?' + mySubmitForm('updateForm'),'div_main')} " type="button"><%=Bean.buttonXML.getfieldTransl("submit", false) %></button>
				</th>
				<% } else { %>
				<th colspan=6 align="left">
					<font color=red><b><%= Bean.questionnaireXML.getfieldTransl("t_cannot_move_quest", false) %></b></font>
				</th>
				<% } %>
		
				<th colspan="4" align="right">
                    <%= Bean.questionnaireXML.getfieldTransl("type_quest", false) %>:
				  	<select onchange="ajaxpage('../crm/cards/questionnaire_packspecs.jsp?quest_page=1&id=<%=id %>&state_pack_quest='+this.value, 'div_main')" name="state_pack_quest" id="state_pack_quest" class="inputfield">
						<%= Bean.getMeaningFromLookupNameOptions("NAT_PRS_INT_QUEST_STATE", state_pack_quest, true) %>
					</select>
				</th>
			</tr>
			<% String myStatePack = pack.getValue("STATE_PACK");
			   if ("-1".equalsIgnoreCase(id)) {
				   myStatePack = "OPENED";
			   }
			%>
			</tbody>
			</table>			
			<table class="tablebottom"><tbody>
			<%=pack.getQuestionnaireListHTML("", state_pack_quest, myStatePack, l_quest_page_beg, l_quest_page_end)%>
			</tbody></table>
			</form> 
		<%
}
		

if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CARDS_QUESTIONNAIRE_PACK_REPORTS")) { 
		bcReports report = new bcReports(Bean.getReportFormat());
		
		%>
   <table <%= Bean.getTableBottomFilter() %>>
		<tr>
		<%= Bean.getFindHTML("report_find", report_find, "../crm/cards/questionnaire_packspecs.jsp?id=" + id + "&report_page=1") %>

		</tr>
	</table>
		<%= report.getQuestionnaireImportReportHTML("PACK", id,  id_report, report_find, l_report_page_beg, l_report_page_end) %>
		<br>
		<% if (!(id_report==null || "".equalsIgnoreCase(id_report))) { %>
		<table <%=Bean.getTableMenuParam() %>>
			<tr>
			<td>&nbsp;</td>
		    <!-- Вывод страниц -->
			<%= Bean.getPagesHTML(pageFormName + tagReportDetail, "../crm/cards/questionnaire_packspecs.jsp?id=" + id + "&id_report="+id_report+"&tab="+Bean.currentMenu.getTabID("CARDS_QUESTIONNAIRE_PACK_REPORTS")+"&", "report_det_page") %>
		</tr>
		</table>
			<%= report.getQuestionnaireImportReportDetailHTML(id_report, l_report_det_page_beg, l_report_det_page_end) %>
		<% } %>

	<%  }

} %>
</div></div>
</body>
</html>
