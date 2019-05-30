<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%@page import="bc.objects.bcClubShortObject"%>
<%@page import="bc.objects.bcJurPrsObject"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.ArrayList"%><html>

<%= Bean.getLogOutScript(request) %>

<head>
	<%= Bean.getMetaContent() %>
	<%= Bean.getBottomFrameCSS() %>
	
</head>

			<script>
				var formData = new Array (
					new Array ('name_service_place', 'varchar2', 1),
					new Array ('club_date_beg', 'varchar2', 1)
				);
			</script>


<body>
<%
HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String pageFormName = "CLIENTS_YURPERSONS";

Bean.setJspPageForTabName(pageFormName);

request.setCharacterEncoding("UTF-8");
String 
	id					= Bean.getDecodeParamPrepare(parameters.get("id_jur_prs")),
	id_service_place	= Bean.getDecodeParamPrepare(parameters.get("id_service_place")),
	action				= Bean.getDecodeParamPrepare(parameters.get("action")),
	process				= Bean.getDecodeParamPrepare(parameters.get("process")),
	cd_jur_prs_status	= Bean.getDecodeParamPrepare(parameters.get("cd_jur_prs_status"));

if (process.equalsIgnoreCase("no"))
/* вибираємо тип дії (добавити, видалити...)*/
	{
	   /*  --- Добавити запис --- */
    	if (action.equalsIgnoreCase("add")) {
    		%>
			<%
			bcClubShortObject club = new bcClubShortObject(Bean.getCurrentClubID());
    		
    		bcJurPrsObject jurprs = new bcJurPrsObject(id);
	        %>

			<script language="JavaScript">
				var formData = new Array (
					new Array ('name_service_place', 'varchar2', 1),
					new Array ('name_club', 'varchar2', 1),
					new Array ('cd_club_member_status', 'varchar2', 1),
					new Array ('club_date_beg', 'varchar2', 1)
				);
				function myValidateForm() {
					return validateForm(formData);
				}
				function copyJurPrsFactAddress(){
	    	    	document.getElementById('ADR_ZIP_CODE').value = '<%=jurprs.getValue("FACT_ADR_ZIP_CODE")%>';
	    	    	document.getElementById('ADR_STREET').value = '<%=jurprs.getValue("FACT_ADR_STREET")%>';
	    	    	document.getElementById('ADR_HOUSE').value = '<%=jurprs.getValue("FACT_ADR_HOUSE")%>';
	    	    	document.getElementById('ADR_CASE').value = '<%=jurprs.getValue("FACT_ADR_CASE")%>';
	    	    	document.getElementById('ADR_APARTMENT').value = '<%=jurprs.getValue("FACT_ADR_APARTMENT")%>';
	    	    	dwr_make_ser_place_adr_copy('<%=jurprs.getValue("FACT_ADR_CODE_COUNTRY")%>', '<%=jurprs.getValue("FACT_ADR_ID_OBLAST")%>', '<%=Bean.getSessionId() %>');
	    		}
	    	    dwr_make_ser_place_adr('<%=Bean.getSessionId() %>');
			</script> 
		<script>
			
			function myValidateForm() {
				var formData = new Array (
					new Array ('name_service_place', 'varchar2', 1),
					new Array ('sname_service_place', 'varchar2', 1),
					new Array ('id_jur_prs_form', 'varchar2', 1),
					new Array ('name_club', 'varchar2', 1),
					new Array ('club_date_beg', 'varchar2', 1),
					new Array ('cd_club_member_status', 'varchar2', 1)
				);
				return validateForm(formGeneral);
			}
		</script>

			<%= Bean.getOperationTitleShort(
					"",
					Bean.jurpersonXML.getfieldTransl("LAB_ADD_SERVICE_PLACE", false),
					"Y",
					"Y") 
			%>
        <form action="../crm/clients/yurpersonserviceplaceupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">
	        <input type="hidden" name="action" value="add">
	        <input type="hidden" name="process" value="yes">
	        <input type="hidden" name="id_jur_prs" value="<%=id %>">
		<table <%=Bean.getTableDetailParam() %>>

		<tr>
			<td><%= Bean.jurpersonXML.getfieldTransl("name_jur_prs_form", true) %></td> <td><select name="id_jur_prs_form" class="inputfield"><%= Bean.getJurPrsFormOptions("", true) %></select></td>
			<td><%= Bean.clubXML.getfieldTransl("club", true) %>
				<%= Bean.getGoToClubLink(club.getValue("ID_CLUB")) %>
			</td> 
			<td>
				<%=Bean.getWindowFindClub("club", club.getValue("ID_CLUB"), club.getValue("SNAME_CLUB"),"35") %>
			</td>
		</tr>
		<tr>
			<td>&nbsp;</td>
			<td>
				<%=Bean.getCheckBox("is_resident", "", Bean.jurpersonXML.getfieldTransl("is_resident", false)) %>
			</td>
			<td><%= Bean.jurpersonXML.getfieldTransl("club_date_beg", true) %></td> <td><%=Bean.getCalendarInputField("club_date_beg", jurprs.getValue("DATE_BEG_DF"), "10") %></td>
		</tr>
		<tr>
			<td><%= Bean.jurpersonXML.getfieldTransl("name_service_place", true) %></td> <td><input type="text" name="name_service_place" size="60" value="" class="inputfield"></td>
			<td><%= Bean.jurpersonXML.getfieldTransl("club_date_end", false) %></td> <td><%=Bean.getCalendarInputField("club_date_end", "", "10") %></td>
		</tr>
		<tr>
			<td><%= Bean.jurpersonXML.getfieldTransl("sname_service_place", true) %></td> <td><input type="text" name="sname_service_place" size="60" value="" class="inputfield"></td>
			<td><%= Bean.clubXML.getfieldTransl("club_member_status", true) %></td><td><select name="cd_club_member_status" class="inputfield"> <%= Bean.getClubMemberStatusOptions("", true) %> </select></td>
		</tr>
		<tr>
			<td><%= Bean.jurpersonXML.getfieldTransl("title_partner", false) %></td> <td><input type="text" name="name_jur_prs" size="60" value="<%=Bean.getJurPersonShortName(id) %>" readonly="readonly" class="inputfield-ro"> </td>
			<td colspan="2">&nbsp;</td>
		</tr>
		<tr>								
			<td colspan=8 class="top_line"><b><%= Bean.jurpersonXML.getfieldTransl("fact_adr_full", false) %></b>
			&nbsp;&nbsp;<button type="button" class="button" onclick="copyJurPrsFactAddress(); "><%= Bean.jurpersonXML.getfieldTransl("button_copy_fact_address", false) %> </button>
			</td>
		</tr>
		<tr>								
			<td><%= Bean.jurpersonXML.getfieldTransl("name_country", false) %></td><td><select name="ADR_CODE_COUNTRY" id="ADR_CODE_COUNTRY" class="inputfield" onchange="dwr_oblast_array('ADR_ID_OBLAST', document.getElementById('ADR_CODE_COUNTRY').value, document.getElementById('ADR_ID_OBLAST').value, '<%=Bean.getSessionId() %>');"><%= Bean.getCountryOptions(club.getValue("CODE_COUNTRY_DEF"), false) %></select></td>
			<td><%= Bean.jurpersonXML.getfieldTransl("ADR_CITY", false) %></td><td><input type="text" name="ADR_CITY" id="ADR_CITY" size="25" value=""  class="inputfield"></td>				
		</tr>
		<tr>
			<td><%= Bean.jurpersonXML.getfieldTransl("ADR_ZIP_CODE", false) %></td><td><input type="text" name="ADR_ZIP_CODE" id="ADR_ZIP_CODE" size="25" value=""  class="inputfield"></td>
			<td><%= Bean.jurpersonXML.getfieldTransl("ADR_CITY_DISTRICT", false) %></td><td><input type="text" name="ADR_CITY_DISTRICT" id="ADR_CITY_DISTRICT" size="25" value=""  class="inputfield"></td>				
		</tr>
		<tr>
			<td><%= Bean.jurpersonXML.getfieldTransl("ADR_OBLAST", false) %></td><td><select name="ADR_ID_OBLAST" id="ADR_ID_OBLAST" class="inputfield" onchange="dwr_make_ser_place_adr('<%=Bean.getSessionId() %>');"><option value=""></option></select></td>				
			<td><%= Bean.jurpersonXML.getfieldTransl("ADR_STREET", false) %></td><td><input type="text" name="ADR_STREET" id="ADR_STREET" size="25" value="" class="inputfield"></td>
		</tr>
		<tr>
			<td><%= Bean.jurpersonXML.getfieldTransl("ADR_DISTRICT", false) %></td><td><input type="text" name="ADR_DISTRICT" id="ADR_DISTRICT" size="25" value=""  class="inputfield"></td>				
			<td><%= Bean.jurpersonXML.getfieldTransl("ADR_HOUSE_CASE_APARTMENT", false) %></td><td><input type="text" name="ADR_HOUSE" id="ADR_HOUSE" size="6" value=""  class="inputfield">/<input type="text" name="ADR_CASE" id="ADR_CASE" size="6" value=""  class="inputfield">/<input type="text" name="ADR_APARTMENT" id="ADR_APARTMENT" size="6" value=""  class="inputfield"></td>
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
		</tr>
		<tr>								
			<td colspan="4" class="top_line"><b><%= Bean.jurpersonXML.getfieldTransl("title_others", false) %></b></td>
		</tr>
		<tr>								
			<td><%= Bean.jurpersonXML.getfieldTransl("name_service_place_in_sms", false) %></td><td><input type="text" name="name_jur_prs_in_sms" id="name_jur_prs_in_sms" size="45" value=""  class="inputfield"></td>
			<td colspan="2">&nbsp;</td>
		</tr>
		<tr>
			<td colspan="8" align="center">
				<%=Bean.getSubmitButtonAjax("../crm/clients/yurpersonserviceplaceupdate.jsp", "submit", "updateForm", "div_data_detail") %>
				<%=Bean.getResetButton() %>
				<%=Bean.getGoBackButton("../crm/clients/yurpersonspecs.jsp?id=" + id) %>
			</td>
		</tr>

	</table>

	</form>
	
		<%= Bean.getCalendarScript("club_date_beg", false) %>
		<%= Bean.getCalendarScript("club_date_end", false) %>


	        <%
	        /*  --- Видалити запис ---  */
    	} else if (action.equalsIgnoreCase("edit")) {
    		
    		bcJurPrsObject serviceplace = new bcJurPrsObject(id_service_place);
    		
    		bcJurPrsObject jurprs = new bcJurPrsObject(serviceplace.getValue("ID_JUR_PRS_PARENT"));
    		
    	    %> 
		<script language="JavaScript">
			function myValidateForm() {
				var formData = new Array (
					new Array ('name_service_place', 'varchar2', 1),
					new Array ('sname_service_place', 'varchar2', 1),
					new Array ('id_jur_prs_form', 'varchar2', 1),
					new Array ('cd_club_service_place', 'varchar2', 1),
					new Array ('club_date_beg', 'varchar2', 1),
					new Array ('cd_club_member_status', 'varchar2', 1)
				);
				return validateForm(formData);
			}
    	    function copyJurPrsFactAddress(){
    	    	document.getElementById('ADR_ZIP_CODE').value = '<%=jurprs.getValue("FACT_ADR_ZIP_CODE")%>';
    	    	document.getElementById('ADR_DISTRICT').value = '<%=jurprs.getValue("FACT_ADR_STREET")%>';
    	    	document.getElementById('ADR_CITY').value = '<%=jurprs.getValue("FACT_ADR_STREET")%>';
    	    	document.getElementById('ADR_CITY_DISTRICT').value = '<%=jurprs.getValue("FACT_ADR_STREET")%>';
    	    	document.getElementById('ADR_STREET').value = '<%=jurprs.getValue("FACT_ADR_STREET")%>';
    	    	document.getElementById('ADR_HOUSE').value = '<%=jurprs.getValue("FACT_ADR_HOUSE")%>';
    	    	document.getElementById('ADR_CASE').value = '<%=jurprs.getValue("FACT_ADR_CASE")%>';
    	    	document.getElementById('ADR_APARTMENT').value = '<%=jurprs.getValue("FACT_ADR_APARTMENT")%>';
    	    	dwr_make_ser_place_adr_copy('<%=jurprs.getValue("FACT_ADR_CODE_COUNTRY")%>', '<%=jurprs.getValue("FACT_ADR_ID_OBLAST")%>', '<%=Bean.getSessionId() %>');
    		}
    	    dwr_make_ser_place_adr('<%=Bean.getSessionId() %>');
    	    
		</script> 


			<%= Bean.getOperationTitleShort(
					"",
					Bean.jurpersonXML.getfieldTransl("LAB_EDIT_SERVICE_PLACE", false),
					"Y",
					"Y") 
			%>
    	<form action="../crm/clients/yurpersonserviceplaceupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">
	        <input type="hidden" name="action" value="edit">
	        <input type="hidden" name="process" value="yes">
	        <input type="hidden" name="id_jur_prs" value="<%=serviceplace.getValue("ID_JUR_PRS_PARENT") %>">
	        <input type="hidden" name="id_service_place" value="<%=serviceplace.getValue("ID_JUR_PRS") %>">
	        <input type="hidden" name="LUD" value="<%=serviceplace.getValue("LUD") %>">
		<table <%=Bean.getTableDetailParam() %>>

		<tr>
			<td><%= Bean.jurpersonXML.getfieldTransl("name_jur_prs_form", true) %></td> <td><select name="id_jur_prs_form" class="inputfield"><%= Bean.getJurPrsFormOptions(serviceplace.getValue("ID_JUR_PRS_FORM"), true) %></select></td>
			<td><%= Bean.clubXML.getfieldTransl("club", false) %>
				<%= Bean.getGoToClubLink(serviceplace.getValue("ID_CLUB")) %>
			</td> 
			<td>
				<input type="hidden" name="id_club" size="35" value="<%=serviceplace.getValue("ID_CLUB") %>">
				<input type="text" name="name_club" size="35" value="<%=Bean.getClubShortName(serviceplace.getValue("ID_CLUB")) %>" readonly="readonly" class="inputfield-ro">
			</td>
		</tr>
		<tr>
			<td>&nbsp;</td>
			<td>
				<%=Bean.getCheckBox("is_resident", serviceplace.getValue("IS_RESIDENT"), Bean.jurpersonXML.getfieldTransl("is_resident", false)) %>
			</td>
			<td><%= Bean.jurpersonXML.getfieldTransl("cd_club_service_place", true) %></td> <td><input type="text" name="cd_club_service_place" size="10" value="<%=serviceplace.getValue("CD_CLUB_JUR_PRS") %>" class="inputfield"></td>
		</tr>
		<tr>
			<td><%= Bean.jurpersonXML.getfieldTransl("name_service_place", true) %></td> <td><input type="text" name="name_service_place" size="60" value="<%=serviceplace.getValue("NAME_JUR_PRS") %>" class="inputfield"></td>
			<td><%= Bean.jurpersonXML.getfieldTransl("club_date_beg", true) %></td> <td><%=Bean.getCalendarInputField("club_date_beg", serviceplace.getValue("DATE_BEG_FRMT"), "10") %></td>
		</tr>
		<tr>
			<td><%= Bean.jurpersonXML.getfieldTransl("sname_service_place", true) %></td> <td><input type="text" name="sname_service_place" size="60" value="<%=serviceplace.getValue("SNAME_JUR_PRS") %>" class="inputfield"></td>
			<td><%= Bean.jurpersonXML.getfieldTransl("club_date_end", false) %></td> <td><%=Bean.getCalendarInputField("club_date_end", serviceplace.getValue("DATE_END_FRMT"), "10") %></td>
		</tr>
		<tr>
			<td><%= Bean.jurpersonXML.getfieldTransl("title_partner", false) %>
				<%=Bean.getGoToJurPrsHyperLink(serviceplace.getValue("ID_JUR_PRS_PARENT")) %>
			</td> <td><input type="text" name="name_jur_prs" size="60" value="<%=serviceplace.getValue("SNAME_JUR_PRS_PARENT") %>" readonly="readonly" class="inputfield-ro"> </td>
			<td><%= Bean.clubXML.getfieldTransl("club_member_status", true) %></td><td><select name="cd_club_member_status" class="inputfield"> <%= Bean.getClubMemberStatusOptions(serviceplace.getValue("CD_CLUB_MEMBER_STATUS"), true) %> </select></td>
		</tr>
		<tr>								
			<td colspan=8 class="top_line"><b><%= Bean.jurpersonXML.getfieldTransl("fact_adr_full", false) %></b>
			&nbsp;&nbsp;<button type="button" class="button" onclick="copyJurPrsFactAddress(); "><%= Bean.jurpersonXML.getfieldTransl("button_copy_fact_address", false) %> </button>
			</td>
		</tr>
		<tr>								
			<td><%= Bean.jurpersonXML.getfieldTransl("name_country", false) %></td><td><select name="ADR_CODE_COUNTRY" id="ADR_CODE_COUNTRY" class="inputfield" onchange="dwr_oblast_array('ADR_ID_OBLAST', document.getElementById('ADR_CODE_COUNTRY').value, document.getElementById('ADR_ID_OBLAST').value, '<%=Bean.getSessionId() %>');"><%= Bean.getCountryOptions(serviceplace.getValue("FACT_ADR_CODE_COUNTRY"), false) %></select></td>
			<td><%= Bean.jurpersonXML.getfieldTransl("ADR_CITY", false) %></td><td><input type="text" name="ADR_CITY" id="ADR_CITY" size="25" value="<%=serviceplace.getValue("FACT_ADR_CITY") %>"  class="inputfield"></td>				
		</tr>
		<tr>
			<td><%= Bean.jurpersonXML.getfieldTransl("ADR_ZIP_CODE", false) %></td><td><input type="text" name="ADR_ZIP_CODE" id="ADR_ZIP_CODE" size="25" value="<%=serviceplace.getValue("FACT_ADR_ZIP_CODE") %>"  class="inputfield"></td>
			<td><%= Bean.jurpersonXML.getfieldTransl("ADR_CITY_DISTRICT", false) %></td><td><input type="text" name="ADR_CITY_DISTRICT" id="ADR_CITY_DISTRICT" size="25" value="<%=serviceplace.getValue("FACT_ADR_CITY_DISTRICT") %>"  class="inputfield"></td>				
		</tr>
		<tr>
			<td><%= Bean.jurpersonXML.getfieldTransl("ADR_OBLAST", false) %></td><td><select name="ADR_ID_OBLAST" id="ADR_ID_OBLAST" class="inputfield"><option value="<%=serviceplace.getValue("FACT_ADR_ID_OBLAST") %>"></option></select></td>				
			<td><%= Bean.jurpersonXML.getfieldTransl("ADR_STREET", false) %></td><td><input type="text" name="ADR_STREET" id="ADR_STREET" size="25" value="<%=serviceplace.getValue("FACT_ADR_STREET") %>" class="inputfield"></td>
		</tr>
		<tr>
			<td><%= Bean.jurpersonXML.getfieldTransl("ADR_DISTRICT", false) %></td><td><input type="text" name="ADR_DISTRICT" id="ADR_DISTRICT" size="25" value="<%=serviceplace.getValue("FACT_ADR_DISTRICT") %>"  class="inputfield"></td>				
			<td><%= Bean.jurpersonXML.getfieldTransl("ADR_HOUSE_CASE_APARTMENT", false) %></td><td><input type="text" name="ADR_HOUSE" id="ADR_HOUSE" size="6" value="<%=serviceplace.getValue("FACT_ADR_HOUSE") %>"  class="inputfield">/<input type="text" name="ADR_CASE" id="ADR_CASE" size="6" value="<%=serviceplace.getValue("FACT_ADR_CASE") %>"  class="inputfield">/<input type="text" name="ADR_APARTMENT" id="ADR_APARTMENT" size="6" value="<%=serviceplace.getValue("FACT_ADR_APARTMENT") %>"  class="inputfield"></td>
		</tr>
		<tr>								
			<td colspan="4" class="top_line"><b><%= Bean.jurpersonXML.getfieldTransl("title_contact_info", false) %></b></td>
		</tr>
		<tr>								
			<td><%= Bean.jurpersonXML.getfieldTransl("phone_work", false) %></td><td><input type="text" name="phone_work" id="phone_work" size="25" value="<%=serviceplace.getValue("PHONE_WORK") %>"  class="inputfield"></td>
			<td><%= Bean.jurpersonXML.getfieldTransl("web_site", false) %></td><td><input type="text" name="web_site" id="web_site" size="25" value="<%=serviceplace.getValue("WEB_SITE") %>"  class="inputfield"></td>
		</tr>
		<tr>
			<td><%= Bean.jurpersonXML.getfieldTransl("fax", false) %></td><td><input type="text" name="fax" id="fax" size="25" value="<%=serviceplace.getValue("FAX") %>"  class="inputfield"></td>
			<td><%= Bean.jurpersonXML.getfieldTransl("email", false) %></td><td><input type="text" name="email" id="email" size="25" value="<%=serviceplace.getValue("EMAIL") %>"  class="inputfield"></td>
		</tr>
		<tr>								
			<td colspan="4" class="top_line"><b><%= Bean.jurpersonXML.getfieldTransl("title_others", false) %></b></td>
		</tr>
		<tr>								
			<td><%= Bean.jurpersonXML.getfieldTransl("name_service_place_in_sms", false) %></td><td><input type="text" name="name_jur_prs_in_sms" id="name_jur_prs_in_sms" size="25" value="<%=serviceplace.getValue("NAME_JUR_PRS_IN_SMS") %>"  class="inputfield"></td>
			<td colspan="2">&nbsp;</td>
		</tr>
		<%=	Bean.getIdCreationAndMoficationRecordFields(
				serviceplace.getValue("ID_JUR_PRS"),
				serviceplace.getValue(Bean.getCreationDateFieldName()),
				serviceplace.getValue("CREATED_BY"),
				serviceplace.getValue(Bean.getLastUpdateDateFieldName()),
				serviceplace.getValue("LAST_UPDATE_BY")
			) %>
		<tr>
		<% System.out.println("cd_jur_prs_status="+cd_jur_prs_status); %>
			<td colspan="8" align="center">
				<%=Bean.getSubmitButtonAjax("../crm/clients/yurpersonserviceplaceupdate.jsp", "submit", "updateForm", "div_data_detail") %>
				<%=Bean.getResetButton() %>
				<%=Bean.getGoBackButton("../crm/clients/yurpersonspecs.jsp?id=" + id) %>
			</td>
		</tr>

	</table>

	</form>
		<%= Bean.getCalendarScript("club_date_beg", false) %>
		<%= Bean.getCalendarScript("club_date_end", false) %>
<br><%
    	} else {
    	    %> <%= Bean.getUnknownActionText(action) %><%
    	}
	}

else if (process.equalsIgnoreCase("yes")) {
    
	String    
    	name_service_place 			= Bean.getDecodeParam(parameters.get("name_service_place")), 
    	sname_service_place 		= Bean.getDecodeParam(parameters.get("sname_service_place")), 
    	id_jur_prs_form 			= Bean.getDecodeParam(parameters.get("id_jur_prs_form")), 
    	adr_code_country 			= Bean.getDecodeParam(parameters.get("ADR_CODE_COUNTRY")), 
    	adr_zip_code 				= Bean.getDecodeParam(parameters.get("ADR_ZIP_CODE")), 
    	adr_oblast 					= Bean.getDecodeParam(parameters.get("ADR_ID_OBLAST")),  
    	adr_district 				= Bean.getDecodeParam(parameters.get("ADR_DISTRICT")),  
    	adr_city 					= Bean.getDecodeParam(parameters.get("ADR_CITY")), 
    	adr_city_district 			= Bean.getDecodeParam(parameters.get("ADR_CITY_DISTRICT")),
    	adr_street 					= Bean.getDecodeParam(parameters.get("ADR_STREET")),
    	adr_house 					= Bean.getDecodeParam(parameters.get("ADR_HOUSE")),
    	adr_case 					= Bean.getDecodeParam(parameters.get("ADR_CASE")),
    	adr_apartment				= Bean.getDecodeParam(parameters.get("ADR_APARTMENT")),
    	id_club 					= Bean.getDecodeParam(parameters.get("id_club")),
    	cd_club_service_place		= Bean.getDecodeParam(parameters.get("cd_club_service_place")),
    	cd_club_member_status		= Bean.getDecodeParam(parameters.get("cd_club_member_status")),
    	club_date_beg				= Bean.getDecodeParam(parameters.get("club_date_beg")),
    	club_date_end				= Bean.getDecodeParam(parameters.get("club_date_end")),
    	
		phone_work					= Bean.getDecodeParam(parameters.get("phone_work")),
		fax							= Bean.getDecodeParam(parameters.get("fax")),
		web_site					= Bean.getDecodeParam(parameters.get("web_site")),
		email						= Bean.getDecodeParam(parameters.get("email")),
		is_resident					= Bean.getDecodeParam(parameters.get("is_resident")),
		name_service_place_in_sms	= Bean.getDecodeParam(parameters.get("name_jur_prs_in_sms")),
		LUD							= Bean.getDecodeParam(parameters.get("LUD"));
    
    if (action.equalsIgnoreCase("add")) { 
    	
		ArrayList<String> pParam = new ArrayList<String>();
				
		pParam.add(id);
		pParam.add(name_service_place);
		pParam.add(sname_service_place);
		pParam.add(name_service_place_in_sms);
		pParam.add(id_jur_prs_form);
		pParam.add(is_resident);
		pParam.add(adr_code_country);
		pParam.add(adr_zip_code);
		pParam.add(adr_oblast);
		pParam.add(adr_district);
		pParam.add(adr_city);
		pParam.add(adr_city_district);
		pParam.add(adr_street);
		pParam.add(adr_house);
		pParam.add(adr_case);
		pParam.add(adr_apartment);
		pParam.add(phone_work);
		pParam.add(fax);
		pParam.add(web_site);
		pParam.add(email);
		pParam.add(id_club);
		//pParam.add(cd_club_service_place);
		pParam.add(cd_club_member_status);
		pParam.add(club_date_beg);
		pParam.add(club_date_end);
		pParam.add("");
		pParam.add("");
		pParam.add(Bean.getDateFormat());

	 	%>
		<%= Bean.executeInsertFunction("PACK$JUR_PRS_UI.add_service_place", pParam, "../crm/clients/yurpersonspecs.jsp?id=" + id + "&id_service_place=", "") %>
		<% 	
     
    } else if (action.equalsIgnoreCase("remove")) {
    	
    	id_club = Bean.getDecodeParam(parameters.get("id_club"));
    	
		ArrayList<String> pParam = new ArrayList<String>();
				
		pParam.add(id_service_place);
		pParam.add(id_club);

	 	%>
		<%= Bean.executeDeleteFunction("PACK$JUR_PRS_UI.delete_service_place", pParam, "../crm/clients/yurpersonspecs.jsp?id=" + id, "") %>
		<% 	
     
    } else if (action.equalsIgnoreCase("edit")) { 
    	
    	ArrayList<String> pParam = new ArrayList<String>();
					
		pParam.add(id_service_place);
		pParam.add(name_service_place);
		pParam.add(sname_service_place);
		pParam.add(name_service_place_in_sms);
		pParam.add(id_jur_prs_form);
		pParam.add(is_resident);
		pParam.add(adr_code_country);
		pParam.add(adr_zip_code);
		pParam.add(adr_oblast);
		pParam.add(adr_district);
		pParam.add(adr_city);
		pParam.add(adr_city_district);
		pParam.add(adr_street);
		pParam.add(adr_house);
		pParam.add(adr_case);
		pParam.add(adr_apartment);
		pParam.add(phone_work);
		pParam.add(fax);
		pParam.add(web_site);
		pParam.add(email);
		pParam.add(id_club);
		pParam.add(cd_club_service_place);
		pParam.add(cd_club_member_status);
		pParam.add(club_date_beg);
		pParam.add(club_date_end);
		pParam.add("");
		pParam.add("");
		pParam.add(LUD);
		pParam.add(Bean.getDateFormat());

	 	%>
		<%= Bean.executeUpdateFunction("PACK$JUR_PRS_UI.update_service_place", pParam, "../crm/clients/yurpersonspecs.jsp?id=" + ("SERVICE_PLACE".equalsIgnoreCase(cd_jur_prs_status)?id_service_place:id), "") %>
		<% 	
    
    } else { %>
    	<%= Bean.getUnknownActionText(action) %><% 
    }
} else {%>
	<%= Bean.getUnknownProcessText(process) %> <%
}

%>


</body>
</html>
