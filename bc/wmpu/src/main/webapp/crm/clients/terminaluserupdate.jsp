<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%= Bean.getLogOutScript(request) %>

<%@page import="bc.objects.bcTerminalObject"%>
<%@page import="bc.objects.bcTerminalUserObject"%>
<%@page import="bc.objects.bcObject"%>
<%@page import="bc.objects.bcTerminalDeviceTypeObject"%>
<%@page import="bc.objects.bcClubObject"%>
<%@page import="bc.objects.bcTerminalOnlinePaymentTypeObject"%>
<%@page import="bc.objects.bcClubShortObject"%>
<%@page import="java.util.ArrayList"%>
<%@page import="bc.objects.bcTerminalMessageReceiverObject"%>
<%@page import="bc.objects.bcTerminalSAMObject"%>
<%@page import="bc.lists.bcListTerminal"%>
<%@page import="bc.service.bcFeautureParam"%>
<%@page import="bc.objects.bcContactsObject"%>
<%@page import="bc.objects.bcUserObject"%>
<%@page import="bc.lists.bcListUser"%><html>
<head>
	<%= Bean.getMetaContent() %>
	<%= Bean.getBottomFrameCSS() %>
</head>
<body>

<%
HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String pageFormName = "CLIENTS_TERMINALS";

Bean.setJspPageForTabName(pageFormName);

request.setCharacterEncoding("UTF-8");

String type					= Bean.getDecodeParamPrepare(parameters.get("type"));
String action				= Bean.getDecodeParamPrepare(parameters.get("action")); 
String process				= Bean.getDecodeParamPrepare(parameters.get("process"));
String back_type			= Bean.getDecodeParamPrepare(parameters.get("back_type"));
String external_id			= Bean.getDecodeParamPrepare(parameters.get("id"));
String id_term				= Bean.getDecodeParamPrepare(parameters.get("id_term")); 
String id_user				= Bean.getDecodeParamPrepare(parameters.get("id_user")); 
String id_nat_prs			= Bean.getDecodeParamPrepare(parameters.get("id_nat_prs")); 
String id_nat_prs_role		= Bean.getDecodeParamPrepare(parameters.get("id_nat_prs_role")); 

String updateLink = "../crm/clients/terminaluserupdate.jsp";
String backLink = "";
String generalLink = "";
back_type = Bean.isEmpty(back_type)?"TERMINAL":back_type;
System.out.println("back_type="+back_type);
if ("TERMINAL".equalsIgnoreCase(back_type)) {
	generalLink = "../crm/clients/terminalspecs.jsp?id="+id_term;
	backLink = "../crm/clients/terminalspecs.jsp?id="+id_term;
} else if ("CONTACT_PRS".equalsIgnoreCase(back_type)) {
	generalLink = "../crm/clients/contact_prsspecs.jsp?id="+external_id;
	backLink = "../crm/clients/contact_prsspecs.jsp?id="+external_id;
} else if ("USER".equalsIgnoreCase(back_type)) {
	generalLink = "../crm/security/userspecs.jsp?id="+external_id;
	backLink = "../crm/security/userspecs.jsp?id="+external_id;
}

if (type.equalsIgnoreCase("user")) {

	if (process.equalsIgnoreCase("no")) {
		if (action.equalsIgnoreCase("addlist")) { 
	   
	   		%>
			<div id="div_oper_caption">
				<center><br>
					<b><font style="font-size:14px;"><%=Bean.contactXML.getfieldTransl("title_select_term", false) %></font></b><br><br>
					<%=Bean.getGoBackButton(backLink,"button_back") %>
				</center>
			</div>
			
			<% 

			String tagTermianlsToAdd = "_TERMINALS_TO_ADD";
			String tagTermianlsToAddFind = "_TERMINALS_TO_ADD_FIND";
			//Обрабатываем номера страниц
			String l_term_page = Bean.getDecodeParam(parameters.get("term_page"));
			Bean.pageCheck(pageFormName + tagTermianlsToAdd, l_term_page);
			String l_term_page_beg = Bean.getFirstRowNumber(pageFormName + tagTermianlsToAdd);
			String l_term_page_end = Bean.getLastRowNumber(pageFormName + tagTermianlsToAdd);

			String term_find 	= Bean.getDecodeParam(parameters.get("term_find"));
			term_find 	= Bean.checkFindString(pageFormName + tagTermianlsToAddFind, term_find, l_term_page);
			%>
			<table <%=Bean.getTableBottomFilter() %>>
				<tr>
					<%= Bean.getFindHTML("term_find", term_find, "../crm/clients/terminaluserupdate.jsp?id="+external_id+"&back_type="+back_type+"&id_term="+id_term+"&id_user="+id_user+"&id_nat_prs="+id_nat_prs+"&id_nat_prs_role="+id_nat_prs_role+"&type=user&process=no&action=addlist&term_page=1&", "div_data_detail") %>

					<%= Bean.getPagesHTML(pageFormName + tagTermianlsToAdd, "../crm/clients/terminaluserupdate.jsp?id="+external_id+"&back_type="+back_type+"&id_term="+id_term+"&id_user="+id_user+"&id_nat_prs="+id_nat_prs+"&id_nat_prs_role="+id_nat_prs_role+"&type=user&process=no&action=addlist&", "term_page", "", "div_data_detail") %>
				</tr>
			</table>
			<%

			bcListTerminal list = new bcListTerminal();
		      
		    String pWhereCause = "";
		    	
		    ArrayList<bcFeautureParam> pWhereValue = new ArrayList<bcFeautureParam>();
		    if ("CONTACT_PRS".equalsIgnoreCase(back_type)) {
		    	bcContactsObject contact = new bcContactsObject(id_nat_prs_role);
			    pWhereCause = " WHERE id_service_place = ? ";
			    pWhereValue.add(new bcFeautureParam("int", contact.getValue("ID_SERVICE_PLACE_WORK")));
		    } else if ("USER".equalsIgnoreCase(back_type)) {
		    	bcUserObject user = new bcUserObject(id_user);
		    	if (!Bean.isEmpty(user.getValue("ID_SERVICE_PLACE_WORK"))) {
				    pWhereCause = " WHERE id_service_place = ? ";
				    pWhereValue.add(new bcFeautureParam("int", user.getValue("ID_SERVICE_PLACE_WORK")));
		    	} else {
		    		pWhereCause = " WHERE 1=0 ";
		    	}
		    }
		    %>
		    <%= list.getTerminalsHTML(pWhereCause, pWhereValue, term_find, "", "", "../crm/clients/terminaluserupdate.jsp?id="+external_id+"&back_type="+back_type+"&id_term="+id_term+"&id_user="+id_user+"&id_nat_prs="+id_nat_prs+"&id_nat_prs_role="+id_nat_prs_role+"&type=user&process=no&action=add", "", "div_data_detail", l_term_page_beg, l_term_page_end) %>
	   <%
	   } else if (action.equalsIgnoreCase("adduserlist")) { 
		   
	   		%>
			<div id="div_oper_caption">
				<center><br>
					<b><font style="font-size:14px;"><%=Bean.contactXML.getfieldTransl("title_select_user", false) %></font></b><br><br>
			        <form action="../crm/clients/terminalupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return myValidateForm()">
				        <input type="hidden" name="type" value="user">
				        <input type="hidden" name="action" value="addnew">
				        <input type="hidden" name="process" value="no">
				        <input type="hidden" name="id" value="<%=external_id %>">
				        <input type="hidden" name="back_type" value="<%=back_type %>">
				        <input type="hidden" name="id_term" value="<%=id_term %>">
				        <input type="hidden" name="id_user" value="<%=id_user %>">
				        <input type="hidden" name="id_nat_prs" value="<%=id_nat_prs %>">
				        <input type="hidden" name="id_nat_prs_role" value="<%=id_nat_prs_role %>">
					<%=Bean.getSubmitButtonAjax(updateLink, "new", "updateForm", "div_data_detail") %>
					<%=Bean.getGoBackButton(backLink,"button_back") %>
					</form>
				</center>
			</div>
			
			<% 

			String tagUsersToAdd = "_USERS_TO_ADD";
			String tagUsersToAddFind = "_USERS_TO_ADD_FIND";
			//Обрабатываем номера страниц
			String l_user_page = Bean.getDecodeParam(parameters.get("user_page"));
			Bean.pageCheck(pageFormName + tagUsersToAdd, l_user_page);
			String l_user_page_beg = Bean.getFirstRowNumber(pageFormName + tagUsersToAdd);
			String l_user_page_end = Bean.getLastRowNumber(pageFormName + tagUsersToAdd);

			String user_find 	= Bean.getDecodeParam(parameters.get("user_find"));
			user_find 	= Bean.checkFindString(pageFormName + tagUsersToAddFind, user_find, l_user_page);
			%>
			<table <%=Bean.getTableBottomFilter() %>>
				<tr>
					<%= Bean.getFindHTML("user_find", user_find, "../crm/clients/terminaluserupdate.jsp?id="+external_id+"&back_type="+back_type+"&id_term="+id_term+"&id_user="+id_user+"&id_nat_prs="+id_nat_prs+"&id_nat_prs_role="+id_nat_prs_role+"&type=user&process=no&action=adduserlist&user_page=1&", "div_data_detail") %>

					<%= Bean.getPagesHTML(pageFormName + tagUsersToAdd, "../crm/clients/terminaluserupdate.jsp?id="+external_id+"&back_type="+back_type+"&id_term="+id_term+"&id_user="+id_user+"&id_nat_prs="+id_nat_prs+"&id_nat_prs_role="+id_nat_prs_role+"&type=user&process=no&action=adduserlist&", "user_page", "", "div_data_detail") %>
				</tr>
			</table>
			<%

			bcListUser listUser = new bcListUser();
		      
		    String pWhereCause = "";
		    	
		    ArrayList<bcFeautureParam> pWhereValue = new ArrayList<bcFeautureParam>();
		    if ("CONTACT_PRS".equalsIgnoreCase(back_type)) {
		    	bcContactsObject contact = new bcContactsObject(id_nat_prs_role);
			    pWhereCause = " WHERE id_nat_prs_role = ? ";
			    pWhereValue.add(new bcFeautureParam("int", contact.getValue("ID_NAT_PRS_ROLE")));
		    } else if ("TERMINAL".equalsIgnoreCase(back_type)) {
		    	bcTerminalObject term = new bcTerminalObject(id_term);
		    	term.getFeature();
		    	if (!Bean.isEmpty(term.getValue("ID_SERVICE_PLACE"))) {
				    pWhereCause = " WHERE id_service_place_work = ? ";
				    pWhereValue.add(new bcFeautureParam("int", term.getValue("ID_SERVICE_PLACE")));
		    	} else {
		    		pWhereCause = " WHERE 1=0 ";
		    	}
		    }
		    %>
		    <%= listUser.getUsersHTML(pWhereCause, pWhereValue, back_type, user_find, "", "../crm/clients/terminaluserupdate.jsp?id="+external_id+"&back_type="+back_type+"&id_term="+id_term+"&id_user="+id_user+"&id_nat_prs="+id_nat_prs+"&id_nat_prs_role="+id_nat_prs_role+"&type=user&process=no&action=add", "", "", "div_data_detail", l_user_page_beg, l_user_page_end) %>
	   <%
	   } else if (action.equalsIgnoreCase("add") || action.equalsIgnoreCase("addnew")) {
			
			bcTerminalObject term = new bcTerminalObject(id_term);
			term.getFeature();
			
			String cdTermType = term.getValue("CD_TERM_TYPE");
			boolean isWebPosTerminal = "WEBPOS".equalsIgnoreCase(cdTermType);
			boolean isPhisicalTerminal = "PHYSICAL".equalsIgnoreCase(cdTermType);
		   %> 
			
			<%= Bean.getOperationTitleShort(
					"",
					Bean.terminalXML.getfieldTransl("h_term_user_add", false),
					"Y",
					"N") 
			%>
        <script>
			var formData = new Array (
				new Array ('id_term', 'varchar2', 1),
				<% if (!(isWebPosTerminal && action.equalsIgnoreCase("addnew"))) { %>
				new Array ('name_user', 'varchar2', 1),
				<% } %>
				<% if (isPhisicalTerminal) { %>
				new Array ('login_term_user', 'varchar2', 1),
				new Array ('password_term_user', 'varchar2', 1),
				<% } else { %>
				<% if (action.equalsIgnoreCase("addnew")) { %>
				new Array ('password_term_user', 'varchar2', 1),
				new Array ('name_nat_prs_role', 'varchar2', 1),
				<% } %>
				<% } %>
				new Array ('cd_term_user_access_type', 'varchar2', 1),
				new Array ('cd_term_user_status', 'varchar2', 1)
			);
			function myValidateForm() {
				return validateForm(formData);
			}
		</script>

	        <form action="../crm/clients/terminalupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return myValidateForm()">
		        <input type="hidden" name="type" value="user">
		        <input type="hidden" name="action" value="add">
		        <input type="hidden" name="process" value="yes">
		        <input type="hidden" name="id" value="<%=external_id %>">
		        <input type="hidden" name="back_type" value="<%=back_type %>">
		        <input type="hidden" name="id_user" value="<%=id_user %>">
		        <input type="hidden" name="id_nat_prs" value="<%=id_nat_prs %>">
		        <input type="hidden" name="id_nat_prs_role" value="<%=id_nat_prs_role %>">
			<table <%=Bean.getTableDetailParam() %>>
			<tr>
				<td><%= Bean.terminalXML.getfieldTransl("id_term", false) %>
					<%= Bean.getGoToTerminalLink(id_term) %>
				</td><td><input type="text" name="id_term" size="16" value="<%=id_term %>" readonly="readonly" class="inputfield-ro"></td>
				<td colspan="2">&nbsp;</td>
			</tr>
			<% if (isPhisicalTerminal) { %>
			<tr>
				<td><%= Bean.terminalXML.getfieldTransl("name_term_user", true) %></td> <td><input type="text" name="name_user" size="50" value="" class="inputfield"></td>
			</tr>
			<tr>
				<td><%= Bean.terminalXML.getfieldTransl("login_term_user", true) %></td> <td><input type="text" name="login_term_user" size="50" value="" class="inputfield"></td>
			</tr>
			<tr>
				<td><%= Bean.terminalXML.getfieldTransl("password_term_user", true) %></td> <td><input type="text" name="password_term_user" size="50" value="" class="inputfield"></td>
			</tr>
			<% } else { %>
			<% if (action.equalsIgnoreCase("add")) { %>
			<tr>
				<td><%= Bean.terminalXML.getfieldTransl("name_term_user", true) %></td>
				<td>
					<% if ("TERMINAL".equalsIgnoreCase(back_type)) { %>
						<%=Bean.getWindowFindUserBase("user", id_user, "", term.getValue("ID_SERVICE_PLACE"), "50") %>
					<% } else { %>
						<%=Bean.getWindowFindNatPrsRoleUser("user", "", "", id_nat_prs_role, "50") %>
					<% } %>
				</td>
			</tr>
			<tr>
				<td><%= Bean.terminalXML.getfieldTransl("is_test_mode", false) %></td><td><select name="is_test_mode" class="inputfield"><%= Bean.getMeaningFromLookupNameOptions("YES_NO", "N", true) %></select></td>
			</tr>
			<% } else { %>
			<tr>
				<td><%= Bean.userXML.getfieldTransl("fio_nat_prs", true) %></td> <td>
					<%=Bean.getWindowFindNatPrsRole("nat_prs_role", "", "", term.getValue("ID_SERVICE_PLACE"), "40") %>
				</td>
			</tr>
			<tr>
				<td><%= Bean.terminalXML.getfieldTransl("password_term_user", true) %></td> <td><input type="text" name="password_term_user" size="50" value="" class="inputfield"></td>
			</tr>
			<tr>
				<td><%= Bean.terminalXML.getfieldTransl("is_test_mode", false) %></td><td><select name="is_test_mode" class="inputfield"><%= Bean.getMeaningFromLookupNameOptions("YES_NO", "N", true) %></select></td>
			</tr>
			<% } %>
			<% } %>
			<tr>
				<td><%= Bean.terminalXML.getfieldTransl("cd_term_user_access_type", true) %></td><td><select name="cd_term_user_access_type" class="inputfield"><%= Bean.getTermUserAccessTypeOptions(term.getValue("CD_TERM_TYPE"), "1", true) %></select></td>
			</tr>
			<tr>
				<td><%= Bean.terminalXML.getfieldTransl("cd_term_user_status", true) %></td><td><select name="cd_term_user_status" class="inputfield"><%= Bean.getTermUserStatusOptions("2", true) %></select></td>
			</tr>
			<tr>
				<td colspan="6" align="center">
					<%=Bean.getSubmitButtonAjax(updateLink) %>
					<%=Bean.getResetButton() %>
					<%=Bean.getGoBackButton(backLink) %>
				</td>
			</tr>
		</table>
		</form>

        <%
		} else if (action.equalsIgnoreCase("edit")) { 

			String	id_term_user	= Bean.getDecodeParam(parameters.get("id_term_user"));

			bcTerminalUserObject user = new bcTerminalUserObject(id_term_user);
			
			String cdTermType = user.getValue("CD_TERM_TYPE");
			boolean isWebPosTerminal = "WEBPOS".equalsIgnoreCase(cdTermType);
			boolean isPhisicalTerminal = "PHYSICAL".equalsIgnoreCase(cdTermType);
			%> 

				<%= Bean.getOperationTitleShort(
						"",
						Bean.terminalXML.getfieldTransl("h_term_user_update", false),
						"Y",
						"N") 
				%>
        <script>
			var formData = new Array (
				new Array ('id_term', 'varchar2', 1),
				new Array ('name_user', 'varchar2', 1),
				<% if (isPhisicalTerminal) { %>
				new Array ('login_term_user', 'varchar2', 1),
				new Array ('password_term_user', 'varchar2', 1),
				<% } %>
				new Array ('cd_term_user_access_type', 'varchar2', 1),
				new Array ('cd_term_user_status', 'varchar2', 1)
			);
			function myValidateForm() {
				return validateForm(formData);
			}
		</script>
	
	        <form action="../crm/clients/terminalupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return myValidateForm()">
		        <input type="hidden" name="type" value="user">
		        <input type="hidden" name="action" value="edit">
		        <input type="hidden" name="process" value="yes">
		        <input type="hidden" name="id_term_user" value="<%=user.getValue("ID_TERM_USER") %>">
		        <input type="hidden" name="id" value="<%=external_id %>">
		        <input type="hidden" name="back_type" value="<%=back_type %>">
		        <input type="hidden" name="id_user" value="<%=id_user %>">
		        <input type="hidden" name="id_nat_prs" value="<%=id_nat_prs %>">
		        <input type="hidden" name="id_nat_prs_role" value="<%=id_nat_prs_role %>">
			<table <%=Bean.getTableDetailParam() %>>
				<tr>
					<td><%= Bean.terminalXML.getfieldTransl("id_term", false) %>
						<%= Bean.getGoToTerminalLink(user.getValue("ID_TERM")) %>
					</td><td><input type="text" name="id_term" size="16" value="<%=user.getValue("ID_TERM") %>" readonly="readonly" class="inputfield-ro"></td>
					<td colspan="2">&nbsp;</td>
				</tr>
			<% if (isPhisicalTerminal) { %>
				<tr>
					<td><%= Bean.terminalXML.getfieldTransl("name_term_user", true) %></td> <td><input type="text" name="name_user" size="50" value="<%=user.getValue("NAME_TERM_USER") %>" class="inputfield"></td>
					<td><%= Bean.terminalXML.getfieldTransl("cd_term_user_access_type", true) %></td><td><select name="cd_term_user_access_type" class="inputfield"><%= Bean.getTermUserAccessTypeOptions(user.getValue("CD_TERM_TYPE"), user.getValue("CD_TERM_USER_ACCESS_TYPE"), true) %></select></td>
				</tr>
				<tr>
					<td><%= Bean.terminalXML.getfieldTransl("login_term_user", true) %></td> <td><input type="text" name="login_term_user" size="50" value="<%=user.getValue("LOGIN_TERM_USER") %>" class="inputfield"></td>
					<td><%= Bean.terminalXML.getfieldTransl("cd_term_user_status", true) %></td><td><select name="cd_term_user_status" class="inputfield"><%= Bean.getTermUserStatusOptions(user.getValue("CD_TERM_USER_STATUS"), true) %></select></td>
				</tr>
				<tr>
					<td><%= Bean.terminalXML.getfieldTransl("password_term_user", true) %></td> <td><input type="text" name="password_term_user" size="50" value="<%=user.getValue("PASSWORD_TERM_USER") %>" class="inputfield"></td>
					<td colspan="2">&nbsp;</td>
				</tr>
			<% } else { %>
				<tr>
					<td><%= Bean.terminalXML.getfieldTransl("name_term_user", true) %>
						<%= Bean.getGoToSystemUserLink(user.getValue("ID_USER")) %>
					</td>
					<td>
						<%=Bean.getWindowFindUser("user", user.getValue("ID_USER"), user.getValue("NAME_TERM_USER"), "50") %>
					</td>
					<td><%= Bean.terminalXML.getfieldTransl("cd_term_user_access_type", true) %></td><td><select name="cd_term_user_access_type" class="inputfield"><%= Bean.getTermUserAccessTypeOptions(user.getValue("CD_TERM_TYPE"), user.getValue("CD_TERM_USER_ACCESS_TYPE"), true) %></select></td>
				</tr>
				<tr>
					<td><%= Bean.terminalXML.getfieldTransl("is_test_mode", false) %></td><td><select name="is_test_mode" class="inputfield"><%= Bean.getMeaningFromLookupNameOptions("YES_NO", user.getValue("IS_TEST_MODE"), true) %></select></td>
					<td><%= Bean.terminalXML.getfieldTransl("cd_term_user_status", true) %></td><td><select name="cd_term_user_status" class="inputfield"><%= Bean.getTermUserStatusOptions(user.getValue("CD_TERM_USER_STATUS"), true) %></select></td>
				</tr>
				<% } %>
				<%=	Bean.getIdCreationAndMoficationRecordFields(
						user.getValue("ID_TERM_USER"),
						user.getValue(Bean.getCreationDateFieldName()),
						user.getValue("CREATED_BY"),
						user.getValue(Bean.getLastUpdateDateFieldName()),
						user.getValue("LAST_UPDATE_BY")
				) %>
				<tr>
					<td colspan="6" align="center">
						<%=Bean.getSubmitButtonAjax(updateLink) %>
						<%=Bean.getResetButton() %>
						<%=Bean.getGoBackButton(backLink) %>
					</td>
				</tr>
			</table>
			</form>
		<%

    	} else {%><%= Bean.getUnknownActionText(action) %><%
    	}
	} else if (process.equalsIgnoreCase("yes")){
	    String
	    	id_term_user				= Bean.getDecodeParam(parameters.get("id_term_user")),
    		name_term_user				= Bean.getDecodeParam(parameters.get("name_user")),
	    	login_term_user				= Bean.getDecodeParam(parameters.get("login_term_user")),
	    	password_term_user			= Bean.getDecodeParam(parameters.get("password_term_user")),
	    	cd_term_user_access_type	= Bean.getDecodeParam(parameters.get("cd_term_user_access_type")),
	    	cd_term_user_status			= Bean.getDecodeParam(parameters.get("cd_term_user_status")),
	    	is_test_mode				= Bean.getDecodeParam(parameters.get("is_test_mode"));

	    ArrayList<String> pParam = new ArrayList<String>();
	    
		if (action.equalsIgnoreCase("add")) {
				
			pParam.add(id_term);
			pParam.add(name_term_user);
			pParam.add(login_term_user);
			pParam.add(password_term_user);
			pParam.add(id_user);
			pParam.add(id_nat_prs_role);
			pParam.add(cd_term_user_access_type);
			pParam.add(cd_term_user_status);
			pParam.add(is_test_mode);

			%>
			<%= Bean.executeInsertFunction("PACK$TERM_UI.add_term_user", pParam, backLink + "&id_term_user=", "") %>
			<% 	

		} else if (action.equalsIgnoreCase("remove")) {

			pParam.add(id_term_user);

			%>
			<%= Bean.executeDeleteFunction("PACK$TERM_UI.delete_term_user", pParam, generalLink, "") %>
			<% 	
		     
		} else if (action.equalsIgnoreCase("edit")) { 
		    
	    	pParam.add(id_term_user);
			pParam.add(name_term_user);
			pParam.add(login_term_user);
			pParam.add(password_term_user);
			pParam.add(id_user);
			pParam.add(cd_term_user_access_type);
			pParam.add(cd_term_user_status);
			pParam.add(is_test_mode);

			%>
			<%= Bean.executeUpdateFunction("PACK$TERM_UI.update_term_user", pParam, backLink, "") %>
			<% 	
		} else { %>
			<%= Bean.getUnknownActionText(action) %><% 
		}
	} else {%><%= Bean.getUnknownProcessText(process) %><%
	}
	
} else {%> 
	<%= Bean.getUnknownTypeText(type) %><%
}
%>


</body>

<%@page import="bc.objects.bcTerminalMessageObject"%>
<%@page import="bc.objects.bcClubRelationshipObject"%>
<%@page import="java.util.HashMap"%></html>
