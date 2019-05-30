	<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" 
import="java.io.*,java.util.List,javax.servlet.*,javax.servlet.http.*,org.apache.commons.fileupload.*, org.apache.commons.fileupload.servlet.*, org.apache.commons.fileupload.disk.*, java.io.*"%>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />
<%@ page import="bc.objects.bcDocumentObject"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="bc.objects.bcClubShortObject"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.net.URLEncoder"%>
<%@page import="bc.objects.bcDocumentFileObject"%>
<html>
<head>


	<script type="text/javascript" src="../js/frame_emulator.js" > </script>
<%= Bean.getLogOutScript(request) %>
	<%= Bean.getMetaContent() %>
	<%= Bean.getBottomFrameCSS() %>

</head>

<body>
<%
HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String pageFormName = "CLUB_DOCUMENTS";

Bean.setJspPageForTabName(pageFormName);

request.setCharacterEncoding("UTF-8");

String id		= ""; 
String type     = "";
String action	= ""; 
String process	= "";

String id_doc_file 		= "";
String id_club 			= "";
String id_doc 			= "";
String file_doc         = "";
String file_caption 	= "";
String file_version 	= "";

Boolean transfer_ok     = true;
String file_name        = "";
String stored_file_name = "";
String fileWriteError   = "";

List<FileItem> fileList1 = null;

if(ServletFileUpload.isMultipartContent(request)){
  	
  	// Create a factory for disk-based file items
  	 FileItemFactory factory = new DiskFileItemFactory();
  	// Create a new file upload handler
  	ServletFileUpload upload = new ServletFileUpload(factory);
  	try{
  		
   		fileList1=upload.parseRequest(request);
		//System.out.println("isMultipartContent: fileList1.size="+fileList1.size());
   		for(FileItem file:fileList1){
   			if(file.isFormField()){
   	   			//System.out.println("field1: " + file.getFieldName() + ", value: " + file.getString());
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

   				if ("id_doc_file".equalsIgnoreCase(file.getFieldName())) {
   					id_doc_file = Bean.decodeUtf(file.getString());
	  			}
   				if ("id_club".equalsIgnoreCase(file.getFieldName())) {
   					id_club = Bean.decodeUtf(file.getString());
	  			}
	  			if ("id_club".equalsIgnoreCase(file.getFieldName())) {
	  				id_doc = Bean.decodeUtf(file.getString());
	  			}
	  			if ("file_doc".equalsIgnoreCase(file.getFieldName())) {
	  				file_doc = Bean.decodeUtf(file.getString());
	  			}
	  			if ("file_caption".equalsIgnoreCase(file.getFieldName())) {
	  				file_caption = Bean.prepareStringToSQL(Bean.decodeUtf(file.getString()));
	  			}
	  			if ("file_version".equalsIgnoreCase(file.getFieldName())) {
	  				file_version = Bean.prepareStringToSQL(Bean.decodeUtf(file.getString()));
	  			}
	  		}   
   		}
   		//System.out.println("id_club=" + id_club);
   		bcClubShortObject club = new bcClubShortObject(id_club);
		  			
		String doc_dir = Bean.getClubDocFilesDirectory(club);	

   	   	for(FileItem file2:fileList1){
   	   		if(!file2.isFormField()){
   	   			//System.out.println("field1: " + file2.getFieldName());
   				String filename=file2.getName();
   				//LOGGER.debug(type+" "+process+" "+action+ " filename="+filename);
   				String shortname="";
   				if (filename.contains("\\")) {
   					shortname=filename.substring(filename.lastIndexOf("\\")+1,filename.length());
   				} else if (filename.contains("/")) {
   					shortname=filename.substring(filename.lastIndexOf("/")+1,filename.length());
   				} else {
   					shortname = filename;
   				}
   				//LOGGER.debug(type+" "+process+" "+action+ " shortname="+shortname);
   				file_name = shortname;
  				stored_file_name = doc_dir + "\\" + Bean.getDocumentsPrefix() + "_" + shortname;
  			  		
  	  		    if (!(shortname==null || "".equalsIgnoreCase(shortname))) {
  	  		    	try{
  	  		    		file2.write(new File(stored_file_name));
  	  		    	} catch (Exception e){
  	  		    		transfer_ok = false;
  	  		    		fileWriteError = e.toString();
  	  		    	}
  	  		    } else {
  	  		    	stored_file_name = "";
  	  		    }
  	   		}
  		}
  	} catch (FileUploadException e1) {   
   		System.err.println("Exception:"+e1.getMessage()); 	
  	}catch(Exception ex){
   		System.err.println("Exception:"+ex.getMessage());
  	}
} else {
	id					= Bean.getDecodeParam(parameters.get("id")); 
	type     			= Bean.getDecodeParam(parameters.get("type"));
	action				= Bean.getDecodeParam(parameters.get("action")); 
	process				= Bean.getDecodeParam(parameters.get("process"));
	
	id_doc_file			= Bean.getDecodeParam(parameters.get("id_doc_file")); 
	id_club    			= Bean.getDecodeParam(parameters.get("id_club"));
	id_doc     			= Bean.getDecodeParam(parameters.get("id_doc"));
	file_doc			= Bean.getDecodeParam(parameters.get("file_doc")); 
	file_caption		= Bean.getDecodeParam(parameters.get("file_caption"));
	file_version		= Bean.getDecodeParam(parameters.get("file_version"));

}

if (id==null || ("".equalsIgnoreCase(id))) id="empty";
if (type==null || ("".equalsIgnoreCase(type))) type="empty";
if (action==null || ("".equalsIgnoreCase(action))) action="empty";
if (process==null || ("".equalsIgnoreCase(process))) process="empty";


if (type.equalsIgnoreCase("general")) {
	if (process.equalsIgnoreCase("no")) {
		
		bcDocumentObject doc = new bcDocumentObject(id);
		
		if (action.equalsIgnoreCase("add")) {
			
	        %> 
	
			<script>
				var formData = new Array (
					new Array ('file_caption', 'varchar2', 1),
					new Array ('file_version', 'varchar2', 1),
					new Array ('file_doc', 'varchar2', 1)
				);
			</script>

		<%= Bean.getOperationTitleShort(
				"",
				Bean.documentXML.getfieldTransl("h_add_file", false),
				"Y",
				"N") 
		%>
    <form action="../crm/club/documentfiles.jsp" name="updateForm" id="updateForm" enctype="multipart/form-data" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">
        <input type="hidden" name="type" value="general">
        <input type="hidden" name="action" value="add">
        <input type="hidden" name="process" value="yes">
        <input type="hidden" name="id" value="<%=id %>">
        <input type="hidden" name="id_club" value="<%=doc.getValue("ID_CLUB") %>">
	<table <%=Bean.getTableDetailParam() %>>
	    <tr>
			<td><%= Bean.documentXML.getfieldTransl("title_doc", false) %></td> <td><input type="text" name="full_doc" size="60" value="<%=doc.getValue("FULL_DOC") %>" readonly="readonly" class="inputfield-ro"> </td>
			<td colspan="2">&nbsp;</td>
		</tr>
	    <tr>
			<td><%= Bean.documentXML.getfieldTransl("file_caption", true) %></td> <td><input type="text" name="file_caption" size="60" value="" class="inputfield"></td>
			<td colspan="2">&nbsp;</td>
		</tr>
	    <tr>
			<td><%= Bean.documentXML.getfieldTransl("file_version", true) %></td> <td><input type="text" name="file_version" size="10" value="" class="inputfield"></td>
			<td colspan="2">&nbsp;</td>
		</tr>
	    <tr>
			<td valign="middle"><%= Bean.documentXML.getfieldTransl("file_doc", false) %></td> 
			<td valign="middle">
					<input type="file" name="file_doc" size="60" value="" class="inputfield">
			</td>	
			<td colspan="2">&nbsp;</td>
		</tr>
		<tr>
			<td colspan="6" align="center">
				<%=Bean.getSubmitButtonMultiPart4("../crm/club/documentfiles.jsp", "submit", "updateForm", "div_data_detail", "") %>
				<%=Bean.getResetButton() %>
				<%=Bean.getGoBackButton("../crm/club/documentspecs.jsp?id=" + id) %>
			</td>
		</tr>
	</table>
	</form>

		<% 
		} else if (action.equalsIgnoreCase("edit")) {
			
			bcDocumentFileObject file = new bcDocumentFileObject(id_doc_file);
			
	        %> 
	
			<script>
				var formData = new Array (
					new Array ('file_caption', 'varchar2', 1),
					new Array ('file_version', 'varchar2', 1)
				);
			</script>

		<%= Bean.getOperationTitleShort(
				"",
				Bean.documentXML.getfieldTransl("h_update_file", false),
				"Y",
				"N") 
		%>
    <form action="../crm/club/documentfiles.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">
        <input type="hidden" name="type" value="general">
        <input type="hidden" name="action" value="edit">
        <input type="hidden" name="process" value="yes">
        <input type="hidden" name="id" value="<%=id %>">
        <input type="hidden" name="id_doc_file" value="<%=id_doc_file %>">
	<table <%=Bean.getTableDetailParam() %>>
	    <tr>
			<td><%= Bean.documentXML.getfieldTransl("title_doc", false) %></td> <td><input type="text" name="full_doc" size="60" value="<%=doc.getValue("FULL_DOC") %>" readonly="readonly" class="inputfield-ro"> </td>
			<td colspan="2">&nbsp;</td>
		</tr>
	    <tr>
			<td><%= Bean.documentXML.getfieldTransl("file_caption", true) %></td> <td><input type="text" name="file_caption" size="60" value="<%=file.getValue("FILE_CAPTION") %>" class="inputfield"></td>
			<td colspan="2">&nbsp;</td>
		</tr>
	    <tr>
			<td><%= Bean.documentXML.getfieldTransl("file_version", true) %></td> <td><input type="text" name="file_version" size="10" value="<%=file.getValue("file_version") %>" class="inputfield"></td>
			<td colspan="2">&nbsp;</td>
		</tr>
	    <tr>
			<td><%= Bean.documentXML.getfieldTransl("file_doc", false) %></td> 
			<td><%=file.getValue("FILE_NAME") %>
				<a href="../FileSender?FILENAME=<%=URLEncoder.encode(file.getValue("STORED_FILE_NAME"),"UTF-8")%>" title="<%= Bean.buttonXML.getfieldTransl("open", false) %>" target="_blank">
					<img vspace="0" hspace="0" src="../images/oper/small/open.gif" align="top">
				</a>
			</td>
		</tr>
		<tr>
			<td colspan="6" align="center">
				<%=Bean.getSubmitButtonAjax("../crm/club/documentfiles.jsp", "submit", "updateForm") %>
				<%=Bean.getResetButton() %>
				<%=Bean.getGoBackButton("../crm/club/documentspecs.jsp?id=" + id) %>
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
			
			if (transfer_ok) {
			
				ArrayList<String> pParam = new ArrayList<String>();
					
				pParam.add(id);
				pParam.add(file_name);
				pParam.add(stored_file_name);
				pParam.add(file_caption);
				pParam.add(file_version);
	
			 	%>
				<%= Bean.executeInsertFunction("PACK$DOC_UI.add_doc_file", pParam, "../crm/club/documentspecs.jsp?id=" + id + "&id_doc_file=", "") %>
				<% 	
			} else {%>
				<%=Bean.showCallResult("", "1", fileWriteError, "../crm/club/documentspecs.jsp?id=" + id, "", Bean.documentXML.getfieldTransl("h_upload_file_error", false)) %>
			<%}
		
		} else if (action.equalsIgnoreCase("remove")) {
			
			ArrayList<String> pParam = new ArrayList<String>();
					
			pParam.add(id_doc_file);

		 	%>
			<%= Bean.executeDeleteFunction("PACK$DOC_UI.delete_doc_file", pParam, "../crm/club/documentspecs.jsp?id=" + id, "") %>
			<% 	
	 	
		} else if (action.equalsIgnoreCase("edit")) {
		
			String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK$REFERRAL_UI.update_referral_scheme_line(" + 
				"?,?,?,?,?,?,?,?,?)}";

			ArrayList<String> pParam = new ArrayList<String>();
						
			pParam.add(id_doc_file);
			pParam.add(file_caption);
			pParam.add(file_version);

		 	%>
			<%= Bean.executeUpdateFunction("PACK$DOC_UI.update_doc_file", pParam, "../crm/club/documentspecs.jsp?id=" + id, "") %>
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
