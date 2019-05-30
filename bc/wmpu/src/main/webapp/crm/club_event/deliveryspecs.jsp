<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%@page import="java.util.HashMap"%>

<%= Bean.getLogOutScript(request) %>


<%@page import="bc.objects.bcNatPrsGiftObject"%>
<%@page import="bc.objects.bcNatPrsGiftRequestObject"%>
<%@page import="bc.objects.bcClubActionObject"%><html>
<head>
	<%= Bean.getMetaContent() %>
	<%= Bean.getBottomFrameCSS() %>
</head>
<body topmargin="0">
<div id="div_tabsheet">

<%

HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String pageFormName = "CLUB_EVENT_DELIVERY";
String tagTask = "_TASK";
String tagTaskType = "_TASK_TYPE";
String tagTaskState = "_TASK_STATE";
String tagTaskFind = "_TASK_FIND";

Bean.setJspPageForTabName(pageFormName);

String id = Bean.getDecodeParam(parameters.get("id"));
String filtr = Bean.getDecodeParam(parameters.get("filtr"));
String	tab = Bean.getDecodeParam(parameters.get("tab"));
if (tab==null || "".equalsIgnoreCase(tab)) { 
	tab = Bean.tabsHmGetValue(pageFormName);
}


if (id==null || "".equals(id)) {
%>

	<%=Bean.getIDNotFoundMessage() %>

<% } else {
	Bean.tabsHmSetValue(pageFormName, tab);
	bcNatPrsGiftObject gift = new bcNatPrsGiftObject(id);
	
	String l_task_page = Bean.getDecodeParam(parameters.get("task_page"));
	Bean.pageCheck(pageFormName + tagTask, l_task_page);
	String l_task_page_beg = Bean.getFirstRowNumber(pageFormName + tagTask);
	String l_task_page_end = Bean.getLastRowNumber(pageFormName + tagTask);

	String task_type 	= Bean.getDecodeParam(parameters.get("task_type"));
	task_type 	= Bean.checkFindString(pageFormName + tagTaskType, task_type, l_task_page);
	
	String task_state	= Bean.getDecodeParam(parameters.get("task_state"));
	task_state 		= Bean.checkFindString(pageFormName + tagTaskState, task_state, l_task_page);
	
	String task_find 	= Bean.getDecodeParam(parameters.get("task_find"));
	task_find 	= Bean.checkFindString(pageFormName + tagTaskFind, task_find, l_task_page);
	

%>

	<table <%=Bean.getTableMenuParam() %>>
		<tr>
			<%= Bean.getPageHeader() %>
			<% if (Bean.currentMenu.isCurrentTabAndEditPermitted("CLUB_EVENT_DELIVERY_INFO")) {%>
			    <%= Bean.getMenuButton("ADD", "../crm/club_event/deliveryupdate.jsp?id=" + id + "&type=general&action=add2&process=no", "", "") %>
				<%= Bean.getMenuButton("DELETE", "../crm/club_event/deliveryupdate.jsp?id=" + id + "&type=general&action=remove&process=yes", Bean.club_actionXML.getfieldTransl("h_delete_nat_prs_gift", false), gift.getValue("NAME_GIFT")) %>
			<% } %>

			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLUB_EVENT_DELIVERY_CARD_TASKS"))	{ %>
				<% if (Bean.currentMenu.isCurrentTabAndEditPermitted("CLUB_EVENT_DELIVERY_CARD_TASKS")) { %>
				    <%= Bean.getMenuButton("ADD", "../crm/club_event/deliveryupdate.jsp?id=" + id + "&type=tasks&action=add&process=no", "", "") %>
				<% } %>
				
			    <!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagTask, "../crm/club_event/deliveryspecs.jsp?id=" + id + "&tab="+Bean.currentMenu.getTabID("CLUB_EVENT_DELIVERY_CARD_TASKS")+"&", "task_page") %>
			<% } %>
	
		</tr>
	</table>
	<table <%=Bean.getTableMenuParam() %>>
		<%= Bean.getDetailCaption(gift.getValue("ID_NAT_PRS_GIFT") + " - " + gift.getValue("FULL_NAME") + ", " + gift.getValue("NAME_GIFT")) %>
		<tr>
			<td>
				<!-- Выводим перечень закладок -->
				<%= Bean.currentMenu.getFormTabScheeds(tab, "../crm/club_event/deliveryspecs.jsp?id=" + id) %>
			</td>
	
		</tr>
	</table>
</div>
<div id="div_data">
<div id="div_data_detail">
<%
if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLUB_EVENT_DELIVERY_INFO")) {
%>
	
		<script language="JavaScript">
			var formData = new Array (
				new Array ('name_club_event_gift', 'varchar2', 1),
				new Array ('date_reserve', 'varchar2', 1),
				new Array ('cd_nat_prs_gift_state', 'varchar2', 1)
			);
			</script>

		<% if (Bean.currentMenu.isCurrentTabAndEditPermitted("CLUB_EVENT_DELIVERY_INFO")) { %>
		  <form action="../crm/club_event/deliveryupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onsubmit="return validateForm(formData);">
			<input type="hidden" name="action" value="edit">
	    	<input type="hidden" name="process" value="yes">
	    	<input type="hidden" name="type" value="general">
	    	<input type="hidden" name="id" value="<%= id %>">
		<%} %>

		<table <%=Bean.getTableDetailParam() %>>

		<% if (Bean.currentMenu.isCurrentTabAndEditPermitted("CLUB_EVENT_DELIVERY_INFO")) {%>
		<tr>
			<td><%= Bean.club_actionXML.getfieldTransl("id_nat_prs_gift", false) %></td><td><input type="text" name="id_nat_prs_gift" size="20" value="<%= gift.getValue("ID_NAT_PRS_GIFT") %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.clubXML.getfieldTransl("club", false) %>
				<%= Bean.getGoToClubLink(gift.getValue("ID_CLUB")) %>
			</td><td><input type="text" name="name_club" size="50" value="<%= Bean.getClubShortName(gift.getValue("ID_CLUB")) %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td><%= Bean.club_actionXML.getfieldTransl("name_nat_prs_gift_state", false) %></td><td><input type="text" name="name_nat_prs_gift_state" size="20" value="<%= gift.getValue("NAME_NAT_PRS_GIFT_STATE") %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.club_actionXML.getfieldTransl("full_name", false) %>
				<%= Bean.getGoToNatPrsLink(gift.getValue("ID_NAT_PRS")) %>
			</td><td><input type="text" name="full_name" size="50" value="<%= gift.getValue("FULL_NAME") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td><%= Bean.club_actionXML.getfieldTransl("name_club_event", false) %>
				<%= Bean.getGoToClubEventLink(gift.getValue("ID_CLUB_EVENT")) %>
			</td><td><input type="text" name="name_club_event" size="60" value="<%= gift.getValue("NAME_CLUB_EVENT") %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.natprsXML.getfieldTransl("phone_contact", false) %></td><td><input type="text" name="phone_contact" size="20" value="<%= gift.getValue("PHONE_CONTACT") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
 		    <td><%= Bean.club_actionXML.getfieldTransl("name_gift", false) %>
				<%= Bean.getGoToClubEventGiftLink(gift.getValue("ID_GIFT")) %>
			</td>
		  	<td>
				<input type="text" name="name_nat_prs_gift" size="60" value="<%= gift.getValue("NAME_NAT_PRS_GIFT") %>" readonly="readonly" class="inputfield-ro">
	  		</td>
		</tr>
		<% if (!(gift.getValue("ID_LG_GIFT")==null || "".equalsIgnoreCase(gift.getValue("ID_LG_GIFT"))) && "RESERVED".equalsIgnoreCase(gift.getValue("CD_NAT_PRS_GIFT_STATE"))) { %>
		<tr> 		    
 		    <td><%= Bean.club_actionXML.getfieldTransl("desc_lg_gift", false) %>
				<%= Bean.getGoToClubEventWarehouseGiftLink(gift.getValue("ID_LG_GIFT")) %>
			</td>
		  	<td>
				<input type="text" name="desc_lg_gift" size="60" value="<%= gift.getValue("DESC_LG_GIFT") %>" readonly="readonly" class="inputfield-ro">
	  		</td>
		</tr>
		<% } %>
		<% if ("RESERVED".equalsIgnoreCase(gift.getValue("CD_NAT_PRS_GIFT_STATE"))) { %>
		<tr>
			<td>&nbsp;</td><td>
				<%=Bean.getSubmitHyperLink("../crm/club_event/deliveryupdate.jsp?id="+id+"&type=general&process=no&action=given", "Выдать") %>
				<%=Bean.getSubmitHyperLink("../crm/club_event/deliveryupdate.jsp?id="+id+"&type=general&process=no&action=cancel", "Отказать") %>
			</td>
			<td colspan="2">&nbsp;</td>
		</tr>
		<% } %>
		<tr>
			<td class="top_line" colspan="4"><b><%= Bean.club_actionXML.getfieldTransl("h_gifts_reserve", false) %></b></td>
		</tr>
		<tr>
			<td><%= Bean.club_actionXML.getfieldTransl("date_reserve", false) %></td> <td><%=Bean.getCalendarInputField("date_reserve", gift.getValue("DATE_RESERVE_FRMT"), "10") %></td>
			<td><%= Bean.club_actionXML.getfieldTransl("id_nat_prs_gift_request", false) %>
				<%= Bean.getGoToClubEventRequestLink(gift.getValue("ID_NAT_PRS_GIFT_REQUEST")) %>
			</td><td><input type="text" name="id_nat_prs_gift_request" size="20" value="<%= gift.getValue("ID_NAT_PRS_GIFT_REQUEST") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<% if ("GIVEN".equalsIgnoreCase(gift.getValue("CD_NAT_PRS_GIFT_STATE")) || 
				"RETURNED".equalsIgnoreCase(gift.getValue("CD_NAT_PRS_GIFT_STATE"))) { %>
		<tr>
			<td class="top_line" colspan="4"><b><%= Bean.club_actionXML.getfieldTransl("h_gifts_given", false) %></b></td>
		</tr>
		<tr>
			<td><%= Bean.club_actionXML.getfieldTransl("date_given", false) %></td> <td><input type="text" name="date_given" size="20" value="<%= gift.getValue("DATE_GIVEN_FRMT") %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.club_actionXML.getfieldTransl("event_type_put_gifts", false) %></td>
			<%
				String lEventCaption = "";
				if ("NONE".equalsIgnoreCase(gift.getValue("WRITE_OFF_GOODS_ACTION"))) {
	    			lEventCaption =  Bean.club_actionXML.getfieldTransl("event_type_put_gifts_none", false);
	    		} else if ("WRITE_OFF_BONS".equalsIgnoreCase(gift.getValue("WRITE_OFF_GOODS_ACTION"))) {
	    			lEventCaption =  Bean.club_actionXML.getfieldTransl("event_type_put_gifts_write_off_bons", false);
	    		} else if ("WRITE_OFF_GOODS_FROM_PURSE".equalsIgnoreCase(gift.getValue("WRITE_OFF_GOODS_ACTION"))) {
	    			lEventCaption =  Bean.club_actionXML.getfieldTransl("event_type_put_gifts_write_off_purse", false);
	    		}
			%>
			<td><b><i><%= lEventCaption %></i></b>
			</td>
		</tr>
		<tr>
 		    <td><%= Bean.club_actionXML.getfieldTransl("gifts_given_place", false) %>
				<%= Bean.getGoToJurPrsHyperLink(gift.getValue("ID_GIFTS_GIVEN_PLACE")) %>
			</td>
			<td>
				<input type="text" name="name_gifts_given_place" size="60" value="<%= Bean.getServicePlaceName(gift.getValue("ID_GIFTS_GIVEN_PLACE")) %>" readonly="readonly" class="inputfield-ro">
			</td>
			
			<% if ("WRITE_OFF_BONS".equalsIgnoreCase(gift.getValue("WRITE_OFF_GOODS_ACTION")) ||
					"WRITE_OFF_GOODS_FROM_PURSE".equalsIgnoreCase(gift.getValue("WRITE_OFF_GOODS_ACTION"))) { %>
			<td><%= Bean.club_actionXML.getfieldTransl("write_off_amount", false) %></td><td><input type="text" name="write_off_amount" size="20" value="<%= gift.getValue("WRITE_OFF_AMOUNT_FRMT") %>" readonly="readonly" class="inputfield-ro"></td>
			<% } %>
		</tr>
		<tr>
 		    <td><%= Bean.club_actionXML.getfieldTransl("desc_lg_gift", false) %>
				<%= Bean.getGoToClubEventWarehouseGiftLink(gift.getValue("ID_LG_GIFT")) %>
			</td>
		  	<td>
				<input type="text" name="desc_lg_gift" size="60" value="<%= gift.getValue("DESC_LG_GIFT") %>" readonly="readonly" class="inputfield-ro">
	  		</td>
			<% if ("WRITE_OFF_BONS".equalsIgnoreCase(gift.getValue("WRITE_OFF_GOODS_ACTION")) ||
					"WRITE_OFF_GOODS_FROM_PURSE".equalsIgnoreCase(gift.getValue("WRITE_OFF_GOODS_ACTION"))) { %>
			<td><%= Bean.getClubCardXMLFieldTransl("cd_card1", false) %>
				<% String cd_card1 = Bean.getClubCardCode(gift.getValue("CARD_SERIAL_NUMBER") + "_" + gift.getValue("CARD_ID_ISSUER") + "_" + gift.getValue("CARD_ID_PAYMENT_SYSTEM")); %>
				<%= Bean.getGoToClubCardLink(
						gift.getValue("CARD_SERIAL_NUMBER"),
						gift.getValue("CARD_ID_ISSUER"),
						gift.getValue("CARD_ID_PAYMENT_SYSTEM")
					) %>
			</td>
			<td>
				<input type="text" name="cd_card1" size="20" value="<%= cd_card1 %>" readonly="readonly" class="inputfield-ro">
			</td>
			<% } %>
		</tr>
		<tr>
			<td><%= Bean.club_actionXML.getfieldTransl("cost_gift", false) %></td><td><input type="text" name="cost_lg_gift" size="20" value="<%= gift.getValue("COST_LG_GIFT_FRMT") %> <%= gift.getValue("SNAME_LG_CURRENCY") %>" readonly="readonly" class="inputfield-ro"></td>
			<% if ("WRITE_OFF_GOODS_FROM_PURSE".equalsIgnoreCase(gift.getValue("WRITE_OFF_GOODS_ACTION"))) { %>
			<td><%= Bean.club_actionXML.getfieldTransl("card_purse", false) %>
				<%= Bean.getGoToClubCardPurseLink(gift.getValue("ID_CARD_PURSE")) %>
			</td>
			<td><input type="text" name="name_card_purse" size="20" value="<%= Bean.getClubCardPurseTypeForPurseNumber(gift.getValue("ID_CARD_PURSE")) %>" readonly="readonly" class="inputfield-ro"></td>
			<% } %>
		</tr>
		<% if ("GIVEN".equalsIgnoreCase(gift.getValue("CD_NAT_PRS_GIFT_STATE"))) { %>
		<tr>
			<td>&nbsp;</td><td>
				<%=Bean.getSubmitHyperLink("../crm/club_event/deliveryupdate.jsp?id="+id+"&type=general&process=yes&action=delete_given", "Отменить выдачу", "Отменить выдачу подарка") %>
				<%=Bean.getSubmitHyperLink("../crm/club_event/deliveryupdate.jsp?id="+id+"&type=general&process=no&action=return_client", "Возврат Клиентом") %>
			</td>
			<td colspan="2">&nbsp;</td>
		</tr>
		<% } %>
		<% } %>
		<% if ("RETURNED".equalsIgnoreCase(gift.getValue("CD_NAT_PRS_GIFT_STATE"))) { %>
		<tr>
			<td class="top_line" colspan="4"><b><%= Bean.club_actionXML.getfieldTransl("h_gifts_return", false) %></b></td>
		</tr>
		<tr>
			<td><%= Bean.club_actionXML.getfieldTransl("date_returned", false) %></td> <td><%=Bean.getCalendarInputField("date_returned", gift.getValue("DATE_RETURNED_FRMT"), "10") %></td>
			<td colspan="2">&nbsp;</td>
		</tr>
		<tr>
			<td><%= Bean.club_actionXML.getfieldTransl("reason_return", false) %></td> <td><textarea name="reason_return" cols="56" rows="3" class="inputfield"><%=gift.getValue("REASON_RETURN") %></textarea></td>
			<td colspan="2">&nbsp;</td>
		</tr>
		<tr>
			<td>&nbsp;</td><td>
				<%=Bean.getSubmitHyperLink("../crm/club_event/deliveryupdate.jsp?id="+id+"&type=general&process=yes&action=delete_return", "Удалить возврат", "Удалить информацию о возврате подарка") %>
			</td>
			<td colspan="2">&nbsp;</td>
		</tr>
		<% } %>

		<% if ("CANCELED".equalsIgnoreCase(gift.getValue("CD_NAT_PRS_GIFT_STATE"))) { %>
		<tr>
			<td class="top_line" colspan="4"><b><%= Bean.club_actionXML.getfieldTransl("h_gifts_cancel", false) %></b></td>
		</tr>
		<tr>
			<td><%= Bean.club_actionXML.getfieldTransl("date_canceled", false) %></td> <td><%=Bean.getCalendarInputField("date_canceled", gift.getValue("DATE_CANCELED_FRMT"), "10") %></td>
			<td colspan="2">&nbsp;</td>
		</tr>
		<tr>
			<td><%= Bean.club_actionXML.getfieldTransl("reason_cancel", false) %></td> <td><textarea name="reason_canceld" cols="56" rows="3" class="inputfield"><%=gift.getValue("REASON_CANCEL") %></textarea></td>
			<td colspan="2">&nbsp;</td>
		</tr>
		<tr>
			<td>&nbsp;</td><td>
				<%=Bean.getSubmitHyperLink("../crm/club_event/deliveryupdate.jsp?id="+id+"&type=general&process=yes&action=delete_cancel", "Удалить отказ", "Удалить информацию об отказе в выдаче подарка") %>
			</td>
			<td colspan="2">&nbsp;</td>
		</tr>
		<% } %>
		<%=	Bean.getCreationAndMoficationRecordFields(
				gift.getValue(Bean.getCreationDateFieldName()),
				gift.getValue("CREATED_BY"),
				gift.getValue(Bean.getLastUpdateDateFieldName()),
				gift.getValue("LAST_UPDATE_BY")
			) %>
		<tr>
			<td colspan="4" align="center">
				<%=Bean.getSubmitButtonAjax("../crm/club_event/deliveryupdate.jsp") %>
				<%=Bean.getResetButton() %>
				<%=Bean.getGoBackButton("../crm/club_event/delivery.jsp") %>
			</td>
		</tr>

		<%} else {%>
		<tr>
			<td><%= Bean.club_actionXML.getfieldTransl("id_nat_prs_gift", false) %></td><td><input type="text" name="id_nat_prs_gift" size="20" value="<%= gift.getValue("ID_NAT_PRS_GIFT") %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.clubXML.getfieldTransl("club", false) %>
				<%= Bean.getGoToClubLink(gift.getValue("ID_CLUB")) %>
			</td><td><input type="text" name="name_club" size="50" value="<%= Bean.getClubShortName(gift.getValue("ID_CLUB")) %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td><%= Bean.club_actionXML.getfieldTransl("name_nat_prs_gift_state", false) %></td><td><input type="text" name="name_nat_prs_gift_state" size="20" value="<%= gift.getValue("NAME_NAT_PRS_GIFT_STATE") %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.club_actionXML.getfieldTransl("full_name", false) %>
				<%= Bean.getGoToNatPrsLink(gift.getValue("ID_NAT_PRS")) %>
			</td><td><input type="text" name="full_name" size="50" value="<%= gift.getValue("FULL_NAME") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td><%= Bean.club_actionXML.getfieldTransl("name_club_event", false) %>
				<%= Bean.getGoToClubEventLink(gift.getValue("ID_CLUB_EVENT")) %>
			</td><td><input type="text" name="name_club_event" size="60" value="<%= gift.getValue("NAME_CLUB_EVENT") %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.natprsXML.getfieldTransl("phone_contact", false) %></td><td><input type="text" name="phone_contact" size="20" value="<%= gift.getValue("PHONE_CONTACT") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
 		    <td><%= Bean.club_actionXML.getfieldTransl("name_gift", false) %>
				<%= Bean.getGoToClubEventGiftLink(gift.getValue("ID_GIFT")) %>
			</td>
		  	<td>
				<input type="text" name="name_gift" size="60" value="<%= gift.getValue("NAME_GIFT") %>" readonly="readonly" class="inputfield-ro">
	  		</td>
		</tr>
		<tr>
			<td class="top_line" colspan="4"><b><%= Bean.club_actionXML.getfieldTransl("h_gifts_reserve", false) %></b></td>
		</tr>
		<tr>
			<td><%= Bean.club_actionXML.getfieldTransl("date_reserve", false) %></td> <td><input type="text" name="date_reserve" size="20" value="<%= gift.getValue("DATE_RESERVE_FRMT") %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.club_actionXML.getfieldTransl("id_nat_prs_gift_request", false) %>
				<%= Bean.getGoToClubEventRequestLink(gift.getValue("ID_NAT_PRS_GIFT_REQUEST")) %>
			</td><td><input type="text" name="id_nat_prs_gift_request" size="20" value="<%= gift.getValue("ID_NAT_PRS_GIFT_REQUEST") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<% if ("GIVEN".equalsIgnoreCase(gift.getValue("CD_NAT_PRS_GIFT_STATE")) || 
				"RETURNED".equalsIgnoreCase(gift.getValue("CD_NAT_PRS_GIFT_STATE"))) { %>
		<tr>
			<td class="top_line" colspan="4"><b><%= Bean.club_actionXML.getfieldTransl("h_gifts_given", false) %></b></td>
		</tr>
		<tr>
			<td><%= Bean.club_actionXML.getfieldTransl("date_given", false) %></td> <td><input type="text" name="date_given" size="20" value="<%= gift.getValue("DATE_GIVEN_FRMT") %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.club_actionXML.getfieldTransl("event_type_put_gifts", false) %></td>
			<%
				String lEventCaption = "";
				if ("NONE".equalsIgnoreCase(gift.getValue("WRITE_OFF_GOODS_ACTION"))) {
	    			lEventCaption =  Bean.club_actionXML.getfieldTransl("event_type_put_gifts_none", false);
	    		} else if ("WRITE_OFF_BONS".equalsIgnoreCase(gift.getValue("WRITE_OFF_GOODS_ACTION"))) {
	    			lEventCaption =  Bean.club_actionXML.getfieldTransl("event_type_put_gifts_write_off_bons", false);
	    		} else if ("WRITE_OFF_GOODS_FROM_PURSE".equalsIgnoreCase(gift.getValue("WRITE_OFF_GOODS_ACTION"))) {
	    			lEventCaption =  Bean.club_actionXML.getfieldTransl("event_type_put_gifts_write_off_purse", false);
	    		}
			%>
			<td><b><i><%= lEventCaption %></i></b>
			</td>
		</tr>
		<tr>
 		    <td><%= Bean.club_actionXML.getfieldTransl("gifts_given_place", false) %>
				<%= Bean.getGoToJurPrsHyperLink(gift.getValue("ID_GIFTS_GIVEN_PLACE")) %>
			</td>
			<td>
				<input type="text" name="name_gifts_given_place" size="60" value="<%= Bean.getServicePlaceName(gift.getValue("ID_GIFTS_GIVEN_PLACE")) %>" readonly="readonly" class="inputfield-ro">
			</td>
			
			<% if ("WRITE_OFF_BONS".equalsIgnoreCase(gift.getValue("WRITE_OFF_GOODS_ACTION")) ||
					"WRITE_OFF_GOODS_FROM_PURSE".equalsIgnoreCase(gift.getValue("WRITE_OFF_GOODS_ACTION"))) { %>
			<td><%= Bean.club_actionXML.getfieldTransl("write_off_amount", false) %></td><td><input type="text" name="write_off_amount" size="20" value="<%= gift.getValue("WRITE_OFF_AMOUNT_FRMT") %>" readonly="readonly" class="inputfield-ro"></td>
			<% } %>
		</tr>
		<tr>
 		    <td><%= Bean.club_actionXML.getfieldTransl("desc_lg_gift", false) %>
				<%= Bean.getGoToClubEventWarehouseGiftLink(gift.getValue("ID_LG_GIFT")) %>
			</td>
		  	<td>
				<input type="text" name="desc_lg_gift" size="60" value="<%= gift.getValue("DESC_LG_GIFT") %>" readonly="readonly" class="inputfield-ro">
	  		</td>
			<% if ("WRITE_OFF_BONS".equalsIgnoreCase(gift.getValue("WRITE_OFF_GOODS_ACTION")) ||
					"WRITE_OFF_GOODS_FROM_PURSE".equalsIgnoreCase(gift.getValue("WRITE_OFF_GOODS_ACTION"))) { %>
			<td><%= Bean.getClubCardXMLFieldTransl("cd_card1", false) %>
				<% String cd_card1 = Bean.getClubCardCode(gift.getValue("CARD_SERIAL_NUMBER") + "_" + gift.getValue("CARD_ID_ISSUER") + "_" + gift.getValue("CARD_ID_PAYMENT_SYSTEM")); %>
				<%= Bean.getGoToClubCardLink(
						gift.getValue("CARD_SERIAL_NUMBER"),
						gift.getValue("CARD_ID_ISSUER"),
						gift.getValue("CARD_ID_PAYMENT_SYSTEM")
					) %>
			</td>
			<td>
				<input type="text" name="cd_card1" size="20" value="<%= cd_card1 %>" readonly="readonly" class="inputfield-ro">
			</td>
			<% } %>
		</tr>
		<tr>
			<td><%= Bean.club_actionXML.getfieldTransl("cost_gift", false) %></td><td><input type="text" name="cost_lg_gift" size="20" value="<%= gift.getValue("COST_LG_GIFT_FRMT") %> <%= gift.getValue("SNAME_LG_CURRENCY") %>" readonly="readonly" class="inputfield-ro"></td>
			<% if ("WRITE_OFF_GOODS_FROM_PURSE".equalsIgnoreCase(gift.getValue("WRITE_OFF_GOODS_ACTION"))) { %>
			<td><%= Bean.club_actionXML.getfieldTransl("card_purse", false) %></td><td><input type="text" name="name_card_purse" size="20" value="<%= Bean.getClubCardPurseTypeForPurseNumber(gift.getValue("ID_CARD_PURSE")) %>" readonly="readonly" class="inputfield-ro"></td>
			<% } %>
		</tr>
		<% } %>
		<% if ("RETURNED".equalsIgnoreCase(gift.getValue("CD_NAT_PRS_GIFT_STATE"))) { %>
		<tr>
			<td class="top_line" colspan="4"><b><%= Bean.club_actionXML.getfieldTransl("h_gifts_return", false) %></b></td>
		</tr>
		<tr>
			<td><%= Bean.club_actionXML.getfieldTransl("date_returned", false) %></td> <td><input type="text" name="date_returned" size="20" value="<%= gift.getValue("DATE_RETURNED_FRMT") %>" readonly="readonly" class="inputfield-ro"></td>
			<td colspan="2">&nbsp;</td>
		</tr>
		<tr>
			<td><%= Bean.club_actionXML.getfieldTransl("reason_return", false) %></td> <td><textarea name="reason_return" cols="56" rows="3" readonly="readonly" class="inputfield-ro"><%=gift.getValue("REASON_RETURN") %></textarea></td>
			<td colspan="2">&nbsp;</td>
		</tr>
		<% } %>

		<% if ("CANCELED".equalsIgnoreCase(gift.getValue("CD_NAT_PRS_GIFT_STATE"))) { %>
		<tr>
			<td class="top_line" colspan="4"><b><%= Bean.club_actionXML.getfieldTransl("h_gifts_cancel", false) %></b></td>
		</tr>
		<tr>
			<td><%= Bean.club_actionXML.getfieldTransl("date_canceled", false) %></td> <td><input type="text" name="date_canceled" size="20" value="<%= gift.getValue("DATE_CANCELED_FRMT") %>" readonly="readonly" class="inputfield-ro"></td>
			<td colspan="2">&nbsp;</td>
		</tr>
		<tr>
			<td><%= Bean.club_actionXML.getfieldTransl("reason_cancel", false) %></td> <td><textarea name="reason_canceld" cols="56" rows="3" readonly="readonly" class="inputfield-ro"><%=gift.getValue("REASON_CANCEL") %></textarea></td>
			<td colspan="2">&nbsp;</td>
		</tr>
		<% } %>
		<%=	Bean.getCreationAndMoficationRecordFields(
				gift.getValue(Bean.getCreationDateFieldName()),
				gift.getValue("CREATED_BY"),
				gift.getValue(Bean.getLastUpdateDateFieldName()),
				gift.getValue("LAST_UPDATE_BY")
			) %>
		<tr>
			<td colspan="4" align="center">
				<%=Bean.getGoBackButton("../crm/club_event/delivery.jsp") %>
			</td>
		</tr>
		<%} %>

	</table>
	</form>

	<% if (Bean.currentMenu.isCurrentTabAndEditPermitted("CLUB_EVENT_DELIVERY_INFO")) { %>
		<%= Bean.getCalendarScript("date_reserve", false) %>
		<% if ("RETURNED".equalsIgnoreCase(gift.getValue("CD_NAT_PRS_GIFT_STATE"))) { %>
			<%= Bean.getCalendarScript("date_returned", false) %>
		<% } %>
		<% if ("CANCELED".equalsIgnoreCase(gift.getValue("CD_NAT_PRS_GIFT_STATE"))) { %>
			<%= Bean.getCalendarScript("date_canceled", false) %>
		<% } %>
	<% } %>

<% }

if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLUB_EVENT_DELIVERY_CARD_TASKS")) { 
	boolean hasEditPerm = Bean.currentMenu.isTabSheetEditPermitted("CARDS_CLUBCARDS_TASKS");

%> 		
	<table <%=Bean.getTableBottomFilter() %>>
	  	<tr>
		<%= Bean.getFindHTML("task_find", task_find, "../crm/club_event/clubeventspecs.jsp?id=" + id + "&task_page=1") %>

		<%=Bean.getSelectOnChangeBeginHTML("task_type", "../crm/club_event/deliveryspecs.jsp?id=" + id + "&task_page=1", Bean.card_taskXML.getfieldTransl("CD_CARD_OPERATION_TYPE", false)) %>
			<%= Bean.getClubCardOperationTypeOptions(task_type, true) %>
		<%=Bean.getSelectOnChangeEndHTML() %>

		<%=Bean.getSelectOnChangeBeginHTML("task_state", "../crm/club_event/deliveryspecs.jsp?id=" + id + "&task_page=1", Bean.card_taskXML.getfieldTransl("cd_card_oper_state", false)) %>
			<%= Bean.getClubCardOperationStateOptions(task_state, true) %>
		<%=Bean.getSelectOnChangeEndHTML() %>
	  	</tr>
	</table>
	<%= gift.getClubCardsTasksHTML(task_find, task_type, task_state, l_task_page_beg, l_task_page_end) %>
<%
}
}
		
%>
</div></div>
</body>
</html>
