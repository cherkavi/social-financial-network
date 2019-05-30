<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" 
import="java.io.*,java.util.List,javax.servlet.*,javax.servlet.http.*,org.apache.commons.fileupload.*, org.apache.commons.fileupload.servlet.*, org.apache.commons.fileupload.disk.*"%>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%= Bean.getLogOutScript(request) %>

<%@ page import="java.net.URLEncoder"%>
<%@ page import="bc.objects.bcJurPrsObject"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="bc.objects.bcNatPrsDocumentObject"%>
<%@ page import="org.apache.log4j.Logger" %>


<%@page import="bc.objects.bcNatPrsObject"%>
<%@page import="bc.objects.bcClubShortObject"%><html>
<head>
	<%= Bean.getMetaContent() %>
	<%= Bean.getBottomFrameCSS() %>
</head>

<body>
<%
Logger LOGGER = Logger.getLogger( "../crm/clients/natpersondocupdate.jsp" );

HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String pageFormName = "CLIENTS_NATPERSONS";

Bean.setJspPageForTabName(pageFormName);

request.setCharacterEncoding("UTF-8");
String id		= ""; 
String type     = "";
String action	= ""; 
String process	= "";

String 
	id_doc 					= "",
	id_nat_prs 				= "",
	cd_nat_prs_doc_type 	= "",
	number_nat_prs_doc 		= "",
	date_nat_prs_doc		= "",
	name_nat_prs_doc 		= "",
	src_nat_prs_doc			= "";

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
	  			
	  			if ("id_doc".equalsIgnoreCase(file.getFieldName())) {
	  				id_doc = Bean.prepareStringToSQL(Bean.decodeUtf(file.getString()));
	  			}

	  			if ("id".equalsIgnoreCase(file.getFieldName())) {
	  				id_nat_prs = Bean.prepareStringToSQL(Bean.decodeUtf(file.getString()));
	  			}
	  			if ("cd_nat_prs_doc_type".equalsIgnoreCase(file.getFieldName())) {
	  				cd_nat_prs_doc_type = Bean.prepareStringToSQL(Bean.decodeUtf(file.getString()));
	  			}
	  			if ("number_nat_prs_doc".equalsIgnoreCase(file.getFieldName())) {
	  				number_nat_prs_doc = Bean.prepareStringToSQL(Bean.decodeUtf(file.getString()));
	  			}
	  			if ("date_nat_prs_doc".equalsIgnoreCase(file.getFieldName())) {
	  				date_nat_prs_doc = Bean.prepareStringToSQL(Bean.decodeUtf(file.getString()));
	  			}
	  			if ("name_nat_prs_doc".equalsIgnoreCase(file.getFieldName())) {
	  				name_nat_prs_doc = Bean.prepareStringToSQL(Bean.decodeUtf(file.getString()));
	  			}
	  			if ("src_nat_prs_doc".equalsIgnoreCase(file.getFieldName())) {
	  				src_nat_prs_doc = Bean.prepareStringToSQL(Bean.decodeUtf(file.getString()));
	  			}
	  		}
   		}
  	}catch(Exception ex){
   		System.err.println("Exception:"+ex.getMessage());
  	}
} else {
	id		= Bean.getDecodeParam(parameters.get("id")); 
	type     = Bean.getDecodeParam(parameters.get("type"));
	action	= Bean.getDecodeParam(parameters.get("action")); 
	process	= Bean.getDecodeParam(parameters.get("process"));
	
	id_doc 					= Bean.getDecodeParam(parameters.get("id_doc"));
	id_nat_prs 				= Bean.getDecodeParam(parameters.get("id"));
	cd_nat_prs_doc_type 	= Bean.getDecodeParam(parameters.get("cd_nat_prs_doc_type"));
	number_nat_prs_doc 		= Bean.getDecodeParam(parameters.get("number_nat_prs_doc"));
	date_nat_prs_doc		= Bean.getDecodeParam(parameters.get("date_nat_prs_doc"));
	name_nat_prs_doc		= Bean.getDecodeParam(parameters.get("name_nat_prs_doc"));
	src_nat_prs_doc			= Bean.getDecodeParam(parameters.get("src_nat_prs_doc"));

}

if (id==null || ("".equalsIgnoreCase(id))) id="empty";
if (type==null || ("".equalsIgnoreCase(type))) type="general";
if (action==null || ("".equalsIgnoreCase(action))) action="empty";
if (process==null || ("".equalsIgnoreCase(process))) process="empty";

if (type.equalsIgnoreCase("doc")) {
	
    String[] results = new String[2];
	
	if (process.equalsIgnoreCase("no")) {
		if (action.equalsIgnoreCase("add")) {

			bcJurPrsObject jur = new bcJurPrsObject(id);
			
			%>
			<script>
				var formData = new Array (
					new Array ('number_nat_prs_doc', 'varchar2', 1),
					new Array ('name_nat_prs_doc', 'varchar2', 1),
					new Array ('date_nat_prs_doc', 'varchar2', 1),
					new Array ('cd_nat_prs_doc_type', 'varchar2', 1)
				);
				function myValidateForm() {
					return validateForm(formData);
				}
			</script>

		<%= Bean.getOperationTitleShort(
				"",
				Bean.documentXML.getfieldTransl("l_add_doc", false),
				"Y",
				"Y") 
		%>
    <form action="../crm/clients/natpersondocupdate.jsp" name="updateForm" id="updateForm" enctype="multipart/form-data" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">
        <input type="hidden" name="type" value="doc">
        <input type="hidden" name="action" value="add">
        <input type="hidden" name="process" value="yes">
        <input type="hidden" name="id" value="<%=id %>">
	<table <%=Bean.getTableDetailParam() %>>
	    <tr>
			<td><%= Bean.documentXML.getfieldTransl("id_doc_type", true) %></td> <td><select name="cd_nat_prs_doc_type" class="inputfield" > <%= Bean.getNatPrsDocTypeOptions("", false) %></select></td>
		</tr>
	    <tr>
			<td><%= Bean.documentXML.getfieldTransl("number_doc", true) %></td> <td><input type="text" name="number_nat_prs_doc" size="25" value="" class="inputfield"></td>
		</tr>
	    <tr>
			<td><%= Bean.documentXML.getfieldTransl("date_doc", true) %></td> <td><%=Bean.getCalendarInputField("date_nat_prs_doc", "", "10") %></td>
		</tr>
	    <tr>
			<td><%= Bean.documentXML.getfieldTransl("name_doc", true) %></td><td><textarea name="name_nat_prs_doc" cols="60" rows="3" class="inputfield"></textarea></td>
		</tr>
	    <tr>
			<td valign="top"><%= Bean.documentXML.getfieldTransl("src_doc", false) %></td> 
			<td valign="top">
				<input type="file" name="src_nat_prs_doc" size="50" value="" class="inputfield">
			</td>
		</tr>
		<tr>
		</tr>
		<tr>
			<td colspan="6" align="center">
				<%=Bean.getSubmitButtonMultiPart("../crm/clients/natpersondocupdate.jsp", "submit", "updateForm") %>
				<%=Bean.getResetButton() %>
				<%=Bean.getGoBackButton("../crm/clients/natpersonspecs.jsp?id=" + id) %>
			</td>
		</tr>
	</table>
	</form>
	<!-- Скрипт для втавки меню вибору дати -->
		<%= Bean.getCalendarScript("date_nat_prs_doc", false) %>

		<% 
		} else if (action.equalsIgnoreCase("edit")) {
			
			bcNatPrsDocumentObject doc = new bcNatPrsDocumentObject(id_doc);
			
	        %> 
			<script>
				var formData = new Array (
					new Array ('number_nat_prs_doc', 'varchar2', 1),
					new Array ('name_nat_prs_doc', 'varchar2', 1),
					new Array ('date_nat_prs_doc', 'varchar2', 1),
					new Array ('cd_nat_prs_doc_type', 'varchar2', 1)
				);
				function myValidateForm() {
					return validateForm(formData);
				}
				function docChange (newDoc) {
					var docHidden = document.getElementById('src_nat_prs_doc');
					docHidden.value = newDoc.value;
				}
			</script>

		<%= Bean.getOperationTitleShort(
				"",
				Bean.documentXML.getfieldTransl("l_edit_doc", false),
				"Y",
				"Y") 
		%>
    <form action="../crm/clients/natpersondocupdate.jsp" name="updateForm" id="updateForm" enctype="multipart/form-data" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">
        <input type="hidden" name="type" value="doc">
        <input type="hidden" name="action" value="edit">
        <input type="hidden" name="process" value="yes">
        <input type="hidden" name="id" value="<%=id %>">
	<table <%=Bean.getTableDetailParam() %>>
    	<tr>
			<td><%= Bean.documentXML.getfieldTransl("id_doc_type", true) %></td> <td><select name="cd_nat_prs_doc_type" class="inputfield" > <%= Bean.getNatPrsDocTypeOptions(doc.getValue("CD_NAT_PRS_DOC_TYPE"), true) %></select></td>
		</tr>
	    <tr>
			<td><%= Bean.documentXML.getfieldTransl("number_doc", true) %></td> <td><input type="text" name="number_nat_prs_doc" size="25" value="<%=doc.getValue("NUMBER_NAT_PRS_DOC") %>" class="inputfield"></td>
		</tr>
    	<tr>
			<td><%= Bean.documentXML.getfieldTransl("date_doc", true) %></td> <td><%=Bean.getCalendarInputField("date_nat_prs_doc", doc.getValue("DATE_NAT_PRS_DOC_FRMT"), "10") %></td>
		</tr>
    	<tr>
			<td><%= Bean.documentXML.getfieldTransl("name_doc", true) %></td><td><textarea name="name_nat_prs_doc" cols="60" rows="3" class="inputfield"><%=doc.getValue("NAME_NAT_PRS_DOC") %></textarea></td>
		</tr>
    	<tr>
			<td><%= Bean.documentXML.getfieldTransl("src_doc", false) %></td> 
			<td>
				<% if (!(doc.getValue("FILE_NAT_PRS_DOC")==null || "".equalsIgnoreCase(doc.getValue("FILE_NAT_PRS_DOC")))) { %>
					<%=doc.getValue("SRC_NAT_PRS_DOC") %><a href="../FileSender?FILENAME=<%=URLEncoder.encode(doc.getValue("FILE_NAT_PRS_DOC"),"UTF-8")%>" title="<%= Bean.buttonXML.getfieldTransl("open", false) %>" target="_blank">
						<img vspace="0" hspace="0" src="../images/oper/small/open.gif" align="top">
					</a>
					<a href="#" onclick="if (window.confirm('<%=Bean.documentXML.getfieldTransl("l_remove_src_doc", false) %>?')) {ajaxpage('../crm/clients/natpersondocupdate.jsp?type=doc&id=<%=id%>&id_doc=<%=id_doc%>&process=yes&action=remove_src', 'div_main')}" title="<%= Bean.buttonXML.getfieldTransl("button_delete", false) %>">
						<img vspace="0" hspace="0" src="../images/oper/small/delete.gif" align="top">
					</a>
					<br>
					<input type="hidden" name="src_nat_prs_doc" id="src_nat_prs_doc" value="<%=doc.getValue("SRC_NAT_PRS_DOC") %>">
					<input type="file" name="src_nat_prs_doc2" size="50" value="<%=doc.getValue("SRC_NAT_PRS_DOC") %>" class="inputfield">
				<% } else {%>
					<input type="file" name="src_nat_prs_doc" size="50" value="<%=doc.getValue("SRC_NAT_PRS_DOC") %>" class="inputfield">
				<% } %>
			</td>
		</tr>
		<%=	Bean.getCreationAndMoficationRecordFields(
				"",
				"1",
				doc.getValue("ID_NAT_PRS_DOC"),
				"",
				doc.getValue(Bean.getCreationDateFieldName()),
				"",
				doc.getValue("CREATED_BY"),
				"",
				doc.getValue(Bean.getLastUpdateDateFieldName()),
				"",
				doc.getValue("LAST_UPDATE_BY"),
				""
			) %>
		<tr>
			<td colspan="6" align="center">
				<%=Bean.getSubmitButtonMultiPart("../crm/clients/natpersondocupdate.jsp", "submit", "updateForm") %>
				<%=Bean.getResetButton() %>
				<%=Bean.getGoBackButton("../crm/clients/natpersonspecs.jsp?id=" + id) %>
			</td>
		</tr>
	</table>
	</form>
	<!-- Скрипт для втавки меню вибору дати -->
		<%= Bean.getCalendarScript("date_nat_prs_doc", false) %>

		<%
			
		} else { %> 
		<%= Bean.getUnknownActionText(action) %><%
		}
	} else if (process.equalsIgnoreCase("yes")) {
		bcNatPrsObject natprs = new bcNatPrsObject(id);

   		bcClubShortObject club = new bcClubShortObject(natprs.getValue("ID_CLUB"));
		  			
		String doc_dir = Bean.getClubDocFilesDirectory(club);	
		if (action.equalsIgnoreCase("add")) {

			boolean transfer_ok = true;
			String errorText = "";
			String file_nat_prs_doc = "";
			
			if(ServletFileUpload.isMultipartContent(request)){
	
				for(FileItem file:fileList){
					if(!file.isFormField()){
						String filename=file.getName();
						LOGGER.debug(type+" "+process+" "+action+ "filename="+filename);
						String shortname="";
						if (filename.contains("\\")) {
							shortname=filename.substring(filename.lastIndexOf("\\")+1,filename.length());
						} else if (filename.contains("/")) {
							shortname=filename.substring(filename.lastIndexOf("/")+1,filename.length());
						} else {
							shortname = filename;
						}
						LOGGER.debug(type+" "+process+" "+action+ "shortname="+shortname);
			  			
						src_nat_prs_doc = shortname;
			  			file_nat_prs_doc = doc_dir + shortname;
				  		
			  		    if (!(shortname==null || "".equalsIgnoreCase(shortname))) {
			  		    	try{
			  		    		file.write(new File(file_nat_prs_doc));
			  		    	} catch (Exception e){
			  		    		transfer_ok = false;
			  		    		errorText = e.toString();
			  		    	}
			  		    } else {
			  		    	file_nat_prs_doc = "";
			  		    }
			  		}
		    		
		   		}
			 } else {
				 LOGGER.debug(type+" "+process+" "+action+ "File not found");
			 }

			if (transfer_ok) {
				String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK$NAT_PRS_UI.add_doc(" + 
					"?,?,?,?,?,?,?,?,?,?)}";

				String[] pParam = new String [8];
									 		 	    	      				
				pParam[0] = id_nat_prs;
				pParam[1] = cd_nat_prs_doc_type;
				pParam[2] = number_nat_prs_doc;
				pParam[3] = date_nat_prs_doc;
				pParam[4] = name_nat_prs_doc;
				pParam[5] = src_nat_prs_doc;
				pParam[6] = file_nat_prs_doc;
				pParam[7] = Bean.getDateFormat();
	
		 		%>
				<%= Bean.getCallResultParam("INSERT", callSQL, pParam, "../crm/clients/natpersonspecs.jsp?id=" + id + "&id_doc=", "") %>
				<% 	
			} else {%>
				<%=Bean.showCallResult("", "1", errorText, "../crm/clients/natpersonspecs.jsp?id=" + id, "", Bean.documentXML.getfieldTransl("h_upload_file_error", false)) %>
			<%}
		
		} else if (action.equalsIgnoreCase("remove")) {
		
			String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK$NAT_PRS_UI.delete_doc(?,?)}";

			String[] pParam = new String [1];
									 		 	    	      				
			pParam[0] = id_doc;

			%>
			<%= Bean.getCallResultParam("DELETE", callSQL, pParam, "../crm/clients/natpersonspecs.jsp?id=" + id, "") %>
			<% 	
	 	
		} else if (action.equalsIgnoreCase("remove_src")) {
			
			String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK$NAT_PRS_UI.delete_src_doc(?,?)}";

			String[] pParam = new String [1];
									 		 	    	      				
			pParam[0] = id_doc;

			%>
			<%= Bean.getCallResultParam("DELETE", callSQL, pParam, "../crm/clients/natpersondocupdate.jsp?type=doc&process=no&action=edit&id=" + id + "&id_doc=" + id_doc, "") %>
			<% 	
	 	
		} else if (action.equalsIgnoreCase("edit")) {

			boolean transfer_ok = true;
			String errorText = "";
			String file_nat_prs_doc = "";
			
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
			  			
						LOGGER.debug(type+" "+process+" "+action+ " shortname="+shortname);

			  			if (!(shortname==null || "".equalsIgnoreCase(shortname))) {
			  				src_nat_prs_doc = shortname;
			  				file_nat_prs_doc = doc_dir + shortname;
			  			}
				  		
			  		     	if (!(shortname==null || "".equalsIgnoreCase(shortname))) {
			  		     		try{
			  		     			file.write(new File(file_nat_prs_doc));
				  		    	} catch (Exception e){
				  		    		transfer_ok = false;
				  		    		errorText = e.toString();
				  		    	}
			  		     	} else {
				  		    	file_nat_prs_doc = "";
				  		    }
			  		}
					
						
		   		}
			 } else {
				 LOGGER.debug(type+" "+process+" "+action+ "File not found");
			 }

			if (transfer_ok) {
			
		 		String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK$NAT_PRS_UI.update_doc(" + 
		 			"?,?,?,?,?,?,?,?,?)}";

				String[] pParam = new String [8];
										 		 	    	      				
				pParam[0] = id_doc;
				pParam[1] = cd_nat_prs_doc_type;
				pParam[2] = number_nat_prs_doc;
				pParam[3] = date_nat_prs_doc;
				pParam[4] = name_nat_prs_doc;
				pParam[5] = src_nat_prs_doc;
				pParam[6] = file_nat_prs_doc;
				pParam[7] = Bean.getDateFormat();
	
				%>
				<%= Bean.getCallResultParam("UPDATE", callSQL, pParam, "../crm/clients/natpersonspecs.jsp?id=" + id, "") %>
				<%
			} else {%>
				<%=Bean.showCallResult("", "1", errorText, "../crm/clients/natpersonspecs.jsp?id=" + id, "", Bean.documentXML.getfieldTransl("h_upload_file_error", false)) %>
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
