<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%@page import="bc.objects.bcLoySchemeObject"%>
<%@page import="bc.objects.bcComissionObject"%>
<%@page import="bc.objects.bcClubRelationshipObject"%>
<%@page import="java.util.HashMap"%>
<%@page import="bc.objects.bcClubShortObject"%>

<%@page import="java.util.ArrayList"%>
<%@page import="bc.objects.bcReferralSchemeObject"%>
<%@page import="bc.objects.bcReferralSchemeLineObject"%>
<%@page import="bc.lists.bcListReferralScheme"%>
<%@page import="bc.service.bcFeautureParam"%><html>

<%= Bean.getLogOutScript(request) %>

<head>
	<%= Bean.getMetaContent() %>
	<%= Bean.getBottomFrameCSS() %>
</head>

<body>

<%

HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String pageFormName = "CRM_CLUB_REFERRAL_SCHEME";

Bean.setJspPageForTabName(pageFormName);

request.setCharacterEncoding("UTF-8");

String external_id			= Bean.getDecodeParamPrepare(parameters.get("id"));
String id_referral_scheme	= Bean.getDecodeParamPrepare(parameters.get("id_referral_scheme")); 
String type					= Bean.getDecodeParamPrepare(parameters.get("type")); 
String action				= Bean.getDecodeParamPrepare(parameters.get("action")); 
String process				= Bean.getDecodeParamPrepare(parameters.get("process"));
String back_type			= Bean.getDecodeParamPrepare(parameters.get("back_type"));
String id_jur_prs			= Bean.getDecodeParamPrepare(parameters.get("id_jur_prs"));
String id_target_prg		= Bean.getDecodeParamPrepare(parameters.get("id_target_prg"));
String id_doc				= Bean.getDecodeParamPrepare(parameters.get("id_doc"));

String updateLink = "../crm/club/referral_schemeupdate.jsp";
String backLink = "";
String generalLink = "";
back_type = Bean.isEmpty(back_type)?"REFERRAL_SCHEME":back_type;
System.out.println("back_type="+back_type);
if ("REFERRAL_SCHEME".equalsIgnoreCase(back_type)) {
	generalLink = "../crm/club/referral_scheme.jsp?";
	if (action.equalsIgnoreCase("add")) {
		backLink = "../crm/club/referral_scheme.jsp?";
	} else {
		backLink = "../crm/club/referral_schemespecs.jsp?id="+id_referral_scheme;
	}
} else if ("DOC".equalsIgnoreCase(back_type)) {
	generalLink = "../crm/club/documentspecs.jsp?id="+external_id;
	backLink = "../crm/club/documentspecs.jsp?id="+external_id;
} else if ("PARTNER".equalsIgnoreCase(back_type)) {
	generalLink = "../crm/clients/yurpersonspecs.jsp?id="+external_id;
	backLink = "../crm/clients/yurpersonspecs.jsp?id="+external_id;
} else if ("TARGET_PROGRAM".equalsIgnoreCase(back_type)) {
	generalLink = "../crm/club/target_programspecs.jsp?id="+external_id;
	backLink = "../crm/club/target_programspecs.jsp?id="+external_id;
}

if (type.equalsIgnoreCase("general")) {
	if (process.equalsIgnoreCase("no")) {
		
		String cd_referral_scheme_type 	= Bean.getDecodeParam(parameters.get("cd_referral_scheme_type"));
		String action_prev 	= Bean.getDecodeParam(parameters.get("action_prev"));
		
		if (action.equalsIgnoreCase("add") || action.equalsIgnoreCase("add2")) {
			
	        %> 
			<% if ("REFERRAL_SCHEME".equalsIgnoreCase(back_type)) { %>
			<%= Bean.getOperationTitle(
					Bean.clubXML.getfieldTransl("H_ADD_REFERRAL_SCHEME", false),
					"Y",
					"Y") 
			%>
			<% } else { %>
			<%= Bean.getOperationTitleShort(
					"",
					Bean.clubXML.getfieldTransl("H_ADD_REFERRAL_SCHEME", false),
					"Y",
					"Y") 
			%>
			<% } %>
		<script>
			var formData = new Array (
				new Array ('cd_referral_scheme_type', 'varchar2', 1)
			);
			function myValidateForm() {
				return validateForm(formData);
			}
		</script>

		<form action="../crm/club/referral_schemeupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">  	        
	        <input type="hidden" name="type" value="general">
	        <input type="hidden" name="action" value="add_det">
	        <input type="hidden" name="process" value="no">
	        <input type="hidden" name="id_referral_scheme" value="<%=id_referral_scheme %>">
	        <input type="hidden" name="action_prev" value="<%=action %>">
    		<input type="hidden" name="id" value="<%=external_id %>">
    		<input type="hidden" name="back_type" value="<%=back_type %>">
		<table <%=Bean.getTableDetailParam() %>>
		<tr>
			<td colspan="4">&nbsp;</td>
        </tr>
		<tr>
			<td><%= Bean.clubXML.getfieldTransl("cd_referral_scheme_type", true) %></td> <td><%= Bean.getReferralShemeTypeRadio("cd_referral_scheme_type", cd_referral_scheme_type) %></td>
			<td colspan="2">&nbsp;</td>
        </tr>
		<tr>
			<td colspan="4">&nbsp;</td>
        </tr>
	 		<tr>
				<td colspan="6" align="center">
					<%=Bean.getSubmitButtonAjax(updateLink, "submit", "updateForm", ("REFERRAL_SCHEME".equalsIgnoreCase(back_type))?"div_main":"div_data_detail") %>
					<%=Bean.getResetButton() %>
					<%=Bean.getGoBackButton(backLink) %>
				</td>
			</tr>
		</table>
		</form>

		<% 	
	   	} else if (action.equalsIgnoreCase("add_det")) {
    		
			bcClubShortObject club = new bcClubShortObject(Bean.getCurrentClubID());
			
	        %> 
			<% if ("REFERRAL_SCHEME".equalsIgnoreCase(back_type)) { %>
			<%= Bean.getOperationTitle(
					Bean.clubXML.getfieldTransl("H_ADD_REFERRAL_SCHEME", false),
					"Y",
					"Y") 
			%>
			<% } else { %>
			<%= Bean.getOperationTitleShort(
					"",
					Bean.clubXML.getfieldTransl("H_ADD_REFERRAL_SCHEME", false),
					"Y",
					"Y") 
			%>
			<% } %>
		<script>
			var formData = new Array (
				<% if ("PAYMENT".equalsIgnoreCase(cd_referral_scheme_type)) { %>
				new Array ('name_jur_prs', 'varchar2', 1),
				<% } else if ("TARGET_PROGRAM".equalsIgnoreCase(cd_referral_scheme_type)) { %>
				new Array ('name_target_prg', 'varchar2', 1),
				<% } %>
				new Array ('cd_referral_scheme_type', 'varchar2', 1),
				new Array ('name_referral_scheme', 'varchar2', 1),
				new Array ('cd_referral_scheme_calc_type', 'varchar2', 1),
				new Array ('name_club', 'varchar2', 1),
				new Array ('date_beg', 'varchar2', 1)
			);
			function myValidateForm() {
				return validateForm(formData);
			}
		</script>

		<form action="../crm/club/referral_schemeupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">  	        
	        <input type="hidden" name="type" value="general">
	        <input type="hidden" name="action" value="add">
	        <input type="hidden" name="process" value="yes">
	        <input type="hidden" name="cd_referral_scheme_type" value="<%=cd_referral_scheme_type %>">
    		<input type="hidden" name="id" value="<%=external_id %>">
    		<input type="hidden" name="back_type" value="<%=back_type %>">
		<table <%=Bean.getTableDetailParam() %>>
		<tr>
			<td><%= Bean.clubXML.getfieldTransl("name_referral_scheme_type", false) %></td> <td><input type="text" name="name_referral_scheme_type" size="40" value="<%=Bean.getReferralShemeTypeName(cd_referral_scheme_type) %>" readonly="readonly" class="inputfield-ro"></td>
 		    <td><%= Bean.clubXML.getfieldTransl("club", true) %>
				<%= Bean.getGoToClubLink(club.getValue("ID_CLUB")) %>
			</td>
		  	<td>
				<%=Bean.getWindowFindClub("club", club.getValue("ID_CLUB"), club.getValue("SNAME_CLUB"), "25") %>
		  	</td>
		</tr>
   		<tr>
			<% if ("PAYMENT".equalsIgnoreCase(cd_referral_scheme_type)) { %>
			<td><%= Bean.clubcardXML.getfieldTransl("sname_partner", true) %></td> <td><%=Bean.getWindowFindJurPrs("jur_prs", "", "", "ALL", "60") %></td>
			<% } else if ("TARGET_PROGRAM".equalsIgnoreCase(cd_referral_scheme_type)) { %>
			<td><%= Bean.clubXML.getfieldTransl("name_target_prg", true) %></td> <td><%=Bean.getWindowFindTargetPrg("target_prg", "", "", "60") %></td>
			<% } %>
            <td><%= Bean.clubXML.getfieldTransl("date_beg", true) %></td><td><%=Bean.getCalendarInputField("date_beg", Bean.getSysDate(), "10") %></td>
   		</tr>
   		<tr>
			<td><%= Bean.clubcardXML.getfieldTransl("id_doc", false) %></td> 
			<td>
				<%=Bean.getWindowDocuments("doc", "", "60") %>
			</td>
            <td><%= Bean.clubXML.getfieldTransl("date_end", false) %></td><td><%=Bean.getCalendarInputField("date_end", "", "10") %></td>
   		</tr>
	    <tr>
			<td><%= Bean.clubXML.getfieldTransl("cd_referral_scheme_calc_type", true) %></td> <td><select name="cd_referral_scheme_calc_type" class="inputfield"><%= Bean.getReferralShemeCalcTypeOptions("", true) %></select></td>
			<td colspan="2">&nbsp;</td>
		</tr>
	    <tr>
			<td><%= Bean.clubXML.getfieldTransl("name_referral_scheme", true) %></td> <td><input type="text" name="name_referral_scheme" size="70" value="" class="inputfield"></td>
			<td colspan="2">&nbsp;</td>
		</tr>
	    <tr>
			<td><%= Bean.clubXML.getfieldTransl("desc_referral_scheme", false) %></td> <td colspan="3"><textarea name="desc_referral_scheme" cols="67" rows="3" class="inputfield"></textarea></td>
		</tr>
	 		<tr>
				<td colspan="6" align="center">
					<%=Bean.getSubmitButtonAjax(updateLink) %>
					<%=Bean.getResetButton() %>
					<%=Bean.getGoBackButton("../crm/club/referral_schemeupdate.jsp?type=general&action=" + action_prev + "&process=no&id=" + external_id + "&back_type=" + back_type + "&id_referral_scheme=" + id_referral_scheme + "&cd_referral_scheme_type=" + cd_referral_scheme_type, "cancel", ("REFERRAL_SCHEME".equalsIgnoreCase(back_type))?"div_main":"div_data_detail") %>
				</td>
			</tr>
		</table>
		</form>

		<% 	
		} else if (action.equalsIgnoreCase("select")) {
			String tagReferralScheme = "" + back_type + "_SELECT_REFERRAL_SCHEME";
			String tagReferralSchemeFind = "" + back_type + "__SELECT_REFERRAL_SCHEME_FIND";
			String tagReferralSchemeType = "" + back_type + "__SELECT_REFERRAL_SCHEME_TYPE";
			String tagReferralSchemeCalcType = "" + back_type + "_SELECT_REFERRAL_SCHEME_CALC_TYPE";
			
			String l_referral_scheme_page = Bean.getDecodeParam(parameters.get("referral_scheme_page"));
			Bean.pageCheck(pageFormName + tagReferralScheme, l_referral_scheme_page);
			String l_referral_scheme_page_beg = Bean.getFirstRowNumber(pageFormName + tagReferralScheme);
			String l_referral_scheme_page_end = Bean.getLastRowNumber(pageFormName + tagReferralScheme);
			
			String referral_scheme_find 	= Bean.getDecodeParam(parameters.get("referral_scheme_find"));
			referral_scheme_find 	= Bean.checkFindString(pageFormName + tagReferralSchemeFind, referral_scheme_find, l_referral_scheme_page);

			String referral_scheme_type 	= Bean.getDecodeParam(parameters.get("referral_scheme_type"));
			referral_scheme_type 	= Bean.checkFindString(pageFormName + tagReferralSchemeType, referral_scheme_type, l_referral_scheme_page);

			String referral_scheme_calc_type 	= Bean.getDecodeParam(parameters.get("referral_scheme_calc_type"));
			referral_scheme_calc_type 	= Bean.checkFindString(pageFormName + tagReferralSchemeCalcType, referral_scheme_calc_type, l_referral_scheme_page);
			
			bcListReferralScheme list = new bcListReferralScheme();
	    	
	    	String mySelectLink = "../crm/club/referral_schemeupdate.jsp?back_type="+back_type+"&type=general&id="+external_id+"&id_jur_prs="+id_jur_prs+"&id_doc="+id_doc+"&id_target_prg="+id_target_prg+"&action=copy&process=no";
	    	
	    	%>
			<%= Bean.getOperationTitleShort(
					"",
					Bean.clubXML.getfieldTransl("h_select_referral_scheme", false),
					"N",
					"N") 
			%>
			<table <%=Bean.getTableBottomFilter() %>><tbody>
			<tr>
				<%=Bean.getFindHTML("referral_scheme_find", referral_scheme_find, "../crm/club/referral_schemeupdate.jsp?back_type="+back_type+"&type=general&id="+external_id+"&action=select&process=no&referral_scheme_page=1&", "div_data_detail") %>
			
				<%=Bean.getSelectOnChangeBeginHTML("", "referral_scheme_type", "../crm/club/referral_schemeupdate.jsp?back_type="+back_type+"&type=general&id="+external_id+"&action=select&process=no&referral_scheme_page=1", Bean.clubXML.getfieldTransl("name_referral_scheme_type", false), "div_data_detail") %>
					<%= Bean.getReferralShemeTypeOptions(referral_scheme_type, true) %>
				<%=Bean.getSelectOnChangeEndHTML() %>
		
				<%=Bean.getSelectOnChangeBeginHTML("", "referral_scheme_calc_type", "../crm/club/referral_schemeupdate.jsp?back_type="+back_type+"&type=general&id="+external_id+"&action=select&process=no&referral_scheme_page=1", Bean.clubXML.getfieldTransl("name_referral_scheme_calc_type", false), "div_data_detail") %>
					<%= Bean.getReferralShemeCalcTypeOptions(referral_scheme_calc_type, true) %>
				<%=Bean.getSelectOnChangeEndHTML() %>

				<%= Bean.getPagesHTML(pageFormName + tagReferralScheme, "../crm/club/referral_schemeupdate.jsp?back_type="+back_type+"&type=general&id="+external_id+"&action=select&process=no&", "referral_scheme_page", "", "div_data_detail") %>
	
			</tr>
			</tbody>
			</table>

	    	<%=list.getReferralSchemesHTMLOnlySelect(referral_scheme_find, referral_scheme_type, referral_scheme_calc_type, mySelectLink, l_referral_scheme_page_beg, l_referral_scheme_page_end) %>
			<%
	   	} else if (action.equalsIgnoreCase("edit") || action.equalsIgnoreCase("copy")) { 
	   		bcReferralSchemeObject scheme = new bcReferralSchemeObject(id_referral_scheme);
	   	%>

			<%= Bean.getOperationTitleShort(
					"",
					Bean.clubXML.getfieldTransl(action.equalsIgnoreCase("edit")?"H_UPDATE_REFERRAL_SCHEME":"H_ADD_REFERRAL_SCHEME", false),
					"Y",
					"Y") 
			%>
		<script>
			var formData = new Array (
				<% if ("PAYMENT".equalsIgnoreCase(scheme.getValue("CD_REFERRAL_SCHEME_TYPE"))) { %>
				new Array ('name_jur_prs', 'varchar2', 1),
				<% } else { %>
				new Array ('name_target_prg', 'varchar2', 1),
				<% } %>
				new Array ('name_referral_scheme', 'varchar2', 1),
				new Array ('date_beg', 'varchar2', 1)
			);
			function myValidateForm() {
				return validateForm(formData);
			}
		</script>
	<div id="div_detail">
    <form action="../crm/club/referral_schemeupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST">
    	<input type="hidden" name="action" value="<%=action %>">
    	<input type="hidden" name="process" value="yes">
    	<input type="hidden" name="type" value="general">
    	<input type="hidden" name="id_referral_scheme" value="<%=scheme.getValue("ID_REFERRAL_SCHEME") %>">
    	<input type="hidden" name="LUD" value="<%=scheme.getValue("LUD") %>">
    	<input type="hidden" name="id" value="<%=external_id %>">
    	<input type="hidden" name="id_club" value="<%=scheme.getValue("ID_CLUB") %>">
    	<input type="hidden" name="back_type" value="<%=back_type %>">
	<table <%=Bean.getTableDetailParam() %>>
	    <tr>
			<td><%= Bean.clubXML.getfieldTransl("name_referral_scheme_type", false) %></td> <td><input type="text" name="name_referral_scheme_type" size="70" value="<%=scheme.getValue("NAME_REFERRAL_SCHEME_TYPE") %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.clubXML.getfieldTransl("club", false) %>
				<%= Bean.getGoToClubLink(scheme.getValue("ID_CLUB")) %>
			</td><td><input type="text" name="name_club" size="30" value="<%= Bean.getClubShortName(scheme.getValue("ID_CLUB")) %>" readonly="readonly" class="inputfield-ro"></td>
   		</tr>
	    <tr>
			<% if ("PAYMENT".equalsIgnoreCase(scheme.getValue("CD_REFERRAL_SCHEME_TYPE"))) { %>
			<td><%= Bean.clubcardXML.getfieldTransl("sname_partner", true) %>
				<%=(back_type.equalsIgnoreCase("PARTNER"))?Bean.getGoToJurPrsHyperLink(scheme.getValue("ID_JUR_PRS")):(action.equalsIgnoreCase("copy"))?"":Bean.getGoToJurPrsHyperLink(scheme.getValue("ID_JUR_PRS")) %>
			</td> 
			<td>
				<%=Bean.getWindowFindJurPrs("jur_prs", (action.equalsIgnoreCase("copy")?(back_type.equalsIgnoreCase("PARTNER")?external_id:""):scheme.getValue("ID_JUR_PRS")), (action.equalsIgnoreCase("copy")?"":scheme.getValue("SNAME_JUR_PRS")), "ALL", "60") %>
			</td>
			<% } else { %>
			<td><%= Bean.clubXML.getfieldTransl("target_prg", true) %>
				<%= Bean.getGoToTargetProgramLink(scheme.getValue("ID_TARGET_PRG")) %>
			</td>
			<td>
				<%=Bean.getWindowFindTargetPrg("target_prg", scheme.getValue("ID_TARGET_PRG"), scheme.getValue("NAME_TARGET_PRG"), "60") %>
			</td>
			<% } %>
            <td><%= Bean.clubXML.getfieldTransl("date_beg", true) %></td><td><%=Bean.getCalendarInputField("date_beg", (action.equalsIgnoreCase("copy"))?"":scheme.getValue("DATE_BEG_DF"), "10") %></td>
		</tr>
   		<tr>
			<td><%= Bean.clubXML.getfieldTransl("name_referral_scheme", true) %></td> <td><input type="text" name="name_referral_scheme" size="70" value="<%=(action.equalsIgnoreCase("copy"))?"":scheme.getValue("NAME_REFERRAL_SCHEME") %>" class="inputfield"></td>
            <td><%= Bean.clubXML.getfieldTransl("date_end", false) %></td><td><%=Bean.getCalendarInputField("date_end", (action.equalsIgnoreCase("copy"))?"":scheme.getValue("DATE_END_DF"), "10") %></td>
   		</tr>
	    <tr>
			<td rowspan="3"><%= Bean.clubXML.getfieldTransl("desc_referral_scheme", false) %></td> <td rowspan="3"><textarea name="desc_referral_scheme" cols="67" rows="3" class="inputfield"><%= (action.equalsIgnoreCase("copy"))?"":scheme.getValue("DESC_REFERRAL_SCHEME") %></textarea></td>
			<td><%= Bean.clubXML.getfieldTransl("accounting_level_count", false) %></td> <td><input type="text" name="accounting_level_count" size="10" value="<%=scheme.getValue("ACCOUNTING_LEVEL_COUNT") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
	    <tr>
			<td><%= Bean.clubXML.getfieldTransl("accounting_percent_all", false) %></td> <td><input type="text" name="accounting_percent_all" size="10" value="<%=scheme.getValue("ACCOUNTING_PERCENT_ALL_FRMT") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
	    <tr>
			<td colspan="2">&nbsp;</td>
		</tr>
	    <tr>
			<td><%= Bean.clubXML.getfieldTransl("name_referral_scheme_calc_type", false) %></td> <td><input type="text" name="name_referral_scheme_calc_type" size="70" value="<%=scheme.getValue("NAME_REFERRAL_SCHEME_CALC_TYPE") %>" readonly="readonly" class="inputfield-ro"></td>
			<td colspan="2">&nbsp;</td>
		</tr>
   		<tr>
			<td><%= Bean.clubcardXML.getfieldTransl("id_doc", false) %>
				<%= (back_type.equalsIgnoreCase("DOC"))?Bean.getGoToDocLink(scheme.getValue("ID_DOC")):(action.equalsIgnoreCase("copy"))?"":Bean.getGoToDocLink(scheme.getValue("ID_DOC")) %>
			</td> 
			<td>
				<%=Bean.getWindowDocuments("doc", (back_type.equalsIgnoreCase("DOC"))?scheme.getValue("ID_DOC"):(action.equalsIgnoreCase("copy"))?"":scheme.getValue("ID_DOC"), "60") %>
			</td>
    		</tr>
		<%=	Bean.getIdCreationAndMoficationRecordFields(
				scheme.getValue("ID_REFERRAL_SCHEME"),
				scheme.getValue(Bean.getCreationDateFieldName()),
				scheme.getValue("CREATED_BY"),
				scheme.getValue(Bean.getLastUpdateDateFieldName()),
				scheme.getValue("LAST_UPDATE_BY")
			) %>
		<tr>
			<td colspan="6" align="center">
				<%=Bean.getSubmitButtonAjax(updateLink, "submit", "updateForm", "div_detail") %>
				<%=Bean.getResetButton() %>
				<%=Bean.getGoBackButton(backLink) %>
			</td>
		</tr>
	</table>

	</form>
	</div>
		<%= Bean.getCalendarScript("date_beg", false) %>
		<%= Bean.getCalendarScript("date_end", false) %>
	   	<% } else {
   	    %> <%= Bean.getUnknownActionText(action) %><%
   		}
	} else if (process.equalsIgnoreCase("yes")) {    

	  	String
	  		cd_referral_scheme_type 		= Bean.getDecodeParam(parameters.get("cd_referral_scheme_type")), 
	  		cd_referral_scheme_calc_type 	= Bean.getDecodeParam(parameters.get("cd_referral_scheme_calc_type")), 
	  		name_referral_scheme 			= Bean.getDecodeParam(parameters.get("name_referral_scheme")), 
	  		desc_referral_scheme 			= Bean.getDecodeParam(parameters.get("desc_referral_scheme")), 
	  		id_club 						= Bean.getDecodeParam(parameters.get("id_club")), 
	  		date_beg 						= Bean.getDecodeParam(parameters.get("date_beg")), 
	  		date_end 						= Bean.getDecodeParam(parameters.get("date_end")), 
	  		LUD		 						= Bean.getDecodeParam(parameters.get("LUD"));
	    
		if (action.equalsIgnoreCase("add")) { 
				
			ArrayList<String> pParam = new ArrayList<String>();	

			pParam.add(cd_referral_scheme_type);
			pParam.add(id_jur_prs);
			pParam.add(id_target_prg);
			pParam.add(name_referral_scheme);
			pParam.add(desc_referral_scheme);
			pParam.add(cd_referral_scheme_calc_type);
			pParam.add(id_club);
			pParam.add(date_beg);
			pParam.add(date_end);
			pParam.add(id_doc);
			pParam.add(Bean.getDateFormat());
				
		 	%>
			<%= Bean.executeInsertFunction("PACK$REFERRAL_UI.add_referral_scheme", pParam, backLink + "&id_referral_scheme=", backLink) %>
			<% 	
	
		} else if (action.equalsIgnoreCase("remove")) {
			ArrayList<String> pParam = new ArrayList<String>();	
					
			pParam.add(id_referral_scheme);
			
		 	%>
			<%= Bean.executeDeleteFunction("PACK$REFERRAL_UI.delete_referral_scheme", pParam, generalLink , "") %>
			<% 	
	
		} else if (action.equalsIgnoreCase("edit")) {
			ArrayList<String> pParam = new ArrayList<String>();	
				
			pParam.add(id_referral_scheme);
			pParam.add(id_jur_prs);
			pParam.add(id_target_prg);
			pParam.add(name_referral_scheme);
			pParam.add(desc_referral_scheme);
			pParam.add(date_beg);
			pParam.add(date_end);
			pParam.add(id_doc);
			pParam.add(LUD);
			pParam.add(Bean.getDateFormat());
	
		 	%>
			<%= Bean.executeUpdateFunction("PACK$REFERRAL_UI.update_referral_scheme", pParam, backLink, "") %>
			<% 	
	
		} else if (action.equalsIgnoreCase("copy")) {
			ArrayList<String> pParam = new ArrayList<String>();	
				
			pParam.add(id_referral_scheme);
			pParam.add(id_jur_prs);
			pParam.add(id_target_prg);
			pParam.add(name_referral_scheme);
			pParam.add(desc_referral_scheme);
			pParam.add(id_club);
			pParam.add(date_beg);
			pParam.add(date_end);
			pParam.add(id_doc);
			pParam.add(Bean.getDateFormat());
			pParam.add(LUD);
	
		 	%>
			<%= Bean.executeInsertFunction("PACK$REFERRAL_UI.copy_referral_scheme", pParam, backLink + "&id_referral_scheme=", backLink) %>
			<% 	
	
		} else {%> 
			<%= Bean.getUnknownActionText(action) %><% 
		}
	} else {%> 
		<%= Bean.getUnknownProcessText(process) %><% 
	}
	
	
} else if (type.equalsIgnoreCase("line")) {
	
	String 
		id_referral_scheme_line 		= Bean.getDecodeParam(parameters.get("id_referral_scheme_line")),
		accounting_level				= Bean.getDecodeParam(parameters.get("accounting_level")),
		accounting_percent 				= Bean.getDecodeParam(parameters.get("accounting_percent")),
		card_serial_number 				= Bean.getDecodeParam(parameters.get("card_serial_number")),
		card_id_issuer 					= Bean.getDecodeParam(parameters.get("id_issuer")),
		card_id_payment_system 			= Bean.getDecodeParam(parameters.get("id_payment_system")),
		cd_referral_scheme_rec_type 	= Bean.getDecodeParam(parameters.get("cd_referral_scheme_rec_type")),
		LUD							 	= Bean.getDecodeParam(parameters.get("LUD"));

	if (process.equalsIgnoreCase("no")) {
		if (action.equalsIgnoreCase("add")) {
			
			bcReferralSchemeObject scheme = new bcReferralSchemeObject(id_referral_scheme);
			
	        %> 
	
			<script>
				var formData = new Array (
					new Array ('accounting_level', 'varchar2', 1),
					new Array ('accounting_percent', 'varchar2', 1),
					new Array ('cd_referral_scheme_rec_type', 'varchar2', 1)
				);
			</script>

		<%= Bean.getOperationTitleShort(
				"",
				Bean.clubXML.getfieldTransl("h_add_referral_scheme_line", false),
				"Y",
				"N") 
		%>
    <form action="../crm/club/referral_schemeupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">
        <input type="hidden" name="type" value="line">
        <input type="hidden" name="action" value="add">
        <input type="hidden" name="process" value="yes">
        <input type="hidden" name="id_referral_scheme" value="<%=id_referral_scheme %>">
	<table <%=Bean.getTableDetailParam() %>>
	    <tr>
			<td><%= Bean.clubXML.getfieldTransl("name_referral_scheme", false) %></td> <td><input type="text" name="name_referral_scheme" size="55" value="<%=scheme.getValue("NAME_REFERRAL_SCHEME") %>" readonly="readonly" class="inputfield-ro"> </td>
			<td colspan="2">&nbsp;</td>
		</tr>
	    <tr>
			<td><%= Bean.clubXML.getfieldTransl("accounting_level", true) %></td> <td><input type="text" name="accounting_level" size="10" value="" class="inputfield"></td>
		</tr>
	    <tr>
			<td><%= Bean.clubXML.getfieldTransl("accounting_percent", true) %></td> <td><input type="text" name="accounting_percent" size="10" value="" class="inputfield"></td>
		</tr>
	    <tr>
			<td><%= Bean.clubXML.getfieldTransl("cd_referral_scheme_rec_type", true) %></td> <td><select name="cd_referral_scheme_rec_type" class="inputfield" > <%= Bean.getReferralSchemeRecTypeOptions("", true) %></select></td>
		</tr>
	    <tr>
			<td><%= Bean.card_taskXML.getfieldTransl("cd_card1", false) %></td> 
			<td>
				<%=Bean.getWindowFindClubCard("", "", "", "", "30") %>
			</td>
			<td colspan="2">&nbsp;</td>
		</tr>
		<tr>
			<td colspan="6" align="center">
				<%=Bean.getSubmitButtonAjax("../crm/club/referral_schemeupdate.jsp") %>
				<%=Bean.getResetButton() %>
				<%=Bean.getGoBackButton("../crm/club/referral_schemespecs.jsp?id=" + id_referral_scheme) %>
			</td>
		</tr>
	</table>
	</form>

		<% 
		} else if (action.equalsIgnoreCase("edit")) {
			
			bcReferralSchemeLineObject line = new bcReferralSchemeLineObject(id_referral_scheme_line);
			
	        %> 
	
			<script>
				var formData = new Array (
						new Array ('accounting_level', 'varchar2', 1),
						new Array ('accounting_percent', 'varchar2', 1),
						new Array ('cd_referral_scheme_rec_type', 'varchar2', 1)
				);
			</script>

		<%= Bean.getOperationTitleShort(
				"",
				Bean.clubXML.getfieldTransl("h_update_referral_scheme_line", false),
				"Y",
				"N") 
		%>
    <form action="../crm/club/referral_schemeupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">
        <input type="hidden" name="type" value="line">
        <input type="hidden" name="action" value="edit">
        <input type="hidden" name="process" value="yes">
        <input type="hidden" name="id_referral_scheme" value="<%=id_referral_scheme %>">
        <input type="hidden" name="id_referral_scheme_line" value="<%=id_referral_scheme_line %>">
        <input type="hidden" name="LUD" value="<%=line.getValue("LUD") %>">
	<table <%=Bean.getTableDetailParam() %>>
	    
	    <tr>
			<td><%= Bean.clubXML.getfieldTransl("name_referral_scheme", false) %></td> <td><input type="text" name="name_referral_scheme" size="55" value="<%=line.getValue("NAME_REFERRAL_SCHEME") %>" readonly="readonly" class="inputfield-ro"> </td>
			<td colspan="2">&nbsp;</td>
		</tr>
	    <tr>
			<td><%= Bean.clubXML.getfieldTransl("accounting_level", true) %></td> <td><input type="text" name="accounting_level" size="15" value="<%=line.getValue("ACCOUNTING_LEVEL") %>" class="inputfield"></td>
		</tr>
	    <tr>
			<td><%= Bean.clubXML.getfieldTransl("accounting_percent", true) %></td> <td><input type="text" name="accounting_percent" size="15" value="<%=line.getValue("ACCOUNTING_PERCENT") %>" class="inputfield"></td>
		</tr>
	    <tr>
			<td><%= Bean.clubXML.getfieldTransl("cd_referral_scheme_rec_type", true) %></td> <td><select name="cd_referral_scheme_rec_type" class="inputfield" > <%= Bean.getReferralSchemeRecTypeOptions(line.getValue("CD_REFERRAL_SCHEME_REC_TYPE"), true) %></select></td>
		</tr>
	    <tr>
			<td><%= Bean.card_taskXML.getfieldTransl("cd_card1", false) %></td> 
			<td>
				<%=Bean.getWindowFindClubCard("", line.getValue("CARD_SERIAL_NUMBER"), line.getValue("CARD_ID_ISSUER"), line.getValue("CARD_ID_PAYMENT_SYSTEM"), "30") %>
			</td>
		</tr>
		<%=	Bean.getCreationAndMoficationRecordFields(
				line.getValue(Bean.getCreationDateFieldName()),
				line.getValue("CREATED_BY"),
				line.getValue(Bean.getLastUpdateDateFieldName()),
				line.getValue("LAST_UPDATE_BY")
			) %>

		<tr>
			<td colspan="6" align="center">
				<%=Bean.getSubmitButtonAjax("../crm/club/referral_schemeupdate.jsp") %>
				<%=Bean.getResetButton() %>
				<%=Bean.getGoBackButton("../crm/club/referral_schemespecs.jsp?id=" + id_referral_scheme) %>
			</td>
		</tr>
	</table>
	</form>

		<%
			
		} else { %> 
		<%= Bean.getUnknownActionText(action) %><%
			
		}
	} else if (process.equalsIgnoreCase("yes")) {
		
		if (action.equalsIgnoreCase("add")) {
			ArrayList<String> pParam = new ArrayList<String>();
					
			pParam.add(id_referral_scheme);
			pParam.add(accounting_level);
			pParam.add(accounting_percent);
			pParam.add(card_serial_number);
			pParam.add(card_id_issuer);
			pParam.add(card_id_payment_system);
			pParam.add(cd_referral_scheme_rec_type);
	
		 	%>
			<%= Bean.executeInsertFunction("PACK$REFERRAL_UI.add_referral_scheme_line", pParam, "../crm/club/referral_schemespecs.jsp?id=" + id_referral_scheme + "&id_referral_scheme_line=", "") %>
			<% 	
		
		} else if (action.equalsIgnoreCase("remove")) {
			
			ArrayList<String> pParam = new ArrayList<String>();
					
			pParam.add(id_referral_scheme_line);

		 	%>
			<%= Bean.executeDeleteFunction("PACK$REFERRAL_UI.delete_referral_scheme_line", pParam, "../crm/club/referral_schemespecs.jsp?id=" + id_referral_scheme, "") %>
			<% 	
	 	
		} else if (action.equalsIgnoreCase("edit")) {
		
			ArrayList<String> pParam = new ArrayList<String>();
						
			pParam.add(id_referral_scheme_line);
			pParam.add(accounting_level);
			pParam.add(accounting_percent);
			pParam.add(card_serial_number);
			pParam.add(card_id_issuer);
			pParam.add(card_id_payment_system);
			pParam.add(cd_referral_scheme_rec_type);
			pParam.add(LUD);

		 	%>
			<%= Bean.executeUpdateFunction("PACK$REFERRAL_UI.update_referral_scheme_line", pParam, "../crm/club/referral_schemespecs.jsp?id=" + id_referral_scheme, "") %>
			<% 	
		
		} else { %> 
			<%= Bean.getUnknownActionText(action) %><%
		}
	} else {
	    %> <%= Bean.getUnknownProcessText(process) %> <%
	}
	
} else {%> 
	<%= Bean.getUnknownTypeText(type) %> <%
}
  %>

</body>
</html>
