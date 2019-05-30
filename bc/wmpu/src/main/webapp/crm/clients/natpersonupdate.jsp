<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%@page import="bc.objects.bcNatPrsObject"%>
<%@page import="bc.objects.bcClubShortObject"%>

<%@page import="java.util.HashMap"%>
<%@page import="bc.objects.bcBankAccountObject"%>
<%@page import="java.util.ArrayList"%>
<%@page import="org.apache.commons.fileupload.servlet.ServletFileUpload"%>
<%@page import="java.util.List"%>
<%@page import="org.apache.commons.fileupload.FileItem"%>
<%@page import="org.apache.commons.fileupload.FileItemFactory"%>
<%@page import="org.apache.commons.fileupload.disk.DiskFileItemFactory"%>
<%@page import="javax.naming.Context"%>
<%@page import="javax.naming.InitialContext"%>
<%@page import="java.io.File"%>
<%@page import="java.awt.Image"%>
<%@page import="javax.imageio.ImageIO"%>


<%= Bean.getLogOutScript(request) %>

<%
HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String pageFormName = "CLIENTS_NATPERSONS";

Bean.setJspPageForTabName(pageFormName);

request.setCharacterEncoding("UTF-8");

String id			= Bean.getDecodeParamPrepare(parameters.get("id"));
String type			= Bean.getDecodeParamPrepare(parameters.get("type")); 
String action		= Bean.getDecodeParamPrepare(parameters.get("action")); 
String process		= Bean.getDecodeParamPrepare(parameters.get("process"));


String photoFileName 		= "";
String fullPhotoFileName 	= "";
boolean loadError              = false;

if(ServletFileUpload.isMultipartContent(request)){
	Context environmentContext = (Context) new InitialContext().lookup("java:/comp/env");
	String CONTEXT_PARAM_LOADED_FILES="wmpupt/images/icons";
	String photo_dir = (String)environmentContext.lookup(CONTEXT_PARAM_LOADED_FILES)+"nat_prs/";

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
	  			if ("id".equalsIgnoreCase(file.getFieldName())) id = Bean.decodeUtf(file.getString());

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

%>


<%@page import="java.util.Date"%>
<%@page import="bc.objects.bcNatPrsRoleObject"%><html>
<head>
	<%= Bean.getMetaContent() %>
	<%= Bean.getBottomFrameCSS() %>

	<script language="JavaScript">

</script>
</head>

<%
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
				new Array ('surname', 'varchar2', 1),
				new Array ('date_of_birth', 'varchar2', 1),
				new Array ('sex', 'varchar2', 1),
				new Array ('name_club', 'varchar2', 1)
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
	
			<%= Bean.getOperationTitle(
				Bean.natprsXML.getfieldTransl("LAB_ADD_NATPRS", false),
				"Y",
				"Y") 
			%>
	   <form action="../crm/clients/natpersonupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">
	        <input type="hidden" name="type" value="general">
	        <input type="hidden" name="action" value="add">
	        <input type="hidden" name="process" value="yes">
		<table <%=Bean.getTableDetailParam() %>>
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
				<td colspan="2">&nbsp;</td>
			</tr>
			<tr>
				<td><%= Bean.natprsXML.getfieldTransl("name", false) %> </td><td><input type="text" name="name" size="30" value="" class="inputfield"></td>
				<td><%= Bean.natprsXML.getfieldTransl("date_of_birth", true) %></td><td><%=Bean.getCalendarInputField("date_of_birth", "", "10") %></td>
			</tr>
			<tr>
				<td><%= Bean.natprsXML.getfieldTransl("patronymic", false) %></td><td><input type="text" name="patronymic" size="30" value="" class="inputfield"></td>
				<td><%= Bean.natprsXML.getfieldTransl("sex", true) %></td><td><select name="sex" class="inputfield"><%= Bean.getMeaningFromLookupNameOptions("MALE_FEMALE", "M", false) %></select> </td>
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
				<td rowspan="2"><%= Bean.natprsXML.getfieldTransl("pasport_text", false) %></td><td rowspan="2"><textarea name="pasport_text" cols="50" rows="2" class="inputfield"></textarea></td>
			</tr>
			<tr>
				<td><%= Bean.natprsXML.getfieldTransl("pasport_division_code", false) %></td><td><input type="text" name="pasport_division_code" size="30" value="" class="inputfield"></td>
			</tr>
			<tr>
				<td colspan="4" class="top_line"><b><%= Bean.natprsXML.getfieldTransl("h_contact_information", false) %></b></td>
			</tr>
			<tr>
				<td><%= Bean.natprsXML.getfieldTransl("phone_work", false) %> </td><td><input type="text" name="phone_work" size="30" value="" class="inputfield"></td>
				<td><%= Bean.natprsXML.getfieldTransl("phone_mobile", false) %></td><td><input type="text" name="phone_mobile" size="30" value="" class="inputfield"></td>
			</tr>
			<tr>
				<td><%= Bean.natprsXML.getfieldTransl("phone_home", false) %></td><td><input type="text" name="phone_home" size="30" value="" class="inputfield"></td>
				<td><%= Bean.natprsXML.getfieldTransl("email", false) %> </td><td><input type="text" name="email" size="30" value="" class="inputfield"></td>
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
			<tr>
				<td colspan="4" align="center">
					<%=Bean.getSubmitButtonAjax("../crm/clients/natpersonupdate.jsp") %>
					<%=Bean.getResetButton() %>
				<% if (action.equalsIgnoreCase("add")) { %>
					<%=Bean.getGoBackButton("../crm/clients/natpersons.jsp") %>
				<% } else { %>
					<%=Bean.getGoBackButton("../crm/clients/natpersonspecs.jsp?id=" + id) %>
				<% } %>
				</td>
			</tr>
	
		</table>
	</form>

		<%= Bean.getCalendarScript("date_of_birth", false) %>
		<%= Bean.getCalendarScript("pasport_date", false) %>
	
		        <%
	    	} else {
	    	    %> <%= Bean.getUnknownActionText(action) %><%
	    	}
	
	} else if (process.equalsIgnoreCase("yes"))
		{
	    String    
	    	id_nat_prs 					= Bean.getDecodeParam(parameters.get("id_nat_prs")),
	    	tax_code 					= Bean.getDecodeParam(parameters.get("tax_code")),
	    	surname 					= Bean.getDecodeParam(parameters.get("surname")), 
	    	name 						= Bean.getDecodeParam(parameters.get("name")), 
	    	patronymic 					= Bean.getDecodeParam(parameters.get("patronymic")), 
	    	date_of_birth 				= Bean.getDecodeParam(parameters.get("date_of_birth")), 
	    	sex 						= Bean.getDecodeParam(parameters.get("sex")),
	    	fact_code_country 			= Bean.getDecodeParam(parameters.get("fact_code_country")),
	    	fact_adr_zip_code 			= Bean.getDecodeParam(parameters.get("fact_adr_zip_code")),
	    	fact_adr_oblast 			= Bean.getDecodeParam(parameters.get("fact_adr_oblast")),
	    	fact_adr_district 			= Bean.getDecodeParam(parameters.get("fact_adr_district")),
	    	fact_adr_city 				= Bean.getDecodeParam(parameters.get("fact_adr_city")),
	    	fact_adr_street 			= Bean.getDecodeParam(parameters.get("fact_adr_street")),
	    	fact_adr_house 				= Bean.getDecodeParam(parameters.get("fact_adr_house")),
	    	fact_adr_case 				= Bean.getDecodeParam(parameters.get("fact_adr_case")),
	    	fact_adr_apartment 			= Bean.getDecodeParam(parameters.get("fact_adr_apartment")),
	    	reg_code_country 			= Bean.getDecodeParam(parameters.get("reg_code_country")),
	    	reg_adr_zip_code 			= Bean.getDecodeParam(parameters.get("reg_adr_zip_code")),
	    	reg_adr_oblast 				= Bean.getDecodeParam(parameters.get("reg_adr_oblast")),
	    	reg_adr_district 			= Bean.getDecodeParam(parameters.get("reg_adr_district")),
	    	reg_adr_city 				= Bean.getDecodeParam(parameters.get("reg_adr_city")),
	    	reg_adr_street 				= Bean.getDecodeParam(parameters.get("reg_adr_street")),
	    	reg_adr_house 				= Bean.getDecodeParam(parameters.get("reg_adr_house")),
	    	reg_adr_case 				= Bean.getDecodeParam(parameters.get("reg_adr_case")),
	    	reg_adr_apartment 			= Bean.getDecodeParam(parameters.get("reg_adr_apartment")),
	    	pasport_code_country 		= Bean.getDecodeParam(parameters.get("pasport_code_country")),
	    	pasport_series 				= Bean.getDecodeParam(parameters.get("pasport_series")),
	    	pasport_number 				= Bean.getDecodeParam(parameters.get("pasport_number")),
	    	pasport_division_code		= Bean.getDecodeParam(parameters.get("pasport_division_code")),
	    	pasport_date 				= Bean.getDecodeParam(parameters.get("pasport_date")),
	    	pasport_text 				= Bean.getDecodeParam(parameters.get("pasport_text")),
	    	phone_work 					= Bean.getDecodeParam(parameters.get("phone_work")),
	    	phone_home 					= Bean.getDecodeParam(parameters.get("phone_home")),
	    	phone_mobile 				= Bean.getDecodeParam(parameters.get("phone_mobile")),
	    	email 						= Bean.getDecodeParam(parameters.get("email")),
	    	id_club 					= Bean.getDecodeParam(parameters.get("id_club")),
	    	is_phone_work_valid 		= Bean.getDecodeParam(parameters.get("is_phone_work_valid")),
	    	is_phone_mobile_valid 		= Bean.getDecodeParam(parameters.get("is_phone_mobile_valid")),
	    	is_phone_home_valid 		= Bean.getDecodeParam(parameters.get("is_phone_home_valid")),
	    	is_email_valid 		        = Bean.getDecodeParam(parameters.get("is_email_valid")); 
	    
	    ArrayList<String> pParam = new ArrayList<String>();
	    
	    if (action.equalsIgnoreCase("add")) { 
	    	
			pParam.add(tax_code);
			pParam.add(surname);
			pParam.add(name);
			pParam.add(patronymic);
			pParam.add(date_of_birth);
			pParam.add(sex);
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
			pParam.add(phone_work);
			pParam.add(phone_home);
			pParam.add(phone_mobile);
			pParam.add(email);
			pParam.add(id_club);
			pParam.add(Bean.getDateFormat());
		
			%>
			<%= Bean.executeInsertFunction("PACK$NAT_PRS_UI.add_nat_prs", pParam, "../crm/clients/natpersonspecs.jsp?id=" , "../crm/clients/natpersons.jsp") %>
			<% 	
	
	     } else if (action.equalsIgnoreCase("remove")) {  
	    	
	    	pParam.add(id);
	
			%>
			<%= Bean.executeDeleteFunction("PACK$NAT_PRS_UI.drop_nat_prs", pParam, "../crm/clients/natpersons.jsp", "") %>
			<% 	
	
	     } else if (action.equalsIgnoreCase("del_photo")) { 
				
				String resultInt        = "0";
				String resultMessage    = ""; 
		    	
		    	pParam.add(id);
				
				String[] results = new String[2];
					
				results 		= Bean.executeFunction("PACK$NAT_PRS_UI.delete_nat_prs_photo", pParam, results.length);
				resultInt 		= results[0];
				resultMessage 	= results[1];
				if (Bean.C_SUCCESS_RESULT.equalsIgnoreCase(resultInt)) {%>
					<p><%=Bean.clubfundXML.getfieldTransl("titl_picture_load", false) %></p>
					<input type="file" class="photo_file" name="client_photo" size="50" value="" accept="image/*" capture="camera" onchange="load_photo()">
				<% } else { %>
					<p>error: <%=resultMessage %></p>
					<input type="file" class="photo_file" name="client_photo" size="50" value="" accept="image/*" capture="camera" onchange="load_photo()">
				<% } 
		
	     } else if (action.equalsIgnoreCase("load_photo")) {
				if (!loadError) {
					
					String resultInt        = "0";
					String resultMessage    = "";
						
					pParam.add(id);
					pParam.add(photoFileName);
					pParam.add(fullPhotoFileName);
						
					String[] results = new String[2];
						
					results 		= Bean.executeFunction("PACK$NAT_PRS_UI.add_nat_prs_photo", pParam, results.length);
					resultInt 		= results[0];
					resultMessage 	= results[1];
					if (Bean.C_SUCCESS_RESULT.equalsIgnoreCase(resultInt)) {%>
						<div onclick="del_photo()" title="<%=Bean.buttonXML.getfieldTransl("photo_delete", false) %>" class="del" id="delgimage-0"></div>
							<div title="<%=Bean.buttonXML.getfieldTransl("photo_edit", false) %>" class="edt" id="edtgimage-0">
								<input type="file" title="Изменить фото" onchange="load_photo()" capture="camera" accept="image/*" value="" size="50" name="client_photo" class="photo_file">
							</div>
							<img class="photo_image" id="image-0" height="150" src="../NatPrsPhoto?id_nat_prs=<%=id %>&noCache=<%=(new Date().getTime()) + Math.random() %>">
					<% } else { %>
						<p>error: <%=resultMessage %></p>
						<input type="file" class="photo_file" name="client_photo" size="50" value="" accept="image/*" capture="camera" onchange="load_photo()">
					<% } 
				}
				
			} else if (action.equalsIgnoreCase("edit")) { 	
			
			 	pParam.add(id_nat_prs);
				pParam.add(tax_code);
				pParam.add(surname);
				pParam.add(name);
				pParam.add(patronymic);
				pParam.add(date_of_birth);
				pParam.add(sex);
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
				pParam.add(phone_work);
				pParam.add(is_phone_work_valid);
				pParam.add(phone_home);
				pParam.add(is_phone_home_valid);
				pParam.add(phone_mobile);
				pParam.add(is_phone_mobile_valid);
				pParam.add(email);
				pParam.add(is_email_valid);
				pParam.add(Bean.getDateFormat());
	
			%>
			<%= Bean.executeUpdateFunction("PACK$NAT_PRS_UI.update_nat_prs", pParam, "../crm/clients/natpersonspecs.jsp?id=" + id_nat_prs, "") %>
			<% 	
	
	    } else { %> 
	    	<%= Bean.getUnknownActionText(action) %><% 
	    }
	} else {
	    %> <%= Bean.getUnknownProcessText(process) %> <%
	}
} else if (type.equalsIgnoreCase("account")) {
	
	String 
		id_bank_account 			= Bean.getDecodeParam(parameters.get("id_bank_account")),
		number_bank_account 		= Bean.getDecodeParam(parameters.get("number_bank_account")),
		desc_bank_account 			= Bean.getDecodeParam(parameters.get("desc_bank_account")),
		cd_currency 				= Bean.getDecodeParam(parameters.get("cd_currency")),
		cd_bank_account_type 		= Bean.getDecodeParam(parameters.get("cd_bank_account_type")),
		date_beg 					= Bean.getDecodeParam(parameters.get("date_beg")),
		id_bank		 				= Bean.getDecodeParam(parameters.get("id_bank")),
		code_country 				= Bean.getDecodeParam(parameters.get("code_country")),
		id_nat_prs 					= Bean.getDecodeParam(parameters.get("id_nat_prs")),
		id_club			 			= Bean.getDecodeParam(parameters.get("id_club"));

	if (process.equalsIgnoreCase("no")) {
		if (action.equalsIgnoreCase("add")) {
    		
			bcClubShortObject club = new bcClubShortObject(id_club);
	        
	        %>
			<script>
				var formData = new Array (
					new Array ('cd_bank_account_type', 'varchar2', 1),
					new Array ('number_bank_account', 'varchar2', 1),
					new Array ('name_bank', 'varchar2', 1),
					new Array ('cd_currency', 'varchar2', 1),
					new Array ('name_club', 'varchar2', 1),
					new Array ('date_beg', 'varchar2', 1)
				);
			</script>

		<%= Bean.getOperationTitleShort(
				"",
				Bean.accountXML.getfieldTransl("h_accounts_add", false),
				"Y",
				"Y") 
		%>
        <form action="../crm/clients/natpersonupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">
	        <input type="hidden" name="type" value="account">
	        <input type="hidden" name="action" value="add">
	        <input type="hidden" name="process" value="yes">
	        <input type="hidden" name="id" value="<%=id %>">
		<table <%=Bean.getTableDetailParam() %>>
		<tr>
			<td><%=Bean.accountXML.getfieldTransl("name_owner_bank_account", false)%></td>
			<td>
				<input type="hidden" id="id_nat_prs" name="id_nat_prs" value="<%= id %>">
				<input type="text" id="name_nat_prs" name="name_nat_prs" size="60" value="<%=Bean.getNatPrsName(id) %>" readonly="readonly" class="inputfield-ro">
			</td>
 		    <td><%= Bean.clubXML.getfieldTransl("club", true) %>
				<%= Bean.getGoToClubLink(club.getValue("ID_CLUB")) %>
			</td>
		  	<td>
				<%=Bean.getWindowFindClub("club", club.getValue("ID_CLUB"), club.getValue("SNAME_CLUB"), "25") %>
		  	</td>
		</tr>
		<tr>
			<td><%=Bean.accountXML.getfieldTransl("name_bank_account_type", true)%> </td><td><select name="cd_bank_account_type"  class="inputfield"><%=Bean.getBankAccountTypeOptions("", false)%></select></td>
			<td><%=Bean.clubXML.getfieldTransl("club_date_beg", true)%></td> <td><%=Bean.getCalendarInputField("date_beg", "", "10") %></td>
		</tr>
		<tr>
			<td><%=Bean.accountXML.getfieldTransl("number_bank_account", true)%> </td><td><input type="text" name="number_bank_account" size="60" value="" class="inputfield"></td>
			<td rowspan="3"><%=Bean.accountXML.getfieldTransl("desc_bank_account", false)%></td><td rowspan="3"><textarea name="desc_bank_account" cols="70" rows="3" class="inputfield"></textarea></td>
		</tr>
		<tr>
			<td><%=Bean.accountXML.getfieldTransl("name_bank_alt", true)%> </td>
			<td>
				<%=Bean.getWindowFindJurPrs("bank", "", "BANK", "50") %>
			</td>		
		</tr>
		<tr>
			<td><%=Bean.accountXML.getfieldTransl("name_currency", true)%> </td> <td><select name="cd_currency"  class="inputfield"><%=Bean.getCurrencyOptions("", false)%></select> </td>
		</tr>
		<tr>
			<td colspan="4">&nbsp;</td>
		</tr>
 		<tr>
			<td colspan="6" align="center">
				<%=Bean.getSubmitButtonAjax("../crm/clients/natpersonupdate.jsp") %>
				<%=Bean.getResetButton() %>
				<%=Bean.getGoBackButton("../crm/clients/natpersonspecs.jsp?id=" + id) %>
			</td>
		</tr>

	</table>
	</form>
	
	<!-- Скрипт для втавки меню вибору дати -->
	<%= Bean.getCalendarScript("date_beg", false) %>

	        <%
    	} else if (action.equalsIgnoreCase("edit")) {
    		
    		bcBankAccountObject account = new bcBankAccountObject(id_bank_account);
	        
	        %>
			<script>
				var formData = new Array (
					new Array ('cd_bank_account_type', 'varchar2', 1),
					new Array ('number_bank_account', 'varchar2', 1),
					new Array ('name_bank', 'varchar2', 1),
					new Array ('cd_currency', 'varchar2', 1),
					new Array ('date_beg', 'varchar2', 1)
				);
			</script>

		<%= Bean.getOperationTitleShort(
				"",
				Bean.accountXML.getfieldTransl("h_accounts_edit", false),
				"Y",
				"Y") 
		%>
        <form action="../crm/clients/natpersonupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">
	        <input type="hidden" name="type" value="account">
	        <input type="hidden" name="action" value="edit">
	        <input type="hidden" name="process" value="yes">
	        <input type="hidden" name="id" value="<%=account.getValue("ID_NAT_PRS") %>">
		<table <%=Bean.getTableDetailParam() %>>
		<tr>
			<td><%=Bean.accountXML.getfieldTransl("name_owner_bank_account", false)%></td>
			<td>
				<input type="hidden" id="id_nat_prs" name="id_nat_prs" value="<%= account.getValue("ID_NAT_PRS") %>">
				<input type="text" id="name_nat_prs" name="name_nat_prs" size="60" value="<%=account.getValue("FULLNAME_NAT_PRS") %>" readonly="readonly" class="inputfield-ro">
			</td>			
			<td><%=Bean.clubXML.getfieldTransl("club", false)%>
				<%= Bean.getGoToClubLink(account.getValue("ID_CLUB")) %>
			</td> 
			<td><input type="text" name="name_club" size="50" value="<%= Bean.getClubShortName(account.getValue("ID_CLUB"))%>" readonly="readonly" class="inputfield-ro"> </td>
		</tr>
		<tr>
			<td><%=Bean.accountXML.getfieldTransl("name_bank_account_type", true)%> </td><td><select name="cd_bank_account_type"  class="inputfield"><%=Bean.getBankAccountTypeOptions(account.getValue("CD_BANK_ACCOUNT_TYPE"), true)%></select></td>
			<td><%=Bean.clubXML.getfieldTransl("club_date_beg", true)%></td> <td><%=Bean.getCalendarInputField("date_beg", account.getValue("DATE_BEG_FRMT"), "10") %></tr>
		</tr>
		<tr>
			<td><%=Bean.accountXML.getfieldTransl("number_bank_account", true)%> </td><td><input type="text" name="number_bank_account" size="60" value="<%=account.getValue("NUMBER_BANK_ACCOUNT") %>" class="inputfield"></td>
			<td rowspan="3"><%=Bean.accountXML.getfieldTransl("desc_bank_account", false)%></td><td rowspan="3"><textarea name="desc_bank_account" cols="70" rows="2" class="inputfield"><%=account.getValue("DESC_BANK_ACCOUNT") %></textarea></td>
		</tr>
		<tr>
			<td><%=Bean.accountXML.getfieldTransl("name_bank_alt", true)%>
				<%= Bean.getGoToJurPrsHyperLink(account.getValue("ID_BANK")) %>
			</td>
			<td>
				<%=Bean.getWindowFindJurPrs("bank", account.getValue("ID_BANK"), "BANK", "50") %>
			</td>			
		</tr>
		<tr>
			<td><%=Bean.accountXML.getfieldTransl("name_currency", true)%> </td> <td><select name="cd_currency"  class="inputfield"><%=Bean.getCurrencyOptions(account.getValue("CD_CURRENCY"), true)%></select> </td>
		</tr>
		<%=	Bean.getIdCreationAndMoficationRecordFields(
				account.getValue("ID_BANK_ACCOUNT"),
				account.getValue(Bean.getCreationDateFieldName()),
				account.getValue("CREATED_BY"),
				account.getValue(Bean.getLastUpdateDateFieldName()),
				account.getValue("LAST_UPDATE_BY")
			) %>
		<tr>
			<td colspan="4">&nbsp;</td>
		</tr>
 		<tr>
			<td colspan="6" align="center">
				<%=Bean.getSubmitButtonAjax("../crm/clients/natpersonupdate.jsp") %>
				<%=Bean.getResetButton() %>
				<%=Bean.getGoBackButton("../crm/clients/natpersonspecs.jsp?id=" + id) %>
			</td>
		</tr>

	</table>
	</form>
	
	<!-- Скрипт для втавки меню вибору дати -->
	<%= Bean.getCalendarScript("date_beg", false) %>
		 
		<%} else {
    	    %> <%= Bean.getUnknownActionText(action) %><%
    	}
	} else if (process.equalsIgnoreCase("yes")) {
		
		ArrayList<String> pParam = new ArrayList<String>();
		
		if (action.equalsIgnoreCase("add")) { 

			pParam.add(id_bank);
			pParam.add(cd_currency);
			pParam.add(id_nat_prs);
			pParam.add(date_beg);
			pParam.add(number_bank_account);
			pParam.add(cd_bank_account_type);
			pParam.add(desc_bank_account);
			pParam.add(id_club);
			pParam.add(Bean.getDateFormat());
			
		 	%>
			<%= Bean.executeInsertFunction("PACK$NAT_PRS_UI.add_bank_account", pParam, "../crm/clients/natpersonspecs.jsp?id=" + id + "&id_bank_account=", "") %>
			<% 	
	   
		} else if (action.equalsIgnoreCase("remove")) { 
		   
			pParam.add(id_bank_account);

			%>
			<%= Bean.executeDeleteFunction("PACK$NAT_PRS_UI.delete_bank_account", pParam, "../crm/clients/natpersonspecs.jsp?id=" + id, "") %>
			<% 	
		     
		} else if (action.equalsIgnoreCase("edit")) { 
			
		 	pParam.add(id_bank_account);
			pParam.add(id_bank);
			pParam.add(cd_currency);
			pParam.add(id_nat_prs);
			pParam.add(date_beg);
			pParam.add(number_bank_account);
			pParam.add(cd_bank_account_type);
			pParam.add(desc_bank_account);
			pParam.add(Bean.getDateFormat());
			
		 	%>
			<%= Bean.executeUpdateFunction("PACK$NAT_PRS_UI.update_bank_account", pParam, "../crm/clients/natpersonspecs.jsp?id=" + id, "") %>
			<% 	
		
		} else { %>
			<%= Bean.getUnknownActionText(action) %><% 
		}
	} else { %>
		<%= Bean.getUnknownProcessText(process) %> <br><% 
	}
} else if (type.equalsIgnoreCase("role")) {
	
	String 
		id_nat_prs_role 			= Bean.getDecodeParam(parameters.get("id_nat_prs_role"));

	if (process.equalsIgnoreCase("no")) {
		if (action.equalsIgnoreCase("edit")) {
    		
    		bcNatPrsRoleObject role = new bcNatPrsRoleObject(id_nat_prs_role);
    		bcNatPrsRoleObject referral = null;
    		boolean referralExist = false;
    		if (!Bean.isEmpty(role.getValue("ID_REFERRAL_NAT_PRS_ROLE"))) {
    			referral = new bcNatPrsRoleObject(role.getValue("ID_REFERRAL_NAT_PRS_ROLE"));
    			if (referral.getResultSetRowCount()> 0) {
    				referralExist = true ;
    			}
    		}
	        %>
			<script>
				var formData = new Array (
					new Array ('name_referral_nat_prs_role', 'varchar2', 1),
					new Array ('club_date_beg', 'varchar2', 1)
				);
			</script>

		<%= Bean.getOperationTitleShort(
				"",
				Bean.natprsXML.getfieldTransl("h_role_edit", false),
				"Y",
				"Y") 
		%>
        <form action="../crm/clients/natpersonupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">
	        <input type="hidden" name="type" value="role">
	        <input type="hidden" name="action" value="edit">
	        <input type="hidden" name="process" value="yes">
	        <input type="hidden" name="id" value="<%=id %>">
	        <input type="hidden" name="id_nat_prs_role" value="<%=id_nat_prs_role %>">
	        <input type="hidden" name="LUD" value="<%=role.getValue("LUD") %>">
		<table <%=Bean.getTableDetailParam() %>>
		<tr>
			<td><%=Bean.natprsXML.getfieldTransl("fio_nat_prs", false)%></td>
			<td>
				<input type="hidden" id="id_nat_prs" name="id_nat_prs" value="<%= role.getValue("ID_NAT_PRS") %>">
				<input type="text" id="name_nat_prs" name="name_nat_prs" size="45" value="<%=role.getValue("FIO_NAT_PRS") %>" readonly="readonly" class="inputfield-ro">
			</td>			
			<td><%=Bean.clubXML.getfieldTransl("club", false)%>
				<%= Bean.getGoToClubLink(role.getValue("ID_CLUB")) %>
			</td> 
			<td><input type="text" name="name_club" size="45" value="<%= role.getValue("SNAME_CLUB")%>" readonly="readonly" class="inputfield-ro"> </td>
		</tr>
		<tr>
			<td><%= Bean.getClubCardXMLFieldTransl("cd_card1", false) %>
				<%= Bean.getGoToClubCardLink(
						role.getValue("CARD_SERIAL_NUMBER"),
						role.getValue("CARD_ID_ISSUER"),
						role.getValue("CARD_ID_PAYMENT_SYSTEM")
					) %>
			</td><td><input type="text" name="cd_card1" size="45" value="<%= role.getValue("CD_CARD1") %>" readonly="readonly" class="inputfield-ro"></td>
			<% if (referralExist) { %>
				<td><%=Bean.natprsXML.getfieldTransl("cd_referral_card1", true)%>
					<%= Bean.getGoToClubCardLink(
							referral.getValue("CARD_SERIAL_NUMBER"),
							referral.getValue("CARD_ID_ISSUER"),
							referral.getValue("CARD_ID_PAYMENT_SYSTEM")
						) %>
				</td><td>
					<%=Bean.getWindowFindNatPrsRole("referral_nat_prs_role", role.getValue("ID_REFERRAL_NAT_PRS_ROLE"), "30") %>
				</td>
			<% } else { %>
				<td><%=Bean.natprsXML.getfieldTransl("cd_referral_card1", true)%> (<%= Bean.natprsXML.getfieldTransl("referral_isnot_specified_short", true).toLowerCase() %>)</td>
				<td>
					<%=Bean.getWindowFindNatPrsRole("referral_nat_prs_role", "", "30") %>
				</td>
			<% } %>
		</tr>
		<tr>
			<td>&nbsp;</td>
			<td><%=Bean.natprsXML.getfieldTransl("title_card", false)%>:&nbsp;
				<%=Bean.getCheckBox("is_corporate_card", role.getValue("IS_CORPORATE_CARD"), Bean.natprsXML.getfieldTransl("is_corporate_card", false)) %>
				<%=Bean.getCheckBox("is_temporary_card", role.getValue("IS_TEMPORARY_CARD"), Bean.natprsXML.getfieldTransl("is_temporary_card", false)) %>
			</td>			
			<td><%=Bean.natprsXML.getfieldTransl("club_date_beg", true)%></td> <td><%=Bean.getCalendarInputField("club_date_beg", role.getValue("CLUB_DATE_BEG_DF"), "10") %></td>
		</tr>
		<tr>
			<td><%=Bean.natprsXML.getfieldTransl("title_work_place", false)%>
				<%= Bean.getGoToServicePlaceLink(role.getValue("ID_SERVICE_PLACE_WORK")) %>
			</td>
			<td>
				<%=Bean.getWindowFindServicePlace("service_place_work", role.getValue("ID_SERVICE_PLACE_WORK"), role.getValue("SNAME_SERVICE_PLACE_WORK"), "35") %>
			</td> 			
			<td><%=Bean.natprsXML.getfieldTransl("club_date_end", false)%></td> <td><%=Bean.getCalendarInputField("club_date_end", role.getValue("CLUB_DATE_END_DF"), "10") %></td>
		</tr>
		<tr>
			<td><%= Bean.contactXML.getfieldTransl("name_post", false) %></td> <td><select name="cd_post" class="inputfield" > <%= Bean.getContactPrsTypeOptions(role.getValue("CD_POST"), true) %></select></td>
			<td><%= Bean.clubXML.getfieldTransl("club_member_status", false) %></td> <td><select name="cd_club_member_status" class="inputfield" > <%= Bean.getClubMemberStatusOptions(role.getValue("CD_CLUB_MEMBER_STATUS"), true) %></select></td>
		</tr>
		<tr>
			<td>
				<%= Bean.natprsXML.getfieldTransl("phone_work", false) %>
				<% if ("N".equalsIgnoreCase(role.getValue("IS_PHONE_WORK_VALID"))) { %>&nbsp;(<blink><font color="red"><b><%= Bean.natprsXML.getfieldTransl("h_contact_invalid", false) %></b></font></blink>)<%} %>
			</td>
			<td>
				<input type="text" name="phone_work" size="30" value="<%= role.getValue("PHONE_WORK") %>" class="inputfield">
				<input type="checkbox" name="is_phone_work_valid" size="20" value="Y" style="height: inherit;padding:0;margin-top:4px;" <% if ("Y".equalsIgnoreCase(role.getValue("IS_PHONE_WORK_VALID"))) { %>checked<%} %> title="<%= Bean.natprsXML.getfieldTransl("h_contact_checked", false) %>">
			</td>
			<td>&nbsp;</td>
			<td>
				<%=Bean.getCheckBox("is_organizer", role.getValue("IS_ORGANIZER"), Bean.natprsXML.getfieldTransl("is_organizer", false)) %>
				<%=Bean.getCheckBox("is_investor", role.getValue("IS_INVESTOR"), Bean.natprsXML.getfieldTransl("is_investor", false)) %>
			</td>			
		</tr>
		<tr>
			<td>
				<%= Bean.natprsXML.getfieldTransl("email_work", false) %>
				<% if ("N".equalsIgnoreCase(role.getValue("IS_EMAIL_WORK_VALID"))) { %>&nbsp;(<blink><font color="red"><b><%= Bean.natprsXML.getfieldTransl("h_contact_invalid", false) %></b></font></blink>)<%} %>
			</td>
			<td>
				<input type="text" name="email_work" size="30" value="<%= role.getValue("EMAIL_WORK") %>" class="inputfield">
				<input type="checkbox" name="is_email_work_valid" size="20" value="Y" style="height: inherit;padding:0;margin-top:4px;" <% if ("Y".equalsIgnoreCase(role.getValue("IS_EMAIL_WORK_VALID"))) { %>checked<%} %> title="<%= Bean.natprsXML.getfieldTransl("h_contact_checked", false) %>">
			</td>
			<td colspan="2">&nbsp;</td>
		</tr>
		<tr>
			<td class="top_line_gray"><%= Bean.natprsXML.getfieldTransl("cd_nat_prs_role_state", false) %></td> <td class="top_line_gray"><input type="text" name="name_nat_prs_role_state" size="40" value="<%= role.getValue("NAME_NAT_PRS_ROLE_STATE") %>" readonly="readonly" class="inputfield-ro"></td>
			<td class="top_line_gray"><%=Bean.natprsXML.getfieldTransl("id_jur_prs_who_card_sold", false)%>
				<%= Bean.getGoToJurPrsHyperLink(role.getValue("ID_JUR_PRS_WHO_CARD_SOLD")) %>
			</td>
			<td class="top_line_gray"><input type="text" name="id_jur_prs_who_card_sold" size="40" value="<%= Bean.getJurPersonShortName(role.getValue("ID_JUR_PRS_WHO_CARD_SOLD")) %>" readonly="readonly" class="inputfield-ro"></td>			
		</tr>
		<tr>
			<td><%=Bean.natprsXML.getfieldTransl("code_country_give", false)%></td><td><input type="text" name="code_country_give" size="20" value="<%= Bean.getCountryName(role.getValue("CODE_COUNTRY")) %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%=Bean.natprsXML.getfieldTransl("name_serv_plce_where_card_sold", false)%>
				<%= Bean.getGoToServicePlaceLink(role.getValue("ID_SERV_PLACE_WHERE_CARD_SOLD")) %>
			</td>
			<td><input type="text" name="id_serv_place_where_card_sold" size="40" value="<%= Bean.getServicePlaceShortName(role.getValue("ID_SERV_PLACE_WHERE_CARD_SOLD")) %>" readonly="readonly" class="inputfield-ro"></td>			
		</tr>
		<tr>
			<td><%=Bean.natprsXML.getfieldTransl("date_card_sale", false)%></td><td><input type="text" name="date_card_sale" size="20" value="<%= role.getValue("DATE_CARD_SALE_DF") %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%=Bean.natprsXML.getfieldTransl("id_term_who_card_sold", false)%>
				<%= Bean.getGoToTerminalLink(role.getValue("ID_TERM_WHO_CARD_SOLD")) %>
			</td>
			<td><input type="text" name="id_term_who_card_sold" size="40" value="<%= role.getValue("ID_TERM_WHO_CARD_SOLD") %>" readonly="readonly" class="inputfield-ro"></td>			
		</tr>
		<tr>
			<td><%=Bean.natprsXML.getfieldTransl("total_amount_card_sale", false)%></td><td><input type="text" name="total_amount_card_sale" size="20" value="<%= role.getValue("TOTAL_AMOUNT_CARD_SALE_FRMT") %> <%=Bean.getCurrencyShortNameById(role.getValue("CD_CURRENCY_CARD_SALE")) %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%=Bean.natprsXML.getfieldTransl("id_user_who_card_sold", false)%>
				<%= Bean.getGoToSystemUserLink(role.getValue("ID_USER_WHO_CARD_SOLD")) %>
			</td>
			<td><input type="text" name="id_user_who_card_sold" size="40" value="<%= Bean.getUserNatPrsName2(role.getValue("ID_USER_WHO_CARD_SOLD")) %>" readonly="readonly" class="inputfield-ro"></td>			
		</tr>
		<tr>
			<td><%=Bean.natprsXML.getfieldTransl("id_jur_prs_card_pack", false)%>
				<%= Bean.getGoToCardPackageLink(role.getValue("ID_JUR_PRS_CARD_PACK")) %>
			</td>
			<td><input type="text" name="id_jur_prs_card_pack" size="40" value="<%= Bean.getClubJurPrsCardPackName(role.getValue("ID_JUR_PRS_CARD_PACK")) %>" readonly="readonly" class="inputfield-ro"></td>			
			<td><%=Bean.natprsXML.getfieldTransl("id_trans_card_given", false)%>
				<%= Bean.getGoToTransactionLink(role.getValue("ID_TRANS_CARD_GIVEN")) %>
			</td>
			<td><input type="text" name="id_trans_card_given" size="20" value="<%= role.getValue("ID_TRANS_CARD_GIVEN") %>" readonly="readonly" class="inputfield-ro"></td>			
		</tr>
		<tr>
			<td><%=Bean.natprsXML.getfieldTransl("membership_month_sum", false)%> </td> 
			<td>
				<input type="text" name="membership_month_sum" size="15" value="<%= role.getValue("MEMBERSHIP_MONTH_SUM_FRMT") %>" class="inputfield">
				<select name="cd_currency" class="inputfield"><%=Bean.getCurrencyShortNameOptions(role.getValue("MEMBERSHIP_CD_CURRENCY"), true)%></select> 
			</td>
			<td><%=Bean.natprsXML.getfieldTransl("id_trans_card_activation", false)%>
				<%= Bean.getGoToTransactionLink(role.getValue("ID_TRANS_CARD_ACTIVATION")) %>
			</td>
			<td><input type="text" name="id_trans_card_activation" size="20" value="<%= role.getValue("id_trans_card_activation") %>" readonly="readonly" class="inputfield-ro"></td>			
		</tr>
		<tr>
			<td><%=Bean.natprsXML.getfieldTransl("membership_last_date", false)%></td> <td><%=Bean.getCalendarInputField("membership_last_date", role.getValue("MEMBERSHIP_LAST_DATE_DF"), "10") %></td>
			<td rowspan="2"><%=Bean.natprsXML.getfieldTransl("desc_nat_prs_role", false)%></td><td rowspan="2"><textarea name="desc_nat_prs_role" cols="37" rows="2" class="inputfield"><%=role.getValue("DESC_NAT_PRS_ROLE") %></textarea></td>
		</tr>
		<tr>
			<td><%= Bean.natprsXML.getfieldTransl("is_questionnaire_checked", false) %></td> <td><select name="is_questionnaire_checked" class="inputfield" > <%= Bean.getYesNoLookupOptions(role.getValue("IS_QUESTIONNAIRE_CHECKED"), true) %></select></td>
		</tr>
		<tr>
			<td><%= Bean.natprsXML.getfieldTransl("can_use_share_account", false) %></td> <td><select name="can_use_share_account" class="inputfield" > <%= Bean.getMeaningFromLookupNameOrderByNymberValueOptions("YES_NO_UNKNOWN", role.getValue("CAN_USE_SHARE_ACCOUNT"), true) %></select></td>
		</tr>
		<%=	Bean.getIdCreationAndMoficationRecordFields(
				role.getValue("ID_NAT_PRS_ROLE"),
				role.getValue(Bean.getCreationDateFieldName()),
				role.getValue("CREATED_BY"),
				role.getValue(Bean.getLastUpdateDateFieldName()),
				role.getValue("LAST_UPDATE_BY")
			) %>
		<tr>
			<td colspan="4">&nbsp;</td>
		</tr>
 		<tr>
			<td colspan="6" align="center">
				<%=Bean.getSubmitButtonAjax("../crm/clients/natpersonupdate.jsp") %>
				<%=Bean.getResetButton() %>
				<%=Bean.getGoBackButton("../crm/clients/natpersonspecs.jsp?id=" + id) %>
			</td>
		</tr>

	</table>
	</form>
	
	<!-- Скрипт для втавки меню вибору дати -->
	<%= Bean.getCalendarScript("club_date_beg", false) %>
	<%= Bean.getCalendarScript("club_date_end", false) %>
	<%= Bean.getCalendarScript("membership_last_date", false) %>
		 
		<%} else {
    	    %> <%= Bean.getUnknownActionText(action) %><%
    	}
	} else if (process.equalsIgnoreCase("yes")) {

		String 
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
		
		ArrayList<String> pParam = new ArrayList<String>();
		
		if (action.equalsIgnoreCase("edit")) { 
			
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
			<%= Bean.executeUpdateFunction("PACK$NAT_PRS_UI.update_nat_prs_role", pParam, "../crm/clients/natpersonspecs.jsp?id=" + id, "") %>
			<% 	
		
		} else { %>
			<%= Bean.getUnknownActionText(action) %><% 
		}
	} else { %>
		<%= Bean.getUnknownProcessText(process) %> <br><% 
	}
} else {
    %> <%= Bean.getUnknownTypeText(type) %> <%
}

%>


</body>
</html>
