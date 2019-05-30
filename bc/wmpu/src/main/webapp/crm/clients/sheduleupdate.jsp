<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%@page import="bc.objects.bcLoySheduleObject"%>
<%@page import="bc.objects.bcLoySheduleLineObject"%>
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

String pageFormName = "CLIENTS_SHEDULE";

Bean.setJspPageForTabName(pageFormName);

request.setCharacterEncoding("UTF-8");

String id		= Bean.getDecodeParam(parameters.get("id")); 
String id_line	= Bean.getDecodeParam(parameters.get("id_line"));
String action	= Bean.getDecodeParam(parameters.get("action")); 
String process	= Bean.getDecodeParam(parameters.get("process"));
String type		= Bean.getDecodeParam(parameters.get("type"));

if (id==null || ("".equalsIgnoreCase(id))) id="empty";
if (action==null || ("".equalsIgnoreCase(action))) action="empty";
if (process==null || ("".equalsIgnoreCase(process))) process="empty";
if (type==null || ("".equalsIgnoreCase(process))) process="type";

if ("general".equalsIgnoreCase(type)) {
  if (process.equalsIgnoreCase("no"))
/* вибираємо тип дії (добавити, видалити...)*/
	{ %>
	<script>
		var formData = new Array (
			new Array ('name_shedule', 'varchar2', 1),
			new Array ('name_loyality_scheme_default', 'varchar2', 1),
			new Array ('name_club', 'varchar2', 1),
			new Array ('date_beg', 'varchar2', 1)
		);
	</script> 
	<%
	   /*  --- Добавити запис --- */
    	if (action.equalsIgnoreCase("add") || action.equalsIgnoreCase("add2")) {

    		bcClubShortObject club = new bcClubShortObject(Bean.getCurrentClubID());
    		
    		%> 
			<%= Bean.getOperationTitle(
					Bean.sheduleXML.getfieldTransl("h_shedulelines_add", true),
					"Y",
					"Y") 
			%>
        <form action="../crm/clients/sheduleupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">
			<input type="hidden" name="action" value="add">
	        <input type="hidden" name="process" value="yes">
        	<input type="hidden" name="type" value="general">
		<table <%=Bean.getTableDetailParam() %>>

		<tr>
			<td><%= Bean.sheduleXML.getfieldTransl("name_shedule", true) %> </td> <td><input type="text" name="name_shedule" size="60" value="" class="inputfield"> </td>
 		    <td><%= Bean.clubXML.getfieldTransl("club", true) %>
				<%= Bean.getGoToClubLink(club.getValue("ID_CLUB")) %>
			</td>
		  	<td>
				<%=Bean.getWindowFindClub("club", club.getValue("ID_CLUB"), club.getValue("SNAME_CLUB"), "25") %>
	  		</td>
		</tr>
		<tr>
			<td rowspan="3" valign="top"><%= Bean.sheduleXML.getfieldTransl("desc_shedule", false) %></td><td rowspan="3" valign="top"><textarea name="desc_shedule" cols="57" rows="3" class="inputfield"></textarea></td>
			<td><%= Bean.sheduleXML.getfieldTransl("date_beg", true) %> </td><td><%=Bean.getCalendarInputField("date_beg", Bean.getSysDate(), "10") %></td>
		</tr>
		<tr>
			<td valign="top"><%= Bean.sheduleXML.getfieldTransl("date_end", false) %></td><td valign="top"><%=Bean.getCalendarInputField("date_end", "", "10") %></td>
		</tr>
		<tr>
			<td colspan="2">&nbsp;</td>
		</tr>
		<tr>
			<td><%= Bean.sheduleXML.getfieldTransl("name_loyality_scheme_default", true) %></td>
			<td>
				<%=Bean.getWindowFindLoyScheme("loyality_scheme_default", "", "50", false) %>
			</td>			
		</tr>
		<tr>
			<td colspan="2">&nbsp;</td>
		</tr>
 		<tr>
			<td colspan="6" align="center">
				<%=Bean.getSubmitButtonAjax("../crm/clients/sheduleupdate.jsp") %>
				<%=Bean.getResetButton() %>
				<% if ("add".equalsIgnoreCase(action)) { %>
					<%=Bean.getGoBackButton("../crm/clients/shedule.jsp") %>
				<% } else { %>
					<%=Bean.getGoBackButton("../crm/clients/shedulespecs.jsp?id=" + id) %>
				<% } %>
			</td>
		</tr>

	</table>

	</form>
	<!-- Скрипт для втавки меню вибору дати -->
		<%= Bean.getCalendarScript("date_beg", false) %>
		<%= Bean.getCalendarScript("date_end", false) %>

	        <%
    	} else {
    	    %> <%= Bean.getUnknownActionText(action) %><%
    	}

	} else if (process.equalsIgnoreCase("yes")) {
    
		String
    		shedname_id_shedule 					= Bean.getDecodeParam(parameters.get("id_shedule")),
    		shedname_name_shedule 					= Bean.getDecodeParam(parameters.get("name_shedule")),
    		shedname_desc_shedule 					= Bean.getDecodeParam(parameters.get("desc_shedule")),
    		shedname_date_beg 						= Bean.getDecodeParam(parameters.get("date_beg")),
    		shedname_date_end 						= Bean.getDecodeParam(parameters.get("date_end")),
    		shedname_id_loyality_scheme_default 	= Bean.getDecodeParam(parameters.get("id_loyality_scheme_default")),
    		id_club				    				= Bean.getDecodeParam(parameters.get("id_club"));
    
		ArrayList<String> pParam = new ArrayList<String>();

		if (action.equalsIgnoreCase("add")) { 
       		pParam.add(shedname_name_shedule);
       		pParam.add(shedname_desc_shedule);
       		pParam.add(shedname_id_loyality_scheme_default);
       		pParam.add(shedname_date_beg);
       		pParam.add(shedname_date_end);
       		pParam.add(id_club);
       		pParam.add(Bean.getDateFormat());

   		 	%>
   			<%= Bean.executeInsertFunction("PACK$LOY_SHEDULE_UI.add_shedule", pParam, "../crm/clients/shedulespecs.jsp?id=" , "../crm/clients/shedule.jsp") %>
   			<% 	

		} else if (action.equalsIgnoreCase("remove")) { 
			
			pParam.add(id);

   		 	%>
   			<%= Bean.executeDeleteFunction("PACK$LOY_SHEDULE_UI.delete_shedule", pParam, "../crm/clients/shedule.jsp" , "") %>
   			<% 	

		} else if (action.equalsIgnoreCase("edit")) { 
			pParam.add(id);
           	pParam.add(shedname_name_shedule);
           	pParam.add(shedname_desc_shedule);
           	pParam.add(shedname_id_loyality_scheme_default);
           	pParam.add(shedname_date_beg);
           	pParam.add(shedname_date_end);
           	pParam.add(Bean.getDateFormat());

   		 	%>
   			<%= Bean.executeUpdateFunction("PACK$LOY_SHEDULE_UI.update_shedule", pParam, "../crm/clients/shedulespecs.jsp?id=" + id, "") %>
   			<% 	

		} else { %> 
			<%= Bean.getUnknownActionText(action) %><% 
		}
	} else {%> 
		<%= Bean.getUnknownProcessText(process) %> <%
  	}

//  БЛОК РАБОТЫ СО СТРОКАМИ ГРАФИКА
} else if ("line".equalsIgnoreCase(type)) {
	if (process.equalsIgnoreCase("no"))
		  /* вибираємо тип дії (добавити, видалити...)*/
		  	{
	%>
		<script>
		var formData = new Array (
			new Array ('name_loyality_scheme', 'varchar2', 1),
			new Array ('line', 'varchar2', 1),
			new Array ('date_beg', 'varchar2', 1),
			new Array ('day_time_beg', 'varchar2', 1),
			new Array ('day_time_end', 'varchar2', 1)
		);
	</script> 

	<%
		/*  --- Добавити запис --- */
		if (action.equalsIgnoreCase("add")) {%> 
			<%= Bean.getOperationTitle(
					Bean.shedulelineXML.getfieldTransl("h_shedulelines_add", true),
					"Y",
					"Y") 
			%>
  	        <form action="../crm/clients/sheduleupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">
	  	        <input type="hidden" name="action" value="add">
	  	        <input type="hidden" name="process" value="yes">
	  	        <input type="hidden" name="type" value="line">
	  	        <input type="hidden" name="id" value="<%= id %>">
		  	<table <%=Bean.getTableDetailParam() %>>
	            <tr>
					<td><%= Bean.shedulelineXML.getfieldTransl("name_loyality_scheme", true) %></td>
					<td>
						<%=Bean.getWindowFindLoyScheme("loyality_scheme", "", "30", false) %>
					</td>			
		          	<td><%= Bean.shedulelineXML.getfieldTransl("date_beg", true) %></td> <td><%=Bean.getCalendarInputField("date_beg", Bean.getSysDate(), "10") %></td>
			    </tr>
		        <tr>
			        <td><%= Bean.shedulelineXML.getfieldTransl("line_number", true) %></td><td><input type="text" name="line" size="16" value="" class="inputfield"></td>
			        <td><%= Bean.shedulelineXML.getfieldTransl("date_end", false) %></td> <td><%=Bean.getCalendarInputField("date_end", "", "10") %></td>
			    </tr>
		        <tr>
			  		<td><%= Bean.shedulelineXML.getfieldTransl("day_time_beg", true) %></td> <td> <input type="text" name="day_time_beg" size="16" value="" class="inputfield"> </td>
				  	<td><%= Bean.shedulelineXML.getfieldTransl("exist_flag", true) %></td><td><select name="exist_flag" class="inputfield"><%= Bean.getMeaningFromLookupNameOptions("YES_NO", "Y", false) %></select></td>
		        </tr>
		        <tr>
			  		<td><%= Bean.shedulelineXML.getfieldTransl("day_time_end", true) %></td> <td> <input type="text" name="day_time_end" size="16" value="" class="inputfield"> </td>
			  		<td colspan="2">&nbsp;</td>
		        </tr>
		  		<tr>
					<td colspan="6" class="top_line">
		        		<i><%= Bean.shedulelineXML.getfieldTransl("h_shedulelines_type", true) %></i>
					</td>
				</tr>
	            <tr>
	              	<td><INPUT type="radio" value="DAY_IN_MONTH" name="type_shedule" id="type_shedule1" checked><label class="checbox_label" for="type_shedule1"><%= Bean.shedulelineXML.getfieldTransl("DAY_IN_MONTH_H", false) %></label></td>
		          	<td><%= Bean.shedulelineXML.getfieldTransl("day_in_month", false) %>&nbsp;<input type="text" name="day_in_month" size="16" value="1" class="inputfield"> </td>
		        </tr>
	            <tr>
	              	<td><INPUT type="radio" value="DAYS_INTERVAL" name="type_shedule" name="type_shedule2"><label class="checbox_label" for="type_shedule2"><%= Bean.shedulelineXML.getfieldTransl("DAYS_INTERVAL", false) %></label></td>
		          	<td><%= Bean.shedulelineXML.getfieldTransl("day_every_day_number", false) %>&nbsp;<input type="text" name="day_every_day_number" size="16" value="" class="inputfield"> </td>
		        </tr>
	            <tr>
		          	<td><INPUT type="radio" value="EVERY_DAY" name="type_shedule" id="type_shedule3"><label class="checbox_label" for="type_shedule3"><%= Bean.shedulelineXML.getfieldTransl("EVERY_DAY", false) %></label></td>
		          	<td  colspan="3">
			            <INPUT type="checkbox" value="Y" name="day_week_monday" id="day_week_monday" checked><label class="checbox_label" for="day_week_monday"><%= Bean.shedulelineXML.getfieldTransl("week_monday_s", false) %></label>
			            <INPUT type="checkbox" value="Y" name="day_week_tuesday" id="day_week_tuesday" checked><label class="checbox_label" for="day_week_tuesday"><%= Bean.shedulelineXML.getfieldTransl("week_tuesday_s", false) %></label>
			            <INPUT type="checkbox" value="Y" name="day_week_wednesday" id="day_week_wednesday" checked><label class="checbox_label" for="day_week_wednesday"><%= Bean.shedulelineXML.getfieldTransl("week_wednesday_s", false) %></label>
			            <INPUT type="checkbox" value="Y" name="day_week_thursday" id="day_week_thursday" checked><label class="checbox_label" for="day_week_thursday"><%= Bean.shedulelineXML.getfieldTransl("week_thursday_s", false) %></label>
			            <INPUT type="checkbox" value="Y" name="day_week_friday" id="day_week_friday" checked><label class="checbox_label" for="day_week_friday"><%= Bean.shedulelineXML.getfieldTransl("week_friday_s", false) %></label>
			            <INPUT type="checkbox" value="Y" name="day_week_saturday" id="day_week_saturday" checked><label class="checbox_label" for="day_week_saturday"><%= Bean.shedulelineXML.getfieldTransl("week_saturday_s", false) %></label>
			            <INPUT type="checkbox" value="Y" name="day_week_sunday" id="day_week_sunday" checked><label class="checbox_label" for="day_week_sunday"><%= Bean.shedulelineXML.getfieldTransl("week_sunday_s", false) %></label>
		          	</td>
		        </tr>
	            <tr>
		          	<td><INPUT type="radio" value="EVERY_WEEK" name="type_shedule" id="type_shedule4"><label class="checbox_label" for="type_shedule4"><%= Bean.shedulelineXML.getfieldTransl("EVERY_WEEK", false) %></label></td>
		          	<td  colspan="3">
			            <select name="week_number" class="inputfield"><%= Bean.getMeaningFromLookupNameOrderByNymberValueOptions("WEEK_NUMBER", "", false) %></select>
			            <INPUT type="checkbox" value="Y" name="week_monday" id="week_monday" checked><label class="checbox_label" for="week_monday"><%= Bean.shedulelineXML.getfieldTransl("week_monday_s", false) %></label>
			            <INPUT type="checkbox" value="Y" name="week_tuesday" id="week_tuesday" checked><label class="checbox_label" for="week_tuesday"><%= Bean.shedulelineXML.getfieldTransl("week_tuesday_s", false) %></label>
			            <INPUT type="checkbox" value="Y" name="week_wednesday" id="week_wednesday" checked><label class="checbox_label" for="week_wednesday"><%= Bean.shedulelineXML.getfieldTransl("week_wednesday_s", false) %></label>
			            <INPUT type="checkbox" value="Y" name="week_thursday" id="week_thursday" checked><label class="checbox_label" for="week_thursday"><%= Bean.shedulelineXML.getfieldTransl("week_thursday_s", false) %></label>
			            <INPUT type="checkbox" value="Y" name="week_friday" id="week_friday" checked><label class="checbox_label" for="week_friday"><%= Bean.shedulelineXML.getfieldTransl("week_friday_s", false) %></label>
			            <INPUT type="checkbox" value="Y" name="week_saturday" id="week_saturday" checked><label class="checbox_label" for="week_saturday"><%= Bean.shedulelineXML.getfieldTransl("week_saturday_s", false) %></label>
			            <INPUT type="checkbox" value="Y" name="week_sunday" id="week_sunday" checked><label class="checbox_label" for="week_sunday"><%= Bean.shedulelineXML.getfieldTransl("week_sunday_s", false) %></label>
		          	</td>
		        </tr>
	            <tr>
					<td class="top_line">
		        		<i><%= Bean.shedulelineXML.getfieldTransl("h_months", true) %></i>
					</td>
	              	<td colspan="5" class="top_line">
			            <INPUT type="checkbox" value="Y" name="month_january" id="month_january" checked><label class="checbox_label" for="month_january"><%= Bean.shedulelineXML.getfieldTransl("month_january_s", false) %></label>
			            <INPUT type="checkbox" value="Y" name="month_february" id="month_february" checked><label class="checbox_label" for="month_february"><%= Bean.shedulelineXML.getfieldTransl("month_february_s", false) %></label>
			            <INPUT type="checkbox" value="Y" name="month_march" id="month_march" checked><label class="checbox_label" for="month_march"><%= Bean.shedulelineXML.getfieldTransl("month_march_s", false) %></label>
			            <INPUT type="checkbox" value="Y" name="month_april" id="month_april" checked><label class="checbox_label" for="month_april"><%= Bean.shedulelineXML.getfieldTransl("month_april_s", false) %></label>
			            <INPUT type="checkbox" value="Y" name="month_may" id="month_may" checked><label class="checbox_label" for="month_may"><%= Bean.shedulelineXML.getfieldTransl("month_may_s", false) %></label>
			            <INPUT type="checkbox" value="Y" name="month_june" id="month_june" checked><label class="checbox_label" for="month_june"><%= Bean.shedulelineXML.getfieldTransl("month_june_s", false) %></label>
			            <INPUT type="checkbox" value="Y" name="month_july" id="month_july" checked><label class="checbox_label" for="month_july"><%= Bean.shedulelineXML.getfieldTransl("month_july_s", false) %></label>
			            <INPUT type="checkbox" value="Y" name="month_august" id="month_august" checked><label class="checbox_label" for="month_august"><%= Bean.shedulelineXML.getfieldTransl("month_august_s", false) %></label>
			            <INPUT type="checkbox" value="Y" name="month_september" id="month_september" checked><label class="checbox_label" for="month_september"><%= Bean.shedulelineXML.getfieldTransl("month_september_s", false) %></label>
			            <INPUT type="checkbox" value="Y" name="month_october" id="month_october" checked><label class="checbox_label" for="month_october"><%= Bean.shedulelineXML.getfieldTransl("month_october_s", false) %></label>
			            <INPUT type="checkbox" value="Y" name="month_november" id="month_november" checked><label class="checbox_label" for="month_november"><%= Bean.shedulelineXML.getfieldTransl("month_november_s", false) %></label>
			            <INPUT type="checkbox" value="Y" name="month_december" id="month_december" checked><label class="checbox_label" for="month_december"><%= Bean.shedulelineXML.getfieldTransl("month_december_s", false) %></label>
		          	</td>
		        </tr>
	            <tr>
		  			<td colspan="4" align="center">
						<%=Bean.getSubmitButtonAjax("../crm/clients/sheduleupdate.jsp") %>
						<%=Bean.getResetButton() %>
						<%=Bean.getGoBackButton("../crm/clients/shedulespecs.jsp?id=" + id) %>
		  			</td>
		  		</tr>

		  	</table>

		  </form>
	<!-- Скрипт для втавки меню вибору дати -->
		<%= Bean.getCalendarScript("date_beg", false) %>
		<%= Bean.getCalendarScript("date_end", false) %>

        <%
		} else if (action.equalsIgnoreCase("edit")) {
		      		
			bcLoySheduleLineObject sheduleLine = new bcLoySheduleLineObject(id_line);
		      		
		    %> 
			<%= Bean.getOperationTitle(
					Bean.shedulelineXML.getfieldTransl("h_shedulelines_edit", true),
					"Y",
					"Y") 
			%>
  	        <form action="../crm/clients/sheduleupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">
	  	        <input type="hidden" name="action" value="edit">
	  	        <input type="hidden" name="process" value="yes">
	  	        <input type="hidden" name="type" value="line">
	  	        <input type="hidden" name="id" value="<%= id %>">
	  	        <input type="hidden" name="id_line" value="<%= id_line %>">
		  	<table <%=Bean.getTableDetailParam() %>>
	            <tr>
					<td><%= Bean.shedulelineXML.getfieldTransl("name_loyality_scheme", true) %>
						<%= Bean.getGoToLoyalityLink(sheduleLine.getValue("ID_LOYALITY_SCHEME")) %>
					</td>
					<td>
						<%=Bean.getWindowFindLoyScheme("loyality_scheme", sheduleLine.getValue("ID_LOYALITY_SCHEME"), "30", false) %>
					</td>			
		          	<td><%= Bean.shedulelineXML.getfieldTransl("date_beg", true) %></td> <td><%=Bean.getCalendarInputField("date_beg", sheduleLine.getValue("DATE_BEG_FRMT"), "10") %></td>
		        </tr>
	            <tr>
		          	<td><%= Bean.shedulelineXML.getfieldTransl("line_number", true) %></td><td><input type="text" name="line_number" size="16" value="<%= sheduleLine.getValue("LINE_NUMBER") %>" class="inputfield"></td>
		          	<td><%= Bean.shedulelineXML.getfieldTransl("date_end", false) %></td> <td><%=Bean.getCalendarInputField("date_end", sheduleLine.getValue("DATE_END_FRMT"), "10") %></td>
		        </tr>
	            <tr>
		  	 	  	<td><%= Bean.shedulelineXML.getfieldTransl("day_time_beg", true) %></td> <td> <input type="text" name="day_time_beg" size="16" value="<%= sheduleLine.getValue("DAY_TIME_BEG") %>" class="inputfield"> </td>
				  	<td><%= Bean.shedulelineXML.getfieldTransl("exist_flag", true) %></td><td><select name="exist_flag" class="inputfield"><%= Bean.getMeaningFromLookupNameOptions("YES_NO", sheduleLine.getValue("EXIST_FLAG"), false) %></select></td>
		        </tr>
	            <tr>
		  		  	<td><%= Bean.shedulelineXML.getfieldTransl("day_time_end", true) %></td> <td> <input type="text" name="day_time_end" size="16" value="<%= sheduleLine.getValue("DAY_TIME_END") %>" class="inputfield"> </td>
				  	<td colspan="2">&nbsp;</td>
		        </tr>
		  		<tr>
					<td colspan="6" class="top_line">
		        		<i><%= Bean.shedulelineXML.getfieldTransl("h_shedulelines_type", true) %></i>
					</td>
				</tr>
	            <tr>
		        	<td><INPUT type="radio" value="DAY_IN_MONTH" name="type_shedule" id="type_shedule1" <% if (sheduleLine.getValue("TYPE_SHEDULE").equalsIgnoreCase("day_in_month")) {%> checked <% } %> ><label class="checbox_label" for="type_shedule1"><%= Bean.shedulelineXML.getfieldTransl("DAY_IN_MONTH_H", false) %></label></td>
			        <td><%= Bean.shedulelineXML.getfieldTransl("day_in_month", false) %>&nbsp;<input type="text" name="day_in_month" size="16" value="<%= sheduleLine.getValue("DAY_IN_MONTH") %>" class="inputfield"> </td>
		        </tr>
	            <tr>
	              	<td><INPUT type="radio" value="DAYS_INTERVAL" name="type_shedule" id="type_shedule2" <% if (sheduleLine.getValue("TYPE_SHEDULE").equalsIgnoreCase("DAYS_INTERVAL")) {%> checked <% } %> ><label class="checbox_label" for="type_shedule2"><%= Bean.shedulelineXML.getfieldTransl("DAYS_INTERVAL", false) %></label></td>
		          	<td><%= Bean.shedulelineXML.getfieldTransl("day_every_day_number", false) %>&nbsp;<input type="text" name="day_every_day_number" size="16" value="<%= sheduleLine.getValue("DAY_EVERY_DAY_NUMBER") %>" class="inputfield"> </td>
		        </tr>
	            <tr>
			        <td><INPUT type="radio" value="EVERY_DAY" name="type_shedule" id="type_shedule3" <% if (sheduleLine.getValue("TYPE_SHEDULE").equalsIgnoreCase("every_day")) {%> checked <% } %> ><label class="checbox_label" for="type_shedule3"><%= Bean.shedulelineXML.getfieldTransl("EVERY_DAY", false) %></label></td>
			        <td colspan="5">
			            <INPUT type="checkbox" value="" name="day_week_monday" id="day_week_monday" <% if ("Y".equalsIgnoreCase(sheduleLine.getValue("WEEK_MONDAY")) && "EVERY_DAY".equalsIgnoreCase(sheduleLine.getValue("TYPE_SHEDULE"))) { %> checked <% } %> ><label class="checbox_label" for="day_week_monday"><%= Bean.shedulelineXML.getfieldTransl("week_monday_s", false) %></label>
			            <INPUT type="checkbox" value="" name="day_week_tuesday" id="day_week_tuesday" <% if ("Y".equalsIgnoreCase(sheduleLine.getValue("WEEK_TUESDAY")) && "EVERY_DAY".equalsIgnoreCase(sheduleLine.getValue("TYPE_SHEDULE"))) {%> checked <% } %>><label class="checbox_label" for="day_week_tuesday"><%= Bean.shedulelineXML.getfieldTransl("week_tuesday_s", false) %></label>
			            <INPUT type="checkbox" value="" name="day_week_wednesday" id="day_week_wednesday" <% if ("Y".equalsIgnoreCase(sheduleLine.getValue("WEEK_WEDNESDAY")) && "EVERY_DAY".equalsIgnoreCase(sheduleLine.getValue("TYPE_SHEDULE"))) {%> checked <% } %>><label class="checbox_label" for="day_week_wednesday"><%= Bean.shedulelineXML.getfieldTransl("week_wednesday_s", false) %></label>
			            <INPUT type="checkbox" value="" name="day_week_thursday" id="day_week_thursday" <% if ("Y".equalsIgnoreCase(sheduleLine.getValue("WEEK_THURSDAY")) && "EVERY_DAY".equalsIgnoreCase(sheduleLine.getValue("TYPE_SHEDULE"))) {%> checked <% } %>><label class="checbox_label" for="day_week_thursday"><%= Bean.shedulelineXML.getfieldTransl("week_thursday_s", false) %></label>
			            <INPUT type="checkbox" value="" name="day_week_friday" id="day_week_friday" <% if ("Y".equalsIgnoreCase(sheduleLine.getValue("WEEK_FRIDAY")) && "EVERY_DAY".equalsIgnoreCase(sheduleLine.getValue("TYPE_SHEDULE"))) {%> checked <% } %>><label class="checbox_label" for="day_week_friday"><%= Bean.shedulelineXML.getfieldTransl("week_friday_s", false) %></label>
			            <INPUT type="checkbox" value="" name="day_week_saturday" id="day_week_saturday" <% if ("Y".equalsIgnoreCase(sheduleLine.getValue("WEEK_SATURDAY")) && "EVERY_DAY".equalsIgnoreCase(sheduleLine.getValue("TYPE_SHEDULE"))) {%> checked <% } %>><label class="checbox_label" for="day_week_saturday"><%= Bean.shedulelineXML.getfieldTransl("week_saturday_s", false) %></label>
			            <INPUT type="checkbox" value="" name="day_week_sunday" id="day_week_sunday" <% if ("Y".equalsIgnoreCase(sheduleLine.getValue("WEEK_SUNDAY")) && "EVERY_DAY".equalsIgnoreCase(sheduleLine.getValue("TYPE_SHEDULE"))) {%> checked <% } %>><label class="checbox_label" for="day_week_sunday"><%= Bean.shedulelineXML.getfieldTransl("week_sunday_s", false) %></label>
		          	</td>
		        </tr>
	            <tr>
		          	<td><INPUT type="radio" value="EVERY_WEEK" name="type_shedule" id="type_shedule4" <% if (sheduleLine.getValue("TYPE_SHEDULE").equalsIgnoreCase("EVERY_WEEK")) {%> checked <% } %> ><label class="checbox_label" for="type_shedule4"><%= Bean.shedulelineXML.getfieldTransl("EVERY_WEEK", false) %></label></td>
		          	<td colspan="5">
			            <select name="week_number" class="inputfield"><%= Bean.getMeaningFromLookupNameOrderByNymberValueOptions("WEEK_NUMBER", sheduleLine.getValue("WEEK_NUMBER"), false) %></select>
			            <INPUT type="checkbox" value="" name="week_monday" id="week_monday" <% if ("Y".equalsIgnoreCase(sheduleLine.getValue("WEEK_MONDAY")) && sheduleLine.getValue("TYPE_SHEDULE").equalsIgnoreCase("EVERY_WEEK")) {%> checked <% } %>><label class="checbox_label" for="week_monday"><%= Bean.shedulelineXML.getfieldTransl("week_monday_s", false) %></label>
			            <INPUT type="checkbox" value="" name="week_tuesday" id="week_tuesday" <% if ("Y".equalsIgnoreCase(sheduleLine.getValue("WEEK_TUESDAY")) && sheduleLine.getValue("TYPE_SHEDULE").equalsIgnoreCase("EVERY_WEEK")) {%> checked <% } %>><label class="checbox_label" for="week_tuesday"><%= Bean.shedulelineXML.getfieldTransl("week_tuesday_s", false) %></label>
			            <INPUT type="checkbox" value="" name="week_wednesday" id="week_wednesday" <% if ("Y".equalsIgnoreCase(sheduleLine.getValue("WEEK_WEDNESDAY")) && sheduleLine.getValue("TYPE_SHEDULE").equalsIgnoreCase("EVERY_WEEK")) {%> checked <% } %>><label class="checbox_label" for="week_wednesday"><%= Bean.shedulelineXML.getfieldTransl("week_wednesday_s", false) %></label>
			            <INPUT type="checkbox" value="" name="week_thursday" id="week_thursday" <% if ("Y".equalsIgnoreCase(sheduleLine.getValue("WEEK_THURSDAY")) && sheduleLine.getValue("TYPE_SHEDULE").equalsIgnoreCase("EVERY_WEEK")) {%> checked <% } %>><label class="checbox_label" for="week_thursday"><%= Bean.shedulelineXML.getfieldTransl("week_thursday_s", false) %></label>
			            <INPUT type="checkbox" value="" name="week_friday" id="week_friday" <% if ("Y".equalsIgnoreCase(sheduleLine.getValue("WEEK_FRIDAY")) && sheduleLine.getValue("TYPE_SHEDULE").equalsIgnoreCase("EVERY_WEEK")) {%> checked <% } %>><label class="checbox_label" for="week_friday"><%= Bean.shedulelineXML.getfieldTransl("week_friday_s", false) %></label>
			            <INPUT type="checkbox" value="" name="week_saturday" id="week_saturday" <% if ("Y".equalsIgnoreCase(sheduleLine.getValue("WEEK_SATURDAY")) && sheduleLine.getValue("TYPE_SHEDULE").equalsIgnoreCase("EVERY_WEEK")) {%> checked <% } %>><label class="checbox_label" for="week_saturday"><%= Bean.shedulelineXML.getfieldTransl("week_saturday_s", false) %></label>
			            <INPUT type="checkbox" value="" name="week_sunday" id="week_sunday" <% if ("Y".equalsIgnoreCase(sheduleLine.getValue("WEEK_SUNDAY")) && sheduleLine.getValue("TYPE_SHEDULE").equalsIgnoreCase("EVERY_WEEK")) {%> checked <% } %>><label class="checbox_label" for="week_sunday"><%= Bean.shedulelineXML.getfieldTransl("week_sunday_s", false) %></label>
		          	</td>
		        </tr>
	            <tr>
					<td class="top_line">
		        		<i><%= Bean.shedulelineXML.getfieldTransl("h_months", true) %></i>
					</td>
	              	<td colspan="5" class="top_line">
			            <INPUT type="checkbox" value="" name="month_january" id="month_january" <% if (sheduleLine.getValue("MONTH_JANUARY").equalsIgnoreCase("Y")) {%> checked <% } %>><label class="checbox_label" for="month_january"><%= Bean.shedulelineXML.getfieldTransl("month_january_s", false) %></label>
			            <INPUT type="checkbox" value="" name="month_february" id="month_february" <% if (sheduleLine.getValue("MONTH_FEBRUARY").equalsIgnoreCase("Y")) {%> checked <% } %>><label class="checbox_label" for="month_february"><%= Bean.shedulelineXML.getfieldTransl("month_february_s", false) %></label>
			            <INPUT type="checkbox" value="" name="month_march" id="month_march" <% if (sheduleLine.getValue("MONTH_MARCH").equalsIgnoreCase("Y")) {%> checked <% } %>><label class="checbox_label" for="month_march"><%= Bean.shedulelineXML.getfieldTransl("month_march_s", false) %></label>
			            <INPUT type="checkbox" value="" name="month_april" id="month_april" <% if (sheduleLine.getValue("MONTH_APRIL").equalsIgnoreCase("Y")) {%> checked <% } %>><label class="checbox_label" for="month_april"><%= Bean.shedulelineXML.getfieldTransl("month_april_s", false) %></label>
			            <INPUT type="checkbox" value="" name="month_may" id="month_may" <% if (sheduleLine.getValue("MONTH_MAY").equalsIgnoreCase("Y")) {%> checked <% } %>><label class="checbox_label" for="month_may"><%= Bean.shedulelineXML.getfieldTransl("month_may_s", false) %></label>
			            <INPUT type="checkbox" value="" name="month_june" id="month_june" <% if (sheduleLine.getValue("MONTH_JUNE").equalsIgnoreCase("Y")) {%> checked <% } %>><label class="checbox_label" for="month_june"><%= Bean.shedulelineXML.getfieldTransl("month_june_s", false) %></label>
			            <INPUT type="checkbox" value="" name="month_july" id="month_july" <% if (sheduleLine.getValue("MONTH_JULY").equalsIgnoreCase("Y")) {%> checked <% } %>><label class="checbox_label" for="month_july"><%= Bean.shedulelineXML.getfieldTransl("month_july_s", false) %></label>
			            <INPUT type="checkbox" value="" name="month_august" id="month_august" <% if (sheduleLine.getValue("MONTH_AUGUST").equalsIgnoreCase("Y")) {%> checked <% } %>><label class="checbox_label" for="month_august"><%= Bean.shedulelineXML.getfieldTransl("month_august_s", false) %></label>
			            <INPUT type="checkbox" value="" name="month_september" id="month_september" <% if (sheduleLine.getValue("MONTH_SEPTEMBER").equalsIgnoreCase("Y")) {%> checked <% } %>><label class="checbox_label" for="month_september"><%= Bean.shedulelineXML.getfieldTransl("month_september_s", false) %></label>
			            <INPUT type="checkbox" value="" name="month_october" id="month_october" <% if (sheduleLine.getValue("MONTH_OCTOBER").equalsIgnoreCase("Y")) {%> checked <% } %>><label class="checbox_label" for="month_october"><%= Bean.shedulelineXML.getfieldTransl("month_october_s", false) %></label>
			            <INPUT type="checkbox" value="" name="month_november" id="month_november" <% if (sheduleLine.getValue("MONTH_NOVEMBER").equalsIgnoreCase("Y")) {%> checked <% } %>><label class="checbox_label" for="month_november"><%= Bean.shedulelineXML.getfieldTransl("month_november_s", false) %></label>
			            <INPUT type="checkbox" value="" name="month_december" id="month_december" <% if (sheduleLine.getValue("MONTH_DECEMBER").equalsIgnoreCase("Y")) {%> checked <% } %>><label class="checbox_label" for="month_december"><%= Bean.shedulelineXML.getfieldTransl("month_december_s", false) %></label>
		          	</td>
		        </tr>
				<%=	Bean.getCreationAndMoficationRecordFields(
						sheduleLine.getValue(Bean.getCreationDateFieldName()),
						sheduleLine.getValue("CREATED_BY"),
						sheduleLine.getValue(Bean.getLastUpdateDateFieldName()),
						sheduleLine.getValue("LAST_UPDATE_BY")
					) %>
	            <tr>
		  			<td colspan="8" align="center">
						<%=Bean.getSubmitButtonAjax("../crm/clients/sheduleupdate.jsp") %>
						<%=Bean.getResetButton() %>
						<%=Bean.getGoBackButton("../crm/clients/shedulespecs.jsp?id=" + id) %>
		  			</td>
		  		</tr>

		  	</table>

		  </form>
	<!-- Скрипт для втавки меню вибору дати -->
		<%= Bean.getCalendarScript("date_beg", false) %>
		<%= Bean.getCalendarScript("date_end", false) %>
		  
		  <br><%
	   	} else {
	   	    %> <%= Bean.getUnknownActionText(action) %><%
	   	}
	} else if (process.equalsIgnoreCase("yes"))	{
		      
		String
		      shedule_id_shedule 				= id,
		      shedule_id_loyality_scheme 		= Bean.getDecodeParamPrepare(parameters.get("id_loyality_scheme")),
		      shedule_name_loyality_scheme 		= Bean.getDecodeParamPrepare(parameters.get("name_loyality_scheme")),
		      shedule_line_number 				= Bean.getDecodeParamPrepare(parameters.get("line_number")),
		      shedule_line_number_old 			= Bean.getDecodeParamPrepare(parameters.get("line")),
		      shedule_date_beg 					= Bean.getDecodeParamPrepare(parameters.get("date_beg")),
		      shedule_date_end 					= Bean.getDecodeParamPrepare(parameters.get("date_end")),
		      shedule_type_shedule 				= Bean.getDecodeParamPrepare(parameters.get("type_shedule")),
		      shedule_day_time_beg 				= Bean.getDecodeParamPrepare(parameters.get("day_time_beg")),
		      shedule_day_time_end 				= Bean.getDecodeParamPrepare(parameters.get("day_time_end")),
		      shedule_day_in_month 				= Bean.getDecodeParamPrepare(parameters.get("day_in_month")),
		      shedule_day_every_day_number 		= Bean.getDecodeParamPrepare(parameters.get("day_every_day_number")),
		      shedule_week_number 				= Bean.getDecodeParamPrepare(parameters.get("week_number")),
		      shedule_week_monday 				= Bean.getDecodeParamPrepare(parameters.get("week_monday")),
		      shedule_week_tuesday 				= Bean.getDecodeParamPrepare(parameters.get("week_tuesday")),
		      shedule_week_wednesday 			= Bean.getDecodeParamPrepare(parameters.get("week_wednesday")),
		      shedule_week_thursday 			= Bean.getDecodeParamPrepare(parameters.get("week_thursday")),
		      shedule_week_friday 				= Bean.getDecodeParamPrepare(parameters.get("week_friday")),
		      shedule_week_saturday 			= Bean.getDecodeParamPrepare(parameters.get("week_saturday")),
		      shedule_week_sunday 				= Bean.getDecodeParamPrepare(parameters.get("week_sunday")),
		      shedule_day_week_monday 			= Bean.getDecodeParamPrepare(parameters.get("day_week_monday")),
		      shedule_day_week_tuesday 			= Bean.getDecodeParamPrepare(parameters.get("day_week_tuesday")),
		      shedule_day_week_wednesday 		= Bean.getDecodeParamPrepare(parameters.get("day_week_wednesday")),
		      shedule_day_week_thursday 		= Bean.getDecodeParamPrepare(parameters.get("day_week_thursday")),
		      shedule_day_week_friday 			= Bean.getDecodeParamPrepare(parameters.get("day_week_friday")),
		      shedule_day_week_saturday 		= Bean.getDecodeParamPrepare(parameters.get("day_week_saturday")),
		      shedule_day_week_sunday 			= Bean.getDecodeParamPrepare(parameters.get("day_week_sunday")),
              shedule_month_january 			= Bean.getDecodeParamPrepare(parameters.get("month_january")),
              shedule_month_february 			= Bean.getDecodeParamPrepare(parameters.get("month_february")),
              shedule_month_march 				= Bean.getDecodeParamPrepare(parameters.get("month_march")),
              shedule_month_april 				= Bean.getDecodeParamPrepare(parameters.get("month_april")),
              shedule_month_may 				= Bean.getDecodeParamPrepare(parameters.get("month_may")),
              shedule_month_june 				= Bean.getDecodeParamPrepare(parameters.get("month_june")),
              shedule_month_july 				= Bean.getDecodeParamPrepare(parameters.get("month_july")),
              shedule_month_august 				= Bean.getDecodeParamPrepare(parameters.get("month_august")),
              shedule_month_september 			= Bean.getDecodeParamPrepare(parameters.get("month_september")),
              shedule_month_october 			= Bean.getDecodeParamPrepare(parameters.get("month_october")),
              shedule_month_november 			= Bean.getDecodeParamPrepare(parameters.get("month_november")),
              shedule_month_december 			= Bean.getDecodeParamPrepare(parameters.get("month_december")),
              shedule_exist_flag 				= Bean.getDecodeParamPrepare(parameters.get("exist_flag"));
		
		String week_monday 	= "";
		String week_tuesday 	= "";
		String week_wednesday = "";
		String week_thursday 	= "";
		String week_friday 	= "";
		String week_saturday 	= "";
		String week_sunday 	= "";
		
		if ("EVERY_DAY".equalsIgnoreCase(shedule_type_shedule)) {
			week_monday 	= shedule_day_week_monday;
			week_tuesday 	= shedule_day_week_tuesday;
			week_wednesday 	= shedule_day_week_wednesday;
			week_thursday 	= shedule_day_week_thursday;
			week_friday 	= shedule_day_week_friday;
			week_saturday 	= shedule_day_week_saturday;
			week_sunday 	= shedule_day_week_sunday;
		} else if ("EVERY_WEEK".equalsIgnoreCase(shedule_type_shedule)) {
			week_monday 	= shedule_week_monday;
			week_tuesday 	= shedule_week_tuesday;
			week_wednesday 	= shedule_week_wednesday;
			week_thursday 	= shedule_week_thursday;
			week_friday 	= shedule_week_friday;
			week_saturday 	= shedule_week_saturday;
			week_sunday 	= shedule_week_sunday;
		}
		
		ArrayList<String> pParam = new ArrayList<String>();
		
		if (action.equalsIgnoreCase("add")) { 
			pParam.add(shedule_id_shedule);
	       	pParam.add(shedule_line_number_old);
	       	pParam.add(shedule_id_loyality_scheme);
	       	pParam.add(shedule_date_beg);		
	       	pParam.add(shedule_date_end);
	       	pParam.add(shedule_type_shedule);
	       	pParam.add(shedule_day_time_beg);
	       	pParam.add(shedule_day_time_end);
	       	pParam.add(shedule_day_in_month);
	       	pParam.add(shedule_day_every_day_number);	
	       	pParam.add(shedule_week_number);
	       	pParam.add(week_monday);
	       	pParam.add(week_tuesday);
	       	pParam.add(week_wednesday);		
	       	pParam.add(week_thursday);
	       	pParam.add(week_friday);
	       	pParam.add(week_saturday);
	       	pParam.add(week_sunday);   			
	       	pParam.add(shedule_month_january);
	       	pParam.add(shedule_month_february);	
	       	pParam.add(shedule_month_march);
	       	pParam.add(shedule_month_april);
	       	pParam.add(shedule_month_may);
	       	pParam.add(shedule_month_june);		
	       	pParam.add(shedule_month_july);
	       	pParam.add(shedule_month_august);
	       	pParam.add(shedule_month_september);
	       	pParam.add(shedule_month_october);
	       	pParam.add(shedule_month_november);
	       	pParam.add(shedule_month_december);
	    	pParam.add(shedule_exist_flag);
	       	pParam.add(Bean.getDateFormat());	

   		 	%>
   			<%= Bean.executeInsertFunction("PACK$LOY_SHEDULE_UI.add_shedule_line", pParam, "../crm/clients/shedulespecs.jsp?id=" + shedule_id_shedule + "&id_line=", "") %>
   			<% 	

		} else if (action.equalsIgnoreCase("remove")) { 
			
			pParam.add(id_line);

   		 	%>
   			<%= Bean.executeDeleteFunction("PACK$LOY_SHEDULE_UI.delete_shedule_line", pParam, "../crm/clients/shedulespecs.jsp?id=" + id, "") %>
   			<% 	

		} else if (action.equalsIgnoreCase("edit")) {
			
			pParam.add(id_line);
		    pParam.add(shedule_line_number);
		    pParam.add(shedule_id_loyality_scheme);
		    pParam.add(shedule_date_beg);		
		    pParam.add(shedule_date_end);
		    pParam.add(shedule_type_shedule);
		    pParam.add(shedule_day_time_beg);
		    pParam.add(shedule_day_time_end);
		    pParam.add(shedule_day_in_month);
		    pParam.add(shedule_day_every_day_number);	
		    pParam.add(shedule_week_number);
		    pParam.add(week_monday);
		    pParam.add(week_tuesday);
		    pParam.add(week_wednesday);		
		    pParam.add(week_thursday);
		    pParam.add(week_friday);
		    pParam.add(week_saturday);
		    pParam.add(week_sunday);   			
		    pParam.add(shedule_month_january);
		    pParam.add(shedule_month_february);	
		    pParam.add(shedule_month_march);
		    pParam.add(shedule_month_april);
		    pParam.add(shedule_month_may);
		    pParam.add(shedule_month_june);		
		    pParam.add(shedule_month_july);
		    pParam.add(shedule_month_august);
		    pParam.add(shedule_month_september);
		    pParam.add(shedule_month_october);
		    pParam.add(shedule_month_november);
		    pParam.add(shedule_month_december);
		    pParam.add(shedule_exist_flag);
		    pParam.add(Bean.getDateFormat());	

   		 	%>
   			<%= Bean.executeUpdateFunction("PACK$LOY_SHEDULE_UI.update_shedule_line", pParam, "../crm/clients/shedulespecs.jsp?id=" + id, "") %>
   			<% 	

		} else { %> 
			<%= Bean.getUnknownActionText(action) %><% 
		}
	} else {%> 
		<%= Bean.getUnknownProcessText(process) %> <%
	}
	
}  else {
    %><%= Bean.getUnknownTypeText(type) %><%
}
	
%>


</body>
</html>
