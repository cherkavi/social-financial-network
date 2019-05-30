<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%= Bean.getLogOutScript(request) %>

<%@page import="bc.objects.bcTerminalCertificateObject"%>
<%@page import="java.io.File"%>
<%@page import="bc.io.files"%>
<%@page import="bc.AppConst"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.net.URLEncoder"%><html>

<head>
	<%= Bean.getMetaContent() %>
	<%= Bean.getBottomFrameCSS() %>
<title></title>
</head>
<body>

<% 

HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String pageFormName = "CLIENTS_CERTIFICATE";
String tagLoaded = "_WAS_LOADED";
String tagLoadedFind = "_WAS_LOADED_FIND";
String tagInterface = "_IN_INTERFACE";
String tagInterfaceFind = "_IN_INTERFACE_FIND";

Bean.setJspPageForTabName(pageFormName);

String id_term = Bean.getDecodeParam(parameters.get("id_term"));
String id_cert = Bean.getDecodeParam(parameters.get("id_cert"));
String id_profile = Bean.getDecodeParam(parameters.get("id_profile"));
String	tab = Bean.getDecodeParam(parameters.get("tab"));
if (tab==null || "".equalsIgnoreCase(tab)) { tab = Bean.tabsHmGetValue(pageFormName); }
if (id_term==null || "".equalsIgnoreCase(id_term)) { id_term=""; }
if (id_cert==null || "".equalsIgnoreCase(id_cert)) { id_cert=""; }
if (id_profile==null || "".equalsIgnoreCase(id_profile)) { id_profile=Bean.filtersHmGetValue(pageFormName); }

if ("".equalsIgnoreCase(id_cert) && "".equalsIgnoreCase(id_term)) {
%>
	<%=Bean.getIDNotFoundMessage() %>
<% 
} else {
	Bean.tabsHmSetValue(pageFormName, tab);
	/*
	if ("I".equalsIgnoreCase(id_profile)) {
		if (tab.equals("2") || tab.equals("3")) {
			tab = "1";
		}
	}
	*/
	bcTerminalCertificateObject cert = new bcTerminalCertificateObject(id_cert, id_term);

	//Обрабатываем номера страниц
	String l_loaded_page = Bean.getDecodeParam(parameters.get("loaded_page"));
	Bean.pageCheck(pageFormName + tagLoaded, l_loaded_page);
	String l_loaded_page_beg = Bean.getFirstRowNumber(pageFormName + tagLoaded);
	String l_loaded_page_end = Bean.getLastRowNumber(pageFormName + tagLoaded);
	
	String loaded_find 	= Bean.getDecodeParam(parameters.get("loaded_find"));
	loaded_find 	= Bean.checkFindString(pageFormName + tagLoadedFind, loaded_find, l_loaded_page);

	String l_interface_page = Bean.getDecodeParam(parameters.get("interface_page"));
	Bean.pageCheck(pageFormName + tagInterface, l_interface_page);
	String l_interface_page_beg = Bean.getFirstRowNumber(pageFormName + tagInterface);
	String l_interface_page_end = Bean.getLastRowNumber(pageFormName + tagInterface);
	
	String interface_find 	= Bean.getDecodeParam(parameters.get("interface_find"));
	interface_find 	= Bean.checkFindString(pageFormName + tagInterfaceFind, interface_find, l_interface_page);
	
	
	Bean.currentMenu.setExistFlag("CLIENTS_CERTIFICATE_INFO", true);
	if ("T".equalsIgnoreCase(id_profile)) {
		Bean.currentMenu.setExistFlag("CLIENTS_CERTIFICATE_LOADED", true);
		Bean.currentMenu.setExistFlag("CLIENTS_CERTIFICATE_INTERFACE", true);
	} else {
		Bean.currentMenu.setExistFlag("CLIENTS_CERTIFICATE_LOADED", false);
		Bean.currentMenu.setExistFlag("CLIENTS_CERTIFICATE_INTERFACE", false);
		if (Bean.currentMenu.isCurrentTab("CLIENTS_CERTIFICATE_LOADED") ||
				Bean.currentMenu.isCurrentTab("CLIENTS_CERTIFICATE_INTERFACE")) {
    		Bean.currentMenu.setFirstCurrentTab();
    		tab = Bean.currentMenu.getCurrentTab();
    		Bean.tabsHmSetValue(pageFormName, tab);
		}	
	}		

	if ("I".equalsIgnoreCase(id_profile)) {
		cert.getInterfaceFeature();
	} else {
		cert.getFeature();
	}
	String lTitle = "";
	if ("T".equalsIgnoreCase(id_profile)) {
		lTitle = Bean.terminalXML.getfieldTransl("id_term", false) + " - " + id_term;
	} else {
		lTitle = cert.getValue("ID_TERM_CERTIFICATE") + " - " + cert.getValue("ID_TERM");
	}
	
	
	%>
<body>	
<div id="div_tabsheet">
	<table <%=Bean.getTableMenuParam() %>>
		<tr>
			<%= Bean.getPageHeader() %>
	
			<% if (Bean.currentMenu.isCurrentTabAndEditPermitted("CLIENTS_CERTIFICATE_INFO")) { %> 
				<% if ("I".equalsIgnoreCase(id_profile)) {%>
				    <%= Bean.getMenuButton("LOAD_FROM_FILE", "../crm/clients/certificateimport.jsp?type=import&process=no&action=load_from_file", "", "", Bean.terminalXML.getfieldTransl("h_load_certificates", false)) %>
				    <%= Bean.getMenuButton("IMPORT", "../crm/clients/certificateupdate.jsp?id_term=" + cert.getValue("ID_TERM") + "&id_cert=" + cert.getValue("ID_TERM_CERTIFICATE") + "&filtr_type="+id_profile + "&type=apply&process=yes", "", "") %>
				    <%= Bean.getMenuButton("DELETE", "../crm/clients/certificateupdate.jsp?id_cert=" + cert.getValue("ID_TERM_CERTIFICATE") + "&type=general&action=remove&process=yes&filtr_type="+id_profile, Bean.terminalXML.getfieldTransl("h_delete_certificate", false), lTitle) %>
				<% } %>
				<% if ("C".equalsIgnoreCase(id_profile)) {%>
				    <%= Bean.getMenuButton("DELETE", "../crm/clients/certificateupdate.jsp?id_cert=" + cert.getValue("ID_TERM_CERTIFICATE") + "&type=general&action=remove&process=yes&filtr_type="+id_profile, Bean.terminalXML.getfieldTransl("h_delete_certificate", false), lTitle) %>
				<% } %>
			<% } %>
			<% if ("T".equalsIgnoreCase(id_profile)) {%>
			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLIENTS_CERTIFICATE_LOADED")) { %>
			    <!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagLoaded, "../crm/clients/certificatespecs.jsp?id_term=" + id_term + "&id_cert=" + cert.getValue("ID_TERM_CERTIFICATE") + "&id_profile=" + id_profile + "&tab="+Bean.currentMenu.getTabID("CLIENTS_CERTIFICATE_LOADED")+"&", "loaded_page") %>
			<% } %>
	
			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLIENTS_CERTIFICATE_INTERFACE")) { %>
			    <!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagInterface, "../crm/clients/certificatespecs.jsp?id_term=" + id_term + "&id_cert=" + cert.getValue("ID_TERM_CERTIFICATE") + "&id_profile=" + id_profile + "&tab="+Bean.currentMenu.getTabID("CLIENTS_CERTIFICATE_INTERFACE")+"&", "interface_page") %>
			<% } %>
			<% } %>
		</tr>
	</table>
	<table <%=Bean.getTableMenuParam() %>>
		<%= Bean.getDetailCaption(lTitle) %>
		<tr>
			<td>
				<!-- Выводим перечень закладок -->
				<%= Bean.currentMenu.getFormTabScheeds(tab, "../crm/clients/certificatespecs.jsp?id_term=" + id_term + "&id_cert=" + cert.getValue("ID_TERM_CERTIFICATE") + "&id_profile=" + id_profile) %>
			</td>
		</tr>
	</table>
</div>
<div id="div_data">
<div id="div_data_detail">	
	<%
	//------------------------------
	// В разрезе терминалов
	//------------------------------
	if ("T".equalsIgnoreCase(id_profile)) {

		if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLIENTS_CERTIFICATE_INFO")) { %>
			<%= cert.getCurrentCertificatesHTML() %>
			<br>
			<%= cert.getNextCertificatesHTML() %>
		
		<% }
		if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLIENTS_CERTIFICATE_LOADED")) { %>
	<table <%=Bean.getTableBottomFilter() %>>
	  	<tr>
		<%= Bean.getFindHTML("loaded_find", loaded_find, "../crm/clients/certificatespecs.jsp?id_term=" + id_term + "&id_cert=" + cert.getValue("ID_TERM_CERTIFICATE") + "&id_profile=" + id_profile + "&loaded_page=1&") %>

		<td>&nbsp;</td>
	  	</tr>
	</table>
		
		<%= cert.getLoadedCertificatesHTML(loaded_find, l_loaded_page_beg, l_loaded_page_end) %>
			
		<%}
		
		if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLIENTS_CERTIFICATE_INTERFACE")) { %>
	<table <%=Bean.getTableBottomFilter() %>>
	  	<tr>
		<%= Bean.getFindHTML("interface_find", interface_find, "../crm/clients/certificatespecs.jsp?id_term=" + id_term + "&id_cert=" + cert.getValue("ID_TERM_CERTIFICATE") + "&id_profile=" + id_profile + "&interface_page=1&") %>

		<td>&nbsp;</td>
	  	</tr>
	</table>
	
		<%= cert.getInterfaceCertificatesHTML(interface_find, l_interface_page_beg, l_interface_page_end) %>
			
		<%}
	
	
	//------------------------------
	// Действующий сертификат
	//------------------------------
	} else if ("C".equalsIgnoreCase(id_profile) ||
			//------------------------------
			// Сертификат в интерфейсной таблице
			//------------------------------
			("I".equalsIgnoreCase(id_profile))) { 
		
		if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLIENTS_CERTIFICATE_INFO")) {
			
			boolean hasEditPermission = false;
			if (Bean.currentMenu.isCurrentTabAndEditPermitted("CLIENTS_CERTIFICATE_INFO")) {
				hasEditPermission = true;
			}
			// Действующие сертификаты не редактируем
			if ("C".equalsIgnoreCase(id_profile)) {
				hasEditPermission = false;
			}
			//hasEditPermission = true;
			
			if (hasEditPermission) {
		%>
		    <form action="../crm/clients/certificateupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST">
		        <input type="hidden" name="type" value="general">
				<input type="hidden" name="action" value="edit">
		        <input type="hidden" name="process" value="yes">
		        <input type="hidden" name="id_cert" value="<%=cert.getValue("ID_TERM_CERTIFICATE") %>">
		        <input type="hidden" name="filtr_type" value="<%=id_profile %>">
			<table <%=Bean.getTableDetailParam() %>>
				<tr>
					<td><%= Bean.terminalCertificateXML.getfieldTransl("ID_TERM_CERTIFICATE", false) %> </td><td><input type="text" name="id_term_certificate" size="20" value="<%= cert.getValue("ID_TERM_CERTIFICATE") %>" readonly="readonly" class="inputfield-ro"></td>
					<td><%= Bean.terminalCertificateXML.getfieldTransl("begin_action_date", false) %></td><td><%=Bean.getCalendarInputField("begin_action_date", cert.getValue("BEGIN_ACTION_DATE_FRMT"), "10") %></td>
				</tr>
				<tr>
					<td><%= Bean.terminalCertificateXML.getfieldTransl("ID_TERM", false) %>
						<%= Bean.getGoToTerminalLink(cert.getValue("ID_TERM")) %>
					</td> <td><input type="text" name="id_term" size="20" value="<%= cert.getValue("ID_TERM") %>" class="inputfield"> </td>
					<td><%= Bean.terminalCertificateXML.getfieldTransl("end_action_date", false) %></td><td><%=Bean.getCalendarInputField("end_action_date", cert.getValue("END_ACTION_DATE_FRMT"), "10") %></td>
				</tr>
				<tr>
					<%if ("I".equalsIgnoreCase(id_profile)) { %>
						<td><%= Bean.clubXML.getfieldTransl("club", true) %>
							<%= Bean.getGoToClubLink(cert.getValue("ID_CLUB")) %>
						</td>
					  	<td>
							<%=Bean.getWindowFindClub("club", cert.getValue("ID_CLUB"), cert.getValue("SNAME_CLUB"), "25") %>
				  		</td>
						<td><%= Bean.terminalCertificateXML.getfieldTransl("state_certificate", false) %></td><td><input type="text" name="state_certificate" size="20" value="<%= cert.getValue("STATE_CERTIFICATE_TSL") %>" readonly="readonly" class="inputfield-ro"></td>
					<%} else {%>
						<td><%= Bean.clubXML.getfieldTransl("club", false) %>
							<%= Bean.getGoToClubLink(cert.getValue("ID_CLUB")) %>
						</td>
					  	<td><input type="text" name="name_club" size="30" value="<%= Bean.getClubShortName(cert.getValue("ID_CLUB")) %>" readonly="readonly" class="inputfield-ro"></td>
					<% } %>
				</tr>
				<tr>
					<%if (!(cert.getValue("STORED_FULL_FILE_NAME")==null || "".equalsIgnoreCase(cert.getValue("STORED_FULL_FILE_NAME")))) { %>
						<td><%= Bean.terminalCertificateXML.getfieldTransl("FILE_NAME", false) %> </td>
						<td>
							<a href="../FileSender?FILENAME=<%=URLEncoder.encode(cert.getValue("STORED_FULL_FILE_NAME"),"UTF-8")%>" title="<%= Bean.buttonXML.getfieldTransl("open", false) %>" target="_blank">
								<%=cert.getValue("FILE_NAME") %>
							</a> 					
						</td>
					<%} %>
				</tr>
				<%if ("C".equalsIgnoreCase(id_profile)) { %>
				<tr>
					<td><%= Bean.terminalCertificateXML.getfieldTransl("is_certificate_current", false) %> </td><td><input type="text" name="is_certificate_current" size="12" value="<%= cert.getValue("IS_CERTIFICATE_CURRENT_TSL") %>" readonly="readonly" class="inputfield-ro"></td>
					<td><%= Bean.terminalCertificateXML.getfieldTransl("is_certificate_received", false) %> </td><td><input type="text" name="is_certificate_received" size="12" value="<%= cert.getValue("IS_CERTIFICATE_RECEIVED_TSL") %>" readonly="readonly" class="inputfield-ro"></td>
				</tr>
				<tr>
					<td colspan="2"></td>
					<td><%= Bean.terminalCertificateXML.getfieldTransl("date_certificate_received", false) %> </td><td><input type="text" name="date_certificate_received" size="12" value="<%= cert.getValue("DATE_CERTIFICATE_RECEIVED_FRMT") %>" readonly="readonly" class="inputfield-ro"></td>
				</tr>
				<% } %>
				<tr>
					<td><%= Bean.terminalCertificateXML.getfieldTransl("TEXT_CERTIFICATE", false) %></td> 
						<td  colspan="3"><textarea name="text_certificate" cols="160" rows="3" class="inputfield"><%= cert.getValue("TEXT_CERTIFICATE") %></textarea></td>
				</tr>
				<tr>
					<%if ("I".equalsIgnoreCase(id_profile)) { 
					 	if ("E".equalsIgnoreCase(cert.getValue("STATE_CERTIFICATE"))) { %>
						<td><%= Bean.terminalCertificateXML.getfieldTransl("ERROR_TEXT", false) %> </td>
						<td  colspan="3"><textarea name="error_text" cols="160" rows="3" readonly="readonly" class="inputfield-ro"><%= cert.getValue("ERROR_TEXT") %></textarea></td>
					<% } } %>
				</tr>
				<%=	Bean.getCreationAndMoficationRecordFields(
						cert.getValue(Bean.getCreationDateFieldName()),
						cert.getValue("CREATED_BY"),
						cert.getValue(Bean.getLastUpdateDateFieldName()),
						cert.getValue("LAST_UPDATE_BY")
				) %>
				<tr>
					<td colspan="4" align="center">
						<%=Bean.getSubmitButtonAjax("../crm/clients/certificateupdate.jsp") %>
						<%=Bean.getResetButton() %>
						<%=Bean.getGoBackButton("../crm/clients/certificate.jsp") %>
					</td>
				</tr>
		
			</table>
			</form> 
		<!-- Скрипт для втавки меню вибору дати -->
		<%= Bean.getCalendarScript("begin_action_date", false) %>
		<%= Bean.getCalendarScript("end_action_date", false) %>

		 <% } else if (!hasEditPermission) { %>
		 	<table <%=Bean.getTableDetailParam() %>>
				<tr>
					<td><%= Bean.terminalCertificateXML.getfieldTransl("ID_TERM_CERTIFICATE", false) %> </td><td><input type="text" name="id_term_certificate" size="20" value="<%= cert.getValue("ID_TERM_CERTIFICATE") %>" readonly="readonly" class="inputfield-ro"></td>
					<td><%= Bean.terminalCertificateXML.getfieldTransl("begin_action_date", false) %></td><td><input type="text" name="begin_action_date" size="20" value="<%= cert.getValue("BEGIN_ACTION_DATE_FRMT") %>" readonly="readonly" class="inputfield-ro"></td>
				</tr>
				<tr>
					<td><%= Bean.terminalCertificateXML.getfieldTransl("ID_TERM", false) %>
						<%= Bean.getGoToTerminalLink(cert.getValue("ID_TERM")) %>
					</td> <td><input type="text" name="id_term" size="20" value="<%= cert.getValue("ID_TERM") %>" readonly="readonly" class="inputfield-ro"> </td>
					<td><%= Bean.terminalCertificateXML.getfieldTransl("end_action_date", false) %></td><td><input type="text" name="end_action_date" size="20" value="<%= cert.getValue("END_ACTION_DATE_FRMT") %>" readonly="readonly" class="inputfield-ro"></td>
				</tr>
				<tr>
						<td><%= Bean.clubXML.getfieldTransl("club", false) %>
							<%= Bean.getGoToClubLink(cert.getValue("ID_CLUB")) %>
						</td>
					  	<td><input type="text" name="name_club" size="30" value="<%= Bean.getClubShortName(cert.getValue("ID_CLUB")) %>" readonly="readonly" class="inputfield-ro"></td>
					<%if ("I".equalsIgnoreCase(id_profile)) { %>
						<td><%= Bean.terminalCertificateXML.getfieldTransl("state_certificate", false) %></td><td><input type="text" name="state_certificate" size="20" value="<%= cert.getValue("STATE_CERTIFICATE_TSL") %>" readonly="readonly" class="inputfield-ro"></td>
					<% } %>
				</tr>
				<%if (!(cert.getValue("STORED_FULL_FILE_NAME")==null || "".equalsIgnoreCase(cert.getValue("STORED_FULL_FILE_NAME")))) { %>
					<tr>
						<td><%= Bean.terminalCertificateXML.getfieldTransl("FILE_NAME", false) %> </td>
						<td>
							<a href="../FileSender?FILENAME=<%=URLEncoder.encode(cert.getValue("STORED_FULL_FILE_NAME"),"UTF-8")%>" title="<%= Bean.buttonXML.getfieldTransl("open", false) %>" target="_blank">
								<%=cert.getValue("FILE_NAME") %>
							</a> 					
						</td>
				</tr>
				<%} %>
				<%if ("C".equalsIgnoreCase(id_profile)) { %>
				<tr>
					<td><%= Bean.terminalCertificateXML.getfieldTransl("is_certificate_current", false) %> </td><td><input type="text" name="is_certificate_current" size="20" value="<%= cert.getValue("IS_CERTIFICATE_CURRENT_TSL") %>" readonly="readonly" class="inputfield-ro"></td>
					<td><%= Bean.terminalCertificateXML.getfieldTransl("is_certificate_received", false) %> </td><td><input type="text" name="is_certificate_received" size="20" value="<%= cert.getValue("IS_CERTIFICATE_RECEIVED_TSL") %>" readonly="readonly" class="inputfield-ro"></td>
				</tr>
				<tr>
					<td colspan="2"></td>
					<td><%= Bean.terminalCertificateXML.getfieldTransl("date_certificate_received", false) %> </td><td><input type="text" name="date_certificate_received" size="20" value="<%= cert.getValue("DATE_CERTIFICATE_RECEIVED_FRMT") %>" readonly="readonly" class="inputfield-ro"></td>
				</tr>
				<% } %>
				<tr>
					<td><%= Bean.terminalCertificateXML.getfieldTransl("TEXT_CERTIFICATE", false) %></td> 
						<td colspan="5"><textarea name="text_certificate" cols="160" rows="3" readonly="readonly" class="inputfield-ro"><%= cert.getValue("TEXT_CERTIFICATE") %></textarea></td>
				</tr>
				<tr>
					<%if ("I".equalsIgnoreCase(id_profile)) { 
					 	if ("E".equalsIgnoreCase(cert.getValue("STATE_CERTIFICATE"))) { %>
						<td><%= Bean.terminalCertificateXML.getfieldTransl("ERROR_TEXT", false) %> </td>
						<td  colspan="3"><textarea name="error_text" cols="160" rows="3" readonly="readonly" class="inputfield-ro"><%= cert.getValue("ERROR_TEXT") %></textarea></td>
					<% } } %>
				</tr>
				<%=	Bean.getCreationAndMoficationRecordFields(
						cert.getValue(Bean.getCreationDateFieldName()),
						cert.getValue("CREATED_BY"),
						cert.getValue(Bean.getLastUpdateDateFieldName()),
						cert.getValue("LAST_UPDATE_BY")
				) %>
				<tr>
					<td colspan="8" align="center">
						<%=Bean.getGoBackButton("../crm/clients/certificate.jsp") %>
					</td>
				</tr>
			</table>
		
		 <% } } 
	
	}

} %>
</div></div>
</body>
</html>
