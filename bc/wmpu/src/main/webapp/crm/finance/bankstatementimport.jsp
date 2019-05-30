<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" 
import="java.io.*,java.util.List,javax.servlet.*,javax.servlet.http.*,org.apache.commons.fileupload.*, org.apache.commons.fileupload.servlet.*, org.apache.commons.fileupload.disk.*"%>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%= Bean.getLogOutScript(request) %>

<%@ page import="java.net.URLEncoder"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="bc.objects.bcBankStatementLineIntObject"%>
<%@ page import="bc.objects.bcClubShortObject"%><html>
<%@ page import="org.apache.log4j.Logger" %>

<head>
	<%= Bean.getMetaContent() %>
	<%= Bean.getBottomFrameCSS() %>
</head>

<body>
<%
Logger LOGGER = Logger.getLogger( "../crm/finance/bankstatementimport.jsp" );

HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String pageFormName = "FINANCE_BANKSTATEMENT";

Bean.setJspPageForTabName(pageFormName);

request.setCharacterEncoding("UTF-8");
String id		= ""; 
String type     = "";
String action	= ""; 
String process	= "";


String 
	id_club         = "",
	src_doc 		= "",
	exp_format 		= "",
	file_charset	= "",
	debug 			= "";

List<FileItem> fileList = null;

if(ServletFileUpload.isMultipartContent(request)){
  	FileItemFactory factory = new DiskFileItemFactory();
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
	  			
	  			if ("id_club".equalsIgnoreCase(file.getFieldName())) {
	  				id_club = Bean.prepareStringToSQL(Bean.decodeUtf(file.getString()));
	  			}
	  			if ("src_doc".equalsIgnoreCase(file.getFieldName())) {
	  				src_doc = Bean.prepareStringToSQL(Bean.decodeUtf(file.getString()));
	  			}
	  			if ("exp_format".equalsIgnoreCase(file.getFieldName())) {
	  				exp_format = Bean.prepareStringToSQL(Bean.decodeUtf(file.getString()));
	  			}
	  			if ("file_charset".equalsIgnoreCase(file.getFieldName())) {
	  				file_charset = Bean.prepareStringToSQL(Bean.decodeUtf(file.getString()));
	  			}
	  			if ("debug".equalsIgnoreCase(file.getFieldName())) {
	  				debug = Bean.prepareStringToSQL(Bean.decodeUtf(file.getString()));
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
	
	id_club 		= Bean.getDecodeParam(parameters.get("id_club"));
	src_doc 		= Bean.getDecodeParam(parameters.get("src_doc"));
	exp_format 		= Bean.getDecodeParam(parameters.get("exp_format"));
	file_charset	= Bean.getDecodeParam(parameters.get("file_charset"));
	debug 			= Bean.getDecodeParam(parameters.get("debug"));

}

if (id==null || ("".equalsIgnoreCase(id))) id="empty";
if (type==null || ("".equalsIgnoreCase(type))) type="empty";
if (action==null || ("".equalsIgnoreCase(action))) action="empty";
if (process==null || ("".equalsIgnoreCase(process))) process="empty";

if (type.equalsIgnoreCase("general")) {
	
    String[] results = new String[2];
	
	if (process.equalsIgnoreCase("no")) {
		if (action.equalsIgnoreCase("load_from_file")) {

			bcClubShortObject club = new bcClubShortObject(Bean.getCurrentClubID());
			
			%>
			<script>
				var formData = new Array (
					new Array ('name_club', 'varchar2', 1),
					new Array ('src_doc', 'varchar2', 1),
					new Array ('exp_format', 'varchar2', 1),
					new Array ('file_charset', 'varchar2', 1),
					new Array ('debug', 'varchar2', 1)
				);
			</script>

		<%= Bean.getOperationTitle(
				Bean.bank_statementXML.getfieldTransl("h_load_from_file", false),
				"Y",
				"N") 
		%>
    <form action="../crm/finance/bankstatementimport.jsp" name="updateForm" id="updateForm" enctype="multipart/form-data" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">
        <input type="hidden" name="type" value="general">
        <input type="hidden" name="action" value="load_from_file">
        <input type="hidden" name="process" value="yes">
        <input type="hidden" name="id" value="<%=id %>">
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
	          <font size="2"><%= Bean.exp_fileXML.getfieldTransl("name_file", true) %>:&nbsp;</font>
	       </td>
	       <td align="left"> 
				<input type="file" name="src_doc" size="50" value="" class="inputfield">
	       </td>
	    </tr>
	    <tr>   
	       <td align="left"> 
	          <font size="2"><%= Bean.exp_fileXML.getfieldTransl("file_type", true) %></font>
	       </td>
	       <td align="left"> 
	          <select name="exp_format" class="inputfield"><%= Bean.getMeaningFromLookupNameOptions("CLIENT_BANK_INTERCHANGE_FORMAT", Bean.getUIUserParam("BANK_STATEMENT_LOAD_FILE_FORMAT"), false) %></select>
	       </td>
	    </tr>
	    <tr>   
	       <td align="left"> 
	          <font size="2"><%= Bean.exp_fileXML.getfieldTransl("file_charset", true) %></font>
	       </td>
	       <td align="left"> 
	          <select name="file_charset" class="inputfield"><%= Bean.getMeaningFromLookupNameOptions("FILES_CHARSET", Bean.getUIUserParam("BANK_STATEMENT_LOAD_FILE_CHARSET"), false) %></select>
	       </td>
	    </tr>
	    <tr>
	       <td align="left"> 
	          <font size="2"><%= Bean.postingXML.getfieldTransl("debug", true) %></font>
	       </td>
	       <td align="left"><select name="debug" class="inputfield"><%= Bean.getMeaningFromLookupNameOptions("YES_NO", Bean.getUIUserParam("BANK_STATEMENT_LOAD_DEBUG"), false) %></select></td>
	    </tr>
		<tr>
			<td colspan="6" align="center">
				<%=Bean.getSubmitButtonMultiPart("../crm/finance/bankstatementimport.jsp", "submit", "updateForm") %>
				<%=Bean.getResetButton() %>
				<%=Bean.getGoBackButton("../crm/finance/bankstatement.jsp") %>
			</td>
		</tr>
	</table>
	</form>

		<% 
		} else if (action.equalsIgnoreCase("import")) {

			%>
			<script>
				var formData = new Array (
					new Array ('debug', 'varchar2', 1)
				);
			</script>

		<%= Bean.getOperationTitle(
				Bean.bank_statementXML.getfieldTransl("h_load_from_file", false),
				"Y",
				"N") 
		%>
    <form action="../crm/finance/bankstatementimport.jsp" name="updateForm" id="updateForm" enctype="multipart/form-data" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">
        <input type="hidden" name="type" value="general">
        <input type="hidden" name="action" value="import">
        <input type="hidden" name="process" value="yes">
        <input type="hidden" name="id" value="<%=id %>">
	<table <%=Bean.getTableDetailParam() %>>
	    <tr>   
		       <td align="left"> 
		          <font size="2"><%= Bean.bank_statementXML.getfieldTransl("id_bank_statement", false) %>:&nbsp;</font>
		       </td>
		       <td align="left"> 
					<input type="text" name="id_bank_statement" size="15" value="<%= id %>" readonly="readonly" class="inputfield-ro">
		       </td>
		    </tr>
		    <tr>
		       <td align="left"> 
		          <font size="2"><%= Bean.postingXML.getfieldTransl("debug", true) %></font>
		       </td>
		       <td align="left"><select name="debug" class="inputfield"><%= Bean.getMeaningFromLookupNameOptions("YES_NO", Bean.getUIUserParam("BANK_STATEMENT_IMPORT_DEBUG"), false) %></select></td>
		    </tr>
		<tr>
			<td colspan="6" align="center">
				<%=Bean.getSubmitButtonMultiPart("../crm/finance/bankstatementimport.jsp", "submit", "updateForm") %>
				<%=Bean.getResetButton() %>
				<%=Bean.getGoBackButton("../crm/finance/bankstatement.jsp") %>
			</td>
		</tr>
	</table>
	</form>

		<% 
		} else { %> 
			<%= Bean.getUnknownActionText(action) %><%
		}
	} else if (process.equalsIgnoreCase("yes")) {
		if (action.equalsIgnoreCase("load_from_file")) {

			String doc_dir = Bean.getDirectorySystemParamValue("UPLOADED_FILES_DIR") + Bean.getBankStatementsSubDirectory(id_club) + Bean.getDocumentsPrefix() + "_";

			boolean transfer_ok = true;
			String errorText = "";
			String file_doc = "";
			
			if(ServletFileUpload.isMultipartContent(request)){
	
				for(FileItem file:fileList){
					if(!file.isFormField()){
						String filename=file.getName();
						LOGGER.debug(process+" "+action+" filename="+filename);
						String shortname="";
						if (filename.contains("\\")) {
							shortname=filename.substring(filename.lastIndexOf("\\")+1,filename.length());
						} else if (filename.contains("/")) {
							shortname=filename.substring(filename.lastIndexOf("/")+1,filename.length());
						} else {
							shortname = filename;
						}
			  			
						LOGGER.debug(process+" "+action+" shortname="+shortname);
			  			
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
				 LOGGER.error(process+" "+action+" ERROR! File not found");
			 }

			if (transfer_ok) {
				String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_BANK_STATEMENT.load_from_file(?,?,?,?,?,?,?,?)}";

				String[] pParam = new String [6];

				pParam[0] = id_club;
				pParam[1] = src_doc;
				pParam[2] = Bean.getDocumentsPrefix() + "_" + src_doc;
				pParam[3] = exp_format;
				pParam[4] = file_charset;
				pParam[5] = debug;
	
		 		%>
				<%= Bean.getCallResultParam("INSERT", callSQL, pParam, "../crm/finance/bankstatementspecs.jsp?id=" , "../crm/finance/bankstatement.jsp") %>
				<% 	
			} else {%>
				<%=Bean.showCallResult("", "1", errorText, "../crm/finance/bankstatement.jsp", "", Bean.documentXML.getfieldTransl("h_upload_file_error", false)) %>
			<%}
		
		} else if (action.equalsIgnoreCase("import")) {
		
			String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_BANK_STATEMENT.run_bank_statement_import(?,?,?,?)}";

			String[] pParam = new String [2];

			pParam[0] = id;
			pParam[1] = debug;

			%>
			<%= Bean.getCallResultParam("RUN", callSQL, pParam, "../crm/finance/bankstatementspecs.jsp?id=" + id + "&id_report=", "../crm/finance/bankstatementspecs.jsp?id=" + id) %>
			<% 	
	 	

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
