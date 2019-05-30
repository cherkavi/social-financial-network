	<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" 
import="java.io.*,java.util.List,javax.servlet.*,javax.servlet.http.*,org.apache.commons.fileupload.*, org.apache.commons.fileupload.servlet.*, org.apache.commons.fileupload.disk.*, java.io.*"%>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />
<%@ page import="bc.objects.bcDocumentObject"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="bc.objects.bcDocumentNeededObject"%>
<%@ page import="bc.objects.bcClubShortObject"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.net.URLEncoder"%>
<%@page import="bc.objects.bcDocumentFileObject"%>
<%@page import="bc.objects.bcDocumentBankAccountObject"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.Set"%>
<%@page import="java.util.Iterator"%>
<%@page import="bc.objects.bcLGObject"%><html>
<%@ page import="org.apache.log4j.Logger" %>


	<script type="text/javascript" src="../js/frame_emulator.js" > </script>
<%= Bean.getLogOutScript(request) %>

<head>
	<%= Bean.getMetaContent() %>
	<%= Bean.getBottomFrameCSS() %>

</head>

<body>
<%
Logger LOGGER = Logger.getLogger( "../crm/club/documentupdate.jsp" );
HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String pageFormName = "CLUB_DOCUMENTS";

Bean.setJspPageForTabName(pageFormName);

request.setCharacterEncoding("UTF-8");

String id_doc		= Bean.getDecodeParamPrepare(parameters.get("id_doc")); 
String type     	= Bean.getDecodeParamPrepare(parameters.get("type"));
String action		= Bean.getDecodeParamPrepare(parameters.get("action")); 
String process		= Bean.getDecodeParamPrepare(parameters.get("process"));
String id_club		= Bean.getDecodeParamPrepare(parameters.get("id_club"));
String id_jur_prs	= Bean.getDecodeParamPrepare(parameters.get("id_jur_prs"));
String back_type	= Bean.getDecodeParamPrepare(parameters.get("back_type"));

String updateLink = "../crm/club/documentupdate.jsp";
String backLink = "";
String generalLink = "";
back_type = Bean.isEmpty(back_type)?"DOCUMENT":back_type;
System.out.println("back_type="+back_type);
if ("DOCUMENT".equalsIgnoreCase(back_type)) {
	generalLink = "../crm/club/documents.jsp?";
	if (action.equalsIgnoreCase("add")) {
		backLink = "../crm/club/documents.jsp?";
	} else {
		backLink = "../crm/club/documentspecs.jsp?id="+id_doc;
	}
} else if ("PARTNER".equalsIgnoreCase(back_type)) {
	generalLink = "../crm/clients/yurpersonspecs.jsp?id=" + id_jur_prs;
	backLink = "../crm/clients/yurpersonspecs.jsp?id=" + id_jur_prs;
}

if (type.equalsIgnoreCase("general")) {
	
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


	if (process.equalsIgnoreCase("no"))
	/* вибираємо тип дії (добавити, видалити...)*/
		{
	
		/*  --- Нам нужны параметры клуба --- */
		/*  --- Добавити запис --- */
	    	if (action.equalsIgnoreCase("add") || action.equalsIgnoreCase("addneeded") || action.equalsIgnoreCase("add2") ) {
	    		
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
			<% if ("DOCUMENT".equalsIgnoreCase(back_type)) { %>
			<%= Bean.getOperationTitle(
					Bean.documentXML.getfieldTransl("l_add_doc", false),
					"Y",
					"Y") 
			%>
			<% } else { %>
			<%= Bean.getOperationTitleShort(
					"",
					Bean.documentXML.getfieldTransl("l_add_doc", false),
					"Y",
					"Y") 
			%>
			<% } %>
	 		<%
	 		String lDocType = "";
	 		String lParty1Id = "";
	 		String lParty1Name = "";
	 		String lParty2Id = "";
	 		String lParty2Name = "";
	 		if (action.equalsIgnoreCase("addneeded")) {
	 			String id_doc_needed	= Bean.getDecodeParam(parameters.get("id_doc_needed"));
	 		
	 			bcDocumentNeededObject doc = new bcDocumentNeededObject(id_doc_needed);
	 			
	 			lDocType = doc.getValue("CD_DOC_TYPE");
	 	 		lParty1Id = doc.getValue("ID_PARTY1");
	 	 		lParty1Name = doc.getValue("SNAME_PARTY1");
	 	 		lParty2Id = doc.getValue("ID_PARTY2");
	 	 		lParty2Name = doc.getValue("SNAME_PARTY2");
	 		} else {
	 			if ("PARTNER".equalsIgnoreCase(back_type)) {
	 				lParty1Id = id_jur_prs;
	 			}
	 		}
	 		%>
	    <form action="../crm/club/documentupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">
		    <input type="hidden" name="action" value="add">
    		<input type="hidden" name="type" value="general">
	    	<input type="hidden" name="process" value="yes">
    		<input type="hidden" name="back_type" value="<%=back_type %>">
    		<input type="hidden" name="id_club" value="<%=id_club %>">
    		<input type="hidden" name="id_jur_prs" value="<%=id_jur_prs %>">
		<table <%=Bean.getTableDetailParam() %>>
		    <tr>
				<td><%= Bean.documentXML.getfieldTransl("cd_doc_type", true) %></td> <td><select name="cd_doc_type" class="inputfield" > <%= Bean.getDocTypeOptions(lDocType, false) %></select></td>
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
				<td><b><%= Bean.documentXML.getfieldTransl("name_party1", true) %>
					<%=Bean.getGoToJurPrsHyperLink(lParty1Id) %>
				</b></td>
				<td>
					<%=Bean.getWindowFindJurPrs("party1", lParty1Id, lParty1Name, "ALL", "50") %>
				</td>			
			</tr>
	    	<tr>
				<td><%= Bean.documentXML.getfieldTransl("name_party2", false) %>
					<%=Bean.getGoToJurPrsHyperLink(lParty2Id) %>
				</td>
				<td>
					<%=Bean.getWindowFindJurPrs("party2", lParty2Id, lParty2Name, "ALL", "50") %>
				</td>			
			</tr>
	    	<tr>
				<td><%= Bean.documentXML.getfieldTransl("name_party3", false) %></td>
				<td>
					<%=Bean.getWindowFindJurPrs("party3", "", "", "ALL", "50") %>
				</td>			
			</tr>
	
	 		<tr>
				<td colspan="6" align="center">
					<%=Bean.getSubmitButtonAjax(updateLink, "submit", "updateForm") %>
					<%=Bean.getResetButton() %>
					<%=Bean.getGoBackButton(backLink) %>
				</td>
			</tr>
	
		</table>
	
		</form>
	<%= Bean.getCalendarScript("date_doc", false) %>
	<%= Bean.getCalendarScript("date_begin_doc", false) %>
	<%= Bean.getCalendarScript("date_end_doc", false) %>
	
		        <%
	    } else if (action.equalsIgnoreCase("edit"))	{
	    	bcDocumentObject doc = new bcDocumentObject(id_doc); %>
	    	
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
					Bean.documentXML.getfieldTransl("l_edit_doc", false),
					"Y",
					"Y") 
			%>
	        <form action="../crm/club/documentupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">
	    	    <input type="hidden" name="action" value="edit">
	        	<input type="hidden" name="process" value="yes">
	        	<input type="hidden" name="type" value="general">
	        	<input type="hidden" name="id_doc" value="<%=doc.getValue("ID_DOC") %>">
	    		<input type="hidden" name="back_type" value="<%=back_type %>">
	    		<input type="hidden" name="id_club" value="<%=id_club %>">
	    		<input type="hidden" name="id_jur_prs" value="<%=id_jur_prs %>">
	    	<table <%=Bean.getTableDetailParam() %>>
	    	    <tr>
	    			<td><%= Bean.documentXML.getfieldTransl("cd_doc_type", true) %></td> <td><select name="cd_doc_type" class="inputfield" > <%= Bean.getDocTypeOptions(doc.getValue("CD_DOC_TYPE"), false) %></select></td>
	    			<td><%= Bean.clubXML.getfieldTransl("club", false) %>
	    				<%= Bean.getGoToClubLink(doc.getValue("ID_CLUB")) %>
	    			</td><td><input type="text" name="name_club" size="55" value="<%= Bean.getClubShortName(doc.getValue("ID_CLUB")) %>" readonly="readonly" class="inputfield-ro"></td>
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
	    			<td colspan="6" align="center">
	    				<%=Bean.getSubmitButtonAjax(updateLink, "submit", "updateForm") %>
	    				<%=Bean.getResetButton() %>
	    				<%=Bean.getGoBackButton(backLink) %>
	    			</td>
	    		</tr>
	    	</table>

	    	</form>
	    	<%= Bean.getCalendarScript("date_doc", false) %>
	    	<%= Bean.getCalendarScript("date_begin_doc", false) %>
	    	<%= Bean.getCalendarScript("date_end_doc", false) %>

	    <%
	  	} else {
	   	    %> <%= Bean.getUnknownActionText(action) %><%
	   	}
		
	} else if (process.equalsIgnoreCase("yes"))	{
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
			<%= Bean.executeInsertFunction("PACK$DOC_UI.add_doc", pParam, backLink + "&id=" , "") %>
			<% 	
	
	    } else if (action.equalsIgnoreCase("remove")) { 
	    	ArrayList<String> pParam = new ArrayList<String>();
					
			pParam.add(id_doc);		
		 	%>
			<%= Bean.executeDeleteFunction("PACK$DOC_UI.delete_doc", pParam, generalLink , "") %>
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
			<%= Bean.executeUpdateFunction("PACK$DOC_UI.update_doc", pParam, backLink, "") %>
			<% 
	    } else { %> 
	    	<%= Bean.getUnknownActionText(action) %><% 
	    }
	} else {
	    %> <%= Bean.getUnknownProcessText(process) %> <%
	}
	
} else if (type.equalsIgnoreCase("account")) {
	
	String 
		id_doc_bank_account			= Bean.getDecodeParamPrepare(parameters.get("id_doc_bank_account")),
		number_bank_account 		= Bean.getDecodeParamPrepare(parameters.get("number_bank_account")),
	    desc_bank_account 			= Bean.getDecodeParamPrepare(parameters.get("desc_bank_account")),
	    cd_currency 				= Bean.getDecodeParamPrepare(parameters.get("cd_currency")),
	    cd_bank_account_type 		= Bean.getDecodeParamPrepare(parameters.get("cd_bank_account_type")),
	    date_beg 					= Bean.getDecodeParamPrepare(parameters.get("date_beg")),
	    contact_person 				= Bean.getDecodeParamPrepare(parameters.get("contact_person")),
	    contact_phone 				= Bean.getDecodeParamPrepare(parameters.get("contact_phone")),
	    id_bank		 				= Bean.getDecodeParamPrepare(parameters.get("id_bank")),
	    name_jur_prs 				= Bean.getDecodeParamPrepare(parameters.get("name_jur_prs")),
	    id_nat_prs 					= Bean.getDecodeParamPrepare(parameters.get("id_nat_prs")),
	    cd_doc_bank_account_type 	= Bean.getDecodeParamPrepare(parameters.get("cd_doc_bank_account_type"));

	if (process.equalsIgnoreCase("no")) {
		
		bcDocumentObject doc = new bcDocumentObject(id_doc);
		
		if (action.equalsIgnoreCase("add")) {
			
	        %> 
	
			<script>
				var formData = new Array (
					new Array ('name_entry', 'varchar2', 1),
					new Array ('cd_bank_account_type', 'varchar2', 1),
					new Array ('number_bank_account', 'varchar2', 1),
					new Array ('name_bank', 'varchar2', 1),
					new Array ('cd_currency', 'varchar2', 1),
					new Array ('name_club', 'varchar2', 1),
					new Array ('date_beg', 'varchar2', 1)
				);
				function myValidateForm() {
					return validateForm(formData);
				}
			</script>

		<%= Bean.getOperationTitleShort(
				"",
				Bean.documentXML.getfieldTransl("h_add_doc_bank_account", false),
				"Y",
				"N") 
		%>
    <form action="../crm/club/documentupdate.jsp" name="updateForm" id="updateForm" enctype="multipart/form-data" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">
        <input type="hidden" name="type" value="account">
        <input type="hidden" name="action" value="add">
        <input type="hidden" name="process" value="yes">
        <input type="hidden" name="id_doc" value="<%=id_doc %>">
        <input type="hidden" name="id_club" value="<%=doc.getValue("ID_CLUB") %>">
	<table <%=Bean.getTableDetailParam() %>>
	   <tr>
			<td><%=Bean.accountXML.getfieldTransl("name_owner_bank_account", true)%></td>
			<td>
				<%=Bean.getWindowFindJurAndNatPrs("", "DOC", "", id_doc, "50") %>
			</td>
			<td><%= Bean.clubXML.getfieldTransl("club", false) %>
				<%= Bean.getGoToClubLink(doc.getValue("ID_CLUB")) %>
			</td><td><input type="text" name="name_club" size="50" value="<%= Bean.getClubShortName(doc.getValue("ID_CLUB")) %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td><%=Bean.accountXML.getfieldTransl("name_bank_account_type", true)%></td><td><select name="cd_bank_account_type"  class="inputfield"><%=Bean.getBankAccountTypeOptions("", true)%></select></td>
			<td><%=Bean.clubXML.getfieldTransl("club_date_beg", true)%></td> <td><%=Bean.getCalendarInputField("date_beg", "", "10") %></td>
		</tr>
		<tr>
			<td><%=Bean.accountXML.getfieldTransl("number_bank_account", true)%></td><td><input type="text" name="number_bank_account" size="60" value="" class="inputfield"></td>
			<td rowspan="3"><%=Bean.accountXML.getfieldTransl("desc_bank_account", false)%></td><td rowspan="3"><textarea name="desc_bank_account" cols="47" rows="3" class="inputfield"></textarea></td>
		</tr>
		<tr>
			<td><%=Bean.accountXML.getfieldTransl("name_bank_alt", true)%></td>
			<td>
				<%=Bean.getWindowFindJurPrs("bank", "", "BANK", "50") %>
			</td>				
		</tr>
		<tr>
			<td><%=Bean.accountXML.getfieldTransl("name_currency", true)%></td> <td><select name="cd_currency"  class="inputfield"><%=Bean.getCurrencyOptions("", true)%></select> </td>
		</tr>
		<tr>
			<td colspan="6" align="center">
				<%=Bean.getSubmitButtonAjax("../crm/club/documentupdate.jsp", "submit", "updateForm") %>
				<%=Bean.getResetButton() %>
				<%=Bean.getGoBackButton("../crm/club/documentspecs.jsp?id=" + id_doc) %>
			</td>
		</tr>
	</table>
	</form>
		<!-- Скрипт для втавки меню вибору дати -->
		<%= Bean.getCalendarScript("date_beg", false) %>

		<% 
		} else if (action.equalsIgnoreCase("edit")) {
			
			bcDocumentBankAccountObject account = new bcDocumentBankAccountObject(id_doc_bank_account);
			
	        %> 
	
			<script>
				var formData = new Array (
					new Array ('name_entry', 'varchar2', 1),
					new Array ('cd_bank_account_type', 'varchar2', 1),
					new Array ('number_bank_account', 'varchar2', 1),
					new Array ('name_bank', 'varchar2', 1),
					new Array ('cd_currency', 'varchar2', 1),
					new Array ('name_club', 'varchar2', 1),
					new Array ('date_beg', 'varchar2', 1)
				);
				function myValidateForm() {
					return validateForm(formData);
				}
			</script>

		<%= Bean.getOperationTitleShort(
				"",
				Bean.documentXML.getfieldTransl("h_edit_doc_bank_account", false),
				"Y",
				"N") 
		%>
    <form action="../crm/club/documentupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">
        <input type="hidden" name="type" value="account">
        <input type="hidden" name="action" value="edit">
        <input type="hidden" name="process" value="yes">
        <input type="hidden" name="id_doc" value="<%=id_doc %>">
        <input type="hidden" name="id_doc_bank_account" value="<%=id_doc_bank_account %>">
	<table <%=Bean.getTableDetailParam() %>>
		<tr>
			<% if (Bean.isEmpty(account.getValue("ID_NAT_PRS"))) { %>
			<td><%=Bean.accountXML.getfieldTransl("name_owner_bank_account", false)%>
				<%=Bean.getGoToJurPrsHyperLink(account.getValue("ID_JUR_PRS")) %>
			</td>
			<% } else { %>
			<td><%= Bean.accountXML.getfieldTransl("name_owner_bank_account", false) %>
				<%= Bean.getGoToNatPrsLink(account.getValue("ID_NAT_PRS")) %>
			</td>
			<% } %>
			<td><input type="text" name="name_owner" size="60" value="<%=account.getValue("SNAME_OWNER_BANK_ACCOUNT")%>" readonly="readonly" class="inputfield-ro"></td>
 		    <td><%= Bean.clubXML.getfieldTransl("club", false) %>
				<%= Bean.getGoToClubLink(account.getValue("ID_CLUB")) %>
			</td><td><input type="text" name="name_club" size="50" value="<%= Bean.getClubShortName(account.getValue("ID_CLUB")) %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>		
			<td><%=Bean.accountXML.getfieldTransl("name_bank_account_type", false)%></td><td><input type="text" name="cd_bank_account_type" size="60" value="<%=account.getValue("NAME_BANK_ACCOUNT_TYPE")%>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.documentXML.getfieldTransl("title_doc", false) %></td> <td><input type="text" name="full_doc" size="50" value="<%=doc.getValue("FULL_DOC") %>" readonly="readonly" class="inputfield-ro"> </td>
		</tr>
		<tr>		
			<td><%=Bean.accountXML.getfieldTransl("number_bank_account", false)%>
				<%= Bean.getGoToBankAccountLink(account.getValue("ID_BANK_ACCOUNT")) %>
			</td><td><input type="text" name="number_bank_account" size="60" value="<%=account.getValue("NUMBER_BANK_ACCOUNT")%>" readonly="readonly" class="inputfield-ro"></td>
			<td><%=Bean.accountXML.getfieldTransl("name_doc_bank_account_type", true)%></td><td><select name="cd_doc_bank_account_type"  class="inputfield"><%=Bean.getBankAccountTypeOptions(account.getValue("CD_DOC_BANK_ACCOUNT_TYPE"), true)%></select></td>
		</tr>
		<tr>
			<td><%=Bean.accountXML.getfieldTransl("name_bank_alt", false)%>
				<%= Bean.getGoToJurPrsHyperLink(account.getValue("ID_BANK")) %>
			</td> <td><input type="text" name="name_bank" size="60" value="<%=account.getValue("SNAME_BANK")%>" readonly="readonly" class="inputfield-ro"></td>
			<td colspan="2">&nbsp;</td>
		</tr>
		<tr>
			<td><%=Bean.accountXML.getfieldTransl("name_currency", false)%></td> <td><input type="text" name="name_currency" size="20" value="<%=account.getValue("NAME_CURRENCY")%>" readonly="readonly" class="inputfield-ro"> </td>
			<td colspan="2">&nbsp;</td>
		</tr>
		<tr>
			<td colspan="6" align="center">
				<%=Bean.getSubmitButtonAjax("../crm/club/documentupdate.jsp", "submit", "updateForm") %>
				<%=Bean.getResetButton() %>
				<%=Bean.getGoBackButton("../crm/club/documentspecs.jsp?id=" + id_doc) %>
			</td>
		</tr>
	</table>
	</form>

		<% 
		} else if (action.equalsIgnoreCase("addall")) { %>
		
		<%= Bean.getOperationTitleShort(
				"",
				Bean.documentXML.getfieldTransl("h_add_doc_bank_account", false),
				"Y",
				"N") 
		%>
		<%= doc.getAllBankAccountsHTML("", "", "1", "1000") %>
		<% 
		} else { %> 
		<%= Bean.getUnknownActionText(action) %><%
			
		}
	} else if (process.equalsIgnoreCase("yes")) {
		
		if (action.equalsIgnoreCase("add")) { 
	    	
			ArrayList<String> pParam = new ArrayList<String>();
		    	      				
			pParam.add(id_doc);
	    	pParam.add(id_bank);
	    	pParam.add(cd_currency);
	    	pParam.add(id_jur_prs);
	    	pParam.add(id_nat_prs);
	    	pParam.add(date_beg);
	    	pParam.add(number_bank_account);
	    	pParam.add(cd_bank_account_type);
	    	pParam.add(desc_bank_account);
	    	pParam.add(contact_person);
	    	pParam.add(contact_phone);
	    	pParam.add(id_club);
	    	pParam.add(Bean.getDateFormat());
			
			%>
			<%= Bean.executeInsertFunction("PACK$DOC_UI.add_doc_bank_account", pParam, "../crm/club/documentspecs.jsp?id=" + id_doc + "&id_doc_bank_account=" , "../crm/club/documentspecs.jsp?id=" + id_doc) %>
			<% 	

	   	} else if (action.equalsIgnoreCase("remove")) { 
	   		
		   	ArrayList<String> pParam = new ArrayList<String>();
			 		 	    	      				
			pParam.add(id_doc_bank_account);

			%>
			<%= Bean.executeDeleteFunction("PACK$DOC_UI.delete_doc_bank_account", pParam, "../crm/club/documentspecs.jsp?id=" + id_doc , "") %>
			<%

		} else if (action.equalsIgnoreCase("edit")) { 
			
		 	ArrayList<String> pParam = new ArrayList<String>();
		 	    	      				
	      	pParam.add(id_doc_bank_account);
	     	pParam.add(cd_doc_bank_account_type);
			
			%>
			<%= Bean.executeUpdateFunction("PACK$DOC_UI.update_doc_bank_account_short", pParam, "../crm/club/documentspecs.jsp?id=" + id_doc, "") %>
			<% 	
		} else if (action.equalsIgnoreCase("addall")) { 
			
			String[] results = new String[2];

	    	ArrayList<String> id_value=new ArrayList<String>();
			Map<String,String> type_value=new HashMap<String,String>();

	    	String callSQL = "";

	    	Set<String> keySet = parameters.keySet();
			Iterator<String> keySetIterator = keySet.iterator();
			String key = "";
	    	while(keySetIterator.hasNext()) {
	   			try{
	   				key = (String)keySetIterator.next();
	   				if(key.contains("id_")){
	   					id_value.add(key.substring(3));
		   				System.out.println("key="+key.toString()+", value="+key.substring(3));
	   				}
	   				if(key.contains("tp_")){
	   					type_value.put(key.substring(3), Bean.getDecodeParam(parameters.get(key)));
		   				System.out.println("key="+key.substring(3)+", value="+parameters.get(key));
	   				}
	   			}
	   			catch(Exception ex){
	   				Bean.writeException(
	   						"../crm/club/documentupdate.jsp",
	   						type,
	   						process,
	   						action,
	   						Bean.commonXML.getfieldTransl("h_get_param_value_error", false) + key+": " + ex.toString());
	   			}
	   		}
	   		
	   	    String resultInt = "";
	   	    String resultFull = "0";
	   	    String resultMessage = "";
	   	    String resultMessageFull = "";
			
			if (id_value.size()>0) {
		 		 for(int counter=0;counter<id_value.size();counter++){
			     	callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK$DOC_UI.set_doc_bank_account(?,?,?,?)}";

			        ArrayList<String> pParam = new ArrayList<String>();
			        			
			        pParam.add(id_doc);
			        pParam.add(id_value.get(counter));
			        pParam.add(type_value.get(id_value.get(counter)));
				
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
		    	<%=Bean.showCallResult(callSQL, 
		    		resultFull, 
		    		resultMessage, 
		    		"../crm/club/documentspecs.jsp?id=" + id_doc, 
		    		"../crm/club/documentspecs.jsp?id=" + id_doc, 
		    		Bean.form_messageXML.getfieldTransl("save_error", false)) %>
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
