<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" 
import="java.io.*,java.util.List,javax.servlet.*,javax.servlet.http.*,org.apache.commons.fileupload.*, org.apache.commons.fileupload.servlet.*, org.apache.commons.fileupload.disk.*"%>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%= Bean.getLogOutScript(request) %>

<%@page import="java.net.URLEncoder"%>
<%@page import="java.util.HashMap"%>
<%@page import="bc.objects.bcClubShortObject"%>
<%@ page import="org.apache.log4j.Logger" %>

<html>

<head>
	<%= Bean.getMetaContent() %>
	<%= Bean.getBottomFrameCSS() %>
</head>

<body>
<%
Logger LOGGER = Logger.getLogger( "../crm/clients/certificateimport.jsp" );

HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String pageFormName = "CLIENTS_CERTIFICATE";

Bean.setJspPageForTabName(pageFormName);

request.setCharacterEncoding("UTF-8");
String id_term	= "";
String type     = "";
String action	= ""; 
String process	= "";

String 
	file_name 		= "",
	id_club			= "";

List<FileItem> fileList = null;

if(ServletFileUpload.isMultipartContent(request)){
  	FileItemFactory factory = new DiskFileItemFactory();
  	ServletFileUpload upload = new ServletFileUpload(factory);
  	try{

   		fileList=upload.parseRequest(request);
   		for(FileItem file:fileList){
	  		if(file.isFormField()){
	  			if ("id_term".equalsIgnoreCase(file.getFieldName())) {
	  				id_term = Bean.decodeUtf(file.getString());
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
	  			
	  			if ("file_name".equalsIgnoreCase(file.getFieldName())) {
	  				file_name = Bean.prepareStringToSQL(Bean.decodeUtf(file.getString()));
	  			}	 
	  			if ("id_club".equalsIgnoreCase(file.getFieldName())) {
	  				id_club = Bean.prepareStringToSQL(Bean.decodeUtf(file.getString()));
	  			}
	  		}
   		}
  	}catch(Exception ex){
   		System.err.println("Exception:"+ex.getMessage());
  	}
} else {
	id_term		= Bean.getDecodeParam(parameters.get("id_term")); 
	type    	= Bean.getDecodeParam(parameters.get("type"));
	action		= Bean.getDecodeParam(parameters.get("action")); 
	process		= Bean.getDecodeParam(parameters.get("process"));
	
	file_name 		= Bean.getDecodeParam(parameters.get("file_name"));
	id_club 		= Bean.getDecodeParam(parameters.get("id_club"));

}

if (id_term==null || ("".equalsIgnoreCase(id_term))) id_term="empty";
if (type==null || ("".equalsIgnoreCase(type))) type="empty";
if (action==null || ("".equalsIgnoreCase(action))) action="empty";
if (process==null || ("".equalsIgnoreCase(process))) process="empty";

if (type.equalsIgnoreCase("import")) {
	
    String[] results = new String[2];
	if (process.equalsIgnoreCase("no")) {
		if (action.equalsIgnoreCase("load_from_file")) {

			bcClubShortObject club = new bcClubShortObject(Bean.getCurrentClubID());
			
			%>

			<script>
				var formData = new Array (
					new Array ('name_club', 'varchar2', 1),
					new Array ('file_name', 'varchar2', 1)
				);
				function myValidateForm() {
					return validateForm(formData);
				}
			</script>
			<%= Bean.getOperationTitle(
				Bean.terminalXML.getfieldTransl("h_load_certificates", false),
				"Y",
				"N") 
			%>			  	
			<form action="../crm/clients/certificateupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onsubmit="return validateForm(formData)">
			    <input type="hidden" name="type" value="import">
				<input type="hidden" name="process" value="yes">
				<input type="hidden" name="action" value="load_from_file">
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
				       	<td align="left"> 
				          	<%= Bean.terminalCertificateXML.getfieldTransl("file_name", true) %>
				       	</td>
				       	<td align="left">
							<input type="file" name="file_name" size="50" value="" class="inputfield">
						</td>
				    </tr>
				    <tr>
				        <td align="center" colspan="4"> 
							<%=Bean.getSubmitButtonMultiPart("../crm/clients/certificateimport.jsp", "submit", "updateForm") %>
							<%=Bean.getResetButton() %>
							<%=Bean.getGoBackButton("../crm/clients/certificate.jsp") %>
				        </td>
				    </tr>
				 </table>
				 </form>
				 <%
    	} else {
    	    %> <%= Bean.getUnknownActionText(action) %><%
    	}

	} else if (process.equalsIgnoreCase("yes")) {
		if (action.equalsIgnoreCase("load_from_file")) {

			boolean transfer_ok = true;
			String errorText = "";
			String stored_file_name = "";

			String doc_dir = Bean.getDirectorySystemParamValue("UPLOADED_FILES_DIR") + Bean.getCertificateSubDirectory(id_club) + Bean.getDocumentsPrefix() + "_";


			if(ServletFileUpload.isMultipartContent(request)){
	
				for(FileItem file:fileList){
					if(!file.isFormField()){
						String filename=file.getName();
						LOGGER.debug(type+" "+process+" "+action+ " filename="+filename);
						String shortname="";
						if (filename.contains("\\")) {
							shortname=filename.substring(filename.lastIndexOf("\\")+1,filename.length());
						} else if (filename.contains("/")) {
							shortname=filename.substring(filename.lastIndexOf("/")+1,filename.length());
						} else {
							shortname = filename;
						}
						LOGGER.debug(type+" "+process+" "+action+ "shortname="+shortname);
			  			
			  			if (!(shortname==null || "".equalsIgnoreCase(shortname))) {
			  				file_name = shortname;
			  				stored_file_name = doc_dir + shortname;
			  			}
				  		
			  		     	
			  		     	if (!(shortname==null || "".equalsIgnoreCase(shortname))) {
			  		     		try{
			  		     			file.write(new File(stored_file_name));
				  		    	} catch (Exception e){
				  		    		transfer_ok = false;
				  		    		errorText = e.toString();
				  		    	}
			  		     	} else {
			  		     		stored_file_name = "";
				  		    }
			  		}
					
						
		   		}
			 } else {
				 LOGGER.debug(type+" "+process+" "+action+ "File not found");
			 }

			if (transfer_ok) {
				String callSQL = "{? = call " + Bean.getGeneralDBScheme() +	".PACK$TERM_CERTIFICATE_UI.import_certificate(" + 
					"?,?,?,?,?,?)}";

			 	String[] pParam = new String [4];
			 		 	    	      				
			 	pParam[0] = file_name;
				pParam[1] = Bean.getDocumentsPrefix() + "_" + file_name;
				pParam[2] = stored_file_name;
				pParam[3] = id_club;
				
		 		%>
				<%= Bean.showCallSQL(callSQL) %>
						
				<%= Bean.getCallResultParam("RUN", callSQL, pParam, "../crm/clients/certificatespecs.jsp?id_term=" + id_term + "&id_profile=I&id_cert=", "../crm/clients/certificate.jsp") %>
				<% 	
			} else {%>
				<%=Bean.showCallResult("", "1", errorText, "../crm/clients/certificate.jsp", "", Bean.documentXML.getfieldTransl("h_upload_file_error", false)) %>
			<%}

		} else { %> 
			<%= Bean.getUnknownActionText(action) %><%
		}
	} else {
	    %> <%= Bean.getUnknownProcessText(process) %> <%
	}


} else {
	%><%= Bean.getUnknownTypeText(type) %><%
}

%>


</body>
</html>
