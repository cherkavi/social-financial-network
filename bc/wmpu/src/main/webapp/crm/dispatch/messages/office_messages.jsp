<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%= Bean.getLogOutScript(request) %>

<%@page import="java.util.HashMap"%><html>
<%

request.setCharacterEncoding("UTF-8");

String pageFormName = "DISPATCH_MESSAGES_OFFICE";
String tagFind = "_FIND";
String tagOperationType = "_OPERATION_TYPE";
String tagState = "_STATE";
String tagIsArchive = "_IS_ARCHIVE";
String tagDispatchKind = "_DISPATCH_KIND";
String tagMessageOperType = "_MESSAGE_OPER_TYPE";

Bean.setJspPageForTabName(pageFormName);

HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String specId 		= Bean.getDecodeParamPrepare(parameters.get("specId"));
String find_string 	= Bean.getDecodeParam(parameters.get("find_string"));
String l_page 		= Bean.getDecodeParam(parameters.get("page"));

find_string = Bean.checkFindString(pageFormName + tagFind, find_string, l_page);

String operation_type 	= Bean.getDecodeParam(parameters.get("operation_type"));
operation_type 			= Bean.checkFindString(pageFormName + tagOperationType, operation_type, l_page);

String cd_state			= Bean.getDecodeParam(parameters.get("cd_state"));
cd_state 				= Bean.checkFindString(pageFormName + tagState, cd_state, l_page);

String dispatch_kind	= Bean.getDecodeParam(parameters.get("dispatch_kind"));
dispatch_kind 			= Bean.checkFindString(pageFormName + tagDispatchKind, dispatch_kind, l_page);

String message_oper_type	= Bean.getDecodeParam(parameters.get("message_oper_type"));
message_oper_type			= Bean.checkFindString(pageFormName + tagMessageOperType, message_oper_type, l_page);

String is_archive	= Bean.getDecodeParam(parameters.get("is_archive"));
is_archive 		= Bean.checkFindString(pageFormName + tagIsArchive, is_archive, l_page);
if (is_archive==null || "".equalsIgnoreCase(is_archive)) {
	is_archive = "";
}

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
		    <%= Bean.getMenuButton("ADD", "../crm/dispatch/messages/office_messagesupdate.jsp?type=general&action=add&process=no", "", "") %>
			<% } %>
		    <%= Bean.getMenuButton("PRINT", "../crm/dispatch/messages/office_messages.jsp?print=Y", "", "") %>
	
		    <!-- Вывод страниц -->
			<%= Bean.getPagesHTML(pageFormName, "../crm/dispatch/messages/office_messages.jsp?", "page") %>
		</tr>
	</table>
	<table <%= Bean.getTableMenuFilter() %>>
		<tr>
			<%= Bean.getFindHTML("find_string", find_string, "../crm/dispatch/messages/office_messages.jsp?page=1&") %>
	
			<%=Bean.getSelectOnChangeBeginHTML("message_oper_type", "../crm/dispatch/messages/office_messages.jsp?page=1", Bean.messageXML.getfieldTransl("name_ds_message_oper_type", false)) %>
				<%= Bean.getDispatchMessageOperationTypeOptions(message_oper_type, true) %>
			<%=Bean.getSelectOnChangeEndHTML() %>

			<%=Bean.getSelectOnChangeBeginHTML("dispatch_kind", "../crm/dispatch/messages/office_messages.jsp?page=1", Bean.messageXML.getfieldTransl("name_ds_sender_dispatch_kind", false)) %>
				<%= Bean.getDispatchKindOptions(dispatch_kind, true) %>
			<%=Bean.getSelectOnChangeEndHTML() %>

			<%=Bean.getSelectOnChangeBeginHTML("cd_state", "../crm/dispatch/messages/office_messages.jsp?page=1", Bean.smsXML.getfieldTransl("name_sms_state", false)) %>
				<%= Bean.getMeaningFromLookupNameOrderByNymberValueOptions("SEND_MESSAGE_STATE", cd_state, true) %>
			<%=Bean.getSelectOnChangeEndHTML() %>

			<%=Bean.getSelectOnChangeBeginHTML("is_archive", "../crm/dispatch/messages/office_messages.jsp?page=1", Bean.smsXML.getfieldTransl("is_archive", false)) %>
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

<% Bean.header.setDeleteHyperLink("","",""); %>

<% if (Bean.hasAccessMenuPermission(pageFormName)) {
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
		           	         		if (confirm('<%= Bean.messageXML.getfieldTransl("h_confirm_delete", false) %>'))  {
		           						return true;
		           					} else {
		            					return false;
		           					}
		           	         	} else if (operType == 'cancel') {
		           	         		return true;
		           	        	} else if (operType == 'to_archive') {
		        	         		return true;
		           	        	} else if (operType == 'from_archive') {
		        	         		return true;
		        	         	}
		               			
		           			}
		           		}
		           	}
		       		alert('<%= Bean.messageXML.getfieldTransl("h_not_entered_messages", false) %>');
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
	<%= Bean.header.getDispatchOfficeMessagesHeaderHTML(operation_type, find_string, dispatch_kind, message_oper_type, cd_state, is_archive, l_beg, l_end, print) %>
<%  
} %>
</div></div>
</body>
</html>