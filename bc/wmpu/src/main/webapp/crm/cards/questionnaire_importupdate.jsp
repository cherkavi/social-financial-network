<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%= Bean.getLogOutScript(request) %>

<%@page import="java.util.Enumeration"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Set"%>
<%@page import="java.util.Iterator"%>
<%@page import="bc.objects.bcClubShortObject"%>
<%@page import="bc.objects.bcQuestionnaireObject"%>
<%@page import="bc.objects.bcPostingDetailObject"%>
<%@page import="bc.objects.bcPostingEditObject"%><html>
<head>
	<%= Bean.getMetaContent() %>
	<%= Bean.getBottomFrameCSS() %>
</head>

<%
HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String pageFormName = "CARDS_QUESTIONNAIRE_IMPORT";

Bean.setJspPageForTabName(pageFormName);

request.setCharacterEncoding("UTF-8");

String type		= Bean.getDecodeParam(parameters.get("type"));
String action		= Bean.getDecodeParam(parameters.get("action")); 
String process		= Bean.getDecodeParam(parameters.get("process"));
String id			= Bean.getDecodeParam(parameters.get("id"));

if (id==null || ("".equalsIgnoreCase(id))) id="empty";
if (type==null || ("".equalsIgnoreCase(type))) type="empty";
if (action==null || ("".equalsIgnoreCase(action))) action="empty";
if (process==null || ("".equalsIgnoreCase(process))) process="empty";


if (type.equalsIgnoreCase("general")) {
	if (process.equalsIgnoreCase("no")) {
	   /*  --- Добавити запис --- */
		if (action.equalsIgnoreCase("add") || action.equalsIgnoreCase("add2")) {
		
			bcClubShortObject club = new bcClubShortObject(Bean.getCurrentClubID());
	        
		        %>
	
	<body>
		<script type="text/javascript">
		function checkGroup(){
			var elementCD = document.getElementById('CD_NAT_PRS_GROUP');
			var elementOther = document.getElementById('NAME_OTHER_VARIANT_GROUP');				
			var elementOtherName = document.getElementById('otherName'); 		
			var elementOtherText = document.getElementById('other'); 
			if (elementCD.value != 'OTHER_VARIANT'){				
				elementOther.value = '';
				elementOther.style.visibility = "hidden";
				elementOtherName.style.visibility = "hidden";
				elementOtherText.style.visibility = "hidden";
			} else {
				elementOther.style.visibility = "visible";
				elementOtherName.style.visibility = "visible";
				elementOtherText.style.visibility = "visible";
			}
		}
		checkGroup();
		</script>
		<script>
			var formData = new Array (
				new Array ('name_club', 'varchar2', 1),
				new Array ('date_and_time_card_sale', 'varchar2', 1),
				new Array ('cd_quest_payment_method', 'varchar2', 1),
				new Array ('cd_card', 'varchar2', 1),
				new Array ('surname', 'varchar2', 1),
				new Array ('date_of_birth', 'varchar2', 1),
				new Array ('sex', 'varchar2', 1),
				new Array ('FACT_CODE_COUNTRY', 'varchar2', 1),
				new Array ('FACT_ADR_OBLAST', 'varchar2', 1)
			);
			function myValidateForm() {
				return validateForm(formData);
			}
			function changePackParam(id, title, question, answer){
				document.getElementById('id_quest_pack').value = id;
				document.getElementById('name_quest_pack').value = title;
				document.getElementById('name_quest_pack').className = "inputfield_modified";
			}
		</script>
	
			<%= Bean.getOperationTitle(
				Bean.questionnaireXML.getfieldTransl("h_add_questionnaire", false),
				"Y",
				"Y") 
			%>
			<form action="../crm/cards/questionnaire_importupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">
				<input type="hidden" name="action" value="add">
		    	<input type="hidden" name="process" value="yes">
		    	<input type="hidden" name="type" value="general">
			<table <%=Bean.getTableDetailParam() %>>
			<tr>
				<td><%= Bean.clubXML.getfieldTransl("club", true) %>
				<%= Bean.getGoToClubLink(club.getValue("ID_CLUB")) %>
			  	</td>
			  	<td  colspan="3">
					<%=Bean.getWindowFindClub("club", club.getValue("ID_CLUB"), club.getValue("SNAME_CLUB"), "25") %>
			  	</td>
			</tr>
			<tr>
				<td><%= Bean.questionnaireXML.getfieldTransl("file_name", false) %></td>
			    	<td  colspan="3"><input type="text" name="file_name" size="80" value="" class="inputfield"></td>
			</tr>

			<tr>
				<td colspan="4" class="top_line"><b><%= Bean.getClubCardXMLFieldTransl("h_sell_information", false) %></b></td>
			</tr>
			<tr>
				<td><%= Bean.questionnaireXML.getfieldTransl("pack", false) %></td>
				<td> 
					<%=Bean.getWindowFindQuestionnairePack("quest_pack", "", "16") %>
				</td>
				<td><%= Bean.questionnaireXML.getfieldTransl("cheque_number", false) %></td><td><input type="text" name="cheque_number" size="30" value="" class="inputfield"></td>
			</tr>
			<tr>
				<td><%= Bean.questionnaireXML.getfieldTransl("date_and_time_card_sale", true) %></td><td><%=Bean.getCalendarInputField("date_and_time_card_sale", "", "20") %></td>
				<td><%= Bean.questionnaireXML.getfieldTransl("promoter_code", false) %></td><td><input type="text" name="promoter_code" size="30" value="" class="inputfield"></td>
			</tr>
			<tr>
				<td><%= Bean.questionnaireXML.getfieldTransl("cd_quest_payment_method", true)  %></td> <td><select name="cd_quest_payment_method" id="cd_quest_payment_method" class="inputfield"><%=Bean.getQuestionnairePaymentMethodOptions("CASH", false) %></select></td>
				<td><%= Bean.questionnaireXML.getfieldTransl("validity_percent", false) %></td><td><input type="text" name="validity_percent" size="30" value="" class="inputfield"></td>
			</tr>

			<tr>
				<td colspan="4" class="top_line" style='text-align:center; font-size: 14px'><b><font color="green"><%= Bean.questionnaireXML.getfieldTransl("h_questionnaire_data", false) %></font></b></td>
			</tr>
			<tr>
				<td colspan="4"><b><%= Bean.questionnaireXML.getfieldTransl("h_bon_card", false) %></b></td>
			</tr>
			<tr>
				<td><%= Bean.questionnaireXML.getfieldTransl("cd_card", true) %></td><td><input type="text" name="cd_card" size="30" value="" class="inputfield"></td>
				<td><%= Bean.questionnaireXML.getfieldTransl("number_bon_category_in_quest", false) %></td><td><input type="text" name="number_bon_category" size="30" value="" class="inputfield"></td>
			</tr>
			<tr>
				<td>&nbsp;</td><td>&nbsp;</td>
				<td><%= Bean.questionnaireXML.getfieldTransl("number_disc_category_in_quest", false) %></td><td><input type="text" name="number_disc_category" size="30" value="" class="inputfield"></td>
			</tr>
			<tr>
				<td colspan="4" class="top_line"><b><%= Bean.questionnaireXML.getfieldTransl("h_discount_card", false) %></b></td>
			</tr>
			<tr>
				<td><%= Bean.questionnaireXML.getfieldTransl("discount_card_number", false) %></td><td><input type="text" name="discount_card_number" size="30" value="" class="inputfield"></td>
				<td><%= Bean.questionnaireXML.getfieldTransl("is_discount_card_changed", false) %></td><td><select name="is_discount_card_changed" id="is_discount_card_changed" class="inputfield"><%= Bean.getYesNoLookupOptions("N",true) %></select></td>
			</tr>
			<tr>
				<td><%= Bean.questionnaireXML.getfieldTransl("discount_card_percent", false) %></td><td><input type="text" name="discount_card_percent" size="30" value="" class="inputfield"></td>
				<td><%= Bean.questionnaireXML.getfieldTransl("is_furchet_discount_card", false) %></td><td><select name="is_furchet_discount_card" id="is_furchet_discount_card" class="inputfield"><%= Bean.getYesNoLookupOptions("N",true) %></select></td>
			</tr>
			<tr>
				<td colspan="4" class="top_line"><b><%= Bean.questionnaireXML.getfieldTransl("h_client_info", false) %></b></td>
			</tr>
			<tr>
				<td><%= Bean.questionnaireXML.getfieldTransl("tax_code", false) %></td><td><input type="text" name="tax_code" size="30" value="" class="inputfield"></td>
				<td><%= Bean.questionnaireXML.getfieldTransl("pasport_code_country", false) %></td><td><input type="text" name="pasport_code_country" size="30" value="" class="inputfield"></td>
			</tr>
			<tr>
				<td><%= Bean.questionnaireXML.getfieldTransl("surname", true) %></td><td><input type="text" name="surname" size="30" value="" class="inputfield"></td>
				<td><%= Bean.questionnaireXML.getfieldTransl("pasport_number", false) %></td><td><input type="text" name="pasport_number" size="30" value="" class="inputfield"></td>
			</tr>
			<tr>
				<td><%= Bean.questionnaireXML.getfieldTransl("name", false) %> </td><td><input type="text" name="name" size="30" value="" class="inputfield"></td>
				<td><%= Bean.questionnaireXML.getfieldTransl("pasport_date", false) %> </td><td><%=Bean.getCalendarInputField("pasport_date", "", "10") %></td>
			</tr>
			<tr>
				<td><%= Bean.questionnaireXML.getfieldTransl("patronymic", false) %></td><td><input type="text" name="patronymic" size="30" value="" class="inputfield"></td>
				<td><%= Bean.questionnaireXML.getfieldTransl("pasport_text", false) %></td><td><input type="text" name="pasport_text" size="30" value="" class="inputfield"></td>
			</tr>
			<tr>
				<td><%= Bean.questionnaireXML.getfieldTransl("date_of_birth", true) %></td><td><%=Bean.getCalendarInputField("date_of_birth", "", "10") %></td>
				<td class="top_line"><%= Bean.questionnaireXML.getfieldTransl("surname_eng", false) %> </td><td class="top_line"><input type="text" name="surname_eng" size="30" value="" class="inputfield"></td>
			</tr>
			<tr>
				<td><%= Bean.questionnaireXML.getfieldTransl("sex", true) %></td><td><select name="sex" class="inputfield"><%= Bean.getMeaningFromLookupNameOptions("MALE_FEMALE", "M", false) %></select> </td>
				<td><%= Bean.questionnaireXML.getfieldTransl("name_eng", false) %></td><td><input type="text" name="name_eng" size="30" value="" class="inputfield"></td>
			</tr>
			<tr>
				<td valign=top><%= Bean.questionnaireXML.getfieldTransl("CD_NAT_PRS_GROUP", false) %></td><td><select name="CD_NAT_PRS_GROUP" id="CD_NAT_PRS_GROUP" class="inputfield" onchange="checkGroup()"><%= Bean.getNatPrsGroupsListOptions("",true) %></select></td>			
				<td id='otherName' class="top_line"><%= Bean.questionnaireXML.getfieldTransl("NAME_OTHER_VARIANT_GROUP", false) %></td><td id="other" class="top_line"><input type="text" name="NAME_OTHER_VARIANT_GROUP" id="NAME_OTHER_VARIANT_GROUP" size="30" value=""  class="inputfield"></td>
			</tr>

			<tr>
				<td colspan="4" class="top_line"><b><%= Bean.natprsXML.getfieldTransl("h_contact_information", false) %></b></td>
			</tr>
			<tr>
				<td><%= Bean.questionnaireXML.getfieldTransl("phone_work", false) %> </td><td><input type="text" name="phone_work" size="30" value="" class="inputfield"></td>
				<td><%= Bean.questionnaireXML.getfieldTransl("phone_mobile", false) %></td><td><input type="text" name="phone_mobile" size="30" value="" class="inputfield"></td>
			</tr>
			<tr>
				<td><%= Bean.questionnaireXML.getfieldTransl("phone_home", false) %></td><td><input type="text" name="phone_home" size="30" value="" class="inputfield"></td>
				<td><%= Bean.questionnaireXML.getfieldTransl("email", false) %> </td><td><input type="text" name="email" size="30" value="" class="inputfield"></td>
			</tr>

			<tr>
				<td colspan="4" class="top_line"><b><%= Bean.natprsXML.getfieldTransl("FACT_ADR_FULL", false) %></b></td>
			</tr>
			<tr>
				<td><%= Bean.questionnaireXML.getfieldTransl("code_country", true) %></td><td><select name="FACT_CODE_COUNTRY" id="FACT_CODE_COUNTRY" class="inputfield" onchange="dwr_oblast_array('FACT_ADR_OBLAST', this.value, document.getElementById('FACT_ADR_OBLAST').value, '<%=Bean.getSessionId() %>');"><option value=""></option><%= Bean.getCountryOptions(club.getValue("CODE_COUNTRY_DEF"), false) %></select></td>
				<td><%= Bean.questionnaireXML.getfieldTransl("fact_adr_city", false) %></td><td><input type="text" name="FACT_ADR_CITY" size="30" value="" class="inputfield"></td>
			</tr>
			<tr>
				<td><%= Bean.questionnaireXML.getfieldTransl("fact_adr_zip_code", false) %></td><td><input type="text" name="FACT_ADR_ZIP_CODE" size="30" value="" class="inputfield"></td>
				<td><%= Bean.questionnaireXML.getfieldTransl("fact_adr_street", false) %> </td><td><input type="text" name="FACT_ADR_STREET" size="30" value="" class="inputfield"></td>
			</tr>
			<tr>
				<td><%= Bean.questionnaireXML.getfieldTransl("fact_adr_oblast", true) %></td><td><select name="FACT_ADR_OBLAST" id="FACT_ADR_OBLAST" class="inputfield"><%= Bean.getOblastOptions(club.getValue("CODE_COUNTRY_DEF"), "", true) %></select></td>
				<td><%= Bean.questionnaireXML.getfieldTransl("fact_adr_house", false) %>/<%= Bean.questionnaireXML.getfieldTransl("fact_adr_case", false) %></td>
				<td>
					<input type="text" name="FACT_ADR_HOUSE" size="10" value="" class="inputfield">
					/&nbsp;<input type="text" name="FACT_ADR_CASE" size="10" value="" class="inputfield">
			    </td>
			</tr>
			<tr>
				<td><%= Bean.questionnaireXML.getfieldTransl("fact_adr_district", false) %></td><td><input type="text" name="FACT_ADR_DISTRICT" size="30" value="" class="inputfield"></td>
				<td><%= Bean.questionnaireXML.getfieldTransl("fact_adr_apartment", false) %></td>
				<td>
					<input type="text" name="FACT_ADR_APARTMENT" size="10" value="" class="inputfield">
			    </td>
			</tr>

			<tr>
				<td colspan="4" class="top_line"><b><%= Bean.natprsXML.getfieldTransl("REG_ADR_FULL", false) %></b></td>
			</tr>
			<tr>
				<td><%= Bean.questionnaireXML.getfieldTransl("code_country", false) %></td><td><select name="REG_CODE_COUNTRY" id="REG_CODE_COUNTRY" class="inputfield" onchange="dwr_oblast_array('REG_ADR_OBLAST', this.value, document.getElementById('REG_ADR_OBLAST').value, '<%=Bean.getSessionId() %>');"><option value=""></option><%= Bean.getCountryOptions(club.getValue("CODE_COUNTRY_DEF"), false) %></select></td>
				<td><%= Bean.questionnaireXML.getfieldTransl("reg_adr_city", false) %> </td><td><input type="text" name="REG_ADR_CITY" size="30" value="" class="inputfield"></td>
			</tr>
			<tr>
				<td><%= Bean.questionnaireXML.getfieldTransl("reg_adr_zip_code", false) %> </td><td><input type="text" name="REG_ADR_ZIP_CODE" size="30" value="" class="inputfield"></td>
				<td><%= Bean.questionnaireXML.getfieldTransl("reg_adr_street", false) %></td><td><input type="text" name="REG_ADR_STREET" size="30" value="" class="inputfield"></td>
			</tr>
			<tr>
				<td><%= Bean.questionnaireXML.getfieldTransl("reg_adr_oblast", false) %></td><td><select name="REG_ADR_OBLAST" id="REG_ADR_OBLAST" class="inputfield"><%= Bean.getOblastOptions(club.getValue("CODE_COUNTRY_DEF"), "", true) %></select></td>
				<td><%= Bean.questionnaireXML.getfieldTransl("reg_adr_house", false) %>/<%= Bean.questionnaireXML.getfieldTransl("reg_adr_case", false) %></td>
				<td>
					<input type="text" name="REG_ADR_HOUSE" size="10" value="" class="inputfield">
					/&nbsp;<input type="text" name="REG_ADR_CASE" size="10" value="" class="inputfield">
				</td>
			</tr>
			<tr>
				<td><%= Bean.questionnaireXML.getfieldTransl("reg_adr_district", false) %></td><td><input type="text" name="REG_ADR_DISTRICT" size="30" value="" class="inputfield"></td>
				<td><%= Bean.questionnaireXML.getfieldTransl("reg_adr_apartment", false) %></td>
				<td>
					<input type="text" name="REG_ADR_APARTMENT" size="10" value="" class="inputfield">
				</td>
			</tr>

			<tr>
				<td colspan="4" class="top_line"><b><%= Bean.natprsXML.getfieldTransl("additional_info", false) %></b></td>
			</tr>
			<tr>
				<td valign=top><%= Bean.questionnaireXML.getfieldTransl("cd_purchase_frequence", false) %></td><td><select name="cd_purchase_frequence" id="cd_purchase_frequence" class="inputfield" onchange="checkGroup()"><%= Bean.getMeaningFromLookupNameOptions("NAT_PRS_PURCHASE_FREQUENCE","",true) %></select></td>			
				<td valign=top><%= Bean.questionnaireXML.getfieldTransl("has_auto", false) %></td><td><select name="has_auto" id="has_auto" class="inputfield"><%= Bean.getMeaningFromLookupNumberOptions("YES_NO","N",true) %></select></td>			
			</tr>
			<tr>
				<td valign=top colspan="4" class="top_line"><b><%= Bean.questionnaireXML.getfieldTransl("shop_advantage", false) %></b></td>
			</tr>
			<tr>
				<td valign=top>&nbsp;</td>
				<td>
					<input type="checkbox" name="shop_advantage1" id="shop_advantage1" size="30" value="N" class="inputfield"><label class="checbox_label" for="shop_advantage1"><%= Bean.questionnaireXML.getfieldTransl("shop_advantage1", false) %></label>
				</td>
				<td>
					<input type="checkbox" name="shop_advantage2" id="shop_advantage2" size="30" value="N" class="inputfield"><label class="checbox_label" for="shop_advantage2"><%= Bean.questionnaireXML.getfieldTransl("shop_advantage2", false) %></label>
				</td>
				<td>
					<input type="checkbox" name="shop_advantage5" id="shop_advantage5" size="20" value="N" class="inputfield"><label class="checbox_label" for="shop_advantage5"><%= Bean.questionnaireXML.getfieldTransl("shop_advantage5", false) %></label>
				</td>
			</tr>
			<tr>
				<td valign=top>&nbsp;</td>
				<td>
					<input type="checkbox" name="shop_advantage3" id="shop_advantage3" size="20" value="N" class="inputfield"><label class="checbox_label" for="shop_advantage3"><%= Bean.questionnaireXML.getfieldTransl("shop_advantage3", false) %></label>
				</td>
				<td>
					<input type="checkbox" name="shop_advantage4" id="shop_advantage4" size="20" value="N" class="inputfield"><label class="checbox_label" for="shop_advantage4"><%= Bean.questionnaireXML.getfieldTransl("shop_advantage4", false) %></label>
				</td>
			</tr>
			<tr>
				<td valign=top colspan="4" class="top_line"><b><%= Bean.questionnaireXML.getfieldTransl("reception_information_way", false) %></b></td>
			</tr>
			<tr>
				<td valign=top>&nbsp;</td>
				<td>
					<input type="checkbox" name="reception_information_way1" id="reception_information_way1" size="30" value="phone" class="inputfield"><label class="checbox_label" for="reception_information_way1"><%= Bean.questionnaireXML.getfieldTransl("reception_information_way1", false) %></label>
				</td>
				<td>
					<input type="checkbox" name="reception_information_way2" id="reception_information_way2" size="30" value="e-mail" class="inputfield"><label class="checbox_label" for="reception_information_way2"><%= Bean.questionnaireXML.getfieldTransl("reception_information_way2", false) %></label>
				</td>
			</tr>
			<tr>
				<td valign=top>&nbsp;</td>
				<td>
					<input type="checkbox" name="reception_information_way3" id="reception_information_way3" size="20" value="N" class="inputfield"><label class="checbox_label" for="reception_information_way3"><%= Bean.questionnaireXML.getfieldTransl("reception_information_way3", false) %></label>
				</td>
				<td>
					<input type="checkbox" name="reception_information_way4" id="reception_information_way4" size="20" value="N" class="inputfield"><label class="checbox_label" for="reception_information_way4"><%= Bean.questionnaireXML.getfieldTransl("reception_information_way4", false) %></label>
				</td>
			</tr>
			<tr>
				<td valign=top colspan="4" class="top_line"><b><%= Bean.questionnaireXML.getfieldTransl("h_rules", false) %></b></td>
			</tr>
			<tr>
				<td valign=top>&nbsp;</td>
				<td>
					<input type="checkbox" name="rule_bon" id="rule_bon" size="30" value="bon" class="inputfield"><label class="checbox_label" for="rule_bon"><%= Bean.questionnaireXML.getfieldTransl("rule_bon", false) %></label>
				</td>
				<td>
					<input type="checkbox" name="rule_discount_ground" id="rule_discount_ground" size="30" value="discount_ground" class="inputfield"><label class="checbox_label" for="rule_discount_ground"><%= Bean.questionnaireXML.getfieldTransl("rule_discount_ground", false) %></label>
				</td>
			</tr>
			<tr>
				<td valign=top>&nbsp;</td>
				<td>
					<input type="checkbox" name="rule_superbon" id="rule_superbon" size="20" value="superbon" class="inputfield"><label class="checbox_label" for="rule_superbon"><%= Bean.questionnaireXML.getfieldTransl("rule_superbon", false) %></label>
				</td>
				<td>
					&nbsp;
				</td>
			</tr>

			<tr>
				<td colspan="4" align="center">
					<%=Bean.getSubmitButtonAjax("../crm/cards/questionnaire_importupdate.jsp") %>
					<%=Bean.getResetButton() %>
				<% if (action.equalsIgnoreCase("add2")) { %>
					<%=Bean.getGoBackButton("../crm/cards/questionnaire_importspecs.jsp?id="+id) %>
				<% } else { %>
					<%=Bean.getGoBackButton("../crm/cards/questionnaire_import.jsp") %>
				<% } %>
				</td>
			</tr>
		</table>
		</form>
		<!-- Скрипт для втавки меню вибору дати -->
		<%= Bean.getCalendarScript("date_and_time_card_sale", true) %>
		<%= Bean.getCalendarScript("date_of_birth", false) %>
		<%= Bean.getCalendarScript("pasport_date", false) %>
	
		        <%
		} else {
		%> <%= Bean.getUnknownActionText(action) %><%
		}
	} else if (process.equalsIgnoreCase("yes")) {
	
		String
			cd_card						= Bean.getDecodeParam(parameters.get("cd_card")),
			number_bon_category_in_quest	= Bean.getDecodeParam(parameters.get("number_bon_category_in_quest")),
			number_disc_category_in_quest	= Bean.getDecodeParam(parameters.get("number_disc_category_in_quest")),
			card_serial_number			= Bean.getDecodeParam(parameters.get("card_serial_number")),
			id_issuer					= Bean.getDecodeParam(parameters.get("id_issuer")),
			id_payment_system			= Bean.getDecodeParam(parameters.get("id_payment_system")),
	    	id_nat_prs					= Bean.getDecodeParam(parameters.get("id_nat_prs")),
	    	surname						= Bean.getDecodeParam(parameters.get("surname")),
	    	name						= Bean.getDecodeParam(parameters.get("name")),
	    	patronymic					= Bean.getDecodeParam(parameters.get("patronymic")),
	   		sex							= Bean.getDecodeParam(parameters.get("sex")),
	    	date_of_birth				= Bean.getDecodeParam(parameters.get("date_of_birth")),
	    	tax_code					= Bean.getDecodeParam(parameters.get("tax_code")),
	    	pasport_code_country		= Bean.getDecodeParam(parameters.get("pasport_code_country")),
	    	pasport_date				= Bean.getDecodeParam(parameters.get("pasport_date")),
	    	pasport_text				= Bean.getDecodeParam(parameters.get("pasport_text")),
	    	pasport_number				= Bean.getDecodeParam(parameters.get("pasport_number")),
	    	phone_work					= Bean.getDecodeParam(parameters.get("phone_work")),
	    	phone_home					= Bean.getDecodeParam(parameters.get("phone_home")),
	    	phone_mobile				= Bean.getDecodeParam(parameters.get("phone_mobile")),
	    	email						= Bean.getDecodeParam(parameters.get("email")),
	    	fact_code_country			= Bean.getDecodeParam(parameters.get("FACT_CODE_COUNTRY")),
	    	fact_adr_zip_code			= Bean.getDecodeParam(parameters.get("FACT_ADR_ZIP_CODE")),
	    	fact_adr_oblast				= Bean.getDecodeParam(parameters.get("FACT_ADR_OBLAST")),
	    	fact_adr_district			= Bean.getDecodeParam(parameters.get("FACT_ADR_DISTRICT")),
	    	fact_adr_city				= Bean.getDecodeParam(parameters.get("FACT_ADR_CITY")),
	    	fact_adr_street				= Bean.getDecodeParam(parameters.get("FACT_ADR_STREET")),
	    	fact_adr_house				= Bean.getDecodeParam(parameters.get("FACT_ADR_HOUSE")),
	    	fact_adr_case				= Bean.getDecodeParam(parameters.get("FACT_ADR_CASE")),
	    	fact_adr_apartment			= Bean.getDecodeParam(parameters.get("FACT_ADR_APARTMENT")),
	    	reg_code_country			= Bean.getDecodeParam(parameters.get("REG_CODE_COUNTRY")),
	    	reg_adr_zip_code			= Bean.getDecodeParam(parameters.get("REG_ADR_ZIP_CODE")),
	    	reg_adr_oblast				= Bean.getDecodeParam(parameters.get("REG_ADR_OBLAST")),
	    	reg_adr_district			= Bean.getDecodeParam(parameters.get("REG_ADR_DISTRICT")),
	    	reg_adr_city				= Bean.getDecodeParam(parameters.get("REG_ADR_CITY")),
	    	reg_adr_street				= Bean.getDecodeParam(parameters.get("REG_ADR_STREET")),
	    	reg_adr_house				= Bean.getDecodeParam(parameters.get("REG_ADR_HOUSE")),
	    	reg_adr_case				= Bean.getDecodeParam(parameters.get("REG_ADR_CASE")),
	    	reg_adr_apartment			= Bean.getDecodeParam(parameters.get("REG_ADR_APARTMENT")),
	    	surname_eng					= Bean.getDecodeParam(parameters.get("surname_eng")),
	    	name_eng					= Bean.getDecodeParam(parameters.get("name_eng")),
	    	id_club						= Bean.getDecodeParam(parameters.get("id_club")),
	    	reception_information_way1	= Bean.getDecodeParam(parameters.get("reception_information_way1")),
	    	reception_information_way2	= Bean.getDecodeParam(parameters.get("reception_information_way2")),
	    	reception_information_way3	= Bean.getDecodeParam(parameters.get("reception_information_way3")),
	    	reception_information_way4	= Bean.getDecodeParam(parameters.get("reception_information_way4")),
	    	reception_information_way5	= Bean.getDecodeParam(parameters.get("reception_information_way5")),
	    	reception_information_way6	= Bean.getDecodeParam(parameters.get("reception_information_way6")),
	    	reception_information_way7	= Bean.getDecodeParam(parameters.get("reception_information_way7")),
	    	reception_information_way8	= Bean.getDecodeParam(parameters.get("reception_information_way8")),
	    	reception_information_way9	= Bean.getDecodeParam(parameters.get("reception_information_way9")),
	    	reception_information_way10	= Bean.getDecodeParam(parameters.get("reception_information_way10")),
	    	name_nat_prs_group			= Bean.getDecodeParam(parameters.get("CD_NAT_PRS_GROUP")),
	    	name_other_variant_group	= Bean.getDecodeParam(parameters.get("name_other_variant_group")),
	    	shop_advantage1				= Bean.getDecodeParam(parameters.get("shop_advantage1")),
	    	shop_advantage2				= Bean.getDecodeParam(parameters.get("shop_advantage2")),
	    	shop_advantage3				= Bean.getDecodeParam(parameters.get("shop_advantage3")),
	    	shop_advantage4				= Bean.getDecodeParam(parameters.get("shop_advantage4")),
	    	shop_advantage5				= Bean.getDecodeParam(parameters.get("shop_advantage5")),
	    	shop_advantage6				= Bean.getDecodeParam(parameters.get("shop_advantage6")),
	    	shop_advantage7				= Bean.getDecodeParam(parameters.get("shop_advantage7")),
	    	shop_advantage8				= Bean.getDecodeParam(parameters.get("shop_advantage8")),
	    	shop_advantage9				= Bean.getDecodeParam(parameters.get("shop_advantage9")),
	    	shop_advantage10			= Bean.getDecodeParam(parameters.get("shop_advantage10")),
	    	file_name					= Bean.getDecodeParam(parameters.get("file_name")),
	    	validity_percent			= Bean.getDecodeParam(parameters.get("validity_percent")),
			id_jur_prs_who_has_sold_card	= Bean.getDecodeParam(parameters.get("id_jur_prs_who_has_sold_card")),
			date_card_sale				= Bean.getDecodeParam(parameters.get("date_card_sale")),
			time_card_sale				= Bean.getDecodeParam(parameters.get("time_card_sale")),
			date_and_time_card_sale		= Bean.getDecodeParam(parameters.get("date_and_time_card_sale")),
			id_jur_prs_where_card_sold	= Bean.getDecodeParam(parameters.get("id_jur_prs_where_card_sold")),
			id_serv_place_where_card_sold	= Bean.getDecodeParam(parameters.get("id_service_place")),
			id_quest_pack						= Bean.getDecodeParam(parameters.get("id_quest_pack")),
			cd_purchase_frequence		= Bean.getDecodeParam(parameters.get("cd_purchase_frequence")),
			has_auto					= Bean.getDecodeParam(parameters.get("has_auto")),
			id_card_status				= Bean.getDecodeParam(parameters.get("id_card_status")),
			id_bon_category				= Bean.getDecodeParam(parameters.get("id_bon_category")),
			id_disc_category			= Bean.getDecodeParam(parameters.get("id_disc_category")),
			discount_card_number		= Bean.getDecodeParam(parameters.get("discount_card_number")),
			discount_card_percent		= Bean.getDecodeParam(parameters.get("discount_card_percent")),
			cd_quest_payment_method		= Bean.getDecodeParam(parameters.get("cd_quest_payment_method")),
			is_discount_card_changed	= Bean.getDecodeParam(parameters.get("is_discount_card_changed")),
			is_furchet_discount_card	= Bean.getDecodeParam(parameters.get("is_furchet_discount_card")),
			cheque_number				= Bean.getDecodeParam(parameters.get("cheque_number")),
			promoter_code				= Bean.getDecodeParam(parameters.get("promoter_code")),
			rule_bon					= Bean.getDecodeParam(parameters.get("rule_bon")),
			rule_superbon				= Bean.getDecodeParam(parameters.get("rule_superbon")),
			rule_discount_ground		= Bean.getDecodeParam(parameters.get("rule_discount_ground"));
		
	
	    if (action.equalsIgnoreCase("add")) {  
	 
			String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_QUEST.add_questionnaire(" +
			   "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?," +
		       "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";

			String[] pParam = new String [77];
							
			pParam[0] = cd_card;
			pParam[1] = number_bon_category_in_quest;
			pParam[2] = number_disc_category_in_quest;
			pParam[3] = discount_card_number;
			pParam[4] = discount_card_percent;
			pParam[5] = is_discount_card_changed;
			pParam[6] = is_furchet_discount_card;
			pParam[7] = surname;
			pParam[8] = name;
			pParam[9] = patronymic;
			pParam[10] = sex;
			pParam[11] = date_of_birth;
			pParam[12] = tax_code;
			pParam[13] = pasport_code_country;
			pParam[14] = pasport_date;
			pParam[15] = pasport_text;
			pParam[16] = pasport_number;
			pParam[17] = phone_work;
			pParam[18] = phone_home;
			pParam[19] = phone_mobile;
			pParam[20] = email;
			pParam[21] = fact_code_country;
			pParam[22] = fact_adr_zip_code;
			pParam[23] = fact_adr_oblast;
			pParam[24] = fact_adr_district;
			pParam[25] = fact_adr_city;
			pParam[26] = fact_adr_street;
			pParam[27] = fact_adr_house;
			pParam[28] = fact_adr_case;
			pParam[29] = fact_adr_apartment;
			pParam[30] = reg_code_country;
			pParam[31] = reg_adr_zip_code;
			pParam[32] = reg_adr_oblast;
			pParam[33] = reg_adr_district;
			pParam[34] = reg_adr_city;
			pParam[35] = reg_adr_street;
			pParam[36] = reg_adr_house;
			pParam[37] = reg_adr_case;
			pParam[38] = reg_adr_apartment;
			pParam[39] = surname_eng;
			pParam[40] = name_eng;
			pParam[41] = reception_information_way1;
			pParam[42] = reception_information_way2;
			pParam[43] = reception_information_way3;
			pParam[44] = reception_information_way4;
			pParam[45] = reception_information_way5;
			pParam[46] = reception_information_way6;
			pParam[47] = reception_information_way7;
			pParam[48] = reception_information_way8;
			pParam[49] = reception_information_way9;
			pParam[50] = reception_information_way10;
			pParam[51] = name_nat_prs_group;
			pParam[52] = name_other_variant_group;
			pParam[53] = shop_advantage1;
			pParam[54] = shop_advantage2;
			pParam[55] = shop_advantage3;
			pParam[56] = shop_advantage4;
			pParam[57] = shop_advantage5;
			pParam[58] = shop_advantage6;
			pParam[59] = shop_advantage7;
			pParam[60] = shop_advantage8;
			pParam[61] = shop_advantage9;
			pParam[62] = shop_advantage10;
			pParam[63] = cd_purchase_frequence;
			pParam[64] = has_auto;
			pParam[65] = file_name;
			pParam[66] = validity_percent;
			pParam[67] = date_and_time_card_sale;
			pParam[68] = id_quest_pack;
			pParam[69] = cd_quest_payment_method;
			pParam[70] = cheque_number;
			pParam[71] = promoter_code;
			pParam[72] = rule_bon;
			pParam[73] = rule_superbon;
			pParam[74] = rule_discount_ground;
			pParam[75] = id_club;
			pParam[76] = Bean.getDateFormat();
			
		%>
			<%= Bean.getCallResultParam("INSERT", callSQL, pParam, "../crm/cards/questionnaire_importspecs.jsp?type=QUEST&id_profile=NEW&id=" , "../crm/cards/questionnaire_import.jsp?type=QUEST") %>
			<% 	
	
	    } else if (action.equalsIgnoreCase("edit")) { 
	 
			String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_QUEST.update_questionnaire(" + 
			"?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?," +
		       "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";

			String[] pParam = new String [77];
								
			pParam[0] = id;
			pParam[1] = cd_card;
			pParam[2] = number_bon_category_in_quest;
			pParam[3] = number_disc_category_in_quest;
			pParam[4] = discount_card_number;
			pParam[5] = discount_card_percent;
			pParam[6] = is_discount_card_changed;
			pParam[7] = is_furchet_discount_card;
			pParam[8] = surname;
			pParam[9] = name;
			pParam[10] = patronymic;
			pParam[11] = sex;
			pParam[12] = date_of_birth;
			pParam[13] = tax_code;
			pParam[14] = pasport_code_country;
			pParam[15] = pasport_date;
			pParam[16] = pasport_text;
			pParam[17] = pasport_number;
			pParam[18] = phone_work;
			pParam[19] = phone_home;
			pParam[20] = phone_mobile;
			pParam[21] = email;
			pParam[22] = fact_code_country;
			pParam[23] = fact_adr_zip_code;
			pParam[24] = fact_adr_oblast;
			pParam[25] = fact_adr_district;
			pParam[26] = fact_adr_city;
			pParam[27] = fact_adr_street;
			pParam[28] = fact_adr_house;
			pParam[29] = fact_adr_case;
			pParam[30] = fact_adr_apartment;
			pParam[31] = reg_code_country;
			pParam[32] = reg_adr_zip_code;
			pParam[33] = reg_adr_oblast;
			pParam[34] = reg_adr_district;
			pParam[35] = reg_adr_city;
			pParam[36] = reg_adr_street;
			pParam[37] = reg_adr_house;
			pParam[38] = reg_adr_case;
			pParam[39] = reg_adr_apartment;
			pParam[40] = surname_eng;
			pParam[41] = name_eng;
			pParam[42] = reception_information_way1;
			pParam[43] = reception_information_way2;
			pParam[44] = reception_information_way3;
			pParam[45] = reception_information_way4;
			pParam[46] = reception_information_way5;
			pParam[47] = reception_information_way6;
			pParam[48] = reception_information_way7;
			pParam[49] = reception_information_way8;
			pParam[50] = reception_information_way9;
			pParam[51] = reception_information_way10;
			pParam[52] = name_nat_prs_group;
			pParam[53] = name_other_variant_group;
			pParam[54] = shop_advantage1;
			pParam[55] = shop_advantage2;
			pParam[56] = shop_advantage3;
			pParam[57] = shop_advantage4;
			pParam[58] = shop_advantage5;
			pParam[59] = shop_advantage6;
			pParam[60] = shop_advantage7;
			pParam[61] = shop_advantage8;
			pParam[62] = shop_advantage9;
			pParam[63] = shop_advantage10;
			pParam[64] = cd_purchase_frequence;
			pParam[65] = has_auto;
			pParam[66] = file_name;
			pParam[67] = validity_percent;
			pParam[68] = date_and_time_card_sale;
			pParam[69] = id_quest_pack;
			pParam[70] = cd_quest_payment_method;
			pParam[71] = cheque_number;
			pParam[72] = promoter_code;
			pParam[73] = rule_bon;
			pParam[74] = rule_superbon;
			pParam[75] = rule_discount_ground;
			pParam[76] = Bean.getDateFormat();
			
			%>
			<%= Bean.getCallResultParam("UPDATE", callSQL, pParam, "../crm/cards/questionnaire_importspecs.jsp?type=QUEST&id=" + id, "") %>
			<% 	
	     
	    } else if (action.equalsIgnoreCase("edit_imported")) { 
	   	 
			String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_QUEST.update_imported_questionnaire(" + 
				"?,?,?,?,?,?,?)}";

			String[] pParam = new String [6];
					
			pParam[0] = id;
			pParam[1] = discount_card_number;
			pParam[2] = discount_card_percent;
			pParam[3] = is_discount_card_changed;
			pParam[4] = is_furchet_discount_card;
			pParam[5] = promoter_code;
			
			%>
			<%= Bean.getCallResultParam("UPDATE", callSQL, pParam, "../crm/cards/questionnaire_importspecs.jsp?type=QUEST&id=" + id, "") %>
			<% 	
	     
	    } else if (action.equalsIgnoreCase("remove")) {  
	 	
	 		String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_QUEST.delete_questionnaire(?,?)}";

			String[] pParam = new String [1];
					
			pParam[0] = id;
	
			%>
			<%= Bean.getCallResultParam("DELETE", callSQL, pParam, "../crm/cards/questionnaire_import.jsp?type=QUEST" , "") %>
			<% 	
	  	
	    } else if (action.equalsIgnoreCase("import")) {  
		
			String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_QUEST.import_questionnaire(?,?)}";

			String[] pParam = new String [1];
					
			pParam[0] = id;
	
			%>
			<%= Bean.getCallResultParam("UPDATE", callSQL, pParam, "../crm/cards/questionnaire_importspecs.jsp?type=QUEST&id=" + id , "") %>
			<% 	
		
	    } else if (action.equalsIgnoreCase("set_change")) { %> 
	     
			<%= Bean.form_messageXML.getfieldTransl("processing_update", false) %>
		   	<% 
	 
	 		ArrayList<String> id_value=new ArrayList<String>();
			ArrayList<String> prv_value=new ArrayList<String>();
	
	 		String callSQL = "";
	 		Set<String> keySet = parameters.keySet();
			Iterator<String> keySetIterator = keySet.iterator();
			String key = "";
			String[] results = new String[2];
	    	while(keySetIterator.hasNext()) {
				try{
					key = (String)keySetIterator.next();
					if(key.contains("chb_id")){
						id_value.add(key.substring(7));
					}
					if(key.contains("prv_id")){
						prv_value.add(key.substring(7));
					}
				}
				catch(Exception ex){
					Bean.writeException(
	   						"../crm/cards/questionnaire_importupdate.jsp",
	   						"",
	   						process,
	   						action,
	   						Bean.commonXML.getfieldTransl("h_get_param_value_error", false)+key+": " + ex.toString());
				}
			}
			
		    String resultInt = "";
		    String resultFull = "0";
		    String resultMessage = "";
		    String resultMessageFull = "";
	
		    if (id_value.size()>0) {
		 		 for(int counter=0;counter<id_value.size();counter++){ 
		 			 if (!(prv_value.contains(id_value.get(counter)))) {
		 				 
			        	callSQL = "{? = call " + Bean.getGeneralDBScheme() + 
			        		".PACK_UI_QUEST.set_discount_card_changed(?,?,?)}";

			    		String[] pParam = new String [2];
			    					
			    		pParam[0] = id_value.get(counter);
				    	pParam[1] = "Y";
			        	
			        	%>
			        	<%= Bean.showCallSQL(callSQL) %>
			        	<%
			        		
						results = Bean.myCallFunctionParam(callSQL, pParam, 2);
						resultInt = results[0];
						resultMessage = results[1];
					
						if (!("0".equalsIgnoreCase(resultInt))) {
							resultFull = resultInt;
							resultMessageFull = resultMessageFull + "; " +resultMessage;
						}
					}
		 		}
			}
		 	if (prv_value.size()>0) {
		 		for(int counter=0;counter<prv_value.size();counter++){ 
				 	if (!(id_value.contains(prv_value.get(counter)))) {
				   	 				 
			        	callSQL = "{? = call " + Bean.getGeneralDBScheme() + 
			        		".PACK_UI_QUEST.set_discount_card_changed(?,?,?)}";

				    	String[] pParam = new String [2];
				    					
				    	pParam[0] = prv_value.get(counter);
				    	pParam[1] = "N";
						
			        	%>
			        	<%= Bean.showCallSQL(callSQL) %>
			        	<%
			        		
			        	results = Bean.myCallFunctionParam(callSQL, pParam, 2);
						resultInt = results[0];
						resultMessage = results[1];
					
						if (!("0".equalsIgnoreCase(resultInt))) {
							resultFull = resultInt;
							resultMessageFull = resultMessageFull + "; " +resultMessage;
						}
				 	}
		 		 }
		 		
		 	}
			
			%>
	  	    <%=Bean.showCallResult(
	   	    		callSQL, 
	   	    		resultFull, 
	   	    		resultMessageFull, 
	   	    		"../crm/cards/questionnaire_import.jsp", 
	   	    		"../crm/cards/questionnaire_import.jsp", 
	   	    		Bean.form_messageXML.getfieldTransl("save_error", false)) %>
	   		<% 
	
	    } else { %> 
	    	<%= Bean.getUnknownActionText(action) %><% 
	    }
	} else {
	    %> <%= Bean.getUnknownProcessText(process) %> <%
	}


} else if (type.equalsIgnoreCase("posting")) {%>
	<script>
		var formAll = new Array();
		var formData = new Array (
			new Array ('operation_date', 'varchar2', 1),
			new Array ('debet_name_bk_account', 'varchar2', 1),
			new Array ('cd_currency', 'varchar2', 1),
			new Array ('credit_name_bk_account', 'varchar2', 1),
			new Array ('entered_amount', 'varchar2', 1),
			new Array ('run_postings_export', 'varchar2', 1),
			new Array ('using_in_clearing', 'varchar2', 1)
		);
		var formDataClearing = new Array (
			new Array ('name_bank_account_debet', 'varchar2', 1),
			new Array ('name_bank_account_credit', 'varchar2', 1)
		);
		var formDataExport = new Array (
			new Array ('cd_bk_doc_type', 'varchar2', 1)
		);
		
		function myValidateForm() {
			var using = document.getElementById('using_in_clearing').value;
			var run_exp = document.getElementById('run_postings_export').value;
		
			formAll = formData;
			if (using == 'Y') {
				formAll = formAll.concat(formDataClearing);
			}
			if (run_exp == 'Y') {
				formAll = formAll.concat(formDataExport);
			}
			return validateForm(formAll);
		}
		
		function checkClearing(){
			var using = document.getElementById('using_in_clearing').value;
			if (using == 'Y') {
				document.getElementById('span_receiver_id_bank_account').innerHTML='<%= Bean.postingXML.getfieldTransl("receiver_id_bank_account", true) %>';
				document.getElementById('span_payer_id_bank_account').innerHTML='<%= Bean.postingXML.getfieldTransl("payer_id_bank_account", true) %>';
			} else {
				document.getElementById('span_receiver_id_bank_account').innerHTML='<%= Bean.postingXML.getfieldTransl("receiver_id_bank_account", false) %>';
				document.getElementById('span_payer_id_bank_account').innerHTML='<%= Bean.postingXML.getfieldTransl("payer_id_bank_account", false) %>';
			}
		}
		
		function checkBKDocType(){
			var run_exp = document.getElementById('run_postings_export').value;
			if (run_exp == 'Y') {
				document.getElementById('span_cd_bk_doc_type').innerHTML='<%= Bean.posting_schemeXML.getfieldTransl("cd_bk_doc_type", true) %>';
			} else {
				document.getElementById('span_cd_bk_doc_type').innerHTML='<%= Bean.posting_schemeXML.getfieldTransl("cd_bk_doc_type", false) %>';
			}
		}
		checkClearing();
		checkBKDocType();
		
		function changeRelationShipParam(id, name){
			document.getElementById('id_club_rel').value = id;
			document.getElementById('name_club_rel').value = name;
			document.getElementById('name_club_rel').className = "inputfield_modified";
		}
	</script>
	
	<%
	String
		id_posting_detail	= Bean.getDecodeParam(parameters.get("id_posting_detail"));
	
	if (process.equalsIgnoreCase("no")) {
		if (action.equalsIgnoreCase("add")) { 
		
			bcQuestionnaireObject quest = new bcQuestionnaireObject(id);
			quest.getFeature();
			
			bcPostingEditObject posting = new bcPostingEditObject();
	        
	        bcClubShortObject club = new bcClubShortObject(quest.getValue("ID_CLUB"));
			
		%>
	
		<%= Bean.getOperationTitle(
			Bean.postingXML.getfieldTransl("h_postings_add", false),
			"Y",
			"Y") 
		%>
		<form action="../crm/cards/questionnaire_importupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">
	        <input type="hidden" name="type" value="posting">
	        <input type="hidden" name="action" value="add">
	        <input type="hidden" name="process" value="yes">
	        <input type="hidden" name="id" value="<%=id %>">
	    	<input type="hidden" name="id_club" value="<%=quest.getValue("ID_CLUB") %>">
		<table <%=Bean.getTableDetailParam() %>>
	
			<%=posting.getPostingAddHTML(club.getValue("CD_CURRENCY_BASE"), Bean.getDateFormatTitle()) %>
	
			<tr>
				<td colspan="6" align="center">
					<%=Bean.getSubmitButtonAjax("../crm/cards/questionnaire_importupdate.jsp") %>
					<%=Bean.getResetButton() %>
					<%=Bean.getGoBackButton("../crm/cards/questionnaire_importspecs.jsp?type=QUEST&id_profile=IMPORTED&id=" + id) %>
				</td>
			</tr>
		</table>
		</form>	
		<%= Bean.getCalendarScript("operation_date", false) %>
		<%= Bean.getCalendarScript("conversion_date", false) %>
	
		<% } else if (action.equalsIgnoreCase("edit")) { 
		
			bcPostingEditObject posting = new bcPostingEditObject();
			
		%>
	
		<%= Bean.getOperationTitle(
			Bean.postingXML.getfieldTransl("h_postings_edit", false),
			"Y",
			"Y") 
		%>
		<form action="../crm/cards/questionnaire_importupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">
	        <input type="hidden" name="type" value="posting">
	        <input type="hidden" name="action" value="edit">
	        <input type="hidden" name="process" value="yes">
	        <input type="hidden" name="id" value="<%=id %>">
		<table <%=Bean.getTableDetailParam() %>>
	
			<%=posting.getPostingEditHTML(id_posting_detail, Bean.getDateFormatTitle()) %>
	
			<tr>
				<td colspan="6" align="center">
					<%=Bean.getSubmitButtonAjax("../crm/cards/questionnaire_importupdate.jsp") %>
					<%=Bean.getResetButton() %>
					<%=Bean.getGoBackButton("../crm/cards/questionnaire_importspecs.jsp?type=QUEST&id_profile=IMPORTED&id=" + id) %>
				</td>
			</tr>
		</table>
		</form>	
		<%= Bean.getCalendarScript("operation_date", false) %>
		<%= Bean.getCalendarScript("conversion_date", false) %>
	
		<% } else { %> 
			<%= Bean.getUnknownActionText(action) %><% 
		}
	} else if (process.equalsIgnoreCase("yes")) {
		String
			operation_date				= Bean.getDecodeParam(parameters.get("operation_date")),
			debet_id_bk_account			= Bean.getDecodeParam(parameters.get("debet_id_bk_account")),
			cd_currency					= Bean.getDecodeParam(parameters.get("cd_currency")),
			credit_id_bk_account		= Bean.getDecodeParam(parameters.get("credit_id_bk_account")),
			entered_amount				= Bean.getDecodeParam(parameters.get("entered_amount")),
			assignment_posting			= Bean.getDecodeParam(parameters.get("assignment_posting")),
			base_currency				= Bean.getDecodeParam(parameters.get("base_currency")),
			exchange_rate				= Bean.getDecodeParam(parameters.get("exchange_rate")),
			id_bk_operation_scheme_line	= Bean.getDecodeParam(parameters.get("id_bk_operation_scheme_line")),
			conversion_date				= Bean.getDecodeParam(parameters.get("conversion_date")),
			id_club_rel					= Bean.getDecodeParam(parameters.get("id_club_rel")),
			accounted_amount			= Bean.getDecodeParam(parameters.get("accounted_amount")),
			using_in_clearing			= Bean.getDecodeParam(parameters.get("using_in_clearing")),
			run_postings_export			= Bean.getDecodeParam(parameters.get("run_postings_export")),
			cd_bk_doc_type				= Bean.getDecodeParam(parameters.get("cd_bk_doc_type")),
			id_bank_account_debet		= Bean.getDecodeParam(parameters.get("id_bank_account_debet")),
			id_bank_account_credit		= Bean.getDecodeParam(parameters.get("id_bank_account_credit")),
			payment_function			= Bean.getDecodeParam(parameters.get("payment_function")),
			id_club						= Bean.getDecodeParam(parameters.get("id_club"));
		
		if (action.equalsIgnoreCase("run")) { %>
		
	       <%
	       String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_QUEST.run_posting(?,?,?,?)}";

		   String[] pParam = new String [2];
		     			
		   pParam[0] = id_club;
		   pParam[1] = id;
	        
	    %>
			<%= Bean.getCallResultParam("RUN", callSQL, pParam, "../crm/cards/questionnaire_importspecs.jsp?type=QUEST&id_profile=IMPORTED&id=" + id+ "&id_report=", "") %>
		<% 	 	
		} else if (action.equalsIgnoreCase("add")) { 
		       
	       	String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_QUEST.add_posting("+
	       		"?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";

	       	String[] pParam = new String [21];
	       				
	       	pParam[0] = id;
	       	pParam[1] = debet_id_bk_account;
	       	pParam[2] = credit_id_bk_account;
	       	pParam[3] = cd_currency;
	       	pParam[4] = operation_date;
	       	pParam[5] = entered_amount;
	       	pParam[6] = base_currency;
	       	pParam[7] = exchange_rate;
	       	pParam[8] = conversion_date;
	       	pParam[9] = accounted_amount;
	       	pParam[10] = assignment_posting;
	       	pParam[11] = id_bk_operation_scheme_line;
	       	pParam[12] = id_club_rel;
	       	pParam[13] = using_in_clearing;
	       	pParam[14] = run_postings_export;
	       	pParam[15] = cd_bk_doc_type;
	       	pParam[16] = id_bank_account_debet;
	       	pParam[17] = id_bank_account_credit;
	       	pParam[18] = payment_function;
	       	pParam[19] = id_club;
	       	pParam[20] = Bean.getDateFormat();
	
		 	%>
			<%= Bean.getCallResultParam("INSERT", callSQL, pParam, "../crm/cards/questionnaire_importspecs.jsp?type=QUEST&id_profile=IMPORTED&id=" + id + "&id_posting_detail=", "") %>
			<% 	
			
		} else if (action.equalsIgnoreCase("edit")) { 
		       
	       	String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_QUEST.update_posting("+
	       		"?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";

	    	 String[] pParam = new String [20];
		       				
		     pParam[0] = id_posting_detail;
		     pParam[1] = debet_id_bk_account;
		     pParam[2] = credit_id_bk_account;
		     pParam[3] = cd_currency;
		     pParam[4] = operation_date;
		     pParam[5] = entered_amount;
		     pParam[6] = base_currency;
		     pParam[7] = exchange_rate;
		     pParam[8] = conversion_date;
		     pParam[9] = accounted_amount;
		     pParam[10] = assignment_posting;
		     pParam[11] = id_bk_operation_scheme_line;
		     pParam[12] = id_club_rel;
		     pParam[13] = using_in_clearing;
		     pParam[14] = run_postings_export;
		     pParam[15] = cd_bk_doc_type;
		     pParam[16] = id_bank_account_debet;
		     pParam[17] = id_bank_account_credit;
		     pParam[18] = payment_function;
		     pParam[19] = Bean.getDateFormat();
	
		 	%>
			<%= Bean.getCallResultParam("UPDATE", callSQL, pParam, "../crm/cards/questionnaire_importspecs.jsp?type=QUEST&id_profile=IMPORTED&id=" + id, "") %>
			<% 	
			
		} else if (action.equalsIgnoreCase("remove")) { 
		       
	       	String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_QUEST.delete_posting(?,?)}";

			String[] pParam = new String [1];
							
			pParam[0] = id;
	
		 	%>
			<%= Bean.getCallResultParam("DELETE", callSQL, pParam, "../crm/cards/questionnaire_importspecs.jsp?type=QUEST&id_profile=IMPORTED&id=" + id, "") %>
			<% 	
			
		} else if (action.equalsIgnoreCase("deleteall")) { 
	       
	       	String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_QUEST.delete_all_quest_posting(?,?)}";

			String[] pParam = new String [1];
							
			pParam[0] = id;
	
		 	%>
			<%= Bean.getCallResultParam("DELETE", callSQL, pParam, "../crm/cards/questionnaire_importspecs.jsp?type=QUEST&id_profile=IMPORTED&id=" + id, "") %>
			<% 	
			
		} else { %> 
			<%= Bean.getUnknownActionText(action) %><% 
		}
	} else {%> 
		<%= Bean.getUnknownProcessText(process) %> <%
	}
} else {
	%> <%= Bean.getUnknownTypeText(type) %> <%
}
%>


</body>
</html>
