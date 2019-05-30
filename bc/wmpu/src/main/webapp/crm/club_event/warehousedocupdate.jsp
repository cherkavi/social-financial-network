<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" 
import="java.io.*,java.util.List,javax.servlet.*,javax.servlet.http.*,org.apache.commons.fileupload.*, org.apache.commons.fileupload.servlet.*, org.apache.commons.fileupload.disk.*"%>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%= Bean.getLogOutScript(request) %>

<%@ page import="bc.objects.bcDocumentObject"%>
<%@ page import="java.net.URLEncoder"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="bc.objects.bcLGObject"%>
<%@ page import="org.apache.log4j.Logger" %>
<html>

<head>
	<%= Bean.getMetaContent() %>
	<%= Bean.getBottomFrameCSS() %>
</head>

<body>
<%
Logger LOGGER = Logger.getLogger( "../crm/club_event/warehousedocupdate.jsp" );

HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String pageFormName = "CLUB_EVENT_WAREHOUSE";

request.setCharacterEncoding("UTF-8");
String id		= ""; 
String type     = "";
String action	= ""; 
String process	= "";
String lg_type   = "";

String doc_dir = Bean.getDirectorySystemParamValue("UPLOADED_FILES_DIR") + Bean.getDocumentsSubDirectory() + Bean.getDocumentsPrefix() + "_";

String 
	id_doc 				= "",
	cd_doc_type 		= "",
	cd_doc_state 		= "",
	number_doc 			= "",
	date_doc 			= "",
	date_begin_doc 		= "",
	date_end_doc 		= "",
	name_doc 			= "",
	desc_doc 			= "",
	id_jur_prs_part1 	= "",
	id_jur_prs_part2	= "",
	id_jur_prs_part3	= "",
	src_doc				= "",
	id_club		        = "";

List<FileItem> fileList = null;

if(ServletFileUpload.isMultipartContent(request)){
  	
  	// Create a factory for disk-based file items
  	FileItemFactory factory = new DiskFileItemFactory();
  	// Create a new file upload handler
  	ServletFileUpload upload = new ServletFileUpload(factory);
  	try{

   		fileList = upload.parseRequest(request);
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
	  			if ("lg_type".equalsIgnoreCase(file.getFieldName())) {
	  				lg_type = Bean.decodeUtf(file.getString());
	  			}
	  			
	  			if ("id_doc".equalsIgnoreCase(file.getFieldName())) {
	  				id_doc = Bean.prepareStringToSQL(Bean.decodeUtf(file.getString()));
	  			}
	  			
	  			if ("cd_doc_type".equalsIgnoreCase(file.getFieldName())) {
	  				cd_doc_type = Bean.prepareStringToSQL(Bean.decodeUtf(file.getString()));
	  			}
	  			if ("cd_doc_state".equalsIgnoreCase(file.getFieldName())) {
	  				cd_doc_state = Bean.prepareStringToSQL(Bean.decodeUtf(file.getString()));
	  			}
	  			if ("number_doc".equalsIgnoreCase(file.getFieldName())) {
	  				number_doc = Bean.prepareStringToSQL(Bean.decodeUtf(file.getString()));
	  			}
	  			if ("date_doc".equalsIgnoreCase(file.getFieldName())) {
	  				date_doc = Bean.prepareStringToSQL(Bean.decodeUtf(file.getString()));
	  			}
	  			if ("date_begin_doc".equalsIgnoreCase(file.getFieldName())) {
	  				date_begin_doc = Bean.prepareStringToSQL(Bean.decodeUtf(file.getString()));
	  			}
	  			if ("date_end_doc".equalsIgnoreCase(file.getFieldName())) {
	  				date_end_doc = Bean.prepareStringToSQL(Bean.decodeUtf(file.getString()));
	  			}
	  			if ("name_doc".equalsIgnoreCase(file.getFieldName())) {
	  				name_doc = Bean.prepareStringToSQL(Bean.decodeUtf(file.getString()));
	  			}
	  			if ("desc_doc".equalsIgnoreCase(file.getFieldName())) {
	  				desc_doc = Bean.prepareStringToSQL(Bean.decodeUtf(file.getString()));
	  			}
	  			if ("id_party1".equalsIgnoreCase(file.getFieldName())) {
	  				id_jur_prs_part1 = Bean.prepareStringToSQL(Bean.decodeUtf(file.getString()));
	  			}
	  			if ("id_party2".equalsIgnoreCase(file.getFieldName())) {
	  				id_jur_prs_part2 = Bean.prepareStringToSQL(Bean.decodeUtf(file.getString()));
	  			}
	  			if ("id_party3".equalsIgnoreCase(file.getFieldName())) {
	  				id_jur_prs_part3 = Bean.prepareStringToSQL(Bean.decodeUtf(file.getString()));
	  			}
	  			if ("src_doc".equalsIgnoreCase(file.getFieldName())) {
	  				src_doc = Bean.prepareStringToSQL(Bean.decodeUtf(file.getString()));
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
	id		= Bean.getDecodeParam(parameters.get("id")); 
	type     = Bean.getDecodeParam(parameters.get("type"));
	action	= Bean.getDecodeParam(parameters.get("action")); 
	process	= Bean.getDecodeParam(parameters.get("process")); 
	lg_type	= Bean.getDecodeParam(parameters.get("lg_type"));
	
	id_doc 				= Bean.getDecodeParam(parameters.get("id_doc"));
	cd_doc_type 		= Bean.getDecodeParam(parameters.get("cd_doc_type"));
	cd_doc_state 		= Bean.getDecodeParam(parameters.get("cd_doc_state"));
	number_doc 			= Bean.getDecodeParam(parameters.get("number_doc"));
	date_doc 			= Bean.getDecodeParam(parameters.get("date_doc"));
	date_begin_doc 		= Bean.getDecodeParam(parameters.get("date_begin_doc"));
	date_end_doc 		= Bean.getDecodeParam(parameters.get("date_end_doc"));
	name_doc 			= Bean.getDecodeParam(parameters.get("name_doc"));
	desc_doc 			= Bean.getDecodeParam(parameters.get("desc_doc"));
	id_jur_prs_part1 	= Bean.getDecodeParam(parameters.get("id_party1"));
	id_jur_prs_part2	= Bean.getDecodeParam(parameters.get("id_party2"));
	id_jur_prs_part3	= Bean.getDecodeParam(parameters.get("id_party3"));
	src_doc				= Bean.getDecodeParam(parameters.get("src_doc"));
	id_club				= Bean.getDecodeParam(parameters.get("id_club"));

}

if (id==null || ("".equalsIgnoreCase(id))) id="empty";
if (type==null || ("".equalsIgnoreCase(type))) type="general";
if (action==null || ("".equalsIgnoreCase(action))) action="empty";
if (process==null || ("".equalsIgnoreCase(process))) process="empty";


Bean.setJspPageForTabName(pageFormName);

if (type.equalsIgnoreCase("doc")) {
	
    String[] results = new String[2];
	
	if (process.equalsIgnoreCase("no")) {
		
		if (action.equalsIgnoreCase("add")) {
			
			bcLGObject logistic = new bcLGObject(lg_type, id);
			
			%>
			<script>
				var formData = new Array (
					new Array ('cd_doc_type', 'varchar2', 1),
					new Array ('cd_doc_state', 'varchar2', 1),
					new Array ('number_doc', 'varchar2', 1),
					new Array ('date_doc', 'varchar2', 1),
					new Array ('date_begin_doc', 'varchar2', 1),
					new Array ('name_doc', 'varchar2', 1),
					new Array ('name_party1', 'varchar2', 1)
				);
			</script>

		<%= Bean.getOperationTitle(
	    		Bean.documentXML.getfieldTransl("l_add_doc", false),
				"Y",
				"Y") 
		%>
    <form action="../crm/club_event/warehousedocupdate.jsp" name="updateForm" id="updateForm" enctype="multipart/form-data" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">
        <input type="hidden" name="type" value="doc">
        <input type="hidden" name="action" value="add">
        <input type="hidden" name="process" value="yes">
        <input type="hidden" name="lg_type" value="<%=lg_type %>">
        <input type="hidden" name="id" value="<%=id %>">
        <input type="hidden" name="id_club" value="<%=logistic.getValue("ID_CLUB") %>">
	<table <%=Bean.getTableDetailParam() %>>
	    <tr>
			<td><%= Bean.documentXML.getfieldTransl("cd_doc_type", true) %></td> <td><select name="cd_doc_type" class="inputfield" > <%= Bean.getLGDocTypeOptions("", false) %></select></td>
			<td><%= Bean.clubXML.getfieldTransl("club", false) %>
				<%= Bean.getGoToClubLink(logistic.getValue("ID_CLUB")) %>
			</td><td><input type="text" name="name_club" size="55" value="<%= Bean.getClubShortName(logistic.getValue("ID_CLUB")) %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
	    <tr>
			<td><%= Bean.documentXML.getfieldTransl("number_doc", true) %></td> <td><input type="text" name="number_doc" size="25" value="" class="inputfield"></td>
			<td><%= Bean.documentXML.getfieldTransl("cd_doc_state", true) %></td> <td><select name="cd_doc_state" class="inputfield" > <%= Bean.getDocStateOptions("", true) %></select></td>
		</tr>
	    <tr>
			<td><%= Bean.documentXML.getfieldTransl("date_doc", true) %></td> <td><%=Bean.getCalendarInputField("date_doc", "", "10") %></td>
			<td><%= Bean.documentXML.getfieldTransl("date_begin_doc", true) %></td> <td><%=Bean.getCalendarInputField("date_begin_doc", "", "10") %></td>
		</tr>
	    <tr>
			<td><%= Bean.documentXML.getfieldTransl("name_doc", true) %></td> <td><input type="text" name="name_doc" size="64" value="" class="inputfield"></td>
			<td><%= Bean.documentXML.getfieldTransl("date_end_doc", false) %></td> <td><%=Bean.getCalendarInputField("date_end_doc", "", "10") %></td>
		</tr>
	    <tr>
			<td rowspan="3"><%= Bean.documentXML.getfieldTransl("desc_doc", false) %></td><td rowspan="3"><textarea name="desc_doc" cols="60" rows="3" class="inputfield"></textarea></td>
			<td class="top_line"><%= Bean.getLGTitle(logistic.getValue("CD_LG_TYPE")) %></td> <td class="top_line"><input type="text" name="operation_name" size="55" value="<%=logistic.getValue("OPERATION_NAME") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
	    <tr>
			<td><b><%= Bean.documentXML.getfieldTransl("name_party1", true) %></b></td>
			<td>
				<%=Bean.getWindowFindJurPrs("party1", logistic.getValue("ID_JUR_PRS_RECEIVER"), logistic.getValue("SNAME_JUR_PRS_RECEIVER"), "ALL", "50") %>
			</td>			
		</tr>
	    <tr>
			<td><%= Bean.documentXML.getfieldTransl("name_party2", false) %></td>
			<td>
				<%=Bean.getWindowFindJurPrs("party2", logistic.getValue("ID_JUR_PRS_SENDER"), logistic.getValue("SNAME_JUR_PRS_SENDER"), "ALL", "50") %>
			</td>			
		</tr>
		<tr>
			<td valign="top"><%= Bean.documentXML.getfieldTransl("src_doc", false) %></td> 
			<td valign="top">
				<input type="file" name="src_doc" size="50" value="" class="inputfield">
			</td>
			<td><%= Bean.documentXML.getfieldTransl("name_party3", false) %></td>
			<td>
				<%=Bean.getWindowFindJurPrs("party3", "", "", "ALL", "50") %>
			</td>
		</tr>
		<tr>
			<td colspan="6" align="center">
				<%=Bean.getSubmitButtonMultiPart("../crm/club_event/warehousedocupdate.jsp", "submit", "updateForm") %>
				<%=Bean.getResetButton() %>
				<%=Bean.getGoBackButton("../crm/club_event/warehousespecs.jsp?id=" + id) %>
			</td>
		</tr>
	</table>
	</form>
	<!-- Скрипт для втавки меню вибору дати -->
	<%= Bean.getCalendarScript("date_doc", false) %>
	<%= Bean.getCalendarScript("date_begin_doc", false) %>
	<%= Bean.getCalendarScript("date_end_doc", false) %>

		<% 
		} else if (action.equalsIgnoreCase("edit")) {
			
			bcDocumentObject doc = new bcDocumentObject(id_doc);
			
			bcLGObject logistic = new bcLGObject(lg_type, doc.getValue("ID_LG_RECORD"));
			
	        %> 
			<script>
				var formData = new Array (
					new Array ('cd_doc_type', 'varchar2', 1),
					new Array ('cd_doc_state', 'varchar2', 1),
					new Array ('number_doc', 'varchar2', 1),
					new Array ('date_doc', 'varchar2', 1),
					new Array ('date_begin_doc', 'varchar2', 1),
					new Array ('name_doc', 'varchar2', 1),
					new Array ('name_party1', 'varchar2', 1)
				);
				function docChange (newDoc) {
					var docHidden = document.getElementById('src_doc');
					docHidden.value = newDoc.value;
				}
			</script>

		<%= Bean.getOperationTitle(
	    		Bean.documentXML.getfieldTransl("l_edit_doc", false),
				"Y",
				"Y") 
		%>
    <form action="../crm/club_event/warehousedocupdate.jsp" name="updateForm" id="updateForm" enctype="multipart/form-data" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">
        <input type="hidden" name="type" value="doc">
        <input type="hidden" name="action" value="edit">
        <input type="hidden" name="process" value="yes">
        <input type="hidden" name="lg_type" value="<%=lg_type %>">
        <input type="hidden" name="id" value="<%=doc.getValue("ID_LG_RECORD") %>">
	<table <%=Bean.getTableDetailParam() %>>
	    <tr>
			<td><%= Bean.documentXML.getfieldTransl("id_doc", false) %></td> <td><input type="text" name="id_doc" size="25" value="<%=doc.getValue("ID_DOC") %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.clubXML.getfieldTransl("club", false) %>
				<%= Bean.getGoToClubLink(doc.getValue("ID_CLUB")) %>
			</td><td><input type="text" name="name_club" size="55" value="<%= Bean.getClubShortName(doc.getValue("ID_CLUB")) %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
    	<tr>
			<td><%= Bean.documentXML.getfieldTransl("cd_doc_type", true) %></td> <td><select name="cd_doc_type" class="inputfield" > <%= Bean.getDocTypeOptions(doc.getValue("CD_DOC_TYPE"), false) %></select></td>
			<td><%= Bean.documentXML.getfieldTransl("cd_doc_state", true) %></td> <td><select name="cd_doc_state" class="inputfield" > <%= Bean.getDocStateOptions(doc.getValue("CD_DOC_STATE"), true) %></select></td>
		</tr>
    	<tr>
			<td><%= Bean.documentXML.getfieldTransl("number_doc", true) %></td> <td><input type="text" name="number_doc" size="25" value="<%=doc.getValue("NUMBER_DOC") %>" class="inputfield"></td>
			<td><%= Bean.documentXML.getfieldTransl("date_begin_doc", false) %></td> <td><%=Bean.getCalendarInputField("date_begin_doc", doc.getValue("DATE_BEGIN_DOC_FRMT"), "10") %></td>
		</tr>
    	<tr>
			<td><%= Bean.documentXML.getfieldTransl("date_doc", true) %></td> <td><%=Bean.getCalendarInputField("date_doc", doc.getValue("DATE_DOC_FRMT"), "10") %></td>
			<td><%= Bean.documentXML.getfieldTransl("date_end_doc", false) %></td> <td><%=Bean.getCalendarInputField("date_end_doc", doc.getValue("DATE_END_DOC_FRMT"), "10") %></td>
		</tr>
    	<tr>
			<td><%= Bean.documentXML.getfieldTransl("name_doc", true) %></td> <td><input type="text" name="name_doc" size="64" value="<%=doc.getValue("NAME_DOC") %>" class="inputfield"></td>
			<td class="top_line"><%= Bean.getLGTitle(logistic.getValue("CD_LG_TYPE")) %></td> <td class="top_line"><input type="text" name="operation_name" size="55" value="<%=logistic.getValue("OPERATION_NAME") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
    	<tr>
			<td rowspan="3"><%= Bean.documentXML.getfieldTransl("desc_doc", false) %></td><td rowspan="3"><textarea name="desc_doc" cols="60" rows="3" class="inputfield"><%=doc.getValue("DESC_DOC") %></textarea></td>
			<td><b><%= Bean.documentXML.getfieldTransl("name_party1", true) %></b>
			<%=Bean.getGoToJurPrsHyperLink(doc.getValue("ID_JUR_PRS_PARTY1")) %>
			</td>
			<td>
				<%=Bean.getWindowFindJurPrs("party1", doc.getValue("ID_JUR_PRS_PARTY1"), doc.getValue("SNAME_JUR_PRS_PARTY1"), "ALL", "50") %>
			</td>			
		</tr>
    	<tr>
			<td><%= Bean.documentXML.getfieldTransl("name_party2", false) %>
			<%=Bean.getGoToJurPrsHyperLink(doc.getValue("ID_JUR_PRS_PARTY2")) %>
			</td>
			<td>
				<%=Bean.getWindowFindJurPrs("party2", doc.getValue("ID_JUR_PRS_PARTY2"), doc.getValue("SNAME_JUR_PRS_PARTY2"), "ALL", "50") %>
			</td>			
		</tr>
    	<tr>
			<td><%= Bean.documentXML.getfieldTransl("name_party3", false) %>
			<%=Bean.getGoToJurPrsHyperLink(doc.getValue("ID_JUR_PRS_PARTY3")) %>
			</td>
			<td>
				<%=Bean.getWindowFindJurPrs("party3", doc.getValue("ID_JUR_PRS_PARTY3"), doc.getValue("SNAME_JUR_PRS_PARTY3"), "ALL", "50") %>
			</td>			
		</tr>
    	<tr>
			<td><%= Bean.documentXML.getfieldTransl("src_doc", false) %></td> 
			<td>
				<% if (!(doc.getValue("FILE_DOC")==null || "".equalsIgnoreCase(doc.getValue("FILE_DOC")))) { %>
					<%=doc.getValue("SRC_DOC") %><a href="../FileSender?FILENAME=<%=URLEncoder.encode(doc.getValue("FILE_DOC"),"UTF-8")%>" title="<%= Bean.buttonXML.getfieldTransl("open", false) %>" target="_blank">
						<img vspace="0" hspace="0" src="../images/oper/small/open.gif" align="top">
					</a>
					<a href="#" onclick="if (window.confirm('<%=Bean.documentXML.getfieldTransl("l_remove_src_doc", false) %>?')) {ajaxpage('../crm/club_event/warehousedocupdate.jsp?lg_type=<%=lg_type %>&type=doc&id=<%=id%>&id_doc=<%=id_doc%>&process=yes&action=remove_src', 'div_main')}" title="<%= Bean.buttonXML.getfieldTransl("button_delete", false) %>">
						<img vspace="0" hspace="0" src="../images/oper/small/delete.gif" align="top">
					</a>
					<br>
					<input type="hidden" name="src_doc" id="src_doc" value="<%=doc.getValue("SRC_DOC") %>">
					<input type="file" name="src_doc2" size="50" value="<%=doc.getValue("SRC_DOC") %>" class="inputfield">
				<% } else {%>
					<input type="file" name="src_doc" size="50" value="<%=doc.getValue("SRC_DOC") %>" class="inputfield">
				<% } %>
			</td>
		</tr>
		<tr>
			<td colspan="2">&nbsp;</td>
		</tr>
		<%=	Bean.getCreationAndMoficationRecordFields(
				doc.getValue(Bean.getCreationDateFieldName()),
				doc.getValue("CREATED_BY"),
				doc.getValue(Bean.getLastUpdateDateFieldName()),
				doc.getValue("LAST_UPDATE_BY")
			) %>
		<tr>
			<td colspan="6" align="center">
				<%=Bean.getSubmitButtonMultiPart("../crm/club_event/warehousedocupdate.jsp", "submit", "updateForm") %>
				<%=Bean.getResetButton() %>
				<%=Bean.getGoBackButton("../crm/club_event/warehousespecs.jsp?id=" + id) %>
			</td>
		</tr>
	</table>
	</form>
	<!-- Скрипт для втавки меню вибору дати -->
	<%= Bean.getCalendarScript("date_doc", false) %>
	<%= Bean.getCalendarScript("date_begin_doc", false) %>
	<%= Bean.getCalendarScript("date_end_doc", false) %>

		<%
			
		} else { %> 
		<%= Bean.getUnknownActionText(action) %><%
		}
	} else if (process.equalsIgnoreCase("yes")) {
		if (action.equalsIgnoreCase("add")) {

			boolean transfer_ok = true;
			String errorText = "";
			String file_doc = "";
			
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
			  			
			  			src_doc = shortname;
			  			file_doc = doc_dir + shortname;
				  		
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
			 } else {
				 LOGGER.error(type+" "+process+" "+action+ " File not found");
			 }

			if (transfer_ok) {
				String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK$DOC_UI.add_doc(" + 
					"?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";

				String[] pParam = new String [17];
											 		 	    	      				
				pParam[0] = cd_doc_type;
				pParam[1] = cd_doc_state;
				pParam[2] = number_doc;
				pParam[3] = date_doc;
				pParam[4] = date_begin_doc;
				pParam[5] = date_end_doc;
				pParam[6] = name_doc;
				pParam[7] = desc_doc;
				pParam[8] = id_club;
				pParam[9] = id_jur_prs_part1;
				pParam[10] = id_jur_prs_part2;
				pParam[11] = id_jur_prs_part3;
				pParam[12] = "";
				pParam[13] = src_doc;
				pParam[14] = file_doc;
				pParam[15] = id;
				pParam[16] = Bean.getDateFormat();
	
		 		%>
				<%= Bean.getCallResultParam("INSERT", callSQL, pParam, "../crm/club_event/warehousespecs.jsp?id=" + id + "&id_doc=", "") %>
				<% 	
			} else {%>
				<%=Bean.showCallResult("", "1", errorText, "../crm/club_event/warehousespecs.jsp?id=" + id, "", Bean.documentXML.getfieldTransl("h_upload_file_error", false)) %>
			<%}
		
		} else if (action.equalsIgnoreCase("remove")) {
		
			String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK$DOC_UI.delete_doc(?,?)}";

			String[] pParam = new String [1];
					
			pParam[0] = id_doc;

			%>
			<%= Bean.getCallResultParam("DELETE", callSQL, pParam, "../crm/club_event/warehousespecs.jsp?id=" + id, "") %>
			<% 	
	 	
		} else if (action.equalsIgnoreCase("remove_src")) {
			
			String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK$DOC_UI.delete_src_doc(?,?)}";

			String[] pParam = new String [1];
					
			pParam[0] = id_doc;

			%>
			<%= Bean.getCallResultParam("DELETE", callSQL, pParam, "../crm/club_event/warehousedocupdate.jsp?type=doc&process=no&action=edit&id=" + id + "&id_doc=" + id_doc + "&lg_type" + lg_type, "") %>
			<% 	
	 	
		} else if (action.equalsIgnoreCase("edit")) {

			boolean transfer_ok = true;
			String errorText = "";
			String file_doc = "";
			
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
			  				src_doc = shortname;
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
			 } else {
				 LOGGER.error(type+" "+process+" "+action+ " File not found");				 
			 }

			if (transfer_ok) {
			
		 		String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK$DOC_UI.update_doc(" + 
	 				"?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";

				String[] pParam = new String [17];
												 		 	    	      				
				pParam[0] = id_doc;
				pParam[1] = cd_doc_type;
				pParam[2] = cd_doc_state;
				pParam[3] = number_doc;
				pParam[4] = date_doc;
				pParam[5] = date_begin_doc;
				pParam[6] = date_end_doc;
				pParam[7] = name_doc;
				pParam[8] = desc_doc;
				pParam[9] = id_jur_prs_part1;
				pParam[10] = id_jur_prs_part2;
				pParam[11] = id_jur_prs_part3;
				pParam[12] = "";
				pParam[13] = src_doc;
				pParam[14] = file_doc;
				pParam[15] = id;
				pParam[16] = Bean.getDateFormat();
	
				%>
				<%= Bean.getCallResultParam("UPDATE", callSQL, pParam, "../crm/club_event/warehousespecs.jsp?id=" + id, "") %>
				<%
			} else {%>
				<%=Bean.showCallResult("", "1", errorText, "../crm/club_event/warehousespecs.jsp?id=" + id, "", Bean.documentXML.getfieldTransl("h_upload_file_error", false)) %>
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
