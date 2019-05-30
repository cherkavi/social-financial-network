<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%= Bean.getLogOutScript(request) %>

<%@page import="bc.objects.bcLGCardRangeObject"%>
<%@page import="java.util.HashMap"%>
<%@page import="bc.objects.bcClubShortObject"%><html>
<head>
	<%= Bean.getMetaContent() %>
	<%= Bean.getBottomFrameCSS() %>
</head>
<body>

<%
HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String pageFormName = "LOGISTIC_PARTNERS_CARDS";

Bean.setJspPageForTabName(pageFormName);

request.setCharacterEncoding("UTF-8");
String id		= Bean.getDecodeParam(parameters.get("id")); 
String line		= Bean.getDecodeParam(parameters.get("line")); 
String type		= Bean.getDecodeParam(parameters.get("type")); 
String action	= Bean.getDecodeParam(parameters.get("action")); 
String process	= Bean.getDecodeParam(parameters.get("process"));

if (id==null || ("".equalsIgnoreCase(id))) id="empty";
if (line==null || ("".equalsIgnoreCase(line))) line="empty";
if (type==null || ("".equalsIgnoreCase(type))) type="empty";
if (action==null || ("".equalsIgnoreCase(action))) action="empty";
if (process==null || ("".equalsIgnoreCase(process))) process="empty";

if (type.equalsIgnoreCase("general")) {
	if (process.equalsIgnoreCase("no"))
	/* вибираємо тип дії (добавити, видалити...)*/
	{
	   /*  --- Добавити запис --- */
    	if (action.equalsIgnoreCase("add") || action.equalsIgnoreCase("add2")) {
    		
    		bcClubShortObject club = new bcClubShortObject(Bean.getCurrentClubID());

	        %>
	<script>
		var formData = new Array (
			new Array ('action_date', 'varchar2', 1),
			new Array ('name_club', 'varchar2', 1)
		);
	</script> 
		<%= Bean.getOperationTitle(
	    		Bean.logisticXML.getfieldTransl("h_action_bon_card_add", false),
				"Y",
				"Y") 
		%>
    <form action="../crm/logistic/partners/cardupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">
        <input type="hidden" name="action" value="add">
        <input type="hidden" name="process" value="yes">
    	<input type="hidden" name="type" value="general">
	<table <%=Bean.getTableDetailParam() %>>
		<tr>
			<td><%= Bean.logisticXML.getfieldTransl("name_lg_type", false) %></td><td><input type="text" name="name_lg_type" size="60" value="<%=Bean.getLGTypeName("BON_CARD") %>" readonly="readonly" class="inputfield-ro"></td>
 		    <td><%= Bean.clubXML.getfieldTransl("club", true) %>
				<%= Bean.getGoToClubLink(club.getValue("ID_CLUB")) %>
			</td>
		  	<td>
				<%=Bean.getWindowFindClub("club", club.getValue("ID_CLUB"), club.getValue("SNAME_CLUB"), "25") %>
	  		</td>
		</tr>
		<tr>
			<td><%= Bean.logisticXML.getfieldTransl("action_date_frmt", true) %></td><td><%=Bean.getCalendarInputField("action_date", Bean.getSysDate(), "10") %></td>
			<td class="top_line"><%= Bean.logisticXML.getfieldTransl("sname_jur_prs_receiver", false) %></td>
			<td class="top_line">
				<%=Bean.getWindowFindJurPrs("jur_prs_receiver", "", "", "ALL", "40") %>
			</td>			
		</tr>
		<tr>
			<td rowspan="3"><%=  Bean.logisticXML.getfieldTransl("operation_desc", false) %></td><td rowspan="3"><textarea name="operation_desc" cols="60" rows="6" class="inputfield"></textarea></td>
			<td><%= Bean.logisticXML.getfieldTransl("id_service_place_receiver", false) %></td>
			<td>
				<%=Bean.getWindowFindServicePlace("service_place_receiver", "", "", "'+document.getElementById('id_jur_prs_receiver').value+'", "", "40") %>
			</td>			
		</tr>
		<tr>
			<td><%= Bean.logisticXML.getfieldTransl("id_contact_prs_receiver", false) %></td>
			<td>
				<%=Bean.getWindowContactPersons("contact_prs_receiver", "", "'+document.getElementById('id_jur_prs_receiver').value+'", "'+document.getElementById('id_service_place_receiver').value+'", "40") %>
			</td>			
		</tr>
		<tr>
			<td><%=  Bean.logisticXML.getfieldTransl("desc_receiver", false) %></td><td><textarea name="desc_receiver" cols="60" rows="3" class="inputfield"></textarea></td>
		</tr>
		<tr>
			<td><%= Bean.logisticXML.getfieldTransl("bon_cards_count", false) %></td><td><input type="text" name="object_count" size="20" value="" class="inputfield"></td>
			<td class="top_line"><%= Bean.logisticXML.getfieldTransl("sname_jur_prs_sender", false) %></td>
			<td class="top_line">
				<%=Bean.getWindowFindJurPrs("jur_prs_sender", "", "", "ALL", "40") %>
			</td>			
		</tr>
		<tr>
			<td colspan="2">&nbsp;</td>
			<td><%= Bean.logisticXML.getfieldTransl("id_service_place_sender", false) %></td>
			<td>
				<%=Bean.getWindowFindServicePlace("service_place_sender", "", "", "'+document.getElementById('id_jur_prs_sender').value+'", "", "40") %>
			</td>			
		</tr>
		<tr>
			<td colspan="2">&nbsp;</td>
			<td><%= Bean.logisticXML.getfieldTransl("id_contact_prs_sender", false) %></td>
			<td>
				<%=Bean.getWindowContactPersons("contact_prs_sender", "", "'+document.getElementById('id_jur_prs_sender').value+'", "'+document.getElementById('id_service_place_sender').value+'", "40") %>
			</td>			
		</tr>
 		<tr>
			<td colspan="2">&nbsp;</td>
			<td><%= Bean.logisticXML.getfieldTransl("desc_sender", false) %></td><td><textarea name="desc_sender" cols="60" rows="3" class="inputfield"></textarea></td>
		</tr>
 		<tr>
			<td colspan="6" align="center">
				<%=Bean.getSubmitButtonAjax("../crm/logistic/partners/cardupdate.jsp") %>
				<%=Bean.getResetButton() %>
				<% if ("add".equalsIgnoreCase(action)) { %>
					<%=Bean.getGoBackButton("../crm/logistic/partners/cards.jsp") %>
				<% } else { %>
					<%=Bean.getGoBackButton("../crm/logistic/partners/cardspecs.jsp?id=" + id) %>
				<% } %>
			</td>
		</tr>

	</table>
	
	<!-- Скрипт для втавки меню вибору дати -->
	<%= Bean.getCalendarScript("action_date", false) %>

</form>

	        <%
    	} else {
    	    %> <%= Bean.getUnknownActionText(action) %><%
    	}
	
	} else if (process.equalsIgnoreCase("yes"))	{
    	
		String
    		id_jur_prs_receiver			= Bean.getDecodeParam(parameters.get("id_jur_prs_receiver")),
    		id_service_place_receiver	= Bean.getDecodeParam(parameters.get("id_service_place_receiver")),
    		id_contact_prs_receiver		= Bean.getDecodeParam(parameters.get("id_contact_prs_receiver")),
    		desc_receiver 				= Bean.getDecodeParam(parameters.get("desc_receiver")),
    		id_jur_prs_sender 			= Bean.getDecodeParam(parameters.get("id_jur_prs_sender")),
    		id_service_place_sender 	= Bean.getDecodeParam(parameters.get("id_service_place_sender")),
    		id_contact_prs_sender 		= Bean.getDecodeParam(parameters.get("id_contact_prs_sender")),
    		desc_sender 				= Bean.getDecodeParam(parameters.get("desc_sender")),
    		action_date 				= Bean.getDecodeParam(parameters.get("action_date")),
    		object_count 				= Bean.getDecodeParam(parameters.get("object_count")),
    		operation_desc 				= Bean.getDecodeParam(parameters.get("operation_desc")),
    		id_club		 				= Bean.getDecodeParam(parameters.get("id_club"));

    	if (action.equalsIgnoreCase("add")) { 
    		String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_LG_CARD.add_action(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";

			String[] pParam = new String [14];

			pParam[0] = "BON_CARD";
			pParam[1] = operation_desc;
			pParam[2] = id_jur_prs_receiver;
			pParam[3] = id_service_place_receiver;
			pParam[4] = id_contact_prs_receiver;
			pParam[5] = desc_receiver;
			pParam[6] = id_jur_prs_sender;
			pParam[7] = id_service_place_sender;
			pParam[8] = id_contact_prs_sender;
			pParam[9] = desc_sender;
			pParam[10] = action_date;
			pParam[11] = object_count;
			pParam[12] = id_club;
			pParam[13] = Bean.getDateFormat();
		
		 	%>
			<%= Bean.getCallResultParam("INSERT", callSQL, pParam, "../crm/logistic/partners/cardspecs.jsp?id=" , "../crm/logistic/partners/cards.jsp") %>
			<% 	

    	} else if (action.equalsIgnoreCase("remove")) { 
    		
    		String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_LG_CARD.delete_action(?,?)}";

			String[] pParam = new String [1];

			pParam[0] = id;

		 	%>
			<%= Bean.getCallResultParam("DELETE", callSQL, pParam, "../crm/logistic/partners/cards.jsp" , "") %>
			
			<% 	

    	} else if (action.equalsIgnoreCase("edit")) { 
    		
    		String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_LG_CARD.update_action(?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";

			String[] pParam = new String [13];

			pParam[0] = id;
			pParam[1] = operation_desc;
			pParam[2] = id_jur_prs_receiver;
			pParam[3] = id_service_place_receiver;
			pParam[4] = id_contact_prs_receiver;
			pParam[5] = desc_receiver;
			pParam[6] = id_jur_prs_sender;
			pParam[7] = id_service_place_sender;
			pParam[8] = id_contact_prs_sender;
			pParam[9] = desc_sender;
			pParam[10] = action_date;
			pParam[11] = object_count;
			pParam[12] = Bean.getDateFormat();
		
		 	%>
			<%= Bean.getCallResultParam("UPDATE", callSQL, pParam, "../crm/logistic/partners/cardspecs.jsp?id=" + id, "") %>
			
			<% 	

    	} else { %> 
    		<%= Bean.getUnknownActionText(action) %><% 
    	}
	} else {
    	%> <%= Bean.getUnknownProcessText(process) %> <%
	}


} else if (type.equalsIgnoreCase("range")) {
	if (process.equalsIgnoreCase("no"))
		/* вибираємо тип дії (добавити, видалити...)*/
		{
		   /*  --- Добавити запис --- */
	    	if (action.equalsIgnoreCase("add")) {
	    		
		        %>
		<script>
			var formData = new Array (
					new Array ('begin_cd_card2', 'varchar2', 1),
					new Array ('end_cd_card2', 'varchar2', 1),
					new Array ('name_issuer', 'varchar2', 1),
					new Array ('id_payment_system', 'varchar2', 1),
					new Array ('id_card_status', 'varchar2', 1)
			);
		</script> 
		<%= Bean.getOperationTitle(
	    		Bean.logisticXML.getfieldTransl("h_range_add", false),
				"Y",
				"Y") 
		%>
        <form action="../crm/logistic/partners/cardupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">
	        <input type="hidden" name="action" value="add">
	        <input type="hidden" name="process" value="yes">
	    	<input type="hidden" name="type" value="range">
	    	<input type="hidden" name="id" value="<%=id %>">
	    	<input type="hidden" name="line" value="<%=line %>">
		<table <%=Bean.getTableDetailParam() %>>
			<tr>
				<td><%= Bean.logisticXML.getfieldTransl("begin_cd_card2", true) %></td><td><input type="text" name="begin_cd_card2" size="40" value="" class="inputfield"></td>
				<td><%= Bean.logisticXML.getfieldTransl("sname_issuer", true) %></td>
				<td>
					<%=Bean.getWindowFindJurPrs("issuer", "", "", "ISSUER", "40") %>
				</td>			
			</tr>
			<tr>
				<td><%= Bean.logisticXML.getfieldTransl("end_cd_card2", true) %></td><td><input type="text" name="end_cd_card2" size="40" value="" class="inputfield"></td>
				<td><%= Bean.logisticXML.getfieldTransl("name_payment_system", true) %></td><td><select name="id_payment_system" class="inputfield"><%= Bean.getPaymentSystemOptions("1", false) %> </select></td>
			</tr>
			<tr>
				<td>&nbsp;</td><td>&nbsp;</td>			
				<td><%= Bean.logisticXML.getfieldTransl("name_card_status", true) %></td><td><select name="id_card_status" class="inputfield"><%= Bean.getClubCardStatusOptions("", false) %> </select></td>
			</tr>
	 		<tr>
				<td colspan="6" align="center">
					<%=Bean.getSubmitButtonAjax("../crm/logistic/partners/cardupdate.jsp") %>
					<%=Bean.getResetButton() %>
					<%=Bean.getGoBackButton("../crm/logistic/partners/cardspecs.jsp?id=" + id) %>
				</td>
			</tr>

		</table>
	</form>

		        <%
	    	} else if (action.equalsIgnoreCase("edit")) {
	    		
	    	bcLGCardRangeObject range = new bcLGCardRangeObject(id, line);
	    		
		        %>
		<script>
			var formData = new Array (
					new Array ('begin_cd_card2', 'varchar2', 1),
					new Array ('end_cd_card2', 'varchar2', 1),
					new Array ('name_issuer', 'varchar2', 1),
					new Array ('id_payment_system', 'varchar2', 1),
					new Array ('id_card_status', 'varchar2', 1)
			);
		</script> 
		<%= Bean.getOperationTitle(
	    		Bean.logisticXML.getfieldTransl("h_range_edit", false),
				"Y",
				"Y") 
		%>
        <form action="../crm/logistic/partners/cardupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">
	        <input type="hidden" name="action" value="edit">
	        <input type="hidden" name="process" value="yes">
	    	<input type="hidden" name="type" value="range">
	    	<input type="hidden" name="id" value="<%=id %>">
	    	<input type="hidden" name="line" value="<%=line %>">
		<table <%=Bean.getTableDetailParam() %>>
			<tr>
				<td><%= Bean.logisticXML.getfieldTransl("begin_cd_card2", true) %></td><td><input type="text" name="begin_cd_card2" size="40" value="<%= range.getValue("begin_cd_card2") %>" class="inputfield"></td>
				<td><%= Bean.logisticXML.getfieldTransl("sname_issuer", true) %></td>
				<td>
					<%=Bean.getWindowFindJurPrs("issuer", range.getValue("ID_ISSUER"), range.getValue("SNAME_ISSUER"), "ISSUER", "40") %>
				</td>			
			</tr>
			<tr>
				<td><%= Bean.logisticXML.getfieldTransl("end_cd_card2", true) %></td><td><input type="text" name="end_cd_card2" size="40" value="<%= range.getValue("end_cd_card2") %>" class="inputfield"></td>
				<td><%= Bean.logisticXML.getfieldTransl("name_payment_system", true) %></td><td><select name="id_payment_system" class="inputfield"><%= Bean.getPaymentSystemOptions(range.getValue("ID_PAYMENT_SYSTEM"), false) %> </select></td>
			</tr>
			<tr>
				<td>&nbsp;</td><td>&nbsp;</td>			
				<td><%= Bean.logisticXML.getfieldTransl("name_card_status", true) %></td><td><select name="id_card_status" class="inputfield"><%= Bean.getClubCardStatusOptions(range.getValue("ID_CARD_STATUS"), false) %> </select></td>
			</tr>
			<%=	Bean.getCreationAndMoficationRecordFields(
					range.getValue(Bean.getCreationDateFieldName()),
					range.getValue("CREATED_BY"),
					range.getValue(Bean.getLastUpdateDateFieldName()),
					range.getValue("LAST_UPDATE_BY")
				) %>
	 		<tr>
				<td colspan="6" align="center">
					<%=Bean.getSubmitButtonAjax("../crm/logistic/partners/cardupdate.jsp") %>
					<%=Bean.getResetButton() %>
					<%=Bean.getGoBackButton("../crm/logistic/partners/cardspecs.jsp?id=" + id) %>
				</td>
			</tr>

		</table>
	</form>

		        <%
		        /*  --- Видалити запис --- */
	    	} else {
	    	    %> <%= Bean.getUnknownActionText(action) %><%
	    	}
	
	} else if (process.equalsIgnoreCase("yes"))	{
	    
		String
	    	begin_cd_card2		= Bean.getDecodeParam(parameters.get("begin_cd_card2")),
	    	end_cd_card2 		= Bean.getDecodeParam(parameters.get("end_cd_card2")),
	    	id_issuer 			= Bean.getDecodeParam(parameters.get("id_issuer")),
	    	id_payment_system 	= Bean.getDecodeParam(parameters.get("id_payment_system")),
	    	id_card_status 		= Bean.getDecodeParam(parameters.get("id_card_status"));

		if (action.equalsIgnoreCase("add")) { 
			
			String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_LG_CARD.add_range(?,?,?,?,?,?,?,?)}";

			String[] pParam = new String [6];

			pParam[0] = id;
			pParam[1] = begin_cd_card2;
			pParam[2] = end_cd_card2;
			pParam[3] = id_issuer;
			pParam[4] = id_payment_system;
			pParam[4] = id_card_status;
			
		 	%>
			<%= Bean.getCallResultParam("INSERT", callSQL, pParam, "../crm/logistic/partners/cardspecs.jsp?id=" + id + "&line=", "") %>
			<% 	

		} else if (action.equalsIgnoreCase("remove")) { 
			String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_LG_CARD.delete_range(?,?,?)}";

			String[] pParam = new String [2];

			pParam[0] = id;
			pParam[1] = line;

		 	%>
			<%= Bean.getCallResultParam("DELETE", callSQL, pParam, "../crm/logistic/partners/cardspecs.jsp?id=" + id, "") %>
			<% 	

		} else if (action.equalsIgnoreCase("edit")) { 
			
			String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_LG_CARD.update_range(?,?,?,?,?,?,?,?)}";

			String[] pParam = new String [7];

			pParam[0] = id;
			pParam[1] = line;
			pParam[2] = begin_cd_card2;
			pParam[3] = end_cd_card2;
			pParam[4] = id_issuer;
			pParam[5] = id_payment_system;
			pParam[6] = id_card_status;
			
		 	%>
			<%= Bean.getCallResultParam("UPDATE", callSQL, pParam, "../crm/logistic/partners/cardspecs.jsp?id=" + id, "") %>
			<% 	

		} else { %> 
			<%= Bean.getUnknownActionText(action) %><% 
		}
	} else { %> 
		<%= Bean.getUnknownProcessText(process) %> <%
	}

} else {
	%> <%= Bean.getUnknownTypeText(type) %> <%
}

%>

</body>
</html>
