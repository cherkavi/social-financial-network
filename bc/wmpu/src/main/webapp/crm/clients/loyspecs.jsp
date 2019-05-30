<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%@page import="bc.objects.bcLoySchemeObject"%>

<%= Bean.getLogOutScript(request) %>

<%@page import="java.util.HashMap"%><html>
<head>
	<%= Bean.getMetaContent() %>
	<%= Bean.getBottomFrameCSS() %>

</head>
<body>
<div id="div_tabsheet">

<% 

HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String pageFormName = "CLIENTS_LOY";

String tagLines = "_LINES";
String tagLineFind = "_LINE_FIND";
String tagLineKindLoyality = "_LINE_KIND_LOYALITY";
String tagLineStatus = "_LINE_CARD_STATUS";
String tagLineCategory = "_LINE_CATEGORY";

String tagTerminals = "_TERMINALS";
String tagTerminalFind = "_TERMINAL_FIND";
String tagTerminalType = "_TERMINAL_TYPE";
String tagTerminalStatus = "_TERMINAL_STATUS";

Bean.setJspPageForTabName(pageFormName);

String id = Bean.getDecodeParam(parameters.get("id"));
String tab = Bean.getDecodeParam(parameters.get("tab"));
if ( tab==null || tab.trim().length() < 1 ) { tab = Bean.tabsHmGetValue(pageFormName); }
%>

<%

if ( id==null || id.trim().length() < 1 ) {
%>
	<%=Bean.getIDNotFoundMessage() %>
<% 
} else {	
	Bean.tabsHmSetValue(pageFormName, tab);
	bcLoySchemeObject loy = new bcLoySchemeObject(id);
	
	//Обрабатываем номера страниц
	String l_lines_page = Bean.getDecodeParam(parameters.get("lines_page"));
	Bean.pageCheck(pageFormName + tagLines, l_lines_page);
	String l_lines_page_beg = Bean.getFirstRowNumber(pageFormName + tagLines);
	String l_lines_page_end = Bean.getLastRowNumber(pageFormName + tagLines);
	
	String line_find 	= Bean.getDecodeParam(parameters.get("line_find"));
	line_find 	= Bean.checkFindString(pageFormName + tagLineFind, line_find, l_lines_page);
	
	String cd_kind = Bean.getDecodeParam(parameters.get("cd_kind"));
	cd_kind 		= Bean.checkFindString(pageFormName + tagLineKindLoyality, cd_kind, l_lines_page);

	String id_status = Bean.getDecodeParam(parameters.get("id_status"));
	id_status 		= Bean.checkFindString(pageFormName + tagLineStatus, id_status, l_lines_page);

	String id_category = Bean.getDecodeParam(parameters.get("id_category"));
	id_category 	= Bean.checkFindString(pageFormName + tagLineCategory, id_category, l_lines_page);

	String l_terminals_page = Bean.getDecodeParam(parameters.get("terminals_page"));
	Bean.pageCheck(pageFormName + tagTerminals, l_terminals_page);
	String l_terminals_page_beg = Bean.getFirstRowNumber(pageFormName + tagTerminals);
	String l_terminals_page_end = Bean.getLastRowNumber(pageFormName + tagTerminals);
	
	String terminal_find 	= Bean.getDecodeParam(parameters.get("terminal_find"));
	terminal_find 	= Bean.checkFindString(pageFormName + tagTerminalFind, terminal_find, l_terminals_page);
	
	String terminal_status	= Bean.getDecodeParam(parameters.get("terminal_status"));
	terminal_status		= Bean.checkFindString(pageFormName + tagTerminalStatus, terminal_status, l_terminals_page);
	
	String terminal_type	= Bean.getDecodeParam(parameters.get("terminal_type"));
	terminal_type		= Bean.checkFindString(pageFormName + tagTerminalType, terminal_type, l_terminals_page);
	
	%>

	<table <%=Bean.getTableMenuParam() %>>
		<tr>
			<%= Bean.getPageHeader() %>

			<% if (Bean.currentMenu.isCurrentTabAndEditPermitted("CLIENTS_LOY_INFO")) { %>
			    <%= Bean.getMenuButton("ADD", "../crm/clients/loyupdate.jsp?id_loyality_scheme=" + id + "&type=general&action=add2&process=no", "", "") %>
			    <%= Bean.getMenuButton("COPY", "../crm/clients/loyupdate.jsp?id_loyality_scheme=" + id + "&type=general&action=copy&process=no", "", "") %>
			    <%= Bean.getMenuButton("DELETE", "../crm/clients/loyupdate.jsp?id_loyality_scheme=" + id + "&type=general&action=remove&process=yes", Bean.loyXML.getfieldTransl("H_LOYALITY_DELETE", false), loy.getValue("NAME_LOYALITY_SCHEME")) %>
			<%  } %>
		
			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLIENTS_LOY_BON")) { %>
				<% if (Bean.currentMenu.isCurrentTabAndEditPermitted("CLIENTS_LOY_BON")) { %>
				    <%= Bean.getMenuButton("ADD", "../crm/clients/loylineupdate.jsp?id_loyality_scheme=" + id + "&type=loylines&action=add&process=no", "", "") %>
				<% } %>	
	
			    <!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagLines, "../crm/clients/loyspecs.jsp?id=" + id + "&tab="+Bean.currentMenu.getTabID("CLIENTS_LOY_BON")+"&", "lines_page") %>
			<%  }  %>
	
			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLIENTS_LOY_TERM")) {%>
			    <!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagTerminals, "../crm/clients/loyspecs.jsp?id=" + id + "&tab="+Bean.currentMenu.getTabID("CLIENTS_LOY_TERM")+"&", "terminals_page") %>
			<% } %>
	
		</tr>
	</table>
	<table <%=Bean.getTableMenuParam() %>>
		<%= Bean.getDetailCaption(loy.getValue("NAME_LOYALITY_SCHEME") + " (" + loy.getValue("SNAME_JUR_PRS") + ")") %>
		<tr>
			<td>
				<!-- Выводим перечень закладок -->
				<%= Bean.currentMenu.getFormTabScheeds(tab, "../crm/clients/loyspecs.jsp?id=" + id) %>
			</td>
		</tr>
	</table>
</div>
<div id="div_data">
<div id="div_data_detail">
<%

if (Bean.currentMenu.isCurrentTabAndPreviewPermitted("CLIENTS_LOY_INFO")) {
%>
	<form>
	<table <%=Bean.getTableDetailParam() %> style="border-bottom:none">
		<tr>
			<td><%= Bean.loyXML.getfieldTransl("name_loyality_scheme", false) %> </td> <td><input type="text" name="NAME_LOYALITY_SCHEME" size="70" value="<%= loy.getValue("NAME_LOYALITY_SCHEME") %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.clubXML.getfieldTransl("club", false) %>
					<%= Bean.getGoToClubLink(loy.getValue("ID_CLUB")) %>
			</td><td><input type="text" name="name_club" size="30" value="<%= Bean.getClubShortName(loy.getValue("ID_CLUB")) %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td rowspan="3"><%= Bean.loyXML.getfieldTransl("desc_loyality_scheme", false) %></td><td rowspan="3"><textarea name="DESC_LOYALITY_SCHEME" cols="67" rows="3" readonly="readonly" class="inputfield-ro"><%= loy.getValue("DESC_LOYALITY_SCHEME") %></textarea></td>
			<td><%= Bean.loyXML.getfieldTransl("date_beg", false) %></td> <td><input type="text" name="DATE_BEG" size="10" value="<%= loy.getValue("DATE_BEG_FRMT") %>" readonly="readonly" class="inputfield-ro"> </td>
		</tr>
		<tr>
			<td valign="top"><%= Bean.loyXML.getfieldTransl("date_end", false) %></td> <td valign="top"><input type="text" name="DATE_END" size="10" value="<%= loy.getValue("DATE_END_FRMT") %>" readonly="readonly" class="inputfield-ro"> </td>
		</tr>
		<tr>
			<td colspan="2">&nbsp;</td>
		</tr>
	    <tr>
			<td><%= Bean.clubcardXML.getfieldTransl("sname_partner", false) %>
				<%=Bean.getGoToJurPrsHyperLink(loy.getValue("ID_JUR_PRS")) %>
			</td> 
			<td>
				<input type="text" name="name_jur_prs" size="70" value="<%= loy.getValue("SNAME_JUR_PRS") %>" readonly="readonly" class="inputfield-ro">
			</td>
            <td colspan="2">&nbsp;</td>
		</tr>
   		<tr>
			<td><%= Bean.clubcardXML.getfieldTransl("id_doc", false) %>
				<%= Bean.getGoToDocLink(loy.getValue("ID_DOC")) %>
			</td> 
			<td>
				<input type="text" name="name_doc" size="70" value="<%= Bean.getDocName(loy.getValue("ID_DOC")) %>" readonly="readonly" class="inputfield-ro">
			</td>
			<td colspan="2">&nbsp;</td>
   		</tr>
		<tr>
			<td colspan="4">&nbsp;</td>
   		</tr>
	</table>
	<table <%=Bean.getTableDetailParam() %>>
		<tr>
			<td><%= Bean.loyXML.getfieldTransl("cd_kind_loyality", false) %> </td><td colspan="2"><input type="text" name="CD_KIND_LOYALITY" size="75" value="<%= loy.getValue("NAME_KIND_LOYALITY")%>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td><%= Bean.loyXML.getfieldTransl("term_type_calc", false) %></td> <td colspan="2"><input type="text" name="TERM_TYPE_CALC" size="75" value="<%= loy.getValue("TERM_TYPE_CALC_TSL")%>" readonly="readonly" class="inputfield-ro"> </td>
		</tr>
		<tr>
			<td><%= Bean.loyXML.getfieldTransl("cd_currency", false) %></td><td>
				<input type="text" name="name_currency" size="20" value="<%= loy.getValue("NAME_CURRENCY") %>" readonly="readonly" class="inputfield-ro">
			</td>
			<td colspan="2">&nbsp;</td>
		</tr>
		<tr>
			<td><%= Bean.loyXML.getfieldTransl("term_max_bonus", false) %>, <%= loy.getValue("SNAME_CURRENCY") %></td> <td><input type="text" name="TERM_MAX_BONUS" size="20" value="<%= loy.getValue("TERM_MAX_BONUS_FRMT")%>" readonly="readonly" class="inputfield-ro"> </td>
			<td><%= Bean.loyXML.getfieldTransl("term_cash_bon", false) %></td> <td><input type="text" name="TERM_CASH_BON" size="20" value="<%= loy.getValue("TERM_BON_BON_TSL")%>" readonly="readonly" class="inputfield-ro"> </td>
		</tr>
		<tr>
			<td><%= Bean.loyXML.getfieldTransl("term_min_amount", false) %>, <%= loy.getValue("SNAME_CURRENCY") %></td> <td><input type="text" name="TERM_MIN_AMOUNT" size="20" value="<%= loy.getValue("TERM_MIN_AMOUNT_FRMT")%>" readonly="readonly" class="inputfield-ro"> </td>
			<td> <%= Bean.loyXML.getfieldTransl("TERM_BON_BON", false) %></td><td><input type="text" name="TERM_BON_BON" size="20" class="inputfield-ro" readonly="readonly" value="<%= loy.getValue("TERM_BON_BON_TSL") %>"></td>
		</tr>
		<tr>
			<td><%= Bean.loyXML.getfieldTransl("term_max_sumpayoffline", false) %>, <%= loy.getValue("SNAME_CURRENCY") %></td> <td><input type="text" name="TERM_MAX_SUMPAYOFFLINE" size="20" value="<%= loy.getValue("TERM_MAX_SUMPAYOFFLINE_FRMT")%>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.loyXML.getfieldTransl("term_loyality_for_all_nsmep", false) %> </td> <td><input type="text" name="TERM_LOYALITY_FOR_ALL_NSMEP" size="20" value="<%= loy.getValue("TERM_LOY_FOR_ALL_NSMEP_TSL")%>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td><%= Bean.loyXML.getfieldTransl("term_max_sumpaynopin", false) %>, <%= loy.getValue("SNAME_CURRENCY") %></td> <td><input type="text" name="TERM_MAX_SUMPAYNOPIN" size="20" value="<%= loy.getValue("TERM_MAX_SUMPAYNOPIN_FRMT")%>" readonly="readonly" class="inputfield-ro"> </td>
			<td><%= Bean.loyXML.getfieldTransl("term_rounding_rule", false) %></td> <td><input type="text" name="TERM_ROUNDING_RULE" size="20" value="<%= loy.getValue("TERM_ROUNDING_RULE_TSL")%>" readonly="readonly" class="inputfield-ro"> </td>
		</tr>
		<tr>
			<td><%= Bean.loyXML.getfieldTransl("term_max_date_onl_term", false) %> </td> <td><input type="text" name="TERM_MAX_DATE_ONL_TERM" size="20" value="<%= loy.getValue("TERM_MAX_DATE_ONL_TERM")%>" readonly="readonly" class="inputfield-ro"></td>
			<td> <%= Bean.loyXML.getfieldTransl("TERM_EXT_LOYL", false) %></td><td><input type="text" name="TERM_EXT_LOYL" size="20" class="inputfield-ro" readonly="readonly" value="<%= loy.getValue("TERM_EXT_LOYL_TSL") %>"></td>			
		</tr>
		<tr>
			<td><%= Bean.loyXML.getfieldTransl("term_print_filter", false) %> </td> <td><input type="text" name="TERM_PRINT_FILTER" size="20" value="<%= Bean.getMeaningForNumValue("LOYALITY_PRINT_TERM", loy.getValue("TERM_PRINT_FILTER")) %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.loyXML.getfieldTransl("term_nomenkl", false) %> </td> <td><input type="text" name="TERM_NOMENKL" size="20" value="<%= loy.getValue("TERM_NOMENKL") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<%=	Bean.getIdCreationAndMoficationRecordFields(
				loy.getValue("ID_LOYALITY_SCHEME"),
				loy.getValue(Bean.getCreationDateFieldName()),
				loy.getValue("CREATED_BY"),
				loy.getValue(Bean.getLastUpdateDateFieldName()),
				loy.getValue("LAST_UPDATE_BY")
			) %>
 		<tr>
			<td colspan="6" align="center">
				<%=Bean.getGoBackButton("../crm/clients/loy.jsp") %>
			</td>
		</tr>
	</table>
	</form>
<%  
} else if (Bean.currentMenu.isCurrentTabAndEditPermitted("CLIENTS_LOY_INFO")) { %>
	<script>
		var formData = new Array (
			new Array ('NAME_LOYALITY_SCHEME', 'varchar2', 1),
			new Array ('name_jur_prs', 'varchar2', 1),
			new Array ('DATE_BEG', 'varchar2', 1),
			new Array ('CD_KIND_LOYALITY', 'varchar2', 1),
			new Array ('TERM_TYPE_CALC', 'varchar2', 1),
			new Array ('TERM_MAX_BONUS', 'varchar2', 1),
			new Array ('TERM_MIN_AMOUNT', 'varchar2', 1),
			new Array ('TERM_MAX_SUMPAYOFFLINE', 'varchar2', 1),
			new Array ('TERM_MAX_SUMPAYNOPIN', 'varchar2', 1),
			new Array ('TERM_MAX_DATE_ONL_TERM', 'varchar2', 1),
			new Array ('TERM_CASH_BON', 'varchar2', 1),
			new Array ('TERM_BON_BON', 'varchar2', 1),
			new Array ('TERM_LOYALITY_FOR_ALL_NSMEP', 'varchar2', 1),
			new Array ('TERM_ROUNDING_RULE', 'varchar2', 1),
			new Array ('TERM_EXT_LOYL', 'varchar2', 1)
		);

		function myValidateForm(){
			return validateForm(formData);
		}
	</script>

      <form action="../crm/clients/loyupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return myValidateForm()">
	        <input type="hidden" name="type" value="general">
	        <input type="hidden" name="action" value="edit">
	        <input type="hidden" name="process" value="yes">
	        <input type="hidden" name="id_loyality_scheme" value="<%= loy.getValue("ID_LOYALITY_SCHEME") %>">
	<table <%=Bean.getTableDetailParam() %> style="border-bottom:none">
		<tr>
			<td><%= Bean.loyXML.getfieldTransl("name_loyality_scheme", true) %> </td> <td><input type="text" name="NAME_LOYALITY_SCHEME" size="70" value="<%= loy.getValue("NAME_LOYALITY_SCHEME") %>" class="inputfield"></td>
			<td><%= Bean.clubXML.getfieldTransl("club", false) %>
					<%= Bean.getGoToClubLink(loy.getValue("ID_CLUB")) %>
			</td><td><input type="text" name="name_club" size="30" value="<%= Bean.getClubShortName(loy.getValue("ID_CLUB")) %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td rowspan="3"><%= Bean.loyXML.getfieldTransl("desc_loyality_scheme", false) %></td><td rowspan="3"><textarea name="DESC_LOYALITY_SCHEME" cols="67" rows="3" class="inputfield"><%= loy.getValue("DESC_LOYALITY_SCHEME") %></textarea></td>
			<td><%= Bean.loyXML.getfieldTransl("date_beg", true) %></td> <td><%=Bean.getCalendarInputField("DATE_BEG", loy.getValue("DATE_BEG_DF"), "10") %></td>
		</tr>
		<tr>
			<td valign="top"><%= Bean.loyXML.getfieldTransl("date_end", false) %></td> <td valign="top"><%=Bean.getCalendarInputField("DATE_END", loy.getValue("DATE_END_DF"), "10") %></td>
		</tr>
		<tr>
			<td colspan="2">&nbsp;</td>
   		</tr>
	    <tr>
			<td><%= Bean.clubcardXML.getfieldTransl("sname_partner", true) %>
				<%=Bean.getGoToJurPrsHyperLink(loy.getValue("ID_JUR_PRS")) %>
			</td> 
			<td>
				<%=Bean.getWindowFindJurPrs("jur_prs", loy.getValue("ID_JUR_PRS"), loy.getValue("SNAME_JUR_PRS"), "ALL", "60") %>
			</td>
            <td colspan="2">&nbsp;</td>
		</tr>
   		<tr>
			<td><%= Bean.clubcardXML.getfieldTransl("id_doc", false) %>
				<%= Bean.getGoToDocLink(loy.getValue("ID_DOC")) %>
			</td> 
			<td>
				<%=Bean.getWindowDocuments("doc", loy.getValue("ID_DOC"), "60") %>
			</td>
			<td colspan="2">&nbsp;</td>
   		</tr>
		<tr>
			<td colspan="4">&nbsp;</td>
   		</tr>
	</table>
	<table <%=Bean.getTableDetailParam() %>>	
		<tr>		    
			<td><%= Bean.loyXML.getfieldTransl("cd_kind_loyality", true) %> </td><td colspan="3"><select name="CD_KIND_LOYALITY"  class="inputfield"><%= Bean.getLoyalityKindOptions(loy.getValue("CD_KIND_LOYALITY"),true) %></select></td>
		</tr>
		<tr>		    
			<td><%= Bean.loyXML.getfieldTransl("term_type_calc", true) %></td> <td colspan="3"><select name="TERM_TYPE_CALC"  class="inputfield"><%= Bean.getMeaningFromLookupNumHTML("TYPE_CALC", loy.getValue("TERM_TYPE_CALC"), true) %></select></td>
		</tr>
		<tr>		    
			<td><%= Bean.loyXML.getfieldTransl("cd_currency", false) %></td><td>
				<input type="hidden" name="cd_currency" value="<%= loy.getValue("CD_CURRENCY") %>">
				<input type="text" name="name_currency" size="20" value="<%= loy.getValue("NAME_CURRENCY") %>" readonly="readonly" class="inputfield-ro">
			</td>
			<td colspan="2">&nbsp;</td>
		</tr>
		<tr>
			<td><%= Bean.loyXML.getfieldTransl("term_max_bonus", true) %>, <%= loy.getValue("SNAME_CURRENCY") %></td> <td><input type="text" name="TERM_MAX_BONUS" size="20" value="<%= loy.getValue("TERM_MAX_BONUS_FRMT") %>" class="inputfield"> </td>
			<td><%= Bean.loyXML.getfieldTransl("term_cash_bon", true) %></td> <td><select name="TERM_CASH_BON"  class="inputfield"><%= Bean.getMeaningFromLookupNumHTML("YES_NO", loy.getValue("TERM_CASH_BON"), true) %></select></td>
		</tr>
		<tr>
			<td><%= Bean.loyXML.getfieldTransl("term_min_amount", true) %>, <%= loy.getValue("SNAME_CURRENCY") %></td> <td><input type="text" name="TERM_MIN_AMOUNT" size="20" value="<%= loy.getValue("TERM_MIN_AMOUNT_FRMT") %>" class="inputfield"> </td>
			<td><%= Bean.loyXML.getfieldTransl("term_bon_bon", true) %></td><td><select name="TERM_BON_BON"  class="inputfield"><%= Bean.getMeaningFromLookupNumHTML("YES_NO", loy.getValue("TERM_BON_BON"), true) %></select></td>
		</tr>
		<tr>
			<td><%= Bean.loyXML.getfieldTransl("term_max_sumpayoffline", true) %>, <%= loy.getValue("SNAME_CURRENCY") %> </td> <td><input type="text" name="TERM_MAX_SUMPAYOFFLINE" size="20" value="<%= loy.getValue("TERM_MAX_SUMPAYOFFLINE_FRMT") %>" class="inputfield"></td>
			<td><%= Bean.loyXML.getfieldTransl("term_loyality_for_all_nsmep", true) %></td><td><select name="TERM_LOYALITY_FOR_ALL_NSMEP"  class="inputfield"><%= Bean.getMeaningFromLookupNumHTML("YES_NO", loy.getValue("TERM_LOYALITY_FOR_ALL_NSMEP"), true) %></select></td>
		</tr>
		<tr>			
			<td><%= Bean.loyXML.getfieldTransl("term_max_sumpaynopin", true) %>, <%= loy.getValue("SNAME_CURRENCY") %></td> <td><input type="text" name="TERM_MAX_SUMPAYNOPIN" size="20" value="<%= loy.getValue("TERM_MAX_SUMPAYNOPIN_FRMT") %>" class="inputfield"> </td>
			<td><%= Bean.loyXML.getfieldTransl("term_rounding_rule", true) %></td> <td><select name="TERM_ROUNDING_RULE"  class="inputfield"><%= Bean.getMeaningFromLookupNumHTML("ROUNDING_RULE", loy.getValue("TERM_ROUNDING_RULE"), true) %></select></td>
		</tr>
		<tr>
			<td><%= Bean.loyXML.getfieldTransl("term_max_date_onl_term", true) %> </td> <td><input type="text" name="TERM_MAX_DATE_ONL_TERM" size="20" value="<%= loy.getValue("TERM_MAX_DATE_ONL_TERM") %>" class="inputfield"></td>
			<td><%= Bean.loyXML.getfieldTransl("term_ext_loyl", true) %></td><td><select name="TERM_EXT_LOYL"  class="inputfield"><%= Bean.getMeaningFromLookupNumHTML("YES_NO", loy.getValue("TERM_EXT_LOYL"), true) %></select></td>			
		</tr>
		<tr>
			<td><%= Bean.loyXML.getfieldTransl("term_limit_cash", false) %> </td> <td><input type="text" name="TERM_LIMIT_CASH" size="20" value="<%= loy.getValue("TERM_LIMIT_CASH_FRMT")%>" class="inputfield"></td>
			<td><%= Bean.loyXML.getfieldTransl("term_nomenkl", false) %> </td> <td><input type="text" name="TERM_NOMENKL" size="25" value="<%= loy.getValue("TERM_NOMENKL") %>" class="inputfield"></td>
		</tr>
		<%=	Bean.getIdCreationAndMoficationRecordFields(
				loy.getValue("ID_LOYALITY_SCHEME"),
				loy.getValue(Bean.getCreationDateFieldName()),
				loy.getValue("CREATED_BY"),
				loy.getValue(Bean.getLastUpdateDateFieldName()),
				loy.getValue("LAST_UPDATE_BY")
			) %>
 		<tr>
			<td colspan="6" align="center">
				<%=Bean.getSubmitButtonAjax("../crm/clients/loyupdate.jsp") %>
				<%=Bean.getResetButton() %>
				<%=Bean.getGoBackButton("../crm/clients/loy.jsp") %>
			</td>
		</tr>

	</table>
	</form>
		
	<!-- Скрипт для втавки меню вибору дати -->
		<%= Bean.getCalendarScript("DATE_BEG", false) %>
		<%= Bean.getCalendarScript("DATE_END", false) %>
<%	}

if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLIENTS_LOY_BON")) {%>
		<table <%=Bean.getTableBottomFilter() %>><tbody>
		<tr>
			<%=Bean.getFindHTML("line_find", line_find, "../crm/clients/loyspecs.jsp?id=" + id + "&lines_page=1") %>
		
			<%=Bean.getSelectOnChangeBeginHTML("cd_kind", "../crm/clients/loyspecs.jsp?id=" + id + "&lines_page=1", Bean.loylineXML.getfieldTransl("CD_KIND_LOYALITY", false)) %>
				<%= Bean.getLoyalityKindOnlyCDOptions(cd_kind, true) %>
			<%=Bean.getSelectOnChangeEndHTML() %>

			<%=Bean.getSelectOnChangeBeginHTML("id_status", "../crm/clients/loyspecs.jsp?id=" + id + "&lines_page=1", Bean.loylineXML.getfieldTransl("NAME_CARD_STATUS", false)) %>
				<%= Bean.getClubCardStatusOptions(id_status, true) %>
			<%=Bean.getSelectOnChangeEndHTML() %>

			<%=Bean.getSelectOnChangeBeginHTML("id_category", "../crm/clients/loyspecs.jsp?id=" + id + "&lines_page=1", Bean.loylineXML.getfieldTransl("NAME_CATEGORY", false)) %>
				<%= Bean.getClubCardCategoryForStatusOptions(id_status, id_category, true) %>
			<%=Bean.getSelectOnChangeEndHTML() %>
		</tr>
		</tbody>
		</table>
	<%= loy.getLoyalityLinesHTML(line_find, cd_kind, id_status, id_category, l_lines_page_beg, l_lines_page_end) %>
<%}
if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLIENTS_LOY_TERM")) {%>
		<table <%=Bean.getTableBottomFilter() %>><tbody>
		<tr>
			<%=Bean.getFindHTML("terminal_find", terminal_find, "../crm/clients/loyspecs.jsp?id=" + id + "&terminals_page=1") %>
		
			<%=Bean.getSelectOnChangeBeginHTML("terminal_type", "../crm/clients/loyspecs.jsp?id=" + id + "&terminals_page=1", Bean.terminalXML.getfieldTransl("cd_term_type", false)) %>
				<%= Bean.getTermTypeOptions(terminal_type, true) %>
			<%=Bean.getSelectOnChangeEndHTML() %>

			<%=Bean.getSelectOnChangeBeginHTML("terminal_status", "../crm/clients/loyspecs.jsp?id=" + id + "&terminals_page=1", Bean.terminalXML.getfieldTransl("cd_term_status", false)) %>
				<%= Bean.getTermStatusOptions(terminal_status, true) %>
			<%=Bean.getSelectOnChangeEndHTML() %>
		</tr>
		</tbody>
		</table>
	<%= loy.getTerminalsHTML(terminal_find, terminal_type, terminal_status, l_terminals_page_beg, l_terminals_page_end) %>
<%}
%>

<%   } %>
</div></div>
</body>
</html>