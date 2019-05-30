<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%@page import="java.util.HashMap"%>
<%@page import="bc.objects.bcLGObject"%>

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

String pageFormName = "LOGISTIC_PARTNERS_SAMS";

String tagDoc = "_DOC";
String tagDocFind = "_DOC_FIND";
String tagDocType = "_DOC_TYPE";
String tagDocState = "_DOC_STATE";
String tagSAM = "_SAM";
String tagSAMFind = "_SAM_FIND";

Bean.setJspPageForTabName(pageFormName);

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
	bcLGObject lgSAM = new bcLGObject("SAM", id);

	//Обрабатываем номера страниц
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

	String l_sam_page = Bean.getDecodeParam(parameters.get("sam_page"));
	Bean.pageCheck(pageFormName + tagSAM, l_sam_page);
	String l_sam_page_beg = Bean.getFirstRowNumber(pageFormName + tagSAM);
	String l_sam_page_end = Bean.getLastRowNumber(pageFormName + tagSAM);

	String sam_find 	= Bean.getDecodeParam(parameters.get("sam_find"));
	sam_find 	= Bean.checkFindString(pageFormName + tagSAMFind, sam_find, l_sam_page);
%>

	<table <%=Bean.getTableMenuParam() %>>
		<tr>
			<%= Bean.getPageHeader() %>
			<% if (Bean.currentMenu.isCurrentTabAndEditPermitted("LOGISTIC_PARTNERS_SAMS_INFO")) {%>
				<%= Bean.getMenuButton("ADD", "../crm/logistic/partners/samupdate.jsp?id=" + id + "&type=general&action=add2&process=no", "", "") %>
				<%= Bean.getMenuButton("DELETE", "../crm/logistic/partners/samupdate.jsp?id=" + id + "&type=general&action=remove&process=yes", Bean.logisticXML.getfieldTransl("h_action_sam_delete", false), lgSAM.getValue("ID_LG_RECORD") + " - " +  lgSAM.getValue("OPERATION_NAME")) %>
			<% } %>

			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("LOGISTIC_PARTNERS_SAMS_DOC")) { %>
				<% if (Bean.currentMenu.isCurrentTabAndEditPermitted("LOGISTIC_PARTNERS_SAMS_DOC")) { %>
					<%= Bean.getMenuButton("ADD", "../crm/logistic/lgdocupdate.jsp?lg_type=SAM&type=doc&id="+ id + "&action=add&process=no", "", "") %>
				<% } %>
			    <!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagDoc, "../crm/logistic/partners/samspecs.jsp?id=" + id + "&tab="+Bean.currentMenu.getTabID("LOGISTIC_PARTNERS_SAMS_DOC") + "&", "doc_page") %>
			<% } %>

			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("LOGISTIC_PARTNERS_SAMS_SAMS")) { %>
				<% if (Bean.currentMenu.isCurrentTabAndEditPermitted("LOGISTIC_PARTNERS_SAMS_SAMS")) { %>
					<%= Bean.getMenuButton("ADD", "../crm/logistic/partners/samupdate.jsp?type=sam&id="+ id + "&action=add&process=no", "", "") %>
					<%= Bean.getMenuButton("ADD_ALL", "../crm/logistic/partners/samupdate.jsp?type=sam&id="+ id + "&action=add_list&process=no", "", "", Bean.logisticXML.getfieldTransl("h_add_sam_select", false)) %>
				<% } %>
			    <!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagSAM, "../crm/logistic/partners/samspecs.jsp?id=" + id + "&tab="+Bean.currentMenu.getTabID("LOGISTIC_PARTNERS_SAMS_SAMS") + "&", "sam_page") %>
			<% } %>

		</tr>
	</table>
	<table <%=Bean.getTableMenuParam() %>>
		<%= Bean.getDetailCaption(lgSAM.getValue("ID_LG_RECORD") + " - " + lgSAM.getValue("OPERATION_NAME")) %>
		<tr>
			<td>
				<!-- Выводим перечень закладок -->
				<%= Bean.currentMenu.getFormTabScheeds(tab, "../crm/logistic/partners/samspecs.jsp?id=" + id) %>
			</td>
		</tr>
	</table>
</div>
<div id="div_data">
<div id="div_data_detail">
<%
if (Bean.currentMenu.isCurrentTabAndAccessPermitted("LOGISTIC_PARTNERS_SAMS_INFO")) {
%>
	
		<script language="JavaScript">
			var formData = new Array (
				new Array ('action_date', 'varchar2', 1)
			);
			</script>

		<% if (Bean.currentMenu.isCurrentTabAndEditPermitted("LOGISTIC_PARTNERS_SAMS_INFO")) { %>
		  <form action="../crm/logistic/partners/samupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onsubmit="return validateForm(formData);">
			<input type="hidden" name="action" value="edit">
	    	<input type="hidden" name="process" value="yes">
	    	<input type="hidden" name="type" value="general">
	    	<input type="hidden" name="id" value="<%= id %>">
		<%} %>

		<table <%=Bean.getTableDetailParam() %>>

		<% if (Bean.currentMenu.isCurrentTabAndEditPermitted("LOGISTIC_PARTNERS_SAMS_INFO")) {%>
		<tr>
			<td><%= Bean.logisticXML.getfieldTransl("id_lg_record", false) %></td><td><input type="text" name="id_lg_record" size="20" value="<%= lgSAM.getValue("ID_LG_RECORD") %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.clubXML.getfieldTransl("club", false) %>
				<%= Bean.getGoToClubLink(lgSAM.getValue("ID_CLUB")) %>
			</td><td><input type="text" name="name_club" size="50" value="<%= Bean.getClubShortName(lgSAM.getValue("ID_CLUB")) %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td><%= Bean.logisticXML.getfieldTransl("name_lg_type", false) %></td><td><input type="text" name="name_lg_type" size="60" value="<%= lgSAM.getValue("NAME_LG_TYPE") %>" readonly="readonly" class="inputfield-ro"></td>
			<td class="top_line"><%= Bean.logisticXML.getfieldTransl("sname_jur_prs_receiver", false) %>
			<%=Bean.getGoToJurPrsHyperLink(lgSAM.getValue("ID_JUR_PRS_RECEIVER")) %>
			</td>
			<td class="top_line">
				<%=Bean.getWindowFindJurPrs("jur_prs_receiver", lgSAM.getValue("ID_JUR_PRS_RECEIVER"), lgSAM.getValue("SNAME_JUR_PRS_RECEIVER"), "ALL", "40") %>
			</td>			
		</tr>
		<tr>
			<td><%= Bean.logisticXML.getfieldTransl("action_date_frmt", true) %></td><td><%=Bean.getCalendarInputField("action_date", lgSAM.getValue("ACTION_DATE_FRMT"), "10") %></td>
			<td><%= Bean.logisticXML.getfieldTransl("id_service_place_receiver", false) %>
				<%= Bean.getGoToJurPrsHyperLink(lgSAM.getValue("ID_SERVICE_PLACE_RECEIVER")) %>
			</td>
			<td>
				<%=Bean.getWindowFindServicePlace("service_place_receiver", lgSAM.getValue("ID_SERVICE_PLACE_RECEIVER"), "", "'+document.getElementById('id_jur_prs_receiver').value+'", "", "40") %>
			</td>			
		</tr>
		<tr>
			<td rowspan="3"><%=  Bean.logisticXML.getfieldTransl("operation_desc", false) %></td><td rowspan="3"><textarea name="operation_desc" cols="60" rows="6" class="inputfield"><%= lgSAM.getValue("OPERATION_DESC") %></textarea></td>
			<td><%= Bean.logisticXML.getfieldTransl("id_contact_prs_receiver", false) %>
				<%= Bean.getGoToContactPersonLink(lgSAM.getValue("ID_CONTACT_PRS_RECEIVER")) %>
			</td>
			<td>
				<%=Bean.getWindowContactPersons("contact_prs_receiver", lgSAM.getValue("ID_CONTACT_PRS_RECEIVER"), "'+document.getElementById('id_jur_prs_receiver').value+'", "'+document.getElementById('id_service_place_receiver').value+'", "40") %>
			</td>			
		</tr>
		<tr>
			<td><%=  Bean.logisticXML.getfieldTransl("desc_receiver", false) %></td><td><textarea name="desc_receiver" cols="60" rows="3" class="inputfield"><%= lgSAM.getValue("DESC_RECEIVER") %></textarea></td>
		</tr>
		<tr>
			<td class="top_line"><%= Bean.logisticXML.getfieldTransl("sname_jur_prs_sender", false) %>
			<%=Bean.getGoToJurPrsHyperLink(lgSAM.getValue("ID_JUR_PRS_SENDER")) %>
			</td>
			<td class="top_line">
				<%=Bean.getWindowFindJurPrs("jur_prs_sender", lgSAM.getValue("ID_JUR_PRS_SENDER"), lgSAM.getValue("SNAME_JUR_PRS_SENDER"), "ALL", "40") %>
			</td>			
		</tr>
		<tr>
			<td><%= Bean.logisticXML.getfieldTransl("sam_count", false) %></td><td><input type="text" name="object_count" size="20" value="<%= lgSAM.getValue("SAM_COUNT") %>" class="inputfield"></td>
			<td><%= Bean.logisticXML.getfieldTransl("id_service_place_sender", false) %>
				<%= Bean.getGoToJurPrsHyperLink(lgSAM.getValue("ID_SERVICE_PLACE_SENDER")) %>
			</td>
			<td>
				<%=Bean.getWindowFindServicePlace("service_place_sender", lgSAM.getValue("ID_SERVICE_PLACE_SENDER"), "", "'+document.getElementById('id_jur_prs_sender').value+'", "", "40") %>
			</td>			
		</tr>
		<tr>
			<td colspan="2">&nbsp;</td>
			<td><%= Bean.logisticXML.getfieldTransl("id_contact_prs_sender", false) %>
				<%= Bean.getGoToContactPersonLink(lgSAM.getValue("ID_CONTACT_PRS_SENDER")) %>
			</td>
			<td>
				<%=Bean.getWindowContactPersons("contact_prs_sender", lgSAM.getValue("ID_CONTACT_PRS_SENDER"), "'+document.getElementById('id_jur_prs_sender').value+'", "'+document.getElementById('id_service_place_sender').value+'", "40") %>
			</td>			
		</tr>
		<tr>
			<td colspan="2">&nbsp;</td>
			<td><%= Bean.logisticXML.getfieldTransl("desc_sender", false) %></td><td><textarea name="desc_sender" cols="60" rows="3" class="inputfield"><%= lgSAM.getValue("DESC_SENDER") %></textarea></td>
		</tr>
		<%=	Bean.getCreationAndMoficationRecordFields(
				lgSAM.getValue(Bean.getCreationDateFieldName()),
				lgSAM.getValue("CREATED_BY"),
				lgSAM.getValue(Bean.getLastUpdateDateFieldName()),
				lgSAM.getValue("LAST_UPDATE_BY")
			) %>
		<tr>
			<td colspan="4" align="center">
				<%=Bean.getSubmitButtonAjax("../crm/logistic/partners/samupdate.jsp") %>
				<%=Bean.getResetButton() %>
				<%=Bean.getGoBackButton("../crm/logistic/partners/sams.jsp") %>
			</td>
		</tr>

		<%} else {%>
		<tr>
			<td><%= Bean.logisticXML.getfieldTransl("id_lg_record", false) %></td><td><input type="text" name="id_lg_record" size="20" value="<%= lgSAM.getValue("ID_LG_RECORD") %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.clubXML.getfieldTransl("club", false) %>
				<%= Bean.getGoToClubLink(lgSAM.getValue("ID_CLUB")) %>
			</td><td><input type="text" name="name_club" size="50" value="<%= Bean.getClubShortName(lgSAM.getValue("ID_CLUB")) %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td><%= Bean.logisticXML.getfieldTransl("name_lg_type", false) %></td><td><input type="text" name="name_lg_type" size="60" value="<%= lgSAM.getValue("NAME_LG_TYPE") %>" readonly="readonly" class="inputfield-ro"></td>
			<td class="top_line"><%= Bean.logisticXML.getfieldTransl("sname_jur_prs_receiver", false) %>
			<%=Bean.getGoToJurPrsHyperLink(lgSAM.getValue("ID_JUR_PRS_RECEIVER")) %>
			</td><td class="top_line"><input type="text" name="sname_jur_prs_receiver" size="65" value="<%= lgSAM.getValue("SNAME_JUR_PRS_RECEIVER") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td><%= Bean.logisticXML.getfieldTransl("action_date_frmt", false) %></td><td><input type="text" name="action_date_frmt" size="20" value="<%= lgSAM.getValue("ACTION_DATE_FRMT") %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.logisticXML.getfieldTransl("id_service_place_receiver", false) %>
				<%= Bean.getGoToJurPrsHyperLink(lgSAM.getValue("ID_SERVICE_PLACE_RECEIVER")) %>
			</td><td><input type="text" name="name_service_place_receiver" size="65" value="<%= Bean.getServicePlaceName(lgSAM.getValue("ID_SERVICE_PLACE_RECEIVER")) %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td rowspan="3"><%=  Bean.logisticXML.getfieldTransl("operation_desc", false) %></td><td rowspan="3"><textarea name="operation_desc" cols="60" rows="6" readonly="readonly" class="inputfield-ro"><%= lgSAM.getValue("OPERATION_DESC") %></textarea></td>
			<td><%= Bean.logisticXML.getfieldTransl("id_contact_prs_receiver", false) %>
				<%= Bean.getGoToContactPersonLink(lgSAM.getValue("ID_CONTACT_PRS_RECEIVER")) %>
			</td><td><input type="text" name="name_contact_prs_receiver" size="65" value="<%= Bean.getContactPrsName(lgSAM.getValue("ID_CONTACT_PRS_RECEIVER")) %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td><%=  Bean.logisticXML.getfieldTransl("desc_receiver", false) %></td><td><textarea name="desc_receiver" cols="60" rows="3" readonly="readonly" class="inputfield-ro"><%= lgSAM.getValue("DESC_RECEIVER") %></textarea></td>
		</tr>
		<tr>
			<td class="top_line"><%= Bean.logisticXML.getfieldTransl("sname_jur_prs_sender", false) %>
			<%=Bean.getGoToJurPrsHyperLink(lgSAM.getValue("ID_JUR_PRS_SENDER")) %>
			</td><td class="top_line"><input type="text" name="sname_jur_prs_sender" size="65" value="<%= lgSAM.getValue("SNAME_JUR_PRS_SENDER") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td><%= Bean.logisticXML.getfieldTransl("sam_count", false) %></td><td><input type="text" name="object_count" size="20" value="<%= lgSAM.getValue("SAM_COUNT") %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.logisticXML.getfieldTransl("id_service_place_sender", false) %>
				<%= Bean.getGoToJurPrsHyperLink(lgSAM.getValue("ID_SERVICE_PLACE_SENDER")) %>
			</td><td><input type="text" name="name_service_place_sender" size="65" value="<%= Bean.getServicePlaceName(lgSAM.getValue("ID_SERVICE_PLACE_SENDER")) %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td colspan="2">&nbsp;</td>
			<td><%= Bean.logisticXML.getfieldTransl("id_contact_prs_sender", false) %>
				<%= Bean.getGoToContactPersonLink(lgSAM.getValue("ID_CONTACT_PRS_SENDER")) %>
			</td><td><input type="text" name="name_contact_prs_sender" size="65" value="<%= Bean.getContactPrsName(lgSAM.getValue("ID_CONTACT_PRS_SENDER")) %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td colspan="2">&nbsp;</td>
			<td><%= Bean.logisticXML.getfieldTransl("desc_sender", false) %></td><td><textarea name="desc_sender" cols="60" rows="3" readonly="readonly" class="inputfield-ro"><%= lgSAM.getValue("DESC_SENDER") %></textarea></td>
		</tr>
		<%=	Bean.getCreationAndMoficationRecordFields(
				lgSAM.getValue(Bean.getCreationDateFieldName()),
				lgSAM.getValue("CREATED_BY"),
				lgSAM.getValue(Bean.getLastUpdateDateFieldName()),
				lgSAM.getValue("LAST_UPDATE_BY")
			) %>
		<tr>
			<td colspan="4" align="center">
				<%=Bean.getGoBackButton("../crm/logistic/partners/sams.jsp") %>
			</td>
		</tr>
		<%} %>

	</table>
	</form>

	<% if ((Bean.currentMenu.isCurrentTabAndEditPermitted("LOGISTIC_PARTNERS_SAMS_INFO"))) { %>
		<!-- Скрипт для втавки меню вибору дати -->
		<%= Bean.getCalendarScript("action_date", false) %>
	<%} %>

<% }

if (Bean.currentMenu.isCurrentTabAndAccessPermitted("LOGISTIC_PARTNERS_SAMS_DOC")) {
%> 
	<table <%= Bean.getTableBottomFilter() %>>
		<tr>
		<%= Bean.getFindHTML("doc_find", doc_find, "../crm/logistic/partners/samspecs.jsp?id=" + id + "&doc_page=1") %>

		<%=Bean.getSelectOnChangeBeginHTML("doc_type", "../crm/logistic/partners/samspecs.jsp?id=" + id + "&doc_page=1", Bean.documentXML.getfieldTransl("name_doc_type", false)) %>
			<%= Bean.getDocTypeOptions(doc_type, true) %>
		<%=Bean.getSelectOnChangeEndHTML() %>

		<%=Bean.getSelectOnChangeBeginHTML("doc_state", "../crm/logistic/partners/samspecs.jsp?id=" + id + "&doc_page=1", Bean.documentXML.getfieldTransl("name_doc_state", false)) %>
			<%= Bean.getDocStateOptions(doc_state, true) %>
		<%=Bean.getSelectOnChangeEndHTML() %>

		</tr>
	</table>
	<%= lgSAM.getDocumentsListHTML("LOGISTIC_PARTNERS_SAMS_DOC", "../crm/logistic/lgdocupdate.jsp", doc_find, doc_type, doc_state, l_doc_page_beg, l_doc_page_end) %> 
<%}

if (Bean.currentMenu.isCurrentTabAndAccessPermitted("LOGISTIC_PARTNERS_SAMS_SAMS")) {
%>  
	<table <%= Bean.getTableBottomFilter() %>>
		<tr>
		<%= Bean.getFindHTML("sam_find", sam_find, "../crm/logistic/partners/samspecs.jsp?id=" + id + "&sam_page=1") %>

		<td>&nbsp;</td>

		</tr>
	</table>
	<%= lgSAM.getSAMsListHTML(sam_find, l_sam_page_beg, l_sam_page_end) %> 
<%}

}
		
%>
</div></div>
</body>
</html>
