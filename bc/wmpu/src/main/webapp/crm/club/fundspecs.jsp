<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%@page import="java.util.HashMap"%>
<%@page import="bc.objects.bcClubFundObject"%><html>

<%= Bean.getLogOutScript(request) %>

<head>
	<%= Bean.getMetaContent() %>
	<%= Bean.getBottomFrameCSS() %>
</head>
<body topmargin="0">
<div id="div_tabsheet">
<%

HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String pageFormName = "CLUB_FUND";

String tagTransfer = "_TRANSFER";
String tagTransferFind = "_TRANSFER_FIND";
String tagTransferOperType = "_TRANSFER_OPER_TYPE";
String tagTransferPaymentKind = "_TRANSFER_PAYMENT_KIND";
String tagRest = "_REST";
String tagRestPaymentKind = "_REST_PAYMENT_KIND";


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
	bcClubFundObject fund = new bcClubFundObject(id);
	
	String l_transfer_page = Bean.getDecodeParam(parameters.get("transfer_page"));
	Bean.pageCheck(pageFormName + tagTransfer, l_transfer_page);
	String l_transfer_page_beg = Bean.getFirstRowNumber(pageFormName + tagTransfer);
	String l_transfer_page_end = Bean.getLastRowNumber(pageFormName + tagTransfer);
	
	String transfer_find 	= Bean.getDecodeParam(parameters.get("transfer_find"));
	transfer_find 	= Bean.checkFindString(pageFormName + tagTransferFind, transfer_find, l_transfer_page);
	
	String transfer_oper_type 	= Bean.getDecodeParam(parameters.get("transfer_oper_type"));
	transfer_oper_type 	= Bean.checkFindString(pageFormName + tagTransferOperType, transfer_oper_type, l_transfer_page);
	
	String transfer_payment_kind 	= Bean.getDecodeParam(parameters.get("transfer_payment_kind"));
	transfer_payment_kind 	= Bean.checkFindString(pageFormName + tagTransferPaymentKind, transfer_payment_kind, l_transfer_page);
	
	String l_rest_page = Bean.getDecodeParam(parameters.get("rest_page"));
	Bean.pageCheck(pageFormName + tagRest, l_rest_page);
	String l_rest_page_beg = Bean.getFirstRowNumber(pageFormName + tagRest);
	String l_rest_page_end = Bean.getLastRowNumber(pageFormName + tagRest);

	String rest_payment_kind 	= Bean.getDecodeParam(parameters.get("rest_payment_kind"));
	if (rest_payment_kind==null || "".equalsIgnoreCase(rest_payment_kind)) {
		rest_payment_kind = "TOTAL";
	}
	rest_payment_kind 	= Bean.checkFindString(pageFormName + tagRestPaymentKind, rest_payment_kind, l_rest_page);
	
%>

	<table <%=Bean.getTableMenuParam() %>>
		<tr>
			<%= Bean.getPageHeader() %>

			<% if (Bean.currentMenu.isCurrentTabAndEditPermitted("CLUB_FUND_INFO")) { %>
			    <%= Bean.getMenuButton("ADD", "../crm/club/fundupdate.jsp?id_club_fund=" + id + "&type=general&action=add2&process=no", "", "") %>
			    <%= Bean.getMenuButton("DELETE", "../crm/club/fundupdate.jsp?id_club_fund=" + id + "&type=general&action=remove&process=yes", Bean.buttonXML.getfieldTransl("delete", false), fund.getValue("NAME_CLUB_FUND")) %>
			<%  } %>
			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLUB_FUND_TRANSFER")) { %>
			    <!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagTransfer, "../crm/club/fundspecs.jsp?id=" + id + "&tab="+Bean.currentMenu.getTabID("CLUB_FUND_TRANSFER")+"&", "transfer_page") %>
			<% }%>
			
			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLUB_FUND_RESTS")) { %>
				<% if (Bean.currentMenu.isCurrentTabAndEditPermitted("CLUB_FUND_RESTS")) {	%>
					<%= Bean.getMenuButton("RUN", "../crm/club/fundupdate.jsp?id_club_fund="+ id + "&type=rests&action=run&process=yes", Bean.clubfundXML.getfieldTransl("h_calc_rests", false), "", Bean.clubfundXML.getfieldTransl("h_calc_rests", false)) %>
				<% } %>
			    <!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagRest, "../crm/club/fundspecs.jsp?id_club_fund=" + id + "&tab="+Bean.currentMenu.getTabID("CLUB_FUND_RESTS")+"&", "rest_page") %>
			<% } %>
		</tr>
	</table>
	<table <%=Bean.getTableMenuParam() %>>
		<%= Bean.getDetailCaption(fund.getValue("NAME_CLUB_FUND") + " (" + fund.getValue("SNAME_JUR_PRS") + ")") %>
		<tr>
			<td>
				<!-- Выводим перечень закладок -->
				<%= Bean.currentMenu.getFormTabScheeds(tab, "../crm/club/fundspecs.jsp?id=" + id) %>
			</td>
		</tr>
	</table>
</div>
<div id="div_data">
<div id="div_data_detail">
<%
if (Bean.currentMenu.isCurrentTabAndPreviewPermitted("CLUB_FUND_INFO")) {
 %>	 
	<form>
	<table <%=Bean.getTableDetailParam() %>>
	    <tr>
			<td><%= Bean.clubfundXML.getfieldTransl("sname_jur_prs", false) %>
				<%= Bean.getGoToJurPrsHyperLink(fund.getValue("ID_JUR_PRS")) %>
			</td><td><input type="text" name="name_jur_prs" size="70" value="<%= fund.getValue("SNAME_JUR_PRS") %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.clubXML.getfieldTransl("club", false) %>
				<%= Bean.getGoToClubLink(fund.getValue("ID_CLUB")) %>
			</td><td><input type="text" name="name_club" size="30" value="<%= Bean.getClubShortName(fund.getValue("ID_CLUB")) %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
	    <tr>
			<td><%= Bean.clubfundXML.getfieldTransl("name_club_fund", false) %></td> <td><input type="text" name="name_club_fund" size="70" value="<%=fund.getValue("NAME_CLUB_FUND") %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.clubfundXML.getfieldTransl("fund_date_beg", false) %></td> <td><input type="text" name="date_beg" size="10" value="<%=fund.getValue("DATE_BEG_FRMT") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
	    <tr>
			<td><%= Bean.clubfundXML.getfieldTransl("sname_club_fund", false) %></td> <td><input type="text" name="sname_club_fund" size="70" value="<%=fund.getValue("SNAME_CLUB_FUND") %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.clubfundXML.getfieldTransl("fund_date_end", false) %></td> <td><input type="text" name="date_end" size="10" value="<%=fund.getValue("DATE_END_FRMT") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
	    <tr>
			<td><%= Bean.clubfundXML.getfieldTransl("desc_club_fund", false) %></td> <td><textarea name="desc_club_fund" cols="67" rows="3" readonly="readonly" class="inputfield-ro"><%= fund.getValue("DESC_CLUB_FUND") %></textarea></td>
			<td colspan="2">&nbsp;</td>
		</tr>
		<%=	Bean.getIdCreationAndMoficationRecordFields(
				fund.getValue("ID_CLUB_FUND"),
				fund.getValue(Bean.getCreationDateFieldName()),
				fund.getValue("CREATED_BY"),
				fund.getValue(Bean.getLastUpdateDateFieldName()),
				fund.getValue("LAST_UPDATE_BY")
			) %>
		<tr>
			<td colspan="6" align="center">
				<%=Bean.getGoBackButton("../crm/club/fund.jsp") %>
			</td>
		</tr>
	</table>
	</form>

<% } else if (Bean.currentMenu.isCurrentTabAndEditPermitted("CLUB_FUND_INFO")) { %>
	
		<script>
			var formData = new Array (
				new Array ('name_club_fund', 'varchar2', 1),
				new Array ('sname_club_fund', 'varchar2', 1),
				new Array ('date_beg', 'varchar2', 1)
			);
			function myValidateForm() {
				return validateForm(formData);
			}
		</script>
	<div id="div_detail">
    <form action="../crm/club/fundupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST">
    	<input type="hidden" name="id_club_fund" value="<%=id %>">
	    <input type="hidden" name="LUD" value="<%=fund.getValue("LUD") %>">
	    <input type="hidden" name="action" value="edit">
    	<input type="hidden" name="process" value="yes">
    	<input type="hidden" name="type" value="general">
	<table <%=Bean.getTableDetailParam() %>>
	    <tr>
			<td><%= Bean.clubfundXML.getfieldTransl("sname_jur_prs", false) %>
				<%= Bean.getGoToJurPrsHyperLink(fund.getValue("ID_JUR_PRS")) %>
			</td><td><input type="text" name="name_jur_prs" size="70" value="<%= fund.getValue("SNAME_JUR_PRS") %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.clubXML.getfieldTransl("club", false) %>
				<%= Bean.getGoToClubLink(fund.getValue("ID_CLUB")) %>
			</td><td><input type="text" name="name_club" size="30" value="<%= Bean.getClubShortName(fund.getValue("ID_CLUB")) %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
	    <tr>
			<td><%= Bean.clubfundXML.getfieldTransl("name_club_fund", true) %></td> <td><input type="text" name="name_club_fund" size="70" value="<%=fund.getValue("NAME_CLUB_FUND") %>" class="inputfield"></td>
			<td><%= Bean.clubfundXML.getfieldTransl("fund_date_beg", true) %></td> <td><%=Bean.getCalendarInputField("date_beg", fund.getValue("DATE_BEG_FRMT"), "10") %></td>
		</tr>
	    <tr>
			<td><%= Bean.clubfundXML.getfieldTransl("sname_club_fund", true) %></td> <td><input type="text" name="sname_club_fund" size="70" value="<%=fund.getValue("SNAME_CLUB_FUND") %>" class="inputfield"></td>
			<td><%= Bean.clubfundXML.getfieldTransl("fund_date_end", false) %></td> <td><%=Bean.getCalendarInputField("date_end", fund.getValue("DATE_END_FRMT"), "10") %></td>
		</tr>
	    <tr>
			<td><%= Bean.clubfundXML.getfieldTransl("desc_club_fund", false) %></td> <td><textarea name="desc_club_fund" cols="67" rows="3" class="inputfield"><%= fund.getValue("DESC_CLUB_FUND") %></textarea></td>
			<td colspan="2">&nbsp;</td>
		</tr>
		<%=	Bean.getIdCreationAndMoficationRecordFields(
				fund.getValue("ID_CLUB_FUND"),
				fund.getValue(Bean.getCreationDateFieldName()),
				fund.getValue("CREATED_BY"),
				fund.getValue(Bean.getLastUpdateDateFieldName()),
				fund.getValue("LAST_UPDATE_BY")
			) %>
		<tr>
			<td colspan="6" align="center">
				<%=Bean.getSubmitButtonAjax("../crm/club/fundupdate.jsp","submit","updateForm","div_detail") %>
				<%=Bean.getResetButton() %>
				<%=Bean.getGoBackButton("../crm/club/fund.jsp") %>
			</td>
		</tr>
	</table>

	</form>
	</div>
	<%= Bean.getCalendarScript("date_beg", false) %>
	<%= Bean.getCalendarScript("date_end", false) %>
<% } %>

<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLUB_FUND_RESTS")) {%>
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
	<form action="../crm/club/fundspecs.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">
	   	<input type="hidden" name="id" value="<%= id %>">
	<table <%=Bean.getTableBottomFilter() %>>
		<tr>
			<td valign="top"><%= Bean.bk_accountXML.getfieldTransl("h_begin_period", false) %>&nbsp;<%=Bean.getCalendarInputField("begin_rest_period", begin_rest_period, "10") %>&nbsp;&nbsp;&nbsp;
			<%= Bean.bk_accountXML.getfieldTransl("h_end_period", false) %>&nbsp;<%=Bean.getCalendarInputField("end_rest_period", end_rest_period, "10") %>&nbsp;&nbsp;&nbsp;
			<%= Bean.bk_accountXML.getfieldTransl("h_find_string", false) %>&nbsp;
			<input type="text" name="find_rest" id="find_rest" size="30" value="<%=find_rest %>" class="inputfield" title="<%= Bean.buttonXML.getfieldTransl("find_string", false) %>">&nbsp;
			<%=Bean.getSubmitButtonAjax("../crm/club/fundspecs.jsp?id=" +id + "&rest_page=1&", "find", "updateForm") %>&nbsp;
			</td>
			<%=Bean.getSelectOnChangeBeginHTML("rest_payment_kind", "../crm/club/fundspecs.jsp?id=" + id + "&rest_page=1", Bean.clubfundXML.getfieldTransl("name_club_fund_payment_kind", false)) %>
				<%= Bean.getSelectOptionHTML(rest_payment_kind, "TOTAL", Bean.clubfundXML.getfieldTransl("title_fund_rest_total", false), "style=\"color:red;font-weight:bold;\"") %>
				<%= Bean.getClubFundPaymentKindOptions(id, rest_payment_kind, false) %>
			<%=Bean.getSelectOnChangeEndHTML() %>
		</tr>
	</table>
	</form>
	<%= Bean.getCalendarScript("begin_rest_period", false) %>
	<%= Bean.getCalendarScript("end_rest_period", false) %>

	<%= fund.getRestsHTML(begin_rest_period, end_rest_period, find_rest, rest_payment_kind, l_rest_page_beg, l_rest_page_end) %>
<%} %>

<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLUB_FUND_TRANSFER")) {%>
		<table <%=Bean.getTableBottomFilter() %>><tbody>
		<tr>
			<%=Bean.getFindHTML("transfer_find", transfer_find, "../crm/club/fundspecs.jsp?id=" + id + "&transfer_page=1") %>

			<%=Bean.getSelectOnChangeBeginHTML("transfer_payment_kind", "../crm/club/fundspecs.jsp?id=" + id + "&transfer_page=1", Bean.clubfundXML.getfieldTransl("name_club_fund_payment_kind", false)) %>
				<%= Bean.getClubFundPaymentKindOptions(id, transfer_payment_kind, true) %>
			<%=Bean.getSelectOnChangeEndHTML() %>
	
			<%=Bean.getSelectOnChangeBeginHTML("transfer_oper_type", "../crm/club/fundspecs.jsp?id=" + id + "&transfer_page=1", Bean.clubfundXML.getfieldTransl("name_club_fund_oper_type", false)) %>
				<%= Bean.getClubFundOperTypeOptions(transfer_oper_type, true) %>
			<%=Bean.getSelectOnChangeEndHTML() %>
		</tr>
		</tbody>
		</table>
	<%= fund.getTransferHTML(transfer_find, transfer_payment_kind, transfer_oper_type, l_transfer_page_beg, l_transfer_page_end) %>
<%} %>

<% } %>
</div></div>
</body>
</html>
