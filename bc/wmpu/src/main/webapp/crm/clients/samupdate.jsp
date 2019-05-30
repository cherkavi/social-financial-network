<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%= Bean.getLogOutScript(request) %>

<%@page import="java.util.HashMap"%>
<%@page import="java.util.ArrayList"%>
<%@page import="bc.objects.bcTerminalSAMObject"%>
<%@page import="bc.objects.bcTerminalObject"%><html>
<head>
	<%= Bean.getMetaContent() %>
	<%= Bean.getBottomFrameCSS() %>
</head>

<body>
<%
HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String pageFormName = "CLIENTS_SAM";

Bean.setJspPageForTabName(pageFormName);

request.setCharacterEncoding("UTF-8");

String action	= Bean.getDecodeParam(parameters.get("action")); 
String process	= Bean.getDecodeParam(parameters.get("process"));
String type		= Bean.getDecodeParam(parameters.get("type"));
String id		= Bean.getDecodeParam(parameters.get("id"));

if (id==null || ("".equalsIgnoreCase(id))) id="";
if (action==null || ("".equalsIgnoreCase(action))) action="";
if (process==null || ("".equalsIgnoreCase(process))) process="";
if (type==null || ("".equalsIgnoreCase(type))) process="";

if (type.equalsIgnoreCase("general")) {
	
	if (process.equalsIgnoreCase("no")) {
		if (action.equalsIgnoreCase("add") || action.equalsIgnoreCase("adddet")) { 
	%>
	    
	
		<script>
			var formData = new Array (
				new Array ('id_sam', 'varchar2', 1),
				new Array ('cd_sam_type', 'varchar2', 1),
				new Array ('name_club', 'varchar2', 1),
				new Array ('club_registration_date', 'varchar2', 1)
			);
		</script>
			
	 	<%= Bean.getOperationTitle(
				Bean.samXML.getfieldTransl("h_add_sam", false),
				"Y",
				"Y") 
		%>
	
		<form action="../crm/clients/samupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">	        
		        <input type="hidden" name="action" value="add">
		        <input type="hidden" name="process" value="yes">
		        
		<table <%=Bean.getTableDetailParam() %>>
			<tr>
				<td><%= Bean.samXML.getfieldTransl("id_sam", true) %> </td><td><input type="text" name="id_sam" size="20" value="" class="inputfield"></td>
	 		    <td><%= Bean.clubXML.getfieldTransl("club", true) %>
					<% String id_club = Bean.getCurrentClubID(); %>
				</td>
			  	<td>
					<%=Bean.getWindowFindClub("club", id_club, "20") %>
			  	</td>
			</tr>
			<tr>
				<td><%= Bean.samXML.getfieldTransl("sam_serial_number", false) %></td><td><input type="text" name="sam_serial_number" size="20" value="" class="inputfield"></td>
				<td><%= Bean.samXML.getfieldTransl("club_registration_date", true) %></td><td><%=Bean.getCalendarInputField("club_registration_date", Bean.getSysDate(), "10") %></td>
			</tr>
			<tr>
				<td><%= Bean.samXML.getfieldTransl("name_sam_type", true) %></td><td><select name="cd_sam_type" class="inputfield"><%= Bean.getSAMTypeOptions("", true) %></select></td>
			    <td><%= Bean.samXML.getfieldTransl("nt_sam_last_ses", false) %></td><td><input type="text" name="nt_sam_last_ses" size="20" value="0" class="inputfield"></td>
			</tr>
			<tr>
				<td><%= Bean.samXML.getfieldTransl("expiry_date", false) %></td><td><%=Bean.getCalendarInputField("expiry_date", "", "10") %></td>
				<td><%= Bean.samXML.getfieldTransl("nc", false) %> </td><td><input type="text" name="nc" size="20" value="0" class="inputfield"></td>
			</tr>
			<tr>
				<td colspan="6" align="center">
					<%=Bean.getSubmitButtonAjax("../crm/clients/samupdate.jsp") %>
					<%=Bean.getResetButton() %>
					
					<% if ("add".equalsIgnoreCase(action)) { %>
						<%=Bean.getGoBackButton("../crm/clients/sam.jsp") %>
					<% } else { %>
						<%=Bean.getGoBackButton("../crm/clients/samspecs.jsp?id=" + id) %>
					<% } %>
				</td>
			</tr>
		</table>
		</form>
	
		<!-- Скрипт для втавки меню вибору дати -->
			<%= Bean.getCalendarScript("expiry_date", false) %>
			<%= Bean.getCalendarScript("club_registration_date", false) %>
	    <%
		} else { %> 
		<%= Bean.getUnknownActionText(action) %><% 
		}
	} else if (process.equalsIgnoreCase("yes")) {  
		
		String
			id_sam							= Bean.getDecodeParam(parameters.get("id_sam")),
			sam_serial_number				= Bean.getDecodeParam(parameters.get("sam_serial_number")),
	    	expiry_date						= Bean.getDecodeParam(parameters.get("expiry_date")),
	    	cd_sam_type						= Bean.getDecodeParam(parameters.get("cd_sam_type")),
			nt_sam_last_ses					= Bean.getDecodeParam(parameters.get("nt_sam_last_ses")),
			nc								= Bean.getDecodeParam(parameters.get("nc")),
	    	id_club							= Bean.getDecodeParam(parameters.get("id_club")),
	    	club_registration_date			= Bean.getDecodeParam(parameters.get("club_registration_date"));
		
		if (action.equalsIgnoreCase("add")) { 
	    	
	   		ArrayList<String> pParam = new ArrayList<String>();
	   				
	   		pParam.add(id_sam);
	   		pParam.add(sam_serial_number);
	   		pParam.add(expiry_date);
	   		pParam.add(cd_sam_type);
	   		pParam.add(nt_sam_last_ses);
	   		pParam.add(nc);
	   		pParam.add(id_club);
	   		pParam.add(club_registration_date);
	   		pParam.add(Bean.getDateFormat());
	    	
	   	 	%>
	   		<%= Bean.executeUpdateFunction("PACK$SAM_UI.add_sam", pParam, "../crm/clients/samspecs.jsp?id=" + id_sam,"../crm/clients/sam.jsp") %>
	   		<% 	
		} else if (action.equalsIgnoreCase("edit")) { 
	    	
	   		ArrayList<String> pParam = new ArrayList<String>();
	   				
	   		pParam.add(id_sam);
	   		pParam.add(sam_serial_number);
	   		pParam.add(expiry_date);
	   		pParam.add(cd_sam_type);
	   		pParam.add(nt_sam_last_ses);
	   		pParam.add(nc);
	   		pParam.add(club_registration_date);
	   		pParam.add(Bean.getDateFormat());
	    	
	   	 	%>
	   		<%= Bean.executeUpdateFunction("PACK$SAM_UI.update_sam", pParam, "../crm/clients/samspecs.jsp?id=" + id_sam, "") %>
	   		<% 	
		} else { %> 
			<%= Bean.getUnknownActionText(action) %><% 
		}
	} else {
	    %> <%= Bean.getUnknownProcessText(process) %> <%
	}

} else if (type.equalsIgnoreCase("term")) {
	%> 
	<% 
	  if (process.equalsIgnoreCase("no"))
	  /* вибираємо тип дії (добавити, видалити...)*/
		{
		   /*  --- Добавити запис --- */
	    	if (action.equalsIgnoreCase("add")) { 
	    		
	    		%>
 
				<%= Bean.getOperationTitleShort(
						"",
						Bean.terminalXML.getfieldTransl("h_sam_add", false),
						"Y",
						"Y") 
				%>
		<script>
			var formData = new Array (
				new Array ('id_sam', 'varchar2', 1),
				new Array ('id_term', 'varchar2', 1),
				new Array ('cd_sam_status', 'varchar2', 1),
				new Array ('check_mac_icc', 'varchar2', 1),
				new Array ('check_mac_pda', 'varchar2', 1),
				new Array ('assign_term_date_beg', 'varchar2', 1)
			);
			function myValidateForm() {
				return validateForm(formData);
			}
		</script>
        <form action="../crm/clients/terminalupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST">
	        <input type="hidden" name="type" value="term">
	        <input type="hidden" name="action" value="add">
	        <input type="hidden" name="process" value="yes">
	        <input type="hidden" name="id" value="<%=id %>">
		<table <%=Bean.getTableDetailParam() %>>
			<tr>
				<td><%= Bean.samXML.getfieldTransl("id_sam", false) %></td><td><input type="text" name="id_sam" size="25" value="<%= id %>" readonly="readonly" class="inputfield-ro"></td>
			</tr>
			<tr>
				<td valign="top"><%= Bean.samXML.getfieldTransl("id_term", true) %></td> 
				<td>
					<%=Bean.getWindowFindTerm("term", "", "20") %>
				</td>
			</tr>
			<tr>
				<td><%= Bean.samXML.getfieldTransl("cd_sam_status", true) %></td><td><select name="cd_sam_status" id="cd_sam_status" class="inputfield"><%= Bean.getSAMStatusOptions("", true) %></select></td>
			</tr>
			<tr>
				<td><%= Bean.terminalXML.getfieldTransl("assign_term_date_beg", true) %></td> <td><%=Bean.getCalendarInputField("assign_term_date_beg", Bean.getSysDate(), "10") %></td>
			</tr>
			<tr>
				<td><%= Bean.terminalXML.getfieldTransl("assign_term_date_end", false) %></td> <td><%=Bean.getCalendarInputField("assign_term_date_end", "", "10") %></td>
			</tr>
			<tr>
				<td><%= Bean.terminalXML.getfieldTransl("check_mac_icc", true) %></td><td><select name="check_mac_icc" id="check_mac_icc" class="inputfield"><%= Bean.getYesNoLookupOptions("Y", true) %></select></td>
			</tr>
			<tr>
				<td><%= Bean.terminalXML.getfieldTransl("check_mac_pda", true) %></td><td><select name="check_mac_pda" id="check_mac_pda" class="inputfield"><%= Bean.getYesNoLookupOptions("Y", true) %></select></td>
			</tr>
			<tr>
				<td colspan="2"></td>
			</tr>
			<tr>
				<td colspan="6" align="center">
					<%=Bean.getSubmitButtonAjax("../crm/clients/samupdate.jsp") %>
					<%=Bean.getResetButton() %>
					<%=Bean.getGoBackButton("../crm/clients/samspecs.jsp?id=" + id) %>
				</td>
			</tr>
		</table>
		</form>

		<%= Bean.getCalendarScript("assign_term_date_beg", false) %>
		<%= Bean.getCalendarScript("assign_term_date_end", false) %>

		        <%
	    	} else if (action.equalsIgnoreCase("edit")) { 
	    		String id_term_sam				= Bean.getDecodeParam(parameters.get("id_term_sam"));
	    		bcTerminalSAMObject termSAM = new bcTerminalSAMObject(id_term_sam);

	    		bcTerminalObject terminal = new bcTerminalObject(termSAM.getValue("ID_TERM"));
	    		terminal.getFeature();
	    		
	    		String cdTermType = terminal.getValue("CD_TERM_TYPE");
	    		boolean isWebPosTerminal = "WEBPOS".equalsIgnoreCase(cdTermType);
	    		boolean isPhisicalTerminal = "PHYSICAL".equalsIgnoreCase(cdTermType);
	    		
	    		%>
 
				<%= Bean.getOperationTitleShort(
						"",
						Bean.terminalXML.getfieldTransl("h_sam_update", false),
						"Y",
						"Y") 
				%>
		<script>
			var formData = new Array (
				new Array ('cd_sam_status', 'varchar2', 1),
				<% if (isPhisicalTerminal) { %>
				new Array ('check_mac_icc', 'varchar2', 1),
				new Array ('check_mac_pda', 'varchar2', 1),
				<% }%>
				new Array ('assign_term_date_beg', 'varchar2', 1)
			);
			function myValidateForm() {
				return validateForm(formData);
			}
		</script>
        <form action="../crm/clients/terminalupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST">
	        <input type="hidden" name="type" value="term">
	        <input type="hidden" name="action" value="edit">
	        <input type="hidden" name="process" value="yes">
	        <input type="hidden" name="id" value="<%=id %>">
	        <input type="hidden" name="id_term_sam" value="<%=id_term_sam %>">
		<table <%=Bean.getTableDetailParam() %>>
			<tr>
				<td><%= Bean.terminalXML.getfieldTransl("id_term", false) %></td><td><input type="text" name="id_term" size="25" value="<%= termSAM.getValue("ID_TERM") %>" readonly="readonly" class="inputfield-ro"></td>
			</tr>
			<tr>
				<td><%= Bean.terminalXML.getfieldTransl("t_sucurity_module", false) %></td><td><input type="text" name="id_sam" size="25" value="<%= termSAM.getValue("ID_SAM") %>" readonly="readonly" class="inputfield-ro"></td>
			</tr>
			<tr>
				<td><%= Bean.samXML.getfieldTransl("cd_sam_status", true) %></td><td><select name="cd_sam_status" id="cd_sam_status" class="inputfield"><%= Bean.getSAMStatusOptions(termSAM.getValue("CD_SAM_STATUS"), true) %></select></td>
			</tr>
			<tr>
				<td><%= Bean.terminalXML.getfieldTransl("assign_term_date_beg", true) %></td> <td><%=Bean.getCalendarInputField("assign_term_date_beg", termSAM.getValue("ASSIGN_TERM_DATE_BEG_DF"), "10") %></td>
			</tr>
			<tr>
				<td><%= Bean.terminalXML.getfieldTransl("assign_term_date_end", false) %></td> <td><%=Bean.getCalendarInputField("assign_term_date_end", termSAM.getValue("ASSIGN_TERM_DATE_END_DF"), "10") %></td>
			</tr>
			<% if (isPhisicalTerminal) { %>
			<tr>
				<td><%= Bean.terminalXML.getfieldTransl("check_mac_icc", true) %></td><td><select name="check_mac_icc" id="check_mac_icc" class="inputfield"><%= Bean.getYesNoLookupOptions(termSAM.getValue("CHECK_MAC_ICC"), true) %></select></td>
			</tr>
			<tr>
				<td><%= Bean.terminalXML.getfieldTransl("check_mac_pda", true) %></td><td><select name="check_mac_pda" id="check_mac_pda" class="inputfield"><%= Bean.getYesNoLookupOptions(termSAM.getValue("CHECK_MAC_PDA"), true) %></select></td>
			</tr>
			<% }%>
			<tr>
				<td colspan="2"></td>
			</tr>
			<tr>
				<td colspan="6" align="center">
					<%=Bean.getSubmitButtonAjax("../crm/clients/samupdate.jsp") %>
					<%=Bean.getResetButton() %>
					<%=Bean.getGoBackButton("../crm/clients/samspecs.jsp?id=" + id) %>
				</td>
			</tr>
		</table>
		</form>

		<%= Bean.getCalendarScript("assign_term_date_beg", false) %>
		<%= Bean.getCalendarScript("assign_term_date_end", false) %>

		        <%
	    	} else {
	    	    %><%= Bean.getUnknownActionText(process) %><%
	    	}
	
	} else if (process.equalsIgnoreCase("yes")) {
	    String
	    	id_term					= Bean.getDecodeParam(parameters.get("id_term")),
	    	id_term_sam				= Bean.getDecodeParam(parameters.get("id_term_sam")),
	    	cd_sam_status			= Bean.getDecodeParam(parameters.get("cd_sam_status")),
	    	assign_term_date_beg	= Bean.getDecodeParam(parameters.get("assign_term_date_beg")),
	    	assign_term_date_end	= Bean.getDecodeParam(parameters.get("assign_term_date_end")),
	    	check_mac_icc			= Bean.getDecodeParam(parameters.get("check_mac_icc")),
	    	check_mac_pda			= Bean.getDecodeParam(parameters.get("check_mac_pda"));
	    
	    ArrayList<String> pParam = new ArrayList<String>();
	    
		if (action.equalsIgnoreCase("add")) {
	    	 		 	    	      				
	    	pParam.add(id_term);
	    	pParam.add(id);
	    	pParam.add(cd_sam_status);
	    	pParam.add(assign_term_date_beg);
	    	pParam.add(assign_term_date_end);
	    	pParam.add(check_mac_icc);
	    	pParam.add(check_mac_pda);
	    	pParam.add(Bean.getDateFormat());

    		%>
    		<%= Bean.executeUpdateFunction("PACK$TERM_UI.assign_sam", pParam, "../crm/clients/samspecs.jsp?id=" + id, "") %>
    		<% 	
	    } else if (action.equalsIgnoreCase("remove")) {
     		 	    	      				
     	    pParam.add(id_term_sam);

		 	%>
			<%= Bean.executeUpdateFunction("PACK$TERM_UI.detach_sam", pParam, "../crm/clients/samspecs.jsp?id=" + id, "") %>
			<% 	

	    } else if (action.equalsIgnoreCase("edit")) {
	    	 		 	    	      				
	    	pParam.add(id_term_sam);
	    	pParam.add(cd_sam_status);
	    	pParam.add(assign_term_date_beg);
	    	pParam.add(assign_term_date_end);
	    	pParam.add(check_mac_icc);
	    	pParam.add(check_mac_pda);
	    	pParam.add(Bean.getDateFormat());

    		%>
    		<%= Bean.executeUpdateFunction("PACK$TERM_UI.update_term_sam", pParam, "../crm/clients/samspecs.jsp?id=" + id, "") %>
    		<% 	
	    } else  { %><%= Bean.getUnknownProcessText(process) %><% 
	    }
	} else {
	    %><%= Bean.getUnknownProcessText(process) %><%
	}
} else {%> 
	<%= Bean.getUnknownTypeText(type) %><%
}

%>


</body>
</html>
