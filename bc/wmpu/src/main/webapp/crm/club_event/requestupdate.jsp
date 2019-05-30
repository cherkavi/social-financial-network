<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />


<%@page import="bc.objects.bcGiftObject"%>
<%@page import="java.util.HashMap"%>
<%@page import="bc.objects.bcClubShortObject"%>
<%@page import="bc.objects.bcClubActionGivenScheduleObject"%>
<%@page import="bc.objects.bcNatPrsGiftRequestObject"%>
<%@page import="bc.objects.bcNatPrsGiftObject"%><html>

<%= Bean.getLogOutScript(request) %>

<head>
	<%= Bean.getMetaContent() %>
	<%= Bean.getBottomFrameCSS() %>
</head>

<body>
<%
HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String pageFormName = "CLUB_EVENT_REQUEST";

Bean.setJspPageForTabName(pageFormName);

request.setCharacterEncoding("UTF-8");

String type		= Bean.getDecodeParam(parameters.get("type"));
String id		= Bean.getDecodeParam(parameters.get("id")); 
String action	= Bean.getDecodeParam(parameters.get("action"));
String process	= Bean.getDecodeParam(parameters.get("process"));
 
if (id==null || ("".equalsIgnoreCase(id))) id="empty";
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
			new Array ('cd_nat_prs_gift_request_type', 'varchar2', 1),
			new Array ('cd_nat_prs_gift_request_state', 'varchar2', 1),
			new Array ('date_accept', 'varchar2', 1),
			new Array ('name_nat_prs', 'varchar2', 1),
			new Array ('name_club', 'varchar2', 1)
		);
	</script>
		<%= Bean.getOperationTitle(
				Bean.club_actionXML.getfieldTransl("h_add_nat_prs_gift_request", false),
				"Y",
				"Y") 
		%>
        <form action="../crm/club_event/requestupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">
	        <input type="hidden" name="type" value="general">
	        <input type="hidden" name="action" value="add">
	        <input type="hidden" name="process" value="yes">
		<table <%=Bean.getTableDetailParam() %>>
		<tr>
			<td><%= Bean.club_actionXML.getfieldTransl("cd_nat_prs_gift_request_type", true) %></td><td><select name="cd_nat_prs_gift_request_type" class="inputfield"><%=Bean.getNatPrsGiftRequestTypeOptionsExclude("", "", true) %> </select></td>
 		    <td><%= Bean.clubXML.getfieldTransl("club", true) %>
				<%= Bean.getGoToClubLink(club.getValue("ID_CLUB")) %>
			</td>
		  	<td>
				<%=Bean.getWindowFindClub("club", club.getValue("ID_CLUB"), club.getValue("SNAME_CLUB"), "40") %>
	  		</td>
		</tr>
		<tr>
			<td><%= Bean.club_actionXML.getfieldTransl("cd_nat_prs_gift_request_state", true) %></td><td><select name="cd_nat_prs_gift_request_state" class="inputfield"><%=Bean.getNatPrsGiftRequestStateOptions("ACCEPT", true) %> </select></td>
			<td colspan="2">&nbsp;</td>
		</tr>
		<tr>
			<td><%= Bean.club_actionXML.getfieldTransl("date_accept", true) %></td> <td><%=Bean.getCalendarInputField("date_accept", Bean.getSysDate(), "10") %></td>
			<td colspan="2">&nbsp;</td>
		</tr>
		<tr>
			<td><%= Bean.club_actionXML.getfieldTransl("name_nat_prs", true) %></td>
			<td>
				<%=Bean.getWindowFindNatPrs("nat_prs", "", "", "35") %>
			</td>
			<td colspan="2">&nbsp;</td>
		</tr>
		<tr>
 		    <td><%= Bean.club_actionXML.getfieldTransl("name_club_event", false) %></td>
		  	<td>
				<%=Bean.getWindowFindClubAction("club_event", "", "", "35") %>
	  		</td>
			<td colspan="2">&nbsp;</td>
		</tr>
		<tr>
			<td><%= Bean.club_actionXML.getfieldTransl("text_request", false) %></td><td><textarea name="text_request" cols="70" rows="3" class="inputfield"></textarea></td>
			<td colspan="2">&nbsp;</td>
		</tr>
		<tr>
			<td colspan="6" align="center">
				<%=Bean.getSubmitButtonAjax("../crm/club_event/requestupdate.jsp") %>
				<%=Bean.getResetButton() %>
				<% if ("add".equalsIgnoreCase(action)) { %>
					<%=Bean.getGoBackButton("../crm/club_event/request.jsp") %>
				<% } else { %>
					<%=Bean.getGoBackButton("../crm/club_event/requestspecs.jsp?id=" + id) %>
				<% } %>
			</td>
		</tr>
	</table>
	</form>
	<!-- Скрипт для втавки меню вибору дати -->
		<%= Bean.getCalendarScript("date_accept", false) %>

	        <%
    	} else {
    	    %> <%= Bean.getUnknownActionText(action) %><%
    	}
	
	} else if (process.equalsIgnoreCase("yes")) {
    
		String
			cd_nat_prs_gift_request_type	= Bean.getDecodeParam(parameters.get("cd_nat_prs_gift_request_type")),
			cd_nat_prs_gift_request_state	= Bean.getDecodeParam(parameters.get("cd_nat_prs_gift_request_state")),
			date_accept						= Bean.getDecodeParam(parameters.get("date_accept")),
			date_reject						= Bean.getDecodeParam(parameters.get("date_reject")),
			date_processed					= Bean.getDecodeParam(parameters.get("date_processed")),
			id_nat_prs						= Bean.getDecodeParam(parameters.get("id_nat_prs")),
			id_club_event					= Bean.getDecodeParam(parameters.get("id_club_event")),
			id_accept_sms_message			= Bean.getDecodeParam(parameters.get("id_accept_sms_message")),
			text_request					= Bean.getDecodeParam(parameters.get("text_request")),
			id_club							= Bean.getDecodeParam(parameters.get("id_club"));

    	if (action.equalsIgnoreCase("add")) { 
    		
    		String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_CLUB_ACTION.add_nat_prs_gift_request("+
    			"?,?,?,?,?,?,?,?,?)}";

    		String[] pParam = new String [7];
    				
    		pParam[0] = cd_nat_prs_gift_request_type;
    		pParam[1] = cd_nat_prs_gift_request_state;
    		pParam[2] = date_accept;
    		pParam[3] = id_nat_prs;
    		pParam[4] = id_club_event;
    		pParam[5] = text_request;
    		pParam[6] = Bean.getDateFormat();
	
		 	%>
			<%= Bean.getCallResultParam("INSERT", callSQL, pParam, "../crm/club_event/requestspecs.jsp?id=" , "../crm/club_event/request.jsp") %>
			<% 	
   
    	} else if (action.equalsIgnoreCase("remove")) {

	   		String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_CLUB_ACTION.delete_nat_prs_gift_request(?,?)}";

    		String[] pParam = new String [1];
    				
    		pParam[0] = id;

		 	%>
			<%= Bean.getCallResultParam("DELETE", callSQL, pParam, "../crm/club_event/request.jsp" , "") %>
			<% 	
	    
    	} else if (action.equalsIgnoreCase("edit")) { %> 
	        <% 
	        String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_CLUB_ACTION.update_nat_prs_gift_request(" + 
	        	"?,?,?,?,?,?,?,?,?,?,?,?)}";

        	String[] pParam = new String [11];
        				
        	pParam[0] = id;
        	pParam[1] = cd_nat_prs_gift_request_type;
        	pParam[2] = cd_nat_prs_gift_request_state;
        	pParam[3] = date_accept;
        	pParam[4] = date_reject;
        	pParam[5] = date_processed;
        	pParam[6] = id_nat_prs;
        	pParam[7] = id_club_event;
        	pParam[8] = id_accept_sms_message;
        	pParam[9] = text_request;
        	pParam[10] = Bean.getDateFormat();
	
		 	%>
			<%= Bean.getCallResultParam("UPDATE", callSQL, pParam, "../crm/club_event/requestspecs.jsp?id=" + id, "") %>
			<% 	
	     
    	} else { %> 
    		<%= Bean.getUnknownActionText(action) %><% 
    	}
	} else {%> 
		<%= Bean.getUnknownProcessText(process) %> <%
  	}
} else if (type.equalsIgnoreCase("gift")) {

	if (process.equalsIgnoreCase("no")) {
		if (action.equalsIgnoreCase("add")) {
		    		
			bcNatPrsGiftRequestObject req = new bcNatPrsGiftRequestObject(id);

			        %> 
			<% if (!(req.getValue("ID_CLUB_EVENT")==null || "".equalsIgnoreCase(req.getValue("ID_CLUB_EVENT")))) { %>
			<script>
				var formData = new Array (
					new Array ('name_club_event_gift', 'varchar2', 1),
					new Array ('date_reserve', 'varchar2', 1)
				);
			</script>
			<% } else { %>
			<script>
				var formData = new Array (
					new Array ('date_reserve', 'varchar2', 1),
					new Array ('name_gift', 'varchar2', 1),
					new Array ('cost_gift', 'varchar2', 1),
					new Array ('cd_currency', 'varchar2', 1)
				);
			</script>
			<% } %>
			<%= Bean.getOperationTitle(
					Bean.club_actionXML.getfieldTransl("h_gift_add", false),
					"Y",
					"Y") 
			%>
	        <form action="../crm/club_event/requestupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">
		        <input type="hidden" name="type" value="gift">
		        <input type="hidden" name="action" value="add">
		        <input type="hidden" name="process" value="yes">
		        <input type="hidden" name="id" value="<%=id %>">
		        <input type="hidden" name="id_nat_prs" value="<%=req.getValue("ID_NAT_PRS") %>">
			<table <%=Bean.getTableDetailParam() %>>
			<tr>
				<td><%= Bean.club_actionXML.getfieldTransl("full_name", false) %>
					<%= Bean.getGoToNatPrsLink(req.getValue("ID_NAT_PRS")) %>
				</td><td><input type="text" name="full_name" size="60" value="<%= req.getValue("FULL_NAME") %>" readonly="readonly" class="inputfield-ro"></td>
				<td colspan="2">&nbsp;</td>
			</tr>
			<% if (!(req.getValue("ID_CLUB_EVENT")==null || "".equalsIgnoreCase(req.getValue("ID_CLUB_EVENT")))) { %>
			<tr>
				<td><%= Bean.club_actionXML.getfieldTransl("name_club_event", false) %>
					<%= Bean.getGoToClubEventLink(req.getValue("ID_CLUB_EVENT")) %>
				</td>
				<td>
					<input type="hidden" name="id_club_event" id="id_club_event" value="<%= req.getValue("ID_CLUB_EVENT") %>">
					<input type="text" name="name_club_event" size="60" value="<%= req.getValue("NAME_CLUB_EVENT") %>" readonly="readonly" class="inputfield-ro">
				</td>
				<td colspan="2">&nbsp;</td>
			</tr>
	        <tr>
				<td><%= Bean.club_actionXML.getfieldTransl("date_reserve", true) %></td> <td><%=Bean.getCalendarInputField("date_reserve", Bean.getSysDate(), "10") %></td>
				<td colspan="2">&nbsp;</td>
			</tr>
			<tr>
	 		    <td><%= Bean.club_actionXML.getfieldTransl("name_gift", true) %></td>
			  	<td>
					<%=Bean.getWindowFindClubActionGifts("club_event_gift", "'+document.getElementById('id_club_event').value+'", "", "", "35") %>
		  		</td>
				<td colspan="2">&nbsp;</td>
			</tr>
			<tr>
				<td><%= Bean.club_actionXML.getfieldTransl("basis_for_gift", false) %></td> <td><textarea name="basis_for_gift" cols="56" rows="3" class="inputfield"></textarea></td>
				<td colspan="2">&nbsp;</td>
			</tr>
			<% } else { %>
	        <tr>
				<td><%= Bean.club_actionXML.getfieldTransl("date_reserve", true) %></td> <td><%=Bean.getCalendarInputField("date_reserve", Bean.getSysDate(), "10") %></td>
				<td colspan="2">&nbsp;</td>
			</tr>
			<tr>
	 		    <td><%= Bean.club_actionXML.getfieldTransl("name_gift", true) %></td>
			  	<td>
					<%=Bean.getWindowGifts("gift", "", "35") %>
		  		</td>
				<td colspan="2">&nbsp;</td>
			</tr>
			<tr>
				<td><%= Bean.club_actionXML.getfieldTransl("cost_gift", true) %></td><td><input type="text" name="cost_gift" size="20" value="" class="inputfield"></td>
			</tr>
			<tr>
				<td><%= Bean.club_actionXML.getfieldTransl("cd_currency", true) %></td> <td><select name="cd_currency" class="inputfield"><%= Bean.getCurrencyOptions("", true) %></select></td>
			</tr>
			<tr>
				<td><%= Bean.club_actionXML.getfieldTransl("basis_for_gift", false) %></td> <td><textarea name="basis_for_gift" cols="56" rows="3" class="inputfield"></textarea></td>
				<td colspan="2">&nbsp;</td>
			</tr>
			<% } %>
			<tr>
				<td colspan="6" align="center">
					<%=Bean.getSubmitButtonAjax("../crm/club_event/requestupdate.jsp") %>
					<%=Bean.getResetButton() %>
					<%=Bean.getGoBackButton("../crm/club_event/requestspecs.jsp?id=" + id) %>
				</td>
			</tr>
		</table>
		</form>
		<!-- Скрипт для втавки меню вибору дати -->
			<%= Bean.getCalendarScript("date_reserve", false) %>

			        <%
		} else {
		    %> <%= Bean.getUnknownActionText(action) %><%
		}
			
	} else if (process.equalsIgnoreCase("yes")) {
		String
			id_nat_prs						= Bean.getDecodeParam(parameters.get("id_nat_prs")),
			cd_nat_prs_gift_state			= Bean.getDecodeParam(parameters.get("cd_nat_prs_gift_state")),
			date_reserve					= Bean.getDecodeParam(parameters.get("date_reserve")),
			date_given						= Bean.getDecodeParam(parameters.get("date_given")),
			date_returned					= Bean.getDecodeParam(parameters.get("date_returned")),
			date_canceled					= Bean.getDecodeParam(parameters.get("date_canceled")),
			id_club_event_gift				= Bean.getDecodeParam(parameters.get("id_club_event_gift")),
			id_lg_gift						= Bean.getDecodeParam(parameters.get("id_lg_gift")),
			id_club_event_given_schedule	= Bean.getDecodeParam(parameters.get("id_club_event_given_schedule")),
			id_card							= Bean.getDecodeParam(parameters.get("id_card")),
			cd_card_purse_type				= Bean.getDecodeParam(parameters.get("cd_card_purse_type")),
			id_nat_prs_gift					= Bean.getDecodeParam(parameters.get("id_nat_prs_gift")),
			id_gift							= Bean.getDecodeParam(parameters.get("id_gift")),
			cost_gift						= Bean.getDecodeParam(parameters.get("cost_gift")),
			cd_currency						= Bean.getDecodeParam(parameters.get("cd_currency"));
	
	 

	   	if (action.equalsIgnoreCase("add")) { 
		    		
	   		String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_CLUB_ACTION.add_nat_prs_gift("+
		   		"?,?,?,?,?,?,?,?,?,?)}";

	   		String[] pParam = new String [8];
	   				
	   		pParam[0] = id_nat_prs;
	   		pParam[1] = id;
	   		pParam[2] = date_reserve;
	   		pParam[3] = id_club_event_gift;
	   		pParam[4] = id_gift;
	   		pParam[5] = cost_gift;
	   		pParam[6] = cd_currency;
	   		pParam[7] = Bean.getDateFormat();
		
		 	%>
			<%= Bean.getCallResultParam("INSERT", callSQL, pParam, "../crm/club_event/requestspecs.jsp?id=" + id + "&id_club_event_gift=" , "../crm/club_event/request.jsp") %>
			<% 	
		   
	   	} else if (action.equalsIgnoreCase("remove")) {

	   		String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_CLUB_ACTION.delete_winner(?,?)}";

	   		String[] pParam = new String [1];
	   				
	   		pParam[0] = id_nat_prs_gift;
	   		
			%>
			<%= Bean.getCallResultParam("DELETE", callSQL, pParam, "../crm/club_event/requestspecs.jsp?id=" + id , "") %>
			<% 	
	    
    	} else if (action.equalsIgnoreCase("disassemble")) { %> 
	        <% 
	        String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_CLUB_ACTION.disassemble_request(?,?)}"; 

		   	String[] pParam = new String [1];
		   				
		   	pParam[0] = id;
	
		 	%>
			<%= Bean.getCallResultParam("UPDATE", callSQL, pParam, "../crm/club_event/requestspecs.jsp?id=" + id, "") %>
			<% 	
		     
    	} else { %> 
    		<%= Bean.getUnknownActionText(action) %><% 
    	}
	} else {%>  
		<%= Bean.getUnknownProcessText(process) %> <%
  	}

} else {%> 
	<%= Bean.getUnknownTypeText(type) %> <%
}

%>


</body>
</html>
