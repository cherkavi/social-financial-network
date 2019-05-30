<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%@page import="bc.reports.bcReports"%>
<%@page import="bc.objects.bcFNOperSchemeLineObject"%>
<%@page import="java.util.HashMap"%>
<%@page import="bc.objects.bcFNOperSchemeObject"%><html>

<%= Bean.getLogOutScript(request) %>

<head>
	<%=Bean.getMetaContent() %>
	<%=Bean.getBottomFrameCSS() %>

</head>

<%

HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String pageFormName = "FINANCE_OPER_SCHEME";

Bean.setJspPageForTabName(pageFormName);

String tabBKPhase = "_BK_PHASE";
String tagReport = "_REPORTS";
String tagReportDetail = "_REPORT_DETAIL";
String tagFind = "_FIND_GENERAL";
String tagFindDet = "_FIND_DET";
String tagStateLine = "_STATE_LINE";
String tagClubRel = "_CLUB_REL";

String id = Bean.getDecodeParam(parameters.get("id"));

String id_report = Bean.getDecodeParam(parameters.get("id_report"));

if (id==null || "".equalsIgnoreCase(id)) {
	id = "";
}

String tab = Bean.getDecodeParam(parameters.get("tab"));
if (tab==null || "".equalsIgnoreCase(tab)) { tab = Bean.tabsHmGetValue(pageFormName); }

String find_string = Bean.getDecodeParam(parameters.get("find_string"));

if (find_string==null) { 
	find_string = Bean.filtersHmGetValue(pageFormName + tagFind); 
} else if ("".equalsIgnoreCase(find_string)) {
	Bean.filtersHmSetValue(pageFormName + tagFind, find_string);
} else {
	Bean.filtersHmSetValue(pageFormName + tagFind, find_string);
}

//Обрабатываем номера страниц
String l_report_det_page = Bean.getDecodeParam(parameters.get("report_det_page"));

if (id_report==null || "".equalsIgnoreCase(id_report)) {
	l_report_det_page = "1";
}
Bean.pageCheck(pageFormName + tagReportDetail, l_report_det_page);
String l_report_det_page_beg = Bean.getFirstRowNumber(pageFormName + tagReportDetail);
String l_report_det_page_end = Bean.getLastRowNumber(pageFormName + tagReportDetail);

String l_report_page = Bean.getDecodeParam(parameters.get("report_page"));
Bean.pageCheck(pageFormName + tagReport, l_report_page);
String l_report_page_beg = Bean.getFirstRowNumber(pageFormName + tagReport);
String l_report_page_end = Bean.getLastRowNumber(pageFormName + tagReport);

String l_club_rel_page = Bean.getDecodeParam(parameters.get("club_rel_page"));
Bean.pageCheck(pageFormName + tagClubRel, l_club_rel_page);
String l_club_rel_page_beg = Bean.getFirstRowNumber(pageFormName + tagClubRel);
String l_club_rel_page_end = Bean.getLastRowNumber(pageFormName + tagClubRel);

String rel_kind = Bean.getDecodeParam(parameters.get("rel_kind"));
if (rel_kind==null || "".equalsIgnoreCase(rel_kind)) {
	rel_kind = "CREATED";
}

String find_det 			= Bean.getDecodeParam(parameters.get("find_det"));
String state_line			= Bean.getDecodeParam(parameters.get("state_line"));

find_det	 	= Bean.checkFindString(pageFormName + tagFindDet, find_det, l_report_det_page);
state_line		= Bean.checkFindString(pageFormName + tagStateLine, state_line, l_report_det_page);


if ("".equalsIgnoreCase(id)) { 
%>
	<%=Bean.getIDNotFoundMessage() %>
<% 
}
else { 
	Bean.tabsHmSetValue(pageFormName, tab);
	bcFNOperSchemeLineObject line = new bcFNOperSchemeLineObject(id);
	
	bcFNOperSchemeObject scheme = new bcFNOperSchemeObject(line.getValue("ID_FN_OPER_SCHEME"));

	Bean.currentMenu.setExistFlag("FINANCE_OPER_SCHEME_REPORTS",true);
	Bean.currentMenu.setExistFlag("FINANCE_OPER_SCHEME_RELATIONSHIPS",true);
	Bean.currentMenu.setExistFlag("FINANCE_OPER_SCHEME_LINES",false);
	if (Bean.currentMenu.isCurrentTab("FINANCE_OPER_SCHEME_LINES")) {
		Bean.currentMenu.setFirstCurrentTab();
		tab = Bean.currentMenu.getCurrentTab();
		Bean.tabsHmSetValue(pageFormName, tab);
	}

%>
<body>
<div id="div_tabsheet">

	<table <%=Bean.getTableMenuParam() %>>
		<tr>
			<%= Bean.getPageHeader() %>

			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("FINANCE_OPER_SCHEME_INFO")) { %>
				<%= Bean.getReportHyperLink("SOSR02", "ID_BK_OPERATION_SCHEME_LINE=" + id) %>
			<% } %>
			<% if (Bean.currentMenu.isCurrentTabAndEditPermitted("FINANCE_OPER_SCHEME_INFO")) { %>
				<%= Bean.getMenuButton("ADD", "../crm/finance/oper_scheme_lineupdate.jsp?id=" + id + "&id_scheme=" + line.getValue("ID_BK_OPERATION_SCHEME") + "&type=general&action=add2&process=no", "", "") %>
				<%= Bean.getMenuButton("DELETE", "../crm/finance/oper_scheme_lineupdate.jsp?id=" + id + "&id_scheme=" + line.getValue("ID_BK_OPERATION_SCHEME") + "&type=general&action=remove&process=yes", Bean.oper_schemeXML.getfieldTransl("h_delete_line", false), line.getValue("OPER_NUMBER") + " - " + line.getValue("NAME_BK_OPERATION_TYPE")) %>
			<% } %>

			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("FINANCE_OPER_SCHEME_RELATIONSHIPS")) { %>
			    <!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagClubRel, "../crm/finance/oper_scheme_linespecs.jsp?id=" +id + "&tab="+Bean.currentMenu.getTabID("FINANCE_OPER_SCHEME_RELATIONSHIPS")+"&", "club_rel_page") %>
			<% } %>
	
			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("FINANCE_OPER_SCHEME_REPORTS")) { %>
				<% if (Bean.currentMenu.isCurrentTabAndEditPermitted("FINANCE_OPER_SCHEME_REPORTS")) { %>
					<%= Bean.getMenuButton("CHECK", "../crm/finance/oper_scheme_lineupdate.jsp?id=" + id + "&type=general&action=check&process=yes", Bean.oper_schemeXML.getfieldTransl("h_check", false), id) %>
				<% } %>

			    <!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagReport, "../crm/finance/oper_scheme_linespecs.jsp?id=" +id + "&tab="+Bean.currentMenu.getTabID("FINANCE_POSTINGS_REPORTS")+"&", "report_page") %>
			<% } %>
	
		</tr>
	</table>
	<table <%=Bean.getTableMenuParam() %>>
		<tr>
			<td>
				<center>
					<div class="div_caption2" onclick="ajaxpage('../crm/finance/oper_schemespecs.jsp?id=<%=line.getValue("ID_FN_OPER_SCHEME") %>','div_main')"> 
					<%= scheme.getValue("ID_FN_OPER_SCHEME") + ": " + scheme.getValue("NAME_FN_OPER_SCHEME") %>
					</div>
				</center>
			</td>
		</tr>
		<%= Bean.getDetailCaption(line.getValue("ORDER_NUMBER") + " - " + line.getValue("FULL_NAME_FN_OPER_TYPE")) %>
		<tr>
			<td>
				<!-- Выводим перечень закладок -->
				<%= Bean.currentMenu.getFormTabScheeds(tab, "../crm/finance/oper_scheme_linespecs.jsp?id=" + id) %>
			</td>

			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("FINANCE_OPER_SCHEME_REPORTS")) { %>
				<%= Bean.getFindHTML("find_string", find_string, "../crm/finance/oper_scheme_linespecs.jsp?id=" + id + "&report_page=1&") %>
			<% } %>
		</tr>
	</table>
</div>
<div id="div_data">
<div id="div_data_detail">

<% if (Bean.currentMenu.isCurrentTabAndEditPermitted("FINANCE_OPER_SCHEME_INFO")) {%> 
    <script>
    	var formData = new Array (
			new Array ('desc_fn_oper_scheme_line', 'varchar2', 1),
			new Array ('order_number', 'varchar2', 1),
			new Array ('cd_fn_oper_type', 'varchar2', 1)
		);
		function myValidateForm() {
			return validateForm(formData);
		}
	</script>
	<form action="../crm/finance/oper_scheme_lineupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return myValidateForm()">
	        <input type="hidden" name="type" value="general">
	        <input type="hidden" name="action" value="edit">
	        <input type="hidden" name="process" value="yes">
	        <input type="hidden" name="id" value="<%= id %>">
	
		<table <%=Bean.getTableDetailParam() %>>
        <tr>
			<td><%= Bean.oper_schemeXML.getfieldTransl("id_fn_oper_scheme_line", false) %></td> <td><input type="text" name="id_fn_oper_scheme_line" size="20" value="<%=line.getValue("ID_FN_OPER_SCHEME_LINE") %>" readonly="readonly" class="inputfield-ro"> </td>
			<td colspan="2">&nbsp;</td>
		</tr>
		<tr>
			<td><%= Bean.oper_schemeXML.getfieldTransl("cd_fn_oper_type", true) %></td> <td><select name="cd_fn_oper_type" id="cd_fn_oper_type" class="inputfield"><%= Bean.getFNOperTypeOptions(line.getValue("CD_FN_OPER_TYPE"), false) %></select></td>
			<td colspan="2">&nbsp;</td>
		</tr>
		<tr>
			<td><%= Bean.oper_schemeXML.getfieldTransl("order_number", true) %></td> <td><input type="text" name="order_number" size="20" value="<%=line.getValue("ORDER_NUMBER") %>" class="inputfield"> </td>
			<td colspan="2">&nbsp;</td>
		</tr>
        <tr>
			<td valign=top><%= Bean.oper_schemeXML.getfieldTransl("desc_fn_oper_scheme_line", true) %></td><td valign=top><textarea name="desc_fn_oper_scheme_line" cols="74" rows="5" class="inputfield"><%=line.getValue("DESC_FN_OPER_SCHEME_LINE") %></textarea></td>
			<td valign=top><%= Bean.oper_schemeXML.getfieldTransl("note_fn_oper_scheme_line", false) %></td><td valign=top><textarea name="note_fn_oper_scheme_line" cols="64" rows="5" class="inputfield"><%=line.getValue("NOTE_FN_OPER_SCHEME_LINE") %></textarea></td>
		</tr>
		<%=	Bean.getCreationAndMoficationRecordFields(
				line.getValue(Bean.getCreationDateFieldName()),
				line.getValue("CREATED_BY"),
				line.getValue(Bean.getLastUpdateDateFieldName()),
				line.getValue("LAST_UPDATE_BY")
			) %>
		<tr>
			<td colspan="4" align="center">
				<%=Bean.getSubmitButtonAjax("../crm/finance/oper_scheme_lineupdate.jsp") %>
				<%=Bean.getResetButton() %>
				<%=Bean.getGoBackButton("../crm/finance/oper_schemespecs.jsp?id=" + scheme.getValue("ID_FN_OPER_SCHEME")) %>
			</td>
		</tr>
	</table>
</form>
<% } else if (Bean.currentMenu.isCurrentTabAndPreviewPermitted("FINANCE_OPER_SCHEME_INFO")) {%> 
	<table <%=Bean.getTableDetailParam() %>>
        <tr>
			<td><%= Bean.oper_schemeXML.getfieldTransl("id_fn_oper_scheme_line", false) %></td> <td><input type="text" name="id_fn_oper_scheme_line" size="20" value="<%=line.getValue("ID_FN_OPER_SCHEME_LINE") %>" readonly="readonly" class="inputfield-ro"> </td>
			<td colspan="2">&nbsp;</td>
		</tr>
		<tr>
			<td><%= Bean.oper_schemeXML.getfieldTransl("cd_fn_oper_type", true) %></td> <td><select name="cd_fn_oper_type" id="cd_fn_oper_type" class="inputfield"><%= Bean.getFNOperTypeOptions(line.getValue("CD_FN_OPER_TYPE"), false) %></select></td>
			<td colspan="2">&nbsp;</td>
		</tr>
		<tr>
			<td><%= Bean.oper_schemeXML.getfieldTransl("order_number", true) %></td> <td><input type="text" name="order_number" size="20" value="<%=line.getValue("ORDER_NUMBER") %>" class="inputfield"> </td>
			<td colspan="2">&nbsp;</td>
		</tr>
        <tr>
			<td valign=top><%= Bean.oper_schemeXML.getfieldTransl("desc_fn_oper_scheme_line", true) %></td><td valign=top><textarea name="desc_fn_oper_scheme_line" cols="74" rows="5" class="inputfield"><%=line.getValue("DESC_FN_OPER_SCHEME_LINE") %></textarea></td>
			<td valign=top><%= Bean.oper_schemeXML.getfieldTransl("note_fn_oper_scheme_line", false) %></td><td valign=top><textarea name="note_fn_oper_scheme_line" cols="74" rows="5" class="inputfield"><%=line.getValue("NOTE_FN_OPER_SCHEME_LINE") %></textarea></td>
		</tr>
		<%=	Bean.getCreationAndMoficationRecordFields(
				line.getValue(Bean.getCreationDateFieldName()),
				line.getValue("CREATED_BY"),
				line.getValue(Bean.getLastUpdateDateFieldName()),
				line.getValue("LAST_UPDATE_BY")
			) %>

		<tr>
			<td colspan="10" align="center">
				<%=Bean.getGoBackButton("../crm/finance/oper_schemespecs.jsp?id=" + scheme.getValue("ID_FN_OPER_SCHEME")) %>
			</td>
		</tr>
	</table>

<%   } 

if (Bean.currentMenu.isCurrentTabAndAccessPermitted("FINANCE_OPER_SCHEME_REPORTS")) { 
    bcReports report = new bcReports(Bean.getReportFormat());
  %>
	<%= report.getBKOperSchemeCheckReportHTML(id, id_report, find_string, l_report_page_beg, l_report_page_end, "N") %>
	<% if (!(id_report==null || "".equalsIgnoreCase(id_report))) { %>
		<br>
		<br>
		<table  <%=Bean.getTableDetailParam() %>>
			<tr>
				<%= Bean.getFindHTML("find_det", find_det, "../crm/finance/oper_scheme_linespecs.jsp?id=" + id + "&id_report="+id_report+"&report_det_page=1&") %>

				<%=Bean.getSelectOnChangeBeginHTML("state_line", "../crm/finance/oper_scheme_linespecs.jsp?id=" + id + "&id_report="+id_report+"&report_det_page=1", Bean.reportXML.getfieldTransl("state_line", false)) %>
					<%= Bean.getMeaningFromLookupNameOptions("REPORT_LINE_STATE", state_line, true) %>
				<%=Bean.getSelectOnChangeEndHTML() %>

				<%= Bean.getPagesHTML(pageFormName + tagReportDetail, "../crm/finance/oper_scheme_linespecs.jsp?id=" + id + "&id_report="+id_report+"&", "report_det_page") %>
			</tr>
		</table>
		<%= report.getCheckModelReportDetailHTML(id_report, find_det, state_line, l_report_det_page_beg, l_report_det_page_end) %>
	<% } %>
<% }

if (Bean.currentMenu.isCurrentTabAndAccessPermitted("FINANCE_OPER_SCHEME_RELATIONSHIPS")) { %> 		
	<td align="right" width="20">
		  	<select onchange="ajaxpage('../crm/finance/oper_scheme_linespecs.jsp?id=<%=id %>&relation_page=1&rel_kind='+this.value, 'div_main')" name="rel_kind" id="rel_kind" class="inputfield">
				<option value="CREATED" <% if ("CREATED".equalsIgnoreCase(rel_kind)) { %>SELECTED<% } %>><%= Bean.relationshipXML.getfieldTransl("h_created", false) %></option>
				<option value="NEEDED" <% if ("NEEDED".equalsIgnoreCase(rel_kind)) { %>SELECTED<% } %>><%= Bean.relationshipXML.getfieldTransl("h_needed", false) %></option>
			</select>&nbsp;
	<% String needComission = line.getRelationShipsNeededCount(line.getValue("CD_CLUB_REL_TYPE"));
	  
	  if (!(needComission.equalsIgnoreCase("0"))) {
		  %>
			<b><font color=red><%= Bean.relationshipXML.getfieldTransl("need_relationships_count", false) %> -  <%=needComission %></font></b><br>
		  <%
	  } else {
		  %>
			<b><font color=green><%= Bean.relationshipXML.getfieldTransl("all_relationships_was_created", false) %></font></b><br>
		  <%
	  }
	%>
		</td>
	<% if ("CREATED".equalsIgnoreCase(rel_kind)) { %>
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

		<%= line.getClubRelationshipsHTML(l_club_rel_page_beg, l_club_rel_page_end) %>
	<% } else { %>
		<%= line.getRelationShipsNeededHTML(line.getValue("CD_CLUB_REL_TYPE"), l_club_rel_page_beg, l_club_rel_page_end) %>
	<% } %>
<% }

}%>

</div></div>
</body>
</html>
