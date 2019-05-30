<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />


<%@page import="bc.objects.bcGiftObject"%>
<%@page import="java.util.HashMap"%>
<%@page import="bc.objects.bcClubShortObject"%>
<%@page import="bc.objects.bcClubActionGivenScheduleObject"%>
<%@page import="bc.objects.bcNatPrsGiftObject"%>
<%@page import="bc.objects.bcClubCardOperationObject"%>
<%@page import="bc.objects.bcClubCardObject"%>
<%@page import="bc.objects.bcClubCardPurseObject"%>
<%@page import="bc.objects.bcClubActionGiftObject"%><html>

<%= Bean.getLogOutScript(request) %>

<head>
	<%= Bean.getMetaContent() %>
	<%= Bean.getBottomFrameCSS() %>
</head>

<body>
<%
HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String pageFormName = "CLUB_EVENT_DELIVERY";

Bean.setJspPageForTabName(pageFormName);

request.setCharacterEncoding("UTF-8");

String type		= Bean.getDecodeParam(parameters.get("type"));
String id		= Bean.getDecodeParam(parameters.get("id")); 
String action	= Bean.getDecodeParam(parameters.get("action"));
String process	= Bean.getDecodeParam(parameters.get("process"));
 
if (id==null || ("".equalsIgnoreCase(id))) id="empty";
if (type==null || ("".equalsIgnoreCase(type))) type="empty";
if (action==null || ("".equalsIgnoreCase(action))) action="empty";
if (process==null || ("".equalsIgnoreCase(process))) process="empty";

if (type.equalsIgnoreCase("general")) {
  	if (process.equalsIgnoreCase("no")) {
    	if (action.equalsIgnoreCase("add") || action.equalsIgnoreCase("add2")) {

    		bcClubShortObject club = new bcClubShortObject(Bean.getCurrentClubID());

	        %> 
		<script>
			var formDataAdd = new Array (
					new Array ('name_nat_prs', 'varchar2', 1),
					new Array ('date_reserve', 'varchar2', 1),
					new Array ('name_club_event', 'varchar2', 1),
					new Array ('name_club_event_gift', 'varchar2', 1)
			);
			function myValidateForm() {
				return validateForm(formDataAdd);
			}
		</script>
		<%= Bean.getOperationTitle(
				Bean.club_actionXML.getfieldTransl("h_gift_add", false),
				"Y",
				"Y") 
		%>
        <form action="../crm/club_event/deliveryupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return myValidateForm()">
	        <input type="hidden" name="type" value="general">
	        <input type="hidden" name="action" value="add">
	        <input type="hidden" name="action_prev" value="<%=action %>">
	        <input type="hidden" name="process" value="yes">
	        <input type="hidden" name="id" value="<%=id %>">
		<table <%=Bean.getTableDetailParam() %>>
			<tr>
			  	<td><%= Bean.club_actionXML.getfieldTransl("full_name", true) %></td>
				<td>
					<%=Bean.getWindowFindNatPrs("nat_prs", "", "", "50") %>
				</td>
	
			</tr>
	        <tr>
				<td><%= Bean.club_actionXML.getfieldTransl("name_nat_prs_gift_state", false) %></td> <td><input type="text" name="name_nat_prs_gift_state" size="20" value="<%=Bean.getNatPrsGiftStateName("RESERVED") %>" readonly="readonly" class="inputfield-ro"></td>
				<td colspan="2">&nbsp;</td>
			</tr>
	        <tr>
				<td><%= Bean.club_actionXML.getfieldTransl("date_reserve", true) %></td> <td><%=Bean.getCalendarInputField("date_reserve", Bean.getSysDate(), "10") %></td>
				<td colspan="2">&nbsp;</td>
			</tr>
			<tr>
	 		    <td><%= Bean.club_actionXML.getfieldTransl("name_club_event", true) %></td>
			  	<td>
					<%=Bean.getWindowFindClubAction("club_event", "", "", "50") %>
		  		</td>
				<td colspan="2">&nbsp;</td>
			</tr>
			<tr>
	 		    <td><%= Bean.club_actionXML.getfieldTransl("name_club_event_gift", true) %></td>
			  	<td>
					<%=Bean.getWindowFindClubActionGifts("club_event_gift", "'+document.getElementById('id_club_event').value+'", "", "", "50") %>
		  		</td>
				<td colspan="2">&nbsp;</td>
			</tr>
			<tr>
	 		    <td><%= Bean.club_actionXML.getfieldTransl("desc_lg_gift", false) %></td>
			  	<td>
					<%=Bean.getWindowFindGiftsLogistic("lg_gift", "", "", "", "50") %>
			</tr>
	
			<tr>
				<td colspan="6" align="center">
					<%=Bean.getSubmitButtonAjax("../crm/club_event/deliveryupdate.jsp") %>
					<%=Bean.getResetButton() %>
					<% if ("add".equalsIgnoreCase(action)) { %>
						<%=Bean.getGoBackButton("../crm/club_event/delivery.jsp") %>
					<% } else { %>
						<%=Bean.getGoBackButton("../crm/club_event/deliveryspecs.jsp?id=" + id) %>
					<% } %>
				</td>
			</tr>
		</table>
		</form>
		<%= Bean.getCalendarScript("date_reserve", false) %>

	        <%
     	} else if (action.equalsIgnoreCase("given")) {

    		bcNatPrsGiftObject gift = new bcNatPrsGiftObject(id);
    		
    		bcClubActionGiftObject event_gift = new bcClubActionGiftObject(gift.getValue("ID_CLUB_EVENT_GIFT"));
    		
		        %> 
			<%= Bean.getOperationTitle(
					Bean.club_actionXML.getfieldTransl("h_gift_give", false),
					"Y",
					"N") 
			%>
	        <form action="../crm/club_event/deliveryupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">
		        <input type="hidden" name="type" value="general">
		        <input type="hidden" name="action" value="given2">
		        <input type="hidden" name="process" value="no">
		        <input type="hidden" name="id" value="<%=id %>">
			<table <%=Bean.getTableDetailParam() %>>
			<tr>
				<td><%= Bean.club_actionXML.getfieldTransl("full_name", false) %>
					<%= Bean.getGoToNatPrsLink(gift.getValue("ID_NAT_PRS")) %>
				</td><td><input type="text" name="full_name" size="60" value="<%= gift.getValue("FULL_NAME") %>" readonly="readonly" class="inputfield-ro"></td>
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
	 		    <td><%= Bean.club_actionXML.getfieldTransl("desc_lg_gift", false) %>
					<%= Bean.getGoToClubEventWarehouseGiftLink(gift.getValue("ID_LG_GIFT")) %>
				</td>
			  	<td>
					<input type="text" name="desc_lg_gift" size="60" value="<%= gift.getValue("DESC_LG_GIFT") %>" readonly="readonly" class="inputfield-ro">
		  		</td>
			</tr>
			<% if ("NONE".equalsIgnoreCase(event_gift.getValue("CD_GIVEN_EVENT_TYPE"))) { %>
		        <tr>
					<td><b><%= Bean.club_actionXML.getfieldTransl("cd_given_event_type", false) %></b></td>
					<td><%= Bean.club_actionXML.getfieldTransl("event_type_none", false) %></td>
				</tr>
			<% } %>

			<% if ("UP_CATEGORIES".equalsIgnoreCase(event_gift.getValue("CD_GIVEN_EVENT_TYPE"))) { %>
		        <tr>
					<td><b><%= Bean.club_actionXML.getfieldTransl("cd_given_event_type", false) %></b></td>
					<%if ("Y".equalsIgnoreCase(gift.getValue("GE_IS_UP_BON_CATEGORY"))) { %>
					<td><%= Bean.club_actionXML.getfieldTransl("event_type_up_categories_bon", false) %>, номер категории <input type="text" name="ge_up_bon_category_number" id="ge_up_bon_category_number" size="10" value="<%= event_gift.getValue("GE_UP_BON_CATEGORY_NUMBER") %>" class="inputfield"></td>
					<%} %>
				</tr>
				<%if ("Y".equalsIgnoreCase(gift.getValue("GE_IS_UP_BON_CATEGORY"))) { %>
		        <tr>
					<td>&nbsp;</td>
					<td><%= Bean.club_actionXML.getfieldTransl("event_type_up_categories_disc", false) %>, номер категории <input type="text" name="ge_up_disc_category_number" id="ge_up_disc_category_number" size="10" value="<%= event_gift.getValue("GE_UP_DISC_CATEGORY_NUMBER") %>" class="inputfield"></td>
				</tr>
				<%} %>
			<% } %>

			<% if ("WRITE_OFF_GOODS".equalsIgnoreCase(event_gift.getValue("CD_GIVEN_EVENT_TYPE"))) { %>
		        <tr>
					<%if ("WRITE_OFF_BONS".equalsIgnoreCase(event_gift.getValue("GE_WRITE_OFF_TYPE"))) { %>
					<td><b><%= Bean.club_actionXML.getfieldTransl("cd_given_event_type", false) %></b></td>
					<td><%= Bean.club_actionXML.getfieldTransl("event_type_write_off_bons", false) %>, сумма <input type="text" name="ge_write_off_bon_value" id="ge_write_off_bon_value" size="10" value="<%= event_gift.getValue("GE_WRITE_OFF_BON_VALUE_FRMT") %>" class="inputfield"></td>
					<% } %>
				</tr>
				<%if ("WRITE_OFF_GOODS_FROM_PURSE".equalsIgnoreCase(event_gift.getValue("GE_WRITE_OFF_TYPE"))) { %>
		        <tr>
					<td><b><%= Bean.club_actionXML.getfieldTransl("cd_given_event_type", false) %></b></td>
					<td><%= Bean.club_actionXML.getfieldTransl("event_type_write_off_purse", false) %>&nbsp;<select name="event_type_write_off_purse_type" id="event_type_write_off_purse_type"  class="inputfield" title="<%= Bean.club_actionXML.getfieldTransl("cd_card_purse_type", false) %>"><%= Bean.getClubCardPurseTypeOptions(event_gift.getValue("GE_WRITE_OFF_CD_PURSE_TYPE"), true) %></select>, сумма <input type="text" name="ge_write_off_purse_value" id="ge_write_off_purse_value" size="10" value="<%= event_gift.getValue("GE_WRITE_OFF_PURSE_VALUE_FRMT") %>" class="inputfield"></td>
				</tr>
				<% } %>
			<% } %>

			<% if ("ADD_GOODS".equalsIgnoreCase(event_gift.getValue("CD_GIVEN_EVENT_TYPE"))) { %>
		        <tr>
					<td><b><%= Bean.club_actionXML.getfieldTransl("cd_given_event_type", false) %></b></td>
				</tr>
		        <tr>
					<td colspan="2"><%= Bean.club_actionXML.getfieldTransl("event_type_add_goods", false) %></td>
				</tr>
				<tr>
					<td colspan="2">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="radio" name="event_type_add_goods_value" id="event_type_add_goods_bons" value="ADD_BONS" <%if ("ADD_BONS".equalsIgnoreCase(event_gift.getValue("GE_ADD_GOOD_TYPE"))) { %>CHECKED<%} %> class="inputfield"><label class="checbox_label" for="event_type_add_goods_bons"><%= Bean.club_actionXML.getfieldTransl("event_type_add_goods_bons", false) %></label>, сумма <input type="text" name="ge_add_good_bon_value" id="ge_add_good_bon_value" size="10" value="<%= event_gift.getValue("GE_ADD_GOOD_BON_VALUE_FRMT") %>" class="inputfield"></td>
				</tr>
				<tr>
					<td colspan="2">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="radio" name="event_type_add_goods_value" id="event_type_add_goods_purse" value="ADD_GOODS_TO_PURSE" <%if ("ADD_GOODS_TO_PURSE".equalsIgnoreCase(event_gift.getValue("GE_ADD_GOOD_TYPE"))) { %>CHECKED<%} %> class="inputfield"><label class="checbox_label" for="event_type_add_goods_purse"><%= Bean.club_actionXML.getfieldTransl("event_type_add_goods_purse", false) %></label>&nbsp;<select name="event_type_add_goods_purse_type" id="event_type_add_goods_purse_type" class="inputfield" title="<%= Bean.club_actionXML.getfieldTransl("cd_card_purse_type", false) %>"><%= Bean.getClubCardPurseTypeOptions(event_gift.getValue("GE_ADD_GOOD_CD_PURSE_TYPE"), true) %></select>, сумма <input type="text" name="ge_add_good_purse_value" id="ge_add_good_purse_value" size="10" value="<%= event_gift.getValue("GE_ADD_GOOD_PURSE_VALUE_FRMT") %>" class="inputfield"></td>
				</tr>
			<% } %>
	
			<tr>
				<td colspan="6" align="center">
					<%=Bean.getSubmitButtonAjax("../crm/club_event/deliveryupdate.jsp") %>
					<%=Bean.getResetButton() %>
					<% if ("add".equalsIgnoreCase(action)) { %>
						<%=Bean.getGoBackButton("../crm/club_event/delivery.jsp") %>
					<% } else { %>
						<%=Bean.getGoBackButton("../crm/club_event/deliveryspecs.jsp?id=" + id) %>
					<% } %>
				</td>
			</tr>
		</table>
		</form>

		        <%
    	} else if (action.equalsIgnoreCase("given2")) {

    		bcNatPrsGiftObject gift = new bcNatPrsGiftObject(id);
    		String
    			event_type_put_gifts	= Bean.getDecodeParam(parameters.get("event_type_put_gifts"));
    		
    		String lEventCaption = "";
    		if ("NONE".equalsIgnoreCase(event_type_put_gifts)) {
    			lEventCaption =  Bean.club_actionXML.getfieldTransl("event_type_put_gifts_none", false);
    		} else if ("WRITE_OFF_BONS".equalsIgnoreCase(event_type_put_gifts)) {
    			lEventCaption =  Bean.club_actionXML.getfieldTransl("event_type_put_gifts_write_off_bons", false);
    		} else if ("WRITE_OFF_GOODS_FROM_PURSE".equalsIgnoreCase(event_type_put_gifts)) {
    			lEventCaption =  Bean.club_actionXML.getfieldTransl("event_type_put_gifts_write_off_purse", false);
    		}

		        %> 
		<script>
			var formData = new Array (
					new Array ('date_given', 'varchar2', 1),
					new Array ('name_gifts_given_place', 'varchar2', 1),
					new Array ('name_lg_gift', 'varchar2', 1),
					new Array ('reason_write_off_goods', 'varchar2', 1)
			);
			var formBonCard = new Array (
					new Array ('write_off_amount', 'varchar2', 1),
					new Array ('id_card', 'varchar2', 1)
			);
			var formPurse = new Array (
					new Array ('cd_card_purse_type', 'varchar2', 1)
			);
			<% if ("WRITE_OFF_BONS".equalsIgnoreCase(event_type_put_gifts) ||
					"WRITE_OFF_GOODS_FROM_PURSE".equalsIgnoreCase(event_type_put_gifts)) { %>
				formData = formData.concat(formBonCard);
			<% } %>
			<% if ("WRITE_OFF_GOODS_FROM_PURSE".equalsIgnoreCase(event_type_put_gifts)) { %>
				formData = formData.concat(formPurse);
			<% } %>
		</script>
			<%= Bean.getOperationTitle(
					Bean.club_actionXML.getfieldTransl("h_gift_add", false),
					"Y",
					"Y") 
			%>
	        <form action="../crm/club_event/deliveryupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">
		        <input type="hidden" name="type" value="general">
		        <input type="hidden" name="action" value="given">
		        <input type="hidden" name="process" value="yes">
		        <input type="hidden" name="id" value="<%=id %>">
		        <input type="hidden" name="id_gift" id="id_gift" value="<%=gift.getValue("ID_GIFT") %>">
		        <input type="hidden" name="write_off_gooods_action" value="<%=event_type_put_gifts %>">
			<table <%=Bean.getTableDetailParam() %>>
			<tr>
				<td><%= Bean.club_actionXML.getfieldTransl("full_name", false) %>
					<%= Bean.getGoToNatPrsLink(gift.getValue("ID_NAT_PRS")) %>
				</td><td><input type="text" name="full_name" size="60" value="<%= gift.getValue("FULL_NAME") %>" readonly="readonly" class="inputfield-ro"></td>
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
				<td><%= Bean.club_actionXML.getfieldTransl("cost_gift", false) %></td><td><input type="text" name="cost_gift" size="20" value="<%= gift.getValue("COST_GIFT_FRMT") %> <%= gift.getValue("SNAME_CURRENCY") %>" readonly="readonly" class="inputfield-ro"></td>
			</tr>
			<tr>
				<td><%= Bean.club_actionXML.getfieldTransl("date_given", true) %></td> <td><%=Bean.getCalendarInputField("date_given", Bean.getSysDate(), "10") %></td>
			</tr>
			<tr>
	 		    <td><%= Bean.club_actionXML.getfieldTransl("gifts_given_place", true) %></td>
				<td>
					<%=Bean.getWindowFindServicePlace("gifts_given_place", "", "", "40") %>
				</td>
			</tr>
			<tr>
	 		    <td><%= Bean.club_actionXML.getfieldTransl("desc_lg_gift", true) %></td>
			  	<td>
					<%=Bean.getWindowFindGiftsLogistic("lg_gift", "'+document.getElementById('id_gift').value+'", "", "", "40") %>
			</tr>
			<tr>
				<td colspan="2">&nbsp;</td>
			</tr>
	        <tr>
				<td><%= Bean.club_actionXML.getfieldTransl("event_type_put_gifts", false) %></td><td><b><i><%= lEventCaption %></i></b></td>
			</tr>
			<% if ("WRITE_OFF_BONS".equalsIgnoreCase(event_type_put_gifts) ||
					"WRITE_OFF_GOODS_FROM_PURSE".equalsIgnoreCase(event_type_put_gifts)) { %>
	        <tr>
				<td><%= Bean.club_actionXML.getfieldTransl("write_off_amount", true) %></td><td><input type="text" name="write_off_amount" size="20" value="<%= gift.getValue("COST_GIFT_FRMT") %>" class="inputfield"></td>
			</tr>
			<% } %>
			<% if ("WRITE_OFF_BONS".equalsIgnoreCase(event_type_put_gifts) ||
					"WRITE_OFF_GOODS_FROM_PURSE".equalsIgnoreCase(event_type_put_gifts)) { %>
	        <tr>
				<td><%= Bean.getClubCardXMLFieldTransl("cd_card1", true) %>
					<% String cd_card1 = Bean.getClubCardCode(gift.getValue("CARD_SERIAL_NUMBER") + "_" + gift.getValue("CARD_ID_ISSUER") + "_" + gift.getValue("CARD_ID_PAYMENT_SYSTEM")); %>
					<%= Bean.getGoToClubCardLink(
							gift.getValue("CARD_SERIAL_NUMBER"),
							gift.getValue("CARD_ID_ISSUER"),
							gift.getValue("CARD_ID_PAYMENT_SYSTEM")
						) %>
				</td>
				<td>
					<% String id_card = gift.getValue("CARD_SERIAL_NUMBER")+"_"+gift.getValue("CARD_ID_ISSUER")+"_"+gift.getValue("CARD_ID_PAYMENT_SYSTEM"); %>
					<select name="id_card" class="inputfield"><%= Bean.getNatPrsCardsOptions(id_card, gift.getValue("ID_NAT_PRS"), true) %> </select>
				</td>
			</tr>
			<% } %>
			<% if ("WRITE_OFF_GOODS_FROM_PURSE".equalsIgnoreCase(event_type_put_gifts)) { %>
	        <tr>
				<td><%= Bean.club_actionXML.getfieldTransl("card_purse", true) %></td><td><select name="cd_card_purse_type" class="inputfield"><%= Bean.getClubCardPurseTypeOptions(gift.getValue("CD_CARD_PURSE_TYPE"), true) %> </select></td>
			</tr>
			<% } %>
			<% if ("WRITE_OFF_BONS".equalsIgnoreCase(event_type_put_gifts) ||
					"WRITE_OFF_GOODS_FROM_PURSE".equalsIgnoreCase(event_type_put_gifts)) { %>
	        <tr>
				<td><%= Bean.club_actionXML.getfieldTransl("reason_write_off_goods", true) %></td><td><textarea name="reason_write_off_goods" cols="56" rows="3" class="inputfield"></textarea></td>
			</tr>
			<% } %>
			<tr>
				<td colspan="6" align="center">
					<%=Bean.getSubmitButtonAjax("../crm/club_event/deliveryupdate.jsp") %>
					<%=Bean.getResetButton() %>
					<% if ("add".equalsIgnoreCase(action)) { %>
						<%=Bean.getGoBackButton("../crm/club_event/delivery.jsp") %>
					<% } else { %>
						<%=Bean.getGoBackButton("../crm/club_event/deliveryspecs.jsp?id=" + id) %>
					<% } %>
				</td>
			</tr>
		</table>
		</form>
		<%= Bean.getCalendarScript("date_given", false) %>

        <%
    	} else if (action.equalsIgnoreCase("return_client")) {

    		bcNatPrsGiftObject gift = new bcNatPrsGiftObject(id);
	        %> 
		<script>
			var formData = new Array (
					new Array ('date_returned', 'varchar2', 1)
			);
		</script>
			<%= Bean.getOperationTitle(
					Bean.club_actionXML.getfieldTransl("h_gift_add", false),
					"Y",
					"Y") 
			%>
	        <form action="../crm/club_event/deliveryupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">
		        <input type="hidden" name="type" value="general">
		        <input type="hidden" name="action" value="return_client">
		        <input type="hidden" name="process" value="yes">
		        <input type="hidden" name="id" value="<%=id %>">
			<table <%=Bean.getTableDetailParam() %>>
			<tr>
				<td><%= Bean.club_actionXML.getfieldTransl("full_name", false) %>
					<%= Bean.getGoToNatPrsLink(gift.getValue("ID_NAT_PRS")) %>
				</td><td><input type="text" name="full_name" size="60" value="<%= gift.getValue("FULL_NAME") %>" readonly="readonly" class="inputfield-ro"></td>
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
				<td><%= Bean.club_actionXML.getfieldTransl("cost_gift", false) %></td><td><input type="text" name="cost_gift" size="20" value="<%= gift.getValue("COST_GIFT_FRMT") %> <%= gift.getValue("SNAME_CURRENCY") %>" readonly="readonly" class="inputfield-ro"></td>
			</tr>
			<tr>
				<td><%= Bean.club_actionXML.getfieldTransl("date_returned", true) %></td> <td><%=Bean.getCalendarInputField("date_returned", Bean.getSysDate(), "10") %></td>
			</tr>
			<tr>
				<td><%= Bean.club_actionXML.getfieldTransl("reason_return", false) %></td> <td><textarea name="reason_return" cols="56" rows="3" class="inputfield"></textarea></td>
			</tr>
			<tr>
				<td colspan="6" align="center">
					<%=Bean.getSubmitButtonAjax("../crm/club_event/deliveryupdate.jsp") %>
					<%=Bean.getResetButton() %>
					<% if ("add".equalsIgnoreCase(action)) { %>
						<%=Bean.getGoBackButton("../crm/club_event/delivery.jsp") %>
					<% } else { %>
						<%=Bean.getGoBackButton("../crm/club_event/deliveryspecs.jsp?id=" + id) %>
					<% } %>
				</td>
			</tr>
		</table>
		</form>
		<%= Bean.getCalendarScript("date_returned", false) %>

		        <%
    	} else if (action.equalsIgnoreCase("cancel")) {

    		bcNatPrsGiftObject gift = new bcNatPrsGiftObject(id);
	        %> 
		<script>
			var formData = new Array (
					new Array ('date_canceled', 'varchar2', 1)
			);
		</script>
			<%= Bean.getOperationTitle(
					Bean.club_actionXML.getfieldTransl("h_gift_add", false),
					"Y",
					"Y") 
			%>
	        <form action="../crm/club_event/deliveryupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">
		        <input type="hidden" name="type" value="general">
		        <input type="hidden" name="action" value="cancel">
		        <input type="hidden" name="process" value="yes">
		        <input type="hidden" name="id" value="<%=id %>">
			<table <%=Bean.getTableDetailParam() %>>
			<tr>
				<td><%= Bean.club_actionXML.getfieldTransl("full_name", false) %>
					<%= Bean.getGoToNatPrsLink(gift.getValue("ID_NAT_PRS")) %>
				</td><td><input type="text" name="full_name" size="60" value="<%= gift.getValue("FULL_NAME") %>" readonly="readonly" class="inputfield-ro"></td>
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
				<td><%= Bean.club_actionXML.getfieldTransl("cost_gift", false) %></td><td><input type="text" name="cost_gift" size="20" value="<%= gift.getValue("COST_GIFT_FRMT") %> <%= gift.getValue("SNAME_CURRENCY") %>" readonly="readonly" class="inputfield-ro"></td>
			</tr>
			<tr>
				<td><%= Bean.club_actionXML.getfieldTransl("date_canceled", true) %></td> <td><%=Bean.getCalendarInputField("date_canceled", Bean.getSysDate(), "10") %></td>
			</tr>
			<tr>
				<td><%= Bean.club_actionXML.getfieldTransl("reason_cancel", false) %></td> <td><textarea name="reason_cancel" cols="56" rows="3" class="inputfield"></textarea></td>
			</tr>
			<tr>
				<td colspan="6" align="center">
					<%=Bean.getSubmitButtonAjax("../crm/club_event/deliveryupdate.jsp") %>
					<%=Bean.getResetButton() %>
					<% if ("add".equalsIgnoreCase(action)) { %>
						<%=Bean.getGoBackButton("../crm/club_event/delivery.jsp") %>
					<% } else { %>
						<%=Bean.getGoBackButton("../crm/club_event/deliveryspecs.jsp?id=" + id) %>
					<% } %>
				</td>
			</tr>
		</table>
		</form>
		<%= Bean.getCalendarScript("date_canceled", false) %>

		        <%
    	} else {
    	    %> <%= Bean.getUnknownActionText(action) %><%
    	}
	
  	} else if (process.equalsIgnoreCase("yes")) {
    	String
    		id_nat_prs						= Bean.getDecodeParam(parameters.get("id_nat_prs")),
			id_nat_prs_gift					= Bean.getDecodeParam(parameters.get("id_nat_prs_gift")),
    		cd_nat_prs_gift_state			= Bean.getDecodeParam(parameters.get("cd_nat_prs_gift_state")),
    		date_reserve 					= Bean.getDecodeParam(parameters.get("date_reserve")),
    		date_given						= Bean.getDecodeParam(parameters.get("date_given")),
    		date_returned 					= Bean.getDecodeParam(parameters.get("date_returned")),
    		reason_return 					= Bean.getDecodeParam(parameters.get("reason_return")),
    		date_canceled		 			= Bean.getDecodeParam(parameters.get("date_canceled")),
    		reason_cancel		 			= Bean.getDecodeParam(parameters.get("reason_cancel")),
    		reason_write_off_goods 			= Bean.getDecodeParam(parameters.get("reason_write_off_goods")),
    		id_club_event_gift				= Bean.getDecodeParam(parameters.get("id_club_event_gift")),
    		id_lg_gift		 				= Bean.getDecodeParam(parameters.get("id_lg_gift")),
    		id_gifts_given_place			= Bean.getDecodeParam(parameters.get("id_gifts_given_place")),
    		write_off_gooods_action         = Bean.getDecodeParam(parameters.get("write_off_gooods_action")),
			id_card							= Bean.getDecodeParam(parameters.get("id_card")),
			cd_card_purse_type				= Bean.getDecodeParam(parameters.get("cd_card_purse_type")),
			write_off_amount				= Bean.getDecodeParam(parameters.get("write_off_amount"));

    	if (action.equalsIgnoreCase("add")) {

	   		String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_CLUB_ACTION.add_nat_prs_gift("+  
	        	"?,?,?,?,?,?,?,?)}";

	    	String[] pParam = new String [6];
			
			pParam[0] = id_nat_prs;
			pParam[1] = "";
			pParam[2] = date_reserve;
			pParam[3] = id_club_event_gift;
			pParam[4] = id_lg_gift;
			pParam[5] = Bean.getDateFormat();
	    	

		 	%>
			<%= Bean.getCallResultParam("INSERT", callSQL, pParam, "../crm/club_event/deliveryspecs.jsp?id=" , "../crm/club_event/delivery.jsp") %>
			<% 	
	    
    	} else if (action.equalsIgnoreCase("given")) { %> 
	        <% 
	        String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_CLUB_ACTION.give_nat_prs_gift(" + 
	        	"?,?,?,?,?,?,?,?,?,?,?)}";

		    String[] pParam = new String [10];
				
			pParam[0] = id;
			pParam[1] = date_given;
			pParam[2] = id_lg_gift;
			pParam[3] = id_gifts_given_place;
			pParam[4] = reason_write_off_goods;
			pParam[5] = write_off_gooods_action;
			pParam[6] = id_card;
			pParam[7] = cd_card_purse_type;
			pParam[8] = write_off_amount;
			pParam[9] = Bean.getDateFormat();
	
		 	%>
			<%= Bean.getCallResultParam("UPDATE", callSQL, pParam, "../crm/club_event/deliveryspecs.jsp?id=" + id, "") %>
			<% 	
     
    	} else if (action.equalsIgnoreCase("delete_given")) {

	   		String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_CLUB_ACTION.delete_give_nat_prs_gift(?,?)}";

		    String[] pParam = new String [1];
				
			pParam[0] = id;

		 	%>
			<%= Bean.getCallResultParam("UPDATE", callSQL, pParam, "../crm/club_event/deliveryspecs.jsp?id=" + id, "") %>
			<% 	
	    
    	} else if (action.equalsIgnoreCase("return_client")) { %> 
	        <% 
	        String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_CLUB_ACTION.return_client_nat_prs_gift(" + 
	        	"?,?,?,?,?)}";

			String[] pParam = new String [4];
					
			pParam[0] = id;
			pParam[1] = date_returned;
			pParam[2] = reason_return;
			pParam[3] = Bean.getDateFormat();
	
		 	%>
			<%= Bean.getCallResultParam("UPDATE", callSQL, pParam, "../crm/club_event/deliveryspecs.jsp?id=" + id, "") %>
			<% 	
 
    	} else if (action.equalsIgnoreCase("delete_return")) { %> 
	        <% 
	        String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_CLUB_ACTION.delete_return_nat_prs_gift(?,?)}";

			String[] pParam = new String [1];
					
			pParam[0] = id;
	
		 	%>
			<%= Bean.getCallResultParam("UPDATE", callSQL, pParam, "../crm/club_event/deliveryspecs.jsp?id=" + id, "") %>
			<% 	

    	} else if (action.equalsIgnoreCase("cancel")) { %> 
	        <% 
	        String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_CLUB_ACTION.cancel_nat_prs_gift(" + 
	        	"?,?,?,?,?)}";

			String[] pParam = new String [4];
						
			pParam[0] = id;
			pParam[1] = date_canceled;
			pParam[2] = reason_cancel;
			pParam[3] = Bean.getDateFormat();
	
		 	%>
			<%= Bean.getCallResultParam("UPDATE", callSQL, pParam, "../crm/club_event/deliveryspecs.jsp?id=" + id, "") %>
			<% 	
	
		} else if (action.equalsIgnoreCase("delete_cancel")) { %> 
	        <% 
	        String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_CLUB_ACTION.delete_cancel_nat_prs_gift(?,?)}";

			String[] pParam = new String [1];
						
			pParam[0] = id;
	
		 	%>
			<%= Bean.getCallResultParam("UPDATE", callSQL, pParam, "../crm/club_event/deliveryspecs.jsp?id=" + id, "") %>
			<% 	

    	} else if (action.equalsIgnoreCase("remove")) {

	   		String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_CLUB_ACTION.delete_winner(?,?)}";

	    	String[] pParam = new String [1];
			
			pParam[0] = id;

		 	%>
			<%= Bean.getCallResultParam("DELETE", callSQL, pParam, "../crm/club_event/delivery.jsp" , "") %>
			<% 	
	    
    	} else if (action.equalsIgnoreCase("edit")) { %> 
	        <% 
	        String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_CLUB_ACTION.update_winner2(" + 
	        	"?,?,?,?,?,?,?,?,?,?,?,?,?)}";

			String[] pParam = new String [12];
							
			pParam[0] = id;
			pParam[1] = cd_nat_prs_gift_state;
			pParam[2] = date_reserve;
			pParam[3] = date_given;
			pParam[4] = date_returned;
			pParam[5] = date_canceled;
			pParam[6] = id_club_event_gift;
			pParam[7] = id_lg_gift;
			pParam[8] = id_gifts_given_place;
			pParam[9] = id_card;
			pParam[10] = cd_card_purse_type;
			pParam[11] = Bean.getDateFormat();
	
		 	%>
			<%= Bean.getCallResultParam("UPDATE", callSQL, pParam, "../crm/club_event/deliveryspecs.jsp?id=" + id, "") %>
			<% 	
	     
    	} else { %> 
    		<%= Bean.getUnknownActionText(action) %><% 
    	}
	} else {%> 
		<%= Bean.getUnknownProcessText(process) %> <%
  	}
} else if (type.equalsIgnoreCase("tasks")) {
	if (process.equalsIgnoreCase("no")) {
		if (action.equalsIgnoreCase("add")) {
	        
			bcNatPrsGiftObject gift = new bcNatPrsGiftObject(id);
			
	        %>
			<script>
			var formData = new Array (
				new Array ('id_card', 'varchar2', 1)
			);
			</script>
			<%= Bean.getOperationTitle(
					Bean.card_taskXML.getfieldTransl("h_task_add", false),
					"Y",
					"N") 
			%>
        <form action="../crm/club_event/deliveryupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST">
			<input type="hidden" name="type" value="tasks">
			<input type="hidden" name="action" value="add2">
			<input type="hidden" name="process" value="no">
			<input type="hidden" name="id" value="<%=id %>">
			<table <%=Bean.getTableDetailParam() %>>
		        <tr>
					<td><%= Bean.getClubCardXMLFieldTransl("cd_card1", true) %>
						<% String cd_card1 = Bean.getClubCardCode(gift.getValue("CARD_SERIAL_NUMBER") + "_" + gift.getValue("CARD_ID_ISSUER") + "_" + gift.getValue("CARD_ID_PAYMENT_SYSTEM")); %>
						<%= Bean.getGoToClubCardLink(
								gift.getValue("CARD_SERIAL_NUMBER"),
								gift.getValue("CARD_ID_ISSUER"),
								gift.getValue("CARD_ID_PAYMENT_SYSTEM")
							) %>
					</td>
					<td>
						<% String id_card = gift.getValue("CARD_SERIAL_NUMBER")+"_"+gift.getValue("CARD_ID_ISSUER")+"_"+gift.getValue("CARD_ID_PAYMENT_SYSTEM"); %>
						<select name="id_card" class="inputfield"><%= Bean.getNatPrsCardsOptions(id_card, gift.getValue("ID_NAT_PRS"), true) %> </select>
					</td>
				</tr>
				<tr>
					<%
					  	String pOperationType = Bean.getDecodeParam(parameters.get("cd_card_operation_type"));
						if (pOperationType==null || "".equalsIgnoreCase(pOperationType)) {
							String pUserOperationType = Bean.getUIUserParam("CARD_OPERATION_TYPE");	
							if (pUserOperationType==null || "".equalsIgnoreCase(pUserOperationType)) {
								pOperationType = "BLOCK_CARD";
							} else {
								pOperationType = pUserOperationType;
							}
						}
					%>
					<td><%=Bean.card_taskXML.getfieldTransl("cd_card_operation_type", true)%></td><td><%= Bean.getClubCardOperationTypeRadio("cd_card_operation_type", pOperationType) %></td>
				</tr>
		 		<tr>
					<td colspan="2" align="center">
						<%=Bean.getSubmitButtonAjax("../crm/club_event/deliveryupdate.jsp") %>
						<%=Bean.getGoBackButton("../crm/club_event/deliveryspecs.jsp?id=" + id) %>
					</td>
				</tr>
		
			</table>
		</form>

	        <%
	        /*  --- Видалити запис --- */
    	} else if (action.equalsIgnoreCase("add2")) {
    	    
    		String 
    			cd_card_operation_type 	= Bean.getDecodeParam(parameters.get("cd_card_operation_type")),
    			id_card				 	= Bean.getDecodeParam(parameters.get("id_card"));
    		
    		bcNatPrsGiftObject gift = new bcNatPrsGiftObject(id);
    		
	        %>

		<script>
			var formData = new Array (
				new Array ('id_card', 'varchar2', 1),
				new Array ('begin_action_date', 'varchar2', 1),
				new Array ('need_apply', 'varchar2', 1),
				new Array ('basis_for_operation', 'varchar2', 1)
			);
			var formDataCHANGE_PARAM = new Array (
				new Array ('id_card_status', 'varchar2', 1)
			);
			var formDataWRITE_OFF_BON = new Array (
				new Array ('bal_full', 'varchar2', 1)
			);
			var formDataSEND_MESSAGE = new Array (
				new Array ('text_message', 'varchar2', 1)
			);
			var formDataSET_CATEGORIES_ON_PERIOD = new Array (
				new Array ('end_action_date', 'varchar2', 1),
				new Array ('id_card_status', 'varchar2', 1)
			);
			var formDataPurseOperations = new Array (
				new Array ('id_card_purse', 'varchar2', 1),
				new Array ('value_card_purse', 'varchar2', 1)
			);
			<% if ("CHANGE_PARAM".equalsIgnoreCase(cd_card_operation_type)) { %>
				formData = formData.concat(formDataCHANGE_PARAM);
			<%} else if ("WRITE_OFF_BON".equalsIgnoreCase(cd_card_operation_type)) {%>
				formData = formData.concat(formDataWRITE_OFF_BON);
			<%} else if ("SET_CATEGORIES_ON_PERIOD".equalsIgnoreCase(cd_card_operation_type)) {%>
				formData = formData.concat(formDataSET_CATEGORIES_ON_PERIOD);
			<% } else if ("SEND_MESSAGE".equalsIgnoreCase(cd_card_operation_type)) { %>
				formData = formData.concat(formDataSEND_MESSAGE);
			<% } else if ("ADD_GOODS_TO_PURSE".equalsIgnoreCase(cd_card_operation_type) ||
					"WRITE_OFF_GOODS_FROM_PURSE".equalsIgnoreCase(cd_card_operation_type)) { %>
				formData = formData.concat(formDataPurseOperations);
			<% } %>
	
			function myValidateForm() {
				return validateForm(formData);
			}

			<% if ("CHANGE_PARAM".equalsIgnoreCase(cd_card_operation_type) ||
					"SET_CATEGORIES_ON_PERIOD".equalsIgnoreCase(cd_card_operation_type)) { %>
			dwr_get_card_bon_disc_category3('',document.getElementById('id_card_status'),'','','<%=Bean.getSessionId() %>');
			<% } %>
			
		</script>

		<%= Bean.getMessageLengthTextAreaInitialScript("text_message", "length_message") %>

		<body>
			<%= Bean.getOperationTitle(
					Bean.card_taskXML.getfieldTransl("h_task_add", false),
					"Y",
					"Y") 
			%>
        <form action="../crm/club_event/deliveryupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST">
	        <input type="hidden" name="type" value="tasks">
	        <input type="hidden" name="action" value="add">
	        <input type="hidden" name="process" value="yes">
			<input type="hidden" name="cd_card_operation_type" value="<%= cd_card_operation_type %>">
			<input type="hidden" name="id" value="<%=id %>">
			<input type="hidden" name="id_card" value="<%=id_card %>">
		<table <%=Bean.getTableDetailParam() %>>
		<tr>
			<td><%= Bean.card_taskXML.getfieldTransl("CD_CARD_OPERATION_TYPE", false) %></td> <td><input type="text" name="name_card_operation_type" size="40" value="<%= Bean.getClubCardOperationTypeName(cd_card_operation_type) %>" readonly="readonly" class="inputfield-ro"> </td>
			<td><%= Bean.card_taskXML.getfieldTransl("cd_card1", false) %></td> 
			<td>
				<input type="text" name="cd_card1" size="40" value="<%= Bean.getClubCardCode(id_card) %> " readonly="readonly" class="inputfield-ro">
			</td>			
		</tr>
		
		<% if ("SET_CATEGORIES_ON_PERIOD".equalsIgnoreCase(cd_card_operation_type)) {	%>
		<tr>
			<td><%= Bean.card_taskXML.getfieldTransl("CD_CARD_OPERATION_TYPE", false) %></td> <td><input type="text" name="name_card_operation_type" size="40" value="<%= Bean.getClubCardOperationTypeName(cd_card_operation_type) %>" readonly="readonly" class="inputfield-ro"> </td>
		    <td><%= Bean.card_taskXML.getfieldTransl("begin_period_date", true) %></td><td><%=Bean.getCalendarInputField("begin_action_date", Bean.getSysDate(), "10") %></td>
		</tr>
		<tr>
			<%  String cd_app_card_resp_action = Bean.getClubCardOperationTypeAction(cd_card_operation_type);
				String name_app_card_resp_action = Bean.getMeaningForNumValue("APP_CARD_RESP_ACTION", cd_app_card_resp_action);
			%>
			<td><%= Bean.card_taskXML.getfieldTransl("app_card_resp_action", false) %></td> <td><input type="text" name="app_card_resp_action" size="40" value="<%= cd_app_card_resp_action + " - " + name_app_card_resp_action %>" readonly="readonly" class="inputfield-ro" title = "<%=cd_app_card_resp_action + " - " + name_app_card_resp_action %>"> </td>
		    <td><%= Bean.card_taskXML.getfieldTransl("end_period_date", true) %></td><td><%=Bean.getCalendarInputField("end_action_date", "", "10") %></td>
		</tr>
		<tr>
			<td colspan="2">&nbsp;</td>
			<td><%= Bean.card_taskXML.getfieldTransl("need_apply", true) %></td> <td><select name="need_apply" class="inputfield"><%= Bean.getMeaningFromLookupNameOptions("YES_NO", "Y", false) %></select></td>
		</tr>
		<% } else if ("ADD_GOODS_TO_PURSE".equalsIgnoreCase(cd_card_operation_type) ||
				"WRITE_OFF_GOODS_FROM_PURSE".equalsIgnoreCase(cd_card_operation_type)) {	%>
		<tr>
			<td><%= Bean.card_taskXML.getfieldTransl("CD_CARD_OPERATION_TYPE", false) %></td> <td><input type="text" name="name_card_operation_type" size="40" value="<%= Bean.getClubCardOperationTypeName(cd_card_operation_type) %>" readonly="readonly" class="inputfield-ro"> </td>
		    <td><%= Bean.card_taskXML.getfieldTransl("begin_period_date", true) %></td><td><%=Bean.getCalendarInputField("begin_action_date", Bean.getSysDate(), "10") %></td>
		</tr>
		<tr>
			<td colspan="2">&nbsp;</td>
			<td><%= Bean.card_taskXML.getfieldTransl("need_apply", true) %></td> <td><select name="need_apply" class="inputfield"><%= Bean.getMeaningFromLookupNameOptions("YES_NO", "Y", false) %></select></td>
		</tr>
		<% } else { %>
		<tr>
			<td><%= Bean.card_taskXML.getfieldTransl("CD_CARD_OPERATION_TYPE", false) %></td> <td><input type="text" name="name_card_operation_type" size="40" value="<%= Bean.getClubCardOperationTypeName(cd_card_operation_type) %>" readonly="readonly" class="inputfield-ro"> </td>
		    <td><%= Bean.card_taskXML.getfieldTransl("begin_period_date", true) %></td><td><%=Bean.getCalendarInputField("begin_action_date", Bean.getSysDate(), "10") %></td>
		</tr>
		<tr>
			<%  String cd_app_card_resp_action = Bean.getClubCardOperationTypeAction(cd_card_operation_type);
				String name_app_card_resp_action = Bean.getMeaningForNumValue("APP_CARD_RESP_ACTION", cd_app_card_resp_action);
			%>
			<td><%= Bean.card_taskXML.getfieldTransl("app_card_resp_action", false) %></td> <td><input type="text" name="app_card_resp_action" size="40" value="<%= cd_app_card_resp_action + " - " + name_app_card_resp_action %>" readonly="readonly" class="inputfield-ro" title = "<%=cd_app_card_resp_action + " - " + name_app_card_resp_action %>"> </td>
			<td><%= Bean.card_taskXML.getfieldTransl("need_apply", true) %></td> <td><select name="need_apply" class="inputfield"><%= Bean.getMeaningFromLookupNameOptions("YES_NO", "Y", false) %></select></td>
		</tr>
		<% } %>
		<tr>
			<td><%= Bean.card_taskXML.getfieldTransl("basis_for_operation", true) %></td><td  colspan="3"><textarea name="basis_for_operation" cols="120" rows="2" class="inputfield"></textarea></td>
		</tr>
		<% if ("ADD_GOODS_TO_PURSE".equalsIgnoreCase(cd_card_operation_type)) { %>
		<tr>
		    <td colspan="4" class="top_line"><b><%=Bean.card_taskXML.getfieldTransl("tag_param", false)%></b></td>
		</tr>
		<tr>
			<td><%= Bean.card_taskXML.getfieldTransl("card_purse", true) %></td> 
			<td>
				<select name="id_card_purse" class="inputfield"><%=Bean.getClubCardPurses2(id_card, "", false)%></select>
			</td>
			<td colspan="2">&nbsp;</td> 
		</tr>
		<tr>
			<td><%=Bean.card_taskXML.getfieldTransl("h_value_card_purse_add", true)%></td> <td><input type="text" name="value_card_purse" size="20" value="" class="inputfield"></td>
			<td colspan="2">&nbsp;</td> 
		</tr>
		<% } else if ("WRITE_OFF_GOODS_FROM_PURSE".equalsIgnoreCase(cd_card_operation_type)) { %>
		<tr>
		    <td colspan="4" class="top_line"><b><%=Bean.card_taskXML.getfieldTransl("tag_param", false)%></b></td>
		</tr>
		<tr>
			<td><%= Bean.card_taskXML.getfieldTransl("card_purse", true) %></td> 
			<td>
				<select name="id_card_purse" class="inputfield"><%=Bean.getClubCardPurses2(id_card, "", false)%></select>
			</td>
			<td colspan="2">&nbsp;</td> 
		</tr>
		<tr>
			<td><%=Bean.card_taskXML.getfieldTransl("h_value_card_purse_write_off", true)%></td> <td><input type="text" name="value_card_purse" size="20" value="" class="inputfield"></td>
			<td colspan="2">&nbsp;</td> 
		</tr>
		<% } else if ("ADD_BON".equalsIgnoreCase(cd_card_operation_type)) { %>
		<tr>
		    <td colspan="4" class="top_line"><b><%= Bean.card_taskXML.getfieldTransl("tag_param", false) %></b></td>
		</tr>
		<tr>
			<td><%= Bean.card_taskXML.getfieldTransl("h_bal_acc_add", false) %></td> 
			<td><input type="text" name="bal_acc" size="20" value="" class="inputfield"> </td>

		    <td><%= Bean.card_taskXML.getfieldTransl("date_acc", false) %></td><td><%=Bean.getCalendarInputField("date_acc", "", "10") %></td>
		</tr>
		<tr>
            <td><%= Bean.card_taskXML.getfieldTransl("h_bal_cur_add", false) %></td> 
			<td><input type="text" name="bal_cur" size="20" value="" class="inputfield"> </td>
			<td colspan="2">&nbsp;</td>
		</tr>
		<tr>
			<td valign="top"><%= Bean.card_taskXML.getfieldTransl("text_message", false) %></td><td  colspan="3"><textarea name="text_message" id="text_message" cols="120" rows="2" class="inputfield" <%=Bean.getMessageLengthTextAreaEvent("length_message") %>></textarea></td>
		</tr>
		<tr>
			<td valign="top"><%= Bean.messageXML.getfieldTransl("length_message", false) %></td><td valign="top"><input type="text" name="length_message" id="length_message" size="8" value="" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<% } else if ("WRITE_OFF_BON".equalsIgnoreCase(cd_card_operation_type)) { %>
		<tr>
		    <td colspan="4" class="top_line"><b><%=Bean.card_taskXML.getfieldTransl("tag_param", false)%></b></td>
		</tr>
		<tr>
			<td><%=Bean.card_taskXML.getfieldTransl("h_bal_delete", true)%></td> 
            <td  colspan="3"><input type="text" name="bal_full" size="20" value="" class="inputfield"></td>
		</tr>
		<tr>
			<td valign="top"><%= Bean.card_taskXML.getfieldTransl("text_message", false) %></td><td  colspan="3"><textarea name="text_message" id="text_message" cols="120" rows="2" class="inputfield" <%=Bean.getMessageLengthTextAreaEvent("length_message") %>></textarea></td>
		</tr>
		<tr>
			<td valign="top"><%= Bean.messageXML.getfieldTransl("length_message", false) %></td><td valign="top"><input type="text" name="length_message" id="length_message" size="8" value="" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<% } else if ("CHANGE_PARAM".equalsIgnoreCase(cd_card_operation_type)) { %>
		<tr>
		    <td colspan="4" class="top_line"><b><%= Bean.card_taskXML.getfieldTransl("tag_param", false) %></b></td>
		</tr>
		<tr>
			<td><%= Bean.card_taskXML.getfieldTransl("id_card_status", true) %></td><td><select name="id_card_status" id="id_card_status"  class="inputfield" onchange="dwr_get_card_bon_disc_category3('',this,'','','<%=Bean.getSessionId() %>');"><%=Bean.getClubCardStatusOptions("", false) %> </select></td>
		    <td><%= Bean.getClubCardXMLFieldTransl("date_acc", false) %></td><td><%=Bean.getCalendarInputField("date_acc", "", "10") %></tr>
		<tr>
			<td><%= Bean.card_taskXML.getfieldTransl("id_bon_category", false)  %></td> <td class="top_line_gray"><select name="id_bon_category"  id="id_bon_category" class="inputfield"><option value=""></option></select></td>
		    <td><%= Bean.getClubCardXMLFieldTransl("date_mov", false) %></td><td><%=Bean.getCalendarInputField("date_mov", "", "10") %></tr>
		<tr>
			<td>&nbsp;</td>
			<td>
				<input type="checkbox" name="can_bon_category_reduction" id="can_bon_category_reduction" value="Y">
				<label class="checbox_label" for="can_bon_category_reduction"><%=Bean.card_taskXML.getfieldTransl("can_bon_category_reduction",false) %></label>
			</td>
		    <td><%= Bean.getClubCardXMLFieldTransl("date_onl_next", false) %></td><td><%=Bean.getCalendarInputField("date_onl", "", "10") %></td>
		</tr>
		<tr>
			<td><%= Bean.card_taskXML.getfieldTransl("id_disc_category", false)  %></td> <td class="top_line_gray"><select name="id_disc_category"  id="id_disc_category" class="inputfield"><option value=""></option></select></td>
			<td class="top_line_gray"><%= Bean.getClubCardXMLFieldTransl("bal_acc", false) %></td> <td class="top_line_gray"><input type="text" name="bal_acc" size="20" value="" class="inputfield" title="BAL_ACC"> </td>
		</tr>
		<tr>
			<td>&nbsp;</td>
			<td>
				<input type="checkbox" name="can_disc_category_reduction" id="can_disc_category_reduction" value="Y">
				<label class="checbox_label" for="can_disc_category_reduction"><%=Bean.card_taskXML.getfieldTransl("can_disc_category_reduction",false) %></label>
			</td>
			<td><%= Bean.getClubCardXMLFieldTransl("bal_cur", false) %></td> <td><input type="text" name="bal_cur" size="20" value="" class="inputfield" title="BAL_CUR"> </td>
		</tr>
		<tr>
			<td colspan="2">&nbsp;</td>
			<td><%= Bean.getClubCardXMLFieldTransl("bal_bon_per", false) %></td> <td><input type="text" name="bal_bon_per" size="20" value="" class="inputfield" title="BAL_BON_PER"> </td>
		</tr>
		<tr>
			<td colspan="2">&nbsp;</td>
			<td><%= Bean.getClubCardXMLFieldTransl("bal_disc_per", false) %></td> <td><input type="text" name="bal_disc_per" size="20" value="" class="inputfield" title="BAL_DISC_PER"> </td>
		</tr>
		<tr>
			<td valign="top"><%= Bean.card_taskXML.getfieldTransl("text_message", false) %></td><td  colspan="3"><textarea name="text_message" id="text_message" cols="120" rows="2" class="inputfield" <%=Bean.getMessageLengthTextAreaEvent("length_message") %>></textarea></td>
		</tr>
		<tr>
			<td valign="top"><%= Bean.messageXML.getfieldTransl("length_message", false) %></td><td valign="top"><input type="text" name="length_message" id="length_message" size="8" value="" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<% } else if ("SET_CATEGORIES_ON_PERIOD".equalsIgnoreCase(cd_card_operation_type)) { %>
		<tr>
		    <td colspan="4" class="top_line"><b><%= Bean.card_taskXML.getfieldTransl("tag_param", false) %></b></td>
		</tr>
		<tr>
			<td><%= Bean.card_taskXML.getfieldTransl("id_card_status", true) %></td><td><select name="id_card_status" id="id_card_status"  class="inputfield" onchange="dwr_get_card_bon_disc_category3('',this,'','','<%=Bean.getSessionId() %>');"><%=Bean.getClubCardStatusOptions("", false) %> </select></td>
		    <td><%= Bean.getClubCardXMLFieldTransl("date_onl_next", false) %></td><td><%=Bean.getCalendarInputField("date_onl", "", "10") %></td>
		</tr>
		<tr>
			<td><%= Bean.card_taskXML.getfieldTransl("id_bon_category", false)  %></td> <td class="top_line_gray"><select name="id_bon_category"  id="id_bon_category" class="inputfield"><option value=""></option></select></td>
			<td colspan="2">&nbsp;</td>
		</tr>
		<tr>
			<td>&nbsp;</td>
			<td>
				<input type="checkbox" name="can_bon_category_reduction" id="can_bon_category_reduction" value="Y">
				<label class="checbox_label" for="can_bon_category_reduction"><%=Bean.card_taskXML.getfieldTransl("can_bon_category_reduction",false) %></label>
			</td>
			<td colspan="2">&nbsp;</td>
		</tr>
		<tr>
			<td><%= Bean.card_taskXML.getfieldTransl("id_disc_category", false)  %></td> <td class="top_line_gray"><select name="id_disc_category"  id="id_disc_category" class="inputfield"><option value=""></option></select></td>
			<td colspan="2">&nbsp;</td>
		</tr>
		<tr>
			<td>&nbsp;</td>
			<td>
				<input type="checkbox" name="can_disc_category_reduction" id="can_disc_category_reduction" value="Y">
				<label class="checbox_label" for="can_disc_category_reduction"><%=Bean.card_taskXML.getfieldTransl("can_disc_category_reduction",false) %></label>
			</td>
			<td colspan="2">&nbsp;</td>
		</tr>
		<tr>
			<td valign="top"><%= Bean.card_taskXML.getfieldTransl("text_message", false) %></td><td  colspan="3"><textarea name="text_message" id="text_message" cols="120" rows="2" class="inputfield" <%=Bean.getMessageLengthTextAreaEvent("length_message") %>></textarea></td>
		</tr>
		<tr>
			<td valign="top"><%= Bean.messageXML.getfieldTransl("length_message", false) %></td><td valign="top"><input type="text" name="length_message" id="length_message" size="8" value="" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<% } else if ("BLOCK_CARD".equalsIgnoreCase(cd_card_operation_type)) { %>
		<tr>
		    <td colspan="4" class="top_line"><b><%= Bean.card_taskXML.getfieldTransl("tag_param", false) %></b></td>
		</tr>
		<tr>
			<td valign="top"><%= Bean.card_taskXML.getfieldTransl("text_message", false) %></td><td  colspan="3"><textarea name="text_message" id="text_message" cols="120" rows="2" class="inputfield" <%=Bean.getMessageLengthTextAreaEvent("length_message") %>></textarea></td>
		</tr>
		<tr>
			<td valign="top"><%= Bean.messageXML.getfieldTransl("length_message", false) %></td><td valign="top"><input type="text" name="length_message" id="length_message" size="8" value="" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<% } else if ("SEND_MESSAGE".equalsIgnoreCase(cd_card_operation_type)) { %>
		<tr>
		    <td colspan="4" class="top_line"><b><%= Bean.card_taskXML.getfieldTransl("tag_param", false) %></b></td>
		</tr>
		<tr>
			<td valign="top"><%= Bean.card_taskXML.getfieldTransl("text_message", false) %></td><td  colspan="3"><textarea name="text_message" id="text_message" cols="120" rows="2" class="inputfield" <%=Bean.getMessageLengthTextAreaEvent("length_message") %>></textarea></td>
		</tr>
		<tr>
			<td valign="top"><%= Bean.messageXML.getfieldTransl("length_message", false) %></td><td valign="top"><input type="text" name="length_message" id="length_message" size="8" value="" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<% } %>
 		<tr>
			<td colspan="4" align="center">
				<%=Bean.getSubmitButtonAjax("../crm/club_event/deliveryupdate.jsp") %>
				<%=Bean.getResetButton() %>
				<%=Bean.getGoBackButton("../crm/club_event/deliveryspecs.jsp?id=" + id +"&cd_card_operation_type="+cd_card_operation_type + "&type=tasks&process=no&action=add") %>
			</td>
		</tr>

	</table>

	</form>
	
	<!-- Скрипт для втавки меню вибору дати -->
		<%= Bean.getCalendarScript("begin_action_date", false) %>

		<% if ("SET_CATEGORIES_ON_PERIOD".equalsIgnoreCase(cd_card_operation_type)) {	%>
			<%= Bean.getCalendarScript("end_action_date", false) %>
		<% } %>

    	<% if ("ADD_BON".equalsIgnoreCase(cd_card_operation_type) ||
 			   "CHANGE_PARAM".equalsIgnoreCase(cd_card_operation_type)) { %>
			<%= Bean.getCalendarScript("date_acc", false) %>    	
    	<% } %>

    	<% if ("CHANGE_PARAM".equalsIgnoreCase(cd_card_operation_type)) { %>
			<%= Bean.getCalendarScript("date_mov", false) %>
      	<% } %>
		
    	<%	if (("SET_CATEGORIES_ON_PERIOD".equalsIgnoreCase(cd_card_operation_type) || 
    				"CHANGE_PARAM".equalsIgnoreCase(cd_card_operation_type))) { %>
			<%= Bean.getCalendarScript("date_onl", false) %>
    	<% } %>

	        <%
    	} else if (action.equalsIgnoreCase("edit")) {
    		
    		String id_task 	= Bean.getDecodeParam(parameters.get("id_task"));
    	
    		bcClubCardOperationObject operation = new bcClubCardOperationObject(id_task);
    		
    		bcClubCardObject card = null;
    		if ("WRITE_OFF_BON".equalsIgnoreCase(operation.getValue("CD_CARD_OPERATION_TYPE"))) {
    			card = new bcClubCardObject(
    						operation.getValue("CARD_SERIAL_NUMBER"),
    						operation.getValue("CARD_ID_ISSUER"),
    						operation.getValue("CARD_ID_PAYMENT_SYSTEM"));
    		}
    		bcClubCardPurseObject purse = null;
    		if ("ADD_GOODS_TO_PURSE".equalsIgnoreCase(operation.getValue("CD_CARD_OPERATION_TYPE")) ||
    				"WRITE_OFF_GOODS_FROM_PURSE".equalsIgnoreCase(operation.getValue("CD_CARD_OPERATION_TYPE"))) {
    			purse = new bcClubCardPurseObject(operation.getValue("ID_CARD_PURSE"), Bean.getLanguage(), Bean.getSessionId(), Bean.getDateFormat());
    		}
    	%>
	<script>
		var formData = new Array (
			new Array ('priority', 'varchar2', 1),
			new Array ('begin_action_date', 'varchar2', 1),
			new Array ('cd_card_oper_state', 'varchar2', 1),
			new Array ('need_apply', 'varchar2', 1),
			new Array ('basis_for_operation', 'varchar2', 1)
		);
		var formDataCHANGE_PARAM = new Array (
			new Array ('id_card_status', 'varchar2', 1)
		);
		var formDataWRITE_OFF_BON = new Array (
			new Array ('bal_full', 'varchar2', 1)
		);
		var formDataSEND_MESSAGE = new Array (
			new Array ('text_message', 'varchar2', 1)
		);
		var formDataSET_CATEGORIES_ON_PERIOD = new Array (
			new Array ('end_action_date', 'varchar2', 1),
			new Array ('id_card_status', 'varchar2', 1)
		);
		var formDataPurseOperations = new Array (
			new Array ('value_card_purse', 'varchar2', 1)
		);
		<% if ("CHANGE_PARAM".equalsIgnoreCase(operation.getValue("CD_CARD_OPERATION_TYPE"))) { %>
			formData = formData.concat(formDataCHANGE_PARAM);
		<%} else if ("WRITE_OFF_BON".equalsIgnoreCase(operation.getValue("CD_CARD_OPERATION_TYPE"))) {%>
			formData = formData.concat(formDataWRITE_OFF_BON);
		<%} else if ("SET_CATEGORIES_ON_PERIOD".equalsIgnoreCase(operation.getValue("CD_CARD_OPERATION_TYPE"))) {%>
			formData = formData.concat(formDataSET_CATEGORIES_ON_PERIOD);
		<% } else if ("SEND_MESSAGE".equalsIgnoreCase(operation.getValue("CD_CARD_OPERATION_TYPE"))) { %>
			formData = formData.concat(formDataSEND_MESSAGE);
		<% } else if ("ADD_GOODS_TO_PURSE".equalsIgnoreCase(operation.getValue("CD_CARD_OPERATION_TYPE")) ||
				"WRITE_OFF_GOODS_FROM_PURSE".equalsIgnoreCase(operation.getValue("CD_CARD_OPERATION_TYPE"))) { %>
			formData = formData.concat(formDataPurseOperations);
		<% } %>

		function myValidateForm() {
			return validateForm(formData);
		}

	</script>

	<%= Bean.getMessageLengthTextAreaInitialScript("text_message", "length_message") %>

			<%= Bean.getOperationTitle(
					Bean.card_taskXML.getfieldTransl("h_task_edit", false),
					"Y",
					"N") 
			%>
    <form action="../crm/club_event/deliveryupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">
        <input type="hidden" name="type" value="tasks">
        <input type="hidden" name="action" value="edit">
        <input type="hidden" name="process" value="yes">
		<input type="hidden" name="id" value="<%= id %>">
		<input type="hidden" name="id_card" value="<%= operation.getValue("CARD_SERIAL_NUMBER") %>_<%= operation.getValue("CARD_ID_ISSUER") %>_<%= operation.getValue("CARD_ID_PAYMENT_SYSTEM") %>">
		<input type="hidden" name="cd_card_operation_type" value="<%= operation.getValue("CD_CARD_OPERATION_TYPE") %>">
	<table <%=Bean.getTableDetailParam() %>>
		<tr>
			<td><%= Bean.card_taskXML.getfieldTransl("ID_CARD_OPERATION", false) %></td> <td><input type="text" name="id_task" size="20" value="<%= operation.getValue("ID_CARD_OPERATION") %>" readonly="readonly" class="inputfield-ro"> </td>
			<td><%= Bean.clubXML.getfieldTransl("club", false) %>
				<%= Bean.getGoToClubLink(operation.getValue("ID_CLUB")) %>
			</td><td><input type="text" name="name_club" size="30" value="<%= Bean.getClubShortName(operation.getValue("ID_CLUB")) %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td><%= Bean.card_taskXML.getfieldTransl("cd_card1", false) %>
				<%= Bean.getGoToClubCardLink(
						operation.getValue("CARD_SERIAL_NUMBER"),
						operation.getValue("CARD_ID_ISSUER"),
						operation.getValue("CARD_ID_PAYMENT_SYSTEM")
					) %>
			</td> 
			<td><input type="text" name="cd_card1" size="40" value="<%= operation.getValue("CD_CARD1") %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%=Bean.card_taskXML.getfieldTransl("cd_card_oper_state", true)%></td> <td><select name="cd_card_oper_state" class="inputfield"><%=Bean.getClubCardOperationStateOptions(operation.getValue("CD_CARD_OPER_STATE"),true)%></select></td>
		</tr>
		<% if ("SET_CATEGORIES_ON_PERIOD".equalsIgnoreCase(operation.getValue("CD_CARD_OPERATION_TYPE"))) {	%>
		<tr>
			<td><%= Bean.card_taskXML.getfieldTransl("CD_CARD_OPERATION_TYPE", false) %></td> <td><input type="text" name="name_card_operation_type" size="40" value="<%= operation.getValue("NAME_CARD_OPERATION_TYPE") %>" readonly="readonly" class="inputfield-ro"></td>
		    <td><%=Bean.card_taskXML.getfieldTransl("begin_period_date",true)%></td><td><%=Bean.getCalendarInputField("begin_action_date", operation.getValue("BEGIN_ACTION_DATE_FRMT"), "10") %></td>
		</tr>
		<tr>
			<%
				String app_card_resp_action = Bean.getMeaningForNumValue("APP_CARD_RESP_ACTION",operation.getValue("APP_CARD_RESP_ACTION"));
			%>
			<td><%=Bean.card_taskXML.getfieldTransl("app_card_resp_action",false)%></td> <td><input type="text" name="app_card_resp_action" size="40" value="<%=operation.getValue("APP_CARD_RESP_ACTION") + " - " + app_card_resp_action%>" readonly="readonly" class="inputfield-ro" title = "<%=operation.getValue("APP_CARD_RESP_ACTION") + " - " + app_card_resp_action%>"> </td>
		    <td><%=Bean.card_taskXML.getfieldTransl("end_period_date",true)%></td><td><%=Bean.getCalendarInputField("end_action_date", operation.getValue("END_ACTION_DATE_FRMT"), "10") %>
			  <font color="green">&nbsp;(<%=operation.getValue("PERIOD_DURATION")%>&nbsp;<%=Bean.commonXML.getfieldTransl("h_days",false)%>)</font>
		    </td>
		</tr>
		<tr>
			<td><%=Bean.card_taskXML.getfieldTransl("PRIORITY", false)%></td> <td><input type="text" name="priority" size="20" value="<%=operation.getValue("PRIORITY")%>" readonly="readonly" class="inputfield-ro"> </td>
			<td><%=Bean.card_taskXML.getfieldTransl("need_apply", true)%></td> <td><select name="need_apply" class="inputfield"><%=Bean.getMeaningFromLookupNameOptions("YES_NO",operation.getValue("NEED_APPLY"),false)%></select></td>
		</tr>
		<% } else if ("ADD_GOODS_TO_PURSE".equalsIgnoreCase(operation.getValue("CD_CARD_OPERATION_TYPE")) ||
				"WRITE_OFF_GOODS_FROM_PURSE".equalsIgnoreCase(operation.getValue("CD_CARD_OPERATION_TYPE"))) {	%>
		<tr>
			<td><%= Bean.card_taskXML.getfieldTransl("CD_CARD_OPERATION_TYPE", false) %></td> <td><input type="text" name="name_card_operation_type" size="40" value="<%= operation.getValue("NAME_CARD_OPERATION_TYPE") %>" readonly="readonly" class="inputfield-ro"></td>
		    <td><%=Bean.card_taskXML.getfieldTransl("begin_action_date",true)%></td><td><%=Bean.getCalendarInputField("begin_action_date", operation.getValue("BEGIN_ACTION_DATE_FRMT"), "10") %></td>
		</tr>
		<tr>
			<td><%=Bean.card_taskXML.getfieldTransl("PRIORITY", false)%></td> <td><input type="text" name="priority" size="20" value="<%=operation.getValue("PRIORITY")%>" readonly="readonly" class="inputfield-ro"> </td>
			<td><%=Bean.card_taskXML.getfieldTransl("need_apply", true)%></td> <td><select name="need_apply" class="inputfield"><%=Bean.getMeaningFromLookupNameOptions("YES_NO",operation.getValue("NEED_APPLY"),false)%></select></td>
		</tr>
		<% } else { %>
		<tr>
			<td><%= Bean.card_taskXML.getfieldTransl("CD_CARD_OPERATION_TYPE", false) %></td> <td><input type="text" name="name_card_operation_type" size="40" value="<%= operation.getValue("NAME_CARD_OPERATION_TYPE") %>" readonly="readonly" class="inputfield-ro"></td>
		    <td><%=Bean.card_taskXML.getfieldTransl("begin_action_date",true)%></td><td><%=Bean.getCalendarInputField("begin_action_date", operation.getValue("BEGIN_ACTION_DATE_FRMT"), "10") %></td>
		</tr>
		<tr>
			<%
				String app_card_resp_action = Bean.getMeaningForNumValue("APP_CARD_RESP_ACTION",operation.getValue("APP_CARD_RESP_ACTION"));
			%>
			<td><%=Bean.card_taskXML.getfieldTransl("app_card_resp_action",false)%></td> <td><input type="text" name="app_card_resp_action" size="40" value="<%=operation.getValue("APP_CARD_RESP_ACTION") + " - " + app_card_resp_action%>" readonly="readonly" class="inputfield-ro" title = "<%=operation.getValue("APP_CARD_RESP_ACTION") + " - " + app_card_resp_action%>"> </td>
			<td><%=Bean.card_taskXML.getfieldTransl("need_apply", true)%></td> <td><select name="need_apply" class="inputfield"><%=Bean.getMeaningFromLookupNameOptions("YES_NO",operation.getValue("NEED_APPLY"),false)%></select></td>
		</tr>
		<tr>
			<td><%=Bean.card_taskXML.getfieldTransl("PRIORITY", false)%></td> <td><input type="text" name="priority" size="20" value="<%=operation.getValue("PRIORITY")%>" readonly="readonly" class="inputfield-ro"> </td>
			<td colspan="2">&nbsp;</td>
		</tr>
		<% } %>
		<tr>
			<td><%= Bean.card_taskXML.getfieldTransl("basis_for_operation", true) %></td><td  colspan="3"><textarea name="basis_for_operation" cols="120" rows="3" class="inputfield"><%= operation.getValue("BASIS_FOR_OPERATION") %></textarea></td>
		</tr>

		<%
			if ("ADD_GOODS_TO_PURSE".equalsIgnoreCase(operation.getValue("CD_CARD_OPERATION_TYPE"))) {
		%>
		<tr>
		    <td colspan="4" class="top_line"><b><%=Bean.card_taskXML.getfieldTransl("tag_param", false)%></b></td>
		</tr>
		<tr>
			<td><%= Bean.card_taskXML.getfieldTransl("card_purse", false) %>
				<%= Bean.getGoToClubCardPurseLink(purse.getValue("ID_CARD_PURSE")) %>
			</td> 
			<td>
				<input type="hidden" name="id_card_purse" value="<%= purse.getValue("ID_CARD_PURSE") %>">
				<input type="text" name="name_card_purse" size="40" value="<%= purse.getValue("NUMBER_CARD_PURSE") %> - <%= purse.getValue("NAME_CARD_PURSE_TYPE") %>" readonly="readonly" class="inputfield-ro">
			</td>
			<td colspan="2">&nbsp;</td> 
		</tr>
		<tr>
			<td><%=Bean.card_taskXML.getfieldTransl("h_value_card_purse_add", true)%></td> <td><input type="text" name="value_card_purse" size="20" value="<%=operation.getValue("VALUE_CARD_PURSE_FRMT")%>" class="inputfield"></td>
			<td colspan="2">&nbsp;</td> 
		</tr>

		<%
			} else if ("WRITE_OFF_GOODS_FROM_PURSE".equalsIgnoreCase(operation.getValue("CD_CARD_OPERATION_TYPE"))) {
				
				double cardPurseValue = 0;
				double operationPurseValue = 0;
				if (!(purse.getValue("VALUE_CARD_PURSE") == null || "".equalsIgnoreCase(purse.getValue("VALUE_CARD_PURSE")))) {
					cardPurseValue = Double.parseDouble(purse.getValue("VALUE_CARD_PURSE"))/100;
				}
				if (!(operation.getValue("VALUE_CARD_PURSE") == null || "".equalsIgnoreCase(operation.getValue("VALUE_CARD_PURSE")))) {
					operationPurseValue = Double.parseDouble(operation.getValue("VALUE_CARD_PURSE"))/100;
				}

			%>
			
		<tr>
		    <td colspan="4" class="top_line"><b><%=Bean.card_taskXML.getfieldTransl("tag_param", false)%></b>
			<%
			if (cardPurseValue < operationPurseValue) {
				%>
					<b><font color="red">&nbsp;(<%=Bean.card_taskXML.getfieldTransl("h_exception_in_purse_value",false)%>)</font></b>
				<%
					}
			%>
			</td>
		</tr>
		<tr>
			<td><%= Bean.card_taskXML.getfieldTransl("card_purse", false) %>
				<%= Bean.getGoToClubCardPurseLink(purse.getValue("ID_CARD_PURSE")) %>
			</td> 
			<td>
				<input type="hidden" name="id_card_purse" value="<%= purse.getValue("ID_CARD_PURSE") %>">
				<input type="text" name="name_card_purse" size="40" value="<%= purse.getValue("NUMBER_CARD_PURSE") %> - <%= purse.getValue("NAME_CARD_PURSE_TYPE") %>" readonly="readonly" class="inputfield-ro">
			</td>
			<td colspan="2">&nbsp;</td> 
		</tr>
		<tr>
			<td><%=Bean.card_taskXML.getfieldTransl("h_value_card_purse_write_off", true)%></td> <td><input type="text" name="value_card_purse" size="20" value="<%=operation.getValue("VALUE_CARD_PURSE_FRMT")%>" class="inputfield">
				(<%=Bean.card_taskXML.getfieldTransl("h_has_purse_value",false)%>&nbsp;
				<b><span style="color:green"><%="" + cardPurseValue%></span></b>)
            </td>
			<td colspan="2">&nbsp;</td> 
		</tr>

		<%
			} else if ("ADD_BON".equalsIgnoreCase(operation.getValue("CD_CARD_OPERATION_TYPE"))) {
		%>
		<tr>
		    <td colspan="4" class="top_line"><b><%=Bean.card_taskXML.getfieldTransl("tag_param", false)%></b></td>
		</tr>
		<tr>
			<td><%=Bean.card_taskXML.getfieldTransl("h_bal_acc_add", false)%></td> <td><input type="text" name="bal_acc" size="20" value="<%=operation.getValue("BAL_ACC_FRMT")%>" class="inputfield"></td>
			<td><%=Bean.card_taskXML.getfieldTransl("date_acc", false)%></td><td><%=Bean.getCalendarInputField("date_acc", operation.getValue("DATE_ACC_FRMT"), "10") %></td>
		</tr>
		<tr>
			<td><%=Bean.card_taskXML.getfieldTransl("h_bal_cur_add", false)%></td> 
              <td><input type="text" name="bal_cur" size="20" value="<%=operation.getValue("BAL_CUR_FRMT")%>" class="inputfield"></td>
			<td colspan="2">&nbsp;</td> 
		</tr>
		<tr>
			<td valign="top"><%=Bean.card_taskXML.getfieldTransl("text_message",false)%></td><td  colspan="3"><textarea name="text_message" id="text_message" cols="120" rows="2" class="inputfield" <%=Bean.getMessageLengthTextAreaEvent("length_message")%>><%=operation.getValue("TEXT_MESSAGE")%></textarea></td>
		</tr>
		<tr>
			<td valign="top"><%=Bean.messageXML.getfieldTransl("length_message",false)%></td><td valign="top"><input type="text" name="length_message" id="length_message" size="8" value="" readonly="readonly" class="inputfield-ro"></td>
		</tr>

		<%
			} else if ("WRITE_OFF_BON".equalsIgnoreCase(operation.getValue("CD_CARD_OPERATION_TYPE"))) {
		%>
		<tr>
		    <td colspan="4" class="top_line"><b><%=Bean.card_taskXML.getfieldTransl("tag_param", false)%></b>
			<%
				double cardBalAcc = 0;
				double cardBalCur = 0;
				double cardBalFull = 0;
				double operationBalFull = 0;

				if (!(card.getValue("BAL_ACC") == null || "".equalsIgnoreCase(card.getValue("BAL_ACC")))) {
					cardBalAcc = Double.parseDouble(card.getValue("BAL_ACC"))/100;
				}
				if (!(card.getValue("BAL_CUR") == null || "".equalsIgnoreCase(card.getValue("BAL_CUR")))) {
					cardBalCur = Double.parseDouble(card.getValue("BAL_CUR"))/100;
				}
				cardBalFull = cardBalAcc + cardBalFull;
				if (!(operation.getValue("BAL_FULL") == null || "".equalsIgnoreCase(operation.getValue("BAL_FULL")))) {
					operationBalFull = Double.parseDouble(operation.getValue("BAL_FULL"))/100;
				}

				if (cardBalFull < operationBalFull) {
			%>
				<b><font color="red">&nbsp;(<%=Bean.card_taskXML.getfieldTransl("h_exception_in_bon",false)%>)</font></b>
			<%
				}
			%>
			</td>
		</tr>
		<tr>
			<td><%=Bean.card_taskXML.getfieldTransl("h_bal_delete", true)%></td> 
            <td  colspan="3"><input type="text" name="bal_full" size="20" value="<%=operation.getValue("BAL_FULL_FRMT")%>" class="inputfield">
				(<%=Bean.card_taskXML.getfieldTransl("h_has_bon",false)%>&nbsp;
				<b>
					<span style="color:green" title="<%= Bean.getClubCardXMLFieldTransl("BAL_ACC", false) %>"><%="" + cardBalAcc%></span>&nbsp;+
					<span style="color:green" title="<%= Bean.getClubCardXMLFieldTransl("BAL_CUR", false) %>"><%="" + cardBalCur%></span>&nbsp;=
					<span style="color:green" title="<%= Bean.getClubCardXMLFieldTransl("BAL_FULL", false) %>"><%="" + cardBalFull%></span>
				</b>)
            </td>
		</tr>
		<tr>
			<td valign="top"><%=Bean.card_taskXML.getfieldTransl("text_message",false)%></td><td  colspan="3"><textarea name="text_message" cols="120" rows="2" class="inputfield"><%=operation.getValue("TEXT_MESSAGE")%></textarea></td>
		</tr>
		<%
			} else if ("CHANGE_PARAM".equalsIgnoreCase(operation.getValue("CD_CARD_OPERATION_TYPE"))) {
		%>
		<tr>
		    <td colspan="4" class="top_line"><b><%=Bean.card_taskXML.getfieldTransl("tag_param", false)%></b></td>
		</tr>
		<tr>
			<td><%=Bean.card_taskXML.getfieldTransl("id_card_status",true)%></td><td><select name="id_card_status" id="id_card_status" class="inputfield" onchange="dwr_get_card_bon_disc_category3('',this,'','','<%=Bean.getSessionId()%>');"><%=Bean.getClubCardStatusOptions(operation.getValue("ID_CARD_STATUS"),false)%> </select></td>
		    <td><%=Bean.getClubCardXMLFieldTransl("date_acc", false)%></td><td><%=Bean.getCalendarInputField("date_acc", operation.getValue("DATE_ACC_FRMT"), "10") %></td>
		</tr>
		<tr>
			<td><%=Bean.card_taskXML.getfieldTransl("id_bon_category",false)%></td> 
			<td class="top_line_gray">
				<select name="id_bon_category"  id="id_bon_category" class="inputfield"><%=Bean.getClubCardCategoryForStatusOptions(operation.getValue("ID_CARD_STATUS"),operation.getValue("ID_BON_CATEGORY"),"BON",true) %></select>
			</td>
		    <td><%=Bean.getClubCardXMLFieldTransl("date_mov", false)%></td><td><%=Bean.getCalendarInputField("date_mov", operation.getValue("DATE_MOV_FRMT"), "10") %></td>
		</tr>
		<tr>
			<td>&nbsp;</td>
			<td>
				<%
					String bonReductionChecked = "";
					String bonReductionLabel = Bean.card_taskXML.getfieldTransl("can_bon_category_reduction",false);
					if ("Y".equalsIgnoreCase(operation.getValue("CAN_BON_CATEGORY_REDUCTION"))) {
						bonReductionChecked = " CHECKED ";
						bonReductionLabel = "<b><font color=\"red\">" + Bean.card_taskXML.getfieldTransl("can_bon_category_reduction",false) + "</font></b>";
					}
				%>
				<input type="checkbox" name="can_bon_category_reduction" id="can_bon_category_reduction" value="Y" <%=bonReductionChecked %>>
				<label class="checbox_label" for="can_bon_category_reduction"><%=bonReductionLabel %></label>
			</td>
		    <td><%=Bean.getClubCardXMLFieldTransl("date_onl_next", false)%></td><td><%=Bean.getCalendarInputField("date_onl", operation.getValue("DATE_ONL_FRMT"), "10") %></td>
		</tr>
		<tr>
			<td><%=Bean.card_taskXML.getfieldTransl("id_disc_category",false)%></td> 
			<td class="top_line_gray">
				<select name="id_disc_category"  id="id_disc_category" class="inputfield"><%=Bean.getClubCardCategoryForStatusOptions(operation.getValue("ID_CARD_STATUS"),operation.getValue("ID_DISC_CATEGORY"),"DISC",true) %></select>
			</td>
			<td class="top_line_gray"><%=Bean.getClubCardXMLFieldTransl("bal_acc", false)%></td> <td class="top_line_gray"><input type="text" name="bal_acc" size="20" value="<%=operation.getValue("BAL_ACC_FRMT")%>" class="inputfield" title="BAL_ACC"> </td>
		</tr>
		<tr>
			<td>&nbsp;</td>
			<td>
				<%
					String discReductionChecked = "";
					String discReductionLabel = Bean.card_taskXML.getfieldTransl("can_disc_category_reduction",false);
					if ("Y".equalsIgnoreCase(operation.getValue("CAN_DISC_CATEGORY_REDUCTION"))) {
						discReductionChecked = " CHECKED ";
						discReductionLabel = "<b><font color=\"red\">" + Bean.card_taskXML.getfieldTransl("can_disc_category_reduction",false) + "</font></b>";
					}
				%>
				<input type="checkbox" name="can_disc_category_reduction" id="can_disc_category_reduction" value="Y" <%=discReductionChecked %>>
				<label class="checbox_label" for="can_disc_category_reduction"><%=discReductionLabel %></label>
			</td>
			<td><%=Bean.getClubCardXMLFieldTransl("bal_cur", false)%></td> <td><input type="text" name="bal_cur" size="20" value="<%=operation.getValue("BAL_CUR_FRMT")%>" class="inputfield" title="BAL_CUR"> </td>
		</tr>
		<tr>
			<td colspan="2">&nbsp;</td>
			<td><%=Bean.getClubCardXMLFieldTransl("bal_bon_per",false)%></td> <td><input type="text" name="bal_bon_per" size="20" value="<%=operation.getValue("BAL_BON_PER_FRMT")%>" class="inputfield" title="BAL_BON_PER"> </td>
		</tr>
		<tr>
			<td colspan="2">&nbsp;</td>
			<td valign="top"><%=Bean.getClubCardXMLFieldTransl("bal_disc_per",false)%></td> <td valign="top"><input type="text" name="bal_disc_per" size="20" value="<%=operation.getValue("BAL_DISC_PER_FRMT")%>" class="inputfield" title="BAL_DISC_PER"> </td>
		</tr>
		<tr>
			<td valign="top"><%=Bean.card_taskXML.getfieldTransl("text_message", false)%></td><td  colspan="3"><textarea name="text_message" id="text_message" cols="120" rows="2" class="inputfield" <%=Bean.getMessageLengthTextAreaEvent("length_message")%>><%=operation.getValue("TEXT_MESSAGE")%></textarea></td>
		</tr>
		<tr>
			<td valign="top"><%=Bean.messageXML.getfieldTransl("length_message", false)%></td><td valign="top"><input type="text" name="length_message" id="length_message" size="8" value="" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<%
			} else if ("SET_CATEGORIES_ON_PERIOD".equalsIgnoreCase(operation.getValue("CD_CARD_OPERATION_TYPE"))) {
		%>
		<tr>
		    <td colspan="4" class="top_line"><b><%=Bean.card_taskXML.getfieldTransl("tag_param", false)%></b></td>
		</tr>
		<tr>
			<td><%=Bean.card_taskXML.getfieldTransl("id_card_status",true)%></td><td><select name="id_card_status" id="id_card_status" class="inputfield" onchange="dwr_get_card_bon_disc_category3('',this,'','','<%=Bean.getSessionId()%>');"><%=Bean.getClubCardStatusOptions(operation.getValue("ID_CARD_STATUS"),false)%> </select></td>
		    <td><%=Bean.getClubCardXMLFieldTransl("date_onl_next", false)%></td><td><%=Bean.getCalendarInputField("date_onl", operation.getValue("DATE_ONL_FRMT"), "10") %></td>
		</tr>
		<tr>
			<td><%=Bean.card_taskXML.getfieldTransl("id_bon_category",false)%></td> 
			<td class="top_line_gray">
				<select name="id_bon_category"  id="id_bon_category" class="inputfield"><%=Bean.getClubCardCategoryForStatusOptions(operation.getValue("ID_CARD_STATUS"),operation.getValue("ID_BON_CATEGORY"),"BON",true) %></select>
			</td>
			<td colspan="2">&nbsp;</td>
		</tr>
		<tr>
			<td>&nbsp;</td>
			<td>
				<%
					String bonReductionChecked = "";
					String bonReductionLabel = Bean.card_taskXML.getfieldTransl("can_bon_category_reduction",false);
					if ("Y".equalsIgnoreCase(operation.getValue("CAN_BON_CATEGORY_REDUCTION"))) {
						bonReductionChecked = " CHECKED ";
						bonReductionLabel = "<b><font color=\"red\">" + Bean.card_taskXML.getfieldTransl("can_bon_category_reduction",false) + "</font></b>";
					}
				%>
				<input type="checkbox" name="can_bon_category_reduction" id="can_bon_category_reduction" value="Y" <%=bonReductionChecked %>>
				<label class="checbox_label" for="can_bon_category_reduction"><%=bonReductionLabel %></label>
			</td>
			<td colspan="2">&nbsp;</td>
		</tr>
		<tr>
			<td><%=Bean.card_taskXML.getfieldTransl("id_disc_category",false)%></td> 
			<td class="top_line_gray">
				<select name="id_disc_category"  id="id_disc_category" class="inputfield"><%=Bean.getClubCardCategoryForStatusOptions(operation.getValue("ID_CARD_STATUS"),operation.getValue("ID_DISC_CATEGORY"),"DISC",true) %></select>
			</td>
			<td colspan="2">&nbsp;</td>
		</tr>
		<tr>
			<td>&nbsp;</td>
			<td>
				<%
					String discReductionChecked = "";
					String discReductionLabel = Bean.card_taskXML.getfieldTransl("can_disc_category_reduction",false);
					if ("Y".equalsIgnoreCase(operation.getValue("CAN_DISC_CATEGORY_REDUCTION"))) {
						discReductionChecked = " CHECKED ";
						discReductionLabel = "<b><font color=\"red\">" + Bean.card_taskXML.getfieldTransl("can_disc_category_reduction",false) + "</font></b>";
					}
				%>
				<input type="checkbox" name="can_disc_category_reduction" id="can_disc_category_reduction" value="Y" <%=discReductionChecked %>>
				<label class="checbox_label" for="can_disc_category_reduction"><%=discReductionLabel %></label>
			</td>
			<td colspan="2">&nbsp;</td>
		</tr>
		<tr>
			<td valign="top"><%=Bean.card_taskXML.getfieldTransl("text_message", false)%></td><td  colspan="3"><textarea name="text_message" id="text_message" cols="120" rows="2" class="inputfield" <%=Bean.getMessageLengthTextAreaEvent("length_message")%>><%=operation.getValue("TEXT_MESSAGE")%></textarea></td>
		</tr>
		<tr>
			<td valign="top"><%=Bean.messageXML.getfieldTransl("length_message", false)%></td><td valign="top"><input type="text" name="length_message" id="length_message" size="8" value="" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<% } else if ("BLOCK_CARD".equalsIgnoreCase(operation.getValue("CD_CARD_OPERATION_TYPE"))) { %>
		<tr>
		    <td colspan="4" class="top_line"><b><%= Bean.card_taskXML.getfieldTransl("tag_param", false) %></b></td>
		</tr>
		<tr>
			<td valign="top"><%= Bean.card_taskXML.getfieldTransl("text_message", false) %></td><td  colspan="3"><textarea name="text_message" id="text_message" cols="120" rows="2" class="inputfield" <%=Bean.getMessageLengthTextAreaEvent("length_message") %>><%= operation.getValue("TEXT_MESSAGE") %></textarea></td>
		</tr>
		<tr>
			<td valign="top"><%= Bean.messageXML.getfieldTransl("length_message", false) %></td><td valign="top"><input type="text" name="length_message" id="length_message" size="8" value="" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<% } else if ("SEND_MESSAGE".equalsIgnoreCase(operation.getValue("CD_CARD_OPERATION_TYPE"))) { %>
		<tr>
		    <td colspan="4" class="top_line"><b><%= Bean.card_taskXML.getfieldTransl("tag_param", false) %></b></td>
		</tr>
		<tr>
			<td valign="top"><%= Bean.card_taskXML.getfieldTransl("text_message", true) %></td><td  colspan="3"><textarea name="text_message" id="text_message" cols="120" rows="2" class="inputfield" <%=Bean.getMessageLengthTextAreaEvent("length_message") %>><%= operation.getValue("TEXT_MESSAGE") %></textarea></td>
		</tr>
		<tr>
			<td valign="top"><%= Bean.messageXML.getfieldTransl("length_message", false) %></td><td valign="top"><input type="text" name="length_message" id="length_message" size="8" value="" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<% } %>
		<tr>
		    <td colspan="4" class="top_line"><b><%= Bean.card_taskXML.getfieldTransl("tag_source_param", false) %></b></td>
		</tr>
		<tr>
			<td><%=Bean.card_taskXML.getfieldTransl("id_sequrity_monitor", false)%></td> <td><input type="text" name="id_sequrity_monitor" size="20" value="<%=operation.getValue("ID_SEQURITY_MONITOR")%>" readonly="readonly" class="inputfield-ro"> </td>
			<td><%=Bean.card_taskXML.getfieldTransl("id_previous_operation", false)%>
				<%=Bean.getGoToCardTaskLink(operation.getValue("ID_PREVIOUS_OPERATION")) %>
			</td> <td><input type="text" name="id_previous_operation" size="20" value="<%=operation.getValue("ID_PREVIOUS_OPERATION")%>" readonly="readonly" class="inputfield-ro"> </td>
		</tr>
		<tr>
			<td><%=Bean.card_taskXML.getfieldTransl("id_remittance", false)%></td> <td><input type="text" name="id_remittance" size="20" value="<%=operation.getValue("ID_REMITTANCE")%>" readonly="readonly" class="inputfield-ro"> </td>
			<td><%=Bean.card_taskXML.getfieldTransl("id_club_event", false)%>
				<%=Bean.getGoToClubEventLink(operation.getValue("ID_CLUB_EVENT"))
				%>
			  </td> <td><input type="text" name="id_club_event" size="20" value="<%=operation.getValue("ID_CLUB_EVENT")%>" readonly="readonly" class="inputfield-ro"> </td>
		</tr>
		<tr>
			<td><%=Bean.card_taskXML.getfieldTransl("id_quest_int", false)%>
				<%=Bean.getGoToQuestionnaireLink(operation.getValue("ID_QUEST_INT"))%>
			  </td> <td><input type="text" name="id_quest_int" size="20" value="<%=operation.getValue("ID_QUEST_INT")%>" readonly="readonly" class="inputfield-ro"> </td>
			<td colspan="2">&nbsp;</td>
		</tr>
		<%=	Bean.getCreationAndMoficationRecordFields(
				operation.getValue(Bean.getCreationDateFieldName()),
				operation.getValue("CREATED_BY"),
				operation.getValue(Bean.getLastUpdateDateFieldName()),
				operation.getValue("LAST_UPDATE_BY")
			) %>
 		<tr>
			<td colspan="4" align="center">
				<%=Bean.getSubmitButtonAjax("../crm/club_event/deliveryupdate.jsp") %>
				<%=Bean.getResetButton() %>
				<%=Bean.getGoBackButton("../crm/club_event/deliveryspecs.jsp?id=" + id) %>
			</td>
		</tr>
	</table>
	</form>
	<!-- Скрипт для втавки меню вибору дати -->
		<%= Bean.getCalendarScript("begin_action_date", false) %>

		<% if ("SET_CATEGORIES_ON_PERIOD".equalsIgnoreCase(operation.getValue("CD_CARD_OPERATION_TYPE"))) {	%>
			<%= Bean.getCalendarScript("end_action_date", false) %>
		<% } %>

    	<% if (("ADD_BON".equalsIgnoreCase(operation.getValue("CD_CARD_OPERATION_TYPE")) ||
 			   "CHANGE_PARAM".equalsIgnoreCase(operation.getValue("CD_CARD_OPERATION_TYPE")))) { %>
			<%= Bean.getCalendarScript("date_acc", false) %>
    	<% } %>

    	<% if (("CHANGE_PARAM".equalsIgnoreCase(operation.getValue("CD_CARD_OPERATION_TYPE")))) { %>
 			<%= Bean.getCalendarScript("date_mov", false) %>
     	<% } %>
		
    	<%	if (("SET_CATEGORIES_ON_PERIOD".equalsIgnoreCase(operation.getValue("CD_CARD_OPERATION_TYPE")) || 
    				"CHANGE_PARAM".equalsIgnoreCase(operation.getValue("CD_CARD_OPERATION_TYPE")))) { %>
			<%= Bean.getCalendarScript("date_onl", false) %>
    	<% } %>

	   	<%} else {
	   	    %> <%= Bean.getUnknownActionText(action) %><%
	   	}
	} else if (process.equalsIgnoreCase("yes")) {
		String
			id_task 					= Bean.getDecodeParam(parameters.get("id_task")),
	    	cd_card_operation_type 		= Bean.getDecodeParam(parameters.get("cd_card_operation_type")),
	    	begin_action_date 			= Bean.getDecodeParam(parameters.get("begin_action_date")),
	    	end_action_date 			= Bean.getDecodeParam(parameters.get("end_action_date")),
	    	need_apply 					= Bean.getDecodeParam(parameters.get("need_apply")),
	    	basis_for_operation 		= Bean.getDecodeParam(parameters.get("basis_for_operation")),
	    	bal_acc 					= Bean.getDecodeParam(parameters.get("bal_acc")),
	    	bal_cur 					= Bean.getDecodeParam(parameters.get("bal_cur")),
	    	bal_bon_per					= Bean.getDecodeParam(parameters.get("bal_bon_per")),
	    	bal_disc_per				= Bean.getDecodeParam(parameters.get("bal_disc_per")),
	    	bal_full					= Bean.getDecodeParam(parameters.get("bal_full")),
	    	date_acc 					= Bean.getDecodeParam(parameters.get("date_acc")),
	    	date_mov 					= Bean.getDecodeParam(parameters.get("date_mov")),
	    	date_onl 					= Bean.getDecodeParam(parameters.get("date_onl")),
	    	id_card_status 				= Bean.getDecodeParam(parameters.get("id_card_status")),
	    	id_bon_category		 		= Bean.getDecodeParam(parameters.get("id_bon_category")),
	    	id_disc_category 			= Bean.getDecodeParam(parameters.get("id_disc_category")),
	    	text_message 				= Bean.getDecodeParam(parameters.get("text_message")),
	    	cd_card_oper_state			= Bean.getDecodeParam(parameters.get("cd_card_oper_state")),
	    	id_card			 			= Bean.getDecodeParam(parameters.get("id_card")),
	    	can_bon_category_reduction	= Bean.getDecodeParam(parameters.get("can_bon_category_reduction")),
	    	can_disc_category_reduction	= Bean.getDecodeParam(parameters.get("can_disc_category_reduction")),
	    	id_card_purse				= Bean.getDecodeParam(parameters.get("id_card_purse")),
	    	value_card_purse			= Bean.getDecodeParam(parameters.get("value_card_purse"));
	
		if (action.equalsIgnoreCase("add")) { 
		
			String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_CLUB_ACTION.add_card_operation(" +
				"?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";

			String[] pParam = new String [24];
					
			pParam[0] = id;
			pParam[1] = cd_card_operation_type;
			pParam[2] = begin_action_date;
			pParam[3] = end_action_date;
			pParam[4] = need_apply;
			pParam[5] = basis_for_operation;
			pParam[6] = id_card;
			pParam[7] = bal_acc;
			pParam[8] = bal_cur;
			pParam[9] = bal_bon_per;
			pParam[10] = bal_disc_per;
			pParam[11] = bal_full;
			pParam[12] = date_acc;
			pParam[13] = date_mov;
			pParam[14] = date_onl;
			pParam[15] = id_card_status;
			pParam[16] = id_bon_category;
			pParam[17] = can_bon_category_reduction;
			pParam[18] = id_disc_category;
			pParam[19] = can_disc_category_reduction;
			pParam[20] = id_card_purse;
			pParam[21] = value_card_purse;
			pParam[22] = text_message;
			pParam[23] = Bean.getDateFormat();
			
		 	%>
			<%= Bean.getCallResultParam("INSERT", callSQL, pParam, "../crm/club_event/deliveryspecs.jsp?id=" + id + "&id_task=", "") %>
			<% 	
	
		} else if (action.equalsIgnoreCase("remove_task")) { 
			
			String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_CLUB_ACTION.delete_card_operation(?,?)}";

			String[] pParam = new String [1];
					
			pParam[0] = id_task;
	
		 	%>
			<%= Bean.getCallResultParam("DELETE", callSQL, pParam, "../crm/club_event/deliveryspecs.jsp?id=" + id , "") %>
			<% 	
	
		} else if (action.equalsIgnoreCase("edit")) { 
			
			String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_CLUB_ACTION.update_card_operation(" +
				"?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";

			String[] pParam = new String [24];
						
			pParam[0] = id_task;
			pParam[1] = cd_card_operation_type;
			pParam[2] = begin_action_date;
			pParam[3] = end_action_date;
			pParam[4] = need_apply;
			pParam[5] = basis_for_operation;
			pParam[6] = id_card;
			pParam[7] = bal_acc;
			pParam[8] = bal_cur;
			pParam[9] = bal_bon_per;
			pParam[10] = bal_disc_per;
			pParam[11] = bal_full;
			pParam[12] = date_acc;
			pParam[13] = date_mov;
			pParam[14] = date_onl;
			pParam[15] = id_card_status;
			pParam[16] = id_bon_category;
			pParam[17] = can_bon_category_reduction;
			pParam[18] = id_disc_category;
			pParam[19] = can_disc_category_reduction;
			pParam[20] = id_card_purse;
			pParam[21] = value_card_purse;
			pParam[22] = text_message;
			pParam[23] = Bean.getDateFormat();
				
		 	%>
			<%= Bean.getCallResultParam("UPDATE", callSQL, pParam, "../crm/club_event/deliveryspecs.jsp?id=" + id, "") %>
			<% 	
			
	    } else { %> 
			<%= Bean.getUnknownActionText(action) %><% 
		}
	}
} else {%> 
	<%= Bean.getUnknownTypeText(type) %> <%
}

%>


</body>
</html>
