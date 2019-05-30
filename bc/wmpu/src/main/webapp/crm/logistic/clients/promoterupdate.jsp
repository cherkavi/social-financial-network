<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%= Bean.getLogOutScript(request) %>

<%@page import="bc.objects.bcLGCardRangeObject"%>
<%@page import="java.util.HashMap"%>
<%@page import="bc.objects.bcClubShortObject"%>
<%@page import="java.util.Set"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Map"%>
<%@page import="bc.objects.bcLGPromoterObject"%>
<%@page import="bc.objects.bcCurrencyObject"%><html>
	<%= Bean.getMetaContent() %>
	<%= Bean.getBottomFrameCSS() %>
<%
HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String pageFormName = "LOGISTIC_CLIENTS_PROMOTERS";

Bean.setJspPageForTabName(pageFormName);

request.setCharacterEncoding("UTF-8");
String id		= Bean.getDecodeParam(parameters.get("id")); 
String type		= Bean.getDecodeParam(parameters.get("type")); 
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
    	if (action.equalsIgnoreCase("add") || action.equalsIgnoreCase("add2")) { %>
		<%= Bean.getOperationTitle(
	    		Bean.logisticXML.getfieldTransl("h_action_promoter_payment_add", false),
				"N",
				"N") 
		%>
    <form action="../crm/logistic/clients/promoterupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">
        <input type="hidden" name="action" value="adddet">
        <input type="hidden" name="process" value="no">
    	<input type="hidden" name="type" value="general">
    	<input type="hidden" name="id" value="<%=id %>">
        <input type="hidden" name="actionprev" value="<%=action %>">
	<table <%=Bean.getTableDetailParam() %>>
		<tr>
			<td width="30%"><%= Bean.logisticXML.getfieldTransl("cd_lg_promoter_post", true) %></td><td><%=Bean.getLogisticPromoterPostRadio("cd_lg_promoter_post", "PROMOTER") %></td>
			<td colspan="2"></td>			
		</tr>
 		<tr>
			<td colspan="6" align="center">
				<%=Bean.getSubmitButtonAjax("../crm/logistic/clients/promoterupdate.jsp") %>
				<%=Bean.getResetButton() %>
				<% if ("add".equalsIgnoreCase(action)) { %>
					<%=Bean.getGoBackButton("../crm/logistic/clients/promoters.jsp") %>
				<% } else { %>
					<%=Bean.getGoBackButton("../crm/logistic/clients/promoterspecs.jsp?id=" + id) %>
				<% } %>
			</td>
		</tr>

	</table>
	
	</form>

	        <%
    	} else if (action.equalsIgnoreCase("adddet")) {
    		
    		bcClubShortObject club = new bcClubShortObject(Bean.getCurrentClubID());
    		
    		String
				actionprev				= Bean.getDecodeParam(parameters.get("actionprev")),
				cd_lg_promoter_post		= Bean.getDecodeParam(parameters.get("cd_lg_promoter_post"));
	        %>
	<script>
		var formData = new Array (
			new Array ('cd_lg_promoter', 'varchar2', 1),
			new Array ('name_lg_promoter', 'varchar2', 1),
			new Array ('name_club', 'varchar2', 1),
			new Array ('name_jur_prs', 'varchar2', 1),
			new Array ('cd_lg_promoter_post', 'varchar2', 1),
			new Array ('cd_lg_promoter_state', 'varchar2', 1),
			new Array ('date_begin_work', 'varchar2', 1)
		);
	</script> 
		<%= Bean.getOperationTitle(
	    		Bean.logisticXML.getfieldTransl("h_promoter_add", false),
				"Y",
				"Y") 
		%>
    <form action="../crm/logistic/clients/promoterupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">
        <input type="hidden" name="action" value="add">
        <input type="hidden" name="process" value="yes">
    	<input type="hidden" name="type" value="general">
    	<input type="hidden" name="cd_lg_promoter_post" value="<%=cd_lg_promoter_post %>">
	<table <%=Bean.getTableDetailParam() %>>
		<tr>
			<td><%= Bean.logisticXML.getfieldTransl("cd_lg_promoter", true) %></td><td><input type="text" name="cd_lg_promoter" size="20" value="" class="inputfield"></td>
 		    <td class="bottom_line"><%= Bean.clubXML.getfieldTransl("club", true) %>
				<%= Bean.getGoToClubLink(club.getValue("ID_CLUB")) %>
			</td>
		  	<td class="bottom_line">
				<%=Bean.getWindowFindClub("club", club.getValue("ID_CLUB"), club.getValue("SNAME_CLUB"), "25") %>
	  		</td>
		</tr>
		<tr>
			<td><%= Bean.logisticXML.getfieldTransl("name_lg_promoter", true) %></td><td><input type="text" name="name_lg_promoter" size="60" value="" class="inputfield"></td>
			<td><%= Bean.logisticXML.getfieldTransl("jur_prs", true) %></td>
			<td>
				<%=Bean.getWindowFindJurPrs("jur_prs", "", "", "ALL", "40") %>
			</td>			
		</tr>
		<tr>
			<td rowspan="3"><%= Bean.logisticXML.getfieldTransl("desc_lg_promoter", false) %></td><td rowspan="3"><textarea name="desc_lg_promoter" cols="56" rows="3" class="inputfield"></textarea></td>
			<td><%= Bean.logisticXML.getfieldTransl("service_place", false) %></td>
			<td>
				<%=Bean.getWindowFindServicePlace("service_place", "", "", "'+document.getElementById('id_jur_prs').value+'", "", "40") %>
			</td>			
		</tr>
		<tr>
			<td><%= Bean.logisticXML.getfieldTransl("cd_lg_promoter_post", true) %></td><td valign="top"><input type="text" name="name_lg_promoter_post" size="50" value="<%=Bean.getLGPromoterPostName(cd_lg_promoter_post) %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td><%= Bean.logisticXML.getfieldTransl("cd_lg_promoter_state", true) %></td><td valign="top"><select name="cd_lg_promoter_state" class="inputfield"><%= Bean.getLogisticPromoterStateOptions("WORKS", true) %></select></td>
		</tr>
		<tr>
			<td><%= Bean.logisticXML.getfieldTransl("phone_mobile", false) %></td><td><input type="text" name="phone_mobile" size="20" value="" class="inputfield"></td>
			<td><%= Bean.logisticXML.getfieldTransl("date_begin_work", true) %></td><td><%=Bean.getCalendarInputField("date_begin_work", "", "10") %></td>
		</tr>
		<tr>
			<% if ("PROMOTER".equalsIgnoreCase(cd_lg_promoter_post)) { %>
			<td><%= Bean.logisticXML.getfieldTransl("id_lg_supervisor", false) %></td><td><%=Bean.getWindowFindLGPromoter("lg_supervisor", "", "", "", "40") %></td>
			<% } else { %>
			<td colspan="2">&nbsp;</td>
			<% } %>
			<td><%= Bean.logisticXML.getfieldTransl("date_end_work", false) %></td><td><%=Bean.getCalendarInputField("date_end_work", "", "10") %></td>
		</tr>
		<tr>
		</tr>
 		<tr>
			<td colspan="6" align="center">
				<%=Bean.getSubmitButtonAjax("../crm/logistic/clients/promoterupdate.jsp") %>
				<%=Bean.getResetButton() %>
				<%=Bean.getGoBackButton("../crm/logistic/clients/promoterupdate.jsp?id=" + id + "&type=general&action=" + actionprev + "&process=no") %>
			</td>
		</tr>

	</table>
	
	<!-- Скрипт для втавки меню вибору дати -->
	<%= Bean.getCalendarScript("date_begin_work", false) %>
	<%= Bean.getCalendarScript("date_end_work", false) %>

</form>

	        <%
    	} else if (action.equalsIgnoreCase("accept")) {
    		
    		bcLGPromoterObject promoter = new bcLGPromoterObject(id);
    		
	        %>
		<script>
			var formData = new Array (
				new Array ('name_jur_prs', 'varchar2', 1),
				new Array ('cd_lg_promoter', 'varchar2', 1),
				new Array ('cd_lg_promoter_post', 'varchar2', 1),
				new Array ('date_promoter_accept', 'varchar2', 1)
			);
		</script> 
			<%= Bean.getOperationTitle(
		    		Bean.logisticXML.getfieldTransl("h_action_promoter_transfer", false),
					"Y",
					"Y") 
			%>
	    <form action="../crm/logistic/clients/promoterupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">
	        <input type="hidden" name="action" value="accept">
	        <input type="hidden" name="process" value="yes">
	    	<input type="hidden" name="type" value="general">
	    	<input type="hidden" name="id" value="<%=id %>">
		<table <%=Bean.getTableDetailParam() %>>
			<tr>
				<td><%= Bean.logisticXML.getfieldTransl("name_lg_promoter", true) %></td><td><input type="text" name="name_lg_promoter" size="50" value="<%= promoter.getValue("NAME_LG_PROMOTER") %>" readonly="readonly" class="inputfield-ro"></td>
				<td><%= Bean.logisticXML.getfieldTransl("cd_lg_promoter", true) %></td><td><input type="text" name="cd_lg_promoter" size="20" value="<%= promoter.getValue("CD_LG_PROMOTER") %>" class="inputfield"></td>
			</tr>
			<tr>
				<td><%= Bean.logisticXML.getfieldTransl("jur_prs", true) %></td>
				<td>
					<%=Bean.getWindowFindJurPrs("jur_prs", promoter.getValue("ID_JUR_PRS"), promoter.getValue("NAME_JUR_PRS"), "ALL", "40") %>
				</td>			
				<td><%= Bean.logisticXML.getfieldTransl("cd_lg_promoter_post", true) %></td><td valign="top"><select name="cd_lg_promoter_post" class="inputfield"><%= Bean.getLogisticPromoterPostOptions("", true) %></select></td>
			</tr>
			<tr>
				<td><%= Bean.logisticXML.getfieldTransl("service_place", false) %></td>
				<td>
					<%=Bean.getWindowFindServicePlace("service_place", promoter.getValue("ID_SERVICE_PLACE"), "", "'+document.getElementById('id_jur_prs').value+'", "", "40") %>
				</td>			
				<td><%= Bean.logisticXML.getfieldTransl("date_promoter_transfer", true) %></td><td><%=Bean.getCalendarInputField("date_promoter_accept", Bean.getSysDate(), "10") %></td>
			</tr>
	 		<tr>
				<td colspan="6" align="center">
					<%=Bean.getSubmitButtonAjax("../crm/logistic/clients/promoterupdate.jsp") %>
					<%=Bean.getResetButton() %>
					<%=Bean.getGoBackButton("../crm/logistic/clients/promoterspecs.jsp?id=" + id) %>
				</td>
			</tr>
	
		</table>
		
		<!-- Скрипт для втавки меню вибору дати -->
		<%= Bean.getCalendarScript("date_promoter_accept", false) %>
	
	</form>

	        <%
    	} else if (action.equalsIgnoreCase("transfer")) {
    		
    		bcLGPromoterObject promoter = new bcLGPromoterObject(id);
    		
	        %>
		<script>
			var formData = new Array (
				new Array ('name_jur_prs', 'varchar2', 1),
				new Array ('cd_lg_promoter', 'varchar2', 1),
				new Array ('cd_lg_promoter_post', 'varchar2', 1),
				new Array ('date_promoter_transfer', 'varchar2', 1)
			);
		</script> 
			<%= Bean.getOperationTitle(
		    		Bean.logisticXML.getfieldTransl("h_action_promoter_transfer", false),
					"Y",
					"Y") 
			%>
	    <form action="../crm/logistic/clients/promoterupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">
	        <input type="hidden" name="action" value="transfer">
	        <input type="hidden" name="process" value="yes">
	    	<input type="hidden" name="type" value="general">
	    	<input type="hidden" name="id" value="<%=id %>">
		<table <%=Bean.getTableDetailParam() %>>
			<tr>
				<td><%= Bean.logisticXML.getfieldTransl("name_lg_promoter", true) %></td><td><input type="text" name="name_lg_promoter" size="50" value="<%= promoter.getValue("NAME_LG_PROMOTER") %>" readonly="readonly" class="inputfield-ro"></td>
				<td><%= Bean.logisticXML.getfieldTransl("cd_lg_promoter", true) %></td><td><input type="text" name="cd_lg_promoter" size="20" value="<%= promoter.getValue("CD_LG_PROMOTER") %>" class="inputfield"></td>
			</tr>
			<tr>
				<td><%= Bean.logisticXML.getfieldTransl("jur_prs", true) %></td>
				<td>
					<%=Bean.getWindowFindJurPrs("jur_prs", promoter.getValue("ID_JUR_PRS"), promoter.getValue("NAME_JUR_PRS"), "ALL", "40") %>
				</td>			
				<td><%= Bean.logisticXML.getfieldTransl("cd_lg_promoter_post", true) %></td><td valign="top"><select name="cd_lg_promoter_post" class="inputfield"><%= Bean.getLogisticPromoterPostOptions("", true) %></select></td>
			</tr>
			<tr>
				<td><%= Bean.logisticXML.getfieldTransl("service_place", false) %></td>
				<td>
					<%=Bean.getWindowFindServicePlace("service_place", promoter.getValue("ID_SERVICE_PLACE"), "", "'+document.getElementById('id_jur_prs').value+'", "", "40") %>
				</td>			
				<td><%= Bean.logisticXML.getfieldTransl("date_promoter_transfer", true) %></td><td><%=Bean.getCalendarInputField("date_promoter_transfer", Bean.getSysDate(), "10") %></td>
			</tr>
	 		<tr>
				<td colspan="6" align="center">
					<%=Bean.getSubmitButtonAjax("../crm/logistic/clients/promoterupdate.jsp") %>
					<%=Bean.getResetButton() %>
					<%=Bean.getGoBackButton("../crm/logistic/clients/promoterspecs.jsp?id=" + id) %>
				</td>
			</tr>
	
		</table>
		
		<!-- Скрипт для втавки меню вибору дати -->
		<%= Bean.getCalendarScript("date_promoter_transfer", false) %>
	
	</form>

	        <%
    	} else if (action.equalsIgnoreCase("dismiss")) {
    		
    		bcLGPromoterObject promoter = new bcLGPromoterObject(id);
    		
	        %>
		<script>
			var formData = new Array (
				new Array ('date_promoter_dismiss', 'varchar2', 1)
			);
		</script> 
			<%= Bean.getOperationTitle(
		    		Bean.logisticXML.getfieldTransl("h_action_promoter_transfer", false),
					"Y",
					"Y") 
			%>
	    <form action="../crm/logistic/clients/promoterupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">
	        <input type="hidden" name="action" value="dismiss">
	        <input type="hidden" name="process" value="yes">
	    	<input type="hidden" name="type" value="general">
	    	<input type="hidden" name="id" value="<%=id %>">
		<table <%=Bean.getTableDetailParam() %>>
			<tr>
				<td width="30%"><%= Bean.logisticXML.getfieldTransl("name_lg_promoter", true) %></td><td><input type="text" name="name_lg_promoter" size="50" value="<%= promoter.getValue("NAME_LG_PROMOTER") %>" readonly="readonly" class="inputfield-ro"></td>
	 		    <td colspan="2">&nbsp;</td>
			</tr>
			<tr>
				<td><%= Bean.logisticXML.getfieldTransl("date_promoter_dismiss", true) %></td><td><%=Bean.getCalendarInputField("date_promoter_dismiss", Bean.getSysDate(), "10") %></td>
	 		    <td colspan="2">&nbsp;</td>
			</tr>
	 		<tr>
				<td colspan="6" align="center">
					<%=Bean.getSubmitButtonAjax("../crm/logistic/clients/promoterupdate.jsp") %>
					<%=Bean.getResetButton() %>
					<%=Bean.getGoBackButton("../crm/logistic/clients/promoterspecs.jsp?id=" + id) %>
				</td>
			</tr>
	
		</table>
		
		<!-- Скрипт для втавки меню вибору дати -->
		<%= Bean.getCalendarScript("date_promoter_dismiss", false) %>
	
	</form>

	        <%
    	} else {
    	    %> <%= Bean.getUnknownActionText(action) %><%
    	}
	
	} else if (process.equalsIgnoreCase("yes"))	{
    	
		String
			cd_lg_promoter				= Bean.getDecodeParam(parameters.get("cd_lg_promoter")),
			name_lg_promoter			= Bean.getDecodeParam(parameters.get("name_lg_promoter")),
			desc_lg_promoter			= Bean.getDecodeParam(parameters.get("desc_lg_promoter")),
			id_jur_prs 					= Bean.getDecodeParam(parameters.get("id_jur_prs")),
			id_service_place 			= Bean.getDecodeParam(parameters.get("id_service_place")),
    		cd_lg_promoter_post			= Bean.getDecodeParam(parameters.get("cd_lg_promoter_post")),
    		date_begin_work 			= Bean.getDecodeParam(parameters.get("date_begin_work")),
    		date_end_work 				= Bean.getDecodeParam(parameters.get("date_end_work")),
    		id_lg_supervisor			= Bean.getDecodeParam(parameters.get("id_lg_supervisor")),
    		phone_mobile 				= Bean.getDecodeParam(parameters.get("phone_mobile")),
    		id_club 					= Bean.getDecodeParam(parameters.get("id_club")),
    		cd_lg_promoter_state 		= Bean.getDecodeParam(parameters.get("cd_lg_promoter_state")),
    		date_promoter_accept 		= Bean.getDecodeParam(parameters.get("date_promoter_accept")),
    		date_promoter_transfer 		= Bean.getDecodeParam(parameters.get("date_promoter_transfer")),
    		date_promoter_dismiss 		= Bean.getDecodeParam(parameters.get("date_promoter_dismiss"));

    	if (action.equalsIgnoreCase("add")) { 
    		String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_LG_CARD.add_promoter(" +
    			"?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";

    		String[] pParam = new String [13];

    		pParam[0] = cd_lg_promoter;
    		pParam[1] = name_lg_promoter;
    		pParam[2] = desc_lg_promoter;
    		pParam[3] = id_jur_prs;
    		pParam[4] = id_service_place;
    		pParam[5] = cd_lg_promoter_post;
    		pParam[6] = cd_lg_promoter_state;
    		pParam[7] = date_begin_work;
    		pParam[8] = date_end_work;
    		pParam[9] = id_lg_supervisor;
    		pParam[10] = phone_mobile;
    		pParam[11] = id_club;
    		pParam[12] = Bean.getDateFormat();
		
		 	%>
			<%= Bean.getCallResultParam("INSERT", callSQL, pParam, "../crm/logistic/clients/promoterspecs.jsp?id=" , "../crm/logistic/clients/promoters.jsp") %>
			<% 	

    	} else if (action.equalsIgnoreCase("remove")) { 
    		
    		String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_LG_CARD.delete_promoter(?,?)}";

			String[] pParam = new String [1];

			pParam[0] = id;

		 	%>
			<%= Bean.getCallResultParam("DELETE", callSQL, pParam, "../crm/logistic/clients/promoters.jsp" , "") %>
			
			<% 	

    	} else if (action.equalsIgnoreCase("edit")) { 
    		
    		String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_LG_CARD.update_promoter(?,?,?,?,?)}";

    		String[] pParam = new String [4];

    		pParam[0] = id;
    		pParam[1] = name_lg_promoter;
    		pParam[2] = desc_lg_promoter;
    		pParam[3] = phone_mobile;
		
		 	%>
			<%= Bean.getCallResultParam("UPDATE", callSQL, pParam, "../crm/logistic/clients/promoterspecs.jsp?id=" + id, "") %>
			
			<% 	

    	} else if (action.equalsIgnoreCase("accept")) { 
    		
    		String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_LG_CARD.update_promoter_accept(?,?,?,?,?,?,?,?)}";

			String[] pParam = new String [7];

			pParam[0] = id;
			pParam[1] = cd_lg_promoter;
			pParam[2] = id_jur_prs;
			pParam[3] = id_service_place;
			pParam[4] = cd_lg_promoter_post;
			pParam[5] = date_promoter_accept;
			pParam[6] = Bean.getDateFormat();
		
		 	%>
			<%= Bean.getCallResultParam("UPDATE", callSQL, pParam, "../crm/logistic/clients/promoterspecs.jsp?id=" + id, "") %>
			
			<% 	

    	} else if (action.equalsIgnoreCase("transfer")) { 
    		
    		String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_LG_CARD.update_promoter_transfer(?,?,?,?,?,?,?,?)}";

			String[] pParam = new String [7];

			pParam[0] = id;
			pParam[1] = cd_lg_promoter;
			pParam[2] = id_jur_prs;
			pParam[3] = id_service_place;
			pParam[4] = cd_lg_promoter_post;
			pParam[5] = date_promoter_accept;
			pParam[6] = Bean.getDateFormat();
		
		 	%>
			<%= Bean.getCallResultParam("UPDATE", callSQL, pParam, "../crm/logistic/clients/promoterspecs.jsp?id=" + id, "") %>
			
			<% 	

    	} else if (action.equalsIgnoreCase("dismiss")) { 
    		
    		String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_LG_CARD.update_promoter_dismiss(?,?,?,?)}";

			String[] pParam = new String [3];

			pParam[0] = id;
			pParam[1] = date_promoter_dismiss;
			pParam[2] = Bean.getDateFormat();
		
		 	%>
			<%= Bean.getCallResultParam("UPDATE", callSQL, pParam, "../crm/logistic/clients/promoterspecs.jsp?id=" + id, "") %>
			
			<% 	

    	} else { %> 
    		<%= Bean.getUnknownActionText(action) %><% 
    	}
	} else {
    	%> <%= Bean.getUnknownProcessText(process) %> <%
	}

} else if (type.equalsIgnoreCase("pay_param")) {
	if (process.equalsIgnoreCase("no"))
		/* вибираємо тип дії (добавити, видалити...)*/
		{
		   /*  --- Добавити запис --- */
	    	if (action.equalsIgnoreCase("add")) { 
	    	
	    	bcLGPromoterObject promoter = new bcLGPromoterObject(id);
	    	
	    	bcCurrencyObject curr = new bcCurrencyObject("980");
	    	
	    	String l_cd_post = promoter.getValue("CD_LG_PROMOTER_POST");
	    	
	    	String id_lg_promoter_work		= Bean.getDecodeParam(parameters.get("id_lg_promoter_work"));
	    	
	    	%>
			<%= Bean.getOperationTitle(
		    		Bean.logisticXML.getfieldTransl("h_action_promoter_pay_param_add", false),
					"N",
					"N") 
			%>
	    <form action="../crm/logistic/clients/promoterupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">
	        <input type="hidden" name="action" value="add">
	        <input type="hidden" name="process" value="yes">
	    	<input type="hidden" name="type" value="pay_param">
	    	<input type="hidden" name="id" value="<%=id %>">
	        <input type="hidden" name="id_lg_promoter_work" value="<%=id_lg_promoter_work %>">
		<table <%=Bean.getTableDetailParam() %>>
			<% if ("CASHIER".equalsIgnoreCase(l_cd_post) ||
	        		"GAS_STATION_OPERATOR".equalsIgnoreCase(l_cd_post) ||
	        		"SELLER".equalsIgnoreCase(l_cd_post)) { %>
			<tr>
				<td><%= Bean.logisticXML.getfieldTransl("name_lg_promoter", false) %></td><td><input type="text" name="name_lg_promoter" size="50" value="<%= promoter.getValue("NAME_LG_PROMOTER") %>" readonly="readonly" class="inputfield-ro"></td>
				<td><%= Bean.logisticXML.getfieldTransl("date_begin_pay_param", true) %></td><td><%=Bean.getCalendarInputField("date_begin_pay_param", Bean.getSysDate(), "10") %></td>
			</tr>
			<tr>
				<td><%= Bean.logisticXML.getfieldTransl("cd_lg_promoter_post", false) %></td><td><input type="text" name="cd_lg_promoter_post" size="50" value="<%= promoter.getValue("NAME_LG_PROMOTER_POST") %>" readonly="readonly" class="inputfield-ro"></td>
				<td><%= Bean.logisticXML.getfieldTransl("cashier_pay_oc_salary_frmt", true) %>, <%= curr.getValue("SNAME_CURRENCY") %></td><td><input type="text" name="cashier_pay_one_card_salary" size="20" value="" class="inputfield"></td>
			</tr>
			<tr>
				<td colspan="2">&nbsp;</td>
				<td><%= Bean.logisticXML.getfieldTransl("cashier_first_card_not_pay", true) %></td><td><input type="text" name="cashier_first_card_not_pay" size="20" value="" class="inputfield"></td>
			</tr>
			<% } else if ("PROMOTER".equalsIgnoreCase(l_cd_post)) { %>
			<tr>
				<td><%= Bean.logisticXML.getfieldTransl("name_lg_promoter", false) %></td><td><input type="text" name="name_lg_promoter" size="50" value="<%= promoter.getValue("NAME_LG_PROMOTER") %>" readonly="readonly" class="inputfield-ro"></td>
				<td><%= Bean.logisticXML.getfieldTransl("date_begin_pay_param", true) %></td><td><%=Bean.getCalendarInputField("date_begin_pay_param", Bean.getSysDate(), "10") %></td>
			</tr>
			<tr>
				<td><%= Bean.logisticXML.getfieldTransl("cd_lg_promoter_post", false) %></td><td><input type="text" name="cd_lg_promoter_post" size="50" value="<%= promoter.getValue("NAME_LG_PROMOTER_POST") %>" readonly="readonly" class="inputfield-ro"></td>
				<td><%= Bean.logisticXML.getfieldTransl("promoter_min_day_salary_frmt", true) %>, <%= curr.getValue("SNAME_CURRENCY") %></td><td><input type="text" name="promoter_min_day_salary" size="20" value="" class="inputfield"></td>
			</tr>
			<tr>
				<td colspan="2">&nbsp;</td>
				<td><%= Bean.logisticXML.getfieldTransl("promoter_cost_oc_less_frmt", true) %>, <%= curr.getValue("SNAME_CURRENCY") %></td><td><input type="text" name="promoter_cost_one_card_less" size="20" value="" class="inputfield"></td>
			</tr>
			<tr>
				<td colspan="2">&nbsp;</td>
				<td><%= Bean.logisticXML.getfieldTransl("promoter_cost_oc_over_frmt", true) %>, <%= curr.getValue("SNAME_CURRENCY") %></td><td><input type="text" name="promoter_cost_one_card_over" size="20" value="" class="inputfield"></td>
			</tr>
			<tr>
				<td colspan="2">&nbsp;</td>
				<td><%= Bean.logisticXML.getfieldTransl("promoter_plan_day_salary_frmt", true) %>, <%= curr.getValue("SNAME_CURRENCY") %></td><td><input type="text" name="promoter_plan_day_salary" size="20" value="" class="inputfield"></td>
			</tr>
			<% } else if ("SUPERVISOR".equalsIgnoreCase(l_cd_post)) { %>
			<tr>
				<td><%= Bean.logisticXML.getfieldTransl("name_lg_promoter", false) %></td><td><input type="text" name="name_lg_promoter" size="50" value="<%= promoter.getValue("NAME_LG_PROMOTER") %>" readonly="readonly" class="inputfield-ro"></td>
				<td><%= Bean.logisticXML.getfieldTransl("date_begin_pay_param", true) %></td><td><%=Bean.getCalendarInputField("date_begin_pay_param", Bean.getSysDate(), "10") %></td>
			</tr>
			<tr>
				<td><%= Bean.logisticXML.getfieldTransl("cd_lg_promoter_post", false) %></td><td><input type="text" name="cd_lg_promoter_post" size="50" value="<%= promoter.getValue("NAME_LG_PROMOTER_POST") %>" readonly="readonly" class="inputfield-ro"></td>
				<td><%= Bean.logisticXML.getfieldTransl("supervisor_month_br_frmt", true) %>, <%= curr.getValue("SNAME_CURRENCY") %></td><td><input type="text" name="supervisor_month_base_rate" size="20" value="" class="inputfield"></td>
			</tr>
			<tr>
				<td colspan="2">&nbsp;</td>
				<td><%= Bean.logisticXML.getfieldTransl("supervisor_cost_oc_overp_frmt", true) %>, <%= curr.getValue("SNAME_CURRENCY") %></td><td><input type="text" name="supervisor_cost_one_card_overp" size="20" value="" class="inputfield"></td>
			</tr>
			<% } %>
	 		<tr>
				<td colspan="6" align="center">
					<%=Bean.getSubmitButtonAjax("../crm/logistic/clients/promoterupdate.jsp") %>
					<%=Bean.getResetButton() %>
					<%=Bean.getGoBackButton("../crm/logistic/clients/promoterspecs.jsp?id=" + id) %>
				</td>
			</tr>

		</table>
		
		<!-- Скрипт для втавки меню вибору дати -->
		<%= Bean.getCalendarScript("date_begin_pay_param", false) %>

	</form>

		        <%
	    	} else {
	    	    %> <%= Bean.getUnknownActionText(action) %><%
	    	}
		
		} else if (process.equalsIgnoreCase("yes"))	{
			String
				id_pay_param					= Bean.getDecodeParam(parameters.get("id_pay_param")),
				id_lg_promoter_work				= Bean.getDecodeParam(parameters.get("id_lg_promoter_work")),
				date_begin_pay_param			= Bean.getDecodeParam(parameters.get("date_begin_pay_param")),
				cd_currency 					= "980",//Bean.getDecodeParam(parameters.get("cd_currency")),
				supervisor_month_base_rate 		= Bean.getDecodeParam(parameters.get("supervisor_month_base_rate")),
				supervisor_cost_one_card_overp	= Bean.getDecodeParam(parameters.get("supervisor_cost_one_card_overp")),
				promoter_min_day_salary 		= Bean.getDecodeParam(parameters.get("promoter_min_day_salary")),
				promoter_cost_one_card_less 	= Bean.getDecodeParam(parameters.get("promoter_cost_one_card_less")),
				promoter_cost_one_card_over		= Bean.getDecodeParam(parameters.get("promoter_cost_one_card_over")),
				promoter_plan_day_salary 		= Bean.getDecodeParam(parameters.get("promoter_plan_day_salary")),
				cashier_pay_one_card_salary 	= Bean.getDecodeParam(parameters.get("cashier_pay_one_card_salary")),
				cashier_first_card_not_pay 		= Bean.getDecodeParam(parameters.get("cashier_first_card_not_pay"));

	    	if (action.equalsIgnoreCase("add")) { 
	    		String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_LG_CARD.add_promoter_pay_param(" +
	    			"?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";

	    		String[] pParam = new String [12];

	    		pParam[0] = id_lg_promoter_work;
	    		pParam[1] = date_begin_pay_param;
	    		pParam[2] = cd_currency;
	    		pParam[3] = supervisor_month_base_rate;
	    		pParam[4] = supervisor_cost_one_card_overp;
	    		pParam[5] = promoter_min_day_salary;
	    		pParam[6] = promoter_cost_one_card_less;
	    		pParam[7] = promoter_cost_one_card_over;
	    		pParam[8] = promoter_plan_day_salary;
	    		pParam[9] = cashier_pay_one_card_salary;
	    		pParam[10] = cashier_first_card_not_pay;
	    		pParam[11] = Bean.getDateFormat();
			
			 	%>
				<%= Bean.getCallResultParam("INSERT", callSQL, pParam, "../crm/logistic/clients/promoterspecs.jsp?id=" + id + "&id_pay_param=", "../crm/logistic/clients/promoterspecs.jsp?id=" + id) %>
				<% 	

	    	} else if (action.equalsIgnoreCase("remove")) { 
	    		
	    		String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_LG_CARD.delete_promoter_pay_param(?,?)}";

	    		String[] pParam = new String [1];

	    		pParam[0] = id_pay_param;

			 	%>
				<%= Bean.getCallResultParam("DELETE", callSQL, pParam, "../crm/logistic/clients/promoterspecs.jsp?id=" + id , "../crm/logistic/clients/promoterspecs.jsp?id=" + id) %>
				
				<% 	

	    	} else { %> 
	    		<%= Bean.getUnknownActionText(action) %><% 
	    	}
		} else {
	    	%> <%= Bean.getUnknownProcessText(process) %> <%
		}

} else if (type.equalsIgnoreCase("supervisor")) {
	if (process.equalsIgnoreCase("no"))
		/* вибираємо тип дії (добавити, видалити...)*/
		{
		   /*  --- Добавити запис --- */
	    	if (action.equalsIgnoreCase("add")) { 
	    	
	    	bcLGPromoterObject promoter = new bcLGPromoterObject(id);
	    	
	    	bcCurrencyObject curr = new bcCurrencyObject("980");
	    	
	    	String l_cd_post = promoter.getValue("CD_LG_PROMOTER_POST");
	    	
	    	String id_lg_promoter_work		= Bean.getDecodeParam(parameters.get("id_lg_promoter_work"));
	    	
	    	%>
			<%= Bean.getOperationTitle(
		    		Bean.logisticXML.getfieldTransl("h_action_promoter_pay_param_add", false),
					"N",
					"N") 
			%>
	    <form action="../crm/logistic/clients/promoterupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">
	        <input type="hidden" name="action" value="add">
	        <input type="hidden" name="process" value="yes">
	    	<input type="hidden" name="type" value="pay_param">
	    	<input type="hidden" name="id" value="<%=id %>">
	        <input type="hidden" name="id_lg_promoter_work" value="<%=id_lg_promoter_work %>">
		<table <%=Bean.getTableDetailParam() %>>
			<% if ("CASHIER".equalsIgnoreCase(l_cd_post) ||
	        		"GAS_STATION_OPERATOR".equalsIgnoreCase(l_cd_post) ||
	        		"SELLER".equalsIgnoreCase(l_cd_post)) { %>
			<tr>
				<td><%= Bean.logisticXML.getfieldTransl("name_lg_promoter", false) %></td><td><input type="text" name="name_lg_promoter" size="50" value="<%= promoter.getValue("NAME_LG_PROMOTER") %>" readonly="readonly" class="inputfield-ro"></td>
				<td><%= Bean.logisticXML.getfieldTransl("date_begin_pay_param", true) %></td><td><%=Bean.getCalendarInputField("date_begin_pay_param", Bean.getSysDate(), "10") %></td>
			</tr>
			<tr>
				<td><%= Bean.logisticXML.getfieldTransl("cd_lg_promoter_post", false) %></td><td><input type="text" name="cd_lg_promoter_post" size="50" value="<%= promoter.getValue("NAME_LG_PROMOTER_POST") %>" readonly="readonly" class="inputfield-ro"></td>
				<td><%= Bean.logisticXML.getfieldTransl("cashier_pay_oc_salary_frmt", true) %>, <%= curr.getValue("SNAME_CURRENCY") %></td><td><input type="text" name="cashier_pay_one_card_salary" size="20" value="" class="inputfield"></td>
			</tr>
			<tr>
				<td colspan="2">&nbsp;</td>
				<td><%= Bean.logisticXML.getfieldTransl("cashier_first_card_not_pay", true) %></td><td><input type="text" name="cashier_first_card_not_pay" size="20" value="" class="inputfield"></td>
			</tr>
			<% } else if ("PROMOTER".equalsIgnoreCase(l_cd_post)) { %>
			<tr>
				<td><%= Bean.logisticXML.getfieldTransl("name_lg_promoter", false) %></td><td><input type="text" name="name_lg_promoter" size="50" value="<%= promoter.getValue("NAME_LG_PROMOTER") %>" readonly="readonly" class="inputfield-ro"></td>
				<td><%= Bean.logisticXML.getfieldTransl("date_begin_pay_param", true) %></td><td><%=Bean.getCalendarInputField("date_begin_pay_param", Bean.getSysDate(), "10") %></td>
			</tr>
			<tr>
				<td><%= Bean.logisticXML.getfieldTransl("cd_lg_promoter_post", false) %></td><td><input type="text" name="cd_lg_promoter_post" size="50" value="<%= promoter.getValue("NAME_LG_PROMOTER_POST") %>" readonly="readonly" class="inputfield-ro"></td>
				<td><%= Bean.logisticXML.getfieldTransl("promoter_min_day_salary_frmt", true) %>, <%= curr.getValue("SNAME_CURRENCY") %></td><td><input type="text" name="promoter_min_day_salary" size="20" value="" class="inputfield"></td>
			</tr>
			<tr>
				<td colspan="2">&nbsp;</td>
				<td><%= Bean.logisticXML.getfieldTransl("promoter_cost_oc_less_frmt", true) %>, <%= curr.getValue("SNAME_CURRENCY") %></td><td><input type="text" name="promoter_cost_one_card_less" size="20" value="" class="inputfield"></td>
			</tr>
			<tr>
				<td colspan="2">&nbsp;</td>
				<td><%= Bean.logisticXML.getfieldTransl("promoter_cost_oc_over_frmt", true) %>, <%= curr.getValue("SNAME_CURRENCY") %></td><td><input type="text" name="promoter_cost_one_card_over" size="20" value="" class="inputfield"></td>
			</tr>
			<tr>
				<td colspan="2">&nbsp;</td>
				<td><%= Bean.logisticXML.getfieldTransl("promoter_plan_day_salary_frmt", true) %>, <%= curr.getValue("SNAME_CURRENCY") %></td><td><input type="text" name="promoter_plan_day_salary" size="20" value="" class="inputfield"></td>
			</tr>
			<% } else if ("SUPERVISOR".equalsIgnoreCase(l_cd_post)) { %>
			<tr>
				<td><%= Bean.logisticXML.getfieldTransl("name_lg_promoter", false) %></td><td><input type="text" name="name_lg_promoter" size="50" value="<%= promoter.getValue("NAME_LG_PROMOTER") %>" readonly="readonly" class="inputfield-ro"></td>
				<td><%= Bean.logisticXML.getfieldTransl("date_begin_pay_param", true) %></td><td><%=Bean.getCalendarInputField("date_begin_pay_param", Bean.getSysDate(), "10") %></td>
			</tr>
			<tr>
				<td><%= Bean.logisticXML.getfieldTransl("cd_lg_promoter_post", false) %></td><td><input type="text" name="cd_lg_promoter_post" size="50" value="<%= promoter.getValue("NAME_LG_PROMOTER_POST") %>" readonly="readonly" class="inputfield-ro"></td>
				<td><%= Bean.logisticXML.getfieldTransl("supervisor_month_br_frmt", true) %>, <%= curr.getValue("SNAME_CURRENCY") %></td><td><input type="text" name="supervisor_month_base_rate" size="20" value="" class="inputfield"></td>
			</tr>
			<tr>
				<td colspan="2">&nbsp;</td>
				<td><%= Bean.logisticXML.getfieldTransl("supervisor_cost_oc_overp_frmt", true) %>, <%= curr.getValue("SNAME_CURRENCY") %></td><td><input type="text" name="supervisor_cost_one_card_overp" size="20" value="" class="inputfield"></td>
			</tr>
			<% } %>
	 		<tr>
				<td colspan="6" align="center">
					<%=Bean.getSubmitButtonAjax("../crm/logistic/clients/promoterupdate.jsp") %>
					<%=Bean.getResetButton() %>
					<%=Bean.getGoBackButton("../crm/logistic/clients/promoterspecs.jsp?id=" + id) %>
				</td>
			</tr>

		</table>
		
		<!-- Скрипт для втавки меню вибору дати -->
		<%= Bean.getCalendarScript("date_begin_pay_param", false) %>

	</form>

		        <%
	    	} else {
	    	    %> <%= Bean.getUnknownActionText(action) %><%
	    	}
		
		} else if (process.equalsIgnoreCase("yes"))	{
			String
				id_pay_param					= Bean.getDecodeParam(parameters.get("id_pay_param")),
				id_lg_promoter_work				= Bean.getDecodeParam(parameters.get("id_lg_promoter_work")),
				date_begin_pay_param			= Bean.getDecodeParam(parameters.get("date_begin_pay_param")),
				cd_currency 					= "980",//Bean.getDecodeParam(parameters.get("cd_currency")),
				supervisor_month_base_rate 		= Bean.getDecodeParam(parameters.get("supervisor_month_base_rate")),
				supervisor_cost_one_card_overp	= Bean.getDecodeParam(parameters.get("supervisor_cost_one_card_overp")),
				promoter_min_day_salary 		= Bean.getDecodeParam(parameters.get("promoter_min_day_salary")),
				promoter_cost_one_card_less 	= Bean.getDecodeParam(parameters.get("promoter_cost_one_card_less")),
				promoter_cost_one_card_over		= Bean.getDecodeParam(parameters.get("promoter_cost_one_card_over")),
				promoter_plan_day_salary 		= Bean.getDecodeParam(parameters.get("promoter_plan_day_salary")),
				cashier_pay_one_card_salary 	= Bean.getDecodeParam(parameters.get("cashier_pay_one_card_salary")),
				cashier_first_card_not_pay 		= Bean.getDecodeParam(parameters.get("cashier_first_card_not_pay"));

	    	if (action.equalsIgnoreCase("add")) { 
	    		String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_LG_CARD.add_promoter_pay_param(" +
	    			"?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";

	    		String[] pParam = new String [12];

	    		pParam[0] = id_lg_promoter_work;
	    		pParam[1] = date_begin_pay_param;
	    		pParam[2] = cd_currency;
	    		pParam[3] = supervisor_month_base_rate;
	    		pParam[4] = supervisor_cost_one_card_overp;
	    		pParam[5] = promoter_min_day_salary;
	    		pParam[6] = promoter_cost_one_card_less;
	    		pParam[7] = promoter_cost_one_card_over;
	    		pParam[8] = promoter_plan_day_salary;
	    		pParam[9] = cashier_pay_one_card_salary;
	    		pParam[10] = cashier_first_card_not_pay;
	    		pParam[11] = Bean.getDateFormat();
			
			 	%>
				<%= Bean.getCallResultParam("INSERT", callSQL, pParam, "../crm/logistic/clients/promoterspecs.jsp?id=" + id + "&id_pay_param=", "../crm/logistic/clients/promoterspecs.jsp?id=" + id) %>
				<% 	

	    	} else if (action.equalsIgnoreCase("remove")) { 
	    		
	    		String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_LG_CARD.delete_promoter_pay_param(?,?)}";

	    		String[] pParam = new String [1];

	    		pParam[0] = id_pay_param;

			 	%>
				<%= Bean.getCallResultParam("DELETE", callSQL, pParam, "../crm/logistic/clients/promoterspecs.jsp?id=" + id , "../crm/logistic/clients/promoterspecs.jsp?id=" + id) %>
				
				<% 	

	    	} else { %> 
	    		<%= Bean.getUnknownActionText(action) %><% 
	    	}
		} else {
	    	%> <%= Bean.getUnknownProcessText(process) %> <%
		}

} else if (type.equalsIgnoreCase("set_schedule")) {
	if (process.equalsIgnoreCase("yes")) {
		if (action.equalsIgnoreCase("set")) {%> 
    
			<%= Bean.form_messageXML.getfieldTransl("processing_update", false) %>
		   	<% 
	
		String[] results = new String[2];
		   	
		ArrayList<String> id_value = new ArrayList<String>();
		//Map<String,String> sp_value = new HashMap<String,String>();
		//Map<String,String> state_value = new HashMap<String,String>();
		Map<String,String> begin_value = new HashMap<String,String>();
		Map<String,String> end_value = new HashMap<String,String>();
		//Map<String,String> cards_value = new HashMap<String,String>();
		Map<String,String> notes_value = new HashMap<String,String>();
	
		String callSQL = "";
	
		Set<String> keySet = parameters.keySet();
		Iterator<String> keySetIterator = keySet.iterator();
		String key = "";
		
		String[] pParam = new String [4];
		
		while(keySetIterator.hasNext()) {
				try{
					key = (String)keySetIterator.next();
					//System.out.println(key + " = '" + parameters.get(key) + "'");
					if(key.contains("id_")){
						id_value.add(key.substring(3));
					}
					//if(key.contains("sp_")){
					//	sp_value.put(key.substring(3), Bean.getDecodeParam(parameters.get(key)));
					//}
					//if(key.contains("state_")){
					//	state_value.put(key.substring(6), Bean.getDecodeParam(parameters.get(key)));
					//}
					if(key.contains("begin_")){
						begin_value.put(key.substring(6), Bean.getDecodeParam(parameters.get(key)));
					}
					if(key.contains("end_")){
						end_value.put(key.substring(4), Bean.getDecodeParam(parameters.get(key)));
					}
					//if(key.contains("cards_")){
					//	cards_value.put(key.substring(6), Bean.getDecodeParam(parameters.get(key)));
					//}
					if(key.contains("notes_")){
						notes_value.put(key.substring(6), Bean.getDecodeParam(parameters.get(key)));
					}
				}
				catch(Exception ex){
					Bean.writeException(
							"../crm/logistic/crm/clients/promoterupdate.jsp",
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
	 			//String sp = sp_value.get(id_value.get(counter));
	 			//String state = state_value.get(id_value.get(counter));
	 			String begin = begin_value.get(id_value.get(counter));
	 			String end = end_value.get(id_value.get(counter));
	 			//String cards = cards_value.get(id_value.get(counter));
	 			String notes = notes_value.get(id_value.get(counter));
	 			
	 			callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_LG_CARD.update_ca_given_schedule_line(?,?,?,?,?)}";
			
        		pParam[0] = id_value.get(counter);
    			pParam[1] = begin;
    			pParam[2] = end;
    			pParam[3] = notes;
		    			
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
	    		"../crm/logistic/clients/promoterspecs.jsp?id=" + id, 
	    		"../crm/logistic/clients/promoterspecs.jsp?id=" + id, 
	    		Bean.form_messageXML.getfieldTransl("save_error", false)) %>
			<% 
		
		} else { %>
			<%= Bean.getUnknownActionText(action) %><% 
		} 
	} else { %>
		<%= Bean.getUnknownProcessText(process) %><% 
	}
} else {
	%> <%= Bean.getUnknownTypeText(type) %> <%
}

%>


</html>
