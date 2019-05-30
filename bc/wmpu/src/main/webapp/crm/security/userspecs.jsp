<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />
<%@page import="bc.objects.bcUserObject"%>
<%@page import="java.util.HashMap"%><html>

<%= Bean.getLogOutScript(request) %>

<head>
	<%= Bean.getMetaContent() %>
	<%= Bean.getBottomFrameCSS() %> 
<Head>

</head>
<body>
<div id="div_tabsheet">


<%

HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String pageFormName = "SECURITY_USERS";

Bean.setJspPageForTabName(pageFormName);

String tagRoles = "_ROLES";
String tagRoleFind = "_ROLE_FIND";
String tagRoleModuleType = "_ROLE_MODULE_TYPE";
String tagRoleSelected = "_ROLE_SELECTED";

String tagReport = "_REPORTS";
String tagReportFind = "_FIND_REPORTS";
String tagReportModuleType = "_REPORT_MODULE_TYPE";
String tagReportKind = "_REPORT_KIND";

String tagMenuFind = "_MENU_FIND";
String tagMenuModuleType = "_MENU_MODULE_TYPE";
String tagMenuAccessType = "_MENU_ACCESS_TYPE";

String tagSystemParamFind = "_SYSTEM_PARAM_FIND";

String tagJurPersons = "_JUR_PRS";
String tagJurPersonFind = "_JUR_PRS_FIND";
String tagJurPrsSelected = "_JUR_PRS_SELECTED";

String tagConnections = "_CONNECTIONS";
String tagConnectionFind = "_CONNECTION_FIND";
String tagConnectionType = "_CONNECTION_TYPE";
String tagConnectionModule = "_CONNECTION_MODULE";

String tagSession = "_SESSION";
String tagSessionFind = "_SESSION_FIND";

String tagClubPriv = "_CLUB_PRIV";
String tagClubPrivFind = "_CLUB_PRIV_FIND";
String tagClubPrivSelected = "_CLUB_PRIV_SELECTED";

String tagAction = "_ACTIONS";
String tagActionFind = "_ACTIONS_FIND";

String tagTrans = "_TRANS";
String tagTransType = "_TRANS_TYPE";
String tagTransState = "_TRANS_STATE";
String tagTransPayType = "_TRANS_PAY_TYPE";
String tagTransFind = "_FIND";

String tagTermUser = "_TERM_USER";
String tagTermUserFind = "_TERMINAL_USER_FIND";
String tagTermUserAccessType = "_TERM_USER_ACCESS_TYPE";
String tagTermUserStatus = "_TERM_USER_STATUS";

String tagTermMessages = "_TERM_MESSAGES";
String tagTermMessagesFind = "_TERM_MESSAGES_FIND";

String userid = Bean.getDecodeParam(parameters.get("id"));
String	tab = Bean.getDecodeParam(parameters.get("tab"));
if (tab==null || "".equalsIgnoreCase(tab)) { tab = Bean.tabsHmGetValue(pageFormName); }
if (userid==null || "".equalsIgnoreCase(userid)) {
%>
	<%=Bean.getIDNotFoundMessage() %>
<% 
} else { 
	Bean.tabsHmSetValue(pageFormName, tab);
	bcUserObject user = new bcUserObject(userid);
	
	//Обрабатываем номера страниц
	String l_role_page = Bean.getDecodeParam(parameters.get("role_page"));
	Bean.pageCheck(pageFormName + tagRoles, l_role_page);
	String l_role_page_beg = Bean.getFirstRowNumber(pageFormName + tagRoles);
	String l_role_page_end = Bean.getLastRowNumber(pageFormName + tagRoles);
	
	String role_find 	= Bean.getDecodeParam(parameters.get("role_find"));
	role_find 	= Bean.checkFindString(pageFormName + tagRoleFind, role_find, l_role_page);
	
	String role_module_type 	= Bean.getDecodeParam(parameters.get("role_module_type"));
	role_module_type 	= Bean.checkFindString(pageFormName + tagRoleModuleType, role_module_type, l_role_page);
	
	String role_selected 	= Bean.getDecodeParam(parameters.get("role_selected"));
	role_selected 	= Bean.checkFindString(pageFormName + tagRoleSelected, role_selected, l_role_page);
	
	String menu_find 	= Bean.getDecodeParam(parameters.get("menu_find"));
	menu_find 	= Bean.checkFindString(pageFormName + tagMenuFind, menu_find, "");
	
	String menu_module_type 	= Bean.getDecodeParam(parameters.get("menu_module_type"));
	menu_module_type 	= Bean.checkFindString(pageFormName + tagMenuModuleType, menu_module_type, "");
	
	String menu_access_type 	= Bean.getDecodeParam(parameters.get("menu_access_type"));
	menu_access_type 	= Bean.checkFindString(pageFormName + tagMenuAccessType, menu_access_type, "");
	
	String system_param_find 	= Bean.getDecodeParam(parameters.get("system_param_find"));
	system_param_find 	= Bean.checkFindString(pageFormName + tagSystemParamFind, system_param_find, "");

	String l_report_page = Bean.getDecodeParam(parameters.get("report_page"));
	Bean.pageCheck(pageFormName + tagReport, l_report_page);
	String l_report_page_beg = Bean.getFirstRowNumber(pageFormName + tagReport);
	String l_report_page_end = Bean.getLastRowNumber(pageFormName + tagReport);

	String find_report 	= Bean.getDecodeParam(parameters.get("find_report"));
	find_report 	= Bean.checkFindString(pageFormName + tagReportFind, find_report, l_report_page);

	String module_type = Bean.getDecodeParam(parameters.get("module_type"));
	module_type 		= Bean.checkFindString(pageFormName + tagReportModuleType, module_type, l_report_page);

	String report_kind = Bean.getDecodeParam(parameters.get("report_kind"));
	report_kind 		= Bean.checkFindString(pageFormName + tagReportKind, report_kind, l_report_page);

	String l_jur_page = Bean.getDecodeParam(parameters.get("jur_page"));
	Bean.pageCheck(pageFormName + tagJurPersons, l_jur_page);
	String l_jur_page_beg = Bean.getFirstRowNumber(pageFormName + tagJurPersons);
	String l_jur_page_end = Bean.getLastRowNumber(pageFormName + tagJurPersons);
	
	String jur_prs_find 	= Bean.getDecodeParam(parameters.get("jur_prs_find"));
	jur_prs_find 	= Bean.checkFindString(pageFormName + tagJurPersonFind, jur_prs_find, l_jur_page);

	String jur_prs_selected 	= Bean.getDecodeParam(parameters.get("jur_prs_selected"));
	jur_prs_selected 	= Bean.checkFindString(pageFormName + tagJurPrsSelected, jur_prs_selected, l_jur_page);

	String l_club_priv_page = Bean.getDecodeParam(parameters.get("club_priv_page"));
	Bean.pageCheck(pageFormName + tagClubPriv, l_club_priv_page);
	String l_club_priv_page_beg = Bean.getFirstRowNumber(pageFormName + tagClubPriv);
	String l_club_priv_page_end = Bean.getLastRowNumber(pageFormName + tagClubPriv);
	
	String club_find 	= Bean.getDecodeParam(parameters.get("club_find"));
	club_find 	= Bean.checkFindString(pageFormName + tagClubPrivFind, club_find, l_club_priv_page);

	String club_selected 	= Bean.getDecodeParam(parameters.get("club_selected"));
	club_selected 	= Bean.checkFindString(pageFormName + tagClubPrivSelected, club_selected, l_club_priv_page);

	String l_connections_page = Bean.getDecodeParam(parameters.get("connections_page"));
	Bean.pageCheck(pageFormName + tagConnections, l_connections_page);
	String l_connections_page_beg = Bean.getFirstRowNumber(pageFormName + tagConnections);
	String l_connections_page_end = Bean.getLastRowNumber(pageFormName + tagConnections);

	String find_connection 	= Bean.getDecodeParam(parameters.get("find_connection"));
	find_connection 	= Bean.checkFindString(pageFormName + tagConnectionFind, find_connection, l_connections_page);

	String connection_module 	= Bean.getDecodeParam(parameters.get("connection_module"));
	connection_module 	= Bean.checkFindString(pageFormName + tagConnectionModule, connection_module, l_connections_page);

	String l_session_page = Bean.getDecodeParam(parameters.get("session_page"));
	Bean.pageCheck(pageFormName + tagSession, l_session_page);
	String l_session_page_beg = Bean.getFirstRowNumber(pageFormName + tagSession);
	String l_session_page_end = Bean.getLastRowNumber(pageFormName + tagSession);

	String find_session 	= Bean.getDecodeParam(parameters.get("find_session"));
	find_session 	= Bean.checkFindString(pageFormName + tagSessionFind, find_session, l_session_page);

	String connection_type 	= Bean.getDecodeParam(parameters.get("connection_type"));
	connection_type 	= Bean.checkFindString(pageFormName + tagConnectionType, connection_type, l_connections_page);
	if (Bean.isEmpty(connection_type)) {
		connection_type = "CONNECTION";
		connection_type 	= Bean.checkFindString(pageFormName + tagConnectionType, connection_type, l_connections_page);
	}

	String l_action_page = Bean.getDecodeParam(parameters.get("action_page"));
	Bean.pageCheck(pageFormName + tagAction, l_action_page);
	String l_action_page_beg = Bean.getFirstRowNumber(pageFormName + tagAction);
	String l_action_page_end = Bean.getLastRowNumber(pageFormName + tagAction);

	String find_action 	= Bean.getDecodeParam(parameters.get("find_action"));
	find_action 	= Bean.checkFindString(pageFormName + tagActionFind, find_action, l_action_page);

	String l_trans_page = Bean.getDecodeParam(parameters.get("trans_page"));
	Bean.pageCheck(pageFormName + tagTrans, l_trans_page);
	String l_trans_page_beg = Bean.getFirstRowNumber(pageFormName + tagTrans);
	String l_trans_page_end = Bean.getLastRowNumber(pageFormName + tagTrans);

	String find_trans 	= Bean.getDecodeParam(parameters.get("find_trans"));
	find_trans 	= Bean.checkFindString(pageFormName + tagTransFind, find_trans, l_trans_page);

	String type_trans 	= Bean.getDecodeParam(parameters.get("type_trans"));
	type_trans 	= Bean.checkFindString(pageFormName + tagTransType, type_trans, l_trans_page);

	String state_trans 	= Bean.getDecodeParam(parameters.get("state_trans"));
	state_trans 	= Bean.checkFindString(pageFormName + tagTransState, state_trans, l_trans_page);

	String pay_type_trans 	= Bean.getDecodeParam(parameters.get("pay_type_trans"));
	pay_type_trans 	= Bean.checkFindString(pageFormName + tagTransPayType, pay_type_trans, l_trans_page);
	
	String l_term_user_page = Bean.getDecodeParam(parameters.get("term_user_page"));
	Bean.pageCheck(pageFormName + tagTermUser, l_term_user_page);
	String l_term_user_page_beg = Bean.getFirstRowNumber(pageFormName + tagTermUser);
	String l_term_user_page_end = Bean.getLastRowNumber(pageFormName + tagTermUser);

	String term_user_find 	= Bean.getDecodeParam(parameters.get("term_user_find"));
	term_user_find 	= Bean.checkFindString(pageFormName + tagTermUserFind, term_user_find, l_term_user_page);

	String term_user_access_type 	= Bean.getDecodeParam(parameters.get("term_user_access_type"));
	term_user_access_type 	= Bean.checkFindString(pageFormName + tagTermUserAccessType, term_user_access_type, l_term_user_page);
	
	String term_user_status 	= Bean.getDecodeParam(parameters.get("term_user_status"));
	term_user_status 	= Bean.checkFindString(pageFormName + tagTermUserStatus, term_user_status, l_term_user_page);
	
	String l_term_message_page = Bean.getDecodeParam(parameters.get("term_message_page"));
	Bean.pageCheck(pageFormName + tagTermMessages, l_term_message_page);
	String l_term_message_page_beg = Bean.getFirstRowNumber(pageFormName + tagTermMessages);
	String l_term_message_page_end = Bean.getLastRowNumber(pageFormName + tagTermMessages);

	String term_message_find 	= Bean.getDecodeParam(parameters.get("term_message_find"));
	term_message_find 	= Bean.checkFindString(pageFormName + tagTermMessagesFind, term_message_find, l_term_message_page);

%>

	<table <%=Bean.getTableMenuParam() %>>
		<tr>
			<%= Bean.getPageHeader() %>

			<% if (Bean.currentMenu.isCurrentTabAndEditPermitted("SECURITY_USERS_INFO")) { %>
			    <%= Bean.getMenuButton("ADD", "../crm/security/usersupdate.jsp?id=" + user.getValue("ID_USER") + "&type=general&action=add2&process=no", "", "") %>
			    <%= Bean.getMenuButton("DELETE", "../crm/security/usersupdate.jsp?id=" + user.getValue("ID_USER") + "&type=general&action=remove&process=yes", Bean.userXML.getfieldTransl("LAB_DELETE_USER", false), user.getValue("ID_USER") + " - " + user.getValue("NAME_USER")) %>
			<% } %>
	
			<% if (Bean.currentMenu.isCurrentTabAndEditPermitted("SECURITY_USERS_PRIVILEGES")) { %>
			    <!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagRoles, "../crm/security/userspecs.jsp?id=" + userid + "&tab="+Bean.currentMenu.getTabID("SECURITY_USERS_PRIVILEGES")+"&", "role_page") %>
			<% } %>
	
			<% if (Bean.currentMenu.isCurrentTabAndEditPermitted("SECURITY_USERS_REPORTS")) { %>
			    <!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagReport, "../crm/security/userspecs.jsp?id=" + userid + "&tab="+Bean.currentMenu.getTabID("SECURITY_USERS_REPORTS")+"&", "report_page") %>
			<% } %>
	
			<% if (Bean.currentMenu.isCurrentTabAndEditPermitted("SECURITY_USERS_TRANSACTIONS")) { %>
			    <!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagTrans, "../crm/security/userspecs.jsp?id=" + userid + "&tab="+Bean.currentMenu.getTabID("SECURITY_USERS_TRANSACTIONS")+"&", "trans_page") %>
			<% } %>
	
			<% if (Bean.currentMenu.isCurrentTabAndEditPermitted("SECURITY_USERS_CLUB_PRIV")) { %>
			    <!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagClubPriv, "../crm/security/userspecs.jsp?id=" + userid + "&tab="+Bean.currentMenu.getTabID("SECURITY_USERS_CLUB_PRIV")+"&", "club_priv_page") %>
			<% } %>

			<% if (Bean.currentMenu.isCurrentTabAndEditPermitted("SECURITY_USERS_JUR_PRS")) { %>
			    <!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagJurPersons, "../crm/security/userspecs.jsp?id=" + userid + "&tab="+Bean.currentMenu.getTabID("SECURITY_USERS_JUR_PRS")+"&", "jur_page") %>
			<% } %>

			<% if (Bean.currentMenu.isCurrentTabAndEditPermitted("SECURITY_USERS_CONNECTIONS")) { %>
			    <!-- Вывод страниц -->
				<% if ("CONNECTION".equalsIgnoreCase(connection_type)) { %>
				<%= Bean.getPagesHTML(pageFormName + tagConnections, "../crm/security/userspecs.jsp?id=" + userid + "&connection_type="+connection_type + "&tab="+Bean.currentMenu.getTabID("SECURITY_USERS_CONNECTIONS")+"&", "connections_page") %>
				<% } else { %>
				<%= Bean.getPagesHTML(pageFormName + tagSession, "../crm/security/userspecs.jsp?id=" + userid + "&connection_type="+connection_type + "&tab="+Bean.currentMenu.getTabID("SECURITY_USERS_CONNECTIONS")+"&", "session_page") %>
				<% } %>
			<% } %>

			<% if (Bean.currentMenu.isCurrentTabAndEditPermitted("SECURITY_USERS_ACTIONS")) { %>
			    <!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagAction, "../crm/security/userspecs.jsp?id=" + userid + "&tab="+Bean.currentMenu.getTabID("SECURITY_USERS_ACTIONS")+"&", "action_page") %>
			<% } %>

			<% if (Bean.currentMenu.isCurrentTabAndEditPermitted("SECURITY_USERS_TERMINALS")) { %>
			    <% if (Bean.currentMenu.isCurrentTabAndEditPermitted("SECURITY_USERS_TERMINALS")) { %>
				    <%= Bean.getMenuButtonBase("ADD", "../crm/clients/terminaluserupdate.jsp?back_type=USER&type=user&id=" + userid + "&id_user=" + userid + "&id_nat_prs=" + user.getValue("ID_NAT_PRS") + "&id_nat_prs_role=" + user.getValue("ID_NAT_PRS") + "&action=addlist&process=no", "", "", "", "div_data_detail") %>
				<% } %>
			    <!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagTermUser, "../crm/security/userspecs.jsp?id=" + userid + "&tab="+Bean.currentMenu.getTabID("SECURITY_USERS_TERMINALS")+"&", "term_user_page") %>
			<% } %>

			<% if (Bean.currentMenu.isCurrentTabAndEditPermitted("SECURITY_USERS_TERM_MESSAGES")) { %>
			    <!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagTermMessages, "../crm/security/userspecs.jsp?id=" + userid + "&tab="+Bean.currentMenu.getTabID("SECURITY_USERS_TERM_MESSAGES")+"&", "term_message_page") %>
			<% } %>
		</tr>
	</table>
	<table <%=Bean.getTableMenuParam() %>>
		<%= Bean.getDetailCaption(user.getValue("NAME_USER") + (!Bean.isEmpty(user.getValue("FIO_NAT_PRS"))?" ("+user.getValue("FIO_NAT_PRS")+")":"")) %>
		<tr>
			<td>
				<!-- Выводим перечень закладок -->
				<%= Bean.currentMenu.getFormTabScheeds(tab, "../crm/security/userspecs.jsp?id=" + userid) %>
			</td>
		</tr>
	</table>
</div>
<div id="div_data">
<div id="div_data_detail">

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
	function CheckAll(Element,Name) {
		thisCheckBoxes = document.getElementsByTagName('input');
		for (i = 1; i < thisCheckBoxes.length; i++) { 
			myName = thisCheckBoxes[i].name;
			
			if (myName.substr(0,6) == Name){
					thisCheckBoxes[i].checked = document.getElementById('mainCheck').checked;
			}
		}
	}
	CheckCB();
</script>
<%
if (Bean.currentMenu.isCurrentTabAndPreviewPermitted("SECURITY_USERS_INFO")) {
 %>

	<form>
	<table <%=Bean.getTableDetailParam() %>> 
	    <tr>
			<td><%= Bean.userXML.getfieldTransl("NAME_USER", false) %> </td><td><input type="text" name="NAME_USER" size="20" value="<%= user.getValue("NAME_USER") %>" readonly="readonly" class="inputfield-ro"></td>
			<td rowspan="4"><%= Bean.userXML.getfieldTransl("permit_ip", false) %> </td>
			<td rowspan="4">
				<select id="list_ip" name="list_ip" size="4" class="inputfield-ro" style="width: 200px !important;;overflow-y: scroll; height: auto !important;" ><%=user.getPermittedIPOptions() %></select>
			</td>
		</tr>
		<tr>
			<td><%= Bean.userXML.getfieldTransl("CD_USER_STATUS", false) %></td> <td><input type="text" name="NAME_USER_STATUS" size="50" value="<%= user.getValue("NAME_USER_STATUS") %>" readonly="readonly" class="inputfield-ro"> </td>
		</tr>
		<tr>
			<td><%=Bean.userXML.getfieldTransl("fio_nat_prs", false)%>
				<%=Bean.getGoToContactPersonLink(user.getValue("ID_NAT_PRS_ROLE")) %>
			</td>
			<td>
				<input type="text" name="fio_nat_prs" size="50" value="<%= user.getValue("FIO_NAT_PRS") %>" readonly="readonly" class="inputfield-ro">
			</td>
		</tr>
		<% if (!Bean.isEmpty(user.getValue("ID_NAT_PRS_ROLE"))) { %>
		<tr>
			<td><%= Bean.userXML.getfieldTransl("SNAME_SERVICE_PLACE_WORK", false) %>
				<%=Bean.getGoToServicePlaceLink(user.getValue("ID_SERVICE_PLACE_WORK")) %>
			</td><td><input type="text" name="SNAME_SERVICE_PLACE_WORK" size="50" value="<%= user.getValue("SNAME_SERVICE_PLACE_WORK") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td><%= Bean.userXML.getfieldTransl("name_post", false) %> </td><td><input type="text" name="NAME_POST" size="50" value="<%= user.getValue("NAME_POST") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<% } %>
	    <tr>
			<td><%= Bean.userXML.getfieldTransl("DESC_USER", false) %></td> <td><textarea name="DESC_USER" cols="47" rows="3" readonly="readonly" class="inputfield-ro"><%= user.getValue("DESC_USER") %></textarea></td>
		</tr>
	    <tr>
			<td colspan="2">&nbsp;</td>
		</tr>
		<%=	Bean.getIdCreationAndMoficationRecordFields(
				user.getValue("ID_USEr"),
				user.getValue(Bean.getCreationDateFieldName()),
				user.getValue("CREATED_BY"),
				user.getValue(Bean.getLastUpdateDateFieldName()),
				user.getValue("LAST_UPDATE_BY")
			) %>
		<tr>
			<td colspan="4" align="center">
				<%=Bean.getGoBackButton("../crm/security/users.jsp") %>
			</td>
		</tr>
	</table>
	</form>

<% } else if (Bean.currentMenu.isCurrentTabAndEditPermitted("SECURITY_USERS_INFO")) { %>
	<script language="JavaScript">
	    var needPassword = 0;
		function checkUserStatus() {
			var prevStatus = '<%=user.getValue("CD_USER_STATUS") %>';
			var curStatus = document.getElementById('user_status').value;
			if (prevStatus=='DELETED') {
				if (prevStatus != curStatus) {
					document.getElementById('span_password').innerHTML = '<%= Bean.userXML.getfieldTransl("password", true) %>';
					document.getElementById('span_confirm_password').innerHTML = '<%= Bean.userXML.getfieldTransl("confirm_password", false) %>';
					needPassword = 1;
				} else {
					document.getElementById('span_password').innerHTML = '<%= Bean.userXML.getfieldTransl("password", false) %>';
					document.getElementById('span_confirm_password').innerHTML = '<%= Bean.userXML.getfieldTransl("confirm_password", false) %>';
					needPassword = 0;
				}
			} else {
				document.getElementById('span_password').innerHTML = '<%= Bean.userXML.getfieldTransl("password", false) %>';
				document.getElementById('span_confirm_password').innerHTML = '<%= Bean.userXML.getfieldTransl("confirm_password", false) %>';
				needPassword = 0;
			}
		}
		
	</script>
	<script>
		var formData = new Array (
			new Array ('CD_USER_STATUS', 'varchar2', 1)
		);
		var formPassword = new Array (
			new Array ('PASSWORD', 'varchar2', 1),
			new Array ('CONFIRM_PASSWORD', 'varchar2', 1)
		);
		function myValidateForm(){
			if (needPassword==1) {
				formData = formData.concat(formPassword);
			}
			var select, form;
			form = document.getElementById('updateForm');
			select = document.getElementById('list_ip');
			try {
				if (select.options.length > 0) {
		    		var elem = document.createElement('input');
					elem.type = 'hidden';
					elem.name = 'ipcount';
					elem.value = select.options.length;
					form.appendChild(elem);
			  		for (i=0; i < select.options.length; i++) {
			    		var elem = document.createElement('input');
						elem.type = 'hidden';
						elem.name = 'ip_'+i;
						elem.value = select.options[i].text;
						form.appendChild(elem);
			  		}
				}
			} catch (e) {alert(e);}
			return validateForm(formData);
		}
		function addElementToSelect(id, value) {
			var select, option;
			if (!checkIP(value)) {
				return false;
			}
			select = document.getElementById(id);
			for (i=0; i < select.options.length; i++) {
				if (validIp == select.options[i].text) {
					return false;
				}
			}
			option = document.createElement('option');
			option.value = option.text = validIp;
			select.appendChild(option);
			return true;
		}
		var validIp = '';
		function addIp() {
			if (addElementToSelect('list_ip', document.getElementById('new_ip').value)) {
			}
		}
		function removeIp() {
			var select, current;
			select = document.getElementById('list_ip');
			current = select.selectedIndex;
			//alert(current);
			if (current != -1) {
				select.options.remove(current);
			}
		}
		function checkIP(ip) {
			validIp = '';
		    var x = ip.split("."), x1, x2, x3, x4;

		    if (x.length == 4) {
		        x1 = parseInt(x[0], 10);
		        x2 = parseInt(x[1], 10);
		        x3 = parseInt(x[2], 10);
		        x4 = parseInt(x[3], 10);

		        if (isNaN(x1) || isNaN(x2) || isNaN(x3) || isNaN(x4)) {
		            return false;
		        }

		        if ((x1 >= 0 && x1 <= 255) && (x2 >= 0 && x2 <= 255) && (x3 >= 0 && x3 <= 255) && (x4 >= 0 && x4 <= 255)) {
		        	validIp = x1 + '.' + x2 + '.' + x3 + '.' + x4;
		            return true;
		        }
		    }
		    alert("You have entered an invalid IP address!");
		    return false;
		}
	</script>

    <form action="../crm/security/usersupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return myValidateForm()">
        <input type="hidden" name="action" value="edit">
        <input type="hidden" name="process" value="yes">
        <input type="hidden" name="id" value="<%=user.getValue("ID_USER") %>">
	<table <%=Bean.getTableDetailParam() %>>
		<tr>
			<td><%= Bean.userXML.getfieldTransl("NAME_USER", false) %> </td><td><input type="text" name="NAME_USER" size="20" value="<%= user.getValue("NAME_USER") %>" readonly="readonly" class="inputfield-ro"></td>
			<td rowspan="5"><%= Bean.userXML.getfieldTransl("permit_ip", false) %> </td>
			<td rowspan="5">
				<select id="list_ip" name="list_ip" size="4" class="inputfield" style="width: 200px !important;;overflow-y: scroll; height: auto !important;" ><%=user.getPermittedIPOptions() %></select>
				<span style="width: 100px; vertical-align: top; display: inline-block; height: 50px ! important;">
					<input type="button" class="inputfield" style="width:30px !important" value="-" onclick="removeIp();">
				</span>
				<div id="add_ip"><input type="text" id="new_ip" class="inputfield" size="20"><input type="button" class="inputfield" value="Добавить" onclick="addIp();"></div>
			</td>
		</tr>
	    <tr>
			<td><span id="span_password"><%= Bean.userXML.getfieldTransl("password", false) %></span></td><td><input type="password" name="PASSWORD" size="20" value="" class="inputfield"></td>
		</tr>	
		<tr>
			<td><span id="span_confirm_password"><%= Bean.userXML.getfieldTransl("confirm_password", false) %></span></td><td><input type="password" name="CONFIRM_PASSWORD" size="20" value="" class="inputfield"></td>
		</tr>	
		<tr>
			<td><%= Bean.userXML.getfieldTransl("CD_USER_STATUS", true) %></td> <td><select name="CD_USER_STATUS" id='user_status' class="inputfield" onchange="checkUserStatus()"><%=Bean.getUserStatusOptions(user.getValue("CD_USER_STATUS"), true) %></select></td>
		</tr>		
		<tr>
			<td><%=Bean.userXML.getfieldTransl("fio_nat_prs", false)%>
				<%=Bean.getGoToContactPersonLink(user.getValue("ID_NAT_PRS_ROLE")) %>
			</td>
			<td>
				<%=Bean.getWindowFindNatPrsRole("nat_prs_role", user.getValue("ID_NAT_PRS_ROLE"), user.getValue("FIO_NAT_PRS"), "40") %>
			</td>
		</tr>	
		<% if (!Bean.isEmpty(user.getValue("ID_NAT_PRS_ROLE"))) { %>
		<tr>
			<td><%= Bean.userXML.getfieldTransl("SNAME_SERVICE_PLACE_WORK", false) %>
				<%=Bean.getGoToServicePlaceLink(user.getValue("ID_SERVICE_PLACE_WORK")) %>
			</td><td><input type="text" name="SNAME_SERVICE_PLACE_WORK" size="50" value="<%= user.getValue("SNAME_SERVICE_PLACE_WORK") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td><%= Bean.userXML.getfieldTransl("name_post", false) %> </td><td><input type="text" name="NAME_POST" size="50" value="<%= user.getValue("NAME_POST") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<% } %>
		<tr>
			<td><%= Bean.userXML.getfieldTransl("DESC_USER", false) %></td> <td><textarea name="DESC_USER" cols="47" rows="3" class="inputfield"><%=user.getValue("DESC_USER") %></textarea></td>
		<tr>
			<td colspan="2">&nbsp;</td>
		</tr>
		<%=	Bean.getIdCreationAndMoficationRecordFields(
				user.getValue("ID_USER"),
				user.getValue(Bean.getCreationDateFieldName()),
				user.getValue("CREATED_BY"),
				user.getValue(Bean.getLastUpdateDateFieldName()),
				user.getValue("LAST_UPDATE_BY")
			) %>
		<tr>
			<td colspan="6" align="center">
				<%=Bean.getSubmitButtonAjax("../crm/security/usersupdate.jsp") %>
				<%=Bean.getResetButton() %>
				<%=Bean.getGoBackButton("../crm/security/users.jsp") %>
			</td>
		</tr>
	</table>

	</form>
<% 
}
if (Bean.currentMenu.isCurrentTabAndAccessPermitted("SECURITY_USERS_PRIVILEGES")) {%>
	<table <%= Bean.getTableBottomFilter() %>>
		<tr>
			<%= Bean.getFindHTML("role_find", role_find, "../crm/security/userspecs.jsp?id=" + userid + "&role_page=1") %>
		
			<%=Bean.getSelectOnChangeBeginHTML("role_module_type", "../crm/security/userspecs.jsp?id=" + userid + "&role_page=1", Bean.roleXML.getfieldTransl("cd_module_type", false)) %>
				<%= Bean.getSysModuleTypeOptions(role_module_type, true) %>
			<%=Bean.getSelectOnChangeEndHTML() %>


		<%=Bean.getSelectOnChangeBeginHTML("role_selected", "../crm/security/userspecs.jsp?id=" + userid + "&role_page=1&role_selected=", Bean.commonXML.getfieldTransl("h_chosen", false)) %>
			<%=Bean.getSelectOptionHTML(role_selected, "", "") %>
			<%=Bean.getSelectOptionHTML(role_selected, "0", Bean.commonXML.getfieldTransl("h_not_chosen", false)) %>
			<%=Bean.getSelectOptionHTML(role_selected, "1", Bean.commonXML.getfieldTransl("h_chosen", false)) %>
		<%=Bean.getSelectOnChangeEndHTML() %>
		</tr>
	</table>

	 <%= user.editUserRolesHTML(role_find, role_module_type, role_selected, l_role_page_beg, l_role_page_end) %>
<%	
}
if (Bean.currentMenu.isCurrentTabAndAccessPermitted("SECURITY_USERS_MENU")) {%>
		<table <%=Bean.getTableBottomFilter() %>><tbody>
		<tr>
			<%=Bean.getFindHTML("menu_find", menu_find, "../crm/security/userspecs.jsp?id=" + userid + "&menu_page=1") %>
		
			<%=Bean.getSelectOnChangeBeginHTML("menu_module_type", "../crm/security/userspecs.jsp?id=" + userid + "&menu_page=1", Bean.roleXML.getfieldTransl("cd_module_type", false)) %>
				<%= Bean.getSysModuleTypeOptions(menu_module_type, true) %>
			<%=Bean.getSelectOnChangeEndHTML() %>

			<%=Bean.getSelectOnChangeBeginHTML("menu_access_type", "../crm/security/userspecs.jsp?id=" + userid + "&menu_page=1", Bean.syslogXML.getfieldTransl("row_type", false)) %>
				<%= Bean.getPrivilegeTypeOptions(menu_access_type, true) %>
			<%=Bean.getSelectOnChangeEndHTML() %>

		</tr>
		</tbody>
		</table>
	<%= user.getUserMenuHTML(menu_find, menu_module_type, menu_access_type) %>
<%}
if (Bean.currentMenu.isCurrentTabAndAccessPermitted("SECURITY_USERS_PARAM")) {%>
		<table <%=Bean.getTableBottomFilter() %>><tbody>
		<tr>
			<%=Bean.getFindHTML("system_param_find", system_param_find, "../crm/security/userspecs.jsp?id=" + userid + "&system_param_page=1") %>
		
			<td>&nbsp;</td>
		</tr>
		</tbody>
		</table>
	<%=user.getUserParamHTML(system_param_find) %>
<%}
if (Bean.currentMenu.isCurrentTabAndAccessPermitted("SECURITY_USERS_REPORTS")) {%>
	<table <%= Bean.getTableBottomFilter() %>>
		<tr>
			<%= Bean.getFindHTML("find_report", find_report, "../crm/security/userspecs.jsp?id=" + userid + "&report_page=1") %>
	
			<%=Bean.getSelectOnChangeBeginHTML("module_type", "../crm/security/userspecs.jsp?id=" + userid + "&report_page=1", Bean.reportXML.getfieldTransl("cd_module_type", false)) %>
			 	<%= Bean.getSysModuleTypeOptions(module_type, true) %>
			<%=Bean.getSelectOnChangeEndHTML() %>
	
			<%=Bean.getSelectOnChangeBeginHTML("report_kind", "../crm/security/userspecs.jsp?id=" + userid + "&report_page=1", Bean.reportXML.getfieldTransl("cd_report_kind", false)) %>
			 	<%= Bean.getReportKindOptions(report_kind, true) %>
			<%=Bean.getSelectOnChangeEndHTML() %>
		</tr>
	</table>
	<%=user.getUserReportsHTML(find_report, module_type, report_kind, l_report_page_beg, l_report_page_end, "", "") %>
<%}
if (Bean.currentMenu.isCurrentTabAndAccessPermitted("SECURITY_USERS_TRANSACTIONS")) {%>
<table <%= Bean.getTableBottomFilter() %>>
	<tr>
		<%= Bean.getFindHTML("find_trans", find_trans, "../crm/security/userspecs.jsp?id=" + userid + "&trans_page=1") %>
	
	 	<%=Bean.getSelectOnChangeBeginHTML("type_trans", "../crm/cards/transactions.jsp?page=1", Bean.transactionXML.getfieldTransl("type_trans", false)) %>
	 		<%= Bean.getTransTypeOptions(type_trans, true) %>
	 	<%=Bean.getSelectOnChangeEndHTML() %>

	 	<%=Bean.getSelectOnChangeBeginHTML("pay_type_trans", "../crm/cards/transactions.jsp?page=1", Bean.transactionXML.getfieldTransl("pay_type", false)) %>
	 		<%= Bean.getTransPayTypeOptions(pay_type_trans, true) %>
	 	<%=Bean.getSelectOnChangeEndHTML() %>
	
	 	<%=Bean.getSelectOnChangeBeginHTML("state_trans", "../crm/cards/transactions.jsp?page=1", Bean.transactionXML.getfieldTransl("state_trans", false)) %>
	 		<%= Bean.getTransStateOptions(state_trans, true) %>
	 	<%=Bean.getSelectOnChangeEndHTML() %>
	</tr>
</table>
<%=user.getUserTransactionsHTML(find_trans, type_trans, state_trans, pay_type_trans, l_trans_page_beg, l_trans_page_end) %>
<%}
if (Bean.currentMenu.isCurrentTabAndAccessPermitted("SECURITY_USERS_JUR_PRS")) {%>
	<table <%= Bean.getTableBottomFilter() %>>
		<tr>
			<%= Bean.getFindHTML("jur_prs_find", jur_prs_find, "../crm/security/userspecs.jsp?id=" + userid + "&jur_page=1") %>
		<td align="right">
			<b><font color="green"><%= Bean.userXML.getfieldTransl("l_has_jur_prs_permission", false) %>&nbsp;-&nbsp;<%= Bean.getUserJurPrsPermissionCount(userid) %></font></b>
		</td>
		<%=Bean.getSelectOnChangeBeginHTML("jur_prs_selected", "../crm/security/userspecs.jsp?id=" + userid + "&jur_page=1&jur_prs_selected=", Bean.commonXML.getfieldTransl("h_chosen", false)) %>
			<%=Bean.getSelectOptionHTML(jur_prs_selected, "", "") %>
			<%=Bean.getSelectOptionHTML(jur_prs_selected, "N", Bean.commonXML.getfieldTransl("h_not_chosen", false)) %>
			<%=Bean.getSelectOptionHTML(jur_prs_selected, "Y", Bean.commonXML.getfieldTransl("h_chosen", false)) %>
		<%=Bean.getSelectOnChangeEndHTML() %>
		</tr>
	</table>


	<%=user.getUserJurPrsPrivilegesHTML(jur_prs_find, jur_prs_selected, l_jur_page_beg, l_jur_page_end) %>
<%}
if (Bean.currentMenu.isCurrentTabAndAccessPermitted("SECURITY_USERS_CLUB_PRIV")) {%>
	<table <%= Bean.getTableBottomFilter() %>>
		<tr>
			<%= Bean.getFindHTML("club_find", club_find, "../crm/security/userspecs.jsp?id=" + userid + "&club_priv_page=1") %>
		<%=Bean.getSelectOnChangeBeginHTML("club_selected", "../crm/security/userspecs.jsp?id=" + userid + "&club_priv_page=1&jur_prs_selected=", Bean.commonXML.getfieldTransl("h_chosen", false)) %>
			<%=Bean.getSelectOptionHTML(club_selected, "", "") %>
			<%=Bean.getSelectOptionHTML(club_selected, "N", Bean.commonXML.getfieldTransl("h_not_chosen", false)) %>
			<%=Bean.getSelectOptionHTML(club_selected, "Y", Bean.commonXML.getfieldTransl("h_chosen", false)) %>
		<%=Bean.getSelectOnChangeEndHTML() %>
		</tr>
	</table>
	
	<%=user.getUserClubPrivilegesHTML(club_find, club_selected, l_jur_page_beg, l_jur_page_end) %>
<%}
if (Bean.currentMenu.isCurrentTabAndAccessPermitted("SECURITY_USERS_CONNECTIONS")) {%>
	<table <%= Bean.getTableBottomFilter() %>>
		<tr>
			<% if ("CONNECTION".equalsIgnoreCase(connection_type)) { %>
			<%= Bean.getFindHTML("find_connection", find_connection, "../crm/security/userspecs.jsp?id=" + userid + "&connection_type="+connection_type + "&connections_page=1") %>

			<%=Bean.getSelectOnChangeBeginHTML("connection_module", "../crm/security/userspecs.jsp?id=" + userid + "&connections_page=1", Bean.roleXML.getfieldTransl("cd_module_type", false)) %>
				<%= Bean.getSysModuleTypeOptions(connection_module, true) %>
			<%=Bean.getSelectOnChangeEndHTML() %>

			<% } else { %>
			<%= Bean.getFindHTML("find_session", find_session, "../crm/security/userspecs.jsp?id=" + userid + "&connection_type="+connection_type + "&connections_page=1") %>
			<% } %>
			
			<%=Bean.getSelectOnChangeBeginHTML("connection_type", "../crm/security/userspecs.jsp?id=" + userid + "&connection_type=", Bean.userXML.getfieldTransl("title_connection_type", false)) %>
				<%=Bean.getSelectOptionHTML(connection_type, "CONNECTION", Bean.userXML.getfieldTransl("title_connection", false)) %>
				<%=Bean.getSelectOptionHTML(connection_type, "SESSION", Bean.userXML.getfieldTransl("title_session", false)) %>
			<%=Bean.getSelectOnChangeEndHTML() %>
		</tr>
	</table>
	<% if ("CONNECTION".equalsIgnoreCase(connection_type)) { %>
	<%=user.getConnectionsHTML(find_connection, connection_module, l_connections_page_beg, l_connections_page_end) %>
	<% } else { %>
	<%=user.getSessionHTML(find_session, l_session_page_beg, l_session_page_end) %>
	<% } %>
<%}

if (Bean.currentMenu.isCurrentTabAndAccessPermitted("SECURITY_USERS_ACTIONS")) {%>

<table <%= Bean.getTableBottomFilter() %>>
	<tr>
	<%= Bean.getFindHTML("find_action", find_connection, "../crm/security/userspecs.jsp?id=" + userid + "&action_page=1") %>
	</tr>
</table>
<%=user.getActionsHTML(find_action, l_action_page_beg, l_action_page_end) %>
<%}

if (Bean.currentMenu.isCurrentTabAndAccessPermitted("SECURITY_USERS_TERMINALS")) {%>

<table <%= Bean.getTableBottomFilter() %>>
	<tr>
		<%= Bean.getFindHTML("term_user_find", term_user_find, "../crm/security/userspecs.jsp?id=" + userid + "&term_user_page=1") %>

		<%=Bean.getSelectOnChangeBeginHTML("term_user_access_type", "../crm/security/userspecs.jsp?id=" + userid + "&term_user_page=1", Bean.contactXML.getfieldTransl("name_term_user_access_type", false)) %>
			<%= Bean.getTermUserAccessTypeOptions(term_user_access_type, true) %>
		<%=Bean.getSelectOnChangeEndHTML() %>

		<%=Bean.getSelectOnChangeBeginHTML("term_user_status", "../crm/security/userspecs.jsp?id=" + userid + "&term_user_page=1", Bean.contactXML.getfieldTransl("name_term_user_status", false)) %>
			<%= Bean.getTermUserStatusOptions(term_user_status, true) %>
		<%=Bean.getSelectOnChangeEndHTML() %>
	</tr>
</table>
<%=user.getTerminalUsersHTML(term_user_find, term_user_access_type, term_user_status, l_term_user_page_beg, l_term_user_page_end) %>
<%}

if (Bean.currentMenu.isCurrentTabAndAccessPermitted("SECURITY_USERS_TERM_MESSAGES")) {%>

<table <%= Bean.getTableBottomFilter() %>>
	<tr>
		<%= Bean.getFindHTML("term_message_find", term_message_find, "../crm/security/userspecs.jsp?id=" + userid + "&term_message_page=1") %>

		<td>&nbsp;</td>
	</tr>
</table>
<%=user.getCRMUserMessagesHTML(term_message_find, l_term_message_page_beg, l_term_message_page_end) %>
<%}

} %>

</div></div>
</body>
</html>
