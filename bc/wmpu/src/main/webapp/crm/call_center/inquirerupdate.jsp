<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%= Bean.getLogOutScript(request) %>

<%@page import="java.util.HashMap"%>
<%@page import="bc.objects.bcClubShortObject"%>


<%@page import="bc.objects.bcCallCenterInquirerObject"%>

<%@page import="bc.objects.bcCallCenterInquirerLineObject"%>
<%@page import="bc.objects.bcCallCenterCallGroupObject"%>
<%@page import="bc.objects.bcCallCenterQuestionObject"%>
<%@page import="bc.objects.bcNatPrsObject"%>
<%@page import="bc.objects.bcClubCardObject"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Set"%>
<%@page import="java.util.Iterator"%>
<%@page import="bc.objects.bcClubCardPurseObject"%><html>
<head>
	<%= Bean.getMetaContent() %>
	<%= Bean.getBottomFrameCSS() %>

</head>

<body>
<%
HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String pageFormName = "CALL_CENTER_INQUIRER";

Bean.setJspPageForTabName(pageFormName);

request.setCharacterEncoding("UTF-8");

String type		= Bean.getDecodeParam(parameters.get("type"));
String id		= Bean.getDecodeParam(parameters.get("id"));
String action	= Bean.getDecodeParam(parameters.get("action")); 
String process	= Bean.getDecodeParam(parameters.get("process"));

if (id==null || ("".equalsIgnoreCase(id))) id="empty";
if (action==null || ("".equalsIgnoreCase(action))) action="empty";
if (process==null || ("".equalsIgnoreCase(process))) process="empty";
if (type==null || ("".equalsIgnoreCase(type))) type="empty";

if (type.equalsIgnoreCase("general")) {
	if (process.equalsIgnoreCase("no"))
	/* вибираємо тип дії (добавити, видалити...)*/
	{
	   /*  --- Добавити запис --- */
    	if (action.equalsIgnoreCase("add")|| action.equalsIgnoreCase("add2")) {
    	
    		bcClubShortObject club = new bcClubShortObject(Bean.getCurrentClubID());
    	%>
	<script>
		var formDataInquirer = new Array (
			new Array ('name_cc_inquirer', 'varchar2', 1),
			new Array ('cd_cc_inquirer_state', 'varchar2', 1),
			new Array ('date_cc_inquirer', 'varchar2', 1)
		);
		function myValidateForm() {
			return validateForm(formDataInquirer);
		}
	</script> 
		<%= Bean.getOperationTitle(
				Bean.call_centerXML.getfieldTransl("h_add_inquirer", false),
				"Y",
				"Y") 
		%>
    <form action="../crm/call_center/inquirerupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return myValidateForm()">
	        <input type="hidden" name="type" value="general">
	        <input type="hidden" name="action" value="add">
	        <input type="hidden" name="process" value="yes">
	<table <%=Bean.getTableDetailParam() %>>
		<tr>
			<td><%= Bean.clubXML.getfieldTransl("club", true) %>
			<%= Bean.getGoToClubLink(club.getValue("ID_CLUB")) %>
		  	</td>
		  	<td>
				<%=Bean.getWindowFindClub("club", club.getValue("ID_CLUB"), club.getValue("SNAME_CLUB"), "25") %>
		  	</td>
			<td colspan="2">&nbsp;</td>
		</tr>
		<tr>
			<td><%= Bean.call_centerXML.getfieldTransl("name_cc_inquirer", true) %></td><td><input type="text" name="name_cc_inquirer" size="40" value="" class="inputfield"></td>
			<td colspan="2">&nbsp;</td>
		</tr>
		<tr>
			<td><%= Bean.call_centerXML.getfieldTransl("cd_cc_inquirer_state", true) %></td><td><select name="cd_cc_inquirer_state" class="inputfield"><%= Bean.getCallCenterInquirerStateOptions("CONSTUCTION", false) %></select></td>
			<td colspan="2">&nbsp;</td>
		</tr>			
		<tr>
		  	<td><%= Bean.call_centerXML.getfieldTransl("date_cc_inquirer", true) %></td><td><%=Bean.getCalendarInputField("date_cc_inquirer", Bean.getSysDate(), "10") %></td>
			<td colspan="2">&nbsp;</td>
		</tr>			
		<tr>
			<td><%= Bean.call_centerXML.getfieldTransl("desc_cc_inquirer", false) %></td><td  colspan="3"><textarea name="desc_cc_inquirer" cols="100" rows="3" class="inputfield"></textarea></td>
		</tr>
		<tr>
			<td colspan="4" align="center">
				<%=Bean.getSubmitButtonAjax("../crm/call_center/inquirerupdate.jsp") %>
				<%=Bean.getResetButton() %>
				<% if (action.equalsIgnoreCase("add")) { %>
					<%=Bean.getGoBackButton("../crm/call_center/inquirer.jsp") %>
				<% } else { %>
					<%=Bean.getGoBackButton("../crm/call_center/inquirerspecs.jsp?id=" + id) %>
				<% } %>
			</td>
		</tr>

	</table>
	

	</form>
	<%= Bean.getCalendarScript("date_cc_inquirer", false) %>

	        <%
    	} else {
    	    %> <%= Bean.getUnknownActionText(action) %><%
    	}
	}

	else if (process.equalsIgnoreCase("yes")){
    	String
    		name_cc_inquirer 		= Bean.getDecodeParam(parameters.get("name_cc_inquirer")),
    		cd_cc_inquirer_state 	= Bean.getDecodeParam(parameters.get("cd_cc_inquirer_state")),
    		date_cc_inquirer 		= Bean.getDecodeParam(parameters.get("date_cc_inquirer")),
    		desc_cc_inquirer 		= Bean.getDecodeParam(parameters.get("desc_cc_inquirer")),
    		id_club		 			= Bean.getDecodeParam(parameters.get("id_club"));


		if (action.equalsIgnoreCase("add")) { 

			String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_CALL_CENTER.add_inquirer("+
				"?,?,?,?,?,?,?,?)}";

			ArrayList<String> pParam = new ArrayList<String>();
					
			pParam.add(name_cc_inquirer);
			pParam.add(cd_cc_inquirer_state);
			pParam.add(date_cc_inquirer);
			pParam.add(desc_cc_inquirer);
			pParam.add(id_club);
			pParam.add(Bean.getDateFormat());
		
			%>
			<%= Bean.getCallResultParam("INSERT", callSQL, pParam, "../crm/call_center/inquirerspecs.jsp?id=", "../crm/call_center/inquirer.jsp") %>
			<% 	

		} else if (action.equalsIgnoreCase("remove")) { 

		   	String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_CALL_CENTER.delete_inquirer(?,?)}";

		   	ArrayList<String> pParam = new ArrayList<String>();
						
			pParam.add(id);

			%>
			<%= Bean.getCallResultParam("DELETE", callSQL, pParam, "../crm/call_center/inquirer.jsp", "") %>
			<%
		
		} else if (action.equalsIgnoreCase("edit")) { 
		
	 		String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_CALL_CENTER.update_inquirer(" + 
	 			"?,?,?,?,?,?,?)}";

	 		ArrayList<String> pParam = new ArrayList<String>();
						
			pParam.add(id);
			pParam.add(name_cc_inquirer);
			pParam.add(cd_cc_inquirer_state);
			pParam.add(date_cc_inquirer);
			pParam.add(desc_cc_inquirer);
			pParam.add(Bean.getDateFormat());
		
			%>
			<%= Bean.getCallResultParam("UPDATE", callSQL, pParam, "../crm/call_center/inquirerspecs.jsp?id=" + id, "") %>
			<% 	
		} else { %> 
			<%= Bean.getUnknownActionText(action) %><% 
		}
	} else {%> 
		<%= Bean.getUnknownProcessText(process) %> <%
	}
} else if (type.equalsIgnoreCase("line")) {
	
	String id_cc_inquirer_line			= Bean.getDecodeParam(parameters.get("id_line"));
	
	bcCallCenterInquirerObject inquirer = new bcCallCenterInquirerObject(id);
	
	if (process.equalsIgnoreCase("no"))	{
		%>
		<script>
			var formData = new Array (
				new Array ('order_number', 'varchar2', 1),
				new Array ('cd_cc_inquirer_line_type', 'varchar2', 1),
				new Array ('question_cc_inquirer_line', 'varchar2', 1),
				new Array ('values_cc_inquirer_line', 'varchar2', 1)
			);
			function myValidateForm() {
				return validateForm(formData);
			}
		</script>

	  <%
	   	if (action.equalsIgnoreCase("add")) {
			%>
			<%= Bean.getOperationTitle(
					Bean.call_centerXML.getfieldTransl("h_add_inquirer_line", false),
					"Y",
					"N") 
			%>
	        <form action="../crm/call_center/inquirerupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">
		        <input type="hidden" name="type" value="line">
		        <input type="hidden" name="action" value="add">
		        <input type="hidden" name="process" value="yes">
        		<input type="hidden" name="id" value="<%= id %>">
			<table <%=Bean.getTableDetailParam() %>>
				<tr>
					<td><%= Bean.call_centerXML.getfieldTransl("name_cc_inquirer", false) %> </td><td><input type="text" name="name_cc_inquirer" size="70" value="<%= inquirer.getValue("NAME_CC_INQUIRER") %>" readonly="readonly" class="inputfield-ro"></td>
					<td colspan="2">&nbsp;</td>
				</tr>
		        <tr>
					<td><%= Bean.call_centerXML.getfieldTransl("order_number", true) %></td><td><input type="text" name="order_number" size="10" value="" class="inputfield"></td>
					<td colspan="2">&nbsp;</td>
				</tr>
		        <tr>
					<td><%= Bean.call_centerXML.getfieldTransl("cd_cc_inquirer_line_type", true) %></td><td><select name="cd_cc_inquirer_line_type" class="inputfield"><%= Bean.getCallCenterInquirerLineTypeOptions("QUESTION", false) %></select></td>
					<td colspan="2">&nbsp;</td>
				</tr>
		        <tr>
					<td><%= Bean.call_centerXML.getfieldTransl("question_cc_inquirer_line", true) %></td><td><textarea name="question_cc_inquirer_line" cols="66" rows="3" class="inputfield"></textarea></td>
					<td colspan="2">&nbsp;</td>
				</tr>
		        <tr>
					<td><%= Bean.call_centerXML.getfieldTransl("desc_cc_inquirer_line", false) %></td><td><textarea name="desc_cc_inquirer_line" cols="66" rows="3" class="inputfield"></textarea></td>
					<td colspan="2">&nbsp;</td>
				</tr>
		        <tr>
					<td><%= Bean.call_centerXML.getfieldTransl("values_cc_inquirer_line", true) %></td><td><textarea name="values_cc_inquirer_line" cols="66" rows="3" class="inputfield"></textarea></td>
					<td colspan="2">&nbsp;</td>
				</tr>
		        <tr>
					<td><%= Bean.call_centerXML.getfieldTransl("multy_choice", true) %></td><td><select name="multy_choice" class="inputfield"><%= Bean.getYesNoLookupOptions("N", false) %></select></td>
					<td colspan="2">&nbsp;</td>
				</tr>
				<tr>
					<td colspan="6" align="center">
						<%=Bean.getSubmitButtonAjax("../crm/call_center/inquirerupdate.jsp") %>
						<%=Bean.getResetButton() %>
						<%=Bean.getGoBackButton("../crm/call_center/inquirerspecs.jsp?id=" + id) %>
					</td>
				</tr>
			</table>
			</form>
			<%
	   	} else if (action.equalsIgnoreCase("edit")) {
    		
	   		bcCallCenterInquirerLineObject line = new bcCallCenterInquirerLineObject(id_cc_inquirer_line);
		    
		    %> 

			<%= Bean.getOperationTitle(
					Bean.call_centerXML.getfieldTransl("h_update_inquirer_line", false),
					"Y",
					"N") 
			%>
	        <form action="../crm/call_center/inquirerupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">
		        <input type="hidden" name="type" value="line">
		        <input type="hidden" name="action" value="edit">
		        <input type="hidden" name="process" value="yes">
		        <input type="hidden" name="id" value="<%= id %>">
		        <input type="hidden" name="id_line" value="<%= id_cc_inquirer_line %>">
			<table <%=Bean.getTableDetailParam() %>>
				<tr>
					<td><%= Bean.call_centerXML.getfieldTransl("name_cc_inquirer", false) %> </td><td><input type="text" name="name_cc_inquirer" size="70" value="<%= inquirer.getValue("NAME_CC_INQUIRER") %>" readonly="readonly" class="inputfield-ro"></td>
					<td colspan="2">&nbsp;</td>
				</tr>
		        <tr>
					<td><%= Bean.call_centerXML.getfieldTransl("order_number", true) %></td><td><input type="text" name="order_number" size="10" value="<%= line.getValue("ORDER_NUMBER") %>" class="inputfield"></td>
					<td colspan="2">&nbsp;</td>
				</tr>
		        <tr>
					<td><%= Bean.call_centerXML.getfieldTransl("cd_cc_inquirer_line_type", true) %></td><td><select name="cd_cc_inquirer_line_type" class="inputfield"><%= Bean.getCallCenterInquirerLineTypeOptions(line.getValue("CD_CC_INQUIRER_LINE_TYPE"), false) %></select></td>
					<td colspan="2">&nbsp;</td>
				</tr>
		        <tr>
					<td><%= Bean.call_centerXML.getfieldTransl("question_cc_inquirer_line", true) %></td><td><textarea name="question_cc_inquirer_line" cols="66" rows="3" class="inputfield"><%= line.getValue("QUESTION_CC_INQUIRER_LINE") %></textarea></td>
					<td colspan="2">&nbsp;</td>
				</tr>
		        <tr>
					<td><%= Bean.call_centerXML.getfieldTransl("desc_cc_inquirer_line", false) %></td><td><textarea name="desc_cc_inquirer_line" cols="66" rows="3" class="inputfield"><%= line.getValue("DESC_CC_INQUIRER_LINE") %></textarea></td>
					<td colspan="2">&nbsp;</td>
				</tr>
		        <tr>
					<td><%= Bean.call_centerXML.getfieldTransl("values_cc_inquirer_line", true) %></td><td><textarea name="values_cc_inquirer_line" cols="66" rows="3" class="inputfield"><%= line.getValue("VALUES_CC_INQUIRER_LINE") %></textarea></td>
					<td colspan="2">&nbsp;</td>
				</tr>
		        <tr>
					<td><%= Bean.call_centerXML.getfieldTransl("multy_choice", true) %></td><td><select name="multy_choice" class="inputfield"><%= Bean.getYesNoLookupOptions(line.getValue("MULTY_CHOICE"), false) %></select></td>
					<td colspan="2">&nbsp;</td>
				</tr>
				<%=	Bean.getCreationAndMoficationRecordFieldsOneColl(
						line.getValue(Bean.getCreationDateFieldName()),
						line.getValue("CREATED_BY"),
						line.getValue(Bean.getLastUpdateDateFieldName()),
						line.getValue("LAST_UPDATE_BY")
					) %>
				<tr>
					<td colspan="6" align="center">
						<%=Bean.getSubmitButtonAjax("../crm/call_center/inquirerupdate.jsp") %>
						<%=Bean.getResetButton() %>
						<%=Bean.getGoBackButton("../crm/call_center/inquirerspecs.jsp?id=" + id) %>
					</td>
				</tr>
			</table>

			</form>
			
			<br><%
		} else {
		    %> <%= Bean.getUnknownActionText(action) %><%
	   	}
	} else if (process.equalsIgnoreCase("yes"))	{
		String
			question_cc_inquirer_line	= Bean.getDecodeParam(parameters.get("question_cc_inquirer_line")),
			desc_cc_inquirer_line		= Bean.getDecodeParam(parameters.get("desc_cc_inquirer_line")),
			cd_cc_inquirer_line_type	= Bean.getDecodeParam(parameters.get("cd_cc_inquirer_line_type")),
			order_number				= Bean.getDecodeParam(parameters.get("order_number")),
			values_cc_inquirer_line		= Bean.getDecodeParam(parameters.get("values_cc_inquirer_line")),
			multy_choice				= Bean.getDecodeParam(parameters.get("multy_choice"));
		if (action.equalsIgnoreCase("add")) { 
			
			String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_CALL_CENTER.add_inquirer_line("+
				"?,?,?,?,?,?,?,?,?)}";

			ArrayList<String> pParam = new ArrayList<String>();
							
			pParam.add(id);
			pParam.add(order_number);
			pParam.add(cd_cc_inquirer_line_type);
			pParam.add(question_cc_inquirer_line);
			pParam.add(desc_cc_inquirer_line);
			pParam.add(values_cc_inquirer_line);
			pParam.add(multy_choice);
	
		 	%>
			<%= Bean.getCallResultParam("INSERT", callSQL, pParam, "../crm/call_center/inquirerspecs.jsp?id=" + id + "&id_club_event_gift=", "") %>
			<% 	
		   
		} else if (action.equalsIgnoreCase("remove")) {
			   
			String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_CALL_CENTER.delete_inquirer_line(?,?)}";

			ArrayList<String> pParam = new ArrayList<String>();
							
			pParam.add(id_cc_inquirer_line);

		 	%>
			<%= Bean.getCallResultParam("DELETE", callSQL, pParam, "../crm/call_center/inquirerspecs.jsp?id=" + id, "") %>
			<% 	
			     
		} else if (action.equalsIgnoreCase("edit")) { 
			
			String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_CALL_CENTER.update_inquirer_line(" +
				"?,?,?,?,?,?,?,?)}";

			ArrayList<String> pParam = new ArrayList<String>();
								
			pParam.add(id_cc_inquirer_line);
			pParam.add(order_number);
			pParam.add(cd_cc_inquirer_line_type);
			pParam.add(question_cc_inquirer_line);
			pParam.add(desc_cc_inquirer_line);
			pParam.add(values_cc_inquirer_line);
			pParam.add(multy_choice);
	
		 	%>
			<%= Bean.getCallResultParam("UPDATE", callSQL, pParam, "../crm/call_center/inquirerspecs.jsp?id=" + id, "") %>
			<% 	
	    } else { %> 
	    	<%= Bean.getUnknownActionText(action) %><% 
		}
	}
} else if (type.equalsIgnoreCase("CLIENT")) {
	if (process.equalsIgnoreCase("no"))	{
	   	if (action.equalsIgnoreCase("edit")) {
	   		
	   		boolean hasEditPermission = false;
	   		int hasTabSheetPermission = Bean.currentMenu.isEditPermited("CALL_CENTER_INQUIRER_CLIENTS");
	   		if (hasTabSheetPermission > 0) {
	   			hasEditPermission = true;
	   		}
			
	   		String	id_activity 				= Bean.getDecodeParam(parameters.get("id_activity"));
	   		
	   		bcCallCenterQuestionObject question = new bcCallCenterQuestionObject(id_activity);
			
			bcCallCenterCallGroupObject group = new bcCallCenterCallGroupObject(question.getValue("ID_CC_CALL_GROUP"));
			
			bcNatPrsObject natprs = new bcNatPrsObject(question.getValue("ID_NAT_PRS"));
			
			bcClubCardObject clubcard = new bcClubCardObject(question.getValue("CARD_SERIAL_NUMBER"), question.getValue("CARD_ID_ISSUER"), question.getValue("CARD_ID_PAYMENT_SYSTEM"), true);
			
			bcClubCardPurseObject superbon = new bcClubCardPurseObject(question.getValue("CARD_SERIAL_NUMBER"), question.getValue("CARD_ID_ISSUER"), question.getValue("CARD_ID_PAYMENT_SYSTEM"), "SUPERBON");
	
	%>

	<%= Bean.getOperationTitle(
				Bean.call_centerXML.getfieldTransl("h_edit_call", false),
				"N",
				"N") 
		%>
	<% if (hasEditPermission) { %>
	<form action="../crm/call_center/inquirerupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return myValidateForm()">
	        <input type="hidden" name="type" value="client">
	        <input type="hidden" name="action" value="edit">
	        <input type="hidden" name="process" value="yes">
	        <input type="hidden" name="id" value="<%=id %>">
	        <input type="hidden" name="id_cc_question" value="<%=question.getValue("ID_CC_QUESTION")%>">
	<% } %>
	<table <%=Bean.getTableMenuFilter() %>>
		
		<tr>
			<td><%= Bean.call_centerXML.getfieldTransl("name_cc_call_group", false) %>
				<%= Bean.getGoToCallCenterCallGroupLink(group.getValue("ID_CC_CALL_GROUP")) %>
			</td><td><input type="text" name="name_cc_call_group" size="40" value="<%= group.getValue("NAME_CC_CALL_GROUP") %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.getClubCardXMLFieldTransl("name_nat_prs", false) %>
				<%= Bean.getGoToNatPrsLink(question.getValue("ID_NAT_PRS")) %>
		    </td><td><input type="text" name="name_nat_prs" size="40" value="<%= Bean.getNatPrsName(question.getValue("ID_NAT_PRS")) %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td><%= Bean.call_centerXML.getfieldTransl("date_cc_call_group", false) %></td><td><input type="text" name="name_cc_call_group" size="10" value="<%= group.getValue("DATE_CC_CALL_GROUP_FRMT") %>" readonly="readonly" class="inputfield-ro"></td>
		  	<td><%= Bean.natprsXML.getfieldTransl("date_of_birth", false) %> </td><td><input type="text" name="date_of_birth" size="10" value="<%= natprs.getValue("DATE_OF_BIRTH_FRMT") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>			
		<tr>
			<td><%= Bean.call_centerXML.getfieldTransl("name_cc_inquirer", false) %>
				<%= Bean.getGoToCallCenterInquirerLink(group.getValue("ID_CC_INQUIRER")) %>
			</td>
			<td>
				<input type="text" name="name_cc_inquirer" size="40" value="<%= group.getValue("NAME_CC_INQUIRER") %>" readonly="readonly" class="inputfield-ro">
			</td>
		    <td><%= Bean.natprsXML.getfieldTransl("phone_mobile", false) %></td><td><input type="text" name="phone_mobile" size="30" value="<%= natprs.getValue("PHONE_MOBILE") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>			
		<tr>
			<td colspan="2"><b><%= Bean.call_centerXML.getfieldTransl("cg_criterion", false) %></b></td>
			<% String cd_card1 = Bean.getClubCardCode(question.getValue("CARD_SERIAL_NUMBER")+"_"+question.getValue("CARD_ID_ISSUER")+"_"+question.getValue("CARD_ID_PAYMENT_SYSTEM")); %>
			<td valign="top" style="border-top:solid windowtext 1.0pt;"><%= Bean.getClubCardXMLFieldTransl("cd_card1", false) %>
				<%= Bean.getGoToClubCardLink(
						question.getValue("CARD_SERIAL_NUMBER"),
						question.getValue("CARD_ID_ISSUER"),
						question.getValue("CARD_ID_PAYMENT_SYSTEM")
					) %>
			</td><td style="border-top:solid windowtext 1.0pt;"><input type="text" name="cd_card1" size="30" value="<%= cd_card1 %>" readonly="readonly" class="inputfield-ro"></td>			
		<tr>
		  	<td><%= Bean.call_centerXML.getfieldTransl("cg_crit_begin_purchase_date", false) %></td><td><input type="text" name="cg_crit_begin_purchase_date" size="10" value="<%= group.getValue("CRIT_BEGIN_PURCHASE_DATE_DF") %>" readonly="readonly" class="inputfield-ro"></td>
			<%  String goToJurPrsHyperLink = Bean.getGoToJurPrsHyperLink(clubcard.getValue("ID_JUR_PRS_WHO_CARD_SOLD"));%>
		  	<td><%= Bean.getClubCardXMLFieldTransl("sname_jur_pr_who_has_sold_card", false) %>
				<%= goToJurPrsHyperLink %></td><td><input type="text" name="id_jur_pr_who_has_sold_card" size="30" value="<%= Bean.getJurPersonShortName(clubcard.getValue("ID_JUR_PRS_WHO_CARD_SOLD")) %>" readonly="readonly" class="inputfield-ro">
			</td>
		</tr>			
		<tr>
		    <td><%= Bean.call_centerXML.getfieldTransl("cg_crit_give_for_event", false) %>
				<%= Bean.getGoToClubEventLink(group.getValue("CRIT_GIVE_FOR_EVENT")) %>
			</td>
		  	<td>
				<input type="text" name="crit_give_for_event" size="40" value="<%= Bean.getClubActionName(group.getValue("CRIT_GIVE_FOR_EVENT")) %>" readonly="readonly" class="inputfield-ro">
			</td>	
			<%  String goToJurPrsHyperLink2 = Bean.getGoToJurPrsHyperLink(clubcard.getValue("ID_JUR_PRS_WHERE_CARD_SOLD"));	%>
			  <td><%= Bean.getClubCardXMLFieldTransl("sname_jur_prs_where_card_sold", false) %>
				<%= goToJurPrsHyperLink2 %></td><td><input type="text" name="id_jur_prs_where_card_sold" size="30" value="<%= Bean.getJurPersonShortName(clubcard.getValue("ID_JUR_PRS_WHERE_CARD_SOLD")) %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
		  	<td><%= Bean.call_centerXML.getfieldTransl("cg_crit_given_from_date", false) %></td><td><input type="text" name="cg_crit_given_from_date" size="10" value="<%= group.getValue("CRIT_GIVEN_FROM_DATE_DF") %>" readonly="readonly" class="inputfield-ro">&nbsp;<%= Bean.call_centerXML.getfieldTransl("cg_crit_given_to_date", false) %>&nbsp;<input type="text" name="cg_crit_given_to_date" size="10" value="<%= group.getValue("CRIT_GIVEN_TO_DATE_DF") %>" readonly="readonly" class="inputfield-ro"></td>
			 <td><%= Bean.getClubCardXMLFieldTransl("name_serv_plce_where_card_sold", false) %>
				<%= Bean.getGoToServicePlaceLink(clubcard.getValue("ID_SERV_PLACE_WHERE_CARD_SOLD")) %>
			  </td><td><input type="text" id="name_service_place" name="name_service_place" size="30" value="<%= Bean.getServicePlaceName(clubcard.getValue("ID_SERV_PLACE_WHERE_CARD_SOLD")) %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
		  	<td><%= Bean.call_centerXML.getfieldTransl("cg_crit_made_purchases", false) %></td>
				<td>
					<%
					  String p_crit_made_purchase_condition = "";
					  if ("LESS".equalsIgnoreCase(group.getValue("CRIT_MADE_PURCHASE_CONDITION"))) {
						  p_crit_made_purchase_condition = Bean.call_centerXML.getfieldTransl("cg_crit_made_purchase_less", false);
					  } else if ("EQUAL".equalsIgnoreCase(group.getValue("CRIT_MADE_PURCHASE_CONDITION"))) {
						  p_crit_made_purchase_condition = Bean.call_centerXML.getfieldTransl("cg_crit_made_purchase_equal", false);
					  } else if ("MORE".equalsIgnoreCase(group.getValue("CRIT_MADE_PURCHASE_CONDITION"))) {
						  p_crit_made_purchase_condition = Bean.call_centerXML.getfieldTransl("cg_crit_made_purchase_more", false);
					  }
					%>
					<input type="text" name="crit_made_purchase_condition" size="10" value="<%= p_crit_made_purchase_condition %>" readonly="readonly" class="inputfield-ro">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="text" name="crit_made_purchase_value" size="5" value="<%= group.getValue("CRIT_MADE_PURCHASE_VALUE") %>" readonly="readonly" class="inputfield-ro">
				</td>
			<td><%= Bean.getClubCardXMLFieldTransl("club_event_given_card", false) %>
				<%= Bean.getGoToClubEventLink(clubcard.getValue("ID_CLUB_EVENT")) %>
			</td>
			<td><input type="text" name="id_club_event" size="35" value="<%= Bean.getClubActionName(clubcard.getValue("ID_CLUB_EVENT")) %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
		  	<td>&nbsp;&nbsp;&nbsp;&nbsp;<%= Bean.call_centerXML.getfieldTransl("cg_crit_made_purchase_from_date", false) %></td><td><input type="text" name="crit_made_purchase_from_date" size="10" value="<%= group.getValue("CRIT_MADE_PURCHASE_FROM_DATE_DF") %>" readonly="readonly" class="inputfield-ro">&nbsp;<%= Bean.call_centerXML.getfieldTransl("cg_crit_made_purchase_to_date", false) %>&nbsp;<input type="text" name="crit_made_purchase_to_date" size="10" value="<%= group.getValue("CRIT_MADE_PURCHASE_TO_DATE_DF") %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.getClubCardXMLFieldTransl("date_card_sale", false) %></td><td><input type="text" name="date_card_sale" size="12" value="<%= clubcard.getValue("DATE_CARD_SALE_DF") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
		    <td>&nbsp;&nbsp;&nbsp;&nbsp;<%= Bean.call_centerXML.getfieldTransl("crit_made_purchase_dealer", false) %>
				<%= Bean.getGoToJurPrsHyperLink(group.getValue("CRIT_MADE_PURCHASE_DEALER")) %>
			</td>
		  	<td>
				<input type="text" name="crit_made_purchase_dealer" size="40" value="<%= Bean.getJurPersonShortName(group.getValue("CRIT_MADE_PURCHASE_DEALER")) %>" readonly="readonly" class="inputfield-ro">
			</td>	
			<td style="border-top:solid windowtext 1.0pt;"><%= Bean.call_centerXML.getfieldTransl("card_bal_exist", false) %> (<%=clubcard.getValue("SNAME_CURRENCY") %>)</td> <td style="border-top:solid windowtext 1.0pt;"><input type="text" name="card_bal_exist" size="12" value="<%= clubcard.getValue("BAL_EXIST_FRMT") %>" readonly="readonly" class="inputfield-ro"> </td>
		</tr>
		<tr>
			<td colspan="2">&nbsp;</td>
			<td><%= Bean.call_centerXML.getfieldTransl("card_bal_feauture", false) %></td> <td><input type="text" name="card_bal_feauture" size="12" value="<%= clubcard.getValue("BAL_FEAUTURE_FRMT") %>" readonly="readonly" class="inputfield-ro"> </td>
		</tr>
		<tr>
			<td colspan="2">&nbsp;</td>
			<td><%= Bean.call_centerXML.getfieldTransl("card_last_date_payment", false) %></td> <td><input type="text" name="card_last_date_payment" size="12" value="<%= clubcard.getValue("DATE_ACC_DF") %>" readonly="readonly" class="inputfield-ro"> </td>
		</tr>
		<tr>
			<td colspan="2">&nbsp;</td>
			<td><%= Bean.call_centerXML.getfieldTransl("card_last_date_payment", false) %></td> <td><input type="text" name="card_last_date_payment" size="12" value="<%= clubcard.getValue("DATE_ACC_DF") %>" readonly="readonly" class="inputfield-ro"> </td>
		</tr>
		<tr>
			<td colspan="2">&nbsp;</td>
			<td><%= Bean.call_centerXML.getfieldTransl("card_payment_count", false) %></td> <td><input type="text" name="card_payment_count" size="12" value="<%= question.getValue("CARD_PAYMENT_COUNT") %>" readonly="readonly" class="inputfield-ro"> </td>
		</tr>
		<tr>
			<td colspan="2">&nbsp;</td>
			<td><%= Bean.call_centerXML.getfieldTransl("card_payment_sum", false) %></td> <td><input type="text" name="card_payment_sum" size="12" value="<%= question.getValue("CARD_PAYMENT_SUM_FRMT") %>" readonly="readonly" class="inputfield-ro"> </td>
		</tr>
		<tr>
			<td colspan="2">&nbsp;</td>
			<td><%= Bean.call_centerXML.getfieldTransl("suporbon_sum", false) %></td> <td><input type="text" name="suporbon_sum" size="12" value="<%= superbon.getValue("VALUE_CARD_PURSE_FRMT") %>" readonly="readonly" class="inputfield-ro"> </td>
		</tr>
		
	</table>
	<table <%=Bean.getTableBottomParam() %>><tbody>
		<%
			String pLineStyle = Bean.getUIUserParam("inquirer_line_answer_style");
			if (pLineStyle == null || "".equalsIgnoreCase(pLineStyle)) {
				pLineStyle = "SELECT";
			}
		%>
		<%=question.getInquirerLineHTML(hasEditPermission, pLineStyle, "1", "1000") %>

		<%
		String pCallDate = question.getValue("DUE_DATE_DF");
		if (pCallDate == null || "".equalsIgnoreCase(pCallDate)) {
			pCallDate = Bean.getSysDate();
		}
		%>

	<% if (hasEditPermission) { %>
		<tr>
		  	<td colspan="2"><%= Bean.call_centerXML.getfieldTransl("call_date", true) %></td><td style="background-color: #D3D3D3;"><%=Bean.getCalendarInputField("call_date", pCallDate, "10") %></td>
		</tr>
		<tr>
			<td colspan="2"><%= Bean.call_centerXML.getfieldTransl("cc_question_status", true) %></td><td style="background-color: #D3D3D3;"><select name="cd_cc_question_status" class="inputfield"><%= Bean.getCallCenterQuestionStatusOptions(question.getValue("CD_CC_QUESTION_STATUS"), false) %></select></td>
		</tr>
		<tr>
			<td><%= Bean.call_centerXML.getfieldTransl("note", false) %></td><td colspan="2"><textarea name="question_note" cols="120" rows="3" class="inputfield"><%= question.getValue("NOTE") %></textarea></td>
		</tr>
	<% } else { %>
		<tr>
		  	<td colspan="2"><%= Bean.call_centerXML.getfieldTransl("call_date", false) %></td><td><input type="text" name="call_date" size="10" value="<%= pCallDate %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td colspan="2"><%= Bean.call_centerXML.getfieldTransl("cc_question_status", false) %></td><td><input type="text" name="name_cc_question_status" size="10" value="<%= question.getValue("NAME_CC_QUESTION_STATUS") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td><%= Bean.call_centerXML.getfieldTransl("note", false) %></td><td colspan="2"><textarea name="question_note" cols="120" rows="3" readonly="readonly" class="inputfield-ro"><%= question.getValue("NOTE") %></textarea></td>
		</tr>
	<% } %>
	</tbody></table>
	<table <%=Bean.getTableMenuFilter() %>>
 		<tr>
			<td colspan="4" align="center">
				<% if (hasEditPermission) { %>
				<%=Bean.getSubmitButtonAjax("../crm/call_center/inquirerupdate.jsp") %>
				<%=Bean.getResetButton() %>
				<% } %>
				<% if (action.equalsIgnoreCase("add")) { %>
					<%=Bean.getGoBackButton("../crm/call_center/inquirer.jsp") %>
				<% } else { %>
					<%=Bean.getGoBackButton("../crm/call_center/inquirerspecs.jsp?id=" + id) %>
				<% } %>
			</td>
		</tr>
	</table>
	</form>
	<% if (hasEditPermission) { %>
		<%= Bean.getCalendarScript("call_date", false) %>
	<% } %>
	<%
	   	}
	} else if (process.equalsIgnoreCase("yes"))	{
		if (action.equalsIgnoreCase("edit")) {
		    String  
		    	l_paramCount			= Bean.getDecodeParam(parameters.get("paramcount")),
		    	id_cc_question			= Bean.getDecodeParam(parameters.get("id_cc_question")),
		    	call_date				= Bean.getDecodeParam(parameters.get("call_date")),
		    	cd_cc_question_status	= Bean.getDecodeParam(parameters.get("cd_cc_question_status")),
		    	question_note			= Bean.getDecodeParam(parameters.get("question_note"));
		    
		String callSQL = "";
		String[] results = new String[2];
		String resultInt = "";
		String resultFull = "0";
		String resultMessage = "";
		String resultMessageFull = "";
		    
		ArrayList<String> id_inquirer=new ArrayList<String>();
		ArrayList<String> value_inquirer=new ArrayList<String>();
    	Set<String> keySet = parameters.keySet();
    	Iterator<String> keySetIterator = keySet.iterator();
    	String key = "";
    	while(keySetIterator.hasNext()) {
    		try{
    			key = (String)keySetIterator.next();
    			if(key.contains("inquirer_line__")){
    				id_inquirer.add(key.substring(15));
    				value_inquirer.add(Bean.getDecodeParam(parameters.get(key)));
    			}
    			
    		}
    		catch(Exception ex){
    			Bean.writeException(
    						"../crm/call_center/inquirerupdate.jsp",
    						type,
    						process,
    						action,
    						Bean.commonXML.getfieldTransl("h_get_param_value_error", false)+key+": " + ex.toString());
    		}
    		 
    	}

    %>

		<%= Bean.form_messageXML.getfieldTransl("processing_update", false) %>
		<%
		if (!(id_inquirer == null)) {
		 for(int counter=0;counter<id_inquirer.size();counter++){
	    	callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_CALL_CENTER.set_question_inquirer_line(" +
	    		"?,?,?,?)}";

	    	ArrayList<String> pParam2 = new ArrayList<String>();
						
			pParam2.add(id_cc_question);
			pParam2.add(id_inquirer.get(counter));
			pParam2.add(value_inquirer.get(counter));
		
			results = Bean.myCallFunctionParam(callSQL, pParam2, results.length);
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
		
		callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_CALL_CENTER.update_question_inquirer(" + 
			"?,?,?,?,?,?)}";

		ArrayList<String> pParam = new ArrayList<String>();
					
		pParam.add(id_cc_question);
		pParam.add(call_date);
		pParam.add(cd_cc_question_status);
		pParam.add(question_note);
		pParam.add(Bean.getDateFormat());
				
		results = Bean.myCallFunctionParam(callSQL, pParam, results.length);
		resultInt = results[0];
		resultMessage = results[1];
		
		%>
		<%= Bean.showCallSQL(callSQL) %>
		<%
		
		if (!("0".equalsIgnoreCase(resultInt))) {
			resultFull = resultInt;
			resultMessageFull = resultMessageFull + "; " +resultMessage;
		}
		
		%>
  	    <%=Bean.showCallResult(
   	    		callSQL, 
   	    		resultFull, 
   	    		resultMessageFull, 
   	    		"../crm/call_center/inquirerspecs.jsp?id=" + id, 
   	    		"../crm/call_center/inquirerspecs.jsp?id=" + id, 
   	    		Bean.form_messageXML.getfieldTransl("save_error", false)) %>
   		<% 

	    } else { %> 
	    	<%= Bean.getUnknownActionText(action) %><% 
		}
	}
} else {
    %> <%= Bean.getUnknownTypeText(type) %> <%
}
%>


</body>
</html>
