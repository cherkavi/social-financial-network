<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%@page import="bc.objects.bcLoySchemeObject"%>
<%@page import="bc.objects.bcComissionObject"%>
<%@page import="bc.objects.bcClubRelationshipObject"%>
<%@page import="java.util.HashMap"%>
<%@page import="bc.objects.bcClubShortObject"%>

<%@page import="java.util.ArrayList"%><html>

<%= Bean.getLogOutScript(request) %>

<head>
	<%= Bean.getMetaContent() %>
	<%= Bean.getBottomFrameCSS() %>
</head>

<body>

<%

HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String pageFormName = "CLUB_COMISTYPE";

Bean.setJspPageForTabName(pageFormName);

request.setCharacterEncoding("UTF-8");

String id			= Bean.getDecodeParam(parameters.get("id"));
String type			= Bean.getDecodeParam(parameters.get("type")); 
String action		= Bean.getDecodeParam(parameters.get("action")); 
String process		= Bean.getDecodeParam(parameters.get("process"));

if (id==null || ("".equalsIgnoreCase(id))) id="empty";
if (type==null || ("".equalsIgnoreCase(type))) type="empty";
if (action==null || ("".equalsIgnoreCase(action))) action="empty";
if (process==null || ("".equalsIgnoreCase(process))) process="empty";
if (type.equalsIgnoreCase("general")) {
	if (process.equalsIgnoreCase("no")) {
		if (action.equalsIgnoreCase("add") || action.equalsIgnoreCase("add2")) {
    		
			bcClubShortObject club = new bcClubShortObject(Bean.getCurrentClubID());
			
	        %> 
			<%= Bean.getOperationTitle(
					Bean.comissionXML.getfieldTransl("H_COMISSION_TYPE_ADD", false),
					"Y",
					"N") 
			%>
		<script>
			var formData = new Array (
				new Array ('cd_club_rel_type', 'varchar2', 1)
			);
			function myValidateForm() {
				return validateForm(formData);
			}
		</script>

		<form action="../crm/club/comistypeupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">  	        
	        <input type="hidden" name="type" value="general">
	        <input type="hidden" name="action" value="adddet">
	        <input type="hidden" name="action_prev" value="<%=action %>">
	        <input type="hidden" name="id" value="<%=id %>">
	        <input type="hidden" name="process" value="no">
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
			<td><%= Bean.comissionXML.getfieldTransl("name_club_rel_type", true) %></td> <td><%= Bean.getClubRelTypeRadio("cd_club_rel_type", "") %></td>
		</tr>
	 		<tr>
				<td colspan="6" align="center">
					<%=Bean.getSubmitButtonAjax("../crm/club/comistypeupdate.jsp") %>
					<%=Bean.getResetButton() %>
					<% if ("add".equalsIgnoreCase(action)) { %>
						<%=Bean.getGoBackButton("../crm/club/comistype.jsp") %>
					<% } else { %>
						<%=Bean.getGoBackButton("../crm/club/comistypespecs.jsp?id=" + id) %>
					<% } %>
				</td>
			</tr>
		</table>
		</form>

		<% 	
			
		} else if (action.equalsIgnoreCase("adddet")) {
    		
			bcClubShortObject club 		= new bcClubShortObject(Bean.getCurrentClubID());
			String action_prev			= Bean.getDecodeParam(parameters.get("action_prev")); 
			String cd_club_rel_type		= Bean.getDecodeParam(parameters.get("cd_club_rel_type")); 
	        %> 
			<%= Bean.getOperationTitle(
					Bean.comissionXML.getfieldTransl("H_COMISSION_TYPE_ADD", false),
					"Y",
					"N") 
			%>
		<script>
			var formData = new Array (
				new Array ('cd_comission_type', 'varchar2', 1),
				new Array ('name_comission_type', 'varchar2', 1),
				new Array ('name_club', 'varchar2', 1),
				new Array ('cd_club_rel_type', 'varchar2', 1),
				new Array ('cd_participant_payer', 'varchar2', 1),
				new Array ('cd_participant_receiver', 'varchar2', 1)
			);
			function myValidateForm() {
				return validateForm(formData);
			}
		</script>

		<form action="../crm/club/comistypeupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">  	        
	        <input type="hidden" name="type" value="general">
	        <input type="hidden" name="action" value="add">
	        <input type="hidden" name="process" value="yes">
	        <input type="hidden" name="cd_club_rel_type" value="<%=cd_club_rel_type %>">
		<table <%=Bean.getTableDetailParam() %>>
		<tr>
			<td><%= Bean.comissionXML.getfieldTransl("cd_comission_type", true) %></td> <td><input type="text" name="cd_comission_type" size="60" value="" class="inputfield" title=""></td>
 		    <td><%= Bean.clubXML.getfieldTransl("club", true) %>
				<%= Bean.getGoToClubLink(club.getValue("ID_CLUB")) %>
			</td>
		  	<td>
				<%=Bean.getWindowFindClub("club", club.getValue("ID_CLUB"), club.getValue("SNAME_CLUB"), "25") %>
		  	</td>
		</tr>
	    <tr>
			<td rowspan="3"><%= Bean.comissionXML.getfieldTransl("name_comission_type_full", true) %></td> <td rowspan="3"><textarea name="name_comission_type" cols="57" rows="3" class="inputfield"></textarea></td>
			<td><%= Bean.comissionXML.getfieldTransl("fixed_value_def_frmt", false) %></td> <td><input type="text" name="fixed_value" size="15" value="" class="inputfield"></td>
		</tr>
	    <tr>
			<td><%= Bean.comissionXML.getfieldTransl("percent_value_def_frmt", false) %></td> <td><input type="text" name="percent_value" size="15" value="" class="inputfield"></td>
		</tr>
		<tr>
			<td><%= Bean.comissionXML.getfieldTransl("exist_flag", false) %></td><td><select name="exist_flag" class="inputfield"><%= Bean.getMeaningFromLookupNameOptions("YES_NO", "Y", false) %> </select></td>
		</tr>
		<tr>
			<td class="top_line" colspan="4">
				<b><%=Bean.comissionXML.getfieldTransl("h_payer_and_receiver", false) %></b>
			</td>
		</tr>
	    <tr>
			<td><%= Bean.comissionXML.getfieldTransl("name_club_rel_type", false) %></td> <td><input type="text" name="name_club_rel_type" size="60" value="<%=Bean.getClubRelTypeName(cd_club_rel_type) %>" readonly="readonly" class="inputfield-ro"></td>
			<td colspan="2">&nbsp;</td>
		</tr>
	    <tr>
			<td><%= Bean.comissionXML.getfieldTransl("jur_prs_payer", true) %></td> <td><select name="cd_participant_payer" class="inputfield"><%= Bean.getClubComissionParticipantOptions("", true) %> </select></td>
			<td colspan="2">&nbsp;</td>
		</tr>
	    <tr>
			<td><%= Bean.comissionXML.getfieldTransl("jur_prs_receiver", true) %></td> <td><select name="cd_participant_receiver" class="inputfield"><%= Bean.getClubComissionParticipantOptions("", true) %> </select></td>
			<td colspan="2">&nbsp;</td>
		</tr>
	    <tr>
			<td><%= Bean.comissionXML.getfieldTransl("name_payment_system", false) %></td> <td><select name="id_payment_system" class="inputfield"><%= Bean.getPaymentSystemOptions("", true) %> </select></td>
			<td colspan="2">&nbsp;</td>
		</tr>
	 		<tr>
				<td colspan="6" align="center">
					<%=Bean.getSubmitButtonAjax("../crm/club/comistypeupdate.jsp") %>
					<%=Bean.getResetButton() %>
					<%=Bean.getGoBackButton("../crm/club/comistypeupdate.jsp?id=" + id + "&type=general&process=no&action=" + action_prev) %>
				</td>
			</tr>
		</table>
		</form>

		<% 	
	   	} else {
   	    %> <%= Bean.getUnknownActionText(action) %><%
   		}
	} else if (process.equalsIgnoreCase("yes")) {    

	  	String
	  		id_comission_type 			= Bean.getDecodeParam(parameters.get("id_comission_type")), 
  			cd_comission_type 			= Bean.getDecodeParam(parameters.get("cd_comission_type")), 
  			name_comission_type 		= Bean.getDecodeParam(parameters.get("name_comission_type")), 
  			id_payment_system 			= Bean.getDecodeParam(parameters.get("id_payment_system")), 
  			cd_club_rel_type 			= Bean.getDecodeParam(parameters.get("cd_club_rel_type")), 
  			cd_participant_payer 		= Bean.getDecodeParam(parameters.get("cd_participant_payer")), 
  			cd_participant_receiver 	= Bean.getDecodeParam(parameters.get("cd_participant_receiver")), 
	  		fixed_value 				= Bean.getDecodeParam(parameters.get("fixed_value")), 
	  		percent_value 				= Bean.getDecodeParam(parameters.get("percent_value")), 
	  		exist_flag	 				= Bean.getDecodeParam(parameters.get("exist_flag")), 
	  		id_club		 				= Bean.getDecodeParam(parameters.get("id_club"));
	    
		if (action.equalsIgnoreCase("add")) { 
				
			ArrayList<String> pParam = new ArrayList<String>();
					
			pParam.add(id_club);
			pParam.add(cd_comission_type);
			pParam.add(name_comission_type);
			pParam.add(id_payment_system);
			pParam.add(cd_club_rel_type);
			pParam.add(cd_participant_payer);
			pParam.add(cd_participant_receiver);
			pParam.add(fixed_value);
			pParam.add(percent_value);
			pParam.add(exist_flag);
	
		 	%>
			<%= Bean.executeInsertFunction("PACK$COMIS_TYPE_UI.add_type", pParam, "../crm/club/comistypespecs.jsp?id=", "../crm/club/comistype.jsp") %>
			<% 	
	
		} else if (action.equalsIgnoreCase("remove")) {
			ArrayList<String> pParam = new ArrayList<String>();
					
			pParam.add(id);
			
		 	%>
			<%= Bean.executeDeleteFunction("PACK$COMIS_TYPE_UI.delete_type", pParam, "../crm/club/comistype.jsp" , "") %>
			<% 	
	
		} else if (action.equalsIgnoreCase("edit")) {
			String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK$COMIS_TYPE_UI.update_type("+
				"?,?,?,?,?,?,?,?,?,?,?,?,?)}";

			ArrayList<String> pParam = new ArrayList<String>();
						
			pParam.add(id);
			pParam.add(cd_comission_type);
			pParam.add(name_comission_type);
			pParam.add(id_payment_system);
			pParam.add(cd_participant_payer);
			pParam.add(cd_participant_receiver);
			pParam.add(fixed_value);
			pParam.add(percent_value);
			pParam.add(exist_flag);
	
		 	%>
			<%= Bean.executeUpdateFunction("PACK$COMIS_TYPE_UI.update_type", pParam, "../crm/club/comistypespecs.jsp?id=" + id, "") %>
			<% 	
	
		} else {%> 
			<%= Bean.getUnknownActionText(action) %><% 
		}
	} else {%> 
		<%= Bean.getUnknownProcessText(process) %><% 
	}
} else  if (type.equalsIgnoreCase("comission")) {
	  String id_comission		= Bean.getDecodeParam(parameters.get("id_comission"));
	
	  if (process.equalsIgnoreCase("no")) {
		  
		if (action.equalsIgnoreCase("edit")) {
			  
			bcComissionObject comission = new bcComissionObject(id_comission);

        	String idClubRel = comission.getValue("ID_CLUB_REL");
        	bcClubRelationshipObject rel = new bcClubRelationshipObject("CREATED", idClubRel);
        	
        	String rel_type = rel.getValue("CD_CLUB_REL_TYPE");

			%> 
			<%= Bean.getOperationTitle(
				Bean.comissionXML.getfieldTransl("h_edit_comission", false),
				"Y",
				"Y") 
			%>
        <form action="../crm/club/comistypeupdate.jsp"name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return myValidateForm()">
		        <input type="hidden" name="type" value="comission">
		        <input type="hidden" name="action" value="edit">
		        <input type="hidden" name="process" value="yes">
	        	<input type="hidden" name="id" value="<%=comission.getValue("ID_COMISSION_TYPE") %>">
	        	<input type="hidden" name="id_comission" value="<%=id_comission %>">
	        	<input type="hidden" name="id_comission_type" value="<%=comission.getValue("ID_COMISSION_TYPE") %>">
	        	<input type="hidden" name="id_club_rel" value="<%=comission.getValue("ID_CLUB_REL") %>">
		<table <%=Bean.getTableDetailParam() %>>
			<tr>
				<td><%= Bean.relationshipXML.getfieldTransl("general", false) %></td><td><input type="text" name="name_club_rel" size="90" value="<%=rel.getValue("FULL_NAME_CLUB_REL") %>" readonly="readonly" class="inputfield-ro"></td>
	 		    <td><%= Bean.clubXML.getfieldTransl("club", false) %>
					<%= Bean.getGoToClubLink(comission.getValue("ID_CLUB")) %>
				</td>
				<td>
					<input type="hidden" name="id_club" id="id_club" value="<%= comission.getValue("ID_CLUB") %>">
					<input type="text" name="name_club" id="name_club" size="40" value="<%= Bean.getClubShortName(comission.getValue("ID_CLUB")) %>" readonly="readonly" class="inputfield-ro">
				</td>
			</tr>
			<tr>
				<td><%= Bean.comissionXML.getfieldTransl("name_comission_type", false) %></td><td><input type="text" name="name_comission_type" size="90" value="<%= Bean.getComissionTypeName(comission.getValue("ID_COMISSION_TYPE")) %>" readonly="readonly" class="inputfield-ro"></td>
				<td><%= Bean.comissionXML.getfieldTransl("fixed_value", false) %></td><td><input type="text" name="fixed_value" size="16" value="<%=comission.getValue("FIXED_VALUE_FRMT") %>" class="inputfield"></td>
			</tr>
			<tr>
				<td><%= Bean.comissionXML.getfieldTransl("full_doc", false) %>
					<%= Bean.getGoToDocLink(comission.getValue("ID_DOC")) %>
				</td><td><select name="id_doc" class="inputfield"><%= Bean.getDocForClubRelationshipOptions(comission.getValue("ID_CLUB_REL"), comission.getValue("ID_DOC"), true) %></select></td>
				<td valign="top"><%= Bean.comissionXML.getfieldTransl("percent_value", false) %></td><td valign="top"><input type="text" name="percent_value" size="16" value="<%=comission.getValue("PERCENT_VALUE_FRMT") %>" class="inputfield"></td>
			</tr>
			<tr>
				<td rowspan="3"><%= Bean.comissionXML.getfieldTransl("description", false) %></td><td rowspan="3"><textarea name="description" cols="90" rows="3" class="inputfield"><%=comission.getValue("DESCRIPTION") %></textarea></td>
				<td><%= Bean.comissionXML.getfieldTransl("begin_action_date", true) %></td> <td><%=Bean.getCalendarInputField("begin_action_date", comission.getValue("BEGIN_ACTION_DATE_FRMT"), "10") %></td>
			</tr>
			<tr>
				<td><%= Bean.comissionXML.getfieldTransl("end_action_date", false) %></td> <td><%=Bean.getCalendarInputField("end_action_date", comission.getValue("END_ACTION_DATE_FRMT"), "10") %></td>
			</tr>
			<tr>
				<td><%= Bean.comissionXML.getfieldTransl("exist_flag", false) %></td><td><select name="exist_flag" class="inputfield"><%= Bean.getMeaningFromLookupNameOptions("YES_NO", comission.getValue("EXIST_FLAG"), false) %></select></td>
			</tr>
			<tr>
				<td><%= Bean.comissionXML.getfieldTransl("jur_prs_payer", false) %>
					<%= Bean.getGoToJurPrsHyperLink(comission.getValue("ID_JUR_PRS_PAYER")) %>
				</td>
				<td>
					<input type="hidden" id="id_jur_prs_payer" name="id_jur_prs_payer" value="<%= comission.getValue("ID_JUR_PRS_PAYER") %>" readonly="readonly" class="inputfield">
					<input type="text" id="name_jur_prs_payer" name="name_jur_prs_payer" size="90" value="<%= comission.getValue("SNAME_JUR_PRS_PAYER") %>" readonly="readonly" class="inputfield-ro">
				</td>	
			</tr>
			<tr>
				<td><%= Bean.comissionXML.getfieldTransl("jur_prs_receiver", true) %>
					<%= Bean.getGoToJurPrsHyperLink(comission.getValue("ID_JUR_PRS_RECEIVER")) %>
				</td>
				<td>
					<input type="hidden" id="id_jur_prs_receiver" name="id_jur_prs_receiver" value="<%= comission.getValue("ID_JUR_PRS_RECEIVER") %>" readonly="readonly" class="inputfield">
					<input type="text" id="name_jur_prs_receiver" name="name_jur_prs_receiver" size="90" value="<%= comission.getValue("SNAME_JUR_PRS_RECEIVER") %>" readonly="readonly" class="inputfield-ro">
				</td>	
			</tr>
			<tr>
				<td colspan="4" class="top_line"><b><%= Bean.comissionXML.getfieldTransl("t_additional_param", false) %></b></td>
			</tr>
			<% if ("DEALER-TECHNICAL_ACQUIRER".equalsIgnoreCase(rel_type) ||
				   "DEALER-FINANCE_ACQUIRER".equalsIgnoreCase(rel_type)||
				   "OPERATOR-DEALER".equalsIgnoreCase(rel_type)) {%>
			<tr>
				<td><%= Bean.comissionXML.getfieldTransl("name_service_place", false) %>
					<%= Bean.getGoToJurPrsHyperLink(comission.getValue("ID_SERVICE_PLACE")) %>
				</td>
				<td>
					<%=Bean.getWindowFindServicePlace("service_place", comission.getValue("ID_SERVICE_PLACE"), "", "40") %>
				</td>			
				<td valign="top"><%= Bean.comissionXML.getfieldTransl("id_card_status", false) %></td> <td><select name="id_card_status" id="id_card_status" class="inputfield" onchange="dwr_get_card_bon_disc_category3('', this, '', '', '<%=Bean.getSessionId() %>');"><%= Bean.getClubCardStatusOptions(comission.getValue("ID_CARD_STATUS"), true) %></select></td>
			</tr>
			<tr>
				<td valign="top"><%= Bean.comissionXML.getfieldTransl("id_term", false) %>
					<%= Bean.getGoToTerminalLink(comission.getValue("ID_TERM")) %>
				</td> 
				<td>
					<%=Bean.getWindowFindTerm("term", comission.getValue("ID_TERM"), "8") %>
				</td>			
				<td><%= Bean.card_taskXML.getfieldTransl("id_bon_category", false)  %></td> <td><select name="id_bon_category"  id="id_bon_category" class="inputfield"><%=Bean.getClubCardCategoryForStatusOptions(comission.getValue("ID_CARD_STATUS"),comission.getValue("ID_BON_CATEGORY"), "BON", true) %></select></td>
			</tr>
			<tr>
				<td>&nbsp;</td> <td>&nbsp;</td>
				<td><%= Bean.card_taskXML.getfieldTransl("id_disc_category", false)  %></td> <td><select name="id_disc_category"  id="id_disc_category" class="inputfield"><%=Bean.getClubCardCategoryForStatusOptions(comission.getValue("ID_CARD_STATUS"),comission.getValue("ID_DISC_CATEGORY"), "DISC", true) %></select></td>
			</tr>

			<% } else if ("OPERATOR-TERMINAL_MANUFACTURER".equalsIgnoreCase(rel_type)) {%>
			<tr>
				<td valign="top"><%= Bean.comissionXML.getfieldTransl("id_term", false) %></td> 
				<td>
					<%=Bean.getWindowFindTerm("term", comission.getValue("ID_TERM"), "8") %>
				</td>			
				<td valign="top"><%= Bean.comissionXML.getfieldTransl("id_card_status", false) %></td> <td><select name="id_card_status" id="id_card_status" class="inputfield" onchange="dwr_get_card_bon_disc_category3('', this, '', '', '<%=Bean.getSessionId() %>');"><%= Bean.getClubCardStatusOptions(comission.getValue("ID_CARD_STATUS"), true) %></select></td>
			</tr>
			<tr>
				<td>&nbsp;</td> <td>&nbsp;</td>
				<td><%= Bean.card_taskXML.getfieldTransl("id_bon_category", false)  %></td> <td><select name="id_bon_category"  id="id_bon_category" class="inputfield"><%=Bean.getClubCardCategoryForStatusOptions(comission.getValue("ID_CARD_STATUS"),comission.getValue("ID_BON_CATEGORY"), "BON", true) %></select></td>
			</tr>
			<tr>
				<td>&nbsp;</td> <td>&nbsp;</td>
				<td><%= Bean.card_taskXML.getfieldTransl("id_disc_category", false)  %></td> <td><select name="id_disc_category"  id="id_disc_category" class="inputfield"><%=Bean.getClubCardCategoryForStatusOptions(comission.getValue("ID_CARD_STATUS"),comission.getValue("ID_DISC_CATEGORY"), "DISC", true) %></select></td>
			</tr>

			<% } else {%>

			<tr>
				<td valign="top"><%= Bean.comissionXML.getfieldTransl("id_card_status", false) %></td> <td><select name="id_card_status" id="id_card_status" class="inputfield" onchange="dwr_get_card_bon_disc_category3('', this, '', '', '<%=Bean.getSessionId() %>');"><%= Bean.getClubCardStatusOptions(comission.getValue("ID_CARD_STATUS"), true) %></select></td>
			</tr>
			<tr>
				<td><%= Bean.card_taskXML.getfieldTransl("id_bon_category", false)  %></td> <td><select name="id_bon_category"  id="id_bon_category" class="inputfield"><%=Bean.getClubCardCategoryForStatusOptions(comission.getValue("ID_CARD_STATUS"),comission.getValue("ID_BON_CATEGORY"), "BON", true) %></select></td>
			</tr>
			<tr>
				<td><%= Bean.card_taskXML.getfieldTransl("id_disc_category", false)  %></td> <td><select name="id_disc_category"  id="id_disc_category" class="inputfield"><%=Bean.getClubCardCategoryForStatusOptions(comission.getValue("ID_CARD_STATUS"),comission.getValue("ID_DISC_CATEGORY"), "DISC", true) %></select></td>
			</tr>
			<% } %>
			<%=	Bean.getCreationAndMoficationRecordFields(
					comission.getValue(Bean.getCreationDateFieldName()),
					comission.getValue("CREATED_BY"),
					comission.getValue(Bean.getLastUpdateDateFieldName()),
					comission.getValue("LAST_UPDATE_BY")
				) %>
			<tr>
				<td colspan="6" align="center">
					<%=Bean.getSubmitButtonAjax("../crm/club/comistypeupdate.jsp") %>
					<%=Bean.getResetButton() %>
					<%=Bean.getGoBackButton("../crm/club/comistypespecs.jsp?id=" + comission.getValue("ID_COMISSION_TYPE")) %>
				</td>
			</tr>
		</table>
		</form>	
		<!-- Скрипт для втавки меню вибору дати -->
		<%= Bean.getCalendarScript("begin_action_date", false) %>
		<%= Bean.getCalendarScript("end_action_date", false) %>


		        <%
		  } else {
	    	%><%= Bean.getUnknownActionText(action) %><%
		}
	  // Применить  
	} else if (process.equalsIgnoreCase("yes")) {
		String
			l_paramCount	= Bean.getDecodeParam(parameters.get("paramcount"));
			
		String 
			id_comission_type	= Bean.getDecodeParam(parameters.get("id_comission_type")),
			fixed_value			= Bean.getDecodeParam(parameters.get("fixed_value")),
			percent_value		= Bean.getDecodeParam(parameters.get("percent_value")),
			begin_action_date	= Bean.getDecodeParam(parameters.get("begin_action_date")),
			end_action_date		= Bean.getDecodeParam(parameters.get("end_action_date")),
			exist_flag			= Bean.getDecodeParam(parameters.get("exist_flag")),
			description			= Bean.getDecodeParam(parameters.get("description")),
			id_doc				= Bean.getDecodeParam(parameters.get("id_doc")),
			id_jur_prs_payer	= Bean.getDecodeParam(parameters.get("id_jur_prs_payer")),
			id_jur_prs_receiver	= Bean.getDecodeParam(parameters.get("id_jur_prs_receiver")),
			id_service_place	= Bean.getDecodeParam(parameters.get("id_service_place")),
			id_term      		= Bean.getDecodeParam(parameters.get("id_term")),
			id_card_status		= Bean.getDecodeParam(parameters.get("id_card_status")),
			id_bon_category		= Bean.getDecodeParam(parameters.get("id_bon_category")),
			id_disc_category	= Bean.getDecodeParam(parameters.get("id_disc_category")),
			id_club_rel			= Bean.getDecodeParam(parameters.get("id_club_rel")),
			id_club				= Bean.getDecodeParam(parameters.get("id_club"));
		  
		if (action.equalsIgnoreCase("addall")) {

			String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_COMISSION.add_all_comis_type_comission(?,?)}";

			String[] pParam = new String [1];
						
			pParam[0] = id;
		
			%>
			<%= Bean.getCallResultParam("UPDATE", callSQL, pParam, "../crm/club/comistypespecs.jsp?id=" + id, "") %>
			<% 	

		} else if (action.equalsIgnoreCase("remove")) {
			
			String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_COMISSION.delete_comission(?,?)}";

			String[] pParam = new String [1];
						
			pParam[0] = id_comission;
		
			%>
			<%= Bean.getCallResultParam("DELETE", callSQL, pParam, "../crm/club/comistypespecs.jsp?id=" + id, "") %>
			<% 	

		} else if (action.equalsIgnoreCase("edit")) {
			  
			String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_COMISSION.update_comission("+
				"?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";

			String[] pParam = new String [18];
					
			pParam[0] = id_comission;
			pParam[1] = id_service_place;
			pParam[2] = id_term;
			pParam[3] = id_card_status;
			pParam[4] = id_bon_category;
			pParam[5] = id_disc_category;
			pParam[6] = id_jur_prs_payer;
			pParam[7] = id_jur_prs_receiver;
			pParam[8] = id_comission_type;
			pParam[9] = fixed_value;
			pParam[10] = percent_value;
			pParam[11] = begin_action_date;
			pParam[12] = end_action_date;
			pParam[13] = exist_flag;
			pParam[14] = description;
			pParam[15] = id_doc;
			pParam[16] = id_club_rel;
			pParam[17] = Bean.getDateFormat();
			
		 	%>
			<%= Bean.getCallResultParam("UPDATE", callSQL, pParam, "../crm/club/comistypespecs.jsp?id=" + id, "") %>
			<% 	
			  
		} else if (action.equalsIgnoreCase("editall")) { 

			String update_sql = "";
			String nameParam = "";
			String fixedValue = "";
			String percentValue = "";
			String fullResult = "0";
			String fullResultMessage = "";
			String callSQL = "";
	 		String resultInt = "";
	 		String resultMessage = "";

	 		String[] results = new String[2];

			String[] pParam = new String [3];
	   		 
	   	    int i;
	   		for (i=1;i<=Integer.parseInt(l_paramCount);i++) {
	   			nameParam		= Bean.getDecodeParam(parameters.get("nameparam"+i));
	   			fixedValue		= Bean.getDecodeParam(parameters.get("fixedvalue"+i));
	   			percentValue	= Bean.getDecodeParam(parameters.get("percentvalue"+i));
	   			
	   			callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_COMISSION.update_comission_values(?,?,?,?)}";
						
				pParam[0] = nameParam;
				pParam[1] = fixedValue;
				pParam[2] = percentValue;
		
				results = Bean.myCallFunctionParam(callSQL, pParam, 2);
 				resultInt = results[0];
 				resultMessage = results[1];
 		
 				%>
				<%= Bean.showCallSQL(callSQL) %>
				<%

				if ("0".equalsIgnoreCase(resultInt)) { 
					fullResult = resultInt;
	   				fullResultMessage = fullResultMessage + ", " + resultMessage;
	   			}
	   		}
	   		
	   		%>
	   	    <%=Bean.showCallResult(
	   	    		callSQL, 
	   	    		fullResult, 
	   	    		fullResultMessage, 
	   	    		"../crm/club/comistypespecs.jsp?id=" + id, 
	   	    		"../crm/club/comistypespecs.jsp?id=" + id, 
	   	    		Bean.form_messageXML.getfieldTransl("save_error", false)) %>
	   		<% 
		 } // кінець умови обробки редагування запису
		  
	} else {%> 
		<%= Bean.getUnknownProcessText(process) %> <%
	}
} else {%> 
	<%= Bean.getUnknownTypeText(type) %> <%
}
  %>

</body>
</html>
