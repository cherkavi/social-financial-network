<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcWebPosBean"  />

<%@page import="java.util.HashMap"%>
<%@page import="java.util.ArrayList"%>
<%@page import="webpos.wpNatPrsObject"%>
<%@page import="webpos.wpNatPrsRoleObject"%>
<%@page import="org.apache.commons.fileupload.servlet.ServletFileUpload"%>
<%@page import="org.apache.commons.fileupload.FileItemFactory"%>
<%@page import="org.apache.commons.fileupload.disk.DiskFileItemFactory"%>
<%@page import="java.util.List"%>
<%@page import="org.apache.commons.fileupload.FileItem"%>
<%@page import="java.io.File"%>
<%@page import="java.util.Date"%><html>
<head>
	<%= Bean.getMetaContent() %>

	<script type="text/javascript" src="../js/jquery-1.11.3.js"></script>
    <script type="text/javascript" src="../js/jquery-ui.js"></script>
	<script type="text/javascript" src="../js/jquery.maskedinput.js"></script>
	<script type="text/javascript" src="../js/jquery.dirtyforms.min.js"></script>
	<script type="text/javascript" src="../js/mask.js"></script>
	<script type="text/javascript" language="javascript" src="../../dwr/interface/responseUtility.js"></script>
	<script type="text/javascript" language="javascript" src="../../dwr/engine.js"></script>

</head>
<body>
<%
request.setCharacterEncoding("UTF-8");

HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String pageFormName = "WEBPOS_SERVICE_CARD_ACTIVATION";

Bean.setJspPageForTabName(pageFormName);

Bean.readWebPosMenuHTML();

request.setCharacterEncoding("UTF-8");
String type			= "";
String action		= Bean.getDecodeParam(parameters.get("action")); 

String id_role		= Bean.getDecodeParam(parameters.get("id_role"));
String id_user		= Bean.getDecodeParam(parameters.get("id_user"));
String id_term	 	= Bean.getCurrentTerm();

String
	referral_in				= Bean.getDecodeParam(parameters.get("referral_in")),
	surname					= Bean.getDecodeParam(parameters.get("surname")),
	name					= Bean.getDecodeParam(parameters.get("name")),
	patronymic				= Bean.getDecodeParam(parameters.get("patronymic")),
	date_of_birth			= Bean.getDecodeParam(parameters.get("date_of_birth")),
	date_of_birth_day		= Bean.getDecodeParam(parameters.get("date_of_birth_day")),
	date_of_birth_month		= Bean.getDecodeParam(parameters.get("date_of_birth_month")),
	date_of_birth_year		= Bean.getDecodeParam(parameters.get("date_of_birth_year")),
	sex						= Bean.getDecodeParam(parameters.get("sex")),
	phone_mobile			= Bean.getDecodeParam(parameters.get("phone_mobile")),
	phone_mobile_hide		= Bean.getDecodeParam(parameters.get("phone_mobile_hide")),
	phone_stationary		= Bean.getDecodeParam(parameters.get("phone_stationary")),
	email					= Bean.getDecodeParam(parameters.get("email")),
	pasport_code_country	= Bean.getDecodeParam(parameters.get("pasport_code_country")),
	pasport_date			= Bean.getDecodeParam(parameters.get("pasport_date")),
	pasport_date_day		= Bean.getDecodeParam(parameters.get("pasport_date_day")),
	pasport_date_month		= Bean.getDecodeParam(parameters.get("pasport_date_month")),
	pasport_date_year		= Bean.getDecodeParam(parameters.get("pasport_date_year")),
	pasport_series			= Bean.getDecodeParam(parameters.get("pasport_series")),
	pasport_number			= Bean.getDecodeParam(parameters.get("pasport_number")),
	pasport_division_code	= Bean.getDecodeParam(parameters.get("pasport_division_code")),
	pasport_desc			= Bean.getDecodeParam(parameters.get("pasport_desc")),
	fact_code_country		= Bean.getDecodeParam(parameters.get("fact_code_country")),
	fact_adr_zip_code		= Bean.getDecodeParam(parameters.get("fact_adr_zip_code")),			
	fact_adr_oblast			= Bean.getDecodeParam(parameters.get("fact_adr_oblast")),
	fact_adr_district		= Bean.getDecodeParam(parameters.get("fact_adr_district")),
	fact_adr_city			= Bean.getDecodeParam(parameters.get("fact_adr_city")),
	fact_adr_street			= Bean.getDecodeParam(parameters.get("fact_adr_street")),
	fact_adr_house			= Bean.getDecodeParam(parameters.get("fact_adr_house")),
	fact_adr_case			= Bean.getDecodeParam(parameters.get("fact_adr_case")),
	fact_adr_apartment		= Bean.getDecodeParam(parameters.get("fact_adr_apartment")),
	reg_code_country		= Bean.getDecodeParam(parameters.get("reg_code_country")),
	reg_adr_zip_code		= Bean.getDecodeParam(parameters.get("reg_adr_zip_code")),
	reg_adr_oblast			= Bean.getDecodeParam(parameters.get("reg_adr_oblast")),			
	reg_adr_district		= Bean.getDecodeParam(parameters.get("reg_adr_district")),
	reg_adr_city			= Bean.getDecodeParam(parameters.get("reg_adr_city")),
	reg_adr_street			= Bean.getDecodeParam(parameters.get("reg_adr_street")),
	reg_adr_house			= Bean.getDecodeParam(parameters.get("reg_adr_house")),
	reg_adr_case			= Bean.getDecodeParam(parameters.get("reg_adr_case")),
	reg_adr_apartment		= Bean.getDecodeParam(parameters.get("reg_adr_apartment")),
	nat_prs_lud				= Bean.getDecodeParam(parameters.get("nat_prs_lud")),
	nat_prs_role_lud		= Bean.getDecodeParam(parameters.get("nat_prs_role_lud")),
	is_questionnaire_checked	= Bean.getDecodeParam(parameters.get("is_questionnaire_checked")),
	client_country			= Bean.getDecodeParam(parameters.get("client_country")),
	cd_nat_prs_role_state   = Bean.getDecodeParam(parameters.get("cd_nat_prs_role_state")),
	full_photo_file_name	= Bean.getDecodeParam(parameters.get("FULL_PHOTO_FILE_NAME"));

System.out.println("phone_mobile_hide="+phone_mobile_hide);
	
String file_name               = "";
String fieldName               = "";


String shortFileName1="";
String fullFileName1="";
String shortFileName2="";
String fullFileName2="";
String shortFileName3="";
String fullFileName3="";
String doc_dir = Bean.getDirectorySystemParamValue("UPLOADED_FILES_DIR") + "documents/uploaded/";

String photoFileName="";
String fullPhotoFileName="";

String photo_dir = Bean.getDirectorySystemParamValue("UPLOADED_FILES_DIR") + "documents/photo/";

String state = "";

if(ServletFileUpload.isMultipartContent(request)){
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
	  			if ("id_role".equalsIgnoreCase(file.getFieldName())) id_role = Bean.decodeUtf(file.getString());
	  			if ("id_user".equalsIgnoreCase(file.getFieldName())) id_user = Bean.decodeUtf(file.getString());
	  			if ("referral_in".equalsIgnoreCase(file.getFieldName())) referral_in = Bean.decodeUtf(file.getString());
	  			if ("surname".equalsIgnoreCase(file.getFieldName())) surname = Bean.decodeUtf(file.getString());
	  			if ("name".equalsIgnoreCase(file.getFieldName())) name = Bean.decodeUtf(file.getString());
	  			if ("patronymic".equalsIgnoreCase(file.getFieldName())) patronymic = Bean.decodeUtf(file.getString());
	  			if ("date_of_birth".equalsIgnoreCase(file.getFieldName())) date_of_birth = Bean.decodeUtf(file.getString());
	  			if ("date_of_birth_day".equalsIgnoreCase(file.getFieldName())) date_of_birth_day = Bean.decodeUtf(file.getString());
	  			if ("date_of_birth_month".equalsIgnoreCase(file.getFieldName())) date_of_birth_month = Bean.decodeUtf(file.getString());
	  			if ("date_of_birth_year".equalsIgnoreCase(file.getFieldName())) date_of_birth_year = Bean.decodeUtf(file.getString());
	  			if ("sex".equalsIgnoreCase(file.getFieldName())) sex = Bean.decodeUtf(file.getString());
	  			if ("phone_mobile".equalsIgnoreCase(file.getFieldName())) phone_mobile = Bean.decodeUtf(file.getString());
	  			if ("phone_mobile_hide".equalsIgnoreCase(file.getFieldName())) phone_mobile_hide = Bean.decodeUtf(file.getString());
	  			if ("phone_stationary".equalsIgnoreCase(file.getFieldName())) phone_stationary = Bean.decodeUtf(file.getString());
	  			if ("email".equalsIgnoreCase(file.getFieldName())) email = Bean.decodeUtf(file.getString());
	  			if ("pasport_code_country".equalsIgnoreCase(file.getFieldName())) pasport_code_country = Bean.decodeUtf(file.getString());
	  			if ("pasport_date".equalsIgnoreCase(file.getFieldName())) pasport_date = Bean.decodeUtf(file.getString());
	  			if ("pasport_date_day".equalsIgnoreCase(file.getFieldName())) pasport_date_day = Bean.decodeUtf(file.getString());
	  			if ("pasport_date_month".equalsIgnoreCase(file.getFieldName())) pasport_date_month = Bean.decodeUtf(file.getString());
	  			if ("pasport_date_year".equalsIgnoreCase(file.getFieldName())) pasport_date_year = Bean.decodeUtf(file.getString());
	  			if ("pasport_series".equalsIgnoreCase(file.getFieldName())) pasport_series = Bean.decodeUtf(file.getString());
	  			if ("pasport_number".equalsIgnoreCase(file.getFieldName())) pasport_number = Bean.decodeUtf(file.getString());
	  			if ("pasport_division_code".equalsIgnoreCase(file.getFieldName())) pasport_division_code = Bean.decodeUtf(file.getString());
	  			if ("pasport_desc".equalsIgnoreCase(file.getFieldName())) pasport_desc = Bean.decodeUtf(file.getString());
	  			if ("fact_code_country".equalsIgnoreCase(file.getFieldName())) fact_code_country = Bean.decodeUtf(file.getString());
	  			if ("fact_adr_zip_code".equalsIgnoreCase(file.getFieldName())) fact_adr_zip_code = Bean.decodeUtf(file.getString());
	  			if ("fact_adr_oblast".equalsIgnoreCase(file.getFieldName())) fact_adr_oblast = Bean.decodeUtf(file.getString());
	  			if ("fact_adr_district".equalsIgnoreCase(file.getFieldName())) fact_adr_district = Bean.decodeUtf(file.getString());
	  			if ("fact_adr_city".equalsIgnoreCase(file.getFieldName())) fact_adr_city = Bean.decodeUtf(file.getString());
	  			if ("fact_adr_street".equalsIgnoreCase(file.getFieldName())) fact_adr_street = Bean.decodeUtf(file.getString());
	  			if ("fact_adr_house".equalsIgnoreCase(file.getFieldName())) fact_adr_house = Bean.decodeUtf(file.getString());
	  			if ("fact_adr_case".equalsIgnoreCase(file.getFieldName())) fact_adr_case = Bean.decodeUtf(file.getString());
	  			if ("fact_adr_apartment".equalsIgnoreCase(file.getFieldName())) fact_adr_apartment = Bean.decodeUtf(file.getString());
	  			if ("reg_code_country".equalsIgnoreCase(file.getFieldName())) reg_code_country = Bean.decodeUtf(file.getString());
	  			if ("reg_adr_zip_code".equalsIgnoreCase(file.getFieldName())) reg_adr_zip_code = Bean.decodeUtf(file.getString());
	  			if ("reg_adr_oblast".equalsIgnoreCase(file.getFieldName())) reg_adr_oblast = Bean.decodeUtf(file.getString());
	  			if ("reg_adr_district".equalsIgnoreCase(file.getFieldName())) reg_adr_district = Bean.decodeUtf(file.getString());
	  			if ("reg_adr_city".equalsIgnoreCase(file.getFieldName())) reg_adr_city = Bean.decodeUtf(file.getString());
	  			if ("reg_adr_street".equalsIgnoreCase(file.getFieldName())) reg_adr_street = Bean.decodeUtf(file.getString());
	  			if ("reg_adr_house".equalsIgnoreCase(file.getFieldName())) reg_adr_house = Bean.decodeUtf(file.getString());
	  			if ("reg_adr_case".equalsIgnoreCase(file.getFieldName())) reg_adr_case = Bean.decodeUtf(file.getString());
	  			if ("reg_adr_apartment".equalsIgnoreCase(file.getFieldName())) reg_adr_apartment = Bean.decodeUtf(file.getString());
	  			if ("nat_prs_lud".equalsIgnoreCase(file.getFieldName())) nat_prs_lud = Bean.decodeUtf(file.getString());
	  			if ("nat_prs_role_lud".equalsIgnoreCase(file.getFieldName())) nat_prs_role_lud = Bean.decodeUtf(file.getString());
	  			if ("is_questionnaire_checked".equalsIgnoreCase(file.getFieldName())) is_questionnaire_checked = Bean.decodeUtf(file.getString());
	  			if ("client_country".equalsIgnoreCase(file.getFieldName())) client_country = Bean.decodeUtf(file.getString());
	  			if ("cd_nat_prs_role_state".equalsIgnoreCase(file.getFieldName())) cd_nat_prs_role_state = Bean.decodeUtf(file.getString());
	  			
	  			if ("full_photo_file_name".equalsIgnoreCase(file.getFieldName())) full_photo_file_name = Bean.decodeUtf(file.getString());
	  		} else {
	  			fieldName = file.getFieldName();
	  			file_name = file.getName();
	  			String sFileName = "";
	  			String fFileName = "";
				
	   			if (!Bean.isEmpty(file_name)) {
					if (file_name.contains("\\")) {
						sFileName=file_name.substring(file_name.lastIndexOf("\\")+1,file_name.length());
					} else if (file_name.contains("/")) {
						sFileName=file_name.substring(file_name.lastIndexOf("/")+1,file_name.length());
					} else {
						sFileName = file_name;
					}
					fFileName = doc_dir + Bean.getDocumentsPrefix() + "_" + sFileName;
		   			System.out.println("sFileName="+sFileName+", fFileName="+fFileName);
		   			
	   			    File filePath = new File(doc_dir);
	
	                if (!filePath.exists()) {
	                    filePath.mkdirs();
	                }
	
	                uploadedFile = new File(fFileName);
	                file.write(uploadedFile);
	                
	                if ("file1".equalsIgnoreCase(fieldName)) {
	                	shortFileName1 	= sFileName;
	                	fullFileName1 	= fFileName;
	                } else if ("file2".equalsIgnoreCase(fieldName)) {
	                	shortFileName2 	= sFileName;
	                	fullFileName2 	= fFileName;
	                } else if ("file3".equalsIgnoreCase(fieldName)) {
	                	shortFileName3 	= sFileName;
	                	fullFileName3 	= fFileName;
	                } else if ("photo".equalsIgnoreCase(fieldName)) {
	                	photoFileName 		= sFileName;
	                	fullPhotoFileName 	= fFileName;
	                }
	   			}
	  		}
   		}

   		System.out.println("pasport_code_country(1)="+pasport_code_country);
  	}catch(Exception ex){
   		System.err.println("Exception (fieldName="+fieldName+", file_name="+file_name+"):"+ex.getMessage());
   		shortFileName1 = "";
   		fullFileName1 = "";
   		shortFileName2 = "";
   		fullFileName2 = "";
   		shortFileName3 = "";
   		fullFileName3 = "";
   		photoFileName = "";
   		fullPhotoFileName = "";
   		//action = "error";
  	}
	
}
state = state + ", action=" + action;
action 	= Bean.isEmpty(action)?"":action;

if (!Bean.hasMenuPermission(pageFormName, Bean.C_WRITE_MENU_PERMISSION, Bean.loginTerm)) { %>
<%=Bean.getErrorPermissionMessage(pageFormName, Bean.loginTerm) %>
<% type = "error_term"; %>
<% } %>
<%
if (type.equalsIgnoreCase("error_term")) {
} else {

if (action.equalsIgnoreCase("edit") || action.equalsIgnoreCase("update") || action.equalsIgnoreCase("del_doc")) {
	state = state + ", 1";
 	//wpOnlineOperationObject oper = new wpOnlineOperationObject(id_telgr);
			
	wpNatPrsRoleObject role = new wpNatPrsRoleObject(id_role);
			
	wpNatPrsObject nat_prs = new wpNatPrsObject(role.getValue("ID_NAT_PRS"));
	System.out.println("surname="+nat_prs.getValue("SURNAME"));
	
	boolean new_question = "GIVEN".equalsIgnoreCase(role.getValue("CD_NAT_PRS_ROLE_STATE"))?true:false; 
			
	boolean is_confirmed = "Y".equalsIgnoreCase(role.getValue("IS_QUESTIONNAIRE_CHECKED"))?true:false; 
	
	nat_prs_lud					= nat_prs.getValue("LUD");
	nat_prs_role_lud			= role.getValue("LUD");
	client_country				= role.getValue("CODE_COUNTRY");
	cd_nat_prs_role_state   	= role.getValue("CD_NAT_PRS_ROLE_STATE");
	//is_questionnaire_checked	= role.getValue("IS_QUESTIONNAIRE_CHECKED");
	String roleState 			= Bean.getNatPrsRoleStateName(cd_nat_prs_role_state, role.getValue("IS_QUESTIONNAIRE_CHECKED"));
	String cardStatus 			= Bean.getCardStatusName(role.getValue("ID_CARD_STATUS"));
	
	String resultUpdateInt 				= "0";
	String resultUpdateMessage 			= "";
	String resultFullInt                = "0";
	String resultFullMessage            = "";
			
	if (action.equalsIgnoreCase("update")) {
		state = state + ", 2";
		ArrayList<String> pParam = new ArrayList<String>();
		
		pParam.add(id_term);
		pParam.add(id_role);
		pParam.add(Bean.isEmpty(referral_in)?"":referral_in.replace(" ", ""));
			
		pParam.add(surname);
		pParam.add(name);
		pParam.add(patronymic);
		pParam.add(date_of_birth_day);
		pParam.add(date_of_birth_month);
		pParam.add(date_of_birth_year);
		pParam.add(sex);
		pParam.add(phone_mobile);
		pParam.add(phone_stationary);
		pParam.add(email);
		pParam.add(pasport_code_country);
		pParam.add(pasport_date_day);
		pParam.add(pasport_date_month);
		pParam.add(pasport_date_year);
		pParam.add(pasport_series);
		pParam.add(pasport_number);
		pParam.add(pasport_division_code);
		pParam.add(pasport_desc);
	
		pParam.add(fact_code_country);
		pParam.add(fact_adr_zip_code);
		pParam.add(fact_adr_oblast);
		pParam.add(fact_adr_district);
		pParam.add(fact_adr_city);
		pParam.add(fact_adr_street);
		pParam.add(fact_adr_house);
		pParam.add(fact_adr_case);
		pParam.add(fact_adr_apartment);
		pParam.add(reg_code_country);
		pParam.add(reg_adr_zip_code);
		pParam.add(reg_adr_oblast);
		pParam.add(reg_adr_district);
		pParam.add(reg_adr_city);
		pParam.add(reg_adr_street);
		pParam.add(reg_adr_house);
		pParam.add(reg_adr_case);
		pParam.add(reg_adr_apartment);
		pParam.add(is_questionnaire_checked);
		pParam.add(full_photo_file_name);
		pParam.add(nat_prs_lud);
		pParam.add(nat_prs_role_lud);
		pParam.add(Bean.getDateFormat());
		
		String[] results = new String[2];
		
		results 				= Bean.executeFunction("PACK$WEBPOS_UI.update_nat_prs", pParam, results.length);
		resultUpdateInt 		= results[0];
		resultUpdateMessage 	= results[1];
		if (!Bean.C_SUCCESS_RESULT.equalsIgnoreCase(resultUpdateInt)) {
			resultFullInt = resultUpdateInt;
			resultFullMessage = resultFullMessage
				+ ((!Bean.isEmpty(resultFullMessage))?", ":"")
				+ resultUpdateMessage;
		} else if (Bean.C_SUCCESS_RESULT.equalsIgnoreCase(resultUpdateInt)) {
			state = state + ", 3";
			
			role = new wpNatPrsRoleObject(id_role);
					
			nat_prs = new wpNatPrsObject(role.getValue("ID_NAT_PRS"));
			
			new_question = "GIVEN".equalsIgnoreCase(role.getValue("CD_NAT_PRS_ROLE_STATE"))?true:false; 
					
			is_confirmed = "Y".equalsIgnoreCase(role.getValue("IS_QUESTIONNAIRE_CHECKED"))?true:false; 
			
			nat_prs_lud					= nat_prs.getValue("LUD");
			nat_prs_role_lud			= role.getValue("LUD");
			client_country				= role.getValue("CODE_COUNTRY");
			cd_nat_prs_role_state   	= role.getValue("CD_NAT_PRS_ROLE_STATE");
			is_questionnaire_checked	= role.getValue("IS_QUESTIONNAIRE_CHECKED");
			roleState 					= Bean.getNatPrsRoleStateName(cd_nat_prs_role_state, is_questionnaire_checked);
			cardStatus 					= Bean.getCardStatusName(role.getValue("ID_CARD_STATUS"));
		}
		
		if (!Bean.isEmpty(shortFileName1)) {
			ArrayList<String> pParamFile = new ArrayList<String>();
					
			pParamFile.add(id_role);
			pParamFile.add(shortFileName1);
			pParamFile.add(fullFileName1);
			
			String[] resultsFile = new String[2];
			
			resultsFile 			= Bean.executeFunction("PACK$WEBPOS_UI.add_nat_prs_role_file", pParamFile, resultsFile.length);
			String resultInt 		= resultsFile[0];
			String resultMessage 	= resultsFile[1];
			if (!Bean.C_SUCCESS_RESULT.equalsIgnoreCase(resultInt)) {
				resultFullInt = resultInt;
				resultFullMessage = resultFullMessage
					+ ((!Bean.isEmpty(resultFullMessage))?", ":"")
					+ resultMessage;
			}
		}
		
		if (!Bean.isEmpty(shortFileName2)) {
			ArrayList<String> pParamFile = new ArrayList<String>();
					
			pParamFile.add(id_role);
			pParamFile.add(shortFileName2);
			pParamFile.add(fullFileName2);
			
			String[] resultsFile = new String[2];
			
			resultsFile 			= Bean.executeFunction("PACK$WEBPOS_UI.add_nat_prs_role_file", pParamFile, resultsFile.length);
			String resultInt 		= resultsFile[0];
			String resultMessage 	= resultsFile[1];
			if (!Bean.C_SUCCESS_RESULT.equalsIgnoreCase(resultInt)) {
				resultFullInt = resultInt;
				resultFullMessage = resultFullMessage
					+ ((!Bean.isEmpty(resultFullMessage))?", ":"")
					+ resultMessage;
			}
		}
		
		if (!Bean.isEmpty(shortFileName3)) {
			ArrayList<String> pParamFile = new ArrayList<String>();
					
			pParamFile.add(id_role);
			pParamFile.add(shortFileName3);
			pParamFile.add(fullFileName3);
			
			String[] resultsFile = new String[2];
			
			resultsFile 			= Bean.executeFunction("PACK$WEBPOS_UI.add_nat_prs_role_file", pParamFile, resultsFile.length);
			String resultInt 		= resultsFile[0];
			String resultMessage 	= resultsFile[1];
			if (!Bean.C_SUCCESS_RESULT.equalsIgnoreCase(resultInt)) {
				resultFullInt = resultInt;
				resultFullMessage = resultFullMessage
					+ ((!Bean.isEmpty(resultFullMessage))?", ":"")
					+ resultMessage;
			}
		}
		
		if (!Bean.isEmpty(photoFileName)) {
			ArrayList<String> pParamFile = new ArrayList<String>();
					
			pParamFile.add(id_role);
			pParamFile.add(photoFileName);
			pParamFile.add(fullPhotoFileName);
			
			String[] resultsFile = new String[2];
			
			resultsFile 			= Bean.executeFunction("PACK$WEBPOS_UI.add_nat_prs_photo", pParamFile, resultsFile.length);
			String resultInt 		= resultsFile[0];
			String resultMessage 	= resultsFile[1];
			if (!Bean.C_SUCCESS_RESULT.equalsIgnoreCase(resultInt)) {
				resultFullInt = resultInt;
				resultFullMessage = resultFullMessage
					+ ((!Bean.isEmpty(resultFullMessage))?", ":"")
					+ resultMessage;
			}
		}
	}
	if (action.equalsIgnoreCase("del_doc")) {
		state = state + ", 4";
		String id_doc				= Bean.getDecodeParam(parameters.get("id_doc"));
		
		ArrayList<String> pParamDoc = new ArrayList<String>();
		
		pParamDoc.add(id_doc);
		
		String[] resultsDoc = new String[2];
		
		resultsDoc 				= Bean.executeFunction("PACK$WEBPOS_UI.delete_nat_prs_role_file", pParamDoc, resultsDoc.length);
		String resultInt 		= resultsDoc[0];
		String resultMessage 	= resultsDoc[1];
		if (!Bean.C_SUCCESS_RESULT.equalsIgnoreCase(resultInt)) {
			resultFullInt = resultInt;
			resultFullMessage = resultFullMessage
				+ ((!Bean.isEmpty(resultFullMessage))?", ":"")
				+ resultMessage;
		}
	}
	
	String cashierCard = Bean.loginUserNatPrsRole.getValue("CD_CARD1");
	
	if (action.equalsIgnoreCase("edit") || action.equalsIgnoreCase("del_doc") || (action.equalsIgnoreCase("update") && Bean.C_SUCCESS_RESULT.equalsIgnoreCase(resultUpdateInt))) {
		state = state + ", 5";

		/*nat_prs = new wpNatPrsObject(oper.getValue("ID_NAT_PRS"));
		
		role = new wpNatPrsRoleObject(oper.getValue("ID_NAT_PRS_ROLE"));
				
		new_question = "GIVEN".equalsIgnoreCase(role.getValue("CD_NAT_PRS_ROLE_STATE"))?true:false; 
				
		is_confirmed = "Y".equalsIgnoreCase(role.getValue("IS_QUESTIONNAIRE_CHECKED"))?true:false; */
		
		if (!(role.getValue("ID_REFERRAL_NAT_PRS_ROLE") == null || "".equalsIgnoreCase(role.getValue("ID_REFERRAL_NAT_PRS_ROLE")))) {
			wpNatPrsRoleObject ref_role = new wpNatPrsRoleObject(role.getValue("ID_REFERRAL_NAT_PRS_ROLE"));
			referral_in = ref_role.getValue("CD_CARD1");
		} else {
			referral_in			= "";
		}
		surname					= nat_prs.getValue("SURNAME");
		name					= nat_prs.getValue("NAME");
		patronymic				= nat_prs.getValue("PATRONYMIC");
		date_of_birth			= nat_prs.getValue("DATE_OF_BIRTH_DF");
		date_of_birth_day		= nat_prs.getValue("DATE_OF_BIRTH_DAY");
		date_of_birth_month		= nat_prs.getValue("DATE_OF_BIRTH_MONTH");
		date_of_birth_year		= nat_prs.getValue("DATE_OF_BIRTH_YEAR");
		sex						= nat_prs.getValue("SEX");
		phone_mobile			= role.getValue("PHONE_MOBILE");
		phone_mobile_hide		= role.getValue("PHONE_MOBILE_HIDE");
		phone_stationary		= nat_prs.getValue("PHONE_HOME");
		email					= nat_prs.getValue("EMAIL");
		pasport_code_country	= nat_prs.getValue("PASPORT_CODE_COUNTRY");
		pasport_date			= nat_prs.getValue("PASPORT_DATE_DF");
		pasport_date_day		= nat_prs.getValue("PASPORT_DATE_DAY");
		pasport_date_month		= nat_prs.getValue("PASPORT_DATE_MONTH");
		pasport_date_year		= nat_prs.getValue("PASPORT_DATE_YEAR");
		pasport_series			= nat_prs.getValue("PASPORT_SERIES");
		pasport_number			= nat_prs.getValue("PASPORT_NUMBER");
		pasport_division_code	= nat_prs.getValue("PASPORT_DIVISION_CODE");
		pasport_desc			= nat_prs.getValue("PASPORT_TEXT");
		fact_code_country		= nat_prs.getValue("FACT_CODE_COUNTRY");
		fact_adr_zip_code		= nat_prs.getValue("FACT_ADR_ZIP_CODE");		
		fact_adr_oblast			= nat_prs.getValue("FACT_ADR_ID_OBLAST");
		fact_adr_district		= nat_prs.getValue("FACT_ADR_DISTRICT");
		fact_adr_city			= nat_prs.getValue("FACT_ADR_CITY");
		fact_adr_street			= nat_prs.getValue("FACT_ADR_STREET");
		fact_adr_house			= nat_prs.getValue("FACT_ADR_HOUSE");
		fact_adr_case			= nat_prs.getValue("FACT_ADR_CASE");
		fact_adr_apartment		= nat_prs.getValue("FACT_ADR_APARTMENT");
		reg_code_country		= nat_prs.getValue("REG_CODE_COUNTRY");
		reg_adr_zip_code		= nat_prs.getValue("REG_ADR_ZIP_CODE");
		reg_adr_oblast			= nat_prs.getValue("REG_ADR_ID_OBLAST");		
		reg_adr_district		= nat_prs.getValue("REG_ADR_DISTRICT");
		reg_adr_city			= nat_prs.getValue("REG_ADR_CITY");
		reg_adr_street			= nat_prs.getValue("REG_ADR_STREET");
		reg_adr_house			= nat_prs.getValue("REG_ADR_HOUSE");
		reg_adr_case			= nat_prs.getValue("REG_ADR_CASE");
		reg_adr_apartment		= nat_prs.getValue("REG_ADR_APARTMENT");
		is_questionnaire_checked	= role.getValue("IS_QUESTIONNAIRE_CHECKED");
		full_photo_file_name		= nat_prs.getValue("full_photo_file_name");
		
		state = state + ", 6";
		System.out.println("phone_mobile_hide="+phone_mobile_hide);
		
		
	}
	System.out.println("state="+state);
	%>
		<% if (!is_confirmed) { %>
			<script>
		function myValidateForm(){
			<% if (new_question) { %>
			var formParam = new Array (
	            new Array ('cd_card1', 'card', 1),
				new Array ('referral_in', 'varchar2', 1)		,
				new Array ('surname', 'varchar2', 1),
				new Array ('name', 'varchar2', 1),
				new Array ('date_of_birth_day', 'varchar2', 1),
				new Array ('date_of_birth_month', 'varchar2', 1),
				new Array ('date_of_birth_year', 'varchar2', 1),
				new Array ('sex', 'varchar2', 1),
				new Array ('phone_mobile', 'varchar2', 1)
	        );
			<% } else { %>
			var formParam = new Array (
	            new Array ('cd_card1', 'card', 1),
				new Array ('date_of_birth_day', 'varchar2', 1),
				new Array ('date_of_birth_month', 'varchar2', 1),
				new Array ('date_of_birth_year', 'varchar2', 1),
	            new Array ('sex', 'varchar2', 1)
	        );
			<% }%>
			return validateFormForID(formParam, 'updateForm3');
		}

		function checkPassportCountry(){
			division = document.getElementById('pasport_division_code');
			if(!(division)){
				return;
			}
			country = document.getElementById('pasport_code_country');
			if (country!=null && country.value != '804') {
				division.className = 'inputfield';
				division.readOnly = 0;
			} else {
				division.value = '';
				division.className = 'inputfield-ro';
				division.readOnly = 1;
			}
		}
		checkPassportCountry();

		</script>

	<script type="text/javascript">
		<%=Bean.getJQueryAutocompleteOblastValues()%>
	
		function getOblast(field, country){
			jQuery(document).ready(function(){
				jQuery("#"+field+"_name").selectOblast(field, country);
			});
		}
		getOblast("reg_adr_oblast", "<%=reg_code_country%>");
		getOblast("fact_adr_oblast", "<%=fact_code_country%>");
		</script>

	<% if (action.equalsIgnoreCase("edit")) {%>
		<h1><%=Bean.webposXML.getfieldTransl("title_questionnaire", false) %><%=Bean.getHelpButton("questionnaire", "div_action_big") %></h1>
	<% } else if (action.equalsIgnoreCase("update") || action.equalsIgnoreCase("del_doc")) {%>
		<%if (Bean.C_SUCCESS_RESULT.equalsIgnoreCase(resultFullInt)) { %>
			<h1 class="confirm"><%=Bean.webposXML.getfieldTransl("title_questionnaire", false) %>: <%=Bean.webposXML.getfieldTransl("operation_success", false) %><%=Bean.getHelpButton("questionnaire", "div_action_big") %></h1>
			<% } else { %>
			<h1 class="error"><%=Bean.webposXML.getfieldTransl("title_questionnaire", false) %>: <%=Bean.webposXML.getfieldTransl("operation_error", false) %>&nbsp;<%=Bean.getHelpButton("questionnaire", "div_action_big") %></h1>
			<% } %>
	<%}	 %>
		
		<%=Bean.getCardActivationTabSheets("2") %>

			<script type="text/javascript">
				function mask_form() {
					<% if (new_question) { %>
					phone_mask_empty("phone_mobile","<%=client_country%>");
					<% } %>
				}
				mask_form();
				function onAjaxDone(){
					mask_form();
				}
				function setReferralCashierCard(){
					<% if (!Bean.isEmpty(cashierCard)) { %>
					referral_field = document.getElementById('referral_in');
					referral_field.value = '<%=cashierCard%>';
					card_mask2("referral_in");
					<% } %>
				}
				function del_photo() {
					var msg='<%=Bean.webposXML.getfieldTransl("titl_client_photo_delete", false) %>?';
					var res=window.confirm(msg);
					if (res) {
						post_form('service/load_photo.jsp','delPhoto','div_photo');
					}
				}
				function load_photo() {
					post_form('service/load_photo.jsp','updateForm3','div_photo');
				}
			</script>

		<form name="updateForm3" id="updateForm3" accept-charset="UTF-8" method="POST" enctype="multipart/form-data">
	        <input type="hidden" name="action" value="update">
			<input type="hidden" name="id_role" value="<%=id_role %>">
			<input type="hidden" name="id_nat_prs" value="<%=role.getValue("ID_NAT_PRS") %>">
			<input type="hidden" name="id_user" value="<%=id_user %>">
			<input type="hidden" name="nat_prs_lud" value="<%=nat_prs_lud %>">
			<input type="hidden" name="nat_prs_role_lud" value="<%=nat_prs_role_lud %>">
			<input type="hidden" name="cd_card1" value="<%=role.getValue("cd_card1") %>">
			<table class="action_table" id="question_data">
				<tr><td colspan="2"><b><%= Bean.webposXML.getfieldTransl("title_sold_card_information", false) %></b> <span id="event_text"></span></tr>
				<% if ("ADMINISTRATOR".equalsIgnoreCase(Bean.getLoginUserAccessType())) { %>
	  			<tr><td <%=Bean.getDetailTableFirstColumnWidth() %>><%=Bean.webposXML.getfieldTransl("id_user_who_card_sold", false) %></td><td><input type="text" name="id_user" id="id_user" size="40" value="<%=Bean.getUserName(role.getValue("ID_USER_WHO_CARD_SOLD")) %>" readonly class="inputfield-ro"></td></tr>
				<tr><td><%= Bean.webposXML.getfieldTransl("id_term_who_card_sold", false) %></td><td><input type="text" name="id_term" id="id_term" size="40" value="<%=role.getValue("ID_TERM_WHO_CARD_SOLD") %>" readonly class="inputfield-ro"></td></tr>
				<% } %>
	  			<tr><td <%=Bean.getDetailTableFirstColumnWidth() %>><%=Bean.webposXML.getfieldTransl("cd_card1", false) %>&nbsp;(<span id="quest_refresh" style="color:red; cursor:pointer;" onclick="ajaxpage('action/new_client_questionnaire.jsp?id_role=<%=id_role %>&action=edit', 'div_action_big')"><%= Bean.commonXML.getfieldTransl("title_refresh", false).toLowerCase() %></span>)</td><td><input type="text" name="cd_card1_hide" id="cd_card1_hide" size="20" value="<%=role.getValue("CD_CARD1_HIDE") %>" readonly class="inputfield_finish_green"></td></tr>
				<tr><td><%= Bean.webposXML.getfieldTransl("card_status", false) %></td><td><input type="text" name="card_status" id="card_status" size="20" value="<%=cardStatus %>" readonly class="inputfield_finish_green"></td></tr>
				<tr><td><%= Bean.webposXML.getfieldTransl("nat_prs_role_state", false) %></td><td><input type="text" name="nat_prs_role_state" id="nat_prs_role_state" size="20" value="<%=roleState %>" readonly class="inputfield_finish_red"></td></tr>
			  	<tr><td><%= Bean.webposXML.getfieldTransl("client_country", false) %></td><td><input type="text" name="client_country_name" id="client_country_name" size="20" value="<%=Bean.getCountryName(client_country) %>" readonly class="inputfield_finish_blue"></td></tr>
			  	<tr><td><%=Bean.webposXML.getfieldTransl("date_card_sale", false) %></td><td><input type="text" name="date_card_sale" id="date_card_sale" size="10" value="<%=role.getValue("DATE_CARD_SALE_DF") %>" readonly class="inputfield_finish_blue"></td></tr>
				<tr><td><%=Bean.webposXML.getfieldTransl("total_amount_card_sale", false) %></td><td><input type="text" name="total_amount_card_sale" id="total_amount_card_sale" size="20" value="<%=role.getValue("TOTAL_AMOUNT_CARD_SALE_FRMT") %> <%= Bean.getWPCurrencyShortName(role.getValue("CD_CURRENCY_CARD_SALE"))%>" readonly class="inputfield_finish_red"></td></tr>
				<% if (new_question) { %>
				<%	String setCashierCardTags = "";
					if (!Bean.isEmpty(cashierCard)) {
						setCashierCardTags = "&nbsp;<span class=\"go_to\" onclick=\"setReferralCashierCard()\">Вставить карту кассира</span>";
					}
				%>
			  	<tr><td><%=Bean.webposXML.getfieldTransl("referral_in", true) %></td><td><input type="text" name="referral_in" id="referral_in" size="40" value="<%=referral_in %>" class="inputfield"><br><%=setCashierCardTags %><br><span class="warning_header"><%=Bean.webposXML.getfieldTransl("client_not_modified_again_warning", false) %></span></td></tr>
				<% } else { %>
			  	<tr>
					<td><%=Bean.webposXML.getfieldTransl("referral_in", false) %></td>
					<td>
						<input type="hidden" name="referral_in" value="<%=referral_in %>">
						<input type="text" name="referral_in_txt" id="referral_in_txt" size="20" value="<%=Bean.hideCdCard1(referral_in) %>" readonly class="inputfield_finish_green">
					</td>
				</tr>
				<% } %>

				<tr><td colspan="2" class="top_line_gray"><b><%= Bean.webposXML.getfieldTransl("client_title", false) %></b></tr>
				<% if (new_question || (surname == null || "".equalsIgnoreCase(surname))) { %>
	  			<tr><td><%=Bean.webposXML.getfieldTransl("client_surname", true) %></td><td><input type="text" name="surname" id="surname" size="40" value="<%=surname %>" class="inputfield"><br><span class="warning_header"><%=Bean.webposXML.getfieldTransl("client_not_modified_again_warning", false) %></span></td></tr>
				<% } else { %>
	  			<tr><td><%=Bean.webposXML.getfieldTransl("client_surname", false) %></td><td><input type="text" name="surname" id="surname" size="40" value="<%=surname %>" readonly class="inputfield-ro"></td></tr>
				<% } %>
				<% if (new_question || (name == null || "".equalsIgnoreCase(name))) { %>
	  			<tr><td><%=Bean.webposXML.getfieldTransl("client_name", true) %></td><td><input type="text" name="name" size="40" value="<%=name %>" class="inputfield"><br><span class="warning_header"><%=Bean.webposXML.getfieldTransl("client_not_modified_again_warning", false) %></span></td></tr>
				<% } else { %>
	  			<tr><td><%=Bean.webposXML.getfieldTransl("client_name", false) %></td><td><input type="text" name="name" size="40" value="<%=name %>" readonly class="inputfield-ro"></td></tr>
				<% } %>
	  			<tr><td><%=Bean.webposXML.getfieldTransl("client_patronymic", false) %></td><td><input type="text" name="patronymic" size="40" value="<%=patronymic %>" class="inputfield"></td></tr>
      			<tr>
					<td><%=Bean.webposXML.getfieldTransl("client_date_of_birth", true) %></td>
					<td>
						<select name="date_of_birth_day" id="date_of_birth_day" class="inputfield"><%= Bean.getMeaningFromLookupNameOrderByNymberValueOptions("DAY_NUMBER", date_of_birth_day, true) %></select>
						<select name="date_of_birth_month" id="date_of_birth_month" class="inputfield"><%= Bean.getMeaningFromLookupNameOrderByNymberValueOptions("MONTH_NAME", date_of_birth_month, true) %></select>
						<select name="date_of_birth_year" id="date_of_birth_year" class="inputfield"><%= Bean.getShareholderDateOfBirthYear(date_of_birth_year, true) %></select> 
					</td>
				</tr>
				<tr><td><%=Bean.webposXML.getfieldTransl("client_sex", true) %></td><td><select name="sex" class="inputfield"><%= Bean.getMeaningFromLookupNameOptions("MALE_FEMALE", sex, true) %></select> </td></tr>
				<% if (new_question || (phone_mobile == null || "".equalsIgnoreCase(phone_mobile))) { %>
	  			<tr><td><%=Bean.webposXML.getfieldTransl("client_phone_mobile", true) %></td><td><input type="text" name="phone_mobile" id="phone_mobile" size="40" value="<%=phone_mobile %>" class="inputfield"><br><span class="warning_header"><%=Bean.webposXML.getfieldTransl("client_phone_mobile_warning", false) %></span></td></tr>
				<% } else { %>
	  			<tr>
					<td><%=Bean.webposXML.getfieldTransl("client_phone_mobile", false) %></td>
					<td>
						<input type="hidden" name="phone_mobile" value="<%=phone_mobile %>">
						<input type="text" name="phone_mobile_hide" id="phone_mobile_hide" size="40" value="<%=phone_mobile_hide %>" readonly class="inputfield-ro">
					</td>
				</tr>
				<% } %>
			  	<tr><td><%=Bean.webposXML.getfieldTransl("client_phone_stationary", false) %></td><td><input type="text" name="phone_stationary" id="phone_stationary" size="40" value="<%=phone_stationary %>"  class="inputfield"></td></tr>
	  			<tr><td><%=Bean.webposXML.getfieldTransl("client_email", false) %></td><td><input type="text" name="email" id="email" size="40" value="<%=email %>"  class="inputfield"><br><span class="warning_header"><%=Bean.webposXML.getfieldTransl("client_email_warning", false) %></span></td></tr>


				<tr>
					<td><%=Bean.webposXML.getfieldTransl("client_photo", false) %></td>
					<td>
						<div class="photo_rect" id="div_photo">
							<% if (!(role.getValue("FULL_PHOTO_FILE_NAME") == null || "".equalsIgnoreCase(role.getValue("FULL_PHOTO_FILE_NAME")))) { %>
							<div id="delgimage-0" class="del" title="<%=Bean.webposXML.getfieldTransl("titl_client_photo_delete", false) %>" onclick="del_photo()"></div>
							<div id="edtgimage-0" class="edt" title="<%=Bean.webposXML.getfieldTransl("titl_client_photo_edit", false) %>">
								<input type="file" class="photo_file" name="client_photo" size="50" value="" accept="image/*" capture="camera" onchange="load_photo()" title="<%=Bean.webposXML.getfieldTransl("titl_client_photo_edit", false) %>">
							</div>
							<input type="hidden" name="full_photo_file_name" value="<%=role.getValue("FULL_PHOTO_FILE_NAME") %>">
							<img src="../NatPrsPhoto?id_nat_prs=<%=role.getValue("ID_NAT_PRS") %>&noCache=<%=Bean.getNoCasheValue() %>" class="photo_image">
							<% } else { %>
							<p><%=Bean.webposXML.getfieldTransl("titl_client_photo_load", false) %></p>
							<input type="file" class="photo_file" name="client_photo" size="50" value="" accept="image/*" capture="camera" onchange="load_photo()">
							<% } %>
						</div>
					</td>
				</tr>

				<tr><td colspan="4" class="top_line_gray"><b><%= Bean.webposXML.getfieldTransl("pasport_title", false) %></b></td></tr>
				<tr><td><%= Bean.webposXML.getfieldTransl("pasport_code_country", false) %></td><td><select name="pasport_code_country" id="pasport_code_country" class="inputfield" onchange="checkPassportCountry();"><%= Bean.getCountryOptions(pasport_code_country, true) %></select></td></tr>
      			<tr>
					<td><%=Bean.webposXML.getfieldTransl("pasport_date", false) %></td>
					<td>
						<select name="pasport_date_day" id="pasport_date_day" class="inputfield"><%= Bean.getMeaningFromLookupNameOrderByNymberValueOptions("DAY_NUMBER", pasport_date_day, true) %></select>
						<select name="pasport_date_month" id="pasport_date_month" class="inputfield"><%= Bean.getMeaningFromLookupNameOrderByNymberValueOptions("MONTH_NAME", pasport_date_month, true) %></select>
						<select name="pasport_date_year" id="pasport_date_year" class="inputfield"><%= Bean.getYears(pasport_date_year, true) %></select> 
					</td>
				</tr>
				<tr><td><%= Bean.webposXML.getfieldTransl("pasport_number", false) %> </td><td><input type="text" name="pasport_series" id="pasport_series" size="10" value="<%=pasport_series %>" class="inputfield">&nbsp;<input type="text" name="pasport_number" id="pasport_number" size="25" value="<%=pasport_number %>" class="inputfield"></td></tr>
				<tr><td><%= Bean.webposXML.getfieldTransl("pasport_division_code", false) %> </td><td><input type="text" name="pasport_division_code" id="pasport_division_code" size="10" value="<%=pasport_division_code %>" class="inputfield"></td></tr>
				<tr><td><%= Bean.webposXML.getfieldTransl("pasport_desc", false) %></td><td colspan="3"><textarea name="pasport_desc" cols="37" rows="3" class="inputfield"><%=pasport_desc %></textarea></td></tr>
						
				<tr><td colspan="4" class="top_line_gray"><b><%= Bean.webposXML.getfieldTransl("REG_ADR_FULL", false) %></b></td></tr>
				<tr><td><%= Bean.webposXML.getfieldTransl("reg_adr_country", false) %></td><td><select name="reg_code_country" id="reg_code_country" class="inputfield" onchange="getOblast('reg_adr_oblast',this.value); document.getElementById('reg_adr_oblast_name').value = '';"><%= Bean.getCountryOptions(reg_code_country, true) %></select></td></tr>
				<tr><td><%= Bean.webposXML.getfieldTransl("reg_adr_zip_code", false) %> </td><td><input type="text" name="reg_adr_zip_code" id="reg_adr_zip_code" size="20" value="<%=reg_adr_zip_code %>" class="inputfield"></td></tr>
				<tr><td><%= Bean.webposXML.getfieldTransl("reg_adr_oblast", false) %> </td><td><input type="hidden" name="reg_adr_oblast" id="reg_adr_oblast" value="<%=reg_adr_oblast %>"><input type="text" name="reg_adr_oblast_name" id="reg_adr_oblast_name" size="20" value="<%=Bean.getOblastName(reg_adr_oblast) %>" class="inputfield"></td></tr>
				<tr><td><%= Bean.webposXML.getfieldTransl("reg_adr_district", false) %></td><td><input type="text" name="reg_adr_district" id="reg_adr_district" size="40" value="<%=reg_adr_district %>" class="inputfield"></td></tr>
				<tr><td><%= Bean.webposXML.getfieldTransl("reg_adr_city", false) %> </td><td><input type="text" name="reg_adr_city" id="reg_adr_city" size="40" value="<%=reg_adr_city %>" class="inputfield"></td></tr>
				<tr><td><%= Bean.webposXML.getfieldTransl("reg_adr_street", false) %></td><td><input type="text" name="reg_adr_street" id="reg_adr_street" size="40" value="<%=reg_adr_street %>" class="inputfield"></td></tr>
				<tr>
					<td><%= Bean.webposXML.getfieldTransl("reg_adr_house_case_apartment", false) %></td>
					<td>
						<input type="text" name="reg_adr_house" id="reg_adr_house" size="9" value="<%=reg_adr_house %>" class="inputfield">
						/&nbsp;<input type="text" name="reg_adr_case" id="reg_adr_case" size="8" value="<%=reg_adr_case %>" class="inputfield">
						/&nbsp;<input type="text" name="reg_adr_apartment" id="reg_adr_apartment" size="9" value="<%=reg_adr_apartment %>" class="inputfield">
					</td>
				</tr>
				<tr><td colspan="4" class="top_line_gray"><b><%= Bean.webposXML.getfieldTransl("FACT_ADR_FULL", false) %></b></td></tr>
				<tr><td><%= Bean.webposXML.getfieldTransl("fact_adr_country", false) %></td><td><select name="fact_code_country" id="fact_code_country" class="inputfield"  onchange="getOblast('fact_adr_oblast',this.value); document.getElementById('fact_adr_oblast_name').value = '';"><%= Bean.getCountryOptions(fact_code_country, true) %></select></td></tr>
				<tr><td><%= Bean.webposXML.getfieldTransl("fact_adr_zip_code", false) %></td><td><input type="text" name="fact_adr_zip_code" id="fact_adr_zip_code" size="20" value="<%=fact_adr_zip_code %>" class="inputfield"></td></tr>
				<tr><td><%= Bean.webposXML.getfieldTransl("fact_adr_oblast", false) %> </td><td><input type="hidden" name="fact_adr_oblast" id="fact_adr_oblast" value="<%=fact_adr_oblast %>"><input type="text" name="fact_adr_oblast_name" id="fact_adr_oblast_name" size="20" value="<%=Bean.getOblastName(fact_adr_oblast) %>" class="inputfield"></td></tr>
				<tr><td><%= Bean.webposXML.getfieldTransl("fact_adr_district", false) %></td><td><input type="text" name="fact_adr_district" id="fact_adr_district" size="40" value="<%=fact_adr_district %>" class="inputfield"></td></tr>
				<tr><td><%= Bean.webposXML.getfieldTransl("fact_adr_city", false) %></td><td><input type="text" name="fact_adr_city" id="fact_adr_city" size="40" value="<%=fact_adr_city %>" class="inputfield"></td></tr>
				<tr><td><%= Bean.webposXML.getfieldTransl("fact_adr_street", false) %> </td><td><input type="text" name="fact_adr_street" id="fact_adr_street" size="40" value="<%=fact_adr_street %>" class="inputfield"></td>
				</tr>
				<tr>
					<td><%= Bean.webposXML.getfieldTransl("fact_adr_house_case_apartment", false) %></td>
					<td>
						<input type="text" name="fact_adr_house" id="fact_adr_house" size="9" value="<%=fact_adr_house %>" class="inputfield">
						/&nbsp;<input type="text" name="fact_adr_case" id="fact_adr_case" size="8" value="<%=fact_adr_case %>" class="inputfield">
						/&nbsp;<input type="text" name="fact_adr_apartment" id="fact_adr_apartment" size="9" value="<%=fact_adr_apartment %>" class="inputfield">
					</td>
				</tr>
				<tr>
					<td><%=Bean.webposXML.getfieldTransl("attached_files", false) %></td>
					<td><%=role.getDocumentsListHTML() %>
					<% if (role.getFileCount() >= 10) { %>
					<br><font style="color:blue;"><%=Bean.webposXML.getfieldTransl("attached_files_limit_warting", false) %></font>
					<% } else { %>
						<br>
						<b><%=Bean.buttonXML.getfieldTransl("button_add", false) %></b><br>
						<% if (role.getFileCount() == 0 || role.getFileCount() >= 3) { %>
						<input type="file" style="width:223px" name="file1" size="50" value="" class="inputfield" accept="image/*" capture="camera"><br>
						<% } 
						if (role.getFileCount() < 2) { %>
						<input type="file" style="width:223px" name="file2" size="50" value="" class="inputfield" accept="image/*" capture="camera">
						<% } 
						if (role.getFileCount() < 3) { %>
						<input type="file" style="width:223px" name="file3" size="50" value="" class="inputfield" accept="image/*" capture="camera">
						<% } %>
					<% } %>
					<br><span class="warning_header"><%=Bean.webposXML.getfieldTransl("attached_files_warting", false) %></span>
					</td>
				</tr>

				<tr><td colspan="2">&nbsp;</td></tr>
				<tr><td><%=Bean.webposXML.getfieldTransl("is_questionnaire_checked", true) %></td><td><select name="is_questionnaire_checked" class="inputfield"><%= Bean.getMeaningFromLookupNameOptions("YES_NO", is_questionnaire_checked, true) %></select><br><span class="warning_header"><%=Bean.webposXML.getfieldTransl("questionnaire_checked_warning", false) %></span></td></tr>

				<% if (!action.equalsIgnoreCase("edit")) { %>
					<%if (Bean.C_SUCCESS_RESULT.equalsIgnoreCase(resultFullInt)) { %>
					<tr><td colspan="2">&nbsp;</td></tr>
					<tr><td colspan="2">
						<span id="succes_title"><%=Bean.webposXML.getfieldTransl("title_questionnaire", false) %>:</span>&nbsp;
						<span id="succes_description"><%=Bean.webposXML.getfieldTransl("operation_success", false) %></span>
					</td></tr>
					<% } else { %>
					<tr><td colspan="2">&nbsp;</td></tr>
					<tr><td colspan="2"><span id="error_title"><%=Bean.webposXML.getfieldTransl("operation_error", false) %></span></td></tr>
					<tr><td colspan="2"><span id="error_description"><%=resultFullMessage %></span></td></tr>
					<% } %>
				<% } %>
				<tr><td colspan="2"  align="center">
					<%=Bean.getSubmitButtonMultiPart4("action/new_client_questionnaire.jsp", "button_save", "updateForm3", "div_action_big", null /*"mask_form()"*/) %>
					<% if ("QUESTIONED".equalsIgnoreCase(role.getValue("CD_NAT_PRS_ROLE_STATE"))) { %> 
					<%=Bean.getSubmitButtonAjax("action/new_client_activation.jsp", "activate", "updateForm4", "div_action_big") %>
					<% } %> 
					<% if (Bean.isEmpty(id_user)) { %>
			        <%=Bean.getSubmitButtonAjax("action/new_client.jsp", "button_back", "updateFormBack", "div_main", "") %>
					<% } else { %>
			        <%=Bean.getSubmitButtonAjax("admin/setting.jsp", "button_back", "updateFormBack", "div_main", "") %>
					<% } %>
				</td></tr>
			</table>
		</form>
		<form name="delPhoto" id="delPhoto" accept-charset="UTF-8" method="POST">
	        <input type="hidden" name="action" value="delete">
			<input type="hidden" name="id_role" value="<%=id_role %>">
			<input type="hidden" name="id_nat_prs" value="<%=role.getValue("ID_NAT_PRS") %>">
		</form>

		<form name="updateForm4" id="updateForm4" accept-charset="UTF-8" method="POST">
	        <input type="hidden" name="type" value="client">
	        <input type="hidden" name="action" value="check_card">
	        <input type="hidden" name="process" value="yes">
	        <input type="hidden" name="cd_card1" value="<%=role.getValue("CD_CARD1") %>">
			<input type="hidden" name="id_reg_oper" value="<%=role.getValue("ID_TRANS_CARD_GIVEN") %>">
		</form>
		<% if (Bean.isEmpty(id_user)) { %>
		<form name="updateFormBack" id="updateFormBack" accept-charset="UTF-8" method="POST">
	        <input type="hidden" name="tab" value="2">
		</form>
		<% } else { %>
		<form name="updateFormBack" id="updateFormBack" accept-charset="UTF-8" method="POST">
	        <input type="hidden" name="id_user" value="<%=id_user %>">
	        <input type="hidden" name="tab" value="3">
	        <input type="hidden" name="type" value="edit">
	        <input type="hidden" name="action" value="edit">
	        <input type="hidden" name="process" value="no">
		</form>
		<% } %>

		<% } else if (is_confirmed) { %>
			<script>
				phone_mask("phone_stationary","<%=client_country%>");
				card_mask2("cd_card1");
				card_mask2("referral_in");
				
				/**
				function for execute after ajax request will be processes successfully
				*/
				function onAjaxDone(){
					phone_mask("phone_stationary","<%=client_country%>");
					card_mask2("cd_card1");
					card_mask2("referral_in");
				}
			</script>
		<h1><%=Bean.webposXML.getfieldTransl("title_questionnaire", false) %><%=Bean.getHelpButton("questionnaire", "div_action_big") %></h1>
		<%=Bean.getCardActivationTabSheets("2") %>
		<form name="updateForm3" id="updateForm3" accept-charset="UTF-8" method="POST">
	        <table class="action_table">
				<tr><td colspan="2"><b><%= Bean.webposXML.getfieldTransl("title_sold_card_information", false) %></b></tr>
				<% if ("ADMINISTRATOR".equalsIgnoreCase(Bean.getLoginUserAccessType())) { %>
	  			<tr><td <%=Bean.getDetailTableFirstColumnWidth() %>><%=Bean.webposXML.getfieldTransl("id_user_who_card_sold", false) %></td><td><input type="text" name="id_user" id="id_user" size="40" value="<%=Bean.getUserName(role.getValue("ID_USER_WHO_CARD_SOLD")) %>" readonly class="inputfield-ro"></td></tr>
				<tr><td><%= Bean.webposXML.getfieldTransl("id_term_who_card_sold", false) %></td><td><input type="text" name="id_term" id="id_term" size="40" value="<%=role.getValue("ID_TERM_WHO_CARD_SOLD") %>" readonly class="inputfield-ro"></td></tr>
				<% } %>
	  			<tr><td <%=Bean.getDetailTableFirstColumnWidth() %>><%=Bean.webposXML.getfieldTransl("cd_card1", false) %>&nbsp;(<span style="color:red; cursor:pointer;" onclick="ajaxpage('action/new_client_questionnaire.jsp?id_role=<%=id_role %>&action=edit', 'div_action_big')"><%= Bean.commonXML.getfieldTransl("title_refresh", false).toLowerCase() %></span>)</td><td><input type="text" name="cd_card1_hide" id="cd_card1_hide" size="40" value="<%=role.getValue("CD_CARD1_HIDE") %>" readonly class="inputfield-ro"></td></tr>
				<tr><td><%= Bean.webposXML.getfieldTransl("card_status", false) %></td><td><input type="text" name="card_status" id="card_status" size="20" value="<%=cardStatus %>" readonly class="inputfield_finish_green"></td></tr>
	  			<tr><td><%= Bean.webposXML.getfieldTransl("nat_prs_role_state", false) %></td><td><input type="text" name="v" id="nat_prs_role_state" size="40" value="<%=roleState %>" readonly class="inputfield-ro"></td></tr>
			  	<tr><td><%= Bean.webposXML.getfieldTransl("client_country", false) %></td><td><input type="text" name="client_country_name" id="client_country_name" size="40" value="<%=Bean.getCountryName(client_country) %>" readonly class="inputfield-ro"></td></tr>
      			<tr><td><%=Bean.webposXML.getfieldTransl("date_card_sale", false) %></td><td><input type="text" name="date_card_sale" id="date_card_sale" size="10" value="<%=role.getValue("DATE_CARD_SALE_DF") %>" readonly class="inputfield-ro"></td></tr>
				<tr><td><%=Bean.webposXML.getfieldTransl("total_amount_card_sale", false) %></td><td><input type="text" name="total_amount_card_sale" id="total_amount_card_sale" size="40" value="<%=role.getValue("TOTAL_AMOUNT_CARD_SALE_FRMT") %>" readonly class="inputfield-ro"></td></tr>
			  	<tr><td><%=Bean.webposXML.getfieldTransl("referral_in", false) %></td><td><input type="text" name="referral_in" id="referral_in" size="20" value="<%=referral_in %>" readonly class="inputfield_finish_green"></td></tr>

				<tr><td colspan="2" class="top_line_gray"><b><%= Bean.webposXML.getfieldTransl("client_title", false) %></b></tr>
	  			<tr><td><%=Bean.webposXML.getfieldTransl("client_surname", false) %></td><td><input type="text" name="surname" id="surname" size="40" value="<%=surname %>" readonly class="inputfield-ro"></td></tr>
	  			<tr><td><%=Bean.webposXML.getfieldTransl("client_name", false) %></td><td><input type="text" name="name" size="40" value="<%=name %>" readonly class="inputfield-ro"></td></tr>
	  			<tr><td><%=Bean.webposXML.getfieldTransl("client_patronymic", false) %></td><td><input type="text" name="patronymic" size="40" value="<%=patronymic %>" readonly class="inputfield-ro"></td></tr>
      			<tr><td><%=Bean.webposXML.getfieldTransl("client_date_of_birth", false) %></td><td><input type="text" name="date_of_birth" size="10" value="<%=date_of_birth %>" readonly class="inputfield-ro"></td></tr>

				<tr><td><%=Bean.webposXML.getfieldTransl("client_sex", false) %></td><td><input type="text" name="sex" size="40" value="<%=Bean.getMeaningFoCodeValue("MALE_FEMALE", sex) %>" readonly class="inputfield-ro"></td></tr>
				<tr><td><%=Bean.webposXML.getfieldTransl("client_phone_mobile", false) %></td><td><input type="text" name="phone_mobile" id="phone_mobile" size="40" value="<%=phone_mobile_hide %>" readonly class="inputfield-ro"></td></tr>
				<tr><td><%=Bean.webposXML.getfieldTransl("client_phone_stationary", false) %></td><td><input type="text" name="phone_stationary" id="phone_stationary" size="40" value="<%=phone_stationary %>"  readonly class="inputfield-ro"></td></tr>
	  			<tr><td><%=Bean.webposXML.getfieldTransl("client_email", false) %></td><td><input type="text" name="email" id="email" size="40" value="<%=email %>"  readonly class="inputfield-ro"></td></tr>

				<tr><td colspan="4" class="top_line_gray"><b><%= Bean.webposXML.getfieldTransl("pasport_title", false) %></b></td></tr>
				<tr><td><%= Bean.webposXML.getfieldTransl("pasport_code_country", false) %></td><td><input type="text" name="pasport_code_country" id="pasport_code_country" size="40" value="<%=Bean.getCountryName(pasport_code_country) %>" readonly class="inputfield-ro"></td></tr>
				<tr><td><%= Bean.webposXML.getfieldTransl("pasport_date", false) %> </td><td><input type="text" name="pasport_date" size="10" value="<%=pasport_date %>" readonly class="inputfield-ro"></td></tr>
				<tr><td><%= Bean.webposXML.getfieldTransl("pasport_number", false) %> </td><td><input type="text" name="pasport_series" id="pasport_series" size="10" value="<%=pasport_series %>" readonly class="inputfield-ro">&nbsp;<input type="text" name="pasport_number" id="pasport_number" size="25" value="<%=pasport_number %>" readonly class="inputfield-ro"></td></tr>
				<tr><td><%= Bean.webposXML.getfieldTransl("pasport_division_code", false) %> </td><td><input type="text" name="pasport_division_code" id="pasport_division_code" size="10" value="<%=pasport_division_code %>" readonly class="inputfield-ro"></td></tr>
				<tr><td><%= Bean.webposXML.getfieldTransl("pasport_desc", false) %></td><td colspan="3"><textarea name="pasport_desc" cols="37" rows="3" readonly class="inputfield-ro"><%=pasport_desc %></textarea></td></tr>
						
				<tr><td colspan="4" class="top_line_gray"><b><%= Bean.webposXML.getfieldTransl("REG_ADR_FULL", false) %></b></td></tr>
				<tr><td><%= Bean.webposXML.getfieldTransl("reg_adr_country", false) %></td><td><input type="text" name="reg_adr_country" id="reg_adr_country" size="40" value="<%=Bean.getCountryName(reg_code_country) %>" readonly class="inputfield-ro"></td></tr>
				<tr><td><%= Bean.webposXML.getfieldTransl("reg_adr_zip_code", false) %> </td><td><input type="text" name="reg_adr_zip_code" id="reg_adr_zip_code" size="20" value="<%=reg_adr_zip_code %>" readonly class="inputfield-ro"></td></tr>
				<tr><td><%= Bean.webposXML.getfieldTransl("reg_adr_oblast", false) %></td><td><input type="text" name="reg_adr_oblast" id="reg_adr_oblast" size="40" value="<%=Bean.getOblastName(reg_adr_oblast) %>" readonly class="inputfield-ro"></td></tr>
				<tr><td><%= Bean.webposXML.getfieldTransl("reg_adr_district", false) %></td><td><input type="text" name="reg_adr_district" id="reg_adr_district" size="40" value="<%=reg_adr_district %>" readonly class="inputfield-ro"></td></tr>
				<tr><td><%= Bean.webposXML.getfieldTransl("reg_adr_city", false) %> </td><td><input type="text" name="reg_adr_city" id="reg_adr_city" size="40" value="<%=reg_adr_city %>" readonly class="inputfield-ro"></td></tr>
				<tr><td><%= Bean.webposXML.getfieldTransl("reg_adr_street", false) %></td><td><input type="text" name="reg_adr_street" id="reg_adr_street" size="40" value="<%=reg_adr_street %>" readonly class="inputfield-ro"></td></tr>
				<tr>
					<td><%= Bean.webposXML.getfieldTransl("reg_adr_house_case_apartment", false) %></td>
					<td>
						<input type="text" name="reg_adr_house" id="reg_adr_house" size="9" value="<%=reg_adr_house %>" readonly class="inputfield-ro">
						/&nbsp;<input type="text" name="reg_adr_case" id="reg_adr_case" size="8" value="<%=reg_adr_case %>" readonly class="inputfield-ro">
						/&nbsp;<input type="text" name="reg_adr_apartment" id="reg_adr_apartment" size="9" value="<%=reg_adr_apartment %>" readonly class="inputfield-ro">
					</td>
				</tr>
				<tr><td colspan="4" class="top_line_gray"><b><%= Bean.webposXML.getfieldTransl("FACT_ADR_FULL", false) %></b></td></tr>
				<tr><td><%= Bean.webposXML.getfieldTransl("fact_adr_country", false) %></td><td><input type="text" name="fact_adr_country" id="fact_adr_country" size="40" value="<%=Bean.getCountryName(fact_code_country) %>" readonly class="inputfield-ro"></td></tr>
				<tr><td><%= Bean.webposXML.getfieldTransl("fact_adr_zip_code", false) %></td><td><input type="text" name="fact_adr_zip_code" id="fact_adr_zip_code" size="20" value="<%=fact_adr_zip_code %>" readonly class="inputfield-ro"></td></tr>
				<tr><td><%= Bean.webposXML.getfieldTransl("fact_adr_oblast", false) %></td><td><input type="text" name="fact_adr_oblast" id="fact_adr_oblast" size="40" value="<%=Bean.getOblastName(fact_adr_oblast) %>" readonly class="inputfield-ro"></td></tr>
				<tr><td><%= Bean.webposXML.getfieldTransl("fact_adr_district", false) %></td><td><input type="text" name="fact_adr_district" id="fact_adr_district" size="40" value="<%=fact_adr_district %>" readonly class="inputfield-ro"></td></tr>
				<tr><td><%= Bean.webposXML.getfieldTransl("fact_adr_city", false) %></td><td><input type="text" name="fact_adr_city" id="fact_adr_city" size="40" value="<%=fact_adr_city %>" readonly class="inputfield-ro"></td></tr>
				<tr><td><%= Bean.webposXML.getfieldTransl("fact_adr_street", false) %> </td><td><input type="text" name="fact_adr_street" id="fact_adr_street" size="40" value="<%=fact_adr_street %>" readonly class="inputfield-ro"></td>
				</tr>
				<tr>
					<td><%= Bean.webposXML.getfieldTransl("fact_adr_house_case_apartment", false) %></td>
					<td>
						<input type="text" name="fact_adr_house" id="fact_adr_house" size="9" value="<%=fact_adr_house %>" readonly class="inputfield-ro">
						/&nbsp;<input type="text" name="fact_adr_case" id="fact_adr_case" size="8" value="<%=fact_adr_case %>" readonly class="inputfield-ro">
						/&nbsp;<input type="text" name="fact_adr_apartment" id="fact_adr_apartment" size="9" value="<%=fact_adr_apartment %>" readonly class="inputfield-ro">
					</td>
				</tr>
				
				<tr>
					<td><%=Bean.webposXML.getfieldTransl("attached_files", false) %></td>
					<td><%=role.getDocumentsListHTML() %>
				</tr>

				<tr><td colspan="2">&nbsp;111</td></tr>
				<tr><td><%=Bean.webposXML.getfieldTransl("is_questionnaire_checked", false) %></td><td><input type="text" name="is_questionnaire_checked" id="is_questionnaire_checked" size="10" value="<%=Bean.getMeaningFoCodeValue("YES_NO", is_questionnaire_checked) %>"  readonly class="inputfield-ro"></td></tr>

				<tr><td colspan="2"  align="center">
					<% if ("QUESTIONED".equalsIgnoreCase(role.getValue("CD_NAT_PRS_ROLE_STATE"))) { %> 
					<%=Bean.getSubmitButtonAjax("action/new_client_activation.jsp", "activate", "updateForm4", "div_action_big") %>
					<% } %> 
					<% if (Bean.isEmpty(id_user)) { %>
			        <%=Bean.getSubmitButtonAjax("action/new_client.jsp", "button_back", "updateFormBack", "div_main","") %>
					<% } else { %>
			        <%=Bean.getSubmitButtonAjax("admin/setting.jsp", "button_back", "updateFormBack", "div_main","") %>
					<% } %>
				</td></tr>
			</table>
		</form>
		<form name="updateForm4" id="updateForm4" accept-charset="UTF-8" method="POST">
	        <input type="hidden" name="type" value="client">
	        <input type="hidden" name="action" value="check_card">
	        <input type="hidden" name="process" value="yes">
	        <input type="hidden" name="cd_card1" value="<%=role.getValue("CD_CARD1") %>">
			<input type="hidden" name="id_reg_oper" value="<%=role.getValue("ID_TRANS_CARD_GIVEN") %>">
		</form>

		<% if (Bean.isEmpty(id_user)) { %>
		<form name="updateFormBack" id="updateFormBack" accept-charset="UTF-8" method="POST">
	        <input type="hidden" name="action" value="edit">
			<input type="hidden" name="id_role" value="<%=id_role %>">
		</form>
		<% } else { %>
		<form name="updateFormBack" id="updateFormBack" accept-charset="UTF-8" method="POST">
	        <input type="hidden" name="id_user" value="<%=id_user %>">
	        <input type="hidden" name="tab" value="3">
	        <input type="hidden" name="type" value="edit">
	        <input type="hidden" name="action" value="edit">
	        <input type="hidden" name="process" value="no">
		</form>
		<% } %>

		<% } %>
	<%
} else { 
	%> 
	<%= Bean.getUnknownActionText(action) %><%
}
}
%>


</body>
</html>
