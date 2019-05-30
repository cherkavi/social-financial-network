<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" 
import="java.io.*,java.util.List,javax.servlet.*,javax.servlet.http.*,org.apache.commons.fileupload.*, org.apache.commons.fileupload.servlet.*, org.apache.commons.fileupload.disk.*"%>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%= Bean.getLogOutScript(request) %>

<%@ page import="bc.objects.bcDocumentObject"%>
<%@ page import="java.net.URLEncoder"%>
<%@ page import="bc.objects.bcJurPrsObject"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="bc.objects.bcClubShortObject"%>
<%@ page import="org.apache.log4j.Logger" %>

<%@page import="java.util.ArrayList"%>
<%@page import="bc.objects.bcLGObject"%><html>
<head>
	<%= Bean.getMetaContent() %>
	<%= Bean.getBottomFrameCSS() %>
</head>

<body>
<%
Logger LOGGER = Logger.getLogger( "../crm/clients/yurpersondocupdate.jsp" );

HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String pageFormName = "CLIENTS_YURPERSONS";

Bean.setJspPageForTabName(pageFormName);

request.setCharacterEncoding("UTF-8");

String doc_dir = Bean.getDirectorySystemParamValue("UPLOADED_FILES_DIR") + Bean.getDocumentsSubDirectory() + Bean.getDocumentsPrefix() + "_";

String id		= Bean.getDecodeParam(parameters.get("id")); 
String type     = Bean.getDecodeParam(parameters.get("type"));
String action	= Bean.getDecodeParam(parameters.get("action")); 
String process	= Bean.getDecodeParam(parameters.get("process"));

String id_doc 				= Bean.getDecodeParam(parameters.get("id_doc"));
String cd_doc_type 			= Bean.getDecodeParam(parameters.get("cd_doc_type"));
String cd_doc_state 		= Bean.getDecodeParam(parameters.get("cd_doc_state"));
String number_doc 			= Bean.getDecodeParam(parameters.get("number_doc"));
String date_doc 			= Bean.getDecodeParam(parameters.get("date_doc"));
String date_begin_doc 		= Bean.getDecodeParam(parameters.get("date_begin_doc"));
String date_end_doc 		= Bean.getDecodeParam(parameters.get("date_end_doc"));
String name_doc 			= Bean.getDecodeParam(parameters.get("name_doc"));
String desc_doc 			= Bean.getDecodeParam(parameters.get("desc_doc"));
String id_jur_prs_part1 	= Bean.getDecodeParam(parameters.get("id_party1"));
String id_jur_prs_part2		= Bean.getDecodeParam(parameters.get("id_party2"));
String id_jur_prs_part3		= Bean.getDecodeParam(parameters.get("id_party3"));
String cd_club_rel_type		= Bean.getDecodeParam(parameters.get("cd_club_rel_type"));
String id_lg_record			= Bean.getDecodeParam(parameters.get("id_lg_record"));
String id_club				= Bean.getDecodeParam(parameters.get("id_club"));


if (id==null || ("".equalsIgnoreCase(id))) id="empty";
if (type==null || ("".equalsIgnoreCase(type))) type="general";
if (action==null || ("".equalsIgnoreCase(action))) action="empty";
if (process==null || ("".equalsIgnoreCase(process))) process="empty";

if (type.equalsIgnoreCase("doc")) {
	
    String[] results = new String[2];
	
	if (process.equalsIgnoreCase("no")) {
		if (action.equalsIgnoreCase("add")) {

			bcClubShortObject club = new bcClubShortObject(Bean.getCurrentClubID());
			
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

		<%= Bean.getOperationTitleShort(
				"",
				Bean.documentXML.getfieldTransl("l_add_doc", false),
				"Y",
				"Y") 
		%>
    <form action="../crm/clients/yurpersondocupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">
        <input type="hidden" name="type" value="doc">
        <input type="hidden" name="action" value="add">
        <input type="hidden" name="process" value="yes">
        <input type="hidden" name="id" value="<%=id %>">
	<table <%=Bean.getTableDetailParam() %>>
	    <tr>
			<td><%= Bean.documentXML.getfieldTransl("cd_doc_type", true) %></td> <td><select name="cd_doc_type" class="inputfield" > <%= Bean.getDocTypeOptions("", false) %></select></td>
 		    <td><%= Bean.clubXML.getfieldTransl("club", true) %>
				<%= Bean.getGoToClubLink(club.getValue("ID_CLUB")) %>
			</td>
		  	<td>
				<%=Bean.getWindowFindClub("club", club.getValue("ID_CLUB"), club.getValue("SNAME_CLUB"), "25") %>
	  		</td>
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
			<td rowspan="4"><%= Bean.documentXML.getfieldTransl("desc_doc", false) %></td><td rowspan="4"><textarea name="desc_doc" cols="60" rows="4" class="inputfield"></textarea></td>
			<td class="top_line"><%= Bean.documentXML.getfieldTransl("club_rel_type", false) %></td> <td class="top_line"><select name="cd_club_rel_type" class="inputfield" > <%= Bean.getClubRelTypeOptions("", true) %></select></td>
		</tr>
	    <tr>
			<td><b><%= Bean.documentXML.getfieldTransl("name_party1", true) %></b></td>
			<td>
				<%=Bean.getWindowFindJurPrs("party1", id, "ALL", "50") %>
			</td>			
		</tr>
	    <tr>
			<td><%= Bean.documentXML.getfieldTransl("name_party2", false) %></td>
			<td>
				<%=Bean.getWindowFindJurPrs("party2", "", "ALL", "50") %>
			</td>			
		</tr>
		<tr>
			<td><%= Bean.documentXML.getfieldTransl("name_party3", false) %></td>
			<td>
				<%=Bean.getWindowFindJurPrs("party3", "", "ALL", "50") %>
			</td>			
		</tr>
		<tr>
			<td colspan="6" align="center">
				<%=Bean.getSubmitButtonAjax("../crm/clients/yurpersondocupdate.jsp", "submit", "updateForm", "div_data_detail") %>
				<%=Bean.getResetButton() %>
				<%=Bean.getGoBackButton("../crm/clients/yurpersonspecs.jsp?id=" + id) %>
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

		<%= Bean.getOperationTitleShort(
				"",
				Bean.documentXML.getfieldTransl("l_edit_doc", false),
				"Y",
				"Y") 
		%>
    <form action="../crm/clients/yurpersondocupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">
        <input type="hidden" name="type" value="doc">
        <input type="hidden" name="action" value="edit">
        <input type="hidden" name="process" value="yes">
        <input type="hidden" name="id" value="<%=id %>">
        <input type="hidden" name="id_doc" value="<%=doc.getValue("ID_DOC") %>">
	<table <%=Bean.getTableDetailParam() %>>
	    <tr>
			<td><%= Bean.documentXML.getfieldTransl("cd_doc_type", true) %></td> <td><select name="cd_doc_type" class="inputfield" > <%= Bean.getDocTypeOptions(doc.getValue("CD_DOC_TYPE"), false) %></select></td>
			<td><%= Bean.clubXML.getfieldTransl("club", false) %>
					<%= Bean.getGoToClubLink(doc.getValue("ID_CLUB")) %>
			</td><td><input type="text" name="name_club" size="50" value="<%= Bean.getClubShortName(doc.getValue("ID_CLUB")) %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
    	<tr>
			<td><%= Bean.documentXML.getfieldTransl("number_doc", true) %></td> <td><input type="text" name="number_doc" size="25" value="<%=doc.getValue("NUMBER_DOC") %>" class="inputfield"></td>
			<td><%= Bean.documentXML.getfieldTransl("cd_doc_state", true) %></td> <td><select name="cd_doc_state" class="inputfield" > <%= Bean.getDocStateOptions(doc.getValue("CD_DOC_STATE"), true) %></select></td>
		</tr>
    	<tr>
			<td><%= Bean.documentXML.getfieldTransl("date_doc", true) %></td> <td><%=Bean.getCalendarInputField("date_doc", doc.getValue("DATE_DOC_FRMT"), "10") %></td>
			<td><%= Bean.documentXML.getfieldTransl("date_begin_doc", false) %></td> <td><%=Bean.getCalendarInputField("date_begin_doc", doc.getValue("DATE_BEGIN_DOC_FRMT"), "10") %></td>
		</tr>
    	<tr>
			<td><%= Bean.documentXML.getfieldTransl("name_doc", true) %></td> <td><input type="text" name="name_doc" size="64" value="<%=doc.getValue("NAME_DOC") %>" class="inputfield"></td>
			<td><%= Bean.documentXML.getfieldTransl("date_end_doc", false) %></td> <td><%=Bean.getCalendarInputField("date_end_doc", doc.getValue("DATE_END_DOC_FRMT"), "10") %></td>
		</tr>
    	<tr>
			<td rowspan="4"><%= Bean.documentXML.getfieldTransl("desc_doc", false) %></td><td rowspan="4"><textarea name="desc_doc" cols="60" rows="4" class="inputfield"><%=doc.getValue("DESC_DOC") %></textarea></td>
			<% if (!(doc.getValue("ID_LG_RECORD")==null || "".equalsIgnoreCase(doc.getValue("ID_LG_RECORD")))) {
				bcLGObject logistic = new bcLGObject(doc.getValue("ID_LG_RECORD"));
			%>
			<td class="top_line">
				<input type="hidden" name="id_lg_record" value="<%=doc.getValue("ID_LG_RECORD") %>">
				<%= Bean.getLGTitle(logistic.getValue("CD_LG_TYPE")) %>
				<%= Bean.getLGHyperLink(logistic.getValue("CD_LG_TYPE"), doc.getValue("ID_LG_RECORD")) %>
			</td> <td class="top_line"><input type="text" name="operation_name" size="55" value="<%=logistic.getValue("OPERATION_NAME") %>" readonly="readonly" class="inputfield-ro">
			</td>
			<% } else { %>
			<td class="top_line"><%= Bean.documentXML.getfieldTransl("club_rel_type", false) %></td> <td class="top_line"><select name="cd_club_rel_type" class="inputfield" > <%= Bean.getClubRelTypeOptions(doc.getValue("CD_CLUB_REL_TYPE"), true) %></select></td>
			<% } %>
		</tr>
    	<tr>
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
		<%=	Bean.getIdCreationAndMoficationRecordFields(
				doc.getValue("ID_DOC"),
				doc.getValue(Bean.getCreationDateFieldName()),
				doc.getValue("CREATED_BY"),
				doc.getValue(Bean.getLastUpdateDateFieldName()),
				doc.getValue("LAST_UPDATE_BY")
		) %>
    	<tr>
			<td colspan="2">&nbsp;</td>
		</tr>
		<tr>
			<td colspan="6" align="center">
				<%=Bean.getSubmitButtonAjax("../crm/clients/yurpersondocupdate.jsp", "submit", "updateForm", "div_data_detail") %>
				<%=Bean.getResetButton() %>
				<%=Bean.getGoBackButton("../crm/clients/yurpersonspecs.jsp?id=" + id) %>
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
			ArrayList<String> pParam = new ArrayList<String>();
										 		 	    	      				
			pParam.add(cd_doc_type);
			pParam.add(cd_doc_state);
			pParam.add(number_doc);
			pParam.add(date_doc);
			pParam.add(date_begin_doc);
			pParam.add(date_end_doc);
			pParam.add(name_doc);
			pParam.add(desc_doc);
			pParam.add(id_club);
			pParam.add(id_jur_prs_part1);
			pParam.add(id_jur_prs_part2);
			pParam.add(id_jur_prs_part3);
			pParam.add(cd_club_rel_type);
			pParam.add("");
			pParam.add(Bean.getDateFormat());
		 	%>
			<%= Bean.executeInsertFunction("PACK$DOC_UI.add_doc", pParam, "../crm/clients/yurpersonspecs.jsp?id=" + id + "&id_doc=", "") %>
			<% 	
		
		} else if (action.equalsIgnoreCase("remove")) {
		
			ArrayList<String> pParam = new ArrayList<String>();
			
			pParam.add(id_doc);		
		 	%>
			<%= Bean.executeDeleteFunction("PACK$DOC_UI.delete_doc", pParam, "../crm/clients/yurpersonspecs.jsp?id=" + id, "") %>
			<% 	
	 	
		} else if (action.equalsIgnoreCase("edit")) {
			ArrayList<String> pParam = new ArrayList<String>();
			
			pParam.add(id_doc);
			pParam.add(cd_doc_type);
			pParam.add(cd_doc_state);
			pParam.add(number_doc);
			pParam.add(date_doc);
			pParam.add(date_begin_doc);
			pParam.add(date_end_doc);
			pParam.add(name_doc);
			pParam.add(desc_doc);
			pParam.add(id_jur_prs_part1);
			pParam.add(id_jur_prs_part2);
			pParam.add(id_jur_prs_part3);
			pParam.add(cd_club_rel_type);
			pParam.add(id_lg_record);
			pParam.add(Bean.getDateFormat());
				
			%>
			<%= Bean.executeUpdateFunction("PACK$DOC_UI.update_doc", pParam, "../crm/clients/yurpersonspecs.jsp?id=" + id, "") %>
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
