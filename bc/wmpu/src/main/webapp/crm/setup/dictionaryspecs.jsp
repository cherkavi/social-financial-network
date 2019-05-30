<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%@page import="bc.objects.bcDictionaryObject"%>
<%@page import="java.util.HashMap"%><html>

<%= Bean.getLogOutScript(request) %>

<head>
	<%= Bean.getMetaContent() %>
	<%= Bean.getBottomFrameCSS() %>
<title></title>
</head>
<body>
<div id="div_tabsheet">

<% 

HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String pageFormName = "SETUP_DICTIONARY";

Bean.setJspPageForTabName(pageFormName);

//Обрабатываем номера страниц
String l_page = Bean.getDecodeParam(parameters.get("page"));
Bean.pageCheck(pageFormName, l_page);
String l_beg = Bean.getFirstRowNumber(pageFormName);
String l_end = Bean.getLastRowNumber(pageFormName);

String view_name = Bean.getDecodeParam(parameters.get("view_name"));
String language = Bean.getDecodeParam(parameters.get("language"));
String	tab = Bean.getDecodeParam(parameters.get("tab"));

if (tab==null || "".equalsIgnoreCase(tab)) { tab = Bean.tabsHmGetValue(pageFormName); }
if (language==null || "".equalsIgnoreCase(language)) { 
	language = Bean.filtersHmGetValue(pageFormName);
    if ("".equalsIgnoreCase(language)) {
    	language = Bean.getLanguage();
    }
}

if (view_name==null || "".equalsIgnoreCase(view_name)) {
%>
	<%=Bean.getIDNotFoundMessage() %>
<% 
} else {
	Bean.tabsHmSetValue(pageFormName, tab);
	
	bcDictionaryObject dict = new bcDictionaryObject(view_name, language);

	if ("Y".equalsIgnoreCase(dict.getValue("HAS_TRANSLATE"))) {
		Bean.filtersHmSetValue(pageFormName, language);
	}

%>
	
	<table <%=Bean.getTableMenuParam() %>>
		<tr>
			<%= Bean.getPageHeader() %>

			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("SETUP_DICTIONARY_INFO")) { %>
				<% if (Bean.currentMenu.isCurrentTabAndEditPermitted("SETUP_DICTIONARY_INFO") &&
						"Y".equalsIgnoreCase(dict.getValue("UPDATED"))) { %>
				    <%= Bean.getMenuButton("ADD", "../crm/setup/dictionaryupdate.jsp?view_name=" + view_name + "&type=general&action=add&process=no", "", "") %>
				<% } %>
			    <!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName, "../crm/setup/dictionaryspecs.jsp?view_name=" + view_name + "&", "page") %>
			<% } %>
		</tr>
	</table>
	<table <%=Bean.getTableMenuParam() %>>
		<%= Bean.getDetailCaption(Bean.getMeaningFoCodeValue("DICTIONARY_NAME",dict.getValue("TABLE_NAME"))) %>
		<tr>
			<td>
				<!-- Выводим перечень закладок -->
				<%= Bean.currentMenu.getFormTabScheeds(tab, "../crm/setup/dictionaryspecs.jsp?view_name=" + view_name) %>
			</td>

			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("SETUP_DICTIONARY_INFO")) { %>
				<% if ("Y".equalsIgnoreCase(dict.getValue("HAS_TRANSLATE"))) { %>
				<td align="right" width=20>
			  		<%= Bean.dictionaryXML.getfieldTransl("language", false) %>
				</td>
				<td align="right" width=20>
			  		<select onchange="ajaxpage('../crm/setup/dictionaryspecs.jsp?view_name=<%=view_name %>&page=1&language='+getElementById('language').value, 'div_main')" name="language" id="language" class="inputfield"><%= Bean.getLanguageOptions(language, false) %></select>
				</td>
				<% } %>
	
			<% } %>
		</tr>
	</table>
</div>
<div id="div_data">
<div id="div_data_detail">

<%

if (Bean.currentMenu.isCurrentTabAndAccessPermitted("SETUP_DICTIONARY_INFO")) {
 %>
 <%= dict.getDictionaryContentHTML(dict.getValue("HAS_TRANSLATE"), l_beg, l_end) %>
<%
	/*
        Map<String, String> mp = Connector.getConnectionMap();
        for (String maps:mp.keySet()) {
        	html.append(maps + ": " + mp.get(maps));
        }*/
  %>
 <%
} 
}%>
</div></div>
</body>
</html>
