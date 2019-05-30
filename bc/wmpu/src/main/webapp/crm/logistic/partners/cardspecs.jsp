<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%= Bean.getLogOutScript(request) %>

<%@page import="java.util.HashMap"%>
<%@page import="bc.objects.bcLGObject"%>
<html>
<head>
	<%= Bean.getMetaContent() %>
	<%= Bean.getBottomFrameCSS() %>
</head>
<body topmargin="0">
<div id="div_tabsheet">

<%

HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String pageFormName = "LOGISTIC_PARTNERS_CARDS";

Bean.setJspPageForTabName(pageFormName);

String tagRanges = "_RANGES";
String tagRangeFind = "_RANGE_FIND";
String tagCards = "_CARDS";
String tagCardFind = "_CARD_FIND";
String tagDoc = "_DOC";
String tagDocFind = "_DOC_FIND";
String tagDocType = "_DOC_TYPE";
String tagDocState = "_DOC_STATE";

String id = Bean.getDecodeParam(parameters.get("id"));
String filtr = Bean.getDecodeParam(parameters.get("filtr"));
String	tab = Bean.getDecodeParam(parameters.get("tab"));
if (tab==null || "".equalsIgnoreCase(tab)) { 
	tab = Bean.tabsHmGetValue(pageFormName);
}


if (id==null || "".equals(id)) {
%>

	<%=Bean.getIDNotFoundMessage() %>

<% } else {
	Bean.tabsHmSetValue(pageFormName, tab);
	bcLGObject lgCard = new bcLGObject("BON_CARD", id);

	String l_range_page = Bean.getDecodeParam(parameters.get("range_page"));
	//Обрабатываем номера страниц
	Bean.pageCheck(pageFormName + tagRanges, l_range_page);
	String l_range_page_beg = Bean.getFirstRowNumber(pageFormName + tagRanges);
	String l_range_page_end = Bean.getLastRowNumber(pageFormName + tagRanges);

	String range_find 	= Bean.getDecodeParam(parameters.get("range_find"));
	range_find 	= Bean.checkFindString(pageFormName + tagRangeFind, range_find, l_range_page);
	
	String l_doc_page = Bean.getDecodeParam(parameters.get("doc_page"));
	Bean.pageCheck(pageFormName + tagDoc, l_doc_page);
	String l_doc_page_beg = Bean.getFirstRowNumber(pageFormName + tagDoc);
	String l_doc_page_end = Bean.getLastRowNumber(pageFormName + tagDoc);

	String doc_find 	= Bean.getDecodeParam(parameters.get("doc_find"));
	doc_find 	= Bean.checkFindString(pageFormName + tagDocFind, doc_find, l_doc_page);

	String doc_type	= Bean.getDecodeParam(parameters.get("doc_type"));
	doc_type		= Bean.checkFindString(pageFormName + tagDocType, doc_type, l_doc_page);

	String doc_state	= Bean.getDecodeParam(parameters.get("doc_state"));
	doc_state		= Bean.checkFindString(pageFormName + tagDocState, doc_state, l_doc_page);
	
	String l_cards = Bean.getDecodeParam(parameters.get("cards"));
	//Обрабатываем номера страниц
	Bean.pageCheck(pageFormName + tagCards, l_cards);
	String l_cards_beg = Bean.getFirstRowNumber(pageFormName + tagCards);
	String l_cards_end = Bean.getLastRowNumber(pageFormName + tagCards);

	String card_find 	= Bean.getDecodeParam(parameters.get("card_find"));
	card_find 	= Bean.checkFindString(pageFormName + tagCardFind, card_find, l_cards);

%>

	<table <%=Bean.getTableMenuParam() %>>
		<tr>
			<%= Bean.getPageHeader() %>
			<% if (Bean.currentMenu.isCurrentTabAndEditPermitted("LOGISTIC_PARTNERS_CARDS_INFO")) {%>
				<%= Bean.getMenuButton("ADD", "../crm/logistic/partners/cardupdate.jsp?id=" + id + "&type=general&action=add2&process=no", "", "") %>
				<%= Bean.getMenuButton("DELETE", "../crm/logistic/partners/cardupdate.jsp?id=" + id + "&type=general&action=remove&process=yes", Bean.logisticXML.getfieldTransl("h_action_bon_card_delete", false), lgCard.getValue("ID_LG_RECORD") + " - " +  lgCard.getValue("OPERATION_NAME")) %>
			<% } %>

			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("LOGISTIC_PARTNERS_CARDS_DOC")) { %>
				<% if (Bean.currentMenu.isCurrentTabAndEditPermitted("LOGISTIC_PARTNERS_CARDS_DOC")) { %>
					<%= Bean.getMenuButton("ADD", "../crm/logistic/lgdocupdate.jsp?lg_type=BON_CARD&type=doc&id="+ id + "&action=add&process=no", "", "") %>
				<% } %>
			    <!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagDoc, "../crm/logistic/partners/cardspecs.jsp?id=" + id + "&tab="+Bean.currentMenu.getTabID("LOGISTIC_PARTNERS_CARDS_DOC") + "&", "doc_page") %>
			<% } %>

			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("LOGISTIC_PARTNERS_CARDS_RANGE")) { %>
				<% if (Bean.currentMenu.isCurrentTabAndEditPermitted("LOGISTIC_PARTNERS_CARDS_RANGE")) { %>
					<%= Bean.getMenuButton("ADD", "../crm/logistic/partners/cardupdate.jsp?type=range&id="+ id + "&action=add&process=no", "", "") %>
				<% } %>
			    <!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagRanges, "../crm/logistic/partners/cardspecs.jsp?id=" + id + "&tab="+Bean.currentMenu.getTabID("LOGISTIC_PARTNERS_CARDS_RANGE")+ "&", "range_page") %>
			<% } %>

			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("LOGISTIC_PARTNERS_CARDS_CARDS")) {%>
	    		<!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagCards, "../crm/logistic/partners/cardspecs.jsp?id=" + id + "&tab="+Bean.currentMenu.getTabID("LOGISTIC_PARTNERS_CARDS_CARDS") + "&", "cards") %>
			<% } %>
		</tr>
	</table>
	<table <%=Bean.getTableMenuParam() %>>
		<%= Bean.getDetailCaption(lgCard.getValue("ID_LG_RECORD") + " - " + lgCard.getValue("OPERATION_NAME")) %>
		<tr>
			<td>
				<!-- Выводим перечень закладок -->
				<%= Bean.currentMenu.getFormTabScheeds(tab, "../crm/logistic/partners/cardspecs.jsp?id=" + id) %>
			</td>
		</tr>
	</table>
</div>
<div id="div_data">
<div id="div_data_detail">
<%
if (Bean.currentMenu.isCurrentTabAndAccessPermitted("LOGISTIC_PARTNERS_CARDS_INFO")) {
%>
	
		<script language="JavaScript">
			var formData = new Array (
				new Array ('action_date', 'varchar2', 1)
			);
			</script>

		<% if (Bean.currentMenu.isCurrentTabAndEditPermitted("LOGISTIC_PARTNERS_CARDS_INFO")) { %>
		  <form action="../crm/logistic/partners/cardupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onsubmit="return validateForm(formData);">
			<input type="hidden" name="action" value="edit">
	    	<input type="hidden" name="process" value="yes">
	    	<input type="hidden" name="type" value="general">
	    	<input type="hidden" name="id" value="<%= id %>">
		<%} %>

		<table <%=Bean.getTableDetailParam() %>>

		<% if (Bean.currentMenu.isCurrentTabAndEditPermitted("LOGISTIC_PARTNERS_CARDS_INFO")) {%>
		<tr>
			<td><%= Bean.logisticXML.getfieldTransl("id_lg_record", false) %></td><td><input type="text" name="id_lg_record" size="20" value="<%= lgCard.getValue("ID_LG_RECORD") %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.clubXML.getfieldTransl("club", false) %>
				<%= Bean.getGoToClubLink(lgCard.getValue("ID_CLUB")) %>
			</td><td><input type="text" name="name_club" size="50" value="<%= Bean.getClubShortName(lgCard.getValue("ID_CLUB")) %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td><%= Bean.logisticXML.getfieldTransl("name_lg_type", false) %></td><td><input type="text" name="name_lg_type" size="60" value="<%= lgCard.getValue("NAME_LG_TYPE") %>" readonly="readonly" class="inputfield-ro"></td>
			<td class="top_line"><%= Bean.logisticXML.getfieldTransl("sname_jur_prs_receiver", false) %>
			<%=Bean.getGoToJurPrsHyperLink(lgCard.getValue("ID_JUR_PRS_RECEIVER")) %>
			</td>
			<td class="top_line">
				<%=Bean.getWindowFindJurPrs("jur_prs_receiver", lgCard.getValue("ID_JUR_PRS_RECEIVER"), lgCard.getValue("SNAME_JUR_PRS_RECEIVER"), "ALL", "40") %>
			</td>			
		</tr>
		<tr>
			<td><%= Bean.logisticXML.getfieldTransl("action_date_frmt", true) %></td><td><%=Bean.getCalendarInputField("action_date", lgCard.getValue("ACTION_DATE_FRMT"), "10") %></td>
			<td><%= Bean.logisticXML.getfieldTransl("id_service_place_receiver", false) %>
				<%= Bean.getGoToJurPrsHyperLink(lgCard.getValue("ID_SERVICE_PLACE_RECEIVER")) %>
			</td>
			<td>
				<%=Bean.getWindowFindServicePlace("service_place_receiver", lgCard.getValue("ID_SERVICE_PLACE_RECEIVER"), "", "'+document.getElementById('id_jur_prs_receiver').value+'", "", "40") %>
			</td>			
		</tr>
		<tr>
			<td rowspan="3"><%=  Bean.logisticXML.getfieldTransl("operation_desc", false) %></td><td rowspan="3"><textarea name="operation_desc" cols="60" rows="6" class="inputfield"><%= lgCard.getValue("OPERATION_DESC") %></textarea></td>
			<td><%= Bean.logisticXML.getfieldTransl("id_contact_prs_receiver", false) %>
				<%= Bean.getGoToContactPersonLink(lgCard.getValue("ID_CONTACT_PRS_RECEIVER")) %>
			</td>
			<td>
				<%=Bean.getWindowContactPersons("contact_prs_receiver", lgCard.getValue("ID_CONTACT_PRS_RECEIVER"), "'+document.getElementById('id_jur_prs_receiver').value+'", "'+document.getElementById('id_service_place_receiver').value+'", "40") %>
			</td>			
		</tr>
		<tr>
			<td><%=  Bean.logisticXML.getfieldTransl("desc_receiver", false) %></td><td><textarea name="desc_receiver" cols="60" rows="3" class="inputfield"><%= lgCard.getValue("DESC_RECEIVER") %></textarea></td>
		</tr>
		<tr>
			<td class="top_line"><%= Bean.logisticXML.getfieldTransl("sname_jur_prs_sender", false) %>
			<%=Bean.getGoToJurPrsHyperLink(lgCard.getValue("ID_JUR_PRS_SENDER")) %>
			</td>
			<td class="top_line">
				<%=Bean.getWindowFindJurPrs("jur_prs_sender", lgCard.getValue("ID_JUR_PRS_SENDER"), lgCard.getValue("SNAME_JUR_PRS_SENDER"), "ALL", "40") %>
			</td>			
		</tr>
		<tr>
			<td><%= Bean.logisticXML.getfieldTransl("bon_cards_count", false) %></td><td><input type="text" name="object_count" size="20" value="<%= lgCard.getValue("BON_CARDS_COUNT") %>" class="inputfield"></td>
			<td><%= Bean.logisticXML.getfieldTransl("id_service_place_sender", false) %>
				<%= Bean.getGoToJurPrsHyperLink(lgCard.getValue("ID_SERVICE_PLACE_SENDER")) %>
			</td>
			<td>
				<%=Bean.getWindowFindServicePlace("service_place_sender", lgCard.getValue("ID_SERVICE_PLACE_SENDER"), "", "'+document.getElementById('id_jur_prs_sender').value+'", "", "40") %>
			</td>			
		</tr>
		<tr>
			<td colspan="2">&nbsp;</td>
			<td><%= Bean.logisticXML.getfieldTransl("id_contact_prs_sender", false) %>
				<%= Bean.getGoToContactPersonLink(lgCard.getValue("ID_CONTACT_PRS_SENDER")) %>
			</td>
			<td>
				<%=Bean.getWindowContactPersons("contact_prs_sender", lgCard.getValue("ID_CONTACT_PRS_SENDER"), "'+document.getElementById('id_jur_prs_sender').value+'", "'+document.getElementById('id_service_place_sender').value+'", "40") %>
			</td>			
		</tr>
		<tr>
			<td colspan="2">&nbsp;</td>
			<td><%= Bean.logisticXML.getfieldTransl("desc_sender", false) %></td><td><textarea name="desc_sender" cols="60" rows="3" class="inputfield"><%= lgCard.getValue("DESC_SENDER") %></textarea></td>
		</tr>
		<%=	Bean.getCreationAndMoficationRecordFields(
				lgCard.getValue(Bean.getCreationDateFieldName()),
				lgCard.getValue("CREATED_BY"),
				lgCard.getValue(Bean.getLastUpdateDateFieldName()),
				lgCard.getValue("LAST_UPDATE_BY")
			) %>
		<tr>
			<td colspan="6" align="center">
				<%=Bean.getSubmitButtonAjax("../crm/logistic/partners/cardupdate.jsp") %>
				<%=Bean.getResetButton() %>
				<%=Bean.getGoBackButton("../crm/logistic/partners/cards.jsp") %>
			</td>
		</tr>

		<%} else {%>
		<tr>
			<td><%= Bean.logisticXML.getfieldTransl("id_lg_record", false) %></td><td><input type="text" name="id_lg_record" size="20" value="<%= lgCard.getValue("ID_LG_RECORD") %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.clubXML.getfieldTransl("club", false) %>
				<%= Bean.getGoToClubLink(lgCard.getValue("ID_CLUB")) %>
			</td><td><input type="text" name="name_club" size="50" value="<%= Bean.getClubShortName(lgCard.getValue("ID_CLUB")) %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td><%= Bean.logisticXML.getfieldTransl("name_lg_type", false) %></td><td><input type="text" name="name_lg_type" size="60" value="<%= lgCard.getValue("NAME_LG_TYPE") %>" readonly="readonly" class="inputfield-ro"></td>
			<td class="top_line"><%= Bean.logisticXML.getfieldTransl("sname_jur_prs_receiver", false) %>
			<%=Bean.getGoToJurPrsHyperLink(lgCard.getValue("ID_JUR_PRS_RECEIVER")) %>
			</td><td class="top_line"><input type="text" name="sname_jur_prs_receiver" size="65" value="<%= lgCard.getValue("SNAME_JUR_PRS_RECEIVER") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td><%= Bean.logisticXML.getfieldTransl("action_date_frmt", false) %></td><td><input type="text" name="action_date_frmt" size="20" value="<%= lgCard.getValue("ACTION_DATE_FRMT") %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.logisticXML.getfieldTransl("id_service_place_receiver", false) %>
				<%= Bean.getGoToJurPrsHyperLink(lgCard.getValue("ID_SERVICE_PLACE_RECEIVER")) %>
			</td><td><input type="text" name="name_service_place_receiver" size="65" value="<%= Bean.getServicePlaceName(lgCard.getValue("ID_SERVICE_PLACE_RECEIVER")) %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td rowspan="3"><%=  Bean.logisticXML.getfieldTransl("operation_desc", false) %></td><td rowspan="3"><textarea name="operation_desc" cols="60" rows="6" readonly="readonly" class="inputfield-ro"><%= lgCard.getValue("OPERATION_DESC") %></textarea></td>
			<td><%= Bean.logisticXML.getfieldTransl("id_contact_prs_receiver", false) %>
				<%= Bean.getGoToContactPersonLink(lgCard.getValue("ID_CONTACT_PRS_RECEIVER")) %>
			</td><td><input type="text" name="name_contact_prs_receiver" size="65" value="<%= Bean.getContactPrsName(lgCard.getValue("ID_CONTACT_PRS_RECEIVER")) %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td><%=  Bean.logisticXML.getfieldTransl("desc_receiver", false) %></td><td><textarea name="desc_receiver" cols="60" rows="3" readonly="readonly" class="inputfield-ro"><%= lgCard.getValue("DESC_RECEIVER") %></textarea></td>
		</tr>
		<tr>
			<td class="top_line"><%= Bean.logisticXML.getfieldTransl("sname_jur_prs_sender", false) %>
			<%=Bean.getGoToJurPrsHyperLink(lgCard.getValue("ID_JUR_PRS_SENDER")) %>
			</td><td class="top_line"><input type="text" name="sname_jur_prs_sender" size="65" value="<%= lgCard.getValue("SNAME_JUR_PRS_SENDER") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td><%= Bean.logisticXML.getfieldTransl("bon_cards_count", false) %></td><td><input type="text" name="object_count" size="20" value="<%= lgCard.getValue("BON_CARDS_COUNT") %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.logisticXML.getfieldTransl("id_service_place_sender", false) %>
				<%= Bean.getGoToJurPrsHyperLink(lgCard.getValue("ID_SERVICE_PLACE_SENDER")) %>
			</td><td><input type="text" name="name_service_place_sender" size="65" value="<%= Bean.getServicePlaceName(lgCard.getValue("ID_SERVICE_PLACE_SENDER")) %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td colspan="2">&nbsp;</td>
			<td><%= Bean.logisticXML.getfieldTransl("id_contact_prs_sender", false) %>
				<%= Bean.getGoToContactPersonLink(lgCard.getValue("ID_CONTACT_PRS_SENDER")) %>
			</td><td><input type="text" name="name_contact_prs_sender" size="65" value="<%= Bean.getContactPrsName(lgCard.getValue("ID_CONTACT_PRS_SENDER")) %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td colspan="2">&nbsp;</td>
			<td><%= Bean.logisticXML.getfieldTransl("desc_sender", false) %></td><td><textarea name="desc_sender" cols="60" rows="3" readonly="readonly" class="inputfield-ro"><%= lgCard.getValue("DESC_SENDER") %></textarea></td>
		</tr>
		<%=	Bean.getCreationAndMoficationRecordFields(
				lgCard.getValue(Bean.getCreationDateFieldName()),
				lgCard.getValue("CREATED_BY"),
				lgCard.getValue(Bean.getLastUpdateDateFieldName()),
				lgCard.getValue("LAST_UPDATE_BY")
			) %>
		<tr>
			<td colspan="6" align="center">
				<%=Bean.getGoBackButton("../crm/logistic/partners/cards.jsp") %>
			</td>
		</tr>
		<%} %>

	</table>
	</form>

	<% if ((Bean.currentMenu.isCurrentTabAndEditPermitted("LOGISTIC_PARTNERS_CARDS_INFO"))) { %>
		<!-- Скрипт для втавки меню вибору дати -->
		<%= Bean.getCalendarScript("action_date", false) %>
	<%} %>

<% }

if (Bean.currentMenu.isCurrentTabAndAccessPermitted("LOGISTIC_PARTNERS_CARDS_DOC")) {
%>  
	<table <%= Bean.getTableBottomFilter() %>>
		<tr>
		<%= Bean.getFindHTML("doc_find", doc_find, "../crm/logistic/partners/cardspecs.jsp?id=" + id + "&doc_page=1") %>

		<%=Bean.getSelectOnChangeBeginHTML("doc_type", "../crm/logistic/partners/cardspecs.jsp?id=" + id + "&doc_page=1", Bean.documentXML.getfieldTransl("name_doc_type", false)) %>
			<%= Bean.getDocTypeOptions(doc_type, true) %>
		<%=Bean.getSelectOnChangeEndHTML() %>

		<%=Bean.getSelectOnChangeBeginHTML("doc_state", "../crm/logistic/partners/cardspecs.jsp?id=" + id + "&doc_page=1", Bean.documentXML.getfieldTransl("name_doc_state", false)) %>
			<%= Bean.getDocStateOptions(doc_state, true) %>
		<%=Bean.getSelectOnChangeEndHTML() %>

		</tr>
	</table>
	<%= lgCard.getDocumentsListHTML("LOGISTIC_PARTNERS_CARDS_DOC", "../crm/logistic/lgdocupdate.jsp", doc_find, doc_type, doc_state, l_doc_page_beg, l_doc_page_end) %> 
<%}

if (Bean.currentMenu.isCurrentTabAndAccessPermitted("LOGISTIC_PARTNERS_CARDS_RANGE")) {
	%>
	<table <%= Bean.getTableBottomFilter() %>>
		<tr>
		<%= Bean.getFindHTML("range_find", range_find, "../crm/logistic/partners/cardspecs.jsp?id=" + id + "&range_page=1") %>

		<td>&nbsp;</td>

		</tr>
	</table>
	<%= lgCard.getBonCardsRangesHTML(range_find, l_range_page_beg, l_range_page_end) %>
<%
}

if (Bean.currentMenu.isCurrentTabAndAccessPermitted("LOGISTIC_PARTNERS_CARDS_CARDS")) {
	%>
	<table <%= Bean.getTableBottomFilter() %>>
		<tr>
		<%= Bean.getFindHTML("card_find", card_find, "../crm/logistic/partners/cardspecs.jsp?id=" + id + "&card_page=1") %>

		<td>&nbsp;</td>

		</tr>
	</table>
	<%= lgCard.getBonCardsListHTML(card_find, l_cards_beg, l_cards_end) %>
<%
}

}
		
%>
</div></div>
</body>
</html>
