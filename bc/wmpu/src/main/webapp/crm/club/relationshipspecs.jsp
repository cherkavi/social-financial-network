<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%@page import="bc.objects.bcContactsObject"%>
<%@page import="bc.objects.bcDocumentObject"%>
<%@page import="bc.objects.bcClubRelationshipObject"%>
<%@page import="java.util.HashMap"%><html>

<%= Bean.getLogOutScript(request) %>

<head>
	<%= Bean.getMetaContent() %>
	<%= Bean.getBottomFrameCSS() %>

<title></title>
</head>

<% 

HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String pageFormName = "CLUB_RELATIONSHIP";
String tagDoc = "_DOC";
String tagDocFind = "_DOC_FIND";
String tagDocType = "_DOC_TYPE";
String tagDocState = "_DOC_STATE";
String tagDocKind = "_DOC_KIND";
String tagComissions = "_COMISSIONS";
String tagComissionFind = "_COMISSION_FIND";
String tagPostingScheme = "_POSTING_SCHEME";
String tagPostingSchemeFind = "_POSTING_SCHEME_FIND";
String tagPostingSchemeFiltr = "_POSTING_SCHEME_FILTR";


Bean.setJspPageForTabName(pageFormName);

String type = Bean.getDecodeParam(parameters.get("type"));

String id = Bean.getDecodeParam(parameters.get("id"));
String tab = Bean.getDecodeParam(parameters.get("tab"));
if (tab==null || "".equalsIgnoreCase(tab)) { tab = Bean.tabsHmGetValue(pageFormName); }
if (id==null || "".equalsIgnoreCase(id)) { id=""; }
if ("".equalsIgnoreCase(id)) {
%>
	<%=Bean.getIDNotFoundMessage() %>
<% } 
else {
	Bean.tabsHmSetValue(pageFormName, tab);
	bcClubRelationshipObject rel = new bcClubRelationshipObject("CREATED", id);

	//Обрабатываем номера страниц
	String l_doc_page = Bean.getDecodeParam(parameters.get("doc_page"));

	String doc_kind 	= Bean.getDecodeParam(parameters.get("doc_kind"));
	if (doc_kind==null || "".equalsIgnoreCase(doc_kind)) {
		doc_kind = "CREATED";
	}
	doc_kind = Bean.checkFindString(pageFormName + tagDocKind, doc_kind, l_doc_page);
	
	Bean.pageCheck(pageFormName + tagDoc, l_doc_page);
	String l_doc_page_beg = Bean.getFirstRowNumber(pageFormName + tagDoc);
	String l_doc_page_end = Bean.getLastRowNumber(pageFormName + tagDoc);
	
	String doc_find 	= Bean.getDecodeParam(parameters.get("doc_find"));
	doc_find 	= Bean.checkFindString(pageFormName + tagDocFind, doc_find, l_doc_page);
	
	String doc_type 	= Bean.getDecodeParam(parameters.get("doc_type"));
	doc_type 	= Bean.checkFindString(pageFormName + tagDocType, doc_type, l_doc_page);
	
	String doc_state 	= Bean.getDecodeParam(parameters.get("doc_state"));
	doc_state 	= Bean.checkFindString(pageFormName + tagDocState, doc_state, l_doc_page);
	
	String l_comis_page = Bean.getDecodeParam(parameters.get("comis_page"));
	Bean.pageCheck(pageFormName + tagComissions, l_comis_page);
	String l_comis_page_beg = Bean.getFirstRowNumber(pageFormName + tagComissions);
	String l_comis_page_end = Bean.getLastRowNumber(pageFormName + tagComissions);
	
	String comis_find 	= Bean.getDecodeParam(parameters.get("comis_find"));
	comis_find 	= Bean.checkFindString(pageFormName + tagComissionFind, comis_find, l_comis_page);
	
	String l_posting_scheme_page = Bean.getDecodeParam(parameters.get("posting_scheme_page"));
	Bean.pageCheck(pageFormName + tagPostingScheme, l_posting_scheme_page);
	String l_posting_scheme_page_beg = Bean.getFirstRowNumber(pageFormName + tagPostingScheme);
	String l_posting_scheme_page_end = Bean.getLastRowNumber(pageFormName + tagPostingScheme);
	
	String posting_scheme_find 	= Bean.getDecodeParam(parameters.get("posting_scheme_find"));
	posting_scheme_find 	= Bean.checkFindString(pageFormName + tagPostingSchemeFind, posting_scheme_find, l_posting_scheme_page);
	
	String posting_scheme_filtr	= Bean.getDecodeParam(parameters.get("posting_scheme_filtr"));
	posting_scheme_filtr 			= Bean.checkFindString(pageFormName + tagPostingSchemeFiltr, posting_scheme_filtr, l_posting_scheme_page);
	
%>
<% Bean.currentMenu.setCurrentTab(Bean.currentMenu.getTabSheetName(tab)); %>
<body>
<div id="div_tabsheet">

	<table <%=Bean.getTableMenuParam() %>>
		<tr>
			<%= Bean.getPageHeader() %>
			<% if (Bean.currentMenu.isCurrentTabAndEditPermitted("CLUB_RELATIONSHIP_INFO")) {%>
			    <%= Bean.getMenuButton("ADD", "../crm/club/relationshipupdate.jsp?id=" + id + "&type=general&action=adddet&process=no", "", "") %>
			    <%= Bean.getMenuButton("DELETE", "../crm/club/relationshipupdate.jsp?id=" + id + "&type=general&action=remove&process=yes", Bean.relationshipXML.getfieldTransl("h_delete_relationship", false), rel.getValue("ID_CLUB_REL") + " - " +  rel.getValue("FULL_NAME_CLUB_REL")) %>
			<% } %>

			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLUB_RELATIONSHIP_DOC")) {%>
				<% if (Bean.currentMenu.isCurrentTabAndEditPermitted("CLUB_RELATIONSHIP_DOC")) { %>
				    <%= Bean.getMenuButton("ADD", "../crm/club/relationshipdocupdate.jsp?type=doc&id="+id+"&action=add&process=no", "", "") %>
				<% } %>	
			    <!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagDoc, "../crm/club/relationshipspecs.jsp?id=" + id + "&tab="+Bean.currentMenu.getTabID("CLUB_RELATIONSHIP_DOC")+"&", "doc_page") %>
			<% } %>
	
			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLUB_RELATIONSHIP_POSTING_SCHEME")) {%>
			    <!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagPostingScheme, "../crm/club/relationshipspecs.jsp?id=" + id + "&tab="+Bean.currentMenu.getTabID("CLUB_RELATIONSHIP_POSTING_SCHEME")+"&", "posting_scheme_page") %>
			<% } %>
	
			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLUB_RELATIONSHIP_COMISSIONS")) {%>
				<% if (Bean.currentMenu.isCurrentTabAndEditPermitted("CLUB_RELATIONSHIP_COMISSIONS")) { %>
				    <%= Bean.getMenuButton("ADD", "../crm/club/relationshipcomissionupdate.jsp?type=comission&id="+id+"&action=add&process=no", "", "") %>
				    <%= Bean.getMenuButton("ADD_ALL", "../crm/club/relationshipcomissionupdate.jsp?type=comission&id="+id+"&action=addall&process=yes", Bean.jurpersonXML.getfieldTransl("h_add_all_comissions", false), "") %>
				    <%= Bean.getMenuButton("DELETE_ALL", "../crm/club/relationshipcomissionupdate.jsp?type=comission&id="+id+"&action=removeall&process=yes", Bean.jurpersonXML.getfieldTransl("h_delete_all_comissions", false), "") %>
				<% } %>	
			    <!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagComissions, "../crm/club/relationshipspecs.jsp?id=" + id + "&tab="+Bean.currentMenu.getTabID("CLUB_RELATIONSHIP_COMISSIONS")+"&", "comis_page") %>
			<% } %>

		</tr>

	</table>
	<table <%=Bean.getTableMenuParam() %>>
		<%= Bean.getDetailCaption(rel.getValue("ID_CLUB_REL") + " - " +  rel.getValue("FULL_NAME_CLUB_REL")) %>
		<tr>
			<td>
				<!-- Выводим перечень закладок -->
				<%= Bean.currentMenu.getFormTabScheeds(tab, "../crm/club/relationshipspecs.jsp?id=" + id) %>
			</td>

		</tr>
	</table>
</div>
<div id="div_data">
<div id="div_data_detail">

<%

if (Bean.currentMenu.isCurrentTabAndEditPermitted("CLUB_RELATIONSHIP_INFO")) {
%>

	<script language="JavaScript">
		<%= rel.getClubRelCheckScript(rel.getValue("CD_CLUB_REL_TYPE"))%>
	</script>

    <form action="../crm/club/relationshipupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return myValidateForm()">
        <input type="hidden" name="type" value="general">
        <input type="hidden" name="action" value="edit">
        <input type="hidden" name="process" value="yes">
        <input type="hidden" name="id" value="<%=id%>">
	<table <%=Bean.getTableDetailParam() %>>
		
		<%=rel.getClubRelEditHTML(Bean.getDateFormatTitle()) %>

 		<tr>
			<td colspan="6" align="center">
				<%=Bean.getSubmitButtonAjax("../crm/club/relationshipupdate.jsp") %>
				<%=Bean.getResetButton() %>
				<%=Bean.getGoBackButton("../crm/club/relationship.jsp") %>
			</td>
		</tr>
	</table>

	</form> 
		<script type="text/javascript">
  		Calendar.setup({
			inputField  : "id_date_club_rel",         // ID поля вводу дати
      		ifFormat    : "<%= Bean.getJSPDateFormat()%>",    // формат дати (23.03.2008)
      		button      : "btn_date_club_rel"       // ID кнопки для меню вибору дати
    	});		
		</script>
 <% } else if (Bean.currentMenu.isCurrentTabAndPreviewPermitted("CLUB_RELATIONSHIP_INFO")) { %>
 	<table <%=Bean.getTableDetailParam() %>>
		
		<%= rel.getClubRelPreviewHTML(Bean.getDateFormatTitle()) %>

 		<tr>
			<td colspan="6" align="center">
				<%=Bean.getGoBackButton("../crm/club/relationship.jsp") %>
			</td>
		</tr>
	</table>

 <% }

if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLUB_RELATIONSHIP_DOC")) {%>
		<table <%=Bean.getTableBottomFilter() %>><tbody>
		<tr>
			<%=Bean.getFindHTML("doc_find", doc_find, "../crm/club/relationshipspecs.jsp?id=" + id + "&doc_page=1") %>

			<%=Bean.getSelectOnChangeBeginHTML("doc_kind", "../crm/club/relationshipspecs.jsp?id=" + id + "&doc_page=1", "") %>
				<%=Bean.getSelectOptionHTML(doc_kind, "CREATED", Bean.relationshipXML.getfieldTransl("h_created", false)) %>
				<%=Bean.getSelectOptionHTML(doc_kind, "NEEDED", Bean.relationshipXML.getfieldTransl("h_needed", false)) %>
			<%=Bean.getSelectOnChangeEndHTML() %>

			<%=Bean.getSelectOnChangeBeginHTML("doc_type", "../crm/club/relationshipspecs.jsp?id=" + id + "&doc_page=1", Bean.documentXML.getfieldTransl("name_doc_type", false)) %>
				<%= Bean.getDocTypeOptions(doc_type, true) %>
			<%=Bean.getSelectOnChangeEndHTML() %>

			<% if ("CREATED".equalsIgnoreCase(doc_kind)) { %>
			<%=Bean.getSelectOnChangeBeginHTML("doc_state", "../crm/club/relationshipspecs.jsp?id=" + id + "&doc_page=1", Bean.documentXML.getfieldTransl("name_doc_state", false)) %>
				<%= Bean.getDocStateOptions(doc_state, true) %>
			<%=Bean.getSelectOnChangeEndHTML() %>
			<% } %>

		</tr>
		</tbody>
		</table>
	<% if ("CREATED".equalsIgnoreCase(doc_kind)) { %>
		<%= rel.getDocumentsListHTML(doc_find, doc_type, doc_state, l_doc_page_beg, l_doc_page_end, "CLUB_RELATIONSHIP_DOC", "../crm/club/relationshipdocupdate.jsp?id=" + id) %>
	<% } else { %>
		<%= rel.getDocumentsNeededListHTML(doc_find, doc_type, l_doc_page_beg, l_doc_page_end, "CLUB_RELATIONSHIP_DOC", "../crm/club/relationshipdocupdate.jsp?id=" + id) %>
	<% } %>
<%}

if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLUB_RELATIONSHIP_COMISSIONS")) {%>
		<table <%=Bean.getTableBottomFilter() %>><tbody>
		<tr>
			<%=Bean.getFindHTML("comis_find", comis_find, "../crm/club/relationshipspecs.jsp?id=" + id + "&comis_page=1") %>
			<td align="right">
			<%
			String needComission = rel.getComissionNeedCount();
			  
			  if (!(needComission.equalsIgnoreCase("0"))) {
				  %>
					<b><font color=red><%= Bean.comissionXML.getfieldTransl("need_comission_count", false) %> -  <%=needComission %></font></b><br>
				  <%
			  } else {
				  %>
					<b><font color=green><%= Bean.comissionXML.getfieldTransl("all_comission_was_created", false) %></font></b><br>
				  <%
			  }

			%>
			</td>
		</tr>
		</tbody>
		</table>
	<%= rel.getComissionListHTML(comis_find, l_comis_page_beg, l_comis_page_end) %>
<%}

if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLUB_RELATIONSHIP_POSTING_SCHEME")) {%>
<script type="text/javascript">
 	function CheckCB(Element) {
		myCheck = true;

		thisCheckBoxes = document.getElementsByTagName('input');
		for (i = 1; i < thisCheckBoxes.length; i++) { 
			myName = thisCheckBoxes[i].name;
			if (myName.substr(0,6) == 'chb_id'){
				myCheck = myCheck && thisCheckBoxes[i].checked;
			}
		}
		if (document.getElementById('mainCheck')) {
			document.getElementById('mainCheck').checked = myCheck;
		}
	}
	function CheckAll(Element) {
		thisCheckBoxes = document.getElementsByTagName('input');
		for (i = 1; i < thisCheckBoxes.length; i++) { 
			myName = thisCheckBoxes[i].name;
			
			if (myName.substr(0,6) == 'chb_id'){
					thisCheckBoxes[i].checked = document.getElementById('mainCheck').checked;
			}
		}
	}
	CheckCB();
</script>
		<table <%=Bean.getTableBottomFilter() %>><tbody>
		<tr>
			<%=Bean.getFindHTML("posting_scheme_find", posting_scheme_find, "../crm/club/relationshipspecs.jsp?id=" + id + "&posting_scheme_page=1") %>

			<%=Bean.getSelectOnChangeBeginHTML("posting_scheme_filtr", "../crm/club/relationshipspecs.jsp?id=" + id + "&posting_scheme_page=1", Bean.posting_schemeXML.getfieldTransl("general", false)) %>
				<%= Bean.getBKOperationSchemeOptions(posting_scheme_filtr, true) %>
			<%=Bean.getSelectOnChangeEndHTML() %>
		</tr>
		</tbody>
		</table>

	<%= rel.getOperationSchemesHTML(posting_scheme_find, posting_scheme_filtr, l_posting_scheme_page_beg, l_posting_scheme_page_end) %>
<%}

} %>
</div></div>
</body>
</html>
