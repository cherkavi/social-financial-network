<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%= Bean.getLogOutScript(request) %>

<%@page import="bc.objects.bcCallCenterCallGroupObject"%>
<%@page import="java.util.HashMap"%>
<%@page import="bc.objects.bcClubShortObject"%>

<%@page import="bc.objects.bcCallCenterQuestionObject"%>
<%@page import="bc.objects.bcNatPrsObject"%>
<%@page import="bc.objects.bcClubCardObject"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Set"%>
<%@page import="java.util.Iterator"%>
<%@page import="bc.objects.bcClubCardPurseObject"%><html>
<head>
	<%= Bean.getMetaContent() %>
	<%= Bean.getBottomFrameCSS() %>

</head>

<body>
<%
HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String pageFormName = "CALL_CENTER_CALL_GROUP";

Bean.setJspPageForTabName(pageFormName);

request.setCharacterEncoding("UTF-8");

String type		= Bean.getDecodeParam(parameters.get("type"));
String id		= Bean.getDecodeParam(parameters.get("id"));
String action	= Bean.getDecodeParam(parameters.get("action")); 
String process	= Bean.getDecodeParam(parameters.get("process"));

if (id==null || ("".equalsIgnoreCase(id))) id="empty";
if (action==null || ("".equalsIgnoreCase(action))) action="empty";
if (process==null || ("".equalsIgnoreCase(process))) process="empty";
if (type==null || ("".equalsIgnoreCase(type))) type="empty";

if (type.equalsIgnoreCase("general")) {
	if (process.equalsIgnoreCase("no"))
	/* вибираємо тип дії (добавити, видалити...)*/
	{
	   /*  --- Добавити запис --- */
    	if (action.equalsIgnoreCase("add")|| action.equalsIgnoreCase("add2")) {
    	
    		bcClubShortObject club = new bcClubShortObject(Bean.getCurrentClubID());
    	%>
	<script>
		var formDataCallGroup = new Array (
			new Array ('name_cc_call_group', 'varchar2', 1),
			new Array ('cd_cc_call_group_state', 'varchar2', 1),
			new Array ('date_cc_call_group', 'varchar2', 1),
			new Array ('name_cc_inquirer', 'varchar2', 1),
			new Array ('cd_cc_question_type', 'varchar2', 1)
		);
		function myValidateForm() {
			return validateForm(formDataCallGroup);
		}
	</script> 
		<%= Bean.getOperationTitle(
				Bean.call_centerXML.getfieldTransl("h_add_call_group", false),
				"Y",
				"Y") 
		%>
    <form action="../crm/call_center/call_groupupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return myValidateForm()">
	        <input type="hidden" name="type" value="general">
	        <input type="hidden" name="action" value="add">
	        <input type="hidden" name="process" value="yes">
	<table <%=Bean.getTableDetailParam() %>>
		<tr>
			<td><%= Bean.call_centerXML.getfieldTransl("name_cc_call_group", true) %></td><td><input type="text" name="name_cc_call_group" size="53" value="" class="inputfield"></td>
			<td><%= Bean.clubXML.getfieldTransl("club", true) %>
				<%= Bean.getGoToClubLink(club.getValue("ID_CLUB")) %>
		  	</td>
		  	<td>
				<%=Bean.getWindowFindClub("club", club.getValue("ID_CLUB"), club.getValue("SNAME_CLUB"), "25") %>
		  	</td>
		</tr>
		<tr>
			<td><%= Bean.call_centerXML.getfieldTransl("cd_cc_call_group_state", true) %></td><td><select name="cd_cc_call_group_state" class="inputfield"><%= Bean.getCallCenterCallGroupStateOptions("NEW", false) %></select></td>
			<td colspan="2"><b>Критерии отбора:</b></td>
		</tr>
		<tr>
		  	<td><%= Bean.call_centerXML.getfieldTransl("date_cc_call_group", true) %></td><td><%=Bean.getCalendarInputField("date_cc_call_group", "", "10") %></td>
		  	<td><%= Bean.call_centerXML.getfieldTransl("cg_crit_begin_purchase_date", false) %></td><td><%=Bean.getCalendarInputField("crit_begin_purchase_date", "", "10") %><!-- &nbsp;<%= Bean.call_centerXML.getfieldTransl("cg_crit_end_purchase_date", false) %>&nbsp;<%=Bean.getCalendarInputField("crit_end_purchase_date", "", "10") %> --></td>
		</tr>			
		<tr>
			<td><%= Bean.call_centerXML.getfieldTransl("id_cc_inquirer", true) %></td>
			<td>
				<%=Bean.getWindowFindCallCenterInquirer("cc_inquirer", "", "", "40") %>
			</td>
		    <td><%= Bean.call_centerXML.getfieldTransl("cg_crit_give_for_event", false) %></td>
			<td>
				<%=Bean.getWindowFindClubAction("club_event", "", Bean.getClubActionName(""), "30") %>
		  	</td>	
		</tr>			
		<tr>
			<td><%= Bean.call_centerXML.getfieldTransl("cc_question_type", true) %></td><td><select name="cd_cc_question_type" class="inputfield"><%= Bean.getCallCenterQuestionTypeOptions("", false) %></select></td>
		  	<td><%= Bean.call_centerXML.getfieldTransl("cg_crit_given_from_date", false) %></td><td><%=Bean.getCalendarInputField("crit_given_from_date", "", "10") %>&nbsp;<%= Bean.call_centerXML.getfieldTransl("cg_crit_given_to_date", false) %>&nbsp;<%=Bean.getCalendarInputField("crit_given_to_date", "", "10") %></td>
		<tr>
			<td rowspan="3"><%= Bean.call_centerXML.getfieldTransl("desc_cc_call_group", false) %></td><td rowspan="3"><textarea name="desc_cc_call_group" cols="50" rows="3" class="inputfield"></textarea></td>
		  	<td><%= Bean.call_centerXML.getfieldTransl("cg_crit_made_purchases", false) %></td>
				<td><select id="crit_made_purchase_condition" name="crit_made_purchase_condition" class="inputfield">
						<option value="LESS"><%= Bean.call_centerXML.getfieldTransl("cg_crit_made_purchase_less", false) %></option>
						<option value="EQUAL"><%= Bean.call_centerXML.getfieldTransl("cg_crit_made_purchase_equal", false) %></option>
						<option value="MORE"><%= Bean.call_centerXML.getfieldTransl("cg_crit_made_purchase_more", false) %></option>
				    </select>&nbsp;<input type="text" name="crit_made_purchase_value" size="5" value="<%= "" %>" class="inputfield">
				</td>
		</tr>			
		<tr>
		  	<td>&nbsp;&nbsp;&nbsp;&nbsp;<%= Bean.call_centerXML.getfieldTransl("cg_crit_made_purchase_from_date", false) %></td><td><%=Bean.getCalendarInputField("crit_made_purchase_from_date", "", "10") %>&nbsp;<%= Bean.call_centerXML.getfieldTransl("cg_crit_made_purchase_to_date", false) %>&nbsp;<%=Bean.getCalendarInputField("crit_made_purchase_to_date", "", "10") %></td>
		</tr>		
		<tr>
			<td>&nbsp;&nbsp;&nbsp;&nbsp;<%= Bean.call_centerXML.getfieldTransl("crit_made_purchase_dealer", false) %>
			<td>
				<%=Bean.getWindowFindJurPrs("crit_made_purchase_dealer", "", "ALL", "30") %>
			 </td>
 		</tr>
		<tr>
			<td colspan="4" align="center">
				<%=Bean.getSubmitButtonAjax("../crm/call_center/call_groupupdate.jsp") %>
				<%=Bean.getResetButton() %>
				<% if (action.equalsIgnoreCase("add")) { %>
					<%=Bean.getGoBackButton("../crm/call_center/call_group.jsp") %>
				<% } else { %>
					<%=Bean.getGoBackButton("../crm/call_center/call_groupspecs.jsp?id=" + id) %>
				<% } %>
			</td>
		</tr>

	</table>
	

	</form>
	<%= Bean.getCalendarScript("date_cc_call_group", false) %>
	<%= Bean.getCalendarScript("crit_begin_purchase_date", false) %>
	<%= Bean.getCalendarScript("crit_given_from_date", false) %>
	<%= Bean.getCalendarScript("crit_given_to_date", false) %>
	<%= Bean.getCalendarScript("crit_made_purchase_from_date", false) %>
	<%= Bean.getCalendarScript("crit_made_purchase_to_date", false) %>

	        <%
    	} else {
    	    %> <%= Bean.getUnknownActionText(action) %><%
    	}
	}

	else if (process.equalsIgnoreCase("yes")){
    	String
    		name_cc_call_group 				= Bean.getDecodeParam(parameters.get("name_cc_call_group")),
    		desc_cc_call_group 				= Bean.getDecodeParam(parameters.get("desc_cc_call_group")),
    		cd_cc_call_group_state 			= Bean.getDecodeParam(parameters.get("cd_cc_call_group_state")),
    		date_cc_call_group 				= Bean.getDecodeParam(parameters.get("date_cc_call_group")),
    		id_cc_inquirer 					= Bean.getDecodeParam(parameters.get("id_cc_inquirer")),
    		cd_cc_question_type 			= Bean.getDecodeParam(parameters.get("cd_cc_question_type")),
    		inquirer_line_answer_style		= Bean.getDecodeParam(parameters.get("inquirer_line_answer_style")),
    		crit_begin_purchase_date 		= Bean.getDecodeParam(parameters.get("crit_begin_purchase_date")),
    		crit_end_purchase_date		 	= Bean.getDecodeParam(parameters.get("crit_end_purchase_date")),
    		crit_give_for_event		 		= Bean.getDecodeParam(parameters.get("id_club_event")),
    		crit_given_from_date		 	= Bean.getDecodeParam(parameters.get("crit_given_from_date")),
    		crit_given_to_date		 		= Bean.getDecodeParam(parameters.get("crit_given_to_date")),
    		crit_made_purchase_condition	= Bean.getDecodeParam(parameters.get("crit_made_purchase_condition")),
    		crit_made_purchase_value		= Bean.getDecodeParam(parameters.get("crit_made_purchase_value")),
    		crit_made_purchase_from_date	= Bean.getDecodeParam(parameters.get("crit_made_purchase_from_date")),
    		crit_made_purchase_to_date		= Bean.getDecodeParam(parameters.get("crit_made_purchase_to_date")),
    		crit_made_purchase_dealer		= Bean.getDecodeParam(parameters.get("id_crit_made_purchase_dealer")),
    		id_club		 					= Bean.getDecodeParam(parameters.get("id_club")),
    		LUD			 					= Bean.getDecodeParam(parameters.get("LUD"));


		if (action.equalsIgnoreCase("add")) { 

			String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_CALL_CENTER.add_call_group("+
				"?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";

			ArrayList<String> pParam = new ArrayList<String>();
					
			pParam.add(name_cc_call_group);
			pParam.add(desc_cc_call_group);
			pParam.add(cd_cc_call_group_state);
			pParam.add(date_cc_call_group);
			pParam.add(id_cc_inquirer);
			pParam.add(cd_cc_question_type);
			pParam.add(inquirer_line_answer_style);
			pParam.add(crit_begin_purchase_date);
			pParam.add(crit_end_purchase_date);
			pParam.add(crit_give_for_event);
			pParam.add(crit_given_from_date);
			pParam.add(crit_given_to_date);
			pParam.add(crit_made_purchase_condition);
			pParam.add(crit_made_purchase_value);
			pParam.add(crit_made_purchase_from_date);
			pParam.add(crit_made_purchase_to_date);
			pParam.add(crit_made_purchase_dealer);
			pParam.add(id_club);
			pParam.add(Bean.getDateFormat());
		
			%>
			<%= Bean.getCallResultParam("INSERT", callSQL, pParam, "../crm/call_center/call_groupspecs.jsp?id=", "../crm/call_center/call_group.jsp") %>
			<% 	

		} else if (action.equalsIgnoreCase("remove")) { 

		   	String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_CALL_CENTER.delete_call_group(?,?)}";

		   	ArrayList<String> pParam = new ArrayList<String>();
						
			pParam.add(id);

			%>
			<%= Bean.getCallResultParam("DELETE", callSQL, pParam, "../crm/call_center/call_group.jsp", "") %>
			<%
		
		} else if (action.equalsIgnoreCase("edit")) { 
		
	 		String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_CALL_CENTER.update_call_group(" + 
	 			"?,?,?,?,?,?,?,?,?)}";

	 		ArrayList<String> pParam = new ArrayList<String>();
						
			pParam.add(id);
			pParam.add(name_cc_call_group);
			pParam.add(desc_cc_call_group);
			pParam.add(cd_cc_call_group_state);
			pParam.add(date_cc_call_group);
			pParam.add(inquirer_line_answer_style);
			pParam.add(Bean.getDateFormat());
			pParam.add(LUD);
		
			%>
			<%= Bean.getCallResultParam("UPDATE", callSQL, pParam, "../crm/call_center/call_groupspecs.jsp?id=" + id, "") %>
			<% 	
		} else if (action.equalsIgnoreCase("edit_answer_style")) { 
			
	 		String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_CALL_CENTER.update_call_group_answer_style(?,?)}";

	 		ArrayList<String> pParam = new ArrayList<String>();
							
			pParam.add(inquirer_line_answer_style);
		
			%>
			<%= Bean.getCallResultParam("UPDATE", callSQL, pParam, "../crm/call_center/call_groupspecs.jsp?id=" + id, "") %>
			<% 	
		} else { %> 
			<%= Bean.getUnknownActionText(action) %><% 
		}
	} else {%> 
		<%= Bean.getUnknownProcessText(process) %> <%
	}
} else if (type.equalsIgnoreCase("CLIENT")) {
	if (process.equalsIgnoreCase("no"))	{
	   	if (action.equalsIgnoreCase("edit")) {
	   		
	   		boolean hasEditPermission = false;
	   		int hasTabSheetPermission = Bean.currentMenu.isEditPermited("CALL_CENTER_CALL_GROUP_CLIENTS");
	   		if (hasTabSheetPermission > 0) {
	   			hasEditPermission = true;
	   		}
			
	   		String	id_activity 				= Bean.getDecodeParam(parameters.get("id_activity"));
	   		
	   		bcCallCenterCallGroupObject group = new bcCallCenterCallGroupObject(id);
			
			bcCallCenterQuestionObject question = new bcCallCenterQuestionObject(id_activity);
			
			bcNatPrsObject natprs = new bcNatPrsObject(question.getValue("ID_NAT_PRS"));
			
			bcClubCardObject clubcard = new bcClubCardObject(question.getValue("CARD_SERIAL_NUMBER"), question.getValue("CARD_ID_ISSUER"), question.getValue("CARD_ID_PAYMENT_SYSTEM"), true);
			
			bcClubCardPurseObject superbon = new bcClubCardPurseObject(question.getValue("CARD_SERIAL_NUMBER"), question.getValue("CARD_ID_ISSUER"), question.getValue("CARD_ID_PAYMENT_SYSTEM"), "SUPERBON");
	
	%>
	
	<%= Bean.getOperationTitle(
				Bean.call_centerXML.getfieldTransl("h_edit_call", false),
				"N",
				"N") 
		%>
	<% if (hasEditPermission) { %>
	<script>
		var formDataCallGroup = new Array (
			new Array ('call_date', 'varchar2', 1)
		);
		function myValidateForm() {
			return validateForm(formDataCallGroup);
		}
		function setCheckSummary(id_checkbox, row_number) {
			var checkValue = "";
			var allElem=document.forms['updateForm'].elements;
			for(i=0;i<allElem.length;i++){
				if(allElem[i].type=='checkbox'){
					idElement = allElem[i].id;
					//alert(idElement);
					pos = idElement.indexOf(id_checkbox);
					//alert(pos);
					if (pos != -1 && allElem[i].checked) {
						checkValue = checkValue + allElem[i].title + '\n';
					}
					//if allElem[i].checked {
					//	checkValue = checkValue + allElem[i].title + '\n';
					//}
				}
			}
			document.getElementById(id_checkbox).value = checkValue;
			//alert(document.getElementById(id_checkbox).value);
		}
	</script> 
	<form action="../crm/call_center/call_groupupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return myValidateForm()">
	        <input type="hidden" name="type" value="client">
	        <input type="hidden" name="action" value="edit">
	        <input type="hidden" name="process" value="yes">
	        <input type="hidden" name="id" value="<%=id %>">
	        <input type="hidden" name="id_cc_question" value="<%=question.getValue("ID_CC_QUESTION")%>">
	<% } %>
	<table <%=Bean.getTableMenuFilter() %>>
		
		<tr>
			<td><%= Bean.call_centerXML.getfieldTransl("name_cc_call_group", false) %>
				<%= Bean.getGoToCallCenterCallGroupLink(group.getValue("ID_CC_CALL_GROUP")) %>
			</td><td><input type="text" name="name_cc_call_group" size="40" value="<%= group.getValue("NAME_CC_CALL_GROUP") %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.getClubCardXMLFieldTransl("name_nat_prs", false) %>
				<%= Bean.getGoToNatPrsLink(question.getValue("ID_NAT_PRS")) %>
		    </td><td><input type="text" name="name_nat_prs" size="40" value="<%= Bean.getNatPrsName(question.getValue("ID_NAT_PRS")) %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td><%= Bean.call_centerXML.getfieldTransl("date_cc_call_group", false) %></td><td><input type="text" name="name_cc_call_group" size="10" value="<%= group.getValue("DATE_CC_CALL_GROUP_FRMT") %>" readonly="readonly" class="inputfield-ro"></td>
		  	<td><%= Bean.natprsXML.getfieldTransl("date_of_birth", false) %> </td><td><input type="text" name="date_of_birth" size="10" value="<%= natprs.getValue("DATE_OF_BIRTH_FRMT") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>			
		<tr>
			<td><%= Bean.call_centerXML.getfieldTransl("name_cc_inquirer", false) %>
				<%= Bean.getGoToCallCenterInquirerLink(group.getValue("ID_CC_INQUIRER")) %>
			</td>
			<td>
				<input type="text" name="name_cc_inquirer" size="40" value="<%= group.getValue("NAME_CC_INQUIRER") %>" readonly="readonly" class="inputfield-ro">
			</td>
		    <td><%= Bean.natprsXML.getfieldTransl("phone_mobile", false) %></td><td><input type="text" name="phone_mobile" size="30" value="<%= natprs.getValue("PHONE_MOBILE") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>			
		<tr>
			<td colspan="2"><b><%= Bean.call_centerXML.getfieldTransl("cg_criterion", false) %></b></td>
			<% String cd_card1 = Bean.getClubCardCode(question.getValue("CARD_SERIAL_NUMBER")+"_"+question.getValue("CARD_ID_ISSUER")+"_"+question.getValue("CARD_ID_PAYMENT_SYSTEM")); %>
			<td valign="top" style="border-top:solid windowtext 1.0pt;"><%= Bean.getClubCardXMLFieldTransl("cd_card1", false) %>
				<%= Bean.getGoToClubCardLink(
						question.getValue("CARD_SERIAL_NUMBER"),
						question.getValue("CARD_ID_ISSUER"),
						question.getValue("CARD_ID_PAYMENT_SYSTEM")
					) %>
			</td><td style="border-top:solid windowtext 1.0pt;"><input type="text" name="cd_card1" size="30" value="<%= cd_card1 %>" readonly="readonly" class="inputfield-ro"></td>			
		<tr>
		  	<td><%= Bean.call_centerXML.getfieldTransl("cg_crit_begin_purchase_date", false) %></td><td><input type="text" name="cg_crit_begin_purchase_date" size="10" value="<%= group.getValue("CRIT_BEGIN_PURCHASE_DATE_DF") %>" readonly="readonly" class="inputfield-ro"></td>
			<%  String goToJurPrsHyperLink = Bean.getGoToJurPrsHyperLink(clubcard.getValue("ID_JUR_PRS_WHO_CARD_SOLD"));%>
		  	<td><%= Bean.getClubCardXMLFieldTransl("sname_jur_pr_who_has_sold_card", false) %>
				<%= goToJurPrsHyperLink %></td><td><input type="text" name="id_jur_pr_who_has_sold_card" size="30" value="<%= Bean.getJurPersonShortName(clubcard.getValue("ID_JUR_PRS_WHO_CARD_SOLD")) %>" readonly="readonly" class="inputfield-ro">
			</td>
		</tr>			
		<tr>
		    <td><%= Bean.call_centerXML.getfieldTransl("cg_crit_give_for_event", false) %>
				<%= Bean.getGoToClubEventLink(group.getValue("CRIT_GIVE_FOR_EVENT")) %>
			</td>
		  	<td>
				<input type="text" name="crit_give_for_event" size="40" value="<%= Bean.getClubActionName(group.getValue("CRIT_GIVE_FOR_EVENT")) %>" readonly="readonly" class="inputfield-ro">
			</td>	
			<%  String goToJurPrsHyperLink2 = Bean.getGoToJurPrsHyperLink(clubcard.getValue("ID_JUR_PRS_WHERE_CARD_SOLD"));	%>
			  <td><%= Bean.getClubCardXMLFieldTransl("sname_jur_prs_where_card_sold", false) %>
				<%= goToJurPrsHyperLink2 %></td><td><input type="text" name="id_jur_prs_where_card_sold" size="30" value="<%= Bean.getJurPersonShortName(clubcard.getValue("ID_JUR_PRS_WHERE_CARD_SOLD")) %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
		  	<td><%= Bean.call_centerXML.getfieldTransl("cg_crit_given_from_date", false) %></td><td><input type="text" name="cg_crit_given_from_date" size="10" value="<%= group.getValue("CRIT_GIVEN_FROM_DATE_DF") %>" readonly="readonly" class="inputfield-ro">&nbsp;<%= Bean.call_centerXML.getfieldTransl("cg_crit_given_to_date", false) %>&nbsp;<input type="text" name="cg_crit_given_to_date" size="10" value="<%= group.getValue("CRIT_GIVEN_TO_DATE_DF") %>" readonly="readonly" class="inputfield-ro"></td>
			 <td><%= Bean.getClubCardXMLFieldTransl("name_serv_plce_where_card_sold", false) %>
				<%= Bean.getGoToServicePlaceLink(clubcard.getValue("ID_SERV_PLACE_WHERE_CARD_SOLD")) %>
			  </td><td><input type="text" id="name_service_place" name="name_service_place" size="30" value="<%= Bean.getServicePlaceName(clubcard.getValue("ID_SERV_PLACE_WHERE_CARD_SOLD")) %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
		  	<td><%= Bean.call_centerXML.getfieldTransl("cg_crit_made_purchases", false) %></td>
				<td>
					<%
					  String p_crit_made_purchase_condition = "";
					  if ("LESS".equalsIgnoreCase(group.getValue("CRIT_MADE_PURCHASE_CONDITION"))) {
						  p_crit_made_purchase_condition = Bean.call_centerXML.getfieldTransl("cg_crit_made_purchase_less", false);
					  } else if ("EQUAL".equalsIgnoreCase(group.getValue("CRIT_MADE_PURCHASE_CONDITION"))) {
						  p_crit_made_purchase_condition = Bean.call_centerXML.getfieldTransl("cg_crit_made_purchase_equal", false);
					  } else if ("MORE".equalsIgnoreCase(group.getValue("CRIT_MADE_PURCHASE_CONDITION"))) {
						  p_crit_made_purchase_condition = Bean.call_centerXML.getfieldTransl("cg_crit_made_purchase_more", false);
					  }
					%>
					<input type="text" name="crit_made_purchase_condition" size="10" value="<%= p_crit_made_purchase_condition %>" readonly="readonly" class="inputfield-ro">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="text" name="crit_made_purchase_value" size="5" value="<%= group.getValue("CRIT_MADE_PURCHASE_VALUE") %>" readonly="readonly" class="inputfield-ro">
				</td>
			<td><%= Bean.getClubCardXMLFieldTransl("club_event_given_card", false) %>
				<%= Bean.getGoToClubEventLink(clubcard.getValue("ID_CLUB_EVENT")) %>
			</td>
			<td><input type="text" name="id_club_event" size="35" value="<%= Bean.getClubActionName(clubcard.getValue("ID_CLUB_EVENT")) %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
		  	<td>&nbsp;&nbsp;&nbsp;&nbsp;<%= Bean.call_centerXML.getfieldTransl("cg_crit_made_purchase_from_date", false) %></td><td><input type="text" name="crit_made_purchase_from_date" size="10" value="<%= group.getValue("CRIT_MADE_PURCHASE_FROM_DATE_DF") %>" readonly="readonly" class="inputfield-ro">&nbsp;<%= Bean.call_centerXML.getfieldTransl("cg_crit_made_purchase_to_date", false) %>&nbsp;<input type="text" name="crit_made_purchase_to_date" size="10" value="<%= group.getValue("CRIT_MADE_PURCHASE_TO_DATE_DF") %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.getClubCardXMLFieldTransl("date_card_sale", false) %></td><td><input type="text" name="date_card_sale" size="12" value="<%= clubcard.getValue("DATE_CARD_SALE_DF") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
		    <td>&nbsp;&nbsp;&nbsp;&nbsp;<%= Bean.call_centerXML.getfieldTransl("crit_made_purchase_dealer", false) %>
				<%= Bean.getGoToJurPrsHyperLink(group.getValue("CRIT_MADE_PURCHASE_DEALER")) %>
			</td>
		  	<td>
				<input type="text" name="crit_made_purchase_dealer" size="40" value="<%= Bean.getJurPersonShortName(group.getValue("CRIT_MADE_PURCHASE_DEALER")) %>" readonly="readonly" class="inputfield-ro">
			</td>	
			<td style="border-top:solid windowtext 1.0pt;"><%= Bean.call_centerXML.getfieldTransl("card_bal_exist", false) %> (<%=clubcard.getValue("SNAME_CURRENCY") %>)</td> <td style="border-top:solid windowtext 1.0pt;"><input type="text" name="card_bal_exist" size="12" value="<%= clubcard.getValue("BAL_EXIST_FRMT") %>" readonly="readonly" class="inputfield-ro"> </td>
		</tr>
		<tr>
			<td colspan="2">&nbsp;</td>
			<td><%= Bean.call_centerXML.getfieldTransl("card_bal_feauture", false) %></td> <td><input type="text" name="card_bal_feauture" size="12" value="<%= clubcard.getValue("BAL_FEAUTURE_FRMT") %>" readonly="readonly" class="inputfield-ro"> </td>
		</tr>
		<tr>
			<td colspan="2">&nbsp;</td>
			<td><%= Bean.call_centerXML.getfieldTransl("card_last_date_payment", false) %></td> <td><input type="text" name="card_last_date_payment" size="12" value="<%= clubcard.getValue("DATE_ACC_DF") %>" readonly="readonly" class="inputfield-ro"> </td>
		</tr>
		<tr>
			<td colspan="2">&nbsp;</td>
			<td><%= Bean.call_centerXML.getfieldTransl("card_last_date_payment", false) %></td> <td><input type="text" name="card_last_date_payment" size="12" value="<%= clubcard.getValue("DATE_ACC_DF") %>" readonly="readonly" class="inputfield-ro"> </td>
		</tr>
		<tr>
			<td colspan="2">&nbsp;</td>
			<td><%= Bean.call_centerXML.getfieldTransl("card_payment_count", false) %></td> <td><input type="text" name="card_payment_count" size="12" value="<%= question.getValue("CARD_PAYMENT_COUNT") %>" readonly="readonly" class="inputfield-ro"> </td>
		</tr>
		<tr>
			<td colspan="2">&nbsp;</td>
			<td><%= Bean.call_centerXML.getfieldTransl("card_payment_sum", false) %></td> <td><input type="text" name="card_payment_sum" size="12" value="<%= question.getValue("CARD_PAYMENT_SUM_FRMT") %>" readonly="readonly" class="inputfield-ro"> </td>
		</tr>
		<tr>
			<td colspan="2">&nbsp;</td>
			<td><%= Bean.call_centerXML.getfieldTransl("suporbon_sum", false) %></td> <td><input type="text" name="suporbon_sum" size="12" value="<%= superbon.getValue("VALUE_CARD_PURSE_FRMT") %>" readonly="readonly" class="inputfield-ro"> </td>
		</tr>
		
	</table>
	<table <%=Bean.getTableBottomParam() %>><tbody>
		<%
			String pLineStyle = Bean.getUIUserParam("inquirer_line_answer_style");
			if (pLineStyle == null || "".equalsIgnoreCase(pLineStyle)) {
				pLineStyle = "SELECT";
			}
		%>
		<%=question.getInquirerLineHTML(hasEditPermission, pLineStyle, "1", "1000") %>

		<%
		String pCallDate = question.getValue("DUE_DATE_DF");
		if (pCallDate == null || "".equalsIgnoreCase(pCallDate)) {
			pCallDate = Bean.getSysDate();
		}
		%>

	<% if (hasEditPermission) { %>
		<tr>
		  	<td colspan="2"><%= Bean.call_centerXML.getfieldTransl("call_date", true) %></td><td style="background-color: #D3D3D3;"><%=Bean.getCalendarInputField("call_date", pCallDate, "10") %></td>
		</tr>
		<tr>
			<td colspan="2"><%= Bean.call_centerXML.getfieldTransl("cc_question_status", true) %></td><td style="background-color: #D3D3D3;"><select name="cd_cc_question_status" class="inputfield"><%= Bean.getCallCenterQuestionStatusOptions(question.getValue("CD_CC_QUESTION_STATUS"), false) %></select></td>
		</tr>
		<tr>
			<td><%= Bean.call_centerXML.getfieldTransl("note", false) %></td><td colspan="2"><textarea name="question_note" cols="120" rows="3" class="inputfield"><%= question.getValue("NOTE") %></textarea></td>
		</tr>
	<% } else { %>
		<tr>
		  	<td colspan="2"><%= Bean.call_centerXML.getfieldTransl("call_date", false) %></td><td><input type="text" name="call_date" size="10" value="<%= pCallDate %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td colspan="2"><%= Bean.call_centerXML.getfieldTransl("cc_question_status", false) %></td><td><input type="text" name="name_cc_question_status" size="10" value="<%= question.getValue("NAME_CC_QUESTION_STATUS") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td><%= Bean.call_centerXML.getfieldTransl("note", false) %></td><td colspan="2"><textarea name="question_note" cols="120" rows="3" readonly="readonly" class="inputfield-ro"><%= question.getValue("NOTE") %></textarea></td>
		</tr>
	<% } %>
	</tbody></table>
	<table <%=Bean.getTableMenuFilter() %>>
 		<tr>
			<td colspan="4" align="center">
				<% if (hasEditPermission) { %>
				<%=Bean.getSubmitButtonAjax("../crm/call_center/call_groupupdate.jsp") %>
				<%=Bean.getResetButton() %>
				<% } %>
				<% if (action.equalsIgnoreCase("add")) { %>
					<%=Bean.getGoBackButton("../crm/call_center/call_group.jsp") %>
				<% } else { %>
					<%=Bean.getGoBackButton("../crm/call_center/call_groupspecs.jsp?id=" + id) %>
				<% } %>
			</td>
		</tr>
	</table>
	</form>
	<% if (hasEditPermission) { %>
		<%= Bean.getCalendarScript("call_date", false) %>
	<% } %>
	<%
	   	}
	} else if (process.equalsIgnoreCase("yes"))	{
		if (action.equalsIgnoreCase("edit")) {
		    String  
		    	l_paramCount			= Bean.getDecodeParam(parameters.get("paramcount")),
		    	id_cc_question			= Bean.getDecodeParam(parameters.get("id_cc_question")),
		    	call_date				= Bean.getDecodeParam(parameters.get("call_date")),
		    	cd_cc_question_status	= Bean.getDecodeParam(parameters.get("cd_cc_question_status")),
		    	question_note			= Bean.getDecodeParam(parameters.get("question_note"));
		    
		String callSQL = "";
		String[] results = new String[2];
		String resultInt = "";
		String resultFull = "0";
		String resultMessage = "";
		String resultMessageFull = "";
		    
		ArrayList<String> id_inquirer=new ArrayList<String>();
		ArrayList<String> value_inquirer=new ArrayList<String>();
    	Set<String> keySet = parameters.keySet();
    	Iterator<String> keySetIterator = keySet.iterator();
    	String key = "";
    	while(keySetIterator.hasNext()) {
    		try{
    			key = (String)keySetIterator.next();
    			if(key.contains("inquirer_line__")){
    				id_inquirer.add(key.substring(15));
    				value_inquirer.add(Bean.getDecodeParam(parameters.get(key)));
    			}
    			
    		}
    		catch(Exception ex){
    			Bean.writeException(
    						"../crm/call_center/call_groupupdate.jsp",
    						type,
    						process,
    						action,
    						Bean.commonXML.getfieldTransl("h_get_param_value_error", false)+key+": " + ex.toString());
    		}
    		 
    	}

    %>

		<%= Bean.form_messageXML.getfieldTransl("processing_update", false) %>
		<%
		if (!(id_inquirer == null)) {
		 for(int counter=0;counter<id_inquirer.size();counter++){
	    	callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_CALL_CENTER.set_question_inquirer_line(" +
	    		"?,?,?,?)}";

	    	ArrayList<String> pParam2 = new ArrayList<String>();
						
			pParam2.add(id_cc_question);
			pParam2.add(id_inquirer.get(counter));
			pParam2.add(value_inquirer.get(counter));
		
			results = Bean.myCallFunctionParam(callSQL, pParam2, 2);
			resultInt = results[0];
			resultMessage = results[1];
			
			%>
			<%= Bean.showCallSQL(callSQL) %>
			<%
			
			if (!("0".equalsIgnoreCase(resultInt))) {
				resultFull = resultInt;
				resultMessageFull = resultMessageFull + "; " +resultMessage;
			}
		 }
		}
		
		callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_CALL_CENTER.update_question_inquirer(" + 
			"?,?,?,?,?,?)}";

		ArrayList<String> pParam = new ArrayList<String>();
					
		pParam.add(id_cc_question);
		pParam.add(call_date);
		pParam.add(cd_cc_question_status);
		pParam.add(question_note);
		pParam.add(Bean.getDateFormat());
		
		results = Bean.myCallFunctionParam(callSQL, pParam, 2);
		resultInt = results[0];
		resultMessage = results[1];
		
		%>
		<%= Bean.showCallSQL(callSQL) %>
		<%
		
		if (!("0".equalsIgnoreCase(resultInt))) {
			resultFull = resultInt;
			resultMessageFull = resultMessageFull + "; " +resultMessage;
		}
		
		%>
  	    <%=Bean.showCallResult(
   	    		callSQL, 
   	    		resultFull, 
   	    		resultMessageFull, 
   	    		"../crm/call_center/call_groupspecs.jsp?id=" + id, 
   	    		"../crm/call_center/call_groupspecs.jsp?id=" + id, 
   	    		Bean.form_messageXML.getfieldTransl("save_error", false)) %>
   		<% 

	    } else { %> 
	    	<%= Bean.getUnknownActionText(action) %><% 
		}
	}
} else {
    %> <%= Bean.getUnknownTypeText(type) %> <%
}
%>


</body>
</html>
