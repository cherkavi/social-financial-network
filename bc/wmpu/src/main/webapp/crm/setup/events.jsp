<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%= Bean.getLogOutScript(request) %>

<%@page import="java.util.HashMap"%><html>
<%

request.setCharacterEncoding("UTF-8");

String pageFormName = "SETUP_EVENTS";
String tagFind = "_FIND";
String tagType = "_TYPE";

Bean.setJspPageForTabName(pageFormName);

HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String specId 		= Bean.getDecodeParamPrepare(parameters.get("specId"));
String find_string 	= Bean.getDecodeParam(parameters.get("find_string"));
String l_page 		= Bean.getDecodeParam(parameters.get("page"));
String id_type 		= Bean.getDecodeParam(parameters.get("id_type"));

find_string 	= Bean.checkFindString(pageFormName + tagFind, find_string, l_page);
id_type 		= Bean.checkFindString(pageFormName + tagType, id_type, l_page);

Bean.pageCheck(pageFormName, l_page);
String l_beg = Bean.getFirstRowNumber(pageFormName);
String l_end = Bean.getLastRowNumber(pageFormName);

String print        = Bean.checkPrint(Bean.getDecodeParam(parameters.get("print")));
%>
<head>
	<%=Bean.getJSPHeadHTML(print) %>
</head>
<body>

<% StringBuilder html = new StringBuilder();
   html.append(Bean.header.getEventsHTML(find_string, id_type, l_beg, l_end, print));
%>
<div id="div_tabsheet">
<% if ("N".equalsIgnoreCase(print)) { %>
	<table <%= Bean.getTableMenuParam() %>>
		<tr>
			<%= Bean.getPageHeader() %>
			<% if (Bean.hasEditMenuPermission(pageFormName)) { %>
				<%= Bean.getMenuButton("DELETE", "../crm/setup/eventsupdate.jsp?action=delete_pattern&process=no", "", "", Bean.eventXML.getfieldTransl("h_delete_event_for_pattern", false)) %>
			<% } %>

		    <%= Bean.getMenuButton("PRINT", "../crm/setup/events.jsp?print=Y", "", "") %>
	
		    <!-- Вывод страниц -->
			<%= Bean.getPagesHTML(pageFormName, "../crm/setup/events.jsp?", "page", Bean.header.getLastResultSetRowCount()) %>
		</tr>
	</table>
	<table <%= Bean.getTableMenuFilter() %>>
		<tr>
			<%= Bean.getFindHTML("find_string", find_string, "../crm/setup/events.jsp?page=1&") %>
	
			<%=Bean.getSelectOnChangeBeginHTML("id_type", "../crm/setup/events.jsp?page=1", Bean.eventXML.getfieldTransl("desc_event_type", false)) %>
				<%= Bean.getEventTypeOptions(id_type, true) %>
			<%=Bean.getSelectOnChangeEndHTML() %>
		</tr>
	</table>
<% } else { %>
	<table <%= Bean.getTableMenuParam() %>>
		<tr>
			<%= Bean.getPageHeaderPrint() %>
		</tr>
	</table>
<% } %>
</div>
<div id="div_data">
<div id="div_data_detail">
<% if (Bean.hasAccessMenuPermission(pageFormName)) { 
	if (Bean.hasEditMenuPermission(pageFormName)) {
%>
     <script type="text/javascript">
     	function CheckSelect(form)  {
        	for (i = 0; i < form.elements.length; i++) {
           		var item = form.elements[i];
           		if (item.name.substr(0,3) == "chb")  {
           			if (item.checked)  {
           				if (confirm("<%= Bean.eventXML.getfieldTransl("t_confirm_delete", false) %>?"))  {
           					return true;
           				} else {
            				return false;
           				}
           			}
           		}
           	}
       		alert("<%= Bean.eventXML.getfieldTransl("t_events_not_selected", false) %>");
        	return false;
   	 	}

   	 	function CheckCB(Element) {
	   	 	myCheck = true;
	
			thisCheckBoxes = document.getElementsByTagName('input');
			for (i = 1; i < thisCheckBoxes.length; i++) { 
				myName = thisCheckBoxes[i].name;
				if (myName.substr(0,3) == 'chb'){
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
   				
   				if (myName.substr(0,3) == Name){
 					thisCheckBoxes[i].checked = Element.checked;
   				}
   	 		}
   	 	}
   	 </script>
<% } %>

	<%= html.toString() %>
<%} %>
</div></div>
</body>
</html>