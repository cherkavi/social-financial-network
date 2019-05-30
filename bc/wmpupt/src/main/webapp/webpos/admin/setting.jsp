<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcWebPosBean"  />

<%@page import="java.util.HashMap"%>
<%@page import="webpos.wpUserObject"%>
<%@page import="java.util.ArrayList"%>
<%@page import="webpos.wpChequeObject"%>
<%@page import="webpos.wpTerminalObject"%><html>

<% 
String pageFormName = "WEBPOS_ADMIN_SETTING";

String tagTermLoy = "_TERM_LOY";
String tagTermLoyFind = "_TERM_LOY_FIND";

String tagAccess = "_ACCESS";
String tagAccessFind = "_ACCESS_FIND";

Bean.setJspPageForTabName(pageFormName);

HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String id_term = Bean.getCurrentTerm();
id_term 		= Bean.isEmpty(id_term)?Bean.getCurrentTerm():id_term;

String id_loy_line = Bean.getDecodeParam(parameters.get("id_loy_line"));
id_loy_line		= Bean.isEmpty(id_loy_line)?"":id_loy_line;

String id_card_pack = Bean.getDecodeParam(parameters.get("id_card_pack"));
id_card_pack	= Bean.isEmpty(id_card_pack)?"":id_card_pack;

String cheque_format = Bean.getChequeSaveFormat();

String access_type = Bean.getDecodeParam(parameters.get("type"));
//String access_process = Bean.getDecodeParam(parameters.get("process"));
String access_action = Bean.getDecodeParam(parameters.get("action"));
if (access_type == null || "".equalsIgnoreCase(access_type) || "null".equalsIgnoreCase(access_type)) {
	access_type = "show";
}

%>
<% 
	Bean.loginTerm.getFeature();
	
	//Обрабатываем номера страниц
	String l_term_loy = Bean.getDecodeParam(parameters.get("term_loy_page"));
	Bean.pageCheck(pageFormName + tagTermLoy, l_term_loy);
	String l_term_loy_beg = Bean.getFirstRowNumber(pageFormName + tagTermLoy);
	String l_term_loy_end = Bean.getLastRowNumber(pageFormName + tagTermLoy);
	
	String term_loy_find 	= Bean.getDecodeParam(parameters.get("term_loy_find"));
	term_loy_find 	= Bean.checkFindString(pageFormName + tagTermLoyFind, term_loy_find, l_term_loy);

	String l_access_page = Bean.getDecodeParam(parameters.get("access_page"));
	Bean.pageCheck(pageFormName + tagAccess, l_access_page);
	String l_access_page_beg = Bean.getFirstRowNumber(pageFormName + tagAccess);
	String l_access_page_end = Bean.getLastRowNumber(pageFormName + tagAccess);
	
	String access_find 	= Bean.getDecodeParam(parameters.get("access_find"));
	access_find 	= Bean.checkFindString(pageFormName + tagAccessFind, access_find, l_access_page);
	
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

	boolean tab1HasPermission = Bean.hasReadMenuPermission("WEBPOS_ADMIN_SETTING_USER");
	boolean tab2HasPermission = Bean.hasReadMenuPermission("WEBPOS_ADMIN_SETTING_TERMINAL");
	boolean tab3HasPermission = Bean.hasReadMenuPermission("WEBPOS_ADMIN_SETTING_ADMINISTRATION");
	int tabCount = (tab1HasPermission?1:0)+(tab2HasPermission?1:0)+(tab3HasPermission?1:0);

	if ("1".equalsIgnoreCase(tab) && !tab1HasPermission) {
		tab = !tab2HasPermission?(!tab3HasPermission?"4":"3"):"2";
	} else if ("2".equalsIgnoreCase(tab) && !tab2HasPermission) {
		tab = !tab1HasPermission?(!tab3HasPermission?"4":"3"):"1";
	} else if ("3".equalsIgnoreCase(tab) && !tab3HasPermission) {
		tab = !tab1HasPermission?(!tab2HasPermission?"4":"2"):"1";
	}
	
	Bean.tabsHmSetValue(pageFormName, tab);


	%>
    
	<% if (tabCount > 0) { %>
	<table <%=Bean.getTableDetail2Param() %>>
			<tr>
				<td>
	<div id="div_action_big">
		<div>
		<h1>
		<%if ("1".equalsIgnoreCase(tab) && tab1HasPermission) {%>
		<%=Bean.webposXML.getfieldTransl("title_setting_user", false) %>
		<% } %>
		<%if ("2".equalsIgnoreCase(tab) && tab2HasPermission) {%>
		<%=Bean.webposXML.getfieldTransl("title_setting_terminal", false) %>
		<% } %>
		<%if ("3".equalsIgnoreCase(tab) && tab3HasPermission) {%>
		<%=Bean.webposXML.getfieldTransl("title_setting_administration", false) %>
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
					<li <%if ("1".equalsIgnoreCase(tab)) {%> class="current"<% } %>><a href="#"><span onclick="ajaxpage('admin/setting.jsp?tab=1', 'div_main')"><%=Bean.webposXML.getfieldTransl("title_setting_user", false) %></span></a></li>
					<% } %>
					<% if (tab2HasPermission) { %>
					<li <%if ("2".equalsIgnoreCase(tab)) {%> class="current"<% } %>><a href="#"><span onclick="ajaxpage('admin/setting.jsp?tab=2', 'div_main')"><%=Bean.webposXML.getfieldTransl("title_setting_terminal", false) %></span></a></li>
					<% } %>
					<% if (tab3HasPermission) { %>
					<li <%if ("3".equalsIgnoreCase(tab)) {%> class="current"<% } %>><a href="#"><span onclick="ajaxpage('admin/setting.jsp?tab=3', 'div_main')"><%=Bean.webposXML.getfieldTransl("title_setting_administration", false) %></span></a></li>
					<% } %>
					</ul>
					</div>
				</td>
			</tr>
		</table>
		<% } %>
	</div>
	<div id="div_data">
	<div id="div_data_detail">
	<% if ("1".equalsIgnoreCase(tab) && tab1HasPermission) { 

		wpUserObject user = new wpUserObject(Bean.getLoginUserIdTermUser());
		%>
		<script>
		function validateParam(){
			var returnValue = null;
			var formParam = new Array (
				new Array ('id_theme', 'varchar2', 1)
			);
			returnValue = validateFormForID(formParam, 'updateForm');
			return returnValue;
		}
		</script>
					<form name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST">
				        <input type="hidden" name="type" value="param">
				        <input type="hidden" name="action" value="online">
				        <input type="hidden" name="process" value="yes">
				        <input type="hidden" name="id_term_user" value="<%=user.getValue("ID_TERM_USER") %>">
						<table class="action_table" style="font-family: arial; font-size:12px;">
							<tr>
								<td <%=Bean.getDetailTableFirstColumnWidth() %>><%= Bean.webposXML.getfieldTransl("user_param_name_user", false) %></td> <td colspan="3"><input type="text" name="id_term" size="25" value="<%= user.getValue("NAME_USER") %>" readonly class="inputfield_finish_green inputfield_finish_small_font"> </td>
							</tr>
							<tr>
								<td><%= Bean.webposXML.getfieldTransl("user_param_fio_nat_prs", false) %></td> <td colspan="3"><input type="text" name="fio_nat_prs" size="25" value="<%= user.getValue("FIO_NAT_PRS") %>" readonly class="inputfield_finish_green inputfield_finish_small_font"> </td>
							</tr>
							<tr>
								<td><%= Bean.webposXML.getfieldTransl("user_work_place", false) %></td> <td colspan="3"><input type="text" name="sname_jur_prs" size="25" value="<%= user.getValue("SNAME_JUR_PRS") %>" readonly class="inputfield_finish_blue inputfield_finish_small_font"> </td>
							</tr>
							<tr>
								<td><%= Bean.webposXML.getfieldTransl("user_param_id_term", false) %></td> <td colspan="3"><input type="text" name="id_term" size="25" value="<%= user.getValue("ID_TERM") %>" readonly class="inputfield_finish_blue inputfield_finish_small_font"> </td>
							</tr>
							<tr>
								<td><%= Bean.webposXML.getfieldTransl("user_role", false) %></td> <td colspan="3"><input type="text" name="id_user_role" size="25" value="<%= user.getValue("NAME_ROLE") %>" readonly class="inputfield_finish_red inputfield_finish_small_font"> </td>
							</tr>
							<% String cd_user_access_type = user.getValue("CD_TERM_USER_ACCESS_TYPE");
								String accessTypeName = "";
								if ("CASHIER".equalsIgnoreCase(cd_user_access_type)) {
									accessTypeName = Bean.webposXML.getfieldTransl("title_access_type_cashier", false);
								} else if ("MANAGER".equalsIgnoreCase(cd_user_access_type)) {
									accessTypeName = Bean.webposXML.getfieldTransl("title_access_type_manager", false);
								} else {
									accessTypeName = Bean.webposXML.getfieldTransl("title_access_type_unknown", false);
								}
								%>
							<tr>
								<td><%= Bean.webposXML.getfieldTransl("title_access_type", false) %></td> <td colspan="3"><input type="text" name="cd_user_access_type" size="25" value="<%= accessTypeName %>" readonly class="inputfield_finish_red inputfield_finish_small_font"> </td>
							</tr>
							
							<% boolean hasPutCardPermission = Bean.hasWriteMenuPermission("WEBPOS_SERVICE_CARD_ACTIVATION_REGISTRATION"); %>
							<% boolean hasQuestiooairePermission = Bean.hasWriteMenuPermission("WEBPOS_SERVICE_CARD_ACTIVATION_QUESTIONNAIRE"); %>
							<% boolean hasActivationCardCardPermission = Bean.hasWriteMenuPermission("WEBPOS_SERVICE_CARD_ACTIVATION_ACTIVATION"); %>
							<% String cd_card1_title = "";
							   boolean cardExist = false;	
								if (!Bean.isEmpty(user.getValue("CD_CARD1"))) {
									cd_card1_title = user.getValue("CD_CARD1_HIDE");
									cardExist = true;
								} else {
									cd_card1_title = Bean.webposXML.getfieldTransl("user_cd_card1_none", false);
								}
								String putCardCaption = "";
								if (hasPutCardPermission && !cardExist) {
									putCardCaption = "(<span class=\"go_to\" onclick=\"ajaxpage('action/new_client.jsp?tab=1&id_user=" + user.getValue("ID_USER")+ "', 'div_main')\" title=\"" + Bean.webposXML.getfieldTransl("title_user_give_card", false) + "\">" + Bean.webposXML.getfieldTransl("title_user_give_card_short", false) + "</span>)";
								}
							%>
							<tr>
								<% if (!cardExist) { %>
								<td><%= Bean.webposXML.getfieldTransl("user_cd_card1", false) %></td> <td colspan="3"><input type="text" name="user_cd_card1" size="25" value="<%= Bean.webposXML.getfieldTransl("user_cd_card1_none", false) %>" readonly class="inputfield_finish_red"> </td>
								<% } else { %>
								<td><%= Bean.webposXML.getfieldTransl("user_cd_card1", false) %></td> 
									<td colspan="3">
									<span id="user_cd_card1" class="inputfield_finish_blue"><%= cd_card1_title %></span>
									<% if ("GIVEN".equalsIgnoreCase(user.getValue("CD_NAT_PRS_ROLE_STATE")) && hasQuestiooairePermission) { %>
										&nbsp;(<span class="go_to" onclick="ajaxpage('action/new_client_questionnaire.jsp?id_role=<%= user.getValue("ID_NAT_PRS_ROLE")%>&type=questionnaire&action=edit&process=no&id_user=<%= user.getValue("ID_USER")%>', 'div_action_big')" title="<%=Bean.buttonXML.getfieldTransl("button_questionnaire", false) %>"><%=Bean.buttonXML.getfieldTransl("button_questionnaire", false) %></span>)
									<% } else if ("QUESTIONED".equalsIgnoreCase(user.getValue("CD_NAT_PRS_ROLE_STATE")) && hasActivationCardCardPermission) { %>
										&nbsp;(<span class="go_to" onclick="ajaxpage('action/new_client_activation.jsp?cd_card1=<%= user.getValue("CD_CARD1")%>&type=client&process=yes&action=check_card&back_type=user&id_user=<%= user.getValue("ID_USER")%>', 'div_action_big')" title="<%=Bean.buttonXML.getfieldTransl("activate", false) %>"><%=Bean.buttonXML.getfieldTransl("activate", false) %></span>)
									<% } %> 
									</td>
								<% } %>
							</tr>
							<%
								String isUserTestMode = user.getValue("IS_TEST_MODE");
							
								wpTerminalObject term = new wpTerminalObject(user.getValue("ID_TERM"));
								term.getFeature();
								String isTermTestMode = term.getValue("CAN_TEST_MODE");
								
								String isTestModeTitle = Bean.commonXML.getfieldTransl("no", false);
								String isTestModeStyle = "inputfield_finish_green";
								if ("Y".equalsIgnoreCase(isUserTestMode)) {
									isTestModeTitle = Bean.commonXML.getfieldTransl("yes", false);
									isTestModeStyle = "inputfield_finish_red";
								}
							%>
							<% if ("Y".equalsIgnoreCase(isTermTestMode)) { %>
							<tr>
								<td><%= Bean.userXML.getfieldTransl("is_test_mode", false) %></td> <td colspan="3"><input type="text" name="id_term" size="25" value="<%= isTestModeTitle %>" readonly class="<%=isTestModeStyle %> inputfield_finish_small_font"> </td>
							</tr>
							<% } %>
							<tr>
								<td><%= Bean.webposXML.getfieldTransl("text_theme", true) %></td> 
								<td colspan="3">
									<%=Bean.getSelectBeginHTML("id_theme", Bean.webposXML.getfieldTransl("text_theme", false)) %>
									<%=Bean.getThemeOptions(Bean.getCurrentThemeFolder(), true) %> 
									<%=Bean.getSelectEndHTML() %>
								</td>
							</tr>
							<tr>
								<td><%= Bean.webposXML.getfieldTransl("title_cheque_save_format", true) %></td> 
								<td colspan="3">
									<%=Bean.getSelectBeginHTML("cheque_format", Bean.webposXML.getfieldTransl("title_cheque_save_format", true)) %>
									<%=Bean.getSelectOptionHTML(cheque_format, "TXT", "TXT") %> 
									<%=Bean.getSelectOptionHTML(cheque_format, "XML", "XML") %> 
									<%=Bean.getSelectEndHTML() %>
								</td>
							</tr>
							<tr>
								<tr><td colspan="4"  align="center"><%=Bean.getSubmitButtonAjax("admin/user_paramupdate.jsp", "button_save", "updateForm", "div_action_big", "validateParam") %></td>
							</tr>
						</table>
					</form>


	<% } else if ("2".equalsIgnoreCase(tab) && tab2HasPermission) { %>
		<script type="text/javascript">
		function show_desc(name) {
			var elem = document.getElementById(name);
			//alert(name + " - " + elem.style.display);
			if (elem.style.display == 'none' || elem.style.display == '') {
				//elem.style.visibility = "hidden";
				elem.style.display = "inline";
			} else {
				//elem.style.visibility = "visible";
				elem.style.display = "none";
			}
		}
		</script>
		<table class="action_table" style="font-family: arial; font-size:11px;">
			
			<tr>
				<td width="250"><%= Bean.terminalXML.getfieldTransl("id_term", false) %></td> <td><span class="inputfield_finish_blue inputfield_finish_small_font"><%= id_term %></span></td>
			</tr>
			<tr>
				<td><%= Bean.terminalXML.getfieldTransl("cd_term_currency", false) %></td> <td><span class="inputfield_finish_blue inputfield_finish_small_font"><%= Bean.loginTerm.getValue("SNAME_TERM_CURRENCY") %></span></td>
			</tr>
			<tr>
				<td valign="top"><%= Bean.webposXML.getfieldTransl("title_online_pay_cash_conf_type", false) %></td>  <td><span class="<%=("NONE".equalsIgnoreCase(Bean.loginTerm.getValue("CD_ONLINE_PAY_CASH_CONF_TYPE"))?"inputfield_finish_red":"inputfield_finish_green") %> inputfield_finish_small_font"><%=Bean.getTermPayConfirmationWayName(Bean.loginTerm.getValue("CD_ONLINE_PAY_CASH_CONF_TYPE")) %></span></td>
			</tr>
			<tr>
				<td valign="top"><%= Bean.webposXML.getfieldTransl("title_online_pay_card_conf_type", false) %></td>  <td><span class="<%=("NONE".equalsIgnoreCase(Bean.loginTerm.getValue("CD_ONLINE_PAY_CARD_CONF_TYPE"))?"inputfield_finish_red":"inputfield_finish_green") %> inputfield_finish_small_font"><%=Bean.getTermPayConfirmationWayName(Bean.loginTerm.getValue("CD_ONLINE_PAY_CARD_CONF_TYPE")) %></span></td>
			</tr>
			<tr>
				<td valign="top"><%= Bean.webposXML.getfieldTransl("title_online_pay_point_conf_type", false) %></td>  <td><span class="<%=("NONE".equalsIgnoreCase(Bean.loginTerm.getValue("CD_ONLINE_PAY_POINT_CONF_TYPE"))?"inputfield_finish_red":"inputfield_finish_green") %> inputfield_finish_small_font"><%=Bean.getTermPayConfirmationWayName(Bean.loginTerm.getValue("CD_ONLINE_PAY_POINT_CONF_TYPE")) %></span></td>
			</tr>
			<tr>
				<td valign="top"><%= Bean.webposXML.getfieldTransl("title_online_pay_robokassa_conf_type", false) %></td>  <td><span class="<%=("NONE".equalsIgnoreCase(Bean.loginTerm.getValue("CD_ONLINE_PAY_RBK_CONF_TYPE"))?"inputfield_finish_red":"inputfield_finish_green") %> inputfield_finish_small_font"><%=Bean.getTermPayConfirmationWayName(Bean.loginTerm.getValue("CD_ONLINE_PAY_RBK_CONF_TYPE")) %></span></td>
			</tr>
			<tr>
				<td valign="top"><%= Bean.webposXML.getfieldTransl("title_term_pay_robokassa_permission", false) %></td>  <td><span class="<%=("N".equalsIgnoreCase(Bean.loginTerm.getValue("HAS_ONLINE_PAY_RBK_PERMISSION"))?"inputfield_finish_red":"inputfield_finish_green") %> inputfield_finish_small_font"><%=Bean.getMeaningFoCodeValue("YES_NO", Bean.loginTerm.getValue("HAS_ONLINE_PAY_RBK_PERMISSION")) %></span></td>
			</tr>
			<tr>
				<td colspan="4">&nbsp;</td>
			</tr>
	
			<tr>
				<td valign="top" colspan="2" class="top_line_gray_dashed"><b><%= Bean.webposXML.getfieldTransl("title_setting_card_pack", false) %></b></td>
			</tr>
			<tr>
				<td colspan="4">
				<%= Bean.loginTerm.getCardPackLinesHTML("", id_card_pack, "1", "100") %>
				</td>
			</tr>
			<tr>
				<td colspan="4">&nbsp;</td>
			</tr>
	
			<tr>
				<td valign="top" colspan="2" class="top_line_gray_dashed"><b><%= Bean.webposXML.getfieldTransl("title_setting_loyality", false) %></b></td>
			</tr>
			<tr>
				<td><%= Bean.terminalXML.getfieldTransl("ext_loyl", false) %></td> <td><span class="<%=("0".equalsIgnoreCase(Bean.loginTerm.getValue("EXT_LOYL","LOYALITY"))?"inputfield_finish_red":"inputfield_finish_green") %> inputfield_finish_small_font"><%= Bean.loginTerm.getValue("EXT_LOYL_TSL","LOYALITY") %></span> </td>
			</tr>
			<tr>
				<td><%= Bean.terminalXML.getfieldTransl("MAX_BONUS", false) %> <span class="loyality_uses_caption" onclick="show_desc('desc_max_bonus')">&nbsp;</span><br><span class="loyality_uses_description" id="desc_max_bonus">Максимальная сумма начисленных баллов для одной оплаты</span></td> <td><span class="inputfield_finish_green inputfield_finish_small_font" title="MAX_BONUS"><%= Bean.loginTerm.getValue("MAX_BONUS_FRMT","LOYALITY") %></span></td>
			</tr>
			<tr>
				<td><%= Bean.loyXML.getfieldTransl("term_limit_cash", false) %> <span class="loyality_uses_caption" onclick="show_desc('desc_limit_cash')">&nbsp;</span><br><span class="loyality_uses_description" id="desc_limit_cash">Максимальная сумма оплаты наличными для одной оплаты</span></td> <td><span class="inputfield_finish_green inputfield_finish_small_font" title="TR_LIMIT"><%= Bean.loginTerm.getValue("LIMIT_CASH_FRMT","LOYALITY") %></span></td>
			</tr>
			<tr>
				<td><%= Bean.terminalXML.getfieldTransl("TYPE_CALC_TSL", false) %> <span class="loyality_uses_caption" onclick="show_desc('desc_type_calc')">&nbsp;</span><br><span class="loyality_uses_description" id="desc_type_calc">Возможные способы расчета:<br> - от суммы платежа;<br> - от суммы платежа за вычетом скидки</span></td><td><span class="inputfield_finish_green inputfield_finish_small_font" title="TYPE_CALC"><%= Bean.getMeaningForNumValue("TYPE_CALC", Bean.loginTerm.getValue("TYPE_CALC","LOYALITY")) %></span></td>
			</tr>
			<tr>
				<td><%= Bean.terminalXML.getfieldTransl("CASH_BON_TSL", false) %></td><td><span class="inputfield_finish_green inputfield_finish_small_font" title="CASH_BON"><%= Bean.getMeaningForNumValue("YES_NO", Bean.loginTerm.getValue("CASH_BON","LOYALITY")) %></span></td>
			</tr>
			<tr>
				<td><%= Bean.terminalXML.getfieldTransl("BON_BON", false) %></td> <td><span class="inputfield_finish_green inputfield_finish_small_font" title="BON_BON"><%= Bean.getMeaningForNumValue("YES_NO", Bean.loginTerm.getValue("BON_BON","LOYALITY")) %></span></td>
			</tr>
			<tr>
				<td colspan="4">
				<%= Bean.loginTerm.getTermLoyalityLinesHTML("", "", "", "", "", id_loy_line, "1", "100") %>
				</td>
			</tr>
		</table>


	<% } else if ("3".equalsIgnoreCase(tab) && tab3HasPermission) { %>	
		<% 
		String term_code_country = Bean.loginTerm.getValue("ADR_CODE_COUNTRY_SERVICE_PLACE", "TERM");
		if ("show".equalsIgnoreCase(access_type)){ 
			StringBuilder html = new StringBuilder();
			html.append(Bean.loginUser.getWebPOSAccessHTML(access_find, l_access_page_beg, l_access_page_end));
			%>
			<table class="action_table">
					<tr>
						<td>
							<table <%=Bean.getTableBottomFilter() %>>
								<tr><td colspan="20" class="center">
									<%=Bean.getSubmitButtonAjax("admin/setting.jsp", "button_add", "updateForm33", "div_main") %> 
								</td></tr>
							  	<tr>
								<%= Bean.getFindHTML("access_find", access_find, "admin/setting.jsp?access_page=1&", "div_main") %>
								
								
							    <!-- Вывод страниц -->
								<%= Bean.getPagesHTML(pageFormName + tagAccess, "admin/setting.jsp?", "access_page", Bean.loginUser.getAccessUserCount(), "div_main") %>
							  	</tr>
							</table>
			<%= html.toString()%>
						</td>
					</tr>
				</table>
				
				<form name="updateForm33" id="updateForm33" accept-charset="UTF-8" method="POST">
					<input type="hidden" name="type" value="add_user">
					<input type="hidden" name="action" value="enter">
					<input type="hidden" name="tab" value="3">
				</form>
		<% } else if ("edit".equalsIgnoreCase(access_type)){ 
				String resultInt 				= "";
		 		String resultMessage 			= "";
				System.out.println("access_action="+access_action);
				String 
					id_term_user			= Bean.getDecodeParam(parameters.get("id_term_user")),
					cd_user_status			= Bean.getDecodeParam(parameters.get("cd_user_status")),
					id_user_term			= Bean.getDecodeParam(parameters.get("id_user_term")),
					id_user_role			= Bean.getDecodeParam(parameters.get("id_user_role")),
					phone_mobile			= Bean.getDecodeParam(parameters.get("phone_mobile")),
					email					= Bean.getDecodeParam(parameters.get("email")),
					cd_user_access_type		= Bean.getDecodeParam(parameters.get("cd_user_access_type")),
					is_test_mode			= Bean.getDecodeParam(parameters.get("is_test_mode")),
					id_service_place_work	= Bean.getDecodeParam(parameters.get("id_service_place_work"));
				
				if ("update".equalsIgnoreCase(access_action)) {
					
					ArrayList<String> pParam = new ArrayList<String>();
					
					pParam.add(id_term_user);
					pParam.add(phone_mobile);
					pParam.add(email);
					pParam.add(cd_user_status);
					pParam.add(id_user_term);
					pParam.add(id_user_role);
					pParam.add(cd_user_access_type);
					pParam.add(is_test_mode);
					
					String[] results = new String[2];
					
					results 		= Bean.executeFunction("PACK$WEBPOS_UI.update_webpos_user", pParam, results.length);
					resultInt 		= results[0];
			 		resultMessage 	= results[1];
				}
			
				wpUserObject user = new wpUserObject(id_term_user);
				
				if ("edit".equalsIgnoreCase(access_action) || ("update".equalsIgnoreCase(access_action) && Bean.C_SUCCESS_RESULT.equalsIgnoreCase(resultInt))) {
					cd_user_status			= user.getValue("CD_TERM_USER_STATUS");
					id_user_term			= user.getValue("ID_TERM");
					id_user_role			= user.getValue("ID_ROLE");
					phone_mobile			= user.getValue("PHONE_MOBILE");
					email					= user.getValue("EMAIL");
					cd_user_access_type		= user.getValue("CD_TERM_USER_ACCESS_TYPE");
					is_test_mode			= user.getValue("IS_TEST_MODE");
					id_service_place_work	= user.getValue("ID_SERVICE_PLACE_WORK");
					System.out.println("get parameters");
				}
			%>
			<script>
			
				function validateParam(){
					var returnValue = null;
					var formParam = new Array (
						new Array ('cd_user_status', 'varchar2', 1),
						new Array ('id_user_role', 'varchar2', 1),
						new Array ('id_user_term', 'varchar2', 1),
						new Array ('cd_user_access_type', 'varchar2', 1)					
					);
					returnValue = validateFormForID(formParam, 'updateForm');
					return returnValue;
				}
				phone_mask("phone_mobile","<%=user.getValue("ADR_CODE_COUNTRY")%>");
				</script>
			<form name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST">
				        <input type="hidden" name="type" value="edit">
				        <input type="hidden" name="action" value="update">
				        <input type="hidden" name="tab" value="3">
				        <input type="hidden" name="id_term_user" value="<%=user.getValue("ID_TERM_USER") %>">
						<table class="action_table">
							<tr><td colspan="4" align="center"><span style="color:blue; font-weight: bold; font-family: arial; font-size:16px;"><%=Bean.webposXML.getfieldTransl("title_update_user", false) %></span></td></tr>
							<tr>
								<td <%=Bean.getDetailTableFirstColumnWidth() %>><%= Bean.webposXML.getfieldTransl("user_param_name_user", false) %></td> <td colspan="3"><input type="text" name="name_user" size="25" value="<%= user.getValue("NAME_USER") %>" readonly class="inputfield_finish_green inputfield_finish_small_font"> </td>
							</tr>
							<tr>
								<td><%= Bean.webposXML.getfieldTransl("user_work_place", false) %></td> <td colspan="3"><input type="text" name="sname_jur_prs" size="25" value="<%= user.getValue("SNAME_SERVICE_PLACE_WORK") %>" readonly class="inputfield_finish_blue inputfield_finish_small_font"> </td>
							</tr>
							<tr>
								<td><%= Bean.webposXML.getfieldTransl("user_param_fio_nat_prs", false) %></td> <td colspan="3"><input type="text" name="fio_nat_prs" size="25" value="<%= user.getValue("FIO_NAT_PRS") %>" readonly class="inputfield_finish_green inputfield_finish_small_font"> </td>
							</tr>
							<tr>
								<td><%= Bean.webposXML.getfieldTransl("user_phone_mobile", false) %></td> <td colspan="3"><input type="text" name="phone_mobile" id=phone_mobile size="25" value="<%=phone_mobile %>" class="inputfield"><br><span class="warning_header"><%=Bean.webposXML.getfieldTransl("title_add_user_phone_mobile_warning", false) %></span></td>
							</tr>
							<tr>
								<td><%= Bean.webposXML.getfieldTransl("user_email", false) %></td> <td colspan="3"><input type="text" name="email" id=email size="25" value="<%=email %>" class="inputfield"></td>
							</tr>
							<tr>
								<td><%= Bean.webposXML.getfieldTransl("user_status_full", true) %></td> <td colspan="3"><select name="cd_user_status" id='cd_user_status' class="inputfield"><%=Bean.getUserStatusOptions(cd_user_status, true) %></select></td>
							</tr>
							<tr>
								<td><%= Bean.webposXML.getfieldTransl("id_term", true) %></td> <td colspan="3"><select name="id_user_term" id='id_user_term' class="inputfield"><%=Bean.getWebPOSAdminTerminalsOptions(id_user_term, true) %></select></td>
							</tr>
							<tr>
								<td><%= Bean.webposXML.getfieldTransl("user_role", true) %></td> <td colspan="3"><select name="id_user_role" id='id_user_role' class="inputfield"><%=Bean.getWebPOSRolesOptions(id_user_role, id_service_place_work, true) %></select></td>
							</tr>
							<tr>
								<td><%=Bean.webposXML.getfieldTransl("title_access_type", true) %></td>
								<td><%=Bean.getSelectBeginHTML("cd_user_access_type", Bean.webposXML.getfieldTransl("title_access_type", false)) %>
								 	<%=Bean.getSelectOptionHTML(cd_user_access_type, "", "") %>
								 	<%=Bean.getSelectOptionHTML(cd_user_access_type, "CASHIER", Bean.webposXML.getfieldTransl("title_access_type_cashier", false), "style=\"font-weight: bold; color: green;\"") %>
								 	<%=Bean.getSelectOptionHTML(cd_user_access_type, "MANAGER", Bean.webposXML.getfieldTransl("title_access_type_manager", false), "style=\"font-weight: bold; color: blue;\"") %>
								<%=Bean.getSelectOnChangeEndHTML() %></td>
							</tr>
							<% boolean hasPutCardPermission = Bean.hasWriteMenuPermission("WEBPOS_SERVICE_CARD_ACTIVATION_REGISTRATION"); %>
							<% boolean hasQuestiooairePermission = Bean.hasWriteMenuPermission("WEBPOS_SERVICE_CARD_ACTIVATION_QUESTIONNAIRE"); %>
							<% boolean hasActivationCardCardPermission = Bean.hasWriteMenuPermission("WEBPOS_SERVICE_CARD_ACTIVATION_ACTIVATION"); %>
							<% String cd_card1_title = "";
							   boolean cardExist = false;	
								if (!Bean.isEmpty(user.getValue("CD_CARD1"))) {
									cd_card1_title = user.getValue("CD_CARD1_HIDE");
									cardExist = true;
								} else {
									cd_card1_title = Bean.webposXML.getfieldTransl("user_cd_card1_none", false);
								}
								String putCardCaption = "";
								if (hasPutCardPermission && !cardExist) {
									putCardCaption = "(<span class=\"go_to\" onclick=\"ajaxpage('action/new_client.jsp?tab=1&id_user=" + user.getValue("ID_TERM_USER")+ "', 'div_main')\" title=\"" + Bean.webposXML.getfieldTransl("title_user_give_card", false) + "\">" + Bean.webposXML.getfieldTransl("title_user_give_card_short", false) + "</span>)";
								}
							%>
							<tr>
								<td><%= Bean.webposXML.getfieldTransl("user_cd_card1", false) %> <%=putCardCaption %></td> 
								<td colspan="3">
									<span id="user_cd_card1" class="inputfield_finish_red inputfield_finish_small_font"><%= cd_card1_title %></span>
									<% if (cardExist && ("GIVEN".equalsIgnoreCase(user.getValue("CD_NAT_PRS_ROLE_STATE"))) && hasQuestiooairePermission) { %>
										&nbsp;(<span class="go_to" onclick="ajaxpage('action/new_client_questionnaire.jsp?id_role=<%= user.getValue("ID_NAT_PRS_ROLE")%>&type=questionnaire&action=edit&process=no&id_user=<%= user.getValue("ID_USER")%>', 'div_action_big')" title="<%=Bean.buttonXML.getfieldTransl("button_questionnaire", false) %>"><%=Bean.buttonXML.getfieldTransl("button_questionnaire", false) %></span>)
									<% } else if (cardExist && "QUESTIONED".equalsIgnoreCase(user.getValue("CD_NAT_PRS_ROLE_STATE")) && hasActivationCardCardPermission) { %>
										&nbsp;(<span class="go_to" onclick="ajaxpage('action/new_client_activation.jsp?cd_card1=<%= user.getValue("CD_CARD1")%>&type=client&process=yes&action=check_card&back_type=user&id_user=<%= user.getValue("ID_USER")%>', 'div_action_big')" title="<%=Bean.buttonXML.getfieldTransl("activate", false) %>"><%=Bean.buttonXML.getfieldTransl("activate", false) %></span>)
									<% } %>
								</td>
							</tr>
							<tr>
								<td><%= Bean.userXML.getfieldTransl("is_test_mode", false) %></td> 
								<td colspan="3">
									<%=Bean.getSelectBeginHTML("is_test_mode", Bean.userXML.getfieldTransl("is_test_mode", false)) %>
									<%=Bean.getMeaningFromLookupNameOptions("YES_NO",is_test_mode,true) %> 
									<%=Bean.getSelectEndHTML() %>
								</td>
							</tr>
							<tr><td colspan="4"  align="center">&nbsp;</td>	</tr>
							<tr>
								<td colspan="4"  align="center">
									<%=Bean.getSubmitButtonAjax("admin/setting.jsp", "button_save", "updateForm", "div_main", "validateParam") %>
							        <%=Bean.getSubmitButtonAjax("admin/setting.jsp", "button_back", "updateForm2", "div_main") %>
								</td>
							</tr>
							<% if ("update".equalsIgnoreCase(access_action)) { %>
								<% if (!(Bean.C_SUCCESS_RESULT.equalsIgnoreCase(resultInt))) { %>
								<tr><td colspan="4"><span id="error_title"><br><%=Bean.webposXML.getfieldTransl("operation_error", false) %></span></td></tr>
								<tr><td colspan="4"><span id="error_description"><%=resultMessage %></span></td></tr>
								<% } else { %>
								<tr><td colspan="4"><span id="succes_title"><br><%=Bean.webposXML.getfieldTransl("operation_confirmation", false) %></span></td></tr>
								<tr><td colspan="4"><span id="succes_description"><%=resultMessage %></span></td></tr>
								<% } %>
							<% } %>
						</table>
					</form>	
					<form name="updateForm2" id="updateForm2" accept-charset="UTF-8" method="POST">
				        <input type="hidden" name="type" value="show">
					</form>


		<% } else if ("add_user".equalsIgnoreCase(access_type)){ %> 
			<%if ("enter".equalsIgnoreCase(access_action)) {
				//if ("no".equalsIgnoreCase(access_process)) {
				String resultInt 				= "";
		 		String resultMessage 			= "";
				ArrayList<String> pParam = new ArrayList<String>();
					
				pParam.add(Bean.getLoginUserServicePlaceId());
					
				String[] results = new String[3];
					
				results 				= Bean.executeFunction("PACK$WEBPOS_UI.get_next_name_user", pParam, results.length);
				resultInt 				= results[0];
				String name_new_user 	= results[1];
			 	resultMessage 			= results[2];
			
			%>
				<% if (!(Bean.C_SUCCESS_RESULT.equalsIgnoreCase(resultInt))) { %>
	
					<table class="action_table">
						<tr><td colspan="4" align="center"><span style="color:blue; font-weight: bold; font-family: arial; font-size:16px;"><%=Bean.webposXML.getfieldTransl("title_add_user", false) %></span></td></tr>
						<tr><td colspan="4"><span id="error_title"><br><%=Bean.webposXML.getfieldTransl("operation_error", false) %></span></td></tr>
						<tr><td colspan="4"><span id="error_description"><%=resultMessage %></span></td></tr>
						<tr>
							<td colspan="4"  align="center">
						        <%=Bean.getSubmitButtonAjax("admin/setting.jsp", "button_back", "updateForm2", "div_main") %>
							</td>
						</tr>
					</table>
					<form name="updateForm2" id="updateForm2" accept-charset="UTF-8" method="POST">
				        <input type="hidden" name="type" value="show">
					</form>
				<% } else { %>
			<script>
			
				function validateParam(){
					var returnValue = null;
					var formParam = new Array (
						new Array ('name_user', 'varchar2', 1),
						new Array ('surname_nat_prs', 'varchar2', 1),
						new Array ('name_nat_prs', 'varchar2', 1),
						new Array ('sex_nat_prs', 'varchar2', 1),
						new Array ('id_user_role', 'varchar2', 1),
						new Array ('id_user_term', 'varchar2', 1),
						new Array ('cd_user_access_type', 'varchar2', 1)					
					);
					returnValue = validateFormForID(formParam, 'updateForm');
					return returnValue;
				}
				phone_mask_empty("phone_mobile","<%=term_code_country%>");
				</script>
			<form name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST">
				        <input type="hidden" name="type" value="add_user">
				        <input type="hidden" name="action" value="add">
				        <input type="hidden" name="tab" value="3">
				        <table class="action_table" >
							<tr>
								<td <%=Bean.getDetailTableFirstColumnWidth() %>><%= Bean.webposXML.getfieldTransl("user_param_name_user", false) %></td> <td colspan="3"><input type="text" name="name_user" id="name_user" size="25" value="<%= name_new_user %>" readonly class="inputfield_finish_green inputfield_finish_small_font"> </td>
							</tr>
							<tr>
								<td><%= Bean.webposXML.getfieldTransl("user_work_place", false) %></td> <td colspan="3"><input type="text" name="sname_jur_prs" id="sname_jur_prs" size="25" value="<%= Bean.getLoginUserServicePlaceName() %>" class="inputfield_finish_blue inputfield_finish_small_font"> </td>
							</tr>
							<tr>
								<td><%= Bean.webposXML.getfieldTransl("user_surname_nat_prs", true) %></td> <td colspan="3"><input type="text" name="surname_nat_prs" id="surname_nat_prs" size="25" value="" class="inputfield"> </td>
							</tr>
							<tr>
								<td><%= Bean.webposXML.getfieldTransl("user_name_nat_prs", true) %></td> <td colspan="3"><input type="text" name="name_nat_prs" id="name_nat_prs" size="25" value="" class="inputfield"> </td>
							</tr>
							<tr>
								<td><%= Bean.webposXML.getfieldTransl("user_patronymic_nat_prs", false) %></td> <td colspan="3"><input type="text" name="patronymic_nat_prs" id="patronymic_nat_prs" size="25" value="" class="inputfield"> </td>
							</tr>
							<tr>
								<td><%= Bean.webposXML.getfieldTransl("user_sex_nat_prs", true) %></td> <td colspan="3"><select name="sex_nat_prs" id='sex_nat_prs' class="inputfield"><%=Bean.getMeaningFromLookupNameOptions("MALE_FEMALE", "M", true) %></select></td>
							</tr>
							<tr>
								<td><%= Bean.webposXML.getfieldTransl("user_phone_mobile", false) %></td> <td colspan="3"><input type="text" name="phone_mobile" id="phone_mobile" size="25" value="" class="inputfield"><br><span class="warning_header"><%=Bean.webposXML.getfieldTransl("title_add_user_phone_mobile_warning", false) %></span></td>
							</tr>
							<tr>
								<td><%= Bean.webposXML.getfieldTransl("user_email", false) %></td> <td colspan="3"><input type="text" name="email" id="email" size="25" value="" class="inputfield"></td>
							</tr>
							<tr>
								<td><%= Bean.webposXML.getfieldTransl("id_term", true) %></td> <td colspan="3"><select name="id_user_term" id='id_user_term' class="inputfield"><%=Bean.getWebPOSAdminTerminalsOptions(Bean.getCurrentTerm(), true) %></select></td>
							</tr>
							<tr>
								<td><%= Bean.webposXML.getfieldTransl("user_role", true) %></td> <td colspan="3"><select name="id_user_role" id='id_user_role' class="inputfield"><%=Bean.getWebPOSRolesOptions("", Bean.getLoginUserServicePlaceId(), true) %></select></td>
							</tr>
							<% String cd_user_access_type = "CASHIER";%>
							<tr>
								<td><%=Bean.webposXML.getfieldTransl("title_access_type", true) %></td>
								<td><%=Bean.getSelectBeginHTML("cd_user_access_type", Bean.webposXML.getfieldTransl("title_access_type", false)) %>
								 	<%=Bean.getSelectOptionHTML(cd_user_access_type, "", "") %>
								 	<%=Bean.getSelectOptionHTML(cd_user_access_type, "CASHIER", Bean.webposXML.getfieldTransl("title_access_type_cashier", false), "style=\"font-weight: bold; color: green;\"") %>
								 	<%=Bean.getSelectOptionHTML(cd_user_access_type, "MANAGER", Bean.webposXML.getfieldTransl("title_access_type_manager", false), "style=\"font-weight: bold; color: blue;\"") %>
								<%=Bean.getSelectOnChangeEndHTML() %></td>
							</tr>
							<tr>
								<td><%= Bean.userXML.getfieldTransl("is_test_mode", false) %></td> 
								<td colspan="3">
									<%=Bean.getSelectBeginHTML("is_test_mode", Bean.userXML.getfieldTransl("is_test_mode", false)) %>
									<%=Bean.getMeaningFromLookupNameOptions("YES_NO","N",true) %> 
									<%=Bean.getSelectEndHTML() %>
								</td>
							</tr>
							<tr>
								<td colspan="4"  align="center">
									<%=Bean.getSubmitButtonAjax("admin/setting.jsp", "button_save", "updateForm", "div_main", "validateParam") %>
							        <%=Bean.getSubmitButtonAjax("admin/setting.jsp", "button_back", "updateForm2", "div_main") %>
								</td>
							</tr>
							<tr><td colspan="2" class="left">
							<div id=div_hints>
								<i><b><%=Bean.webposXML.getfieldTransl("title_note", false) %>
								</b><br><%=Bean.webposXML.getfieldTransl("title_add_user_hint", false) %></i>
							</div>
							</td></tr>
						</table>
					</form>	
					<form name="updateForm2" id="updateForm2" accept-charset="UTF-8" method="POST">
				        <input type="hidden" name="type" value="show">
					</form>
				<% } %>
			<% } else if ("add".equalsIgnoreCase(access_action)) { 
				String resultInt 				= "";
		 		String resultMessage 			= "";
				String 
					name_user			= Bean.getDecodeParam(parameters.get("name_user")),
					surname_nat_prs		= Bean.getDecodeParam(parameters.get("surname_nat_prs")),
					name_nat_prs		= Bean.getDecodeParam(parameters.get("name_nat_prs")),
					patronymic_nat_prs	= Bean.getDecodeParam(parameters.get("patronymic_nat_prs")),
					sex_nat_prs			= Bean.getDecodeParam(parameters.get("sex_nat_prs")),
					phone_mobile		= Bean.getDecodeParam(parameters.get("phone_mobile")),
					email				= Bean.getDecodeParam(parameters.get("email")),
					id_user_term		= Bean.getDecodeParam(parameters.get("id_user_term")),
					id_user_role		= Bean.getDecodeParam(parameters.get("id_user_role")),
					cd_user_access_type	= Bean.getDecodeParam(parameters.get("cd_user_access_type")),
					is_test_mode		= Bean.getDecodeParam(parameters.get("is_test_mode"));
				
				//if ("add".equalsIgnoreCase(access_action)) {
					
				ArrayList<String> pParam = new ArrayList<String>();
					
				pParam.add(name_user);
				pParam.add(Bean.getLoginUserServicePlaceId());
				pParam.add(surname_nat_prs);
				pParam.add(name_nat_prs);
				pParam.add(patronymic_nat_prs);
				pParam.add(sex_nat_prs);
				pParam.add(phone_mobile);
				pParam.add(email);
				pParam.add(id_user_term);
				pParam.add(id_user_role);
				pParam.add(cd_user_access_type);
				pParam.add(is_test_mode);
					
				String[] results = new String[3];
					
				results 			= Bean.executeFunction("PACK$WEBPOS_UI.add_webpos_user", pParam, results.length);
				resultInt			= results[0];
				String id_new_user	= results[1];
			 	resultMessage 		= results[2];
			%>
				<% if (Bean.C_SUCCESS_RESULT.equalsIgnoreCase(resultInt)) { 
					StringBuilder html = new StringBuilder();
					html.append(Bean.loginUser.getWebPOSAccessHTML(access_find, l_access_page_beg, l_access_page_end)); %>
			<table class="action_table">
					<tr>
						<td>
							<table <%=Bean.getTableBottomFilter() %>>
								<tr><td colspan="20" class="center">
									<%=Bean.getSubmitButtonAjax("admin/setting.jsp", "button_add", "updateForm33", "div_main") %> 
								</td></tr>
							  	<tr>
								<%= Bean.getFindHTML("access_find", access_find, "admin/setting.jsp?access_page=1&", "div_main") %>
								
								
							    <!-- Вывод страниц -->
								<%= Bean.getPagesHTML(pageFormName + tagAccess, "admin/setting.jsp?", "access_page", Bean.loginUser.getAccessUserCount(), "div_main") %>
							  	</tr>
							</table>
			<%= html.toString()%>
						</td>
					</tr>
				</table>
				
				<form name="updateForm33" id="updateForm33" accept-charset="UTF-8" method="POST">
					<input type="hidden" name="type" value="add_user">
					<input type="hidden" name="action" value="enter">
					<input type="hidden" name="tab" value="3">
				</form>
				<% } else { %>
			<script>
			
				function validateParam(){
					var returnValue = null;
					var formParam = new Array (
						new Array ('name_user', 'varchar2', 1),
						new Array ('surname_nat_prs', 'varchar2', 1),
						new Array ('name_nat_prs', 'varchar2', 1),
						new Array ('sex_nat_prs', 'varchar2', 1),
						new Array ('id_user_role', 'varchar2', 1),
						new Array ('id_user_term', 'varchar2', 1),
						new Array ('cd_user_access_type', 'varchar2', 1)					
					);
					returnValue = validateFormForID(formParam, 'updateForm');
					return returnValue;
				}
				phone_mask_empty("phone_mobile","<%=term_code_country%>");
				</script>
			<form name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST">
				        <input type="hidden" name="type" value="add_user">
				        <input type="hidden" name="action" value="add">
				        <input type="hidden" name="tab" value="3">
						<table class="action_table">
							<tr>
								<td <%=Bean.getDetailTableFirstColumnWidth() %>><%= Bean.webposXML.getfieldTransl("user_param_name_user", false) %></td> <td colspan="3"><input type="text" name="name_user" id="name_user" size="25" value="<%= name_user %>" readonly class="inputfield_finish_green inputfield_finish_small_font"> </td>
							</tr>
							<tr>
								<td><%= Bean.webposXML.getfieldTransl("user_work_place", false) %></td> <td colspan="3"><input type="text" name="sname_jur_prs" id="sname_jur_prs" size="25" value="<%= Bean.getLoginUserServicePlaceName() %>" class="inputfield_finish_blue inputfield_finish_small_font"> </td>
							</tr>
							<tr>
								<td><%= Bean.webposXML.getfieldTransl("user_surname_nat_prs", true) %></td> <td colspan="3"><input type="text" name="surname_nat_prs" id="surname_nat_prs" size="25" value="<%=surname_nat_prs %>" class="inputfield"> </td>
							</tr>
							<tr>
								<td><%= Bean.webposXML.getfieldTransl("user_name_nat_prs", true) %></td> <td colspan="3"><input type="text" name="name_nat_prs" id="name_nat_prs" size="25" value="<%=name_nat_prs %>" class="inputfield"> </td>
							</tr>
							<tr>
								<td><%= Bean.webposXML.getfieldTransl("user_patronymic_nat_prs", false) %></td> <td colspan="3"><input type="text" name="patronymic_nat_prs" id="patronymic_nat_prs" size="25" value="<%=patronymic_nat_prs %>" class="inputfield"> </td>
							</tr>
							<tr>
								<td><%= Bean.webposXML.getfieldTransl("user_sex_nat_prs", true) %></td> <td colspan="3"><select name="sex_nat_prs" id='sex_nat_prs' class="inputfield"><%=Bean.getMeaningFromLookupNameOptions("MALE_FEMALE", sex_nat_prs, true) %></select></td>
							</tr>
							<tr>
								<td><%= Bean.webposXML.getfieldTransl("user_phone_mobile", false) %></td> <td colspan="3"><input type="text" name="phone_mobile" id=phone_mobile size="25" value="<%=phone_mobile %>" class="inputfield"><br><span class="warning_header"><%=Bean.webposXML.getfieldTransl("title_add_user_phone_mobile_warning", false) %></span></td>
							</tr>
							<tr>
								<td><%= Bean.webposXML.getfieldTransl("user_email", false) %></td> <td colspan="3"><input type="text" name="email" id=email size="25" value="<%=email %>" class="inputfield"></td>
							</tr>
							<tr>
								<td><%= Bean.webposXML.getfieldTransl("id_term", true) %></td> <td colspan="3"><select name="id_user_term" id='id_user_term' class="inputfield"><%=Bean.getWebPOSAdminTerminalsOptions(id_user_term, true) %></select></td>
							</tr>
							<tr>
								<td><%= Bean.webposXML.getfieldTransl("user_role", true) %></td> <td colspan="3"><select name="id_user_role" id='id_user_role' class="inputfield"><%=Bean.getWebPOSRolesOptions(id_user_role, Bean.getLoginUserServicePlaceId(), true) %></select></td>
							</tr>
							<tr>
								<td><%=Bean.webposXML.getfieldTransl("title_access_type", true) %></td>
								<td><%=Bean.getSelectBeginHTML("cd_user_access_type", Bean.webposXML.getfieldTransl("title_access_type", false)) %>
								 	<%=Bean.getSelectOptionHTML(cd_user_access_type, "", "") %>
								 	<%=Bean.getSelectOptionHTML(cd_user_access_type, "CASHIER", Bean.webposXML.getfieldTransl("title_access_type_cashier", false), "style=\"font-weight: bold; color: green;\"") %>
								 	<%=Bean.getSelectOptionHTML(cd_user_access_type, "MANAGER", Bean.webposXML.getfieldTransl("title_access_type_manager", false), "style=\"font-weight: bold; color: blue;\"") %>
								<%=Bean.getSelectOnChangeEndHTML() %></td>
							</tr>
							<tr>
								<td><%= Bean.userXML.getfieldTransl("is_test_mode", false) %></td> 
								<td colspan="3">
									<%=Bean.getSelectBeginHTML("is_test_mode", Bean.userXML.getfieldTransl("is_test_mode", false)) %>
									<%=Bean.getMeaningFromLookupNameOptions("YES_NO","N",true) %> 
									<%=Bean.getSelectEndHTML() %>
								</td>
							</tr>
							<tr>
								<td colspan="4"  align="center">
									<%=Bean.getSubmitButtonAjax("admin/setting.jsp", "button_save", "updateForm", "div_main", "validateParam") %>
							        <%=Bean.getSubmitButtonAjax("admin/setting.jsp", "button_back", "updateForm2", "div_main") %>
								</td>
							</tr>
							<tr><td colspan="4"><span id="error_title"><br><%=Bean.webposXML.getfieldTransl("operation_error", false) %></span></td></tr>
							<tr><td colspan="4"><span id="error_description"><%=resultMessage %></span></td></tr>
						
						</table>
					</form>	
					<form name="updateForm2" id="updateForm2" accept-charset="UTF-8" method="POST">
				        <input type="hidden" name="type" value="show">
					</form>
				<% } %>
			<% } %>


			<% /*} else if ("card".equalsIgnoreCase(access_type)){*/ } %> 
		<% /*} else if ("3".equalsIgnoreCase(tab)*/ } %>
		</div>
					</div>
				</td>
			</tr>
		</table>
	<!-- </div>  -->
	
		<% /*if (tabCount > 0) {*/ } else { %>
		<table <%=Bean.getTableDetail2Param() %>>
			<tr>
				<td>
					<div id="div_action_big">
					<h1><%=Bean.webposXML.getfieldTransl("title_settings", false) %><%=Bean.getHelpButton("setting", "div_action_big") %></h1>
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