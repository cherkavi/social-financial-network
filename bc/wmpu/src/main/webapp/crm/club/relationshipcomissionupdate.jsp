<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%= Bean.getLogOutScript(request) %>

<%@page import="bc.objects.bcComissionObject"%>
<%@page import="bc.objects.bcClubRelationshipObject"%>
<%@page import="java.util.HashMap"%><html>
<head>
	<%= Bean.getMetaContent() %>
	<%= Bean.getBottomFrameCSS() %>

</head>

<body>
<%
HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());
String pageFormName = "CLUB_RELATIONSHIP";

Bean.setJspPageForTabName(pageFormName);

request.setCharacterEncoding("UTF-8");

String p_id			= Bean.getDecodeParam(parameters.get("id"));
String p_code		= Bean.getDecodeParam(parameters.get("code"));
String p_type		= Bean.getDecodeParam(parameters.get("type"));
String p_action		= Bean.getDecodeParam(parameters.get("action")); 
String p_process	= Bean.getDecodeParam(parameters.get("process"));

if (p_id==null || ("".equalsIgnoreCase(p_id))) p_id="empty";
if (p_type==null || ("".equalsIgnoreCase(p_type))) p_type="empty";
if (p_action==null || ("".equalsIgnoreCase(p_action))) p_action="empty";
if (p_process==null || ("".equalsIgnoreCase(p_process))) p_process="empty";

if (p_type.equalsIgnoreCase("comission")) {
	if (p_process.equalsIgnoreCase("no")) {

		bcClubRelationshipObject rel = new bcClubRelationshipObject("CREATED", p_id);
			
		String rel_type 	= rel.getValue("CD_CLUB_REL_TYPE");

		  %>
		<script>
			var formFixData = new Array (
				new Array ('fixed_value', 'varchar2', 1),
				new Array ('begin_action_date', 'varchar2', 1),
				new Array ('id_comission_type', 'varchar2', 1),
				new Array ('name_jur_prs_receiver', 'varchar2', 1)
			);
			var formPercentData = new Array (
				new Array ('percent_value', 'varchar2', 1),
				new Array ('begin_action_date', 'varchar2', 1),
				new Array ('id_comission_type', 'varchar2', 1),
				new Array ('name_jur_prs_receiver', 'varchar2', 1)
			);

			function myValidateForm() {
				var fixValue = document.getElementById('fixed_value').value;
				var perValue = document.getElementById('percent_value').value;
				if (!fixValue) {
					return validateForm(formPercentData);
				} else {
					return validateForm(formFixData);
				}
			}
		</script>
		<%
	    
		if (p_action.equalsIgnoreCase("add")) {
			  
			%> 
<body>
		<script type="text/javascript">
		  var element_payer=new Array();
		  var element_receiver=new Array();

		  var cd_rel_type = '<%=rel.getValue("CD_CLUB_REL_TYPE")%>';
		  var id_party1 = '<%=rel.getValue("ID_PARTY1")%>';
		  var name_party1 = '<%=rel.getValue("SNAME_PARTY1")%>';
		  var id_party2 = '<%=rel.getValue("ID_PARTY2")%>';
		  var name_party2 = '<%=rel.getValue("SNAME_PARTY2")%>';
		
		  <%=Bean.getClubRelationshipComissionTypeArray(rel_type, rel.getValue("CD_JUR_PRS_PRIMARY_TYPE_PARTY1"), rel.getValue("CD_JUR_PRS_PRIMARY_TYPE_PARTY2"), "element_payer", "element_receiver")%>

		  set_payer_receiver(document.getElementById('id_comission_type'));
		</script>

			<%= Bean.getOperationTitle(
					Bean.comissionXML.getfieldTransl("h_add_comission", false),
					"Y",
					"Y") 
			%>
        <form action="../crm/club/relationshipcomissionupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return myValidateForm()">
	        <input type="hidden" name="type" value="comission">
	        <input type="hidden" name="action" value="add">
	        <input type="hidden" name="process" value="yes">
        	<input type="hidden" name="id" value="<%=p_id %>">
		<table <%=Bean.getTableDetailParam() %>>
			<tr>
				<td><%= Bean.relationshipXML.getfieldTransl("general", false) %></td><td><input type="text" name="name_club_rel" size="90" value="<%=rel.getValue("FULL_NAME_CLUB_REL") %>" readonly="readonly" class="inputfield-ro"></td>
	 		    <td><%= Bean.clubXML.getfieldTransl("club", false) %>
					<%= Bean.getGoToClubLink(rel.getValue("ID_CLUB")) %>
				</td>
				<td>
					<input type="hidden" name="id_club" id="id_club" value="<%= rel.getValue("ID_CLUB") %>">
					<input type="text" name="name_club" id="name_club" size="40" value="<%= Bean.getClubShortName(rel.getValue("ID_CLUB")) %>" readonly="readonly" class="inputfield-ro">
				</td>
			</tr>
			<tr>
				<td><%= Bean.comissionXML.getfieldTransl("name_comission_type", true) %></td><td><select name="id_comission_type" id="id_comission_type" class="inputfield" onchange="set_payer_receiver(this);"><%= Bean.getClubRelationshipComissionOptions(rel_type, "", rel.getValue("CD_JUR_PRS_PRIMARY_TYPE_PARTY1"), rel.getValue("CD_JUR_PRS_PRIMARY_TYPE_PARTY2"), false) %></select></td>
				<td valign="top"><%= Bean.comissionXML.getfieldTransl("fixed_value", false) %></td><td valign="top"><input type="text" name="fixed_value" id="fixed_value" size="16" value="" class="inputfield"></td>
			</tr>
			<tr>
				<td><%= Bean.comissionXML.getfieldTransl("full_doc", false) %></td><td><select name="id_doc" class="inputfield"><%= Bean.getDocForClubRelationshipOptions(p_id, "", false) %></select></td>
				<td><%= Bean.comissionXML.getfieldTransl("percent_value", false) %></td><td valign="top"><input type="text" name="percent_value" id="percent_value" size="16" value="" class="inputfield"></td>
			</tr>
			<tr>
				<td rowspan="3"><%= Bean.comissionXML.getfieldTransl("description", false) %></td><td rowspan="3"><textarea name="description" cols="90" rows="3" class="inputfield"></textarea></td>
				<td><%= Bean.comissionXML.getfieldTransl("begin_action_date", true) %></td> <td><%=Bean.getCalendarInputField("begin_action_date", Bean.getSysDate(), "10") %></td>
			</tr>
			<tr>
				<td><%= Bean.comissionXML.getfieldTransl("end_action_date", false) %></td> <td><%=Bean.getCalendarInputField("end_action_date", "", "10") %></td>
			</tr>
			<tr>
				<td><%= Bean.comissionXML.getfieldTransl("exist_flag", false) %></td><td><select name="exist_flag" class="inputfield"><%= Bean.getMeaningFromLookupNameOptions("YES_NO", "Y", false) %></select></td>
			</tr>
			<tr>
				<td><%= Bean.comissionXML.getfieldTransl("jur_prs_payer", false) %></td>
				<td>
					<input type="hidden" id="id_jur_prs_payer" name="id_jur_prs_payer" value="" readonly="readonly" class="inputfield">
					<input type="text" id="name_jur_prs_payer" name="name_jur_prs_payer" size="90" value="" readonly="readonly" class="inputfield-ro">
				</td>	
			</tr>
			<tr>
				<td><%= Bean.comissionXML.getfieldTransl("jur_prs_receiver", true) %></td>
				<td>
					<input type="hidden" id="id_jur_prs_receiver" name="id_jur_prs_receiver" value="" readonly="readonly" class="inputfield">
					<input type="text" id="name_jur_prs_receiver" name="name_jur_prs_receiver" size="90" value="" readonly="readonly" class="inputfield-ro">
				</td>	
			</tr>
			<tr>
				<td colspan="4" class="top_line"><b><%= Bean.comissionXML.getfieldTransl("t_additional_param", false) %></b></td>
			</tr>
			<% if ("DEALER-TECHNICAL_ACQUIRER".equalsIgnoreCase(rel_type) ||
				   "DEALER-FINANCE_ACQUIRER".equalsIgnoreCase(rel_type)||
				   "OPERATOR-DEALER".equalsIgnoreCase(rel_type)) {%>
			<tr>
				<td><%= Bean.comissionXML.getfieldTransl("name_service_place", false) %> </td>
				<td>
					<%=Bean.getWindowFindServicePlace("service_place", "", "", "40") %>
				</td>			
				<td valign="top"><%= Bean.comissionXML.getfieldTransl("id_card_status", false) %></td> <td><select name="id_card_status" id="id_card_status" class="inputfield" onchange="dwr_get_card_bon_disc_category3('', this, '', '', '<%=Bean.getSessionId() %>');"><%= Bean.getClubCardStatusOptions("", true) %></select></td>
			</tr>
			<tr>
				<td valign="top"><%= Bean.comissionXML.getfieldTransl("id_term", false) %></td> 
				<td>
					<%=Bean.getWindowFindTerm("term", "", "8") %>
				</td>			
				<td><%= Bean.card_taskXML.getfieldTransl("id_bon_category", false)  %></td> <td><select name="id_bon_category"  id="id_bon_category" class="inputfield"><option value=""></option></select></td>
			</tr>
			<tr>
				<td>&nbsp;</td> <td>&nbsp;</td>
				<td><%= Bean.card_taskXML.getfieldTransl("id_disc_category", false)  %></td> <td><select name="id_disc_category"  id="id_disc_category" class="inputfield"><option value=""></option></select></td>
			</tr>

			<% } else if ("OPERATOR-TERMINAL_MANUFACTURER".equalsIgnoreCase(rel_type)) {%>
			<tr>
				<td valign="top"><%= Bean.comissionXML.getfieldTransl("id_term", false) %></td> 
				<td>
					<%=Bean.getWindowFindTerm("term", "", "8") %>
				</td>			
				<td valign="top"><%= Bean.comissionXML.getfieldTransl("id_card_status", false) %></td> <td><select name="id_card_status" id="id_card_status" class="inputfield" onchange="dwr_get_card_bon_disc_category3('', this, '', '', '<%=Bean.getSessionId() %>');"><%= Bean.getClubCardStatusOptions("", true) %></select></td>
			</tr>
			<tr>
				<td>&nbsp;</td> <td>&nbsp;</td>
				<td><%= Bean.card_taskXML.getfieldTransl("id_bon_category", false)  %></td> <td><select name="id_bon_category"  id="id_bon_category" class="inputfield"><option value=""></option></select></td>
			</tr>
			<tr>
				<td>&nbsp;</td> <td>&nbsp;</td>
				<td><%= Bean.card_taskXML.getfieldTransl("id_disc_category", false)  %></td> <td><select name="id_disc_category"  id="id_disc_category" class="inputfield"><option value=""></option></select></td>
			</tr>

			<% } else {%>

			<tr>
				<td valign="top"><%= Bean.comissionXML.getfieldTransl("id_card_status", false) %></td> <td><select name="id_card_status" id="id_card_status" class="inputfield" onchange="dwr_get_card_bon_disc_category3('', this, '', '', '<%=Bean.getSessionId() %>');"><%= Bean.getClubCardStatusOptions("", true) %></select></td>
			</tr>
			<tr>
				<td><%= Bean.card_taskXML.getfieldTransl("id_bon_category", false)  %></td> <td><select name="id_bon_category"  id="id_bon_category" class="inputfield"><option value=""></option></select></td>
			</tr>
			<tr>
				<td><%= Bean.card_taskXML.getfieldTransl("id_disc_category", false)  %></td> <td><select name="id_disc_category"  id="id_disc_category" class="inputfield"><option value=""></option></select></td>
			</tr>
			<% } %>
			<tr>
				<td colspan="6" align="center">
					<%=Bean.getSubmitButtonAjax("../crm/club/relationshipcomissionupdate.jsp") %>
					<%=Bean.getResetButton() %>
					<%=Bean.getGoBackButton("../crm/club/relationshipspecs.jsp?id=" + p_id) %>
				</td>
			</tr>
		</table>
		</form>
		<!-- Скрипт для втавки меню вибору дати -->
		<%= Bean.getCalendarScript("begin_action_date", false) %>
		<%= Bean.getCalendarScript("end_action_date", false) %>

		        <%
		} else if (p_action.equalsIgnoreCase("edit")) {
			  
			bcComissionObject comission = new bcComissionObject(p_code);
			  
			%> 
			<body>
			<%= Bean.getOperationTitle(
					Bean.comissionXML.getfieldTransl("h_edit_comission", false),
					"Y",
					"Y") 
			%>
        <form action="../crm/club/relationshipcomissionupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return myValidateForm()">
	        <input type="hidden" name="type" value="comission">
	        <input type="hidden" name="action" value="edit">
	        <input type="hidden" name="process" value="yes">
        	<input type="hidden" name="id" value="<%=p_id %>">
        	<input type="hidden" name="code" value="<%=p_code %>">
        	<input type="hidden" name="id_comission_type" value="<%=comission.getValue("ID_COMISSION_TYPE") %>">
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
				<td><%= Bean.comissionXML.getfieldTransl("name_comission_type", true) %></td><td><input type="text" name="name_comission_type" size="90" value="<%= Bean.getComissionTypeName(comission.getValue("ID_COMISSION_TYPE")) %>" readonly="readonly" class="inputfield-ro"></td>
				<td><%= Bean.comissionXML.getfieldTransl("fixed_value", false) %></td><td><input type="text" name="fixed_value" id="fixed_value" size="16" value="<%=comission.getValue("FIXED_VALUE_FRMT") %>" class="inputfield"></td>
			</tr>
			<tr>
				<td><%= Bean.comissionXML.getfieldTransl("full_doc", false) %>
					<%= Bean.getGoToDocLink(comission.getValue("ID_DOC")) %>
				</td><td><select name="id_doc" class="inputfield"><%= Bean.getDocForClubRelationshipOptions(p_id, comission.getValue("ID_DOC"), true) %></select></td>
				<td valign="top"><%= Bean.comissionXML.getfieldTransl("percent_value", false) %></td><td valign="top"><input type="text" name="percent_value" id="percent_value" size="16" value="<%=comission.getValue("PERCENT_VALUE_FRMT") %>" class="inputfield"></td>
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
				<td><%= Bean.comissionXML.getfieldTransl("name_service_place", false) %> </td>
				<td>
					<%=Bean.getWindowFindServicePlace("service_place", comission.getValue("ID_SERVICE_PLACE"), "", "40") %>
				</td>			
				<td valign="top"><%= Bean.comissionXML.getfieldTransl("id_card_status", false) %></td> <td><select name="id_card_status" id="id_card_status" class="inputfield" onchange="dwr_get_card_bon_disc_category3('', this, '', '', '<%=Bean.getSessionId() %>');"><%= Bean.getClubCardStatusOptions(comission.getValue("ID_CARD_STATUS"), true) %></select></td>
			</tr>
			<tr>
				<td valign="top"><%= Bean.comissionXML.getfieldTransl("id_term", false) %></td> 
				<td>
					<%=Bean.getWindowFindTerm("term", comission.getValue("ID_TERM"), "8") %>
				</td>			
				<td><%= Bean.card_taskXML.getfieldTransl("id_bon_category", false)  %></td> <td><select name="id_bon_category"  id="id_bon_category" class="inputfield"><%=Bean.getClubCardCategoryForStatusOptions(comission.getValue("ID_CARD_STATUS"), comission.getValue("ID_BON_CATEGORY"), "BON", true) %></select></td>
			</tr>
			<tr>
				<td>&nbsp;</td> <td>&nbsp;</td>
				<td><%= Bean.card_taskXML.getfieldTransl("id_disc_category", false)  %></td> <td><select name="id_disc_category"  id="id_disc_category" class="inputfield"><%=Bean.getClubCardCategoryForStatusOptions(comission.getValue("ID_CARD_STATUS"), comission.getValue("ID_DISC_CATEGORY"), "DISC", true) %></select></td>
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
				<td><%= Bean.card_taskXML.getfieldTransl("id_bon_category", false)  %></td> <td><select name="id_bon_category"  id="id_bon_category" class="inputfield"><%=Bean.getClubCardCategoryForStatusOptions(comission.getValue("ID_CARD_STATUS"), comission.getValue("ID_BON_CATEGORY"), "BON", true) %></select></td>
			</tr>
			<tr>
				<td>&nbsp;</td> <td>&nbsp;</td>
				<td><%= Bean.card_taskXML.getfieldTransl("id_disc_category", false)  %></td> <td><select name="id_disc_category"  id="id_disc_category" class="inputfield"><%=Bean.getClubCardCategoryForStatusOptions(comission.getValue("ID_CARD_STATUS"), comission.getValue("ID_DISC_CATEGORY"), "DISC", true) %></select></td>
			</tr>

			<% } else {%>

			<tr>
				<td valign="top"><%= Bean.comissionXML.getfieldTransl("id_card_status", false) %></td> <td><select name="id_card_status" id="id_card_status" class="inputfield" onchange="dwr_get_card_bon_disc_category3('', this, '', '', '<%=Bean.getSessionId() %>');"><%= Bean.getClubCardStatusOptions(comission.getValue("ID_CARD_STATUS"), true) %></select></td>
			</tr>
			<tr>
				<td><%= Bean.card_taskXML.getfieldTransl("id_bon_category", false)  %></td> <td><select name="id_bon_category"  id="id_bon_category" class="inputfield"><%=Bean.getClubCardCategoryForStatusOptions(comission.getValue("ID_CARD_STATUS"), comission.getValue("ID_BON_CATEGORY"), "BON", true) %></select></td>
			</tr>
			<tr>
				<td><%= Bean.card_taskXML.getfieldTransl("id_disc_category", false)  %></td> <td><select name="id_disc_category"  id="id_disc_category" class="inputfield"><%=Bean.getClubCardCategoryForStatusOptions(comission.getValue("ID_CARD_STATUS"), comission.getValue("ID_DISC_CATEGORY"), "DISC", true) %></select></td>
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
					<%=Bean.getSubmitButtonAjax("../crm/club/relationshipcomissionupdate.jsp") %>
					<%=Bean.getResetButton() %>
					<%=Bean.getGoBackButton("../crm/club/relationshipspecs.jsp?id=" + p_id) %>
				</td>
			</tr>
		</table>
		</form>	
		<!-- Скрипт для втавки меню вибору дати -->
		<%= Bean.getCalendarScript("begin_action_date", false) %>
		<%= Bean.getCalendarScript("end_action_date", false) %>

		        <%
		  } else {
	    	%><%= Bean.getUnknownActionText(p_action) %><%
		}
	  // Применить  
	} else if (p_process.equalsIgnoreCase("yes")) { %>
<body>
		
	<%
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
			id_club				= Bean.getDecodeParam(parameters.get("id_club"));
	  
		if (p_action.equalsIgnoreCase("add")) {
		
			String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_COMISSION.add_comission("+
				"?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";

			String[] pParam = new String [18];
							
			pParam[0] = id_service_place;
			pParam[1] = id_term;
			pParam[2] = id_card_status;
			pParam[3] = id_bon_category;
			pParam[4] = id_disc_category;
			pParam[5] = id_jur_prs_payer;
			pParam[6] = id_jur_prs_receiver;
			pParam[7] = id_comission_type;
			pParam[8] = fixed_value;
			pParam[9] = percent_value;
			pParam[10] = begin_action_date;
			pParam[11] = end_action_date;
			pParam[12] = exist_flag;
			pParam[13] = description;
			pParam[14] = id_doc;
			pParam[15] = id_club;
			pParam[16] = p_id;
			pParam[17] = Bean.getDateFormat();
		
			%>
			<%= Bean.getCallResultParam("INSERT", callSQL, pParam, "../crm/club/relationshipspecs.jsp?id=" + p_id + "&id_comission=", "") %>
			<% 	
		  
		} else if (p_action.equalsIgnoreCase("addall")) {
			
			String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_COMISSION.add_all_club_rel_comission(?,?)}";

			String[] pParam = new String [1];
					
			pParam[0] = p_id;
	
			%>
			<%= Bean.getCallResultParam("UPDATE", callSQL, pParam, "../crm/club/relationshipspecs.jsp?id=" + p_id, "") %>
			<% 	
		  
		} else if (p_action.equalsIgnoreCase("remove")) {
			
			String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_COMISSION.delete_comission(?,?)}";

			String[] pParam = new String [1];
						
			pParam[0] = p_code;
	
			%>
			<%= Bean.getCallResultParam("DELETE", callSQL, pParam, "../crm/club/relationshipspecs.jsp?id=" + p_id, "") %>
			<% 	
		  
		} else if (p_action.equalsIgnoreCase("removeall")) {
			
			String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_COMISSION.delete_all_club_rel_comission(?,?)}";

			String[] pParam = new String [1];
							
			pParam[0] = p_id;
	
			%>
			<%= Bean.getCallResultParam("DELETE", callSQL, pParam, "../crm/club/relationshipspecs.jsp?id=" + p_id, "") %>
			<% 	
		  
		} else if (p_action.equalsIgnoreCase("edit")) {
			
			String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_COMISSION.update_comission("+
				"?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";

			String[] pParam = new String [18];
							
			pParam[0] = p_code;
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
			pParam[16] = p_id;
			pParam[17] = Bean.getDateFormat();
		
			%>
			<%= Bean.getCallResultParam("UPDATE", callSQL, pParam, "../crm/club/relationshipspecs.jsp?id=" + p_id, "") %>
			<% 	
		  
		} else if (p_action.equalsIgnoreCase("editall")) { %>

			<%= Bean.form_messageXML.getfieldTransl("processing_update", false) %>
			<%

			String update_sql = "";
			String nameParam = "";
			String fixedValue = "";
			String percentValue = "";
			String fullResult = "0";
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

				if (!("0".equalsIgnoreCase(resultInt))) { 
   					fullResult = fullResult + ", " + resultMessage;
   				}
   			}
   	   
	   		%>
	   	    <%=Bean.showCallResult(
	   	    		callSQL, 
	   	    		fullResult, 
	   	    		fullResult, 
	   	    		"../crm/club/relationshipspecs.jsp?id=" + p_id, 
	   	    		"../crm/club/relationshipspecs.jsp?id=" + p_id, 
	   	    		Bean.form_messageXML.getfieldTransl("save_error", false)) %>
	   		<% 
	       
		} // кінець умови обробки редагування запису
	}
}	      
%>
</body>
</html>
