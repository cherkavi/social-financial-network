<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%@page import="java.util.HashMap"%>
<%@page import="bc.objects.bcCardPackageObject"%><html>

<%= Bean.getLogOutScript(request) %>

<head>
	<%= Bean.getMetaContent() %>
	<%= Bean.getBottomFrameCSS() %>
</head>
<body topmargin="0">
<div id="div_tabsheet">
<%

HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String pageFormName = "CRM_CLUB_CARD_PACKAGE";

String tagGivenCards = "_GIVEN_CARDS";
String tagGivenCardsFind = "_GIVEN_CARDS_FIND";
String tagGivenCardsMemberStatus = "_GIVEN_CARDS_MEMBER_STATUS";
String tagGivenCardsRoleState = "_GIVEN_CARDS_ROLE_STATE";

String tagTrans = "_TRANS";
String tagTransFind = "_TRANS_FIND";
String tagTransPayType = "_TRANS_PAY_TYPE";

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
	bcCardPackageObject pack = new bcCardPackageObject(id);
	
	String l_given_cards_page = Bean.getDecodeParam(parameters.get("given_cards_page"));
	Bean.pageCheck(pageFormName + tagGivenCards, l_given_cards_page);
	String l_given_cards_page_beg = Bean.getFirstRowNumber(pageFormName + tagGivenCards);
	String l_given_cards_page_end = Bean.getLastRowNumber(pageFormName + tagGivenCards);
	
	String given_cards_find 	= Bean.getDecodeParam(parameters.get("given_cards_find"));
	given_cards_find 	= Bean.checkFindString(pageFormName + tagGivenCardsFind, given_cards_find, l_given_cards_page);
	
	String given_cards_member_status 	= Bean.getDecodeParam(parameters.get("given_cards_member_status"));
	given_cards_member_status 	= Bean.checkFindString(pageFormName + tagGivenCardsMemberStatus, given_cards_member_status, l_given_cards_page);
	
	String given_cards_role_state 	= Bean.getDecodeParam(parameters.get("given_cards_role_state"));
	given_cards_role_state 	= Bean.checkFindString(pageFormName + tagGivenCardsRoleState, given_cards_role_state, l_given_cards_page);
	
	String l_trans_page = Bean.getDecodeParam(parameters.get("trans_page"));
	Bean.pageCheck(pageFormName + tagTrans, l_trans_page);
	String l_trans_page_beg = Bean.getFirstRowNumber(pageFormName + tagTrans);
	String l_trans_page_end = Bean.getLastRowNumber(pageFormName + tagTrans);

	String trans_find 	= Bean.getDecodeParam(parameters.get("trans_find"));
	trans_find 	= Bean.checkFindString(pageFormName + tagTransFind, trans_find, l_trans_page);

	String trans_pay_type 	= Bean.getDecodeParam(parameters.get("trans_pay_type"));
	trans_pay_type 	= Bean.checkFindString(pageFormName + tagTransPayType, trans_pay_type, l_trans_page);
	

%>

	<table <%=Bean.getTableMenuParam() %>>
		<tr>
			<%= Bean.getPageHeader() %>

			<% if (Bean.currentMenu.isCurrentTabAndEditPermitted("CRM_CLUB_CARD_PACKAGE_INFO")) { %>
			    <%= Bean.getMenuButton("ADD", "../crm/club/card_packageupdate.jsp?id_jur_prs_card_pack=" + id + "&type=general&action=add2&process=no", "", "") %>
			    <%= Bean.getMenuButton("DELETE", "../crm/club/card_packageupdate.jsp?id_jur_prs_card_pack=" + id + "&type=general&action=remove&process=yes", Bean.clubcardXML.getfieldTransl("h_delete_pack", false), pack.getValue("NAME_JUR_PRS_CARD_PACK")) %>
			<%  } %>
			
			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CRM_CLUB_CARD_PACKAGE_GIVEN_CARDS")) { %>

				 <!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagGivenCards, "../crm/club/card_packagespecs.jsp?id=" + id + "&tab="+Bean.currentMenu.getTabID("CRM_CLUB_CARD_PACKAGE_GIVEN_CARDS")+"&", "given_cards_page") %>
			<% } %>
			
			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CRM_CLUB_CARD_PACKAGE_TRANS")) { %>

				 <!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagTrans, "../crm/club/card_packagespecs.jsp?id=" + id + "&tab="+Bean.currentMenu.getTabID("CRM_CLUB_CARD_PACKAGE_TRANS")+"&", "trans_page") %>
			<% } %>
		</tr>
	</table>
	<table <%=Bean.getTableMenuParam() %>>
		<%= Bean.getDetailCaption(pack.getValue("NAME_JUR_PRS_CARD_PACK")) %>
		<tr>
			<td>
				<!-- Выводим перечень закладок -->
				<%= Bean.currentMenu.getFormTabScheeds(tab, "../crm/club/card_packagespecs.jsp?id=" + id) %>
			</td>
		</tr>
	</table>
</div>
<div id="div_data">
<div id="div_data_detail">
<%
if (Bean.currentMenu.isCurrentTabAndPreviewPermitted("CRM_CLUB_CARD_PACKAGE_INFO")) {
 %>	 
	<form>
	<table <%=Bean.getTableDetailParam() %>>
	    <tr>
			<td><%= Bean.clubcardXML.getfieldTransl("name_jur_prs_card_pack", false) %></td> <td><%=Bean.getInputTextElement("name_jur_prs_card_pack", "", pack.getValue("NAME_JUR_PRS_CARD_PACK"), true, "350") %></td>
			<td><%= Bean.clubXML.getfieldTransl("club", false) %>
					<%= Bean.getGoToClubLink(pack.getValue("ID_CLUB")) %>
			</td><td><input type="text" name="name_club" size="30" value="<%= Bean.getClubShortName(pack.getValue("ID_CLUB")) %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
	    <tr>
			<td rowspan="3"><%= Bean.clubcardXML.getfieldTransl("desc_jur_prs_card_pack", false) %></td> <td rowspan="3"><%=Bean.getTextareaElement("desc_jur_prs_card_pack", "", pack.getValue("DESC_JUR_PRS_CARD_PACK"), true, "350", "60") %></td>
			<td><%= Bean.clubcardXML.getfieldTransl("action_date_beg", false) %></td> <td><input type="text" name="action_date_beg" size="15" value="<%=pack.getValue("ACTION_DATE_BEG_DF") %>" readonly="readonly" class="inputfield-ro""></td>
		</tr>
	    <tr>
			<td><%= Bean.clubcardXML.getfieldTransl("action_date_end", false) %></td> <td><input type="text" name="action_date_end" size="15" value="<%=pack.getValue("ACTION_DATE_END_DF") %>" readonly="readonly" class="inputfield-ro""></td>
		</tr>
	    <tr>
            <td colspan="2">&nbsp;</td>
		</tr>
	    <tr>
			<td><%= Bean.clubcardXML.getfieldTransl("sname_partner", false) %>
				<%=Bean.getGoToJurPrsHyperLink(pack.getValue("ID_JUR_PRS")) %>
			</td> 
			<td>
				<%=Bean.getInputTextElement("name_jur_prs", "", pack.getValue("SNAME_JUR_PRS"), true, "350") %>
			</td>
			<td colspan="2">&nbsp;</td>
		</tr>
	    <tr>
			
			<td><%= Bean.clubcardXML.getfieldTransl("id_doc", false) %>
				<%= Bean.getGoToDocLink(pack.getValue("ID_DOC")) %>
			</td> 
			<td>
				<%=Bean.getInputTextElement("name_doc", "", Bean.getDocName(pack.getValue("ID_DOC")), true, "350") %>
			</td>
			<td colspan="2">&nbsp;</td>
		</tr>
	    <tr>
			<td><%= Bean.clubcardXML.getfieldTransl("card_status", false) %>
				<%=Bean.getGoToCardSettingLink(pack.getValue("ID_CARD_STATUS"), pack.getValue("ID_CLUB")) %>
			</td><td><%=Bean.getInputTextElement("name_card_status", "", pack.getValue("NAME_CARD_STATUS"), true, "150") %></td>
		</tr>
		<tr>
			<td colspan="4" class="top_line">&nbsp;</td>
		</tr>
	    <tr>
			<td><%= Bean.clubcardXML.getfieldTransl("cd_currency", false) %>
				<%=Bean.getGoToCurrencyLink(pack.getValue("CD_CURRENCY")) %>
			</td><td><%=Bean.getInputTextElement("name_currency", "", Bean.getCurrencyNameById(pack.getValue("CD_CURRENCY")), true, "150") %></td>
			<td colspan="2">&nbsp;</td>
		</tr>
	    <tr>
			<td><%= Bean.clubcardXML.getfieldTransl("total_amount_card_pack", false) %></td> <td><input type="text" name="total_amount_card_pack" size="15" value="<%=pack.getValue("TOTAL_AMOUNT_JP_CARD_PACK_FRMT") %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.clubcardXML.getfieldTransl("dealer_margin", false) %></td> <td><input type="text" name="dealer_margin" size="15" value="<%=pack.getValue("DEALER_MARGIN_FRMT") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
	    <tr>
			<td><%= Bean.clubcardXML.getfieldTransl("entrance_fee", false) %></td> <td><input type="text" name="entrance_fee" size="15" value="<%=pack.getValue("ENTRANCE_FEE_FRMT") %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.clubcardXML.getfieldTransl("is_dealer_margin_into_pack", false) %></td><td><input type="text" name="is_dealer_margin_into_pack" size="15" value="<%=Bean.getMeaningFoCodeValue("YES_NO", pack.getValue("IS_DEALER_MARGIN_INTO_PACK")) %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
	    <tr>
			<td><%= Bean.clubcardXML.getfieldTransl("membership_fee", false) %></td> 
			<td>
				<input type="text" name="membership_fee" size="15" value="<%=pack.getValue("MEMBERSHIP_FEE_FRMT") %>" readonly="readonly" class="inputfield-ro">
				&nbsp;<%= Bean.clubcardXML.getfieldTransl("membership_fee_month_count", false) %>&nbsp;
				<input type="text" name="membership_fee_month_count" size="1" value="<%=pack.getValue("MEMBERSHIP_FEE_MONTH_COUNT") %>" readonly="readonly" class="inputfield-ro" maxlength="3">
			</td>
			<td><%= Bean.clubcardXML.getfieldTransl("agent_margin", false) %></td> <td><input type="text" name="agent_margin" size="15" value="<%=pack.getValue("AGENT_MARGIN_FRMT") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
	    <tr>
			<td><%= Bean.clubcardXML.getfieldTransl("share_fee", false) %></td> <td><input type="text" name="share_fee" size="15" value="<%=pack.getValue("SHARE_FEE_FRMT") %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.clubcardXML.getfieldTransl("is_agent_margin_into_pack", false) %></td><td><input type="text" name="is_agent_margin_into_pack" size="15" value="<%=Bean.getMeaningFoCodeValue("YES_NO", pack.getValue("IS_AGENT_MARGIN_INTO_PACK")) %>" readonly="readonly" class="inputfield-ro"></td>
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
				<%=Bean.getGoBackButton("../crm/club/card_package.jsp") %>
			</td>
		</tr>
	</table>
	</form>

<% } else if (Bean.currentMenu.isCurrentTabAndEditPermitted("CRM_CLUB_CARD_PACKAGE_INFO")) { %>
	
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
    	<input type="hidden" name="action" value="edit">
    	<input type="hidden" name="process" value="yes">
    	<input type="hidden" name="type" value="general">
    	<input type="hidden" name="id" value="<%=pack.getValue("ID_JUR_PRS_CARD_PACK") %>">
    	<input type="hidden" name="id_jur_prs_card_pack" value="<%=pack.getValue("ID_JUR_PRS_CARD_PACK") %>">
    	<input type="hidden" name="LUD" value="<%=pack.getValue("LUD") %>">
	<table <%=Bean.getTableDetailParam() %>>
	    <tr>
			<td><%= Bean.clubcardXML.getfieldTransl("name_jur_prs_card_pack", true) %></td> <td><input type="text" name="name_jur_prs_card_pack" size="70" value="<%=pack.getValue("NAME_JUR_PRS_CARD_PACK") %>" class="inputfield"></td>
			<td><%= Bean.clubXML.getfieldTransl("club", false) %>
					<%= Bean.getGoToClubLink(pack.getValue("ID_CLUB")) %>
			</td><td><input type="text" name="name_club" size="30" value="<%= Bean.getClubShortName(pack.getValue("ID_CLUB")) %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
	    <tr>
			<td rowspan="3"><%= Bean.clubcardXML.getfieldTransl("desc_jur_prs_card_pack", false) %></td> <td rowspan="3"><textarea name="desc_jur_prs_card_pack" cols="67" rows="3" class="inputfield"><%= pack.getValue("DESC_JUR_PRS_CARD_PACK") %></textarea></td>
            <td><%= Bean.clubcardXML.getfieldTransl("action_date_beg", true) %></td><td><%=Bean.getCalendarInputField("action_date_beg", pack.getValue("ACTION_DATE_BEG_FRMT"), "10") %></td>
		</tr>
	    <tr>
			<td><%= Bean.clubcardXML.getfieldTransl("action_date_end", false) %></td><td><%=Bean.getCalendarInputField("action_date_end", pack.getValue("ACTION_DATE_END_FRMT"), "10") %></td>
		</tr>
	    <tr>
            <td colspan="2">&nbsp;</td>
		</tr>
	    <tr>
			<td><%= Bean.clubcardXML.getfieldTransl("sname_partner", true) %>
				<%=Bean.getGoToJurPrsHyperLink(pack.getValue("ID_JUR_PRS")) %>
			</td> 
			<td>
				<%=Bean.getWindowFindJurPrs("jur_prs", pack.getValue("ID_JUR_PRS"), pack.getValue("SNAME_JUR_PRS"), "ALL", "60") %>
			</td>
            <td colspan="2">&nbsp;</td>
		</tr>
   		<tr>
			<td><%= Bean.clubcardXML.getfieldTransl("id_doc", false) %>
				<%= Bean.getGoToDocLink(pack.getValue("ID_DOC")) %>
			</td> 
			<td>
				<%=Bean.getWindowDocuments("doc", pack.getValue("ID_DOC"), "60") %>
			</td>
			<td colspan="2">&nbsp;</td>
   		</tr>
	    <tr>
			<td><%= Bean.clubcardXML.getfieldTransl("card_status", true) %>
				<%=Bean.getGoToCardSettingLink(pack.getValue("ID_CARD_STATUS"), pack.getValue("ID_CLUB")) %>
			</td><td><select name="id_card_status" class="inputfield"><%= Bean.getClubCardStatusOptions(pack.getValue("ID_CARD_STATUS"), true) %> </select></td>
			<td colspan="2">&nbsp;</td>
		</tr>
		<tr>
			<td colspan="4" class="top_line">&nbsp;</td>
		</tr>
	    <tr>
			<td><%= Bean.clubcardXML.getfieldTransl("cd_currency", true) %>
				<%=Bean.getGoToCurrencyLink(pack.getValue("CD_CURRENCY")) %>
			</td><td><select name="cd_currency" class="inputfield"><%= Bean.getCurrencyOptions(pack.getValue("CD_CURRENCY"), true) %> </select></td>
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
				<%=Bean.getSubmitButtonAjax("../crm/club/card_packageupdate.jsp") %>
				<%=Bean.getResetButton() %>
				<%=Bean.getGoBackButton("../crm/club/card_package.jsp") %>
			</td>
		</tr>
	</table>

	</form>
		<%= Bean.getCalendarScript("action_date_beg", false) %>
		<%= Bean.getCalendarScript("action_date_end", false) %>

<% } %>

<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CRM_CLUB_CARD_PACKAGE_GIVEN_CARDS")) {%>
		<table <%=Bean.getTableBottomFilter() %>><tbody>
		<tr>
			<%=Bean.getFindHTML("given_cards_find", given_cards_find, "../crm/club/card_packagespecs.jsp?id=" + id + "&given_cards_page=1") %>

			<%=Bean.getSelectOnChangeBeginHTML("given_cards_member_status", "../crm/club/card_packagespecs.jsp?id=" + id + "&given_cards_page=1", Bean.natprsXML.getfieldTransl("name_club_member_status", false)) %>
				<%= Bean.getClubMemberStatusOptions(given_cards_member_status, true) %>
			<%=Bean.getSelectOnChangeEndHTML() %>

			<%=Bean.getSelectOnChangeBeginHTML("given_cards_role_state", "../crm/club/card_packagespecs.jsp?id=" + id + "&given_cards_page=1", Bean.natprsXML.getfieldTransl("name_nat_prs_role_state", false)) %>
				<%= Bean.getNatPrsRoleStateOptions(given_cards_role_state, true) %>
			<%=Bean.getSelectOnChangeEndHTML() %>
		</tr>
		</tbody>
		</table>

	<%= pack.getGivenCardsHTML(given_cards_find, given_cards_member_status, given_cards_role_state, l_given_cards_page_beg, l_given_cards_page_end) %>
<%} %>

<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CRM_CLUB_CARD_PACKAGE_TRANS")) {%>
		<table <%=Bean.getTableBottomFilter() %>><tbody>
		<tr>
			<%=Bean.getFindHTML("trans_find", trans_find, "../crm/club/card_packagespecs.jsp?id=" + id + "&trans_page=1") %>


		<%=Bean.getSelectOnChangeBeginHTML("trans_pay_type", "../crm/club/card_packagespecs.jsp?id=" + id + "&trans_page=1", Bean.transactionXML.getfieldTransl("cheque_type", false)) %>
			<%= Bean.getTransPayTypeOptions(trans_pay_type, true) %>
		<%=Bean.getSelectOnChangeEndHTML() %>
	
		</tr>
		</tbody>
		</table>

	<%= pack.getTransactionsHTML(trans_find, "14", trans_pay_type, "0", l_trans_page_beg, l_trans_page_end) %>
<%} %>


<% } %>
</div></div>
</body>
</html>
