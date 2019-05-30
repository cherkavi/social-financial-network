<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%@page import="bc.objects.bcEventObject"%>
<%@page import="java.util.HashMap"%><html>

<%= Bean.getLogOutScript(request) %>

<head>
	<%= Bean.getMetaContent() %>
	<%= Bean.getBottomFrameCSS() %>
<Head>

<%

HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String pageFormName = "SETUP_EVENTS";

Bean.setJspPageForTabName(pageFormName);

String eventid = Bean.getDecodeParam(parameters.get("id"));
String	tab = Bean.getDecodeParam(parameters.get("tab"));
if (tab==null || "".equalsIgnoreCase(tab)) { tab = Bean.tabsHmGetValue(pageFormName); }
if (eventid==null) { eventid="0"; }
if (eventid.equals("0")) { 
	// 	<jsp:include flush="true" page="../time.html"></jsp:include>
}else {
	Bean.tabsHmSetValue(pageFormName, tab);
	bcEventObject event = new bcEventObject(eventid);
%>
</head>
<body topmargin="0">
<div id="div_tabsheet">
	<table <%=Bean.getTableMenuParam() %>>
		<tr>
			<%= Bean.getPageHeader() %>
			<% if (Bean.currentMenu.isCurrentTabAndEditPermitted("SETUP_EVENTS_INFO")) { %>
			   	<%= Bean.getMenuButton("DELETE", "../crm/setup/eventsupdate.jsp?chb" + event.getValue("ID_EVENT") + "=Y&type=general&action=delete&process=yes", Bean.eventXML.getfieldTransl("t_delete_event", false) + " \\\'" + event.getValue("ID_EVENT") + "\\\'", "") %>
			<% } %>
		</tr>
	</table>
	<table <%=Bean.getTableMenuParam() %>>
		<%= Bean.getDetailCaption(event.getValue("ID_EVENT") + " - " + event.getValue("DESC_EVENT_TYPE")) %>
		<tr>
			<td>
				<!-- Выводим перечень закладок -->
				<%= Bean.currentMenu.getFormTabScheeds(tab, "../crm/setup/eventspecs.jsp?id=" + eventid) %>
			</td>

	</tr>
</table>
</div>
<div id="div_data">
<div id="div_data_detail">
<%
if (Bean.currentMenu.isCurrentTabAndAccessPermitted("SETUP_EVENTS_INFO")) {
  boolean hasReport = false;
  if (!(event.getValue("ID_REPORT")== null || "".equalsIgnoreCase(event.getValue("ID_REPORT")))) {
	  hasReport = true;
  }
 %>
	<form>
	<table <%=Bean.getTableDetailParam() %>>
		<tr>
			<td><%= Bean.eventXML.getfieldTransl("DESC_EVENT_TYPE", false) %> </td> <td align="left"><input type="text" name="desc_event_type" size="30" value="<%= event.getValue("DESC_EVENT_TYPE") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td><%= Bean.eventXML.getfieldTransl("DESC_EVENT", false) %></td> <td><textarea name="desc_event" cols="90" rows="3" readonly="readonly" class="inputfield-ro"><%= event.getValue("DESC_EVENT") %></textarea></td>
		</tr>
		<tr>
			<td><%= Bean.eventXML.getfieldTransl("PARAM_EVENT", false) %></td> <td><textarea name="param_event" cols="90" rows="7" readonly="readonly" class="inputfield-ro"><%= event.getValue("PARAM_EVENT") %></textarea></td>
		</tr>
		<% if (hasReport) {%>
		<tr>
			<td><%= Bean.reportXML.getfieldTransl("ID_REPORT", false) %> </td><td><input type="text" name="id_report" size="25" value="<%=event.getValue("ID_REPORT") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td><%= Bean.reportXML.getfieldTransl("TYPE_REPORT", false) %> </td><td><input type="text" name="type_report" size="45" value="<%=event.getValue("TYPE_REPORT_TSL") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td><%= Bean.reportXML.getfieldTransl("DATE_REPORT", false) %> </td><td><input type="text" name="date_report" size="45" value="<%=event.getValue("DATE_REPORT_FRMT") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>		
		<tr>
			<td><%= Bean.reportXML.getfieldTransl("STATE_REPORT", false) %> </td><td><input type="text" name="state_report" size="45" value="<%=event.getValue("STATE_REPORT_TSL") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<% } %>
		<%=	Bean.getIdCreationAndMoficationRecordFieldsOneColl(
				event.getValue("ID_EVENT"),
				event.getValue(Bean.getCreationDateFieldName()),
				event.getValue("CREATED_BY"),
				"",
				""
			) %>
		<tr>
			<td colspan="6" align="center">
				<%=Bean.getGoBackButton("../crm/setup/events.jsp") %>
			</td>
		</tr>


	</table>
	</form>

<%  }

%>

<%   } %>
</div></div>
</body>
</html>
