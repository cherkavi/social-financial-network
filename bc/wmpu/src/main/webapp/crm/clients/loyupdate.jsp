<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%@page import="bc.objects.bcLoySchemeObject"%>
<%@page import="bc.objects.bcClubShortObject"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.ArrayList"%>
<%@page import="bc.lists.bcListLoyalityScheme"%>
<%@page import="bc.service.bcFeautureParam"%><html>

<%= Bean.getLogOutScript(request) %>

<head>
	<%= Bean.getMetaContent() %>
	<%= Bean.getBottomFrameCSS() %>
</head>

<body>
<%
HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String pageFormName = "CLIENTS_LOY";

Bean.setJspPageForTabName(pageFormName);

request.setCharacterEncoding("UTF-8");

String external_id			= Bean.getDecodeParamPrepare(parameters.get("id"));
String id_loyality_scheme	= Bean.getDecodeParamPrepare(parameters.get("id_loyality_scheme"));
String id_line				= Bean.getDecodeParamPrepare(parameters.get("id_line"));
String type					= Bean.getDecodeParamPrepare(parameters.get("type"));
String action				= Bean.getDecodeParamPrepare(parameters.get("action")); 
String process				= Bean.getDecodeParamPrepare(parameters.get("process"));
String back_type			= Bean.getDecodeParamPrepare(parameters.get("back_type"));
String id_jur_prs	 		= Bean.getDecodeParamPrepare(parameters.get("id_jur_prs"));
String id_doc		 		= Bean.getDecodeParamPrepare(parameters.get("id_doc"));
String id_club		 		= Bean.getDecodeParamPrepare(parameters.get("id_club"));

String updateLink = "../crm/clients/loyupdate.jsp";
String backLink = "";
String generalLink = "";
back_type = Bean.isEmpty(back_type)?"LOYALITY_SCHEME":back_type;
System.out.println("back_type="+back_type);
if ("LOYALITY_SCHEME".equalsIgnoreCase(back_type)) {
	generalLink = "../crm/clients/loy.jsp?";
	if (action.equalsIgnoreCase("add")) {
		backLink = "../crm/clients/loy.jsp?";
	} else if (action.equalsIgnoreCase("copy")) {
		backLink = "../crm/clients/loyspecs.jsp?";
	} else {
		backLink = "../crm/clients/loyspecs.jsp?id="+id_loyality_scheme;
	}
} else if ("DOC".equalsIgnoreCase(back_type)) {
	generalLink = "../crm/club/documentspecs.jsp?id="+external_id;
	backLink = "../crm/club/documentspecs.jsp?id="+external_id;
} else if ("PARTNER".equalsIgnoreCase(back_type)) {
	generalLink = "../crm/clients/yurpersonspecs.jsp?id="+external_id;
	backLink = "../crm/clients/yurpersonspecs.jsp?id="+external_id;
}

if (type.equalsIgnoreCase("general")) {
  if (process.equalsIgnoreCase("no")) {
	   /*  --- Добавити запис --- */
	   
	   %>
	   	<script>
		var formData = new Array (
			new Array ('NAME_LOYALITY_SCHEME', 'varchar2', 1),
			new Array ('name_jur_prs', 'varchar2', 1),
			new Array ('DATE_BEG', 'varchar2', 1),
			new Array ('CD_KIND_LOYALITY', 'varchar2', 1),
			new Array ('TERM_TYPE_CALC', 'varchar2', 1),
			new Array ('cd_currency', 'varchar2', 1),
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

	   <%
	   
    	if (action.equalsIgnoreCase("add") || action.equalsIgnoreCase("add2")) {
    		
    		bcClubShortObject club = new bcClubShortObject(Bean.getCurrentClubID());
    		
	        %> 
			<% if ("LOYALITY_SCHEME".equalsIgnoreCase(back_type)) { %>
			<%= Bean.getOperationTitle(
					Bean.loyXML.getfieldTransl("H_LOYALITY_ADD", false),
					"Y",
					"Y") 
			%>
			<% } else { %>
			<%= Bean.getOperationTitleShort(
					"",
					Bean.loyXML.getfieldTransl("H_LOYALITY_ADD", false),
					"Y",
					"Y") 
			%>
			<% } %>
		<form action="<%=updateLink %>" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return myValidateForm()">  	        
	        <input type="hidden" name="type" value="general">
	        <input type="hidden" name="action" value="add">
	        <input type="hidden" name="process" value="yes">
    		<input type="hidden" name="id" value="<%=external_id %>">
    		<input type="hidden" name="back_type" value="<%=back_type %>">
		<table <%=Bean.getTableDetailParam() %> style="border-bottom:none">
		<tr>
			<td><%= Bean.loyXML.getfieldTransl("name_loyality_scheme", true) %> </td> <td><input type="text" name="NAME_LOYALITY_SCHEME" size="70" value="" class="inputfield"></td>
 		    <td><%= Bean.clubXML.getfieldTransl("club", true) %>
				<%= Bean.getGoToClubLink(club.getValue("ID_CLUB")) %>
			</td>
		  	<td>
				<%=Bean.getWindowFindClub("club", club.getValue("ID_CLUB"), club.getValue("SNAME_CLUB"), "25") %>
	  		</td>
		</tr>
		<tr>
			<td rowspan="3"><%= Bean.loyXML.getfieldTransl("desc_loyality_scheme", false) %></td><td rowspan="3"><textarea name="DESC_LOYALITY_SCHEME" cols="67" rows="3" class="inputfield"></textarea></td>
			<td><%= Bean.loyXML.getfieldTransl("date_beg", true) %></td> <td><%=Bean.getCalendarInputField("DATE_BEG", Bean.getSysDate(), "10") %></td>
		</tr>
		<tr>
			<td><%= Bean.loyXML.getfieldTransl("date_end", false) %></td> <td><%=Bean.getCalendarInputField("DATE_END", "", "10") %></td>
		</tr>
		<tr>
			<td colspan="2">&nbsp;</td>
   		</tr>
	    <tr>
			<td><%= Bean.clubcardXML.getfieldTransl("sname_partner", true) %></td> 
			<td>
				<%=Bean.getWindowFindJurPrs("jur_prs", "", "", "ALL", "60") %>
			</td>
            <td colspan="2">&nbsp;</td>
		</tr>
   		<tr>
			<td><%= Bean.clubcardXML.getfieldTransl("id_doc", false) %></td> 
			<td>
				<%=Bean.getWindowDocuments("doc", "", "60") %>
			</td>
			<td colspan="2">&nbsp;</td>
   		</tr>
		<tr>
			<td colspan="2">&nbsp;</td>
   		</tr>
	</table>
	<table <%=Bean.getTableDetailParam() %>>	
		<tr>		    
			<td><%= Bean.loyXML.getfieldTransl("cd_kind_loyality", true) %> </td><td colspan="3"><select name="CD_KIND_LOYALITY"  class="inputfield"><%= Bean.getLoyalityKindOptions("0001",false) %></select></td>
		</tr>
		<tr>		    
			<td><%= Bean.loyXML.getfieldTransl("term_type_calc", true) %></td> <td colspan="3"><select name="TERM_TYPE_CALC"  class="inputfield"><%= Bean.getMeaningFromLookupNumHTML("TYPE_CALC", "0", false) %></select></td>
		</tr>
		<tr>		    
			<td><%= Bean.loyXML.getfieldTransl("cd_currency", true) %></td> <td colspan="2"><select name="cd_currency"  class="inputfield"><%= Bean.getCurrencyOptions("", false) %></select></td>
			<td colspan="2">&nbsp;</td>
		</tr>
		<tr>
			<td><%= Bean.loyXML.getfieldTransl("term_max_bonus", true) %></td> <td><input type="text" name="TERM_MAX_BONUS" size="20" value="" class="inputfield"> </td>
			<td><%= Bean.loyXML.getfieldTransl("term_cash_bon", true) %></td> <td><select name="TERM_CASH_BON"  class="inputfield"><%= Bean.getMeaningFromLookupNumHTML("YES_NO", "0", false) %></select></td>
		</tr>
		<tr>
			<td><%= Bean.loyXML.getfieldTransl("term_min_amount", true) %></td> <td><input type="text" name="TERM_MIN_AMOUNT" size="20" value="" class="inputfield"> </td>
			<td><%= Bean.loyXML.getfieldTransl("term_bon_bon", true) %></td><td><select name="TERM_BON_BON"  class="inputfield"><%= Bean.getMeaningFromLookupNumHTML("YES_NO", "0", false) %></select></td>
		</tr>
		<tr>
			<td><%= Bean.loyXML.getfieldTransl("term_max_sumpayoffline", true) %></td> <td><input type="text" name="TERM_MAX_SUMPAYOFFLINE" size="20" value="" class="inputfield"></td>
			<td><%= Bean.loyXML.getfieldTransl("term_loyality_for_all_nsmep", true) %></td><td><select name="TERM_LOYALITY_FOR_ALL_NSMEP"  class="inputfield"><%= Bean.getMeaningFromLookupNumHTML("YES_NO", "0", false) %></select></td>			
		</tr>
		<tr>			
			<td><%= Bean.loyXML.getfieldTransl("term_max_sumpaynopin", true) %></td> <td><input type="text" name="TERM_MAX_SUMPAYNOPIN" size="20" value="" class="inputfield"> </td>
			<td><%= Bean.loyXML.getfieldTransl("term_rounding_rule", true) %></td> <td><select name="TERM_ROUNDING_RULE"  class="inputfield"><%= Bean.getMeaningFromLookupNumHTML("ROUNDING_RULE", "1", false) %></select></td>
		</tr>
		<tr>
			<td><%= Bean.loyXML.getfieldTransl("term_max_date_onl_term", true) %> </td> <td><input type="text" name="TERM_MAX_DATE_ONL_TERM" size="20" value="" class="inputfield"></td>
			<td><%= Bean.loyXML.getfieldTransl("term_ext_loyl", true) %></td><td><select name="TERM_EXT_LOYL"  class="inputfield"><%= Bean.getMeaningFromLookupNumHTML("YES_NO", "0", false) %></select></td>			
		</tr>
		<tr>
			<td><span id="id_limit_cash"><%= Bean.loyXML.getfieldTransl("term_limit_cash", false) %></span></td> <td><input type="text" name="TERM_LIMIT_CASH" size="20" value="" class="inputfield"></td>
			<td><%= Bean.loyXML.getfieldTransl("term_nomenkl", false) %> </td> <td><input type="text" name="TERM_NOMENKL" size="25" value="" class="inputfield"></td>
		</tr>
 		<tr>
			<td colspan="6" align="center">
				<%=Bean.getSubmitButtonAjax(updateLink, "submit", "updateForm", ("LOYALITY_SCHEME".equalsIgnoreCase(back_type))?"div_main":"div_data_detail") %>
				<%=Bean.getResetButton() %>
				<%=Bean.getGoBackButton(backLink) %>
			</td>
		</tr>
	</table>
	</form>
		
	<!-- Скрипт для втавки меню вибору дати -->
		<%= Bean.getCalendarScript("DATE_BEG", false) %>
		<%= Bean.getCalendarScript("DATE_END", false) %>

	        <%
		} else if (action.equalsIgnoreCase("select")) {
			String tagLoyality = "" + back_type + "_SELECT_LOYALITY";
			String tagLoyalityFind = "" + back_type + "__SELECT_LOYALITY_FIND";
			String tagLoyalityKind = "" + back_type + "__SELECT_LOYALITY_KIND";
			
			String l_loyality_page = Bean.getDecodeParam(parameters.get("loyality_page"));
			Bean.pageCheck(pageFormName + tagLoyality, l_loyality_page);
			String l_loyality_page_beg = Bean.getFirstRowNumber(pageFormName + tagLoyality);
			String l_loyality_page_end = Bean.getLastRowNumber(pageFormName + tagLoyality);
			
			String loyality_find 	= Bean.getDecodeParam(parameters.get("loyality_find"));
			loyality_find 	= Bean.checkFindString(pageFormName + tagLoyalityFind, loyality_find, l_loyality_page);

			String loyality_kind 	= Bean.getDecodeParam(parameters.get("loyality_kind"));
			loyality_kind 	= Bean.checkFindString(pageFormName + tagLoyalityKind, loyality_kind, l_loyality_page);

			bcListLoyalityScheme list = new bcListLoyalityScheme();
	    	
	    	String mySelectLink = "../crm/clients/loyupdate.jsp?back_type="+back_type+"&type=general&id="+external_id+"&id_jur_prs="+id_jur_prs+"&id_doc="+id_doc+"&id_club="+id_club+"&action=copy&process=no";
	    	
	    	%>
			<%= Bean.getOperationTitleShort(
					"",
					Bean.loyXML.getfieldTransl("H_LOYALITY_SELECT", false),
					"N",
					"N") 
			%>
			<table <%=Bean.getTableBottomFilter() %>><tbody>
			<tr>
				<%=Bean.getFindHTML("loyality_find", loyality_find, "../crm/clients/loyupdate.jsp?back_type="+back_type+"&type=general&id="+external_id+"&id_jur_prs="+id_jur_prs+"&id_doc="+id_doc+"&id_club="+id_club+"&action=select&process=no&loyality_page=1&", "div_data_detail") %>
			
				<%=Bean.getSelectOnChangeBeginHTML("", "loyality_kind", "../crm/clients/loyupdate.jsp?back_type="+back_type+"&type=general&id="+external_id+"&id_jur_prs="+id_jur_prs+"&id_doc="+id_doc+"&id_club="+id_club+"&action=select&process=no&loyality_page=1", Bean.clubXML.getfieldTransl("name_referral_scheme_type", false), "div_data_detail") %>
					<%= Bean.getLoyalityKindOptions(loyality_kind, true) %>
				<%=Bean.getSelectOnChangeEndHTML() %>
		
				<%= Bean.getPagesHTML(pageFormName + tagLoyality, "../crm/clients/loyupdate.jsp?back_type="+back_type+"&type=general&id="+external_id+"&id_jur_prs="+id_jur_prs+"&id_doc="+id_doc+"&id_club="+id_club+"&action=select&process=no&", "loyality_page", "", "div_data_detail") %>
	
			</tr>
			</tbody>
			</table>

	    	<%=list.getLoyalitySchemesHTMLOnlySelect(loyality_find, loyality_kind, mySelectLink, l_loyality_page_beg, l_loyality_page_end) %>
			<%

    	} else if (action.equalsIgnoreCase("edit") || action.equalsIgnoreCase("copy")) {
    		bcLoySchemeObject loy = new bcLoySchemeObject(id_loyality_scheme);
    		
    		
    	    %> 
			<% if ("LOYALITY_SCHEME".equalsIgnoreCase(back_type)) { %>
			<%= Bean.getOperationTitle(
					Bean.loyXML.getfieldTransl(action.equalsIgnoreCase("edit")?"H_LOYALITY_EDIT":"H_LOYALITY_ADD", false),
					"Y",
					"Y") 
			%>
			<% } else { %>
			<%= Bean.getOperationTitleShort(
					"",
					Bean.loyXML.getfieldTransl(action.equalsIgnoreCase("edit")?"H_LOYALITY_EDIT":"H_LOYALITY_ADD", false),
					"Y",
					"Y") 
			%>
			<% } %>
	<script>
		var formData = new Array (
			new Array ('NAME_LOYALITY_SCHEME', 'varchar2', 1),
			new Array ('name_jur_prs', 'varchar2', 1),
			new Array ('DATE_BEG', 'varchar2', 1),
			new Array ('CD_KIND_LOYALITY', 'varchar2', 1),
			new Array ('TERM_TYPE_CALC', 'varchar2', 1),
			new Array ('cd_currency', 'varchar2', 1),
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
	<form action="<%=updateLink %>" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return myValidateForm()">
	    <input type="hidden" name="type" value="general">
	    <input type="hidden" name="action" value="<%=action %>">
	    <input type="hidden" name="process" value="yes">
	    <input type="hidden" name="id_loyality_scheme" value="<%= id_loyality_scheme %>">
	    <input type="hidden" name="id_club" value="<%= loy.getValue("ID_CLUB") %>">
    	<input type="hidden" name="id" value="<%=external_id %>">
    	<input type="hidden" name="back_type" value="<%=back_type %>">
	<table <%=Bean.getTableDetailParam() %> style="border-bottom:none">
		<tr>
			<td><%= Bean.loyXML.getfieldTransl("name_loyality_scheme", true) %> </td> <td><input type="text" name="NAME_LOYALITY_SCHEME" size="70" value="<%=(action.equalsIgnoreCase("copy"))?"":loy.getValue("NAME_LOYALITY_SCHEME") %>" class="inputfield"></td>
			<td><%= Bean.clubXML.getfieldTransl("club", false) %>
					<%= Bean.getGoToClubLink(loy.getValue("ID_CLUB")) %>
			</td><td><input type="text" name="name_club" size="30" value="<%= Bean.getClubShortName(loy.getValue("ID_CLUB")) %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td rowspan="3"><%= Bean.loyXML.getfieldTransl("desc_loyality_scheme", false) %></td><td rowspan="3"><textarea name="DESC_LOYALITY_SCHEME" cols="67" rows="3" class="inputfield"><%= (action.equalsIgnoreCase("copy"))?"":loy.getValue("DESC_LOYALITY_SCHEME") %></textarea></td>
			<td><%= Bean.loyXML.getfieldTransl("date_beg", true) %></td> <td><%=Bean.getCalendarInputField("DATE_BEG", (action.equalsIgnoreCase("copy"))?"":loy.getValue("DATE_BEG_DF"), "10") %></td>
		</tr>
		<tr>
			<td valign="top"><%= Bean.loyXML.getfieldTransl("date_end", false) %></td> <td valign="top"><%=Bean.getCalendarInputField("DATE_END", (action.equalsIgnoreCase("copy"))?"":loy.getValue("DATE_END_DF"), "10") %></td>
		</tr>
		<tr>
			<td colspan="2">&nbsp;</td>
   		</tr>
	    <tr>
			<td><%= Bean.clubcardXML.getfieldTransl("sname_partner", true) %>
				<%=(back_type.equalsIgnoreCase("PARTNER"))?Bean.getGoToJurPrsHyperLink(loy.getValue("ID_JUR_PRS")):(action.equalsIgnoreCase("copy"))?"":Bean.getGoToJurPrsHyperLink(loy.getValue("ID_JUR_PRS")) %>
			</td> 
			<td>
				<%=Bean.getWindowFindJurPrs("jur_prs", (action.equalsIgnoreCase("copy")?(back_type.equalsIgnoreCase("PARTNER")?external_id:""):loy.getValue("ID_JUR_PRS")), (action.equalsIgnoreCase("copy")?"":loy.getValue("SNAME_JUR_PRS")), "ALL", "60") %>
			</td>
            <td colspan="2">&nbsp;</td>
		</tr>
   		<tr>
			<td><%= Bean.clubcardXML.getfieldTransl("id_doc", false) %>
				<%= (back_type.equalsIgnoreCase("DOC"))?Bean.getGoToDocLink(loy.getValue("ID_DOC")):(action.equalsIgnoreCase("copy"))?"":Bean.getGoToDocLink(loy.getValue("ID_DOC")) %>
			</td> 
			<td>
				<%=Bean.getWindowDocuments("doc", (back_type.equalsIgnoreCase("DOC"))?loy.getValue("ID_DOC"):(action.equalsIgnoreCase("copy"))?"":loy.getValue("ID_DOC"), "60") %>
			</td>
			<td colspan="2">&nbsp;</td>
   		</tr>
		<tr>
			<td colspan="2">&nbsp;</td>
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
			<td><%= Bean.loyXML.getfieldTransl("cd_currency", true) %></td> <td colspan="2"><select name="cd_currency"  class="inputfield"><%= Bean.getCurrencyOptions(loy.getValue("CD_CURRENCY"), true) %></select></td>
			<td colspan="2">&nbsp;</td>
		</tr>
		<tr>
			<td><%= Bean.loyXML.getfieldTransl("term_max_bonus", true) %></td> <td><input type="text" name="TERM_MAX_BONUS" size="20" value="<%= loy.getValue("TERM_MAX_BONUS_FRMT") %>" class="inputfield"> </td>
			<td><%= Bean.loyXML.getfieldTransl("term_cash_bon", true) %></td> <td><select name="TERM_CASH_BON"  class="inputfield"><%= Bean.getMeaningFromLookupNumHTML("YES_NO", loy.getValue("TERM_CASH_BON"), true) %></select></td>
		</tr>
		<tr>
			<td><%= Bean.loyXML.getfieldTransl("term_min_amount", true) %></td> <td><input type="text" name="TERM_MIN_AMOUNT" size="20" value="<%= loy.getValue("TERM_MIN_AMOUNT_FRMT") %>" class="inputfield"> </td>
			<td><%= Bean.loyXML.getfieldTransl("term_bon_bon", true) %></td><td><select name="TERM_BON_BON"  class="inputfield"><%= Bean.getMeaningFromLookupNumHTML("YES_NO", loy.getValue("TERM_BON_BON"), true) %></select></td>
		</tr>
		<tr>
			<td><%= Bean.loyXML.getfieldTransl("term_max_sumpayoffline", true) %></td> <td><input type="text" name="TERM_MAX_SUMPAYOFFLINE" size="20" value="<%= loy.getValue("TERM_MAX_SUMPAYOFFLINE_FRMT") %>" class="inputfield"></td>
			<td><%= Bean.loyXML.getfieldTransl("term_loyality_for_all_nsmep", true) %></td><td><select name="TERM_LOYALITY_FOR_ALL_NSMEP"  class="inputfield"><%= Bean.getMeaningFromLookupNumHTML("YES_NO", loy.getValue("TERM_LOYALITY_FOR_ALL_NSMEP"), true) %></select></td>			
		</tr>
		<tr>			
			<td><%= Bean.loyXML.getfieldTransl("term_max_sumpaynopin", true) %></td> <td><input type="text" name="TERM_MAX_SUMPAYNOPIN" size="20" value="<%= loy.getValue("TERM_MAX_SUMPAYNOPIN_FRMT") %>" class="inputfield"> </td>
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
				<%=Bean.getSubmitButtonAjax(updateLink, "submit", "updateForm", ("LOYALITY_SCHEME".equalsIgnoreCase(back_type))?"div_main":"div_data_detail") %>
				<%=Bean.getResetButton() %>
				<%=Bean.getGoBackButton(backLink) %>
			</td>
		</tr>
	</table>
	</form> 
	<!-- Скрипт для втавки меню вибору дати -->
		<%= Bean.getCalendarScript("DATE_BEG", false) %>
		<%= Bean.getCalendarScript("DATE_END", false) %>

<br><%

    	} else {
    	    %> <%= Bean.getUnknownActionText(action) %><%
    	}
	}
	else if (process.equalsIgnoreCase("yes")) {    

	  	String
	    	NAME_LOYALITY_SCHEME 	= Bean.getDecodeParam(parameters.get("NAME_LOYALITY_SCHEME")), 
	    	DATE_BEG 				= Bean.getDecodeParam(parameters.get("DATE_BEG")), 
	    	DESC_LOYALITY_SCHEME 	= Bean.getDecodeParam(parameters.get("DESC_LOYALITY_SCHEME")), 
	    	DATE_END 				= Bean.getDecodeParam(parameters.get("DATE_END")),
	    	CD_KIND_LOYALITY 		= Bean.getDecodeParam(parameters.get("CD_KIND_LOYALITY")), 
	    	TERM_TYPE_CALC 			= Bean.getDecodeParam(parameters.get("TERM_TYPE_CALC")), 
	    	TERM_MAX_BONUS 			= Bean.getDecodeParam(parameters.get("TERM_MAX_BONUS")), 
	    	TERM_MAX_SUMPAYOFFLINE 	= Bean.getDecodeParam(parameters.get("TERM_MAX_SUMPAYOFFLINE")), 
	    	TERM_ROUNDING_RULE 		= Bean.getDecodeParam(parameters.get("TERM_ROUNDING_RULE")), 
	    	TERM_MIN_AMOUNT 		= Bean.getDecodeParam(parameters.get("TERM_MIN_AMOUNT")),
	    	TERM_MAX_SUMPAYNOPIN 	= Bean.getDecodeParam(parameters.get("TERM_MAX_SUMPAYNOPIN")),
	    	TERM_MAX_DATE_ONL_TERM 	= Bean.getDecodeParam(parameters.get("TERM_MAX_DATE_ONL_TERM")),
	    	TERM_CASH_BON 			= Bean.getDecodeParam(parameters.get("TERM_CASH_BON")),
	    	TERM_BON_BON 			= Bean.getDecodeParam(parameters.get("TERM_BON_BON")),
	    	TERM_EXT_LOYL 			= Bean.getDecodeParam(parameters.get("TERM_EXT_LOYL")),
	    	TERM_NOMENKL 			= Bean.getDecodeParam(parameters.get("TERM_NOMENKL")),
	    	cd_currency 			= Bean.getDecodeParam(parameters.get("cd_currency")),
	    	term_loyality_for_all_nsmep	= Bean.getDecodeParam(parameters.get("TERM_LOYALITY_FOR_ALL_NSMEP")),
	    	term_limit_cash 			= Bean.getDecodeParam(parameters.get("TERM_LIMIT_CASH"));
	    
		if (action.equalsIgnoreCase("add")) { 
			
			ArrayList<String> pParam = new ArrayList<String>();
					
			pParam.add(NAME_LOYALITY_SCHEME);
			pParam.add(DESC_LOYALITY_SCHEME);
			pParam.add(CD_KIND_LOYALITY);
			pParam.add(DATE_BEG);
			pParam.add(DATE_END);
			pParam.add(TERM_MAX_BONUS);
			pParam.add(TERM_MIN_AMOUNT);
			pParam.add(TERM_MAX_SUMPAYOFFLINE);
			pParam.add(TERM_MAX_SUMPAYNOPIN);
			pParam.add(TERM_MAX_DATE_ONL_TERM);
			pParam.add(TERM_TYPE_CALC);
			pParam.add(TERM_ROUNDING_RULE);
			pParam.add(TERM_CASH_BON);
			pParam.add(TERM_BON_BON);
			pParam.add(TERM_EXT_LOYL);
			pParam.add(term_loyality_for_all_nsmep);
			pParam.add(term_limit_cash);
			pParam.add(TERM_NOMENKL);
			pParam.add(cd_currency);
			pParam.add(id_club);
			pParam.add(id_jur_prs);
			pParam.add(id_doc);
			pParam.add(Bean.getDateFormat());
	
		 	%>
			<%= Bean.executeInsertFunction("PACK$LOY_UI.add_loyality_scheme", pParam, backLink + "&id_loyality_scheme=" , "") %>
			<% 	

		} else if (action.equalsIgnoreCase("copy")) { 
			ArrayList<String> pParam = new ArrayList<String>();
			
			pParam.add(id_loyality_scheme);
			pParam.add(NAME_LOYALITY_SCHEME);
			pParam.add(DESC_LOYALITY_SCHEME);
			pParam.add(CD_KIND_LOYALITY);
			pParam.add(DATE_BEG);
			pParam.add(DATE_END);
			pParam.add(TERM_MAX_BONUS);
			pParam.add(TERM_MIN_AMOUNT);
			pParam.add(TERM_MAX_SUMPAYOFFLINE);
			pParam.add(TERM_MAX_SUMPAYNOPIN);
			pParam.add(TERM_MAX_DATE_ONL_TERM);
			pParam.add(TERM_TYPE_CALC);
			pParam.add(TERM_ROUNDING_RULE);
			pParam.add(TERM_CASH_BON);
			pParam.add(TERM_BON_BON);
			pParam.add(TERM_EXT_LOYL);
			pParam.add(term_loyality_for_all_nsmep);
			pParam.add(term_limit_cash);
			pParam.add(TERM_NOMENKL);
			pParam.add(cd_currency);
			pParam.add(id_club);
			pParam.add(id_jur_prs);
			pParam.add(id_doc);
			pParam.add(Bean.getDateFormat());

		 	%>
			<%= Bean.executeInsertFunction("PACK$LOY_UI.copy_loyality_scheme", pParam, backLink + "&id_loyality_scheme=" , "") %>
			<% 	

		} else if (action.equalsIgnoreCase("remove")) {
			ArrayList<String> pParam = new ArrayList<String>();
				
			pParam.add(id_loyality_scheme);

		 	%>
			<%= Bean.executeDeleteFunction("PACK$LOY_UI.delete_loyality_scheme", pParam, generalLink , "") %>
			<% 	

		} else if (action.equalsIgnoreCase("edit")) {
			ArrayList<String> pParam = new ArrayList<String>();
							
			pParam.add(id_loyality_scheme);
			pParam.add(NAME_LOYALITY_SCHEME);
			pParam.add(DESC_LOYALITY_SCHEME);
			pParam.add(CD_KIND_LOYALITY);
			pParam.add(DATE_BEG);
			pParam.add(DATE_END);
			pParam.add(TERM_MAX_BONUS);
			pParam.add(TERM_MIN_AMOUNT);
			pParam.add(TERM_MAX_SUMPAYOFFLINE);
			pParam.add(TERM_MAX_SUMPAYNOPIN);
			pParam.add(TERM_MAX_DATE_ONL_TERM);
			pParam.add(TERM_TYPE_CALC);
			pParam.add(TERM_ROUNDING_RULE);
			pParam.add(TERM_CASH_BON);
			pParam.add(TERM_BON_BON);
			pParam.add(TERM_EXT_LOYL);
			pParam.add(term_loyality_for_all_nsmep);
			pParam.add(term_limit_cash);
			pParam.add(TERM_NOMENKL);
			pParam.add(cd_currency);
			pParam.add(id_jur_prs);
			pParam.add(id_doc);
			pParam.add(Bean.getDateFormat());

		 	%>
			<%= Bean.executeUpdateFunction("PACK$LOY_UI.update_loyality_scheme", pParam, backLink, "") %>
			<% 	

		} else {%> 
			<%= Bean.getUnknownActionText(action) %><% 
		}
	} else {%> 
		<%= Bean.getUnknownProcessText(process) %> <%
  	}
} else {
    %> <%= Bean.getUnknownTypeText(type) %> <%
}
  %>

</body>
</html>
