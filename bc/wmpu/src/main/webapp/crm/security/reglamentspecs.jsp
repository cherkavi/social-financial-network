<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />
<%@page import="bc.objects.bcReglamentObject"%>
<%@page import="java.util.HashMap"%>
<%@page import="bc.reports.bcReports"%>
<%@page import="bc.objects.bcSysLogObject"%><html>

<%= Bean.getLogOutScript(request) %>

<head>
	<%= Bean.getMetaContent() %>
	<%= Bean.getBottomFrameCSS() %>
<Head>


</head>
<body topmargin="0">
<div id="div_tabsheet">
<%

HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String pageFormName = "SECURITY_REGLAMENTS";

String tagReports = "_REPORTS";
String tagReportFind = "_REPORT_FIND";
String tagReportDetail = "_REPORT_DETAIL";
String tagReportLogFind = "_REPORT_LOG_FIND";
String tagReportLogRowType = "_REPORT_LOG_ROWTYPE";

Bean.setJspPageForTabName(pageFormName);

String reglamentid = Bean.getDecodeParam(parameters.get("id"));
String	tab = Bean.getDecodeParam(parameters.get("tab"));
if (tab==null || "".equalsIgnoreCase(tab)) { tab = Bean.tabsHmGetValue(pageFormName); }
if (reglamentid==null || "".equalsIgnoreCase(reglamentid)) {
%>
	<%=Bean.getIDNotFoundMessage() %>
<% 
} else {
	Bean.tabsHmSetValue(pageFormName, tab);
	bcReglamentObject reglement = new bcReglamentObject(reglamentid);
		
	String l_report_page = Bean.getDecodeParam(parameters.get("report_page"));
	Bean.pageCheck(pageFormName + tagReports, l_report_page);
	String l_report_page_beg = Bean.getFirstRowNumber(pageFormName + tagReports);
	String l_report_page_end = Bean.getLastRowNumber(pageFormName + tagReports);

	String report_find 	= Bean.getDecodeParam(parameters.get("report_find"));
	report_find 	= Bean.checkFindString(pageFormName + tagReportFind, report_find, l_report_page);

	String id_report = Bean.getDecodeParam(parameters.get("id_report"));
	
	String l_report_det_page = Bean.getDecodeParam(parameters.get("report_det_page"));
	
	if (id_report==null || "".equalsIgnoreCase(id_report)) {
		l_report_det_page = "1";
	}
	Bean.pageCheck(pageFormName + tagReportDetail, l_report_det_page);
	String l_report_det_page_beg = Bean.getFirstRowNumber(pageFormName + tagReportDetail);
	String l_report_det_page_end = Bean.getLastRowNumber(pageFormName + tagReportDetail);
	
	String log_row_type 	= Bean.getDecodeParam(parameters.get("log_row_type"));
	if ("0".equalsIgnoreCase(log_row_type)) {
		log_row_type = "";
	}
	log_row_type 	= Bean.checkFindString(pageFormName + tagReportLogRowType, log_row_type, l_report_det_page);

	String report_log_find 	= Bean.getDecodeParam(parameters.get("report_log_find"));
	report_log_find 	= Bean.checkFindString(pageFormName + tagReportLogFind, report_log_find, l_report_det_page);
%>

	<table <%=Bean.getTableMenuParam() %>>
		<tr>
			<%= Bean.getPageHeader() %>

			<% if (Bean.currentMenu.isCurrentTabAndEditPermitted("SECURITY_REGLAMENTS_INFO")) { %>
			    <%= Bean.getMenuButton("ADD", "../crm/security/reglamentsupdate.jsp?id=" + reglement.getValue("ID_JOB") + "&type=general&action=add2&process=no", "", "") %>
			    <%= Bean.getMenuButton("DELETE", "../crm/security/reglamentsupdate.jsp?id=" + reglement.getValue("ID_JOB") + "&type=general&action=remove&process=yes", Bean.reglamentXML.getfieldTransl("LAB_DEL_JOB", false), reglement.getValue("ID_JOB") + " - " + reglement.getValue("NAME_JOB")) %>
				<%= Bean.getMenuButton("RUN", "../crm/security/reglamentsupdate.jsp?id=" + reglement.getValue("ID_JOB") + "&type=general&action=run&process=yes", Bean.reglamentXML.getfieldTransl("LAB_RUN_JOB", false), reglement.getValue("ID_JOB") + " - " + reglement.getValue("NAME_JOB")) %>
			<%  } %>

			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("SECURITY_REGLAMENTS_REPORTS")) { %>
		
				<% if (id_report==null || "".equalsIgnoreCase(id_report)) { %>
				    <!-- Вывод страниц -->
					<%= Bean.getPagesHTML(pageFormName + tagReports, "../crm/security/reglamentspecs.jsp?id=" + reglamentid + "&tab="+Bean.currentMenu.getTabID("SECURITY_REGLAMENTS_REPORTS")+"&", "report_page") %>
				<% } %>
			<% } %>
		</tr>
	</table>
	<table <%=Bean.getTableMenuParam() %>>
		<%= Bean.getDetailCaption(reglement.getValue("ID_JOB") + " - " + reglement.getValue("NAME_JOB")) %>
		<tr>
			<td>
				<!-- Выводим перечень закладок -->
				<%= Bean.currentMenu.getFormTabScheeds(tab, "../crm/security/reglamentspecs.jsp?id=" + reglamentid) %>
			</td>
		</tr>
	</table>
</div>
<div id="div_data">
<div id="div_data_detail">
<%
if (Bean.currentMenu.isCurrentTabAndPreviewPermitted("SECURITY_REGLAMENTS_INFO")) {
 %>
	<table <%=Bean.getTableDetailParam() %>>
		<tr>
			<td><%= Bean.reglamentXML.getfieldTransl("id_job", false) %></td> <td><input type="text" name="id_job" size="10" value="<%= reglamentid %>" readonly="readonly" class="inputfield-ro"> </td>
			<td rowspan="3"><%= Bean.reglamentXML.getfieldTransl("desc_job", false) %></td> <td rowspan="3"><textarea name="desc_job" cols="100" rows="3" readonly="readonly" class="inputfield-ro"><%= reglement.getValue("DESC_JOB") %></textarea></td>
 		</tr>
		<tr>
			<td><%= Bean.reglamentXML.getfieldTransl("num_job", false) %></td> <td><input type="text" name="num_job" size="10" value="<%= reglement.getValue("NUM_JOB") %>" readonly="readonly" class="inputfield-ro"> </td>
		</tr>
		<tr>
			<td><%= Bean.reglamentXML.getfieldTransl("name_job", false) %></td> <td><input type="text" name="name_job" size="40" value="<%= reglement.getValue("NAME_JOB") %>" readonly="readonly" class="inputfield-ro"> </td>
		</tr>
 		<tr>
			<td><%= Bean.reglamentXML.getfieldTransl("next_date", false) %></td> <td><input type="text" name="next_date" size="40" value="<%= reglement.getValue("NEXT_DATE") %>" readonly="readonly" class="inputfield-ro"> </td>
			<td rowspan="3"><%= Bean.reglamentXML.getfieldTransl("what", false) %></td> <td rowspan="3"><textarea name="what" cols="100" rows="10" readonly="readonly" class="inputfield-ro"><%= reglement.getValue("WHAT") %></textarea></td>
		</tr>
		<tr>
			<td><%= Bean.reglamentXML.getfieldTransl("interval", false) %></td> <td><input type="text" name="interval" size="40" value="<%= reglement.getValue("INTERVAL") %>" readonly="readonly" class="inputfield-ro"> </td>
		</tr>
		<tr>
			<td><%= Bean.reglamentXML.getfieldTransl("is_enable", false) %></td> <td><input type="text" name="is_enable" size="10" value="<%= Bean.getMeaningFoCodeValue("YES_NO", reglement.getValue("IS_ENABLE")) %>" readonly="readonly" class="inputfield-ro"> </td>
		</tr>
		<%=	Bean.getCreationAndMoficationRecordFields(
				reglement.getValue(Bean.getCreationDateFieldName()),
				reglement.getValue("CREATED_BY"),
				reglement.getValue(Bean.getLastUpdateDateFieldName()),
				reglement.getValue("LAST_UPDATE_BY")
			) %>
		<tr>
			<td colspan="4" align="center">
				<%=Bean.getGoBackButton("../crm/security/reglaments.jsp") %>
			</td>
		</tr>
	</table>

<% } else if (Bean.currentMenu.isCurrentTabAndEditPermitted("SECURITY_REGLAMENTS_INFO")) { %>

    <form action="../crm/security/reglamentsupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST">
        <input type="hidden" name="action" value="edit">
        <input type="hidden" name="process" value="yes">
	<table <%=Bean.getTableDetailParam() %>>
		<tr>
			<td><%= Bean.reglamentXML.getfieldTransl("id_job", false) %></td> <td><input type="text" name="id_job" size="10" value="<%= reglamentid %>" readonly="readonly" class="inputfield-ro"> </td>
			<td rowspan="3"><%= Bean.reglamentXML.getfieldTransl("desc_job", false) %></td> <td rowspan="3"><textarea name="desc_job" cols="100" rows="3" class="inputfield"><%= reglement.getValue("DESC_JOB") %></textarea></td>
 		</tr>
 		<tr>
			<td><%= Bean.reglamentXML.getfieldTransl("num_job", false) %></td> <td><input type="text" name="num_job" size="10" value="<%= reglement.getValue("NUM_JOB") %>" readonly="readonly" class="inputfield-ro"> </td>
		</tr>
		<tr>
			<td><%= Bean.reglamentXML.getfieldTransl("name_job", false) %></td> <td><input type="text" name="name_job" size="40" value="<%= reglement.getValue("NAME_JOB") %>" class="inputfield"> </td>
		</tr>
 		<tr>
			<td><%= Bean.reglamentXML.getfieldTransl("next_date", false) %></td> <td><input type="text" name="next_date" size="40" value="<%= reglement.getValue("NEXT_DATE") %>" class="inputfield"> </td>
			<td rowspan="3"><%= Bean.reglamentXML.getfieldTransl("what", false) %></td> <td rowspan="3"><textarea name="what" cols="100" rows="10" class="inputfield"><%= reglement.getValue("WHAT") %></textarea></td>
		</tr>
		<tr>
			<td><%= Bean.reglamentXML.getfieldTransl("interval", false) %></td> <td><input type="text" name="interval" size="40" value="<%= reglement.getValue("INTERVAL") %>" class="inputfield"> </td>
		</tr>
 		<tr>
			<td><%= Bean.reglamentXML.getfieldTransl("is_enable", false) %></td> <td><select name="is_enable" class="inputfield"><%=Bean.getMeaningFromLookupNameOptions("YES_NO", reglement.getValue("IS_ENABLE"), false) %></select></td>
		</tr>
		<%=	Bean.getCreationAndMoficationRecordFields(
				reglement.getValue(Bean.getCreationDateFieldName()),
				reglement.getValue("CREATED_BY"),
				reglement.getValue(Bean.getLastUpdateDateFieldName()),
				reglement.getValue("LAST_UPDATE_BY")
			) %>
		<tr>
			<td colspan="4" align="center">
				<%=Bean.getSubmitButtonAjax("../crm/security/reglamentsupdate.jsp") %>
				<%=Bean.getResetButton() %>
				<%=Bean.getGoBackButton("../crm/security/reglaments.jsp") %>
			</td>
		</tr>
	</table>
	</form>
<%	}

	if (Bean.currentMenu.isCurrentTabAndAccessPermitted("SECURITY_REGLAMENTS_REPORTS")) { 
		bcReports report = new bcReports(Bean.getReportFormat());
		
		%>
	
		<table <%=Bean.getTableBottomFilter() %>><tbody>
		<tr>
			<%=Bean.getFindHTML("report_find", report_find, "../crm/security/reglamentspecs.jsp?id=" + reglamentid + "&report_page=1&id_report=") %>
		
			<td>&nbsp;</td>
	
		</tr>
		</tbody>
		</table>			
	
		<%= report.getReportHeaderHTML("'REGLAMENT'", reglamentid, report_find, id_report, "", "../crm/security/reglamentspecs.jsp?id=" + reglamentid, l_report_page_beg, l_report_page_end, "N") %>
		<br>
		<% if (!(id_report==null || "".equalsIgnoreCase(id_report))) { %>
			<br>
				<table  <%=Bean.getTableBottomFilter() %>>
				<tr>
					<%=Bean.getFindHTML("report_log_find", report_log_find, "../crm/security/reglamentspecs.jsp?id=" + reglamentid + "&id_report="+id_report+"&tab="+Bean.currentMenu.getTabID("SECURITY_REGLAMENTS_REPORTS")+"&report_det_page=1") %>

					<%=Bean.getSelectOnChangeBeginHTML("log_row_type", "../crm/security/reglamentspecs.jsp?id=" + reglamentid + "&id_report="+id_report+"&tab="+Bean.currentMenu.getTabID("SECURITY_REGLAMENTS_REPORTS")+"&report_det_page=1", Bean.syslogXML.getfieldTransl("row_type", false)) %>
						<%= Bean.getSelectOptionHTML(log_row_type, "0", "") %>
						<%= Bean.getMeaningFromLookupNameOptions("SYS_LOG_ROW_TYPE", log_row_type, false) %>
					<%=Bean.getSelectOnChangeEndHTML() %>

					<%= Bean.getPagesHTML(pageFormName + tagReportDetail, "../crm/security/reglamentspecs.jsp?id=" + reglamentid + "&id_report="+id_report+"&tab="+Bean.currentMenu.getTabID("SECURITY_REGLAMENTS_REPORTS")+"&", "report_det_page") %>
				</tr>
				</table>
	
				<% bcSysLogObject log = new bcSysLogObject(); %>
	
				<%= log.getSysLogHTML(report_log_find, "", "", "", "", "", "", id_report, log_row_type, l_report_det_page_beg, l_report_det_page_end) %>

		<% } %>
	
	<% } 

}
%>

</div></div>
</body>
</html>
