<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%@page import="java.util.HashMap"%>
<%@page import="bc.objects.bcClubTargetProgramObject"%>
<%@page import="java.util.Date"%><html>

<%= Bean.getLogOutScript(request) %>

<head>
	<%= Bean.getMetaContent() %>
	<%= Bean.getBottomFrameCSS() %>
</head>
<body topmargin="0">
<div id="div_tabsheet">
<%

HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String pageFormName = "CLUB_TARGET_PROGRAM";

String tagSubprogram = "_SUBPROGRAM";
String tagSubprogramFind = "_SUBPROGRAM_FIND";
String tagSubprogramPayPeriod = "_SUBPROGRAM_PAY_PERIOD";
String tagParticipant = "_PARTICIPANT";
String tagParticipantFind = "_PARTICIPANT_FIND";

String tagTrans = "_TRANS";
String tagTransFind = "_TRANS_FIND";
String tagTransPayType = "_TRANS_PAY_TYPE";
String tagRest = "_REST";

String tagReferralScheme = "_REFERRAL_SCHEME";
String tagReferralSchemeFind = "_REFERRAL_SCHEME_FIND";
String tagReferralSchemeType = "_REFERRAL_SCHEME_TYPE";
String tagReferralSchemeCalcType = "_REFERRAL_SCHEME_CALC_TYPE";

String tagServicePlace = "_SERVICE_PLACE";
String tagServicePlaceFind = "_SERVICE_PLACE_FIND";


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
	bcClubTargetProgramObject program = new bcClubTargetProgramObject(id);
	
	if (!"0".equalsIgnoreCase(program.getValue("CHILD_COUNT"))) {
		Bean.currentMenu.setExistFlag("CLUB_TARGER_PROGRAM_PARTICIPANT", false);
		Bean.currentMenu.setExistFlag("CLUB_TARGET_PROGRAM_REFERRAL_SCHEME", false);
		Bean.currentMenu.setExistFlag("CLUB_TARGET_PROGRAM_FEE", false);
		Bean.currentMenu.setExistFlag("CLUB_TARGET_PROGRAM_SERVICE_PLACES", false);
		
		if ((Bean.currentMenu.isCurrentTab("CLUB_TARGER_PROGRAM_PARTICIPANT") ||
				Bean.currentMenu.isCurrentTab("CLUB_TARGET_PROGRAM_REFERRAL_SCHEME") ||
				Bean.currentMenu.isCurrentTab("CLUB_TARGET_PROGRAM_FEE") ||
				Bean.currentMenu.isCurrentTab("CLUB_TARGET_PROGRAM_SERVICE_PLACES"))) {
    		Bean.currentMenu.setFirstCurrentTab();
    		tab = Bean.currentMenu.getCurrentTab();
    		Bean.tabsHmSetValue(pageFormName, tab);
		}
	} else {
		Bean.currentMenu.setExistFlag("CLUB_TARGER_PROGRAM_PARTICIPANT", true);
		Bean.currentMenu.setExistFlag("CLUB_TARGET_PROGRAM_REFERRAL_SCHEME", true);
		Bean.currentMenu.setExistFlag("CLUB_TARGET_PROGRAM_FEE", true);
		Bean.currentMenu.setExistFlag("CLUB_TARGET_PROGRAM_SERVICE_PLACES", true);
	}
	
	String l_subprogram_page = Bean.getDecodeParam(parameters.get("subprogram_page"));
	Bean.pageCheck(pageFormName + tagSubprogram, l_subprogram_page);
	String l_subprogram_page_beg = Bean.getFirstRowNumber(pageFormName + tagSubprogram);
	String l_subprogram_page_end = Bean.getLastRowNumber(pageFormName + tagSubprogram);
	
	String subprogram_find 	= Bean.getDecodeParam(parameters.get("subprogram_find"));
	subprogram_find 	= Bean.checkFindString(pageFormName + tagSubprogramFind, subprogram_find, l_subprogram_page);
	
	String subprogram_pay_period 	= Bean.getDecodeParam(parameters.get("subprogram_pay_period"));
	subprogram_pay_period 	= Bean.checkFindString(pageFormName + tagSubprogramPayPeriod, subprogram_pay_period, l_subprogram_page);
	
	String l_participant_page = Bean.getDecodeParam(parameters.get("participant_page"));
	Bean.pageCheck(pageFormName + tagParticipant, l_participant_page);
	String l_participant_page_beg = Bean.getFirstRowNumber(pageFormName + tagParticipant);
	String l_participant_page_end = Bean.getLastRowNumber(pageFormName + tagParticipant);
	
	String participant_find 	= Bean.getDecodeParam(parameters.get("participant_find"));
	participant_find 	= Bean.checkFindString(pageFormName + tagParticipantFind, participant_find, l_participant_page);
	
	String l_trans_page = Bean.getDecodeParam(parameters.get("trans_page"));
	Bean.pageCheck(pageFormName + tagTrans, l_trans_page);
	String l_trans_page_beg = Bean.getFirstRowNumber(pageFormName + tagTrans);
	String l_trans_page_end = Bean.getLastRowNumber(pageFormName + tagTrans);

	String trans_find 	= Bean.getDecodeParam(parameters.get("trans_find"));
	trans_find 	= Bean.checkFindString(pageFormName + tagTransFind, trans_find, l_trans_page);

	String trans_pay_type 	= Bean.getDecodeParam(parameters.get("trans_pay_type"));
	trans_pay_type 	= Bean.checkFindString(pageFormName + tagTransPayType, trans_pay_type, l_trans_page);
	
	String l_rest_page = Bean.getDecodeParam(parameters.get("rest_page"));
	Bean.pageCheck(pageFormName + tagRest, l_rest_page);
	String l_rest_page_beg = Bean.getFirstRowNumber(pageFormName + tagRest);
	String l_rest_page_end = Bean.getLastRowNumber(pageFormName + tagRest);

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

	String l_service_place_page = Bean.getDecodeParam(parameters.get("service_place_page"));
	Bean.pageCheck(pageFormName + tagServicePlace, l_service_place_page);
	String l_service_place_page_beg = Bean.getFirstRowNumber(pageFormName + tagServicePlace);
	String l_service_place_page_end = Bean.getLastRowNumber(pageFormName + tagServicePlace);
	
	String service_place_find 	= Bean.getDecodeParam(parameters.get("service_place_find"));
	service_place_find 	= Bean.checkFindString(pageFormName + tagServicePlaceFind, service_place_find, l_service_place_page);
	
%>

	<table <%=Bean.getTableMenuParam() %>>
		<tr>
			<%= Bean.getPageHeader() %>

			<% if (Bean.currentMenu.isCurrentTabAndEditPermitted("CLUB_TARGET_PROGRAM_INFO")) { %>
			    <%= Bean.getMenuButton("ADD", "../crm/club/target_programupdate.jsp?id_target_prg=" + id + "&type=general&action=add2&process=no", "", "") %>
			    <%= Bean.getMenuButton("DELETE", "../crm/club/target_programupdate.jsp?id_target_prg=" + id + "&type=general&action=remove&process=yes", Bean.buttonXML.getfieldTransl("delete", false), program.getValue("ID_TARGET_PROGRAM")) %>
			<%  } %>
			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLUB_TARGER_PROGRAM_SUBPROGRAMS")) { %>
				<% if (Bean.currentMenu.isCurrentTabAndEditPermitted("CLUB_TARGER_PROGRAM_SUBPROGRAMS")) { %>
				    <%= Bean.getMenuButton("ADD", "../crm/club/target_programupdate.jsp?id_target_prg=" + id + "&type=general&action=addsubprogram&process=no", "", "") %>
				<%  } %>
			    <!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagSubprogram, "../crm/club/target_programspecs.jsp?id=" + id + "&tab="+Bean.currentMenu.getTabID("CLUB_TARGER_PROGRAM_SUBPROGRAMS")+"&", "subprogram_page") %>
			<% }%>
			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLUB_TARGET_PROGRAM_SERVICE_PLACES")) { %>
				<% if (Bean.currentMenu.isCurrentTabAndEditPermitted("CLUB_TARGET_PROGRAM_SERVICE_PLACES")) { %>
				    <%= Bean.getMenuButtonBase("ADD", "../crm/club/target_programupdate.jsp?id_target_prg=" + id + "&type=service_place&action=add&process=no", "", "", "", "div_data_detail") %>
				<%  } %>
			    <!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagServicePlace, "../crm/club/target_programspecs.jsp?id=" + id + "&tab="+Bean.currentMenu.getTabID("CLUB_TARGET_PROGRAM_SERVICE_PLACES")+"&", "service_place_page") %>
			<% }%>
			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLUB_TARGET_PROGRAM_FEE")) { %>
			    <!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagTrans, "../crm/club/target_programspecs.jsp?id=" + id + "&tab="+Bean.currentMenu.getTabID("CLUB_TARGET_PROGRAM_FEE")+"&", "trans_page") %>
			<% }%>
			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLUB_TARGER_PROGRAM_PARTICIPANT")) { %>
			    <% if (Bean.currentMenu.isCurrentTabAndEditPermitted("CLUB_TARGER_PROGRAM_PARTICIPANT")) { %>
				    <%= Bean.getMenuButtonBase("ADD", "../crm/club/target_programupdate.jsp?id_target_prg=" + id + "&type=participant&action=add&process=no", "", "", "", "div_data_detail") %>
				<%  } %>
			    <!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagParticipant, "../crm/club/target_programspecs.jsp?id=" + id + "&tab="+Bean.currentMenu.getTabID("CLUB_TARGER_PROGRAM_PARTICIPANT")+"&", "participant_page") %>
			<% }%>
			
			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLUB_TARGET_PROGRAM_RESTS")) { %>
				<% if (Bean.currentMenu.isCurrentTabAndEditPermitted("CLUB_TARGET_PROGRAM_RESTS")) {	%>
					<%= Bean.getMenuButton("RUN", "../crm/club/target_programupdate.jsp?id_target_prg="+ id + "&type=rests&action=run&process=yes", Bean.clubfundXML.getfieldTransl("h_calc_rests", false), "", Bean.clubfundXML.getfieldTransl("h_calc_rests", false)) %>
				<% } %>
			    <!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagRest, "../crm/club/target_programspecs.jsp?id=" + id + "&tab="+Bean.currentMenu.getTabID("CLUB_FUND_RESTS")+"&", "rest_page") %>
			<% } %>

			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLUB_TARGET_PROGRAM_REFERRAL_SCHEME")) { %>
				<% if (Bean.currentMenu.isCurrentTabAndEditPermitted("CLUB_TARGET_PROGRAM_REFERRAL_SCHEME")) { %>
				    <%= Bean.getMenuButtonBase("ADD", "../crm/club/referral_schemeupdate.jsp?back_type=TARGET_PROGRAM&type=general&id=" + id + "&action=add&process=no", "", "", "", "div_data_detail") %>
				<%  } %>
			    <!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagReferralScheme, "../crm/club/target_programspecs.jsp?id=" + id + "&tab="+Bean.currentMenu.getTabID("CLUB_TARGET_PROGRAM_REFERRAL_SCHEME")+"&", "referral_scheme_page") %>
			<% }%>
		</tr>
	</table>
	<table <%=Bean.getTableMenuParam() %>>
		<%= Bean.getDetailCaption(program.getValue("ID_TARGET_PRG") + " - " + program.getValue("NAME_TARGET_PRG")) %>
		<tr>
			<td>
				<!-- Выводим перечень закладок -->
				<%= Bean.currentMenu.getFormTabScheeds(tab, "../crm/club/target_programspecs.jsp?id=" + id) %>
			</td>
		</tr>
	</table>
</div>
<div id="div_data">
<div id="div_data_detail">
<%
if (Bean.currentMenu.isCurrentTabAndPreviewPermitted("CLUB_TARGET_PROGRAM_INFO")) {
 %>	 
	<form>
	<table <%=Bean.getTableDetailParam() %>>
	    <tr>
			<td><%= Bean.clubfundXML.getfieldTransl("name_target_prg", true) %></td> <td><input type="text" name="name_target_prg" size="60" value="<%=program.getValue("NAME_TARGET_PRG") %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.clubXML.getfieldTransl("club", false) %>
				<%= Bean.getGoToClubLink(program.getValue("ID_CLUB")) %>
			</td><td><input type="text" name="name_club" size="30" value="<%= Bean.getClubShortName(program.getValue("ID_CLUB")) %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
	    <tr>
			<td><%= Bean.clubfundXML.getfieldTransl("sname_target_prg", true) %></td> <td><input type="text" name="sname_target_prg" size="60" value="<%=program.getValue("NAME_TARGET_PRG") %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.clubfundXML.getfieldTransl("target_program_date_beg", true) %></td> <td><input type="text" name="date_beg" size="10" value="<%=program.getValue("DATE_BEG_FRMT") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
	    <tr>
			<% if (!Bean.isEmpty(program.getValue("ID_TARGET_PRG_PARENT"))) { %>
			<td><%= Bean.clubfundXML.getfieldTransl("id_target_prg_parent", false) %>
				<%= Bean.getGoToTargetProgramLink(program.getValue("ID_TARGET_PRG_PARENT")) %>
			</td> <td><input type="text" name="name_target_prg_parent" size="60" value="<%=Bean.getTargetPrgName(program.getValue("ID_TARGET_PRG_PARENT")) %>" readonly="readonly" class="inputfield-ro"></td>
			<% } else { %>
			<td colspan="2">&nbsp;</td>
			<% } %>
			<td><%= Bean.clubfundXML.getfieldTransl("target_program_date_end", false) %></td> <td><input type="text" name="date_end" size="10" value="<%=program.getValue("DATE_END_FRMT") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
	    <tr>
			<td><%= Bean.clubfundXML.getfieldTransl("initiator_target_prg", false) %>
				<%= Bean.getGoToNatPrsLink(program.getValue("ID_NAT_PRS_INITIATOR")) %>
			</td>
			<td><input type="text" name="name_nat_prs_initiator" size="60" value="<%=program.getValue("NAME_NAT_PRS_INITIATOR") %>" readonly="readonly" class="inputfield-ro"></td>
			<td colspan="2" rowspan="8">
				<div id="div_photo" class="photo_rect">
					<% if (!(program.getValue("IMAGE_TARGET_PRG") == null || "".equalsIgnoreCase(program.getValue("IMAGE_TARGET_PRG")))) { %>
						<img class="photo_image" id="image-0" height="150" src="../TargetProgramPicture?id_target_prg=<%=program.getValue("ID_TARGET_PRG") %>&noCache=<%=(new Date().getTime()) + Math.random() %>">
					<% } else { %>
						<p><%=Bean.clubfundXML.getfieldTransl("titl_picture_not_found", false) %></p>
					<% } %>
				</div>
			</td>
		</tr>
	    <tr>
			<td><%= Bean.clubfundXML.getfieldTransl("administrator_target_prg", false) %>
				<%= Bean.getGoToNatPrsLink(program.getValue("ID_NAT_PRS_ADMINISTRATOR")) %>
			</td>
			<td><input type="text" name="name_nat_prs_administrator" size="60" value="<%=program.getValue("NAME_NAT_PRS_ADMINISTRATOR") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td><%= Bean.clubfundXML.getfieldTransl("sname_jur_prs", false) %>
				<%= Bean.getGoToJurPrsHyperLink(program.getValue("ID_JUR_PRS")) %>
			</td>
			<td><input type="text" name="sname_jur_prs" size="60" value="<%=program.getValue("SNAME_JUR_PRS") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td><%= Bean.clubcardXML.getfieldTransl("id_doc", false) %>
				<%= Bean.getGoToDocLink(program.getValue("ID_DOC")) %>
			</td>
			<td><input type="text" name="name_doc" size="60" value="<%=Bean.getDocName(program.getValue("ID_DOC")) %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
	    <tr>
			<td rowspan="3"><%= Bean.clubfundXML.getfieldTransl("desc_target_prg", false) %></td> <td rowspan="3"><textarea name="desc_target_prg" cols="57" rows="3" readonly="readonly" class="inputfield-ro"><%= program.getValue("DESC_TARGET_PRG") %></textarea></td>
		</tr>
   		<tr>
			<td colspan="2">&nbsp;</td>
   		</tr>
   		<tr>
			<td colspan="2">&nbsp;</td>
   		</tr>
   		<tr>
			<td colspan="2">&nbsp;</td>
   		</tr>
		<% if ("0".equalsIgnoreCase(program.getValue("CHILD_COUNT"))) { %>
	    <tr><td colspan="4">&nbsp;</td></tr>
	    <tr>
			<td colspan="2" class="top_line"><b><%= Bean.clubfundXML.getfieldTransl("target_program_fees", false) %></b></td>
			<td colspan="2" class="top_line"><b><%= Bean.clubfundXML.getfieldTransl("target_program_subscribe", false) %></b></td>
		</tr>
	    <tr>
			<td><%= Bean.clubfundXML.getfieldTransl("cd_currency", false) %></td> <td><input type="text" name="name_currency" size="15" value="<%=program.getValue("NAME_CURRENCY") %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.clubfundXML.getfieldTransl("need_subscribe", false) %></td> <td><input type="text" name="need_subscribe" size="10" value="<%=Bean.getMeaningFoCodeValue("YES_NO", program.getValue("NEED_SUBSCRIBE")) %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
	    <tr>
			<td><%= Bean.clubfundXML.getfieldTransl("entrance_fee", false) %></td> <td><input type="text" name="entrance_fee" size="15" value="<%=program.getValue("ENTRANCE_FEE_FRMT") %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.clubfundXML.getfieldTransl("need_administrator_confirm", false) %></td> <td><input type="text" name="need_administrator_confirm" size="10" value="<%=Bean.getMeaningFoCodeValue("YES_NO", program.getValue("NEED_ADMINISTRATOR_CONFIRM")) %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
	    <tr>
			<td><%= Bean.clubfundXML.getfieldTransl("membership_fee", false) %></td> <td><input type="text" name="membership_fee" size="15" value="<%=program.getValue("MEMBERSHIP_FEE_FRMT") %>" readonly="readonly" class="inputfield-ro"> <%= Bean.clubfundXML.getfieldTransl("title_membership_for", false) %> <input type="text" name="membership_period_value" size="5" value="<%=program.getValue("MEMBERSHIP_PERIOD_VALUE") %>" readonly="readonly" class="inputfield-ro"> <%= Bean.clubfundXML.getfieldTransl("title_membership_many_days", false) %></td>
			<td colspan="2" class="top_line_gray"><b><%= Bean.clubfundXML.getfieldTransl("title_others", false) %></b></td>
		</tr>
	    <tr>
			<td><%= Bean.clubfundXML.getfieldTransl("target_prg_pay_period", false) %></td> <td><input type="text" name="target_prg_pay_period" size="60" value="<%=program.getValue("NAME_TARGET_PRG_PAY_PERIOD") %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.clubfundXML.getfieldTransl("name_target_prg_in_sms", false) %></td> <td><input type="text" name="name_target_prg_in_sms" size="30" value="<%=program.getValue("NAME_TARGET_PRG_IN_SMS") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
	    <tr>
			<% if ("IRREGULAR".equalsIgnoreCase(program.getValue("CD_TARGET_PRG_PAY_PERIOD"))) { %>
				<td><span id="part1Title"><%= Bean.clubfundXML.getfieldTransl("min_pay_amount", false) %></span></td> <td><span id="part1Value"><input type="text" name="min_pay_amount" size="15" value="<%=program.getValue("MIN_PAY_AMOUNT") %>" readonly="readonly" class="inputfield-ro"></span></td>
			<%} else if ("YEAR".equalsIgnoreCase(program.getValue("CD_TARGET_PRG_PAY_PERIOD"))) {%>
				<td><span id="part1Title"><%= Bean.clubfundXML.getfieldTransl("pay_amount", false) %></span></td> <td><span id="part1Value"><input type="text" name="pay_amount" size="15" value="<%=program.getValue("PAY_AMOUNT_FRMT") %>" readonly="readonly" class="inputfield-ro"> <%= Bean.clubfundXML.getfieldTransl("title_membership_years", false) %></span></td>
			<%} else if ("MONTH".equalsIgnoreCase(program.getValue("CD_TARGET_PRG_PAY_PERIOD"))) {%>
				<td><span id="part1Title"><%= Bean.clubfundXML.getfieldTransl("pay_amount", false) %></span></td> <td><span id="part1Value"><input type="text" name="pay_amount" size="15" value="<%=program.getValue("PAY_AMOUNT_FRMT") %>" readonly="readonly" class="inputfield-ro"> <%= Bean.clubfundXML.getfieldTransl("title_membership_months", false) %></span></td>
			<%} else if ("WEEK".equalsIgnoreCase(program.getValue("CD_TARGET_PRG_PAY_PERIOD"))) {%>
				<td><span id="part1Title"><%= Bean.clubfundXML.getfieldTransl("pay_amount", false) %></span></td> <td><span id="part1Value"><input type="text" name="pay_amount" size="15" value="<%=program.getValue("PAY_AMOUNT_FRMT") %>" readonly="readonly" class="inputfield-ro"> <%= Bean.clubfundXML.getfieldTransl("title_membership_weeks", false) %></span></td>
			<%} else if ("DAY".equalsIgnoreCase(program.getValue("CD_TARGET_PRG_PAY_PERIOD"))) {%>
				<td><span id="part1Title"><%= Bean.clubfundXML.getfieldTransl("pay_amount", false) %></span></td> <td><span id="part1Value"><input type="text" name="pay_amount" size="15" value="<%=program.getValue("PAY_AMOUNT_FRMT") %>" readonly="readonly" class="inputfield-ro"> <%= Bean.clubfundXML.getfieldTransl("title_membership_days", false) %></span></td>
			<%} else if ("HOUR".equalsIgnoreCase(program.getValue("CD_TARGET_PRG_PAY_PERIOD"))) {%>
				<td><span id="part1Title"><%= Bean.clubfundXML.getfieldTransl("pay_amount", false) %></span></td> <td><span id="part1Value"><input type="text" name="pay_amount" size="15" value="<%=program.getValue("PAY_AMOUNT_FRMT") %>" readonly="readonly" class="inputfield-ro"> <%= Bean.clubfundXML.getfieldTransl("title_membership_hours", false) %></span></td>
			<%} else if ("STUDY_COUNT".equalsIgnoreCase(program.getValue("CD_TARGET_PRG_PAY_PERIOD"))) {%>
				<td><span id="part1Title"><%= Bean.clubfundXML.getfieldTransl("pay_amount", false) %></span></td> <td><span id="part1Value"><input type="text" name="pay_amount" size="15" value="<%=program.getValue("PAY_AMOUNT_FRMT") %>" readonly="readonly" class="inputfield-ro"> <%= Bean.clubfundXML.getfieldTransl("title_membership_for", false) %> <input type="text" name="pay_count" size="5" value="<%=program.getValue("PAY_COUNT") %>" readonly="readonly" class="inputfield-ro"> <%= Bean.clubfundXML.getfieldTransl("title_membership_studies", false) %></span></td>
			<%} else {%>
				<td><span id="part1Title">&nbsp;</span></td> <td><span id="part1Value">&nbsp;</span></td>
			<% } %>
		</tr>
		<% } %>
		<%=	Bean.getIdCreationAndMoficationRecordFields(
				program.getValue("ID_TARGET_PRG"),
				program.getValue(Bean.getCreationDateFieldName()),
				program.getValue("CREATED_BY"),
				program.getValue(Bean.getLastUpdateDateFieldName()),
				program.getValue("LAST_UPDATE_BY")
			) %>
		<tr>
			<td colspan="6" align="center">
				<%=Bean.getGoBackButton("../crm/club/target_program.jsp") %>
			</td>
		</tr>
	</table>
	</form>

<% } else if (Bean.currentMenu.isCurrentTabAndEditPermitted("CLUB_TARGET_PROGRAM_INFO")) { %>
	
		<script>
		var formValidateData = new Array (
				new Array ('name_target_prg', 'varchar2', 1),
				new Array ('sname_target_prg', 'varchar2', 1),
				new Array ('name_nat_prs_initiator', 'varchar2', 1),
				new Array ('name_nat_prs_administrator', 'varchar2', 1),
				new Array ('name_jur_prs', 'varchar2', 1),
				new Array ('date_beg', 'varchar2', 1)
			);
			var formPayDetail = new Array (
				new Array ('cd_currency', 'varchar2', 1),
				new Array ('cd_target_prg_pay_period', 'varchar2', 1)
			);
			var formPayAmount = new Array (
				new Array ('pay_amount', 'number', 1)
			);
			var formPayCount = new Array (
				new Array ('pay_count', 'number', 1)
			);
			
			function myValidateForm() {

				<% if ("0".equalsIgnoreCase(program.getValue("CHILD_COUNT"))) { %>
				formValidateData = formValidateData.concat(formPayDetail);
				var cd_target_prg_pay_period = document.getElementById('cd_target_prg_pay_period');
				if (cd_target_prg_pay_period.value!='IRREGULAR' && cd_target_prg_pay_period.value!='') {
					formValidateData = formValidateData.concat(formPayAmount);
				}
				if (cd_target_prg_pay_period.value=='STUDY_COUNT') {
					formValidateData = formValidateData.concat(formPayCount);
				}
				<% } %>
				return validateForm(formValidateData);
			}

			function del_photo() {
				var msg='<%=Bean.buttonXML.getfieldTransl("photo_delete", false) %>?';
				var res=window.confirm(msg);
				if (res) {
					img = document.getElementById('image-0');
					img.src = '';
					div_photo = document.getElementById('div_photo');
					div_photo.innerHTML = '<p><%=Bean.clubfundXML.getfieldTransl("titl_picture_load", false) %></p><input type="hidden" name="id_loaded_file" value="-1"><input type="file" class="photo_file" name="client_photo" size="50" value="" accept="image/*" capture="camera" onchange="load_photo()">';
					//post_form('service/load_photo.jsp','delPhoto','div_photo');
				}
			}
			function load_photo() {
				action = document.getElementById('action');
				action.value = 'load_photo';
				post_form('../crm/club/target_programupdate.jsp','updateForm7','div_photo');
				action.value = 'edit';
				//alert('action.value='+action.value);
			}
			function changePayPeriod(val) {
				part1Title = document.getElementById('part1Title');
				part1Value = document.getElementById('part1Value');
				if (val=='IRREGULAR') {
					part1Title.innerHTML='<%= Bean.clubfundXML.getfieldTransl("min_pay_amount", false) %>';
					part1Value.innerHTML='<input type="text" name="min_pay_amount" size="15" value="" class="inputfield">';
				} else if (val=='YEAR') {
					part1Title.innerHTML='<%= Bean.clubfundXML.getfieldTransl("pay_amount", false) %>';
					part1Value.innerHTML='<input type="text" name="pay_amount" size="15" value="" class="inputfield"> <%= Bean.clubfundXML.getfieldTransl("title_membership_years", false) %>';
				} else if (val=='MONTH') {
					part1Title.innerHTML='<%= Bean.clubfundXML.getfieldTransl("pay_amount", false) %>';
					part1Value.innerHTML='<input type="text" name="pay_amount" size="15" value="" class="inputfield"> <%= Bean.clubfundXML.getfieldTransl("title_membership_months", false) %>';
				} else if (val=='WEEK') {
					part1Title.innerHTML='<%= Bean.clubfundXML.getfieldTransl("pay_amount", false) %>';
					part1Value.innerHTML='<input type="text" name="pay_amount" size="15" value="" class="inputfield"> <%= Bean.clubfundXML.getfieldTransl("title_membership_weeks", false) %>';
				} else if (val=='DAY') {
					part1Title.innerHTML='<%= Bean.clubfundXML.getfieldTransl("pay_amount", false) %>';
					part1Value.innerHTML='<input type="text" name="pay_amount" size="15" value="" class="inputfield"> <%= Bean.clubfundXML.getfieldTransl("title_membership_days", false) %>';
				} else if (val=='HOUR') {
					part1Title.innerHTML='<%= Bean.clubfundXML.getfieldTransl("pay_amount", false) %>';
					part1Value.innerHTML='<input type="text" name="pay_amount" size="15" value="" class="inputfield"> <%= Bean.clubfundXML.getfieldTransl("title_membership_hours", false) %>';
				} else if (val=='STUDY_COUNT') {
					part1Title.innerHTML='<%= Bean.clubfundXML.getfieldTransl("pay_amount", false) %>';
					part1Value.innerHTML='<input type="text" name="pay_amount" size="15" value="" class="inputfield"> <%= Bean.clubfundXML.getfieldTransl("title_membership_for", false) %> <input type="text" name="pay_count" size="5" value="" class="inputfield"> <%= Bean.clubfundXML.getfieldTransl("title_membership_studies", false) %>';
				} else {
					part1Title.innerHTML='&nbsp;';
					part1Value.innerHTML='&nbsp;';
				}
			}
		</script>
	<div id="div_detail">
    <form name="updateForm7" id="updateForm7" accept-charset="UTF-8" method="POST" enctype="multipart/form-data">
    	<input type="hidden" name="id_target_prg" value="<%=id %>">
	    <input type="hidden" name="LUD" value="<%=program.getValue("LUD") %>">
	    <input type="hidden" name="action" id="action" value="edit">
    	<input type="hidden" name="process" value="yes">
    	<input type="hidden" name="type" value="general">
	<table <%=Bean.getTableDetailParam() %> style="border: 1px solid">
	    <tr>
			<td><%= Bean.clubfundXML.getfieldTransl("name_target_prg", true) %></td> <td><input type="text" name="name_target_prg" size="60" value="<%=program.getValue("NAME_TARGET_PRG") %>" class="inputfield"></td>
			<td><%= Bean.clubXML.getfieldTransl("club", false) %>
				<%= Bean.getGoToClubLink(program.getValue("ID_CLUB")) %>
			</td><td><input type="text" name="name_club" size="30" value="<%= Bean.getClubShortName(program.getValue("ID_CLUB")) %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
	    <tr>
			<td><%= Bean.clubfundXML.getfieldTransl("sname_target_prg", true) %></td> <td><input type="text" name="sname_target_prg" size="60" value="<%=program.getValue("SNAME_TARGET_PRG") %>" class="inputfield"></td>
			<td><%= Bean.clubfundXML.getfieldTransl("target_program_date_beg", true) %></td> <td><%=Bean.getCalendarInputField("date_beg", program.getValue("DATE_BEG_FRMT"), "10") %></td>
		</tr>
	    <tr>
			<% if (!Bean.isEmpty(program.getValue("ID_TARGET_PRG_PARENT"))) { %>
			<td><%= Bean.clubfundXML.getfieldTransl("id_target_prg_parent", false) %>
				<%= Bean.getGoToTargetProgramLink(program.getValue("ID_TARGET_PRG_PARENT")) %>
			</td> <td><input type="text" name="name_target_prg_parent" size="60" value="<%=Bean.getTargetPrgName(program.getValue("ID_TARGET_PRG_PARENT")) %>" readonly="readonly" class="inputfield-ro"></td>
			<% } else { %>
			<td colspan="2">&nbsp;</td>
			<% } %>
			<td><%= Bean.clubfundXML.getfieldTransl("target_program_date_end", false) %></td> <td><%=Bean.getCalendarInputField("date_end", program.getValue("DATE_END_FRMT"), "10") %></td>
		</tr>
	    <tr>
			<td><%= Bean.clubfundXML.getfieldTransl("initiator_target_prg", true) %>
				<%= Bean.getGoToNatPrsLink(program.getValue("ID_NAT_PRS_INITIATOR")) %>
			</td>
			<td>
				<%=Bean.getWindowFindNatPrs("nat_prs_initiator", program.getValue("ID_NAT_PRS_INITIATOR"), program.getValue("NAME_NAT_PRS_INITIATOR"), "50") %>
			</td>
			<td colspan="2" rowspan="8">
				<div id="div_photo" class="photo_rect">
					<% if (!(program.getValue("IMAGE_TARGET_PRG") == null || "".equalsIgnoreCase(program.getValue("IMAGE_TARGET_PRG")))) { %>
						<div onclick="del_photo()" title="<%=Bean.buttonXML.getfieldTransl("photo_delete", false) %>" class="del" id="delgimage-0"></div>
						<div title="<%=Bean.buttonXML.getfieldTransl("photo_edit", false) %>" class="edt" id="edtgimage-0">
							<input type="file" title="Изменить фото" onchange="load_photo()" capture="camera" accept="image/*" value="" size="50" name="client_photo" class="photo_file">
						</div>
						<img class="photo_image" id="image-0" height="150" src="../TargetProgramPicture?id_target_prg=<%=program.getValue("ID_TARGET_PRG") %>&noCache=<%=(new Date().getTime()) + Math.random() %>">
					<% } else { %>
						<p><%=Bean.clubfundXML.getfieldTransl("titl_picture_load", false) %></p>
						<input type="file" class="photo_file" name="client_photo" size="50" value="" accept="image/*" capture="camera" onchange="load_photo()">
					<% } %>
				</div>
			</td>
		</tr>
	    <tr>
			<td><%= Bean.clubfundXML.getfieldTransl("administrator_target_prg", true) %>
				<%= Bean.getGoToNatPrsLink(program.getValue("ID_NAT_PRS_ADMINISTRATOR")) %>
			</td>
			<td>
				<%=Bean.getWindowFindNatPrs("nat_prs_administrator", program.getValue("ID_NAT_PRS_ADMINISTRATOR"), program.getValue("NAME_NAT_PRS_ADMINISTRATOR"), "50") %>
			</td>
		</tr>
		<tr>
			<td><%= Bean.clubfundXML.getfieldTransl("sname_jur_prs", true) %>
				<%= Bean.getGoToJurPrsHyperLink(program.getValue("ID_JUR_PRS")) %>
			</td>
			<td>
				<%=Bean.getWindowFindJurPrs("jur_prs", program.getValue("ID_JUR_PRS"), program.getValue("NAME_JUR_PRS"), "50") %>
			</td>
		</tr>
   		<tr>
			<td><%= Bean.clubcardXML.getfieldTransl("id_doc", false) %>
				<%= Bean.getGoToDocLink(program.getValue("ID_DOC")) %>
			</td> 
			<td>
				<%=Bean.getWindowDocuments("doc", program.getValue("ID_DOC"), "50") %>
			</td>
   		</tr>
	    <tr>
			<td rowspan="3"><%= Bean.clubfundXML.getfieldTransl("desc_target_prg", false) %></td> <td rowspan="3"><textarea name="desc_target_prg" cols="57" rows="3" class="inputfield"><%= program.getValue("DESC_TARGET_PRG") %></textarea></td>
		</tr>
   		<tr>
			<td colspan="2">&nbsp;</td>
   		</tr>
   		<tr>
			<td colspan="2">&nbsp;</td>
   		</tr>
		<% if ("0".equalsIgnoreCase(program.getValue("CHILD_COUNT"))) { %>
	    <tr><td colspan="4">&nbsp;</td></tr>
	    <tr>
			<td colspan="2" class="top_line"><b><%= Bean.clubfundXML.getfieldTransl("target_program_fees", false) %></b></td>
			<td colspan="2" class="top_line"><b><%= Bean.clubfundXML.getfieldTransl("target_program_subscribe", false) %></b></td>
		</tr>
	    <tr>
			<td><%= Bean.clubfundXML.getfieldTransl("cd_currency", true) %></td> <td><select name="cd_currency" class="inputfield" ><%=Bean.getCurrencyOptions(program.getValue("CD_CURRENCY"), true) %></select></td>
			<td><%= Bean.clubfundXML.getfieldTransl("need_subscribe", false) %></td> <td><select name="need_subscribe" class="inputfield" ><%=Bean.getMeaningFromLookupNameOptions("YES_NO", program.getValue("NEED_SUBSCRIBE"), true) %></select></td>
		</tr>
	    <tr>
			<td><%= Bean.clubfundXML.getfieldTransl("entrance_fee", false) %></td> <td><input type="text" name="entrance_fee" size="15" value="<%=program.getValue("ENTRANCE_FEE_FRMT") %>" class="inputfield"></td>
			<td><%= Bean.clubfundXML.getfieldTransl("need_administrator_confirm", false) %></td> <td><select name="need_administrator_confirm" class="inputfield" ><%=Bean.getMeaningFromLookupNameOptions("YES_NO", program.getValue("NEED_ADMINISTRATOR_CONFIRM"), true) %></select></td>
		</tr>
	    <tr>
			<td><%= Bean.clubfundXML.getfieldTransl("membership_fee", false) %></td> <td><input type="text" name="membership_fee" size="15" value="<%=program.getValue("MEMBERSHIP_FEE_FRMT") %>" class="inputfield"> <%= Bean.clubfundXML.getfieldTransl("title_membership_for", false) %> <input type="text" name="membership_period_value" size="5" value="<%=program.getValue("MEMBERSHIP_PERIOD_VALUE") %>" class="inputfield"> <%= Bean.clubfundXML.getfieldTransl("title_membership_many_days", false) %></td>
			<td colspan="2" class="top_line_gray"><b><%= Bean.clubfundXML.getfieldTransl("title_others", false) %></b></td>
		</tr>
	    <tr>
			<td><%= Bean.clubfundXML.getfieldTransl("target_prg_pay_period", true) %></td> <td><select name="cd_target_prg_pay_period" id="cd_target_prg_pay_period" class="inputfield" onchange="changePayPeriod(this.value)"><%=Bean.getTargetPrgPayPeriodOptions(program.getValue("CD_TARGET_PRG_PAY_PERIOD"), true) %></select></td>
			<td><%= Bean.clubfundXML.getfieldTransl("name_target_prg_in_sms", false) %></td> <td><input type="text" name="name_target_prg_in_sms" size="30" value="<%=program.getValue("NAME_TARGET_PRG_IN_SMS") %>" class="inputfield"></td>
		</tr>
	    <tr>
			<% if ("IRREGULAR".equalsIgnoreCase(program.getValue("CD_TARGET_PRG_PAY_PERIOD"))) { %>
				<td><span id="part1Title"><%= Bean.clubfundXML.getfieldTransl("min_pay_amount", false) %></span></td> <td><span id="part1Value"><input type="text" name="min_pay_amount" size="15" value="<%=program.getValue("MIN_PAY_AMOUNT") %>" class="inputfield"></span></td>
			<%} else if ("YEAR".equalsIgnoreCase(program.getValue("CD_TARGET_PRG_PAY_PERIOD"))) {%>
				<td><span id="part1Title"><%= Bean.clubfundXML.getfieldTransl("pay_amount", false) %></span></td> <td><span id="part1Value"><input type="text" name="pay_amount" size="15" value="<%=program.getValue("PAY_AMOUNT_FRMT") %>" class="inputfield"> <%= Bean.clubfundXML.getfieldTransl("title_membership_years", false) %></span></td>
			<%} else if ("MONTH".equalsIgnoreCase(program.getValue("CD_TARGET_PRG_PAY_PERIOD"))) {%>
				<td><span id="part1Title"><%= Bean.clubfundXML.getfieldTransl("pay_amount", false) %></span></td> <td><span id="part1Value"><input type="text" name="pay_amount" size="15" value="<%=program.getValue("PAY_AMOUNT_FRMT") %>" class="inputfield"> <%= Bean.clubfundXML.getfieldTransl("title_membership_months", false) %></span></td>
			<%} else if ("WEEK".equalsIgnoreCase(program.getValue("CD_TARGET_PRG_PAY_PERIOD"))) {%>
				<td><span id="part1Title"><%= Bean.clubfundXML.getfieldTransl("pay_amount", false) %></span></td> <td><span id="part1Value"><input type="text" name="pay_amount" size="15" value="<%=program.getValue("PAY_AMOUNT_FRMT") %>" class="inputfield"> <%= Bean.clubfundXML.getfieldTransl("title_membership_weeks", false) %></span></td>
			<%} else if ("DAY".equalsIgnoreCase(program.getValue("CD_TARGET_PRG_PAY_PERIOD"))) {%>
				<td><span id="part1Title"><%= Bean.clubfundXML.getfieldTransl("pay_amount", false) %></span></td> <td><span id="part1Value"><input type="text" name="pay_amount" size="15" value="<%=program.getValue("PAY_AMOUNT_FRMT") %>" class="inputfield"> <%= Bean.clubfundXML.getfieldTransl("title_membership_days", false) %></span></td>
			<%} else if ("HOUR".equalsIgnoreCase(program.getValue("CD_TARGET_PRG_PAY_PERIOD"))) {%>
				<td><span id="part1Title"><%= Bean.clubfundXML.getfieldTransl("pay_amount", false) %></span></td> <td><span id="part1Value"><input type="text" name="pay_amount" size="15" value="<%=program.getValue("PAY_AMOUNT_FRMT") %>" class="inputfield"> <%= Bean.clubfundXML.getfieldTransl("title_membership_hours", false) %></span></td>
			<%} else if ("STUDY_COUNT".equalsIgnoreCase(program.getValue("CD_TARGET_PRG_PAY_PERIOD"))) {%>
				<td><span id="part1Title"><%= Bean.clubfundXML.getfieldTransl("pay_amount", false) %></span></td> <td><span id="part1Value"><input type="text" name="pay_amount" size="15" value="<%=program.getValue("PAY_AMOUNT_FRMT") %>" class="inputfield"> <%= Bean.clubfundXML.getfieldTransl("title_membership_for", false) %> <input type="text" name="pay_count" size="5" value="<%=program.getValue("PAY_COUNT") %>" class="inputfield"> <%= Bean.clubfundXML.getfieldTransl("title_membership_studies", false) %></span></td>
			<%} else {%>
				<td><span id="part1Title">&nbsp;</span></td> <td><span id="part1Value">&nbsp;</span></td>
			<% } %>
		</tr>
		<% } %>
	    <%=	Bean.getIdCreationAndMoficationRecordFields(
	    		program.getValue("ID_TARGET_PRG"),
				program.getValue(Bean.getCreationDateFieldName()),
				program.getValue("CREATED_BY"),
				program.getValue(Bean.getLastUpdateDateFieldName()),
				program.getValue("LAST_UPDATE_BY")
			) %>
		<tr>
			<td colspan="6" align="center">
				<%=Bean.getSubmitButtonMultiPart4("../crm/club/target_programupdate.jsp","apply","updateForm7","div_detail", "") %>
				<%=Bean.getResetButton() %>
				<%=Bean.getGoBackButton("../crm/club/target_program.jsp") %>
			</td>
		</tr>
	</table>

	</form>
	</div>
	<%= Bean.getCalendarScript("date_beg", false) %>
	<%= Bean.getCalendarScript("date_end", false) %>
<% } %>

<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLUB_TARGER_PROGRAM_SUBPROGRAMS")) {%>
		<table <%=Bean.getTableBottomFilter() %>><tbody>
		<tr>
			<%=Bean.getFindHTML("subprogram_find", subprogram_find, "../crm/club/target_programspecs.jsp?id=" + id + "&subprogram_page=1") %>

		<%=Bean.getSelectOnChangeBeginHTML("subprogram_pay_period", "../crm/club/target_programspecs.jsp?id=" + id + "&subprogram_page=1", Bean.clubfundXML.getfieldTransl("target_prg_pay_period", false)) %>
			<%= Bean.getTargetPrgPayPeriodOptions(subprogram_pay_period, true) %>
		<%=Bean.getSelectOnChangeEndHTML() %>
	
		</tr>
		</tbody>
		</table>
	<%= program.getSubprogramsHTML(subprogram_find, subprogram_pay_period, l_subprogram_page_beg, l_subprogram_page_end)  %>
<%} %>

<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLUB_TARGET_PROGRAM_SERVICE_PLACES")) {%>
		<table <%=Bean.getTableBottomFilter() %>><tbody>
		<tr>
			<%=Bean.getFindHTML("service_place_find", service_place_find, "../crm/club/target_programspecs.jsp?id=" + id + "&service_place_page=1") %>

			<td>&nbsp;</td>
		</tr>
		</tbody>
		</table>
	<%= program.getServicePlaces(service_place_find, l_service_place_page_beg, l_service_place_page_end)%> 
<%} %>

<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLUB_TARGET_PROGRAM_FEE")) {%>
		<table <%=Bean.getTableBottomFilter() %>><tbody>
		<tr>
			<%=Bean.getFindHTML("trans_find", trans_find, "../crm/club/target_programspecs.jsp?id=" + id + "&trans_page=1") %>


		<%=Bean.getSelectOnChangeBeginHTML("trans_pay_type", "../crm/club/target_programspecs.jsp?id=" + id + "&trans_page=1", Bean.transactionXML.getfieldTransl("cheque_type", false)) %>
			<%= Bean.getTransPayTypeOptions(trans_pay_type, true) %>
		<%=Bean.getSelectOnChangeEndHTML() %>
	
		</tr>
		</tbody>
		</table>

	<%= program.getTransactionsHTML(trans_find, "12", trans_pay_type, "0", l_trans_page_beg, l_trans_page_end) %>
<%} %>

<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLUB_TARGER_PROGRAM_PARTICIPANT")) {%>
		<table <%=Bean.getTableBottomFilter() %>><tbody>
		<tr>
			<%=Bean.getFindHTML("participant_find", participant_find, "../crm/club/target_programspecs.jsp?id=" + id + "&participant_page=1") %>

			<td>&nbsp;</td>
		</tr>
		</tbody>
		</table>
	<%=program.getParticipantsHTML(participant_find, l_participant_page_beg, l_participant_page_end) %>
<%} %>

<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLUB_TARGET_PROGRAM_RESTS")) {%>
	<%
	String tagFindRest = "_FIND_REST";
    String tagFindRestBegin = "_FIND_REST_BEGIN";
    String tagFindRestEnd = "_FIND_REST_END";
    
	String find_rest	 	= Bean.getDecodeParam(parameters.get("find_rest"));
	find_rest 				= Bean.checkFindString(pageFormName + tagFindRest, find_rest, "");
	String begin_rest_period	 	= Bean.getDecodeParam(parameters.get("begin_rest_period"));
	begin_rest_period 			= Bean.checkFindString(pageFormName + tagFindRestBegin, begin_rest_period, "");
	String end_rest_period	 	= Bean.getDecodeParam(parameters.get("end_rest_period"));
	end_rest_period 				= Bean.checkFindString(pageFormName + tagFindRestEnd, end_rest_period, "");
	%>
	<form action="../crm/club/target_programspecs.jsp" name="updateForm3" id="updateForm3" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">
	   	<input type="hidden" name="id" value="<%= id %>">
	<table <%=Bean.getTableBottomFilter() %>>
		<tr>
			<td valign="top"><%= Bean.bk_accountXML.getfieldTransl("h_begin_period", false) %>&nbsp;<%=Bean.getCalendarInputField("begin_rest_period", begin_rest_period, "10") %>&nbsp;&nbsp;&nbsp;
			<%= Bean.bk_accountXML.getfieldTransl("h_end_period", false) %>&nbsp;<%=Bean.getCalendarInputField("end_rest_period", end_rest_period, "10") %>&nbsp;&nbsp;&nbsp;
			<%= Bean.bk_accountXML.getfieldTransl("h_find_string", false) %>&nbsp;
			<input type="text" name="find_rest" id="find_rest" size="30" value="<%=find_rest %>" class="inputfield" title="<%= Bean.buttonXML.getfieldTransl("find_string", false) %>">&nbsp;
			<%=Bean.getSubmitButtonAjax("../crm/club/target_programspecs.jsp?id=" +id + "&rest_page=1&", "find", "updateForm3") %>&nbsp;
			</td>
			<td>&nbsp;</td>
		</tr>
	</table>
	</form>
	<%= Bean.getCalendarScript("begin_rest_period", false) %>
	<%= Bean.getCalendarScript("end_rest_period", false) %>

	<%= program.getRestsHTML(begin_rest_period, end_rest_period, find_rest, l_rest_page_beg, l_rest_page_end) %>
<%} %>

<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLUB_TARGET_PROGRAM_REFERRAL_SCHEME")) {%>
		<table <%=Bean.getTableBottomFilter() %>><tbody>
		<tr>
			<%=Bean.getFindHTML("referral_scheme_find", referral_scheme_find, "../crm/club/target_programspecs.jsp?id=" + id + "&referral_scheme_page=1") %>

			<%=Bean.getSelectOnChangeBeginHTML("referral_scheme_type", "../crm/club/target_programspecs.jsp?id=" + id + "&referral_scheme_page=1", Bean.clubXML.getfieldTransl("name_referral_scheme_type", false)) %>
				<%= Bean.getReferralShemeTypeOptions(referral_scheme_type, true) %>
			<%=Bean.getSelectOnChangeEndHTML() %>
	
			<%=Bean.getSelectOnChangeBeginHTML("referral_scheme_calc_type", "../crm/club/target_programspecs.jsp?id=" + id + "&referral_scheme_page=1", Bean.clubXML.getfieldTransl("name_referral_scheme_calc_type", false)) %>
				<%= Bean.getReferralShemeCalcTypeOptions(referral_scheme_calc_type, true) %>
			<%=Bean.getSelectOnChangeEndHTML() %>

		</tr>
		</tbody>
		</table>
	<%= program.getReferralSchemeHTML(referral_scheme_find, referral_scheme_type, referral_scheme_calc_type, l_referral_scheme_page_beg, l_referral_scheme_page_end)%> 
<%} %>

<% } %>
</div></div>
</body>
</html>
