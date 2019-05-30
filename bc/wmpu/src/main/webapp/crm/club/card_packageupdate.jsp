<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%@page import="bc.objects.bcLoySchemeObject"%>
<%@page import="bc.objects.bcComissionObject"%>
<%@page import="bc.objects.bcClubRelationshipObject"%>
<%@page import="java.util.HashMap"%>
<%@page import="bc.objects.bcClubShortObject"%>

<%@page import="java.util.ArrayList"%>
<%@page import="bc.objects.bcCardPackageObject"%>
<%@page import="bc.lists.bcListCardPackage"%>
<%@page import="bc.service.bcFeautureParam"%><html>

<%= Bean.getLogOutScript(request) %>

<head>
	<%= Bean.getMetaContent() %>
	<%= Bean.getBottomFrameCSS() %>
</head>

<body>

<%

HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String pageFormName = "CRM_CLUB_CARD_PACKAGE";

Bean.setJspPageForTabName(pageFormName);

request.setCharacterEncoding("UTF-8");

String external_id				= Bean.getDecodeParamPrepare(parameters.get("id"));
String type						= Bean.getDecodeParamPrepare(parameters.get("type")); 
String action					= Bean.getDecodeParamPrepare(parameters.get("action")); 
String process					= Bean.getDecodeParamPrepare(parameters.get("process"));
String back_type				= Bean.getDecodeParamPrepare(parameters.get("back_type"));
String id_jur_prs_card_pack 	= Bean.getDecodeParamPrepare(parameters.get("id_jur_prs_card_pack"));
String id_card_status			= Bean.getDecodeParamPrepare(parameters.get("id_card_status"));
String id_club					= Bean.getDecodeParamPrepare(parameters.get("id_club"));
String id_jur_prs				= Bean.getDecodeParamPrepare(parameters.get("id_jur_prs"));
String id_doc					= Bean.getDecodeParamPrepare(parameters.get("id_doc"));

String updateLink = "../crm/club/card_packageupdate.jsp";
String backLink = "";
String generalLink = "";
back_type = Bean.isEmpty(back_type)?"CARD_PACKAGE":back_type;
System.out.println("back_type="+back_type);
if ("CARD_PACKAGE".equalsIgnoreCase(back_type)) {
	generalLink = "../crm/club/card_package.jsp?";
	if (action.equalsIgnoreCase("add")) {
		backLink = "../crm/club/card_package.jsp?";
	} else {
		backLink = "../crm/club/card_packagespecs.jsp?id="+id_jur_prs_card_pack;
	}
} else if ("CARD_SETTING".equalsIgnoreCase(back_type)) {
	generalLink = "../crm/cards/cardsettingspecs.jsp?id=" + external_id + "&id_club=" + id_club;
	backLink = "../crm/cards/cardsettingspecs.jsp?id=" + external_id + "&id_club=" + id_club;
} else if ("PARTNER".equalsIgnoreCase(back_type)) {
	generalLink = "../crm/clients/yurpersonspecs.jsp?id=" + external_id;
	backLink = "../crm/clients/yurpersonspecs.jsp?id=" + external_id;
} else if ("DOC".equalsIgnoreCase(back_type)) {
	generalLink = "../crm/club/documentspecs.jsp?id=" + external_id;
	backLink = "../crm/club/documentspecs.jsp?id=" + external_id;
}

if (type.equalsIgnoreCase("general")) {
	if (process.equalsIgnoreCase("no")) {
		if (action.equalsIgnoreCase("add") || action.equalsIgnoreCase("add2")) {
    		
			bcClubShortObject club = new bcClubShortObject(Bean.getCurrentClubID());
			
	        %> 
			
			<% if ("CARD_PACKAGE".equalsIgnoreCase(back_type)) { %>
			<%= Bean.getOperationTitle(
					Bean.clubcardXML.getfieldTransl("H_ADD_PACK", false),
					"Y",
					"Y") 
			%>
			<% } else { %>
			<%= Bean.getOperationTitleShort(
					"",
					Bean.clubcardXML.getfieldTransl("H_ADD_PACK", false),
					"Y",
					"Y") 
			%>
			<% } %>
		<script>
			var formData = new Array (
				new Array ('name_jur_prs_card_pack', 'varchar2', 1),
				new Array ('name_jur_prs', 'varchar2', 1),
				new Array ('id_card_status', 'varchar2', 1),
				new Array ('cd_currency', 'varchar2', 1),
				new Array ('action_date_beg', 'varchar2', 1)
			);
			function myValidateForm() {
				return validateForm(formData);
			}
		</script>

		<form action="../crm/club/card_packageupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">  	        
	        <input type="hidden" name="type" value="general">
	        <input type="hidden" name="action" value="add">
	        <input type="hidden" name="process" value="yes">
	        <input type="hidden" name="id_jur_prs_card_pack" value="<%=id_jur_prs_card_pack %>">
    		<input type="hidden" name="id" value="<%=external_id %>">
    		<input type="hidden" name="id_club" value="<%=id_club %>">
    		<input type="hidden" name="back_type" value="<%=back_type %>">
		<table <%=Bean.getTableDetailParam() %>>
		<tr>
			<td><%= Bean.clubcardXML.getfieldTransl("name_jur_prs_card_pack", true) %></td> <td><input type="text" name="name_jur_prs_card_pack" size="70" value="" class="inputfield"></td>
 		    <td><%= Bean.clubXML.getfieldTransl("club", true) %>
				<%= Bean.getGoToClubLink(club.getValue("ID_CLUB")) %>
			</td>
		  	<td>
				<%=Bean.getWindowFindClub("club", club.getValue("ID_CLUB"), club.getValue("SNAME_CLUB"), "25") %>
		  	</td>
		</tr>
	    
	    <tr>
			<td rowspan="3"><%= Bean.clubcardXML.getfieldTransl("desc_jur_prs_card_pack", false) %></td> <td rowspan="3"><textarea name="desc_jur_prs_card_pack" cols="67" rows="3" class="inputfield"></textarea></td>
            <td><%= Bean.clubcardXML.getfieldTransl("action_date_beg", true) %></td><td><%=Bean.getCalendarInputField("action_date_beg", Bean.getSysDate(), "10") %></td>
		</tr>
	    <tr>
            <td><%= Bean.clubcardXML.getfieldTransl("action_date_end", false) %></td><td><%=Bean.getCalendarInputField("action_date_end", "", "10") %></td>
		</tr>
	    <tr>
            <td colspan="2">&nbsp;</td>
		</tr>
   		<tr>
			<td><%= Bean.clubcardXML.getfieldTransl("sname_partner", true) %></td> 
			<td>
				<%=Bean.getWindowFindJurPrs("jur_prs", id_jur_prs, "", "ALL", "60") %>
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
			<td><%= Bean.clubcardXML.getfieldTransl("card_status", true) %></td><td><select name="id_card_status" class="inputfield"><%= Bean.getClubCardStatusOptions(id_card_status, true) %> </select></td>
			<td colspan="2">&nbsp;</td>
		</tr>
		<tr>
			<td colspan="4" class="top_line">&nbsp;</td>
		</tr>
	    <tr>
			<td><%= Bean.clubcardXML.getfieldTransl("cd_currency", true) %></td><td><select name="cd_currency" class="inputfield"><%= Bean.getCurrencyOptions("", true) %> </select></td>
			<td colspan="2">&nbsp;</td>
		</tr>
	    <tr>
			<td><%= Bean.clubcardXML.getfieldTransl("entrance_fee", false) %></td> <td><input type="text" name="entrance_fee" size="15" value="" class="inputfield"></td>
			<td><%= Bean.clubcardXML.getfieldTransl("dealer_margin", false) %></td> <td><input type="text" name="dealer_margin" size="15" value="" class="inputfield"></td>
		</tr>
	    <tr>
			<td><%= Bean.clubcardXML.getfieldTransl("membership_fee", false) %></td> 
			<td>
				<input type="text" name="membership_fee" size="15" value="" class="inputfield">
				&nbsp;за&nbsp;месяцев&nbsp;
				<input type="text" name="membership_fee_month_count" size="1" value="" class="inputfield" maxlength="3">
			</td>
			<td><%= Bean.clubcardXML.getfieldTransl("is_dealer_margin_into_pack", false) %></td><td><select name="is_dealer_margin_into_pack" class="inputfield"><%= Bean.getYesNoLookupNameOptions("N", true) %> </select></td>
		</tr>
	    <tr>
			<td><%= Bean.clubcardXML.getfieldTransl("share_fee", false) %></td> <td><input type="text" name="share_fee" size="15" value="" class="inputfield"></td>
			<td><%= Bean.clubcardXML.getfieldTransl("agent_margin", false) %></td> <td><input type="text" name="agent_margin" size="15" value="" class="inputfield"></td>
		</tr>
	    <tr>
			<td colspan="2">&nbsp;</td>
			<td><%= Bean.clubcardXML.getfieldTransl("is_agent_margin_into_pack", false) %></td><td><select name="is_agent_margin_into_pack" class="inputfield"><%= Bean.getYesNoLookupNameOptions("N", true) %> </select></td>
		</tr>
	 		<tr>
				<td colspan="6" align="center">
					<%=Bean.getSubmitButtonAjax(updateLink, "submit", "updateForm", ("CARD_PACKAGE".equalsIgnoreCase(back_type))?"div_main":"div_data_detail") %>
					<%=Bean.getResetButton() %>
					<%=Bean.getGoBackButton(backLink) %>
				</td>
			</tr>
		</table>
		</form>

		<% 	
		} else if (action.equalsIgnoreCase("select")) {
			String tagCardPack = "" + back_type + "_SELECT_CARD_PACK";
			String tagCardPackFind = "" + back_type + "_SELECT_CARD_PACK_FIND";
			String tagCardPackageCardStatus = "" + back_type + "_SELECT_CARD_PACKAGE_CARD_STATUS";
			
			String l_card_pack_page = Bean.getDecodeParam(parameters.get("card_pack_page"));
			Bean.pageCheck(pageFormName + tagCardPack, l_card_pack_page);
			String l_card_pack_page_beg = Bean.getFirstRowNumber(pageFormName + tagCardPack);
			String l_card_pack_page_end = Bean.getLastRowNumber(pageFormName + tagCardPack);
			
			String card_pack_find 	= Bean.getDecodeParam(parameters.get("card_pack_find"));
			System.out.println("card_pack_find="+card_pack_find);
			card_pack_find 	= Bean.checkFindString(pageFormName + tagCardPackFind, card_pack_find, l_card_pack_page);

			String card_pack_card_status 	= Bean.getDecodeParam(parameters.get("card_pack_card_status"));
			card_pack_card_status 	= Bean.checkFindString(pageFormName + tagCardPackageCardStatus, card_pack_card_status, l_card_pack_page);
			
			bcListCardPackage list = new bcListCardPackage();
	    	
	    	String mySelectLink = "../crm/club/card_packageupdate.jsp?back_type="+back_type+"&type=general&id="+external_id+"&id_jur_prs="+id_jur_prs+"&id_doc="+id_doc+"&action=copy&process=no";
	    	
	    	%>
			<%= Bean.getOperationTitleShort(
					"",
					Bean.clubcardXML.getfieldTransl("H_SELECT_PACK", false),
					"N",
					"N") 
			%>
			<table <%=Bean.getTableBottomFilter() %>><tbody>
			<tr>
				<%=Bean.getFindHTML("card_pack_find", card_pack_find, "../crm/club/card_packageupdate.jsp?back_type="+back_type+"&type=general&id="+external_id+"&action=select&process=no&card_pack_page=1&", "div_data_detail") %>
			
				<%=Bean.getSelectOnChangeBeginHTML("", "card_pack_card_status", "../crm/club/card_packageupdate.jsp?back_type="+back_type+"&type=general&id="+external_id+"&action=select&process=no&card_pack_page=1", Bean.clubcardXML.getfieldTransl("NAME_CARD_STATUS", false), "div_data_detail") %>
					<%= Bean.getClubCardStatusOptions(card_pack_card_status, true) %>
				<%=Bean.getSelectOnChangeEndHTML() %>

				<%= Bean.getPagesHTML(pageFormName + tagCardPack, "../crm/club/card_packageupdate.jsp?back_type="+back_type+"&type=general&id="+external_id+"&action=select&process=no&", "card_pack_page", "", "div_data_detail") %>
	
			</tr>
			</tbody>
			</table>

	    	<%=list.getCardPackagesHTMLOnlySelect(card_pack_find, card_pack_card_status, mySelectLink, l_card_pack_page_beg, l_card_pack_page_end) %>
			<%
		} else if (action.equalsIgnoreCase("edit") || action.equalsIgnoreCase("copy")) {
			bcCardPackageObject pack = new bcCardPackageObject(id_jur_prs_card_pack);
			
	        %> 
			
			<%= Bean.getOperationTitleShort(
					"",
					Bean.clubcardXML.getfieldTransl(action.equalsIgnoreCase("edit")?"H_UPDATE_PACK":"H_ADD_PACK", false),
					"Y",
					"Y") 
			%>

		<script>
			var formData = new Array (
				new Array ('name_jur_prs_card_pack', 'varchar2', 1),
				new Array ('name_jur_prs', 'varchar2', 1),
				new Array ('id_card_status', 'varchar2', 1),
				new Array ('cd_currency', 'varchar2', 1),
				new Array ('action_date_beg', 'varchar2', 1)
			);
			function myValidateForm() {
				return validateForm(formData);
			}
		</script>
    <form action="../crm/club/card_packageupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST">
    	<input type="hidden" name="action" value="<%=action %>">
    	<input type="hidden" name="process" value="yes">
    	<input type="hidden" name="type" value="general">
	    <input type="hidden" name="id_jur_prs_card_pack" value="<%=id_jur_prs_card_pack %>">
    	<input type="hidden" name="id" value="<%=external_id %>">
    	<input type="hidden" name="id_club" value="<%=Bean.isEmpty(id_club)?pack.getValue("ID_CLUB"):id_club %>">
    	<input type="hidden" name="back_type" value="<%=back_type %>">
    	<input type="hidden" name="LUD" value="<%=pack.getValue("LUD") %>">
	<table <%=Bean.getTableDetailParam() %>>
	    <tr>
			<td><%= Bean.clubcardXML.getfieldTransl("name_jur_prs_card_pack", true) %></td> <td><input type="text" name="name_jur_prs_card_pack" size="70" value="<%=(action.equalsIgnoreCase("copy"))?"":pack.getValue("NAME_JUR_PRS_CARD_PACK") %>" class="inputfield"></td>
			<td><%= Bean.clubXML.getfieldTransl("club", false) %>
					<%= Bean.getGoToClubLink(pack.getValue("ID_CLUB")) %>
			</td><td><input type="text" name="name_club" size="30" value="<%= Bean.getClubShortName(pack.getValue("ID_CLUB")) %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
	    <tr>
			<td rowspan="3"><%= Bean.clubcardXML.getfieldTransl("desc_jur_prs_card_pack", false) %></td> <td rowspan="3"><textarea name="desc_jur_prs_card_pack" cols="67" rows="3" class="inputfield"><%= (action.equalsIgnoreCase("copy"))?"":pack.getValue("DESC_JUR_PRS_CARD_PACK") %></textarea></td>
            <td><%= Bean.clubcardXML.getfieldTransl("action_date_beg", true) %></td><td><%=Bean.getCalendarInputField("action_date_beg", (action.equalsIgnoreCase("copy"))?"":pack.getValue("ACTION_DATE_BEG_FRMT"), "10") %></td>
		</tr>
	    <tr>
			<td><%= Bean.clubcardXML.getfieldTransl("action_date_end", false) %></td><td><%=Bean.getCalendarInputField("action_date_end", (action.equalsIgnoreCase("copy"))?"":pack.getValue("ACTION_DATE_END_FRMT"), "10") %></td>
		</tr>
	    <tr>
            <td colspan="2">&nbsp;</td>
		</tr>
	    <tr>
			<td><%= Bean.clubcardXML.getfieldTransl("sname_partner", true) %>
				<%=(back_type.equalsIgnoreCase("PARTNER"))?Bean.getGoToJurPrsHyperLink(pack.getValue("ID_JUR_PRS")):(action.equalsIgnoreCase("copy"))?"":Bean.getGoToJurPrsHyperLink(pack.getValue("ID_JUR_PRS")) %>
			</td> 
			<td>
				<%=Bean.getWindowFindJurPrs("jur_prs", (action.equalsIgnoreCase("copy")?(back_type.equalsIgnoreCase("PARTNER")?external_id:""):pack.getValue("ID_JUR_PRS")), (action.equalsIgnoreCase("copy")?"":pack.getValue("SNAME_JUR_PRS")), "ALL", "60") %>
			</td>
            <td colspan="2">&nbsp;</td>
		</tr>
   		<tr>
			<td><%= Bean.clubcardXML.getfieldTransl("id_doc", false) %>
				<%= (back_type.equalsIgnoreCase("DOC"))?Bean.getGoToDocLink(pack.getValue("ID_DOC")):(action.equalsIgnoreCase("copy"))?"":Bean.getGoToDocLink(pack.getValue("ID_DOC")) %>
			</td> 
			<td>
				<%=Bean.getWindowDocuments("doc", (back_type.equalsIgnoreCase("DOC"))?pack.getValue("ID_DOC"):(action.equalsIgnoreCase("copy"))?"":pack.getValue("ID_DOC"), "60") %>
			</td>
			<td colspan="2">&nbsp;</td>
   		</tr>
	    <tr>
			<td><%= Bean.clubcardXML.getfieldTransl("card_status", true) %></td><td><select name="id_card_status" class="inputfield"><%= Bean.getClubCardStatusOptions(pack.getValue("ID_CARD_STATUS"), true) %> </select></td>
			<td colspan="2">&nbsp;</td>
		</tr>
		<tr>
			<td colspan="4" class="top_line">&nbsp;</td>
		</tr>
	    <tr>
			<td><%= Bean.clubcardXML.getfieldTransl("cd_currency", true) %></td><td><select name="cd_currency" class="inputfield"><%= Bean.getCurrencyOptions(pack.getValue("CD_CURRENCY"), true) %> </select></td>
			<td colspan="2">&nbsp;</td>
		</tr>
	    <tr>
			<td><%= Bean.clubcardXML.getfieldTransl("total_amount_card_pack", false) %></td> <td><input type="text" name="total_amount_card_pack" size="15" value="<%=pack.getValue("TOTAL_AMOUNT_JP_CARD_PACK_FRMT") %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.clubcardXML.getfieldTransl("dealer_margin", false) %></td> <td><input type="text" name="dealer_margin" size="15" value="<%=pack.getValue("DEALER_MARGIN_FRMT") %>" class="inputfield"></td>
		</tr>
	    <tr>
			<td><%= Bean.clubcardXML.getfieldTransl("entrance_fee", false) %></td> <td><input type="text" name="entrance_fee" size="15" value="<%=pack.getValue("ENTRANCE_FEE_FRMT") %>" class="inputfield"></td>
			<td><%= Bean.clubcardXML.getfieldTransl("is_dealer_margin_into_pack", false) %></td><td><select name="is_dealer_margin_into_pack" class="inputfield"><%= Bean.getYesNoLookupNameOptions(pack.getValue("IS_DEALER_MARGIN_INTO_PACK"), true) %> </select></td>
		</tr>
	    <tr>
			<td><%= Bean.clubcardXML.getfieldTransl("membership_fee", false) %></td> 
			<td>
				<input type="text" name="membership_fee" size="15" value="<%=pack.getValue("MEMBERSHIP_FEE_FRMT") %>" class="inputfield">
				&nbsp;<%= Bean.clubcardXML.getfieldTransl("membership_fee_month_count", false) %>&nbsp;
				<input type="text" name="membership_fee_month_count" size="1" value="<%=pack.getValue("MEMBERSHIP_FEE_MONTH_COUNT") %>" class="inputfield" maxlength="3">
			</td>
			<td><%= Bean.clubcardXML.getfieldTransl("agent_margin", false) %></td> <td><input type="text" name="agent_margin" size="15" value="<%=pack.getValue("AGENT_MARGIN_FRMT") %>" class="inputfield"></td>
		</tr>
	    <tr>
			<td><%= Bean.clubcardXML.getfieldTransl("share_fee", false) %></td> <td><input type="text" name="share_fee" size="15" value="<%=pack.getValue("SHARE_FEE_FRMT") %>" class="inputfield"></td>
			<td><%= Bean.clubcardXML.getfieldTransl("is_agent_margin_into_pack", false) %></td><td><select name="is_agent_margin_into_pack" class="inputfield"><%= Bean.getYesNoLookupNameOptions(pack.getValue("IS_AGENT_MARGIN_INTO_PACK"), true) %> </select></td>
		</tr>
		<%=	Bean.getIdCreationAndMoficationRecordFields(
				pack.getValue("ID_JUR_PRS_CARD_PACK"),
				pack.getValue(Bean.getCreationDateFieldName()),
				pack.getValue("CREATED_BY"),
				pack.getValue(Bean.getLastUpdateDateFieldName()),
				pack.getValue("LAST_UPDATE_BY")
			) %>
		<tr>
			<td colspan="6" align="center">
				<%=Bean.getSubmitButtonAjax(updateLink, "submit", "updateForm", ("CARD_PACKAGE".equalsIgnoreCase(back_type))?"div_main":"div_data_detail") %>
				<%=Bean.getResetButton() %>
				<%=Bean.getGoBackButton(backLink) %>
			</td>
		</tr>
	</table>

	</form>
		<%= Bean.getCalendarScript("action_date_beg", false) %>
		<%= Bean.getCalendarScript("action_date_end", false) %>
			<%
			
	   	} else {
   	    %> <%= Bean.getUnknownActionText(action) %><%
   		}
	} else if (process.equalsIgnoreCase("yes")) {    

	  	String
	  		name_jur_prs_card_pack 		= Bean.getDecodeParam(parameters.get("name_jur_prs_card_pack")), 
	  		desc_jur_prs_card_pack 		= Bean.getDecodeParam(parameters.get("desc_jur_prs_card_pack")), 
	  		action_date_beg 			= Bean.getDecodeParam(parameters.get("action_date_beg")), 
	  		action_date_end 			= Bean.getDecodeParam(parameters.get("action_date_end")), 
	  		cd_currency 				= Bean.getDecodeParam(parameters.get("cd_currency")), 
	  		entrance_fee 				= Bean.getDecodeParam(parameters.get("entrance_fee")), 
	  		share_fee 					= Bean.getDecodeParam(parameters.get("share_fee")), 
	  		membership_fee	 			= Bean.getDecodeParam(parameters.get("membership_fee")), 
	  		membership_fee_month_count	= Bean.getDecodeParam(parameters.get("membership_fee_month_count")), 
	  		dealer_margin		 		= Bean.getDecodeParam(parameters.get("dealer_margin")), 
	  		agent_margin		 		= Bean.getDecodeParam(parameters.get("agent_margin")), 
	  		is_dealer_margin_into_pack	= Bean.getDecodeParam(parameters.get("is_dealer_margin_into_pack")), 
	  		is_agent_margin_into_pack	= Bean.getDecodeParam(parameters.get("is_agent_margin_into_pack")), 
	  		LUD							= Bean.getDecodeParam(parameters.get("LUD"));
	    
		if (action.equalsIgnoreCase("add")) { 
				
			ArrayList<String> pParam = new ArrayList<String>();	
					
			pParam.add(name_jur_prs_card_pack);
			pParam.add(desc_jur_prs_card_pack);
			pParam.add(id_club);
			pParam.add(action_date_beg);
			pParam.add(action_date_end);
			pParam.add(id_jur_prs);
			pParam.add(id_card_status);
			pParam.add(cd_currency);
			pParam.add(entrance_fee);
			pParam.add(membership_fee);
			pParam.add(membership_fee_month_count);
			pParam.add(share_fee);
			pParam.add(dealer_margin);
			pParam.add(is_dealer_margin_into_pack);
			pParam.add(agent_margin);
			pParam.add(is_agent_margin_into_pack);
			pParam.add(id_doc);
			pParam.add(Bean.getDateFormat());
				
		 	%>
			<%= Bean.executeInsertFunction("PACK$CARD_UI.add_card_pack", pParam, backLink + "&id_jur_prs_card_pack=", "") %>
			<% 	
	
		} else if (action.equalsIgnoreCase("remove")) {
			ArrayList<String> pParam = new ArrayList<String>();	
					
			pParam.add(id_jur_prs_card_pack);
			
		 	%>
			<%= Bean.executeDeleteFunction("PACK$CARD_UI.delete_card_pack", pParam, generalLink , "") %>
			<% 	
	
		} else if (action.equalsIgnoreCase("edit")) {
			ArrayList<String> pParam = new ArrayList<String>();	
				
			pParam.add(id_jur_prs_card_pack);
			pParam.add(name_jur_prs_card_pack);
			pParam.add(desc_jur_prs_card_pack);
			pParam.add(action_date_beg);
			pParam.add(action_date_end);
			pParam.add(id_jur_prs);
			pParam.add(id_card_status);
			pParam.add(cd_currency);
			pParam.add(entrance_fee);
			pParam.add(membership_fee);
			pParam.add(membership_fee_month_count);
			pParam.add(share_fee);
			pParam.add(dealer_margin);
			pParam.add(is_dealer_margin_into_pack);
			pParam.add(agent_margin);
			pParam.add(is_agent_margin_into_pack);
			pParam.add(id_doc);
			pParam.add(Bean.getDateFormat());
			pParam.add(LUD);
	
		 	%>
			<%= Bean.executeUpdateFunction("PACK$CARD_UI.update_card_pack", pParam, backLink, "") %>
			<% 	
	
		} else if (action.equalsIgnoreCase("copy")) {
			ArrayList<String> pParam = new ArrayList<String>();	
				
			pParam.add(id_jur_prs_card_pack);
			pParam.add(name_jur_prs_card_pack);
			pParam.add(desc_jur_prs_card_pack);
			pParam.add(id_club);
			pParam.add(action_date_beg);
			pParam.add(action_date_end);
			pParam.add(id_jur_prs);
			pParam.add(id_card_status);
			pParam.add(cd_currency);
			pParam.add(entrance_fee);
			pParam.add(membership_fee);
			pParam.add(membership_fee_month_count);
			pParam.add(share_fee);
			pParam.add(dealer_margin);
			pParam.add(is_dealer_margin_into_pack);
			pParam.add(agent_margin);
			pParam.add(is_agent_margin_into_pack);
			pParam.add(id_doc);
			pParam.add(Bean.getDateFormat());
	
		 	%>
			<%= Bean.executeInsertFunction("PACK$CARD_UI.copy_card_pack", pParam, backLink + "&id_jur_prs_card_packd=", "") %>
			<% 	
	
		} else {%> 
			<%= Bean.getUnknownActionText(action) %><% 
		}
	} else {%> 
		<%= Bean.getUnknownProcessText(process) %><% 
	}
} else {%> 
	<%= Bean.getUnknownTypeText(type) %> <%
}
  %>

</body>
</html>
