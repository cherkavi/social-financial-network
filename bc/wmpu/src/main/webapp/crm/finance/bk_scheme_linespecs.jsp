<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%@page import="bc.objects.bcClubShortObject"%>
<%@page import="bc.objects.bcFNBKSchemeLineObject"%>
<%@page import="java.util.HashMap"%>
<%@page import="bc.objects.bcFNBKSchemeObject"%>

<%= Bean.getLogOutScript(request) %>
<html>
<head>
	<link rel="stylesheet" type="text/css" href="../CSS/tablebottom.css">
	<%= Bean.getMetaContent() %>
	<%= Bean.getBottomFrameCSS() %>
</head>

<%

HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String pageFormName = "FINANCE_BK_SCHEME";

Bean.setJspPageForTabName(pageFormName);

String tagBKAccounts = "_BK_ACCOUNTS";
String tagBKAccountFind = "_BK_ACCOUNT_FIND";
String tagPostingScheme = "_POSTING_SCHEME";
String tagPostingSchemeFind = "_POSTING_SCHEME_FIND";
String tagPostingSchemeFiltr = "_POSTING_SCHEME_FILTR";

String id = Bean.getDecodeParam(parameters.get("id"));
String tab = Bean.getDecodeParam(parameters.get("tab"));

if (id==null || "".equalsIgnoreCase(id)) { id=""; }

if (tab==null || "".equalsIgnoreCase(tab)) { tab = Bean.tabsHmGetValue(pageFormName); }

if (id==null || "".equalsIgnoreCase(id)) {
%>
	<%=Bean.getIDNotFoundMessage() %>
<% 
}
else {
	Bean.tabsHmSetValue(pageFormName, tab);
	bcFNBKSchemeLineObject line = new bcFNBKSchemeLineObject(id);
	
	String id_scheme = line.getValue("ID_BK_ACCOUNT_SCHEME");
	
	bcFNBKSchemeObject setting = new bcFNBKSchemeObject(id_scheme);

	Bean.currentMenu.setExistFlag("FINANCE_BK_SCHEME_BK_ACCOUNTS",true);
	Bean.currentMenu.setExistFlag("FINANCE_BK_SCHEME_POSTING_SCHEME",true);
	Bean.currentMenu.setExistFlag("FINANCE_BK_SCHEME_LINES",false);
	if (Bean.currentMenu.isCurrentTab("FINANCE_BK_SCHEME_LINES")) {
		Bean.currentMenu.setFirstCurrentTab();
		tab = Bean.currentMenu.getCurrentTab();
		Bean.tabsHmSetValue(pageFormName, tab);
	}
	String segment = Bean.getDecodeParam(parameters.get("segment"));
	String sementFull = "";
	String participant = Bean.getDecodeParam(parameters.get("participant"));
	if (segment==null || "".equalsIgnoreCase(segment)) { 
		segment=""; 
	} else {
		sementFull = "segment" + segment;
	}

	//Обрабатываем номера страниц
	String l_bk_acc_page = Bean.getDecodeParam(parameters.get("bk_acc_page"));
	Bean.pageCheck(pageFormName + tagBKAccounts, l_bk_acc_page);
	String l_bk_acc_page_beg = Bean.getFirstRowNumber(pageFormName + tagBKAccounts);
	String l_bk_acc_page_end = Bean.getLastRowNumber(pageFormName + tagBKAccounts);

	String bk_acc_find = Bean.getDecodeParam(parameters.get("bk_acc_find"));
	bk_acc_find = Bean.checkFindString(pageFormName + tagBKAccountFind, bk_acc_find, l_bk_acc_page);

	String l_posting_scheme_page = Bean.getDecodeParam(parameters.get("posting_scheme_page"));
	Bean.pageCheck(pageFormName + tagPostingScheme, l_posting_scheme_page);
	String l_posting_scheme_page_beg = Bean.getFirstRowNumber(pageFormName + tagPostingScheme);
	String l_posting_scheme_page_end = Bean.getLastRowNumber(pageFormName + tagPostingScheme);

	String posting_scheme_find = Bean.getDecodeParam(parameters.get("posting_scheme_find"));
	posting_scheme_find = Bean.checkFindString(pageFormName + tagPostingSchemeFind, posting_scheme_find, l_posting_scheme_page);

	String posting_scheme_filtr	= Bean.getDecodeParam(parameters.get("posting_scheme_filtr"));
	posting_scheme_filtr 			= Bean.checkFindString(pageFormName + tagPostingSchemeFiltr, posting_scheme_filtr, l_posting_scheme_page);

	
if (participant==null || "".equalsIgnoreCase(participant)) { 
  	if (!"".equalsIgnoreCase(segment)) {
  		if ("1".equalsIgnoreCase(segment)) {
  			participant = line.getValue("SEGMENT1_PARTICIPANT");
  		} else if ("2".equalsIgnoreCase(segment)) { 
  			participant = line.getValue("SEGMENT2_PARTICIPANT");
  		} else if ("3".equalsIgnoreCase(segment)) {
  			participant = line.getValue("SEGMENT3_PARTICIPANT");
  		} else if ("4".equalsIgnoreCase(segment)) {
  			participant = line.getValue("SEGMENT4_PARTICIPANT");
  		} else if ("5".equalsIgnoreCase(segment)) {
  			participant = line.getValue("SEGMENT5_PARTICIPANT");
  		} else if ("6".equalsIgnoreCase(segment)) {
  			participant = line.getValue("SEGMENT6_PARTICIPANT");
  		} else if ("7".equalsIgnoreCase(segment)) {
  			participant = line.getValue("SEGMENT7_PARTICIPANT");
  		} else if ("8".equalsIgnoreCase(segment)) {
  			participant = line.getValue("SEGMENT8_PARTICIPANT");
  		} else if ("9".equalsIgnoreCase(segment)) {
  			participant = line.getValue("SEGMENT9_PARTICIPANT");
  		} else if ("10".equalsIgnoreCase(segment)) {
  			participant = line.getValue("SEGMENT10_PARTICIPANT");
  		} else if ("11".equalsIgnoreCase(segment)) {
  			participant = line.getValue("SEGMENT11_PARTICIPANT");
  		} else if ("12".equalsIgnoreCase(segment)) {
  			participant = line.getValue("SEGMENT12_PARTICIPANT");
  		} else if ("13".equalsIgnoreCase(segment)) {
  			participant = line.getValue("SEGMENT13_PARTICIPANT");
  		} else if ("14".equalsIgnoreCase(segment)) {
  			participant = line.getValue("SEGMENT14_PARTICIPANT");
  		} else if ("15".equalsIgnoreCase(segment)) {
  			participant = line.getValue("SEGMENT15_PARTICIPANT");
  		}
		//participant=Bean.bk_cd_participant; 
  	}	
}


%>
<body>
<div id="div_tabsheet">

<table <%=Bean.getTableMenuParam() %>>
		<tr>
			<%= Bean.getPageHeader() %>

			<% if (Bean.currentMenu.isCurrentTabAndEditPermitted("FINANCE_BK_SCHEME_INFO")) { %>
				<%= Bean.getMenuButton("ADD", "../crm/finance/bk_scheme_lineupdate.jsp?id_scheme=" + id_scheme + "&id=" + id + "&type=participant&action=add2&process=no", "", "") %>
				<%= Bean.getMenuButton("COPY", "../crm/finance/bk_scheme_lineupdate.jsp?id_scheme=" + id_scheme + "&id=" + id + "&type=participant&action=copy&process=no", "", "") %>
				<%= Bean.getMenuButton("DELETE", "../crm/finance/bk_scheme_lineupdate.jsp?id_scheme=" + id_scheme + "&id=" + id + "&type=participant&action=remove&process=yes", Bean.bk_schemeXML.getfieldTransl("h_delete_bk_scheme_line", false), line.getValue("CD_BK_ACCOUNT_SCHEME_LINE") + " - " + line.getValue("NAME_BK_ACCOUNT_SCHEME_LINE")) %>
			<% } %>
	
			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("FINANCE_BK_SCHEME_BK_ACCOUNTS")){ %>
	    		<!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagBKAccounts, "../crm/finance/bk_scheme_linespecs.jsp?id=" + id + "&tab="+Bean.currentMenu.getTabID("FINANCE_BK_SCHEME_BK_ACCOUNTS")+"&", "bk_acc_page") %>
			<% } %>

			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("FINANCE_BK_SCHEME_POSTING_SCHEME")){ %>
	    		<!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagPostingScheme, "../crm/finance/bk_scheme_linespecs.jsp?id=" + id + "&tab="+Bean.currentMenu.getTabID("FINANCE_BK_SCHEME_POSTING_SCHEME")+"&", "posting_scheme_page") %>
			<% } %>
	
		</tr>
	</table>
	<table <%=Bean.getTableMenuParam() %>>
		<tr>
			<td>
				<center>
					<div class="div_caption2" onclick="ajaxpage('../crm/finance/bk_schemespecs.jsp?id=<%=line.getValue("ID_BK_ACCOUNT_SCHEME") %>','div_main')"> 
					<%= setting.getValue("ID_BK_ACCOUNT_SCHEME") + ": " + setting.getValue("DESC_BK_ACCOUNT_SCHEME") %>
					</div>
				</center>
			</td>
		</tr>
		<%= Bean.getDetailCaption(line.getValue("CD_BK_ACCOUNT_SCHEME_LINE") + " - " + line.getValue("NAME_BK_ACCOUNT_SCHEME_LINE")) %>
		<tr>
			<td>
				<!-- Выводим перечень закладок -->
				<%= Bean.currentMenu.getFormTabScheeds(tab, "../crm/finance/bk_scheme_linespecs.jsp?id=" + id) %>
			</td>

		</tr>
	</table>
</div>
<div id="div_data">
<div id="div_data_detail">
<%
if (Bean.currentMenu.isCurrentTabAndAccessPermitted("FINANCE_BK_SCHEME_INFO")) { 

	bcClubShortObject club = new bcClubShortObject(setting.getValue("ID_CLUB"));

	if (Bean.currentMenu.isCurrentTabAndEditPermitted("FINANCE_BK_SCHEME_INFO")) {
	%>

		<script>
			var formData = new Array (
				new Array ('segment1', 'varchar2', 1),
				new Array ('name_bk_account_scheme_line', 'varchar2', 1),
				new Array ('int_nm_bk_account_scheme_line', 'varchar2', 1)
			);
		</script>


        <form action="../crm/finance/bk_scheme_lineupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">
			<% if ("".equalsIgnoreCase(segment)) { %>
				<input type="hidden" name="type" value="participant">
			<% } else { %>
				<input type="hidden" name="type" value="setting">
			<% } %>
	        <input type="hidden" name="action" value="edit">
	        <input type="hidden" name="process" value="yes">
	        <input type="hidden" name="id" value="<%= id %>">
	        <input type="hidden" name="id_scheme" value="<%= id_scheme %>">
	        <input type="hidden" name="segment" value="<%= segment %>">
		<table <%=Bean.getTableDetailParam() %>>
		<tr>
			<td><%= Bean.bk_schemeXML.getfieldTransl("cd_bk_account_scheme_line", true) %> </td>
			<td  colspan="3">
			<%  
			
			   int i = 0;
			  for (i=1; i<=Integer.parseInt(club.getValue("bk_account_segments_count")); i++) {
			   if (i==1) {%>
				<%=Bean.getImgPs(
						segment,
						"segment1", 
						line.getValue("SEGMENT1"), 
						line.getValue("SEGMENT1_PARTICIPANT"), 
						"../crm/finance/bk_scheme_linespecs.jsp?id=" + id + "&segment=1") %>
			<%} else if (i==2) { %>	
				<%=Bean.getImgPs(
						segment,
						"segment2", 
						line.getValue("SEGMENT2"), 
						line.getValue("SEGMENT2_PARTICIPANT"), 
						"../crm/finance/bk_scheme_linespecs.jsp?id=" + id + "&segment=2") %>
			<%} else if (i==3) { %>	
				<%=Bean.getImgPs(
						segment,
						"segment3", 
						line.getValue("SEGMENT3"), 
						line.getValue("SEGMENT3_PARTICIPANT"), 
						"../crm/finance/bk_scheme_linespecs.jsp?id=" + id + "&segment=3") %>
			<%} else if (i==4) { %>	
				<%=Bean.getImgPs(
						segment,
						"segment4", 
						line.getValue("SEGMENT4"), 
						line.getValue("SEGMENT4_PARTICIPANT"), 
						"../crm/finance/bk_scheme_linespecs.jsp?id=" + id + "&segment=4") %>
			<%} else if (i==5) { %>	
				<%=Bean.getImgPs(
						segment,
						"segment5", 
						line.getValue("SEGMENT5"), 
						line.getValue("SEGMENT5_PARTICIPANT"), 
						"../crm/finance/bk_scheme_linespecs.jsp?id=" + id + "&segment=5") %>
			<%} else if (i==6) { %>	
				<%=Bean.getImgPs(
						segment,
						"segment6", 
						line.getValue("SEGMENT6"), 
						line.getValue("SEGMENT6_PARTICIPANT"), 
						"../crm/finance/bk_scheme_linespecs.jsp?id=" + id + "&segment=6") %>
			<%} else if (i==7) { %>	
				<%=Bean.getImgPs(
						segment,
						"segment7", 
						line.getValue("SEGMENT7"), 
						line.getValue("SEGMENT7_PARTICIPANT"), 
						"../crm/finance/bk_scheme_linespecs.jsp?id=" + id + "&segment=7") %>
			<%} else if (i==8) { %>	
				<%=Bean.getImgPs(
						segment,
						"segment8", 
						line.getValue("SEGMENT8"), 
						line.getValue("SEGMENT8_PARTICIPANT"), 
						"../crm/finance/bk_scheme_linespecs.jsp?id=" + id + "&segment=8") %>
			<%} else if (i==9) { %>	
				<%=Bean.getImgPs(
						segment,
						"segment9", 
						line.getValue("SEGMENT9"), 
						line.getValue("SEGMENT9_PARTICIPANT"), 
						"../crm/finance/bk_scheme_linespecs.jsp?id=" + id + "&segment=9") %>
			<%} else if (i==10) { %>	
				<%=Bean.getImgPs(
						segment,
						"segment10", 
						line.getValue("SEGMENT10"), 
						line.getValue("SEGMENT10_PARTICIPANT"), 
						"../crm/finance/bk_scheme_linespecs.jsp?id=" + id + "&segment=10") %>
			<%} else if (i==11) { %>	
				<%=Bean.getImgPs(
						segment,
						"segment11", 
						line.getValue("SEGMENT11"), 
						line.getValue("SEGMENT11_PARTICIPANT"), 
						"../crm/finance/bk_scheme_linespecs.jsp?id=" + id + "&segment=11") %>
			<%} else if (i==12) { %>	
				<%=Bean.getImgPs(
						segment,
						"segment12", 
						line.getValue("SEGMENT12"), 
						line.getValue("SEGMENT12_PARTICIPANT"), 
						"../crm/finance/bk_scheme_linespecs.jsp?id=" + id + "&segment=12") %>
			<%} else if (i==13) { %>	
				<%=Bean.getImgPs(
						segment,
						"segment13", 
						line.getValue("SEGMENT13"), 
						line.getValue("SEGMENT13_PARTICIPANT"), 
						"../crm/finance/bk_scheme_linespecs.jsp?id=" + id + "&segment=13") %>
			<%} else if (i==14) { %>	
				<%=Bean.getImgPs(
						segment,
						"segment14", 
						line.getValue("SEGMENT14"), 
						line.getValue("SEGMENT14_PARTICIPANT"), 
						"../crm/finance/bk_scheme_linespecs.jsp?id=" + id + "&segment=14") %>
			<%} else if (i==15) { %>	
				<%=Bean.getImgPs(
						segment,
						"segment15", 
						line.getValue("SEGMENT15"), 
						line.getValue("SEGMENT15_PARTICIPANT"), 
						"../crm/finance/bk_scheme_linespecs.jsp?id=" + id + "&segment=15") %>
			<% } 
			}%>
		</tr>
		<% if (!"".equalsIgnoreCase(segment)) { %>
		<tr>
			<td class="top_line" colspan="2"><b><i><%= Bean.bk_schemeXML.getfieldTransl(sementFull, false) %></i></b></td>
		</tr>
		<tr>
			<td><%= Bean.bk_schemeXML.getfieldTransl("cd_participant", false) %></td><td><select name="cd_participant" id="cd_participant" class="inputfield"><%= Bean.getBKParticipantOptions(participant, true) %></select></td>
		</tr>
 		<tr>
			<td colspan="2" align="center" class="bottom_line">
				<%=Bean.getSubmitButtonAjax("../crm/finance/bk_scheme_lineupdate.jsp?id_scheme=" + id_scheme + "&type=setting&action=edit&process=yes&id=" + id + "&segment=" + segment + "&cd_participant='+getElementById('cd_participant').value") %>
				<%=Bean.getGoBackButton("../crm/finance/bk_scheme_linespecs.jsp?id=" + id) %>
			</td>
		</tr>
		<tr>
			<td><%= Bean.bk_schemeXML.getfieldTransl("name_bk_account_scheme_line", false) %></td> <td><input type="text" name="name_bk_account_scheme_line" size="90" value="<%= line.getValue("NAME_BK_ACCOUNT_SCHEME_LINE") %>" readonly="readonly" class="inputfield-ro"> </td>
			<td><%= Bean.bk_schemeXML.getfieldTransl("is_group", false) %></td><td><input type="text" name="is_group" size="15" value="<%= Bean.getMeaningFoCodeValue("YES_NO", line.getValue("IS_GROUP")) %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td valign="top"><%= Bean.bk_schemeXML.getfieldTransl("int_name_bk_account_scheme_line", false) %></td><td valign="top"><input type="text" name="int_nm_bk_account_scheme_line" size="90" value="<%= line.getValue("INT_NM_BK_ACCOUNT_SCHEME_LINE") %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.bk_schemeXML.getfieldTransl("id_bk_account_scheme_ln_parent", false) %></td><td><input type="text" name="id_bk_account_scheme_ln_parent" size="50" value="" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td valign=top rowspan="2"><%= Bean.bk_schemeXML.getfieldTransl("desc_bk_account_scheme_line", false) %></td> <td valign=top rowspan="2"><textarea name="desc_bk_account_scheme_line" cols="90" rows="4" readonly="readonly" class="inputfield-ro"><%= line.getValue("DESC_BK_ACCOUNT_SCHEME_LINE") %></textarea> </td>
			<td><%= Bean.bk_schemeXML.getfieldTransl("exist_flag", false) %> </td><td><input type="text" name="exist_flag" size="15" value="<%= line.getValue("EXIST_FLAG_TSL") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<%} else {%>
		<tr>
			<td><%= Bean.bk_schemeXML.getfieldTransl("name_bk_account_scheme_line", true) %></td> <td><input type="text" name="name_bk_account_scheme_line" size="90" value="<%= line.getValue("NAME_BK_ACCOUNT_SCHEME_LINE") %>" class="inputfield"> </td>
			<td><%= Bean.bk_schemeXML.getfieldTransl("is_group", false) %></td><td><select name="is_group" class="inputfield"><%= Bean.getMeaningFromLookupNameOptions("YES_NO", line.getValue("IS_GROUP"), false) %></select></td>
		</tr>
		<tr>
			<td valign="top"><%= Bean.bk_schemeXML.getfieldTransl("int_nm_bk_account_scheme_line", true) %></td><td valign="top"><input type="text" name="int_nm_bk_account_scheme_line" size="90" value="<%= line.getValue("INT_NM_BK_ACCOUNT_SCHEME_LINE") %>" class="inputfield"></td>
			<td><%= Bean.bk_schemeXML.getfieldTransl("id_bk_account_scheme_ln_parent", false) %></td><td><select name="id_bk_account_scheme_ln_parent" class="inputfield"><OPTION value=""></OPTION><%= Bean.getPostingSettingsGroupOptions(line.getValue("ID_BK_ACCOUNT_SCHEME_LN_PARENT"), false) %></select></td>
		</tr>
		<tr>
			<td valign=top><%= Bean.bk_schemeXML.getfieldTransl("desc_bk_account_scheme_line", false) %></td> <td valign=top><textarea name="desc_bk_account_scheme_line" cols="90" rows="4" class="inputfield"><%= line.getValue("DESC_BK_ACCOUNT_SCHEME_LINE") %></textarea> </td>
			<td><%= Bean.bk_schemeXML.getfieldTransl("exist_flag", false) %> </td><td><select name="exist_flag" class="inputfield"><%= Bean.getMeaningFromLookupNameOptions("YES_NO", line.getValue("EXIST_FLAG"), false) %></select></td>
		</tr>
		<%=	Bean.getCreationAndMoficationRecordFields(
				line.getValue(Bean.getCreationDateFieldName()),
				line.getValue("CREATED_BY"),
				line.getValue(Bean.getLastUpdateDateFieldName()),
				line.getValue("LAST_UPDATE_BY")
			) %>
		<tr>
			<td colspan="6" align="center">
				<%=Bean.getSubmitButtonAjax("../crm/finance/bk_scheme_lineupdate.jsp") %>
				<%=Bean.getResetButton() %>
				<%=Bean.getGoBackButton("../crm/finance/bk_schemespecs.jsp?id=" + id_scheme) %>
			</td>
		</tr>
		<%}%>

	</table>

	</form>
<% } else if (Bean.currentMenu.isCurrentTabAndPreviewPermitted("FINANCE_BK_SCHEME_INFO")) {

 %>
	<form>
	<table <%=Bean.getTableDetailParam() %>>
		<tr>
			<td><%= Bean.bk_schemeXML.getfieldTransl("cd_bk_account_scheme_line", false) %></td>
			<td  colspan="3">
			<%  
			
			   int i = 0;
			  	for (i=1; i<=Integer.parseInt(club.getValue("bk_account_segments_count")); i++) {
			   	if (i==1) {  %>
					<input type="text" name="segment1" size="5" value="<%= line.getValue("SEGMENT1") %>" readonly="readonly" class="inputfield-ro">
				<%} else if (i==2) { %>	
				  	<input type="text" name="segment2" size="5" value="<%= line.getValue("SEGMENT2") %>" readonly="readonly" class="inputfield-ro">
				<%} else if (i==3) { %>	
				  	<input type="text" name="segment3" size="5" value="<%= line.getValue("SEGMENT3") %>" readonly="readonly" class="inputfield-ro">
				<%} else if (i==4) { %>	
				  	<input type="text" name="segment4" size="5" value="<%= line.getValue("SEGMENT4") %>" readonly="readonly" class="inputfield-ro">
				<%} else if (i==5) { %>	
				  	<input type="text" name="segment5" size="5" value="<%= line.getValue("SEGMENT5") %>" readonly="readonly" class="inputfield-ro">
				<%} else if (i==6) { %>	
				  	<input type="text" name="segment6" size="5" value="<%= line.getValue("SEGMENT6") %>" readonly="readonly" class="inputfield-ro">
				<%} else if (i==7) { %>	
				  	<input type="text" name="segment7" size="5" value="<%= line.getValue("SEGMENT7") %>" readonly="readonly" class="inputfield-ro">
				<%} else if (i==8) { %>	
				  	<input type="text" name="segment8" size="5" value="<%= line.getValue("SEGMENT8") %>" readonly="readonly" class="inputfield-ro">
				<%} else if (i==9) { %>	
				  	<input type="text" name="segment9" size="5" value="<%= line.getValue("SEGMENT9") %>" readonly="readonly" class="inputfield-ro">
				<%} else if (i==10) { %>	
				  	<input type="text" name="segment10" size="5" value="<%= line.getValue("SEGMENT10") %>" readonly="readonly" class="inputfield-ro">
				<%} else if (i==11) { %>	
				  	<input type="text" name="segment11" size="5" value="<%= line.getValue("SEGMENT11") %>" readonly="readonly" class="inputfield-ro">
				<%} else if (i==12) { %>	
				  	<input type="text" name="segment12" size="5" value="<%= line.getValue("SEGMENT12") %>" readonly="readonly" class="inputfield-ro">
				<%} else if (i==13) { %>	
				  	<input type="text" name="segment13" size="5" value="<%= line.getValue("SEGMENT13") %>" readonly="readonly" class="inputfield-ro">
				<%} else if (i==14) { %>	
				  	<input type="text" name="segment14" size="5" value="<%= line.getValue("SEGMENT14") %>" readonly="readonly" class="inputfield-ro">
				<%} else if (i==15) { %>	
				  	<input type="text" name="segment15" size="5" value="<%= line.getValue("SEGMENT15") %>" readonly="readonly" class="inputfield-ro">
			<% } 
			}%>
		</tr>
		<tr>
			<td><%= Bean.bk_schemeXML.getfieldTransl("name_bk_account_scheme_line", false) %></td> <td><input type="text" name="name_bk_account_scheme_line" size="90" value="<%= line.getValue("NAME_BK_ACCOUNT_SCHEME_LINE") %>" readonly="readonly" class="inputfield-ro"> </td>
			<td><%= Bean.bk_schemeXML.getfieldTransl("is_group", false) %></td><td><input type="text" name="is_group" size="15" value="<%= Bean.getMeaningFoCodeValue("YES_NO", line.getValue("IS_GROUP")) %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td valign="top"><%= Bean.bk_schemeXML.getfieldTransl("int_nm_bk_account_scheme_line", false) %></td><td valign="top"><input type="text" name="int_nm_bk_account_scheme_line" size="90" value="<%= line.getValue("INT_NM_BK_ACCOUNT_SCHEME_LINE") %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.bk_schemeXML.getfieldTransl("id_bk_account_scheme_ln_parent", false) %></td><td><input type="text" name="id_bk_account_scheme_ln_parent" size="50" value="" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td valign=top rowspan="2"><%= Bean.bk_schemeXML.getfieldTransl("desc_bk_account_scheme_line", false) %></td> <td valign=top rowspan="2"><textarea name="desc_bk_account_scheme_line" cols="90" rows="4" readonly="readonly" class="inputfield-ro"><%= line.getValue("DESC_BK_ACCOUNT_SCHEME_LINE") %></textarea> </td>
			<td><%= Bean.bk_schemeXML.getfieldTransl("exist_flag", false) %> </td><td><input type="text" name="exist_flag" size="15" value="<%= line.getValue("EXIST_FLAG_TSL") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<%=	Bean.getCreationAndMoficationRecordFields(
				line.getValue(Bean.getCreationDateFieldName()),
				line.getValue("CREATED_BY"),
				line.getValue(Bean.getLastUpdateDateFieldName()),
				line.getValue("LAST_UPDATE_BY")
			) %>
		<tr>
			<td colspan="6" align="center">
				<%=Bean.getGoBackButton("../crm/finance/bk_schemespecs.jsp?id=" + id_scheme) %>
			</td>
		</tr>
	</table>
	</form>
    
<% }
} 

if (Bean.currentMenu.isCurrentTabAndAccessPermitted("FINANCE_BK_SCHEME_BK_ACCOUNTS")) { %>
	<table <%= Bean.getTableBottomFilter() %>>
		<tr>
		<%= Bean.getFindHTML("bk_acc_find", bk_acc_find, "../crm/finance/bk_scheme_linespecs.jsp?id=" + id + "&bk_acc_page=1") %>
	
			<td>&nbsp;</td>
		</tr>
	</table>
	<%= line.getBKAccountsHTML(bk_acc_find, l_bk_acc_page_beg, l_bk_acc_page_end) %>
<%
}

if (Bean.currentMenu.isCurrentTabAndAccessPermitted("FINANCE_BK_SCHEME_POSTING_SCHEME")) { %>
	<table <%= Bean.getTableBottomFilter() %>>
		<tr>
		<%= Bean.getFindHTML("posting_scheme_find", posting_scheme_find, "../crm/finance/bk_scheme_linespecs.jsp?id=" + id + "&posting_scheme_page=1") %>
	
		<%=Bean.getSelectOnChangeBeginHTML("posting_scheme_filtr", "../crm/finance/bk_scheme_linespecs.jsp?id=" + id + "&posting_scheme_page=1", Bean.posting_schemeXML.getfieldTransl("general", false)) %>
			<%= Bean.getBKOperationSchemeOptions(posting_scheme_filtr, true) %>
		<%=Bean.getSelectOnChangeEndHTML() %>
		</tr>
	</table>
	<%= line.getOperationSchemesHTML(posting_scheme_find, posting_scheme_filtr, l_posting_scheme_page_beg, l_posting_scheme_page_end) %>
<%
}
 } %>
</div></div>
</body>
</html>
