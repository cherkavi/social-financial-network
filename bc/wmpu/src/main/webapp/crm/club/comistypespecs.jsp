<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%@page import="bc.objects.bcReglamentObject"%>
<%@page import="java.net.URLEncoder"%>
<%@page import="bc.objects.bcClubComissionTypeObject"%>
<%@page import="java.util.HashMap"%><html>

<%= Bean.getLogOutScript(request) %>

<head>
	<%= Bean.getMetaContent() %>
	<%= Bean.getBottomFrameCSS() %>
</head>
<body topmargin="0">
<div id="div_tabsheet">
<%

HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String pageFormName = "CLUB_COMISTYPE";

String tagComission = "_COMISSION";
String tagComissionFind = "_COMISSION_FIND";
String tagPostingScheme = "_POSTING_SCHEME";
String tagPostingSchemeFind = "_POSTING_SCHEME_FIND";
String tagPostingSchemeFiltr = "_POSTING_SCHEME_FILTR";
String tagClubRel = "_CLUB_REL";
String tagClubRelFind = "_CLUB_REL_FIND";


Bean.setJspPageForTabName(pageFormName);

String id = Bean.getDecodeParam(parameters.get("id"));
String	tab = Bean.getDecodeParam(parameters.get("tab"));
if (tab==null || "".equalsIgnoreCase(tab)) { tab = Bean.tabsHmGetValue(pageFormName); }
if (id==null || "".equalsIgnoreCase(id)) {
%>
	<%=Bean.getIDNotFoundMessage() %>
<% 
} else {
	Bean.tabsHmSetValue(pageFormName, tab);
	bcClubComissionTypeObject comis = new bcClubComissionTypeObject(id);
	
	String l_comis_page = Bean.getDecodeParam(parameters.get("comis_page"));
	Bean.pageCheck(pageFormName + tagComission, l_comis_page);
	String l_comis_page_beg = Bean.getFirstRowNumber(pageFormName + tagComission);
	String l_comis_page_end = Bean.getLastRowNumber(pageFormName + tagComission);
	
	String comis_find 	= Bean.getDecodeParam(parameters.get("comis_find"));
	comis_find 	= Bean.checkFindString(pageFormName + tagComissionFind, comis_find, l_comis_page);
	
	String l_posting_scheme_page = Bean.getDecodeParam(parameters.get("posting_scheme_page"));
	Bean.pageCheck(pageFormName + tagPostingScheme, l_posting_scheme_page);
	String l_posting_scheme_page_beg = Bean.getFirstRowNumber(pageFormName + tagPostingScheme);
	String l_posting_scheme_page_end = Bean.getLastRowNumber(pageFormName + tagPostingScheme);
	
	String posting_scheme_find 	= Bean.getDecodeParam(parameters.get("posting_scheme_find"));
	posting_scheme_find 	= Bean.checkFindString(pageFormName + tagPostingSchemeFind, posting_scheme_find, l_posting_scheme_page);

	String posting_scheme_filtr	= Bean.getDecodeParam(parameters.get("posting_scheme_filtr"));
	posting_scheme_filtr 			= Bean.checkFindString(pageFormName + tagPostingSchemeFiltr, posting_scheme_filtr, l_posting_scheme_page);
	
	String l_club_rel_page = Bean.getDecodeParam(parameters.get("club_rel_page"));
	Bean.pageCheck(pageFormName + tagClubRel, l_club_rel_page);
	String l_club_rel_page_beg = Bean.getFirstRowNumber(pageFormName + tagClubRel);
	String l_club_rel_page_end = Bean.getLastRowNumber(pageFormName + tagClubRel);
	
	String club_rel_find 	= Bean.getDecodeParam(parameters.get("club_rel_find"));
	club_rel_find 	= Bean.checkFindString(pageFormName + tagClubRelFind, club_rel_find, l_club_rel_page);

%>

	<table <%=Bean.getTableMenuParam() %>>
		<tr>
			<%= Bean.getPageHeader() %>

			<% if (Bean.currentMenu.isCurrentTabAndEditPermitted("CLUB_COMISTYPE_INFO")) { %>
			    <%= Bean.getMenuButton("ADD", "../crm/club/comistypeupdate.jsp?id=" + id + "&type=general&action=add2&process=no", "", "") %>
			    <%= Bean.getMenuButton("DELETE", "../crm/club/comistypeupdate.jsp?id=" + id + "&type=general&action=remove&process=yes", Bean.buttonXML.getfieldTransl("delete", false), comis.getValue("NAME_COMISSION_TYPE")) %>
			<%  } %>
			
			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLUB_COMISTYPE_COMISSIONS")) { %>
				<% if (Bean.currentMenu.isCurrentTabAndEditPermitted("CLUB_COMISTYPE_COMISSIONS")) {	%>
					<%= Bean.getMenuButton("ADD_ALL", "../crm/club/comistypeupdate.jsp?id=" + id + "&type=comission&action=addall&process=yes", Bean.jurpersonXML.getfieldTransl("h_add_all_comissions", false), "") %>
				<% } %>
			    <!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagComission, "../crm/club/comistypespecs.jsp?id=" + id + "&tab="+Bean.currentMenu.getTabID("CLUB_COMISTYPE_COMISSIONS")+"&", "comis_page") %>
			<% } %>
			
			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLUB_COMISTYPE_POSTING_SCHEME")) { %>

				 <!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagPostingScheme, "../crm/club/comistypespecs.jsp?id=" + id + "&tab="+Bean.currentMenu.getTabID("CLUB_COMISTYPE_POSTING_SCHEME")+"&", "posting_scheme_page") %>
			<% } %>
			
			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLUB_COMISTYPE_JUR_PRS_CLUB_REL")) { %>
				 <!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagClubRel, "../crm/club/comistypespecs.jsp?id=" + id + "&tab="+Bean.currentMenu.getTabID("CLUB_COMISTYPE_JUR_PRS_CLUB_REL")+"&", "club_rel_page") %>
			<% } %>
		</tr>
	</table>
	<table <%=Bean.getTableMenuParam() %>>
		<%= Bean.getDetailCaption(comis.getValue("NAME_COMISSION_TYPE")) %>
		<tr>
			<td>
				<!-- Выводим перечень закладок -->
				<%= Bean.currentMenu.getFormTabScheeds(tab, "../crm/club/comistypespecs.jsp?id=" + id) %>
			</td>
		</tr>
	</table>
</div>
<div id="div_data">
<div id="div_data_detail">
<%
if (Bean.currentMenu.isCurrentTabAndPreviewPermitted("CLUB_COMISTYPE_INFO")) {
 %>	 
	<form>
	<table <%=Bean.getTableDetailParam() %>>
	    <tr>
			<td><%= Bean.comissionXML.getfieldTransl("cd_comission_type", false) %></td> <td><input type="text" name="cd_comission_type" size="60" value="<%=comis.getValue("CD_COMISSION_TYPE") %>" readonly="readonly" class="inputfield-ro" title="<%=comis.getValue("CD_COMISSION_TYPE") %>"></td>
			<td><%= Bean.clubXML.getfieldTransl("club", false) %>
					<%= Bean.getGoToClubLink(comis.getValue("ID_CLUB")) %>
			</td><td><input type="text" name="name_club" size="30" value="<%= Bean.getClubShortName(comis.getValue("ID_CLUB")) %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
	    <tr>
			<td rowspan="3"><%= Bean.comissionXML.getfieldTransl("name_comission_type_full", false) %></td> <td rowspan="3"><%=Bean.getTextareaElement("name_comission_type", "", comis.getValue("NAME_COMISSION_TYPE"), true, "310", "60") %></td>
			<td><%= Bean.comissionXML.getfieldTransl("fixed_value_def_frmt", false) %></td> <td><input type="text" name="fixed_value" size="15" value="<%=comis.getValue("FIXED_VALUE_FRMT") %>" readonly="readonly" class="inputfield-ro""></td>
		</tr>
	    <tr>
			<td><%= Bean.comissionXML.getfieldTransl("percent_value_def_frmt", false) %></td> <td><input type="text" name="percent_value" size="15" value="<%=comis.getValue("PERCENT_VALUE_FRMT") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
	    <tr>
			<td><%= Bean.comissionXML.getfieldTransl("exist_flag", false) %></td> <td><input type="text" name="exist_flag" size="15" value="<%=comis.getValue("EXIST_FLAG_TSL") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td class="top_line" colspan="4">
				<b><%=Bean.comissionXML.getfieldTransl("h_payer_and_receiver", false) %></b>
			</td>
		</tr>
	    <tr>
			<td><%= Bean.comissionXML.getfieldTransl("name_club_rel_type", false) %></td> 
			<td>
				<input type="text" name="name_club_rel_type" size="60" value="<%=comis.getValue("NAME_CLUB_REL_TYPE") %>" readonly="readonly" class="inputfield-ro">
			</td>
			<td colspan="2">&nbsp;</td>
		</tr>
	    <tr>
			<td><%= Bean.comissionXML.getfieldTransl("jur_prs_payer", false) %></td> 
			<td>
				<input type="text" name="name_participant_payer" size="60" value="<%=comis.getValue("NAME_PARTICIPANT_PAYER") %>" readonly="readonly" class="inputfield-ro">
			</td>
			<td colspan="2">&nbsp;</td>
		</tr>
	    <tr>
			<td><%= Bean.comissionXML.getfieldTransl("jur_prs_receiver", false) %></td> 
			<td>
				<input type="text" name="name_participant_receiver" size="60" value="<%=comis.getValue("NAME_PARTICIPANT_RECEIVER") %>" readonly="readonly" class="inputfield-ro">
			</td>
			<td colspan="2">&nbsp;</td>
		</tr>
	    <tr>
			<td><%= Bean.comissionXML.getfieldTransl("name_payment_system", false) %></td> <td><input type="text" name="name_payment_system" size="60" value="<%=comis.getValue("NAME_PAYMENT_SYSTEM") %>" readonly="readonly" class="inputfield-ro"></td>
			<td colspan="2">&nbsp;</td>
		</tr>
		<%=	Bean.getIdCreationAndMoficationRecordFields(
				comis.getValue("ID_COMISSION_TYPE"),
				comis.getValue(Bean.getCreationDateFieldName()),
				comis.getValue("CREATED_BY"),
				comis.getValue(Bean.getLastUpdateDateFieldName()),
				comis.getValue("LAST_UPDATE_BY")
			) %>
		<tr>
			<td colspan="6" align="center">
				<%=Bean.getGoBackButton("../crm/club/comistype.jsp") %>
			</td>
		</tr>
	</table>
	</form>

<% } else if (Bean.currentMenu.isCurrentTabAndEditPermitted("CLUB_COMISTYPE_INFO")) { %>
	
		<script>
			var formData = new Array (
				new Array ('cd_comission_type', 'varchar2', 1),
				new Array ('name_comission_type', 'varchar2', 1),
				new Array ('cd_participant_payer', 'varchar2', 1),
				new Array ('cd_participant_receiver', 'varchar2', 1)
			);
			function myValidateForm() {
				return validateForm(formData);
			}
		</script>
    <form action="../crm/club/comistypeupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST">
    	<input type="hidden" name="id" value="<%=comis.getValue("ID_COMISSION_TYPE") %>">
	    <input type="hidden" name="action" value="edit">
    	<input type="hidden" name="process" value="yes">
    	<input type="hidden" name="type" value="general">
	<table <%=Bean.getTableDetailParam() %>>
	    <tr>
			<td><%= Bean.comissionXML.getfieldTransl("cd_comission_type", true) %></td> <td><input type="text" name="cd_comission_type" size="60" value="<%=comis.getValue("CD_COMISSION_TYPE") %>" class="inputfield" title="<%=comis.getValue("CD_COMISSION_TYPE") %>"></td>
			<td><%= Bean.clubXML.getfieldTransl("club", false) %>
					<%= Bean.getGoToClubLink(comis.getValue("ID_CLUB")) %>
			</td><td><input type="text" name="name_club" size="30" value="<%= Bean.getClubShortName(comis.getValue("ID_CLUB")) %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
	    <tr>
			<td rowspan="3"><%= Bean.comissionXML.getfieldTransl("name_comission_type_full", true) %></td> <td rowspan="3"><textarea name="name_comission_type" cols="57" rows="3" class="inputfield"><%= comis.getValue("NAME_COMISSION_TYPE") %></textarea></td>
			<td><%= Bean.comissionXML.getfieldTransl("fixed_value_def_frmt", false) %></td> <td><input type="text" name="fixed_value" size="15" value="<%=comis.getValue("FIXED_VALUE_FRMT") %>" class="inputfield"></td>
		</tr>
	    <tr>
			<td><%= Bean.comissionXML.getfieldTransl("percent_value_def_frmt", false) %></td> <td><input type="text" name="percent_value" size="15" value="<%=comis.getValue("PERCENT_VALUE_FRMT") %>" class="inputfield"></td>
		</tr>
	    <tr>
			<td><%= Bean.comissionXML.getfieldTransl("exist_flag", false) %></td><td><select name="exist_flag" class="inputfield"><%= Bean.getMeaningFromLookupNameOptions("YES_NO", comis.getValue("EXIST_FLAG"), false) %> </select></td>
		</tr>
		<tr>
			<td class="top_line" colspan="4">
				<b><%=Bean.comissionXML.getfieldTransl("h_payer_and_receiver", false) %></b>
			</td>
		</tr>
		<%
		String needComission = Bean.getComisTypeComissionCreatedCount(id);
		  
		if (!(needComission.equalsIgnoreCase("0"))) { %>
	    <tr>
			<td colspan="4">
			<b><font color=red><%= Bean.comissionXML.getfieldTransl("h_cannon_update_comission_type", false) %> -  <%=needComission %></font></b><br>
			</td>
		</tr>
	    <tr>
			<td><%= Bean.comissionXML.getfieldTransl("name_club_rel_type", false) %></td> 
			<td>
				<input type="hidden" name="cd_club_rel_type" value="<%=comis.getValue("CD_CLUB_REL_TYPE") %>">
				<input type="text" name="name_club_rel_type" size="60" value="<%=comis.getValue("NAME_CLUB_REL_TYPE") %>" readonly="readonly" class="inputfield-ro">
			</td>
			<td colspan="2">&nbsp;</td>
		</tr>
	    <tr>
			<td><%= Bean.comissionXML.getfieldTransl("jur_prs_payer", false) %></td> 
			<td>
				<input type="hidden" name="cd_participant_payer" value="<%=comis.getValue("CD_PARTICIPANT_PAYER") %>">
				<input type="text" name="name_participant_payer" size="60" value="<%=comis.getValue("NAME_PARTICIPANT_PAYER") %>" readonly="readonly" class="inputfield-ro">
			</td>
			<td colspan="2">&nbsp;</td>
		</tr>
	    <tr>
			<td><%= Bean.comissionXML.getfieldTransl("jur_prs_receiver", false) %></td> 
			<td>
				<input type="hidden" name="cd_participant_receiver" value="<%=comis.getValue("CD_PARTICIPANT_RECEIVER") %>">
				<input type="text" name="name_participant_receiver" size="60" value="<%=comis.getValue("NAME_PARTICIPANT_RECEIVER") %>" readonly="readonly" class="inputfield-ro">
			</td>
			<td colspan="2">&nbsp;</td>
		</tr>
	    <tr>
			<td><%= Bean.comissionXML.getfieldTransl("name_payment_system", false) %></td> <td><input type="text" name="name_payment_system" size="60" value="<%=comis.getValue("NAME_PAYMENT_SYSTEM") %>" readonly="readonly" class="inputfield-ro"></td>
			<td colspan="2">&nbsp;</td>
		</tr>
		<%} else {%>
	    <tr>
			<td><%= Bean.comissionXML.getfieldTransl("name_club_rel_type", false) %></td> 
			<td>
				<input type="hidden" name="cd_club_rel_type" value="<%=comis.getValue("CD_CLUB_REL_TYPE") %>">
				<input type="text" name="name_club_rel_type" size="60" value="<%=comis.getValue("NAME_CLUB_REL_TYPE") %>" readonly="readonly" class="inputfield-ro">
			</td>
			<td colspan="2">&nbsp;</td>
		</tr>
	    <tr>
			<td><%= Bean.comissionXML.getfieldTransl("jur_prs_payer", true) %></td> <td><select name="cd_participant_payer" class="inputfield"><%= Bean.getClubComissionParticipantOptions(comis.getValue("CD_PARTICIPANT_PAYER"), true) %> </select></td>
			<td colspan="2">&nbsp;</td>
		</tr>
	    <tr>
			<td><%= Bean.comissionXML.getfieldTransl("jur_prs_receiver", true) %></td> <td><select name="cd_participant_receiver" class="inputfield"><%= Bean.getClubComissionParticipantOptions(comis.getValue("CD_PARTICIPANT_RECEIVER"), true) %> </select></td>
			<td colspan="2">&nbsp;</td>
		</tr>
	    <tr>
			<td><%= Bean.comissionXML.getfieldTransl("name_payment_system", false) %></td> <td><select name="id_payment_system" class="inputfield"><%= Bean.getPaymentSystemOptions(comis.getValue("ID_PAYMENT_SYSTEM"), true) %> </select></td>
			<td colspan="2">&nbsp;</td>
		</tr>
		<% } %>
		<%=	Bean.getIdCreationAndMoficationRecordFields(
				comis.getValue("ID_COMISSION_TYPE"),
				comis.getValue(Bean.getCreationDateFieldName()),
				comis.getValue("CREATED_BY"),
				comis.getValue(Bean.getLastUpdateDateFieldName()),
				comis.getValue("LAST_UPDATE_BY")
			) %>
		<tr>
			<td colspan="6" align="center">
				<%=Bean.getSubmitButtonAjax("../crm/club/comistypeupdate.jsp") %>
				<%=Bean.getResetButton() %>
				<%=Bean.getGoBackButton("../crm/club/comistype.jsp") %>
			</td>
		</tr>
	</table>

	</form>
<% } %>
<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLUB_COMISTYPE_COMISSIONS")) {%>

		<table <%=Bean.getTableBottomFilter() %>><tbody>
		<tr>
			<%=Bean.getFindHTML("comis_find", comis_find, "../crm/club/comistypespecs.jsp?id=" + id + "&comis_page=1") %>

			<td align="right">
		  <%String needComission = Bean.getComisTypeComissionNeedCount(id);
		  
		  if (!(needComission.equalsIgnoreCase("0"))) {
			  %>
				<b><font color=red><%= Bean.comissionXML.getfieldTransl("need_comission_count", false) %> -  <%=needComission %></font></b><br>
			  <%
		  } else {
			  %>
				<b><font color=green><%= Bean.comissionXML.getfieldTransl("all_comission_was_created", false) %></font></b><br>
			  <%
		  }%>
			</td>
		</tr>
		</tbody>
		</table> 
	<%= comis.getJurPersonComissionHTML(comis_find, l_comis_page_beg, l_comis_page_end) %>
<%} %>

<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLUB_COMISTYPE_POSTING_SCHEME")) {%>
		<table <%=Bean.getTableBottomFilter() %>><tbody>
		<tr>
			<%=Bean.getFindHTML("posting_scheme_find", posting_scheme_find, "../crm/club/comistypespecs.jsp?id=" + id + "&posting_scheme_page=1") %>

			<%=Bean.getSelectOnChangeBeginHTML("posting_scheme_filtr", "../crm/club/comistypespecs.jsp?id=" + id + "&posting_scheme_page=1", Bean.posting_schemeXML.getfieldTransl("general", false)) %>
				<%= Bean.getBKOperationSchemeOptions(posting_scheme_filtr, true) %>
			<%=Bean.getSelectOnChangeEndHTML() %>
		</tr>
		</tbody>
		</table>

	<%= comis.getOperSchemesHTML(posting_scheme_find, posting_scheme_filtr, l_posting_scheme_page_beg, l_posting_scheme_page_end) %>
<%} %>

<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLUB_COMISTYPE_JUR_PRS_CLUB_REL")) {%>
		<table <%=Bean.getTableBottomFilter() %>><tbody>
		<tr>
			<%=Bean.getFindHTML("club_rel_find", club_rel_find, "../crm/club/comistypespecs.jsp?id=" + id + "&club_rel_page=1") %>

			<td>&nbsp;</td>
		</tr>
		</tbody>
		</table>
	<%= comis.getClubRelationshipsHTML(club_rel_find, l_club_rel_page_beg, l_club_rel_page_end) %>
<%} %>

<% } %>
</div></div>
</body>
</html>
