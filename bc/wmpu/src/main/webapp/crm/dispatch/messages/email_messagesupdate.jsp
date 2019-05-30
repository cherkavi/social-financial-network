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
Logger LOGGER = Logger.getLogger( "../crm/dispatch/messages/email_messagesupdate.jsp" );

HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String pageFormName = "DISPATCH_MESSAGES_EMAIL";

Bean.setJspPageForTabName(pageFormName);

request.setCharacterEncoding("UTF-8");

String id					= ""; 
String process  			= "";
String action				= "";
String type					= "";

String text_ds_message		= "";
String begin_action_date	= "";
String end_action_date		= "";
String cd_ds_message_state	= "";
String basis_for_operation	= "";
String title_ds_message		= "";
String id_email_profile		= "";
String attach_file			= "";
String is_archive			= "";
String id_club				= "";
String cd_ds_message_send_state	= "";
String id_ds_message_send		= "";

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
	  			if ("action".equalsIgnoreCase(file.getFieldName())) {
	  				action = Bean.decodeUtf(file.getString());
	  			}
	  			if ("process".equalsIgnoreCase(file.getFieldName())) {
	  				process = Bean.decodeUtf(file.getString());
	  			}
	  			if ("type".equalsIgnoreCase(file.getFieldName())) {
	  				type = Bean.decodeUtf(file.getString());
	  			}
	  			
	  			
	  			if ("text_ds_message".equalsIgnoreCase(file.getFieldName())) {
	  				text_ds_message = Bean.prepareStringToSQL(Bean.decodeUtf(file.getString()));
	  			}
	  			if ("begin_action_date".equalsIgnoreCase(file.getFieldName())) {
	  				begin_action_date = Bean.prepareStringToSQL(Bean.decodeUtf(file.getString()));
	  			}
	  			if ("end_action_date".equalsIgnoreCase(file.getFieldName())) {
	  				end_action_date = Bean.prepareStringToSQL(Bean.decodeUtf(file.getString()));
	  			}
	  			if ("cd_ds_message_state".equalsIgnoreCase(file.getFieldName())) {
	  				cd_ds_message_state = Bean.prepareStringToSQL(Bean.decodeUtf(file.getString()));
	  			}
	  			if ("basis_for_operation".equalsIgnoreCase(file.getFieldName())) {
	  				basis_for_operation = Bean.prepareStringToSQL(Bean.decodeUtf(file.getString()));
	  			}
	  			if ("title_ds_message".equalsIgnoreCase(file.getFieldName())) {
	  				title_ds_message = Bean.prepareStringToSQL(Bean.decodeUtf(file.getString()));
	  			}
	  			if ("id_email_profile".equalsIgnoreCase(file.getFieldName())) {
	  				id_email_profile = Bean.prepareStringToSQL(Bean.decodeUtf(file.getString()));
	  			}
	  			if ("attach_file".equalsIgnoreCase(file.getFieldName())) {
	  				attach_file = Bean.prepareStringToSQL(Bean.decodeUtf(file.getString()));
	  			}
	  			if ("id_club".equalsIgnoreCase(file.getFieldName())) {
	  				id_club = Bean.prepareStringToSQL(Bean.decodeUtf(file.getString()));
	  			}
	  			if ("is_archive".equalsIgnoreCase(file.getFieldName())) {
	  				is_archive = Bean.prepareStringToSQL(Bean.decodeUtf(file.getString()));
	  			}
	  			if ("cd_ds_message_send_state".equalsIgnoreCase(file.getFieldName())) {
	  				cd_ds_message_send_state = Bean.prepareStringToSQL(Bean.decodeUtf(file.getString()));
	  			}
	  			if ("id_ds_message_send".equalsIgnoreCase(file.getFieldName())) {
	  				id_ds_message_send = Bean.prepareStringToSQL(Bean.decodeUtf(file.getString()));
	  			}
	  		}
   		}
   		
  	}catch(Exception ex){
   		System.err.println("Exception:"+ex.getMessage());
  	}
} else {
	id		= Bean.getDecodeParam(parameters.get("id")); 
	action	= Bean.getDecodeParam(parameters.get("action")); 
	process	= Bean.getDecodeParam(parameters.get("process")); 
	type	= Bean.getDecodeParam(parameters.get("type"));
	
	text_ds_message				= Bean.getDecodeParam(parameters.get("text_ds_message"));
	begin_action_date			= Bean.getDecodeParam(parameters.get("begin_action_date"));
	end_action_date				= Bean.getDecodeParam(parameters.get("end_action_date"));
	cd_ds_message_state			= Bean.getDecodeParam(parameters.get("cd_ds_message_state"));
	basis_for_operation			= Bean.getDecodeParam(parameters.get("basis_for_operation"));
	title_ds_message			= Bean.getDecodeParam(parameters.get("title_ds_message"));
	id_email_profile			= Bean.getDecodeParam(parameters.get("id_email_profile"));
	attach_file					= Bean.getDecodeParam(parameters.get("attach_file"));
	is_archive					= Bean.getDecodeParam(parameters.get("is_archive"));
	id_club						= Bean.getDecodeParam(parameters.get("id_club"));
	cd_ds_message_send_state	= Bean.getDecodeParam(parameters.get("cd_ds_message_send_state"));
	id_ds_message_send			= Bean.getDecodeParam(parameters.get("id_ds_message_send"));

}

if (id==null || ("".equalsIgnoreCase(id))) id="empty";
if (action==null || ("".equalsIgnoreCase(action))) action="empty";
if (process==null || ("".equalsIgnoreCase(process))) process="empty";
if (type==null || ("".equalsIgnoreCase(type))) type="empty";

if (type.equalsIgnoreCase("general")) {
	if (process.equalsIgnoreCase("no"))
	/* вибираємо тип дії (добавити, видалити...)*/
		{
		   /*  --- Добавити запис --- */
	    	if (action.equalsIgnoreCase("add") || action.equalsIgnoreCase("add2")) {
	    		
	    		bcClubShortObject club = new bcClubShortObject(Bean.getCurrentClubID());
		        
	    		%>
				<%= Bean.getOperationTitle(
						Bean.messageXML.getfieldTransl("h_message_add", true),
						"Y",
						"Y") 
				%>
	        <script>
				var formData = new Array (
					new Array ('basis_for_operation', 'varchar2', 1),
					new Array ('name_email_profile', 'varchar2', 1),
					new Array ('name_club', 'varchar2', 1),
					new Array ('cd_ds_message_state', 'varchar2', 1),
					new Array ('begin_action_date', 'varchar2', 1),
					new Array ('is_archive', 'varchar2', 1)
				);
			</script>
	
			<%= Bean.getMessageLengthTextAreaInitialScript("text_ds_message", "length_message") %>
	
			<form action="../crm/dispatch/messages/email_messagesupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">
		        <input type="hidden" name="action" value="add">
		        <input type="hidden" name="process" value="yes">
				<input type="hidden" name="id" value="<%= id %>">
			<table <%=Bean.getTableDetailParam() %>>
			<tr>
				<td valign="top" rowspan="3"><%= Bean.messageXML.getfieldTransl("basis_for_operation", true) %></td><td valign="top" rowspan="3"><textarea name="basis_for_operation" cols="60" rows="3" class="inputfield"></textarea></td>
				<td><%= Bean.clubXML.getfieldTransl("club", true) %>
					<%= Bean.getGoToClubLink(club.getValue("ID_CLUB")) %>
				</td> 
				<td>
					<%=Bean.getWindowFindClub("club", club.getValue("ID_CLUB"), club.getValue("SNAME_CLUB"),"35") %>
				</td>
			</tr>
			<tr>
				<td valign="top"><%= Bean.messageXML.getfieldTransl("state_operation", true) %></td><td valign="top"><select name="cd_ds_message_state" class="inputfield"><%= Bean.getMeaningFromLookupNameOrderByNymberValueOptions("SEND_MESSAGE_STATE", "PREPARED", true) %></select></td>
			</tr>
			<tr>
				<td><%= Bean.messageXML.getfieldTransl("begin_action_date", true) %></td> <td><%=Bean.getCalendarInputField("begin_action_date", "", "10") %></td>
			</tr>
			<tr>
				<td><%= Bean.messageXML.getfieldTransl("email_profile", true) %></td> 
				<td>
					<%=Bean.getWindowEmailProfiles("email_profile", "", "50") %>
				</td>			
				<td><%= Bean.messageXML.getfieldTransl("end_action_date", false) %></td> <td><%=Bean.getCalendarInputField("end_action_date", "", "10") %></td>
			</tr>
			<tr>
				<td><%= Bean.messageXML.getfieldTransl("title_message", false) %></td> <td><input type="text" name="title_ds_message" size="63" value="" class="inputfield"> </td>
				<td><%= Bean.smsXML.getfieldTransl("is_archive", true) %> </td> <td align="left"><select name="is_archive" class="inputfield"><%= Bean.getMeaningFromLookupNameOptions("YES_NO", "N", true) %></select></td>
			</tr>
			<tr>
				<td valign="top"><%= Bean.messageXML.getfieldTransl("text_message", false) %></td><td valign="top" colspan="2"><textarea name="text_ds_message" id="text_ds_message" cols="120" rows="12" class="inputfield" <%=Bean.getMessageLengthTextAreaEvent("length_message") %>></textarea></td>
			</tr>
			<tr>
				<td valign="top"><%= Bean.messageXML.getfieldTransl("length_message", false) %></td><td valign="top"><input type="text" name="length_message" id="length_message" size="8" value="" readonly="readonly" class="inputfield-ro"></td>
			</tr>
	
			<tr>
				<td colspan="6" align="center">
					<%=Bean.getSubmitButtonAjax("../crm/dispatch/messages/email_messagesupdate.jsp") %>
					<%=Bean.getResetButton() %>
					<% if (action.equalsIgnoreCase("add")) { %>
						<%=Bean.getGoBackButton("../crm/dispatch/messages/email_messages.jsp") %>
					<% } else { %>
						<%=Bean.getGoBackButton("../crm/dispatch/messages/email_messagesspecs.jsp?id=" + id) %>
					<% } %>
				</td>
			</tr>
	
		</table>
		</form> 
		
		<%= Bean.getCalendarScript("begin_action_date", false) %>
		<%= Bean.getCalendarScript("end_action_date", false) %>
	
		        <%
	    	} else {
	    	    %> <%= Bean.getUnknownActionText(action) %><%
	    	}
	
	} else if (process.equalsIgnoreCase("yes"))	{
	    
		if (action.equalsIgnoreCase("add")) { 
			
			String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_DS_CLIENT.add_message(" +
				"?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";

			String[] pParam = new String [13];

			pParam[0] = "EMAIL";
			pParam[1] = "SEND";
			pParam[2] = title_ds_message;
			pParam[3] = text_ds_message;
			pParam[4] = cd_ds_message_state;
			pParam[5] = begin_action_date;
			pParam[6] = end_action_date;
			pParam[7] = basis_for_operation;
			pParam[8] = id_email_profile;
			pParam[9] = "";
			pParam[10] = is_archive;
			pParam[11] = id_club;
			pParam[12] = Bean.getDateFormat();
			
		 	%>
			<%= Bean.getCallResultParam("INSERT", callSQL, pParam, "../crm/dispatch/messages/email_messagesspecs.jsp?id=", "../crm/dispatch/messages/email_messages.jsp") %>
			<% 	
	
		} else if (action.equalsIgnoreCase("remove")) { 
			
			String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_DS_CLIENT.delete_message_for_type(?,?,?)}";

			String[] pParam = new String [2];

			pParam[0] = id;
			pParam[1] = "EMAIL";
	
		 	%>
			<%= Bean.getCallResultParam("DELETE", callSQL, pParam, "../crm/dispatch/messages/email_messages.jsp", "") %>
			<% 	
	
		} else if (action.equalsIgnoreCase("remove_file")) { 
			
			String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_DS_CLIENT.delete_message_file(?,?)}";

			String[] pParam = new String [1];

			pParam[0] = id;
	
		 	%>
			<%= Bean.getCallResultParam("UPDATE", callSQL, pParam, "../crm/dispatch/messages/email_messagesspecs.jsp?id=" + id, "") %>
			<% 	
	
		} else if (action.equalsIgnoreCase("remove_src")) { 
			
			String 	id_file		= Bean.getDecodeParam(parameters.get("id_file"));
			
			String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_DS_CLIENT.detach_file_from_email_message(?,?,?)}";

			String[] pParam = new String [2];

			pParam[0] = id;
			pParam[1] = id_file;
	
		 	%>
			<%= Bean.getCallResultParam("UPDATE", callSQL, pParam, "../crm/dispatch/messages/email_messagesspecs.jsp?id=" + id, "") %>
			<% 	
	
		} else if (action.equalsIgnoreCase("execute")) {
			
			String operation_type 	= Bean.getDecodeParam(parameters.get("operation_type"));
			String callSQL = "";
			ArrayList<String> id_value=new ArrayList<String>();		
	
			Object current_parameter;
				Set<String> keySet = parameters.keySet();
				Iterator<String> keySetIterator = keySet.iterator();
				String key = "";
		    	while(keySetIterator.hasNext()) {
					try{
						key = (String)keySetIterator.next();
						
						if(key.contains("chb")){
							id_value.add(key.substring(3));
							/*
							if(Bean.getDecodeParam(parameters.get(current_key) instanceof String[]){
								id_value=(String[])Bean.getDecodeParam(parameters.get(current_key);
							}else{
								id_value=new String[]{Bean.getDecodeParam(parameters.get(current_key)};
							}*/
						}
						
					}
					catch(Exception ex){
						Bean.writeException(
								"../crm/dispatch/messages/email_messagesupdate.jsp",
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
		    	pParam[1] = "EMAIL";
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
	   	    		"../crm/dispatch/messages/email_messages.jsp?operation_type=" + operation_type + "&specid=", 
	   	    		"../crm/dispatch/messages/email_messages.jsp?operation_type=" + operation_type + "&specid=", 
	   	    		Bean.form_messageXML.getfieldTransl("save_error", false)) %>
	   		<% 
	
		} else if (action.equalsIgnoreCase("set_state")) {
			String 	new_state		= Bean.getDecodeParam(parameters.get("new_state"));
			
			String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_DS_CLIENT.set_message_state(?,?,?,?)}";

			String[] pParam = new String [3];

			pParam[0] = id;
			pParam[1] = "EMAIL";
			pParam[2] = new_state;
	
			%>
			<%= Bean.getCallResultParam("UPDATE", callSQL, pParam, "../crm/dispatch/messages/email_messagesspecs.jsp?id=" + id, "") %>
			<% 		
	
		} else if (action.equalsIgnoreCase("edit")) { 
			
			boolean transfer_ok = true;
			String errorText = "";
			String file_doc = "";
			String src_doc = "";
			
			if(ServletFileUpload.isMultipartContent(request)){
	
				for(FileItem file:fileList){
					if(!file.isFormField()){
						String filename=file.getName();
						String fieldName = file.getFieldName();
						if (("Y".equalsIgnoreCase(attach_file) && "attached_file_name".equalsIgnoreCase(fieldName)) ||
								("N".equalsIgnoreCase(attach_file) && "message_file_name".equalsIgnoreCase(fieldName))) {
							
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
					
						
		   		}
			 } else {
				 LOGGER.error(type+" "+process+" "+action+ " File not found");
			 }
			if (transfer_ok) {
				
				String callSQL = "";
				if ("Y".equalsIgnoreCase(attach_file)) {
					callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_DS_CLIENT.attach_file_to_email_message(?,?,?,?)}";

					String[] pParam = new String [3];

					pParam[0] = id;
					pParam[1] = src_doc;
					pParam[2] = file_doc;
					
			 		%>
					<%= Bean.getCallResultParam("UPDATE", callSQL, pParam, "../crm/dispatch/messages/email_messagesspecs.jsp?id=" + id, "") %>
					<% 	
				} else {
					callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_DS_CLIENT.update_message(" + 
						"?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";

					String[] pParam = new String [13];

					pParam[0] = id;
					pParam[1] = title_ds_message;
					pParam[2] = text_ds_message;
					pParam[3] = cd_ds_message_state;
					pParam[4] = begin_action_date;
					pParam[5] = end_action_date;
					pParam[6] = basis_for_operation;
					pParam[7] = id_email_profile;
					pParam[8] = "";
					pParam[9] = is_archive;
					pParam[10] = src_doc;
					pParam[11] = file_doc;
					pParam[12] = Bean.getDateFormat();
					
			 		%>
					<%= Bean.getCallResultParam("UPDATE", callSQL, pParam, "../crm/dispatch/messages/email_messagesspecs.jsp?id=" + id, "") %>
					<% 	
				}
			} else {%>
				<%=Bean.showCallResult("", "1", errorText, "../crm/dispatch/messages/email_messagesspecs.jsp?id=" + id, "", Bean.documentXML.getfieldTransl("h_upload_file_error", false)) %>
			<%}
	
		} else { %> 
			<%= Bean.getUnknownActionText(action) %><% 
		}
	
	} else {
	    %> <%= Bean.getUnknownProcessText(process) %> <%
	}
} else if (type.equalsIgnoreCase("receiver")) {
	String entry_type	= Bean.getDecodeParam(parameters.get("entry_type"));
	
	if (process.equalsIgnoreCase("no"))
		/* вибираємо тип дії (добавити, видалити...)*/
			{
			   /*  --- Добавити запис --- */
		    	if (action.equalsIgnoreCase("add")) {%>
		    		
		    	<%= Bean.getOperationTitle(
						Bean.messageXML.getfieldTransl("h_add_message_receiver", false),
						"Y",
						"Y") 
				%>
				<script>
					var formData = new Array (
							new Array ('action', 'varchar2', 1)
					);
				</script>
		        <form action="../crm/dispatch/messages/email_messagesupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData);">
				        <input type="hidden" name="action" value="adddet">
				        <input type="hidden" name="process" value="no">
				        <input type="hidden" name="type" value="receiver">
				        <input type="hidden" name="id" value="<%=id %>">
				        <input type="hidden" name="action_prev" value="<%=action %>">


				<table <%=Bean.getTableDetailParam() %>>
					<tr>
						<td><%= Bean.messageXML.getfieldTransl("ID_MESSAGE", false) %></td> <td><input type="text" name="id_ds_message" size="20" value="<%= id %>" readonly="readonly" class="inputfield-ro"> </td>
						<td colspan="2">&nbsp;</td>
					</tr>
					<tr>
						<td><%= Bean.messageXML.getfieldTransl("cd_ds_receiver_dispatch_kind", true) %></td><td><%= Bean.getDSDispatchKindRadio("cd_dispatch_kind","CLIENT") %></td>			
						<td colspan="2">&nbsp;</td>
					</tr>
					<tr>
						<td colspan="4" align="center">
							<%=Bean.getSubmitButtonAjax("../crm/dispatch/messages/email_messagesupdate.jsp") %>
							<%=Bean.getResetButton() %>
							<%=Bean.getGoBackButton("../crm/dispatch/messages/email_messagesspecs.jsp?id=" + id) %>
						</td>
					</tr>

				</table>

				</form>

			<%
			} else if (action.equalsIgnoreCase("adddet")) { 
				String 
					action_prev 				= Bean.getDecodeParam(parameters.get("action_prev")),
					cd_dispatch_kind			= Bean.getDecodeParam(parameters.get("cd_dispatch_kind"));
			%>
			
				<%= Bean.getOperationTitle(
						Bean.messageXML.getfieldTransl("h_add_message_receiver", false),
						"Y",
						"Y") 
				%>
				<script>
					var formData = new Array (
							new Array ('cd_ds_message_state', 'varchar2', 1),
							new Array ('is_archive', 'varchar2', 1)
					);
				</script>
			    <form action="../crm/dispatch/messages/smsupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData);">
				        <input type="hidden" name="action" value="add">
				        <input type="hidden" name="process" value="yes">
				        <input type="hidden" name="type" value="receiver">
				        <input type="hidden" name="cd_dispatch_kind" value="<%=cd_dispatch_kind %>">
				        <input type="hidden" name="id" value="<%=id %>">
			
				<table <%=Bean.getTableDetailParam() %>>
					<tr>
						<td><%= Bean.messageXML.getfieldTransl("cd_ds_receiver_dispatch_kind", false) %> </td> <td align="left"><input type="text" name="name_dispatch_kind" size="35" value="<%=Bean.getDispatchKindName(cd_dispatch_kind) %>" readonly="readonly" class="inputfield-ro"></td>
						<td colspan="2">&nbsp;</td>
					</tr>
						<% if ("CLIENT".equalsIgnoreCase(cd_dispatch_kind)) { %>
					<tr>
						<td><%= Bean.messageXML.getfieldTransl("full_name_nat_prs", false) %></td>
			            <td>
							<%=Bean.getWindowFindNatPrs("sender_receiver", "", "35") %>
						</td>
						<td colspan="2">&nbsp;</td>
					</tr>
						<% } else if ("PARTNER".equalsIgnoreCase(cd_dispatch_kind)) { %>
					<tr>
						<td><%= Bean.messageXML.getfieldTransl("id_contact_prs", false) %></td>
			            <td>
							<%=Bean.getWindowContactPersons("sender_receiver", "", "", "", "35") %>
						</td>
						<td colspan="2">&nbsp;</td>
					</tr>
						<% } else if ("SYSTEM".equalsIgnoreCase(cd_dispatch_kind)) { %>
					<tr>
						<td><%= Bean.messageXML.getfieldTransl("id_user", false) %></td>
			            <td>
							<%=Bean.getWindowFindUser("sender_receiver", "", "", "35") %>
						</td>
						<td colspan="2">&nbsp;</td>
					</tr>
						<% } else if ("TRAINING".equalsIgnoreCase(cd_dispatch_kind)) { %>
					<tr>
						<td><%= Bean.messageXML.getfieldTransl("id_tr_person", false) %></td>
			            <td>
							<%=Bean.getWindowFindTrainingPerson("sender_receiver", "", "", "35") %>
						</td>
						<td colspan="2">&nbsp;</td>
					</tr>
						<% } %>
					<tr>
						<td><%= Bean.messageXML.getfieldTransl("email", false) %> </td> <td align="left"><input type="text" name="receiver_email" size="35" value="" class="inputfield"></td>
						<td colspan="2">&nbsp;</td>
					</tr>
					<tr>
						<td valign="top"><%= Bean.messageXML.getfieldTransl("cd_ds_message_send_state", true) %></td><td valign="top"><select name="cd_ds_message_state" class="inputfield"><%= Bean.getMeaningFromLookupNameOrderByNymberValueOptions("SEND_MESSAGE_STATE", "NEW", true) %></select></td>
						<td colspan="2">&nbsp;</td>
					</tr>
					<tr>
						<td><%= Bean.messageXML.getfieldTransl("is_archive", true) %> </td> <td align="left"><select name="is_archive" class="inputfield"><%= Bean.getMeaningFromLookupNameOptions("YES_NO", "N", true) %></select></td>
						<td colspan="2">&nbsp;</td>
					</tr>
					<tr>
						<td colspan="4" align="center">
							<%=Bean.getSubmitButtonAjax("../crm/dispatch/messages/email_messagesupdate.jsp") %>
							<%=Bean.getResetButton() %>
							<%=Bean.getGoBackButton("../crm/dispatch/messages/email_messagesupdate.jsp?id=" + id + "&type=receiver&process=no&action=" + action_prev) %>
						</td>
					</tr>
			
				</table>
			
				</form> 
			
			        <%
		    	} else {
		    	    %> <%= Bean.getUnknownActionText(action) %><%
		    	}
		
		} else if (process.equalsIgnoreCase("yes"))	{
			
			String
				cd_dispatch_kind		= Bean.getDecodeParam(parameters.get("cd_dispatch_kind")),
				id_sender_receiver		= Bean.getDecodeParam(parameters.get("id_sender_receiver")),
				receiver_email			= Bean.getDecodeParam(parameters.get("receiver_email")),
		    	id_ds_message_receiver	= Bean.getDecodeParam(parameters.get("id_ds_message_receiver"));
		    
			if (action.equalsIgnoreCase("add")) { 
				
				String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_DS_CLIENT.add_message_receiver(" + 
					"?,?,?,?,?,?,?,?)}";

				String[] pParam = new String [6];

				pParam[0] = id;
				pParam[1] = cd_dispatch_kind;
				pParam[2] = id_sender_receiver;
				pParam[3] = receiver_email;
				pParam[4] = cd_ds_message_state;
				pParam[5] = is_archive;
				
			 	%>
				<%= Bean.getCallResultParam("INSERT", callSQL, pParam, "../crm/dispatch/messages/email_messagereceiverspecs.jsp?id_ds_message=" + id + "&id=", "") %>
				<% 	
		
			} else if (action.equalsIgnoreCase("remove")) { 
				
				String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_DS_CLIENT.delete_message_receiver(?,?)}";

				String[] pParam = new String [1];

				pParam[0] = id_ds_message_receiver;
		
			 	%>
				<%= Bean.getCallResultParam("DELETE", callSQL, pParam, "../crm/dispatch/messages/email_messagesspecs.jsp?id=" + id, "") %>
				<% 	
		
			} else if (action.equalsIgnoreCase("edit")) { 
				
				String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_DS_CLIENT.update_message_receiver(?,?,?,?)}";

				String[] pParam = new String [3];

				pParam[0] = id_ds_message_receiver;
				pParam[1] = cd_ds_message_state;
				pParam[2] = is_archive;
				
			 	%>
				<% if ("RECEIVER".equalsIgnoreCase(entry_type)) { %>
				<%= Bean.getCallResultParam("UPDATE", callSQL, pParam, "../crm/dispatch/messages/email_messagereceiverspecs.jsp?id=" + id_ds_message_receiver, "../crm/dispatch/messages/email_messagereceiverspecs.jsp?id=" + id_ds_message_receiver) %>
				<% } else { %>
				<%= Bean.getCallResultParam("UPDATE", callSQL, pParam, "../crm/dispatch/messages/email_messagesspecs.jsp?id=" + id, "../crm/dispatch/messages/email_messagesspecs.jsp?id=" + id) %>
				<% } %>
				<% 	
		
			} else { %> 
				<%= Bean.getUnknownActionText(action) %><% 
			}
		
		} else {
		    %> <%= Bean.getUnknownProcessText(process) %> <%
		}
	
} else if (type.equalsIgnoreCase("send")) {
	if (process.equalsIgnoreCase("yes"))	{
			
			if (action.equalsIgnoreCase("edit")) { 
				
				String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_DS_CLIENT.update_message_send(?,?,?)}";

				String[] pParam = new String [2];

				pParam[0] = id_ds_message_send;
				pParam[1] = cd_ds_message_send_state;
				
			 	%>
				<%= Bean.getCallResultParam("UPDATE", callSQL, pParam, "../crm/dispatch/messages/email_messagesendspecs.jsp?id=" + id_ds_message_send, "") %>
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
