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
<%@page import="java.awt.Image"%>
<%@page import="javax.imageio.ImageIO"%><html>
<head>
	<%= Bean.getMetaContent() %>

</head>
<body>
<%
request.setCharacterEncoding("UTF-8");

HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String pageFormName = "WEBPOS_SERVICE_CARD_ACTIVATION";

Bean.setJspPageForTabName(pageFormName);

Bean.readWebPosMenuHTML();

request.setCharacterEncoding("UTF-8");
String action		= Bean.getDecodeParam(parameters.get("action")); 

//String id_role		= Bean.getDecodeParam(parameters.get("id_role"));
String id_nat_prs	= Bean.getDecodeParam(parameters.get("id_nat_prs"));
String id_term	 	= Bean.getCurrentTerm();

String photoFileName="";
String fullPhotoFileName="";

String photo_dir = Bean.getDirectorySystemParamValue("UPLOADED_FILES_DIR") + "documents/uploaded/photo/";
	
String file_name               = "";
String fieldName               = "";
boolean loadError              = false;

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
	  			if ("id_nat_prs".equalsIgnoreCase(file.getFieldName())) id_nat_prs = Bean.decodeUtf(file.getString());
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
					fFileName = photo_dir + Bean.getDocumentsPrefix() + "_" + sFileName;
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
   		System.out.println("file "+photoFileName+" loaded"); 
  	}catch(Exception ex){
   		System.out.println("Exception (fieldName="+fieldName+", file_name="+file_name+"):"+ex.getMessage());
   		photoFileName = "";
   		fullPhotoFileName = "";
   		loadError = true;
   		%>
   		
			<p><%=Bean.webposXML.getfieldTransl("titl_load_file_error", false) %>:<br> <%=ex.getMessage() %></p>
			<input type="file" class="photo_file" name="file1" size="50" value="" accept="image/*" capture="camera" onchange="load_photo()">
   		<%
   		//action = "error";
  	}
	
}
System.out.println("fullPhotoFileName="+fullPhotoFileName);
action 	= Bean.isEmpty(action)?"":action;

if (!loadError) {
	if (action.equalsIgnoreCase("update")) {
	 	//wpOnlineOperationObject oper = new wpOnlineOperationObject(id_telgr);
				
		//wpNatPrsRoleObject role = new wpNatPrsRoleObject(id_role);
		//wpNatPrsObject nat_prs = new wpNatPrsObject(id_nat_prs);
		
		String resultInt        = "0";
		String resultMessage    = "";
		
		ArrayList<String> pParam = new ArrayList<String>();
			
		pParam.add(id_nat_prs);
		pParam.add(fullPhotoFileName);
			
		String[] results = new String[2];
			
		results 		= Bean.executeFunction("PACK$WEBPOS_UI.set_nat_prs_photo", pParam, results.length);
		resultInt 		= results[0];
		resultMessage 	= results[1];
		if (Bean.C_SUCCESS_RESULT.equalsIgnoreCase(resultInt)) {%>
			<div id="delgimage-0" class="del" title="<%=Bean.webposXML.getfieldTransl("titl_client_photo_delete", false) %>" onclick="del_photo()"></div>
			<div id="edtgimage-0" class="edt" title="<%=Bean.webposXML.getfieldTransl("titl_client_photo_edit", false) %>">
				<input type="file" class="photo_file" name="client_photo" size="50" value="" accept="image/*" capture="camera" onchange="load_photo()" title="<%=Bean.webposXML.getfieldTransl("titl_client_photo_edit", false) %>">
			</div>
			<input type="hidden" name="full_photo_file_name" value="<%=fullPhotoFileName %>">
			<img src="../NatPrsPhoto?id_nat_prs=<%=id_nat_prs %>&noCache=<%=Bean.getNoCasheValue() %>" class="photo_image">
		<% } else { %>
			<p>error: <%=resultMessage %></p>
			<input type="file" class="photo_file" name="client_photo" size="50" value="" accept="image/*" capture="camera" onchange="load_photo()">
		<% } %>
	
		<%
	} else if (action.equalsIgnoreCase("delete")) {
		 	//wpOnlineOperationObject oper = new wpOnlineOperationObject(id_telgr);
					
			String resultInt        = "0";
			String resultMessage    = "";
					
			ArrayList<String> pParam = new ArrayList<String>();
				
			pParam.add(id_nat_prs);
				
			String[] results = new String[2];
				
			results 		= Bean.executeFunction("PACK$WEBPOS_UI.delete_nat_prs_photo", pParam, results.length);
			resultInt 		= results[0];
			resultMessage 	= results[1];
			if (Bean.C_SUCCESS_RESULT.equalsIgnoreCase(resultInt)) {%>
				<p><%=Bean.webposXML.getfieldTransl("titl_client_photo_load", false) %></p>
				<input type="file" class="photo_file" name="client_photo" size="50" value="" accept="image/*" capture="camera" onchange="load_photo()">
			<% } else { %>
				<p>error: <%=resultMessage %></p>
				<div id="delgimage-0" class="del" title="<%=Bean.webposXML.getfieldTransl("titl_client_photo_delete", false) %>" onclick="del_photo()"></div>
				<div id="edtgimage-0" class="edt" title="<%=Bean.webposXML.getfieldTransl("titl_client_photo_edit", false) %>">
					<input type="file" class="photo_file" name="client_photo" size="50" value="" accept="image/*" capture="camera" onchange="load_photo()" title="<%=Bean.webposXML.getfieldTransl("titl_client_photo_edit", false) %>">
				</div>
				<input type="hidden" name="full_photo_file_name" value="">
				<img src="../NatPrsPhoto?id_nat_prs=<%=id_nat_prs %>&noCache=<%=Bean.getNoCasheValue() %>" class="photo_image">
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
