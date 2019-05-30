<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%@page import="bc.objects.bcLoySchemeObject"%>
<%@page import="bc.objects.bcComissionObject"%>
<%@page import="bc.objects.bcClubRelationshipObject"%>
<%@page import="java.util.HashMap"%>
<%@page import="bc.objects.bcClubShortObject"%>

<%@page import="org.apache.commons.fileupload.servlet.ServletFileUpload"%>
<%@page import="java.util.List"%>
<%@page import="org.apache.commons.fileupload.FileItem"%>
<%@page import="org.apache.commons.fileupload.FileItemFactory"%>
<%@page import="org.apache.commons.fileupload.disk.DiskFileItemFactory"%>
<%@page import="java.io.File"%>
<%@page import="java.awt.Image"%>
<%@page import="javax.imageio.ImageIO"%>
<%@page import="java.util.Date"%>
<%@page import="java.util.ArrayList"%>
<%@page import="javax.naming.Context"%>
<%@page import="javax.naming.InitialContext"%>
<%@page import="bc.objects.bcClubTargetProgramObject"%>
<%@page import="bc.objects.bcTargetProgramServicePlaceObject"%>
<%@page import="bc.objects.bcNatPrsRoleTargetPrgObject"%>
<%@page import="bc.lists.bcListTargetProgram"%><html>

<%= Bean.getLogOutScript(request) %>

<head>
	<%= Bean.getMetaContent() %>
	<%= Bean.getBottomFrameCSS() %>
</head>

<body>

<%

HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String pageFormName = "CLUB_TARGET_PROGRAM";

Bean.setJspPageForTabName(pageFormName);

request.setCharacterEncoding("UTF-8");

String external_id		= Bean.getDecodeParamPrepare(parameters.get("id"));
String type				= Bean.getDecodeParamPrepare(parameters.get("type")); 
String action			= Bean.getDecodeParamPrepare(parameters.get("action")); 
String process			= Bean.getDecodeParamPrepare(parameters.get("process"));
String back_type		= Bean.getDecodeParamPrepare(parameters.get("back_type"));
String id_jur_prs		= Bean.getDecodeParamPrepare(parameters.get("id_jur_prs"));
String id_doc			= Bean.getDecodeParamPrepare(parameters.get("id_doc"));
String id_target_prg	= Bean.getDecodeParam(parameters.get("id_target_prg"));

String id_target_prg_parent				= Bean.getDecodeParam(parameters.get("id_target_prg_parent"));
String name_target_prg					= Bean.getDecodeParam(parameters.get("name_target_prg"));
String sname_target_prg					= Bean.getDecodeParam(parameters.get("desc_target_prg"));
String desc_target_prg					= Bean.getDecodeParam(parameters.get("desc_target_prg"));
String date_beg							= Bean.getDecodeParam(parameters.get("date_beg"));
String date_end							= Bean.getDecodeParam(parameters.get("date_end"));
String id_nat_prs_initiator				= Bean.getDecodeParam(parameters.get("id_nat_prs_initiator"));
String id_nat_prs_administrator			= Bean.getDecodeParam(parameters.get("id_nat_prs_administrator"));
String cd_currency						= Bean.getDecodeParam(parameters.get("cd_currency"));
String cd_target_prg_pay_period			= Bean.getDecodeParam(parameters.get("cd_target_prg_pay_period"));
String pay_amount						= Bean.getDecodeParam(parameters.get("pay_amount"));
String pay_count						= Bean.getDecodeParam(parameters.get("pay_count"));
String need_subscribe					= Bean.getDecodeParam(parameters.get("need_subscribe"));
String need_administrator_confirm		= Bean.getDecodeParam(parameters.get("need_administrator_confirm"));
String name_target_prg_in_sms			= Bean.getDecodeParam(parameters.get("name_target_prg_in_sms"));
String entrance_fee						= Bean.getDecodeParam(parameters.get("entrance_fee"));
String min_pay_amount					= Bean.getDecodeParam(parameters.get("min_pay_amount"));
String membership_fee					= Bean.getDecodeParam(parameters.get("membership_fee"));
String membership_period				= Bean.getDecodeParam(parameters.get("membership_period"));
String membership_period_value			= Bean.getDecodeParam(parameters.get("membership_period_value"));
String id_loaded_file					= Bean.getDecodeParam(parameters.get("id_loaded_file"));
String id_club							= Bean.getDecodeParam(parameters.get("id_club"));
String LUD								= Bean.getDecodeParam(parameters.get("LUD"));
String id_target_prg_place				= Bean.getDecodeParam(parameters.get("id_target_prg_place"));

String photoFileName 		= "";
String fullPhotoFileName 	= "";
boolean loadError              = false;

if(ServletFileUpload.isMultipartContent(request)){
	Context environmentContext = (Context) new InitialContext().lookup("java:/comp/env");
	String CONTEXT_PARAM_LOADED_FILES="wmpupt/images/icons";
	String photo_dir = (String)environmentContext.lookup(CONTEXT_PARAM_LOADED_FILES)+"target_prg/";

	List<FileItem> fileList = null;
  	
  	// Create a factory for disk-based file items
  	FileItemFactory factory = new DiskFileItemFactory();
  	// Create a new file upload handler
  	ServletFileUpload upload = new ServletFileUpload(factory);
    File uploadedFile;
  	try{
  		System.out.println("Начало команды fileList = upload.parseRequest(request)");
   		fileList = upload.parseRequest(request);
  		System.out.println("В предыдущей команде (fileList = upload.parseRequest(request);) иногда зависает на целую минуту");
   		for(FileItem file:fileList){
	  		if(file.isFormField()){
	   			//System.out.println("field="+file.getString());
	  			if ("action".equalsIgnoreCase(file.getFieldName())) action = Bean.decodeUtf(file.getString());
	  			if ("type".equalsIgnoreCase(file.getFieldName())) type = Bean.decodeUtf(file.getString());
	  			if ("process".equalsIgnoreCase(file.getFieldName())) process = Bean.decodeUtf(file.getString());
	  			if ("id".equalsIgnoreCase(file.getFieldName())) external_id = Bean.decodeUtf(file.getString());
	  			if ("back_type".equalsIgnoreCase(file.getFieldName())) back_type = Bean.decodeUtf(file.getString());

	  			if ("id_target_prg".equalsIgnoreCase(file.getFieldName())) id_target_prg = Bean.decodeUtf(file.getString());
	  			if ("id_target_prg_parent".equalsIgnoreCase(file.getFieldName())) id_target_prg_parent = Bean.decodeUtf(file.getString());
	  			if ("name_target_prg".equalsIgnoreCase(file.getFieldName())) name_target_prg = Bean.decodeUtf(file.getString());
	  			if ("sname_target_prg".equalsIgnoreCase(file.getFieldName())) sname_target_prg = Bean.decodeUtf(file.getString());
	  			if ("desc_target_prg".equalsIgnoreCase(file.getFieldName())) desc_target_prg = Bean.decodeUtf(file.getString());
	  			if ("date_beg".equalsIgnoreCase(file.getFieldName())) date_beg = Bean.decodeUtf(file.getString());
	  			if ("date_end".equalsIgnoreCase(file.getFieldName())) date_end = Bean.decodeUtf(file.getString());
	  			if ("id_nat_prs_initiator".equalsIgnoreCase(file.getFieldName())) id_nat_prs_initiator = Bean.decodeUtf(file.getString());
	  			if ("id_nat_prs_administrator".equalsIgnoreCase(file.getFieldName())) id_nat_prs_administrator = Bean.decodeUtf(file.getString());
	  			if ("id_jur_prs".equalsIgnoreCase(file.getFieldName())) id_jur_prs = Bean.decodeUtf(file.getString());
	  			if ("cd_currency".equalsIgnoreCase(file.getFieldName())) cd_currency = Bean.decodeUtf(file.getString());
	  			if ("cd_target_prg_pay_period".equalsIgnoreCase(file.getFieldName())) cd_target_prg_pay_period = Bean.decodeUtf(file.getString());
	  			if ("pay_amount".equalsIgnoreCase(file.getFieldName())) pay_amount = Bean.decodeUtf(file.getString());
	  			if ("pay_count".equalsIgnoreCase(file.getFieldName())) pay_count = Bean.decodeUtf(file.getString());
	  			if ("need_subscribe".equalsIgnoreCase(file.getFieldName())) need_subscribe = Bean.decodeUtf(file.getString());
	  			if ("need_administrator_confirm".equalsIgnoreCase(file.getFieldName())) need_administrator_confirm = Bean.decodeUtf(file.getString());
	  			if ("name_target_prg_in_sms".equalsIgnoreCase(file.getFieldName())) name_target_prg_in_sms = Bean.decodeUtf(file.getString());
	  			if ("entrance_fee".equalsIgnoreCase(file.getFieldName())) entrance_fee = Bean.decodeUtf(file.getString());
	  			if ("min_pay_amount".equalsIgnoreCase(file.getFieldName())) min_pay_amount = Bean.decodeUtf(file.getString());
	  			if ("membership_fee".equalsIgnoreCase(file.getFieldName())) membership_fee = Bean.decodeUtf(file.getString());
	  			if ("membership_period".equalsIgnoreCase(file.getFieldName())) membership_period = Bean.decodeUtf(file.getString());
	  			if ("membership_period_value".equalsIgnoreCase(file.getFieldName())) membership_period_value = Bean.decodeUtf(file.getString());
	  			if ("id_loaded_file".equalsIgnoreCase(file.getFieldName())) id_loaded_file = Bean.decodeUtf(file.getString());
	  			if ("id_club".equalsIgnoreCase(file.getFieldName())) id_club = Bean.decodeUtf(file.getString());
	  			if ("id_doc".equalsIgnoreCase(file.getFieldName())) id_doc = Bean.decodeUtf(file.getString());
	  			if ("LUD".equalsIgnoreCase(file.getFieldName())) LUD = Bean.decodeUtf(file.getString());
	  		} else {
	  			String fieldName = file.getFieldName();
	  			String file_name = file.getName();
	  			String sFileName = "";
	  			String fFileName = "";
				
	   			if (!(file_name==null || "".equalsIgnoreCase(file_name))) {
					if (file_name.contains("\\")) {
						sFileName=file_name.substring(file_name.lastIndexOf("\\")+1,file_name.length());
					} else if (file_name.contains("/")) {
						sFileName=file_name.substring(file_name.lastIndexOf("/")+1,file_name.length());
					} else {
						sFileName = file_name;
					}
					sFileName = Bean.getDocumentsPrefix() + "_" + sFileName;
					fFileName = photo_dir + sFileName;
		   			System.out.println("fieldName="+fieldName+", sFileName="+sFileName+", fFileName="+fFileName);
		   			
	   			    File filePath = new File(photo_dir);
	
	                if (!filePath.exists()) {
	                    filePath.mkdirs();
	                }
	
	                uploadedFile = new File(fFileName);
	                file.write(uploadedFile);
	                
	                if ("client_photo".equalsIgnoreCase(fieldName)) {
	                	photoFileName 		= sFileName;
	                	fullPhotoFileName 	= fFileName;
	                	
	                	File sourceimage = new File(fullPhotoFileName);
	                	Image image = ImageIO.read(sourceimage);
	                }
	   			}
	  		}
   		}

  	}catch(Exception ex){
   		System.err.println("Exception " + ex.getMessage());
   		loadError = true;
  	}
}

String updateLink = "../crm/club/target_programupdate.jsp";
String backLink = "";
String generalLink = "";
back_type = Bean.isEmpty(back_type)?"TARGET_PROGRAM":back_type;
System.out.println("back_type="+back_type);
if ("TARGET_PROGRAM".equalsIgnoreCase(back_type)) {
	generalLink = "../crm/club/target_program.jsp?";
	if (action.equalsIgnoreCase("add")) {
		backLink = "../crm/club/target_program.jsp?";
	} else {
		backLink = "../crm/club/target_programspecs.jsp?id="+id_target_prg;
	}
} else if ("CLUB".equalsIgnoreCase(back_type)) {
	generalLink = "../crm/club/clubspecs.jsp?id="+external_id;
	backLink = "../crm/club/clubspecs.jsp?id="+external_id;
} else if ("DOCUMENT".equalsIgnoreCase(back_type)) {
	generalLink = "../crm/club/documentspecs.jsp?id="+external_id;
	backLink = "../crm/club/documentspecs.jsp?id="+external_id;
} else if ("PARTNER".equalsIgnoreCase(back_type)) {
	generalLink = "../crm/clients/yurpersonspecs.jsp?id="+external_id;
	backLink = "../crm/clients/yurpersonspecs.jsp?id="+external_id;
}
if (type.equalsIgnoreCase("general")) {
	if (process.equalsIgnoreCase("no")) {
		if (action.equalsIgnoreCase("add") || action.equalsIgnoreCase("add2") || action.equalsIgnoreCase("addsubprogram")) {
    		
			bcClubShortObject club = new bcClubShortObject(Bean.getCurrentClubID());
			
			bcClubTargetProgramObject subprogram = null;
			
			if (action.equalsIgnoreCase("addsubprogram")) {
				subprogram = new bcClubTargetProgramObject(id_target_prg);
			}
			
	        %> 
			<% if ("TARGET_PROGRAM".equalsIgnoreCase(back_type)) { %>
			<%= Bean.getOperationTitle(
					Bean.clubfundXML.getfieldTransl("h_add_target_program", false),
					"Y",
					"Y") 
			%>
			<% } else { %>
			<%= Bean.getOperationTitleShort(
					"",
					Bean.clubfundXML.getfieldTransl("h_add_target_program", false),
					"Y",
					"Y") 
					%>
			<% } %>
		<script>
			var formValidateData = new Array (
				new Array ('name_target_prg', 'varchar2', 1),
				new Array ('sname_target_prg', 'varchar2', 1),
				new Array ('name_nat_prs_initiator', 'varchar2', 1),
				new Array ('name_nat_prs_administrator', 'varchar2', 1),
				new Array ('name_jur_prs', 'varchar2', 1),
				new Array ('cd_currency', 'varchar2', 1),
				new Array ('name_club', 'varchar2', 1),
				new Array ('date_beg', 'varchar2', 1)
			);
			var formPayAmount = new Array (
				new Array ('pay_amount', 'number', 1)
			);
			var formPayCount = new Array (
				new Array ('pay_count', 'number', 1)
			);
			
			function myValidateForm() {

				var cd_target_prg_pay_period = document.getElementById('cd_target_prg_pay_period');
				if (cd_target_prg_pay_period.value!='IRREGULAR' && cd_target_prg_pay_period.value!='') {
					formValidateData = formValidateData.concat(formPayAmount);
				}
				if (cd_target_prg_pay_period.value=='STUDY_COUNT') {
					formValidateData = formValidateData.concat(formPayCount);
				}
				return validateForm(formValidateData);
			}

			function del_photo() {
				var msg='<%=Bean.buttonXML.getfieldTransl("photo_delete", false) %>?';
				var res=window.confirm(msg);
				if (res) {
					img = document.getElementById('image-0');
					img.src = '';
					div_photo = document.getElementById('div_photo');
					div_photo.innerHTML = '<p><%=Bean.clubfundXML.getfieldTransl("titl_picture_load", false) %></p><input type="file" class="photo_file" name="client_photo" size="50" value="" accept="image/*" capture="camera" onchange="load_photo()">';
					//post_form('service/load_photo.jsp','delPhoto','div_photo');
				}
			}
			function load_photo() {
				action = document.getElementById('action');
				action.value = 'load_photo';
				post_form('<%=updateLink%>','updateForm7','div_photo');
				action.value = 'add';
				//alert('action.value='+action.value);
			}
			function changePayPeriod(val) {
				part1Title = document.getElementById('part1Title');
				part1Value = document.getElementById('part1Value');
				if (val=='IRREGULAR') {
					part1Title.innerHTML='<%= Bean.clubfundXML.getfieldTransl("min_pay_amount", false) %>';
					part1Value.innerHTML='<input type="text" name="min_pay_amount" size="15" value="" class="inputfield">';
				} else if (val=='MONTH') {
					part1Title.innerHTML='<%= Bean.clubfundXML.getfieldTransl("pay_amount", false) %>';
					part1Value.innerHTML='<input type="text" name="pay_amount" size="15" value="" class="inputfield"> <%= Bean.clubfundXML.getfieldTransl("title_membership_months", false) %>';
				} else if (val=='WEEK') {
					part1Title.innerHTML='<%= Bean.clubfundXML.getfieldTransl("pay_amount", false) %>';
					part1Value.innerHTML='<input type="text" name="pay_amount" size="15" value="" class="inputfield"> <%= Bean.clubfundXML.getfieldTransl("title_membership_weeks", false) %>';
				} else if (val=='DAY') {
					part1Title.innerHTML='<%= Bean.clubfundXML.getfieldTransl("pay_amount", false) %>';
					part1Value.innerHTML='<input type="text" name="pay_amount" size="15" value="" class="inputfield"> <%= Bean.clubfundXML.getfieldTransl("title_membership_days", false) %>';
				} else if (val=='HOUR') {
					part1Title.innerHTML='<%= Bean.clubfundXML.getfieldTransl("pay_amount", false) %>';
					part1Value.innerHTML='<input type="text" name="pay_amount" size="15" value="" class="inputfield"> <%= Bean.clubfundXML.getfieldTransl("title_membership_hours", false) %>';
				} else if (val=='STUDY_COUNT') {
					part1Title.innerHTML='<%= Bean.clubfundXML.getfieldTransl("pay_amount", false) %>';
					part1Value.innerHTML='<input type="text" name="pay_amount" size="15" value="" class="inputfield"> <%= Bean.clubfundXML.getfieldTransl("title_membership_for", false) %> <input type="text" name="pay_count" size="5" value="" class="inputfield"> <%= Bean.clubfundXML.getfieldTransl("title_membership_studies", false) %>';
				} else {
					part1Title.innerHTML='&nbsp;';
					part1Value.innerHTML='&nbsp;';
				}
			}
		</script>
    <form name="updateForm7" id="updateForm7" accept-charset="UTF-8" method="POST" enctype="multipart/form-data">
		<% if (action.equalsIgnoreCase("addsubprogram")) { %>
    	<input type="hidden" name="id_target_prg_parent" value="<%=id_target_prg %>">
		<% } %>
	    <input type="hidden" name="action" id="action" value="add">
    	<input type="hidden" name="process" value="yes">
    	<input type="hidden" name="type" value="general">
    	<input type="hidden" name="id" value="<%=external_id %>">
    	<input type="hidden" name="back_type" value="<%=back_type %>">

	<table <%=Bean.getTableDetailParam() %> style="border: 1px solid">
	    <tr>
			<td><%= Bean.clubfundXML.getfieldTransl("name_target_prg", true) %></td> <td><input type="text" name="name_target_prg" size="60" value="" class="inputfield"></td>
			<td><%= Bean.clubXML.getfieldTransl("club", true) %>
				<%= Bean.getGoToClubLink(club.getValue("ID_CLUB")) %>
			</td>
		  	<td>
				<%=Bean.getWindowFindClub("club", club.getValue("ID_CLUB"), club.getValue("SNAME_CLUB"), "25") %>
		  	</td>
		</tr>
	    <tr>
			<td><%= Bean.clubfundXML.getfieldTransl("sname_target_prg", true) %></td> <td><input type="text" name="sname_target_prg" size="60" value="" class="inputfield"></td>
			<td><%= Bean.clubfundXML.getfieldTransl("target_program_date_beg", true) %></td> <td><%=Bean.getCalendarInputField("date_beg", Bean.getSysDate(), "10") %></td>
		</tr>
	    <tr>
		<% if (action.equalsIgnoreCase("addsubprogram")) { %>
			<td><%= Bean.clubfundXML.getfieldTransl("id_target_prg_parent", false) %>
				<%= Bean.getGoToTargetProgramLink(subprogram.getValue("ID_TARGET_PRG")) %>
			</td> <td><input type="text" name="name_target_prg_parent" size="60" value="<%=subprogram.getValue("NAME_TARGET_PRG") %>" readonly="readonly" class="inputfield-ro"></td>
		<% } else { %>
			<td colspan="2">&nbsp;</td>
		<% } %>
			<td><%= Bean.clubfundXML.getfieldTransl("target_program_date_end", false) %></td> <td><%=Bean.getCalendarInputField("date_end", "", "10") %></td>
		</tr>
	    <tr>
		<% if (action.equalsIgnoreCase("addsubprogram")) { %>
			<td><%= Bean.clubfundXML.getfieldTransl("initiator_target_prg", true) %>
				<%= Bean.getGoToNatPrsLink(subprogram.getValue("ID_NAT_PRS_INITIATOR")) %>
			</td>
			<td>
				<%=Bean.getWindowFindNatPrs("nat_prs_initiator", subprogram.getValue("ID_NAT_PRS_INITIATOR"), subprogram.getValue("NAME_NAT_PRS_INITIATOR"), "50") %>
			</td>
		<% } else { %>
			<td><%= Bean.clubfundXML.getfieldTransl("initiator_target_prg", true) %></td>
			<td>
				<%=Bean.getWindowFindNatPrs("nat_prs_initiator", "", "", "50") %>
			</td>
		<% } %>
			<td colspan="2" rowspan="8">
				<div id="div_photo" class="photo_rect">
					<p><%=Bean.clubfundXML.getfieldTransl("titl_picture_load", false) %></p>
					<input type="file" class="photo_file" name="client_photo" size="50" value="" accept="image/*" capture="camera" onchange="load_photo()">
				</div>
			</td>
		</tr>
	    <tr>
		<% if (action.equalsIgnoreCase("addsubprogram")) { %>
			<td><%= Bean.clubfundXML.getfieldTransl("administrator_target_prg", true) %>
				<%= Bean.getGoToNatPrsLink(subprogram.getValue("ID_NAT_PRS_ADMINISTRATOR")) %>
			</td>
			<td>
				<%=Bean.getWindowFindNatPrs("nat_prs_administrator", subprogram.getValue("ID_NAT_PRS_ADMINISTRATOR"), subprogram.getValue("NAME_NAT_PRS_ADMINISTRATOR"), "50") %>
			</td>
		<% } else { %>
			<td><%= Bean.clubfundXML.getfieldTransl("administrator_target_prg", true) %></td>
			<td>
				<%=Bean.getWindowFindNatPrs("nat_prs_administrator", "", "", "50") %>
			</td>
		<% } %>
		</tr>
		<tr>
		<% if (action.equalsIgnoreCase("addsubprogram")) { %>
			<td><%= Bean.clubfundXML.getfieldTransl("sname_jur_prs", true) %>
				<% if (action.equalsIgnoreCase("addsubprogram")) { %>
				<%= Bean.getGoToJurPrsHyperLink(subprogram.getValue("ID_JUR_PRS")) %>
				<% } %>
			</td>
			<td>
				<%=Bean.getWindowFindJurPrs("jur_prs", subprogram.getValue("ID_JUR_PRS"), subprogram.getValue("NAME_JUR_PRS"), "50") %>
			</td>
		<% } else { %>
			<td><%= Bean.clubfundXML.getfieldTransl("sname_jur_prs", true) %></td>
			<td>
				<%=Bean.getWindowFindJurPrs("jur_prs", "", "", "50") %>
			</td>
		<% } %>
		</tr>
   		<tr>
			<td><%= Bean.clubcardXML.getfieldTransl("id_doc", false) %></td> 
			<td>
				<%=Bean.getWindowDocuments("doc", "", "50") %>
			</td>
   		</tr>
	    <tr>
			<td rowspan="3"><%= Bean.clubfundXML.getfieldTransl("desc_target_prg", false) %></td> <td rowspan="3"><textarea name="desc_target_prg" cols="57" rows="3" class="inputfield"></textarea></td>
		</tr>
		<tr><td colspan="2">&nbsp;</td></tr>
		<tr><td colspan="2">&nbsp;</td></tr>
		<tr><td colspan="2">&nbsp;</td></tr>
	    <tr>
			<td colspan="2" class="top_line"><b><%= Bean.clubfundXML.getfieldTransl("target_program_fees", false) %></b></td>
			<td colspan="2" class="top_line"><b><%= Bean.clubfundXML.getfieldTransl("target_program_subscribe", false) %></b></td>
		</tr>
	    <tr>
			<td><%= Bean.clubfundXML.getfieldTransl("cd_currency", true) %></td> <td><select name="cd_currency" class="inputfield" ><%=Bean.getCurrencyOptions(club.getValue("CD_CURRENCY_BASE"), true) %></select></td>
			<td><%= Bean.clubfundXML.getfieldTransl("need_subscribe", false) %></td> <td><select name="need_subscribe" class="inputfield" ><%=Bean.getMeaningFromLookupNameOptions("YES_NO", "N", true) %></select></td>
		</tr>
	    <tr>
			<td><%= Bean.clubfundXML.getfieldTransl("entrance_fee", false) %></td> <td><input type="text" name="entrance_fee" size="15" value="" class="inputfield"></td>
			<td><%= Bean.clubfundXML.getfieldTransl("need_administrator_confirm", false) %></td> <td><select name="need_administrator_confirm" class="inputfield" ><%=Bean.getMeaningFromLookupNameOptions("YES_NO", "N", true) %></select></td>
		</tr>
	    <tr>
			<td><%= Bean.clubfundXML.getfieldTransl("membership_fee", false) %></td> <td><input type="text" name="membership_fee" size="15" value="" class="inputfield"> <%= Bean.clubfundXML.getfieldTransl("title_membership_for", false) %> <input type="text" name="membership_period_value" size="5" value="" class="inputfield"> <%= Bean.clubfundXML.getfieldTransl("title_membership_many_days", false) %></td>
			<td colspan="2" class="top_line_gray"><b><%= Bean.clubfundXML.getfieldTransl("title_others", false) %></b></td>
		</tr>
	    <tr>
			<td><%= Bean.clubfundXML.getfieldTransl("target_prg_pay_period", true) %></td> <td><select name="cd_target_prg_pay_period" id="cd_target_prg_pay_period" class="inputfield" onchange="changePayPeriod(this.value)"><%=Bean.getTargetPrgPayPeriodOptions("STUDY_COUNT", true) %></select></td>
			<td><%= Bean.clubfundXML.getfieldTransl("name_target_prg_in_sms", false) %></td> <td><input type="text" name="name_target_prg_in_sms" size="30" value="" class="inputfield"></td>
		</tr>
	    <tr>
			<td><span id="part1Title"><%= Bean.clubfundXML.getfieldTransl("pay_amount", false) %></span></td> <td><span id="part1Value"><input type="text" name="pay_amount" size="15" value="" class="inputfield"> <%= Bean.clubfundXML.getfieldTransl("title_membership_for", false) %> <input type="text" name="pay_count" size="5" value="" class="inputfield"> <%= Bean.clubfundXML.getfieldTransl("title_membership_studies", false) %></span></td>
		</tr>
		<tr>
			<td colspan="6" align="center">
				<%=Bean.getSubmitButtonMultiPart4(updateLink,"apply","updateForm7","div_main", "") %>
				<%=Bean.getResetButton() %>
				<%=Bean.getGoBackButton(backLink) %>
			</td>
		</tr>
	</table>

	</form>
	<%= Bean.getCalendarScript("date_beg", false) %>
	<%= Bean.getCalendarScript("date_end", false) %>

		<% 	
		} else if (action.equalsIgnoreCase("select")) {
			String tagTargetProgram = "" + back_type + "_SELECT_TARGET_PROGRAM";
			String tagTargetProgramFind = "" + back_type + "__SELECT_TARGET_PROGRAM";
			String tagTargetProgramPayPeriod = "" + back_type + "__SELECT_TARGET_PROGRAM_PAY_PERIOD";
			
			String l_target_program_page = Bean.getDecodeParam(parameters.get("target_program_page"));
			Bean.pageCheck(pageFormName + tagTargetProgram, l_target_program_page);
			String l_target_program_page_beg = Bean.getFirstRowNumber(pageFormName + tagTargetProgram);
			String l_target_program_page_end = Bean.getLastRowNumber(pageFormName + tagTargetProgram);
			
			String target_program_find 	= Bean.getDecodeParam(parameters.get("target_program_find"));
			target_program_find 	= Bean.checkFindString(pageFormName + tagTargetProgramFind, target_program_find, l_target_program_page);

			String target_program_pay_period 	= Bean.getDecodeParam(parameters.get("target_program_pay_period"));
			target_program_pay_period 	= Bean.checkFindString(pageFormName + tagTargetProgramPayPeriod, target_program_pay_period, l_target_program_page);

			bcListTargetProgram list = new bcListTargetProgram();
	    	
	    	String mySelectLink = "../crm/club/target_programupdate.jsp?back_type="+back_type+"&type=general&id="+external_id+"&id_jur_prs="+id_jur_prs+"&id_doc="+id_doc+"&id_target_prg="+id_target_prg+"&action=copy&process=no";
	    	
	    	%>
			<%= Bean.getOperationTitleShort(
					"",
					Bean.clubfundXML.getfieldTransl("h_select_target_program", false),
					"N",
					"N") 
			%>
			<table <%=Bean.getTableBottomFilter() %>><tbody>
			<tr>
				<%=Bean.getFindHTML("target_program_find", target_program_find, "../crm/club/target_programupdate.jsp?back_type="+back_type+"&type=general&id="+external_id+"&action=select&process=no&target_program_page=1&", "div_data_detail") %>
			
				<%=Bean.getSelectOnChangeBeginHTML("", "target_program_pay_period", "../crm/club/target_programupdate.jsp?back_type="+back_type+"&type=general&id="+external_id+"&action=select&process=no&target_program_page=1", Bean.clubfundXML.getfieldTransl("target_prg_pay_period", false), "div_data_detail") %>
					<%= Bean.getTargetPrgPayPeriodOptions(target_program_pay_period, true) %>
				<%=Bean.getSelectOnChangeEndHTML() %>

				<%= Bean.getPagesHTML(pageFormName + tagTargetProgram, "../crm/club/target_programupdate.jsp?back_type="+back_type+"&type=general&id="+external_id+"&action=select&process=no&", "target_program_page", "", "div_data_detail") %>
	
			</tr>
			</tbody>
			</table>

	    	<%=list.getTargetProgramsHTMLOnlySelect(target_program_find, target_program_pay_period, mySelectLink, l_target_program_page_beg, l_target_program_page_end) %>
			<%
		} else if (action.equalsIgnoreCase("edit") || action.equalsIgnoreCase("copy")) {

			bcClubTargetProgramObject program = new bcClubTargetProgramObject(id_target_prg);
	        %> 
			<%= Bean.getOperationTitleShort(
					"",
					Bean.clubfundXML.getfieldTransl("h_update_target_program", false),
					"Y",
					"Y") 
			%>

		<script>
		var formValidateData = new Array (
				new Array ('name_target_prg', 'varchar2', 1),
				new Array ('sname_target_prg', 'varchar2', 1),
				new Array ('name_nat_prs_initiator', 'varchar2', 1),
				new Array ('name_nat_prs_administrator', 'varchar2', 1),
				new Array ('name_jur_prs', 'varchar2', 1),
				new Array ('date_beg', 'varchar2', 1)
			);
			var formPayDetail = new Array (
				new Array ('cd_currency', 'varchar2', 1),
				new Array ('cd_target_prg_pay_period', 'varchar2', 1)
			);
			var formPayAmount = new Array (
				new Array ('pay_amount', 'number', 1)
			);
			var formPayCount = new Array (
				new Array ('pay_count', 'number', 1)
			);
			
			function myValidateForm() {

				<% if ("0".equalsIgnoreCase(program.getValue("CHILD_COUNT"))) { %>
				formValidateData = formValidateData.concat(formPayDetail);
				var cd_target_prg_pay_period = document.getElementById('cd_target_prg_pay_period');
				if (cd_target_prg_pay_period.value!='IRREGULAR' && cd_target_prg_pay_period.value!='') {
					formValidateData = formValidateData.concat(formPayAmount);
				}
				if (cd_target_prg_pay_period.value=='STUDY_COUNT') {
					formValidateData = formValidateData.concat(formPayCount);
				}
				<% } %>
				return validateForm(formValidateData);
			}

			function del_photo() {
				var msg='<%=Bean.buttonXML.getfieldTransl("photo_delete", false) %>?';
				var res=window.confirm(msg);
				if (res) {
					img = document.getElementById('image-0');
					img.src = '';
					div_photo = document.getElementById('div_photo');
					div_photo.innerHTML = '<p><%=Bean.clubfundXML.getfieldTransl("titl_picture_load", false) %></p><input type="hidden" name="id_loaded_file" value="-1"><input type="file" class="photo_file" name="client_photo" size="50" value="" accept="image/*" capture="camera" onchange="load_photo()">';
					//post_form('service/load_photo.jsp','delPhoto','div_photo');
				}
			}
			function load_photo() {
				action = document.getElementById('action');
				action.value = 'load_photo';
				post_form('<%=updateLink%>','updateForm7','div_photo');
				action.value = 'edit';
				//alert('action.value='+action.value);
			}
			function changePayPeriod(val) {
				part1Title = document.getElementById('part1Title');
				part1Value = document.getElementById('part1Value');
				if (val=='IRREGULAR') {
					part1Title.innerHTML='<%= Bean.clubfundXML.getfieldTransl("min_pay_amount", false) %>';
					part1Value.innerHTML='<input type="text" name="min_pay_amount" size="15" value="" class="inputfield">';
				} else if (val=='YEAR') {
					part1Title.innerHTML='<%= Bean.clubfundXML.getfieldTransl("pay_amount", false) %>';
					part1Value.innerHTML='<input type="text" name="pay_amount" size="15" value="" class="inputfield"> <%= Bean.clubfundXML.getfieldTransl("title_membership_years", false) %>';
				} else if (val=='MONTH') {
					part1Title.innerHTML='<%= Bean.clubfundXML.getfieldTransl("pay_amount", false) %>';
					part1Value.innerHTML='<input type="text" name="pay_amount" size="15" value="" class="inputfield"> <%= Bean.clubfundXML.getfieldTransl("title_membership_months", false) %>';
				} else if (val=='WEEK') {
					part1Title.innerHTML='<%= Bean.clubfundXML.getfieldTransl("pay_amount", false) %>';
					part1Value.innerHTML='<input type="text" name="pay_amount" size="15" value="" class="inputfield"> <%= Bean.clubfundXML.getfieldTransl("title_membership_weeks", false) %>';
				} else if (val=='DAY') {
					part1Title.innerHTML='<%= Bean.clubfundXML.getfieldTransl("pay_amount", false) %>';
					part1Value.innerHTML='<input type="text" name="pay_amount" size="15" value="" class="inputfield"> <%= Bean.clubfundXML.getfieldTransl("title_membership_days", false) %>';
				} else if (val=='HOUR') {
					part1Title.innerHTML='<%= Bean.clubfundXML.getfieldTransl("pay_amount", false) %>';
					part1Value.innerHTML='<input type="text" name="pay_amount" size="15" value="" class="inputfield"> <%= Bean.clubfundXML.getfieldTransl("title_membership_hours", false) %>';
				} else if (val=='STUDY_COUNT') {
					part1Title.innerHTML='<%= Bean.clubfundXML.getfieldTransl("pay_amount", false) %>';
					part1Value.innerHTML='<input type="text" name="pay_amount" size="15" value="" class="inputfield"> <%= Bean.clubfundXML.getfieldTransl("title_membership_for", false) %> <input type="text" name="pay_count" size="5" value="" class="inputfield"> <%= Bean.clubfundXML.getfieldTransl("title_membership_studies", false) %>';
				} else {
					part1Title.innerHTML='&nbsp;';
					part1Value.innerHTML='&nbsp;';
				}
			}
		</script>
	<div id="div_detail">
    <form name="updateForm7" id="updateForm7" accept-charset="UTF-8" method="POST" enctype="multipart/form-data">
    	<input type="hidden" name="id_target_prg" value="<%=id_target_prg %>">
	    <input type="hidden" name="LUD" value="<%=program.getValue("LUD") %>">
	    <input type="hidden" name="action" id="action" value="<%=action %>">
    	<input type="hidden" name="process" value="yes">
    	<input type="hidden" name="type" value="general">
    	<input type="hidden" name="id" value="<%=external_id %>">
    	<input type="hidden" name="back_type" value="<%=back_type %>">
	<table <%=Bean.getTableDetailParam() %> style="border: 1px solid">
	    <tr>
			<td><%= Bean.clubfundXML.getfieldTransl("name_target_prg", true) %></td> <td><input type="text" name="name_target_prg" size="60" value="<%=(action.equalsIgnoreCase("copy"))?"":program.getValue("NAME_TARGET_PRG") %>" class="inputfield"></td>
			<td><%= Bean.clubXML.getfieldTransl("club", false) %>
				<%= Bean.getGoToClubLink(program.getValue("ID_CLUB")) %>
			</td><td><input type="text" name="name_club" size="30" value="<%= Bean.getClubShortName(program.getValue("ID_CLUB")) %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
	    <tr>
			<td><%= Bean.clubfundXML.getfieldTransl("sname_target_prg", true) %></td> <td><input type="text" name="sname_target_prg" size="60" value="<%=(action.equalsIgnoreCase("copy"))?"":program.getValue("SNAME_TARGET_PRG") %>" class="inputfield"></td>
			<td><%= Bean.clubfundXML.getfieldTransl("target_program_date_beg", true) %></td> <td><%=Bean.getCalendarInputField("date_beg", (action.equalsIgnoreCase("copy"))?"":program.getValue("DATE_BEG_FRMT"), "10") %></td>
		</tr>
	    <tr>
			<% if (!Bean.isEmpty(program.getValue("ID_TARGET_PRG_PARENT"))) { %>
			<td><%= Bean.clubfundXML.getfieldTransl("id_target_prg_parent", false) %>
				<%= Bean.getGoToTargetProgramLink(program.getValue("ID_TARGET_PRG_PARENT")) %>
			</td> <td><input type="text" name="name_target_prg_parent" size="60" value="<%=Bean.getTargetPrgName(program.getValue("ID_TARGET_PRG_PARENT")) %>" readonly="readonly" class="inputfield-ro"></td>
			<% } else { %>
			<td colspan="2">&nbsp;</td>
			<% } %>
			<td><%= Bean.clubfundXML.getfieldTransl("target_program_date_end", false) %></td> <td><%=Bean.getCalendarInputField("date_end", (action.equalsIgnoreCase("copy"))?"":program.getValue("DATE_END_FRMT"), "10") %></td>
		</tr>
	    <tr>
			<td><%= Bean.clubfundXML.getfieldTransl("initiator_target_prg", true) %>
				<%= Bean.getGoToNatPrsLink(program.getValue("ID_NAT_PRS_INITIATOR")) %>
			</td>
			<td>
				<%=Bean.getWindowFindNatPrs("nat_prs_initiator", program.getValue("ID_NAT_PRS_INITIATOR"), program.getValue("NAME_NAT_PRS_INITIATOR"), "50") %>
			</td>
			<td colspan="2" rowspan="8">
				<div id="div_photo" class="photo_rect">
					<% if (!(program.getValue("IMAGE_TARGET_PRG") == null || "".equalsIgnoreCase(program.getValue("IMAGE_TARGET_PRG")))) { %>
						<div onclick="del_photo()" title="<%=Bean.buttonXML.getfieldTransl("photo_delete", false) %>" class="del" id="delgimage-0"></div>
						<div title="<%=Bean.buttonXML.getfieldTransl("photo_edit", false) %>" class="edt" id="edtgimage-0">
							<input type="file" title="Изменить фото" onchange="load_photo()" capture="camera" accept="image/*" value="" size="50" name="client_photo" class="photo_file">
						</div>
						<img class="photo_image" id="image-0" height="150" src="../TargetProgramPicture?id_target_prg=<%=program.getValue("ID_TARGET_PRG") %>&noCache=<%=(new Date().getTime()) + Math.random() %>">
					<% } else { %>
						<p><%=Bean.clubfundXML.getfieldTransl("titl_picture_load", false) %></p>
						<input type="file" class="photo_file" name="client_photo" size="50" value="" accept="image/*" capture="camera" onchange="load_photo()">
					<% } %>
				</div>
			</td>
		</tr>
	    <tr>
			<td><%= Bean.clubfundXML.getfieldTransl("administrator_target_prg", true) %>
				<%= Bean.getGoToNatPrsLink(program.getValue("ID_NAT_PRS_ADMINISTRATOR")) %>
			</td>
			<td>
				<%=Bean.getWindowFindNatPrs("nat_prs_administrator", program.getValue("ID_NAT_PRS_ADMINISTRATOR"), program.getValue("NAME_NAT_PRS_ADMINISTRATOR"), "50") %>
			</td>
		</tr>
		<tr>
			<td><%= Bean.clubfundXML.getfieldTransl("sname_jur_prs", true) %>
				<%= Bean.getGoToJurPrsHyperLink(program.getValue("ID_JUR_PRS")) %>
			</td>
			<td>
				<%=Bean.getWindowFindJurPrs("jur_prs", (action.equalsIgnoreCase("copy")?(back_type.equalsIgnoreCase("PARTNER")?external_id:""):program.getValue("ID_JUR_PRS")), (action.equalsIgnoreCase("copy")?"":program.getValue("SNAME_JUR_PRS")), "50") %>
			</td>
		</tr>
   		<tr>
			<td><%= Bean.clubcardXML.getfieldTransl("id_doc", false) %>
				<%= Bean.getGoToDocLink(program.getValue("ID_DOC")) %>
			</td> 
			<td>
				<%=Bean.getWindowDocuments("doc", program.getValue("ID_DOC"), "50") %>
			</td>
   		</tr>
	    <tr>
			<td rowspan="3"><%= Bean.clubfundXML.getfieldTransl("desc_target_prg", false) %></td> <td rowspan="3"><textarea name="desc_target_prg" cols="57" rows="3" class="inputfield"><%= program.getValue("DESC_TARGET_PRG") %></textarea></td>
		</tr>
   		<tr>
			<td colspan="2">&nbsp;</td>
   		</tr>
   		<tr>
			<td colspan="2">&nbsp;</td>
   		</tr>
		<% if ("0".equalsIgnoreCase(program.getValue("CHILD_COUNT"))) { %>
	    <tr><td colspan="4">&nbsp;</td></tr>
	    <tr>
			<td colspan="2" class="top_line"><b><%= Bean.clubfundXML.getfieldTransl("target_program_fees", false) %></b></td>
			<td colspan="2" class="top_line"><b><%= Bean.clubfundXML.getfieldTransl("target_program_subscribe", false) %></b></td>
		</tr>
	    <tr>
			<td><%= Bean.clubfundXML.getfieldTransl("cd_currency", true) %></td> <td><select name="cd_currency" class="inputfield" ><%=Bean.getCurrencyOptions(program.getValue("CD_CURRENCY"), true) %></select></td>
			<td><%= Bean.clubfundXML.getfieldTransl("need_subscribe", false) %></td> <td><select name="need_subscribe" class="inputfield" ><%=Bean.getMeaningFromLookupNameOptions("YES_NO", program.getValue("NEED_SUBSCRIBE"), true) %></select></td>
		</tr>
	    <tr>
			<td><%= Bean.clubfundXML.getfieldTransl("entrance_fee", false) %></td> <td><input type="text" name="entrance_fee" size="15" value="<%=program.getValue("ENTRANCE_FEE_FRMT") %>" class="inputfield"></td>
			<td><%= Bean.clubfundXML.getfieldTransl("need_administrator_confirm", false) %></td> <td><select name="need_administrator_confirm" class="inputfield" ><%=Bean.getMeaningFromLookupNameOptions("YES_NO", program.getValue("NEED_ADMINISTRATOR_CONFIRM"), true) %></select></td>
		</tr>
	    <tr>
			<td><%= Bean.clubfundXML.getfieldTransl("membership_fee", false) %></td> <td><input type="text" name="membership_fee" size="15" value="<%=program.getValue("MEMBERSHIP_FEE_FRMT") %>" class="inputfield"> <%= Bean.clubfundXML.getfieldTransl("title_membership_for", false) %> <input type="text" name="membership_period_value" size="5" value="<%=program.getValue("MEMBERSHIP_PERIOD_VALUE") %>" class="inputfield"> <%= Bean.clubfundXML.getfieldTransl("title_membership_many_days", false) %></td>
			<td colspan="2" class="top_line_gray"><b><%= Bean.clubfundXML.getfieldTransl("title_others", false) %></b></td>
		</tr>
	    <tr>
			<td><%= Bean.clubfundXML.getfieldTransl("target_prg_pay_period", true) %></td> <td><select name="cd_target_prg_pay_period" id="cd_target_prg_pay_period" class="inputfield" onchange="changePayPeriod(this.value)"><%=Bean.getTargetPrgPayPeriodOptions(program.getValue("CD_TARGET_PRG_PAY_PERIOD"), true) %></select></td>
			<td><%= Bean.clubfundXML.getfieldTransl("name_target_prg_in_sms", false) %></td> <td><input type="text" name="name_target_prg_in_sms" size="30" value="<%=program.getValue("NAME_TARGET_PRG_IN_SMS") %>" class="inputfield"></td>
		</tr>
	    <tr>
			<% if ("IRREGULAR".equalsIgnoreCase(program.getValue("CD_TARGET_PRG_PAY_PERIOD"))) { %>
				<td><span id="part1Title"><%= Bean.clubfundXML.getfieldTransl("min_pay_amount", false) %></span></td> <td><span id="part1Value"><input type="text" name="min_pay_amount" size="15" value="<%=program.getValue("MIN_PAY_AMOUNT") %>" class="inputfield"></span></td>
			<%} else if ("YEAR".equalsIgnoreCase(program.getValue("CD_TARGET_PRG_PAY_PERIOD"))) {%>
				<td><span id="part1Title"><%= Bean.clubfundXML.getfieldTransl("pay_amount", false) %></span></td> <td><span id="part1Value"><input type="text" name="pay_amount" size="15" value="<%=program.getValue("PAY_AMOUNT_FRMT") %>" class="inputfield"> <%= Bean.clubfundXML.getfieldTransl("title_membership_years", false) %></span></td>
			<%} else if ("MONTH".equalsIgnoreCase(program.getValue("CD_TARGET_PRG_PAY_PERIOD"))) {%>
				<td><span id="part1Title"><%= Bean.clubfundXML.getfieldTransl("pay_amount", false) %></span></td> <td><span id="part1Value"><input type="text" name="pay_amount" size="15" value="<%=program.getValue("PAY_AMOUNT_FRMT") %>" class="inputfield"> <%= Bean.clubfundXML.getfieldTransl("title_membership_months", false) %></span></td>
			<%} else if ("WEEK".equalsIgnoreCase(program.getValue("CD_TARGET_PRG_PAY_PERIOD"))) {%>
				<td><span id="part1Title"><%= Bean.clubfundXML.getfieldTransl("pay_amount", false) %></span></td> <td><span id="part1Value"><input type="text" name="pay_amount" size="15" value="<%=program.getValue("PAY_AMOUNT_FRMT") %>" class="inputfield"> <%= Bean.clubfundXML.getfieldTransl("title_membership_weeks", false) %></span></td>
			<%} else if ("DAY".equalsIgnoreCase(program.getValue("CD_TARGET_PRG_PAY_PERIOD"))) {%>
				<td><span id="part1Title"><%= Bean.clubfundXML.getfieldTransl("pay_amount", false) %></span></td> <td><span id="part1Value"><input type="text" name="pay_amount" size="15" value="<%=program.getValue("PAY_AMOUNT_FRMT") %>" class="inputfield"> <%= Bean.clubfundXML.getfieldTransl("title_membership_days", false) %></span></td>
			<%} else if ("HOUR".equalsIgnoreCase(program.getValue("CD_TARGET_PRG_PAY_PERIOD"))) {%>
				<td><span id="part1Title"><%= Bean.clubfundXML.getfieldTransl("pay_amount", false) %></span></td> <td><span id="part1Value"><input type="text" name="pay_amount" size="15" value="<%=program.getValue("PAY_AMOUNT_FRMT") %>" class="inputfield"> <%= Bean.clubfundXML.getfieldTransl("title_membership_hours", false) %></span></td>
			<%} else if ("STUDY_COUNT".equalsIgnoreCase(program.getValue("CD_TARGET_PRG_PAY_PERIOD"))) {%>
				<td><span id="part1Title"><%= Bean.clubfundXML.getfieldTransl("pay_amount", false) %></span></td> <td><span id="part1Value"><input type="text" name="pay_amount" size="15" value="<%=program.getValue("PAY_AMOUNT_FRMT") %>" class="inputfield"> <%= Bean.clubfundXML.getfieldTransl("title_membership_for", false) %> <input type="text" name="pay_count" size="5" value="<%=program.getValue("PAY_COUNT") %>" class="inputfield"> <%= Bean.clubfundXML.getfieldTransl("title_membership_studies", false) %></span></td>
			<%} else {%>
				<td><span id="part1Title">&nbsp;</span></td> <td><span id="part1Value">&nbsp;</span></td>
			<% } %>
		</tr>
		<% } %>
	    <%=	Bean.getIdCreationAndMoficationRecordFields(
	    		program.getValue("ID_TARGET_PRG"),
				program.getValue(Bean.getCreationDateFieldName()),
				program.getValue("CREATED_BY"),
				program.getValue(Bean.getLastUpdateDateFieldName()),
				program.getValue("LAST_UPDATE_BY")
			) %>
		<tr>
			<td colspan="6" align="center">
				<%=Bean.getSubmitButtonMultiPart4(updateLink,"apply","updateForm7","div_detail", "") %>
				<%=Bean.getResetButton() %>
				<%=Bean.getGoBackButton(backLink) %>
			</td>
		</tr>
	</table>

	</form>
	</div>
	<%= Bean.getCalendarScript("date_beg", false) %>
	<%= Bean.getCalendarScript("date_end", false) %>
<%
	   	} else {
   	    %> <%= Bean.getUnknownActionText(action) %><%
   		}
	} else if (process.equalsIgnoreCase("yes")) {    
		
		// Пока этот параметр всегда такой
		membership_period = "DAY";

		if (action.equalsIgnoreCase("add")) { 
				
			ArrayList<String> pParam = new ArrayList<String>();
					
			pParam.add(id_club);
			pParam.add(id_target_prg_parent);
			pParam.add(name_target_prg);
			pParam.add(sname_target_prg);
			pParam.add(name_target_prg_in_sms);
			pParam.add(desc_target_prg);
			pParam.add(date_beg);
			pParam.add(date_end);
			pParam.add(id_nat_prs_initiator);
			pParam.add(id_nat_prs_administrator);
			pParam.add(id_jur_prs);
			pParam.add(cd_currency);
			pParam.add(cd_target_prg_pay_period);
			pParam.add(pay_amount);
			pParam.add(pay_count);
			pParam.add(need_subscribe);
			pParam.add(need_administrator_confirm);
			pParam.add(entrance_fee);
			pParam.add(min_pay_amount);
			pParam.add(membership_fee);
			pParam.add(membership_period);
			pParam.add(membership_period_value);
			pParam.add(id_loaded_file);
			pParam.add(id_doc);
			pParam.add(Bean.getDateFormat());
	
		 	%>
			<%= Bean.executeInsertFunction("PACK$TARGET_PRG_UI.add_target_prg", pParam, backLink + "&id_target_prg=", backLink) %>
			<% 	
	
		} else if (action.equalsIgnoreCase("remove")) {
			ArrayList<String> pParam = new ArrayList<String>();
					
			pParam.add(id_target_prg);
			
		 	%>
			<%= Bean.executeDeleteFunction("PACK$TARGET_PRG_UI.delete_target_prg", pParam, generalLink , "") %>
			<% 	
	
		} else if (action.equalsIgnoreCase("edit")) {
			ArrayList<String> pParam = new ArrayList<String>();
						
			pParam.add(id_target_prg);
			pParam.add(name_target_prg);
			pParam.add(sname_target_prg);
			pParam.add(name_target_prg_in_sms);
			pParam.add(desc_target_prg);
			pParam.add(date_beg);
			pParam.add(date_end);
			pParam.add(id_nat_prs_initiator);
			pParam.add(id_nat_prs_administrator);
			pParam.add(id_jur_prs);
			pParam.add(cd_currency);
			pParam.add(cd_target_prg_pay_period);
			pParam.add(pay_amount);
			pParam.add(pay_count);
			pParam.add(need_subscribe);
			pParam.add(need_administrator_confirm);
			pParam.add(entrance_fee);
			pParam.add(min_pay_amount);
			pParam.add(membership_fee);
			pParam.add(membership_period);
			pParam.add(membership_period_value);
			pParam.add(id_loaded_file);
			pParam.add(id_doc);
			pParam.add(LUD);
			pParam.add(Bean.getDateFormat());
	
		 	%>
			<%= Bean.executeUpdateFunction("PACK$TARGET_PRG_UI.update_target_prg", pParam, backLink, "") %>
			<% 	
		} else if (action.equalsIgnoreCase("copy")) {
			ArrayList<String> pParam = new ArrayList<String>();
						
			pParam.add(id_target_prg);
			pParam.add(name_target_prg);
			pParam.add(sname_target_prg);
			pParam.add(desc_target_prg);
			pParam.add(date_beg);
			pParam.add(date_end);
			pParam.add(id_nat_prs_initiator);
			pParam.add(id_nat_prs_administrator);
			pParam.add(id_jur_prs);
			pParam.add(cd_currency);
			pParam.add(cd_target_prg_pay_period);
			pParam.add(pay_amount);
			pParam.add(pay_count);
			pParam.add(need_subscribe);
			pParam.add(need_administrator_confirm);
			pParam.add(entrance_fee);
			pParam.add(min_pay_amount);
			pParam.add(membership_fee);
			pParam.add(membership_period);
			pParam.add(membership_period_value);
			pParam.add(id_loaded_file);
			pParam.add(id_doc);
			pParam.add(LUD);
			pParam.add(Bean.getDateFormat());
	
		 	%>
			<%= Bean.executeInsertFunction("PACK$TARGET_PRG_UI.copy_target_prg", pParam, backLink + "&id_target_prg=", backLink) %>
			<% 	
		} else if (action.equalsIgnoreCase("load_photo")) {
			if (!loadError) {

				
				String resultInt        = "0";
				String resultMessage    = "";
				
				ArrayList<String> pParam = new ArrayList<String>();
					
				pParam.add(photoFileName);
				pParam.add(fullPhotoFileName);
					
				String[] results = new String[3];
					
				results 		= Bean.executeFunction("PACK$TARGET_PRG_UI.fix_load_file_info", pParam, results.length);
				resultInt 		= results[0];
				id_loaded_file	= results[1];
				resultMessage 	= results[2];
				if (Bean.C_SUCCESS_RESULT.equalsIgnoreCase(resultInt)) {%>
					<div onclick="del_photo()" title="<%=Bean.buttonXML.getfieldTransl("photo_delete", false) %>" class="del" id="delgimage-0"></div>
						<div title="<%=Bean.buttonXML.getfieldTransl("photo_edit", false) %>" class="edt" id="edtgimage-0">
							<input type="file" title="Изменить фото" onchange="load_photo()" capture="camera" accept="image/*" value="" size="50" name="client_photo" class="photo_file">
						</div>
						<input type="hidden" name="id_loaded_file" value="<%=id_loaded_file %>">
						<img class="photo_image" id="image-0" height="150" src="../LoadedImage?id_file=<%=id_loaded_file %>&noCache=<%=(new Date().getTime()) + Math.random() %>">
				<% } else { %>
					<p>error: <%=resultMessage %></p>
					<input type="file" class="photo_file" name="client_photo" size="50" value="" accept="image/*" capture="camera" onchange="load_photo()">
				<% } 
			}
			
		} else {%> 
			<%= Bean.getUnknownActionText(action) %><% 
		}
	} else {%> 
		<%= Bean.getUnknownProcessText(process) %><% 
	}
} else  if (type.equalsIgnoreCase("rests")) {
	if (process.equalsIgnoreCase("yes")) {
		if (action.equalsIgnoreCase("run")) {
			String lBackLink = "";
			if (!(id_target_prg==null || "".equalsIgnoreCase(id_target_prg) || "empty".equalsIgnoreCase(id_target_prg))) {
				lBackLink = "../crm/club/target_programspecs.jsp?id="+id_target_prg;
			} else {
				lBackLink = "../crm/club/target_program.jsp";
			}

			ArrayList<String> pParam = new ArrayList<String>();
							
			pParam.add(id_target_prg);		  	     
		 	%>
			
			<%= Bean.executeUpdateFunction("PACK$TARGET_PRG_UI.calc_target_prg_rest_initial", pParam, lBackLink, "") %>
			<% 	
	    } else { %> 
	    	<%= Bean.getUnknownActionText(action) %><% 
	    }
		  
	} else {%> 
		<%= Bean.getUnknownProcessText(process) %> <%
	}
} else if (type.equalsIgnoreCase("service_place")) {
	bcClubTargetProgramObject program = new bcClubTargetProgramObject(id_target_prg);
	//System.out.println(parameters.toString());
	
	if (process.equalsIgnoreCase("no")) {
		if (action.equalsIgnoreCase("add")) {
			
	        %> 
			<%= Bean.getOperationTitleShort(
					"",
					Bean.clubfundXML.getfieldTransl("h_add_service_place", false),
					"Y",
					"Y") 
			%>
		<script>
			var formData = new Array (
				new Array ('name_service_place', 'varchar2', 1),
				new Array ('date_beg', 'varchar2', 1)
			);
			
			function myValidateForm() {

				return validateForm(formData);
			}
		</script>
    <form name="updateForm7" id="updateForm7" accept-charset="UTF-8" method="POST">
		 <input type="hidden" name="action" id="action" value="add">
    	<input type="hidden" name="process" value="yes">
    	<input type="hidden" name="type" value="service_place">
    	<input type="hidden" name="id_target_prg" value="<%=id_target_prg %>">
	<table <%=Bean.getTableDetailParam() %> style="border: 1px solid">
	    <tr>
			<td><%= Bean.clubfundXML.getfieldTransl("name_target_prg", false) %></td> <td><input type="text" name="name_target_prg" size="60" value="<%=program.getValue("NAME_TARGET_PRG") %>" readonly="readonly" class="inputfield-ro"></td>
			<td colspan="2">&nbsp;</td>
		</tr>
	    <tr>
			<td><%= Bean.clubfundXML.getfieldTransl("sname_service_place", true) %></td>
			<td>
				<%=Bean.getWindowFindServicePlace("service_place", "", "", "50") %>
			</td>
			<td colspan="2">&nbsp;</td>
		</tr>
	    <tr>
			<td><%= Bean.clubfundXML.getfieldTransl("payment_date_beg", true) %></td> <td><%=Bean.getCalendarInputField("date_beg", "", "10") %></td>
			<td colspan="2">&nbsp;</td>
		</tr>
	    <tr>
			<td><%= Bean.clubfundXML.getfieldTransl("payment_date_end", false) %></td> <td><%=Bean.getCalendarInputField("date_end", "", "10") %></td>
			<td colspan="2">&nbsp;</td>
		</tr>
		<tr>
			<td colspan="6" align="center">
				<%=Bean.getSubmitButtonAjax("../crm/club/target_programupdate.jsp","apply","updateForm7","div_data_detail", "") %>
				<%=Bean.getResetButton() %>
				<%=Bean.getGoBackButton("../crm/club/target_programspecs.jsp?id="+id_target_prg) %>
			</td>
		</tr>
	</table>

	</form>
	<%= Bean.getCalendarScript("date_beg", false) %>
	<%= Bean.getCalendarScript("date_end", false) %>

		<% 	
	   	} else if (action.equalsIgnoreCase("edit")) {
	   		
	   		bcTargetProgramServicePlaceObject place = new bcTargetProgramServicePlaceObject(id_target_prg_place);
			
	        %> 
			<%= Bean.getOperationTitleShort(
					"",
					Bean.clubfundXML.getfieldTransl("h_update_service_place", false),
					"Y",
					"Y") 
			%>
		<script>
			var formData = new Array (
				new Array ('name_service_place', 'varchar2', 1),
				new Array ('date_beg', 'varchar2', 1)
			);
			
			function myValidateForm() {

				return validateForm(formData);
			}
		</script>
    <form name="updateForm7" id="updateForm7" accept-charset="UTF-8" method="POST">
		 <input type="hidden" name="action" id="action" value="edit">
    	<input type="hidden" name="process" value="yes">
    	<input type="hidden" name="type" value="service_place">
    	<input type="hidden" name="id_target_prg" value="<%=id_target_prg %>">
    	<input type="hidden" name="id_target_prg_place" value="<%=id_target_prg_place %>">
    	<input type="hidden" name="LUD" value="<%=place.getValue("LUD") %>">
	<table <%=Bean.getTableDetailParam() %> style="border: 1px solid">
	    <tr>
			<td><%= Bean.clubfundXML.getfieldTransl("name_target_prg", false) %></td> <td><input type="text" name="name_target_prg" size="60" value="<%=program.getValue("NAME_TARGET_PRG") %>" readonly="readonly" class="inputfield-ro"></td>
			<td colspan="2">&nbsp;</td>
		</tr>
	    <tr>
			<td><%= Bean.clubfundXML.getfieldTransl("sname_service_place", true) %></td>
			<td>
				<%=Bean.getWindowFindServicePlace("service_place", place.getValue("ID_SERVICE_PLACE"), place.getValue("SNAME_SERVICE_PLACE"), "50") %>
			</td>
			<td colspan="2">&nbsp;</td>
		</tr>
	    <tr>
			<td><%= Bean.clubfundXML.getfieldTransl("payment_date_beg", true) %></td> <td><%=Bean.getCalendarInputField("date_beg", place.getValue("DATE_BEG_DF"), "10") %></td>
			<td colspan="2">&nbsp;</td>
		</tr>
	    <tr>
			<td><%= Bean.clubfundXML.getfieldTransl("payment_date_end", false) %></td> <td><%=Bean.getCalendarInputField("date_end", place.getValue("DATE_END_DF"), "10") %></td>
			<td colspan="2">&nbsp;</td>
		</tr>
		<tr>
			<td colspan="6" align="center">
				<%=Bean.getSubmitButtonAjax("../crm/club/target_programupdate.jsp","apply","updateForm7","div_data_detail", "") %>
				<%=Bean.getResetButton() %>
				<%=Bean.getGoBackButton("../crm/club/target_programspecs.jsp?id="+id_target_prg) %>
			</td>
		</tr>
	</table>

	</form>
	<%= Bean.getCalendarScript("date_beg", false) %>
	<%= Bean.getCalendarScript("date_end", false) %>

		<% 	
	   	} else {
   	    %> <%= Bean.getUnknownActionText(action) %><%
   		}
	} else if (process.equalsIgnoreCase("yes")) {    
		
		// Пока этот параметр всегда такой
		membership_period = "DAY";
		String id_service_place		= Bean.getDecodeParam(parameters.get("id_service_place"));

		if (action.equalsIgnoreCase("add")) { 
				
			String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK$TARGET_PRG_UI.add_target_prg_place(" + 
				"?,?,?,?,?, ?,?)}";

			String[] pParam = new String [5];
					
			pParam[0] = id_target_prg;
			pParam[1] = id_service_place;
			pParam[2] = date_beg;
			pParam[3] = date_end;
			pParam[4] = Bean.getDateFormat();
	
		 	%>
			<%= Bean.getCallResultParam("INSERT", callSQL, pParam, "../crm/club/target_programspecs.jsp?id="+id_target_prg+"&id_target_prg_place=", "../crm/club/target_programspecs.jsp?id="+id_target_prg) %>
			<% 	
	
		} else if (action.equalsIgnoreCase("remove")) {
			String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK$TARGET_PRG_UI.delete_target_prg_place(?,?)}";

			String[] pParam = new String [1];
					
			pParam[0] = id_target_prg_place;
			
		 	%>
			<%= Bean.getCallResultParam("DELETE", callSQL, pParam, "../crm/club/target_programspecs.jsp?id="+id_target_prg, "../crm/club/target_programspecs.jsp?id="+id_target_prg) %>
			<% 	
	
		} else if (action.equalsIgnoreCase("edit")) {
			String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK$TARGET_PRG_UI.update_target_prg_place("+
				"?,?,?,?,?, ?)}";

			String[] pParam = new String [5];
						
			pParam[0] = id_target_prg_place;
			pParam[1] = date_beg;
			pParam[2] = date_end;
			pParam[3] = Bean.getDateFormat();
			pParam[4] = LUD;
	
		 	%>
			<%= Bean.getCallResultParam("UPDATE", callSQL, pParam, "../crm/club/target_programspecs.jsp?id=" + id_target_prg, "../crm/club/target_programspecs.jsp?id="+id_target_prg) %>
			<% 	
		} else {%> 
			<%= Bean.getUnknownActionText(action) %><% 
		}
	} else {%> 
		<%= Bean.getUnknownProcessText(process) %><% 
	}
} else if (type.equalsIgnoreCase("referral_scheme")) {
	bcClubTargetProgramObject program = new bcClubTargetProgramObject(id_target_prg);
	System.out.println(parameters.toString());
	
	if (process.equalsIgnoreCase("no")) {
		if (action.equalsIgnoreCase("add")) {
    		
			bcClubShortObject club = new bcClubShortObject(Bean.getCurrentClubID());
			
	        %> 
			<%= Bean.getOperationTitleShort(
					"",
					Bean.clubXML.getfieldTransl("H_ADD_REFERRAL_SCHEME", false),
					"Y",
					"Y") 
			%>
		<script>
			var formData = new Array (
				new Array ('cd_referral_scheme_calc_type', 'varchar2', 1),
				new Array ('name_referral_scheme', 'varchar2', 1),
				new Array ('name_club', 'varchar2', 1),
				new Array ('date_beg', 'varchar2', 1)
			);
			function myValidateForm() {
				return validateForm(formData);
			}
		</script>

		<form action="../crm/club/referral_schemeupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">  	        
	        <input type="hidden" name="type" value="referral_scheme">
	        <input type="hidden" name="action" value="add">
	        <input type="hidden" name="process" value="yes">
	        <input type="hidden" name="id_target_prg" value="<%=id_target_prg %>">
		<table <%=Bean.getTableDetailParam() %>>
		<tr>
			<td><%= Bean.clubXML.getfieldTransl("name_referral_scheme_type", false) %></td> <td><input type="text" name="name_referral_scheme_type" size="40" value="<%=Bean.getReferralShemeTypeName("TARGET_PROGRAM") %>" readonly="readonly" class="inputfield-ro"></td>
 		    <td><%= Bean.clubXML.getfieldTransl("club", true) %>
				<%= Bean.getGoToClubLink(club.getValue("ID_CLUB")) %>
			</td>
		  	<td>
				<%=Bean.getWindowFindClub("club", club.getValue("ID_CLUB"), club.getValue("SNAME_CLUB"), "25") %>
		  	</td>
		</tr>
   		<tr>
			<td><%= Bean.clubfundXML.getfieldTransl("name_target_prg", false) %></td> <td><input type="text" name="name_target_prg" size="45" value="<%=program.getValue("NAME_TARGET_PRG") %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.clubXML.getfieldTransl("date_beg", true) %></td><td><%=Bean.getCalendarInputField("date_beg", Bean.getSysDate(), "10") %></td>
   		</tr>
	    <tr>
			<td><%= Bean.clubXML.getfieldTransl("cd_referral_scheme_calc_type", true) %></td> <td><select name="cd_referral_scheme_calc_type" class="inputfield"><%= Bean.getReferralShemeCalcTypeOptions("", true) %></select></td>
            <td><%= Bean.clubXML.getfieldTransl("date_end", false) %></td><td><%=Bean.getCalendarInputField("date_end", "", "10") %></td>
		</tr>
	    <tr>
			<td><%= Bean.clubXML.getfieldTransl("name_referral_scheme", true) %></td> <td><input type="text" name="name_referral_scheme" size="70" value="" class="inputfield"></td>
			<td colspan="2">&nbsp;</td>
		</tr>
	    <tr>
			<td><%= Bean.clubXML.getfieldTransl("desc_referral_scheme", false) %></td> <td><textarea name="desc_referral_scheme" cols="67" rows="3" class="inputfield"></textarea></td>
			<td colspan="2">&nbsp;</td>
		</tr>
	 		<tr>
				<td colspan="6" align="center">
					<%=Bean.getSubmitButtonAjax("../crm/club/target_programupdate.jsp") %>
					<%=Bean.getResetButton() %>
					<%=Bean.getGoBackButton("../crm/club/target_programspecs.jsp?id=" + id_target_prg) %>
				</td>
			</tr>
		</table>
		</form>

	</form>
	<%= Bean.getCalendarScript("date_beg", false) %>
	<%= Bean.getCalendarScript("date_end", false) %>

		<% 	
	   	} else {
   	    %> <%= Bean.getUnknownActionText(action) %><%
   		}
	} else if (process.equalsIgnoreCase("yes")) {    
		
		String
  			id_referral_scheme				= Bean.getDecodeParam(parameters.get("id_referral_scheme")), 
			cd_referral_scheme_calc_type 	= Bean.getDecodeParam(parameters.get("cd_referral_scheme_calc_type")), 
	  		name_referral_scheme 			= Bean.getDecodeParam(parameters.get("name_referral_scheme")), 
	  		desc_referral_scheme 			= Bean.getDecodeParam(parameters.get("desc_referral_scheme"));

		if (action.equalsIgnoreCase("add")) { 
			ArrayList<String> pParam = new ArrayList<String>();	

			pParam.add("TARGET_PROGRAM");
			pParam.add(id_jur_prs);
			pParam.add(id_target_prg);
			pParam.add(name_referral_scheme);
			pParam.add(desc_referral_scheme);
			pParam.add(cd_referral_scheme_calc_type);
			pParam.add(id_club);
			pParam.add(date_beg);
			pParam.add(date_end);
			pParam.add(Bean.getDateFormat());
				
		 	%>
			<%= Bean.executeInsertFunction("PACK$REFERRAL_UI.add_referral_scheme", pParam, "../crm/club/target_programspecs.jsp?id=" + id_target_prg + "&id_referral_scheme=", "../crm/club/referral_scheme.jsp") %>
			<% 	
	
		} else if (action.equalsIgnoreCase("remove")) {
			String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK$REFERRAL_UI.delete_referral_scheme(?,?)}";

			ArrayList<String> pParam = new ArrayList<String>();	
			pParam.add(id_referral_scheme);
			
		 	%>
			<%= Bean.executeDeleteFunction("PACK$REFERRAL_UI.delete_referral_scheme", pParam, "../crm/club/target_programspecs.jsp?id=" + id_target_prg , "") %>
			<% 	
		} else {%> 
			<%= Bean.getUnknownActionText(action) %><% 
		}
	} else {%> 
		<%= Bean.getUnknownProcessText(process) %><% 
	}
} else if (type.equalsIgnoreCase("participant")) {
	bcClubTargetProgramObject program = new bcClubTargetProgramObject(id_target_prg);
	//System.out.println(parameters.toString());
	String 
		id_nat_prs_role_target_prg	= Bean.getDecodeParam(parameters.get("id_nat_prs_role_target_prg")),
		id_nat_prs_role				= Bean.getDecodeParam(parameters.get("id_nat_prs_role")),
		cd_target_prg_use_type		= Bean.getDecodeParam(parameters.get("cd_target_prg_use_type")),
		is_administrator_confirm	= Bean.getDecodeParam(parameters.get("is_administrator_confirm")),
		pay_entrance_fee			= Bean.getDecodeParam(parameters.get("pay_entrance_fee")),
		can_subscribe				= Bean.getDecodeParam(parameters.get("can_subscribe")),
		membership_fee_sum			= Bean.getDecodeParam(parameters.get("membership_fee_sum")),
		membership_last_date		= Bean.getDecodeParam(parameters.get("membership_last_date"));
	
	if (process.equalsIgnoreCase("no")) {
		if (action.equalsIgnoreCase("add")) {
			
	        %> 
			<%= Bean.getOperationTitleShort(
					"",
					Bean.clubfundXML.getfieldTransl("h_add_participant", false),
					"Y",
					"Y") 
			%>
		<script>
			var formData = new Array (
				new Array ('name_nat_prs_role', 'varchar2', 1),
				new Array ('cd_target_prg_use_type', 'varchar2', 1),
				new Array ('can_subscribe', 'varchar2', 1),
				new Array ('is_administrator_confirm', 'varchar2', 1)
			);
			
			function myValidateForm() {

				return validateForm(formData);
			}
		</script>
    <form name="updateForm7" id="updateForm7" accept-charset="UTF-8" method="POST">
		 <input type="hidden" name="action" id="action" value="add">
    	<input type="hidden" name="process" value="yes">
    	<input type="hidden" name="type" value="participant">
    	<input type="hidden" name="id_target_prg" value="<%=id_target_prg %>">
	<table <%=Bean.getTableDetailParam() %> style="border: 1px solid">
	    <tr>
			<td><%= Bean.clubfundXML.getfieldTransl("name_target_prg", false) %></td> <td><input type="text" name="name_target_prg" size="40" value="<%=program.getValue("NAME_TARGET_PRG") %>" readonly="readonly" class="inputfield-ro"></td>
			<td colspan="2">&nbsp;</td>
		</tr>
	    <tr>
			<td><%= Bean.clubXML.getfieldTransl("fio_nat_prs", true) %></td>
			  <td>
				  <%=Bean.getWindowFindNatPrsRole("nat_prs_role", "", "", "25") %>
			  </td>
			<td colspan="2">&nbsp;</td>
		</tr>
	    <tr>
			<td><%= Bean.clubfundXML.getfieldTransl("cd_target_prg_use_type", true) %></td> 
				<td>
				<% cd_target_prg_use_type = "SIGNED"; %>
				<%=Bean.getSelectBeginHTML("cd_target_prg_use_type", Bean.clubfundXML.getfieldTransl("cd_target_prg_use_type", false)) %>
					<%=Bean.getSelectOptionHTML(cd_target_prg_use_type, "", "") %>
					<%=Bean.getSelectOptionHTML(cd_target_prg_use_type, "SIGNED", Bean.clubfundXML.getfieldTransl("cd_target_prg_use_type_signed", false)) %>
					<%=Bean.getSelectOptionHTML(cd_target_prg_use_type, "PAYD", Bean.clubfundXML.getfieldTransl("cd_target_prg_use_type_paid", false)) %>
				<%=Bean.getSelectEndHTML() %>
				</td>
			<td><%= Bean.clubfundXML.getfieldTransl("pay_entrance_fee", false) %>, <b><%=program.getValue("SNAME_CURRENCY") %></b></td> <td><input type="text" name="pay_entrance_fee" size="15" value="" class="inputfield"></td>
		</tr>
	    <tr>
			<td><%= Bean.clubfundXML.getfieldTransl("can_subscribe", true) %></td> <td><select name="can_subscribe" class="inputfield" ><%=Bean.getMeaningFromLookupNameOptions("YES_NO", "N", true) %></select></td>
			<td><%= Bean.clubfundXML.getfieldTransl("membership_fee_sum", false) %>, <b><%=program.getValue("SNAME_CURRENCY") %></b></td> <td><input type="text" name="membership_fee_sum" size="15" value="" class="inputfield"></td>
		</tr>
	    <tr>
			<td><%= Bean.clubfundXML.getfieldTransl("is_administrator_confirm", true) %></td> <td><select name="is_administrator_confirm" class="inputfield" ><%=Bean.getMeaningFromLookupNameOptions("YES_NO", "N", true) %></select></td>
			<td><%= Bean.clubfundXML.getfieldTransl("membership_last_date", false) %></td> <td><%=Bean.getCalendarInputField("membership_last_date", "", "10") %></td>
		</tr>
		<tr>
			<td colspan="6" align="center">
				<%=Bean.getSubmitButtonAjax("../crm/club/target_programupdate.jsp","apply","updateForm7","div_data_detail", "") %>
				<%=Bean.getResetButton() %>
				<%=Bean.getGoBackButton("../crm/club/target_programspecs.jsp?id="+id_target_prg) %>
			</td>
		</tr>
	</table>

	</form>
	<%= Bean.getCalendarScript("membership_last_date", false) %>

		<% 	
	   	} else if (action.equalsIgnoreCase("edit")) {
	   		
	   		bcNatPrsRoleTargetPrgObject role = new bcNatPrsRoleTargetPrgObject(id_nat_prs_role_target_prg);
			
	        %> 
			<%= Bean.getOperationTitleShort(
					"",
					Bean.clubfundXML.getfieldTransl("h_update_participant", false),
					"Y",
					"Y") 
			%>
		<script>
			var formData = new Array (
				new Array ('cd_target_prg_use_type', 'varchar2', 1),
				new Array ('can_subscribe', 'varchar2', 1),
				new Array ('is_administrator_confirm', 'varchar2', 1)
			);
			
			function myValidateForm() {

				return validateForm(formData);
			}
		</script>
    <form name="updateForm7" id="updateForm7" accept-charset="UTF-8" method="POST">
		 <input type="hidden" name="action" id="action" value="edit">
    	<input type="hidden" name="process" value="yes">
    	<input type="hidden" name="type" value="participant">
    	<input type="hidden" name="id_target_prg" value="<%=id_target_prg %>">
    	<input type="hidden" name="id_nat_prs_role_target_prg" value="<%=id_nat_prs_role_target_prg %>">
    	<input type="hidden" name="LUD" value="<%=role.getValue("LUD") %>">
	<table <%=Bean.getTableDetailParam() %> style="border: 1px solid">
	    <tr>
			<td><%= Bean.clubfundXML.getfieldTransl("name_target_prg", false) %></td> <td><input type="text" name="name_target_prg" size="40" value="<%=program.getValue("NAME_TARGET_PRG") %>" readonly="readonly" class="inputfield-ro"></td>
			<td colspan="2">&nbsp;</td>
		</tr>
	    <tr>
			<td><%= Bean.clubfundXML.getfieldTransl("fio_nat_prs", false) %></td> <td><input type="text" name="fio_nat_prs" size="40" value="<%=role.getValue("FIO_NAT_PRS") %>" readonly="readonly" class="inputfield-ro"></td>
			<td colspan="2">&nbsp;</td>
		</tr>
	    <tr>
			<td><%= Bean.clubfundXML.getfieldTransl("cd_card1", false) %></td> <td><input type="text" name="cd_card1" size="20" value="<%=role.getValue("CD_CARD1") %>" readonly="readonly" class="inputfield-ro"></td>
			<td colspan="2">&nbsp;</td>
		</tr>
	    <tr>
			<td><%= Bean.clubfundXML.getfieldTransl("cd_target_prg_use_type", true) %></td> 
				<td>
				<% cd_target_prg_use_type = role.getValue("CD_TARGET_PRG_USE_TYPE"); %>
				<%=Bean.getSelectBeginHTML("cd_target_prg_use_type", Bean.clubfundXML.getfieldTransl("cd_target_prg_use_type", false)) %>
					<%=Bean.getSelectOptionHTML(cd_target_prg_use_type, "", "") %>
					<%=Bean.getSelectOptionHTML(cd_target_prg_use_type, "SIGNED", Bean.clubfundXML.getfieldTransl("cd_target_prg_use_type_signed", false)) %>
					<%=Bean.getSelectOptionHTML(cd_target_prg_use_type, "PAYD", Bean.clubfundXML.getfieldTransl("cd_target_prg_use_type_paid", false)) %>
				<%=Bean.getSelectEndHTML() %>
				</td>
			<td><%= Bean.clubfundXML.getfieldTransl("pay_entrance_fee", false) %>, <b><%=role.getValue("SNAME_CURRENCY") %></b></td> <td><input type="text" name="pay_entrance_fee" size="15" value="<%=role.getValue("PAY_ENTRANCE_FEE_FRMT") %>" class="inputfield"></td>
		</tr>
	    <tr>
			<td><%= Bean.clubfundXML.getfieldTransl("can_subscribe", true) %></td> <td><select name="can_subscribe" class="inputfield" ><%=Bean.getMeaningFromLookupNameOptions("YES_NO", role.getValue("CAN_SUBSCRIBE"), true) %></select></td>
			<td><%= Bean.clubfundXML.getfieldTransl("membership_fee_sum", false) %>, <b><%=role.getValue("SNAME_CURRENCY") %></b></td> <td><input type="text" name="membership_fee_sum" size="15" value="<%=role.getValue("MEMBERSHIP_FEE_SUM_FRMT") %>" class="inputfield"></td>
		</tr>
	    <tr>
			<td><%= Bean.clubfundXML.getfieldTransl("is_administrator_confirm", true) %></td> <td><select name="is_administrator_confirm" class="inputfield" ><%=Bean.getMeaningFromLookupNameOptions("YES_NO", role.getValue("IS_ADMINISTRATOR_CONFIRM"), true) %></select></td>
			<td><%= Bean.clubfundXML.getfieldTransl("membership_last_date", false) %></td> <td><%=Bean.getCalendarInputField("membership_last_date", role.getValue("MEMBERSHIP_LAST_DATE_DF"), "10") %></td>
		</tr>
		<tr>
			<td colspan="6" align="center">
				<%=Bean.getSubmitButtonAjax("../crm/club/target_programupdate.jsp","apply","updateForm7","div_data_detail", "") %>
				<%=Bean.getResetButton() %>
				<%=Bean.getGoBackButton("../crm/club/target_programspecs.jsp?id="+id_target_prg) %>
			</td>
		</tr>
	</table>

	</form>
	<%= Bean.getCalendarScript("membership_last_date", false) %>

		<% 	
	   	} else {
   	    %> <%= Bean.getUnknownActionText(action) %><%
   		}
	} else if (process.equalsIgnoreCase("yes")) { 

		if (action.equalsIgnoreCase("add")) { 
				
			ArrayList<String> pParam = new ArrayList<String>();
					
			pParam.add(id_nat_prs_role);
			pParam.add(id_target_prg);
			pParam.add(cd_target_prg_use_type);
			pParam.add(can_subscribe);
			pParam.add(is_administrator_confirm);
			pParam.add(pay_entrance_fee);
			pParam.add(membership_fee_sum);
			pParam.add(membership_last_date);
			pParam.add(Bean.getDateFormat());
	
		 	%>
			<%= Bean.executeInsertFunction("PACK$TARGET_PRG_UI.add_nat_prs_role_target_prg", pParam, "../crm/club/target_programspecs.jsp?id="+id_target_prg+"&id_nat_prs_role_target_prg=", "../crm/club/target_programspecs.jsp?id="+id_target_prg) %>
			<% 	
	
		} else if (action.equalsIgnoreCase("remove")) {
			ArrayList<String> pParam = new ArrayList<String>();
					
			pParam.add(id_nat_prs_role_target_prg);
			
		 	%>
			<%= Bean.executeDeleteFunction("PACK$TARGET_PRG_UI.delete_nat_prs_role_target_prg", pParam, "../crm/club/target_programspecs.jsp?id="+id_target_prg, "../crm/club/target_programspecs.jsp?id="+id_target_prg) %>
			<% 	
	
		} else if (action.equalsIgnoreCase("edit")) {
			ArrayList<String> pParam = new ArrayList<String>();
						
			pParam.add(id_nat_prs_role_target_prg);
			pParam.add(cd_target_prg_use_type);
			pParam.add(can_subscribe);
			pParam.add(is_administrator_confirm);
			pParam.add(pay_entrance_fee);
			pParam.add(membership_fee_sum);
			pParam.add(membership_last_date);
			pParam.add(Bean.getDateFormat());
			pParam.add(LUD);
	
		 	%>
			<%= Bean.executeUpdateFunction("PACK$TARGET_PRG_UI.update_nat_prs_role_target_prg", pParam, "../crm/club/target_programspecs.jsp?id=" + id_target_prg, "../crm/club/target_programspecs.jsp?id="+id_target_prg) %>
			<% 	
		} else {%> 
			<%= Bean.getUnknownActionText(action) %><% 
		}
	} else {%> 
		<%= Bean.getUnknownProcessText(process) %><% 
	}
} else {%> 
	<%= Bean.getUnknownTypeText(type) %> <%
}
  %>

</body>
</html>
