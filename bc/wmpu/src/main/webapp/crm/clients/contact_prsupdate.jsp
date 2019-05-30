<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%= Bean.getLogOutScript(request) %>

<%@page import="java.util.HashMap"%>
<%@page import="bc.objects.bcContactPrsDataTerminalObject"%>
<%@page import="bc.objects.bcClubShortObject"%>
<%@page import="java.util.ArrayList"%>
<%@page import="bc.objects.bcUserObject"%>
<%@page import="bc.objects.bcContactsObject"%>
<%@page import="bc.objects.bcTerminalObject"%>
<%@page import="bc.objects.bcTerminalUserObject"%><html>
<head>
	<%= Bean.getMetaContent() %>
	<%= Bean.getBottomFrameCSS() %>

</head>

<body>
<%
HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String pageFormName = "CLIENTS_CONTACT_PRS";

Bean.setJspPageForTabName(pageFormName);

request.setCharacterEncoding("UTF-8");

String external_id				= Bean.getDecodeParamPrepare(parameters.get("id")); 
String action					= Bean.getDecodeParamPrepare(parameters.get("action")); 
String process					= Bean.getDecodeParamPrepare(parameters.get("process"));
String type						= Bean.getDecodeParamPrepare(parameters.get("type"));
String id_contact_prs			= Bean.getDecodeParamPrepare(parameters.get("id_contact_prs"));
String id_jur_prs				= Bean.getDecodeParamPrepare(parameters.get("id_jur_prs"));
String id_service_place_work	= Bean.getDecodeParamPrepare(parameters.get("id_service_place_work"));
String back_type				= Bean.getDecodeParamPrepare(parameters.get("back_type"));

String updateLink = "../crm/clients/contact_prsupdate.jsp";
String backLink = "";
String generalLink = "";
back_type = Bean.isEmpty(back_type)?"CONTACT_PRS":back_type;
System.out.println("back_type="+back_type);
if ("CONTACT_PRS".equalsIgnoreCase(back_type)) {
	generalLink = "../crm/clients/contact_prs.jsp?";
	if (action.equalsIgnoreCase("add")) {
		backLink = "../crm/clients/contact_prs.jsp?";
	} else {
		backLink = "../crm/clients/contact_prsspecs.jsp?id="+id_contact_prs;
	}
} else if ("PARTNER".equalsIgnoreCase(back_type)) {
	generalLink = "../crm/clients/yurpersonspecs.jsp?id=" + external_id;
	backLink = "../crm/clients/yurpersonspecs.jsp?id=" + external_id;
}

if (type.equalsIgnoreCase("general")) {
	if (process.equalsIgnoreCase("no"))
	/* вибираємо тип дії (добавити, видалити...)*/
		{
		   /*  --- Добавити запис --- */
	    	if (action.equalsIgnoreCase("add") || action.equalsIgnoreCase("add2")) {
	    		
	    		bcClubShortObject club = new bcClubShortObject(Bean.getCurrentClubID());
	    		
		        %>
		<script>
			var formData = new Array (
				new Array ('surname', 'varchar2', 1),
				new Array ('sex', 'varchar2', 1),
				new Array ('name_club', 'varchar2', 1),
				new Array ('club_date_beg', 'varchar2', 1),
				<% if (Bean.isEmpty(id_service_place_work)) { %>
				new Array ('name_service_place_work', 'varchar2', 1),
				<% } %>
				new Array ('cd_post', 'varchar2', 1)
			);
			function myValidateForm() {
				return validateForm(formData);
			}
		</script>
			<% if ("CONTACT_PRS".equalsIgnoreCase(back_type)) { %>
			<%= Bean.getOperationTitle(
					Bean.contactXML.getfieldTransl("l_add_contact", false),
					"Y",
					"Y") 
			%>
			<% } else { %>
			<%= Bean.getOperationTitleShort(
					"",
					Bean.contactXML.getfieldTransl("l_add_contact", false),
					"Y",
					"Y") 
			%>
			<% } %>
	    <form action="<%=updateLink %>" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">
		    <input type="hidden" name="type" value="general">
		    <input type="hidden" name="action" value="add">
		    <input type="hidden" name="process" value="yes">
    		<input type="hidden" name="back_type" value="<%=back_type %>">
    		<input type="hidden" name="id" value="<%=external_id %>">
    		<input type="hidden" name="id_contact_prs" value="<%=id_contact_prs %>">
    		<input type="hidden" name="id_service_place_work" value="<%=id_service_place_work %>">
		<table <%=Bean.getTableDetailParam() %>>
			<tr>
				<td><%= Bean.natprsXML.getfieldTransl("surname", true) %></td> <td><input type="text" name="surname" size="55" value="" class="inputfield"> </td>
	 		    <td><%= Bean.clubXML.getfieldTransl("club", true) %>
					<%= Bean.getGoToClubLink(club.getValue("ID_CLUB")) %>
				</td>
			  	<td>
					<%=Bean.getWindowFindClub("club", club.getValue("ID_CLUB"), club.getValue("SNAME_CLUB"), "25") %>
		  		</td>
			</tr>
			<tr>
				<td><%= Bean.natprsXML.getfieldTransl("name", false) %></td> <td><input type="text" name="name" size="55" value="" class="inputfield"> </td>
				<td><%= Bean.contactXML.getfieldTransl("club_date_beg", true) %></td> <td><%=Bean.getCalendarInputField("club_date_beg", "", "10") %></td>
			</tr>
			<tr>
				<td><%= Bean.natprsXML.getfieldTransl("patronymic", false) %></td> <td><input type="text" name="patronymic" size="55" value="" class="inputfield"> </td>
				<td rowspan="4"><%= Bean.contactXML.getfieldTransl("desc_contact_prs", false) %></td><td rowspan="4"><textarea name="desc_contact_prs" cols="52" rows="3" class="inputfield"></textarea></td>
			</tr>
			<tr>
				<td><%= Bean.natprsXML.getfieldTransl("sex", true) %></td><td><select name="sex" class="inputfield"><%= Bean.getMeaningFromLookupNameOptions("MALE_FEMALE", "", true) %></select> </td>
			</tr>
		    <tr>
				<td><%=Bean.contactXML.getfieldTransl("contact_prs_place", Bean.isEmpty(id_service_place_work))%>
					<%=Bean.getGoToServicePlaceLink(id_service_place_work) %>
				</td>
				<td>
					<% if (!Bean.isEmpty(id_service_place_work)) { %>
    				<input type="hidden" name="id_service_place_work" value="<%=id_service_place_work %>">
					<input type="text" name="sname_service_place_work" size="55" value="<%=Bean.getServicePlaceShortName(id_service_place_work)%>" readonly="readonly" class="inputfield-ro">
					<% } else { %>
					<%=Bean.getWindowFindServicePlace("service_place_work", id_service_place_work, "", id_jur_prs, "", "37") %>
					<% } %>
				</td>
			</tr>
		    <tr>
				<td><%= Bean.contactXML.getfieldTransl("name_post", true) %></td> <td><select name="cd_post" class="inputfield" > <%= Bean.getContactPrsTypeOptions("", true) %></select></td>
			</tr>
		    <tr>
				<td colspan="4" class="top_line"><b><%= Bean.contactXML.getfieldTransl("h_contact_information", false) %></b></td>
			</tr>
		    <tr>
				<td><%= Bean.contactXML.getfieldTransl("phone_work", false) %></td> <td><input type="text" name="phone_work" size="30" value="" class="inputfield"></td>
				<td><%= Bean.contactXML.getfieldTransl("phone_mobile", false) %></td> <td><input type="text" name="phone_mobile" size="30" value="" class="inputfield"></td>
			</tr>
		    <tr>
				<td><%= Bean.contactXML.getfieldTransl("email_work", false) %></td><td><input type="text" name="email_work" size="30" value="" class="inputfield"></td>
				<td colspan="2">&nbsp;</td>
			</tr>
	 		<tr>
				<td colspan="4" align="center">
					<%=Bean.getSubmitButtonAjax(updateLink) %>
					<%=Bean.getResetButton() %>
					<%=Bean.getGoBackButton(backLink) %>
				</td>
			</tr>
	
		</table>
	
	</form>
		<%= Bean.getCalendarScript("club_date_beg", false) %>
	
		        <%
	    	} else if (action.equalsIgnoreCase("edit")) {
	    		

	    		bcContactsObject contact = new bcContactsObject(id_contact_prs);
	    		
	    		boolean isShareholder = !Bean.isEmpty(contact.getValue("CARD_SERIAL_NUMBER"));
	    		
		        %>
		<script>
			var formData = new Array (
				new Array ('name_service_place_work', 'varchar2', 1),
				new Array ('cd_post', 'varchar2', 1),
				new Array ('name_contact_prs', 'varchar2', 1)
			);
			function myValidateForm() {
				return validateForm(formData);
			}
		</script>
			<% if ("CONTACT_PRS".equalsIgnoreCase(back_type)) { %>
			<%= Bean.getOperationTitle(
					Bean.contactXML.getfieldTransl("l_edit_contact", false),
					"Y",
					"Y") 
			%>
			<% } else { %>
			<%= Bean.getOperationTitleShort(
					"",
					Bean.contactXML.getfieldTransl("l_edit_contact", false),
					"Y",
					"Y") 
			%>
			<% } %>
	    <form action="<%=updateLink %>" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">
		    <input type="hidden" name="type" value="general">
		    <input type="hidden" name="action" value="edit">
		    <input type="hidden" name="process" value="yes">
    		<input type="hidden" name="back_type" value="<%=back_type %>">
    		<input type="hidden" name="id" value="<%=external_id %>">
    		<input type="hidden" name="id_contact_prs" value="<%=id_contact_prs %>">
    		<input type="hidden" name="id_service_place_work" value="<%=id_service_place_work %>">
        	<input type="hidden" name="LUD" value="<%=contact.getValue("LUD")%>">

	<table <%=Bean.getTableDetailParam() %>>
	   <tr>
			<td><%= Bean.contactXML.getfieldTransl("name_contact_prs", false) %>
					<%= Bean.getGoToContactPersonLink(id_contact_prs) %>
				</td> <td><input type="text" name="name_contact_prs" size="55" value="<%=contact.getValue("NAME_CONTACT_PRS") %>" readonly="readonly" class="inputfield-ro"> </td>
			<td><%= Bean.clubXML.getfieldTransl("club", false) %>
					<%= Bean.getGoToClubLink(contact.getValue("ID_CLUB")) %>
			</td><td><input type="text" name="name_club" size="55" value="<%= Bean.getClubShortName(contact.getValue("ID_CLUB")) %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
	    <tr>
			<td><%=Bean.contactXML.getfieldTransl("contact_prs_place", false)%>
				<%=Bean.getGoToJurPrsHyperLink(contact.getValue("ID_SERVICE_PLACE_WORK")) %>
			</td> <td><input type="text" name="sname_service_place_work" size="55" value="<%=contact.getValue("SNAME_SERVICE_PLACE_WORK")%>" readonly="readonly" class="inputfield-ro"> </td>
			<td><%= Bean.jurpersonXML.getfieldTransl("club_date_beg", true) %></td> <td><%=Bean.getCalendarInputField("club_date_beg", contact.getValue("CLUB_DATE_BEG_DF"), "10") %></td>
		
		</tr>
	    <tr>
			<td><%= Bean.contactXML.getfieldTransl("name_post", true) %></td> <td><select name="cd_post" class="inputfield" > <%= Bean.getContactPrsTypeOptions(contact.getValue("CD_POST"), true) %></select></td>
			<% if (isShareholder) { %>
			<td><%= Bean.contactXML.getfieldTransl("is_shareholder", false) %>
					<%= Bean.getGoToNatPrsLink(contact.getValue("ID_NAT_PRS")) %>
				</td> <td><input type="text" name="is_shareholder_tsl" size="55" value="<%=contact.getValue("NAME_CONTACT_PRS") %>" readonly="readonly" class="inputfield-ro"></td>
			<% } else { %>
			<td><%=Bean.clubcardXML.getfieldTransl("title_card_not_issued", false)%></td>
			<td><%=Bean.getSubmitButtonAjax("../crm/cards/clubcardupdate.jsp", "button_give", "updateGiveCard", "div_main") %></td>
			<% } %>
		</tr>
	    <tr>
			<td rowspan="3"><%= Bean.contactXML.getfieldTransl("desc_contact_prs", false) %></td><td rowspan="3"><textarea name="desc_contact_prs" cols="52" rows="3" class="inputfield"><%=contact.getValue("DESC_NAT_PRS_ROLE") %></textarea></td>
			<% if (isShareholder) { %>
			<td><%= Bean.getClubCardXMLFieldTransl("cd_card1", false) %>
				<%= Bean.getGoToClubCardLink(
						contact.getValue("CARD_SERIAL_NUMBER"),
						contact.getValue("CARD_ID_ISSUER"),
						contact.getValue("CARD_ID_PAYMENT_SYSTEM")
					) %>
			</td><td><input type="text" name="cd_card1" size="30" value="<%= contact.getValue("CD_CARD1") %>" readonly="readonly" class="inputfield-ro"></td>
			<% } else { %>
			<td colspan="2">&nbsp;</td>
			<% } %>			
		</tr>
	    <tr>
			<% if (isShareholder) { %>
			<td>&nbsp;</td>
			<td><%=Bean.natprsXML.getfieldTransl("title_card", false)%>:&nbsp;
				<%=Bean.getCheckBoxBase("is_corporate_card", contact.getValue("IS_CORPORATE_CARD"), Bean.natprsXML.getfieldTransl("is_corporate_card", false), false, true) %>
				<%=Bean.getCheckBoxBase("is_temporary_card", contact.getValue("IS_TEMPORARY_CARD"), Bean.natprsXML.getfieldTransl("is_temporary_card", false), false, true) %>
			</td>
			<% } else { %>
			<td colspan="2">&nbsp;</td>
			<% } %>			
		</tr>
	    <tr><td colspan="2">&nbsp;</td></tr>
	    <tr>
			<td colspan="4" class="top_line"><b><%= Bean.contactXML.getfieldTransl("h_contact_information", false) %></b></td>
		</tr>
	    <tr>
			<td><%= Bean.contactXML.getfieldTransl("phone_work", false) %></td> <td><input type="text" name="phone_work" size="30" value="<%=contact.getValue("PHONE_WORK") %>" class="inputfield"></td>
			<td><%= Bean.contactXML.getfieldTransl("phone_mobile", false) %></td> <td><input type="text" name="phone_mobile" size="30" value="<%=contact.getValue("PHONE_MOBILE") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
	    <tr>
			<td><%= Bean.contactXML.getfieldTransl("email_work", false) %></td><td><input type="text" name="email_work" size="30" value="<%=contact.getValue("EMAIL_WORK") %>" class="inputfield"></td>
			<td colspan="2">&nbsp;</td>
		</tr>
		<%=	Bean.getIdCreationAndMoficationRecordFields(
				contact.getValue("ID_CONTACT_PRS"),
				contact.getValue(Bean.getCreationDateFieldName()),
				contact.getValue("CREATED_BY"),
				contact.getValue(Bean.getLastUpdateDateFieldName()),
				contact.getValue("LAST_UPDATE_BY")
			) %>
		<tr>
			<td colspan="4" align="center">
				<%=Bean.getSubmitButtonAjax(updateLink) %>
				<%=Bean.getResetButton() %>
				<%=Bean.getGoBackButton(backLink) %>
			</td>
		</tr>

	</table>
	</form>

	<form action="../crm/cards/clubcardupdate.jsp" name="updateGiveCard" id="updateGiveCard" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">
			<input type="hidden" name="type" value="actions">
	    	<input type="hidden" name="process" value="no">
	    	<input type="hidden" name="id_contact_prs" value="<%=id_contact_prs %>">
	    	<input type="hidden" name="action" value="choisecontact">
	    	<input type="hidden" name="card_action" value="give">
	</form>
			<%
	    	} else {
	    	    %> <%= Bean.getUnknownActionText(action) %><%
	    	}
		}
	
	else if (process.equalsIgnoreCase("yes"))
		{
		String 
			surname				= Bean.getDecodeParam(parameters.get("surname")),
			name 				= Bean.getDecodeParam(parameters.get("name")),
			patronymic 			= Bean.getDecodeParam(parameters.get("patronymic")),
			sex 				= Bean.getDecodeParam(parameters.get("sex")),
			desc_contact_prs 	= Bean.getDecodeParam(parameters.get("desc_contact_prs")),
			cd_post				= Bean.getDecodeParam(parameters.get("cd_post")),
			phone_work 			= Bean.getDecodeParam(parameters.get("phone_work")),
			phone_mobile 		= Bean.getDecodeParam(parameters.get("phone_mobile")),
			email_work 			= Bean.getDecodeParam(parameters.get("email_work")),
			id_club				= Bean.getDecodeParam(parameters.get("id_club")),
			club_date_beg		= Bean.getDecodeParam(parameters.get("club_date_beg")),
			LUD					= Bean.getDecodeParam(parameters.get("LUD"));
	
		if (action.equalsIgnoreCase("add")) {
			ArrayList<String> pParam = new ArrayList<String>();
					 		 	    	      				
			pParam.add(id_service_place_work);
			pParam.add(cd_post);
			pParam.add(surname);
			pParam.add(name);
			pParam.add(patronymic);
			pParam.add(desc_contact_prs);
			pParam.add(sex);
			pParam.add(phone_work);
			pParam.add(phone_mobile);
			pParam.add(email_work);
			pParam.add(id_club);
			pParam.add(club_date_beg);
			pParam.add(Bean.getDateFormat());
	
		 	%>
			<%= Bean.executeInsertFunction("PACK$CONTACT_PRS_UI.add_contact_prs", pParam, backLink + "&id_contact_prs=", "") %>
			<% 	
		
		} else if (action.equalsIgnoreCase("remove")) {
			
			ArrayList<String> pParam = new ArrayList<String>();
							 		 	    	      				
			pParam.add(id_contact_prs);
	
		 	%>
			<%= Bean.executeDeleteFunction("PACK$CONTACT_PRS_UI.delete_contact_prs", pParam, generalLink, "") %>
			<% 	
	 	
		} else if (action.equalsIgnoreCase("edit")) {
		
			ArrayList<String> pParam = new ArrayList<String>();
						 		 	    	      				
			pParam.add(id_contact_prs);
			//pParam.add(id_service_place_work);
			pParam.add(cd_post);
			pParam.add(desc_contact_prs);
			pParam.add(phone_work);
			pParam.add(email_work);
			pParam.add(LUD);
	
		 	%>
			<%= Bean.executeUpdateFunction("PACK$CONTACT_PRS_UI.update_contact_prs", pParam, backLink, "") %>
			<% 	
		
		} else { %> 
			<%= Bean.getUnknownActionText(action) %><%
		}
	
	} else {
	    %> <%= Bean.getUnknownProcessText(process) %> <%
	}
} else if (type.equalsIgnoreCase("system_user")) {
	%> 
	<% 
	  if (process.equalsIgnoreCase("no"))
	  /* вибираємо тип дії (добавити, видалити...)*/
		{
		   /*  --- Добавити запис --- */
	    	if (action.equalsIgnoreCase("add")) { 
	    		bcContactsObject contact = new bcContactsObject(id_contact_prs);
	    		
	    		%>
 
				<%= Bean.getOperationTitleShort(
						"",
						Bean.userXML.getfieldTransl("LAB_ADD_USER", false),
						"Y",
						"N") 
				%>
		<script>
			var formData = new Array (
				new Array ('name_user', 'varchar2', 1),
				new Array ('password', 'varchar2', 1),
				new Array ('confirm_password', 'varchar2', 1),
				new Array ('cd_user_status', 'varchar2', 1)
			);
			function myValidateForm() {
				return validateForm(formData);
			}
		</script>
        <form action="../crm/clients/terminalupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST">
	        <input type="hidden" name="type" value="system_user">
	        <input type="hidden" name="action" value="add">
	        <input type="hidden" name="process" value="yes">
	        <input type="hidden" name="id_contact_prs" value="<%=id_contact_prs %>">
	        <input type="hidden" name="id_nat_prs_role" value="<%=contact.getValue("ID_NAT_PRS_ROLE") %>">
		<table <%=Bean.getTableDetailParam() %>>
			<tr>
				<td><%= Bean.userXML.getfieldTransl("id_user", true) %> </td><td><input type="text" name="name_user" size="40" value="" class="inputfield"></td>
			</tr>
		    <tr>
				<td><%= Bean.userXML.getfieldTransl("password", true) %> </td><td><input type="password" name="password" size="40" value="" class="inputfield"></td>
	    	</tr>
		    <tr>
				<td valign="top"><%= Bean.userXML.getfieldTransl("confirm_password", true) %> </td><td valign="top"><input type="password" name="confirm_password" size="40" value="" class="inputfield"></td>
			</tr>	
		    <tr>
				<td><%= Bean.userXML.getfieldTransl("desc_user", false) %></td> <td><textarea name="desc_user" cols="57" rows="3" class="inputfield"></textarea></td>
			</tr>	
			<tr>
				<td><%= Bean.userXML.getfieldTransl("cd_user_status", true) %></td><td><select name="cd_user_status" id="cd_user_status" class="inputfield"><%= Bean.getUserStatusOptions("", true) %></select></td>
			</tr>
			<tr>
				<td colspan="2"></td>
			</tr>
			<tr>
				<td colspan="6" align="center">
					<%=Bean.getSubmitButtonAjax("../crm/clients/contact_prsupdate.jsp") %>
					<%=Bean.getResetButton() %>
					<%=Bean.getGoBackButton("../crm/clients/contact_prsspecs.jsp?id=" + id_contact_prs) %>
				</td>
			</tr>
		</table>
		</form>

		        <%
	    	} else if (action.equalsIgnoreCase("edit")) { 
	    		String id_system_user	= Bean.getDecodeParam(parameters.get("id_system_user"));
	    		bcUserObject user 		= new bcUserObject(id_system_user);

	    		%>
 
				<%= Bean.getOperationTitleShort(
						"",
						Bean.userXML.getfieldTransl("LAB_EDIT_USER", false),
						"Y",
						"N") 
				%>
		<script>
			var formData = new Array (
				new Array ('cd_user_status', 'varchar2', 1)
			);
			function myValidateForm() {
				return validateForm(formData);
			}
		</script>
        <form action="../crm/clients/terminalupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST">
	        <input type="hidden" name="type" value="system_user">
	        <input type="hidden" name="action" value="edit">
	        <input type="hidden" name="process" value="yes">
	        <input type="hidden" name="id_contact_prs" value="<%=id_contact_prs %>">
	        <input type="hidden" name="id_system_user" value="<%=id_system_user %>">
		<table <%=Bean.getTableDetailParam() %>>
			<tr>
				<td><%= Bean.userXML.getfieldTransl("NAME_USER", false) %> </td><td><input type="text" name="name_user" size="40" value="<%=user.getValue("NAME_USER") %>" readonly="readonly" class="inputfield-ro"></td>
			</tr>
		    <tr>
				<td><%= Bean.userXML.getfieldTransl("password", false) %> </td><td><input type="password" name="password" size="40" value="" class="inputfield"></td>
	    	</tr>
		    <tr>
				<td valign="top"><%= Bean.userXML.getfieldTransl("confirm_password", false) %> </td><td valign="top"><input type="password" name="confirm_password" size="40" value="" class="inputfield"></td>
			</tr>	
		    <tr>
				<td><%= Bean.userXML.getfieldTransl("DESC_USER", false) %></td> <td><textarea name="desc_user" cols="57" rows="3" class="inputfield"><%=user.getValue("DESC_USER") %></textarea></td>
			</tr>	
			<tr>
				<td><%= Bean.userXML.getfieldTransl("cd_user_status", true) %></td><td><select name="cd_user_status" id="cd_user_status" class="inputfield"><%= Bean.getUserStatusOptions(user.getValue("CD_USER_STATUS"), true) %></select></td>
			</tr>
			<%=	Bean.getCreationAndMoficationRecordFields(
					"2",
					"1",
					user.getValue("ID_USER"),
					"",
					user.getValue(Bean.getCreationDateFieldName()),
					"",
					user.getValue("CREATED_BY"),
					"",
					user.getValue(Bean.getLastUpdateDateFieldName()),
					"",
					user.getValue("LAST_UPDATE_BY"),
					""
			) %>
			<tr>
				<td colspan="2"></td>
			</tr>
			<tr>
				<td colspan="6" align="center">
					<%=Bean.getSubmitButtonAjax("../crm/clients/contact_prsupdate.jsp") %>
					<%=Bean.getResetButton() %>
					<%=Bean.getGoBackButton("../crm/clients/contact_prsspecs.jsp?id=" + id_contact_prs) %>
				</td>
			</tr>
		</table>
		</form>

		        <%
	    	} else {
	    	    %><%= Bean.getUnknownActionText(process) %><%
	    	}
	
	} else if (process.equalsIgnoreCase("yes")) {
	    String
	    	id_system_user			= Bean.getDecodeParam(parameters.get("id_system_user")),
	    	name_user				= Bean.getDecodeParam(parameters.get("name_user")),
	    	password				= Bean.getDecodeParam(parameters.get("password")),
	    	confirm_password		= Bean.getDecodeParam(parameters.get("confirm_password")),
	    	desc_user				= Bean.getDecodeParam(parameters.get("desc_user")),
	    	cd_user_status			= Bean.getDecodeParam(parameters.get("cd_user_status"));
	    
	    ArrayList<String> pParam = new ArrayList<String>();
	    
		if (action.equalsIgnoreCase("add")) {
	    	 		 	    	      				
	    	pParam.add(name_user);
	    	pParam.add(password);
	    	pParam.add(confirm_password);
	    	pParam.add(desc_user);
	    	pParam.add(cd_user_status);
	    	pParam.add(id_contact_prs);

    		%>
    		<%= Bean.executeInsertFunction("PACK$CONTACT_PRS_UI.add_system_user", pParam, "../crm/clients/contact_prsspecs.jsp?id=" + id_contact_prs + "&id_system_user=", "") %>
    		<% 	
	    } else if (action.equalsIgnoreCase("remove")) {
     		 	    	      				
     	    pParam.add(id_system_user);

		 	%>
			<%= Bean.executeUpdateFunction("PACK$CONTACT_PRS_UI.delete_system_user", pParam, "../crm/clients/contact_prsspecs.jsp?id=" + id_contact_prs, "") %>
			<% 	

	    } else if (action.equalsIgnoreCase("edit")) {
	    	 		 	    	      				
	    	pParam.add(id_system_user);
	    	pParam.add(password);
	    	pParam.add(confirm_password);
	    	pParam.add(desc_user);
	    	pParam.add(cd_user_status);
	    	pParam.add(id_contact_prs);

    		%>
    		<%= Bean.executeUpdateFunction("PACK$CONTACT_PRS_UI.update_system_user", pParam, "../crm/clients/contact_prsspecs.jsp?id=" + id_contact_prs, "") %>
    		<% 	
	    } else  { %><%= Bean.getUnknownProcessText(process) %><% 
	    }
	} else {
	    %><%= Bean.getUnknownProcessText(process) %><%
	}
} else {
    %> <%= Bean.getUnknownTypeText(type) %> <%
}
%>


</body>
</html>
