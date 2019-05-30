<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%= Bean.getLogOutScript(request) %>
<html>
<%

String pageFormName = "REPORTS_SELECTED";
String tagFind = "_FIND";

Bean.setJspPageForTabName(pageFormName);

request.setCharacterEncoding("UTF-8");

HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String specId 		= Bean.getDecodeParamPrepare(parameters.get("specId"));
String find_string 	= Bean.getDecodeParam(parameters.get("find_string"));
String l_page 		= Bean.getDecodeParam(parameters.get("page"));

find_string = Bean.checkFindString(pageFormName + tagFind, find_string, l_page);

//Обрабатываем номера страниц
Bean.pageCheck(pageFormName, l_page);
String l_page_beg = Bean.getFirstRowNumber(pageFormName);
String l_page_end = Bean.getLastRowNumber(pageFormName);

String print        = Bean.checkPrint(Bean.getDecodeParam(parameters.get("print")));

%>
<head>
	<%=Bean.getJSPHeadHTML(print) %>
</head>
<body>
<% if ("N".equalsIgnoreCase(print)) { %>
	<table <%= Bean.getTableMenuParam() %>>
		<tr>
			<%= Bean.getPageHeader() %>
		    <%= Bean.getMenuButton("PRINT", "/reports/rep_selected.jsp?print=Y", "", "") %>
	
		    <!-- Вывод страниц -->
			<%= Bean.getPagesHTML(pageFormName, "/reports/rep_selected.jsp?", "page") %>
		</tr>
	</table>
	<table <%= Bean.getTableMenuFilter() %>>
		<tr>
			<%= Bean.getFindHTML("find_string", find_string, "/reports/rep_selected.jsp?page=1&") %>
		</tr>
	</table>
<% } else { %>
	<table <%= Bean.getTableMenuParam() %>>
		<tr>
			<%= Bean.getPageHeaderPrint() %>
		</tr>
	</table>
<% } %>
<% if (Bean.hasAccessMenuPermission(pageFormName)) { %>
	<script type="text/javascript">
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
	<%= Bean.header.getReportsSelectedHTML(find_string, "", "", l_page_beg, l_page_end, print) %>
<%} %>
</body>

<%@page import="java.util.HashMap"%></html>