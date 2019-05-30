<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" 
import="java.io.*,java.util.List,javax.servlet.*,javax.servlet.http.*,org.apache.commons.fileupload.*, org.apache.commons.fileupload.servlet.*, org.apache.commons.fileupload.disk.*,java.io.*"%>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%= Bean.getLogOutScript(request) %>

<%@ page import="java.util.HashMap"%>

<%@ page import="java.sql.Connection"%>
<%@ page import="bc.connection.Connector"%>
<%@ page import="javax.swing.JOptionPane"%>
<%@ page import="java.util.Enumeration"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.Set"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="bc.objects.bcClubShortObject"%><html>
<%@ page import="org.apache.log4j.Logger" %>
<head>
	<%= Bean.getMetaContent() %>
	<%= Bean.getBottomFrameCSS() %>
</head>

<body>
<%
Logger LOGGER = Logger.getLogger( "../crm/dispatch/messages/patternupdate.jsp" );

HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String pageFormName = "DISPATCH_MESSAGES_PATTERN";

Bean.setJspPageForTabName(pageFormName);

request.setCharacterEncoding("UTF-8");

String id					= ""; 
String type		  			= "";
String process  			= "";
String action				= "";

String begin_action_date		= "";
String end_action_date			= "";
String basis_for_operation		= "";
String id_email_profile			= "";
String name_ds_pattern 			= "";
String title_email_message 		= "";
String cd_pattern_status 		= "";
String cd_pattern_type 			= "";
String id_club		 			= "";
String text_sms_ua 				= "";
String text_sms_ru 				= "";
String text_sms_en 				= "";
String sms_code_country 		= "";
String id_sms_profile 			= "";
String email_file_name 			= "";
String stored_email_file_name 	= "";
String title_office_message 	= "";
String can_send_sms 			= "";
String can_send_email 			= "";
String can_send_office 			= "";
String text_email_message 		= "";
String text_office_message 		= "";

String side_condition			= "";
String day_number				= "";
String month_name				= "";
String cd_ds_sender_dispatch_kind	= "";
String text_request				= "";

String sms_begin_send_hour		= "";
String sms_begin_send_min		= "";
String sms_end_send_hour		= "";
String sms_end_send_min			= "";

String new_state_operation      = "";

String attach_file			= "";

String doc_dir = Bean.getDirectorySystemParamValue("UPLOADED_FILES_DIR") + Bean.getEmailFilesSubDirectory() + Bean.getDocumentsPrefix() + "_";

List<FileItem> fileList = null;

if(ServletFileUpload.isMultipartContent(request)){
  	
  	// Create a factory for disk-based file items
  	FileItemFactory factory = new DiskFileItemFactory();
  	// Create a new file upload handler
  	ServletFileUpload upload = new ServletFileUpload(factory);
  	try{

   		fileList=upload.parseRequest(request);
   		for(FileItem file:fileList){
   			if(file.isFormField()){
   				LOGGER.debug(type+" "+process+" "+action+ " fileList: "+file.getFieldName() + "=" + Bean.prepareStringToSQL(Bean.decodeUtf(file.getString())));
   				if ("id".equalsIgnoreCase(file.getFieldName())) {
	  				id = Bean.decodeUtf(file.getString());
	  			}
   				if ("type".equalsIgnoreCase(file.getFieldName())) {
	  				type = Bean.decodeUtf(file.getString());
	  			}
	  			if ("action".equalsIgnoreCase(file.getFieldName())) {
	  				action = Bean.decodeUtf(file.getString());
	  			}
	  			if ("process".equalsIgnoreCase(file.getFieldName())) {
	  				process = Bean.decodeUtf(file.getString());
	  			}
	  			
	  			
	  			if ("begin_action_date".equalsIgnoreCase(file.getFieldName())) {
	  				begin_action_date = Bean.prepareStringToSQL(Bean.decodeUtf(file.getString()));
	  			}
	  			if ("end_action_date".equalsIgnoreCase(file.getFieldName())) {
	  				end_action_date = Bean.prepareStringToSQL(Bean.decodeUtf(file.getString()));
	  			}
	  			if ("basis_for_operation".equalsIgnoreCase(file.getFieldName())) {
	  				basis_for_operation = Bean.prepareStringToSQL(Bean.decodeUtf(file.getString()));
	  			}
	  			if ("id_email_profile".equalsIgnoreCase(file.getFieldName())) {
	  				id_email_profile = Bean.prepareStringToSQL(Bean.decodeUtf(file.getString()));
	  			}
	  			if ("name_ds_pattern".equalsIgnoreCase(file.getFieldName())) {
	  				name_ds_pattern = Bean.prepareStringToSQL(Bean.decodeUtf(file.getString()));
	  			}
	  			if ("title_email_message".equalsIgnoreCase(file.getFieldName())) {
	  				title_email_message = Bean.prepareStringToSQL(Bean.decodeUtf(file.getString()));
	  			}
	  			if ("cd_pattern_status".equalsIgnoreCase(file.getFieldName())) {
	  				cd_pattern_status = Bean.prepareStringToSQL(Bean.decodeUtf(file.getString()));
	  			}
	  			if ("cd_pattern_type".equalsIgnoreCase(file.getFieldName())) {
	  				cd_pattern_type = Bean.prepareStringToSQL(Bean.decodeUtf(file.getString()));
	  			}
	  			if ("id_club".equalsIgnoreCase(file.getFieldName())) {
	  				id_club = Bean.prepareStringToSQL(Bean.decodeUtf(file.getString()));
	  			}
	  			if ("text_sms_ua".equalsIgnoreCase(file.getFieldName())) {
	  				text_sms_ua = Bean.prepareStringToSQL(Bean.decodeUtf(file.getString()));
	  			}
	  			if ("text_sms_ru".equalsIgnoreCase(file.getFieldName())) {
	  				text_sms_ru = Bean.prepareStringToSQL(Bean.decodeUtf(file.getString()));
	  			}
	  			if ("text_sms_en".equalsIgnoreCase(file.getFieldName())) {
	  				text_sms_en = Bean.prepareStringToSQL(Bean.decodeUtf(file.getString()));
	  			}
	  			if ("sms_code_country".equalsIgnoreCase(file.getFieldName())) {
	  				sms_code_country = Bean.prepareStringToSQL(Bean.decodeUtf(file.getString()));
	  			}
	  			if ("id_sms_profile".equalsIgnoreCase(file.getFieldName())) {
	  				id_sms_profile = Bean.prepareStringToSQL(Bean.decodeUtf(file.getString()));
	  			}
	  			if ("email_file_name".equalsIgnoreCase(file.getFieldName())) {
	  				email_file_name = Bean.prepareStringToSQL(Bean.decodeUtf(file.getString()));
	  			}
	  			if ("stored_email_file_name".equalsIgnoreCase(file.getFieldName())) {
	  				stored_email_file_name = Bean.prepareStringToSQL(Bean.decodeUtf(file.getString()));
	  			}
	  			if ("title_office_message".equalsIgnoreCase(file.getFieldName())) {
	  				title_office_message = Bean.prepareStringToSQL(Bean.decodeUtf(file.getString()));
	  			}
	  			if ("can_send_sms".equalsIgnoreCase(file.getFieldName())) {
	  				can_send_sms = Bean.prepareStringToSQL(Bean.decodeUtf(file.getString()));
	  			}
	  			if ("can_send_email".equalsIgnoreCase(file.getFieldName())) {
	  				can_send_email = Bean.prepareStringToSQL(Bean.decodeUtf(file.getString()));
	  			}
	  			if ("can_send_office".equalsIgnoreCase(file.getFieldName())) {
	  				can_send_office = Bean.prepareStringToSQL(Bean.decodeUtf(file.getString()));
	  			}
	  			if ("text_email_message".equalsIgnoreCase(file.getFieldName())) {
	  				text_email_message = Bean.prepareStringToSQL(Bean.decodeUtf(file.getString()));
	  			}
	  			if ("text_office_message".equalsIgnoreCase(file.getFieldName())) {
	  				text_office_message = Bean.prepareStringToSQL(Bean.decodeUtf(file.getString()));
	  			}
	  			if ("sms_begin_send_hour".equalsIgnoreCase(file.getFieldName())) {
	  				sms_begin_send_hour = Bean.prepareStringToSQL(Bean.decodeUtf(file.getString()));
	  			}
	  			if ("sms_begin_send_min".equalsIgnoreCase(file.getFieldName())) {
	  				sms_begin_send_min = Bean.prepareStringToSQL(Bean.decodeUtf(file.getString()));
	  			}
	  			if ("sms_end_send_hour".equalsIgnoreCase(file.getFieldName())) {
	  				sms_end_send_hour = Bean.prepareStringToSQL(Bean.decodeUtf(file.getString()));
	  			}
	  			if ("sms_end_send_min".equalsIgnoreCase(file.getFieldName())) {
	  				sms_end_send_min = Bean.prepareStringToSQL(Bean.decodeUtf(file.getString()));
	  			}
	  			if ("side_condition".equalsIgnoreCase(file.getFieldName())) {
	  				side_condition = Bean.prepareStringToSQL(Bean.decodeUtf(file.getString()));
	  			}
	  			if ("day_number".equalsIgnoreCase(file.getFieldName())) {
	  				day_number = Bean.prepareStringToSQL(Bean.decodeUtf(file.getString()));
	  			}
	  			if ("month_name".equalsIgnoreCase(file.getFieldName())) {
	  				month_name = Bean.prepareStringToSQL(Bean.decodeUtf(file.getString()));
	  			}
	  			if ("cd_ds_sender_dispatch_kind".equalsIgnoreCase(file.getFieldName())) {
	  				cd_ds_sender_dispatch_kind = Bean.prepareStringToSQL(Bean.decodeUtf(file.getString()));
	  			}
	  			if ("text_request".equalsIgnoreCase(file.getFieldName())) {
	  				text_request = Bean.prepareStringToSQL(Bean.decodeUtf(file.getString()));
	  			}
	  			if ("new_state_operation".equalsIgnoreCase(file.getFieldName())) {
	  				new_state_operation = Bean.prepareStringToSQL(Bean.decodeUtf(file.getString()));
	  			}

	  			if ("attach_file".equalsIgnoreCase(file.getFieldName())) {
	  				attach_file = Bean.prepareStringToSQL(Bean.decodeUtf(file.getString()));
	  			}
	  		}
   		}
   		
  	}catch(Exception ex){
   		System.err.println("Exception:"+ex.getMessage());
  	}
} else {
	id		= Bean.getDecodeParam(parameters.get("id")); 
	type	= Bean.getDecodeParam(parameters.get("type")); 
	action	= Bean.getDecodeParam(parameters.get("action")); 
	process	= Bean.getDecodeParam(parameters.get("process"));
	
	begin_action_date		= Bean.getDecodeParam(parameters.get("begin_action_date"));
	end_action_date			= Bean.getDecodeParam(parameters.get("end_action_date"));
	basis_for_operation		= Bean.getDecodeParam(parameters.get("basis_for_operation"));
	id_email_profile		= Bean.getDecodeParam(parameters.get("id_email_profile"));
	name_ds_pattern			= Bean.getDecodeParam(parameters.get("name_ds_pattern"));
	title_email_message		= Bean.getDecodeParam(parameters.get("title_email_message"));
	cd_pattern_status		= Bean.getDecodeParam(parameters.get("cd_pattern_status"));
	cd_pattern_type			= Bean.getDecodeParam(parameters.get("cd_pattern_type"));
	id_club					= Bean.getDecodeParam(parameters.get("id_club"));
	text_sms_ua				= Bean.getDecodeParam(parameters.get("text_sms_ua"));
	text_sms_ru				= Bean.getDecodeParam(parameters.get("text_sms_ru"));
	text_sms_en				= Bean.getDecodeParam(parameters.get("text_sms_en"));
	sms_code_country		= Bean.getDecodeParam(parameters.get("sms_code_country"));
	id_sms_profile			= Bean.getDecodeParam(parameters.get("id_sms_profile"));
	email_file_name			= Bean.getDecodeParam(parameters.get("email_file_name"));
	stored_email_file_name	= Bean.getDecodeParam(parameters.get("stored_email_file_name"));
	title_office_message	= Bean.getDecodeParam(parameters.get("title_office_message"));
	can_send_sms			= Bean.getDecodeParam(parameters.get("can_send_sms"));
	can_send_email			= Bean.getDecodeParam(parameters.get("can_send_email"));
	can_send_office			= Bean.getDecodeParam(parameters.get("can_send_office"));
	text_email_message		= Bean.getDecodeParam(parameters.get("text_email_message"));
	text_office_message		= Bean.getDecodeParam(parameters.get("text_office_message"));

	sms_begin_send_hour		= Bean.getDecodeParam(parameters.get("sms_begin_send_hour"));
	sms_begin_send_min		= Bean.getDecodeParam(parameters.get("sms_begin_send_min"));
	sms_end_send_hour		= Bean.getDecodeParam(parameters.get("sms_end_send_hour"));
	sms_end_send_min		= Bean.getDecodeParam(parameters.get("sms_end_send_min"));

	side_condition			= Bean.getDecodeParam(parameters.get("side_condition"));
	day_number				= Bean.getDecodeParam(parameters.get("day_number"));
	month_name				= Bean.getDecodeParam(parameters.get("month_name"));
	cd_ds_sender_dispatch_kind	= Bean.getDecodeParam(parameters.get("cd_ds_sender_dispatch_kind"));
	text_request			= Bean.getDecodeParam(parameters.get("text_request"));
	
	new_state_operation		= Bean.getDecodeParam(parameters.get("new_state_operation"));

}

if (id==null || ("".equalsIgnoreCase(id))) id="empty";
if (type==null || ("".equalsIgnoreCase(type))) type="empty";
if (action==null || ("".equalsIgnoreCase(action))) action="empty";
if (process==null || ("".equalsIgnoreCase(process))) process="empty";

if (type.equalsIgnoreCase("general")) {
	if (process.equalsIgnoreCase("no"))
		{
		   /*  --- Добавити запис --- */
	    	if (action.equalsIgnoreCase("add") || action.equalsIgnoreCase("add2")) {
		        
	    		bcClubShortObject club = new bcClubShortObject(Bean.getCurrentClubID());
	    		%>
				<%= Bean.getOperationTitle(
						Bean.messageXML.getfieldTransl("h_pattern_add", true),
						"Y",
						"Y") 
				%>
	        <script>
				var formGeneral = new Array (
					new Array ('name_club', 'varchar2', 1)
				);
				function myValidateForm(){
					return validateForm(formGeneral);
				}
			</script>

			<form action="../crm/dispatch/messages/patternupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">
		        <input type="hidden" name="type" value="general">
		        <input type="hidden" name="action" value="adddet">
		        <input type="hidden" name="process" value="no">
				<input type="hidden" name="action_prev" value="<%=action %>">
				<input type="hidden" name="id" value="<%= id %>">
			<table <%=Bean.getTableDetailParam() %>>
				<tr>
		 		    <td><%= Bean.clubXML.getfieldTransl("club", true) %>
						<%= Bean.getGoToClubLink(club.getValue("ID_CLUB")) %>
					</td>
				  	<td>
						<%=Bean.getWindowFindClub("club", club.getValue("ID_CLUB"), club.getValue("SNAME_CLUB"), "25") %>
			  		</td>
				</tr>
				<tr>
					<td><%= Bean.smsXML.getfieldTransl("cd_pattern_type", true) %></td><td><%= Bean.getDispatchPatternTypeRadio("cd_pattern_type","SMS_REQUEST") %></td>			
				</tr>
			<tr>
				<td colspan="6" align="center">
					<%=Bean.getSubmitButtonAjax("../crm/dispatch/messages/patternupdate.jsp") %>
					<%=Bean.getResetButton() %>
					<% if (action.equalsIgnoreCase("add")) { %>
						<%=Bean.getGoBackButton("../crm/dispatch/messages/pattern.jsp") %>
					<% } else { %>
						<%=Bean.getGoBackButton("../crm/dispatch/messages/patternspecs.jsp?id=" + id) %>
					<% } %>
				</td>
			</tr>
	
		</table>
		</form> 
			
		        <%
	    	} else if (action.equalsIgnoreCase("adddet")) {
		        
	    		bcClubShortObject club = new bcClubShortObject(Bean.getCurrentClubID());
	    		
	    		String action_prev		= Bean.getDecodeParam(parameters.get("action_prev"));
	    		
	    		%>
				<%= Bean.getOperationTitle(
						Bean.messageXML.getfieldTransl("h_pattern_add", true),
						"Y",
						"Y") 
				%>
			<script>
			var formAll = new Array (
			);
			var formGeneral = new Array (
				new Array ('name_ds_pattern', 'varchar2', 1),
				new Array ('cd_pattern_status', 'varchar2', 1),
				new Array ('begin_action_date', 'varchar2', 1),
				new Array ('basis_for_operation', 'varchar2', 1)
			);
			var formCongratulation = new Array (
				new Array ('side_condition', 'varchar2', 1),
				new Array ('day_number', 'varchar2', 1),
				new Array ('month_name', 'varchar2', 1)
			);
			var formSideCondition = new Array (
				new Array ('side_condition', 'varchar2', 1)
			);
			var formSMSRequest = new Array (
				new Array ('cd_ds_sender_dispatch_kind', 'varchar2', 1),
				new Array ('text_request', 'varchar2', 1)
			);

			function myValidateForm(){
				var smsType = document.getElementById('cd_pattern_type').value;
				formAll = formGeneral;
				if (smsType == 'CONGRATULATION') {
					formAll = formAll.concat(formCongratulation);
				}
				if (smsType == 'BIRTHDAY') {
					formAll = formAll.concat(formSideCondition);
				}
				if (smsType == 'END_PERION_CARD_VALIDITY') {
					formAll = formAll.concat(formSideCondition);
				}
				if (smsType == 'SMS_REQUEST') {
					formAll = formAll.concat(formSMSRequest);
				}
				//alert(formAll);
				return validateForm(formAll);
			}
		</script>

			<form action="../crm/dispatch/messages/patternupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">
		        <input type="hidden" name="type" value="general">
		        <input type="hidden" name="action" value="add">
		        <input type="hidden" name="process" value="yes">
        		<input type="hidden" name="cd_pattern_type" id="cd_pattern_type"  value="<%=cd_pattern_type %>">
				<input type="hidden" name="id" value="<%= id %>">
				<input type="hidden" name="id_club" value="<%= id_club %>">
			<table <%=Bean.getTableDetailParam() %>>
				<tr>
					<td><%= Bean.smsXML.getfieldTransl("cd_pattern_type", true) %></td><td><input type="text" name="name_pattern_type" size="65" value="<%=Bean.getDispatchPatternTypeName(cd_pattern_type) %>" readonly="readonly" class="inputfield-ro"></td>			
		 		    <td class="bottom_line"><%= Bean.clubXML.getfieldTransl("club", true) %>
						<%= Bean.getGoToClubLink(club.getValue("ID_CLUB")) %>
					</td>
				  	<td class="bottom_line">
						<input type="text" name="name_club" size="30" value="<%=Bean.getClubShortName(id_club) %>" readonly="readonly" class="inputfield-ro">
			  		</td>
				</tr>
				<tr>
					<td><%= Bean.messageXML.getfieldTransl("name_pattern", true) %></td><td><input type="text" name="name_ds_pattern" size="65" value="" class="inputfield"></td>
					<td><%= Bean.smsXML.getfieldTransl("code_country", true) %></td><td><select name="sms_code_country" class="inputfield"><%= Bean.getCountryOptions(club.getValue("CODE_COUNTRY_DEF"), true) %></select></td>			
				</tr>
				<tr>
					<td valign="top" rowspan="3"><%= Bean.messageXML.getfieldTransl("basis_for_operation", true) %></td><td valign="top" rowspan="3"><textarea name="basis_for_operation" cols="60" rows="3" class="inputfield"></textarea></td>
					<td><%= Bean.smsXML.getfieldTransl("name_pattern_status", true) %></td><td><select name="cd_pattern_status" class="inputfield"><%= Bean.getDsPatternStatusOptions("ACTIVE", true) %></select></td>			
				</tr>
				<tr>
					<td><%= Bean.messageXML.getfieldTransl("begin_action_date", true) %></td> <td><%=Bean.getCalendarInputField("begin_action_date", Bean.getSysDate(), "10") %></td>
				</tr>
				<tr>
					<td><%= Bean.messageXML.getfieldTransl("end_action_date_frmt", false) %></td> <td><%=Bean.getCalendarInputField("end_action_date", "", "10") %></td>
				</tr>
				<% if ("SMS_REQUEST".equalsIgnoreCase(cd_pattern_type)) { %>
				<tr>
					<td colspan="4" class="top_line"><b><%= Bean.messageXML.getfieldTransl("p_additional_param", false) %></b></td>
				</tr>
				<tr>
					<td><%= Bean.messageXML.getfieldTransl("cd_ds_sender_dispatch_kind", true) %></td>
					<td>
						<select name="cd_ds_sender_dispatch_kind" class="inputfield"><%= Bean.getDispatchKindOptions("", true) %></select>
					</td>
					<td colspan="2">&nbsp;</td>
				</tr>
				<tr>
					<td><%= Bean.messageXML.getfieldTransl("text_request", true) %></td>
					<td><input type="text" name="text_request" size="20" value="" class="inputfield"></td>			
					<td colspan="2">&nbsp;</td>
				</tr>
				<% } else if ("CONGRATULATION".equalsIgnoreCase(cd_pattern_type)) { %>
				<tr>
					<td colspan="4" class="top_line"><b><%= Bean.messageXML.getfieldTransl("p_additional_param", false) %></b></td>
				</tr>
				<tr>
					<td><%= Bean.messageXML.getfieldTransl("side_condition", true) %></td><td><select name="side_condition" class="inputfield"><%= Bean.getMeaningFromLookupNameOptions("SMS_PATTERN_SIDE_CONDITION", "", true) %></select></td>
				</tr>
				<tr>
					<td><span id="span_day_in_year"><%= Bean.messageXML.getfieldTransl("day_in_year", false) %></span></td>
					<td>
						<select name="day_number" class="inputfield"><%= Bean.getMeaningFromLookupNameOrderByNymberValueOptions("DAY_NUMBER", "", true) %></select>
						&nbsp;
						<select name="month_name" class="inputfield"><%= Bean.getMeaningFromLookupNameOrderByNymberValueOptions("MONTH_NAME", "", true) %></select>
					</td>
				</tr>
				<tr>
					<td><%= Bean.messageXML.getfieldTransl("side_condition", false) %></td><td><select name="side_condition" class="inputfield"><%= Bean.getMeaningFromLookupNameOptions("SMS_PATTERN_SIDE_CONDITION", "", true) %></select></td>
				</tr>
				<% } else if ("BIRTHDAY".equalsIgnoreCase(cd_pattern_type) ||
						"END_PERION_CARD_VALIDITY".equalsIgnoreCase(cd_pattern_type)) { %>
				<tr>
					<td colspan="4" class="top_line"><b><%= Bean.messageXML.getfieldTransl("p_additional_param", false) %></b></td>
				</tr>
				<tr>
					<td><%= Bean.messageXML.getfieldTransl("side_condition", true) %></td><td><select name="side_condition" class="inputfield"><%= Bean.getMeaningFromLookupNameOptions("SMS_PATTERN_SIDE_CONDITION", "", true) %></select></td>
				</tr>
				<% } %>	
			<tr>
				<td colspan="6" align="center">
					<%=Bean.getSubmitButtonAjax("../crm/dispatch/messages/patternupdate.jsp") %>
					<%=Bean.getResetButton() %>
					<% if (action.equalsIgnoreCase("add")) { %>
						<%=Bean.getGoBackButton("../crm/dispatch/messages/pattern.jsp") %>
					<% } else { %>
						
						<%=Bean.getGoBackButton("../crm/dispatch/messages/patternupdate.jsp?id=" + id + "&type=general&process=no&action=" + action_prev) %>
					<% } %>
				</td>
			</tr>
	
		</table>
		</form> 
			
		<%= Bean.getCalendarScript("begin_action_date", false) %>
		<%= Bean.getCalendarScript("end_action_date", false) %>
	
		        <%
    	} else if (action.equalsIgnoreCase("apply")) {
		        
	    		%>
				<%= Bean.getOperationTitle(
						Bean.messageXML.getfieldTransl("h_pattern_apply", true),
						"N",
						"N") 
				%>
	
			<%= Bean.getMessageLengthTextAreaInitialScript("text_message", "length_message") %>
	
			<form action="../crm/dispatch/messages/patternupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">
		        <input type="hidden" name="type" value="general">
		        <input type="hidden" name="action" value="apply">
		        <input type="hidden" name="process" value="yes">
				<input type="hidden" name="id" value="<%= id %>">
			<table <%=Bean.getTableDetailParam() %>>
			<tr>   
		       <td align="left" colspan="2">
				  <b><%= Bean.messageXML.getfieldTransl("h_operation_messages", false) %></b>
		       </td>
		    </tr>
			<tr>   
		       <td align="left" colspan="2">
				  <input type="radio" name="apply_type" id="apply_type1" size="20" CHECKED value="create_all" class="inputfield"> 
		          <label for="apply_type1"><%= Bean.messageXML.getfieldTransl("h_operation_create_all", false) %></label>
		       </td>
		    </tr>
		    <tr>   
				<td valign="top">
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<%= Bean.messageXML.getfieldTransl("type_message", false) %>
					&nbsp;&nbsp;<select name="type_message" class="inputfield"><%= Bean.getMessagePatternTypeWitoutTerminals("", true) %></select>
				</td>
		    </tr>
		    <tr>   
				<td valign="top">
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<%= Bean.messageXML.getfieldTransl("state_operation", false) %>
					&nbsp;&nbsp;<select name="new_state_operation" class="inputfield"><%= Bean.getMeaningFromLookupNameOrderByNymberValueOptions("SEND_MESSAGE_STATE", "PREPARED", true) %></select>
				</td>
		    </tr>
		    <tr>   
				<td valign="top">
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<%= Bean.messageXML.getfieldTransl("full_name_nat_prs", false) %>
					&nbsp;&nbsp;<%=Bean.getWindowFindNatPrs("nat_prs", "", "", "50") %>
				</td>
		    </tr>
		    <tr>   
				<td valign="top">
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<%= Bean.messageXML.getfieldTransl("ignore_can_send_sms", false) %>
					&nbsp;&nbsp;<select name="ignore_can_send_sms" class="inputfield"><%= Bean.getMeaningFromLookupNameOptions("YES_NO", "N", false) %></select>
				</td>
		    </tr>
		    <tr>   
				<td valign="top">
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<%= Bean.messageXML.getfieldTransl("ignore_can_send_email", false) %>
					&nbsp;&nbsp;<select name="ignore_can_send_email" class="inputfield"><%= Bean.getMeaningFromLookupNameOptions("YES_NO", "N", false) %></select>
				</td>
		    </tr>
		    <tr>   
				<td valign="top">
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<%= Bean.messageXML.getfieldTransl("create_repeated_messages", false) %>
					&nbsp;&nbsp;<select name="create_repeated_messages" class="inputfield"><%= Bean.getMeaningFromLookupNameOptions("YES_NO", "N", false) %></select>
				</td>
		    </tr>
		    <tr>   
		       <td align="left" colspan="2">
				  <input type="radio" name="apply_type" id="apply_type2" size="20" value="delete_all" class="inputfield"> 
		          <label for="apply_type2"><%= Bean.messageXML.getfieldTransl("h_operation_delete_all", false) %></label>
		       </td>
		    </tr>
			<tr>   
		       <td align="left" colspan="2">
				  <b><%= Bean.messageXML.getfieldTransl("h_operation_send_messages", false) %></b>
		       </td>
		    </tr>
		    <tr>   
		       <td align="left" colspan="2">
				  <input type="radio" name="apply_type" id="apply_type3" size="20" value="send_all" class="inputfield"> 
		          <label for="apply_type3"><%= Bean.messageXML.getfieldTransl("h_operation_send_all", false) %></label>
		       </td>
		    </tr>
		    <tr>   
		       <td align="left" colspan="2">
				  <input type="radio" name="apply_type" id="apply_type31" size="20" value="send_only_undispatched" class="inputfield"> 
		          <label for="apply_type31"><%= Bean.messageXML.getfieldTransl("h_operation_send_only_undispatched", false) %></label>
		       </td>
		    </tr>
		    <tr>   
		       <td align="left" colspan="2">
				  <input type="radio" name="apply_type" id="apply_type4" size="20" value="cancel_all" class="inputfield"> 
		          <label for="apply_type4"><%= Bean.messageXML.getfieldTransl("h_operation_cancel_all", false) %></label>
		       </td>
		    </tr>
			<tr>   
		       <td align="left" colspan="2">
				  <b><%= Bean.messageXML.getfieldTransl("h_operation_archive_massages", false) %></b>
		       </td>
		    </tr>
		    <tr>   
		       <td align="left" colspan="2">
				  <input type="radio" name="apply_type" id="apply_type5" size="20" value="to_archive_all" class="inputfield"> 
		          <label for="apply_type5"><%= Bean.messageXML.getfieldTransl("h_operation_to_archive_all", false) %></label>
		       </td>
		    </tr>
		    <tr>   
		       <td align="left" colspan="2">
				  <input type="radio" name="apply_type" id="apply_type6" size="20" value="from_archive_all" class="inputfield"> 
		          <label for="apply_type6"><%= Bean.messageXML.getfieldTransl("h_operation_from_archive_all", false) %></label>
		       </td>
		    </tr>
	
			<tr>
				<td colspan="6" align="center">
					<%=Bean.getSubmitButtonAjax("../crm/dispatch/messages/patternupdate.jsp") %>
					<%=Bean.getResetButton() %>
					<%=Bean.getGoBackButton("../crm/dispatch/messages/patternspecs.jsp?id=" + id) %>
				</td>
			</tr>
	
		</table>
		</form> 
	
        <%
	    } else {
	        %> <%= Bean.getUnknownActionText(action) %><%
	    }
	
	} else if (process.equalsIgnoreCase("yes"))	{
	    
		if (action.equalsIgnoreCase("add")) { 
			
			String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_DS_CLIENT.add_pattern(" + 
				"?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";

			String[] pParam = new String [14];

			pParam[0] = name_ds_pattern;
			pParam[1] = cd_pattern_status;
			pParam[2] = cd_pattern_type;
			pParam[3] = begin_action_date;
			pParam[4] = end_action_date;
			pParam[5] = basis_for_operation;
			pParam[6] = sms_code_country;
			pParam[7] = side_condition;
			pParam[8] = day_number;
			pParam[9] = month_name;
			pParam[10] = cd_ds_sender_dispatch_kind;
			pParam[11] = text_request;
			pParam[12] = id_club;
			pParam[13] = Bean.getDateFormat();
			
		 	%>
			<%= Bean.getCallResultParam("INSERT", callSQL, pParam, "../crm/dispatch/messages/patternspecs.jsp?id=", "../crm/dispatch/messages/pattern.jsp") %>
			<% 	
	
		} else if (action.equalsIgnoreCase("remove")) { 
			
			String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_DS_CLIENT.delete_pattern(?,?)}";

			String[] pParam = new String [1];

			pParam[0] = id;
	
		 	%>
			<%= Bean.getCallResultParam("DELETE", callSQL, pParam, "../crm/dispatch/messages/pattern.jsp", "") %>
			<% 	
	
		} else if (action.equalsIgnoreCase("apply")) { 
			
			String 	type_message				= Bean.getDecodeParam(parameters.get("type_message"));
			String 	id_nat_prs					= Bean.getDecodeParam(parameters.get("id_nat_prs"));
			String 	apply_type					= Bean.getDecodeParam(parameters.get("apply_type"));
			String 	ignore_can_send_sms			= Bean.getDecodeParam(parameters.get("ignore_can_send_sms"));
			String 	ignore_can_send_email		= Bean.getDecodeParam(parameters.get("ignore_can_send_email"));
			String 	create_repeated_messages	= Bean.getDecodeParam(parameters.get("create_repeated_messages"));
			
			String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_DS_CLIENT.set_pattern_action(" + 
				"?,?,?,?,?,?,?,?,?)}";

			String[] pParam = new String [8];

			pParam[0] = id;
			pParam[1] = id_nat_prs;
			pParam[2] = type_message;
			pParam[3] = apply_type;
			pParam[4] = new_state_operation;
			pParam[5] = ignore_can_send_sms;
			pParam[6] = ignore_can_send_email;
			pParam[7] = create_repeated_messages;

		 	%>
			<%= Bean.getCallResultParam("DELETE", callSQL, pParam, "../crm/dispatch/messages/patternspecs.jsp?id=" + id + "&messages_page=1", "") %>
			<% 	

		} else if (action.equalsIgnoreCase("edit")) { 
			
			String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_DS_CLIENT.update_pattern('" + 
				"?,?,?,?,?,?,?,?,?,?,?,?,?)}";

			String[] pParam = new String [12];

			pParam[0] = id;
			pParam[1] = name_ds_pattern;
			pParam[2] = cd_pattern_status;
			pParam[3] = begin_action_date;
			pParam[4] = end_action_date;
			pParam[5] = basis_for_operation;
			pParam[6] = side_condition;
			pParam[7] = day_number;
			pParam[8] = month_name;
			pParam[9] = cd_ds_sender_dispatch_kind;
			pParam[10] = text_request;
			pParam[11] = Bean.getDateFormat();
			
		 	%>
			<%= Bean.getCallResultParam("UPDATE", callSQL, pParam, "../crm/dispatch/messages/patternspecs.jsp?id=" + id, "../crm/dispatch/messages/patternspecs.jsp?id=" + id) %>
			<% 	
	
		} else { %> 
			<%= Bean.getUnknownActionText(action) %><% 
		}
	
	} else {
	    %> <%= Bean.getUnknownProcessText(process) %> <%
	}
} else if (type.equalsIgnoreCase("sms")) {
	if (process.equalsIgnoreCase("yes")) { 
		if (action.equalsIgnoreCase("edit")) { 
			
		    String resultInt = "";
		    String resultFull = "0";
		    String resultMessage = "";
		    String resultMessageFull = "";
		    String[] results = new String[4];
			String callSQL = "";

			callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_DS_CLIENT.update_pattern_sms("+
				"?,?,?,?,?,?,?,?,?,?,?)}";

			String[] pParam = new String [10];

			pParam[0] = id;
			pParam[1] = can_send_sms;
			pParam[2] = id_sms_profile;
			pParam[3] = sms_begin_send_hour;
			pParam[4] = sms_begin_send_min;
			pParam[5] = sms_end_send_hour;
			pParam[6] = sms_end_send_min;
			pParam[7] = text_sms_ua;
			pParam[8] = text_sms_ru;
			pParam[9] = text_sms_en;

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
				
			String[] pParam2 = new String [2];
			
			ArrayList<String> id_value=new ArrayList<String>();
			ArrayList<String> prv_value=new ArrayList<String>();

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
								"../crm/dispatch/messages/patternupdate.jsp",
								type,
								process,
								action,
								Bean.commonXML.getfieldTransl("h_get_param_value_error", false)+key+": " + ex.getMessage());
					}
				}


			    if (id_value.size()>0) {
			 		 for(int counter=0;counter<id_value.size();counter++){ 
			 			 if (!(prv_value.contains(id_value.get(counter)))) {
			 				 
			        	callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_DS_CLIENT.atach_oblast_to_pattern(?,?,?)}";
			        		
			        	pParam2[0] = id;
			        	pParam2[1] = id_value.get(counter);
				
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
			}
			 	if (prv_value.size()>0) {
			 		for(int counter=0;counter<prv_value.size();counter++){ 
				 	if (!(id_value.contains(prv_value.get(counter)))) {
				   	 				 
			        	callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_DS_CLIENT.detach_oblast_from_pattern(?,?,?)}";
						
						pParam2[0] = id;
			        	pParam2[1] = prv_value.get(counter);
				
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
			 		
			 	}

			 	%>
			    <%=Bean.showCallResult(
			    		callSQL, 
			    		resultFull, 
			    		resultMessageFull, 
			    		"../crm/dispatch/messages/patternspecs.jsp?id=" + id, 
			    		"../crm/dispatch/messages/patternspecs.jsp?id=" + id, 
			    		Bean.form_messageXML.getfieldTransl("save_error", false)) %>
				<% 
 	

		} else { %> 
			<%= Bean.getUnknownActionText(action) %><% 
		}
	} else {
	    %> <%= Bean.getUnknownProcessText(process) %> <%
	}
} else if (type.equalsIgnoreCase("email")) {
	if (process.equalsIgnoreCase("yes")) { 
		if (action.equalsIgnoreCase("edit")) { 
			boolean transfer_ok = true;
			String errorText = "";
			String file_doc = "";
			
			if(ServletFileUpload.isMultipartContent(request)){
	
				for(FileItem file:fileList){
					if(!file.isFormField()){
						String filename=file.getName();
						String fieldName = file.getFieldName();
						if (("Y".equalsIgnoreCase(attach_file) && "email_attached_file_name".equalsIgnoreCase(fieldName)) ||
								("N".equalsIgnoreCase(attach_file) && "email_file_name".equalsIgnoreCase(fieldName))) {
							LOGGER.debug(type+" "+process+" "+action+ " filename="+filename);
							String shortname="";
							if (filename.contains("\\")) {
								shortname=filename.substring(filename.lastIndexOf("\\")+1,filename.length());
							} else if (filename.contains("/")) {
								shortname=filename.substring(filename.lastIndexOf("/")+1,filename.length());
							} else {
								shortname = filename;
							}
							LOGGER.debug(type+" "+process+" "+action+ " shortname="+shortname);
				  			
				  			if (!(shortname==null || "".equalsIgnoreCase(shortname))) {
				  				email_file_name = shortname;
				  				file_doc = doc_dir + shortname;
				  			}
					  		
			  		     	if (!(shortname==null || "".equalsIgnoreCase(shortname))) {
			  		     		try{
			  		     			file.write(new File(file_doc));
				  		    	} catch (Exception e){
				  		    		transfer_ok = false;
				  		    		errorText = e.toString();
				  		    	}
			  		     	} else {
				  		    	file_doc = "";
				  		    }
						}
			  		}
					
						
		   		}
			 } else {
				 LOGGER.error(type+" "+process+" "+action+ " File not found");
			 }
			if (transfer_ok) {
				
				String callSQL = "";
				if ("Y".equalsIgnoreCase(attach_file)) {
					callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_DS_CLIENT.attach_file_to_pattern(?,?,?,?,?)}";

					String[] pParam = new String [4];

					pParam[0] = id;
					pParam[1] = "EMAIL";
					pParam[2] = email_file_name;
					pParam[3] = file_doc;
					
			 		%>
					<%= Bean.getCallResultParam("UPDATE", callSQL, pParam, "../crm/dispatch/messages/patternspecs.jsp?id=" + id, "") %>
					<% 	
					
				} else {
					callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_DS_CLIENT.update_pattern_email(" + 
						"?,?,?,?,?,?,?,?)}";

					String[] pParam = new String [7];

					pParam[0] = id;
					pParam[1] = can_send_email;
					pParam[2] = id_email_profile;
					pParam[3] = title_email_message;
					pParam[4] = text_email_message;
					pParam[5] = email_file_name;
					pParam[6] = file_doc;
					
			 		%>
					<%= Bean.getCallResultParam("UPDATE", callSQL, pParam, "../crm/dispatch/messages/patternspecs.jsp?id=" + id, "") %>
					<% 	
				}
			} else {%>
				<%=Bean.showCallResult("", "1", errorText, "../crm/dispatch/messages/patternspecs.jsp?id=" + id, "", Bean.documentXML.getfieldTransl("h_upload_file_error", false)) %>
			<%}
	
		} else if (action.equalsIgnoreCase("remove_file")) { 
			
			String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_DS_CLIENT.delete_pattern_email_file(?,?)}";

			String[] pParam = new String [1];

			pParam[0] = id;

		 	%>
			<%= Bean.getCallResultParam("DELETE", callSQL, pParam, "../crm/dispatch/messages/patternspecs.jsp?id=" + id, "") %>
			<% 	
		} else if (action.equalsIgnoreCase("remove_attached_file")) { 
			
			String 	id_file		= Bean.getDecodeParam(parameters.get("id_file"));
			
			String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_DS_CLIENT.detach_file_from_pattern(?,?,?)}";

			String[] pParam = new String [1];

			pParam[0] = id;
			pParam[1] = id_file;

		 	%>
			<%= Bean.getCallResultParam("DELETE", callSQL, pParam, "../crm/dispatch/messages/patternspecs.jsp?id=" + id, "") %>
			<% 	
		} else { %> 
			<%= Bean.getUnknownActionText(action) %><% 
		}
	} else {
	    %> <%= Bean.getUnknownProcessText(process) %> <%
	}
} else if (type.equalsIgnoreCase("office")) {
	if (process.equalsIgnoreCase("yes")) { 
		if (action.equalsIgnoreCase("edit")) { 
			String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_DS_CLIENT.update_pattern_office(" + 
				"?,?,?,?,?)}";

			String[] pParam = new String [4];

			pParam[0] = id;
			pParam[1] = can_send_office;
			pParam[2] = title_office_message;
			pParam[3] = text_office_message;
				
			%>
			<%= Bean.getCallResultParam("UPDATE", callSQL, pParam, "../crm/dispatch/messages/patternspecs.jsp?id=" + id, "") %>
			<% 	
		
		} else if (action.equalsIgnoreCase("remove_file")) { 
				
			String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_DS_CLIENT.delete_pattern_office_file(?,?)}";

			String[] pParam = new String [1];

			pParam[0] = id;

		 	%>
			<%= Bean.getCallResultParam("DELETE", callSQL, pParam, "../crm/dispatch/messages/patternspecs.jsp?id=" + id, "") %>
			<% 	
		} else if (action.equalsIgnoreCase("remove_attached_file")) { 
				
			String 	id_file		= Bean.getDecodeParam(parameters.get("id_file"));
				
			String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_DS_CLIENT.detach_file_from_pattern(?,?,?)}";

			String[] pParam = new String [2];

			pParam[0] = id;
			pParam[1] = id_file;

		 	%>
			<%= Bean.getCallResultParam("DELETE", callSQL, pParam, "../crm/dispatch/messages/patternspecs.jsp?id=" + id, "") %>
			<% 	
		} else { %> 
			<%= Bean.getUnknownActionText(action) %><% 
		}
	} else {
	    %> <%= Bean.getUnknownProcessText(process) %> <%
	}
} else if (type.equalsIgnoreCase("message")) {
	if (process.equalsIgnoreCase("yes")) { 
		if (action.equalsIgnoreCase("execute")) {
			
			String operation_type 	= Bean.getDecodeParam(parameters.get("operation_type"));
			String callSQL = "";
			ArrayList<String> id_value=new ArrayList<String>();		
			ArrayList<String> type_message=new ArrayList<String>();
	    	
			Object current_parameter;
		    int _position = 0;
	    	String fullValue = "";
		    	Set<String> keySet = parameters.keySet();
	    		Iterator<String> keySetIterator = keySet.iterator();
	    		String key = "";
		    	while(keySetIterator.hasNext()) {
					try{
						key = (String)keySetIterator.next();
						
						if(key.contains("chb")){
							fullValue = key.substring(3);
							_position = fullValue.indexOf("_");
							id_value.add(fullValue.substring(0, _position));
							type_message.add(fullValue.substring(_position+1));
						}
						
					}
					catch(Exception ex){
						Bean.writeException(
								"../crm/dispatch/messages/patternupdate.jsp",
								"",
								process,
								action,
								Bean.commonXML.getfieldTransl("h_get_param_value_error", false)+key+": " + ex.getMessage());
					}
					 
				}
			
		    String[] results = new String[2];
		    String resultInt = "";
		    String resultFull = "0";
		    String resultMessage = "";
		    String resultMessageFull = "";
		    
		    String[] pParam = new String [3];
	 
			%>

			<%= Bean.form_messageXML.getfieldTransl("processing_update", false) %>
			<%
			if (!(id_value == null)) {
			 for(int counter=0;counter<id_value.size();counter++){
				 callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_DS_CLIENT.set_message_state(?,?,?,?)}";
				 
			    pParam[0] = id_value.get(counter);
			    pParam[1] = type_message.get(counter);
			    pParam[2] = operation_type;
			
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
			
			%>
	  	    <%=Bean.showCallResult(
	   	    		callSQL, 
	   	    		resultFull, 
	   	    		resultMessageFull, 
	   	    		"../crm/dispatch/messages/patternspecs.jsp?id=" + id + "&operation_type=" + operation_type + "&specid=", 
	   	    		"../crm/dispatch/messages/patternspecs.jsp?id=" + id + "&operation_type=" + operation_type + "&specid=", 
	   	    		Bean.form_messageXML.getfieldTransl("save_error", false)) %>
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
