<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%= Bean.getLogOutScript(request) %>

<%@page import="java.util.HashMap"%><html>
<%

request.setCharacterEncoding("UTF-8");

String pageFormName = "DISPATCH_MESSAGES_SMS";
String tagDispatchKind = "_DISPATCH_KIND";
String tagType = "_TYPE";
String tagState = "_STATE";
String tagFind = "_FIND";
String tagIsArchive = "_IS_ARCHIVE";
String tagOperationType = "_OPERATION_TYPE";

Bean.setJspPageForTabName(pageFormName);

HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String specId 		= Bean.getDecodeParamPrepare(parameters.get("specId"));
String find_string 	= Bean.getDecodeParam(parameters.get("find_string"));
String l_page 		= Bean.getDecodeParam(parameters.get("page"));
String cd_type 		= Bean.getDecodeParam(parameters.get("cd_type"));
String cd_state		= Bean.getDecodeParam(parameters.get("cd_state"));
String cd_kind		= Bean.getDecodeParam(parameters.get("cd_kind"));
String is_archive	= Bean.getDecodeParam(parameters.get("is_archive"));

find_string 	= Bean.checkFindString(pageFormName + tagFind, find_string, l_page);
cd_type 		= Bean.checkFindString(pageFormName + tagType, cd_type, l_page);
cd_state 		= Bean.checkFindString(pageFormName + tagState, cd_state, l_page);
cd_kind 		= Bean.checkFindString(pageFormName + tagDispatchKind, cd_kind, l_page);
is_archive 		= Bean.checkFindString(pageFormName + tagIsArchive, is_archive, l_page);
if (is_archive==null || "".equalsIgnoreCase(is_archive)) {
	is_archive = "";
}

String operation_type 	= Bean.getDecodeParam(parameters.get("operation_type"));
operation_type 			= Bean.checkFindString(pageFormName + tagOperationType, operation_type, l_page);

Bean.pageCheck(pageFormName, l_page);
String l_beg = Bean.getFirstRowNumber(pageFormName);
String l_end = Bean.getLastRowNumber(pageFormName);

String print        = Bean.checkPrint(Bean.getDecodeParam(parameters.get("print")));

%>
<head>
	<%=Bean.getJSPHeadHTML(print) %>
</head>
<body>
<div id="div_tabsheet">
<% if ("N".equalsIgnoreCase(print)) { %>
	<table <%= Bean.getTableMenuParam() %>>
		<tr>
			<%= Bean.getPageHeader() %>

			<% if (Bean.hasEditMenuPermission(pageFormName)) { %>
		    <%= Bean.getMenuButton("ADD", "../crm/dispatch/messages/smsupdate.jsp?type=general&action=add&process=no", "", "") %>
			<% } %>
		    <%= Bean.getMenuButton("PRINT", "../crm/dispatch/messages/sms.jsp?print=Y", "", "") %>

		    <!-- Вывод страниц -->
			<%= Bean.getPagesHTML(pageFormName, "../crm/dispatch/messages/sms.jsp?", "page") %>
		</tr>
	</table>
	<table <%= Bean.getTableMenuFilter() %>>
		<tr>
			<%= Bean.getFindHTML("find_string", find_string, "../crm/dispatch/messages/sms.jsp?page=1&") %>
	
			<%=Bean.getSelectOnChangeBeginHTML("cd_kind", "../crm/dispatch/messages/sms.jsp?page=1", Bean.smsXML.getfieldTransl("name_dispatch_kind", false)) %>
				<%= Bean.getDispatchKindOptions(cd_kind, true) %>
			<%=Bean.getSelectOnChangeEndHTML() %>

			<%=Bean.getSelectOnChangeBeginHTML("cd_type", "../crm/dispatch/messages/sms.jsp?page=1", Bean.smsXML.getfieldTransl("name_sms_message_type", false)) %>
				<%= Bean.getSMSTypeOptions(cd_type, true) %>
			<%=Bean.getSelectOnChangeEndHTML() %>
	
			<%=Bean.getSelectOnChangeBeginHTML("cd_state", "../crm/dispatch/messages/sms.jsp?page=1", Bean.smsXML.getfieldTransl("name_sms_state", false)) %>
				<%= Bean.getSMSStateOptions(cd_state, true) %>
			<%=Bean.getSelectOnChangeEndHTML() %>

			<%=Bean.getSelectOnChangeBeginHTML("is_archive", "../crm/dispatch/messages/sms.jsp?page=1", Bean.smsXML.getfieldTransl("is_archive", false)) %>
				<%=Bean.getSelectOptionHTML(is_archive, "", "") %>
				<%=Bean.getSelectOptionHTML(is_archive, "Y", Bean.messageXML.getfieldTransl("in_archive", false)) %>
				<%=Bean.getSelectOptionHTML(is_archive, "N", Bean.messageXML.getfieldTransl("not_in_archive", false)) %>
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
	if ("-1".equalsIgnoreCase(cd_type)) {
		cd_type = "";
	}
	if (Bean.hasEditMenuPermission(pageFormName)) {
%>
     <script type="text/javascript">
     	function CheckSelect(form)  {
         	var operType = document.getElementById('operation_type').value;
         	//alert (operType);
        	for (i = 0; i < form.elements.length; i++) {
           		var item = form.elements[i];
           		if (item.name.substr(0,3) == "chb")  {
           			if (item.checked)  {
           	         	if (operType == 'send') {
           	         		return true;
           	         	} else if (operType == 'delete') {
           	         		if (confirm('<%= Bean.smsXML.getfieldTransl("h_confirm_delete", false) %>'))  {
           						return true;
           					} else {
            					return false;
           					}
           	         	} else if (operType == 'prepare') {
           	         		return true;
           	        	} else if (operType == 'to_archive') {
        	         		return true;
           	        	} else if (operType == 'from_archive') {
        	         		return true;
           	        	} else if (operType == 'delivered') {
        	         		return true;
           	        	} else if (operType == 'error') {
        	         		return true;
        	         	}
               			
           			}
           		}
           	}
       		alert('<%= Bean.smsXML.getfieldTransl("h_not_entered_sms", false) %>');
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
<%= Bean.header.getSMSHTML(operation_type, find_string, cd_kind, is_archive, cd_type, cd_state, l_beg, l_end, print) %>
<%} %>
</div></div>
</body>
</html>