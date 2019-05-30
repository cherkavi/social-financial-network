<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcWebPosBean"  />

<%@page import="java.util.HashMap"%>
<%@page import="bc.objects.bcUserMessageObject"%>
<%@page import="java.util.ArrayList"%><html>

<% 
String pageFormName = "WEBPOS_ADMIN_HELP";

String tagTemplate = "_TEMPLATE";
String tagTemplateFind = "_TEMPLATE_FIND";
String tagMessage = "_MESSAGE";
String tagMessageFind = "_MESSAGE_FIND";

Bean.setJspPageForTabName(pageFormName);

HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());


String id_term = Bean.getDecodeParam(parameters.get("termid"));
String action  = Bean.getDecodeParam(parameters.get("action"));

id_term 	= Bean.isEmpty(id_term)?Bean.getCurrentTerm():id_term;
action 		= Bean.isEmpty(action)?"show":action;

String id_message  		= Bean.getDecodeParam(parameters.get("id_message")); 
String text_message  	= Bean.getDecodeParam(parameters.get("text_message")); 

Bean.loginTerm.getFeature();

String l_template_page = Bean.getDecodeParam(parameters.get("template_page"));
Bean.pageCheck(pageFormName + tagTemplate, l_template_page);
String l_template_page_beg = Bean.getFirstRowNumber(pageFormName + tagTemplate);
String l_template_page_end = Bean.getLastRowNumber(pageFormName + tagTemplate);

String template_find 	= Bean.getDecodeParam(parameters.get("template_find"));
template_find 	= Bean.checkFindString(pageFormName + tagTemplateFind, template_find, l_template_page);

String l_message_page = Bean.getDecodeParam(parameters.get("message_page"));
Bean.pageCheck(pageFormName + tagMessage, l_message_page);
String l_message_page_beg = Bean.getFirstRowNumber(pageFormName + tagMessage);
String l_message_page_end = Bean.getLastRowNumber(pageFormName + tagMessage);
	
String message_find 	= Bean.getDecodeParam(parameters.get("message_find"));
message_find 	= Bean.checkFindString(pageFormName + tagMessageFind, message_find, l_message_page);

//messages.append(Bean.loginUser.getUserMessagesHTML(message_find, l_message_page_beg, l_message_page_end));


%>
<body>
	<%=Bean.getWebPosMenuHTML(pageFormName) %>
<% if (!Bean.hasMenuPermission(pageFormName, Bean.C_READ_MENU_PERMISSION, Bean.loginTerm)) { %>
	<%=Bean.getErrorPermissionMessage(pageFormName, Bean.loginTerm) %>
<% } else { 

	String tab = Bean.getDecodeParam(parameters.get("tab"));
	if (tab == null || "".equalsIgnoreCase(tab)) {
		tab = Bean.tabsHmGetValue(pageFormName);
		if (!("1".equalsIgnoreCase(tab) || "2".equalsIgnoreCase(tab) || "3".equalsIgnoreCase(tab))) {
			tab = "1";
		}
	}

	boolean tab1HasPermission = Bean.hasReadMenuPermission("WEBPOS_ADMIN_HELP_HELP");
	boolean tab2HasPermission = Bean.hasReadMenuPermission("WEBPOS_ADMIN_HELP_TEMPLATES");
	boolean tab3HasPermission = Bean.hasReadMenuPermission("WEBPOS_ADMIN_HELP_MESSAGES");
	int tabCount = (tab1HasPermission?1:0)+(tab2HasPermission?1:0)+(tab3HasPermission?1:0) + 1 /*О программе показываем всегда*/;

	System.out.println("tab1="+tab);
	if ("1".equalsIgnoreCase(tab) && !tab1HasPermission) {
		tab = !tab2HasPermission?(!tab3HasPermission?"4":"3"):"2";
	} else if ("2".equalsIgnoreCase(tab) && !tab2HasPermission) {
		tab = !tab1HasPermission?(!tab3HasPermission?"4":"3"):"1";
	} else if ("3".equalsIgnoreCase(tab) && !tab3HasPermission) {
		tab = !tab1HasPermission?(!tab2HasPermission?"4":"2"):"1";
	}
	System.out.println("tab2="+tab);

	Bean.tabsHmSetValue(pageFormName, tab);

	if ("3".equalsIgnoreCase(tab) && tab3HasPermission) {
		if ("read".equalsIgnoreCase(action) || 
			"not_read".equalsIgnoreCase(action) || 
			"edit".equalsIgnoreCase(action)) { 
						
			ArrayList<String> pParam = new ArrayList<String>();
						
			String[] results = new String[2];
						
			pParam.add(id_message);
			pParam.add(Bean.getCurrentTerm());
				
			if ("read".equalsIgnoreCase(action) || "edit".equalsIgnoreCase(action)) {
				pParam.add("READ");
				results 					= Bean.executeFunction("PACK$WEBPOS_UI.user_message_set_state", pParam, results.length);
			} else if ("not_read".equalsIgnoreCase(action)) {
				pParam.add("NOT_READ");
				results 					= Bean.executeFunction("PACK$WEBPOS_UI.user_message_set_state", pParam, results.length);
			}
			String resultInt 			= results[0];
			String resultMessage 		= results[1];
		}
	}
	String lMessageCnt = Bean.loginUser.getUserMessageNotReadCount(Bean.getLoginUserId());
%>

<% if (tabCount > 0) { %>
<table <%=Bean.getTableDetail2Param() %>>
		<tr>
			<td>
<div id="div_action_big">
	<div>
	<h1>
	<%if ("1".equalsIgnoreCase(tab) && tab1HasPermission) {%>
	<%=Bean.webposXML.getfieldTransl("title_setting_help", false) %>
	<% } %>
	<%if ("2".equalsIgnoreCase(tab) && tab2HasPermission) {%>
	<%=Bean.webposXML.getfieldTransl("title_templates", false) %>
	<% } %>
	<%if ("3".equalsIgnoreCase(tab) && tab3HasPermission) {%>
	<%=Bean.webposXML.getfieldTransl("title_setting_messages", false) %>
	<% } %>
	<%if ("4".equalsIgnoreCase(tab) && tab3HasPermission) {%>
	<%=Bean.webposXML.getfieldTransl("title_setting_about", false) %>
	<% } %>
	<%=Bean.getHelpButton("setting", "div_action_big") %>
	</h1>
	<% if (tabCount>1) { %>
	<table <%=Bean.getTableDetail2Param()%>>
		<tr>
			<td>
				<div id="slidetabsmenu">
				<ul>
				<% if (tab1HasPermission) { %>
				<li <%if ("1".equalsIgnoreCase(tab)) {%> class="current"<% } %>><a href="#"><span onclick="ajaxpage('admin/help.jsp?tab=1', 'div_main')"><%=Bean.webposXML.getfieldTransl("title_setting_help", false) %></span></a></li>
				<% } %>
				<% if (tab2HasPermission) { %>
				<li <%if ("2".equalsIgnoreCase(tab)) {%> class="current"<% } %>><a href="#"><span onclick="ajaxpage('admin/help.jsp?tab=2', 'div_main')"><%=Bean.webposXML.getfieldTransl("title_templates", false) %></span></a></li>
				<% } %>
				<% if (tab3HasPermission) { %>
				<li <%if ("3".equalsIgnoreCase(tab)) {%> class="current"<% } %>><a href="#"><span onclick="ajaxpage('admin/help.jsp?tab=3', 'div_main')"><%=Bean.webposXML.getfieldTransl("title_setting_messages", false) + ((!"0".equalsIgnoreCase(lMessageCnt))?" ("+lMessageCnt+")":"") %></span></a></li>
				<% } %>
				<li <%if ("4".equalsIgnoreCase(tab)) {%> class="current"<% } %>><a href="#"><span onclick="ajaxpage('admin/help.jsp?tab=4', 'div_main')"><%=Bean.webposXML.getfieldTransl("title_setting_about", false) %></span></a></li>
				</ul>
				</div>
			</td>
		</tr>
	</table>
	<% } %>
</div>
<div id="div_data">
<div id="div_data_detail">
<% if ("1".equalsIgnoreCase(tab) && tab1HasPermission) { %>
	<table class="table_doc_template">
		<tbody>
			<tr><td class="section"><span onclick="ajaxpage('admin/help.jsp', 'div_main')"><%= Bean.webposXML.getfieldTransl("title_base_menu", false) %></span></td></tr>
			<tr><td><span onclick="ajaxpage('admin/help.jsp', 'div_main')"><%= Bean.webposXML.getfieldTransl("title_pay_check", false) %></span></td></tr>
			<tr><td><span onclick="ajaxpage('admin/help.jsp', 'div_main')"><%= Bean.webposXML.getfieldTransl("title_replenish", false) %></span></td></tr>
			<tr><td>&nbsp;&nbsp;&nbsp;&nbsp;<span onclick="ajaxpage('admin/help.jsp', 'div_main')"><%= Bean.webposXML.getfieldTransl("fee_kind_share", false) %></span></td></tr>
			<tr><td>&nbsp;&nbsp;&nbsp;&nbsp;<span onclick="ajaxpage('admin/help.jsp', 'div_main')"><%= Bean.webposXML.getfieldTransl("fee_kind_membership", false) %></span></td></tr>
			<tr><td>&nbsp;&nbsp;&nbsp;&nbsp;<span onclick="ajaxpage('admin/help.jsp', 'div_main')"><%= Bean.webposXML.getfieldTransl("fee_kind_admission_short", false) %></span></td></tr>
			<tr><td><span onclick="ajaxpage('admin/help.jsp', 'div_main')"><%= Bean.webposXML.getfieldTransl("title_transfer", false) %></span></td></tr>
			<tr><td><span onclick="ajaxpage('admin/help.jsp', 'div_main')"><%= Bean.webposXML.getfieldTransl("title_storno", false) %></span></td></tr>
			<tr><td>&nbsp;&nbsp;&nbsp;&nbsp;<span onclick="ajaxpage('admin/help.jsp', 'div_main')"><%= Bean.webposXML.getfieldTransl("storno_type_cancellation", false) %></span></td></tr>
			<tr><td>&nbsp;&nbsp;&nbsp;&nbsp;<span onclick="ajaxpage('admin/help.jsp', 'div_main')"><%= Bean.webposXML.getfieldTransl("storno_type_return", false) %></span></td></tr>
			<tr><td><span onclick="ajaxpage('admin/help.jsp', 'div_main')"><%= Bean.webposXML.getfieldTransl("title_coupon", false) %></span></td></tr>
			<tr><td><span onclick="ajaxpage('admin/help.jsp', 'div_main')"><%= Bean.webposXML.getfieldTransl("title_operation", false) %></span></td></tr>
			<tr><td>&nbsp;&nbsp;&nbsp;&nbsp;<span onclick="ajaxpage('admin/help.jsp', 'div_main')"><%= Bean.webposXML.getfieldTransl("title_operation_short", false) %></span></td></tr>
			<tr><td>&nbsp;&nbsp;&nbsp;&nbsp;<span onclick="ajaxpage('admin/help.jsp', 'div_main')"><%= Bean.webposXML.getfieldTransl("title_reports", false) %></span></td></tr>
			<tr><td><span onclick="ajaxpage('admin/help.jsp', 'div_main')"><%= Bean.webposXML.getfieldTransl("title_card_issue", false) %></span></td></tr>
			<tr><td>&nbsp;&nbsp;&nbsp;&nbsp;<span onclick="ajaxpage('admin/help.jsp', 'div_main')"><%= Bean.webposXML.getfieldTransl("title_card_registration", false) %></span></td></tr>
			<tr><td>&nbsp;&nbsp;&nbsp;&nbsp;<span onclick="ajaxpage('admin/help.jsp', 'div_main')"><%= Bean.webposXML.getfieldTransl("title_questionnaire", false) %></span></td></tr>
			<tr><td>&nbsp;&nbsp;&nbsp;&nbsp;<span onclick="ajaxpage('admin/help.jsp', 'div_main')"><%= Bean.webposXML.getfieldTransl("title_activation", false) %></span></td></tr>
			<tr><td><span onclick="ajaxpage('admin/help.jsp', 'div_main')"><%= Bean.webposXML.getfieldTransl("title_settings", false) %></span></td></tr>
			<tr><td>&nbsp;&nbsp;&nbsp;&nbsp;<span onclick="ajaxpage('admin/help.jsp', 'div_main')"><%= Bean.webposXML.getfieldTransl("title_setting_user", false) %></span></td></tr>
			<tr><td>&nbsp;&nbsp;&nbsp;&nbsp;<span onclick="ajaxpage('admin/help.jsp', 'div_main')"><%= Bean.webposXML.getfieldTransl("title_setting_terminal", false) %></span></td></tr>
			<tr><td>&nbsp;&nbsp;&nbsp;&nbsp;<span onclick="ajaxpage('admin/help.jsp', 'div_main')"><%= Bean.webposXML.getfieldTransl("title_setting_administration", false) %></span></td></tr>
			<tr><td><span onclick="ajaxpage('admin/help.jsp', 'div_main')"><%= Bean.webposXML.getfieldTransl("title_setting_help", false) %></span></td></tr>
			<tr><td>&nbsp;&nbsp;&nbsp;&nbsp;<span onclick="ajaxpage('admin/help.jsp', 'div_main')"><%= Bean.webposXML.getfieldTransl("title_setting_help", false) %></span></td></tr>
			<tr><td>&nbsp;&nbsp;&nbsp;&nbsp;<span onclick="ajaxpage('admin/help.jsp', 'div_main')"><%= Bean.webposXML.getfieldTransl("title_templates", false) %></span></td></tr>
			<tr><td>&nbsp;&nbsp;&nbsp;&nbsp;<span onclick="ajaxpage('admin/help.jsp', 'div_main')"><%= Bean.webposXML.getfieldTransl("title_setting_messages", false) %></span></td></tr>
		</tbody>
	</table>
<% } %>
<% if ("2".equalsIgnoreCase(tab) && tab2HasPermission) { 
		StringBuilder html = new StringBuilder();
		html.append(Bean.loginUser.getWebPOSDocTemplatesHTML(template_find, l_template_page_beg, l_template_page_end));
		
		%>
			<table <%=Bean.getTableDetail2Param() %>>
				<tr>
					<td>
						<table <%=Bean.getTableBottomFilter() %>>
						  	<tr>
							<%= Bean.getFindHTML("template_find", template_find, "admin/help.jsp?template_page=1&", "div_main") %>
							
							
						    <!-- Вывод страниц -->
							<%= Bean.getPagesHTML(pageFormName + tagTemplate, "admin/help.jsp?", "template_page", Bean.loginUser.getDocumentsCount(), "div_main") %>
						  	</tr>
						</table>
					</td>
				</tr>
			</table>
			<% String lDocCount = Bean.loginUser.getDocumentsCount();
				
			if (!(Bean.isEmpty(lDocCount) || "0".equalsIgnoreCase(lDocCount))) { %>
			<%= html.toString()%>
			<br>
			<br>
			<% } else { 
				String lMessage = Bean.webposXML.getfieldTransl("title_templates_not_found", false);
				lMessage = lMessage.replace("%FIND_STRING%",template_find);
				%>
				<table class="action_table">
						<tr><td align="center">&nbsp;</td></tr>
						<tr><td style="color:black;"><%=lMessage %></td></tr>
						<tr><td align="center">&nbsp;</td></tr>
					</table>
			<% } %>
<% } %>
<% if ("3".equalsIgnoreCase(tab) && tab3HasPermission) {%>
	<table >
		
		<tr>
			<td>
			<% if ("show".equalsIgnoreCase(action) || 
					"read".equalsIgnoreCase(action) || 
					"not_read".equalsIgnoreCase(action)) { 

				StringBuilder html = new StringBuilder();
				html.append(Bean.loginUser.getUserMessagesHTML(message_find, l_message_page_beg, l_message_page_end));
					%>
				<form name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST">
			        <input type="hidden" name="action" value="new">
			        <input type="hidden" name="id_user" value="<%=Bean.getLoginUserId() %>">
					<table <%=Bean.getTableDetail2Param() %>>
						<tr>
							<td colspan="2"  align="center">
								<%=Bean.getSubmitButtonAjax("admin/help.jsp", "button_message_new", "updateForm", "div_main") %>
							</td>
						</tr>
						<tr>
							<td>
								<table <%=Bean.getTableBottomFilter() %>>
								  	<tr>
									<%= Bean.getFindHTML("message_find", message_find, "admin/help.jsp?message_page=1&", "div_main") %>
									
									
								    <!-- Вывод страниц -->
									<%= Bean.getPagesHTML(pageFormName + tagMessage, "admin/help.jsp?", "message_page", Bean.loginUser.getMessagesCount(), "div_main") %>
								  	</tr>
								</table>
							</td>
						</tr>
					</table>
					<%=html.toString() %>
				</form>
			<% } else if ("new".equalsIgnoreCase(action)) { %>
			<script>
		
			function validateMessage(){
				var returnValue = null;
				var formParam = new Array (
					new Array ('text_message', 'varchar2', 1)
				);
				returnValue = validateFormForID(formParam, 'updateForm');
				return returnValue;
			}
			</script>

				<form name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST">
			        <input type="hidden" name="action" value="send">
			        <input type="hidden" name="id_user" value="<%=Bean.getLoginUserId() %>">
					<table <%=Bean.getTableDetail2Param() %>>
						<tr>
							<td <%=Bean.getDetailTableFirstColumnWidth() %>><%= Bean.webposXML.getfieldTransl("user_param_name_user", false) %></td> <td><input type="text" name="name_user" size="30" value="<%= Bean.getLoginUserName() %>" readonly class="inputfield-ro"> </td>
						</tr>
						<tr>
							<td><%= Bean.webposXML.getfieldTransl("user_param_fio_nat_prs", false) %></td> <td><input type="text" name="fio_nat_prs" size="30" value="<%= Bean.getLoginUserNatPrsFIO() %>" readonly class="inputfield-ro"> </td>
						</tr>
						<tr>
							<td colspan="2"><%= Bean.webposXML.getfieldTransl("text_message", true) %></td>
						</tr>
						<tr>
							<td colspan="2"><textarea name="text_message" cols="71" rows="10" class="inputfield"></textarea></td>
						</tr>
						<tr>
							<td colspan="2"  align="center">
								<%=Bean.getSubmitButtonAjax("admin/help.jsp", "send", "updateForm", "div_main", "validateMessage") %>
						        <%=Bean.getSubmitButtonAjax("admin/help.jsp", "button_back", "updateForm2", "div_main") %>
							</td>
						</tr>
					</table>
				</form>			
				<form name="updateForm2" id="updateForm2" accept-charset="UTF-8" method="POST">
			        <input type="hidden" name="action" value="show">
				</form>
			<% } else if ("edit".equalsIgnoreCase(action) || "reply".equalsIgnoreCase(action)) { %>
			<%	
				bcUserMessageObject message = new bcUserMessageObject(id_message);
			%>
			<script>
		
			function validateMessage(){
				var returnValue = null;
				var formParam = new Array (
					new Array ('text_message', 'varchar2', 1)
				);
				returnValue = validateFormForID(formParam, 'updateForm');
				return returnValue;
			}
			</script>

				<form name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST">
					<% if ("edit".equalsIgnoreCase(action)) { %>
			        <input type="hidden" name="action" value="reply">
					<% } else { %>	
			        <input type="hidden" name="action" value="send">
					<% } %>
			        <input type="hidden" name="id_message" value="<%=id_message %>">
			        <input type="hidden" name="id_user" value="<%=Bean.getLoginUserId() %>">
					<table <%=Bean.getTableDetail2Param() %>>
						<tr>
							<td <%=Bean.getDetailTableFirstColumnWidth() %>><%= Bean.webposXML.getfieldTransl("user_param_name_user", false) %></td> <td><input type="text" name="name_user" size="30" value="<%= Bean.getLoginUserName() %>" readonly class="inputfield-ro"> </td>
						</tr>
						<tr>
							<td><%= Bean.webposXML.getfieldTransl("user_param_fio_nat_prs", false) %></td> <td><input type="text" name="fio_nat_prs" size="30" value="<%= Bean.getLoginUserNatPrsFIO() %>" readonly class="inputfield-ro"> </td>
						</tr>
						<tr>
							<td colspan="2"><%= Bean.webposXML.getfieldTransl("text_message", true) %></td>
						</tr>
						<tr>
							<% if ("edit".equalsIgnoreCase(action)) { %>
							<td colspan="2"><textarea name="text_message" cols="71" rows="10" readonly class="inputfield-ro"><%=message.getValue("TEXT_MESSAGE") %></textarea></td>
							<% } else { %>	
							<td colspan="2"><textarea name="text_message" cols="71" rows="10" class="inputfield"><%=Bean.replyMessage(message.getValue("TEXT_MESSAGE")) %></textarea></td>
							<% } %>
						</tr>
						<tr>
							<td colspan="2"  align="center">
								<% if ("edit".equalsIgnoreCase(action)) { %>
								<%=Bean.getSubmitButtonAjax("admin/help.jsp", "button_message_reply", "updateForm", "div_main", "validateMessage") %>
								<% } else { %>
								<%=Bean.getSubmitButtonAjax("admin/help.jsp", "send", "updateForm", "div_main", "validateMessage") %>
								<% } %>
								<%=Bean.getSubmitButtonAjax("admin/help.jsp", "button_back", "updateForm2", "div_main") %>
							</td>
						</tr>
					</table>
				</form>			
				<form name="updateForm2" id="updateForm2" accept-charset="UTF-8" method="POST">
			        <input type="hidden" name="action" value="show">
				</form>
				<% } else if ("send".equalsIgnoreCase(action)) { %>
				<%
				ArrayList<String> pParam = new ArrayList<String>();
				
				pParam.add(Bean.getLoginUserId());
				pParam.add(Bean.getCurrentTerm());
				pParam.add(text_message);
				
				String[] results = new String[3];
				
				results 					= Bean.executeFunction("PACK$WEBPOS_UI.user_message_send", pParam, results.length);
				String resultInt 			= results[0];
				String id_user_message      = results[1];
		 		String resultMessage 		= results[2];

				StringBuilder html = new StringBuilder();
				html.append(Bean.loginUser.getUserMessagesHTML(message_find, l_message_page_beg, l_message_page_end));
				
				%>
				<form name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST">
			        <input type="hidden" name="action" value="new">
			        <input type="hidden" name="id_user" value="<%=Bean.getLoginUserId() %>">
					<table <%=Bean.getTableDetail2Param() %>>
						<tr>
							<td colspan="2"  align="center">
								<%=Bean.getSubmitButtonAjax("admin/help.jsp", "button_message_new", "updateForm", "div_main") %>
							</td>
						</tr>
						<tr>
							<td>
								<table <%=Bean.getTableBottomFilter() %>>
								  	<tr>
									<%= Bean.getFindHTML("message_find", message_find, "admin/help.jsp?message_page=1&", "div_main") %>
									
									
								    <!-- Вывод страниц -->
									<%= Bean.getPagesHTML(pageFormName + tagMessage, "admin/help.jsp?", "message_page", Bean.loginUser.getMessagesCount(), "div_main") %>
								  	</tr>
								</table>
							</td>
						</tr>
					</table>
					<%=html.toString() %>
				</form>
				<% } %>
			</td>
		</tr>
	</table>
<%} %>

<% if ("4".equalsIgnoreCase(tab)) { %>
	<table <%=Bean.getTableDetail2Param() %> style="padding: 15px 0 25px 5px; font-size:12px !important;">
		<tbody>
			<tr><td <%=Bean.getDetailTableFirstColumnWidth() %>><%=Bean.webposXML.getfieldTransl("application_name", false) %></td><td>&nbsp;</td></tr>
			<tr><td><%=Bean.webposXML.getfieldTransl("application_description", false) %></td><td>&nbsp;</td></tr>
			<tr><td><%=Bean.webposXML.getfieldTransl("application_version", false) %></td><td><%=Bean.webposXML.getfieldTransl("application_version_value", false) %></td></tr>
			<tr><td>&nbsp;</td><td><%=Bean.getVersion() %></td></tr>
		</tbody>
	</table>
<% } %>

</div>
				</div>
			</td>
		</tr>
	</table>
<!-- </div>  -->

	<% } else { %>
	<table <%=Bean.getTableDetail2Param() %>>
		<tr>
			<td>
				<div id="div_action_big">
				<h1><%=Bean.webposXML.getfieldTransl("title_setting_help", false) %><%=Bean.getHelpButton("help", "div_action_big") %></h1>
					<table class="action_table">
						<tr><td align="center" style="padding: 10px;"><font style="font-size: 22px; color:red; font-weight: bold;"><%=Bean.webposXML.getfieldTransl("operation_error", false) %></font></td></tr>
						<tr><td align="center"><%=Bean.webposXML.getfieldTransl("title_access_denied", false) %></td></tr>
						<tr><td align="center">&nbsp;</td></tr>
					</table>
				</div>
			</td>
		</tr>
	</table>

	<% } %>
<% } %>
</body>
</html>