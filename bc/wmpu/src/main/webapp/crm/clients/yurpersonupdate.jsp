<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%@page import="bc.objects.bcJurPrsObject"%>
<%@page import="bc.objects.bcClubShortObject"%>
<%@page import="bc.objects.bcContactsObject"%>
<%@page import="bc.objects.bcDocumentObject"%>
<%@page import="bc.objects.bcBankAccountObject"%>
<%@page import="bc.objects.bcNatPrsObject"%>
<%@page import="bc.objects.bcClubRelationshipObject"%>
<%@page import="java.util.HashMap"%>
<%@page import="bc.objects.bcTerminalDeviceTypeObject"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Set"%>
<%@page import="java.util.Iterator"%>
<%@page import="bc.objects.bcJurPrsNomenklatureObject"%><html>

<%= Bean.getLogOutScript(request) %>

<head>
	<%= Bean.getMetaContent() %>
	<%= Bean.getBottomFrameCSS() %>
	
</head>

<%
HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String pageFormName = "CLIENTS_YURPERSONS";

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
  if (process.equalsIgnoreCase("no"))
/* вибираємо тип дії (добавити, видалити...)*/
	{
	   /*  --- Добавити запис --- */
    	if (action.equalsIgnoreCase("add") || action.equalsIgnoreCase("add2")) {
    		
    		bcClubShortObject club = new bcClubShortObject(Bean.getCurrentClubID());
    		
    		%>
	<body>

		<script language="JavaScript">
			var formGeneral = new Array (
				new Array ('cd_jur_prs_kind', 'varchar2', 1),
				new Array ('SNAME_JUR_PRS', 'varchar2', 1),
				new Array ('NAME_JUR_PRS', 'varchar2', 1),
				new Array ('reg_code_country', 'varchar2', 1),
				new Array ('tax_percent', 'varchar2', 1),
				new Array ('cd_club_member_status', 'varchar2', 1),
				new Array ('club_date_beg', 'varchar2', 1)
			);
			
			function myValidateForm() {
				return validateForm(formGeneral);
			}
			
			checkJurPrsShareholder();
		</script>
    		
			<%= Bean.getOperationTitle(
					Bean.jurpersonXML.getfieldTransl("LAB_ADD_JURPRS", false),
					"Y",
					"Y") 
			%>
        <form action="../crm/clients/yurpersonupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return myValidateForm()">
	        <input type="hidden" name="type" value="general">
	        <input type="hidden" name="action" value="add">
	        <input type="hidden" name="process" value="yes">
		<table <%=Bean.getTableDetailParam() %>>
		<tr>
			<td>
				<%= Bean.jurpersonXML.getfieldTransl("jur_prs_kind", false) %></td><td><select name="cd_jur_prs_kind" id="cd_jur_prs_kind" class="inputfield"><%= Bean.getJurPrsKindOptions("JUR_PRS", true) %> </select>
				<%=Bean.getCheckBox("is_resident", "Y", Bean.jurpersonXML.getfieldTransl("is_resident", false)) %>
			</td>
			<td><%= Bean.jurpersonXML.getfieldTransl("reg_code_country", true) %></td><td><select name="reg_code_country" class="inputfield"> <%= Bean.getCountryOptions(club.getValue("CODE_COUNTRY_DEF"), true) %> </select>
		</tr>
		<tr>
			<td><%= Bean.jurpersonXML.getfieldTransl("NAME_JUR_PRS", true) %></td> <td><input type="text" name="NAME_JUR_PRS" size="45" value="" class="inputfield"></td>
			<td><span id="inn_number_span"><%= Bean.jurpersonXML.getfieldTransl("inn_number", false) %></span></td> <td><input type="text" name="inn_number" id="inn_number" size="20" value="" class="inputfield"> </td>
		</tr>
		<tr>
			<td><%= Bean.jurpersonXML.getfieldTransl("SNAME_JUR_PRS", true) %></td> <td><input type="text" name="SNAME_JUR_PRS" size="45" value="" class="inputfield"></td>
			<td><%= Bean.jurpersonXML.getfieldTransl("kpp_number", false) %></td> <td><input type="text" name="kpp_number" id="kpp_number" size="20" value="" class="inputfield"> </td>
		</tr>
		<tr>
			<td><%= Bean.jurpersonXML.getfieldTransl("id_jur_prs_parent", false) %></td>
			<td>
				<%=Bean.getWindowFindJurPrs("jur_prs_parent", "", "ALL", "30") %>
			</td>			
			<td><%= Bean.jurpersonXML.getfieldTransl("ogrn_number", false) %></td> <td><input type="text" name="ogrn_number" id="ogrn_number" size="20" value="" class="inputfield"> </td>
		</tr>
		<tr>
			<td colspan="2">&nbsp;</td>			
			<td><%= Bean.jurpersonXML.getfieldTransl("mfo_bank", false) %></td> <td><input type="text" name="mfo_bank" id="mfo_bank" size="20" value="" class="inputfield"></td>
		</tr>
		<tr>
			<td colspan="2">&nbsp;</td>			
			<td><%= Bean.jurpersonXML.getfieldTransl("tax_percent", true) %></td> <td><input type="text" name="tax_percent" id="tax_percent" size="10" value="" class="inputfield"></td>
		</tr>

		<tr>							
			<td colspan="2" class="top_line"><b><%= Bean.jurpersonXML.getfieldTransl("h_club_registration", false) %></b></td>
			<td colspan="2" class="top_line"><b><%= Bean.jurpersonXML.getfieldTransl("h_club_member_parameters", false) %></b></td>
		</tr>
		<tr>
			<td><%= Bean.clubXML.getfieldTransl("club", false) %>
				<%= Bean.getGoToClubLink(club.getValue("ID_CLUB")) %>
			</td> 
			<td>
				<input type="hidden" name="id_club" size="35" value="<%=club.getValue("ID_CLUB") %>">
				<input type="text" name="name_club" size="35" value="<%=club.getValue("SNAME_CLUB") %>" readonly="readonly" class="inputfield-ro">
			</td>
			<td><%=Bean.getCheckBoxBase("is_shareholder", "N", Bean.jurpersonXML.getfieldTransl("is_shareholder", false), "color:green;font-weight:bold;", "checkJurPrsShareholder();", false, false) %></td>
			<td><%=Bean.getCheckBoxBase("is_dealer", "N", Bean.jurpersonXML.getfieldTransl("is_dealer", false), "color:blue;", false, false) %></td>
		</tr>

		<tr>
			<td><%= Bean.jurpersonXML.getfieldTransl("club_date_beg", true) %></td> <td><%=Bean.getCalendarInputField("club_date_beg", "", "10") %></td>
			<td><%=Bean.getCheckBoxBase("is_registrator", "N", Bean.jurpersonXML.getfieldTransl("is_registrator", false), "color:green;", false, false) %></td>
			<td><%=Bean.getCheckBox("is_bank", "N", Bean.jurpersonXML.getfieldTransl("is_bank", false)) %></td>
		</tr>
		<tr>
			<td><%= Bean.jurpersonXML.getfieldTransl("club_date_end", false) %></td> <td><%=Bean.getCalendarInputField("club_date_end", "", "10") %></td>
			<td><%=Bean.getCheckBoxBase("is_coordinator", "N", Bean.jurpersonXML.getfieldTransl("is_coordinator", false), "color:green;", false, false) %></td>
			<td><%=Bean.getCheckBox("is_issuer", "N", Bean.jurpersonXML.getfieldTransl("is_issuer", false)) %></td>
		</tr>
		<tr>
			<td><%= Bean.clubXML.getfieldTransl("club_member_status", true) %></td><td><select name="cd_club_member_status" class="inputfield" onchange="checkJurPrsClubMemberStatus(this.value);"> <%= Bean.getClubMemberStatusOptions("PARTICIPATE", true) %> </select></td>
			<td><%=Bean.getCheckBoxBase("is_curator", "N", Bean.jurpersonXML.getfieldTransl("is_curator", false), "color:green;", false, false) %></td>
			<td><%=Bean.getCheckBox("is_finance_acquirer", "N", Bean.jurpersonXML.getfieldTransl("is_finance_acquirer", false)) %></td>
		</tr>
		<tr>
			<td colspan="2">&nbsp;</td>
			<td><%=Bean.getCheckBoxBase("is_operator", "N", Bean.jurpersonXML.getfieldTransl("is_operator", false), "color:red;", false, false) %></td>
			<td><%=Bean.getCheckBox("is_technical_acquirer", "N", Bean.jurpersonXML.getfieldTransl("is_technical_acquirer", false)) %></td>
		</tr>
		<tr>
			<td colspan="2">&nbsp;</td>
			<td><%=Bean.getCheckBoxBase("is_agent", "N", Bean.jurpersonXML.getfieldTransl("is_agent", false), "color:red;", false, false) %></td>
			<td><%=Bean.getCheckBox("is_terminal_manufacturer", "N", Bean.jurpersonXML.getfieldTransl("is_terminal_manufacturer", false)) %></td>
		</tr>
		<tr>							
			<td colspan="4" class="top_line"><b><%= Bean.jurpersonXML.getfieldTransl("jur_adr_full", false) %></b></td>
		</tr>
		<tr>								
			<td><%= Bean.jurpersonXML.getfieldTransl("name_country", false) %></td><td><select name="JUR_ADR_CODE_COUNTRY" id="JUR_ADR_CODE_COUNTRY" class="inputfield" onchange="dwr_make_jur_adr('<%=Bean.getSessionId() %>');"><%= Bean.getCountryOptions("", true) %></select></td>
			<td><%= Bean.jurpersonXML.getfieldTransl("ADR_CITY", false) %></td><td><input type="text" name="JUR_ADR_CITY" id="JUR_ADR_CITY" size="25" value=""  class="inputfield"></td>				
		</tr>
		<tr>
			<td><%= Bean.jurpersonXML.getfieldTransl("ADR_ZIP_CODE", false) %></td><td><input type="text" name="JUR_ADR_ZIP_CODE" id="JUR_ADR_ZIP_CODE" size="25" value=""  class="inputfield"></td>
			<td><%= Bean.jurpersonXML.getfieldTransl("ADR_CITY_DISTRICT", false) %></td><td><input type="text" name="JUR_ADR_CITY_DISTRICT" id="JUR_ADR_CITY_DISTRICT" size="25" value=""  class="inputfield"></td>				
		</tr>
		<tr>
			<td><%= Bean.jurpersonXML.getfieldTransl("ADR_OBLAST", false) %></td><td><select name="JUR_ADR_ID_OBLAST" id="JUR_ADR_ID_OBLAST" class="inputfield" ><%= Bean.getOblastOptions("", "", true) %></select></td>				
			<td><%= Bean.jurpersonXML.getfieldTransl("ADR_STREET", false) %></td><td><input type="text" name="JUR_ADR_STREET" id="JUR_ADR_STREET" size="25" value="" class="inputfield"></td>
		</tr>
		<tr>
			<td><%= Bean.jurpersonXML.getfieldTransl("ADR_DISTRICT", false) %></td><td><input type="text" name="JUR_ADR_DISTRICT" id="JUR_ADR_DISTRICT" size="25" value=""  class="inputfield"></td>				
			<td><%= Bean.jurpersonXML.getfieldTransl("ADR_HOUSE_CASE_APARTMENT", false) %></td><td><input type="text" name="JUR_ADR_HOUSE" id="JUR_ADR_HOUSE" size="6" value=""  class="inputfield">/<input type="text" name="JUR_ADR_CASE" id="JUR_ADR_CASE" size="6" value=""  class="inputfield">/<input type="text" name="JUR_ADR_APARTMENT" id="JUR_ADR_APARTMENT" size="6" value=""  class="inputfield"></td>
		</tr>
		<tr>								
			<td colspan="4" class="top_line"><b><%= Bean.jurpersonXML.getfieldTransl("fact_adr_full", false) %></b>
			&nbsp;&nbsp;<button type="button" class="button" onclick="copyJurPrsFactAddress('<%=Bean.getSessionId() %>'); "><%= Bean.jurpersonXML.getfieldTransl("button_copy_jur_address", false) %> </button>
			</td>
		</tr>
		<tr>								
			<td><%= Bean.jurpersonXML.getfieldTransl("name_country", false) %></td><td><select name="FACT_ADR_CODE_COUNTRY" id="FACT_ADR_CODE_COUNTRY" class="inputfield" onchange="dwr_make_fact_adr('<%=Bean.getSessionId() %>');"><%= Bean.getCountryOptions("", true) %> </select></td>
			<td><%= Bean.jurpersonXML.getfieldTransl("ADR_CITY", false) %></td><td><input type="text" name="FACT_ADR_CITY" id="FACT_ADR_CITY" size="25" value=""  class="inputfield"></td>				
		</tr>
		<tr>
			<td><%= Bean.jurpersonXML.getfieldTransl("ADR_ZIP_CODE", false) %></td><td><input type="text" name="FACT_ADR_ZIP_CODE" id="FACT_ADR_ZIP_CODE" size="25" value=""  class="inputfield"></td>
			<td><%= Bean.jurpersonXML.getfieldTransl("ADR_CITY_DISTRICT", false) %></td><td><input type="text" name="FACT_ADR_CITY_DISTRICT" id="FACT_ADR_CITY_DISTRICT" size="25" value=""  class="inputfield"></td>				
		</tr>
		<tr>
			<td><%= Bean.jurpersonXML.getfieldTransl("ADR_OBLAST", false) %></td><td><select name="FACT_ADR_ID_OBLAST" id="FACT_ADR_ID_OBLAST" class="inputfield" onchange="dwr_make_fact_adr('<%=Bean.getSessionId() %>');"><%= Bean.getOblastOptions("", "", true) %></select></td>				
			<td><%= Bean.jurpersonXML.getfieldTransl("ADR_STREET", false) %></td><td><input type="text" name="FACT_ADR_STREET" id="FACT_ADR_STREET" size="25" value="" class="inputfield"></td>
		</tr>
		<tr>
			<td><%= Bean.jurpersonXML.getfieldTransl("ADR_DISTRICT", false) %></td><td><input type="text" name="FACT_ADR_DISTRICT" id="FACT_ADR_DISTRICT" size="25" value=""  class="inputfield"></td>				
			<td><%= Bean.jurpersonXML.getfieldTransl("ADR_HOUSE_CASE_APARTMENT", false) %></td><td><input type="text" name="FACT_ADR_HOUSE" id="FACT_ADR_HOUSE" size="6" value=""  class="inputfield">/<input type="text" name="FACT_ADR_CASE" id="FACT_ADR_CASE" size="6" value=""  class="inputfield">/<input type="text" name="FACT_ADR_APARTMENT" id="FACT_ADR_APARTMENT" size="6" value=""  class="inputfield"></td>
		</tr>
		<tr>								
			<td colspan="4" class="top_line"><b><%= Bean.jurpersonXML.getfieldTransl("title_contact_info", false) %></b></td>
		</tr>
		<tr>								
			<td><%= Bean.jurpersonXML.getfieldTransl("phone_work", false) %></td><td><input type="text" name="phone_work" id="phone_work" size="25" value=""  class="inputfield"></td>
			<td><%= Bean.jurpersonXML.getfieldTransl("web_site", false) %></td><td><input type="text" name="web_site" id="web_site" size="25" value=""  class="inputfield"></td>
		</tr>
		<tr>
			<td><%= Bean.jurpersonXML.getfieldTransl("fax", false) %></td><td><input type="text" name="fax" id="fax" size="25" value=""  class="inputfield"></td>
			<td><%= Bean.jurpersonXML.getfieldTransl("email", false) %></td><td><input type="text" name="email" id="email" size="25" value=""  class="inputfield"></td>
		</tr>		<tr>								
			<td colspan="4" class="top_line"><b><%= Bean.jurpersonXML.getfieldTransl("title_others", false) %></b></td>
		</tr>
		<tr>								
			<td><%= Bean.jurpersonXML.getfieldTransl("name_jur_prs_in_sms", false) %></td><td><input type="text" name="name_jur_prs_in_sms" id="name_jur_prs_in_sms" size="45" value=""  class="inputfield"></td>
			<td colspan="2">&nbsp;</td>
		</tr>

		<tr>
			<td colspan="4" align="center">
				<%=Bean.getSubmitButtonAjax("../crm/clients/yurpersonupdate.jsp") %>
				<%=Bean.getResetButton() %>
				<% if ("add".equalsIgnoreCase(action)) { %>
					<%=Bean.getGoBackButton("../crm/clients/yurpersons.jsp") %>
				<% } else { %>
					<%=Bean.getGoBackButton("../crm/clients/yurpersonspecs.jsp?id=" + id) %>
				<% } %>
			</td>
		</tr>

	</table>

</form>
		<%= Bean.getCalendarScript("club_date_beg", false) %>
		<%= Bean.getCalendarScript("club_date_end", false) %>

	        <%
    	} else {
    	    %> <%= Bean.getUnknownActionText(action) %><%
    	}
	
	} else if (process.equalsIgnoreCase("yes")) {
    	String    
    		id_jur_prs 					= Bean.getDecodeParam(parameters.get("id_jur_prs")), 
    		inn_number 					= Bean.getDecodeParam(parameters.get("inn_number")),
    		reg_code_country 			= Bean.getDecodeParam(parameters.get("reg_code_country")),
    		cd_jur_prs_kind				= Bean.getDecodeParam(parameters.get("cd_jur_prs_kind")), 
    		name_jur_prs 				= Bean.getDecodeParam(parameters.get("NAME_JUR_PRS")), 
    		sname_jur_prs 				= Bean.getDecodeParam(parameters.get("SNAME_JUR_PRS")), 
    		id_jur_prs_parent 			= Bean.getDecodeParam(parameters.get("id_jur_prs_parent")),
    		is_resident 				= Bean.getDecodeParam(parameters.get("is_resident")),
    		is_operator					= Bean.getDecodeParam(parameters.get("is_operator")),
    		is_agent					= Bean.getDecodeParam(parameters.get("is_agent")),
    		is_issuer 					= Bean.getDecodeParam(parameters.get("is_issuer")),
    		is_finance_acquirer 		= Bean.getDecodeParam(parameters.get("is_finance_acquirer")),
    		is_technical_acquirer 		= Bean.getDecodeParam(parameters.get("is_technical_acquirer")),
    		is_dealer			 		= Bean.getDecodeParam(parameters.get("is_dealer")),
    		is_bank				 		= Bean.getDecodeParam(parameters.get("is_bank")),
    		is_partner 					= Bean.getDecodeParam(parameters.get("is_partner")),
    		is_terminal_manufacturer	= Bean.getDecodeParam(parameters.get("is_terminal_manufacturer")),
    		is_shareholder				= Bean.getDecodeParam(parameters.get("is_shareholder")),
    		is_registrator				= Bean.getDecodeParam(parameters.get("is_registrator")),
    		is_coordinator				= Bean.getDecodeParam(parameters.get("is_coordinator")),
    		is_curator					= Bean.getDecodeParam(parameters.get("is_curator")),
    		//id_loyality_scheme_default 	= Bean.getDecodeParam(parameters.get("id_loyality_scheme_default")),
    		//id_shedule_default 			= Bean.getDecodeParam(parameters.get("id_shedule_default")),
    		jur_adr_code_country 		= Bean.getDecodeParam(parameters.get("JUR_ADR_CODE_COUNTRY")),
    		jur_adr_zip_code 			= Bean.getDecodeParam(parameters.get("JUR_ADR_ZIP_CODE")), 
    		jur_adr_oblast 				= Bean.getDecodeParam(parameters.get("JUR_ADR_ID_OBLAST")), 
    		jur_adr_district 			= Bean.getDecodeParam(parameters.get("JUR_ADR_ID_DISTRICT")), 
    		jur_adr_city_district 		= Bean.getDecodeParam(parameters.get("JUR_ADR_ID_CITY_DISTRICT")), 
    		jur_adr_city 				= Bean.getDecodeParam(parameters.get("JUR_ADR_ID_CITY")), 
    		jur_adr_street 				= Bean.getDecodeParam(parameters.get("JUR_ADR_STREET")),
    		jur_adr_house 				= Bean.getDecodeParam(parameters.get("JUR_ADR_HOUSE")),
    		jur_adr_case 				= Bean.getDecodeParam(parameters.get("JUR_ADR_CASE")),
    		jur_adr_apartment			= Bean.getDecodeParam(parameters.get("JUR_ADR_APARTMENT")),
    		fact_adr_code_country 		= Bean.getDecodeParam(parameters.get("FACT_ADR_CODE_COUNTRY")),
    		fact_adr_zip_code 			= Bean.getDecodeParam(parameters.get("FACT_ADR_ZIP_CODE")), 
    		fact_adr_oblast 			= Bean.getDecodeParam(parameters.get("FACT_ADR_ID_OBLAST")), 
    		fact_adr_district 			= Bean.getDecodeParam(parameters.get("FACT_ADR_ID_DISTRICT")), 
    		fact_adr_city_district 		= Bean.getDecodeParam(parameters.get("FACT_ADR_ID_CITY_DISTRICT")), 
    		fact_adr_city 				= Bean.getDecodeParam(parameters.get("FACT_ADR_ID_CITY")), 
    		fact_adr_street 			= Bean.getDecodeParam(parameters.get("FACT_ADR_STREET")),
    		fact_adr_house 				= Bean.getDecodeParam(parameters.get("FACT_ADR_HOUSE")),
    		fact_adr_case 				= Bean.getDecodeParam(parameters.get("FACT_ADR_CASE")),
    		fact_adr_apartment			= Bean.getDecodeParam(parameters.get("FACT_ADR_APARTMENT")),
    		id_club						= Bean.getDecodeParam(parameters.get("id_club")),
    		club_date_beg				= Bean.getDecodeParam(parameters.get("club_date_beg")),
    		club_date_end				= Bean.getDecodeParam(parameters.get("club_date_end")),
    		cd_club_jur_prs				= Bean.getDecodeParam(parameters.get("cd_club_jur_prs")),
    		cd_club_member_status		= Bean.getDecodeParam(parameters.get("cd_club_member_status")),
	    	//client_bank_format			= Bean.getDecodeParam(parameters.get("client_bank_format")),
	    	//client_bank_charset			= Bean.getDecodeParam(parameters.get("client_bank_charset")),
	    	
	    	mfo_bank					= Bean.getDecodeParam(parameters.get("mfo_bank")),
	    	kpp_number					= Bean.getDecodeParam(parameters.get("kpp_number")),
	    	ogrn_number					= Bean.getDecodeParam(parameters.get("ogrn_number")),
	    	
    		phone_work					= Bean.getDecodeParam(parameters.get("phone_work")),
    		fax							= Bean.getDecodeParam(parameters.get("fax")),
    		web_site					= Bean.getDecodeParam(parameters.get("web_site")),
    		email						= Bean.getDecodeParam(parameters.get("email")),
    		tax_percent					= Bean.getDecodeParam(parameters.get("tax_percent")),
    		name_jur_prs_in_sms			= Bean.getDecodeParam(parameters.get("name_jur_prs_in_sms")),
    		LUD							= Bean.getDecodeParam(parameters.get("LUD"));
	    	
	    	//rec_payment_ext_nomenkl_format	= Bean.getDecodeParam(parameters.get("rec_payment_ext_nomenkl_format"));
    
		if (action.equalsIgnoreCase("add")) { 
			
			ArrayList<String> pParam = new ArrayList<String>();
			
			pParam.add(reg_code_country);
			pParam.add(inn_number);
			pParam.add(kpp_number);
			pParam.add(ogrn_number);
			pParam.add(cd_jur_prs_kind);
			pParam.add(name_jur_prs);
			pParam.add(sname_jur_prs);
			pParam.add(name_jur_prs_in_sms);
			pParam.add(id_jur_prs_parent);
			pParam.add(mfo_bank);
			pParam.add(tax_percent);
			pParam.add(is_resident);
			pParam.add(jur_adr_code_country);
			pParam.add(jur_adr_zip_code);
			pParam.add(jur_adr_oblast);
			pParam.add(jur_adr_district);
			pParam.add(jur_adr_city);
			pParam.add(jur_adr_city_district);
			pParam.add(jur_adr_street);
			pParam.add(jur_adr_house);
			pParam.add(jur_adr_case);
			pParam.add(jur_adr_apartment);
			pParam.add(fact_adr_code_country);
			pParam.add(fact_adr_zip_code);
			pParam.add(fact_adr_oblast);
			pParam.add(fact_adr_district);
			pParam.add(fact_adr_city);
			pParam.add(fact_adr_city_district);
			pParam.add(fact_adr_street);
			pParam.add(fact_adr_house);
			pParam.add(fact_adr_case);
			pParam.add(fact_adr_apartment);
			pParam.add(phone_work);
			pParam.add(fax);
			pParam.add(web_site);
			pParam.add(email);
			pParam.add(id_club);
			//pParam.add(cd_club_jur_prs);
			pParam.add(cd_club_member_status);
			pParam.add(is_issuer);
			pParam.add(is_finance_acquirer);
			pParam.add(is_technical_acquirer);
			pParam.add(is_dealer);
			pParam.add(is_bank);
			pParam.add(is_operator);
			pParam.add(is_agent);
			pParam.add(is_partner);
			pParam.add(is_terminal_manufacturer);
			pParam.add(is_shareholder);
			pParam.add(is_registrator);
			pParam.add(is_coordinator);
			pParam.add(is_curator);
			pParam.add(club_date_beg);
			pParam.add(club_date_end);
			pParam.add(Bean.getDateFormat());
	
			%>
			<%= Bean.executeInsertFunction("PACK$JUR_PRS_UI.add_jur_prs", pParam, "../crm/clients/yurpersonspecs.jsp?id=" , "../crm/clients/yurpersons.jsp") %>
			<% 	
     
		} else if (action.equalsIgnoreCase("remove")) { 
			
			id_club = Bean.getCurrentClubID();
			
			ArrayList<String> pParam = new ArrayList<String>();
				
			pParam.add(id);
			pParam.add(id_club);

			%>
			<%= Bean.executeDeleteFunction("PACK$JUR_PRS_UI.delete_jur_prs", pParam, "../crm/clients/yurpersons.jsp" , "") %>
			<% 	
     
		} else if (action.equalsIgnoreCase("edit")) { 
			
			ArrayList<String> pParam = new ArrayList<String>();
						
			pParam.add(id_jur_prs);
			pParam.add(reg_code_country);
			pParam.add(inn_number);
			pParam.add(kpp_number);
			pParam.add(ogrn_number);
			pParam.add(cd_jur_prs_kind);
			pParam.add(name_jur_prs);
			pParam.add(sname_jur_prs);
			pParam.add(name_jur_prs_in_sms);
			pParam.add(id_jur_prs_parent);
			pParam.add(mfo_bank);
			pParam.add(tax_percent);
			pParam.add(is_resident);
			pParam.add(jur_adr_code_country);
			pParam.add(jur_adr_zip_code);
			pParam.add(jur_adr_oblast);
			pParam.add(jur_adr_district);
			pParam.add(jur_adr_city);
			pParam.add(jur_adr_city_district);
			pParam.add(jur_adr_street);
			pParam.add(jur_adr_house);
			pParam.add(jur_adr_case);
			pParam.add(jur_adr_apartment);
			pParam.add(fact_adr_code_country);
			pParam.add(fact_adr_zip_code);
			pParam.add(fact_adr_oblast);
			pParam.add(fact_adr_district);
			pParam.add(fact_adr_city);
			pParam.add(fact_adr_city_district);
			pParam.add(fact_adr_street);
			pParam.add(fact_adr_house);
			pParam.add(fact_adr_case);
			pParam.add(fact_adr_apartment);
			pParam.add(phone_work);
			pParam.add(fax);
			pParam.add(web_site);
			pParam.add(email);
			pParam.add(id_club);
			pParam.add(cd_club_jur_prs);
			pParam.add(cd_club_member_status);
			pParam.add(is_issuer);
			pParam.add(is_finance_acquirer);
			pParam.add(is_technical_acquirer);
			pParam.add(is_dealer);
			pParam.add(is_bank);
			pParam.add(is_operator);
			pParam.add(is_agent);
			pParam.add(is_partner);
			pParam.add(is_terminal_manufacturer);
			pParam.add(is_shareholder);
			pParam.add(is_registrator);
			pParam.add(is_coordinator);
			pParam.add(is_curator);
			pParam.add(club_date_beg);
			pParam.add(club_date_end);
			pParam.add(LUD);
			pParam.add(Bean.getDateFormat());

			%>
			<%= Bean.executeUpdateFunction("PACK$JUR_PRS_UI.update_jur_prs", pParam, "../crm/clients/yurpersonspecs.jsp?id=" + id_jur_prs, "../crm/clients/yurpersonspecs.jsp?id=" + id_jur_prs) %>
			<% 	
    
		} else { %>
			<%= Bean.getUnknownActionText(action) %><% 
		}
	} else {%>
		<%= Bean.getUnknownProcessText(process) %> <%
  	}
} else if (type.equalsIgnoreCase("issuer")) {
	if (process.equalsIgnoreCase("yes")) {
		if (action.equalsIgnoreCase("edit")) {
			String l_paramCount	= Bean.getDecodeParam(parameters.get("paramcount"));
			  
			String[] results = new String[2];
			
			String update_sql = "";
		    String nameParam = "";
		    String valueParam = "";
		    String exist_flag = "N";
		    String id_club = "";
		    String fullResult = "0";
		    String fullResultMessage = "";
		    String callSQL = "";
		    String errorCallSQL = "";
 			String resultInt = "";
 			String resultMessage = "";
   		 
   		    int i;
   			for (i=1;i<=Integer.parseInt(l_paramCount);i++) {
   				nameParam		= Bean.getDecodeParam(parameters.get("idpaysys"+i));
   				valueParam		= Bean.getDecodeParam(parameters.get("cdintelgr"+i));
   				exist_flag		= Bean.getDecodeParam(parameters.get("existflag"+i));
   				id_club			= Bean.getDecodeParam(parameters.get("id_club"+i));

   				callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK$JUR_PRS_UI.update_issuer_param(?,?,?,?,?,?)}";

				String[] pParam = new String [5];
						
				pParam[0] = id;
				pParam[1] = nameParam;
				pParam[2] = valueParam;
				pParam[3] = exist_flag;
				pParam[4] = id_club;
	
				results = Bean.myCallFunctionParam(callSQL, pParam, 2);
				resultInt = results[0];
				resultMessage = results[1];
		
				%>
				<%= Bean.showCallSQL(callSQL) %>
				<%

				if (!"0".equalsIgnoreCase(resultInt)) { 
					fullResult = resultInt;
   					fullResultMessage = fullResultMessage + ", " + resultMessage;
   					errorCallSQL = errorCallSQL + ",<br>" + callSQL;
   				}
   			}
   	   
   		 	%>
	   	    <%=Bean.showCallResult(
	   	    		errorCallSQL, 
	   	    		fullResult, 
	   	    		fullResult, 
	   	    		"../crm/clients/yurpersonspecs.jsp?id=" + id, 
	   	    		"../crm/clients/yurpersonspecs.jsp?id=" + id, 
	   	    		Bean.form_messageXML.getfieldTransl("save_error", false)) %>
	   		<% 
	   		
	 	} else { %> 
			<%= Bean.getUnknownActionText(action) %><%
		}
	} else {
	    %> <%= Bean.getUnknownProcessText(process) %> <%
	}
} else if (type.equalsIgnoreCase("contacts")) {
	
	String 
		id_contact 			= Bean.getDecodeParam(parameters.get("id_contact")),
		surname				= Bean.getDecodeParam(parameters.get("surname")),
		name 				= Bean.getDecodeParam(parameters.get("name")),
		patronymic 			= Bean.getDecodeParam(parameters.get("patronymic")),
		sex 				= Bean.getDecodeParam(parameters.get("sex")),
		desc_contact_prs 	= Bean.getDecodeParam(parameters.get("desc_contact_prs")),
		cd_post				= Bean.getDecodeParam(parameters.get("cd_post")),
		phone_work 			= Bean.getDecodeParam(parameters.get("phone_work")),
		phone_mobile 		= Bean.getDecodeParam(parameters.get("phone_mobile")),
		email_work 			= Bean.getDecodeParam(parameters.get("email_work")),
		id_club				= Bean.getDecodeParam(parameters.get("id_club"));

	if (process.equalsIgnoreCase("no")) {
		
		bcJurPrsObject jurprs = new bcJurPrsObject(id);
		
		if (action.equalsIgnoreCase("add")) {
			
			bcClubShortObject club = new bcClubShortObject(Bean.getCurrentClubID());
			
	        %> 
	
			<script>
				var formData = new Array (
						new Array ('surname', 'varchar2', 1),
						new Array ('name', 'varchar2', 1),
						new Array ('sex', 'varchar2', 1),
						new Array ('name_club', 'varchar2', 1),
						new Array ('cd_post', 'varchar2', 1)
				);
			</script>

		<%= Bean.getOperationTitleShort(
				"",
				Bean.contactXML.getfieldTransl("l_add_contact", false),
				"Y",
				"N") 
		%>
    <form action="../crm/clients/yurpersonupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">
        <input type="hidden" name="type" value="contacts">
        <input type="hidden" name="action" value="add">
        <input type="hidden" name="process" value="yes">
        <input type="hidden" name="id" value="<%=id %>">
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
				<td><%= Bean.natprsXML.getfieldTransl("name", true) %></td> <td><input type="text" name="name" size="55" value="" class="inputfield"> </td>
				<%  String jurPrsTitle = "PARTNER".equalsIgnoreCase(jurprs.getValue("CD_JUR_PRS_STATUS"))?"title_partner":"title_service_place"; %>
				<td><%= Bean.jurpersonXML.getfieldTransl(jurPrsTitle, false) %></td> <td><input type="text" name="NAME_JUR_PRS" size="45" value="<%= jurprs.getValue("SNAME_JUR_PRS") %>" readonly="readonly" class="inputfield-ro"></td>
			</tr>
			<tr>
				<td><%= Bean.natprsXML.getfieldTransl("patronymic", false) %></td> <td><input type="text" name="patronymic" size="55" value="" class="inputfield"> </td>
				<td><%= Bean.contactXML.getfieldTransl("name_post", true) %></td> <td><select name="cd_post" class="inputfield" > <%= Bean.getContactPrsTypeOptions("", true) %></select></td>
			</tr>
			<tr>
				<td><%= Bean.natprsXML.getfieldTransl("sex", true) %></td><td><select name="sex" class="inputfield"><%= Bean.getMeaningFromLookupNameOptions("MALE_FEMALE", "", true) %></select> </td>
				<td colspan="2">&nbsp;</td>
			</tr>
		    <tr>
				<td><%= Bean.contactXML.getfieldTransl("desc_contact_prs", false) %></td><td><textarea name="desc_contact_prs" cols="52" rows="3" class="inputfield"></textarea></td>
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
			<td colspan="6" align="center">
				<%=Bean.getSubmitButtonAjax("../crm/clients/yurpersonupdate.jsp", "submit", "updateForm", "div_data_detail") %>
				<%=Bean.getResetButton() %>
				<%=Bean.getGoBackButton("../crm/clients/yurpersonspecs.jsp?id=" + id) %>
			</td>
		</tr>
	</table>
	</form>

		<% 
		} else if (action.equalsIgnoreCase("edit")) {
			
			bcContactsObject contact = new bcContactsObject(id_contact);
			boolean isShareholder = "Y".equalsIgnoreCase(contact.getValue("IS_SHAREHOLDER"))?true:false;
			
	        %> 
	
			<script>
				var formData = new Array (
						new Array ('cd_post', 'varchar2', 1)
				);
			</script>

		<%= Bean.getOperationTitleShort(
				"",
				Bean.contactXML.getfieldTransl("l_edit_contact", false),
				"Y",
				"N") 
		%>
    <form action="../crm/clients/yurpersonupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">
        <input type="hidden" name="type" value="contacts">
        <input type="hidden" name="action" value="edit">
        <input type="hidden" name="process" value="yes">
        <input type="hidden" name="id" value="<%=id %>">
        <input type="hidden" name="id_contact" value="<%=id_contact %>">
	<table <%=Bean.getTableDetailParam() %>>
	   <tr>
			<td><%= Bean.contactXML.getfieldTransl("name_contact_prs", false) %></td> <td><input type="text" name="name_contact_prs" size="55" value="<%=contact.getValue("NAME_CONTACT_PRS") %>" readonly="readonly" class="inputfield-ro"> </td>
			<td><%= Bean.clubXML.getfieldTransl("club", false) %>
					<%= Bean.getGoToClubLink(contact.getValue("ID_CLUB")) %>
			</td><td><input type="text" name="name_club" size="55" value="<%= Bean.getClubShortName(contact.getValue("ID_CLUB")) %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
	    <tr>
			<td><%=Bean.contactXML.getfieldTransl("contact_prs_place", false)%>
				<%=Bean.getGoToJurPrsHyperLink(contact.getValue("ID_JUR_PRS")) %>
			</td>
			<td><input type="text" name="NAME_JUR_PRS" size="55" value="<%= jurprs.getValue("SNAME_JUR_PRS") %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.contactXML.getfieldTransl("is_shareholder", false) %>
			<% if (isShareholder) { %>
					<%= Bean.getGoToNatPrsLink(contact.getValue("ID_NAT_PRS")) %>
			<% } else { %>
			
			<% } %>
			</td> <td><input type="text" name="is_shareholder_tsl" size="10" value="<%=Bean.getMeaningFoCodeValue("YES_NO", contact.getValue("IS_SHAREHOLDER")) %>" readonly="readonly" class="inputfield-ro"> </td>
		</tr>
	    <tr>
			<td><%= Bean.contactXML.getfieldTransl("name_post", true) %></td> <td><select name="cd_post" class="inputfield" > <%= Bean.getContactPrsTypeOptions(contact.getValue("CD_POST"), true) %></select></td>
			<% if (isShareholder) { %>
			<td><%= Bean.clubXML.getfieldTransl("club_date_beg", false) %></td> <td><input type="text" name="club_date_beg" size="10" value="<%=contact.getValue("CLUB_DATE_BEG_FRMT") %>" readonly="readonly" class="inputfield-ro"> </td>
			<% } %>			
		</tr>
	    <tr>
			<td><%= Bean.contactXML.getfieldTransl("desc_contact_prs", false) %></td><td><textarea name="desc_contact_prs" cols="52" rows="3" class="inputfield"><%=contact.getValue("DESC_NAT_PRS_ROLE") %></textarea></td>
			<% if (isShareholder) { %>
			<td><%= Bean.getClubCardXMLFieldTransl("cd_card1", false) %>
				<%= Bean.getGoToClubCardLink(
						contact.getValue("CARD_SERIAL_NUMBER"),
						contact.getValue("CARD_ID_ISSUER"),
						contact.getValue("CARD_ID_PAYMENT_SYSTEM")
					) %>
			</td><td><input type="text" name="cd_card1" size="30" value="<%= contact.getValue("CD_CARD1") %>" readonly="readonly" class="inputfield-ro"></td>
			<% } %>			
		</tr>
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
			<td colspan="6" align="center">
				<%=Bean.getSubmitButtonAjax("../crm/clients/yurpersonupdate.jsp", "submit", "updateForm", "div_data_detail") %>
				<%=Bean.getResetButton() %>
				<%=Bean.getGoBackButton("../crm/clients/yurpersonspecs.jsp?id=" + id) %>
			</td>
		</tr>
	</table>
	</form>

		<%
			
		} else { %> 
		<%= Bean.getUnknownActionText(action) %><%
			
		}
	} else if (process.equalsIgnoreCase("yes")) {
		
		if (action.equalsIgnoreCase("add")) {
			ArrayList<String> pParam = new ArrayList<String>();
					
			pParam.add(id);
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
	
		 	%>
			<%= Bean.executeInsertFunction("PACK$JUR_PRS_UI.add_contact_prs", pParam, "../crm/clients/yurpersonspecs.jsp?id=" + id + "&id_contact=", "") %>
			<% 	
		
		} else if (action.equalsIgnoreCase("remove")) {
			
			ArrayList<String> pParam = new ArrayList<String>();
					
			pParam.add(id_contact);

		 	%>
			<%= Bean.executeDeleteFunction("PACK$JUR_PRS_UI.delete_contact_prs", pParam, "../crm/clients/yurpersonspecs.jsp?id=" + id, "") %>
			<% 	
	 	
		} else if (action.equalsIgnoreCase("edit")) {
		
			ArrayList<String> pParam = new ArrayList<String>();
						
			pParam.add(id_contact);
			pParam.add(id);
			pParam.add(cd_post);
			pParam.add(desc_contact_prs);
			pParam.add(phone_work);
			pParam.add(email_work);

		 	%>
			<%= Bean.executeUpdateFunction("PACK$JUR_PRS_UI.update_contact_prs", pParam, "../crm/clients/yurpersonspecs.jsp?id=" + id, "") %>
			<% 	
		
		} else { %> 
			<%= Bean.getUnknownActionText(action) %><%
		}
	} else {
	    %> <%= Bean.getUnknownProcessText(process) %> <%
	}
	
} else if (type.equalsIgnoreCase("bk_account")) {
	
	if (process.equalsIgnoreCase("yes")) {
		if (action.equalsIgnoreCase("check")) {
			
			ArrayList<String> pParam = new ArrayList<String>();
				
			pParam.add("JUR_PRS");
			pParam.add(id);
			
		 	%>
			<%= Bean.executeInsertFunction("PACK$JUR_PRS_UI.check_bk_accounts", pParam, "../crm/clients/yurpersonspecs.jsp?id=" + id + "&id_report=", "") %>
			<% 	
	   
		} else { %> 
			<%= Bean.getUnknownActionText(action) %><% 
		}
	} else { %>
		<%= Bean.getUnknownProcessText(process) %> <br><% 
	}
} else if (type.equalsIgnoreCase("loy")) {
	if (process.equalsIgnoreCase("yes")) {
    	if (action.equalsIgnoreCase("submit")) {
    	    String    
	    	    id_serv_place				= Bean.getDecodeParam(parameters.get("id_serv_place")),
    		    cd_loy	 					= Bean.getDecodeParam(parameters.get("cd_loy")),
    		    id_schedule					= Bean.getDecodeParam(parameters.get("id_schedule"));

		    ArrayList<String> pParam = new ArrayList<String>();
							
			pParam.add(id);
			pParam.add(id_serv_place);
			pParam.add(cd_loy);
			pParam.add(id_schedule);

		 	%> 
			<%= Bean.executeUpdateFunction("PACK$JUR_PRS_UI.apply_loy", pParam, "../crm/clients/yurpersonspecs.jsp?id=" + id, "") %>
			<% 	

    	} else { %> 
    		<%= Bean.getUnknownActionText(action) %><% 
		}
	}  else { %> 
		<%= Bean.getUnknownProcessText(process) %> <br><% 
	}
}  else if (type.equalsIgnoreCase("device")) {
	System.out.println(parameters.values());
	String 
		cd_term_type 			= Bean.getDecodeParamPrepare(parameters.get("cd_term_type")),
		id_device_type 			= Bean.getDecodeParamPrepare(parameters.get("id_device_type")),
		name_device_type 		= Bean.getDecodeParamPrepare(parameters.get("name_device_type")),
		desc_device_type 		= Bean.getDecodeParamPrepare(parameters.get("desc_device_type")),
		exist_flag 				= Bean.getDecodeParamPrepare(parameters.get("exist_flag")),
		work_with_certificate 	= Bean.getDecodeParamPrepare(parameters.get("work_with_certificate")),
		term_code_page 			= Bean.getDecodeParamPrepare(parameters.get("term_code_page")),
		resp_time	 			= Bean.getDecodeParamPrepare(parameters.get("resp_time")),
		ver_telgr	 			= Bean.getDecodeParamPrepare(parameters.get("ver_telgr")),
		crypt_telgr 			= Bean.getDecodeParamPrepare(parameters.get("crypt_telgr")),
		vk_enc  	 			= Bean.getDecodeParamPrepare(parameters.get("vk_enc")),
		tr_limit  	 			= Bean.getDecodeParamPrepare(parameters.get("tr_limit")),
		nincmax  	 			= Bean.getDecodeParamPrepare(parameters.get("nincmax")),
		connect_ekka 			= Bean.getDecodeParamPrepare(parameters.get("connect_ekka")),
		noprint_cheque_discount_club 	= Bean.getDecodeCheckBoxNumber(parameters.get("noprint_cheque_discount_club")),
		noprint_cheque_bonus_club 		= Bean.getDecodeCheckBoxNumber(parameters.get("noprint_cheque_bonus_club")),
		noprint_cheque_mov_bon 			= Bean.getDecodeCheckBoxNumber(parameters.get("noprint_cheque_mov_bon")),
		noprint_cheque_check_param 		= Bean.getDecodeCheckBoxNumber(parameters.get("noprint_cheque_check_param")),
		noprint_cheque_pay_cash 		= Bean.getDecodeCheckBoxNumber(parameters.get("noprint_cheque_pay_cash")),
		noprint_cheque_pay_card 		= Bean.getDecodeCheckBoxNumber(parameters.get("noprint_cheque_pay_card")),
		noprint_cheque_pay_bon 			= Bean.getDecodeCheckBoxNumber(parameters.get("noprint_cheque_pay_bon")),
		card_type_active_nsmep 			= Bean.getDecodeCheckBoxNumber(parameters.get("card_type_active_nsmep")),
		card_type_active_magnetic 		= Bean.getDecodeCheckBoxNumber(parameters.get("card_type_active_magnetic")),
		card_type_active_chip 			= Bean.getDecodeCheckBoxNumber(parameters.get("card_type_active_chip")),
		card_type_active_emv 			= Bean.getDecodeCheckBoxNumber(parameters.get("card_type_active_emv")),
		card_type_active_barcode 		= Bean.getDecodeCheckBoxNumber(parameters.get("card_type_active_barcode")),
		oper_type_active_nsmep 			= Bean.getDecodeCheckBoxNumber(parameters.get("oper_type_active_nsmep")),
		oper_type_active_cash 			= Bean.getDecodeCheckBoxNumber(parameters.get("oper_type_active_cash")),
		oper_type_active_bon 			= Bean.getDecodeCheckBoxNumber(parameters.get("oper_type_active_bon")),
		oper_type_active_cheque 		= Bean.getDecodeCheckBoxNumber(parameters.get("oper_type_active_cheque")),
		oper_type_active_emv 			= Bean.getDecodeCheckBoxNumber(parameters.get("oper_type_active_emv"));

    String[] results = new String[2];
	
	if (process.equalsIgnoreCase("no")) {
		%>
			<script>
				var formData = new Array (
					new Array ('cd_term_type', 'varchar2', 1),
					new Array ('name_device_type', 'varchar2', 1),
					new Array ('work_with_certificate', 'varchar2', 1),
					new Array ('exist_flag', 'varchar2', 1),
					new Array ('term_code_page', 'varchar2', 1),
					new Array ('resp_time', 'varchar2', 1),
					new Array ('crypt_telgr', 'varchar2', 1),
					new Array ('ver_telgr', 'varchar2', 1),
					new Array ('vk_enc', 'varchar2', 1),
					new Array ('nincmax', 'varchar2', 1),
					new Array ('tr_limit', 'varchar2', 1),
					new Array ('connect_ekka', 'varchar2', 1)
				);
			</script>

		<%
		if (action.equalsIgnoreCase("add")) {
			
			%> 
			<%= Bean.getOperationTitleShort(
					"",
					Bean.terminalXML.getfieldTransl("h_add_devict_type", false),
					"Y",
					"N") 
			%>
		<form action="../crm/clients/yurpersonupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">
        	<input type="hidden" name="type" value="device">
	        <input type="hidden" name="action" value="add">
    	    <input type="hidden" name="process" value="yes">
        	<input type="hidden" name="id" value="<%=id %>">
		
		<table <%=Bean.getTableDetailParam() %>>
    		<tr>
				<td><%= Bean.terminalXML.getfieldTransl("cd_term_type", true) %></td> <td><select name="cd_term_type" class="inputfield" > <%= Bean.getTermTypeOptions("", true) %></select></td>
				<td><%= Bean.terminalXML.getfieldTransl("work_with_certificate", true) %></td> <td><select name="work_with_certificate" class="inputfield" > <%= Bean.getMeaningFromLookupNameOptions("YES_NO", "Y", true) %></select></td>
			</tr>
    		<tr>
				<td><%= Bean.terminalXML.getfieldTransl("name_device_type", true) %></td> <td><input type="text" name="name_device_type" size="53" value="" class="inputfield"> </td>
				<td><%= Bean.terminalXML.getfieldTransl("exist_flag", true) %></td> <td><select name="exist_flag" class="inputfield" > <%= Bean.getMeaningFromLookupNameOptions("YES_NO", "Y", true) %></select></td>
			</tr>
    		<tr>
				<td><%= Bean.terminalXML.getfieldTransl("desc_device_type", false) %></td> <td><textarea name="desc_device_type" cols="50" rows="3" class="inputfield"></textarea></td>
			</tr>

		<tr>
			<td class="top_line" colspan="4"><b><%=Bean.terminalXML.getfieldTransl("h_term_setting_param", false) %></b></td>
		</tr>
		<tr>
			<td><%= Bean.terminalXML.getfieldTransl("TERM_CODE_PAGE", true) %></td> <td><select name="term_code_page"  class="inputfield"><%= Bean.getMeaningFromLookupNameOptions("TERMINAL_CODE_PAGE", "", true) %></select></td>
			<td><%= Bean.terminalXML.getfieldTransl("VER_TELGR", true) %></td> <td><select name="ver_telgr" class="inputfield" title="TEL_VERSION"><%= Bean.getMeaningFromLookupNameOptions("VER_TELGR", "", true) %></select></td>
		</tr>
		<tr>
			<td><%= Bean.terminalXML.getfieldTransl("CRYPT_TELGR", true) %></td><td><select name="crypt_telgr" class="inputfield" title="CRYPT_TELGR"><%= Bean.getMeaningFromLookupNumHTML("YES_NO", "0", true) %></select></td>
			<td valign="top"><%= Bean.terminalXML.getfieldTransl("nincmax", true) %></td>  <td><input type="text" name="nincmax" size="16" value="" class="inputfield" title="NINCMAX"></td>
		</tr>
		<tr>
			<td><%= Bean.terminalXML.getfieldTransl("RESP_TIME", true) %></td> <td><input type="text" name="resp_time" size="16" value="" class="inputfield" title="RESP_TIME"></td>
			<td><%= Bean.terminalXML.getfieldTransl("connect_ekka", true) %></td> <td><select name="connect_ekka" class="inputfield" title="CONNECT_EKKA"><%= Bean.getMeaningFromLookupNumberOptions("YES_NO", "0", true) %></select></td>
		</tr>
		<tr>
			<td valign="top"><%= Bean.terminalXML.getfieldTransl("VK_ENC", true) %></td> <td valign="top"><select name="vk_enc" class="inputfield" title="VK_ENC"><%= Bean.getMeaningFromLookupNameOptions("VER_KEY", "", true) %></select></td>
			<td valign="top"><%= Bean.terminalXML.getfieldTransl("tr_limit", true) %></td>  <td><input type="text" name="tr_limit" size="16" value="" class="inputfield" title="TR_LIMIT"></td>
		</tr>
		<tr>
			<td colspan="4">&nbsp;</td>
		</tr>

		<tr>
			<td><b><%= Bean.terminalXML.getfieldTransl("h_bon_cards_types", false) %></b></td>
			<td><b><%= Bean.terminalXML.getfieldTransl("h_operations_types", false) %></b></td>
			<td colspan="2"><b><%= Bean.terminalXML.getfieldTransl("h_print_cheque_settings", false) %></b></td>
		</tr>
		<tr>
			<td><%=Bean.getCheckBoxNumber("card_type_active_nsmep", "1", Bean.terminalXML.getfieldTransl("card_type_active_nsmep", false)) %></td>
			<td><%=Bean.getCheckBoxNumber("oper_type_active_nsmep", "1", Bean.terminalXML.getfieldTransl("oper_type_active_nsmep", false)) %></td>
			<td><%=Bean.getCheckBoxNumber("noprint_cheque_discount_club", "0", Bean.terminalXML.getfieldTransl("noprint_cheque_discount_club", false)) %></td>
			<td><%=Bean.getCheckBoxNumber("noprint_cheque_pay_cash", "0", Bean.terminalXML.getfieldTransl("noprint_cheque_pay_cash", false)) %></td>
		</tr>
		<tr>
			<td><%=Bean.getCheckBoxNumber("card_type_active_magnetic", "1", Bean.terminalXML.getfieldTransl("card_type_active_magnetic", false)) %></td>
			<td><%=Bean.getCheckBoxNumber("oper_type_active_cash", "1", Bean.terminalXML.getfieldTransl("oper_type_active_cash", false)) %></td>
			<td><%=Bean.getCheckBoxNumber("noprint_cheque_bonus_club", "0", Bean.terminalXML.getfieldTransl("noprint_cheque_bonus_club", false)) %></td>
			<td><%=Bean.getCheckBoxNumber("noprint_cheque_pay_card", "0", Bean.terminalXML.getfieldTransl("noprint_cheque_pay_card", false)) %></td>
		</tr>
		<tr>
			<td><%=Bean.getCheckBoxNumber("card_type_active_chip", "1", Bean.terminalXML.getfieldTransl("card_type_active_chip", false)) %></td>
			<td><%=Bean.getCheckBoxNumber("oper_type_active_bon", "1", Bean.terminalXML.getfieldTransl("oper_type_active_bon", false)) %></td>
			<td><%=Bean.getCheckBoxNumber("noprint_cheque_mov_bon", "0", Bean.terminalXML.getfieldTransl("noprint_cheque_mov_bon", false)) %></td>
			<td><%=Bean.getCheckBoxNumber("noprint_cheque_pay_bon", "0", Bean.terminalXML.getfieldTransl("NOPRINT_CHEQUE_PAY_BON", false)) %></td>
		</tr>
		<tr>
			<td><%=Bean.getCheckBoxNumber("card_type_active_emv", "1", Bean.terminalXML.getfieldTransl("card_type_active_emv", false)) %></td>
			<td><%=Bean.getCheckBoxNumber("oper_type_active_cheque", "1", Bean.terminalXML.getfieldTransl("oper_type_active_cheque", false)) %></td>
			<td><%=Bean.getCheckBoxNumber("noprint_cheque_check_param", "0", Bean.terminalXML.getfieldTransl("noprint_cheque_check_param", false)) %></td>
			<td colspan="2">&nbsp;</td>
		</tr>
		<tr>
			<td><%=Bean.getCheckBoxNumber("card_type_active_barcode", "1", Bean.terminalXML.getfieldTransl("card_type_active_barcode", false)) %></td>
			<td><%=Bean.getCheckBoxNumber("oper_type_active_emv", "1", Bean.terminalXML.getfieldTransl("OPER_TYPE_ACTIVE_EMV", false)) %></td>
			<td colspan="2">&nbsp;</td>
			<td colspan="2">&nbsp;</td>
		</tr>
			<tr>
				<td colspan="4" align="center">
					<%=Bean.getSubmitButtonAjax("../crm/clients/yurpersonupdate.jsp", "submit", "updateForm", "div_data_detail") %>
					<%=Bean.getResetButton() %>
					<%=Bean.getGoBackButton("../crm/clients/yurpersonspecs.jsp?id=" + id) %>
				</td>
			</tr>
		</table>
	</form>

		<%
		} else if (action.equalsIgnoreCase("edit")) {
			
			bcTerminalDeviceTypeObject device = new bcTerminalDeviceTypeObject(id_device_type);
			
	        %> 
			<%= Bean.getOperationTitleShort(
					"",
					Bean.terminalXML.getfieldTransl("h_update_devict_type", false),
					"Y",
					"N") 
			%>
			<script>
				var formData = new Array (
					new Array ('cd_term_type', 'varchar2', 1),
					new Array ('name_device_type', 'varchar2', 1),
					new Array ('work_with_certificate', 'varchar2', 1),
					new Array ('exist_flag', 'varchar2', 1),
					new Array ('term_code_page', 'varchar2', 1),
					new Array ('resp_time', 'varchar2', 1),
					new Array ('crypt_telgr', 'varchar2', 1),
					new Array ('ver_telgr', 'varchar2', 1),
					new Array ('vk_enc', 'varchar2', 1),
					new Array ('nincmax', 'varchar2', 1),
					new Array ('tr_limit', 'varchar2', 1)
				);
			</script>
        <form action="../crm/clients/yurpersonupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">
	        <input type="hidden" name="type" value="device">
    	    <input type="hidden" name="action" value="edit">
        	<input type="hidden" name="process" value="yes">
        	<input type="hidden" name="id" value="<%=id %>">
        	<input type="hidden" name="id_device_type" value="<%=id_device_type %>">
		
		<table <%=Bean.getTableDetailParam() %>>
    		<tr>
				<td><%= Bean.terminalXML.getfieldTransl("cd_term_type", false) %></td> <td><input type="text" name="name_term_type" size="53" value="<%=device.getValue("NAME_TERM_TYPE") %>" readonly="readonly" class="inputfield-ro"></td>
				<td><%= Bean.terminalXML.getfieldTransl("work_with_certificate", true) %></td> <td><select name="work_with_certificate" class="inputfield" > <%= Bean.getMeaningFromLookupNameOptions("YES_NO", device.getValue("WORK_WITH_CERTIFICATE"), true) %></select></td>
			</tr>
    		<tr>
				<td><%= Bean.terminalXML.getfieldTransl("name_device_type", true) %>
					<%= Bean.getGoToTerminalDeviceTypeLink(device.getValue("ID_DEVICE_TYPE")) %>
				</td> <td><input type="text" name="name_device_type" size="53" value="<%=device.getValue("NAME_DEVICE_TYPE") %>" class="inputfield"> </td>
				<td><%= Bean.terminalXML.getfieldTransl("exist_flag", true) %></td> <td><select name="exist_flag" class="inputfield" > <%= Bean.getMeaningFromLookupNameOptions("YES_NO", device.getValue("EXIST_FLAG"), true) %></select></td>
			</tr>
    		<tr>
				<td><%= Bean.terminalXML.getfieldTransl("desc_device_type", false) %></td> <td><textarea name="desc_device_type" cols="50" rows="3" class="inputfield"><%=device.getValue("DESC_DEVICE_TYPE") %></textarea></td>
			</tr>

		<tr>
			<td class="top_line" colspan="4"><b><%=Bean.terminalXML.getfieldTransl("h_term_setting_param", false) %></b></td>
		</tr>
		<tr>
			<td><%= Bean.terminalXML.getfieldTransl("TERM_CODE_PAGE", true) %></td> <td><select name="term_code_page"  class="inputfield"><%= Bean.getMeaningFromLookupNameOptions("TERMINAL_CODE_PAGE", device.getValue("TERM_CODE_PAGE"), true) %></select></td>
			<td><%= Bean.terminalXML.getfieldTransl("VER_TELGR", true) %></td> <td><select name="ver_telgr" class="inputfield" title="TEL_VERSION"><%= Bean.getMeaningFromLookupNameOptions("VER_TELGR", device.getValue("VER_TELGR"), true) %></select></td>
		</tr>
		<tr>
			<td><%= Bean.terminalXML.getfieldTransl("CRYPT_TELGR", true) %></td><td><select name="crypt_telgr" class="inputfield" title="CRYPT_TELGR"><%= Bean.getMeaningFromLookupNumHTML("YES_NO", device.getValue("CRYPT_TELGR"), true) %></select></td>
			<td valign="top"><%= Bean.terminalXML.getfieldTransl("nincmax", true) %></td>  <td><input type="text" name="nincmax" size="16" value="<%=device.getValue("NINCMAX") %>" class="inputfield" title="NINCMAX"></td>
		</tr>
		<tr>
			<td><%= Bean.terminalXML.getfieldTransl("RESP_TIME", true) %></td> <td><input type="text" name="resp_time" size="16" value="<%=device.getValue("RESP_TIME") %>" class="inputfield" title="RESP_TIME"></td>
			<td><%= Bean.terminalXML.getfieldTransl("connect_ekka", true) %></td> <td><select name="connect_ekka" class="inputfield" title="CONNECT_EKKA"><%= Bean.getMeaningFromLookupNumberOptions("YES_NO", device.getValue("CONNECT_EKKA"), true) %></select></td>
		</tr>
		<tr>
			<td valign="top"><%= Bean.terminalXML.getfieldTransl("VK_ENC", true) %></td> <td valign="top"><select name="vk_enc" class="inputfield" title="VK_ENC"><%= Bean.getMeaningFromLookupNameOptions("VER_KEY", device.getValue("VK_ENC"), true) %></select></td>
			<td valign="top"><%= Bean.terminalXML.getfieldTransl("tr_limit", true) %></td>  <td><input type="text" name="tr_limit" size="16" value="<%=device.getValue("TR_LIMIT") %>" class="inputfield" title="TR_LIMIT"></td>
		</tr>
		<tr>
			<td colspan="4">&nbsp;</td>
		</tr>

		<tr>
			<td><b><%= Bean.terminalXML.getfieldTransl("h_bon_cards_types", false) %></b></td>
			<td><b><%= Bean.terminalXML.getfieldTransl("h_operations_types", false) %></b></td>
			<td colspan="2"><b><%= Bean.terminalXML.getfieldTransl("h_print_cheque_settings", false) %></b></td>
		</tr>
		<tr>
			<td><%=Bean.getCheckBoxNumber("card_type_active_nsmep", device.getValue("CARD_TYPE_ACTIVE_NSMEP"), Bean.terminalXML.getfieldTransl("card_type_active_nsmep", false)) %></td>
			<td><%=Bean.getCheckBoxNumber("oper_type_active_nsmep", device.getValue("OPER_TYPE_ACTIVE_NSMEP"), Bean.terminalXML.getfieldTransl("oper_type_active_nsmep", false)) %></td>
			<td><%=Bean.getCheckBoxNumber("noprint_cheque_discount_club", device.getValue("NOPRINT_CHEQUE_DISCOUNT_CLUB"), Bean.terminalXML.getfieldTransl("noprint_cheque_discount_club", false)) %></td>
			<td><%=Bean.getCheckBoxNumber("noprint_cheque_pay_cash", device.getValue("NOPRINT_CHEQUE_PAY_CASH"), Bean.terminalXML.getfieldTransl("noprint_cheque_pay_cash", false)) %></td>
		</tr>
		<tr>
			<td><%=Bean.getCheckBoxNumber("card_type_active_magnetic", device.getValue("CARD_TYPE_ACTIVE_MAGNETIC"), Bean.terminalXML.getfieldTransl("card_type_active_magnetic", false)) %></td>
			<td><%=Bean.getCheckBoxNumber("oper_type_active_cash", device.getValue("OPER_TYPE_ACTIVE_CASH"), Bean.terminalXML.getfieldTransl("oper_type_active_cash", false)) %></td>
			<td><%=Bean.getCheckBoxNumber("noprint_cheque_bonus_club", device.getValue("NOPRINT_CHEQUE_BONUS_CLUB"), Bean.terminalXML.getfieldTransl("noprint_cheque_bonus_club", false)) %></td>
			<td><%=Bean.getCheckBoxNumber("noprint_cheque_pay_card", device.getValue("NOPRINT_CHEQUE_PAY_CARD"), Bean.terminalXML.getfieldTransl("noprint_cheque_pay_card", false)) %></td>
		</tr>
		<tr>
			<td><%=Bean.getCheckBoxNumber("card_type_active_chip", device.getValue("CARD_TYPE_ACTIVE_CHIP"), Bean.terminalXML.getfieldTransl("card_type_active_chip", false)) %></td>
			<td><%=Bean.getCheckBoxNumber("oper_type_active_bon", device.getValue("OPER_TYPE_ACTIVE_BON"), Bean.terminalXML.getfieldTransl("oper_type_active_bon", false)) %></td>
			<td><%=Bean.getCheckBoxNumber("noprint_cheque_mov_bon", device.getValue("NOPRINT_CHEQUE_MOV_BON"), Bean.terminalXML.getfieldTransl("noprint_cheque_mov_bon", false)) %></td>
			<td><%=Bean.getCheckBoxNumber("noprint_cheque_pay_bon", device.getValue("noprint_cheque_pay_bon"), Bean.terminalXML.getfieldTransl("NOPRINT_CHEQUE_PAY_BON", false)) %></td>
		</tr>
		<tr>
			<td><%=Bean.getCheckBoxNumber("card_type_active_emv", device.getValue("CARD_TYPE_ACTIVE_EMV"), Bean.terminalXML.getfieldTransl("card_type_active_emv", false)) %></td>
			<td><%=Bean.getCheckBoxNumber("oper_type_active_cheque", device.getValue("OPER_TYPE_ACTIVE_CHEQUE"), Bean.terminalXML.getfieldTransl("oper_type_active_cheque", false)) %></td>
			<td><%=Bean.getCheckBoxNumber("noprint_cheque_check_param", device.getValue("NOPRINT_CHEQUE_CHECK_PARAM"), Bean.terminalXML.getfieldTransl("noprint_cheque_check_param", false)) %></td>
			<td colspan="2">&nbsp;</td>
		</tr>
		<tr>
			<td><%=Bean.getCheckBoxNumber("card_type_active_barcode", device.getValue("CARD_TYPE_ACTIVE_BARCODE"), Bean.terminalXML.getfieldTransl("card_type_active_barcode", false)) %></td>
			<td><%=Bean.getCheckBoxNumber("oper_type_active_emv", device.getValue("OPER_TYPE_ACTIVE_EMV"), Bean.terminalXML.getfieldTransl("OPER_TYPE_ACTIVE_EMV", false)) %></td>
			<td colspan="2">&nbsp;</td>
			<td colspan="2">&nbsp;</td>
		</tr>
			<%=	Bean.getIdCreationAndMoficationRecordFields(
					device.getValue("ID_DEVICE_TYPE"),
					device.getValue(Bean.getCreationDateFieldName()),
					device.getValue("CREATED_BY"),
					device.getValue(Bean.getLastUpdateDateFieldName()),
					device.getValue("LAST_UPDATE_BY")
				) %>
			<tr>
				<td colspan="4" align="center">
					<%=Bean.getSubmitButtonAjax("../crm/clients/yurpersonupdate.jsp", "submit", "updateForm", "div_data_detail") %>
					<%=Bean.getResetButton() %>
					<%=Bean.getGoBackButton("../crm/clients/yurpersonspecs.jsp?id=" + id) %>
				</td>
			</tr>
		</table>
	</form>
		<%
			
		} else { %> 
			<%= Bean.getUnknownActionText(action) %><%
		}
	} else if (process.equalsIgnoreCase("yes")) {
	    
		if (action.equalsIgnoreCase("add")) {
			ArrayList<String> pParam = new ArrayList<String>();
					
			pParam.add(id);
			pParam.add(cd_term_type);
			pParam.add(name_device_type);
			pParam.add(desc_device_type);
			pParam.add(exist_flag);
			pParam.add(work_with_certificate);
			pParam.add(term_code_page);
			pParam.add(resp_time);
			pParam.add(ver_telgr);
			pParam.add(crypt_telgr);
			pParam.add(vk_enc);
			pParam.add(tr_limit);
			pParam.add(nincmax);
			pParam.add(connect_ekka);
			pParam.add(noprint_cheque_discount_club);
			pParam.add(noprint_cheque_bonus_club);
			pParam.add(noprint_cheque_mov_bon);
			pParam.add(noprint_cheque_check_param);
			pParam.add(noprint_cheque_pay_cash);
			pParam.add(noprint_cheque_pay_card);
			pParam.add(noprint_cheque_pay_bon);
			pParam.add(card_type_active_nsmep);
			pParam.add(card_type_active_magnetic);
			pParam.add(card_type_active_chip);
			pParam.add(card_type_active_emv);
			pParam.add(card_type_active_barcode);
			pParam.add(oper_type_active_nsmep);
			pParam.add(oper_type_active_cash);
			pParam.add(oper_type_active_bon);
			pParam.add(oper_type_active_cheque);
			pParam.add(oper_type_active_emv);
		
			%>
			<%= Bean.executeInsertFunction("PACK$TERM_DEVICE_TYPE_UI.add_device_type", pParam, "../crm/clients/yurpersonspecs.jsp?id=" + id + "&id_device_type=", "") %>
			<%

		} else if (action.equalsIgnoreCase("remove")) {
			ArrayList<String> pParam = new ArrayList<String>();
					
			pParam.add(id_device_type);

		 	%>
			<%= Bean.executeDeleteFunction("PACK$TERM_DEVICE_TYPE_UI.delete_device_type", pParam, "../crm/clients/yurpersonspecs.jsp?id=" + id, "") %>
			<% 	

		} else if (action.equalsIgnoreCase("edit")) {
	 		
	 		ArrayList<String> pParam = new ArrayList<String>();
						
			pParam.add(id_device_type);
			pParam.add(id);
			//pParam.add(cd_term_type);
			pParam.add(name_device_type);
			pParam.add(desc_device_type);
			pParam.add(exist_flag);
			pParam.add(work_with_certificate);
			pParam.add(term_code_page);
			pParam.add(resp_time);
			pParam.add(ver_telgr);
			pParam.add(crypt_telgr);
			pParam.add(vk_enc);
			pParam.add(tr_limit);
			pParam.add(nincmax);
			pParam.add(connect_ekka);
			pParam.add(noprint_cheque_discount_club);
			pParam.add(noprint_cheque_bonus_club);
			pParam.add(noprint_cheque_mov_bon);
			pParam.add(noprint_cheque_check_param);
			pParam.add(noprint_cheque_pay_cash);
			pParam.add(noprint_cheque_pay_card);
			pParam.add(noprint_cheque_pay_bon);
			pParam.add(card_type_active_nsmep);
			pParam.add(card_type_active_magnetic);
			pParam.add(card_type_active_chip);
			pParam.add(card_type_active_emv);
			pParam.add(card_type_active_barcode);
			pParam.add(oper_type_active_nsmep);
			pParam.add(oper_type_active_cash);
			pParam.add(oper_type_active_bon);
			pParam.add(oper_type_active_cheque);
			pParam.add(oper_type_active_emv);

			%>
			<%= Bean.executeUpdateFunction("PACK$TERM_DEVICE_TYPE_UI.update_device_type", pParam, "../crm/clients/yurpersonspecs.jsp?id=" + id, "") %>
			<%

		} else { %> 
			<%= Bean.getUnknownActionText(action) %><%
		}
	} else {
	    %> <%= Bean.getUnknownProcessText(process) %> <%
	}
}  else if (type.equalsIgnoreCase("access")) {
	if (process.equalsIgnoreCase("yes")) {
		if (action.equalsIgnoreCase("set")) {%>
    	
	   		<%= Bean.form_messageXML.getfieldTransl("processing_update", false) %>
	   	   	<% 
	    
	    	ArrayList<String> id_value=new ArrayList<String>();
			ArrayList<String> prv_value=new ArrayList<String>();
	
	    	String callSQL = "";
	    	Set<String> keySet = parameters.keySet();
			Iterator<String> keySetIterator = keySet.iterator();
			String key = "";
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
	   						"../crm/clients/yurpersonupdate.jsp",
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
	   	    String[] results = new String[4];
	   	    
	   	 	String[] pParam = new String [3];
	
	   	    if (id_value.size()>0) {
	  	 		 for(int counter=0;counter<id_value.size();counter++){ 
	  	 			 if (!(prv_value.contains(id_value.get(counter)))) {
	  	 				 
	  	 				String id_club = id_value.get(counter).substring(0,id_value.get(counter).indexOf("_"));
	  	 				String id_user = id_value.get(counter).substring(id_value.get(counter).indexOf("_")+1);
	  	 				
	  	 				callSQL = "{? = call " + Bean.getGeneralDBScheme() + 
			        		".PACK$JUR_PRS_UI.add_user_jur_prs(?,?,?,?)}";
			        	
			        	pParam[0] = id_user;
			        	pParam[1] = id_club;
			        	pParam[2] = id;
				
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
					}
	  	 		}
			}
	   	 	if (prv_value.size()>0) {
	   	 		for(int counter=0;counter<prv_value.size();counter++){ 
				 	if (!(id_value.contains(prv_value.get(counter)))) {
				   	 				 
				 		String id_club = prv_value.get(counter).substring(0,prv_value.get(counter).indexOf("_"));
	  	 				String id_user = prv_value.get(counter).substring(prv_value.get(counter).indexOf("_")+1);
	  	 							 
			        	callSQL = "{? = call " + Bean.getGeneralDBScheme() + 
			        		".PACK$JUR_PRS_UI.delete_user_jur_prs(?,?,?,?)}";
				        	
				        pParam[0] = id_user;
				        pParam[1] = id_club;
				        pParam[2] = id;
				
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
				 	}
	  	 		 }
	   	 		
	   	 	}
	
	   	 	%>
	  	    <%=Bean.showCallResult(
	   	    		callSQL, 
	   	    		resultFull, 
	   	    		resultMessageFull, 
	   	    		"../crm/clients/yurpersonspecs.jsp?id=" + id, 
	   	    		"../crm/clients/yurpersonspecs.jsp?id=" + id, 
	   	    		Bean.form_messageXML.getfieldTransl("save_error", false)) %>
	   		<% 
	
	    } else { %> 
	    	<%= Bean.getUnknownActionText(action) %><% 
	    }
	} else {
	    %> <%= Bean.getUnknownProcessText(process) %> <%
	}
} else if (type.equalsIgnoreCase("nomenkl")) {
	
	String 
		id_club 				= Bean.getDecodeParam(parameters.get("id_club")),
		id_jur_prs_nomenkl 		= Bean.getDecodeParam(parameters.get("id_jur_prs_nomenkl")),
		cd_jur_prs_nomenkl 		= Bean.getDecodeParam(parameters.get("cd_jur_prs_nomenkl")),
		name_jur_prs_nomenkl 	= Bean.getDecodeParam(parameters.get("name_jur_prs_nomenkl")),
		begin_action_date 		= Bean.getDecodeParam(parameters.get("begin_action_date")),
		end_action_date 		= Bean.getDecodeParam(parameters.get("end_action_date")),
		cd_nomenkl_unit	 		= Bean.getDecodeParam(parameters.get("cd_nomenkl_unit"));

	if (process.equalsIgnoreCase("no")) {
		if (action.equalsIgnoreCase("add")) {
    		if (id_club==null || "".equalsIgnoreCase(id_club)) {
    			id_club = Bean.getCurrentClubID();
    		}
			bcClubShortObject club = new bcClubShortObject(id_club);
	        
	        %>
			<script>
				var formData = new Array (
					new Array ('cd_jur_prs_nomenkl', 'varchar2', 1),
					new Array ('name_jur_prs_nomenkl', 'varchar2', 1),
					new Array ('cd_nomenkl_unit', 'varchar2', 1),
					new Array ('name_club', 'varchar2', 1)
				);
			</script>

		<%= Bean.getOperationTitleShort(
				"",
				Bean.jurpersonXML.getfieldTransl("h_add_nomenkl", false),
				"Y",
				"Y") 
		%>
        <form action="../crm/clients/yurpersonupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">
	        <input type="hidden" name="type" value="nomenkl">
	        <input type="hidden" name="action" value="add">
	        <input type="hidden" name="process" value="yes">
	        <input type="hidden" name="id" value="<%=id %>">
		<table <%=Bean.getTableDetailParam() %>>
		<tr>
			<td><%=Bean.jurpersonXML.getfieldTransl("cd_jur_prs_nomenkl", true)%> </td><td><input type="text" name="cd_jur_prs_nomenkl" size="20" value="" class="inputfield"></td>
 		    <td><%= Bean.clubXML.getfieldTransl("club", true) %>
				<%= Bean.getGoToClubLink(club.getValue("ID_CLUB")) %>
			</td>
		  	<td>
				<%=Bean.getWindowFindClub("club", club.getValue("ID_CLUB"), club.getValue("SNAME_CLUB"), "25") %>
		  	</td>
		</tr>
		<tr>
			<td><%=Bean.jurpersonXML.getfieldTransl("name_jur_prs_nomenkl", true)%> </td><td><input type="text" name="name_jur_prs_nomenkl" size="40" value="" class="inputfield"></td>
			<td><%=Bean.jurpersonXML.getfieldTransl("begin_action_date", false)%></td> <td><%=Bean.getCalendarInputField("begin_action_date", Bean.getSysDate(), "10") %></td>
		</tr>
		<tr>
			<td><%= Bean.jurpersonXML.getfieldTransl("cd_nomenkl_unit", true) %></td><td><select name="cd_nomenkl_unit" class="inputfield"><%= Bean.getNomenklUnitOptions("", true) %> </select></td>
			<td><%=Bean.jurpersonXML.getfieldTransl("end_action_date", false)%></td> <td><%=Bean.getCalendarInputField("end_action_date", "", "10") %></td>
		</tr>
 		<tr>
			<td colspan="6" align="center">
				<%=Bean.getSubmitButtonAjax("../crm/clients/yurpersonupdate.jsp", "submit", "updateForm", "div_data_detail") %>
				<%=Bean.getResetButton() %>
				<%=Bean.getGoBackButton("../crm/clients/yurpersonspecs.jsp?id=" + id) %>
			</td>
		</tr>

	</table>
	
	<!-- Скрипт для втавки меню вибору дати -->
		<%= Bean.getCalendarScript("begin_action_date", false) %>
		<%= Bean.getCalendarScript("end_action_date", false) %>

	</form>

	        <%
    	} else if (action.equalsIgnoreCase("edit")) {
    		
    		bcJurPrsNomenklatureObject nomenkl = new bcJurPrsNomenklatureObject(id_jur_prs_nomenkl);
	        
	        %>
			<script>
				var formData = new Array (
					new Array ('cd_jur_prs_nomenkl', 'varchar2', 1),
					new Array ('name_jur_prs_nomenkl', 'varchar2', 1),
					new Array ('cd_nomenkl_unit', 'varchar2', 1),
					new Array ('name_club', 'varchar2', 1)
				);
			</script>

		<%= Bean.getOperationTitleShort(
				"",
				Bean.jurpersonXML.getfieldTransl("h_update_nomenkl", false),
				"Y",
				"Y") 
		%>
        <form action="../crm/clients/yurpersonupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">
	        <input type="hidden" name="type" value="nomenkl">
	        <input type="hidden" name="action" value="edit">
	        <input type="hidden" name="process" value="yes">
	        <input type="hidden" name="id" value="<%=id %>">
	        <input type="hidden" name="id_jur_prs_nomenkl" value="<%=nomenkl.getValue("ID_JUR_PRS_NOMENKL") %>">
		<table <%=Bean.getTableDetailParam() %>>
		<tr>
			<td><%=Bean.jurpersonXML.getfieldTransl("cd_jur_prs_nomenkl", true)%> </td><td><input type="text" name="cd_jur_prs_nomenkl" size="20" value="<%=nomenkl.getValue("CD_JUR_PRS_NOMENKL")%>" class="inputfield"></td>
			<td><%=Bean.clubXML.getfieldTransl("club", false)%>
				<%= Bean.getGoToClubLink(nomenkl.getValue("ID_CLUB")) %>
			</td> 
			<td><input type="text" name="name_club" size="50" value="<%= Bean.getClubShortName(nomenkl.getValue("ID_CLUB"))%>" readonly="readonly" class="inputfield-ro"> </td>
		</tr>
		<tr>
			<td><%=Bean.jurpersonXML.getfieldTransl("name_jur_prs_nomenkl", true)%> </td><td><input type="text" name="name_jur_prs_nomenkl" size="40" value="<%=nomenkl.getValue("NAME_JUR_PRS_NOMENKL")%>" class="inputfield"></td>
			<td><%=Bean.jurpersonXML.getfieldTransl("begin_action_date", false)%></td> <td><%=Bean.getCalendarInputField("begin_action_date", nomenkl.getValue("BEGIN_ACTION_DATE_FRMT"), "10") %></td>
		</tr>
		<tr>
			<td><%= Bean.jurpersonXML.getfieldTransl("cd_nomenkl_unit", true) %></td><td><select name="cd_nomenkl_unit" class="inputfield"><%= Bean.getNomenklUnitOptions(nomenkl.getValue("CD_NOMENKL_UNIT"), true) %> </select></td>
			<td><%=Bean.jurpersonXML.getfieldTransl("end_action_date", false)%></td> <td><%=Bean.getCalendarInputField("end_action_date", nomenkl.getValue("END_ACTION_DATE_FRMT"), "10") %></td>
		</tr>
		<%=	Bean.getIdCreationAndMoficationRecordFields(
				nomenkl.getValue("ID_JUR_PRS_NOMENKL"),
				nomenkl.getValue(Bean.getCreationDateFieldName()),
				nomenkl.getValue("CREATED_BY"),
				nomenkl.getValue(Bean.getLastUpdateDateFieldName()),
				nomenkl.getValue("LAST_UPDATE_BY")
			) %>
 		<tr>
			<td colspan="6" align="center">
				<%=Bean.getSubmitButtonAjax("../crm/clients/yurpersonupdate.jsp", "submit", "updateForm", "div_data_detail") %>
				<%=Bean.getResetButton() %>
				<%=Bean.getGoBackButton("../crm/clients/yurpersonspecs.jsp?id=" + id) %>
			</td>
		</tr>

	</table>
	
	<!-- Скрипт для втавки меню вибору дати -->
		<%= Bean.getCalendarScript("begin_action_date", false) %>
		<%= Bean.getCalendarScript("end_action_date", false) %>

	</form>
		 
		<%} else {
    	    %> <%= Bean.getUnknownActionText(action) %><%
    	}
	} else if (process.equalsIgnoreCase("yes")) {
		if (action.equalsIgnoreCase("add")) { 

			String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK$JUR_PRS_UI.add_nomenkl("+
				"?,?,?,?,?,?,?,?,?,?)}";

			String[] pParam = new String [8];
					
			pParam[0] = id;
			pParam[1] = id_club;
			pParam[2] = cd_jur_prs_nomenkl;
			pParam[3] = name_jur_prs_nomenkl;
			pParam[4] = cd_nomenkl_unit;
			pParam[5] = begin_action_date;
			pParam[6] = end_action_date;
			pParam[7] = Bean.getDateFormat();
			
		 	%>
			<%= Bean.getCallResultParam("INSERT", callSQL, pParam, "../crm/clients/yurpersonspecs.jsp?id=" + id + "&id_jur_prs_nomenkl=", "") %>
			<% 	
	   
		} else if (action.equalsIgnoreCase("remove")) { 
		   
			String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK$JUR_PRS_UI.delete_nomenkl(?,?)}";

			String[] pParam = new String [1];
						
			pParam[0] = id_jur_prs_nomenkl;

			%>
			<%= Bean.getCallResultParam("DELETE", callSQL, pParam, "../crm/clients/yurpersonspecs.jsp?id=" + id, "") %>
			<% 	
		     
		} else if (action.equalsIgnoreCase("edit")) { 
			
		 	String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK$JUR_PRS_UI.update_nomenkl(" + 
		 		"?,?,?,?,?,?,?,?)}";

			String[] pParam = new String [7];
						
			pParam[0] = id_jur_prs_nomenkl;
			pParam[1] = cd_jur_prs_nomenkl;
			pParam[2] = name_jur_prs_nomenkl;
			pParam[3] = cd_nomenkl_unit;
			pParam[4] = begin_action_date;
			pParam[5] = end_action_date;
			pParam[6] = Bean.getDateFormat();
			
		 	%>
			<%= Bean.getCallResultParam("UPDATE", callSQL, pParam, "../crm/clients/yurpersonspecs.jsp?id=" + id, "") %>
			<% 	
		
		} else { %>
			<%= Bean.getUnknownActionText(action) %><% 
		}
	} else { %>
		<%= Bean.getUnknownProcessText(process) %> <br><% 
	}
}  else {
    %><%= Bean.getUnknownTypeText(type) %><%
}
%>


</body>
</html>
