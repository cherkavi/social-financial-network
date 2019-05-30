<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />
<%@page import="bc.objects.bcAutoreconcilationObject"%>
<%@page import="java.util.HashMap"%><html>

<%= Bean.getLogOutScript(request) %>

<head>
	<%= Bean.getMetaContent() %>
	<%= Bean.getBottomFrameCSS() %>

</head>

<% 
HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

request.setCharacterEncoding("UTF-8");

String pageFormName = "FINANCE_AUTORECONCIL";

Bean.setJspPageForTabName(pageFormName);

String tagLines = "_LINES";
String tagLineFind = "_LINE_FIND";

String id = Bean.getDecodeParam(parameters.get("id"));
if (id==null || "".equalsIgnoreCase(id)) { id = ""; }

String	tab = Bean.getDecodeParam(parameters.get("tab"));
if (id==null || "".equalsIgnoreCase(id)) {
%>
	<%=Bean.getIDNotFoundMessage() %>
<% 
} else {
	if (tab==null || "".equalsIgnoreCase(tab)) { 
		tab = Bean.tabsHmGetValue(pageFormName);
	}
	Bean.tabsHmSetValue(pageFormName, tab);
	bcAutoreconcilationObject auto = new bcAutoreconcilationObject(id);

	Bean.currentMenu.setExistFlag("FINANCE_AUTORECONCIL_INFO", true);
	Bean.currentMenu.setExistFlag("FINANCE_AUTORECONCIL_LINES", true);

	//Обрабатываем номера страниц
	String l_lines_page = Bean.getDecodeParam(parameters.get("lines_page"));
	Bean.pageCheck(pageFormName + tagLines, l_lines_page);
	String l_lines_page_beg = Bean.getFirstRowNumber(pageFormName + tagLines);
	String l_lines_page_end = Bean.getLastRowNumber(pageFormName + tagLines);

	String line_find 	= Bean.getDecodeParam(parameters.get("line_find"));
	line_find 	= Bean.checkFindString(pageFormName + tagLineFind, line_find, l_lines_page);


%>

<body topmargin="0">
<div id="div_tabsheet">

	<table <%=Bean.getTableMenuParam() %>>
		<tr>
			<%= Bean.getPageHeader() %>

			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("FINANCE_AUTORECONCIL_INFO")){ %>
				<%= Bean.getMenuButton("RUN", "../crm/finance/autoreconcilupdate.jsp?type=general&action=run&process=no", "", "") %>
			<% } %>

			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("FINANCE_AUTORECONCIL_LINES")){ %>
				<%= Bean.getReportHyperLink("AUTORECONCIL_REP1", "ID_AUTORECONCILATION=" + id) %>
		
			    <!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagLines, "../crm/finance/autoreconcilspecs.jsp?id=" + id + "&tab="+Bean.currentMenu.getTabID("FINANCE_AUTORECONCIL_LINES")+"&", "lines_page") %>
			<% } %>
		</tr>
	</table>
	<table <%=Bean.getTableMenuParam() %>>
		<%= Bean.getDetailCaption(auto.getValue("ID_AUTORECONCILATION") + " - " + auto.getValue("DATE_AUTORECONCIL_FULL_FRMT")) %>
		<tr>
			<td>
				<!-- Выводим перечень закладок -->
				<%= Bean.currentMenu.getFormTabScheeds(tab, "../crm/finance/autoreconcilspecs.jsp?id=" + id) %>
			</td>
		</tr>
	</table>
</div>
<div id="div_data">
<div id="div_data_detail">
	
<%
if (Bean.currentMenu.isCurrentTabAndAccessPermitted("FINANCE_AUTORECONCIL_INFO")) { 
 %>
	<form>
	<table <%=Bean.getTableDetailParam() %>>
		<tr>
			<td><%=Bean.kvitovkaXML.getfieldTransl("id_autoreconcilation", false) %> </td><td><input type="text" name="id_autoreconcilation" size="25" value="<%= auto.getValue("ID_AUTORECONCILATION") %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.clubXML.getfieldTransl("club", false) %>
				<%= Bean.getGoToClubLink(auto.getValue("ID_CLUB")) %>
			</td><td><input type="text" name="name_club" size="30" value="<%= Bean.getClubShortName(auto.getValue("ID_CLUB")) %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td><%=Bean.kvitovkaXML.getfieldTransl("date_autoreconcilation", false) %></td> <td><input type="text" name="date_autoreconcilation" size="25" value="<%= auto.getValue("DATE_AUTORECONCIL_FULL_FRMT") %>" readonly="readonly" class="inputfield-ro"> </td>
			<td><%=Bean.kvitovkaXML.getfieldTransl("reconciled_lines_count", false) %></td> <td><input type="text" name="reconciled_lines_count" size="30" value="<%= auto.getValue("RECONCILED_LINES_COUNT") %>" readonly="readonly" class="inputfield-ro"> </td>
		</tr>
		<tr>
		    <td colspan="4" class="top_line"><b><%=Bean.commonXML.getfieldTransl("h_record_param", false)%></b></td>
		</tr>
		<tr>
			<td><%=Bean.commonXML.getfieldTransl("creation_date", false)%></td> <td><input type="text" name="creation_date" size="20" value="<%=auto.getValue(Bean.getCreationDateFieldName())%>" readonly="readonly" class="inputfield-ro"> </td>
			<td><%=Bean.commonXML.getfieldTransl("created_by", false)%></td> <td><input type="text" name="created_by" size="20" value="<%=Bean.getSystemUserName(auto.getValue("CREATED_BY"))%>" readonly="readonly" class="inputfield-ro"> </td>
		</tr>
		<tr>
			<td colspan="4" align="center">
				<%=Bean.getGoBackButton("../crm/finance/autoreconcil.jsp") %>
			</td>
		</tr>
	</table>
	</form>
<% } %>

<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("FINANCE_AUTORECONCIL_LINES")) { %>
	<table <%=Bean.getTableBottomFilter() %>>
	  	<tr>
		<%= Bean.getFindHTML("line_find", line_find, "../crm/finance/autoreconcilspecs.jsp?id=" + id + "&lines_page=1") %>

		<td>&nbsp;</td>
	  	</tr>
	</table>
	<%= auto.getAutoreconcilationLinesHTML(line_find, l_lines_page_beg, l_lines_page_end) %>
<%} %>


 <% 
} %>
</div></div>
</body>
</html>
