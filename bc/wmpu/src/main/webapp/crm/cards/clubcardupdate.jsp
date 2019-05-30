<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%= Bean.getLogOutScript(request) %>

<%@page import="java.util.HashMap"%>
<%@page import="bc.objects.bcErrorTransactionObject"%>
<%@page import="bc.objects.bcClubCardOperationObject"%>
<%@page import="bc.objects.bcClubCardObject"%>
<%@page import="bc.objects.bcClubCardPurseObject"%>
<%@page import="bc.objects.bcNatPrsObject"%>
<%@page import="bc.objects.bcNatPrsRoleObject"%>
<%@page import="bc.objects.bcClubShortObject"%>
<%@page import="java.util.ArrayList"%><html>
<head>
	<%= Bean.getMetaContent() %>
	<%= Bean.getBottomFrameCSS() %>
	
</head>

<body>
<%
HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String pageFormName = "CARDS_CLUBCARDS";

Bean.setJspPageForTabName(pageFormName);

request.setCharacterEncoding("UTF-8");

String type						= Bean.getDecodeParamPrepare(parameters.get("type")); 
String action					= Bean.getDecodeParamPrepare(parameters.get("action")); 
String process					= Bean.getDecodeParamPrepare(parameters.get("process"));
String card_serial_number		= Bean.getDecodeParamPrepare(parameters.get("card_serial_number"));
String id_issuer				= Bean.getDecodeParamPrepare(parameters.get("id_issuer")); 
String id_payment_system		= Bean.getDecodeParamPrepare(parameters.get("id_payment_system"));
String id_card_status			= Bean.getDecodeParamPrepare(parameters.get("id_card_status"));
String cd_card1               	= Bean.getDecodeParamPrepare(parameters.get("cd_card1"));

if (type.equalsIgnoreCase("general")) {
	if (process.equalsIgnoreCase("yes")) {
		
		ArrayList<String> pParam = new ArrayList<String>();
	    
	    if (action.equalsIgnoreCase("edit")) { 
	    	
	    	String
	    		id_nat_prs_role 			= Bean.getDecodeParam(parameters.get("id_nat_prs_role")),
				is_questionnaire_checked 	= Bean.getDecodeParam(parameters.get("is_questionnaire_checked")),
				can_use_share_account 		= Bean.getDecodeParam(parameters.get("can_use_share_account")),
				club_date_beg 				= Bean.getDecodeParam(parameters.get("club_date_beg")),
				club_date_end 				= Bean.getDecodeParam(parameters.get("club_date_end")),
				cd_club_member_status		= Bean.getDecodeParam(parameters.get("cd_club_member_status")),
				is_organizer 				= Bean.getDecodeParam(parameters.get("is_organizer")),
				is_investor 				= Bean.getDecodeParam(parameters.get("is_investor")),
				desc_nat_prs_role			= Bean.getDecodeParam(parameters.get("desc_nat_prs_role")),
				id_service_place_work 		= Bean.getDecodeParam(parameters.get("id_service_place_work")),
				cd_post			 			= Bean.getDecodeParam(parameters.get("cd_post")),
				phone_work			 		= Bean.getDecodeParam(parameters.get("phone_work")),
				is_phone_work_valid			= Bean.getDecodeParam(parameters.get("is_phone_work_valid")),
				email_work			 		= Bean.getDecodeParam(parameters.get("email_work")),
				is_email_work_valid			= Bean.getDecodeParam(parameters.get("is_email_work_valid")),
				is_corporate_card			= Bean.getDecodeParam(parameters.get("is_corporate_card")),
				is_temporary_card			= Bean.getDecodeParam(parameters.get("is_temporary_card")),
				id_referral_nat_prs_role	= Bean.getDecodeParam(parameters.get("id_referral_nat_prs_role")),
				membership_month_sum		= Bean.getDecodeParam(parameters.get("membership_month_sum")),
				cd_currency					= Bean.getDecodeParam(parameters.get("cd_currency")),
				membership_last_date		= Bean.getDecodeParam(parameters.get("membership_last_date")),
				LUD							= Bean.getDecodeParam(parameters.get("LUD"));
	
	    	pParam.add(id_nat_prs_role);
			pParam.add(desc_nat_prs_role);
			pParam.add(is_questionnaire_checked);
			pParam.add(can_use_share_account);
			pParam.add(is_organizer);
			pParam.add(is_investor);
			pParam.add(id_service_place_work);
			pParam.add(cd_post);
			pParam.add(phone_work);
			pParam.add(is_phone_work_valid);
			pParam.add(email_work);
			pParam.add(is_email_work_valid);
			pParam.add(is_corporate_card);
			pParam.add(is_temporary_card);
			pParam.add(id_referral_nat_prs_role);
			pParam.add(membership_month_sum);
			pParam.add(cd_currency);
			pParam.add(membership_last_date);
			pParam.add(cd_club_member_status);
			pParam.add(club_date_beg);
			pParam.add(club_date_end);
			pParam.add(LUD);
			pParam.add(Bean.getDateFormat());
			
		 	%>
			<%= Bean.executeUpdateFunction("PACK$NAT_PRS_UI.update_nat_prs_role", pParam, "../crm/cards/clubcardspecs.jsp?id=" + card_serial_number + "&iss=" + id_issuer + "&paysys=" + id_payment_system, "") %>
			<% 	
	
	    } else if (action.equalsIgnoreCase("bonappl")) { 
	
	    	String
		        vk_sys_key_card		= Bean.getDecodeParam(parameters.get("vk_sys_key_card")),
		        nt_icc				= Bean.getDecodeParam(parameters.get("nt_icc")),
	        	id_bon_category		= Bean.getDecodeParam(parameters.get("id_bon_category")),
	        	id_disc_category	= Bean.getDecodeParam(parameters.get("id_disc_category")),
	        	bal_acc				= Bean.getDecodeParam(parameters.get("bal_acc")),
	        	bal_cur				= Bean.getDecodeParam(parameters.get("bal_cur")),
	        	bal_bon_per			= Bean.getDecodeParam(parameters.get("bal_bon_per")),
	        	bal_disc_per		= Bean.getDecodeParam(parameters.get("bal_disc_per")),
	        	date_acc			= Bean.getDecodeParam(parameters.get("date_acc")),
	        	date_mov			= Bean.getDecodeParam(parameters.get("date_mov")),
		        date_calc			= Bean.getDecodeParam(parameters.get("date_calc")),
	    	    date_onl			= Bean.getDecodeParam(parameters.get("date_onl")),
	    	    date_onl_next		= Bean.getDecodeParam(parameters.get("date_onl_next"));
	
			pParam.add(card_serial_number);
			pParam.add(id_issuer);
			pParam.add(id_payment_system);
			pParam.add(vk_sys_key_card);
			pParam.add(nt_icc);
			pParam.add(id_card_status);
			pParam.add(id_bon_category);
			pParam.add(id_disc_category);
			pParam.add(bal_acc);
			pParam.add(bal_cur);
			pParam.add(bal_bon_per);
			pParam.add(bal_disc_per);
			pParam.add(date_acc);
			pParam.add(date_mov);
			pParam.add(date_calc);
			pParam.add(date_onl);
			pParam.add(date_onl_next);
			pParam.add(Bean.getDateFormat());
			
		 	%>
			<%= Bean.executeUpdateFunction("PACK$CARD_UI.update_bonus_appl", pParam, "../crm/cards/clubcardspecs.jsp?id=" + card_serial_number + "&iss=" + id_issuer + "&paysys=" + id_payment_system, "") %>
			<% 	
	
		} else { %> 
	    	<%= Bean.getUnknownActionText(action) %><% 
	    }
	} else {
	    %> <%= Bean.getUnknownProcessText(process) %> <%
	}
} else if (type.equalsIgnoreCase("errortrans")) {
	if (process.equalsIgnoreCase("yes")) {
		if (action.equalsIgnoreCase("check")) {
			ArrayList<String> pParam = new ArrayList<String>();

			pParam.add(card_serial_number);
			pParam.add(id_issuer);
			pParam.add(id_payment_system);
		  	     
		 	%>
			<%= Bean.executeUpdateFunction("PACK$CARD_UI.check_transactions", pParam, "../crm/cards/clubcardspecs.jsp?id=" + card_serial_number + "&iss=" + id_issuer + "&paysys=" + id_payment_system, "") %>
			<% 	
	    } else { %> 
	    	<%= Bean.getUnknownActionText(action) %><% 
	    }
	} else {
	    %> <%= Bean.getUnknownProcessText(process) %> <%
	}

} else if (type.equalsIgnoreCase("purse")) {
	if (process.equalsIgnoreCase("no")) {
		if (action.equalsIgnoreCase("edit")) {
    		
    		String id_purse 	= Bean.getDecodeParam(parameters.get("id_purse"));
    	
    		bcClubCardPurseObject purse = new bcClubCardPurseObject(id_purse, Bean.getLanguage(), Bean.getSessionId(), Bean.getDateFormat());
    		
    	%>
	<script>
		var formData = new Array (
			new Array ('value_card_purse', 'varchar2', 1)
		);

		function myValidateForm() {
			return validateForm(formData);
		}
	</script>

			<%= Bean.getOperationTitle(
					Bean.getClubCardXMLFieldTransl("h_purse_edit", false),
					"N",
					"N") 
			%>
    <form action="../crm/cards/clubcardupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">
        <input type="hidden" name="type" value="purse">
        <input type="hidden" name="action" value="edit">
        <input type="hidden" name="process" value="yes">
		<input type="hidden" name="id_purse" value="<%= id_purse %>">
		<input type="hidden" name="card_serial_number" value="<%= card_serial_number %>">
		<input type="hidden" name="id_issuer" value="<%= id_issuer %>">
		<input type="hidden" name="id_payment_system" value="<%= id_payment_system %>">
	<table <%=Bean.getTableDetailParam() %>>
		<tr>
			<td><%= Bean.getClubCardXMLFieldTransl("id_card_purse", false) %></td> <td><input type="text" name="id_card_purse" size="10" value="<%= purse.getValue("ID_CARD_PURSE") %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.getClubCardXMLFieldTransl("name_currency", false) %></td> <td><input type="text" name="name_currency" size="30" value="<%= purse.getValue("NAME_CURRENCY") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td><%= Bean.getClubCardXMLFieldTransl("number_card_purse", false) %></td> <td><input type="text" name="number_card_purse" size="10" value="<%= purse.getValue("NUMBER_CARD_PURSE") %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.getClubCardXMLFieldTransl("value_card_purse", true) %></td> <td><input type="text" name="value_card_purse" size="10" value="<%= purse.getValue("VALUE_CARD_PURSE_FRMT") %>"  class="inputfield"></td>
		</tr>
		<tr>
			<td><%= Bean.getClubCardXMLFieldTransl("name_card_purse_type", false) %></td> <td><input type="text" name="name_card_purse_type" size="30" value="<%= purse.getValue("NAME_CARD_PURSE_TYPE") %>" readonly="readonly" class="inputfield-ro"></td>
			<td colspan="2">&nbsp;</td>
		</tr>
		<%=	Bean.getCreationAndMoficationRecordFields(
				purse.getValue(Bean.getCreationDateFieldName()),
				purse.getValue("CREATED_BY"),
				purse.getValue(Bean.getLastUpdateDateFieldName()),
				purse.getValue("LAST_UPDATE_BY")
			) %>
 		<tr>
			<td colspan="4" align="center">
				<%=Bean.getSubmitButtonAjax("../crm/cards/clubcardupdate.jsp") %>
				<%=Bean.getResetButton() %>
				<%=Bean.getGoBackButton("../crm/cards/clubcardspecs.jsp?id="+card_serial_number+"&iss="+id_issuer+"&paysys="+id_payment_system) %>
			</td>
		</tr>
	</table>
	</form>

	   	<%} else {
	   	    %> <%= Bean.getUnknownActionText(action) %><%
	   	}
	} else if (process.equalsIgnoreCase("yes")) {
		String
			id_card_purse 			= Bean.getDecodeParam(parameters.get("id_card_purse")),
			value_card_purse 		= Bean.getDecodeParam(parameters.get("value_card_purse"));
	
		if (action.equalsIgnoreCase("edit")) { 
		
			ArrayList<String> pParam = new ArrayList<String>();
					
			pParam.add(id_card_purse);
			pParam.add(value_card_purse);
			
		 	%>
			<%= Bean.executeUpdateFunction("PACK$CARD_UI.update_card_purse", pParam, "../crm/cards/clubcardspecs.jsp?id="+card_serial_number+"&iss="+id_issuer+"&paysys="+id_payment_system, "") %>
			<% 	
			
	    } else { %> 
			<%= Bean.getUnknownActionText(action) %><% 
		}
	} else {
	    %> <%= Bean.getUnknownProcessText(process) %> <%
	}
} else if (type.equalsIgnoreCase("errortrans")) {
	if (process.equalsIgnoreCase("yes")) {
		if (action.equalsIgnoreCase("check")) {
			ArrayList<String> pParam = new ArrayList<String>();

			pParam.add(card_serial_number);
			pParam.add(id_issuer);
			pParam.add(id_payment_system);
		  	     
		 	%>
			<%= Bean.executeUpdateFunction("PACK$CARD_UI.check_transactions", pParam, "../crm/cards/clubcardspecs.jsp?id=" + card_serial_number + "&iss=" + id_issuer + "&paysys=" + id_payment_system, "") %>
			<% 	
	    } else { %> 
	    	<%= Bean.getUnknownActionText(action) %><% 
	    }
	} else {
	    %> <%= Bean.getUnknownProcessText(process) %> <%
	}

} else if (type.equalsIgnoreCase("rests")) {
	if (process.equalsIgnoreCase("yes")) {
		if (action.equalsIgnoreCase("run")) {
			ArrayList<String> pParam = new ArrayList<String>();

			pParam.add(card_serial_number);
			pParam.add(id_issuer);
			pParam.add(id_payment_system);
		  	     
		 	%>
			<%= Bean.executeUpdateFunction("PACK$CARD_UI.calc_bon_rests_initial", pParam, "../crm/cards/clubcardspecs.jsp?id=" + card_serial_number + "&iss=" + id_issuer + "&paysys=" + id_payment_system, "") %>
			<% 	
	    } else { %> 
	    	<%= Bean.getUnknownActionText(action) %><% 
	    }
	} else {
	    %> <%= Bean.getUnknownProcessText(process) %> <%
	}
} else if (type.equalsIgnoreCase("actions")) {
	
	if (process.equalsIgnoreCase("no"))	{
		
		String card_action 	= Bean.getDecodeParamPrepare(parameters.get("card_action"));
		String id_serv_place_where_card_sold 	= Bean.getDecodeParamPrepare(parameters.get("id_serv_place_where_card_sold"));
		String name_serv_place_where_card_sold = Bean.getServicePlaceShortName(id_serv_place_where_card_sold);
		String id_contact_prs = Bean.getDecodeParamPrepare(parameters.get("id_contact_prs"));
		String action_prv = Bean.getDecodeParamPrepare(parameters.get("action_prv"));

		bcClubShortObject club = new bcClubShortObject(Bean.getCurrentClubID());
		String errorMessage = "";
		
		if (action.equalsIgnoreCase("prepare")) {
    		bcClubCardObject clubcard = null;
    		if (Bean.isEmpty(card_serial_number)) {
    			clubcard = new bcClubCardObject(cd_card1);
    		} else {
    			clubcard = new bcClubCardObject(card_serial_number, id_issuer, id_payment_system);
    		}
    		if (clubcard.getResultSetRowCount() == 0) {
    			errorMessage = "Карту не найдено";
    			action = action_prv;
    		}
		}
		
	   	if (action.equalsIgnoreCase("choise") || 
	   			action.equalsIgnoreCase("choisegeneral") || 
	   			action.equalsIgnoreCase("choisecontact")) {
			
			bcClubCardObject clubcard = null;
			String id_role_current = "";
			if (!Bean.isEmpty(cd_card1)) {
				clubcard = new bcClubCardObject(card_serial_number, id_issuer, id_payment_system);
				id_role_current = clubcard.getValue("ID_NAT_PRS_ROLE_CURRENT");
			}
			System.out.println("card_serial_number="+card_serial_number);
		%> 
		<%= Bean.getOperationTitle(
			Bean.clubcardXML.getfieldTransl("title_card_actions", false),
			"Y",
			"N") 
		%>
		<script>
			var formGeneral = new Array (
			);
			var formData = new Array (
				new Array ('card_action', 'varchar2', 1)
			);
			var formCard = new Array (
				new Array ('cd_card1', 'varchar2', 1)
			);
			var formGiveCard = new Array (
				new Array ('name_serv_place_where_card_sold', 'varchar2', 1)
			);
			function myValidateForm() {
				formGeneral = formData;
				<% if (Bean.isEmpty(cd_card1)) { %>
				formGeneral = formGeneral.concat(formCard);
				<% } %>
				var card_action = document.getElementById('card_action_give');
				if (card_action.checked == true) {
					formGeneral = formGeneral.concat(formGiveCard);
				}
				return validateForm(formGeneral);
			}
		</script>
        <form action="../crm/cards/clubcardupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">
		    <input type="hidden" name="action" value="prepare">
		    <input type="hidden" name="process" value="no">
	    	<input type="hidden" name="type" value="actions">
	    	<input type="hidden" name="action_prv" value="<%=action %>">
	    	<input type="hidden" name="card_action" value="<%=card_action %>">
	    	<input type="hidden" name="id_serv_place_where_card_sold" value="<%=id_serv_place_where_card_sold %>">
	    	<input type="hidden" name="id_contact_prs" value="<%=id_contact_prs %>">

		<table <%=Bean.getTableDetailParam() %>>
			<% if (!Bean.isEmpty(cd_card1) && !Bean.isEmpty(card_serial_number) && Bean.isEmpty(errorMessage)) { %>
			<tr>
				<td><%= Bean.getClubCardXMLFieldTransl("cd_card1", false) %></td>
				<td>
					<input type="hidden" name="card_serial_number" value="<%= card_serial_number %>">
					<input type="hidden" name="id_issuer" value="<%= id_issuer %>">
					<input type="hidden" name="id_payment_system" value="<%= id_payment_system %>">
					<input type="text" name="cd_card1" size="40" value="<%= clubcard.getValue("CD_CARD1") %>" readonly="readonly" class="inputfield-ro">
				</td>
			</tr>
			<% } else { %>
			<tr>
				<td><%= Bean.getClubCardXMLFieldTransl("cd_card1", true) %></td>
			  	<td>
					<%=Bean.getWindowFindClubCard(cd_card1, "35", false) %>
					<% if (!Bean.isEmpty(errorMessage)) { %>
					<br><font color="red"><b><%=errorMessage %></b></font>
					<% } %>
		  		</td>
			</tr>
			<% } %>
			<tr>
				<td style="width: 200px;"><%= Bean.clubcardXML.getfieldTransl("card_action_kind", true) %></td> 
				<td>
					<% if (Bean.isEmpty(id_role_current)) { %>
					<%=Bean.getInputRadioElement("card_action", "Выдать карту", "give", "", "give".equalsIgnoreCase(card_action), false)%><br>
					<%=Bean.natprsXML.getfieldTransl("name_serv_plce_where_card_sold", true)%><br>
					<%=Bean.getWindowFindServicePlace("serv_place_where_card_sold", id_serv_place_where_card_sold, "", "35") %><br>

					<%=Bean.getInputRadioElement("card_action", "Изменить анкету (в процессе разработки)", "questionnaire", "", false, true)%><br>
					<%=Bean.getInputRadioElement("card_action", "Активировать (в процессе разработки)", "activate", "", false, true)%><br>
					<%=Bean.getInputRadioElement("card_action", "Отменить выдачу (в процессе разработки)", "reverse_give", "", false, true)%><br>
					<% } else { %>
					<%=Bean.getInputRadioElement("card_action", "Выдать карту", "give", "", false, true)%><br>
					<%=Bean.getInputRadioElement("card_action", "Изменить анкету (в процессе разработки)", "questionnaire", "", "questionnaire".equalsIgnoreCase(card_action), true)%><br>
					<%=Bean.getInputRadioElement("card_action", "Активировать (в процессе разработки)", "activate", "", "activate".equalsIgnoreCase(card_action), true)%><br>
					<%=Bean.getInputRadioElement("card_action", "Отменить выдачу (в процессе разработки)", "reverse_give", "", "reverse_give".equalsIgnoreCase(card_action), true)%><br>
					<% } %>
					<%=Bean.getInputRadioElement("card_action", "Блокировать (в процессе разработки)", "block", "", "block".equalsIgnoreCase(card_action), true)%><br>
					<%=Bean.getInputRadioElement("card_action", "Списать все средства в счет общества (в процессе разработки)", "write_off_means", "", "write_off_means".equalsIgnoreCase(card_action), true)%><br>
					<%=Bean.getInputRadioElement("card_action", "Выдать другому пайщику (в процессе разработки)", "give_another", "", "give_another".equalsIgnoreCase(card_action), true)%><br>
					<%=Bean.getInputRadioElement("card_action", "Изменить вид карты (в процессе разработки)", "change_card_status", "", "change_card_status".equalsIgnoreCase(card_action), true)%><br>
				</td>
			</tr>
			<tr>
				<td colspan="6" align="center">
					<%=Bean.getSubmitButtonAjax("../crm/cards/clubcardupdate.jsp") %>
					<%=Bean.getResetButton() %>
					<% if ("choisegeneral".equalsIgnoreCase(action)) { %>
						<%=Bean.getGoBackButton("../crm/cards/clubcards.jsp") %>
					<% } else if ("choisecontact".equalsIgnoreCase(action)) { %>
						<%=Bean.getGoBackButton("../crm/clients/contact_prsspecs.jsp?id="+id_contact_prs) %>
					<% } else { %>
						<% if (Bean.isEmpty(card_serial_number)) { %>
						<%=Bean.getGoBackButton("../crm/cards/clubcards.jsp") %>
						<% } else { %>
						<%=Bean.getGoBackButton("../crm/cards/clubcardspecs.jsp?id="+card_serial_number+"&iss="+id_issuer+"&paysys="+id_payment_system) %>
						<% } %>
					<% } %>
				</td>
			</tr>

		</table>

	</form>

		        <%
	    	} else if (action.equalsIgnoreCase("prepare")) {
	    		bcClubCardObject clubcard = null;
	    		if (Bean.isEmpty(card_serial_number)) {
	    			clubcard = new bcClubCardObject(cd_card1);
	    		} else {
	    			clubcard = new bcClubCardObject(card_serial_number, id_issuer, id_payment_system);
	    		}

	    		bcNatPrsRoleObject role1 = null;
	    		bcNatPrsRoleObject referral1 = null;
	    		boolean referralExist = false;
	    		if (!Bean.isEmpty(clubcard.getValue("ID_NAT_PRS_ROLE_CURRENT"))) {
	        		role1 = new bcNatPrsRoleObject(clubcard.getValue("ID_NAT_PRS_ROLE_CURRENT"));
	        		if (!Bean.isEmpty(role1.getValue("ID_REFERRAL_NAT_PRS_ROLE"))) {
	        			referral1 = new bcNatPrsRoleObject(role1.getValue("ID_REFERRAL_NAT_PRS_ROLE"));
	        			if (referral1.getResultSetRowCount()> 0) {
	        				referralExist = true ;
	        			}
	        		}
	    		}
	    		
	    		if (card_action.equalsIgnoreCase("give")) { 
	    		
	    		%>
				<%= Bean.getOperationTitle(
					Bean.clubcardXML.getfieldTransl("title_card_action_give", false),
					"Y",
					"Y") 
				%>
		<script language="JavaScript">
		function copyFactAddress(){
			document.getElementById('reg_adr_district').value = document.getElementById('fact_adr_district').value;
			document.getElementById('reg_adr_house').value = document.getElementById('fact_adr_house').value;
			document.getElementById('reg_adr_zip_code').value = document.getElementById('fact_adr_zip_code').value;
			document.getElementById('reg_adr_city').value = document.getElementById('fact_adr_city').value;
			document.getElementById('reg_adr_street').value = document.getElementById('fact_adr_street').value;
			document.getElementById('reg_adr_case').value = document.getElementById('fact_adr_case').value;
			document.getElementById('reg_adr_apartment').value = document.getElementById('fact_adr_apartment').value;

			var regCountries		= document.getElementById('reg_code_country');
			for(var counter=regCountries.childNodes.length-1;counter>=0;counter--){
				regCountries.removeChild(regCountries.childNodes[counter]);
			}

			var factCountries		= document.getElementById('fact_code_country');
			
			for(counter=0;counter<factCountries.childNodes.length;counter++){
				var option_element=document.createElement("option");
				var text_element=document.createTextNode(factCountries.childNodes[counter].text);
				option_element.value=factCountries.childNodes[counter].value;
				if(factCountries.childNodes[counter].selected==true){
					option_element.selected="selected";
				}
				option_element.appendChild(text_element);
				regCountries.appendChild(option_element);
			}

			var regOblast		= document.getElementById('reg_adr_oblast');
			for(var counter=regOblast.childNodes.length-1;counter>=0;counter--){
				regOblast.removeChild(regOblast.childNodes[counter]);
			}

			var factOblast		= document.getElementById('fact_adr_oblast');
			
			for(counter=0;counter<factOblast.childNodes.length;counter++){
				var option_element=document.createElement("option");
				var text_element=document.createTextNode(factOblast.childNodes[counter].text);
				option_element.value=factOblast.childNodes[counter].value;
				if(factOblast.childNodes[counter].selected==true){
					option_element.selected="selected";
				}
				option_element.appendChild(text_element);
				regOblast.appendChild(option_element);
			}
			
		}
	
		</script>
		<script>
			var formData = new Array (
				new Array ('code_country_give', 'varchar2', 1),
				new Array ('date_card_sale', 'varchar2', 1),
				new Array ('name_jur_prs_card_pack', 'varchar2', 1),
				new Array ('name_referral_nat_prs_role', 'varchar2', 1),
				new Array ('name_jur_prs_who_card_sold', 'varchar2', 1),
				new Array ('name_serv_plce_where_card_sold', 'varchar2', 1),
				new Array ('surname', 'varchar2', 1),
				new Array ('date_of_birth', 'varchar2', 1),
				new Array ('sex', 'varchar2', 1),
				new Array ('cd_nat_prs_role_state', 'varchar2', 1),
				new Array ('name_club', 'varchar2', 1),
				new Array ('club_date_beg', 'varchar2', 1),
				new Array ('cd_club_member_status', 'varchar2', 1)
			);
			function myValidateForm() {
				return validateForm(formData);
			}
			function setCheckValue(element) {
				if (element.checked) {
					element.value='Y';
				} else {
					element.value='N';
				}
			}
		</script>
		  <form action="../crm/cards/clubcardupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">
			<input type="hidden" name="action" value="give">
	    	<input type="hidden" name="process" value="yes">
	    	<input type="hidden" name="type" value="actions">
	    	<input type="hidden" name="card_serial_number" value="<%= card_serial_number %>">
	    	<input type="hidden" name="id_issuer" value="<%= id_issuer %>">
	    	<input type="hidden" name="id_payment_system" value="<%= id_payment_system %>">
		<table <%=Bean.getTableDetailParam() %>>

		<tr><td colspan="4"><b><%= Bean.natprsXML.getfieldTransl("title_card_sale", false) %></b></td></tr>
		<tr>
			<td><%= Bean.natprsXML.getfieldTransl("cd_card1", false) %>
				<%= Bean.getGoToClubCardLink(card_serial_number, id_issuer, id_payment_system) %>
			</td><td><input type="text" name="cd_card1" size="30" value="<%=clubcard.getValue("CD_CARD1") %>" readonly="readonly" class="inputfield-ro"></td>
			<td colspan="2">&nbsp;</td>			
		</tr>
		<tr>
			<td>&nbsp;</td>
			<td><%=Bean.natprsXML.getfieldTransl("title_card", false)%>:&nbsp;
				<%=Bean.getCheckBox("is_corporate_card", "", Bean.natprsXML.getfieldTransl("is_corporate_card", false)) %>
				<%=Bean.getCheckBox("is_temporary_card", "", Bean.natprsXML.getfieldTransl("is_temporary_card", false)) %>
			</td>			
			<td><%=Bean.natprsXML.getfieldTransl("id_jur_prs_who_card_sold", true)%></td>
			<td>
				<%=Bean.getWindowFindJurPrs("jur_prs_who_card_sold", "", "", "ALL", "35") %>
			</td>			
		</tr>
		<tr>
			<td><%= Bean.natprsXML.getfieldTransl("code_country_give", true) %></td><td><select name="code_country_give" id="code_country_give" class="inputfield"><%= Bean.getCountryOptions(club.getValue("CODE_COUNTRY_DEF"), true) %></select></td>
			<td><%=Bean.natprsXML.getfieldTransl("id_serv_plce_where_card_sold", true)%></td>
			<td>
				<input type="hidden" name="id_serv_place_where_card_sold" value="<%=id_serv_place_where_card_sold %>">
				<input type="text" name="name_serv_place_where_card_sold" size="45" value="<%=name_serv_place_where_card_sold %>" readonly="readonly" class="inputfield-ro">
			</td>
		</tr>
		<tr>
			<td><%=Bean.natprsXML.getfieldTransl("date_card_sale", true)%></td><td><%=Bean.getCalendarInputField("date_card_sale", "", "10") %></td>
			<td><%=Bean.natprsXML.getfieldTransl("id_term_who_card_sold", false)%></td>
			<td>
				<%=Bean.getWindowFindTerm("term_who_card_sold", "", id_serv_place_where_card_sold, "35") %>
			</td>
		</tr>
		<tr>
			<td><%=Bean.natprsXML.getfieldTransl("id_jur_prs_card_pack", true)%></td>
			<td>
				<%=Bean.getWindowCardPackage("jur_prs_card_pack", "", "", id_serv_place_where_card_sold, "35") %>
			</td>			
			<td><%=Bean.natprsXML.getfieldTransl("id_user_who_card_sold", false)%></td>
			<td>
				<%=Bean.getWindowFindUserBase("user_who_card_sold", "", "", id_serv_place_where_card_sold, "35") %>
			</td>			
		</tr>
		<tr>
			<td><%=Bean.natprsXML.getfieldTransl("cd_referral_card1", true)%></td>
			<td>
				<%=Bean.getWindowFindNatPrsRole("referral_nat_prs_role", "", "", "", "35") %>
			</td>			
			<td colspan="2">&nbsp;</td>
		</tr>

		<tr><td colspan="4" class="top_line"><b><%= Bean.natprsXML.getfieldTransl("title_nat_prs_param", false) %></b></td></tr>
		<tr>
			<td><%= Bean.natprsXML.getfieldTransl("tax_code", false) %></td><td><input type="text" name="tax_code" size="30" value="" class="inputfield"></td>
	 	    <td><%= Bean.clubXML.getfieldTransl("club", true) %>
				<%= Bean.getGoToClubLink(club.getValue("ID_CLUB")) %>
			</td>
		  	<td>
				<%=Bean.getWindowFindClub("club", club.getValue("ID_CLUB"), club.getValue("SNAME_CLUB"), "25") %>
		 		</td>
		</tr>
		<tr>
			<td><%= Bean.natprsXML.getfieldTransl("surname", true) %></td><td><input type="text" name="surname" size="30" value="" class="inputfield"></td>
			<td><%=Bean.natprsXML.getfieldTransl("club_date_beg", true)%></td> <td><%=Bean.getCalendarInputField("club_date_beg", "", "10") %></td>
		</tr>
		<tr>
			<td><%= Bean.natprsXML.getfieldTransl("name", false) %> </td><td><input type="text" name="name" size="30" value="" class="inputfield"></td>
			<td><%=Bean.natprsXML.getfieldTransl("club_date_end", false)%></td> <td><%=Bean.getCalendarInputField("club_date_end", "", "10") %></td>
		</tr>
		<tr>
			<td><%= Bean.natprsXML.getfieldTransl("patronymic", false) %></td><td><input type="text" name="patronymic" size="30" value="" class="inputfield"></td>
			<td><%= Bean.clubXML.getfieldTransl("club_member_status", true) %></td> <td><select name="cd_club_member_status" class="inputfield" > <%= Bean.getClubMemberStatusOptions("", true) %></select></td>
		</tr>
		<tr>
			<td><%= Bean.natprsXML.getfieldTransl("date_of_birth", true) %></td><td><%=Bean.getCalendarInputField("date_of_birth", "", "10") %></td>
			<td>&nbsp;</td>
			<td>
				<%=Bean.getCheckBox("is_organizer", "", Bean.natprsXML.getfieldTransl("is_organizer", false)) %>
				<%=Bean.getCheckBox("is_investor", "", Bean.natprsXML.getfieldTransl("is_investor", false)) %>
			</td>			
		</tr>
		<tr>
			<td><%= Bean.natprsXML.getfieldTransl("sex", true) %></td><td><select name="sex" class="inputfield"><%= Bean.getMeaningFromLookupNameOptions("MALE_FEMALE", "", true) %></select> </td>
			<td rowspan="4"><%=Bean.natprsXML.getfieldTransl("desc_nat_prs_role", false)%></td><td rowspan="4"><textarea name="desc_nat_prs_role" cols="40" rows="2" class="inputfield"></textarea></td>
		</tr>
		<tr>
			<td><%= Bean.natprsXML.getfieldTransl("cd_nat_prs_role_state", true) %></td> <td><select name="cd_nat_prs_role_state" class="inputfield" > <%= Bean.getNatPrsRoleStateOptions("QUESTIONED", true) %></select></td>
		</tr>
		<tr>
			<td><%= Bean.natprsXML.getfieldTransl("is_questionnaire_checked", false) %></td> <td><select name="is_questionnaire_checked" class="inputfield" > <%= Bean.getYesNoLookupOptions("N", true) %></select></td>
		</tr>
		<tr>
			<td><%= Bean.natprsXML.getfieldTransl("can_use_share_account", false) %></td> <td><select name="can_use_share_account" class="inputfield" > <%= Bean.getMeaningFromLookupNameOrderByNymberValueOptions("YES_NO_UNKNOWN", "N", true) %></select></td>
		</tr>
		<tr>
			<td colspan="4" class="top_line"><b><%= Bean.natprsXML.getfieldTransl("title_pasport", false) %></b></td>
		</tr>
		<tr>
			<td><%= Bean.natprsXML.getfieldTransl("pasport_code_country", false) %></td><td><select name="pasport_code_country" class="inputfield"><%= Bean.getCountryOptions(club.getValue("CODE_COUNTRY_DEF"), true) %></select></td>
			<td><%= Bean.natprsXML.getfieldTransl("pasport_date", false) %> </td><td><%=Bean.getCalendarInputField("pasport_date", "", "10") %></td>
		</tr>
		<tr>
			<td><%= Bean.natprsXML.getfieldTransl("pasport_series_number", false) %></td><td><input type="text" name="pasport_series" size="5" value="" class="inputfield"><input type="text" name="pasport_number" size="20" value="" class="inputfield"></td>
			<td rowspan="2"><%= Bean.natprsXML.getfieldTransl("pasport_text", false) %></td><td rowspan="2"><textarea name="pasport_text" cols="40" rows="2" class="inputfield"></textarea></td>
		</tr>
		<tr>
			<td><%= Bean.natprsXML.getfieldTransl("pasport_division_code", false) %></td><td><input type="text" name="pasport_division_code" size="30" value="" class="inputfield"></td>
		</tr>
		<tr>
			<td colspan="4" class="top_line"><b><%= Bean.natprsXML.getfieldTransl("h_contact_information", false) %></b></td>
		</tr>
		<tr>
			<td><%= Bean.natprsXML.getfieldTransl("phone_mobile", false) %></td><td><input type="text" name="phone_mobile" size="30" value="" class="inputfield"></td>
			<td><%= Bean.natprsXML.getfieldTransl("email", false) %> </td><td><input type="text" name="email" size="30" value="" class="inputfield"></td>
		</tr>
		<tr>
			<td><%= Bean.natprsXML.getfieldTransl("phone_home", false) %></td><td><input type="text" name="phone_home" size="30" value="" class="inputfield"></td>
			<td colspan="2">&nbsp;</td>
		</tr>
		<tr>
			<td colspan="4" class="top_line"><b><%= Bean.natprsXML.getfieldTransl("FACT_ADR_FULL", false) %></b></td>
		</tr>
			<tr>
				<td><%= Bean.natprsXML.getfieldTransl("code_country", false) %></td><td><select name="fact_code_country" id="fact_code_country" class="inputfield" onchange="dwr_oblast_array('fact_adr_oblast', this.value, document.getElementById('fact_adr_oblast').value, '<%=Bean.getSessionId() %>');"><option value=""></option><%= Bean.getCountryOptions(club.getValue("CODE_COUNTRY_DEF"), false) %></select></td>
				<td><%= Bean.natprsXML.getfieldTransl("fact_adr_city", false) %></td><td><input type="text" name="fact_adr_city" id="fact_adr_city" size="30" value="" class="inputfield"></td>
			</tr>
			<tr>
				<td><%= Bean.natprsXML.getfieldTransl("fact_adr_zip_code", false) %></td><td><input type="text" name="fact_adr_zip_code" id="fact_adr_zip_code" size="30" value="" class="inputfield"></td>
				<td><%= Bean.natprsXML.getfieldTransl("fact_adr_street", false) %> </td><td><input type="text" name="fact_adr_street" id="fact_adr_street" size="30" value="" class="inputfield"></td>
			</tr>
			<tr>
				<td><%= Bean.natprsXML.getfieldTransl("fact_adr_oblast", false) %></td><td><select name="fact_adr_oblast" id="fact_adr_oblast" class="inputfield"><option value=""></option><%= Bean.getOblastOptions(club.getValue("CODE_COUNTRY_DEF"), "", false) %></select></td>
				<td><%= Bean.natprsXML.getfieldTransl("fact_adr_house", false) %>/<%= Bean.natprsXML.getfieldTransl("fact_adr_case", false) %></td>
				<td>
					<input type="text" name="fact_adr_house" id="fact_adr_house" size="10" value="" class="inputfield">
					/&nbsp;<input type="text" name="fact_adr_case" id="fact_adr_case" size="10" value="" class="inputfield">
				</td>
			</tr>
			<tr>
				<td><%= Bean.natprsXML.getfieldTransl("fact_adr_district", false) %></td><td><input type="text" name="fact_adr_district" id="fact_adr_district" size="30" value="" class="inputfield"></td>
				<td><%= Bean.natprsXML.getfieldTransl("fact_adr_apartment", false) %></td>
				<td>
					<input type="text" name="fact_adr_apartment" id="fact_adr_apartment" size="30" value="" class="inputfield">
				</td>
			</tr>
			<tr>
				<td colspan="4" class="top_line"><b><%= Bean.natprsXML.getfieldTransl("REG_ADR_FULL", false) %></b>
				&nbsp;&nbsp;<button type="button" class="button" onclick="copyFactAddress(); "><%= Bean.natprsXML.getfieldTransl("button_copy_fact_address", false) %> </button>
				</td>
			</tr>
			<tr>
				<td><%= Bean.natprsXML.getfieldTransl("code_country", false) %></td><td><select name="reg_code_country" id="reg_code_country" class="inputfield" onchange="dwr_oblast_array('reg_adr_oblast', this.value, document.getElementById('reg_adr_oblast').value, '<%=Bean.getSessionId() %>');"><option value=""></option><%= Bean.getCountryOptions(club.getValue("CODE_COUNTRY_DEF"), false) %></select></td>
				<td><%= Bean.natprsXML.getfieldTransl("reg_adr_city", false) %> </td><td><input type="text" name="reg_adr_city" id="reg_adr_city" size="30" value="" class="inputfield"></td>
			</tr>
			<tr>
				<td><%= Bean.natprsXML.getfieldTransl("reg_adr_zip_code", false) %> </td><td><input type="text" name="reg_adr_zip_code" id="reg_adr_zip_code" size="30" value="" class="inputfield"></td>
				<td><%= Bean.natprsXML.getfieldTransl("reg_adr_street", false) %></td><td><input type="text" id="reg_adr_street" name="reg_adr_street" size="30" value="" class="inputfield"></td>
			</tr>
			<tr>
				<td><%= Bean.natprsXML.getfieldTransl("reg_adr_oblast", false) %></td><td><select name="reg_adr_oblast" id="reg_adr_oblast" class="inputfield"><option value=""></option><%= Bean.getOblastOptions(club.getValue("CODE_COUNTRY_DEF"), "", false) %></select></td>
				<td><%= Bean.natprsXML.getfieldTransl("reg_adr_house", false) %>/<%= Bean.natprsXML.getfieldTransl("reg_adr_case", false) %></td>
				<td>
					<input type="text" name="reg_adr_house" id="reg_adr_house" size="10" value="" class="inputfield">
					/&nbsp;<input type="text" name="reg_adr_case" id="reg_adr_case" size="10" value="" class="inputfield">
				</td>
			</tr>
			<tr>
				<td><%= Bean.natprsXML.getfieldTransl("reg_adr_district", false) %></td><td><input type="text" name="reg_adr_district" id="reg_adr_district" size="30" value="" class="inputfield"></td>
				<td><%= Bean.natprsXML.getfieldTransl("reg_adr_apartment", false) %></td>
				<td>
					<input type="text" name="reg_adr_apartment" id="reg_adr_apartment" size="30" value="" class="inputfield">
				</td>
			</tr>

		<tr><td colspan="4" class="top_line"><b><%= Bean.natprsXML.getfieldTransl("title_work_place", false) %></b></td></tr>
		<tr>
			<td><%=Bean.natprsXML.getfieldTransl("id_jur_prs", false)%></td>
			<td>
				<%=Bean.getWindowFindServicePlace("service_place_work", "", "", "35") %>
			</td>			
			<td><%= Bean.natprsXML.getfieldTransl("phone_work", false) %></td><td><input type="text" name="phone_work" size="30" value="" class="inputfield"></td>
		</tr>
		<tr>
			<td><%= Bean.contactXML.getfieldTransl("name_post", false) %></td> <td><select name="cd_post" class="inputfield" > <%= Bean.getContactPrsTypeOptions("", true) %></select></td>
			<td><%= Bean.natprsXML.getfieldTransl("email_work", false) %></td><td><input type="text" name="email_work" size="30" value="" class="inputfield"></td>
		</tr>
			<tr>
				<td colspan="6" align="center">
					<%=Bean.getSubmitButtonAjax("../crm/cards/clubcardupdate.jsp") %>
					<%=Bean.getResetButton() %>
					<%=Bean.getGoBackButton("../crm/cards/clubcardupdate.jsp?card_serial_number="+card_serial_number+"&id_issuer="+id_issuer+"&id_payment_system="+id_payment_system+"&type=actions&process=no&action=choise&card_action="+card_action+"&id_serv_place_where_card_sold="+id_serv_place_where_card_sold+"&cd_card1="+cd_card1+"&id_contact_prs="+id_contact_prs+"&action="+action_prv) %>
				</td>
			</tr>
		</table>
		</form>
		
		<%= Bean.getCalendarScript("date_card_sale", false) %>
		<%= Bean.getCalendarScript("date_of_birth", false) %>
		<%= Bean.getCalendarScript("club_date_beg", false) %>
		<%= Bean.getCalendarScript("club_date_end", false) %>
		<%= Bean.getCalendarScript("pasport_date", false) %>
	    			
	    		<% } else if (card_action.equalsIgnoreCase("activate")) { %>
				<%= Bean.getOperationTitle(
					Bean.clubcardXML.getfieldTransl("title_card_actions", false),
					"Y",
					"N") 
				%>
	    		
	    		<% } else if (card_action.equalsIgnoreCase("reverse_give")) { %>
				<%= Bean.getOperationTitle(
					Bean.clubcardXML.getfieldTransl("title_card_actions", false),
					"Y",
					"N") 
				%>
	    		
	    		<% } else if (card_action.equalsIgnoreCase("block")) { %>
				<%= Bean.getOperationTitle(
					Bean.clubcardXML.getfieldTransl("title_card_actions", false),
					"Y",
					"N") 
				%>
	    			
	    		<% }
	    	} else {
	    	    %> <%= Bean.getUnknownActionText(action) %><%
	    	}
	} else if (process.equalsIgnoreCase("yes"))	{
		if (action.equalsIgnoreCase("give")) { 
		    	
			String
				is_corporate_card				= Bean.getDecodeParam(parameters.get("is_corporate_card")),
				is_temporary_card				= Bean.getDecodeParam(parameters.get("is_temporary_card")),
				code_country_give				= Bean.getDecodeParam(parameters.get("code_country_give")),
				date_card_sale					= Bean.getDecodeParam(parameters.get("date_card_sale")),
				id_jur_prs_card_pack			= Bean.getDecodeParam(parameters.get("id_jur_prs_card_pack")),
				id_referral_nat_prs_role		= Bean.getDecodeParam(parameters.get("id_referral_nat_prs_role")),
				id_jur_prs_who_card_sold		= Bean.getDecodeParam(parameters.get("id_jur_prs_who_card_sold")),
				id_serv_place_where_card_sold	= Bean.getDecodeParam(parameters.get("id_serv_place_where_card_sold")),
				id_term_who_card_sold			= Bean.getDecodeParam(parameters.get("id_term_who_card_sold")),
				id_user_who_card_sold			= Bean.getDecodeParam(parameters.get("id_user_who_card_sold")),
				tax_code						= Bean.getDecodeParam(parameters.get("tax_code")),
		    	surname 						= Bean.getDecodeParam(parameters.get("surname")), 
		    	name 							= Bean.getDecodeParam(parameters.get("name")), 
		    	patronymic 						= Bean.getDecodeParam(parameters.get("patronymic")), 
		    	date_of_birth 					= Bean.getDecodeParam(parameters.get("date_of_birth")), 
		    	sex 							= Bean.getDecodeParam(parameters.get("sex")),
		    	cd_nat_prs_role_state			= Bean.getDecodeParam(parameters.get("cd_nat_prs_role_state")),
		    	is_questionnaire_checked		= Bean.getDecodeParam(parameters.get("is_questionnaire_checked")),
		    	can_use_share_account			= Bean.getDecodeParam(parameters.get("can_use_share_account")),
		    	id_club							= Bean.getDecodeParam(parameters.get("id_club")),
		    	club_date_beg					= Bean.getDecodeParam(parameters.get("club_date_beg")),
		    	club_date_end					= Bean.getDecodeParam(parameters.get("club_date_end")),
		    	cd_club_member_status			= Bean.getDecodeParam(parameters.get("cd_club_member_status")),
		    	is_organizer					= Bean.getDecodeParam(parameters.get("is_organizer")),
		    	is_investor						= Bean.getDecodeParam(parameters.get("is_investor")),
		    	desc_nat_prs_role				= Bean.getDecodeParam(parameters.get("desc_nat_prs_role")),
		    	reg_code_country 				= Bean.getDecodeParam(parameters.get("reg_code_country")),
		    	reg_adr_zip_code 				= Bean.getDecodeParam(parameters.get("reg_adr_zip_code")),
		    	reg_adr_oblast 					= Bean.getDecodeParam(parameters.get("reg_adr_oblast")),
		    	reg_adr_district 				= Bean.getDecodeParam(parameters.get("reg_adr_district")),
		    	reg_adr_city 					= Bean.getDecodeParam(parameters.get("reg_adr_city")),
		    	reg_adr_street 					= Bean.getDecodeParam(parameters.get("reg_adr_street")),
		    	reg_adr_house 					= Bean.getDecodeParam(parameters.get("reg_adr_house")),
		    	reg_adr_case 					= Bean.getDecodeParam(parameters.get("reg_adr_case")),
		    	reg_adr_apartment 				= Bean.getDecodeParam(parameters.get("reg_adr_apartment")),
		    	fact_code_country 				= Bean.getDecodeParam(parameters.get("fact_code_country")),
		    	fact_adr_zip_code 				= Bean.getDecodeParam(parameters.get("fact_adr_zip_code")),
		    	fact_adr_oblast 				= Bean.getDecodeParam(parameters.get("fact_adr_oblast")),
		    	fact_adr_district 				= Bean.getDecodeParam(parameters.get("fact_adr_district")),
		    	fact_adr_city 					= Bean.getDecodeParam(parameters.get("fact_adr_city")),
		    	fact_adr_street 				= Bean.getDecodeParam(parameters.get("fact_adr_street")),
		    	fact_adr_house 					= Bean.getDecodeParam(parameters.get("fact_adr_house")),
		    	fact_adr_case 					= Bean.getDecodeParam(parameters.get("fact_adr_case")),
		    	fact_adr_apartment 				= Bean.getDecodeParam(parameters.get("fact_adr_apartment")),
		    	pasport_code_country 			= Bean.getDecodeParam(parameters.get("pasport_code_country")),
		    	pasport_series 					= Bean.getDecodeParam(parameters.get("pasport_series")),
		    	pasport_number 					= Bean.getDecodeParam(parameters.get("pasport_number")),
		    	pasport_division_code			= Bean.getDecodeParam(parameters.get("pasport_division_code")),
		    	pasport_date 					= Bean.getDecodeParam(parameters.get("pasport_date")),
		    	pasport_text 					= Bean.getDecodeParam(parameters.get("pasport_text")),
		    	phone_home 						= Bean.getDecodeParam(parameters.get("phone_home")),
		    	phone_mobile 					= Bean.getDecodeParam(parameters.get("phone_mobile")),
		    	email 							= Bean.getDecodeParam(parameters.get("email")),
		    	id_service_place_work			= Bean.getDecodeParam(parameters.get("id_service_place_work")),
		    	cd_post							= Bean.getDecodeParam(parameters.get("cd_post")),
		    	phone_work						= Bean.getDecodeParam(parameters.get("phone_work")),
		    	email_work						= Bean.getDecodeParam(parameters.get("email_work"));
			
			ArrayList<String> pParam = new ArrayList<String>();
	
			pParam.add(card_serial_number);
			pParam.add(id_issuer);
			pParam.add(id_payment_system);
			pParam.add(is_corporate_card);
			pParam.add(is_temporary_card);
			pParam.add(code_country_give);
			pParam.add(date_card_sale);
			pParam.add(id_jur_prs_card_pack);
			pParam.add(id_referral_nat_prs_role);
			pParam.add(id_jur_prs_who_card_sold);
			pParam.add(id_serv_place_where_card_sold);
			pParam.add(id_term_who_card_sold);
			pParam.add(id_user_who_card_sold);
			pParam.add(tax_code);
			pParam.add(surname);
			pParam.add(name);
			pParam.add(patronymic);
			pParam.add(date_of_birth);
			pParam.add(sex);
			pParam.add(cd_nat_prs_role_state);
			pParam.add(is_questionnaire_checked);
			pParam.add(can_use_share_account);
			pParam.add(id_club);
			pParam.add(club_date_beg);
			pParam.add(club_date_end);
			pParam.add(cd_club_member_status);
			pParam.add(is_organizer);
			pParam.add(is_investor);
			pParam.add(desc_nat_prs_role);
			pParam.add(reg_code_country);
			pParam.add(reg_adr_zip_code);
			pParam.add(reg_adr_oblast);
			pParam.add(reg_adr_district);
			pParam.add(reg_adr_city);
			pParam.add(reg_adr_street);
			pParam.add(reg_adr_house);
			pParam.add(reg_adr_case);
			pParam.add(reg_adr_apartment);
			pParam.add(fact_code_country);
			pParam.add(fact_adr_zip_code);
			pParam.add(fact_adr_oblast);
			pParam.add(fact_adr_district);
			pParam.add(fact_adr_city);
			pParam.add(fact_adr_street);
			pParam.add(fact_adr_house);
			pParam.add(fact_adr_case);
			pParam.add(fact_adr_apartment);
			pParam.add(pasport_code_country);
			pParam.add(pasport_series);
			pParam.add(pasport_number);
			pParam.add(pasport_division_code);
			pParam.add(pasport_date);
			pParam.add(pasport_text);
			pParam.add(phone_home);
			pParam.add(phone_mobile);
			pParam.add(email);
			pParam.add(id_service_place_work);
			pParam.add(cd_post);
			pParam.add(phone_work);
			pParam.add(email_work);
			pParam.add(Bean.getDateFormat());
			
		 	%>
			<%= Bean.executeInsertFunction("PACK$CARD_UI.give_card", pParam, "../crm/cards/clubcardspecs.jsp?id=" + card_serial_number + "&iss=" + id_issuer + "&paysys=" + id_payment_system + "&id_nat_prs_role=", "") %>
			<% 	
	
		} else { %> 
			<%= Bean.getUnknownActionText(action) %><% 
		}
	} else {
	    %> <%= Bean.getUnknownProcessText(process) %> <%
	}
} else {
    %> <%= Bean.getUnknownTypeText(type) %> <%
}
%>


</body>
</html>
